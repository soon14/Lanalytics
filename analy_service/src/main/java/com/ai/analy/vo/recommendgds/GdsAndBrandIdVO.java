package com.ai.analy.vo.recommendgds;

import java.util.Collections;
import java.util.List;

public class GdsAndBrandIdVO {
	
	private List<Goods4RecomVO> goods4RecomList;
	
	private String brandIdsInStr;
	
	public GdsAndBrandIdVO(){
		
	}
	
	public GdsAndBrandIdVO(List<Goods4RecomVO> goods4RecomList, String brandIdsInStr) {
		this.goods4RecomList = goods4RecomList;
		this.brandIdsInStr = brandIdsInStr;
	}

	public static GdsAndBrandIdVO emptyVO(){
		List<Goods4RecomVO> resultList=Collections.emptyList();
		
		return new GdsAndBrandIdVO(resultList, "-1");
	}
	
	

	public List<Goods4RecomVO> getGoods4RecomList() {
		return goods4RecomList;
	}

	public void setGoods4RecomList(List<Goods4RecomVO> goods4RecomList) {
		this.goods4RecomList = goods4RecomList;
	}

	public String getBrandIdsInStr() {
		return brandIdsInStr;
	}

	public void setBrandIdsInStr(String brandIdsInStr) {
		this.brandIdsInStr = brandIdsInStr;
	}
	
	
	
}
