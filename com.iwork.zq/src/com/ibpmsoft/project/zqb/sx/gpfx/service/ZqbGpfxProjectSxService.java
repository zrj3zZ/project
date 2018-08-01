package com.ibpmsoft.project.zqb.sx.gpfx.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.Region;
import com.ibpmsoft.project.zqb.sx.gpfx.dao.ZqbGpfxProjectSxDAO;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import org.apache.log4j.Logger;

public class ZqbGpfxProjectSxService {
	private ZqbGpfxProjectSxDAO zqbGpfxProjectSxDAO;
	private static Logger logger = Logger.getLogger(ZqbGpfxProjectSxService.class);
	public void setZqbGpfxProjectSxDAO(ZqbGpfxProjectSxDAO zqbGpfxProjectSxDAO) {
		this.zqbGpfxProjectSxDAO = zqbGpfxProjectSxDAO;
	}

	public List<HashMap> getRunlist(int pageSize, int pageNumber, String customername, String sshy, String czbm, String cyrname) {
		return zqbGpfxProjectSxDAO.getRunlist(pageSize, pageNumber, customername, sshy, czbm, cyrname);
	}

	public List<HashMap> getRunlistSize(String customername, String sshy, String czbm, String cyrname) {
		return zqbGpfxProjectSxDAO.getRunlistSize(customername, sshy, czbm, cyrname);
	}

	public List<HashMap> getCloselist(int pageSize1, int pageNumber1, String customername, String sshy, String czbm, String cyrname) {
		return zqbGpfxProjectSxDAO.getCloselist(pageSize1, pageNumber1, customername, sshy, czbm, cyrname);
	}

	public List<HashMap> getCloselistSize(String customername, String sshy, String czbm, String cyrname) {
		return zqbGpfxProjectSxDAO.getCloselistSize(customername, sshy, czbm, cyrname);
	}
	
	public String projectCloseDialog(String instanceids){
		StringBuffer commonstr = new StringBuffer();
		String[] insarr = instanceids.split(",");
		String[] pnamearr = new String[insarr.length];
		DemAPI demapi = DemAPI.getInstance();
		HashMap map;
		for (int i = 0; i < insarr.length; i++) {
			map = demapi.getFromData(Long.parseLong(insarr[i]));
			pnamearr[i] = map.get("PROJECTNAME")+"-"+map.get("GPFXJZ")+"</br>";
			commonstr.append(pnamearr[i]);
		}
		return commonstr.toString();
	}
	
	public String projectXmjdContent(String projectno) {
		StringBuffer sql = new StringBuffer("SELECT A.PROJECTNO,NVL(A.LCBS,'') LCBS,NVL(A.LCBH,'') LCBH,NVL(A.TASKID,'') TASKID,NVL(A.STEPID,'') STEPID,");
			       sql.append(" NVL(A.CREATEUSER,'') CREATEUSER,NVL(A.FORMID,'') FORMID,NVL(B.JDMC,'') TASK_NAME,NVL(A.SPZT,'') SPZT,");
			       sql.append(" NVL(A.EXCUTIONID,'') EXCUTIONID,MBNUM,B.ID JDBH,MBNAME,MB"); 
			       sql.append(" FROM BD_ZQB_TYXM_INFO B LEFT JOIN (");
			       sql.append(" SELECT * FROM (");
			       sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.LCBS,BOTABLE.LCBH,BOTABLE.TASKID,BOTABLE.STEPID,BOTABLE.CREATEUSER,BOTABLE.SPZT,BOTABLE.EXCUTIONID,BOTABLE.GROUPID,BINDTABLE.FORMID FROM BD_ZQB_GPFXXMNFXB BOTABLE" );
			       sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='拟发行信息')");
			     
			       sql.append(" union all");
			       sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.LCBS,BOTABLE.LCBH,BOTABLE.TASKID,BOTABLE.STEPID,BOTABLE.CREATEUSER,BOTABLE.SPZT,BOTABLE.EXCUTIONID,BOTABLE.GROUPID,BINDTABLE.FORMID FROM BD_ZQB_GPFXXMFAZLBSB BOTABLE");
			       sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='方案资料报审信息')");
			     
			       sql.append(" union all");
			       sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.LCBS,BOTABLE.LCBH,BOTABLE.TASKID,BOTABLE.STEPID,BOTABLE.CREATEUSER,BOTABLE.SPZT,BOTABLE.EXCUTIONID,BOTABLE.GROUPID,BINDTABLE.FORMID FROM BD_ZQB_GPFXXMSBZLSB BOTABLE");
			       sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='申报资料信息')");
		           
			       sql.append(" union all");
			       sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.LCBS,BOTABLE.LCBH,BOTABLE.TASKID,BOTABLE.STEPID,BOTABLE.CREATEUSER,BOTABLE.SPZT,BOTABLE.EXCUTIONID,BOTABLE.GROUPID,BINDTABLE.FORMID FROM BD_ZQB_GPFXXMSJFXB BOTABLE");
			       sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='实际发行信息')");
			     
			       sql.append(" )");
			       sql.append(" WHERE PROJECTNO=?");
			     
			       sql.append(" ) A ON B.ID = A.GROUPID" );

			       sql.append(" LEFT JOIN (SELECT JDBH,COUNT(*) MBNUM FROM BD_ZQB_TYXMZLB GROUP BY JDBH) C ON B.ID = C.JDBH" );
			       sql.append(" LEFT JOIN  (SELECT A.ID,A.JDBH,D.FILE_SRC_NAME MBNAME,A.JDZL MB FROM BD_ZQB_TYXMZLB A LEFT JOIN SYS_UPLOAD_FILE D ON A.JDZL = D.FILE_ID) D ON B.ID = D.JDBH"); 

			       sql.append(" WHERE B.XMLX='股票发行项目' AND B.STATE=1 ORDER BY B.SORTID,MB");
		StringBuffer content = new StringBuffer();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rsZL = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, projectno);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				content.append("<tr class=\"ui-widget-content jqgrow ui-row-ltr\" role=\"row\">");
					content.append("<td title=\""+rs.getString("TASK_NAME")+"\" style=\"padding-left:10px;\" role=\"gridcell\">"+rs.getString("TASK_NAME")+"</td>");
					content.append("<td title=\""+(rs.getString("CREATEUSER")==null?"":rs.getString("CREATEUSER"))+"\" style=\"\" role=\"gridcell\">"+(rs.getString("CREATEUSER")==null?"":rs.getString("CREATEUSER"))+"</td>");
					content.append("<td title=\"阶段资料\" role=\"gridcell\">");
					String mb = rs.getString("MB")==null?"":rs.getString("MB");
					List<FileUpload> l = null;
					if(!mb.equals("")){
						l = FileUploadAPI.getInstance().getFileUploads(mb);
						if(l!=null){
							for (int i = 0; i < l.size(); i++) {
								if(i==0){
									content.append("<a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+l.get(i).getFileId()+"\">"+l.get(i).getFileSrcName()+"</a>");
								}else{
									content.append("</br><a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+l.get(i).getFileId()+"\">"+l.get(i).getFileSrcName()+"</a>");
								}
							}
						}
					}
					content.append("</td>");
					content.append("<td title=\""+(rs.getString("SPZT")==null?"":rs.getString("SPZT"))+"\" role=\"gridcell\">"+(rs.getString("SPZT")==null?"":rs.getString("SPZT"))+"</td>");
					content.append("<td role=\"gridcell\">");
						if(rs.getString("SPZT")==null){
							content.append("<a href=\"javascript:reportXfx("+rs.getString("JDBH")+",'"+rs.getString("TASK_NAME")+"')\">呈报</a>");
						}else{
							content.append("<a href=\"javascript:submitXfx('"+rs.getString("LCBS")+"','"+rs.getString("EXCUTIONID")+"','"+rs.getString("EXCUTIONID")+"','"+rs.getString("TASKID")+"','"+rs.getString("TASK_NAME")+"')\">查看</a>");
						}
					content.append("</td>");
				content.append("</tr>");
				}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rsZL);
		}
		return content.toString();
	}
	
	public String projectMainMsgContent(String projectno){
		StringBuffer content = new StringBuffer();
		List lables = new ArrayList();
		lables.add("CUSTOMERNAME");
		lables.add("PROJECTNAME");
		lables.add("OWNER");
		lables.add("MANAGER");
		lables.add("KHLXR");
		lables.add("KHLXDH");
		lables.add("CLBM");
		lables.add("CZBM");
		lables.add("GPFXJZ");
		lables.add("STARTDATE");
		Map params = new HashMap();
		params.put(1, projectno);
		List<HashMap> l = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT * FROM BD_ZQB_GPFXXMB WHERE PROJECTNO=?", params);
		HashMap data = l.get(0);
		content.append("<fieldset style=\"margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC\">");
		content.append("<legend style=\"color:#004080;\" algin=\"right\">项目基本情况</legend>");
		content.append("<table width=\"100%\">");
		content.append("<tbody><tr id=\"itemTr_0\">");
		content.append("<td width=\"50%\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		content.append("客户名称<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("CUSTOMERNAME")==null?"":data.get("CUSTOMERNAME"))+"\" style=\"width:266px;\" readonly=\"readonly\" class=\"{maxlength:256,string:true}\"\">");
		content.append("</td>");
		content.append("<td width=\"50%\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		content.append("项目名称<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("PROJECTNAME")==null?"":data.get("PROJECTNAME"))+"\" style=\"width:240px;\" readonly=\"readonly\" class=\"{maxlength:256,string:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_1\">");
		content.append("<td width=\"50%\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;大区负责人<input type=\"text\" value=\""+(data.get("OWNER")==null?"":data.get("OWNER"))+"\" style=\"background-color:#efefef;width:265px\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\"{maxlength:130,required:true}\"\">");
		content.append("</td>");
		content.append("<td width=\"50%\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		content.append("项目负责人<input type=\"text\" value=\""+(data.get("MANAGER")==null?"":data.get("MANAGER"))+"\" style=\"background-color:#efefef;width:240px\" \"=\"\" =\"readonly=\"\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\"{maxlength:130,required:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_2\">");
		content.append("<td width=\"50%\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;客户联系人<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("KHLXR")==null?"":data.get("KHLXR"))+"\" style=\"width:265px;\" readonly=\"readonly\" class=\"{maxlength:32,required:false,string:true}\">");
		content.append("</td>");
		content.append("<td width=\"50%\" id=\"data_CLBM\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		content.append("承揽部门<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("CLBM")==null?"":data.get("CLBM"))+"\" style=\"width:240px;\" readonly=\"readonly\" class=\"{maxlength:256,required:true,string:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_3\">");
		content.append("<td width=\"50%\">");
		content.append("客户联系电话<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("KHLXDH")==null?"":data.get("KHLXDH"))+"\" style=\"width:265px;\" readonly=\"readonly\" class=\"{maxlength:32,required:false,string:true}\">");
		content.append("</td>");
		content.append("<td width=\"50%\" id=\"data_CZBM\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		content.append("承做部门<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("CZBM")==null?"":data.get("CZBM"))+"\" style=\"width:240px;\" readonly=\"readonly\" class=\"{maxlength:256,required:true,string:true}\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_4\">");
		content.append("<td width=\"50%\">");
		content.append("股票发行进展<select disabled=\"disabled\" style=\"width:273px;\">");
		content.append("<option value=\""+(data.get("GPFXJZ")==null?"":data.get("GPFXJZ"))+"\">"+(data.get("GPFXJZ")==null?"":data.get("GPFXJZ"))+"</option>");
		content.append("</select>");
		content.append("</td>");
		content.append("<td width=\"50%\">");
		content.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		content.append("项目发起日期<input type=\"text\" value=\""+(data.get("STARTDATE")==null?"":data.get("STARTDATE"))+"\" style=\"width:240px\" readonly=\"readonly\" class=\"{required:true}\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("</tbody></table>");
		content.append("</fieldset>");
		
		return content.toString();
	}
	public String wxprojectMainMsgContent(String projectno){
		StringBuffer content = new StringBuffer();
		List lables = new ArrayList();
		lables.add("CUSTOMERNAME");
		lables.add("PROJECTNAME");
		lables.add("OWNER");
		lables.add("MANAGER");
		lables.add("KHLXR");
		lables.add("KHLXDH");
		lables.add("CLBM");
		lables.add("CZBM");
		lables.add("GPFXJZ");
		lables.add("STARTDATE");
		Map params = new HashMap();
		params.put(1, projectno);
		List<HashMap> l = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT * FROM BD_ZQB_GPFXXMB WHERE PROJECTNO=?", params);
		HashMap data = l.get(0);
		content.append("<fieldset style=\"margin-top:15px;color:#004080;width:100%;border:1px dashed #CCCCCC\">");
		content.append("<legend style=\"color:#004080;\" algin=\"right\">项目基本情况</legend>");
		content.append("<table width=\"100%\">");
		content.append("<tbody><tr id=\"itemTr_0\">");
		content.append("<td width=\"50%\">");
		content.append("客户名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("CUSTOMERNAME")==null?"":data.get("CUSTOMERNAME"))+"\" style=\"width:220px;\" readonly=\"readonly\" class=\"{maxlength:256,string:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_1\">");
		content.append("<td width=\"50%\">");
		content.append("项目名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("PROJECTNAME")==null?"":data.get("PROJECTNAME"))+"\" style=\"width:220px;\" readonly=\"readonly\" class=\"{maxlength:256,string:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_1\">");
		content.append("<td width=\"50%\">");
		content.append("大区负责人&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" value=\""+(data.get("OWNER")==null?"":data.get("OWNER"))+"\" style=\"background-color:#efefef;width:220px\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\"{maxlength:130,required:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_1\">");
		content.append("<td width=\"50%\">");
		content.append("项目负责人&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" value=\""+(data.get("MANAGER")==null?"":data.get("MANAGER"))+"\" style=\"background-color:#efefef;width:220px\" \"=\"\" =\"readonly=\"\" onfocus=\"this.blur()\" readonly=\"readonly\" class=\"{maxlength:130,required:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_2\">");
		content.append("<td width=\"50%\">");
		content.append("客户联系人&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("KHLXR")==null?"":data.get("KHLXR"))+"\" style=\"width:220px;\" readonly=\"readonly\" class=\"{maxlength:32,required:false,string:true}\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_1\">");
		content.append("<td width=\"50%\" id=\"data_CLBM\">");
		content.append("承揽部门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("CLBM")==null?"":data.get("CLBM"))+"\" style=\"width:220px;\" readonly=\"readonly\" class=\"{maxlength:256,required:true,string:true}\"\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_3\">");
		content.append("<td width=\"50%\">");
		content.append("客户联系电话&nbsp;<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("KHLXDH")==null?"":data.get("KHLXDH"))+"\" style=\"width:220px;\" readonly=\"readonly\" class=\"{maxlength:32,required:false,string:true}\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_1\">");
		content.append("<td width=\"50%\" id=\"data_CZBM\">");
		content.append("承做部门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"text\" form-type=\"al_textbox\" value=\""+(data.get("CZBM")==null?"":data.get("CZBM"))+"\" style=\"width:220px;\" readonly=\"readonly\" class=\"{maxlength:256,required:true,string:true}\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_4\">");
		content.append("<td width=\"50%\">");
		content.append("股票发行进展&nbsp;<select disabled=\"disabled\" style=\"width:231px;\">");
		content.append("<option value=\""+(data.get("GPFXJZ")==null?"":data.get("GPFXJZ"))+"\">"+(data.get("GPFXJZ")==null?"":data.get("GPFXJZ"))+"</option>");
		content.append("</select>");
		content.append("</td>");
		content.append("</tr>");
		content.append("<tr id=\"itemTr_1\">");
		content.append("<td width=\"50%\">");
		content.append("项目发起日期&nbsp;<input type=\"text\" value=\""+(data.get("STARTDATE")==null?"":data.get("STARTDATE"))+"\" style=\"width:220px\" readonly=\"readonly\" class=\"{required:true}\">");
		content.append("</td>");
		content.append("</tr>");
		content.append("</tbody></table>");
		content.append("</fieldset>");
		
		return content.toString();
	}
	public boolean projectClose(String instanceids,String jsyy,String memo) {
		String[] insarr = instanceids.split(",");
		Boolean[] updateresult = new Boolean[insarr.length];
		int succesnum=0;
		for (int i = 0; i < insarr.length; i++) {
			long instanceId = Long.parseLong(insarr[i]);
			HashMap hash = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
			String projectname = hash.get("PROJECTNAME").toString();
			Long dataid = (Long) hash.get("ID");
			hash.put("STATUS","已完成");
			if(hash.get("GPFXJZ")!=null&&hash.get("GPFXJZ").equals("股转系统报批")&&!(hash.get("JSYY")!=null&&!hash.get("JSYY").equals(""))){
				hash.put("JSYY","已向股转系统递交定增备案申请材料");
			}else{
				hash.put("JSYY",jsyy);
			}
			hash.put("MEMO",memo);
			// ============================消息提醒===============================================
			/*String smsContent = "";
			String sysMsgContent = "";
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.PROJECT_BASE_CLOSE_KEY, hash);
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.PROJECT_BASE_CLOSE_KEY, hash);
			String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			if (target != null) {
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				if (!sysMsgContent.equals("")) {
					MessageAPI.getInstance().sendSysMsg(userid, "项目任务时间变更",sysMsgContent);
				}
			}*/
			String ProjectUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目信息'", "UUID");
			boolean updateFormData = DemAPI.getInstance().updateFormData(ProjectUUID, instanceId,hash, dataid, false);
			updateresult[i]=updateFormData;
			if(updateFormData){
				succesnum++;
				LogUtil.getInstance().addLog(dataid, "股票发行项目信息管理维护", "关闭项目："+projectname);
			}
		}
		if(succesnum == updateresult.length){
			return true;
		}else{
			return false;
		}
	}
	
	public String getFileListHtml(List<HashMap> closelist) {
		String filelist = closelist.get(0).get("ATTACH")==null?"":closelist.get(0).get("ATTACH").toString();
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(filelist);
		String commonstr = "";
		for (FileUpload fileUpload : sublist) {
			commonstr+="<div  id=\"" + fileUpload.getFileId() + "\"" + ">";
			commonstr+="<span><a href=\"uploadifyDownload.action?fileUUID=" + fileUpload.getFileId() + "\" target=\"_blank\">" + fileUpload.getFileSrcName() + "</a></span>";
			commonstr+="</div>";
		}
		return commonstr;
	}
	
	public void expFixedReserve(HttpServletResponse response,String month) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("企业定增储备项目");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		// 第四步，创建单元格，并设置值表头 设置表头居中
		
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor((short) 1);
		
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setFillForegroundColor((short) 1);
		style5.setFillPattern((short) 1);
		style5.setFont(font);
		
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap<Integer, Integer> length = new HashMap<Integer, Integer>();
		List<Integer> rows = new ArrayList<Integer>();
		Integer count = 1;
		int m=0;
		int z=0;
		rows.add(m);
		
		/*上月末企业定增储备项目*/
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("序号");cell1.setCellStyle(style5);length.put(0, "序号".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("主办券商");cell1.setCellStyle(style5);length.put(1, "主办券商".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("上月末企业定增储备项目");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		row1 = sheet.createRow((int) m++);
		z=0;
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("企业全称");cell1.setCellStyle(style5);length.put(2, "企业全称".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("企业类型");cell1.setCellStyle(style5);length.put(3, "企业类型".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券代码");cell1.setCellStyle(style5);length.put(4, "证券代码".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券简称");cell1.setCellStyle(style5);length.put(5, "证券简称".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("所属行业");cell1.setCellStyle(style5);length.put(6, "所属行业".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("拟发行股数（万元）");cell1.setCellStyle(style5);length.put(7, "拟发行股数（万元）".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("拟募集资金金额（万元）");cell1.setCellStyle(style5);length.put(8, "拟募集资金金额（万元）".length());
		list=zqbGpfxProjectSxDAO.fixedReserveForExp(month,"before","run");
		for (HashMap map : list) {
			z=0;
			HSSFCell cell2;
			row1 = sheet.createRow((int) m++);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(count++);cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("COMPANYNAME") == null ? "" : map.get("COMPANYNAME").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CUSTOMERNAME") == null ? "" : map.get("CUSTOMERNAME").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZRFS") == null ? "" : map.get("ZRFS").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQDM") == null ? "" : map.get("ZQDM").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQJC") == null ? "" : map.get("ZQJC").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("SSHY") == null ? "" : map.get("SSHY").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("GPFXSL") == null ? "" : map.get("GPFXSL").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("MJZJZE") == null ? "" : map.get("MJZJZE").toString());cell2.setCellStyle(style6);
			
			length.put(1, map.get("COMPANYNAME") == null ? length.get(1) : map.get("COMPANYNAME").toString().length()>length.get(1)?map.get("COMPANYNAME").toString().length():length.get(1));
			length.put(2, map.get("CUSTOMERNAME") == null ? length.get(2) : map.get("CUSTOMERNAME").toString().length()>length.get(2)?map.get("CUSTOMERNAME").toString().length():length.get(2));
			length.put(3, map.get("ZRFS") == null ? length.get(3) : map.get("ZRFS").toString().length()>length.get(3)?map.get("ZRFS").toString().length():length.get(3));
			length.put(4, map.get("ZQDM") == null ? length.get(4) : map.get("ZQDM").toString().length()>length.get(4)?map.get("ZQDM").toString().length():length.get(4));
			length.put(5, map.get("ZQJC") == null ? length.get(5) : map.get("ZQJC").toString().length()>length.get(5)?map.get("ZQJC").toString().length():length.get(5));
			length.put(6, map.get("SSHY") == null ? length.get(6) : map.get("SSHY").toString().length()>length.get(6)?map.get("SSHY").toString().length():length.get(6));
			length.put(7, map.get("GPFXSL") == null ? length.get(7) : map.get("GPFXSL").toString().length()>length.get(7)?map.get("GPFXSL").toString().length():length.get(7));
			length.put(8, map.get("MJZJZE") == null ? length.get(8) : map.get("MJZJZE").toString().length()>length.get(8)?map.get("MJZJZE").toString().length():length.get(8));
		}
		
		/*本月末企业定增储备项目*/
		count=1;
		m++;
		rows.add(m);
		row1 = sheet.createRow((int) m++);
		z=0;
		cell1 = row1.createCell((short) z++);cell1.setCellValue("序号");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("主办券商");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("本月末企业定增储备项目");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		row1 = sheet.createRow((int) m++);
		z=0;
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("企业全称");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("企业类型");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券代码");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券简称");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("所属行业");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("拟发行股数（万元）");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("拟募集资金金额（万元）");cell1.setCellStyle(style5);
		list=zqbGpfxProjectSxDAO.fixedReserveForExp(month,"thismonth","run");
		for (HashMap map : list) {
			z=0;
			HSSFCell cell2;
			row1 = sheet.createRow((int) m++);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(count++);cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("COMPANYNAME") == null ? "" : map.get("COMPANYNAME").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CUSTOMERNAME") == null ? "" : map.get("CUSTOMERNAME").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZRFS") == null ? "" : map.get("ZRFS").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQDM") == null ? "" : map.get("ZQDM").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQJC") == null ? "" : map.get("ZQJC").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("SSHY") == null ? "" : map.get("SSHY").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("GPFXSL") == null ? "" : map.get("GPFXSL").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("MJZJZE") == null ? "" : map.get("MJZJZE").toString());cell2.setCellStyle(style6);
			
			length.put(1, map.get("COMPANYNAME") == null ? length.get(1) : map.get("COMPANYNAME").toString().length()>length.get(1)?map.get("COMPANYNAME").toString().length():length.get(1));
			length.put(2, map.get("CUSTOMERNAME") == null ? length.get(2) : map.get("CUSTOMERNAME").toString().length()>length.get(2)?map.get("CUSTOMERNAME").toString().length():length.get(2));
			length.put(3, map.get("ZRFS") == null ? length.get(3) : map.get("ZRFS").toString().length()>length.get(3)?map.get("ZRFS").toString().length():length.get(3));
			length.put(4, map.get("ZQDM") == null ? length.get(4) : map.get("ZQDM").toString().length()>length.get(4)?map.get("ZQDM").toString().length():length.get(4));
			length.put(5, map.get("ZQJC") == null ? length.get(5) : map.get("ZQJC").toString().length()>length.get(5)?map.get("ZQJC").toString().length():length.get(5));
			length.put(6, map.get("SSHY") == null ? length.get(6) : map.get("SSHY").toString().length()>length.get(6)?map.get("SSHY").toString().length():length.get(6));
			length.put(7, map.get("GPFXSL") == null ? length.get(7) : map.get("GPFXSL").toString().length()>length.get(7)?map.get("GPFXSL").toString().length():length.get(7));
			length.put(8, map.get("MJZJZE") == null ? length.get(8) : map.get("MJZJZE").toString().length()>length.get(8)?map.get("MJZJZE").toString().length():length.get(8));
		}
		
		/*本月减少企业定增储备项目*/
		count=1;
		m++;
		rows.add(m);
		row1 = sheet.createRow((int) m++);
		z=0;
		cell1 = row1.createCell((short) z++);cell1.setCellValue("序号");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("主办券商");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("本月减少企业定增储备项目");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		row1 = sheet.createRow((int) m++);
		z=0;
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("企业全称");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("企业类型");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券代码");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券简称");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("所属行业");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("项目减少原因");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("备注");cell1.setCellStyle(style5);
		list=zqbGpfxProjectSxDAO.fixedReserveForExp(month,"thismonth","close");
		for (HashMap map : list) {
			z=0;
			HSSFCell cell2;
			row1 = sheet.createRow((int) m++);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(count++);cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("COMPANYNAME") == null ? "" : map.get("COMPANYNAME").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CUSTOMERNAME") == null ? "" : map.get("CUSTOMERNAME").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZRFS") == null ? "" : map.get("ZRFS").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQDM") == null ? "" : map.get("ZQDM").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQJC") == null ? "" : map.get("ZQJC").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("SSHY") == null ? "" : map.get("SSHY").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("JSYY") == null ? "" : map.get("JSYY").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("MEMO") == null ? "" : map.get("MEMO").toString());cell2.setCellStyle(style6);
			
			length.put(1, map.get("COMPANYNAME") == null ? length.get(1) : map.get("COMPANYNAME").toString().length()>length.get(1)?map.get("COMPANYNAME").toString().length():length.get(1));
			length.put(2, map.get("CUSTOMERNAME") == null ? length.get(2) : map.get("CUSTOMERNAME").toString().length()>length.get(2)?map.get("CUSTOMERNAME").toString().length():length.get(2));
			length.put(3, map.get("ZRFS") == null ? length.get(3) : map.get("ZRFS").toString().length()>length.get(3)?map.get("ZRFS").toString().length():length.get(3));
			length.put(4, map.get("ZQDM") == null ? length.get(4) : map.get("ZQDM").toString().length()>length.get(4)?map.get("ZQDM").toString().length():length.get(4));
			length.put(5, map.get("ZQJC") == null ? length.get(5) : map.get("ZQJC").toString().length()>length.get(5)?map.get("ZQJC").toString().length():length.get(5));
			length.put(6, map.get("SSHY") == null ? length.get(6) : map.get("SSHY").toString().length()>length.get(6)?map.get("SSHY").toString().length():length.get(6));
			length.put(7, map.get("JSYY") == null ? length.get(7) : map.get("JSYY").toString().length()>length.get(7)?map.get("JSYY").toString().length():length.get(7));
			length.put(8, map.get("MEMO") == null ? length.get(8) : map.get("MEMO").toString().length()>length.get(8)?map.get("MEMO").toString().length():length.get(8));
		}
		
		for (int i = 0; i <= 8; i++) {
			sheet.setColumnWidth(i,length.get(i)*600);
		}
		for (Integer o:rows) {
			sheet.addMergedRegion(new Region(o,(short)0, o+1,(short)0));
			sheet.addMergedRegion(new Region(o,(short)1, o+1,(short)1));
			sheet.addMergedRegion(new Region(o,(short)2, o,(short)8));
		}
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("企业定增储备项目.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			try {wb.write(out1);} catch (Exception e) {}
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
	
	public void expIssueObject(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("定增项目的发行对象");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		// 第四步，创建单元格，并设置值表头 设置表头居中
		
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor((short) 1);
		
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setFillForegroundColor((short) 1);
		style5.setFillPattern((short) 1);
		style5.setFont(font);
		
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap<Integer, Integer> length = new HashMap<Integer, Integer>();
		List<Integer> rows = new ArrayList<Integer>();
		Integer count = 1;
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("序号");cell1.setCellStyle(style5);length.put(0, "序号".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("主办券商");cell1.setCellStyle(style5);length.put(1, "主办券商".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("挂牌企业");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("定增对象(人,家)");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(5, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(6, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(7, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(8, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(9, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(10, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(11, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(12, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("定增对象获取渠道(人,家)");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(14, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(15, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(16, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(17, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(18, "".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);length.put(19, "".length());
		
		row1 = sheet.createRow((int) m++);
		z=0;
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("");cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券代码");cell1.setCellStyle(style5);length.put(2, "证券代码".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券简称");cell1.setCellStyle(style5);length.put(3, "证券简称".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公司股东(自然人)");cell1.setCellStyle(style5);length.put(4, "公司股东(自然人)".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("董监高");cell1.setCellStyle(style5);length.put(5, "董监高".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("核心员工");cell1.setCellStyle(style5);length.put(6, "核心员工".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("其他自然人投资者");cell1.setCellStyle(style5);length.put(7, "其他自然人投资者".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("自然人投资者小计");cell1.setCellStyle(style5);length.put(8, "自然人投资者小计".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公司股东(机构)");cell1.setCellStyle(style5);length.put(9, "公司股东(机构)".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("一般法人机构");cell1.setCellStyle(style5);length.put(10, "一般法人机构".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("专业机构");cell1.setCellStyle(style5);length.put(11, "专业机构".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("机构投资者小计");cell1.setCellStyle(style5);length.put(12, "机构投资者小计".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("营业部客户");cell1.setCellStyle(style5);length.put(13, "营业部客户".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("券商资管(非营业部客户)");cell1.setCellStyle(style5);length.put(14, "券商资管(非营业部客户)".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("券商直投(非营业部客户)");cell1.setCellStyle(style5);length.put(15, "券商直投(非营业部客户)".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("基金公司(非营业部客户)");cell1.setCellStyle(style5);length.put(16, "基金公司(非营业部客户)".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("大股东关联方");cell1.setCellStyle(style5);length.put(17, "大股东关联方".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("其他客户群体");cell1.setCellStyle(style5);length.put(18, "其他客户群体".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("其他客户群体获取渠道");cell1.setCellStyle(style5);length.put(19, "其他客户群体获取渠道".length());
		
//		String clos = "'公司股东（自然人）' A,'董监高' B,'核心员工' C,'其他自然人投资者' D,'自然人投资者小计' E,'公司股东（机构）' F,'一般法人机构' G,"
//				+ "'专业机构' H,'机构投资者小计' I,'营业部客户' J,'券商资管（非营业部客户）' K,'券商直投（非营业部客户）' L,'基金公司（非营业部客户）' M,"
//				+ "'大股东关联方' N,'其他客户群体' O,'其他客户群体获取渠道' P";
		list=zqbGpfxProjectSxDAO.issueObjectForExp();
		for (HashMap map : list) {
			z=0;
			HSSFCell cell2;
			row1 = sheet.createRow((int) m++);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(count++);cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("COMPANYNAME") == null ? "" : map.get("COMPANYNAME").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQDM") == null ? "" : map.get("ZQDM").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQJC") == null ? "" : map.get("ZQJC").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("A") == null ? "" : map.get("A").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("B") == null ? "" : map.get("B").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("C") == null ? "" : map.get("C").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("D") == null ? "" : map.get("D").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("E") == null ? "" : map.get("E").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("F") == null ? "" : map.get("F").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("G") == null ? "" : map.get("G").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("H") == null ? "" : map.get("H").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("I") == null ? "" : map.get("I").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("J") == null ? "" : map.get("J").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("K") == null ? "" : map.get("K").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("L") == null ? "" : map.get("L").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("M") == null ? "" : map.get("M").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("N") == null ? "" : map.get("N").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("O") == null ? "" : map.get("O").toString());cell2.setCellStyle(style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("P") == null ? "" : map.get("P").toString());cell2.setCellStyle(style6);
		}
		for (int i = 0; i <= 19; i++) {
			sheet.setColumnWidth(i,length.get(i)*600);
		}
		sheet.addMergedRegion(new Region(0,(short)0, 1,(short)0));
		sheet.addMergedRegion(new Region(0,(short)1, 1,(short)1));
		sheet.addMergedRegion(new Region(0,(short)2, 0,(short)3));
		sheet.addMergedRegion(new Region(0,(short)4, 0,(short)12));
		sheet.addMergedRegion(new Region(0,(short)13, 0,(short)19));
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("定增项目的发行对象.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			try {wb.write(out1);} catch (Exception e) {}
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

	public List<HashMap> accountedForList(int pageSize,int pageNumber,String customername,String departmentname,String dzrqbegin,String dzrqend,String xmlx,String xylx) {
		return zqbGpfxProjectSxDAO.accountedForList(pageSize,pageNumber,customername,departmentname,dzrqbegin,dzrqend,xmlx,xylx);
	}

	public List<HashMap> accountedForListSize(String customername,String departmentname,String dzrqbegin,String dzrqend,String xmlx,String xylx) {
		return zqbGpfxProjectSxDAO.accountedForList(customername,departmentname,dzrqbegin,dzrqend,xmlx,xylx);
	}
	public void accountedForExp(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("财务入账");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		// 第四步，创建单元格，并设置值表头 设置表头居中
		
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor((short) 1);
		
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style5.setFillForegroundColor((short) 1);
		style5.setFillPattern((short) 1);
		style5.setFont(font);
		
		HSSFCellStyle style8 = wb.createCellStyle();
		style8.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style8.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style8.setFillPattern((short) 1);
		style8.setBorderBottom((short) 1);
		style8.setBorderLeft((short) 2);
		style8.setBorderRight((short) 1);
		style8.setBorderTop((short) 1);
		style8.setFillForegroundColor((short) 1);
		
		HSSFCellStyle style7 = wb.createCellStyle();
		style7.setFillPattern((short) 1);
		style7.setBorderBottom((short) 1);
		style7.setBorderLeft((short) 2);
		style7.setBorderRight((short) 1);
		style7.setBorderTop((short) 1);
		style7.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style7.setFillForegroundColor((short) 1);
		style7.setFillPattern((short) 1);
		style7.setFont(font);
		
		HSSFCellStyle style10 = wb.createCellStyle();
		style10.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style10.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style10.setFillPattern((short) 1);
		style10.setBorderBottom((short) 1);
		style10.setBorderLeft((short) 1);
		style10.setBorderRight((short) 2);
		style10.setBorderTop((short) 1);
		style10.setFillForegroundColor((short) 1);
		
		HSSFCellStyle style9 = wb.createCellStyle();
		style9.setFillPattern((short) 1);
		style9.setBorderBottom((short) 1);
		style9.setBorderLeft((short) 1);
		style9.setBorderRight((short) 2);
		style9.setBorderTop((short) 1);
		style9.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style9.setFillForegroundColor((short) 1);
		style9.setFillPattern((short) 1);
		style9.setFont(font);
		
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 2);
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setFillForegroundColor((short) 1);
		style4.setFillPattern((short) 1);
		style4.setFont(font);
		
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setFillPattern((short) 1);
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 2);
		style3.setBorderTop((short) 2);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style3.setFillForegroundColor((short) 1);
		style3.setFillPattern((short) 1);
		style3.setFont(font);
		
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setFillPattern((short) 1);
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 2);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 2);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setFillForegroundColor((short) 1);
		style2.setFillPattern((short) 1);
		style2.setFont(font);
		
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setFillPattern((short) 1);
		style1.setBorderBottom((short) 2);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setFillForegroundColor((short) 1);
		style1.setFillPattern((short) 1);
		style1.setFont(font);
		
		HSSFCellStyle style11 = wb.createCellStyle();
		style11.setFillPattern((short) 1);
		style11.setBorderBottom((short) 2);
		style1.setBorderLeft((short) 1);
		style11.setBorderRight((short) 2);
		style11.setBorderTop((short) 1);
		style11.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style11.setFillForegroundColor((short) 1);
		style11.setFillPattern((short) 1);
		style11.setFont(font);
		
		HSSFCellStyle style12 = wb.createCellStyle();
		style12.setFillPattern((short) 1);
		style12.setBorderBottom((short) 2);
		style12.setBorderLeft((short) 2);
		style12.setBorderRight((short) 1);
		style12.setBorderTop((short) 1);
		style12.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style12.setFillForegroundColor((short) 1);
		style12.setFillPattern((short) 1);
		style12.setFont(font);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap<Integer, Integer> length = new HashMap<Integer, Integer>();
		List<Integer> rows = new ArrayList<Integer>();
		Integer count = 0;
		int m=1;
		int z=1;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("序号");cell1.setCellStyle(style2);length.put(1, "序号".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("月份");cell1.setCellStyle(style2);length.put(2, "月份".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("公司名称");cell1.setCellStyle(style4);length.put(3, "公司名称".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("协议类型");cell1.setCellStyle(style4);length.put(4, "协议类型".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("项目类型");cell1.setCellStyle(style4);length.put(5, "项目类型".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("到账金额(万元)");cell1.setCellStyle(style4);length.put(6, "到账金额(万元)".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("到账日期");cell1.setCellStyle(style4);length.put(7, "到账日期".length());
		cell1 = row1.createCell((short) z++);cell1.setCellValue("部门");cell1.setCellStyle(style3);length.put(8, "部门".length());
		//cell1 = row1.createCell((short) z++);cell1.setCellValue("入账人");cell1.setCellStyle(style5);length.put(9, "入账人".length());
		//cell1 = row1.createCell((short) z++);cell1.setCellValue("入账日期");cell1.setCellStyle(style5);length.put(10, "入账日期".length());
		list=zqbGpfxProjectSxDAO.accountedForExp();
		for (HashMap map : list) {
			z=1;
			HSSFCell cell2;if(map.get("CUSTOMERNAME") == null){rows.add(m);}else{count++;}
			row1 = sheet.createRow((int) m++);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CUSTOMERNAME") == null?(map.get("MONTH") == null ? "" : map.get("MONTH").toString())+(map.get("CUSTOMERNAME") == null&&map.get("MONTH").toString().length()==7?"月合计":map.get("CUSTOMERNAME") == null&&map.get("MONTH").toString().length()==4?"年合计":""):count.toString());cell2.setCellStyle((m-2)==list.size()?style12:map.get("CUSTOMERNAME") == null?style7:style8);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("MONTH") == null ? "" : map.get("MONTH").toString());cell2.setCellStyle((m-2)==list.size()?style1:map.get("CUSTOMERNAME") == null?style5:style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CUSTOMERNAME") == null ? "" : map.get("CUSTOMERNAME").toString());cell2.setCellStyle((m-2)==list.size()?style1:map.get("CUSTOMERNAME") == null?style5:style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("XYLX") == null ? "" : map.get("XYLX").toString());cell2.setCellStyle((m-2)==list.size()?style1:map.get("CUSTOMERNAME") == null?style5:style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("XMLX") == null ? "" : map.get("XMLX").toString());cell2.setCellStyle((m-2)==list.size()?style1:map.get("CUSTOMERNAME") == null?style5:style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("DZJE") == null ? "" : map.get("DZJE").toString());cell2.setCellStyle((m-2)==list.size()?style1:map.get("CUSTOMERNAME") == null?style5:style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("DZRQ") == null ? "" : map.get("DZRQ").toString());cell2.setCellStyle((m-2)==list.size()?style1:map.get("CUSTOMERNAME") == null?style5:style6);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("DEPARTMENTNAME") == null ? "" : map.get("DEPARTMENTNAME").toString());cell2.setCellStyle((m-2)==list.size()?style11:map.get("CUSTOMERNAME") == null?style9:style10);
			//cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("LRR") == null ? "" : map.get("LRR").toString());cell2.setCellStyle(map.get("CUSTOMERNAME") == null?style5:style6);
			//cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CJSJ") == null ? "" : map.get("CJSJ").toString());cell2.setCellStyle(map.get("CUSTOMERNAME") == null?style5:style6);
			
			length.put(2, map.get("MONTH") == null ? length.get(2) : map.get("MONTH").toString().length()>length.get(2)?map.get("MONTH").toString().length():length.get(2));
			length.put(3, map.get("CUSTOMERNAME") == null ? length.get(3) : map.get("CUSTOMERNAME").toString().length()>length.get(3)?map.get("CUSTOMERNAME").toString().length():length.get(3));
			length.put(4, map.get("XYLX") == null ? length.get(4) : map.get("XYLX").toString().length()>length.get(4)?map.get("XYLX").toString().length():length.get(4));
			length.put(5, map.get("XMLX") == null ? length.get(5) : map.get("XMLX").toString().length()>length.get(5)?map.get("XMLX").toString().length():length.get(5));
			length.put(6, map.get("DZJE") == null ? length.get(6) : map.get("DZJE").toString().length()>length.get(6)?map.get("DZJE").toString().length():length.get(6));
			length.put(7, map.get("DZRQ") == null ? length.get(7) : map.get("DZRQ").toString().length()>length.get(7)?map.get("DZRQ").toString().length():length.get(7));
			length.put(8, map.get("DEPARTMENTNAME") == null ? length.get(8) : map.get("DEPARTMENTNAME").toString().length()>length.get(8)?map.get("DEPARTMENTNAME").toString().length():length.get(8));
			//length.put(9, map.get("LRR") == null ? length.get(9) : map.get("LRR").toString().length()>length.get(9)?map.get("LRR").toString().length():length.get(9));
			//length.put(10, map.get("CJSJ") == null ? length.get(10) : map.get("CJSJ").toString().length()>length.get(10)?map.get("CJSJ").toString().length():length.get(10));
		}
		for (int i = 1; i <= 8; i++) {
			sheet.setColumnWidth(i,length.get(i)*600);
		}
		for (Integer o:rows) {
			sheet.addMergedRegion(new Region(o,(short)1, o,(short)5));
			sheet.addMergedRegion(new Region(o,(short)7, o,(short)8));
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("财务入账信息.xls");
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
	
	public void expCwrzList(HttpServletResponse response, String dzrq) {
		HashMap<String, Object> dataMap = zqbGpfxProjectSxDAO.getDataMap(dzrq);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("财务顾问业务监管报表");
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		
		HSSFFont titlefont = wb.createFont();
		titlefont.setFontName("宋体");
		titlefont.setFontHeightInPoints((short) 18);// 字体大小
		titlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 10);// 字体大小
		
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setFillForegroundColor((short) 22);
		style1.setFillPattern((short) 1);
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setFillForegroundColor(HSSFColor.WHITE.index);
		style1.setFont(headfont);
		style1.setWrapText(true);
		
		HSSFCellStyle style2 = wb.createCellStyle();
		//style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style2.setFillForegroundColor((short) 22);
		style2.setFillPattern((short) 1);
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFont(headfont);
		style2.setWrapText(true);
		
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setFillForegroundColor((short) 22);
		style3.setFillPattern((short) 1);
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 1);
		style3.setBorderTop((short) 1);
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setFont(contentfont);
		style3.setWrapText(true);
		
		HSSFCellStyle style4 = wb.createCellStyle();
		//style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setFont(contentfont);
		style4.setWrapText(true);
		
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setBottomBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style5.setBorderBottom((short) 1);
		style5.setFont(titlefont);
		style5.setWrapText(true);
		
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居左格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		style6.setTopBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setLeftBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setRightBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setBottomBorderColor(HSSFColor.GREY_25_PERCENT.index);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFont(contentfont);
		style6.setWrapText(true);
		
		HSSFCellStyle style7 = wb.createCellStyle();
		style7.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居左格式
		style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style7.setFillForegroundColor((short) 22);
		style7.setFillPattern((short) 1);
		style7.setBorderBottom((short) 1);
		style7.setBorderLeft((short) 1);
		style7.setBorderRight((short) 1);
		style7.setBorderTop((short) 1);
		style7.setFillForegroundColor(HSSFColor.WHITE.index);
		style7.setFont(contentfont);
		style7.setWrapText(true);
		int z = 1;
		int m = 0;
		HSSFRow row = sheet.createRow((int) m);
		HSSFCell cell = row.createCell((short) z++);
		row.setHeight((short) (50*20));
		cell.setCellValue("财务顾问业务监管报表");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style5);
		sheet.addMergedRegion(new Region(m, (short) 1, m, (short) 10));
		m++;
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("填报单位：");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		sheet.addMergedRegion(new Region(m, (short) 1, m, (short) 4));
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.isEmpty()?"":dataMap.get("QSXYRQ").toString()+"    日");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		sheet.addMergedRegion(new Region(m, (short) 6, m, (short) 7));
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("单位：元");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		sheet.addMergedRegion(new Region(m, (short) 9, m, (short) 10));
		m++;
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("运作管理部门名称：");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		sheet.addMergedRegion(new Region(m, (short) 1, m, (short) 4));
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("部门负责人：");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		sheet.addMergedRegion(new Region(m, (short) 6, m, (short) 7));
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style6);
		sheet.addMergedRegion(new Region(m, (short) 9, m, (short) 10));
		m++;
		z=1;
		row = sheet.createRow((int) m++);
		cell = row.createCell((short) z++);
		cell.setCellValue("序号");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("服务对象");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目类型");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("本期发生");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("本年累计");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("备注");
		cell.setCellStyle(style1);
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("签约客户数量（个）");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("签约合同总金额");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("已实现财务顾问收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("签约客户数量（个）");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("签约合同总金额");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("已实现财务顾问收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style1);
		sheet.addMergedRegion(new Region(m-1, (short) 1, m, (short) 1));
		sheet.addMergedRegion(new Region(m-1, (short) 2, m, (short) 2));
		sheet.addMergedRegion(new Region(m-1, (short) 3, m, (short) 3));
		sheet.addMergedRegion(new Region(m-1, (short) 4, m-1, (short) 6));
		sheet.addMergedRegion(new Region(m-1, (short) 7, m-1, (short) 9));
		sheet.addMergedRegion(new Region(m-1, (short) 10, m, (short) 10));
		m++;
		z=1;
		row = sheet.createRow((int) m++);
		cell = row.createCell((short) z++);
		cell.setCellValue("1");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("上市公司");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("收购兼并");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		z=1;
		row = sheet.createRow((int) m++);
		cell = row.createCell((short) z++);
		cell.setCellValue("2");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("资产重组");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		z=1;
		row = sheet.createRow((int) m++);
		cell = row.createCell((short) z++);
		cell.setCellValue("3");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("股份回购");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("4");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("其他");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		sheet.addMergedRegion(new Region(m-3, (short) 2, m, (short) 2));
		m++;
		z=1;
		row = sheet.createRow((int) m++);
		cell = row.createCell((short) z++);
		cell.setCellValue("5");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("非上市公司");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("企业改制");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("6");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("其他");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("MQYCOUNT")==null?"":dataMap.get("MQYCOUNT").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("MXYJE")==null?"":dataMap.get("MXYJE").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("MTOTAL")==null?"":dataMap.get("MTOTAL").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("YQYCOUNT")==null?"":dataMap.get("YQYCOUNT").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("YXYJE")==null?"":dataMap.get("YXYJE").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("YTOTAL")==null?"":dataMap.get("YTOTAL").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		sheet.addMergedRegion(new Region(m-1, (short) 2, m, (short) 2));
		m++;
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("7");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("其他服务对象");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		sheet.addMergedRegion(new Region(m, (short) 2, m, (short) 3));
		m++;
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("合计");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("MQYCOUNT")==null?"":dataMap.get("MQYCOUNT").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("MXYJE")==null?"":dataMap.get("MXYJE").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("MTOTAL")==null?"":dataMap.get("MTOTAL").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("YQYCOUNT")==null?"":dataMap.get("YQYCOUNT").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("YXYJE")==null?"":dataMap.get("YXYJE").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue(dataMap.get("YTOTAL")==null?"":dataMap.get("YTOTAL").toString());
		cell.setCellStyle(style4);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style4);
		sheet.addMergedRegion(new Region(m, (short) 1, m, (short) 3));
		m++;
		z=1;
		row = sheet.createRow((int) m);
		cell = row.createCell((short) z++);
		cell.setCellValue("补充资料");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style3);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style7);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style7);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style7);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style7);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style7);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style7);
		cell = row.createCell((short) z++);
		cell.setCellValue("");
		cell.setCellStyle(style7);
		sheet.addMergedRegion(new Region(m, (short) 1, m, (short) 3));
		sheet.addMergedRegion(new Region(m, (short) 4, m, (short) 10));
		sheet.setColumnWidth(1, 3 * 256 * 2);
		sheet.setColumnWidth(2, 6 * 256 * 2);
		sheet.setColumnWidth(3, 5 * 256 * 2);
		sheet.setColumnWidth(4, 12 * 256 * 2);
		sheet.setColumnWidth(5, 10 * 256 * 2);
		sheet.setColumnWidth(6, 10 * 256 * 2);
		sheet.setColumnWidth(7, 12 * 256 * 2);
		sheet.setColumnWidth(8, 10 * 256 * 2);
		sheet.setColumnWidth(9, 10 * 256 * 2);
		sheet.setColumnWidth(10, 10 * 256 * 2);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目签约汇总表.xls");
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
					
				}
			}

		}
	}

	public void expXmsrhzList(HttpServletResponse response) {
		List<HashMap<String,Object>> dataList=zqbGpfxProjectSxDAO.getXmsrhzList();
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目收入汇总表");
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 10);// 字体大小
		
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setFillForegroundColor((short) 22);
		style1.setFillPattern((short) 1);
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setFillForegroundColor(HSSFColor.WHITE.index);
		style1.setFont(headfont);
		style1.setWrapText(true);
		
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style2.setFillPattern((short) 1);
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFont(contentfont);
		style2.setWrapText(true);
		int index=1;
		int m=0;
		int z=0;
		HSSFRow row = sheet.createRow((int) m++);
		HSSFCell cell = row.createCell((short) z++);
		cell.setCellValue("序号");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("公司简称");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("股票代码");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("挂牌日期");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目进度");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("部门");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("交叉销售部门");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("交叉分配比例");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("承揽人");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目负责人");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("挂牌签约金额");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("督导签约金额");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("挂牌收入差额");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("年收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("挂牌收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("督导收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("定增收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("并购收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("财务收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("交叉分配金额");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("其他收入");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("年净收入");
		cell.setCellStyle(style1);
		for (HashMap<String, Object> hashMap : dataList) {
			z=0;
			row = sheet.createRow((int) m++);
			cell = row.createCell((short) z++);
			cell.setCellValue(index++);
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZQJC")==null?"":hashMap.get("ZQJC").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZQDM")==null?"":hashMap.get("ZQDM").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("GPRQ")==null?"":hashMap.get("GPRQ").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("TYPE")==null?"":hashMap.get("TYPE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("SSBM")==null?"":hashMap.get("SSBM").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("JCXSBM")==null?"":hashMap.get("JCXSBM").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("JCFPBL")==null?"":hashMap.get("JCFPBL").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZCLR")==null?"":hashMap.get("ZCLR").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("MANAGER")==null?"":hashMap.get("MANAGER").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("GPQYJE")==null?"":hashMap.get("GPQYJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("DDQYJE")==null?"":hashMap.get("DDQYJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("GPSRCE")==null?"":hashMap.get("GPSRCE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("NSRJE")==null?"":hashMap.get("NSRJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("GPSRJE")==null?"":hashMap.get("GPSRJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("DDSRJE")==null?"":hashMap.get("DDSRJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("DZSRJE")==null?"":hashMap.get("DZSRJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("BGSRJE")==null?"":hashMap.get("BGSRJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("CWSRJE")==null?"":hashMap.get("CWSRJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("JCFPJE")==null?"":hashMap.get("JCFPJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("QTSRJE")==null?"":hashMap.get("QTSRJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("NJSR")==null?"":hashMap.get("NJSR").toString());
			cell.setCellStyle(style2);
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		sheet.autoSizeColumn(12);
		sheet.autoSizeColumn(13);
		sheet.autoSizeColumn(14);
		sheet.autoSizeColumn(15);
		sheet.autoSizeColumn(16);
		sheet.autoSizeColumn(17);
		sheet.autoSizeColumn(18);
		sheet.autoSizeColumn(19);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目收入汇总表.xls");
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
					
				}
			}

		}
		
	}
}
