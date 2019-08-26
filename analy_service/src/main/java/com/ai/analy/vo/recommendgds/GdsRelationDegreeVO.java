package com.ai.analy.vo.recommendgds;

import java.io.Serializable;

/**
 * 商品与商品之间的关系度
 * @author liangyi
 *
 */
public class GdsRelationDegreeVO implements Serializable,Comparable<GdsRelationDegreeVO>{
	
	private static final long serialVersionUID = -2092448475156996570L;
	
	/**用户id*/
	private String userId;

	/**源单品id*/
	private String srcSkuId;
	
	/**目标单品id*/
	private String targetSkuId;
	
	/**目标单品与源单品的关系度（值越高相关度越强）*/
	private double gdsDegree;
	
	/**用户与单品的相关度（值越高相关度越强）*/
	private double userGdsDegree;

	public GdsRelationDegreeVO(){
		
	}
	

	
	public GdsRelationDegreeVO(String userId, String srcSkuId,
			String targetSkuId, double gdsDegree) {
		super();
		this.userId = userId;
		this.srcSkuId = srcSkuId;
		this.targetSkuId = targetSkuId;
		this.gdsDegree = gdsDegree;
	}



	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getSrcSkuId() {
		return srcSkuId;
	}

	public void setSrcSkuId(String srcSkuId) {
		this.srcSkuId = srcSkuId;
	}

	public String getTargetSkuId() {
		return targetSkuId;
	}

	public void setTargetSkuId(String targetSkuId) {
		this.targetSkuId = targetSkuId;
	}

	public double getGdsDegree() {
		return gdsDegree;
	}

	public void setGdsDegree(double gdsDegree) {
		this.gdsDegree = gdsDegree;
	}

	public double getUserGdsDegree() {
		return userGdsDegree;
	}

	public void setUserGdsDegree(double userGdsDegree) {
		this.userGdsDegree = userGdsDegree;
	}



	/**
	 * 排序，用户与单品的相关度高的在前
	 */
	@Override
	public int compareTo(GdsRelationDegreeVO o) {
		if (o==null) {
			return -1;
		}
		if (o.getUserGdsDegree()>this.userGdsDegree) {
			return 1;
		}else if (this.userGdsDegree==o.getUserGdsDegree()) {
			return 0;
		}else {
			return -1;
		}
	}
	
}
