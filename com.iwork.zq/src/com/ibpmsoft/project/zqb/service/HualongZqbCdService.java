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
import org.apache.poi.hssf.util.CellRangeAddress;

import com.ibpmsoft.project.zqb.dao.HualongZqbCdDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class HualongZqbCdService {
	private static Logger logger = Logger.getLogger(HualongZqbCdService.class);
	private final static String CN_FILENAME = "/common.properties";
	private HualongZqbCdDAO hualongZqbCdDAO;
	public String getConfigUUID(String parameter) {
		String config = ConfigUtil.readValue(CN_FILENAME, parameter);
		return config;
	}
	public List<HashMap<String, Object>> getHldaList(String damc,String gdwz,String dalx,String zt,
			int pageNumber, int pageSize) {
		List<String> parameter=new ArrayList<String>();//存放参数WHERE a.rn>1 AND a.rn<=10
		StringBuffer sql=new StringBuffer();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		
		String configUUID = this.getConfigUUID("HLGDKSY");
		boolean flag=false;
		if(configUUID!=null&&!"".equals(configUUID)){
			
			if(!configUUID.contains(",")){
				if(user.getUserid().equals(configUUID)){
					flag=true;
					
				}
				
			}else{
				
				String[] split = configUUID.split(",");
				for (String string : split) {
					if(user.getUserid().equals(string)){
						flag=true;
						break;
					}
				}
			}
		}
		if(flag|| user.getOrgroleid()==5){
			sql.append("  select * from ( select s.* ,rownum rn from (  ");// 
			sql.append(" select s.id id,s.damc damc,s.gdwz gdwz,s.dalx dalx,s.zt zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj, "
					+ "s.dabh dabh,s.dajs dajs,s.dafj dafj,s.lcbh lcbh,s.rwid rwid,s.lzjd lzjd,s.instanceid instanceid,D.YJINS from BD_ZQB_DACCLCB s LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON S.INSTANCEID=D.GGINS where 1=1");
			if(damc!=null&&!damc.equals("")){
				sql.append(" and s.damc like  ?");
				parameter.add("%"+damc+"%");
			}
			if(gdwz!=null&&!gdwz.equals("")){
				sql.append(" and s.gdwz like  ?");
				parameter.add("%"+gdwz+"%");
			}
			if(dalx!=null&&!dalx.equals("")){
				sql.append(" and s.dalx =  ?");
				parameter.add(dalx);
			}
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt =  ?");
				parameter.add(zt);
			}
		
		}else{
			sql.append("  select * from ( select s.* ,rownum rn from (  ");//
			sql.append(" select s.id id,s.damc damc,s.gdwz gdwz,s.dalx dalx,s.zt zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj, "
					+ "s.dabh dabh,s.dajs dajs,s.dafj dafj,s.lcbh lcbh,s.rwid rwid,s.lzjd lzjd,s.instanceid instanceid,D.YJINS from BD_ZQB_DACCLCB s LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON S.INSTANCEID=D.GGINS where 1=1");
			if(damc!=null&&!damc.equals("")){
				sql.append(" and s.damc like  ?");
				parameter.add("%"+damc+"%");
			}
			if(gdwz!=null&&!gdwz.equals("")){
				sql.append(" and s.gdwz like  ?");
				parameter.add("%"+gdwz+"%");
			}
			if(dalx!=null&&!dalx.equals("")){
				sql.append(" and s.dalx =  ?");
				parameter.add(dalx);
			}
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt =  ?");
				parameter.add(zt);
			}
			sql.append(" and s.yhid =  ?");
			
			parameter.add(user.getUserid());
		}
		//   
		sql.append("  order by  s.sqsj desc ) s ) WHERE rn>? AND rn<=?    ");
		List<HashMap<String, Object>> list = hualongZqbCdDAO.getHldaList(sql.toString(),parameter,pageNumber,pageSize);
		return list;
	}
	public Integer getHldaListSize(String damc,String gdwz,String dalx,String zt) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		if(user.getUserid()=="YANGGUANG"||user.getUserid()=="ZHAOWEI"|| user.getOrgroleid()==5){
			sql.append("   select s.* ,rownum rn from (  ");//,to_char(s.sqsj,'yyyy-MM-dd') sqsj
			sql.append(" select s.id,s.damc damc,s.gdwz gdwz,s.dalx dalx,s.dabh dabh,s.dajs dajs,s.dafj dafj,s.zt zt from BD_ZQB_DACCLCB s  where 1=1");
			if(damc!=null&&!damc.equals("")){
				sql.append(" and s.damc like  ?");
				parameter.add("%"+damc+"%");
			}
			if(gdwz!=null&&!gdwz.equals("")){
				sql.append(" and s.gdwz like  ?");
				parameter.add("%"+gdwz+"%");
			}
			if(dalx!=null&&!dalx.equals("")){
				sql.append(" and s.dalx =  ?");
				parameter.add(dalx);
			}
		
		}else{
			sql.append("  select s.* ,rownum rn from (  ");//,s.zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj
			sql.append(" select s.id,s.damc damc,s.gdwz gdwz,s.dalx dalx,s.dabh dabh,s.dajs dajs,s.dafj  dafj from BD_ZQB_DACCLCB s  where 1=1");
			if(damc!=null&&!damc.equals("")){
				sql.append(" and s.damc like  ?");
				parameter.add("%"+damc+"%");
			}
			if(gdwz!=null&&!gdwz.equals("")){
				sql.append(" and s.gdwz like  ?");
				parameter.add("%"+gdwz+"%");
			}
			if(dalx!=null&&!dalx.equals("")){
				sql.append(" and s.dalx =  ?");
				parameter.add(dalx);
			}
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt =  ?");
				parameter.add(zt);
			}
			sql.append(" and s.yhid =  ? ");
			parameter.add(user.getUserid());
		}
		//order by  s.sqsj desc
			sql.append("  ) s     ");
		int count = hualongZqbCdDAO.getHldaListSize(sql.toString(),parameter);
		return count;
	}
	public HualongZqbCdDAO getHualongZqbCdDAO() {
		return hualongZqbCdDAO;
	}
	public void setHualongZqbCdDAO(HualongZqbCdDAO hualongZqbCdDAO) {
		this.hualongZqbCdDAO = hualongZqbCdDAO;
	}
	public List<HashMap<String, Object>> getUpd(String daid) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		sql.append(" select * from BD_ZQB_DACCLCB where 1=1");
		if(daid!=null&&!daid.equals("")){
			sql.append(" and id = ?");
			parameter.add(daid.toString());
		}
		
		List<HashMap<String, Object>> list = hualongZqbCdDAO.getUpd(sql.toString(),parameter);
		return list;
	}
	public String delDg(String daid) {
		String flag="";
		if(daid!=null && !"".equals(daid)){
			try {
				Map params = new HashMap();
				params.put(1, daid);
				DBUTilNew.update(" delete from BD_ZQB_DACCLCB where id= ? ",params);
				flag="success";
			} catch (Exception e) {
				flag="error";
			}
		}
		
		return flag;
	}
	public void gdOut(HttpServletResponse response,String damc, String gdwz, String dalx, String zt) {
		
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet("档案归档信息");
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row0 = sheet.createRow((int) 0);
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,6)); 
			row0.setHeightInPoints(35);
			HSSFCell yearCell=row0.createCell(0);
			yearCell.setCellValue("档案归档信息表");
		
			HSSFRow row = sheet.createRow((int) 1);
			row.setHeightInPoints(30);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//
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
			yearCell.setCellStyle(style3);
			HSSFCell cell = row.createCell((short) 0);
			cell.setCellValue("序号");
			cell.setCellStyle(style);
			cell = row.createCell((short) 1);
			cell.setCellValue("档案名称");
			cell.setCellStyle(style);
			
			/*cell = row.createCell((short) 2);
			cell.setCellValue("所属部门");
			cell.setCellStyle(style);*/
			cell = row.createCell((short) 2);
			cell.setCellValue("档案编号");
			cell.setCellStyle(style);
			
			cell = row.createCell((short) 3);
			cell.setCellValue("档案类型");
			cell.setCellStyle(style);
			
			cell = row.createCell((short) 4);
			cell.setCellValue("归档位置");
			cell.setCellStyle(style);
			cell = row.createCell((short) 5);
			cell.setCellValue("档案件数");
			cell.setCellStyle(style);
			cell = row.createCell((short) 6);
			cell.setCellValue("审批状态");
			cell.setCellStyle(style);
			
		

			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
			//获取当前用户权限
			List list = getGdOutList(damc,gdwz,dalx,zt);
			int n = 2;
			if (list == null) {
				return;
			}
			int m = 0;
			for (int i = 0; i < list.size(); i++) {

					@SuppressWarnings("rawtypes")
					Map map = (HashMap) list.get(i);
					row = sheet.createRow((int) n++);
					// 第四步，创建单元格，并设置值
					HSSFCell cell1 = row.createCell((short) 0);
					cell1.setCellValue(map.get("rm").toString());
					cell1.setCellStyle(style2);
					HSSFCell cell2 = row.createCell((short) 1);
					cell2.setCellValue(map.get("damc")==null?"暂无":map.get("damc").toString());
					cell2.setCellStyle(style1);
					
					/*HSSFCell cell10 = row.createCell((short) 2);
					cell10.setCellValue(map.get("departmentname").toString());
					cell10.setCellStyle(style2);*/
					
					HSSFCell cell9 = row.createCell((short) 2);
					cell9.setCellValue(map.get("dabh")==null?"暂无":map.get("dabh").toString());
					cell9.setCellStyle(style2);
					
					HSSFCell cell10 = row.createCell((short) 3);
					cell10.setCellValue(map.get("dalx")==null?"暂无":map.get("dalx").toString());
					cell10.setCellStyle(style2);
					
					HSSFCell cell3 = row.createCell((short) 4);
					cell3.setCellValue(map.get("gdwz")==null?"暂无":map.get("gdwz").toString());
					cell3.setCellStyle(style2);
					HSSFCell cell4 = row.createCell((short) 5);
					cell4.setCellValue(map.get("dajs")==null?"暂无":map.get("dajs").toString());
					cell4.setCellStyle(style2);
					HSSFCell cell5 = row.createCell((short) 6);
					cell5.setCellValue(map.get("zt")==null?"暂无":map.get("zt").toString());
					cell5.setCellStyle(style2);
					
					
					m++;

			}
			for (int i = 0; i < 7; i++) {
				sheet.setColumnWidth(i, 7000);
			}
			sheet.setColumnWidth(0, 1800);
			sheet.setColumnWidth(1,10000);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3,4000);
			sheet.setColumnWidth(4,4000);
			sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 3000);

			OutputStream out1 = null;
			// 第六步，将文件存到指定位置
			try {//档案归档信息表
				String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("档案归档.xls");
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-disposition", disposition);
				out1 = new BufferedOutputStream(response.getOutputStream());
				wb.write(out1);
				// out1.flush();
				// out1.close();

			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				if (out1 != null) {
					try {
						out1.flush();
						out1.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}

			}
		}
	public List getGdOutList(String damc, String gdwz, String dalx, String zt) {
		List<String> parameter=new ArrayList<String>();//存放参数WHERE a.rn>1 AND a.rn<=10
		StringBuffer sql=new StringBuffer();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		
		String configUUID = this.getConfigUUID("HLGDKSY");
		boolean flag=false;
		if(configUUID!=null&&!"".equals(configUUID)){
			
			if(!configUUID.contains(",")){
				if(user.getUserid().equals(configUUID)){
					flag=true;
					
				}
				
			}else{
				
				String[] split = configUUID.split(",");
				for (String string : split) {
					if(user.getUserid().equals(string)){
						flag=true;
						break;
					}
				}
			}
		}
		if(flag|| user.getOrgroleid()==5){
			sql.append("  select * from ( select s.* ,rownum rn from (  ");// 
			sql.append(" select s.id id,s.damc damc,s.gdwz gdwz,s.dalx dalx,s.zt zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj, "
					+ "s.dabh dabh,s.dajs dajs,s.dafj dafj,s.lcbh lcbh,s.rwid rwid,s.lzjd lzjd,s.instanceid instanceid from BD_ZQB_DACCLCB s  where 1=1");
			if(damc!=null&&!damc.equals("")){
				sql.append(" and s.damc like  ?");
				parameter.add("%"+damc+"%");
			}
			if(gdwz!=null&&!gdwz.equals("")){
				sql.append(" and s.gdwz like  ?");
				parameter.add("%"+gdwz+"%");
			}
			if(dalx!=null&&!dalx.equals("")){
				sql.append(" and s.dalx =  ?");
				parameter.add(dalx);
			}
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt =  ?");
				parameter.add(zt);
			}
		
		}else{
			sql.append("select s.* ,rownum rn from (  ");//
			sql.append(" select s.id id,s.damc damc,s.gdwz gdwz,s.dalx dalx,s.zt zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj, "
					+ "s.dabh dabh,s.dajs dajs,s.dafj dafj,s.lcbh lcbh,s.rwid rwid,s.lzjd lzjd,s.instanceid instanceid from BD_ZQB_DACCLCB s  where 1=1");
			if(damc!=null&&!damc.equals("")){
				sql.append(" and s.damc like  ?");
				parameter.add("%"+damc+"%");
			}
			if(gdwz!=null&&!gdwz.equals("")){
				sql.append(" and s.gdwz like  ?");
				parameter.add("%"+gdwz+"%");
			}
			if(dalx!=null&&!dalx.equals("")){
				sql.append(" and s.dalx =  ?");
				parameter.add(dalx);
			}
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt =  ?");
				parameter.add(zt);
			}
			sql.append(" and s.yhid =  ?");
			
			parameter.add(user.getUserid());
		}
		//   
		sql.append("  order by  s.sqsj desc) s)");
		List<HashMap<String, Object>> list = hualongZqbCdDAO.getGdOutList(sql.toString(),parameter);
		return list;
	}

		
	
	
}
