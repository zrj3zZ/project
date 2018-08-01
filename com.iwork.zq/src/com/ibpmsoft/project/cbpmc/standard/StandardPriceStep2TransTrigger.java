package com.ibpmsoft.project.cbpmc.standard;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.ProcessAPI;

public class StandardPriceStep2TransTrigger  extends ProcessStepTriggerEvent{
	private UserContext _me;
	private HashMap params;
	private String log = "";
	public StandardPriceStep2TransTrigger(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	
	public boolean execute()
	  {
		
		String subformkey = "SUBFORM_BZJGDZZB";
		List<HashMap> sublist = ProcessAPI.getInstance().getFromSubData(this.getInstanceId(), subformkey);
		
		for(HashMap item:sublist){
			BigDecimal id = (BigDecimal)item.get("ID");
			//设置资产卡片信息默认价格
			String isdefSupplier = (String)item.get("ISDEFAULTSUPPLIER");
			if(isdefSupplier!=null&&isdefSupplier.equals("是")){
				BigDecimal price = (BigDecimal)item.get("STANDARDPRICE");
				BigDecimal sid = (BigDecimal)item.get("STATIONERYID");
				DBUtil.executeUpdate(" update BD_EAM_CARD set defaultstdprice=" + price + " where id=" + sid.intValue());
			}
			 ProcessAPI.getInstance().updateFormDatas(this.getActDefId(),subformkey,  this.getInstanceId(), sublist, "BILLSTATUS", "完成", false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
	    return true;
	  }
} 
