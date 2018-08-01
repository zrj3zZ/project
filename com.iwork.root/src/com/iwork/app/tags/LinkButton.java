package com.iwork.app.tags;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.iwork.app.navigation.operation.cache.SysNavOperationCache;
import com.iwork.app.navigation.operation.model.SysNavOperation;
import com.iwork.app.tags.commons.LinkBtnTypeCommon;
import com.iwork.core.db.DBUtil;
import com.iwork.core.security.NavSecurityUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.log4j.Logger;
@StrutsTag(name="linkbutton", tldTagClass="com.iwork.app.tags.LinkButtonTag", description="linkBtn")
public class LinkButton extends UIBean {
	private static Logger logger = Logger.getLogger(LinkButton.class);
	public static final String TEMPLATE_NAME_1 = "toolbar_link"; 
	public static final String TEMPLATE_NAME_2 = "icon_link"; 
	public static final String TEMPLATE_NAME_3 = "btn_link"; 
	private String oid;
	private String text;
	private String icon;
	private String type;
	public LinkButton(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

	@Override
	protected String getDefaultTemplate() {
		//判断linkbutton 类型
		if(type==null){
			return TEMPLATE_NAME_1;
		}else if(type.equals(LinkBtnTypeCommon.TOOLBAR_LINKBTN)){
			return TEMPLATE_NAME_1;
		}else if(type.equals(LinkBtnTypeCommon.ICON_LINKBTN)){
			return TEMPLATE_NAME_2;
		}else if(type.equals(LinkBtnTypeCommon.BUTTON_LINKBTN)){
			return TEMPLATE_NAME_3;
		}else{
			return TEMPLATE_NAME_1;
		}
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
	public void setText(String  name) {
		this.text = text;
	}

	/**
	 * 装载模版参数
	 */
    @Override
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (null != oid) {
        	if(oid!=null){
        		SysNavOperation operation = this.getAction(Integer.parseInt(oid));
        		if(operation!=null){
        			
        			//后缀判断
        			if(operation.getActionurl().toLowerCase().indexOf(".action")>0){
        				addParameter("action", findString(operation.getActionurl()));
        			}else{
        				addParameter("action", findString(operation.getActionurl()+".action"));
        			}
        			
        			addParameter("oid", findString(operation.getId()));
        			//如果未定义text属性，自动获取
        			if(text!=null&&!"".equals(text)){
        				addParameter("text", findString(text));
        			}else{
        				addParameter("text", findString(operation.getOname()));
        			}
        			//判断按钮是否具备权限
        			if(NavSecurityUtil.getInstance().isCheckSecurity(operation))
        				addParameter("disabled", findString(""));
        			else
        				addParameter("disabled", findString("disabled"));
        			
        		}else{
        			addParameter("disabled", findString("disabled"));
        			
        		}
        	}
        	
        }
        if (null != icon) {
        	addParameter("icon", findString(icon));
        }
        if (null != type) {
        	addParameter("type", findString(type));
        }
    }
    
    
    /**
     * 获得action连接
     * @param oid
     * @return
     */
    private SysNavOperation getAction(int oid){
    	//从cache中获取
    	SysNavOperation sno =  SysNavOperationCache.getInstance().getModel(String.valueOf(oid));
    	if(sno==null){
    		StringBuffer sql = new StringBuffer();
	    	sql.append("select * from sysnavoperation where id = ?");
	    	Connection conn = null;
	    	PreparedStatement stmt = null;
	    	ResultSet rs = null;
	    	try {
	    		conn = DBUtil.open();
	    		stmt = conn.prepareStatement(sql.toString());
	    		stmt.setInt(1, oid);
	    		rs = stmt.executeQuery();
	    		if(rs.next()){
	    			sno = new SysNavOperation();
	    			sno.setId(rs.getString("ID"));
	    			sno.setOname(rs.getString("ONAME"));
	    				if(sno.getOname()==null)sno.setOname("");
	    			sno.setPid(rs.getString("PID"));
	    				if(sno.getPid()==null)sno.setPid("");
	    			sno.setPtype(rs.getString("PTYPE"));
	    				if(sno.getPtype()==null)sno.setPtype("");
	    			sno.setActionurl(rs.getString("ACTIONURL"));
	    				if(sno.getActionurl()==null)sno.setActionurl("");
	    		}
	    		//装载cache
	    		SysNavOperationCache.getInstance().putModel(sno);
			} catch (Exception e) {
				logger.error(e,e);
			}finally {
				DBUtil.close(conn, stmt, rs);
	    	}
    	}
    	return sno;
    }
}
