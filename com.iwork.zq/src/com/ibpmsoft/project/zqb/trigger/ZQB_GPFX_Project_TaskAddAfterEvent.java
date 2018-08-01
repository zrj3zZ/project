package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;


import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;


public class ZQB_GPFX_Project_TaskAddAfterEvent  extends DemTriggerEvent {
	public ZQB_GPFX_Project_TaskAddAfterEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long instanceId = Long.valueOf(String.valueOf(map.get("instanceId"))).longValue();
		if (!map.get("ATTACH").equals("")) {
			String[] sxzlmb = map.get("ATTACH").toString()
					.split(";");
			String userid = this.getUserContext().get_userModel()
					.getUserid();
			for (int i = 0; i < sxzlmb.length; i++) {
				String aa="JDZL"+i;
				if(!"".equals(map.get(aa).toString())){
					String[] zllist = sxzlmb[i].split(":");
					if(zllist.length>=1){
						String[] zll = map.get(aa).toString().split(",");
						String mb = zllist[0];
						for(int j=0;j<zll.length;j++){
							String zl=zll[j];
							HashMap map1 = new HashMap();
							map1.put("JDZL", zl);
							map1.put("PROJECTNO", map.get("PROJECTNO")
									.toString());
							map1.put("SXZL", mb);
							map1.put("JDBH", map.get("GROUPID").toString());
							//通用项目任务资料表单
							String XMRWZL = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '通用项目资料表单'", "UUID");
							Long ins = DemAPI.getInstance().newInstance(XMRWZL, userid);
								DemAPI.getInstance().saveFormData(XMRWZL, ins,
										map1, true);// 物理表单
						}
						}
				}
			}
		}
		return true;
	
	}
}
