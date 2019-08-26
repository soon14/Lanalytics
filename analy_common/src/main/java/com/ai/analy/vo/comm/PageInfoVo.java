package com.ai.analy.vo.comm;

import com.ai.analy.vo.BaseVo;

public class PageInfoVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9162384925426516080L;
	
	private String pageCode;
	private String pageName;
	private String pageUrl;
	
	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public PageInfoVo(){
		
	}
	
	public PageInfoVo(String pageCode,String pageName){
		this.pageCode = pageCode;
		this.pageName = pageName;
	}
}
