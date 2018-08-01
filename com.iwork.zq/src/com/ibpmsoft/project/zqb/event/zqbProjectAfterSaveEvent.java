package com.ibpmsoft.project.zqb.event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.PinYinUtil;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.OrganizationAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class zqbProjectAfterSaveEvent extends ProcessStepTriggerEvent {
	private static Logger logger = Logger.getLogger(zqbProjectAfterSaveEvent.class);
	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";
	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";
	private static OrgUserService orgUserService;
	private static OrgDepartmentDAO orgDepartmentDAO;
	private static OrgCompanyDAO orgCompanyDAO;
	
	static final char SBC_CHAR_START = 65281; // 全角！ 
	static final char SBC_CHAR_END = 65374; // 全角～
	static final int CONVERT_STEP = 65248; // 全角半角转换间隔
	static final char SBC_SPACE = 12288; // 全角空格 12288
	static final char DBC_SPACE = ' '; // 半角空格

	public zqbProjectAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		return newMethod();
	}
	
	public boolean newMethod(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			Long lcTaskId = this.getTaskId();
			String actDefId = this.getActDefId();
			String actStepId = this.getActStepId();
			String projectNo = dataMap.get("PROJECTNO").toString();
			String projectName = dataMap.get("PROJECTNAME").toString();
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser getUserModel = uc.get_userModel();
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap<String,String> conditionMap = new HashMap<String,String>();
			conditionMap.put("PROJECTNO", projectNo);
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(PROJECT_UUID, conditionMap, null);
			String smsContent = "";
			String sysMsgContent = "";
			String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			Long dataInstanceid=0L;
			if (lcbsexists != null && lcbsexists.size() == 1) {
				for (HashMap<String, Object> hash : lcbsexists) {
					dataInstanceid=Long.parseLong(hash.get("INSTANCEID").toString());
					Long dataId=Long.parseLong(hash.get("ID").toString());
					Set<String> keySet = dataMap.keySet();
					for (String key : keySet) {
						if(key.equals("CREATEUSER")||key.equals("CREATEDATE")||key.equals("CREATEUSERID")||key.equals("A02")||key.equals("A03")){
							continue;
						}
						if(key.equals("WBCLRY")){
							hash.put("WBCLRJG", dataMap.get(key) == null ? "": dataMap.get(key).toString());
						}else if(!key.equals("ID")){
							hash.put(key, dataMap.get(key) == null ? "": dataMap.get(key).toString());
						}
					}
					hash.put("LCBH", actDefId);
					hash.put("LCBS", instanceId);
					hash.put("STEPID", actStepId);
					hash.put("TASKID", lcTaskId==0?instanceId+7:lcTaskId);
					flag = DemAPI.getInstance().updateFormData(PROJECT_UUID,dataInstanceid,	hash, dataId,false);
				}
			} else {
				Long instanceid = DemAPI.getInstance().newInstance(PROJECT_UUID, getUserModel.getUserid());
				HashMap<String, Object> hashdata = new HashMap<String, Object>();
				Set<String> keySet = dataMap.keySet();
				for (String key : keySet) {
					if(key.equals("CREATEUSER")||key.equals("CREATEDATE")||key.equals("CREATEUSERID")||key.equals("A02")||key.equals("A03")){
						continue;
					}
					if(key.equals("WBCLRY")){
						hashdata.put("WBCLRJG", dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}else if(!key.equals("ID")){
						hashdata.put(key, dataMap.get(key) == null ? "": dataMap.get(key).toString());
					}
				}
				hashdata.put("LCBH", actDefId);
				hashdata.put("LCBS", instanceId);
				hashdata.put("STEPID", actStepId);
				hashdata.put("TASKID", lcTaskId==0?instanceId+7:lcTaskId);
				// 保存到项目任务数据表中
				hashdata.put("USERID", getUserModel.getUserid());
				flag = DemAPI.getInstance().saveFormData(PROJECT_UUID,instanceid, hashdata, false);
				if(flag){
					smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.PROJECT_BASE_ADD_KEY, hashdata);
					sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.PROJECT_BASE_ADD_KEY, hashdata);
					if (target != null) {
						if (!smsContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
								MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
							}
						}
						if (!sysMsgContent.equals("")) {
							MessageAPI.getInstance().sendSysMsg(userid, "项目基本信息维护提醒",
									sysMsgContent);
						}
					}
				}
				//添加项目时预置两条 分配部门数据-----start
				String username = getUserModel.getUsername();
				String tbsj = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				//投行质控部//场外市场部
				List<HashMap> data=new ArrayList<HashMap>();
				HashMap<String,Object> datamap1 = new HashMap<String,Object>();
				datamap1.put("JGMC", "场外市场部");
				datamap1.put("FPBL", "0.00");
				datamap1.put("TBR", username);
				datamap1.put("TBSJ", tbsj);
				HashMap<String,Object> datamap2 = new HashMap<String,Object>();
				datamap2.put("JGMC", "投行质控部");
				datamap2.put("FPBL", "0.00");
				datamap2.put("TBR", username);
				datamap2.put("TBSJ", tbsj);
				data.add(datamap1);
				data.add(datamap2);
				ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, "SUBFORM_CLJG", data, false);
				//添加项目时预置两条 分配部门数据------end
			}
			//根据主表中的项目阶段列判断是否需要新增项目阶段
			HashMap<String,Object> taskMap=new HashMap<String,Object>();
			taskMap.put("PROJECTNO", projectNo);
			String xmjd=dataMap.get("XMJD").toString();
			Long xmjdID = DBUtil.getLong("SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='"+xmjd+"'", "ID");
			taskMap.put("GROUPID", xmjdID);
			List<HashMap> list = DemAPI.getInstance().getList(PROJECT_TASK_UUID, taskMap, null);
			if(list.isEmpty()){
				Long taskinstanceid = DemAPI.getInstance().newInstance(PROJECT_TASK_UUID, getUserModel.getUserid());
				HashMap<String,Object> map = new HashMap<String,Object>();
				String nowDatetime = UtilDate.getNowDatetime();
				String manager = "";
				manager = getUserModel.getUserid() + "[" + getUserModel.getUsername() + "]";
				map.put("MANAGER",manager);
				map.put("GROUPID", xmjdID);
				map.put("PROJECTNO", projectNo);
				map.put("PROJECTNAME", projectName);
				map.put("SPZT","已提交");
				map.put("GXSJ",nowDatetime);
				flag = DemAPI.getInstance().saveFormData(PROJECT_TASK_UUID,taskinstanceid, map, false);
			}
			//获取流程数据子表并向物理数据子表中插入数据
			List<HashMap> lcFromSubXmcyData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
			/*List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
			List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");*/
			List<HashMap> lcFromSubZjjgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");

			Long demId = DemAPI.getInstance().getDemModel(PROJECT_UUID).getId();
			
			List<HashMap> fromSubXmcyData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMCYLB");
			setFromSubData(demId,fromSubXmcyData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMCYLB",lcFromSubXmcyData);
			/*List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLR");
			setFromSubData(demId,fromSubClrData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLR",lcFromSubClrData);
			List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLJG");
			setFromSubData(demId,fromSubCljgData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLJG",lcFromSubCljgData);*/
			List<HashMap> fromSubZjjgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMZJJG");
			setFromSubData(demId,fromSubZjjgData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMZJJG",lcFromSubZjjgData);
		}
		return flag;
	}
	
	public boolean oldMethod(){
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			Long instanceid = DemAPI.getInstance().newInstance(
					PROJECT_UUID, uc.get_userModel().getUserid());

			HashMap<String, Object> hashdata = new HashMap<String, Object>();
			hashdata.put("KHLXDH", dataMap.get("KHLXDH") == null ? ""
					: dataMap.get("KHLXDH").toString());
			hashdata.put("OWNER", dataMap.get("OWNER") == null ? ""
					: dataMap.get("OWNER").toString());
			hashdata.put("MEMO", dataMap.get("MEMO") == null ? "" : dataMap
					.get("MEMO").toString());
			hashdata.put("STARTDATE", dataMap.get("STARTDATE") == null ? ""
					: dataMap.get("STARTDATE").toString());
			hashdata.put("PROJECTNO", dataMap.get("PROJECTNO") == null ? "" : dataMap
					.get("PROJECTNO").toString());
			hashdata.put("XMJD", dataMap.get("XMJD") == null ? ""
					: dataMap.get("XMJD").toString());
			hashdata.put("PROJECTNAME", dataMap.get("PROJECTNAME") == null ? ""
					: dataMap.get("PROJECTNAME").toString());
			hashdata.put("SBJZR", dataMap.get("SBJZR") == null ? ""
					: dataMap.get("SBJZR").toString());
			hashdata.put("KHLXR", dataMap.get("KHLXR") == null ? "" : dataMap
					.get("KHLXR").toString());
			hashdata.put(
					"XMBZ",
					dataMap.get("XMBZ") == null ? "" : dataMap.get("XMBZ"));
			hashdata.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER"));
			hashdata.put("STATUS", dataMap.get("STATUS")==null?"":dataMap.get("STATUS"));
			hashdata.put("WBCLRYJG", dataMap.get("WBCLRYJG")==null?"":dataMap.get("WBCLRYJG"));
			hashdata.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER"));
			hashdata.put("CUSTOMERINFO", dataMap.get("CUSTOMERINFO")==null?"":dataMap.get("CUSTOMERINFO"));
			hashdata.put("FZJGLXR", dataMap.get("FZJGLXR")==null?"":dataMap.get("FZJGLXR"));
			hashdata.put("CUSTOMERNO", dataMap.get("CUSTOMERNO")==null?"":dataMap.get("CUSTOMERNO"));
			hashdata.put("YJZXYNJLR", dataMap.get("YJZXYNJLR")==null?"":dataMap.get("YJZXYNJLR"));
			hashdata.put("QRDG", dataMap.get("QRDG")==null?"":dataMap.get("QRDG"));
			hashdata.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE"));
			hashdata.put("ZCLR", dataMap.get("ZCLR")==null?"":dataMap.get("ZCLR"));
			hashdata.put("SFXZCL", dataMap.get("SFXZCL")==null?"":dataMap.get("SFXZCL"));
			hashdata.put("GGJZR", dataMap.get("GGJZR")==null?"":dataMap.get("GGJZR"));
			hashdata.put("CUSTOMERNAME", dataMap.get("CUSTOMERNAME")==null?"":dataMap.get("CUSTOMERNAME"));
			hashdata.put("FZJGMC", dataMap.get("FZJGMC")==null?"":dataMap.get("FZJGMC"));
			hashdata.put("ZCLRDH", dataMap.get("ZCLRDH")==null?"":dataMap.get("ZCLRDH"));
			hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE"));
			hashdata.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE"));
			hashdata.put("SSSYB", dataMap.get("SSSYB")==null?"":dataMap.get("SSSYB"));
			hashdata.put("XMZRY", dataMap.get("XMZRY")==null?"":dataMap.get("XMZRY"));
			hashdata.put("LCBH", this.getActDefId());
			hashdata.put("LCBS", instanceId);
			hashdata.put("STEPID", this.getActStepId());
			hashdata.put("CLSLR", dataMap.get("CLSLR") == null ? ""
					: dataMap.get("CLSLR").toString());
			hashdata.put("SHTGR", dataMap.get("SHTGR") == null ? ""
					: dataMap.get("SHTGR").toString());
			hashdata.put("SFTXCL", dataMap.get("SFTXCL"));
			hashdata.put("XMCY", dataMap.get("XMCY"));
			hashdata.put("TASKID", this.getTaskId()==0?instanceId+7:this.getTaskId());
			
			//instanceId=99031&excutionId=99031&taskId=99038
			// 判断是否存在，已经存在，不存在保存，存在更新
			HashMap conditionMap = new HashMap();
			conditionMap.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
			List<HashMap> lcbsexists = DemAPI.getInstance().getList(
					PROJECT_UUID, conditionMap, null);
			
			String smsContent = "";
			String sysMsgContent = "";
			String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(
					ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
			UserContext target = UserContextUtil.getInstance().getUserContext(
					userid);
			if (lcbsexists != null && lcbsexists.size() == 1) {
				for (HashMap<String, Object> hash : lcbsexists) {
					hash.put("KHLXDH", dataMap.get("KHLXDH") == null ? ""
							: dataMap.get("KHLXDH").toString());
					hash.put("OWNER", dataMap.get("OWNER") == null ? ""
							: dataMap.get("OWNER").toString());
					hash.put("MEMO", dataMap.get("MEMO") == null ? "" : dataMap
							.get("MEMO").toString());
					hash.put("STARTDATE", dataMap.get("STARTDATE") == null ? ""
							: dataMap.get("STARTDATE").toString());
					hash.put("PROJECTNO", dataMap.get("PROJECTNO") == null ? "" : dataMap
							.get("PROJECTNO").toString());
					hash.put("XMJD", dataMap.get("XMJD") == null ? ""
							: dataMap.get("XMJD").toString());
					hash.put("PROJECTNAME", dataMap.get("PROJECTNAME") == null ? ""
							: dataMap.get("PROJECTNAME").toString());
					hash.put("SBJZR", dataMap.get("SBJZR") == null ? ""
							: dataMap.get("SBJZR").toString());
					hash.put("KHLXR", dataMap.get("KHLXR") == null ? "" : dataMap
							.get("KHLXR").toString());
					hash.put(
							"XMBZ",
							dataMap.get("XMBZ") == null ? "" : dataMap.get("XMBZ"));
					hash.put("CREATEUSER", dataMap.get("CREATEUSER")==null?"":dataMap.get("CREATEUSER"));
					hash.put("STATUS", dataMap.get("STATUS")==null?"":dataMap.get("STATUS"));
					hash.put("WBCLRYJG", dataMap.get("WBCLRYJG")==null?"":dataMap.get("WBCLRYJG"));
					hash.put("MANAGER", dataMap.get("MANAGER")==null?"":dataMap.get("MANAGER"));
					hash.put("CUSTOMERINFO", dataMap.get("CUSTOMERINFO")==null?"":dataMap.get("CUSTOMERINFO"));
					hash.put("FZJGLXR", dataMap.get("FZJGLXR")==null?"":dataMap.get("FZJGLXR"));
					hash.put("CUSTOMERNO", dataMap.get("CUSTOMERNO")==null?"":dataMap.get("CUSTOMERNO"));
					hash.put("YJZXYNJLR", dataMap.get("YJZXYNJLR")==null?"":dataMap.get("YJZXYNJLR"));
					hash.put("QRDG", dataMap.get("QRDG")==null?"":dataMap.get("QRDG"));
					hash.put("ENDDATE", dataMap.get("ENDDATE")==null?"":dataMap.get("ENDDATE"));
					hash.put("ZCLR", dataMap.get("ZCLR")==null?"":dataMap.get("ZCLR"));
					hash.put("SFXZCL", dataMap.get("SFXZCL")==null?"":dataMap.get("SFXZCL"));
					hash.put("GGJZR", dataMap.get("GGJZR")==null?"":dataMap.get("GGJZR"));
					hash.put("CUSTOMERNAME", dataMap.get("CUSTOMERNAME")==null?"":dataMap.get("CUSTOMERNAME"));
					hash.put("FZJGMC", dataMap.get("FZJGMC")==null?"":dataMap.get("FZJGMC"));
					hash.put("ZCLRDH", dataMap.get("ZCLRDH")==null?"":dataMap.get("ZCLRDH"));
					hash.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE"));
					hash.put("HTJE", dataMap.get("HTJE")==null?"":dataMap.get("HTJE"));
					hash.put("SSSYB", dataMap.get("SSSYB")==null?"":dataMap.get("SSSYB"));
					hash.put("XMZRY", dataMap.get("XMZRY")==null?"":dataMap.get("XMZRY"));
					hash.put("LCBH", this.getActDefId());
					hash.put("LCBS", instanceId);
					hash.put("STEPID", this.getActStepId());
					hash.put("SFTXCL", dataMap.get("SFTXCL"));
					hash.put("TASKID", this.getTaskId()==0?instanceId+7:this.getTaskId());
					hash.put("XMCY", dataMap.get("XMCY"));
					hash.put("CLSLR", dataMap.get("CLSLR") == null ? ""
							: dataMap.get("CLSLR").toString());
					hash.put("SHTGR", dataMap.get("SHTGR") == null ? ""
							: dataMap.get("SHTGR").toString());
					flag = DemAPI.getInstance().updateFormData(PROJECT_UUID,
							Long.parseLong(hash.get("INSTANCEID").toString()),
							hash, Long.parseLong(hash.get("ID").toString()),
							false);
				}
			} else {
				// 保存到项目任务数据表中
				hashdata.put("USERID", uc._userModel.getUserid());
				flag = DemAPI.getInstance().saveFormData(PROJECT_UUID,
						instanceid, hashdata, false);
				if(flag){
					smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
							ZQB_Notice_Constants.PROJECT_BASE_ADD_KEY, hashdata);
					sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
							ZQB_Notice_Constants.PROJECT_BASE_ADD_KEY, hashdata);
					if (target != null) {
						if (!smsContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
								MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
							}
						}
						if (!sysMsgContent.equals("")) {
							MessageAPI.getInstance().sendSysMsg(userid, "项目基本信息维护提醒",
									sysMsgContent);
						}
					}
				}
				//添加项目时预置两条 分配部门数据-----start
				String username = uc.get_userModel().getUsername();
				String tbsj = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				//投行质控部//场外市场部
				List<HashMap> data=new ArrayList<HashMap>();
				HashMap datamap1 = new HashMap();
				datamap1.put("JGMC", "场外市场部");
				datamap1.put("FPBL", "0.00");
				datamap1.put("TBR", username);
				datamap1.put("TBSJ", tbsj);
				HashMap datamap2 = new HashMap();
				datamap2.put("JGMC", "投行质控部");
				datamap2.put("FPBL", "0.00");
				datamap2.put("TBR", username);
				datamap2.put("TBSJ", tbsj);
				data.add(datamap1);
				data.add(datamap2);
				ProcessAPI.getInstance().saveFormDatas(this.getActDefId(), instanceId, "SUBFORM_CLJG", data, false);
				//添加项目时预置两条 分配部门数据------end
			}
		}
		HashMap taskMap=new HashMap();
		taskMap.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
		String xmjd=dataMap.get("XMJD").toString();
		Long xmjdID = DBUtil.getLong("SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='"+xmjd+"'", "ID");
		taskMap.put("GROUPID", xmjdID);
		List<HashMap> list = DemAPI.getInstance().getList(PROJECT_TASK_UUID, taskMap, null);
		if(list.isEmpty()){
			Long taskinstanceid = DemAPI.getInstance().newInstance(
					PROJECT_TASK_UUID, uc.get_userModel().getUserid());
			HashMap map = new HashMap();
			String nowDatetime = UtilDate.getNowDatetime();
			String manager = "";
			manager = uc._userModel.getUserid() + "["
						+ uc._userModel.getUsername() + "]";
			map.put("MANAGER",manager);
			map.put("GROUPID", xmjdID);
			map.put("PROJECTNO", dataMap.get("PROJECTNO").toString());
			map.put("PROJECTNAME", dataMap.get("PROJECTNAME").toString());
			map.put("SPZT","已提交");
			map.put("GXSJ",nowDatetime);
			//MANAGER,GROUPID,PROJECTNO,PROJECTNAME,SPZT,GXSJ
			flag = DemAPI.getInstance().saveFormData(PROJECT_TASK_UUID,taskinstanceid, map, false);
		}
		String xmcy = dataMap.get("XMCY").toString();
		if(xmcy!=null&&!"".equals(xmcy)){
			Long processDefaultFormId = ProcessAPI.getInstance().getProcessDefaultFormId(this.getActDefId());
			boolean removeSubFormData = DemAPI.getInstance().removeSubFormData(processDefaultFormId,instanceId, Long.parseLong(dataMap.get("ID").toString()), "SUBFORM_XMCYLB");
			String qj2bj = qj2bj(xmcy);
			String[] split = qj2bj.split(",");
			Set<String> strSet = new HashSet<String>();   
			CollectionUtils.addAll(strSet, split);
			List<HashMap> CYList=new ArrayList<HashMap>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (String name : strSet) {
				if(name==null||"".equals(name.trim())){
					continue;
				}
				String userid = DBUtil.getString("select USERID from orguser where trim(USERNAME)='"+name.trim()+"'", "USERID");
				HashMap map = new HashMap();
				if(userid==null||"".equals(userid)){
					String adduserid ="";
					String upperCase ="";
					String useridResult="";
					upperCase = PinYinUtil.zh_CnToPinyinHeadParser(name).toUpperCase();
					adduserid = DBUtil.getString("select USERID from orguser where trim(USERID)='"+upperCase.trim()+"'", "USERID");
					if(adduserid!=null&&!"".equals(adduserid)){
						int id = DBUtil.getInt("select MAX(id) id from orguser", "id");
						useridResult=upperCase+id;
					}else{
						useridResult=upperCase;
					}
					String sql = "SELECT MAX(id)+1 ID FROM OrgUser ";
					String noStr = null;
					String ll = DBUtil.getString(sql, "ID");
					if (ll == null)
						noStr = "1";
					else {
						noStr = ll.toString();
					}
					if (noStr.length() == 1)
						noStr = "000" + noStr;
					else if (noStr.length() == 2)
						noStr = "00" + noStr;
					else if (noStr.length() == 3)
						noStr = "0" + noStr;
					else {
						noStr = noStr;
					}
					OrgUser user=new OrgUser();
					user.setUserid(useridResult);
					user.setUsername(name);
					user.setDepartmentid((long) 54);
					user.setDepartmentname("场外市场部");
					user.setUsertype(0L);
					user.setUserno(noStr);
					try {
						user.setStartdate(sdf.parse("2014-01-01"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
					try {
						user.setEnddate(sdf.parse("2099-12-31"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
					//执行MD5加密
					/*MD5 md5 = new MD5();
					String md5pwd = md5.getEncryptedPwd(pwd);*/
					//执行SHA-256+SALT加密
					String salt=ShaSaltUtil.getStringSalt();
					String shapwd=ShaSaltUtil.getEncryptedPwd(SystemConfig._iworkServerConf.getUserDefaultPassword(),salt,true);
			    	user.setPassword(shapwd);
			    	user.setExtend3(salt);
			    	user.setUserstate(new Long(0L));
			    	orgDepartmentDAO=(OrgDepartmentDAO) SpringBeanUtil.getBean("orgDepartmentDAO");
					orgCompanyDAO=(OrgCompanyDAO) SpringBeanUtil.getBean("orgCompanyDAO");
			    	OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(user.getDepartmentid());
			    	user.setCompanyid(Long.valueOf(Long.parseLong(orgDepartment.getCompanyid())));
				    OrgCompany orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
				    user.setCompanyname(orgCompany.getCompanyname());
				    user.setOrgroleid((long)7);
				    user.setIsmanager(0L);
				    OrganizationAPI.getInstance().addUser(user);
				    map.put("NAME", name);
				    map.put("USERID", useridResult);
				    map.put("POSITION", "项目成员");
				}else{
					orgUserService=(OrgUserService) SpringBeanUtil.getBean("orgUserService");
					OrgUser userModel = orgUserService.getUserModel(userid);
					map.put("NAME", userModel.getUsername());
					map.put("USERID", userModel.getUserid());
					map.put("POSITION", DBUtil.getString("SELECT ROLENAME FROM ORGROLE WHERE ID="+userModel.getOrgroleid(), "ROLENAME"));
					map.put("PHONE", userModel.getMobile());
					map.put("EMAIL", userModel.getEmail());
				}
				CYList.add(map);
			}
			List<HashMap> fromSubData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
			if(fromSubData.size()>0){
				for (HashMap hashMap : fromSubData) {
					ProcessAPI.getInstance().removeSubFormData(this.getActDefId(),instanceId,Long.parseLong(hashMap.get("ID").toString()),"SUBFORM_XMCYLB");
				}
				ProcessAPI.getInstance().saveFormDatas(this.getActDefId(), instanceId, "SUBFORM_XMCYLB", CYList, false);
			}else{
				ProcessAPI.getInstance().saveFormDatas(this.getActDefId(), instanceId, "SUBFORM_XMCYLB", CYList, false);
			}
		}
		return flag;
	}
	
	public static String qj2bj(String src) {  
        if (src == null) {  
            return src;  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < src.length(); i++) {  
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内  
                buf.append((char) (ca[i] - CONVERT_STEP));  
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格  
                buf.append(DBC_SPACE);  
            } else { // 不处理全角空格，全角！到全角～区间外的字符  
                buf.append(ca[i]);  
            }  
        }  
        return buf.toString();  
    }
	
	/**
	 * 
	 * @param demId 
	 * @param getFromSubData 物理表中子表信息的集合
	 * @param demUUID
	 * @param dataInstanceid
	 * @param subFromKey 子表名
	 * @param saveFromSubData 需要更新到物理子表中的集合
	 */
	private void setFromSubData(Long demId, List<HashMap> getFromSubData,
			String demUUID, Long dataInstanceid, String subFromKey,
			List<HashMap> saveFromSubData) {
		if(getFromSubData!=null){
			if(getFromSubData.size()>0){
				for (HashMap hashMap : getFromSubData) {
					DemAPI.getInstance().removeSubFormData(PROJECT_UUID, dataInstanceid, Long.parseLong(hashMap.get("ID").toString()), subFromKey);
				}
				if(saveFromSubData!=null){
					DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
				}
			}else{
				if(saveFromSubData!=null){
					DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
				}
			}
		}else{
			if(saveFromSubData!=null){
				DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
			}
		}
	}
	
}
