package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.FlowService;
import com.ai.analy.service.interfaces.SearchRankingService;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowOverviewVo;
import com.ai.analy.vo.search.FlowSearchOverviewVo;
import com.ai.analy.vo.search.SearchRankingShopVo;
import com.ai.analy.vo.search.SearchRankingVo;
import com.alibaba.dubbo.common.utils.StringUtils;

public class SearchRankingServiceImpl extends BaseDao implements SearchRankingService {
	
	@Autowired
	private FlowService flowService;

	/**
	 * 平台热词
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	public PageBean<SearchRankingVo> queryHotKeyWords(QueryParamsVo param) {
		/**
		select tb.keywords , sum(tb.pv) searchNum , sum(tb.pv)/(select sum(t0.pv) from flow_se_global t0 where 1=1
				and t0.dt>='?' and t0.dt<='?'
				and tb.client_type = '?'
			)  searchNumPercent
			from flow_se_global tb where 1=1
				and tb.dt>='?' and tb.dt<='?'
				and tb.client_type = '1'
			group by  keywords  order by searchNum
		*/
		
		if (param == null) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select tb.keywords , sum(tb.pv) searchNum , sum(tb.pv)/(select sum(t0.pv) from flow_se_global t0 where 1=1");
		if (param.getSource() != 0) {
			sql.append(" and t0.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t0.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t0.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" )  searchNumPercent");
		sql.append(" from flow_se_global tb where 1=1");
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
		sql.append(" group by  keywords  order by searchNum desc");
		
		int recordCount = queryCount(sql.toString(), params);
		
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<SearchRankingVo> recordList = queryList(sql.toString(), params, new SearchRankingRowMapper());
		
		if (recordList.size() == 0 ) {
			param.setPageSize(10);
		}
		
		PageBean<SearchRankingVo>  pageBean = new PageBean<SearchRankingVo>(param.getPageNo(), param.getPageSize() == null ? recordList.size() : param.getPageSize(), recordList, recordCount);
		
		return pageBean;
	}

	/**
	 * 平台热词(含环比)
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageBean<SearchRankingVo> getHotKeyWords(QueryParamsVo param) {
		if (param == null) {
			return null;
		}
		
		// 本期
		PageBean<SearchRankingVo>  currPageBean = queryHotKeyWords(param);
		
		// 上期
		param.setDateFrom(param.getPreDateFrom());
		param.setDateTo(param.getPreDateTo());
		// 不分页
		param.setPageSize(null);
		PageBean<SearchRankingVo>  lastPageBean = queryHotKeyWords(param);
		
		List<SearchRankingVo> resultList = new ArrayList<SearchRankingVo>();
		
		// 计算环比
		if (currPageBean != null && lastPageBean != null 
				&& currPageBean.getRecordList() != null && currPageBean.getRecordList().size() > 0
				&& lastPageBean.getRecordList() != null && lastPageBean.getRecordList().size() > 0) {
			List<SearchRankingVo> currList = (List<SearchRankingVo>) currPageBean.getRecordList();
			List<SearchRankingVo> lastList = (List<SearchRankingVo>) lastPageBean.getRecordList();
			for (SearchRankingVo currSr : currList) {
				for (SearchRankingVo lastSr : lastList) {
					if (currSr.getKeyWords().equals(lastSr.getKeyWords())) {
						double searchNumMom = (currSr.getSearchNum() - lastSr.getSearchNum())/(double) lastSr.getSearchNum();
						currSr.setSearchNumMom(searchNumMom);
						break;
					}
				}
				resultList.add(currSr);
			}
		}
		
		// 是否店铺展现
		param.setPageSize(10000);
		param.setPageNo(1);
		PageBean<SearchRankingShopVo> pb = getHotKeyWordsShop(param);
		List<SearchRankingShopVo> srShopList = null;
		if (pb != null) {
			srShopList = (List<SearchRankingShopVo>) pb.getRecordList();
		}
		if (resultList != null && resultList.size() > 0) {
			for (SearchRankingVo srVo : resultList) {
				if (srShopList != null && srShopList.size() > 0) {
					for (SearchRankingShopVo srShopVo : srShopList) {
						if (srVo.getKeyWords().equals(srShopVo.getKeywords())) {
							srVo.setIsShowInShop(1);
							break;
						}
					}
				}
			}
		}
		
		if (resultList.size() > 0) {
			currPageBean.setRecordList(resultList);
		}
		
		
		return currPageBean;
		
	}
	
	
	protected class SearchRankingRowMapper implements RowMapper<SearchRankingVo> {
		public SearchRankingVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			SearchRankingVo searchRankingVo =new SearchRankingVo();
			searchRankingVo.setKeyWords(rs.getString("keywords"));
			searchRankingVo.setSearchNum(rs.getLong("searchNum"));
			searchRankingVo.setSearchNumPercent(rs.getDouble("searchNumPercent"));
			return searchRankingVo;
		}
	}


	/**
	 * 店铺热搜词
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	public PageBean<SearchRankingShopVo> getHotKeyWordsShop(QueryParamsVo param) {
		/**
		select tb.keywords, sum(tb.pv) pv, sum(tb.uv) uv , sum(tb.order_uv) orderUv, sum(tb.order_uv)/sum(tb.uv) orderUvRate, sum(pay_amount) tradeMoney 
			from flow_se_detail tb where 1=1
				and tb.shop_id=?
				and tb.client_type = ?
				and tb.dt>=? and tb.dt<=?
			group by tb.keywords order by pv desc
			limit ?, ?
		*/
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select tb.keywords, sum(tb.pv) pv, sum(tb.uv) uv , sum(tb.order_uv) orderUv, sum(tb.order_uv)/sum(tb.uv) orderUvRate, sum(pay_amount) tradeMoney from flow_se_detail tb where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and tb.shop_id=?");
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
		sql.append(" group by tb.keywords order by pv desc");
		
		int recordCount = queryCount(sql.toString(), params);
		
		if (param.getPageNo() != null && param.getPageSize() != null) {
			sql.append(" limit ?, ?");
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		List<SearchRankingShopVo> recordList = queryList(sql.toString(), params, new SearchRankingShopRowMapper());
		
		PageBean<SearchRankingShopVo> pageBean = new PageBean<SearchRankingShopVo>(param.getPageNo(), param.getPageSize(), recordList, recordCount);
		
		return pageBean;
	}
	
	protected class SearchRankingShopRowMapper implements RowMapper<SearchRankingShopVo> {
		public SearchRankingShopVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			SearchRankingShopVo searchRankingShopVo = new SearchRankingShopVo();
			searchRankingShopVo.setKeywords(rs.getString("keywords"));
			searchRankingShopVo.setOrderUv(rs.getLong("orderUv"));
			searchRankingShopVo.setUv(rs.getLong("uv"));
			searchRankingShopVo.setOrderUvRate(rs.getDouble("orderUvRate"));
			searchRankingShopVo.setPv(rs.getLong("pv"));
			searchRankingShopVo.setTradeMoney(rs.getLong("tradeMoney"));
			return searchRankingShopVo;
		}
	}

	/**
	 * 店铺搜索流量总览
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	public FlowSearchOverviewVo getFlowSearchOverviw(QueryParamsVo param) {
		/**
		select sum(tb.pv) pv, sum(tb.uv) uv, sum(tb.order_uv) orderUv, sum(tb.order_uv)/sum(tb.uv) orderUvRate , sum(tb.keys_num) keyWordsNum
			from flow_se_overview tb where 1=1
				and tb.shop_id=?
				and tb.client_type =?
				and tb.dt>=? and tb.dt<=?
		 */
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(tb.pv) pv, sum(tb.uv) uv, sum(tb.order_uv) orderUv, sum(tb.order_uv)/sum(tb.uv) orderUvRate , sum(tb.keys_num) keyWordsNum");
		sql.append(" 	from flow_se_overview tb where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and tb.shop_id=?");
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
		
		FlowSearchOverviewVo flowSearchOverviewVo = queryOne(sql.toString(), params, new FlowSearchOverviewRowMapper());
		
		FlowOverviewVo flowOverviewVo = flowService.getFlowOverviewAll(param);
		
		double uvPercent = flowSearchOverviewVo.getUv()/(double)flowOverviewVo.getUv();
		flowSearchOverviewVo.setUvPercent(uvPercent);
		
		return flowSearchOverviewVo;
	}
	
	protected class FlowSearchOverviewRowMapper implements RowMapper<FlowSearchOverviewVo> {
		public FlowSearchOverviewVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			FlowSearchOverviewVo obj = new FlowSearchOverviewVo();
			obj.setKeyWordsNum(rs.getLong("keyWordsNum"));
			obj.setOrderUv(rs.getLong("orderUv"));
			obj.setOrderUvRate(rs.getDouble("orderUvRate"));
			obj.setUv(rs.getLong("uv"));
			return obj;
		}
	}
	

}
