package com.iwork.plugs.sms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.dao.LogDao;
import com.iwork.plugs.sms.util.MsgConst;
import org.apache.log4j.Logger;

public class LogService {
	private static Logger logger = Logger.getLogger(LogService.class);
	private LogDao logDao;

	public void setLogDao(LogDao LogDao) {
		this.logDao = LogDao;
	}
	/**
	 * 分页
	 * @param userid
	 * @return
	 */
	public int getRows() {
		// TODO Auto-generated method stub
		return logDao.getRows();
	}	
	/**
	 * 分页
	 * @param userid
	 * @return
	 */
	public int getRows2(String sender, String keywords,String  begintime, String endtime, List type_list) {
		// TODO Auto-generated method stub
		return logDao.getRows2(sender, keywords, begintime, endtime,type_list);
	}	
	/**
	 * 日志加入到数据库
	 * 
	 * @param mm
	 */
	public void adddb(LogMst lm) {
		logDao.adddb(lm);
	}
	/**
	 * 查询日志列表（jquery）
	 * @return
	 */
	public String getLogJson(int pageSize,int startRow,String sender, String keywords, String begintime,
			String endtime, String logtype){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			ArrayList type_list = new ArrayList();
			splitType(logtype, type_list);
			List list = logDao.getLogj(pageSize,startRow,sender, keywords, begintime, endtime, type_list);
			for(int i = 0;i<list.size();i++){
				LogMst pbook = (LogMst)list.get(i);	
					Map<String,Object> item = new HashMap<String,Object>();
					//item.put("id", pbook.getCid());
					String logtypeid=String.valueOf(pbook.getLogtype())==null?"":String.valueOf(pbook.getLogtype());
					String logtypec="";
					if(!logtypeid.equals("")) {
						String sql1 = " from ConfigMst where type='LOG_TYPE' and key=?";
						List<Object> params = new ArrayList<Object>();
						params.add(logtypeid);
						logtypec =logDao. gettypev(sql1,params);
					}
					item.put("type",logtypec );		
					item.put("time",pbook.getLogtime().toString());
					item.put("operator", pbook.getUserid());
					item.put("value",pbook.getValue());
					items.add(item);			
			}
		}catch(Exception e){
			logger.error(e,e);
		}		
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	/**
	 * 查询日志类型
	 * 
	 * @return
	 */
	public String getTypeCheckbox() {

		StringBuffer ret = new StringBuffer();
		ret
				.append("<input type='checkbox' id='type_all' name='type_all' checked value='LOG_TYPE_ALL' />全部");
		int i = 1;
		while (MsgConst.LOG_TYPE_HASHMAP.containsValue(i)) {
			String sql1 = "from ConfigMst where type='LOG_TYPE' and key=?";
			List<Object> params = new ArrayList<Object>();
			params.add(i);
			String typevalue = logDao.gettypev(sql1,params);
			ret.append("&nbsp;<input type='checkbox' id='type" + i
					+ "' name='type'" + i + " value='LOG_TYPE" + i + "' />"
					+ typevalue);
			i++;
		}
		i = 0;
		String sql1 = " from ConfigMst where type='LOG_TYPE' and key=?";
		List<Object> params1 = new ArrayList<Object>();
		params1.add(i);
		String typevalue = logDao.gettypev(sql1,params1);
		ret.append("&nbsp;<input type='checkbox' id='type" + i
				+ "' name='type'" + i + " value='LOG_TYPE" + i + "' />"
				+ typevalue);

		return ret.toString();
	}
	/**
	 * 查询日志类型2
	 * 
	 * @return
	 */
	public String getTypeCheckbox(ArrayList type_list) {

		StringBuffer ret = new StringBuffer();
		if(type_list.size() > 0){						
					ret.append("<input type='checkbox' id='type_all' name='type_all'  value='LOG_TYPE_ALL' />全部");
				}
				else {
				    ret.append("<input type='checkbox' id='type_all' name='type_all' checked value='LOG_TYPE_ALL' />全部");
				}
			
		
		int i = 1;
		while (MsgConst.LOG_TYPE_HASHMAP.containsValue(i)) {
			String sql1 = "from ConfigMst where type='LOG_TYPE' and key=?";
			List<Object> params = new ArrayList<Object>();
			params.add(i);
			String typevalue = logDao.gettypev(sql1,params);
			String typecheced="LOG_TYPE" + i;
			if(type_list.size()>0){
			for(int j= 0; j < type_list.size(); j++) {
				String typename2="LOG_TYPE"+type_list.get(j);
				if(typename2.equals(typecheced)) {
					ret.append("&nbsp;<input type='checkbox' id='type" + i
							+ "' name='type'" + i + "  checked value='LOG_TYPE" + i + "' />"
							+ typevalue);
				 }
				else {
					ret.append("&nbsp;<input type='checkbox' id='type" + i
							+ "' name='type'" + i + "   value='LOG_TYPE" + i + "' />"
							+ typevalue);
				    }
			 }
			 }else{
				 ret.append("&nbsp;<input type='checkbox' id='type" + i
							+ "' name='type'" + i + "   value='LOG_TYPE" + i + "' />"
							+ typevalue);				 
			 }	
			i++;
		}
		i = 0;
		String sql1 = " from ConfigMst where type='LOG_TYPE' and key=?";
		List<Object> params = new ArrayList<Object>();
		params.add(i);
		String typevalue = logDao.gettypev(sql1,params);
		ret.append("&nbsp;<input type='checkbox' id='type" + i
				+ "' name='type'" + i + " value='LOG_TYPE" + i + "' />"
				+ typevalue);

		return ret.toString();
	}
	/**
	 * 查询日志类型
	 * 
	 * @return
	 */
	public String getTypeCheckbox2() {

		StringBuffer ret = new StringBuffer();
		ret
				.append("<input type='checkbox' id='type_all' name='type_all' checked value='LOG_TYPE_ALL' />全部");
		int i = 1;
		while (MsgConst.LOG_TYPE_HASHMAP.containsValue(i)) {
			String sql1 = "from ConfigMst where type='LOG_TYPE' and key=?";
			List<Object> params = new ArrayList<Object>();
			params.add(i);
			String typevalue = logDao.gettypev(sql1,params);
			ret.append("&nbsp;<input type='checkbox' id='type" + i
					+ "' name='type'" + i + " value='LOG_TYPE" + i + "' />"
					+ typevalue);
			i++;
		}
		i = 0;
		String sql1 = " from ConfigMst where type='LOG_TYPE' and key=?";
		List<Object> params = new ArrayList<Object>();
		params.add(i);
		String typevalue = logDao.gettypev(sql1,params);
		ret.append("&nbsp;<input type='checkbox' id='type" + i
				+ "' name='type'" + i + " value='LOG_TYPE" + i + "' />"
				+ typevalue);

		return ret.toString();
	}
/**
 * 查询日志
 * @param sender
 * @param keywords
 * @param begintime
 * @param endtime
 * @param logtype
 * @return
 */
	public String getLog(String sender, String keywords, String begintime,
			String endtime, String logtype) {
		ArrayList type_list = new ArrayList();
		splitType(logtype, type_list);
		
		return logDao.getLog(sender, keywords, begintime, endtime, type_list);
	}
	public void splitType(String s, ArrayList list) {
		String types[] = s.split(" ");
		for(int i = 0; i < types.length; i++) {
			if(types[i].length() > 0) {
				//全部
				if(types[i].equals("LOG_TYPE_ALL")) {
					list.clear();
					return;
				}
				
				if(MsgConst.LOG_TYPE_HASHMAP.containsKey(types[i])) {
					int type = MsgConst.LOG_TYPE_HASHMAP.get(types[i]);
					list.add(type);
				}
			}
		}
	}
	
	/**
	 *日志查询列表显示
	 * @param list
	 * @return
	 */
	public String getlogList(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			int i=1;
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				sb.append("<tr>\n");
				sb.append("<td class ='actionsoftReportData'  nowrap align='center'>" + i
            + "</td>");
        sb.append(
            "<td align='center'  nowrap>"
                + ht.get("checktype").toString()).append("</td>\n");
        sb.append(
            "<td align='center'  nowrap>"
                + ht.get("logtime").toString()).append("</td>\n");
        sb.append(
            "<td align='center'  nowrap>"
                + ht.get("userid").toString()).append("</td>\n");
        sb.append("<td align='left'>"
            + ht.get("value").toString() + "</td>\n");
				i++;
			}
			
		}
		return sb.toString();
	}
		
}
