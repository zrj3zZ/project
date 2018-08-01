package com.iwork.app.schedule.quartz;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
/**
 * 当用户设置了定时提醒功能后，系统将每间隔半小时校验一次，将短信发至督导人员
 * @author duqingqing
 *
 */
public class MeetScheduleTask{

	private static Logger logger = Logger.getLogger(MeetScheduleTask.class);
    /**
     * 在会议开始的时间，要给对应的持续督导人员 发送短信提醒，提醒内容是：【公司简称】于2014-12-24日08:30分召开第一届第十一次董事会，
     * 请注意汇总信息披露相关资料！

     */
    public void execute() {
    	 String ZQB_MEET_PLAN_UUID = "ab33c7b0b1b04bb3adbd16eb77a43409";
		 HashMap conditionMap = new HashMap(); 
		 conditionMap.put("STATUS", "未召开");
		 String customerno="";
		 String smsContent = "";
			String sysMsgContent = "";
			String useraddress ="";
			HashMap map;
		List<HashMap> list = DemAPI.getInstance().getList(ZQB_MEET_PLAN_UUID, conditionMap, null);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				  SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd"); 
				    try { 
				    	Date upDate = UtilDate.StringToDate(list.get(i).get("PLANTIME").toString(),"yyyy-MM-dd HH:mm");
				    	//如果到达召开时间，或者是当前时间是6:30，则将今天没有进行短信提醒的会议都发送给持续督导人员
				        if((UtilDate.datetimeFormat(upDate,"yyyy-MM-dd").equals(UtilDate.datetimeFormat(new Date(),"yyyy-MM-dd"))
				        		&&UtilDate.datetimeFormat(upDate,"HH:mm").compareTo(UtilDate.datetimeFormat(new Date(),"HH:mm"))<=0
				        		&&UtilDate.datetimeFormat(upDate,"HH:mm").compareTo("08:30")>=0&&
				        		!list.get(i).get("SFYFDX").equals("是"))
				        		||(UtilDate.datetimeFormat(new Date(),"HH:mm").compareTo("18:30")==0&&
				        				!list.get(i).get("SFYFDX").equals("是")&&
				        				UtilDate.datetimeFormat(upDate,"yyyy-MM-dd").equals(UtilDate.datetimeFormat(new Date(),"yyyy-MM-dd")))){
				        	//给督导人员发送短信
				        	if(list.get(i).get("CUSTOMERNO")!=null){
				    			customerno = list.get(i).get("CUSTOMERNO").toString();
				    		}
				        	map=new HashMap();
				     		map.put("CUSTOMERNAME",list.get(i).get("CUSTOMERNAME").toString());
				     		map.put("MEETNAME", list.get(i).get("MEETNAME").toString());
				     		map.put("PLANTIME",UtilDate.datetimeFormat(upDate,"yyyy-MM-dd")+"日"+UtilDate.datetimeFormat(upDate,"HH:mm")+"分");
				     		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.HYJH_TX_KEY, map); 
				        	 useraddress = ZQBNoticeUtil.getInstance().getDuDaoCustomer(customerno);
				        	 String userid = UserContextUtil.getInstance().getUserId(useraddress);
				     		UserContext target = UserContextUtil.getInstance().getUserContext(userid); 		
				     		UserContext uc =UserContextUtil.getInstance().getUserContext(SecurityUtil.supermanager);
				     		if(target!=null){
				     			sendDX(smsContent,target,uc);
				     			//更新发送状态
				     			StringBuffer sql = new StringBuffer("update BD_MEET_PLAN set SFYFDX='是' where id=?");
				     			Connection conn = DBUtil.open();
				     			PreparedStatement stmt = null;
				     			 int j=0;
				     			try {
				     				 stmt = conn.prepareStatement(sql.toString());
				     				 stmt.setString(1, list.get(i).get("ID").toString());
				     				 j = stmt.executeUpdate();
				     			} catch (Exception e) {
				     				logger.error(e,e);
				     			}finally {
				     				DBUtil.close(conn, stmt, null);
				     	    	}
				     		}
					    }
				    } catch (Exception e) { 
				    	logger.error(e,e);
				    } 
				
		  }
		}
		
    } 

    public void sendDX(String smsContent,UserContext target ,UserContext uc){
		 if(!smsContent.equals("")){
				String mobile = target.get_userModel().getMobile();
				if(mobile!=null&&!mobile.equals("")){
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
			}
	 }

	

    
}
