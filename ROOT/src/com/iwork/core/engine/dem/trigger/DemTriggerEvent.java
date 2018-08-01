package com.iwork.core.engine.dem.trigger;

import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.service.SysEngineIFormService;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.engine.trigger.interceptor.TriggerEventInterface;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DemTriggerEvent
  implements TriggerEventInterface
{
  private UserContext me;
  private HashMap formData;
  private Long dataId;
  private Long instanceId;
  private String actionType;
  private String tableName;
  private Long formId;
  private HashMap parameters;
  private static SysEngineMetadataDAO sysEngineMetadataDAO;
  private SysEngineIFormService sysEngineIFormService;
  public static final String DEM_PARAMETER_INSTANCEID = "PROCESS_PARAMETER_INSTANCEID";
  public static final String DEM_PARAMETER_ID = "DEM_PARAMETER_ID";
  public static final String DEM_PARAMETER_FORMDATA = "DEM_PARAMETER_FORMDATA";
  public static final String DEM_PARAMETER_FORMID = "DEM_PARAMETER_FORMID";
  public static final String DEM_PARAMETER_TABLENAME = "DEM_PARAMETER_TABLENAME";
  public static final String DEM_PARAMETER_ACTION_TYPE = "DEM_PARAMETER_ACTION_TYPE";
  public static final String DEM_ACTION_TYPE_UPDATE = "UPDATE";
  public static final String DEM_ACTION_TYPE_ADD = "ADD";

  public DemTriggerEvent()
  {
  }

  public DemTriggerEvent(UserContext uc, HashMap hash)
  {
    this.me = uc;
    this.parameters = hash;
  }

  public boolean execute() {
    return false;
  }

  public Long getInstanceId()
  {
    if ((this.parameters != null) && (this.parameters.get("PROCESS_PARAMETER_INSTANCEID") != null)) {
      this.instanceId = Long.valueOf(Long.parseLong(this.parameters.get("PROCESS_PARAMETER_INSTANCEID").toString()));
    }
    return this.instanceId;
  }

  public HashMap getFormData()
  {
    if ((this.parameters != null) && (this.parameters.get("DEM_PARAMETER_FORMDATA") != null)) {
    	Map map=(Map) this.parameters.get("DEM_PARAMETER_FORMDATA");
    	Map maps=getParameterMap(map);
      this.formData = (HashMap) maps;
    }
    return this.formData;
  }

  
  public  Map getParameterMap( Map properties) {
	    Map returnMap = new HashMap();
	    Iterator entries = properties.entrySet().iterator();
	    Map.Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Map.Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}
  
  public String getTableName()
  {
    if ((this.parameters != null) && (this.parameters.get("DEM_PARAMETER_TABLENAME") != null)) {
      this.tableName = ((String)this.parameters.get("DEM_PARAMETER_TABLENAME"));
    }
    else if (getFormId() != null) {
      if (this.sysEngineIFormService == null) {
        SpringBeanUtil.getInstance(); this.sysEngineIFormService = ((SysEngineIFormService)SpringBeanUtil.getBean("sysEngineIFormService"));
      }SysEngineIform formModel = this.sysEngineIFormService.getModel(getFormId());
      if (formModel != null) {
        if (sysEngineMetadataDAO == null) {
          SpringBeanUtil.getInstance(); sysEngineMetadataDAO = (SysEngineMetadataDAO)SpringBeanUtil.getBean("sysEngineMetadataDAO");
        }SysEngineMetadata metadata = sysEngineMetadataDAO.getModel(formModel.getMetadataid());
        if (metadata != null) {
          this.tableName = metadata.getEntityname();
        }
      }
    }

    return this.tableName;
  }

  public Long getFormId()
  {
    if ((this.parameters != null) && (this.parameters.get("DEM_PARAMETER_FORMID") != null)) {
      this.formId = Long.valueOf(Long.parseLong(this.parameters.get("DEM_PARAMETER_FORMID").toString()));
    }
    return this.formId;
  }

  public UserContext getUserContext()
  {
    return this.me;
  }

  public Long getDataId()
  {
    if ((this.parameters != null) && (this.parameters.get("DEM_PARAMETER_ID") != null)) {
      this.dataId = Long.valueOf(Long.parseLong(this.parameters.get("DEM_PARAMETER_ID").toString()));
    }
    return this.dataId;
  }

  public String getActionType()
  {
    if ((this.parameters != null) && (this.parameters.get("DEM_PARAMETER_ACTION_TYPE") != null)) {
      this.actionType = ((String)this.parameters.get("DEM_PARAMETER_ACTION_TYPE"));
    }
    return this.actionType;
  }
}