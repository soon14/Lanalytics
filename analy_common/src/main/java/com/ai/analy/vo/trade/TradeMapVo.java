package com.ai.analy.vo.trade;

import com.ai.analy.vo.BaseVo;

/**
 * 交易地域分布
 * @author xiongqian
 *
 */
public class TradeMapVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 680835526530010358L;
	
	//省份编码
	private String provinceCode;
	
	//省份名称
	private String provinceName;
	
	//访客数
	private long uv;
	
	//商品交易量
	private long tradeCount;
	
	//商品交易额
	private long tradeAmount;
	
	//占比
	private double countScale;
	
	//占比
	private double amountScale;
    //支付转化率
	private double payUvRate;
	//客单价
	private double payPrice;
	
	public double getCountScale() {
		return countScale;
	}

	public void setCountScale(double countScale) {
		this.countScale = countScale;
	}

	public double getAmountScale() {
		return amountScale;
	}

	public void setAmountScale(double amountScale) {
		this.amountScale = amountScale;
	}

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

	public long getTradeCount() {
		return tradeCount;
	}

	public void setTradeCount(long tradeCount) {
		this.tradeCount = tradeCount;
	}

	public long getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(long tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public double getPayUvRate() {
		return payUvRate;
	}

	public void setPayUvRate(double payUvRate) {
		this.payUvRate = payUvRate;
	}

	public double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(double payPrice) {
		this.payPrice = payPrice;
	}
	
}
