package com.iwork.plugs.cms.action;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.plugs.cms.service.CmsChannelService;
import com.iwork.core.security.SecurityUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * CMS频道管理跳转类
 * @author WeiGuangjian
 *
 */
public class CmsChannelAction extends ActionSupport {

	private CmsChannelService cmsChannelService;
    private String channelid;
    private String channelname;
    private String groupname;
    private String manager;
    private String template;
    private long verifytype;
    private String browse;
    private String begindate;
    private String enddate;
    private long status;
    private String memo;
    private String type;
    private boolean flag;
    private String pstrScript;
    private String gname;
    
	/**
	 * 主页
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception {
		this.setFlag(SecurityUtil.isSuperManager());
		try {
			gname = URLDecoder.decode(gname, "UTF-8");
		} catch (Exception e) {
		}
		this.setGname(gname);
		return "index";
	}
	
	/**
	 * 获取频道树
	 * @return
	 * @throws Exception
	 */
	public String openjson() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = cmsChannelService.getTreeJson();
		request.setAttribute("CmsChannel", json);
		return "openjson";
	}
	
	/**
	 * 获取频道表
	 * @return
	 * @throws Exception
	 */
	public String channelgrid() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			gname = URLDecoder.decode(gname, "UTF-8");
		} catch (Exception e) {
		}
		json = cmsChannelService.getGridJson(gname);
		request.setAttribute("CmsChannel", json);
		return "openjson";
	}
	
	/**
	 * 频道增删改
	 * @return
	 * @throws Exception
	 */
	public String cmsChannelEdit()throws Exception{	
		String pstrScript = "<script>$(function(){";	
		String msg=cmsChannelService.cmsChannelEdit(channelid,channelname,groupname,manager,template,verifytype,browse,begindate,enddate,status,memo,type); 
		if(msg.equals("failure")){			     
			pstrScript +="$.messager.alert('系统提示', '此频道下有栏目内容，无法删除！', 'error');";//弹出提示
		}	
		pstrScript +="});</script>";	
		this.setPstrScript(pstrScript);
		this.setFlag(SecurityUtil.isSuperManager());
		this.setGname(gname);
		return "index";	
	}
	
	public CmsChannelService getCmsChannelService() {
		return cmsChannelService;
	}

	public void setCmsChannelService(CmsChannelService cmsChannelService) {
		this.cmsChannelService = cmsChannelService;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
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
	
	
	
}
