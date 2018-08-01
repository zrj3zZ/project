package com.iwork.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.log4j.Logger;
public class FileReaderUtil {
	private static HSSFRow row;
	private static Logger logger = Logger.getLogger(FileReaderUtil.class);

	public static String fileReader(String extension,String fileurl){
		String text="";
		if(extension.equals(".wps")){
			try {
				FileInputStream in = new FileInputStream(fileurl);// 加载文档  D:\\文档检验\\宏发有色-2015-半年报.doc
				POIFSFileSystem pfs = new POIFSFileSystem(in);
				HWPFDocument hwpf = new HWPFDocument(pfs);
				text = hwpf.getText().toString();
				in.close();
			} catch (Exception e) {logger.error(e,e);
				text="";
				return text;
			}
		}else if(extension.equals(".docx")){
			try {
				FileInputStream in = new FileInputStream(fileurl);
				XWPFDocument document = new XWPFDocument(in);
				XWPFWordExtractor extractor = new XWPFWordExtractor(document);
				text = extractor.getText();
				in.close();
			} catch (Exception e) {logger.error(e,e);
				text="";
				return text;
			}
		}else if(extension.equals(".doc")){
			try {
				FileInputStream in = new FileInputStream(fileurl);// 加载文档  D:\\文档检验\\宏发有色-2015-半年报.doc
				POIFSFileSystem pfs = new POIFSFileSystem(in);
				HWPFDocument hwpf = new HWPFDocument(pfs);
				text = hwpf.getText().toString();
				in.close();
			} catch (Exception e) {logger.error(e,e);
				text="";
				return text;
			}
		}else if(extension.equals(".xls")){
	        try {
				Workbook workBook = new HSSFWorkbook(new FileInputStream(fileurl));
				for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
	                Sheet sheet = workBook.getSheetAt(numSheet);  
	                if (sheet == null) {  
	                    continue;  
	                }  
	                // 循环行Row
	                int rowNum = sheet.getLastRowNum();
	                row = (HSSFRow) sheet.getRow(0);
	                if(row!=null){
	                	int colNum = row.getPhysicalNumberOfCells();
	                	// 正文内容应该从第二行开始,第一行为表头的标题
	                	for (int i = 0; i <= rowNum; i++) {
	                		row = (HSSFRow) sheet.getRow(i);
	                		int j = 0;
	                		while (j < colNum) {
	                			HSSFCell cell = row.getCell((short) j);
	                			if(cell!=null){
	                				text += getValue(cell).trim() + "    ";
	                			}
	                			j++;
	                		}
	                	}
	                }
	            }
			} catch (Exception e) {logger.error(e,e);
				text="";
				return text;
			}  
		}else if(extension.equals(".xlsx")){
			try {
				Workbook workBook = new XSSFWorkbook(fileurl);
				int numberOfSheets = workBook.getNumberOfSheets();
				for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
	                Sheet sheet = workBook.getSheetAt(numSheet);
	                if (sheet == null) {  
	                    continue;  
	                }  
	                // 循环行Row
	                int rowNum = sheet.getLastRowNum();
	                Row row = sheet.getRow(0); 
	                if(row!=null){
	                	int colNum = row.getPhysicalNumberOfCells();
	                	// 正文内容应该从第二行开始,第一行为表头的标题
	                	for (int i = 1; i <= rowNum; i++) {
	                		row = sheet.getRow(i);
	                		int j = 0;
	                		while (j < colNum) {
	                			Cell cell = row.getCell(j);
	                			if(cell!=null){
	                				text += getValue(cell).trim() + "    ";
	                			}
	                			j++;
	                		}
	                	}
	                }
	            }
			} catch (Exception e) {logger.error(e,e);
				text="";
				return text;
			}  
		}else{
			FileInputStream fis;
			byte[] bytes;
			try {
				File f = new File(fileurl);
				fis = new FileInputStream(f);
				bytes = IOUtils.toByteArray(fis);
				if (bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65){
					text = new String(bytes,"UTF-8");
				}else{
					text = new String(bytes, "GBK");
				}
				fis.close();
			} catch (Exception e) {logger.error(e,e);
				text="";
				return text;
			}  
		}
		return text;
	}
	
	private static String getValue(Cell cell) {  
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {  
            return String.valueOf(cell.getBooleanCellValue());  
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {  
            return String.valueOf(cell.getNumericCellValue());  
        } else {  
            return String.valueOf(cell.getStringCellValue());  
        }  
    }
}
