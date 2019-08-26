package com.ai.analy.controller.busi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.GdsCategoryVo;
import com.ai.analy.vo.goods.GdsRankVo;

@Controller
@RequestMapping(value = "/goodsAnaly")
public class GoodsAnalyDetailController extends AppBaseController{

	@Autowired
	private GoodsAnalyService goodsAnalyService;
	
	/**
	 * 流量概况界面
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public String goodsbasic(Model model){
		List<GdsCategoryVo> cats = goodsAnalyService.queryCatsForShop(this.getQueryParams().getShopId());
		model.addAttribute("cats", cats);
		return "busi/goodsanaly/goodsdetail";
	}
	
	/**
	 * 获取店铺搜索热词排行数据
	 * @return
	 */
	@RequestMapping(value = "/getGoodsDetailDate")
	public String getGoodsDetailDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		
		//设置查询条件
		String catId = this.getParam("catId");
		String gdsName = this.getParam("gdsName");
		String skuIsbn = this.getParam("skuIsbn");
		params.setCatId(catId);
		params.setGdsName(gdsName);
		params.setSkuIsbn(skuIsbn);
		
		//排序
		String orderTypeNames = "uv,pv,exitRate,orderUvRate,orderCountAvg,payCount,payMoney"; 
		String orderTypeName = this.getParam("option");
		if(orderTypeNames.indexOf(orderTypeName) == -1){
			orderTypeName = "uv";
		}
		
		String sort = this.getParam("sort");
		if(!Constants.OrderWay.ORDER_DESC.equalsIgnoreCase(sort) && !Constants.OrderWay.ORDER_ASC.equalsIgnoreCase(sort)){
			sort = Constants.OrderWay.ORDER_DESC;
		}
		
		params.setOrderTypeName(orderTypeName);
		params.setOrderWay(sort);
		
		//查询数据
		PageBean<GdsRankVo> pages = goodsAnalyService.getGdsDetails(params);
		
		//返回数据
		model.addAttribute("data", pages);
		return "busi/goodsanaly/detail/detailtb";
	}
}