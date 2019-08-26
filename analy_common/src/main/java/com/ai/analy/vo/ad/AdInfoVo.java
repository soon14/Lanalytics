package com.ai.analy.vo.ad;

import java.util.Date;

import com.ai.analy.vo.BaseVo;

/**
 * 广告信息vo
 * 
 * @author limb
 * @date 2016年9月4日
 */
public class AdInfoVo extends BaseVo {
	private static final long serialVersionUID = -7494281778683491946L;

	private int id;
	private String advertiseTitle;
	private String linkType;
	private String linkName;
	private String linkUrl;
	private String vfsName;
	private String vfsId;
	private int sortNo;
	private String status;
	private Date pubTime;
	private Date lostTime;
	private int siteId;
	private int templateId;
	private int placeId;
	private String remark;

	private String siteName;
	private String templateName;
	private String placeName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdvertiseTitle() {
		return advertiseTitle;
	}

	public void setAdvertiseTitle(String advertiseTitle) {
		this.advertiseTitle = advertiseTitle;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getVfsName() {
		return vfsName;
	}

	public void setVfsName(String vfsName) {
		this.vfsName = vfsName;
	}

	public String getVfsId() {
		return vfsId;
	}

	public void setVfsId(String vfsId) {
		this.vfsId = vfsId;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public Date getLostTime() {
		return lostTime;
	}

	public void setLostTime(Date lostTime) {
		this.lostTime = lostTime;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
}
