package com.iwork.core.organization.dao;

import java.util.List;

import com.iwork.core.organization.model.OrgDepartment;

public interface IOrgDepartmentDAO {
	 void  addBoData(OrgDepartment data);
	 List getBoDatas(int pageSize, int startRow);
	 List getAll();//获得所有记录
		int getRows();//获得总行数
		int getRows(int companyid,int parentdeptid);//获得总行数
		List queryBoDatas(String fieldname,String value);//根据条件查询
		List getBoDatas(int companyid,int parentdeptid,int pageSize, int startRow);//根据条件查询
		OrgDepartment getBoData(String id);//根据ID获得记录
		String getMaxID();//获得最大ID值
		void updateBoData(OrgDepartment obj);//修改记录
		void deleteBoData(OrgDepartment obj);//删除记录
		List getSubDepartmentList(String parentid);  //获得子部门列表
		 List getTopDepartmentList(String companyid);
}
