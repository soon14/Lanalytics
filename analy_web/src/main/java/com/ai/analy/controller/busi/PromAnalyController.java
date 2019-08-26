package com.ai.analy.controller.busi;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.PromService;
import com.ai.analy.service.interfaces.SystemConfigService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.comm.ShopInfoVo;
import com.ai.analy.vo.comm.SiteInfoVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.prom.PromIndexDateSumVo;
import com.ai.analy.vo.prom.PromIndexOverviewVo;
import com.ai.analy.vo.prom.PromIndexTrendVo;
import com.ai.analy.vo.prom.PromInfoVo;
import com.ai.analy.vo.prom.PromTypeVo;

@Controller
@RequestMapping(value = "/promAnaly")
public class PromAnalyController extends AppBaseController{
	@Autowired
	private SystemConfigService systemConfigService;//系统配置服务
	@Autowired
	private PromService promService;//促销服务
	
	@RequestMapping(value = "/basic")
	public String prombasic(Model model){
		List<ShopInfoVo> shopList = systemConfigService.getShopList();
		List<SiteInfoVo> siteList = systemConfigService.getSiteList();
		List<PromTypeVo> promTypeList = promService.getValidPromType();
		
		model.addAttribute("shops", shopList);
		model.addAttribute("sites", siteList);
		model.addAttribute("promTypes",promTypeList);
		
		return "busi/promanaly/prombasic";
	}
	
	/**
	 * 查询促销列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getPromList")
	public String getPromList(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);		
		
		params.setPromName(this.getParam("promName"));
		params.setPromTypeCode(this.getParam("promTypeCode"));
		params.setStatus(this.getParam("promStatus"));
		params.setSiteId(this.getParam("siteId"));
		params.setParamShopId(this.getParam("promShopId"));
		
		//查询数据
		PageBean<PromInfoVo> pages = promService.getPromList(params);
		//返回数据
		model.addAttribute("data", pages);
		
		return "busi/promanaly/promtable";
	}
	
	/**
	 * 查询促销指标概览
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getPromIndexOverview")
	public String getPromIndexOverview(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPromId(this.getParam("promId"));
		
		PromIndexOverviewVo promIndexOverview = promService.getPromIndexOverview(params);
		model.addAttribute("data",promIndexOverview);
		
		return "busi/promanaly/promindexoverview";
	}
	
	/**
	 * 查询促销指标趋势
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getPromIndexTrend")
	public String getPromIndexTrend(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPromId(this.getParam("promId"));
		
		PromIndexTrendVo promIndexTrend = promService.getPromIndexTrend(params);
		if(promIndexTrend == null){
			promIndexTrend = new PromIndexTrendVo();
		}
		
		model.addAttribute("data",promIndexTrend);
		model.addAttribute("param",params);
		
		return "busi/promanaly/promindextrend";
	}
	
	/**
	 * 查询促销商品列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getPromGdsTable")
	public String getPromGdsTable(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);	
		params.setPromId(this.getParam("promId"));
		
		//排序
		String orderTypeNames = "uv,pv,exitRate,orderUvRate,orderCountAvg,payCount,payMoney"; 
		String orderTypeName = this.getParam("option");
		if(orderTypeNames.indexOf(orderTypeName) == -1){
			orderTypeName = "uv";
		}
		String sort = this.getParam("sort");
		if(!Constants.OrderWay.ORDER_DESC.equalsIgnoreCase(sort) && !Constants.OrderWay.ORDER_ASC.equalsIgnoreCase(sort)){
			sort = Constants.OrderWay.ORDER_DESC;
		}
		
		params.setOrderTypeName(orderTypeName);
		params.setOrderWay(sort);
		
		PageBean<GdsRankVo> pages = promService.getPromGdsList(params);
		model.addAttribute("data",pages);
		
		return "busi/promanaly/promgdstable";
	}
	
	/**
	 * 查询促销指标-时间对比数据
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getPromIndexDateCompData")
	public String getPromIndexDateCompData(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPromId(this.getParam("promId"));
		
		PromIndexTrendVo promIndexTrend = promService.getPromIndexTrend(params);
		if(promIndexTrend == null){
			promIndexTrend = new PromIndexTrendVo();
		}
		model.addAttribute("data",promIndexTrend);
		
		return "busi/promanaly/promindexdatecomp";
	}
	
	/**
	 * 查询商品指标趋势
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getGdsIndexTrend")
	public String getGdsIndexTrend(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPromId(this.getParam("promId"));
		params.setSkuId(this.getParam("skuId"));
		
		PromIndexTrendVo gdsIndexTrend = promService.getPromIndexTrend(params);
		if(gdsIndexTrend == null){
			gdsIndexTrend = new PromIndexTrendVo();
		}
		model.addAttribute("data",gdsIndexTrend);
		
		return "busi/promanaly/promgdsindexdetail";
	}
	
	/**
	 * 查询商品指标日期列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/getGdsIndexTable")
	public String getGdsIndexTable(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);	
		params.setPromId(this.getParam("promId"));
		params.setSkuId(this.getParam("skuId"));
		
		PageBean<PromIndexDateSumVo> pageBean = promService.getPromIndexDateList(params);
		model.addAttribute("data",pageBean);
		
		return "busi/promanaly/promgdsindextable";
	}
}
