package com.iwork.plugs.noticeSynchronous;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.sdk.DemAPI;

import news.common.HelloWebService;
import news.service.Comnotice;
import news.service.Gpgsxq;

public class NoticeSynchronous implements IWorkScheduleInterface {
	private static Logger logger = Logger.getLogger(NoticeSynchronous.class);
	public boolean executeAfter() throws ScheduleException {
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		String UUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='已披露公告'", "UUID");
		String param = DBUtil.getString("SELECT COMPANYNAME FROM (SELECT COMPANYNAME,ROWNUM AS RN FROM (SELECT COMPANYNAME FROM ORGCOMPANY ORDER BY ID)) WHERE RN=1", "COMPANYNAME");
		List<Gpgsxq> gpgsxqList = HelloWebService.getGpgsxq(param);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Gpgsxq gpgsxq : gpgsxqList) {
			String zqdm = gpgsxq.getGfdm().getValue();
			
			Map params = new HashMap();
			params.put(1, zqdm);
			String khbh = com.iwork.commons.util.DBUtil.getDataStr("CUSTOMERNO", "SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE ZQDM=?", params);
			
			if(khbh!=null&&!khbh.equals("")){
				//清空分派持续督导
				//SELECT QYNBRYSH,KHFZR,ZZCXDD,FHSPR,ZZSPR,CWSCBFZR2,CWSCBFZR3,FBBWJSHR,GGFBR,KHFZRDQ,ZZCXDDDQ,FHSPRDQ,ZZSPRDQ,CWSCBFZR2DQ,CWSCBFZR3DQ,FBBWJSHRDQ,GGFBRDQ FROM BD_MDM_KHQXGLB WHERE KHBH='CNO2017-10-92';
				Map params1 = new HashMap();
				params1.put(1, khbh);
				com.iwork.commons.util.DBUtil.update("UPDATE BD_MDM_KHQXGLB SET QYNBRYSH='',KHFZR='',ZZCXDD='',FHSPR='',ZZSPR='',CWSCBFZR2='',CWSCBFZR3='',FBBWJSHR='',GGFBR='',KHFZRDQ='',ZZCXDDDQ='',FHSPRDQ='',ZZSPRDQ='',CWSCBFZR2DQ='',CWSCBFZR3DQ='',FBBWJSHRDQ='',GGFBRDQ='' WHERE KHBH=?", params1);
				
				//设置用户有效期
				Map params2 = new HashMap();
				params2.put(1, sdf.format(cal.getTime()));
				params2.put(2, khbh);
				com.iwork.commons.util.DBUtil.update("UPDATE ORGUSER SET ENDDATE=TO_DATE(?,'yyyy-MM-dd') WHERE EXTEND1=?", params2);
				
				//更改挂牌状态、挂牌时间
				Map params3 = new HashMap();
				params3.put(1, UtilDate.getNowdate());
				params3.put(2, khbh);
				com.iwork.commons.util.DBUtil.update("UPDATE BD_ZQB_KH_BASE SET YGP='已挂牌',EXTEND5=? WHERE CUSTOMERNO=?", params3);
			}
		}
		//插入已披露公告
		List<String> ggnameList = com.iwork.commons.util.DBUtil.getStringList("GGMSG", "SELECT GGNAME||GSDM||TO_CHAR(PLRQ,'yyyy-MM-dd') GGMSG  FROM YPLGG", null);
		
		List<Comnotice> comnoticeList = HelloWebService.getComnotice(param);
		for (Comnotice comnotice : comnoticeList) {
			String Noticename = comnotice.getNoticename().getValue();
			Integer Gsdm = comnotice.getGsdm().getValue();
			String Noticepublishdate = comnotice.getNoticepublishdate().getValue();
			String Noticeurl = comnotice.getNoticeurl().getValue();
			if(!ggnameList.contains(Noticename+Gsdm.toString()+Noticepublishdate)){
				Long instanceId = DemAPI.getInstance().newInstance(UUID, "NEEQMANAGER");
				HashMap hashdata = new HashMap();
				hashdata.put("INSTANCEID", instanceId);
				hashdata.put("GGNAME", Noticename);
				hashdata.put("GSDM", Gsdm);
				hashdata.put("FILEURL", Noticeurl);
				try {
					hashdata.put("PLRQ", sdf.parse(comnotice.getNoticepublishdate().getValue()));
				} catch (ParseException e) {
					logger.error(e,e);
				}
				DemAPI.getInstance().saveFormData(UUID, instanceId, hashdata, false);
			}
		}
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		return true;
	}
}


