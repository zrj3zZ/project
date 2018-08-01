package com.iwork.core.server.servicemanager.dao;

import java.util.List;  
import com.iwork.core.server.servicemanager.model.Sysservice;


public interface ISysServiceManagerDAO {
	 void  addBoData(Sysservice data);
	 List getBoDatas(int pageSize, int startRow);
	 List getAll();//获得所有记录
		int getRows();//获得总行数
		Sysservice getBoData(Long id);//根据ID获得记录
		Long getMaxID();//获得最大ID值
		void updateBoData(Sysservice obj);//修改记录
		void deleteBoData(Sysservice obj);//删除记录
}
