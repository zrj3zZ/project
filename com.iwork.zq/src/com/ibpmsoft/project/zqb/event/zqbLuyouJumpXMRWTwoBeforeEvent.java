package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.DemAPI;

/**
 * 椤圭洰浠诲姟娴佺▼瑙勫垯璺宠浆瑙﹀彂绗簩姝�
 * 
 * @author zouyalei
 * 
 */
public class zqbLuyouJumpXMRWTwoBeforeEvent extends SysJumpTriggerEvent {

	public zqbLuyouJumpXMRWTwoBeforeEvent(UserContext me, Task task) {
		super(me, task);
	}

	/**
	 * 鑾峰緱涓嬩竴涓姙鐞嗚妭鐐�
	 */
	public String getNextStepId() {
		if(isExists("CSFZR")){
			return SystemConfig._xmlcConf.getJd3();
		}	
	  else if(isExists("FSFZR")){
		 return SystemConfig._xmlcConf.getJd4();
		}else if(isExists("ZZSPR")){
			return SystemConfig._xmlcConf.getJd5();
		}else{
			return SystemConfig._xmlcConf.getEnd();
			}
	}
	public boolean isExists(String filedName){
			 HashMap<String,Object> conditionMap=new HashMap<String,Object>();
			String spr= DBUtil.getString("select * from BD_ZQB_XMSPRWH ", filedName);
			if(spr!=null&&!spr.equals("")){
				return true;
			}
		 return false;
	}
	

	public List<UserContext> getNextUserList() {
		String demUUID = "24f7944184ca402d986325ce72fa20c9";
		HashMap conditionMap=new HashMap();
		List<UserContext> uclist=new ArrayList<UserContext>();
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		if (list.size() == 1) {
			if (list.get(0).get("CSFZR") != null
					&& !list.get(0).get("CSFZR").toString().equals(
							"")) {
				String user=list.get(0).get("CSFZR").toString().substring(0, 
						list.get(0).get("CSFZR").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance()
						.getUserContext(user);
				uclist.add(uc);

			}else if (list.get(0).get("FSFZR") != null
					&& !list.get(0).get("FSFZR").toString().equals(
							"")) {
				String user=list.get(0).get("FSFZR").toString().substring(0, 
						list.get(0).get("FSFZR").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance()
						.getUserContext(user);
				uclist.add(uc);

			}else if (list.get(0).get("ZZSPR") != null
					&& !list.get(0).get("ZZSPR").toString().equals(
							"")) {
				String user=list.get(0).get("ZZSPR").toString().substring(0, 
						list.get(0).get("ZZSPR").toString().indexOf("["));
				UserContext uc = UserContextUtil.getInstance()
						.getUserContext(user);
				uclist.add(uc);

			}
		}

		return uclist;
	}
   
	/**
	 * 鑾峰緱鎶勯�鐢ㄦ埛鍒楄〃
	 */
	public List<UserContext> getCCUserList() {
		List<UserContext> list = new ArrayList<UserContext>();
		String userid = "DU1";
		UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		list.add(uc);
		return list;
	}

}
