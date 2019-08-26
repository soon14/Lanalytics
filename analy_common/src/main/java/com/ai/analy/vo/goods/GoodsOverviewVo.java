package com.ai.analy.vo.goods;

import com.ai.analy.vo.BaseVo;

public class GoodsOverviewVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2700291518429333994L;
	
	
	private long pv = 0;
	//pv较上期增减占比
	private double pvCompare = 0;
	
	//浏览量
	private long uv = 0;
	//uv较上期增减占比
	private double uvCompare= 0;
	
	private double gv = 0;
	
	private double gvCompare = 0;
	
	private double avgStay = 0;
	
	private double avgStayCompare;
	
	private double exitRate;
	
	private double exitRateCompare ;
	
	private long cartQuantities;
	
	private double cartQuantitiesCompare;
	
	private long payQuantities;
	
	private double payQuantitiesCompare;
	
	private long abnormals;
	
	private double abnormalsCompare;
	
	private long favors;
	
	private double favorsCompare;
	
	private long orderOver3Count;
	
	private double orderOver3CountCompare;

	//支付数
	private long payUv;
	private double payUvCompare;
	
	//下单买家数
	private long orderUv;
	private double orderUvCompare;
	
	//下单金额
	private long orderAmount;
	private double orderAmountCompare;
	
	//客单价
	private double payPrice;
	private double payPriceCompare;
	
	//支付金额
	private long payAmount;
	private double payAmountCompare;
	//下单转化率
	private double orderUvRate;
	//下单支付转化率
	private double orderPayUvRate;
	//支付转化率
	private double payUvRate;
	
	public double getPayUvCompare() {
		return payUvCompare;
	}

	public void setPayUvCompare(double payUvCompare) {
		this.payUvCompare = payUvCompare;
	}

	public double getPayPriceCompare() {
		return payPriceCompare;
	}

	public void setPayPriceCompare(double payPriceCompare) {
		this.payPriceCompare = payPriceCompare;
	}

	public double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(double payPrice) {
		this.payPrice = payPrice;
	}

	public long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public double getOrderAmountCompare() {
		return orderAmountCompare;
	}

	public void setOrderAmountCompare(double orderAmountCompare) {
		this.orderAmountCompare = orderAmountCompare;
	}

	public long getPayUv() {
		return payUv;
	}

	public void setPayUv(long payUv) {
		this.payUv = payUv;
	}

	public long getOrderUv() {
		return orderUv;
	}

	public void setOrderUv(long orderUv) {
		this.orderUv = orderUv;
	}

	public double getOrderUvCompare() {
		return orderUvCompare;
	}

	public void setOrderUvCompare(double orderUvCompare) {
		this.orderUvCompare = orderUvCompare;
	}

	public long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}

	public double getPayAmountCompare() {
		return payAmountCompare;
	}

	public void setPayAmountCompare(double payAmountCompare) {
		this.payAmountCompare = payAmountCompare;
	}

	public double getOrderUvRate() {
		return orderUvRate;
	}

	public void setOrderUvRate(double orderUvRate) {
		this.orderUvRate = orderUvRate;
	}

	public double getOrderPayUvRate() {
		return orderPayUvRate;
	}

	public void setOrderPayUvRate(double orderPayUvRate) {
		this.orderPayUvRate = orderPayUvRate;
	}

	public double getPayUvRate() {
		return payUvRate;
	}

	public void setPayUvRate(double payUvRate) {
		this.payUvRate = payUvRate;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public double getPvCompare() {
		return pvCompare;
	}

	public void setPvCompare(double pvCompare) {
		this.pvCompare = pvCompare;
	}

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public double getUvCompare() {
		return uvCompare;
	}

	public void setUvCompare(double uvCompare) {
		this.uvCompare = uvCompare;
	}

	public double getGv() {
		return gv;
	}

	public void setGv(double gv) {
		this.gv = gv;
	}

	public double getGvCompare() {
		return gvCompare;
	}

	public void setGvCompare(double gvCompare) {
		this.gvCompare = gvCompare;
	}

	public double getAvgStay() {
		return avgStay;
	}

	public void setAvgStay(double avgStay) {
		this.avgStay = avgStay;
	}

	public double getAvgStayCompare() {
		return avgStayCompare;
	}

	public void setAvgStayCompare(double avgStayCompare) {
		this.avgStayCompare = avgStayCompare;
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

	public long getCartQuantities() {
		return cartQuantities;
	}

	public void setCartQuantities(long cartQuantities) {
		this.cartQuantities = cartQuantities;
	}

	public double getCartQuantitiesCompare() {
		return cartQuantitiesCompare;
	}

	public void setCartQuantitiesCompare(double cartQuantitiesCompare) {
		this.cartQuantitiesCompare = cartQuantitiesCompare;
	}

	public long getPayQuantities() {
		return payQuantities;
	}

	public void setPayQuantities(long payQuantities) {
		this.payQuantities = payQuantities;
	}

	public double getPayQuantitiesCompare() {
		return payQuantitiesCompare;
	}

	public void setPayQuantitiesCompare(double payQuantitiesCompare) {
		this.payQuantitiesCompare = payQuantitiesCompare;
	}

	public long getAbnormals() {
		return abnormals;
	}

	public void setAbnormals(long abnormals) {
		this.abnormals = abnormals;
	}

	public double getAbnormalsCompare() {
		return abnormalsCompare;
	}

	public void setAbnormalsCompare(double abnormalsCompare) {
		this.abnormalsCompare = abnormalsCompare;
	}

	public long getFavors() {
		return favors;
	}

	public void setFavors(long favors) {
		this.favors = favors;
	}

	public double getFavorsCompare() {
		return favorsCompare;
	}

	public void setFavorsCompare(double favorsCompare) {
		this.favorsCompare = favorsCompare;
	}

	public long getOrderOver3Count() {
		return orderOver3Count;
	}

	public void setOrderOver3Count(long orderOver3Count) {
		this.orderOver3Count = orderOver3Count;
	}

	public double getOrderOver3CountCompare() {
		return orderOver3CountCompare;
	}

	public void setOrderOver3CountCompare(double orderOver3CountCompare) {
		this.orderOver3CountCompare = orderOver3CountCompare;
	}
	
}
