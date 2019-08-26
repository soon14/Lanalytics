package com.ai.analy.vo.trade;

import com.ai.analy.vo.BaseVo;
import com.ai.analy.vo.goods.GdsRankVo;

public class TradeAssociationVo extends BaseVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -959458977141548420L;

    private GdsRankVo gdsA;
    private GdsRankVo gdsB;
	
    //AB都买客人占比
	private double abScale;
	//a->b关联度
	private double aRelev;
	//b->a关联度
	private double bRelev;
	
	public GdsRankVo getGdsA() {
		return gdsA;
	}
	public void setGdsA(GdsRankVo gdsA) {
		this.gdsA = gdsA;
	}
	public GdsRankVo getGdsB() {
		return gdsB;
	}
	public void setGdsB(GdsRankVo gdsB) {
		this.gdsB = gdsB;
	}
	public double getAbScale() {
		return abScale;
	}
	public void setAbScale(double abScale) {
		this.abScale = abScale;
	}
	public double getaRelev() {
		return aRelev;
	}
	public void setaRelev(double aRelev) {
		this.aRelev = aRelev;
	}
	public double getbRelev() {
		return bRelev;
	}
	public void setbRelev(double bRelev) {
		this.bRelev = bRelev;
	}
}
