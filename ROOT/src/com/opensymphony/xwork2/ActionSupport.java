package com.opensymphony.xwork2;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iwork.commons.util.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import com.opensymphony.xwork2.util.ValueStack;

public class ActionSupport
  implements Action, Validateable, ValidationAware, TextProvider, LocaleProvider, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static HashMap<String,List<String>> pageAction;
	private static HashMap<Long,List<String>> menuMap;
	private static List<String> menuList;
	
	/*
	 * 程序启动时初始化代码(执行一次)
	 */
	static{
		setSubMenuAction();
		setRoleMenuList();
	}
	/*
	 * 重写默认构造函数,用于过滤用户请求
	 */
	public ActionSupport(){
		filteAction();
	}
	/**
	 * 过滤当前请求
	 */
	private void filteAction() {
		if(menuMap==null||menuList==null||pageAction==null){
			setSubMenuAction();
			setRoleMenuList();
    	}
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);
		String uri = null;
   		String uri0 = request.getRequestURI();
   		if(uri0.contains(".")){
   			uri = uri0.substring(0, uri0.indexOf(".")).replace("/", "");
   		}
   		if(menuList.contains(uri)){
   			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
   			if(uri!=null&&!uri.equals("")&&menuMap!=null&&uc!=null){
   				Long orgRoleId = uc._userModel.getOrgroleid();
   				List<String> roleMenu = (List<String>) menuMap.get(orgRoleId);
   				List<OrgUserMap> usermaplist = uc._userMapList;
   				for (OrgUserMap orgUserMap : usermaplist) {
   					roleMenu.addAll(menuMap.get(Long.parseLong(orgUserMap.getOrgroleid())));
				}
   				if(!roleMenu.contains(uri)&&orgRoleId!=1){
   					try {
						request.getRequestDispatcher("/login.jsp").forward((ServletRequest)request, (ServletResponse)response);
						Thread th=Thread.currentThread();
						th.destroy();
						return;
					} catch (Exception e) {}
   				}
   			}
   		}
	}
	/**
	 *获取所有角色对应的菜单请求集合 
	 */
	private static void setRoleMenuList() {
		if(menuMap==null){
    		menuMap = new HashMap<Long,List<String>>();
    	}else{
    		return;
    	}
		if(menuList==null){
			menuList = new ArrayList<String>();
    	}else{
    		return;
    	}
		String sqlRole = "SELECT ID FROM ORGROLE";
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sqlRole);
			rs = ps.executeQuery();
			while(rs.next()){
				List lables = new ArrayList();
				lables.add("MENUACTION");
				
				Map params = new HashMap();
				params.put(1, rs.getLong("ID"));
				
				String sql = "SELECT N.NODE_NAME,REPLACE (REPLACE (SUBSTR(N.NODE_URL,0,INSTR(N.NODE_URL,'.',1,1)-1),'/',''),' ','') MENUACTION FROM SYSROLEPURVIEW S LEFT JOIN SYSPURVIEWSCHEMA A ON S.PURVIEWID=A.PURVIEWID LEFT JOIN SYS_NAV_NODE N ON A.NAVID=N.ID WHERE S.PURVIEWID IS NOT NULL AND A.NAVTYPE='NODE' AND N.NODE_URL IS NOT NULL AND S.ROLEID=?";
				List<String> l = DBUtil.getResultList(lables, sql, params);
				List<String> list = new ArrayList<String>();
				for (String menuAction : l) {
					if(pageAction.get(menuAction)!=null&&pageAction.get(menuAction).size()!=0)
						list.addAll(pageAction.get(menuAction));
				}
				l.addAll(list);
				menuMap.put(rs.getLong("ID"), l);
				menuList.addAll(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
				try {
					if(rs!=null)
						rs.close();
					if(ps!=null)
						ps.close();
					if(conn!=null)
						conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	/**
	 * 人工统计,菜单页面里的详细action请求
	 */
	private static void setSubMenuAction(){
		if(pageAction==null){
			pageAction = new HashMap<String,List<String>>();
		}
		List<String> l1 = new ArrayList<String>();
		l1.add("company_add");
		l1.add("department_add");
		l1.add("user_add");
		pageAction.put("user_index", l1);

		List<String> l2 = new ArrayList<String>();
		l2.add("department_add");
		pageAction.put("department_index", l2);

		List<String> l3 = new ArrayList<String>();
		l3.add("xxzcMbupload");
		l3.add("xxzcDr");
		l3.add("downxpzcwt");
		l3.add("upxpzcwt");
		l3.add("downbottomxpzcwt");
		l3.add("uptopxpzcwt");
		pageAction.put("sysDem_Mdm_xpzcwt", l3);

		List<String> l4 = new ArrayList<String>();
		l4.add("zqb_XPItemMoveUp");
		l4.add("zqb_XPItemMoveDown");
		l4.add("zqb_XPItemMoveBottom");
		l4.add("zqb_XPItemMoveTop");
		pageAction.put("zqb_XPItemIndex", l4);

		List<String> l5 = new ArrayList<String>();
		l5.add("zqb_XPItemMoveUp");
		l5.add("zqb_XPItemMoveDown");
		l5.add("zqb_XPItemMoveBottom");
		l5.add("zqb_XPItemMoveTop");
		pageAction.put("zqb_XPItemContentIndex", l5);

		List<String> l6 = new ArrayList<String>();
		l6.add("zqb_XPItemMoveUp");
		l6.add("zqb_XPItemMoveDown");
		l6.add("zqb_XPItemMoveBottom");
		l6.add("zqb_XPItemMoveTop");
		pageAction.put("zqb_XPItemBcIndex", l6);

		List<String> l7 = new ArrayList<String>();
		l7.add("zqb_XPItemMoveTop");
		l7.add("zqb_XPItemMoveBottom");
		l7.add("zqb_XPItemMoveDown");
		l7.add("zqb_XPItemMoveUp");
		pageAction.put("zqb_XPItemStepIndex", l7);

		List<String> l8 = new ArrayList<String>();
		l8.add("uptopxmjdzl");
		l8.add("upxmjdzl");
		l8.add("downxmjdzl");
		l8.add("downbottomxmjdzl");
		pageAction.put("sysDem_Mdm_xmjdzl", l8);

		List<String> l9 = new ArrayList<String>();
		l9.add("deletexmlx");
		pageAction.put("sysDem_Mdm_xmlx", l9);

		List<String> l10 = new ArrayList<String>();
		l10.add("uptoptyxmjdzl");
		l10.add("uptyxmjdzl");
		l10.add("downtyxmjdzl");
		l10.add("downbottomtyxmjdzl");
		pageAction.put("sysDem_Mdm_tyxmjdzl", l10);

		List<String> l11 = new ArrayList<String>();
		l11.add("zqb_industrymsg_remove");
		l11.add("zqb_imp_industrymsg_index");
		pageAction.put("zqb_industrymsg_manegement", l11);

		List<String> l12 = new ArrayList<String>();
		l12.add("deletekflb");
		l12.add("xinzengkflb");
		pageAction.put("cxkflb", l12);
		
		List<String> l13 = new ArrayList<String>();
		l13.add("zqb_change_password");
		l13.add("zqb_password_check");
		pageAction.put("zqb_XPPasswordReset", l13);
		
		List<String> l14 = new ArrayList<String>();
		l14.add("sysDem_DataList");
		pageAction.put("zqb_fzgs_indextree", l14);
	}
	
  private final ValidationAwareSupport validationAware = new ValidationAwareSupport();
  private transient TextProvider textProvider;
  private transient LocaleProvider localeProvider;
  protected Container container;

  public void setActionErrors(Collection<String> errorMessages)
  {
    this.validationAware.setActionErrors(errorMessages);
  }

  public Collection<String> getActionErrors() {
    return this.validationAware.getActionErrors();
  }

  public void setActionMessages(Collection<String> messages) {
    this.validationAware.setActionMessages(messages);
  }

  public Collection<String> getActionMessages() {
    return this.validationAware.getActionMessages();
  }

  public void setFieldErrors(Map<String, List<String>> errorMap) {
    this.validationAware.setFieldErrors(errorMap);
  }

  public Map<String, List<String>> getFieldErrors() {
    return this.validationAware.getFieldErrors();
  }

  public Locale getLocale()
  {
    return getLocaleProvider().getLocale();
  }

  public boolean isValidLocaleString(String localeStr)
  {
    return getLocaleProvider().isValidLocaleString(localeStr);
  }

  public boolean isValidLocale(Locale locale)
  {
    return getLocaleProvider().isValidLocale(locale);
  }

  public boolean hasKey(String key) {
    return getTextProvider().hasKey(key);
  }

  public String getText(String aTextName) {
    return getTextProvider().getText(aTextName);
  }

  public String getText(String aTextName, String defaultValue) {
    return getTextProvider().getText(aTextName, defaultValue);
  }

  public String getText(String aTextName, String defaultValue, String obj) {
    return getTextProvider().getText(aTextName, defaultValue, obj);
  }

  public String getText(String aTextName, List<?> args) {
    return getTextProvider().getText(aTextName, args);
  }

  public String getText(String key, String[] args) {
    return getTextProvider().getText(key, args);
  }

  public String getText(String aTextName, String defaultValue, List<?> args) {
    return getTextProvider().getText(aTextName, defaultValue, args);
  }

  public String getText(String key, String defaultValue, String[] args) {
    return getTextProvider().getText(key, defaultValue, args);
  }

  public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
    return getTextProvider().getText(key, defaultValue, args, stack);
  }

  public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
    return getTextProvider().getText(key, defaultValue, args, stack);
  }

  public String getFormatted(String key, String expr)
  {
    Map conversionErrors = ActionContext.getContext().getConversionErrors();
    if (conversionErrors.containsKey(expr)) {
      String[] vals = (String[])conversionErrors.get(expr);
      return vals[0];
    }
    ValueStack valueStack = ActionContext.getContext().getValueStack();
    Object val = valueStack.findValue(expr);
    return getText(key, Arrays.asList(new Object[] { val }));
  }

  public ResourceBundle getTexts()
  {
    return getTextProvider().getTexts();
  }

  public ResourceBundle getTexts(String aBundleName) {
    return getTextProvider().getTexts(aBundleName);
  }

  public void addActionError(String anErrorMessage) {
    this.validationAware.addActionError(anErrorMessage);
  }

  public void addActionMessage(String aMessage) {
    this.validationAware.addActionMessage(aMessage);
  }

  public void addFieldError(String fieldName, String errorMessage) {
    this.validationAware.addFieldError(fieldName, errorMessage);
  }

  public String input() throws Exception {
    return "input";
  }

  public String execute()
    throws Exception
  {
    return "success";
  }

  public boolean hasActionErrors() {
    return this.validationAware.hasActionErrors();
  }

  public boolean hasActionMessages() {
    return this.validationAware.hasActionMessages();
  }

  public boolean hasErrors() {
    return this.validationAware.hasErrors();
  }

  public boolean hasFieldErrors() {
    return this.validationAware.hasFieldErrors();
  }

  public void clearFieldErrors()
  {
    this.validationAware.clearFieldErrors();
  }

  public void clearActionErrors()
  {
    this.validationAware.clearActionErrors();
  }

  public void clearMessages()
  {
    this.validationAware.clearMessages();
  }

  public void clearErrors()
  {
    this.validationAware.clearErrors();
  }

  public void clearErrorsAndMessages()
  {
    this.validationAware.clearErrorsAndMessages();
  }

  public void validate()
  {
  }

  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }

  public void pause(String result)
  {
  }

  protected TextProvider getTextProvider()
  {
    if (this.textProvider == null) {
      TextProviderFactory tpf = (TextProviderFactory)this.container.getInstance(TextProviderFactory.class);
      this.textProvider = tpf.createInstance(getClass());
    }
    return this.textProvider;
  }

  protected LocaleProvider getLocaleProvider() {
    if (this.localeProvider == null) {
      LocaleProviderFactory localeProviderFactory = (LocaleProviderFactory)this.container.getInstance(LocaleProviderFactory.class);
      this.localeProvider = localeProviderFactory.createLocaleProvider();
    }
    return this.localeProvider;
  }

  @Inject
  public void setContainer(Container container) {
    this.container = container;
  }
}