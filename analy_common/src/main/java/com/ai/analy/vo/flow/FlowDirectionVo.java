package com.ai.analy.vo.flow;

import java.util.List;

import com.ai.analy.vo.BaseVo;

public class FlowDirectionVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5018891099125730931L;

	//流量来源
	private List<FlowSourceVo> sourcePv;
		
	/**
	 * 店内分布
	 */
	//店铺首页Pv
	private int shopPv;
	//商品详情页Pv
	private int goodsPv;
	
	//流量去向
	private List<FlowNextVo> destPv;
	
	public List<FlowSourceVo> getSourcePv() {
		return sourcePv;
	}

	public void setSourcePv(List<FlowSourceVo> sourcePv) {
		this.sourcePv = sourcePv;
	}

	public int getShopPv() {
		return shopPv;
	}

	public void setShopPv(int shopPv) {
		this.shopPv = shopPv;
	}

	public int getGoodsPv() {
		return goodsPv;
	}

	public void setGoodsPv(int goodsPv) {
		this.goodsPv = goodsPv;
	}

	public List<FlowNextVo> getDestPv() {
		return destPv;
	}

	public void setDestPv(List<FlowNextVo> destPv) {
		this.destPv = destPv;
	}
}
