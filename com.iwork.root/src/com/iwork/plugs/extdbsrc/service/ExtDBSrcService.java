package com.iwork.plugs.extdbsrc.service;

import java.util.ArrayList;
import java.util.List;

import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.db.JDBCDriver;
import com.iwork.plugs.extdbsrc.dao.ExtDBSrcDao;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;

public class ExtDBSrcService {
	private ExtDBSrcDao extDBSrcDao;
	/**
	 * 获得外部数据源列表
	 * @return
	 */
	public List<SysExdbsrcCenter> getList(){
		List<SysExdbsrcCenter> list = new ArrayList<SysExdbsrcCenter>();
		SysExdbsrcCenter local=this.getLocalDbsrc();
		list.add(local);
		List extList = extDBSrcDao.getList();
		if(extList!=null){ 
			list.addAll(extList); 
		}	
		return list;
	}
	/**
	 * 获得本地数据源
	 * @return
	 */
	public SysExdbsrcCenter getLocalDbsrc(){
		JDBCDriver jdbc = JDBCDriver.getInstance();
		String local_type=jdbc.getDBType();
		String local_driverName=jdbc.getDriverName();
		String local_dsrcUrl=jdbc.getURL();
		String local_username=jdbc.getUserName();
		String local_password=jdbc.getPWD();
		String uuid="";
		SysExdbsrcCenter local = new SysExdbsrcCenter("本地数据源",local_type,local_driverName,local_dsrcUrl,local_username,local_password,uuid);
		local.setId(new Long(0));
		return local;
	}
	/**
	 * 执行保存
	 * @param model
	 */
	public void save(SysExdbsrcCenter model){
		if(model!=null){
			if(model.getUuid()==null){
				model.setUuid(UUIDUtil.getUUID());
			}
		}
		extDBSrcDao.save(model);
	}
	/**
	 * 获得单条数据
	 * @param id 主键
	 * @return
	 */
	public SysExdbsrcCenter getModel(long id){
		return extDBSrcDao.getModel(id);
	}
	/**
	 * 删除单条数据
	 * @param id 主键
	 */
	public void delModel(SysExdbsrcCenter model){
		extDBSrcDao.delModel(model);
	}
	/**
	 * 更新单条数据
	 * @param model
	 */
	public void updateModle(SysExdbsrcCenter model){
		if(model.getUuid()==null){
			model.setUuid(UUIDUtil.getUUID());
		}
		extDBSrcDao.updateModle(model);
	}
	/**
	 * 测试能否链接到数据源
	 * @param model
	 * @return 0:链接成功;1:加载驱动失败;2:注册驱动失败;3:用户名或密码错误;4:链接地址错误;5:链接失败(其他原因)
	 */
	public int testCon(SysExdbsrcCenter model,String sql){
		int code=5;
		if(model!=null&sql!=null){
			String dsrcType=model.getDsrcType().toUpperCase();		
			if(dsrcType.equals("Oracle (Thin driver)".toUpperCase()) || dsrcType.equals("ORACLE".toUpperCase())){
				code=extDBSrcDao.testOraCon(model,sql);
			}//Oracle测试
			else if(dsrcType.startsWith("Microsoft SQL Server".toUpperCase())){
				code=extDBSrcDao.testMssCon(model,sql);
			}//Microsoft SQL Server测试
			else if(dsrcType.equals("MySQL Connector/J".toUpperCase())){
				code=extDBSrcDao.testMySQLCon(model,sql);
			}//MySQL测试
		}
		
		return code;
	}
	//=================================//

	public ExtDBSrcDao getExtDBSrcDao() {
		return extDBSrcDao;
	}

	public void setExtDBSrcDao(ExtDBSrcDao extDBSrcDao) {
		this.extDBSrcDao = extDBSrcDao;
	}
	

}
