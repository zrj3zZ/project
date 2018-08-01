package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbGGSPAfterSaveEvent extends ProcessTriggerEvent {
	public zqbGGSPAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

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
				List<HashMap> list = DemAPI.getInstance().getList(GG_UUID,hashmap, null);
				if (!list.isEmpty()) {
					HashMap map=list.get(0);//原来数据
					map.put("SPZT", "审批通过");
					map.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
					map.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
					map.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
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
					map.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
					map.put("GGZY", dataMap.get("GGZY")==null?"":dataMap.get("GGZY").toString());
					Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
					Long dataid=Long.parseLong(map.get("ID").toString());
					flag=DemAPI.getInstance().updateFormData(GG_UUID, instanceid, map, dataid, true);
					String value=map.get("NOTICENAME")==null?"":map.get("NOTICENAME").toString();
					Long dataId=Long.parseLong(map.get("ID").toString());
					LogUtil.getInstance().addLog(dataId, "公告呈报管理", "提交公告："+value);
				}else if(list.isEmpty()){//归档时没有数据，则保存一条
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
			//xlj update 2017年3月9日15:24:35 归档时进行短信提醒			
			Task a = ProcessAPI.getInstance().getTask(this.getTaskId().toString());
			String thisStepid = a.getTaskDefinitionKey();
			String actDefId=this.getActDefId();			
			String content = dataMap.get("KHMC").toString() + "的公告：" + dataMap.get("NOTICENAME").toString() + "，已审批通过";;
			if(actDefId.equals(ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC"))){
				//持续督导发起流程，提交发布版文件归档,不给董秘发短信；公告发布人归档，通知董秘
				String jd7 = SystemConfig._ggsplcConf.getJd7();//提交发布版文件
				String jd8 = SystemConfig._ggsplcConf.getJd8();
				if(thisStepid.equals(jd8))//公告发布人节点触发归档操作
				{
					content = dataMap.get("NOTICENAME").toString() + "，已通过审批。";					
				}
				else if(thisStepid.equals(jd7))
				{
					content="";
				}
			}
			else
			{
				//董秘发起流程，提交发布版文件归档,不给董秘发短信；公告发布人归档，通知董秘
				String jd8 = SystemConfig._ggsplcConf.getJd8();//提交发布版文件
				String jd9 = SystemConfig._ggsplcConf.getJd9();//公告发布人节点
				if(thisStepid.equals(jd9))//公告发布人节点触发归档操作
				{
					content = dataMap.get("NOTICENAME").toString() + "，已通过审批。";
				}
				else if(thisStepid.equals(jd8))
				{
					content="";
				}
			}
			if(!content.equals(""))
			{
				senSMS(content,dataMap);
			}
		}

		return flag;
	}
	
	public void senSMS(String content,HashMap dataMap) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (dataMap != null) {
				UserContext target = UserContextUtil.getInstance().getUserContext(dataMap.get("QCRID").toString());
				UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
				String ggzy = (dataMap.get("GGZY").toString()==null||dataMap.get("GGZY").equals(""))?"公告摘要：空。":"公告摘要："+dataMap.get("GGZY").toString()+"。";
				if (target != null) {
					if (!content.equals("")) {
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
							MessageAPI.getInstance().sendSMS(uc, mobile, content);
						}
						String email = target.get_userModel().getEmail();
						if (email != null && !email.equals("") && ut != null) {
							String senduser = ut.get_userModel().getUsername();
							 MessageAPI.getInstance().sendSysMail(senduser, email, "公告发布提醒", content+"<br>"+ggzy);
						}
					}
				}
		}
	}
}
