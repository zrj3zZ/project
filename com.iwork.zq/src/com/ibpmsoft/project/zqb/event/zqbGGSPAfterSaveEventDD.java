package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class zqbGGSPAfterSaveEventDD extends ProcessTriggerEvent {
	public zqbGGSPAfterSaveEventDD(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	private static Logger logger = Logger.getLogger(zqbGGSPAfterSaveEventDD.class);
	private static final String GG_UUID = "1dfe958166a347188339af1337e25fb7";

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
				Long instanceid = DemAPI.getInstance().newInstance(GG_UUID, uc.get_userModel().getUserid());
				HashMap<String,Object> hashdata = new HashMap<String,Object>();
				hashdata.put("MTYDKJ", dataMap.get("MTYDKJ")==null?"":dataMap.get("MTYDKJ").toString());
				hashdata.put("COMPANYNO", dataMap.get("COMPANYNO")==null?"":dataMap.get("COMPANYNO").toString());
				hashdata.put("COMPANYNAME", dataMap.get("COMPANYNAME")==null?"":dataMap.get("COMPANYNAME").toString());
				hashdata.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
				hashdata.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
				hashdata.put("NOTICEFLAG", dataMap.get("NOTICEFLAG")==null?"":dataMap.get("NOTICEFLAG").toString());
				hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
				hashdata.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
				hashdata.put("PAPERFILENO", dataMap.get("PAPERFILENO")==null?"":dataMap.get("PAPERFILENO").toString());
				hashdata.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
				hashdata.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
				hashdata.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
				hashdata.put("MEETID", dataMap.get("MEETID")==null?"":dataMap.get("MEETID").toString());
				hashdata.put("MEETNAME", dataMap.get("MEETNAME")==null?"":dataMap.get("MEETNAME").toString());
				hashdata.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC").toString());
				hashdata.put("KHBH", dataMap.get("KHBH")==null?"":dataMap.get("KHBH").toString());
				hashdata.put("CREATERID", dataMap.get("CREATERID")==null?"":dataMap.get("CREATERID").toString());
				hashdata.put("CREATENAME", dataMap.get("CREATENAME")==null?"":dataMap.get("CREATENAME").toString());
				hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE").toString());
				hashdata.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
				hashdata.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
				hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
				hashdata.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
				hashdata.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
				hashdata.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
				hashdata.put("NOTICESQ", dataMap.get("NOTICESQ")==null?"":dataMap.get("NOTICESQ").toString());
				hashdata.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
				hashdata.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
				hashdata.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
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
				hashdata.put("RWID", this.getTaskId());
				hashdata.put("LCBS", instanceId);
				hashdata.put("LCBH", actDefId);
				// 保存到临时公告数据表中
				flag = DemAPI.getInstance().saveFormData(GG_UUID, instanceid, hashdata, false);
				HashMap map=new HashMap();
				map.put("NOTICESQ", sq);
				HashMap hashMap = DemAPI.getInstance().getList(GG_UUID, map, null).get(0);
				String value=hashMap.get("NOTICENAME")==null?"":hashMap.get("NOTICENAME").toString();
				Long dataId=Long.parseLong(hashMap.get("ID").toString());
				LogUtil.getInstance().addLog(dataId, "公告呈报管理", "添加公告："+value);
			}else{
				hashmap.put("NOTICESQ", sq);
				List<HashMap> list = DemAPI.getInstance().getList(GG_UUID,
						hashmap, null);
				if (list != null && list.size() == 1) {
					HashMap map=list.get(0);//原来数据
					map.put("SPZT", "审批通过");
					map.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
					map.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
					map.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
					map.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
					map.put("COMPANYNO", dataMap.get("COMPANYNO")==null?"":dataMap.get("COMPANYNO").toString());
					map.put("PAPERFILENO", dataMap.get("PAPERFILENO")==null?"":dataMap.get("PAPERFILENO").toString());
					map.put("MEETID", dataMap.get("MEETID")==null?"":dataMap.get("MEETID").toString());
					map.put("MEETNAME", dataMap.get("MEETNAME")==null?"":dataMap.get("MEETNAME").toString());        		
					map.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
					map.put("COMPANYNAME", dataMap.get("COMPANYNAME")==null?"":dataMap.get("COMPANYNAME").toString());
	        		map.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
	        		map.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
	        		map.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
	        		map.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
	    			map.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
					map.put("GGZY", dataMap.get("GGZY")==null?"":dataMap.get("GGZY").toString());
					Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
					Long dataid=Long.parseLong(map.get("ID").toString());
					flag=DemAPI.getInstance().updateFormData(GG_UUID, instanceid, map, dataid, true);
					String value=map.get("NOTICENAME")==null?"":map.get("NOTICENAME").toString();
					Long dataId=Long.parseLong(map.get("ID").toString());
					LogUtil.getInstance().addLog(dataId, "公告呈报管理", "提交公告："+value);
				}else if(list==null||list.size()==0){//归档时没有数据，则保存一条
					Long instanceid = DemAPI.getInstance().newInstance(GG_UUID, uc.get_userModel().getUserid());
					HashMap<String,Object> hashdata = new HashMap<String,Object>();
					hashdata.put("MTYDKJ", dataMap.get("MTYDKJ")==null?"":dataMap.get("MTYDKJ").toString());
					hashdata.put("COMPANYNO", dataMap.get("COMPANYNO")==null?"":dataMap.get("COMPANYNO").toString());
					hashdata.put("COMPANYNAME", dataMap.get("COMPANYNAME")==null?"":dataMap.get("COMPANYNAME").toString());
					hashdata.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
					hashdata.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
					hashdata.put("NOTICEFLAG", dataMap.get("NOTICEFLAG")==null?"":dataMap.get("NOTICEFLAG").toString());
					hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
					hashdata.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
					hashdata.put("PAPERFILENO", dataMap.get("PAPERFILENO")==null?"":dataMap.get("PAPERFILENO").toString());
					hashdata.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
					hashdata.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
					hashdata.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
					hashdata.put("MEETID", dataMap.get("MEETID")==null?"":dataMap.get("MEETID").toString());
					hashdata.put("MEETNAME", dataMap.get("MEETNAME")==null?"":dataMap.get("MEETNAME").toString());
					hashdata.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC").toString());
					hashdata.put("KHBH", dataMap.get("KHBH")==null?"":dataMap.get("KHBH").toString());
					hashdata.put("CREATERID", dataMap.get("CREATERID")==null?"":dataMap.get("CREATERID").toString());
					hashdata.put("CREATENAME", dataMap.get("CREATENAME")==null?"":dataMap.get("CREATENAME").toString());
					hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE").toString());
					hashdata.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
					hashdata.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
					hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
					hashdata.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
					hashdata.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
					hashdata.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
					hashdata.put("NOTICESQ", dataMap.get("NOTICESQ")==null?"":dataMap.get("NOTICESQ").toString());
					hashdata.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
					hashdata.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
					hashdata.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
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
					hashdata.put("RWID", this.getTaskId());
					hashdata.put("LCBS", instanceId);
					hashdata.put("LCBH", actDefId);
					// 保存到临时公告数据表中
					flag = DemAPI.getInstance().saveFormData(GG_UUID, instanceid, hashdata, false);
					HashMap map=new HashMap();
					map.put("NOTICESQ", sq);
					HashMap hashMap = DemAPI.getInstance().getList(GG_UUID, map, null).get(0);
					String value=hashMap.get("NOTICENAME")==null?"":hashMap.get("NOTICENAME").toString();
					Long dataId=Long.parseLong(hashMap.get("ID").toString());
					LogUtil.getInstance().addLog(dataId, "公告呈报管理", "添加公告："+value);
				}
			}
			senSMS(dataMap,customerno);
		}
		return flag;
	}
	public void senSMS(HashMap dataMap,String customerno) {
		String senduser="";
		String content="";
		String ggzy = (dataMap.get("GGZY").toString()==null||dataMap.get("GGZY").equals(""))?"公告摘要：空。":"公告摘要："+dataMap.get("GGZY").toString()+"。";
		List<HashMap<String,String>> orgUserList = getOrgUserList(customerno);
		UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
		for (HashMap<String, String> hashMap : orgUserList) {
			String mobile = hashMap.get("MOBILE").toString();
			String email = hashMap.get("EMAIL").toString();
			content = dataMap.get("KHMC").toString() + "的公告：" + dataMap.get("NOTICENAME").toString() + "，已审批通过";
			if (mobile != null && !mobile.equals("")) {
				mobile = hashMap.get("MOBILE").toString()+"["+hashMap.get("USERNAME").toString()+"]";
				MessageAPI.getInstance().sendSMS(mobile, content);
			}
			if (!content.equals("")) {
				if(email != null && !email.equals("")){
					senduser = ut.get_userModel().getUsername();
					MessageAPI.getInstance().sendSysMail(senduser, email, "公告审批通过", content+"<br>"+ggzy);
				}
			}
		}
	}
	
	public List<HashMap<String,String>> getOrgUserList(String customerno) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MOBILE,EMAIL,USERNAME FROM ORGUSER ORG INNER JOIN BD_MDM_KHQXGLB QX ON SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1)=USERID WHERE QX.KHBH=?");
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
				String username = rs.getString("USERNAME")==null?"":rs.getString("USERNAME").toString();
				map.put("MOBILE", mobile);
				map.put("EMAIL", email);
				map.put("USERNAME", username);
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
