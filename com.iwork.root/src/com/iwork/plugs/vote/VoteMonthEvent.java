package com.iwork.plugs.vote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.service.ZqbAnnouncementService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import org.apache.log4j.Logger;
public class VoteMonthEvent implements IWorkScheduleInterface {
	private static Logger logger = Logger.getLogger(VoteMonthEvent.class);
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件

	public boolean executeAfter() throws ScheduleException {
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		AddGGToDB();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		return true;
	}

	
	public void AddGGToDB() {
		//-------------发送邮件短信start-----------------
		/**短信邮件是否可发送*/
		String smsstatu = DBUtil.getString("SELECT SMS FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "SMS");
		String mailstatus = DBUtil.getString("SELECT EMAIL FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "EMAIL");
		
		/**短信、邮件文本*/
		String smsContent = DBUtil.getString("SELECT DXTXWB FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "DXTXWB");
		String mailContent = DBUtil.getString("SELECT YJTXWB FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "YJTXWB");
	
		//-----------将公告数据添加至数据库----------
		String path = VoteMonthEvent.class.getResource("").getPath().replace("/", File.separator).substring(1, VoteMonthEvent.class.getResource("").getPath().replace("/", File.separator).indexOf("WEB-INF"));
		String filePath = path+"WEB-INF" +File.separator+"templates"+File.separator+"user_templates"+File.separator+"挂牌公司重大事项温馨提示函.flt";
		File file = new File(filePath);
		if(!file.exists()){
		   try {
			file.createNewFile();
		} catch (Exception e) {
			logger.error(e,e);
		}// 创建文件自身 
		}
		try {
			file.delete();
			FileWriter fileWritter = new FileWriter(file,true);
			StringBuffer sb=new StringBuffer("<div id=\"border\"><table style=\"margin-bottom:5px;\" class=\"ke-zeroborder\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tbody><tr><td class=\"formpage_title\">挂牌公司重大事项温馨提示函</td></tr><tr><td id=\"help\" align=\"right\"><br/></td></tr><tr><td class=\"line\" align=\"right\"><br /></td></tr><tr><td align=\"left\"><table class=\"ke-zeroborder\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tbody>");
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			int num=DBUtil.getInt("select count(*) count from BD_VOTE_WJDCWTB where status='显示'", "count");
			bufferWritter.write(sb.toString());
			for (int i = 1; i <= (num>=50?50:num); i++) {
				StringBuffer sb1=new StringBuffer();
				sb1.append("<tr id=\"itemTr_209"+i+"\"><td id=\"title_WT"+i+"\" width=\"180\" colspan=\"2\"><br/>"+i+"、${WT"+i+"}&nbsp;</td></tr><tr id=\"itemTr_209"+i+"\"><td id=\"data_AS"+i+"\" colspan=\"2\">是否发生:${AS"+i+"}&nbsp;&nbsp;&nbsp;&nbsp;是否披露:${PL"+i+"}</td></tr><tr id=\"itemTr_209"+i+"\"><td class=\"td_title\" id=\"title_BZ"+i+"\" style=\"text-align:left;width:5%;\">情况说明：</td><td id=\"data_BZ"+i+"\" class=\"td_data\">${BZ"+i+"}&nbsp;</td></tr>");
				bufferWritter.write(sb1.toString());
			}
			String ss="</tbody></table></td></tr></tbody></table></div><div style=\"display:none;\">${CUSTOMERNAME}${CUSTOMERNO}${USERNAME}${USERID}${TZGGID}</div>";
			bufferWritter.write(ss);
			bufferWritter.flush();
			bufferWritter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		Date date = new Date();
		String newDate = UtilDate.dateFormat(date);
		ZqbAnnouncementService announcementService=new ZqbAnnouncementService();
		List<HashMap<String, Object>> customer = announcementService.getAllCustomer();
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
		boolean saveFormData = false;
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		//--------------系统邮件名称-------------------
		String WJDCEmailTitle = config.get("WJDCEmailTitle");
		String demUUID = config.get("tzggbuuid");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String ggid = "";
		HashMap hashmap = new HashMap();
		int day = UtilDate.getDay(date);
		Long instanceid = DemAPI.getInstance()
				.newInstance(
						demUUID,
						SecurityUtil.supermanager);
		hashmap.put("instanceId", instanceid);
		hashmap.put("formid", config.get("tzggbformid"));
		String zz="";
		if(day==1){
			zz="月";
		}else{
			zz="周";
		}
		hashmap.put("TZBT", "挂牌公司重大事项信息披露"+zz+"自查反馈表("+newDate+")");
		String format = sd.format(new Date());
		hashmap.put("ZCHFSJ", format);
		hashmap.put("TZNR", "createFormInstance.action?formid="+config.get("wjdcformid")+"&demId="+config.get("wjdcid"));
		hashmap.put("HFR", hfr);
		/*hashmap.put("SFTZ", sftz);*/
		hashmap.put("FSR", "超级管理员");
		hashmap.put("FSSJ", format);
		hashmap.put("FSZT", "未完成");
		hashmap.put("JSZT", "未回复");
		saveFormData = DemAPI.getInstance().saveFormData(demUUID,
				instanceid, hashmap, false);
		ggid = DBUtil.getString(
				"select DATAID from  SYS_ENGINE_FORM_BIND where formid="
						+ config.get("tzggbformid") + " and instanceid="
						+ instanceid + "", "DATAID");

		if (hfr != null && !"".equals(hfr)) {
			String[] hfrArr = hfr.split(",");
			List<HashMap> list = new ArrayList();
			for (String string : hfrArr) {
				String name = string.substring(0, string.indexOf("["));
				StringBuffer sql = new StringBuffer();
				sql.append("select a.username as username,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel from orguser a left join bd_zqb_kh_base b on a.departmentname=b.customername where a.userid =?");
				Connection conn = DBUtil.open();
				PreparedStatement stmt = null;
				ResultSet rset = null;
				try {
					stmt = conn.prepareStatement(sql.toString());
					stmt.setString(1, name);
					rset = stmt.executeQuery();
					while (rset.next()) {
						HashMap map = new HashMap();
						String uname = rset.getString("username");
						String departmentname = rset
								.getString("departmentname");
						String orgroleid = rset.getString("orgroleid");
						String mobile = rset.getString("mobile");
						String zqdm = rset.getString("zqdm");
						String tel = rset.getString("tel");
						map.put("XM", uname);
						map.put("GSMC", departmentname);
						map.put("GSDM", zqdm);
						if (orgroleid != null && !"".equals(orgroleid)) {
							if ("3".equals(orgroleid)) {
								map.put("SJH", tel);
							} else {
								map.put("SJH", mobile);
							}
						}
						String userid = UserContextUtil.getInstance().getUserId(string);
						UserContext target = UserContextUtil.getInstance().getUserContext(userid);
						if (target != null) {
							if(smsstatu.equals("是")){
								String sendMobile = sendMobile = target.get_userModel().getMobile();
								if(sendMobile != null && !sendMobile.equals("")){
									MessageAPI.getInstance().sendSMS(mobile, smsContent);
								}
							}
							if(mailstatus.equals("是")){
								String email = target.get_userModel().getEmail();
								if(email != null && !email.equals("")){
									MessageAPI.getInstance().sendSysMail("系统邮件", email, WJDCEmailTitle,mailContent,"");
								}
							}
						}
						list.add(map);
					}
				} catch (Exception e) {
					logger.error(e,e);
				} finally{
					DBUtil.close(conn, stmt, rset);
				}
			}
			for (HashMap map : list) {
				map.put("formid", config.get("hfqkbformid"));
				map.put("GGID", ggid);
				map.put("STATUS", "未回复");
				Long instanceidTz = DemAPI.getInstance().newInstance(
						config.get("hfqkbuuid"),
						SecurityUtil.supermanager);
				DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),
						instanceidTz, map, false);
			}
		}
		//----------------------------------------
	}

}

