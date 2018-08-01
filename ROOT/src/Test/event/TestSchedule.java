package Test.event;

import java.util.HashMap;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;

public class TestSchedule implements IWorkScheduleInterface {

	public boolean executeAfter() throws ScheduleException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		String actDefId = "CSQJLC:1:128208";
		String targetStepId = "usertask12";
		String createUser = "ADMIN";
		String[] receiveUser = {"WANGLEI"};
		
		Long formId = new Long(58);
		String formno = "cssq001";
		HashMap formData = new HashMap();
		formData.put("SQRZH", "ADMIN");
		formData.put("SQR", "超级管理员");
		formData.put("SQRQ", "2016-05-01");
		formData.put("SQYY", "测试原因");
		formData.put("QJLX", "事假");
		formData.put("QJRQC", "2016-06-01");
		formData.put("QJRQD", "2016-06-30");
		formData.put("RESOURCEID", "001");
		formData.put("RESOURCENAME", "啊啊啊啊啊");
		
		String title = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//		ProcessAPI.getInstance().startProcessInstance(actDefId, targetStepId, receiveUser, formId, createUser, formno, formData, title);
		return true;
	} 
}
