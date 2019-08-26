package com.ai.analy.sv;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.analy.service.interfaces.TradeService;
import com.ai.analy.system.AppBaseController;

/**
 * 店铺交易金额日统计接口
 * Title: MVNO-CRM <br>
 * Description: <br>
 * Date: 2016年5月16日 <br>
 * Copyright (c) 2016 AILK <br>
 * 
 * @author liangyi
 */
@Controller
@RequestMapping(value = "/service")
public class TradeController extends AppBaseController{
	
	private static final Logger log = Logger.getLogger(TradeController.class);
	
	@Autowired
	private TradeService tradeService;
	
	@RequestMapping(value = "/dailyTradeData",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String dailyTradeData(){
		
	    String shopId=this.getParam("shopId");
	    String format=this.getParam("format");
	    String certainDateStr=this.getParam("certainDate");
	    String startDateStr=this.getParam("startDate");
	    String endDateStr=this.getParam("endDate");
	    
		
	    String resJson = null;
	    try {
	        resJson=tradeService.dailyTradeData(shopId, format, certainDateStr, startDateStr, endDateStr);
        } catch (Exception e) {
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
            log.error("获取店铺每日订单金额统计异常", e);
        }
	    
        return resJson;
        
	}
	
	/**
	 * 每日新增会员数接口
	 * @return
	 */
	@RequestMapping(value = "/dailyMemberAnaly",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String dailyMemberAnaly(){
		
	    String certainDateStr=this.getParam("certainDate");
	    String startDateStr=this.getParam("startDate");
	    String endDateStr=this.getParam("endDate");
	    
	    String resJson = null;
	    try {
	        resJson=tradeService.dailyMemberAnaly(certainDateStr, startDateStr, endDateStr);
        } catch (Exception e) {
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
            log.error("获取每日新增会员数接口异常", e);
        }
	    
        return resJson;
        
	}
	
}
