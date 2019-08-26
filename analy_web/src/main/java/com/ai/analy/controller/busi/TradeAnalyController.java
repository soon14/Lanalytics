package com.ai.analy.controller.busi;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.service.interfaces.TradeService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.system.util.StaffSessionUtil;
import com.ai.analy.vo.comm.AreaInfoVo;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.comm.StaffInfoVo;
import com.ai.analy.vo.goods.GdsSaleTrendVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;
import com.ai.analy.vo.trade.TradeMapVo;
import com.ai.analy.vo.trade.TradeRankTrendVo;
import com.ai.analy.vo.trade.TradeRankVo;
import com.ai.paas.utils.MoneyUtil;

@Controller
@RequestMapping(value = "/tradeAnaly")
public class TradeAnalyController extends AppBaseController {
	
	@Autowired
	private GoodsAnalyService goodsAnalyService;
	@Autowired
	private TradeService tradeService;
	
	/**
	 * 交易概况界面
	 * @return
	 */
	@RequestMapping(value = "/basic")
	public String tradeInit(Model model) {
		
		//请求参数
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		Date dateFrom = new Date(dateTo.getTime()-6*24*60*60*1000L);
		QueryParamsVo params = getQueryParams(df.format(dateFrom),df.format(dateTo),"","");
		
		//7天数据
		GoodsOverviewVo goodsOverviewVo = tradeService.getTradeOverview(params);
		if(goodsOverviewVo == null){
			goodsOverviewVo = new GoodsOverviewVo();
		}
		
		//交易排行商品类目tab页数据
		params.setCatId("");
		List<String> shopIds = new ArrayList<String>();
		shopIds.add(params.getShopId());
		List<TradeRankVo> radeRankList = tradeService.queryTradeRank(params,shopIds);
		
		StaffInfoVo staffInfoVo = StaffSessionUtil.getStaffInfo(session);
		
		//返回数据
		model.addAttribute("goodsOverviewVo", goodsOverviewVo);
		model.addAttribute("provinceCode", "");
		//TODO
		//model.addAttribute("provinceCode", staffInfoVo.getShopProvinceCode());
		model.addAttribute("radeRankList", radeRankList);
		
		return "busi/tradeanaly/tradeAnalyInit";
	}

	@RequestMapping(value = "/getAreaInfo")
	public Object getAreaInfo(Model model) {
		
		String parentAreaCode = this.getParam("parentAreaCode");
		
		List<AreaInfoVo> areaList = tradeService.queryAreaByParentCode(parentAreaCode);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		map.put("areaList", areaList);
		
		return map;
	}
	
	/**
	 * 交易总览
	 * @return
	 */
	@RequestMapping(value = "/tradeOverview")
	public String tradeOverview(Model model) {
		
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		
		String provinceCode = this.getParam("provinceCode");
		String cityCode = this.getParam("cityCode");
		
		params.setProvinceCode(provinceCode);
		params.setCityCode(cityCode);
		
		//商品概况
		GoodsOverviewVo goodsOverviewVo = tradeService.getTradeOverview(params);
		if(goodsOverviewVo == null){
			goodsOverviewVo = new GoodsOverviewVo();
		}
		
		//返回数据
		model.addAttribute("goodsOverviewVo", goodsOverviewVo);
		
		return "busi/tradeanaly/div/trade/tradeOverview";
	}

	/**
	 * 交易趋势
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tradeTrend")
	public String tradeTrend(Model model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		// 查询数据
		List<GdsSaleTrendVo> lineList = goodsAnalyService.getGdsSaleTrend(params);
		// 返回数据
		model.addAttribute("data", lineList);
		
		return "busi/tradeanaly/div/trade/tradeTrend";
	}

	/**
	 * 交易地域分布
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tradeMap", method = RequestMethod.POST)
	public String tradeMap(Model model) {
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//发起查询请求
		List<TradeMapVo> tradeMapList = tradeService.queryTradeMap(params);
		if(tradeMapList == null){
			tradeMapList = new ArrayList<TradeMapVo>();
		}
		
		List<TradeMapVo> tradeMapList1 = new ArrayList<TradeMapVo>(java.util.Arrays.asList(new TradeMapVo[tradeMapList.size()]));
		Collections.copy(tradeMapList1, tradeMapList);
		
		//按交易额排序
		Collections.sort(tradeMapList, new Comparator<TradeMapVo>(){
			public int compare(TradeMapVo arg0, TradeMapVo arg1) {
                return arg0.getTradeAmount() > arg1.getTradeAmount()? -1:1;
            }
		});
		
		//按交易量排序
		Collections.sort(tradeMapList1, new Comparator<TradeMapVo>(){
			public int compare(TradeMapVo arg0, TradeMapVo arg1) {
                return arg0.getTradeCount() > arg1.getTradeCount()? -1:1;
            }
		});
		
		
		//设置结果
		model.addAttribute("data1", tradeMapList);
		model.addAttribute("data2", tradeMapList1);
		
		return "busi/tradeanaly/div/trade/tradeMap";
	}
	
	
	/**
	 * 导出交易地域分布
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportTradeMap.xls")
	public String exportTradeMap(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();

		// 发起查询请求
		List<TradeMapVo> tradeMapList = tradeService.queryTradeMap(params);
		if(tradeMapList == null){
			tradeMapList = new ArrayList<TradeMapVo>();
		}
		
		//按交易量排序
		Collections.sort(tradeMapList, new Comparator<TradeMapVo>(){
			public int compare(TradeMapVo arg0, TradeMapVo arg1) {
                return arg0.getTradeCount() > arg1.getTradeCount()? -1:1;
            }
		});
		
		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("省份");
		excelData.getTitleList().add("交易量");
		excelData.getTitleList().add("交易量百分比");
		excelData.getTitleList().add("交易额");
		excelData.getTitleList().add("交易额百分比");

		DecimalFormat df = new DecimalFormat("#0.0");
		for (TradeMapVo tradeMapVo : tradeMapList) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(tradeMapVo.getProvinceName());
			rowData.add(""+tradeMapVo.getTradeCount());
			rowData.add(df.format(tradeMapVo.getCountScale()*100)+"%");
			rowData.add(MoneyUtil.convertCentToYuan(tradeMapVo.getTradeAmount()));
			rowData.add(df.format(tradeMapVo.getAmountScale()*100)+"%");
			
			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String fileName = "交易地域分布(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
	
	/**
	 * 交易排行
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tradeRank")
	public String tradeRank(Model model) {
		// 请求参数
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		Date dateFrom = new Date(dateTo.getTime()-6*24*60*60*1000L);
		QueryParamsVo params = getQueryParams(df.format(dateFrom),df.format(dateTo),"","");
		
		/***我的行业排名***/
		params.setCatId(this.getParam("catId"));
		//本期
		List<String> shopIds = new ArrayList<String>();
		shopIds.add(params.getShopId());
		List<TradeRankVo> radeRankList = tradeService.queryTradeRank(params,shopIds);
		
		TradeRankVo curTradeRankVo = null;
		String curShopId = params.getShopId();
		
		if(radeRankList != null && radeRankList.size()>0){
			
			//计算较上期排名
			curTradeRankVo = radeRankList.get(0);
			
			//上期
			params.setDateTo(params.getPreDateTo());
			List<TradeRankVo> preRadeRankList = tradeService.queryTradeRank(params,shopIds);
			
			int preRank = curTradeRankVo.getAmountRank();
			if(preRadeRankList != null && preRadeRankList.size()>0){
				TradeRankVo preTradeRankVo = preRadeRankList.get(0);
				preRank = curTradeRankVo.getAmountRank() - preTradeRankVo.getAmountRank();
				
				curTradeRankVo.setCountCompare((curTradeRankVo.getTradeCount()-preTradeRankVo.getTradeCount())/(double)preTradeRankVo.getTradeCount());
			}
			
			if(preRank <0){
				model.addAttribute("rankPreFlag", "up");
			}else{
				model.addAttribute("rankPreFlag", "down ");
			}
			
			//计算层级
			int rankLevel = 1;
			double countLevel = curTradeRankVo.getCountLevel();
			if(countLevel>=0.2d && countLevel<0.4d){
				rankLevel = 2;
			}
			if(countLevel>=0.4d && countLevel<0.6d){
				rankLevel = 3;
			}
			if(countLevel>=0.6d && countLevel<0.8d){
				rankLevel = 4;
			}
			if(countLevel>=0.8){
				rankLevel = 5;
			}
				
			model.addAttribute("preRank", Math.abs(preRank));
			model.addAttribute("rankLevel", rankLevel);
			model.addAttribute("tradeRankVo", radeRankList.get(0));
		}
		
		/***我的行业排名趋势***/
		params.setDateTo(dateTo);
		
		List<String> preTimes = new ArrayList<String>();
		preTimes.add(df.format(params.getDateTo()));
		cd = Calendar.getInstance();
		cd.setTime(params.getDateTo());
		for(int i=0;i<6;i++){
			cd.add(Calendar.DATE, -7);
			preTimes.add(df.format(cd.getTime()));
		}
		
		List<TradeRankVo> rankTrendList = tradeService.queryTradeRankTrend(params, preTimes);
		
		//分类
		if(rankTrendList != null && rankTrendList.size()>0){
			HashMap<String,TradeRankVo> dataTypes = new HashMap<String,TradeRankVo>();
			HashMap<String,String> gdsTypes = new HashMap<String,String>();
			
			for (TradeRankVo tradeRankVo : rankTrendList) {
				String key = tradeRankVo.getCatId() + df.format(tradeRankVo.getDateTo());
				if(dataTypes.containsKey(key) == false){
					dataTypes.put(key, tradeRankVo);
				}
				
				if(gdsTypes.containsKey(tradeRankVo.getCatId()) == false){
					gdsTypes.put(tradeRankVo.getCatId(),tradeRankVo.getCatName());
				}
			}
			
			List<List<TradeRankTrendVo>> trendLineData = new ArrayList<List<TradeRankTrendVo>>(); 
			List<List<String>> trendTableData = new ArrayList<List<String>>();
			List<String> trendTimeData = new ArrayList<String>();
			
			for (String gdsTpye : gdsTypes.keySet()) {
				
				String catName = gdsTypes.get(gdsTpye);
				
				List<TradeRankTrendVo> amountRankTrend = new ArrayList<TradeRankTrendVo>();
				List<String> amountRank = new ArrayList<String>();
				amountRank.add(catName);
				
				for (int i=preTimes.size();i>0;i--) {
					
					String time = preTimes.get(i-1);
					
					String key = gdsTpye + time;
					
					TradeRankTrendVo tradeRankTrendVo = new TradeRankTrendVo();
					tradeRankTrendVo.setCatName(catName);
					
					cd = Calendar.getInstance();
					try {
						cd.setTime(df.parse(time));
					} catch (ParseException e) {
					}
					
					SimpleDateFormat df1 = new SimpleDateFormat("MM/dd");
					time = df1.format(cd.getTime());
					cd.add(Calendar.DATE, -6);
					String dateF = df1.format(cd.getTime());
					tradeRankTrendVo.setDate(dateF + "-" + time);
					
					if(dataTypes.containsKey(key)){
						TradeRankVo tradeRankVo = dataTypes.get(key);
						//表格数据
						amountRank.add(tradeRankVo.getAmountRank()+"");
						//趋势线数据
						tradeRankTrendVo.setAmountRank(tradeRankVo.getAmountRank());
					}else{
						amountRank.add("-");
						tradeRankTrendVo.setAmountRank(0);
					}
					
					amountRankTrend.add(tradeRankTrendVo);
				}
				
				trendTableData.add(amountRank);
				trendLineData.add(amountRankTrend);
			}
			
			//表格时间轴
			if(trendLineData.size()>0){
				for (TradeRankTrendVo tradeRankTrendVo : trendLineData.get(0)) {
					trendTimeData.add(tradeRankTrendVo.getDate());
				}
			}
			
			model.addAttribute("trendTableData", trendTableData);
			model.addAttribute("trendLineData", trendLineData);
			model.addAttribute("trendTimeData", trendTimeData);
		}
		
		/***平台店铺销量TOP10***/
		params.setDateTo(dateTo);
		params.setShopId("");
		params.setPageSize(10);
		params.setPageNo(1);
		
		//本期
		List<TradeRankVo> rankTopList = tradeService.queryTradeRank(params,null);
		
		//上期
		shopIds = new ArrayList<String>();
		for (TradeRankVo rankVo : rankTopList) {
			shopIds.add(rankVo.getShopId());
		}
		
		cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -31);
		params.setDateTo(cd.getTime());
		List<TradeRankVo> preRankTopList = tradeService.queryTradeRank(params,shopIds);
		boolean isExistShop = false;
		for (TradeRankVo tradeRankVo : rankTopList) {
			
			String key = tradeRankVo.getShopId();
			
			if(preRankTopList != null){
				for (TradeRankVo tradeRankVo2 : preRankTopList) {
					String key2 = tradeRankVo2.getShopId();
					
					if(key.equals(key2)){
						tradeRankVo.setCountCompare((tradeRankVo.getTradeCount()-tradeRankVo2.getTradeCount())/(double)tradeRankVo2.getTradeCount());
					}
				}
			}
			
			if(curShopId.equals(tradeRankVo.getShopId())){
				isExistShop = true;
			}
		}
		
		//如果前10不存在当前店铺，则将当前店铺加入最后
		if(isExistShop == false){
			rankTopList.add(curTradeRankVo);
		}
		
		model.addAttribute("rankTopList",rankTopList);
		model.addAttribute("curShopId",curShopId);
		
		return "busi/tradeanaly/div/trade/tradeRank";
	}
}
