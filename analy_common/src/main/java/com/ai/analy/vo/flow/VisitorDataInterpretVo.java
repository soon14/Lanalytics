package com.ai.analy.vo.flow;

import java.util.List;

import com.ai.analy.vo.BaseVo;

public class VisitorDataInterpretVo extends BaseVo {
	private static final long serialVersionUID = 3104325161815372451L;

	private String chnlType;			// 类型渠道 T1,T2等
	private double chnlTypePercent;		// 类型渠道占比
	private String chainType;   		// 连锁渠道
	private double chainTypePercent;	// 连锁渠道占比
	private String whiteList;			// 白名单
	private double whiteListPercent;	// 白名单占比
	private String timeSegment;			// 集中浏览时间段
	private int dailyUv;				// 日均访客量
	private String thirdtyDaysTimes;			// 30天浏览频次
	private double thirdtyDaysTimesPercent;     // 30天浏览频次占比
	private List<String> timeList;

	public String getChnlType() {
		return chnlType;
	}

	public void setChnlType(String chnlType) {
		this.chnlType = chnlType;
	}

	public double getChnlTypePercent() {
		return chnlTypePercent;
	}

	public void setChnlTypePercent(double chnlTypePercent) {
		this.chnlTypePercent = chnlTypePercent;
	}

	public String getChainType() {
		return chainType;
	}

	public void setChainType(String chainType) {
		this.chainType = chainType;
	}

	public double getChainTypePercent() {
		return chainTypePercent;
	}

	public void setChainTypePercent(double chainTypePercent) {
		this.chainTypePercent = chainTypePercent;
	}

	public String getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
	}

	public double getWhiteListPercent() {
		return whiteListPercent;
	}

	public void setWhiteListPercent(double whiteListPercent) {
		this.whiteListPercent = whiteListPercent;
	}

	public String getTimeSegment() {
		return timeSegment;
	}

	public void setTimeSegment(String timeSegment) {
		this.timeSegment = timeSegment;
	}

	public int getDailyUv() {
		return dailyUv;
	}

	public void setDailyUv(int dailyUv) {
		this.dailyUv = dailyUv;
	}


	public double getThirdtyDaysTimesPercent() {
		return thirdtyDaysTimesPercent;
	}

	public void setThirdtyDaysTimesPercent(double thirdtyDaysTimesPercent) {
		this.thirdtyDaysTimesPercent = thirdtyDaysTimesPercent;
	}

	public List<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}

	public String getThirdtyDaysTimes() {
		return thirdtyDaysTimes;
	}

	public void setThirdtyDaysTimes(String thirdtyDaysTimes) {
		this.thirdtyDaysTimes = thirdtyDaysTimes;
	}

}
