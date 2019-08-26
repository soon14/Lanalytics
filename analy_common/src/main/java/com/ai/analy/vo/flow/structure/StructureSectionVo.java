package com.ai.analy.vo.flow.structure;

import com.ai.analy.vo.BaseVo;

/**
 * 地域分布vo
 * @author 熊谦
 *
 */
public class StructureSectionVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2755534179644855545L;

	//渠道类型
	private String chnlType;
	//流量量
	private int pv = 0;
	//占比
	private double scale = 0;
	//下单转化率
	private double orderRate = 0;
	//访客数
	private long uv = 0;
	//支付转化率
	private double payUvRate;
	//客单价
	private double payPrice;
	
	//商品交易量
	private long tradeCount;
	
	//商品交易额
	private double tradeAmount;
	
	//订单量
	private long orderCount;
	
	public long getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(long orderCount) {
		this.orderCount = orderCount;
	}
	public String getChnlType() {
		return chnlType;
	}
	public void setChnlType(String chnlType) {
		this.chnlType = chnlType;
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
	public long getTradeCount() {
		return tradeCount;
	}
	public void setTradeCount(long tradeCount) {
		this.tradeCount = tradeCount;
	}
	public double getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
}
