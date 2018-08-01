package com.iwork.plugs.sysletters.action;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.syscalendar.util.SysCalendarUtil;
import com.iwork.plugs.sysletters.constants.SysLettersConstants;
import com.iwork.plugs.sysletters.model.SysLettersContent;
import com.iwork.plugs.sysletters.model.SysLettersDetailInfo;
import com.iwork.plugs.sysletters.service.SysLettersControlsService;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 站内信处理action
 * @author WangJianhui
 *
 */
public class SysLettersControlsAction extends ActionSupport{
	private static Logger logger = Logger.getLogger(SysLettersControlsAction.class);
	private static final long serialVersionUID = 1L;
	private SysLettersDetailInfo syslettersdetailinfo;
	private SysLettersContent sysletterscontent;
	private SysLettersControlsService sysletterscontrolsservice;
	private String currentUserId;
	private List<SysLettersDetailInfo> queryLettersList;
	private String letterTitle;
	private String letterContent;
	private String sentUserId;
	private String sentUserName;
	private String receiveUserId;
	private String receiveUserName;
	private String letterLevel;
	private String sentUserIdsGroup;
	private String toUserId;
	private String toUserName;
	private Long beforeId;
	/**
	 * 进入页面初始化当前人的所有信息
	 * @return
	 */
	public String list(){
		UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
		String userId = me.get_userModel().getUserid();
		queryLettersList = sysletterscontrolsservice.list(userId);
		return SUCCESS;
	}
	
	public String requestReply(){
		return SUCCESS;
	}
	/**
	 * 长连接自动查询回复信息
	 */
	public void getAjax() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String beforeId = request.getParameter("beforeId");
		String receiveUserId = request.getParameter("receiveUserId");
		String letterId = request.getParameter("letterId");
		Long beforeIdL = 0L;
		Long letterIdL = 0L;
		if(beforeId!=null&&!"".equals(beforeId)&&letterId!=null&&!"".equals(letterId)){
			letterIdL = Long.valueOf(letterId);
			beforeIdL = Long.valueOf(beforeId);
		}
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		long startTime = new Date().getTime();
	      // 查询有无数据变化
	      while (true) {
	          try {
	        	// 休眠5秒处理业务等
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			} 
			Long afterId = isExitsLettersReply(letterIdL, receiveUserId, beforeIdL);
	          if (afterId!=null) { 
	              // 返回数据信息，请求时间、返回数据时间、耗时
	              writer.print(afterId);
	              break; // 跳出循环，返回数据
	          } else { //没有数据变化，将休眠 hold住连接
	              try {
	            	//15秒
					Thread.sleep(15000);
					long responseTime = System.currentTimeMillis();
					//开始到结束用了1分钟没有请求到数据,则默认为超时,需从前台重新请求
					//由于前台可能把对话框关掉,所以需要重新请求,判断是否已经关掉,若关掉了无需再查询
					if((responseTime - startTime) >= 60000){
						break;
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
	          }
	      }
	}
	
	
	/**
	 * 查询是否有新回复
	 * @param letterId
	 * @param receiveUserId
	 * @param beforeId
	 * @return
	 */
	public Long isExitsLettersReply(Long letterId,String receiveUserId,Long beforeId){
		Long afterId = sysletterscontrolsservice.isExitsLettersReply(letterId, receiveUserId, beforeId);
		return afterId;
	}
	/**
	 * 查看站内信详细信息
	 * @return
	 */
//	public String lookForLetters(){
//		HttpServletRequest request = ServletActionContext.getRequest();
//		String receiveUserId = request.getParameter("receiveUserId").trim();
//		String letterId = request.getParameter("letterId");
//		String flag = request.getParameter("flag");
//		Long letterIdL = 0L;
//		if(letterId!=null){
//			letterIdL = Long.valueOf(letterId);
//		}
//		//更改已读或未读状态
//		if("0".equals(flag)){
//			flag = "1";
//			sysletterscontrolsservice.changeLetterFlag( letterIdL, receiveUserId, receiveUserId, flag);
//		}
//		sysletterscontent = sysletterscontrolsservice.getLettersContent(letterIdL,receiveUserId);
//		return SUCCESS;
//	}
	
	public String lookForLettersContent(){
		HttpServletRequest request = ServletActionContext.getRequest();
		UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
		String receiveUserIdT = me.get_userModel().getUserid();
		String letterId = request.getParameter("letterId");
		String changeType = request.getParameter("changeType");//当为1的时候,证明是从站内信详细信息内的“收取回复”按钮发起的,执行changeLetterFlag方法来变更为已读,
		Long letterIdL = 0L;
		if(letterId!=null){
			letterIdL = Long.valueOf(letterId);
		}
		if(changeType!=null&&!"".equals(changeType)&&"1".equals(changeType)){
			sysletterscontrolsservice.changeLetterFlag( letterIdL, receiveUserIdT, receiveUserIdT, SysLettersConstants.CHECKSTATUS_TURE);
		}
		String flag = request.getParameter("flag");
		if("0".equals(flag)){
			flag = "1";
			sysletterscontrolsservice.changeLetterFlag( letterIdL, receiveUserIdT, receiveUserIdT, flag);
		}
		sysletterscontent = sysletterscontrolsservice.getLettersContent(letterIdL,receiveUserIdT);
		if(!me.get_userModel().getUserid().equals(sysletterscontent.getCreateUserId())){
			toUserId = sysletterscontent.getCreateUserId();
			toUserName = sysletterscontent.getCreateUserName();
		}else{
			toUserId = sysletterscontent.getToUserIds();
			toUserName = UserContextUtil.getInstance().getUserContext(toUserId).get_userModel().getUsername();
		}
		receiveUserId = receiveUserIdT;
		beforeId = sysletterscontrolsservice.getLastLettersReply(letterIdL,receiveUserIdT);
		return SUCCESS;
	}
	
	/**
	 * 查询回复信息
	 * @return
	 */
	public String lookForLettersReply(){
		HttpServletRequest request = ServletActionContext.getRequest();
		UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
		String receiveUserId = me.get_userModel().getUserid();
		String letterId = request.getParameter("letterId");
		Long letterIdL = 0L;
		if(letterId!=null){
			letterIdL = Long.valueOf(letterId);
		}
		List<SysLettersDetailInfo> list = new ArrayList<SysLettersDetailInfo>();
		list = sysletterscontrolsservice.getLettersReply(letterIdL,receiveUserId);
		Collections.sort(list,new Comparator<SysLettersDetailInfo>(){  
            public int compare(SysLettersDetailInfo arg0, SysLettersDetailInfo arg1) {  
                return arg1.getTs().compareTo(arg0.getTs());  //降序
            }  
        }); 
		queryLettersList  = list;
		//beforeId = queryLettersList.get(0).getId();
		return SUCCESS;
	}
	/**
	 * 打开发送页面
	 * @return
	 */
	public String openSentLetterPage(){
		sentUserId = UserContextUtil.getInstance().getCurrentUserId();
		return SUCCESS;
	}
	
	/**
	 * 更改已读或未读状态
	 */
	
	public void changeLetterFlag(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String msg = "false";
		String letterId = request.getParameter("letterId");
		String receiveUserId = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		String flag = request.getParameter("flag");
		int count = 0;
		String[] letterIds = letterId.split(",");
		//count = sysletterscontrolsservice.changeLetterFlag( letterIds, receiveUserId, receiveUserId, flag);
		count = sysletterscontrolsservice.changeLetterMoreFlag(letterIds, receiveUserId, receiveUserId, flag) ; 
		if(count>0){
			msg = "succ";
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 发送站内信
	 * @return
	 */
	public void createLetter(){
		UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
		String sentUserid = me.get_userModel().getUserid();
		String sentUsername = me.get_userModel().getUsername();
		SysCalendarUtil util = new SysCalendarUtil();
		SysLettersContent sysletterscontent = new SysLettersContent();
		SysLettersDetailInfo syslettersdetailinfo = new SysLettersDetailInfo();
		sysletterscontent.setCreateUserId(sentUserid);
		sysletterscontent.setCreateUserName(sentUsername);
		sysletterscontent.setLetterContent(letterContent);
		sysletterscontent.setLetterLevel(letterLevel);
		sysletterscontent.setLetterTitle(letterTitle);
		sysletterscontent.setLetterDate(new Date());
		sysletterscontent.setTs(util.getTimeStamp());
		sysletterscontent.setToUserIds(receiveUserId);
		syslettersdetailinfo.setCheckStatus(SysLettersConstants.CHECKSTATUS_FALSE);
		syslettersdetailinfo.setCreateUserId(sentUserid);
		syslettersdetailinfo.setCreateUserName(sentUsername);
		syslettersdetailinfo.setSentUserId(sentUserid);
		syslettersdetailinfo.setSentUserName(sentUsername);
		syslettersdetailinfo.setLetterDate(new Date());
		syslettersdetailinfo.setReceiveUserId(receiveUserId);
		syslettersdetailinfo.setReceiveUserName(receiveUserName);
		syslettersdetailinfo.setTs(util.getTimeStamp());
		sysletterscontrolsservice.createLetter(syslettersdetailinfo, sysletterscontent);
		ResponseUtil.write("OK");
		// return "OK";
	}
	/**
	 * 用于一对一聊天
	 * @throws UnsupportedEncodingException
	 */
	public void createLetterReply() throws UnsupportedEncodingException{
		String msg = "succ";
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
		SysCalendarUtil util = new SysCalendarUtil();
		String sentUserid = me.get_userModel().getUserid();
		String sentUsername = me.get_userModel().getUsername();
		SysLettersDetailInfo syslettersdetailinfo = new SysLettersDetailInfo();
		syslettersdetailinfo.setCheckStatus(SysLettersConstants.CHECKSTATUS_FALSE);//给自己发的时候要置为0
		String createUserId = request.getParameter("createUserId");
		String letterId = request.getParameter("letterId");
		String content = request.getParameter("content");
		Long letterIdL = 0L;
		if(letterId!=null){
			letterIdL = Long.valueOf(letterId);
		}
		String receivePersonsIds = request.getParameter("receivePersonsIds");
		String receivePersonsNames = request.getParameter("receivePersonsNames");
		UserContext me_c =  UserContextUtil.getInstance().getUserContext(createUserId);
		String createUserName = me_c.get_userModel().getUsername();
		syslettersdetailinfo.setLetterId(letterIdL);
		syslettersdetailinfo.setCreateUserId(createUserId);
		syslettersdetailinfo.setCreateUserName(createUserName);
		syslettersdetailinfo.setSentUserId(sentUserid);
		syslettersdetailinfo.setSentUserName(sentUsername);
		syslettersdetailinfo.setLetterDate(new Date());
		syslettersdetailinfo.setTs(util.getTimeStamp());
		sysletterscontrolsservice.createLetterReply_single(syslettersdetailinfo, receivePersonsIds, receivePersonsNames,content);
		ResponseUtil.write(msg);
	}
	
	
	/**
	 * 回复站内信用于多人
	 * @throws UnsupportedEncodingException 
	 */
	
	public void createLetterReply_mult() throws UnsupportedEncodingException{
		String msg = "succ";
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
		SysCalendarUtil util = new SysCalendarUtil();
		String sentUserid = me.get_userModel().getUserid();
		String sentUsername = me.get_userModel().getUsername();
		SysLettersDetailInfo syslettersdetailinfo = new SysLettersDetailInfo();
		syslettersdetailinfo.setCheckStatus(SysLettersConstants.CHECKSTATUS_FALSE);//给自己发的时候要置为0
		String createUserId = request.getParameter("createUserId");
		String letterId = request.getParameter("letterId");
		String content = request.getParameter("content");
		Long letterIdL = 0L;
		if(letterId!=null){
			letterIdL = Long.valueOf(letterId);
		}
		String receivePersonsIds = request.getParameter("receivePersonsIds");
		String receivePersonsNames = request.getParameter("receivePersonsNames");
		String[] recePersIds = receivePersonsIds.split(",");
		String[] recePersNames = receivePersonsNames.split(",");
		UserContext me_c =  UserContextUtil.getInstance().getUserContext(createUserId);
		String createUserName = me_c.get_userModel().getUsername();
		syslettersdetailinfo.setLetterId(letterIdL);
		syslettersdetailinfo.setCreateUserId(createUserId);
		syslettersdetailinfo.setCreateUserName(createUserName);
		syslettersdetailinfo.setSentUserId(sentUserid);
		syslettersdetailinfo.setSentUserName(sentUsername);
		syslettersdetailinfo.setLetterDate(new Date());
		syslettersdetailinfo.setTs(util.getTimeStamp());
		sysletterscontrolsservice.createLetterReply(syslettersdetailinfo, recePersIds, recePersNames,content);
		ResponseUtil.write(msg);
	}
	/**
	 * 删除站内信详细回复
	 */
	public void delLetterReply() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		Long idL = 0L;
		String msg = "false";
		int count = 0;
		if(id!=null&&!"".equals(id)){
			idL = Long.valueOf(id);
			count = sysletterscontrolsservice.delLetterReply( idL);
		}
		if(count > 0){
			msg = "succ";
		}else{
			msg = "false";
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 删除站内信列表中的主要信息
	 */
	public void delLettersList(){
		String msg = "false";
		HttpServletRequest request = ServletActionContext.getRequest();
		String letterIds = request.getParameter("letterIds");
		UserContext me =  UserContextUtil.getInstance().getCurrentUserContext();
		String userId = me.get_userModel().getUserid();
		int count = sysletterscontrolsservice.delLettersList(letterIds, userId);
		if(count>0){
			msg = "succ";
		}
		ResponseUtil.write(msg);
	}
	public SysLettersControlsService getSysletterscontrolsservice() {
		return sysletterscontrolsservice;
	}


	public void setSysletterscontrolsservice(
			SysLettersControlsService sysletterscontrolsservice) {
		this.sysletterscontrolsservice = sysletterscontrolsservice;
	}


	public SysLettersDetailInfo getSyslettersdetailinfo() {
		return syslettersdetailinfo;
	}
	public void setSyslettersdetailinfo(SysLettersDetailInfo syslettersdetailinfo) {
		this.syslettersdetailinfo = syslettersdetailinfo;
	}
	public SysLettersContent getSysletterscontent() {
		return sysletterscontent;
	}
	public void setSysletterscontent(SysLettersContent sysletterscontent) {
		this.sysletterscontent = sysletterscontent;
	}


	public String getCurrentUserId() {
		return currentUserId;
	}


	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}


	public List<SysLettersDetailInfo> getQueryLettersList() {
		return queryLettersList;
	}


	public void setQueryLettersList(List<SysLettersDetailInfo> queryLettersList) {
		this.queryLettersList = queryLettersList;
	}


	public String getLetterTitle() {
		return letterTitle;
	}


	public void setLetterTitle(String letterTitle) {
		this.letterTitle = letterTitle;
	}


	public String getLetterContent() {
		return letterContent;
	}


	public void setLetterContent(String letterContent) {
		this.letterContent = letterContent;
	}


	public String getSentUserId() {
		return sentUserId;
	}


	public void setSentUserId(String sentUserId) {
		this.sentUserId = sentUserId;
	}


	public String getSentUserName() {
		return sentUserName;
	}


	public void setSentUserName(String sentUserName) {
		this.sentUserName = sentUserName;
	}


	public String getReceiveUserId() {
		return receiveUserId;
	}


	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}


	public String getReceiveUserName() {
		return receiveUserName;
	}


	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}


	public String getLetterLevel() {
		return letterLevel;
	}


	public void setLetterLevel(String letterLevel) {
		this.letterLevel = letterLevel;
	}

	public String getSentUserIdsGroup() {
		return sentUserIdsGroup;
	}

	public void setSentUserIdsGroup(String sentUserIdsGroup) {
		this.sentUserIdsGroup = sentUserIdsGroup;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public Long getBeforeId() {
		return beforeId;
	}

	public void setBeforeId(Long beforeId) {
		this.beforeId = beforeId;
	}

	
	
}
