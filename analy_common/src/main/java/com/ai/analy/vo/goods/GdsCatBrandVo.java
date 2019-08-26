package com.ai.analy.vo.goods;

import java.util.List;

import com.ai.analy.vo.BaseVo;

public class GdsCatBrandVo extends BaseVo {

	private static final long serialVersionUID = -5089384616037649753L;

	private String catId;
	private String catName;
	private String brandId;
	private String brandName;
	private String catgParent;
	private List<GdsBrandVo> brands;
	private int categoryLevel;
	
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
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCatgParent() {
		return catgParent;
	}
	public void setCatgParent(String catgParent) {
		this.catgParent = catgParent;
	}
	public List<GdsBrandVo> getBrands() {
		return brands;
	}
	public void setBrands(List<GdsBrandVo> brands) {
		this.brands = brands;
	}
	public int getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(int categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	
}
