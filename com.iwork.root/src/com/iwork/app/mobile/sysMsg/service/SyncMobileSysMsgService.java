package com.iwork.app.mobile.sysMsg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.message.sysmsg.dao.SysMessageDAO;
import com.iwork.app.message.sysmsg.model.SysMessage;
import com.iwork.app.message.sysmsg.service.SysMessageService;
import com.iwork.app.mobile.process.management.service.SyncMobileProcessManagementService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import org.apache.log4j.Logger;
public class SyncMobileSysMsgService {
	private static Logger logger = Logger.getLogger(SyncMobileSysMsgService.class);

	private final String CMD_READ_ONE = "READ_ONE";
	private final String CMD_DEL_ONE = "DEL_ONE";
	private final String CMD_READ_ALL = "READ_ALL";
	private final String CMD_DEL_ALL = "DEL_ALL";
	private SysMessageDAO sysMessageDAO;
	private SysMessageService sysMessageService;

	public String getMobileSysMsgJson(String curPageNo,
			String pageSize, Long id, String cmd) {
		UserContext userContext = UserContextUtil.getInstance()
				.getCurrentUserContext();
		if (null == userContext) {
			return null;
		} else {
			Map<String, Object> total = new HashMap<String, Object>();
			StringBuffer jsonHtml = new StringBuffer();
			//没有CMD的值就是阅读系统消息
			if (null == cmd || "".equals(cmd)) {
				if(null==curPageNo||"".equals(curPageNo)||null==pageSize||"".equals(pageSize)){
					return null;
				}else{
					String userId = userContext.get_userModel().getUserid();
					StringBuffer sql = new StringBuffer("FROM "
							+ SysMessage.DATABASE_ENTITY + " where receiver='");
					sql.append(userId)
					.append("'")
					.append(" and status <> '"
							+ SysMessage.MSG_STATUS_DELETED + "' ");
					sql.append(" ORDER BY  senddate desc");
					try {
						List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
						@SuppressWarnings("unchecked")
						List<SysMessage> list = sysMessageDAO.getMsgPageList(
								userId, Integer.parseInt(pageSize),
								Integer.parseInt(curPageNo), sql.toString());
						for (SysMessage sysMessage : list) {
							Map<String, Object> item = new HashMap<String, Object>();
							if (null == sysMessage) {
								continue;
							}
							if (null != sysMessage.getId()) {
								item.put("id", sysMessage.getId());
							} else {
								item.put("id", "");
							}
							item.put("priority", sysMessage.getPriority());
							item.put("type", sysMessage.getType());
							if (null != sysMessage.getTitle()) {
								item.put("title", sysMessage.getTitle());
							} else {
								item.put("title", "");
							}
							if (null != sysMessage.getContent()) {
								item.put("content", sysMessage.getContent());
							} else {
								item.put("content", "");
							}
							if (null != sysMessage.getReceiver()) {
								item.put("receiver", sysMessage.getReceiver());
							} else {
								item.put("receiver", "");
							}
							if (null != sysMessage.getUrl()) {
								item.put("url", sysMessage.getUrl());
							} else {
								item.put("url", "");
							}
							item.put("status", sysMessage.getStatus());
							if (null != sysMessage.getSendDate()) {
								item.put("sendDate", SyncMobileProcessManagementService.getSimpleTime(sysMessage.getSendDate()));
							} else {
								item.put("sendDate", "");
							}
							if (null != sysMessage.getReadDate()) {
								item.put("readDate", sysMessage.getReadDate());
							} else {
								item.put("readDate", "");
							}
							if (null != sysMessage.getSender()) {
								item.put("sender", sysMessage.getSender());
							} else {
								item.put("sender", "");
							}
							if (null != sysMessage.getRcvRange()) {
								item.put("rcvRange", sysMessage.getRcvRange());
							} else {
								item.put("rcvRange", "");
							}
							items.add(item);
						}
						total.put("dataRows", items);
						JSONArray json = JSONArray.fromObject(total);
						jsonHtml.append(json);
						return jsonHtml.toString();
					} catch (Exception e) {logger.error(e,e);
						return null;
					}
				}
			} else if (cmd.equals(this.CMD_READ_ONE)) {
				if(null==id||"".equals(id)){
					total.put("operateResult", "fail");
					total.put("cmd", this.CMD_READ_ONE);
				}else{
					sysMessageService.setReadById(id);
					total.put("operateResult", "success");
					total.put("cmd", this.CMD_READ_ONE);
				}
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			} else if (cmd.equals(this.CMD_READ_ALL)) {
				String userid = userContext.get_userModel().getUserid();
				if(null!=userid){
					sysMessageService.setReadAll(userid);
					total.put("operateResult", "success");
					total.put("cmd", this.CMD_READ_ALL);
				}else{
					total.put("operateResult", "fail");
					total.put("cmd", this.CMD_READ_ALL);
				}
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			} else if (cmd.equals(this.CMD_DEL_ONE)) {
				if(null==id||"".equals(id)){
					total.put("operateResult", "fail");
					total.put("cmd", this.CMD_DEL_ONE);
				}else{
					sysMessageService.removeSysMsgById(id);
					total.put("operateResult", "success");
					total.put("cmd", this.CMD_DEL_ONE);
				}
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			} else if (cmd.equals(this.CMD_DEL_ALL)) {
				String userid = userContext.get_userModel().getUserid();
				if(null!=userid){
					sysMessageService.removeAllSysMsgOfUserId(userid);
					total.put("operateResult", "success");
					total.put("cmd", this.CMD_DEL_ALL);
				}else{
					total.put("operateResult", "fail");
					total.put("cmd", this.CMD_DEL_ALL);
				}
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			} else{
				return null;
			}
		}
	}
	/**
	 * 设置状态为“已读”
	 * @param id
	 * @return
	 */
	public void setMsgStatus(Long id,String userid){
		if(id!=null){
			SysMessage model = sysMessageDAO.getSysMsgById(id);
			model.setStatus(SysMessage.MSG_STATUS_READ_YES);
			sysMessageDAO.updateBoData(model); 
		}else{
			sysMessageDAO.setAllRead(userid);
		}
		
	}
	public SysMessageDAO getSysMessageDAO() {
		return sysMessageDAO;
	}

	public void setSysMessageDAO(SysMessageDAO sysMessageDAO) {
		this.sysMessageDAO = sysMessageDAO;
	}

	public SysMessageService getSysMessageService() {
		return sysMessageService;
	}

	public void setSysMessageService(SysMessageService sysMessageService) {
		this.sysMessageService = sysMessageService;
	}
}
