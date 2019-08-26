package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

public class FlowExitPageVo extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7453605370958999289L;

	private String pageUrl;
	private int spv;
	private int sexitpv;
	private double percent;
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public int getSpv() {
		return spv;
	}
	public void setSpv(int spv) {
		this.spv = spv;
	}
	public int getSexitpv() {
		return sexitpv;
	}
	public void setSexitpv(int sexitpv) {
		this.sexitpv = sexitpv;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
}
