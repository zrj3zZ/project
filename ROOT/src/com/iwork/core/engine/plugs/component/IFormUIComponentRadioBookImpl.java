package com.iwork.core.engine.plugs.component;

import com.iwork.commons.util.JsonUtil;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

public class IFormUIComponentRadioBookImpl extends IFormUIComponentAbst
{

    public IFormUIComponentRadioBookImpl(SysEngineMetadataMap metadataMap, SysEngineIformMap iformMap, String value)
    {
        super(metadataMap, iformMap, value);
    }

    public String getMobileHtmlDefine(HashMap params)
    {
        StringBuffer fieldHtml = new StringBuffer();
        fieldHtml.append("<table width=\"100%\">").append("\n");
        fieldHtml.append("\t<tr>");
        fieldHtml.append("\t\t<td>").append("<input type='text'").append(" name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("' value='").append(getValue()).append("'  >").append("</td>");
        fieldHtml.append("<td>").append("<a href=\"ifrom_mb_radio_address_show.action?fieldName=").append(getIformMapModel().getFieldName()).append("\" data-mini=\"true\" data-role=\"button\" data-inline=\"true\" data-rel=\"dialog\" data-transition=\"pop\">\u5730\u5740\u7C3F</a>").append("</td>");
        fieldHtml.append("</tr>");
        fieldHtml.append("</table>").append("\n");
        return fieldHtml.toString();
    }

    public String getModifyHtmlDefine(HashMap params)
    {
        StringBuffer validateStr = new StringBuffer();
        String fieldLength = "";
        String fieldnotnull = "";
        String icon = "";
        validateStr.append("class = '{");
        if(getMetadataMapModel().getFieldLength() == null)
            getMetadataMapModel().setFieldLength("0");
        if(!getMetadataMapModel().getFieldLength().equals("0"))
        {
            int length = 0;
            if(getMetadataMapModel().getFieldLength().indexOf(",") != -1)
                length = Integer.parseInt(getMetadataMapModel().getFieldLength().substring(0, getMetadataMapModel().getFieldLength().indexOf(","))) + 1;
            else
                length = Integer.parseInt(getMetadataMapModel().getFieldLength());
            validateStr.append("maxlength:").append(length).append(",");
        }
        StringBuffer fieldHtml = new StringBuffer();
        String innerHtml = getIformMapModel().getHtmlInner();
        if(innerHtml == null)
        {
            getIformMapModel().setHtmlInner("");
            innerHtml = "";
        }
        if(innerHtml.toLowerCase().indexOf("readonly") > 1 || innerHtml.toLowerCase().indexOf("disabled") > -1)
            innerHtml = innerHtml.replace("readonly", "readonly onfocus='this.blur()'").replace("disabled", "readonly onfocus='this.blur()'");
        if(getIformMapModel().getFieldNotnull() != null)
            if(getIformMapModel().getFieldNotnull().equals(new Long(1L)))
            {
                validateStr.append("required:true");
                icon = "<font color=red>*</font>";
            } else
            {
                validateStr.append("required:false");
            }
        validateStr.append("}'");
        Long inputWidth = getIformMapModel().getInputWidth();
        if(inputWidth == null || inputWidth.equals(new Long(0L)))
            inputWidth = new Long(100L);
        fieldHtml.append("<div style=\" white-space:nowrap;vertical-align:bottom;\"><input type='text'  ").append(innerHtml).append(" name='").append(getIformMapModel().getFieldName()).append("' ").append(validateStr).append("  id='").append(getIformMapModel().getFieldName()).append("' value='").append(getValue()).append("'  style=\"width:").append(inputWidth).append("px;margin-right:5px;\"  form-type=\"radioAddress\" >").append("<a id='radioBtnhtml' href=\"###\" title=\"\u5355\u9009\u5730\u5740\u8584\"  onclick=\"radio_book(").append(parseConfig()).append(");\"").append(" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-radiobook\"></a>");
        return fieldHtml.toString();
    }

    private String parseConfig()
    {
        String rowData = getIformMapModel().getDisplayEnum() != null ? getIformMapModel().getDisplayEnum().toUpperCase() : "";
        JSONObject json = JsonUtil.strToJson(IFormUIComponentAbst.wrapJsonDisplayEnum(rowData));
        if(rowData == null || rowData.length() == 0 || json == null)
            return (new StringBuilder("'','','','','','','','','")).append(getIformMapModel().getFieldName()).append("'").toString();
        if(getIformMapModel().getHtmlInner() == null)
            getIformMapModel().setHtmlInner("");
        String showType = "";
        String parentDept = "false";
        String currentDept = "false";
        String startDept = null;
        String targetUserNo = null;
        String targetUserId = null;
        String targetUserName = null;
        String targetDeptId = null;
        String targetDeptName = null;
        String defaultField = null;
        if(json.containsKey("SHOWTYPE"))
        {
            showType = (String)json.get("SHOWTYPE");
            if(showType.equals("STARTDEPT"))
            {
                if(json.containsKey("STARTDEPT"))
                    startDept = (String)json.get("STARTDEPT");
            } else
            if(showType.equals("CURRENTDEPT"))
                currentDept = "true";
            else
            if(showType.equals("PARENTDEPT"))
                parentDept = "true";
        }
        if(json.containsKey("PARENTDEPT"))
            try
            {
                Object obj = json.get("PARENTDEPT");
                if(obj instanceof Boolean)
                {
                    boolean temp = ((Boolean)json.get("PARENTDEPT")).booleanValue();
                    if(temp)
                        parentDept = "true";
                } else
                if(obj instanceof String)
                    parentDept = (String)json.get("PARENTDEPT");
            }
            catch(Exception exception) { }
        else
        if(json.containsKey("CURRENTDEPT"))
            try
            {
                Object obj = json.get("CURRENTDEPT");
                if(obj instanceof Boolean)
                {
                    boolean temp = ((Boolean)json.get("CURRENTDEPT")).booleanValue();
                    if(temp)
                        currentDept = "true";
                } else
                if(obj instanceof String)
                    currentDept = (String)json.get("CURRENTDEPT");
            }
            catch(Exception exception1) { }
        else
        if(json.containsKey("STARTDEPT"))
            try
            {
                Object obj = json.get("STARTDEPT");
                if(obj instanceof String)
                    startDept = (String)json.get("STARTDEPT");
                else
                if(obj instanceof Integer)
                {
                    int temp = ((Integer)json.get("STARTDEPT")).intValue();
                    startDept = (new StringBuilder(String.valueOf(temp))).toString();
                }
            }
            catch(Exception exception2) { }
        if(json.containsKey("TARGETUSERNO"))
            try
            {
                targetUserNo = (String)json.get("TARGETUSERNO");
            }
            catch(Exception exception3) { }
        if(json.containsKey("TARGETUSERID"))
            try
            {
                targetUserId = (String)json.get("TARGETUSERID");
            }
            catch(Exception exception4) { }
        if(json.containsKey("TARGETUSERNAME"))
            try
            {
                targetUserName = (String)json.get("TARGETUSERNAME");
            }
            catch(Exception exception5) { }
        if(json.containsKey("TARGETDEPTID"))
            try
            {
                targetDeptId = (String)json.get("TARGETDEPTID");
            }
            catch(Exception exception6) { }
        if(json.containsKey("TARGETDEPTNAME"))
            try
            {
                targetDeptName = (String)json.get("TARGETDEPTNAME");
            }
            catch(Exception exception7) { }
        StringBuffer params = new StringBuffer("");
        defaultField = getIformMapModel().getFieldName();
        params.append((new StringBuilder("'")).append(parentDept).append("',").toString());
        params.append((new StringBuilder("'")).append(currentDept).append("',").toString());
        params.append(startDept != null ? (new StringBuilder("'")).append(startDept).append("',").toString() : "'',");
        params.append(targetUserId != null ? (new StringBuilder("'")).append(targetUserId).append("',").toString() : "'',");
        params.append(targetUserNo != null ? (new StringBuilder("'")).append(targetUserNo).append("',").toString() : "'',");
        params.append(targetUserName != null ? (new StringBuilder("'")).append(targetUserName).append("',").toString() : "'',");
        params.append(targetDeptId != null ? (new StringBuilder("'")).append(targetDeptId).append("',").toString() : "'',");
        params.append(targetDeptName != null ? (new StringBuilder("'")).append(targetDeptName).append("',").toString() : "'',");
        params.append(defaultField != null ? (new StringBuilder("'")).append(defaultField).append("'").toString() : "'',");
        return params.toString();
    }

    public String getReadHtmlDefine(HashMap params)
    {
        StringBuffer fieldHtml = new StringBuffer();
        Long inputWidth = getIformMapModel().getInputWidth();
        if(inputWidth == null || inputWidth.equals(new Long(0L)))
            inputWidth = new Long(100L);
        fieldHtml.append("<input type='hidden' readonly ").append(" name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("' value='").append(getValue()).append("' >").append(getValue());
        return fieldHtml.toString();
    }

    public String getValidateJavaScript(HashMap params)
    {
        return "";
    }

    public String getSubFormColumnModelScript(String columnModelType)
    {
        return null;
    }

    public Map parseToJson(String displayString, String columnModelType, Map rootMap)
    {
        return rootMap;
    }
}
