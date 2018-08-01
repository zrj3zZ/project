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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.service.ZqbAnnouncementService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.app.weixin.process.action.qy.util.TestSendMes;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.iform.action.SysEngineIFormMapAction;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.iform.service.SysEngineIFormMapService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;

import org.apache.log4j.Logger;
public class VoteEvent implements IWorkScheduleInterface {
	
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static Logger logger = Logger.getLogger(VoteEvent.class);
	
	public boolean executeAfter() throws ScheduleException {
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		Long a = Calendar.getInstance().getTimeInMillis();
		//生成flt文件前，先跟据信披自查问题数量，更新表单域设置的默认值
		int xpzcCount = DBUtil.getInt("SELECT COUNT(ID) AS NUM FROM BD_VOTE_WJDCWTB WHERE TYPE='信披自查'", "NUM");
		updateSetting(xpzcCount);
		/*Date date = new Date();
		int day = UtilDate.getDay(date);
		if(day!=1){*/
			AddGGToDB(xpzcCount);
		/*}else{
			AddGGToMonthDB();
		}*/
		Long b = Calendar.getInstance().getTimeInMillis();
		System.out.println(b-a);
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		return true;
	}
	private void updateSetting(int xpzcCount){
		Long formid = DBUtil.getLong("SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='挂牌公司重大事项温馨提示函'", "ID");
		int zxwtCount = DBUtil.getInt("SELECT COUNT(ID) AS NUM FROM BD_VOTE_WJDCWTB WHERE TYPE='知悉问题'", "NUM");
		//设置默认值前将WT(问题)、AS(是否发生)、PL(是否披露)、YJXYSFFS(预计下月/周是否发生)的默认值参数更新为空
		DBUtil.executeUpdate("update SYS_ENGINE_IFORM_MAP s set s.field_default='' WHERE s.IFORM_ID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='挂牌公司重大事项温馨提示函') AND (FIELD_NAME LIKE 'WT%' OR FIELD_NAME LIKE 'AS%' OR FIELD_NAME LIKE 'PL%' OR FIELD_NAME LIKE 'YJXYSFFS%')");
		
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer")==null?"":config.get("zqServer").toString();
		
		SysEngineIFormMapService sysEngineIFormMapService = (SysEngineIFormMapService)SpringBeanUtil.getBean("sysEngineIFormMapService");
		List<SysEngineIformMap> datalist = (sysEngineIFormMapService).getMapList(formid);
		for (SysEngineIformMap sysEngineIformMap : datalist) {
			//只遍历WT(问题)、AS(是否发生)、PL(是否披露)、YJXYSFFS(预计下月/周是否发生)
			if(sysEngineIformMap.getFieldName().startsWith("WT")||sysEngineIformMap.getFieldName().startsWith("AS")||sysEngineIformMap.getFieldName().startsWith("PL")||sysEngineIformMap.getFieldName().startsWith("YJXYSFFS")){
				for (int i = 1; i <= xpzcCount; i++) {
					if(sysEngineIformMap.getFieldName().equals("WT"+i)||sysEngineIformMap.getFieldName().equals("AS"+i)||sysEngineIformMap.getFieldName().equals("PL"+i)||sysEngineIformMap.getFieldName().equals("YJXYSFFS"+i)){
						/*if(sysEngineIformMap.getFieldName().equals("WT"+i)){
							//设置默认值参数
							Map params = new HashMap();
							params.put(1, i);
							sysEngineIformMap.setFieldDefault(com.iwork.commons.util.DBUtil.getDataStr("QUESTION", "select QUESTION from (select a.QUESTION,rownum r from(select QUESTION from BD_VOTE_WJDCWTB where type='信披自查' order by sortid) a) where r=?", params));
						}*/
						if(sysEngineIformMap.getFieldName().equals("AS"+i)||sysEngineIformMap.getFieldName().equals("PL"+i)||sysEngineIformMap.getFieldName().equals("YJXYSFFS"+i)){
							Map params = new HashMap();
							params.put(1, i);
							sysEngineIformMap.setFieldDefault(com.iwork.commons.util.DBUtil.getDataStr("DEFULTANSWER", "select DEFULTANSWER from (select a.DEFULTANSWER,rownum r from(select DEFULTANSWER from BD_VOTE_WJDCWTB where type='信披自查' order by sortid) a) where r=?", params));
						}
						//修改默认值
						sysEngineIFormMapService.update(sysEngineIformMap);
					}
				}
				//华龙证券\西南证券需要判断是否有知悉问题
				if(zqServer.equals("hlzq")||zqServer.equals("xnzq")){
					for (int i = (xpzcCount+1); i <= (xpzcCount+zxwtCount); i++) {
						if(sysEngineIformMap.getFieldName().equals("WT"+i)||sysEngineIformMap.getFieldName().equals("AS"+i)){
							/*if(sysEngineIformMap.getFieldName().equals("WT"+i)){
								Map params = new HashMap();
								params.put(1, i-xpzcCount);
								sysEngineIformMap.setFieldDefault(com.iwork.commons.util.DBUtil.getDataStr("QUESTION", "select QUESTION from (select a.QUESTION,rownum r from(select QUESTION from BD_VOTE_WJDCWTB where type='知悉问题' order by sortid) a) where r=?", params));
							}*/
							if(sysEngineIformMap.getFieldName().equals("AS"+i)){
								Map params = new HashMap();
								params.put(1, i-xpzcCount);
								sysEngineIformMap.setFieldDefault(com.iwork.commons.util.DBUtil.getDataStr("DEFULTANSWER", "select DEFULTANSWER from (select a.DEFULTANSWER,rownum r from(select DEFULTANSWER from BD_VOTE_WJDCWTB where type='知悉问题' order by sortid) a) where r=?", params));
							}
							sysEngineIFormMapService.update(sysEngineIformMap);
						}
					}
				}
			}
			
		}
	}
	
	public void AddGGToDB(int xpzcCount) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer")==null?"":config.get("zqServer").toString();
		Date date = new Date();
		String newDate = UtilDate.dateFormat(date);
		String zz="";
		//-------------发送邮件短信start-----------------
		/**短信邮件是否可发送*/
		String smsstatu = DBUtil.getString("SELECT SMS FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "SMS");
		String mailstatus = DBUtil.getString("SELECT EMAIL FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "EMAIL");
		
		/**短信、邮件文本*/
		String smsContent = "";//DBUtil.getString("SELECT DXTXWB FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "DXTXWB");
		String mailContent = "";//DBUtil.getString("SELECT YJTXWB FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "YJTXWB");
//		if(!zqServer.equals("")&&zqServer.equals("dgzq")){
			smsContent="您好，"+SystemConfig._iworkServerConf.getTitle()+"，发送了"+"挂牌公司重大事项信息披露自查反馈表("+newDate+")"+"，请尽快填写，谢谢！";
			mailContent="您好，"+SystemConfig._iworkServerConf.getTitle()+"，发送了"+"挂牌公司重大事项信息披露自查反馈表("+newDate+")"+"，请尽快填写，谢谢！";
//		}
		
		//-----------将公告数据添加至数据库----------
		String path = VoteEvent.class.getResource("").getPath().replace("/", File.separator).substring(1, VoteEvent.class.getResource("").getPath().replace("/", File.separator).indexOf("WEB-INF"));
		reWriteFltFile(xpzcCount, zqServer, path, newDate);
		ZqbAnnouncementService announcementService=new ZqbAnnouncementService();
		List<HashMap<String, Object>> customer = announcementService.getAllCustomer();
		StringBuffer ssb = new StringBuffer();
		for (int i = 0; i < customer.size(); i++) {
			if (i == (customer.size() - 1)) {
				ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]");
			} else {
				ssb.append(customer.get(i).get("USERID") + "[" + customer.get(i).get("USERNAME") + "]").append(",");
			}
		}
		String hfr = ssb.toString();
		boolean saveFormData = false;
		String demUUID = config.get("tzggbuuid");
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String ggid = "";
		HashMap hashmap = new HashMap();
		int day = UtilDate.getDay(date);
		Long instanceid = DemAPI.getInstance().newInstance(demUUID,SecurityUtil.supermanager);
		hashmap.put("instanceId", instanceid);
		hashmap.put("formid", config.get("tzggbformid"));
		/*if(day==1){
			zz="月";
		}else{
			zz="周";
		}*/
		hashmap.put("TZBT", "挂牌公司重大事项信息披露"+zz+"自查反馈表("+newDate+")");
		String format = sd.format(new Date());
		hashmap.put("ZCHFSJ", format);
		hashmap.put("TZNR", "createFormInstance.action?formid="+config.get("wjdcformid")+"&demId="+config.get("wjdcid"));
		hashmap.put("HFR", "");
		/*hashmap.put("SFTZ", sftz);*/
		hashmap.put("FSR", "超级管理员");
		hashmap.put("FSSJ", format);
		hashmap.put("FSZT", "未完成");
		hashmap.put("JSZT", "未回复");
		saveFormData = DemAPI.getInstance().saveFormData(demUUID,instanceid, hashmap, false);
		ggid = DBUtil.getString("select DATAID from  SYS_ENGINE_FORM_BIND where formid=" + config.get("tzggbformid") + " and instanceid=" + instanceid + "", "DATAID");
		//--------------系统邮件名称-------------------
		String WJDCEmailTitle = config.get("WJDCEmailTitle");
		if (hfr != null && !"".equals(hfr)) {
			String[] hfrArr = hfr.split(",");
			List<HashMap> list = new ArrayList();
			for (String string : hfrArr) {
				String name = string.substring(0, string.indexOf("["));
				StringBuffer sql = new StringBuffer();
				sql.append("select a.userid as userid,a.extend1 as customerno,a.username as username,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel from orguser a left join bd_zqb_kh_base b on a.extend1=b.customerno where b.ygp='已挂牌' and  b.status='有效'   and (b.cxddbg<>'转出' or b.cxddbg is null) and a.userid =?");
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
						String customerno = rset.getString("customerno");
						String uid=rset.getString("userid");
						String departmentname = rset.getString("departmentname");
						String orgroleid = rset.getString("orgroleid");
						String mobile = rset.getString("mobile");
						String zqdm = rset.getString("zqdm");
						String tel = rset.getString("tel");
						map.put("USERID", uid);
						map.put("XM", uname);
						map.put("GSMC", departmentname);
						map.put("GSDM", zqdm);
						map.put("CUSTOMERNO", customerno);
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
								try {
									TestSendMes tms=new TestSendMes();
									tms.sendDcwjMsgList(uid, smsContent);
								} catch (Exception e) {}
								String sendMobile = "";
								/*if ("3".equals(orgroleid)) {
									sendMobile=tel;
								} else {*/
									sendMobile = target.get_userModel().getMobile();
								/*}*/
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
			
			DBUtil.executeUpdate("DELETE FROM SYS_ENGINE_FORM_BIND WHERE FORMID="+(config.get("wjdcformid")==null?"0":config.get("wjdcformid").toString()));
			DBUtil.executeUpdate("DELETE FROM SYS_INSTANCE_DATA WHERE FORMID="+(config.get("wjdcformid")==null?"0":config.get("wjdcformid").toString()));
			DBUtil.executeUpdate("UPDATE SYSSEQUENCE S SET S.SEQUENCEVALUE=(SELECT ((CASE WHEN MAX(ID) IS NULL THEN 0 ELSE MAX(ID) END)+1) AS MAXID FROM BD_VOTE_WJDC) WHERE S.SEQUENCEKEY='BD_VOTE_WJDC'");
			
			List lables = new ArrayList();
			lables.add("QUESTION");
			lables.add("DEFULTANSWER");
			lables.add("TYPE");
			List<HashMap> questionList = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT QUESTION,DEFULTANSWER,TYPE FROM BD_VOTE_WJDCWTB WHERE TYPE IN ('信披自查','知悉问题') ORDER BY TYPE ASC,SORTID ASC", null);
			HashMap question = new HashMap();
			for (int i = 0; i <= 49; i++) {
				if(i<questionList.size()){
					question.put("WT"+(i+1), questionList.get(i).get("QUESTION"));
					question.put("AS"+(i+1), questionList.get(i).get("DEFULTANSWER"));
					question.put("PL"+(i+1), questionList.get(i).get("DEFULTANSWER"));
					question.put("YJXYSFFS"+(i+1), questionList.get(i).get("DEFULTANSWER"));
					question.put("TYPE"+(i+1), questionList.get(i).get("TYPE"));
				}else{
					question.put("WT"+(i+1), "");
					question.put("AS"+(i+1), "");
					question.put("PL"+(i+1), "");
					question.put("YJXYSFFS"+(i+1), "");
					question.put("TYPE"+(i+1), "");
				}
			}
			
			StringBuffer logStr = new StringBuffer();
			logStr.append(UtilDate.getNowdate()+":");
			
			for (HashMap map : list) {
				map.put("formid", config.get("hfqkbformid"));
				map.put("GGID", ggid);
				map.put("STATUS", "未回复");
				Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),SecurityUtil.supermanager);
				DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
				
				Long ins = DemAPI.getInstance().newInstance(config.get("wjdcuuid"),SecurityUtil.supermanager);
				HashMap data = new HashMap();
				data.put("CUSTOMERNAME", map.get("GSMC"));
				data.put("CUSTOMERNO", map.get("CUSTOMERNO"));
				data.put("USERNAME", map.get("XM"));
				data.put("USERID", map.get("USERID"));
				data.put("TZGGID", ggid);
				
				for (int i = 1; i <= 50; i++) {
					if(question.get("TYPE"+i).equals("信披自查")){
						data.put("WT"+i, question.get("WT"+i)==null?"":question.get("WT"+i));
						data.put("AS"+i, question.get("AS"+i)==null?"":question.get("AS"+i));
						data.put("PL"+i, question.get("PL"+i)==null?"":question.get("PL"+i));
						data.put("YJXYSFFS"+i, question.get("YJXYSFFS"+i)==null?"":question.get("YJXYSFFS"+i));
					}else if(question.get("TYPE"+i).equals("知悉问题")){
						data.put("WT"+i, question.get("WT"+i)==null?"":question.get("WT"+i));
						data.put("AS"+i, question.get("AS"+i)==null?"":question.get("AS"+i));
					}else{
						
					}
					/*data.put("WT"+i, question.get("WT"+i)==null?"":question.get("WT"+i));
					data.put("AS"+i, question.get("AS"+i)==null?"":question.get("AS"+i));
					data.put("PL"+i, question.get("PL"+i)==null?"":question.get("PL"+i));
					data.put("YJXYSFFS"+i, question.get("YJXYSFFS"+i)==null?"":question.get("YJXYSFFS"+i));*/
				}
				
				try {
					boolean f = DemAPI.getInstance().saveFormData(config.get("wjdcuuid"),ins, data, false);
				} catch (Exception e) {
					logStr.append(map.get("USERID").toString()+(map.get("GSMC")));
				}
			}
			
			logger.error(logStr);
		}
		//----------------------------------------
	}

	private void reWriteFltFile(int xpzcCount, String zqServer, String path, String newDate) {
		String filePath = path+"WEB-INF" +File.separator+"templates"+File.separator+"user_templates"+File.separator+"挂牌公司重大事项温馨提示函.flt";
		File file = new File(filePath);
		if(!file.exists()){
		   try {
			file.createNewFile();
		} catch (Exception e) {
			logger.error(e,e);
		}// 创建文件自身 
		}
		int zxwtCount = DBUtil.getInt("SELECT COUNT(ID) AS NUM FROM BD_VOTE_WJDCWTB WHERE TYPE='知悉问题'", "NUM");
		try {
			file.delete();
			FileWriter fileWritter = new FileWriter(file,true);
			StringBuffer sb=new StringBuffer();
			sb.append("<script type=\"text/javascript\">");
				sb.append("$(function(){");
				
				
				sb.append("var ggid = $(\"#TZGGID\").val();");
				sb.append("$.post(\"zqb_vote_getwjdcwt.action\",{ggid:ggid},function (data) {");
				sb.append("var dataJson = eval(\"(\" + data + \")\");");
				for (int i = 1; i <= (xpzcCount+zxwtCount); i++) {
					sb.append("$(\"#WT"+i+"\").val(dataJson[0].WT"+i+");$(\"#labelWT"+i+"\").html(dataJson[0].WT"+i+");");
				}
				sb.append("});");
				
				
					sb.append("for(var i=1;i<="+(xpzcCount+zxwtCount)+";i++){");
						sb.append("var o1 = \"#WT\"+i;");
						sb.append("var oa = \"#itemTr_W\"+i+\"a\";");
						sb.append("var ob = \"#itemTr_W\"+i+\"b\";");
						sb.append("var oc = \"#itemTr_W\"+i+\"c\";");
						sb.append("var yjxysffs= \"#yjxysffs\"+i;");
						sb.append("if($(o1).val()==null||$(o1).val()==\"\"){");
							sb.append("$(oa).remove();");
							sb.append("$(ob).remove();");
							sb.append("$(oc).remove();");
						sb.append("}else if(GetQueryString(\"dqrq\")==\"01\"){");
							sb.append("$(yjxysffs).html(\"预计本月是否发生:\");");
						sb.append("}else{");
							sb.append("$(yjxysffs).html(\"预计下周是否发生:\");");
						sb.append("}");
					sb.append("}");
					sb.append("$.post(\"zqb_vote_getwjdccontent.action\", function (data) {");
					sb.append("	var dataJson = eval(\"(\" + data + \")\");");
					for (int i = 1; i <= (xpzcCount+zxwtCount); i++) {
						sb.append("$(\"#BZ"+i+"\").bind({focus:function(){hideTip(this,dataJson[0].BZ"+i+")},blur:function(){showTip(this,dataJson[0].BZ"+i+")}}).attr(\"placeholder\",dataJson[0].BZ"+i+");setTip($(\"#BZ"+i+"\"),dataJson[0].BZ"+i+");$(\"#BZ"+i+"\").focus();$(\"#BZ"+i+"\").blur();");
					}
						sb.append("document.documentElement.scrollTop = 0;");
					sb.append("});");
					
					sb.append("var ggid = $(\"#TZGGID\").val();");
					sb.append("$.post(\"zqb_vote_gettzggdate.action?ggid=\"+ggid,function (data) {");
						sb.append("	var dataJson = eval(\"(\" + data + \")\");");
						sb.append(" var html = \"填写\"+dataJson[0].THISMON+\"月实际发生内容 \"+dataJson[0].NEXTMON+\"月预计发生内容\";");
						sb.append(" var html_ = \"填写上周实际发生内容，本周预计发生内容\";");
						sb.append(" $(\"#memotd\").html(").append(newDate.substring(8,10).equals("01")?"html":"html_").append(");");
						sb.append("});");
					
				sb.append("});");
				sb.append("function savebeforeEvent() {");
					sb.append("$.ajaxSetup({");
						sb.append("async: false");
						sb.append("});");
					sb.append("for(var i=1;i<="+(xpzcCount+zxwtCount)+";i++){");
						sb.append("var o1 = \"#BZ\"+i;");
						sb.append("if($(o1).attr(\"placeholder\")!=null&&$(o1).attr(\"placeholder\")!=\"\"&&($(o1).attr(\"placeholder\")==$(o1).val()||$(o1).attr(\"placeholder\")==$(o1).html()||$(o1).attr(\"placeholder\")==$(o1).innerHTML)){");
							sb.append("$(o1).val(\"\");");
							sb.append("$(o1).innerHTML=\"\";");
							sb.append("$(o1).text(\"\");");
							sb.append("$(o1).html(\"\");");
							sb.append("}");
						sb.append("}");
					sb.append("return true;");
					sb.append("}");
				sb.append("function setTip(obj,txt){");
					sb.append("var html = obj.value;");
					sb.append("if(html==null||html==''){");
						sb.append("obj.value=txt;");
						sb.append("}");
					sb.append("}");
				sb.append("function hideTip(obj,txt){");
					sb.append("var html = obj.value;");
					sb.append("if(html==txt){");
						sb.append("obj.value=\"\";");
						sb.append("}");
					sb.append("}");
				sb.append("function showTip(obj,txt){");
					sb.append("var html = obj.value;");
					sb.append("if(html==null||html==''){");
						sb.append("obj.value=txt;");
						sb.append("}");
					sb.append("}");
				sb.append("function GetQueryString(name){");
					sb.append("var reg = new RegExp(\"(^|&)\"+ name +\"=([^&]*)(&|$)\");");
					sb.append("var r = window.location.search.substr(1).match(reg);");
					sb.append("if(r!=null)return  unescape(r[2]); return null;");
				sb.append("}");
			sb.append("</script>");
			sb.append("<style type=\"text/css\">");
				sb.append(".breakword{");
					sb.append("word-break:break-all;");
				sb.append("}");
			sb.append("</style>");
			
			sb.append("<div id=\"border\"><table style=\"margin-bottom:5px;\" class=\"ke-zeroborder\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tbody><tr><td class=\"formpage_title\">挂牌公司重大事项温馨提示函</td></tr><tr><td id=\"help\" align=\"right\"><br/></td></tr><tr><td class=\"line\" align=\"right\"><br /></td></tr><tr><td align=\"left\"><table class=\"ke-zeroborder\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tbody>");
			sb.append("<tr><td id=\"memotd\" colspan='2' style='font-size: 16px;text-align: right;'></td></tr>");
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			int num=DBUtil.getInt("select count(*) count from BD_VOTE_WJDCWTB where status='显示'", "count");
			bufferWritter.write(sb.toString());
			String a = newDate.substring(8,10).equals("01")?"预计本月是否发生":"预计下周是否发生";
			StringBuffer sb1=new StringBuffer();
			if(zqServer.equals("xnzq")){
				sb1.append("<tr><td colspan='2' style='font-size: 16px;'>第一部分：").append(zqServer.equals("hlzq")?"注意事项":"重点提示内容").append("</td></tr>");
				for (int i = (xpzcCount+1); i <= (xpzcCount+zxwtCount); i++) {
					sb1.append("<tr id=\"itemTr_W"+i+"a\"><td id=\"title_WT"+i+"\" width=\"180\" colspan=\"2\"><br/>"+(i-xpzcCount)+"、${WT"+i+"}&nbsp;</td></tr>"
							+ "<tr id=\"itemTr_W"+i+"b\"><td id=\"data_AS"+i+"\" colspan=\"2\">是否知悉:${AS"+i+"}</td></tr>"
							+ "<tr id=\"itemTr_W"+i+"c\"><td class=\"td_title\" id=\"title_BZ"+i+"\" style=\"text-align:left;width:5%;\">情况说明：</td><td id=\"data_BZ"+i+"\" class=\"td_data\">${BZ"+i+"}&nbsp;</td></tr>");
				}
			}
			
			
			if(zqServer.equals("hlzq")||zqServer.equals("xnzq")){
				sb1.append("<tr><td colspan='2' style='font-size: 16px;'>第").append(zqServer.equals("xnzq")?"二":"一").append("部分：").append(zqServer.equals("hlzq")?"日常督导":"主要事项").append("</td></tr>");
			}
			for (int i = 1; i <= xpzcCount; i++) {
				sb1.append("<tr id=\"itemTr_W"+i+"a\"><td id=\"title_WT"+i+"\" width=\"180\" colspan=\"2\"><br/>"+i+"、${WT"+i+"}&nbsp;</td></tr>"
						+ "<tr id=\"itemTr_W"+i+"b\"><td id=\"data_AS"+i+"\" colspan=\"2\">是否发生:${AS"+i+"}&nbsp;&nbsp;&nbsp;&nbsp;是否披露:${PL"+i+"}&nbsp;&nbsp;&nbsp;&nbsp;"+ a +":${YJXYSFFS"+i+"}</td></tr>"
						+ "<tr id=\"itemTr_W"+i+"c\"><td class=\"td_title\" id=\"title_BZ"+i+"\" style=\"text-align:left;width:5%;\">情况说明：</td><td id=\"data_BZ"+i+"\" class=\"td_data\">${BZ"+i+"}&nbsp;</td></tr>");
			}
			
			
			if(zqServer.equals("hlzq")){
				sb1.append("<tr><td colspan='2' style='font-size: 16px;'>第二部分：").append(zqServer.equals("hlzq")?"注意事项":"重点提示内容").append("</td></tr>");
				for (int i = (xpzcCount+1); i <= (xpzcCount+zxwtCount); i++) {
					sb1.append("<tr id=\"itemTr_W"+i+"a\"><td id=\"title_WT"+i+"\" width=\"180\" colspan=\"2\"><br/>"+(i-xpzcCount)+"、${WT"+i+"}&nbsp;</td></tr>"
							+ "<tr id=\"itemTr_W"+i+"b\"><td id=\"data_AS"+i+"\" colspan=\"2\">是否知悉:${AS"+i+"}</td></tr>"
							+ "<tr id=\"itemTr_W"+i+"c\"><td class=\"td_title\" id=\"title_BZ"+i+"\" style=\"text-align:left;width:5%;\">情况说明：</td><td id=\"data_BZ"+i+"\" class=\"td_data\">${BZ"+i+"}&nbsp;</td></tr>");
				}
			}
			bufferWritter.write(sb1.toString());
			String ss="</tbody></table></td></tr></tbody></table></div><div style=\"display:none;\">${CUSTOMERNAME}${CUSTOMERNO}${USERNAME}${USERID}${TZGGID}</div>";
			bufferWritter.write(ss);
			bufferWritter.flush();
			bufferWritter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//logger.error(e,e);
		}
	}
	
	public void AddGGToMonthDB() {
		//-------------发送邮件短信start-----------------
		/**短信邮件是否可发送*/
		String smsstatu = DBUtil.getString("SELECT SMS FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "SMS");
		String mailstatus = DBUtil.getString("SELECT EMAIL FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "EMAIL");
		
		/**短信、邮件文本*/
		String smsContent = DBUtil.getString("SELECT DXTXWB FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "DXTXWB");
		String mailContent = DBUtil.getString("SELECT YJTXWB FROM BD_SYS_NOTICE WHERE JZ='"+ZQB_Notice_Constants.XPZC_KEY+"'", "YJTXWB");
		
		//-----------将公告数据添加至数据库----------
		String path = VoteEvent.class.getResource("").getPath().replace("/", File.separator).substring(1, VoteEvent.class.getResource("").getPath().replace("/", File.separator).indexOf("WEB-INF"));
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
		/*if(day==1){
			zz="月";
		}else{
			zz="周";
		}*/
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
				sql.append("select a.username as username,a.departmentname as departmentname,a.orgroleid as orgroleid,a.mobile as mobile,b.zqdm as zqdm,b.tel as tel,a.userid as userid from orguser a left join bd_zqb_kh_base b on a.departmentname=b.customername where a.userid =?");
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
						String uid = rset.getString("userid");
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
						//--------------系统邮件名称-------------------
						String WJDCEmailTitle = config.get("WJDCEmailTitle");
						if (target != null) {
							if(smsstatu.equals("是")){
								try {
									TestSendMes tms=new TestSendMes();
									tms.sendDcwjMsgList(uid, smsContent);
								} catch (Exception e) {}
								String sendMobile = "";
								/*if ("3".equals(orgroleid)) {
									sendMobile=tel;
								} else {*/
								sendMobile = target.get_userModel().getMobile();
								/*}*/
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

