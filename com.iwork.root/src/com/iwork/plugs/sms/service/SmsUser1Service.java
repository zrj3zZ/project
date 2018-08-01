package com.iwork.plugs.sms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.plugs.sms.bean.LimitMst;
import com.iwork.plugs.sms.dao.SmsUser1Dao;
import org.apache.log4j.Logger;
public class SmsUser1Service {
	private SmsUser1Dao smsUser1Dao;
	private static Logger logger = Logger.getLogger(SmsUser1Service.class);
	public void setSmsUser1Dao(SmsUser1Dao SmsUser1Dao) {
		this.smsUser1Dao = SmsUser1Dao;
	}
	/**
	 * 查询用户列表（jquery）
	 * @return
	 */
	public String getSmsUserJson(String operator,String begintime,String endtime){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			List list = smsUser1Dao.getUserNums(operator,begintime,endtime);
			for(int i = 0;i<list.size();i++){
				LimitMst pbook = (LimitMst)list.get(i);	
					Map<String,Object> item = new HashMap<String,Object>();
					//item.put("id", pbook.getCid());
					int cid=pbook.getCid();
					String operate="<a  href='###' onclick=\"dataEdit('" + cid
					+ "')\">设置</a>" ;
					String time=pbook.getSubmittime().toString();
					if(!time.equals("")){
						time=time.substring(0,10);
					}
					item.put("sender", pbook.getUserid());		
					item.put("time",time);
					item.put("operate", operate);
					items.add(item);			
			}
		}catch(Exception e){
			logger.error(e,e);
		}		
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	public void savechange(String cid,String type,String count){
		smsUser1Dao.savec(cid,type,count);
		
	}
}
