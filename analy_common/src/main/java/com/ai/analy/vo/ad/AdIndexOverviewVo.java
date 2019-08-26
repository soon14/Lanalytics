package com.ai.analy.vo.ad;

import com.ai.analy.vo.BaseVo;

/**
 * 广告指标概览vo
 * 
 * @author limb
 * @date 2016年9月4日
 */
public class AdIndexOverviewVo extends BaseVo {
	private static final long serialVersionUID = -288373348155214160L;
	private int uv;
	private double uvCompare = 0;
	private int pv;
	private double pvCompare = 0;
	private double orderUvRate;
	private double orderUvRateCompare = 0;
	private double payUvRate;
	private double payUvRateCompare = 0;

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public double getUvCompare() {
		return uvCompare;
	}

	public void setUvCompare(double uvCompare) {
		this.uvCompare = uvCompare;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public double getPvCompare() {
		return pvCompare;
	}

	public void setPvCompare(double pvCompare) {
		this.pvCompare = pvCompare;
	}

	public double getOrderUvRate() {
		return orderUvRate;
	}

	public void setOrderUvRate(double orderUvRate) {
		this.orderUvRate = orderUvRate;
	}

	public double getOrderUvRateCompare() {
		return orderUvRateCompare;
	}

	public void setOrderUvRateCompare(double orderUvRateCompare) {
		this.orderUvRateCompare = orderUvRateCompare;
	}

	public double getPayUvRate() {
		return payUvRate;
	}

	public void setPayUvRate(double payUvRate) {
		this.payUvRate = payUvRate;
	}

	public double getPayUvRateCompare() {
		return payUvRateCompare;
	}

	public void setPayUvRateCompare(double payUvRateCompare) {
		this.payUvRateCompare = payUvRateCompare;
	}
}
