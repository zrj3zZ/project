package com.ibpmsoft.project.zqb.hl.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 股改归档保存后触发
 * @author zouyalei
 *
 */
public class zqbGpProDemAfterSaveEvent extends DemTriggerEvent {

	public zqbGpProDemAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		String XMUUID="";
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long instanceid = this.getInstanceId();
		HashMap hash = DemAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(hash.get("XMJD")!=null&&hash.get("XMJD").equals("归档")){
			XMUUID=DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌归档'", null);
			hash.put("STATUS", "已归档");
		}else if(hash.get("XMJD")!=null&&hash.get("XMJD").equals("股转反馈通知")){
			XMUUID=DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌股转反馈通知(物理)'", null);
			if(hash.get("STATUS")==null||hash.get("STATUS").equals("")){
				String projectno = hash.get("PROJECTNO")==null?"":hash.get("PROJECTNO").toString();
				sendMsg(projectno,hash);
			}
			hash.put("STATUS", "已通知");
		}else if(hash.get("XMJD")!=null&&hash.get("XMJD").equals("提交初始登记表")){
			XMUUID=DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌提交初始登记'", null);
			hash.put("STATUS", "已提交");
		}else if(hash.get("XMJD")!=null&&hash.get("XMJD").equals("初步尽调")){
			XMUUID=DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌初步尽调'", null);
			hash.put("STATUS", "已尽调");
		}
		hash.put("TYPENO", instanceid);
		boolean flag = DemAPI.getInstance().updateFormData(XMUUID, instanceid, hash, Long.parseLong(hash.get("ID").toString()), false);
		return true;
	}
	private void sendMsg(String projectno,HashMap hash){
		String actDefId = ProcessAPI.getInstance().getProcessActDefId("XMNHSH");
		
		List lables = new ArrayList();
		lables.add("USERID");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (SELECT SUBSTR(P.ROUTE_PARAM,0, INSTR(P.ROUTE_PARAM,'[',1)-1) USERID FROM PROCESS_STEP_ROUTE P WHERE P.ACT_DEF_ID=?");
		sql.append(" UNION ");
		sql.append("SELECT SUBSTR(P.MANAGER,0, INSTR(P.MANAGER,'[',1)-1) USERID FROM BD_ZQB_PJ_BASE P WHERE P.PROJECTNO=?) WHERE USERID IS NOT NULL");
		
		Map params = new HashMap();
		params.put(1, actDefId);
		params.put(2, projectno);
		
		List<HashMap> list = DBUtil.getDataList(lables, sql.toString(), params);//通知列表
		UserContext uc_ = UserContextUtil.getInstance().getCurrentUserContext();//发件人
		for (HashMap map : list) {
			String userid = map.get("USERID").toString();
			UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
			if(uc!=null){
				String mobile = uc._userModel.getMobile();
				if(mobile!=null&&!mobile.equals("")){
					HashMap<String,String> contentMap = new HashMap<String,String>();
					contentMap.put("CREATEUSER", hash.get("CREATEUSER")==null?"":hash.get("CREATEUSER").toString());
					contentMap.put("PROJECTNAME", hash.get("PROJECTNAME")==null?"":hash.get("PROJECTNAME").toString());
					String content = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.GZFKTZ_REJECT_KEY, contentMap);
					
					MessageAPI.getInstance().sendSMS(uc_, mobile, content.toString());
				}
			}
		}
	}
}
