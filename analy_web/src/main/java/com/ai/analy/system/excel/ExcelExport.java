package com.ai.analy.system.excel;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelExport {
	/**
	 * 默认每个sheet页最多显示的行数
	 */
	private static final int sheet_rows = 50000;

	/**
	 * 导出Excel文件
	 * 
	 * @param titleList
	 *            表头信息
	 * @param dataList
	 *            表格数据
	 * @param fileName
	 *            导出文件完整名称 demo.xls
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void exportExcelFile(List<String> titleList,
			List<List<String>> dataList, String fileName,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HSSFWorkbook workBook = exportDataToExcel(titleList, dataList);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("UTF-8");
		fileName = encodeFilename(fileName, request);
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		workBook.write(response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	public static String encodeFilename(String filename, HttpServletRequest request) {
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && ( 0 <= agent.indexOf("Firefox"))) {
				return MimeUtility.encodeText(filename, "UTF-8", "B");
			} else if((agent != null) && ( 0 <= agent.indexOf("Chrome"))){
				
				return filename = new String(filename.getBytes(), "ISO8859-1");   
			}
			else {

				if (agent != null) {

					String newFileName = URLEncoder.encode(filename, "UTF-8");
					newFileName = StringUtils.replace(newFileName, "+", "%20");
					if (newFileName.length() > 150) {
						newFileName = new String(filename.getBytes("GB2312"),
								"ISO8859-1");
						newFileName = StringUtils.replace(newFileName, " ",
								"%20");
					}
					return newFileName;
				}
			}
		} catch (Exception ex) {
			return filename;
		}
		return filename;
	}

	public static HSSFWorkbook exportDataToExcel(List<String> titleList, List<List<String>> dataList) {

		/* 1.创建一个Excel文件 */
		HSSFWorkbook workbook = new HSSFWorkbook();
		/* 2.创建Excel的一个Sheet */
		HSSFSheet sheet = workbook.createSheet();
		/* 3.创建表头冻结 */
		sheet.createFreezePane(0, 1);
		/* 4.设置列宽 */
		for (int i = 0; i < titleList.size(); i++) {
			sheet.setColumnWidth(i, 5000);
		}
		/* 5.表头字体 */
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 12);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		/* 6.表头样式 */
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		// 设置背景色为蓝色
		headstyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		headstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		/* 7.普通单元格字体 */
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		/* 8.普通单元格样式 */
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		/* 9. 拼装表头 */
		Iterator<String> titleRowIterator = titleList.iterator();
		int columnIndex = 0;
		HSSFRow row = sheet.createRow(0);
		while (titleRowIterator.hasNext()) {
			String cellValue = titleRowIterator.next();
			HSSFCell cell = row.createCell(columnIndex);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cellValue);
			cell.setCellStyle(headstyle);
			columnIndex++;
			cell = null;
		}
		/* 10.组织表数据 */
		Iterator<List<String>> rowIterator = dataList.iterator();
		int rowIndex = 1;
		while (rowIterator.hasNext()) {
			List<String> columnList = rowIterator.next();
			row = sheet.createRow(rowIndex);
			Iterator<String> columnIterator = columnList.iterator();
			columnIndex = 0;
			while (columnIterator.hasNext()) {
				String cellValue = columnIterator.next();
				HSSFCell cell = row.createCell(columnIndex);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cellValue);
				cell.setCellStyle(style);
				cell = null;
				columnIndex++;
			}
			row = null;
			rowIndex++;
		}
		return workbook;
	}
	
	/**
	 * 重载导出数据到Excel中
	 * @param titleList 表头
	 * @param dataList 表数据
	 * @param amount 每个sheet页显示行数
	 * @return
	 */
	public static HSSFWorkbook exportDataToExcel(List<String> titleList, List<List<String>> dataList, int amount) {

		/* 1.创建一个Excel文件 */
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		//校验传入的参数
		if(titleList==null){
			titleList = new ArrayList<String>();
		}
		//无数据直接返回
		if(dataList==null || dataList.size()==0){
			return workbook;
		}
		//传入数据不正确，按照默认条数显示
		if(amount>65535 || amount<=0){
			amount = sheet_rows;
		}
		
		//获取sheet页的数量
		int row_num = 0;
		int y = dataList.size()%amount;
		if(y == 0){
			row_num = dataList.size()/amount;
		}else{
			row_num = dataList.size()/amount + 1;
		}
		
		/* 表头字体 */
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 12);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		/* 表头样式 */
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		// 设置背景色为蓝色
		headstyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		headstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		/* 普通单元格字体 */
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);
		/* 普通单元格样式 */
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		
		//循环写入每个sheet页
		for(int i=0;i<row_num;i++){
			/* 创建Excel的一个Sheet */
			HSSFSheet sheet = workbook.createSheet();
			/* 创建表头冻结 */
			sheet.createFreezePane(0, 1);
			/* 设置列宽 */
			for (int t = 0; t < titleList.size(); t++) {
				sheet.setColumnWidth(t, 5000);
			}
			/* 拼装表头 */
			Iterator<String> titleRowIterator = titleList.iterator();
			int columnIndex = 0;
			HSSFRow row = sheet.createRow(0);
			while (titleRowIterator.hasNext()) {
				String cellValue = titleRowIterator.next();
				HSSFCell cell = row.createCell(columnIndex);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(cellValue);
				cell.setCellStyle(headstyle);
				columnIndex++;
				cell = null;
			}
			/* 组织表数据 */
			int rowIndex = 1;
			for (int j=amount*i;(j<amount*(i+1)&&j<dataList.size());j++) {
				List<String> columnList = dataList.get(j);
				row = sheet.createRow(rowIndex);
				Iterator<String> columnIterator = columnList.iterator();
				columnIndex = 0;
				while (columnIterator.hasNext()) {
					String cellValue = columnIterator.next();
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(cellValue);
					cell.setCellStyle(style);
					cell = null;
					columnIndex++;
				}
				row = null;
				rowIndex++;
			}
		}
		return workbook;
	}
	
	/**
	 * 重载导出Excel功能,新增一项amount每个sheet导出记录行数
	 * @param titleList
	 * @param dataList
	 * @param fileName
	 * @param amount 行数如果小于等于0或大于65535则按照默认显示
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void exportExcelFile(List<String> titleList,
			List<List<String>> dataList, String fileName, int amount,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HSSFWorkbook workBook = exportDataToExcel(titleList, dataList, amount);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("UTF-8");
		fileName = encodeFilename(fileName, request);
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		workBook.write(response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
}
