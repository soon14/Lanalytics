package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.IGoodsRankService;
import com.ai.analy.utils.DataConvertUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoodsRankServiceImpl extends BaseDao implements IGoodsRankService{
	
	/**  请求参参：请查看接口定义
	 *   返回参数：
		 成功:
				{
	  "goodsList": [
	    {
	      "gdsName": "抗菌药物临床应用指导原则（2015年版）",
	      "category": "1",
	      "gdsStatus": "11",
	      "gdsPicId": "565334030cf214b58f0fd59a",
	      "skuId": "74634",
	      "gdsId": "73635",
	      "pv": 647,
	      "tradeAmount": 2115,
          "guidePrice":9000,
          "discountPrice":8000,
          "skuProps":[
			{
			  "prop_id":"1001",
			  "prop_name":"作者",
			  "prop_value":"樊立华"
            }
		   ]
	    }
	  ],
	  "serviceMsg": "成功",
	  "itemCount": 13828,
	  "serviceState": "0000"
	  	}
												
		失败：
			{
			"serviceMsg": "错误提示",
			"serviceState": "1001"
			}
	 */
	@Override
	public String getGoodsRank(Map<String, String> requestParams) {
		// TODO Auto-generated method stub
		Map<String,Object> responseMsg = new HashMap<String,Object>();
		
		try {
			
			Map<String,String> orderClos = new HashMap<String,String>();
			orderClos.put("11", "gds.tradeAmount DESC ");//11-销量排行
			orderClos.put("12", "gds.pv DESC ");//12-浏览量排行
			orderClos.put("13", "gds.tradeAmount DESC");//13-新书销量排行
			
			//查询参数
			String shopId = requestParams.get("shopId");
			if(StringUtils.isBlank(shopId)){
				shopId = "100";
			}
			String category = requestParams.get("category");
			
			//商品状态
			String status = requestParams.get("status");
			if("1".equals(status)){
				status = "";
			}else{
				status = "11";
			}
			
			String rankType = requestParams.get("rankType");
			if(StringUtils.isBlank(rankType)){
				rankType = "11";
			}
			
			int pageNumber = 1;
			int pageSize = 20;
			
			if(requestParams.containsKey("pageNumber")){
				try{
					if(StringUtils.isBlank(requestParams.get("pageNumber")) ==false)
				        pageNumber = Integer.valueOf(requestParams.get("pageNumber"));
				}catch(Exception e){
					//参数校验错误
					responseMsg.put("serviceState", "1001");
					responseMsg.put("serviceMsg", "pageNumber不合法");
					return new ObjectMapper().writeValueAsString(responseMsg);
				}
			}
			if(requestParams.containsKey("pageSize")){
				try{
					if(StringUtils.isBlank(requestParams.get("pageSize")) ==false)
				        pageSize = Integer.valueOf(requestParams.get("pageSize"));
	            }catch(Exception e){
	            	//参数校验错误
	            	responseMsg.put("serviceState", "1001");
	            	responseMsg.put("serviceMsg", "pageSize不合法");
	            	return new ObjectMapper().writeValueAsString(responseMsg);
				}
			}
			
			if(orderClos.containsKey(rankType) == false){
				responseMsg.put("serviceState", "1001");
				responseMsg.put("serviceMsg", "暂不支持排行榜类型"+rankType);
	        	return new ObjectMapper().writeValueAsString(responseMsg);
			}
			
			if(pageSize>500){
				pageSize = 500;
			}
			
			//查询sql
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT gds.*, "
					+ "sku.gds_id gdsId, "
					+ "sku.sku_name gdsName, "
					+ "sku.STATUS gdsStatus, "
					+ "sku.main_catgs category,"
					+ "sku.pic_id gdspicId,"
					+ "sku.agent_price guidePrice,"
					+ "sku.sku_prop skuProp "
					+ "FROM ( "
					+ "  SELECT "
					+ "  t.shop_id shopId,"
					+ "  t.sku_id skuId, "
					+ "  sum(t.pay_quantities) tradeAmount, "
					+ "  sum(t.pv) pv "
					+ "  FROM goods_detail t "
					+ "  WHERE t.shop_id = ? "
					+ "  AND t.sku_id <> '' "
				  //+ "  AND t.dt > DATE_SUB(CURDATE(), INTERVAL 1 YEAR) "
					+ "  GROUP BY t.shop_id,t.sku_id "
					+ " ) gds "
					+ " LEFT JOIN t_goods_sku sku "
					+ " ON gds.skuId = sku.sku_id");
			
			//查询参数
			List<Object> params = new ArrayList<Object>();
			params.add(shopId);
			
			sql.append(" WHERE 1=1");
			//商品状态
			if(StringUtils.isBlank(status) == false){
				sql.append(" AND sku.STATUS = ?");
				params.add(status);//已上架
			}
			//商品分类
			if(StringUtils.isBlank(category) == false){
				sql.append(" AND sku.plat_catgs LIKE ?");
				params.add("%<"+category+">%");
			}
			
			//新书排行
			if("13".equals(rankType)){
				//出版日期一年内
				sql.append(" AND sku.pub_date > DATE_SUB(CURDATE(), INTERVAL 1 YEAR) ");
			}
			
			sql.append(" ORDER BY "+orderClos.get(rankType));
			
			//获取总记录数
			int itemCount = queryCount(sql.toString(), params);
			
			//分页查询
			sql.append(" LIMIT ?, ?");
			params.add((pageNumber - 1) * pageSize);
			params.add(pageSize);
			
			List<Map<String, Object>> dataList = this.queryList(sql.toString(), params, new RowMapper<Map<String, Object>>() {
	
				@Override
				public Map<String, Object> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					// TODO Auto-generated method stub
					Map<String, Object> gds = new HashMap<String, Object>();
					
					String gdsId = rs.getString("gdsId");// 商品ID
					String gdsName = rs.getString("gdsName");// 商品名称     
					String gdsStatus = rs.getString("gdsStatus"); // 商品状态     
					String skuId = rs.getString("skuId");// 单品ID      
					String category = rs.getString("category");  //商品分类    
					long tradeAmount = rs.getLong("tradeAmount");//交易量
					long pv = rs.getLong("pv");//流量量
					String gdsPicId = rs.getString("gdspicId");
					long guidePrice = rs.getLong("guidePrice");  
					String skuProp = rs.getString("skuProp");
					
					gds.put("gdsId", gdsId);
					gds.put("gdsName", gdsName);
					gds.put("gdsStatus", gdsStatus);
					gds.put("skuId", skuId);
					gds.put("category", category);
					gds.put("gdsPicId", gdsPicId);
					gds.put("tradeAmount", tradeAmount);
					gds.put("pv", pv);
					gds.put("guidePrice", guidePrice);
					gds.put("skuProps", DataConvertUtil.ConvertProps(skuProp));
					
					return gds;
				}
				
			});
			if(dataList != null){
			}else{
				dataList = new ArrayList<Map<String, Object>>();
			}
	
			responseMsg.put("serviceState", "0000");
			responseMsg.put("serviceMsg", "成功");
			responseMsg.put("itemCount", itemCount);
			responseMsg.put("goodsList", dataList);
			
			return new ObjectMapper().writeValueAsString(responseMsg);
			
		} catch (Exception e) {
			// TODO: handle exception
        	return "{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
		}
	}
}
