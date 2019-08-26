package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.FlowMapService;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowExitPageVo;
import com.ai.analy.vo.flow.FlowMapOverviewVo;
import com.ai.analy.vo.flow.FlowMapRefDomainVo;
import com.ai.analy.vo.flow.FlowMapVo;
import com.ai.analy.vo.flow.FlowNextVo;
import com.ai.analy.vo.flow.FlowSourceOrderVo;
import com.ai.analy.vo.flow.FlowSourceVo;
import com.ai.analy.vo.prom.PromInfoVo;
import com.alibaba.dubbo.common.utils.StringUtils;

public class FlowMapServiceImpl extends BaseDao implements FlowMapService {

	/**
	 * 流量地图区域查询
	 *
	 * @author huangcm
	 * @date 2015-10-15
	 * @param param
	 * @return
	 */
	public List<FlowMapVo> queryFlowMap(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select t.province,tba.area_name provinceName, sum(t.pv) pv, sum(t.uv) uv , sum(t.uv)/(select sum(a.uv) from flow_user_region as a where 1=1");
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
		sql.append(
				" ) scale from flow_user_region t left join t_base_area_admin tba on t.province = tba.area_code where 1=1");
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
		sql.append("  group by t.province  order by uv desc");

		// 按省份分组
		List<FlowMapVo> list = queryList(sql.toString(), params, new FMapRowMapper());

		return list;
	}

	protected class FMapRowMapper implements RowMapper<FlowMapVo> {
		public FlowMapVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			FlowMapVo fMap = new FlowMapVo();
			fMap.setProvinceCode(rs.getString("province"));
			if (StringUtils.isEmpty(rs.getString("provinceName"))) {
				fMap.setProvinceName("未知");
			} else {
				fMap.setProvinceName(rs.getString("provinceName"));
			}
			fMap.setPv(rs.getInt("pv"));
			fMap.setUv(rs.getInt("uv"));
			fMap.setScale(rs.getDouble("scale"));
			return fMap;
		}

	}

	/**
	 * 流量来源查询
	 *
	 * @author huangcm
	 * @date 2015-10-16
	 * @param param
	 * @return
	 */
	public PageBean<FlowSourceVo> queryFlowSource(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}

		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select t.refer_page, sum(t.uv) suv, (sum(t.uv)/(select sum(a.uv) suv from flow_map_path_src a where 1=1");
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
		if (StringUtils.isNotEmpty(param.getTarget())) {
			sql.append(" and a.page_name = ?");
			// params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
			params.add(param.getTarget());
		}
		sql.append(" )) scale from flow_map_path_src t where 1=1");
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
		if (StringUtils.isNotEmpty(param.getTarget())) {
			sql.append(" and t.page_name = ?");
			// params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
			params.add(param.getTarget());
		}
		sql.append(" group by t.refer_page order by suv desc");

		int count = queryCount(sql.toString(), params);

		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		List<FlowSourceVo> list = queryList(sql.toString(), params, new FlowSourceMapper());

		PageBean<FlowSourceVo> pageBean = new PageBean<FlowSourceVo>(param.getPageNo(), param.getPageSize(), list,
				count);

		return pageBean;
	}

	protected class FlowSourceMapper implements RowMapper<FlowSourceVo> {
		public FlowSourceVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			FlowSourceVo fl = new FlowSourceVo();
			fl.setPageName(rs.getString("refer_page"));
			fl.setUv(rs.getInt("suv"));
			fl.setScale(rs.getDouble("scale"));
			return fl;
		}
	}

	/**
	 * 流量去向查询
	 *
	 * @author huangcm
	 * @date 2015-10-16
	 * @return
	 */
	public PageBean<FlowNextVo> queryFlowNext(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select t.next_page, sum(t.uv) suv, (sum(t.uv)/(select sum(a.uv) suv from flow_map_path_next a where 1=1");
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
		if (StringUtils.isNotEmpty(param.getTarget())) {
			sql.append(" and a.page_name = ?");
			// params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
			params.add(param.getTarget());
		}

		sql.append(" )) scale from flow_map_path_next t where 1=1");
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
		if (StringUtils.isNotEmpty(param.getTarget())) {
			sql.append(" and t.page_name = ?");
			// params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
			params.add(param.getTarget());
		}
		sql.append(" group by t.next_page order by suv desc");

		int count = queryCount(sql.toString(), params);

		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		List<FlowNextVo> list = queryList(sql.toString(), params, new FlowNextMapper());

		PageBean<FlowNextVo> pageBean = new PageBean<FlowNextVo>(param.getPageNo(), param.getPageSize(), list, count);

		return pageBean;
	}

	protected class FlowNextMapper implements RowMapper<FlowNextVo> {
		public FlowNextVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			FlowNextVo fl = new FlowNextVo();
			fl.setNextPageName(rs.getString("next_page"));
			fl.setUv(rs.getInt("suv"));
			fl.setScale(rs.getDouble("scale"));
			return fl;
		}
	}

	/**
	 * 查询总店铺或商品详情UV
	 *
	 * @author huangcm
	 * @date 2015-10-16
	 * @param param
	 * @return
	 */
	public Integer getSumUV(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return 0;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(t.uv) suv from flow_map_path_src t where 1=1");
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
		if (StringUtils.isNotEmpty(param.getTarget())) {
			sql.append(" and t.page_name = ?");
			// params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
			params.add(param.getTarget());
		}
		sql.append(" group by t.page_name");

		List<Integer> list = queryList(sql.toString(), params, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs == null ? 0 : rs.getInt("suv");
			}
		});

		return (list == null || list.size() == 0) ? 0 : list.get(0);
	}

	/**
	 * 查询总店铺页或商品详情页流量概况
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowMapOverviewVo> queryFlowMapOverview(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select t.page_name, sum(t.uv) suv, sum(t.pv) spv, (sum(t.uv)/(select sum(a.uv) suv from flow_map_overview a where 1=1");
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
		sql.append(" )) percent from flow_map_overview t where 1=1");
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
		sql.append(" group by t.page_name");

		List<FlowMapOverviewVo> list = queryList(sql.toString(), params, new RowMapper<FlowMapOverviewVo>() {
			public FlowMapOverviewVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FlowMapOverviewVo fMap = new FlowMapOverviewVo();
				fMap.setPercent(rs.getDouble("percent"));
				fMap.setUv(rs.getInt("suv"));
				fMap.setPv(rs.getInt("spv"));
				fMap.setPageName(rs.getString("page_name"));
				return fMap;
			}
		});

		return list;
	}

	/**
	 * 离开页面去向查询
	 *
	 * @author huangcm
	 * @date 2015-10-19
	 * @param param
	 * @return
	 */
	public PageBean<FlowExitPageVo> queryExitPage(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		// sql.append("select t.post_exit_page, sum(t.uv) suv, sum(t.pv) spv,
		// (sum(t.uv)/(select sum(a.uv) suv from flow_map_postexit a where
		// 1=1");
		sql.append(
				"select t.page_url, sum(t.pv) spv, sum(t.exit_pv) sexitpv,sum(t.exit_pv)/ sum(t.pv) percent from flow_map_url t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (!StringUtils.isEmpty(param.getUrl())) {
			sql.append(" and t.page_url = ?");
			params.add(param.getUrl());
		}
		// if (StringUtils.isNotEmpty(param.getTarget())) {
		// sql.append(" and t.exit_page_name = ?");
		// //params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
		// params.add(param.getTarget());
		// }
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" group by t.page_url  order by spv desc");

		int count = queryCount(sql.toString(), params);

		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		List<FlowExitPageVo> list = queryList(sql.toString(), params, new FlowExitPageMapper());

		PageBean<FlowExitPageVo> pageBean = new PageBean<FlowExitPageVo>(param.getPageNo(), param.getPageSize(), list,
				count);

		return pageBean;
	}

	protected class FlowExitPageMapper implements RowMapper<FlowExitPageVo> {
		public FlowExitPageVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			FlowExitPageVo flExitPage = new FlowExitPageVo();
			flExitPage.setPageUrl(rs.getString("page_url"));
			flExitPage.setSpv(rs.getInt("spv"));
			flExitPage.setSexitpv(rs.getInt("sexitpv"));
			flExitPage.setPercent(rs.getDouble("percent"));
			return flExitPage;
		}

	}

	/**
	 * 流量来源下单转化率情况
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-23
	 * @param param
	 * @return
	 */
	public PageBean<FlowSourceOrderVo> queryFlowSourceOrder(QueryParamsVo param) {
		/**
		 * select t.refer_page, t.page_name, sum(t.uv) suv, sum(t.order_uv)
		 * sorder_uv, sum(t.order_uv)/sum(t.uv) orderRate from flow_map_src t
		 * where 1=1 and shop_id = ? and t.client_type = ? and t.dt>=? and
		 * t.dt<=? group by t.refer_page
		 */
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select t.refer_page,t.page_name, sum(t.uv) suv, sum(t.order_uv) sorder_uv,  sum(t.order_uv)/sum(t.uv) orderRate "
						+ "	from flow_map_src t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (StringUtils.isNotEmpty(param.getTarget())) {
			sql.append(" and t.page_name = ?");
			// params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
			params.add(param.getTarget());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" group by t.refer_page order by suv desc");

		int count = queryCount(sql.toString(), params);

		if (param.getPageNo() != null && param.getPageSize() != null && param.getPageNo() != 0
				&& param.getPageSize() != 0) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		List<FlowSourceOrderVo> rList = queryList(sql.toString(), params, new RowMapper<FlowSourceOrderVo>() {
			public FlowSourceOrderVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FlowSourceOrderVo obj = new FlowSourceOrderVo();
				obj.setReferPage(rs.getString("refer_page"));
				obj.setPageName(rs.getString("page_name"));
				obj.setUv(rs.getInt("suv"));
				obj.setOrderUv((rs.getInt("sorder_uv")));
				obj.setOrderRate(rs.getDouble("orderRate"));
				return obj;
			}
		});

		if (rList.size() == 0) {
			param.setPageSize(10);
		}

		PageBean<FlowSourceOrderVo> pageBean = new PageBean<FlowSourceOrderVo>(param.getPageNo(),
				param.getPageSize() == null ? rList.size() : param.getPageSize(), rList, count);

		return pageBean;
	}

	/**
	 * 流量来源下单转化率情况以及环比
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-23
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageBean<FlowSourceOrderVo> getFlowSourceOrderMom(QueryParamsVo param) {
		// 本期数据
		List<FlowSourceOrderVo> currentlist = null;
		PageBean<FlowSourceOrderVo> pageBean = queryFlowSourceOrder(param);
		if (pageBean != null) {
			currentlist = (List<FlowSourceOrderVo>) pageBean.getRecordList();
		}
		// 上期数据
		param.setDateFrom(param.getPreDateFrom());
		param.setDateTo(param.getPreDateTo());
		List<FlowSourceOrderVo> lastlist = null;
		param.setPageSize(null);
		PageBean<FlowSourceOrderVo> pageBean1 = queryFlowSourceOrder(param);
		if (pageBean1 != null) {
			lastlist = (List<FlowSourceOrderVo>) pageBean1.getRecordList();
		}
		// 计算环比率 : 环比增长率 = (本期数－上期数)/上期数x100%
		if (currentlist != null && currentlist.size() > 0) {
			for (FlowSourceOrderVo currObj : currentlist) {
				if (lastlist != null && lastlist.size() > 0) {
					for (FlowSourceOrderVo lastObj : lastlist) {
						if (currObj.getReferPage().equals(lastObj.getReferPage())) {
							double uvMom = 0.0;
							if (lastObj.getUv() != 0) {
								uvMom = (currObj.getUv() - lastObj.getUv()) / (double) lastObj.getUv();
							}
							currObj.setUvMom(uvMom);

							double orderUvMom = 0.0;
							if (lastObj.getOrderUv() != 0) {
								orderUvMom = (currObj.getOrderUv() - lastObj.getOrderUv())
										/ (double) lastObj.getOrderUv();
							}
							currObj.setOrderUvMom(orderUvMom);

							double orderRateMom = 0.0;
							if (lastObj.getOrderRate() != 0) {
								orderRateMom = (currObj.getOrderRate() - lastObj.getOrderRate())
										/ (double) lastObj.getOrderRate();
							}
							currObj.setOrderRateMom(orderRateMom);
							break;
						}
					}
				}
			}
		}

		pageBean.setRecordList(currentlist);

		return pageBean;
	}

	/**
	 * 查询统计界面
	 * 
	 * @return
	 */
	public List<com.ai.analy.vo.comm.PageInfoVo> getPageInfo(QueryParamsVo param) {

		String sql = "SELECT t.url pageCode, t.name pageName " + " FROM t_url_stat_def t " + " WHERE t.enable = 'true' "
				+ " ORDER BY t.display_order ASC";

		List<Object> params = new ArrayList<Object>();

		return this.queryList(sql, params, new RowMapper<com.ai.analy.vo.comm.PageInfoVo>() {
			@Override
			public com.ai.analy.vo.comm.PageInfoVo mapRow(ResultSet rs, int rowNum) throws SQLException {

				com.ai.analy.vo.comm.PageInfoVo vo = new com.ai.analy.vo.comm.PageInfoVo();

				vo.setPageCode(rs.getString("pageCode"));
				vo.setPageName(rs.getString("pageName"));

				return vo;
			}
		});
	}

	@Override
	public PageBean<FlowMapRefDomainVo> queryFlowRefDomain(QueryParamsVo param) {
		// 本期数据
		List<FlowMapRefDomainVo> currentlist = null;
		PageBean<FlowMapRefDomainVo> pageBean = queryFlowRefDomainPageBean(param);
		if (pageBean != null) {
			currentlist = (List<FlowMapRefDomainVo>) pageBean.getRecordList();
		}
		// 上期数据
		param.setDateFrom(param.getPreDateFrom());
		param.setDateTo(param.getPreDateTo());
		List<FlowMapRefDomainVo> lastlist = null;
		param.setPageSize(null);
		PageBean<FlowMapRefDomainVo> pageBean1 = queryFlowRefDomainPageBean(param);
		if (pageBean1 != null) {
			lastlist = (List<FlowMapRefDomainVo>) pageBean1.getRecordList();
		}
		// 计算环比率 : 环比增长率 = (本期数－上期数)/上期数x100%
		if (currentlist != null && currentlist.size() > 0) {
			for (FlowMapRefDomainVo currObj : currentlist) {
				if (lastlist != null && lastlist.size() > 0) {
					for (FlowMapRefDomainVo lastObj : lastlist) {
						if (currObj.getReferDomain().equals(lastObj.getReferDomain())) {
							double uvScale = 0.0;
							if (lastObj.getUv() != 0) {
								uvScale = (currObj.getUv() - lastObj.getUv()) / (double) lastObj.getUv();
							}
							currObj.setUvScale(uvScale);
							
							double pvScale = 0.0;
							if (lastObj.getUv() != 0) {
								pvScale = (currObj.getPv() - lastObj.getPv()) / (double) lastObj.getPv();
							}
							currObj.setPvScale(pvScale);

							break;
						}
					}
				}
			}
		}

		pageBean.setRecordList(currentlist);

		return pageBean;
	}
	
	private PageBean<FlowMapRefDomainVo> queryFlowRefDomainPageBean(QueryParamsVo param){
		if (param == null) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select t.refer_domain, sum(t.uv) uv, sum(t.pv) pv from flow_src_all t where 1=1");
		
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
		sql.append(" group by t.refer_domain order by uv desc");

		int count = queryCount(sql.toString(), params);

		if (param.getPageNo() != null && param.getPageSize() != null && param.getPageNo() != 0
				&& param.getPageSize() != 0) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}

		
		List<FlowMapRefDomainVo> rList = queryList(sql.toString(), params, new BeanPropertyRowMapper<FlowMapRefDomainVo>(FlowMapRefDomainVo.class));
		
		if (rList.size() == 0) {
			param.setPageSize(10);
		}
		
		PageBean<FlowMapRefDomainVo> pageBean = new PageBean<FlowMapRefDomainVo>(param.getPageNo(),param.getPageSize() == null ? rList.size() : param.getPageSize(), rList, count);

		return pageBean;
	}
}
