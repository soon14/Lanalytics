package com.ai.analy.system.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ai.analy.cache.impl.RedisCacheClient;
import com.ai.analy.cache.impl.RedisDowntime;
import com.ai.analy.cache.util.JSONValidator;
import com.ai.analy.config.ConfigException;
import com.ai.analy.config.ConfigurationCenter;
import com.ai.analy.config.ConfigurationWatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionManager implements ConfigurationWatcher {
	private Logger log = Logger.getLogger(SessionManager.class);
	private static final String SESSION_ID_PREFIX = "R_JSID_";
	//该值不作为常量使用，修改为从ZK中获取配置，用于处理不同应用在共用域名的时候，导致的问题；
	//private static final String SESSION_ID_COOKIE = "WOEGO_JSESSIONID";
	private static final String EXPIRATIONUPDATEINTERVAL_KEY = "expirationUpdateInterval";
	private static final String MAXINACTIVEINTERVAL_KEY = "maxInactiveInterval";
	private static final String HOST_KEY = "host";
	private static final String PORT_KEY = "port";
	private static final String TIMEOUT_KEY = "timeOut";
	private static final String MAXACTIVE_KEY = "maxActive";
	private static final String MAXIDLE_KEY = "maxIdle";
	private static final String MAXWAIT_KEY = "maxWait";
	private static final String TESTONBORROW_KEY = "testOnBorrow";
	private static final String TESTONRETURN_KEY = "testOnReturn";
	private static final String DBINDEX_KEY = "dbIndex";
	private static final String TWEMPROXY_KEY = "twemproxy";
	private static final String DOMAIN_KEY = "domain";
	private static final String COOKIE_SESSION_ID = "cookieSessionId";
	private String host = null;
	private String port = null;
	private String timeOut = null;
	private String maxActive = null;
	private String maxIdle = null;
	private String maxWait = null;
	private String testOnBorrow = null;
	private String testOnReturn = null;
	private RedisCacheClient cacheClient = null;
	private int dbIndex = 0;
	private String twemproxy = null;
	private int expirationUpdateInterval = 300;
	private int maxInactiveInterval = 1800;
	private String domain = "";
	//设置sessionId 在 cookie 中的默认值；
	private String cookieSessionId = "ANALY_JSESSIONID";

	private String confPath = "";
	private ConfigurationCenter confCenter = null;
	//redis宕机后，session存放在本地
	private Map<String,HttpSession> localSessions = new HashMap<String,HttpSession>();
	//redis宕机标识
	private boolean redisDowntime = false;

	public ConfigurationCenter getConfCenter() {
		return confCenter;
	}

	public void setConfCenter(ConfigurationCenter confCenter) {
		this.confCenter = confCenter;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public CacheHttpSession createSession(
			SessionHttpServletRequestWrapper request,
			HttpServletResponse response,
			RequestEventSubject requestEventSubject, boolean create) {
		String sessionId = getRequestedSessionId(request);

		CacheHttpSession session = null;
		if ((StringUtils.isEmpty(sessionId)) && (!create))
			return null;
		if (StringUtils.isNotEmpty(sessionId)) {
			session = loadSession(sessionId);
		}
		if ((session == null) && (create)) {
			session = createEmptySession(request, response);
		}
		if (session != null)
			attachEvent(session, request, response, requestEventSubject);
		
		return session;
	}

	private String getRequestedSessionId(HttpServletRequestWrapper request) {
		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0))
			return null;
		for (Cookie cookie : cookies) {
			if (cookieSessionId.equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}

	private void saveSession(CacheHttpSession session) {
		try {
			if (this.log.isDebugEnabled())
				this.log.debug("CacheHttpSession saveSession [ID=" + session.id
						+ ",isNew=" + session.isNew + ",isDiry="
						+ session.isDirty + ",isExpired=" + session.expired
						+ "]");
			if (session.expired)
				this.removeSessionFromCache(generatorSessionKey(session.id));
			else
				
				this.saveSessionToCache(generatorSessionKey(session.id),
						session, session.maxInactiveInterval);
		} catch (Exception e) {
			throw new SessionException(e);
		}
	}

	private CacheHttpSession createEmptySession(
			SessionHttpServletRequestWrapper request,
			HttpServletResponse response) {
		CacheHttpSession session = new CacheHttpSession();
		session.id = createSessionId();
		session.creationTime = System.currentTimeMillis();
		session.maxInactiveInterval = this.maxInactiveInterval;
		session.isNew = true;
		if (this.log.isDebugEnabled())
			this.log.debug("CacheHttpSession Create [ID=" + session.id + "]");
		saveCookie(session, request, response);
		return session;
	}

	private String createSessionId() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	private void attachEvent(final CacheHttpSession session,
			final HttpServletRequestWrapper request,
			final HttpServletResponse response,
			RequestEventSubject requestEventSubject) {
		    session.setListener(new SessionListenerAdaptor() {
			public void onInvalidated(CacheHttpSession session) {
				SessionManager.this.saveCookie(session, request, response);
			}
		});
		requestEventSubject.attach(new RequestEventObserver() {
			public void completed(HttpServletRequest servletRequest,
					HttpServletResponse response) {
				int updateInterval = (int) ((System.currentTimeMillis() - session.lastAccessedTime) / 1000L);
				if (SessionManager.this.log.isDebugEnabled()) {
					SessionManager.this.log
							.debug("CacheHttpSession Request completed [ID="
									+ session.id + ",lastAccessedTime="
									+ session.lastAccessedTime
									+ ",updateInterval=" + updateInterval + "]");
				}
				if ((!session.isNew)
						&& (!session.isDirty)
						&& (updateInterval < SessionManager.this.expirationUpdateInterval))
					return;
				if ((session.isNew) && (session.expired))
					return;
				session.lastAccessedTime = System.currentTimeMillis();
				SessionManager.this.saveSession(session);
			}
		});
	}

	private void addCookie(CacheHttpSession session,
			HttpServletRequestWrapper request, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieSessionId, null);
		if (!StringUtils.isBlank(domain))
			cookie.setDomain(domain);
		cookie.setPath(StringUtils.isBlank(request.getContextPath())?"/":request.getContextPath());
		cookie.setPath("/");
		if (session.expired)
			cookie.setMaxAge(0);
		else if (session.isNew) {
			cookie.setValue(session.getId());
		}
		
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
	
	private void saveCookie(CacheHttpSession session,
			HttpServletRequestWrapper request, HttpServletResponse response) {
		if ((!session.isNew) && (!session.expired))
			return;

		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0)) {
			addCookie(session, request, response);
		} else {
			for (Cookie cookie : cookies) {
				if (cookieSessionId.equals(cookie.getName())) {
					if (!StringUtils.isBlank(domain))
						cookie.setDomain(domain);
					cookie.setPath(StringUtils.isBlank(request.getContextPath())?"/":request.getContextPath());
					cookie.setMaxAge(0);
				}
			}
			addCookie(session, request, response);
		}
		if (this.log.isDebugEnabled())
			this.log.debug("CacheHttpSession saveCookie [ID=" + session.id
					+ "]");
	}

	private CacheHttpSession loadSession(String sessionId) {
		CacheHttpSession session;
		try {
			HttpSession data = this
					.getSessionFromCache(generatorSessionKey(sessionId));
			if (data == null) {
				this.log.debug("Session " + sessionId + " not found in Redis");
				session = null;
			} else {
				session = (CacheHttpSession) data;
			}
			if (this.log.isDebugEnabled())
				this.log.debug("CacheHttpSession Load [ID=" + sessionId
						+ ",exist=" + (session != null) + "]");
			if (session != null) {
				session.isNew = false;
				session.isDirty = false;
			}
			return session;
		} catch (Exception e) {
			this.log.warn("exception loadSession [Id=" + sessionId + "]", e);
		}
		return null;
	}

	private String generatorSessionKey(String sessionId) {
		return SESSION_ID_PREFIX.concat(sessionId);
		// return "R_JSID_".concat(sessionId);
	}

	public SessionManager() {
	}

	public void init() {
		try {
			process(confCenter.getConfAndWatch(confPath, this));
		} catch (ConfigException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param conf
	 * 
	 * @see com.ai.paas.client.ConfigurationWatcher#process(java.lang.String)
	 */
	@Override
	public void process(String conf) {
		if (log.isInfoEnabled()) {
			log.info("new session configuration is received: " + conf);
		}
		
		try {
			JsonNode json = new ObjectMapper().readTree(conf);
			
			boolean changed = false;
			if (json.has(HOST_KEY)
					&& !json.get(HOST_KEY).asText().equals(host)) {
				changed = true;
				host = json.get(HOST_KEY).asText();
			}
			if (json.has(PORT_KEY)
					&& !json.get(PORT_KEY).asText().equals(port)) {
				changed = true;
				port = json.get(PORT_KEY).asText();
			}
			if (json.has(TIMEOUT_KEY)
					&& !json.get(TIMEOUT_KEY).asText().equals(timeOut)) {
				changed = true;
				timeOut = json.get(TIMEOUT_KEY).asText();
			}
			if (json.has(MAXACTIVE_KEY)
					&& !json.get(MAXACTIVE_KEY).asText().equals(maxActive)) {
				changed = true;
				maxActive = json.get(MAXACTIVE_KEY).asText();
			}
			if (json.has(MAXIDLE_KEY)
					&& !json.get(MAXIDLE_KEY).asText().equals(maxIdle)) {
				changed = true;
				maxIdle = json.get(MAXIDLE_KEY).asText();
			}
			if (json.has(MAXWAIT_KEY)
					&& !json.get(MAXWAIT_KEY).asText().equals(maxWait)) {
				changed = true;
				maxWait = json.get(MAXWAIT_KEY).asText();
			}
			if (json.has(TESTONBORROW_KEY)
					&& !json.get(TESTONBORROW_KEY).asText().equals(testOnBorrow)) {
				changed = true;
				testOnBorrow = json.get(TESTONBORROW_KEY).asText();
			}
			if (json.has(TESTONRETURN_KEY)
					&& !json.get(TESTONRETURN_KEY).asText().equals(testOnReturn)) {
				changed = true;
				testOnReturn = json.get(TESTONRETURN_KEY).asText();
			}
			if (json.has(DBINDEX_KEY)
					&& json.get(DBINDEX_KEY).asInt()!=dbIndex) {
				dbIndex = json.get(DBINDEX_KEY).asInt();
			}
			if (json.has(TWEMPROXY_KEY)
					&& !json.get(TWEMPROXY_KEY).asText().equals(twemproxy)) {
				twemproxy = json.get(TWEMPROXY_KEY).asText();
			}
			if (json.has(EXPIRATIONUPDATEINTERVAL_KEY)
					&& json.get(EXPIRATIONUPDATEINTERVAL_KEY).asInt() != expirationUpdateInterval) {
				expirationUpdateInterval = json.get(EXPIRATIONUPDATEINTERVAL_KEY).asInt();
			}
			if (json.has(MAXINACTIVEINTERVAL_KEY)
					&& json.get(MAXINACTIVEINTERVAL_KEY).asInt() != maxInactiveInterval) {
				maxInactiveInterval = json.get(MAXINACTIVEINTERVAL_KEY).asInt();
			}
			if (JSONValidator.isChanged(json, DOMAIN_KEY, domain)) {
				domain = json.get(DOMAIN_KEY).asText();
			}
			//对cookie中的sessionId 转为参数处理；key存在，且值非空，与现有值不一致；才需要设置；
			if (json.has(COOKIE_SESSION_ID) 
					&& StringUtils.isNotEmpty(json.get(COOKIE_SESSION_ID).asText())
					&& !json.get(COOKIE_SESSION_ID).asText().equals(cookieSessionId)) {
				cookieSessionId = json.get(COOKIE_SESSION_ID).asText();
			}
			
			if (changed) {
				cacheClient = new RedisCacheClient(conf);
				if (log.isInfoEnabled()) {
					log.info("cache server address is changed to " + conf);
				}
			}
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public HttpSession getSessionFromCache(String id) {
		Object obj = null;
		if ("true".equalsIgnoreCase(twemproxy)) {
			obj = cacheClient.getSession(id);
		} else {
			obj = cacheClient.getSession(dbIndex, id);
		}
		if (obj != null && obj instanceof HttpSession) {
			redisDowntime = false;
			return (HttpSession) obj;
		} else if(obj != null && obj instanceof RedisDowntime) {
			redisDowntime = true;
			log.warn("--------------redis宕机-------------");
			return localSessions.get(id);
		}else {
			redisDowntime = false;
			return null;
		}
	}

	public void saveSessionToCache(String id, HttpSession session, int liveTime) {
		if(redisDowntime){
			localSessions.put(id, session);
		}else{
			if ("true".equalsIgnoreCase(twemproxy)) {
				cacheClient.addSession(id, session, liveTime);
			} else {
				cacheClient.addSession(dbIndex, id, session, liveTime);
			}
			localSessions.clear();
		}

	}

	public void removeSessionFromCache(String id) {
		if(redisDowntime){
			localSessions.remove(id);
		}else{
			if ("true".equalsIgnoreCase(twemproxy)) {
				cacheClient.delItem(id);
			} else {
				cacheClient.delItem(dbIndex, id);
			}
		}
		
	}
}