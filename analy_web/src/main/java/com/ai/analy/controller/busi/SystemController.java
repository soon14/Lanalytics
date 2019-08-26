package com.ai.analy.controller.busi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.cache.ICache;
import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.UrlDefinitionService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.system.util.StaffSessionUtil;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.UrlDefinition;

@Controller
public class SystemController extends AppBaseController{

	@Autowired
	private UrlDefinitionService urlDefinitionService;
	@Autowired
	private ICache alyCacheSv;
	
	@RequestMapping(value = "/login")
	public String login(Model model){
		return "busi/sys/index";
	}
	
	@RequestMapping(value="/404")
    public String handle(Model  model) {
        return "redirect:/error";
    }
	
	@RequestMapping(value="/error")
    public String error(Model  model) {
        return "/busi/error/404";
    }
	
	/**
	 * 退出系统
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/system/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		
		StaffSessionUtil.removeStaffInfo(session);
		StaffSessionUtil.removeMenuInfo(session);
		
		com.ai.ecp.base.util.WebContextUtil.logout(request, response);
		
		return "busi/sys/index";
	}
	
	/**
	 * ajax请求是session 超时处理
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/system/sessionOut")
	public String sessionOut(HttpServletResponse response){
		response.setStatus(401);
		return "busi/sys/sessionout";
	}
	
	/**
	 * 无访问权限跳转界面
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/system/noAuth")
	public String noAuth(HttpServletResponse response){
		return "busi/sys/noauth";
	}
	
	/*******************************************
	 * 以下为系统管理处理
	 */
	
	/**
	 * url配置管理
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/system/urlconfig")
	public String urlConfig(HttpServletResponse response){
		
		return "busi/sys/urlconfig";
	}
	
	/**
	 * 查询url信息
	 * @return
	 */
	@RequestMapping(value = "/system/geturldates")
	public String getUrlDates(Model model,@ModelAttribute UrlDefinition urlDefinition){
		PageBean<UrlDefinition> pages = urlDefinitionService.getPageBean(urlDefinition);
		model.addAttribute("data", pages);
		return "busi/sys/url/urlstb";
	}
	/**
	 * 保存并刷新缓存
	 * @return
	 */
	@RequestMapping(value = "/system/saveUrlDates")
	public Object saveUrlDates(@ModelAttribute UrlDefinition urlDefinition){
		
		Map<String,String> res = new HashMap<String,String>();
		String result = "false";
		
		if(urlDefinition != null){
			if(StringUtils.isEmpty(urlDefinition.getName()) == false){
				
				int updRes = urlDefinitionService.update(urlDefinition);
				
				if(updRes>0){
					UrlDefinition resUrl = urlDefinitionService.getByIdx(urlDefinition.getIdx());
					
					if(resUrl != null){
						
						if(StringUtils.isEmpty(resUrl.getUrl()) == false && StringUtils.isEmpty(resUrl.getName()) == false){
							alyCacheSv.addItem(2, Constants.REDIS_KEY.URL_KEY_PRE+resUrl.getUrl().trim().toLowerCase(), "name", resUrl.getName());
							result = "true";
						}
					}
				}
			}
		}
		
		urlDefinition = null;
		res.put("result", result);
		return res;
	}
	
	/**
	 * 保存并刷新缓存
	 * @return
	 */
	@RequestMapping(value = "/system/refreshUrlCache")
	public Object refreshUrlCache(){
		
		Map<String,String> res = new HashMap<String,String>();
		
		String result = "false";
		List<UrlDefinition> list = urlDefinitionService.findAll();
		if(list != null && list.size()>0){
			for (UrlDefinition resUrl : list) {
				if(StringUtils.isEmpty(resUrl.getUrl()) == false && StringUtils.isEmpty(resUrl.getName()) == false){
					alyCacheSv.addItem(2, Constants.REDIS_KEY.URL_KEY_PRE+resUrl.getUrl().trim().toLowerCase(), "name", resUrl.getName());
				}
			}
			
			result = "true";
		}
		
		res.put("result", result);
		return res;
	}
}