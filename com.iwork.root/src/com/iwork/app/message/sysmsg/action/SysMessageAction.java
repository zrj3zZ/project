package com.iwork.app.message.sysmsg.action;

import java.util.List;
import com.iwork.app.constant.AppContextConstant;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.message.sysmsg.model.SysMessage;
import com.iwork.app.message.sysmsg.model.SysMessageLog;
import com.iwork.app.message.sysmsg.service.SysMessageLogService;
import com.iwork.app.message.sysmsg.service.SysMessageService;
import com.iwork.app.message.sysmsg.util.PageBean;
import com.iwork.commons.PagerService;
import com.iwork.commons.util.PurviewCommonUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.server.sequence.service.SysSequenceService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SysMessageAction extends ActionSupport {
	private SysMessageService sysMessageService;
	private SysMessageLogService sysMessageLogService;
	private SysSequenceService sysSequenceService;
	private PagerService pagerService;
	
	private SysMessage model;
	private List<SysMessage> list;
	private SysMessageLog logModel;
	
	private String msgList;
	protected String totalRows;
	protected Long id ;
	protected OrgUser userModel;
	protected OrgDepartment departmentModel;
	
	private String currentPage;
	private String status;
	private String type;
	private String cmd;
	private String userPanel;
	
	private int totalNum; // 总页数
	private int pageNumber; // 当前页数
	private int startRow; // 
	private int currLogPage; // 
	private int totalLogPage; //
	private int perLogPage = 10; // 	
	private PageBean logListBean; // 

	
	
	private int pageSize = 10; //每页条数
	
	private static final String CMD_READ_ONE = "READ_ONE";
	private static final String CMD_DEL_ONE = "DEL_ONE";
	private static final String CMD_READ_ALL = "READ_ALL";
	private static final String CMD_DEL_ALL = "DEL_ALL";
	
	
	//private static final String[] divTabs = {"divsysmsgall", "divsysmsgcommon", "divsysmsgbirth", "divsysmsgworkflow", "divmsgunread", "divmsgread"};
	
	public String index() {
		int totalRows = sysMessageLogService.getMsgLogRow();
		totalLogPage = PageBean.countTotalPage(perLogPage, totalRows);
		if (currLogPage == 0) {
			this.setCurrLogPage(1);
		}
		final int offset = PageBean.countOffset(perLogPage, currLogPage);    //当前页开始记录
        int length = perLogPage;    //每页记录数
        final int currentPage = PageBean.countCurrentPage(currLogPage);
        if (totalRows <= length) {
        	length = totalRows;
        }
        List<SysMessageLog> list = sysMessageLogService.getMsgLogList(perLogPage, currLogPage); //"一页"的记录
        
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(perLogPage);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalRows(totalRows);
        pageBean.setTotalPages(totalLogPage);
        pageBean.setList(list);
        pageBean.init();
        this.setLogListBean(pageBean);
		
		return SUCCESS;
	}
	
	public String send() throws Exception {
		if (model != null) {
			LoginContext lc = (LoginContext)ActionContext.getContext().getSession().get(AppContextConstant.LOGIN_CONTEXT_INFO);
			model.setSender(lc.getUid());
			model.setStatus(SysMessage.MSG_STATUS_READ_NO);
			model.setContent(model.getContent().replaceAll("\r\n", "<br>"));
			PurviewCommonUtil addressBookUtil = new PurviewCommonUtil();
			List<String> receiverList = addressBookUtil.getUserListFromAddressCode(model.getRcvRange());
			sysMessageService.sendMessageToUserList(model, receiverList);
			
			logModel = new SysMessageLog(model);
			logModel.setStatus(logModel.SYS_MESSAGE_LOG_STATUS_SUCCESS);
			sysMessageLogService.save(logModel);
			
		}
		return index();
	}

	/**
	 * 
	 * @return
	 */
	public String listInit() {
		UserContext userContext =  UserContextUtil.getInstance().getCurrentUserContext();
		if (id != null && !id.equals("")) {
			sysMessageService.setReadById(id);
		}
		this.setUserPanel(sysMessageService.getUserPannel(userContext));
		return "messagePage";
	}

	/**
	 * 获得设计列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		if(pageNumber==0)pageNumber=1;
		list = sysMessageService.getMessageList(pageSize, pageNumber, status, type);
		if(id!=null){
	        sysMessageService.setReadById(id);
	     }
		 totalNum = sysMessageService.getTotalListSize(status, type);
		return SUCCESS;   
	}
	/**
	 * 获得设计列表
	 * @return
	 * @throws Exception
	 */
	public String miniWinlist() throws Exception {
		if(pageNumber==0)pageNumber=1;
		pageSize = 5;
		list = sysMessageService.getMessageList(pageSize, pageNumber, status, type);
		if(id!=null){
			sysMessageService.setReadById(id);
		}
		totalNum = sysMessageService.getTotalListSize(status, type);
		return SUCCESS;  
	}
	
	public String popUpSysMsg() {
		UserContext userContext =  UserContextUtil.getInstance().getCurrentUserContext();	
		String msg = "";
		if(userContext==null){
			msg = "";
		}else{
			msg = sysMessageService.getPopUpJson(userContext);
		}
		ResponseUtil.write(msg);
		return null;
	}
	public String ggPopUpSysMsg() {
		UserContext userContext =  UserContextUtil.getInstance().getCurrentUserContext();	
		String msg = "";
		if(userContext==null){
			msg = "";
		}else{
			msg = sysMessageService.getGGPopUpJson(userContext);
		}
		ResponseUtil.write(msg);
		return null;
	}
	/**
	 * 获得消息详细页面
	 * @return
	 */
	public String getSyMsgItem(){
		if(id!=null){ 
			sysMessageService.setMsgStatus(this.getId(),UserContextUtil.getInstance().getCurrentUserId());
			model = sysMessageService.getSysMessageDAO().getSysMsgById(id);
			
		}
		return SUCCESS;
	}
	public String addIndex(){
		return SUCCESS;
	}
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public void remove() throws Exception {
		sysMessageService.removeSysMsgById(this.getId());
		ResponseUtil.write(SUCCESS); 
	}
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public void removeAll() throws Exception {
		sysMessageService.removeAllSysMsgOfUserId(UserContextUtil.getInstance().getCurrentUserId());
	}
	/**
	 * 修改状态为已读
	 * @return
	 * @throws Exception
	 */
	public String changeStatus(){
		sysMessageService.setMsgStatus(this.getId(),UserContextUtil.getInstance().getCurrentUserId());
		return SUCCESS; 
	}
	public PagerService getPagerService() {
		return pagerService;
	}
	public void setPagerService(PagerService pagerService) {
		this.pagerService = pagerService;
	}

	
	public String getMsgList() {
		return msgList;
	}

	public void setMsgList(String msgList) {
		this.msgList = msgList;
	}

	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SysMessage getModel() {
		return model;
	}
	public void setModel(SysMessage model) {
		this.model = model;
	}
	public SysMessageService getSysMessageService() {
		return sysMessageService;
	}
	public void setSysMessageService(SysMessageService sysMessageService) {
		this.sysMessageService = sysMessageService;
	}

	public SysSequenceService getSysSequenceService() {
		return sysSequenceService;
	}
	public void setSysSequenceService(SysSequenceService sysSequenceService) {
		this.sysSequenceService = sysSequenceService;
	}

	public OrgUser getUserModel() {
		return userModel;
	}

	public void setUserModel(OrgUser userModel) {
		this.userModel = userModel;
	}
	
	


	public SysMessageLogService getSysMessageLogService() {
		return sysMessageLogService;
	}

	public void setSysMessageLogService(SysMessageLogService sysMessageLogService) {
		this.sysMessageLogService = sysMessageLogService;
	}

	public SysMessageLog getLogModel() {
		return logModel;
	}


	public void setLogModel(SysMessageLog logModel) {
		this.logModel = logModel;
	}


	


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public OrgDepartment getDepartmentModel() {
		return departmentModel;
	}


	public void setDepartmentModel(OrgDepartment departmentModel) {
		this.departmentModel = departmentModel;
	}

	public String getUserPanel() {
		return userPanel;
	}

	public void setUserPanel(String userPanel) {
		this.userPanel = userPanel;
	}
	
	public String getCmd() {
		return this.cmd;
	}
	
	public void setCmd(String cmd) {
		this.cmd = cmd;
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

	public int getPerLogPage() {
		return perLogPage;
	}

	public void setPerLogPage(int perLogPage) {
		this.perLogPage = perLogPage;
	}

	public PageBean getLogListBean() {
		return logListBean;
	}

	public void setLogListBean(PageBean logListBean) {
		this.logListBean = logListBean;
	}

	public List<SysMessage> getList() {
		return list;
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
