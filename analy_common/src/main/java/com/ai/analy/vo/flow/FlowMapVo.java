package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

/**
 * 流量地图
 * @author xiongqian
 *
 */
public class FlowMapVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7853783796265323525L;

	//省份编码
	private String provinceCode;
	//省份名称
	private String provinceName;
	//访客数
	private int pv;
	
	private int uv;
	
	//占比
	private double scale;
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
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
	
}
