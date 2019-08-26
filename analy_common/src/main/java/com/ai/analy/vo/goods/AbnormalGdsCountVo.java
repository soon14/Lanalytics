package com.ai.analy.vo.goods;

import com.ai.analy.vo.BaseVo;

public class AbnormalGdsCountVo extends BaseVo {

	private static final long serialVersionUID = 2516882127231631579L;

	private int gdsFlowDownCoun;        // 流量下跌环数量
	private int payUvRateDownCoun;		// 支付转换率下跌数量
	private int sellZeroCoun;			// 零销量数量
	private int stockZeroCoun;			// 零库存数量
	private int stockWarningCoun;		// 库存预警数量

	public int getGdsFlowDownCoun() {
		return gdsFlowDownCoun;
	}

	public void setGdsFlowDownCoun(int gdsFlowDownCoun) {
		this.gdsFlowDownCoun = gdsFlowDownCoun;
	}

	public int getPayUvRateDownCoun() {
		return payUvRateDownCoun;
	}

	public void setPayUvRateDownCoun(int payUvRateDownCoun) {
		this.payUvRateDownCoun = payUvRateDownCoun;
	}

	public int getSellZeroCoun() {
		return sellZeroCoun;
	}

	public void setSellZeroCoun(int sellZeroCoun) {
		this.sellZeroCoun = sellZeroCoun;
	}

	public int getStockZeroCoun() {
		return stockZeroCoun;
	}

	public void setStockZeroCoun(int stockZeroCoun) {
		this.stockZeroCoun = stockZeroCoun;
	}

	public int getStockWarningCoun() {
		return stockWarningCoun;
	}

	public void setStockWarningCoun(int stockWarningCoun) {
		this.stockWarningCoun = stockWarningCoun;
	}

}
