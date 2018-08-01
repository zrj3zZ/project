package com.iwork.plugs.sms.action;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.iwork.plugs.sms.bean.TempMst;
import com.iwork.plugs.sms.service.TempService;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 临时短信加入数据库
 * @author Administrator
 *
 */
public class TempAction extends ActionSupport {
private TempService tempService;
public String add(String content, HashMap<String, String> nums,
		String uuid, String uid){
	String num = "";
	String c = "";
	String pathname = "";
	TempMst tm=new TempMst();
	Calendar cl = Calendar.getInstance();
	Date date = cl.getTime(); // 返回Date型日期时间 
	tm.setBatchnum(uuid);
	//tm.setBillinglen(billinglen)
	Iterator iter = nums.entrySet().iterator();
	while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		num = String.valueOf(entry.getKey());
		String name = String.valueOf(entry.getValue());
//		MsgUtil mu=new MsgUtil();
//		c = mu.replaceWildcard(uid, num, content, name);
//		if (c.length() == 0) {
//			continue;
//		}
		
	}	
	return "";
  }
public void setTempService(TempService tempService) {
	this.tempService = tempService;
}

}
