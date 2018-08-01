package com.iwork.plugs.addressbook.action;

import java.util.HashMap;
import java.util.Iterator;

import net.sf.json.JSONObject;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.AssetClassAddressBookService;
import com.opensymphony.xwork2.ActionSupport;

public class AssetClassAddressBookAction  extends ActionSupport {
	private AssetClassAddressBookService assetClassAddressBookService;

	private String unit;
	private String html;
	private String typeno;
	private String extend;
	private String status;
	
	public String index(){
		html = assetClassAddressBookService.getAssetClassHTML(unit);
		return SUCCESS;
	}
	
	public String getJSON(){
		String json = assetClassAddressBookService.getAssetClassHTML(unit);
		ResponseUtil.write(json);
		return null;
	}
	
	public String getViewHTML(){
		String json = assetClassAddressBookService.getViewHTML(typeno);
		ResponseUtil.write(json);
		return null;
	}
	
	public String getHTML(){
		HashMap<String, Object> property = null;
		String json = "";
		if(extend!=null&&!"".equals(extend)){
			HashMap<String,Object> hash = new HashMap<String,Object>();
			JSONObject jsonObject = JSONObject.fromObject(extend);
			Iterator it = jsonObject.keys();
			if(it.hasNext()){
				Object no = it.next();
				Object obj = jsonObject.get(no);
				property = assetClassAddressBookService.jsonToMap(obj.toString());
				json = assetClassAddressBookService.getHTML(no.toString(),property,status);
			}
		}else{
			if(typeno!=null)
				json = assetClassAddressBookService.getHTML(typeno,property,status);
		}
		ResponseUtil.write(json);
		return null;
	}
	public AssetClassAddressBookService getAssetClassAddressBookService() {
		return assetClassAddressBookService;
	}

	public void setAssetClassAddressBookService(
			AssetClassAddressBookService assetClassAddressBookService) {
		this.assetClassAddressBookService = assetClassAddressBookService;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}

	public String getTypeno() {
		return typeno;
	}

	public void setTypeno(String typeno) {
		this.typeno = typeno;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
