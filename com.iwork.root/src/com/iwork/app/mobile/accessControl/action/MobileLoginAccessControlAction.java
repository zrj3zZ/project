package com.iwork.app.mobile.accessControl.action;

import com.iwork.app.mobile.accessControl.model.SysMobileVisitset;
import com.iwork.app.mobile.accessControl.service.MobileLoginAccessControlService;
import com.iwork.app.mobile.accessControl.util.AccessControlUtil;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MobileLoginAccessControlAction extends ActionSupport {

	private MobileLoginAccessControlService mobileLoginAccessControlService;
	SysMobileVisitset mobileVisitset;
	String userId;
	int isVisit;
	String visitType;
	int isBind;
	String deviceIds;
	String userIds;
	String keyWord;
	String userLoginTime;
	String userLoginCount;

	/**
	 * 分页类
	 */
	String conditionStr;
	Long rowNum;
	Page page;
	String colNames;
	String colModel;
	String paperno;

	public String index() {

		return SUCCESS;
	}
 
	public String mobileAccessControlGetTreeJson() {

		if(keyWord == null){
			keyWord = "";
		}
		
		String result = mobileLoginAccessControlService
				.getIndexTreeJsonStr(keyWord);

		ResponseUtil.writeTextUTF8(result);

		return null;
	}

	public String mobileAccessSet() {

		if (userId != null) {
			mobileVisitset = mobileLoginAccessControlService
					.getMobileAccessSetByUserId(userId);
			userLoginTime = AccessControlUtil.getInstance().getUserLastLoginTime(userId);
			userLoginCount = AccessControlUtil.getInstance().countUserLoginNum(userId);
		}

		
		return SUCCESS;
	}

	public String submitUserAccess() {

		SysMobileVisitset userMobileVisitset = mobileLoginAccessControlService
				.getMobileAccessSetByUserId(userId);
		userMobileVisitset.setDeviceIds(deviceIds);
		userMobileVisitset.setIsBind(isBind);
		userMobileVisitset.setIsVisit(isVisit);
		userMobileVisitset.setVisitType(visitType);
		userMobileVisitset.setUserid(userId);

		if (userMobileVisitset.getId() > 0) {
			mobileLoginAccessControlService
					.updateMobileVisitset(userMobileVisitset);
		} else {
			mobileLoginAccessControlService
					.saveMobileVisitset(userMobileVisitset);
		}

		ResponseUtil.writeTextUTF8("更新成功");
		return null;
	}

	public String moreUserAccessSet() {

		return SUCCESS;
	}

	public String moreUserAccessSubmit() {

		if (userIds.trim().length() > 0) {
			mobileLoginAccessControlService.updateMoreUserAccess(userIds,
					isBind, isVisit, visitType);
			ResponseUtil.writeTextUTF8("更新成功");
		} else {
			ResponseUtil.writeTextUTF8("请选择要更新的人员");
		}

		return null;
	}

	public String userMobileLonginIndex() {
		colModel = mobileLoginAccessControlService.getColModel();
		colNames = mobileLoginAccessControlService.getColName();
		rowNum = new Long(20);
		return SUCCESS;
	}

	public String userMobileLonginData() {

		if (userId.trim().length() > 0) {
			String json = mobileLoginAccessControlService.logDoSearch(page,
					userId, paperno);
			if (json != null && json.length() > 0) {
				ResponseUtil.write(json.substring(1, json.length() - 1));
			}
		}

		return null;
	}

	public String userMobileBindIndex() {

		colModel = mobileLoginAccessControlService.getBindColModel();
		colNames = mobileLoginAccessControlService.getBindColName();
		rowNum = new Long(10);

		return SUCCESS;
	}

	public String userMobileBindData() {

		if (userId.trim().length() > 0) {
			String json = mobileLoginAccessControlService.bindDoSearch(page,
					userId, paperno);
			if (json != null && json.length() > 0) {
				ResponseUtil.write(json.substring(1, json.length() - 1));
			}
		}
		return null;
	}

	public String getUserLoginTime() {
		return userLoginTime;
	}

	public void setUserLoginTime(String userLoginTime) {
		this.userLoginTime = userLoginTime;
	}

	public String getUserLoginCount() {
		return userLoginCount;
	}

	public void setUserLoginCount(String userLoginCount) {
		this.userLoginCount = userLoginCount;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getConditionStr() {
		return conditionStr;
	}

	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}

	public Long getRowNum() {
		return rowNum;
	}

	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}

	public String getPaperno() {
		return paperno;
	}

	public void setPaperno(String paperno) {
		this.paperno = paperno;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public int getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(int isVisit) {
		this.isVisit = isVisit;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public int getIsBind() {
		return isBind;
	}

	public void setIsBind(int isBind) {
		this.isBind = isBind;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public SysMobileVisitset getMobileVisitset() {
		return mobileVisitset;
	}

	public void setMobileVisitset(SysMobileVisitset mobileVisitset) {
		this.mobileVisitset = mobileVisitset;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MobileLoginAccessControlService getMobileLoginAccessControlService() {
		return mobileLoginAccessControlService;
	}

	public void setMobileLoginAccessControlService(
			MobileLoginAccessControlService mobileLoginAccessControlService) {
		this.mobileLoginAccessControlService = mobileLoginAccessControlService;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getColNames() {
		return colNames;
	}

	public void setColNames(String colNames) {
		this.colNames = colNames;
	}

	public String getColModel() {
		return colModel;
	}

	public void setColModel(String colModel) {
		this.colModel = colModel;
	}

}
