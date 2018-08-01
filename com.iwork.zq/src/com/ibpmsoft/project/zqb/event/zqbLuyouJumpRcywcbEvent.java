package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.EventUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 日常业务呈报流程规则跳转触发第一步
 * @author zouyalei
 *
 */
public class zqbLuyouJumpRcywcbEvent extends SysJumpTriggerEvent {
	public zqbLuyouJumpRcywcbEvent(UserContext me, Task task) {
		super(me, task);
	}
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件   
	
	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		HashMap<String,Object>  hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		 String sxlx=hash.get("SXLX")==null?"":hash.get("SXLX").toString();
		 String gszq=this.getConfigUUID("zqServer")==null?"":this.getConfigUUID("zqServer").toString();
		 boolean flag=true;
		 if(!"".equals(sxlx)){
			 String[] sxls=sxlx.split(",");
			 for (int i = 0; i < sxls.length; i++) {
				 if(Integer.parseInt(sxls[i])>57 && Integer.parseInt(sxls[i])!=63){
					flag=false;
					break;
				}
			}
		 }
		 boolean flags=true;
		 String rcywtssp=this.getConfigUUID("RCYWTSSP")==null?"":this.getConfigUUID("RCYWTSSP").toString();
		 String[] rcyws=rcywtssp.split(",");
		 String creatuser  =hash.get("KHBH")==null?"":hash.get("KHBH").toString();
		 for (int i = 0; i < rcyws.length; i++) {
			 if(rcyws[i].equals(creatuser)){
					flags=false;
					break;
				}
		}
		 //获取当前客户编号
		 if(hash.get("KHBH")!=null&&!hash.get("KHBH").toString().equals("")){
			 conditionMap.put("KHBH", hash.get("KHBH").toString());
			 List<HashMap> list=DemAPI.getInstance().getList(this.getConfigUUID("cxddfpbduuid"), conditionMap, null);
			 if(list.size()==1){
				 HashMap hashMap = list.get(0);
				 boolean isActiveNextNodeFunction = EventUtil.isActiveNextNodeFunction();
				 boolean isZhongYinDmGglc = EventUtil.isZhongYinDmGglc();
				 if(isExists("QYNBRYSH")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "QYNBRYSH",SystemConfig._rcywcbLcConf.getJd1())&&!isSkip(hashMap, "QYNBRYSH",SystemConfig._rcywcbLcConf.getJd1()))){
					 return SystemConfig._rcywcbLcConf.getJd1();
				 }else if(isExists("KHFZR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "KHFZR",SystemConfig._rcywcbLcConf.getJd2())&&!isSkip(hashMap, "KHFZR",SystemConfig._rcywcbLcConf.getJd2()))){
					 return SystemConfig._rcywcbLcConf.getJd2();
				 }else if(!flag && !flags && "hlzq".equals(gszq)){
					 return SystemConfig._rcywcbLcConf.getJd12();
				 }else if(isExists("ZZCXDD")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "ZZCXDD",SystemConfig._rcywcbLcConf.getJd3())&&!isSkip(hashMap, "ZZCXDD",SystemConfig._rcywcbLcConf.getJd3()))){
					 return SystemConfig._rcywcbLcConf.getJd3();
				 }else if(isExists("FHSPR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "FHSPR",SystemConfig._rcywcbLcConf.getJd4())&&!isSkip(hashMap, "FHSPR",SystemConfig._rcywcbLcConf.getJd4()))){
					 return SystemConfig._rcywcbLcConf.getJd4();
				 }else if(isExists("ZZSPR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "ZZSPR",SystemConfig._rcywcbLcConf.getJd5())&&!isSkip(hashMap, "ZZSPR",SystemConfig._rcywcbLcConf.getJd5()))){
					 return SystemConfig._rcywcbLcConf.getJd5();
				 }else if(isZhongYinDmGglc&&isExists("CWSCBFZR2")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "CWSCBFZR2",SystemConfig._rcywcbLcConf.getJd6())&&!isSkip(hashMap, "CWSCBFZR2",SystemConfig._rcywcbLcConf.getJd6()))){
					 return SystemConfig._rcywcbLcConf.getJd6();
				 }else if(isZhongYinDmGglc&&isExists("CWSCBFZR3")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "CWSCBFZR3",SystemConfig._rcywcbLcConf.getJd7())&&!isSkip(hashMap, "CWSCBFZR3",SystemConfig._rcywcbLcConf.getJd7()))){
					 return SystemConfig._rcywcbLcConf.getJd7();
				 }else{
					 return SystemConfig._rcywcbLcConf.getEnd();
				 }
			 }else{
				 return SystemConfig._rcywcbLcConf.getEnd();
			 }
		}else{
			return SystemConfig._rcywcbLcConf.getEnd();
		}
	}
	
	public boolean isExists(String fieldName ){
		 HashMap<String,Object>  hash=ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 if(hash.get("KHBH")!=null&&!hash.get("KHBH").toString().equals("")){
			 HashMap<String,Object> conditionMap=new HashMap<String,Object>();
			 conditionMap.put("KHBH", hash.get("KHBH").toString());
			List<HashMap> list=DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", conditionMap, null);
			if(list.size()>0){
				for(HashMap map:list){
					if(map.get(fieldName)!=null&&!map.get(fieldName).toString().equals("")&&isDqspr(fieldName,hash,map)){
						return true;
					}
				}
			}
		 }
		 return false;
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
	public boolean isNextNode(HashMap map,String filedName,String nextStepId){
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		Long excutionId = this.getExcutionId();
		String actdefid = this.getActDefId();
		String forwardUSERID = EventUtil.getForwardUSERID(nextStepId, excutionId,actdefid);
		String sprId;
		if(forwardUSERID!=null&&!forwardUSERID.equals("")){
			sprId = forwardUSERID;
		}else{
			sprId = map.get(filedName).toString().substring(0,map.get(filedName).toString().indexOf("["));
		}
		
		if(sprId!=null&&!sprId.equals("")&&!userId.equals(sprId)){
			return false;
		}
		return true;
    }
	
	
	public boolean isSkip(HashMap map,String sprNode,String nextStepId){
		boolean flag=false;
		String userId = "";

		Long excutionId = this.getExcutionId();
		String actdefid = this.getActDefId();
		String forwardUSERID = EventUtil.getForwardUSERID(nextStepId, excutionId,actdefid);
		String sprId;
		if(forwardUSERID!=null&&!forwardUSERID.equals("")){
			sprId = forwardUSERID;
		}else{
			sprId = map.get(sprNode).toString().substring(0,map.get(sprNode).toString().indexOf("["));
		}
		
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
	
	public  List<UserContext> getNextUserList(){
		 List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		 HashMap<String,Object>  hash = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		 HashMap<String,Object> conditionMap=new HashMap<String,Object>();
		 String gszq=this.getConfigUUID("zqServer")==null?"":this.getConfigUUID("zqServer").toString();
		 String sxlx=hash.get("SXLX")==null?"":hash.get("SXLX").toString();
		 String[] sxls=sxlx.split(",");
		 boolean flag=true;
		 if(!"".equals(sxlx)){
		 for (int i = 0; i < sxls.length; i++) {
			if(Integer.parseInt(sxls[i])>57 && Integer.parseInt(sxls[i])!=63){
				flag=false;
				break;
			}
		}}
		 boolean flags=true;
		 String rcywtssp=this.getConfigUUID("RCYWTSSP")==null?"":this.getConfigUUID("RCYWTSSP").toString();
		 String[] rcyws=rcywtssp.split(",");
		 String creatuser  =hash.get("KHBH")==null?"":hash.get("KHBH").toString();
		 for (int i = 0; i < rcyws.length; i++) {
			 if(rcyws[i].equals(creatuser)){
					flags=false;
					break;
				}
		}
		 //获取当前客户编号
		 if(hash.get("KHBH")!=null&&!hash.get("KHBH").toString().equals("")){
			 String qcrid = hash.get("CREATEUSER").toString();
			 conditionMap.put("KHBH", hash.get("KHBH").toString());
			 List<HashMap> list=DemAPI.getInstance().getList(this.getConfigUUID("cxddfpbduuid"), conditionMap, null);
			 if(list.size()==1){
				 HashMap hashMap = list.get(0);
				 boolean isActiveNextNodeFunction = EventUtil.isActiveNextNodeFunction();
				 boolean isZhongYinDmGglc = EventUtil.isZhongYinDmGglc();
				 String nextStepId = this.getNextStepId();
				 Long excutionId = this.getExcutionId();
				 String actdefid = this.getActDefId();
				 if(isExists("QYNBRYSH")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "QYNBRYSH",nextStepId)&&!isSkip(hashMap, "QYNBRYSH",nextStepId))){
					 String collocationUSERID = list.get(0).get("QYNBRYSH").toString().substring(0, list.get(0).get("QYNBRYSH").toString().indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
				 }else if(isExists("KHFZR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "KHFZR",nextStepId)&&!isSkip(hashMap, "KHFZR",nextStepId))){
					 String collocationUSERID = list.get(0).get("KHFZR").toString().substring(0, list.get(0).get("KHFZR").toString().indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
				 }else if(!flag && !flags && "hlzq".equals(gszq)){
					 String hqry=this.getConfigUUID("RCYWTSSPR")==null?"":this.getConfigUUID("RCYWTSSPR").toString();
					 String collocationUSERID = hqry.substring(0, hqry.indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
				 }else if(isExists("ZZCXDD")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "ZZCXDD",nextStepId)&&!isSkip(hashMap, "ZZCXDD",nextStepId))){
					 String collocationUSERID = list.get(0).get("ZZCXDD").toString().substring(0, list.get(0).get("ZZCXDD").toString().indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
				 }else  if(isExists("FHSPR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "FHSPR",nextStepId)&&!isSkip(hashMap, "FHSPR",nextStepId))){
					 String collocationUSERID = list.get(0).get("FHSPR").toString().substring(0,list.get(0).get("FHSPR").toString().indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
				 }else if(isExists("ZZSPR")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "ZZSPR",nextStepId)&&!isSkip(hashMap, "ZZSPR",nextStepId))){
					 String collocationUSERID = list.get(0).get("ZZSPR").toString().substring(0,list.get(0).get("ZZSPR").toString().indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
				 }else if(isZhongYinDmGglc&&isExists("CWSCBFZR2")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "CWSCBFZR2",nextStepId)&&!isSkip(hashMap, "CWSCBFZR2",nextStepId))){
					 String collocationUSERID = list.get(0).get("CWSCBFZR2").toString().substring(0,list.get(0).get("CWSCBFZR2").toString().indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
				 }else if(isZhongYinDmGglc&&isExists("CWSCBFZR3")&&(!isActiveNextNodeFunction?true:!isNextNode(hashMap, "CWSCBFZR3",nextStepId)&&!isSkip(hashMap, "CWSCBFZR3",nextStepId))){
					 String collocationUSERID = list.get(0).get("CWSCBFZR3").toString().substring(0,list.get(0).get("CWSCBFZR3").toString().indexOf("["));
					 uclist = EventUtil.getUcList(uclist, collocationUSERID,nextStepId,excutionId,actdefid);
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


}
