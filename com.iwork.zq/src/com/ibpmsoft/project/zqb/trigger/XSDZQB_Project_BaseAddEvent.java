package com.ibpmsoft.project.zqb.trigger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UtilDate;
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
public class XSDZQB_Project_BaseAddEvent extends DemTriggerEvent {
	public XSDZQB_Project_BaseAddEvent(UserContext me, HashMap hash) {
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
		String projectName=hash.get("PROJECTNAME").toString();
		Long dataId = Long.parseLong(hash.get("ID").toString());
		
		// 添加项目负责人时，查询项目审批人维护是否存在配置记录，如果不存在，则插入一条
		// 判断是不是多项目流程
		String config = SystemConfig._xmlcConf.getConfig();
		if (config.equals("2")) {
			String xmsprUUID = "24f7944184ca402d986325ce72fa20c9";
			HashMap conditionMap = new HashMap();
			conditionMap.put("CSFZR", map.get("OWNER"));
			List list = DemAPI.getInstance().getList(xmsprUUID, conditionMap,null);
			if (list.size() <= 0) {
				Long ins = DemAPI.getInstance().newInstance(xmsprUUID,
						UserContextUtil.getInstance().getCurrentUserId());
				HashMap hashdata = new HashMap();
				hashdata.put("CSFZR", map.get("OWNER"));
				String spr = DBUtil
						.getString(
								"select * from BD_ZQB_XMSPRWH  order by nvl(to_char(zhgxsj,'yyyy-MM-dd hh24:mi:ss'),0) desc",
								"FSFZR");
				if (spr != null && !spr.equals("")) {
					hashdata.put("FSFZR", spr);
				}
				String spr1 = DBUtil
						.getString(
								"select * from BD_ZQB_XMSPRWH  order by nvl(to_char(zhgxsj,'yyyy-MM-dd hh24:mi:ss'),0) desc",
								"ZZSPR");
				if (spr1 != null && !spr1.equals("")) {
					hashdata.put("ZZSPR", spr1);
				}
				hashdata.put("ZHGXSJ", UtilDate.datetimeFormat(new Date()));
				DemAPI.getInstance().saveFormData(xmsprUUID, ins, hashdata,
						false);
			}
		}

		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "项目管理维护", "更新项目："+projectName);
		}else{
			LogUtil.getInstance().addLog(dataId, "项目管理维护", "添加项目："+projectName);
		}
		return true;
	}
}
