package com.ibpmsoft.project.zqb.hl.event;

import java.util.HashMap;

import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;


public class zqbQtProDemAfterSaveEvent extends DemTriggerEvent {
		public zqbQtProDemAfterSaveEvent(UserContext uc, HashMap hash) {
			super(uc, hash);
		}

		public boolean execute() {
			// 定增(200人以内)项目
			String XMUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='其他项目'", "UUID");
			HashMap formData = this.getFormData();
			HashMap map = ParameterMapUtil.getParameterMap(formData);
			HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if (hash.get("JDBH") != null && !"".equals(hash.get("JDBH"))) {
				HashMap params=new HashMap();
				Long jdbh=(Long) hash.get("JDBH");
				params.put(1, jdbh);
				String sql="SELECT JDMC FROM BD_ZQB_TYXM_INFO WHERE ID=?";
				String JDMC = DBUTilNew.getDataStr("JDMC", sql, params);
				hash.put("JDMC", JDMC);
				if ("初步尽调".equals(JDMC)) {
					hash.put("SPZT", "已尽调");
				} else {
					hash.put("SPZT", "已归档");
				}
			} 
			
			boolean flag = DemAPI.getInstance().updateFormData(XMUUID, this.getInstanceId(), hash,
					Long.parseLong(hash.get("ID").toString()), false);
			return true;
		}
	}


