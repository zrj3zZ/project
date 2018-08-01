
package com.iwork.core.engine.plugs.component;

import java.util.HashMap;
import java.util.Map;

import com.iwork.commons.util.UtilString;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;


/**
 * @author David.Yang
 * 
 * @preserve 声明此方法不被JOC混淆.
 */
public class IFormUIComponentTextAreaImpl extends IFormUIComponentAbst {

	/**
	 * @param value
	 * @param metaDataMapModel
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public IFormUIComponentTextAreaImpl(SysEngineMetadataMap metadataMap,SysEngineIformMap iformMap,String value) {
		super(metadataMap,iformMap, value);
	}
	/**
	 * 获得移动端显示的组件代码
	 */
	public String getMobileHtmlDefine(HashMap params) {
		StringBuffer fieldHtml = new StringBuffer();
		String fieldValue = new UtilString(getValue()).replace("__eol__", "\n");
		fieldValue = new UtilString(getValue()).replace("<br>", "\n");
		StringBuffer fieldnotnull = new StringBuffer();
		fieldnotnull.append("{");
		if (this.getIformMapModel().getInputHeight() == 0)
			getIformMapModel().setInputHeight(new Long(5));// 默认是5行
		if(this.getMetadataMapModel()!=null){
			if(this.getMetadataMapModel().getFieldLength()!=null && !"".equals(this.getMetadataMapModel().getFieldLength())){
				fieldnotnull.append("maxlength:").append(this.getMetadataMapModel().getFieldLength()).append(",");
			}
			fieldnotnull.append("");
			if(this.getMetadataMapModel().getFieldnotnull()==1||getIformMapModel().getFieldNotnull()==1){
				fieldnotnull.append("required:true");
			}else{
				fieldnotnull.append("required:false");
			}
		}
		fieldnotnull.append("} ");
		if(getIformMapModel().getHtmlInner()==null)getIformMapModel().setHtmlInner("");
		
		Long inputWidth = this.getIformMapModel().getInputWidth();
		if(inputWidth==null||inputWidth.equals(new Long(0)))inputWidth=new Long(100);
		Long inputHeight = this.getIformMapModel().getInputHeight();
		if(inputHeight==null||inputHeight.equals(new Long(0)))inputHeight=new Long(30); 
		
		fieldHtml.append("<textarea  class=\"").append(fieldnotnull).append(getIformMapModel().getHtmlInner()).append("\"  name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("'  >").append(fieldValue).append("</textarea>");
		if(this.getMetadataMapModel().getFieldnotnull()==1||getIformMapModel().getFieldNotnull()==1){
			fieldHtml.append("<font color=red>*</font>");
		}	
		return fieldHtml.toString();
	}
	/**
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public String getModifyHtmlDefine(HashMap params) {
		StringBuffer fieldHtml = new StringBuffer();
		String fieldValue = new UtilString(getValue()).replace("__eol__", "\n");
		fieldValue = new UtilString(getValue()).replace("<br>", "\n");
		StringBuffer fieldnotnull = new StringBuffer();
		fieldnotnull.append("{");
		if (this.getIformMapModel().getInputHeight() == 0)
			getIformMapModel().setInputHeight(new Long(5));// 默认是5行
		
		if(this.getMetadataMapModel().getFieldLength()!=null){
			fieldnotnull.append("maxlength:").append(this.getMetadataMapModel().getFieldLength()).append(",");
		}
		fieldnotnull.append("");
		if(this.getMetadataMapModel().getFieldnotnull()==1||getIformMapModel().getFieldNotnull()==1){
			fieldnotnull.append("required:true");
		}else{
			fieldnotnull.append("required:false");
		}
		fieldnotnull.append("} ");
		if(getIformMapModel().getHtmlInner()==null)getIformMapModel().setHtmlInner("");
		
		Long inputWidth = this.getIformMapModel().getInputWidth();
		if(inputWidth==null||inputWidth.equals(new Long(0)))inputWidth=new Long(100);
		Long inputHeight = this.getIformMapModel().getInputHeight();
		if(inputHeight==null||inputHeight.equals(new Long(0)))inputHeight=new Long(30); 
		
		fieldHtml.append("<textarea  class=\"").append(fieldnotnull).append(getIformMapModel().getHtmlInner()).append("\"  name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("'  style=\"width:").append(inputWidth).append("px;height:").append(inputHeight).append("px;\"  >").append(fieldValue).append("</textarea>");
		if(this.getMetadataMapModel().getFieldnotnull()==1||getIformMapModel().getFieldNotnull()==1){
			fieldHtml.append("<font color=red>*</font>");
		}		 
		return fieldHtml.toString();
	}

	
	/**
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public String getReadHtmlDefine(HashMap params) {
		StringBuffer html = new StringBuffer();
		String fieldValue = new UtilString(getValue()).replace("__eol__", "<br>");
		fieldValue = fieldValue.replace("\n","&nbsp;");
		fieldValue = fieldValue.replace(" ","&nbsp;");
		fieldValue = fieldValue.replace("\t","&nbsp;&nbsp;&nbsp;&nbsp;");
		fieldValue = fieldValue.replace("\r","<br/>");
		Long height=getIformMapModel().getInputHeight()*13;
		html.append("<span style=\"display:none\"><textarea name='").append(getIformMapModel().getFieldName()).append("' id='").append(getIformMapModel().getFieldName()).append("'  style=\"width:680px;height:50px;\"  >").append(getValue()).append("</textarea></span><div style='height:"+height+"'>").append(fieldValue).append("</div>");
		return html.toString();
	}
	
	/**
	 * 获取当前组件的客户端JavaScript校验代码，包括字段超长、是否允许为空、常规值校验
	 * 
	 * @param params
	 * @return
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public String getValidateJavaScript(HashMap params) {
		
		return "";
	}

	public String getSubFormColumnModelScript(String columnModelType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> parseToJson(String displayString,String columnModelType,Map<String,Object> rootMap) {
		if(rootMap==null){
			rootMap = new HashMap<String,Object>();
		}
		rootMap.put("edittype", "textarea");
		return rootMap;
	}
}
