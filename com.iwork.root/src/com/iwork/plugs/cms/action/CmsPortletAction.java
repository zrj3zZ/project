package com.iwork.plugs.cms.action;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.service.CmsPortletService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
/**
 * CMS栏目管理跳转类
 * @author WeiGuangjian
 *
 */
public class CmsPortletAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(CmsPortletAction.class);
	private CmsPortletService cmsPortletService;
    private String portletid;
    private String portletkey;
    private String portletname;
    private String groupname;
    private String manager;
    private String template;
    private long verifytype;
    private String browse;
    private long portlettype;
    private String param;
    private String begindate;
    private String enddate;
    private long status;
    private String memo;
    private String type;
    private String key;
    private boolean flag;
    private String user;
    private String pstrScript;
    private String gname;
    private long prows;
    private long pwidth;
    private long pheight;
    private long isborder;
    private long istitle;
    private String portletid2;
    private String type2;
    private String gname2;
    private String morelink;
    private String linktarget;
    private Long orderindex;
    private IworkCmsPortlet model;
    
	/**
	 * 主页
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception {
		this.setFlag(SecurityUtil.isSuperManager());
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		this.setUser(uc._userModel.getUserid());
		try {
			gname = URLDecoder.decode(gname, "UTF-8");
		} catch (Exception e) {
			logger.error(e,e);
		}
		this.setGname(gname);
		return "index";
	}
	
	public String cmsPortletAdd() throws Exception {
		if(portletid!=null && !"".equals(portletid)){
			model = cmsPortletService.getCmsPortletDAO().getPortleByIdtModel(portletid);
			type="edit";
		}else{
			type="add";
		}
		 
		return SUCCESS;
	}

	public String add() {

		return "add";
	}
	
	/**
	 * 获取导航树
	 * @return
	 * @throws Exception
	 */
	public String openjson() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = cmsPortletService.getTreeJson();
		request.setAttribute("CmsPortlet", json);
		return "openjson";
	}
	
	/**
	 * 获取栏目表
	 * @return
	 * @throws Exception
	 */
	public String portletgrid() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			gname = URLDecoder.decode(gname, "UTF-8");
		} catch (Exception e) {logger.error(e,e);
		}
		json = cmsPortletService.getGridJson(gname);
		request.setAttribute("CmsPortlet", json);
		return "openjson";
	}
	
	/**
	 * 获取分组下拉列表
	 * @return
	 * @throws Exception
	 */
	public String combobox() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = cmsPortletService.getComboboxJson();
		request.setAttribute("CmsPortlet", json);
		return "openjson";
	}
	
	/**
	 * 栏目增改
	 * @return
	 * @throws Exception
	 */
	public void cmsPortletEdit()throws Exception{
		if(orderindex==null){
			this.orderindex=0L;			
		}
		try {
			cmsPortletService.cmsPortletEdit(portletid,portletkey,portletname,groupname,manager,template,morelink,linktarget,verifytype,browse,portlettype,param,begindate,enddate,status,memo,prows,pwidth,pheight,isborder,istitle,type,orderindex); 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("gb2312");
			response.setContentType("text/html;charset=gb2312");
			response.getWriter().print("success");
		} catch (Exception e) {
			logger.error(e,e);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("gb2312");
			response.setContentType("text/html;charset=gb2312");
			response.getWriter().print("failure");
		}

	}
	
	/**
	 * 栏目删除
	 * @return
	 * @throws Exception
	 */
	public String cmsPortletRemove()throws Exception{
		String pstrScript = "<script>$(function(){";	
		String msg=cmsPortletService.cmsPortletRemove(portletid); 
		if(msg.equals("failure")){			     
			pstrScript +="$.messager.alert('系统提示', '此栏目下有资讯内容，无法删除！', 'error');";//弹出提示
		}	
		pstrScript +="});</script>";	
		this.setPstrScript(pstrScript);
		this.setFlag(SecurityUtil.isSuperManager());
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		this.setUser(uc._userModel.getUserid());
		this.setGname(gname);
		return "index";	
	}
	

	/**
	 * 键值校验
	 */
	public void cmsKeyVal(){	
		try {
			String sb = cmsPortletService.getKeyVal(key,type,portletid);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("gb2312");
			response.setContentType("text/html;charset=gb2312");
			response.getWriter().print(sb.toString());
			
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	
	public CmsPortletService getCmsPortletService() {
		return cmsPortletService;
	}

	public void setCmsPortletService(CmsPortletService cmsPortletService) {
		this.cmsPortletService = cmsPortletService;
	}

	public String getPortletid() {
		return portletid;
	}

	public void setPortletid(String portletid) {
		this.portletid = portletid;
	}

	public String getPortletkey() {
		return portletkey;
	}

	public void setPortletkey(String portletkey) {
		this.portletkey = portletkey;
	}

	public String getPortletname() {
		return portletname;
	}

	public void setPortletname(String portletname) {
		this.portletname = portletname;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public long getVerifytype() {
		return verifytype;
	}

	public void setVerifytype(long verifytype) {
		this.verifytype = verifytype;
	}

	public String getBrowse() {
		return browse;
	}

	public void setBrowse(String browse) {
		this.browse = browse;
	}

	public long getPortlettype() {
		return portlettype;
	}

	public void setPortlettype(long portlettype) {
		this.portlettype = portlettype;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPstrScript() {
		return pstrScript;
	}

	public void setPstrScript(String pstrScript) {
		this.pstrScript = pstrScript;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public long getProws() {
		return prows;
	}

	public void setProws(long prows) {
		this.prows = prows;
	}

	public long getPwidth() {
		return pwidth;
	}

	public void setPwidth(long pwidth) {
		this.pwidth = pwidth;
	}

	public long getPheight() {
		return pheight;
	}

	public void setPheight(long pheight) {
		this.pheight = pheight;
	}

	public long getIsborder() {
		return isborder;
	}

	public void setIsborder(long isborder) {
		this.isborder = isborder;
	}

	public String getPortletid2() {
		return portletid2;
	}

	public void setPortletid2(String portletid2) {
		this.portletid2 = portletid2;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getGname2() {
		return gname2;
	}

	public void setGname2(String gname2) {
		this.gname2 = gname2;
	}

	public long getIstitle() {
		return istitle;
	}

	public void setIstitle(long istitle) {
		this.istitle = istitle;
	}

	public String getMorelink() {
		return morelink;
	}

	public void setMorelink(String morelink) {
		this.morelink = morelink;
	}

	public String getLinktarget() {
		return linktarget;
	}

	public void setLinktarget(String linktarget) {
		this.linktarget = linktarget;
	}

	public Long getOrderindex() {
		return orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}

	public IworkCmsPortlet getModel() {
		return model;
	}

	public void setModel(IworkCmsPortlet model) {
		this.model = model;
	}



	
	
}
