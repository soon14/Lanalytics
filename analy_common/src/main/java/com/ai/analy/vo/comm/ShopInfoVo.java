package com.ai.analy.vo.comm;

import com.ai.analy.vo.BaseVo;

/**
 * 店铺信息vo
 * 
 * @author limb
 * @date 2016年9月5日
 */
public class ShopInfoVo extends BaseVo{
	private static final long serialVersionUID = 6791036323121431014L;
	
	private int companyId;
	private String shopFullName;
	private String shopDesc;
	private String shopStatus;
	
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getShopFullName() {
		return shopFullName;
	}
	public void setShopFullName(String shopFullName) {
		this.shopFullName = shopFullName;
	}
	public String getShopDesc() {
		return shopDesc;
	}
	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}
	public String getShopStatus() {
		return shopStatus;
	}
	public void setShopStatus(String shopStatus) {
		this.shopStatus = shopStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
