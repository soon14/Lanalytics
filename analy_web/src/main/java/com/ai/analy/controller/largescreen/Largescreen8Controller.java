/**
 * FlowAnalyController  流量分析-流量概况
 */
package com.ai.analy.controller.largescreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.FlowMapService;
import com.ai.analy.service.interfaces.FlowService;
import com.ai.analy.service.interfaces.VisitorStructureService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowMapVo;
import com.ai.analy.vo.flow.FlowSourceVo;
import com.ai.analy.vo.flow.FlowTrendVo;
import com.ai.analy.vo.flow.structure.FlowUserTypeVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;

@Controller
@RequestMapping(value = "/largeScreen")
public class Largescreen8Controller extends AppBaseController{
	
	@Autowired
	private FlowService flowService;//流量概况服务
	@Autowired
	private FlowMapService flowMapService;//流量地图服务
	@Autowired
	private VisitorStructureService visitorStructureService;//访客结构服务
	
	/**********************************************************************
	 * 大屏8
	 **********************************************************************/
	/**
	 * 流量概况界面
	 * @return
	 */
	@RequestMapping(value = "/screen8")
	public String screen9(Model model){
		
		return "busi/largescreen/largescreen8";
	}
	
	/**
	 * 获取流量地图数据
	 * @return
	 */
	@RequestMapping(value = "/flowLine")
	public Object flowLine(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		
		//流量趋势
		FlowTrendVo flowTrendVo = flowService.getFlowOverviewTimelyVo(params);
		if(flowTrendVo == null){
			flowTrendVo = new FlowTrendVo();
		}
		
		//设置结果
		Map<String,Object> resData = new HashMap<String,Object>();
		resData.put("flowline", flowTrendVo);
		
		return resData;
	}
	
	
	/**
	 * 流量地图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/flowMap")
	public String flowMap(Model model){
		
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		
		//UV 身份分布
		List<FlowMapVo> flowMapList = flowMapService.queryFlowMap(params);
		
		model.addAttribute("mapData", flowMapList);
		
		return "busi/largescreen/largescreen8/map";
	}
	
	/**
	 * 分布图
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/flowPie")
	public Object flowPie(Model model){
		
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		
		//区域分布
		List<StructureSectionVo> regionList = visitorStructureService.queryFlowUserRegionT(params);
		
		//渠道购买力度分布
		params.setUserType(Constants.UserType.QueryType.BUY_LEVEL);
		List<FlowUserTypeVo> listBuyLevelScales =  visitorStructureService.queryFlowUserType(params);
		
		Map<String,Object> resData = new HashMap<String,Object>();
		resData.put("regionList", regionList);
		resData.put("listBuyLevelScales", listBuyLevelScales);
		
		return resData;
	}
	
	/**
	 * 流量来源Top5
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/flowSource")
	public String flowSource(Model model){
		
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageSize(5);
		params.setPageNo(1);
		params.setTarget("");
		
		//流量来源列表
		PageBean<FlowSourceVo> sourcePv = flowMapService.queryFlowSource(params);
		
		model.addAttribute("sourcePage", sourcePv);
		return "busi/largescreen/largescreen8/source";
	}
}