package com.iwork.core.organization.interceptor;

import com.iwork.core.organization.model.OrgDepartment;

public interface OrgExtendDepartmentInterface {
	public void addDepartment(OrgDepartment model);
	public void updateDepartment(OrgDepartment model);
	public void removeDepartment(OrgDepartment model);
}
