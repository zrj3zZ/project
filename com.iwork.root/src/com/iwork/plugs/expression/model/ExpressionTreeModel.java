package com.iwork.plugs.expression.model;

public class ExpressionTreeModel {

	private int id;
	private int pId;
	private String name;
	private boolean open;
	private Object click;
	private String icon;
	private String methodName;
	private String eModelId;

	public String geteModelId() {
		return eModelId;
	}

	public void seteModelId(String eModelId) {
		this.eModelId = eModelId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Object getClick() {
		return click;
	}

	public void setClick(Object click) {
		this.click = click;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
