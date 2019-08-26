package com.ai.analy.vo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 107593624225904617L;
	
	private String shopId;
	private String shopName;
	private String shopProvinceCode;
	
	private Integer pageNo = 1;
	
	private Integer pageSize = 10;
	
	public String getShopProvinceCode() {
		return shopProvinceCode;
	}
	public void setShopProvinceCode(String shopProvinceCode) {
		this.shopProvinceCode = shopProvinceCode;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
		}
		return super.toString();
	}
}
