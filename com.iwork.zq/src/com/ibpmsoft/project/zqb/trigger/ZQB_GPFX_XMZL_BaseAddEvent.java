package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class ZQB_GPFX_XMZL_BaseAddEvent extends DemTriggerEvent {
	public ZQB_GPFX_XMZL_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute(){
		// 保存后，将所需资料保存至子表
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		String XMJDZL=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '通用项目资料表单'", "UUID");
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String sxzlqd = hash.get("SXZLQD") == null ? "" : hash.get("SXZLQD")
				.toString();// 事项资料清单
		String jdbh = hash.get("ID") == null ? "" : hash.get("ID")
				.toString();
		if (!sxzlqd.equals("")) {
			String[] sxzlqdArr = sxzlqd.split(",");// 以逗号分隔
			String sql = "";
			int id = DBUtil.getInt(
					"select nvl(max(id),0)+1 id from BD_ZQB_TYXMZLB", "id");
			for (String sx : sxzlqdArr) {
				String yyjdbh = DBUtil.getString(
						"select JDBH from BD_ZQB_TYXMZLB where jdbh='" + jdbh
								+ "' and jdzl='" + sx + "'", "JDBH");
				if (yyjdbh.equals("")) {
					String userid=this.getUserContext().get_userModel().getUserid();
					Long instanceid=DemAPI.getInstance().newInstance(XMJDZL, userid);
					HashMap hashdata=new HashMap();
					hashdata.put("ID", id);
					hashdata.put("JDBH", jdbh);
					hashdata.put("JDZL", sx);
					DemAPI.getInstance().saveFormData(XMJDZL, instanceid, hashdata, true);
				}
			}
		}
		String usql="UPDATE BD_ZQB_GPFXXMRWLCB SET TASK_NAME='"+map.get("JDMC").toString()+"' WHERE GROUPID='"+jdbh+"'";
		DBUtil.executeUpdate(usql);
		return true;
	}
}
