package com.ibpmsoft.project.zqb.hl.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbBgProLuyouJumpSbbaEvent extends SysJumpTriggerEvent{

	public zqbBgProLuyouJumpSbbaEvent(UserContext me, Task task) {
		super(me, task);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._hlBgSbzlLcConf.getJd1())){
			return SystemConfig._hlBgSbzlLcConf.getJd2();
		}else{
			return SystemConfig._hlBgSbzlLcConf.getJd3();
		}
	}
	
	public List<UserContext> getNextUserList() {
		HashMap<String, Object> processData = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String projectno = processData.get("XMBH")==null?"":processData.get("XMBH").toString();
		Map params = new HashMap();
		params.put(1, projectno);
		String manager = DBUtil.getDataStr("MANAGER", "SELECT SUBSTR(B.MANAGER,0,INSTR(B.MANAGER,'[',1)-1) AS MANAGER FROM BD_ZQB_BGZZLXXX B WHERE B.XMBH=?", params);
		HashMap<String, Object> hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		// 获取项目负责人
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		if (manager != null && !manager.toString().equals("")) {
			UserContext uc = UserContextUtil.getInstance().getUserContext(manager);
			uclist.add(uc);
		}
		return uclist;
	}
}
