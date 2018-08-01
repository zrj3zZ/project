package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.sdk.MessageAPI;

public class DdqyEvent implements IWorkScheduleInterface{
	@Override
	public boolean executeBefore() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始督导签约提醒...... ");
		return true;
	}

	@Override
	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始督导签约提醒...... ");
		SendMsg();
		return true;
	}

	@Override
	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：[" + UtilDate.getNowDatetime() + "]-即将开始督导签约提醒...... ");
		return true;
	}
	private void SendMsg() {
		List lables = new ArrayList();
		lables.add("qy");
		lables.add("qs");
		lables.add("qssj");
		lables.add("qysj");
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select   decode(s.sxzl,null,'nulls',s.sxzl) qy ,  decode(s.fkdzl,null,'nulls',s.fkdzl) qs,s.tel qssj,s.gsmc qysj from BD_XP_ZYDDSXB s where s.extend5 is null ");
		
		Map params = new HashMap();
		List<HashMap> targetmsg = DBUtil.getDataList(lables, sb.toString(), params);
		for (HashMap data : targetmsg) {
			String qy = data.get("qy").toString();
			String qs = data.get("qs").toString();
			String qssj = data.get("qssj").toString();
			String qysj = data.get("qysj").toString();
			if(!"nulls".equals(qy) ){
				String sqy=qy;
				Object mobile = qysj;
				if (mobile != null && !mobile.equals("")) {	
					
					MessageAPI.getInstance().sendSMS(mobile.toString(),sqy);
				}
			}
			if( !"nulls".equals(qs)){
				String sqy=qs;
				Object mobile = qssj;
				if (mobile != null && !mobile.equals("")) {	
					
					MessageAPI.getInstance().sendSMS(mobile.toString(),sqy);
				}
			}
			
			
		}
	
	}
}
