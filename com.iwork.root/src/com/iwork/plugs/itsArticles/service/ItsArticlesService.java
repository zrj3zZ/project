package com.iwork.plugs.itsArticles.service;

import java.util.HashMap;
import java.util.List;

import com.iwork.plugs.itsArticles.dao.ItsArticlesDao;

public class ItsArticlesService {
	
	private ItsArticlesDao itsArticlesDao;
	
	public List<HashMap<String, Object>> getItsList(String khbh,String zdmc,
			String fjmc, int pageNumber, int pageSize) {
		return itsArticlesDao.getItsList(khbh,zdmc,fjmc,pageNumber,pageSize);
	}

	public int getItsListSize(String khbh,String zdmc,String fjmc) {
		return itsArticlesDao.getItsListSize(khbh,zdmc,fjmc);
	}
	
	public void setItsArticlesDao(ItsArticlesDao itsArticlesDao) {
		this.itsArticlesDao = itsArticlesDao;
	}
}
