package com.iwork.app.mobile.process.create.action;

import com.iwork.app.mobile.process.create.service.SyncMobileProcessLaunchCenterService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 移动端流程发起中心
 * @author chenM
 *
 */
public class SyncMobileProcessLaunchCenterAction  extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private SyncMobileProcessLaunchCenterService syncMobileProcessLaunchCenterService;
	private String sessionId;
	private String html;
	private String deviceType;
	
	public String index(){
		html = syncMobileProcessLaunchCenterService.getMobileProcessLaunchCenterHtml();
		deviceType = UserContextUtil.getInstance().getCurrentUserLoginDeviceType();
		return SUCCESS;
	}
	
	/**
	 * 流程发起中心
	 * @return
	 */
	public String process_mb_launch_index(){
		
		return SUCCESS;
	}
	
	/**
	 * 传输
	 * @return
	 */
	public String getMobileProcessLaunchCenterJson() {
		String json = "";
			json = syncMobileProcessLaunchCenterService.getMobileProcessLaunchCenterJson();
			if (null != json) {
				json = json.replace("null", "\"\"");
				ResponseUtil.writeTextUTF8(json);
			} else {
				ResponseUtil.writeTextUTF8("");
			}
			
		return null;
	}
	
	public SyncMobileProcessLaunchCenterService getSyncMobileProcessLaunchCenterService() {
		return syncMobileProcessLaunchCenterService;
	}
	public void setSyncMobileProcessLaunchCenterService(
			SyncMobileProcessLaunchCenterService syncMobileProcessLaunchCenterService) {
		this.syncMobileProcessLaunchCenterService = syncMobileProcessLaunchCenterService;
	}

	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	
}
