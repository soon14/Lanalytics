package com.ai.analy.vo.goods;

import com.ai.analy.vo.BaseVo;

public class GdsCategoryVo extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5089384616037649753L;

	private String catId;
	private String catName;

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

}
