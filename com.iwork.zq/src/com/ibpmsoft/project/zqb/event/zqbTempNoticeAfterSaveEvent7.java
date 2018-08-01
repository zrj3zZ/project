package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;

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
public class zqbTempNoticeAfterSaveEvent7 extends ProcessStepTriggerEvent {
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件    
	private static final String TEMP_NOTICE_UUID = "1dfe958166a347188339af1337e25fb7";
	public zqbTempNoticeAfterSaveEvent7(UserContext uc, HashMap hash) {
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
		String sq = dataMap.get("NOTICESQ").toString();
		if(dataMap!=null){
			//判断是否存在，已经存在，不存在保存，存在更新
			HashMap conditionMap=new HashMap();
			conditionMap.put("NOTICESQ", sq);
			List<HashMap> lcbsexists=DemAPI.getInstance().getList(TEMP_NOTICE_UUID, conditionMap, null);
            if(lcbsexists!=null&&lcbsexists.size()==1){
            	for(HashMap<String,Object> hash:lcbsexists){
            		hash.put("MEETID", dataMap.get("MEETID")==null?"":dataMap.get("MEETID").toString());
            		hash.put("MEETNAME", dataMap.get("MEETNAME")==null?"":dataMap.get("MEETNAME").toString());
            		hash.put("COMPANYNO", dataMap.get("COMPANYNO")==null?"":dataMap.get("COMPANYNO").toString());
            		hash.put("PAPERFILENO", dataMap.get("PAPERFILENO")==null?"":dataMap.get("PAPERFILENO").toString());
            		hash.put("COMPANYNAME", dataMap.get("COMPANYNAME")==null?"":dataMap.get("COMPANYNAME").toString());
            		hash.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
        			hash.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
        			hash.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
        			hash.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
        			hash.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
        			hash.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
        			hash.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
        			hash.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
					hash.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
					hash.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
        			hash.put("GGZY", dataMap.get("GGZY")==null?"":dataMap.get("GGZY").toString());
        			hash.put("SPZT", "审批中");
        			hash.put("STEPID", this.getActStepId());
        			hash.put("YXID", this.getExcutionId());
        			hash.put("NOTICESQ", sq);
        			hash.put("ZPRQ", dataMap.get("ZPRQ")==null?"":dataMap.get("ZPRQ").toString());
					hash.put("XSBLSJ", dataMap.get("XSBLSJ")==null?"":dataMap.get("XSBLSJ").toString());
					hash.put("SFCG", dataMap.get("SFCG")==null?"":dataMap.get("SFCG").toString());
        			hash.put("RWID", this.getTaskId());
        			flag = DemAPI.getInstance().updateFormData(TEMP_NOTICE_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), hash, Long.parseLong(hash.get("ID").toString()), false);
        			String value=hash.get("NOTICENAME")==null?"":hash.get("NOTICENAME").toString();
        			Long dataId=Long.parseLong(hash.get("ID").toString());
        			LogUtil.getInstance().addLog(dataId, "公告呈报管理", "提交公告："+value);
            	}
            }else{
            	Long instanceid = DemAPI.getInstance().newInstance(TEMP_NOTICE_UUID, uc.get_userModel().getUserid());
    			HashMap<String,Object> hashdata = new HashMap<String,Object>();
    			hashdata.put("ZPRQ", dataMap.get("ZPRQ")==null?"":dataMap.get("ZPRQ").toString());
				hashdata.put("XSBLSJ", dataMap.get("XSBLSJ")==null?"":dataMap.get("XSBLSJ").toString());
				hashdata.put("SFCG", dataMap.get("SFCG")==null?"":dataMap.get("SFCG").toString());
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
    			hashdata.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
    			hashdata.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
				hashdata.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
    			hashdata.put("LCBS", instanceId);
    			hashdata.put("GGZY", dataMap.get("GGZY")==null?"":dataMap.get("GGZY").toString());
    			hashdata.put("LCBH", this.getActDefId());
    			hashdata.put("YXID", this.getExcutionId());
    			hashdata.put("RWID", this.getTaskId());
    			hashdata.put("SPZT", "审批中");
    			hashdata.put("PRCID", this.getProDefId());
    			hashdata.put("STEPID", this.getActStepId());
    			hashdata.put("NOTICESQ", sq);
            	// 保存到临时公告数据表中
            	flag = DemAPI.getInstance().saveFormData(TEMP_NOTICE_UUID, instanceid, hashdata, false);
            	HashMap map=new HashMap();
    			map.put("NOTICESQ", sq);
    			HashMap hashMap = DemAPI.getInstance().getList(TEMP_NOTICE_UUID, map, null).get(0);
    			String value=hashMap.get("NOTICENAME")==null?"":hashMap.get("NOTICENAME").toString();
    			Long dataId=Long.parseLong(hashMap.get("ID").toString());
    			LogUtil.getInstance().addLog(dataId, "公告呈报管理", "添加公告："+value);
            }
           // String smsContent = "";
            String mailContent = "";
        		HashMap<String, Object> map=new HashMap<String, Object>();
        		map.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC"));
        		//smsContent = dataMap.get("NOTICENAME").toString()+"已通过审批，请提交发布版文件。";
        		mailContent = dataMap.get("NOTICENAME").toString()+"已通过审批，请提交发布版文件。";
        		String username = "";
    			String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
        		UserContext target = UserContextUtil.getInstance().getUserContext(assignee); 
        		if(target!=null){
        			//if(!smsContent.equals("")){
        				String mobile = target.get_userModel().getMobile();
        				if(mobile!=null&&!mobile.equals("")){
        					mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
        					MessageAPI.getInstance().sendSMS(uc, mobile, mailContent);
        				}
        				String email = target.get_userModel().getEmail();
        				if(email!=null&&!email.equals("") && uc != null){
        					username = uc.get_userModel().getUsername();
        					String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
	    					if(!zqserver.equals("xnzq")){
	    						MessageAPI.getInstance().sendSysMail(username, email, "公告审批提醒", mailContent);
	    					}
	    				}
        			//}
        		}
		}
		return flag;
	}

}
