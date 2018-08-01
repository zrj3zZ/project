package com.ibpmsoft.project.zqb.trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import org.apache.log4j.Logger;
public class ZQB_DDFP_SaveEvent extends DemTriggerEvent{
	private static Logger logger = Logger.getLogger(ZQB_DDFP_SaveEvent.class);
	public ZQB_DDFP_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**
	 * 执行触发器
	 */
	public boolean execute(){ 
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		boolean isFs=false;
		/*//更新是否分派字段为是
		HashMap hashmap = new HashMap();
		hashmap.put("KHBH", map.get("KHBH").toString());
		hashmap.put("KHMC", map.get("KHMC").toString());
		List<HashMap> dList = DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", hashmap, null);
		if(dList.size()==1){
			HashMap ddfpmap=dList.get(0);
			ddfpmap.put("SFFP","是");
			DemAPI.getInstance().updateFormData("84ff70949eac4051806dc02cf4837bd9",Long.parseLong(ddfpmap.get("INSTANCEID").toString()), 
					ddfpmap, Long.parseLong(ddfpmap.get("ID").toString()), false);
		}*/
		//发送消息
		
		String customerno = "";
		if(map.get("KHBH")!=null){
			customerno = map.get("KHBH").toString();
		}
		
		String smsContent = "";
		String sysMsgContent = "";
		String CXDD = "";
		String ZZCX = "";
		if(map.get("TXDF").equals("是")){
			isFs=true;
			String CUSTOMERNAME = DBUtil.getString("SELECT CUSTOMERNAME FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO = '"+ customerno +"'", "CUSTOMERNAME");
			String ZQDM = DBUtil.getString("SELECT ZQDM FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO = '"+ customerno +"'", "ZQDM");
			String ZQJC = DBUtil.getString("SELECT ZQJC FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO = '"+ customerno +"'", "ZQJC");
			CXDD = DBUtil.getString("SELECT KHFZR FROM BD_MDM_KHQXGLB WHERE KHBH = '"+customerno+"'", "KHFZR");
			ZZCX = DBUtil.getString("SELECT FHSPR FROM BD_MDM_KHQXGLB WHERE KHBH = '"+customerno+"'", "FHSPR");
			if(CUSTOMERNAME != "" && CUSTOMERNAME!=null){
				smsContent += CUSTOMERNAME+"项目，(";
			}
			if(ZQDM != "" && ZQDM!=null){
				smsContent += ("简称："+ZQDM+"，");
			}
			if(ZQJC != "" && ZQJC!=null){
				smsContent += ("股票代码："+ZQJC+")，");
			}else{
				smsContent += ")，";
			}
			if(CXDD != "" && CXDD!=null){
				smsContent += "持续督导专员："+CXDD;
			}
			if(ZZCX != "" && ZZCX!=null){
				smsContent += "，专职持续督导："+ZZCX;
			}
			//smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.DDFP_ADD_KEY, map);
			//sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.DDFP_ADD_KEY, map);
			sysMsgContent = smsContent;
		}
		//获取收信人，发送短信和邮件
		if(isFs){
			List<Map<String,Object>> list=ZQBNoticeUtil.getInstance().getZYZDCustomer(customerno);
			for(Map<String,Object> c:list){
				String khfzr=c.get("KHFZR").toString();//持续督导专员
				if(!khfzr.equals("")){
					sendMsg(khfzr,smsContent,sysMsgContent);
					sendEmail(khfzr, sysMsgContent);
				}
				Boolean issame=(Boolean)c.get("ISSAME");
				if(!issame){
					//不相同则给专制持续督导发送
					String fhspr=c.get("FHSPR").toString();
					sendMsg(fhspr,smsContent,sysMsgContent);
					sendEmail(fhspr, sysMsgContent);
				}
				if(CXDD != null || ZZCX !=null){
					List<HashMap<String,String>> orgUserList = getOrgUserList(customerno);
					for (HashMap<String,String> userMap : orgUserList) {
						String mobile = userMap.get("MOBILE");
						String email = userMap.get("EMAIL");
						UserContext uc = this.getUserContext();
						if(mobile != null && !mobile.equals("")){
							MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
						}
						if(email != null && !email.equals("")){
							MessageAPI.getInstance().sendSysMail("持续督导分派提醒", email, "持续督导分派提醒", sysMsgContent);
						}
					}
				}
			}
		}
		String value=hash.get("KHMC")==null?"":hash.get("KHMC").toString();
		Long dataId=Long.parseLong(hash.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "持续督导分派", "添加/编辑持续督导分派："+value);
		return true;
	}
	public void sendMsg(String userAddress,String smsContent,String sysMsgContent){
		String userid = UserContextUtil.getInstance().getUserId(userAddress);
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		UserContext uc = this.getUserContext();
		if(target!=null){
				//“场外市场部经理”会分配客户给“专职持续督导”,如果也填写了 “持续督导专员”并且选择了“提醒对方”，那么应该给这2个人（注意如果同一人，只发送一条）对应的短信、系统消息通知
				if(!smsContent.equals("")){
					String mobile = target.get_userModel().getMobile();
					if(mobile!=null&&!mobile.equals("")){
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				if(!sysMsgContent.equals("")){ 
						MessageAPI.getInstance().sendSysMsg(userid, "持续督导分派提醒", sysMsgContent);
				}
		}
	}
	public void sendEmail(String userAddress,String sysMsgContent){
		String userid = UserContextUtil.getInstance().getUserId(userAddress);
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		if(target!=null){
				//“场外市场部经理”会分配客户给“专职持续督导”,如果也填写了 “持续督导专员”并且选择了“提醒对方”，那么应该给这2个人（注意如果同一人，只发送一条）对应的短信、系统消息通知
			if(!sysMsgContent.equals("")){
				String mail = target.get_userModel().getEmail();
				if(mail!=null&&!mail.equals("")){
					MessageAPI.getInstance().sendSysMail("持续督导分派提醒", mail, "持续督导分派提醒", sysMsgContent);
				}
			}
		}
	}
	
	public List<HashMap<String,String>> getOrgUserList(String customerno) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MOBILE,EMAIL FROM ORGUSER WHERE EXTEND1 = ? AND USERSTATE=0");
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> map=new HashMap<String,String>();
				String email = rs.getString("EMAIL");
				String mobile = rs.getString("MOBILE");
				map.put("MOBILE", mobile);
				map.put("EMAIL", email);
				dataList.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
}
