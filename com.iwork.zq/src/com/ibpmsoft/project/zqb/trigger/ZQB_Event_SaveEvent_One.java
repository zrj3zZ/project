package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;

/**
 * 项目基本信息添加
 * @author David
 *
 */
@SuppressWarnings("rawtypes")
public class ZQB_Event_SaveEvent_One extends DemTriggerEvent{
	private UserContext me;
	private HashMap formData;
	private HashMap parameters;
	
	public ZQB_Event_SaveEvent_One(UserContext me,HashMap hash){
		this.me=me;
		this.parameters=hash;
	}
	
	
	/**
	 * 执行触发器
	 */
	public boolean execute(){ 
		HashMap formData = this.getFormData();
		if(formData==null){
			return true;
		}
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		String demUUID="d1885d90bfff4cdc8423778bca98e273";
		String dxmc = (String) map.get("JYDXMC");
		String dxbh = (String) map.get("JYDXBH");
		map.put("GSMC", map.get("JYDXMC"));
		map.put("GSLX", map.get("BZ"));
		map.put("ZCH", map.get("JYDXBH"));
		Long InstanceId = Long.valueOf(String.valueOf(map.get("instanceId"))).longValue();
		String createUser = UserContextUtil.getInstance().getCurrentUserId();
		Long instanceId=DemAPI.getInstance().newInstance(demUUID,createUser);
		boolean isLog=false;
		List<HashMap> data = DemAPI.getInstance().getFromSubData(InstanceId, "SUBFORM_JYDX");
		for (HashMap hashMap : data) {
			String jydxmc = hashMap.get("JYDXMC").toString();
			String jydxbh = hashMap.get("JYDXBH").toString();
			if(jydxmc.toString().equals(dxmc)&&jydxbh.toString().equals(dxbh)){
				return isLog;
			}
		}
		List<HashMap> list = DemAPI.getInstance().getList(demUUID, map, null);
		int i=0;
		isLog=true;
		for (HashMap Map : list) {
			String gsmc= Map.get("GSMC").toString();
			String zch = Map.get("ZCH").toString();
			if(gsmc.toString().equals(dxmc)&&zch.toString().equals(dxbh)){
				i++;
			}
		}
		if(i==0){
			DemAPI.getInstance().saveFormData(demUUID, instanceId, map, false);
		}
		return isLog;
	}
	
	public UserContext getMe() {
		return me;
	}


	public void setMe(UserContext me) {
		this.me = me;
	}


	public HashMap getFormData() {
		if ((this.parameters != null) && (this.parameters.get("PROCESS_PARAMETER_FORMDATA") != null)) {
		      this.formData = ((HashMap)this.parameters.get("PROCESS_PARAMETER_FORMDATA"));
		    }
		return formData;
	}


	public void setFormData(HashMap formData) {
		this.formData = formData;
	}
}

