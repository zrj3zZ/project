package com.iwork.core.engine.plugs.component;

import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import java.util.HashMap;
import java.util.Map;

public class IFormComponentTextImpl extends IFormUIComponentAbst
{

    public String getMobileHtmlDefine(HashMap params)
    {
        StringBuffer validateStr = new StringBuffer();
        StringBuffer style = new StringBuffer();
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
        Long inputWidth = getIformMapModel().getInputWidth();
        if(inputWidth == null || inputWidth.equals(new Long(0L)))
            inputWidth = new Long(100L);
        style.append("width:").append(inputWidth).append(";");
        if(innerHtml.toLowerCase().indexOf(" readonly ") > 1 || innerHtml.toLowerCase().indexOf("disabled") > -1)
        {
            innerHtml = innerHtml.replace("readonly", "readonly onfocus='this.blur()'").replace("disabled", "readonly onfocus='this.blur()'");
            style.append("background:#efefef;");
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
        validateStr.append("}'");
        fieldHtml.append("<input type='text'  ").append(innerHtml).append(" style=\"").append(style).append("\" name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("'  value='").append(getValue()).append("' >").append(icon);
        return fieldHtml.toString();
    }

    public IFormComponentTextImpl(SysEngineMetadataMap metadataMap, SysEngineIformMap iformMap, String value)
    {
        super(metadataMap, iformMap, value);
    }

    public String getModifyHtmlDefine(HashMap params)
    {
        StringBuffer validateStr = new StringBuffer();
        StringBuffer style = new StringBuffer();
        String icon = "";
        int length = 20;
        validateStr.append("class = '{");
        if(getMetadataMapModel().getFieldLength() == null)
            getMetadataMapModel().setFieldLength("0");
        if(!getMetadataMapModel().getFieldLength().equals("0"))
        {
            
            if(getMetadataMapModel().getFieldLength().indexOf(",") != -1)
                length = Integer.parseInt(getMetadataMapModel().getFieldLength().substring(0, getMetadataMapModel().getFieldLength().indexOf(","))) + 1;
            else
                length = Integer.parseInt(getMetadataMapModel().getFieldLength());
            validateStr.append("maxlength:").append(length).append(",");
        }
        StringBuffer fieldHtml = new StringBuffer();
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
                icon = "<font style=\"color:red;padding-left:5px\">*</font>";
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
        style.append("width:").append(inputWidth).append("px;");
        fieldHtml.append("<input type='text' maxlength='").append(length).append("'"  ).append(validateStr).append(" ").append(innerHtml).append(" style=\"").append(style).append("\" name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("'  value='").append(getValue()).append("'   form-type='al_textbox'>").append(icon);
        return fieldHtml.toString();
    }

    public String getReadHtmlDefine(HashMap params)
    {
        StringBuffer html = new StringBuffer();
        html.append("<input type=hidden name='").append(getIformMapModel().getFieldName()).append("' value=\"").append(getValue()).append("\">").append(getValue());
        return html.toString();
    }

    public String getSubFormColumnModelScript(String columnModelType)
    {
        return null;
    }

    public Map parseToJson(String displayString, String columnModelType, Map rootMap)
    {
        return null;
    }
}
