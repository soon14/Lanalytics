package com.ai.analy.system.util;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ai.analy.vo.comm.StaffInfoVo;
import com.ai.analy.vo.comm.MenuDisplayVO;

public final class StaffSessionUtil {

	//行为分析登录信息session 名
	private static final String staffSessionName = "analystaffSession";
	
	private static final String menuSessionName = "analymenuSession";
	
	private static final String platformMenuSessionName = "platformMenuSession";
	
	//沃易购存放session的CookieId
	private static final String cookieSessionId = "ECP_JSESSIONID";				
	
	/**
	 * 设置登陆Session信息
	 * @param session
	 * @param staff
	 */
	public static void setStaffInfo(HttpSession session,StaffInfoVo staff){
		session.setAttribute(staffSessionName, staff);
	}
	
	/**
	 * 获取登陆信息
	 * @param session
	 * @return
	 */
	public static StaffInfoVo getStaffInfo(HttpSession session){
		Object u = session.getAttribute(staffSessionName);
		if(u != null){
			return (StaffInfoVo)u;
		}else{
			return null;
		}
	}
	
	/**
	 * 设置菜单结构到Session 方便界面展示
	 * @param session
	 */
	public static void setMenuDisplayVO(HttpSession session,List<MenuDisplayVO> menus){
		session.setAttribute(menuSessionName, menus);
	}
	
	/**
	 * 设置全平台菜单结构到Session 方便界面展示
	 * @param session
	 */
	public static void setPlatformMenuDisplayVO(HttpSession session,List<MenuDisplayVO> menus){
		session.setAttribute(platformMenuSessionName, menus);
	}
	
	/**
	 * 移除登录Session信息
	 * @param session
	 */
	public static void removeStaffInfo(HttpSession session){
		session.removeAttribute(staffSessionName);
	}
	
	/**
	 * 移除菜单Session信息
	 * @param session
	 */
	public static void removeMenuInfo(HttpSession session){
		session.removeAttribute(menuSessionName);
	}
	
	/**
	 * 移除全平台菜单Session信息
	 * @param session
	 */
	public static void removePlatformMenuInfo(HttpSession session){
		session.removeAttribute(platformMenuSessionName);
	}
	
	/**
	 * 根据根据cookie获取sessionId
	 * @param request
	 * @return
	 */
	public static String getRequestedSessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0))
			return null;
		for (Cookie cookie : cookies) {
			if (cookieSessionId.equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}
}
