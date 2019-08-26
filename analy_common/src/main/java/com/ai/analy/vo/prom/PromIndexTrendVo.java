package com.ai.analy.vo.prom;

import java.util.ArrayList;
import java.util.List;

import com.ai.analy.vo.BaseVo;

/**
 * 促销、促销商品趋势vo
 * 
 * @author limb
 * @date 2016年8月26日
 */
public class PromIndexTrendVo extends BaseVo {
	private static final long serialVersionUID = -6331404710624723431L;

	// 商品访客数
	private List<Integer> pvs = new ArrayList<Integer>();
	// 商品浏览量
	private List<Integer> uvs = new ArrayList<Integer>();
	// 跳失率
	private List<Double> exitRates = new ArrayList<Double>();
	// 下单转化率
	private List<Double> orderUvRates = new ArrayList<Double>();
	// 平均下单件数
	private List<Double> orderCountAvgs = new ArrayList<Double>();
	// 交易量
	private List<Long> payCounts = new ArrayList<Long>();
	// 跳失率
	private List<Double> payMoneys = new ArrayList<Double>();

	private List<String> xAxis = new ArrayList<String>();

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

	public List<Double> getExitRates() {
		return exitRates;
	}

	public void setExitRates(List<Double> exitRates) {
		this.exitRates = exitRates;
	}

	public List<Double> getOrderUvRates() {
		return orderUvRates;
	}

	public void setOrderUvRates(List<Double> orderUvRates) {
		this.orderUvRates = orderUvRates;
	}

	public List<Double> getOrderCountAvgs() {
		return orderCountAvgs;
	}

	public void setOrderCountAvgs(List<Double> orderCountAvgs) {
		this.orderCountAvgs = orderCountAvgs;
	}

	public List<Long> getPayCounts() {
		return payCounts;
	}

	public void setPayCounts(List<Long> payCounts) {
		this.payCounts = payCounts;
	}

	public List<Double> getPayMoneys() {
		return payMoneys;
	}

	public void setPayMoneys(List<Double> payMoneys) {
		this.payMoneys = payMoneys;
	}

	public List<String> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}
}
