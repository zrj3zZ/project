package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.sdk.MessageAPI;

public class NoticeMeetingRemindEvent implements IWorkScheduleInterface  {

	@Override
	public boolean executeBefore() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始股东大会公告披露提醒...... ");
		return true;
	}

	@Override
	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-开始发送股东大会公告披露提醒...... ");
		SendMsg();
		return true;
	}

	@Override
	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-股东大会公告披露提醒完毕...... ");
		return true;
	}

	private void SendMsg() {
		List lables = new ArrayList();
		lables.add("USERID");
		lables.add("TITLE");
		lables.add("STARTDATE");
		lables.add("ENDDATE");
		lables.add("STARTTIME");
		lables.add("ENDTIME");
		lables.add("ISALERT");
		lables.add("MOBILE");
		lables.add("EMAIL");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.USERID,A.TITLE,A.STARTDATE,A.ENDDATE,A.STARTTIME,A.ENDTIME,A.ISALERT,O.MOBILE,O.EMAIL FROM IWORK_SCH_CALENDAR A LEFT JOIN ORGUSER O ON A.USERID=O.USERID WHERE A.TITLE LIKE '您好，%（%）请确认是否于近期有股东大会即将召开，如正常召开，请及时披露相关公告。' AND A.STARTDATE <= TO_DATE(?,'yyyy-MM-dd') AND TO_DATE(?,'yyyy-MM-dd') <= A.ENDDATE");
		Map params = new HashMap();
		params.put(1, UtilDate.getNowdate());
		params.put(2, UtilDate.getNowdate());
		List<HashMap> targetmsg = DBUtil.getDataList(lables, sql.toString(), params);
		for (HashMap data : targetmsg) {
			String userid = data.get("USERID").toString();
			//发短信
			Object mobile = data.get("MOBILE");
			if (mobile != null && !mobile.equals("")) {				
				MessageAPI.getInstance().sendSMS(mobile.toString(),data.get("TITLE").toString());
			}
			//发邮件
			Object email = data.get("EMAIL");
			if (email != null && !email.equals("")) {		
				MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email.toString(), "披露公告提醒", data.get("TITLE").toString(),"");
			}
			//发系统消息
			//MessageAPI.getInstance().sendSysMsg(userid, "披露公告提醒", data.get("TITLE").toString());
		}
	}
	
}
