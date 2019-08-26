package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.constants.Constants;
import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.utils.DataConvertUtil;
import com.ai.analy.utils.DateUtils;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.GdsBrandVo;
import com.ai.analy.vo.goods.GdsCatAnalyVo;
import com.ai.analy.vo.goods.GdsCatBrandVo;
import com.ai.analy.vo.goods.GdsCategoryVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.goods.GdsSaleTrendVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;
import com.alibaba.dubbo.common.utils.StringUtils;

public class GoodsAnalyServiceImpl extends BaseDao implements GoodsAnalyService {

	/**
	 * 商品信息总览以及与上期比较
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public GoodsOverviewVo getGoodsOverview(QueryParamsVo param) {
		
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		// 本期
		GoodsOverviewVo currentGdsOverview = queryGoodsOverview(param);
		
		// 上期
		param.setDateFrom(param.getPreDateFrom());
		param.setDateTo(param.getPreDateTo());
		GoodsOverviewVo lastTimeGdsOverview = queryGoodsOverview(param);
		
		if (currentGdsOverview != null && lastTimeGdsOverview != null) {
			double uvCompare = (currentGdsOverview.getUv() - lastTimeGdsOverview.getUv())/(double)lastTimeGdsOverview.getUv();
			currentGdsOverview.setUvCompare(uvCompare);
			double pvCompare = (currentGdsOverview.getPv() - lastTimeGdsOverview.getPv())/(double)lastTimeGdsOverview.getPv();
			currentGdsOverview.setPvCompare(pvCompare);
			double gvCompare = (currentGdsOverview.getGv() - lastTimeGdsOverview.getGv())/(double)lastTimeGdsOverview.getGv();
			currentGdsOverview.setGvCompare(gvCompare);
			double exitRateCompare = (currentGdsOverview.getExitRate() - lastTimeGdsOverview.getExitRate())/(double)lastTimeGdsOverview.getExitRate();
			currentGdsOverview.setExitRateCompare(exitRateCompare);
			double avgStayCompare = (currentGdsOverview.getAvgStay() - lastTimeGdsOverview.getAvgStay())/(double)lastTimeGdsOverview.getAvgStay();
			currentGdsOverview.setAvgStayCompare(avgStayCompare);
			double abnormalsCompare = (currentGdsOverview.getAbnormals() - lastTimeGdsOverview.getAbnormals())/(double)lastTimeGdsOverview.getAbnormals();
			currentGdsOverview.setAbnormalsCompare(abnormalsCompare);
			double cartQuantitiesCompare = (currentGdsOverview.getCartQuantities() - lastTimeGdsOverview.getCartQuantities())/(double)lastTimeGdsOverview.getCartQuantities();
			currentGdsOverview.setCartQuantitiesCompare(cartQuantitiesCompare);
			double orderOver3CountCompare = (currentGdsOverview.getOrderOver3Count() - lastTimeGdsOverview.getOrderOver3Count())/(double)lastTimeGdsOverview.getOrderOver3Count();
			currentGdsOverview.setOrderOver3CountCompare(orderOver3CountCompare);
			double favorsCompare = (currentGdsOverview.getFavors() - lastTimeGdsOverview.getFavors())/(double)lastTimeGdsOverview.getFavors();
			currentGdsOverview.setFavorsCompare(favorsCompare);
			double payQuantitiesCompare = (currentGdsOverview.getPayQuantities() - lastTimeGdsOverview.getPayQuantities())/(double)lastTimeGdsOverview.getPayQuantities();
			currentGdsOverview.setPayQuantitiesCompare(payQuantitiesCompare);
			
			double orderAmountCompare = (currentGdsOverview.getOrderAmount() - lastTimeGdsOverview.getOrderAmount())/(double)lastTimeGdsOverview.getOrderAmount();
			currentGdsOverview.setOrderAmountCompare(orderAmountCompare);
			
			double orderUvCompare = (currentGdsOverview.getOrderUv() - lastTimeGdsOverview.getOrderUv())/(double)lastTimeGdsOverview.getOrderUv();
			currentGdsOverview.setOrderUvCompare(orderUvCompare);
			
			double payAmountCompare = (currentGdsOverview.getPayAmount() - lastTimeGdsOverview.getPayAmount())/(double)lastTimeGdsOverview.getPayAmount();
			currentGdsOverview.setPayAmountCompare(payAmountCompare);
			
			double payUvCompare = (currentGdsOverview.getPayUv() - lastTimeGdsOverview.getPayUv())/(double)lastTimeGdsOverview.getPayUv();
			currentGdsOverview.setPayUvCompare(payUvCompare);
			
			//下单转化率
			currentGdsOverview.setOrderUvRate(currentGdsOverview.getOrderUv()/(double)currentGdsOverview.getUv());
			//下单支付转化率
			currentGdsOverview.setOrderPayUvRate(currentGdsOverview.getPayUv()/(double)currentGdsOverview.getOrderUv());
			//支付转化率
			currentGdsOverview.setPayUvRate(currentGdsOverview.getPayUv()/(double)currentGdsOverview.getUv());
			//客单价
			currentGdsOverview.setPayPrice(currentGdsOverview.getPayAmount()/(double)currentGdsOverview.getPayUv());
			lastTimeGdsOverview.setPayPrice(lastTimeGdsOverview.getPayAmount()/(double)lastTimeGdsOverview.getPayUv());
			//客单价较上期
			double payPriceCompare = (currentGdsOverview.getPayPrice() - lastTimeGdsOverview.getPayPrice())/lastTimeGdsOverview.getPayPrice();
			currentGdsOverview.setPayPriceCompare(payPriceCompare);
		}
		
		return currentGdsOverview;
		
	}
	
	protected class GoodsOverviewRowMapper implements RowMapper<GoodsOverviewVo> {
		public GoodsOverviewVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GoodsOverviewVo gdsOver = new GoodsOverviewVo();
			gdsOver.setUv(rs.getLong("uv"));
			gdsOver.setPv(rs.getLong("pv"));
			gdsOver.setGv(rs.getLong("gv"));
			gdsOver.setAvgStay(rs.getDouble("avgStay"));
			gdsOver.setExitRate(rs.getDouble("exitRate"));
			gdsOver.setCartQuantities(rs.getLong("cartQuantities"));
			gdsOver.setPayQuantities(rs.getLong("payQuantities"));
			gdsOver.setAbnormals(rs.getLong("abnormals"));
			gdsOver.setFavors(rs.getLong("favors"));
			gdsOver.setOrderOver3Count(rs.getLong("orderOver3Count"));
			gdsOver.setOrderAmount(rs.getLong("orderAmount"));
			gdsOver.setOrderUv(rs.getLong("orderUv"));
			gdsOver.setPayAmount(rs.getLong("payAmount"));
			gdsOver.setPayUv(rs.getLong("payUv"));
			return gdsOver;
		}
		
	}

	
	/**
	 * 商品信息总览
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public GoodsOverviewVo queryGoodsOverview(QueryParamsVo param) {
		/**
		select sum(t.uv) uv, sum(t.pv) pv, sum(gv) gv, sum(t.stay_seconds)/sum(t.pv) avgStay, sum(t.exit_pages)/sum(t.pv) exitRate,
		    sum(t.cart_quantities) cartQuantities, sum(t.pay_quantities) payQuantities, sum(t.favors) favors, 
			sum(t.ordercount_over3_num) orderOver3Count, t.abnormals_gds_num abnormals
				from ( select max(tb.minutes) max_minutes , tb.* from goods_overview tb WHERE 1=1 
					and tb.shop_id = ?
					and tb.dt>=? and tb.dt<=?
					and tb.client_type = ?
					group by  tb.dt, tb.client_type ) t
		*/
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(t.uv) uv, sum(t.pv) pv, sum(gv) gv, sum(t.stay_seconds)/sum(t.pv) avgStay, sum(t.exit_pages)/sum(t.pv) exitRate, " +
					"	sum(t.cart_quantities) cartQuantities, sum(t.pay_count) payQuantities, sum(t.favors) favors, " +
					"	sum(t.ordercount_over3_num) orderOver3Count, sum(t.abnormals_gds_num) abnormals, " +
					"   sum(t.order_amount) orderAmount, sum(t.order_uv) orderUv, sum(t.pay_amount) payAmount,sum(t.pay_uv) payUv "+
					"		from ( select max(tb.minutes) max_minutes , tb.* from goods_overview tb WHERE 1=1 ");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and tb.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and tb.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and tb.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and tb.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" group by  tb.dt, tb.client_type ) t");
		
		GoodsOverviewVo GoodsOverviewVo = queryOne(sql.toString(), params, new GoodsOverviewRowMapper());
		
		GoodsOverviewVo.setAbnormals(this.stockAbnormal(param));
		
		return GoodsOverviewVo;
	}
	
	
	/**
	 * 商品销售趋势(日内)
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public List<GdsSaleTrendVo> getGdsSaleTrend4Hourly(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.hour_time times, sum(t.pv) pv, sum(t.uv) uv, sum(gv) gv, sum(t.stay_seconds)/sum(t.pv) stayAvg ," +
				" sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv)  orderUvRate, sum(t.pay_uv)/sum(t.order_uv)  orderPayUvRate," +
				" sum(t.pay_uv)/sum(t.uv) payUvRate, sum(t.order_amount) orderMoney, sum(t.order_quantities) orderCount, " +
				" sum(t.order_uv) orderUv, sum(t.pay_amount) payMoney, sum(t.pay_count) payCount, sum(t.pay_uv) payUv, " +
				" sum(t.pay_amount)/sum(t.pay_uv) payPrice , sum(t.cart_quantities) cartCount from goods_hourly t where 1=1 ");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" group by t.hour_time order by t.hour_time asc");
		
		List<GdsSaleTrendVo> list = queryList(sql.toString(), params, new GdsSaleTrendVoRowMapper());
		
		return list;
	}
	
	private class GdsSaleTrendVoRowMapper implements RowMapper<GdsSaleTrendVo> {
		public GdsSaleTrendVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GdsSaleTrendVo gdsSaleTrendVo = new GdsSaleTrendVo();
			gdsSaleTrendVo.setUv(rs.getLong("uv"));
			gdsSaleTrendVo.setPv(rs.getLong("pv"));
			gdsSaleTrendVo.setGv(rs.getLong("gv"));
			gdsSaleTrendVo.setExitRate(rs.getDouble("exitRate"));
			gdsSaleTrendVo.setOrderUvRate(rs.getDouble("orderUvRate"));
			gdsSaleTrendVo.setOrderPayUvRate(rs.getDouble("orderPayUvRate"));
			gdsSaleTrendVo.setStayAvg(rs.getLong("stayAvg"));
			gdsSaleTrendVo.setPayUvRate(rs.getDouble("payUvRate"));
			gdsSaleTrendVo.setOrderMoney(rs.getLong("orderMoney"));
			gdsSaleTrendVo.setOrderCount(rs.getLong("orderCount"));
			gdsSaleTrendVo.setOrderUv(rs.getLong("orderUv"));
			gdsSaleTrendVo.setPayMoney(rs.getLong("payMoney"));
			gdsSaleTrendVo.setPayCount(rs.getLong("payCount"));
			gdsSaleTrendVo.setPayUv(rs.getLong("payUv"));
			gdsSaleTrendVo.setPayPrice(rs.getLong("payPrice"));
			gdsSaleTrendVo.setCartCount(rs.getLong("cartCount"));
			gdsSaleTrendVo.setXTime(rs.getString("times"));
			
			return gdsSaleTrendVo;
		}
		
	}

	/**
	 * 商品销售趋势(日区间)
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public List<GdsSaleTrendVo> getGdsSaleTrend4Days(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.dt times, sum(t.pv) pv, sum(t.uv) uv, sum(gv) gv, sum(t.stay_seconds)/sum(t.pv) stayAvg ," +
				" sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv)  orderUvRate, sum(t.pay_uv)/sum(t.order_uv)  orderPayUvRate," +
				" sum(t.pay_uv)/sum(t.uv) payUvRate, sum(t.order_amount) orderMoney, sum(t.order_quantities) orderCount, " +
				" sum(t.order_uv) orderUv, sum(t.pay_amount) payMoney, sum(t.pay_count) payCount, sum(t.pay_uv) payUv, " +
				" sum(t.pay_amount)/sum(t.pay_uv) payPrice , sum(t.cart_quantities) cartCount from goods_overview t where 1=1 ");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" group by t.dt order by t.dt asc");
		
		List<GdsSaleTrendVo> list = queryList(sql.toString(), params, new GdsSaleTrendVoRowMapper());
		
		return list;
	}
	
	/**
	 * 商品销售趋势
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public List<GdsSaleTrendVo> getGdsSaleTrend(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<GdsSaleTrendVo> gdsSaleTrendList = new ArrayList<GdsSaleTrendVo>();
		
		if (param.getDateFromStr().equals(param.getDateToStr())) {
			List<GdsSaleTrendVo> gdsTrendList = getGdsSaleTrend4Hourly(param);
			if (gdsTrendList != null && gdsTrendList.size() > 0) {
				int k = 0;
				for (int i = 0; i < 24; i++) {
					GdsSaleTrendVo vo = null;
					if (k < gdsTrendList.size()) {
						vo = gdsTrendList.get(k);
					} else {
						vo = new GdsSaleTrendVo();
					}
					if (String.valueOf(i).equals(vo.getXTime())) {
						gdsSaleTrendList.add(vo);
						k++;
					} else {
						GdsSaleTrendVo gdsSale = new GdsSaleTrendVo();
						gdsSale.setXTime(String.valueOf(i));
						gdsSaleTrendList.add(gdsSale);
					}
				}
			} else {
				for (int i = 0; i < 24; i++) {
					GdsSaleTrendVo gdsSale = new GdsSaleTrendVo();
					gdsSale.setXTime(String.valueOf(i));
					gdsSaleTrendList.add(gdsSale);
				}
			}
		} else {
			List<GdsSaleTrendVo> gdsTrendList = getGdsSaleTrend4Days(param);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (gdsTrendList != null && gdsTrendList.size() > 0) {
				int k = 0;
				for (int i = 0; i < DateUtils.betweenDays(param.getDateFrom(), param.getDateTo()) + 1; i++) {
					GdsSaleTrendVo vo = null;
					if (k < gdsTrendList.size()) {
						vo = gdsTrendList.get(k);
					} else {
						vo = new GdsSaleTrendVo();
					}
					String cuurDate = sdf.format(DateUtils.addDayas(param.getDateFrom(), i));
					if (cuurDate.equals(vo.getXTime())) {
						gdsSaleTrendList.add(vo);
						k++;
					} else {
						GdsSaleTrendVo gdsSale = new GdsSaleTrendVo();
						gdsSale.setXTime(cuurDate);
						gdsSaleTrendList.add(gdsSale);
					}
				}
			} else {
				for (int i = 0; i < DateUtils.betweenDays(param.getDateFrom(), param.getDateTo()) + 1; i++) {
					String cuurDate = sdf.format(DateUtils.addDayas(param.getDateFrom(), i));
					GdsSaleTrendVo gdsSale = new GdsSaleTrendVo();
					gdsSale.setXTime(cuurDate);
					gdsSaleTrendList.add(new GdsSaleTrendVo());
				}
			}
		}
		
		return gdsSaleTrendList;
	}
	
	/**
	 * 商品排行概览
	 * 
	 */
	public PageBean<GdsRankVo> getGdsRankingList(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.goods_id gdsId, t.sku_id skuId, gdspic.vfs_id picId, sku.stock, sku.status state, sku.cat_id catId, sku.sku_name gdsName, sku.sku_prop_desc skuPropDesc, sku.agent_price price, sku.sku_prop skuProp, " +
					" sku.update_time shelvesTime, sum(t.pv) pv, sum(t.uv) uv, sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv) orderUvRate," +
					" sum(t.pay_quantities) payCount, sum(t.pay_amount) payMoney, t.order_count/t.order_uv orderCountAvg," +
					" sum(t.uv)/(select sum(a.uv) from goods_detail a where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and a.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and a.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and a.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and a.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" ) uvPercent from goods_detail t left join t_goods_sku sku on t.sku_id=sku.sku_id");
		sql.append(" left join t_goods_pic gdspic on t.goods_id = gdspic.gds_id where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		if (StringUtils.isNotEmpty(param.getCatId())) {
			sql.append(" and sku.cat_id = ?");
			params.add(param.getCatId());
		}
		if (StringUtils.isNotEmpty(param.getGdsName())) {
			sql.append(" and sku.sku_name like ?");
			params.add("%" + param.getGdsName() + "%");
		}
		
		if (StringUtils.isNotEmpty(param.getSkuIsbn())) {
			sql.append(" and sku.sku_prop like ?");
			params.add("%\"ISBN\",\"%" + param.getSkuIsbn() + "%");
		}
		
		if(param.getCategoryLevel() != 0){
			String catField = null;
			switch(param.getCategoryLevel()){
			case 1:
				catField = "cat_one";
				break;
			case 2:
				catField = "cat_two";
				break;
			case 3:
				catField = "cat_three";
				break;
			}
			
			if(catField != null){
				sql.append(" and t."+ catField +" = ?");
				params.add(param.getCategoryId());
			}
		}
		
		sql.append(" and t.sku_id != ''");
		sql.append(" group by t.sku_id");
		
		if (StringUtils.isNotEmpty(param.getOrderTypeName())) {
			sql.append(" order by " + param.getOrderTypeName());
			if (StringUtils.isNotEmpty(param.getOrderWay())) {
				sql.append(" " + param.getOrderWay());
			}
		}
		
		int count = 0;
		if (param.getPageNo() != null  && param.getPageNo() != 0 && param.getPageSize() != null) {
			count = queryCount(sql.toString(), params);
			
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<GdsRankVo> list = queryList(sql.toString(), params, new GdsRankRowMapper());
		
		PageBean<GdsRankVo> pageBean = new PageBean<GdsRankVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		return pageBean;
	}
	
	class GdsRankRowMapper implements RowMapper<GdsRankVo> {
		public GdsRankVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GdsRankVo gdsRankVo = new GdsRankVo();
			gdsRankVo.setGdsId(rs.getLong("gdsId"));
			gdsRankVo.setSkuId(rs.getLong("skuId"));
			gdsRankVo.setCatId(rs.getString("catId"));
			if (StringUtils.isNotEmpty(gdsRankVo.getCatId()) && !gdsRankVo.getCatId().equals("900")) {
				gdsRankVo.setSkuProp(rs.getString("skuProp"));
			} 
			gdsRankVo.setGdsName(rs.getString("gdsName"));
			gdsRankVo.setPrice(rs.getLong("price"));
			gdsRankVo.setExitRate(rs.getDouble("exitRate"));
			gdsRankVo.setOrderUvRate(rs.getDouble("orderUvRate"));
			gdsRankVo.setPayCount(rs.getLong("payCount"));
			gdsRankVo.setPayMoney(rs.getLong("payMoney"));
			gdsRankVo.setUv(rs.getLong("uv"));
			gdsRankVo.setPv(rs.getLong("pv"));
			//gdsRankVo.setUvPercent(rs.getDouble("uvPercent"));
			gdsRankVo.setOrderCountAvg(rs.getDouble("orderCountAvg"));
			gdsRankVo.setPicId(rs.getString("picId"));
			gdsRankVo.setStock(rs.getLong("stock"));
			gdsRankVo.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state")));
			gdsRankVo.setShelvesTime(rs.getTimestamp("shelvesTime"));
			
			//设置ISBN
			String isbn = "";
			String skuPropStr = rs.getString("skuProp");
			if(!StringUtils.isEmpty(skuPropStr)){
				List<Map<String,String>> skuProps = DataConvertUtil.ConvertProps(skuPropStr);
				for(Map<String,String> skuProp : skuProps){
					if("ISBN".equals(skuProp.get("prop_name"))){
						isbn = skuProp.get("prop_value");
						if(isbn != null && isbn.startsWith("ISBN ")){
							isbn = isbn.substring(5);
						}
						break;
					}
				}
			}
			gdsRankVo.setSkuIsbn(isbn);
			
			return gdsRankVo;
		}
	}


	/**
	 * 商品效果明细
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-11-11
	 * @param param
	 * @return
	 */
	public PageBean<GdsRankVo> getGdsDetails(QueryParamsVo param) {
		return getGdsRankingList(param);
	}

	
	public List<GdsCategoryVo> queryCatsForShop(String shopId) {
		if (StringUtils.isEmpty(shopId)) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select t1.cat_id catId, t2.cat_name catName from t_goods_sku t1 left join  t_goods_spu_category t2 on t1.cat_id=t2.cat_id where 1=1 ");
		
		if (StringUtils.isNotEmpty(shopId)) {
			sql.append("and t1.shop_id = ?");
			params.add(shopId);
		}
		sql.append(" group by t1.cat_id order by t2.dis_order asc");
		
		List<GdsCategoryVo> list = queryList(sql.toString(), params, new RowMapper<GdsCategoryVo>() {
			public GdsCategoryVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				GdsCategoryVo cat = new GdsCategoryVo();
				cat.setCatId(rs.getString("catId"));
				cat.setCatName(rs.getString("catName"));
				return cat;
			}
		});
		
		return list;
	}


	/**
	 * 商品分类分析(类目以及价格区间)
	 *
	 * @param param
	 * @return
	 */
	public List<GdsCatAnalyVo> queryGdsCategory(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		if ("2".equals(param.getCategoryType())) {
			return queryGdsCategory4Brand(param);
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.cat_id catId, tt.cat_name catName, t.cat_type categoryType, t.cat_value categoryTypeName,t.value_rank value_rank, sum(t.gv) gdsCount, " +
				   "  	sum(t.uv) uv, sum(t.pv) pv, sum(t.pay_quantities) salesVolumn, sum(t.pay_amount) tradeMoney, sum(t.pay_uv)/sum(t.uv) payRate " +
				   " from goods_category t left join t_goods_spu_category tt on t.cat_id=tt.cat_id where 1=1 ");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.cat_type = ?");
			params.add(param.getCategoryType());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" group by t.cat_id, t.cat_value order by tt.dis_order,t.value_rank asc ");
		
		List<GdsCatAnalyVo> list = queryList(sql.toString(), params, new GdsCatAnalyRowMapper());
		
		return list;
	}
	
	/**
	 * 商品分类分析(品牌)
	 *
	 * @param param
	 * @return
	 */
	public List<GdsCatAnalyVo> queryGdsCategory4Brand(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<GdsCatAnalyVo> gdsCatAnalyVos = new ArrayList<>();
		List<GdsCatBrandVo> gdsBrandVos = new ArrayList<>();
		//判断分类级别
		if(param.getCategoryLevel() == 1){
			//获取第一级别的商品分类信息
			gdsBrandVos = getFirstGdsBrandByBrandId();
		}else{
			//获取下一级别的商品分类信息
			gdsBrandVos = getLowerGdsBrandByBrandId(param.getCatId(), param.getCategoryId());
		}
		
		//获取各个商品分类的分析数据
		for(GdsCatBrandVo gdsBrandVo : gdsBrandVos){
			GdsCatAnalyVo catAnalyVo = getGdsCatAnalyVoBycatIdAndBrandId(param, 
					gdsBrandVo.getCatId(), gdsBrandVo.getCategoryLevel(), gdsBrandVo.getBrandId());
			catAnalyVo.setBrandId(gdsBrandVo.getBrandId());
			catAnalyVo.setCategoryTypeName(gdsBrandVo.getBrandName());
			catAnalyVo.setCatName(gdsBrandVo.getCatName());
			catAnalyVo.setCatId(gdsBrandVo.getCatId());
			catAnalyVo.setBrands(gdsBrandVo.getBrands());
			gdsCatAnalyVos.add(catAnalyVo);
		}
		
		return gdsCatAnalyVos;
	}
	
	/**
	 * 商品分类分析(品牌)数据导出需要
	 *
	 * @param param
	 * @return
	 */
	public List<GdsCatAnalyVo> queryGdsCategory4BrandByExport(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<GdsCatAnalyVo> gdsCatAnalyVos = new ArrayList<>();
		List<GdsCatBrandVo> gdsBrandVos = new ArrayList<>();
		//获取所有级别的商品分类信息
		gdsBrandVos = getGdsCatBrandAllLevel();
		
		//获取各个商品分类的分析数据
		for(GdsCatBrandVo gdsBrandVo : gdsBrandVos){
			GdsCatAnalyVo catAnalyVo = getGdsCatAnalyVoBycatIdAndBrandId(param, 
					gdsBrandVo.getCatId(), gdsBrandVo.getCategoryLevel(), gdsBrandVo.getBrandId());
			catAnalyVo.setBrandId(gdsBrandVo.getBrandId());
			catAnalyVo.setCategoryTypeName(gdsBrandVo.getBrandName());
			catAnalyVo.setCatName(gdsBrandVo.getCatName());
			catAnalyVo.setCatId(gdsBrandVo.getCatId());
			catAnalyVo.setBrands(gdsBrandVo.getBrands());
			gdsCatAnalyVos.add(catAnalyVo);
		}
		
		return gdsCatAnalyVos;
	}
	
	//根据brandId判断商品类型是什么
	private GdsCatBrandVo queryGdsBrandForCatId(String brandId){
		GdsCatBrandVo gdsCatBrandVo = new GdsCatBrandVo();
		
		GdsBrandVo retGdsBrandVo = new GdsBrandVo();
		do{
			StringBuilder catSql = new StringBuilder();
			catSql.append("select t.brand_id brandId, t.brand_name brandName, t.catg_parent catgParent, t.catg_level catgLevel from t_goods_brand t"+
					" where t.brand_id = ?");
			List<Object> params = new ArrayList<Object>(1);
			params.add(brandId);
			GdsBrandVo gdsBrand = queryOne(catSql.toString(), params, new GdsBrandRowMapper());
			if(gdsBrand == null){
				break;
			}
			retGdsBrandVo = gdsBrand;
			brandId = gdsBrand.getCatgParent();
			
		}while(StringUtils.isNotEmpty(brandId));
		
		if("1115".equals(retGdsBrandVo.getBrandId())){
			gdsCatBrandVo.setCatId("1");
			gdsCatBrandVo.setCatName("实物商品");
		}else if("1199".equals(retGdsBrandVo.getBrandId())){
			gdsCatBrandVo.setCatId("2");
			gdsCatBrandVo.setCatName("虚拟商品");
		}else{
			gdsCatBrandVo.setCatId("-1");
		}
		
		return gdsCatBrandVo;
	}
	
	//获取所有级别的商品分类数据
	private List<GdsCatBrandVo> getGdsCatBrandAllLevel(){
		List<GdsCatBrandVo> retGdsBrands = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select t.brand_id brandId, t.brand_name brandName,"+
					" t.catg_level categoryLevel from t_goods_brand t"+
				" order by t.catg_level");
		List<GdsCatBrandVo> gdsBrands = queryList(sql.toString(), new ArrayList<Object>(), new RowMapper<GdsCatBrandVo>() {
			@Override
			public GdsCatBrandVo mapRow(ResultSet rs, int arg1) throws SQLException {
				GdsCatBrandVo gdsBrandVo = new GdsCatBrandVo();
				gdsBrandVo.setBrandId(rs.getString("brandId"));
				gdsBrandVo.setBrandName(rs.getString("brandName"));
				gdsBrandVo.setCategoryLevel(Integer.valueOf(rs.getString("categoryLevel")));
				return gdsBrandVo;
			}
		});
		
		for(GdsCatBrandVo gdsCatBrandVo : gdsBrands){
			GdsCatBrandVo gdsCat = queryGdsBrandForCatId(gdsCatBrandVo.getBrandId());
			if("-1".equals(gdsCat.getCatId())){
				continue;
			}
			
			gdsCatBrandVo.setCatId(gdsCat.getCatId());
			gdsCatBrandVo.setCatName(gdsCat.getCatName());
			List<GdsBrandVo> brands = new ArrayList<>();
			brands.addAll(getAllParentBrands(gdsCatBrandVo.getBrandId()));
			gdsCatBrandVo.setBrands(brands);
			
			retGdsBrands.add(gdsCatBrandVo);
		}
		
		return retGdsBrands;
	}
	
	//判断是否还有下级分类
	private boolean hasLowerGdsBrand(String brandId){
		StringBuilder catSql = new StringBuilder();
		catSql.append("select * from t_goods_brand t"+
				" where t.catg_parent = ?");
		List<Object> params = new ArrayList<Object>(1);
		params.add(brandId);
		int count = queryCount(catSql.toString(), params);
		
		if(count > 0 ){
			return true;
		}
		return false;
	}
	
	//获取第一级别的商品分类信息
	private List<GdsCatBrandVo> getFirstGdsBrandByBrandId(){
		List<GdsCatBrandVo> gdsBrands = new ArrayList<>();
		
		StringBuilder catSql1 = new StringBuilder();
		catSql1.append(" select t.brand_id brandId, t.brand_name brandName, c.cat_id catId, c.cat_name catName, "+
					" t.catg_level categoryLevel from t_goods_brand t, t_goods_spu_category c"+
					" where c.cat_id='1' and t.brand_id='1115'");
		StringBuilder catSql2 = new StringBuilder();
		catSql2.append(" select t.brand_id brandId, t.brand_name brandName, c.cat_id catId, c.cat_name catName, "+
					" t.catg_level categoryLevel from t_goods_brand t, t_goods_spu_category c"+
					" where c.cat_id='2' and t.brand_id='1199'");
		GdsCatBrandVo gdsBrand1 = queryOne(catSql1.toString(), new ArrayList<Object>(), new GdsCatBrandRowMapper());
		gdsBrands.add(gdsBrand1);
		GdsCatBrandVo gdsBrand2 = queryOne(catSql2.toString(), new ArrayList<Object>(), new GdsCatBrandRowMapper());
		gdsBrands.add(gdsBrand2);
		for(GdsCatBrandVo gdsCatBrandVo : gdsBrands){
			List<GdsBrandVo> brands = new ArrayList<>();
			GdsBrandVo gdsBrandVo = new GdsBrandVo();
			gdsBrandVo.setBrandId(gdsCatBrandVo.getBrandId());
			gdsBrandVo.setBrandName(gdsCatBrandVo.getBrandName());
			//判断是否还有下级分类
			gdsBrandVo.setHasLowerBrand(hasLowerGdsBrand(gdsCatBrandVo.getBrandId()));
			brands.add(gdsBrandVo);
			gdsCatBrandVo.setBrands(brands);
		}
		
		return gdsBrands;
	}
	
	//获取下一级别的商品类别与分类信息
	private List<GdsCatBrandVo> getLowerGdsBrandByBrandId(String catId, String brandId){
		StringBuilder catSql = new StringBuilder();
		catSql.append(" select t.brand_id brandId, t.brand_name brandName, c.cat_id catId, c.cat_name catName, "+
					" t.catg_level categoryLevel from t_goods_brand t, t_goods_spu_category c"+
					" where c.cat_id=? and t.catg_parent=? order by c.dis_order");
		List<Object> params = new ArrayList<Object>(2);
		params.add(catId);
		params.add(brandId);
		
		List<GdsBrandVo> parentGdsBrands = getAllParentBrands(brandId);
		
		List<GdsCatBrandVo> gdsBrands = queryList(catSql.toString(), params, new GdsCatBrandRowMapper());
		
		for(GdsCatBrandVo gdsCatBrandVo : gdsBrands){
			List<GdsBrandVo> brands = new ArrayList<>();
			brands.addAll(parentGdsBrands);
			GdsBrandVo gdsBrandVo = new GdsBrandVo();
			gdsBrandVo.setBrandId(gdsCatBrandVo.getBrandId());
			gdsBrandVo.setBrandName(gdsCatBrandVo.getBrandName());
			//判断是否还有下级分类
			gdsBrandVo.setHasLowerBrand(hasLowerGdsBrand(gdsCatBrandVo.getBrandId()));
			brands.add(gdsBrandVo);
			gdsCatBrandVo.setBrands(brands);
		}
		return gdsBrands;
	}
	
	//获取所有父级分类对象(包括其本身)
	private List<GdsBrandVo> getAllParentBrands(String brandId){
		List<GdsBrandVo> retGdsBrandVos = new ArrayList<>();
		List<GdsBrandVo> gdsBrandVos = new ArrayList<>();
		do{
			StringBuilder catSql = new StringBuilder();
			catSql.append("select t.brand_id brandId, t.brand_name brandName, t.catg_parent catgParent, t.catg_level catgLevel from t_goods_brand t"+
					" where t.brand_id = ?");
			List<Object> params = new ArrayList<Object>(1);
			params.add(brandId);
			GdsBrandVo gdsBrand = queryOne(catSql.toString(), params, new GdsBrandRowMapper());
			if(gdsBrand == null){
				break;
			}
			gdsBrand.setHasLowerBrand(true);
			gdsBrandVos.add(gdsBrand);
			brandId = gdsBrand.getCatgParent();
			
		}while(StringUtils.isNotEmpty(brandId));
		
		for(int i=gdsBrandVos.size()-1;i>=0;i--){
			retGdsBrandVos.add(gdsBrandVos.get(i));
		}
		
		return retGdsBrandVos;
	}
	
	//所取商品分类最后一级的级别
	private int getLowestCatgLevel(){
		StringBuffer sql = new StringBuffer();
		sql.append("select catg_level catgLevel from t_goods_brand order by catg_level desc limit 1");
		return queryOne(sql.toString(), new ArrayList<>(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("catgLevel");
			}
		});
	}
	
	//根据catId和brandId查询出商品分类统计信息
	private GdsCatAnalyVo getGdsCatAnalyVoBycatIdAndBrandId(QueryParamsVo param, String catId, int categoryLevel, String brandId){
		//获取brandId所有下级及其本身的所有brandId
		int lowestCatgLevel = getLowestCatgLevel();
		List<String> allLowerBrandIds = getAllLowerBrandIds(lowestCatgLevel,categoryLevel, brandId);
		
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(t.gv) gdsCount, sum(t.uv) uv, sum(t.pv) pv,"+
				" sum(t.pay_quantities) salesVolumn, sum(t.pay_amount) tradeMoney,"+
				" sum(t.pay_uv)/sum(t.uv) payRate "+
				" from goods_category t where cat_type='2' ");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		
		if (StringUtils.isNotEmpty(catId)) {
			sql.append(" and t.cat_id = ?");
			params.add(catId);
		}
		
		sql.append(" and t.cat_value in (");
		
		for(int i=0;i<allLowerBrandIds.size();i++){
			if(i!=0){
				sql.append(",");
			}
			sql.append("'"+allLowerBrandIds.get(i)+"'");
		}
		sql.append(")");
		
		GdsCatAnalyVo gdsCatAnalyVo = queryOne(sql.toString(), params, new GdsCatBrandAnalyRowMapper());
		return gdsCatAnalyVo;
	}
	
	//根据catgLevel和brandId获取brandId下级及其本身的所有brandId
	private List<String> getAllLowerBrandIds(int lowestLevel, int categoryLevel, String brandId){
		StringBuilder sql = new StringBuilder();
		sql.append("select DISTINCT brand_id brandId from ");
		sql.append("(select t1.brand_id,");
		
		
		for(int i=1;i<=lowestLevel;i++){
			sql.append(" case ");
			for(int j=1;j<=lowestLevel;j++){
				sql.append(" when t"+j+".catg_level ="+i+" then t"+j+".brand_id ");
			}
			sql.append("end as BRAND_ID_LEVEL"+i);
			if(i!=lowestLevel){
				sql.append(",");
			}
		}
		sql.append(" from t_goods_brand t1 ");
		for(int i=2;i<=lowestLevel;i++){
			sql.append(" left join t_goods_brand t"+i+" on t"+(i-1)+".catg_parent = t"+i+".brand_id "); 
		}
		sql.append(") b where b.BRAND_ID_LEVEL"+categoryLevel+"=?");
		List<Object> params = new ArrayList<Object>(1);
		params.add(brandId);
		
		List<String> brandIds = queryList(sql.toString(), params, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("brandId");
			}
		});
	
		return brandIds;
	}
	
	protected class GdsBrandRowMapper implements RowMapper<GdsBrandVo> {
		public GdsBrandVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GdsBrandVo gdsBrandVo = new GdsBrandVo();
			gdsBrandVo.setBrandId(rs.getString("brandId"));
			gdsBrandVo.setBrandName(rs.getString("brandName"));
			gdsBrandVo.setCatgParent(rs.getString("catgParent"));
			gdsBrandVo.setCatgLevel(Integer.valueOf(rs.getString("catgLevel")));
			return gdsBrandVo;
		}
	}
	
	protected class GdsCatBrandRowMapper implements RowMapper<GdsCatBrandVo> {
		public GdsCatBrandVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GdsCatBrandVo gdsBrandVo = new GdsCatBrandVo();
			gdsBrandVo.setCatId(rs.getString("catId"));
			gdsBrandVo.setCatName(rs.getString("catName"));
			gdsBrandVo.setBrandId(rs.getString("brandId"));
			gdsBrandVo.setBrandName(rs.getString("brandName"));
			gdsBrandVo.setCategoryLevel(Integer.valueOf(rs.getString("categoryLevel")));
			return gdsBrandVo;
		}
	}
	
	protected class GdsCatAnalyRowMapper implements RowMapper<GdsCatAnalyVo> {
		public GdsCatAnalyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GdsCatAnalyVo gdsCatAnalyVo = new GdsCatAnalyVo();
			gdsCatAnalyVo.setCategoryType(rs.getString("categoryType"));
			gdsCatAnalyVo.setCategoryTypeName(rs.getString("categoryTypeName"));
			gdsCatAnalyVo.setCatId(rs.getString("catId"));
			gdsCatAnalyVo.setCatName(rs.getString("catName"));
			gdsCatAnalyVo.setGdsCount(rs.getLong("gdsCount"));
			gdsCatAnalyVo.setUv(rs.getLong("uv"));
			gdsCatAnalyVo.setPv(rs.getLong("pv"));
			gdsCatAnalyVo.setSalesVolumn(rs.getLong("salesVolumn"));
			gdsCatAnalyVo.setTradeMoney(rs.getDouble("tradeMoney"));
			gdsCatAnalyVo.setPayRate(rs.getDouble("payRate"));
			return gdsCatAnalyVo;
		}
	}
	
	protected class GdsCatBrandAnalyRowMapper implements RowMapper<GdsCatAnalyVo> {
		public GdsCatAnalyVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			GdsCatAnalyVo gdsCatAnalyVo = new GdsCatAnalyVo();
			gdsCatAnalyVo.setGdsCount(rs.getLong("gdsCount"));
			gdsCatAnalyVo.setUv(rs.getLong("uv"));
			gdsCatAnalyVo.setPv(rs.getLong("pv"));
			gdsCatAnalyVo.setSalesVolumn(rs.getLong("salesVolumn"));
			gdsCatAnalyVo.setTradeMoney(rs.getDouble("tradeMoney"));
			gdsCatAnalyVo.setPayRate(rs.getDouble("payRate"));
			return gdsCatAnalyVo;
		}
	}
	
	/**
	 * 异常商品(库存为0)
	 *
	 * @author huangcm
	 * @date 2015-12-25
	 * @param param
	 * @return
	 */
	public long stockAbnormal(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return 0;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select count(distinct t.gds_id) from t_goods_sku t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and  t.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			sql.append(" and t.empty_time >= ?");
			params.add(param.getDateFromStr());
			sql.append(" and t.empty_time <= ?");
			params.add(param.getDateToStr());
		}
		sql.append(" and stock = 0");

		
		long count = getLong(sql.toString(), params);
		
		return count;
	}


	/**
	 * 查询商品子分类
	 * 
	 * @param parentCatId
	 *            父分类ID
	 * @return
	 */
	@Override
	public List<GdsCategoryVo> querySubGdsCategory(String parentCatId) {
		String sql = "select brand_id as catId, brand_name as catName from t_goods_brand where catg_parent=? and status = 1 order by sort_no asc";
		List<Object> params = new ArrayList<Object>();
		params.add(parentCatId);
		
		List<GdsCategoryVo> list = queryList(sql.toString(), params, new BeanPropertyRowMapper<GdsCategoryVo>(GdsCategoryVo.class));
		
		return list;
	}


	@Override
	public GdsCategoryVo getGdsCategoryById(String catId) {
		String sql = "select brand_id as catId, brand_name as catName from t_goods_brand where brand_id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(catId);
		
		GdsCategoryVo categoryVo = queryOne(sql.toString(), params, new BeanPropertyRowMapper<GdsCategoryVo>(GdsCategoryVo.class));
		
		return categoryVo;
	}
}
