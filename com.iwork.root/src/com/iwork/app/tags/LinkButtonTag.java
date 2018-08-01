package com.iwork.app.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.apache.struts2.views.jsp.ui.AbstractUITag;
import com.opensymphony.xwork2.util.ValueStack;


public class LinkButtonTag extends AbstractUITag {
	private String text;
	private String icon;
	private String type;
	private String oid;
    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new LinkButton(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();

        LinkButton linkbutton = (LinkButton)component;
        
        linkbutton.setOid(oid);
        linkbutton.setText(text);
        linkbutton.setIcon(icon);
        linkbutton.setType(type);
    }
	@StrutsTagAttribute(description="",type="String")
	public void setOid(String oid) {
		this.oid = oid;
	}
	@StrutsTagAttribute(description="",type="String")
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@StrutsTagAttribute(description="",type="String")
	public void setType(String  type) {
		this.type = type;
	}
	@StrutsTagAttribute(description="",type="String")
	public void setText(String  text) {
		this.text = text;
	}
}
