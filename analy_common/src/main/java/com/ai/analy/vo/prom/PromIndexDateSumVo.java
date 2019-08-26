package com.ai.analy.vo.prom;

import com.ai.analy.vo.BaseVo;

/**
 * 促销、促销商品指标日期汇总Vo
 * 
 * @author limb
 * @date 2016年8月30日
 */
public class PromIndexDateSumVo extends BaseVo {
	private static final long serialVersionUID = -2366009548575659886L;

	// 日期
	private String dt;
	// 访客数
	private int pv = 0;
	// 浏览量
	private int uv = 0;
	// 跳失率
	private double exitRate = 0;
	// 下单转化率
	private double orderUvRate = 0;
	// 平均下单件数
	private double orderCountAvg = 0;
	// 交易量
	private long payCount = 0;
	// 交易额
	private double payMoney = 0;

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
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

	public double getOrderCountAvg() {
		return orderCountAvg;
	}

	public void setOrderCountAvg(double orderCountAvg) {
		this.orderCountAvg = orderCountAvg;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}
}
