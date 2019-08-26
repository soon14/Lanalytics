package com.ai.analy.service.interfaces;

import java.util.Map;

/**
 * 商品排行公共服务
 * Created by yafei on 2016/4/29.
 */
public interface IGoodsRankService {

    /**
     * 获取商品排行数据，公共服务
     __接口入参__  
		| 参数名                        | 类型                      | 说明                                                                                                          | 默认值   |
		|-------------|-----------|---------------------------------------|------|
		| shopId      | string    | 商城ID：100-人卫智慧服务商城，101-人卫积分商城            | 100  |
		| category    | string    | 商品分类，按照指定分类生成排行，字典值取自商品分类字典表  | 全部       |
		| status      | string    | 商品状态，1-全部，2-已上架                                                          | 2    |
		| rankType    | string    | 排行榜类型：11-销量排行，12-浏览量排行                                | 11   |
		| pageNumber  | int       | 当前查询页码                                                                                          | 1    |
		| pageSize    | int       | 每页显示商品数，最大值500                    | 20   |
	/**
	 * 商品排行榜接口
	 * @param requestParams 该参数为http请求的参数
	 * @return
	 */
	public String getGoodsRank(Map<String,String> requestParams);
}
