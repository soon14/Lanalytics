package com.ai.analy.system.view;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.ai.analy.system.excel.ExcelExport;
import com.ai.analy.vo.comm.ExcelDataVo;

public class ExcelView extends AbstractView{

	private static final String CONTENT_TYPE = "application/vnd.ms-excel";
	private static final String EXTENSION = ".xls";
	
	public ExcelView() {
		setContentType(CONTENT_TYPE);
	}
	
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	/**
	 * Renders the Excel view, given the specified model.
	 */
	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ExcelDataVo excelData = new ExcelDataVo();
		if(model != null && model.values().isEmpty() == false){
			for (Object obj : model.values()) {
				if (obj instanceof ExcelDataVo) {
					excelData = (ExcelDataVo) obj;
					break;
				}
			}
		}
		
		String fileName = excelData.getFileName();
		//如果未空，则随机生成文件名
		if(StringUtils.isEmpty(fileName)){
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			fileName = df.format(new java.util.Date());
		}
		
		//加后缀
		String[] str = fileName.split(".");
		if(str != null && str.length>0){
			fileName = str[0];
		}
		fileName = fileName + EXTENSION;
		
		ExcelExport.exportExcelFile(excelData.getTitleList(), excelData.getDataList(),fileName,request,response);
	}
}
