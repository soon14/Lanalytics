package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.PageInfoVo;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowExitPageVo;
import com.ai.analy.vo.flow.FlowMapOverviewVo;
import com.ai.analy.vo.flow.FlowMapRefDomainVo;
import com.ai.analy.vo.flow.FlowMapVo;
import com.ai.analy.vo.flow.FlowNextVo;
import com.ai.analy.vo.flow.FlowSourceOrderVo;
import com.ai.analy.vo.flow.FlowSourceVo;

public interface FlowMapService {
	
	/**
	 * 流量地图查询
	 *
	 * @author huangcm
	 * @date 2015-10-15
	 * @param param
	 * @return
	 */
	public List<FlowMapVo> queryFlowMap(QueryParamsVo param);
	
	/**
	 * 流量来源查询
	 *
	 * @author huangcm
	 * @date 2015-10-16
	 * @param param
	 * @return
	 */
	public PageBean<FlowSourceVo> queryFlowSource(QueryParamsVo param);
	
	/**
	 * 流量去向查询
	 *
	 * @author huangcm
	 * @date 2015-10-16
	 * @return
	 */
	public PageBean<FlowNextVo> queryFlowNext(QueryParamsVo param);
	
	
	
	/**
	 * 查询总店铺或商品详情UV
	 *
	 * @author huangcm
	 * @date 2015-10-16
	 * @param param
	 * @return
	 */
	public Integer getSumUV(QueryParamsVo param);
	
	/**
	 * 离开页面去向查询
	 *
	 * @author huangcm
	 * @date 2015-10-19
	 * @param param
	 * @return
	 */
	public PageBean<FlowExitPageVo> queryExitPage(QueryParamsVo param);
	
	/**
	 * 查询总店铺页或商品详情页流量概况
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowMapOverviewVo> queryFlowMapOverview(QueryParamsVo param);

	/**
	 * 流量来源下单情况
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-23
	 * @param param
	 * @return
	 */
	public PageBean<FlowSourceOrderVo> queryFlowSourceOrder(QueryParamsVo param);

	
	/**
	 * 流量来源下单转化率情况以及环比
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-23
	 * @param param
	 * @return
	 */
	public PageBean<FlowSourceOrderVo> getFlowSourceOrderMom(QueryParamsVo param);
	
	/**
	 * 查询统计界面
	 * @return
	 */
	public List<PageInfoVo> getPageInfo(QueryParamsVo param);
	
	/**
	 * 查询全平台-外站访问来源
	 * @param param
	 * @return
	 */
	public PageBean<FlowMapRefDomainVo> queryFlowRefDomain(QueryParamsVo param);
}
