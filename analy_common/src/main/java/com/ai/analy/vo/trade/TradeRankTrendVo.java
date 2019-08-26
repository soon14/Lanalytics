package com.ai.analy.vo.trade;

import com.ai.analy.vo.BaseVo;

/**
 * 交易排名趋势
 * @author xiongqian
 *
 */
public class TradeRankTrendVo extends BaseVo{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1839942546082532591L;
	
	//商品类目ID
    private String catId;
    //商品类目名称
    private String catName;
    
	//开始时间
	private String date;
	
	//交易额排名
	private int amountRank = 0;
	//销量排名
	private int countRank = 0;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
}
