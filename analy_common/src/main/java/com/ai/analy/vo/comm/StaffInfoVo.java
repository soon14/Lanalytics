package com.ai.analy.vo.comm;

import java.util.ArrayList;
import java.util.List;

import com.ai.analy.vo.BaseVo;

public class StaffInfoVo extends BaseVo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3746802255572054988L;
	
	private String userId;
	private String userName;
	private String provinceCode;
    private boolean hasAuth = false;
	
	private List<ShopInfoVo> shops;
	
	private List<String> authUrl = new ArrayList<String>();
	
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public boolean isHasAuth() {
		return hasAuth;
	}

	public void setHasAuth(boolean hasAuth) {
		this.hasAuth = hasAuth;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ShopInfoVo> getShops() {
		return shops;
	}

	public void setShops(List<ShopInfoVo> shops) {
		this.shops = shops;
	}

	public List<String> getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(List<String> authUrl) {
		this.authUrl = authUrl;
	}
}
