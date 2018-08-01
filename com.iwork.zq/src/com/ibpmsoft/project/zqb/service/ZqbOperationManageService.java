package com.ibpmsoft.project.zqb.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.ibpmsoft.project.zqb.common.EventConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.dao.ZqbOperationManageDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.NumberUtils;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;

import org.apache.log4j.Logger;
public class ZqbOperationManageService {
	
	private ZqbOperationManageDAO zqbOperationManageDAO;
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件
	private static final String ZQB_EVEVT_TALK_UUID = "c807510e83a0415cb37810bc2994d71a";
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private static Logger logger = Logger.getLogger(ZqbOperationManageService.class);
	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	public String getChartBarData(){

		List<String> list =  this.getChartBarLabelList();
		
		List<Integer> list1 = new ArrayList(); 
		List<Integer> list2 = new ArrayList(); 
		
		for(String userid :list){
			List<String> companylist = new ArrayList(); 
			//获取持续督导人员共计负责了多少家公司
			StringBuffer sql = new StringBuffer();
			sql.append("select count(1) cnum,khbh from (select distinct khbh from BD_MDM_KHQXGLB where khfzr=?) group by khbh");
			Connection conn=null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				stmt =conn.prepareStatement(sql.toString());
				stmt.setString(1, userid);
				rs = stmt.executeQuery();
				 int count = 0;
				 while(rs.next()){
					 count = rs.getInt("cnum");
					 String khbh=rs.getString("khbh");
					 companylist.add(khbh);
				 }
				 list1.add(count);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}finally{
				if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}
				if(stmt!=null){
					try {
						stmt.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}
				if(conn!=null){
					try {
						conn.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}
			}
			/*
			List parameter=new ArrayList();//存放参数
			StringBuffer sql1 = new StringBuffer();
			sql1.append("SELECT COUNT(*) NUM FROM BD_XP_BASEINFO where 1=1 )");
			if(companylist.size()>0){
				sql1.append(" and (1=2 ");
				for(String no:companylist){
					sql1.append(" or ").append("KHBH=? ");
					parameter.add(no);
				}
				sql1.append(")");
			}
			try {
				stmt =conn.prepareStatement(sql1.toString());
				for (int i = 0; i < parameter.size(); i++) {
					stmt.setString(i+1, parameter.get(i).toString());
				}
				rs = stmt.executeQuery();
				int num = 0;
				while(rs.next()){
					num = rs.getInt("num");
					list2.add(num);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}finally{
				DBUtil.close(conn, stmt, rs);
			}*/
			list2.add(0);
		}
			
		JSONArray json1 = JSONArray.fromObject(list1);
		JSONArray json2 = JSONArray.fromObject(list2);
		String label = "["+json1.toString()+","+json2.toString()+"]";
		return label;
	}
	/**
	 * 获取持续督导人员列表
	 * @return
	 */
	public List<String> getChartBarLabelList(){
		StringBuffer sql = new StringBuffer();
		sql.append("select userid,username from orguser where orgroleid  = 4 order by userid");
		Connection conn =null;
		PreparedStatement stmt = null;
		 ResultSet rs = null;
		 List<String> list = new ArrayList();
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			 while(rs.next()){
				 String userid = rs.getString("userid");
				 String userName = rs.getString("username");
				 list.add(userid+"["+userName+"]");
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rs);
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param sxbh
	 * @param content
	 * @return
	 */
	public boolean saveTalk(String sxbh,String content){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String createUser = UserContextUtil.getInstance().getCurrentUserId();
		Long instanceId = DemAPI.getInstance().newInstance(ZQB_EVEVT_TALK_UUID, createUser);
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		HashMap conditionMap = new HashMap();
		HashMap conditionMap1 = new HashMap(); 
		List<HashMap> list1 = null;
		conditionMap.put("INSTANCEID", sxbh);
		List<HashMap> list = DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"), conditionMap, "ID DESC");
		HashMap hashdata = new HashMap();
		for (HashMap hashMap : list) {
			if(sxbh.equals(hashMap.get("INSTANCEID").toString())){
				hashdata.put("SXBH", sxbh);
				hashdata.put("FSR", uc._userModel.getUsername());
				hashdata.put("FSRZH",createUser);
				hashdata.put("CONTENT", content); 
				hashdata.put("SXMC", hashMap.get("SXMC").toString()); 
				//发送消息	
				String smsContent = "";
				String sysMsgContent = "";
				smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.ENENT_TALK_KEY, hashdata); 
				sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.ENENT_TALK_KEY, hashdata); 
				String userid = null;
				if(roleid.equals("3")){
					conditionMap1.put("KHBH", uc.get_userModel().getExtend1());
					conditionMap1.put("KHMC", uc.get_userModel().getExtend2());
					list1=DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap1, null);
					userid=list1.get(0).get("KHFZR")==null?list1.get(0).get("FHSPR").toString().substring(0, list1.get(0).get("FHSPR").toString().indexOf("[")):list1.get(0).get("KHFZR").toString().substring(0, list1.get(0).get("KHFZR").toString().indexOf("["));
				}else{
					String sql="select * from orguser where extend1='"+hashMap.get("KHBH").toString()+"'";
					userid = DBUtil.getString(sql, "USERID");
				}
				UserContext target = UserContextUtil.getInstance().getUserContext(userid);
				if(target!=null){
					if(!smsContent.equals("")){
						String mobile = target.get_userModel().getMobile();
						if(mobile!=null&&!mobile.equals("")){
							MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
						}
					}
					if(!sysMsgContent.equals("")){
						MessageAPI.getInstance().sendSysMsg(userid, "重大事项留言提醒", sysMsgContent);
					}
				}
			}
		}
		return DemAPI.getInstance().saveFormData(ZQB_EVEVT_TALK_UUID, instanceId, hashdata, false);
	} 
	
	public List<HashMap> getTalkList(Long instanceid){
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("SXBH",instanceid+"");
		List<HashMap> list = DemAPI.getInstance().getList(ZQB_EVEVT_TALK_UUID, conditionMap, "ID");
		return list; 
	}
	
	
	/** 
	 * 获得当前客户列表
	 * @return
	 */
	public List<HashMap> getCurrentCustomerList(){
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
//		
//		if(SecurityUtil.isSuperManager()){
//			conditionMap = null;
//		}else{
//			conditionMap.put("KHFZR", userFullName); 
//		}
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> l = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		for(HashMap map:l){
			if((map.get("KHFZR")!=null&&!map.get("KHFZR").toString().equals("")&&map.get("KHFZR").toString().equals(userFullName))
					||(map.get("ZZCXDD")!=null&&!map.get("ZZCXDD").toString().equals("")&&map.get("ZZCXDD").toString().equals(userFullName))
					||(map.get("FHSPR")!=null&&!map.get("FHSPR").toString().equals("")&&map.get("FHSPR").toString().equals(userFullName))
					||(map.get("ZZSPR")!=null&&!map.get("ZZSPR").toString().equals("")&&map.get("ZZSPR").toString().equals(userFullName))){
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param year
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public String getMeetFilterBar(int year,String meettype,String grouptype,String status){
		StringBuffer filterbar = new StringBuffer();
		filterbar.append("<table width=\"100%\">");
		filterbar.append("<td><select id=\"\" onChange=\"dofilterYear(this.value)\">");
		for(int i=(year-5);i<year+5;i++){
			if(i==year){
				filterbar.append("<option value=\"").append(i).append("\" selected=\"selected\">").append(i).append("年</option>");
			}else{
				filterbar.append("<option value=\"").append(i).append("\" >").append(i).append("年</option>");
			}
		}
		filterbar.append("</select></td>");
		
		filterbar.append("	<td>");
		int num1=0;
		if(meettype==null){meettype="全部";}
		for(String type:EventConstants.MEETING_TYPE){
			num1++;
			if(meettype.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterMeet('").append(type).append("')\" >").append(type).append("</a>");
			}
			
			if(num1<EventConstants.MEETING_TYPE.length){
				filterbar.append("|");
			}
			
		}
		filterbar.append("	</td>");
		filterbar.append("	<td>");
		int num=0;
		if(grouptype==null){grouptype="全部";}
		for(String type:EventConstants.GROUP_TYPE){
			num++;
			if(grouptype.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterGroup('").append(type).append("')\" >").append(type).append("</a>");

			}
			
			if(num<EventConstants.GROUP_TYPE.length){
				filterbar.append("|");
			}
			
		}
		filterbar.append("	</td>");
		
		filterbar.append("	<td>");
		int num2=0;
		if(status==null){status="全部";}
		for(String type:EventConstants.STATUS){
			num2++;
			if(status.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterStatus('").append(type).append("')\" >").append(type).append("</a>");
			}
			 
			if(num2<EventConstants.STATUS.length){
				filterbar.append("|");
			}
		}
		filterbar.append("	</td>");
		filterbar.append("		</tr>");
		filterbar.append("</table>");
		return filterbar.toString();
	}
	
	/**
	 * 
	 * @param year
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public List<HashMap> getMeetRunList(int pageSize,int pageNumber,String userid,String customerno,Long orgRoleId,String zqdm,String zqjc,String startdate,String enddate,String noticename,String spzt){
		List<HashMap> list=zqbOperationManageDAO.getMeetRunList(pageSize,pageNumber,userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);//DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"), conditionMap, "ID desc", pageSize,(pageNumber - 1) * pageSize);
		return list;
	}
	/**
	 * 获取业务呈报明细
	 */
	public static String getYWCBMX(Long instanceId){
		HashMap<String, Object> hashMap = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		// 拼表单对象
		StringBuffer content = new StringBuffer();
		if (hashMap != null && hashMap.size() > 0) {
			content.append("<table class=\"ke-zeroborder\" border=\"0\"  cellspacing=\"0\" cellpadding=\"0\" width=\"98%;\">");
			content.append("<tbody>");
			content.append("<tr id=\"itemTr_1706\">");
			content.append("<td id=\"title_NOTICEDATE\" class=\"td_title\" style=\"width:120px;margin-right:10px;\">");
			content.append("事项名称:");
			content.append("</td>");
			content.append("<td id=\"data_NOTICEDATE\"  class=\"td_data\" style=\"margin-left:10px;\">");
			String sxmc = hashMap.get("SXMC") != null ? hashMap.get("SXMC").toString() : "";
			content.append(sxmc + "&nbsp;");
			content.append("</td><td></td>");
			content.append("</tr>");
			content.append("<tr id=\"itemTr_1726\">");
			content.append("<td id=\"title_NOTICETYPE\" class=\"td_title\" style=\"width:120px;margin-right:10px;\">");
			content.append("事项类型:");
			content.append("</td>");
			content.append("</td>");
			content.append("<td id=\"data_XPSX\" class=\"td_data\"  style=\"margin-left:10px;\">");
			String id = hashMap.get("SXLX")==null?"":hashMap.get("SXLX").toString();
			StringBuffer xpsxname = new StringBuffer();
			if(id!= null&&!id.equals("")){
				PreparedStatement ps = null;
				Connection conn =null;
				ResultSet rs = null;
				int i=1;
				try {
					conn = DBUtil.open();
					ps = conn.prepareStatement("SELECT ID,SXMC FROM BD_XP_XPSXB WHERE ID IN("+id+")");
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
			content.append("</td><td></td><td></td>");
			content.append("</tr>");
			content.append("<tr id=\"itemTr_1709\">");
			content.append("<td id=\"title_NOTICENAME\" class=\"td_title\" style=\"width:120px;margin-right:10px;\">");
			content.append("事项概要:");
			content.append("</td>");
			content.append("<td id=\"data_NOTICENAME\"  class=\"td_data\" style=\"margin-left:10px;\">");
			String sxgy = hashMap.get("SXGY") != null ? hashMap.get("SXGY").toString() : "";
			content.append("<input type=hidden name='NOTICENAME' value=\""+ sxgy + "\">" + sxgy + "&nbsp;");
			content.append("</td></td><td></td>");
			content.append("</tr>");
			content.append("<tr>");
			
			content.append("<tr id=\"itemTr_1712\">");
			String noticefile = hashMap.get("SXFJ") != null ? hashMap.get("SXFJ").toString() : "";
			content.append("<td id=\"data_NOTICEFILE\" class=\"td_data\" colspan=\"4\" style=\"padding:0px;border:0px;\">");
				content.append("<center>");
					content.append("<div>");
						content.append("<div>");
							content.append("<input size=\"100\" id=\"NOTICEFILE\" class=\"{maxlength:2048}\" name=\"NOTICEFILE\" value=\"").append(noticefile).append("\" type=\"hidden\">");
						content.append("</div>");
					content.append("</div>");
					content.append("<table id=\"DIVNOTICEFILE\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" width=\"825px;\" cellspacing=\"0\" cellpadding=\"0\">");
						content.append("<tbody>");
							content.append("<tr align=\"center\">");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"120px;\">类型</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"410px;\">文件名称</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"100px;\">上传人</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height:30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"150px;\">上传时间</td>");
							content.append("</tr>");
							List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(noticefile);
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
									uploader = uploader.substring(uploader.lastIndexOf("[")+1, uploader.lastIndexOf("]"));
									int versionnum = DBUtil.getInt("SELECT COUNT(FILE_ID) NUM FROM IWORK_WEBOFFCIE_VERSION V WHERE V.RECORDID='"+file_id+"'", "NUM");
									if(i==0){
										content.append("<tr>");
										content.append("<td id=\"rowspantd\" rowspan=\"").append(size).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
										content.append("事项附件");
										content.append("</td>");
										content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"410px;\" align=\"left\">");
										content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\">").append(filesrcname.length()>=25?filesrcname.substring(0, 25)+"....":filesrcname).append("</a>&nbsp;");
												
										if(versionnum>0){
											content.append("&nbsp;&nbsp;<a style=\"text-decoration:none;margin-right:20px;\" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','SXFJ');\">【历史版本】</a>");
										}
										content.append("</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"100px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;");
										content.append("</td>");
										content.append("</tr>");
									}else{
										content.append("<tr>");
										content.append("<td title=\"").append(filesrcname).append("\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"410px;\" align=\"left\">");
										content.append("<a href=\"uploadifyDownload.action?fileUUID=").append(file_id).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\">").append(filesrcname.length()>=25?filesrcname.substring(0, 25)+"....":filesrcname).append("</a>&nbsp;");
												if(versionnum>0){
													content.append("&nbsp;&nbsp;<a style=\"text-decoration:none;margin-right:20px; \" href=\"javascript:aloneWebOpenVersion('").append(file_id).append("','','','SXFJ');\">【历史版本】</a>");
												}
										content.append("</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"100px;\" align=\"center\">&nbsp;").append(uploader).append("&nbsp;</td>");
										content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;").append(uploadtime).append("&nbsp;</td>");
										
									
										content.append("</tr>");
									}
								}
							}else{
							content.append("<tr>");
							content.append("<td id=\"rowspantd\" rowspan=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\" align=\"center\">");
							content.append("事项附件");
							content.append("</td>");
							content.append("<td title=\"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"410px;\" align=\"left\">");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"100px;\" align=\"center\">&nbsp;&nbsp;</td>");
							content.append("<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"150px;\" align=\"center\">&nbsp;&nbsp;</td>");
							content.append("</tr>");
							}
						content.append("</tbody>");
					content.append("</table>");
				content.append("</center>");
			content.append("</td>");
			content.append("</tr>");
			
			//content.append("<tr id=\"itemTr_1712\"><td class=\"td_data\"></td></tr>");
			
			String INSTANCEID = hashMap.get("INSTANCEID")==null?"":hashMap.get("INSTANCEID").toString();
			OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			if(orgUser.getOrgroleid()!=3L){
				List lables = new ArrayList();
				lables.add("CONTENT");
				lables.add("CONDATE");
				lables.add("CONUSER");
				String sql = "SELECT CONTENT,CONDATE,CONUSER FROM BD_XP_GGSMJ WHERE GGINS=?";
				Map params = new HashMap();
				params.put(1, INSTANCEID);
				List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
				String contenttext = "";
				String condate = "";
				String conuser = "";
				if(data.size()==1){
					contenttext = data.get(0).get("CONTENT")==null?"":data.get(0).get("CONTENT").toString();
					condate = data.get(0).get("CONDATE")==null?"":data.get(0).get("CONDATE").toString();
					conuser = data.get(0).get("CONUSER")==null?"":data.get(0).get("CONUSER").toString();
					content.append("<tr><td height=20px></td></tr>");
					content.append("<tr id=\"itemTr_1712\"><td colspan=6 class=\"td_data\" style=\"padding-left:80px;border-bottom: 0px dotted #999999;color:red;\">").append("【").append(conuser).append("，&nbsp;&nbsp;").append(condate).append("】").append("</br>").append(contenttext).append("</td></tr>");
				}
			}
			
			content.append("</tbody>");
			content.append("</table>");
		}
		return content.toString();
	}
	/**
	 * 获得会议次数
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public String getMeetingCount(String customerno,int jc,String meettype,String grouptype){
		int count = zqbOperationManageDAO.getMeetingCount(customerno,jc, meettype, grouptype);
		if(count==0){
			count=1;
		}else{
			count++;
		}
		String cnNum = NumberUtils.numberArab2CN(count);
		HashMap hash = new HashMap();
		hash.put("num",count);
		hash.put("cn",cnNum);
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(hash);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public boolean setStatus(String opreatorType,Long instanceid,Date date){
		boolean flag = false;
		HashMap hash = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(opreatorType==null)
			opreatorType = "retime";
		if(opreatorType.equals("finish")){
			hash.put("STATUS", "已召开"); 
			hash.put("PLANTIME", UtilDate.dateFormat(date));
			Long dataid = Long.parseLong(hash.get("ID").toString());
			flag = DemAPI.getInstance().updateFormData(this.getConfigUUID("rcywcbuuid"), instanceid, hash, dataid, true);
		}else{
			if(hash.get("STATUS")!=null&&!hash.get("STATUS").toString().equals("已召开")){
				hash.put("PLANTIME", UtilDate.dateFormat(date));
				Long dataid = Long.parseLong(hash.get("ID").toString());
				flag = DemAPI.getInstance().updateFormData(this.getConfigUUID("rcywcbuuid"), instanceid, hash, dataid, true); 
			}
		}
		return flag;
	}
	/**
	 * 判断持续督导人员列表
	 * @return
	 */
	public boolean getChartBarLabelListFlag(String userid){
		int num=0;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) num from (select orgroleid from orguser where orgroleid  = 4 and userid=? union select  orgroleid from orgusermap where orgroleid  = 4 and userid=? ) ");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			stmt.setString(2, userid);
			rs = stmt.executeQuery();
			 while(rs.next()){
				 num = rs.getInt("num");
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rs);
		}
		
		return num==0;
	}
	public HashMap getMeetingMap(Long instanceid){
		return DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
	}
	
	public boolean removeItem(String projectNo,Long taskid,Long instanceid){
		return  DemAPI.getInstance().removeFormData(instanceid);
	}

	public void setZqbOperationManageDAO(ZqbOperationManageDAO zqbOperationManageDAO) {
		this.zqbOperationManageDAO = zqbOperationManageDAO;
	}
	public String geGslx(String jydxbh,String jydxmc) {
		
		HashMap hash = new HashMap();
			String gslx = zqbOperationManageDAO.getGslx(jydxbh,jydxmc);
			hash.put("GSLX",gslx);
		
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(hash);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public boolean getIsSuperMan() {
		String roleTyle = "";
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId.equals(new Long(5))) {
				flag = true;
			}
		}
		// String currentUserid =
		// UserContextUtil.getInstance().getCurrentUserId();
		// HashMap projectSetMap = DemAPI.getInstance().getFromData(new
		// Long(35722), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		// if(projectSetMap!=null&&projectSetMap.get("SUPERMAN")!=null){
		// String supermans = projectSetMap.get("SUPERMAN").toString();
		// String[] users = supermans.split(",");
		// for(String user:users){
		// if(user.trim().equals(""))continue;
		// String userid = UserContextUtil.getInstance().getUserId(user);
		// if(userid!=null){
		// if(userid.equals(currentUserid)){
		// flag = true;
		// break;
		// }
		// }
		// }
		//
		// }
		return flag;
	}
	/**
	 * 获得会议树json
	 * @return
	 * 
	 */
	public String getEventTreeJson(boolean superman){
		StringBuffer json = new StringBuffer();
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();//角色id
		List<OrgUserMap> usermaplist=UserContextUtil.getInstance().getCurrentUserContext().get_userMapList();
		boolean isjrdd=false;
		if(usermaplist.size()>0){
			for(OrgUserMap oum:usermaplist){
				String rolejrid=oum.getOrgroleid();
				if(rolejrid.equals("4")){
					isjrdd=true;
					break;
				}
			}
		}
		List<HashMap> list = new ArrayList<HashMap>();
		//List<HashMap> l = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		List<HashMap> l = zqbOperationManageDAO.getList();
		//根据用户角色判断自己可以看到的客户
		//场外角色
		  if(superman||Integer.parseInt(roleid)==9){
				for(HashMap map:l){
					map.put("id", map.get("ID")==null?"":map.get("ID").toString());
					String zqdm=map.get("ZQDM")==null?"":map.get("ZQDM").toString();
					String zqjc=map.get("ZQJC")==null?"":map.get("ZQJC").toString();
					map.put("name", zqjc+"("+zqdm+")");
					map.put("khbh",map.get("KHBH"));
					map.put("khmc",map.get("KHMC"));
					map.put("zqjc",map.get("ZQJC"));
					map.put("zqdm",map.get("ZQDM"));
					map.put("open", true);
					map.put("ckecked", true);
					map.put("iconOpen", "iwork_img/package_add.png");
					map.put("iconClose", "iwork_img/package_delete.png");
					map.put("type", "customer");
					map.put("isedit", "true");
					list.add(map);
				}
		  }else if(Integer.parseInt(roleid)==4||isjrdd){//持续督导或者兼任持续督导的角色或质控部负责人
			  for(HashMap map:l){
					//督导角色所查询到的客户
					if((map.get("KHFZR")!=null&&!map.get("KHFZR").toString().equals("")&&map.get("KHFZR").toString().equals(userFullName))
							||(map.get("GGFBR")!=null&&!map.get("GGFBR").toString().equals("")&&map.get("GGFBR").toString().equals(userFullName))
							||(map.get("ZZCXDD")!=null&&!map.get("ZZCXDD").toString().equals("")&&map.get("ZZCXDD").toString().equals(userFullName))
							||(map.get("FHSPR")!=null&&!map.get("FHSPR").toString().equals("")&&map.get("FHSPR").toString().equals(userFullName))
							||(map.get("ZZSPR")!=null&&!map.get("ZZSPR").toString().equals("")&&map.get("ZZSPR").toString().equals(userFullName))
							||(map.get("GGFBR")!=null&&!map.get("GGFBR").toString().equals("")&&map.get("GGFBR").toString().equals(userFullName))
							){
						map.put("id", map.get("ID")==null?"":map.get("ID").toString());
						String zqdm=map.get("ZQDM")==null?"":map.get("ZQDM").toString();
						String zqjc=map.get("ZQJC")==null?"":map.get("ZQJC").toString();
						map.put("name", zqjc+"("+zqdm+")");
						map.put("khbh",map.get("KHBH"));
						map.put("khmc",map.get("KHMC"));
						map.put("zqjc",map.get("ZQJC"));
						map.put("zqdm",map.get("ZQDM"));
						map.put("open", true);
						map.put("ckecked", true);
						map.put("iconOpen", "iwork_img/package_add.png");
						map.put("iconClose", "iwork_img/package_delete.png");
						map.put("type", "customer");
						 map.put("isedit", "true");
						list.add(map);
					}
				}
		  }else if(Integer.parseInt(roleid)==3){//董秘角色
			OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			String	customerno = orgUser.getExtend1();
			String	customername = orgUser.getExtend2();
			if(customerno!=null&&customername!=null&&!customerno.equals("")&&!customername.equals("")){
			HashMap map=new HashMap();
			String zqjc=DBUtil.getString("select ZQJC from bd_zqb_kh_base where customerno = '"+customerno+"'", "ZQJC");
			String zqdm=DBUtil.getString("select ZQDM from bd_zqb_kh_base where customerno = '"+customerno+"'", "ZQDM");
			String jc=zqjc==null?"":zqjc;
			String dm=zqdm==null?"":zqdm;
			map.put("name", jc+"("+dm+")");
			map.put("khbh",customerno);
			map.put("khmc",customername);
			map.put("zqjc",zqjc);
			map.put("zqdm",zqdm);
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
	public int getEventRunListSize(String userid,String customerno,Long orgRoleId,String zqdm,String zqjc,String startdate,String enddate,String noticename,String spzt) {
		List<HashMap> list = zqbOperationManageDAO.getEventRunListSize(userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
		if(list!=null){
		int num=list.size();
		return num;
		}else{
			return 0;
		}
	}
	public String updateClFlag(Long instanceid, Long id) {
		String msg="";
		HashMap hashdata=DemAPI.getInstance().getFormData("3bcf5cecda9942bb8b9cda55afb76d16", id);
		hashdata.put("YCL", '1');//1为已处理
		boolean flag=DemAPI.getInstance().updateFormData("3bcf5cecda9942bb8b9cda55afb76d16", instanceid, hashdata, id, false);
		if(flag){
			msg="更新成功！";
		}else{
			msg="更新失败！";
		}
		return msg;
	}
	public String updateGzsc(String cid, String did) {
		String msg="";
		int num=0;
		Map params=null;
		if(did != null&&!did.equals("")){
			params=new HashMap();
			params.put(1, did);
			num = DBUTilNew.update("UPDATE BD_XP_GZSCSXFKRB SET FKZT='1' WHERE ID= ? ",params);
		}
		if(num==1){
			msg="更新成功！";
		}else{
			msg="更新失败！";
		}
		//判断当前工作审查是否所有人都已反馈,查询未反馈数量,若等于0，则都已反馈
		params=new HashMap();
		params.put(1, cid);
		int allFK = DBUTilNew.getInt( "RNUM","SELECT COUNT(*) RNUM FROM (SELECT INSTANCEID,B.ID,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C INNER JOIN (SELECT A.INSTANCEID,B.ID,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL AND D.FKZT = 0 WHERE C.ID= ? ",params);
		if(allFK==0){
			params=new HashMap();
			params.put(1, cid);
			DBUTilNew.update("UPDATE BD_XP_GZSC SET FKZT='1' WHERE ID= ? ",params);
		}
		return msg;
	}
}













