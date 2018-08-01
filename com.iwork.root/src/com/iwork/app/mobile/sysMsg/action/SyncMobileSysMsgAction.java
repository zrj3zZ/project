package com.iwork.app.mobile.sysMsg.action;

import com.iwork.app.message.sysmsg.model.SysMessage;
import com.iwork.app.mobile.sysMsg.service.SyncMobileSysMsgService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 移动端系统消息同步
 */
public class SyncMobileSysMsgAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private SyncMobileSysMsgService syncMobileSysMsgService;
	private String curPageNo;
	private String pageSize;
	private String cmd;
	private SysMessage model;
	private Long id;

	/**
	 * 移动端获得系统消息
	 */
	public String getMobileSysMsgJson() {
		String json = "";
			json = syncMobileSysMsgService.getMobileSysMsgJson(curPageNo, pageSize, id, cmd);
			if (null != json) {
				json = json.replaceAll("</br>", "\n");
				json = json.replaceAll("<br/>", "\n");
				json = json.replaceAll("/br>", "\n");
				json = json.replaceAll("<", "");
				ResponseUtil.writeTextUTF8(json);
			} else {
				ResponseUtil.writeTextUTF8("");
			}
		return null;
	}
	
	/**
	 * 获得消息详细页面
	 * @return
	 */
	public String getMobileSyMsgItem(){
		if(id!=null){ 
			model = syncMobileSysMsgService.getSysMessageDAO().getSysMsgById(id);
			if(model!=null){
				syncMobileSysMsgService.setMsgStatus(id,UserContextUtil.getInstance().getCurrentUserId());
			}
		}
		return SUCCESS;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public SyncMobileSysMsgService getSyncMobileSysMsgService() {
		return syncMobileSysMsgService;
	}

	public void setSyncMobileSysMsgService(
			SyncMobileSysMsgService syncMobileSysMsgService) {
		this.syncMobileSysMsgService = syncMobileSysMsgService;
	}

	public String getCurPageNo() {
		return curPageNo;
	}

	public void setCurPageNo(String curPageNo) {
		this.curPageNo = curPageNo;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysMessage getModel() {
		return model;
	}

}
