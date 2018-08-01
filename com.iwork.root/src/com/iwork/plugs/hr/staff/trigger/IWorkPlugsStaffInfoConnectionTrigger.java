package com.iwork.plugs.hr.staff.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.iwork.connection.ConnectionTriggerAbst;

public class IWorkPlugsStaffInfoConnectionTrigger extends ConnectionTriggerAbst {
	public IWorkPlugsStaffInfoConnectionTrigger(){
		this.setDescription("设置员工基本信息");
	}
	
	
	 
public HashMap setInputParams(HashMap hashdata){
	if(hashdata!=null){
		List<HashMap> list = new ArrayList();
		HashMap data = new HashMap();
		data.put("P_SYBID", "005");
		data.put("P_BMID", "");  
		list.add(data); 
		 data = new HashMap();
		data.put("P_SYBID", "007");
		data.put("P_BMID", "");  
		list.add(data); 
		hashdata.put("P_PRA_ZZSHYB", list);
	}
		return hashdata;
	} 
	public List<HashMap> setOutputParams(List<HashMap> datalist){
		return datalist;
	}
 
}
