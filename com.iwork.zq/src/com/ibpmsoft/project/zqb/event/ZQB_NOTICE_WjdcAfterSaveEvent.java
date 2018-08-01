package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.Map;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;

public class ZQB_NOTICE_WjdcAfterSaveEvent extends DemTriggerEvent {

	public ZQB_NOTICE_WjdcAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute(){
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		String tzggid = map.get("TZGGID").toString();
		String userid = map.get("USERID").toString();
		int num = DBUtil.executeUpdate("UPDATE BD_XP_TZGGB SET SFYHF=1 WHERE ID="+tzggid);
		
		int count = DBUtil.executeUpdate("update BD_XP_HFQKB set status='已回复',hfsj=sysdate where ggid="+tzggid+" and userid='"+userid+"'");
		
		
		Map<String, String> config = ConfigUtil.readAllProperties("/common.properties");// 获取连接网址配置
		String demUUID = config.get("hfxxbuuid");
		Long insid = DemAPI.getInstance().newInstance(demUUID, userid);
		HashMap hashdata = new HashMap();
		hashdata.put("INSTANCEID", insid);
		hashdata.put("COMPANYNO", "");
		hashdata.put("COMPANYNAME", "");
		hashdata.put("CONTENT", "");
		hashdata.put("XGZL", "");
		hashdata.put("HFR", UserContextUtil.getInstance().getUserContext(userid).get_userModel().getUsername());
		hashdata.put("GGID", tzggid);
		hashdata.put("HFRID", userid);
		hashdata.put("HFQKID", DBUtil.getString("SELECT ID FROM BD_XP_HFQKB WHERE GGID="+tzggid+" AND USERID='"+userid+"'", "ID"));
		DemAPI.getInstance().saveFormData(demUUID, insid, hashdata, false);
		
		DBUtil.executeUpdate("update BD_XP_HFQKB set hfid="+DemAPI.getInstance().getFromData(insid).get("ID")+" where ggid="+tzggid+" and userid='"+userid+"'");
		return true;
	}
}

