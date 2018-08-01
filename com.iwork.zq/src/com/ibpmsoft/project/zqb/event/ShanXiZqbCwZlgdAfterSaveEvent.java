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
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
/**
 * 股改流程保存后触发
 * 
 */
public class ShanXiZqbCwZlgdAfterSaveEvent extends ProcessStepTriggerEvent {

	private final static String CN_FILENAME = "/common.properties";
	private static Logger logger = Logger.getLogger(ShanXiZqbCwZlgdAfterSaveEvent.class);
	public ShanXiZqbCwZlgdAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean updateFormData = false;
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(dataMap!=null){
			//重置提交时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
			dataMap.put("TJSJ", startDate);
			
			
			String xmbhforupdate = dataMap.get("XMBH").toString();
			HashMap<String,String> dataMapMain = getDataMap(xmbhforupdate);
			//立项
			Object lxzl1 = dataMap.get("LXFJUUID");
			String lxzl2 = dataMapMain.get("LXFJUUID");
			boolean xmggzlFlag=false;
			if(lxzl1!=null&&!lxzl1.toString().equals(lxzl2)){
				Long lcggInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_CWXMLX WHERE XMBH='"+xmbhforupdate+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(lcggInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					processData.put("FJ", lxzl1.toString());
					Long lcggId = Long.parseLong(processData.get("ID").toString());
					processData.put("TJSJ", startDate);
					updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), lcggInsId, processData, lcggId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			//工作进度汇报
			Object gzjdhb1 = dataMap.get("HBFJUUID");
			String gzjdhb2 = dataMapMain.get("HBFJUUID");
			if(gzjdhb1!=null&&!gzjdhb1.toString().equals(gzjdhb2)){
				Long xmnhInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_CWGZJDHB WHERE XMBH='"+xmbhforupdate+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(xmnhInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					processData.put("FJ", gzjdhb1.toString());
					Long xmnhId = Long.parseLong(processData.get("ID").toString());
					processData.put("TJSJ", startDate);
					updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), xmnhInsId, processData, xmnhId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			
			
			String xmbh = dataMap.get("XMBH").toString();
			Long jdbh = Long.parseLong(dataMap.get("JDBH").toString());
			HashMap<String,Object> dataMap2 = getDataMap(xmbh,jdbh);
			if(!dataMap2.isEmpty()){
				String cwgwxxUUID = getConfigUUID("cwgwxxuuid");
				Long sortid = Long.parseLong(dataMap2.get("SORTID").toString());
				Long lcsortid = Long.parseLong(dataMap2.get("LCSORTID").toString());
				if(lcsortid>sortid){
					HashMap<String,String> conditionMap = new HashMap<String,String>();
					conditionMap.put("XMBH", xmbh);
					List<HashMap> xmlxList = DemAPI.getInstance().getList(cwgwxxUUID, conditionMap, null);
					if (xmlxList != null && xmlxList.size() == 1) {
						HashMap hash = xmlxList.get(0);
						Long instanceid = Long.parseLong(dataMap2.get("INSTANCEID").toString());
						Long dataId = Long.parseLong(hash.get("ID").toString());
						hash.put("JDBH", jdbh);
						flag = DemAPI.getInstance().updateFormData(cwgwxxUUID,instanceid,hash, dataId,false);
					}
				}
				String processActDefId = this.getActDefId();
				Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
				String taskName = newTaskId.getName()!=null?newTaskId.getName().equals("起草")?"呈报中":newTaskId.getName()+"审批中":"";
				Long lcDataId=Long.parseLong(dataMap.get("ID").toString());
				dataMap.put("LCBH", processActDefId);
				dataMap.put("LCBS", instanceId);
				dataMap.put("TASKID", newTaskId.getId());
				dataMap.put("SPZT", taskName);
				flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, lcDataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
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
	
	public HashMap<String,String> getDataMap(String xmbh){
		StringBuffer sql = new StringBuffer();
		HashMap<String,String> result = new HashMap<String,String>();
		sql.append("SELECT LX.FJ LXFJUUID,HB.FJ HBFJUUID FROM (SELECT XMBH FROM BD_ZQB_CWGWXMXX WHERE XMBH=?) GP");
		sql.append(" INNER JOIN BD_ZQB_BGZZLXXX MAINTB ON GP.XMBH=MAINTB.XMBH");
		sql.append(" LEFT JOIN BD_ZQB_CWXMLX LX ON GP.XMBH=LX.XMBH");
		sql.append(" LEFT JOIN BD_ZQB_CWGZJDHB HB ON GP.XMBH=HB.XMBH");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				String lxfjuuid = rs.getString("LXFJUUID");
				String hbfjuuid = rs.getString("HBFJUUID");
				result.put("LXFJUUID", lxfjuuid);
				result.put("HBFJUUID", hbfjuuid);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
}
