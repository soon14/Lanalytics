package com.ai.analy.vo.ad;

import com.ai.analy.vo.BaseVo;

/**
 * 广告指标日期汇总Vo
 * 
 * @author limb
 * @date 2016年8月30日
 */
public class AdIndexDateSumVo extends BaseVo {

	private static final long serialVersionUID = -2366009548575659886L;
	// 日期
	private String dt;
	// 访客数
	private int pv = 0;
	// 浏览量
	private int uv = 0;
	// 下单率
	private double orderUvRate = 0;
	// 支付成功率
	private double payUvRate = 0;

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

	public double getOrderUvRate() {
		return orderUvRate;
	}

	public void setOrderUvRate(double orderUvRate) {
		this.orderUvRate = orderUvRate;
	}

	public double getPayUvRate() {
		return payUvRate;
	}

	public void setPayUvRate(double payUvRate) {
		this.payUvRate = payUvRate;
	}
}
