package com.iwork.connector.mdm.erp;

import java.util.HashMap;
import java.util.List;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.connection.ConnectionAPI;
import com.iwork.connector.common.ConnectorUUIDConstant;
import com.iwork.core.security.SecurityUtil;
import com.iwork.sdk.DemAPI;

/**
 * 同步财务辅助核算主数据信息
 * @author David
 *
 */
public class MdmSynchronizeFiFieldSchedule implements IWorkScheduleInterface{
	
	public static final String FI_DEM_UUID= "758d146c87d941ddaa7b3037a269196c";
	
	
	/**
	 * 同步
	 */
	public boolean executeBefore() throws ScheduleException {
		System.out.println("开始同步财务辅助核算主数据.............................");
		
		HashMap hash = new HashMap();
		hash.put("PRHEADER","123");
		List list = ConnectionAPI.getInstance().getList("983ff901ae3b4e76984f8bcfe015bcff", hash);
		
		
		
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		doExecute();
		
		return true;
	}

	public boolean executeAfter() throws ScheduleException {
		System.out.println("同步财务辅助核算主数据完成.............................OK");
		return true; 
	}
	 
	/**
	 * 执行操作
	 * @return
	 */
	private boolean doExecute(){
//		remove();
		//获取项目定义列表
		List<HashMap> dataList =  ConnectionAPI.getInstance().getList(ConnectorUUIDConstant.ZFM_LIFNR_MASTER_DATA, new HashMap());
		if(dataList!=null){
			for(HashMap map:dataList){
				String ztype = "";
				String ztypems = "";
				String lifnr = "";
				String name1 = "";
				if(map.get("ZTYPE")!=null){
					ztype = map.get("ZTYPE").toString();
				}
				if(map.get("ZTYPEMS")!=null){
					ztypems = map.get("ZTYPEMS").toString();
				}
				if(map.get("LIFNR")!=null){
					lifnr = map.get("LIFNR").toString();
				}
				if(map.get("NAME1")!=null){
					name1 = map.get("NAME1").toString();
				}
				HashMap data = null;
				//装置主数据对象
				HashMap condition = new HashMap();
				condition.put("FZHSLX", ztype);
				condition.put("FZHSLXMS", ztypems);
				condition.put("FZHSX", lifnr);
				List<HashMap> datalist = DemAPI.getInstance().getList(FI_DEM_UUID, condition, null);
				if(datalist!=null&&datalist.size()>0){
					data = datalist.get(0);
				}
				if(data!=null){
					data.put("FZHSLX", ztype);
					data.put("FZHSLXMS", ztypems);
					data.put("FZHSX", lifnr);
					data.put("FZHSXMS", name1);
					Long dataid = Long.parseLong(data.get("ID").toString());
					Long instanceId = Long.parseLong(data.get("INSTANCEID").toString());
				   DemAPI.getInstance().updateFormData(FI_DEM_UUID, instanceId, data, dataid, false);				   
				}else{
					data = new HashMap();
					data.put("FZHSLX", ztype);
					data.put("FZHSLXMS", ztypems);
					data.put("FZHSX", lifnr);
					data.put("FZHSXMS", name1);
					Long newinstanceid = DemAPI.getInstance().newInstance(FI_DEM_UUID, SecurityUtil.supermanager);
					boolean flag = DemAPI.getInstance().saveFormData(FI_DEM_UUID, newinstanceid, data, false); 					
				}
			}
		}
		return true;
	}
}
