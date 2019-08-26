package com.ai.analy.vo.trade;

import java.util.Date;

import com.ai.analy.vo.BaseVo;

/**
 * 交易地域分布
 * @author xiongqian
 *
 */
public class TradeRankVo extends BaseVo{

/**
	 * 
	 */
	private static final long serialVersionUID = 1931444071709464708L;

    private String shopName;
    private String shopLogo;
	
    //商品类目ID
    private String catId;
    //商品类目名称
    private String catName;
    
	//开始时间
	private Date dateFrom;
	//结束时间
	private Date dateTo;
	//交易额排名
	private int amountRank = 0;
	//销量排名
	private int countRank = 0;
	//交易额层级
	private double amountLevel;
	//销量层级
	private double countLevel;
	//商品交易量
	private long tradeCount;
	//商品交易额
	private long tradeAmount;
	//较上期
	private double countCompare;
	//较上期
	private double amountCompare;
	
	public String getShopLogo() {
		return shopLogo;
	}
	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public int getAmountRank() {
		return amountRank;
	}
	public void setAmountRank(int amountRank) {
		this.amountRank = amountRank;
	}
	public int getCountRank() {
		return countRank;
	}
	public void setCountRank(int countRank) {
		this.countRank = countRank;
	}
	public double getAmountLevel() {
		return amountLevel;
	}
	public void setAmountLevel(double amountLevel) {
		this.amountLevel = amountLevel;
	}
	public double getCountLevel() {
		return countLevel;
	}
	public void setCountLevel(double countLevel) {
		this.countLevel = countLevel;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
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
	public double getCountCompare() {
		return countCompare;
	}
	public void setCountCompare(double countCompare) {
		this.countCompare = countCompare;
	}
	public double getAmountCompare() {
		return amountCompare;
	}
	public void setAmountCompare(double amountCompare) {
		this.amountCompare = amountCompare;
	}
}
