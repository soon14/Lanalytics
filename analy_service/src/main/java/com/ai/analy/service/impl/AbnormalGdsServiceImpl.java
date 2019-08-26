package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.constants.Constants;
import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.AbnormalGdsService;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.AbnormalGdsCountVo;
import com.ai.analy.vo.goods.AbnormalGdsVo;
import com.ai.analy.vo.goods.ComplaintGdsVo;
import com.ai.analy.vo.goods.ReturnGdsVo;
import com.alibaba.dubbo.common.utils.StringUtils;

public class AbnormalGdsServiceImpl extends BaseDao implements AbnormalGdsService {

	/**
	 * 商品退货数据
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-12-15
	 * @param param
	 * @return
	 */
	public ReturnGdsVo returnGds(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		// 店铺退货数据
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(t.amount) returnAmount , sum(abs((unix_timestamp(t.update_time) - unix_timestamp(t.create_time))))/count(*) avgTimeShop from t_ord_back_apply t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.create_time >= ?");
			params.add(df.format(param.getDateFrom()) + " 00:00:00");
			sql.append(" and t.create_time <= ?");
			params.add(df.format(param.getDateTo()) + " 23:59:59");
		}
		ReturnGdsVo returnGdsVo = queryOne(sql.toString(), params, new RowMapper<ReturnGdsVo>() {
			public ReturnGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReturnGdsVo retuGds = new ReturnGdsVo();
				retuGds.setReturnAmount(rs.getLong("returnAmount"));
				retuGds.setAvgDaysShop(rs.getDouble("avgTimeShop")/(double)(1 * 60 * 60 * 24));
				return retuGds;
			}
		} );
		
		
		// 平台退货数据
		params.clear();
		StringBuffer sqlP = new StringBuffer();
		sqlP.append("select sum(t.amount) returnAmountPlat , sum(abs((unix_timestamp(t.update_time) - unix_timestamp(t.create_time))))/count(*) avgTimePlat from t_ord_back_apply t where 1=1");
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sqlP.append(" and t.create_time >= ?");
			params.add(df.format(param.getDateFrom()) + " 00:00:00");
			sqlP.append(" and t.create_time <= ?");
			params.add(df.format(param.getDateTo()) + " 23:59:59");
		}
		
		ReturnGdsVo returnGdsVoP = queryOne(sqlP.toString(), params, new RowMapper<ReturnGdsVo>() {
			public ReturnGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReturnGdsVo retuGds = new ReturnGdsVo();
				retuGds.setReturnAmountPlat(rs.getLong("returnAmountPlat"));
				retuGds.setAvgDaysPlat(rs.getDouble("avgTimePlat")/(double)(1 * 60 * 60 * 24));
				return retuGds;
			}
		} );
		
		returnGdsVo.setReturnAmountPlat(returnGdsVoP.getReturnAmountPlat());
		returnGdsVo.setAvgDaysPlat(returnGdsVoP.getAvgDaysPlat());
		
		
		// 店铺退货率
		long orderAmountShop = getOrderAmountShop(param);
		double rateShop = returnGdsVo.getReturnAmount() / (double) orderAmountShop;
		returnGdsVo.setRateShop(rateShop);
		
		// 平台退货率
		long orderAmountPlat = getOrderAmountPlat(param);
		double ratePlat = returnGdsVo.getReturnAmountPlat() / (double) orderAmountPlat;
		returnGdsVo.setRatePlat(ratePlat);
		
		return returnGdsVo;
	}


	/**
	 * 投诉数据
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-12-15
	 * @param param
	 * @return
	 */
	public ComplaintGdsVo Complaint(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		// 店铺投诉数据
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) complaintAmount , sum(abs((unix_timestamp(if(t.complaint_status='1', now(), t.update_time)) - unix_timestamp(t.complaints_time))))/count(*) avgTimeShop from t_ord_complaint t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.complaints_time >= ?");
			params.add(df.format(param.getDateFrom()) + " 00:00:00");
			sql.append(" and t.complaints_time <= ?");
			params.add(df.format(param.getDateTo()) + " 23:59:59");
		}
		ComplaintGdsVo complaintGdsVo = queryOne(sql.toString(), params, new RowMapper<ComplaintGdsVo>() {
			public ComplaintGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ComplaintGdsVo compGdsVo = new ComplaintGdsVo();
				compGdsVo.setComplaintAmount(rs.getLong("complaintAmount"));
				compGdsVo.setAvgDaysShop(rs.getDouble("avgTimeShop") / (double) (1 * 60 * 60 * 24));
				return compGdsVo;
			}
		});
		
		// 平台投诉数据
		params.clear();
		StringBuffer sqlP = new StringBuffer();
		sqlP.append("select count(*) complaintAmountPlat , sum(abs((unix_timestamp(if(t.complaint_status='1', now(), t.update_time)) - unix_timestamp(t.complaints_time))))/count(*) avgTimePlat from t_ord_complaint t where 1=1");
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sqlP.append(" and t.complaints_time >= ?");
			params.add(df.format(param.getDateFrom()) + " 00:00:00");
			sqlP.append(" and t.complaints_time <= ?");
			params.add(df.format(param.getDateTo()) + " 23:59:59");
		}
		ComplaintGdsVo complaintGdsVoP = queryOne(sqlP.toString(), params, new RowMapper<ComplaintGdsVo>() {
			public ComplaintGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ComplaintGdsVo compGdsVo = new ComplaintGdsVo();
				compGdsVo.setComplaintAmountPlat(rs.getLong("complaintAmountPlat"));
				compGdsVo.setAvgDaysPlat(rs.getDouble("avgTimePlat") / (double) (1 * 60 * 60 * 24));
				return compGdsVo;
			}
		});
		
		complaintGdsVo.setComplaintAmountPlat(complaintGdsVoP.getComplaintAmountPlat());
		complaintGdsVo.setAvgDaysPlat(complaintGdsVoP.getAvgDaysPlat());
		
		// 店铺投诉率
		long orderAmountShop = getOrderAmountShop(param);
		double rateShop = complaintGdsVo.getComplaintAmount() / (double) orderAmountShop;
		complaintGdsVo.setRateShop(rateShop);

		// 平台投诉率
		long orderAmountPlat = getOrderAmountPlat(param);
		double ratePlat = complaintGdsVo.getComplaintAmountPlat() / (double) orderAmountPlat;
		complaintGdsVo.setRatePlat(ratePlat);
		
		return complaintGdsVo;
	}

	
	/**
	 * 平台订单量
	 *
	 * @author huangcm
	 * @date 2015-12-17
	 * @param param
	 * @return
	 */
	private long getOrderAmountPlat(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return 0;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
				
		sql.append("select sum(t.ord_num) orderNum from t_full_ord_info t where 1=1");
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		
		long orderNum = queryOne(sql.toString(), params, new RowMapper<Long> () {
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long orderNum = rs.getLong("orderNum");
				return orderNum;
			}
		});
		
		return orderNum;
	}

	/**
	 * 店铺订单量
	 *
	 * @author huangcm
	 * @date 2015-12-17
	 * @param param
	 * @return
	 */
	private long getOrderAmountShop(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return 0;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
				
		sql.append("select sum(t.ord_num) orderNum from t_full_ord_info t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		
		long orderNum = queryOne(sql.toString(), params, new RowMapper<Long> () {
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long orderNum = rs.getLong("orderNum");
				return orderNum;
			}
		});
		
		return orderNum;
	}

	
	/**
	 * 流量下跌环比50%商品
	 *
	 *
	 * @author huangcm
	 * @date 2015-12-16
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> gdsFlowDown(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return new PageBean<AbnormalGdsVo>();
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.sku_id, sku.cat_id catId, sku.pic_id picId, sku.stock, sku.status state, sku.sku_name gdsName, " +
				   "	sku.sku_prop_desc skuProp, sku.agent_price price, sku.update_time shelvesTime," +
				   "    t.thisweek_pv currPv, t.lastweek_pv lastPv, (t.thisweek_pv - t.lastweek_pv)/t.lastweek_pv pvMom " +
				   "  from goods_detail_weekly t left join t_goods_sku sku on t.sku_id=sku.sku_id where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		
		if (StringUtils.isNotEmpty(param.getDateToStr())) {
			sql.append(" and t.dt = ?");
			params.add(param.getDateToStr());
		}
		sql.append(" and t.thisweek_pv*2 < t.lastweek_pv order by state asc,pvMom asc,lastPv desc ");

		int count = queryCount(sql.toString(), params);
		
		if (param.isQueryCountOnly()) {
			return new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), null, count);
		}
		
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<AbnormalGdsVo> list = queryList(sql.toString(), params, new RowMapper<AbnormalGdsVo> () {
			public AbnormalGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AbnormalGdsVo abnormalGdsVo = new AbnormalGdsVo();
				abnormalGdsVo.setCatId(rs.getString("catId"));
				abnormalGdsVo.setGdsName(rs.getString("gdsName"));
				if (StringUtils.isNotEmpty(abnormalGdsVo.getCatId()) && !abnormalGdsVo.getCatId().equals("900")) {
					abnormalGdsVo.setSkuProp(rs.getString("skuProp"));
				} 
				abnormalGdsVo.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state")));
				abnormalGdsVo.setPrice(rs.getLong("price"));
				abnormalGdsVo.setPicId(rs.getString("picId"));
				abnormalGdsVo.setStock(rs.getLong("stock"));
				abnormalGdsVo.setShelvesTime(rs.getTimestamp("shelvesTime"));
				abnormalGdsVo.setCurrPv(rs.getLong("currPv"));
				abnormalGdsVo.setLastPv(rs.getLong("lastPv"));
				abnormalGdsVo.setPvMom(rs.getDouble("pvMom"));
				return abnormalGdsVo;
			}
		});
		
		PageBean<AbnormalGdsVo> pageBean = new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		return pageBean;
		
	}

	/**
	 * 支付转换率下跌
	 *
	 * @author huangcm
	 * @date 2015-12-17
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> payUvRateDown(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return new PageBean<AbnormalGdsVo>();
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.*, sku.cat_id catId,sku.pic_id picId, sku.stock, sku.status state, " +
					"     sku.sku_name gdsName, sku.sku_prop_desc skuProp, sku.agent_price price, sku.update_time shelvesTime" +
					"  from (  select t1.sku_id, t1.thisweek_uv currUv, t1.lastweek_uv lastUv,  t1.thisweek_pay_uv/t1.thisweek_uv currPayUvRate, " +
					"          t1.lastweek_pay_uv/t1.lastweek_uv lastPayUvRate, t2.pay_uv/t2.uv payUvRatePlat  from goods_detail_weekly t1, goods_spu_7days t2 where 1=1 " +
					"          and t1.spu_id=t2.spu_id");
		
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t1.shop_id = ?");
			params.add(param.getShopId());
		}
		
		if (StringUtils.isNotEmpty(param.getDateToStr())) {
			sql.append(" and t1.dt = ?");
			params.add(param.getDateToStr());
			sql.append(" and t2.dt = ?");
			params.add(param.getDateToStr());
		}
		//sql.append(" and t1.thisweek_pay_uv/t1.thisweek_uv < t2.pay_uv/t2.uv) t left join t_goods_sku sku on t.sku_id=sku.sku_id order by currUv desc,state asc ");
		//去除跟平台支付转化率对比，改成本期跟上期比较
		sql.append(" ) t left join t_goods_sku sku on t.sku_id=sku.sku_id"
				+ " where currPayUvRate<lastPayUvRate"
				+ " order by state asc,currUv desc ");
		int count = queryCount(sql.toString(), params);
		
		if (param.isQueryCountOnly()) {
			return new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), null, count);
		}
		
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<AbnormalGdsVo> list = queryList(sql.toString(), params, new RowMapper<AbnormalGdsVo> () {
			public AbnormalGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AbnormalGdsVo abnormalGdsVo = new AbnormalGdsVo();
				abnormalGdsVo.setCatId(rs.getString("catId"));
				abnormalGdsVo.setGdsName(rs.getString("gdsName"));
				if (StringUtils.isNotEmpty(abnormalGdsVo.getCatId()) && !abnormalGdsVo.getCatId().equals("900")) {
					abnormalGdsVo.setSkuProp(rs.getString("skuProp"));
				} 
				abnormalGdsVo.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state")));
				abnormalGdsVo.setPrice(rs.getLong("price"));
				abnormalGdsVo.setPicId(rs.getString("picId"));
				abnormalGdsVo.setStock(rs.getLong("stock"));
				abnormalGdsVo.setShelvesTime(rs.getTimestamp("shelvesTime"));
				
				abnormalGdsVo.setCurrUv(rs.getLong("currUv"));
				abnormalGdsVo.setLastUv(rs.getLong("lastUv"));
				abnormalGdsVo.setCurrPayUvRate(rs.getDouble("currPayUvRate"));
				abnormalGdsVo.setLastPayUvRate(rs.getDouble("lastPayUvRate"));
				return abnormalGdsVo;
			}
		});
		
		PageBean<AbnormalGdsVo> pageBean = new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		
		return pageBean;
	}


	/**
	 * 零销量
	 *
	 * @author huangcm
	 * @date 2015-12-18
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> sellZero(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return new PageBean<AbnormalGdsVo>();
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  t.sku_id, t.thisweek_pay_quantities currPayAmount , t.lastweek_pay_quantities lastPayAmount, sku.cat_id catId, sku.pic_id picId, sku.stock, sku.status state, sku.sku_name gdsName, " +
				   "	sku.sku_prop_desc skuProp, sku.agent_price price, sku.update_time shelvesTime," +
				   "    t.thisweek_pv currPv, t.lastweek_pv lastPv" +
				   "  from goods_detail_weekly t left join t_goods_sku sku on t.sku_id=sku.sku_id where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		
		if (StringUtils.isNotEmpty(param.getDateToStr())) {
			sql.append(" and t.dt = ?");
			params.add(param.getDateToStr());
		}
		sql.append(" and t.thisweek_pay_quantities=0 order by state asc ");

		int count = queryCount(sql.toString(), params);
		
		if (param.isQueryCountOnly()) {
			return new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), null, count);
		}
		
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<AbnormalGdsVo> list = queryList(sql.toString(), params, new RowMapper<AbnormalGdsVo> () {
			public AbnormalGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AbnormalGdsVo abnormalGdsVo = new AbnormalGdsVo();
				abnormalGdsVo.setCatId(rs.getString("catId"));
				abnormalGdsVo.setGdsName(rs.getString("gdsName"));
				if (StringUtils.isNotEmpty(abnormalGdsVo.getCatId()) && !abnormalGdsVo.getCatId().equals("900")) {
					abnormalGdsVo.setSkuProp(rs.getString("skuProp"));
				} 
				abnormalGdsVo.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state")));
				abnormalGdsVo.setPrice(rs.getLong("price"));
				abnormalGdsVo.setPicId(rs.getString("picId"));
				abnormalGdsVo.setStock(rs.getLong("stock"));
				abnormalGdsVo.setShelvesTime(rs.getTimestamp("shelvesTime"));
				abnormalGdsVo.setCurrPv(rs.getLong("currPv"));
				abnormalGdsVo.setLastPv(rs.getLong("lastPv"));
				
				abnormalGdsVo.setCurrPayAmount(rs.getLong("currPayAmount"));
				abnormalGdsVo.setLastPayAmount(rs.getLong("lastPayAmount"));
				return abnormalGdsVo;
			}
		});
		
		PageBean<AbnormalGdsVo> pageBean = new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		return pageBean;
	}


	/**
	 * 零库存
	 *
	 * @author huangcm
	 * @date 2015-12-18
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> stockZero(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return new PageBean<AbnormalGdsVo>();
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  t.sku_id, sku.empty_time stockZeroTiem, t.thisweek_uv currUv, t.thisweek_pay_quantities currPayAmount , t.lastweek_pay_quantities lastPayAmount, sku.cat_id catId, sku.pic_id picId, sku.stock, sku.status state, sku.sku_name gdsName, " +
				   "	sku.sku_prop_desc skuProp, sku.agent_price price, sku.update_time shelvesTime," +
				   "    t.thisweek_pv currPv, t.lastweek_pv lastPv" +
				   "  from goods_detail_weekly t left join t_goods_sku sku on t.sku_id=sku.sku_id where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		
		if (StringUtils.isNotEmpty(param.getDateToStr())) {
			sql.append(" and t.dt = ?");
			params.add(param.getDateToStr());
		}
		sql.append(" and sku.stock=0 ");
		
		if (StringUtils.isNotEmpty(param.getDateFromStr())) {
			sql.append(" and sku.empty_time >=?");
			params.add(param.getDateFromStr());
		}
		
		if (StringUtils.isNotEmpty(param.getDateToStr())) {
			sql.append(" and sku.empty_time <=?");
			params.add(param.getDateToStr());
		}
		
		sql.append(" order by state asc,sku.empty_time desc");

		int count = queryCount(sql.toString(), params);
		
		if (param.isQueryCountOnly()) {
			return new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), null, count);
		}
		
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<AbnormalGdsVo> list = queryList(sql.toString(), params, new RowMapper<AbnormalGdsVo> () {
			public AbnormalGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AbnormalGdsVo abnormalGdsVo = new AbnormalGdsVo();
				abnormalGdsVo.setCatId(rs.getString("catId"));
				abnormalGdsVo.setGdsName(rs.getString("gdsName"));
				if (StringUtils.isNotEmpty(abnormalGdsVo.getCatId()) && !abnormalGdsVo.getCatId().equals("900")) {
					abnormalGdsVo.setSkuProp(rs.getString("skuProp"));
				} 
				abnormalGdsVo.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state")));
				abnormalGdsVo.setPrice(rs.getLong("price"));
				abnormalGdsVo.setPicId(rs.getString("picId"));
				abnormalGdsVo.setStock(rs.getLong("stock"));
				abnormalGdsVo.setShelvesTime(rs.getTimestamp("shelvesTime"));
				abnormalGdsVo.setCurrPv(rs.getLong("currPv"));
				abnormalGdsVo.setLastPv(rs.getLong("lastPv"));
				
				abnormalGdsVo.setCurrPayAmount(rs.getLong("currPayAmount"));
				abnormalGdsVo.setLastPayAmount(rs.getLong("lastPayAmount"));
				
				abnormalGdsVo.setStockZeroTiem(rs.getTimestamp("stockZeroTiem"));
				abnormalGdsVo.setCurrUv(rs.getLong("currUv"));
				
				return abnormalGdsVo;
			}
		});
		
		PageBean<AbnormalGdsVo> pageBean = new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		return pageBean;
	}


	/**
	 * 库存预警
	 *
	 * @author huangcm
	 * @date 2015-12-18
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> stockWarning(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return new PageBean<AbnormalGdsVo>();
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  t.sku_id, sku.empty_time stockZeroTiem, t.thisweek_uv currUv, t.thisweek_pay_quantities currPayAmount , t.lastweek_pay_quantities lastPayAmount, sku.cat_id catId, sku.pic_id picId, sku.stock, sku.status state, sku.sku_name gdsName, " +
				   "	sku.sku_prop_desc skuProp, sku.agent_price price, sku.update_time shelvesTime," +
				   "    t.thisweek_pv currPv, t.lastweek_pv lastPv" +
				   "  from goods_detail_weekly t left join t_goods_sku sku on t.sku_id=sku.sku_id where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		
		if (StringUtils.isNotEmpty(param.getDateToStr())) {
			sql.append(" and t.dt = ?");
			params.add(param.getDateToStr());
		}
		sql.append(" and t.thisweek_pay_quantities > (t.thisweek_pay_quantities + stock) * 0.8 order by state asc");

		int count = queryCount(sql.toString(), params);
		
		if (param.isQueryCountOnly()) {
			return new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), null, count);
		}
		
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<AbnormalGdsVo> list = queryList(sql.toString(), params, new RowMapper<AbnormalGdsVo> () {
			public AbnormalGdsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AbnormalGdsVo abnormalGdsVo = new AbnormalGdsVo();
				abnormalGdsVo.setCatId(rs.getString("catId"));
				abnormalGdsVo.setGdsName(rs.getString("gdsName"));
				if (StringUtils.isNotEmpty(abnormalGdsVo.getCatId()) && !abnormalGdsVo.getCatId().equals("900")) {
					abnormalGdsVo.setSkuProp(rs.getString("skuProp"));
				} 
				abnormalGdsVo.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state")));
				abnormalGdsVo.setPrice(rs.getLong("price"));
				abnormalGdsVo.setPicId(rs.getString("picId"));
				abnormalGdsVo.setStock(rs.getLong("stock"));
				abnormalGdsVo.setShelvesTime(rs.getTimestamp("shelvesTime"));
				abnormalGdsVo.setCurrPv(rs.getLong("currPv"));
				abnormalGdsVo.setLastPv(rs.getLong("lastPv"));
				
				abnormalGdsVo.setCurrPayAmount(rs.getLong("currPayAmount"));
				abnormalGdsVo.setLastPayAmount(rs.getLong("lastPayAmount"));
				
				abnormalGdsVo.setStockZeroTiem(rs.getTimestamp("stockZeroTiem"));
				abnormalGdsVo.setCurrUv(rs.getLong("currUv"));
				
				return abnormalGdsVo;
			}
		});
		
		PageBean<AbnormalGdsVo> pageBean = new PageBean<AbnormalGdsVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		return pageBean;
	}

	
	public AbnormalGdsCountVo queryCount(QueryParamsVo param) {
		
		AbnormalGdsCountVo count = new AbnormalGdsCountVo();
		param.setQueryCountOnly(true);
		
		count.setGdsFlowDownCoun(this.gdsFlowDown(param).getRecordCount());
		count.setPayUvRateDownCoun(this.payUvRateDown(param).getRecordCount());
		count.setSellZeroCoun(this.sellZero(param).getRecordCount());
		count.setStockWarningCoun(this.stockWarning(param).getRecordCount());
		count.setStockZeroCoun(this.stockZero(param).getRecordCount());
		
		return count;
	}
	
}
