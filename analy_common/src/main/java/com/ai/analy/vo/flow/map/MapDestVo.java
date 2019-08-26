package com.ai.analy.vo.flow.map;

import com.ai.analy.vo.BaseVo;

/**
 * 流量地图流量去向Vo
 * @author 熊谦
 *
 */
public class MapDestVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4703975519179814015L;

	//访问页面
	private String pageName;
	//离开访客数
	private int destPv = 0;
	//离开浏览量
	private int destUv = 0;
	//去向访客数占比
	private double  destScale = 0;
	
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public int getDestPv() {
		return destPv;
	}
	public void setDestPv(int destPv) {
		this.destPv = destPv;
	}
	public int getDestUv() {
		return destUv;
	}
	public void setDestUv(int destUv) {
		this.destUv = destUv;
	}
	public double getDestScale() {
		return destScale;
	}
	public void setDestScale(double destScale) {
		this.destScale = destScale;
	}
}
