package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.constants.Constants;
import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.FlowMapService;
import com.ai.analy.service.interfaces.VisitorStructureService;
import com.ai.analy.utils.DateUtils;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowDateInterpretVo;
import com.ai.analy.vo.flow.FlowMapVo;
import com.ai.analy.vo.flow.FlowNextVo;
import com.ai.analy.vo.flow.FlowSourceVo;
import com.ai.analy.vo.flow.VisitorDataInterpretVo;
import com.ai.analy.vo.flow.structure.FlowUserDaysVo;
import com.ai.analy.vo.flow.structure.FlowUserTypeVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;
import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 访客结构服务
 * 
 * @author huangcm
 * @date 2015-10-21
 */
public class VisitorStructureServiceImpl extends BaseDao implements VisitorStructureService {

	@Autowired
	private FlowMapService flowMapService;
	
	/**
	 * 地域分布查询
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<StructureSectionVo> queryFlowUserRegionT(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.T_type, sum(t.uv) suv, sum(t.order_uv)/sum(t.uv) orderRate, (sum(t.uv)/(select sum(a.uv) suv from flow_user_region_t a where 1=1");
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
			//params.add(Constants.PAGE_NAME.getPageInfo(param.getTarget()).getPageCode());
			params.add(param.getTarget());
		}
		sql.append(" group by a.shop_id");
		sql.append(" )) scale from flow_user_region_t t where 1=1");
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
		sql.append(" group by t.T_type order by suv desc ");
		if (param.getPageSize() != null && param.getPageSize() != 0) {
			sql.append(" limit 0 , ?");
			params.add(param.getPageSize());
		}

		List<StructureSectionVo> list = queryList(sql.toString(), params, new RowMapper<StructureSectionVo>() {
			public StructureSectionVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StructureSectionVo structureVO = new StructureSectionVo();
				if (StringUtils.isEmpty(rs.getString("T_type"))) {
					structureVO.setChnlType("其他");
				} else {
					structureVO.setChnlType(rs.getString("T_type"));
				}
				structureVO.setPv(rs.getInt("suv"));
				structureVO.setScale(rs.getDouble("scale"));
				structureVO.setOrderRate(rs.getDouble("orderRate"));
				return structureVO;
			}
		});

		return list;
	}

	/**
	 * 访客类结构
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowUserTypeVo> queryFlowUserType(QueryParamsVo param) {

		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		// 查询pc端和无线端访客结构
		if (StringUtils.isNotEmpty(param.getUserType()) && param.getUserType().equals(Constants.UserType.QueryType.PC_AND_WIRELESS)) {
			return queryFlowTypePCAndWireless(param);
		}
		
		// 渠道购买力等级
		if (StringUtils.isNotEmpty(param.getUserType()) && param.getUserType().equals(Constants.UserType.QueryType.BUY_LEVEL)) {
			return getChnlBuylevel(param);
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.user_type, sum(t.uv) suv, sum(t.order_uv) sorderuv, sum(t.sessions) sessions, sum(t.order_uv)/sum(t.uv) orderRate, (sum(t.uv)/(select sum(a.uv) suv from flow_user_type a where 1=1");
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
		// 查询类型，如白名单，连锁渠道，新老访客，购买力分级
		if (StringUtils.isNotEmpty(param.getUserType())) {
			if (Constants.UserType.QueryType.WHITE_LIST.equals(param.getUserType())) {
				sql.append(" and a.user_type in ('" + Constants.UserType.USER_BLACK_LIST + "', '" + Constants.UserType.USER_WHITE_LIST + "')");
			} else if (Constants.UserType.QueryType.CHAIN.equals(param.getUserType())) {
				sql.append(" and a.user_type in ('" + Constants.UserType.USER_CHAIN + "', '" + Constants.UserType.USER_NOT_CHAIN + "')");
			} else if (Constants.UserType.QueryType.NEW_AND_OLD.equals(param.getUserType())) {
				sql.append(" and a.user_type in ('" + Constants.UserType.USER_NEW + "', '" + Constants.UserType.USER_OLD + "')");
			} else if (Constants.UserType.QueryType.LINK_TYPE.equals(param.getUserType())) {
				sql.append(" and a.user_type in ('" + Constants.UserType.USER_OUTSIDE_LINK + "', '" + Constants.UserType.USER_INSIDE_LINK + "')");
			}
		}
		sql.append(" )) uvPercent from flow_user_type t where 1=1");
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
		// 查询类型，如白名单，连锁渠道，新老访客，购买力分级，外链与自主访问
		if (StringUtils.isNotEmpty(param.getUserType())) {
			if (Constants.UserType.QueryType.WHITE_LIST.equals(param.getUserType())) {
				sql.append(" and t.user_type in ('" + Constants.UserType.USER_BLACK_LIST + "', '" + Constants.UserType.USER_WHITE_LIST + "')");
			} else if (Constants.UserType.QueryType.CHAIN.equals(param.getUserType())) {
				sql.append(" and t.user_type in ('" + Constants.UserType.USER_CHAIN + "', '" + Constants.UserType.USER_NOT_CHAIN + "')");
			} else if (Constants.UserType.QueryType.NEW_AND_OLD.equals(param.getUserType())) {
				sql.append(" and t.user_type in ('" + Constants.UserType.USER_NEW + "', '" + Constants.UserType.USER_OLD + "')");
			} else if (Constants.UserType.QueryType.LINK_TYPE.equals(param.getUserType())) {
				sql.append(" and t.user_type in ('" + Constants.UserType.USER_OUTSIDE_LINK + "', '" + Constants.UserType.USER_INSIDE_LINK + "')");
			}
		}
		sql.append(" group by t.user_type");

		List<FlowUserTypeVo> resultList = queryList(sql.toString(), params, new FlowUserTypeRowMapper());

		return resultList;
	}

	protected class FlowUserTypeRowMapper implements RowMapper<FlowUserTypeVo> {
		public FlowUserTypeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			FlowUserTypeVo fUserType = new FlowUserTypeVo();
			fUserType.setUserType(rs.getString("user_type"));
			fUserType.setUv(rs.getInt("suv"));
			fUserType.setOrderUv(rs.getInt("sorderuv"));
			fUserType.setUvPercent(rs.getDouble("uvPercent"));
			fUserType.setOrderRate(rs.getDouble("orderRate"));
			fUserType.setSessions(rs.getLong("sessions"));
			return fUserType;
		}
	}

	/**
	 * 最近30天浏览频次分布
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowUserDaysVo> queryFlowUser30Days(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  t.visit_times_type, sum(t.uv) uv, t.order_uv/t.uv orderRate, (sum(t.uv)/(select sum(a.uv) suv from flow_user_30days a where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and a.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and a.client_type = ?");
			params.add(param.getSource());
		}

		if (param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and a.dt = ?");
			params.add(df.format(DateUtils.addDayas(param.getDateTo(), -1)));
		}

		sql.append(" )) uvPercent from flow_user_30days t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id = ?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t.dt = ?");
			params.add(df.format(DateUtils.addDayas(param.getDateTo(), -1)));
		}
		sql.append(" group by t.visit_times_type order by t.uv desc");

		List<FlowUserDaysVo> resultList = queryList(sql.toString(), params, new RowMapper<FlowUserDaysVo>() {
			public FlowUserDaysVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FlowUserDaysVo flowUserDaysVo = new FlowUserDaysVo();
				flowUserDaysVo.setVisitType(rs.getString("visit_times_type"));
				flowUserDaysVo.setUv(rs.getInt("uv"));
				flowUserDaysVo.setUvPercent(rs.getDouble("uvPercent"));
				flowUserDaysVo.setOrderRate(rs.getDouble("orderRate"));
				return flowUserDaysVo;
			}

		});

		return resultList;
	}

	/**
	 * 访客类结构-pc端和无线端
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowUserTypeVo> queryFlowTypePCAndWireless(QueryParamsVo param) {
		/**
		select tb.client_type, sum(tb.uv) suv from 
			(select max(t1.minutes), t1.client_type, t1.uv from flow_overview_all t1 where 1=1 
				and t1.shop_id = ?
				and t1.dt>=? and t1.dt<=?
				group by  t1.dt, t1.client_type) tb
			group by  tb.client_type
		*/
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select tb.client_type, sum(tb.uv) suv from " +
				   "	(select max(t1.minutes), t1.client_type, t1.uv from flow_overview_all t1 where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t1.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t1.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t1.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append("group by  t1.dt, t1.client_type) tb group by  tb.client_type");
		
		List<FlowUserTypeVo> resultList = queryList(sql.toString(), params, new RowMapper<FlowUserTypeVo>() {
			public FlowUserTypeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FlowUserTypeVo obj = new FlowUserTypeVo();
				obj.setUserType("0" + rs.getString("client_type"));
				obj.setUv(rs.getInt("suv"));
				return obj;
			}
		});
		 
		return resultList;
	}

	/**
	 * 7天流量概况解读
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-22
	 * @param param
	 * @return
	 */
	public FlowDateInterpretVo queryFlowOverviewTop(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		FlowDateInterpretVo flowData = new FlowDateInterpretVo();
		
		// 省份以及日均
		List<FlowMapVo> flowPList = flowMapService.queryFlowMap(param);
		if (flowPList != null && flowPList.size() > 0) {
			FlowMapVo flowMapVo = flowPList.get(0);
			flowData.setProvinceName(flowMapVo.getProvinceName());
			flowData.setDateAvgUv((int)flowMapVo.getUv()/7);
		}
		
		// 新访客以及占比
		param.setUserType(Constants.UserType.QueryType.NEW_AND_OLD);
		List<FlowUserTypeVo> newUserList = queryFlowUserType(param);
		if (newUserList != null && newUserList.size() > 0) {
			for (FlowUserTypeVo flowUserTypeVo : newUserList) {
				if (Constants.UserType.USER_NEW.equals(flowUserTypeVo.getUserType())) {
					flowData.setNewUvScale(flowUserTypeVo.getUvPercent());
					break;
				}
			}
		}
		
		// PC端以及占比
		param.setUserType(Constants.UserType.QueryType.PC_AND_WIRELESS);
		List<FlowUserTypeVo> pcUserList = queryFlowUserType(param);
		if (pcUserList != null && pcUserList.size() > 0) {
			double sumUv = 0.0;
			double pcUv = 0.0;
			for (FlowUserTypeVo flowUserTypeVo : pcUserList) {
				sumUv += flowUserTypeVo.getUv();
				if (("0" + Constants.UserType.USER_PC).equals(flowUserTypeVo.getUserType())) {
					pcUv = flowUserTypeVo.getUv();
				}
			}
			flowData.setPcUvScale(pcUv/sumUv);
		}
		
		param.setPageSize(10);
		// 访客主要来源(占比最大，首页楼层等)
		PageBean<FlowSourceVo> fSource = flowMapService.queryFlowSource(param);
		if (fSource != null && fSource.getRecordList().size() > 1) {
			FlowSourceVo flowSourceVo = (FlowSourceVo) fSource.getRecordList().get(0);
			flowData.setSourcePageName(flowSourceVo.getPageName());
		}
		
		// 访客主要去向(占比最大，活动专区等)
		PageBean<FlowNextVo> nextList = flowMapService.queryFlowNext(param);
		if (nextList != null && nextList.getRecordList().size() > 0) {
			FlowNextVo FlowNextVo = (FlowNextVo) nextList.getRecordList().get(0);
			flowData.setDestPageName(FlowNextVo.getNextPageName());
		}
		
		// 最近7天访问人数，较上期
		int currentStatSuv = statisticstUvByTimeSegments(param);
		param.setDateFrom(param.getPreDateFrom());
		param.setDateTo(param.getPreDateTo());
		int lastTimeSuv = statisticstUvByTimeSegments(param);
		flowData.setUv(currentStatSuv);
		flowData.setIncreaseUv(currentStatSuv - lastTimeSuv);
		
		return flowData;
	}
	
	/**
	 * 按时间段统计店铺UV
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public int statisticstUvByTimeSegments(QueryParamsVo param) {
		/**
		select sum(t.uv) suv from (
			select max(tb.minutes) max_minutes ,tb.dt, tb.client_type, tb.uv, tb.pv from flow_overview_all tb where 1=1 
			and tb.shop_id = ?
			and tb.dt>=? and tb.dt<=?
			group by  tb.dt, tb.client_type ) t
		*/
		if (param == null || StringUtils.isEmpty(param.getShopId()) ) {
			return 0;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(t.uv) suv from (" +
				"select max(tb.minutes) max_minutes ,tb.dt, tb.client_type, tb.uv, tb.pv from flow_overview_all tb where 1=1 ");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and tb.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and tb.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and tb.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}
		sql.append(" group by  tb.dt, tb.client_type ) t");
		
		Integer suv = queryOne(sql.toString(), params, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (Integer)rs.getInt("suv");
			}
			
		});
		
		return suv == null ? 0 : suv;
	}

	/**
	 * 7天访客结构解读
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-22
	 * @param param
	 * @return
	 */
	public VisitorDataInterpretVo queryVisitorOverviewTop(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		VisitorDataInterpretVo visitorDate = new VisitorDataInterpretVo();
		
		/*
		// 连锁渠道
		param.setUserType(Constants.UserType.QueryType.CHAIN);
		List<FlowUserTypeVo> chainList = queryFlowUserType(param);
		if (chainList != null && chainList.size() > 0 ) {
			for (FlowUserTypeVo flowUserTypeVo : chainList) {
				if (flowUserTypeVo.getUserType().equals(Constants.UserType.USER_CHAIN)) {
					visitorDate.setChainType(flowUserTypeVo.getUserTypeName());
					visitorDate.setChainTypePercent(flowUserTypeVo.getUvPercent());
					break;
				}
			}
		}
		
		// 白名单
		param.setUserType(Constants.UserType.QueryType.WHITE_LIST);
		List<FlowUserTypeVo> whiteList = queryFlowUserType(param);
		if (whiteList != null && whiteList.size() > 0) {
			for (FlowUserTypeVo flowUserTypeVo : whiteList) {
				if (Constants.UserType.USER_WHITE_LIST.equals(flowUserTypeVo.getUserType())) {
					visitorDate.setWhiteList(flowUserTypeVo.getUserTypeName());
					visitorDate.setWhiteListPercent(flowUserTypeVo.getUvPercent());
					break;
				}
			}
		}
		
		// 渠道类型
		List<StructureSectionVo> chnlTypeList = queryFlowUserRegionT(param);
		if (chnlTypeList != null && chnlTypeList.size() > 0) {
			StructureSectionVo structureSectionVo = chnlTypeList.get(0);
			visitorDate.setChnlType(structureSectionVo.getChnlType());
			visitorDate.setChnlTypePercent(structureSectionVo.getScale());
		}
		*/
		
		// 30天访客浏览频率
		List<FlowUserDaysVo> list = queryFlowUser30Days(param);
		if (list != null && list.size() > 0) {
			FlowUserDaysVo flowUserDaysVo = list.get(0);
			visitorDate.setThirdtyDaysTimes(flowUserDaysVo.getVisitType());
			visitorDate.setThirdtyDaysTimesPercent(flowUserDaysVo.getUvPercent());
		}
		
		// 集中时间段
		List<String> timeList = getHourTime(param);
		visitorDate.setTimeList(timeList);
		
		// 日均UV
		int dailyUv = getdailyUv(param);
		visitorDate.setDailyUv(dailyUv);
		
		
		return visitorDate;
	}
	
	private int getdailyUv(QueryParamsVo param) {
		if (param == null) {
			return 0;
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(t.uv) suv from flow_overview_hourly t  where 1=1");
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
		
		int sumUv = queryOne(sql.toString(), params, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("suv");
			}
		});
		
		if (sumUv != 0) {
			return sumUv/(DateUtils.betweenDays(param.getDateFrom(), param.getDateTo()) + 1);
		}
		
		return 0;
	}

	private List<String> getHourTime(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) {
			return null;
		}
		
		List<String> timeList = new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select tb.hour_time from (  select t.hour_time, sum(t.uv) suv from flow_overview_hourly t  where 1=1");
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
		sql.append(" group by hour_time order by  suv desc  limit 0, 3 ) tb order by tb.hour_time asc ");
		
		timeList = queryList(sql.toString(), params, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("hour_time");
			}
		});
		
		return timeList;
	}
	
	
	private List<FlowUserTypeVo> getChnlBuylevel(QueryParamsVo param) {
		if (param == null || StringUtils.isEmpty(param.getShopId())) 
			return null;
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select tt.orderNum orderNum, tt.orders_type orders_type , _tn.* from flow_map_channel_type tt left join (");
		sql.append(" select t.orders_type t_orders_type, sum(t.order_v) orderUv, sum(t.order_v)/(select sum(t1.order_v) orderv from flow_map_channel t1 where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t1.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t1.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t1.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t1.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}					
		sql.append(" ) orderUvPercent, sum(t.order_uv) uv, sum(t.order_uv)/(select sum(t2.order_uv) orderv from flow_map_channel t2 where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t2.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t2.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t2.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t2.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}	
		sql.append(" ) uvPercent"); 
		
		sql.append(" , sum(t.order_uv)/(select sum(t3.uv) from flow_overview_all t3 where 1=1 ");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t3.shop_id=?");
			params.add(param.getShopId());
		}
		if (param.getSource() != 0) {
			sql.append(" and t3.client_type = ?");
			params.add(param.getSource());
		}
		if (param.getDateFrom() != null && param.getDateTo() != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" and t3.dt >= ?");
			params.add(df.format(param.getDateFrom()));
			sql.append(" and t3.dt <= ?");
			params.add(df.format(param.getDateTo()));
		}					
		sql.append(" ) uvPercentAll");

		sql.append(" from flow_map_channel t where 1=1");
		if (StringUtils.isNotEmpty(param.getShopId())) {
			sql.append(" and t.shop_id=?");
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
		sql.append("group by t_orders_type  ) _tn on _tn.t_orders_type=tt.orders_type order by orderNum asc");
		
		List<FlowUserTypeVo> list = queryList(sql.toString(), params, new RowMapper<FlowUserTypeVo> () {
			public FlowUserTypeVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FlowUserTypeVo fu = new FlowUserTypeVo();
				fu.setUserType(rs.getString("orders_type"));
				fu.setUv(rs.getInt("uv"));
				fu.setOrderUv(rs.getInt("orderUv"));
				fu.setUvPercent(rs.getDouble("uvPercent"));
				fu.setOrderUvPercent(rs.getDouble("orderUvPercent"));
				fu.setUvPercentAll(rs.getDouble("uvPercentAll"));
				return fu;
			}
		});
		
		return list;
	}
	
}
