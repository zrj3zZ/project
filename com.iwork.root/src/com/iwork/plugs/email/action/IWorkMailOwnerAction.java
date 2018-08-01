package com.iwork.plugs.email.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.service.IWorkMailOwnerService;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkMailOwnerAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private IWorkMailOwnerService iWorkMailOwnerService;// 业务层对象实例化
	private List<MailOwnerModel> ownerlist;// 发件箱集合
	private MailOwnerModel mailOwnerModel;
	private MailModel mailModel;
	private MailModel model;
	private String ids;// 发件邮箱的邮件id
	private String _to;// 收件人
	private Date _createDate;// 创建时间
	private String _cc;// 抄送人
	private String _title;// 标题
	private String _content;// 内容
	public Long _mailType;// 邮件类型
	public Long _isSend;// 是否已发送
	public Long _isImportant;// 是否为重要文件
	public Long _mailSize;// 邮件大小
	public String _bcc;// 秘送人
	public String _mailFrom; // 发件人
	public String _attachments;// 附件
	public Long _messageId;//
	public Long _relatedId;// 关联邮件id
	private int totalNum; // 
	private int pageNumber; // 当前页数
	private int currLogPage; // 当前页
	private int totalLogPage; // 总页数
	private int pageSize = 10; // 每页显示的条数
	// private PageBean logListBean; //
	private Long boxType;
	private int total;
	private String subTitle;// 截取title的字段
	private String emailType;// 邮件类型
	private String taskId;// 所需要邮箱id
	private String id;
	private String attachHtml;
	private String tabTittle;//获取页签
	private String tittle;
	private String recipient;//
	private String sqr;//截取到的收件人字符串
	private String toName;//取收件人姓名
	private String ccName;//取抄送人人姓名
	/**
	 * 获取已发邮件
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public String getSendList() {
		// 获取当前用户
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		// 获取当前用户发件箱信息的所有记录条数
		ownerlist = iWorkMailOwnerService.getSendList(userId);

		return SUCCESS;
	}

	/**
	 * 
	 * 删除发件箱邮件
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public void delEamil() {
		String msg = "false";
		// 获取页面要删除的id
		iWorkMailOwnerService.update(id);
		msg = "succ";
		ResponseUtil.write(msg);
	}

	/**
	 * 
	 * 查看发件箱邮件
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public String searchEmail() {

		mailModel = iWorkMailOwnerService.searchEmail(id);
		/*
		 * _to = mailModel.get_to();// 收件人 _createDate =
		 * mailModel.get_createDate();// 创建时间 _cc = mailModel.get_cc();// 抄送人
		 * _title = mailModel.get_title();// 主题 _content =
		 * mailModel.get_content();// 内容 _mailType = mailModel.get_mailType();//
		 * 邮件类型 _isSend = mailModel.get_isSend();// 是否已发送 _isImportant =
		 * mailModel.get_isImportant();// 是否为重要邮件 _mailSize =
		 * mailModel.get_mailSize();// 又见大小 _bcc = mailModel.get_bcc();// 秘送人
		 * _mailFrom = mailModel.get_mailFrom();// 发件人 _attachments =
		 * mailModel.get_attachments();// 附件 _relatedId =
		 * mailModel.get_relatedId();// 关联id
		 */return SUCCESS;
	}
	/**
	 * 通用查询MailId
	 */
	public void showMailId(){

		if(id!=null && emailType!=null){
			ResponseUtil.write(iWorkMailOwnerService.showMailId(id,emailType).toString());
		}

	}
	/**
	 * 
	 * 通用查看邮件
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public String generalSearchEmail() {

		mailModel = iWorkMailOwnerService.generalSearchEmail(id, emailType,
				taskId);

		return SUCCESS;
	}

	/**
	 * 
	 * 彻底删除发件箱邮件
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public void cleanEmail() {
		String msg = "false";
		iWorkMailOwnerService.cleanEmail(id);
		msg = "succ";
		ResponseUtil.write(msg);
	}

	/**
	 * 
	 * 标星发件箱邮件
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public void hasStarEmail() {
		String msg = "false";
		iWorkMailOwnerService.hasStarEmail(id);
		msg = "succ";
		ResponseUtil.write(msg);
	}

	/**
	 * 
	 * 取消标星发件箱邮件
	 * 
	 * @author 杨连峰
	 * @return
	 */
	public void cancelStar() {
		String msg = "false";
		// 获取页面要标星的id
		iWorkMailOwnerService.cancelStar(id);
		msg = "succ";
		ResponseUtil.write(msg);
	}

	/**
	 * 分页显示发件箱邮件列表
	 * 
	 * @return
	 */
	public String sendListEmails() {
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		// 获取邮件的总记录数
		total = iWorkMailOwnerService.countSendEail(userId);
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		ownerlist = new ArrayList<MailOwnerModel>();
		List<MailOwnerModel> resultList = iWorkMailOwnerService
		.getSendListEmails(userId, pageSize, startRow);
		for (MailOwnerModel mailOwnerModel : resultList) {
			String titles = mailOwnerModel.getTitle();
			//获取收件人
			sqr=mailOwnerModel.getMailTo();


			// 截取字符串
			int i = titles.length();
			int y=sqr.length();

			if (i > 40) {
				subTitle = titles.substring(0, 10) + "...";
			} else {
				subTitle = titles;

			}
			if(y > 15){
				recipient=sqr.substring(0, 12)+ "...";
			}else{
				recipient=sqr;
			}
			mailOwnerModel.setSubTitle(subTitle);
			mailOwnerModel.setMailTo(recipient);
			mailOwnerModel.setSjr(sqr);
			ownerlist.add(mailOwnerModel);

		}
		emailType = "-1";
		return SUCCESS;
	}


	/**  以下功能为草稿箱功能的操作   开始      **/
	/**
	 * 删除草稿箱指定数据
	 * 根据id
	 */
	public void delDraft() {
		String msg = "false";
		// 获取页面要删除的id
		iWorkMailOwnerService.updateDraft(id);
		msg = "succ";
		ResponseUtil.write(msg);
	}
	/**
	 * 彻底删除草稿箱邮件
	 * 根据id
	 */
	public void cleanDraft() {
		String msg = "false";
		iWorkMailOwnerService.cleanEmailDraft(id);
		msg = "succ";
		ResponseUtil.write(msg);
	}
	/**
	 * 显示草稿箱邮件列表
	 * 
	 * @return
	 */
	public String getDraftList() {
		// 获取当前用户
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		// 获取当前用户发件箱信息的所有记录条数
		ownerlist = iWorkMailOwnerService.getDraftList(userId);

		return SUCCESS;
	}

	/**
	 * 
	 * 查看草稿箱邮件
	 * 
	 * @author 杨连峰
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String searchDraftEmail() throws UnsupportedEncodingException {
		mailOwnerModel = iWorkMailOwnerService.searchDraftEmail(id);
		String bindId = String.valueOf(mailOwnerModel.getBindId());
		//获取发件信息表中的部分数据
		model = iWorkMailOwnerService.searchEmail(bindId);
		//获取邮件信息表中该条记录的抄送人
		attachHtml =iWorkMailOwnerService.buildAttachTipHtml(model);
		StringBuffer sbToName = new StringBuffer();
		if(model.get_to() != null && !model.get_to().equals("")){
			for (int i = 0; i < model.get_to().length(); i++) {
				if ((model.get_to().charAt(i)+"").getBytes().length>1) {
					sbToName.append(model.get_to().charAt(i));
					if(i != model.get_to().length()){
						if((model.get_to().charAt(i+1)+"").getBytes().length==1){
							sbToName.append(",");
						}
					}
				}
			}
			toName = sbToName.substring(0, sbToName.length()-1);
		}

		sbToName = new StringBuffer();
		if(model.get_cc() != null && !model.get_cc().equals("")){
			for (int i = 0; i < model.get_cc().length(); i++) {
				if ((model.get_cc().charAt(i)+"").getBytes().length>1) {
					sbToName.append(model.get_cc().charAt(i));
					if(i != model.get_cc().length()){
						if((model.get_cc().charAt(i+1)+"").getBytes().length==1){
							sbToName.append(",");
						}
					}
				}
			}
			ccName = sbToName.substring(0, sbToName.length()-1);
		}

		return SUCCESS;
	}

	/**
	 * 分页显示草稿箱邮件列表
	 * 
	 * @return
	 */
	public String getDraftListPage() {
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		// 获取邮件的总记录数
		total = iWorkMailOwnerService.countDraftEail(userId);
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		ownerlist = new ArrayList<MailOwnerModel>();
		List<MailOwnerModel> resultList  = iWorkMailOwnerService.getDraftListEmails(userId, pageSize,
				startRow);
		for (MailOwnerModel mailOwnerModel : resultList) {
			String titles = mailOwnerModel.getTitle();
			// 截取字符串
			if(titles!=null && !"".equals(titles)){
				int i = titles.length();
				if ( i > 40) {
					subTitle = titles.substring(0, 10) + "。。。";
				} else {
					subTitle = titles;
				}
				sqr=mailOwnerModel.getMailTo()==null?"":mailOwnerModel.getMailTo();
				int y=sqr.length();
				if(y > 15){
					recipient=sqr.substring(0, 12)+ "...";
				}else{
					recipient=sqr;
				}

			}
			mailOwnerModel.setMailTo(recipient);
			mailOwnerModel.setSubTitle(subTitle);
			ownerlist.add(mailOwnerModel);


		}

		return SUCCESS;
	}
	/**  以下功能为草稿箱功能的操作   结束     **/
	public List<MailOwnerModel> getOwnerlist() {
		return ownerlist;
	}

	public void setOwnerlist(List<MailOwnerModel> ownerlist) {
		this.ownerlist = ownerlist;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public IWorkMailOwnerService getiWorkMailOwnerService() {
		return iWorkMailOwnerService;
	}

	public void setiWorkMailOwnerService(
			IWorkMailOwnerService iWorkMailOwnerService) {
		this.iWorkMailOwnerService = iWorkMailOwnerService;
	}

	public String get_to() {
		return _to;
	}

	public void set_to(String to) {
		_to = to;
	}

	public Date get_createDate() {
		return _createDate;
	}

	public void set_createDate(Date createDate) {
		_createDate = createDate;
	}

	public String get_cc() {
		return _cc;
	}

	public void set_cc(String cc) {
		_cc = cc;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String title) {
		_title = title;
	}

	public String get_content() {
		return _content;
	}

	public void set_content(String content) {
		_content = content;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getCurrLogPage() {
		return currLogPage;
	}

	public void setCurrLogPage(int currLogPage) {
		this.currLogPage = currLogPage;
	}

	public int getTotalLogPage() {
		return totalLogPage;
	}

	public void setTotalLogPage(int totalLogPage) {
		this.totalLogPage = totalLogPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Long getBoxType() {
		return boxType;
	}

	public void setBoxType(Long boxType) {
		this.boxType = boxType;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Long get_mailType() {
		return _mailType;
	}

	public void set_mailType(Long mailType) {
		_mailType = mailType;
	}

	public Long get_isSend() {
		return _isSend;
	}

	public void set_isSend(Long isSend) {
		_isSend = isSend;
	}

	public Long get_isImportant() {
		return _isImportant;
	}

	public void set_isImportant(Long isImportant) {
		_isImportant = isImportant;
	}

	public Long get_mailSize() {
		return _mailSize;
	}

	public void set_mailSize(Long mailSize) {
		_mailSize = mailSize;
	}

	public String get_bcc() {
		return _bcc;
	}

	public void set_bcc(String bcc) {
		_bcc = bcc;
	}

	public String get_mailFrom() {
		return _mailFrom;
	}

	public void set_mailFrom(String mailFrom) {
		_mailFrom = mailFrom;
	}

	public String get_attachments() {
		return _attachments;
	}

	public void set_attachments(String attachments) {
		_attachments = attachments;
	}

	public Long get_messageId() {
		return _messageId;
	}

	public void set_messageId(Long messageId) {
		_messageId = messageId;
	}

	public Long get_relatedId() {
		return _relatedId;
	}

	public void set_relatedId(Long relatedId) {
		_relatedId = relatedId;
	}

	public MailOwnerModel getMailOwnerModel() {
		return mailOwnerModel;
	}

	public void setMailOwnerModel(MailOwnerModel mailOwnerModel) {
		this.mailOwnerModel = mailOwnerModel;
	}

	public MailModel getMailModel() {
		return mailModel;
	}

	public void setMailModel(MailModel mailModel) {
		this.mailModel = mailModel;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public MailModel getModel() {
		return model;
	}

	public void setModel(MailModel model) {
		this.model = model;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttachHtml() {
		return attachHtml;
	}

	public void setAttachHtml(String attachTipsHtml) {
		this.attachHtml = attachTipsHtml;
	}

	public String getTabTittle() {
		return tabTittle;
	}

	public void setTabTittle(String tabTittle) {
		this.tabTittle = tabTittle;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getCcName() {
		return ccName;
	}

	public void setCcName(String ccName) {
		this.ccName = ccName;
	}




}
