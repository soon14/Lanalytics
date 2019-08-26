package com.ai.analy.controller.largescreen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.FlowService;
import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowOverviewVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;

@Controller
@RequestMapping(value = "/largeScreen")
public class Largescreen9Controller extends AppBaseController{
	
	@Autowired
	private FlowService flowService;//流量概况服务
	@Autowired
	private GoodsAnalyService goodsAnalyService;
	
	/**********************************************************************
	 * 大屏商品分析
	 **********************************************************************/
	/**
	 * 大屏9
	 * @return
	 */
	@RequestMapping(value = "/screen9")
	public String screen9(Model model){
		
		return "busi/largescreen/largescreen9";
	}
	
	/**
	 * 大屏9
	 * @return
	 */
	@RequestMapping(value = "/left")
	public String left(Model model){
		
		return "busi/largescreen/largescreen9/left";
	}
	
	/**
	 * 大屏9
	 * @return
	 */
	@RequestMapping(value = "/right")
	public String right(Model model){
		
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		
		//商品概况
		GoodsOverviewVo goodsOverviewVo = goodsAnalyService.getGoodsOverview(params);
		if(goodsOverviewVo == null){
			goodsOverviewVo = new GoodsOverviewVo();
		}
		
		//流量概况
		FlowOverviewVo flowOverviewVo = flowService.getFlowOverviewAll(params);
		if(flowOverviewVo == null){
			flowOverviewVo = new FlowOverviewVo();
		}
		
		//商品交易TOP5
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(5);
		this.setPageInfo(params);
		//设置查询的内容
		params.setOrderTypeName(Constants.OrderTypeName.ORDER_BY_PAY_COUNT);
		params.setOrderWay(Constants.OrderWay.ORDER_DESC);
		
		//查询数据
		PageBean<GdsRankVo> pages = goodsAnalyService.getGdsRankingList(params);
		
		//返回数据
		model.addAttribute("goodsOverviewVo", goodsOverviewVo);
		model.addAttribute("flowOverviewVo", flowOverviewVo);
		model.addAttribute("data", pages);
		
		return "busi/largescreen/largescreen9/right";
	}
}