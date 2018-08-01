package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;

import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.FormDataUtil;
import com.iwork.sdk.DemAPI;
public class ZQB_InUser_SetPhoneBookEvent  extends DemTriggerEvent { 
	public ZQB_InUser_SetPhoneBookEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap hash = this.getFormData();
		HashMap map = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(hash!=null){
			String name = FormDataUtil.getInstance().getFormData(hash.get("XM"));
			HashMap conditionMap = new HashMap();
			conditionMap.put("NAME", name);
			conditionMap.put("USERID", UserContextUtil.getInstance().getCurrentUserId());
			List<HashMap> list = DemAPI.getInstance().getList("5a47f41adc764690ae7f8258730e1618", conditionMap, null);
			HashMap data = null;
			if(list!=null&&list.size()>0){
				data = list.get(0);
			}
			if(data!=null){
				if( hash.get("SJ")!=null){
					String sj = FormDataUtil.getInstance().getFormData(hash.get("SJ"));
					data.put("TEL",sj);
				}
				if( hash.get("DZYJ")!=null){
					String email = FormDataUtil.getInstance().getFormData(hash.get("DZYJ"));
					data.put("EMAIL",email);
				}
				if( hash.get("RZLX")!=null){
					String rzlx = FormDataUtil.getInstance().getFormData(hash.get("RZLX"));
					data.put("TITLE",rzlx);
				}
				DemAPI.getInstance().updateFormData("5a47f41adc764690ae7f8258730e1618",Long.parseLong( data.get("INSTANCEID").toString()), data,Long.parseLong(  data.get("ID").toString()), false);
				/*MessageQueueUtil.getInstance().putAlertMsg("通讯录同步成功");*/
			}else{
				data = new HashMap();
				data.put("NAME", name);
				data.put("USERID", UserContextUtil.getInstance().getCurrentUserId());
				if( hash.get("SJ")!=null){
					String sj = FormDataUtil.getInstance().getFormData(hash.get("SJ"));
					data.put("TEL",sj);
				}
				if( hash.get("DZYJ")!=null){
					String email = FormDataUtil.getInstance().getFormData(hash.get("DZYJ"));
					data.put("EMAIL",email);
				}
				if( hash.get("RZLX")!=null){
					String rzlx = FormDataUtil.getInstance().getFormData(hash.get("RZLX"));
					data.put("TITLE",rzlx);
				}
				Long instanceid = DemAPI.getInstance().newInstance("5a47f41adc764690ae7f8258730e1618", UserContextUtil.getInstance().getCurrentUserId());
				DemAPI.getInstance().saveFormData("5a47f41adc764690ae7f8258730e1618", instanceid, data, false);
				/*MessageQueueUtil.getInstance().putAlertMsg("通讯录同步成功");*/
			}
			String value=map.get("XM")==null?"":map.get("XM").toString();
			Long dataId=Long.parseLong(map.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "外部人员信息", "添加/编辑外部人员信息："+value);
		}
		return false;
	}
}

