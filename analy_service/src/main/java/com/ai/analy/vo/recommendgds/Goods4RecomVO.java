package com.ai.analy.vo.recommendgds;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Goods4RecomVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 商品ID */
	private String gdsId;
	/** 商品名称 */
	private String gdsName;
	/** 商品主图ID */
	private String mainPicId;
	/** 商品状态 */
	private String gdsStatus;
	/** 单品ID */
	private String skuId;
	/** 指导价 */
	private long guidePrice;
	/** 单品属性 */
	private List<Map<String, String>> skuProps;

	public String getGdsId() {
		return gdsId;
	}

	public void setGdsId(String gdsId) {
		this.gdsId = gdsId;
	}

	public String getGdsName() {
		return gdsName;
	}

	public void setGdsName(String gdsName) {
		this.gdsName = gdsName;
	}

	public String getMainPicId() {
		return mainPicId;
	}

	public void setMainPicId(String mainPicId) {
		this.mainPicId = mainPicId;
	}

	public String getGdsStatus() {
		return gdsStatus;
	}

	public void setGdsStatus(String gdsStatus) {
		this.gdsStatus = gdsStatus;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public long getGuidePrice() {
		return guidePrice;
	}

	public void setGuidePrice(long guidePrice) {
		this.guidePrice = guidePrice;
	}

	public List<Map<String, String>> getSkuProps() {
		return skuProps;
	}

	public void setSkuProps(List<Map<String, String>> skuProps) {
		this.skuProps = skuProps;
	}


}
