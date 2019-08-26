package com.ai.analy.controller.busi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.TradeService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.system.util.StaffSessionUtil;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.comm.StaffInfoVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;
import com.ai.analy.vo.trade.TradeAssociationVo;
import com.ai.analy.vo.trade.TradeMapVo;
import com.ai.paas.utils.MoneyUtil;

@Controller
@RequestMapping(value = "/tradeAnaly")
public class TradeChnlAnalyController extends AppBaseController {
	
	@Autowired
	private TradeService tradeService;
	
	/**
	 * 渠道分析界面
	 * @return
	 */
	@RequestMapping(value = "/chnlanaly")
	public String chnlInit() {
		return "busi/tradeanaly/chnlAnalyInit";
	}

	/**
	 * 地域差异
	 * @return
	 */
	@RequestMapping(value = "/chnlRegDiffer")
	public String chnlRegDiffer(Model model) {
		
		QueryParamsVo params = this.getQueryParams();
		
		StaffInfoVo staffInfoVo = StaffSessionUtil.getStaffInfo(session);
		if("9A".equals(staffInfoVo.getProvinceCode())){
			params.setProvinceCode("");
		}else{
			params.setProvinceCode(staffInfoVo.getProvinceCode());
		}
		
		//获取柱状图数据
		List<TradeMapVo> barList = tradeService.queryTradeMap(params);
		if(barList == null) barList = new ArrayList<TradeMapVo>();
		model.addAttribute("barList", barList);
		
		//区域分布
		List<StructureSectionVo> regionList = tradeService.queryUserRegion(params);
		if(regionList == null) regionList = new ArrayList<StructureSectionVo>();
		model.addAttribute("regionList", regionList);
		
		return "busi/tradeanaly/div/chnl/chnlRegDiffer";
	}

	/**
	 * 类型差异
	 * @return
	 */
	@RequestMapping(value = "/chnlTypeDiffer")
	public String chnlTypeDiffer(Model model) {
		
		QueryParamsVo params = this.getQueryParams();
		
		//新老用户
		params.setUserType(Constants.UserType.QueryType.NEW_AND_OLD);
		List<StructureSectionVo> newOldList = tradeService.queryChnlType(params);
		if(newOldList == null) newOldList = new ArrayList<StructureSectionVo>();
		model.addAttribute("newOldList",newOldList);
		
		//连锁和非连锁
//		params.setUserType(Constants.UserType.QueryType.CHAIN);
//		List<StructureSectionVo> chainList = tradeService.queryChnlType(params);
//		if(chainList == null) chainList = new ArrayList<StructureSectionVo>();
//		model.addAttribute("chainList",chainList);
		
		return "busi/tradeanaly/div/chnl/chnlTypeDiffer";
	}

	/**
	 * 行为分布
	 * @return
	 */
	@RequestMapping(value = "/chnlBehavior")
	public String chnlBehavior(Model model) {
		
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		
		QueryParamsVo params = this.getQueryParams();
		params.setDateTo(dateTo);
		
		String month = this.getParam("month");
		
		params.setCatId(month);
		params.setTarget("1");//按件数统计
		List<StructureSectionVo> list1 = tradeService.queryChnlBehavior(params);
		params.setTarget("2");//按次数统计
		List<StructureSectionVo> list2 = tradeService.queryChnlBehavior(params);
		
		model.addAttribute("list1", list1);
		model.addAttribute("list2", list2);
		
		return "busi/tradeanaly/div/chnl/chnlBehavior";
	}
	
	/**
	 * 导出行为分布数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportChnlBehaviorData.xls")
	public String exportTradeMap(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		
		params.setDateTo(dateTo);
		
		String month = this.getParam("month");
		params.setCatId(month);
		String type = this.getParam("type");
		params.setTarget(type); //1:按件数统计，2：按次数统计
		// 发起查询请求
		List<StructureSectionVo> list = tradeService.queryChnlBehavior(params);
		
		ExcelDataVo excelData = new ExcelDataVo();

		if("1".equals(type)){
			excelData.getTitleList().add("购买商品件数");
		}else{
			excelData.getTitleList().add("购买商品次数");
		}
		excelData.getTitleList().add("订单数量");
		excelData.getTitleList().add("占比");

		DecimalFormat df = new DecimalFormat("#0.0");
		for (StructureSectionVo structureSectionVo : list) {
			List<String> rowData = new ArrayList<String>();
			if("1".equals(type)){
				rowData.add(structureSectionVo.getChnlType()+"件");
			}else{
				rowData.add(structureSectionVo.getChnlType()+"次");
			}
			rowData.add(""+structureSectionVo.getOrderCount());
			rowData.add(df.format(structureSectionVo.getScale() * 100)+"%");
			
			excelData.getDataList().add(rowData);
		}

		String fileName = "客户行为分布";
		if("1".equals(type)){
			fileName += "-平均每第订单购买商品数量分布";
		}else{
			fileName += "-订购次数分布";
		}
		fileName += "(最近"+month+"个月)";  
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}

	/**
	 * 主营产品关联
	 * @return
	 */
	@RequestMapping(value = "/gdsRelation")
	public String gdsRelation(Model model) {
		
		//请求参数
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		Date dateFrom = new Date(dateTo.getTime()-29*24*60*60*1000L);
		QueryParamsVo params = getQueryParams(df.format(dateFrom),df.format(dateTo),"","");
		
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(5);
		this.setPageInfo(params);
		
		PageBean<TradeAssociationVo> data = tradeService.queryAssociation(params);
		
		model.addAttribute("data", data);
		
		return "busi/tradeanaly/div/chnl/gdsRelation";
	}

	/**
	 * 导出主营产品关联数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportgdsRelationData.xls")
	public String exportGdsRelation(ModelMap model) {
		//请求参数
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		Date dateFrom = new Date(dateTo.getTime()-29*24*60*60*1000L);
		QueryParamsVo params = getQueryParams(df.format(dateFrom),df.format(dateTo),"","");
		
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10000);
		this.setPageInfo(params);
		// 发起查询请求
		PageBean<TradeAssociationVo> data = tradeService.queryAssociation(params);

		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("商品A名称");
		excelData.getTitleList().add("商品A ISBN号");
		excelData.getTitleList().add("商品A价格");
		excelData.getTitleList().add("商品A库存");
		excelData.getTitleList().add("商品B名称");
		excelData.getTitleList().add("商品B ISBN号");
		excelData.getTitleList().add("商品B价格");
		excelData.getTitleList().add("商品B库存");
		excelData.getTitleList().add("商品AB都买占比");
		excelData.getTitleList().add("关联度A");
		excelData.getTitleList().add("关联度B");

		DecimalFormat moneyDf = new DecimalFormat("#0.00");
		DecimalFormat momDf = new DecimalFormat("#0.0");
		for (TradeAssociationVo tradeAssociationVo : data.getRecordList()) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(tradeAssociationVo.getGdsA().getGdsName());
			rowData.add(tradeAssociationVo.getGdsA().getSkuIsbn());
			rowData.add(MoneyUtil.convertCentToYuan(moneyDf.format(tradeAssociationVo.getGdsA().getPrice())));
			rowData.add(""+tradeAssociationVo.getGdsA().getStock());
			rowData.add(tradeAssociationVo.getGdsB().getGdsName());
			rowData.add(tradeAssociationVo.getGdsB().getSkuIsbn());
			rowData.add(MoneyUtil.convertCentToYuan(moneyDf.format(tradeAssociationVo.getGdsB().getPrice())));
			rowData.add(""+tradeAssociationVo.getGdsB().getStock());
			rowData.add(momDf.format(tradeAssociationVo.getAbScale() * 100)+"%");
			rowData.add(momDf.format(tradeAssociationVo.getaRelev() * 100)+"%");
			rowData.add(momDf.format(tradeAssociationVo.getbRelev() * 100)+"%");
			
			excelData.getDataList().add(rowData);
		}

		String fileName = "主营产品关联(最近30天)";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
}
