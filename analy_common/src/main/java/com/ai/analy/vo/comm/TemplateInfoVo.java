package com.ai.analy.vo.comm;

import com.ai.analy.vo.BaseVo;

/**
 * 模板vo 对应t_cms_template表
 * 
 * @author limb
 * @date 2016年9月4日
 */
public class TemplateInfoVo extends BaseVo {
	private static final long serialVersionUID = -1602696403940675661L;

	private int id;
	private int siteId;
	private String templateName;
	private String templateClass;
	private String templateFileUrl;
	private String status;

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

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateClass() {
		return templateClass;
	}

	public void setTemplateClass(String templateClass) {
		this.templateClass = templateClass;
	}

	public String getTemplateFileUrl() {
		return templateFileUrl;
	}

	public void setTemplateFileUrl(String templateFileUrl) {
		this.templateFileUrl = templateFileUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
