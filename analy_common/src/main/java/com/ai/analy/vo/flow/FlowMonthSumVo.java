package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

/**
 * 流量月报vo
 * 
 * @author limb
 * @date 2016年4月22日
 */
public class FlowMonthSumVo extends BaseVo {

	private static final long serialVersionUID = 4681572566823466802L;

	// 月份
	private String month;
	// 访客数
	private int pv = 0;
	//新注册用户数
	private int registerStaff = 0;
	// 浏览量
	private int uv = 0;
	// 跳失率
	private double bounceRate = 0;
	// 平均浏览页面
	private double avgPv = 0;
	// 平均停留时间（秒）
	private double avgStay = 0;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getPv() {
		return pv;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
	public int getRegisterStaff() {
		return registerStaff;
	}
	public void setRegisterStaff(int registerStaff) {
		this.registerStaff = registerStaff;
	}
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	public double getBounceRate() {
		return bounceRate;
	}
	public void setBounceRate(double bounceRate) {
		this.bounceRate = bounceRate;
	}
	public double getAvgPv() {
		return avgPv;
	}
	public void setAvgPv(double avgPv) {
		this.avgPv = avgPv;
	}
	public double getAvgStay() {
		return avgStay;
	}
	public void setAvgStay(double avgStay) {
		this.avgStay = avgStay;
	}

}
