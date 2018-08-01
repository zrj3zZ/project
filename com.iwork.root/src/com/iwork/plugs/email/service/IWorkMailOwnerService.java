package com.iwork.plugs.email.service;

import java.util.List;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.dao.IWorkMailDAO;
import com.iwork.plugs.email.dao.IWorkMailDelDAO;
import com.iwork.plugs.email.dao.IWorkMailOwnerDAO;
import com.iwork.plugs.email.dao.IWorkMailTaskDAO;
import com.iwork.plugs.email.model.MailDelModel;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.util.EmailContentUtil;
import com.iwork.sdk.FileUploadAPI;

public class IWorkMailOwnerService {
	private IWorkMailDAO iWorkMailDAO;
	private IWorkMailTaskDAO iWorkMailTaskDAO;
	private IWorkMailOwnerDAO iWorkMailOwnerDAO;
	private IWorkMailDelDAO iWorkMailDelDAO;

	/**
	 * 获取所有已发邮件
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */
	public List<MailOwnerModel> getSendList(String userName) {

		return iWorkMailOwnerDAO.getSendList(userName);

	}

	/**
	 * 逻辑删除已选邮件
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */

	public void update(String ids) {

		// Long is_del = new Long(1);
		if (ids != null && !"".equals(ids)) {

			String[] strArr = ids.split(",");
			for (int i = 0; i < strArr.length; i++) {
				Long id = Long.parseLong(strArr[i]);
				if (id > 0) {
					MailOwnerModel mailOwnerModel = iWorkMailOwnerDAO
							.getSendListById(id);
					mailOwnerModel.setIsDel(BoxTypeConst.IS_DEL_YES);
					iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
					// 执行插入删除记录表中操作
					Long blindid = mailOwnerModel.getBindId();
					// 获取当前时间
					String currentDate = UtilDate.getNowDatetime();
					// 获取当前人
					String owner = mailOwnerModel.getOwner();
					// 获取邮件id
					Long emailId = mailOwnerModel.getId();
					// 获取邮件的类型
					String emailType = String.valueOf(BoxTypeConst.TYPE_SEND);
					MailDelModel mailDelModel = new MailDelModel();

					mailDelModel.setBindId(blindid);

					mailDelModel.setType(emailType);
					mailDelModel.setCreateTime(UtilDate.StringToDate(
							currentDate, "yyyy-MM-dd HH:mm:ss"));
					mailDelModel.setOwner(owner);
					mailDelModel.setTaskId(emailId);
					iWorkMailDelDAO.addBoData(mailDelModel);

				}
			}

		}
	}
	/**
	 * 通用查询MailId
	 */
	public String showMailId(String id,String emailType){
		String msg="";
		if(id!=null && emailType.equals("-1")){
			Long ids = new Long(id);
			MailOwnerModel mailOwnerModel = iWorkMailOwnerDAO.getSendListById(ids);
			if(mailOwnerModel!=null && mailOwnerModel.getBindId()!=null){
				msg=mailOwnerModel.getBindId().toString();
			}
		}
		if(id!=null && emailType.equals("-2")){
			Long ids = new Long(id);
			MailTaskModel mailTaskModel = iWorkMailTaskDAO.findTaskById(ids);
			if(mailTaskModel!=null && mailTaskModel.getBindId()!=null){
				msg=mailTaskModel.getBindId().toString();
			}
		}
		if(id!=null && emailType.equals("-3")){
			Long ids = new Long(id);
			MailDelModel mailDelModel = iWorkMailDelDAO.getModeByTaskId(ids);
			if(mailDelModel!=null && mailDelModel.getBindId()!=null){
				msg=mailDelModel.getBindId().toString();
			}
		}
		return msg;
	}

	/**
	 * 彻底删除已选邮件
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */

	public void cleanEmail(String ids) {

		// Long is_del = new Long(1);
		if (ids != null && !"".equals(ids)) {
			String[] strArr = ids.split(",");
			for (int i = 0; i < strArr.length; i++) {
				Long id = Long.parseLong(strArr[i]);
				if (id > 0) {
					MailOwnerModel mailOwnerModel = iWorkMailOwnerDAO
							.getSendListById(id);
					iWorkMailOwnerDAO.delEamil(mailOwnerModel);
				}
			}
		}

	}
	/**
	 * 逻辑删除草稿箱已选邮件
	 */

	public void updateDraft(String ids) {

		// Long is_del = new Long(1);
		if (ids != null && !"".equals(ids)) {

			String[] strArr = ids.split(",");
			for (int i = 0; i < strArr.length; i++) {
				Long id = Long.parseLong(strArr[i]);
				if (id > 0) {
					MailOwnerModel mailOwnerModel = iWorkMailOwnerDAO
							.getSendListById(id);
					mailOwnerModel.setIsDel(BoxTypeConst.IS_DEL_YES);
					iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
					// 执行插入删除记录表中操作
					Long blindid = mailOwnerModel.getBindId();
					// 获取当前时间
					String currentDate = UtilDate.getNowDatetime();
					// 获取当前人
					String owner = mailOwnerModel.getOwner();
					// 获取邮件id
					Long emailId = mailOwnerModel.getId();
					// 获取邮件的类型
					String emailType = String.valueOf(BoxTypeConst.TYPE_DRAFT);
					MailDelModel mailDelModel = new MailDelModel();

					mailDelModel.setBindId(blindid);

					mailDelModel.setType(emailType);
					mailDelModel.setCreateTime(UtilDate.StringToDate(
							currentDate, "yyyy-MM-dd HH:mm:ss"));
					mailDelModel.setOwner(owner);
					mailDelModel.setTaskId(emailId);
					iWorkMailDelDAO.addBoData(mailDelModel);

				}
			}

		}
	}

	/**
	 * 彻底删除草稿箱已选邮件
	 */

	public void cleanEmailDraft(String ids) {

		// Long is_del = new Long(1);
		if (ids != null && !"".equals(ids)) {
			String[] strArr = ids.split(",");
			for (int i = 0; i < strArr.length; i++) {
				Long id = Long.parseLong(strArr[i]);
				if (id > 0) {
					MailOwnerModel mailOwnerModel = iWorkMailOwnerDAO
							.getSendListById(id);
					iWorkMailOwnerDAO.delEamil(mailOwnerModel);
				}
			}
		}

	}

	/**
	 * 标星已选发件箱邮件
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */

	public void hasStarEmail(String ids) {
		// Long is_star = new Long();
		if (ids != null && !"".equals(ids)) {
			String[] strArr = ids.split(",");
			for (int i = 0; i < strArr.length; i++) {
				Long id = Long.parseLong(strArr[i]);
				if (id > 0) {
					MailOwnerModel mailOwnerModel = iWorkMailOwnerDAO
							.getSendListById(id);

					mailOwnerModel.setIsStar(BoxTypeConst.IS_STAR_YES);
					iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
				}
			}
		}

	}

	/**
	 * 取消标星已选发件箱邮件
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */

	public void cancelStar(String ids) {
		// Long is_star = new Long(0);
		if (ids != null && !"".equals(ids)) {
			// 获取id数组
			String[] strArr = ids.split(",");
			for (int i = 0; i < strArr.length; i++) {
				Long id = Long.parseLong(strArr[i]);
				if (id > 0) {
					// 验证id为0的情况
					MailOwnerModel mailOwnerModel = iWorkMailOwnerDAO
							.getSendListById(id);

					mailOwnerModel.setIsStar(BoxTypeConst.IS_STAR_NO);
					iWorkMailOwnerDAO.updateMailOwnerModel(mailOwnerModel);
				}
			}
		}

	}

	/**
	 * 查看发件箱每条邮件的详细内容
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */

	public MailModel searchEmail(String id) {

		MailModel mailModel = null;
		if (id != null && !id.equals("")) {
			// 转换id的类型
			Long mailId = Long.parseLong(id);
			mailModel = iWorkMailDAO.searchEmail(mailId);
			if(mailModel!=null&&mailModel.get_content()!=null){
	    		String pathname = mailModel.get_content();
	    		String content = EmailContentUtil.getInstance().getText(mailModel.get_id()+"",pathname);
	    		mailModel.set_content(content);
	    	}

		}
		return mailModel;

	}
	/**
	 * 单击 草稿箱邮件详细信息附件显示
	 */
	public String buildAttachTipHtml(MailModel model){
		StringBuffer html = new StringBuffer();
		if(model!=null&&model.get_attachments()!=null){
			//html.append("<Div>");
			String attchstr = model.get_attachments();
			String[] attchlist = attchstr.split(",");
			
			int count = 0;
			for(String uuid:attchlist){
				if(uuid.trim().equals("")){
					continue;
				}
				FileUpload fu = null;
				if(fu==null){
					 fu = FileUploadAPI.getInstance().getFileUpload(uuid);
					 if(fu!=null){
						 String fus=fu.getFileSrcName();
						 
						 if(fus!=null && fus.length()>16){
							 fus=fu.getFileSrcName().substring(0,15)+"...";
						 }
						 
						 html.append("<span  id=\"").append(uuid).append("\" class=\"attachItem\">").append(" <span><a href=\"iwork_email_download.action?fileUUID=")
						 .append(uuid).append("\"").append(" target=\"_blank\"><img src=\"/iwork_img/attach.png\"/>").append(fus).append("</a></span>");
						 
						 html.append("<a href=\"javascript:uploadifyReomve('").append(fu.getFileSrcName()).append("','").append(uuid).append("','").append(uuid).append("');\"><img src=\"/iwork_img/del3.gif\"/></a></span><br/>");
					 }
				}
				 count++;
			}
			/*if(fu!=null){
				String icon = "iwork_img/attach.png";
				if(fu.getFileSrcName()!=null){
					icon = FileType.getFileIcon(fu.getFileSrcName());
				}
				html.append("<span class=\"tipfileName\"><a href=\"javascript:redirectDiv()\"> <img src=\"").append("/iwork_img/attach.png").append("\"/>").append(fu.getFileSrcName()).append("</a>");
				
				
				if(count>1){
					html.append("..."); 
				} 
				html.append(" </span>");
			}
			
			html.append("</div>");*/
		}
		return html.toString(); 
	}
	/**
	 * 通用查询邮件信息
	 * 
	 * @author 杨连峰
	 * @param model
	 * @return
	 */

	public MailModel generalSearchEmail(String id, String emailType,
			String taskId) {
		MailTaskModel mailTaskModel = null;
		MailModel mailModel = null;
		MailOwnerModel mailOwnerModel=null;
		if (id != null && !id.equals("")) {
			// 转换id的类型
			Long mailId = Long.parseLong(id);
			Long emailtype = new Long(emailType);
			Long taskid = Long.parseLong(taskId);
			if (emailtype.equals(BoxTypeConst.TYPE_SEND)) {
				// 发件箱
				mailModel = iWorkMailDAO.searchEmail(mailId);
				// 格式化创建时间时间
				String date = String.valueOf(mailModel.get_createDate());
				UtilDate.StringToDate(date, "yyyy-MM-dd HH:mm:ss");
				mailModel.set_createDate(UtilDate.StringToDate(date,
						"yyyy-MM-dd HH:mm:ss"));
			
				
			} else if (emailtype.equals(BoxTypeConst.TYPE_TRANSACT)
					|| emailtype.equals(BoxTypeConst.TYPE_DRAFT)) {
				// 收件箱邮件和草稿箱
				mailTaskModel = iWorkMailTaskDAO.findTaskById(taskid);
				mailTaskModel.getIsRead();
				if (mailTaskModel.getIsRead().equals(BoxTypeConst.IS_READ_NO)) {
					// 更改已读状态
					mailTaskModel.setIsRead(BoxTypeConst.IS_READ_YES);
					// 获取当前时间
					String currentDate = UtilDate.getNowDatetime();
					mailTaskModel.setReadTime(UtilDate.StringToDate(
							currentDate, "yyyy-MM-dd HH:mm:ss"));
					mailModel = iWorkMailDAO.searchEmail(mailId);

					// 更新收件箱的已读时间和已读状态
					iWorkMailTaskDAO.updateMailTask(mailTaskModel);

				} else {
					mailModel = iWorkMailDAO.searchEmail(mailId);
				}

			} else if (emailtype.equals(BoxTypeConst.TYPE_DELETED)) {
				// 垃圾箱查看邮件
				MailDelModel mailDelModel = iWorkMailDelDAO.getBoData(taskid);
				// 获取垃圾表中对应邮件的邮件类型
				Long types = Long.parseLong(mailDelModel.getType());
				if (types == BoxTypeConst.TYPE_TRANSACT) {
					mailTaskModel.setIsRead(BoxTypeConst.IS_READ_YES);
					mailModel = iWorkMailDAO.searchEmail(mailId);
				} else {
					mailModel = iWorkMailDAO.searchEmail(mailId);

				}

			}

		}
		return mailModel;

	}

	/**
	 * 分页获取当前用户发件箱邮件总行数
	 * 
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public int countSendEail(String userId) {
		return iWorkMailOwnerDAO.countSendEail(userId);
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
	public List<MailOwnerModel> getSendListEmails(String userId, int pageSize,
			int startRow) {

		return iWorkMailOwnerDAO.getSendListEmails(userId, pageSize, startRow);
	}

	// 以下为草稿箱的实现功能
	/**
	 *获取当前用户发件箱邮件
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param currLogPage
	 * @return
	 */
	public List<MailOwnerModel> getDraftList(String userName) {

		return iWorkMailOwnerDAO.getDraftList(userName);

	}

	/**
	 *查看草稿箱邮件
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param currLogPage
	 * @return
	 */

	public MailOwnerModel searchDraftEmail(String id) {

		MailOwnerModel mailOwnerModel = null;
		if (id != null && !id.equals("")) {
			// 转换id的类型
			Long mailId = Long.parseLong(id);
			mailOwnerModel = iWorkMailOwnerDAO.searchDraftEmail(mailId);
		}
		return mailOwnerModel;

	}

	/**
	 * 分页获取当前用户草稿箱箱邮件总行数
	 * 
	 * @param boxType
	 * @param userId
	 * @return
	 */
	public int countDraftEail(String userId) {
		return iWorkMailOwnerDAO.countDraftEail(userId);
	}

	/**
	 *分页获取当前用户草稿箱箱邮件
	 * 
	 * @param boxType
	 * @param userId
	 * @param pageSize
	 * @param currLogPage
	 * @return
	 */
	public List<MailOwnerModel> getDraftListEmails(String userId, int pageSize,
			int startRow) {

		return iWorkMailOwnerDAO.getDraftListEmails(userId, pageSize, startRow);
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

	public IWorkMailOwnerDAO getiWorkMailOwnerDAO() {
		return iWorkMailOwnerDAO;
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
