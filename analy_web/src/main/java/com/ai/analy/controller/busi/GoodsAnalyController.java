package com.ai.analy.controller.busi;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.GdsCategoryVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.goods.GdsSaleTrendVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;
import com.ai.paas.utils.MoneyUtil;

@Controller
@RequestMapping(value = "/goodsAnaly")
public class GoodsAnalyController extends AppBaseController {

	@Autowired
	private GoodsAnalyService goodsAnalyService;

	/**
	 * 流量概况界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/basic")
	public String goodsbasic(Model model) {

		model.addAttribute("ORDER_BY_UV", Constants.OrderTypeName.ORDER_BY_UV);
		model.addAttribute("ORDER_BY_PAY_COUNT", Constants.OrderTypeName.ORDER_BY_PAY_COUNT);
		model.addAttribute("ORDER_BY_PAY_MONEY", Constants.OrderTypeName.ORDER_BY_PAY_MONEY);

		return "busi/goodsanaly/goodsbasic";
	}

	/**
	 * 获取流量总览数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getOverviewDate")
	public String getOverviewDate(Model model) {

		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 查询
		GoodsOverviewVo goodsOverviewVo = goodsAnalyService.getGoodsOverview(params);

		// 返回数据
		model.addAttribute("data", goodsOverviewVo);
		return "busi/goodsanaly/basic/gdoverview";
	}

	/**
	 * 获取流量趋势数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getFlowTrendDate")
	public String getFlowTrendDate(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		// 查询数据
		List<GdsSaleTrendVo> lineList = goodsAnalyService.getGdsSaleTrend(params);

		// 返回数据
		model.addAttribute("data", lineList);
		return "busi/goodsanaly/basic/gdtrend";
	}

	/**
	 * 获取商品排行页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getGoodsTop10Page")
	public String getGoodsTop10Page(Model model) {
		/* 设置商品分类等级参数 */
		String gdsCatg1 = this.getParam("gdscatg1");
		String gdsCatg2 = this.getParam("gdscatg2");

		List<GdsCategoryVo> gdsCatgList2 = null, gdsCatgList3 = null;
		if (!StringUtils.isEmpty(gdsCatg1)) {
			gdsCatgList2 = goodsAnalyService.querySubGdsCategory(gdsCatg1);
		}

		if (!StringUtils.isEmpty(gdsCatg2)) {
			gdsCatgList3 = goodsAnalyService.querySubGdsCategory(gdsCatg2);
		}

		// 返回数据
		model.addAttribute("gdsCatgList2", gdsCatgList2);
		model.addAttribute("gdsCatgList3", gdsCatgList3);

		return "busi/goodsanaly/basic/gdtopow";
	}

	/**
	 * 获取商品排行列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getGoodsTopRateTable")
	public String getGoodsTopRateTable(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		// 设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		// 设置查询的内容
		String orderTypeName = this.getParam("option");
		params.setOrderTypeName(orderTypeName);
		params.setOrderWay(Constants.OrderWay.ORDER_DESC);

		/* 设置商品分类等级参数 */
		String gdsCatg1 = this.getParam("gdscatg1");
		String gdsCatg2 = this.getParam("gdscatg2");
		String gdsCatg3 = this.getParam("gdscatg3");

		if (!StringUtils.isEmpty(gdsCatg1)) {
			params.setCategoryLevel(1);
			params.setCategoryId(gdsCatg1);
		}

		if (!StringUtils.isEmpty(gdsCatg2)) {
			params.setCategoryLevel(2);
			params.setCategoryId(gdsCatg2);
		}

		if (!StringUtils.isEmpty(gdsCatg3)) {
			params.setCategoryLevel(3);
			params.setCategoryId(gdsCatg3);
		}

		// 查询数据
		PageBean<GdsRankVo> pages = goodsAnalyService.getGdsRankingList(params);

		// 返回数据
		model.addAttribute("data", pages);
		model.addAttribute("showPage", this.getParam("showPage"));

		return "busi/goodsanaly/basic/gdtopowtb";
	}

	/**
	 * 导出商品排行列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportGoodsTopRateTable")
	public String exportGoodsTopRateTable(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(0);

		// 设置查询的内容
		String orderTypeName = this.getParam("option");
		params.setOrderTypeName(orderTypeName);
		params.setOrderWay(Constants.OrderWay.ORDER_DESC);

		/* 设置商品分类等级参数 */
		String gdsCatg1 = this.getParam("gdscatg1");
		String gdsCatg2 = this.getParam("gdscatg2");
		String gdsCatg3 = this.getParam("gdscatg3");
		String catName = "全部";

		if (!StringUtils.isEmpty(gdsCatg1)) {
			params.setCategoryLevel(1);
			params.setCategoryId(gdsCatg1);

			GdsCategoryVo category = goodsAnalyService.getGdsCategoryById(gdsCatg1);
			catName = (category != null ? category.getCatName() : "");
		}

		if (!StringUtils.isEmpty(gdsCatg2)) {
			params.setCategoryLevel(2);
			params.setCategoryId(gdsCatg2);

			GdsCategoryVo category = goodsAnalyService.getGdsCategoryById(gdsCatg2);
			catName = catName + "/" + (category != null ? category.getCatName() : "");
		}

		if (!StringUtils.isEmpty(gdsCatg3)) {
			params.setCategoryLevel(3);
			params.setCategoryId(gdsCatg3);

			GdsCategoryVo category = goodsAnalyService.getGdsCategoryById(gdsCatg3);
			catName = catName + "/" + (category != null ? category.getCatName() : "");
		}

		// 查询数据
		PageBean<GdsRankVo> page = goodsAnalyService.getGdsRankingList(params);
		ExcelDataVo excelData = new ExcelDataVo();

		/** 设置excel参数 */
		excelData.getTitleList().add("排行");
		excelData.getTitleList().add("商品名称");
		excelData.getTitleList().add("访客量");
		excelData.getTitleList().add("访客量占比");
		excelData.getTitleList().add("详情页跳失率");
		excelData.getTitleList().add("商品下单转化率");
		excelData.getTitleList().add("交易量");
		excelData.getTitleList().add("交易额");

		DecimalFormat df = new DecimalFormat("#0.0");
		int rank = 1;
		for (GdsRankVo rankVo : page.getRecordList()) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(String.valueOf(rank));
			rowData.add(rankVo.getGdsName());
			rowData.add(String.valueOf(rankVo.getUv()));
			rowData.add(df.format(rankVo.getUvPercent() * 100) + "%");
			rowData.add(df.format(rankVo.getExitRate() * 100) + "%");
			rowData.add(df.format(rankVo.getOrderUvRate() * 100) + "%");
			rowData.add(String.valueOf(rankVo.getPayCount()));
			rowData.add(MoneyUtil.convertCentToYuan(rankVo.getPayMoney()));

			excelData.getDataList().add(rowData);
			rank++;
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		String tabName = "";
		switch (this.getParam("option")) {
		case "uv":
			tabName = "访客量";
			break;
		case "payCount":
			tabName = "交易量";
			break;
		case "payMoney":
			tabName = "交易额";
			break;

		}
		String fileName = "商品"+tabName+"排行-"+ catName +"(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);

		model.addAttribute("excelData", excelData);

		return "";
	}
}