package com.iwork.app.weixin.process.action.qy.util;

import java.util.HashMap;


import java.util.Map;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.sdk.ProcessAPI;

import net.sf.json.JSONObject;

/** 
 * @author: py
 * @version:2016年12月31日 上午8:48:29 
 * com.kp.qy.util.TestSendMes.java
 * @Desc 
 */
public class TestSendMes {
//  发送消息  
public static String SEND_MSG_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";  
      
/*public  void sendMsg(String userid,String fqrname,Long instanceId,String flag) {  
	System.out.println(userid);
    //应用ID，账号，部门为""，标签为""，消息类型，内容  
    //部门不为""，标签不为""，将会给该部门的每个成员发送消息  
    Send_msg("zhouruijin","","1","text",2,fqrname+"您有一个新的待办流程，请尽快处理");  
  
}  */
public void sendMsgList(String[] list,String fqrname,Long instanceId,String flag ,String title){
	try {
		Map params=new HashMap();
		
		String userid="";
		String content="";
		if(list!=null){
		for (int i = 0; i < list.length; i++) {
			params.put(1,list[i]);
			if(i==(list.length-1)){
				userid=userid+DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid=?", params);
			}else{
				userid=userid+DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid=?", params)+"|";
			}
		}
		}else{
			 HashMap fromData = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			 Object userId = fromData.get("CREATEUSERID");
			 Object userd = fromData.get("TBRID");
			 Object userdgid = fromData.get("EXTEND4");
			 Object usertxid = fromData.get("QCRID");
			 
			 if(userId!=null&&!"".equals(userId)){
				 HashMap map = new HashMap();
				 map.put(1, userId.toString());
				 userid=userid+DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid=?", map);
			 }else if(userd!=null&&!"".equals(userd)){
				 HashMap map = new HashMap();
				 map.put(1, userd.toString());
				 userid=userid+DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid=?", map);
			 }else if(userdgid!=null&&!"".equals(userdgid)){
				 HashMap map = new HashMap();
				 map.put(1, userdgid.toString());
				 userid=userid+DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid=?", map);
			 }else if(usertxid!=null&&!"".equals(usertxid)){
				 HashMap map = new HashMap();
				 map.put(1, usertxid.toString());
				 userid=userid+DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid=?", map);
			 }
			 
		}
		String tablename = DBUTilNew.getDataStr("ENTITYNAME", "select ENTITYNAME from sys_engine_metadata where id=(select metadataid from sys_engine_form_bind where instanceid='"+instanceId+"')", null);
		String dataid = DBUTilNew.getDataStr("DATAID", "select DATAID from sys_engine_form_bind S where instanceid='"+instanceId+"'", null);
		Map param=new HashMap();
		param.put(1,dataid);
		if(tablename.equals("BD_XP_QTGGZLLC")){
			String sql="SELECT NOTICENAME FROM "+tablename+" WHERE ID= ? ";
			title=DBUTilNew.getDataStr("NOTICENAME", sql, param);
			content=fqrname+flag+"了"+title+"的审核,"+"请注意查看";
		}else if(tablename.equals("RCYWCB")){
			title=DBUTilNew.getDataStr("SXMC", "SELECT SXMC FROM "+tablename+" WHERE ID= ? ", param);
			content=fqrname+flag+"了"+title+"的审核,"+"请注意查看";
		}else{
			content=fqrname+flag+"了"+title+","+"请注意查看";
		}

		Send_msg(userid,"","1","text",Integer.parseInt(SystemConfig._weixinConf.getAgentid()),content);
	} catch (NumberFormatException e) {
		
	}  
}
public void sendTzggMsgList(HashMap user,String fqrname,String tzbt){
	try {
		Map params=new HashMap();
		params.put(1,user.get("USERID"));
		String userid=DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid=? ", params);
		String content=fqrname+"给你发送了"+tzbt+"通知公告，请查看。";
		Send_msg(userid,"","1","text",Integer.parseInt(SystemConfig._weixinConf.getAgentid()),content);
	} catch (NumberFormatException e) {
		
	}  
}
public void sendDcwjMsgList(String uid,String dxnr){
	try {
		Map params=new HashMap();
		params.put(1,uid);
		String userid=DBUTilNew.getDataStr("weixin_code", " select weixin_code from orguser  where userid= ? ", params);
		Send_msg(userid,"","1","text",Integer.parseInt(SystemConfig._weixinConf.getAgentid()),dxnr);
	} catch (NumberFormatException e) {
		
	}  
}
/**
 * @date 2016年12月31日上午10:05:07
 * @param touser 成员ID列表
 * @param toparty 部门ID列表
 * @param totag 标签ID列表
 * @param msgtype 消息类型，此时固定为：text （支持消息型应用跟主页型应用）
 * @param agentid 企业应用的id，整型。可在应用的设置页面查看
 * @param content 消息内容，最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
 * @return int 表示是否是保密消息，0表示否，1表示是，默认0
 * @Des: 主动发送文字给企业用户 
 */
	public static int Send_msg(String  touser,String toparty,String totag,String msgtype,int agentid,String content){  
	    int errCode=0;  
	    if(SystemConfig._weixinConf.getServer().equals("on")){
	    	 //拼接请求地址  
		    String requestUrl=SEND_MSG_URL.replace("ACCESS_TOKEN",  WechatAccessToken.getAccessToken(Constants.CORPID, Constants.SECRET,1).getToken());  
		    //需要提交的数据  
		//    String postJson = "{\"agentid\":\"%s\",\"touser\":\"%s\",\"toparty\":\"[1,%s]\",\"totag\":\"%s\",\"msgtype\":\"text\",\"%s\":{\"content\":\"%s\"},\"safe\":\"0\"}";      
		//    String postJson = "{\"agentid\":\"%s\",\"touser\":\"%s\",\"toparty\":\"%s\",\"totag\":\"%s\",\"msgtype\":\"text\",\"%s\":{\"content\":\"%s\"},\"safe\":\"0\"}";      
		    String postJson = "{\"agentid\":%s,\"touser\": \"%s\",\"toparty\": \"%s\",\"totag\": \"%s\","+
		     "\"msgtype\":\"%s\",\"text\": {\"content\": \"%s\"},\"safe\":0}";
		    String outputStr=String.format(postJson,agentid,touser,toparty,totag,msgtype,content);  
		    //创建成员  
		    JSONObject jsonObject=HttpRequestUtil.httpRequest(requestUrl, "POST", outputStr);  
		   
		}
	    return errCode;  
	}  
}
