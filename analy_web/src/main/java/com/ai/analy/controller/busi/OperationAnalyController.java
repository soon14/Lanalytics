package com.ai.analy.controller.busi;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.analy.service.interfaces.FlowService;
import com.ai.analy.service.interfaces.TradeService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.system.view.ExcelView;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowMonthSumVo;
import com.ai.analy.vo.flow.FlowTrendVo;
import com.ai.analy.vo.trade.TradeCategorySumVo;
import com.ai.analy.vo.trade.TradeMonthSumVo;
import com.ai.analy.vo.trade.TradeOrdersTypeSumVo;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/operationAnaly")
public class OperationAnalyController extends AppBaseController {
	@Autowired
	private FlowService flowService;// 流量概况服务
	@Autowired
	private TradeService tradeService;// 交易概况服务

	/**********************************************************************
	 * 经营概况界面处理逻辑
	 **********************************************************************/
	/**
	 * 流量概况界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/basic")
	public String operationbasic(Model model) {
		return "busi/operationanaly/operationbasic";
	}
	
	/**
	 * 获取流量月报统计
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getFlowMonthSum")
	public String getFlowMonthSum(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 发起查询请求
		FlowMonthSumVo sumData = flowService.sumFlowMonth(params);

		// 设置结果
		model.addAttribute("sumData", sumData);

		return "busi/operationanaly/basic/sumflow";
	}

	/**
	 * 获取流量月报列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getFlowMonthSumTable")
	public String getFlowMonthSumTable(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		// 设置显示行数
		params.setPageNo(1);
		params.setPageSize(12);
		this.setPageInfo(params);

		// 发起查询请求
		PageBean<FlowMonthSumVo> pageData = flowService.queryFlowMonthSum(params);

		// 设置结果
		model.addAttribute("pageData", pageData);

		return "busi/operationanaly/basic/flowtable";
	}

	/**
	 * 获取流量月报图表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getFlowMonthSumLine")
	public String getFlowMonthSumLine(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 发起查询请求
		PageBean<FlowMonthSumVo> data = flowService.queryFlowMonthSum(params);

		// 设置图表数据
		FlowTrendVo chartData = new FlowTrendVo();
		for (FlowMonthSumVo flowRecord : data.getRecordList()) {
			chartData.getxAxis().add(flowRecord.getMonth());
			chartData.getPvs().add(flowRecord.getPv());
			chartData.getUvs().add(flowRecord.getUv());
			chartData.getRegisterStaffs().add(flowRecord.getRegisterStaff());
			chartData.getAvgPvs().add(flowRecord.getAvgPv());
			chartData.getAvgStays().add(flowRecord.getAvgStay());
			chartData.getBounceRates().add(flowRecord.getBounceRate());
		}

		model.addAttribute("chartData", chartData);

		return "busi/operationanaly/basic/flowline";
	}

	/**
	 * 导出流量月报
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportFlowMonthSum.xls")
	public String exportFlowMonthSum(ModelMap model, HttpServletRequest request, HttpServletResponse respons) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(0);

		// 发起查询请求
		PageBean<FlowMonthSumVo> pageData = flowService.queryFlowMonthSum(params);
		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("月份");
		excelData.getTitleList().add("访客数（uv）");
		excelData.getTitleList().add("访客量（pv）");
		excelData.getTitleList().add("新注册用户数");
		excelData.getTitleList().add("跳失率");
		excelData.getTitleList().add("平均浏览页面");
		excelData.getTitleList().add("平均停留时间（秒）");

		DecimalFormat df = new DecimalFormat("#0.00");
		for (FlowMonthSumVo flowRecord : pageData.getRecordList()) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(flowRecord.getMonth());
			rowData.add(String.valueOf(flowRecord.getUv()));
			rowData.add(String.valueOf(flowRecord.getPv()));
			rowData.add(String.valueOf(flowRecord.getRegisterStaff()));
			rowData.add(df.format(flowRecord.getBounceRate() * 100) + "%");
			rowData.add(df.format(flowRecord.getAvgPv()));
			rowData.add(df.format(flowRecord.getAvgStay()));

			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String fileName = "流量月报(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}

	/**
	 * 获取交易月报
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getTradeMonthSum")
	public String getTradeMonthSum(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		// 设置显示行数
		params.setPageNo(1);
		params.setPageSize(12);
		this.setPageInfo(params);

		// 发起查询请求
		PageBean<TradeMonthSumVo> page = tradeService.queryTradeMonthSum(params);

		// 设置结果
		model.addAttribute("page", page);

		return "busi/operationanaly/basic/trademonth";
	}

	/**
	 * 导出交易月报
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportTradeMonthSum.xls")
	public String exportTradeMonthSum(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(0);

		// 发起查询请求
		PageBean<TradeMonthSumVo> pageData = tradeService.queryTradeMonthSum(params);
		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("月份");
		excelData.getTitleList().add("码洋");
		excelData.getTitleList().add("实洋");
		excelData.getTitleList().add("订单总量");
		excelData.getTitleList().add("支付成功量");
		excelData.getTitleList().add("下单成功率");

		DecimalFormat df = new DecimalFormat("#0.00");
		for (TradeMonthSumVo record : pageData.getRecordList()) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(record.getMonth());
			rowData.add(df.format(record.getOrderMoney() / 100.0));
			rowData.add(df.format(record.getPayMoney() / 100.0));
			rowData.add(String.valueOf(record.getOrderCount()));
			rowData.add(String.valueOf(record.getPayCount()));
			rowData.add(df.format(record.getOrdSuccessRate() * 100) + "%");

			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String fileName = "销售量及订单量统计月报(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}

	/**
	 * 获取商品分类销售报表
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping(value = "/getTradeCategorySum")
	public String getTradeCategorySum(Model model) throws Exception {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 发起查询'图书'子分类销售
		params.setCategoryLevel(2);
		params.setCatId("12125");
		List<TradeCategorySumVo> bookSumList = tradeService.queryTradeCategorySum(params);
		// 发起查询'电子产品'子分类销售
		params.setCategoryLevel(1);
		params.setCatId("1199");
		List<TradeCategorySumVo> digitSumList = tradeService.queryTradeCategorySum(params);

		Map<String, List<TradeCategorySumVo>> catSumMap = new HashMap<String, List<TradeCategorySumVo>>();
		catSumMap.put("图书", bookSumList);
		catSumMap.put("数字产品", digitSumList);

		Map<String, List<String>> chartData = new HashMap<String, List<String>>();
		chartData.put("axis", new ArrayList<String>());
		chartData.put("saleCounts", new ArrayList<String>());
		chartData.put("saleMoneys", new ArrayList<String>());
		for (List<TradeCategorySumVo> list : catSumMap.values()) {
			if (list != null && !list.isEmpty()) {
				for (TradeCategorySumVo vo : list) {
					if (vo.getCatId() != 0) {
						chartData.get("axis").add(vo.getCatName());
						chartData.get("saleCounts").add(String.valueOf(vo.getSaleCount()));
						chartData.get("saleMoneys").add(String.valueOf(vo.getSaleMoney()));
					}
				}
			}
		}

		// 设置结果
		model.addAttribute("catSumMap", catSumMap);
		model.addAttribute("chartData", new ObjectMapper().writeValueAsString(chartData));

		return "busi/operationanaly/basic/tradecategory";
	}

	/**
	 * 导出交易月报
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportTradeCategorySum.xls")
	public String exportTradeCategorySum(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 发起查询'图书'子分类销售
		params.setCategoryLevel(2);
		params.setCatId("12125");
		List<TradeCategorySumVo> bookSumList = tradeService.queryTradeCategorySum(params);
		// 发起查询'图书'子分类销售
		params.setCategoryLevel(1);
		params.setCatId("1199");
		List<TradeCategorySumVo> digitSumList = tradeService.queryTradeCategorySum(params);

		Map<String, List<TradeCategorySumVo>> catSumMap = new HashMap<String, List<TradeCategorySumVo>>();
		catSumMap.put("图书", bookSumList);
		catSumMap.put("数字产品", digitSumList);

		// 设置excel参数
		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("");
		excelData.getTitleList().add("");
		excelData.getTitleList().add("总计销售数量");
		excelData.getTitleList().add("总销售金额");

		DecimalFormat df = new DecimalFormat("#0.00");
		for (List<TradeCategorySumVo> list : catSumMap.values()) {
			if (list != null && !list.isEmpty()) {
				for (TradeCategorySumVo vo : list) {
					List<String> rowData = new ArrayList<String>();
					rowData.add(vo.getParentCatName());
					rowData.add(vo.getCatName());
					rowData.add(String.valueOf(vo.getSaleCount()));
					rowData.add(df.format(vo.getSaleMoney() / 100.0));

					excelData.getDataList().add(rowData);
				}
			}
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String fileName = "商品分类销售情况报表(截止至" + dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}

	/**
	 * 获取交易月报
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@RequestMapping(value = "/getTradeOrdersTypeSum")
	public String getTradeOrdersTypeSum(Model model) throws Exception {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 发起查询请求
		List<TradeOrdersTypeSumVo> list = tradeService.queryTradeOrdersTypeSum(params);

		// 封装饼图数据
		Map<String, List<String>> chartData = new HashMap<String, List<String>>();
		chartData.put("axis", new ArrayList<String>());
		chartData.put("staffNums", new ArrayList<String>());
		chartData.put("payAmounts", new ArrayList<String>());
		for (TradeOrdersTypeSumVo vo : list) {
			chartData.get("axis").add(vo.getOrdersType());
			chartData.get("staffNums").add(String.valueOf(vo.getStaffNum()));
			chartData.get("payAmounts").add(String.valueOf(vo.getPayAmount()));
		}

		// 设置结果
		model.addAttribute("list", list);
		model.addAttribute("chartData", new ObjectMapper().writeValueAsString(chartData));

		return "busi/operationanaly/basic/orderstype";
	}

	/**
	 * 导出购买分布
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportTradeOrdersTypeSum.xls")
	public String exportTradeOrdersTypeSum(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 发起查询请求
		List<TradeOrdersTypeSumVo> list = tradeService.queryTradeOrdersTypeSum(params);

		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("购买次数");
		excelData.getTitleList().add("客户数量");
		excelData.getTitleList().add("购买金额");
		excelData.getTitleList().add("平均消费");

		DecimalFormat df = new DecimalFormat("#0.00");
		for (TradeOrdersTypeSumVo vo : list) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(vo.getOrdersType());
			rowData.add(String.valueOf(vo.getStaffNum()));
			rowData.add(df.format(vo.getPayAmount()/ 100.0));
			rowData.add(String.valueOf(vo.getAvgPayAmount()/100.0));

			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String fileName = "订购次数分布报表(截止至" + dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
}
