package com.iwork.app.schedule;


import java.util.Date;

import com.iwork.commons.util.UtilDate;

public class testEvent5 implements IWorkScheduleInterface {
    private int count = 0;
	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：["+UtilDate.dateFormat(new Date())+"]-流程通知数据清理结束......");
		System.out.println("     共清理流程通知["+count+"]个！");
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["+UtilDate.dateFormat(new Date())+"]-流程通知数据清理前...... ");
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		
//		count = this.getCallMsgList().size();
		System.out.println("服务启动=======================提示：["+UtilDate.dateFormat(new Date())+"]-流程通知数据清理中...... ");
		return true;
	}
}
