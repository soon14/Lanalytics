package com.ai.analy.cache.impl;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ai.analy.cache.ICache;
import com.ai.analy.cache.util.JSONValidator;
import com.ai.analy.config.ConfigurationCenter;
import com.ai.analy.config.ConfigurationWatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 基于统一配置的redis缓存实现
 *
 */
public class RedisCache implements ConfigurationWatcher, ICache {
	
	private static final Logger log = Logger.getLogger(RedisCache.class);
	private String confPath = "";
	private static final String HOST_KEY = "host";
	private static final String PORT_KEY = "port";
	private static final String TIMEOUT_KEY = "timeOut";
	private static final String MAXACTIVE_KEY = "maxActive";
	private static final String MAXIDLE_KEY = "maxIdle";
	private static final String MAXWAIT_KEY = "maxWait";
	private static final String TESTONBORROW_KEY = "testOnBorrow";
	private static final String TESTONRETURN_KEY = "testOnReturn";

	private String host = null;
	private String port = null;
	private String timeOut = null;
	private String maxActive = null;
	private String maxIdle = null;
	private String maxWait = null;
	private String testOnBorrow = null;
	private String testOnReturn = null;
	private RedisCacheClient cacheClient = null;
	private ConfigurationCenter confCenter = null;
	
	public RedisCache() {
	}

	public void init() {
		try {
			process(confCenter.getConfAndWatch(confPath, this));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public void process(String conf) {
		if (log.isInfoEnabled()) {
			log.info("new log configuration is received: " + conf);
		}
		
		try {
		
			JsonNode jsonObj = new ObjectMapper().readTree(conf);
			
			boolean changed = false;
			if (JSONValidator.isChanged(jsonObj, HOST_KEY, host)) {
				changed = true;
				host = jsonObj.get(HOST_KEY).asText();
			}
			if (JSONValidator.isChanged(jsonObj, PORT_KEY, port)) {
				changed = true;
				port = jsonObj.get(PORT_KEY).asText();
			}
			if (JSONValidator.isChanged(jsonObj, TIMEOUT_KEY, timeOut)) {
				changed = true;
				timeOut = jsonObj.get(TIMEOUT_KEY).asText();
			}
			if (JSONValidator.isChanged(jsonObj, MAXACTIVE_KEY, maxActive)) {
				changed = true;
				maxActive = jsonObj.get(MAXACTIVE_KEY).asText();
			}
			if (JSONValidator.isChanged(jsonObj, MAXIDLE_KEY, maxIdle)) {
				changed = true;
				maxIdle = jsonObj.get(MAXIDLE_KEY).asText();
			}
			if (JSONValidator.isChanged(jsonObj, MAXWAIT_KEY, maxWait)) {
				changed = true;
				maxWait = jsonObj.get(MAXWAIT_KEY).asText();
			}
			if (JSONValidator.isChanged(jsonObj, TESTONBORROW_KEY, testOnBorrow)) {
				changed = true;
				testOnBorrow = jsonObj.get(TESTONBORROW_KEY).asText();
			}
			if (JSONValidator.isChanged(jsonObj, TESTONRETURN_KEY, testOnReturn)) {
				changed = true;
				testOnReturn = jsonObj.get(TESTONRETURN_KEY).asText();
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

	
	@Override
	public String getItem(int dbIndex,String key){
		
		return cacheClient.getItem(dbIndex, key);
	}
	
	@Override
	public String getItem(int dbIndex,String key,String field){
		
		return cacheClient.hgetItem(dbIndex,key,field);
	}

	@Override
	public Long addItem(int dbIndex, String key, String field,String value) {
		// TODO Auto-generated method stub
		return cacheClient.hsetItem(dbIndex, key, field, value);
	}

    @Override
    public String getItem(String key) {
        return cacheClient.getItem(key);
    }
	
    @Override
    public String getItem(String key,String field){
    	return cacheClient.hgetItem(key, field);
    }
}
