package com.ibpmsoft.project.zqb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;


public class EventUtil {
	
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	
	public static boolean isActiveNextNodeFunction(){
		String configParameter = ConfigUtil.readAllProperties(CN_FILENAME).get("isActiveNextNodeFunction");
		if(configParameter==null){
			return true;
		}else{
			if(configParameter.equals("0")){
				return false;
			}else{
				return true;
			}
		}
    }
	public static boolean isZhongYinDmGglc(){
		String configParameter = ConfigUtil.readAllProperties(CN_FILENAME).get("isZhongYinDmGglc");
		if(configParameter==null){
			return true;
		}else{
			if(configParameter.equals("0")){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public static String getIsTjfb(String key){
		String configParameter = ConfigUtil.readAllProperties(CN_FILENAME).get(key);
		if(configParameter==null){
			configParameter="";
		}
		return configParameter;
	}
	
	public static void updateMainData(HashMap<String, Object> dataMap) {
		List lables = new ArrayList();lables.add("ID");lables.add("INSTANCEID");
		Map<Integer,String> params = new HashMap<Integer,String>();params.put(1,dataMap.get("PROJECTNO").toString());
		List<HashMap> l = DBUtil.getDataList(lables, "SELECT B.ID,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.PROJECTNO=?", params);
		String demUUID = DBUtil.getDataStr("UUID", "SELECT * FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目信息'", null);
		HashMap h = DemAPI.getInstance().getFromData(Long.parseLong(l.get(0).get("INSTANCEID").toString()));
		h.put("GPFXSL",dataMap.get("GPFXSL")==null?"":dataMap.get("GPFXSL").toString());
		h.put("NFXRQ",dataMap.get("NFXRQ")==null?"":dataMap.get("NFXRQ").toString());
		h.put("MJZJZE",dataMap.get("MJZJZE")==null?"":dataMap.get("MJZJZE").toString());
		h.put("FXMDCS",dataMap.get("FXMDCS")==null?"":dataMap.get("FXMDCS").toString());
		h.put("FXGPJG",dataMap.get("FXGPJG")==null?"":dataMap.get("FXGPJG").toString());
		DemAPI.getInstance().updateFormData(demUUID, Long.parseLong(l.get(0).get("INSTANCEID").toString()), h, Long.parseLong(l.get(0).get("ID").toString()), false);
	}
	
	public static void updateMainData4(HashMap<String, Object> dataMap) {
		List lables = new ArrayList();lables.add("ID");lables.add("INSTANCEID");
		Map<Integer,String> params = new HashMap<Integer,String>();params.put(1,dataMap.get("PROJECTNO").toString());
		List<HashMap> l = DBUtil.getDataList(lables, "SELECT B.ID,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID = S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目信息') AND B.PROJECTNO=?", params);
		String demUUID = DBUtil.getDataStr("UUID", "SELECT * FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目信息'", null);
		HashMap h = DemAPI.getInstance().getFromData(Long.parseLong(l.get(0).get("INSTANCEID").toString()));
		h.put("SJGPFXSL",dataMap.get("SJGPFXSL")==null?"":dataMap.get("SJGPFXSL").toString());
		h.put("FXRQ",dataMap.get("FXRQ")==null?"":dataMap.get("FXRQ").toString());
		h.put("SYL",dataMap.get("SYL")==null?"":dataMap.get("SYL").toString());
		h.put("SJFXZE",dataMap.get("SJFXZE")==null?"":dataMap.get("SJFXZE").toString());
		h.put("RSSFCG",dataMap.get("RSSFCG")==null?"":dataMap.get("RSSFCG").toString());
		h.put("SJFXGPJG",dataMap.get("SJFXGPJG")==null?"":dataMap.get("SJFXGPJG").toString());
		h.put("SFZYXS",dataMap.get("SFZYXS")==null?"SFZYXS":dataMap.get("SFZYXS").toString());
		h.put("FXZT",dataMap.get("FXZT")==null?"FXZT":dataMap.get("FXZT").toString());
		DemAPI.getInstance().updateFormData(demUUID, Long.parseLong(l.get(0).get("INSTANCEID").toString()), h, Long.parseLong(l.get(0).get("ID").toString()), false);
	}
	
	public static Boolean checkUser(String username){
		Boolean flag=false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String name = uc._userModel.getUserid() + "[" + uc._userModel.getUsername() + "]";
		if(username.equals(name)){
			flag=true;
		}
		return flag;
	}
	public static String getForwardUSERID(String nextStepId,Long excutionId,String actdefid){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.FORWARDUSERID,B.RN FROM (");//'"+actdefid+"'  '"+nextStepId+"'  '"+excutionId+"'
		sql.append("SELECT A.FORWARDUSERID,ROWNUM RN FROM (");
		sql.append("SELECT ASSIGNEE_ AS FORWARDUSERID FROM PROCESS_HI_TASKINST WHERE 1=1 AND PROC_DEF_ID_= ? AND TASK_DEF_KEY_= ? AND EXECUTION_ID_= ? AND NAME_ LIKE '%转发%' ORDER BY TO_NUMBER(ID_) DESC");
		sql.append(")A");
		sql.append(")B WHERE B.RN=1");
		Map params = new HashMap();
		params.put(1, actdefid);
		params.put(2, nextStepId);
		params.put(3, excutionId);
		//return com.iwork.core.db.DB(sql.toString(), "FORWARDUSERID");
		return com.iwork.commons.util.DBUTilNew.getDataStr("FORWARDUSERID", sql.toString(), params);
	}
	
	public static List<UserContext> getUcList(List<UserContext> uclist, String collocationUSERID,String nextStepId,Long excutionId,String actdefid) {
		 String forwardUSERID = EventUtil.getForwardUSERID(nextStepId, excutionId, actdefid);
		 UserContext uc;
		 if(forwardUSERID!=null&&!forwardUSERID.equals("")){
			 uc = UserContextUtil.getInstance().getUserContext(forwardUSERID);
		 }else{
			 uc = UserContextUtil.getInstance().getUserContext(collocationUSERID);
		 }
		 uclist.add(uc);
		 return uclist;
	}
}
