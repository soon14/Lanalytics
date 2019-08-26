package com.ai.analy.vo.prom;

import com.ai.analy.vo.BaseVo;

/**
 * 促销类型Vo
 * 
 * @author limb
 * @date 2016年8月30日
 */
public class PromTypeVo extends BaseVo {
	private static final long serialVersionUID = -7266106625148751533L;
	private String promTypeCode;
	private String promTypeName;
	private String nameShort;
	private String promTypeDesc;
	private String promClass;
	private String status;

	public String getPromTypeCode() {
		return promTypeCode;
	}

	public void setPromTypeCode(String promTypeCode) {
		this.promTypeCode = promTypeCode;
	}

	public String getPromTypeName() {
		return promTypeName;
	}

	public void setPromTypeName(String promTypeName) {
		this.promTypeName = promTypeName;
	}

	public String getNameShort() {
		return nameShort;
	}

	public void setNameShort(String nameShort) {
		this.nameShort = nameShort;
	}

	public String getPromTypeDesc() {
		return promTypeDesc;
	}

	public void setPromTypeDesc(String promTypeDesc) {
		this.promTypeDesc = promTypeDesc;
	}

	public String getPromClass() {
		return promClass;
	}

	public void setPromClass(String promClass) {
		this.promClass = promClass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
