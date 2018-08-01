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

public class NoticeDgdyEvent implements IWorkScheduleInterface{

	@Override
	public boolean executeBefore() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始底稿调阅流程提醒...... ");
		return true;
	}

	@Override
	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始底稿调阅流程提醒...... ");
		SendMsg();
		return true;
	}

	@Override
	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始底稿调阅流程提醒...... ");
		return true;
	}
	private void SendMsg() {
		List lables = new ArrayList();
		lables.add("sqsj");
		lables.add("yjghsj");
		lables.add("dyzlnr");
		lables.add("dyrid");
		lables.add("dyrxm");
		lables.add("userid");
		lables.add("username");
		lables.add("email");
		lables.add("mobile");
		lables.add("sy");
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.sqsj,a.yjghsj,a.dyzlnr,a.dyrid,a.dyrxm,l.userid,l.username,l.email,l.mobile,a.sy from  BD_ZQB_DGDYLCB a, ");
		sb.append(" (select r.mobile,r.email,r.userid,r.username,s.subid from orguser r,(select z.subid,z.createuserid from BD_XP_TYRCGLB z where z.mainid  in  ");
		sb.append(" ( select id from iwork_sch_calendar t where t.startdate= TO_DATE(?,'yyyy-MM-dd') )) s ");
		sb.append("  where r.userid=s.createuserid) l where to_char(a.instanceid)=l.subid and a.spzt <>'已归档' ");
		Map params = new HashMap();
		params.put(1, UtilDate.getNowdate());
		List<HashMap> targetmsg = DBUtil.getDataList(lables, sb.toString(), params);
		for (HashMap data : targetmsg) {
			String dyrid = data.get("dyrid").toString();
			String userid = data.get("userid").toString();
			if(dyrid.equals(userid)){
				String sqy="您在"+data.get("sqsj").toString()+"日申请调阅的"+data.get("dyzlnr").toString()+"在"+data.get("yjghsj").toString()+"日到期，请按时归还!";
				//发短信
				Object mobile = data.get("mobile");
				if (mobile != null && !mobile.equals("")) {	
					
					MessageAPI.getInstance().sendSMS(mobile.toString(),sqy);
				}
				//发邮件
				Object email = data.get("email");
				if (email != null && !email.equals("")) {		
					MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email.toString(), data.get("yjghsj").toString()+"归还提醒",sqy+"   事由："+data.get("sy").toString(),"");
				}
			}else{
				String sqy=data.get("dyrxm").toString()+"在"+data.get("sqsj").toString()+"日申请调阅的"+data.get("dyzlnr").toString()+"在"+data.get("yjghsj").toString()+"日到期，请留意!";
				//发短信
				Object mobile = data.get("mobile");
				if (mobile != null && !mobile.equals("")) {		
				
					MessageAPI.getInstance().sendSMS(mobile.toString(),sqy);
				}
				//发邮件
				Object email = data.get("email");
				if (email != null && !email.equals("")) {		
					MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email.toString(), data.get("yjghsj").toString()+"归还提醒", sqy+"   事由："+data.get("sy").toString(),"");
				}
			}
			
			
		}
	
	}
}
