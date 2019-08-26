package com.ai.analy.vo.ad;

import java.util.ArrayList;
import java.util.List;

import com.ai.analy.vo.BaseVo;

/**
 * 广告指标趋势vo
 * 
 * @author limb
 * @date 2016年8月26日
 */
public class AdIndexTrendVo extends BaseVo {
	private static final long serialVersionUID = -6331404710624723431L;

	// 访客数
	private List<Integer> pvs = new ArrayList<Integer>();
	// 访问量
	private List<Integer> uvs = new ArrayList<Integer>();
	// 下单率
	private List<Double> orderUvRates = new ArrayList<Double>();
	// 支付成功率
	private List<Double> payUvRates = new ArrayList<Double>();
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

	public List<Double> getOrderUvRates() {
		return orderUvRates;
	}

	public void setOrderUvRates(List<Double> orderUvRates) {
		this.orderUvRates = orderUvRates;
	}

	public List<Double> getPayUvRates() {
		return payUvRates;
	}

	public void setPayUvRates(List<Double> payUvRates) {
		this.payUvRates = payUvRates;
	}

	public List<String> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}
}
