package com.ai.analy.vo.flow.structure;

import com.ai.analy.vo.BaseVo;

/**
 * 类型结构vo
 * @author 熊谦
 *
 */
public class StructureTypeVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1292755007664008859L;

	//连锁渠道与非连锁渠道
	private double[] newOldPvScales = new double[]{0,0};//[新，旧]
	//名单制非名单制客户
	private double[] listNonPvScales= new double[]{0,0};//[名单制，非名单制]
	//PC端与无线端
	private double[] pcWlPvScales= new double[]{0,0};//[PC端，无线端]
	//外链入口与自主访问
	private double[] inOutPvScales= new double[]{0,0};//[外链入口，自主访问]
	public double[] getNewOldPvScales() {
		return newOldPvScales;
	}
	public void setNewOldPvScales(double[] newOldPvScales) {
		this.newOldPvScales = newOldPvScales;
	}
	public double[] getListNonPvScales() {
		return listNonPvScales;
	}
	public void setListNonPvScales(double[] listNonPvScales) {
		this.listNonPvScales = listNonPvScales;
	}
	public double[] getPcWlPvScales() {
		return pcWlPvScales;
	}
	public void setPcWlPvScales(double[] pcWlPvScales) {
		this.pcWlPvScales = pcWlPvScales;
	}
	public double[] getInOutPvScales() {
		return inOutPvScales;
	}
	public void setInOutPvScales(double[] inOutPvScales) {
		this.inOutPvScales = inOutPvScales;
	}
}
