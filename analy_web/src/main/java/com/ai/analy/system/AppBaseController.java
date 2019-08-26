package com.ai.analy.system;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContext;

import com.ai.analy.system.util.StaffSessionUtil;
import com.ai.analy.vo.comm.QueryParamsVo;
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
	
	protected QueryParamsVo getQueryParams(String dateFromStr,String dateToStr,String source,String target){
		
		QueryParamsVo vo = new QueryParamsVo();
		
		//如果为空，则默认取全部
		if(StringUtils.isBlank(source)){
			vo.setSource(0);
		}else{
			try {
			    vo.setSource(Integer.parseInt(source));
			} catch (Exception e) {
				vo.setSource(0);
			}
		}
		//统计对象
		if(StringUtils.isBlank(target) == false){
			vo.setTarget(target);
		}
		
		//开始时间
		Date dateFrom;
		//结束时间
		Date dateTo;
		
		//上期开始时间
		Date preDateFrom;
		//上期结束时间 
		Date preDateTo;
		
		Calendar cd = Calendar.getInstance();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if(StringUtils.isBlank(dateFromStr)){
			dateFromStr = df.format(cd.getTime());
		}
		if(StringUtils.isBlank(dateToStr)){
			dateToStr = df.format(cd.getTime());
		}
		
		try{
			
			dateFrom = df.parse(dateFromStr);
		    dateTo = df.parse(dateToStr);
		    cd.setTime(dateFrom);
		    cd.add(Calendar.DATE, -1);
		    preDateTo = cd.getTime();
		    int subDate = (int)((dateTo.getTime()+6000 - dateFrom.getTime())/1000/60/60/24);
			cd.add(Calendar.DATE, 0-subDate);
			preDateFrom = cd.getTime();
		    
		}catch(Exception e){
			dateTo = cd.getTime();
			dateFrom = cd.getTime();
			cd.add(Calendar.DATE, -1);
			preDateTo = cd.getTime();
			preDateFrom = cd.getTime();
		}
		
		//时间
		vo.setDateFrom(getOnlyDate(dateFrom));
		vo.setDateTo(getOnlyDate(dateTo));
		vo.setPreDateFrom(getOnlyDate(preDateFrom));
        vo.setPreDateTo(getOnlyDate(preDateTo));
		
        //店铺ID
		vo.setShopId(StaffSessionUtil.getStaffInfo(session).getShopId());
		
		return vo;
	}
	
	protected QueryParamsVo getQueryParams(){
		
		String source = getParam("source");//来源类型
		String dateFromStr = getParam("dateFrom");//开始时间
		String dateToStr = getParam("dateTo");//结束时间
		String target = getParam("target");//统计对象（界面）
		
		return getQueryParams(dateFromStr,dateToStr,source,target);
	}

	protected void setPageInfo(QueryParamsVo params){
		String pageSize = this.getParam("pageSize");
		String pageNo = this.getParam("pageNo");
		if(StringUtils.isBlank(pageSize) == false){
			try{
			    params.setPageSize(Integer.parseInt(pageSize));
			}catch(Exception e){}
		}
		
		if(StringUtils.isBlank(pageNo) == false){
			try{
			    params.setPageNo(Integer.parseInt(pageNo));
			}catch(Exception e){}
		}
	}
	
    protected String toJsonString(Object obj){
    	try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
		}
    	return "{}";
    }
    
    private Date getOnlyDate(Date source){
    	
    	Calendar cd = Calendar.getInstance();
    	cd.setTime(source);
    	
    	cd.set(Calendar.HOUR_OF_DAY, 0);
    	cd.set(Calendar.SECOND, 0);
    	cd.set(Calendar.MINUTE, 0);
    	cd.set(Calendar.MILLISECOND, 0);
    	
    	return cd.getTime();
    }
}
