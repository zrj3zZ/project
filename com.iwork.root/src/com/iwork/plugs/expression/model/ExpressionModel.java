package com.iwork.plugs.expression.model;

import java.util.List;

public class ExpressionModel {

	private String groupName;
	private int groupId;
	private String id;
	private String implementsClass;
	private String label;
	private String desc;
	private List<ExpressionParam> params;
	private String returnType;
	private String returnDesc;
	private String eg;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImplementsClass() {
		return implementsClass;
	}

	public void setImplementsClass(String implementsClass) {
		this.implementsClass = implementsClass;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ExpressionParam> getParams() {
		return params;
	}

	public void setParams(List<ExpressionParam> params) {
		this.params = params;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

	public String getEg() {
		return eg;
	}

	public void setEg(String eg) {
		this.eg = eg;
	}

}
