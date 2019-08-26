package com.ai.analy.controller.busi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.service.interfaces.AdService;
import com.ai.analy.service.interfaces.SystemConfigService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.ad.AdIndexDateSumVo;
import com.ai.analy.vo.ad.AdIndexOverviewVo;
import com.ai.analy.vo.ad.AdIndexTrendVo;
import com.ai.analy.vo.ad.AdInfoVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.PlaceInfoVo;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.comm.ShopInfoVo;
import com.ai.analy.vo.comm.SiteInfoVo;
import com.ai.analy.vo.comm.TemplateInfoVo;

@Controller
@RequestMapping(value = "/adAnaly")
public class AdAnalyController extends AppBaseController{
	@Autowired
	private SystemConfigService systemConfigService;//系统配置服务
	@Autowired
	private AdService adService;//促销服务
	
	@RequestMapping(value = "/basic")
	public String prombasic(Model model){
		List<SiteInfoVo> siteList = systemConfigService.getSiteList();
		List<ShopInfoVo> shopList = systemConfigService.getShopList();
		
		model.addAttribute("shops", shopList);
		model.addAttribute("sites", siteList);
		
		return "busi/adanaly/adbasic";
	}
	
	/**
	 * 查询模板选项
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getTemplateOptions")
	public String getTemplateOptions(Model model){
		String siteId = this.getParam("siteId");
		List<TemplateInfoVo> templateList = systemConfigService.getTemplateList(Integer.valueOf(siteId));
		
		model.addAttribute("templates", templateList);
		
		return "busi/adanaly/templateoptions";
	}
	
	/**
	 * 查询模板位置选项
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getPlaceOptions")
	public String getPlaceOptions(Model model){
		String templateId = this.getParam("templateId");
		List<PlaceInfoVo> placeList = systemConfigService.getPlaceList(Integer.valueOf(templateId));
		
		model.addAttribute("places", placeList);
		
		return "busi/adanaly/placeoptions";
	}
	/**
	 * 查询广告列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getAdList")
	public String getAdList(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);		
		
		params.setSiteId(this.getParam("siteId"));
		params.setTemplateId(this.getParam("templateId"));
		params.setPlaceId(this.getParam("placeId"));
		params.setAdTitle(this.getParam("adTitle"));
		params.setParamShopId(this.getParam("paramShopId"));
		params.setStatus(this.getParam("status"));
		params.setFirstDateFrom(this.getParam("firstDateFrom"));
		params.setFirstDateTo(this.getParam("firstDateTo"));
		params.setSecondDateFrom(this.getParam("secondDateFrom"));
		params.setSecondDateTo(this.getParam("secondDateTo"));
		
		//查询数据
		PageBean<AdInfoVo> pages = adService.queryAdList(params);
		
		//返回数据
		model.addAttribute("data", pages);
		return "busi/adanaly/adtable";
	}
	
	/**
	 * 查询促销指标概览
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getAdIndexOverview")
	public String getAdIndexOverview(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setAdId(this.getParam("adId"));
		
		AdIndexOverviewVo adIndexOverview = adService.getAdIndexOverview(params);
		model.addAttribute("data",adIndexOverview);
		
		return "busi/adanaly/adindexoverview";
	}
	
	/**
	 * 查询广告指标趋势
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getAdIndexTrend")
	public String getAdIndexTrend(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setAdId(this.getParam("adId"));
		
		AdIndexTrendVo adIndexTrend = adService.getAdIndexTrend(params);
		if(adIndexTrend == null){
			adIndexTrend = new AdIndexTrendVo();
		}
		
		model.addAttribute("data",adIndexTrend);
		model.addAttribute("param",params);
		
		return "busi/adanaly/adindextrend";
	}
	
	/**
	 * 查询广告指标趋势
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getAdIndexTrendTable")
	public String getAdIndexTrendTable(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		params.setAdId(this.getParam("adId"));
		
		PageBean<AdIndexDateSumVo> adIndexDateList = adService.getAdIndexDateList(params);
		model.addAttribute("data",adIndexDateList);
		
		return "busi/adanaly/adindextrendtable";
	}
}
