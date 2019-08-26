package com.ai.analy.vo.trade;

import com.ai.analy.vo.BaseVo;

/**
 * 商品分类销售vo
 * 
 * @author limb
 * @date 2016年4月24日
 */
public class TradeCategorySumVo extends BaseVo {

	private static final long serialVersionUID = 3036677313670254452L;
	// 分类ID
	private int catId;
	// 分类名称
	private String catName;
	// 父分类ID
	private int parentCatId;
	// 父分类名称
	private String parentCatName;
	// 销售数量
	private long saleCount;
	// 销售金额
	private long saleMoney;

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public int getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(int parentId) {
		this.parentCatId = parentId;
	}

	public String getParentCatName() {
		return parentCatName;
	}

	public void setParentCatName(String parentCatName) {
		this.parentCatName = parentCatName;
	}

	public long getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(long saleCount) {
		this.saleCount = saleCount;
	}

	public long getSaleMoney() {
		return saleMoney;
	}

	public void setSaleMoney(long saleMoney) {
		this.saleMoney = saleMoney;
	}

}
