package com.ibpmsoft.project.zqb.service;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.dao.CjywlczyDao;

public class CjywlczyService {
	private static Logger logger = Logger.getLogger(CjywlczyService.class);
	private CjywlczyDao cjywlczyDao;

	public List<HashMap> getCjList(String sxmc, int pageSize, int pageNumber){
		List<HashMap> list=cjywlczyDao.getCjList(sxmc, pageSize, pageNumber);
		return list;
	}
	public int getCjListSize(String sxmc) {
		return cjywlczyDao.getCjListSize(sxmc).size();
	}
	public CjywlczyDao getCjywlczyDao() {
		return cjywlczyDao;
	}

	public void setCjywlczyDao(CjywlczyDao cjywlczyDao) {
		this.cjywlczyDao = cjywlczyDao;
	}

}
