package com.ai.analy.service.impl;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.FlowService;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowMonthSumVo;
import com.ai.analy.vo.flow.FlowOverviewVo;
import com.ai.analy.vo.flow.FlowTrendVo;
import com.ai.analy.vo.goods.ReturnGdsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowServiceImpl extends BaseDao implements FlowService{

	@Autowired
	JdbcTemplate jdbc;
	
	class Tmp{
		double avgPv;
		double avgStay ;
		int pv;
		int uv;
		double bounce;
		
		public Tmp(int pv, int uv, double bounce, double avgPv, double avgStay){
			this.pv = pv;
			this.uv = uv;
			this.bounce = bounce;
			this.avgPv = avgPv;
			this.avgStay = avgStay;
		}

	}
	
	
	@Override
	public FlowOverviewVo getFlowOverviewAll(QueryParamsVo param) {

		SqlRowSet max = jdbc.queryForRowSet("select max(minutes) as ms from flow_overview_all "
				+ "where dt = ? AND shop_id = ?  ", param.getDateTo(), param.getShopId());
		max.next();
		int maxMM = max.getInt("ms");
		String sqlFirst = "select sum(pv) as pv, sum(uv) as uv, sum(sessions) as sv, sum(session_ones) as sv1, sum(stay_seconds) as stay from flow_overview_all "
				+ "where dt >= ? AND dt <= ? AND shop_id = ? AND  minutes = ? AND %s = ?";
		sqlFirst = String.format( sqlFirst, param.getSource() == 0 ? "0" : "client_type");
		SqlRowSet value1 = jdbc.queryForRowSet(sqlFirst, param.getDateFrom(), param.getDateTo(), param.getShopId(), maxMM, param.getSource());
		FlowOverviewVo vo = new FlowOverviewVo();;
		if (value1.next()){
			vo.setPv(value1.getInt("pv"));
			vo.setUv(value1.getInt("uv"));
			vo.setAvgStay(value1.getFloat("stay") / vo.getPv());
			vo.setAvgPv(vo.getPv() / (vo.getUv() + 0.0));
			vo.setBounceRate(value1.getFloat("sv1") / value1.getFloat("sv"));
		
			SqlRowSet value2 = jdbc.queryForRowSet(sqlFirst, param.getPreDateFrom(), param.getPreDateTo(), param.getShopId(), maxMM, param.getSource());
			if (value2.next()){
				float pv = value2.getFloat("pv");
				vo.setPvCompare( (vo.getPv() - pv ) / pv);
				float uv = value2.getFloat("uv");
				vo.setUvCompare((vo.getUv() - uv ) / uv);
				float avgStay = value2.getFloat("stay") / pv;;
				if (Float.isNaN(avgStay)) {
					avgStay = 0f;
				}
				vo.setAvgStayCompare((vo.getAvgStay() - avgStay ) / avgStay);
				double avgPv = (pv + 0.0) /uv;
				if (Double.isNaN(avgPv)) {
					avgPv = 0f;
				}
				vo.setAvgPvCompare((vo.getAvgPv() - avgPv ) / avgPv);
				double bounceRate = value2.getFloat("sv1") / value2.getFloat("sv");
				if (Double.isNaN(bounceRate)) {
					bounceRate = 0f;
				}
				vo.setBounceRateCompare((vo.getBounceRate() - bounceRate ) / bounceRate);				
			}
		}
		return vo;
	}

	@Override
	public FlowTrendVo getFlowOverviewTimelyVo(QueryParamsVo param) {
		FlowTrendVo vo = new FlowTrendVo();
		if (param.getDateFrom().equals(param.getDateTo())){
			String sqlFirst = "select sum(pv) as pv, sum(uv) as uv, sum(sessions) as sv, sum(session_ones) as sv1, sum(stay_seconds) as stay, hour_time from flow_overview_hourly "
					+ " where dt >= ? AND dt <= ? AND shop_id = ? AND %s = ? "
					+ " GROUP BY hour_time ";
			sqlFirst = String.format( sqlFirst, param.getSource() == 0 ? "0" : "client_type");
			SqlRowSet rowSet = jdbc.queryForRowSet(sqlFirst, param.getDateFrom(), param.getDateTo(), param.getShopId(),  param.getSource());
			Map<String, Tmp> tmpMap = new HashMap<String, FlowServiceImpl.Tmp>();
			while(rowSet.next()){
				Tmp tmp = new Tmp(rowSet.getInt("pv"), rowSet.getInt("uv"), rowSet.getDouble("sv1") / rowSet.getDouble("sv"),
						rowSet.getDouble("pv")/rowSet.getInt("uv"), rowSet.getDouble("stay") /rowSet.getInt("pv"));
				tmpMap.put(rowSet.getString("hour_time"), tmp);
			}
			for(int i = 0 ; i < 24; i++){
				String dt = i + "";
				vo.getxAxis().add(dt);
				Tmp tmp = tmpMap.get(dt);
				if (tmp == null){
					vo.getPvs().add(0);
					vo.getUvs().add(0);
					vo.getAvgPvs().add(0.0);
					vo.getAvgStays().add(0.0);
					vo.getBounceRates().add(0.0);
				}else{
					vo.getPvs().add(tmp.pv);
					vo.getUvs().add(tmp.uv);
					vo.getAvgPvs().add(tmp.avgPv);
					vo.getAvgStays().add(tmp.avgStay);
					vo.getBounceRates().add(tmp.bounce);
				}
			}
		}else{
			String sqlFirst = "select T.dt, max(T.pv) as pv, max(T.uv) as uv, sum(T.sv) as sv, max(T.sv1) as sv1, max(T.stay) as stay from"
					+ " (select dt,sum(pv) as pv, sum(uv) as uv, sum(sessions) as sv, sum(session_ones) as sv1, sum(stay_seconds) as stay from flow_overview_all "
					+ " where dt >= ? AND dt <= ? AND shop_id = ? AND %s = ? "
					+ " GROUP BY dt, minutes) T "
					+ " GROUP BY T.dt";
			sqlFirst = String.format( sqlFirst, param.getSource() == 0 ? "0" : "client_type");
			SqlRowSet rowSet = jdbc.queryForRowSet(sqlFirst, param.getDateFrom(), param.getDateTo(), param.getShopId(),  param.getSource());
			Map<String, Tmp> tmpMap = new HashMap<String, FlowServiceImpl.Tmp>();

			while(rowSet.next()){
				Tmp tmp = new Tmp(rowSet.getInt("pv"), rowSet.getInt("uv"), rowSet.getDouble("sv1") / rowSet.getDouble("sv"),
						rowSet.getDouble("pv")/rowSet.getInt("uv"), rowSet.getDouble("stay") /rowSet.getInt("pv"));
				tmpMap.put(rowSet.getString("dt").split(" ")[0], tmp);
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");   
			for(int i = 0 ; ; i++){
				Date now = new Date(param.getDateFrom().getTime() + i * 86400000L);
				if (now.after(param.getDateTo())){
					break;
				}
				String dt = df.format(now);
				vo.getxAxis().add(dt);
				Tmp tmp = tmpMap.get(dt);
				if (tmp == null){
					vo.getPvs().add(0);
					vo.getUvs().add(0);
					vo.getAvgPvs().add(0.0);
					vo.getAvgStays().add(0.0);
					vo.getBounceRates().add(0.0);
				}else{
					vo.getPvs().add(tmp.pv);
					vo.getUvs().add(tmp.uv);
					vo.getAvgPvs().add(tmp.avgPv);
					vo.getAvgStays().add(tmp.avgStay);
					vo.getBounceRates().add(tmp.bounce);
				}
			}
		}
		return vo;
	}

	@Override
	public PageBean<FlowMonthSumVo> queryFlowMonthSum(QueryParamsVo param) {
		String sql = "select flow.month, flow.pv, flow.uv, flow.sv, flow.sv1, flow.stay, staff.registerStaff from "
				+"(select DATE_FORMAT(dt,'%Y-%m') as month, sum(pv) as pv, sum(uv) as uv, sum(sessions) as sv, sum(session_ones) as sv1, sum(stay_seconds) as stay  "
				+"from flow_overview_all as t where  dt >= ? AND dt <= ? AND shop_id = ? group by DATE_FORMAT(dt,'%Y-%m')) as flow "
				+"left join "
				+"(select DATE_FORMAT(dt,'%Y-%m') as month, sum(new_staff_num) as registerStaff "
				+"from staff_create where  dt >= ? AND dt <= ? group by DATE_FORMAT(dt,'%Y-%m')) as staff "
				+"on flow.month = staff.month "
				+"order by flow.month asc";
		
		List<Object> params = new ArrayList<Object>();
		params.add(param.getDateFrom());
		params.add(param.getDateTo());
		params.add(param.getShopId());
		params.add(param.getDateFrom());
		params.add(param.getDateTo());
		
		//查询总数
		int count = 0;
		if (param.getPageNo() != null && param.getPageNo() != 0 && param.getPageSize() != null) {
			count = queryCount(sql, params);
			
			sql+=" limit ?, ?";
			params.add((param.getPageNo() - 1) * param.getPageSize());
			params.add(param.getPageSize());
		}
		
		//查询分页
		List<FlowMonthSumVo> list = queryList(sql.toString(), params, new RowMapper<FlowMonthSumVo>(){
			public FlowMonthSumVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FlowMonthSumVo sumVo = new FlowMonthSumVo();
				sumVo.setMonth(rs.getString("month"));
				sumVo.setPv(rs.getInt("pv"));
				sumVo.setUv(rs.getInt("uv"));
				sumVo.setAvgStay(rs.getFloat("stay") / sumVo.getPv());
				sumVo.setAvgPv(sumVo.getPv() / (sumVo.getUv() + 0.0));
				sumVo.setBounceRate(rs.getFloat("sv1") / rs.getFloat("sv"));
				sumVo.setRegisterStaff(rs.getInt("registerStaff"));
				
				return sumVo;
			}
		});
		
		//封装pagebean
		PageBean<FlowMonthSumVo> pageBean = new PageBean<FlowMonthSumVo>(param.getPageNo(), param.getPageSize(), list, count);
		
		return pageBean;
	}

	@Override
	public FlowMonthSumVo sumFlowMonth(QueryParamsVo param) {
		String sql = "select flow.pv, flow.uv, staff.registerStaff from "
				+"(select sum(pv) as pv, sum(uv) as uv "
				+"from flow_overview_all as t where  dt >= ? AND dt <= ? AND shop_id = ? ) as flow , "
				+"(select sum(new_staff_num) as registerStaff "
				+"from staff_create where  dt >= ? AND dt <= ? ) as staff ";
		
		List<Object> params = new ArrayList<Object>();
		params.add(param.getDateFrom());
		params.add(param.getDateTo());
		params.add(param.getShopId());
		params.add(param.getDateFrom());
		params.add(param.getDateTo());
		
		FlowMonthSumVo sumVo = queryOne(sql.toString(), params, new RowMapper<FlowMonthSumVo>() {
			public FlowMonthSumVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FlowMonthSumVo sumVo = new FlowMonthSumVo();
				sumVo.setPv(rs.getInt("pv"));
				sumVo.setUv(rs.getInt("uv"));
				sumVo.setRegisterStaff(rs.getInt("registerStaff"));
				return sumVo;
			}
		} );
		
		return sumVo;
	}
	
	
}
