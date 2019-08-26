package com.ai.analy.system.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public class SimpleMappingExceptionResolver extends org.springframework.web.servlet.handler.SimpleMappingExceptionResolver{

	private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE;
		
	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView(viewName);
		if (this.exceptionAttribute != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
			}
			
			mv.addObject(this.exceptionAttribute, ex.getMessage());
			mv.addObject("isAjaxRequest", isAjaxRequest(request));
		}
		return mv;
	}
	
	private boolean isAjaxRequest(HttpServletRequest request){
		
		String requestedWith = request.getHeader("x-requested-with"); 
        // 表示是一个AJAX POST请求
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
        	return true;
        }
        return false;
	}
}
