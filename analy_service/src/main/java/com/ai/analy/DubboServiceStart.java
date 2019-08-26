package com.ai.analy;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.analy.utils.PaasContextHolder;

public class DubboServiceStart {
	
	private static final Logger log = Logger.getLogger(DubboServiceStart.class);
	private static final String configFile = "analyServiceContext.xml";
	
	public static void init(){
		log.info("DubboStart......begin");
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{configFile});
		context.registerShutdownHook();
		context.start();
		log.info("DubboStart......success");
		
		PaasContextHolder.setCtx(context);
	}
	
	
    public static void main(String[] args) throws ParseException {

    	init();
    	
		while (true) {
			try {
				Thread.currentThread();
				Thread.sleep(100L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
}