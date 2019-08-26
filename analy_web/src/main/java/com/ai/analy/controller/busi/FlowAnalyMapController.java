/**
 * FlowAnalyController  流量分析-流量地图
 */
package com.ai.analy.controller.busi;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.service.interfaces.FlowMapService;
import com.ai.analy.service.interfaces.UrlDefinitionService;
import com.ai.analy.service.interfaces.VisitorStructureService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.PageInfoVo;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.comm.UrlDefinition;
import com.ai.analy.vo.flow.FlowExitPageVo;
import com.ai.analy.vo.flow.FlowMapOverviewVo;
import com.ai.analy.vo.flow.FlowMapRefDomainVo;
import com.ai.analy.vo.flow.FlowSourceOrderVo;

@Controller
@RequestMapping(value = "/flowAnaly")
public class FlowAnalyMapController extends AppBaseController{
	
	@Autowired
	private FlowMapService flowMapService;//流量地图服务
	@Autowired
	private VisitorStructureService visitorStructureService;//访客结构服务
	@Autowired
	private UrlDefinitionService urlDefinitionService;
	
	/**********************************************************************
	 * 流量地图界面处理逻辑
	 **********************************************************************/
	/**
	 * 流量地图界面
	 * @return
	 */
	@RequestMapping(value = "/flowmap")
	public String  flowmap(Model model){
		
		List<PageInfoVo> tabs = new ArrayList<PageInfoVo>();
		for (PageInfoVo page : flowMapService.getPageInfo(this.getQueryParams())) {
			tabs.add(page);
		}
		List<UrlDefinition> urlDefList = urlDefinitionService.findAll();
		
		model.addAttribute("data", tabs);
		model.addAttribute("urls", urlDefList);
		
		return "busi/flowanaly/flowmap";
	}
	
	/**
	 * 获取流量地图界面头部数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowMapOwDate")
	public String getFlowMapOwDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//查询数据
		List<FlowMapOverviewVo> result = flowMapService.queryFlowMapOverview(params);
		
		FlowMapOverviewVo vo = new FlowMapOverviewVo();
		if(result != null){
			for (FlowMapOverviewVo flowMapOverviewVo : result) {
				if (flowMapOverviewVo.getPageName().equals(params.getTarget())) {
					vo = flowMapOverviewVo;
				}
			}
		}
		
		//返回数据
		model.addAttribute("data", vo);
		return "busi/flowanaly/map/mapoverview";
	}
	
	/**
	 * 获取流量来源数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowMapSourceDate")
	public String getFlowMapSourceDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		
		//查询
		PageBean<FlowSourceOrderVo> flowSourceList = flowMapService.getFlowSourceOrderMom(params);
		
		//返回数据
		model.addAttribute("data", flowSourceList);
		return "busi/flowanaly/map/mapsourcetb";
	}
	
	/**
	 * 获取全平台-流量来源数据
	 * @return
	 */
	//TODO
	@RequestMapping(value = "/getFlowRefDomainData")
	public String getFlowRefDomainData(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		
		//查询
		PageBean<FlowMapRefDomainVo> flowRefDomainPage = flowMapService.queryFlowRefDomain(params);
		
		//返回数据
		model.addAttribute("data", flowRefDomainPage);
		return "busi/flowanaly/map/maprefdomaintb";
	}
	
	
	/**
	 * 导出流量来源数据
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportFlowSourceData.xls")
	public String exportFlowSourceData(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(1);
		params.setPageSize(10000);
		this.setPageInfo(params);
		
		String targetName = this.getParam("targetName");
		
		// 发起查询请求
		PageBean<FlowSourceOrderVo> flowSourceList = flowMapService.getFlowSourceOrderMom(params);
		ExcelDataVo excelData = new ExcelDataVo();
		excelData.getTitleList().add("来源种类");
		excelData.getTitleList().add("访客数");
		excelData.getTitleList().add("环比");
		excelData.getTitleList().add("下单人数");
		excelData.getTitleList().add("环比");
		excelData.getTitleList().add("下单转化率");
		excelData.getTitleList().add("环比");
		DecimalFormat df = new DecimalFormat("#0.0");
		for (FlowSourceOrderVo flowSourceOrder : flowSourceList.getRecordList()) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(flowSourceOrder.getReferPage());
			rowData.add(String.valueOf(flowSourceOrder.getUv()));
			rowData.add(df.format(flowSourceOrder.getUvMom() * 100) + "%");
			rowData.add(""+flowSourceOrder.getOrderUv());
			rowData.add(df.format(flowSourceOrder.getOrderUvMom() * 100) + "%");
			rowData.add(df.format(flowSourceOrder.getOrderRate() * 100) + "%");
			rowData.add(df.format(flowSourceOrder.getOrderRateMom() * 100) + "%");

			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		if("idx_shoppage".equals(params.getTarget())){
			targetName = "商城首页";
		}else if("idx_search".equals(params.getTarget())){
			targetName = "商城搜索页";
		}else if("idx_gdsdetail".equals(params.getTarget())){
			targetName = "商城商品页";
		}
		
		String fileName = "流量来源-"+targetName+"(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
	
	/**
	 * 获取流量去向数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowMapDestDate")
	public String getFlowMapDestDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		params.setUrl(this.getParam("url"));
		
		//查询数据
		PageBean<FlowExitPageVo> flowMapList = flowMapService.queryExitPage(params);
		
		//返回数据
		model.addAttribute("data", flowMapList);
		return "busi/flowanaly/map/mapdesttb";
	}
	
	/**
	 * 导出页面流量数据
	 * @return
	 */
	@RequestMapping(value = "/exportPageFlowData.xls")
	public String exportPageFlowData(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10000);
		this.setPageInfo(params);
		
		//查询数据
		PageBean<FlowExitPageVo> flowMapList = flowMapService.queryExitPage(params);
		ExcelDataVo excelData = new ExcelDataVo();
		excelData.getTitleList().add("排名");
		excelData.getTitleList().add("浏览页面");
		excelData.getTitleList().add("浏览量");
		excelData.getTitleList().add("离开浏览量");
		excelData.getTitleList().add("离开浏览量占比");
		DecimalFormat df = new DecimalFormat("#0.0");
		for (int i=0;i<flowMapList.getRecordList().size();i++){
			FlowExitPageVo flowPage = flowMapList.getRecordList().get(i);
			List<String> rowData = new ArrayList<String>();
			rowData.add(String.valueOf(i+1));
			rowData.add(flowPage.getPageUrl());
			rowData.add(""+flowPage.getSpv());
			rowData.add(""+flowPage.getSexitpv());
			rowData.add(df.format(flowPage.getPercent() * 100) + "%");

			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String fileName = "页面流量(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
}