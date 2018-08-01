package com.iwork.plugs.meeting.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
public class MeetingEvent implements IWorkScheduleInterface {
	private static Logger logger = Logger.getLogger(MeetingEvent.class);
	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-会议信息抓取结束......");
		return true;
	}
	 public  String getNoticeUserId(String roleId,String customername){
			//获取市场部总经理
		 		Map params = new HashMap();
		 		params.put(1,roleId);
		 		params.put(2,customername);
				String userid = DBUTilNew.getDataStr("USERID","select * from OrgUser  where orgroleid =  ? and departmentname= ? ",params);
				
				return userid;
		 }
	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-会议信息抓取前...... ");
		// 得到会议列表，若到达提醒时间，给督导人员和董秘发送短信 
		HashMap conditionMap = new HashMap(); 
		conditionMap.put("STATUS", "未召开");
		String customerno="";
		String smsContent = "";
		String sysMsgContent = "";
		String useraddress ="";
		HashMap map;
		List<HashMap> list = DemAPI.getInstance().getList("ab33c7b0b1b04bb3adbd16eb77a43409", conditionMap, null);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				    try { 
				        Calendar cal = Calendar.getInstance(); 
				        cal.setTime(sdf.parse(list.get(i).get("PLANTIME").toString())); 
				        cal.add(Calendar.DATE,-Integer.parseInt(list.get(i).get("NOTICETIME").toString())); 
				        if(Calendar.getInstance().getTime().getYear()==
				        		cal.getTime().getYear()&&Calendar.getInstance().getTime().getMonth()==
					        		cal.getTime().getMonth()&&Calendar.getInstance().getTime().getDate()==
						        		cal.getTime().getDate()){
				        	//给督导人员和董秘发送短信
				        	if(list.get(i).get("CUSTOMERNO")!=null){
				    			customerno = list.get(i).get("CUSTOMERNO").toString();
				    		}
				        	map=new HashMap();
				     		map.put("CUSTOMERNAME",list.get(i).get("CUSTOMERNAME").toString());
				     		map.put("MEETNAME", list.get(i).get("MEETNAME").toString());
				     		map.put("PLANTIME", list.get(i).get("PLANTIME").toString());
				     		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.HYJH_TX_KEY, map); 
				        	useraddress = ZQBNoticeUtil.getInstance().getDuDaoCustomer(customerno);
				        	String userid = UserContextUtil.getInstance().getUserId(useraddress);
				     		UserContext uc =  UserContextUtil.getInstance().getUserContext(SecurityUtil.supermanager); 
				     		UserContext target = UserContextUtil.getInstance().getUserContext(userid); 			     		
				     		UserContext t = UserContextUtil.getInstance().getUserContext(
				     				getNoticeUserId("3",list.get(i).get("CUSTOMERNAME").toString())); 
				     		if(target!=null){
				     			sendDX(smsContent,target,uc);
				     		}
				     		if(t!=null){
				     			sendDX(smsContent,t,uc);
				     		}
							
					    }
				    } catch (Exception e) { 
				    	logger.error(e,e);
				    } 
				
		  }
		}
		
		return true;
	}
	 public void sendDX(String smsContent,UserContext target ,UserContext uc){
		 if(!smsContent.equals("")){
				String mobile = target.get_userModel().getMobile();
				if(mobile!=null&&!mobile.equals("")){
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
			}
	 }
	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-会议信息抓取中...... ");
		
		return true;
	}

}
