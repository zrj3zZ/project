package com.iwork.app.mobile.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.calender.service.SchCalendarService;

/**
 * 将服务器日程同步到移动端
 * @author chenM
 *
 */
public class SyncCalendarAction {
	private String sessionId;
	private SchCalendarService schCalendarService;
	/**
	 * 返回移动端日程的JSON
	 * @return
	 */
	public String getMobileSchCalendar() throws Exception{
			UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
			if(null!=userContext){
				String userid = userContext.get_userModel().getUserid();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String endTemp = "2099-12-31";
				Date endDate = sdf.parse(endTemp);
				Date startDate = new Date();
				if(null!=userid){
					String noRepeateData = schCalendarService.getJsonData(startDate, endDate, userid);
					String repeatData = schCalendarService.getJsonData_Repeate_Single(startDate, endDate, userid);
					List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("noRepeateData",noRepeateData);
					item.put("repeatData",repeatData);
					rows.add(item);
					JSONArray json = JSONArray.fromObject(rows);
					StringBuffer html = new StringBuffer();
					String jsonData = html.append(json).toString();
					jsonData = jsonData.replace("null", "\"\"");
					ResponseUtil.writeTextUTF8(jsonData);
				}else{
					ResponseUtil.writeTextUTF8("");
				}
			}else{
				ResponseUtil.writeTextUTF8("");
			}
		return null;
	}
//==========================POJO================================
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public SchCalendarService getSchCalendarService() {
		return schCalendarService;
	}
	public void setSchCalendarService(SchCalendarService schCalendarService) {
		this.schCalendarService = schCalendarService;
	}
}
