package com.ai.analy.service.interfaces;

/**
 * 猜你喜欢功能，推荐商品列表服务
 * Title: MVNO-CRM <br>
 * Description: <br>
 * Date: 2016-5-10 <br>
 * Copyright (c) 2016 AILK <br>
 * 
 * @author liangyi
 */
public interface IRecommendGoodsService {
    
	/**
     * 根据用户id获取推荐商品列表
     * @param userId    用户的ID
     * @param format    返回数据的格式。默认JSON,目前仅支持JSON格式。
     * @param gdsNumStr 返回商品数量。默认返回10个商品。
     * @param willQueryCatgList 是否要查询商品分类
     * @return
     * @author liangyi
     */
    public String recommendGoods(String userId,String format,String gdsNumStr,boolean willQueryCatgList);
    
    /**
     * 根据用户id获取推荐商品列表。分页查询
     * @param userId	用户的ID
     * @param format	返回数据的格式。默认JSON,目前仅支持JSON格式。
     * @param gdsNumStr	查询商品总个数。默认100，最大5000。
     * @param willQueryCatgList	是否要查询商品分类
     * @param pageNoStr	分页查询时所查询的页数。
     * @param pageSizeStr	分页查询时每页显示的数据条数。
     * @return
     */
    public String recommendGoodsPaging(String userId,String format,String gdsNumStr,boolean willQueryCatgList,String pageNoStr,String pageSizeStr);

    
    /**
     * 商品推荐接口
     * @param skuId    	源单品id
     * @param userId    用户的ID
     * @param skuNumStr 返回商品数量。默认返回20个商品。
     * @return
     * @author liangyi
     */
    public String recommendRelatedGds(String skuId,String userId,String skuNumStr);
    
    /**
     * 根据skuId获取推荐商品列表。分页查询
     * @param skuId
     * @param userId
     * @param skuNumStr
     * @param pageNoStr
     * @param pageSizeStr
     * @return
     * @author liangyi
     */
    public String recommendRelatedGdsPaging(String skuId,String userId,String skuNumStr,String pageNoStr,String pageSizeStr);

}
