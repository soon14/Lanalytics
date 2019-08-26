package com.ai.analy.vo.prom;

import java.util.Date;

import com.ai.analy.vo.BaseVo;

/**
 * 促销信息Vo
 * 
 * @author limb
 * @date 2016年8月30日
 */
public class PromInfoVo extends BaseVo {
	private static final long serialVersionUID = 5223023382896013345L;

	private int id;
	private int siteId;
	private String siteName;
	// private int groupId;
	private String promTypeCode;
	private String promTypeName;
	private String promClass;
	private String promTheme;
	private String promContent;
	private String priority;
	private String status;
	private Date startTime;
	private Date endTime;
	private Date showStartTime;
	private Date showEndTime;
	private String shopId;
	private String joinRange;
	private String ifMatch;
	private String ifBlackList;
	private String ifComposit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getPromTypeCode() {
		return promTypeCode;
	}

	public void setPromTypeCode(String promTypeCode) {
		this.promTypeCode = promTypeCode;
	}

	public String getPromClass() {
		return promClass;
	}

	public void setPromClass(String promClass) {
		this.promClass = promClass;
	}

	public String getPromTheme() {
		return promTheme;
	}

	public void setPromTheme(String promTheme) {
		this.promTheme = promTheme;
	}

	public String getPromContent() {
		return promContent;
	}

	public void setPromContent(String promContent) {
		this.promContent = promContent;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getShowStartTime() {
		return showStartTime;
	}

	public void setShowStartTime(Date showStartTime) {
		this.showStartTime = showStartTime;
	}

	public Date getShowEndTime() {
		return showEndTime;
	}

	public void setShowEndTime(Date showEndTime) {
		this.showEndTime = showEndTime;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPromTypeName() {
		return promTypeName;
	}

	public void setPromTypeName(String promTypeName) {
		this.promTypeName = promTypeName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getJoinRange() {
		return joinRange;
	}

	public void setJoinRange(String joinRange) {
		this.joinRange = joinRange;
	}

	public String getIfMatch() {
		return ifMatch;
	}

	public void setIfMatch(String ifMatch) {
		this.ifMatch = ifMatch;
	}

	public String getIfBlackList() {
		return ifBlackList;
	}

	public void setIfBlackList(String ifBlackList) {
		this.ifBlackList = ifBlackList;
	}

	public String getIfComposit() {
		return ifComposit;
	}

	public void setIfComposit(String ifComposit) {
		this.ifComposit = ifComposit;
	}
}
