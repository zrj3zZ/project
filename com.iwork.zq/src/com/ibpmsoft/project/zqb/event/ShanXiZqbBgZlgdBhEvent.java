package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class ShanXiZqbBgZlgdBhEvent extends ProcessStepTriggerEvent {
	
	private final static String CN_FILENAME = "/common.properties";
	private static Logger logger = Logger.getLogger(ShanXiZqbBgZlgdBhEvent.class);
	public ShanXiZqbBgZlgdBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			
			//重置提交时间格式
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			dataMap.put("TJSJ", startDate);
			
			String xmbh = dataMap.get("XMBH").toString();
			Long jdbh = Long.parseLong(dataMap.get("JDBH").toString());
			HashMap<String,Object> dataMap2 = getDataMap(xmbh,jdbh);
			if(!dataMap2.isEmpty()){
				Long dataId=Long.parseLong(dataMap.get("ID").toString());
				String jyf = dataMap2.get("JYF").toString();
				
				String processActDefId = this.getActDefId();
				Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
				Long lcDataId = Long.parseLong(dataMap.get("ID").toString());
				dataMap.put("LCBH", processActDefId);
				dataMap.put("LCBS", instanceId);
				dataMap.put("TASKID", newTaskId.getId());
				dataMap.put("SPZT", newTaskId.getName());
				flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				
				/*String smsContent = jyf+"并购资料归档流程被驳回！";
				LogUtil.getInstance().addLog(dataId, "并购资料归档审核流程", smsContent);
				String userid = zqbLcBhHQBHREvent.getUsername();
				UserContext target = UserContextUtil.getInstance().getUserContext(userid);
				UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
				if (target != null && tg != null) {
					String senduser = tg.get_userModel().getUsername();
					if (!smsContent.equals("")) {
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
						}
						String email = target.get_userModel().getEmail();
						if (email != null && !email.equals("")) {
							MessageAPI.getInstance().sendSysMail(senduser,email, "并购资料归档", smsContent);
						}
					}
				}*/
			}
		}
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
	
	public HashMap<String,Object> getDataMap(String xmbh,Long dqjdbh){
		StringBuffer sql = new StringBuffer();
		HashMap<String,Object> result = new HashMap<String,Object>();
		sql.append("SELECT BGCZ.ID,BGCZ.JYF,BGCZ.INSTANCEID,NVL(BGCZ.JDBH,0) JDBH,NVL(XMINFO.SORTID,0) SORTID,(SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID = ?) LCSORTID FROM BD_ZQB_BGZLGD BGZLGD INNER JOIN BD_ZQB_BGZZLXXX BGCZ ON BGZLGD.XMBH=BGCZ.XMBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON BGCZ.JDBH=XMINFO.ID WHERE BGZLGD.XMBH = ?");
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
				String jyf = rs.getString("JYF");
				Long instanceid = rs.getLong("INSTANCEID");
				Long jdbh = rs.getLong("JDBH");
				Long sortid = rs.getLong("SORTID");
				Long lcsortid = rs.getLong("LCSORTID");
				result.put("DATAID", id);
				result.put("JDBH", jdbh);
				result.put("INSTANCEID", instanceid);
				result.put("JYF", jyf);
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
