package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

import net.sf.json.JSONArray;

import com.ibpmsoft.project.zqb.dao.QywgfxjlDao;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class QywgfxjlService {
	private QywgfxjlDao qywgfxjlDao;
	private static Logger logger=Logger.getLogger(QywgfxjlService.class);
	public List<HashMap> getQyList(String khbh, String khmc, String gsdm,String gsjc,String wgfxnr,String wgfxlx, int pageSize, int pageNumber) {
		return qywgfxjlDao.getQyList(khbh, khmc, gsdm, gsjc, wgfxnr, wgfxlx, pageSize, pageNumber);
	}
	public int getQyListSize(String khbh, String khmc, String gsdm,String gsjc,String wgfxnr,String wgfxlx) {
		return qywgfxjlDao.getQyListSize(khbh, khmc, gsdm, gsjc, wgfxnr, wgfxlx).size();
	}
	public QywgfxjlDao getQywgfxjlDao() {
		return qywgfxjlDao;
	}
	public String getDmJcData() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		List<String> parameter=new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.zqdm,s.zqjc from bd_zqb_kh_base s  ");
		if(user.getOrgroleid()==4){
			sql.append("  where s.createuser= ? or s.createuser= ? or s.username= ?  ");
			sql.append(" or s.fddbr= ?  or s.userid= ? or s.userid= ? ");
			parameter.add(user.getUserid());
			parameter.add(user.getUsername());
			parameter.add(user.getUsername());
			parameter.add(user.getUsername());
			parameter.add(user.getUsername());
			parameter.add(user.getUserid());
		}
		List<HashMap<String,String>> customerAutoDataList = getCustomerAutoDataList(sql.toString(),parameter);
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(customerAutoDataList);
		jsonHtml.append("{\"success\":true,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	public String getQysj(int id) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();
		sql.append("  select s.sxlx sxlx,s.sxgy_1 sxlxs from bd_xp_baseinfo s  where to_char(s.id)=? ");
		parameter.add(String.valueOf(id));
		List<HashMap<String,String>> customerAutoDataList = getqyxxList(sql.toString(),parameter);
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(customerAutoDataList);
		jsonHtml.append(json.toString());
		return jsonHtml.toString();
	}
	public List<HashMap<String,String>> getCustomerAutoDataList(String sql,List<String> parameter){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		list = qywgfxjlDao.getCustomerAutoDataList(sql,parameter);
		return list;
	}
	public List<HashMap<String,String>> getqyxxList(String sql,List<String> parameter){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		list = qywgfxjlDao.getqyxxList(sql,parameter);
		return list;
	}
	public void setQywgfxjlDao(QywgfxjlDao qywgfxjlDao) {
		this.qywgfxjlDao = qywgfxjlDao;
	}
	public void thxmexportexcl(HttpServletResponse response){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("企业违规风险记录");
		
	
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
		cell.setCellValue("公司代码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("公司简称");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 2);
		cell.setCellValue("填报人");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 3);
		cell.setCellValue("发起日期");
		cell.setCellStyle(style);
		
	
		
		cell = row.createCell((short) 4);
		cell.setCellValue("违规风险类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("违规风险内容");
		cell.setCellStyle(style);
	
	
	

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		//获取当前用户权限
		List list = qywgfxjlDao.getMeetExcl();
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
				cell1.setCellValue(map.get("ycl").toString());
				cell1.setCellStyle(style2);
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("tjjsh").toString());
				cell2.setCellStyle(style2);
				
				
				
				
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("sxmc").toString());
				cell3.setCellStyle(style2);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("sbrq").toString());
				cell4.setCellStyle(style2);
				
			
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("sxgy_1").toString());
				cell6.setCellStyle(style2);
				HSSFCell cell7 = row.createCell((short) 5);
				cell7.setCellValue(map.get("sxgy").toString());
				cell7.setCellStyle(style2);
				
				
				m++;

		}
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 18000);
		sheet.setColumnWidth(5, 20000);
	
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("违规风险类型.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);

		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}

		}
	}

}
