package com.iwork.plugs.addressbook.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import com.iwork.plugs.addressbook.dao.AssetClassAddressBookDAO;

public class AssetClassAddressBookService {
	private AssetClassAddressBookDAO assetClassAddressBookDAO;
	
	public String getAssetClassHTML(String unit){
		StringBuffer html = new StringBuffer();
		List<HashMap<String, String>>  list = assetClassAddressBookDAO.getAssetClass(unit);
		if(list!=null&&list.size()>0){
			for (HashMap<String, String> hash : list) {
				html.append("<tr onclick=\"selectAsset('").append(hash.get("TYPENO")).append("','").append(hash.get("TYPENAME")).append("')\" class=\"row_item\" id=\"tr-").append(hash.get("TYPENO")).append("\">").append("\n");
				html.append("  <td>").append("\n");
				html.append("    <span title=\"类别编号:[").append(hash.get("TYPENO")).append("]\">").append(hash.get("TYPENAME")).append("</span>").append("\n");
				html.append("  </td>").append("\n");
				html.append("</tr>").append("\n");
			}
		}
		return html.toString();
	}

	public String getViewHTML(String typeno){
		StringBuffer html = new StringBuffer();
		List<HashMap<String,Object>> list = assetClassAddressBookDAO.getPropertyByTypeno(typeno);
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				HashMap<String,Object> hash = list.get(i);
				if(i%2==0){
					html.append("<tr>").append("\n");
				}
				html.append("  <td class=\"td_title\" id=\"title_").append(hash.get("PROPERTYNO")).append("\" width=\"15%\">").append("\n");
				html.append("    "+hash.get("PROPERTYNAME")).append("\n");
				html.append("  </td>").append("\n");
				html.append("  <td class=\"td_data\" id=\"data_").append(hash.get("PROPERTYNO")).append("\" width=\"35%\">");
				html.append("    <input type='text' class = ''  style=\"width:100px;\" name='NAME' id='NAME' disabled=disabled   form-type='al_textbox'>");
				if("是".equals(hash.get("REQUIRED"))){
					html.append("<font style=\"color:red;padding-left:5px\">*</font>");
				}
				html.append("\n");
				html.append("  </td>").append("\n");
				if(i%2==1){
					html.append("</tr>").append("\n");
				}
				if(i==(list.size()-1)&&i%2==0){
					html.append("</tr>").append("\n");
				}
			}
		}
		return html.toString();
	}
	/**
	 * 拼装html页面
	 * @param typeno
	 * @return
	 */
	public String getHTML(String typeno,HashMap<String, Object> property,String status){
		String html = "";
		html = this.buildHTML(typeno,property,status);
		return html;
	}
	
	public String buildHTML(String typeno,HashMap<String, Object> property,String status){
		StringBuffer html = new StringBuffer();
		StringBuffer innerhtml = new StringBuffer();
		//只读
		if("readonly".equals(status)){
			innerhtml.append(" readonly ");
		}
		//类型名称
		String typename = assetClassAddressBookDAO.getTypenameByNo(typeno);
		html.append("<tr id=\"addHTML\" name=\"").append(typeno).append("\"><td><fieldset style=\"border-color: #cccccc;\"><legend align='left' style=\"margin-left:50px\">").append(typename).append("</legend><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width='100%' align='center'>");
		List<HashMap<String,Object>> list = assetClassAddressBookDAO.getPropertyByTypeno(typeno);
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				HashMap<String,Object> hash = list.get(i);
				//取值
				String val = "";
				if(property!=null)
					val = property.get(hash.get("PROPERTYNO"))==null?"":property.get(hash.get("PROPERTYNO")).toString();
				if("文本".equals(hash.get("DATATYPE"))){
					html.append("<tr align=\"center\" class=\"").append(typeno).append("\">").append("\n");
					html.append("  <td class=\"td_title\" id=\"title_").append(hash.get("PROPERTYNO")).append("\">").append("\n");
					html.append("    "+hash.get("PROPERTYNAME")).append("\n");
					html.append("  </td>").append("\n");
					html.append("  <td class=\"td_data\" id=\"data_").append(hash.get("PROPERTYNO")).append("\" colspan=\"3\">");
					html.append("    <textarea ").append(innerhtml).append(" form-type='assetInput' data-type='textarea' oninput=\"\" onpropertychange=\"\" class=\"{");
					innerhtml = new StringBuffer();
					//表单验证
					if(!"readonly".equals(status)){
						if(Integer.parseInt(hash.get("DL").toString())!=0){
							html.append("maxlength:").append(Integer.parseInt(hash.get("DL").toString())).append(",");
						}
						if("是".equals(hash.get("REQUIRED"))){
							html.append("required:true");
						}else{
							html.append("required:false");
						}
					}
					html.append("} \" name='").append(hash.get("PROPERTYNO")).append("' id='").append(hash.get("PROPERTYNO")).append("' style=\"width:630px;height:50px;\" >");
					//填值
					html.append(val);
					html.append("</textarea>");
					if("是".equals(hash.get("REQUIRED"))){
						html.append("<font style=\"color:red;padding-left:5px\">*</font>");
					}
					html.append("  </td>").append("\n");
					html.append("</tr>").append("\n");
				}else{
					if(i%2==0){
						html.append("<tr width='100%' align=\"center\" class=\"").append(typeno).append("\">").append("\n");
					}
					html.append("  <td class=\"td_title\" id=\"title_").append(hash.get("PROPERTYNO")).append("\" width=\"15%\">").append("\n");
					html.append("    "+hash.get("PROPERTYNAME")).append("\n");
					html.append("  </td>").append("\n");
					html.append("  <td class=\"td_data\" id=\"data_").append(hash.get("PROPERTYNO")).append("\" width=\"35%\">");
					html.append("    <input type='text' form-type='assetInput' oninput=\"\" onpropertychange=\"\" class = '{");
					//判断是否为日期格式
					if("日期".equals(hash.get("DATATYPE"))){
						if(!"readonly".equals(status)){
							innerhtml.append(" onfocus=\"WdatePicker()\"");
						}
						innerhtml.append(" data-type='date'");
					}
					if("日期时间".equals(hash.get("DATATYPE"))){
						if(!"readonly".equals(status)){
							innerhtml.append(" onfocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})\"");
						}
						innerhtml.append(" data-type='datetime'");
					}
					if("字符".equals(hash.get("DATATYPE"))){
						innerhtml.append(" data-type='text'");
					}
					if("数值".equals(hash.get("DATATYPE"))){
						innerhtml.append(" data-type='number'");
					}
					//表单验证
					if(!"readonly".equals(status)){
						if(Integer.parseInt(hash.get("DL").toString())!=0){
							html.append("maxlength:").append(Integer.parseInt(hash.get("DL").toString())).append(",");
						}
						if("是".equals(hash.get("REQUIRED"))){
							html.append("required:true");
						}else{
							html.append("required:false");
						}
						if("字符".equals(hash.get("DATATYPE"))){
							
						}
						if("数值".equals(hash.get("DATATYPE"))){
							html.append(",number:true");
						}
					}
					html.append("}' ").append(innerhtml).append(" style=\"width:100px;\" name='").append(hash.get("PROPERTYNO")).append("' id='").append(hash.get("PROPERTYNO")).append("' form-type='al_textbox'");
					innerhtml = new StringBuffer();
					if("readonly".equals(status)){
						innerhtml.append(" readonly ");
					}
					//填值
					html.append(" value=\"").append(val).append("\" />");
					//必填项加红色星号
					if("是".equals(hash.get("REQUIRED"))){
						html.append("<font style=\"color:red;padding-left:5px\">*</font>");
					}
					html.append("\n");
					html.append("  </td>").append("\n");
					if(i%2==1){
						html.append("</tr>").append("\n");
					}
					if(i==(list.size()-1)&&i%2==0){
						html.append("</tr>").append("\n");
					}
				}
			}
		}else{
			return "";
		}
		html.append("</table></fieldset></td></tr>");
		return html.toString();
	}
	
	public HashMap<String,Object> jsonToMap(Object json){
		HashMap<String,Object> hash = new HashMap<String,Object>();
		JSONObject jsonObject = JSONObject.fromObject(json);
		Iterator it = jsonObject.keys();
		while (it.hasNext())  
	       {  
	           String key = String.valueOf(it.next());  
	           Object value = jsonObject.get(key);  
	           hash.put(key, value);  
	       } 
		return hash;
	}
	public AssetClassAddressBookDAO getAssetClassAddressBookDAO() {
		return assetClassAddressBookDAO;
	}

	public void setAssetClassAddressBookDAO(
			AssetClassAddressBookDAO assetClassAddressBookDAO) {
		this.assetClassAddressBookDAO = assetClassAddressBookDAO;
	}
	
}
