package com.ai.analy.vo.search;

import com.ai.analy.vo.BaseVo;

public class FlowSearchOverviewVo extends BaseVo {

	private static final long serialVersionUID = -4763785236381794852L;
	
	private long uv;						// 搜索访客数
	private double uvPercent;				// 搜索访客占总访客比例
	private long orderUv;					// 下单量
	private double orderUvRate;				// 下单转换率
	private long keyWordsNum;				// 关键词数量

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public double getUvPercent() {
		return uvPercent;
	}

	public void setUvPercent(double uvPercent) {
		this.uvPercent = uvPercent;
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

	public long getKeyWordsNum() {
		return keyWordsNum;
	}

	public void setKeyWordsNum(long keyWordsNum) {
		this.keyWordsNum = keyWordsNum;
	}
	
}
