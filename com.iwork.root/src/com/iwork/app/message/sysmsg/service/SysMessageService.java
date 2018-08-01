package com.iwork.app.message.sysmsg.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import com.iwork.app.message.sysmsg.dao.SysMessageDAO;
import com.iwork.app.message.sysmsg.model.SysMessage;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;

public class SysMessageService {
	private SysMessageDAO sysMessageDAO;	
	

	
	
	/**
	 * 获得未读系统消息条目数
	 * @param userId
	 * @return
	 */
	public int getUnreadCount(String userId){
		return sysMessageDAO.getUnReadMsgRow(userId);
	}

	/**
	 * 删除单条消息
	 * @param id
	 */
	public void removeSysMsgById(Long id) {
		SysMessage model = sysMessageDAO.getSysMsgById(id);
		model.setStatus(SysMessage.MSG_STATUS_DELETED);
		sysMessageDAO.updateBoData(model);
	}

	
	/**
	 * 删除指定用户所有消息
	 * @param userid
	 * @return
	 */
	public void removeAllSysMsgOfUserId(String userid) {
		sysMessageDAO.removeAllMsg(userid);
	}

	
	/**
	 * 设置单条已读
	 * @param id
	 * @return
	 */
	public void setReadById(Long id) {
		SysMessage model = sysMessageDAO.getSysMsgById(id);
		model.setStatus(SysMessage.MSG_STATUS_READ_YES);
		sysMessageDAO.updateBoData(model);
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
	
	/**
	 * 设置用户的消息已读
	 * @param userId
	 * @return
	 */
	public void setReadAll(String userid) {
		List<SysMessage> list = sysMessageDAO.getUnReadMsgs(userid);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				SysMessage model = (SysMessage)list.get(i);
				model.setStatus(SysMessage.MSG_STATUS_READ_YES);
				sysMessageDAO.updateBoData(model);
			}
		}
	}
	
	/**
	 * 创建系统消息
	 * 
	 * @param sysMessage
	 * @return
	 */
	public void createSysMessage(SysMessage sysMsg) {
		sysMessageDAO.createSysMessage(sysMsg);
	}
	
	/**
     * 批量Insert消息
     * @param modelArray
     * @throws SQLException
     */
    public void batchInsertMessage (SysMessage[] modelArray) throws SQLException {
    	sysMessageDAO.batchInsertMessage(modelArray);       
    }
    
	
    /**
     * 批量发送消息
     * @param model
     * @param userList
     * @throws SQLException
     */
    public void sendMessageToUserList(SysMessage model, String[] userList) throws SQLException{
    	List list = Arrays.asList(userList);
    	this.sendMessageToUserList(model, list);
    }
    /**
     * 批量发送消息
     * @param model
     * @param userList
     * @throws SQLException
     */
    public void sendMessageToUserList(SysMessage model, List<String> userList) throws SQLException{
    	int listSize = userList.size();
    	SysMessage[] modelArray = new SysMessage[listSize];
    	int count = 0;
    	for(String userid:userList){
    		SysMessage newModel = new SysMessage();
    		newModel.setContent(model.getContent());
    		newModel.setPriority(model.getPriority());
    		newModel.setRcvRange(model.getRcvRange());
    		newModel.setReadDate(model.getReadDate());
    		newModel.setSender(model.getSender());
    		newModel.setStatus(model.getStatus());
    		newModel.setTitle(model.getTitle());
    		newModel.setType(model.getType());
    		newModel.setUrl(model.getUrl());
    		newModel.setUrlTarget(model.getUrlTarget());
    		newModel.setReceiver(userid);
    		newModel.setSendDate(UtilDate.getNowDatetime());
    		modelArray[count] = newModel;
    		count++;
    	}
    	sysMessageDAO.batchInsertMessage(modelArray);
    }
    
    /**
     * 获取当前用户面板
     * @return
     */
    public String getUserPannel(UserContext usercontext) {
        StringBuffer userInfo = new StringBuffer("");
        userInfo.append("<table>");
        userInfo.append("<tr><td>" +
                "&nbsp;&nbsp;<img align='middle' src='iwork_img/sysmsg/ico2.jpg'>&nbsp;</td><td nowrap><font color=#838383 style='font-weight:bold'>帐号:&nbsp;</font>" +
                "</td><td><font color=#838383>" + 
                usercontext._userModel.getUserid() + 
                "</font></td></tr>");
        userInfo.append("<tr><td></td><td></td></tr>");
        userInfo.append("<tr><td>" +
                "&nbsp;&nbsp;<img align='middle' src='iwork_img/sysmsg/ico2.jpg'>&nbsp;</td><td nowrap><font color=#838383 style='font-weight:bold'>姓名:&nbsp;</font>" +
                "</td><td><font color=#838383>" + 
                usercontext._userModel.getUsername() + 
                "</font></td></tr>");
        userInfo.append("<tr><td></td><td></td></tr>");
        userInfo.append("<tr><td>" +
                "&nbsp;&nbsp;<img align='middle' src='iwork_img/sysmsg/ico2.jpg'>&nbsp;</td><td nowrap><font color=#838383 style='font-weight:bold'>部门:&nbsp;</font>" +
                "</td><td><font color=#838383>" + 
                usercontext._deptModel.getDepartmentname() + 
                "</font></td></tr>");
        userInfo.append("<tr><td></td><td></td></tr>");
        userInfo.append("<tr><td></td><td></td></tr>");
        userInfo.append("</table>");
        return userInfo.toString();
    }
	

	/**
	 * 获得当前用户消息列表
	 * @param pageSize
	 * @param pageNow
	 * @param status
	 * @param type
	 * @return
	 */
	public List<SysMessage> getMessageList(int pageSize, int pageNum, String status, String type) {
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		 List<SysMessage> list = sysMessageDAO.getSyMsgList(userId, type, status, pageSize, pageNum);
		 for(SysMessage msg:list){
			 String showTime = UtilDate.getShowTime(msg.getSendDate());
			 msg.setSendDate(showTime);
		 }
  		return list;
	}
	/**
	 * 获得当前用户消息列表
	 * @param pageSize
	 * @param pageNow
	 * @param status
	 * @param type
	 * @return
	 */
	public int getTotalListSize(String status, String type) {
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		return sysMessageDAO.getSyMsgListSize(userId, type, status);
	}
	
	public int getRows(String userid) {
		
		return sysMessageDAO.getUnReadMsgRow(userid);
	}	

	public void setSysMessageDAO(SysMessageDAO sysMessageDAO) {
		this.sysMessageDAO = sysMessageDAO;
	}
	
	public String getPopUpJson(UserContext userContext) {
		return sysMessageDAO.getFirstUnreadMsg(userContext);
	}
	
	public String getGGPopUpJson(UserContext userContext) {
		return sysMessageDAO.getFirstGGUnreadMsg(userContext);
	}

	public SysMessageDAO getSysMessageDAO() {
		return sysMessageDAO;
	}

	

}
