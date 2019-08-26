package com.ai.analy.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.constants.Constants;
import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.TradeService;
import com.ai.analy.utils.CommonUtils;
import com.ai.analy.utils.DataConvertUtil;
import com.ai.analy.utils.DateUtils;
import com.ai.analy.vo.comm.AreaInfoVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;
import com.ai.analy.vo.trade.DailyMemberAnalyVO;
import com.ai.analy.vo.trade.DailyTradeDataVO;
import com.ai.analy.vo.trade.TradeAssociationVo;
import com.ai.analy.vo.trade.TradeCategorySumVo;
import com.ai.analy.vo.trade.TradeMapVo;
import com.ai.analy.vo.trade.TradeMonthSumVo;
import com.ai.analy.vo.trade.TradeOrdersTypeSumVo;
import com.ai.analy.vo.trade.TradeRankVo;
import com.alibaba.dubbo.common.utils.StringUtils;

public class TradeServiceImpl extends BaseDao implements TradeService {

	@Override
	public GoodsOverviewVo getTradeOverview(QueryParamsVo param) {
		// TODO Auto-generated method stub

		// 本期
		GoodsOverviewVo currentGdsOverview = queryGoodsOverviewVo(param);

		// 上期
		param.setDateFrom(param.getPreDateFrom());
		param.setDateTo(param.getPreDateTo());
		GoodsOverviewVo lastTimeGdsOverview = queryGoodsOverviewVo(param);

		if (currentGdsOverview != null && lastTimeGdsOverview != null) {
			double uvCompare = (currentGdsOverview.getUv() - lastTimeGdsOverview.getUv())
					/ (double) lastTimeGdsOverview.getUv();
			currentGdsOverview.setUvCompare(uvCompare);
			double pvCompare = (currentGdsOverview.getPv() - lastTimeGdsOverview.getPv())
					/ (double) lastTimeGdsOverview.getPv();
			currentGdsOverview.setPvCompare(pvCompare);

			double orderAmountCompare = (currentGdsOverview.getOrderAmount() - lastTimeGdsOverview.getOrderAmount())
					/ (double) lastTimeGdsOverview.getOrderAmount();
			currentGdsOverview.setOrderAmountCompare(orderAmountCompare);

			double orderUvCompare = (currentGdsOverview.getOrderUv() - lastTimeGdsOverview.getOrderUv())
					/ (double) lastTimeGdsOverview.getOrderUv();
			currentGdsOverview.setOrderUvCompare(orderUvCompare);

			double payAmountCompare = (currentGdsOverview.getPayAmount() - lastTimeGdsOverview.getPayAmount())
					/ (double) lastTimeGdsOverview.getPayAmount();
			currentGdsOverview.setPayAmountCompare(payAmountCompare);

			double payUvCompare = (currentGdsOverview.getPayUv() - lastTimeGdsOverview.getPayUv())
					/ (double) lastTimeGdsOverview.getPayUv();
			currentGdsOverview.setPayUvCompare(payUvCompare);

			// 下单转化率
			currentGdsOverview.setOrderUvRate(currentGdsOverview.getOrderUv() / (double) currentGdsOverview.getUv());
			// 下单支付转化率
			currentGdsOverview
					.setOrderPayUvRate(currentGdsOverview.getPayUv() / (double) currentGdsOverview.getOrderUv());
			// 支付转化率
			currentGdsOverview.setPayUvRate(currentGdsOverview.getPayUv() / (double) currentGdsOverview.getUv());
			// 客单价
			currentGdsOverview.setPayPrice(currentGdsOverview.getPayAmount() / (double) currentGdsOverview.getPayUv());
			lastTimeGdsOverview
					.setPayPrice(lastTimeGdsOverview.getPayAmount() / (double) lastTimeGdsOverview.getPayUv());
			// 客单价较上期
			double payPriceCompare = (currentGdsOverview.getPayPrice() - lastTimeGdsOverview.getPayPrice())
					/ lastTimeGdsOverview.getPayPrice();
			currentGdsOverview.setPayPriceCompare(payPriceCompare);
		}

		return currentGdsOverview;
	}

	@Override
	public List<TradeMapVo> queryTradeMap(QueryParamsVo param) {
		// TODO Auto-generated method stub

		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}

		List<Object> params = new ArrayList<Object>();
		String filedName = "t.province";
		if (StringUtils.isNotEmpty(param.getProvinceCode())) {
			filedName = "t.city";
		}

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  " + filedName + " provinceCode," + " tba.area_name provinceName," + " sum(t.uv) uv,"
				+ " sum(t.pay_uv)/sum(t.uv) payUvRate," + " sum(t.pay_amount)/sum(t.pay_uv) payPrice,"
				+ " sum(t.order_count) tradeCount," + " sum(t.order_amount) tradeAmount"
				+ " from flow_user_region t left join t_base_area_admin tba on " + filedName + " = tba.area_code"
				+ " where 1=1" + " AND t.province != '9A'" + " AND t.province != '' ");

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

		if (StringUtils.isNotEmpty(param.getProvinceCode())) {
			sql.append(" and t.province = ?");
			params.add(param.getProvinceCode());
			sql.append(" group by t.city");
		} else {
			sql.append(" group by t.province");
		}

		// 按省份分组
		List<TradeMapVo> list = queryList(sql.toString(), params, new TradeMapRowMapper());

		// 计算百分比
		if (list != null && list.size() > 0) {
			double totalAmount = 0;
			double totalCount = 0;
			for (TradeMapVo tradeMapVo : list) {
				totalAmount += tradeMapVo.getTradeAmount();
				totalCount += tradeMapVo.getTradeCount();
			}

			// 设置百分比
			for (TradeMapVo tradeMapVo : list) {
				tradeMapVo.setAmountScale(tradeMapVo.getTradeAmount() / totalAmount);
				tradeMapVo.setCountScale(tradeMapVo.getTradeCount() / totalCount);
			}
		}

		return list;

	}

	@Override
	public List<AreaInfoVo> queryAreaByParentCode(String parentAreaCode) {

		List<AreaInfoVo> list = new ArrayList<AreaInfoVo>();

		if (StringUtils.isEmpty(parentAreaCode)) {
			return list;
		}

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT" + " t.area_code areaCode," + " t.area_name areaName," + " t.parent_area_code parentAreaCode"
				+ " FROM t_base_area_admin t" + " WHERE t.parent_area_code = ?" + " AND t.area_code != ?"
				+ " AND t. STATUS = ?" + " ORDER BY t.area_order");

		params.add(parentAreaCode);
		params.add("");
		params.add("1");

		list = this.queryList(sql.toString(), params, new RowMapper<AreaInfoVo>() {
			@Override
			public AreaInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				AreaInfoVo areaInfoVo = new AreaInfoVo();

				areaInfoVo.setAreaCode(rs.getString("areaCode"));
				areaInfoVo.setAreaName(rs.getString("areaName"));
				areaInfoVo.setParentAreaCode(rs.getString("parentAreaCode"));

				return areaInfoVo;
			}
		});

		return list;
	}

	protected class TradeMapRowMapper implements RowMapper<TradeMapVo> {
		public TradeMapVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TradeMapVo fMap = new TradeMapVo();
			fMap.setProvinceCode(rs.getString("provinceCode"));
			if (StringUtils.isEmpty(rs.getString("provinceName"))) {
				fMap.setProvinceName(fMap.getProvinceCode());
			} else {
				fMap.setProvinceName(rs.getString("provinceName"));
			}
			fMap.setTradeCount(rs.getLong("tradeCount"));
			fMap.setTradeAmount(rs.getLong("tradeAmount"));
			fMap.setUv(rs.getLong("uv"));
			fMap.setPayPrice(rs.getDouble("payPrice"));
			fMap.setPayUvRate(rs.getDouble("payUvRate"));

			return fMap;
		}
	}

	protected class GoodsOverviewRowMapper implements RowMapper<GoodsOverviewVo> {
		public GoodsOverviewVo mapRow(ResultSet rs, int rowNum) throws SQLException {

			GoodsOverviewVo gdsOver = new GoodsOverviewVo();
			gdsOver.setUv(rs.getLong("uv"));
			gdsOver.setPv(rs.getLong("pv"));
			gdsOver.setPayQuantities(rs.getLong("payQuantities"));
			gdsOver.setOrderAmount(rs.getLong("orderAmount"));
			gdsOver.setOrderUv(rs.getLong("orderUv"));
			gdsOver.setPayAmount(rs.getLong("payAmount"));
			gdsOver.setPayUv(rs.getLong("payUv"));
			return gdsOver;

		}
	}

	private GoodsOverviewVo queryGoodsOverviewVo(QueryParamsVo param) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT" + " sum(t.uv) uv," + " sum(t.pv) pv," + " sum(t.pay_quantities) payQuantities,"
				+ " sum(t.order_amount) orderAmount," + " sum(t.order_uv) orderUv," + " sum(t.pay_amount) payAmount,"
				+ " sum(t.pay_uv) payUv" + " FROM flow_user_region t" + " WHERE 1=1");

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

		if (StringUtils.isBlank(param.getProvinceCode()) == false) {
			sql.append(" and t.province = ?");
			params.add(param.getProvinceCode());
		}

		if (StringUtils.isBlank(param.getCityCode()) == false) {
			sql.append(" and t.city = ?");
			params.add(param.getCityCode());
		}

		GoodsOverviewVo goodsOverviewVo = queryOne(sql.toString(), params, new GoodsOverviewRowMapper());

		return goodsOverviewVo;
	}

	@Override
	public List<TradeRankVo> queryTradeRank(QueryParamsVo param, List<String> shopIds) {
		// TODO Auto-generated method stub

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT" + " t.dt_from dateFrom," + " t.dt_to dateTo," + " t.shop_id shopId,"
				+ " s.shop_full_name shopName," + " s.logo_path shopLogo," + " t.cat_id catId," + " c.cat_name catName,"
				+ " t.pay_quantites_rank+1 countRank," + " t.pay_quantites tradeCount,"
				+ " t.pay_amount_rank+1 amountRank," + " t.pay_quantites_rank_percent countLevel"
				+ " FROM transaction_rank_all t" + " LEFT JOIN t_goods_spu_category c" + " ON t.cat_id = c.cat_id"
				+ " LEFT JOIN t_shop_main_info s" + " ON t.shop_id = s.shop_id" + " WHERE 1=1");

		if (shopIds != null && shopIds.size() > 0) {
			String inSql = "";
			for (String shopId : shopIds) {
				params.add(shopId);
				inSql += ",?";
			}
			inSql = inSql.substring(1);
			sql.append(" and t.shop_id in(" + inSql + ")");
		}
		if (StringUtils.isNotEmpty(param.getCatId())) {
			sql.append(" AND t.cat_id = ?");
			params.add(param.getCatId());
		}
		if (param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt_to = ?");
			params.add(df.format(param.getDateTo()));
		}

		sql.append(" ORDER BY t.pay_quantites_rank asc");
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		return this.queryList(sql.toString(), params, new TradeRankRowMapper());
	}

	protected class TradeRankRowMapper implements RowMapper<TradeRankVo> {

		@Override
		public TradeRankVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub

			TradeRankVo tradeRankVo = new TradeRankVo();
			tradeRankVo.setDateFrom(rs.getDate("dateFrom"));
			tradeRankVo.setDateTo(rs.getDate("dateTo"));
			tradeRankVo.setShopId(rs.getString("shopId"));
			String shopName = rs.getString("shopName");
			if (StringUtils.isEmpty(shopName)) {
				shopName = tradeRankVo.getShopId();
			}
			tradeRankVo.setShopName(shopName);
			tradeRankVo.setShopLogo(rs.getString("shopLogo"));
			tradeRankVo.setCatId(rs.getString("catId"));
			tradeRankVo.setCatName(rs.getString("catName"));
			tradeRankVo.setAmountRank(rs.getInt("amountRank"));
			tradeRankVo.setCountLevel(rs.getDouble("countLevel"));
			tradeRankVo.setTradeCount(rs.getLong("tradeCount"));
			tradeRankVo.setCountRank(rs.getInt("countRank"));

			return tradeRankVo;
		}
	}

	/**
	 * 
	 * @param param
	 *            查询参数
	 * @param pre
	 *            几个周期
	 * @return
	 */
	@Override
	public List<TradeRankVo> queryTradeRankTrend(QueryParamsVo param, List<String> preTimes) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT" + " t.dt_from dateFrom," + " t.dt_to dateTo," + " t.shop_id shopId,"
				+ " s.shop_full_name shopName," + " s.logo_path shopLogo," + " t.cat_id catId," + " c.cat_name catName,"
				+ " t.pay_quantites_rank+1 countRank," + " t.pay_quantites tradeCount,"
				+ " t.pay_amount_rank+1 amountRank," + " t.pay_quantites_rank_percent countLevel"
				+ " FROM transaction_rank_all t" + " LEFT JOIN t_goods_spu_category c" + " ON t.cat_id = c.cat_id"
				+ " LEFT JOIN t_shop_main_info s" + " ON t.shop_id = s.shop_id" + " WHERE 1=1");

		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String inSql = "?";
		params.add(df.format(param.getDateTo()));

		for (String preTime : preTimes) {
			inSql += ",?";
			params.add(preTime);
		}

		sql.append(" AND t.dt_to in (" + inSql + ")");
		sql.append(" ORDER BY t.pay_quantites desc");

		return this.queryList(sql.toString(), params, new TradeRankRowMapper());
	}

	/**
	 * 查询按渠道类型统计的数据 T1-T6
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<StructureSectionVo> queryUserRegion(QueryParamsVo param) {

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT" + " t.T_type chnlType," + " sum(t.pv) pv," + " sum(t.uv) uv,"
				+ " sum(t.pay_quantities) tradeCount," + " sum(t.pay_amount) tradeAmount,"
				+ " sum(t.order_uv)/sum(t.uv) orderRate," + " sum(t.pay_uv)/sum(t.uv) payUvRate,"
				+ " sum(t.pay_amount)/sum(t.pay_uv) payPrice" + " FROM channel_region t" + " WHERE 1=1");

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

		sql.append(" GROUP BY t.T_type");
		sql.append(" ORDER BY case when t.T_type ='' then 1 else 0 end, t.T_type");

		return this.queryList(sql.toString(), params, new RowMapper<StructureSectionVo>() {
			@Override
			public StructureSectionVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub

				StructureSectionVo vo = new StructureSectionVo();

				String chnlType = rs.getString("chnlType");
				if (StringUtils.isBlank(chnlType)) {
					chnlType = "其他";
				}

				vo.setChnlType(chnlType);
				vo.setPv(rs.getInt("pv"));
				vo.setUv(rs.getLong("uv"));
				vo.setTradeCount(rs.getLong("tradeCount"));
				vo.setTradeAmount(rs.getDouble("tradeAmount"));
				vo.setOrderRate(rs.getDouble("orderRate"));
				vo.setPayUvRate(rs.getDouble("payUvRate"));
				vo.setPayPrice(rs.getDouble("payPrice"));

				return vo;
			}

		});
	}

	@Override
	public List<StructureSectionVo> queryChnlType(QueryParamsVo param) {
		// TODO Auto-generated method stub

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT" + " t.user_type userType," + " sum(t.pv) pv," + " sum(t.uv) uv,"
				+ " sum(t.pay_quantities) tradeCount," + " sum(t.pay_amount) tradeAmount,"
				+ " sum(t.order_uv)/sum(t.uv) orderRate," + " sum(t.pay_uv)/sum(t.uv) payUvRate,"
				+ " sum(t.pay_amount)/sum(t.pay_uv) payPrice" + " FROM channel_type t" + " WHERE 1=1");

		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" AND t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" AND t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" AND t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" AND t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		// 新老用户
		if (Constants.UserType.QueryType.NEW_AND_OLD.equals(param.getUserType())) {
			sql.append("AND t.user_type in(?,?)");
			params.add(Constants.UserType.USER_NEW);
			params.add(Constants.UserType.USER_OLD);
		}

		// 连锁和非连锁
		if (Constants.UserType.QueryType.CHAIN.equals(param.getUserType())) {
			sql.append("AND t.user_type in(?,?)");
			params.add(Constants.UserType.USER_CHAIN);
			params.add(Constants.UserType.USER_NOT_CHAIN);
		}

		sql.append(" GROUP BY t.user_type");

		List<StructureSectionVo> list = this.queryList(sql.toString(), params, new RowMapper<StructureSectionVo>() {
			@Override
			public StructureSectionVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub

				StructureSectionVo vo = new StructureSectionVo();

				String chnlType = Constants.UserType.getUserTypeName(rs.getString("userType"));
				if (StringUtils.isBlank(chnlType)) {
					chnlType = "其他";
				}

				vo.setChnlType(chnlType);
				vo.setPv(rs.getInt("pv"));
				vo.setUv(rs.getLong("uv"));
				vo.setTradeCount(rs.getLong("tradeCount"));
				vo.setTradeAmount(rs.getDouble("tradeAmount"));
				vo.setOrderRate(rs.getDouble("orderRate"));
				vo.setPayUvRate(rs.getDouble("payUvRate"));
				vo.setPayPrice(rs.getDouble("payPrice"));

				return vo;
			}

		});

		return getChnlTypeAllDatas(list, param.getUserType());
	}

	private List<StructureSectionVo> getChnlTypeAllDatas(List<StructureSectionVo> list, String queryType) {

		StructureSectionVo vo1 = new StructureSectionVo();
		StructureSectionVo vo2 = new StructureSectionVo();

		if (Constants.UserType.QueryType.NEW_AND_OLD.equals(queryType)) {
			vo1.setChnlType(Constants.UserType.getUserTypeName(Constants.UserType.USER_NEW));
			vo2.setChnlType(Constants.UserType.getUserTypeName(Constants.UserType.USER_OLD));
		}

		if (Constants.UserType.QueryType.CHAIN.equals(queryType)) {
			vo1.setChnlType(Constants.UserType.getUserTypeName(Constants.UserType.USER_CHAIN));
			vo2.setChnlType(Constants.UserType.getUserTypeName(Constants.UserType.USER_NOT_CHAIN));
		}

		if (list == null || list.size() == 0) {

			list = new ArrayList<StructureSectionVo>();

			list.add(vo1);
			list.add(vo2);

			return list;
		} else {

			if (list.size() > 1) {
				return list;
			} else {

				StructureSectionVo structureSectionVo = list.get(0);

				if (structureSectionVo.getChnlType()
						.equals(Constants.UserType.getUserTypeName(Constants.UserType.USER_NEW))) {
					list.add(vo2);
				}

				if (structureSectionVo.getChnlType()
						.equals(Constants.UserType.getUserTypeName(Constants.UserType.USER_OLD))) {
					list.add(vo1);
				}

				if (structureSectionVo.getChnlType()
						.equals(Constants.UserType.getUserTypeName(Constants.UserType.USER_CHAIN))) {
					list.add(vo2);
				}

				if (structureSectionVo.getChnlType()
						.equals(Constants.UserType.getUserTypeName(Constants.UserType.USER_NOT_CHAIN))) {
					list.add(vo1);
				}

				return list;
			}
		}
	}

	@Override
	public List<StructureSectionVo> queryChnlBehavior(QueryParamsVo param) {
		// TODO Auto-generated method stub

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		String filedName = "v_1month";
		if ("3".equals(param.getCatId())) {
			filedName = "v_3month";
		}
		if ("6".equals(param.getCatId())) {
			filedName = "v_6month";
		}

		sql.append("SELECT t2.* from(" + " SELECT" + " t.orders_type chnlType," + " sum(t." + filedName + ") orderCount"
				+ " FROM channel_behavior_order t" + " WHERE 1=1");

		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" AND t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" AND t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" AND t.dt = ?");
			params.add(df.format(param.getDateTo()));
		}
		// 1：件数 2：次数
		if (StringUtils.isNotEmpty(param.getTarget())) {
			sql.append(" AND t.behavior_type = ?");
			params.add(param.getTarget());
		}

		sql.append(" GROUP BY t.orders_type" + " ) t2" + " LEFT JOIN flow_map_channel_type t3"
				+ " on t2.chnlType = t3.orders_type" + " ORDER BY t3.orderNum");

		List<StructureSectionVo> result = this.queryList(sql.toString(), params, new RowMapper<StructureSectionVo>() {
			@Override
			public StructureSectionVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub

				StructureSectionVo vo = new StructureSectionVo();

				vo.setChnlType(rs.getString("chnlType"));
				vo.setOrderCount(rs.getLong("orderCount"));

				return vo;
			}

		});

		// 计算占比
		if (result != null && result.size() > 0) {
			long totalCount = 0;
			for (StructureSectionVo structureSectionVo : result) {
				totalCount += structureSectionVo.getOrderCount();
			}

			for (StructureSectionVo structureSectionVo : result) {
				structureSectionVo.setScale(structureSectionVo.getOrderCount() / (double) totalCount);
			}
		}

		return result;
	}

	@Override
	public PageBean<TradeAssociationVo> queryAssociation(QueryParamsVo param) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sql.append("SELECT" + " tt.*," + " gdspic1.vfs_id picId1," + " sku1.stock stock1," + " sku1.STATUS state1,"
				+ " sku1.cat_id catId1," + " sku1.sku_name gdsName1," + " sku1.sku_prop_desc skuProp1," + "sku1.sku_prop skuIsbn1,"
				+ " sku1.agent_price price1," + " sku1.update_time shelvesTime1," + " gdspic2.vfs_id picId2,"
				+ " sku2.stock stock2," + " sku2.STATUS state2," + " sku2.cat_id catId2," + " sku2.sku_name gdsName2,"
				+ " sku2.sku_prop_desc skuProp2," + "sku2.sku_prop skuIsbn2," + " sku2.agent_price price2," + " sku2.update_time shelvesTime2"
				+ " FROM" + " (" + " SELECT" + " t.sku_id_A," + " t.sku_id_B," + " t.goods_id_A," + " t.goods_id_B,"
				+ " sum(t.nab)/sum(t.na) aRelev," + " sum(t.nab)/sum(t.ntotal) abScale,"
				+ " sum(t.nab)/sum(t.nb) bRelev," + " sum(t.nab) sumnab" + " FROM goods_association_daily t"
				+ " WHERE 1=1");

		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" AND t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" AND t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" AND t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" AND t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}

		sql.append(" GROUP BY t.sku_id_A,t.sku_id_B having sumnab >= 3) tt"
				+ " LEFT JOIN t_goods_sku sku1 ON tt.sku_id_A = sku1.sku_id"
				+ " LEFT JOIN t_goods_pic gdspic1 ON tt.goods_id_A = gdspic1.gds_id"
				+ " LEFT JOIN t_goods_sku sku2 ON tt.sku_id_B = sku2.sku_id"
				+ " LEFT JOIN t_goods_pic gdspic2 ON tt.goods_id_B = gdspic2.gds_id");

		sql.append(" ORDER BY abScale desc");

		int count = queryCount(sql.toString(), params);

		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		List<TradeAssociationVo> list = this.queryList(sql.toString(), params, new RowMapper<TradeAssociationVo>() {
			@Override
			public TradeAssociationVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub

				TradeAssociationVo vo = new TradeAssociationVo();

				// 商品A
				GdsRankVo gdsRankVo1 = new GdsRankVo();

				gdsRankVo1.setCatId(rs.getString("catId1"));
				if (StringUtils.isNotEmpty(gdsRankVo1.getCatId()) && !gdsRankVo1.getCatId().equals("900")) {
					gdsRankVo1.setSkuProp(rs.getString("skuProp1"));
				}
				gdsRankVo1.setGdsName(rs.getString("gdsName1"));
				gdsRankVo1.setPrice(rs.getLong("price1"));
				gdsRankVo1.setPicId(rs.getString("picId1"));
				gdsRankVo1.setStock(rs.getLong("stock1"));
				gdsRankVo1.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state1")));
				gdsRankVo1.setShelvesTime(rs.getTimestamp("shelvesTime1"));
				//设置商品A ISBN
				String isbn = "";
				String skuPropStr = rs.getString("skuIsbn1");
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
				gdsRankVo1.setSkuIsbn(isbn);
				// 商品B
				GdsRankVo gdsRankVo2 = new GdsRankVo();

				gdsRankVo2.setCatId(rs.getString("catId2"));
				if (StringUtils.isNotEmpty(gdsRankVo2.getCatId()) && !gdsRankVo2.getCatId().equals("900")) {
					gdsRankVo2.setSkuProp(rs.getString("skuProp2"));
				}
				gdsRankVo2.setGdsName(rs.getString("gdsName2"));
				gdsRankVo2.setPrice(rs.getLong("price2"));
				gdsRankVo2.setPicId(rs.getString("picId2"));
				gdsRankVo2.setStock(rs.getLong("stock2"));
				gdsRankVo2.setState(Constants.GoodsStatus.getGoodsStatusName(rs.getString("state2")));
				gdsRankVo2.setShelvesTime(rs.getTimestamp("shelvesTime2"));
				//设置商品B ISBN
				isbn = "";
				skuPropStr = rs.getString("skuIsbn2");
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
				gdsRankVo2.setSkuIsbn(isbn);
				
				vo.setAbScale(rs.getDouble("abScale"));
				vo.setaRelev(rs.getDouble("aRelev"));
				vo.setbRelev(rs.getDouble("bRelev"));

				vo.setGdsA(gdsRankVo1);
				vo.setGdsB(gdsRankVo2);

				return vo;
			}

		});

		PageBean<TradeAssociationVo> pageBean = new PageBean<TradeAssociationVo>(param.getPageNo(), param.getPageSize(),
				list, count);

		return pageBean;
	}

	/**
	 * 查询交易月报
	 */
	@Override
	public PageBean<TradeMonthSumVo> queryTradeMonthSum(QueryParamsVo param) {
		String sql = "select DATE_FORMAT(dt,'%Y-%m') as month, sum(pay_order_amount) as orderMoney, sum(pay_amount) payMoney, sum(order_count) as orderCount, sum(pay_count) as payCount,  sum(pay_count)/sum(order_count) as ordSuccessRate from ord_overview "
				+ "where dt >= ? AND dt <= ? AND shop_id = ? " + "group by DATE_FORMAT(dt,'%Y-%m') order by month asc";

		List<Object> params = new ArrayList<Object>();
		params.add(param.getDateFrom());
		params.add(param.getDateTo());
		params.add(param.getShopId());

		// 查询总数
		int count = 0;

		if (param.getPageNo() != null && param.getPageNo() != 0 && param.getPageSize() != null) {
			count = queryCount(sql, params);
			
			sql += " limit ?, ?";
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		// 查询分页
		List<TradeMonthSumVo> list = queryList(sql.toString(), params, new RowMapper<TradeMonthSumVo>() {
			public TradeMonthSumVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TradeMonthSumVo sumVo = new TradeMonthSumVo();
				sumVo.setMonth(rs.getString("month"));
				sumVo.setOrderMoney(rs.getInt("orderMoney"));
				sumVo.setPayMoney(rs.getInt("payMoney"));
				sumVo.setOrderCount(rs.getLong("orderCount"));
				sumVo.setPayCount(rs.getLong("payCount"));
				sumVo.setOrdSuccessRate(rs.getDouble("ordSuccessRate"));

				return sumVo;
			}
		});

		// 封装pagebean
		PageBean<TradeMonthSumVo> pageBean = new PageBean<TradeMonthSumVo>(param.getPageNo(), param.getPageSize(), list,
				count);

		return pageBean;
	}

	/**
	 * 查询商品子分类销售统计
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<TradeCategorySumVo> queryTradeCategorySum(QueryParamsVo param) {
		String parentCat = "cat_one";
		String cat = "cat_two";
		if (param.getCategoryLevel() == 2) {
			parentCat = "cat_two";
			cat = "cat_three";
		}

		String sql = "select t.#parentCat# as parentCatId, tt.brand_name as parentCatName, t.#cat# as catId, ttt.brand_name as catName, sum(t.pay_quantities) saleCount, sum(t.pay_amount) as saleMoney "
				+ "from goods_cats as t " + "left join t_goods_brand tt on t.#parentCat#=tt.brand_id "
				+ "left join t_goods_brand ttt on t.#cat#=ttt.brand_id "
				+ "where t.shop_id = ? and t.#parentCat# = ? "
				+ "group by t.#parentCat#, tt.brand_name, t.#cat#, ttt.brand_name "
				+ "order by saleMoney desc";

		sql = sql.replace("#parentCat#", parentCat).replace("#cat#", cat);

		List<Object> params = new ArrayList<Object>();
		params.add(param.getShopId());
		params.add(param.getCatId());

		List<TradeCategorySumVo> list = this.queryList(sql, params, new RowMapper<TradeCategorySumVo>() {
			@Override
			public TradeCategorySumVo mapRow(ResultSet rs, int rowNum) throws SQLException {

				TradeCategorySumVo vo = new TradeCategorySumVo();
				vo.setCatId(rs.getInt("catId"));
				vo.setCatName(rs.getString("catName"));
				vo.setParentCatId(rs.getInt("parentCatId"));
				vo.setParentCatName(rs.getString("parentCatName"));
				vo.setSaleCount(rs.getLong("saleCount"));
				vo.setSaleMoney(rs.getLong("saleMoney"));

				return vo;
			}
		});

		if (list != null && !list.isEmpty()) {
			// 构造总计vo，并加入list
			long sumSaleCount = 0, sumSaleMoney = 0;
			for (TradeCategorySumVo vo : list) {
				sumSaleCount += vo.getSaleCount();
				sumSaleMoney += vo.getSaleMoney();
			}
			TradeCategorySumVo vo = new TradeCategorySumVo();
			vo.setCatName("总计");
			vo.setSaleCount(sumSaleCount);
			vo.setSaleMoney(sumSaleMoney);
			list.add(vo);
		}
		
		return list;
	}

	@Override
	public List<TradeOrdersTypeSumVo> queryTradeOrdersTypeSum(QueryParamsVo param) {
		String sql = "select orders_type as ordersType, staff_num as staffNum, pay_amount as payAmount "
				+"from staff_order_type "
				+"where shop_id = ? "
				+"and dt = (select max(dt) from staff_order_type) "
				+"order by payAmount desc";
		
		List<Object> params = new ArrayList<Object>();
		params.add(param.getShopId());
		
		List<TradeOrdersTypeSumVo> list = this.queryList(sql, params, new RowMapper<TradeOrdersTypeSumVo>() {
			@Override
			public TradeOrdersTypeSumVo mapRow(ResultSet rs, int rowNum) throws SQLException {

				TradeOrdersTypeSumVo vo = new TradeOrdersTypeSumVo();
				vo.setOrdersType(rs.getString("ordersType"));
				vo.setStaffNum(rs.getInt("staffNum"));
				vo.setPayAmount(rs.getLong("payAmount"));
				vo.setAvgPayAmount(vo.getPayAmount()/vo.getStaffNum());

				return vo;
			}
		});
		
		return list;
	}
	
	/**
	 * 查询店铺每日的交易金额接口
	 * @param shopId
	 * @param format
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public String dailyTradeData(String shopId,String format,String certainDateStr, String startDateStr,String endDateStr){

        String resJson = null;
        
        try {
            Map<String,Object> responseMsg=null;
            //校验参数
            if (shopId==null) {
				return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
			}
            
            List<String> dateStrList=null;//要统计的日期
            
            
            //如果certainDate不为空则统计该日期的数据。如果为空则统计时间区间startDate到endDate内每天的数据。
            if (StringUtils.isNotEmpty(certainDateStr)) {
            	
            	try {
            		Timestamp certainDate=DateUtils.getTimestamp(certainDateStr, DateUtils.YYYYMMDD);
            		
            		String dateStr=DateUtils.getDateString(certainDate, DateUtils.YYYY_MM_DD);
            		dateStrList=new ArrayList<>(1);
            		dateStrList.add(dateStr);
				} catch (Exception e) {
					e.printStackTrace();
					return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
				}
				
			}else {
				
				Timestamp startDate=null;
	            Timestamp endDate=null;
				
				if (StringUtils.isNotEmpty(endDateStr)) {
					//结束日期，yyyyMMdd字符串格式，如果空值默认当天
	            	try {
	            		endDate=DateUtils.getTimestamp(endDateStr, DateUtils.YYYYMMDD);
					} catch (Exception e) {
						e.printStackTrace();
						return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
					}
				}else {
					//默认当天
					endDate=DateUtils.getSysDate();
				}
	            
	            if (StringUtils.isNotEmpty(startDateStr)) {
					//开始日期，yyyyMMdd字符串格式，如果空值默认为结束日期七天前
	            	try {
	            		startDate=DateUtils.getTimestamp(startDateStr, DateUtils.YYYYMMDD);
					} catch (Exception e) {
						e.printStackTrace();
						return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
					}
				}else {
					//默认结束日期的七天前
					startDate=DateUtils.getNDays(endDate,-6);//
				}
	            
	            //获取startDate和endDate之间的日期
	            dateStrList=DateUtils.getDates(startDate, endDate, DateUtils.YYYY_MM_DD);
			}
            
            if (dateStrList==null||dateStrList.size()<=0||dateStrList.size()>90) {
            	return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1002","时间超出范围"));
			}
           
            Map<String, DailyTradeDataVO> resultMap=new HashMap<>();
            
            StringBuilder datesIn4Query=new StringBuilder();
            for (String dateStr : dateStrList) {
            	datesIn4Query.append("'").append(dateStr).append("',");
            	
            	DailyTradeDataVO defaultVO=new DailyTradeDataVO();
            	defaultVO.setDataDate(dateStr);
            	
            	resultMap.put(dateStr, defaultVO);
			}
            datesIn4Query.deleteCharAt(datesIn4Query.length()-1);
            
//            SELECT dt as dataDate,sum(ifnull(order_amount,0)) as orderAmount,sum(ifnull(pay_amount,0)) as paidAmount,
//            sum(ifnull(order_count,0)) as orderCount,sum(ifnull(pay_count,0)) as payCount, sum(ifnull(uv,0)) as uv, sum(ifnull(order_uv,0)) as orderUv
//            FROM goods_overview  WHERE dt='2016-2-25' and shop_id='100';
            
            StringBuilder sqlBuilder=new StringBuilder();
            sqlBuilder.append("SELECT dt as dataDate,sum(ifnull(order_amount,0)) as orderAmount,sum(ifnull(pay_amount,0)) as paidAmount,");
            sqlBuilder.append(" sum(ifnull(order_count,0)) as orderCount,sum(ifnull(pay_count,0)) as payCount, sum(ifnull(uv,0)) as uv, sum(ifnull(order_uv,0)) as orderUv");
            sqlBuilder.append(" FROM goods_overview  WHERE ");
            sqlBuilder.append("  dt in (").append(datesIn4Query).append(") ");
            if (!"-1".equals(shopId)) {
            	//如果是-1则统计全平台
            	sqlBuilder.append(" and shop_id='").append(shopId).append("' ");
			}
            sqlBuilder.append(" group by dt ");
            
            
            List<DailyTradeDataVO> queryResultList = this.queryList(sqlBuilder.toString(), null, new RowMapper<DailyTradeDataVO>() {
    			@Override
    			public DailyTradeDataVO mapRow(ResultSet rs, int rowNum) throws SQLException {

    				DailyTradeDataVO vo = new DailyTradeDataVO();
    				
    				String dataDate=rs.getString("dataDate");
    				
    				if (dataDate==null) {
						return vo;
					}
    				vo.setDataDate(dataDate);
    				
    				Double orderAmountF=rs.getDouble("orderAmount");//下单金额，单位：分
    				vo.setOrderAmount(orderAmountF==null?"0":""+orderAmountF.longValue());
    				
    				Double payAmountF=rs.getDouble("paidAmount");//已支付金额，单位：分
    				vo.setPaidAmount(payAmountF==null?"0":""+payAmountF.longValue());
    				
    				int orderCount=rs.getInt("orderCount");//订单总量
    				vo.setOrderCount(orderCount);
    				
    				int payCount=rs.getInt("payCount");//支付成功量
    				vo.setPayCount(payCount);
    				
    				int uv=rs.getInt("uv");
    				int orderUv=rs.getInt("orderUv");
    				
    				//下单成功率。例：百分之1.22则返回1.22
    				if (uv<=0) {
    					vo.setOrdSuccRate(""+0);
					}else {
						//下单成功率=orderUv/uv
						double ordSuccRate=((double)orderUv/uv)*100;
						//转为string保留两位小数
						BigDecimal bigDecimal=new BigDecimal(ordSuccRate).setScale(2,BigDecimal.ROUND_HALF_UP);
				        vo.setOrdSuccRate(bigDecimal.toString());
					}
    				
    				return vo;
    			}
    		});
            
            for (DailyTradeDataVO queryResultVO : queryResultList) {
            	
            	String dataDateInList=queryResultVO.getDataDate();
            	if (dataDateInList==null) {
					continue;
				}
            	
            	DailyTradeDataVO voInMap=resultMap.get(dataDateInList);
            	if (voInMap==null) {
            		resultMap.put(queryResultVO.getDataDate(), queryResultVO);
				}else {
					voInMap.setOrderAmount(queryResultVO.getOrderAmount());
					voInMap.setPaidAmount(queryResultVO.getPaidAmount());
					voInMap.setOrderCount(queryResultVO.getOrderCount());
					voInMap.setPayCount(queryResultVO.getPayCount());
					voInMap.setOrdSuccRate(queryResultVO.getOrdSuccRate());
				}
            	
			}
            
            //把map中的对象存放到list中
            List<DailyTradeDataVO> tradeList=new ArrayList<>(resultMap.size());
            for (Map.Entry<String, DailyTradeDataVO> entry : resultMap.entrySet()) {
            	
            	DailyTradeDataVO vo=entry.getValue();
            	
            	//要把日期格式转为yyyyMMdd
            	String dataDate=vo.getDataDate();
            	vo.setDataDate(dataDate.replaceAll("-", ""));
            	
            	tradeList.add(vo);
			}
            
            //按日期排序
            Collections.sort(tradeList);
            
            //把所有信息放到map中，转成json
            responseMsg=new HashMap<>();
            responseMsg.put("serviceState", "0000");
            responseMsg.put("serviceMsg", "正常");
            responseMsg.put("shopId", shopId);
            responseMsg.put("itemCount", tradeList.size());
            responseMsg.put("tradeList", tradeList);
            
            resJson=CommonUtils.toJson(responseMsg);
        } catch (Exception e) {
        	log.error("获取店铺每日订单金额统计异常", e);
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
        }
        
        return resJson;
    
	}
	
	/**
	 * 每日新增会员数接口
	 * @param shopId
	 * @param format
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public String dailyMemberAnaly(String certainDateStr,String startDateStr,String endDateStr){

        String resJson = null;
        
        try {
            Map<String,Object> responseMsg=null;
            
            List<String> dateStrList=null;//要统计的日期
            
            //如果certainDate不为空则统计该日期的数据。如果为空则统计时间区间startDate到endDate内每天的数据。
            if (StringUtils.isNotEmpty(certainDateStr)) {
            	
            	try {
            		Timestamp certainDate=DateUtils.getTimestamp(certainDateStr, DateUtils.YYYYMMDD);
            		
            		String dateStr=DateUtils.getDateString(certainDate, DateUtils.YYYY_MM_DD);
            		dateStrList=new ArrayList<>(1);
            		dateStrList.add(dateStr);
				} catch (Exception e) {
					e.printStackTrace();
					return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
				}
				
			}else {
				
				Timestamp startDate=null;
	            Timestamp endDate=null;
				
				if (StringUtils.isNotEmpty(endDateStr)) {
					//结束日期，yyyyMMdd字符串格式，如果空值默认当天
	            	try {
	            		endDate=DateUtils.getTimestamp(endDateStr, DateUtils.YYYYMMDD);
					} catch (Exception e) {
						e.printStackTrace();
						return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
					}
				}else {
					//默认当天
					endDate=DateUtils.getSysDate();
				}
	            
	            if (StringUtils.isNotEmpty(startDateStr)) {
					//开始日期，yyyyMMdd字符串格式，如果空值默认为结束日期七天前
	            	try {
	            		startDate=DateUtils.getTimestamp(startDateStr, DateUtils.YYYYMMDD);
					} catch (Exception e) {
						e.printStackTrace();
						return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
					}
				}else {
					//默认结束日期的七天前
					startDate=DateUtils.getNDays(endDate,-6);//
				}
	            
	            //获取startDate和endDate之间的日期
	            dateStrList=DateUtils.getDates(startDate, endDate, DateUtils.YYYY_MM_DD);
			}
            
            if (dateStrList==null||dateStrList.size()<=0||dateStrList.size()>90) {
            	return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1002","时间超出范围"));
			}
           
            Map<String, DailyMemberAnalyVO> resultMap=new HashMap<>();
            
            StringBuilder datesIn4Query=new StringBuilder();
            for (String dateStr : dateStrList) {
            	datesIn4Query.append("'").append(dateStr).append("',");
            	
            	DailyMemberAnalyVO defaultVO=new DailyMemberAnalyVO();
            	defaultVO.setDataDate(dateStr);
            	
            	resultMap.put(dateStr, defaultVO);
			}
            datesIn4Query.deleteCharAt(datesIn4Query.length()-1);
            
            StringBuilder sqlBuilder=new StringBuilder();
            sqlBuilder.append("SELECT dt as dataDate,new_staff_num as memberCount ");
            sqlBuilder.append(" FROM staff_create  WHERE ");
            sqlBuilder.append("  dt in (").append(datesIn4Query).append(") ");
            
            List<DailyMemberAnalyVO> queryResultList = this.queryList(sqlBuilder.toString(), null, new RowMapper<DailyMemberAnalyVO>() {
    			@Override
    			public DailyMemberAnalyVO mapRow(ResultSet rs, int rowNum) throws SQLException {

    				DailyMemberAnalyVO vo = new DailyMemberAnalyVO();
    				
    				String dataDate=rs.getString("dataDate");
    				
    				if (dataDate==null) {
						return vo;
					}
    				vo.setDataDate(dataDate);
    				
    				int memberCount=rs.getInt("memberCount");//订单总量
    				vo.setMemberCount(memberCount);
    				
    				return vo;
    			}
    		});
            
            for (DailyMemberAnalyVO queryResultVO : queryResultList) {
            	
            	String dataDateInList=queryResultVO.getDataDate();
            	if (dataDateInList==null) {
					continue;
				}
            	
            	DailyMemberAnalyVO voInMap=resultMap.get(dataDateInList);
            	if (voInMap==null) {
            		resultMap.put(queryResultVO.getDataDate(), queryResultVO);
				}else {
					voInMap.setMemberCount(queryResultVO.getMemberCount());
				}
            	
			}
            
            //把map中的对象存放到list中
            List<DailyMemberAnalyVO> tradeList=new ArrayList<>(resultMap.size());
            for (Map.Entry<String, DailyMemberAnalyVO> entry : resultMap.entrySet()) {
            	
            	DailyMemberAnalyVO vo=entry.getValue();
            	
            	//要把日期格式转为yyyyMMdd
            	String dataDate=vo.getDataDate();
            	vo.setDataDate(dataDate.replaceAll("-", ""));
            	
            	tradeList.add(vo);
			}
            
            //按日期排序
            Collections.sort(tradeList);
            
            //把所有信息放到map中，转成json
            responseMsg=new HashMap<>();
            responseMsg.put("serviceState", "0000");
            responseMsg.put("serviceMsg", "正常");
            responseMsg.put("itemCount", tradeList.size());
            responseMsg.put("dataList", tradeList);
            
            resJson=CommonUtils.toJson(responseMsg);
        } catch (Exception e) {
        	log.error("获取每日新增会员数接口异常", e);
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
        }
        
        return resJson;
    
	
	}
}
