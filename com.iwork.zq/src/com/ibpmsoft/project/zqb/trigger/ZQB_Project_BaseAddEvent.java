package com.ibpmsoft.project.zqb.trigger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.OrganizationAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 项目基本信息添加
 * 
 * @author David
 * 
 */
public class ZQB_Project_BaseAddEvent extends DemTriggerEvent {
	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";
	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public ZQB_Project_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		newMethod();
		return true;
	}
	
	public void newMethod(){
		HashMap formData = this.getFormData();
		Long instanceId = this.getInstanceId();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long demId = Long.parseLong(map.get("modelId").toString());
		String customerno = map.get("CUSTOMERNO").toString();
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String projectNo = hash.get("PROJECTNO").toString();
		String projectName=hash.get("PROJECTNAME").toString();
		Long dataId = Long.parseLong(hash.get("ID").toString());
		String readValue = ConfigUtil.readValue(CN_FILENAME, "isDwPj");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(readValue!=null&&readValue.equals("1")){
			OrgUser getUserModel = uc.get_userModel();
			//添加项目时预置两条 分配部门数据-----start
			String username = getUserModel.getUsername();
			String userid = getUserModel.getUserid();
			String tbsj = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			//投行质控部//场外市场部
			List<HashMap> fromSubData = DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
			if(fromSubData.isEmpty()){
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
				if(fromSubData!=null&&!fromSubData.isEmpty()){
					for (HashMap hashMap : fromSubData) {
						DemAPI.getInstance().removeSubFormData(demId, instanceId, Long.parseLong(hashMap.get("ID").toString()), "SUBFORM_CLJG");
					}
					DemAPI.getInstance().saveFormDatas(demId, instanceId, "SUBFORM_CLJG", data, false);
				}else{
					DemAPI.getInstance().saveFormDatas(demId, instanceId, "SUBFORM_CLJG", data, false);
				}
			}
			//添加项目时预置两条 分配部门数据------end
			//根据主表中的项目阶段列判断是否需要新增项目阶段
			HashMap<String,Object> taskMap=new HashMap<String,Object>();
			taskMap.put("PROJECTNO", projectNo);
			String xmjd=hash.get("XMJD").toString();
			Long xmjdID = DBUtil.getLong("SELECT ID FROM BD_ZQB_KM_INFO WHERE JDMC='"+xmjd+"'", "ID");
			if(xmjdID!=0){
				taskMap.put("GROUPID", xmjdID);
				List<HashMap> list = DemAPI.getInstance().getList(PROJECT_TASK_UUID, taskMap, null);
				if(list.isEmpty()){
					BigDecimal xmjs = getXmjs(customerno, projectNo);
					Long taskinstanceid = DemAPI.getInstance().newInstance(PROJECT_TASK_UUID, getUserModel.getUserid());
					HashMap<String,Object> dataMap = new HashMap<String,Object>();
					String nowDatetime = UtilDate.getNowDatetime();
					String manager = "";
					manager = getUserModel.getUserid() + "[" + getUserModel.getUsername() + "]";
					dataMap.put("MANAGER",manager);
					dataMap.put("GROUPID", xmjdID);
					dataMap.put("SSJE", xmjs);
					dataMap.put("PROJECTNO", projectNo);
					dataMap.put("PROJECTNAME", projectName);
					dataMap.put("SPZT","已提交");
					dataMap.put("GXSJ",nowDatetime);
					DemAPI.getInstance().saveFormData(PROJECT_TASK_UUID,taskinstanceid, dataMap, false);
				}else{
					BigDecimal xmjs = getXmjs(customerno, projectNo);
					if(xmjs.compareTo(new BigDecimal(0))==1){
						Long jdId = DBUtil.getLong("SELECT ID FROM BD_PM_TASK WHERE GROUPID=(SELECT MAX(GROUPID) FROM BD_PM_TASK WHERE PROJECTNO='"+projectNo+"') AND PROJECTNO='"+projectNo+"'", "ID");
						HashMap xmjdMap = DemAPI.getInstance().getFormData(PROJECT_TASK_UUID, jdId);
						BigDecimal ssje=new BigDecimal(xmjdMap.get("SSJE")==null?"0":xmjdMap.get("SSJE").toString().equals("")?"0":xmjdMap.get("SSJE").toString());
						xmjdMap.put("SSJE", ssje.add(xmjs));
						Long xmjdId = Long.parseLong(xmjdMap.get("ID").toString());
						Long instaceId = DemAPI.getInstance().getInstaceId("BD_PM_TASK", xmjdId);
						Long xmjsDemId = Long.parseLong(ConfigUtil.readValue(CN_FILENAME, "xmjsid"));
						DemAPI.getInstance().updateFormData(PROJECT_TASK_UUID, instaceId, xmjdMap, xmjdId, false);
					}
				}
			}
			/*HashMap lcFromData = (HashMap) hash.clone();
			String sfsp=hash.get("SFBMFZRSP")==null?"":hash.get("SFBMFZRSP").toString();
			String zbspzt=hash.get("ZBSPZT")==null?"":hash.get("ZBSPZT").toString();
			Long lcbs=hash.get("ZBLCBS")==null?0:Long.parseLong(hash.get("ZBLCBS").toString().equals("")?"0":hash.get("ZBLCBS").toString());
			String taskid = "";
			String executionId = "";
			String actDefId ="";
			Long lcInstanceId = 0L;
			if(sfsp.equals("1")&&lcbs==0&&zbspzt.equals("")){
				actDefId= ProcessAPI.getInstance().getProcessActDefId("XMZBLC");
				lcFromData.put("CREATEUSER", username);
				lcFromData.put("CREATEUSERID", userid);
				lcFromData.put("CREATEDATE", UtilDate.getNowDatetime());
				Long formId = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
				lcInstanceId = ProcessAPI.getInstance().newInstance(actDefId, formId, userid);
				ProcessAPI.getInstance().saveFormData(actDefId, lcInstanceId, lcFromData, false);// 保存流程
				String jd1 = SystemConfig._xmzbConf.getJd1();
				List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
				Task newTaskId = ProcessAPI.getInstance().newTaskId(lcInstanceId);
				for (Task task : tasklist) {
					if (Long.parseLong(task.getProcessInstanceId()) == lcInstanceId) {
						taskid = task.getId();
						executionId = task.getExecutionId();
					}
				}
				hash.put("A02", 1);
				hash.put("ZBSPZT", "未提交");
				hash.put("ZBLCBH", actDefId);
				hash.put("ZBLCBS", lcInstanceId);
				hash.put("ZBSTEPID", jd1);
				hash.put("ZBTASKID", taskid);
				Long dataid = Long.parseLong(hash.get("ID").toString());
				DemAPI.getInstance().updateFormData(PROJECT_UUID, instanceId, hash, dataid, false);
				List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
				List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
				List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(lcInstanceId, "SUBFORM_CLR");
				setFromSubData(lcFromSubClrData,actDefId,lcInstanceId,"SUBFORM_CLR",fromSubClrData,false);
				List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(lcInstanceId, "SUBFORM_CLJG");
				setFromSubData(lcFromSubCljgData,actDefId,lcInstanceId,"SUBFORM_CLJG",fromSubCljgData,false);
			}*/
		}
		// 添加项目负责人时，查询项目审批人维护是否存在配置记录，如果不存在，则插入一条
		// 判断是不是多项目流程
		String config = SystemConfig._xmlcConf.getConfig();
		if (config.equals("2")) {
			String xmsprUUID = "24f7944184ca402d986325ce72fa20c9";
			HashMap conditionMap = new HashMap();
			conditionMap.put("CSFZR", map.get("OWNER"));
			List list = DemAPI.getInstance().getList(xmsprUUID, conditionMap,null);
			if (list.size() <= 0) {
				Long ins = DemAPI.getInstance().newInstance(xmsprUUID,UserContextUtil.getInstance().getCurrentUserId());
				HashMap hashdata = new HashMap();
				hashdata.put("CSFZR", map.get("OWNER"));
				String spr = DBUtil.getString("select * from BD_ZQB_XMSPRWH  order by nvl(to_char(zhgxsj,'yyyy-MM-dd hh24:mi:ss'),0) desc","FSFZR");
				if (spr != null && !spr.equals("")) {
					hashdata.put("FSFZR", spr);
				}
				String spr1 = DBUtil.getString("select * from BD_ZQB_XMSPRWH  order by nvl(to_char(zhgxsj,'yyyy-MM-dd hh24:mi:ss'),0) desc","ZZSPR");
				if (spr1 != null && !spr1.equals("")) {
					hashdata.put("ZZSPR", spr1);
				}
				hashdata.put("ZHGXSJ", UtilDate.datetimeFormat(new Date()));
				DemAPI.getInstance().saveFormData(xmsprUUID, ins, hashdata,false);
			}
		}
		
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "项目管理维护", "更新项目："+projectName);
		}else{
			LogUtil.getInstance().addLog(dataId, "项目管理维护", "添加项目："+projectName);
		}
	}
	
	public BigDecimal getXmjs(String customerno,String projectno){
		BigDecimal bd=new BigDecimal(0L);
		BigDecimal xmjdTotal=new BigDecimal(DBUtil.getDouble("SELECT SUM(SSJE) TOTAL FROM BD_PM_TASK WHERE PROJECTNO='"+projectno+"'", "TOTAL"));
		BigDecimal xmjsTotal=new BigDecimal(DBUtil.getDouble("SELECT SUM(RZJE) TOTAL FROM BD_ZQB_XMJSXXGLB WHERE XMLX='推荐挂牌' AND CUSTOMERNO='"+customerno+"'", "TOTAL"));
		int compareTo = xmjsTotal.compareTo(xmjdTotal);
		if(compareTo==1){
			bd = xmjsTotal.subtract(xmjdTotal);
		}
		return bd;
	}
	
	public void oldMethod(){
		String demUUID = "33833384d109463285a6a348813539f1";// 项目管理uuid
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String smsContent = "";
		String sysMsgContent = "";
		/*if (!"0".equals(map.get("instanceId").toString())) {
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
					ZQB_Notice_Constants.PROJECT_BASE_UPDATE_KEY, map);
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
					ZQB_Notice_Constants.PROJECT_BASE_UPDATE_KEY, map);
		} else {
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(
					ZQB_Notice_Constants.PROJECT_BASE_ADD_KEY, map);
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(
					ZQB_Notice_Constants.PROJECT_BASE_ADD_KEY, map);
		}*/
		String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(
				ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
		UserContext uc = this.getUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(
				userid);
		HashMap data = null;
		HashMap data2 = null;
		if (map.get("TYPENO")!=null&&map.get("TYPENO").toString().equals("1")) {
			HashMap conditionMap = new HashMap();
			HashMap conditionMap2 = new HashMap();
			conditionMap.put("CUSTOMERNO", map.get("CUSTOMERNO").toString());
			conditionMap.put("PROJECTNO", map.get("PROJECTNO").toString());
			conditionMap2.put("CUSTOMERNO", map.get("CUSTOMERNO").toString());
			List<HashMap> list2 = DemAPI.getInstance().getList(
					"a243efd832bf406b9caeaec5df082e28", conditionMap2, null);
			for (int i = 0; i < list2.size(); i++) {
				HashMap customer = list2.get(i);
				data2 = (HashMap) customer.clone();
				data2.put("YGP", "已挂牌");
				if (customer.get("YGP") == null
						|| customer.get("YGP").toString().equals("未挂牌")) {
					boolean flag = DemAPI.getInstance().updateFormData(
							"a243efd832bf406b9caeaec5df082e28",
							Long.valueOf(
									String.valueOf(customer.get("INSTANCEID")))
									.longValue(), data2,
							Long.parseLong(customer.get("ID").toString()),
							false);
					if (flag) {
						OrgDepartment model = new OrgDepartment();
						model.setDepartmentname(hash.get("CUSTOMERNAME")
								.toString());
						model.setDepartmentno(hash.get("CUSTOMERNO").toString());
						model.setCompanyid("2");
						model.setParentdepartmentid(new Long(51));
						String id = DBUtil.getString(
								"select * from ORGDEPARTMENT where DEPARTMENTNO='"
										+ model.getDepartmentno() + "'", "ID");
						if (id != null && !id.equals("")) {
							model.setId(Long.parseLong(id));
							OrganizationAPI.getInstance().updateDepartment(
									model);
						} else {
							OrganizationAPI.getInstance().addDepartment(model);
						}
					}
				}

			}

			List<HashMap> list = DemAPI.getInstance().getList(
					"33833384d109463285a6a348813539f1", conditionMap, null);
			if (list.size() > 0) {
				data = list.get(0);
			}
			data.put("STATUS", "已完成");
			DemAPI.getInstance().updateFormData(
					"33833384d109463285a6a348813539f1",
					Long.parseLong(data.get("INSTANCEID").toString()), data,
					Long.parseLong(data.get("ID").toString()), false);
			String cxduid = DBUtil.getString(
					"select * from BD_MDM_KHQXGLB where KHBH='"
							+ hash.get("CUSTOMERNO") + "'", "ID");
			if (cxduid == null || cxduid.equals("")) {
				String demUUIDDD = "84ff70949eac4051806dc02cf4837bd9";// 持续督导
				HashMap hashmap = new HashMap();
				Long instanceid = DemAPI.getInstance()
						.newInstance(
								demUUIDDD,
								UserContextUtil.getInstance()
										.getCurrentUserContext()._userModel
										.getUserid());
				hashmap.put("instanceId", instanceid);
				hashmap.put("formid", 114);
				hashmap.put("metadataid", 36);
				hashmap.put("modelType", "DEM");
				hashmap.put(
						"KHFZR",
						hash.get("OWNER").equals("PJDU[项目督导]") ? hash
								.get("OWNER") : "");
				hashmap.put("KHBH", hash.get("CUSTOMERNO"));
				hashmap.put("KHMC", hash.get("CUSTOMERNAME"));
				hashmap.put("TXDF", "否");
				hashmap.put("SFFP",
						hash.get("OWNER").equals("PJDU[项目督导]") ? "是" : "否");
				DemAPI.getInstance().saveFormData(demUUIDDD, instanceid,
						hashmap, false);
			}
		}
		if (target != null) {
			if (!smsContent.equals("")) {
				String mobile = target.get_userModel().getMobile();
				if (mobile != null && !mobile.equals("")) {
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
			}
			if (!sysMsgContent.equals("")) {
				// MessageAPI.getInstance().sendSysMsg(userid, type, title,
				// content, url, priority)
				MessageAPI.getInstance().sendSysMsg(userid, "项目基本信息维护提醒",
						sysMsgContent);
			}
		}
		// 添加项目负责人时，查询项目审批人维护是否存在配置记录，如果不存在，则插入一条
		// 判断是不是多项目流程
		String config = SystemConfig._xmlcConf.getConfig();
		if (config.equals("2")) {
			String xmsprUUID = "24f7944184ca402d986325ce72fa20c9";
			HashMap conditionMap = new HashMap();
			conditionMap.put("CSFZR", map.get("OWNER"));
			List list = DemAPI.getInstance().getList(xmsprUUID, conditionMap,
					null);
			if (list.size() <= 0) {
				Long ins = DemAPI.getInstance().newInstance(xmsprUUID,
						UserContextUtil.getInstance().getCurrentUserId());
				HashMap hashdata = new HashMap();
				hashdata.put("CSFZR", map.get("OWNER"));
				String spr = DBUtil
						.getString(
								"select * from BD_ZQB_XMSPRWH  order by nvl(to_char(zhgxsj,'yyyy-MM-dd hh24:mi:ss'),0) desc",
								"FSFZR");
				if (spr != null && !spr.equals("")) {
					hashdata.put("FSFZR", spr);
				}
				String spr1 = DBUtil
						.getString(
								"select * from BD_ZQB_XMSPRWH  order by nvl(to_char(zhgxsj,'yyyy-MM-dd hh24:mi:ss'),0) desc",
								"ZZSPR");
				if (spr1 != null && !spr1.equals("")) {
					hashdata.put("ZZSPR", spr1);
				}
				hashdata.put("ZHGXSJ", UtilDate.datetimeFormat(new Date()));
				DemAPI.getInstance().saveFormData(xmsprUUID, ins, hashdata,
						false);
			}
		}
	}
	
	/**
	 * 
	 * @param getFromSubData 流程表中子表的信息
	 * @param actDefId 流程Id
	 * @param instanceId
	 * @param fromSubKey 子表名
	 * @param savefromSubData 需保存到流程数据中子表的信息
	 * @param isLog 是否记录日志
	 */
	private void setFromSubData(List<HashMap> getFromSubData, String actDefId, Long instanceId,
			String fromSubKey, List<HashMap> savefromSubData, boolean isLog) {
		if(getFromSubData!=null){
			if(getFromSubData.size()>0){
				for (HashMap hashMap : getFromSubData) {
					ProcessAPI.getInstance().removeSubFormData(actDefId,instanceId,Long.parseLong(hashMap.get("ID").toString()),fromSubKey);
				}
				if(savefromSubData!=null){
					ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
				}
			}else{
				if(savefromSubData!=null){
					ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
				}
			}
		}else{
			if(savefromSubData!=null){
				ProcessAPI.getInstance().saveFormDatas(actDefId, instanceId, fromSubKey, savefromSubData, isLog);
			}
		}
	}
}
