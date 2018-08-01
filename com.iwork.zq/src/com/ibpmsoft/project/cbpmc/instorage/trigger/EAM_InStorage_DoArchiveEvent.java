package com.ibpmsoft.project.cbpmc.instorage.trigger;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.ibpmsoft.project.cbpmc.util.ParamUtil;
import com.ibpmsoft.project.cbpmc.util.StringUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class EAM_InStorage_DoArchiveEvent extends ProcessTriggerEvent {
	private static Logger logger = Logger.getLogger(EAM_InStorage_DoArchiveEvent.class);

	private HashMap params = null;
	private String subformkey = "SUBFORM_SWZCMXB_1";
	private String demUUID = "394a51e931f14eeb95fb75519467305b";
	public EAM_InStorage_DoArchiveEvent(UserContext me,HashMap hash){
		super(me,hash);
		params = hash;
	}
	
	public boolean execute()
	  {
	    Long instanceId = this.getInstanceId();
	    try {
	      HashMap hash = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
	      Long type = (Long)hash.get("TYPE");
	      if (type.equals(new Long(1))) {
	        InsertBOASSETSLEDGER(instanceId, "在用");
	      } else if(type.equals(new Long(0))) {
	        InsertBOASSETSLEDGER(instanceId, "在库");
	        InsertBOASSETSWAREHOUSE(instanceId);
	      } else {
	        MessageQueueUtil.getInstance().putAlertMsg("请选择入库类型！");
	        return false;
	      }
	      hash.put("STATUS", "30");
	      ProcessAPI.getInstance().saveFormData(this.getActDefId(), instanceId, hash, false);
	    } catch (Exception e) {logger.error(e,e);
	      return false;
	    }
	    return true;
	  }

	  private void InsertBOASSETSLEDGER(Long instanceId, String billstatus)
	    throws Exception
	  {
	    List dataVector = new ArrayList();
	    List dataVectorLog = new ArrayList(); 
	    List<HashMap> sublist = ProcessAPI.getInstance().getFromSubData(instanceId, subformkey);
	   for(HashMap ht:sublist){ 
	      ht.put("NAME", StringUtil.isNull(ht.get("ASSETSNAME")));
	      ht.put("ASSETSNO", StringUtil.isNull(ht.get("ASSETSNO")));
	      ht.put("CODE", StringUtil.isNull(ht.get("CODE")));
	      ht.put("SPECIFICATION", StringUtil.isNull(ht.get("SPECIFICATION")));
	      ht.put("COUNT", ParamUtil.setLong(ht.get("COUNT")));
	      ht.put("ACTUALUNIT", ht.get("ACTUALUNIT"));
	      ht.put("UNIT", StringUtil.isNull(ht.get("UNIT")));
	      ht.put("MODEL", StringUtil.isNull(ht.get("MODEL")));
	      ht.put("BRAND", StringUtil.isNull(ht.get("BRAND")));
	      ht.put("MATERIA", StringUtil.isNull(ht.get("MATERIAL")));
	      ht.put("OTHER", StringUtil.isNull(ht.get("OTHER")));
	      ht.put("DEPARTMENTID", ht.get("DEPARTMENTID"));
	      ht.put("DEPARTMENTNAME", StringUtil.isNull(ht.get("DEPARTMENTNAME")));
	      ht.put("USEPLACE", StringUtil.isNull(ht.get("AFTERPLACE")));
	      ht.put("USERID", StringUtil.isNull(ht.get("USERID")));
	      ht.put("USERNAME", StringUtil.isNull(ht.get("USERNAME")));
	      ht.put("WAREHOUSENAME", ht.get("WAREHOUSENAME"));
	      ht.put("WAREHOUSEID",ht.get("WAREHOUSEID"));
	      ht.put("WAREHOUSEPLACE",ht.get("WAREHOUSEPLACE"));   
	      ht.put("BILLSTATUS", billstatus);
	      if (ht.get("BUYDATE") != null)
	      {
	        ht.put("BUYDATE",ht.get("BUYDATE"));
	      }
	      if (ht.get("USEDATE") != null)
	      {
	        ht.put("USEDATE", ht.get("USEDATE"));
	      }
	      ht.put("MATERIALCODE", StringUtil.isNull(ht.get("MATERIALCODE")));
//	      ProcessAPI.getInstance().saveFormData(this.getActDefId(), instanceId, ht, false);
//	      dataVector.add(ht);
	     Long newInstanceId = DemAPI.getInstance().newInstance(demUUID, UserContextUtil.getInstance().getCurrentUserId());
	      DemAPI.getInstance().saveFormData(demUUID, newInstanceId, ht, false);

	      HashMap htLog = new HashMap();
	      htLog.put("ASSETSNO", StringUtil.isNull(ht.get("ASSETSNO")));
	      htLog.put("OUTID", ht.get("ID"));
	      htLog.put("OUTTABLE", "BO_ASSETS_S");
	      htLog.put("LEDGERTYPE", Integer.valueOf(40));

	    }
	  }

	  /**
	   * 同步在库资产库存
	   * @param instanceId
	   * @throws Exception
	   */
	  private void InsertBOASSETSWAREHOUSE(Long instanceId)
	    throws Exception
	  {
	    Vector recordData = new Vector();
	    List<HashMap> sublist = ProcessAPI.getInstance().getFromSubData(instanceId, subformkey);
	   for(HashMap hash:sublist){
	      HashMap data = new HashMap();
	      data.put("ASSETSNO", StringUtil.isNull(hash.get("ASSETSNO")));
	      data.put("NAME", StringUtil.isNull(hash.get("ASSETSNAME")));
	      data.put("CODE", StringUtil.isNull(hash.get("CODE")));
	      data.put("SPECIFICATION", StringUtil.isNull(hash.get("SPECIFICATION")));
	      data.put("MODEL", StringUtil.isNull(hash.get("MODEL")));
	      data.put("BRAND", StringUtil.isNull(hash.get("BRAND")));
	      data.put("MATERIAL", StringUtil.isNull(hash.get("MATERIAL")));
	      data.put("OTHER", StringUtil.isNull(hash.get("OTHER")));
	      data.put("UNIT", StringUtil.isNull(hash.get("UNIT")));
	      data.put("COUNT", hash.get("COUNT"));
	      data.put("ACTUALUNIT", hash.get("ACTUALUNIT"));  
	      data.put("BILLSTATUS", "在库");
	      data.put("TYPE", Integer.valueOf(70));
	      data.put("OUTID", hash.get("ID"));
	      data.put("OUTTABLE", "BO_ASSETS_S");
	      data.put("OUTCOUNT", Integer.valueOf(0));
	      Long newInstanceId = DemAPI.getInstance().newInstance("ec06d60f549841bea8adc97cde74d191", UserContextUtil.getInstance().getCurrentUserId());
	      DemAPI.getInstance().saveFormData("ec06d60f549841bea8adc97cde74d191", newInstanceId, data, false); 
	    }
	  }
}
