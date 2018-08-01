package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class TzggHfxxAfterSaveEvent extends DemTriggerEvent {
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件	
	public TzggHfxxAfterSaveEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}
	
	/**
	 * 执行触发器
	 */
	public boolean execute() { 		
		
		HashMap formData = this.getFormData();
		HashMap hash = ParameterMapUtil.getParameterMap(formData);
		if( hash.get("GGID")==null || "".equals(hash.get("GGID"))){
			return false;
			}
		boolean saveFormData=false;		
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("hfxxbuuid");
		String hfid = DBUtil.getString(
				"select DATAID from  SYS_ENGINE_FORM_BIND where formid="
						+ config.get("hfxxbformid")
						+ " and instanceid=" + this.getInstanceId()
						+ "", "DATAID");
		hash.put("HFQKID", hfid);
		HashMap<String, Object> datemap=new HashMap();
		datemap.put("GGID",hash.get("GGID").toString());
		datemap.put("HFR",hash.get("HFR").toString());
		
		List<HashMap> map1 = DemAPI.getInstance().getAllList(demUUID, datemap, null);
		if(map1.size()>0){
			 saveFormData = DemAPI.getInstance().updateFormData(demUUID, this.getInstanceId(), hash, Long.parseLong(hfid), false); 
		}
		
		String sql = "update Bd_Xp_Hfqkb set status='已回复',hfid=" + hfid
				+ ",hfsj=sysdate where (userid='"+hash.get("HFRID")+"') and ggid=" + hash.get("GGID") + "";
		
			int updateInt = DBUtil.executeUpdate(sql);
		
		return saveFormData;
	}
}