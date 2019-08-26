package com.ai.analy.system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BaseController
 * @author lixc
 */
public class AppBaseController {
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpSession session;
	@Autowired
	protected ServletContext servletContext;
	
	protected RequestContext requestContext;
	
	public static final String CONTENTTYPE_HTML = "text/html";
	public static final String CONTENTTYPE_JSON = "application/json";
    public static final String CHARTSET_UTF8 = "utf-8";
    
    
    
    /**
     * 从Request获取参数
     * @param name
     * @return
     */
    final protected String getParam(String name){
    	return request.getParameter(name);
    }
    
    /**
     * 从Request获取参数
     * @param name
     * @return
     */
    final protected String[] getParams(String name){
    	return request.getParameterValues(name);
    }
    
    /**
     * 获取参数为Map
     * @return
     */
	final protected Map<String,String> getParamMap(){
    	Map<String, String> m = new HashMap<String, String>();
        Iterator<String> keys = request.getParameterMap().keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            m.put(key, getParam(key));
        }
        return m;
    }
	
	final protected String getMessages(String code){
		if(requestContext == null){
			requestContext = new RequestContext(request);
		}
		
		return requestContext.getMessage(code);
	}
	
    protected String toJsonString(Object obj){
    	try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
		}
    	return "{}";
    }
}
