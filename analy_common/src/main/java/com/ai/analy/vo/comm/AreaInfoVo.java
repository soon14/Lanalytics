package com.ai.analy.vo.comm;

import com.ai.analy.vo.BaseVo;

public class AreaInfoVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4360166659960818123L;

	private String areaCode;
	private String areaName;
	private String parentAreaCode;
	
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getParentAreaCode() {
		return parentAreaCode;
	}
	public void setParentAreaCode(String parentAreaCode) {
		this.parentAreaCode = parentAreaCode;
	}
}
