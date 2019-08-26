package com.ai.analy.controller.busi;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.service.interfaces.AbnormalGdsService;
import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.AbnormalGdsCountVo;
import com.ai.analy.vo.goods.AbnormalGdsVo;
import com.ai.analy.vo.goods.ComplaintGdsVo;
import com.ai.analy.vo.goods.ReturnGdsVo;
import com.alibaba.dubbo.common.utils.StringUtils;

@Controller
@RequestMapping(value = "/goodsAnaly")
public class GoodsAnalyAbnormalController extends AppBaseController{

	@Autowired
	private GoodsAnalyService goodsAnalyService;
	
	@Autowired
	private AbnormalGdsService abnormalGdsService;
	
	/**
	 * 商品异常商品界面
	 * @return
	 */
	@RequestMapping(value = "/abnormal")
	public String goodsabnormal(Model model){
		//请求参数
		QueryParamsVo params = this.getAbnormalParams(false);
		
		//异常商品数量
		AbnormalGdsCountVo abnormalGdsCountVo = abnormalGdsService.queryCount(params);
		
		model.addAttribute("abnormalGdsCountVo",abnormalGdsCountVo);
		
		return "busi/goodsanaly/goodsabnormal";
	}
	
	@RequestMapping(value = "/getGoodsAbnormalSvDate")
	public String getGoodsAbnormalSvDate(Model model){
		
		QueryParamsVo params = this.getAbnormalParams(true);
		
		//最近7天投诉数据
		ComplaintGdsVo complaintGdsVo7 = abnormalGdsService.Complaint(params);
		//最近7天商品退货数据
		ReturnGdsVo returnGdsVo7 = abnormalGdsService.returnGds(params);
		
		//设置为最近30天
		Calendar cd = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateToStr = df.format(cd.getTime());
		cd.add(Calendar.DATE, 0-29);
		String dateFromStr = df.format(cd.getTime());
		params = this.getQueryParams(dateFromStr, dateToStr, null, null);
		
		//最近30天投诉数据
		ComplaintGdsVo complaintGdsVo30 = abnormalGdsService.Complaint(params);
		//最近30天商品退货数据
		ReturnGdsVo returnGdsVo30 = abnormalGdsService.returnGds(params);
		
		model.addAttribute("complaintGdsVo7",  complaintGdsVo7);
		model.addAttribute("complaintGdsVo30", complaintGdsVo30);
		
		model.addAttribute("returnGdsVo7",  returnGdsVo7);
		model.addAttribute("returnGdsVo30", returnGdsVo30);
		
		return "busi/goodsanaly/abnormal/svanaly";
	}
	
	/**
	 * 获取异常商品数据
	 * @return
	 */
	@RequestMapping(value = "/getGoodsAbnormalDate")
	public String getGoodsAbnormalDate(Model model){
		//请求参数
		QueryParamsVo params = this.getAbnormalParams(false);
		
		//设置显示行数
		params.setPageNo(1);
		params.setPageSize(10);
		this.setPageInfo(params);
		
		String abnormaType = this.getParam("abnormaType");
		if(StringUtils.isBlank(abnormaType)){
			abnormaType = "1";
		}
		
		//查询数据
		PageBean<AbnormalGdsVo> pages = null;
				
		String pageName = "flowtb";
		if("1".equals(abnormaType)){
			//流量下跌
			pageName = "flowtb";
			pages = abnormalGdsService.gdsFlowDown(params);
		}
		
		if("2".equals(abnormaType)){
			//支付转换率下跌
			pageName = "payratetb";
			pages = abnormalGdsService.payUvRateDown(params);
		}
		
		if("3".equals(abnormaType)){
			//零销量
			pageName = "salestb";
			pages = abnormalGdsService.sellZero(params);
		}
		
		if("4".equals(abnormaType)){
			//零库存
			pageName = "stocktb";
			pages = abnormalGdsService.stockZero(params);
		}
		
		if("5".equals(abnormaType)){
			//库存预警
			pageName = "stockwarntb";
			pages = abnormalGdsService.stockWarning(params);
		}
		
		if(pages == null){
			pages = new PageBean<AbnormalGdsVo>();
		}
		
		//返回数据
		model.addAttribute("data", pages);
		
		//2015.08.10~2015.08.16
		//最近7天
		model.addAttribute("now7DayFrom", params.getDateFrom());
		model.addAttribute("now7DayTo", params.getDateTo());
		model.addAttribute("pre7DayFrom", params.getPreDateFrom());
		model.addAttribute("pre7DayTo", params.getPreDateTo());
		
		return "busi/goodsanaly/abnormal/"+pageName;
	}
	
	private QueryParamsVo getAbnormalParams(boolean now){
		
		//请求参数
		String dateFromStr = "";
		String dateToStr = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		//设置为最近7天
		Calendar cd = Calendar.getInstance();
		if(now == false){
		 cd.add(Calendar.DATE, 0-1);
		}
		dateToStr = df.format(cd.getTime());
		cd.add(Calendar.DATE, 0-6);
		dateFromStr = df.format(cd.getTime());
		QueryParamsVo params = this.getQueryParams(dateFromStr, dateToStr, null, null);
		
		return params;
	}
}