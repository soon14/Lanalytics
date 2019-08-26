package com.ai.analy.vo.goods;

import com.ai.analy.vo.BaseVo;

public class GdsSaleTrendVo extends BaseVo {
	private static final long serialVersionUID = -5200857814399092141L;
	
	private long pv;  					// 浏览量
	private long uv;					// 访客数
	private long gv;					// 被访商品数
	private long stayAvg; 				// 平均停留时间
	private double exitRate;  			// 跳失率
	private double orderUvRate; 		// 下单转换率
	private double orderPayUvRate; 		// 下单-支付转换率
	private double payUvRate; 			// 支付转换率
	private long orderMoney;			// 下单金额
	private long orderCount;			// 下单件数
	private long orderUv;				// 下单买家
	private long payMoney;				// 支付金额
	private long payCount;				// 支付件数
	private long payUv;					// 支付买家数
	private long payPrice;				// 客单价
	private long cartCount;				// 加入购物车件数
	
	private String xTime;				// 时间轴坐标
	
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

	public long getGv() {
		return gv;
	}

	public void setGv(long gv) {
		this.gv = gv;
	}

	public double getExitRate() {
		return exitRate;
	}

	public void setExitRate(double exitRate) {
		this.exitRate = exitRate;
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

	public long getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(long orderMoney) {
		this.orderMoney = orderMoney;
	}

	public long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(long orderCount) {
		this.orderCount = orderCount;
	}

	public long getOrderUv() {
		return orderUv;
	}

	public void setOrderUv(long orderUv) {
		this.orderUv = orderUv;
	}

	public long getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(long payMoney) {
		this.payMoney = payMoney;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}

	public long getPayUv() {
		return payUv;
	}

	public void setPayUv(long payUv) {
		this.payUv = payUv;
	}

	public long getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(long payPrice) {
		this.payPrice = payPrice;
	}

	public long getCartCount() {
		return cartCount;
	}

	public void setCartCount(long cartCount) {
		this.cartCount = cartCount;
	}

	public long getStayAvg() {
		return stayAvg;
	}

	public void setStayAvg(long stayAvg) {
		this.stayAvg = stayAvg;
	}

	public String getXTime() {
		return xTime;
	}

	public void setXTime(String xTime) {
		this.xTime = xTime;
	}

}
