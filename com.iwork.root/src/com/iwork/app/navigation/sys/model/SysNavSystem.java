package com.iwork.app.navigation.sys.model;

import com.iwork.core.db.ObjectModel;

/**
 * SysNavSystemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysNavSystem implements java.io.Serializable,ObjectModel {
	public static String DATABASE_ENTITY = "SysNavSystem";
	// Fields

	private String id;
	private String sysName;
	private String sysIcon;
	private String sysUrl;
	private String sysTarget;
	private String sysIsHidden;
	private String sysLayout;
	private String sysService;
	private String orderindex;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getSysIcon() {
		return sysIcon;
	}
	public void setSysIcon(String sysIcon) {
		this.sysIcon = sysIcon;
	}
	public String getSysUrl() {
		return sysUrl;
	}
	public void setSysUrl(String sysUrl) {
		this.sysUrl = sysUrl;
	}
	public String getSysTarget() {
		return sysTarget;
	}
	public void setSysTarget(String sysTarget) {
		this.sysTarget = sysTarget;
	}
	public String getSysIsHidden() {
		return sysIsHidden;
	}
	public void setSysIsHidden(String sysIsHidden) {
		this.sysIsHidden = sysIsHidden;
	}
	public String getSysLayout() {
		return sysLayout;
	}
	public void setSysLayout(String sysLayout) {
		this.sysLayout = sysLayout;
	}
	public String getSysService() {
		return sysService;
	}
	public void setSysService(String sysService) {
		this.sysService = sysService;
	}
	public String getOrderindex() {
		return orderindex;
	}
	public void setOrderindex(String orderindex) {
		this.orderindex = orderindex;
	}

//	// Constructors
// 
	/** default constructor */
	public SysNavSystem() {
	}
//
//	/** full constructor */
//	public SysNavSystem(String id, String sysName, String sysIcon,
//			String sysUrl, String sysTarget, String sysIsHidden,
//			String sysLayout, String orderindex) {
//		this.id = id;
//		this.sysName = sysName;
//		this.sysIcon = sysIcon;
//		this.sysUrl = sysUrl;
//		this.sysTarget = sysTarget;
//		this.sysIsHidden = sysIsHidden;
//		this.sysLayout = sysLayout;
//		this.orderindex = orderindex;
//	}
//
//	// Property accessors
//
//	public int getId() {
//		return this.id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getSysName() {
//		return this.sysName;
//	}
//
//	public void setSysName(String sysName) {
//		this.sysName = sysName;
//	}
//
//	public String getSysIcon() {
//		return this.sysIcon;
//	}
//
//	public void setSysIcon(String sysIcon) {
//		this.sysIcon = sysIcon;
//	}
//
//	public String getSysUrl() {
//		return this.sysUrl;
//	}
//
//	public void setSysUrl(String sysUrl) {
//		this.sysUrl = sysUrl;
//	}
//
//	public String getSysTarget() {
//		return this.sysTarget;
//	}
//
//	public void setSysTarget(String sysTarget) {
//		this.sysTarget = sysTarget;
//	}
//
//	public int getSysIsHidden() {
//		return this.sysIsHidden;
//	}
//
//	public void setSysIsHidden(int sysIsHidden) {
//		this.sysIsHidden = sysIsHidden;
//	}
//
//	public String getSysLayout() {
//		return this.sysLayout;
//	}
//
//	public void setSysLayout(String sysLayout) {
//		this.sysLayout = sysLayout;
//	}
//
//	public int getOrderindex() {
//		return this.orderindex;
//	}
//
//	public void setOrderindex(int orderindex) {
//		this.orderindex = orderindex;
//	}
//
//	public boolean equals(Object other) {
//		if ((this == other))
//			return true;
//		if ((other == null))
//			return false;
//		if (!(other instanceof SysNavSystem))
//			return false;
//		SysNavSystem castOther = (SysNavSystem) other;
//
//		return ((this.getId() == castOther.getId()) || (this.getId() != null
//				&& castOther.getId() != null && this.getId().equals(
//				castOther.getId())))
//				&& ((this.getSysName() == castOther.getSysName()) || (this
//						.getSysName() != null
//						&& castOther.getSysName() != null && this.getSysName()
//						.equals(castOther.getSysName())))
//				&& ((this.getSysIcon() == castOther.getSysIcon()) || (this
//						.getSysIcon() != null
//						&& castOther.getSysIcon() != null && this.getSysIcon()
//						.equals(castOther.getSysIcon())))
//				&& ((this.getSysUrl() == castOther.getSysUrl()) || (this
//						.getSysUrl() != null
//						&& castOther.getSysUrl() != null && this.getSysUrl()
//						.equals(castOther.getSysUrl())))
//				&& ((this.getSysTarget() == castOther.getSysTarget()) || (this
//						.getSysTarget() != null
//						&& castOther.getSysTarget() != null && this
//						.getSysTarget().equals(castOther.getSysTarget())))
//				&& ((this.getSysIsHidden() == castOther.getSysIsHidden()) || (this
//						.getSysIsHidden() != null
//						&& castOther.getSysIsHidden() != null && this
//						.getSysIsHidden().equals(castOther.getSysIsHidden())))
//				&& ((this.getSysLayout() == castOther.getSysLayout()) || (this
//						.getSysLayout() != null
//						&& castOther.getSysLayout() != null && this
//						.getSysLayout().equals(castOther.getSysLayout())))
//				&& ((this.getOrderindex() == castOther.getOrderindex()) || (this
//						.getOrderindex() != null
//						&& castOther.getOrderindex() != null && this
//						.getOrderindex().equals(castOther.getOrderindex())));
//	}
//
//	public int hashCode() {
//		int result = 17;
//
//		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
//		result = 37 * result
//				+ (getSysName() == null ? 0 : this.getSysName().hashCode());
//		result = 37 * result
//				+ (getSysIcon() == null ? 0 : this.getSysIcon().hashCode());
//		result = 37 * result
//				+ (getSysUrl() == null ? 0 : this.getSysUrl().hashCode());
//		result = 37 * result
//				+ (getSysTarget() == null ? 0 : this.getSysTarget().hashCode());
//		result = 37
//				* result
//				+ (getSysIsHidden() == null ? 0 : this.getSysIsHidden()
//						.hashCode());
//		result = 37 * result
//				+ (getSysLayout() == null ? 0 : this.getSysLayout().hashCode());
//		result = 37
//				* result
//				+ (getOrderindex() == null ? 0 : this.getOrderindex()
//						.hashCode());
//		return result;
//	}

}
