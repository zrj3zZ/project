package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.util.EventUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbLuyouJumpGGDUSevenEvent  extends SysJumpTriggerEvent{
	public zqbLuyouJumpGGDUSevenEvent(UserContext me, Task task) {
		super(me, task);
	}
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		HashMap<String,Object>  hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		 String noticetype = hash.get("NOTICETYPE").toString();
		 //获取当前客户编号
		 if(hash.get("KHBH")!=null&&!hash.get("KHBH").toString().equals("")){
			 String demUUID="84ff70949eac4051806dc02cf4837bd9";//持续督导分派表单
			 conditionMap.put("KHBH", hash.get("KHBH").toString());
			 conditionMap.put("KHMC", hash.get("KHMC").toString());
			 List<HashMap> list=DemAPI.getInstance().getList(demUUID, conditionMap, null);
			 if(list.size()==1){
				 HashMap hashMap = list.get(0);
				 boolean isActiveNextNodeFunction = EventUtil.isActiveNextNodeFunction();
				 if(isExists("GGFBR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "GGFBR"))){
					 return SystemConfig._cxddfqlcConf.getJd8();
				 }else{
					 return SystemConfig._cxddfqlcConf.getEnd();
				 }
			 }else{
				 return SystemConfig._cxddfqlcConf.getEnd();
			 }
		}else{
			return SystemConfig._cxddfqlcConf.getEnd();
		}
	}
	public boolean isExists(String filedName){
		 HashMap<String,Object>  hash=ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 if(hash.get("KHBH")!=null&&!hash.get("KHBH").toString().equals("")){
			 HashMap<String,Object> conditionMap=new HashMap<String,Object>();
			 conditionMap.put("KHBH", hash.get("KHBH").toString());
			 conditionMap.put("KHMC", hash.get("KHMC").toString());
			List<HashMap> list=DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", conditionMap, null);
			if(list.size()>0){
				for(HashMap map:list){
					if(map.get(filedName)!=null&&!map.get(filedName).toString().equals("")&&isDqspr(filedName,hash,map)){
						return true;
					}
				}
			}
			
		
		 }
		 return false;
	}
	public  List<UserContext> getNextUserList(){
		 List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		 HashMap<String,Object>  hash=ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		 //获取当前客户编号
		 if(hash.get("KHBH")!=null&&!hash.get("KHBH").toString().equals("")){
			 String demUUID="84ff70949eac4051806dc02cf4837bd9";//持续督导分派表单
			 conditionMap.put("KHBH", hash.get("KHBH").toString());
			 conditionMap.put("KHMC", hash.get("KHMC").toString());
			 List<HashMap> list=DemAPI.getInstance().getList(demUUID, conditionMap, null);
			 if(list.size()==1){
				 HashMap hashMap = list.get(0);
				 boolean isActiveNextNodeFunction = EventUtil.isActiveNextNodeFunction();
				 if(isExists("GGFBR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "GGFBR"))){
					 UserContext uc = UserContextUtil.getInstance().getUserContext(list.get(0).get("GGFBR").toString().substring(0,list.get(0).get("GGFBR").toString().indexOf("[")));
					 uclist.add(uc);
				 }
			 }
		 }
		 return uclist;
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

	/**
	 * 是否为定期审批人
	 * @return
	 */
	public boolean isDqspr(String spr,HashMap hash,HashMap map){
		boolean flag=true;
		if(!"QYNBRYSH".equals(spr)){
			String noticetype = hash.get("NOTICETYPE").toString();
			String isDq = map.get(spr+"DQ").toString();
			if(!"".equals(isDq)&&"定期报告".equals(noticetype)){
				flag=true;
			}else if(!"".equals(isDq)&&!"定期报告".equals(noticetype)){
				flag=false;
			}
		}
		return flag;
	}
	
	public boolean isSkip(HashMap map,String sprNode){
		boolean flag=false;
		String userId = "";
		String sprId = map.get(sprNode).toString().substring(0,map.get(sprNode).toString().indexOf("["));
		Long taskId=0L;
		if(sprId!=null&&!sprId.equals("")&&!userId.equals(sprId)){
			userId=sprId;
		}
		List<ProcessRuOpinion> processOpinionList = ProcessAPI.getInstance().getProcessOpinionList(this.getActDefId(), this.getInstanceId());
		if(processOpinionList.isEmpty()){
			return flag;
		}else{
			Collections.reverse(processOpinionList);
			for (ProcessRuOpinion processRuOpinion : processOpinionList) {
				String action = processRuOpinion.getAction();
				if(action.equals("驳回")){
					taskId=processRuOpinion.getTaskid();
					break;
				}
			}
			for (ProcessRuOpinion processRuOpinion : processOpinionList) {
				if(taskId!=0){
					String createuser = processRuOpinion.getCreateuser();
					if(taskId<processRuOpinion.getTaskid()){
						if(createuser.equals(userId)){
							flag=true;
							break;
						}else{
							flag=false;
						}
					}else{
						continue;
					}
				}else if(taskId==0){
					String createuser = processRuOpinion.getCreateuser();
					if(createuser.equals(userId)){
						flag=true;
						break;
					}else{
						flag=false;
					}
				}
			}
		}
		return flag;
	}
	
	public boolean isNextNode(HashMap map,String filedName){
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		String sprId = map.get(filedName).toString().substring(0,map.get(filedName).toString().indexOf("["));
		if(sprId!=null&&!sprId.equals("")&&!userId.equals(sprId)){
			return false;
		}
		return true;
    }
}
