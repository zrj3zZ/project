package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.ibpmsoft.project.zqb.dao.ZqbInvoiceDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;

public class ZqbInvoiceService {
	private ZqbInvoiceDAO zqbInvoiceDAO;
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件

	public void setZqbInvoiceDAO(ZqbInvoiceDAO zqbInvoiceDAO) {
		this.zqbInvoiceDAO = zqbInvoiceDAO;
	}
	
	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter));
		}
		return result;
	}

	public List<HashMap<String, Object>> getKhList(String customername,
			int pageNumber, int pageSize) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT ID,CUSTOMERNO,CUSTOMERNAME,BANKNAME,BANKACCOUNT,INSTANCEID,ROWNUM RM FROM (SELECT KH.ID,KH.CUSTOMERNO,KH.CUSTOMERNAME,KH.BANKNAME,KH.BANKACCOUNT,BIND.INSTANCEID FROM BD_ZQB_KHXXGL KH INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_KHXXGL') META ON IFORM.METADATAID=META.ID) BIND ON KH.ID=BIND.DATAID ORDER BY ID) WHERE 1=1 ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND UPPER(CUSTOMERNAME) LIKE ?");
			parameter.add(customername);
		}
		sql.append(") WHERE RM>? AND RM<=?");
		List<HashMap<String, Object>> list = zqbInvoiceDAO.getKhList(sql.toString(),parameter,pageNumber,pageSize);
		return list;
	}

	public Integer getKhListSize(String customername) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) CNUM FROM (SELECT KH.ID,KH.CUSTOMERNAME,KH.BANKNAME,KH.BANKACCOUNT,BIND.INSTANCEID FROM BD_ZQB_KHXXGL KH INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_KHXXGL') META ON IFORM.METADATAID=META.ID) BIND ON KH.ID=BIND.DATAID ORDER BY ID) WHERE 1=1 ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND UPPER(CUSTOMERNAME) LIKE ?");
			parameter.add(customername);
		}
		int count = zqbInvoiceDAO.getKhListSize(sql.toString(),parameter);
		return count;
	}
	public List<HashMap<String, Object>> getKpList(String customername,String customerno,String state,
			int pageNumber, int pageSize) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		if(user.getOrgroleid()==13 || user.getOrgroleid()==5){
			sql.append("  select * from ( select s.* ,rownum rn from (  ");
			sql.append(" select s.id,s.customername,s.customerno,s.state,s.nsrlx,to_char(s.sqrq,'yyyy-MM-dd') sqrq from BD_ZQB_SQKP s  where 1=1");
			if(customername!=null&&!customername.equals("")){
				sql.append(" and s.customername like  ?");
				parameter.add("%"+customername+"%");
			}
			if(customerno!=null&&!customerno.equals("")){
				sql.append(" and s.customerno like  ?");
				parameter.add("%"+customerno+"%");
			}
			if(state!=null&&!state.equals("")){
				sql.append(" and s.state =  ?");
				parameter.add(state);
			}
		
		}else if(user.getOrgroleid()!=3){
			sql.append("  select * from ( select s.* ,rownum rn from (  ");
			sql.append(" select s.id,s.customername,s.customerno,s.state,s.nsrlx,to_char(s.sqrq,'yyyy-MM-dd') sqrq from BD_ZQB_SQKP s  where 1=1");
			if(customername!=null&&!customername.equals("")){
				sql.append(" and s.customername like  ?");
				parameter.add("%"+customername+"%");
			}
			if(customerno!=null&&!customerno.equals("")){
				sql.append(" and s.customerno like  ?");
				parameter.add("%"+customerno+"%");
			}
			if(state!=null&&!state.equals("")){
				sql.append(" and s.state =  ?");
				parameter.add(state);
			}
			sql.append(" and s.userid =  ?");
			parameter.add(user.getUserid());
		}
		
		sql.append("order by  s.sqrq desc  ) s ) WHERE rn>? AND rn<=?     ");
		List<HashMap<String, Object>> list = zqbInvoiceDAO.getKpList(sql.toString(),parameter,pageNumber,pageSize);
		return list;
	}
	public List<HashMap<String, Object>> getUpd(Long khid) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		sql.append(" select s.id,s.customername,s.customerno,s.state,s.nsrlx,to_char(s.sqrq,'yyyy-MM-dd') sqrq,s.memo from BD_ZQB_SQKP s  where 1=1");
		if(khid!=null&&!khid.equals("")){
			sql.append(" and s.id = ?");
			parameter.add(khid.toString());
		}
		
		List<HashMap<String, Object>> list = zqbInvoiceDAO.getUpd(sql.toString(),parameter);
		return list;
	}
	public String updsave(Long khid,String customername,String customerno,String state,String nsrlx,String sqrq,String memo) {
		String flag="";
		if(khid!=null && !"".equals(khid)){
			
			try {
				Map params = new HashMap();
				params.put(1, customername);
				params.put(2, customerno);
				params.put(3, state);
				params.put(4, nsrlx);
				params.put(5, sqrq);
				params.put(6, memo);
				params.put(7, khid);
				DBUTilNew.update("UPDATE BD_ZQB_SQKP s SET s.customername= ? ,s.customerno= ? ,s.state= ? ,s.nsrlx= ? ,s.sqrq=to_date( ? ,'yyyy-MM-dd'),s.memo= ?  WHERE s.id= ?  ",params);
				flag="success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				flag="error";
			}
		}
		
		return flag;
	}
	public String delDg(Long khid) {
		String flag="";
		if(khid!=null && !"".equals(khid)){
			try {
				DBUtil.executeUpdate(" delete from BD_ZQB_SQKP where id="+khid+"");
				flag="success";
			} catch (Exception e) {
				flag="error";
			}
		}
		
		return flag;
	}
	
	
	public Integer getKpListSize(String customername,String customerno,String state) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		if(user.getOrgroleid()==13 || user.getOrgroleid()==5){
			sql.append("   select s.* ,rownum rn from (  ");
			sql.append(" select s.id,s.customername,s.customerno,s.state,s.nsrlx,to_char(s.sqrq,'yyyy-MM-dd') sqrq from BD_ZQB_SQKP s  where 1=1");
			if(customername!=null&&!customername.equals("")){
				sql.append(" and s.customername like  ?");
				parameter.add("%"+customername+"%");
			}
			if(customerno!=null&&!customerno.equals("")){
				sql.append(" and s.customerno like  ?");
				parameter.add("%"+customerno+"%");
			}
			if(state!=null&&!state.equals("")){
				sql.append(" and s.state =  ?");
				parameter.add(state);
			}
		
		}else if(user.getOrgroleid()!=3){
			sql.append("  select s.* ,rownum rn from (  ");
			sql.append(" select s.id,s.customername,s.customerno,s.state,s.nsrlx,to_char(s.sqrq,'yyyy-MM-dd') sqrq from BD_ZQB_SQKP s  where 1=1");
			if(customername!=null&&!customername.equals("")){
				sql.append(" and s.customername like  ?");
				parameter.add("%"+customername+"%");
			}
			if(customerno!=null&&!customerno.equals("")){
				sql.append(" and s.customerno like  ?");
				parameter.add("%"+customerno+"%");
			}
			if(state!=null&&!state.equals("")){
				sql.append(" and s.state =  ?");
				parameter.add(state);
			}
			sql.append(" and s.userid =  ? ");
			parameter.add(user.getUserid());
		}
		
		sql.append("order by  s.sqrq desc  ) s     ");
		int count = zqbInvoiceDAO.getKpListSize(sql.toString(),parameter);
		return count;
	}

	public boolean removekh(Long khinstanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(khinstanceid);
		boolean removeFormData=false;
		String customerno = fromData.get("CUSTOMERNO").toString();
		Long dataId = Long.parseLong(fromData.get("ID").toString());
		String customername = fromData.get("CUSTOMERNAME").toString();
		int count = DBUtil.getInt("SELECT COUNT(*) NUM FROM BD_ZQB_SQKP WHERE CUSTOMERNO='"+customerno+"'", "NUM");
		if(count==0){
			removeFormData = DemAPI.getInstance().removeFormData(khinstanceid);
		}else{
			removeFormData=false;
		}
		if(removeFormData){
			LogUtil.getInstance().addLog(dataId, "开票信息维护", "删除开票信息："+customername);
		}
		return removeFormData;
	}

	public boolean removekp(Long kpinstanceid) {
		boolean removeFormData=false;
		HashMap dataMap = DemAPI.getInstance().getFromData(kpinstanceid);
		Long dataId = Long.parseLong(dataMap.get("ID").toString());
		String customername = dataMap.get("CUSTOMERNAME").toString();
		removeFormData = DemAPI.getInstance().removeFormData(kpinstanceid);
		if(removeFormData){
			LogUtil.getInstance().addLog(dataId, "申请开票信息维护", "删除申请开票信息："+customername);
		}
		return removeFormData;
	}

	public boolean setKhState(Long kpinstanceid, Long demId,Long kpdataId) {
		HashMap fromData = DemAPI.getInstance().getFromData(kpinstanceid);
		fromData.put("STATE","1");
		boolean updateFormData = DemAPI.getInstance().updateFormData(demId, kpinstanceid, fromData, kpdataId, false);
		return updateFormData;
	}
	
	/**
	 * 导出工作底稿归档
	 * @param response
	 * @param customername
	 * @param customerno
	 * @param state
	 */
	public void thxmexportexcl(HttpServletResponse response,String customername,String customerno,String state){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("工作底稿归档");
		
	
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
		cell.setCellValue("项目名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("归档人员");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 2);
		cell.setCellValue("归档时间");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 3);
		cell.setCellValue("归档位置");
		cell.setCellStyle(style);
		
	
		
		cell = row.createCell((short) 4);
		cell.setCellValue("归档状态");
		cell.setCellStyle(style);
		
		
		List list = zqbInvoiceDAO.getDdggDc(customername,customerno,state);
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
				cell1.setCellValue(map.get("customername").toString());
				cell1.setCellStyle(style1);
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("customerno").toString());
				cell2.setCellStyle(style1);
				
				
				
				
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("sqrq").toString());
				cell3.setCellStyle(style2);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("nsrlx").toString());
				cell4.setCellStyle(style2);
				
			
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("state").toString());
				cell6.setCellStyle(style2);
				
				
				m++;

		}
		sheet.setColumnWidth(0, 9000);
		sheet.setColumnWidth(1, 9000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
	
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("工作底稿归档.xls");
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
