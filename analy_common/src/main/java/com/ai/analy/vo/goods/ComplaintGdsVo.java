package com.ai.analy.vo.goods;

import com.ai.analy.vo.BaseVo;

public class ComplaintGdsVo extends BaseVo {

	private static final long serialVersionUID = 9177435971208393811L;

	private long complaintAmount;		// 店铺投诉量
	private long complaintAmountPlat;	// 平台投诉量
	private double rateShop;			// 店铺投诉率
	private double ratePlat;			// 平台投诉率
	private double avgDaysShop;			// 店铺投诉平均处理天数
	private double avgDaysPlat;			// 平台投诉平均处理天数

	public long getComplaintAmount() {
		return complaintAmount;
	}

	public void setComplaintAmount(long complaintAmount) {
		this.complaintAmount = complaintAmount;
	}

	public double getRateShop() {
		return rateShop;
	}

	public void setRateShop(double rateShop) {
		this.rateShop = rateShop;
	}

	public double getRatePlat() {
		return ratePlat;
	}

	public void setRatePlat(double ratePlat) {
		this.ratePlat = ratePlat;
	}

	public double getAvgDaysShop() {
		return avgDaysShop;
	}

	public void setAvgDaysShop(double avgDaysShop) {
		this.avgDaysShop = avgDaysShop;
	}

	public double getAvgDaysPlat() {
		return avgDaysPlat;
	}

	public void setAvgDaysPlat(double avgDaysPlat) {
		this.avgDaysPlat = avgDaysPlat;
	}

	public long getComplaintAmountPlat() {
		return complaintAmountPlat;
	}

	public void setComplaintAmountPlat(long complaintAmountPlat) {
		this.complaintAmountPlat = complaintAmountPlat;
	}
	
}
