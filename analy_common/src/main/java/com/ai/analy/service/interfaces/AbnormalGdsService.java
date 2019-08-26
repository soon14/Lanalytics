package com.ai.analy.service.interfaces;

import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.goods.AbnormalGdsCountVo;
import com.ai.analy.vo.goods.AbnormalGdsVo;
import com.ai.analy.vo.goods.ComplaintGdsVo;
import com.ai.analy.vo.goods.ReturnGdsVo;

/**
 * @Description: 异常商品服务
 * 
 * @author huangcm
 * @date 2015-12-15
 */
public interface AbnormalGdsService {
	
	/**
	 * 流量下跌环比50%商品
	 *
	 *
	 * @author huangcm
	 * @date 2015-12-16
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> gdsFlowDown(QueryParamsVo param);
	
	
	/**
	 * 支付转换率下跌
	 *
	 * @author huangcm
	 * @date 2015-12-17
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> payUvRateDown(QueryParamsVo param);
	
	
	/**
	 * 零销量
	 *
	 * @author huangcm
	 * @date 2015-12-18
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> sellZero(QueryParamsVo param);
	
	/**
	 * 零库存
	 *
	 * @author huangcm
	 * @date 2015-12-18
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> stockZero(QueryParamsVo param);
	
	/**
	 * 库存预警
	 *
	 * @author huangcm
	 * @date 2015-12-18
	 * @param param
	 * @return
	 */
	public PageBean<AbnormalGdsVo> stockWarning(QueryParamsVo param);
	
	
	public AbnormalGdsCountVo queryCount(QueryParamsVo param);
	
	/**
	 * 商品退货数据
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-12-15
	 * @param param
	 * @return
	 */
	public ReturnGdsVo returnGds(QueryParamsVo param);

	/**
	 * 投诉数据
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-12-15
	 * @param param
	 * @return
	 */
	public ComplaintGdsVo Complaint(QueryParamsVo param);

}
