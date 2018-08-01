package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
public class zqbRCYWAfterSaveEvent extends ProcessTriggerEvent {
	public zqbRCYWAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	private static Logger logger = Logger.getLogger(zqbRCYWAfterSaveEvent.class);
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件      

	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//归档时回写表中的审批状态 字段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			String sq = dataMap.get("NOTICESQ").toString();
			String customerno = dataMap.get("KHBH").toString();
			if("".equals(sq)||sq==null){
				Long instanceid = DemAPI.getInstance().newInstance(this.getConfigUUID("rcywcbuuid"), uc.get_userModel().getUserid());
				HashMap<String,Object> hashdata = new HashMap<String,Object>();
				hashdata.put("SXMC", dataMap.get("SXMC")==null?"":dataMap.get("SXMC").toString());
				hashdata.put("QCRID", dataMap.get("QCRID")==null?"":dataMap.get("QCRID").toString());
				hashdata.put("SXFJ", dataMap.get("SXFJ")==null?"":dataMap.get("SXFJ").toString());
				hashdata.put("EXTEND1", dataMap.get("EXTEND1")==null?"":dataMap.get("EXTEND1").toString());
				hashdata.put("EXTEND2", dataMap.get("EXTEND2")==null?"":dataMap.get("EXTEND2").toString());
				hashdata.put("EXTEND3", dataMap.get("EXTEND3")==null?"":dataMap.get("EXTEND3").toString());
				hashdata.put("EXTEND4", dataMap.get("EXTEND4")==null?"":dataMap.get("EXTEND4").toString());
				hashdata.put("EXTEND5", dataMap.get("EXTEND5")==null?"":dataMap.get("EXTEND5").toString());
				hashdata.put("TASKID", dataMap.get("TASKID")==null?"":dataMap.get("TASKID").toString());
				hashdata.put("STEPID", dataMap.get("STEPID")==null?"":dataMap.get("STEPID").toString());
				hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE").toString());
				hashdata.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER").toString());
				hashdata.put("SXLX", dataMap.get("SXLX")==null?"":dataMap.get("SXLX").toString());
				hashdata.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC").toString());
				hashdata.put("KHBH", dataMap.get("KHBH")==null?"":dataMap.get("KHBH").toString());
				hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
				hashdata.put("SXGY", dataMap.get("SXGY")==null?"":dataMap.get("SXGY").toString());
				hashdata.put("SPZT", "审批通过");
				String actStepId="";
				String actDefId=this.getActDefId();
				if(actDefId.equals(ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC"))){
					actStepId="endevent5";
				}else{
					actStepId="endevent6";
				}
				hashdata.put("STEPID",actStepId);
				hashdata.put("YXID", this.getExcutionId());
				hashdata.put("TASKID", this.getTaskId());
				hashdata.put("LCBS", instanceId);
				hashdata.put("LCBH", actDefId);
				
				flag = DemAPI.getInstance().saveFormData(this.getConfigUUID("rcywcbuuid"), instanceid, hashdata, false);
				HashMap map=new HashMap();
				map.put("NOTICESQ", sq);
				HashMap hashMap = DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"), map, null).get(0);
				String value=hashMap.get("SXMC")==null?"":hashMap.get("SXMC").toString();
				Long dataId=Long.parseLong(hashMap.get("ID").toString());
				LogUtil.getInstance().addLog(dataId, "公告呈报管理", "添加公告："+value);
			}else{
				hashmap.put("NOTICESQ", sq);
				List<HashMap> list = DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"),hashmap, null);
				if (!list.isEmpty()) {
					HashMap map=list.get(0);//原来数据
					map.put("SPZT", "审批通过");
					Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
					Long dataid=Long.parseLong(map.get("ID").toString());
					flag=DemAPI.getInstance().updateFormData(this.getConfigUUID("rcywcbuuid"), instanceid, map, dataid, true);
					String value=map.get("SXMC")==null?"":map.get("SXMC").toString();
					Long dataId=Long.parseLong(map.get("ID").toString());
					LogUtil.getInstance().addLog(dataId, "日常业务呈报管理", "提交公告："+value);
				}else if(list.isEmpty()){//归档时没有数据，则保存一条
					Long instanceid = DemAPI.getInstance().newInstance(this.getConfigUUID("rcywcbuuid"), uc.get_userModel().getUserid());
					HashMap<String,Object> hashdata = new HashMap<String,Object>();
					hashdata.put("SXMC", dataMap.get("SXMC")==null?"":dataMap.get("SXMC").toString());
					hashdata.put("QCRID", dataMap.get("QCRID")==null?"":dataMap.get("QCRID").toString());
					hashdata.put("SXFJ", dataMap.get("SXFJ")==null?"":dataMap.get("SXFJ").toString());
					hashdata.put("EXTEND1", dataMap.get("EXTEND1")==null?"":dataMap.get("EXTEND1").toString());
					hashdata.put("EXTEND2", dataMap.get("EXTEND2")==null?"":dataMap.get("EXTEND2").toString());
					hashdata.put("EXTEND3", dataMap.get("EXTEND3")==null?"":dataMap.get("EXTEND3").toString());
					hashdata.put("EXTEND4", dataMap.get("EXTEND4")==null?"":dataMap.get("EXTEND4").toString());
					hashdata.put("EXTEND5", dataMap.get("EXTEND5")==null?"":dataMap.get("EXTEND5").toString());
					hashdata.put("LCBS", dataMap.get("LCBS")==null?"":dataMap.get("LCBS").toString());
					hashdata.put("LCBH", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
					hashdata.put("TASKID", dataMap.get("TASKID")==null?"":dataMap.get("TASKID").toString());
					hashdata.put("STEPID", dataMap.get("STEPID")==null?"":dataMap.get("STEPID").toString());
					hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE").toString());
					hashdata.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER").toString());
					hashdata.put("SXLX", dataMap.get("SXLX")==null?"":dataMap.get("SXLX").toString());
					hashdata.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC").toString());
					hashdata.put("KHBH", dataMap.get("KHBH")==null?"":dataMap.get("KHBH").toString());
					hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
					hashdata.put("SXGY", dataMap.get("SXGY")==null?"":dataMap.get("SXGY").toString());
					hashdata.put("SPZT", "审批通过1");
					String actStepId="";
					String actDefId=this.getActDefId();
					if(actDefId.equals(ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC"))){
						actStepId="endevent5";
					}else{
						actStepId="endevent6";
					}
					hashdata.put("STEPID",actStepId);
					hashdata.put("YXID", this.getExcutionId());
					hashdata.put("TASKID", this.getTaskId());
					hashdata.put("LCBS", instanceId);
					hashdata.put("LCBH", actDefId);
					
					// 保存到临时公告数据表中
					flag = DemAPI.getInstance().saveFormData(this.getConfigUUID("rcywcbuuid"), instanceid, hashdata, false);
					HashMap map=new HashMap();
					map.put("SXMC", sq);
					HashMap hashMap = DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"), map, null).get(0);
					String value=hashMap.get("SXMC")==null?"":hashMap.get("SXMC").toString();
					Long dataId=Long.parseLong(hashMap.get("ID").toString());
					LogUtil.getInstance().addLog(dataId, "公告呈报管理", "添加公告："+value);
				}
			}
			String content = dataMap.get("KHMC").toString() + "的事项：" + dataMap.get("SXMC").toString() + "，已审批通过";
			senSMS(dataMap,customerno);
		}

		return flag;
	}
	public void senSMS(HashMap dataMap,String customerno) {
		String senduser="";
		String content="";
		String userid = dataMap.get("CREATEUSER").toString();
		String ggzy = (dataMap.get("SXGY").toString()==null||dataMap.get("SXGY").equals(""))?"事项概要：空。":"事项概要："+dataMap.get("SXGY").toString()+"。";
		//List<HashMap<String,String>> orgUserList = getOrgUserList(customerno);
		UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
		/*for (HashMap<String, String> hashMap : orgUserList) {*/
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		if(target != null && ut != null){
			String mobile = target.get_userModel().getMobile();
			String email = target.get_userModel().getEmail();
			content = dataMap.get("KHMC").toString() + "的事项：" + dataMap.get("SXMC").toString() + "，已审批通过";
			if (mobile != null && !mobile.equals("")) {
				mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
				MessageAPI.getInstance().sendSMS(mobile, content);
			}
			if (!content.equals("")) {
				if(email != null && !email.equals("")){
					senduser = ut.get_userModel().getUsername();
					MessageAPI.getInstance().sendSysMail(senduser, email, "事项审批通过", content+"<br>"+ggzy);
				}
			}
		}
		/*}*/
	}
	
	public List<HashMap<String,String>> getOrgUserList(String customerno) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MOBILE,EMAIL FROM ORGUSER ORG INNER JOIN BD_MDM_KHQXGLB QX ON SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1)=USERID WHERE QX.KHBH=?");
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
				String mobile = rs.getString("MOBILE")==null?"":rs.getString("MOBILE").toString();
				String email = rs.getString("EMAIL")==null?"":rs.getString("EMAIL").toString();
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

