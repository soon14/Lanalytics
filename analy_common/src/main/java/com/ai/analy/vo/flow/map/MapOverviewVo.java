package com.ai.analy.vo.flow.map;

import com.ai.analy.vo.BaseVo;

/**
 * 流量地图流量总览
 * @author 熊谦
 *
 */
public class MapOverviewVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4232319193958867265L;

	/**
	 * 
	 */
	//访客数
	private int pv = 0;
	//访客数占比
	private double pvCompare = 0;
	//贡献浏览量
	private int contributeUv = 0;
	
	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public double getPvCompare() {
		return pvCompare;
	}
	public void setPvCompare(double pvCompare) {
		this.pvCompare = pvCompare;
	}
	public int getContributeUv() {
		return contributeUv;
	}
	public void setContributeUv(int contributeUv) {
		this.contributeUv = contributeUv;
	}
}
