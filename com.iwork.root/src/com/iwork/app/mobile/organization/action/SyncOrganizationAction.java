package com.iwork.app.mobile.organization.action;

import com.iwork.app.mobile.organization.service.SyncOrganizationService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 将组织结构同步到移动端
 * @author chenM
 *
 */
public class SyncOrganizationAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private SyncOrganizationService syncOrganizationService;
	private String id;
	private String idType;
	
	/**
	 * 同步到移动端的JSON
	 * @return
	 */
	public String getMobileOrganization() throws Exception{
		if(id==null){id="";}
		if(idType==null){idType="";}
		String JsonData =syncOrganizationService.getNode(id,idType);
		if(null!=JsonData){
			JsonData = JsonData.replace("null", "\"\"");
			ResponseUtil.writeTextUTF8(JsonData);
		}else{
			ResponseUtil.writeTextUTF8("");
		}
		return null;
	}
	
	/**
	 * 获取用户头像
	 * @return
	 * @throws Exception
	 */
	public String getMobileUserImg() throws Exception{
		if(null!=id){
			String JsonData =syncOrganizationService.getUserImg(id);
			if(null!=JsonData){
				JsonData = JsonData.replace("\\\\", "/");
				ResponseUtil.writeTextUTF8(JsonData);
			}else{
				ResponseUtil.writeTextUTF8("");
			}
		}else{
			ResponseUtil.writeTextUTF8("");
		}
		return null;
	}
	
	public SyncOrganizationService getSyncOrganizationService() {
		return syncOrganizationService;
	}

	public void setSyncOrganizationService(
			SyncOrganizationService syncOrganizationService) {
		this.syncOrganizationService = syncOrganizationService;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}
	
}
