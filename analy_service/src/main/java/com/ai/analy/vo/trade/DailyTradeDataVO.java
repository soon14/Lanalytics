package com.ai.analy.vo.trade;

import java.io.Serializable;

public class DailyTradeDataVO implements Serializable,Comparable<DailyTradeDataVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dataDate;
	
	private String orderAmount="0";
	
	private String paidAmount="0";
	
	private int orderCount;
	
	private int payCount;
	
	private String ordSuccRate="0";
	
	

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getPayCount() {
		return payCount;
	}

	public void setPayCount(int payCount) {
		this.payCount = payCount;
	}

	public String getOrdSuccRate() {
		return ordSuccRate;
	}

	public void setOrdSuccRate(String ordSuccRate) {
		this.ordSuccRate = ordSuccRate;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Override
	public int compareTo(DailyTradeDataVO o) {
		String d1=dataDate;
		String d2=o.getDataDate();
		if (d1==null) {
			return -1;
		}
		if (d2==null) {
			return 1;
		}
		return d2.compareTo(d1);
	}
	
	
}
