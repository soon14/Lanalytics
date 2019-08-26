/**
 * FlowAnalyController  流量分析-流量概况
 */
package com.ai.analy.controller.busi;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.FlowMapService;
import com.ai.analy.service.interfaces.FlowService;
import com.ai.analy.service.interfaces.VisitorStructureService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.PageInfoVo;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowDateInterpretVo;
import com.ai.analy.vo.flow.FlowMapOverviewVo;
import com.ai.analy.vo.flow.FlowMapVo;
import com.ai.analy.vo.flow.FlowNextVo;
import com.ai.analy.vo.flow.FlowOverviewVo;
import com.ai.analy.vo.flow.FlowSourceVo;
import com.ai.analy.vo.flow.FlowTrendVo;
import com.ai.analy.vo.flow.VisitorDataInterpretVo;
import com.ai.analy.vo.flow.structure.FlowUserTypeVo;

@Controller
@RequestMapping(value = "/flowAnaly")
public class FlowAnalyController extends AppBaseController{
	
	@Autowired
	private FlowService flowService;//流量概况服务
	@Autowired
	private FlowMapService flowMapService;//流量地图服务
	@Autowired
	private VisitorStructureService visitorStructureService;//访客结构服务
	
	/**********************************************************************
	 * 流量概况界面处理逻辑
	 **********************************************************************/
	/**
	 * 流量概况界面
	 * @return
	 */
	@RequestMapping(value = "/flowbasic")
	public String flowbasic(Model model){
		
		//请求参数
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		Date dateFrom = new Date(dateTo.getTime()-6*24*60*60*1000L);
		QueryParamsVo params = getQueryParams(df.format(dateFrom),df.format(dateTo),"","");
		
		//查询解读数据
		FlowDateInterpretVo vo = visitorStructureService.queryFlowOverviewTop(params);
		if(vo == null){
			vo = new FlowDateInterpretVo();
		}
		
		if(StringUtils.isBlank(vo.getSourcePageName())){
			vo.setSourcePageName("暂无数据");
		}
		if(StringUtils.isBlank(vo.getDestPageName())){
			vo.setDestPageName("暂无数据");
		}
		
		VisitorDataInterpretVo vo1 = visitorStructureService.queryVisitorOverviewTop(params);
		if(vo1 == null){
			vo1 = new VisitorDataInterpretVo();
		}
		//计算时间范围
		if(vo1.getTimeList()!= null && vo1.getTimeList().size()>0){
			Map<Integer,Integer> timeMap = new LinkedHashMap<Integer,Integer>(); 
			
			List<String> timeList = vo1.getTimeList();
			
			int curTime = -1;
			for(int i=0;i<timeList.size();i++){
				int iTime = Integer.valueOf(timeList.get(i));
				boolean b = false;
				int index = 0;
				
				if(curTime > iTime) continue;
				
				for(int j=i+1;j<timeList.size();j++){
					index++;
					int jTime = Integer.valueOf(timeList.get(j));
					if(iTime == jTime-index){
						timeMap.put(iTime, jTime+1);
						curTime = jTime+1;
						b = true;
					}
				}
				
				if(b == false){
			        timeMap.put(iTime, iTime+1);
				}
			}
			
			String timeSegment = "";
			int index = 0;
			for (Map.Entry<Integer,Integer> entry : timeMap.entrySet()) {
				if(index > 0)timeSegment +="，";
				timeSegment += String.format("%02d",entry.getKey())+":00-" + String.format("%02d",entry.getValue())+":00";
				index++;
			}
			
			vo1.setTimeSegment(timeSegment);
			
		}else{
			vo1.setTimeSegment("暂无数据");
		}
		
		//返回数据
		model.addAttribute("data", vo);
		model.addAttribute("data1", vo1);
		List<PageInfoVo> pages = flowMapService.getPageInfo(params);
		String target = "idx_mallshoppage";
		if(pages != null && pages.size()>0){
			target = pages.get(0).getPageCode();
		}
		model.addAttribute("target_HOME_PAGE", target);
		return "busi/flowanaly/flowbasic";
	}
	
	/**
	 * 获取流量总览数据
	 * @return
	 */
	@RequestMapping(value = "/getOverviewDate")
	public String getOverviewDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//发起查询请求
		FlowOverviewVo flowOverviewVo = flowService.getFlowOverviewAll(params);
		if(flowOverviewVo == null){
			flowOverviewVo = new FlowOverviewVo();
		}
		
		/**
		 * 访客结构
		 */
		Map<String,Object> userStru = new HashMap<String,Object>();
		
		//新老访客
		params.setUserType(Constants.UserType.QueryType.NEW_AND_OLD);
		List<FlowUserTypeVo> newOldScales =  visitorStructureService.queryFlowUserType(params);
		
		//名单制非名单制客户
		//params.setUserType(Constants.UserType.QueryType.WHITE_LIST);
		//List<FlowUserTypeVo> listNonScales =  visitorStructureService.queryFlowUserType(params);
		
		//PC端与无线端
		params.setUserType(Constants.UserType.QueryType.PC_AND_WIRELESS);
		List<FlowUserTypeVo> pcWlScales =  visitorStructureService.queryFlowUserType(params);
		
		//外链入口与自主访问
//		params.setUserType(Constants.UserType.QueryType.LINK_TYPE);
//		List<FlowUserTypeVo> inOutScales =  visitorStructureService.queryFlowUserType(params);
//		if(inOutScales != null && inOutScales.size()>0){
//			for (FlowUserTypeVo flowUserTypeVo : inOutScales) {
//				flowUserTypeVo.setUv((int)flowUserTypeVo.getSessions());
//			}
//		}
		
		//渠道购买力度分布
		params.setUserType(Constants.UserType.QueryType.BUY_LEVEL);
		List<FlowUserTypeVo> listBuyLevelScales =  visitorStructureService.queryFlowUserType(params);
		
		userStru.put("newOldScales", newOldScales);
		//userStru.put("listNonScales", listNonScales);
		userStru.put("pcWlScales", pcWlScales);
		//userStru.put("inOutScales", inOutScales);
		userStru.put("listBuyLevelScales", listBuyLevelScales);
		
		//设置结果
		model.addAttribute("data", flowOverviewVo);
		model.addAttribute("pieChartData", this.toJsonString(userStru));
		
		return "busi/flowanaly/basic/overview";
	}
	
	/**
	 * 获取流量趋势数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowTrendDate")
	public String getFlowTrendDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//发起查询请求
		FlowTrendVo flowTrendVo = flowService.getFlowOverviewTimelyVo(params);
		if(flowTrendVo == null){
			flowTrendVo = new FlowTrendVo();
		}
		//设置结果
		model.addAttribute("data", flowTrendVo);
		return "busi/flowanaly/basic/trend";
	}
	
	/**
	 * 获取流量地图数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowMapDate")
	public String getFlowMapDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//发起查询请求
		List<FlowMapVo> flowMapList = flowMapService.queryFlowMap(params);
		
		//设置结果
		model.addAttribute("data", flowMapList);
		return "busi/flowanaly/basic/map";
	}
	
	/**
	 * 导出流量地图数据
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportMap.xls")
	public String exportFlowMapData(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(0);

		// 发起查询请求
		List<FlowMapVo> flowMapList = flowMapService.queryFlowMap(params);
		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("省份");
		excelData.getTitleList().add("访客数");
		excelData.getTitleList().add("百分比");

		DecimalFormat df = new DecimalFormat("#0.0");
		for (FlowMapVo flowMap : flowMapList) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(flowMap.getProvinceName());
			rowData.add(String.valueOf(flowMap.getUv()));
			rowData.add(df.format(flowMap.getScale() * 100) + "%");

			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String fileName = "流量地图(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
	
	/**
	 * 获取流量来源去向数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowDirectionDate")
	public String getFlowDirectionDate(Model model){
		
		QueryParamsVo params = this.getQueryParams();
		
		//设置首页还是商品详情页
		String target = this.getParam("target");
		params.setTarget(target);
		
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(5);
		this.setPageInfo(params);
		
		//查询数据
		
		//流量来源列表
		PageBean<FlowSourceVo> sourcePv = flowMapService.queryFlowSource(params);
		
		//流量去向列表
		PageBean<FlowNextVo> descPv = flowMapService.queryFlowNext(params);
		
		//页面访客数
		params.setTarget("");
		List<FlowMapOverviewVo> results = flowMapService.queryFlowMapOverview(params);
		List<FlowMapOverviewVo> targetDatas = new ArrayList<FlowMapOverviewVo>();
		
		//设置target用于界面做控制
		for (PageInfoVo page : flowMapService.getPageInfo(params)) {
			
			if("idx_mallsearch".equals(page.getPageCode()) || "idx_pointsearch".equals(page.getPageCode())){
				continue;
			}
			
			boolean exist = false;
			for (FlowMapOverviewVo flowMapOverviewVo : results) {
				if(page.getPageCode().equals(flowMapOverviewVo.getPageName())){
					flowMapOverviewVo.setPageShowName(page.getPageName());
					targetDatas.add(flowMapOverviewVo);
					exist = true;
					break;
				}
			}
			
			if(exist == false){
				FlowMapOverviewVo tempVo = new FlowMapOverviewVo();
				tempVo.setPageName(page.getPageCode());
				tempVo.setPageShowName(page.getPageName());
				targetDatas.add(tempVo);
			}
		}
		
		model.addAttribute("target",target);
		model.addAttribute("data", targetDatas);
		model.addAttribute("sourcePage", sourcePv);
		model.addAttribute("descPage", descPv);
		model.addAttribute("showPage", this.getParam("showPage"));
		
		return "busi/flowanaly/basic/direction";
	}
	
	/**
	 * 获取流量来源分页数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowDirectionSourceDate")
	public String getFlowDirectionSourceDate(Model model){
		
		QueryParamsVo params = this.getQueryParams();
		
		//设置首页还是商品详情页
		params.setTarget("");
		String target = this.getParam("target");
		if(StringUtils.isEmpty(target) == false){
		    params.setTarget(target);
		}
		
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(5);
		this.setPageInfo(params);
		
		//流量来源列表
		PageBean<FlowSourceVo> sourcePv = flowMapService.queryFlowSource(params);
		
		model.addAttribute("sourcePage", sourcePv);
		model.addAttribute("showPage", true);
		
		return "busi/flowanaly/basic/direction_srctb";
	}
	
	/**
	 * 获取流量去向分页数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowDirectionDestDate")
	public String getFlowDirectionDestDate(Model model){
		
		QueryParamsVo params = this.getQueryParams();
		
		//设置首页还是商品详情页
		params.setTarget("");
		String target = this.getParam("target");
		if(StringUtils.isEmpty(target) == false){
		    params.setTarget(target);
		}
		
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(5);
		this.setPageInfo(params);
		
		//流量去向列表
		PageBean<FlowNextVo> descPv = flowMapService.queryFlowNext(params);
		
		model.addAttribute("descPage", descPv);
		model.addAttribute("showPage", true);
		
		return "busi/flowanaly/basic/direction_desttb";
	}
}