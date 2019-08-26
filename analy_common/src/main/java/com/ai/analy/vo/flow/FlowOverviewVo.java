package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

/**
 * 流量总览
 * @author 熊谦
 *
 */
public class FlowOverviewVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6693421584782321158L;
	
	/**
	 * 规模
	 */
	//访客数
	private int pv = 0;
	//pv较上期增减占比
	private double pvCompare = 0;
	
	//浏览量
	private int uv = 0;
	//uv较上期增减占比
	private double uvCompare= 0;
	
	//跳失率
	private double bounceRate= 0;
	//跳失率较上期增减占比
	private double bounceRateCompare= 0;
	
	//平均浏览页面
	private double avgPv= 0;
	//平均浏览页面较上期增减占比
	private double avgPvCompare= 0;
	
	//平均停留时间（秒）
	private double avgStay= 0;
	//平均停留时间较上期增减占比
	private double avgStayCompare= 0;
	
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
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	public double getUvCompare() {
		return uvCompare;
	}
	public void setUvCompare(double uvCompare) {
		this.uvCompare = uvCompare;
	}
	public double getBounceRate() {
		return bounceRate;
	}
	public void setBounceRate(double bounceRate) {
		this.bounceRate = bounceRate;
	}
	public double getBounceRateCompare() {
		return bounceRateCompare;
	}
	public void setBounceRateCompare(double bounceRateCompare) {
		this.bounceRateCompare = bounceRateCompare;
	}
	public double getAvgPv() {
		return avgPv;
	}
	public void setAvgPv(double avgPv) {
		this.avgPv = avgPv;
	}
	public double getAvgPvCompare() {
		return avgPvCompare;
	}
	public void setAvgPvCompare(double avgPvCompare) {
		this.avgPvCompare = avgPvCompare;
	}
	public double getAvgStay() {
		return avgStay;
	}
	public void setAvgStay(double avgStay) {
		this.avgStay = avgStay;
	}
	public double getAvgStayCompare() {
		return avgStayCompare;
	}
	public void setAvgStayCompare(double avgStayCompare) {
		this.avgStayCompare = avgStayCompare;
	}
}
