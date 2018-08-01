package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.DBUTilNew;
import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
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
public class zqbTempNoticeAfterSaveEvent1 extends ProcessStepTriggerEvent {
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件    
	private static final String TEMP_NOTICE_UUID = "1dfe958166a347188339af1337e25fb7";
	public zqbTempNoticeAfterSaveEvent1(UserContext uc, HashMap hash) {
		super(uc, hash);
		// TODO Auto-generated constructor stub
	}
	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	public List<HashMap> getJdxx(){
		List<HashMap> ls =new ArrayList();
		Long instanceId = this.getInstanceId();
		List list=new ArrayList();
		list.add("ASSIGNEE_");
		list.add("TASK_DEF_KEY_");
		list.add("NAME_");
		ls= DBUTilNew.getDataList(list,"SELECT * FROM (SELECT S.ASSIGNEE_,S.TASK_DEF_KEY_,S.NAME_ FROM PROCESS_HI_TASKINST S WHERE S.PROC_INST_ID_="+instanceId+"  ORDER BY ID_ DESC) WHERE ROWNUM=1",null);
		if(ls!=null && ls.size()>0){
			return ls;
		}else{
			return null;
		}

	}
	public boolean execute() {
		String jdid="";
		String jdmc="";
		String jsr="";
		if(getJdxx()!=null){
			jdid=getJdxx().get(0).get("TASK_DEF_KEY_").toString();
			jdmc=getJdxx().get(0).get("NAME_").toString();
			jsr=getJdxx().get(0).get("ASSIGNEE_").toString();
		}
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
        			hash.put("GGZY", dataMap.get("GGZY")==null?"":dataMap.get("GGZY").toString());
        			hash.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
        			hash.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
					hash.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
					hash.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
        			hash.put("SPZT", "审批中");
        			hash.put("STEPID", this.getActStepId());
        			hash.put("YXID", this.getExcutionId());
        			hash.put("RWID", this.getTaskId());


					hash.put("EXTEND1",jdid);
					hash.put("EXTEND2",jdmc);
					hash.put("EXTEND3",jsr);

        			hash.put("ZPRQ", dataMap.get("ZPRQ")==null?"":dataMap.get("ZPRQ").toString());
					hash.put("XSBLSJ", dataMap.get("XSBLSJ")==null?"":dataMap.get("XSBLSJ").toString());
					hash.put("SFCG", dataMap.get("SFCG")==null?"":dataMap.get("SFCG").toString());
        			flag = DemAPI.getInstance().updateFormData(TEMP_NOTICE_UUID, Long.parseLong(hash.get("INSTANCEID").toString()), hash, Long.parseLong(hash.get("ID").toString()), false);
        			String value=hash.get("NOTICENAME")==null?"":hash.get("NOTICENAME").toString();
        			Long dataId=Long.parseLong(hash.get("ID").toString());
        			LogUtil.getInstance().addLog(dataId, "公告呈报管理", "提交公告："+value);
            	}
            }else{
            	Long instanceid = DemAPI.getInstance().newInstance(TEMP_NOTICE_UUID, uc.get_userModel().getUserid());
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

				hashdata.put("EXTEND1",jdid);
				hashdata.put("EXTEND2",jdmc);
				hashdata.put("EXTEND3",jsr);

    			hashdata.put("ZPRQ", dataMap.get("ZPRQ")==null?"":dataMap.get("ZPRQ").toString());
				hashdata.put("XSBLSJ", dataMap.get("XSBLSJ")==null?"":dataMap.get("XSBLSJ").toString());
				hashdata.put("SFCG", dataMap.get("SFCG")==null?"":dataMap.get("SFCG").toString());
            	// 保存到临时公告数据表中
            	flag = DemAPI.getInstance().saveFormData(TEMP_NOTICE_UUID, instanceid, hashdata, false);
            	HashMap map=new HashMap();
    			map.put("NOTICESQ", sq);
    			HashMap hashMap = DemAPI.getInstance().getList(TEMP_NOTICE_UUID, map, null).get(0);
    			String value=hashMap.get("NOTICENAME")==null?"":hashMap.get("NOTICENAME").toString();
    			Long dataId=Long.parseLong(hashMap.get("ID").toString());
    			LogUtil.getInstance().addLog(dataId, "公告呈报管理", "添加公告："+value);
			}
			String smsContent = "";
			String mailContent = "";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("KHMC",dataMap.get("KHMC") == null ? "" : dataMap.get("KHMC"));
			//smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.ANN_ADD_KEY, map);
			//获取当前日期时间
			SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");//设置日期格式
			String  nowdatetime = df.format(new Date());
			String zqjc=dataMap.get("ZQJCXS")==null?dataMap.get("KHMC").toString():dataMap.get("ZQJCXS").toString();
			//拼接短信
			mailContent = zqjc+nowdatetime+ "提交了："+dataMap.get("NOTICENAME").toString()+"，请审核";
			String TJGGSMS = ConfigUtil.readAllProperties("/common.properties").get("TJGGSMS");
			//xlj 2017年3月9日15:08:42 判断下一节点是否是提交发布版文件，是则短信发送改为完成审核。
			Task a = ProcessAPI.getInstance().newTaskId(instanceId);			
			if((SystemConfig._ggsplcConf.getJd8().equals(a.getTaskDefinitionKey()))){				
				mailContent = dataMap.get("NOTICENAME").toString() + "，已审批通过，请提交发布版文件。";
				TJGGSMS="1";
			}
			String customerno = "";
			if (dataMap.get("KHBH") != null) {
				customerno = dataMap.get("KHBH").toString();
			}
			String assignee = ProcessAPI.getInstance().newTaskId(instanceId).getAssignee();
			String username = "";
			String noticename = (dataMap.get("NOTICENAME").toString()==null||dataMap.get("NOTICENAME").equals(""))?"公告标题：空。":"公告标题："+dataMap.get("NOTICENAME").toString()+"。";
			String ggzy = (dataMap.get("GGZY").toString()==null||dataMap.get("GGZY").equals(""))?"公告摘要：空。":"公告摘要："+dataMap.get("GGZY").toString()+"。";
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
    						MessageAPI.getInstance().sendSysMail(username, email, "公告审批提醒", mailContent+"<br>"+ggzy);
    					}
    				}
				//}
			}
		}
		return flag;
	}
}
