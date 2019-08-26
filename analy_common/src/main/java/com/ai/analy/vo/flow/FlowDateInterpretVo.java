package com.ai.analy.vo.flow;

import com.ai.analy.vo.BaseVo;

/**
 * 数据解读Vo
 * @author xiongqian
 *
 */
public class FlowDateInterpretVo extends BaseVo{

	private static final long serialVersionUID = -4886591886611080765L;

	/**
	 * 交易波动解读
	 */
	//近7天访客数
	private int uv;
	//较上7天增加数据
	private int increaseUv;
	
	/**
	 * 转化率解读
	 */
	//近7天访客集中于 省份
	private String provinceName;
	//日均Pv
	private int dateAvgUv;
	//新访客占比
	private double newUvScale;
	//pc端占比
	private double pcUvScale;
	//访客主要来源界面
	private String sourcePageName;//例：sourcePageName = "首页楼层"
	//访客主要去向界面
	private String destPageName;//例：destPageName = "活动专区页面"
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	public int getIncreaseUv() {
		return increaseUv;
	}
	public void setIncreaseUv(int increaseUv) {
		this.increaseUv = increaseUv;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public int getDateAvgUv() {
		return dateAvgUv;
	}
	public void setDateAvgUv(int dateAvgUv) {
		this.dateAvgUv = dateAvgUv;
	}
	public double getNewUvScale() {
		return newUvScale;
	}
	public void setNewUvScale(double newUvScale) {
		this.newUvScale = newUvScale;
	}
	public double getPcUvScale() {
		return pcUvScale;
	}
	public void setPcUvScale(double pcUvScale) {
		this.pcUvScale = pcUvScale;
	}
	public String getSourcePageName() {
		return sourcePageName;
	}
	public void setSourcePageName(String sourcePageName) {
		this.sourcePageName = sourcePageName;
	}
	public String getDestPageName() {
		return destPageName;
	}
	public void setDestPageName(String destPageName) {
		this.destPageName = destPageName;
	}
}