package com.ai.analy.vo.flow.structure;

import com.ai.analy.vo.BaseVo;

public class FlowUserDaysVo extends BaseVo {

	private static final long serialVersionUID = 7557848901330859076L;
	// 访问频次类型 1,2,3,4,5,5次以上
	private String visitType;
	private int uv;
	// uv百分比
	private double uvPercent;
	// 下单转化率
	private double orderRate;

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public double getUvPercent() {
		return uvPercent;
	}

	public void setUvPercent(double uvPercent) {
		this.uvPercent = uvPercent;
	}

	public double getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(double orderRate) {
		this.orderRate = orderRate;
	}

}
