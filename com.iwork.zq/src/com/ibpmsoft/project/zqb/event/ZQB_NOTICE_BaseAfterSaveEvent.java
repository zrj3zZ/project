package com.ibpmsoft.project.zqb.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.calender.model.IworkSchCalendar;
import com.iwork.plugs.calender.service.SchCalendarService;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import org.apache.log4j.Logger;
public class ZQB_NOTICE_BaseAfterSaveEvent extends ProcessStepTriggerEvent {
	private static Logger logger = Logger.getLogger(ZQB_NOTICE_BaseAfterSaveEvent.class);
	public ZQB_NOTICE_BaseAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}
	
	public boolean execute(){
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		
		setSchCal(map);
		return true;
	}

	private void setSchCal(HashMap map) {
		int i = 0;
		String noticename = map.get("NOTICENAME").toString();
		if(noticename.contains("临时股东大会通知公告")||noticename.contains("临时股东大会的通知公告")||noticename.contains("临时股东大会通知的公告")){
			setSchCalSub(map,12);
			i=1;
		}
		if(i==0&&(noticename.contains("股东大会通知公告")||noticename.contains("股东大会的通知公告")||noticename.contains("股东大会通知的公告"))){
			setSchCalSub(map,17);
		}
	}

	private void setSchCalSub(HashMap map,int num) {
		String zqdm = map.get("ZQDMXS").toString();
		String zqjc = map.get("ZQJCXS").toString();
		String noticedate = map.get("NOTICEDATE").toString();
		StringBuffer title = new StringBuffer();
		title.append("您好，").append(zqjc).append("（").append(zqdm).append("）请确认是否于近期有股东大会即将召开，如正常召开，请及时披露相关公告。");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date notice_date = null;
		try {
			notice_date = sdf.parse(noticedate);
		} catch (ParseException e) {
			logger.error(e,e);
			return;
		}
		Calendar notice_date_cal = Calendar.getInstance();
		notice_date_cal.setTime(notice_date);
		
		notice_date_cal.add(Calendar.DAY_OF_YEAR, num);
		Calendar notice_date_cal_add12 = notice_date_cal;
		Date startdate = notice_date_cal_add12.getTime();//+12 AS STARTDATE
		
		notice_date_cal.add(Calendar.DAY_OF_YEAR, 2);
		Calendar notice_date_cal_add15 = notice_date_cal;
		Date enddate = notice_date_cal_add15.getTime();//+15 AS ENDDATE
		
		String khbh = map.get("KHBH").toString();
		
		List lables = new ArrayList();lables.add("USERID");
		Map params = new HashMap();params.put(1, khbh);
		List<HashMap> userlist = com.iwork.commons.util.DBUtil.getDataList(lables, "SELECT USERID FROM ORGUSER WHERE ID=(SELECT MIN(ID) FROM ORGUSER WHERE EXTEND1=?)", params);
		
		SchCalendarService schCalendarService = null;
		for (HashMap data : userlist) {
			if(schCalendarService==null){
				schCalendarService = (SchCalendarService)SpringBeanUtil.getBean("schCalendarService");
			}
			String rcid = DBUtil.getString("SELECT SUBID FROM BD_XP_TYRCGLB WHERE MAINID="+map.get("dataid").toString(), "SUBID");
			if(rcid!=null&&!rcid.equals("")&&schCalendarService.getModel(rcid)==null){
				DBUtil.executeUpdate("DELETE FROM BD_XP_TYRCGLB WHERE MAINID="+map.get("dataid").toString()+" AND SUBID="+rcid);
				rcid="";
			}
			if(rcid!=null&&!rcid.equals("")){
				IworkSchCalendar model = schCalendarService.getModel(rcid);
				model.setStartdate(startdate);
				model.setEnddate(enddate);
				schCalendarService.update(model);
			}else{
				IworkSchCalendar model = new IworkSchCalendar();
				model.setUserid(data.get("USERID").toString());
				model.setStartdate(startdate);
				model.setEnddate(enddate);
				model.setTitle(title.toString());
				model.setStarttime("09:00");
				model.setEndtime("17:00");
				model.setIsalert(1l);//是否提醒
				model.setAlerttime("15");//提前多久提醒(分钟)
				model.setIsallday(0l);
				schCalendarService.save(model);
				
				
				String UUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='通用日程关联表'", "UUID");
				
				Long instanceid = DemAPI.getInstance().newInstance(UUID, data.get("USERID").toString());
				
				HashMap hashdata = new HashMap();
				hashdata.put("MAINID", map.get("dataid").toString());
				hashdata.put("SUBID",model.getId());
				hashdata.put("CREATEUSERID", UserContextUtil.getInstance().getCurrentUserId());
				hashdata.put("CJSJ", UtilDate.getNowdate());
				
				DemAPI.getInstance().saveFormData(UUID, instanceid, hashdata, false);
			}
		}
	} 
}

