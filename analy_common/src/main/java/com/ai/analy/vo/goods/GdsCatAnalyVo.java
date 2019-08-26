package com.ai.analy.vo.goods;

import java.util.List;

import com.ai.analy.vo.BaseVo;

public class GdsCatAnalyVo extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8930455520464260520L;
	
	private String categoryType;		// 分类类型   1:商品类目  2:终端品牌 3：价格区间
	private String categoryTypeName;	// 分类名称   
	private String catId;				// 商品类别ID
	private String catName;				// 商品类别名称
	private String brandId;             //商品分类ID
	private long gdsCount;				// 商品数
	private long uv;					// 访客数
	private long pv;					// 浏览量
	private long salesVolumn;			// 销售量
	private double tradeMoney;			// 交易额
	private double payRate;				// 支付转化率
	private List<GdsBrandVo> brands;
	private boolean hasLowerBrand;
	
	private int catGroupSize = 0; //用于界面分组展示

	public int getCatGroupSize() {
		return catGroupSize;
	}

	public void setCatGroupSize(int catGroupSize) {
		this.catGroupSize = catGroupSize;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getCategoryTypeName() {
		return categoryTypeName;
	}

	public void setCategoryTypeName(String categoryTypeName) {
		this.categoryTypeName = categoryTypeName;
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

	public long getGdsCount() {
		return gdsCount;
	}

	public void setGdsCount(long gdsCount) {
		this.gdsCount = gdsCount;
	}

	public long getUv() {
		return uv;
	}

	public void setUv(long uv) {
		this.uv = uv;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public long getSalesVolumn() {
		return salesVolumn;
	}

	public void setSalesVolumn(long salesVolumn) {
		this.salesVolumn = salesVolumn;
	}

	public double getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(double tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public double getPayRate() {
		return payRate;
	}

	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public List<GdsBrandVo> getBrands() {
		return brands;
	}

	public void setBrands(List<GdsBrandVo> brands) {
		this.brands = brands;
	}

	public boolean isHasLowerBrand() {
		return hasLowerBrand;
	}

	public void setHasLowerBrand(boolean hasLowerBrand) {
		this.hasLowerBrand = hasLowerBrand;
	}

}
