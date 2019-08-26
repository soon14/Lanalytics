package com.ai.analy.vo.comm;

import java.io.Serializable;
import java.util.List;

/**
 * 分页PageBean
 * 
 * @author huangcm
 */
public class PageBean<T>  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3430384872651728450L;
	
	private int currentPage; 	// 当前页
	private int pageSize; 		// 每页显示多少条记录
	private List<T> recordList; // 分页页的数据列表
	private int recordCount; 	// 总记录数

	private int pageCount; 		// 总页数
	private int beginPageIndex; // 页码列表的开始索引（包含）
	private int endPageIndex; 	// 页码列表的结束索引（包含）

	public PageBean(){
		
	}
	
	/**
	 * 只接受4个必要的属性，会自动的计算出其他3个属性的值
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param recordList
	 * @param recordCount
	 */
	public PageBean(int currentPage, int pageSize, List<T> recordList, int recordCount) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.recordList = recordList;
		this.recordCount = recordCount;
		// 计算 pageCount
		pageCount = (recordCount + pageSize - 1) / pageSize;
		// 计算 beginPageIndex 与  endPageIndex
		if (pageCount <= 10) {
			beginPageIndex = 1;
			endPageIndex = pageCount;
		} else {
			beginPageIndex = currentPage - 4;
			endPageIndex = currentPage + 5;
			if (beginPageIndex < 1) {
				beginPageIndex = 1;
				endPageIndex = 10;
			} else if (endPageIndex > pageCount) {
				endPageIndex = pageCount;
				beginPageIndex = pageCount - 9;
			}
		}
	}

	public List<T> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<T> recordList) {
		this.recordList = recordList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getBeginPageIndex() {
		return beginPageIndex;
	}

	public void setBeginPageIndex(int beginPageIndex) {
		this.beginPageIndex = beginPageIndex;
	}

	public int getEndPageIndex() {
		return endPageIndex;
	}

	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}

}
