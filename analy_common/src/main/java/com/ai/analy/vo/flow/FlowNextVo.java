package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

public class FlowNextVo extends BaseVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6155661660396073105L;
	private String nextPageName;
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

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public String getNextPageName() {
		return nextPageName;
	}

	public void setNextPageName(String nextPageName) {
		this.nextPageName = nextPageName;
	}
}
