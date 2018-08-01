package com.iwork.commons.component;

import java.io.IOException;
import java.io.Writer;

import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.log4j.Logger;
public class IWorkComponent_DeptSelect extends Component {
	private static Logger logger = Logger.getLogger(IWorkComponent_DeptSelect.class);

	private String name;
	private String targetname;
	private String targetid;
	private String width;
	private String height;
	public IWorkComponent_DeptSelect(ValueStack stack) {
		super(stack);
	}
	 public boolean start(Writer writer) {  
		 StringBuffer str = new StringBuffer();
         boolean result = super.start(writer);  
         try {  
        	 if("".equals(width)||width==null){
        		 width = "120";
        	 }
        	 if("".equals(height)||height==null){
        		 height = "350";
        	 }
             str.append("&nbsp;<a href=\"###\" onclick=\"openDept('").append(targetid).append("','").append(targetname).append("');\"><img alt=\"打开部门列表\" src=\"iwork_img/but_jgt.gif\" border=\"0\"></a>").append("\n");
             str.append("<script>");
             str.append("function openDept(id,name){").append("\n");
             str.append("var url=\"departmentRadio_select.action?fieldid=").append(targetid).append("&fieldname=").append(targetname).append("\";").append("\n");
//             str.append("alert(url);").append("\n");
             str.append("window.showModalDialog(url, window, \"dialogWidth=").append(width).append("px;dialogHeight=").append(height).append("px\");//模态对话框").append("\n");
             str.append("}").append("\n");
             str.append("</script>");
             writer.write(str.toString());  
         } catch (Exception e) {  
        	 logger.error(e,e);
         }  
         return result;  
     }
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getTargetname() {
		return targetname;
	}
	public void setTargetname(String targetname) {
		this.targetname = targetname;
	}
	public String getTargetid() {
		return targetid;
	}
	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}     

}
