package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.AreaInfoVo;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;
import com.ai.analy.vo.trade.TradeAssociationVo;
import com.ai.analy.vo.trade.TradeCategorySumVo;
import com.ai.analy.vo.trade.TradeMapVo;
import com.ai.analy.vo.trade.TradeMonthSumVo;
import com.ai.analy.vo.trade.TradeOrdersTypeSumVo;
import com.ai.analy.vo.trade.TradeRankVo;

public interface TradeService {

	/**
	 * 查询交易总览数据
	 * @param param
	 * @return
	 */
	public GoodsOverviewVo getTradeOverview(QueryParamsVo param);
	
	/**
	 * 获取交易交易地域分布地域分布数据
	 * @param param
	 * @param showName 1：商品交易额 2：商品交易量
	 * @return
	 */
	public List<TradeMapVo> queryTradeMap(QueryParamsVo param);
	
	/**
	 * 查询省份地市信息
	 * @param areaInfoVo
	 * @return
	 */
	public List<AreaInfoVo> queryAreaByParentCode(String parentAreaCode);
	
	/**
	 * 查询当前店铺排名信息
	 * @param param
	 * @return
	 */
	public List<TradeRankVo> queryTradeRank(QueryParamsVo param,List<String> shopIds);
	
	/**
	 * 我的行业排名趋势
	 * @param param
	 * @return
	 */
	public List<TradeRankVo> queryTradeRankTrend(QueryParamsVo param,List<String> preTimes);
	
	/**
	 * 查询按渠道类型统计的数据 T1-T6
	 * @param param
	 * @return
	 */
	public List<StructureSectionVo> queryUserRegion(QueryParamsVo param);
	
	/**
	 * 查询渠道对比数据
	 * @param param
	 * @return
	 */
	public List<StructureSectionVo> queryChnlType(QueryParamsVo param);
	
	/**
	 * 查询用户行为
	 * @param param
	 * @return
	 */
	public List<StructureSectionVo> queryChnlBehavior(QueryParamsVo param);
	
	/**
	 * 查询商品关联
	 * @param param
	 * @return
	 */
	public PageBean<TradeAssociationVo> queryAssociation(QueryParamsVo param);
	
	
	/**
	 * 查询交易月报
	 * @param param
	 * @return
	 */
	public PageBean<TradeMonthSumVo> queryTradeMonthSum(QueryParamsVo param);
	
	/**
	 * 查询商品子分类销售统计
	 * @param param
	 * @return
	 */
	public List<TradeCategorySumVo> queryTradeCategorySum(QueryParamsVo param);
	
	/**
	 * 查询历史订购次数分布
	 * @param param
	 * @return
	 */
	public List<TradeOrdersTypeSumVo> queryTradeOrdersTypeSum(QueryParamsVo param);
	
	/**
	 * 查询店铺每日的交易金额接口
	 * @param shopId
	 * @param format
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String dailyTradeData(String shopId,String  format, String certainDateStr,String startDateStr,String endDateStr);
	
	/**
	 * 每日新增会员数接口
	 * @param shopId
	 * @param format
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String dailyMemberAnaly(String certainDateStr,String startDateStr,String endDateStr);
	
}