package com.iwork.app.navigation.function.dao;

import java.util.List;

import com.iwork.app.navigation.function.model.Sysnavfunction;


public interface ISysNavFunctionDAO {
	 void  addBoData(Sysnavfunction data);
	 List getBoDatas(int pageSize, int startRow);
	 List getAll();//获得所有记录
		int getRows();//获得总行数
		int getRows(String fieldname,String value);//获得总行数
		List queryBoDatas(String fieldname,String value);//根据条件查询
		List getBoDatas(String fieldname,String value,int pageSize, int startRow);//根据条件查询
		Sysnavfunction getBoData(String id);//根据ID获得记录
		String getMaxID();//获得最大ID值
		void updateBoData(Sysnavfunction obj);//修改记录
		void deleteBoData(Sysnavfunction obj);//删除记录
		List getSubFunctionList(String directoryid);//获得目录下模块功能列表
}
