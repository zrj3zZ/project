package com.iwork.core.server.servicemanager.service;

import java.util.List;
  

import com.iwork.core.server.servicemanager.dao.SysServiceManagerDAO;
import com.iwork.core.server.servicemanager.model.Sysservice;


public class SysServiceManagerService {
	private SysServiceManagerDAO sysServiceManagerDAO;
	
	
	public void deleteBoData(Long id) {
		Sysservice model=sysServiceManagerDAO.getBoData(id);
		sysServiceManagerDAO.deleteBoData(model);
	}

	public List<Sysservice> getAll() {
		// TODO Auto-generated method stub
		return sysServiceManagerDAO.getAll();
	}

	public Sysservice getBoData(Long id) {
		return sysServiceManagerDAO.getBoData(id);
	} 

	public List getBoDatas(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return sysServiceManagerDAO.getBoDatas(pageSize, startRow);
	}

	public String getOrderIndex(){
		return sysServiceManagerDAO.getOrderIndex();
	}

	public int getRows() {
		// TODO Auto-generated method stub
		return sysServiceManagerDAO.getRows();
	}

	/**
	 * 保存服务模型
	 * @param obj
	 */
	public void saveModel(Sysservice obj) {
		if(obj.getId()==null){
			obj.setOrderindex(sysServiceManagerDAO.getMaxID());
			sysServiceManagerDAO.addBoData(obj);
		}else{
			sysServiceManagerDAO.updateBoData(obj);
		}
		

	}
	
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(Long id){
		String type="up";
		sysServiceManagerDAO.updateIndex(id, type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(Long id){
		String type="down";
		sysServiceManagerDAO.updateIndex(id, type);
	}
	public void setSysServiceManagerDAO(SysServiceManagerDAO sysServiceManagerDAO) {
		this.sysServiceManagerDAO = sysServiceManagerDAO;
	}
	/**
	 * 改变服务状态
	 */
	public void changeStatus(Long id){
		Sysservice model=sysServiceManagerDAO.getBoData(id);
		if(new Long(1).equals(model.getStatus())){
			model.setStatus(new Long(0));
			sysServiceManagerDAO.updateBoData(model);
		}else if(new Long(0).equals(model.getStatus())){
			model.setStatus(new Long(1));
			sysServiceManagerDAO.updateBoData(model);
		}
	}
}
