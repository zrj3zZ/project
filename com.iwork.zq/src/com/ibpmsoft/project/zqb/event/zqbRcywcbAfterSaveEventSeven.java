package com.ibpmsoft.project.zqb.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 董秘发起流程保存后触发
 * @author zouyalei
 *
 */
public class zqbRcywcbAfterSaveEventSeven extends ProcessStepTriggerEvent {

	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件     
	public zqbRcywcbAfterSaveEventSeven(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}

	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	
	public boolean execute() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			//判断是否存在，已经存在，不存在保存，存在更新
			HashMap conditionMap=new HashMap();
			String sq = dataMap.get("NOTICESQ").toString();
			conditionMap.put("NOTICESQ", sq);
			List<HashMap> lcbsexists=DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"), conditionMap, null);
			if(lcbsexists!=null&&lcbsexists.size()==1){
					for(HashMap<String,Object> hash:lcbsexists){
						hash.put("STEPID", dataMap.get("STEPID")==null?"":dataMap.get("STEPID").toString());
						hash.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC").toString());
						hash.put("CREATENAME", dataMap.get("CREATENAME")==null?"":dataMap.get("CREATENAME").toString());
						hash.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER").toString());
						hash.put("KHBH", dataMap.get("KHBH")==null?"":dataMap.get("KHBH").toString());
						hash.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE").toString());
						hash.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
						hash.put("SXLX", dataMap.get("SXLX")==null?"":dataMap.get("SXLX").toString());
						hash.put("EXTEND1", dataMap.get("EXTEND1")==null?"":dataMap.get("EXTEND1").toString());
						hash.put("EXTEND3", dataMap.get("EXTEND3")==null?"":dataMap.get("EXTEND3").toString());
						hash.put("QCRID", dataMap.get("QCRID")==null?"":dataMap.get("QCRID").toString());
						hash.put("EXTEND2", dataMap.get("EXTEND2")==null?"":dataMap.get("EXTEND2").toString());
						hash.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
						hash.put("SXGY", dataMap.get("SXGY")==null?"":dataMap.get("SXGY").toString());
						hash.put("SXMC", dataMap.get("SXMC")==null?"":dataMap.get("SXMC").toString());
						hash.put("SXFJ", dataMap.get("SXFJ")==null?"":dataMap.get("SXFJ").toString());
						hash.put("NOTICESQ", sq);
						hash.put("SPZT", "审批中");
						hash.put("LCBS", instanceId);
						hash.put("LCBH", this.getActDefId());
						hash.put("STEPID", this.getActStepId());
						hash.put("YXID", this.getExcutionId());
						hash.put("TASKID", this.getTaskId());
						hash.put("PRCID", this.getProDefId());
						flag = DemAPI.getInstance().updateFormData(this.getConfigUUID("rcywcbuuid"), Long.parseLong(hash.get("INSTANCEID").toString()), hash, Long.parseLong(hash.get("ID").toString()), false);
						String value=hash.get("SXMC")==null?"":hash.get("SXMC").toString();
						Long dataId=Long.parseLong(hash.get("ID").toString());
						LogUtil.getInstance().addLog(dataId, "日常业务呈报管理", "提交事项："+value);
					}
				}else{
					// 保存到临时公告数据表中
					String userid = uc.get_userModel().getUserid();
					Long instanceid = DemAPI.getInstance().newInstance(this.getConfigUUID("rcywcbuuid"), userid);
					HashMap<String,Object> hashdata = new HashMap<String,Object>();
					hashdata.put("STEPID", dataMap.get("STEPID")==null?"":dataMap.get("STEPID").toString());
					hashdata.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC").toString());
					hashdata.put("CREATENAME", dataMap.get("CREATENAME")==null?"":dataMap.get("CREATENAME").toString());
					hashdata.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER").toString());
					hashdata.put("KHBH", dataMap.get("KHBH")==null?"":dataMap.get("KHBH").toString());
					hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE").toString());
					hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
					hashdata.put("SXLX", dataMap.get("SXLX")==null?"":dataMap.get("SXLX").toString());
					hashdata.put("EXTEND1", dataMap.get("EXTEND1")==null?"":dataMap.get("EXTEND1").toString());
					hashdata.put("EXTEND3", dataMap.get("EXTEND3")==null?"":dataMap.get("EXTEND3").toString());
					hashdata.put("QCRID", dataMap.get("QCRID")==null?"":dataMap.get("QCRID").toString());
					hashdata.put("EXTEND2", dataMap.get("EXTEND2")==null?"":dataMap.get("EXTEND2").toString());
					hashdata.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
					hashdata.put("SXMC", dataMap.get("SXMC")==null?"":dataMap.get("SXMC").toString());
					hashdata.put("SXGY", dataMap.get("SXGY")==null?"":dataMap.get("SXGY").toString());
					hashdata.put("SXFJ", dataMap.get("SXFJ")==null?"":dataMap.get("SXFJ").toString());
					hashdata.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
					hashdata.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
					hashdata.put("LCBS", instanceId);
					hashdata.put("GGZY", dataMap.get("GGZY")==null?"":dataMap.get("GGZY").toString());
					hashdata.put("LCBH", this.getActDefId());
					hashdata.put("YXID", this.getExcutionId());
					hashdata.put("TASKID", this.getTaskId());
					hashdata.put("SPZT", "审批中");
					hashdata.put("PRCID", this.getProDefId());
					hashdata.put("STEPID", this.getActStepId());
					hashdata.put("NOTICESQ", dataMap.get("NOTICESQ")==null?"":dataMap.get("NOTICESQ").toString());
					hashdata.put("QCRID", userid);
					flag = DemAPI.getInstance().saveFormData(this.getConfigUUID("rcywcbuuid"), instanceid, hashdata, false);
					HashMap map=new HashMap();
					map.put("NOTICESQ", sq);
					HashMap hashMap = DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"), map, null).get(0);
					String value=hashMap.get("SXMC")==null?"":hashMap.get("SXMC").toString();
					Long dataId=Long.parseLong(hashMap.get("ID").toString());
					LogUtil.getInstance().addLog(dataId, "日常业务呈报管理", "添加事项："+value);
				}
			}
					String smsContent = "";
					String mailContent = "";
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("KHMC",dataMap.get("KHMC") == null ? "" : dataMap.get("KHMC"));
					//smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.ANN_ADD_KEY, map);
					//获取当前日期时间
					SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");//设置日期格式
					String  nowdatetime = df.format(new Date());
					//编辑短信内容
					mailContent = dataMap.get("KHMC").toString()+nowdatetime+ "提交了："+dataMap.get("SXMC").toString()+"的事项，请审核！";
					String TJGGSMS = ConfigUtil.readAllProperties("/common.properties").get("TJGGSMS");
					String customerno = "";
					if (dataMap.get("KHBH") != null) {
						customerno = dataMap.get("KHBH").toString();
					}
					/*String useraddress2 = ZQBNoticeUtil.getInstance().getTouHangCustomer(customerno);
					String userid = UserContextUtil.getInstance().getUserId(useraddress2);*/
					String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
					String username = "";
					String noticename = (dataMap.get("SXMC").toString()==null||dataMap.get("SXMC").equals(""))?"事项名称：空。":"事项名称："+dataMap.get("SXMC").toString()+"。";
					String ggzy = (dataMap.get("SXGY").toString()==null||dataMap.get("SXGY").equals(""))?"事项摘要：空。":"事项摘要："+dataMap.get("SXGY").toString()+"。";
					UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
					if (target != null) {
						//if (!smsContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("") && TJGGSMS.equals("1")) {
								mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
								MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
							}
							String email = target.get_userModel().getEmail();
							if(email!=null&&!email.equals("") && uc != null){
								username = uc.get_userModel().getUsername();
								String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
		    					if(!zqserver.equals("xnzq")){
		    						MessageAPI.getInstance().sendSysMail(username, email, "事项审批提醒", mailContent+"<br>"+ggzy);
		    					}
		    				}
						//}
					}
				
		return flag;
	}

}
