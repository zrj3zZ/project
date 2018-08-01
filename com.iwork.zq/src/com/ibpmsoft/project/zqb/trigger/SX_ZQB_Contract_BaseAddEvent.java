package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class SX_ZQB_Contract_BaseAddEvent  extends DemTriggerEvent{
	
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static String XMLXUUID = "33833384d109463285a6a348813539f1";
	
	public SX_ZQB_Contract_BaseAddEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		if (map != null) {
			HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
			Long dataId = Long.parseLong(hash.get("ID").toString());
			String gsmc = hash.get("GSMC").toString();
			UserContext uc = this.getUserContext();
			if (!"0".equals(map.get("instanceId").toString())) {
				LogUtil.getInstance().addLog(dataId, "项目签约信息维护",gsmc + "修改项目签约信息。");
			} else {
				LogUtil.getInstance().addLog(dataId, "项目签约信息维护",gsmc + "添加项目签约信息。");
			}
			if (uc != null) {
				String xylx = hash.get("XYLX").toString();
				if(xylx.equals("推荐挂牌并持续督导协议书之补充协议")){
					String customerno = hash.get("CUSTOMERNO").toString();
					HashMap<String,String> conditionMap = new HashMap<String,String>();
					conditionMap.put("CUSTOMERNO", customerno);
					List<HashMap> list = DemAPI.getInstance().getList(XMLXUUID, conditionMap, null);
					if(list!=null&&list.size()>0){
						String xyje = hash.get("XYJE").toString();
						HashMap hashMap = list.get(0);
						Long dataid = Long.parseLong(hashMap.get("ID").toString());
						Long instanceid = Long.parseLong(hashMap.get("INSTANCEID").toString());
						hashMap.put("A08", xyje);
						DemAPI.getInstance().updateFormData(XMLXUUID, instanceid, hashMap, dataid, false);
					}
				}
				//推荐挂牌并持续督导协议书之补充协议
				String name = uc.get_userModel().getUserid() + "[" + uc.get_userModel().getUsername() + "]";
				hash.put("ZHXGR", name);
				hash.put("ZHXGSJ", UtilDate.getNowDatetime());
				String xmqyuuid = getConfigUUID("xmqyuuid");
				DemAPI.getInstance().updateFormData(xmqyuuid, this.getInstanceId(), hash, this.getDataId(), false);
			}
		}
		return true;
	}
	
	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter)==null?"0":config.get(parameter));
		}
		return result;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter)==null?"":config.get(parameter);
		}
		return result;
	}
	

}
