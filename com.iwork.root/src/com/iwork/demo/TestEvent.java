package com.iwork.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.connection.ConnectionAPI;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class TestEvent extends ProcessStepTriggerEvent {
	private UserContext _me;
	private HashMap params;
	public TestEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	public boolean execute() {
		String uuid = "6903844b0564480a87715c767e16659b";
		HashMap hash = new HashMap();
		
		HashMap mainData = ProcessAPI.getInstance().getFromData(this.getInstanceId());
		List list = ProcessAPI.getInstance().getFromSubData(this.getInstanceId(), "SUBFORM_YSCWGZXXM");
		List itemlist = new ArrayList();
		HashMap item = new HashMap();
		item.put("HEADER_TXT",mainData.get("GSTT"));
		item.put("DOC_DATE",mainData.get("GSTT"));
		
		itemlist.add(item);
		hash.put("DOCUMENTHEADER", itemlist);
		
		
		ConnectionAPI.getInstance().getList(uuid, hash);
		return false;
	}
}
