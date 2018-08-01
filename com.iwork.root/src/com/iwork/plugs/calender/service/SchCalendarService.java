package com.iwork.plugs.calender.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.CxddfqlcConf;
import com.iwork.app.conf.GgsplcConf;
import com.iwork.app.conf.RcywcbLcConf;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.persion.dao.SysPersonDAO;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.calender.Util.CalendarUtil;
import com.iwork.plugs.calender.dao.SchCalendarDAO;
import com.iwork.plugs.calender.model.IworkSchCalendar;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

import net.sf.json.JSONArray;
public class SchCalendarService {
	private SchCalendarDAO schCalendarDAO;
	private SysPersonDAO sysPersonDAO;
	private final static String CN_FILENAME = "/common.properties";
	private static Logger logger = Logger.getLogger(SchCalendarService.class);
	
	public String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	public List<Map> getGzrzxx(int pageSize, int pageNow,String startDate, String endDate,String gznr,String userid) {
		return schCalendarDAO.getGzrzxx(pageSize, pageNow, startDate, endDate,gznr,userid);
	}
	public int getGzrzxxSize(String startDate, String endDate,String ggnr,String userid) {
		return schCalendarDAO.getGzrzxxSize(startDate, endDate, ggnr, userid).size();
	}
	public List<Map> getWorkLogList(int pageSize, int pageNow, String username,String startDate, String endDate,String depname) {
		if(username!=null&&!"".equals(username)){
		if(username.contains("-")){
			int index=username.indexOf("-");
			username=username.substring(index+1, username.length());
			
		}
		}
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
        String cuid = user.getUserid();
        Long orgrolid = user.getOrgroleid();
        String ismanager = "";
        String departmentname = "";
        int type = -1;
        
        List lables = new ArrayList();
        lables.add("USERID");
        lables.add("DEPARTMENTNAME");
        lables.add("ISMANAGER");
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM (SELECT USERID,DEPARTMENTNAME,ISMANAGER FROM ORGUSER UNION SELECT USERID,DEPARTMENTNAME,ISMANAGER FROM ORGUSERMAP)WHERE USERID=? AND ISMANAGER=1");
        
        Map params = new HashMap();
        params.put(1, cuid);
        
        List<HashMap> userdata = com.iwork.commons.util.DBUtil.getDataList(lables, sb.toString(), params);
        for (int i = 0; i < userdata.size(); i++) {
       	 ismanager = userdata.get(i).get("ISMANAGER").toString();
       	 if(i==0){
       		 departmentname+=("'"+userdata.get(i).get("DEPARTMENTNAME").toString()+"'");
       	 }else{
       		 departmentname+=(",'"+userdata.get(i).get("DEPARTMENTNAME").toString()+"'");
       	 }
        }
        
        boolean flag = false;
        String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='工作总结权限表单'", "UUID");
        List<HashMap> datalist = DemAPI.getInstance().getAllList(uuid, null, null);
        for (HashMap datamap : datalist) {
			if(datamap.get("USERID").toString().equals(cuid)){
				flag = true;
        		break;
			}
		}
        
        if(orgrolid==3l){
        	type=0;
        }else{
        	if(!flag){
        		if(departmentname!=null&&!departmentname.equals("")){
        			type=1;
        		}else{
        			type=2;
        		}
        	}
        }
		
		return schCalendarDAO.getWorkLogList(pageSize, pageNow, username, type, cuid,depname);
	}

	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public String getOtherUserJson() {
		StringBuffer jsonHtml = new StringBuffer();
		List<String> list = schCalendarDAO.getConfigTypeUsers();

		HashMap hash = new HashMap();
		hash.put("id", "001");
		hash.put("name", "共享日志的用户");
		hash.put("icon", "iwork_img/logohome.gif");
		hash.put("open", true);
		List<Map> items = new ArrayList();
		for (String uid : list) {
			Map<String, Object> item = new HashMap<String, Object>();
			UserContext uc = UserContextUtil.getInstance().getUserContext(uid);
			if (uc != null) {
				item.put("id", uid);
				item.put("name", uc.get_userModel().getUsername());
				item.put("userid", uc.get_userModel().getUserid());
				item.put("icon", "iwork_img/user.png");
				items.add(item);
			}
		}
		hash.put("children", items);
		JSONArray json = JSONArray.fromObject(hash);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public void addGlb(Long instanceId,IworkSchCalendar model){
		String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='通用日程关联表'", "UUID");
		String rcid = DBUtil.getString("SELECT ID FROM iwork_sch_calendar WHERE TITLE='"+model.getTitle()+"' and userid='"+model.getUserid()+"'  order by id desc", "ID");
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		if(rcid!=null){
			Long ins_t = DemAPI.getInstance().newInstance(uuid, userid);
			HashMap hashdata_t = new HashMap();
			hashdata_t.put("MAINID", rcid);//日程ID
			hashdata_t.put("SUBID",instanceId);//阶段key+流程instanceId做唯一标示
			hashdata_t.put("CREATEUSERID", userid);
			hashdata_t.put("CJSJ", date);
			DemAPI.getInstance().saveFormData(uuid, ins_t, hashdata_t, false);
		}
	}
	public void processAddlog(String action,String titl,Long instanceId,String actStepDefId){
		List<String> l = new ArrayList<String>();
		GgsplcConf _ggsplcConf = SystemConfig._ggsplcConf;
		CxddfqlcConf _cxddfqlcConf = SystemConfig._cxddfqlcConf;
		RcywcbLcConf _rcywcbLcConf = SystemConfig._rcywcbLcConf;
		l.add(_ggsplcConf.getJd0());l.add(_cxddfqlcConf.getJd1());l.add(_rcywcbLcConf.getJd0());
		l.add(_ggsplcConf.getJd1());l.add(_cxddfqlcConf.getJd2());l.add(_rcywcbLcConf.getJd1());
		l.add(_ggsplcConf.getJd2());l.add(_cxddfqlcConf.getJd3());l.add(_rcywcbLcConf.getJd2());
		l.add(_ggsplcConf.getJd3());l.add(_cxddfqlcConf.getJd4());l.add(_rcywcbLcConf.getJd3());
		l.add(_ggsplcConf.getJd4());l.add(_cxddfqlcConf.getJd5());l.add(_rcywcbLcConf.getJd4());
		l.add(_ggsplcConf.getJd5());l.add(_cxddfqlcConf.getJd6());l.add(_rcywcbLcConf.getJd5());
		l.add(_ggsplcConf.getJd6());l.add(_cxddfqlcConf.getJd7());l.add(_rcywcbLcConf.getJd6());
		l.add(_ggsplcConf.getJd7());l.add(_cxddfqlcConf.getJd8());l.add(_rcywcbLcConf.getJd7());
		l.add(_ggsplcConf.getJd8());l.add(_cxddfqlcConf.getJd9());l.add(_rcywcbLcConf.getJd8());
		l.add(_ggsplcConf.getJd9());l.add(_cxddfqlcConf.getJd10());l.add(_rcywcbLcConf.getJd9());
		l.add(_ggsplcConf.getJd10());
		l.add(_ggsplcConf.getJd11());
		l.add(_ggsplcConf.getJd12());
		
		if(!l.contains(actStepDefId)){
			return;
		}
		String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='通用日程关联表'", "UUID");
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		Task t = ProcessAPI.getInstance().newTaskId(instanceId);
		String starttime = "08:00";
		String endtime = "17:00";
		Long isallday = 0l;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			logger.error(e,e);
		}
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//判断是否存在lcbh字段
		boolean czlcbh = dataMap.containsKey("LCBH");  
 		if(czlcbh&&dataMap.get("LCBH").toString().startsWith("DGDYLC")){
			String dgdygdblr=getConfigUUID("dgdygdblr");
			if(actStepDefId.equals(dgdygdblr)){
				if(!dataMap.get("SPZT").toString().equals("驳回")){
					String yjghsj=dataMap.get("YJGHSJ").toString();
					Date d = new Date();
					try {
						d = sdf.parse(yjghsj);
					} catch (ParseException e) {
						logger.error(e,e);
					}     
					Calendar cal=Calendar.getInstance();
					cal.setTime(d);
					cal.add(Calendar.DATE, -7); 
					if(!cal.getTime().before(date)){
						date=cal.getTime();
					}
					starttime = "10:00";
					String title = t.getDescription();
					String targetUserid = t.getAssignee();
					String currentUserid=t.getOwner();
					//给下一节点人制定日程
					IworkSchCalendar model_t = new IworkSchCalendar();
					model_t.setUserid(targetUserid);
					model_t.setTitle(title + "    待归还");
					model_t.setStartdate(date);
					try {
						model_t.setEnddate(sdf.parse(yjghsj));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
					model_t.setStarttime(starttime);
					model_t.setEndtime(endtime);
					model_t.setIsallday(isallday);
					
					this.save(model_t);
					
					//添加流程日程关联关系
					Long ins_t = DemAPI.getInstance().newInstance(uuid, targetUserid);
					HashMap hashdata_t = new HashMap();
					hashdata_t.put("MAINID", model_t.getId());//日程ID
					hashdata_t.put("SUBID",instanceId);//阶段key+流程instanceId做唯一标示
					hashdata_t.put("CREATEUSERID", targetUserid);
					hashdata_t.put("CJSJ", date);
					DemAPI.getInstance().saveFormData(uuid, ins_t, hashdata_t, false);
					//给当前节点人制定日程
					IworkSchCalendar model_c = new IworkSchCalendar();
					model_c.setUserid(currentUserid);
					model_c.setTitle(title + "    待归还");
					model_c.setStartdate(date);
					try {
						model_c.setEnddate(sdf.parse(yjghsj));
					} catch (Exception e) {
						logger.error(e,e);
					}
					model_c.setStarttime(starttime);
					model_c.setEndtime(endtime);
					model_c.setIsallday(isallday);
				
					this.save(model_c);
					
					Long ins_c = DemAPI.getInstance().newInstance(uuid, currentUserid);
					HashMap hashdata_c = new HashMap();
					hashdata_c.put("MAINID", model_c.getId());//日程ID
					hashdata_c.put("SUBID", instanceId);//阶段key+流程instanceId做唯一标示
					hashdata_c.put("CREATEUSERID", currentUserid);
					hashdata_c.put("CJSJ", date);
					DemAPI.getInstance().saveFormData(uuid, ins_c, hashdata_c, false);
				}
			}
		}else if(action.equals("顺序流转")||actStepDefId.equals(SystemConfig._ggsplcConf.getJd1())||actStepDefId.equals(SystemConfig._cxddfqlcConf.getJd1())||actStepDefId.equals(SystemConfig._rcywcbLcConf.getJd0())){//起草提交
			String title = t.getDescription();
			
			//提交人  添加
			Map param = new HashMap();
			param.put(1, actStepDefId+instanceId);
			param.put(2, userid);
			String id = com.iwork.commons.util.DBUtil.getDataStr("MAINID", "SELECT MAINID FROM BD_XP_TYRCGLB WHERE SUBID=? AND CREATEUSERID=?", param);
			if(id!=null&&!id.equals("")){//驳回到起草人再次提交不添加关联关系
				IworkSchCalendar model = this.getModel(id);
				model.setTitle(title + "    已审批");
				this.update(model);//更新当前日程信息
				
				String targetUserid = t.getAssignee();
				Map params = new HashMap();
				params.put(1, t.getTaskDefinitionKey()+instanceId);
				params.put(2, targetUserid);
				String id_t = com.iwork.commons.util.DBUtil.getDataStr("MAINID", "SELECT MAINID FROM BD_XP_TYRCGLB WHERE SUBID=? AND CREATEUSERID=?", params);
				if(id_t==null||id_t.equals("")){
					//目标人  添加
					targetUserid = t.getAssignee();
					IworkSchCalendar model_t = new IworkSchCalendar();
					model_t.setUserid(targetUserid);
					model_t.setTitle(title + "    审批中");
					model_t.setStartdate(date);
					model_t.setEnddate(date);
					model_t.setStarttime(starttime);
					model_t.setEndtime(endtime);
					model_t.setIsallday(isallday);
					this.save(model_t);
					
					//添加流程日程关联关系
					Long ins_t = DemAPI.getInstance().newInstance(uuid, targetUserid);
					HashMap hashdata_t = new HashMap();
					hashdata_t.put("MAINID", model_t.getId());//日程ID
					hashdata_t.put("SUBID", t.getTaskDefinitionKey()+instanceId);//阶段key+流程instanceId做唯一标示
					hashdata_t.put("CREATEUSERID", targetUserid);
					hashdata_t.put("CJSJ", date);
					DemAPI.getInstance().saveFormData(uuid, ins_t, hashdata_t, false);
				}else{
					IworkSchCalendar model_t = this.getModel(id_t);
					model_t.setTitle(title + "    审批中");
					this.update(model_t);//更新目标日程信息
				}
			}else{//初次提交
				IworkSchCalendar model = new IworkSchCalendar();
				model.setUserid(userid);
				model.setTitle(title + "    已审批");
				model.setStartdate(date);
				model.setEnddate(date);
				model.setStarttime(starttime);
				model.setEndtime(endtime);
				model.setIsallday(isallday);
				this.save(model);
				//添加流程日程关联关系
				Long ins = DemAPI.getInstance().newInstance(uuid, userid);
				HashMap hashdata = new HashMap();
				hashdata.put("MAINID", model.getId());//日程ID
				hashdata.put("SUBID", actStepDefId+instanceId);//阶段key+流程instanceId做唯一标示
				hashdata.put("CREATEUSERID", userid);
				hashdata.put("CJSJ", date);
				DemAPI.getInstance().saveFormData(uuid, ins, hashdata, false);
				
				//目标人  添加
				String targetUserid = t.getAssignee();
				IworkSchCalendar model_t = new IworkSchCalendar();
				model_t.setUserid(targetUserid);
				model_t.setTitle(title + "    审批中");
				model_t.setStartdate(date);
				model_t.setEnddate(date);
				model_t.setStarttime(starttime);
				model_t.setEndtime(endtime);
				model_t.setIsallday(isallday);
				this.save(model_t);
				//添加流程日程关联关系
				Long ins_t = DemAPI.getInstance().newInstance(uuid, targetUserid);
				HashMap hashdata_t = new HashMap();
				hashdata_t.put("MAINID", model_t.getId());//日程ID
				hashdata_t.put("SUBID", t.getTaskDefinitionKey()+instanceId);//阶段key+流程instanceId做唯一标示
				hashdata_t.put("CREATEUSERID", targetUserid);
				hashdata_t.put("CJSJ", date);
				DemAPI.getInstance().saveFormData(uuid, ins_t, hashdata_t, false);
			}
			
		}else if(action.equals("归档")){//归档 无目标人
			//归档 修改日程
			Map params = new HashMap();
			params.put(1, actStepDefId+instanceId);
			params.put(2, userid);
			String id = com.iwork.commons.util.DBUtil.getDataStr("MAINID", "SELECT MAINID FROM BD_XP_TYRCGLB WHERE SUBID=? AND CREATEUSERID=?", params);
			IworkSchCalendar model = this.getModel(id);
			model.setTitle(titl + "    已归档");
			this.update(model);
		}else if(action.equals("驳回")){
			String title = t.getDescription();
			Map param = new HashMap();
			param.put(1, actStepDefId+instanceId);
			param.put(2, userid);
			String id = com.iwork.commons.util.DBUtil.getDataStr("MAINID", "SELECT MAINID FROM BD_XP_TYRCGLB WHERE SUBID=? AND CREATEUSERID=?", param);
			IworkSchCalendar model = this.getModel(id);
			model.setTitle(title + "    已驳回");
			this.update(model);
			
			String targetUserid = t.getAssignee();
			Map params = new HashMap();
			params.put(1, t.getTaskDefinitionKey()+instanceId);
			params.put(2, targetUserid);
			String id_t = com.iwork.commons.util.DBUtil.getDataStr("MAINID", "SELECT MAINID FROM BD_XP_TYRCGLB WHERE SUBID=? AND CREATEUSERID=?", params);
			IworkSchCalendar model_t = this.getModel(id_t);
			model_t.setTitle(title + "    审批中");
			this.update(model_t);
		}else{//过程提交
			String title = t.getDescription();
			
			//提交人  修改日程
			Map params = new HashMap();
			params.put(1, actStepDefId+instanceId);
			params.put(2, userid);
			String id = com.iwork.commons.util.DBUtil.getDataStr("MAINID", "SELECT MAINID FROM BD_XP_TYRCGLB WHERE SUBID=? AND CREATEUSERID=?", params);
			if(!id.equals("")&&id!=null){
				IworkSchCalendar model = this.getModel(id);
				model.setTitle(title + "    已审批");
				this.update(model);
			}
			
			//目标人  添加
			String targetUserid = t.getAssignee();
			IworkSchCalendar model_t = new IworkSchCalendar();
			model_t.setUserid(targetUserid);
			model_t.setTitle(title + "审批中");
			model_t.setStartdate(date);
			model_t.setEnddate(date);
			model_t.setStarttime(starttime);
			model_t.setEndtime(endtime);
			model_t.setIsallday(isallday);
			this.save(model_t);
			//添加流程日程关联关系
			Long ins_t = DemAPI.getInstance().newInstance(uuid, targetUserid);
			HashMap hashdata_t = new HashMap();
			hashdata_t.put("MAINID", model_t.getId());//日程ID
			hashdata_t.put("SUBID", t.getTaskDefinitionKey()+instanceId);//阶段key+流程instanceId做唯一标示
			hashdata_t.put("CREATEUSERID", targetUserid);
			hashdata_t.put("CJSJ", date);
			DemAPI.getInstance().saveFormData(uuid, ins_t, hashdata_t, false);
		}
	}

	/**
	 * 获得时间段内指定用户的一般日程
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	public String getJsonData(Date startdate, Date enddate, String userid) {
		List<IworkSchCalendar> list = schCalendarDAO.getPeriodList(startdate,
				enddate, userid);
		int length = list.size();
		StringBuffer html = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (int i = 0; i < length; i++) {
				if (list.get(i) == null)
					continue;
				IworkSchCalendar isc = (IworkSchCalendar) list.get(i);
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", isc.getId());
				item.put("userid", isc.getUserid());
				item.put("title", isc.getTitle());
				if (null == isc.getStarttime() || "".equals(isc.getStarttime())) {
					item.put("start", sdf.format(isc.getStartdate()).toString());
					item.put("end", sdf.format(isc.getEnddate()).toString());
				} else {
					item.put("start", sdf.format(isc.getStartdate()) + " "
							+ isc.getStarttime());
					item.put(
							"end",
							sdf.format(isc.getEnddate()) + " "
									+ isc.getEndtime());
				}
				Long isAllDayLong = isc.getIsallday();// 是否全天事件
				boolean isAllDay = false;
				if (null == isAllDayLong || 0 == isAllDayLong) {
					isAllDay = false;
				} else {
					isAllDay = true;
				}
				item.put("allDay", String.valueOf(isAllDay));
				item.put("alert", isc.getIsalert());
				item.put("alertTime", isc.getAlerttime());
				item.put("sharing", isc.getIssharing());
				item.put("remark", isc.getRemark());
				if (null == isc.getReStartdate()) {
					item.put("reStartdate", "");
				} else {
					item.put("reStartdate", sdf.format(isc.getReStartdate()));
				}
				if (null == isc.getReEnddate()) {
					item.put("reEnddate", "");
				} else {
					item.put("reEnddate", sdf.format(isc.getReEnddate()));
				}
				item.put("reStarttime", isc.getReStarttime());
				item.put("reEndtime", isc.getReEndtime());
				item.put("reMode", isc.getReMode());
				item.put("reDayInterval", isc.getReDayInterval());
				item.put("reWeekDate", isc.getReWeekDate());
				item.put("reMonthDays", isc.getReMonthDays());
				item.put("reYearMonth", isc.getReYearMonth());
				item.put("reYearDays", isc.getReYearDays());
				item.put("wczt", isc.getWczt());
				item.put("wcqk", isc.getWcqk());
				rows.add(item);
			}
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json);
		return html.toString();
	}

	/**
	 * 获得时间段内指定用户的重复日程(一条不拆分成多条进行显示)
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public String getJsonData_Repeate_Single(Date startdate, Date enddate,
			String userid) throws Exception {
		List<IworkSchCalendar> list = schCalendarDAO
				.getPeriodList_Repeate(userid);
		int length = list.size();
		StringBuffer html = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfMore = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (int i = 0; i < length; i++) {
				if (list.get(i) == null)
					continue;
				IworkSchCalendar isc = (IworkSchCalendar) list.get(i);
				Date endDate = isc.getReEnddate();
				String endDateStr = sdf.format(endDate);
				String endStr = endDateStr + " " + isc.getReEndtime();
				Date end = sdfMore.parse(endStr);
				Date cur = new Date();
				if (end.compareTo(cur) < 0) {
					continue;
				} else {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", isc.getId());
					item.put("userid", isc.getUserid());
					item.put("title", isc.getTitle());
					if (null == isc.getReStarttime()
							|| null == isc.getReEndtime()
							|| "".equals(isc.getReStarttime())
							|| "".equals(isc.getReEndtime())) {
						item.put("start", sdf.format(isc.getReStartdate())
								.toString());
						item.put("end", sdf.format(isc.getReEnddate())
								.toString());
					} else {
						item.put("start", sdf.format(isc.getReStartdate())
								+ " " + isc.getReStarttime());
						item.put("end", sdf.format(isc.getReEnddate()) + " "
								+ isc.getReEndtime());
					}
					Long isAllDayLong = isc.getIsallday();// 是否全天事件
					boolean isAllDay = false;
					if (null == isAllDayLong || 0 == isAllDayLong) {
						isAllDay = false;
					} else {
						isAllDay = true;
					}
					item.put("allDay", String.valueOf(isAllDay));
					item.put("alert", isc.getIsalert());
					item.put("alertTime", isc.getAlerttime());
					item.put("sharing", isc.getIssharing());
					item.put("remark", isc.getRemark());
					if (null == isc.getReStartdate()) {
						item.put("reStartdate", "");
					} else {
						item.put("reStartdate",
								sdf.format(isc.getReStartdate()));
					}
					if (null == isc.getReEnddate()) {
						item.put("reEnddate", "");
					} else {
						item.put("reEnddate", sdf.format(isc.getReEnddate()));
					}
					item.put("reStarttime", isc.getReStarttime());
					item.put("reEndtime", isc.getReEndtime());
					item.put("reMode", isc.getReMode());
					item.put("reDayInterval", isc.getReDayInterval());
					item.put("reWeekDate", isc.getReWeekDate());
					item.put("reMonthDays", isc.getReMonthDays());
					item.put("reYearMonth", isc.getReYearMonth());
					item.put("reYearDays", isc.getReYearDays());
					item.put("wczt", isc.getWczt());
					item.put("wcqk", isc.getWcqk());
					rows.add(item);
				}
			}
			JSONArray json = JSONArray.fromObject(rows);
			html.append(json);
		}
		return html.toString();
	}

	/**
	 * 获得时间段内指定用户的重复日程(一条拆分成多条进行显示)
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	public String getJsonData_Repeate(Date startdate, Date enddate,
			String userid) {
		List<IworkSchCalendar> list = schCalendarDAO
				.getPeriodList_Repeate(userid);
		int length = list.size();
		StringBuffer html = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;// 将重复事件转一般事件的开始时间
		Date end = null;// 将重复事件转一般事件的结束时间
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (int i = 0; i < length; i++) {
				if (list.get(i) == null) {
					continue;
				}
				IworkSchCalendar isc = (IworkSchCalendar) list.get(i);
				Date Re_startdate = isc.getReStartdate();
				if (Re_startdate.compareTo(startdate) >= 0) {
					start = Re_startdate;
				} else {
					start = startdate;
				}
				Date Re_enddate = isc.getReEnddate();
				if (null == Re_enddate || "".equals(Re_enddate)) {
					end = enddate;
				} else {
					if (Re_enddate.compareTo(enddate) >= 0) {
						end = enddate;
					} else {
						end = Re_enddate;
					}
				}
				if (start.compareTo(end) > 0) {
					continue;
				}// 重复事件的时间段与指定的时间段没有公共区间
				if ("0".equals(isc.getReMode())) {// 按天模式计算
					int interval = Integer.parseInt(isc.getReDayInterval());// '天'模式下的间隔时间
					Calendar cal = Calendar.getInstance();
					cal.setTime(start);
					while (true) {
						Date temp = cal.getTime();
						if (temp.compareTo(end) <= 0) {
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("id", isc.getId());
							item.put("userid", isc.getUserid());
							item.put("title", isc.getTitle());
							if (null == isc.getReStarttime()
									|| null == isc.getReEndtime()
									|| "".equals(isc.getReStarttime())
									|| "".equals(isc.getReEndtime())) {
								item.put("start", sdf.format(temp).toString());
								item.put("end", sdf.format(temp).toString());
							} else {
								item.put(
										"start",
										sdf.format(temp) + " "
												+ isc.getReStarttime());
								item.put(
										"end",
										sdf.format(temp) + " "
												+ isc.getReEndtime());
							}
							Long isAllDayLong = isc.getIsallday();// 是否全天事件
							boolean isAllDay = false;
							if (null == isAllDayLong || 0 == isAllDayLong) {
								isAllDay = false;
							} else {
								isAllDay = true;
							}
							item.put("allDay", isAllDay);
							item.put("alert", isc.getIsalert());
							item.put("alertTime", isc.getAlerttime());
							item.put("sharing", isc.getIssharing());
							item.put("remark", isc.getRemark());
							if (null == isc.getReStartdate()) {
								item.put("reStartdate", "");
							} else {
								item.put("reStartdate",
										sdf.format(isc.getReStartdate()));
							}
							if (null == isc.getReEnddate()) {
								item.put("reEnddate", "");
							} else {
								item.put("reEnddate",
										sdf.format(isc.getReEnddate()));
							}
							item.put("reStarttime", isc.getReStarttime());
							item.put("reEndtime", isc.getReEndtime());
							item.put("reMode", isc.getReMode());
							item.put("reDayInterval", isc.getReDayInterval());
							item.put("reWeekDate", isc.getReWeekDate());
							item.put("reMonthDays", isc.getReMonthDays());
							item.put("reYearMonth", isc.getReYearMonth());
							item.put("reYearDays", isc.getReYearDays());
							item.put("wczt", isc.getWczt());
							item.put("wcqk", isc.getWcqk());
							item.put("editable", false);
							rows.add(item);
						} else {
							break;
						}
						cal.add(Calendar.DAY_OF_MONTH, interval);
					}
				} else if ("1".equals(isc.getReMode())) {// 按周模式计算
					String reWeekDate = isc.getReWeekDate();
					String[] strArray = reWeekDate.split("_");// 周中的哪几天提醒,存到数据库中的数据1对应星期天
					for (int j = 0; j < strArray.length; j++) {
						CalendarUtil cu = new CalendarUtil();
						String strWeek = strArray[j];
						if (null == strWeek || "".equals(strWeek)) {
							strWeek = "1";
						}
						int week = Integer.parseInt(strWeek);
						List<String> weekInfoList = cu.getWeekInfo(start, end,
								week);
						for (String c : weekInfoList) {
							Map<String, Object> item = new HashMap<String, Object>();
							item.put("id", isc.getId());
							item.put("userid", isc.getUserid());
							item.put("title", isc.getTitle());
							if (null == isc.getReStarttime()
									|| null == isc.getReEndtime()
									|| "".equals(isc.getReStarttime())
									|| "".equals(isc.getReEndtime())) {
								item.put("start", c);
								item.put("end", c);
							} else {
								item.put("start",
										c + " " + isc.getReStarttime());
								item.put("end", c + " " + isc.getReEndtime());
							}
							Long isAllDayLong = isc.getIsallday();// 是否全天事件
							boolean isAllDay = false;
							if (null == isAllDayLong || 0 == isAllDayLong) {
								isAllDay = false;
							} else {
								isAllDay = true;
							}
							item.put("allDay", isAllDay);
							item.put("alert", isc.getIsalert());
							item.put("alertTime", isc.getAlerttime());
							item.put("sharing", isc.getIssharing());
							item.put("remark", isc.getRemark());
							if (null == isc.getReStartdate()) {
								item.put("reStartdate", "");
							} else {
								item.put("reStartdate",
										sdf.format(isc.getReStartdate()));
							}
							if (null == isc.getReEnddate()) {
								item.put("reEnddate", "");
							} else {
								item.put("reEnddate",
										sdf.format(isc.getReEnddate()));
							}
							item.put("reStarttime", isc.getReStarttime());
							item.put("reEndtime", isc.getReEndtime());
							item.put("reMode", isc.getReMode());
							item.put("reDayInterval", isc.getReDayInterval());
							item.put("reWeekDate", isc.getReWeekDate());
							item.put("reMonthDays", isc.getReMonthDays());
							item.put("reYearMonth", isc.getReYearMonth());
							item.put("reYearDays", isc.getReYearDays());
							item.put("wczt", isc.getWczt());
							item.put("wcqk", isc.getWcqk());
							item.put("editable", false);
							rows.add(item);
						}
					}
				} else if ("2".equals(isc.getReMode())) {// 按月模式计算
					String dayOfMonth = isc.getReMonthDays();
					if (null == dayOfMonth || "".equals(dayOfMonth)) {
						dayOfMonth = "1";
					}
					int month = Integer.parseInt(dayOfMonth);
					CalendarUtil cu = new CalendarUtil();
					List<String> monthInfoList = cu.getMonthInfo(start, end,
							month);
					for (String c : monthInfoList) {
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("id", isc.getId());
						item.put("userid", isc.getUserid());
						item.put("title", isc.getTitle());
						if (null == isc.getReStarttime()
								|| null == isc.getReEndtime()
								|| "".equals(isc.getReStarttime())
								|| "".equals(isc.getReEndtime())) {
							item.put("start", c);
							item.put("end", c);
						} else {
							item.put("start", c + " " + isc.getReStarttime());
							item.put("end", c + " " + isc.getReEndtime());
						}
						Long isAllDayLong = isc.getIsallday();// 是否全天事件
						boolean isAllDay = false;
						if (null == isAllDayLong || 0 == isAllDayLong) {
							isAllDay = false;
						} else {
							isAllDay = true;
						}
						item.put("allDay", isAllDay);
						item.put("alert", isc.getIsalert());
						item.put("alertTime", isc.getAlerttime());
						item.put("sharing", isc.getIssharing());
						item.put("remark", isc.getRemark());
						if (null == isc.getReStartdate()) {
							item.put("reStartdate", "");
						} else {
							item.put("reStartdate",
									sdf.format(isc.getReStartdate()));
						}
						if (null == isc.getReEnddate()) {
							item.put("reEnddate", "");
						} else {
							item.put("reEnddate",
									sdf.format(isc.getReEnddate()));
						}
						item.put("reStarttime", isc.getReStarttime());
						item.put("reEndtime", isc.getReEndtime());
						item.put("reMode", isc.getReMode());
						item.put("reDayInterval", isc.getReDayInterval());
						item.put("reWeekDate", isc.getReWeekDate());
						item.put("reMonthDays", isc.getReMonthDays());
						item.put("reYearMonth", isc.getReYearMonth());
						item.put("reYearDays", isc.getReYearDays());
						item.put("wczt", isc.getWczt());
						item.put("wcqk", isc.getWcqk());
						item.put("editable", false);
						rows.add(item);
					}
				} else if ("3".equals(isc.getReMode())) {// 按年模式计算
					String monthOfYear = isc.getReYearMonth();
					String dayOfMonth = isc.getReYearDays();
					if (null == monthOfYear || "".equals(monthOfYear)) {
						monthOfYear = "1";
					}
					if (null == dayOfMonth || "".equals(dayOfMonth)) {
						dayOfMonth = "1";
					}
					int month = Integer.parseInt(monthOfYear);
					int day = Integer.parseInt(monthOfYear);
					CalendarUtil cu = new CalendarUtil();
					List<String> yearInfoList = cu.getYearInfo(start, end,
							month, day);
					for (String c : yearInfoList) {
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("id", isc.getId());
						item.put("userid", isc.getUserid());
						item.put("title", isc.getTitle());
						if (null == isc.getReStarttime()
								|| null == isc.getReEndtime()
								|| "".equals(isc.getReStarttime())
								|| "".equals(isc.getReEndtime())) {
							item.put("start", c);
							item.put("end", c);
						} else {
							item.put("start", c + " " + isc.getReStarttime());
							item.put("end", c + " " + isc.getReEndtime());
						}
						Long isAllDayLong = isc.getIsallday();// 是否全天事件
						boolean isAllDay = false;
						if (null == isAllDayLong || 0 == isAllDayLong) {
							isAllDay = false;
						} else {
							isAllDay = true;
						}
						item.put("allDay", isAllDay);
						item.put("alert", isc.getIsalert());
						item.put("alertTime", isc.getAlerttime());
						item.put("sharing", isc.getIssharing());
						item.put("remark", isc.getRemark());
						if (null == isc.getReStartdate()) {
							item.put("reStartdate", "");
						} else {
							item.put("reStartdate",
									sdf.format(isc.getReStartdate()));
						}
						if (null == isc.getReEnddate()) {
							item.put("reEnddate", "");
						} else {
							item.put("reEnddate",
									sdf.format(isc.getReEnddate()));
						}
						item.put("reStarttime", isc.getReStarttime());
						item.put("reEndtime", isc.getReEndtime());
						item.put("reMode", isc.getReMode());
						item.put("reDayInterval", isc.getReDayInterval());
						item.put("reWeekDate", isc.getReWeekDate());
						item.put("reMonthDays", isc.getReMonthDays());
						item.put("reYearMonth", isc.getReYearMonth());
						item.put("reYearDays", isc.getReYearDays());
						item.put("wczt", isc.getWczt());
						item.put("wcqk", isc.getWcqk());
						item.put("editable", false);
						rows.add(item);
					}
				}
			}
		}
		JSONArray json = JSONArray.fromObject(rows);
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 获得短信提醒文本
	 */
	public String  tixingSMS(String title,String starttime){
       return schCalendarDAO.tixingSMS(title, starttime);
		
	}
	
	
	/**
	 * 获取单条数据
	 * 
	 * @param id
	 * @returnString fqr = DBUtil.getString("SELECT USERNAME FROM ORGUSER WHERE USERID='"+userid+"'", "USERNAME");
	 */
	public IworkSchCalendar getModel(String id) {
		long temp = Long.parseLong(id);
		IworkSchCalendar iworkSchCalendar = schCalendarDAO.getBoData(temp);
		return iworkSchCalendar;
	}
	public String getCs(String id) {
		
		long temp = Long.parseLong(id);
		String flag = schCalendarDAO.getFlag(temp);
		return flag;
	}
	/**
	 * 执行更新操作
	 * 
	 * @param iworkSchCalendar
	 * @return
	 */
	public void update(IworkSchCalendar iworkSchCalendar) {
		schCalendarDAO.updateBoData(iworkSchCalendar);
	}

	/**
	 * 执行保存动作
	 * 
	 * @param iworkSchCalendar
	 * @return
	 */
	public void save(IworkSchCalendar iworkSchCalendar) {
		schCalendarDAO.addBoData(iworkSchCalendar);
	}

	/**
	 * 执行删除动作
	 * 
	 * @param iworkSchCalendar
	 * @return
	 */
	public void delete(IworkSchCalendar iworkSchCalendar) {
		schCalendarDAO.deleteBoData(iworkSchCalendar);
	}

	// ======================POJO=========================================
	public SchCalendarDAO getSchCalendarDAO() {
		return schCalendarDAO;
	}

	public void setSchCalendarDAO(SchCalendarDAO schCalendarDAO) {
		this.schCalendarDAO = schCalendarDAO;
	}

	public void setSysPersonDAO(SysPersonDAO sysPersonDAO) {
		this.sysPersonDAO = sysPersonDAO;
	}

	public int getWorkLogRow() {
		return schCalendarDAO.getWorkLogRow();
	}

	public int getTotalListSize(String username, String startDate, String endDate,String depname) {
		if(username!=null&&!"".equals(username)){
			if(username.contains("-")){
				int index=username.indexOf("-");
				username=username.substring(index+1, username.length());
				
			}
			}
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
        String cuid = user.getUserid();
        Long orgrolid = user.getOrgroleid();
        String ismanager = "";
        String departmentname = "";
        int type = -1;
        
        List lables = new ArrayList();
        lables.add("USERID");
        lables.add("DEPARTMENTNAME");
        lables.add("ISMANAGER");
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM (SELECT USERID,DEPARTMENTNAME,ISMANAGER FROM ORGUSER UNION SELECT USERID,DEPARTMENTNAME,ISMANAGER FROM ORGUSERMAP)WHERE USERID=? AND ISMANAGER=1");
        
        Map params = new HashMap();
        params.put(1, cuid);
        
        List<HashMap> userdata = com.iwork.commons.util.DBUtil.getDataList(lables, sb.toString(), params);
        for (int i = 0; i < userdata.size(); i++) {
       	 ismanager = userdata.get(i).get("ISMANAGER").toString();
       	 if(i==0){
       		 departmentname+=("'"+userdata.get(i).get("DEPARTMENTNAME").toString()+"'");
       	 }else{
       		 departmentname+=(",'"+userdata.get(i).get("DEPARTMENTNAME").toString()+"'");
       	 }
        }
        
        boolean flag = false;
        String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='工作总结权限表单'", "UUID");
        List<HashMap> datalist = DemAPI.getInstance().getAllList(uuid, null, null);
        for (HashMap datamap : datalist) {
			if(datamap.get("USERID").toString().equals(cuid)){
				flag = true;
        		break;
			}
		}
        
        if(orgrolid==3l){
        	type=0;
        }else{
        	if(!flag){
        		if(departmentname!=null&&!departmentname.equals("")){
        			type=1;
        		}else{
        			type=2;
        		}
        	}
        }
		return schCalendarDAO.getWorkListSize(username, startDate, endDate, type, departmentname, cuid,depname).size();
	}

	public void doExcelExp(HttpServletResponse response,String username, String depname) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
        String cuid = user.getUserid();
        Long orgrolid = user.getOrgroleid();
        String ismanager = "";
        String departmentname = "";
        int type = -1;
        
        List lables = new ArrayList();
        lables.add("USERID");
        lables.add("DEPARTMENTNAME");
        lables.add("ISMANAGER");
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM (SELECT USERID,DEPARTMENTNAME,ISMANAGER FROM ORGUSER UNION SELECT USERID,DEPARTMENTNAME,ISMANAGER FROM ORGUSERMAP)WHERE USERID=? AND ISMANAGER=1");
        
        Map params = new HashMap();
        params.put(1, cuid);
        
        List<HashMap> userdata = com.iwork.commons.util.DBUtil.getDataList(lables, sb.toString(), params);
        for (int i = 0; i < userdata.size(); i++) {
       	 ismanager = userdata.get(i).get("ISMANAGER").toString();
       	 if(i==0){
       		 departmentname+=("'"+userdata.get(i).get("DEPARTMENTNAME").toString()+"'");
       	 }else{
       		 departmentname+=(",'"+userdata.get(i).get("DEPARTMENTNAME").toString()+"'");
       	 }
        }
        
        boolean flag = false;
        String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='工作总结权限表单'", "UUID");
        List<HashMap> datalist = DemAPI.getInstance().getAllList(uuid, null, null);
        for (HashMap datamap : datalist) {
			if(datamap.get("USERID").toString().equals(cuid)){
				flag = true;
        		break;
			}
		}
        
        if(orgrolid==3l){
        	type=0;
        }else{
        	if(!flag){
        		if(departmentname!=null&&!departmentname.equals("")){
        			type=1;
        		}else{
        			type=2;
        		}
        	}
        }
        
		schCalendarDAO.doExcelExp(response,username, depname, type, departmentname, cuid);
	}
}
