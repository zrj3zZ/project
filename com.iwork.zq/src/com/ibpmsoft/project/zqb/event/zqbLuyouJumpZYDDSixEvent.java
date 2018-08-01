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
 * 重大督导事项流程规则跳转触发第六步
 * 
 * @author 
 * 
 */
public class zqbLuyouJumpZYDDSixEvent extends SysJumpTriggerEvent {

	public zqbLuyouJumpZYDDSixEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {
		if(isExists()){
			return "usertask12";
		}else if (isHasSPR()) {
			return "usertask3";
		} else {
			return "endevent2";
		}
	}

	public boolean isExists() {
		HashMap<String, Object> hash = ProcessAPI.getInstance()
				.getFromData(this.getInstanceId(),
						EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (hash.get("WLINS") != null
				&& !hash.get("WLINS").toString().equals("")) {
			Long instanceid = Long.parseLong(hash.get("WLINS")
					.toString());
			List<HashMap> list = DemAPI.getInstance().getFromSubData(
					instanceid, "SUBFORM_SXCYRY");// 获取参与人员
				// 有参与人取得第五个参与人员
				if (list.size() > 5) {
					return true;
				}
		}
		return false;
	}

	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		HashMap<String, Object> hash = ProcessAPI.getInstance()
				.getFromData(this.getInstanceId(),
						EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap map=DemAPI.getInstance().getFromData(Long.parseLong(hash.get("WLINS").toString()), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if (map!=null&&map.size() > 0 ) {
			// 获取事项基本信息
			//hash =map;
			// 获取
			if (hash.get("WLINS") != null
					&& !hash.get("WLINS").toString().equals("")) {
				Long instanceid = Long.parseLong(hash.get("WLINS")
						.toString());
				List<HashMap> list = DemAPI.getInstance().getFromSubData(
						instanceid, "SUBFORM_SXCYRY");// 获取参与人员
				// 有参与人取得第五个参与人员
				if (list.size() > 5) {
					if (list.get(5).get("USERID") != null) {
						String userid = list.get(5).get("USERID").toString();
						UserContext uc = UserContextUtil.getInstance()
								.getUserContext(userid);// 获取用户
						uclist.add(uc);
						return uclist;

					}
					// 没有参与人员，直接回到最后的审批人
				}
			} 
				// 获取下一节点人员
				String user = map.get("SPR").toString().substring(0,
						map.get("SPR").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance()
						.getUserContext(user);
				uclist.add(uc);

		}
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
