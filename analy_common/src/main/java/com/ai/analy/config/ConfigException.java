package com.ai.analy.config;

/**
 * pass 层异常定义
 *
 */
public class ConfigException extends Exception {
	private static final long serialVersionUID = -8886495803406807620L;
	private String errorCode;
	private String errorDetail;

	public ConfigException(String errorCode, String errorDetail) {
		super(errorCode + ":" + errorDetail);
		this.errorCode = errorCode;
		this.errorDetail = errorDetail;
	}

	public ConfigException(String errorCode, Exception ex) {
		super(errorCode + ":" + errorCode, ex);
		this.errorCode = errorCode;
	}

	public ConfigException(String errorCode, String errorDetail, Exception ex) {
		super(errorCode + ":" + errorDetail, ex);
		this.errorCode = errorCode;
		this.errorDetail = errorDetail;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}

}
