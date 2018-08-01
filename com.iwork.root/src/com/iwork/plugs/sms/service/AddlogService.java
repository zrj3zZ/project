package com.iwork.plugs.sms.service;

import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.dao.AddlogDao;

public class AddlogService {
private AddlogDao addlogDao;
public void setAddlogDao(AddlogDao AddlogDao){
	this.addlogDao=AddlogDao;
}
public void add(LogMst lm){
	addlogDao.add(lm);
}
}
