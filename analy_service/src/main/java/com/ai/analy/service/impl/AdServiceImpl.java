package com.ai.analy.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.AdService;
import com.ai.analy.vo.ad.AdIndexDateSumVo;
import com.ai.analy.vo.ad.AdIndexOverviewVo;
import com.ai.analy.vo.ad.AdIndexTrendVo;
import com.ai.analy.vo.ad.AdInfoVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 广告分析服务接口实现
 * 
 * @author limb
 * @date 2016年9月5日
 */
public class AdServiceImpl extends BaseDao implements AdService {
	@Autowired
	JdbcTemplate jdbc;

	/**
	 * 查询广告列表
	 */
	@Override
	public PageBean<AdInfoVo> queryAdList(QueryParamsVo paramsVo) {
		if (paramsVo == null) {
			return null;
		}

		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(
				"select a.id, a.advertise_title as advertiseTitle, a.vfs_id as vfsId, a.pub_time as pubTime, a.lost_time as lostTime, a.link_type as linkType, a.status");
		sql.append(" ,b.site_name as siteName, c.template_name as templateName, d.place_name as placeName");
		sql.append(" from t_cms_advertise as a");
		sql.append(" left join t_cms_site as b on b.id = a.site_id");
		sql.append(" left join t_cms_template as c on c.id = a.template_id");
		sql.append(" left join t_cms_place as d on d.id = a.place_id");
		sql.append(" where 1=1");

		if (StringUtils.isNotEmpty(paramsVo.getSiteId())) {
			sql.append(" and a.site_id = ?");
			params.add(paramsVo.getSiteId());
		}

		if (StringUtils.isNotEmpty(paramsVo.getTemplateId())) {
			sql.append(" and a.template_id = ?");
			params.add(paramsVo.getTemplateId());
		}

		if (StringUtils.isNotEmpty(paramsVo.getPlaceId())) {
			sql.append(" and a.place_id = ?");
			params.add(paramsVo.getPlaceId());
		}

		if (StringUtils.isNotEmpty(paramsVo.getAdTitle())) {
			sql.append(" and a.advertise_title like ?");
			params.add("%" + paramsVo.getAdTitle() + "%");
		}

		if (StringUtils.isNotEmpty(paramsVo.getParamShopId())) {
			sql.append(" and a.shop_id = ?");
			params.add(paramsVo.getParamShopId());
		}

		if (StringUtils.isNotEmpty(paramsVo.getStatus())) {
			sql.append(" and a.status = ?");
			params.add(paramsVo.getStatus());
		}

		if (StringUtils.isNotEmpty(paramsVo.getFirstDateFrom())) {
			sql.append(" and a.pub_time >= ?");
			params.add(paramsVo.getFirstDateFrom());
		}

		if (StringUtils.isNotEmpty(paramsVo.getFirstDateTo())) {
			sql.append(" and a.pub_time <= ?");
			params.add(paramsVo.getFirstDateTo());
		}

		if (StringUtils.isNotEmpty(paramsVo.getSecondDateFrom())) {
			sql.append(" and a.lost_time >= ?");
			params.add(paramsVo.getSecondDateFrom());
		}

		if (StringUtils.isNotEmpty(paramsVo.getSecondDateTo())) {
			sql.append(" and a.lost_time <= ?");
			params.add(paramsVo.getSecondDateTo());
		}

		int count = 0;
		if (paramsVo.getPageNo() != null && paramsVo.getPageNo() != 0 && paramsVo.getPageSize() != null) {
			count = queryCount(sql.toString(), params);

			sql.append(" order by a.id desc");
			sql.append(" limit ?, ?");
			params.add((paramsVo.getPageNo() - 1) * paramsVo.getPageSize());
			params.add(paramsVo.getPageSize());
		}

		List<AdInfoVo> list = queryList(sql.toString(), params, new BeanPropertyRowMapper<AdInfoVo>(AdInfoVo.class));

		PageBean<AdInfoVo> pageBean = new PageBean<AdInfoVo>(paramsVo.getPageNo(), paramsVo.getPageSize(), list, count);

		return pageBean;
	}

	/**
	 * 查询广告指标概览
	 */
	@Override
	public AdIndexOverviewVo getAdIndexOverview(QueryParamsVo paramsVo) {
		List<Object> params1 = new ArrayList<Object>();
		List<Object> params2 = new ArrayList<Object>();

		StringBuilder sql = new StringBuilder();
		sql.append(
				"select sum(t.pv) pv, sum(t.uv) uv,  sum(t.order_uv)/sum(t.uv) as orderUvRate, sum(t.pay_uv)/sum(t.order_uv) as payUvRate from ad_overview t where 1=1");

		sql.append(" and t.ad_id = ?");
		params1.add(paramsVo.getAdId());
		params2.add(paramsVo.getAdId());

		if (paramsVo.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params1.add(paramsVo.getSource());
			params2.add(paramsVo.getSource());
		}

		if (paramsVo.getDateFrom() != null && paramsVo.getDateTo() != null) {
			sql.append(" and t.dt >= ?");
			params1.add(paramsVo.getDateFrom());
			params2.add(paramsVo.getPreDateFrom());

			sql.append(" and t.dt <= ?");
			params1.add(paramsVo.getDateTo());
			params2.add(paramsVo.getPreDateTo());
		}

		AdIndexOverviewVo indexOverviewVo = new AdIndexOverviewVo();
		try {
			indexOverviewVo = queryOne(sql.toString(), params1,
					new BeanPropertyRowMapper<AdIndexOverviewVo>(AdIndexOverviewVo.class));
			if (indexOverviewVo != null) {

				AdIndexOverviewVo preIndexOverviewVo = queryOne(sql.toString(), params2,
						new BeanPropertyRowMapper<AdIndexOverviewVo>(AdIndexOverviewVo.class));
				if (preIndexOverviewVo != null) {
					int pv = preIndexOverviewVo.getPv();
					indexOverviewVo.setPvCompare((indexOverviewVo.getPv() - pv) / pv);
					float uv = preIndexOverviewVo.getUv();
					indexOverviewVo.setUvCompare((indexOverviewVo.getUv() - uv) / uv);
					double orderUvRate = preIndexOverviewVo.getOrderUvRate();
					indexOverviewVo
							.setOrderUvRateCompare((indexOverviewVo.getOrderUvRate() - orderUvRate) / orderUvRate);
					double payUvRate = preIndexOverviewVo.getPayUvRate();
					indexOverviewVo.setPayUvRateCompare((indexOverviewVo.getPayUvRate() - payUvRate) / payUvRate);
				}
			}

		} catch (Exception e) {
			// queryOne查询无记录异常处理
			// e.printStackTrace();
		}

		return indexOverviewVo;
	}

	/**
	 * 查询广告指标趋势
	 */
	@Override
	public AdIndexTrendVo getAdIndexTrend(QueryParamsVo paramsVo) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(
				"select t.dt, sum(t.pv) pv, sum(t.uv) uv,  sum(t.order_uv)/sum(t.uv) as orderUvRate, sum(t.pay_uv)/sum(t.order_uv) as payUvRate from ad_overview t where 1=1");

		sqlBuilder.append(" and t.ad_id = ?");
		params.add(paramsVo.getAdId());

		if (paramsVo.getSource() != 0) {
			sqlBuilder.append(" and t.client_type = ?");
			params.add(paramsVo.getSource());
		}

		if (paramsVo.getDateFrom() != null && paramsVo.getDateTo() != null) {
			sqlBuilder.append(" and t.dt >= ?");
			params.add(paramsVo.getDateFrom());

			sqlBuilder.append(" and t.dt <= ?");
			params.add(paramsVo.getDateTo());
		}

		sqlBuilder.append(" group by t.dt");

		AdIndexTrendVo trendVo = new AdIndexTrendVo();
		Map<String, Tmp> tmpMap = new HashMap<String, Tmp>();

		String sql = sqlBuilder.toString();
		SqlRowSet rowSet = jdbc.queryForRowSet(sql, params.toArray());
		while (rowSet.next()) {
			Tmp tmp = new Tmp(rowSet.getInt("pv"), rowSet.getInt("uv"), rowSet.getDouble("orderUvRate"),
					rowSet.getDouble("payUvRate"));
			tmpMap.put(rowSet.getString("dt"), tmp);
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0;; i++) {
			Date now = new Date(paramsVo.getDateFrom().getTime() + i * 86400000L);
			if (now.after(paramsVo.getDateTo())) {
				break;
			}
			String dt = df.format(now);
			trendVo.getxAxis().add(dt);
			Tmp tmp = tmpMap.get(dt);
			if (tmp == null) {
				trendVo.getPvs().add(0);
				trendVo.getUvs().add(0);
				trendVo.getOrderUvRates().add(0.0);
				trendVo.getPayUvRates().add(0.0);
			} else {
				trendVo.getPvs().add(tmp.pv);
				trendVo.getUvs().add(tmp.uv);
				trendVo.getOrderUvRates().add(tmp.orderUvRate);
				trendVo.getPayUvRates().add(tmp.payUvRate);
			}
		}

		return trendVo;
	}

	/**
	 * 查询广告指标日期汇总列表
	 */
	@Override
	public PageBean<AdIndexDateSumVo> getAdIndexDateList(QueryParamsVo paramsVo) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(
				"select t.dt, sum(t.pv) pv, sum(t.uv) uv,  sum(t.order_uv)/sum(t.uv) as orderUvRate, sum(t.pay_uv)/sum(t.order_uv) as payUvRate from ad_overview t where 1=1");

		sqlBuilder.append(" and t.ad_id = ?");
		params.add(paramsVo.getAdId());
		if (paramsVo.getSource() != 0) {
			sqlBuilder.append(" and t.client_type = ?");
			params.add(paramsVo.getSource());
		}
		if (paramsVo.getDateFrom() != null && paramsVo.getDateTo() != null) {
			sqlBuilder.append(" and t.dt >= ?");
			params.add(paramsVo.getDateFrom());

			sqlBuilder.append(" and t.dt <= ?");
			params.add(paramsVo.getDateTo());
		}
		sqlBuilder.append(" group by t.dt");

		List<AdIndexDateSumVo> recordList = new ArrayList<AdIndexDateSumVo>();
		int recordCount = 0;
		AdIndexDateSumVo tempRecord;

		// 24小时趋势
		if (paramsVo.getDateFrom().equals(paramsVo.getDateTo())) {
			// goods_detail表无hour字段，暂不支持24小时趋势

		} else {// 天趋势
			Map<String, Tmp> tmpMap = new HashMap<String, Tmp>();
			SqlRowSet rowSet = jdbc.queryForRowSet(sqlBuilder.toString(), params.toArray());
			while (rowSet.next()) {
				Tmp tmp = new Tmp(rowSet.getInt("pv"), rowSet.getInt("uv"), rowSet.getDouble("orderUvRate"),
						rowSet.getDouble("payUvRate"));
				tmpMap.put(rowSet.getString("dt"), tmp);
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0;; i++) {
				Date now = new Date(paramsVo.getDateFrom().getTime() + i * 86400000L);
				if (now.after(paramsVo.getDateTo())) {
					// 记录总数
					recordCount = i;
					break;
				}
				String dt = df.format(now);
				tempRecord = new AdIndexDateSumVo();
				tempRecord.setDt(dt);
				Tmp tmp = tmpMap.get(dt);
				if (tmp == null) {
					tempRecord.setPv(0);
					tempRecord.setUv(0);
					tempRecord.setOrderUvRate(0);
					tempRecord.setPayUvRate(0);

				} else {
					tempRecord.setPv(tmp.pv);
					tempRecord.setUv(tmp.uv);
					tempRecord.setOrderUvRate(tmp.orderUvRate);
					tempRecord.setPayUvRate(tmp.payUvRate);
				}

				recordList.add(tempRecord);
			}
		}
		int toIndex = paramsVo.getPageNo() * paramsVo.getPageSize() > recordCount ? recordCount
				: paramsVo.getPageNo() * paramsVo.getPageSize();
		List<AdIndexDateSumVo> currentPageRecordList = recordList
				.subList((paramsVo.getPageNo() - 1) * paramsVo.getPageSize(), toIndex);
		PageBean<AdIndexDateSumVo> pageBean = new PageBean<AdIndexDateSumVo>(paramsVo.getPageNo(),
				paramsVo.getPageSize(), currentPageRecordList, recordCount);

		return pageBean;
	}

	class Tmp {
		int pv;
		int uv;
		double orderUvRate;
		double payUvRate;

		public Tmp(int pv, int uv, double orderUvRate, double payUvRate) {
			this.pv = pv;
			this.uv = uv;
			this.orderUvRate = orderUvRate;
			this.payUvRate = payUvRate;
		}
	}
}
