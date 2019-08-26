package com.ai.analy.vo.trade;

import com.ai.analy.vo.BaseVo;

/**
 * 用户订购次数分布vo
 * 
 * @author limb
 * @date 2016年4月25日
 */
public class TradeOrdersTypeSumVo extends BaseVo {
	private static final long serialVersionUID = -8499915602079090164L;
	
	private String ordersType;
	private int staffNum;
	private long payAmount;
	private double avgPayAmount;

	public String getOrdersType() {
		return ordersType;
	}
	public void setOrdersType(String ordersType) {
		this.ordersType = ordersType;
	}
	public int getStaffNum() {
		return staffNum;
	}
	public void setStaffNum(int staffNum) {
		this.staffNum = staffNum;
	}
	public long getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}
	public double getAvgPayAmount() {
		return avgPayAmount;
	}
	public void setAvgPayAmount(double setAvgPayAmount) {
		this.avgPayAmount = setAvgPayAmount;
	}
}
