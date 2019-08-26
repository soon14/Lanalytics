package com.ai.analy.vo.comm;

import com.ai.analy.vo.BaseVo;

/**
 * 站点信息vo
 * 
 * @author limb
 * @date 2016年9月5日
 */
public class SiteInfoVo extends BaseVo {
	private static final long serialVersionUID = -7478889645693610335L;
	
	private int id;
	private String siteName;
	private String siteUrl;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
