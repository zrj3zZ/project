package com.ibpmsoft.project.zqb.service;

import java.io.IOException;
import java.text.SimpleDateFormat; 
import java.util.Date;  
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;  
import javax.websocket.OnError;  
import javax.websocket.OnMessage;  
import javax.websocket.OnOpen;  
import javax.websocket.Session;  
import javax.websocket.server.ServerEndpoint;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.onchat.service.OnlineChatService;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;
/** 
 * 聊天服务器类 
 * @author shiyanlou 
 * 
 */  
@ServerEndpoint(value="/websocket",configurator=GetHttpSessionConfigurator.class)
public class ChatServer {  
	private static Logger logger = Logger.getLogger(ChatServer.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");    // 日期格式化
    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ChatServer> connections = new CopyOnWriteArraySet<ChatServer>();
    private OnlineChatService onlineChatService;
    private String nickname;
    private Session session;
    private String onlyChat;
    private HttpSession httpSession;
    private UserContext uc;
    
    public ChatServer() {
        nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
    }
    
    @OnOpen
    public void open(Session session,EndpointConfig config) {
        // 添加初始化操作
    	this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties()
                .get(HttpSession.class.getName());
    	uc=(UserContext) httpSession.getAttribute("USER_CONTEXT");
    	this.nickname=uc._userModel.getUserid() + "[" + uc._userModel.getUsername() + "]";
    	connections.add(this);
        HashMap map = new HashMap();
        StringBuffer sb=new StringBuffer();
        for (ChatServer client : connections) {
        	sb.append(client.nickname+",");
        }
        map.put("onlineName",sb.toString());
        JSONObject jsonObject = JSONObject.fromObject(map);
		try {
			this.session.getBasicRemote().sendText(jsonObject.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
    }
  
     
    /** 接受客户端的消息，并把消息发送给所有连接的会话 
	@param message 客户端发来的消息 
	@param session 客户端的会话 
     */
     
    @OnMessage  
	public void getMessage(String msg,Session session){
    	// 把客户端的消息解析为JSON对象
        JSONObject jsonObject = JSONObject.fromObject(msg);
        String onlyChat = (String) jsonObject.get("onlyChat");
        String chatRecordName = (String) jsonObject.get("chatRecordName").toString().replace("\"","");
        // 在消息中添加发送日期
        jsonObject.put("date", DATE_FORMAT.format(new Date()));
        StringBuffer sb=new StringBuffer();
        for (ChatServer client : connections) {
        	sb.append(client.nickname+",");
        }
        HashMap hashmap = new HashMap();
		hashmap.put("USERNAME", this.nickname);
		hashmap.put("SENDNAME", onlyChat.contains("所有人")?"all":onlyChat);
		hashmap.put("CONTENT", jsonObject.get("content").toString());
		String nowDatetime = UtilDate.getNowDatetime();
		hashmap.put("DATATIME", nowDatetime);
		hashmap.put("CHATRECORDNAME", chatRecordName);
		onlineChatService = (OnlineChatService)SpringBeanUtil.getBean("onlineChatService");
        onlineChatService.addOnlineChat(hashmap);
        jsonObject.put("onlineName", sb.toString());
        StringBuffer sql=new StringBuffer();
        String result ="";
        if(!"".equals(onlyChat)&&onlyChat!=null){
        	if(onlyChat.contains("证券")){
        		String name = onlyChat.substring(onlyChat.lastIndexOf("[")+1, onlyChat.length()-1);
        		sql.append("select userid||'['||username||']' username from orguser where username='"+name+"' and orgroleid!=3");
        	}else{
        		String name = onlyChat.substring(onlyChat.lastIndexOf("[")+1, onlyChat.length()-1);
        		sql.append("select userid||'['||username||']' username from orguser where username='"+name+"' and orgroleid=3");
        	}
        	result = DBUtil.getString(sql.toString(), "username");
        }
        // 把消息发送给所有连接的会话
        for (ChatServer client : connections) {
            try {
                synchronized (client) {
                	if(!"".equals(onlyChat)&&onlyChat!=null&&!onlyChat.contains("所有人")){
                		if(result.equalsIgnoreCase(client.nickname)){
                			jsonObject.put("isSelf", this.nickname.equalsIgnoreCase(client.nickname));
                			client.session.getBasicRemote().sendText(jsonObject.toString());
                		}else if(this.nickname.equalsIgnoreCase(client.nickname)){
                			jsonObject.put("isSelf", this.nickname.equalsIgnoreCase(client.nickname));
                			client.session.getBasicRemote().sendText(jsonObject.toString());
                		}
                	}else{
                		jsonObject.put("isSelf", this.nickname.equalsIgnoreCase(client.nickname));
            			client.session.getBasicRemote().sendText(jsonObject.toString());
                	}
                }
            } catch (Exception e) {
                logger.error(e,e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (Exception e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                /*broadcast(message);*/
            }
        }
	}
    
	@OnClose
    public void close(){//添加关闭会话时的操作
		connections.remove(this);
	}
	@OnError
	public void error(Throwable t) {
		// 添加处理错误的操作  
	
	}
	
	private static void broadcast(String msg) {
        for (ChatServer client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (Exception e) {
               logger.error(e,e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (Exception e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                broadcast(message);
            }
        }
    }
}
