package com.iwork.core.engine.iform.action;

import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.constant.IFormStatusConstants;
import com.iwork.core.engine.dem.dao.SysDemEngineDAO;
import com.iwork.core.engine.dem.model.SysDemEngine;
import com.iwork.core.engine.dem.service.SysDemTriggerService;
import com.iwork.core.engine.dem.service.SysDemWorkBoxService;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.iform.service.SysEngineIFormComponentsService;
import com.iwork.core.engine.iform.service.SysEngineIFormMapService;
import com.iwork.core.engine.iform.service.SysEngineIFormService;
import com.iwork.core.engine.iform.service.SysEngineSubformService;
import com.iwork.core.engine.trigger.TriggerAPI;
import com.iwork.core.engine.trigger.model.BaseTriggerModel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

public class IFormAction extends ActionSupport
{
  private static final Logger logger = LoggerFactory.getLogger(IFormAction.class);
  private SysEngineIFormComponentsService sysEngineIFormComponentsService;
  private SysEngineIFormService sysEngineIFormService;
  private SysEngineIFormMapService sysEngineIFormMapService;
  private ProcessStepTriggerService processStepTriggerService;
  private SysEngineSubformService sysEngineSubformService;
  private FreeMarkerConfigurer freemarderConfig;
  private IFormService iformService;
  private SysDemWorkBoxService sysDemWorkBoxService;
  private SysDemTriggerService sysDemTriggerService;
  private SysDemEngineDAO sysDemEngineDAO;
  private OrgUserDAO orgUserDAO;
  private Long isLog;
  private Long isShowLog;
  private Long demId;
  private Long modelId;
  private String modelType;
  private Long engineType;
  private String actDefId;
  private Long prcDefId;
  private String actStepDefId;
  private String taskId;
  private String excutionId;
  private Long formid;
  private Long dataid;
  private Long instanceId;
  private File upFile;
  private Long type;
  private String removeList;
  private String _search;
  private String q;
  private String menuLayoutType;
  private String menuLayoutUpdateFlag;
  private Long subformid;
  private String subformkey;
  private String id;
  private String oper;
  private String template;
  private String tablelist_th;
  private String search_html;
  private String pagination;
  private String toolsbar;
  private ActionMapping mapping;
  private String title;
  private String style;
  private String script;
  private String forminfo;
  private String fieldName;
  private String content;
  private String param;
  private String info;
  private String baseinfo;
  private String verifySuccessInfo;
  private String verifyErrorInfo;
  private String projectNo;
  private String projectname;
  private String dggbxz;
  private String dgxmlx;
  private String dginsid;
private String dgyc;


public String getDgyc() {
	return dgyc;
}

public void setDgyc(String dgyc) {
	this.dgyc = dgyc;
}

public String getDgxmlx() {
	return dgxmlx;
}

public void setDgxmlx(String dgxmlx) {
	this.dgxmlx = dgxmlx;
}

public String getDginsid() {
	return dginsid;
}

public void setDginsid(String dginsid) {
	this.dginsid = dginsid;
}

public String getDggbxz() {
	return dggbxz;
}

public void setDggbxz(String dggbxz) {
	this.dggbxz = dggbxz;
}

public String getProjectname() {
	return projectname;
}

public void setProjectname(String projectname) {
	this.projectname = projectname;
}

public String getProjectNo() {
	return projectNo;
}

public void setProjectNo(String projectNo) {
	this.projectNo = projectNo;
}

public String loadIformPage()
  {
    Long iformid = getFormid();
    Long instanceid = getInstanceId();
   
    boolean flag = false;
    String style_html = "";
    String script_html = "";
    if (iformid != null)
    {
      if ((this.demId != null) && (this.demId != null)) {
        this.modelId = this.demId;

        this.modelType = "DEM";

        SysDemEngine model = this.sysDemEngineDAO.getModel(this.demId);
        if ((model != null) && (model.getFormid().equals(this.formid))) {
        	if(this.demId!=71L)  flag = this.sysDemWorkBoxService.checkDemSecurity(this.demId);
        	else flag=true;
          if (model.getIsLog().equals(SysConst.on))
            this.isLog = SysConst.on;
          else {
            this.isLog = SysConst.off;
          }

          if (model.getIsShowLog().equals(SysConst.on))
            this.isShowLog = SysConst.on;
          else {
            this.isShowLog = SysConst.off;
          }
        }
      }
      HttpServletRequest request = ServletActionContext.getRequest();
      Map params = request.getParameterMap();
      if (this.info == null) this.info = "";

      SysEngineIform iformModel = this.sysEngineIFormService.getModel(iformid);

      if ((flag) && (iformModel != null)) {
        flag = true;
        if (instanceid == null) instanceid = new Long(0L);
        BaseTriggerModel triggerModel = null;
        boolean isrun = true;

        String page = "";

        triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.demId, "DEM_FORM_LOAD");
        if (triggerModel != null) {
          HashMap triggerParams = new HashMap();

          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
          triggerParams.put("DEM_PARAMETER_ID", this.dataid);
          triggerParams.put("DEM_PARAMETER_FORMID", iformid);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
        }

        page = this.iformService.getFormPage(this.formid, instanceid, flag, params, EngineConstants.SYS_INSTANCE_TYPE_DEM);
        if (iformModel.getIformCss() != null)
          style_html = iformModel.getIformCss();
        if (iformModel.getIformJs() != null) {
          script_html = iformModel.getIformJs();
        }
        Long dataid = this.iformService.getDataid(iformid, instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
        setDataid(dataid);

        setTitle(iformModel.getIformTitle());

        setStyle(style_html);

        setScript(script_html);
        setForminfo("");
        setContent(page);
        setParam("");
        setInfo(this.info);
        setInstanceId(instanceid);
      } else {
        flag = false;
      }
    } else {
      flag = false;
    }

    if (flag) {
      return "success";
    }
    return "error";
  }

  public String loadVisitPage()
  {
    Long iformid = getFormid();
    Long instanceid = getInstanceId();
    boolean flag = false;
    String style_html = "";
    String script_html = "";
    if (iformid != null) {
      if (this.info == null) this.info = "";

      SysEngineIform iformModel = this.sysEngineIFormService.getModel(iformid);

      if (iformModel != null) {
        flag = true;
        if (instanceid == null) instanceid = new Long(0L);
        BaseTriggerModel triggerModel = null;
        boolean isrun = true;

        String page = "";
        page = this.iformService.getFormPage(this.formid, instanceid, IFormStatusConstants.FORM_PAGE_READ, null, null, null, EngineConstants.SYS_INSTANCE_TYPE_DEM);
        if (iformModel.getIformCss() != null)
          style_html = iformModel.getIformCss();
        if (iformModel.getIformJs() != null) {
          script_html = iformModel.getIformJs();
        }
        Long dataid = this.iformService.getDataid(iformid, instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
        setDataid(dataid);

        setTitle(iformModel.getIformTitle());

        setStyle(style_html);

        setScript(script_html);
        setForminfo("");
        setContent(page);
        setParam("");
        setInfo(this.info);
        setInstanceId(instanceid);
      } else {
        flag = false;
      }
    } else {
      flag = false;
    }

    if (flag) {
      return "success";
    }
    return "error";
  }

  public String loadSubformPage()
  {
    Long instanceid = getInstanceId();
    boolean flag = false;
    String style_html = "";
    String script_html = "";
    if (this.subformid != null) {
      HttpServletRequest request = ServletActionContext.getRequest();
      Map params = request.getParameterMap();
      if (this.info == null) this.info = "";

      SysEngineIform iformModel = this.sysEngineIFormService.getModel(this.subformid);

      if (iformModel != null)
      {
        if (this.engineType.equals(new Long(0L))) {
          flag = true;
        }

        if (instanceid == null) instanceid = new Long(0L);
        BaseTriggerModel triggerModel = null;
        boolean isrun = true;

        String page = "";
        if (this.dataid == null) this.dataid = new Long(0L);
        page = this.iformService.getSubFormPage(this.subformid, instanceid, this.dataid, flag, params, this.engineType);
        if (iformModel.getIformCss() != null)
          style_html = iformModel.getIformCss();
        if (iformModel.getIformJs() != null) {
          script_html = iformModel.getIformJs();
        }
        if ((this.dataid == null) || (this.dataid.equals(new Long(0L)))) {
          Long localLong1 = this.iformService.getDataid(this.subformid, instanceid, this.engineType);
        }
        setId(this.dataid.toString());

        setTitle(iformModel.getIformTitle());

        setStyle(style_html);

        setScript(script_html);
        setForminfo("");
        setContent(page);
        setParam("");
        setInfo(this.info);
        setInstanceId(instanceid);
      } else {
        flag = false;
      }
    } else {
      flag = false;
    }

    if (flag) {
      return "success";
    }
    return "error";
  }

  public void saveIform()
  {
	  Long instanceid = 0L;
	  boolean flag = false;
	  try
	  {
	    Long dataid = getDataid();
	    Long iformid = getFormid();
	    instanceid = getInstanceId();
	    flag = false;
	    Long type = getType();
	    BaseTriggerModel triggerModel = null;
	    if ((iformid != null) && (this.modelId != null)) {
	      if (type == null) {
	        type = new Long(0L);
	      }
	      this.demId = this.modelId;
	      HttpServletRequest request = ServletActionContext.getRequest();
	      Map params = request.getParameterMap();
	      boolean isrun = true;
	
	      boolean islog = false;
	      if ((this.isLog != null) && (this.isLog.equals(SysConst.on))) {
	        islog = true;
	      }
	      if ((instanceid != null) && (instanceid.longValue() > 0L))
	      {
	        triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.demId, "DEM_FORM_SAVE_BEFOR");
	        if (triggerModel != null) {
	          HashMap triggerParams = new HashMap();
	
	          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
	          triggerParams.put("DEM_PARAMETER_ID", dataid);
	          triggerParams.put("DEM_PARAMETER_FORMID", iformid);
	          triggerParams.put("DEM_PARAMETER_FORMDATA", params);
	          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
	        }
	
	        if (isrun) {
	          flag = this.iformService.updateForm(this.demId, iformid, instanceid, params, dataid, islog, EngineConstants.SYS_INSTANCE_TYPE_DEM);
	
	          triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.demId, "DEM_FORM_SAVE_AFTER");
	          if (triggerModel != null) {
	            HashMap triggerParams = new HashMap();
	
	            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
	            triggerParams.put("DEM_PARAMETER_ID", dataid);
	            triggerParams.put("DEM_PARAMETER_FORMID", iformid);
	            triggerParams.put("DEM_PARAMETER_FORMDATA", params);
	            isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
	          }
	
	        }
	
	      }
	      else
	      {
	        instanceid = this.iformService.initInstanceId(this.formid, type);
	
	        triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.demId, "DEM_FORM_SAVE_BEFOR");
	        if (triggerModel != null) {
	          HashMap triggerParams = new HashMap();
	
	          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
	          triggerParams.put("DEM_PARAMETER_ID", dataid);
	          triggerParams.put("DEM_PARAMETER_FORMID", iformid);
	          triggerParams.put("DEM_PARAMETER_FORMDATA", params);
	          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
	        }
	
	        if (isrun) {
	          dataid = this.iformService.saveForm(this.modelId, iformid, instanceid, params, islog, EngineConstants.SYS_INSTANCE_TYPE_DEM);
	          setInstanceId(instanceid);
	          setDataid(dataid);
	          if (dataid.longValue() > 0L)
	          {
	            triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.demId, "DEM_FORM_SAVE_AFTER");
	            if (triggerModel != null) {
	              HashMap triggerParams = new HashMap();
	
	              triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
	              triggerParams.put("DEM_PARAMETER_ID", dataid);
	              triggerParams.put("DEM_PARAMETER_FORMID", iformid);
	              triggerParams.put("DEM_PARAMETER_FORMDATA", params);
	              isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
	            }
	
	            flag = true;
	          } else {
	            flag = false;
	          }
	        }
	      }
	    }
	  }catch(Exception ex)
	  {		  
		  logger.error("IFormAction.saveIform："+ex.getMessage());
	  }
	String str = flag + "," + instanceid + "," + dataid;
    ResponseUtil.write(str);
  }

  public String menuLayoutEdit()
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc._userModel.getUserid();
    OrgUser model = this.orgUserDAO.getUserModel(userid);

    if ((this.menuLayoutType != null) && (!this.menuLayoutType.equals(""))) {
      model.setMenulayouttype(this.menuLayoutType);
      this.orgUserDAO.updateBoData(model);
      this.menuLayoutUpdateFlag = "true";
    }
    this.menuLayoutType = model.getMenulayouttype();
    if ((this.menuLayoutType == null) || (this.menuLayoutType.equals(""))) {
      this.menuLayoutType = "Right";
    }
    return "success";
  }

  public String preview()
  {
    Long iformid = getFormid();
    if (iformid != null) {
      if (this.info == null) this.info = "";
      String style_html = "";
      String script_html = "";

      SysEngineIform iformModel = this.sysEngineIFormService.getModel(iformid);

      if (iformModel != null)
      {
        String page = this.iformService.getPreviewPage(this.formid);
        if (iformModel.getIformCss() != null)
          style_html = iformModel.getIformCss();
        if (iformModel.getIformJs() != null) {
          script_html = iformModel.getIformJs();
        }
        setTitle(iformModel.getIformTitle());

        setStyle(style_html);

        setScript(script_html);
        setForminfo("");
        setContent(page);
        setParam("");
        setInfo(this.info);
        setBaseinfo(this.iformService.getFormBaseInfo(iformid));
        setVerifySuccessInfo(this.iformService.getVerifySuccessInfo());
        setVerifyErrorInfo(this.iformService.getVerifyErrorInfo(this.formid));
      }
    }
    return "success";
  }

  public String remove()
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    String l = request.getParameter("removeList");
    String ids = getRemoveList();
    boolean flag = false;
    Long formid = getFormid();
    if ((ids != null) && 
      (!ids.equals("")) && (this.demId != null)) {
      String[] id = ids.split(",");
      flag = this.sysDemWorkBoxService.remove(this.demId, id);
    }

    return "success";
  }

  public String subForm_save()
  {
    Long instanceid = this.instanceId;
    String oper = getOper();
    HttpServletRequest request = ServletActionContext.getRequest();
    Map params = request.getParameterMap();
    boolean flag = true;
    boolean islog = false;
    if ((this.isLog != null) && (this.isLog.equals(SysConst.on))) {
      islog = true;
    }

    if ((this.modelType != null) && (this.modelType.equals("PROCESS")))
      this.engineType = EngineConstants.SYS_INSTANCE_TYPE_PROCESS;
    else if ((this.modelType != null) && (this.modelType.equals("DEM"))) {
      this.engineType = EngineConstants.SYS_INSTANCE_TYPE_DEM;
    }
    if ((this.id == null) || (this.id.equals(""))) {
      this.id = "0";
    }
    if (this.id != null) {
      int temp = Integer.parseInt(this.id);
      if (temp <= 0) {
        oper = "add";
      }

    }

    HashMap triggerParams = new HashMap();
    BaseTriggerModel triggerModel = null;
    boolean isrun = true;
    if (this.engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM))
    {
      triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.modelId, "DEM_FORM_SAVE_BEFOR");
      if (triggerModel != null)
      {
        triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
        triggerParams.put("DEM_PARAMETER_ID", this.dataid);
        triggerParams.put("DEM_PARAMETER_FORMID", this.subformid);
        triggerParams.put("DEM_PARAMETER_FORMDATA", params);
        isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
      }

    }
    else if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null)) {
      triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_SAVE_BEFOR");
      if (triggerModel != null) {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

        triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
        triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
        triggerParams.put("PROCESS_PARAMETER_FORMID", this.subformid);
        triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
        triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
        triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
        triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
        isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
      }

    }

    if (!isrun) {
      flag = false;
      ResponseUtil.write(String.valueOf(flag));
      return null;
    }

    if ("add".equals(oper)) {
      Long newId = this.iformService.saveForm(this.modelId, this.subformid, instanceid, params, islog, this.engineType);
      if (newId.longValue() <= 0L) {
        flag = false;
      }
      if (flag) {
        if (this.engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM))
        {
          triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.modelId, "DEM_FORM_SAVE_AFTER");
          if (triggerModel != null)
          {
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
            triggerParams.put("DEM_PARAMETER_ID", this.dataid);
            triggerParams.put("DEM_PARAMETER_FORMID", this.subformid);
            triggerParams.put("DEM_PARAMETER_FORMDATA", params);
            isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
          }

        }
        else if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null)) {
          triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_SAVE_AFTER");
          if (triggerModel != null) {
            UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

            triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
            triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
            triggerParams.put("PROCESS_PARAMETER_FORMID", this.subformid);
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
            triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
            triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
            triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
            isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
          }
        }

      }

    }
    else if ("edit".equals(oper)) {
      Long l = Long.valueOf(Long.parseLong(this.id));

      if (l.longValue() > 0L) {
        flag = this.iformService.updateForm(this.modelId, this.subformid, instanceid, params, new Long(this.id), islog, this.engineType);
      } else {
        Long newId = this.iformService.saveForm(this.modelId, this.subformid, instanceid, params, islog, this.engineType);
        if (newId.longValue() <= 0L) {
          flag = false;
        }
      }
      if (flag) {
        if (this.engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM))
        {
          triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.modelId, "DEM_FORM_SAVE_AFTER");
          if (triggerModel != null)
          {
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
            triggerParams.put("DEM_PARAMETER_ID", this.dataid);
            triggerParams.put("DEM_PARAMETER_FORMID", this.subformid);
            triggerParams.put("DEM_PARAMETER_FORMDATA", params);
            isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
          }

        }
        else if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null)) {
          triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_SAVE_AFTER");
          if (triggerModel != null) {
            UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

            triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
            triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
            triggerParams.put("PROCESS_PARAMETER_FORMID", this.subformid);
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
            triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
            triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
            triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
            isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
          }
        }
      }

    }
    else if ("del".equals(oper)) {
      if (this.engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM))
      {
        triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.modelId, "DEM_REMOVE_BEFOR");
        if (triggerModel != null)
        {
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
          triggerParams.put("DEM_PARAMETER_ID", this.dataid);
          triggerParams.put("DEM_PARAMETER_FORMID", this.subformid);
          triggerParams.put("DEM_PARAMETER_FORMDATA", params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
        }

      }
      else if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null)) {
        triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_REMOVE_BEFOR");
        if (triggerModel != null) {
          UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

          triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
          triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
          triggerParams.put("PROCESS_PARAMETER_FORMID", this.subformid);
          triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
          triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
          triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
          triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
          isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
        }

      }

      flag = this.iformService.removeSubformData(this.subformid, instanceid, this.id);
      if (flag) {
        if (this.engineType.equals(EngineConstants.SYS_INSTANCE_TYPE_DEM))
        {
          triggerModel = this.sysDemTriggerService.getSysDemTriggerModel(this.demId, "DEM_REMOVE_AFTER");
          if (triggerModel != null)
          {
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", instanceid);
            triggerParams.put("DEM_PARAMETER_ID", this.dataid);
            triggerParams.put("DEM_PARAMETER_FORMID", this.subformid);
            triggerParams.put("DEM_PARAMETER_FORMDATA", params);
            isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, UserContextUtil.getInstance().getCurrentUserContext(), "synchronous", triggerParams, this.instanceId);
          }

        }
        else if ((this.actDefId != null) && (this.prcDefId != null) && (this.actStepDefId != null)) {
          triggerModel = this.processStepTriggerService.getProcessStepTriggerModel(this.actDefId, this.prcDefId, this.actStepDefId, "FORM_REMOVE_AFTER");
          if (triggerModel != null) {
            UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

            triggerParams.put("PROCESS_PARAMETER_ACTDEFID", this.actDefId);
            triggerParams.put("PROCESS_PARAMETER_ACTDEFSTEPID", this.actStepDefId);
            triggerParams.put("PROCESS_PARAMETER_FORMID", this.subformid);
            triggerParams.put("PROCESS_PARAMETER_INSTANCEID", this.instanceId);
            triggerParams.put("PROCESS_PARAMETER_TASKID", this.taskId);
            triggerParams.put("PROCESS_PARAMETER_EXECUTEID", this.excutionId);
            triggerParams.put("PROCESS_PARAMETER_FORMDATA", params);
            isrun = TriggerAPI.getInstance().excuteEvent(triggerModel, uc, "synchronous", triggerParams, this.instanceId);
          }
        }
      }

    }

    ResponseUtil.write(String.valueOf(flag));
    return null;
  }

  public String subForm_remove()
  {
    String id = getId() == null ? "0" : getId();
    Long subformid = getSubformid();
    Long instanceid = getInstanceId();
    boolean flag = true;
    flag = this.iformService.removeSubformData(subformid, instanceid, id);
    ResponseUtil.write(String.valueOf(flag));
    return null;
  }

  public String openExcelImpPage()
  {
    return "success";
  }

  public String doExcelImp()
  {
    String msg = "";
    if ((this.modelId != null) && (this.subformid != null) && (this.instanceId != null) && (this.upFile != null) && (this.engineType != null)) {
      msg = this.iformService.subSheetExcelImp(this.modelId, this.instanceId, this.subformid, this.upFile, this.engineType);
    }
    ResponseUtil.write(msg);
    return null;
  }

  public String subForm_expExcelModel()
  {
    if ((this.subformid != null) && (this.instanceId != null) && (this.upFile != null)) {
      HttpServletResponse response = ServletActionContext.getResponse();
      this.iformService.exportGridExcelModal(this.subformid, this.instanceId, response);
    }
    return null;
  }

  public String subForm_expExcelData()
  {
    HttpServletResponse response = ServletActionContext.getResponse();
    this.iformService.exportGridExcelData(this.subformid, this.instanceId, response);
    return null;
  }

  public void loadFormInputHistoryJSON()
  {
    if ((this.fieldName != null) && (this.formid != null)) {
      String json = this.iformService.getFormInputHistoryJSON(this.fieldName, this.q, this.formid);
      ResponseUtil.write(json);
    }
  }

  public SysEngineIFormComponentsService getSysEngineIFormComponentsService() { return this.sysEngineIFormComponentsService; }

  public void setSysEngineIFormComponentsService(SysEngineIFormComponentsService sysEngineIFormComponentsService)
  {
    this.sysEngineIFormComponentsService = sysEngineIFormComponentsService;
  }
  public SysEngineIFormService getSysEngineIFormService() {
    return this.sysEngineIFormService;
  }
  public void setSysEngineIFormService(SysEngineIFormService sysEngineIFormService) {
    this.sysEngineIFormService = sysEngineIFormService;
  }
  public SysEngineIFormMapService getSysEngineIFormMapService() {
    return this.sysEngineIFormMapService;
  }

  public void setSysEngineIFormMapService(SysEngineIFormMapService sysEngineIFormMapService) {
    this.sysEngineIFormMapService = sysEngineIFormMapService;
  }
  public SysEngineSubformService getSysEngineSubformService() {
    return this.sysEngineSubformService;
  }

  public void setSysEngineSubformService(SysEngineSubformService sysEngineSubformService) {
    this.sysEngineSubformService = sysEngineSubformService;
  }
  public Long getFormid() {
    return this.formid;
  }
  public void setFormid(Long formid) {
    this.formid = formid;
  }

  public Long getInstanceId()
  {
    return this.instanceId;
  }

  public void setInstanceId(Long instanceId) {
    this.instanceId = instanceId;
  }

  public String getTemplate() {
    return this.template;
  }
  public void setTemplate(String template) {
    this.template = template;
  }

  public FreeMarkerConfigurer getFreemarderConfig()
  {
    return this.freemarderConfig;
  }

  public void setFreemarderConfig(FreeMarkerConfigurer freemarderConfig)
  {
    this.freemarderConfig = freemarderConfig;
  }

  public IFormService getIformService()
  {
    return this.iformService;
  }

  public void setIformService(IFormService iformService)
  {
    this.iformService = iformService;
  }
  public String getTablelist_th() {
    return this.tablelist_th;
  }
  public void setTablelist_th(String tablelist_th) {
    this.tablelist_th = tablelist_th;
  }
  public String getSearch_html() {
    return this.search_html;
  }
  public void setSearch_html(String search_html) {
    this.search_html = search_html;
  }
  public String isPagination() {
    return this.pagination;
  }
  public void setPagination(String pagination) {
    this.pagination = pagination;
  }
  public Long getType() {
    return this.type;
  }
  public void setType(Long type) {
    this.type = type;
  }
  public String getInfo() {
    return this.info;
  }
  public void setInfo(String info) {
    this.info = info;
  }
  public String getRemoveList() {
    return this.removeList;
  }
  public void setRemoveList(String removeList) {
    this.removeList = removeList;
  }
  public String getTitle() {
    return this.title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getStyle() {
    return this.style;
  }
  public void setStyle(String style) {
    this.style = style;
  }
  public String getScript() {
    return this.script;
  }
  public void setScript(String script) {
    this.script = script;
  }
  public String getForminfo() {
    return this.forminfo;
  }
  public void setForminfo(String forminfo) {
    this.forminfo = forminfo;
  }
  public String getContent() {
    return this.content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getParam() {
    return this.param;
  }
  public void setParam(String param) {
    this.param = param;
  }
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Long getSubformid() {
    return this.subformid;
  }
  public void setSubformid(Long subformid) {
    this.subformid = subformid;
  }
  public String getOper() {
    return this.oper;
  }
  public void setOper(String oper) {
    this.oper = oper;
  }
  public Long getDataid() {
    return this.dataid;
  }
  public void setDataid(Long dataid) {
    this.dataid = dataid;
  }
  public OrgUserDAO getOrgUserDAO() {
    return this.orgUserDAO;
  }
  public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
    this.orgUserDAO = orgUserDAO;
  }
  public String getMenuLayoutType() {
    return this.menuLayoutType;
  }
  public void setMenuLayoutType(String menuLayoutType) {
    this.menuLayoutType = menuLayoutType;
  }
  public String getMenuLayoutUpdateFlag() {
    return this.menuLayoutUpdateFlag;
  }
  public void setMenuLayoutUpdateFlag(String menuLayoutUpdateFlag) {
    this.menuLayoutUpdateFlag = menuLayoutUpdateFlag;
  }
  public String getBaseinfo() {
    return this.baseinfo;
  }
  public void setBaseinfo(String baseinfo) {
    this.baseinfo = baseinfo;
  }
  public String getVerifySuccessInfo() {
    return this.verifySuccessInfo;
  }
  public void setVerifySuccessInfo(String verifySuccessInfo) {
    this.verifySuccessInfo = verifySuccessInfo;
  }
  public String getVerifyErrorInfo() {
    return this.verifyErrorInfo;
  }
  public void setVerifyErrorInfo(String verifyErrorInfo) {
    this.verifyErrorInfo = verifyErrorInfo;
  }

  public File getUpFile() {
    return this.upFile;
  }

  public void setUpFile(File upFile) {
    this.upFile = upFile;
  }

  public String getSubformkey() {
    return this.subformkey;
  }
  public void setSubformkey(String subformkey) {
    this.subformkey = subformkey;
  }

  public Long getDemId() {
    return this.demId;
  }

  public void setDemId(Long demId) {
    this.demId = demId;
  }

  public void setSysDemWorkBoxService(SysDemWorkBoxService sysDemWorkBoxService) {
    this.sysDemWorkBoxService = sysDemWorkBoxService;
  }

  public void setSysDemEngineDAO(SysDemEngineDAO sysDemEngineDAO) {
    this.sysDemEngineDAO = sysDemEngineDAO;
  }

  public void setSysDemTriggerService(SysDemTriggerService sysDemTriggerService) {
    this.sysDemTriggerService = sysDemTriggerService;
  }

  public Long getModelId() {
    return this.modelId;
  }

  public void setModelId(Long modelId) {
    this.modelId = modelId;
  }

  public void setModelType(String modelType) {
    this.modelType = modelType;
  }

  public String getModelType() {
    return this.modelType;
  }
  public Long getIsLog() {
    return this.isLog;
  }
  public void setIsLog(Long isLog) {
    this.isLog = isLog;
  }

  public Long getIsShowLog() {
    return this.isShowLog;
  }

  public void set_search(String _search) {
    this._search = _search;
  }

  public Long getEngineType() {
    return this.engineType;
  }

  public void setEngineType(Long engineType) {
    this.engineType = engineType;
  }

  public void setProcessStepTriggerService(ProcessStepTriggerService processStepTriggerService) {
    this.processStepTriggerService = processStepTriggerService;
  }

  public void setActDefId(String actDefId) {
    this.actDefId = actDefId;
  }
  public void setActStepDefId(String actStepDefId) {
    this.actStepDefId = actStepDefId;
  }
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }
  public void setExcutionId(String excutionId) {
    this.excutionId = excutionId;
  }
  public void setPrcDefId(Long prcDefId) {
    this.prcDefId = prcDefId;
  }
  public String getToolsbar() {
    return this.toolsbar;
  }
  public void setToolsbar(String toolsbar) {
    this.toolsbar = toolsbar;
  }

  public String getFieldName() {
    return this.fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  public void setQ(String q) {
    this.q = q;
  }
}