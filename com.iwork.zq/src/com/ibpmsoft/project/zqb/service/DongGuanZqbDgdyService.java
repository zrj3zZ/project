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
import com.ibpmsoft.project.zqb.dao.DongGuanZqbDgdyDao;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.util.ResponseUtil;

import net.sf.json.JSONObject;

public class DongGuanZqbDgdyService {
	private static Logger logger = Logger.getLogger(DongGuanZqbDgdyService.class);
	private DongGuanZqbDgdyDao dongGuanZqbDgdyDao;

	public List<HashMap> getDgdyList(String startdate,String enddate,String status,String dyrId,int pageSize, int pageNumber){
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
		return dongGuanZqbDgdyDao.getDgdyList(stdate, endate, status, dyrId, pageSize, pageNumber);
	}
	public int getDgdyListSize(String startdate,String enddate,String status,String dyrId){
		return dongGuanZqbDgdyDao.getDgdyListSize(startdate, enddate, status, dyrId).size();
	}
	public DongGuanZqbDgdyDao getDongGuanZqbDgdyDao() {
		return dongGuanZqbDgdyDao;
	}

	public void setDongGuanZqbDgdyDao(DongGuanZqbDgdyDao dongGuanZqbDgdyDao) {
		this.dongGuanZqbDgdyDao = dongGuanZqbDgdyDao;
	}
	
	public void thxmexportexcl(HttpServletResponse response,String startdate,String enddate,String dyrId,String status){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("工作底稿调阅");
		
	
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
		cell.setCellValue("调阅资料内容");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("申请时间");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 2);
		cell.setCellValue("预计归还时间");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 3);
		cell.setCellValue("调阅人");
		cell.setCellStyle(style);
		
	
		
		cell = row.createCell((short) 4);
		cell.setCellValue("事由");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("归还状态");
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
		List list = dongGuanZqbDgdyDao.getDgdyDc(stdate, endate, status, dyrId);
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
				cell1.setCellValue(map.get("dyzlnr").toString());
				cell1.setCellStyle(style1);
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("sqsj").toString());
				cell2.setCellStyle(style2);
				
				
				
				
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("yjghsj").toString());
				cell3.setCellStyle(style2);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("dyrxm").toString());
				cell4.setCellStyle(style2);
				
			
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("sy").toString());
				cell6.setCellStyle(style1);
				HSSFCell cell7 = row.createCell((short) 5);
				cell7.setCellValue(map.get("spzt").toString());
				cell7.setCellStyle(style2);
				
				m++;

		}
		sheet.setColumnWidth(0, 15000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 15000);
	
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("工作底稿调阅.xls");
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
	
	//王欢创建删除节点方法
	public String dgcdZreeDelete(int zTreeId){
		String result ="";
		Map params = new HashMap();
		params.put(1, zTreeId);
		String reslut ="";
		
		String sql = "select count(*) as num from BD_ZQB_ZKNHXZRYB where roleid=? ";
		int num = DBUTilNew.getInt("num", sql, params);
		if(num>0){
			result="目录中有"+num+"个文档，请删除完毕后再删除目录";
		}
		
		String sql2 = "select count(*) as num from BD_ZQB_XMQY where XYJE =? ";
		int num2 = DBUTilNew.getInt("num", sql2, params);
		if(num2>0){
			result="目录中有"+num2+"个子目录，请删除完毕后再删除目录";
		}
		
		if(result.equals("")){
		//判断有记录才删除
			String sql3= "update BD_ZQB_XMQY set DDJE=DDJE-1 where DDJE > (select DDJE from BD_ZQB_XMQY where id= ?)";
			DBUTilNew.update(sql3, params);
			String sql4= "delete from BD_ZQB_XMQY where ID = ?";
			DBUTilNew.update(sql4, params);
		}
		return result;
	}
	
	//王欢创建删除项目目录节点方法
	public String dgcdZreeProjectDelete(int zTreeId,String departmentid){
		String result ="";
		Map params = new HashMap();
		params.put(1, zTreeId);
		params.put(2, departmentid);
		String reslut ="";
		//查看当前这个项目 的 对应目录下是否有文件
		String sql = "select count(*) as num from BD_ZQB_ZKNHXZRYB where roleid=? and departmentid=? ";
		int num = DBUTilNew.getInt("num", sql, params);
		if(num>0){
			result="目录中有"+num+"个文档，请删除完毕后再删除目录";
		}
		
		String sql2 = "select count(*) as num from BD_GF_CGXZ where CGBL =? and wxsgf=? ";
		int num2 = DBUTilNew.getInt("num", sql2, params);
		if(num2>0){
			result="目录中有"+num2+"个子目录，请删除完毕后再删除目录";
		}
		
		if(result.equals("")){
			//判断有记录才删除
			String sql3= "update BD_GF_CGXZ set XSGFS=XSGFS-1 where  XSGFS >(select XSGFS from BD_GF_CGXZ where CGS=? and wxsgf=?)";
			DBUTilNew.update(sql3, params);
			String sql4= "delete from BD_GF_CGXZ where CGS =? and wxsgf=? ";
			DBUTilNew.update(sql4, params);
		}
		return result;
	}
	
}
