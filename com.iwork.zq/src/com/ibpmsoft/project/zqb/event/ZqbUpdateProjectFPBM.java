package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.core.db.DBUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
public class ZqbUpdateProjectFPBM implements IWorkScheduleInterface {
	
	private static final String PROJECT_UUID = "33833384d109463285a6a348813539f1";
	private static Logger logger = Logger.getLogger(ZqbUpdateProjectFPBM.class);
	public boolean executeAfter() throws ScheduleException {
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		UpdateGGToDB();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		return true;
	}

	public void UpdateGGToDB() {
		List<HashMap<String,Long>> list = getGG();
		for (HashMap<String, Long> hashMap : list) {
			Long instanceId=Long.parseLong(hashMap.get("LCBS").toString());
			Long dataInstanceid=Long.parseLong(hashMap.get("INSTANCEID").toString());
			List<HashMap> lcFromSubXmcyData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMCYLB");
			List<HashMap> lcFromSubClrData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLR");
			List<HashMap> lcFromSubCljgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_CLJG");
			List<HashMap> lcFromSubZjjgData = ProcessAPI.getInstance().getFromSubData(instanceId, "SUBFORM_XMZJJG");

			Long demId = DemAPI.getInstance().getDemModel(PROJECT_UUID).getId();
			
			List<HashMap> fromSubXmcyData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMCYLB");
			setFromSubData(demId,fromSubXmcyData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMCYLB",lcFromSubXmcyData);
			List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLR");
			setFromSubData(demId,fromSubClrData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLR",lcFromSubClrData);
			List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_CLJG");
			setFromSubData(demId,fromSubCljgData,PROJECT_UUID,dataInstanceid,"SUBFORM_CLJG",lcFromSubCljgData);
			List<HashMap> fromSubZjjgData = DemAPI.getInstance().getFromSubData(dataInstanceid, "SUBFORM_XMZJJG");
			setFromSubData(demId,fromSubZjjgData,PROJECT_UUID,dataInstanceid,"SUBFORM_XMZJJG",lcFromSubZjjgData);
		}
	}
	
	public List<HashMap<String,Long>> getGG(){
		StringBuffer sb = new StringBuffer("SELECT PJ.ID,BIND.INSTANCEID,PJ.LCBS FROM BD_ZQB_PJ_BASE PJ INNER JOIN SYS_ENGINE_FORM_BIND BIND ON FORMID=91 AND METADATAID=101 AND PJ.ID=BIND.DATAID WHERE PJ.LCBS IS NOT NULL");
		String sql = sb.toString();
		List<HashMap<String,Long>> list = new ArrayList<HashMap<String,Long>>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				HashMap<String,Long> map;
				map = new HashMap<String,Long>();
				Long id = rs.getLong("ID");
				Long instanceid = rs.getLong("INSTANCEID");
				Long lcbs = rs.getLong("LCBS");
				map.put("ID", id);
				map.put("INSTANCEID", instanceid);
				map.put("LCBS", lcbs);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		} 
		return list;
	}
	
	private void setFromSubData(Long demId, List<HashMap> getFromSubData,
			String demUUID, Long dataInstanceid, String subFromKey,
			List<HashMap> saveFromSubData) {
		if(getFromSubData!=null){
			if(getFromSubData.size()>0){
				for (HashMap hashMap : getFromSubData) {
					DemAPI.getInstance().removeSubFormData(PROJECT_UUID, dataInstanceid, Long.parseLong(hashMap.get("ID").toString()), subFromKey);
				}
				if(saveFromSubData!=null){
					DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
				}
			}else{
				if(saveFromSubData!=null){
					DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
				}
			}
		}else{
			if(saveFromSubData!=null){
				DemAPI.getInstance().saveFormDatas(demId, dataInstanceid, subFromKey, saveFromSubData, false);
			}
		}
	}
}


