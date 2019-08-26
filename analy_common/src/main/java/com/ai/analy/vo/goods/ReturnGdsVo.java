package com.ai.analy.vo.goods;

import com.ai.analy.vo.BaseVo;

public class ReturnGdsVo extends BaseVo {

	private static final long serialVersionUID = 3788505869634204110L;
	
	private long returnAmount;		// 退货量
	private long returnAmountPlat;	// 平台量
	private double rateShop;		// 店铺退货率
	private double ratePlat;		// 平台退货率
	private double avgDaysShop;		// 店铺退货平均处理天数
	private double avgDaysPlat;		// 平台退货平均处理天数

	public long getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(long returnAmount) {
		this.returnAmount = returnAmount;
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

	public long getReturnAmountPlat() {
		return returnAmountPlat;
	}

	public void setReturnAmountPlat(long returnAmountPlat) {
		this.returnAmountPlat = returnAmountPlat;
	}
	
}
