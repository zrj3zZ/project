package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;


public class ZQB_CXDDGZFK_BaseAddEvent extends DemTriggerEvent {
	public ZQB_CXDDGZFK_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		// 插入流程表，并将数据更新到流程表中
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long instanceId = this.getInstanceId();
		HashMap hashMap = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String tablename = this.getTableName();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		String extend1 = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend1();
		String tzggid = map.get("TZGGID").toString();
		String sql = "update BD_XP_DDGZHFQK set status='已回复',hfsj=sysdate where userid='"+uc._userModel.getUserid()+"' and ggid=" + tzggid;
		int updateInt = DBUtil.executeUpdate(sql);
		String xm = map.get("XM").toString();
		
		LogUtil.getInstance().addLog(Long.parseLong(hashMap.get("ID").toString()), "持续督导工作反馈", "新增持续督导工作反馈：通知公告ID："+tzggid+",回复人为"+xm+"的持续督导工作反馈信息。");
		return true;
	}
}
