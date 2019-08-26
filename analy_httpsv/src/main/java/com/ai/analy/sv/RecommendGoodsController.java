package com.ai.analy.sv;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.analy.service.interfaces.IRecommendGoodsService;
import com.ai.analy.system.AppBaseController;

/**
 * 猜你喜欢功能，商品推荐服务
 * Title: MVNO-CRM <br>
 * Description: <br>
 * Date: 2016-5-10 <br>
 * Copyright (c) 2016 AILK <br>
 * 
 * @author liangyi
 */
@Controller
@RequestMapping(value = "/service")
public class RecommendGoodsController extends AppBaseController{
	
	private static final Logger log = Logger.getLogger(RecommendGoodsController.class);
	    
	@Autowired
	private IRecommendGoodsService iRecommendGoodsService;
	
	/**
	 * 猜你喜欢接口
	 * @return
	 * @author liangyi
	 */
	@RequestMapping(value = "/recommendGoods",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String recommendGoods(){
		
	    String userId=this.getParam("userId");
	    String format=this.getParam("format");
	    String gdsNumStr=this.getParam("gdsNum");
	    String pagingStr=this.getParam("paging");
	    String pageNoStr=this.getParam("pageNo");
	    String pageSizeStr=this.getParam("pageSize");
	    
	    log.debug("猜你喜欢接口，收到参数userId="+userId+";format="+format+";gdsNum="+gdsNumStr);
		
	    String resJson = null;
	    
	    try {
	    	boolean paging=Boolean.valueOf(pagingStr);
	    	
	    	if (paging) {
				//分页查询
	    		resJson=iRecommendGoodsService.recommendGoodsPaging(userId, format, gdsNumStr, false, pageNoStr, pageSizeStr);
			}else{
				resJson=iRecommendGoodsService.recommendGoods(userId, format, gdsNumStr,true);
			}
	        
        } catch (Exception e) {
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
            log.error("调用猜你喜欢接口异常", e);
        }
	    
        return resJson;
        
	}
	
	
	/**
	 * 商品推荐接口
	 * @return
	 * @author liangyi
	 */
	@RequestMapping(value = "/recommendRelatedGds",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String recommendRelatedGds(){
		
		String skuId=this.getParam("skuId");
	    String userId=this.getParam("userId");
	    String skuNumStr=this.getParam("skuNum");
	    String pagingStr=this.getParam("paging");
	    String pageNoStr=this.getParam("pageNo");
	    String pageSizeStr=this.getParam("pageSize");
	    
	    String resJson = null;
	    
	    try {
	    	boolean paging=Boolean.valueOf(pagingStr);
	    	
	    	if (paging) {
				//分页查询
	    		resJson=iRecommendGoodsService.recommendRelatedGdsPaging(skuId, userId, skuNumStr, pageNoStr, pageSizeStr);
			}else{
				resJson=iRecommendGoodsService.recommendRelatedGds(skuId, userId, skuNumStr);
			}
	        
        } catch (Exception e) {
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
            log.error("调用商品推荐接口异常", e);
        }
	    
        return resJson;
        
	}
	
}
