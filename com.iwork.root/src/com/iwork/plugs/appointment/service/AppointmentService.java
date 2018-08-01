package com.iwork.plugs.appointment.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import com.iwork.app.persion.dao.SysPersonDAO;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.appointment.dao.AppointmentDAO;
import com.iwork.plugs.appointment.model.Appointment;

public class AppointmentService {
	private AppointmentDAO appointmentDAO;
	private SysPersonDAO sysPersonDAO;

	public List<Map> getWorkLogList(int pageSize, int pageNow, String username,
			String startDate, String endDate) {
		return appointmentDAO.getWorkLogList(pageSize, pageNow, username,
				startDate, endDate);
	}

	/**
	 * 获得当前用户
	 * 
	 * @return
	 */
	public String getOtherUserJson() {
		StringBuffer jsonHtml = new StringBuffer();
		List<String> list = appointmentDAO.getConfigTypeUsers();

		HashMap hash = new HashMap();
		hash.put("id", "001");
		hash.put("name", "共享日志的用户");
		hash.put("icon", "iwork_img/logohome.gif");
		hash.put("open", true);
		List<Map> items = new ArrayList<Map>();
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

	/**
	 * 获得时间段内指定用户的一般日程
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	public String getJsonData(Date startdate, Date enddate, String userid) {
		List<Appointment> list = appointmentDAO.getPeriodList(startdate,
				enddate, userid);
		int length = list.size();
		StringBuffer html = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (int i = 0; i < length; i++) {
				if (list.get(i) == null)
					continue;
				Appointment isc = (Appointment) list.get(i);
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", isc.getId());
				item.put("userid", isc.getSzr().toString());
				item.put("title", isc.getSfjy()==null?"":isc.getSfjy()==1?"休息":"工作");
				item.put("sfjy", isc.getSfjy());
				item.put("start", isc.getJyr()==null?"":isc.getJyr().toString());
				item.put("end", isc.getJyr()==null?"":isc.getJyr().toString());
				item.put("jyr", isc.getJyr()==null?"":isc.getJyr().toString());
				Long isAllDayLong = (long)1;// 是否全天事件
				boolean isAllDay = false;
				if (null == isAllDayLong || 0 == isAllDayLong) {
					isAllDay = false;
				} else {
					isAllDay = true;
				}
				item.put("allDay", String.valueOf(isAllDay));
				item.put("alert", null);
				item.put("alertTime", null);
				item.put("sharing", null);
				item.put("remark", null);
				item.put("reStartdate", "");
				item.put("reEnddate", "");
				item.put("reStarttime", null);
				item.put("reEndtime", null);
				item.put("reMode", null);
				item.put("reDayInterval", null);
				item.put("reWeekDate", null);
				item.put("reMonthDays", null);
				item.put("reYearMonth", null);
				item.put("reYearDays", null);
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
		List<Appointment> list = appointmentDAO
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
				Appointment isc = (Appointment) list.get(i);
				Date cur = new Date();
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
		List<Appointment> list = appointmentDAO
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
       return appointmentDAO.tixingSMS(title, starttime);
		
	}
	
	
	/**
	 * 获取单条数据
	 * 
	 * @param id
	 * @return
	 */
	public Appointment getModel(String id) {
		long temp = Long.parseLong(id);
		Appointment appointment = appointmentDAO.getBoData(temp);
		return appointment;
	}

	/**
	 * 执行更新操作
	 * 
	 * @param iworkSchCalendar
	 * @return
	 */
	public void update(Appointment appointment) {
		appointmentDAO.updateBoData(appointment);
	}

	/**
	 * 执行保存动作
	 * 
	 * @param iworkSchCalendar
	 * @return
	 */
	public void save(Appointment appointment) {
		appointmentDAO.addBoData(appointment);
	}

	/**
	 * 执行删除动作
	 * 
	 * @param iworkSchCalendar
	 * @return
	 */
	public void delete(Appointment appointment) {
		appointmentDAO.deleteBoData(appointment);
	}
	
	public Appointment getBoData(long id) {
		Appointment boData = appointmentDAO.getBoData(id);
		return boData;
	}

	// ======================POJO=========================================

	public void setSysPersonDAO(SysPersonDAO sysPersonDAO) {
		this.sysPersonDAO = sysPersonDAO;
	}

	public AppointmentDAO getAppointmentDAO() {
		return appointmentDAO;
	}

	public void setAppointmentDAO(AppointmentDAO appointmentDAO) {
		this.appointmentDAO = appointmentDAO;
	}

	public int getWorkLogRow() {
		return appointmentDAO.getWorkLogRow();
	}

	public int getTotalListSize(String username, String startDate,
			String endDate) {
		return appointmentDAO.getWorkListSize(username, startDate, endDate);
	}

	public void doExcelExp(HttpServletResponse response) {
		appointmentDAO.doExcelExp(response);
	}
}
