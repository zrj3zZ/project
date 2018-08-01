package com.iwork.app.navigation.directory.service;

import java.util.List;

import com.iwork.app.navigation.directory.dao.SysNavDirectoryDAO;
import com.iwork.app.navigation.directory.model.SysNavDirectory;

public class SysNavDirectoryService {
	private SysNavDirectoryDAO sysNavDirectoryDAO;
	
	public void addBoData(SysNavDirectory obj) {
		sysNavDirectoryDAO.addBoData(obj);
	} 

	public void deleteBoData(Long id) {
		SysNavDirectory model=sysNavDirectoryDAO.getBoData(id);
		sysNavDirectoryDAO.deleteBoData(model);
	}

	public List getAll() {
		// TODO Auto-generated method stub
		return sysNavDirectoryDAO.getAll();
	}
	
	public List<SysNavDirectory> getDirectoryList() {
		// TODO Auto-generated method stub
		return sysNavDirectoryDAO.getAll();
	}
	public SysNavDirectory getBoData(Long id) {
		// TODO Auto-generated method stub
		return sysNavDirectoryDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return sysNavDirectoryDAO.getBoDatas(pageSize, startRow);
	}

	public List getBoDatas(String fieldname, String value, int pageSize,
			int startRow) {
		// TODO Auto-generated method stub
		return sysNavDirectoryDAO.getBoDatas(fieldname, value, pageSize, startRow);
	}

	public Long getMaxID() {
		// TODO Auto-generated method stub
		return Long.parseLong(sysNavDirectoryDAO.getMaxID());
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return sysNavDirectoryDAO.getRows();
	}

	public int getRows(String fieldname, String value) {
		// TODO Auto-generated method stub
		return sysNavDirectoryDAO.getRows(fieldname, value);
	} 

	public void updateBoData(SysNavDirectory obj) {
		sysNavDirectoryDAO.updateBoData(obj);

	}
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(int id){
		String type="up";
		sysNavDirectoryDAO.updateIndex(id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(int id){
		String type="down";
		sysNavDirectoryDAO.updateIndex(id, type);
	}

	public void setSysNavDirectoryDAO(SysNavDirectoryDAO sysNavDirectoryDAO) {
		this.sysNavDirectoryDAO = sysNavDirectoryDAO;
	}
}
