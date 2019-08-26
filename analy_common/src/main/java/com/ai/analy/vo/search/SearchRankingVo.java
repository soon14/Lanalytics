package com.ai.analy.vo.search;

import com.ai.analy.vo.BaseVo;

public class SearchRankingVo extends BaseVo {

	private static final long serialVersionUID = -8409197480232520333L;
	
	private String keyWords;  		 // 关键字
	private long searchNum; 		 // 检索量
	private double searchNumPercent; // 百分比
	private double searchNumMom;     // 检索量环比
	private int isShowInShop;    	 // 是否店铺展现, 是：1 ，否：0
	
	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public long getSearchNum() {
		return searchNum;
	}

	public void setSearchNum(long searchNum) {
		this.searchNum = searchNum;
	}

	public double getSearchNumPercent() {
		return searchNumPercent;
	}

	public void setSearchNumPercent(double searchNumPercent) {
		this.searchNumPercent = searchNumPercent;
	}

	public double getSearchNumMom() {
		return searchNumMom;
	}

	public void setSearchNumMom(double searchNumMom) {
		this.searchNumMom = searchNumMom;
	}

	public int getIsShowInShop() {
		return isShowInShop;
	}

	public void setIsShowInShop(int isShowInShop) {
		this.isShowInShop = isShowInShop;
	}

}
