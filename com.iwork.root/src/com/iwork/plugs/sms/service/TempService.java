package com.iwork.plugs.sms.service;


import com.iwork.plugs.sms.bean.TempMst;
import com.iwork.plugs.sms.dao.TempDao;

public class TempService {
private TempDao tempDao;
public void setTempDao(TempDao TempDao) {
	this.tempDao = TempDao;
}

/**
 * 临时短信加入到数据库
 * @param mm
 */
public void adddb(TempMst tm){
	tempDao.adddb(tm);
}
}
