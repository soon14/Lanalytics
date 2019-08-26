package com.ai.analy.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PaasContextHolder {

    private static ApplicationContext ctx = null;
//            new ClassPathXmlApplicationContext(new String[] { "analyServiceContext.xml" });

    public static ApplicationContext getContext() {
        return ctx;
    }
    
    public static void setCtx(ApplicationContext context){
        ctx=context;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) ctx.getBean(beanName);
    }

    public static void closeCtx() {
        ((ClassPathXmlApplicationContext) ctx).close();
    }
}
