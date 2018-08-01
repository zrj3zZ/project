	package com.iwork.plugs.email.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.sf.json.JSONArray;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.weixin.core.tools.SendMessageUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.UtilDate;
import com.iwork.commons.util.UtilString;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.HtmlRegexpUtil;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.constant.MailStatusConst;
import com.iwork.plugs.email.constant.MailTaskStatusConst;
import com.iwork.plugs.email.dao.IWorkMailDAO;
import com.iwork.plugs.email.dao.IWorkMailDelDAO;
import com.iwork.plugs.email.dao.IWorkMailOwnerDAO;
import com.iwork.plugs.email.dao.IWorkMailTaskDAO;
import com.iwork.plugs.email.fullsearch.SearchIndexUtil;
import com.iwork.plugs.email.model.MailDelModel;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.sdk.FileUploadAPI;

public class RecIWorkMailClientService {

	private IWorkMailDAO iWorkMailDAO;
	public IWorkMailTaskDAO iWorkMailTaskDAO;
	private IWorkMailOwnerDAO iWorkMailOwnerDAO;
    private IWorkMailDelDAO iWorkMailDelDAO;

	/**
	 * 获取总行数
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public int getMsgLogRow(String userId) {
		return iWorkMailTaskDAO.getInstanceOfBoxRows(userId);
	}
	
	
	
	/**
	 * 获得正文头
	 * @param type
	 * @param model
	 * @return
	 */
	public String getContentHtmlHead(Long type,MailModel model){
		StringBuffer html = new StringBuffer();
		html.append("<div style=\"font-size:16px;\"></div><br/><br/><br/><br/><br/><br/><div style=\"line-height:30px;font-size:12px;\"><p>--------------------------------").append("原始邮件").append("------------------------------------------------------------------------------------------------------------------------------------------</p></div>");
		html.append("<div style=\"background-color:#efefef;\">");
		html.append("<table >");
		html.append("<tr>"); 
		html.append("<td style=\"font-family:微软雅黑;padding-left:20px;text-align:right;font-weight:bold;line-height:30px;\">").append("　发件人：").append("</td>").append("<td style=\"font-size:12px;font-family:微软雅黑;\">").append(model._mailFrom).append("</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td style=\"font-family:微软雅黑;padding-left:20px;text-align:right;font-weight:bold;width:150px;line-height:30px;\">").append("　发送时间：").append("</td>").append("<td style=\"font-size:12px;font-family:微软雅黑;\">").append(model._createDate).append("</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td style=\"font-family:微软雅黑;text-align:right;font-weight:bold;width:150px;line-height:30px;\">").append("　收件人：").append("</td>").append("<td style=\"font-size:12px;font-family:微软雅黑;\">").append(model.get_to()).append("</td>");
		html.append("</tr>");
		if(model.get_cc()!=null){
			html.append("<tr>");
			html.append("<td style=\"font-family:微软雅黑;text-align:right;font-weight:bold;width:150px;line-height:30px;\">").append("　抄送人：").append("</td>").append("<td style=\"font-size:12px;font-family:微软雅黑;\">").append(model.get_cc()).append("</td>");
			html.append("</tr>");
		}
		html.append("<tr>");
		html.append("<td style=\"font-family:微软雅黑;text-align:right;font-weight:bold;width:150px;line-height:30px;\">").append("　主题：").append("</td>").append("<td style=\"font-size:12px;font-family:微软雅黑;\">").append(model.get_title()).append("</td>");
		html.append("</tr>");
		html.append("</table>");
		html.append("</div>");
		html.append("<div class=\"mailContent\">");
		html.append(model.get_content()); 
		html.append("</div>");
		return html.toString();
		
	}
	/**
	 * 获得正文头
	 * @param type
	 * @param model
	 * @return
	 */
	public String getAttachHtml(MailModel model){
		StringBuffer fieldHtml = new StringBuffer();
		if(model!=null&&model.get_attachments()!=null){
			String uuids = model.get_attachments();
			String[] arr = uuids.split(",");
			for(int i=0;i<arr.length;i++){
				FileUpload fileUpload = FileUploadAPI.getInstance().getFileUpload(arr[i]);
				if(fileUpload!=null){
					String fileDivId = fileUpload.getFileId();
					fieldHtml.append("<span  id=\"").append(fileDivId).append("\"  class=\"attachItem\">\n");
					String icon = "iwork_img/attach.png";
					if(fileUpload.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fileUpload.getFileSrcName());
					}
					fieldHtml.append("<span><a href=\"iwork_email_download.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a></span>\n");
					fieldHtml.append("<a href=\"javascript:forwardReomve('").append(fileDivId).append("');\"><img src=\"/iwork_img/del3.gif\"/></a>\n");
					fieldHtml.append("</span>\n");
				}
			} 
			
		}
		return fieldHtml.toString();
		
	}
	
	/**
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param currLogPage
	 * @return
	 */
//	public List<MailTaskModel> getReceiveList(String userId,int pageSize, int currLogPage){
//		int startRow = 0;
//		if(currLogPage>1){
//			startRow = pageSize*currLogPage;
//		}
//		return iWorkMailTaskDAO.getInstanceOfBoxRows(userId);
//	}
	
	
	/**
	 * 发送邮件
	 * @param model 
	 * @return
	 */
	public String sendEmail(MailModel model,Long taskid){
 
		if(model!=null){
			HashMap innerAccount = new HashMap();
			UserContext sender = UserContextUtil.getInstance().getCurrentUserContext();
			if(model.get_createUser()==null||"".equals(model.get_createUser())){
				model._createUser = sender.get_userModel().getUserid();
				model._createDate = Calendar.getInstance().getTime();
				model._mailFrom = UserContextUtil.getInstance().getCurrentUserFullName();
			}
			String returnCode = ""; 
			
			if (taskid!=null) {

				model.set_relatedId(taskid);
			} else {
				model.set_relatedId(new Long(0));

			}
			model.set_isSend(BoxTypeConst.IS_READ_YES);
			model.set_isImportant(BoxTypeConst.IS_IMPORTANT_NO);
			boolean flag = iWorkMailDAO.save(model); 
			if(!flag)return MailStatusConst.DB_CONNECT_ERROR;
			
			//获取账户列表
			String mail_to = model.get_to();
			returnCode=getAddressList(innerAccount,model._to,"TO");
			if(!returnCode.equals(MailStatusConst.OK)){
				// 删除已添加数据
				return returnCode;
			}
			//获取邮件抄送
			model._cc = model._cc.trim();
			returnCode=getAddressList(innerAccount,model._cc,"CC");
			if(!returnCode.equals(MailStatusConst.OK)){
				return returnCode;
			}
			//获取密送
//			model._bcc = model._bcc.trim();
//			returnCode=getAddressList(innerAccount,model._bcc,"BCC");
//			if(!returnCode.equals(MailStatusConst.OK)){
//				return returnCode;
//			}
			//添加发件箱
			MailOwnerModel mom = new MailOwnerModel();
			mom.setTitle(model._title);
			mom.setIsArchives(new Long(1));
			mom.setBindId(model._id);
			mom.setMailTo(model._to);
			mom.setIsDel(new Long(0));
			mom.setIsImportant(model._isImportant);
			mom.setCreateTime(Calendar.getInstance().getTime());
			mom.setOwner(sender._userModel.getUserid());
			mom.setIsRe(new Long(0));
			mom.setIsImportant(new Long(0));
			mom.setIsStar(new Long(0));
			mom.setMailBox(BoxTypeConst.TYPE_SEND);
			Long ownerId = iWorkMailOwnerDAO.save(mom);
			//if(ownerId==null)return MailStatusConst.DB_CONNECT_ERROR;
			if(innerAccount!=null && innerAccount.size()>0){
				for (int i = 0; i < innerAccount.size(); i++) {
					String mailAccount=(String)innerAccount.get(new Integer(i));
					OrgUser ou = UserContextUtil.getInstance().getOrgUserInfo(mailAccount);
					if(ou==null){
						continue;
					}
					
					//收件箱
					MailTaskModel sendTaskModel = new MailTaskModel();
				       
					sendTaskModel.owner = ou.getUserid();
					sendTaskModel.mailFrom = model._mailFrom;
					sendTaskModel.mailTo = model.get_to();
					sendTaskModel.bindId = model._id;
					if(model._attachments == null || model._attachments.equals("")){
						sendTaskModel.isArchives = new Long(1);
					} 
					sendTaskModel.isImportant = model._isImportant;
					sendTaskModel.createTime=Calendar.getInstance().getTime();
					sendTaskModel.mailBox = BoxTypeConst.TYPE_TRANSACT;
					sendTaskModel.title = model._title;
					sendTaskModel.setIsDel(new Long(0));
					sendTaskModel.setIsImportant(model._isImportant);
					sendTaskModel.setIsRead(new Long(0));
					Long s = iWorkMailTaskDAO.save(sendTaskModel);
					if (s ==null) {
						return MailStatusConst.SYSTEM_ERROR;
					}else{
						//微信提醒
						if(SystemConfig._weixinConf.getServer().equals("on")){
							StringBuffer content = new StringBuffer();
							content.append("【邮件提醒】\n");
							content.append("您收到了一封来自[").append(model._mailFrom).append("]的标题为【").append(model._title).append("】内部邮件,请及时处理");
							SendMessageUtil.getInstance().sendTextMessage(ou.getUserid(), content.toString());
						}
					}
					//添加索引
					SearchIndexUtil.getInstance().addDocIndex(sendTaskModel.getId(), ou.getUserid(), BoxTypeConst.TYPE_TRANSACT, model);
				}
				
			} 
			return MailStatusConst.OK;
		}
		return "";
	}
	/**
	 * 转发邮件
	 * @param model 
	 * @return
	 */
	public String resendEmail(MailTaskModel model,Long taskid){

		if(model!=null){
			HashMap innerAccount = new HashMap();
			UserContext sender = UserContextUtil.getInstance().getCurrentUserContext();
			if(model.getOwner()==null||"".equals(model.getOwner())){
				model.owner = sender.get_userModel().getUserid();
				model.createTime = Calendar.getInstance().getTime();
				model.mailFrom= UserContextUtil.getInstance().getCurrentUserFullName();
			}
			String returnCode = ""; 
			//
			Long flag = iWorkMailTaskDAO.save(model); 
			if(flag!=0)return MailStatusConst.DB_CONNECT_ERROR;
			
			//获取账户列表
			String mail_to = model.getMailTo();
			returnCode=getAddressList(innerAccount,model.mailTo,"TO");
			if(!returnCode.equals(MailStatusConst.OK)){
				// 删除已添加数据
				return returnCode;
			}
			//获取邮件抄送
//			model. = model._cc.trim();
//			returnCode=getAddressList(innerAccount,model._cc,"CC");
//			if(!returnCode.equals(MailStatusConst.OK)){
//				return returnCode;
//			}
			//获取密送
//			model._bcc = model._bcc.trim();
//			returnCode=getAddressList(innerAccount,model._bcc,"BCC");
//			if(!returnCode.equals(MailStatusConst.OK)){
//				return returnCode;
//			}
			//添加发件箱
//			MailOwnerModel mom = new MailOwnerModel();
//			mom.setTitle(model._title);
//			mom.setBindId(model._id);
//
//			mom.setMailTo(model._to);
//			mom.setIsDel(new Long(0));
//			mom.setIsImportant(model._isImportant);
//			mom.setCreateTime(Calendar.getInstance().getTime());
//			mom.setOwner(sender._userModel.getUserid());
//			mom.setMailBox(BoxTypeConst.TYPE_SEND);
//			Long ownerId = iWorkMailOwnerDAO.save(mom);
//			//if(ownerId==null)return MailStatusConst.DB_CONNECT_ERROR;
			if(innerAccount!=null && innerAccount.size()>0){
				for (int i = 0; i < innerAccount.size(); i++) {
					String mailAccount=(String)innerAccount.get(new Integer(i));
					OrgUser ou = UserContextUtil.getInstance().getOrgUserInfo(mailAccount);
					if(ou==null){
						continue;
					}
					
					//邮件信息表
					MailModel Model = new MailModel();
					Model.set_createUser(ou.getUserid());
					Model.set_mailFrom( model.mailFrom);
					Model.set_to(model.mailTo);
					Model.set_relatedId(taskid);
					Model.set_isImportant(model.isImportant);
					Model.set_createDate(Calendar.getInstance().getTime());
					Model.set_mailType(BoxTypeConst.TYPE_TRANSACT);
					Model.set_title(model.title);
					Model.set_isImportant(model.isImportant);
					Model.set_relatedId(taskid);
					boolean s = iWorkMailDAO.save(Model);
					if (s ==false) {
						return MailStatusConst.SYSTEM_ERROR;
					}
				}
			}
			return MailStatusConst.OK;
		}
		return "";
	}
	
	
	/**
	 * 分页获取当前用户发件箱邮件总行数
	 * 
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public int countSendEail(String userId) {
		return iWorkMailTaskDAO.countReceiveEail(userId);
	}
	
	/**
	 * 获取当前用户收件箱中未读邮件总数
	 * 
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public int countRecUnReadEmail(String userId) {
		return iWorkMailTaskDAO.countRecUnReadEmail(userId);
	}
	
	/**
	 * 获取当前用户收件箱中未读邮件总数
	 * 
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public String getRecUnReadEmail(String userId,int pageSize,int startRow) {
		StringBuffer jsonHtml = new StringBuffer();
		HashMap<String,Object> jsonMap = new HashMap<String,Object>();
		List<HashMap> items = new ArrayList<HashMap>();
		int mailNum = this.countRecUnReadEmail(userId);
		List<MailTaskModel> recList = iWorkMailTaskDAO.getRecUnReadEmail(userId,pageSize, startRow);
		if(recList!=null && recList.size()>0){
			for(MailTaskModel recModel:recList){
				HashMap<String,Object> item = new HashMap<String,Object>();
				item.put("ID", recModel.getBindId());
				item.put("TASKID", recModel.getId());
				item.put("TITLE", recModel.getTitle());
				item.put("EMAILTYPE", recModel.getMailBox());
				item.put("MAILFROM", UserContextUtil.getInstance().getUserName(recModel.getMailFrom()));
				item.put("CREATETIME", UtilDate.datetimeFormat(recModel.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				items.add(item);
			}
		}
		
		jsonMap.put("MAILNUM", mailNum);
		jsonMap.put("DATAROWS", items);
		JSONArray json = JSONArray.fromObject(jsonMap);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
		/**
		 *分页获取当前用户发件箱邮件
		 * 
		 * @param boxType
		 * @param userId
		 * @param pageSize
		 * @param currLogPage
		 * @return
		 */
		public List<MailTaskModel> getReceiveListEmails(String userId, int pageSize,
				int startRow) {

			return iWorkMailTaskDAO.getReceiveListEmails(userId, pageSize, startRow);
		}
		
	
	 /**
	  * 获得邮件显示列表
	  * @param userId
	  * @param pageSize
	  * @param startRow
	  * @return
	  */
     public String getReceiveListHtml(String userId, int pageSize,int startRow){
    	 List<MailTaskModel> list = iWorkMailTaskDAO.getReceiveListEmails(userId, pageSize, startRow);
    	 StringBuffer html = new StringBuffer();
    	 if(list!=null&&list.size()>0){
    		 for(MailTaskModel model:list){
    			 String star ="";
    			 StringBuffer icon = new StringBuffer();
    			 String isread = "";
    			 String content = "";
    			 String sender = "";
    			 String isImportant = "";
    			 if(model.getIsStar()!=null&&model.getIsStar().equals(new Long(1))){
    				 star = " <img id=\"bflag\" onclick=\"javascript:clickCancerStar('"+model.getId()+"');\" src=\"iwork_img/star_full.png\"/>";
    			 }else{
    				 star = " <img id=\"bflag\" onclick=\"javascript:clickSetStar('"+model.getId()+"');\" src=\"iwork_img/star_empty.png\"/>";
    			 }
    			 if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(0))){
    				 isread ="unread"; 
    				 icon.append("<input type=\"button\" class=\"unread_icon\" disabled/>");
    				//判断是否有附件
    			 }else{
    				 icon.append("<input type=\"button\" class=\"read_icon\" disabled/>");
    			 }
    			 if(model.getAttachment()==null||model.getAttachment().equals("")){
    				 
    			}else{
    				icon.append("<input type=\"button\" class=\"attach\" disabled/>");
    			}
    			 if(model.getIsImportant()!=null){
	    			 if(model.getIsImportant().equals(new Long(1))){
	    				 isImportant = "[重要邮件]";
	    			 }else if(model.getIsImportant().equals(new Long(2))){
	    					 isImportant = "[非常重要]";
	    			}
    			 }
    			 MailModel mm =  iWorkMailDAO.getMailModelById(model.getBindId());
    			 if(mm!=null){
    				 content = mm.get_content();
    				 if(content!=null){
    					 String tmp = HtmlRegexpUtil.filterHtml(content.replace("\t","").replace("\n","").replace("\r",""));
    					 if(tmp.length()>30){
    						 content = tmp.substring(0,30);
    					 }else{
    						 content = tmp;
    					 }
    				 }
    			 }
    			 String fullpath = model.getMailFrom();
    			String  userid = UserContextUtil.getInstance().getUserId(model.getMailFrom());
    			if(userid!=null){
    				UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
    				if(uc!=null){
    					sender = uc.get_userModel().getUsername();
    					fullpath = uc.get_deptModel().getDepartmentname()+"/"+model.getMailFrom();
    				}
    			}else{
    				sender = model.getMailFrom();
    			}
				 html.append("<tr class=\"mailItem ").append(isread).append("\" id=\"item_").append(model.getId()).append("\"  name=\"receiveMail\">").append("\n");
				 html.append("	<td ><input type=\"checkbox\" name=\"ckb_selectAll\"; value=\"").append(model.getId()).append("\"/></td>").append("\n");
				 html.append("	<td  class=\"mailicon\" >").append(icon).append("</td>").append("\n");
				 html.append("	<td  class=\"mailUser\"  onClick=\"javascript:showDetailInfo('").append(model.getBindId()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','").append(model.getMailBox()).append("');\" ><span title=\"").append(fullpath).append("\">").append(sender).append("</span></td>").append("\n");
				 html.append("	<td class=\"mailTitle\"  onClick=\"javascript:showDetailInfo('").append(model.getBindId()).append("','").append(model.getId()).append("','").append(model.getTitle()).append("','").append(model.getMailBox()).append("');\" >").append(isImportant).append(model.getTitle()).append("-<span>").append(content).append("</span></td>").append("\n");
				 html.append("	<td class=\"mailTitle\" >").append("\n");
				 html.append(UtilDate.datetimeFormat(model.getCreateTime(),"yyyy-MM-dd HH:mm"));
				 html.append("	</td>").append("\n");
				 html.append("	<td class=\"mailicon\">").append("\n");
				 html.append(star).append("\n");
				 html.append("	</td>").append("\n");
				 html.append("</tr>").append("\n");
    		 }
    	 }else{
    		 html.append("<tr><td colspan=\"6\">未发现您的内部邮件!").append("</td></tr>");
    	 }
    	 
    	 return html.toString();
     }
	/**
	 * 
	 * @param innerAccount
	 * @param wwwAccount
	 * @param list
	 * @param addressType
	 * @return
	 */
	public String getAddressList(HashMap innerAccount,String list,String addressType){
		list = list.trim();
		Vector tmpList=new UtilString(list).split(";");
		for(int i=0;i<tmpList.size();i++){
			String mailAccount=(String)tmpList.get(i);
			mailAccount=mailAccount.trim();
			if(mailAccount.length()==0)continue;
				if(UserContextUtil.getInstance().checkAddress(mailAccount)){
						innerAccount.put(new Integer(innerAccount.size()), mailAccount);
				}else{
					return MailStatusConst.ADDRESS_ERROR;
				}
		}
		return MailStatusConst.OK;
	}

	
	

	/**
	 * 获取收件箱内的信息List
	 * @author wenpengyu
	 * */
	public List<MailTaskModel> getReceiveBoxInfo(String username){
		
		return iWorkMailTaskDAO.findall(username);
		
	}

	

	/**
	 * 获取所有已发邮件
	 * @author 杨连峰
	 * @param model 
	 * @return
	 */
	public List<MailOwnerModel> getSendList(String userName){
		
		return iWorkMailOwnerDAO.getSendList(userName);
		
	}
	
	

	/**
	 * 获取收件箱内每条信息的详细内容
	 * @author wenpengyu
	 * */
	
	public List<MailModel> findReceiveEmailById(long id){
		 MailTaskModel model = new MailTaskModel();
		 model.setIsRe(MailTaskStatusConst.isRe);
		 List list =  iWorkMailDAO.findById(id);	
		   
			return list;
		}

	

	
	/**
	 * 更新邮件信息
	 * 
	 * */
	public void updateRecEmail(MailTaskModel model){
		
		iWorkMailTaskDAO.updateRecEmail(model);
		
		
	}
	/**
	 * 收件箱邮件删除
	 */	
	public boolean deleteReEmail(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				Long id = Long.parseLong(str[i]);
				if (id > 0) {
					MailTaskModel taskmodel = iWorkMailTaskDAO.getMailTaskModelById(id);
						
					taskmodel.setIsDel(BoxTypeConst.IS_DEL_YES);
					iWorkMailTaskDAO.updateMailTask(taskmodel);
					// 执行插入删除记录表中操作
					Long blindid = taskmodel.getBindId();
					// 获取当前时间
					String currentDate = UtilDate.getNowDatetime();
					// 获取当前人
					String owner = taskmodel.getOwner();
					// 获取邮件id
					Long emailId = taskmodel.getId();
					// 获取邮件的类型
					String emailType = String.valueOf(BoxTypeConst.TYPE_TRANSACT);
					MailDelModel mailDelModel = new MailDelModel();

					mailDelModel.setBindId(blindid);

					mailDelModel.setType(emailType);
					mailDelModel.setCreateTime(UtilDate.StringToDate(
							currentDate, "yyyy-MM-dd HH:mm:ss"));
					mailDelModel.setOwner(owner);
					mailDelModel.setTaskId(emailId);
					iWorkMailDelDAO.addBoData(mailDelModel);
					flag=true;

				}
			}
		}
		return flag;
	}



	/**
	 * 收件箱邮件彻底删除
	 */	
	public boolean deleteAllReEmail(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				if(str[i].equals("0")){
					
				}else{
			    Long id = Long.parseLong(str[i]);
				model = iWorkMailTaskDAO.getMailTaskModelById(id);
				model.setIsStar(new Long(0));
				iWorkMailTaskDAO.deleteMailTask(model);
				flag = true;
				}
			}
		}
		return flag;
	}
	/**
	 * 收件箱邮件标星
	 */	
	public boolean setRecStar(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				if(str[i].equals("0")){
					
				}else{
			    Long id = Long.parseLong(str[i]);
				model = iWorkMailTaskDAO.getMailTaskModelById(id);
				model.setIsStar(new Long(1));
				iWorkMailTaskDAO.updateRecEmail(model);
				flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 取消收件箱邮件标星
	 */	
	public boolean cancelSetRecStar(String ids){
		boolean flag = false;
		MailTaskModel model;
		if(ids!=null||"".equals(ids)){
			String str[] = ids.split(",");
			for(int i = 0;i<str.length;i++){
				if(str[i].equals("0")){
					
				}else{
			    Long id = Long.parseLong(str[i]);
				model = iWorkMailTaskDAO.getMailTaskModelById(id);
				model.setIsStar(new Long(0));
				iWorkMailTaskDAO.updateRecEmail(model);
				flag = true;
				}
			}
		}
		return flag;
	}
	public String ToAndCc(String to,String cc){
		String returnCode = "";
		HashMap innerAccount = new HashMap();
		// 获取账户列表
		returnCode = getAddressList(innerAccount, to, "TO");
		if (!returnCode.equals(MailStatusConst.OK)) {
			return returnCode;
		}
		// 获取邮件抄送
		returnCode = getAddressList(innerAccount, cc, "CC");
		if (!returnCode.equals(MailStatusConst.OK)) {
			return returnCode;
		}

		return MailStatusConst.OK;
	}
	/**
	 * 写信保存草稿箱
	 * @return 
	 * 
	 * */
	public String saveToUnsend(MailModel model,String Ownerid) {
		if (model != null) {
			HashMap innerAccount = new HashMap();
			UserContext sender = UserContextUtil.getInstance()
					.getCurrentUserContext();
			if (model.get_createUser() == null
					|| "".equals(model.get_createUser())) {
				model._createUser = sender.get_userModel().getUserid();
				model._createDate = Calendar.getInstance().getTime();
				model._mailFrom = UserContextUtil.getInstance()
						.getCurrentUserFullName();
			}
			String returnCode = "";
			//
			// 获取账户列表
			/*String mail_to = model.get_to();
			returnCode = getAddressList(innerAccount, model._to, "TO");
			if (!returnCode.equals(MailStatusConst.OK)) {
				return returnCode;
			}
			// 获取邮件抄送
			model._cc = model._cc.trim();
			returnCode = getAddressList(innerAccount, model._cc, "CC");
			if (!returnCode.equals(MailStatusConst.OK)) {
				return returnCode;
			}
			
			String to_="";
			if(model.get_to()!=null){
				String[] to = model.get_to().split(",");
				for(int i=0;to.length>i;i++){
					if(i==0){
						to_=to[i];
					}else{
						to_ += ","+to[i];
					}
					model.set_to(to_);
				}
			}
			String cc_="";
			if(model.get_cc()!=null){
				String[] cc = model.get_cc().split(",");
				for(int i=0;cc.length>i;i++){
					if(i==0){
						cc_=cc[i];
					}else{
						cc_ += ","+cc[i];
					}
					model.set_cc(cc_);
				}
			}*/
			
			boolean flag = iWorkMailDAO.save(model);
			if (!flag)
				return MailStatusConst.DB_CONNECT_ERROR;

			
			// 获取密送
			// model._bcc = model._bcc.trim();
			// returnCode=getAddressList(innerAccount,model._bcc,"BCC");
			// if(!returnCode.equals(MailStatusConst.OK)){
			// return returnCode;
			// }
			// 添加发送任务表
			MailOwnerModel mom = new MailOwnerModel();
			mom.setTitle(model._title);
			mom.setBindId(model._id);
			// mom.setMailFrom(model._mailFrom);
			mom.setMailTo(model._to);
			mom.setIsDel(new Long(0));
			mom.setIsRe(new Long(0));
			mom.setIsImportant(new Long(0));
			mom.setIsStar(new Long(0));
			mom.setIsArchives(new Long(0));
			mom.setIsImportant(model._isImportant);
			mom.setCreateTime(Calendar.getInstance().getTime());
			mom.setOwner(sender._userModel.getUserid());
			mom.setMailBox(BoxTypeConst.TYPE_SEND);
            mom.setIsArchives(new Long(0));
            if(Ownerid!=null){
            	Long id=new Long(Ownerid);
            	mom.setId(id);
            	iWorkMailOwnerDAO.updateMailOwnerModel(mom);
            }else if(Ownerid==null){
            	Long ownerId = iWorkMailOwnerDAO.save(mom);
			
			if (ownerId == null)
				return MailStatusConst.DB_CONNECT_ERROR;
			if (innerAccount != null && innerAccount.size() > 0) {
				for (int i = 0; i < innerAccount.size(); i++) {
					String mailAccount = (String) innerAccount.get(new Integer(
							i));
					OrgUser ou = UserContextUtil.getInstance().getOrgUserInfo(
							mailAccount);
					if (ou == null) {
						continue;
					}

				}
			}
         }
	}
		return  MailStatusConst.OK;
}
	/**
	 * 根据taskid查找邮件信息表里的bindid并将其收件人，发送人，抄送人list出来
	 * 
	 * 
	 * */
	public List getMailInfoByBindid(String taskid){

		List<MailModel> list = iWorkMailDAO.getMailModelList(taskid);
//		List<MailModel> mailList = new ArrayList();
//		for (MailModel model : list) {
//			MailModel mail = new MailModel();
//			Long bindid = model.getBindId();
//
//			mail = iWorkMailDAO.getMailModelById(bindid);
//
//			mailList.add(mail);
//		}
		return list;
	}
	
	/**
	 * 获取页面收件箱"内部邮件"列表
	 * @param userId
	 * @param pageSize
	 * @param startRow
	 * @param isDel
	 * @param isRead
	 * @param mailBox
	 * @return
	 */
	public List<MailTaskModel> queryPageMails(String userId,int pageSize,int startRow,int isDel,int isRead,int mailBox){
		
		List<MailTaskModel> list = new ArrayList<MailTaskModel>();
		
		list = iWorkMailTaskDAO.getPageEmails(userId,pageSize,startRow,isDel,isRead,mailBox);
		
		return list;
	}
	
	
	public IWorkMailDAO getiWorkMailDAO() {
		return iWorkMailDAO;
	}
	public void setiWorkMailDAO(IWorkMailDAO iWorkMailDAO) {
		this.iWorkMailDAO = iWorkMailDAO;
	}
	public IWorkMailTaskDAO getiWorkMailTaskDAO() {
		return iWorkMailTaskDAO;
	}
	public void setiWorkMailTaskDAO(IWorkMailTaskDAO iWorkMailTaskDAO) {
		this.iWorkMailTaskDAO = iWorkMailTaskDAO;
	}
	public void setiWorkMailOwnerDAO(IWorkMailOwnerDAO iWorkMailOwnerDAO) {
		this.iWorkMailOwnerDAO = iWorkMailOwnerDAO;
	}
	public IWorkMailDelDAO getiWorkMailDelDAO() {
		return iWorkMailDelDAO;
	}


	public void setiWorkMailDelDAO(IWorkMailDelDAO iWorkMailDelDAO) {
		this.iWorkMailDelDAO = iWorkMailDelDAO;
	}
	
	
}
