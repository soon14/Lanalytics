package com.ai.analy.vo.goods;

import com.ai.analy.vo.BaseVo;

public class GdsBrandVo extends BaseVo {

	private static final long serialVersionUID = -5089384616037649753L;

	private String brandId;
	private String brandName;
	private String catgParent;
	private int catgLevel;
	private boolean hasLowerBrand;
	
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
	public boolean isHasLowerBrand() {
		return hasLowerBrand;
	}
	public void setHasLowerBrand(boolean hasLowerBrand) {
		this.hasLowerBrand = hasLowerBrand;
	}
	public int getCatgLevel() {
		return catgLevel;
	}
	public void setCatgLevel(int catgLevel) {
		this.catgLevel = catgLevel;
	}
	
}
