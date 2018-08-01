package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

/**
 * 临时公告归档触发器
 * @author zouyalei
 *
 */
public class zqbTempNoticeEvent extends ProcessTriggerEvent {

	private static final String TEMP_NOTICE_UUID = "1dfe958166a347188339af1337e25fb7";
	public zqbTempNoticeEvent(UserContext uc, HashMap<String,Object> hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = this.getInstanceId();
		// 获取流程表单数据
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		
		if(dataMap!=null){
			Long instanceid = DemAPI.getInstance().newInstance(TEMP_NOTICE_UUID, uc.get_userModel().getUserid());
			
			HashMap<String,Object> hashdata = new HashMap<String,Object>();
			hashdata.put("MTYDKJ", dataMap.get("MTYDKJ")==null?"":dataMap.get("MTYDKJ").toString());
			hashdata.put("COMPANYNO", dataMap.get("COMPANYNO")==null?"":dataMap.get("COMPANYNO").toString());
			hashdata.put("COMPANYNAME", dataMap.get("COMPANYNAME")==null?"":dataMap.get("COMPANYNAME").toString());
			hashdata.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
			hashdata.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
			hashdata.put("NOTICEFLAG", dataMap.get("NOTICEFLAG")==null?"":dataMap.get("NOTICEFLAG").toString());
			hashdata.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
			hashdata.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
			hashdata.put("PAPERFILENO", dataMap.get("PAPERFILENO")==null?"":dataMap.get("PAPERFILENO").toString());
			hashdata.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
			hashdata.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
			hashdata.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
			hashdata.put("MEETID", dataMap.get("MEETID")==null?"":dataMap.get("MEETID").toString());
			hashdata.put("MEETNAME", dataMap.get("MEETNAME")==null?"":dataMap.get("MEETNAME").toString());
			hashdata.put("KHMC", dataMap.get("KHMC")==null?"":dataMap.get("KHMC").toString());
			hashdata.put("KHBH", dataMap.get("KHBH")==null?"":dataMap.get("KHBH").toString());
			hashdata.put("CREATERID", dataMap.get("CREATERID")==null?"":dataMap.get("CREATERID").toString());
			hashdata.put("CREATENAME", dataMap.get("CREATENAME")==null?"":dataMap.get("CREATENAME").toString());
			hashdata.put("CREATEDATE", dataMap.get("CREATEDATE")==null?"":dataMap.get("CREATEDATE").toString());
			hashdata.put("LCBH", instanceId);
			
			// 保存到临时公告数据表中
			flag = DemAPI.getInstance().saveFormData(TEMP_NOTICE_UUID, instanceid, hashdata, false);
		}
		
		return flag;
	}
}
