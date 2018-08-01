package com.iwork.plugs.sms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.plugs.sms.bean.MsgMst;
import com.iwork.plugs.sms.dao.PrivateMsgDao;
import org.apache.log4j.Logger;
public class PrivateMsgService {
	private static Logger logger = Logger.getLogger(PrivateMsgService.class);
private PrivateMsgDao privateMsgDao;
public void setPrivateMsgDao(PrivateMsgDao PrivateMsgDao){
	this.privateMsgDao=PrivateMsgDao;
}
public String queryPMsg(int pageSize,int startRow,String mobilenum,String keywords,String status,String begintime,String endtime,String batch){
	return privateMsgDao.queryPMsg(pageSize,startRow,mobilenum,keywords,status,begintime,endtime,batch);
}
/**
 * 分页
 * @param userid
 * @return
 */
public int getRows() {
	// TODO Auto-generated method stub
	return privateMsgDao.getRows();
}	
/**
 * 分页2
 * @param userid
 * @return
 */
public int getRows2(String mobilenum,String keywords,String status,String begintime,String endtime,String batch) {
	// TODO Auto-generated method stub
	return privateMsgDao.getRows2(mobilenum,keywords,status,begintime,endtime,batch);
}	
/**
 * 查询短信列表（jquery）
 * @return
 */
public String getMsgTreeJson(int pageSize,int startRow,String mobilenum,String keywords,String status,String begintime,String endtime,String batch){
	StringBuffer jsonHtml = new StringBuffer();
	List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
	try{
		List list = privateMsgDao.getMsgNums(pageSize,startRow,mobilenum,keywords,status,begintime,endtime,batch);
		for(int i = 0;i<list.size();i++){
			MsgMst pbook = (MsgMst)list.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				//item.put("id", pbook.getCid());
				String statuss="";
				if (!String.valueOf(pbook.getStatus()).equals("")) {	    	         
					statuss = privateMsgDao.getStatusdb(String.valueOf(pbook.getStatus()));	    	          
	    	        }
				String t1=pbook.getSubmittime().toString();
				String t2=pbook.getSendtime().toString();
				if(!t1.equals("")){
					t1=t1.substring(0,19);
				}
				if(!t2.equals("")){
					t2=t2.substring(0,19);
				}
				item.put("batch", pbook.getBatchnum());		
				item.put("submitt",t1);
				item.put("sendt",t2 );
				item.put("phone",pbook.getMobilenum());
				item.put("content", pbook.getContent());
				item.put("status", statuss);
				items.add(item);			
		}
	}catch(Exception e){
		logger.error(e,e);
	}		
	JSONArray json = JSONArray.fromObject(items);
	jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
	return jsonHtml.toString();
}
public String getprimsgList(ArrayList list){
	StringBuffer sb = new StringBuffer();
    int a = list.size();
    int i=1;
    if (list != null && list.size() > 0) {
      Iterator it = list.iterator();
      while (it.hasNext()) {
        Hashtable ht = (Hashtable) it.next();
       // String id = ht.get("ID").toString();
        sb.append("<tr>\n");
        sb.append("<td class ='actionsoftReportData' align='center'>" + i
            + "</td>");
        sb.append(
            "<td align='center' nowrap>"
                + ht.get("BATCHNUM1").toString())
            .append("<br>");
        sb.append(ht.get("BATCHNUM2").toString()).append(
            "</td>\n");
        sb.append(
            "<td align='center' nowrap>"
                + ht.get("SUBMITTIME1").toString()).append(
            "<br>");
        sb.append(ht.get("SUBMITTIME2").toString()).append(
            "</td>\n");
        sb.append(
            "<td align='center' nowrap>"
                + ht.get("SENDTIME1").toString())
            .append("<br>");
        sb.append(ht.get("SENDTIME2").toString()).append(
            "</td>\n");
        sb.append("<td align='center' nowrap>"
            + ht.get("MOBILENUM").toString() + "</td>\n");
        sb.append("<td align='left'>"
            + ht.get("CONTENT").toString() + "</td>\n");
        if ((ht.get("STATUS").toString()).equals("成功")
            || (ht.get("STATUS").toString()).equals("已改成功")) {
          sb.append("<td align='center' nowrap>"
              + ht.get("STATUS").toString() + "</td>\n");
        } else if ((ht.get("STATUS").toString()).equals("失败")
            || (ht.get("STATUS").toString()).equals("其他失败")
            || (ht.get("STATUS").toString()).equals("已改失败")) {
          sb.append("<td align='center'><font color='#FF0000'>"
              + ht.get("STATUS").toString() + "</font></td>\n");
        } else {
          sb.append("<td align='center'><font color='#0099FF'>"
              + ht.get("STATUS").toString() + "</font></td>\n");
        }
        sb.append("</tr>\n");
        i++;
      }
    
    }
    return sb.toString();
  }
/**
 * 查询
 * @param userid
 * @return
 */
public String queryuserdata1(String userid){
	return privateMsgDao.queryuserdata1(userid);
}
public String querystatuses(){
	return privateMsgDao.querystatuses();
}
public String querystatuses2(){
	return privateMsgDao.querystatuses2();
}
public String querystatuses3(String status){
	return privateMsgDao.querystatuses3(status);
}
/**
 * 数据库取短信状态
 * @param list
 * @return
 */
public String getstatuslist(ArrayList list) {
	StringBuffer sb = new StringBuffer();
	if (list != null && list.size() > 0) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Hashtable hs = (Hashtable) it.next();
			sb.append("<option value='").append(
					hs.get("KEY").toString()).append("'>").append(
					hs.get("VALUE").toString()).append("</option>");
		}
	}
	return sb.toString();
}
/**
 * 数据库取短信状态
 * @param list
 * @return
 */
public String getstatuslist2(ArrayList list) {
	StringBuffer sb = new StringBuffer();
	sb.append("<select name='status' id='status' ><option value='' selected>全部</option>");//<option value='' selected>全部</option>  
	if (list != null && list.size() > 0) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Hashtable hs = (Hashtable) it.next();
			sb.append("<option value='").append(
					hs.get("KEY").toString()).append("'>").append(
					hs.get("VALUE").toString()).append("</option>");
		}
	}
	sb.append("</select>");
	return sb.toString();
}
/**
 * 数据库取短信状态
 * @param list
 * @return
 */
public String getstatuslist3(ArrayList list,String statuss) {
	StringBuffer sb = new StringBuffer();
	sb.append("<select name='status' id='status' >");//<option value='' selected>全部</option>  
	if(statuss.equals("")){
		sb.append("<option value='' selected>全部</option>");
	}else{
		sb.append("<option value='' >全部</option>");
	}
	if (list != null && list.size() > 0) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Hashtable hs = (Hashtable) it.next();
			String keyy=hs.get("KEY").toString();
			if(keyy.equals(statuss)){
				sb.append("<option value='").append(
						hs.get("KEY").toString()).append("' selected>").append(
						hs.get("VALUE").toString()).append("</option>");
			}else{
			sb.append("<option value='").append(
					hs.get("KEY").toString()).append("'>").append(
					hs.get("VALUE").toString()).append("</option>");
			}
		}
	}
	sb.append("</select>");
	return sb.toString();
}
}
