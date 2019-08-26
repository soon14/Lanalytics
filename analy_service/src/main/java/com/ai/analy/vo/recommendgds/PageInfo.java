package com.ai.analy.vo.recommendgds;

public class PageInfo {

	private int totalCount;
	
	private int pageNo;
	
	private int pageSize;
	
	public PageInfo(){
		
	}

	public PageInfo(int totalCount, int pageNo, int pageSize) {
		super();
		this.totalCount = totalCount;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
}
