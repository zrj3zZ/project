package com.iwork.commons.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.iwork.commons.component.IWorkComponent_DeptSelect;
import com.opensymphony.xwork2.util.ValueStack;

public class IWorkComponentTag_DeptSelect extends ComponentTagSupport {
	private String name;
	private String targetname;
	private String targetid;
	private String height;
	private String width;
	@Override
	public Component getBean(ValueStack arg0, HttpServletRequest arg1,
			HttpServletResponse arg2) {
		return new IWorkComponent_DeptSelect(arg0);
	}
	//获得参数     
    protected void populateParams() {     
        super.populateParams();     
        IWorkComponent_DeptSelect ic = (IWorkComponent_DeptSelect)component;     
        ic.setName(name);  
        ic.setTargetname(targetname);
        ic.setTargetid(targetid);
        ic.setWidth(width);
        ic.setHeight(height);
           
    }   
    
	public void setName(String name) {
		this.name = name;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public void setTargetname(String targetname) {
		this.targetname = targetname;
	}
	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}

}
