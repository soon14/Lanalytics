package com.ai.analy.vo.recommendgds;

import java.io.Serializable;

/**
 * 用户与商品之间的关系度
 * @author liangyi
 *
 */
public class UserGdsRelationDegreeVO implements Serializable{
	
	private static final long serialVersionUID = -2092448475156996570L;

	/**用户id*/
	private String userId;
	
	/**目标单品id*/
	private String srcSkuId;
	
	/**用户与目标单品的关系度（值越高相关度越强）*/
	private double userDegree;
	

	public UserGdsRelationDegreeVO(){
		
	}
	
	public UserGdsRelationDegreeVO(String userId, String srcSkuId,
			double userDegree) {
		super();
		this.userId = userId;
		this.srcSkuId = srcSkuId;
		this.userDegree = userDegree;
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

	public double getUserDegree() {
		return userDegree;
	}

	public void setUserDegree(double userDegree) {
		this.userDegree = userDegree;
	}

	
}
