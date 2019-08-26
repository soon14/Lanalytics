package com.ai.analy.system.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware{

	private static ApplicationContext applicationContext; 
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		SpringContextUtil.applicationContext = applicationContext;  
	}
	
	public static Object getBean(String name) throws BeansException {  
	    return applicationContext.getBean(name);  
	}
}
