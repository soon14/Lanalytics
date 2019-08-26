package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

/**
 * 流量外站访问来源vo
 * 
 * @author limb
 * @date 2016年9月3日
 */
public class FlowMapRefDomainVo extends BaseVo{
	private static final long serialVersionUID = 5064089557982186292L;
	
	private String referDomain;
	private int uv;
	private double uvScale;
	private int pv;
	private double pvScale;
	
	public String getReferDomain() {
		return referDomain;
	}
	public void setReferDomain(String referDomain) {
		this.referDomain = referDomain;
	}
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	public double getUvScale() {
		return uvScale;
	}
	public void setUvScale(double uvScale) {
		this.uvScale = uvScale;
	}
	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public double getPvScale() {
		return pvScale;
	}
	public void setPvScale(double pvScale) {
		this.pvScale = pvScale;
	}
}
