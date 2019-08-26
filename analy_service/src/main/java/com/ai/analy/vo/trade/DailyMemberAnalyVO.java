package com.ai.analy.vo.trade;

import java.io.Serializable;

public class DailyMemberAnalyVO implements Serializable,Comparable<DailyMemberAnalyVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dataDate;
	
	private int memberCount;
	

	public String getDataDate() {
		return dataDate;
	}


	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}


	public int getMemberCount() {
		return memberCount;
	}


	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}


	@Override
	public int compareTo(DailyMemberAnalyVO o) {
		String d1=dataDate;
		String d2=o.getDataDate();
		if (d1==null) {
			return -1;
		}
		if (d2==null) {
			return 1;
		}
		return d2.compareTo(d1);
	}
	
	
}
