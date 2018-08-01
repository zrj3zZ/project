package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbLuyouJumpCXDDFourEvent extends SysJumpTriggerEvent{

	public zqbLuyouJumpCXDDFourEvent(UserContext me, Task task) {
		super(me, task);
	}
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		 if(isExists("CWSCBFZR3")){
				return SystemConfig._cxddyfplcConf.getJd4();
			}else{
				return SystemConfig._cxddyfplcConf.getEnd();
			}
	}
	
	public  List<UserContext> getNextUserList(){
		 List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		 HashMap<String,Object>  hash=ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 if(hash.get("WLINS")!=null&&!hash.get("WLINS").toString().equals("")){
			 Long instanceid=Long.parseLong(hash.get("WLINS").toString());
			   HashMap map=DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			   if(map!=null&&map.get("CWSCBFZR3")!=null&&!map.get("CWSCBFZR3").equals("")){
					 UserContext uc = UserContextUtil.getInstance().getUserContext(map.get("CWSCBFZR3").toString()
							 .substring(0, 
									 map.get("CWSCBFZR3").toString().indexOf("["))
										);
					 uclist.add(uc);
				 }
		 }
		 
		
		 return uclist;
	}
	
	public boolean isExists(String filedName){
		 HashMap<String,Object>  hash=ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 if(hash.get("WLINS")!=null&&!hash.get("WLINS").toString().equals("")){
			 Long instanceid=Long.parseLong(hash.get("WLINS").toString());
		   HashMap map=DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if(map!=null&&map.get(filedName)!=null&&!map.get(filedName).equals("")){
						return true;
			}
		 }
		 return false;
	}
	/**
	 * 获得抄送用户列表
	 */
	public  List<UserContext> getCCUserList(){
		 List<UserContext> list = new ArrayList<UserContext>();
		 String userid = "DU1";
		 UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		 list.add(uc);
		 return list;
	}
}
