package com.ai.analy.vo.prom;

import com.ai.analy.vo.BaseVo;

/**
 * 促销指标概览Vo
 * 
 * @author limb
 * @date 2016年8月30日
 */
public class PromIndexOverviewVo extends BaseVo {
	private static final long serialVersionUID = -6594587271438616099L;

	// 访客数
	private int pv = 0;
	// pv较上期增减占比
	private double pvCompare = 0;
	// 浏览量
	private int uv = 0;
	// uv较上期增减占比
	private double uvCompare = 0;
	// 跳失率
	private double exitRate = 0;
	// 跳失率较上期增减占比
	private double exitRateCompare = 0;
	// 下单转化率
	private double orderUvRate = 0;
	// 下单转化率较上期增减占比
	private double orderUvRateCompare = 0;
	// 平均下单件数
	private double orderCountAvg = 0;
	// 平均下单件数较上期增减占比
	private double orderCountAvgCompare = 0;
	// 交易量
	private long payCount = 0;
	// 交易量较上期增减占比
	private double payCountCompare = 0;
	// 交易额
	private double payMoney = 0;
	// 交易额较上期增减占比
	private double payMoneyCompare = 0;

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

	public double getExitRate() {
		return exitRate;
	}

	public void setExitRate(double exitRate) {
		this.exitRate = exitRate;
	}

	public double getExitRateCompare() {
		return exitRateCompare;
	}

	public void setExitRateCompare(double exitRateCompare) {
		this.exitRateCompare = exitRateCompare;
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

	public double getOrderCountAvg() {
		return orderCountAvg;
	}

	public void setOrderCountAvg(double orderCountAvg) {
		this.orderCountAvg = orderCountAvg;
	}

	public double getOrderCountAvgCompare() {
		return orderCountAvgCompare;
	}

	public void setOrderCountAvgCompare(double orderCountAvgCompare) {
		this.orderCountAvgCompare = orderCountAvgCompare;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}

	public double getPayCountCompare() {
		return payCountCompare;
	}

	public void setPayCountCompare(double payCountCompare) {
		this.payCountCompare = payCountCompare;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	public double getPayMoneyCompare() {
		return payMoneyCompare;
	}

	public void setPayMoneyCompare(double payMoneyCompare) {
		this.payMoneyCompare = payMoneyCompare;
	}
}
