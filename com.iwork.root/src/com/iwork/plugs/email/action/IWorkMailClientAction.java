package com.iwork.plugs.email.action;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.message.sysmsg.util.PageBean;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.addressbook.service.MultiAddressBookService;
import com.iwork.plugs.email.model.DataModel;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.model.SearchModel;
import com.iwork.plugs.email.service.IWorkMailClientService;
import com.iwork.plugs.email.service.IWorkMailGroupService;
import com.iwork.plugs.email.util.EmailCommonTools;
import com.opensymphony.xwork2.ActionSupport;

public class IWorkMailClientAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	private IWorkMailClientService iWorkMailClientService;
	private MultiAddressBookService multiAddressBookService;
	private IWorkMailGroupService iWorkMailGroupService;
	private FileUploadService emailUploadifyService;
	private List<MailTaskModel> receivelist;
	//private List<MailModel> list;
	private String fileUUID;
	private List<MailOwnerModel> ownerlist;
	private List<MailModel> list;
	private int total; // 总页数
	private int totalNum; // 总页数
	private int pageNumber; // 当前页数
	private int startRow; // 
	private int currLogPage; // 
	private int totalLogPage; //
	private int pageSize = 10; // 	
	private PageBean logListBean; // 
	private Long boxType;
	private MailModel model;
	private MailOwnerModel ownerModel;
	private MailTaskModel taskModel;
	private String attachTipsHtml;
	private String attachHtml;
	private String importantHtml;
	private String replyId;
	private int recNum;
	private int draftNum;
	private File uploadify;
	private String uploadifyFileName;
	private long ids;
	private Long mailId;
	private Long taskid;
	private String id;//删除邮件获取的id
    private String forwardid;//转发传递的id
    private String emailType;//邮件类型
    private String Ownerid;
    private String tabTittle;//获取页签
    
    private String sendStatus;
    private String senderTitle;
    private String recevieTitle;
    private String ccTitle;
    private String q;
    private String input; 
    private String selectJSON; 
    private String targetUserName; 
    private String defaultField; 
    private String html; 
    

    
    private String title; 
    private String userlist; 
    private String desc;
    
    private String companyid;
    private String nodeType;
    
    private SearchModel sm;
    //
    private List<MailTaskModel> tasklist;
    
    /**
     * 地址簿
     * @return
     */
    public String addressIndex(){
    	selectJSON = multiAddressBookService.getSelectOrgJson(input);
    	return SUCCESS;
    }
    
    /**
     * 邮件查询
     * @return
     */
    public String search(){
    	
    	return SUCCESS;
    }
    /**
     * 执行邮件查询
     * @return
     */
    public String doSearch(){
    	
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
		
    	if(sm!=null){ 
//    		html = iWorkMailClientService.getSearchList(sm);
    		DataModel dm = iWorkMailClientService.fullsearch(sm,startRow,pageSize);
    		if(dm!=null){
    			html = dm.getHtml();
    			total = dm.getTotalNum();
    		}
    	}else{
    		sm = new SearchModel();
    		if(input != null && !"".equals(input)){
    			String[] inputs = new String[6];
    			inputs = input.split(",");
    			sm.setSender(inputs[0]);
    			sm.setRecever(inputs[1]);
    			sm.setPosition(inputs[2]);
    			sm.setFolderid(inputs[3]);
//    			if(inputs[4]!=null && !"".equals(inputs[4])){
//    				sm.setBegindate(UtilDate.StringToDate(inputs[4], "yyyy-MM-dd"));
//    			}
//    			if(inputs[5]!=null && !"".equals(inputs[4])){
//    				sm.setEnddate(UtilDate.StringToDate(inputs[5], "yyyy-MM-dd"));
//    			}
    			DataModel dm = iWorkMailClientService.fullsearch(sm,startRow,pageSize);
        		if(dm!=null){
        			html = dm.getHtml();
        			total = dm.getTotalNum();
        		}
    		}
    	}
    	return SUCCESS;
    }
    
    public void showAddressJson(){
    	if(q!=null){
    		String json = iWorkMailClientService.showAddressJson(q);
    		ResponseUtil.write(json);
    	}
    	
    }

	/**
	 * 获得用户树
	 * @return
	 * @throws ParseException
	 */
	public void addressTreeJson() throws ParseException {
		String json = "";
		if(companyid==null || "".equals(companyid)){
			companyid = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getId();
		}
		if(nodeType!=null){ 
			if(id!=null&&nodeType.equals("dept")){
				json = multiAddressBookService.getDeptAndUserJson(new Long(id),input);
			}else{
				json = multiAddressBookService.getCompanyNodeJson(id,nodeType,false); 
			}
		}else{
				nodeType = "com";
				List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
				String addressJson = multiAddressBookService.getCompanyNodeJson(companyid,nodeType,true); 
				String groupJson = iWorkMailGroupService.getGroupListJson();
				StringBuffer jsonHtml = new StringBuffer();
				JSONArray jsonArray = null;
				 Map<String,Object> item = null;
					item = new HashMap<String,Object>();
					item.put("id",-1);
					item.put("name","分组地址簿");
					item.put("open", "true"); 
					item.put("nodeType", "diy");  
					item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
					item.put("iconClose", "iwork_img/ztree/diy/1_close.png");
					item.put("children", groupJson);
					root.add(item);
					item = new HashMap<String,Object>();
					item.put("id",-2);
					item.put("name","组织地址簿");
					item.put("open", "true"); 
					item.put("nodeType", "diy");  
					item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
					item.put("iconClose", "iwork_img/ztree/diy/1_close.png");
					item.put("children", addressJson);
					
					root.add(item);
					jsonArray = JSONArray.fromObject(root);
					jsonHtml.append(jsonArray);
					json = jsonHtml.toString();
		}
		ResponseUtil.write(json); 
		
	}
	/**
	 *  邮箱主页面
	 * @return
	 */
	public String index(){
		// 收件箱总条数
		recNum = iWorkMailClientService.getTotalRecNum();
		// 草稿箱总条数
		draftNum = iWorkMailClientService.getTotalDraftNum();
		return SUCCESS;
	}
	/**
	 * 获取
	 * @return
	 */
	public int showRow(){
		String username = UserContextUtil.getInstance().getCurrentUserId();
	    return	iWorkMailClientService.getMsgLogRow(username);
	}
	
	/**
	 * 获取收件邮件列表
	 * @return
	 */
	public String receiveList(){
		String username = UserContextUtil.getInstance().getCurrentUserId();
		HashMap hash = new HashMap();		
		receivelist =  iWorkMailClientService.getReceiveBoxInfo(username);
		return SUCCESS;
	}
	
	/**
	 * 写邮件
	 * @return
	 */
	public String newadd(){
		
		return SUCCESS;
	}
	
	/**
	 * 发送邮件
	 */
	public String sendEmail(){
		String msg="";
		if(tabTittle!=null&&!"".equals(tabTittle)){
			  String[] tittle=tabTittle.split(",");
			  tabTittle=tittle[0];
		  }
		if(model!=null){
			if(model.get_content()==null||"".equals(model.get_content())){
				model.set_content(" ");
			}
			if(Ownerid!=null){
				msg = iWorkMailClientService.sendEmail(model,Ownerid);
			}else{
				msg = iWorkMailClientService.sendEmail(model,null);
			}
		}	
		if(msg.equals("OK")){
			return SUCCESS;
		}else{
			MessageQueueUtil.getInstance().putAlertMsg("请填写有效姓名");
			return ERROR;
		}
		
	}
	
	/**
	 * 获取收件箱内记录的基本信息
	 */
	public String showEmailDetail(){
		if(taskid!=null && emailType!=null&& emailType.equals("-2")){
			iWorkMailClientService.setMailRead(taskid);
		}
		if(mailId!=null){ 
			model = iWorkMailClientService.getiWorkMailDAO().getMailModelById(mailId);
			if(model!=null){
				StringBuffer sender = new StringBuffer();
				StringBuffer recevieUser = new StringBuffer();
				UserContext uc = UserContextUtil.getInstance().getUserContext(model.get_createUser());
				if(uc!=null){
					senderTitle = EmailCommonTools.getInstance().buildUserAddress(uc,true);
				}
				String currentUserid = UserContextUtil.getInstance().getCurrentUserId();
				if(currentUserid.equals(model.get_createUser())){
					sendStatus = "发送成功";
				}
				String to = model.get_to();
				String[] userlist = to.split(",");
				int i = 0;
				for(String user:userlist){
					i++;
					if(i<=10){
						String userid = UserContextUtil.getInstance().getUserId(user);
						UserContext tocontext = UserContextUtil.getInstance().getUserContext(userid);
						if(tocontext!=null){
							Long isRead = iWorkMailClientService.getIsRead(mailId, userid);
							String address = EmailCommonTools.getInstance().buildUserAddress(tocontext,isRead,false);
							if(address!=null){
								recevieUser.append(address);
							}
						}
					}else{
						recevieUser.append("......");
						break;
					}
				}
				recevieTitle = recevieUser.toString();
				StringBuffer ccUser = new StringBuffer();
				String cc = model.get_cc();
				if(cc!=null){
					String[] cclist = cc.split(",");
					int j = 0;
					for(String user:cclist){
						j++;
						if(j>10){
							break;
						}else{
							String userid = UserContextUtil.getInstance().getUserId(user);
							UserContext tocontext = UserContextUtil.getInstance().getUserContext(userid);
							if(tocontext!=null){
								Long isRead = iWorkMailClientService.getIsRead(mailId, userid);
								String address = EmailCommonTools.getInstance().buildUserAddress(tocontext,isRead,false);
								if(address!=null){
									ccUser.append(address);
								}
							}
						}
					}
				}
				ccTitle = ccUser.toString();
			}
			attachTipsHtml = iWorkMailClientService.buildAttachTipHtml(model);
			attachHtml = iWorkMailClientService.buildAttachHtml(model); 
			importantHtml = iWorkMailClientService.getEmailImportantHtml(model.get_id());
			
		}
	   return SUCCESS;	
	}
	
		
	/**
	 * 删除邮件
	 * 
	 */
	public void deleteReEmail(){
		String msg = "false";
		if(id!=null||"".equals(id)){
		iWorkMailClientService.deleteReEmail(id);
		msg = "succ";
		
		}
		ResponseUtil.write(msg);
	     }
	
	/**
	 * 彻底删除邮件
	 * 
	 */
	public void deleteAllReEmail(){
		String msg = "false";
		if(id!=null||"".equals(id)){
		iWorkMailClientService.deleteAllReEmail(id);
		msg = "succ";
		
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 邮件标星
	 * 
	 */
	public void setRecEmailStar(){
		String msg = "false";
		if(id!=null||"".equals(id)){
		iWorkMailClientService.setRecStar(id);
		msg = "succ";
		
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 取消邮件标星
	 * 
	 */
	public void cancelSetRecEmailStar(){
		String msg = "false";
		if(id!=null||"".equals(id)){
			String str[] = id.split(",");
			iWorkMailClientService.cancelSetRecStar(id);
			msg = "succ";
		}

		ResponseUtil.write(msg);
	}
	
	/**
	 * 获取写信的信息并保存至邮件信息表中设置IS_ARCHIVES =0
	 * 
	 * */
  public void saveWriteEmail(){
	  String msg = "";
	if(model!=null){
		
		iWorkMailClientService.saveToUnsend(model);
		ResponseUtil.write(msg);
	}
	
}
	/**
	 * 跳转至转发邮件页面
	 * 
	 * */
  public String forwardEmail(){
		if (forwardid != null||"".equals(forwardid)) {
			
			list =  iWorkMailClientService.findReceiveEmailById(Long.parseLong(forwardid));
		}
	  return SUCCESS;
  }
  /**
   * 转发邮件
   * */
  public String resendEmail(){
		if(taskModel!=null){
			String msg = iWorkMailClientService.resendEmail(taskModel);
			return SUCCESS;
		}
		return ERROR;
	}
  
  /**
   * 回复邮件
   * 
   * 
   * */
  public String replyEmail(){
		if (replyId != null||"".equals(replyId)) {
			Long id = Long.parseLong(replyId);
			list =  iWorkMailClientService.findReceiveEmailById(id);//用taskid查找mailId
		}
	  return SUCCESS;
  }
  
  /**
   * 回复全部邮件
   * 
   * */
  public String replyEmailAll(){
	  //查找发送人的发送记录（bindid)
	  Map params = new HashMap();
		params.put(1,replyId);
		DBUTilNew.getDataStr("bind_id","select count bind_id from iwork_mail_task where id = ? ",params);
	    list = new ArrayList();
		
		
		if (replyId != null||"".equals(replyId)) {
			Long id = Long.parseLong(replyId);
			list =  iWorkMailClientService.findReceiveEmailById(id);//用taskid查找mailId
		}
	  return SUCCESS;
  }
  /**
	 * 上传文件
	 * @return
	 */
	public String uploadFile(){
		
		return SUCCESS;
	}
	
	
	/**
	 * 附件下载
	 * @return
	 */
	public String download(){
		emailUploadifyService.downLoadFile(FileUpload.class, fileUUID, ServletActionContext.getResponse());
		return null;
	}
	
	/**
	 * 上传附件
	 * @return
	 * @throws Exception
	 */
	public String doUpload() throws Exception {
		FileUpload model = emailUploadifyService.save(uploadify, uploadifyFileName);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		if (model!=null) {
			String uuid = model.getFileId();
			String url = model.getFileUrl();
			response.getWriter().print("{flag:true,uuid:'"+uuid+"',url:'"+url+"'}");
		} else {
			response.getWriter().print("{flag:false}");
			throw new Exception(uploadifyFileName + "上传失败");
		}
		return null; // 这里不需要页面转向，所以返回空就可以了
	}
	
	/**
	 * 
	 */
	public void showSendStatus(){
		if(mailId!=null){
			String content = iWorkMailClientService.showSendStatus(mailId);
			ResponseUtil.write(content);
		}
	}
	
	/**
	 * 
	 */
	public void mailUndo(){
		if(mailId!=null){
			String content = iWorkMailClientService.mailUndo(mailId);
			ResponseUtil.write(content);
		}
	}
	
  
	public List<MailModel> getList() {
		return list;
	}
	
	public void setList(List<MailModel> list) {
		this.list = list;
	}
	public String getReplyId() {
		return replyId;
	}
	public String getForwardid() {
		return forwardid;
	}


	public void setForwardid(String forwardid) {
		this.forwardid = forwardid;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}


	public MailTaskModel getTaskModel() {
		return taskModel;
	}


	public void setTaskModel(MailTaskModel taskModel) {
		this.taskModel = taskModel;
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

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
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

	public PageBean getLogListBean() {
		return logListBean;
	}

	public void setLogListBean(PageBean logListBean) {
		this.logListBean = logListBean;
	}

	public Long getBoxType() {
		return boxType;
	}

	public void setBoxType(Long boxType) {
		this.boxType = boxType;
	}

	public IWorkMailClientService getiWorkMailClientService() {
		return iWorkMailClientService;
	}
	public void setiWorkMailClientService(
			IWorkMailClientService iWorkMailClientService) {
		this.iWorkMailClientService = iWorkMailClientService;
	}
	public List<MailTaskModel> getReceivelist() {
		return receivelist;
	}

	public void setReceivelist(List<MailTaskModel> receivelist) {
		this.receivelist = receivelist;
	}

	public MailModel getModel() {
		return model;
	}

	public void setModel(MailModel model) {
		this.model = model;
	}

	public List<MailOwnerModel> getOwnerlist() {
		return ownerlist;
	}


	public long getIds() {
		return ids;
	}


	public void setIds(long ids) {
		this.ids = ids;
	}

	public int getRecNum() {
		return recNum;
	}

	public void setRecNum(int recNum) {
		this.recNum = recNum;
	}

	public int getDraftNum() {
		return draftNum;
	}

	public void setDraftNum(int draftNum) {
		this.draftNum = draftNum;
	}

	public void setEmailUploadifyService(FileUploadService emailUploadifyService) {
		this.emailUploadifyService = emailUploadifyService;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public void setUploadifyFileName(String uploadifyFileName) {
		this.uploadifyFileName = uploadifyFileName;
	}
	public Long getMailId() {
		return mailId;
	}
	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}
	public String getAttachHtml() {
		return attachHtml;
	}
	public String getAttachTipsHtml() {
		return attachTipsHtml;
	}
	public void setAttachTipsHtml(String attachTipsHtml) {
		this.attachTipsHtml = attachTipsHtml;
	}
	public String getFileUUID() {
		return fileUUID;
	}
	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}
	public Long getTaskid() {
		return taskid;
	}
	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public String getOwnerid() {
		return Ownerid;
	}
	public void setOwnerid(String ownerid) {
		Ownerid = ownerid;
	}
	public String getTabTittle() {
		return tabTittle;
	}
	public void setTabTittle(String tabTittle) {
		this.tabTittle = tabTittle;
	}
	public String getSenderTitle() {
		return senderTitle;
	}
	public String getRecevieTitle() {
		return recevieTitle;
	}
	public String getCcTitle() {
		return ccTitle;
	}
	public void setCcTitle(String ccTitle) {
		this.ccTitle = ccTitle;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getHtml() {
		return html;
	}
	public String getSelectJSON() {
		return selectJSON;
	}
	public void setSelectJSON(String selectJSON) {
		this.selectJSON = selectJSON;
	}
	public String getTargetUserName() {
		return targetUserName;
	}
	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}
	public String getDefaultField() {
		return defaultField;
	}
	public void setDefaultField(String defaultField) {
		this.defaultField = defaultField;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserlist() {
		return userlist;
	}
	public void setUserlist(String userlist) {
		this.userlist = userlist;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setMultiAddressBookService(
			MultiAddressBookService multiAddressBookService) {
		this.multiAddressBookService = multiAddressBookService;
	}
	public void setiWorkMailGroupService(IWorkMailGroupService iWorkMailGroupService) {
		this.iWorkMailGroupService = iWorkMailGroupService;
	}

	public SearchModel getSm() {
		return sm;
	}
	public void setSm(SearchModel sm) {
		this.sm = sm;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getImportantHtml() {
		return importantHtml;
	}
	
	
}
