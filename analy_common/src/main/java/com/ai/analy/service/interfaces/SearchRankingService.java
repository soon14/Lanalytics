package com.ai.analy.service.interfaces;

import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.search.FlowSearchOverviewVo;
import com.ai.analy.vo.search.SearchRankingShopVo;
import com.ai.analy.vo.search.SearchRankingVo;

/**
 * 搜索排行服务
 * 
 * @author huangcm
 * @date 2015-10-30
 */
public interface SearchRankingService {
	
	/**
	 * 平台热词
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	public PageBean<SearchRankingVo> queryHotKeyWords(QueryParamsVo param);
	
	
	
	/**
	 * 平台热词(含环比)
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	public PageBean<SearchRankingVo> getHotKeyWords(QueryParamsVo param);
	
	
	/**
	 * 店铺热搜词
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	public PageBean<SearchRankingShopVo> getHotKeyWordsShop(QueryParamsVo param);
	
	/**
	 * 店铺搜索流量总览
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-30
	 * @param param
	 * @return
	 */
	public FlowSearchOverviewVo getFlowSearchOverviw(QueryParamsVo param);

}
