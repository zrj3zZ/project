package com.iwork.core.engine.plugs.component;

import com.iwork.commons.util.JsonUtil;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

// Referenced classes of package com.iwork.core.engine.plugs.component:
//            IFormUIComponentAbst

public class IFormUIComponentDeptBookImpl extends IFormUIComponentAbst
{

    public IFormUIComponentDeptBookImpl(SysEngineMetadataMap metadataMap, SysEngineIformMap iformMap, String value)
    {
        super(metadataMap, iformMap, value);
    }

    public String getMobileHtmlDefine(HashMap params)
    {
        StringBuffer fieldHtml = new StringBuffer();
        fieldHtml.append("<input type='text' ").append(" name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("' value='").append(getValue()).append("'  >");
        return fieldHtml.toString();
    }

    public String getModifyHtmlDefine(HashMap params)
    {
        StringBuffer fieldHtml = new StringBuffer();
        StringBuffer validateStr = new StringBuffer();
        StringBuffer style = new StringBuffer();
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
        String innerEnum = getIformMapModel().getDisplayEnum();
        if(innerEnum != null && !innerEnum.equals("") && (innerEnum.toLowerCase().indexOf("remote:") > -1 || innerEnum.toLowerCase().indexOf("messages:") > -1))
            validateStr.append(innerEnum).append(",");
        String innerHtml = getIformMapModel().getHtmlInner();
        if(innerHtml == null)
        {
            getIformMapModel().setHtmlInner("");
            innerHtml = "";
        }
        if(innerHtml.toLowerCase().indexOf(" readonly ") > 1 || innerHtml.toLowerCase().indexOf("disabled") > -1)
        {
            innerHtml = innerHtml.replace("readonly", "readonly onfocus='this.blur()'").replace("disabled", "readonly onfocus='this.blur()'");
            style.append("background-color:#efefef;");
        }
        if(getIformMapModel().getFieldNotnull() != null)
        {
            if(getIformMapModel().getFieldNotnull().equals(new Long(1L)))
            {
                validateStr.append("required:true");
                icon = "<font color=red>*</font>";
            } else
            {
                validateStr.append("required:false");
            }
        } else
        {
            validateStr.append("required:false");
        }
        validateStr.append(",string:true");
        validateStr.append("}'");
        Long inputWidth = getIformMapModel().getInputWidth();
        if(inputWidth == null || inputWidth.equals(new Long(0L)))
            inputWidth = new Long(100L);
        																																																																																			//<a href="###" onclick="dept_book('','','','','','','editForm_model_departmentid','editForm_model_departmentname','editForm_model_departmentname');" title="部门地址薄" class="easyui-linkbutton" plain="true" iconCls="icon-deptbook"></a>
        //fieldHtml.append("<input type='text' readonly ").append(" name='").append(getIformMapModel().getFieldName()).append("' ").append(" id='").append(getIformMapModel().getFieldName()).append("' value='").append(getValue()).append("'  style=\"width:").append(inputWidth).append("px;margin-right:5px;background-color:#efefef\"\" >").append("<a href=\"###\" onclick=\"dept_book(").append(parseConfig()).append(");\" ").append(validateStr).append(" title=\"部门地址薄\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-deptbook\"></a>");
        fieldHtml.append("<input type='text' ").append(validateStr).append(" ").append(innerHtml).append(" name='").append(getIformMapModel().getFieldName()).append("' ").append(" id='").append(getIformMapModel().getFieldName()).append("' value='").append(getValue()).append("'  style=\"width:").append(inputWidth).append("px;margin-right:5px;background-color:#efefef\"/>").append("<a href=\"###\" onclick=\"dept_book(").append(parseConfig()).append(");\" title=\"部门地址薄\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-deptbook\"></a>").append(icon);
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
            parentDept = (String)json.get("PARENTDEPT");
        else
        if(json.containsKey("CURRENTDEPT"))
            currentDept = (String)json.get("CURRENTDEPT");
        else
        if(json.containsKey("STARTDEPT"))
            startDept = (String)json.get("STARTDEPT");
        if(json.containsKey("TARGETDEPTID"))
            targetDeptId = (String)json.get("TARGETDEPTID");
        if(json.containsKey("TARGETDEPTNO"))
            targetDeptId = (String)json.get("TARGETDEPTNO");
        if(json.containsKey("TARGETDEPTNAME"))
            targetDeptName = (String)json.get("TARGETDEPTNAME");
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
        if(getIformMapModel().getHtmlInner() == null)
            getIformMapModel().setHtmlInner("");
        fieldHtml.append(getValue()).append("<input type='hidden'").append(getIformMapModel().getHtmlInner()).append(" name='").append(getIformMapModel().getFieldName()).append("' ").append(" value='").append(getValue()).append("' >");
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
