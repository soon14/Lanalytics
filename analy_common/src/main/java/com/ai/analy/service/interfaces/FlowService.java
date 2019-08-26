package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowMonthSumVo;
import com.ai.analy.vo.flow.FlowOverviewVo;
import com.ai.analy.vo.flow.FlowTrendVo;

public interface FlowService {

	/**
	 * 查询流量概况
	 * 
	 * @param param
	 * @return
	 */
	public FlowOverviewVo getFlowOverviewAll(QueryParamsVo param);

	public FlowTrendVo getFlowOverviewTimelyVo(QueryParamsVo param);

	/**
	 * 查询流量月报
	 * 
	 * @param param
	 * @return
	 */
	public PageBean<FlowMonthSumVo> queryFlowMonthSum(QueryParamsVo param);
	
	/**
	 * 统计流量月报
	 * 
	 * @param param
	 * @return
	 */
	public FlowMonthSumVo sumFlowMonth(QueryParamsVo param);
}
