package com.iwork.plugs.sms.action;

import java.util.Date;
import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.service.AddlogService;
import com.opensymphony.xwork2.ActionSupport;

public class AddlogAction  extends ActionSupport{
private AddlogService addlogService;
	public AddlogService getAddlogService() {
	return addlogService;
}
public void setAddlogService(AddlogService addlogService) {
	this.addlogService = addlogService;
}
	public  void add(String uid, int type, String value) {
		
		LogMst lm=new LogMst();
//		Calendar c = Calendar.getInstance();
//		Date date = c.getTime(); // 返回Date型日期时间 
		Date date=new Date();
		lm.setLogtime(date);
		lm.setLogtype(String.valueOf(type));
		lm.setUserid(uid);
		lm.setValue(value);
		addlogService.add(lm);
		
	}
}

