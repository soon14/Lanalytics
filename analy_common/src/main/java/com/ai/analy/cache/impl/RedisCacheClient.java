package com.ai.analy.cache.impl;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import com.ai.analy.cache.util.SerializeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * redis的客户端实现
 *
 */
public class RedisCacheClient {
	private static final Logger log = Logger.getLogger(RedisCacheClient.class);
	private Pool<Jedis> pool;
	private JedisPoolConfig config;
	private static final String HOSTS_KEY = "hosts";
	private static final String TIMEOUT_KEY = "timeOut";
	private static final String MAXACTIVE_KEY = "maxActive";
	private static final String MAXIDLE_KEY = "maxIdle";
	private static final String MAXWAIT_KEY = "maxWait";
	private static final String TESTONBORROW_KEY = "testOnBorrow";
	private static final String TESTONRETURN_KEY = "testOnReturn";
	private static final String MASTERNAME_KEY = "masterName";
	
	private String parameters;

	public RedisCacheClient(String parameter) {
		parameters = parameter;
		createPool();
	}
	private synchronized boolean createPool(){
		boolean result = false;
		if(!canConnection()){
			log.debug("-----------------------创建JedisPool------------------------begin---");

			try {
				
				JsonNode json = new ObjectMapper().readTree(parameters);
				config = new JedisPoolConfig();
				//config.setMaxTotal(json.get(MAXACTIVE_KEY).asInt());
				config.setMaxIdle(json.get(MAXIDLE_KEY).asInt());
				//config.setMaxWaitMillis(json.get(MAXWAIT_KEY).asLong());
				config.setTestOnBorrow(json.get(TESTONBORROW_KEY).asBoolean());
				config.setTestOnReturn(json.get(TESTONRETURN_KEY).asBoolean());
				
				String hosts = json.get(HOSTS_KEY).asText();
				String[] hostArr = hosts.split(",");
				
				if(hostArr!=null&&hostArr.length>0){
					//集群模式
					/*
					Set<String> sentinels = new HashSet<String>();
					for (String attr : hostArr) {
						sentinels.add(attr);
					}
					
					pool = new JedisSentinelPool(json.get(MASTERNAME_KEY).asText(),sentinels,config,json.get(TIMEOUT_KEY).asInt());
					if(canConnection()){
						log.info("-----------------JedisPool使用主机信息:"+hosts);
					}
					*/
					
				    //单例模式
					for(int i=0;i<hostArr.length;i++){
						String ip = hostArr[i].split(":")[0];
						int port = Integer.parseInt(hostArr[i].split(":")[1]);
						pool = new JedisPool(config, ip,port, json.get(TIMEOUT_KEY).asInt());
						if(canConnection()){
							log.info("-----------------JedisPool使用主机信息:"+ip+":"+port);
							result = true;
							break;
						}
					}
				}
				
			} catch (Exception e) {
				log.error("",e);
			}
			log.debug("-----------------------创建JedisPool------------------------end---");
		}
		return result;
	}

	/**
	 * redis是否可用
	 * @return
	 */
	private  boolean canConnection() {
		if(pool == null)
			return false;
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.get("ok");
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException){
				return false;
			}
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}	
		return true;
	}

	public String getItem(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.get(key);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
			return null;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public String getItem(int dbIndex, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(dbIndex);
			return jedis.get(key);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
			return null;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public String hgetItem(String key,String field) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hget(key,field);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
			return null;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public String hgetItem(int dbIndex, String key,String field) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(dbIndex);
			return jedis.hget(key,field);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
			return null;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public Long hsetItem(String key,String field,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hset(key, field, value);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
			return 0L;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public Long hsetItem(int dbIndex, String key,String field,String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(dbIndex);
			return jedis.hset(key,field,value);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
			return 0L;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public long delItem(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.del(key.getBytes());
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}
		return 0;
		
	}

	public long delItem(int dbIndex, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(dbIndex);
			return jedis.del(key.getBytes());
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}
		return 0;
	}
	
	/**
	 * session 操作相关， 存入对象需要序列化，取出反序列化
	 **/
	public Object getSession(String key) {
		Jedis jedis = null;
		byte[] data = null;
		try {
			jedis = pool.getResource();
			data = jedis.get(key.getBytes());
			return SerializeUtil.unserialize(data);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException){
				if(!createPool())
					return RedisDowntime.getInstance();
			}
			return null;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public Object getSession(int dbIndex,String key) {
		Jedis jedis = null;
		byte[] data = null;
		try {
			jedis = pool.getResource();
			jedis.select(dbIndex);
			data = jedis.get(key.getBytes());
			return SerializeUtil.unserialize(data);
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException){
				if(!createPool())
					return RedisDowntime.getInstance();
			}
			return null;
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}

	}
	
	public void addSession(String key, Object object, int seconds) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.setex(key.getBytes(), seconds,
					SerializeUtil.serialize(object));
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}
	}
	
	public void addSession(int dbIndex, String key, Object object, int seconds) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(dbIndex);
			jedis.setex(key.getBytes(), seconds,
					SerializeUtil.serialize(object));
		} catch (Exception e) {
			log.error("",e);
			if(e instanceof java.net.SocketException ||e instanceof java.net.ConnectException 
					|| e instanceof redis.clients.jedis.exceptions.JedisConnectionException)
				createPool();
		} finally {
			if (jedis != null)
				returnResource(jedis);
		}
	}
	
	private void returnResource(Jedis jedis){
		pool.returnResource(jedis);
	}
}