package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;
import java.util.List;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class zqbTempNoticeBhEvent extends ProcessStepTriggerEvent {

	private static final String TEMP_NOTICE_UUID = "1dfe958166a347188339af1337e25fb7";
	public zqbTempNoticeBhEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//归档时回写督导表中的反馈资料、流程相关、反馈状态、审批状态 字段
			// 1.先查询 2.再更新
			HashMap hashmap = new HashMap();
			String sq=dataMap.get("NOTICESQ").toString();
			if("".equals(sq)||sq==null){
				return true;
			}
			hashmap.put("NOTICESQ", sq);
			List<HashMap> list = DemAPI.getInstance().getList(TEMP_NOTICE_UUID,hashmap, null);
			if (!list.isEmpty()) {
				//回写审批状态 字段
				HashMap map=list.get(0);//原来数据
				map.put("SPZT", "驳回");
				map.put("YXID", this.getExcutionId());
				map.put("RWID", this.getTaskId());
				map.put("COMPANYNO", dataMap.get("COMPANYNO")==null?"":dataMap.get("COMPANYNO").toString());
				map.put("PAPERFILENO", dataMap.get("PAPERFILENO")==null?"":dataMap.get("PAPERFILENO").toString());
				map.put("MEETID", dataMap.get("MEETID")==null?"":dataMap.get("MEETID").toString());
				map.put("MEETNAME", dataMap.get("MEETNAME")==null?"":dataMap.get("MEETNAME").toString());        		
				map.put("NOTICENO", dataMap.get("NOTICENO")==null?"":dataMap.get("NOTICENO").toString());
				map.put("COMPANYNAME", dataMap.get("COMPANYNAME")==null?"":dataMap.get("COMPANYNAME").toString());
        		map.put("NOTICENAME", dataMap.get("NOTICENAME")==null?"":dataMap.get("NOTICENAME").toString());
        		map.put("NOTICETYPE", dataMap.get("NOTICETYPE")==null?"":dataMap.get("NOTICETYPE").toString());
        		map.put("NOTICEDATE", dataMap.get("NOTICEDATE")==null?"":dataMap.get("NOTICEDATE").toString());
        		map.put("NOTICEFILE", dataMap.get("NOTICEFILE")==null?"":dataMap.get("NOTICEFILE").toString());
    			map.put("NOTICETEXT", dataMap.get("NOTICETEXT")==null?"":dataMap.get("NOTICETEXT").toString());
    			map.put("GGDF", dataMap.get("GGDF")==null?"":dataMap.get("GGDF").toString());
    			map.put("ZQJCXS", dataMap.get("ZQJCXS")==null?"":dataMap.get("ZQJCXS").toString());
    			map.put("ZQDMXS", dataMap.get("ZQDMXS")==null?"":dataMap.get("ZQDMXS").toString());
				map.put("BZLX", dataMap.get("BZLX")==null?"":dataMap.get("BZLX").toString());
				map.put("GGZY", dataMap.get("GGZY")==null?"":dataMap.get("GGZY").toString());

				map.put("ZPRQ", dataMap.get("ZPRQ")==null?"":dataMap.get("ZPRQ").toString());
				map.put("XSBLSJ", dataMap.get("XSBLSJ")==null?"":dataMap.get("XSBLSJ").toString());
				map.put("SFCG", dataMap.get("SFCG")==null?"":dataMap.get("SFCG").toString());
				Long instanceid=Long.parseLong(map.get("INSTANCEID").toString());
				Long dataid=Long.parseLong(map.get("ID").toString());
				flag=DemAPI.getInstance().updateFormData(TEMP_NOTICE_UUID, instanceid, map, dataid, true);
				
			}
		}
		return flag;
	}
	

}

