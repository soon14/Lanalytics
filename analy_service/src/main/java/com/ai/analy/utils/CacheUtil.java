package com.ai.analy.utils;

import com.ai.analy.cache.ICache;

public class CacheUtil {

    private static ICache alyCacheSv = null;

    private static ICache getIntance() {
        if (null != alyCacheSv) {
            return alyCacheSv;
        }
        alyCacheSv = PaasContextHolder.getBean("alyCacheSv");
        return alyCacheSv;
    }

    public static String getItem(String key) {
        return getIntance().getItem(key);
    }

    public static String getItem(String key, String field) {
        return getIntance().getItem(key, field);
    }
    
    public static String getItem(int dbIndex, String key) {
        return getIntance().getItem(dbIndex, key);
    }

    public static String getItem(int dbIndex, String key, String field) {
        return getIntance().getItem(dbIndex, key, field);
    }

    public static Long addItem(int dbIndex, String key, String field, String value) {
        return getIntance().addItem(dbIndex, key, field, value);
    }
}
