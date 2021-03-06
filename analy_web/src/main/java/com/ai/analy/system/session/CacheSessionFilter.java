package com.ai.analy.system.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class CacheSessionFilter implements Filter {
	public static String[] IGNORE_SUFFIX={};
	private SessionManager sessionManager;

	public void destroy() {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	    HttpServletRequest request = (HttpServletRequest)servletRequest;

	    if (!shouldFilter(request)) {
	      filterChain.doFilter(servletRequest, servletResponse);
	      return;
	    }
	    
	    //*************把访问的客户ip记录在线程里面，目的是为了能在日志里面串起程序执行顺序，与session无关，只是借用这个过滤器程序。
	    //String clientAddr = InetTool.getClientAddr(request);
	    //ThreadId.setThreadIdWithIP(clientAddr);
	    //*************
	    HttpServletResponse response = (HttpServletResponse)servletResponse;
	    RequestEventSubject eventSubject = new RequestEventSubject();
	    
//	    sessionManager=(SessionManager)WebApplicationContextUtils.getWebApplicationContext(servletRequest.getServletContext()).getBean("sessionManager");
	    sessionManager=(SessionManager)WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()).getBean("sessionManager");
	    SessionHttpServletRequestWrapper requestWrapper = new SessionHttpServletRequestWrapper(request, response, this.sessionManager, eventSubject);
	    try {
	      filterChain.doFilter(requestWrapper, servletResponse);
	    }
	    finally {
	      eventSubject.completed(request, response);
	    }
	  }

	  private boolean shouldFilter(HttpServletRequest request)
	  {
	    String uri = request.getRequestURI().toLowerCase();
        for (String suffix : IGNORE_SUFFIX) {
	      if (uri.endsWith(suffix)) return false;
	    }
	    return true;
	  }
	public void init(FilterConfig fc) throws ServletException {
		String ignore_suffix=fc.getInitParameter("ignore_suffix");
		if (!"".equals(ignore_suffix))
		    IGNORE_SUFFIX=fc.getInitParameter("ignore_suffix").split(",");
	}

}
