package com.ai.analy.vo.search;

import com.ai.analy.vo.BaseVo;

public class SearchRankingShopVo extends BaseVo {
	private static final long serialVersionUID = 3595077504295810741L;
	private String keywords;		// 关键词
	private long pv;				// 检索量
	private long uv;				// 访客
	private long orderUv;			// 下单量
	private double orderUvRate;		// 下单转化率
	private long tradeMoney;		// 交易金额

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public long getOrderUv() {
		return orderUv;
	}

	public void setOrderUv(long orderUv) {
		this.orderUv = orderUv;
	}

	public double getOrderUvRate() {
		return orderUvRate;
	}

	public void setOrderUvRate(double orderUvRate) {
		this.orderUvRate = orderUvRate;
	}

	public long getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(long tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

}
