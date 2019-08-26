package com.ai.analy.vo.comm;

import java.util.ArrayList;
import java.util.List;

import com.ai.analy.vo.BaseVo;

public class ExcelDataVo extends BaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2945715675834627924L;
    
	private String fileName;
	private List<String> titleList;
	private List<List<String>> dataList;
	
	public ExcelDataVo(){
		titleList = new ArrayList<String>();
		dataList = new ArrayList<List<String>>();
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<String> getTitleList() {
		return titleList;
	}
	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}
	public List<List<String>> getDataList() {
		return dataList;
	}
	public void setDataList(List<List<String>> dataList) {
		this.dataList = dataList;
	}
	
	public void addTitle(String title){
		titleList.add(title);
	}
	
	public void addData(List<String> data){
		dataList.add(data);
	}
	
}