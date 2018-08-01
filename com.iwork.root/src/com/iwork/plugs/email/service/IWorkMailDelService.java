package com.iwork.plugs.email.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.iwork.plugs.email.dao.IWorkMailDelDAO;
import com.iwork.plugs.email.dao.IWorkMailOwnerDAO;
import com.iwork.plugs.email.dao.IWorkMailTaskDAO;

/**
 * IWorkMailDelService
 * @author zouyalei
 *
 */
public class IWorkMailDelService {

	public IWorkMailDelDAO iWorkMailDelDAO;
	public IWorkMailOwnerDAO iWorkMailOwnerDAO;
	public IWorkMailTaskDAO iWorkMailTaskDAO;
	
	/**
	 * 获取条数记录
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public int getMailDelListSize(String userId) {
	
		return iWorkMailDelDAO.getMailDelListSize(userId);
	}
	
	
	/**
	 * 获取分页查询
	 * @param userId
	 * @param pageSize
	 * @param currLogPage
	 * @return
	 */
	public List<HashMap> getMailDelList(String userId,int pageSize,int pageNumber){
		List<HashMap> mailDelList = new ArrayList<HashMap>();

		mailDelList=iWorkMailDelDAO.getMailDelAllList(userId,pageSize, pageNumber);

		return mailDelList;
		
	}
	
	public void deleteMailDel(String ids){
		 
	}

	public IWorkMailDelDAO getiWorkMailDelDAO() {
		return iWorkMailDelDAO;
	}

	public void setiWorkMailDelDAO(IWorkMailDelDAO iWorkMailDelDAO) {
		this.iWorkMailDelDAO = iWorkMailDelDAO;
	}

	public IWorkMailOwnerDAO getiWorkMailOwnerDAO() {
		return iWorkMailOwnerDAO;
	}

	public void setiWorkMailOwnerDAO(IWorkMailOwnerDAO iWorkMailOwnerDAO) {
		this.iWorkMailOwnerDAO = iWorkMailOwnerDAO;
	}

	public IWorkMailTaskDAO getiWorkMailTaskDAO() {
		return iWorkMailTaskDAO;
	}

	public void setiWorkMailTaskDAO(IWorkMailTaskDAO iWorkMailTaskDAO) {
		this.iWorkMailTaskDAO = iWorkMailTaskDAO;
	}
	
}
