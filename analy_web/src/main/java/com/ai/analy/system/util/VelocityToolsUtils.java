/**
 * 
 */
package com.ai.analy.system.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ai.analy.cache.ICache;
import com.ai.analy.config.ConfigurationCenter;
import com.ai.analy.constants.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 与数字相关的工具类
 * @author xiongqian
 *
 */
public class VelocityToolsUtils {
	
	private Logger log = Logger.getLogger(VelocityToolsUtils.class);
	
	private ICache alyCacheSv;
	private ConfigurationCenter confCenter;
	
	private ICache getCacheInstant(){
		if(alyCacheSv == null){
			alyCacheSv = (ICache)SpringContextUtil.getBean("alyCacheSv");
		}
		return alyCacheSv;
	}
	
	private ConfigurationCenter getconfCenterInstant(){
		if(confCenter == null){
			confCenter = (ConfigurationCenter)SpringContextUtil.getBean("confCenter");
		}
		return confCenter;
	}
	
	/**
	 * 格式化数字
	 * @param input  数字
	 * @param formater 格式
	 * @return
	 */
	public String formatNumber(String input,String formater){
		
		try{
		
			if(StringUtils.isBlank(input)){
				return "0";
			}
			
			double number = 0D;
			///如果传入的非数字，则展示为 原来的值；
			try{
				number = Double.parseDouble(input);
				
				if(Double.isNaN(number)){
					number = 0D;
				}
				
			} catch(Exception err){
				return input;
			}
			
			DecimalFormat decFormatter = new DecimalFormat(formater);
			
			return decFormatter.format(number);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			return input;
		}
	}
	
	/**
	 * 格式化百分比
	 * @param input 浮点数
	 * @param formater 格式
	 * @return  {input*100}%
	 */
	public String showPercentNumber(String input,String formater){
		try{
		
			if(StringUtils.isBlank(input)){
				return "0%";
			}
			
			double number = 0D;
			///如果传入的非数字，则展示为 原来的值；
			try{
				number = Double.parseDouble(input);
				
				if(Double.isNaN(number)){
					number = 0D;
				}
				
			} catch(Exception err){
				return input;
			}
			
			number = number*100;
			
			if(Double.isInfinite(number)){
				return "--";
			}
			
			DecimalFormat decFormatter = new DecimalFormat(formater);
			
			return decFormatter.format(number) +"%";
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			return input;
		}
	}
	
	/**
	 * 格式化金额
	 * @param input 浮点数
	 * @param formater 格式
	 * @return  {input/100}
	 */
	public String showMoneyNumber(String input,String formater){
		try{
		
			if(StringUtils.isBlank(input)){
				return "0%";
			}
			
			double number = 0D;
			///如果传入的非数字，则展示为 原来的值；
			try{
				number = Double.parseDouble(input);
				
				if(Double.isNaN(number)){
					number = 0D;
				}
				
			} catch(Exception err){
				return input;
			}
			
			number = number/100;
			DecimalFormat decFormatter = new DecimalFormat(formater);
			
			String resStr = decFormatter.format(number);
			if(resStr.indexOf(".") > 0){  
				resStr = resStr.replaceAll("0+?$", "");//去掉多余的0  
				resStr = resStr.replaceAll("[.]$", "");//如最后一位是.则去掉  
		    }
			
			return resStr;
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			return input;
		}
	}
	
	/**
	 * 根据url转换成对应的名称
	 * @param input url
	 * @return 名称
	 */
	public String formatUrlToName(String input){
		
		try{
		
			if(StringUtils.isBlank(input)){
				return "";
			}else{
				
				String key = Constants.REDIS_KEY.URL_KEY_PRE + input.trim().toLowerCase();
				
				String name = this.getCacheInstant().getItem(2,key,"name");
				
				if(StringUtils.isBlank(name)){
					if(input.length()>10){
						return input;//return input.substring(0,10)+"...";
					}else{
						return input;
					}
				}else{
					return name;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			return input;
		}
	}
	
	public String getSysConf(String fieldName){
		try{
		
			String conf = getconfCenterInstant().getConf("/com/ai/analy/sys/conf");
			JsonNode json = new ObjectMapper().readTree(conf);
			if(json.has(fieldName)){
				return json.get(fieldName).asText();
			}else{
				return fieldName;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			return fieldName;
		}
	}
	
	public List<JsonNode> getSysConfArr(String fieldName){
		try{
		
			String conf = getconfCenterInstant().getConf("/com/ai/analy/sys/conf");
			JsonNode json = new ObjectMapper().readTree(conf);
			if(json.has(fieldName)){
				Iterator<JsonNode> notes = json.get(fieldName).elements();
				List<JsonNode> list = new ArrayList<JsonNode>();
				while(notes.hasNext()){
					list.add(notes.next());
				}
				return list;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			return null;
		}
	}
	
	public String getzkConf(String pathName){
		String conf = "";
		try{
			conf = getconfCenterInstant().getConf(pathName);
			return conf;
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
			return conf;
		}
	}
}