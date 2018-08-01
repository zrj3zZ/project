package com.iwork.app.navigation.directory.dao;

import java.util.List;
import com.iwork.app.navigation.directory.model.SysNavDirectory;

public interface ISysNavDirectoryDAO {
	 void  addBoData(SysNavDirectory data);
	 List getBoDatas(int pageSize, int startRow);
	 List getAll();//获得所有记录
		int getRows();//获得总行数
		int getRows(String fieldname,String value);//获得总行数
		List queryBoDatas(String fieldname,String value);//根据条件查询
		List getBoDatas(String fieldname,String value,int pageSize, int startRow);//根据条件查询
		SysNavDirectory getBoData(String id);//根据ID获得记录
		String getMaxID();//获得最大ID值
		void updateBoData(SysNavDirectory obj);//修改记录
		void deleteBoData(SysNavDirectory obj);//删除记录
		/**
		 * 函数说明：获得子系统下所有目录信息
		 * 参数说明： 
		 * 返回值：信息的集合
		 */ 
	List getDirectoryList(String systemid);
}
