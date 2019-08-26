package com.ai.analy.vo.comm;

import com.ai.analy.vo.BaseVo;

/**
 * 模板位置vo 对应t_cms_place表
 * 
 * @author limb
 * @date 2016年9月4日
 */
public class PlaceInfoVo extends BaseVo {
	private static final long serialVersionUID = -5010218210517343943L;

	private int id;
	private int siteId;
	private int templateId;
	private String placeName;
	private String placeType;
	private int sortNo;
	private String status;
	private String remark;

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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceType() {
		return placeType;
	}

	public void setPlaceType(String placeType) {
		this.placeType = placeType;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
