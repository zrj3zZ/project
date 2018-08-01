package com.iwork.core.organization.dao;

import java.util.List;

import com.iwork.core.organization.model.OrgCompany;

public interface IOrgCompanyDAO {
	 void  addBoData(OrgCompany data);
	 List getBoDatas(int pageSize, int startRow);
	 List getAll();//获得所有记录
		int getRows();//获得总行数
		int getRows(String fieldname,String value);//获得总行数
		List queryBoDatas(String fieldname,String value);//根据条件查询
		List getBoDatas(String fieldname,String value,int pageSize, int startRow);//根据条件查询
		OrgCompany getBoData(String id);//根据ID获得记录
		String getMaxID();//获得最大ID值
		void updateBoData(OrgCompany obj);//修改记录
		void deleteBoData(OrgCompany obj);//删除记录
}
