package com.ai.analy.vo.flow.structure;

import com.ai.analy.vo.BaseVo;

/**
 * 最近30天浏览频次分布vo
 * @author 熊谦
 *
 */
public class StructureRateVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4256014076672849863L;
	

    //浏览频次
	private int rates;
    //访客数
	private int pv;
	//占比
	private double scale = 0;
	//下单转化率
	private double orderRate = 0;
	public int getRates() {
		return rates;
	}
	public void setRates(int rates) {
		this.rates = rates;
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
	public double getOrderRate() {
		return orderRate;
	}
	public void setOrderRate(double orderRate) {
		this.orderRate = orderRate;
	}
}
