package com.iwork.app.message.sysmsg.service;

import java.util.List;

import com.iwork.app.message.sysmsg.dao.SysMessageLogDAO;
import com.iwork.app.message.sysmsg.model.SysMessageLog;

public class SysMessageLogService {
	private SysMessageLogDAO sysMessageLogDAO;
	
	public int getMsgLogRow() {
		return sysMessageLogDAO.getMsgLogRow();
	}
	
	public List<SysMessageLog> getMsgLogList(int pageSize, int pageNow) {
		return sysMessageLogDAO.getMsgLogList(pageSize, pageNow);
	}
	
	public void save(SysMessageLog model) {
		sysMessageLogDAO.save(model);
	}

	public SysMessageLogDAO getSysMessageLogDAO() {
		return sysMessageLogDAO;
	}

	public void setSysMessageLogDAO(SysMessageLogDAO sysMessageLogDAO) {
		this.sysMessageLogDAO = sysMessageLogDAO;
	}

	

}
