package com.ai.analy.vo.flow.structure;

import com.ai.analy.constants.Constants;
import com.ai.analy.vo.BaseVo;

public class FlowUserTypeVo extends BaseVo {

	private static final long serialVersionUID = 1292755007664008859L;
	private String userType;
	@SuppressWarnings("unused")
	private String userTypeName;
	private int pv;
	private int uv;
	private int orderUv;
	private long staySeconds;
	private int typedValueSum;
	// uv百分比
	private double uvPercent;
	// 下单数占比
	private double orderUvPercent;
	// 下单转化率
	private double orderRate;
	// 自主或外链访问数
	private long sessions;
	
	// uv占比， uv/店铺总uv
	private double uvPercentAll;

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getOrderUv() {
		return orderUv;
	}

	public void setOrderUv(int orderUv) {
		this.orderUv = orderUv;
	}

	public long getStaySeconds() {
		return staySeconds;
	}

	public void setStaySeconds(long staySeconds) {
		this.staySeconds = staySeconds;
	}

	public int getTypedValueSum() {
		return typedValueSum;
	}

	public void setTypedValueSum(int typedValueSum) {
		this.typedValueSum = typedValueSum;
	}

	public double getUvPercent() {
		return uvPercent;
	}

	public void setUvPercent(double uvPercent) {
		this.uvPercent = uvPercent;
	}

	public double getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(double orderRate) {
		this.orderRate = orderRate;
	}

	public String getUserTypeName() {
		return Constants.UserType.getUserTypeName(userType);
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public double getOrderUvPercent() {
		return orderUvPercent;
	}

	public void setOrderUvPercent(double orderUvPercent) {
		this.orderUvPercent = orderUvPercent;
	}

	public long getSessions() {
		return sessions;
	}

	public void setSessions(long sessions) {
		this.sessions = sessions;
	}

	public double getUvPercentAll() {
		return uvPercentAll;
	}

	public void setUvPercentAll(double uvPercentAll) {
		this.uvPercentAll = uvPercentAll;
	}
}
