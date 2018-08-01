package com.iwork.core.engine.log.service;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.iform.dao.*;
import com.iwork.core.engine.iform.model.SysEngineFormBind;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.log.constans.IFormLogConstant;
import com.iwork.core.engine.log.dao.SysIformActionLogDAO;
import com.iwork.core.engine.log.model.SysIformActionLog;
import com.iwork.core.engine.log.model.SysIformActionLogItem;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataMapDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;

import java.util.*;

public class SysIformActionLogService
{

    public SysIformActionLogService()
    {
    }

    public void addFormUpdateLog(Long modelId, SysEngineFormBind formBindModel, Map commitData)
    {
        if(modelId == null)
            return;
        HashMap formdata = null;
        SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formBindModel.getFormid());
        List maplist = sysEngineIFormMapDAO.getDataList(formBindModel.getFormid());
        SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
        List semmlist = sysEngineMetadataMapDAO.getDataList(sysEngineIform.getMetadataid());
        try
        {
            formdata = iformDataDAO.getFormData(maplist, metadataModel.getEntityname(), formBindModel.getDataid());
        }
        catch(Exception exception) { }
        if(formdata == null)
            return;
        boolean isAddLog = false;
        SysIformActionLog sial = null;
        List logitemlist = null;
        for(Iterator iterator = semmlist.iterator(); iterator.hasNext();)
        {
            SysEngineMetadataMap model = (SysEngineMetadataMap)iterator.next();
            if(model.getFieldname() != null && !model.getFieldname().equals("ID"))
            {
                String update_value = "";
                String src_value = "";
                Object obj = commitData.get(model.getFieldname());
                Object old_obj = formdata.get(model.getFieldname());
                if(obj != null)
                {
                	if(obj instanceof String[]){
                		String value[] = (String[])obj;
                		update_value = value[0];
                	}else{
                		update_value=(String) obj;
                	}
                    if(old_obj != null)
                    {
                        if(old_obj instanceof Date)
                        {
                            Calendar c = Calendar.getInstance();
                            c.setTime((Date)old_obj);
                            int hour = c.get(10);
                            int min = c.get(12);
                            if(hour == 0 && min == 0)
                                src_value = UtilDate.dateFormat((Date)old_obj);
                            else
                                src_value = UtilDate.datetimeFormat((Date)old_obj);
                        } else
                        {
                            src_value = old_obj.toString();
                        }
                    } else
                    {
                        src_value = "";
                    }
                    if(!src_value.equals(update_value))
                    {
                        if(!isAddLog)
                        {
                            sial = new SysIformActionLog();
                            sial.setType(IFormLogConstant.IFORM_LOG_TYPE_DEM);
                            sial.setCreatedate(new Date());
                            String userid = UserContextUtil.getInstance().getCurrentUserId();
                            sial.setUserid(userid);
                            sial.setModelId(modelId);
                            sial.setActionType("UPDATE");
                            sial.setFormId(formBindModel.getFormid());
                            sial.setFormName(sysEngineIform.getIformTitle());
                            sial.setInstanceId(formBindModel.getInstanceid());
                            sial.setKeyId(formBindModel.getDataid());
                            sysIformActionLogDAO.save(sial);
                            isAddLog = true;
                            logitemlist = new ArrayList();
                        }
                        SysIformActionLogItem siali = new SysIformActionLogItem();
                        siali.setTablename(metadataModel.getEntityname());
                        siali.setFieldName(model.getFieldname());
                        siali.setFieldTitle(model.getFieldtitle());
                        if(update_value == null || update_value.equals(""))
                            update_value = "\u7A7A";
                        if(src_value == null || src_value.equals(""))
                            src_value = "\u7A7A";
                        siali.setNewValue(update_value);
                        siali.setOldValue(src_value);
                        siali.setLogid(sial.getId());
                        logitemlist.add(siali);
                    }
                }
            }
        }

        if(logitemlist != null && logitemlist.size() > 0)
            sysIformActionLogDAO.saveItemList(logitemlist);
    }

    public void addFormAddLog(Long modelId, Long formId, Long instanceId, Long dataId, Map commitData)
    {
        if(modelId == null)
            return;
        SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formId);
        SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
        List semmlist = sysEngineMetadataMapDAO.getDataList(sysEngineIform.getMetadataid());
        boolean isAddLog = false;
        SysIformActionLog sial = null;
        List logitemlist = null;
        for(Iterator iterator = semmlist.iterator(); iterator.hasNext();)
        {
            SysEngineMetadataMap model = (SysEngineMetadataMap)iterator.next();
            if(model.getFieldname() != null && !model.getFieldname().equals("ID"))
            {
                String value = "";
                Object obj = commitData.get(model.getFieldname());
                if(obj != null)
                {
                	if(obj instanceof String[]){
                		String temp[] = (String[])obj;
                		value = temp[0];
                	}else{
                		value=(String) obj;
                	}
                    if(value != null && !value.equals(""))
                    {
                        if(!isAddLog)
                        {
                            sial = new SysIformActionLog();
                            sial.setType(IFormLogConstant.IFORM_LOG_TYPE_DEM);
                            sial.setCreatedate(new Date());
                            String userid = UserContextUtil.getInstance().getCurrentUserId();
                            sial.setUserid(userid);
                            sial.setModelId(modelId);
                            sial.setActionType("ADD");
                            sial.setFormId(formId);
                            sial.setFormName(sysEngineIform.getIformTitle());
                            sial.setInstanceId(instanceId);
                            sial.setKeyId(dataId);
                            sysIformActionLogDAO.save(sial);
                            isAddLog = true;
                            logitemlist = new ArrayList();
                        }
                        SysIformActionLogItem siali = new SysIformActionLogItem();
                        siali.setTablename(metadataModel.getEntityname());
                        siali.setFieldName(model.getFieldname());
                        siali.setFieldTitle(model.getFieldtitle());
                        siali.setNewValue(value);
                        siali.setOldValue("\u7A7A");
                        siali.setLogid(sial.getId());
                        logitemlist.add(siali);
                    }
                }
            }
        }

        if(logitemlist != null && logitemlist.size() > 0)
            sysIformActionLogDAO.saveItemList(logitemlist);
    }

    public void addFormDelLog(Long modelId, SysEngineFormBind formBindModel)
    {
        if(modelId == null)
            return;
        HashMap formdata = null;
        SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formBindModel.getFormid());
        List maplist = sysEngineIFormMapDAO.getDataList(formBindModel.getFormid());
        SysEngineMetadata metadataModel = sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
        List semmlist = sysEngineMetadataMapDAO.getDataList(sysEngineIform.getMetadataid());
        try
        {
            formdata = iformDataDAO.getFormData(maplist, metadataModel.getEntityname(), formBindModel.getDataid());
        }
        catch(Exception exception) { }
        if(formdata == null)
            return;
        boolean isAddLog = false;
        SysIformActionLog sial = null;
        List logitemlist = null;
        for(Iterator iterator = semmlist.iterator(); iterator.hasNext();)
        {
            SysEngineMetadataMap model = (SysEngineMetadataMap)iterator.next();
            if(model.getFieldname() != null && !model.getFieldname().equals("ID"))
            {
                String update_value = "";
                String src_value = "";
                Object old_obj = formdata.get(model.getFieldname());
                if(old_obj != null)
                {
                    if(old_obj instanceof Date)
                    {
                        Calendar c = Calendar.getInstance();
                        c.setTime((Date)old_obj);
                        int hour = c.get(10);
                        int min = c.get(12);
                        if(hour == 0 && min == 0)
                            src_value = UtilDate.dateFormat((Date)old_obj);
                        else
                            src_value = UtilDate.datetimeFormat((Date)old_obj);
                    } else
                    {
                        src_value = old_obj.toString();
                    }
                } else
                {
                    src_value = "";
                }
                if(src_value != null && !src_value.equals(""))
                {
                    if(!isAddLog)
                    {
                        sial = new SysIformActionLog();
                        sial.setType(IFormLogConstant.IFORM_LOG_TYPE_DEM);
                        sial.setCreatedate(new Date());
                        String userid = UserContextUtil.getInstance().getCurrentUserId();
                        sial.setUserid(userid);
                        sial.setModelId(modelId);
                        sial.setActionType("DEL");
                        sial.setFormId(formBindModel.getFormid());
                        sial.setFormName(sysEngineIform.getIformTitle());
                        sial.setInstanceId(formBindModel.getInstanceid());
                        sial.setKeyId(formBindModel.getDataid());
                        sysIformActionLogDAO.save(sial);
                        isAddLog = true;
                        logitemlist = new ArrayList();
                    }
                    SysIformActionLogItem siali = new SysIformActionLogItem();
                    siali.setTablename(metadataModel.getEntityname());
                    siali.setFieldName(model.getFieldname());
                    siali.setFieldTitle(model.getFieldtitle());
                    siali.setNewValue(update_value);
                    siali.setOldValue(src_value);
                    siali.setLogid(sial.getId());
                    logitemlist.add(siali);
                }
            }
        }

        if(logitemlist != null && logitemlist.size() > 0)
            sysIformActionLogDAO.saveItemList(logitemlist);
    }

    public String showDemLog(Long demId, int currenPage, Long instanceId)
    {
        if(currenPage == 0)
            currenPage = 1;
        int numCount = 10;
        int count = numCount * (currenPage - 1);
        StringBuffer html = new StringBuffer();
        html.append("<table width='100%' border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"contentTable\">\n");
        List<SysIformActionLog> list = sysIformActionLogDAO.getActionLogList(demId, instanceId, count, numCount);
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            SysIformActionLog model = (SysIformActionLog)iterator.next();
            Date createDate = model.getCreatedate();
            String datestr = UtilDate.getDaysBeforeNow(createDate);
            String actionType = "";
            actionType = getLogTypeIcon(model.getActionType());
            UserContext uc = UserContextUtil.getInstance().getUserContext(model.getUserid());
            if(uc != null)
            {
                html.append("<tr >\n");
                html.append("<td onclick=\"showDialog(").append(model.getId()).append(")\">").append(actionType).append("</td>");
                html.append("<td onclick=\"showDialog(").append(model.getId()).append(")\">").append(model.getFormName()).append("</td>");
                html.append("<td onclick=\"showDialog(").append(model.getId()).append(")\">").append(uc.get_userModel().getUsername()).append("[").append(model.getUserid()).append("]</td>");
                html.append("<td onclick=\"showDialog(").append(model.getId()).append(")\"><span title='").append(UtilDate.datetimeFormat(createDate)).append("'>").append(datestr).append("</span></td>");
                if(SecurityUtil.isSuperManager())
                    html.append("<td ><a  href=\"javascript:dellog(").append(model.getId()).append(")\" >").append("<img src='iwork_img/del3.gif' border='0'/>").append("</a></td>");
                html.append("</tr>\n");
                count++;
            }
        }

        html.append("<tr></tr>\n");
        html.append("</table>\n");
        return html.toString();
    }

    public String getLogTypeIcon(String actionType)
    {
        String iconImg = "";
        if(actionType == null)
            iconImg = "<img src='iwork_img/warning.gif' title='\u672A\u77E5' border='0'>\u672A\u77E5";
        else
        if(actionType.equals("ADD"))
            iconImg = "<img src='iwork_img/icon/adds.gif' title='\u589E\u52A0' border='0'>\u65B0\u589E";
        else
        if(actionType.equals("UPDATE"))
            iconImg = "<img src='iwork_img/icon/edits.gif' title='\u4FEE\u6539'  border='0'>\u4FEE\u6539";
        else
        if(actionType.equals("UPDATE"))
            iconImg = "<img src='iwork_img/icon/dels.gif' title='\u5220\u9664'  border='0'>\u5220\u9664";
        return iconImg;
    }

    public List showDemLogItem(Long logid)
    {
        List list = sysIformActionLogDAO.getActionLogItemList(logid);
        return list;
    }

    public void deleteLogItem(Long logid)
    {
        SysIformActionLog model = sysIformActionLogDAO.getActionLogModel(logid);
        List list = sysIformActionLogDAO.getActionLogItemList(model.getId());
        SysIformActionLogItem siali;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); sysIformActionLogDAO.deleteLogItem(siali))
            siali = (SysIformActionLogItem)iterator.next();

        sysIformActionLogDAO.deleteLog(model);
    }

    public SysIformActionLogDAO getSysIformActionLogDAO()
    {
        return sysIformActionLogDAO;
    }

    public void setSysIformActionLogDAO(SysIformActionLogDAO sysIformActionLogDAO)
    {
        this.sysIformActionLogDAO = sysIformActionLogDAO;
    }

    public SysEngineIFormDAO getSysEngineIFormDAO()
    {
        return sysEngineIFormDAO;
    }

    public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO)
    {
        this.sysEngineIFormDAO = sysEngineIFormDAO;
    }

    public SysEngineIFormMapDAO getSysEngineIFormMapDAO()
    {
        return sysEngineIFormMapDAO;
    }

    public void setSysEngineIFormMapDAO(SysEngineIFormMapDAO sysEngineIFormMapDAO)
    {
        this.sysEngineIFormMapDAO = sysEngineIFormMapDAO;
    }

    public SysEngineMetadataDAO getSysEngineMetadataDAO()
    {
        return sysEngineMetadataDAO;
    }

    public void setSysEngineMetadataDAO(SysEngineMetadataDAO sysEngineMetadataDAO)
    {
        this.sysEngineMetadataDAO = sysEngineMetadataDAO;
    }

    public SysEngineMetadataMapDAO getSysEngineMetadataMapDAO()
    {
        return sysEngineMetadataMapDAO;
    }

    public void setSysEngineMetadataMapDAO(SysEngineMetadataMapDAO sysEngineMetadataMapDAO)
    {
        this.sysEngineMetadataMapDAO = sysEngineMetadataMapDAO;
    }

    public SysEngineFormBindDAO getIformBindDAO()
    {
        return iformBindDAO;
    }

    public void setIformBindDAO(SysEngineFormBindDAO iformBindDAO)
    {
        this.iformBindDAO = iformBindDAO;
    }

    public IFormDataDAO getIformDataDAO()
    {
        return iformDataDAO;
    }

    public void setIformDataDAO(IFormDataDAO iformDataDAO)
    {
        this.iformDataDAO = iformDataDAO;
    }

    public SysEngineSubformDAO getSysEngineSubformDAO()
    {
        return sysEngineSubformDAO;
    }

    public void setSysEngineSubformDAO(SysEngineSubformDAO sysEngineSubformDAO)
    {
        this.sysEngineSubformDAO = sysEngineSubformDAO;
    }

    private SysEngineSubformDAO sysEngineSubformDAO;
    private SysIformActionLogDAO sysIformActionLogDAO;
    private SysEngineIFormDAO sysEngineIFormDAO;
    private SysEngineIFormMapDAO sysEngineIFormMapDAO;
    private SysEngineMetadataDAO sysEngineMetadataDAO;
    private SysEngineMetadataMapDAO sysEngineMetadataMapDAO;
    private SysEngineFormBindDAO iformBindDAO;
    private IFormDataDAO iformDataDAO;
}
