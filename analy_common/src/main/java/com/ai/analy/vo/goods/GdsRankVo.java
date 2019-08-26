package com.ai.analy.vo.goods;

import java.util.Date;

import com.ai.analy.vo.BaseVo;

public class GdsRankVo extends BaseVo {

	private static final long serialVersionUID = -638594046392685468L;
	
	private String gdsName;				// 商品名称
	private String skuProp;			    // 单品属性
	private String skuIsbn;             // ISBN
	private long uv;					// 访客量
	private double uvPercent;			// 访客量占比
	private double exitRate;			// 跳失率
	private double orderUvRate;			// 下单转换率
	private long payCount;				// 交易量
	private long payMoney;				// 交易额
	private long pv;					// 浏览量
	private double orderCountAvg;		// 平均下单数
	private long gdsId;					// 商品Id
	private long skuId;					// 单品Id
	private long price;					// 单价
	private String catId;				// 商品分类
	private String picId;				// 图片
	private String state;				// 状态
	private long stock;					// 库存
	private Date shelvesTime;			// 上架时间
	
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getStock() {
		return stock;
	}
	public void setStock(long stock) {
		this.stock = stock;
	}
	public String getSkuProp() {
		return skuProp;
	}
	public void setSkuProp(String skuProp) {
		this.skuProp = skuProp;
	}
	public long getGdsId() {
		return gdsId;
	}
	public void setGdsId(long gdsId) {
		this.gdsId = gdsId;
	}
	public long getSkuId() {
		return skuId;
	}
	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}
	public String getGdsName() {
		return gdsName;
	}
	public void setGdsName(String gdsName) {
		this.gdsName = gdsName;
	}
	public long getUv() {
		return uv;
	}
	public void setUv(long uv) {
		this.uv = uv;
	}
	public double getUvPercent() {
		return uvPercent;
	}
	public void setUvPercent(double uvPercent) {
		this.uvPercent = uvPercent;
	}
	public double getExitRate() {
		return exitRate;
	}
	public void setExitRate(double exitRate) {
		this.exitRate = exitRate;
	}
	public double getOrderUvRate() {
		return orderUvRate;
	}
	public void setOrderUvRate(double orderUvRate) {
		this.orderUvRate = orderUvRate;
	}
	public long getPayCount() {
		return payCount;
	}
	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}
	public long getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(long payMoney) {
		this.payMoney = payMoney;
	}
	public double getOrderCountAvg() {
		return orderCountAvg;
	}
	public void setOrderCountAvg(double orderCountAvg) {
		this.orderCountAvg = orderCountAvg;
	}
	public long getPv() {
		return pv;
	}
	public void setPv(long pv) {
		this.pv = pv;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public Date getShelvesTime() {
		return shelvesTime;
	}
	public void setShelvesTime(Date shelvesTime) {
		this.shelvesTime = shelvesTime;
	}
	public String getSkuIsbn() {
		return skuIsbn;
	}
	public void setSkuIsbn(String skuIsbn) {
		this.skuIsbn = skuIsbn;
	}

}
