package com.ai.analy.sv;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.analy.service.interfaces.IGoodsRankService;
import com.ai.analy.system.AppBaseController;

@Controller
@RequestMapping(value = "/service")
public class GoodsRankHttpSv extends AppBaseController{
	
	@Autowired
	private IGoodsRankService goodsRankService;
	
	/**
	 *
	 * @return
	 */
	@RequestMapping(value = "/getGoodsRank",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getGoodsRank(){
		
		Map<String,String> requestParams = this.getParamMap();
		String resJson = goodsRankService.getGoodsRank(requestParams);
		
		return resJson;
	}
}
