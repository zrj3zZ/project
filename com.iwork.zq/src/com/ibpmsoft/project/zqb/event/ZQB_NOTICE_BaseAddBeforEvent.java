package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;

import com.iwork.core.organization.context.UserContext;

import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;

public class ZQB_NOTICE_BaseAddBeforEvent extends ProcessStepTriggerEvent {

	public ZQB_NOTICE_BaseAddBeforEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute(){
		/*boolean flag = false;
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		if(map.get("SXBCFILE")!=null&&!map.get("SXBCFILE").toString().equals("")){
			int num = DBUtil.getInt("SELECT COUNT(XPFILE) NUM FROM BD_XP_XPSXBCWJB WHERE XPFILE LIKE '%"+map.get("SXBCFILE")+"%'", "NUM");
			if(num==0){
				String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='信披事项备查文件表'", "UUID");
				Long instanceid = DemAPI.getInstance().newInstance(uuid, UserContextUtil.getInstance().getCurrentUserId());
				HashMap xpfilemap = new HashMap();
				xpfilemap.put("XPSXQTID", map.get("BZLX")==null?"":map.get("BZLX").toString());
				xpfilemap.put("XPFILE", map.get("SXBCFILE")==null?"":map.get("SXBCFILE").toString());
				DemAPI.getInstance().saveFormData(uuid, instanceid, xpfilemap, false);
			}
		}*/
		return true;
	}

}

