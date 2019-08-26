package com.ai.analy.vo.trade;

import com.ai.analy.vo.BaseVo;

/**
 * 交易月报vo
 * 
 * @author limb
 * @date 2016年4月24日
 */
public class TradeMonthSumVo extends BaseVo{
	private static final long serialVersionUID = 4213027291915888115L;
	// 月份
	private String month;
	// 下单金额
	private long orderMoney;
	// 支付金额
	private long payMoney;
	// 下单件数
	private long orderCount;			
	// 支付件数
	private long payCount;
	//下单成功率
	private double ordSuccessRate;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getOrderMoney() {
		return orderMoney;
	}
	public void setOrderMoney(long orderMoney) {
		this.orderMoney = orderMoney;
	}
	public long getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(long payMoney) {
		this.payMoney = payMoney;
	}
	public long getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(long orderCount) {
		this.orderCount = orderCount;
	}
	public long getPayCount() {
		return payCount;
	}
	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}
	public double getOrdSuccessRate() {
		return ordSuccessRate;
	}
	public void setOrdSuccessRate(double ordSuccessRate) {
		this.ordSuccessRate = ordSuccessRate;
	}				
}
