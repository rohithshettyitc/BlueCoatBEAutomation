package com.bluecoatcloud.threatpulse.reporting;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.bluecoatcloud.threatpulse.dataProvider.TestEnvironment;

public class Reporter {
	String contextName;
	String method;
	String methodType;
	String reponseTime;
	List<PerformanceData> performanceDataList;
	String fileName;
	String[] titles =  {"Method Type", "Method", "Response Time In ms"};
	
	public Reporter(String theFileName){
		fileName = theFileName;
	}
	
	
		
	/**
	 * read the performance data list and enter it into the file
	 */
	 public void generateReport(){
		 String file = System.getProperty("user.dir") + "\\" + fileName + "_PerfLog.xls";
		 FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		 } catch (FileNotFoundException e1) {
				e1.printStackTrace();
		 }
		 //create workbook if does not exist
		 Workbook wb = new HSSFWorkbook();
	
		 
		 performanceDataList = TestEnvironment.getTestEnvironmentObject().getPerfData();
		 if (performanceDataList!= null){
			 try{
			 for (PerformanceData reportData : performanceDataList){
				 contextName = reportData.getComponentName();
				 method = reportData.getMethod();
				 methodType =  reportData.getMethodType();
				 reponseTime = reportData.getResponseTime();
			
				 //create sheet and header
				 Sheet theSheet = _initializeSheet(wb, contextName);
				 //write perf data
				 int lastRowNum = theSheet.getLastRowNum();
				 Row row = theSheet.createRow(lastRowNum+1);
				 Cell aCell1 = row.createCell(0);
				 aCell1.setCellValue(methodType);
				 aCell1.setCellStyle(_getStyle(wb));
				 theSheet.autoSizeColumn(0);
				 Cell aCell2 = row.createCell(1);
				 aCell2.setCellValue(method);
				 aCell2.setCellStyle(_getStyle(wb));
				 theSheet.autoSizeColumn(1);
				 Cell aCell3 = row.createCell(2);
				 aCell3.setCellValue(reponseTime);
				 aCell3.setCellStyle(_getStyle(wb));
				 theSheet.autoSizeColumn(2);
			   }	 			
			   wb.write(out);
			   out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
	 } 
	

	
	 
	 /**
	  * 
	  * @param theSheet
	  * @return
	  */
	 private boolean _isSheetEmpty(Sheet theSheet){
		 boolean isSheetEmpty = true;
		 Iterator<Row> rowIterator =  theSheet.rowIterator();
		 if (rowIterator.next() != null && rowIterator.next().getCell(0) != null){
			 return false;
		 }
		 
		 return isSheetEmpty;
	 }
	 
	 /**
	  * 
	  * @param wb
	  * @param sheetName
	  * @return Sheet
	  */
	 private Sheet _initializeSheet(Workbook wb, String sheetName){
		 if (wb.getSheet(sheetName) != null){
			 return wb.getSheet(sheetName);
		 }
		 Sheet sheet = wb.createSheet(sheetName);
		 Row headerRow = sheet.createRow(0);
		 Cell headerCell = null;
	     for (int i = 0; i < titles.length; i++) {
	            headerCell = headerRow.createCell(i);
	            headerCell.setCellValue(titles[i]);
	            headerCell.setCellStyle(_getHeaderStyle(wb));
	       }
	     return sheet;
		 
	 }
	 
	 /**
	  * 
	  * @param wb
	  * @return
	  */
	 private CellStyle _getStyle(Workbook wb){
		 CellStyle style = wb.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_LEFT);
		 style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		 style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		 style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		 style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		 style.setBorderRight(CellStyle.BORDER_MEDIUM);
		 style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		 style.setBorderTop(CellStyle.BORDER_MEDIUM);
		 style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		 style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		 style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		 style.setWrapText(true);
		 // Create a new font and alter it.
		 Font font = wb.createFont();
		 font.setFontHeightInPoints((short)12);
		 font.setFontName(HSSFFont.FONT_ARIAL);
		 font.setItalic(false);
		 style.setFont(font);
		 return style;
	 }
	 
	 /**
	  * 
	  * @param wb
	  * @return
	  */
	 private CellStyle _getHeaderStyle(Workbook wb){
		 CellStyle style = wb.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
		 style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		 style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		 style.setBorderRight(CellStyle.BORDER_MEDIUM);
		 style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		 style.setBorderTop(CellStyle.BORDER_MEDIUM);
		 style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		 style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		 style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		 style.setFont(wb.createFont());
		 // Create a new font and alter it.
		 Font font = wb.createFont();
		 font.setFontHeightInPoints((short)20);
		 font.setFontName(HSSFFont.FONT_ARIAL);
		 font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 style.setFont(font);
		 return style;
	 }
	 
}
