package com.iwork.app.mobile.systoplist;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.navigation.web.service.SysNavFrameService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class GetMobileSysTopList extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private SysNavFrameService sysNavFrameService;
	/**
	 * 获得登陆后的用户名、待办、系统消息个数
	 * @return
	 * @throws Exception
	 */
	public String getMobileSysTopList() throws Exception {
		int unReadCount = 0;
		int taskcount = 0;
		String userid = "";
		String userName = "";
		String detpName = "";
		StringBuffer html = new StringBuffer();
		Map<String, Object> userItem = new HashMap<String, Object>();
			UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext(); 
			if(null!=userContext){
				unReadCount = sysNavFrameService.getUnReadCount(userContext._userModel.getUserid());
				taskcount = sysNavFrameService.getWorkflowCount(userContext.get_userModel().getUserid());
				userName = userContext.get_userModel().getUsername();
				detpName = userContext.get_deptModel().getDepartmentname();
				userid =  userContext.get_userModel().getUserid();
				userItem.put("unReadCount", unReadCount);
				userItem.put("taskcount", taskcount);
				userItem.put("userid", userid);  
				userItem.put("userName", userName); 
				userItem.put("deptName", detpName); 
				JSONArray json = JSONArray.fromObject(userItem);
				html.append(json);
				ResponseUtil.writeTextUTF8(html.toString());
			}else{
				ResponseUtil.writeTextUTF8("");
			}
		
		return null;	 
	}

	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public SysNavFrameService getSysNavFrameService() {
		return sysNavFrameService;
	}

	public void setSysNavFrameService(SysNavFrameService sysNavFrameService) {
		this.sysNavFrameService = sysNavFrameService;
	}
}
