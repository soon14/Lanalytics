package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

public class FlowMapOverviewVo extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9133691443602278871L;
	private String pageName;
	private String pageShowName;
	private Integer uv = 0;
	private Integer pv = 0;
	private double percent;

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageShowName() {
		return pageShowName;
	}

	public void setPageShowName(String pageShowName) {
		this.pageShowName = pageShowName;
	}
}
