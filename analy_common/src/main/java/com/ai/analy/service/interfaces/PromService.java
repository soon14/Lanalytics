package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.prom.PromIndexDateSumVo;
import com.ai.analy.vo.prom.PromIndexOverviewVo;
import com.ai.analy.vo.prom.PromIndexTrendVo;
import com.ai.analy.vo.prom.PromInfoVo;
import com.ai.analy.vo.prom.PromTypeVo;

/**
 * 促销分析服务接口
 * 
 * @author limb
 * @date 2016年9月5日
 */
public interface PromService {
	/**
	 * 查询有效促销分类列表
	 * 
	 * @return
	 */
	public List<PromTypeVo> getValidPromType();

	/**
	 * 查询促销列表
	 * 
	 * @param paramsVo
	 * @return
	 */
	public PageBean<PromInfoVo> getPromList(QueryParamsVo paramsVo);

	/**
	 * 查询促销指标概览
	 * 
	 * @param paramsVo
	 * @return
	 */
	public PromIndexOverviewVo getPromIndexOverview(QueryParamsVo paramsVo);

	/**
	 * 查询促销指标趋势，包括促销及促销商品
	 * 
	 * @param paramsVo
	 * @return
	 */
	public PromIndexTrendVo getPromIndexTrend(QueryParamsVo paramsVo);

	/**
	 * 查询促销商品列表
	 * 
	 * @param paramsVo
	 * @return
	 */
	public PageBean<GdsRankVo> getPromGdsList(QueryParamsVo paramsVo);

	/**
	 * 查询促销商品指标日期列表
	 * 
	 * @param paramsVo
	 * @return
	 */
	public PageBean<PromIndexDateSumVo> getPromIndexDateList(QueryParamsVo paramsVo);
}
