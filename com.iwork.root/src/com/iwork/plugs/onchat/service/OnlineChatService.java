package com.iwork.plugs.onchat.service;

import java.util.HashMap;
import com.iwork.commons.util.UtilDate;
import com.iwork.plugs.onchat.dao.OnlineChatDAO;
import com.iwork.plugs.onchat.model.OnlineChat;

public class OnlineChatService {
	private OnlineChatDAO onlineChatDAO;
	
	public void addOnlineChat(HashMap hashmap) {
			OnlineChat oc=new OnlineChat();
			oc.setChatrecordname(hashmap.get("CHATRECORDNAME").toString());
			oc.setContent(hashmap.get("CONTENT").toString());
			oc.setDatatime(UtilDate.StringToDate(hashmap.get("DATATIME").toString(),"yyyy-MM-dd HH:mm:ss"));
			oc.setSendname(hashmap.get("SENDNAME").toString());
			oc.setUsername(hashmap.get("USERNAME").toString());
			onlineChatDAO.addBoData(oc);
	}

	public OnlineChatDAO getOnlineChatDAO() {
		return onlineChatDAO;
	}

	public void setOnlineChatDAO(OnlineChatDAO onlineChatDAO) {
		this.onlineChatDAO = onlineChatDAO;
	}
}
