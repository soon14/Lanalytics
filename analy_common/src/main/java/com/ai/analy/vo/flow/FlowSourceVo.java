package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

public class FlowSourceVo extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5579170800117214200L;
	private String pageName;
	private int pv;
	private int uv;
	private double scale;

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}
}
