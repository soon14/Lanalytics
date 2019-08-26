package com.ai.analy.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConvertUtil {

	private static final Logger log = Logger.getLogger(DataConvertUtil.class);
	
	/**
	 * 转换单品属性字段信息
	 * @param skuProp
	 * @return
	 */
	public static List<Map<String,String>> ConvertProps(String skuProp){
		
		List<Map<String,String>> props = new ArrayList<Map<String,String>>();
		
		try{
			
			List<String[]> list = new ObjectMapper().readValue(skuProp.getBytes("UTF-8"), new TypeReference<List<String[]>>(){});
			if(list != null && list.size()>0){
				for (String[] prop : list) {
					
					if(prop != null && prop.length>0){
						
						Map<String,String> map = new HashMap<String, String>();
						String prop_id = prop[0];
						String prop_name = prop.length>1 ?prop[1]:"";
						String prop_value = prop.length>2 ?prop[2]:"";
						
						map.put("prop_id", prop_id);
						map.put("prop_name", prop_name);
						map.put("prop_value", prop_value);
						
						props.add(map);
					}
				}
			}
			
			
		}catch(Exception e){
			log.error("单品属性转换失败：" +e);
		}
		
		return props;
	}
}
