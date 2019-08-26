package com.ai.analy.service.interfaces;

import com.ai.analy.vo.ad.AdIndexDateSumVo;
import com.ai.analy.vo.ad.AdIndexOverviewVo;
import com.ai.analy.vo.ad.AdIndexTrendVo;
import com.ai.analy.vo.ad.AdInfoVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;

/**
 * 广告分析服务接口
 * 
 * @author limb
 * @date 2016年9月5日
 */
public interface AdService {
	/**
	 * 查询广告列表
	 * 
	 * @param paramsVo
	 * @return
	 */
	public PageBean<AdInfoVo> queryAdList(QueryParamsVo paramsVo);

	/**
	 * 查询广告指标概览
	 * 
	 * @param paramsVo
	 * @return
	 */
	public AdIndexOverviewVo getAdIndexOverview(QueryParamsVo paramsVo);

	/**
	 * 查询广告指标趋势
	 * 
	 * @param paramsVo
	 * @return
	 */
	public AdIndexTrendVo getAdIndexTrend(QueryParamsVo paramsVo);

	/**
	 * 查询广告指标日期列表
	 * 
	 * @param paramsVo
	 * @return
	 */
	public PageBean<AdIndexDateSumVo> getAdIndexDateList(QueryParamsVo paramsVo);
}