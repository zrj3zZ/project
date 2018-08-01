package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
/**
 * 项目任务保存后触发
 * 
 * @author zouyalei
 * 
 */
public class ShanXiZqbCwZlgdAfterCommitEvent extends ProcessStepTriggerEvent {
	
	private final static String CN_FILENAME = "/common.properties";
	private static Logger logger = Logger.getLogger(ShanXiZqbCwZlgdAfterCommitEvent.class);
	public ShanXiZqbCwZlgdAfterCommitEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		//获取登录人信息
		String TJRID = UserContextUtil.getInstance().getCurrentUserId();
		UserContext DLUser = UserContextUtil.getInstance().getCurrentUserContext();
		String TJRXM = DLUser._userModel.getUsername(); 
		//获取提交时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(dataMap!=null){
			String xmbh = dataMap.get("XMBH").toString();
			Long jdbh = Long.parseLong(dataMap.get("JDBH").toString());
			HashMap<String,Object> dataMap2 = getDataMap(xmbh,jdbh);
			if(!dataMap2.isEmpty()){
				Long sortid = Long.parseLong(dataMap2.get("SORTID").toString());
				Long lcsortid = Long.parseLong(dataMap2.get("LCSORTID").toString());
				if(lcsortid>sortid){
					String cwgwxxUUID = getConfigUUID("cwgwxxuuid");
					Long instanceid = Long.parseLong(dataMap2.get("INSTANCEID").toString());
					HashMap<String, Object> conditionMap = new HashMap<String, Object>();
					conditionMap.put("XMBH", xmbh);
					List<HashMap> list = DemAPI.getInstance().getList(cwgwxxUUID,conditionMap, null);
					if(list!=null&&!list.isEmpty()){
						HashMap hashMap = list.get(0);
						hashMap.put("JDBH", jdbh);
						flag = DemAPI.getInstance().updateFormData(cwgwxxUUID, instanceid, hashMap,Long.parseLong(hashMap.get("ID").toString()), false);
					}
				}
				
				String processActDefId = this.getActDefId();
				String customername = dataMap2.get("CUSTOMERNAME").toString();
				StringBuffer content=new StringBuffer();
				content.append(customername).append("已提交一般财务资料归档，请审核!");
				
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
				String taskName = newTaskId.getName()!=null?newTaskId.getName().equals("起草")?"呈报中":"审批中":"";
				Long lcDataId = Long.parseLong(dataMap.get("ID").toString());
				dataMap.put("LCBH", processActDefId);
				dataMap.put("LCBS", instanceId);
				dataMap.put("TASKID", newTaskId.getId());
				dataMap.put("SPZT", UserContextUtil.getInstance().getUserContext(newTaskId.getAssignee())._userModel.getUsername()+taskName);
				dataMap.put("TJSJ", sdf.format(date));
				flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				
				UserContext ut = UserContextUtil.getInstance().getCurrentUserContext();
				String assignee = newTaskId.getAssignee();
				UserContext target = UserContextUtil.getInstance().getUserContext(assignee);
				if (target != null) {
					if (!content.equals("")) {
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
							MessageAPI.getInstance().sendSMS(uc, mobile, content.toString());
						}
						String email = target.get_userModel().getEmail();
						if (email != null && !email.equals("") && ut != null) {
							String senduser = ut.get_userModel().getUsername();
							MessageAPI.getInstance().sendSysMail(senduser, email, "一般财务资料归档审核", content.toString());
						}
					}
				}
			}
		}
		Long dataId=Long.parseLong(dataMap.get("ID").toString());
		String processActDefId = this.getActDefId();
		//查询客户名称
		String customno = (String) dataMap.get("XMBH");
		String manager = DBUtil.getString("SELECT CUSTOMERNAME FROM BD_ZQB_CWGWXMXX WHERE XMBH='"+customno+"'", "CUSTOMERNAME");
		dataMap.put("COMPANYNAME",manager);
		dataMap.put("TJRID", TJRID);
		dataMap.put("TJRXM", TJRXM);
		dataMap.put("TJSJ", sdf.format(date));
		flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		
		return flag;
	}
	

	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	
	private void deleteData(String processActDefId, String actStepId, String userid,Long instanceId) {
		StringBuffer sql = new StringBuffer("DELETE FROM PROCESS_RU_OPINION WHERE");
		sql.append(" ACT_DEF_ID = '").append(processActDefId).append("'");
		sql.append(" AND ACTION = '规则跳转[*]'");
		sql.append(" AND ACT_STEP_ID = '").append(actStepId).append("'");
		sql.append(" AND CREATEUSER = '").append(userid).append("'");
		sql.append(" AND INSTANCEID = ").append(instanceId);
		DBUtil.executeUpdate(sql.toString());
	}
	
	public HashMap<String,Object> getDataMap(String xmbh,Long dqjdbh){
		StringBuffer sql = new StringBuffer();
		HashMap<String,Object> result = new HashMap<String,Object>();
		sql.append("SELECT CWGW.ID,CWGW.CUSTOMERNAME,CWGW.INSTANCEID,NVL(CWGW.JDBH,0) JDBH,NVL(XMINFO.SORTID,0) SORTID,(SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID = ?) LCSORTID FROM BD_ZQB_CWZLGD ZLGD INNER JOIN BD_ZQB_CWGWXMXX CWGW ON ZLGD.XMBH=CWGW.XMBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON CWGW.JDBH=XMINFO.ID WHERE ZLGD.XMBH = ?");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, dqjdbh);
			ps.setString(2, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long id = rs.getLong("ID");
				String customername = rs.getString("CUSTOMERNAME");
				Long instanceid = rs.getLong("INSTANCEID");
				Long jdbh = rs.getLong("JDBH");
				Long sortid = rs.getLong("SORTID");
				Long lcsortid = rs.getLong("LCSORTID");
				result.put("DATAID", id);
				result.put("JDBH", jdbh);
				result.put("INSTANCEID", instanceid);
				result.put("CUSTOMERNAME", customername);
				result.put("SORTID", sortid);
				result.put("LCSORTID", lcsortid);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
}
