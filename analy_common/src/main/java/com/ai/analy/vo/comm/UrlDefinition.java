package com.ai.analy.vo.comm;

import com.ai.analy.vo.BaseVo;

public class UrlDefinition extends BaseVo {

	private static final long serialVersionUID = 7939104734843075964L;

	private long idx;
	private String url;
	private String code;
	private String name;
	private String enable;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public long getIdx() {
		return idx;
	}

	public void setIdx(long idx) {
		this.idx = idx;
	}

}
