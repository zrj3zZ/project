package com.iwork.core.organization.constant;

import java.util.HashMap;
import java.util.Map;

public class GroupTypeConst {
	private static final Map m = new HashMap();
	private static final GroupTypeConst instance = new GroupTypeConst();
	public static final String GROUP_PERSON = "per";
	public static final String GROUP_DEPARTMENT = "dept";
	public static final String GROUP_GROUP = "group";
	
	public static final GroupTypeConst getInstance() {
		return instance;
	}
	
	public GroupTypeConst() {
		m.put("per", "人员");
		m.put("group", "团队");
		m.put("dept", "部门");
	}
	
	public static String getTypeName(String type) {
		return m.get(type).toString();
	}
}
