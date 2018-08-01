package com.ibpmsoft.project.zqb.trigger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;

/**
 * 项目基本信息添加
 * 
 * @author David
 * 
 */
public class ZQB_PjSettlementSheetAddEvent extends DemTriggerEvent {
	private String ProjectUUID = "33833384d109463285a6a348813539f1";
	public ZQB_PjSettlementSheetAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String xmlx = hash.get("XMLX").toString();
		BigDecimal rzje=new BigDecimal(map.get("RZJE")!=null&&!map.get("RZJE").equals("")?map.get("RZJE").toString():"0");
		Long dataId = Long.parseLong(hash.get("ID").toString());
		String customername = hash.get("CUSTOMERNAME").toString();
		Long instanceId = Long.parseLong(map.get("instanceId").toString());
		if(instanceId==0){
			String khbh = DBUtil.getString("SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNAME='"+hash.get("CUSTOMERNAME").toString()+"'", "CUSTOMERNO");
			if((hash.get("CUSTOMERNO")==null||hash.get("CUSTOMERNO").toString().equals(""))&&(khbh==null||khbh.equals(""))){
				addCustomer(map);
			}else{
				DBUtil.executeUpdate("UPDATE BD_ZQB_XMJSXXGLB SET CUSTOMERNO='"+khbh+"' WHERE CUSTOMERNAME='"+hash.get("CUSTOMERNAME").toString()+"'");
			}
			if("推荐挂牌".equals(xmlx)){
				String customerno = hash.get("CUSTOMERNO").toString().equals("")?khbh:hash.get("CUSTOMERNO").toString();
				HashMap valueMap=new HashMap();
				valueMap.put("CUSTOMERNO", customerno);
				List<HashMap> pjList = DemAPI.getInstance().getList(ProjectUUID, valueMap, null);
				if(!pjList.isEmpty()){
					String projectno = pjList.get(0).get("PROJECTNO").toString();
					HashMap<String,String> conditionMap= new HashMap<String,String>();
					conditionMap.put("PROJECTNO", projectno);
					boolean updateFormData = false;
					List<HashMap> list = DemAPI.getInstance().getList("b25ca8ed0a5a484296f2977b50db8396", conditionMap, "ID");
					for (HashMap hashMap : list) {
						Object object = hashMap.get("SSJE");
						if(object==null||object.toString().equals("")){
							hashMap.put("SSJE",rzje);
							Long dataid = Long.parseLong(hashMap.get("ID").toString());
							Long instaceId = DemAPI.getInstance().getInstaceId("BD_PM_TASK", dataid);
							updateFormData = DemAPI.getInstance().updateFormData("b25ca8ed0a5a484296f2977b50db8396", instaceId, hashMap, dataid, false);
							break;
						}
					}
					if(!updateFormData){
						conditionMap= new HashMap<String,String>();
						conditionMap.put("PROJECTNO", projectno);
						list = DemAPI.getInstance().getList("b25ca8ed0a5a484296f2977b50db8396", conditionMap, "ID DESC");
						if(!list.isEmpty()){
							HashMap<String,Object> hashMap = list.get(0);
							BigDecimal ssje=new BigDecimal(hashMap.get("SSJE").toString());
							BigDecimal add = ssje.add(rzje);
							hashMap.put("SSJE",add);
							Long dataid = Long.parseLong(hashMap.get("ID").toString());
							Long instaceId = DemAPI.getInstance().getInstaceId("BD_PM_TASK", dataid);
							updateFormData = DemAPI.getInstance().updateFormData("b25ca8ed0a5a484296f2977b50db8396", instaceId, hashMap, dataid, false);
						}
					}
				}
			}
		}
		String string = map.get("instanceId").toString();
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "项目结算信息管理维护", customername+",更新项目结算信息,项目类型:"+xmlx+",入账金额:"+rzje+"。");
		}else{
			LogUtil.getInstance().addLog(dataId, "项目结算信息管理维护", customername+",添加项目结算信息,项目类型:"+xmlx+",入账金额:"+rzje+"。");
		}
		return true;
	}
	private void addCustomer(HashMap dataMap){
		String CustomerUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='客户主数据维护'", "UUID");
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceid = DemAPI.getInstance().newInstance(CustomerUUID, uc.get_userModel().getUserid());
		/* CUSTOMERNO*****
		 * 公司全称CUSTOMERNAME、
		 * 客户状态（有效）STATUS、
		 * 填报人CREATEUSER(uc.get_userModel().getUsername())、
		 * 填报日期CREATEDATE、
		 * 客户联系人USERNAME（董秘）
		 * 已挂牌YGP（否），
		 * 持续督导变更CXDDBG（未变更过）
		 */
		Date date = new Date();
		String yyyyMM = new SimpleDateFormat("yyyy-MM-").format(date);
		int no = DBUtil.getInt("SELECT DISTINCT MAX(TO_NUMBER(SUBSTR(CUSTOMERNO,INSTR(CUSTOMERNO,'-',-1)+1)))+1 NO FROM BD_ZQB_KH_BASE", "NO");
		String customerno="CNO"+yyyyMM+no;
		String createdate=new SimpleDateFormat("yyyy-MM-dd").format(date);
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		HashMap<String,Object> hashdata = new HashMap<String,Object>();
		hashdata.put("CUSTOMERNAME", dataMap.get("CUSTOMERNAME"));
		hashdata.put("CUSTOMERNO", customerno);
		hashdata.put("STATUS", "有效");
		hashdata.put("CREATEUSER", uc.get_userModel().getUsername());
		hashdata.put("CREATEDATE", createdate);
		hashdata.put("USERNAME", "董秘");
		hashdata.put("YGP", "未挂牌");
		hashdata.put("CXDDBG", "未变更过");
		hashdata.put("USERID", userid);
		DemAPI.getInstance().saveFormData(CustomerUUID, instanceid, hashdata, false);
		DBUtil.executeUpdate("UPDATE BD_ZQB_XMJSXXGLB BZX SET BZX.CUSTOMERNO='"+customerno+"' where CUSTOMERNAME='"+dataMap.get("CUSTOMERNAME")+"'");
	}
}
