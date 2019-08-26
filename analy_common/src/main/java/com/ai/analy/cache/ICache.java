package com.ai.analy.cache;
/**
 * 缓存
 * @author xiongqian
 *
 */
public interface ICache {
	public String getItem(int dbIndex,String key);
	public String getItem(int dbIndex,String key,String field);
	public Long addItem(int dbIndex,String key,String field,String value);
	public String getItem(String key);
	public String getItem(String key,String field);
}