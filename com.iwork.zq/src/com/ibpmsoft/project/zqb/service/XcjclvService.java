package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.dao.XcjclvDao;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.sdk.DemAPI;


public class XcjclvService {
	private static Logger logger = Logger.getLogger(XcjclvService.class);
	private XcjclvDao xcjclvDao;
	/**
	 * 培训记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getPxjlList(String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx,String pxry,int pageSize, int pageNumber){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}
			return xcjclvDao.getPxjlList(zqdmxs,zqjcxs,stdate,endate,xcjcry,xcjclx,pxry,pageSize, pageNumber);
		}
	/**
	 * 培训记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @return
	 */
	public int getPxjlListSize(String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx,String pxry){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}
			return xcjclvDao.getPxjlListSize(zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx,pxry).size();
		}
	/**
	 * 现场检查记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getXcjcList(String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx,int pageSize, int pageNumber){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}
			return xcjclvDao.getXcjcList(zqdmxs,zqjcxs,stdate,endate,xcjcry,xcjclx,pageSize, pageNumber);
		}
	/**
	 * 现场检查记录
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @return
	 */
	public int getXcjcListSize(String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}
			return xcjclvDao.getXcjcListSize(zqdmxs,zqjcxs,startdate,enddate,xcjcry,xcjclx).size();
		}
	public String delete(Long instanceId) {
		HashMap map = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value=map.get("KHMC")==null?"":map.get("KHMC").toString();
		Long dataId=Long.parseLong(map.get("ID").toString());
		boolean flag = DemAPI.getInstance().removeFormData(instanceId);
		String info = "";
		if (!flag) {
			info = "删除失败";
		} else {
			info = "success";
		}
		return info;
	}
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		XcjclvService.logger = logger;
	}
	public XcjclvDao getXcjclvDao() {
		return xcjclvDao;
	}
	public void setXcjclvDao(XcjclvDao xcjclvDao) {
		this.xcjclvDao = xcjclvDao;
	}
	/**
	 * 现场检查记录
	 * @param response
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @param pageSize
	 * @param pageNumber
	 */
	public void thxmexportexcl(HttpServletResponse response,String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx,int pageSize, int pageNumber){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("现场检查记录");
		
	
		HSSFRow row = sheet.createRow((int) 0);
		row.setHeightInPoints(30);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setBorderTop((short) 1);
		style.setWrapText(true);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setWrapText(true);
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		style2.setWrapText(true);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style3.setFont(font);
		HSSFFont font2 = wb.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font2);
		//yearCell.setCellStyle(style3);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("证券代码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("证券简称");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 2);
		cell.setCellValue("现场检查时间");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 3);
		cell.setCellValue("现场检查人员");
		cell.setCellStyle(style);
		
	
		
		cell = row.createCell((short) 4);
		cell.setCellValue("现场检查类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("引发现场检查的因素");
		cell.setCellStyle(style);
	
	
	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		//获取当前用户权限
		List list = xcjclvDao.getXcjcList(zqdmxs,zqjcxs,stdate,endate,xcjcry,xcjclx,pageSize, pageNumber);
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (int i = 0; i < list.size(); i++) {

				Map map = (HashMap) list.get(i);
				row = sheet.createRow((int) n++);
				// 第四步，创建单元格，并设置值
				HSSFCell cell1 = row.createCell((short) 0);
				cell1.setCellValue(map.get("zqdm").toString());
				cell1.setCellStyle(style2);
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("zqjc").toString());
				cell2.setCellStyle(style2);
				
				
				
				
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("sj").toString());
				cell3.setCellStyle(style2);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("ry").toString());
				cell4.setCellStyle(style2);
				
			
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("nsr").toString());
				cell6.setCellStyle(style2);
				HSSFCell cell7 = row.createCell((short) 5);
				cell7.setCellValue(map.get("swdjh").toString());
				cell7.setCellStyle(style2);
				
				
				m++;

		}
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 15000);
	
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("现场检查记录.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);

		} catch (Exception e) {
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
				}
			}

		}
	}
	/**
	 * 培训记录
	 * @param response
	 * @param zqdmxs
	 * @param zqjcxs
	 * @param startdate
	 * @param enddate
	 * @param xcjcry
	 * @param xcjclx
	 * @param pageSize
	 * @param pageNumber
	 */
	public void pxjlexcl(HttpServletResponse response,String zqdmxs,String zqjcxs,String startdate,String enddate,String xcjcry,String xcjclx,String pxry,int pageSize, int pageNumber){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("培训记录");
		
	
		HSSFRow row = sheet.createRow((int) 0);
		row.setHeightInPoints(30);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setBorderTop((short) 1);
		style.setWrapText(true);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setWrapText(true);
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		style2.setWrapText(true);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style3.setFont(font);
		HSSFFont font2 = wb.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font2);
		//yearCell.setCellStyle(style3);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("证券代码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("证券简称");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 2);
		cell.setCellValue("培训时间");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 3);
		cell.setCellValue("培训人员");
		cell.setCellStyle(style);
		
	
		
		cell = row.createCell((short) 4);
		cell.setCellValue("参训人员");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("培训类型");
		cell.setCellStyle(style);
	
	
	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		//获取当前用户权限
		List list = xcjclvDao.getPxjlList(zqdmxs,zqjcxs,stdate,endate,xcjcry,xcjclx,pxry,pageSize, pageNumber);
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (int i = 0; i < list.size(); i++) {

				Map map = (HashMap) list.get(i);
				row = sheet.createRow((int) n++);
				// 第四步，创建单元格，并设置值
				HSSFCell cell1 = row.createCell((short) 0);
				cell1.setCellValue(map.get("zqdm").toString());
				cell1.setCellStyle(style2);
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("zqjc").toString());
				cell2.setCellStyle(style2);
				
				
				
				
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("cjsj").toString());
				cell3.setCellStyle(style2);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("pxry").toString());
				cell4.setCellStyle(style2);
				
			
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("cxry").toString());
				cell6.setCellStyle(style2);
				HSSFCell cell7 = row.createCell((short) 5);
				cell7.setCellValue(map.get("pxlx").toString());
				cell7.setCellStyle(style2);
				
				
				m++;

		}
		sheet.setColumnWidth(0, 6500);
		sheet.setColumnWidth(1, 6500);
		sheet.setColumnWidth(2, 6500);
		sheet.setColumnWidth(3, 6500);
		sheet.setColumnWidth(4, 6500);
		sheet.setColumnWidth(5, 6500);
	
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("培训记录.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);

		} catch (Exception e) {
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
				}
			}

		}
	}
}
