package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.GdsCatAnalyVo;
import com.ai.analy.vo.goods.GdsCategoryVo;
import com.ai.analy.vo.goods.GdsRankVo;
import com.ai.analy.vo.goods.GdsSaleTrendVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;

/**
 * 商品分析服务
 * 
 * @author huangcm
 * @date 2015-10-29
 */
public interface GoodsAnalyService {

	/**
	 * 商品信息总览已及与上期比较
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public GoodsOverviewVo getGoodsOverview(QueryParamsVo param);

	/**
	 * 商品信息总览
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public GoodsOverviewVo queryGoodsOverview(QueryParamsVo param);

	/**
	 * 商品销售趋势(日内)
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public List<GdsSaleTrendVo> getGdsSaleTrend4Hourly(QueryParamsVo param);

	/**
	 * 商品销售趋势(日区间)
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public List<GdsSaleTrendVo> getGdsSaleTrend4Days(QueryParamsVo param);

	/**
	 * 商品销售趋势
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public List<GdsSaleTrendVo> getGdsSaleTrend(QueryParamsVo param);

	/**
	 * 商品排行概览
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-29
	 * @param param
	 * @return
	 */
	public PageBean<GdsRankVo> getGdsRankingList(QueryParamsVo param);

	/**
	 * 商品效果明细
	 *
	 *
	 * @author huangcm
	 * @date 2015-11-11
	 * @param param
	 * @return
	 */
	public PageBean<GdsRankVo> getGdsDetails(QueryParamsVo param);

	/**
	 * 获取商品分类
	 *
	 * @param shopId
	 * @return
	 */
	public List<GdsCategoryVo> queryCatsForShop(String shopId);

	/**
	 * 商品分类分析
	 *
	 * @param param
	 * @return
	 */
	public List<GdsCatAnalyVo> queryGdsCategory(QueryParamsVo param);
	
	/**
	 * 商品分类分析(品牌)数据导出需要
	 *
	 * @param param
	 * @return
	 */
	public List<GdsCatAnalyVo> queryGdsCategory4BrandByExport(QueryParamsVo param);

	/**
	 * 查询商品子分类
	 * 
	 * @param parentCatId
	 *            父分类ID
	 * @return
	 */
	public List<GdsCategoryVo> querySubGdsCategory(String parentCatId);
	
	/**
	 * 根据catId查询商品分类
	 * @param catId
	 * @return
	 */
	public GdsCategoryVo getGdsCategoryById(String catId);
}
