package com.ai.analy.controller.busi;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.GdsBrandVo;
import com.ai.analy.vo.goods.GdsCatAnalyVo;
import com.ai.paas.utils.MoneyUtil;

@Controller
@RequestMapping(value = "/goodsAnaly")
public class GoodsAnalyClassifyController extends AppBaseController{

	@Autowired
	private GoodsAnalyService goodsAnalyService;
	
	/**
	 * 商品分类分析界面
	 * @return
	 */
	@RequestMapping(value = "/classify")
	public String goodsclassify(Model model){
		
		
		return "busi/goodsanaly/goodsclassify";
	}
	
	/**
	 * 获取商品分类数据
	 * @return
	 */
	@RequestMapping(value = "/getGoodsClassifyeDate")
	public String getGoodsClassifyeDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		//分类类型
		String categoryType = this.getParam("categoryType");
		if(StringUtils.isBlank(categoryType)){
			categoryType = "1";
		}
		params.setCategoryType(categoryType);
		//分类级别
		if(this.getParam("categoryLevel") != null){
			int categoryLevel = Integer.valueOf(this.getParam("categoryLevel"));
			params.setCategoryLevel(categoryLevel);
		}
		//类型Id
		params.setCatId(this.getParam("catId"));
		//分类Id
		params.setCategoryId(this.getParam("categoryId"));
		
		//查询数据
		List<GdsCatAnalyVo> datas = goodsAnalyService.queryGdsCategory(params);
		
		if(datas != null && datas.size()>0){
			HashMap<String,Integer> groupSize = new HashMap<String,Integer>();
			for (GdsCatAnalyVo gdsCatAnalyVo : datas) {
				String catId = gdsCatAnalyVo.getCatId();
				
				if(groupSize.containsKey(catId) == false){
					groupSize.put(catId, 1);
				}
				groupSize.put(catId, groupSize.get(catId) +1);
			}
			
			//设置分组
			for (GdsCatAnalyVo gdsCatAnalyVo : datas) {
				String catId = gdsCatAnalyVo.getCatId();
				
				if(groupSize.containsKey(catId) == true){
					gdsCatAnalyVo.setCatGroupSize(groupSize.get(catId));
				}
			}
		}
		
		//返回数据
		model.addAttribute("data", datas);
		model.addAttribute("categoryType", categoryType);
		return "busi/goodsanaly/classify/datatb";
	}	
	
	/**
	 * 导出商品分类数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportClassifyData.xls")
	public String exportClassifyData(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(1);
		params.setPageSize(10000);
		this.setPageInfo(params);
		//分类类型
		String categoryType = this.getParam("categoryType");
		if(StringUtils.isBlank(categoryType)){
			categoryType = "1";
		}
		params.setCategoryType(categoryType);
		//分类级别
		if(StringUtils.isNotBlank(this.getParam("categoryLevel"))){
			int categoryLevel = Integer.valueOf(this.getParam("categoryLevel"));
			params.setCategoryLevel(categoryLevel);
		}
		//类型Id
		params.setCatId(this.getParam("catId"));
		//分类Id
		params.setCategoryId(this.getParam("categoryId"));
		
		//查询数据
		List<GdsCatAnalyVo> datas = new ArrayList<>();
		datas = goodsAnalyService.queryGdsCategory(params);
		
		ExcelDataVo excelData = new ExcelDataVo();
		excelData.getTitleList().add("商品类型");
		
		String catTypeName = "";
		if("1".equals(params.getCategoryType())){
			catTypeName = "商品类型";
		}else if("2".equals(params.getCategoryType())){
			excelData.getTitleList().add("商品分类");
			catTypeName = "商品分类";
		}else if("3".equals(params.getCategoryType())){
			excelData.getTitleList().add("价格区间");
			catTypeName = "价格区间";
		}
		excelData.getTitleList().add("商品数");
		excelData.getTitleList().add("访客数");
		excelData.getTitleList().add("点击量");
		excelData.getTitleList().add("销量");
		excelData.getTitleList().add("交易额");
		excelData.getTitleList().add("平均支付转化率");
		DecimalFormat df = new DecimalFormat("#0.00");
		for (GdsCatAnalyVo gdsCatAnalyVo : datas) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(gdsCatAnalyVo.getCatName());
			if("2".equals(params.getCategoryType())){
				String catTypeName2 = "";
				for(int i=0;i<gdsCatAnalyVo.getBrands().size();i++){
					GdsBrandVo tempGdsBrand = gdsCatAnalyVo.getBrands().get(i);
					if(i != 0){
						catTypeName2 += "-";
					}
					catTypeName2 += tempGdsBrand.getBrandName();
				}
				rowData.add(catTypeName2);
			}else if("3".equals(params.getCategoryType())){
				rowData.add(gdsCatAnalyVo.getCategoryTypeName());
			}
			rowData.add(""+gdsCatAnalyVo.getGdsCount());
			rowData.add(""+gdsCatAnalyVo.getUv());
			rowData.add(""+gdsCatAnalyVo.getPv());
			rowData.add(""+gdsCatAnalyVo.getSalesVolumn());
			rowData.add(MoneyUtil.convertCentToYuan(df.format(gdsCatAnalyVo.getTradeMoney())));
			rowData.add(df.format(gdsCatAnalyVo.getPayRate() * 100) + "%");

			excelData.getDataList().add(rowData);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String fileName = "分类分析-"+catTypeName+"(" + dateFormat.format(params.getDateFrom()) + "-"
				+ dateFormat.format(params.getDateTo()) + ")";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
}