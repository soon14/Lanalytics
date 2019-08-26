/**
 * FlowAnalyController  流量分析-搜索排行
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

import com.ai.analy.service.interfaces.SearchRankingService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.search.FlowSearchOverviewVo;
import com.ai.analy.vo.search.SearchRankingShopVo;
import com.ai.analy.vo.search.SearchRankingVo;
import com.alibaba.dubbo.common.utils.StringUtils;

@Controller
@RequestMapping(value = "/flowAnaly")
public class FlowAnalySearchController extends AppBaseController{
	
	@Autowired
	private SearchRankingService searchRankingService;//搜索排行服务
	
	/**********************************************************************
	 * 搜索排行界面处理逻辑
	 **********************************************************************/
	/**
	 * 流量搜索排行界面
	 * @return
	 */
	@RequestMapping(value = "/flowsearch")
	public String  flowmap(Model model){
		
		return "busi/flowanaly/flowsearch";
	}
	
	@RequestMapping(value = "/getSearchOverviewData")
	public String  getSearchOverviewData(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//查询数据
		FlowSearchOverviewVo vo = searchRankingService.getFlowSearchOverviw(params);
		if(vo == null){
			vo = new FlowSearchOverviewVo();
		}
		
		//返回数据
		model.addAttribute("data",vo);
		return "busi/flowanaly/search/overview";
	}
	
	/**
	 * 获取店铺搜索热词排行数据
	 * @return
	 */
	@RequestMapping(value = "/getShopSearchDate")
	public String getShopSearchDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		
		//查询数据
		PageBean<SearchRankingShopVo> page = searchRankingService.getHotKeyWordsShop(params);
		
		//返回数据
		model.addAttribute("data", page);
		model.addAttribute("showPage", this.getParam("showPage"));
		return "busi/flowanaly/search/shopsearchwordtb";
	}
	
	/**
	 * 获取平台热词TOP10
	 * @return
	 */
	@RequestMapping(value = "/getPlatformSearchDate")
	public String getPlatformSearchDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		
		//查询数据
		PageBean<SearchRankingVo> page = searchRankingService.getHotKeyWords(params);
		
		//返回数据
		model.addAttribute("data", page);
		model.addAttribute("showPage", this.getParam("showPage"));
		return "busi/flowanaly/search/platformsearchwordtb";
	}
	
	
	/**
	 * 导出平台热词
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportPlatformSearch.xls")
	public String exportPlatformSearch(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(1);
		params.setPageSize(10000);
		this.setPageInfo(params);

		// 发起查询请求
		PageBean<SearchRankingVo> page = searchRankingService.getHotKeyWords(params);
		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("排名");
		excelData.getTitleList().add("搜索关键词");
		excelData.getTitleList().add("检索量");
		excelData.getTitleList().add("占比");
		excelData.getTitleList().add("检索量环比");

		DecimalFormat df = new DecimalFormat("#0.0");
		for (int i=0;i<page.getRecordList().size();i++) {
			SearchRankingVo searchRanking = page.getRecordList().get(i);
			List<String> rowData = new ArrayList<String>();
			rowData.add(String.valueOf(i+1));
			rowData.add(searchRanking.getKeyWords());
			rowData.add(""+searchRanking.getSearchNum());
			rowData.add(df.format(searchRanking.getSearchNumPercent() * 100) + "%");
			rowData.add(df.format(searchRanking.getSearchNumMom() * 100) + "%");
			
			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String fileName = "平台热词(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
}