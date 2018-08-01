package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;

public class SuperviseTheDailyWorkEvent implements IWorkScheduleInterface {
	
	
	
	public boolean executeBefore() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始发送持续督导日常工作反馈...... ");
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-开始发送持续督导日常工作反馈...... ");
		setDailyWork();
		return true;
	}

	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-发送持续督导日常工作反馈完毕...... ");
		return true;
	}

	/**
	 * 发送持续督导工作反馈信息
	 */
	private void setDailyWork() {
		String cxdder = null;
		Long instanceid = 0l;
		OrgUser user;
		String mobile;
		String email;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext(SecurityUtil.supermanager);
		String content = "持续督导日常工作反馈需要反馈,请登录系统查看!";
		
		String demUUID = DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='通知公告'", null);
		
		String HFUUID = DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='督导工作回复情况'", null);
		
		String demid_formid = DBUtil.getDataStr("IDS", "SELECT ID||'_'||FORMID IDS FROM SYS_DEM_ENGINE WHERE TITLE='持续督导日常工作反馈'", null);
		String url = "createFormInstance.action?formid="+demid_formid.split("_")[1]+"&demId="+demid_formid.split("_")[0]+"";
		
		Date date = new Date();
		String sysdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		String systime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		
		List lables = new ArrayList();
		lables.add("CXDDER");
		String sql = "SELECT DISTINCT SUBSTR(CXDD,0, INSTR(CXDD,'[',1)-1) CXDDER FROM (SELECT KHFZR AS CXDD FROM BD_MDM_KHQXGLB UNION SELECT FHSPR AS CXDD FROM BD_MDM_KHQXGLB) WHERE CXDD IS NOT NULL";
		List<HashMap> cxdderlist = DBUtil.getDataList(lables, sql, null);
		if(cxdderlist.size()>0){
			instanceid = DemAPI.getInstance().newInstance(demUUID, SecurityUtil.supermanager);
			HashMap hashdata = new HashMap();
			hashdata.put("TZBT", "持续督导日常工作反馈"+"("+sysdate+")");
			hashdata.put("ZCHFSJ", systime);
			hashdata.put("TZNR", url);
			hashdata.put("FSR", "超级管理员");
			hashdata.put("JSZT", "");
			hashdata.put("FSZT", "");
			hashdata.put("FSSJ", systime);
			hashdata.put("FSRID", SecurityUtil.supermanager);
			boolean flag = DemAPI.getInstance().saveFormData(demUUID, instanceid, hashdata, false);
			HashMap newdata = DemAPI.getInstance().getFromData(instanceid);
			for (HashMap cxddermap : cxdderlist) {
				cxdder = cxddermap.get("CXDDER").toString();
				user = UserContextUtil.getInstance().getUserContext(cxdder)._userModel;
				
				HashMap map = new HashMap();
				map.put("XM", user.getUsername());
				map.put("STATUS", "未回复");
				map.put("HFSJ", systime);
				map.put("GGID", newdata.get("ID"));
				map.put("USERID", cxdder);
				Long instanceidHf = DemAPI.getInstance().newInstance(HFUUID,SecurityUtil.supermanager);
				boolean result = DemAPI.getInstance().saveFormData(HFUUID,instanceidHf, map, false);
				
				if(result){
					mobile = user.getMobile();
					email = user.getEmail();
					if(mobile!=null&&!mobile.equals("")){
						mobile = user.getMobile()+"["+user.getUsername()+"]";
						MessageAPI.getInstance().sendSMS(mobile, content);
					}
					if(email!=null&&!email.equals("")){
						MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, "持续督导日常工作反馈", content,"");
					}
				}
			}
		}
	}
}
