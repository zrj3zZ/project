package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.Map;

import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class ZQB_XMZL_BaseAddEvent extends DemTriggerEvent {
	public static final String XMJDZL="98f297bccb85479ba29faf1fb5a05734";//主数据模型管理-》项目资料表单
	public ZQB_XMZL_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute(){
		// 保存后，将所需资料保存至子表
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
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
					"select nvl(max(id),0)+1 id from BD_ZQB_XMZLB", "id");
			for (String sx : sxzlqdArr) {
				Map params = new HashMap();
				params.put(1,jdbh);
				params.put(2,sx);
				//name=DBUTilNew.getDataStr("KFLB", "SELECT KFLB FROM bd_xp_kflxb WHERE ID=? ", params);
				String yyjdbh = DBUTilNew.getDataStr("JDBH","select JDBH from BD_ZQB_XMZLB where jdbh= ? and jdzl= ? ", params);
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
		String usql="UPDATE BD_ZQB_XMRWGLLCB SET TASK_NAME='"+map.get("JDMC").toString()+"' WHERE GROUPID='"+jdbh+"'";
		DBUtil.executeUpdate(usql);
		return true;
	}
}
