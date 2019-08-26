package com.ai.analy.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CommonUtils {

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 * @author liangyi
	 */
	public static boolean isNumeric(String str) {

		if (str == null) {
			return false;
		}

		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	/**
	 * 把map对象转为json
	 * 
	 * @param objectMap
	 * @return
	 * @throws JsonProcessingException
	 * @author liangyi
	 */
	public static String toJson(Map<String, Object> mapObject){
		return JSONObject.toJSONString(mapObject);
	}

	public static String trimToEmpty(String str) {
		return str == null ? "" : str.trim();
	}
	
	public static Map<String, Object> buildAErrorMsgMap(String serviceState,String serviceMsg){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("serviceState", serviceState);
        map.put("serviceMsg", serviceMsg);
        return map;
    }
}
