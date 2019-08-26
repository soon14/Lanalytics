package com.ai.analy.vo.flow.map;

import com.ai.analy.vo.BaseVo;

/**
 * 流量地图流量来源Vo
 * @author 熊谦
 *
 */
public class MapSourceVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3486114653109085263L;

	//来源种类
	private String pvType;
	
	//访客数
	private int pv = 0;
	//访客数 环比
	private double pvScale = 0;
	
	//下单数
	private int orderCount = 0;
	//下单数 环比
	private double orderScale = 0;
	
	//下单转化率
	private double orderRate = 0;
	//下单转化率 环比
	private double rateScale = 0;
	public String getPvType() {
		return pvType;
	}
	public void setPvType(String pvType) {
		this.pvType = pvType;
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
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public double getOrderScale() {
		return orderScale;
	}
	public void setOrderScale(double orderScale) {
		this.orderScale = orderScale;
	}
	public double getOrderRate() {
		return orderRate;
	}
	public void setOrderRate(double orderRate) {
		this.orderRate = orderRate;
	}
	public double getRateScale() {
		return rateScale;
	}
	public void setRateScale(double rateScale) {
		this.rateScale = rateScale;
	}
}
