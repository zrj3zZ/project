/**
 * 
 */
package com.iwork.plugs.sms.util;

import java.util.Calendar;
import java.util.Date;

import com.iwork.plugs.sms.bean.LogMst;



/**
 * @author lee
 * 短信日志
 */
public class MsgLog {

	public static void add(String uid, int type, String value) {
		LogMst lm=new LogMst();
		Calendar c = Calendar.getInstance();
		Date date = c.getTime(); // 返回Date型日期时间 
		lm.setLogtime(date);
		lm.setLogtype(String.valueOf(type));
		lm.setUserid(uid);
		lm.setValue(value);
		
		
	}
}
