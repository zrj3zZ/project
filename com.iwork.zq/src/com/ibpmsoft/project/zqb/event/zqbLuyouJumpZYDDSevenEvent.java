package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 重大督导事项流程规则跳转触发第七步
 * 
 * @author 
 * 
 */
public class zqbLuyouJumpZYDDSevenEvent extends SysJumpTriggerEvent {

	public zqbLuyouJumpZYDDSevenEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		if (isHasSPR()) {
			return "usertask3";
		} else {
			return "endevent2";
		}
	}

	

	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		HashMap<String, Object> hash = ProcessAPI.getInstance()
				.getFromData(this.getInstanceId(),
						EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
//		String dem = "a6a3202f5b294bb79acd578ddbe05e55";
//		HashMap<String, Object> condition = new HashMap<String, Object>();
//		condition.put("SXBH", hash.get("SXBH"));
//		List<HashMap> l = DemAPI.getInstance().getList(dem, condition, null);
//		
//		if (l.size() > 0 && l.size() == 1) {
//			// 获取事项基本信息
//			hash = l.get(0);
		HashMap map=DemAPI.getInstance().getFromData(Long.parseLong(hash.get("WLINS").toString()), EngineConstants.SYS_INSTANCE_TYPE_DEM);
        if(map!=null&&map.size()>0){
				// 获取下一节点人员
				String user = map.get("SPR").toString().substring(0,
						map.get("SPR").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance()
						.getUserContext(user);
				uclist.add(uc);
        }

//		}
		return uclist;
	}

	public boolean isHasSPR() {
		HashMap<String, Object> hash = ProcessAPI.getInstance()
				.getFromData(this.getInstanceId(),
						EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap map=DemAPI.getInstance().getFromData(Long.parseLong(hash.get("WLINS").toString()), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if (map!=null&&map.size()>0) {
			return map.get("SPR") != null && !map.get("SPR").equals("");
		}
		return false;
	}

	/**
	 * 获得抄送用户列表
	 */
	public List<UserContext> getCCUserList() {
		List<UserContext> list = new ArrayList<UserContext>();
		String userid = "DU1";
		UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		list.add(uc);
		return list;
	}

}
