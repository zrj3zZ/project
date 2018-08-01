package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbLuyouJumpManyXMRWTwoEvent extends SysJumpTriggerEvent {

	public zqbLuyouJumpManyXMRWTwoEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId() {

		HashMap<String, Object> h = ProcessAPI.getInstance()
				.getFromData(this.getInstanceId(),
						EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String dem = "33833384d109463285a6a348813539f1";
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("PROJECTNO", h.get("PROJECTNO"));
		List<HashMap> l = DemAPI.getInstance().getList(dem, condition, null);
		if (l.size() > 0) {
			HashMap hash = l.get(0);
			// 获取项目负责人
			if (hash.get("OWNER") != null
					&& !hash.get("OWNER").toString().equals("")) {
				if (isExists("FSFZR", hash.get("OWNER").toString())) {
					return SystemConfig._xmlcConf.getJd3();
				} else if (isExists("ZZSPR", hash.get("OWNER").toString())) {
					return SystemConfig._xmlcConf.getJd4();
				} else {
					return SystemConfig._xmlcConf.getEnd();
				}
			} else {
				return SystemConfig._xmlcConf.getEnd();
			}
		} else {
			return SystemConfig._xmlcConf.getEnd();
		}
	}

	public boolean isExists(String filedName, String name) {
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String spr = DBUtil.getString(
				"select * from BD_ZQB_XMSPRWH where csfzr='" + name + "'",
				filedName);
		if (spr != null && !spr.equals("")) {
			return true;
		}
		return false;
	}

	public List<UserContext> getNextUserList() {
		List<UserContext> uclist = new ArrayList<UserContext>();// 返回用户
		HashMap<String, Object> hash = ProcessAPI.getInstance()
				.getFromData(this.getInstanceId(),
						EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String dem = "33833384d109463285a6a348813539f1";
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("PROJECTNO", hash.get("PROJECTNO"));
		List<HashMap> l = DemAPI.getInstance().getList(dem, condition, null);
		if (l.size() > 0) {
			hash = l.get(0);
			// 获取项目负责人
			if (hash.get("OWNER") != null
					&& !hash.get("OWNER").toString().equals("")) {

				String demUUID = "24f7944184ca402d986325ce72fa20c9";
				conditionMap.put("CSFZR", hash.get("OWNER").toString());
				List<HashMap> list = DemAPI.getInstance().getList(demUUID,
						conditionMap, null);
				if (list.size() == 1) {

					if (list.get(0).get("FSFZR") != null
							&& !list.get(0).get("FSFZR").toString().equals("")) {
						String user = list
								.get(0)
								.get("FSFZR")
								.toString()
								.substring(
										0,
										list.get(0).get("FSFZR").toString()
												.indexOf("["));
						UserContext uc = UserContextUtil.getInstance()
								.getUserContext(user);
						uclist.add(uc);

					} else if (list.get(0).get("ZZSPR") != null
							&& !list.get(0).get("ZZSPR").toString().equals("")) {
						String user = list
								.get(0)
								.get("ZZSPR")
								.toString()
								.substring(
										0,
										list.get(0).get("ZZSPR").toString()
												.indexOf("["));
						UserContext uc = UserContextUtil.getInstance()
								.getUserContext(user);
						uclist.add(uc);

					}
				}
			} else {
				String user = hash
						.get("OWNER")
						.toString()
						.substring(0, hash.get("OWNER").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance().getUserContext(
						user);
				uclist.add(uc);
			}

		}
		return uclist;
	}

	public boolean isOwner() {
		HashMap<String, Object> conditionMap = new HashMap<String, Object>();
		String dem = "33833384d109463285a6a348813539f1";
		HashMap<String, Object> condition = new HashMap<String, Object>();
		HashMap<String, Object> hash = ProcessAPI.getInstance()
				.getFromData(this.getInstanceId(),
						EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		condition.put("PROJECTNO", hash.get("PROJECTNO"));
		List<HashMap> l = DemAPI.getInstance().getList(dem, condition, null);
		if (l.size() > 0) {
			hash = l.get(0);

			String username = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			return username.equals(hash.get("OWNER").toString());
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
