package com.ibpmsoft.project.cbpmc.standard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class StandardPriceStep1VerificationTrigger  extends ProcessStepTriggerEvent{
	private UserContext _me;
	private HashMap params;
	private String log = "";
	public StandardPriceStep1VerificationTrigger(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	
	public boolean execute() {
		boolean flag = false;
		String subformkey = "SUBFORM_BZJGDZZB";
		List<HashMap> subtableList = ProcessAPI.getInstance().getFromSubData(this.getInstanceId(), subformkey);
		 flag = checkData(subtableList);
		 if(!flag){
			 MessageQueueUtil.getInstance().putAlertMsg(log);
		 }
		 
		return flag;
	}
	
	 private boolean checkData(List<HashMap> subtableList) 
	  {
		 boolean flag = true;
	    if(subtableList==null||subtableList.size()==0){
	    	log = "标准间中间表不允许为空！";
	    	flag = false;
	    }else{
	    	//去除重复
	    	List itemlist= new ArrayList();
	    	for(HashMap hash:subtableList){
	    		String supplierid = (String)hash.get("SUPPLIERCODE");
	    		java.math.BigDecimal  stationeryid = (java.math.BigDecimal)hash.get("STATIONERYID"); 
	    		String stationeryname = (String)hash.get("STATIONERYNAME");
	    		String key = supplierid+"_"+stationeryid;
	    		if(itemlist.contains(key)){
	    			log = "发现重复数据[物料名称："+stationeryname+"]，无法办理！";
	    			flag = false;
	    			break;
	    		}else{
	    			itemlist.add(key);
	    		}
	    		
	    		
	    	}
	    }
		 return flag;
	  }

}
