package com.ai.analy.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.PromService;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.prom.PromIndexDateSumVo;
import com.ai.analy.vo.prom.PromIndexOverviewVo;
import com.ai.analy.vo.prom.PromIndexTrendVo;
import com.ai.analy.vo.prom.PromInfoVo;
import com.ai.analy.vo.prom.PromTypeVo;
import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 促销分析服务接口实现
 * 
 * @author limb
 * @date 2016年9月5日
 */
public class PromServiceImpl extends BaseDao implements PromService {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 查询促销类型
	 */
	@Override
	public List<PromTypeVo> getValidPromType() {
		String sql = "select prom_type_code as promTypeCode, prom_type_name as promTypeName from t_prom_type where status =1";

		return queryList(sql, null, new BeanPropertyRowMapper<PromTypeVo>(PromTypeVo.class));
	}

	/**
	 * 查询促销列表
	 */
	@Override
	public PageBean<PromInfoVo> getPromList(QueryParamsVo paramsVo) {
		if (paramsVo == null) {
			return null;
		}

		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(
				"select a.id, b.site_name as siteName, c.prom_type_name as promTypeName, a.prom_class as promClass, a.prom_theme as promTheme, a.prom_content as promContent, a.priority, a.status, a.start_time as startTime, a.end_time as endTime, a.show_start_time as showStartTime, a.show_end_time as showEndTime, d.shop_name as shopName from t_prom_info as a");
		sql.append(" join t_cms_site as b on b.id = a.site_id");
		sql.append(" join t_prom_type as c on c.prom_type_code = a.prom_type_code");
		sql.append(" join t_shop_info as d on d.id = a.shop_id");
		sql.append(" where a.status in(10,20,30)");
		if (StringUtils.isNotEmpty(paramsVo.getPromName())) {
			sql.append(" and a.prom_theme like ?");
			params.add("%" + paramsVo.getPromName() + "%");
		}
		if (StringUtils.isNotEmpty(paramsVo.getPromTypeCode())) {
			sql.append(" and a.prom_type_code = ?");
			params.add(paramsVo.getPromTypeCode());
		}
		if (StringUtils.isNotEmpty(paramsVo.getStatus())) {
			sql.append(" and a.status = ?");
			params.add(paramsVo.getStatus());
		}
		if (StringUtils.isNotEmpty(paramsVo.getSiteId())) {
			sql.append(" and a.site_id = ?");
			params.add(paramsVo.getSiteId());
		}
		if (StringUtils.isNotEmpty(paramsVo.getParamShopId())) {
			sql.append(" and a.shop_id = ?");
			params.add(paramsVo.getParamShopId());
		}
		if (paramsVo.getDateFrom() != null && paramsVo.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and not (a.end_time < ?");
			params.add(df.format(paramsVo.getDateFrom()));
			sql.append(" or a.start_time > ?)");
			params.add(df.format(paramsVo.getDateTo()));
		}

		int count = 0;
		if (paramsVo.getPageNo() != null && paramsVo.getPageNo() != 0 && paramsVo.getPageSize() != null) {
			count = queryCount(sql.toString(), params);

			sql.append(" order by a.id desc");
			sql.append(" limit ?, ?");
			params.add((paramsVo.getPageNo() - 1) * paramsVo.getPageSize());
			params.add(paramsVo.getPageSize());
		}

		List<PromInfoVo> list = queryList(sql.toString(), params,
				new BeanPropertyRowMapper<PromInfoVo>(PromInfoVo.class));
		PageBean<PromInfoVo> pageBean = new PageBean<PromInfoVo>(paramsVo.getPageNo(), paramsVo.getPageSize(), list,
				count);

		return pageBean;
	}

	/**
	 * 查询促销指标概览
	 */
	@Override
	public PromIndexOverviewVo getPromIndexOverview(QueryParamsVo paramsVo) {
		PromInfoVo promInfo = getPromInfoVo(paramsVo.getPromId());
		List<Integer> skuIds = getPromSkuIdList(promInfo);

		Map<String,Object> params1 = new HashMap<String,Object>();
		Map<String,Object> params2 = new HashMap<String,Object>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(t.pv) pv, sum(t.uv) uv, sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv) orderUvRate,"
		+ " sum(t.pay_quantities) payCount, sum(t.pay_amount) payMoney, sum(t.order_count)/sum(t.order_uv) orderCountAvg from goods_detail t where 1=1");
		
		sql.append(" and t.shop_id = (:shopId)");
		params1.put("shopId", promInfo.getShopId());
		params2.put("shopId", promInfo.getShopId());
		
		if(!skuIds.isEmpty()){//促销关联单品id
			sql.append(" and t.sku_id in (:skuIds)");
			params1.put("skuIds", skuIds);
			params2.put("skuIds", skuIds);
		}
		if (paramsVo.getSource() != 0) {
			sql.append(" and t.client_type = (:clientType)");
			params1.put("clientType", paramsVo.getSource());
			params2.put("clientType", paramsVo.getSource());
		}
		if (paramsVo.getDateFrom() != null && paramsVo.getDateTo() != null) {
			sql.append(" and t.dt >= (:dateFrom)");
			params1.put("dateFrom", paramsVo.getDateFrom());
			params2.put("dateFrom", paramsVo.getPreDateFrom());
			
			sql.append(" and t.dt <= (:dateTo)");
			params1.put("dateTo", paramsVo.getDateTo());
			params2.put("dateTo", paramsVo.getPreDateTo());
		}
		sql.append(" and t.sku_id != ''");
		
		PromIndexOverviewVo indexOverviewVo = new PromIndexOverviewVo();
		SqlRowSet value1 = namedParameterJdbcTemplate.queryForRowSet(sql.toString(), params1);
		if (value1.next()){
			indexOverviewVo.setPv(value1.getInt("pv"));
			indexOverviewVo.setUv(value1.getInt("uv"));
			indexOverviewVo.setExitRate(value1.getDouble("exitRate"));
			indexOverviewVo.setOrderUvRate(value1.getDouble("orderUvRate"));
			indexOverviewVo.setPayCount(value1.getLong("payCount"));
			indexOverviewVo.setPayMoney(value1.getDouble("payMoney"));
			indexOverviewVo.setOrderCountAvg(value1.getDouble("orderCountAvg"));
		
			SqlRowSet value2 = namedParameterJdbcTemplate.queryForRowSet(sql.toString(), params2);
			if (value2.next()){
				float pv = value2.getFloat("pv");
				indexOverviewVo.setPvCompare( (indexOverviewVo.getPv() - pv ) / pv);
				float uv = value2.getFloat("uv");
				indexOverviewVo.setUvCompare((indexOverviewVo.getUv() - uv ) / uv);
				float exitRate = value2.getFloat("exitRate");
				indexOverviewVo.setExitRateCompare( (indexOverviewVo.getExitRate() - exitRate ) / exitRate);
				float orderUvRate = value2.getFloat("orderUvRate");
				indexOverviewVo.setOrderUvRateCompare( (indexOverviewVo.getOrderUvRate() - orderUvRate ) / orderUvRate);
				float payCount = value2.getFloat("payCount");
				indexOverviewVo.setPayCountCompare( (indexOverviewVo.getPayCount() - payCount ) / payCount);
				float payMoney = value2.getFloat("payMoney");
				indexOverviewVo.setPayMoneyCompare( (indexOverviewVo.getPayMoney() - payMoney ) / payMoney);
				float orderCountAvg = value2.getFloat("orderCountAvg");
				indexOverviewVo.setOrderCountAvgCompare( (indexOverviewVo.getOrderCountAvg() - orderCountAvg ) / orderCountAvg);
			}
		}
		
		return indexOverviewVo;
	}
	
	/**
	 * 查询促销指标趋势
	 * @param paramsVo
	 * @return
	 */
	@Override
	public PromIndexTrendVo getPromIndexTrend(QueryParamsVo paramsVo) {
		PromInfoVo promInfo = getPromInfoVo(paramsVo.getPromId());
		List<Integer> skuIds = null;
		//查询促销单品指标趋势
		if(!StringUtils.isEmpty(paramsVo.getSkuId())){
			skuIds = new ArrayList<Integer>();
			skuIds.add(Integer.valueOf(paramsVo.getSkuId()));
			
		}else{//查询促销指标趋势
			skuIds = getPromSkuIdList(promInfo);
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select sum(t.pv) pv, sum(t.uv) uv, sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv) orderUvRate,"
		+ " sum(t.pay_quantities) payCount, sum(t.pay_amount) payMoney, sum(t.order_count)/sum(t.order_uv) orderCountAvg, t.%s from %s t where 1=1");
		
		sqlBuilder.append(" and t.shop_id = (:shopId)");
		params.put("shopId", promInfo.getShopId());
		if(!skuIds.isEmpty()){//促销关联单品id
			sqlBuilder.append(" and t.sku_id in (:skuIds)");
			params.put("skuIds", skuIds);
		}
		if (paramsVo.getSource() != 0) {
			sqlBuilder.append(" and t.client_type = (:clientType)");
			params.put("clientType", paramsVo.getSource());
		}
		if (paramsVo.getDateFrom() != null && paramsVo.getDateTo() != null) {
			sqlBuilder.append(" and t.dt >= (:dateFrom)");
			params.put("dateFrom", paramsVo.getDateFrom());
			
			sqlBuilder.append(" and t.dt <= (:dateTo)");
			params.put("dateTo", paramsVo.getDateTo());
		}
		sqlBuilder.append(" and t.sku_id != ''");
		sqlBuilder.append(" group by %s");
		
		PromIndexTrendVo trendVo = new PromIndexTrendVo();
		String sql = sqlBuilder.toString();
		//24小时趋势
		if (paramsVo.getDateFrom().equals(paramsVo.getDateTo())){
			sql = String.format(sql, "hour_time", "goods_hourly","hour_time");
			Map<String, Tmp> tmpMap = new HashMap<String, PromServiceImpl.Tmp>();
			
			SqlRowSet rowSet = namedParameterJdbcTemplate.queryForRowSet(sql, params);
			while(rowSet.next()){
				Tmp tmp = new Tmp(rowSet.getInt("pv"), rowSet.getInt("uv"), rowSet.getDouble("exitRate"),
						rowSet.getDouble("orderUvRate"), rowSet.getDouble("orderCountAvg") , rowSet.getLong("payCount"), rowSet.getDouble("payMoney"));
				tmpMap.put(rowSet.getString("hour_time"), tmp);
			}
			for(int i = 0 ; i < 24; i++){
				String dt = i + "";
				trendVo.getxAxis().add(dt);
				Tmp tmp = tmpMap.get(dt);
				if (tmp == null){
					trendVo.getPvs().add(0);
					trendVo.getUvs().add(0);
					trendVo.getExitRates().add(0.0);
					trendVo.getOrderUvRates().add(0.0);
					trendVo.getOrderCountAvgs().add(0.0);
					trendVo.getPayCounts().add(0L);
					trendVo.getPayMoneys().add(0.0);
				}else{
					trendVo.getPvs().add(tmp.pv);
					trendVo.getUvs().add(tmp.uv);
					trendVo.getExitRates().add(tmp.exitRate);
					trendVo.getOrderUvRates().add(tmp.orderUvRate);
					trendVo.getOrderCountAvgs().add(tmp.orderCountAvg);
					trendVo.getPayCounts().add(tmp.payCount);
					trendVo.getPayMoneys().add(tmp.payMoney);
				}
			}
			
		}else{//天趋势
			sql = String.format(sql,"dt", "goods_detail","dt");
			Map<String, Tmp> tmpMap = new HashMap<String, PromServiceImpl.Tmp>();
			
			SqlRowSet rowSet = namedParameterJdbcTemplate.queryForRowSet(sql, params);
			while(rowSet.next()){
				Tmp tmp = new Tmp(rowSet.getInt("pv"), rowSet.getInt("uv"), rowSet.getDouble("exitRate"),
						rowSet.getDouble("orderUvRate"), rowSet.getDouble("orderCountAvg") , rowSet.getLong("payCount"), rowSet.getDouble("payMoney"));
				tmpMap.put(rowSet.getString("dt"), tmp);
			}
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");   
			for(int i = 0 ; ; i++){
				Date now = new Date(paramsVo.getDateFrom().getTime() + i * 86400000L);
				if (now.after(paramsVo.getDateTo())){
					break;
				}
				String dt = df.format(now);
				trendVo.getxAxis().add(dt);
				Tmp tmp = tmpMap.get(dt);
				if (tmp == null){
					trendVo.getPvs().add(0);
					trendVo.getUvs().add(0);
					trendVo.getExitRates().add(0.0);
					trendVo.getOrderUvRates().add(0.0);
					trendVo.getOrderCountAvgs().add(0.0);
					trendVo.getPayCounts().add(0L);
					trendVo.getPayMoneys().add(0.0);
				}else{
					trendVo.getPvs().add(tmp.pv);
					trendVo.getUvs().add(tmp.uv);
					trendVo.getExitRates().add(tmp.exitRate);
					trendVo.getOrderUvRates().add(tmp.orderUvRate);
					trendVo.getOrderCountAvgs().add(tmp.orderCountAvg);
					trendVo.getPayCounts().add(tmp.payCount);
					trendVo.getPayMoneys().add(tmp.payMoney);
				}
			}
		}
		
		return trendVo;
	}

	/**
	 * 查询促销商品列表
	 */
	@Override
	public PageBean<GdsRankVo> getPromGdsList(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		PromInfoVo promInfo = getPromInfoVo(param.getPromId());
		List<Integer> skuIds = getPromSkuIdList(promInfo);
		
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer();
		//促销关联单品id)
		if(!skuIds.isEmpty()){//sku表左关联goods_detail sum结果表，采用小表（sku)左关联大表（goods_detail）sum结果,避免goods_detail无相关记录，导致商品表不展示商品信息
			sql.append("select gdspic.vfs_id picId, sku.stock, sku.status state, sku.cat_id catId, sku.sku_name gdsName, sku.sku_prop_desc skuPropDesc, sku.agent_price price, sku.sku_prop skuProp,sku.update_time shelvesTime, "+
						"tt.gdsId, tt.skuId, tt.pv, tt.uv, tt.exitRate, tt.orderUvRate,tt.payCount, tt.payMoney, tt.orderCountAvg "+
						"from t_goods_sku sku left join t_goods_pic gdspic on sku.gds_id = gdspic.gds_id "+
						"left join (select t.goods_id gdsId, t.sku_id skuId, sum(t.pv) pv, sum(t.uv) uv, sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv) orderUvRate,sum(t.pay_quantities) payCount, sum(t.pay_amount) payMoney, t.order_count/t.order_uv orderCountAvg from goods_detail as t where 1=1");
		
		}else{//goods_detail左关联sku表
			sql.append("select t.goods_id gdsId, t.sku_id skuId, gdspic.vfs_id picId, sku.stock, sku.status state, sku.cat_id catId, sku.sku_name gdsName, sku.sku_prop_desc skuPropDesc, sku.agent_price price, sku.sku_prop skuProp, " +
						" sku.update_time shelvesTime, sum(t.pv) pv, sum(t.uv) uv, sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv) orderUvRate," +
						" sum(t.pay_quantities) payCount, sum(t.pay_amount) payMoney, t.order_count/t.order_uv orderCountAvg from goods_detail t left join t_goods_sku sku on t.sku_id=sku.sku_id");
			sql.append(" left join t_goods_pic gdspic on t.goods_id = gdspic.gds_id where 1=1");
		}
		
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = (:shopId)");
			params.put("shopId",promInfo.getShopId());
		}
		
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = (:clientType)");
			params.put("clientType",param.getSource());
		}
		
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= (:dateFrom)");
			params.put("dateFrom",df.format(param.getDateFrom()));
			sql.append(" and t.dt <= (:dateTo)");
			params.put("dateTo",df.format(param.getDateTo()));
		}
		
		if(!skuIds.isEmpty()){//促销关联单品id
			sql.append(" and t.sku_id in (:promSkuIds)");
			params.put("promSkuIds", skuIds);
			
			
			sql.append(" group by t.sku_id) as tt on tt.skuid = sku.sku_id where sku.sku_id in (:skuIds)");
			params.put("skuIds", skuIds);
			
		}else{
			sql.append(" and t.sku_id != ''");
			sql.append(" group by t.sku_id");
		}
		
		if (StringUtils.isNotEmpty(param.getOrderTypeName())) {
			sql.append(" order by " + param.getOrderTypeName());
			if (StringUtils.isNotEmpty(param.getOrderWay())) {
				sql.append(" " + param.getOrderWay());
			}
		}
		
		int count = 0;
		if (param.getPageNo() != null  && param.getPageNo() != 0 && param.getPageSize() != null) {
			count = namedParameterJdbcTemplate.queryForObject("select count(*) from ("+sql.toString()+") as _tn", params, Integer.class);
			
			sql.append(" limit "+((param.getPageNo() - 1) * param.getPageSize())+","+(param.getPageSize()));
		}
		
		List<GdsRankVo> list = namedParameterJdbcTemplate.query(sql.toString(), params, new GoodsAnalyServiceImpl().new GdsRankRowMapper());
		PageBean<GdsRankVo> pageBean = new PageBean<GdsRankVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		return pageBean;
	}
	
	/**
	 * 查询促销、促销商品指标日期列表
	 * @param paramsVo
	 * @return
	 */
	public PageBean<PromIndexDateSumVo> getPromIndexDateList(QueryParamsVo paramsVo){
		PromInfoVo promInfo = getPromInfoVo(paramsVo.getPromId());
		List<Integer> skuIds = null;
		//查询促销单品指标趋势
		if(!StringUtils.isEmpty(paramsVo.getSkuId())){
			skuIds = new ArrayList<Integer>();
			skuIds.add(Integer.valueOf(paramsVo.getSkuId()));
			
		}else{//查询促销指标趋势
			skuIds = getPromSkuIdList(promInfo);
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuilder sqlBuilder = new StringBuilder();
		
		sqlBuilder.append("select sum(t.pv) pv, sum(t.uv) uv, sum(t.exit_pages)/sum(t.pv) exitRate, sum(t.order_uv)/sum(t.uv) orderUvRate,"
		+ " sum(t.pay_quantities) payCount, sum(t.pay_amount) payMoney, sum(t.order_count)/sum(t.order_uv) orderCountAvg, t.%s from %s t where 1=1");
		
		sqlBuilder.append(" and t.shop_id = (:shopId)");
		params.put("shopId", promInfo.getShopId());
		
		if(!skuIds.isEmpty()){//促销关联单品id
			sqlBuilder.append(" and t.sku_id in (:skuIds)");
			params.put("skuIds", skuIds);
		}
		
		if (paramsVo.getSource() != 0) {
			sqlBuilder.append(" and t.client_type = (:clientType)");
			params.put("clientType", paramsVo.getSource());
		}
		
		if (paramsVo.getDateFrom() != null && paramsVo.getDateTo() != null) {
			sqlBuilder.append(" and t.dt >= (:dateFrom)");
			params.put("dateFrom", paramsVo.getDateFrom());
			
			sqlBuilder.append(" and t.dt <= (:dateTo)");
			params.put("dateTo", paramsVo.getDateTo());
		}
		
		sqlBuilder.append(" and t.sku_id != ''");
		sqlBuilder.append(" group by %s");
		
		List<PromIndexDateSumVo> recordList = new ArrayList<PromIndexDateSumVo>();
		int recordCount = 0;
		PromIndexDateSumVo tempRecord;
		
		String sql = sqlBuilder.toString();
		//24小时趋势
		if (paramsVo.getDateFrom().equals(paramsVo.getDateTo())){
			//goods_detail表无hour字段，暂不支持24小时趋势
			
		}else{//天趋势
			sql = String.format(sql,"dt", "goods_detail","dt");
			Map<String, Tmp> tmpMap = new HashMap<String, Tmp>();
			
			SqlRowSet rowSet = namedParameterJdbcTemplate.queryForRowSet(sql, params);
			while(rowSet.next()){
				Tmp tmp = new Tmp(rowSet.getInt("pv"), rowSet.getInt("uv"), rowSet.getDouble("exitRate"),
						rowSet.getDouble("orderUvRate"), rowSet.getDouble("orderCountAvg") , rowSet.getLong("payCount"), rowSet.getDouble("payMoney"));
				tmpMap.put(rowSet.getString("dt"), tmp);
			}
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");   
			for(int i = 0 ; ; i++){
				Date now = new Date(paramsVo.getDateFrom().getTime() + i * 86400000L);
				if (now.after(paramsVo.getDateTo())){
					//记录总数
					recordCount = i;
					break;
				}
				String dt = df.format(now);
				tempRecord = new PromIndexDateSumVo();
				tempRecord.setDt(dt);
				Tmp tmp = tmpMap.get(dt);
				if (tmp == null){
					tempRecord.setPv(0);
					tempRecord.setUv(0);
					tempRecord.setExitRate(0.0);
					tempRecord.setOrderUvRate(0.0);
					tempRecord.setOrderCountAvg(0.0);
					tempRecord.setPayCount(0L);
					tempRecord.setPayMoney(0.0);
				}else{
					tempRecord.setPv(tmp.pv);
					tempRecord.setUv(tmp.uv);
					tempRecord.setExitRate(tmp.exitRate);
					tempRecord.setOrderUvRate(tmp.orderUvRate);
					tempRecord.setOrderCountAvg(tmp.orderCountAvg);
					tempRecord.setPayCount(tmp.payCount);
					tempRecord.setPayMoney(tmp.payMoney);
				}
				
				recordList.add(tempRecord);
			}
		}
		int toIndex = paramsVo.getPageNo()*paramsVo.getPageSize()>recordCount?recordCount:paramsVo.getPageNo()*paramsVo.getPageSize();
		List<PromIndexDateSumVo> currentPageRecordList = recordList.subList((paramsVo.getPageNo()-1)*paramsVo.getPageSize(), toIndex);
		PageBean<PromIndexDateSumVo> pageBean = new PageBean<PromIndexDateSumVo>(paramsVo.getPageNo(),paramsVo.getPageSize(), currentPageRecordList, recordCount);
		
		return pageBean;
	}
	
	/**
	 * 查询promId对应促销信息
	 * 
	 * @param promId
	 * @return
	 */
	private PromInfoVo getPromInfoVo(String promId) {
		List<Object> params = new ArrayList<Object>();
		
		String sql = "select * from t_prom_info where id = ?";
		params.add(promId);

		PromInfoVo promInfo = queryOne(sql, params, new BeanPropertyRowMapper<PromInfoVo>(PromInfoVo.class));

		return promInfo;
	}

	/**
	 * 查询促销关联的单品id集合
	 * 
	 * @param promId
	 * @return
	 */
	private List<Integer> getPromSkuIdList(PromInfoVo promInfo) {
		List<Integer> skuIdList = new ArrayList<Integer>();

		if ("1".equals(promInfo.getJoinRange())) {// 全部商品参加
			//TODO
			//全部商品指该店铺在这个站点的所有商品，目前没有加上站点过滤
			
		}else if ("1".equals(promInfo.getIfMatch())) {// 搭配单品
			String sql = "select sku_id from t_prom_match_sku where prom_id = " + promInfo.getId();
			skuIdList = getJdbcTemplate().queryForList(sql, Integer.class);

		} else {//
			String sql = "select sku_id from t_prom_sku where prom_id = " + promInfo.getId();
			skuIdList = getJdbcTemplate().queryForList(sql, Integer.class);
		}

		return skuIdList;
	}
	
	class Tmp{
		int pv;
		int uv;
		double exitRate;
		double orderUvRate;
		double orderCountAvg;
		long payCount;
		double payMoney;
		
		
		public Tmp(int pv, int uv, double exitRate, double orderUvRate, double orderCountAvg, long payCount, double payMoney){
			this.pv = pv;
			this.uv = uv;
			this.exitRate = exitRate;
			this.orderUvRate = orderUvRate;
			this.orderCountAvg = orderCountAvg;
			this.payCount = payCount;
			this.payMoney = payMoney;
		}
	}
}
