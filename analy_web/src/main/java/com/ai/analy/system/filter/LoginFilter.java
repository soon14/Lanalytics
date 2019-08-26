package com.ai.analy.system.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.analy.system.util.StaffSessionUtil;
import com.ai.analy.system.util.VelocityToolsUtils;
import com.ai.analy.vo.comm.MenuDisplayVO;
import com.ai.analy.vo.comm.ShopInfoVo;
import com.ai.analy.vo.comm.StaffInfoVo;
import com.ai.ecp.server.front.security.AuthPrivilegeResDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 是否登录省份验证
 * @author lixc
 *
 */
public class LoginFilter implements Filter{
	
	public String loginPage = "/login";//在web.xml中配置
	public String mainPage = "/flowAnaly/flowbasic";//在web.xml中配置
	
	public static final String sessionOutPage = "/system/sessionOut"; //写死在这，因为不会再修改
	public static final String noAuthPage = "/system/noAuth";//写死在这，因为不会再修改
	
	public String[] IGNORE_SUFFIX={};//在web.xml中配置
	
	public Object[] NOTLOGIN_PAGES = {};//在web.xml中配置
	
	public Object[] LOGIN_PAGES = {};//已登录可访问url
	
	private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		
		//过滤不需要权限控制的Url
		if (shouldFilter(request) ==false) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		
		//判断是否登录
		if(checkLogin(request,session) == true){
			//已登陆
			//如果是登录界面，则跳转到主界面
			String uri = request.getServletPath();
			if(uri.endsWith(this.loginPage)){
				response.sendRedirect(request.getContextPath()+this.mainPage);
				return;
			}
			
			//判断当前Url是否允许访问
			if(shouldFilterPage(request,NOTLOGIN_PAGES) == true || shouldFilterPage(request,LOGIN_PAGES) == true){
				//系统允许访问
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			
			//如果不是ajax请求
			if(isAjaxRequest(request) == false){
				
				StaffInfoVo staff = StaffSessionUtil.getStaffInfo(session);
				//当前登录用户没有该Url权限
				if(staff.isHasAuth() == false || shouldFilterPage(request,staff.getAuthUrl().toArray()) == false){
					//无权限访问
					response.sendRedirect(request.getContextPath()+noAuthPage);
					return;
				}
			}
			
			//有权限访问
			filterChain.doFilter(request, response);
			
		}else{
			//未登陆
			//判断当前Url是否允许访问
			if(shouldFilterPage(request,NOTLOGIN_PAGES) == true){
				//系统允许访问
				filterChain.doFilter(servletRequest, servletResponse);
				return;
				
			}else{
				//如果是AJAX请求，界面需要特殊处理
				if(isAjaxRequest(request)){
					response.sendRedirect(request.getContextPath()+sessionOutPage+"?p="+Math.random());
					return;
				}
				
				//跳转到登陆界面
	            String requestUrl = this.genUrl(request);
				if(StringUtils.isEmpty(requestUrl)){
				    requestUrl = request.getContextPath()+this.loginPage;
				} else {
					requestUrl = request.getContextPath()+this.loginPage+"?toUrl="+requestUrl;
				}
				response.sendRedirect(requestUrl);
				return;
			}
		}
	}
	
	
	/**
	 * 生成当前请求的URL；
	 * @param request
	 * @return
	 */
	private String genUrl(HttpServletRequest request){
		
		String url = request.getServletPath();
		if("/".equalsIgnoreCase(url)){
			return "";
		}
		
		StringBuffer strBuf = new StringBuffer(request.getContextPath()+url);
		
		Map<String,String[]> map = request.getParameterMap();
		
		List<String> paramList = new ArrayList<String>();
		
		for(String key : map.keySet()){
			String strs = StringUtils.join(map.get(key),",");
			try{
			  String value = URLEncoder.encode(strs,"UTF-8");
			  paramList.add(key+"="+value);
			} catch(Exception err){
				logger.error("encoding url :"+strs+"; error;msg"+err.getMessage());
			}
		}
		
		String param  = "";
		if(paramList.size() !=0){
			param = StringUtils.join(paramList, "&");
		}
		if(StringUtils.isNotEmpty(param)){
			strBuf.append("?").append(param);
		}
		
		String resultUrl = "";
		try{
			resultUrl = URLEncoder.encode(strBuf.toString(),"UTF-8");
		} catch(Exception err){
			logger.error("encoding url :"+strBuf.toString()+"; error;msg"+err.getMessage());
		}
		return resultUrl;
	}
	
	/**
	 * 判断当前帐号是否登录；
	 * @param user
	 * @return
	 */
	private boolean checkLogin(HttpServletRequest request,HttpSession session){
		
		AuthPrivilegeResDTO dto = com.ai.ecp.base.util.WebContextUtil.getCurrentUser();
		if(dto != null){
			//判断是否有效
			StaffInfoVo staff = StaffSessionUtil.getStaffInfo(session);
			String currShopId = request.getParameter("shopid");
			staff = null;
			if(staff == null){
				
				VelocityToolsUtils utils = new VelocityToolsUtils();
				
				//用户信息
				staff = new StaffInfoVo();
				staff.setUserId(dto.getStaffId()+"");
				staff.setUserName(com.ai.ecp.base.util.WebContextUtil.getUserDetails().getUsername());
				
				//统计店铺
				List<ShopInfoVo> shops = new ArrayList<ShopInfoVo>();
				List<JsonNode> notes = utils.getSysConfArr("shop");
				for (JsonNode jsonNode : notes) {
					ShopInfoVo shopVo = new ShopInfoVo();
					shopVo.setShopId(jsonNode.get("shopId").asText());
					shopVo.setShopName(jsonNode.get("shopName").asText());
					shops.add(shopVo);
					
					if(shopVo.getShopId().equals(currShopId)){
						staff.setShopId(currShopId);
						staff.setShopName(shopVo.getShopName());
					}
				}
				
				staff.setShops(shops);
				
				//默认店铺
				if(StringUtils.isEmpty(staff.getShopId())){
					if(shops.size()>0){
						staff.setShopId(shops.get(0).getShopId());
						staff.setShopName(shops.get(0).getShopName());
					}
				}
				
				List<MenuDisplayVO> menus = new ArrayList<MenuDisplayVO>();
				List<MenuDisplayVO> platformMenus = new ArrayList<MenuDisplayVO>();
				String menuJson = utils.getzkConf("/com/ai/analy/sys/menus");
				String platformMenuJson = utils.getzkConf("/com/ai/analy/sys/platformmenus");
				
				try {
					menus = new ObjectMapper().readValue(menuJson.getBytes("UTF-8"), new TypeReference<List<MenuDisplayVO>>(){});
					platformMenus = new ObjectMapper().readValue(platformMenuJson.getBytes("UTF-8"), new TypeReference<List<MenuDisplayVO>>(){});
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//设置菜单Url，用于权限控制
				if(menus != null && menus.isEmpty() == false){
					staff.setHasAuth(true);
				    setAuthUrls(menus,staff);
				}else{
					staff.setHasAuth(false);
				}
				
				if(platformMenus != null && platformMenus.isEmpty() == false){
					staff.setHasAuth(true);
				    setAuthUrls(platformMenus,staff);
				}
				
				//设置登陆信息
				StaffSessionUtil.setStaffInfo(session, staff);
				
				//设置菜单信息
				StaffSessionUtil.setMenuDisplayVO(session, menus);
				
				//设置全平台菜单信息
				StaffSessionUtil.setPlatformMenuDisplayVO(session, platformMenus);
			}
			
			//根据当前请求设置店铺
			if(StringUtils.isEmpty(currShopId) == false && currShopId.equals(staff.getShopId()) == false){
				for (ShopInfoVo shop : staff.getShops()) {
					if(currShopId.equals(shop.getShopId())){
						staff.setShopId(currShopId);
						staff.setShopName(shop.getShopName());
						StaffSessionUtil.setStaffInfo(session, staff);
						break;
					}
				}
			}
			
			
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 过滤指定后缀文件不走Filter
	 * @param request
	 * @return
	 */
	private boolean shouldFilter(HttpServletRequest request) {
		String uri = request.getRequestURI().toLowerCase();
		
		for (String suffix : IGNORE_SUFFIX) {
			if (uri.endsWith(suffix)){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 校验当前访问的URL，是否可以 直接访问
	 * @param request
	 * @return true 可以访问
	 */
	private boolean shouldFilterPage(HttpServletRequest request,Object[] urls) {
		String uri = request.getServletPath();
		
		if(urls != null && urls.length>0){
			for (Object suffixObj : urls) {
				String suffix = suffixObj.toString();
				if (uri.endsWith(suffix))
					return true;
				if (uri.endsWith(suffix+".json"))
					return true;
				if (uri.endsWith(suffix+".html"))
					return true;
				if (uri.endsWith(suffix+".htm"))
					return true;
				if (uri.endsWith(suffix+".xml"))
					return true;
				if (uri.endsWith(suffix+".xls"))
					return true;
			}
		}
		
		return false;
	}

	/**
	 * web容器初始化
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		String loginPage = config.getInitParameter("loginPage");
		if(StringUtils.isNotBlank(loginPage)){
			this.loginPage = loginPage;
		}
		
		String mainPage = config.getInitParameter("mainPage");
		if(StringUtils.isNotBlank(mainPage)){
			this.mainPage = mainPage;
		}
		
		String ignore_suffix = config.getInitParameter("ignore_suffix");
		if (StringUtils.isEmpty(ignore_suffix) == false)
			IGNORE_SUFFIX = ignore_suffix.split(",");
		
		String notlogin_pages = config.getInitParameter("notlogin_pages");
		Object[] notLoginPage1 = {};
		if (StringUtils.isEmpty(notlogin_pages) == false)
			notLoginPage1 = notlogin_pages.split(",");
		
		VelocityToolsUtils utils = new VelocityToolsUtils();
		String pageJson = utils.getzkConf("/com/ai/analy/login/pages");
		String notLoginJson = utils.getzkConf("/com/ai/analy/notlogin/pages");
		Object[] notLoginPage2 = {};
		try {
			LOGIN_PAGES = new ObjectMapper().readValue(pageJson.getBytes("UTF-8"), new TypeReference<Object[]>(){});
			notLoginPage2 = new ObjectMapper().readValue(notLoginJson.getBytes("UTF-8"), new TypeReference<Object[]>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		NOTLOGIN_PAGES = ArrayUtils.addAll(notLoginPage1, notLoginPage2);
	}
	
	/**
	 * 判断是否是Ajax请求
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request){
		
		String requestedWith = request.getHeader("x-requested-with"); 
        // 表示是一个AJAX POST请求
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
        	return true;
        }
        return false;
	}
	
	private void setAuthUrls(List<MenuDisplayVO> menus,StaffInfoVo staff){
		
		for (MenuDisplayVO vo : menus) {
			
			if(StringUtils.isNotBlank(vo.getMenuUrl())){
				staff.getAuthUrl().add(vo.getMenuUrl());
			}
			
			//目录不为空
			if(vo.getCatList() != null && vo.getCatList().isEmpty() == false){
				setAuthUrls(vo.getCatList(),staff);
			}
			
			//子菜单不为空
			if(vo.getMenuList() != null && vo.getMenuList().isEmpty() == false){
				setAuthUrls(vo.getMenuList(),staff);
			}
		}
	}
}
