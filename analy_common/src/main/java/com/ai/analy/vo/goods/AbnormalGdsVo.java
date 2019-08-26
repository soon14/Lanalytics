package com.ai.analy.vo.goods;

import java.util.Date;

import com.ai.analy.vo.BaseVo;

public class AbnormalGdsVo extends BaseVo {

	private static final long serialVersionUID = 2516882127231631579L;

	private String gdsName;				// 商品名称
	private String skuProp;			    // 单品属性
	private long price;					// 单价
	private String picId;				// 图片
	private String state;				// 状态
	private long stock;					// 库存
	private Date shelvesTime;			// 上架时间
	private long currPv;				// 本期浏览量
	private long lastPv;				// 上一期浏览量
	private double pvMom;				// 环比 （环比下跌50%）
	
	private long currUv;				// 本期访客
	private long lastUv;				// 上一期访客
	private double currPayUvRate;		// 本期支付转化率
	private double lastPayUvRate;		// 上一期支付转换率
	
	private long currPayAmount;			// 本期销量
	private long lastPayAmount;			// 上期销量
	
	private Date stockZeroTiem;		// 库存为零时间
	
	private String catId;
	
	public long getCurrUv() {
		return currUv;
	}

	public void setCurrUv(long currUv) {
		this.currUv = currUv;
	}

	public long getLastUv() {
		return lastUv;
	}

	public void setLastUv(long lastUv) {
		this.lastUv = lastUv;
	}

	public double getCurrPayUvRate() {
		return currPayUvRate;
	}

	public void setCurrPayUvRate(double currPayUvRate) {
		this.currPayUvRate = currPayUvRate;
	}

	public double getLastPayUvRate() {
		return lastPayUvRate;
	}

	public void setLastPayUvRate(double lastPayUvRate) {
		this.lastPayUvRate = lastPayUvRate;
	}

	public Date getStockZeroTiem() {
		return stockZeroTiem;
	}

	public void setStockZeroTiem(Date stockZeroTiem) {
		this.stockZeroTiem = stockZeroTiem;
	}

	public String getGdsName() {
		return gdsName;
	}

	public void setGdsName(String gdsName) {
		this.gdsName = gdsName;
	}

	public String getSkuProp() {
		return skuProp;
	}

	public void setSkuProp(String skuProp) {
		this.skuProp = skuProp;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

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

	public Date getShelvesTime() {
		return shelvesTime;
	}

	public void setShelvesTime(Date shelvesTime) {
		this.shelvesTime = shelvesTime;
	}

	public long getCurrPv() {
		return currPv;
	}

	public void setCurrPv(long currPv) {
		this.currPv = currPv;
	}

	public long getLastPv() {
		return lastPv;
	}

	public void setLastPv(long lastPv) {
		this.lastPv = lastPv;
	}

	public double getPvMom() {
		return pvMom;
	}

	public void setPvMom(double pvMom) {
		this.pvMom = pvMom;
	}

	public long getLastPayAmount() {
		return lastPayAmount;
	}

	public void setLastPayAmount(long lastPayAmount) {
		this.lastPayAmount = lastPayAmount;
	}

	public long getCurrPayAmount() {
		return currPayAmount;
	}

	public void setCurrPayAmount(long currPayAmount) {
		this.currPayAmount = currPayAmount;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}
	
}
