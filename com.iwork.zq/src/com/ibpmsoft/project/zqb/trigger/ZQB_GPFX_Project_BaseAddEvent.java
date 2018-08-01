package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 项目基本信息添加
 * 
 * @author David
 * 
 */
public class ZQB_GPFX_Project_BaseAddEvent extends DemTriggerEvent {
	public ZQB_GPFX_Project_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		//股票发行项目
		String XMUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String projectName=hash.get("PROJECTNAME").toString();
		Long dataId = Long.parseLong(hash.get("ID").toString());
		/*String smsContent = "";
		String sysMsgContent = "";*/
		/*String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
		UserContext uc = this.getUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
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
				MessageAPI.getInstance().sendSysMsg(userid, "项目基本信息维护提醒",sysMsgContent);
			}
		}*/
		// 添加项目负责人时，查询项目审批人维护是否存在配置记录，如果不存在，则插入一条
		// 判断是不是多项目流程
		String gpfxconfig = SystemConfig._gpfxXmlcConf.getConfig();
		if(gpfxconfig.equals("2")){
			String PROJECT_UUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目'", "UUID");
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put("PROJECTNO", map.get("PROJECTNO").toString());
			List<HashMap> l = DemAPI.getInstance().getList(PROJECT_UUID, condition, null);
			HashMap<String, Object> conditionMap2 = new HashMap<String, Object>();
			String demUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目审批人'", "UUID");
			String owner = l.get(0).get("OWNER").toString();
			conditionMap2.put("CSFZR", owner);
			List<HashMap> list = DemAPI.getInstance().getList(demUUID,conditionMap2, null);
			if(list.isEmpty()){
				Long instanceid1 = DemAPI.getInstance().newInstance(demUUID,owner.substring(0,owner.indexOf("[")));
				HashMap gpfxmap= new HashMap();
				gpfxmap.put("CSFZR", owner);
				gpfxmap.put("ZHGXSJ", UtilDate.getNowDatetime());
				DemAPI.getInstance().saveFormData(demUUID,instanceid1, gpfxmap, false);
			}
		}
		
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "股票发行项目管理维护", "更新股票发行项目："+projectName);
		}else{
			LogUtil.getInstance().addLog(dataId, "股票发行项目管理维护", "添加股票发行项目："+projectName);
		}
		return true;
	}
}
