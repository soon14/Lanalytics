package com.ai.analy.vo.flow;

import java.util.ArrayList;
import java.util.List;

import com.ai.analy.vo.BaseVo;

/**
 * 流量趋势Vo
 * @author xiongqian
 *
 */
public class FlowTrendVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7853783796265323525L;

	//访客数
	private List<Integer> pvs = new ArrayList<Integer>();
	//浏览量
	private List<Integer> uvs= new ArrayList<Integer>();
	//新注册用户数
	private List<Integer> registerStaffs = new ArrayList<Integer>();
	//跳失率
	private List<Double> bounceRates= new ArrayList<Double>();
	//平均浏览页面
	private List<Double> avgPvs= new ArrayList<Double>();
	//平均停留时间（秒）
	private List<Double> avgStays= new ArrayList<Double>();
	
	private List<String> xAxis= new ArrayList<String>();

	public List<Integer> getPvs() {
		return pvs;
	}

	public void setPvs(List<Integer> pvs) {
		this.pvs = pvs;
	}

	public List<Integer> getUvs() {
		return uvs;
	}

	public void setUvs(List<Integer> uvs) {
		this.uvs = uvs;
	}

	public List<Integer> getRegisterStaffs() {
		return registerStaffs;
	}

	public void setRegisterStaffs(List<Integer> registerStaffs) {
		this.registerStaffs = registerStaffs;
	}

	public List<Double> getBounceRates() {
		return bounceRates;
	}

	public void setBounceRates(List<Double> bounceRates) {
		this.bounceRates = bounceRates;
	}

	public List<Double> getAvgPvs() {
		return avgPvs;
	}

	public void setAvgPvs(List<Double> avgPvs) {
		this.avgPvs = avgPvs;
	}

	public List<Double> getAvgStays() {
		return avgStays;
	}

	public void setAvgStays(List<Double> avgStays) {
		this.avgStays = avgStays;
	}

	public List<String> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}
	
}
