package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

public class FlowSourceOrderVo extends BaseVo {

	private static final long serialVersionUID = 8831133262664697151L;
	private String referPage;
	private String pageName;
	private int uv;
	private int orderUv;
	private double orderRate;

	private double uvMom;
	private double orderUvMom;
	private double orderRateMom;

	public String getReferPage() {
		return referPage;
	}

	public void setReferPage(String referPage) {
		this.referPage = referPage;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getOrderUv() {
		return orderUv;
	}

	public void setOrderUv(int orderUv) {
		this.orderUv = orderUv;
	}

	public double getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(double orderRate) {
		this.orderRate = orderRate;
	}

	public double getUvMom() {
		return uvMom;
	}

	public void setUvMom(double uvMom) {
		this.uvMom = uvMom;
	}

	public double getOrderUvMom() {
		return orderUvMom;
	}

	public void setOrderUvMom(double orderUvMom) {
		this.orderUvMom = orderUvMom;
	}

	public double getOrderRateMom() {
		return orderRateMom;
	}

	public void setOrderRateMom(double orderRateMom) {
		this.orderRateMom = orderRateMom;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

}
