package com.ibpmsoft.project.zqb.service;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.dao.ZqbAnnouncementDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ScatterSample;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.app.weixin.process.action.qy.util.TestSendMes;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.core.upload.util.FileUploadUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.calender.Util.CalendarUtil;
import com.iwork.plugs.calender.model.IworkSchCalendar;
import com.iwork.plugs.calender.service.SchCalendarService;
import com.iwork.process.tools.processopinion.service.ProcessOpinionService;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.activiti.engine.task.Task;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.Region;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
public class ZqbAnnouncementService {
	private static Logger logger = Logger.getLogger(ZqbAnnouncementService.class);
	// 公告UUID
	public static final String Announcement_UUID = "1dfe958166a347188339af1337e25fb7";
	public static final String GGSMJ_UUID="cfa092653f594bf4b71fbd49baa64662";
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	static final char SBC_CHAR_START = 65281; // 全角！ 
	static final char SBC_CHAR_END = 65374; // 全角～
	static final int CONVERT_STEP = 65248; // 全角半角转换间隔
	static final char SBC_SPACE = 12288; // 全角空格 12288
	static final char DBC_SPACE = ' '; // 半角空格
	private ProcessOpinionService processOpinionService;
	private ZqbAnnouncementDAO zqbAnnouncementDAO;
	private SysEngineMetadataDAO sysEngineMetadataDAO; //存储模型
	private SysEngineIFormDAO sysEngineIFormDAO;//表单模型获取DAO
	private FileUploadService uploadifyService;
	public FileUploadService getUploadifyService() {
		return uploadifyService;
	}

	public void setUploadifyService(FileUploadService uploadifyService) {
		this.uploadifyService = uploadifyService;
	}

	public void setProcessOpinionService(ProcessOpinionService processOpinionService) {
		this.processOpinionService = processOpinionService;
	}

	public void setSysEngineMetadataDAO(SysEngineMetadataDAO sysEngineMetadataDAO) {
		this.sysEngineMetadataDAO = sysEngineMetadataDAO;
	}

	public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO) {
		this.sysEngineIFormDAO = sysEngineIFormDAO;
	}

	public FileUploadDAO fileUploadDao;

	public ZqbAnnouncementDAO getZqbAnnouncementDAO() {
		return zqbAnnouncementDAO;
	}

	public void setZqbAnnouncementDAO(ZqbAnnouncementDAO zqbAnnouncementDAO) {
		this.zqbAnnouncementDAO = zqbAnnouncementDAO;
	}

	public boolean deleteAnnouncement(Long instanceid) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);
		String ljxDemUUID = config.get("ggljxuuid");
		boolean flag = false;
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		String yxid = fromData.get("YXID").toString();
		Long lcInstanceId = Long.parseLong(yxid);
		HashMap lcData = ProcessAPI.getInstance().getFromData(lcInstanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String actDefId = fromData.get("LCBH").toString();
		/*if(!fromData.get("SPZT").toString().equals("审批通过")){
		}*/
		if(lcData!=null&&!lcData.equals("")&&lcData.size()>0){
			lcData.put("NOTICENO", "");
			Long lcId = Long.parseLong(lcData.get("ID").toString());
			boolean updateFormData = ProcessAPI.getInstance().updateFormData(actDefId, lcInstanceId, lcData, lcId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(yxid));
		if(newTaskId!=null){
			String assignee = newTaskId.getAssignee();
			String taskId = newTaskId.getId();
			String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
			ProcessAPI.getInstance().setTaskAssignee(taskId, "");
			fromData.put("LCWTR", assignee);
			fromData.put("CREATEID", userid);
		}
		Long ljInstanceid = DemAPI.getInstance().newInstance(ljxDemUUID,UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid());
		
		boolean saveFormData = DemAPI.getInstance().saveFormData(ljxDemUUID, ljInstanceid, fromData, false);
		HashMap dataMap = DemAPI.getInstance().getFromData(ljInstanceid);
		Long dataid = Long.parseLong(dataMap.get("ID").toString());
		String khmc = dataMap.get("KHMC").toString();
		String noticename = dataMap.get("NOTICENAME").toString();
		flag = DemAPI.getInstance().removeFormData(instanceid);
		HashMap smjmap=new HashMap();
		smjmap.put("GGINS", instanceid);
		List<HashMap> smjList=DemAPI.getInstance().getList(GGSMJ_UUID, smjmap, null);
		if(smjList.size()>0){
			Long smjInstanceId = Long.parseLong(smjList.get(0).get("INSTANCEID").toString());
			HashMap smjData = DemAPI.getInstance().getFromData(smjInstanceId);
			smjData.put("GGINS", ljInstanceid);
			Long smjId = Long.parseLong(smjData.get("ID").toString());
			DemAPI.getInstance().updateFormData(GGSMJ_UUID, smjInstanceId, smjData, smjId, false);
		}
		if(saveFormData){
			LogUtil.getInstance().addLog(dataid, "公告垃圾箱信息维护", "公告删除："+khmc+"中的"+noticename+"被移到公告垃圾箱中。");
		}else{
			LogUtil.getInstance().addLog(dataid, "公告垃圾箱信息维护", "公告删除："+khmc+"中的"+noticename+"被移到公告垃圾箱中失败。");
		}
		return flag;
	}

	public boolean validationAnnouncement(String noticeno, String khbh,Long instanceId) {
		boolean flag = false;
		Map params =null;
		if(instanceId==null){
			params=new HashMap();
			params.put(1, khbh);
			params.put(2, noticeno);
			int count = DBUTilNew.getInt("COUNT","SELECT COUNT(*) COUNT FROM BD_XP_QTGGZLLC WHERE KHBH= ?  AND NOTICENO = ?  AND NOTICENO IS NOT NULL",params);
			if(count>0){
				flag = true;
			}
		}else{
			params=new HashMap();
			params.put(1, khbh);
			params.put(2, noticeno);
			Long sjinstanceid = DBUTilNew.getLong("INSTANCEID","SELECT INSTANCEID FROM BD_XP_QTGGZLLC WHERE KHBH= ?  AND NOTICENO = ?  AND NOTICENO IS NOT NULL ORDER BY ID", params);
			if(sjinstanceid!=0&&(!sjinstanceid.toString().equals(instanceId.toString()))){
				flag = true;
			}
		}
		return flag;
	}

	public List<HashMap> queryAnnouncement(String khbh) {
		List<HashMap> temList = new ArrayList<HashMap>();
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("KHBH", khbh);
		temList = DemAPI.getInstance().getList(Announcement_UUID, conditionMap,
				null);

		return temList;
	}

	public List<HashMap> getList(String khbh, int pageSize, int pageNumber,
			String noticename,String noticetype, String startdate, String enddate,String bzlx, String spzt,String zqdm,String zqjc,String dq,String companyno) {
		
		return zqbAnnouncementDAO.getList(khbh, pageSize, pageNumber,
				noticename,noticetype, startdate, enddate,bzlx,spzt,zqdm,zqjc,dq,companyno);
	}
	
	public List<HashMap> getCompanyEnterprisesList(int pageSize, int pageNumber, String fullname, String model, String orderby, String starttime, String endtime) {
		return zqbAnnouncementDAO.getCompanyEnterprisesList(pageSize,pageNumber,fullname,model,orderby,starttime,endtime);
	}

	public int getCompanyEnterprisesSize(String fullname, String model, String orderby, String starttime, String endtime) {
		return zqbAnnouncementDAO.getCompanyEnterprisesSize(fullname,model,orderby,starttime,endtime).size();
	}
	
	public List<HashMap> submsgEnterprisesList(int pageSize, int pageNumber, String fullname, String model, String orderby, String starttime, String endtime, String departmentname) {
		return zqbAnnouncementDAO.submsgEnterprisesList(pageSize,pageNumber,fullname,model,orderby,starttime,endtime,departmentname);
	}

	public int submsgEnterprisesSize(String fullname, String model, String orderby, String starttime, String endtime, String departmentname) {
		return zqbAnnouncementDAO.submsgEnterprisesSize(fullname,model,orderby,starttime,endtime,departmentname).size();
	}
	
	public void expCompanyList(HttpServletResponse response, String fullname, String model, String orderby, String starttime, String endtime){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("员工绩效汇总详细");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		int z = 0;
		int n = 0;
		List<HashMap> list = zqbAnnouncementDAO.getCompanyEnterprisesSize(fullname,model,orderby,starttime,endtime);
		HSSFRow row1 = sheet.createRow((int) z++);
		row1.setRowStyle(style5);
		HSSFCell cell1 = row1.createCell((short) n++);
		cell1.setCellValue("姓名");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("所属部门");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("总减分");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("发生模块");
		cell1.setCellStyle(style5);
		for (HashMap hashMap : list) {
			n=0;
			row1 = sheet.createRow((int) z++);
			HSSFCell cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("FULLNAME") == null ? "" : hashMap.get("FULLNAME").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("DEPARTMENTNAME") == null ? "" : hashMap.get("DEPARTMENTNAME").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("MODEL") == null ? "" : hashMap.get("MODEL").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("SUMFS") == null ? "" : hashMap.get("SUMFS").toString());
			cell2.setCellStyle(style4);
		}
		for (int i = 0; i < 4; i++) {
			sheet.setColumnWidth(i, 7000);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("员工绩效汇总统计.xls");
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
					logger.error(e,e);
				}
			}

		}
	}
	
	public void expSubmsgList(HttpServletResponse response, String fullname, String model, String orderby, String starttime, String endtime, String departmentname){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("员工绩效汇总详细");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		int z = 0;
		int n = 0;
		List<HashMap> list = zqbAnnouncementDAO.submsgEnterprisesSize(fullname,model,orderby,starttime,endtime,departmentname);
		HSSFRow row1 = sheet.createRow((int) z++);
		row1.setRowStyle(style5);
		HSSFCell cell1 = row1.createCell((short) n++);
		cell1.setCellValue("姓名");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("所属部门");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("减分");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("发生模块");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("扣分日期");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) n++);
		cell1.setCellValue("公告/项目名称");
		cell1.setCellStyle(style5);
		for (HashMap hashMap : list) {
			n=0;
			row1 = sheet.createRow((int) z++);
			HSSFCell cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("FULLNAME") == null ? "" : hashMap.get("FULLNAME").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("DEPARTMENTNAME") == null ? "" : hashMap.get("DEPARTMENTNAME").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("SUMFS") == null ? "" : hashMap.get("SUMFS").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("MODEL") == null ? "" : hashMap.get("MODEL").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("KFSJ") == null ? "" : hashMap.get("KFSJ").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) n++);
			cell2.setCellValue(hashMap.get("ITEMNAME") == null ? "" : hashMap.get("ITEMNAME").toString());
			cell2.setCellStyle(style4);
		}
		for (int i = 0; i < 6; i++) {
			sheet.setColumnWidth(i, 7000);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("员工绩效汇总详细.xls");
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
					logger.error(e,e);
				}
			}

		}
	}
	
	public String getGzid(String ggid){
		Map params = new HashMap();
		params.put(1,ggid);
		String gzid = DBUTilNew.getDataStr("ID","SELECT B.ID FROM BD_MEET_QTGGZL A LEFT JOIN BD_XP_GZXTYJB B ON A.ID=B.GGID WHERE A.ID= ? ", params);
		if(gzid==null||gzid.equals("")){
			gzid="";
		}
		return gzid;
	}
	public String getSMJinsid(String ggid){

		Map params = new HashMap();
		params.put(1,ggid);
		String smjinsid = DBUTilNew.getDataStr("INSTANCEID","SELECT S.INSTANCEID FROM BD_MEET_QTGGZL A LEFT JOIN BD_XP_GGSMJ B ON A.ID=B.GGID LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='公告扫描件表单') AND A.ID=? ", params);
		if(smjinsid==null||smjinsid.equals("")){
			smjinsid="";
		}
		return smjinsid;
	}
	
	public List<HashMap> AnnouncementUsermulti(String khbh) {
		return zqbAnnouncementDAO.AnnouncementUsermulti(khbh);
	}
	
	public List<HashMap> announcementDxyjtzr(String gzyjid){
		return zqbAnnouncementDAO.announcementDxyjtzr(gzyjid);
	}
	
	public List<HashMap> getGzxtyjhfList(String gzyjid) {
		return zqbAnnouncementDAO.getGzxtyjhfList(gzyjid);
	}
	
	public void expNotice(String khbh,
			String noticename,String noticetype, String startdate, String enddate,String bzlx, String spzt,String zqdm,String zqjc,String dq,String companyno,HttpServletResponse response){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("公告列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style4.setFont(headfont);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公告类型");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公告编号");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公司简称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公告名称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("提交人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("会议名称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公告日期");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("股东大会召开日期");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("审批结果");cell1.setCellStyle(style4);
		list=zqbAnnouncementDAO.getList(khbh, noticename, noticetype, startdate, enddate, bzlx,spzt,zqdm,zqjc,dq,companyno);
		for (HashMap map : list) {
			z=0;
			row1 = sheet.createRow((int) m++);
			HSSFCell 
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("NOTICETYPE") == null ? "" : map.get("NOTICETYPE").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("NOTICENO") == null ? "" : map.get("NOTICENO").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQJCXS") == null ? "" : map.get("ZQJCXS").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("NOTICENAME") == null ? "" : map.get("NOTICENAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CREATENAME") == null ? "" : map.get("CREATENAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("MEETNAME") == null ? "" : map.get("MEETNAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("NOTICEDATE") == null ? "" : map.get("NOTICEDATE").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("COMPANYNO") == null ? "" : map.get("COMPANYNO").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("SPZT") == null ? "" : map.get("SPZT").toString());cell2.setCellStyle(style4);
		}
		for (int i = 0; i < 9; i++) {
			sheet.setColumnWidth(i, 5000);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + URLEncoder.encode(new StringBuilder("公告列表").append(".xls").toString(), "UTF-8");
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
	
	public int getTotalListSize(String khbh, String noticename,String noticetype,
			String startdate, String enddate,String bzlx, String spzt,String zqdm,String zqjc,String dq,String companyno) {
		
		return zqbAnnouncementDAO.getTotalListSize(khbh, noticename,noticetype, startdate,
				enddate,bzlx,spzt,zqdm,zqjc,dq,companyno);
	}

	/**
	 * 获取版本历史
	 * 
	 * @param noticetext
	 * @return
	 */
	public List<HashMap> getLsbb(String noticetext) {
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from iwork_weboffcie_version where recordid=? order by dotime desc");
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1,noticetext);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String file_id = rs.getString("FILE_ID");
				String username = rs.getString("USERNAME");
				String usernameFile = DBUtil.getString(
						"select USERNAME from orguser where userid='"
								+ username + "'", "USERNAME");// 用户姓名
				Timestamp dotime = rs.getTimestamp("DOTIME");
				String descript = rs.getString("DESCRIPT");
				String recordid = rs.getString("RECORDID");
				String name = DBUtil.getString("select FILE_SRC_NAME from sys_upload_file where file_id='" + file_id + "'", "FILE_SRC_NAME");// 文件名称
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("file_id", file_id);
				map.put("dotime", UtilDate.datetimeFormat2(dotime));
				map.put("descript", descript);
				map.put("username", usernameFile);
				map.put("name", name);
				map.put("recordid", file_id);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}

	/**
	 * 获取公告
	 * 
	 * @param instanceId
	 * @return
	 */
	public String getGG(Long instanceId,String zqdmxs,String zqjcxs) {
		String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
		String sm="";
		String xm="";
		if(zqserver.equals("xnzq")){
			sm="公告附件";
			xm="佐证文件";
		}else{
			sm="披露文件";
			xm="底稿文件";
		}
		// 公告表单数据
		HashMap<String, Object> hashMap = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		// 拼表单对象
		StringBuffer content = new StringBuffer();
		if (hashMap != null && hashMap.size() > 0) {
			if((zqjcxs.equals("undefined") || zqjcxs==null  || "".equals(zqjcxs)) && hashMap.get("ZQJCXS")!=null){
				zqjcxs=hashMap.get("ZQJCXS").toString();
			} 
			if((zqdmxs.equals("undefined") || zqdmxs==null  || "".equals(zqdmxs)) && hashMap.get("ZQDMXS")!=null){
				zqdmxs=hashMap.get("ZQDMXS").toString();
			}
			content.append("<table class=\"ke-zeroborder\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"98%\">");
			content.append("<tbody>");
			
		/*	content.append("<tr id=\"itemTr_1705\">");
			content.append("<td id=\"title_ZQJCXS\" class=\"td_title\" width=\"90px\">");
			content.append("公司简称");
			content.append("</td>");
			content.append("<td id=\"data_ZQJCXS\" class=\"td_data\" width=\"120px\"><xmp>");
			content.append((zqjcxs==null||zqjcxs.equals("")?DBUtil.getString("SELECT ZQJC FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"+hashMap.get("KHBH")+"'", "ZQJC"):zqjcxs)+"</xmp>&nbsp;");
			content.append("</td>");
			content.append("<td id=\"title_ZQDMXS\" class=\"td_title\" width=\"90px\">");
			content.append("证券代码");
			content.append("</td>");
			content.append("<td id=\"data_ZQDMXS\" class=\"td_data\" width=\"120px\"><xmp>");
			content.append((zqdmxs==null||zqdmxs.equals("")?DBUtil.getString("SELECT ZQDM FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"+hashMap.get("KHBH")+"'", "ZQDM"):zqdmxs) + "</xmp>&nbsp;");
			content.append("</td>");*/
			
			content.append("</tr>");
			content.append("<tr id=\"itemTr_1706\">");
			content.append("<td id=\"title_NOTICENO\" class=\"td_title\" width=\"90px\">");
			content.append("公告编号");
			content.append("<input type='hidden' id='zqdms' value=\""+(zqdmxs==null||zqdmxs.equals("")?DBUtil.getString("SELECT ZQDM FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"+hashMap.get("KHBH")+"'", "ZQDM"):zqdmxs)+"\"/>");
			content.append("<input type='hidden' id='zqjcs' value=\""+(zqjcxs==null||zqjcxs.equals("")?DBUtil.getString("SELECT ZQJC FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"+hashMap.get("KHBH")+"'", "ZQJC"):zqjcxs)+"\"/>");
			content.append("</td>");
			content.append("<td id=\"data_NOTICENO\" class=\"td_data\" width=\"120px\">");
			// 公告编号
			String noticeno = hashMap.get("NOTICENO") != null ? hashMap.get("NOTICENO").toString() : "";
			content.append("<input type=hidden name='NOTICENO' value=\"" + noticeno + "\"><xmp>" + noticeno + "</xmp>&nbsp;");
			content.append("</td>");
			content.append("<td id=\"title_NOTICEDATE\" class=\"td_title\" width=\"90px\">");
			content.append("公告日期");
			content.append("</td>");
			content.append("<td id=\"data_NOTICEDATE\" class=\"td_data\" width=\"120px\"><xmp>");
			String noticedate = hashMap.get("NOTICEDATE") != null ? hashMap.get("NOTICEDATE").toString() : "";
			content.append(noticedate + "</xmp>&nbsp;");
			content.append("</td>");
			content.append("<td id=\"title_NOTICETYPE\" class=\"td_title\" width=\"90px\">");
			content.append("公告类型");
			content.append("</td>");
			content.append("<td id=\"data_NOTICETYPE\" class=\"td_data\" width=\"130px\">");
			// 公告类型
			String noticetype = hashMap.get("NOTICETYPE") != null ? hashMap.get("NOTICETYPE").toString() : "";
			String displayenum = DBUtil.getString("select DISPLAY_ENUM from sys_engine_iform_map t where field_name='NOTICETYPE' AND IFORM_ID='133'","DISPLAY_ENUM");
			if (displayenum != null && !displayenum.equals("")) {
				String[] displayArray = displayenum.split("\\|");
				content.append("<select disabled=\"disabled\" name=\"NOTICETYPE\" id=\"NOTICETYPE\" class=\"required\">");
				for (int i = 0; i < displayArray.length; i++) {
					String display = displayArray[i];
					content.append("<option "+(noticetype == null ? "" : (noticetype.equals(display) ? "selected=\"selected\"" : ""))+" value=\""+display+"\">"+display+"</option>");
				}
				content.append("</select>");
			}
			content.append("</td>");
			content.append("</tr>");
			
			String hyid = hashMap.get("MEETID")==null?"":hashMap.get("MEETID").toString();
			StringBuffer xpsxnames = new StringBuffer();
			if(hyid!=null&&!hyid.equals("")){
				StringBuffer sb = new StringBuffer("");
				sb.append("SELECT ID,MEETNAME FROM BD_MEET_PLAN WHERE ID IN(");
				String[] ids = hyid.split(",");
				for (int i = 0; i < ids.length; i++) {
					if(i==(ids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
				}
				sb.append(")");
				Connection conn=null;
				PreparedStatement ps = null;
				int i=0;
				ResultSet rs = null;
				try {
					conn = DBUtil.open();
					ps = conn.prepareStatement(sb.toString());
					for (int j = 0; j < ids.length; j++) {
						ps.setObject(j+1, ids[j]);
					}
					rs = ps.executeQuery();
					while(rs.next()){
						if(i!=0){
							xpsxnames.append("</br>");
						}
						i++;
						xpsxnames.append(i+"、<a href='javascript:checkGlhy("+rs.getString(1)+")'>"+rs.getString(2)+"</a>");
					}
				} catch (Exception e) {
					logger.error(e,e);
				} finally{
					DBUtil.close(conn, ps, rs);
				}
			}
			
			content.append("<tr id=\"itemTr_1800\">");
			content.append("<td id=\"title_SFCG\" class=\"td_title\" width=\"120\">");
			content.append("是否持股");
			content.append("</td>");
			content.append("<td id=\"data_SFCG\" class=\"td_data\" >");
			content.append(hashMap.get("SFCG") != null ? hashMap.get("SFCG").toString() : "");
			
			content.append("</td>");
			
		
			
			content.append("<td id=\"title_SFCG\" class=\"td_title\" width=\"120\">");
			content.append("限售办理时间");
			content.append("</td>");
			content.append("<td id=\"data_SFCG\" class=\"td_data\" >");
			content.append( hashMap.get("XSBLSJ") != null ? hashMap.get("XSBLSJ").toString() : "");
			content.append("</td>");
			
			content.append("<td id=\"title_SFCG\" class=\"td_title\" width=\"120\">");
			content.append("摘牌时间");
			content.append("</td>");
			content.append("<td id=\"data_SFCG\" class=\"td_data\" >");
			content.append( hashMap.get("ZPRQ") != null ? hashMap.get("ZPRQ").toString() : "");
			content.append("</td>");
			content.append("</tr>");
		
			
			content.append("<tr id=\"itemTr_1800\">");
			content.append("<td id=\"title_PAPERFILENO\" class=\"td_title\" width=\"120\">");
			content.append("是否要召开股东大会");
			content.append("</td>");
			content.append("<td id=\"data_PAPERFILENO\" class=\"td_data\" colspan=\"5\">");
			String PAPERFILENO = hashMap.get("PAPERFILENO") != null ? hashMap.get("PAPERFILENO").toString() : "";
			String COMPANYNO = hashMap.get("COMPANYNO") != null ? hashMap.get("COMPANYNO").toString() : "";
			content.append("<input type=hidden name='PAPERFILENO' value=\""+ PAPERFILENO + "\"><pre>" + PAPERFILENO + "&nbsp;");
			content.append("<input type=hidden name='COMPANYNO' id='COMPANYNO' value=\""+ COMPANYNO + "\">");
			content.append("<span id='gddh' >");
			content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;召开时间&nbsp;"+COMPANYNO+ "</pre>&nbsp;");
			content.append("</span>");
			content.append("</td>");
			content.append("</tr>");
			
			content.append("<tr id=\"itemTr_1709\">");
			content.append("<td id=\"title_NOTICENAME\" class=\"td_title\" width=\"120\">");
			content.append("公告名称");
			content.append("</td>");
			content.append("<td id=\"data_NOTICENAME\" class=\"td_data\" colspan=\"5\">");
			String noticename = hashMap.get("NOTICENAME") != null ? hashMap.get("NOTICENAME").toString() : "";
			content.append("<input type=hidden name='NOTICENAME' value=\""+ noticename + "\"><pre>" + noticename + "</pre>&nbsp;");
			content.append("</td>");
			content.append("</tr>");
			content.append("<tr id=\"itemTr_1910\">");
			content.append("<td id=\"title_GGZY\" class=\"td_title\" width=\"120\">");
			content.append("公告摘要");
			content.append("</td>");
			content.append("<td id=\"data_GGZY\" class=\"td_data\" colspan=\"5\">");
			String ggzy = hashMap.get("GGZY") != null ? hashMap.get("GGZY").toString() : "";
			content.append("<input type=hidden name='GGZY' value=\"" + ggzy+ "\"><pre>" + ggzy + "</pre>&nbsp;");
			content.append("</td>");
			content.append("</tr>");
			content.append("<tr id=\"itemTr_1704\">");
			content.append("<td id=\"title_XPSX\" class=\"td_title\" width=\"120\">");
			content.append("信披事项");
			content.append("</td>");
			content.append("<td id=\"data_XPSX\" class=\"td_data\" colspan=\"5\">");
			String id = hashMap.get("BZLX")==null?"":hashMap.get("BZLX").toString();
			StringBuffer xpsxname = new StringBuffer();
			if(id!= null&&!id.equals("")){
				Connection conn=null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				StringBuffer sb = new StringBuffer("");
				sb.append("SELECT ID,SXMC FROM BD_XP_XPSXB WHERE ID IN(");
				String[] ids = id.split(",");
				for (int i = 0; i < ids.length; i++) {
					if(i==(ids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
				}
				sb.append(")");
				int i=1;
				try {
					conn = DBUtil.open();
					ps = conn.prepareStatement(sb.toString());
					for (int j = 0; j < ids.length; j++) {
						ps.setObject(j+1, ids[j]);
					}
					rs = ps.executeQuery();
					while(rs.next()){
						if(i>1){
							xpsxname.append("</br>");
						}
						xpsxname.append((i++)+"、<a href='javascript:checkXpsx("+rs.getString(1)+")'>"+rs.getString(2)+"</a>");
					}
				} catch (Exception e) {
				} finally{
					DBUtil.close(conn, ps, rs);
				}
			}
			content.append(xpsxname + "&nbsp;");
			content.append("</td>");
			content.append("</tr>");
			content.append("<tr id=\"itemTr_1799\">");
			content.append("<td id=\"title_MEETNAME\" class=\"td_title\" width=\"120\">");
			content.append("关联会议");
			content.append("</td>");
			content.append("<td id=\"data_MEETNAME\" class=\"td_data\" colspan=\"5\">");
			String MEETNAME = hashMap.get("MEETNAME") != null ? hashMap.get("MEETNAME").toString() : "";
			content.append("<input type=hidden name='MEETNAME' value=\""+ MEETNAME + "\"><pre>" + xpsxnames + "</pre>&nbsp;");
			content.append("</td>");
			content.append("</tr>");
			content.append("<tr>");
			content.append("<td id=\"title_GGZY\" class=\"td_title\" width=\"120\">");
			content.append("</td>");
			content.append("<td class=\"td_data\" colspan=\"5\">");
			content.append("</td>");
			content.append("</tr>");
			
			content.append("<tr id=\"itemTr_1712\">");
			String noticefile = hashMap.get("NOTICEFILE") != null ? hashMap.get("NOTICEFILE").toString() : "";
			String companyname = hashMap.get("COMPANYNAME") != null ? hashMap.get("COMPANYNAME").toString() : "";
			content.append("<td id=\"data_NOTICEFILE\" class=\"td_data\" colspan=\"6\" style=\"padding:0px;border:0px;\">");
				content.append("<center>");
					content.append("<div>");
						content.append("<div>");
							content.append("<input size=\"100\" id=\"NOTICEFILE\" class=\"{maxlength:2048}\" name=\"NOTICEFILE\" value=\"").append(noticefile).append("\" type=\"hidden\">");
							content.append("<input size=\"100\" id=\"COMPANYNAME\" class=\"{maxlength:2048}\" name=\"COMPANYNAME\" value=\"").append(companyname).append("\" type=\"hidden\">");
						content.append("</div>");
					content.append("</div>");
					content.append("<table id=\"DIVNOTICEFILE\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" width=\"725px;\" cellspacing=\"0\" cellpadding=\"0\">");
						content.append("<tbody>");
							content.append("<tr align=\"center\">");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"120px;\">类型</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"230px;\">文件名称</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"180px;\">上传人</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"150px;\">上传时间</td>");
							content.append("<td style=\"border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"90px;\">查看</td>");
							content.append("</tr>");
							List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(noticefile);
							List<FileUpload> comfile = FileUploadAPI.getInstance().getFileUploads(companyname);
							String icon = "iwork_img/attach.png";
							Integer size = files.size();
							if(size>0){
								for (int i = 0; i < files.size(); i++) {
									FileUpload file = files.get(i);
									String filesrcname = file.getFileSrcName();
									String file_id = file.getFileId();
									String uploadtime = file.getUploadTime();
									icon = FileType.getFileIcon(filesrcname);
									String uploader = DBUtil.getString("SELECT USERNAME FROM BD_ZQB_WJTJ WHERE FILEUUID='"+file_id+"'", "USERNAME");
									if(!uploader.equals("")&&uploader!=null){
									uploader = uploader.substring(uploader.lastIndexOf("[")+1, uploader.lastIndexOf("]"));
									}
									int versionnum = DBUtil.getInt("SELECT COUNT(FILE_ID) NUM FROM IWORK_WEBOFFCIE_VERSION V WHERE V.RECORDID='"+file_id+"'", "NUM");
									if(i==0){
										content.append("<tr>");
										content.append("<td id=\"rowspantd\" rowspan=\"").append(size).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
										content.append(""+sm+"");
										content.append("</td>");
										content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\" align=\"left\">");
										content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\">").append(i+1+".").append(filesrcname).append("</a>&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;</td>");
										content.append("<td style=\"border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\" align=\"center\">");
										if(versionnum>0){
											content.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','NOTICEFILE');\">【历史版本】</a>");
										}
										content.append("</td>");
										content.append("</tr>");
									}else{
										content.append("<tr>");
										content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\" align=\"left\">");
										content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\">").append(i+1+".").append(filesrcname).append("</a>&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;</td>");
										content.append("<td style=\"border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\" align=\"center\">");
										if(versionnum>0){
											content.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','NOTICEFILE');\">【历史版本】</a>");
										}
										content.append("</td>");
										content.append("</tr>");
									}
								}
							}else{
								content.append("<tr>");
								content.append("<td id=\"rowspantd\" rowspan=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
								content.append(""+sm+"");
								content.append("</td>");
								content.append("<td title=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\" align=\"left\">");
								content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;&nbsp;</td>");
								content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;&nbsp;</td>");
								content.append("<td style=\"border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\" align=\"center\">");
								content.append("</td>");
								content.append("</tr>");
							}
							size = comfile.size();
							if(size>0){
								for (int i = 0; i < comfile.size(); i++) {
									FileUpload file = comfile.get(i);
									String filesrcname = file.getFileSrcName();
									String file_id = file.getFileId();
									String uploadtime = file.getUploadTime();
									icon = FileType.getFileIcon(filesrcname);
									String uploader = DBUtil.getString("SELECT USERNAME FROM BD_ZQB_WJTJ WHERE FILEUUID='"+file_id+"'", "USERNAME");
									if(!uploader.equals("")&&uploader!=null){
									uploader = uploader.substring(uploader.lastIndexOf("[")+1, uploader.lastIndexOf("]"));
									}
									int versionnum = DBUtil.getInt("SELECT COUNT(FILE_ID) NUM FROM IWORK_WEBOFFCIE_VERSION V WHERE V.RECORDID='"+file_id+"'", "NUM");
									if(i==0){
										content.append("<tr>");
										content.append("<td id=\"rowspantd\" rowspan=\"").append(size).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
										content.append(""+xm+"");
										content.append("</td>");
										content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\" align=\"left\">");
										content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\">").append(i+1+".").append(filesrcname).append("</a>&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;</td>");
										content.append("<td style=\"border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\" align=\"center\">");
										if(versionnum>0){
											content.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','NOTICEFILE');\">【历史版本】</a>");
										}
										content.append("</td>");
										content.append("</tr>");
									}else{
										content.append("<tr>");
										content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\" align=\"left\">");
										content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\">").append(i+1+".").append(filesrcname).append("</a>&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;</td>");
										content.append("<td style=\"border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\" align=\"center\">");
										if(versionnum>0){
											content.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','NOTICEFILE');\">【历史版本】</a>");
										}
										content.append("</td>");
										content.append("</tr>");
									}
								}
							}else{
								content.append("<tr>");
								content.append("<td id=\"rowspantd\" rowspan=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
								content.append(""+xm+"");
								content.append("</td>");
								content.append("<td title=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\" align=\"left\">");
								content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;&nbsp;</td>");
								content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;&nbsp;</td>");
								content.append("<td style=\"border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\" align=\"center\">");
								content.append("</td>");
								content.append("</tr>");
							}
						content.append("</tbody>");
					content.append("</table>");
				content.append("</center>");
			content.append("</td>");
			content.append("</tr>");
			
			content.append("<tr id=\"itemTr_1712\"><td class=\"td_data\" style=\"border-bottom: 0px dotted #999999;\"></td></tr>");
			
			content.append("<tr id=\"itemTr_1713\">");
			String smj ="";
			if(instanceId!=null&&instanceId!=0l){
				smj = DBUtil.getString("SELECT SMJ FROM BD_XP_GGSMJ WHERE GGINS="+instanceId, "SMJ");
			}
			content.append("<td id=\"data_GDSMFILE\" class=\"td_data\" colspan=\"6\" style=\"padding:0px;border:0px;\">");
			content.append("<center>");
				content.append("<div>");
					content.append("<div>");
						content.append("<input size=\"100\" id=\"GDSMFILE\" class=\"{maxlength:2048}\" name=\"NOTICEFILE\" value=\"").append(smj).append("\" type=\"hidden\">");
					content.append("</div>");
				content.append("</div>");
				content.append("<table id=\"DIVGDSMFILE\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" width=\"725px;\" cellspacing=\"0\" cellpadding=\"0\">");
					content.append("<tbody>");
						content.append("<tr align=\"center\">");
						content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"120px;\">类型</td>");
						content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"260px;\">文件名称</td>");
						content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"210px;\">上传人</td>");
						content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"180px;\">上传时间</td>");
						content.append("</tr>");
						List<FileUpload> filesmj = FileUploadAPI.getInstance().getFileUploads(smj);
						String iconsmj = "iwork_img/attach.png";
						Integer sizesmj = filesmj.size();
						if(sizesmj>0){
							for (int i = 0; i < filesmj.size(); i++) {
								FileUpload file = filesmj.get(i);
								String filesrcname = file.getFileSrcName();
								String file_id = file.getFileId();
								String uploadtime = file.getUploadTime();
								iconsmj = FileType.getFileIcon(filesrcname);
								String uploader = DBUtil.getString("SELECT USERNAME FROM BD_ZQB_WJTJ WHERE FILEUUID='"+file_id+"'", "USERNAME");
								if(!uploader.equals("")&&uploader!=null){
									uploader = uploader.substring(uploader.lastIndexOf("[")+1, uploader.lastIndexOf("]"));
									}
								int versionnum = DBUtil.getInt("SELECT COUNT(FILE_ID) NUM FROM IWORK_WEBOFFCIE_VERSION V WHERE V.RECORDID='"+file_id+"'", "NUM");
								if(i==0){
									content.append("<tr>");
									content.append("<td id=\"rowspantd\" rowspan=\"").append(sizesmj).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
									content.append("备查文件");
									content.append("</td>");
									content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"260px;\" align=\"left\">");
									content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(iconsmj).append("\">").append(filesrcname).append("</a>&nbsp;</td>");
									content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"210px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
									content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;</td>");
									if(versionnum>0){
										content.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','NOTICEFILE');\">【历史版本】</a>");
									}
									content.append("</td>");
									content.append("</tr>");
								}else{
									content.append("<tr>");
									content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"260px;\" align=\"left\">");
									content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(iconsmj).append("\">").append(filesrcname).append("</a>&nbsp;</td>");
									content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"210px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
									content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;</td>");
									if(versionnum>0){
										content.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','NOTICEFILE');\">【历史版本】</a>");
									}
									content.append("</td>");
									content.append("</tr>");
								}
							}
						}else{
							content.append("<tr>");
							content.append("<td id=\"rowspantd\" rowspan=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
							content.append("备查文件");
							content.append("</td>");
							content.append("<td title=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"260px;\" align=\"left\">");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"210px;\" align=\"center\">&nbsp;&nbsp;</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"180px;\" align=\"center\">&nbsp;&nbsp;</td>");
							content.append("</td>");
							content.append("</tr>");
						}
					content.append("</tbody>");
				content.append("</table>");
			content.append("</center>");
		content.append("</td>");
		content.append("</tr>");
		
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		if(orgUser.getOrgroleid()!=3L){
			List lables = new ArrayList();
			lables.add("CONTENT");
			lables.add("CONDATE");
			lables.add("CONUSER");
			String sql = "SELECT CONTENT,CONDATE,CONUSER FROM BD_XP_GGSMJ WHERE GGID=?";
			Map params = new HashMap();
			params.put(1, hashMap.get("ID"));
			List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
			String contenttext = "";
			String condate = "";
			String conuser = "";
			if(data.size()==1){
				contenttext = data.get(0).get("CONTENT")==null?"":data.get(0).get("CONTENT").toString();
				condate = data.get(0).get("CONDATE")==null?"":data.get(0).get("CONDATE").toString();
				conuser = data.get(0).get("CONUSER")==null?"":data.get(0).get("CONUSER").toString();
				if(!"".equals(contenttext)){
					content.append("<tr><td height=20px></td></tr>");
					content.append("<tr id=\"itemTr_1712\"><td colspan=6 class=\"td_data\" style=\"padding-left:80px;border-bottom: 0px dotted #999999;color:red;\">").append("【").append(conuser).append("，&nbsp;&nbsp;").append(condate).append("】").append("</br>").append(contenttext).append("</td></tr>");
				}
				
			}
		}
		
		content.append("</tbody>");
		content.append("</table>");
		content.append("<input type=\"hidden\" name=\"instanceId\" value=\"").append(hashMap.get("LCBS")).append("\" id=\"instanceId\"/>");
		content.append("<input type=\"hidden\" name=\"rcid\" value=\"").append(hashMap.get("YXID")).append("\" id=\"INSTANCEID\"/>");
		}
		return content.toString();
	}

	public String getUserTime(){
		HashMap result = new HashMap();
		result.put("USER", UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername());
		result.put("TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getTentUserDate(Long instanceId){
		StringBuffer sb = new StringBuffer("");
		List lables = new ArrayList();
		lables.add("CONTENT");
		lables.add("CONDATE");
		lables.add("CONUSER");
		String sql = "SELECT CONTENT,CONDATE,CONUSER FROM BD_XP_GGSMJ WHERE GGID=(SELECT ID FROM BD_MEET_QTGGZL WHERE NOTICESQ=(SELECT NOTICESQ FROM BD_XP_QTGGZLLC B WHERE B.INSTANCEID=?) AND KHBH=?)";
		Map params = new HashMap();
		params.put(1, instanceId==null?"0":instanceId.toString());
		HashMap datamap = null;
		if(instanceId!=null){
			datamap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		params.put(2, datamap==null?"0":datamap.get("KHBH").toString());
		List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
		String contenttext = "";
		String condate = "";
		String conuser = "";
		if(data.size()==1){
			contenttext = data.get(0).get("CONTENT")==null?"":data.get(0).get("CONTENT").toString();
			condate = data.get(0).get("CONDATE")==null?"":data.get(0).get("CONDATE").toString();
			conuser = data.get(0).get("CONUSER")==null?"":data.get(0).get("CONUSER").toString();
			sb.append("【").append(conuser).append("，&nbsp;&nbsp;").append(condate).append("】</br>").append(contenttext);
		}
		return sb.toString();
	}
	
	public String getTentUserDateForPro(Long instanceId){
//		String XMBH=null;
//		if(xmbh.contains("CZ")){
//			XMBH="并购项目";
//		}else if(xmbh.contains("SG")){
//			XMBH="收购项目管理";
//		}else if(xmbh.contains("YBCW")){
//			XMBH="一般性财务顾问项目";
//		}else if(xmbh.contains("PT")){
//			XMBH="定增项目（200人以上）";
//		}else if(xmbh.contains("PM")){
//			XMBH="定增项目（200人以内）";
//		}else{
//			XMBH="其他项目";
//		}
		StringBuffer sb = new StringBuffer("");
		List lables = new ArrayList();
		lables.add("CONTENT");
		lables.add("CONDATE");
		lables.add("CONUSER");
		String sql = "SELECT B.CONTENT,B.CONDATE,B.CONUSER FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148 AND B.GGINS=?";
//		
		Map params = new HashMap();
		
//		params.put(1, XMBH);
		params.put(1, instanceId==null?0:instanceId.toString());
		/*HashMap datamap = null;
		if(instanceId!=null){
			datamap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		params.put(2, datamap==null?"0":datamap.get("KHBH").toString());*/
		List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
		String contenttext = "";
		String condate = "";
		String conuser = "";
		if(data.size()>=1){
			for (HashMap hashMap : data) {
				
				contenttext =hashMap.get("CONTENT")==null?"":data.get(0).get("CONTENT").toString();
				condate = hashMap.get("CONDATE")==null?"":data.get(0).get("CONDATE").toString();
				conuser = hashMap.get("CONUSER")==null?"":data.get(0).get("CONUSER").toString();
				sb.append("【").append(conuser).append("，&nbsp;&nbsp;").append(condate).append("】</br>").append(contenttext);
			}
		}
		
		return sb.toString();
	}
	
	public String getTentUserDateForRc(Long instanceId){
		StringBuffer sb = new StringBuffer("");
		List lables = new ArrayList();
		lables.add("CONTENT");
		lables.add("CONDATE");
		lables.add("CONUSER");
		String sql = "SELECT CONTENT,CONDATE,CONUSER FROM BD_XP_GGSMJ WHERE GGINS=(SELECT INSTANCEID FROM BD_XP_RCYWCBZSJ WHERE YXID=?)";
		
	
		Map params = new HashMap();
		params.put(1, instanceId==null?"0":instanceId.toString());
		List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
		String contenttext = "";
		String condate = "";
		String conuser = "";
		if(data.size()==1){
			contenttext = data.get(0).get("CONTENT")==null?"":data.get(0).get("CONTENT").toString();
			condate = data.get(0).get("CONDATE")==null?"":data.get(0).get("CONDATE").toString();
			conuser = data.get(0).get("CONUSER")==null?"":data.get(0).get("CONUSER").toString();
			sb.append("【").append(conuser).append("，&nbsp;&nbsp;").append(condate).append("】</br>").append(contenttext);
		}
		return sb.toString();
	}
	
	public List<HashMap> count(String noticename,String noticetype, String startdate,
			String enddate, String khmc, String spzt, String ggzy, String zqdm,
			int pageNumber, int pageSize) {
		List list = new ArrayList<HashMap<String, Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		boolean isSuperMan = this.getIsSuperMan();
		StringBuffer sql=new StringBuffer();
		// 查询数据库
		// String userid = UserContextUtil.getInstance().getCurrentUserId();
		String gpdm = "";
		String gpdm1 = "";
		if (zqdm != null && !"".equals(zqdm)) {
			String[] split = zqdm.split(",");
			for (String string : split) {
				if (!"".equals(string) && string != null) {
					gpdm +=string + ",";
				}
			}
			gpdm1 = gpdm.substring(0, gpdm.length() - 1);
		}
		HashMap map=new HashMap();
		if(isSuperMan){
			sql.append("select * from (select gg.*,rownum r from (select b.*,c1.instanceid CusIns from (select a.id,f.id CusID,a.khbh,a.khmc,a.noticeno,a.noticename,a.noticetype,a.noticedate,a.ggzy,a.spzt,c.instanceid,f.zqjc,f.zqdm,bxq.ggdf from BD_MEET_QTGGZL a left join BD_XP_QTGGZLLC bxq on a.noticeno=bxq.noticeno and a.khbh=bxq.khbh ,SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f "
					+ "where f.customerno=a.khbh and formid = '133'and metadataid = '149' and a.id = c.dataid  and c.instanceid is not null ");
			if (!"".equals(gpdm1)) {
				String[] gpdmArr = gpdm1.split(",");
				String zc="";
				for (String string : gpdmArr) {
					zc += "?,";
				}
				String zzc = zc.substring(0, zc.length() - 1);
				map.put("zqdm", gpdm1);
				sql.append(" and  a.khbh in(select customerno from bd_zqb_kh_base where zqdm in (");
				sql.append(zzc + "))");
			}
			if (noticename != null && !"".equals(noticename)
					&& !"undefined".equals(noticename)) {
				sql.append(" and a.noticename like ?");
				map.put("noticename", noticename);
			}
			if (startdate != null && !"".equals(startdate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')>= ?");
				map.put("startdate", startdate);
			}
			if (enddate != null && !"".equals(enddate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')<= ?");
				map.put("enddate", enddate);
			}
			if (khmc != null && !khmc.equals("") && !"undefined".equals(khmc)) {
				sql.append(" and (a.khmc like ? or zqjc like ?) ");
				map.put("khmc", khmc);
			}
			if (noticetype != null && !noticetype.equals("")) {
				sql.append(" and a.noticetype = ? ");
				map.put("noticetype", noticetype);
			}
			if (spzt != null && !spzt.equals("") && !"undefined".equals(spzt)) {
				sql.append(" and spzt like ? ");
				map.put("spzt", spzt);
			}
			if (ggzy != null && !ggzy.equals("") && !"undefined".equals(ggzy)) {
				sql.append(" and a.ggzy like ? ");
				map.put("ggzy", ggzy);
			}
			sql.append(" ) b left join SYS_ENGINE_FORM_BIND c1 on b.CusID=c1.dataid and formid = '88'and metadataid = '102'and c1.instanceid is not null order by noticedate desc,b.id desc) gg) where  r>? and r<=?");
			map.put("startRow", startRow);
			map.put("endRow", endRow);
		}else{
			String userfullname = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			sql.append("select * from (select gg.*,rownum r from (select b.*,c1.instanceid CusIns from (select a.id,f.id CusID,a.khbh,a.khmc,a.noticeno,a.noticename,a.noticetype,a.noticedate,a.ggzy,a.spzt,c.instanceid,f.zqjc,f.zqdm,bxq.ggdf from BD_MEET_QTGGZL a left join BD_XP_QTGGZLLC bxq on a.noticeno=bxq.noticeno and a.khbh=bxq.khbh ,SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f "
							+ "where f.customerno=a.khbh and formid = '133'and metadataid = '149' and a.id = c.dataid  and c.instanceid is not null and f.CUSTOMERNO in (select KHBH from BD_MDM_KHQXGLB where KHFZR like ? or FHSPR like ? or ZZSPR like ? or CWSCBFZR2 like ? or CWSCBFZR3 like ? or ZZCXDD like ?) ");
			map.put("userfullname", userfullname);
			if (!"".equals(gpdm1)) {
				String[] gpdmArr = gpdm1.split(",");
				String zc="";
				for (String string : gpdmArr) {
					zc += "?,";
				}
				String zzc = zc.substring(0, zc.length() - 1);
				map.put("zqdm", gpdm1);
				sql.append(" and  a.khbh in(select customerno from bd_zqb_kh_base where zqdm in (");
				sql.append(zzc + "))");
			}
			if (noticename != null && !"".equals(noticename)
					&& !"undefined".equals(noticename)) {
				sql.append(" and a.noticename like ?");
				map.put("noticename", noticename);
			}
			if (startdate != null && !"".equals(startdate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')>= ?");
				map.put("startdate", startdate);
			}
			if (enddate != null && !"".equals(enddate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')<= ?");
				map.put("enddate", enddate);
			}
			if (khmc != null && !khmc.equals("") && !"undefined".equals(khmc)) {
				sql.append(" and (a.khmc like ? or zqjc like ?) ");
				map.put("khmc", khmc);
			}
			if (noticetype != null && !noticetype.equals("")) {
				sql.append(" and a.noticetype = ? ");
				map.put("noticetype", noticetype);
			}
			if (spzt != null && !spzt.equals("") && !"undefined".equals(spzt)) {
				sql.append(" and spzt like ? ");
				map.put("spzt", spzt);
			}
			if (ggzy != null && !ggzy.equals("") && !"undefined".equals(ggzy)) {
				sql.append(" and a.ggzy like ? ");
				map.put("ggzy", ggzy);
			}
			sql.append(" ) b left join SYS_ENGINE_FORM_BIND c1 on b.CusID=c1.dataid and formid = '88'and metadataid = '102'and c1.instanceid is not null order by noticedate desc,b.id desc) gg)where  r>? and r<=?");
			map.put("startRow", startRow);
			map.put("endRow", endRow);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
		    conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			int index=1;
			if(isSuperMan){
				if (!"".equals(gpdm1)) {
					String zqdmcs=map.get("zqdm").toString();
					String[] split = zqdmcs.split(",");
					for (int i = 0; i < split.length; i++) {
						stmt.setString(index++, split[i]);
					}
				}
				if (noticename != null && !"".equals(noticename)
						&& !"undefined".equals(noticename)) {
					stmt.setString(index++, "%"+map.get("noticename").toString()+"%");
				}
				if (startdate != null && !"".equals(startdate)) {
					stmt.setString(index++, map.get("startdate").toString());
				}
				if (enddate != null && !"".equals(enddate)) {
					stmt.setString(index++, map.get("enddate").toString());
				}
				if (khmc != null && !khmc.equals("") && !"undefined".equals(khmc)) {
					stmt.setString(index++, "%"+map.get("khmc").toString()+"%");
					stmt.setString(index++, "%"+map.get("khmc").toString()+"%");
				}
				if (noticetype != null && !noticetype.equals("")) {
					stmt.setString(index++, map.get("noticetype").toString());
				}
				if (spzt != null && !spzt.equals("") && !"undefined".equals(spzt)) {
					stmt.setString(index++, "%"+map.get("spzt").toString()+"%");
				}
				if (ggzy != null && !ggzy.equals("") && !"undefined".equals(ggzy)) {
					stmt.setString(index++, "%"+map.get("ggzy").toString()+"%");
				}
				stmt.setInt(index++, startRow);
				stmt.setInt(index++, endRow);
			}else{
				String userfullname = UserContextUtil.getInstance()
						.getCurrentUserFullName();
				stmt.setString(index++, "%"+userfullname+"%");
				stmt.setString(index++, "%"+userfullname+"%");
				stmt.setString(index++, "%"+userfullname+"%");
				stmt.setString(index++, "%"+userfullname+"%");
				stmt.setString(index++, "%"+userfullname+"%");
				stmt.setString(index++, "%"+userfullname+"%");
				if (!"".equals(gpdm1)) {
					String zqdmcs=map.get("zqdm").toString();
					String[] split = zqdmcs.split(",");
					for (int i = 0; i < split.length; i++) {
						stmt.setString(index++, split[i]);
					}
				}
				if (noticename != null && !"".equals(noticename)
						&& !"undefined".equals(noticename)) {
					stmt.setString(index++, "%"+map.get("noticename").toString()+"%");
				}
				if (startdate != null && !"".equals(startdate)) {
					stmt.setString(index++,map.get("startdate").toString());
				}
				if (enddate != null && !"".equals(enddate)) {
					stmt.setString(index++,map.get("enddate").toString());
				}
				if (khmc != null && !khmc.equals("") && !"undefined".equals(khmc)) {
					stmt.setString(index++, "%"+map.get("khmc").toString()+"%");
					stmt.setString(index++, "%"+map.get("khmc").toString()+"%");
				}
				if (noticetype != null && !noticetype.equals("")) {
					stmt.setString(index++, map.get("noticetype").toString());
				}
				if (spzt != null && !spzt.equals("") && !"undefined".equals(spzt)) {
					stmt.setString(index++, "%"+map.get("spzt").toString()+"%");
				}
				if (ggzy != null && !ggzy.equals("") && !"undefined".equals(ggzy)) {
					stmt.setString(index++, "%"+map.get("ggzy").toString()+"%");
				}
				stmt.setInt(index++, startRow);
				stmt.setInt(index++, endRow);
			}
			rs=stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> mapR = new HashMap<String, Object>();
				BigDecimal id = rs.getBigDecimal("ID");
				String bh = rs.getString("KHBH");
				String mc = rs.getString("KHMC");
				String noticeno = rs.getString("NOTICENO");
				String noticena = rs.getString("NOTICENAME");
				Date date = rs.getDate("NOTICEDATE");
				String noticedate = date != null ? sdf.format(date) : "";// 提问时间
				String zy = rs.getString("GGZY");
				String zt = rs.getString("SPZT");
				BigDecimal instanceid = rs.getBigDecimal("INSTANCEID");
				String zqjc = rs.getString("ZQJC");
				String dm = rs.getString("ZQDM");
				String rownum = rs.getString("R");
				BigDecimal cusins = rs.getBigDecimal("CusIns");
				String noticeType = rs.getString("NOTICETYPE");
				String ggdf = rs.getString("GGDF");
				mapR.put("ID", id);
				mapR.put("KHBH", bh);
				mapR.put("KHMC", mc);
				mapR.put("NOTICENO", noticeno);
				mapR.put("NOTICENAME", noticena);
				mapR.put("NOTICEDATE", noticedate);
				mapR.put("GGZY", zy);
				mapR.put("SPZT", zt);
				mapR.put("NOTICETYPE", noticeType);
				mapR.put("GGDF", ggdf);
				mapR.put("INSTANCEID", instanceid);
				mapR.put("ZQJC", zqjc);
				mapR.put("ZQDM", dm);
				mapR.put("XL", rownum);
				mapR.put("CusIns", cusins);
				list.add(mapR);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
		// TODO Auto-generated method stub

	}

	public boolean getIsSuperMan() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId!=null&&(orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9)))) {
				flag = true;
			}
		}
		return flag;
	}

	public void downloadThisNoticeFile(Long instanceId) throws Exception {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer")==null?"":config.get("zqServer");
		String sm="";
		String xm="";
		if(zqServer.equals("xnzq")){
			sm="公告附件";
			xm="佐证文件";
		}else{
			sm="披露文件";
			xm="底稿文件";
		}
		//复制附件到指定文件夹
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		
		HashMap formdata = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		String noticefile = formdata.get("NOTICEFILE").toString();
		String companyname = formdata.get("COMPANYNAME").toString();
		String bcwj = DBUtil.getString("select z.smj from bd_xp_ggsmj z where z.ggid=(select t.id from bd_meet_qtggzl t where t.noticesq=(select s.noticesq from BD_XP_QTGGZLLC s where s.instanceid="+instanceId+"))","smj");
		
		String foldernames = formdata.get("ZQDMXS")==null?formdata.get("KHMC").toString():formdata.get("ZQDMXS").toString()+"-"+formdata.get("NOTICENO").toString();
		List<FileUpload> fileobjs = FileUploadAPI.getInstance().getFileUploads(noticefile);
		List<FileUpload> comflieobjs = FileUploadAPI.getInstance().getFileUploads(companyname);
		List<FileUpload> bcwjobjs = FileUploadAPI.getInstance().getFileUploads(bcwj);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		Date date = new Date();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String foldername = user.getUserid()+sdf.format(date);
		int i=1;
		//公告附件|披露文件
		if(fileobjs.size()>0){
			for (FileUpload fileobj : fileobjs) {
				String srcname = fileobj.getFileSrcName();
				srcname = srcname.substring(0,srcname.lastIndexOf("."))+i+srcname.substring(srcname.lastIndexOf("."));
				//循环复制文件
				File f = new File(filepath+fileobj.getFileUrl());
				if(f.exists()){
					File fnew = new File(filepath+foldername+"\\"+sm);
					int byteread = 0;
					int bytesum = 0;
					InputStream inStream = new FileInputStream(f);
					if(!fnew.exists()){
						fnew.mkdirs();
						fnew = new File(filepath+foldername+"\\"+sm+"\\"+srcname);
						fnew.createNewFile();
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						} 
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}else{
						fnew = new File(filepath+foldername+"\\"+sm+"\\"+srcname);
						if(!fnew.exists()){
							fnew.createNewFile();
						}
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						}
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}
					i++;
				}
			}
		}else{
			File fnew = new File(filepath+foldername);
			if(!fnew.exists()){
				fnew.mkdirs();
			}
			fnew = new File(filepath+foldername+".zip");
			if(!fnew.exists()){
				fnew.createNewFile();
			}
		}
		//佐证文件|底稿文件
		if(comflieobjs.size()>0){
			for (FileUpload fileobj : comflieobjs) {
				String srcname = fileobj.getFileSrcName();
				srcname = srcname.substring(0,srcname.lastIndexOf("."))+i+srcname.substring(srcname.lastIndexOf("."));
				//循环复制文件
				File f = new File(filepath+fileobj.getFileUrl());
				if(f.exists()){
					File fnew = new File(filepath+foldername+"\\"+xm);
					int byteread = 0;
					int bytesum = 0;
					InputStream inStream = new FileInputStream(f);
					if(!fnew.exists()){
						fnew.mkdirs();
						fnew = new File(filepath+foldername+"\\"+xm+"\\"+srcname);
						fnew.createNewFile();
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						} 
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}else{
						fnew = new File(filepath+foldername+"\\"+xm+"\\"+srcname);
						if(!fnew.exists()){
							fnew.createNewFile();
						}
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						}
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}
					i++;
				}
			}
		}else{
			File fnew = new File(filepath+foldername);
			if(!fnew.exists()){
				fnew.mkdirs();
			}
			fnew = new File(filepath+foldername+".zip");
			if(!fnew.exists()){
				fnew.createNewFile();
			}
		}
		//备查文件
		if(bcwjobjs.size()>0){
			for (FileUpload fileobj : bcwjobjs) {
				String srcname = fileobj.getFileSrcName();
				srcname = srcname.substring(0,srcname.lastIndexOf("."))+i+srcname.substring(srcname.lastIndexOf("."));
				//循环复制文件
				File f = new File(filepath+fileobj.getFileUrl());
				if(f.exists()){
					File fnew = new File(filepath+foldername+"\\"+"备查文件");
					int byteread = 0;
					int bytesum = 0;
					InputStream inStream = new FileInputStream(f);
					if(!fnew.exists()){
						fnew.mkdirs();
						fnew = new File(filepath+foldername+"\\"+"备查文件"+"\\"+srcname);
						fnew.createNewFile();
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						} 
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}else{
						fnew = new File(filepath+foldername+"\\"+"备查文件"+"\\"+srcname);
						if(!fnew.exists()){
							fnew.createNewFile();
						}
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						}
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}
					i++;
				}
			}
		}else{
			File fnew = new File(filepath+foldername);
			if(!fnew.exists()){
				fnew.mkdirs();
			}
			fnew = new File(filepath+foldername+".zip");
			if(!fnew.exists()){
				fnew.createNewFile();
			}
		}
		//压缩文件夹
		/*ZipUtil zc = new  ZipUtil(filepath+foldername+".zip");  
        zc.compressExe(filepath+foldername); */
	try {
		final File result = new File(filepath+foldername+".zip");  
		createZipFile(filepath+foldername, result);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
		//下载压缩文件
        try {
	        InputStream fis = new BufferedInputStream(new FileInputStream(filepath+foldername+".zip"));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(foldernames+".zip"));
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
        } catch (Exception e) {logger.error(e,e);       	
        }finally{
        	//删除文件夹、压缩文件文件
        	File filedel = new File(filepath+foldername);
        	delChange(filedel);
        	File filedel2 = new File(filepath+foldername+".zip");
        	if(filedel2.exists()){
        		filedel2.delete();
        	}
        }
	}
	public void xxzcMbupload(){
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String filepath = request.getSession().getServletContext().getRealPath("/");
			filepath=filepath+"\\iwork_file\\信披自查问题管理模板.xls";
			try {
				InputStream fis = new BufferedInputStream(new FileInputStream(filepath));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();

				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("信披自查问题管理模板.xls"));
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			} catch (FileNotFoundException e) {
				
			} catch (Exception e) {
				
			}
	}
	public void downloadThisNoticesxFile(Long instanceId) throws Exception {
		//复制附件到指定文件夹
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		HashMap formdata = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String noticefile = formdata.get("SXFJ").toString();
		String foldernames = formdata.get("ZQDMXS")==null?formdata.get("KHMC").toString():formdata.get("ZQDMXS").toString()+"-"+formdata.get("NOTICENO").toString();
		List<FileUpload> fileobjs = FileUploadAPI.getInstance().getFileUploads(noticefile);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		Date date = new Date();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String foldername = user.getUserid()+sdf.format(date);
		int i=1;
		if(fileobjs.size()>0){
			for (FileUpload fileobj : fileobjs) {
				String srcname = fileobj.getFileSrcName();
				srcname = srcname.substring(0,srcname.lastIndexOf("."))+i+srcname.substring(srcname.lastIndexOf("."));
				//循环复制文件
				File f = new File(filepath+fileobj.getFileUrl());
				if(f.exists()){
					File fnew = new File(filepath+foldername);
					int byteread = 0;
					int bytesum = 0;
					InputStream inStream = new FileInputStream(f);
					if(!fnew.exists()){
						fnew.mkdirs();
						StringBuffer sb = new StringBuffer();
						sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(srcname);
						fnew = new File(sb.toString());
						fnew.createNewFile();
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						} 
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}else{
						StringBuffer sb = new StringBuffer();
						sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(srcname);
						fnew = new File(sb.toString());
						if(!fnew.exists()){
							fnew.createNewFile();
						}
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						}
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}
					i++;
				}
			}
		}else{
			File fnew = new File(filepath+foldername);
			if(!fnew.exists()){
				fnew.mkdirs();
			}
			fnew = new File(filepath+foldername+".zip");
			if(!fnew.exists()){
				fnew.createNewFile();
			}
		}
		//压缩文件夹
		/*ZipUtil zc = new  ZipUtil(filepath+foldername+".zip");  
        zc.compressExe(filepath+foldername); */
	try {
		final File result = new File(filepath+foldername+".zip");  
		createZipFile(filepath+foldername, result);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
		//下载压缩文件
        try {
	        InputStream fis = new BufferedInputStream(new FileInputStream(filepath+foldername+".zip"));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(foldernames+".zip"));
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
        } catch (Exception e) {logger.error(e,e);        	
        }finally{
        	//删除文件夹、压缩文件文件
        	File filedel = new File(filepath+foldername);
        	delChange(filedel);
        	File filedel2 = new File(filepath+foldername+".zip");
        	if(filedel2.exists()){
        		filedel2.delete();
        	}
        }
	}
	//批量下载公告模板
	public String batchdownload(String fileuuid,String instanceids) throws Exception {
		//复制附件到指定文件夹
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		Date date = new Date();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String foldername = user.getUserid()+sdf.format(date);
		
		List<FileUpload> fileobjs = new ArrayList<FileUpload>();
		if(fileuuid==null||fileuuid.equals("")){
			fileuuid = "";
			String[] insArr = instanceids.split(",");
			Long instanceid = 0l;
			HashMap data = new HashMap();
			String content = "";
			for (int i = 0; i < insArr.length; i++) {
				instanceid = Long.parseLong(insArr[i]);
				if(instanceid!=0l){
					data = DemAPI.getInstance().getFromData(instanceid);
					content = data.get("CONTENT")==null?"":data.get("CONTENT").toString();
					if(!content.equals("")){
						fileuuid+=(content+",");
					}
				}
			}
			fileuuid = fileuuid.substring(0, fileuuid.length()-1);
			fileobjs = FileUploadAPI.getInstance().getFileUploads(fileuuid);
		}else{
			fileobjs = FileUploadAPI.getInstance().getFileUploads(fileuuid);
		}
		int i=1;
		if(fileobjs.size()>0){
			for (FileUpload fileobj : fileobjs) {
				String srcname = fileobj.getFileSrcName();
				srcname = srcname.substring(0,srcname.lastIndexOf("."))+i+srcname.substring(srcname.lastIndexOf("."));
				//循环复制文件
				File f = new File(filepath+fileobj.getFileUrl());
				if(f.exists()){
					File fnew = new File(filepath+foldername);
					int byteread = 0;
					int bytesum = 0;
					InputStream inStream = new FileInputStream(f);
					if(!fnew.exists()){
						fnew.mkdirs();
						StringBuffer sb = new StringBuffer();
						sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(srcname);
						fnew = new File(sb.toString());
						fnew.createNewFile();
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						} 
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}else{
						StringBuffer sb = new StringBuffer();
						sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(srcname);
						fnew = new File(sb.toString());
						if(!fnew.exists()){
							fnew.createNewFile();
						}
						FileOutputStream fsot = new FileOutputStream(fnew);
						byte[] buffer = new byte[1444]; 
						while ( (byteread = inStream.read(buffer)) != -1) { 
							bytesum += byteread; 
							fsot.write(buffer, 0, byteread);
						}
						fsot.flush();
						inStream.close(); 
						fsot.close();
					}
					i++;
				}
			}
		}else{
			File fnew = new File(filepath+foldername);
			if(!fnew.exists()){
				fnew.mkdirs();
			}
			fnew = new File(filepath+foldername+".zip");
			if(!fnew.exists()){
				fnew.createNewFile();
			}
		}
		//压缩文件夹
		/*ZipUtil zc = new  ZipUtil(filepath+foldername+".zip");  
        zc.compressExe(filepath+foldername); */
	try {
		final File result = new File(filepath+foldername+".zip");  
		createZipFile(filepath+foldername, result);
	} catch (Exception e) {
		
	}  
	 return foldername;
	}
public void downGgmb(String foldername){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String foldernames = "公告模板批量下载文件";
		String filepath = request.getSession().getServletContext().getRealPath("/");
		//下载压缩文件
        try {
	        InputStream fis = new BufferedInputStream(new FileInputStream(filepath+foldername+".zip"));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(foldernames+".zip"));
	        
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
        } catch (Exception ex) {
        	
        }finally{
        	//删除文件夹、压缩文件文件
        	File filedel1 = new File(filepath+foldername);
        	delChange(filedel1);
        	File filedel2 = new File(filepath+foldername+".zip");
        	if(filedel2.exists()){
        		filedel2.delete();
        	}
        }
	}
	public void delChange(File file) {
        File[] files = file.listFiles();
        if (files != null)
            for (File f : files)
                delChange(f);
        file.delete();
    }
	
	public String getEventTreeJson(boolean superman) {
		StringBuffer json = new StringBuffer();
		HashMap conditionMap = new HashMap();
		String userFullName = UserContextUtil.getInstance()
				.getCurrentUserFullName();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_orgRole().getId();// 角色id
		List<OrgUserMap> usermaplist = UserContextUtil.getInstance()
				.getCurrentUserContext().get_userMapList();
		boolean isjrdd = false;
		if (usermaplist.size() > 0) {
			for (OrgUserMap oum : usermaplist) {
				String rolejrid = oum.getOrgroleid();
				if (rolejrid.equals("4")) {
					isjrdd = true;
					break;
				}
			}
		}
		List<HashMap> list = new ArrayList<HashMap>();
		// List<HashMap> l =
		// DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap,
		// null);
		List<HashMap> l = zqbAnnouncementDAO.getList();
		// 根据用户角色判断自己可以看到的客户
		// 场外角色
		if (superman || Integer.parseInt(roleid) == 9) {
			for (HashMap map : l) {
				map.put("id", map.get("ID") == null ? "" : map.get("ID")
						.toString());
				String zqdm = map.get("ZQDM") == null ? "" : map.get("ZQDM")
						.toString();
				String zqjc = map.get("ZQJC") == null ? "" : map.get("ZQJC")
						.toString();
				map.put("name", zqjc + "(" + zqdm + ")");
				map.put("khbh", map.get("KHBH"));
				map.put("khmc", map.get("KHMC"));
				map.put("open", true);
				map.put("ckecked", true);
				map.put("iconOpen", "iwork_img/package_add.png");
				map.put("iconClose", "iwork_img/package_delete.png");
				map.put("type", "customer");
				map.put("isedit", "true");
				list.add(map);
			}
		} else if (Integer.parseInt(roleid) == 4 || isjrdd) {// 持续督导或者兼任持续督导的角色或质控部负责人
			for (HashMap map : l) {
				// 督导角色所查询到的客户
				if ((map.get("KHFZR") != null
						&& !map.get("KHFZR").toString().equals("") && map
						.get("KHFZR").toString().equals

						(userFullName))
						|| (map.get("GGFBR") != null
								&& !map.get("GGFBR").toString().equals("") && map
								.get("GGFBR").toString

								().equals(userFullName))
						|| (map.get("ZZCXDD") != null
								&& !map.get("ZZCXDD").toString().equals("") && map
								.get("ZZCXDD").toString

								().equals(userFullName))
						|| (map.get("FHSPR") != null
								&& !map.get("FHSPR").toString().equals("") && map
								.get("FHSPR").toString

								().equals(userFullName))
						|| (map.get("ZZSPR") != null
								&& !map.get("ZZSPR").toString().equals("") && map
								.get("ZZSPR").toString

								().equals(userFullName))
						|| (map.get("GGFBR") != null
								&& !map.get("GGFBR").toString().equals("") && map
								.get("GGFBR").toString

								().equals(userFullName))
						|| (map.get("qynbrysh") != null
								&& !map.get("qynbrysh").toString().equals("") && map
								.get("qynbrysh").toString

								().equals(userFullName))) {
					map.put("id", map.get("ID") == null ? "" : map.get("ID")
							.toString());
					String zqdm = map.get("ZQDM") == null ? "" : map
							.get("ZQDM").toString();
					String zqjc = map.get("ZQJC") == null ? "" : map
							.get("ZQJC").toString();
					map.put("name", zqjc + "(" + zqdm + ")");
					map.put("khbh", map.get("KHBH"));
					map.put("khmc", map.get("KHMC"));
					map.put("open", true);
					map.put("ckecked", true);
					map.put("iconOpen", "iwork_img/package_add.png");
					map.put("iconClose", "iwork_img/package_delete.png");
					map.put("type", "customer");
					map.put("isedit", "true");
					list.add(map);
				}
			}
		} else if (Integer.parseInt(roleid) == 3) {// 董秘角色
			OrgUser orgUser = UserContextUtil.getInstance()
					.getCurrentUserContext()._userModel;
			String customerno = orgUser.getExtend1();
			String customername = orgUser.getExtend2();
			if (customerno != null && customername != null
					&& !customerno.equals("") && !customername.equals("")) {
				HashMap map = new HashMap();
				String zqjc = DBUtil.getString(
						"select ZQJC from bd_zqb_kh_base where customerno = '"
								+ customerno + "'", "ZQJC");
				String zqdm = DBUtil.getString(
						"select ZQDM from bd_zqb_kh_base where customerno = '"
								+ customerno + "'", "ZQDM");
				String jc = zqjc == null ? "" : zqjc;
				String dm = zqdm == null ? "" : zqdm;
				map.put("name", jc + "(" + dm + ")");
				map.put("khbh", customerno);
				map.put("khmc", customername);
				map.put("open", true);
				map.put("type", "customer");
				map.put("isedit", "true");
				list.add(map);
			}
		}
		List<HashMap> ll = new ArrayList<HashMap>();
		HashMap map=new HashMap();
		map.put("name", "挂牌公司");
		map.put("khbh", "0");
		map.put("children", list);
		map.put("open", true);
		map.put("type", "root");
		map.put("isedit", "false");//根节点不允许编辑
		ll.add(map);
		JSONArray jsonArray = JSONArray.fromObject(ll);
		json.append(jsonArray);
		return json.toString();
	}

	public int countTotal(String noticename,String noticetype, String startdate, String enddate,
			String khmc, String spzt, String ggzy, String zqdm) {
		List list = new ArrayList<HashMap<String, Object>>();
		int count = 0;
		boolean isSuperMan = this.getIsSuperMan();
		// 查询数据库
		// String userid = UserContextUtil.getInstance().getCurrentUserId();
		String gpdm = "";
		String gpdm1 = "";
		StringBuffer sql = new StringBuffer();
		if (zqdm != null && !"".equals(zqdm)) {
			String[] split = zqdm.split(",");
			for (String string : split) {
				if (!"".equals(string) && string != null) {
					gpdm += "'" + string + "',";
				}
			}
			gpdm1 = gpdm.substring(0, gpdm.length() - 1);
		}
		Map params = new HashMap();
		int n=1;
		if(isSuperMan){
			sql.append("select count(*) as count from (select b.*,rownum r from (select a.id,khbh,khmc,noticeno,noticename,noticedate,ggzy,spzt,c.instanceid,f.zqjc,f.zqdm from BD_MEET_QTGGZL a,SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f "
					+ "where f.customerno=a.khbh and formid = '133'and metadataid = '149' and a.id = c.dataid  and c.instanceid is not null ");
			if (!"".equals(gpdm1)) {
				sql.append(" and  khbh in(select customerno from bd_zqb_kh_base where zqdm in (");
				sql.append(gpdm1 + "))");
			}
			if (noticename != null && !"".equals(noticename) && !"undefined".equals(noticename)) {
				sql.append(" and a.noticename like ? ");
				params.put(n, "%" + noticename + "%");
				n++;
			}
			if (startdate != null && !"".equals(startdate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')>= ? ");
				params.put(n, startdate);
				n++;
			}
			if (enddate != null && !"".equals(enddate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')<= ? ");
				params.put(n, enddate);
				n++;
			}
			if (khmc != null && !khmc.equals("") && !"undefined".equals(khmc)) {
				sql.append(" and (khmc like ? or zqjc like ? ) ");
				params.put(n, "%" + khmc + "%");
				n++;
				params.put(n, "%" + khmc + "%");
				n++;
			}
			if (noticetype != null && !noticetype.equals("")) {
				sql.append(" and noticetype = ?  ");
				params.put(n, spzt);
				n++;
			}
			if (spzt != null && !spzt.equals("") && !"undefined".equals(spzt)) {
				sql.append(" and spzt like ? ");
				params.put(n, "%" + spzt + "%");
				n++;
			}
			if (ggzy != null && !ggzy.equals("") && !"undefined".equals(ggzy)) {
				sql.append(" and ggzy like ? ");
				params.put(n, "%" + ggzy + "%");
				n++;
			}
			sql.append(" order by noticedate desc,id desc) b )");
		}else{
			String userfullname = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			sql.append("select count(*) as count from (select b.*,rownum r from (select a.id,khbh,khmc,noticeno,noticename,noticedate,ggzy,spzt,c.instanceid,f.zqjc,f.zqdm from BD_MEET_QTGGZL a,SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f "
							+ "where f.customerno=a.khbh and formid = '133'and metadataid = '149' and a.id = c.dataid  and c.instanceid is not null and f.CUSTOMERNO in (select KHBH from BD_MDM_KHQXGLB where KHFZR like '%"
							+ userfullname
							+ "%' or FHSPR like '%"
							+ userfullname
							+ "%' or ZZSPR like '%"
							+ userfullname
							+ "%' or CWSCBFZR2 like '%"
							+ userfullname
							+ "%' or CWSCBFZR3 like '%"
							+ userfullname
							+ "%' or ZZCXDD like '%" + userfullname + "%') ");
			if (!"".equals(gpdm1)) {
				sql.append(" and  khbh in(select customerno from bd_zqb_kh_base where zqdm in (");
				sql.append(gpdm1 + "))");
			}
			if (noticename != null && !"".equals(noticename) && !"undefined".equals(noticename)) {
				sql.append(" and a.noticename like ? ");
				params.put(n, "%" + noticename + "%");
				n++;
			}
			if (startdate != null && !"".equals(startdate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')>= ? ");
				params.put(n, startdate);
				n++;
			}
			if (enddate != null && !"".equals(enddate)) {
				sql.append(" and to_char(a.noticedate,'yyyy-MM-dd')<= ? ");
				params.put(n, enddate);
				n++;
			}
			if (khmc != null && !khmc.equals("") && !"undefined".equals(khmc)) {
				sql.append(" and (khmc like ? or zqjc like ? ) ");
				params.put(n, "%" + khmc + "%");
				n++;
				params.put(n, "%" + khmc + "%");
				n++;
			}
			if (noticetype != null && !noticetype.equals("")) {
				sql.append(" and noticetype = ? ");
				params.put(n, spzt);
				n++;
			}
			if (spzt != null && !spzt.equals("") && !"undefined".equals(spzt)) {
				sql.append(" and spzt like ? ");
				params.put(n, "%" + spzt + "%");
				n++;
			}
			if (ggzy != null && !ggzy.equals("") && !"undefined".equals(ggzy)) {
				sql.append(" and ggzy like ? ");
				params.put(n, "%" + ggzy + "%");
				n++;
			}
			sql.append(" order by noticedate desc,id desc) b )");
		}
		count = DBUTilNew.getInt("count",sql.toString(),params);
		return count;
	}

	public void doExcelExp(HttpServletResponse response) {
		// TODO Auto-generated method stub
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("信息披露统计");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setFillForegroundColor(HSSFColor.LIME.index);
		style4.setFillPattern((short) 1);
		style4.setBorderRight((short) 1);
		style4.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillPattern((short) 1);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setWrapText(true);// 自动换行
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBottomBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setBorderLeft((short) 1);
		style6.setLeftBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setBorderRight((short) 1);
		style6.setRightBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setBorderTop((short) 1);
		style6.setTopBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setFont(contentfont);
		HSSFCellStyle style7 = wb.createCellStyle();
		style7.setWrapText(true);// 自动换行
		style7.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style7.setFillForegroundColor(HSSFColor.WHITE.index);
		style7.setFillPattern((short) 1);
		style7.setBorderBottom((short) 1);
		style7.setBottomBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style7.setBorderLeft((short) 1);
		style7.setLeftBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style7.setBorderRight((short) 1);
		style7.setRightBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style7.setBorderTop((short) 1);
		style7.setTopBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style7.setFont(headfont);

		HSSFFont ftRed = wb.createFont();
		ftRed.setColor(HSSFColor.RED.index);

		HSSFFont ftBlue = wb.createFont();
		ftBlue.setColor(HSSFColor.BLUE.index);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int z = 0;
		int n = 0;
		int m = 0;
		int count = 0;
		short zqdmLength = 0;
		short zqjcLength = 0;
		list1 = getAnnouncementCustomerList();
		for (HashMap hashMap : list1) {
			HSSFRow row1 = sheet.createRow((int) z++);
			row1.setRowStyle(style5);
			HSSFRow row2 = sheet.createRow((int) z++);
			HSSFCell cell1 = row1.createCell((short) n++);
			cell1.setCellValue("证券代码");
			cell1.setCellStyle(style5);
			cell1 = row1.createCell((short) n++);
			cell1.setCellValue("证券简称");
			cell1.setCellStyle(style4);
			List<HashMap> list2 = new ArrayList<HashMap>();
			String khbh = hashMap.get("KHBH") == null ? "" : hashMap
					.get("KHBH").toString();
			list2 = getNoticeDate(khbh);
			if (count < list2.size()) {
				count = list2.size();
			}
			HSSFCell cell2 = row2.createCell((short) m++);
			cell2.setCellValue(hashMap.get("ZQDM") == null ? "" : hashMap.get(
					"ZQDM").toString());
			cell2.setCellStyle(style7);
			HSSFCell cell3 = row2.createCell((short) m++);
			cell3.setCellValue(hashMap.get("ZQJC") == null ? "" : hashMap.get(
					"ZQJC").toString());
			cell3.setCellStyle(style7);
			short colLength = (short) (hashMap.get("ZQDM") == null ? 0
					: hashMap.get("ZQDM").toString().length() * 256 * 2);
			if (zqdmLength < colLength) {
				zqdmLength = colLength;
			}
			short colLength2 = (short) (hashMap.get("ZQJC") == null ? 0
					: hashMap.get("ZQJC").toString().length() * 256 * 2);
			if (zqjcLength < colLength2) {
				zqjcLength = colLength2;
			}
			int stringCount = 0;
			for (HashMap hashMap2 : list2) {
				cell1 = row1.createCell((short) n++);
				cell1.setCellValue(hashMap2.get("NOTICEDATE").toString());
				cell1.setCellStyle(style5);
				List<HashMap> list3 = new ArrayList<HashMap>();
				list3 = getNoticeName(hashMap2.get("NOTICEDATE").toString(),
						hashMap.get("KHBH").toString());
				StringBuffer content = new StringBuffer();
				for (HashMap hashMap3 : list3) {
					String noticename = hashMap3.get("NOTICENAME") == null ? ""
							: hashMap3.get("NOTICENAME").toString();
					String ggzy = hashMap3.get("GGZY") == null ? "" : hashMap3
							.get("GGZY").toString();
					content.append(noticename + " ：" + ggzy + "\n");
				}
				String[] split = content.toString().split("\\n");
				HSSFRichTextString textString = new HSSFRichTextString(
						content.toString());
				for (String string : split) {
					String[] split2 = string.split(" ：");
					if (split2[0].endsWith("提示")) {
						textString.applyFont(
								content.toString().indexOf(split2[0],
										stringCount), content.toString()
										.indexOf(split2[0], stringCount)
										+ split2[0].length(), ftRed);
					} else {
						textString.applyFont(
								content.toString().indexOf(split2[0],
										stringCount), content.toString()
										.indexOf(split2[0], stringCount)
										+ split2[0].length(), ftBlue);
					}
					stringCount = string.length()
							+ content.toString()
									.indexOf(split2[0], stringCount);
				}
				stringCount = 0;
				HSSFCell cell4 = row2.createCell((short) m++);
				cell4.setCellValue(textString);
				cell4.setCellStyle(style6);
			}
			n = 0;
			m = 0;
		}
		sheet.setColumnWidth(1, zqdmLength);
		sheet.setColumnWidth(2, zqjcLength);
		for (int i = 0; i < count; i++) {
			sheet.setColumnWidth(i + 2, 15000);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("信息披露汇总表.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
			// out1.flush();
			// out1.close();

		} catch (Exception e) {logger.error(e,e);
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

	private List<HashMap> getNoticeName(String noticedate, String khbh) {
		List list = new ArrayList<HashMap<String, Object>>();
		boolean isSuperMan = this.getIsSuperMan();
		String userfullname = UserContextUtil.getInstance().getCurrentUserFullName();
		StringBuffer sql = new StringBuffer("select noticename,ggzy from BD_MEET_QTGGZL where noticedate = to_date(?,'yyyy-mm-dd')  and khbh=?");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt=conn.prepareStatement(sql.toString());
			stmt.setString(1, noticedate);
			stmt.setString(2, khbh);
			rs=stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String noticename = rs.getString(1);
				String ggzy = rs.getString(2);
				map.put("NOTICENAME", noticename);
				map.put("GGZY", ggzy);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}

	private List<HashMap> getNoticeDate(String khbh) {
		List list = new ArrayList<HashMap<String, Object>>();
		boolean isSuperMan = this.getIsSuperMan();
		String userfullname = UserContextUtil.getInstance().getCurrentUserFullName();
		StringBuffer sql = new StringBuffer("select noticedate from BD_MEET_QTGGZL where khbh =? group by noticedate order by noticedate desc ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, khbh);
			rs=stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				Date date = rs.getDate(1);
				String noticedate = date != null ? sdf.format(date) : "";// 提问时间
				map.put("NOTICEDATE", noticedate);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}

	private List<HashMap> getAnnouncementCustomerList() {
		List list = new ArrayList<HashMap<String, Object>>();
		boolean isSuperMan = this.getIsSuperMan();
		StringBuffer sql = new StringBuffer();
		String userfullname="";
		if(isSuperMan){
			sql.append("select zqdm,zqjc,khbh from (select b.*,rownum r from (select a.id,khbh,khmc,noticeno,noticename,noticedate,ggzy,spzt,c.instanceid,f.zqjc,f.zqdm from BD_MEET_QTGGZL a,SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f where f.customerno=a.khbh and formid = '133'and metadataid = '149' and a.id = c.dataid  and c.instanceid is not null ");
			sql.append(" order by noticedate desc,id desc) b ) group by zqdm,zqjc,khbh");
		}else{
			userfullname = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			sql.append("select zqdm,zqjc,khbh from (select b.*,rownum r from (select a.id,khbh,khmc,noticeno,noticename,noticedate,ggzy,spzt,c.instanceid,f.zqjc,f.zqdm from BD_MEET_QTGGZL a,SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f where f.customerno=a.khbh and formid = '133'and metadataid = '149' and a.id = c.dataid  and c.instanceid is not null and f.CUSTOMERNO in (select KHBH from BD_MDM_KHQXGLB where KHFZR like ? or FHSPR like ? or ZZSPR like ? or CWSCBFZR2 like ? or CWSCBFZR3 like ? or ZZCXDD like ?) ");
			sql.append(" order by noticedate desc,id desc) b ) group by zqdm,zqjc,khbh");
		}
		
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			if(!isSuperMan){
				stmt.setString(1, "%"+userfullname+"%");
				stmt.setString(2, "%"+userfullname+"%");
				stmt.setString(3, "%"+userfullname+"%");
				stmt.setString(4, "%"+userfullname+"%");
				stmt.setString(5, "%"+userfullname+"%");
				stmt.setString(6, "%"+userfullname+"%");
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String zqdm = rs.getString(1);
				String zqjc = rs.getString(2);
				String khbh = rs.getString(3);
				map.put("ZQJC", zqjc);
				map.put("ZQDM", zqdm);
				map.put("KHBH", khbh);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}

	private List<HashMap> getAnnouncementList() {
		// TODO Auto-generated method stub
		List list = new ArrayList<HashMap<String, Object>>();
		boolean isSuperMan = this.getIsSuperMan();
		// 查询数据库
		// String userid = UserContextUtil.getInstance().getCurrentUserId();
		String gpdm = "";
		String gpdm1 = "";
		String userfullname = UserContextUtil.getInstance().getCurrentUserFullName();
		StringBuffer sql = new StringBuffer(
		"select * from (select b.*,rownum r from (select a.id,khbh,khmc,noticeno,noticename,noticedate,ggzy,spzt,c.instanceid,f.zqjc,f.zqdm from BD_MEET_QTGGZL a,SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f "
		+ "where f.customerno=a.khbh and formid = '133'and metadataid = '149' and a.id = c.dataid  and c.instanceid is not null and f.CUSTOMERNO in (select KHBH from BD_MDM_KHQXGLB where KHFZR like ? or FHSPR like ? or ZZSPR like ? or CWSCBFZR2 like ? or CWSCBFZR3 like ? or ZZCXDD like ?) ");
		sql.append(" order by noticedate desc,id desc) b )");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, "%"+userfullname+"%");
			stmt.setString(2, "%"+userfullname+"%");
			stmt.setString(3, "%"+userfullname+"%");
			stmt.setString(4, "%"+userfullname+"%");
			stmt.setString(5, "%"+userfullname+"%");
			stmt.setString(6, "%"+userfullname+"%");
			rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				BigDecimal id = rs.getBigDecimal(1);
				String bh = rs.getString(2);
				String mc = rs.getString(3);
				String noticeno = rs.getString(4);
				String noticena = rs.getString(5);
				Date date = rs.getDate(6);
				String noticedate = date != null ? sdf.format(date) : "";// 提问时间
				String zy = rs.getString(7);
				String zt = rs.getString(8);
				BigDecimal instanceid = rs.getBigDecimal(9);
				String zqjc = rs.getString(10);
				String dm = rs.getString(11);
				String rownum = rs.getString(12);
				map.put("ID", id);
				map.put("KHBH", bh);
				map.put("KHMC", mc);
				map.put("NOTICENO", noticeno);
				map.put("NOTICENAME", noticena);
				map.put("NOTICEDATE", noticedate);
				map.put("GGZY", zy);
				map.put("SPZT", zt);
				map.put("INSTANCEID", instanceid);
				map.put("ZQJC", zqjc);
				map.put("ZQDM", dm);
				map.put("XL", rownum);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}
	
	private synchronized void senMsg(HashMap hashmap,String ggid) {
		TestSendMes tms=new TestSendMes();
		 UserContext ucs = UserContextUtil.getInstance().getCurrentUserContext();
		//tms.sendMsgList(this.receiveUser,ucs._userModel.getUsername(),this.instanceId,"提交",this.title);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String user_id = uc.get_userModel().getUserid();
		List<HashMap> l = zqbAnnouncementDAO.getHfqkbByGgid(ggid);//反馈人员
		List<HashMap> xmjbr = zqbAnnouncementDAO.getXMJBRbByGgid(ggid);//项目经办人
		//反馈人员与项目经办人可能会有重复这里过滤掉项目经办人中重复的数据
		String userid = "";
		String jbr = "";
		for (int i = 0; i < l.size(); i++) {
			userid = l.get(i).get("USERID").toString();
			if(userid.equals(user_id)){
				l.remove(i);
				continue;
			}
			for (int j = 0; j < xmjbr.size(); j++) {
				jbr = xmjbr.get(j).get("USERID").toString();
				if(jbr.equals(userid)||jbr.equals(user_id)){
					xmjbr.remove(j);
				}
			}
		}
		l.addAll(xmjbr);
		String smsContent = "";
		if (hashmap != null) {
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.TZGG_ADD_KEY,hashmap);
		}
		HashMap data = null;
		String hfqkbuuid = this.getConfigUUID("hfqkbuuid");
		Long instanceId;
		Long dataid;
		for (HashMap user : l) {
			try {
				tms.sendTzggMsgList(user, ucs._userModel.getUsername() ,hashmap.get("TZBT").toString());
			} catch (Exception e) {}
			if (!smsContent.equals("")) {
				Object mobile = user.get("MOBILE");
				if (mobile != null && !mobile.equals("")) {
					try {
						boolean flag = MessageAPI.getInstance().sendSMS(uc,mobile.toString(), smsContent);
						
						instanceId = Long.parseLong(user.get("INSTANCEID").toString());
						if(flag&&instanceId!=0l){//更新通知状态
							data = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
							dataid = Long.parseLong(data.get("ID").toString());
							data.put("TZZT", 1);
							DemAPI.getInstance().updateFormData(hfqkbuuid, instanceId, data, dataid, false);;
						}
					} catch (Exception e) {}
				}
				Object email = user.get("EMAIL");
				if(email!=null&&!email.equals("") && uc != null){
					try {
						MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email.toString(), "通知公告提醒",smsContent,"");
					} catch (Exception e) {}
				}
			}
		}
	}
	
	public String getproblems(String ggid){
		List lables = new ArrayList();lables.add("QUESTION");lables.add("DEFULTANSWER");
		String sql = "SELECT D.QUESTION,D.DEFULTANSWER FROM BD_XP_TZGGB T LEFT JOIN BD_VOTE_WJDCWTB D ON T.WJFILE=D.TAG WHERE D.TAG IS NOT NULL AND T.ID=? ORDER BY D.SORTID";
		Map params = new HashMap();params.put(1, ggid);
		List<HashMap> problems = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
		StringBuffer jsonHhtml = new StringBuffer();
		HashMap result = new HashMap();
		String q = "";
		String a = "";
		for (int i = 0; i < 50; i++) {
			if(i>problems.size()-1){
				q="";
				a="";
			}else{
				q = problems.get(i).get("QUESTION")==null?"":problems.get(i).get("QUESTION").toString();
				a = problems.get(i).get("DEFULTANSWER")==null?"":problems.get(i).get("DEFULTANSWER").toString();
			}
			result.put("WT"+(i+1), q);
			result.put("AS"+(i+1), a);
		}
		JSONArray json = JSONArray.fromObject(result);
		jsonHhtml.append(json);
		return jsonHhtml.toString();
	}
	
	public void impWjdc(String uuid){
		Long starttime = new Date().getTime();
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(uuid);
		OrgUser user =UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		String rootPath=request.getRealPath("/");
		//String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
		String question = null;
		Long insid = 0l;
		String userid = user.getUserid();
		String username = user.getUsername();
		File file = null;
		Workbook workBook = null;
		Sheet sheet = null;
		Map params = new HashMap();
		params.put(1, uuid);
		DBUTilNew.update("DELETE FROM BD_VOTE_WJDCWTB WHERE TAG= ? ",params);
		String demUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE S WHERE S.TITLE = '信披自查问题表单'", "UUID");
		Integer maxSortid = DBUtil.getInt("SELECT MAX(SORTID) SORTID FROM BD_VOTE_WJDCWTB", "SORTID");
		
		for (FileUpload fileUpload : sublist) {
			//加载xls文件
			file = new File(rootPath+File.separator+fileUpload.getFileUrl());
			
			try {workBook = new XSSFWorkbook(rootPath+File.separator+fileUpload.getFileUrl());  
			} catch (Exception ex) { logger.error(ex);
				try {workBook = new HSSFWorkbook(new FileInputStream(rootPath+File.separator+fileUpload.getFileUrl()));
				} catch (FileNotFoundException e) {logger.error(e,e);} catch (Exception e) {logger.error(e,e);}
			}
			if(workBook==null){continue;}
			//遍历sheet
			for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {  
				sheet = workBook.getSheetAt(numSheet);  
				if (sheet == null) {continue;}
				// 循环行Row  
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {  
					Row row = sheet.getRow(rowNum);
					if (row == null) {continue;}
					// 循环列Cell
					Cell cell = row.getCell(0);
					if (cell == null) {continue;}
					question = getValue(cell);
					insid = DemAPI.getInstance().newInstance(demUUID, userid);
					HashMap hashdata = new HashMap();
					hashdata.put("QUESTION", question);
					hashdata.put("DEFULTANSWER", "否");
					hashdata.put("STATUS", "显示");
					hashdata.put("SORTID", maxSortid+rowNum);
					hashdata.put("TYPE", "");
					hashdata.put("TAG", uuid);
					DemAPI.getInstance().saveFormData(demUUID, insid, hashdata, false);
				}
			}
		}
	}
	
	public String save(String tzbt, String zchfsj, String tznr, String XGZL,
			String hfr, String sftz, String ggid2,String tzlx,String wjfile,String dctm,String extend5)  {
		boolean saveFormData = false;
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("tzggbuuid");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String username = userModel.getUsername();
		String userid = userModel.getUserid();
		String ggid = "";
		if (ggid2 != null && !"".equals(ggid2)) {
			ggid = ggid2;
			HashMap formData = DemAPI.getInstance().getFormData(demUUID,Long.parseLong(ggid2));
			String[] split = XGZL.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			XGZL = sb.toString();
			formData.put("TZBT", tzbt);
			formData.put("ZCHFSJ", zchfsj);
			formData.put("TZNR", tznr);
			formData.put("XGZL", XGZL);
			formData.put("HFR", "");
			formData.put("SFTZ", sftz);
			formData.put("FSR", username);
			String format = sd.format(new Date());
			formData.put("FSSJ", format);
			formData.put("TZLX", tzlx);
			formData.put("WJFILE", wjfile);
			formData.put("DCTM", dctm);
			formData.put("EXTEND5", extend5);
			formData.put("FSZT", "未完成");
			formData.put("JSZT", "未回复");
			Map params = new HashMap();
			params.put(1, ggid2);
			Long instanceid = DBUTilNew.getLong("instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and dataid= ? ", params);
			saveFormData = DemAPI.getInstance().updateFormData(demUUID,instanceid, formData, Long.parseLong(ggid2), false);
			if ("是".equals(sftz)) {				
				senMsg(formData,ggid2);
			}			
			HashMap conditionMap = new HashMap();
			conditionMap.put("GGID", ggid2);
			List<HashMap> list2 = DemAPI.getInstance().getList(config.get("hfqkbuuid"), conditionMap, null);
			if (hfr != null && !"".equals(hfr)) {
				String[] hfrArr = hfr.split(",");
				boolean equals = false;
				List<HashMap> list = new ArrayList();
				Connection conn = null;
				PreparedStatement stmt = null;
				ResultSet rset = null;
				if(hfrArr!=null&&hfrArr.length>0){
					conn = DBUtil.open();
				}
				for (String string : hfrArr) {
					String repeatUserId = string.substring(0, string.indexOf("["));
					for (HashMap hashMap : list2) {
						equals = repeatUserId.equals(hashMap.get("USERID").toString().trim());
						if (equals) {
							break;
						}
					}
					if (equals) {
						continue;
					}
					StringBuffer sql = new StringBuffer();
					sql.append("select a.userid as userid,a.extend1 as customerno,a.username as username,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel from orguser a left join bd_zqb_kh_base b on a.extend1=b.customerno where a.userid =?");
					try{
						stmt =conn.prepareStatement(sql.toString());
						stmt.setString(1, repeatUserId);
						rset = stmt.executeQuery();
						while (rset.next()) {
							HashMap map = new HashMap();
							String uname = rset.getString("username");
							String uid = rset.getString("userid");
							String customerno = rset.getString("customerno");
							String departmentname = rset.getString("departmentname");
							String orgroleid = rset.getString("orgroleid");
							String mobile = rset.getString("mobile");
							String zqdm = rset.getString("zqdm");
							String tel = rset.getString("tel");
							map.put("USERID", uid);
							map.put("CUSTOMERNO", customerno);
							map.put("XM", uname);
							map.put("GSMC", departmentname);
							map.put("GSDM", zqdm);
							map.put("SJH", mobile);
							list.add(map);
						}
					}catch (Exception e) {
						logger.error(e,e);
					}finally{
						DBUtil.close(conn, stmt, rset);
					}
				}
				
				for (HashMap map : list) {
					map.put("formid", config.get("hfqkbformid"));
					map.put("GGID", ggid);
					map.put("STATUS", "未回复");
					Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
					DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
				}
			}
			HashMap map = DemAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			String value=map.get("TZBT")==null?"":map.get("TZBT").toString();
			Long dataId=Long.parseLong(map.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "通知公告", "编辑通知公告："+value);
		} else {
			HashMap hashmap = new HashMap();
			String[] split = XGZL.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			XGZL = sb.toString();
			Long instanceid = DemAPI.getInstance().newInstance(demUUID,userid);
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", config.get("tzggbformid"));
			hashmap.put("TZBT", tzbt);
			hashmap.put("ZCHFSJ", zchfsj);
			hashmap.put("TZNR", tznr);
			hashmap.put("XGZL", XGZL);
			hashmap.put("HFR", "");
			hashmap.put("SFTZ", sftz);
			hashmap.put("FSR", username);
			String format = sd.format(new Date());
			hashmap.put("FSSJ", format);
			hashmap.put("TZLX", tzlx);
			hashmap.put("WJFILE", wjfile);
			hashmap.put("DCTM", dctm);
			hashmap.put("EXTEND5", extend5);
			hashmap.put("FSZT", "未完成");
			hashmap.put("JSZT", "未回复");
			hashmap.put("FSRID", userid);
			saveFormData = DemAPI.getInstance().saveFormData(demUUID,instanceid, hashmap, false);
			ggid = DBUtil.getString("select DATAID from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and instanceid=" + instanceid + "", "DATAID");
			if ("是".equals(sftz)) {				
				senMsg(hashmap,ggid);
			}
			if (hfr != null && !"".equals(hfr)) {
				String[] hfrArr = hfr.split(",");
				List<HashMap> list = new ArrayList();
				Connection conn = null;
				PreparedStatement stmt = null;
				ResultSet rset = null;
			
				if(hfrArr!=null&&hfrArr.length>0){
					conn = DBUtil.open();
				}
				for (String string : hfrArr) {
					String hfrUserId = string.substring(0, string.indexOf("["));
					StringBuffer sql = new StringBuffer();
					sql.append("select a.userid as userid,a.extend1 as customerno,a.username as username,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel from orguser a left join bd_zqb_kh_base b on a.extend1=b.customerno where a.userid =?");
					try {
						stmt =conn.prepareStatement(sql.toString());
						stmt.setString(1, hfrUserId);
						rset = stmt.executeQuery();
						while (rset.next()) {
							HashMap map = new HashMap();
							String uname = rset.getString("username");
							String uid = rset.getString("userid");
							String customerno = rset.getString("customerno");
							String departmentname = rset.getString("departmentname");
							String orgroleid = rset.getString("orgroleid");
							String mobile = rset.getString("mobile");
							String zqdm = rset.getString("zqdm");
							String tel = rset.getString("tel");
							map.put("USERID", uid);
							map.put("CUSTOMERNO", customerno);
							map.put("XM", uname);
							map.put("GSMC", departmentname);
							map.put("GSDM", zqdm);
							map.put("SJH", mobile);
							list.add(map);
						}
					} catch (Exception e) {
						logger.error(e,e);
					}finally{
						
						DBUtil.close(conn, stmt, rset);
					}
				}
				for (HashMap map : list) {
					map.put("formid", config.get("hfqkbformid"));
					map.put("GGID", ggid);
					map.put("STATUS", "未回复");
					Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
					DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
				}
			}
			HashMap map = DemAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			String value=map.get("TZBT")==null?"":map.get("TZBT").toString();
			Long dataId=Long.parseLong(map.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "通知公告", "添加通知公告："+value);
		}
		HashMap returnMap = new HashMap();
		returnMap.put("HFR", hfr);
		returnMap.put("flag", saveFormData == true ? "success" : "error");
		returnMap.put("ggid", ggid);
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public String saveOne(String tzbt, String zchfsj, String tznr, String XGZL,
			String hfr, String sftz, String ggid2,String tzlx,String wjfile,String dctm,String extend5) {
		List<HashMap<String, Object>> customer = getCustomer();
		StringBuffer ssb = new StringBuffer();
		for (int i = 0; i < customer.size(); i++) {
			if (i == (customer.size() - 1)) {
				ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]");
			} else {
				ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]").append(",");
			}
		}
		hfr = ssb.toString();
		boolean saveFormData = false;
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("tzggbuuid");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String username = userModel.getUsername();
		String userid = userModel.getUserid();
		String ggid = "";
		if (ggid2 != null && !"".equals(ggid2)) {
			ggid = ggid2;
			HashMap formData = DemAPI.getInstance().getFormData(demUUID,Long.parseLong(ggid2));
			String[] split = XGZL.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			XGZL = sb.toString();
			formData.put("TZBT", tzbt);
			formData.put("ZCHFSJ", zchfsj);
			formData.put("TZNR", tznr);
			formData.put("XGZL", XGZL);
			formData.put("HFR", "");
			formData.put("SFTZ", sftz);
			formData.put("FSR", username);
			String format = sd.format(new Date());
			formData.put("FSSJ", format);
			formData.put("TZLX", tzlx);
			formData.put("WJFILE", wjfile);
			formData.put("DCTM", dctm);
			formData.put("EXTEND5", extend5);
			formData.put("FSZT", "未完成");
			formData.put("JSZT", "未回复");
			Map params = new HashMap();
			params.put(1, ggid2);
			Long instanceid = DBUTilNew.getLong("instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and dataid= ? ", params);
			saveFormData = DemAPI.getInstance().updateFormData(demUUID,instanceid, formData, Long.parseLong(ggid2), false);
			HashMap conditionMap = new HashMap();
			conditionMap.put("GGID", ggid2);
			List<HashMap> list2 = DemAPI.getInstance().getList(config.get("hfqkbuuid"), conditionMap, null);
			if (hfr != null && !"".equals(hfr)) {
				String[] hfrArr = hfr.split(",");
				boolean equals = false;
				List<HashMap> list = new ArrayList();			
				for (HashMap<String, Object> data : customer) {
					String repeatUserId = data.get("USERID").toString();//string.substring(0, string.indexOf("["));
					for (HashMap hashMap : list2) {
						equals = repeatUserId.equals(hashMap.get("USERID").toString()
								.trim());
						if (equals) {
							break;
						}
					}
					if (equals) {
						continue;
					}
					HashMap map = new HashMap();
					map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
					map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
					map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
					map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
					map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
					map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
					list.add(map);					
				}
				for (HashMap map : list) {
					map.put("formid", config.get("hfqkbformid"));
					map.put("GGID", ggid);
					map.put("STATUS", "未回复");
					Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
					DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
				}
			}
		} else {
			HashMap hashmap = new HashMap();
			String[] split = XGZL.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			XGZL = sb.toString();
			Long instanceid = DemAPI.getInstance().newInstance(demUUID,userid);
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", config.get("tzggbformid"));
			hashmap.put("TZBT", tzbt);
			hashmap.put("ZCHFSJ", zchfsj);
			hashmap.put("TZNR", tznr);
			hashmap.put("XGZL", XGZL);
			hashmap.put("HFR", "");
			hashmap.put("SFTZ", sftz);
			hashmap.put("FSR", username);
			String format = sd.format(new Date());
			hashmap.put("FSSJ", format);
			hashmap.put("TZLX", tzlx);
			hashmap.put("WJFILE", wjfile);
			hashmap.put("DCTM", dctm);
			hashmap.put("EXTEND5", extend5);
			hashmap.put("FSZT", "未完成");
			hashmap.put("JSZT", "未回复");
			hashmap.put("FSRID", userid);
			saveFormData = DemAPI.getInstance().saveFormData(demUUID,instanceid, hashmap, false);
			ggid = DBUtil.getString(
					"select DATAID from  SYS_ENGINE_FORM_BIND where formid="
							+ config.get("tzggbformid") + " and instanceid="
							+ instanceid + "", "DATAID");
			if (hfr != null && !"".equals(hfr)) {
				String[] hfrArr = hfr.split(",");
				List<HashMap> list = new ArrayList();
				for (HashMap<String, Object> data : customer) {
					HashMap map = new HashMap();
					map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
					map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
					map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
					map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
					map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
					map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
					list.add(map);
				}
				for (HashMap map : list) {
					map.put("formid", config.get("hfqkbformid"));
					map.put("GGID", ggid);
					map.put("STATUS", "未回复");
					Long instanceidTz = DemAPI.getInstance().newInstance(
							config.get("hfqkbuuid"),
							UserContextUtil.getInstance()
									.getCurrentUserContext()._userModel
									.getUserid());
					DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),
							instanceidTz, map, false);
				}
			}
		}
		HashMap returnMap = new HashMap();
		returnMap.put("HFR", hfr);
		returnMap.put("flag", saveFormData == true ? "success" : "error");
		returnMap.put("ggid", ggid);
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public String saveAll(String tzbt, String zchfsj, String tznr, String XGZL,
			String hfr, String sftz, String ggid2,String tzlx,String wjfile,String dctm,String extend5) {
		List<HashMap<String, Object>> customer = getAllCustomer();
		StringBuffer ssb = new StringBuffer();
		for (int i = 0; i < customer.size(); i++) {
			if (i == (customer.size() - 1)) {
				ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]");
			} else {
				ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]").append(",");
			}
		}
		hfr = ssb.toString();
		boolean saveFormData = false;
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("tzggbuuid");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String username = userModel.getUsername();
		String userid = userModel.getUserid();
		String ggid = "";
		if (ggid2 != null && !"".equals(ggid2)) {
			ggid = ggid2;
			HashMap formData = DemAPI.getInstance().getFormData(demUUID,Long.parseLong(ggid2));
			String[] split = XGZL.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			XGZL = sb.toString();
			formData.put("TZBT", tzbt);
			formData.put("ZCHFSJ", zchfsj);
			formData.put("TZNR", tznr);
			formData.put("XGZL", XGZL);
			formData.put("HFR", "");
			formData.put("SFTZ", sftz);
			formData.put("FSR", username);
			String format = sd.format(new Date());
			formData.put("FSSJ", format);
			formData.put("TZLX", tzlx);
			formData.put("WJFILE", wjfile);
			formData.put("DCTM", dctm);
			formData.put("EXTEND5", extend5);
			formData.put("FSZT", "未完成");
			formData.put("JSZT", "未回复");
			Map params = new HashMap();
			params.put(1, ggid2);
			Long instanceid = DBUTilNew.getLong("instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and dataid= ? ", params);
			saveFormData = DemAPI.getInstance().updateFormData(demUUID,instanceid, formData, Long.parseLong(ggid2), false);			
			HashMap conditionMap = new HashMap();
			conditionMap.put("GGID", ggid2);
			List<HashMap> list2 = DemAPI.getInstance().getList(config.get("hfqkbuuid"), conditionMap, null);
			if (hfr != null && !"".equals(hfr)) {
				String[] hfrArr = hfr.split(",");
				boolean equals = false;
				List<HashMap> list = new ArrayList();			
				for (HashMap<String, Object> data : customer) {
					String repeatUserId = data.get("USERID").toString();
					for (HashMap hashMap : list2) {
						equals = repeatUserId.equals(hashMap.get("USERID").toString().trim());
						if (equals) {
							break;
						}
					}
					if (equals) {
						continue;
					}
					HashMap map = new HashMap();
					map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
					map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
					map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
					map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
					map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
					map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
					list.add(map);
				}
				for (HashMap map : list) {
					map.put("formid", config.get("hfqkbformid"));
					map.put("GGID", ggid);
					map.put("STATUS", "未回复");
					Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
					DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
				}
			}
		} else {
			HashMap hashmap = new HashMap();
			String[] split = XGZL.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			XGZL = sb.toString();
			Long instanceid = DemAPI.getInstance().newInstance(demUUID,userid);
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", config.get("tzggbformid"));
			hashmap.put("TZBT", tzbt);
			hashmap.put("ZCHFSJ", zchfsj);
			hashmap.put("TZNR", tznr);
			hashmap.put("XGZL", XGZL);
			hashmap.put("HFR", "");
			hashmap.put("SFTZ", sftz);
			hashmap.put("FSR", username);
			String format = sd.format(new Date());
			hashmap.put("FSSJ", format);
			hashmap.put("TZLX", tzlx);
			hashmap.put("WJFILE", wjfile);
			hashmap.put("DCTM", dctm);
			hashmap.put("EXTEND5", extend5);
			hashmap.put("FSZT", "未完成");
			hashmap.put("JSZT", "未回复");
			hashmap.put("FSRID", userid);
			saveFormData = DemAPI.getInstance().saveFormData(demUUID,instanceid, hashmap, false);
			ggid = DBUtil.getString("select DATAID from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and instanceid=" + instanceid + "", "DATAID");			
			if (hfr != null && !"".equals(hfr)) {
				String[] hfrArr = hfr.split(",");
				List<HashMap> list = new ArrayList();			
				for (HashMap<String, Object> data : customer) {
					HashMap map = new HashMap();
					map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
					map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
					map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
					map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
					map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
					map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
					list.add(map);				
				}
				for (HashMap map : list) {
					map.put("formid", config.get("hfqkbformid"));
					map.put("GGID", ggid);
					map.put("STATUS", "未回复");
					Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
					DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
				}
			}
		}
		HashMap returnMap = new HashMap();
		returnMap.put("HFR", hfr);
		returnMap.put("flag", saveFormData == true ? "success" : "error");
		returnMap.put("ggid", ggid);
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public List<HashMap> getTZGGList(String tzlx ,String tzbt, String spzt, int pageNumber,
			int pageSize) {
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		boolean isSuperMan = this.getIsSuperMan();
		String owner = "";
		owner = uc._userModel.getUsername();
		if (isSuperMan||uc._userModel.getOrgroleid()==4l) {
			list = zqbAnnouncementDAO.getTzggList(tzlx,"", tzbt, spzt, pageNumber,
					pageSize);
		} else {
			list = zqbAnnouncementDAO.getTzggList(tzlx,owner, tzbt, spzt,
					pageNumber, pageSize);
		}
		return list;
	}
	
	//查询所有未挂牌企业
		public String saveWGP(String tzbt, String zchfsj, String tznr, String XGZL,
				String hfr, String sftz, String ggid2,String tzlx,String wjfile,String dctm,String extend5) {
			List<HashMap<String, Object>> customer = getWGPCustomer();
			StringBuffer ssb = new StringBuffer();
			for (int i = 0; i < customer.size(); i++) {
				if (i == (customer.size() - 1)) {
					ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]");
				} else {
					ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]").append(",");
				}
			}
			hfr = ssb.toString();
			boolean saveFormData = false;
			Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
			String demUUID = config.get("tzggbuuid");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			String username = userModel.getUsername();
			String userid = userModel.getUserid();
			String ggid = "";
			if (ggid2 != null && !"".equals(ggid2)) {
				ggid = ggid2;
				HashMap formData = DemAPI.getInstance().getFormData(demUUID,Long.parseLong(ggid2));
				String[] split = XGZL.split(",");
				StringBuffer sb = new StringBuffer();
				Set set = new TreeSet();
				for (int i = 0; i < split.length; i++) {
					set.add(split[i]);
				}
				split = (String[]) set.toArray(new String[0]);
				for (int i = 0; i < split.length; i++) {
					if (i == (split.length - 1)) {
						sb.append(split[i]);
					} else {
						sb.append(split[i]).append(",");
					}
				}
				XGZL = sb.toString();
				formData.put("TZBT", tzbt);
				formData.put("ZCHFSJ", zchfsj);
				formData.put("TZNR", tznr);
				formData.put("XGZL", XGZL);
				formData.put("HFR", "");
				formData.put("SFTZ", sftz);
				formData.put("FSR", username);
				String format = sd.format(new Date());
				formData.put("FSSJ", format);
				formData.put("TZLX", tzlx);
				formData.put("WJFILE", wjfile);
				formData.put("DCTM", dctm);
				formData.put("EXTEND5", extend5);
				formData.put("FSZT", "未完成");
				formData.put("JSZT", "未回复");
				Map params = new HashMap();
				params.put(1, ggid2);
				Long instanceid = DBUTilNew.getLong("instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and dataid= ? ",params);
				saveFormData = DemAPI.getInstance().updateFormData(demUUID,instanceid, formData, Long.parseLong(ggid2), false);
				HashMap conditionMap = new HashMap();
				conditionMap.put("GGID", ggid2);
				List<HashMap> list2 = DemAPI.getInstance().getList(config.get("hfqkbuuid"), conditionMap, null);
				if (hfr != null && !"".equals(hfr)) {
					String[] hfrArr = hfr.split(",");
					boolean equals = false;
					List<HashMap> list = new ArrayList();
					for (HashMap<String, Object> data : customer) {
						String repeatUserId = data.get("USERID").toString();//string.substring(0, string.indexOf("["));
						for (HashMap hashMap : list2) {
							equals = repeatUserId.equals(hashMap.get("USERID").toString().trim());
							if (equals) {
								break;
							}
						}
						if (equals) {
							continue;
						}
						HashMap map = new HashMap();
						map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
						map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
						map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
						map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
						map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
						map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
						list.add(map);
					}
					for (HashMap map : list) {
						map.put("formid", config.get("hfqkbformid"));
						map.put("GGID", ggid);
						map.put("STATUS", "未回复");
						Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
						DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
					}
				}
			} else {
				HashMap hashmap = new HashMap();
				String[] split = XGZL.split(",");
				StringBuffer sb = new StringBuffer();
				Set set = new TreeSet();
				for (int i = 0; i < split.length; i++) {
					set.add(split[i]);
				}
				split = (String[]) set.toArray(new String[0]);
				for (int i = 0; i < split.length; i++) {
					if (i == (split.length - 1)) {
						sb.append(split[i]);
					} else {
						sb.append(split[i]).append(",");
					}
				}
				XGZL = sb.toString();
				Long instanceid = DemAPI.getInstance().newInstance(demUUID,userid);
				hashmap.put("instanceId", instanceid);
				hashmap.put("formid", config.get("tzggbformid"));
				hashmap.put("TZBT", tzbt);
				hashmap.put("ZCHFSJ", zchfsj);
				hashmap.put("TZNR", tznr);
				hashmap.put("XGZL", XGZL);
				hashmap.put("HFR", "");
				hashmap.put("SFTZ", sftz);
				hashmap.put("FSR", username);
				String format = sd.format(new Date());
				hashmap.put("FSSJ", format);
				hashmap.put("TZLX", tzlx);
				hashmap.put("WJFILE", wjfile);
				hashmap.put("DCTM", dctm);
				hashmap.put("EXTEND5", extend5);
				hashmap.put("FSZT", "未完成");
				hashmap.put("JSZT", "未回复");
				hashmap.put("FSRID", userid);
				saveFormData = DemAPI.getInstance().saveFormData(demUUID,instanceid, hashmap, false);
				ggid = DBUtil.getString("select DATAID from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and instanceid=" + instanceid + "", "DATAID");

				if (hfr != null && !"".equals(hfr)) {
					String[] hfrArr = hfr.split(",");
					List<HashMap> list = new ArrayList();
					for (HashMap<String, Object> data : customer) {
						HashMap map = new HashMap();
						map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
						map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
						map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
						map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
						map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
						map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
						list.add(map);
					}
					for (HashMap map : list) {
						map.put("formid", config.get("hfqkbformid"));
						map.put("GGID", ggid);
						map.put("STATUS", "未回复");
						Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
						DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
					}
				}
			}
			HashMap returnMap = new HashMap();
			returnMap.put("HFR", hfr);
			returnMap.put("flag", saveFormData == true ? "success" : "error");
			returnMap.put("ggid", ggid);
			JSONArray json = JSONArray.fromObject(returnMap);
			StringBuffer jsonHtml = new StringBuffer();
			jsonHtml.append(json);
			return jsonHtml.toString();
		}
		
		//查询已选择企业
				public String saveYXZ(String tzbt, String zchfsj, String tznr, String XGZL,
						String hfr, String sftz, String ggid2,String uid,String tzlx,String wjfile,String dctm,String extend5) {
					List<HashMap<String, Object>> customer = getYXZCustomer(uid);
					StringBuffer ssb = new StringBuffer();
					for (int i = 0; i < customer.size(); i++) {
						if (i == (customer.size() - 1)) {
							ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]");
						} else {
							ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]").append(",");
						}
					}
					hfr = ssb.toString();
					boolean saveFormData = false;
					Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
					String demUUID = config.get("tzggbuuid");
					SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
					String username = userModel.getUsername();
					String userid = userModel.getUserid();
					String ggid = "";
					if (ggid2 != null && !"".equals(ggid2)) {
						ggid = ggid2;
						HashMap formData = DemAPI.getInstance().getFormData(demUUID,Long.parseLong(ggid2));
						String[] split = XGZL.split(",");
						StringBuffer sb = new StringBuffer();
						Set set = new TreeSet();
						for (int i = 0; i < split.length; i++) {
							set.add(split[i]);
						}
						split = (String[]) set.toArray(new String[0]);
						for (int i = 0; i < split.length; i++) {
							if (i == (split.length - 1)) {
								sb.append(split[i]);
							} else {
								sb.append(split[i]).append(",");
							}
						}
						XGZL = sb.toString();
						formData.put("TZBT", tzbt);
						formData.put("ZCHFSJ", zchfsj);
						formData.put("TZNR", tznr);
						formData.put("XGZL", XGZL);
						formData.put("HFR", "");
						formData.put("SFTZ", sftz);
						formData.put("FSR", username);
						String format = sd.format(new Date());
						formData.put("FSSJ", format);
						formData.put("TZLX", tzlx);
						formData.put("WJFILE", wjfile);
						formData.put("DCTM", dctm);
						formData.put("EXTEND5", extend5);
						formData.put("FSZT", "未完成");
						formData.put("JSZT", "未回复");
						Map params = new HashMap();
						params.put(1, ggid2);
						Long instanceid = DBUTilNew.getLong( "instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and dataid= ? ",params);
						saveFormData = DemAPI.getInstance().updateFormData(demUUID,instanceid, formData, Long.parseLong(ggid2), false);
						HashMap conditionMap = new HashMap();
						conditionMap.put("GGID", ggid2);
						List<HashMap> list2 = DemAPI.getInstance().getList(config.get("hfqkbuuid"), conditionMap, null);
						if (hfr != null && !"".equals(hfr)) {
							String[] hfrArr = hfr.split(",");
							boolean equals = false;
							List<HashMap> list = new ArrayList();
							for (HashMap<String, Object> data : customer) {
								String repeatUserId = data.get("USERID").toString();//string.substring(0, string.indexOf("["));
								for (HashMap hashMap : list2) {
									equals = repeatUserId.equals(hashMap.get("USERID").toString().trim());
									if (equals) {
										break;
									}
								}
								if (equals) {
									continue;
								}
								HashMap map = new HashMap();
								map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
								map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
								map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
								map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
								map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
								map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
								list.add(map);
							}
							for (HashMap map : list) {
								map.put("formid", config.get("hfqkbformid"));
								map.put("GGID", ggid);
								map.put("STATUS", "未回复");
								Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
								DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
							}
						}
					} else {
						HashMap hashmap = new HashMap();
						String[] split = XGZL.split(",");
						StringBuffer sb = new StringBuffer();
						Set set = new TreeSet();
						for (int i = 0; i < split.length; i++) {
							set.add(split[i]);
						}
						split = (String[]) set.toArray(new String[0]);
						for (int i = 0; i < split.length; i++) {
							if (i == (split.length - 1)) {
								sb.append(split[i]);
							} else {
								sb.append(split[i]).append(",");
							}
						}
						XGZL = sb.toString();
						Long instanceid = DemAPI.getInstance().newInstance(demUUID,userid);
						hashmap.put("instanceId", instanceid);
						hashmap.put("formid", config.get("tzggbformid"));
						hashmap.put("TZBT", tzbt);
						hashmap.put("ZCHFSJ", zchfsj);
						hashmap.put("TZNR", tznr);
						hashmap.put("XGZL", XGZL);
						hashmap.put("HFR", "");
						hashmap.put("SFTZ", sftz);
						hashmap.put("FSR", username);
						String format = sd.format(new Date());
						hashmap.put("FSSJ", format);
						hashmap.put("TZLX", tzlx);
						hashmap.put("WJFILE", wjfile);
						hashmap.put("DCTM", dctm);
						hashmap.put("EXTEND5", extend5);
						hashmap.put("FSZT", "未完成");
						hashmap.put("JSZT", "未回复");
						hashmap.put("FSRID", userid);
						saveFormData = DemAPI.getInstance().saveFormData(demUUID,instanceid, hashmap, false);
						ggid = DBUtil.getString("select DATAID from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and instanceid=" + instanceid + "", "DATAID");

						if (hfr != null && !"".equals(hfr)) {
							String[] hfrArr = hfr.split(",");
							List<HashMap> list = new ArrayList();
							for (HashMap<String, Object> data : customer) {
								HashMap map = new HashMap();
								map.put("USERID", data.get("USERID")==null?"":data.get("USERID").toString());
								map.put("CUSTOMERNO", data.get("CUSTOMERNO")==null?"":data.get("CUSTOMERNO").toString());
								map.put("XM", data.get("USERNAME")==null?"":data.get("USERNAME").toString());
								map.put("GSMC", data.get("GSMC")==null?"":data.get("GSMC").toString());
								map.put("GSDM", data.get("GSDM")==null?"":data.get("GSDM").toString());
								map.put("SJH", data.get("MOBILE")==null?"":data.get("MOBILE").toString());
								list.add(map);
							}
							for (HashMap map : list) {
								map.put("formid", config.get("hfqkbformid"));
								map.put("GGID", ggid);
								map.put("STATUS", "未回复");
								Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
								DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
							}
						}
					}
					HashMap returnMap = new HashMap();
					returnMap.put("HFR", hfr);
					returnMap.put("flag", saveFormData == true ? "success" : "error");
					returnMap.put("ggid", ggid);
					JSONArray json = JSONArray.fromObject(returnMap);
					StringBuffer jsonHtml = new StringBuffer();
					jsonHtml.append(json);
					return jsonHtml.toString();
				}
	
	public int getTZGGListSize(String tzlx,String tzbt, String spzt) {
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		boolean isSuperMan = this.getIsSuperMan();
		String owner = "";
		owner = uc._userModel.getUsername();
		if (isSuperMan||uc._userModel.getOrgroleid()==4l) {
			return zqbAnnouncementDAO.getTzggListSize(tzlx,"", tzbt, spzt);
		} else {
			return zqbAnnouncementDAO.getTzggListSize(tzlx,owner, tzbt, spzt);
		}
	}
	
	public List<HashMap> getGZSCList(String tzbt, String spzt, int pageNumber,
			int pageSize) {
		List<HashMap> list = new ArrayList<HashMap>();
		list = zqbAnnouncementDAO.getGZSCList(tzbt, spzt, pageNumber,pageSize);
		return list;
	}
	
	public List<HashMap> getGZSCHFList(String id,String fkzt) {
		List<HashMap> list = new ArrayList<HashMap>();
		list = zqbAnnouncementDAO.getGZSCHFList(id,fkzt);
		return list;
	}
	/**
	 * 督导签约模板下载
	 */
	public void cwbbmb(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		filepath=filepath+"\\iwork_file\\主要财务数据.docx";
		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("主要财务数据.docx"));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			
		}
}
	public int getGZSCListSize(String tzbt, String spzt) {
		List<HashMap> list = new ArrayList<HashMap>();
		return zqbAnnouncementDAO.getGZSCListSize(tzbt, spzt);
	}
	
	public String fspGzscSendddMail(String instanceid) {
		String title = "工作审查提醒";
		String content = "";
		String info = "";
		String gzsc = "";
		Long id = Long.parseLong(instanceid);
		HashMap tzbt = DemAPI.getInstance().getFromData(id,EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if (tzbt != null && tzbt.get("TZBT") != null) {
			gzsc = tzbt.get("TZBT").toString();
			// 获取反馈人列表
			List<HashMap> list = DemAPI.getInstance().getFromSubData(
					Long.parseLong(instanceid), "SUBFORM_GZSCSXFKRBBD");
			if (list != null && list.size() > 0) {
				for (HashMap map : list) {
					Object userid = map.get("USERID");
					if (userid != null) {
						UserContext uc = UserContextUtil.getInstance().getUserContext(userid.toString());
						content = gzsc ;
						String mobile = uc._userModel.getMobile();
						String email = uc._userModel.getEmail();
						if (mobile != null && !mobile.equals("")) {
							MessageAPI.getInstance().sendSMS(uc, mobile,content);
						}
						if (email != null && !email.equals("")) {
							boolean flag = MessageAPI.getInstance().sendSysMail("", email, title, content);
							if (!flag) {
								info = "发邮件存在问题";
							}
						}
					}
				}
			} else {
				info = "该记录没有事项反馈人员！";
			}
		} else {
			info = "未获取工作审查标题！";
		}

		return info;
	}
	public List<HashMap> getsygdt(){
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		boolean isSuperMan = this.getIsSuperMan();
		String owner = "";
		//owner = userModel.getUserid() + "[" + userModel.getUsername() + "]";
		String username = userModel.getUsername();
		String userid = userModel.getUserid();
		return zqbAnnouncementDAO.getsygdt(userid,username);
	}
	public List<HashMap> getTZGGReceptionList(String tzbt, String spzt,int pageNumber, int pageSize) {
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		boolean isSuperMan = this.getIsSuperMan();
		String owner = "";
		//owner = userModel.getUserid() + "[" + userModel.getUsername() + "]";
		String username = userModel.getUsername();
		String userid = userModel.getUserid();
		return zqbAnnouncementDAO.getTZGGReceptionList(userid,username,owner, tzbt, spzt, pageNumber, pageSize);
	}

	public int getTZGGReceptionListSize(String tzbt, String spzt) {
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String owner = "";
		owner = userModel.getUserid() + "[" + userModel.getUsername() + "]";
		String username = userModel.getUsername();
		String userid = userModel.getUserid();
		return zqbAnnouncementDAO.getTzggReceptionListSize(userid,username,owner, tzbt, spzt);
	}

	public List<HashMap> getNoticeList(String roleid, String status, String ggid,
			int pageNumber, int pageSize) {
		List<HashMap> list = new ArrayList<HashMap>();
		list = zqbAnnouncementDAO.getNoticeList(roleid, status, ggid, pageNumber,
				pageSize);
		return list;
	}

	public int getNoticeListSize(String roleid, String status, String ggid) {
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = "";
		owner = uc._userModel.getUsername();
		return zqbAnnouncementDAO.getNoticeListSize(roleid, status, ggid);
	}

	public int getNoticeListSize(String roleid, String ggid) {
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = "";
		owner = uc._userModel.getUsername();
		return zqbAnnouncementDAO.getNoticeListSize(roleid, ggid);
	}

	public List<HashMap> loadAnnouncementList(String ggid) {
		List<HashMap> list = new ArrayList<HashMap>();
		list = zqbAnnouncementDAO.loadAnnouncementList(ggid);
		return list;
	}

	public List<HashMap> getannouncementNoticeList(String ggid) {
		List<HashMap> list = new ArrayList<HashMap>();
		list = zqbAnnouncementDAO.loadAnnouncementList(ggid);
		return list;
	}

	public List<HashMap> getNoticeList(String ggid) {
		List<HashMap> list = new ArrayList<HashMap>();
		list = zqbAnnouncementDAO.getNoticeList(ggid);
		return list;
	}
	public List<HashMap> getNoticeList1(Long ggid) {
		List<HashMap> list = new ArrayList<HashMap>();
		list = zqbAnnouncementDAO.getNoticeList1(ggid);
		return list;
	}
	public String  gettzlx(Long ggid) {
		String tzlx= DBUtil.getString("select TZLX from bd_xp_tzggb where id="+ggid+"","TZLX");
		return tzlx;
	}

	public List<HashMap> getNoticeList(long ggid) {
		List<HashMap> list = new ArrayList<HashMap>();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("hfxxbuuid");
		String dataid = DBUtil.getString("select DATAID from  SYS_ENGINE_FORM_BIND where formid=" + config.get("hfxxbformid") + " and instanceid=" + ggid + "", "DATAID");
		HashMap formData = DemAPI.getInstance().getFormData(demUUID,Long.parseLong(dataid));
		StringBuffer count = new StringBuffer();
		if(formData!=null&&!formData.equals("")){
			if (formData.get("XGZL") != null && !"".equals(formData.get("XGZL").toString())) {
				String string = formData.get("XGZL").toString();
				count.append("<table>");
				List<FileUpload> sublist = FileUploadAPI.getInstance()
						.getFileUploads(string);
				int n=1;
				for (FileUpload fileUpload : sublist) {
					String xgzl = fileUpload.getFileSrcName();
					String uuid = fileUpload.getFileId();
					count.append("<tr ><td style=\"font-size: 12px;\">"+n+"、<a href=\"uploadifyDownload.action?fileUUID="
							+ uuid
							+ "\" style=\"color: #0000ff;border-bottom: 1px dotted #efefef; color: #0000ff; font-size: 12px; font-weight: 100; line-height: 15px; padding-left: 3px; padding-top: 5px; text-align: left; vertical-align: middle; word-break: break-all; word-wrap: break-word;\">"
							+ xgzl + "</a></td></tr>");
					n++;
				}
				count.append("</table>");
			}
			formData.put("XGZL", count.toString());
			list.add(formData);
		}
		return list;
	}

	public boolean replySave(String hfcontent, String xgzl, String gonggaoID,Long hfqkid,String sfcx) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("hfxxbuuid");
		if(hfqkid==0){
			if(gonggaoID==null ||"".equals(gonggaoID)){
				return false;
			}
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
			String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
			String owner = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid() + "[" + UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername() + "]";
			HashMap hashmap = new HashMap();
			Long instanceid = DemAPI.getInstance().newInstance(demUUID,UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid());
		/*	String[] split = xgzl.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			xgzl = sb.toString();*/
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", config.get("hfxxbformid"));
			hashmap.put("CONTENT", hfcontent);
			hashmap.put("XGZL", xgzl);
			hashmap.put("HFR", username);
			hashmap.put("HFRID", userid);
			hashmap.put("GGID", gonggaoID);
			hashmap.put("EXTEND1", sfcx);
			boolean saveFormData = DemAPI.getInstance().saveFormData(demUUID, instanceid, hashmap, false);
			String hfid = DBUtil.getString("select DATAID from  SYS_ENGINE_FORM_BIND where formid=" + config.get("hfxxbformid") + " and instanceid=" + instanceid + "", "DATAID");
			String sql = "update Bd_Xp_Hfqkb set status='已回复',hfid=" + hfid + ",hfsj=sysdate where (userid='"+userid+"') and ggid= ? ";
			Map params = new HashMap();
			
			params.put(1, gonggaoID);			
			int updateInt = DBUTilNew.update(sql,params);
			return saveFormData;
		}else{
			Long instaceId = DemAPI.getInstance().getInstaceId("BD_XP_HFXXB", hfqkid);
			HashMap fromData = DemAPI.getInstance().getFromData(instaceId);
			/*String[] split = xgzl.split(",");
			StringBuffer sb = new StringBuffer();
			Set set = new TreeSet();
			for (int i = 0; i < split.length; i++) {
				set.add(split[i]);
			}
			split = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < split.length; i++) {
				if (i == (split.length - 1)) {
					sb.append(split[i]);
				} else {
					sb.append(split[i]).append(",");
				}
			}
			xgzl = sb.toString();*/
			fromData.put("CONTENT", hfcontent);
			fromData.put("XGZL", xgzl);
			fromData.put("EXTEND1", sfcx);
			boolean updateFormData = DemAPI.getInstance().updateFormData(demUUID, instaceId, fromData, hfqkid, false);
			return updateFormData;
		}
	}
	/**
	 * 通知公告查看情况
	 */
	public void tzggCkqk(Long ggid){
		if(ggid!=null && !ggid.equals("")){
			String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
			Map params = new HashMap();
			params.put(1, ggid);
			String sql = "update Bd_Xp_Hfqkb set sfck=1 where (userid='"+userid+"') and ggid= ? ";
			DBUTilNew.update(sql,params);
		}
	}
	public void tzggDfyy(String ggid,String userId){
		String userid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
		Map par=new HashMap();
		par.put(1, userid);
		String username=DBUTilNew.getDataStr("username", "select username from orguser where userid = ? ", par);
		if(ggid!=null && !ggid.equals("")){
			Map params = new HashMap();
			params.put(1, userId);
			params.put(2, ggid);
			String sql = "update Bd_Xp_Hfqkb set extend2='"+username+"' where (userid= ? ) and ggid= ? ";
			DBUTilNew.update(sql,params);
		}
	}
	public int getNoticeWHFListSize(String roleid, String ggid) {
		return zqbAnnouncementDAO.getNoticeWHFListSize(roleid, ggid);
	}

	public boolean deleteReply(String ggid,Long tzggid,String hfr) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("tzggbuuid");
		String tzggformid = config.get("tzggbformid");
		Map params = new HashMap();
		Long tzgginstanceid = DBUtil.getLong(
				"select instanceid from  SYS_ENGINE_FORM_BIND where formid="
						+ tzggformid + " and dataid=" + tzggid,
				"instanceid");
		HashMap fromData =DemAPI.getInstance().getFormData(demUUID, tzggid);
		fromData.put("HFR", hfr);
		DemAPI.getInstance().updateFormData(demUUID, tzgginstanceid, fromData, tzggid, false);
		params = new HashMap();
		params.put(1, ggid);
		Long instanceid = DBUTilNew.getLong(
				"instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid="
						+ config.get("hfqkbformid") + " and dataid= ? " ,
				params);
		return DemAPI.getInstance().removeFormData(instanceid);
	}
	
	public boolean deleteReplyAll(String hfr) {
		Map params = new HashMap();		
		params.put(1, hfr);
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM bd_xp_hfqkb where id in(  ");
		String[] strIDs = hfr.split(",");
		int n=1;
		for (int i = 0; i < strIDs.length; i++) {
			if(i==(strIDs.length-1)){
				sql.append("?");
			}else{
				sql.append("?,");
			}
			params.put(n,strIDs[i].replaceAll("'", ""));
			n++;
		}
		sql.append(" ) ");
		DBUTilNew.update(sql.toString(),params);	
		return true;
	}
	public boolean updateMemo(String id,String memo) {
		String sql = "UPDATE BD_XP_HFQKB SET EXTEND1=? WHERE ID=?";
		Map params = new HashMap();
		params.put(1, memo);
		params.put(2, id);
		int num = com.iwork.commons.util.DBUtil.update(sql, params);
		return true;
	}

	public boolean deleteSendNotice(String ggid) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		Map params = new HashMap();
		params.put(1, ggid);
		Long instanceid = DBUTilNew.getLong(
				"instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid="
						+ config.get("tzggbformid") + " and dataid= ? ",
				params);
		HashMap map = DemAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value=map.get("TZBT")==null?"":map.get("TZBT").toString();
		Long dataId=Long.parseLong(map.get("ID").toString());
		boolean flag= DemAPI.getInstance().removeFormData(instanceid);
		if(flag){
			LogUtil.getInstance().addLog(dataId, "通知公告", "删除通知公告："+value);
		}
		return flag;
	}

	public boolean updateSendNotice(String ggid) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("tzggbuuid");
		HashMap formData = DemAPI.getInstance().getFormData(demUUID,
				Long.parseLong(ggid));
		formData.put("FSZT", "完成");
		Map params = new HashMap();
		params.put(1, ggid);
		Long instanceid = DBUTilNew.getLong("instanceid","select instanceid from  SYS_ENGINE_FORM_BIND where formid="+ config.get("tzggbformid") + " and dataid= ? ",params);
		return DemAPI.getInstance().updateFormData(demUUID, instanceid,
				formData, Long.parseLong(ggid), false);
	}

	public String getfilehtml(String ggid) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		StringBuffer html = new StringBuffer();
		String demUUID = config.get("tzggbuuid");
		HashMap formData = DemAPI.getInstance().getFormData(demUUID,
				Long.parseLong(ggid));
		if (formData != null && formData.get("XGZL") != null) {
			String fj = formData.get("XGZL").toString();
			if (!fj.equals("")) {
				List<FileUpload> sublist = FileUploadAPI.getInstance()
						.getFileUploads(fj);
				html.append("<table class=\"filelist\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">");
				int count = 0;
				for (FileUpload upload : sublist) {
					count++;
					html.append("<tr>");
					html.append("<td>").append(count).append("</td>");
					String icon = "";
					String suffix = FileUtil.getFileExt(upload
							.getFileSaveName());
					icon = WebUIUtil.getLinkIcon(suffix);
					html.append(
							"<td><a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="
									+ upload.getFileId() + "\"><span>")
							.append(icon).append("</span>")
							.append(upload.getFileSrcName())
							.append("</a></td>");
					html.append("<td>").append(upload.getUploadTime())
							.append("</td>");
					if (upload.getMemo() == null)
						upload.setMemo("");
					html.append("<td>").append(upload.getMemo()).append("</td>");
					html.append("<td>");
					html.append("</td>");
					html.append("</tr>");
				}
				if (sublist.size() == 0) {
					html.append("<tr>");
					html.append("<td>").append("该通知没有资料文件。").append("</td>");
					html.append("</td>");
					html.append("</tr>");
				}
				html.append("</table>");
			}
		} else {
			html.append("<table class=\"filelist\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">");
			html.append("<tr>");
			html.append("<td>").append("该通知没有资料文件。").append("</td>");
			html.append("</td>");
			html.append("</tr>");
			html.append("</table>");
		}
		return html.toString();
	}

	public int getTZGGWDSize() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String owner = "";
		owner = uc._userModel.getUsername();
		return zqbAnnouncementDAO.getTZGGWDSize(owner);
	}

	public boolean isCustomerJc(String zqjc) {
		Map params = new HashMap();
		params.put(1, "%"+zqjc+"%");
		String khbh = DBUTilNew.getDataStr("CUSTOMERNO","select CUSTOMERNO from bd_zqb_kh_base where zqjc like ? ", params);
		if (!"".equals(khbh)) {
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("KHBH", khbh);
			List<HashMap> list = DemAPI.getInstance().getList(
					"84ff70949eac4051806dc02cf4837bd9", conditionMap, null);
			String userFullName = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			for (HashMap map : list) {
				if ((map.get("KHFZR") != null
						&& !map.get("KHFZR").toString().equals("") && map
						.get("KHFZR").toString().equals(userFullName))
						|| (map.get("GGFBR") != null
								&& !map.get("GGFBR").toString().equals("") && map
								.get("GGFBR").toString().equals(userFullName))
						|| (map.get("ZZCXDD") != null
								&& !map.get("ZZCXDD").toString().equals("") && map
								.get("ZZCXDD").toString().equals(userFullName))
						|| (map.get("FHSPR") != null
								&& !map.get("FHSPR").toString().equals("") && map
								.get("FHSPR").toString().equals(userFullName))
						|| (map.get("ZZSPR") != null
								&& !map.get("ZZSPR").toString().equals("") && map
								.get("ZZSPR").toString().equals(userFullName))
						|| (map.get("QYNBRYSH") != null
								&& !map.get("QYNBRYSH").toString().equals("") && map
								.get("QYNBRYSH").toString().equals(userFullName))
						|| (map.get("GGFBR") != null
								&& !map.get("GGFBR").toString().equals("") && map
								.get("GGFBR").toString().equals(userFullName))) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isCustomerDm(String zqdm) {
		Map params = new HashMap();
		params.put(1, "%"+zqdm+"%");
		String khbh = DBUTilNew.getDataStr("CUSTOMERNO","select CUSTOMERNO from bd_zqb_kh_base where zqdm like ? ", params);
		if (!"".equals(khbh)) {
			HashMap<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("KHBH", khbh);
			List<HashMap> list = DemAPI.getInstance().getList(
					"84ff70949eac4051806dc02cf4837bd9", conditionMap, null);
			String userFullName = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			for (HashMap map : list) {
				if ((map.get("KHFZR") != null
						&& !map.get("KHFZR").toString().equals("") && map
						.get("KHFZR").toString().equals(userFullName))
						|| (map.get("GGFBR") != null
								&& !map.get("GGFBR").toString().equals("") && map
								.get("GGFBR").toString().equals(userFullName))
						|| (map.get("ZZCXDD") != null
								&& !map.get("ZZCXDD").toString().equals("") && map
								.get("ZZCXDD").toString().equals(userFullName))
						|| (map.get("FHSPR") != null
								&& !map.get("FHSPR").toString().equals("") && map
								.get("FHSPR").toString().equals(userFullName))
						|| (map.get("ZZSPR") != null
								&& !map.get("ZZSPR").toString().equals("") && map
								.get("ZZSPR").toString().equals(userFullName))
						|| (map.get("QYNBRYSH") != null
								&& !map.get("QYNBRYSH").toString().equals("") && map
								.get("QYNBRYSH").toString().equals(userFullName))		
						|| (map.get("GGFBR") != null
								&& !map.get("GGFBR").toString().equals("") && map
								.get("GGFBR").toString().equals(userFullName))) {
					return true;
				}
			}
		}

		return false;
	}

	public List<HashMap<String, Object>> getCustomer() {
		List list = new ArrayList<HashMap<String, Object>>();
		// 场外经理看到所有
		boolean isSuperMan = this.getIsSuperMan();
		if (isSuperMan) {// 场外经理看到所有
			// 查询数据库
			// String userid = UserContextUtil.getInstance().getCurrentUserId();
			String gpdm = "";
			String gpdm1 = "";
			StringBuffer sql = new StringBuffer(
					"select  z.userid,z.username,d.customerno,d.departmentname,d.orgroleid,d.mobile,d.zqdm,d.tel from (select a.id,khbh,khmc,khfzr,zzcxdd,fhspr,zzspr,ggfbr,c.instanceid,CWSCBFZR2,CWSCBFZR3,f.zqdm from BD_MDM_KHQXGLB a,");
			sql.append("SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f where 1=1 ");
			sql.append(" and formid = '114'        and metadataid = '130'    and a.id = c.dataid  and c.instanceid is not null  and a.khbh=f.customerno and f.ygp='已挂牌' and f.status='有效' and (f.cxddbg<>'转出' or f.cxddbg is null) ");
			sql.append(" order by id desc) b inner join orguser z on b.khbh=z.extend1");
			sql.append(" left join");
			sql.append(" (select a.userid as userid,a.extend1 as customerno,a.username as username,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel from orguser a left join bd_zqb_kh_base b on a.extend1=b.customerno) d");
			sql.append(" on z.userid=d.userid  where  z.userstate=0  and sysdate<z.enddate");
			Connection conn=null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				stmt =conn.prepareStatement(sql.toString());
				rs = stmt.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					String userid = rs.getString(1);
					String username = rs.getString(2);
					String customerno = rs.getString(3);
					String departmentname = rs.getString(4);
					String orgroleid = rs.getString(5);
					String mobile = rs.getString(6);
					String zqdm = rs.getString(7);
					String tel = rs.getString(8);
					map.put("USERID", userid);
					map.put("USERNAME", username);
					map.put("CUSTOMERNO", customerno);
					map.put("GSMC", departmentname);
					map.put("ORGROLEID", orgroleid);
					map.put("MOBILE", mobile);
					map.put("GSDM", zqdm);
					map.put("TEL", tel);
					list.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rs);
			}
		} else {// 其他角色只看到自己负责的
			// 查询数据库
			// String userid = UserContextUtil.getInstance().getCurrentUserId();
			String userfullname = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			String gpdm = "";
			String gpdm1 = "";
			StringBuffer sql = new StringBuffer(
					"select  z.userid,z.username,d.customerno,d.departmentname,d.orgroleid,d.mobile,d.zqdm,d.tel from (select a.id,khbh,khmc,khfzr,zzcxdd,fhspr,zzspr,ggfbr,c.instanceid,CWSCBFZR2,CWSCBFZR3,f.zqdm from BD_MDM_KHQXGLB a,");
			sql.append("SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f where 1=1 ");
			sql.append(" and formid = '114'        and metadataid = '130'    and a.id = c.dataid  and c.instanceid is not null  and a.khbh=f.customerno ");
			sql.append(" and (a.khfzr=? or a.fhspr=? or a.qynbrysh=?) and f.ygp='已挂牌' and f.status='有效' and (f.cxddbg<>'转出' or f.cxddbg is null) )  b inner join orguser z on b.khbh=z.extend1");
			sql.append(" left join");
			sql.append(" (select a.userid as userid,a.extend1 as customerno,a.username as username,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel from orguser a left join bd_zqb_kh_base b on a.extend1=b.customerno) d");
			sql.append(" on z.userid=d.userid");
			Connection conn=null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				stmt =conn.prepareStatement(sql.toString());
				stmt.setString(1, userfullname);
				stmt.setString(2, userfullname);
				stmt.setString(3, userfullname);
				rs = stmt.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					String userid = rs.getString(1);
					String username = rs.getString(2);
					String customerno = rs.getString(3);
					String departmentname = rs.getString(4);
					String orgroleid = rs.getString(5);
					String mobile = rs.getString(6);
					String zqdm = rs.getString(7);
					String tel = rs.getString(8);
					map.put("USERID", userid);
					map.put("USERNAME", username);
					map.put("CUSTOMERNO", customerno);
					map.put("GSMC", departmentname);
					map.put("ORGROLEID", orgroleid);
					map.put("MOBILE", mobile);
					map.put("GSDM", zqdm);
					map.put("TEL", tel);
					list.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rs);
			}

		}
		return list;
	}

	public List<HashMap<String, Object>> getAllCustomer() {
		List list = new ArrayList<HashMap<String, Object>>();		
		String gpdm = "";
		String gpdm1 = "";
		StringBuffer sql = new StringBuffer("select z.userid,z.username,b.customerno,z.departmentname,z.orgroleid,z.mobile,b.zqdm,z.mobile tel");
				sql.append(" from orguser z inner join (select max(id) id,extend1 from orguser where orgroleid=3 and extend1 is not null and USERSTATE=0  and sysdate<enddate group by extend1) a");
				sql.append(" on z.id = a.id inner join (select zqdm,customerno from bd_zqb_kh_base where zqdm is not null and ygp<>'摘牌' and status='有效') b on a.extend1 = b.customerno where z.userstate=0  and sysdate<z.enddate");
		// 查询数据库
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String userid = rs.getString(1);
				String username = rs.getString(2);
				String customerno = rs.getString(3);
				String departmentname = rs.getString(4);
				String orgroleid = rs.getString(5);
				String mobile = rs.getString(6);
				String zqdm = rs.getString(7);
				String tel = rs.getString(8);
				map.put("USERID", userid);
				map.put("USERNAME", username);
				map.put("CUSTOMERNO", customerno);
				map.put("GSMC", departmentname);
				map.put("ORGROLEID", orgroleid);
				map.put("MOBILE", mobile);
				map.put("GSDM", zqdm);
				map.put("TEL", tel);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}
	//查找所有未挂牌企业
	public List<HashMap<String, Object>> getWGPCustomer() {
		List list = new ArrayList<HashMap<String, Object>>();
	
		StringBuffer sql = new StringBuffer("select a.userid as userid,a.username as username,a.extend1 as customerno,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel ,b.ygp as ygp"); 
                     sql.append(" from orguser a left join bd_zqb_kh_base b on a.extend1=b.customerno where b.ygp='未挂牌' and  a.userstate=0  and sysdate<a.enddate");
		// 查询数据库
         Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String userid = rs.getString(1);
				String username = rs.getString(2);
				String customerno = rs.getString(3);
				String departmentname = rs.getString(4);
				String orgroleid = rs.getString(5);
				String mobile = rs.getString(6);
				String zqdm = rs.getString(7);
				String tel = rs.getString(8);
				map.put("USERID", userid);
				map.put("USERNAME", username);
				map.put("CUSTOMERNO", customerno);
				map.put("GSMC", departmentname);
				map.put("ORGROLEID", orgroleid);
				map.put("MOBILE", mobile);
				map.put("GSDM", zqdm);
				map.put("TEL", tel);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}
	//查找选中的企业
		public List<HashMap<String, Object>> getYXZCustomer(String uid) {
			List list = new ArrayList<HashMap<String, Object>>();
			String gpdm = "";
			String gpdm1 = "";
			StringBuffer sql = new StringBuffer("select a.userid as userid,a.username as username,a.extend1 as customerno,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel ,b.ygp as ygp"); 
	                     sql.append(" from orguser a left join bd_zqb_kh_base b on a.extend1=b.customerno where a.userid in (");
	                     String[] uids = uid.split(",");
	                     for (int i = 0; i < uids.length; i++) {
							if(i==(uids.length-1)){
								sql.append("?");
							}else{
								sql.append("?,");
							}
						}
	                     sql.append(")");
			// 查询数据库
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				ps =conn.prepareStatement(sql.toString());
				for (int i = 0; i < uids.length; i++) {
					ps.setString(i+1, uids[i].replaceAll("'", ""));
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					String userid = rs.getString(1);
					String username = rs.getString(2);
					String customerno = rs.getString(3);
					String departmentname = rs.getString(4);
					String orgroleid = rs.getString(5);
					String mobile = rs.getString(6);
					String zqdm = rs.getString(7);
					String tel = rs.getString(8);
					map.put("USERID", userid);
					map.put("USERNAME", username);
					map.put("CUSTOMERNO", customerno);
					map.put("GSMC", departmentname);
					map.put("ORGROLEID", orgroleid);
					map.put("MOBILE", mobile);
					map.put("GSDM", zqdm);
					map.put("TEL", tel);
					list.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, ps, rs);
			}
			return list;
		}

	public List<HashMap> getNmsxList(String khmc, String khbh,
			String nmxxStart, String nmxxEnd, String nmxx, int pageNumber,
			int pageSize) {
		return zqbAnnouncementDAO.getNmsxList(khmc, khbh, nmxxStart, nmxxEnd,
				nmxx, pageNumber, pageSize);
	}

	public int getNmsxListSize(String khmc, String khbh, String nmxxStart, String nmxxEnd, String nmxx) {
		return zqbAnnouncementDAO.getNmsxListSize(khmc, khbh,nmxxStart,nmxxEnd,nmxx);
	}

	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter)==null?"0":config.get(parameter));
		}
		return result;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter)==null?"":config.get(parameter);
		}
		return result;
	}

	public String deleteNmxx(Long instanceid) {
		String info="";
		boolean flag=false;
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		flag=DemAPI.getInstance().removeFormData(instanceid);
		if(flag){
			info="success";
		}else{
			info="删除失败！";
		}
		String value = fromData.get("NMSX").toString();
		Long dataId=Long.parseLong(fromData.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "内幕知情人", "删除内幕知情人信息："+value);
		return info;
	}
	
	public String fileUpload(Long instanceId){
		HashMap<String, Object> hashMap = DemAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value = hashMap.get("NMXGZL").toString();
		String[] arr = value.split(",");
		Date data=new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
		String format = sd.format(data);
		String rootPath =  ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getFormFilePath().replace("/",File.separator));
		rootPath = rootPath.replace("/",File.separator)+File.separator+format;
		String uuid=null;
		if(uuid==null){ 
			uuid =  UUID.randomUUID().toString().replaceAll("-", ""); 
		}
		String newFileName=uuid+".zip";
		String filePath=rootPath+File.separator+newFileName;
		File zipFile = new File(filePath);
		ZipOutputStream zipStream = null;
		FileInputStream zipSource = null;
		BufferedInputStream bufferStream = null;
		try {
			zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}//用这个构造最终压缩包的输出流 
		for(int i=0;i<arr.length;i++){
			FileUpload fileUpload = getFileUpload(arr[i]);
			if(fileUpload!=null){
				String fileUrl = fileUpload.getFileUrl();
				String fileSaveName = fileUpload.getFileSaveName();
				String fileSrcName = fileUpload.getFileSrcName();
				File file=new File(rootPath+File.separator+fileSaveName);
		        try {
		            zipSource = null;//将源头文件格式化为输入流
		            zipSource = new FileInputStream(file);
		            
		            byte[] bufferArea = new byte[1024 * 10];//读写缓冲区
		            
		            //压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
		            ZipEntry zipEntry = new ZipEntry(fileSrcName);
		            zipStream.putNextEntry(zipEntry);//定位到该压缩条目位置，开始写入文件到压缩包中
		            
		            bufferStream = new BufferedInputStream(zipSource, 1024 * 10);//输入缓冲流
		            int read = 0;
		            //在任何情况下，b[0] 到 b[off] 的元素以及 b[off+len] 到 b[b.length-1] 的元素都不会受到影响。这个是官方API给出的read方法说明，经典！
		            while((read = bufferStream.read(bufferArea, 0, 1024 * 10)) != -1)
		            {
		                zipStream.write(bufferArea, 0, read);
		            }
		        
		        } catch (Exception e) {
		            logger.error(e,e);
		        } finally{
		        	 try {
		                 if(null != bufferStream) bufferStream.close();
		                 if(null != zipSource) zipSource.close();
		             } catch (Exception e) {
		                 // TODO Auto-generated catch block
		                 logger.error(e,e);
		             }
		        }
			}
		}
		 try {
             if(null != zipStream) zipStream.close();
         } catch (Exception e) {
             // TODO Auto-generated catch block
             logger.error(e,e);
         }
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		FileUpload fileModel = null;
		fileModel = new FileUpload();
		fileModel.setFileId(uuid); 
		fileModel.setFileUrl(filePath);
		fileModel.setFileSaveName(newFileName);
		fileModel.setFileSrcName(newFileName);
		fileModel.setUploadTime(sf.format(new Date()));
		FileUploadDAO uploadifyDAO = (FileUploadDAO) SpringBeanUtil.getBean("uploadifyDAO");
		FileUpload fu =uploadifyDAO.save(fileModel);
		if(fu==null){
			FileUploadUtil fileUploadUtil=new FileUploadUtil();
			fileUploadUtil.deleteFile(fileModel);
		}
		
		return uuid;
	}
	
	public FileUpload getFileUpload(Serializable id) {
		if(fileUploadDao==null){
			fileUploadDao = (FileUploadDAO)SpringBeanUtil.getBean("uploadifyDAO");
		}
		return fileUploadDao.getFileUpload(FileUpload.class, id);
	}

	public List<HashMap> getVoteList(int pageNumber, int pageSize) {
		List<HashMap> list=new ArrayList<HashMap>();
		list=zqbAnnouncementDAO.getVoteList(pageNumber,pageSize);
		return list;
	}

	public int getVoteListSize() {
		return zqbAnnouncementDAO.getVoteListSize();
	}

	public List<HashMap> getZxFeedBackList(String ggid, String khbh) {
		List lables = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables.add("PL"+i);
		}
		Map params_ = new HashMap();
		params_.put(1, ggid);
		params_.put(2, khbh);
		String sql ="SELECT PL1,PL2,PL3,PL4,PL5,PL6,PL7,PL8,PL9,PL10,PL11,PL12,PL13,PL14,PL15,PL16,PL17,PL18,PL19,PL20,PL21,PL22,PL23,PL24,PL25,PL26,PL27,PL28,PL29,PL30,PL31,PL32,PL33,PL34,PL35,PL36,PL37,PL38,PL39,PL40,PL41,PL42,PL43,PL44,PL45,PL46,PL47,PL48,PL49,PL50 FROM BD_VOTE_WJDC WHERE TZGGID=? AND CUSTOMERNO=?";
		List<HashMap> xpzcList = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params_);
		
		List data = new ArrayList();
		if(xpzcList.size()==1){
			HashMap xpzcMap = xpzcList.get(0);
			for (int i = 1; i <= 50; i++) {
				if(xpzcMap.get("PL"+i)!=null){
					data.add(xpzcMap.get("PL"+i));
				}
			}
		}
		int xpzcCount = data.size();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orguserrolrid;
		if(uc != null){orguserrolrid = uc.get_userModel().getOrgroleid();}else{orguserrolrid = 0l;}
		Map params = new HashMap();
		params.put(1, khbh);
		params.put(2, ggid);
		String lockstatus = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_WJDC WHERE CUSTOMERNO= ? AND TZGGID= ? ", params);
		if(lockstatus == null || lockstatus.equals("")){lockstatus="0";}
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List<HashMap> list=new ArrayList<HashMap>();
		HashMap conditionMap=new HashMap();
		conditionMap.put("TZGGID", ggid);
		conditionMap.put("CUSTOMERNO", khbh);
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String wjdcuuid = config.get("wjdcuuid");
		String wjdcwtuuid = config.get("wjdcwtuuid");
		
		List lables_ = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables_.add("WT"+i);
			lables_.add("AS"+i);
			lables_.add("BZ"+i);
			lables_.add("PL"+i);
			lables_.add("PZ"+i);
			lables_.add("YJXYSFFS"+i);
		}
		lables_.add("CUSTOMERNAME");
		lables_.add("CUSTOMERNO");
		lables_.add("USERNAME");
		lables_.add("USERID");
		lables_.add("TZGGID");
		lables_.add("ID");
		String sql_ = "SELECT * FROM BD_VOTE_WJDC WHERE CUSTOMERNO=? AND TZGGID=?";
		Map params__ = new HashMap();
		params__.put(1, khbh);
		params__.put(2, ggid);
		List<HashMap> list2 = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);//DemAPI.getInstance().getList(wjdcuuid, conditionMap, "ID");
		for (HashMap hashMap : list2) {
			int num=xpzcCount;
			for(Object o : hashMap.keySet()){
				if(hashMap.get(o)!=null&&!"".equals(hashMap.get(o).toString())&&"WT".equals(o.toString().substring(0, 2))){
					if(Integer.parseInt(o.toString().substring(2))>xpzcCount){
						num++;
					}
				}
			}
			List keyList=new ArrayList();
			for (int i = (xpzcCount+1); i <= num; i++) {
				keyList.add("WT"+i);
			}
			int countSize=0;
			
			for (Object object : keyList) {
				HashMap map=new HashMap();
				String question=hashMap.get(object).toString().replace("\r\n","").replace("\r", "").replace("\n", "");
				params = new HashMap();
				params.put(1, "%"+question+"%");
				String defultAnswer = DBUTilNew.getDataStr("DEFULTANSWER","SELECT DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE TYPE IN ('知悉问题','信披自查') AND QUESTION like ? ", params);
				defultAnswer = defultAnswer==null||defultAnswer.equals("")?"是":defultAnswer;
				String count=object.toString().substring(2, object.toString().length());
				if((defultAnswer!=null&&((hashMap.get("AS"+count)!=null&&!hashMap.get("AS"+count).toString().equals(defultAnswer))  || (hashMap.get("BZ"+count)!=null&&!"".equals(hashMap.get("BZ"+count).toString()))))){
						StringBuffer content=new StringBuffer();
						countSize++;
						map.put("CUSTOMERNO", hashMap.get("CUSTOMERNO").toString());
						map.put("CUSTOMERNAME", hashMap.get("CUSTOMERNAME").toString());
						map.put("QUESTION", countSize+"、"+question);
						
						map.put("ANSWER", hashMap.get("AS"+count)==null?"":hashMap.get("AS"+count).toString());
						map.put("BZ", hashMap.get("BZ"+count)==null?"":hashMap.get("BZ"+count).toString());
						map.put("PL", hashMap.get("PL"+count)==null?"":hashMap.get("PL"+count).toString());
						map.put("PZ", hashMap.get("PZ"+count)==null?"":hashMap.get("PZ"+count).toString());
						map.put("YJXYSFFS", hashMap.get("YJXYSFFS"+count)==null?"":hashMap.get("YJXYSFFS"+count).toString());
						map.put("ORGUSERROLRID", orguserrolrid);
						map.put("LOCKSTATUS", lockstatus);
						map.put("COUNT", count);
						list.add(map);
				}
			}
		}
		return list;
	}
	
	public List<HashMap> getZxSuccessFeedBackList(Long ggid,String khbh,int size) {
		List lables = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables.add("PL"+i);
		}
		Map params_ = new HashMap();
		params_.put(1, ggid);
		params_.put(2, khbh);
		String sql ="SELECT PL1,PL2,PL3,PL4,PL5,PL6,PL7,PL8,PL9,PL10,PL11,PL12,PL13,PL14,PL15,PL16,PL17,PL18,PL19,PL20,PL21,PL22,PL23,PL24,PL25,PL26,PL27,PL28,PL29,PL30,PL31,PL32,PL33,PL34,PL35,PL36,PL37,PL38,PL39,PL40,PL41,PL42,PL43,PL44,PL45,PL46,PL47,PL48,PL49,PL50 FROM BD_VOTE_WJDC WHERE TZGGID=? AND CUSTOMERNO=?";
		List<HashMap> xpzcList = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params_);
		
		List data = new ArrayList();
		if(xpzcList.size()==1){
			HashMap xpzcMap = xpzcList.get(0);
			for (int i = 1; i <= 50; i++) {
				if(xpzcMap.get("PL"+i)!=null){
					data.add(xpzcMap.get("PL"+i));
				}
			}
		}
		int xpzcCount = data.size();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orguserrolrid;
		String userid="";
		if(uc != null){orguserrolrid = uc.get_userModel().getOrgroleid();}else{orguserrolrid = 0l;}
		if(uc!= null){userid = uc.get_userModel().getUserid();}
		Map params = new HashMap();
		params.put(1, khbh);
		params.put(2, ggid);
		String lockstatus = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_WJDC WHERE CUSTOMERNO= ?  AND TZGGID= ? ", params);
		if(lockstatus == null || lockstatus.equals("")){lockstatus="0";}
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List<HashMap> list=new ArrayList<HashMap>();
		HashMap conditionMap=new HashMap();
		conditionMap.put("TZGGID", ggid);
		conditionMap.put("CUSTOMERNO", khbh);
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String wjdcuuid = config.get("wjdcuuid");
		String wjdcwtuuid = config.get("wjdcwtuuid");
		boolean isSuperMan = this.getIsSuperMan();
		
		List lables_ = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables_.add("WT"+i);
			lables_.add("AS"+i);
			lables_.add("BZ"+i);
			lables_.add("PL"+i);
			lables_.add("PZ"+i);
			lables_.add("YJXYSFFS"+i);
		}
		lables_.add("CUSTOMERNAME");
		lables_.add("CUSTOMERNO");
		lables_.add("USERNAME");
		lables_.add("USERID");
		lables_.add("TZGGID");
		lables_.add("ID");
		String sql_ = "SELECT * FROM BD_VOTE_WJDC WHERE CUSTOMERNO=? AND TZGGID=?";
		Map params__ = new HashMap();
		params__.put(1, khbh);
		params__.put(2, ggid);
		List<HashMap> list2 = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);//DemAPI.getInstance().getList(wjdcuuid, conditionMap, "ID");
		for (HashMap hashMap : list2) {
			int num=xpzcCount;
			for(Object o : hashMap.keySet()){
				if(hashMap.get(o)!=null&&!"".equals(hashMap.get(o).toString())&&"WT".equals(o.toString().substring(0, 2))){
					if(Integer.parseInt(o.toString().substring(2))>xpzcCount){
						num++;
					}
				}
			}
			List keyList=new ArrayList();
			for (int i = (xpzcCount+1); i <= num; i++) {
				keyList.add("WT"+i);
			}
			
			for (Object object : keyList) {
				HashMap map=new HashMap();
				String question=hashMap.get(object).toString().replace("\r\n","").replace("\r", "").replace("\n", "");
				params= new HashMap();
				params.put(1, "%"+question+"%");
				String defultAnswer = DBUTilNew.getDataStr("DEFULTANSWER","SELECT DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE TYPE IN ('知悉问题','信披自查') AND QUESTION like ? ", params);
				defultAnswer = defultAnswer==null||defultAnswer.equals("")?"是":defultAnswer;
				String count=object.toString().substring(2, object.toString().length());
				if((defultAnswer!=null&&(hashMap.get("AS"+count)==null||hashMap.get("AS"+count).toString().equals(defultAnswer))  && (hashMap.get("BZ"+count)==null||"".equals(hashMap.get("BZ"+count).toString())) )){
					size++;
					map.put("CUSTOMERNO", hashMap.get("CUSTOMERNO").toString());
					map.put("CUSTOMERNAME", hashMap.get("CUSTOMERNAME").toString());
					map.put("QUESTION", size+"、"+question);
					map.put("ANSWER", hashMap.get("AS"+count)==null?"":hashMap.get("AS"+count).toString());
					map.put("BZ", hashMap.get("BZ"+count)==null?"":hashMap.get("BZ"+count).toString());
					map.put("PL", hashMap.get("PL"+count)==null?"":hashMap.get("PL"+count).toString());
					map.put("PZ", hashMap.get("PZ"+count)==null?"":hashMap.get("PZ"+count).toString());
					map.put("YJXYSFFS", hashMap.get("YJXYSFFS"+count)==null?"":hashMap.get("YJXYSFFS"+count).toString());
					map.put("ORGUSERROLRID", orguserrolrid);
					map.put("LOCKSTATUS", lockstatus);
					map.put("COUNT", count);
					list.add(map);
				}
			}
		}
		return list;
	}
	
	public List<HashMap> xpList(){
		Long orgRoleId = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
		List<HashMap> result = new ArrayList<HashMap>();
		
		List lables = new ArrayList();
		lables.add("QUESTION");
		lables.add("DEFULTANSWER");
		String sql = "SELECT QUESTION,DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE STATUS='显示' AND TYPE='信披自查' ORDER BY SORTID";
		List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, null);
		HashMap obj = null;
		int n = 1;
		for (HashMap map : data) {
			obj = new HashMap();
			obj.put("ANSWER", map.get("DEFULTANSWER"));
			obj.put("PL", map.get("DEFULTANSWER"));
			obj.put("YJXYSFFS", map.get("DEFULTANSWER"));
			obj.put("LOCKSTATUS", 0);
			obj.put("CUSTOMERNO", "NO-9999-99-99");
			obj.put("BZ", "无");
			obj.put("PZ", "无");
			obj.put("COUNT", n);
			obj.put("CUSTOMERNAME", "客户名称");
			obj.put("ORGUSERROLRID", orgRoleId);
			obj.put("QUESTION", n+"、"+map.get("QUESTION"));
			result.add(obj);
			n++;
		}
		return result;
	}
	
	public List<HashMap> zxList(int xpnum){
		Long orgRoleId = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
		List<HashMap> result = new ArrayList<HashMap>();
		
		List lables = new ArrayList();
		lables.add("QUESTION");
		lables.add("DEFULTANSWER");
		String sql = "SELECT QUESTION,DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE STATUS='显示' AND TYPE='知悉问题' ORDER BY SORTID";
		List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, null);
		HashMap obj = null;
		int n = xpnum+1;
		for (HashMap map : data) {
			obj = new HashMap();
			obj.put("ANSWER", map.get("DEFULTANSWER"));
			obj.put("PL", map.get("DEFULTANSWER"));
			obj.put("YJXYSFFS", map.get("DEFULTANSWER"));
			obj.put("LOCKSTATUS", 0);
			obj.put("CUSTOMERNO", "NO-9999-99-99");
			obj.put("BZ", "无");
			obj.put("PZ", "无");
			obj.put("COUNT", n);
			obj.put("CUSTOMERNAME", "客户名称");
			obj.put("ORGUSERROLRID", orgRoleId);
			obj.put("QUESTION", (n-xpnum)+"、"+map.get("QUESTION"));
			result.add(obj);
			n++;
		}
		return result;
	}
	
	public String getWjdcContent(String ggid,String khbh){
		HashMap<String,String> result = new HashMap<String,String>();
		
		List<String> labs = new ArrayList<String>();
		for (int i = 1; i <= 50; i++) {
			labs.add("WT"+i);
		}
		String sq = "SELECT * FROM BD_VOTE_WJDC WHERE TZGGID=? AND CUSTOMERNO=?";
		Map para = new HashMap();
		para.put(1, ggid);
		para.put(2, khbh);
		List<HashMap> wtList = com.iwork.commons.util.DBUtil.getDataList(labs, sq, para);
		
		String s ="SELECT MEMO FROM BD_VOTE_WJDCWTB WHERE QUESTION = ?";
		Map par = new HashMap();
		for (int i = 1; i <= wtList.size(); i++) {
			par.put(1, wtList.get(0).get("WT"+i)==null?"":wtList.get(0).get("WT"+i).toString());
			result.put("BZ"+i, com.iwork.commons.util.DBUtil.getDataStr("MEMO", s, par));
			par.clear();
		}
		
		
		
		/*List<String> lables = new ArrayList<String>();
		lables.add("MEMO");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MEMO FROM BD_VOTE_WJDCWTB WHERE TYPE = '信披自查' ORDER BY SORTID ASC");
		Map params = new HashMap();
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()>=1){
			for (int i = 0; i < content.size(); i++) {
				result.put("BZ"+(i+1), content.get(i).get("MEMO")==null?"":content.get(i).get("MEMO").toString());
			}
		}
		sql.delete(0,sql.length());
		sql.append("SELECT MEMO FROM BD_VOTE_WJDCWTB WHERE TYPE = '知悉问题' ORDER BY SORTID ASC");
		List<HashMap> content_ = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		if(content_.size()>=1){
			for (int i = 0; i < content_.size(); i++) {
				result.put("BZ"+(i+(content.size()+1)), content_.get(i).get("MEMO")==null?"":content_.get(i).get("MEMO").toString());
			}
		}
		for (int i = (content.size()+content_.size()+1); i <= 50; i++) {
			result.put("BZ"+i, "");
		}*/
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String getWjdcWt(String ggid){
		List<String> lables = new ArrayList<String>();
		for (int i = 1; i <= 50; i++) {
			lables.add("WT"+i);
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT WT1,WT2,WT3,WT4,WT5,WT6,WT7,WT8,WT9,WT10,WT11,WT12,WT13,WT14,WT15,WT16,WT17,WT18,WT19,WT20,WT21,WT22,WT23,WT24,WT25,WT26,WT27,WT28,WT29,WT30,WT31,WT32,WT33,WT34,WT35,WT36,WT37,WT38,WT39,WT40,WT41,WT42,WT43,WT44,WT45,WT46,WT47,WT48,WT49,WT50 FROM BD_VOTE_WJDC WHERE TZGGID=?");
		
		Map params = new HashMap();
		params.put(1, ggid);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()>0){
			HashMap data = content.get(0);
			for (int i = 1; i <= 50; i++) {
				result.put("WT"+i, data.get("WT"+i)==null?"":data.get("WT"+i).toString());
			}
		}else{
			List<String> lables_ = new ArrayList<String>();
			lables_.add("WT");
			
			StringBuffer sql_ = new StringBuffer();
			sql_.append("SELECT QUESTION AS WT FROM BD_VOTE_WJDCWTB WHERE TYPE IN ('信披自查','知悉问题') ORDER BY TYPE ASC,SORTID ASC");
			
			List<HashMap> wt = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_.toString(), null);
			for (int i = 0; i <= 49; i++) {
				if(i<wt.size()){
					result.put("WT"+(i+1), wt.get(i).get("WT")==null?"":wt.get(i).get("WT").toString());
				}else{
					result.put("WT"+(i+1),"");
				}
			}
		}
		
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String getTzggFssj(String ggid){
		List<String> lables = new ArrayList<String>();
		lables.add("THISMON");
		lables.add("NEXTMON");
		lables.add("D");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,ADD_MONTHS(FSSJ,-1) THISMON,FSSJ NEXTMON,TO_CHAR(FSSJ,'dd') D FROM BD_XP_TZGGB WHERE ID=?");
		
		Map params = new HashMap();
		params.put(1, ggid);
		
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			result.put("THISMON", content.get(0).get("THISMON").toString().substring(0, 7));
			result.put("NEXTMON", content.get(0).get("NEXTMON").toString().substring(0, 7));
			result.put("DD", content.get(0).get("D")==null?"":content.get(0).get("D").toString());
		}else{
			result.put("THISMON", "");
			result.put("NEXTMON", "");
			result.put("DD", "");
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public List<HashMap> getFeedBackList(Long ggid, String khbh) {
		List lables = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables.add("PL"+i);
		}
		Map params_ = new HashMap();
		params_.put(1, ggid);
		params_.put(2, khbh);
		String sql ="SELECT PL1,PL2,PL3,PL4,PL5,PL6,PL7,PL8,PL9,PL10,PL11,PL12,PL13,PL14,PL15,PL16,PL17,PL18,PL19,PL20,PL21,PL22,PL23,PL24,PL25,PL26,PL27,PL28,PL29,PL30,PL31,PL32,PL33,PL34,PL35,PL36,PL37,PL38,PL39,PL40,PL41,PL42,PL43,PL44,PL45,PL46,PL47,PL48,PL49,PL50 FROM BD_VOTE_WJDC WHERE TZGGID=? AND CUSTOMERNO=?";
		List<HashMap> xpzcList = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params_);
		
		List data = new ArrayList();
		if(xpzcList.size()==1){
			HashMap xpzcMap = xpzcList.get(0);
			for (int i = 1; i <= 50; i++) {
				if(xpzcMap.get("PL"+i)!=null){
					data.add(xpzcMap.get("PL"+i));
				}
			}
		}
		int xpzcCount = data.size();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orguserrolrid;
		if(uc != null){orguserrolrid = uc.get_userModel().getOrgroleid();}else{orguserrolrid = 0l;}
		Map params = new HashMap();
		params.put(1, khbh);
		params.put(2, ggid);
		String lockstatus = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_WJDC WHERE CUSTOMERNO= ? AND TZGGID= ? ", params);
		if(lockstatus == null || lockstatus.equals("")){lockstatus="0";}
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List<HashMap> list=new ArrayList<HashMap>();
		HashMap conditionMap=new HashMap();
		conditionMap.put("TZGGID", ggid);
		conditionMap.put("CUSTOMERNO", khbh);
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String wjdcuuid = config.get("wjdcuuid");
		String wjdcwtuuid = config.get("wjdcwtuuid");
		
		
		List lables_ = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables_.add("WT"+i);
			lables_.add("AS"+i);
			lables_.add("BZ"+i);
			lables_.add("PL"+i);
			lables_.add("PZ"+i);
			lables_.add("YJXYSFFS"+i);
		}
		lables_.add("CUSTOMERNAME");
		lables_.add("CUSTOMERNO");
		lables_.add("USERNAME");
		lables_.add("USERID");
		lables_.add("TZGGID");
		lables_.add("ID");
		String sql_ = "SELECT * FROM BD_VOTE_WJDC WHERE CUSTOMERNO=? AND TZGGID=?";
		Map params__ = new HashMap();
		params__.put(1, khbh);
		params__.put(2, ggid);
		List<HashMap> list2 = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);//DemAPI.getInstance().getList(wjdcuuid, conditionMap, "ID");
		for (HashMap hashMap : list2) {
			int num=0;
			for(Object o : hashMap.keySet()){
				if(hashMap.get(o)!=null&&!"".equals(hashMap.get(o).toString())&&"WT".equals(o.toString().substring(0, 2))){
					if(Integer.parseInt(o.toString().substring(2))<(xpzcCount+1)){
						num++;
					}
				}
			}
			List keyList=new ArrayList();
			for (int i = 1; i <= num; i++) {
				keyList.add("WT"+i);
			}
			int countSize=0;
			
			for (Object object : keyList) {
				HashMap map=new HashMap();
				String question=hashMap.get(object).toString().replace("\r\n","").replace("\r", "").replace("\n", "");
				params = new HashMap();
				params.put(1, "%"+question+"%");
				String defultAnswer = DBUTilNew.getDataStr("DEFULTANSWER","SELECT DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE TYPE IN ('知悉问题','信披自查') AND QUESTION like ? ", params);
				defultAnswer = defultAnswer==null||defultAnswer.equals("")?"否":defultAnswer;
				String count=object.toString().substring(2, object.toString().length());
				if((defultAnswer!=null&&((hashMap.get("AS"+count)!=null&&!hashMap.get("AS"+count).toString().equals(defultAnswer))) || (hashMap.get("YJXYSFFS"+count)!=null&&!hashMap.get("YJXYSFFS"+count).toString().equals(defultAnswer) )|| (hashMap.get("BZ"+count)!=null&&!"".equals(hashMap.get("BZ"+count).toString()) )  || (hashMap.get("PL"+count)!=null&&!hashMap.get("PL"+count).toString().equals(defultAnswer) ))){
						StringBuffer content=new StringBuffer();
						countSize++;
						map.put("CUSTOMERNO", hashMap.get("CUSTOMERNO").toString());
						map.put("CUSTOMERNAME", hashMap.get("CUSTOMERNAME").toString());
						map.put("QUESTION", countSize+"、"+question);
						
						map.put("ANSWER", hashMap.get("AS"+count)==null?"":hashMap.get("AS"+count).toString());
						map.put("BZ", hashMap.get("BZ"+count)==null?"":hashMap.get("BZ"+count).toString());
						map.put("PL", hashMap.get("PL"+count)==null?"":hashMap.get("PL"+count).toString());
						map.put("PZ", hashMap.get("PZ"+count)==null?"":hashMap.get("PZ"+count).toString());
						map.put("YJXYSFFS", hashMap.get("YJXYSFFS"+count)==null?"":hashMap.get("YJXYSFFS"+count).toString());
						map.put("ORGUSERROLRID", orguserrolrid);
						map.put("LOCKSTATUS", lockstatus);
						map.put("COUNT", count);
						list.add(map);
				}
			}
		}
		return list;
	}
	
	public List<HashMap> getNoFeedBackList2(String ggid,String zqdm,String status,String sszjj,String ssbm) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		boolean isSuperMan = this.getIsSuperMan();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String wjdcuuid = config.get("wjdcuuid");
		String wjdcwtuuid = config.get("wjdcwtuuid");
		List<HashMap> list=new ArrayList<HashMap>();
		List<HashMap> ykkResult=new ArrayList<HashMap>();
		List<HashMap> listResult=new ArrayList<HashMap>();
		HashMap<String,String> answerMap=zqbAnnouncementDAO.getAnswerMap();
		List keyList=new ArrayList();
		list=zqbAnnouncementDAO.getNoFeedBackList(ggid,username,isSuperMan,zqdm,status,sszjj,ssbm);
		for (HashMap map : list) {
			if("未反馈".equals(map.get("STATUS").toString())){
				map.put("READONLY", false);
				listResult.add(map);
			}else{
				List lables_ = new ArrayList();
				for (int i = 1; i <= 50; i++) {
					lables_.add("WT"+i);
					lables_.add("AS"+i);
					lables_.add("BZ"+i);
					lables_.add("PL"+i);
					lables_.add("PZ"+i);
					lables_.add("YJXYSFFS"+i);
				}
				lables_.add("CUSTOMERNAME");
				lables_.add("CUSTOMERNO");
				lables_.add("USERNAME");
				lables_.add("USERID");
				lables_.add("TZGGID");
				lables_.add("ID");
				String sql_ = "SELECT * FROM BD_VOTE_WJDC WHERE CUSTOMERNO=? AND TZGGID=?";
				Map params__ = new HashMap();
				params__.put(1, map.get("CUSTOMERNO").toString());
				params__.put(2, ggid);
				List<HashMap> list2 = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);//DemAPI.getInstance().getList(wjdcuuid, conditionMap, "ID");
				map.put("READONLY", false);
				for (HashMap hashMap : list2) {
					if(keyList.isEmpty()){
						for(Object o : hashMap.keySet()){
							if(hashMap.get(o)!=null&&!"".equals(hashMap.get(o).toString())&&"WT".equals(o.toString().substring(0, 2))){
								keyList.add(o.toString());
							}
						}
					}
					for (Object object : keyList) {
						String question=hashMap.get(object).toString();
						String defultAnswer=answerMap.get(question);
						Map params = new HashMap();
						params.put(1,question);
						String type=DBUTilNew.getDataStr("type", "SELECT type FROM BD_VOTE_WJDCWTB where question= ? ", params);
						defultAnswer = defultAnswer==null?type.equals("知悉问题")?"是":"否":defultAnswer;
						String count=object.toString().substring(2, object.toString().length());
						if(type.equals("知悉问题")){
							if( (defultAnswer!=null&&((hashMap.get("AS"+count)!=null&&!hashMap.get("AS"+count).toString().equals(defultAnswer))) || (hashMap.get("BZ"+count)!=null&&!"".equals(hashMap.get("BZ"+count).toString()))  || (hashMap.get("PZ"+count)!=null&&!"".equals(hashMap.get("PZ"+count).toString())))){
								map.put("READONLY", true);
								listResult.add(map);
								break;
							}
						}else{
							if( (defultAnswer!=null&&((hashMap.get("AS"+count)!=null&&!hashMap.get("AS"+count).toString().equals(defultAnswer))) || (hashMap.get("BZ"+count)!=null&&!"".equals(hashMap.get("BZ"+count).toString()))  || (hashMap.get("PZ"+count)!=null&&!"".equals(hashMap.get("PZ"+count).toString())) || (defultAnswer!=null&&hashMap.get("PL"+count)!=null&&!hashMap.get("PL"+count).toString().equals(defultAnswer)) || (defultAnswer!=null&&hashMap.get("YJXYSFFS"+count)!=null&&!hashMap.get("YJXYSFFS"+count).toString().equals(defultAnswer)))){
								map.put("READONLY", true);
								listResult.add(map);
								break;
							}
						}
						
					}
				}
			}
		}	
		for (HashMap map : list) {
			boolean readonly=(Boolean) map.get("READONLY");
			if("已反馈".equals(map.get("STATUS").toString())&&!readonly){
				listResult.add(map);
			}
		}
		return listResult;
	}
	public String xpwtcfyz(String str,Long instanceid){
		String flags="true";
		List lables=new ArrayList();
		lables.add("question");
		str=str.replace(" ","").replaceAll("[\\pP\\p{Punct}]","");
		int n=0;
		List<HashMap> lists=DBUTilNew.getDataList(lables, "select question from BD_VOTE_WJDCWTB where type='信披自查' or type ='知悉问题' ", null);
		if(lists!=null && lists.size()>0){
			for (int i = 0; i < lists.size(); i++) {
					lists.get(i).get("question").toString().replace(" ","").replaceAll("[\\pP\\p{Punct}]","");
					if(lists.get(i).get("question").toString().replace(" ","").replaceAll("[\\pP\\p{Punct}]","").equals(str)){
						n++;
					
				}
			}
		}
		if(instanceid==0){
			if(n!=0){
				flags="false";
			}
		}else{
			if(n>=2){
				flags="false";
			}
		}
		return flags;
	}
	public String xpwtcfyz(){
		String flag="false";
		int s= DBUTilNew.getInt("sl", " select count(id) sl from bd_vote_wjdcwtb s where s.type='知悉问题'  or s.type='信披自查'  ", null);
		if(s<50){
			flag="true";
		}
		return flag;
	}
	//获取信披自查反馈沟通附件数量
	public int voteGoxpfjsl(String ggid){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String usernames=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		StringBuffer sql = new StringBuffer();
		sql.append("select count(XPGTFILE) CNUM from BD_VOTE_XPZCFKGTB  where ggid=?");
		if(uc.get_userModel().getOrgroleid().equals(new Long(4))){
			sql.append("  and  customerno in (select s.khbh   from bd_mdm_khqxglb s ");
			sql.append("     where s.khfzr =?   or s.zzcxdd =?     or s.zzspr = ?   or s.fhspr =? ");
			sql.append("   or s.cwscbfzr2 = ?    or s.cwscbfzr3 = ?     or GGFBR =?) ");
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ggid);
			if(uc.get_userModel().getOrgroleid().equals(new Long(4))){
				ps.setString(2, usernames);
				ps.setString(3, usernames);
				ps.setString(4, usernames);
				ps.setString(5, usernames);
				ps.setString(6, usernames);
				ps.setString(7, usernames);
				ps.setString(8, usernames);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}
	//获取信披自查反馈沟通数据
	public List<HashMap> getgghfvotesj(String ggid){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List list = new ArrayList<HashMap>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct kh.zqjc,kh.zqdm,org.username,org.userid,hf.status,gt.xpgtfile,f.file_src_name,f.file_url from"+
     "   (select distinct userid,ggid,status,customerno from bd_xp_hfqkb where ggid= ?) hf "+
     "   inner join bd_vote_wjdc b on b.customerno = hf.customerno and b.tzggid=hf.ggid "+
     "   inner join orguser org on org.userid = b.userid "+
     "   inner join              "+
     "   (SELECT ID,REGEXP_SUBSTR(xpgtfile, '[^,]+', 1, RN) xpgtfile,ggid,customerno"+
     "        FROM (select * from bd_vote_xpzcfkgtb where xpgtfile is not null)"+
     "        , (SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(xpgtfile, '[^,]+', 1, RN) IS NOT NULL) gt "+
     "   on hf.ggid=gt.ggid and hf.customerno=gt.customerno    "+    
    "     left join bd_zqb_kh_base kh on kh.customerno = hf.customerno "+
     "    inner join sys_upload_file f on gt.xpgtfile = f.file_id");
		if(uc.get_userModel().getOrgroleid().equals(new Long(4))){
			sql.append("  and  kh.zqdm in (select bzkb.zqdm  from bd_zqb_kh_base bzkb where bzkb.customerno in(select s.khbh   from bd_mdm_khqxglb s ");
			sql.append("     where s.khfzr =?   or s.zzcxdd =?     or s.zzspr = ?   or s.fhspr =? ");
			sql.append("   or s.cwscbfzr2 = ?    or s.cwscbfzr3 = ?     or GGFBR =?)) ");
		}
		sql.append("   order by hf.status desc,kh.zqdm ");
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ggid);
			if(uc.get_userModel().getOrgroleid().equals(new Long(4))){
				ps.setString(2, username);
				ps.setString(3, username);
				ps.setString(4, username);
				ps.setString(5, username);
				ps.setString(6, username);
				ps.setString(7, username);
				ps.setString(8, username);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String gsdm = rs.getString("ZQDM");				//公司代码
				String gsmc = rs.getString("ZQJC");				//公司名称
				String hfr = rs.getString("USERNAME");			//回复人
				String userid = rs.getString("USERID");			//回复人id
				String hfzt = rs.getString("status");			//回复状态	
				String xgzl = rs.getString("XPGTFILE");			//附件
				String filename = rs.getString("file_src_name");			//附件名称
				String fileurl = rs.getString("file_url");			//附件URL
				map.put("GSDM", gsdm==null?"":gsdm);
				map.put("GSMC", gsmc);
				map.put("HFR", hfr);
				map.put("USERID", userid);
				map.put("HFZT", hfzt);
				map.put("XGZL", xgzl==null?"":xgzl);
				map.put("FILENAME", filename);
				map.put("FILEURL", fileurl);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}	
		return list;
	}
	public String doExcelImp(File upfile,Long demId){
	

	
	    String userid = UserContextUtil.getInstance().getCurrentUserId();
	    String msg = "success";
	    try {
	      InputStream is = new FileInputStream(upfile);
	      HSSFWorkbook hwk = new HSSFWorkbook(is);
	      HSSFSheet sheet = hwk.getSheetAt(0);

	      int rowIndex = 0;
	      HSSFRow row = sheet.getRow(rowIndex);
	  	 
	      HSSFCell cell = null;
	      row = sheet.getRow(rowIndex);
	      Hashtable fields = new Hashtable();
	      for (short i = 0; i < row.getLastCellNum(); i = (short)(i + 1)) {
	        cell = row.getCell(i);
	        if ((cell != null) && (cell.getCellType() != 0))
	        {
	          String fieldName = "";
	          String temp = cell.getStringCellValue();
	          if (!"".equals(temp)) {
	            if (temp.indexOf("<") > 0)
	              fieldName = temp.substring(0, cell.getStringCellValue().indexOf("<"));
	            else {
	              fieldName = temp;
	            }
	            fields.put(new Short(i), fieldName);
	          }
	        }
	      }
	      if (fields.size() < 1) {
	        return "typeerror";
	      }

	      rowIndex++;

	      int dataNum = 0;
	      int erroecount = 0;
	      String cf = "";
	      int count =1;
	      for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
		        row = sheet.getRow(i);
		        StringBuffer bindData = new StringBuffer();
		        HashMap rowdata = new HashMap();
		        for (short column = 0; column < row.getLastCellNum(); column = (short)(column + 1)) {
		          cell = row.getCell(column);
		          String value = "";
		          Object ofieldName = fields.get(new Short(column));
		          if (ofieldName != null)
		          {
		            String fieldName = ((String)ofieldName).toUpperCase();
		            if (cell == null) {
		              value = "";
		            }
		            else if (cell.getCellType() == 0)
		            {
		              if (HSSFDateUtil.isCellDateFormatted(cell)) {
		                value = UtilDate.dateFormat(cell.getDateCellValue());
		              } else {
		                value = Double.toString(cell.getNumericCellValue());

		                if (value != null) {
		                  if (value.toUpperCase().indexOf("E") > 0)
		                  {
		                    value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
		                    value = value.replaceAll(",", "");
		                  }

		                  if ((value.indexOf(".0") > 0) && (value.lastIndexOf("0") == value.length() - 1))
		                    value = value.substring(0, value.length() - 2);
		                }
		              }
		            }
		            else if (cell.getCellType() == 2) {
		              try {
		                value = String.valueOf(cell.getNumericCellValue());
		              } catch (Exception e) {
		                value = String.valueOf(cell.getRichStringCellValue());
		              }
		            } else {
		              value = cell.getStringCellValue();
		              if (value == null) {
		                value = "";
		              }
		            }
		            value = value.trim();
		            rowdata.put(fieldName, value);
		          }
		        }
		        if (fields.size() >= 1){
		          if(!rowdata.get("QUESTION").equals("")){
		        	  if(count<=50){
				          count++; 
		        	  }else{
		        		  return "flag";
		        	  }
		          }
		          
		        }
		      }
		  	DBUtil.executeUpdate("DELETE FROM BD_VOTE_WJDCWTB WHERE type='信披自查' or type='知悉问题'  ");
		    List lables=new ArrayList();
  	  	  lables.add("question");
	      for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
	        row = sheet.getRow(i);
	        StringBuffer bindData = new StringBuffer();
	        HashMap rowdata = new HashMap();
	        for (short column = 0; column < row.getLastCellNum(); column = (short)(column + 1)) {
	          cell = row.getCell(column);
	          String value = "";
	          Object ofieldName = fields.get(new Short(column));
	          if (ofieldName != null)
	          {
	            String fieldName = ((String)ofieldName).toUpperCase();
	            if (cell == null) {
	              value = "";
	            }
	            else if (cell.getCellType() == 0)
	            {
	              if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                value = UtilDate.dateFormat(cell.getDateCellValue());
	              } else {
	                value = Double.toString(cell.getNumericCellValue());

	                if (value != null) {
	                  if (value.toUpperCase().indexOf("E") > 0)
	                  {
	                    value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
	                    value = value.replaceAll(",", "");
	                  }

	                  if ((value.indexOf(".0") > 0) && (value.lastIndexOf("0") == value.length() - 1))
	                    value = value.substring(0, value.length() - 2);
	                }
	              }
	            }
	            else if (cell.getCellType() == 2) {
	              try {
	                value = String.valueOf(cell.getNumericCellValue());
	              } catch (Exception e) {
	                value = String.valueOf(cell.getRichStringCellValue());
	              }
	            } else {
	              value = cell.getStringCellValue();
	              if (value == null) {
	                value = "";
	              }
	            }
	            value = value.trim();
	            rowdata.put(fieldName, value);
	          }
	        }
	     //   rowdata.put("TYPE", "信披自查");
	        rowdata.put("STATUS", "显示");
	        rowdata.put("SORTID", count);
	        if (fields.size() >= 1){
	          if(!rowdata.get("QUESTION").equals("")){
	        	  	boolean falg=true;
	        	  	  List<HashMap> lists=DBUTilNew.getDataList(lables, "select question from BD_VOTE_WJDCWTB where type='信披自查' or type ='知悉问题' ", null);
		        	  if(lists!=null && lists.size()>0){
		        			for (int j = 0; j < lists.size(); j++) {
		        				if(lists.get(j).get("question").toString().replace(" ","").replaceAll("[\\pP\\p{Punct}]","").equals(rowdata.get("QUESTION").toString().replace(" ","").replaceAll("[\\pP\\p{Punct}]",""))  ){
		        					falg=false;
		        				}
		        			}
		        		
		        	  }
		        	 
		        	  if(!falg){
		        		 if(!"".equals(cf)){
		        			 cf=cf+","+(i+1);
		        		 }else{
		        			 cf=(i+1)+"";
		        		 }
		        	  }else{
		        		  Long instanceId = DemAPI.getInstance().newInstance(demId, userid);
		        			
				          boolean isadd = DemAPI.getInstance().saveFormData(demId, instanceId, rowdata, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
				          if (!isadd) {
				            erroecount++;
				          }
				          dataNum++;
				          count++; 
		        	  }
		        		  
			        
	        	 
	          }
	          
	        }
	      }
	      if (dataNum == 0) {
	        msg = "error";
	      }
	      if (erroecount > 0)
	        msg = "第" + erroecount + "条数据导入异常!";
	      if(!"".equals(cf))
	    	  msg = "第" + cf + "条数据重复未导入!";
	    }
	    catch (FileNotFoundException e) {
	      msg = "error";
	    } catch (Exception e) {
	      msg = "error";
	    }
	    return msg;
	  }
	//下载附件
			public String votedownload(List<HashMap> list) throws Exception {
				//复制附件到指定文件夹
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				String filepath = request.getSession().getServletContext().getRealPath("/");
				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
				Date date = new Date();
				
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				OrgUser user = uc.get_userModel();
				//String foldername = "信披自查反馈批量下载文件";
				String foldername = user.getUserid()+sdf.format(date);
				//删除原有文件夹
				File filedel = new File(filepath+foldername);
	        	delChange(filedel);
				String ghfry="";
				for(HashMap map:list){
								String srcname = map.get("FILENAME").toString();//xxxxx
								//循环复制文件
								File f = new File(filepath+map.get("FILEURL").toString());
								if(f.exists()){
									//根据角色id 创建不同目录
									ghfry = map.get("GSDM").toString();									
									File fnew = new File(filepath+foldername+"\\"+ghfry);
									int byteread = 0;
									int bytesum = 0;
									InputStream inStream = new FileInputStream(f);
									if(!fnew.exists()){
										fnew.mkdirs();
										StringBuffer sb = new StringBuffer();
										//根据角色id 打包不同的文件夹
										ghfry = map.get("GSDM").toString();
										sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(ghfry).append("\\").append(srcname);
										
										fnew = new File(sb.toString());
										fnew.createNewFile();
										FileOutputStream fsot = new FileOutputStream(fnew);
										byte[] buffer = new byte[1444]; 
										while ( (byteread = inStream.read(buffer)) != -1) { 
											bytesum += byteread; 
											fsot.write(buffer, 0, byteread);
										} 
										fsot.flush();
										inStream.close(); 
										fsot.close();
									}else{
										StringBuffer sb = new StringBuffer();
										sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(ghfry).append("\\").append(srcname);
										fnew = new File(sb.toString());
										fnew.createNewFile();
										FileOutputStream fsot = new FileOutputStream(fnew);
										byte[] buffer = new byte[1444]; 
										while ( (byteread = inStream.read(buffer)) != -1) { 
											bytesum += byteread; 
											fsot.write(buffer, 0, byteread);
										} 
										fsot.flush();
										inStream.close(); 
										fsot.close();
									}									
									
									
								}
							}
					//压缩文件夹
					/*ZipUtil zc = new  ZipUtil(filepath+foldername+".zip");  
			        zc.compressExe(filepath+foldername); */
				try {
					final File result = new File(filepath+foldername+".zip");  
					createZipFile(filepath+foldername, result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			        return foldername;
			} 
			
	public void down(String foldername){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String foldernames = "信披自查反馈批量下载文件";
		String filepath = request.getSession().getServletContext().getRealPath("/");
		//下载压缩文件
        try {
	        InputStream fis = new BufferedInputStream(new FileInputStream(filepath+foldername+".zip"));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(foldernames+".zip"));
	        
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
        } catch (Exception ex) {
        	
        }finally{
        	//删除文件夹、压缩文件文件
        	File filedel1 = new File(filepath+foldername);
        	delChange(filedel1);
        	File filedel2 = new File(filepath+foldername+".zip");
        	if(filedel2.exists()){
        		filedel2.delete();
        	}
        }
	}
	public List<HashMap> getSuccessFeedBackList(Long ggid,String khbh,int size) {
		List lables = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables.add("PL"+i);
		}
		Map params_ = new HashMap();
		params_.put(1, ggid);
		params_.put(2, khbh);
		String sql ="SELECT PL1,PL2,PL3,PL4,PL5,PL6,PL7,PL8,PL9,PL10,PL11,PL12,PL13,PL14,PL15,PL16,PL17,PL18,PL19,PL20,PL21,PL22,PL23,PL24,PL25,PL26,PL27,PL28,PL29,PL30,PL31,PL32,PL33,PL34,PL35,PL36,PL37,PL38,PL39,PL40,PL41,PL42,PL43,PL44,PL45,PL46,PL47,PL48,PL49,PL50 FROM BD_VOTE_WJDC WHERE TZGGID=? AND CUSTOMERNO=?";
		List<HashMap> xpzcList = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params_);
		
		List data = new ArrayList();
		if(xpzcList.size()==1){
			HashMap xpzcMap = xpzcList.get(0);
			for (int i = 1; i <= 50; i++) {
				if(xpzcMap.get("PL"+i)!=null){
					data.add(xpzcMap.get("PL"+i));
				}
			}
		}
		int xpzcCount = data.size();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orguserrolrid;
		String userid="";
		if(uc != null){orguserrolrid = uc.get_userModel().getOrgroleid();}else{orguserrolrid = 0l;}
		if(uc!= null){userid = uc.get_userModel().getUserid();}
		Map params = new HashMap();
		params.put(1, khbh);
		params.put(2, ggid);
		String lockstatus = DBUTilNew.getDataStr("LOCKSTATUS","SELECT LOCKSTATUS FROM BD_VOTE_WJDC WHERE CUSTOMERNO= ?  AND TZGGID= ? ", params);
		if(lockstatus == null || lockstatus.equals("")){lockstatus="0";}
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List<HashMap> list=new ArrayList<HashMap>();
		HashMap conditionMap=new HashMap();
		conditionMap.put("TZGGID", ggid);
		conditionMap.put("CUSTOMERNO", khbh);
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String wjdcuuid = config.get("wjdcuuid");
		String wjdcwtuuid = config.get("wjdcwtuuid");
		boolean isSuperMan = this.getIsSuperMan();

		List lables_ = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables_.add("WT"+i);
			lables_.add("AS"+i);
			lables_.add("BZ"+i);
			lables_.add("PL"+i);
			lables_.add("PZ"+i);
			lables_.add("YJXYSFFS"+i);
		}
		lables_.add("CUSTOMERNAME");
		lables_.add("CUSTOMERNO");
		lables_.add("USERNAME");
		lables_.add("USERID");
		lables_.add("TZGGID");
		lables_.add("ID");
		String sql_ = "SELECT * FROM BD_VOTE_WJDC WHERE CUSTOMERNO=? AND TZGGID=?";
		Map params__ = new HashMap();
		params__.put(1, khbh);
		params__.put(2, ggid);
		List<HashMap> list2 = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);//DemAPI.getInstance().getList(wjdcuuid, conditionMap, "ID");
		for (HashMap hashMap : list2) {
			int num=0;
			for(Object o : hashMap.keySet()){
				if(hashMap.get(o)!=null&&!"".equals(hashMap.get(o).toString())&&"WT".equals(o.toString().substring(0, 2))){
					if(Integer.parseInt(o.toString().substring(2))<(xpzcCount+1)){
						num++;
					}
				}
			}
			List keyList=new ArrayList();
			for (int i = 1; i <= num; i++) {
				keyList.add("WT"+i);
			}
			
			for (Object object : keyList) {
				HashMap map=new HashMap();
				String question=hashMap.get(object).toString().replace("\r\n","").replace("\r", "").replace("\n", "");
				params= new HashMap();
				params.put(1, "%"+question+"%");
				String defultAnswer = DBUTilNew.getDataStr("DEFULTANSWER","SELECT DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE TYPE IN ('知悉问题','信披自查') AND QUESTION like ? ", params);
				defultAnswer = defultAnswer==null||defultAnswer.equals("")?"否":defultAnswer;
				String count=object.toString().substring(2, object.toString().length());
				if((defultAnswer!=null&&(hashMap.get("AS"+count)==null||hashMap.get("AS"+count).toString().equals(defultAnswer))) && (hashMap.get("YJXYSFFS"+count)==null||hashMap.get("YJXYSFFS"+count).toString().equals(defultAnswer)) && (hashMap.get("BZ"+count)==null||"".equals(hashMap.get("BZ"+count).toString()))   && (hashMap.get("PL"+count)==null||hashMap.get("PL"+count).toString().equals(defultAnswer)) ){
					size++;
					map.put("CUSTOMERNO", hashMap.get("CUSTOMERNO").toString());
					map.put("CUSTOMERNAME", hashMap.get("CUSTOMERNAME").toString());
					map.put("QUESTION", size+"、"+question);
					map.put("ANSWER", hashMap.get("AS"+count)==null?"":hashMap.get("AS"+count).toString());
					map.put("BZ", hashMap.get("BZ"+count)==null?"":hashMap.get("BZ"+count).toString());
					map.put("PL", hashMap.get("PL"+count)==null?"":hashMap.get("PL"+count).toString());
					map.put("PZ", hashMap.get("PZ"+count)==null?"":hashMap.get("PZ"+count).toString());
					map.put("YJXYSFFS", hashMap.get("YJXYSFFS"+count)==null?"":hashMap.get("YJXYSFFS"+count).toString());
					map.put("ORGUSERROLRID", orguserrolrid);
					map.put("LOCKSTATUS", lockstatus);
					map.put("COUNT", count);
					list.add(map);
				}
			}
		}
		return list;
	}
	
	public List<String> getCustomernoList(String username){
		List<String> list=new ArrayList<String>();
		list=zqbAnnouncementDAO.getCustomernoList(username);
		return list;
	}

	public int getNoFeedBackListSize(String ggid,String zqdm,String sszjj,String ssbm ) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		boolean isSuperMan = this.getIsSuperMan();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		int count=zqbAnnouncementDAO.getNoFeedBackListSize1(ggid,username,isSuperMan,zqdm,sszjj,ssbm);
		return count;
	}

	public boolean communicateSave(String content, String customerno, String ggid,String NOTICEFILE) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("xpgtuuid");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		String username = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUsername();
		HashMap conditionMap = new HashMap();
		conditionMap.put("CUSTOMERNO", customerno);
		conditionMap.put("GGID", ggid);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID, conditionMap, null);
		boolean saveFormData = false;
		if(list.isEmpty()){
			HashMap hashmap = new HashMap();
			Long instanceid = DemAPI
					.getInstance()
					.newInstance(
							demUUID,
							UserContextUtil.getInstance().getCurrentUserContext()._userModel
							.getUserid());
			StringBuffer sb = new StringBuffer();
			hashmap.put("instanceId", instanceid);
			hashmap.put("formid", config.get("xpgtformid"));
			hashmap.put("CONTENT", content);
			hashmap.put("USERNAME", username);
			hashmap.put("GGID", ggid);
			hashmap.put("CUSTOMERNO", customerno);
			hashmap.put("STATUS", "已确认沟通");
			hashmap.put("DATA", sd.format(new Date()));
			hashmap.put("XPGTFILE", NOTICEFILE);
			saveFormData = DemAPI.getInstance().saveFormData(demUUID,
					instanceid, hashmap, false);
			LogUtil.getInstance().addLog(0L, "信披自查反馈确认沟通维护", "新增信披自查反馈确认沟通信息：通知公告ID："+ggid+",客户编号为"+customerno+"的信披自查反馈确认沟通信息。");
		}else{
			HashMap hashMap = list.get(0);
			Long instanceid = Long.parseLong(hashMap.get("INSTANCEID").toString());
			Long dataid = Long.parseLong(hashMap.get("ID").toString());
			hashMap.put("XPGTFILE", NOTICEFILE);
			hashMap.put("CONTENT", content);
			saveFormData=DemAPI.getInstance().updateFormData(demUUID, instanceid, hashMap, dataid, false);
			LogUtil.getInstance().addLog(dataid, "信披自查反馈确认沟通维护", "更新信披自查反馈确认沟通信息：通知公告ID："+ggid+",客户编号为"+customerno+"的信披自查反馈确认沟通信息。");
		}
		return saveFormData;
	}

	public List<HashMap> getCommunicateList(String customerno, Long ggid) {
		String username = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUsername();
		StringBuffer sb = new StringBuffer("select id,username,content,status,ggid,customerno,xpgtfile from Bd_Vote_Xpzcfkgtb where CUSTOMERNO=? and ggid=?");
		List<HashMap> l = new ArrayList<HashMap>();
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			ps.setString(1, customerno);
			ps.setLong(2, ggid);
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap map;
				map = new HashMap();
				BigDecimal id = rs.getBigDecimal("id");
				String uName = rs.getString("username");
				String content = rs.getString("content");
				String status = rs.getString("status");
				BigDecimal ggId = rs.getBigDecimal("ggid");
				String khbh= rs.getString("customerno");
				String noticefile= rs.getString("xpgtfile");
				map.put("GTID", id);
				map.put("USERNAME", uName);
				map.put("CONTENT", content);
				map.put("STATUS", status);
				map.put("CUSTOMERNO", khbh);
				map.put("GGID", ggId);
				map.put("XPGTFILE", noticefile);
				l.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return l;
	}

	public boolean communicateUpdate(String content, String customerno,
			String gtid, String ggid,String NOTICEFILE) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String xpgtuuid=config.get("xpgtuuid");
		String username = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUsername();
		HashMap map = DemAPI.getInstance().getFormData(xpgtuuid, Long.parseLong(gtid));
		HashMap conditionMap = new HashMap();
		conditionMap.put("GGID", ggid);
		conditionMap.put("CUSTOMERNO", customerno);
		conditionMap.put("CONTENT", map.get("CONTENT")==null?"":map.get("CONTENT").toString());
		List<HashMap> list = DemAPI.getInstance().getList(xpgtuuid, conditionMap , null);
		map.put("CONTENT", content);
		map.put("XPGTFILE", NOTICEFILE);
		Long instanceid=Long.parseLong(list.get(0).get("INSTANCEID").toString());
		boolean updateFormData = DemAPI.getInstance().updateFormData(xpgtuuid, instanceid, map, Long.parseLong(gtid), false);
		if(updateFormData){
			LogUtil.getInstance().addLog(Long.parseLong(gtid), "信披自查反馈确认沟通维护", "更新信披自查反馈确认沟通信息：通知公告ID："+ggid+",客户编号为"+customerno+"的信披自查反馈确认沟通信息。");
		}
		return updateFormData;
	}

	public String GogetCustomer() {
		List<HashMap<String, Object>> customer = getCustomer();
		StringBuffer ssb = new StringBuffer();
		for (int i = 0; i < customer.size(); i++) {
			if (i == (customer.size() - 1)) {
				ssb.append(customer.get(i).get("USERID") + "["
						+ customer.get(i).get("USERNAME") + "]");
			} else {
				ssb.append(
						customer.get(i).get("USERID") + "["
								+ customer.get(i).get("USERNAME") + "]")
						.append(",");
			}
		}
		String hfr = ssb.toString();
		return hfr;
	}
	public String getOrgroleid(OrgUser uc) {
		String roleid;
		
		List lables = new ArrayList();
		lables.add("CXDDER");
		String sql = "SELECT DISTINCT SUBSTR(CXDD,0, INSTR(CXDD,'[',1)-1) CXDDER FROM (SELECT KHFZR AS CXDD FROM BD_MDM_KHQXGLB UNION SELECT FHSPR AS CXDD FROM BD_MDM_KHQXGLB) WHERE CXDD IS NOT NULL";
		List<HashMap> cxdderlist = com.iwork.commons.util.DBUtil.getDataList(lables, sql, null);
		
		HashMap check = new HashMap();
		check.put("CXDDER", uc.getUserid());
		if(cxdderlist.contains(check)){
			roleid = "3";
		}else{
			roleid = "0";
		}
		return roleid;
	}
	public List<HashMap> getCxddFeedBackList(Long ggid, String userid,String roleid,String lockstatus) {
		List<HashMap> list=new ArrayList<HashMap>();
		List<HashMap> list2 = zqbAnnouncementDAO.getCxddFeedBackList(ggid,userid);
		for (HashMap hashMap : list2) {
			int num=0;
			for(Object o : hashMap.keySet()){
				if(hashMap.get(o)!=null&&!"".equals(hashMap.get(o).toString())&&"WT".equals(o.toString().substring(0, 2))){
					num++;
				}
			}
			List keyList=new ArrayList();
			for (int i = 1; i <= num; i++) {
				keyList.add("WT"+i);
			}
			int countSize=0;
			for (Object object : keyList) {
				HashMap map=new HashMap();
				String question=hashMap.get(object).toString().replace("\r\n","").replace("\r", "").replace("\n", "");
				String defultAnswer = DBUtil.getString("SELECT DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE QUESTION like '%"+question+"%'", "DEFULTANSWER");
				String count=object.toString().substring(2, object.toString().length());
				if(defultAnswer!=null&&!hashMap.get("AS"+count).toString().equals(defultAnswer)){
						StringBuffer content=new StringBuffer();
						countSize++;
						map.put("USERID", hashMap.get("USERID"));
						map.put("CUSTOMERNAME", hashMap.get("CUSTOMERNAME"));
						map.put("QUESTION", countSize+"、"+question);
						
						map.put("ANSWER", hashMap.get("AS"+count)==null?"":hashMap.get("AS"+count).toString());
						map.put("BZ", hashMap.get("BZ"+count)==null?"":hashMap.get("BZ"+count).toString());
						map.put("PL", hashMap.get("PL"+count)==null?"":hashMap.get("PL"+count).toString());
						map.put("PZ", hashMap.get("PZ"+count)==null?"":hashMap.get("PZ"+count).toString());
						map.put("ORGUSERROLRID", roleid);
						map.put("LOCKSTATUS", lockstatus==null?"0":lockstatus);
						map.put("COUNT", count);
						list.add(map);
				}
			}
		}
		return list;
	}
	public List<HashMap> getCxddSuccessFeedBackList(Long ggid,String userid,int size,String roleid,String lockstatus) {
		List<HashMap> list=new ArrayList<HashMap>();
		List<HashMap> list2 = zqbAnnouncementDAO.getCxddFeedBackList(ggid,userid);//DemAPI.getInstance().getList(wjdcuuid, conditionMap, "ID");
		for (HashMap hashMap : list2) {
			int num=0;
			for(Object o : hashMap.keySet()){
				if(hashMap.get(o)!=null&&!"".equals(hashMap.get(o).toString())&&"WT".equals(o.toString().substring(0, 2))){
					num++;
				}
			}
			List keyList=new ArrayList();
			for (int i = 1; i <= num; i++) {
				keyList.add("WT"+i);
			}
			
			for (Object object : keyList) {
				HashMap map=new HashMap();
				String question=hashMap.get(object).toString().replace("\r\n","").replace("\r", "").replace("\n", "");
				String defultAnswer = DBUtil.getString("SELECT DEFULTANSWER FROM BD_VOTE_WJDCWTB WHERE QUESTION like '%"+question+"%'", "DEFULTANSWER");
				String count=object.toString().substring(2, object.toString().length());
				if(defultAnswer!=null&&hashMap.get("AS"+count).toString().equals(defultAnswer)){
					size++;
					map.put("USERID", hashMap.get("USERID"));
					map.put("CUSTOMERNAME", hashMap.get("CUSTOMERNAME"));
					map.put("QUESTION", size+"、"+question);
					map.put("ANSWER", hashMap.get("AS"+count)==null?"":hashMap.get("AS"+count).toString());
					map.put("BZ", hashMap.get("BZ"+count)==null?"":hashMap.get("BZ"+count).toString());
					map.put("PL", hashMap.get("PL"+count)==null?"":hashMap.get("PL"+count).toString());
					map.put("PZ", hashMap.get("PZ"+count)==null?"":hashMap.get("PZ"+count).toString());
					map.put("ORGUSERROLRID", roleid);
					map.put("LOCKSTATUS", lockstatus==null?"0":lockstatus);
					map.put("COUNT", count);
					list.add(map);
				}
			}
		}
		return list;
	}
	
	public List<HashMap> getCxddList(int pageNumber, int pageSize) {
		List<HashMap> list=new ArrayList<HashMap>();
		list=zqbAnnouncementDAO.getCxddList(pageNumber,pageSize);
		return list;
	}
	public int getCxddListSize() {
		return zqbAnnouncementDAO.getCxddListSize();
	}
	public List<HashMap> getNoFeedBackListCxdd(String ggid,String status) {
		return zqbAnnouncementDAO.getNoFeedBackListCxdd(ggid,status);
	}
	public int getNoFeedBackListCxddSize(String ggid) {
		Map params = new HashMap();
		params.put(1, ggid);
		int count=DBUTilNew.getInt("NUM","SELECT COUNT(*) NUM FROM BD_XP_TZGGB A LEFT JOIN BD_XP_DDGZHFQK B ON A.ID=B.GGID LEFT JOIN BD_VOTE_CXDDRCGZFK C ON B.GGID=C.TZGGID AND B.USERID=C.USERID WHERE 1=1 AND B.STATUS='未回复' AND A.ID= ? ", params);
		return count;
	}
	public void cxddUnlockthis(String lockstatus, String userid,String ggid) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BD_VOTE_CXDDRCGZFK SET LOCKSTATUS=? WHERE USERID=? AND TZGGID=?");
		Connection conn =null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, lockstatus);
			ps.setString(2, userid);
			ps.setString(3, ggid);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}
		
	}

	public void cxddLockthis(String lockstatus, String userid,String ggid) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BD_VOTE_CXDDRCGZFK SET LOCKSTATUS=? WHERE USERID=? AND TZGGID=?");
		Connection conn=null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, lockstatus);
			ps.setString(2, userid);
			ps.setString(3, ggid);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}		
	}

	public List<HashMap> getBondList(int pageNumber, int pageSize,String zqmc,String jc,String zqfzbm,String fxfs,String chfs) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		final int endRow = pageNumber*pageSize;
		final int startRow = (pageNumber - 1) * pageSize;
		String userid = user.getUserid();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqroleid = config.get("zqroleid");
		String dthdepartmenid = config.get("dthdepartmenid");
		boolean flag = user.getOrgroleid()==Long.parseLong(zqroleid);
		String departmentid_departmentname = DBUtil.getString("SELECT OD.ID||'&'||OD.DEPARTMENTNAME DEPARTMENTID_DEPARTMENTNAME FROM ORGDEPARTMENT OD WHERE OD.ID="+dthdepartmenid, "DEPARTMENTID_DEPARTMENTNAME");//Long.toString(user.getDepartmentid());
		List<String> l = new ArrayList<String>();
		l.add(departmentid_departmentname.split("&")[1]);
		List<String> departments=null;
		Connection conn=null;
		try{
		conn = DBUtil.open();
		 departments = getDepartments(conn,departmentid_departmentname.split("&")[0],l);
		}catch(Exception e){
			logger.error(e,e);
		}finally{
		DBUtil.close(conn, null, null);
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT A.*,B.NUM,SU.FXSJ,SU.DFFXGGYTZ,SU.JYSZQDFFX,SU.ZJLSQKHZ,SU.HKSJ FROM  ");

		sb.append("( ");
		sb.append("SELECT * FROM ( ");
		sb.append("SELECT DATA.*,ROWNUM NUMS FROM( ");
		sb.append("SELECT B.*,SUB.ID SUBID FROM ( ");
		sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 ");
		if(!flag){
			sb.append(" AND CREATEUSERID=?");
			params.add(userid);
		}
		sb.append(") B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
		String departmentname = user.getDepartmentname();
		if(!flag){
			if(departments.contains(departmentname)){
				sb.append("UNION ");
				sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 AND FXFS='公开发行') B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			}
			sb.append("UNION ");
			sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 AND FXFS='私募发行' AND ZQFZBM=?) B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			params.add(departmentname);
		}
		sb.append(") B ");
		sb.append("LEFT JOIN BD_XP_FXSJB SUB ON B.ID=SUB.DJZQID WHERE 1=1 AND B.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='登记债券基本信息')  ");
		if(zqmc!=null&&!zqmc.equals("")){
			sb.append("AND ZQMC LIKE ? ");
			params.add("%"+zqmc+"%");
		}
		if(jc!=null&&!jc.equals("")){
			sb.append("AND JC LIKE ? ");
			params.add("%"+jc+"%");
		}
		if(zqfzbm!=null&&!zqfzbm.equals("")){
			sb.append("AND ZQFZBM=? ");
			params.add(zqfzbm);
		}
		if(fxfs!=null&&!fxfs.equals("")){
			sb.append("AND FXFS=? ");
			params.add(fxfs);
		}
		if(chfs!=null&&!chfs.equals("")){
			sb.append("AND CHFS=? ");
			params.add(chfs);
		}
		sb.append("ORDER BY B.ID DESC ");
		sb.append(") DATA ");
		sb.append(") WHERE NUMS > ? AND NUMS <=? ");
		params.add(startRow);
		params.add(endRow);
		sb.append(")A LEFT JOIN ");

		sb.append("( ");
		sb.append("SELECT COUNT(ID) NUM,ID FROM ( ");
		sb.append("SELECT * FROM ( ");
		sb.append("SELECT DATA.*,ROWNUM NUMS FROM( ");
		sb.append("SELECT B.* FROM ( ");
		sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 ");
		if(!flag){
			sb.append(" AND CREATEUSERID=?");
			params.add(userid);
		}
		sb.append(") B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
		if(!flag){
			if(departments.contains(departmentname)){
				sb.append("UNION ");
				sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE FXFS='公开发行') B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			}
			sb.append("UNION ");
			sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE FXFS='私募发行' AND ZQFZBM=?) B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			params.add(departmentname);
		}
		sb.append(") B ");
		sb.append("LEFT JOIN BD_XP_FXSJB SUB ON B.ID=SUB.DJZQID WHERE 1=1 AND B.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='登记债券基本信息')  ");
		if(zqmc!=null&&!zqmc.equals("")){
			sb.append("AND ZQMC LIKE ? ");
			params.add("%"+zqmc+"%");
		}
		if(jc!=null&&!jc.equals("")){
			sb.append("AND JC LIKE ? ");
			params.add("%"+jc+"%");
		}
		if(zqfzbm!=null&&!zqfzbm.equals("")){
			sb.append("AND ZQFZBM=? ");
			params.add(zqfzbm);
		}
		if(fxfs!=null&&!fxfs.equals("")){
			sb.append("AND FXFS=? ");
			params.add(fxfs);
		}
		if(chfs!=null&&!chfs.equals("")){
			sb.append("AND CHFS=? ");
			params.add(chfs);
		}
		sb.append("ORDER BY B.ID DESC ");
		sb.append(") DATA ");
		sb.append(") WHERE NUMS > ? AND NUMS <=? ");

		sb.append(") ");
		sb.append("GROUP BY ID ");

		sb.append(") B ON A.ID=B.ID  ");

		sb.append("LEFT JOIN BD_XP_FXSJB SU ON A.SUBID=SU.ID");
		
		String sql = sb.toString();
		return zqbAnnouncementDAO.getBondList(sql,params);
	}
	
	public int getBondListSize(String zqmc,String jc,String zqfzbm,String fxfs,String chfs){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String userid = user.getUserid();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqroleid = config.get("zqroleid");
		String dthdepartmenid = config.get("dthdepartmenid");
		boolean flag = user.getOrgroleid()==Long.parseLong(zqroleid);
		String departmentid_departmentname = DBUtil.getString("SELECT OD.ID||'&'||OD.DEPARTMENTNAME DEPARTMENTID_DEPARTMENTNAME FROM ORGDEPARTMENT OD WHERE OD.ID="+dthdepartmenid, "DEPARTMENTID_DEPARTMENTNAME");//Long.toString(user.getDepartmentid());
		List<String> l = new ArrayList<String>();
		l.add(departmentid_departmentname.split("&")[1]);
		Connection conn=null;
		List<String> departments=null;
		try{
		conn = DBUtil.open();
		departments = getDepartments(conn,departmentid_departmentname.split("&")[0],l);
		}catch(Exception e){
			logger.error(e,e);
		}finally{
		DBUtil.close(conn, null, null);
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT A.*,B.NUM,SU.FXSJ,SU.DFFXGGYTZ,SU.JYSZQDFFX,SU.ZJLSQKHZ,SU.HKSJ FROM  ");

		sb.append("( ");
		sb.append("SELECT * FROM ( ");
		sb.append("SELECT DATA.*,ROWNUM NUMS FROM( ");
		sb.append("SELECT B.*,SUB.ID SUBID FROM ( ");
		sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 ");
		if(!flag){
			sb.append(" AND CREATEUSERID=?");
			params.add(userid);
		}
		sb.append(") B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
		String departmentname = user.getDepartmentname();
		if(!flag){
			if(departments.contains(departmentname)){
				sb.append("UNION ");
				sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 AND FXFS='公开发行') B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			}
			sb.append("UNION ");
			sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 AND FXFS='私募发行' AND ZQFZBM=?) B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			params.add(departmentname);
		}
		sb.append(") B ");
		sb.append("LEFT JOIN BD_XP_FXSJB SUB ON B.ID=SUB.DJZQID WHERE 1=1 AND B.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='登记债券基本信息')  ");
		if(zqmc!=null&&!zqmc.equals("")){
			sb.append("AND ZQMC LIKE ? ");
			params.add("%"+zqmc+"%");
		}
		if(jc!=null&&!jc.equals("")){
			sb.append("AND JC LIKE ? ");
			params.add("%"+jc+"%");
		}
		if(zqfzbm!=null&&!zqfzbm.equals("")){
			sb.append("AND ZQFZBM=? ");
			params.add(zqfzbm);
		}
		if(fxfs!=null&&!fxfs.equals("")){
			sb.append("AND FXFS=? ");
			params.add(fxfs);
		}
		if(chfs!=null&&!chfs.equals("")){
			sb.append("AND CHFS=? ");
			params.add(chfs);
		}
		sb.append("ORDER BY B.ID DESC ");
		sb.append(") DATA ");
		sb.append(") ");
		sb.append(")A LEFT JOIN ");

		sb.append("( ");
		sb.append("SELECT COUNT(ID) NUM,ID FROM ( ");
		sb.append("SELECT * FROM ( ");
		sb.append("SELECT DATA.*,ROWNUM NUMS FROM( ");
		sb.append("SELECT B.* FROM ( ");
		sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 ");
		if(!flag){
			sb.append(" AND CREATEUSERID=?");
			params.add(userid);
		}
		sb.append(") B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
		if(!flag){
			if(departments.contains(departmentname)){
				sb.append("UNION ");
				sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE FXFS='公开发行') B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			}
			sb.append("UNION ");
			sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE FXFS='私募发行' AND ZQFZBM=?) B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			params.add(departmentname);
		}
		sb.append(") B ");
		sb.append("LEFT JOIN BD_XP_FXSJB SUB ON B.ID=SUB.DJZQID WHERE 1=1 AND B.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='登记债券基本信息')  ");
		if(zqmc!=null&&!zqmc.equals("")){
			sb.append("AND ZQMC LIKE ? ");
			params.add("%"+zqmc+"%");
		}
		if(jc!=null&&!jc.equals("")){
			sb.append("AND JC LIKE ? ");
			params.add("%"+jc+"%");
		}
		if(zqfzbm!=null&&!zqfzbm.equals("")){
			sb.append("AND ZQFZBM=? ");
			params.add(zqfzbm);
		}
		if(fxfs!=null&&!fxfs.equals("")){
			sb.append("AND FXFS=? ");
			params.add(fxfs);
		}
		if(chfs!=null&&!chfs.equals("")){
			sb.append("AND CHFS=? ");
			params.add(chfs);
		}
		sb.append("ORDER BY B.ID DESC ");
		sb.append(") DATA ");
		sb.append(") ");

		sb.append(") ");
		sb.append("GROUP BY ID ");

		sb.append(") B ON A.ID=B.ID  ");

		sb.append("LEFT JOIN BD_XP_FXSJB SU ON A.SUBID=SU.ID");
		String sql = sb.toString();
		return zqbAnnouncementDAO.getBondListSize(sql,params).size();
	}
	
	public void zqDel(Long instanceId){
		String demUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '登记债券基本信息'", "UUID");
		HashMap data = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		data.put("DELSTATUS", 1);
		data.put("CREATEDATE", data.get("CREATEDATE").toString().substring(0, data.get("CREATEDATE").toString().length()-2));
		data.put("ZQQX", data.get("ZQQX").toString().substring(0, data.get("ZQQX").toString().length()-2));
		String djzqid = data.get("ID").toString();
		DemAPI.getInstance().updateFormData(demUUID, instanceId, data, Long.parseLong(djzqid), false);
		
		List lables = new ArrayList();
		lables.add("RCID");
		
		Map<Integer,String> params = new HashMap<Integer, String>();
		params.put(1, djzqid);
		
		List<HashMap> rcData = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT RCID FROM BD_XP_FXSJB WHERE DJZQID = ?", params);
		SchCalendarService schCalendarService = (SchCalendarService)SpringBeanUtil.getBean("schCalendarService");
		for (HashMap hashMap : rcData) {
			IworkSchCalendar model = schCalendarService.getModel(hashMap.get("RCID").toString());
			if(model!=null){
				schCalendarService.delete(model);
			}
		}
		ResponseUtil.write("success");
	}
	
	private List<String> getDepartments(Connection conn,String departmentid,List<String> l){
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer("");
		sb.append("SELECT OD.ID,OD.DEPARTMENTNAME FROM ORGDEPARTMENT OD WHERE OD.PARENTDEPARTMENTID IN (");
		String[] ids = departmentid.split(",");
		for (int i = 0; i < ids.length; i++) {
			if(i==(ids.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
		}
		sb.append(")");
		int i = 0;
		try {
			ps = conn.prepareStatement(sb.toString());
			for (int j = 0; j < ids.length; j++) {
				ps.setString(j, ids[j]);
			}
			rs = ps.executeQuery();
			departmentid = "";
			while(rs.next()){
				if(i==0){
					departmentid+=rs.getString("ID");
				}else{
					departmentid+=(","+rs.getString("ID"));
				}
				i++;
				l.add(rs.getString("DEPARTMENTNAME"));
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		if(i==0){
			return l;
		}else{
			return getDepartments(conn,departmentid,l);
		}
	}
	
	public List<HashMap> getBondList(String startdate,String enddate) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String userid = user.getUserid();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqroleid = config.get("zqroleid");
		String dthdepartmenid = config.get("dthdepartmenid");
		boolean flag = user.getOrgroleid()==Long.parseLong(zqroleid);
		String departmentid_departmentname = DBUtil.getString("SELECT OD.ID||'&'||OD.DEPARTMENTNAME DEPARTMENTID_DEPARTMENTNAME FROM ORGDEPARTMENT OD WHERE OD.ID="+dthdepartmenid, "DEPARTMENTID_DEPARTMENTNAME");//Long.toString(user.getDepartmentid());
		List<String> l = new ArrayList<String>();
		l.add(departmentid_departmentname.split("&")[1]);
		Connection conn=null;
		List<String> departments=null;
		try{
		conn = DBUtil.open();
		departments = getDepartments(conn,departmentid_departmentname.split("&")[0],l);
		}catch(Exception e){
			logger.error(e,e);
		}finally{
		DBUtil.close(conn, null, null);
		}
		
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT A.*,B.NUM,SU.FXSJ,SU.DFFXGGYTZ,SU.JYSZQDFFX,SU.ZJLSQKHZ,SU.HKSJ FROM  ");

		sb.append("( ");
		sb.append("SELECT * FROM ( ");
		sb.append("SELECT DATA.*,ROWNUM NUMS FROM( ");
		sb.append("SELECT B.*,SUB.ID SUBID FROM ( ");
		sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 ");
		if(!flag){
			sb.append(" AND CREATEUSERID=?");
			params.add(userid);
		}
		sb.append(") B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
		String departmentname = user.getDepartmentname();
		if(!flag){
			if(departments.contains(departmentname)){
				sb.append("UNION ");
				sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 AND FXFS='公开发行') B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			}
			sb.append("UNION ");
			sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 AND DELSTATUS=0 AND FXFS='私募发行' AND ZQFZBM=?) B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			params.add(departmentname);
		}
		sb.append(") B ");
		sb.append("LEFT JOIN BD_XP_FXSJB SUB ON B.ID=SUB.DJZQID WHERE 1=1 AND B.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='登记债券基本信息')  ");
		
		sb.append("ORDER BY B.ID DESC ");
		sb.append(") DATA ");
		sb.append(") ");
		sb.append(")A LEFT JOIN ");

		sb.append("( ");
		sb.append("SELECT COUNT(ID) NUM,ID FROM ( ");
		sb.append("SELECT * FROM ( ");
		sb.append("SELECT DATA.*,ROWNUM NUMS FROM( ");
		sb.append("SELECT B.* FROM ( ");
		sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE 1=1 ");
		if(!flag){
			sb.append(" AND CREATEUSERID=?");
			params.add(userid);
		}
		sb.append(") B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
		if(!flag){
			if(departments.contains(departmentname)){
				sb.append("UNION ");
				sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE FXFS='公开发行') B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			}
			sb.append("UNION ");
			sb.append("SELECT B.ID,B.ZQMC,B.JC,B.FXRMC,B.FXRSSD,B.FXSC,B.FXFS,B.ZQFZBM,B.ZQZBR,B.ZQQX,B.ZQLLSM,B.FXJG,B.CHFS,B.FXSM,B.XYJB,B.ZQZL,B.CREATEUSER,B.CREATEUSERID,B.CREATEDATE,S.INSTANCEID SUBID,S.FORMID FROM (SELECT * FROM BD_XP_DJZQJBXXB WHERE FXFS='私募发行' AND ZQFZBM=?) B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID  ");
			params.add(departmentname);
		}
		sb.append(") B ");
		sb.append("LEFT JOIN BD_XP_FXSJB SUB ON B.ID=SUB.DJZQID WHERE 1=1 AND B.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='登记债券基本信息')  ");
		
		sb.append("ORDER BY B.ID DESC ");
		sb.append(") DATA ");
		sb.append(") ");

		sb.append(") ");
		sb.append("GROUP BY ID ");

		sb.append(") B ON A.ID=B.ID  ");

		sb.append("LEFT JOIN BD_XP_FXSJB SU ON A.SUBID=SU.ID WHERE 1=1 ");
		if((startdate!=null&&!startdate.equals(""))||(enddate!=null&&!enddate.equals(""))){
			sb.append("AND A.ID IN (SELECT DJZQID FROM BD_XP_FXSJB WHERE 1=1 ");
			if(startdate!=null&&!startdate.equals("")){
				sb.append("AND FXSJ >= TO_DATE(?,'yyyy-MM-dd') ");
				params.add(startdate);
			}
			if(enddate!=null&&!enddate.equals("")){
				sb.append("AND FXSJ <= TO_DATE(?,'yyyy-MM-dd') ");
				params.add(enddate);
			}
			sb.append(")");
		}
		String sql = sb.toString();
		return zqbAnnouncementDAO.getBondList(sql,params);
	}
	/**
	 * 导出当前登录人能够查看的债券信息
	 * @param response
	 */
	public void zqExp(HttpServletResponse response,String startdate,String enddate) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("债券信息列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style4.setFont(headfont);
		HSSFFont headfont5 = wb.createFont();
		headfont5.setFontName("宋体");
		headfont5.setFontHeightInPoints((short) 11);// 字体大小
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setWrapText(true);
		style5.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style5.setFont(headfont5);
		style5.setFillBackgroundColor(HSSFColor.YELLOW.index);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int count = 0;
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell
		cell1 = row1.createCell((short) z++);cell1.setCellValue("序号");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("债券简称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("发行市场");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("债券负责部门");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("偿还方式");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("兑付付息公告与通知");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("交易所证券兑付付息确认函/确认表");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("关于企业债付息兑付资金落实情况回执");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("划款时间");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("付息时间");cell1.setCellStyle(style4);
		list=getBondList(startdate,enddate);
		List<HashMap> l = new ArrayList<HashMap>();
		String numc = "1";
		for (HashMap map : list) {
			z=0;
			row1 = sheet.createRow((int) m++);
			String num = map.get("NUM").toString();
			if(num.equals("1")||!num.equals(numc)){
				numc=num;
				count++;
			}
			HSSFCell 
			cell2 = row1.createCell((short) z++);cell2.setCellValue(count);cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQMC") == null ? "" : map.get("ZQMC").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("FXSC") == null ? "" : map.get("FXSC").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQFZBM") == null ? "" : map.get("ZQFZBM").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CHFS") == null ? "" : map.get("CHFS").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("DFFXGGYTZ") == null ? "" : map.get("DFFXGGYTZ").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("JYSZQDFFX") == null ? "" : map.get("JYSZQDFFX").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZJLSQKHZ") == null ? "" : map.get("ZJLSQKHZ").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("HKSJ") == null ? "" : map.get("HKSJ").toString());cell2.setCellStyle(style5);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("FXSJ") == null ? "" : map.get("FXSJ").toString());cell2.setCellStyle(style5);
			HashMap data = new HashMap();
			data.put(map.get("NUM"), map.get("ID"));
			if(!l.contains(data)){
				l.add(data);
			}
		}
		sheet.setColumnWidth(0, 2500);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 8500);
		sheet.setColumnWidth(4, 7500);
		sheet.setColumnWidth(5, 6500);
		sheet.setColumnWidth(6, 9500);
		sheet.setColumnWidth(7, 10500);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		int hbRow = 1;
		for (int i = 0; i < l.size(); i++) {
			int size = Integer.parseInt(l.get(i).toString().substring(l.get(i).toString().indexOf("{")+1, l.get(i).toString().indexOf("=")));
			for(int j = 0;j < 5; j++){
				sheet.addMergedRegion(new Region(hbRow, (short) j, hbRow+size-1, (short) j));
			}
			hbRow+=size;
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + URLEncoder.encode(new StringBuilder("债券信息列表").append(".xls").toString(), "UTF-8");

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
	
	public List<HashMap> getBondSubData(String id) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT B.CHZQBJDYBL,B.FXSJ,S.INSTANCEID,B.DFFXGGYTZ,B.JYSZQDFFX,B.ZJLSQKHZ,B.HKSJ FROM BD_XP_FXSJB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE 1=1 AND S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='付息时间表单') AND DJZQID=? ORDER BY B.ID DESC");
		params.add(id);
		String sql = sb.toString();
		return zqbAnnouncementDAO.getBondSubData(sql,params);
	}
	
	private static Date getCounTDown(String content,int daynum){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date fxsj=null;
		try {
			fxsj = sdf.parse(content);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		cal.setTime(fxsj);
		
		//获得所有的周六日-------------------------------A
		CalendarUtil cu = new CalendarUtil();
		int year = Integer.parseInt(content.substring(0, 4));
		List<String> allweekdays = cu.getWeekDayByYear(year);
		List<String> allweekdays2 = cu.getWeekDayByYear(year-1);
		allweekdays.addAll(allweekdays2);
		List<Date> allweeks = new ArrayList<Date>();
		try {
			for (String date : allweekdays) {
				allweeks.add(sdf.parse(date));
			}
		} catch (ParseException e) {
			logger.error(e,e);
		}
		//---------------------------------------------A
		
		//管理员设置的休息日-----------------------------B
		List lables = new ArrayList();
		lables.add("JYR");
		List<HashMap> restday = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT TO_CHAR(JYR,'yyyy-MM-dd') JYR FROM BD_XP_JYR", null);
		try {
			for (HashMap data : restday) {
				allweeks.add(sdf.parse(data.get("JYR").toString()));
			}
		} catch (ParseException e) {
			logger.error(e,e);
		}
		//---------------------------------------------B
		
		Date data1=new Date();
		int count=0;
		while(true){
			cal.add(Calendar.DAY_OF_YEAR, -1);
			if(!allweeks.contains(cal.getTime())){
				count++;
			}
			if(count==daynum){
				data1 = cal.getTime();
				break;
			}
		}
		return data1;
	}
	/**
	 * 添加一条日程,并返回这条日程ID
	 * @param fxsj
	 * @param hksj
	 * @param zqinsid
	 * @param zjlsqkhz
	 * @param jyszqdffx
	 * @param dffxggytz
	 * @param rcid
	 * @param instanceId
	 */
	public void getMaxRcids(String fxsj,String hksj,String zqinsid,String zjlsqkhz,String jyszqdffx,String dffxggytz,String rcid,Long instanceId,Long pInstanceId) {
		String userid = DemAPI.getInstance().getFromData(pInstanceId).get("CREATEUSERID").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date fxsj_d=null;
		Date hksj_d=null;
		try {
			fxsj_d = sdf.parse(fxsj);
			hksj_d = getCounTDown(dffxggytz,3);//sdf.parse(hksj);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		
		SchCalendarService schCalendarService = (SchCalendarService)SpringBeanUtil.getBean("schCalendarService");
		//债券简称【付息提醒-2017-07-08】+【兑付付息公告与通知-2017-07-02】+【XXXX-XXXX】+【XXXX-XXXX】。。。
		StringBuffer title = new StringBuffer();
		Long zqinsid_ = Long.parseLong(zqinsid);
		HashMap zqdata = DemAPI.getInstance().getFromData(zqinsid_, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String zqmc = zqdata.get("ZQMC").toString();
		title.append(zqmc).append("【兑付付息公告与通知-").append(dffxggytz).append("】【交易所证券兑付付息确认时间-").append(jyszqdffx).append("】【企业债付息兑付资金落实情况回执-").append(zjlsqkhz).append("】【划款时间-").append(hksj).append("】").append("【付息时间-").append(fxsj).append("】");
		
		IworkSchCalendar model = new IworkSchCalendar();
		model.setUserid(userid);
		model.setTitle(title.toString());
		model.setStartdate(hksj_d);
		model.setEnddate(fxsj_d);
		model.setStarttime("10:00");
		model.setEndtime("23:59");
		model.setIsallday(1l);
		model.setIsalert(0l);
		model.setAlerttime("15");
		Long instanceid = instanceId;
		if(instanceid==0l){
			schCalendarService.save(model);
			String maxid = DBUtil.getString("SELECT MAX(ID) MAXID FROM IWORK_SCH_CALENDAR", "MAXID");
			ResponseUtil.write(maxid);
		}else{
			IworkSchCalendar model_ = schCalendarService.getModel(rcid);
			if(model_==null){
				schCalendarService.save(model);
				String maxid = DBUtil.getString("SELECT MAX(ID) MAXID FROM IWORK_SCH_CALENDAR", "MAXID");
				ResponseUtil.write(maxid);
			}else{
				model_.setTitle(title.toString());
				model_.setStartdate(hksj_d);
				model_.setEnddate(fxsj_d);
				schCalendarService.update(model_);
				ResponseUtil.write(rcid);
			}
		}
	}
	
	public void getExtraFields(String content){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date fxsj=null;
		try {
			fxsj = sdf.parse(content);
		} catch (ParseException e) {
			logger.error(e,e);
		}
		cal.setTime(fxsj);
		
		//获得所有的周六日-------------------------------A
		CalendarUtil cu = new CalendarUtil();
		int year = Integer.parseInt(content.substring(0, 4));
		List<String> allweekdays = cu.getWeekDayByYear(year);
		List<String> allweekdays2 = cu.getWeekDayByYear(year-1);
		allweekdays.addAll(allweekdays2);
		List<Date> allweeks = new ArrayList<Date>();
		try {
			for (String date : allweekdays) {
				allweeks.add(sdf.parse(date));
			}
		} catch (ParseException e) {
			logger.error(e,e);
		}
		//---------------------------------------------A
		
		//管理员设置的休息日-----------------------------B
		List lables = new ArrayList();
		lables.add("JYR");
		List<HashMap> restday = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT TO_CHAR(JYR,'yyyy-MM-dd') JYR FROM BD_XP_JYR", null);
		try {
			for (HashMap data : restday) {
				allweeks.add(sdf.parse(data.get("JYR").toString()));
			}
		} catch (ParseException e) {
			logger.error(e,e);
		}
		//---------------------------------------------B
		
		String data1="";
		String data2="";
		int count=0;
		while(true){
			cal.add(Calendar.DAY_OF_YEAR, -1);
			if(!allweeks.contains(cal.getTime())){
				count++;
			}
			if(count==3&&data1.equals("")){
				data1 = sdf.format(cal.getTime());
			}
			if(count==9&&data2.equals("")){
				data2 = sdf.format(cal.getTime());
				break;
			}
		}
		if(!allweeks.contains(fxsj)){
			ResponseUtil.write(data1+","+data2);
		}else{
			ResponseUtil.write("null");
		}
	}
	
	public String isDudaoCustomer(String zqdm, String zqjc) {
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		String khbh="";
		String khmc="";
		String zqdmtest="";
		Map params = new HashMap();
		int n=1;
		 if("3".equals(roleid)){
			 khbh=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend1();
			 khmc=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend2();
			 return "success";
		 }else if("5".equals(roleid)||"9".equals(roleid)){
			 if("".equals(zqdm)||zqdm==null){
					zqdmtest="";
				}else{
					zqdmtest=" and zqdm= ? ";
					params.put(n, zqdm);
					n++;
			}
			 if(!"".equals(zqjc)&&zqjc!=null){
				 	params.put(n, zqjc);
					n++;
					khmc = DBUTilNew.getDataStr("CUSTOMERNAME","select CUSTOMERNAME from bd_zqb_kh_base where zqjc= ? "+zqdmtest+"",params);
					khbh = DBUTilNew.getDataStr("CUSTOMERNO","select CUSTOMERNO from bd_zqb_kh_base where zqjc= ? "+zqdmtest+"",params);
			}else if(!"".equals(zqdmtest)){
				khmc = DBUTilNew.getDataStr("CUSTOMERNAME","select CUSTOMERNAME from bd_zqb_kh_base where 1=1 "+zqdmtest+"",params);
				khbh = DBUTilNew.getDataStr("CUSTOMERNO","select CUSTOMERNO from bd_zqb_kh_base where 1=1 "+zqdmtest+"",params);
			}
			return "success";
		 }
		 else{
			if("".equals(zqdm)||zqdm==null){
				zqdmtest="";
			}else{
				zqdmtest=" and zqdm= ? ";
				params.put(n, zqdm);
				n++;
			}
			if(!"".equals(zqjc)&&zqjc!=null){
				boolean isCustomer = this.isCustomerJc(zqjc);
				if(isCustomer){
					params.put(n, zqjc);
					n++;
					khmc = DBUTilNew.getDataStr("CUSTOMERNAME","select CUSTOMERNAME from bd_zqb_kh_base where zqjc= ? "+zqdmtest+"",params);
					khbh = DBUTilNew.getDataStr("CUSTOMERNO","select CUSTOMERNO from bd_zqb_kh_base where zqjc= ? "+zqdmtest+"",params);
					return "success";
				}else{
					return "error";
				}
			}else if(!"".equals(zqdmtest)){
				boolean isCustomer = this.isCustomerDm(zqdm);
				if(isCustomer){
					khmc = DBUTilNew.getDataStr("CUSTOMERNAME","select CUSTOMERNAME from bd_zqb_kh_base where 1=1 "+zqdmtest+"",params);
					khbh =DBUTilNew.getDataStr("CUSTOMERNO","select CUSTOMERNO from bd_zqb_kh_base where 1=1 "+zqdmtest+"",params);
					return "success";
				}else{
					return "error";
				}
			}
			return "success";
		 }
	}

	public String zqbFzgsCwbb(String khbh, String id) {
		return zqbAnnouncementDAO.zqbFzgsCwbb(khbh,id);
	}

	public String zqbGlfGetThisData(String gsmc, String khbh) {
		return zqbAnnouncementDAO.zqbGlfGetThisData(gsmc,khbh);
	}
	public String zqbFzgsGetThisData(String gsmc, String khbh) {
		return zqbAnnouncementDAO.zqbFzgsGetThisData(gsmc,khbh);
	}
	public List<HashMap> getDustbinList(String khbh, int pageSize,
			int pageNumber, String noticename, String noticetype,
			String startdate, String enddate,String bzlx) {
		return zqbAnnouncementDAO.getDustbinList(khbh,pageSize,pageNumber,noticename,noticetype,startdate,enddate,bzlx);
	}

	public int getDustbinListSize(String khbh, String noticename, String noticetype, String startdate, String enddate,String bzlx) {
		return zqbAnnouncementDAO.getDustbinListSize(khbh, noticename,noticetype, startdate,enddate,bzlx);
	}
	
	public boolean thoroughDelete(Long instanceid){
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		String customername = fromData.get("CUSTOMERNAME")==null?fromData.get("KHMC")==null?"":fromData.get("KHMC").toString():fromData.get("CUSTOMERNAME").toString();
		String noticename = fromData.get("NOTICENAME").toString();
		Long dataId=Long.parseLong(fromData.get("ID").toString());
		String yxid = fromData.get("YXID").toString();
		Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(yxid));
		try {
			//	boolean removeProcessInstance= false;
			DBUtil.executeUpdate("delete from process_hi_taskinst t where t.proc_inst_id_='"+ yxid + "'");
			DBUtil.executeUpdate("delete from process_ru_task t where t.proc_inst_id_='"+ yxid + "'");

            DBUtil.executeUpdate("delete from process_hi_actinst t where t.proc_inst_id_='"+ yxid + "'");
            DBUtil.executeUpdate("delete from process_hi_procinst t where t.proc_inst_id_='"+ yxid + "'");
            DBUtil.executeUpdate("delete from process_ru_opinion t where t.instanceid='"+ yxid + "'");
            DBUtil.executeUpdate("delete from process_ru_cc t where t.instanceid='"+ yxid + "'");
            DBUtil.executeUpdate("delete from process_ru_forward t where t.instanceid='"+ yxid + "'");
		} catch (Exception e) {
			logger.info(e);
		}
		/*if(newTaskId!=null){
			String taskId = newTaskId.getId();
			String owner = newTaskId.getOwner();
			//ProcessAPI.getInstance().setTaskAssignee(taskId, owner);
			removeProcessInstance = ProcessAPI.getInstance().removeProcessInstance(taskId, owner);
		}else{
			removeProcessInstance = true;
		}*/
		//公告流程数据删除
		boolean removeFormData2 = ProcessAPI.getInstance().removeFormData(Long.parseLong(yxid));
		//公告垃圾箱物理数据删除
		boolean removeFormData = DemAPI.getInstance().removeFormData(instanceid);
		if(removeFormData&&/*(removeProcessInstance||newTaskId.getName().equals("驳回"))&&*/removeFormData2){
			try {
				LogUtil.getInstance().addLog(dataId, "公告垃圾箱信息维护", "公告彻底删除："+customername+"中的"+noticename+"彻底删除。");
			} catch (Exception e) {}
			return true;
		}else{
			return false;
		}
	}
	
	public String khxxglRsx(String startdate,String enddate){
		Date day=new Date();    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		if(df.format(day).equals(startdate) && df.format(day).equals(enddate)){
			return "0";
		}else if(df.format(day).equals(startdate)){
			return "1";
		}else if(df.format(day).equals(enddate)){
			return "2";
		}else{
			return "3";
		}
	}
	public boolean restore(Long instanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		String customername = fromData.get("CUSTOMERNAME")==null?fromData.get("KHMC")==null?"":fromData.get("KHMC").toString():fromData.get("CUSTOMERNAME").toString();
		String noticename = fromData.get("NOTICENAME").toString();
		Long dataId = Long.parseLong(fromData.get("ID").toString());
		String actDefId = fromData.get("LCBH").toString();
		String noticeno = fromData.get("NOTICENO").toString();
		Long instanceId = Long.parseLong(fromData.get("YXID").toString());
		Long ljInstanceid = DemAPI.getInstance().newInstance(Announcement_UUID,UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid());
		if(!fromData.get("SPZT").toString().equals("审批通过")){
			fromData.remove("NOTICENO"); 
		}
		boolean saveFormData = DemAPI.getInstance().saveFormData(Announcement_UUID, ljInstanceid, fromData, false);
		HashMap smjmap=new HashMap();
		smjmap.put("GGINS", instanceid);
		List<HashMap> smjList=DemAPI.getInstance().getList(GGSMJ_UUID, smjmap, null);
		if(smjList.size()>0){
			Long smjInstanceId = Long.parseLong(smjList.get(0).get("INSTANCEID").toString());
			HashMap smjData = DemAPI.getInstance().getFromData(smjInstanceId);
			smjData.put("GGINS", ljInstanceid);
			Long smjId = Long.parseLong(smjData.get("ID").toString());
			DemAPI.getInstance().updateFormData(GGSMJ_UUID, smjInstanceId, smjData, smjId, false);
		}
		Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
		if(newTaskId!=null){
			String assignee = fromData.get("LCWTR").toString();
			String taskId = newTaskId.getId();
			ProcessAPI.getInstance().setTaskAssignee(taskId, assignee);
		}
		HashMap lcData = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//lcData.put("NOTICENO", noticeno);
		Long lcId = Long.parseLong(lcData.get("ID").toString());
		boolean saveFormData2 =ProcessAPI.getInstance().updateFormData(actDefId, instanceId, lcData, lcId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		boolean removeFormData = DemAPI.getInstance().removeFormData(instanceid);
		if(saveFormData&&saveFormData2&&removeFormData){
			LogUtil.getInstance().addLog(dataId, "公告垃圾箱信息维护", "公告恢复："+customername+"中的"+noticename+"被恢复。");
			return true;
		}else{
			return false;
		}
	}
	
	public String noticeOne() {
		List<HashMap<String, Object>> customer = getCustomer();
		StringBuffer ssb = new StringBuffer();
		for (int i = 0; i < customer.size(); i++) {
			if (i == (customer.size() - 1)) {
				ssb.append(customer.get(i).get("USERID") + "["
						+ customer.get(i).get("USERNAME") + "]");
			} else {
				ssb.append(
						customer.get(i).get("USERID") + "["
								+ customer.get(i).get("USERNAME") + "]")
						.append(",");
			}
		}
		String hfr=ssb.toString();
		HashMap returnMap = new HashMap();
		returnMap.put("HFR", hfr);
		returnMap.put("flag", true ? "success" : "error");
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public String noticeAll() {
		List<HashMap<String, Object>> customer = getAllCustomer();
		StringBuffer ssb = new StringBuffer();
		for (int i = 0; i < customer.size(); i++) {
			if (i == (customer.size() - 1)) {
				ssb.append(customer.get(i).get("USERID") + "["
						+ customer.get(i).get("USERNAME") + "]");
			} else {
				ssb.append(
						customer.get(i).get("USERID") + "["
								+ customer.get(i).get("USERNAME") + "]")
						.append(",");
			}
		}
		String hfr = ssb.toString();
		HashMap returnMap = new HashMap();
		returnMap.put("HFR", hfr);
		returnMap.put("flag", true ? "success" : "error");
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public void expWordDefault(HttpServletResponse response,String dqrq) {
		List<HashMap> list = new ArrayList<HashMap>();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer")==null?"":config.get("zqServer").toString();
		StringBuffer sql = new StringBuffer("SELECT ROWNUM ||'、'||QUESTION QUESTION,ANSWER,PL,BZ,PZ,YJXYSFFS FROM (SELECT QUESTION,BVW.DEFULTANSWER ANSWER,BVW.DEFULTANSWER PL,'' BZ,'' PZ,'' YJXYSFFS FROM BD_VOTE_WJDCWTB BVW WHERE STATUS='显示' AND TYPE='信披自查' ORDER BY ID) A");
		StringBuffer sql_ = new StringBuffer("SELECT ROWNUM ||'、'||QUESTION QUESTION,ANSWER,PL,BZ,PZ,YJXYSFFS FROM (SELECT QUESTION,BVW.DEFULTANSWER ANSWER,BVW.DEFULTANSWER PL,'' BZ,'' PZ,'' YJXYSFFS FROM BD_VOTE_WJDCWTB BVW WHERE STATUS='显示' AND TYPE='知悉问题' ORDER BY ID) A");
		StringBuffer sb=new StringBuffer();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String khbh = uc.get_userModel().getExtend1();
		HashMap khMap=new HashMap();
		khMap.put("CUSTOMERNO", khbh);
		List<HashMap> khlist = DemAPI.getInstance().getList("a243efd832bf406b9caeaec5df082e28", khMap, "ID");
		String zq_dm = khlist.get(0).get("ZQDM")==null?"":khlist.get(0).get("ZQDM").toString();
		String zq_jc = khlist.get(0).get("ZQJC")==null?"":khlist.get(0).get("ZQJC").toString();
		String dm = khlist.get(0).get("EXTEND1")==null?"":khlist.get(0).get("EXTEND1").toString();
		sb.append("<!DOCTYPE html>"
				+ "<html>"
				+ "<head>"
				+ "<title></title>"
				+ "<style>"
				+ "body{font-family:'宋体';word-break:break-all;}"
				+ ".header {width:700px;font-size:14px;height:22px;}"
				+ ".WT {width:700px;font-size:12px;height:20px;word-wrap:break-word;word-break:break-all;width:600px;}"
				+ ".DA {width:700px;font-size:12px;height:18px;width:200px;}"
				+ "</style>"
				+ "</head>"
				+ "<body>	"
				+ "<div  align='center'>	"
				+ "<table cellspacing='0' cellpadding='0'>");
		
		sb.append("<tr style='width:600px;'><td style='width:200px;'></td><td style='width:200px;'></td><td style='width:200px;'></td></tr>");
		
		if(zqServer!=null&&"xnzq".equals(zqServer)){
			sb.append("<tr><td colspan='3' class='WT'><b>挂牌公司简称：").append(zq_jc).append("</b></td></tr>");
			sb.append("<tr><td colspan='3' class='WT'><b>挂牌公司代码：").append(zq_dm).append("</b></td></tr>");
			sb.append("<tr><td colspan='3' class='WT'><b>董秘或信息披露负责人：").append(dm).append("</b></td></tr>");
			
			sb.append("<tr><td colspan='3' class='WT'></td></tr>");
			sb.append("<tr><td colspan='3' class='WT'></td></tr>");
			
			/*sb.append("<tr><td colspan='2' class='WT'><b>重点提示内容</b></td><td class='WT'><b>是否已知悉</b></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>1.禁止控股股东、实际控制人及其他关联方以任何形式占用挂牌公司的资金、资产以及其他资源。</td><td class='WT'></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>2.在按照公司章程提交股东大会或董事会审议之前，挂牌公司不得对外担保。</td><td class='WT'></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>3.发生控股股东、实际控制人或者其关联方占用资金，违规对外担保的，应当至少每月发布一次提示性公告，披露资金占用和违规对外担保的解决进展情况。</td><td class='WT'></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>4.对于超出年初预计金额的日常性关联交易，超出金额应当根据公司章程的规定提交董事会或股东大会审议；对于年初没有预计的偶发性关联交易，一律提交股东大会审议。</td><td class='WT'></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>5.不得在取得股转公司同意函之前使用募集资金；不得未经股东大会审议擅自变更募集资金用途；不得将募集资金用于持有交易性金融资产和可供出售的金融资产、委托理财等。</td><td class='WT'></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>6.失信联合惩戒对象不得担任挂牌公司董事、监事和高级管理人员。</td><td class='WT'></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>7.公司应按照《非上市公众公司收购管理办法》第13条规定，披露权益变动公告，并自权益变动事实发生之日起至披露后2日内，不得再行买卖该公众公司的股票。</td><td class='WT'></td></tr>");
			sb.append("<tr><td colspan='2' class='WT'>8.公司所有董事会（监事会）会议决议均应及时向主办券商报备。</td><td class='WT'></td></tr>");
			
			sb.append("<tr><td colspan='3' class='WT'></td></tr>");
			sb.append("<tr><td colspan='3' class='WT'></td></tr>");*/
		}
		if(zqServer.equals("hlzq")||zqServer.equals("xnzq")){
			sb.append("<tr><td colspan='2' style='font-size: 16px;'>第一部分：").append(zqServer.equals("hlzq")?"日常督导":"主要事项").append("</td></tr>");
		}
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet xp = null;
		try {
			conn = DBUtil.open();
			ps=conn.prepareStatement(sql.toString());
			xp = ps.executeQuery();
			while(xp.next()){
				sb.append("<tr><td colspan='3' class='WT'>" + (xp.getString("QUESTION")==null||xp.getString("QUESTION").toString().equals("")?"空":xp.getString("QUESTION").toString())+"</td></tr>");
				sb.append("<tr class='DA'>");
				sb.append("<td>是否发生:"+(xp.getString("ANSWER")==null||xp.getString("ANSWER").toString().equals("")?"空":xp.getString("ANSWER").toString())+"</td>");
				sb.append("<td>是否披露:"+(xp.getString("PL")==null||xp.getString("PL").toString().equals("")?"空":xp.getString("PL").toString())+"</td>");
				if(dqrq.equals("01")){
					sb.append( "<td>预计本月是否发生:"+(xp.getString("YJXYSFFS")==null||xp.getString("YJXYSFFS").equals("")?"空":xp.getString("YJXYSFFS"))+"</td>");
					}else{
						sb.append( "<td>预计下周是否发生:"+(xp.getString("YJXYSFFS")==null||xp.getString("YJXYSFFS").equals("")?"空":xp.getString("YJXYSFFS"))+"</td>");	
					}
				sb.append("</tr>");
				sb.append( "<tr class='DA'><td colspan='3'>情况说明:"+(xp.getString("BZ")==null||xp.getString("BZ").toString().equals("")?"空":xp.getString("BZ").toString())+"</td></tr>");
				sb.append( "<tr class='DA'><td colspan='3'>批注:"+(xp.getString("PZ")==null||xp.getString("PZ").toString().equals("")?"空":xp.getString("PZ").toString())+"</td></tr>");
				
			}
			if(xp!=null)
				xp.close();
			if(ps!=null)
				ps.close();
			if(zqServer.equals("hlzq")||zqServer.equals("xnzq")){
				sb.append("<tr><td colspan='2' style='font-size: 16px;'>第二部分：").append(zqServer.equals("hlzq")?"注意事项":"重点提示内容").append("</td></tr>");
				ps=conn.prepareStatement(sql_.toString());
				xp = ps.executeQuery();
				while(xp.next()){
					sb.append("<tr><td colspan='3' class='WT'>" + (xp.getString("QUESTION")==null||xp.getString("QUESTION").toString().equals("")?"空":xp.getString("QUESTION").toString())+"</td></tr>");
					sb.append("<tr class='DA'>");
					sb.append("<td>是否知悉:"+(xp.getString("ANSWER")==null||xp.getString("ANSWER").toString().equals("")?"空":xp.getString("ANSWER").toString())+"</td>");
					//sb.append("<td>是否披露:"+(xp.getString("PL")==null||xp.getString("PL").toString().equals("")?"空":xp.getString("PL").toString())+"</td>");
					/*if(dqrq.equals("01")){
						sb.append( "<td>预计本月是否发生:"+(xp.getString("YJXYSFFS")==null||xp.getString("YJXYSFFS").equals("")?"空":xp.getString("YJXYSFFS"))+"</td>");
						}else{
							sb.append( "<td>预计下周是否发生:"+(xp.getString("YJXYSFFS")==null||xp.getString("YJXYSFFS").equals("")?"空":xp.getString("YJXYSFFS"))+"</td>");	
						}*/
					sb.append("</tr>");
					sb.append( "<tr class='DA'><td colspan='3'>情况说明:"+(xp.getString("BZ")==null||xp.getString("BZ").toString().equals("")?"空":xp.getString("BZ").toString())+"</td></tr>");
					sb.append( "<tr class='DA'><td colspan='3'>批注:"+(xp.getString("PZ")==null||xp.getString("PZ").toString().equals("")?"空":xp.getString("PZ").toString())+"</td></tr>");
					
				}
			}
	        if(zqServer!=null&&"xnzq".equals(zqServer)){
	        	sb.append("<tr><td colspan='3' class='WT'>①控股股东、实际控制人或其关联方占用资金：指挂牌公司为控股股东、实际控制人及其附属企业垫付的工资、福利、保险、广告等费用和其他支出；代控股股东、实际控制人及其附属企业偿还债务而支付的资金；有偿或者无偿、直接或者间接拆借给控股股东、实际控制人及其附属企业的资金；为控股股东、实际控制人及其附属企业承担担保责任而形成的债权；其他在没有商品和劳务对价情况下提供给控股股东、实际控制人其他附属企业使用的资金或者全国股份转让系统公司认定的其他形式的占用资金情形。</td></tr>");
	        	sb.append("<tr><td colspan='3' class='WT'>② 挂牌公司的关联交易，是指挂牌公司与关联方之间发生的转移资源或者义务的事项。非控股子公司与挂牌公司、控股子公司间的交易为关联交易。</td></tr>");
	        	sb.append("<tr><td colspan='3' class='WT'>③包括但不限于：1、投资者及其一致行动人通过全国股份转让系统的做市方式、竞价方式进行证券转让拥有权益的股份达到公众公司已发行股份的10%；2、投资者及其一致行动人通过协议方式在公众公司中拥有权益的股份拟达到或者超过公众公司已发行股份的10%；3、挂牌公司中拥有的权益份额达到总股本10%及以上的股东，其拥有权益的股份占挂牌公司已发行股份的比例每增加或者减少5%（即其拥有权益的股份每达到5%的整数倍时），自该事实发生之日起至披露后2日内，买卖公司股票的情形。</td></tr>");
	        }
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, xp);
		}
		sb.append( "</table>"
				+ "<div  >	"
				+ "</body>"
				+ "</html>");
		byte[] bytes = sb.toString().getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	    POIFSFileSystem poifs = new POIFSFileSystem();
	    DirectoryEntry directory = poifs.getRoot();
	    OutputStream out1 = null;
	    try {
	    	DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
		    bais.close();
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("信披自查反馈.doc");
			response.setContentType("application/vnd.ms-word;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			poifs.writeFilesystem(out1);
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
	/*xlj 2018年2月26日13:52:01 注掉周瑞金编写的npoi导出方法，避免出现更多问题，word选项-》高级-》打开时确认文件格式转换（不勾选）
	public void expWord(HttpServletResponse response,String customername,String ggid, String khbh,String dqrq) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List<HashMap> xpList=new ArrayList<HashMap>();
		List<HashMap> zxList=new ArrayList<HashMap>();
		
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer")==null?"":config.get("zqServer").toString();
	
		List lables_ = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables_.add("WT"+i);
			lables_.add("AS"+i);
			lables_.add("BZ"+i);
			lables_.add("PL"+i);
			lables_.add("PZ"+i);
			lables_.add("YJXYSFFS"+i);
		}
		lables_.add("CUSTOMERNAME");
		lables_.add("CUSTOMERNO");
		lables_.add("USERNAME");
		lables_.add("USERID");
		lables_.add("TZGGID");
		lables_.add("ID");
		lables_.add("ZQDM");
		lables_.add("ZQJC");
		String sql_ = "SELECT S.ZQDM,S.ZQJC,A.* FROM BD_VOTE_WJDC A "
				+ "INNER JOIN bd_zqb_kh_base s ON A.CUSTOMERNO=S.CUSTOMERNO"
				+ " WHERE A.CUSTOMERNO=? AND TZGGID=?";
		Map params__ = new HashMap();
		params__.put(1, khbh);
		params__.put(2, ggid);
		List<HashMap> list = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);
		String zqdm="";
		String zqjc="";
		String uName="";
		
		HashMap hashMap=new HashMap();
		if(list!=null && list.size()>0){
			hashMap=list.get(0);
			uName=list.get(0).get("USERNAME")==null?"":list.get(0).get("USERNAME").toString();
			zqdm=list.get(0).get("ZQDM")==null?"":list.get(0).get("ZQDM").toString();
			zqjc=list.get(0).get("ZQJC")==null?"":list.get(0).get("ZQJC").toString();
		}
		if(hashMap!=null){
			HashMap map=null;
			for (int i = 1; i <= 50; i++) {
				String pl= hashMap.get("PL"+i)==null?"":hashMap.get("PL"+i).toString();
				String yjxyssfs=hashMap.get("YJXYSFFS"+i)==null?"":hashMap.get("YJXYSFFS"+i).toString();
				String wt=hashMap.get("WT"+i)==null?"":hashMap.get("WT"+i).toString();
				map=new HashMap();
				map.put("QUESTION", wt);
				map.put("ANSWER", hashMap.get("AS"+i));
				map.put("BZ", hashMap.get("BZ"+i)==null?"":hashMap.get("BZ"+i));
				map.put("PL",pl);
				map.put("PZ", hashMap.get("PZ"+i)==null?"":hashMap.get("PZ"+i));
				map.put("YJXYSFFS",yjxyssfs );
				if(!"".equals(pl) && !"".equals(yjxyssfs)){
					xpList.add(map);
				}else{
					if(!"".equals(wt)){
						zxList.add(map);
					}
					
				}
				
			}
		}
	
		
		StringBuffer sb=new StringBuffer();
		//查询证券简称
				
				
				if(!zqServer.equals("xnzq")){
					for (HashMap xp : xpList) {
						sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
						sb.append( "<tr><td class='DA'>是否发生:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
						sb.append( "<td class='DA'>是否披露:"+(xp.get("PL")==null||xp.get("PL").toString().equals("")?"空":xp.get("PL").toString())+"</td>");
						if(dqrq.equals("01")){
							sb.append( "<td class='DA'>预计本月是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");
						}else{
							sb.append( "<td class='DA'>预计下周是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");	
						}
						sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
						sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
					}
				}else{
					for (HashMap xp : zxList) {
						sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
						sb.append( "<tr><td class='DA'>是否知悉:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
						sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
						sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
					}
				}
				
				if(zqServer.equals("hlzq")||zqServer.equals("xnzq")){
					sb.append("<tr><td colspan='2' style='font-size: 16px;'>第二部分：").append(zqServer.equals("hlzq")?"注意事项":"主要事项").append("</td></tr>");
					if(zqServer.equals("xnzq")){
						for (HashMap xp : xpList) {
							sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
							sb.append( "<tr><td class='DA'>是否发生:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
							sb.append( "<td class='DA'>是否披露:"+(xp.get("PL")==null||xp.get("PL").toString().equals("")?"空":xp.get("PL").toString())+"</td>");
							if(dqrq.equals("01")){
								sb.append( "<td class='DA'>预计本月是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");
							}else{
								sb.append( "<td class='DA'>预计下周是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");	
							}
							sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
							sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
						}
					}else{
						for (HashMap xp : zxList) {
							sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
							sb.append( "<tr><td class='DA'>是否知悉:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
							sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
							sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
						}
					}
					
				}
				        sb.append("<tr></tr>");
				        sb.append("<tr><td></td><td></td><td class='WT'>董秘或信息披露负责人签字:</td></tr>");
				        sb.append("<tr><td></td><td></td><td class='WT'>(公司盖章)</td></tr>");
				        sb.append("<tr><td></td><td></td><td class='WT'>年&nbsp;&nbsp;月&nbsp;&nbsp;日</td></tr>");
				        
				        if(zqServer!=null&&"xnzq".equals(zqServer)){
				        	sb.append("<tr><td colspan='3' class='WT'>①控股股东、实际控制人或其关联方占用资金：指挂牌公司为控股股东、实际控制人及其附属企业垫付的工资、福利、保险、广告等费用和其他支出；代控股股东、实际控制人及其附属企业偿还债务而支付的资金；有偿或者无偿、直接或者间接拆借给控股股东、实际控制人及其附属企业的资金；为控股股东、实际控制人及其附属企业承担担保责任而形成的债权；其他在没有商品和劳务对价情况下提供给控股股东、实际控制人其他附属企业使用的资金或者全国股份转让系统公司认定的其他形式的占用资金情形。</td></tr>");
				        	sb.append("<tr><td colspan='3' class='WT'>② 挂牌公司的关联交易，是指挂牌公司与关联方之间发生的转移资源或者义务的事项。非控股子公司与挂牌公司、控股子公司间的交易为关联交易。</td></tr>");
				        	sb.append("<tr><td colspan='3' class='WT'>③包括但不限于：1、投资者及其一致行动人通过全国股份转让系统的做市方式、竞价方式进行证券转让拥有权益的股份达到公众公司已发行股份的10%；2、投资者及其一致行动人通过协议方式在公众公司中拥有权益的股份拟达到或者超过公众公司已发行股份的10%；3、挂牌公司中拥有的权益份额达到总股本10%及以上的股东，其拥有权益的股份占挂牌公司已发行股份的比例每增加或者减少5%（即其拥有权益的股份每达到5%的整数倍时），自该事实发生之日起至披露后2日内，买卖公司股票的情形。</td></tr>");
				        }
				        
							sb.append( "</table>"
									+ "</div>"
						+ "</body>"
					+ "</html>");
		HashMap dataMap=new HashMap();
		dataMap.put("zqjc", zqjc);
		dataMap.put("zqServer", zqServer);
		dataMap.put("zqdm", zqdm);
		dataMap.put("uName", uName);
		dataMap.put("xpList", xpList);
		dataMap.put("zxList", zxList);
		dataMap.put("dqrq", dqrq);
		String createWord = ConfigurationAPI.getInstance().createWord(response,dataMap,"xpzcfk.ftl");
		
		File file = new File(createWord);
		InputStream is=null;
		OutputStream out1 = null;
		String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目结算单.doc");
		try {
			is = new FileInputStream(createWord);
			out1 = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-word;charset=UTF-8");
			response.setHeader("Content-Disposition",disposition);
			byte[] b = new byte[1024];   
			int len;
			while ((len=is.read(b)) >0) {   
				out1.write(b,0,len);   
			}
			if (is != null) {
				is.close();
			}
			file.delete();
		} catch (Exception e) {
		}finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
				}
			}
		}
	}
	*/
	public void expWord(HttpServletResponse response,String customername,Long ggid, String khbh,String dqrq) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List<HashMap> xpList=new ArrayList<HashMap>();
		List<HashMap> zxList=new ArrayList<HashMap>();
		
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer")==null?"":config.get("zqServer").toString();
	
		List lables_ = new ArrayList();
		for (int i = 1; i <= 50; i++) {
			lables_.add("WT"+i);
			lables_.add("AS"+i);
			lables_.add("BZ"+i);
			lables_.add("PL"+i);
			lables_.add("PZ"+i);
			lables_.add("YJXYSFFS"+i);
		}
		lables_.add("CUSTOMERNAME");
		lables_.add("CUSTOMERNO");
		lables_.add("USERNAME");
		lables_.add("USERID");
		lables_.add("TZGGID");
		lables_.add("ID");
		lables_.add("ZQDM");
		lables_.add("ZQJC");
		String sql_ = "SELECT S.ZQDM,S.ZQJC,A.* FROM BD_VOTE_WJDC A "
				+ "INNER JOIN bd_zqb_kh_base s ON A.CUSTOMERNO=S.CUSTOMERNO"
				+ " WHERE A.CUSTOMERNO=? AND TZGGID=?";
		Map params__ = new HashMap();
		params__.put(1, khbh);
		params__.put(2, ggid);
		List<HashMap> list = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);
		String zqdm="";
		String zqjc="";
		String uName="";
		
		HashMap hashMap=new HashMap();
		if(list!=null && list.size()>0){
			hashMap=list.get(0);
			uName=list.get(0).get("USERNAME")==null?"":list.get(0).get("USERNAME").toString();
			zqdm=list.get(0).get("ZQDM")==null?"":list.get(0).get("ZQDM").toString();
			zqjc=list.get(0).get("ZQJC")==null?"":list.get(0).get("ZQJC").toString();
		}
		if(hashMap!=null){
			HashMap map=null;
			for (int i = 1; i <= 50; i++) {
				String pl= hashMap.get("PL"+i)==null?"":hashMap.get("PL"+i).toString();
				String yjxyssfs=hashMap.get("YJXYSFFS"+i)==null?"":hashMap.get("YJXYSFFS"+i).toString();
				String wt=hashMap.get("WT"+i)==null?"":hashMap.get("WT"+i).toString();
				map=new HashMap();
				map.put("QUESTION", wt);
				map.put("ANSWER", hashMap.get("AS"+i));
				map.put("BZ", hashMap.get("BZ"+i));
				map.put("PL",pl);
				map.put("PZ", hashMap.get("PZ"+i));
				map.put("YJXYSFFS",yjxyssfs );
				if(!"".equals(pl) && !"".equals(yjxyssfs)){
					xpList.add(map);
				}else{
					if(!"".equals(wt)){
						zxList.add(map);
					}
					
				}
				
			}
		}
	
		
		StringBuffer sb=new StringBuffer();
		//查询证券简称
				
				sb.append("<!DOCTYPE html>"
					+ "<html>"
						+ "<head>"
							+ "<title></title>"
							+ "<style>"
								+ "body{font-family: '宋体';word-break:break-all;}"
								+ "tr{word-break:break-all;}"
								+ ".header {font-size:14px;height:22px;}"
								+ ".WT {font-size:12px;height:20px;word-wrap:break-word;word-break:break-all;width:450px;}"
								+ ".DA {font-size:9pt;height:18px;width:150px;}"							
							+ "</style>"
						+ "</head>"
						+ "<body>	"
						+ "<div  align='center'>	"
							+ "<table cellspacing='0' cellpadding='0'>");
				sb.append("<tr style='width:450px;'><td style='width:150px;'></td><td style='width:150px;'></td><td style='width:150px;'></td></tr>");
				
				sb.append("<tr><td></td><td colspan='2' class='header'><b>"+zqjc+":主办券商持续督导问询函</b></td></tr>");
				
				
				if(zqServer!=null&&"xnzq".equals(zqServer)){
					sb.append("<tr><td colspan='3' class='WT'><b>挂牌公司简称：").append(zqjc).append("</b></td></tr>");
					sb.append("<tr><td colspan='3' class='WT'><b>挂牌公司代码：").append(zqdm).append("</b></td></tr>");
					sb.append("<tr><td colspan='3' class='WT'><b>董秘或信息披露负责人：").append(uName).append("</b></td></tr>");
					
					sb.append("<tr><td colspan='3' class='WT'></td></tr>");
					sb.append("<tr><td colspan='3' class='WT'></td></tr>");
					
				
				}
				
				if(zqServer.equals("hlzq")||zqServer.equals("xnzq")){
					sb.append("<tr><td colspan='2' style='font-size: 16px;'>第一部分：").append(zqServer.equals("hlzq")?"日常督导":"重点提示内容").append("</td></tr>");
				}
				if(!zqServer.equals("xnzq")){
					for (HashMap xp : xpList) {
						sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
						sb.append( "<tr><td class='DA'>是否发生:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
						sb.append( "<td class='DA'>是否披露:"+(xp.get("PL")==null||xp.get("PL").toString().equals("")?"空":xp.get("PL").toString())+"</td>");
						if(dqrq.equals("01")){
							sb.append( "<td class='DA'>预计本月是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");
						}else{
							sb.append( "<td class='DA'>预计下周是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");	
						}
						sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
						sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
					}
				}else{
					for (HashMap xp : zxList) {
						sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
						sb.append( "<tr><td class='DA'>是否知悉:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
						sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
						sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
					}
				}
				
				if(zqServer.equals("hlzq")||zqServer.equals("xnzq")){
					sb.append("<tr><td colspan='2' style='font-size: 16px;'>第二部分：").append(zqServer.equals("hlzq")?"注意事项":"主要事项").append("</td></tr>");
					if(zqServer.equals("xnzq")){
						for (HashMap xp : xpList) {
							sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
							sb.append( "<tr><td class='DA'>是否发生:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
							sb.append( "<td class='DA'>是否披露:"+(xp.get("PL")==null||xp.get("PL").toString().equals("")?"空":xp.get("PL").toString())+"</td>");
							if(dqrq.equals("01")){
								sb.append( "<td class='DA'>预计本月是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");
							}else{
								sb.append( "<td class='DA'>预计下周是否发生:"+(xp.get("YJXYSFFS")==null||xp.get("YJXYSFFS").toString().equals("")?"空":xp.get("YJXYSFFS").toString())+"</td></tr>");	
							}
							sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
							sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
						}
					}else{
						for (HashMap xp : zxList) {
							sb.append( "<tr><td colspan='3' class='WT'><b>" + (xp.get("QUESTION")==null||xp.get("QUESTION").toString().equals("")?"空":xp.get("QUESTION").toString())+"</b></td></tr>");
							sb.append( "<tr><td class='DA'>是否知悉:"+(xp.get("ANSWER")==null||xp.get("ANSWER").toString().equals("")?"空":xp.get("ANSWER").toString())+"</td>");
							sb.append( "<tr><td colspan='3' class='DA'>情况说明:"+(xp.get("BZ")==null||xp.get("BZ").toString().equals("")?"空":xp.get("BZ").toString())+"</td></tr>");
							sb.append( "<tr><td colspan='3' class='DA'>批注:"+(xp.get("PZ")==null||xp.get("PZ").toString().equals("")?"空":xp.get("PZ").toString())+"</td></tr>");
						}
					}
					
				}
				        sb.append("<tr></tr>");
				        sb.append("<tr><td></td><td></td><td class='WT'>董秘或信息披露负责人签字:</td></tr>");
				        sb.append("<tr><td></td><td></td><td class='WT'>(公司盖章)</td></tr>");
				        sb.append("<tr><td></td><td></td><td class='WT'>年&nbsp;&nbsp;月&nbsp;&nbsp;日</td></tr>");
				        
				        if(zqServer!=null&&"xnzq".equals(zqServer)){
				        	sb.append("<tr><td colspan='3' class='WT'>①控股股东、实际控制人或其关联方占用资金：指挂牌公司为控股股东、实际控制人及其附属企业垫付的工资、福利、保险、广告等费用和其他支出；代控股股东、实际控制人及其附属企业偿还债务而支付的资金；有偿或者无偿、直接或者间接拆借给控股股东、实际控制人及其附属企业的资金；为控股股东、实际控制人及其附属企业承担担保责任而形成的债权；其他在没有商品和劳务对价情况下提供给控股股东、实际控制人其他附属企业使用的资金或者全国股份转让系统公司认定的其他形式的占用资金情形。</td></tr>");
				        	sb.append("<tr><td colspan='3' class='WT'>② 挂牌公司的关联交易，是指挂牌公司与关联方之间发生的转移资源或者义务的事项。非控股子公司与挂牌公司、控股子公司间的交易为关联交易。</td></tr>");
				        	sb.append("<tr><td colspan='3' class='WT'>③包括但不限于：1、投资者及其一致行动人通过全国股份转让系统的做市方式、竞价方式进行证券转让拥有权益的股份达到公众公司已发行股份的10%；2、投资者及其一致行动人通过协议方式在公众公司中拥有权益的股份拟达到或者超过公众公司已发行股份的10%；3、挂牌公司中拥有的权益份额达到总股本10%及以上的股东，其拥有权益的股份占挂牌公司已发行股份的比例每增加或者减少5%（即其拥有权益的股份每达到5%的整数倍时），自该事实发生之日起至披露后2日内，买卖公司股票的情形。</td></tr>");
				        }
				        
							sb.append( "</table>"
									+ "</div>"
						+ "</body>"
					+ "</html>");
		byte[] bytes = sb.toString().getBytes();
		String a=sb.toString();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	    POIFSFileSystem poifs = new POIFSFileSystem();
	    DirectoryEntry directory = poifs.getRoot();
	    OutputStream out1 = null;
	    try {
	    	DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
	    	bais.close();
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("信披自查反馈.doc");
			response.setContentType("application/vnd.ms-word;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			poifs.writeFilesystem(out1);
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
	public void expWjdc(HttpServletResponse response,String ggid){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("问卷调查回复情况");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setWrapText(true);
		
		int m=0;
		
		HSSFCell 
		cell = row.createCell((short) m);
		cell.setCellValue("公司代码");
		cell.setCellStyle(style);
		m++;
		
		cell = row.createCell((short) m);
		cell.setCellValue("部门/公司名称");
		cell.setCellStyle(style);
		m++;
		
		cell = row.createCell((short) m);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		m++;
		
		cell = row.createCell((short) m);
		cell.setCellValue("手机号");
		cell.setCellStyle(style);
		m++;
		
		cell = row.createCell((short) m);
		cell.setCellValue("状态");
		cell.setCellStyle(style);
		m++;
		
		cell = row.createCell((short) m);
		cell.setCellValue("回复时间");
		cell.setCellStyle(style);
		m++;
		
		List lable = new ArrayList();lable.add("QUESTION");
		String sq = "SELECT QUESTION FROM BD_XP_TZGGB T LEFT JOIN BD_VOTE_WJDCWTB B ON T.WJFILE=B.TAG WHERE T.ID=? ORDER BY SORTID";
		Map params = new HashMap();params.put(1, ggid);
		List<HashMap> qnum = com.iwork.commons.util.DBUtil.getDataList(lable, sq, params);
		for (HashMap data : qnum) {
			cell = row.createCell((short) m);
			cell.setCellValue(data.get("QUESTION")==null?"":data.get("QUESTION").toString());
			cell.setCellStyle(style);
			m++;
		}
		
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到
		List lables = new ArrayList();
		lables.add("GSDM");
		lables.add("GSMC");
		lables.add("XM");
		lables.add("MOBILE");
		lables.add("STATUS");
		lables.add("HFSJ");
		lables.add("TZBT");
		lables.add("USERID");
		for (int i = 1; i <= qnum.size(); i++) {
			lables.add("WT"+i);
			lables.add("AS"+i);
			lables.add("PL"+i);
			lables.add("BZ"+i);
			lables.add("PZ"+i);
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TZ.TZBT,HF.GSDM,HF.GSMC,HF.USERID,HF.XM,O.MOBILE,HF.STATUS,HF.HFSJ");
		for (int i = 1; i <= qnum.size(); i++) {
			sql.append(",HD.WT").append(i).append(",HD.AS").append(i).append(",HD.PL").append(i).append(",HD.BZ").append(i).append(",HD.PZ").append(i);
		}
		sql.append(" FROM BD_XP_TZGGB TZ LEFT JOIN BD_XP_HFQKB HF ON TZ.ID=HF.GGID LEFT JOIN BD_VOTE_CXDDRCGZFK HD ON HF.GGID=HD.TZGGID AND HF.USERID=HD.USERID LEFT JOIN ORGUSER O ON HD.USERID=O.USERID WHERE TZ.ID=?");
		
		Map param = new HashMap();
		params.put(1, ggid);
		List<HashMap> list = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		int n = 1;
		if (list == null) {
			return;
		}
		String filename = list.get(0).get("TZBT")==null?"问卷调查回复情况":list.get(0).get("TZBT").toString()+".xls";
		m=0;
		for (HashMap item : list) {
			if (item == null) {
				continue;
			}
			
			row = sheet.createRow((int) n++);
			row.setHeightInPoints(40);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) m);
			cell1.setCellValue(item.get("GSDM")==null?"":item.get("GSDM").toString());
			cell1.setCellStyle(style1);
			m++;
			
			HSSFCell cell2 = row.createCell((short) m);
			cell2.setCellValue(item.get("GSMC")==null?"":item.get("GSMC").toString());
			cell2.setCellStyle(style1);
			m++;
			
			HSSFCell cell3 = row.createCell((short) m);
			cell3.setCellValue(item.get("XM")==null?"":item.get("XM").toString());
			cell3.setCellStyle(style1);
			m++;
			
			HSSFCell cell4 = row.createCell((short) m);
			cell4.setCellValue(item.get("MOBILE")==null?"":item.get("MOBILE").toString());
			cell4.setCellStyle(style1);
			m++;
			
			HSSFCell cell5 = row.createCell((short) m);
			cell5.setCellValue(item.get("STATUS")==null?"":item.get("STATUS").toString());
			cell5.setCellStyle(style1);
			m++;
			
			HSSFCell cell6 = row.createCell((short) m);
			cell6.setCellValue(item.get("HFSJ")==null?"":item.get("HFSJ").toString());
			cell6.setCellStyle(style1);
			m++;
			
			for (int i = 1; i <= qnum.size(); i++) {
				
				String wt = item.get("WT"+i)==null||item.get("WT"+i).equals("")?"":item.get("WT"+i).toString();
				String as = "是否发生:"+(item.get("AS"+i)==null||item.get("AS"+i).equals("")?"":item.get("AS"+i).toString());
				String pl = "\n是否执行:"+(item.get("PL"+i)==null||item.get("PL"+i).equals("")?"":item.get("PL"+i).toString());
				String bz = "\n情况说明:"+(item.get("BZ"+i)==null||item.get("BZ"+i).equals("")?"空":item.get("BZ"+i).toString());
				String pz = "\n批注:"+(item.get("PZ"+i)==null||item.get("PZ"+i).equals("")?"空":item.get("PZ"+i).toString());
				
				HSSFCell cell8 = row.createCell((short) m);
				cell8.setCellValue(wt.equals("")?"":as+pl+bz+pz);
				cell8.setCellStyle(style1);
				m++;
			}
			m=0;
		}
		/*int count = 6+qnum.size();
		for (int i = 0; i < count; i++) {
			sheet.setColumnWidth(i, 256*2*20);			
		}*/
		sheet.setColumnWidth(0, 256*2*5);
		sheet.setColumnWidth(1, 256*2*16);
		sheet.setColumnWidth(2, 256*2*4);
		sheet.setColumnWidth(3, 256*2*10);
		sheet.setColumnWidth(4, 256*2*4);
		sheet.setColumnWidth(5, 256*2*10);
		for (int i = 6; i < (6+qnum.size()); i++) {
			sheet.setColumnWidth(i, 256*2*20);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(filename);
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
	
	/**
	 * 通知未反馈人员
	 * @param ggid  通知公告id
	 */
	public void senSMSToNotReply(String ggid){
		List<HashMap> l = zqbAnnouncementDAO.getHfqkbByGgStatus(ggid);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String smsContent = "";
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("tzggbuuid");
		HashMap hashmap = DemAPI.getInstance().getFormData(demUUID,Long.parseLong(ggid));
		if (hashmap != null) {
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.TZGG_ADD_KEY,hashmap);
		}
		HashMap data = null;
		String hfqkbuuid = this.getConfigUUID("hfqkbuuid");
		Long instanceId;
		Long dataid;
		for (HashMap user : l) {
			instanceId = Long.parseLong(user.get("INSTANCEID").toString());
				data = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			dataid = Long.parseLong(data.get("ID").toString());
			data.put("TZZT", 1);
			if (!smsContent.equals("")) {
				Object mobile = user.get("MOBILE");
				if (mobile != null && !mobile.equals("")) {
					boolean flag = MessageAPI.getInstance().sendSMS(uc,mobile.toString(), smsContent);
					if(flag){
						DemAPI.getInstance().updateFormData(hfqkbuuid, instanceId, data, dataid, false);;
					}
				}
				Object email = user.get("EMAIL");
				if(email!=null&&!email.equals("") && uc != null){
					MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email.toString(), "通知公告提醒",smsContent,"");
				}
			}
		}
	}
	
	public List<HashMap> getZqbTzggProjectCostormerSet(String userId,String user_name,String roleid,String gsmc){
		StringBuffer sql = new StringBuffer();
    	//XLJ update 2018年6月7日16:51:55 根据西南需求，要求项目经办人查询为所有的督导，故与东莞的项目经办人查询做兼容处理。
    	List params = new ArrayList();
		sql.append("SELECT DISTINCT O.USERID,O.USERNAME,O.MOBILE,O.EMAIL,O.HOMETEL,O.DEPARTMENTNAME FROM ");
		sql.append("(");
		sql.append("SELECT SUBSTR(KHFZR,0, instr(KHFZR,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(A.QYNBRYSH,0, instr(QYNBRYSH,'[',1)-1) USERID FROM BD_MDM_KHQXGLB A");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(ZZCXDD,0, instr(ZZCXDD,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(FHSPR,0, instr(FHSPR,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(CWSCBFZR2,0, instr(CWSCBFZR2,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(CWSCBFZR3,0, instr(CWSCBFZR3,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(FBBWJSHR,0, instr(FBBWJSHR,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(GGFBR,0, instr(GGFBR,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(" UNION ALL ");
		sql.append("SELECT SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) USERID FROM BD_MDM_KHQXGLB");
		sql.append(") A");
		sql.append(" LEFT JOIN ORGUSER O ON A.USERID=O.USERID WHERE 1=1 AND A.USERID IS NOT NULL  and  o.userstate=0  and sysdate<o.enddate ");
		if(userId!=null&&!userId.equals("")){
			sql.append(" AND O.USERID LIKE ?");
			params.add("%"+userId.toUpperCase()+"%");
		}
		if(user_name!=null&&!user_name.equals("")){
			sql.append(" AND O.USERNAME LIKE ?");
			params.add("%"+user_name+"%");
		}
		if(roleid!=null&&!roleid.equals("")){
			sql.append(" AND O.ORGROLEID = ?");
			params.add(roleid);
		}
		if(gsmc!=null&&!gsmc.equals("")){
			sql.append(" AND O.DEPARTMENTNAME LIKE ?");
			params.add("%"+gsmc+"%");
		}
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap map;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn=null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i));
			}
			rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				String USERID= rs.getString("USERID");			//账号
				String USERNAME = rs.getString("USERNAME");		//名称
				String MOBILE = rs.getString("MOBILE");	//部门/公司名称
				String EMAIL = rs.getString("EMAIL");			//所属证监局
				String HOMETEL = rs.getString("HOMETEL");					//省市
				String DEPARTMENTNAME = rs.getString("DEPARTMENTNAME");				//挂牌时间
				map.put("USERID", USERID==null ? "":USERID);
				map.put("USERNAME", USERNAME==null ? "":USERNAME);
				map.put("MOBILE", MOBILE==null ? "":MOBILE);
				map.put("EMAIL", EMAIL==null ? "":EMAIL);
				map.put("HOMETEL", HOMETEL==null ? "":HOMETEL);
				map.put("DEPARTMENTNAME", DEPARTMENTNAME==null ? "":DEPARTMENTNAME);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
	
	/**
	 * 通用转发短信提醒
	 * @param actdefid
	 * @param userName
	 * @param instanceid
	 */
	public void forwardMsgsend(String actdefid, String userName, Long instanceid) {
		if(actdefid.startsWith("GGSPLC")||actdefid.startsWith("CXDDYFQLC")){
			String targetuserid = userName.substring(0, userName.indexOf("["));
			HashMap formdata = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			UserContext target = UserContextUtil.getInstance().getUserContext(targetuserid);
			
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			StringBuffer mailContent = new StringBuffer();
			mailContent.append(uc.get_userModel().getUsername()).append("已将").append(formdata.get("ZQJCXS")).append("(").append(formdata.get("ZQDMXS")).append(")").append("的公告:").append(formdata.get("NOTICENAME")).append("转发给您,请在系统中查看!");
			String mobile = target.get_userModel().getMobile();
			if(mobile!=null&&!mobile.equals("")){
				MessageAPI.getInstance().sendSMS(uc, mobile, mailContent.toString());
			}
		}
	}

	/*public void impXpzcwt(String filename) {
		Long starttime = new Date().getTime();
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(filename);
		UserContext uc =UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		String rootPath=request.getRealPath("/");
		//String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
		
		File file = null;
		Workbook workBook = null;
		Sheet sheet = null;
		//遍历上传文件
		for (FileUpload fileUpload : sublist) {
			//加载xls文件
			file = new File(rootPath+File.separator+fileUpload.getFileUrl());
			
			try {
				workBook = new XSSFWorkbook(rootPath+File.separator+fileUpload.getFileUrl());  
			} catch (Exception ex) {  
				try {
					workBook = new HSSFWorkbook(new FileInputStream(rootPath+File.separator+fileUpload.getFileUrl()));
				} catch (FileNotFoundException e) {
					
				} catch (Exception e) {
					
				}
			}
			if(workBook==null){continue;}
			//遍历sheet
			for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
				sheet = workBook.getSheetAt(numSheet);  
				if (sheet == null) {continue;}
				// 循环行Row  
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					int g = sheet.getLastRowNum();
					Row row = sheet.getRow(rowNum);  
					if (row == null) {continue;}  
					// 循环列Cell  
					Cell cell = row.getCell(0);  
					cell = row.getCell(1);
				}
			}
		}
	}*/
	
	public List<HashMap> zqbGlfGgGlf(String khbh,String gsmc,String gslx,String zch,String zcdz,int pageNumber,int pageSize) {
		return zqbAnnouncementDAO.zqbGlfGgGlf(khbh,gsmc,gslx,zch,zcdz,pageNumber,pageSize);
	}

	public List<HashMap> sysdemMdmXpzcwt() {
		return zqbAnnouncementDAO.sysdemMdmXpzcwt();
	}
	
	public List<HashMap> sysdemMdmXpzcwtSize() {
		return zqbAnnouncementDAO.sysdemMdmXpzcwtSize();
	}
	
	public List<HashMap> sysdemMdmXmjdzl() {
		return zqbAnnouncementDAO.sysdemMdmXmjdzl();
	}
	
	public List<HashMap> sysdemMdmXmlx() {
		return zqbAnnouncementDAO.sysdemMdmXmlx();
	}
	
	public List<HashMap> sysdemMdmTyxmjdzl(String xmlx) {
		return zqbAnnouncementDAO.sysdemMdmTyxmjdzl(xmlx);
	}

	public void zqbVoteUpdateXpzcfk(String sql,String customerno,String tzggid,List<String> list,Map<String, String> map) {
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count=1;
		int num = 0;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				ps.setString(count, map.get(list.get(i)).toString());
				count++;
			}
			ps.setString(count, customerno);
			count++;
			ps.setString(count, tzggid);
			num = ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		if(num==1){
			Map params = new HashMap();
			params.put(1, tzggid);
			params.put(2, customerno);
			String hfsj = com.iwork.commons.util.DBUtil.getDataStr("HFSJ", "SELECT HFSJ FROM BD_XP_HFQKB WHERE  GGID=? AND CUSTOMERNO=?", params);
			if(hfsj==null||hfsj.equals("")){
				com.iwork.commons.util.DBUtil.update("UPDATE BD_XP_HFQKB SET STATUS='已回复',HFSJ=SYSDATE WHERE GGID=? AND CUSTOMERNO=?", params);
			}
		}
		LogUtil.getInstance().addLog(0L, "信披自查反馈信息维护", "更新信披自查反馈信息：通知公告ID："+tzggid+",客户编号为"+customerno+"的信披自查反馈信息。");
	}

	public List<HashMap> getNoticeHfContent(Long hfqkid) {
		List<HashMap> list = new ArrayList<HashMap>();
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("hfxxbuuid");
		HashMap formData = DemAPI.getInstance().getFormData(demUUID,hfqkid);
		StringBuffer count = new StringBuffer();
		if(formData!=null){
		if (formData.get("XGZL") != null && !"".equals(formData.get("XGZL").toString())) {
			String string = formData.get("XGZL").toString();
			count.append("<table>");
			List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(string);
			int n=1;
			for (FileUpload fileUpload : sublist) {
				String xgzl = fileUpload.getFileSrcName();
				String uuid = fileUpload.getFileId();
				
				String fileDivId = fileUpload.getFileId();
				String fileSrcName=fileUpload.getFileSrcName();
				
				count.append("<div  id=\"").append(fileDivId).append("\" style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">\n");
				count.append("<div style=\"align:right;float:right;\">&nbsp;&nbsp;\n");
				/*<a href=\"javascript:updateUploadify('"+fileDivId+"','"+fileSrcName+"');\">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*/
				count.append("<a href=\"javascript:uploadifyReomve('").append("xgzl").append("','").append(fileUpload.getFileId()).append("','").append(fileDivId).append("');\"><img src=\"/iwork_img/del3.gif\"/></a>\n");
				count.append("</div>\n");
				String icon = "iwork_img/attach.png";
				if(fileUpload.getFileSrcName()!=null){
					icon = FileType.getFileIcon(fileUpload.getFileSrcName());
				}
				count.append("<span>"+n+"、<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a></span>\n");
				count.append("</div>\n");
				n++;
			}
		}
		formData.put("XGZLCOUNT", count.toString());
		}
		list.add(formData);
		return list;
	}

	
	public List<HashMap<String, Object>> getItemList(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT SXID,SXINSTANCEID, SXMC,SXSYGZ,SXPLYQ,ROWNUM RM FROM (SELECT SX.ID SXID,BIND.INSTANCEID SXINSTANCEID,SX.SXMC SXMC,SX.SYGZ SXSYGZ,SX.PLYQ SXPLYQ FROM BD_XP_XPSXB SX INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID,BIND.FORMID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXB') META ON IFORM.METADATAID=META.ID) BIND ON SX.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		sql.append( " ORDER BY SX.ORDERINDEX))");
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getItemList(sql.toString(),parameter);
		return list;
	}

	public Integer getItemListSize(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) CNUM FROM BD_XP_XPSXB SX INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXB') META ON IFORM.METADATAID=META.ID) BIND ON SX.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		int count = zqbAnnouncementDAO.getItemListSize(sql.toString(),parameter);
		return count;
	}

	public List<HashMap<String, Object>> getItemContentList(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT NRID,PLNR,SXMC,CINSTANCEID,ROWNUM RM FROM (SELECT SXNR.ID NRID,SXNR.PLNR PLNR,SX.SXMC SXMC,BIND.INSTANCEID CINSTANCEID FROM BD_XP_XPSXNR SXNR INNER JOIN BD_XP_XPSXB SX ON SXNR.SXID=SX.ID INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID,BIND.FORMID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXNR') META ON IFORM.METADATAID=META.ID) BIND ON SXNR.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		sql.append( " ORDER BY SX.ORDERINDEX,SXNR.ORDERINDEX))");
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getItemContentList(sql.toString(),parameter);
		return list;
	}

	public Integer getItemContentListSize(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) CNUM FROM BD_XP_XPSXNR SXNR INNER JOIN BD_XP_XPSXB SX ON SXNR.SXID=SX.ID INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXNR') META ON IFORM.METADATAID=META.ID) BIND ON SXNR.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		int count = zqbAnnouncementDAO.getItemContentListSize(sql.toString(),parameter);
		return count;
	}

	public List<HashMap<String, Object>> getItemBcList(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT BCID,PLBC,SXMC,BCINSTANCEID,ROWNUM RM FROM (SELECT SXBC.ID BCID,SXBC.PLBC PLBC,SX.SXMC SXMC,BIND.INSTANCEID BCINSTANCEID FROM BD_XP_XPSXBC SXBC INNER JOIN BD_XP_XPSXB SX ON SXBC.SXID=SX.ID INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID,BIND.FORMID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXBC') META ON IFORM.METADATAID=META.ID) BIND ON SXBC.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		sql.append( " ORDER BY SX.ORDERINDEX,SXBC.ORDERINDEX ))");
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getItemBcList(sql.toString(),parameter);
		return list;
	}

	public Integer getItemBcListSize(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) CNUM FROM BD_XP_XPSXBC SXBC INNER JOIN BD_XP_XPSXB SX ON SXBC.SXID=SX.ID INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXBC') META ON IFORM.METADATAID=META.ID) BIND ON SXBC.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		int count = zqbAnnouncementDAO.getItemBcListSize(sql.toString(),parameter);
		return count;
	}

	public List<HashMap<String, Object>> getItemStepList(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT * FROM (SELECT BZID,BZNR,SXMC,BZINSTANCEID,ROWNUM RM FROM (SELECT SXBZ.ID BZID,SXBZ.BZNR BZNR,SX.SXMC SXMC,BIND.INSTANCEID BZINSTANCEID FROM BD_XP_XPSXBZ SXBZ INNER JOIN BD_XP_XPSXB SX ON SXBZ.SXID=SX.ID INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID,BIND.FORMID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXBZ') META ON IFORM.METADATAID=META.ID) BIND ON SXBZ.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		sql.append( " ORDER BY SX.ORDERINDEX,SXBZ.ORDERINDEX))");
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getItemStepList(sql.toString(),parameter);
		return list;
	}

	public Integer getItemStepListSize(String sxlx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) CNUM FROM BD_XP_XPSXBZ SXBZ INNER JOIN BD_XP_XPSXB SX ON SXBZ.SXID=SX.ID INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XPSXBZ') META ON IFORM.METADATAID=META.ID) BIND ON SXBZ.ID=BIND.DATAID ");
		if(sxlx!=null&&!sxlx.equals("")){
			sql.append(" WHERE UPPER(SXMC) = ?");
			parameter.add(sxlx);
		}
		int count = zqbAnnouncementDAO.getItemStepListSize(sql.toString(),parameter);
		return count;
	}

	public String itemRemove(Long sxinstanceid) {
		String info="";
		HashMap fromData = DemAPI.getInstance().getFromData(sxinstanceid);
		String sxmc = fromData.get("SXMC").toString();
		int cnum = DBUtil.getInt("SELECT SUM(CNUM) SNUM FROM (SELECT COUNT(*) CNUM FROM BD_XP_XPSXNR NR INNER JOIN (SELECT ID FROM BD_XP_XPSXB WHERE SXMC='"+sxmc+"') SX ON NR.SXID = SX.ID UNION ALL SELECT COUNT(*) CNUM FROM BD_XP_XPSXBC BC INNER JOIN (SELECT ID FROM BD_XP_XPSXB WHERE SXMC='"+sxmc+"') SX ON BC.SXID = SX.ID UNION ALL SELECT COUNT(*) CNUM FROM BD_XP_XPSXBZ BZ INNER JOIN (SELECT ID FROM BD_XP_XPSXB WHERE SXMC='"+sxmc+"') SX ON BZ.SXID = SX.ID) GROUP BY CNUM", "SNUM");
		if(cnum>0){
			info="2";
			return info;
		}else{
			boolean removeFormData = DemAPI.getInstance().removeFormData(sxinstanceid);
			if(removeFormData){
				Long dataId = Long.parseLong(fromData.get("ID").toString());
				LogUtil.getInstance().addLog(dataId, "信披事项信息维护", "删除信披事项名称:"+sxmc);
			}
			return removeFormData==true?"1":"0";
		}
	}

	public boolean itemContentRemove(Long cinstanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(cinstanceid);
		boolean removeFormData=false;
		removeFormData = DemAPI.getInstance().removeFormData(cinstanceid);
		if(removeFormData){
			String sxid=fromData.get("SXID").toString();
			String plnr=fromData.get("PLNR").toString();
			Long dataId = Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "披露内容信息维护", "删除信披事项ID为"+sxid+"的披露内容:"+plnr);
		}
		return removeFormData;
	}

	public boolean itemBcRemove(Long bcinstanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(bcinstanceid);
		boolean removeFormData=false;
		removeFormData = DemAPI.getInstance().removeFormData(bcinstanceid);
		if(removeFormData){
			String sxid=fromData.get("SXID").toString();
			String plbc=fromData.get("PLBC").toString();
			Long dataId = Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "备查文件信息维护", "删除信披事项ID为"+sxid+"的备查文件内容:"+plbc);
		}
		return removeFormData;
	}

	public boolean itemStepRemove(Long bzinstanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(bzinstanceid);
		boolean removeFormData=false;
		removeFormData = DemAPI.getInstance().removeFormData(bzinstanceid);
		if(removeFormData){
			String sxid=fromData.get("SXID").toString();
			String bznr=fromData.get("BZNR").toString();
			Long dataId = Long.parseLong(fromData.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "信披事项步骤信息维护", "删除信披事项ID为"+sxid+"的信披步骤内容:"+bznr);
		}
		return removeFormData;
	}

	public List<String> getItemTitleList() {
		StringBuffer sql=new StringBuffer("SELECT SXMC FROM BD_XP_XPSXB GROUP BY SXMC ORDER BY SXMC");
		List<String> list = zqbAnnouncementDAO.getItemTitleList(sql.toString());
		return list;
	}

	public void itemMoveUp(Long itemDemId, Long itemFormId, Long sxinstanceid) {
		SysEngineIform sysEngineIformModel = sysEngineIFormDAO.getSysEngineIformModel(itemFormId);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIformModel.getMetadataid());
		String entityname = metadataModel.getEntityname();
		zqbAnnouncementDAO.itemMoveUp(entityname,itemDemId,sxinstanceid,"up");
	}

	public void itemMoveDown(Long itemDemId, Long itemFormId, Long sxinstanceid) {
		SysEngineIform sysEngineIformModel = sysEngineIFormDAO.getSysEngineIformModel(itemFormId);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIformModel.getMetadataid());
		String entityname = metadataModel.getEntityname();
		zqbAnnouncementDAO.itemMoveUp(entityname,itemDemId,sxinstanceid,"down");
	}

	public void itemMoveTop(Long itemDemId, Long itemFormId, Long sxinstanceid) {
		SysEngineIform sysEngineIformModel = sysEngineIFormDAO.getSysEngineIformModel(itemFormId);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIformModel.getMetadataid());
		String entityname = metadataModel.getEntityname();
		zqbAnnouncementDAO.itemMoveUp(entityname,itemDemId,sxinstanceid,"top");
	}

	public void itemMoveBottom(Long itemDemId, Long itemFormId,Long sxinstanceid) {
		SysEngineIform sysEngineIformModel = sysEngineIFormDAO.getSysEngineIformModel(itemFormId);
		SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIformModel.getMetadataid());
		String entityname = metadataModel.getEntityname();
		zqbAnnouncementDAO.itemMoveUp(entityname,itemDemId,sxinstanceid,"bottom");
	}
	public List<HashMap> getXpsxlist(String roleid,String zqjc, String zqdm, String xpsxname, String noticename, String noticetype, String noticedatestart, String noticedateend, int pageNumber, int pageSize) {
		return zqbAnnouncementDAO.getXpsxlist(roleid,zqjc,zqdm,xpsxname,noticename,noticetype,noticedatestart,noticedateend,pageNumber,pageSize);
	}
	public List<HashMap> getXpsxlistSize(String roleid,String zqjc, String zqdm, String xpsxname, String noticename, String noticetype, String noticedatestart, String noticedateend) {
		return zqbAnnouncementDAO.getXpsxlistSize(roleid,zqjc,zqdm,xpsxname,noticename,noticetype,noticedatestart,noticedateend);
	}

	public List<HashMap> getXpsxList() {
		return zqbAnnouncementDAO.getXpsxListModel();
	}
	public String getXpsxCompanyList(){
		return zqbAnnouncementDAO.getXpsxCompanyList();
	}

	public List<HashMap> getXpsxById(String id) {
		return zqbAnnouncementDAO.getXpsxById(id);
	}
	public List<HashMap> getGlhyById(String id) {
		return zqbAnnouncementDAO.getGlhyById(id);
	}
	public HashMap getMeetingMap(Long instanceid){
		return DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
	}
	public String getQtFileList(String  id){
		Map map=new HashMap();
		map.put(1, id);
		StringBuffer sql=new StringBuffer();
		sql.append(" select t.instanceid from sys_engine_form_bind t where t.metadataid=(select s.id from sys_engine_metadata s  where s.entitytitle='会议计划表单') and t.dataid= ?  ");
		String insid=DBUTilNew.getDataStr("instanceid", sql.toString(), map);
		Long instanceid=Long.parseLong(insid);
		StringBuffer html = new StringBuffer();
		
		HashMap hash = this.getMeetingMap(instanceid);
		if(hash!=null&&hash.get("FJ")!=null){
			String fj = hash.get("FJ").toString();
			if(!fj.equals("")){ 
				List<FileUpload> list =  uploadifyService.getFileUploads(ZqbAnnouncementService.class, fj);
				html.append("<table class=\"filelist\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">");
				int count = 0;
				for(FileUpload upload:list){
					count++;
					html.append("<tr>");
					//html.append("<td>").append(count).append("</td>");
					String icon = "";
					String suffix = FileUtil.getFileExt(upload.getFileSaveName());
					icon = WebUIUtil.getLinkIcon(suffix);
					html.append("<td><a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+upload.getFileId()+"\"><span>").append(icon).append("</span>").append(upload.getFileSrcName()).append("</a></td>");
					//html.append("<td>").append(upload.getUploadTime()).append("</td>");
					if(upload.getMemo()==null)upload.setMemo("");
					html.append("<td>").append(upload.getMemo()).append("</td>");				
					html.append("</tr>");
				}
				html.append("</table>");
			} 
		}
		return html.toString();
	}
	public String getFileList(String  id){
		Map map=new HashMap();
		map.put(1, id);
		StringBuffer sql=new StringBuffer();
		sql.append(" select t.instanceid from sys_engine_form_bind t where t.metadataid=(select s.id from sys_engine_metadata s  where s.entitytitle='会议计划表单') and t.dataid= ?  ");
		String insid=DBUTilNew.getDataStr("instanceid", sql.toString(), map);
		Long instanceid=Long.parseLong(insid);
		StringBuffer html = new StringBuffer();
		
		HashMap hash = this.getMeetingMap(instanceid);
		if(hash!=null&&hash.get("YAZLFJ")!=null){
			String fj = hash.get("YAZLFJ").toString();
			if(!fj.equals("")){ 
				List<FileUpload> list =  uploadifyService.getFileUploads(ZqbAnnouncementService.class, fj);
				html.append("<table class=\"filelist\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">");
				int count = 0;
				for(FileUpload upload:list){
					count++;
					html.append("<tr>");
					//html.append("<td>").append(count).append("</td>");
					String icon = "";
					String suffix = FileUtil.getFileExt(upload.getFileSaveName());
					icon = WebUIUtil.getLinkIcon(suffix);
					html.append("<td><a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+upload.getFileId()+"\"><span>").append(icon).append("</span>").append(upload.getFileSrcName()).append("</a></td>");
					//html.append("<td>").append(upload.getUploadTime()).append("</td>");
					if(upload.getMemo()==null)upload.setMemo("");
					html.append("<td>").append(upload.getMemo()).append("</td>");				
					html.append("</tr>");
				}
				html.append("</table>");
			} 
		}
		return html.toString();
	}
	public StringBuffer getStr(List lables,String tablename,String id,String tag) {
		Map params = new HashMap();
		params.put(1, id);
		List<HashMap> lnr = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT ROWNUM AS RN,"+tag+" AS STR FROM "+tablename+" WHERE SXID= ?  ORDER BY ORDERINDEX", params);
		StringBuffer sbnr = new StringBuffer();
		for (HashMap hashnr : lnr) {
			sbnr.append(hashnr.get("RN")).append("、").append(hashnr.get("STR")).append("</br>");
		}
		return sbnr;
	}
	public String getTypeList(String id,String nr) {
		StringBuffer html  = new StringBuffer();
		List<HashMap> list = zqbAnnouncementDAO.getTypeList(id,nr);
		int i=1;
		if(list.size()==1){
			html.append(list.get(0).get("NR")==null?"":list.get(0).get("NR").toString());
		}else{
			for (HashMap nrmap : list) {
				html.append(i+"、"+nrmap.get("NR").toString()+"</br>");
				i++;
			}
		}
		return html.toString();
	}
	
	public String getTypeStr(String id) {
		return zqbAnnouncementDAO.getTypeStr(id,"备查");
	}
	
	public String XpsxGgGetbcfile(String id) {
		if(id==null||id.equals("")){
			id="-1";
		}
		return zqbAnnouncementDAO.XpsxGgGetbcfile(id);
	}
	public String Xpsxtgetthisname(String id) {
		StringBuffer xpsxname = new StringBuffer();
		if(id!=null&&!id.equals("")){
			StringBuffer sb = new StringBuffer("");
			sb.append("SELECT ID,SXMC FROM BD_XP_XPSXB WHERE ID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
			PreparedStatement ps = null;
			int i=0;
			Connection conn=null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				ps = conn.prepareStatement(sb.toString());
				int n=1;
				for (int j = 0; j < ids.length; j++) {
					if(isInteger( ids[j])){
						ps.setString(n, ids[j]);
						n++;
					}else{
						return "";
					}
				}
				rs = ps.executeQuery();
				while(rs.next()){
					/*if(i!=0){
						xpsxname.append("</br>");
					}
					i++;
					xpsxname.append(i+"、<a href='javascript:checkXpsx("+rs.getString(1)+")'>"+rs.getString(2)+"</a>");*/
					i++;
					int bs=i;
					xpsxname.append("<div id='divs"+i+"'>");
					xpsxname.append("<a id='ycas"+i+"' href='javascript:checkXpsx("+rs.getString(1)+")'>"+rs.getString(2)+"</a>");
					xpsxname.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("<a id='dels"+i+"' href='#' onclick='dels("+i+","+rs.getString(1)+",\""+rs.getString(2)+"\")'><img src='/iwork_img/del3.gif'/></a>");
					//.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("<img src='/iwork_img/del3.gif'/>")
					xpsxname.append("</div >");
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
		}
		return xpsxname.toString();
	}
	/**
	 * 判断是否数字
	 * @param value
	 * @return
	 */
	 public static boolean isInteger(String value) {
		  try {
		   Integer.parseInt(value);
		   return true;
		  } catch (NumberFormatException e) {
		   return false;
		  }
	 }
	/**
	 * 三会
	 * @param id
	 * @return
	 */
	public String Glhygetthisname(String id) {
		StringBuffer xpsxname = new StringBuffer();
		if(id!=null&&!id.equals("")){
			StringBuffer sb = new StringBuffer("");
			sb.append("SELECT ID,MEETNAME FROM BD_MEET_PLAN WHERE ID IN(");
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
			PreparedStatement ps = null;
			int i=0;
			Connection conn=null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				ps = conn.prepareStatement(sb.toString());
				for (int j = 0; j < ids.length; j++) {
					ps.setString(j+1, ids[j]);
				}
				rs = ps.executeQuery();
				while(rs.next()){
					
					i++;
					int bs=i;
					xpsxname.append("<div id='div"+i+"'>");
					xpsxname.append("<a id='yca"+i+"' href='javascript:checkGlhy("+rs.getString(1)+")'>"+rs.getString(2)+"</a>");
					xpsxname.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("<a id='del"+i+"' href='#' onclick='del("+i+","+rs.getString(1)+",\""+rs.getString(2)+"\")'><img src='/iwork_img/del3.gif'/></a>");
					//.append("&nbsp;&nbsp;&nbsp;&nbsp;").append("<img src='/iwork_img/del3.gif'/>")
					xpsxname.append("</div >");
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
		}
		return xpsxname.toString();
	}
	public List<HashMap> getZqdmList(String roleid) {
		return zqbAnnouncementDAO.getZqdmList(roleid);
	}
	public void itemImp(String fileUUID) {
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(fileUUID);
		OrgUser user =UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
		//事项formId、DemId
		Long itemFormId=getConfig("itemformid");
		Long itemDemId=getConfig("itemdemid");
		//披露内容formId、DemId
		Long itemContentFormId=getConfig("itemcontentformid");
		Long itemContentDemId=getConfig("itemcontentdemid");
		//备查文件formId、DemId
		Long itemBcFormId=getConfig("itembcformid");
		Long itemBcDemId=getConfig("itembcdemid");
		//事项步骤formId、DemId
		Long itemStepFormId=getConfig("itemstepformid");
		Long itemStepDemId=getConfig("itemstepdemid");
		for (FileUpload fileUpload : sublist) {
			File file = new File(rootPath+File.separator+fileUpload.getFileUrl());
	        try {
	            Workbook workBook = null;  
	            try {
	            	workBook = new XSSFWorkbook(rootPath+File.separator+fileUpload.getFileUrl());  
	            }
	            catch (Exception ex) {
	            	workBook = new HSSFWorkbook(new FileInputStream(rootPath+File.separator+fileUpload.getFileUrl()));
	            }   
	            for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
	            	Sheet sheet = workBook.getSheetAt(numSheet);  
	                if (sheet == null) {  
	                    continue;  
	                }  
	                // 循环行Row  
	                for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
	                    Row row = sheet.getRow(rowNum);  
	                    if (row == null) {
	                        continue;  
	                    }  
	                    // 循环列Cell
	                    short lastCellNum = row.getLastCellNum();
	                    if(lastCellNum>=6){
	                    	HashMap itemMap=new HashMap();
                    		Cell cell = row.getCell(0);
                    		String sxmc = getValue(cell);
                    		if(sxmc!=null&&!sxmc.equals("")&&!sxmc.equals("-")){
                    			itemMap.put("SXMC", sxmc);
                        		cell = row.getCell(1);
                        		String sygz = getValue(cell);
                        		if(sygz.equals("-")){
                        			sygz="";
                        		}
                        		itemMap.put("SYGZ", sygz);
                        		cell = row.getCell(2);
                        		String plyq = getValue(cell);
                        		if(plyq.equals("-")){
                        			plyq="";
                        		}
                        		itemMap.put("PLYQ", plyq);
                        		itemMap.put("CREATEUSER", SecurityUtil.supermanager);
                        		Long newInstance = DemAPI.getInstance().newInstance(itemDemId, SecurityUtil.supermanager);
                        		boolean saveFlag = DemAPI.getInstance().saveFormData(itemDemId, newInstance, itemMap, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
                        		if(saveFlag){
                        			HashMap itemData = DemAPI.getInstance().getFromData(newInstance);
                        			Long itemId = Long.parseLong(itemData.get("ID").toString());
                        			itemData.put("ORDERINDEX", itemId);
                        			DemAPI.getInstance().updateFormData(itemDemId, newInstance, itemData, itemId, false);
                        			
                        			cell = row.getCell(3);
                        			String plnr = getValue(cell);
                        			if(plnr!=null&&!plnr.equals("")&&!plnr.equals("-")){
                        				String[] plnrArr = plnr.split("\n");
                        				for (String str : plnrArr) {
                        					str = subString(str,"\\(\\d*\\)");
                        					HashMap itemContentMap=new HashMap();
                        					itemContentMap.put("SXID", itemId);
                        					itemContentMap.put("PLNR", str);
                        					itemContentMap.put("CREATEUSER", SecurityUtil.supermanager);
                        					Long plnrInstanceId = DemAPI.getInstance().newInstance(itemContentDemId, SecurityUtil.supermanager);
                        					boolean contentFlag = DemAPI.getInstance().saveFormData(itemContentDemId, plnrInstanceId, itemContentMap, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
                        					if(contentFlag){
                                    			HashMap itemContentData = DemAPI.getInstance().getFromData(plnrInstanceId);
                                    			Long itemContentId = Long.parseLong(itemContentData.get("ID").toString());
                                    			itemContentData.put("ORDERINDEX", itemContentId);
                                    			DemAPI.getInstance().updateFormData(itemContentDemId, plnrInstanceId, itemContentData, itemContentId, false);
                                    		}
                        				}
                        			}
                        			
                        			cell = row.getCell(4);
                        			String bcwj = getValue(cell);
                        			if(bcwj!=null&&!bcwj.equals("")&&!bcwj.equals("-")){
                        				String[] bcwjArr = bcwj.split("\n");
                        				for (String str : bcwjArr) {
                        					str = subString(str,"\\(\\d*\\)");
                        					HashMap itemBcMap=new HashMap();
                        					itemBcMap.put("SXID", itemId);
                        					itemBcMap.put("PLBC", str);
                        					itemBcMap.put("CREATEUSER", SecurityUtil.supermanager);
                        					Long bcwjInstanceId = DemAPI.getInstance().newInstance(itemBcDemId, SecurityUtil.supermanager);
                        					boolean bcFlag = DemAPI.getInstance().saveFormData(itemBcDemId, bcwjInstanceId, itemBcMap, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
                        					if(bcFlag){
                                    			HashMap itemBcData = DemAPI.getInstance().getFromData(bcwjInstanceId);
                                    			Long itemBcId = Long.parseLong(itemBcData.get("ID").toString());
                                    			itemBcData.put("ORDERINDEX", itemBcId);
                                    			DemAPI.getInstance().updateFormData(itemBcDemId, bcwjInstanceId, itemBcData, itemBcId, false);
                                    		}
    									}
                        			}
                        			
                        			cell = row.getCell(5);
                        			String sxbz = getValue(cell);
                        			if(sxbz!=null&&!sxbz.equals("")&&!sxbz.equals("-")){
                        				String[] sxbzArr = sxbz.split("\n");
                        				for (String str : sxbzArr) {
                        					str = subString(str,"(\\d*)、");
                        					HashMap itemBzMap=new HashMap();
                        					itemBzMap.put("SXID", itemId);
                        					itemBzMap.put("BZNR", str);
                        					itemBzMap.put("CREATEUSER", SecurityUtil.supermanager);
                        					Long bzInstanceId = DemAPI.getInstance().newInstance(itemStepDemId, SecurityUtil.supermanager);
                        					boolean bzFlag = DemAPI.getInstance().saveFormData(itemStepDemId, bzInstanceId, itemBzMap, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
                        					if(bzFlag){
                                    			HashMap itemBzData = DemAPI.getInstance().getFromData(bzInstanceId);
                                    			Long itemBzId = Long.parseLong(itemBzData.get("ID").toString());
                                    			itemBzData.put("ORDERINDEX", itemBzId);
                                    			DemAPI.getInstance().updateFormData(itemStepDemId, bzInstanceId, itemBzData, itemBzId, false);
                                    		}
    									}
                        			}
                        		}
                    		}
                    		
	                    }
	                }
	                break;
	            }  
	        } catch (Exception e) {  
	           logger.error(e,e);
	        }  
		}
	}
	
	private String getValue(Cell cell) {
		if(cell!=null){
			if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
				return String.valueOf(cell.getNumericCellValue());
			} else {
				return String.valueOf(cell.getStringCellValue());  
			}
		}else{
			return "";
		}
    }
	
	public String qj2bj(String src) {  
        if (src == null) {  
            return src;  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < src.length(); i++) {  
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内  
                buf.append((char) (ca[i] - CONVERT_STEP));  
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格  
                buf.append(DBC_SPACE);  
            } else { // 不处理全角空格，全角！到全角～区间外的字符  
                buf.append(ca[i]);  
            }  
        }  
        return buf.toString();  
    }
	
	public String subString(String content,String match){
		String qj2bj = qj2bj(content);
        int beginIndex=0;
        Pattern p = Pattern.compile(match);
        Matcher m = p.matcher(qj2bj);
        while(m.find()){
        	beginIndex=m.end();
        	break;
        }
        String string = qj2bj.substring(beginIndex, qj2bj.length());
		return string;
	}
	//opinionList = processOpinionService.getProcessInstanceOpinionTimerLineList(actDefId, prcDefId.longValue(), actStepDefId, instanceId.longValue());
	public List<HashMap<String, Object>> getXpqaList(String zqjc, String zqdm,String startdate, String enddate) {
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		
		Long roleid =userModel.getOrgroleid();
	
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT ZQJC,ZQDM,KHBH,TO_CHAR(XPPF,'FM9999990.00') XPPF FROM (SELECT KH.ZQJC,KH.ZQDM,KHBH,XPPF FROM (SELECT KHBH,CAST(DECODE(AVG(GGDF),0,'0.00',TRIM(TO_CHAR(AVG(GGDF),'9999999.99'))) AS NUMBER(9,2)) AS XPPF FROM BD_MEET_QTGGZL WHERE GGDF IS NOT NULL ");
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') >= ?");
			parameter.add(startdate);
		}else{
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') >= TO_CHAR(ADD_MONTHS(SYSDATE,-6),'yyyy-MM-dd')");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}else{
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') <= TO_CHAR(SYSDATE,'yyyy-MM-dd') ");
		}
		sql.append(" AND SPZT='审批通过' GROUP BY KHBH) GG INNER JOIN (SELECT ZQDM,ZQJC,CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE 1=1 ");
		if(zqjc!=null&&!zqjc.equals("")){
			sql.append(" AND UPPER(ZQJC) like ?");
			parameter.add("%"+zqjc.toUpperCase()+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sql.append(" AND UPPER(ZQDM) like ?");
			parameter.add("%"+zqdm.toUpperCase()+"%");
		}
		sql.append(" ) KH ON GG.KHBH=KH.CUSTOMERNO ");//ORDER BY XPPF DESC,ZQDM)
		String username=userModel.getUserid()+"["+userModel.getUsername()+"]";
		if(roleid==4){
			sql.append(" AND  GG.KHBH IN ( select fp.khbh khbh from BD_MDM_KHQXGLB fp,bd_zqb_kh_base kh where fp.khbh=kh.customerno AND ( KHFZR=? or ZZCXDD=? OR FHSPR=?  OR ZZSPR=? OR GGFBR=?))");
			parameter.add(username);
			parameter.add(username);
			parameter.add(username);
			parameter.add(username);
			parameter.add(username);
		}
	    sql.append("ORDER BY XPPF DESC,ZQDM)");
		
		
		
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getXpqaList(sql.toString(),parameter);
		return list;
	}
	public void ygjxExcel(HttpServletResponse response,String fullname,String model,String orderby,String startdate,String enddate){
		OutputStream out1 = null;
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(startdate.substring(0, 10)+"到"+enddate.substring(0, 10)+"员工绩效情况汇总");
		
	
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
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
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
		
		cell.setCellValue("被减分人");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		
		cell.setCellValue("所属部门");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		
		cell.setCellValue("总减分");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		
		cell.setCellValue("减分详情");
		cell.setCellStyle(style);
	    cell = row.createCell((short) 4);
	    
		cell.setCellValue("减分详情");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		
		cell.setCellValue("减分详情");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		
		cell.setCellValue("减分详情");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		
		cell.setCellValue("减分详情");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
	
		
		HSSFRow row1 = sheet.createRow(1);
		HSSFCell c5 = row1.createCell(3);
		c5.setCellValue(new HSSFRichTextString("减分人"));
		c5.setCellStyle(style2);
		
		HSSFCell c6 = row1.createCell(4);
		c6.setCellValue(new HSSFRichTextString("减分"));
		c6.setCellStyle(style2);
		
		HSSFCell c7 = row1.createCell(5);
		c7.setCellValue(new HSSFRichTextString("发生模块"));
		c7.setCellStyle(style2);
		
		HSSFCell c8 = row1.createCell(6);
		c8.setCellValue(new HSSFRichTextString("扣分日期"));
		c8.setCellStyle(style2);
		
		HSSFCell c9 = row1.createCell(7);
		c9.setCellValue(new HSSFRichTextString("扣分类型"));
		c9.setCellStyle(style2);
	
		Region region1 = new Region(0, (short)0, 1, (short)0);
        Region region2 = new Region(0, (short)1, 1, (short)1);
        Region region3 = new Region(0, (short)2, 1, (short)2);
        Region region4 = new Region(0, (short)3, 0, (short)7);
   
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        
		List list = zqbAnnouncementDAO.getYgjxList(fullname,model,orderby,startdate,enddate);
		int n = 2;
		if (list == null) {
			return;
		}
		int m = 0;
		Map person = new HashMap();
		for (int i = 0; i < list.size(); i++) {
				Map map = (HashMap) list.get(i);
				row = sheet.createRow((int) n++);

				
				HSSFCell cell1 = row.createCell((short) 0);
				cell1.setCellValue(map.get("FULLNAME").toString());
				cell1.setCellStyle(style2);
				// 如何记录已显示人员的map里没有记录，或者不等于当前的用户
				if (person.get("FULLNAME") == null|| !person.get("FULLNAME").toString().equals((map.get("FULLNAME") != null ? map.get("FULLNAME").toString() : ""))) {
					// 单元格合并
					// 四个参数分别是：起始行，起始列，结束行，结束列
					if (person.get("FULLNAME") != null) {
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 0, n - 2,(short) 0));
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 1, n - 2,(short) 1));
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 2, n - 2,(short) 2));
					}
					person.put("FULLNAME",	(map.get("FULLNAME") != null ? map.get("FULLNAME").toString() : "")	);
					person.put("begin", n - 1);
					// 再把式样设置到cell中：
				}
				
				
				
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("DEPARTMENTNAME").toString());
				cell2.setCellStyle(style2);
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("ZFS").toString()==null?"":map.get("ZFS").toString());
				cell3.setCellStyle(style2);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("KFRXM").toString());
				cell4.setCellStyle(style2);
				
			
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("ZF").toString());
				cell6.setCellStyle(style2);
				HSSFCell cell7 = row.createCell((short) 5);
				cell7.setCellValue(map.get("FSMK").toString()==null?"":map.get("FSMK").toString());
				cell7.setCellStyle(style1);
				
				HSSFCell cell8 = row.createCell((short) 6);
				cell8.setCellValue(map.get("KFSJ").toString());
				cell8.setCellStyle(style2);
				
				HSSFCell cell9 = row.createCell((short) 7);
				cell9.setCellValue(map.get("KFLB").toString());
				cell9.setCellStyle(style1);
				m++;
			
		}
		if(list!=null  && list.size()>0){
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 0, n - 1, (short) 0));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 1, n - 1, (short) 1));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 2, n - 1, (short) 2));
		}
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 4500);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 15000);
		sheet.setColumnWidth(6, 6500);
		sheet.setColumnWidth(7, 7000);
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(startdate.substring(0, 10)+"到"+enddate.substring(0, 10)+"员工绩效情况汇总.xls");
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
	 * 信披质量评估导出
	 * @param response
	 * @param startdate
	 * @param enddate
	 */
	public void pxjlexcl(HttpServletResponse response,String zqdm,String zqjc,String startdate,String enddate){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(startdate+"到"+enddate+"企业信披质量评估");
		
	
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
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
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
		
		cell.setCellValue("证券\n代码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		
		cell.setCellValue("证券简称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		
		cell.setCellValue("信披平均分");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		
		cell.setCellValue("公告编号");
		cell.setCellStyle(style);
	    cell = row.createCell((short) 4);
	    
		cell.setCellValue("公告名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		
		cell.setCellValue("公告日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		
		cell.setCellValue("公告\n得分");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		
		cell.setCellValue("扣分详情");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("");
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("");
		cell.setCellStyle(style);
		cell = row.createCell((short) 11);
		cell.setCellValue("");
		cell.setCellStyle(style);
		cell = row.createCell((short) 12);
		cell.setCellValue("流程意见");
		cell.setCellStyle(style);
		cell = row.createCell((short) 13);
		cell.setCellValue("流程意见");
		cell.setCellStyle(style);
		cell = row.createCell((short) 14);
		cell.setCellValue("流程意见");
		cell.setCellStyle(style);
		cell = row.createCell((short) 15);
		cell.setCellValue("流程意见");
		cell.setCellStyle(style);
		cell = row.createCell((short) 16);
		cell.setCellValue("流程意见");
		cell.setCellStyle(style);
		cell = row.createCell((short) 17);
	
		
		HSSFRow row1 = sheet.createRow(1);
		HSSFCell c5 = row1.createCell(7);
		c5.setCellValue(new HSSFRichTextString("扣分人"));
		c5.setCellStyle(style2);
		
		HSSFCell c6 = row1.createCell(8);
		c6.setCellValue(new HSSFRichTextString("被扣分人"));
		c6.setCellStyle(style2);
		
		HSSFCell c7 = row1.createCell(9);
		c7.setCellValue(new HSSFRichTextString("扣分"));
		c7.setCellStyle(style2);
		
		HSSFCell c8 = row1.createCell(10);
		c8.setCellValue(new HSSFRichTextString("扣分时间"));
		c8.setCellStyle(style2);
		
		HSSFCell c9 = row1.createCell(11);
		c9.setCellValue(new HSSFRichTextString("扣分类型"));
		c5.setCellStyle(style2);
		
		HSSFCell c10 = row1.createCell(12);
		c10.setCellValue(new HSSFRichTextString("任务名称"));
		c10.setCellStyle(style2);
		
		HSSFCell c11 = row1.createCell(13);
		c11.setCellValue(new HSSFRichTextString("操作"));
		c11.setCellStyle(style2);
		
		HSSFCell c12 = row1.createCell(14);
		c12.setCellValue(new HSSFRichTextString("意见描述"));
		c12.setCellStyle(style2);
		
		HSSFCell c15 = row1.createCell(15);
		c15.setCellValue(new HSSFRichTextString("办理人"));
		c15.setCellStyle(style2);
		
		HSSFCell c16 = row1.createCell(16);
		c16.setCellValue(new HSSFRichTextString("办理时间"));
		c16.setCellStyle(style2);
		
		Region region1 = new Region(0, (short)0, 1, (short)0);
        Region region2 = new Region(0, (short)1, 1, (short)1);
        Region region3 = new Region(0, (short)2, 1, (short)2);
        Region region4 = new Region(0, (short)3, 1, (short)3);
        Region region5 = new Region(0, (short)4, 1, (short)4);
        Region region6 = new Region(0, (short)5, 1, (short)5);
        Region region7 = new Region(0, (short)6, 1, (short)6);
        Region region8 = new Region(0, (short)7, 0, (short)11);
        Region region9 = new Region(0, (short)12, 0, (short)16);
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        sheet.addMergedRegion(region5);
        sheet.addMergedRegion(region6);
        sheet.addMergedRegion(region7);
        sheet.addMergedRegion(region8);
        sheet.addMergedRegion(region9);
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(startdate!=null&&!startdate.equals(""))
				 stdate= sdf.parse(startdate);
			 if(enddate!=null&&!enddate.equals(""))
				 endate=  sdf.parse(enddate);
		} catch (ParseException e) {
			logger.error(e,e);
		}*/
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		//获取当前用户权限
		List list = zqbAnnouncementDAO.getPxjlList(zqdm,zqjc,startdate,enddate);
		int n = 2;
		int z = 2;
		if (list == null) {
			return;
		}
		int m = 0;
		Map person = new HashMap();
		for (int i = 0; i < list.size(); i++) {
				Map map = (HashMap) list.get(i);
				row = sheet.createRow((int) n++);
				// 第四步，创建单元格，并设置值
				HSSFCell cell1 = row.createCell((short) 0);
				cell1.setCellValue(map.get("ZQDM").toString());
				cell1.setCellStyle(style2);
				
				/*if (person.get("ZQDM") == null|| !person.get("ZQDM").toString().equals((map.get("ZQDM") != null ? map.get("ZQDM").toString() : ""))) {
					// 单元格合并
					// 四个参数分别是：起始行，起始列，结束行，结束列
					if (person.get("ZQDM") != null) {
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 0, n - 2,(short) 0));
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 1, n - 2,(short) 1));
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 2, n - 2,(short) 2));
					}
					person.put("ZQDM",	(map.get("ZQDM") != null ? map.get("ZQDM").toString() : "")	);
					person.put("begin", n - 1);
					// 再把式样设置到cell中：
				}*/
				
				
				
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("ZQJC").toString());
				cell2.setCellStyle(style2);
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("XPPF").toString());
				cell3.setCellStyle(style2);
				
				
				
				
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("NOTICENO").toString());
				cell4.setCellStyle(style1);
				
				/*if (person.get("NOTICENO") == null|| !person.get("NOTICENO").toString().equals((map.get("NOTICENO") != null ? map.get("NOTICENO").toString() : ""))) {
					// 单元格合并
					// 四个参数分别是：起始行，起始列，结束行，结束列
					if (person.get("NOTICENO") != null) {
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins").toString()), (short) 3, n - 2,(short) 3));
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins").toString()), (short) 4, n - 2,(short) 4));
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins").toString()), (short) 5, n - 2,(short) 5));
						sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins").toString()), (short) 6, n - 2,(short) 6));
					}
					person.put("NOTICENO",	(map.get("NOTICENO") != null ? map.get("NOTICENO").toString() : "")	);
					person.put("begins", n - 1);
					// 再把式样设置到cell中：
				}*/
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("NOTICENAME").toString());
				cell6.setCellStyle(style1);
				HSSFCell cell7 = row.createCell((short) 5);
				cell7.setCellValue(map.get("NOTICEDATE").toString());
				cell7.setCellStyle(style2);
				
				HSSFCell cell8 = row.createCell((short) 6);
				cell8.setCellValue(map.get("Ggdf").toString());
				cell8.setCellStyle(style2);
				HSSFCell cell9 = row.createCell((short) 7);
				cell9.setCellValue(map.get("FKRID").toString());
				cell9.setCellStyle(style2);
				HSSFCell cell10 = row.createCell((short) 8);
				cell10.setCellValue(map.get("BFKRID").toString());
				cell10.setCellStyle(style2);
				HSSFCell cell11 = row.createCell((short) 9);
				cell11.setCellValue(map.get("FS").toString());
				cell11.setCellStyle(style2);
				HSSFCell cell12 = row.createCell((short) 10);
				cell12.setCellValue(map.get("KFSJ").toString());
				cell12.setCellStyle(style1);
				HSSFCell cell13 = row.createCell((short) 11);
				cell13.setCellValue(map.get("TYPE").toString().replaceAll(",", "\n"));
				cell13.setCellStyle(style1);
				HSSFCell cell14 = row.createCell((short) 12);
				cell14.setCellValue(map.get("STEP_TITLE").toString()==null?"":map.get("STEP_TITLE").toString());
				cell14.setCellStyle(style1);
				HSSFCell cell15 = row.createCell((short) 13);
				cell15.setCellValue(map.get("ACTION").toString());
				cell15.setCellStyle(style1);
				HSSFCell cell16 = row.createCell((short) 14);
				cell16.setCellValue(map.get("CONTENT").toString()==null?"":map.get("CONTENT").toString());
				cell16.setCellStyle(style1);
				HSSFCell cell17 = row.createCell((short) 15);
				cell17.setCellValue(map.get("USERNAME").toString());
				cell17.setCellStyle(style1);
				HSSFCell cell18 = row.createCell((short) 16);
				cell18.setCellValue(map.get("CREATETIME").toString());
				cell18.setCellStyle(style1);
				m++;
			
		}
		/*if(list!=null  && list.size()>0){
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 0, n - 1, (short) 0));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 1, n - 1, (short) 1));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 2, n - 1, (short) 2));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins")==null?"0":person.get("begin").toString()), (short) 3, n - 1, (short) 3));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins")==null?"0":person.get("begin").toString()), (short) 4, n - 1, (short) 4));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins")==null?"0":person.get("begin").toString()), (short) 5, n - 1, (short) 5));
			sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begins")==null?"0":person.get("begin").toString()), (short) 6, n - 1, (short) 6));
		}*/
		if(list!= null && list.size()>0){
		int sheetLine=sheet.getLastRowNum();//获得总行数
		String lingyu = sheet.getRow(2).getCell(0).getStringCellValue();
        short j = 1;
        short k = 2;
        for(short i=3;i<sheetLine-1;i++){
        	if(i==(short)sheetLine){
       		 
        	}
            if(lingyu.equals(sheet.getRow(i).getCell(0).getStringCellValue())){
                j++;
            }else{
                //从k开始合并，合并j个
                CellRangeAddress cellRangeAddressJF = new CellRangeAddress(k, k+j-1, 0, 0);
                CellRangeAddress cellRangeAddressJF1 = new CellRangeAddress(k, k+j-1, 1, 1);
                CellRangeAddress cellRangeAddressJF2 = new CellRangeAddress(k, k+j-1, 2, 2);
                RegionUtil.setBorderLeft(1, cellRangeAddressJF, sheet, wb);  
                RegionUtil.setBorderBottom(1, cellRangeAddressJF, sheet, wb);  
                RegionUtil.setBorderRight(1, cellRangeAddressJF, sheet, wb);  
                RegionUtil.setBorderTop(1, cellRangeAddressJF, sheet, wb);
                sheet.addMergedRegion(cellRangeAddressJF);      
                sheet.addMergedRegion(cellRangeAddressJF1);  
                sheet.addMergedRegion(cellRangeAddressJF2);  
                lingyu=sheet.getRow(i).getCell(0).getStringCellValue();
      
                k=(short) (k+j);
                j=1;
            }
            if(i==(short)sheetLine-2){
            	j=(short)(j+2);
            	 CellRangeAddress cellRangeAddressJF = new CellRangeAddress(k, k+j-1, 0, 0);
                 CellRangeAddress cellRangeAddressJF1 = new CellRangeAddress(k, k+j-1, 1, 1);
                 CellRangeAddress cellRangeAddressJF2 = new CellRangeAddress(k, k+j-1, 2, 2);
                 RegionUtil.setBorderLeft(1, cellRangeAddressJF, sheet, wb);  
                 RegionUtil.setBorderBottom(1, cellRangeAddressJF, sheet, wb);  
                 RegionUtil.setBorderRight(1, cellRangeAddressJF, sheet, wb);  
                 RegionUtil.setBorderTop(1, cellRangeAddressJF, sheet, wb);
                 sheet.addMergedRegion(cellRangeAddressJF);      
                 sheet.addMergedRegion(cellRangeAddressJF1);  
                 sheet.addMergedRegion(cellRangeAddressJF2);  
        	}
        } 
        lingyu = sheet.getRow(2).getCell(3).getStringCellValue();
        j = 1;
        k = 2;
       for(short i=3;i<sheetLine;i++){
       	if(i==(short)sheetLine-1){
       		 CellRangeAddress cellRangeAddressJF = new CellRangeAddress(k, k+j-1, 3, 3);
       	}
           if(lingyu.equals(sheet.getRow(i).getCell(3).getStringCellValue())){
               j++;
           }else{
               //从k开始合并，合并j个
               CellRangeAddress cellRangeAddressJF = new CellRangeAddress(k, k+j-1, 3, 3);
               CellRangeAddress cellRangeAddressJF1 = new CellRangeAddress(k, k+j-1, 4, 4);
               CellRangeAddress cellRangeAddressJF2 = new CellRangeAddress(k, k+j-1, 5, 5);
               CellRangeAddress cellRangeAddressJF3 = new CellRangeAddress(k, k+j-1, 6, 6);
               RegionUtil.setBorderLeft(1, cellRangeAddressJF, sheet, wb);  
               RegionUtil.setBorderBottom(1, cellRangeAddressJF, sheet, wb);  
               RegionUtil.setBorderRight(1, cellRangeAddressJF, sheet, wb);  
               RegionUtil.setBorderTop(1, cellRangeAddressJF, sheet, wb);
               sheet.addMergedRegion(cellRangeAddressJF); 
               sheet.addMergedRegion(cellRangeAddressJF1);
               sheet.addMergedRegion(cellRangeAddressJF2);
               sheet.addMergedRegion(cellRangeAddressJF3);
               lingyu=sheet.getRow(i).getCell(3).getStringCellValue();
     
               k=(short) (k+j);
               j=1;
           }
           if(i==(short)sheetLine-2){
           	j=(short)(j+2);
            CellRangeAddress cellRangeAddressJF = new CellRangeAddress(k, k+j-1, 3, 3);
            CellRangeAddress cellRangeAddressJF1 = new CellRangeAddress(k, k+j-1, 4, 4);
            CellRangeAddress cellRangeAddressJF2 = new CellRangeAddress(k, k+j-1, 5, 5);
            CellRangeAddress cellRangeAddressJF3 = new CellRangeAddress(k, k+j-1, 6, 6);
            RegionUtil.setBorderLeft(1, cellRangeAddressJF, sheet, wb);  
            RegionUtil.setBorderBottom(1, cellRangeAddressJF, sheet, wb);  
            RegionUtil.setBorderRight(1, cellRangeAddressJF, sheet, wb);  
            RegionUtil.setBorderTop(1, cellRangeAddressJF, sheet, wb);
            sheet.addMergedRegion(cellRangeAddressJF); 
            sheet.addMergedRegion(cellRangeAddressJF1);
            sheet.addMergedRegion(cellRangeAddressJF2);
            sheet.addMergedRegion(cellRangeAddressJF3);  
       	}
       } 
		for (int i = 0; i < 17; i++) {
			sheet.setColumnWidth(i, 4000);
		}
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 2500);
		sheet.setColumnWidth(2, 1800);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 12000);
		sheet.setColumnWidth(5, 2800);
		sheet.setColumnWidth(6, 1800);
		sheet.setColumnWidth(7, 2200);
		sheet.setColumnWidth(8, 2200);
		sheet.setColumnWidth(9, 1500);
		sheet.setColumnWidth(10, 4500);
		sheet.setColumnWidth(11, 7000);
		sheet.setColumnWidth(12, 3000);
		sheet.setColumnWidth(13, 3000);
		sheet.setColumnWidth(14, 12000);
		sheet.setColumnWidth(15, 3000);
		sheet.setColumnWidth(16, 5000);
		
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(startdate+"到"+enddate+"企业信披质量评估.xls");
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
	public List<HashMap<String, Object>> getNoticeDetails(String customerno,String zqjc, String zqdm, String noticename, String noticetype,String startdate, String enddate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date Date1 = null;
            java.util.Date Date2 = null;
            int ct1 = 0;
            if(startdate!=null&&!startdate.equals(""))
                Date1 = format.parse(startdate);
            if(enddate!=null&&!enddate.equals(""))
                Date2 = format.parse(enddate);
            if(Date1!=null&&Date2!=null)
                ct1 = Date1.compareTo(Date2);

            if(ct1>0){
                return new ArrayList();
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ArrayList();
        }

		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT ID,INSTANCEID,ZQJCXS,ZQDMXS,NOTICENAME,NOTICENO,NOTICETYPE,TO_CHAR(NOTICEDATE,'yyyy-MM-dd') NOTICEDATE,LCBS,LCBH,STEPID,TO_CHAR(GGDF,'FM9999990.00') GGDF FROM BD_MEET_QTGGZL WHERE SPZT='审批通过' ");
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND  TO_CHAR(NOTICEDATE,'yyyy-MM-dd') >= ?");
			parameter.add(startdate);
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(NOTICEDATE,'yyyy-MM-dd') <= ?");
			parameter.add(enddate);
		}
		if(noticename!=null&&!noticename.equals("")){
			sql.append(" AND UPPER(NOTICENAME) LIKE ?");
			parameter.add("%"+noticename.toUpperCase()+"%");
		}
		if(noticetype!=null&&!noticetype.equals("")){
			sql.append(" AND NOTICETYPE = ?");
			parameter.add(noticetype);
		}
		sql.append(" AND KHBH=?");
		parameter.add(customerno);
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getNoticeDetails(sql.toString(),parameter);
		return list;
	}

	public String getOpinionList(Long lcbs, String lcbh, String stepid) {
		String processInstanceOpinionList = processOpinionService.getProcessInstanceOpinionList(false, lcbh, stepid, lcbs);
		return processInstanceOpinionList.equals("")?"无反馈意见":processInstanceOpinionList;
	}

	public List<HashMap<String, Object>> getOpinionUserList(String startdate, String enddate) {
		List<String> parameter=new ArrayList<String>();//存放参数
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String userid=userModel.getUserid();
		Long roleid=userModel.getOrgroleid();
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT ROP.CREATEUSER CREATEUSER,ORG.USERNAME USERNAME,COUNT(1) ONUM FROM (SELECT CREATEUSER,EXECUTIONID FROM PROCESS_RU_OPINION WHERE ACTION='驳回' ");
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(CREATETIME,'yyyy-MM-dd') >= ?");
			parameter.add(startdate);
		}else{
			sql.append(" AND TO_CHAR(CREATETIME,'yyyy-MM-dd') >= TO_CHAR(ADD_MONTHS(SYSDATE,-6),'yyyy-MM-dd')");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(CREATETIME,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}else{
			sql.append(" AND TO_CHAR(CREATETIME,'yyyy-MM-dd') <= TO_CHAR(SYSDATE,'yyyy-MM-dd') ");
		}
		sql.append(" ) ROP INNER JOIN ORGUSER ORG ON ROP.CREATEUSER=ORG.USERID  ");
		if(roleid!=5){
			sql.append(" AND  ORG.USERID=? ");
			parameter.add(userid);
		}
		sql.append(" INNER JOIN BD_MEET_QTGGZL GG ON ROP.EXECUTIONID=GG.LCBS GROUP BY ROP.CREATEUSER,ORG.USERNAME");
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getOpinionUserList(sql.toString(),parameter);
		return list;
	}

	public List<HashMap<String, Object>> getopinionUserContentList( String createuser,String opinionContent, int pageNumber, int pageSize, String startdate, String enddate) {
		List<HashMap<String,Object>> parameter=new ArrayList<HashMap<String,Object>>();//存放参数
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber*pageSize;
		HashMap createuserMap=null;
		HashMap opinionContentMap=null;
		HashMap startRowMap=null;
		HashMap endRowMap=null;
		HashMap startdateMap=null;
		HashMap enddateMap=null;
		createuserMap=new HashMap<String,Object>();
		createuserMap.put("VALUE", createuser);
		createuserMap.put("TYPE", "string");
		
		startRowMap=new HashMap<String,Object>();
		startRowMap.put("VALUE", startRow);
		startRowMap.put("TYPE", "int");
		
		endRowMap=new HashMap<String,Object>();
		endRowMap.put("VALUE", endRow);
		endRowMap.put("TYPE", "int");
		
		if(startdate!=null&&!startdate.equals("")){
			startdateMap=new HashMap<String,Object>();
			startdateMap.put("VALUE", startdate);
			startdateMap.put("TYPE", "string");
		}
		if(enddate!=null&&!enddate.equals("")){
			enddateMap=new HashMap<String,Object>();
			enddateMap.put("VALUE", enddate);
			enddateMap.put("TYPE", "string");
		}
		if(opinionContent!=null&&!opinionContent.equals("")){
			opinionContentMap=new HashMap<String,Object>();
			opinionContentMap.put("VALUE", "%"+opinionContent.toUpperCase()+"%");
			opinionContentMap.put("TYPE", "string");
		}
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT GGIDB.GGID GGID,GGIDB.XH XH,RSPANUM.RNUM RNUM,GGOP.NOTICENAME NOTICENAME,GGOP.ZQJCXS ZQJCXS,GGOP.ZQDMXS ZQDMXS,GGOP.CREATETIME CREATETIME,GGOP.CONTENT CONTENT FROM (SELECT GGID,ROWNUM XH FROM (SELECT GGID,RNUM FROM (SELECT GGID,ROWNUM RNUM FROM (SELECT DISTINCT GGID FROM (SELECT GG.ID GGID FROM BD_MEET_QTGGZL GG INNER JOIN (SELECT ROP.EXECUTIONID FROM PROCESS_RU_OPINION ROP WHERE ROP.ACTION='驳回' AND ROP.CREATEUSER=? ");
		parameter.add(createuserMap);
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= ?");
			parameter.add(startdateMap);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= TO_CHAR(ADD_MONTHS(SYSDATE,-6),'yyyy-MM-dd')");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= ? ");
			parameter.add(enddateMap);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= TO_CHAR(SYSDATE,'yyyy-MM-dd') ");
		}
		if(opinionContent!=null&&!opinionContent.equals("")){
			sql.append(" AND UPPER(ROP.CONTENT) LIKE ?");
			parameter.add(opinionContentMap);
		}
		sql.append(" ) RES ON GG.LCBS=RES.EXECUTIONID ORDER BY ID))) WHERE RNUM>? AND RNUM<=? ORDER BY GGID)) GGIDB "
				+ " INNER JOIN (SELECT GGID,COUNT(1) RNUM FROM (SELECT GG.ID GGID FROM BD_MEET_QTGGZL GG INNER JOIN (SELECT ROP.EXECUTIONID FROM PROCESS_RU_OPINION ROP WHERE ROP.ACTION='驳回' AND ROP.CREATEUSER=? ");
		parameter.add(startRowMap);
		parameter.add(endRowMap);
		parameter.add(createuserMap);
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= ?");
			parameter.add(startdateMap);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= TO_CHAR(ADD_MONTHS(SYSDATE,-6),'yyyy-MM-dd')");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= ? ");
			parameter.add(enddateMap);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= TO_CHAR(SYSDATE,'yyyy-MM-dd') ");
		}
		if(opinionContent!=null&&!opinionContent.equals("")){
			sql.append(" AND UPPER(ROP.CONTENT) LIKE ?");
			parameter.add(opinionContentMap);
		}
		sql.append(" ) RES ON GG.LCBS=RES.EXECUTIONID ORDER BY ID) GROUP BY GGID) RSPANUM ON GGIDB.GGID=RSPANUM.GGID "
				+ " INNER JOIN (SELECT GGID,NOTICENAME,ZQJCXS,ZQDMXS,CREATETIME,CONTENT,ID RID FROM (SELECT GG.ID GGID,GG.NOTICENAME NOTICENAME,GG.ZQJCXS ZQJCXS,GG.ZQDMXS ZQDMXS,RES.CREATETIME CREATETIME,RES.CONTENT CONTENT,RES.ID FROM BD_MEET_QTGGZL GG "
				+ " INNER JOIN (SELECT ROP.ID,ROP.EXECUTIONID,ROP.CONTENT,TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd HH24:MI:SS') CREATETIME FROM PROCESS_RU_OPINION ROP WHERE ROP.ACTION='驳回' AND ROP.CREATEUSER=? ");
		parameter.add(createuserMap);
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= ?");
			parameter.add(startdateMap);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= TO_CHAR(ADD_MONTHS(SYSDATE,-6),'yyyy-MM-dd')");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= ? ");
			parameter.add(enddateMap);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= TO_CHAR(SYSDATE,'yyyy-MM-dd') ");
		}
		if(opinionContent!=null&&!opinionContent.equals("")){
			sql.append(" AND UPPER(ROP.CONTENT) LIKE ?");
			parameter.add(opinionContentMap);
		}
		sql.append(" ) RES ON GG.LCBS=RES.EXECUTIONID ORDER BY ID)) GGOP ON GGIDB.GGID=GGOP.GGID ORDER BY XH,GGOP.RID");
		List<HashMap<String, Object>> list = zqbAnnouncementDAO.getopinionUserContentList(sql.toString(),parameter);
		return list;
	}

	public int getopinionUserContentListSize(String createuser,String opinionContent, String startdate, String enddate) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) CNUM FROM (SELECT GGID FROM (SELECT GGID,ROWNUM RNUM FROM (SELECT DISTINCT GGID FROM (SELECT GG.ID GGID FROM BD_MEET_QTGGZL GG INNER JOIN (SELECT ROP.EXECUTIONID FROM PROCESS_RU_OPINION ROP WHERE ROP.ACTION='驳回' AND ROP.CREATEUSER=? ");
		parameter.add(createuser);
		if(startdate!=null&&!startdate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= ?");
			parameter.add(startdate);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') >= TO_CHAR(ADD_MONTHS(SYSDATE,-6),'yyyy-MM-dd')");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= ? ");
			parameter.add(enddate);
		}else{
			sql.append(" AND TO_CHAR(ROP.CREATETIME,'yyyy-MM-dd') <= TO_CHAR(SYSDATE,'yyyy-MM-dd') ");
		}
		if(opinionContent!=null&&!opinionContent.equals("")){
			sql.append(" AND UPPER(ROP.CONTENT) LIKE ?");
			parameter.add("%"+opinionContent.toUpperCase()+"%");
		}
		sql.append(" ) RES ON GG.LCBS=RES.EXECUTIONID ORDER BY ID)))) GGIDB ");
		return zqbAnnouncementDAO.getOpinionUserContentListSize(sql.toString(),parameter);
	}

	public void docDownload(HttpServletResponse response, String recordid,String fj) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		List<HashMap<String, Object>> filelist = zqbAnnouncementDAO.docDownload(recordid,fj);
		if(filelist.size()==1){
			String fileUrl = filelist.get(0).get("FILE_URL").toString();
			try {
				String fileName=null;
				//获取公告名称
//            	String noticename=filelist.get(0).get("NOTICENAME")==null?"":filelist.get(0).get("NOTICENAME").toString();
				
				
					String sql="SELECT FILE_SRC_NAME FROM SYS_UPLOAD_FILE WHERE FILE_ID=(SELECT RECORDID FROM IWORK_WEBOFFCIE_VERSION WHERE FILE_ID=?)";
					Map params=new HashMap();
					params.put(1, recordid);
					String filename = DBUTilNew.getDataStr("FILE_SRC_NAME", sql, params);
					if(filename!=null&&filename.length()>5){
					filename=filename.substring(0, filename.length()-5);
					if(filename.length()>30){
						filename=filename.substring(0,30);
					}
					}
				if("".equals(filename)){
					
					 fileName=filelist.get(0).get("DESCRIPT").toString()+fileUrl.substring(fileUrl.indexOf("."));
				}else{
			        fileName =filename +"-"+filelist.get(0).get("DESCRIPT").toString()+fileUrl.substring(fileUrl.indexOf("."));}
				try {
					fis = new FileInputStream(filepath+fileUrl);
				} catch (Exception localException1) {}
				if (fis == null) {
					fileUrl = ServletActionContext.getServletContext().getRealPath(fileUrl.replaceAll("\\.\\.", ""));
					fis = new FileInputStream(fileUrl);
				}
				String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(fileName);
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-disposition", disposition);
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				bos.write(buffer);
				bos.flush();
			} catch (Exception e) {
				if (fis != null)
					try {fis.close();}catch (Exception localIOException) {}
				if (bos != null)
					try {bos.close();}catch (Exception localIOException1){}
			}finally {
				if (fis != null)
					try {fis.close();}catch (Exception localIOException2) {}
				if (bos != null)
					try {bos.close();}catch (Exception localIOException3) {}
			}
		}else{
			return;
		}
	}

	public List<HashMap> getNbshrList() {
		List nbshrlist = new ArrayList<HashMap>();
		// 查询数据库
		String KHMC_DEPARTMENTNAME = DBUtil.getString("SELECT DEPARTMENTNAME FROM ORGUSER WHERE USERID='"+UserContextUtil.getInstance().getCurrentUserId()+"'", "DEPARTMENTNAME");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID,KHBH,KHMC,KHFZR,ZZCXDD,FHSPR,ZZSPR,GGFBR,C.INSTANCEID,CWSCBFZR2,CWSCBFZR3,F.ZQDM,QYNBRYSH FROM BD_MDM_KHQXGLB A,SYS_ENGINE_FORM_BIND C,BD_ZQB_KH_BASE F WHERE 1=1 AND FORMID = '114'AND METADATAID = '130' AND A.ID = C.DATAID  AND C.INSTANCEID IS NOT NULL  AND A.KHBH=F.CUSTOMERNO AND A.KHMC=?");
		PreparedStatement stmt = null;
		Connection conn=null;
		ResultSet rs=null;
			try {
				conn = DBUtil.open();
				stmt =conn.prepareStatement(sql.toString());
				stmt.setString(1, KHMC_DEPARTMENTNAME);
				rs = stmt.executeQuery();
				while(rs.next()){
					HashMap<String, Object> map = new HashMap<String, Object>();
					Object ID = rs.getInt("ID");
					Object KHBH = rs.getString("KHBH");
					Object KHMC = rs.getString("KHMC");
					Object KHFZR = rs.getString("KHFZR");
					Object ZZCXDD = rs.getString("ZZCXDD");
					Object FHSPR = rs.getString("FHSPR");
					Object ZZSPR = rs.getString("ZZSPR");
					Object GGFBR = rs.getString("GGFBR");
					Object INSTANCEID = rs.getInt("INSTANCEID");
					Object CWSCBFZR2 = rs.getString("CWSCBFZR2");
					Object CWSCBFZR3 = rs.getString("CWSCBFZR3");
					Object QYNBRYSH = rs.getString("QYNBRYSH");
					map.put("ID", ID);
					map.put("KHBH", KHBH);
					map.put("KHMC", KHMC);
					map.put("KHFZR", KHFZR);
					map.put("ZZCXDD", ZZCXDD);
					map.put("FHSPR", FHSPR);
					map.put("ZZSPR", ZZSPR);
					map.put("GGFBR", GGFBR);
					map.put("INSTANCEID", INSTANCEID);
					map.put("CWSCBFZR2", CWSCBFZR2);
					map.put("CWSCBFZR3", CWSCBFZR3);
					map.put("QYNBRYSH", QYNBRYSH);
					nbshrlist.add(map);
				}
			} catch (Exception e) {
			} finally{
				DBUtil.close(conn, stmt, rs);
			}
			return nbshrlist;
	}

	public List getNbshrCansetList() {
		List cansetList = new ArrayList<HashMap>();
		// 查询数据库
		String KHMC_DEPARTMENTNAME = DBUtil.getString("SELECT DEPARTMENTNAME FROM ORGUSER WHERE USERID='"+UserContextUtil.getInstance().getCurrentUserId()+"'", "DEPARTMENTNAME");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT USERID,USERNAME,ORGROLEID,HOMETEL,MOBILE,EMAIL FROM ORGUSER WHERE DEPARTMENTNAME=? AND USERID <> ?");
		Connection conn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
			try {
				conn = DBUtil.open();
				stmt =conn.prepareStatement(sql.toString());
				stmt.setString(1, KHMC_DEPARTMENTNAME);
				stmt.setString(2, UserContextUtil.getInstance().getCurrentUserId());
				rs = stmt.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					String USERID = rs.getString("USERID");
					String USERNAME = rs.getString("USERNAME");
					String ORGROLEID = rs.getString("ORGROLEID");
					String HOMETEL = rs.getString("HOMETEL");
					String MOBILE = rs.getString("MOBILE");
					String EMAIL = rs.getString("EMAIL");
					map.put("USERID", USERID);
					map.put("USERNAME", USERNAME);
					map.put("ORGROLEID", ORGROLEID);
					map.put("HOMETEL", HOMETEL);
					map.put("MOBILE", MOBILE);
					map.put("EMAIL", EMAIL);
					cansetList.add(map);
				}
			} catch (Exception e) {
			} finally{
				DBUtil.close(conn, stmt, rs);
			}
		return cansetList;
	}

	public void setZqbFzgsUpdateDeleteStatus(String bid) {
		StringBuffer sql = new StringBuffer();
		sql.append( "UPDATE BD_ZQB_CWBB SET DELETESTATUS='1' WHERE ID =?");
		Connection conn=null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.open();
			conn.setAutoCommit(true);
			ps = conn.prepareStatement(sql.toString());
			ps.setInt(1, Integer.parseInt(bid));
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}
	}

	public void setZqbVoteUnlockthis(String lockstatus, String customerno,String ggid) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BD_VOTE_WJDC SET LOCKSTATUS=? WHERE CUSTOMERNO=? AND TZGGID=?");
		Connection conn=null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, lockstatus);
			ps.setString(2, customerno);
			ps.setString(3, ggid);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}
		
	}

	public void setZqbVoteLockthis(String lockstatus, String customerno,String ggid) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BD_VOTE_WJDC SET LOCKSTATUS=? WHERE CUSTOMERNO=? AND TZGGID=?");
		PreparedStatement ps = null;
		Connection conn =null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, lockstatus);
			ps.setString(2, customerno);
			ps.setString(3, ggid);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}		
	}
	public void setZqbVoteLockmore(String lockstatus, String customerno,String ggid) {
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		sql.append("UPDATE BD_VOTE_WJDC SET LOCKSTATUS=? WHERE CUSTOMERNO in(");
		params.add(lockstatus);
		String[] cus = customerno.split(",");
		for (int i = 0; i < cus.length; i++) {
			if(i==(cus.length-1)){
				sql.append("?");
			}else{
				sql.append("?,");
			}
			params.add(cus[i].replaceAll("'", ""));
		}
		sql.append(") AND TZGGID=?");
		params.add(ggid);
		Connection conn =null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i));
			}
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}		
	}

	public List<HashMap> industrymsgManegement(int pageSize,int pageNumber,String zhuhy,String zihy) {
		return zqbAnnouncementDAO.industrymsgManegement(pageSize,pageNumber,zhuhy,zihy);
	}

	public int industrymsgManegementSize(String zhuhy, String zihy) {
		return zqbAnnouncementDAO.industrymsgManegementSize(zhuhy,zihy).size();
	}
	
	public void impIndustrymsg(String filename) {
		Long starttime = new Date().getTime();
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(filename);
		OrgUser user =UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		String rootPath=request.getRealPath("/");
		String zhuhy = null;
		String zihy = null;
		String typename = null; 
		String cjsj = UtilDate.getNowDatetime();
		String userid = user.getUserid();
		File file = null;
		Workbook workBook = null;
		Sheet sheet = null;
		HashMap industrymsgmap = null;
		HashMap addmap = null;
		String demUUID = null;
		Long instanceid = null;
		
		//行业集合,用于存储行业信息
		List<HashMap<String,String>> industrymsg = new ArrayList<HashMap<String,String>>();
		
		String sql = null;
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			sql = "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='行业信息维护表单'";
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				demUUID = rs.getString("UUID");
			}
			rs.close();rs=null;
			ps.close();ps=null;
			
			sql = "SELECT ZHUHY,ZIHY FROM BD_XP_HYXXB";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				industrymsgmap = new HashMap<String,String>();
				industrymsgmap.put(rs.getString("ZHUHY"),rs.getString("ZIHY"));
				industrymsg.add(industrymsgmap);
				industrymsgmap=null;
			}
		} catch (Exception e){
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		
		try {
			//遍历上传文件
			for (FileUpload fileUpload : sublist) {
				//加载xls文件
				file = new File(rootPath+File.separator+fileUpload.getFileUrl());
				
				try {workBook = new XSSFWorkbook(rootPath+File.separator+fileUpload.getFileUrl());  
				} catch (Exception ex) {  
					try {workBook = new HSSFWorkbook(new FileInputStream(rootPath+File.separator+fileUpload.getFileUrl()));
					} catch (FileNotFoundException e) {} catch (Exception e) {}
				}
				if(workBook==null){continue;}
				//遍历sheet
				for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
					sheet = workBook.getSheetAt(numSheet);  
					if (sheet == null) {continue;}
					// 循环行Row  
					for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {  
						Row row = sheet.getRow(rowNum);  
						if (row == null) {continue;}  
						// 循环列Cell  
						Cell cell = row.getCell(0);  
						if (cell == null) {continue;}
						zhuhy = getValue(cell);
						
						cell = row.getCell(1);
						if (cell == null) {continue;}
						zihy = getValue(cell);
						
						industrymsgmap = new HashMap<String,String>();
						industrymsgmap.put(zhuhy, zihy);
						if(!industrymsg.contains(industrymsgmap)){
							instanceid = DemAPI.getInstance().newInstance(demUUID, userid);
							addmap = new HashMap<String,String>();
							addmap.put("ZHUHY",zhuhy);
							addmap.put("ZIHY",zihy);
							addmap.put("CREATOR",userid);
							addmap.put("CJSJ",cjsj);
							DemAPI.getInstance().saveFormData(demUUID, instanceid, addmap, false);
							industrymsg.add(industrymsgmap);
							industrymsgmap=null;
						}
						
						//问题不存在导入
					}
				}
			}
		} catch (Exception e) {}finally{
		}
		Long endtime = new Date().getTime();
		ResponseUtil.write("导入完毕,用时"+(endtime-starttime)+"毫秒");
		addmap = null;
		industrymsgmap = null;instanceid = null;
		actionContext = null;industrymsg = null;
		starttime = null;rootPath = null;
		typename = null;workBook = null;
		endtime = null;sublist = null;
		request = null;demUUID = null;
		userid = null;sheet = null;
		zhuhy = null;sql = null;
		user = null;file = null;
		zihy = null;cjsj = null;
		System.gc();
	}
	//培训通知公告获取应到人员信息
	public List getSigninup(String username,String szbm,String startdate,String enddate,String qd,String ggid){
		List list = new ArrayList<HashMap>();
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer();
		sql.append("select d.* from (select a.gsmc,a.xm,a.status,c.rymc,c.sfqd,c.sjhm,c.szbm,c.cyrq,c.id,r.zqjc,r.zqdm,z.orgroleid from BD_XP_HFQKB a left join BD_XP_HFXXB b on a.hfid =b.id  left join BD_XP_PXTZRYB c on b.instanceid=c.instanceid left join orguser  z on z.userid = a.userid left join bd_zqb_kh_base r on r.customerno=a.customerno  where a.ggid=? order by r.zqdm) d where 1=1");
		parameter.add(ggid);
		if(username!=null&&!username.equals("")){
			sql.append(" and d.rymc like ?");
			parameter.add("%"+username+"%");
		}
		if(szbm!=null&&!szbm.equals("")){
			sql.append(" and d.gsmc like ?");
			parameter.add("%"+szbm+"%");
		}
		if(startdate!=null&&!startdate.equals("")){
			sql.append("and TO_CHAR(d.cyrq,'yyyy-MM-dd') >= ?");
			parameter.add("'"+startdate+"'");
		}
		if(enddate!=null&&!enddate.equals("")){
			sql.append("and TO_CHAR(d.cyrq,'yyyy-MM-dd') <= ?");
			parameter.add("'"+enddate+"'");
		}
		if(qd!=null&&!qd.equals("")){
			sql.append(" and d.sfqd = ?");
			parameter.add(qd);
		}
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			int jcount=2;
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String gsmc = rs.getString("GSMC");				//公司名称
				String hfr = rs.getString("XM");				//回复人
				String hfzt = rs.getString("status");			//回复状态	
				String cyrxm = rs.getString("RYMC");			//参与人姓名
				String sfqd = rs.getString("SFQD");				//是否签到
				String sjhm = rs.getString("SJHM");				//手机号码
				String email = rs.getString("SZBM");			//邮箱
				String cyrq = rs.getString("CYRQ");				//参与日期
				String id = rs.getString("ID");			//参与培训id
				String zqjc=rs.getString("zqjc");
				String zqdm=rs.getString("zqdm");
				String qx=rs.getString("orgroleid");
				map.put("SZBM", gsmc);
				map.put("USERNAME", hfr);
				if(hfzt.equals("未回复")){
					map.put("PXRY", cyrxm==null?"暂未回复":cyrxm);
				}else{
					map.put("PXRY", cyrxm==null?"暂无培训人员":cyrxm);
				}
				map.put("SFQD", sfqd==null?"暂无":sfqd);
				map.put("MOBILE", sjhm==null?"暂无":sjhm);
				map.put("EMAIL", email==null?"暂无":email);
				map.put("CYRQ", cyrq==null?"暂无":cyrq);
				map.put("ID", id);
				if(qx.equals("3")){
					map.put("zqjc", zqjc);
					map.put("zqdm", zqdm);
				}else{
					map.put("zqjc", "");
					map.put("zqdm", "");
				}
				
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}	
		return list;
	
	}
	public boolean updateSigninup(String cyrid) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BD_XP_PXTZRYB SET SFQD=? WHERE id=?");		
		Connection conn =null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.open();
			conn.setAutoCommit(false); 
			ps = conn.prepareStatement(sql.toString());			
			String [] cyr=cyrid.split("\\|");
			for(int i=0;i<cyr.length;i++){
				String[] cy=cyr[i].split(",");
				ps.setString(1, cy[1].equals("true")?"是":"否");
				ps.setInt(2, Integer.parseInt(cy[0])); 
				ps.addBatch();
			}
			
			ps.executeBatch();
			conn.commit();
		} catch (Exception e) {
			logger.error(e,e);
			return false;
		} finally{
			DBUtil.close(conn, ps, null);
		}	
		return true;
	}
	public void SigninupExcelExp(HttpServletResponse response,String username,String szbm,String startdate,String enddate,String qd,String ggid,String tzbt) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("参与培训人员信息");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("公司代码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("公司简称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		
		cell.setCellValue("部门/公司");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("通知反馈人");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("参与培训人员");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("参与日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("电话");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("邮箱");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("签到");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List list = getSigninup(username,szbm, startdate,enddate,qd,ggid);
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (int i = 0; i < list.size(); i++) {

				Map map = (HashMap) list.get(i);
				row = sheet.createRow((int) n++);
				// 第四步，创建单元格，并设置值
				HSSFCell celldm = row.createCell((short) 0);
				celldm.setCellValue(map.get("zqdm").toString());
				celldm.setCellStyle(style1);
				HSSFCell celljc = row.createCell((short) 1);
				celljc.setCellValue(map.get("zqjc").toString());
				celljc.setCellStyle(style1);
				
				
				HSSFCell cell1 = row.createCell((short) 2);
				cell1.setCellValue(map.get("SZBM").toString());
				cell1.setCellStyle(style1);
				HSSFCell cell2 = row.createCell((short) 3);
				cell2.setCellValue(map.get("USERNAME").toString());
				cell2.setCellStyle(style1);
				HSSFCell cell3 = row.createCell((short) 4);
				cell3.setCellValue(map.get("PXRY").toString());
				cell3.setCellStyle(style1);
				HSSFCell cell4 = row.createCell((short) 5);
				cell4.setCellValue(map.get("CYRQ").toString().equals(null)?"暂无":map.get("CYRQ").toString());
				cell4.setCellStyle(style1);
				HSSFCell cell5 = row.createCell((short) 6);
				cell5.setCellValue(map.get("MOBILE").toString()==null?"暂无":map.get("MOBILE").toString());
				cell5.setCellStyle(style1);
				HSSFCell cell6 = row.createCell((short) 7);
				cell6.setCellValue(map.get("EMAIL").toString()==null?"暂无":map.get("EMAIL").toString());
				cell6.setCellStyle(style1);
				HSSFCell cell7 = row.createCell((short) 8);
				cell7.setCellValue(map.get("SFQD").toString());
				cell7.setCellStyle(style1);
				m++;

		}
		for (int i = 0; i < 7; i++) {
			sheet.setColumnWidth(i, 7000);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(tzbt+"参与培训人员.xls");
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
	//获取公告回复附件数量
	public int gettzggbatchsl(String ggid,String roleid){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username = uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(B.XGZL) CNUM  FROM BD_XP_HFXXB B LEFT JOIN ORGUSER O ON B.HFRID=O.USERID WHERE B.GGID=?");
		if(roleid!=null&&roleid.equals("4")){
			sql.append(" AND O.EXTEND1 IN (SELECT S.KHBH FROM BD_MDM_KHQXGLB S WHERE S.KHFZR=? OR S.ZZCXDD=? OR S.ZZSPR=? OR S.FHSPR=? OR S.CWSCBFZR2=? OR S.CWSCBFZR3=? )");
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ggid);
			if(roleid!=null&&roleid.equals("4")){
				ps.setString(2, username);
				ps.setString(3, username);
				ps.setString(4, username);
				ps.setString(5, username);
				ps.setString(6, username);
				ps.setString(7, username);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}
	//获取通知公告回复信息
	public List getgghfxxsj(String ggid,String roleid){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username = uc.get_userModel().getUserid()+"["+uc.get_userModel().getUsername()+"]";
		List list = new ArrayList<HashMap>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.* FROM (SELECT A.GSDM,A.GSMC,A.XM,A.USERID,A.STATUS,B.XGZL FROM BD_XP_HFQKB A LEFT JOIN BD_XP_HFXXB B ON A.HFID =B.ID  LEFT JOIN BD_XP_PXTZRYB C ON B.INSTANCEID=C.INSTANCEID  WHERE A.GGID=?");
		if(roleid!=null&&roleid.equals("4")){
			sql.append(" AND A.CUSTOMERNO IN (SELECT S.KHBH FROM BD_MDM_KHQXGLB S WHERE S.KHFZR=? OR S.ZZCXDD=? OR S.ZZSPR=? OR S.FHSPR=? OR S.CWSCBFZR2=? OR S.CWSCBFZR3=? )");
		}
		sql.append(" ORDER BY A.GSMC,A.XM) D WHERE 1=1 AND D.STATUS='已回复'");
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, ggid);
			if(roleid!=null&&roleid.equals("4")){
				ps.setString(2, username);
				ps.setString(3, username);
				ps.setString(4, username);
				ps.setString(5, username);
				ps.setString(6, username);
				ps.setString(7, username);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String gsdm = rs.getString("GSDM");				//公司代码
				String gsmc = rs.getString("GSMC");				//公司名称
				String hfr = rs.getString("XM");				//回复人
				String userid = rs.getString("USERID");			//回复人id
				String hfzt = rs.getString("status");			//回复状态	
				String xgzl = rs.getString("XGZL");				//附件
				

				map.put("GSDM", gsdm==null?"":gsdm);
				map.put("GSMC", gsmc);
				map.put("HFR", hfr);
				map.put("USERID", userid);
				map.put("HFZT", hfzt);
				map.put("XGZL", xgzl==null?"":xgzl);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}	
		return list;
	}
	//通知公告回复附件信息   压缩
		public String tzggbatchdownload(List<HashMap> list) throws Exception {
			//复制附件到指定文件夹
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String filepath = request.getSession().getServletContext().getRealPath("/");
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
			Date date = new Date();
			
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			//String foldername = "信披自查反馈批量下载文件";
			String foldername = user.getUserid()+sdf.format(date);
		//	String foldernames = "通知公告回复附件批量下载文件";
			//删除原有文件夹
			File filedel = new File(filepath+foldername);
        	delChange(filedel);
			String ghfry="";
			for(HashMap map:list){
				//获取当前回复人员角色
				UserContext target = UserContextUtil.getInstance().getUserContext(map.get("USERID").toString());
				//当前回复人员角色id
				Long orgroleid = target.get_userModel().getOrgroleid();
				if(map.get("HFZT").equals("已回复")&&!map.get("XGZL").equals("")){
					List<FileUpload> fileobjs = FileUploadAPI.getInstance().getFileUploads(map.get("XGZL").toString());
					int i=1;
					if(fileobjs.size()>0){
						for (FileUpload fileobj : fileobjs) {
							String srcname = fileobj.getFileSrcName();
							srcname = srcname.substring(0,srcname.lastIndexOf("."))+i+srcname.substring(srcname.lastIndexOf("."));
							//循环复制文件
							File f = new File(filepath+fileobj.getFileUrl());
							if(f.exists()){
								//根据角色id 创建不同目录
								if(orgroleid==3){
									ghfry = map.get("GSDM").toString();
								}else if(orgroleid==8){
									ghfry = map.get("GSMC").toString();
								}else {
									ghfry = map.get("HFR").toString()+"("+map.get("USERID").toString()+")";
								}
								File fnew = new File(filepath+foldername+"\\"+ghfry);
								int byteread = 0;
								int bytesum = 0;
								InputStream inStream = new FileInputStream(f);
								if(!fnew.exists()){
									fnew.mkdirs();
									StringBuffer sb = new StringBuffer();
									//根据角色id 打包不同的文件夹
									if(orgroleid==3){
										ghfry = map.get("GSDM").toString();
									}else if(orgroleid==8){
										ghfry = map.get("GSMC").toString();
									}else {
										ghfry = map.get("HFR").toString()+"("+map.get("USERID").toString()+")";
									}
									sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(ghfry).append("\\").append(srcname);
									//sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(srcname);
									fnew = new File(sb.toString());
									fnew.createNewFile();
									FileOutputStream fsot = new FileOutputStream(fnew);
									byte[] buffer = new byte[1444]; 
									while ( (byteread = inStream.read(buffer)) != -1) { 
										bytesum += byteread; 
										fsot.write(buffer, 0, byteread);
									} 
									fsot.flush();
									inStream.close(); 
									fsot.close();
								}else{
									StringBuffer sb = new StringBuffer();
									//根据角色id 打包不同的文件夹
									if(orgroleid==3){
										ghfry = map.get("GSDM").toString();
									}else if(orgroleid==8){
										ghfry = map.get("GSMC").toString();
									}else {
										ghfry = map.get("HFR").toString()+"("+map.get("USERID").toString()+")";
									}
									sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(ghfry).append("\\").append(srcname);
									//sb.append(filepath).append(foldername.replaceAll(" ", "")).append("\\").append(srcname);
									fnew = new File(sb.toString());
									if(!fnew.exists()){
										fnew.createNewFile();
									}
									FileOutputStream fsot = new FileOutputStream(fnew);
									byte[] buffer = new byte[1444]; 
									while ( (byteread = inStream.read(buffer)) != -1) { 
										bytesum += byteread; 
										fsot.write(buffer, 0, byteread);
									}
									fsot.flush();
									inStream.close(); 
									fsot.close();
								}
								i++;
							}
						}
					}else{
						File fnew = new File(filepath+foldername);
						if(!fnew.exists()){
							fnew.mkdirs();
						}
						fnew = new File(filepath+foldername+".zip");
						if(!fnew.exists()){
							fnew.createNewFile();
						}
					}
				}
			}
			//压缩文件夹
			/*ZipUtil zc = new  ZipUtil(filepath+foldername+".zip");  
	        zc.compressExe(filepath+foldername); */
			
			try {
				final File result = new File(filepath+foldername+".zip");  
				createZipFile(filepath+foldername, result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		        return foldername;
		} 
		public void downTzgg(String foldername){
			
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String foldernames = "通知公告回复附件批量下载文件";
			String filepath = request.getSession().getServletContext().getRealPath("/");
			//下载压缩文件
	        try {
		        InputStream fis = new BufferedInputStream(new FileInputStream(filepath+foldername+".zip"));
		        byte[] buffer = new byte[fis.available()];
		        fis.read(buffer);
		        fis.close();
		
		        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		        response.setContentType("application/octet-stream");
		        response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(foldernames+".zip"));
		        
		        toClient.write(buffer);
		        toClient.flush();
		        toClient.close();
	        } catch (Exception ex) {
	        	
	        }finally{
	        	//删除文件夹、压缩文件文件
	        	File filedel1 = new File(filepath+foldername);
	        	delChange(filedel1);
	        	File filedel2 = new File(filepath+foldername+".zip");
	        	if(filedel2.exists()){
	        		filedel2.delete();
	        	}
	        }
		}
		//往后都是  压缩文件   .ZIP
		/* public void testSample() throws Exception {  
		        long begin = System.currentTimeMillis();  
		        final File result = new File("d:/test2/eclipse2.zip");  
		        createZipFile("F:/Java/eclipseplusUIx64_best", result);  
		        long end = System.currentTimeMillis();  
		        System.out.println("用时：" + (end - begin) + " ms");  
		    }  */
		 private void createZipFile(final String rootPath, final File result) throws Exception {  
		        File dstFolder = new File(result.getParent());  
		        if (!dstFolder.isDirectory()) {  
		            dstFolder.mkdirs();  
		        }  
		        File rootDir = new File(rootPath);  
		        final ScatterSample scatterSample = new ScatterSample(rootDir.getAbsolutePath());  
		        compressCurrentDirectory(rootDir, scatterSample);  
		        final ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(result);  
		        scatterSample.writeTo(zipArchiveOutputStream);  
		        zipArchiveOutputStream.close();  
		    }  
		 private void compressCurrentDirectory(File dir, ScatterSample scatterSample) throws Exception {  
		        if (dir == null) {  
		            throw new IOException("源路径不能为空！");  
		        }  
		        String relativePath = "";  
		        if (dir.isFile()) {  
		            relativePath = dir.getName();  
		            addEntry(relativePath, dir, scatterSample);  
		            return;  
		        }  
		  
		  
		        // 空文件夹  
		        if (dir.listFiles().length == 0) {  
		            relativePath = dir.getAbsolutePath().replace(scatterSample.getRootPath(), "");  
		            addEntry(relativePath + File.separator, dir, scatterSample);  
		            return;  
		        }  
		        for (File f : dir.listFiles()) {  
		            if (f.isDirectory()) {  
		                compressCurrentDirectory(f, scatterSample);  
		            } else {  
		                relativePath = f.getParent().replace(scatterSample.getRootPath(), "");  
		                addEntry(relativePath + File.separator + f.getName(), f, scatterSample);  
		            }  
		        }  
		    }  
		 private void addEntry(String entryName, File currentFile, ScatterSample scatterSample) throws Exception {  
		        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);  
		        archiveEntry.setMethod(ZipEntry.DEFLATED);  
		        final InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);  
		        scatterSample.addEntry(archiveEntry, supp);  
		    }  
		 class CustomInputStreamSupplier implements InputStreamSupplier {  
		        private File currentFile;  
		  
		  
		        public CustomInputStreamSupplier(File currentFile) {  
		            this.currentFile = currentFile;  
		        }  
		  
		  
		        @Override  
		        public InputStream get() {  
		            try {  
		                // InputStreamSupplier api says:  
		                // 返回值：输入流。永远不能为Null,但可以是一个空的流  
		                return currentFile.isDirectory() ? new NullInputStream(0) : new FileInputStream(currentFile);  
		            } catch (FileNotFoundException e) {  
		                e.printStackTrace();  
		            }  
		            return null;  
		        }  
		    }  
		 //解压文件结束
		 
		 public void downExcel(HttpServletResponse response,String ggid){
				// 第一步，创建一个webbook，对应一个Excel文件
				HSSFWorkbook wb = new HSSFWorkbook();
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
				HSSFSheet sheet = wb.createSheet("信披自查反馈统计");
				
			
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

				style3.setBorderBottom((short) 1);
				style3.setBorderLeft((short) 1);
				style3.setBorderRight((short) 1);
				style3.setBorderTop((short) 1);
				style3.setWrapText(true);
				font.setFontHeightInPoints((short) 12);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				
				HSSFFont font2 = wb.createFont();
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style.setFont(font2);
				style3.setFont(font2);
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
				
				
				List wtlist = zqbAnnouncementDAO.getWtList(ggid);
				List lists=zqbAnnouncementDAO.getWtdaList(ggid);
				int count=0;
				int rowbs=3;
				for (int i = 0; i < wtlist.size(); i++) {
					Map map = (HashMap) wtlist.get(i);
					if(map.get("type").toString().equals("信披自查")){
						cell = row.createCell((short) rowbs++);
						cell.setCellValue((i+1)+"丶"+map.get("question").toString());
						cell.setCellStyle(style3);
					    cell = row.createCell((short) rowbs++);
					    cell.setCellValue((i+1)+"丶"+map.get("question").toString());
						cell.setCellStyle(style3);
					    cell = row.createCell((short) rowbs++);
					    cell.setCellValue((i+1)+"丶"+map.get("question").toString());
						cell.setCellStyle(style3);
					    cell = row.createCell((short) rowbs++);
					    cell.setCellValue((i+1)+"丶"+map.get("question").toString());
						cell.setCellStyle(style3);
					   
					    count=count+4;
					}else{
						cell = row.createCell((short) rowbs++);
						cell.setCellValue((i+1)+"丶"+map.get("question").toString());
						cell.setCellStyle(style3);
					    cell = row.createCell((short) rowbs++);
					    cell.setCellValue((i+1)+"丶"+map.get("question").toString());
						cell.setCellStyle(style3);
					    
					}
				}
				int rowTwo=3;
				HSSFRow row1 = sheet.createRow(1);
				int s=0;
				for (int i = 0; i < count/4; i++) {
					HSSFCell cell1 = row1.createCell((short) rowTwo++);
					cell1.setCellValue(new HSSFRichTextString("是否发生"));
					cell1.setCellStyle(style2);
					
					cell1 = row1.createCell(rowTwo++);
					cell1.setCellValue(new HSSFRichTextString("是否披露"));
					cell1.setCellStyle(style2);
					
					cell1 = row1.createCell(rowTwo++);
					cell1.setCellValue(new HSSFRichTextString("预计"));
					cell1.setCellStyle(style2);
					
					cell1 = row1.createCell(rowTwo++);
					cell1.setCellValue(new HSSFRichTextString("备注"));
					cell1.setCellStyle(style2);
					s++;
				}
				int xh=rowbs-rowTwo;
				for (int i = 0; i < xh/2; i++) {
					HSSFCell c8 = row1.createCell(rowTwo++);
					c8.setCellValue(new HSSFRichTextString("是否知悉"));
					c8.setCellStyle(style2);
					
					c8 = row1.createCell(rowTwo++);
					c8.setCellValue(new HSSFRichTextString("备注"));
					c8.setCellStyle(style2);
				}
				Region region1 = new Region(0, (short)0, 1, (short)0);
		        Region region2 = new Region(0, (short)1, 1, (short)1);
		        Region region3 = new Region(0, (short)2, 1, (short)2);
		        int rowThree=2;
		        List<Region> listRegion=new ArrayList();
		        for (int i = 0; i < count/4; i++) {
		        	 Region region4 = new Region(0, (short)(rowThree+1), 0, (short)(rowThree+4));
		        	 rowThree=rowThree+4;
		        	 listRegion.add(region4);
				}
		       for (int i = 0; i < xh; i++) {
		        	 Region region4 = new Region(0, (short)(rowThree+1), 0, (short)(rowThree+2));
		        	 rowThree=rowThree+2;
		        	 listRegion.add(region4);
				}
		        //Region region4 = new Region(0, (short)3, 0, (short)7);
		   
		        sheet.addMergedRegion(region1);
		        sheet.addMergedRegion(region2);
		        sheet.addMergedRegion(region3);
		        for (int i = 0; i < listRegion.size(); i++) {
		        	 sheet.addMergedRegion(listRegion.get(i));
				}
		        //sheet.addMergedRegion(region4);
				

				// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
				//获取当前用户权限
				List list =new ArrayList();
				int n = 2;
				if (list == null) {
					return;
				}
				int m = 0;
				
				for (int i = 0; i < lists.size(); i++) {

						Map map = (HashMap) lists.get(i);
						row = sheet.createRow((int) n++);
						// 第四步，创建单元格，并设置值
						HSSFCell cell1 = row.createCell((short) 0);
						cell1.setCellValue(map.get("zqdm").toString());
						cell1.setCellStyle(style2);
						HSSFCell cell2 = row.createCell((short) 1);
						cell2.setCellValue(map.get("zqjc").toString());
						cell2.setCellStyle(style2);
						
						
						
						
						HSSFCell cell3 = row.createCell((short) 2);
						cell3.setCellValue(map.get("username").toString());
						cell3.setCellStyle(style2);
						/*HSSFCell cell4 = row.createCell((short) 3);
						cell4.setCellValue(map.get("as1").toString());
						cell4.setCellStyle(style2);*/
						int nn=1;
						int ss=0;
						for (int j = 0; j < wtlist.size(); j++) {
							if(ss>=s){
								cell3= row.createCell((short) 2+nn);
								nn++;
								cell3.setCellValue(map.get("as"+(j+1)).toString());
								cell3.setCellStyle(style2);
								cell3= row.createCell((short) 2+nn);
								nn++;
								cell3.setCellValue(map.get("bz"+(j+1)).toString());
								cell3.setCellStyle(style1);
							}else{
								cell3= row.createCell((short) 2+nn);
								nn++;
								cell3.setCellValue(map.get("as"+(j+1)).toString());
								cell3.setCellStyle(style2);
								cell3= row.createCell((short) 2+nn);
								nn++;
								cell3.setCellValue(map.get("pl"+(j+1)).toString());
								cell3.setCellStyle(style2);
								cell3= row.createCell((short) 2+nn);
								nn++;
								cell3.setCellValue(map.get("yj"+(j+1)).toString());
								cell3.setCellStyle(style2);
								cell3= row.createCell((short) 2+nn);
								nn++;
								cell3.setCellValue(map.get("bz"+(j+1)).toString());
								cell3.setCellStyle(style1);
							}
							
							ss++;
						}
						m++;

				}
				
		        sheet.setColumnWidth(0, 2500);
				sheet.setColumnWidth(1, 2500);
				sheet.setColumnWidth(2, 2500);
				for (int i = 3; i <rowbs; i++) {
					
					if(i <=rowbs-xh){
						sheet.setColumnWidth(i,2500);
						if((i-2)%4==0){
							sheet.setColumnWidth(i,12000);
						}
					}else{
						sheet.setColumnWidth(i,2500);
						if((i-2)%2==0){
							sheet.setColumnWidth(i,12000);
						}
					}
				}
		
				//System.out.println(sheet.getRow(0).getPhysicalNumberOfCells());
				//System.out.println(sheet.getRow(1).getPhysicalNumberOfCells());
				OutputStream out1 = null;
				// 第六步，将文件存到指定位置
				try {
					String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("信披自查反馈统计.xls");
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
		 /**
		  * 判断是xls还是xlsx
		  * @param inp
		  * @return
		  * @throws IOException
		  * @throws InvalidFormatException
		  */
		 public Workbook createExcel(InputStream inp) throws IOException,InvalidFormatException {
			   if (!inp.markSupported()) {
			        inp = new PushbackInputStream(inp, 8);
			    }
			    if (POIFSFileSystem.hasPOIFSHeader(inp)) {
			        return new HSSFWorkbook(inp);
			    }
			    if (POIXMLDocument.hasOOXMLHeader(inp)) {
			        return new XSSFWorkbook(OPCPackage.open(inp));
			    }
			    throw new IllegalArgumentException("你的excel版本目前poi解析不了");
			}
		 /**
		  * 在线考试导入试卷
		  * @param upfile
		  * @param tkstdemid
		  * @param sjdemid
		  * @return
		  * @throws Exception
		  */
		 @SuppressWarnings(value={"unchecked", "deprecation","unused","rawtypes"})
			public String doExcelImp(File upfile,Long tkstdemid,Long sjdemid) throws Exception{
			 	 String xxid = "";
			 	 String dxtid = "";
			 	 String fxtid ="";
			 	 String pdtid = "";
			 	 String fxtdemId = "";
			 	 String fxtformid = "";
			 	 String dxtformid = "";
			 	 String pdtdemId = "";
			 	 String pdtformid = "";
			 	 String xxdemId = "";
			 	 String xxformid = "";
			 	 String dxtdemId= "";
			 	 List lables = new ArrayList();
			 	 lables.add("ID");
		         lables.add("BS");
		         StringBuffer sql= new StringBuffer();
		         sql.append(" select id,'fxtdemId' bs from sys_engine_metadata s where s.entitytitle='试卷复选题目子表' union  ");
		         sql.append(" select id,'fxtformid' from sys_engine_iform s where s.iform_title='试卷复选题目表单'  union  ");
		         sql.append(" select id,'dxtdemId' from sys_engine_metadata s where s.entitytitle='试卷单选题目子表'  union  ");
		         sql.append(" select id,'dxtformid' from sys_engine_iform s where s.iform_title='试卷单选题目表单'  union ");
		         sql.append(" select id,'pdtdemId' from sys_engine_metadata s where s.entitytitle='试卷判断题目子表'  union  ");
		         sql.append(" select id,'pdtformid' from sys_engine_iform s where s.iform_title='试卷判断题目表单'  union  ");
		         sql.append(" select id,'xxdemId' from sys_engine_metadata s where s.entitytitle='题库试题选项表'  union  ");
		         sql.append(" select id,'xxformid' from sys_engine_iform s where s.iform_title='题库试题选项表单'    union  ");
		         sql.append(" SELECT MAX(id)+1,'xxid' ID FROM BD_EXAM_STOREITEM_S  union  ");
		         sql.append(" SELECT MAX(id)+1,'dxtid' ID FROM BD_EXAM_PAPER_RADIO  union  ");
		         sql.append(" SELECT MAX(id)+1,'fxtid' ID FROM BD_EXAM_PAPER_CHECK  union ");
		         sql.append(" SELECT MAX(id)+1,'pdtid' ID FROM BD_EXAM_PAPER_JUDGE  ");
		         List<HashMap> lists = DBUTilNew.getDataList(lables, sql.toString(), null);
		         for (int i = 0; i < lists.size(); i++) {
					if(lists.get(i).get("BS").toString().equals("xxid")) xxid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("dxtid")) dxtid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("fxtid")) fxtid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("pdtid")) pdtid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("fxtdemId")) fxtdemId=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("fxtformid")) fxtformid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("dxtdemId")) dxtdemId=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("dxtformid")) dxtformid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("pdtdemId")) pdtdemId=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("pdtformid")) pdtformid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("xxdemId")) xxdemId=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
					else if(lists.get(i).get("BS").toString().equals("xxformid")) xxformid=lists.get(i).get("ID")==null?null:lists.get(i).get("ID").toString();
				 }
		        
			    
			 	
				String userid = UserContextUtil.getInstance().getCurrentUserId();
			    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			    OrgUser user = uc.get_userModel();
			    String username=userid+"["+user.getUsername()+"]";
			    String msg = "success";
				PreparedStatement ps = null;
				PreparedStatement sjstxxgxpst = null;  //试题选项关系
				PreparedStatement stxxpst = null;  //试题选项
				PreparedStatement dxtpst = null;  //单选题
				PreparedStatement fxtpst = null;	//复选题
				PreparedStatement pdtpst = null;	//判断题
				ResultSet rs = null;
				
				StringBuffer dxtSql = new StringBuffer();
				dxtSql.append("insert into BD_EXAM_PAPER_RADIO(id,no,name,scorenum,orderby) values (?,?,?,?,?)");
				StringBuffer fxtSql = new StringBuffer();
				fxtSql.append("insert into BD_EXAM_PAPER_CHECK(id,no,name,scorenum,orderby) values (?,?,?,?,?)");
				StringBuffer pdtSql = new StringBuffer();
				pdtSql.append("insert into BD_EXAM_PAPER_JUDGE(id,no,name,scorenum,orderby) values (?,?,?,?,?)");
				StringBuffer stxxSql = new StringBuffer();
				stxxSql.append("insert into BD_EXAM_STOREITEM_S(id,itemcode,itemtitle) values (?,?,?)");
				StringBuffer sjstxxgxSql = new StringBuffer();
				sjstxxgxSql.append(" insert into sys_engine_form_bind(instanceid,formid,metadataid,dataid,engine_type,id) values(?,?,?,?,?,?)");
				try {
				Connection conn = DBUtil.open();
				
				dxtpst=conn.prepareStatement(dxtSql.toString());
				fxtpst=conn.prepareStatement(fxtSql.toString());
				pdtpst=conn.prepareStatement(pdtSql.toString());
				stxxpst=conn.prepareStatement(stxxSql.toString());
				sjstxxgxpst=conn.prepareStatement(sjstxxgxSql.toString());
			    
			     /* InputStream is = new FileInputStream(upfile);
			      HSSFWorkbook hwk = new HSSFWorkbook(is);
			      HSSFSheet sheet = hwk.getSheetAt(0);*/
			      InputStream is = new FileInputStream(upfile);
			      Workbook wookbook = null;
			    	//调用下面的方法
			      wookbook = createExcel(is);
			      Sheet sheet = wookbook.getSheetAt(0);

			      int rowIndex = 0;
			      Row row = sheet.getRow(rowIndex);
			      Cell cell = null;
			      row = sheet.getRow(rowIndex);
			      Hashtable fields = new Hashtable();
			      for (short i = 0; i < row.getLastCellNum(); i = (short)(i + 1)) {
			        cell = row.getCell(i);
			        if ((cell != null) && (cell.getCellType() != 0))
			        {
			          String fieldName = "";
			          String temp = cell.getStringCellValue();
			          if (!"".equals(temp)) {
			            if (temp.indexOf("<") > 0)
			              fieldName = temp.substring(0, cell.getStringCellValue().indexOf("<"));
			            else {
			              fieldName = temp;
			            }
			            fields.put(new Short(i), fieldName);
			          }
			        }
			      }
			      if (fields.size() < 1) {
			        return "typeerror";
			      }
			      //开始存试卷的内容
			      String value = "";
			      row = sheet.getRow(0);
			      cell = row.getCell(0);
			      if (cell == null) {
		              value = "";
		           }else if (cell.getCellType() == 0){
		              if (HSSFDateUtil.isCellDateFormatted(cell)) {
		                value = UtilDate.dateFormat(cell.getDateCellValue());
		              } else {
		                value = Double.toString(cell.getNumericCellValue());
		                if (value != null) {
		                  if (value.toUpperCase().indexOf("E") > 0){
		                    value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
		                    value = value.replaceAll(",", "");
		                  }
		                  if ((value.indexOf(".0") > 0) && (value.lastIndexOf("0") == value.length() - 1))
		                    value = value.substring(0, value.length() - 2);
		                }
		              }
		            } else if (cell.getCellType() == 2) {
			              try {
				                value = String.valueOf(cell.getNumericCellValue());
				              } catch (Exception e) {
				                value = String.valueOf(cell.getRichStringCellValue());
				              }
				            } else {
				              value = cell.getStringCellValue();
				              if (value == null) {
				                value = "";
				              }
				    }
				  value = value.trim();
				  //试卷的map集合
				  HashMap sjdata = new HashMap();
				  sjdata.put("NAME", value);
				  row = sheet.getRow(2);
				  String lm="";
				  for (short column = 0; column < row.getLastCellNum(); column = (short)(column + 1)) {
					  cell = row.getCell(column);
					  if (cell == null) {
			              value = "";
			           }else if (cell.getCellType() == 0){
			              if (HSSFDateUtil.isCellDateFormatted(cell)) {
			                value = UtilDate.dateFormat(cell.getDateCellValue());
			              } else {
			                value = Double.toString(cell.getNumericCellValue());
			                if (value != null) {
			                  if (value.toUpperCase().indexOf("E") > 0){
			                    value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
			                    value = value.replaceAll(",", "");
			                  }
			                  if ((value.indexOf(".0") > 0) && (value.lastIndexOf("0") == value.length() - 1))
			                    value = value.substring(0, value.length() - 2);
			                }
			              }
			            } else if (cell.getCellType() == 2) {
				              try {
					                value = String.valueOf(cell.getNumericCellValue());
					              } catch (Exception e) {
					                value = String.valueOf(cell.getRichStringCellValue());
					              }
					            } else {
					              value = cell.getStringCellValue();
					              if (value == null) {
					                value = "";
					              }
					    }
					  value = value.trim();
					   
					  if(column==0) lm="NO";
					  else if(column==1) lm="STATUS";
					  else if(column==2) lm="TYPE";
					  else if(column==4) lm="BEGINDATE";
					  else if(column==5) lm="ENDDATE";
					  else if(column==6) lm="EXAMTIME";
					  else if(column==7) lm="DXT";
					  else if(column==8) lm="PDT";
					  else if(column==9) lm="FXT";
					  else if(column==10) lm="ZF";
					  if(lm!=null){
						  if(!sjdata.containsKey(lm))
							  sjdata.put(lm, value);
					  }
				  }
				  //试卷内容获取结束
				  //保存试卷
				  Long sjinstanceId =new Long(0);
				  if(sjdata.size()>=1){
					  sjinstanceId = DemAPI.getInstance().newInstance(sjdemid, userid);
					 DemAPI.getInstance().saveFormData(sjdemid, sjinstanceId, sjdata, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);	      
				  }
				  int dataNum = 0;
			      int erroecount = 0;
			      int count =1;
			      int bs=1;
			      int bs1=1;
			      //题库和选项
			     
			     
			      for (int i = 4; i <= sheet.getLastRowNum(); i++) {
			    	lm="";
			        row = sheet.getRow(i);
			        StringBuffer bindData = new StringBuffer();
			        //题库map集合
			        HashMap rowdata = new HashMap();
			        //试题选项map集合
			        HashMap xxdata = new HashMap();
			       
			        for (short column = 0; column < row.getLastCellNum(); column = (short)(column + 1)) {
			        	cell = row.getCell(column);
						  if (cell == null) {
				              value = "";
				           }else if (cell.getCellType() == 0){
				              if (HSSFDateUtil.isCellDateFormatted(cell)) {
				                value = UtilDate.dateFormat(cell.getDateCellValue());
				              } else {
				                value = Double.toString(cell.getNumericCellValue());
				                if (value != null) {
				                  if (value.toUpperCase().indexOf("E") > 0){
				                    value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
				                    value = value.replaceAll(",", "");
				                  }
				                  if ((value.indexOf(".0") > 0) && (value.lastIndexOf("0") == value.length() - 1))
				                    value = value.substring(0, value.length() - 2);
				                }
				              }
				            } else if (cell.getCellType() == 2) {
					              try {
						                value = String.valueOf(cell.getNumericCellValue());
						              } catch (Exception e) {
						                value = String.valueOf(cell.getRichStringCellValue());
						              }
						            } else {
						              value = cell.getStringCellValue();
						              if (value == null) {
						                value = "";
						              }
						    }
						  value = value.trim();
						  if(column==0) lm="NO";
						  else if(column==1) lm="TITLE";
						  else if(column==2) lm="TOPICTYPE";
						  else if(column==3) lm="SCORENUM";
						  else if(column==4) lm="xx1";
						  else if(column==5) lm="xx2";
						  else if(column==6) lm="xx3";
						  else if(column==7) lm="xx4";
						  else if(column==8) lm="xx5";
						  else if(column==9) lm="xx6";
						  else if(column==10) lm="ANSWER";
						
						  if(column<4 || column==10){
							  if(lm!=null){
								  if(!rowdata.containsKey(lm))
									  rowdata.put(lm, value);
							  }
						  }else{
							  if(lm!=null){
								  if(!xxdata.containsKey(lm))
									  xxdata.put(lm, value);
							  }
						  }
						
			        }
			        Long tkstinstanceId=new Long(0);
			        if(sjinstanceId!=0){
						 // 保存题库试题
				        if(rowdata.size()>=1){
				        	String xxType=rowdata.get("TOPICTYPE")==null?"":rowdata.get("TOPICTYPE").toString();
							 tkstinstanceId = DemAPI.getInstance().newInstance(tkstdemid, userid);
							 if(!"".equals(rowdata.get("TITLE")==null?"":rowdata.get("TITLE").toString())){
								 DemAPI.getInstance().saveFormData(tkstdemid, tkstinstanceId,rowdata, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
							 }
							
							 String zbbcSql="";
							 int noStr = 1;
							 if(xxType.equals("单选题")){
									if (dxtid == null) noStr = 1+bs;
									else { noStr = Integer.parseInt(dxtid)+bs; }
									dxtpst.setInt(1, noStr);
									dxtpst.setString(2, rowdata.get("NO")==null?"":rowdata.get("NO").toString());
									dxtpst.setString(3, rowdata.get("TITLE")==null?"":rowdata.get("TITLE").toString());
									dxtpst.setString(4, rowdata.get("SCORENUM")==null?"":rowdata.get("SCORENUM").toString());
									dxtpst.setLong(5, 0L);
									dxtpst.addBatch();
									//
									sjstxxgxpst.setLong(1, sjinstanceId);
									sjstxxgxpst.setString(2, dxtformid);
									sjstxxgxpst.setString(3, dxtdemId);
									sjstxxgxpst.setLong(4, noStr);
									sjstxxgxpst.setLong(5, 0L);
									sjstxxgxpst.setString(6, UUID.randomUUID().toString().replaceAll("-", ""));
									sjstxxgxpst.addBatch();
							 }else if(xxType.equals("复选题")){
								
								 if (fxtid == null) noStr = 1+bs;
									else { noStr = Integer.parseInt(fxtid)+bs; }
								 fxtpst.setInt(1, noStr);
									fxtpst.setString(2, rowdata.get("NO")==null?"":rowdata.get("NO").toString());
									fxtpst.setString(3, rowdata.get("TITLE")==null?"":rowdata.get("TITLE").toString());
									fxtpst.setString(4, rowdata.get("SCORENUM")==null?"":rowdata.get("SCORENUM").toString());
									fxtpst.setLong(5, 0L);
									fxtpst.addBatch();
									sjstxxgxpst.setLong(1, sjinstanceId);
									sjstxxgxpst.setString(2, fxtformid);
									sjstxxgxpst.setString(3, fxtdemId);
									sjstxxgxpst.setLong(4, noStr);
									sjstxxgxpst.setLong(5, 0L);
									sjstxxgxpst.setString(6, UUID.randomUUID().toString().replaceAll("-", ""));
									sjstxxgxpst.addBatch();
							 }else if(xxType.equals("判断题")){
								
								 if (pdtid == null) noStr = 1+bs;
									else { noStr = Integer.parseInt(pdtid)+bs; }
								 	pdtpst.setInt(1, noStr);
									pdtpst.setString(2, rowdata.get("NO")==null?"":rowdata.get("NO").toString());
									pdtpst.setString(3, rowdata.get("TITLE")==null?"":rowdata.get("TITLE").toString());
									pdtpst.setString(4, rowdata.get("SCORENUM")==null?"":rowdata.get("SCORENUM").toString());
									pdtpst.setLong(5, 0L);
									pdtpst.addBatch();
									sjstxxgxpst.setLong(1, sjinstanceId);
									sjstxxgxpst.setString(2, pdtformid);
									sjstxxgxpst.setString(3, pdtdemId);
									sjstxxgxpst.setLong(4, noStr);
									sjstxxgxpst.setLong(5, 0L);
									sjstxxgxpst.setString(6, UUID.randomUUID().toString().replaceAll("-", ""));
									sjstxxgxpst.addBatch();
							 }
							
				        }
			        }
			        if(tkstinstanceId!=0){
			        	if(xxdata.size()>=1){
			        		String xxbs="";
			        		for (int j = 0; j < 6; j++) {
			        			if(j==0) xxbs="A";
			        			if(j==1) xxbs="B";
			        			if(j==2) xxbs="C";
			        			if(j==3) xxbs="D";
			        			if(j==4) xxbs="E";
			        			if(j==5) xxbs="F";
			        			int noStr = 1;
								if (xxid == null) noStr = 1+bs1;
								else { noStr = Integer.parseInt(xxid)+bs1; }
								if(!"".equals(xxdata.get("xx"+(j+1)))){
									stxxpst.setInt(1, noStr);
									stxxpst.setString(2, xxbs);
									stxxpst.setString(3, xxdata.get("xx"+(j+1))==null?"":xxdata.get("xx"+(j+1)).toString());
									stxxpst.addBatch();
									bs1++;
									sjstxxgxpst.setLong(1, tkstinstanceId);
									sjstxxgxpst.setString(2, xxformid);
									sjstxxgxpst.setString(3, xxdemId);
									sjstxxgxpst.setLong(4, noStr);
									sjstxxgxpst.setLong(5, 0L);
									sjstxxgxpst.setString(6, UUID.randomUUID().toString().replaceAll("-", ""));
									sjstxxgxpst.addBatch();
								}
							}
			        	}
			        }
					erroecount++;
					dataNum++;
					count++;
					  bs++;
			      }
			      if (dataNum == 0) {
			        msg = "error";
			      }
			      if (erroecount > 0)
			        msg = "第" + erroecount + "条数据导入异常!";
			      dxtpst.executeBatch();
			      fxtpst.executeBatch();
			      pdtpst.executeBatch();
			      stxxpst.executeBatch();
			      sjstxxgxpst.executeBatch();
			      try {
						if(dxtpst!=null)dxtpst.close();
						if(fxtpst!=null)fxtpst.close();
						if(pdtpst!=null)pdtpst.close();
						if(stxxpst!=null)stxxpst.close();
						if(sjstxxgxpst!=null)sjstxxgxpst.close();
					} catch (Exception e) {
						
						
						
					}finally{
						
						DBUtil.close(conn, ps, rs);
					}
			    } catch (FileNotFoundException e) {
			      msg = "error";
			      e.printStackTrace(System.err);
			    } catch (Exception e) {
			      msg = "error";
			      e.printStackTrace(System.err);
			    }
			    return msg;
			  }
		 /**
			 * 在线考试模板下载
			 */
			public void zxksMbupload(){
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				String filepath = request.getSession().getServletContext().getRealPath("/");
				filepath=filepath+"\\iwork_file\\试卷导入模板.xlsx";
				try {
					InputStream fis = new BufferedInputStream(new FileInputStream(filepath));
					byte[] buffer = new byte[fis.available()];
					fis.read(buffer);
					fis.close();

					OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
					response.setContentType("application/octet-stream");
					response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("试卷导入模板.xlsx"));
					toClient.write(buffer);
					toClient.flush();
					toClient.close();
				} catch (FileNotFoundException e) {
					
				} catch (Exception e) {
					
				}
		}
			/**
		     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
		     * @param value 指定的字符串
		     * @return 字符串的长度
		     */
		    public static int length(String value) {
		        int valueLength = 0;
		        String chinese = "[\u0391-\uFFE5]";
		        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		        for (int i = 0; i < value.length(); i++) {
		            /* 获取一个字符 */
		            String temp = value.substring(i, i + 1);
		            /* 判断是否为中文字符 */
		            if (temp.matches(chinese)) {
		                /* 中文字符长度为2 */
		                valueLength += 2;
		            } else {
		                /* 其他字符长度为1 */
		                valueLength += 1;
		            }
		        }
		        return valueLength;
		    }
		    public static boolean isValidDate(String str) {
		        boolean convertSuccess=true;
		         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		         try {
		            format.setLenient(false);
		            format.parse(str);
		         } catch (ParseException e) {
		            // e.printStackTrace();
		  // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
		             convertSuccess=false;
		         } 
		         return convertSuccess;
		  }
		/**
		 * 底稿归档导入
		 * @param upfile
		 * @return
		 * @throws Exception
		 */
		public String doDggdImp(File upfile) throws Exception{
			String demid = getConfigUUID("kpid")==null?"":getConfigUUID("kpid");
			String formid = getConfigUUID("kpformid")==null?"":getConfigUUID("kpformid");
			String userid = UserContextUtil.getInstance().getCurrentUserId();
		    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		    OrgUser user = uc.get_userModel();
		    String username=userid+"["+user.getUsername()+"]";
		    String msg = "success";
		    List list =new ArrayList();
            list.add("CUSTOMERNAME");
		    List<HashMap> dataList = DBUTilNew.getDataList(list, "select CUSTOMERNAME from BD_ZQB_SQKP", null);
		    try {
		      HashMap params=null;
		      InputStream is = new FileInputStream(upfile);
		      Workbook wookbook = null;
		    	//调用下面的方法
		      wookbook = createExcel(is);
		      Sheet sheet = wookbook.getSheetAt(0);

		      int rowIndex = 0;
		      Row row = sheet.getRow(rowIndex);
		      Cell cell = null;
		      row = sheet.getRow(rowIndex);
		      Hashtable fields = new Hashtable();
		      for (short i = 0; i < row.getLastCellNum(); i = (short)(i + 1)) {
		        cell = row.getCell(i);
		        if ((cell != null) && (cell.getCellType() != 0))
		        {
		          String fieldName = "";
		          String temp = cell.getStringCellValue();
		          if (!"".equals(temp)) {
		            if (temp.indexOf("<") > 0)
		              fieldName = temp.substring(0, cell.getStringCellValue().indexOf("<"));
		            else {
		              fieldName = temp;
		            }
		            fields.put(new Short(i), fieldName);
		          }
		        }
		      }
		      if (fields.size() < 1) {
		        return "typeerror";
		      }
		      String lm="";
		      String value = "";
		    
			
			  int dataNum = 0;
		      int erroecount = 1;
		     String cwnr="";
		      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
		    	lm="";
		        row = sheet.getRow(i);
		        StringBuffer bindData = new StringBuffer();
		        HashMap rowdata = new HashMap();
		       
		       
		        for (short column = 0; column < row.getLastCellNum(); column = (short)(column + 1)) {
		        	cell = row.getCell(column);
					  if (cell == null) {
			              value = "";
			           }else if (cell.getCellType() == 0){
			              if (HSSFDateUtil.isCellDateFormatted(cell)) {
			                value = UtilDate.dateFormat(cell.getDateCellValue());
			              } else {
			                value = Double.toString(cell.getNumericCellValue());
			                if (value != null) {
			                  if (value.toUpperCase().indexOf("E") > 0){
			                    value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
			                    value = value.replaceAll(",", "");
			                  }
			                  if ((value.indexOf(".0") > 0) && (value.lastIndexOf("0") == value.length() - 1))
			                    value = value.substring(0, value.length() - 2);
			                }
			              }
			            } else if (cell.getCellType() == 2) {
				              try {
					                value = String.valueOf(cell.getNumericCellValue());
					              } catch (Exception e) {
					                value = String.valueOf(cell.getRichStringCellValue());
					              }
					            } else {
					              value = cell.getStringCellValue();
					              if (value == null) {
					                value = "";
					              }
					    }
					  value = value.trim();
					  if(column==0) lm="CUSTOMERNAME";
					  else if(column==1) lm="CUSTOMERNO";
					  else if(column==2) lm="STATE";
					  else if(column==3) lm="SQRQ";
					  else if(column==4) lm="NSRLX";
					  else if(column==5) lm="MEMO";
						  if(lm!=null && !"".equals(lm)){
							  if(!rowdata.containsKey(lm))
								  rowdata.put(lm, value);
						  }
					
					
		        }
		        erroecount++;
		        
		     if( rowdata.get("CUSTOMERNAME")==null || "".equals(rowdata.get("CUSTOMERNAME").toString()) ){
		    	 continue;
		     }
		     boolean flag=false;
		     if(dataList!=null && dataList.size()>0){
		    	 for (int j = 0; j < dataList.size(); j++) {
						if(rowdata.get("CUSTOMERNAME").toString().equals(dataList.get(j).get("CUSTOMERNAME"))){
							flag=true;
							break;
						}
				 } 
		     }
		     if(flag){
		    	 cwnr=cwnr+"第" + erroecount + "条数据导入异常:项目名称重复！\n";
		    	 continue;
		     }
		     params = new HashMap();
		     params.put("CUSTOMERNAME", rowdata.get("CUSTOMERNAME").toString());
		     dataList.add(params);
		     if(length(rowdata.get("CUSTOMERNAME").toString())>64){
		    	 cwnr=cwnr+"第" + erroecount + "条数据导入异常:项目名称字符串长度最多64！\n";
		    	 continue;
		     }
		     if( rowdata.get("CUSTOMERNO")!=null && !"".equals(rowdata.get("CUSTOMERNO").toString()) ){
		    	 if(length(rowdata.get("CUSTOMERNO").toString())>64){
			    	 cwnr=cwnr+"第" + erroecount + "条数据导入异常:项目成员字符串长度最多64！\n";
			    	 continue;
			     }
		     }
		     if( rowdata.get("STATE")!=null && !"".equals(rowdata.get("STATE").toString()) ){
			     if(!"已归档".equals(rowdata.get("STATE").toString()) && !"未归档".equals(rowdata.get("STATE").toString()) && !"延期归档".equals(rowdata.get("STATE").toString())){
			    	 cwnr=cwnr+"第" + erroecount + "条数据导入异常:归档状态类型有误！\n";
			    	 continue;
			     }
		     }
		     if( rowdata.get("NSRLX")!=null && !"".equals(rowdata.get("NSRLX").toString()) ){
			     if(length(rowdata.get("NSRLX").toString())>128){
			    	 cwnr=cwnr+"第" + erroecount + "条数据导入异常:归档位置字符串长度最多128！\n";
			    	 continue;
			     }
		     }
		     if( rowdata.get("MEMO")!=null && !"".equals(rowdata.get("MEMO").toString()) ){
			     if(length(rowdata.get("MEMO").toString())>512){
			    	 cwnr=cwnr+"第" + erroecount + "条数据导入异常:归档位置字符串长度最多512！\n";
			    	 continue;
			     }
		     }
		     if( rowdata.get("SQRQ")!=null && !"".equals(rowdata.get("SQRQ").toString()) ){
			     if(!isValidDate(rowdata.get("SQRQ").toString())){
			    	 cwnr=cwnr+"第" + erroecount + "条数据导入异常:归档时间日期类型不对！\n";
			    	 continue;
			     }
		     }
		        rowdata.put("USERID",userid);
		        rowdata.put("USERNAME",user.getUsername());
		        rowdata.put("FPLX","普票");
		        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            String time=sdf.format( new  Date());
	            rowdata.put("CJSJ",time);
	            Long instanceId =new Long(0);
	            String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE ID='"+demid+"'", "UUID");
	         
				if(rowdata.size()>=1){
					  instanceId = DemAPI.getInstance().newInstance(uuid, userid);
					  //DemAPI.getInstance().saveFormData(Long.valueOf(demid), instanceId, rowdata, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);	  
					  DemAPI.getInstance().saveFormData(uuid, instanceId, rowdata, false);
				  }
				dataNum++;
		      }
		      if (dataNum == 0) {
		        msg = "error";
		      }
		   /*   if (erroecount > 0)
		        msg = "第" + erroecount + "条数据导入异常!";*/
				if(cwnr!="") msg=cwnr;
		    } catch (FileNotFoundException e) {
		      msg = "error";
		      e.printStackTrace(System.err);
		    } catch (Exception e) {
		      msg = "error";
		      e.printStackTrace(System.err);
		    }
		    return msg;
		}
		
		 /**
		 * 底稿归档模板下载
		 */
		public void dggdMbupload(){
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String filepath = request.getSession().getServletContext().getRealPath("/");
			filepath=filepath+"\\iwork_file\\项目底稿归档模板.xlsx";
			try {
				InputStream fis = new BufferedInputStream(new FileInputStream(filepath));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();

				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目底稿归档模板.xlsx"));
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			} catch (FileNotFoundException e) {
				
			} catch (Exception e) {
				
			}
	}
		/**
		 *  工作日志姓名选择器 工作日志部门选择器
		 * @param name
		 * @return
		 */
		public List<HashMap> getGzrzxmxzq(String name) {
			List params = new ArrayList();
			StringBuffer sql = new StringBuffer("select s.userid,s.username,s.departmentname,s.mobile,s.email from orguser s where 1=1 and orgroleid<>3 and  sysdate<s.enddate and  s.userid not in('DUDAO','DUDAO1','10086','888888','DUDAO2','DUDAO3','GUANLIYUAN') ");
			if(name!=null&&!name.equals("")){
				sql.append(" AND s.username LIKE ?");
				params.add("%"+name.trim()+"%");
			}
			sql.append(" order by s.username ");
			List<HashMap> list = new ArrayList<HashMap>();
			HashMap map;
			Connection conn = DBUtil.open();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql.toString());
				for (int i = 0; i < params.size(); i++) {
					ps.setObject(i+1, params.get(i));
				}
				rs = ps.executeQuery();
				while(rs.next()){
					map = new HashMap();
					String userid = rs.getString("userid");
					String shouzimu = userid.substring(0, 1);
					String username = rs.getString("username");
					String departmentname = rs.getString("departmentname");
					String mobile = rs.getString("mobile");
					String email= rs.getString("email");
					map.put("userid", userid==null ? "":userid);
					map.put("username", username==null ? "":shouzimu+"-"+username);
					map.put("departmentname", departmentname==null ? "":departmentname);
					map.put("mobile", mobile==null ? "":mobile);
					map.put("email", email==null ? "":email);
					list.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
			return list;
		}
		/**
		 *  工作日志部门选择器
		 * @param name
		 * @return
		 */
		public List<HashMap> getGzrzxmxzqBM(String name) {
			List params = new ArrayList();
			StringBuffer sql = new StringBuffer("select s.departmentname,s.departmentno from orgdepartment s where 1=1 and s.departmentno is null ");
			if(name!=null&&!name.equals("")){
				sql.append(" AND s.departmentname LIKE ?");
				params.add("%"+name.trim()+"%");
			}
			List<HashMap> list = new ArrayList<HashMap>();
			HashMap map;
			Connection conn = DBUtil.open();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql.toString());
				for (int i = 0; i < params.size(); i++) {
					ps.setObject(i+1, params.get(i));
				}
				rs = ps.executeQuery();
				while(rs.next()){
					map = new HashMap();
					String departmentname = rs.getString("departmentname");
					String companyno = rs.getString("departmentno");
					map.put("departmentname", departmentname==null ? "":departmentname);
					map.put("companyno", companyno==null ? "":companyno);
				
					list.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
			return list;
		}
		public List<HashMap> getReportList(int pageNumber, int pageSize, String departmentname, String username) {
			return zqbAnnouncementDAO.getReportList(pageNumber, pageSize, departmentname, username);
		}

		public int getReportListSize(String departmentname, String username) {
			return zqbAnnouncementDAO.getReportListSize(departmentname, username).size();
		}
		public List<HashMap> getReportList(int pageNumber, int pageSize, String userid) {
			return zqbAnnouncementDAO.getReportList(pageNumber, pageSize, userid);
		}

		public int getReportListSize(String userid) {
			return zqbAnnouncementDAO.getReportListSize(userid).size();
		}

		public void delReport(Long instanceid) {
			boolean flag = DemAPI.getInstance().removeFormData(instanceid);
			ResponseUtil.write(String.valueOf(flag));
		}
		public String showGzbcXx(Long cid){
            String jsonObject=null;
            List lables =new ArrayList();
            lables.add("TZBT");
            lables.add("ZCHFSJ");
            lables.add("TZNR");
            lables.add("XGZL");
            JSONArray json = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            List<HashMap> list=DBUTilNew.getDataList(lables,"select TZBT,ZCHFSJ,TZNR,XGZL from BD_XP_GZSC where id="+cid,null);
            if(list!=null && list.size()>0){
                String tzbd=list.get(0).get("TZBT")==null?"":list.get(0).get("TZBT").toString();
                String tzsj=list.get(0).get("ZCHFSJ")==null?"":list.get(0).get("ZCHFSJ").toString();
                String tznr=list.get(0).get("TZNR")==null?"":list.get(0).get("TZNR").toString();
                String xgzl=list.get(0).get("XGZL")==null?"":list.get(0).get("XGZL").toString();
                StringBuffer sql=new StringBuffer();
                if(xgzl!=null && !"".equals(xgzl)){
                    List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(xgzl);
                    if(files!=null && files.size()>0){
                        int n=1;
                        for(int i=0;i<files.size();i++){
                            FileUpload file = files.get(i);
                            String filesrcname = file.getFileSrcName();
                            String file_id = file.getFileId();
                            String uploadtime = file.getUploadTime();
                            String icon = FileType.getFileIcon(filesrcname);
                            sql.append("<div  id='"+file_id+"' style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">\n");
                            sql.append("<div style=\"align:right;float:right;\">&nbsp;&nbsp;</div>\n");
                            sql.append("<span>"+n+"、<a href='uploadifyDownload.action?fileUUID="+file_id+"' target='_blank'><img style='margin:3px' src='"+icon+"'/>"+filesrcname+"</a></span>\n");
                            sql.append("</div>\n");
                            n++;
                        }
                    }
                }
                jsonObj.put("TZBT",tzbd);
                jsonObj.put("ZCHFSJ",tzsj);
                jsonObj.put("TZNR",tznr);
                jsonObj.put("XGZL",sql.toString());

            }
            jsonObject=JSONObject.fromObject(jsonObj).toString();
		    return jsonObject;
        }
}
