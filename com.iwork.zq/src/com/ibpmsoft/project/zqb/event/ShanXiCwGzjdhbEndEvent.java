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

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
public class ShanXiCwGzjdhbEndEvent extends ProcessTriggerEvent {
	
	private final static String CN_FILENAME = "/common.properties";
	private static Logger logger = Logger.getLogger(ShanXiCwGzjdhbEndEvent.class);
	public ShanXiCwGzjdhbEndEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
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
				
				String customername = dataMap2.get("CUSTOMERNAME").toString();
				StringBuffer content=new StringBuffer();
				content.append(customername).append("一般财务工作进度汇报审核通过！");
				String processActDefId = this.getActDefId();
				Long dataId=Long.parseLong(dataMap.get("ID").toString());
				dataMap.put("SPZT", "审批通过");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startDate = sdf.format(dataMap.get("TJSJ")==null?new Date():dataMap.get("TJSJ"));
				 dataMap.put("TJSJ", startDate);
				flag = ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				UserContext target = UserContextUtil.getInstance().getUserContext(dataMap.get("TBRID").toString());
				UserContext tg = UserContextUtil.getInstance().getCurrentUserContext();
				if (target != null && tg != null) {
					String senduser = tg.get_userModel().getUsername();
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile,content.toString());
					}
					String email = target.get_userModel().getEmail();
					if (email != null && !email.equals("")) {
						MessageAPI.getInstance().sendSysMail(senduser,email, "一般财务工作进度汇报审核", content.toString());
					}
				}
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
		sql.append("SELECT CWGW.ID,CWGW.CUSTOMERNAME,CWGW.INSTANCEID,NVL(CWGW.JDBH,0) JDBH,NVL(XMINFO.SORTID,0) SORTID,(SELECT SORTID FROM BD_ZQB_TYXM_INFO WHERE ID = ?) LCSORTID FROM BD_ZQB_CWGZJDHB GZJDHB INNER JOIN BD_ZQB_CWGWXMXX CWGW ON GZJDHB.XMBH=CWGW.XMBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON CWGW.JDBH=XMINFO.ID WHERE GZJDHB.XMBH = ?");
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
