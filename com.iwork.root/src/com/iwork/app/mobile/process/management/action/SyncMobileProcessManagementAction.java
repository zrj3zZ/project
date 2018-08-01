package com.iwork.app.mobile.process.management.action;

import java.io.UnsupportedEncodingException;

import com.iwork.app.mobile.process.management.service.SyncMobileProcessManagementService;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.util.ResponseUtil;
import com.iwork.process.desk.constant.ProcessDeskManagementConstant;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
public class SyncMobileProcessManagementAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String pageType;
	private String queryWord;// 查询操作中的任务标题
	private SyncMobileProcessManagementService syncMobileProcessManagementService;
	private String sessionId;
	private String curPageNo;
	private String pageSize;
	private String from;
	private String title;
	private Long isCreate;
	private Long processStatus;
	private static Logger logger = Logger.getLogger(SyncMobileProcessManagementAction.class);
	/**
	 * 获取手机端用户待办列表
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getMobileProcessJson() {
		String json = "";
		if (null != pageType && !"".equals(pageType)) {
				if (null != curPageNo && !"".equals(curPageNo)
						&& null != pageSize && !"".equals(pageSize)) {
					Page page = new Page();
					page.setCurPageNo(Integer.parseInt(curPageNo));
					page.setPageSize(Integer.parseInt(pageSize));
					//对前台传过来的查询信息进行数据处理
					if(null!=queryWord&&!"".equals(queryWord)){
						try {
							queryWord = java.net.URLDecoder.decode(queryWord,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							logger.error(e,e);
						}
					}
					// 待办
					if (Integer.parseInt(pageType) == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_TODOLIST) {
						json = syncMobileProcessManagementService
								.getTodoListJson(page, queryWord);
						// 在办未结
					} else if (Integer.parseInt(pageType) == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_PROCESSING) {
						json = syncMobileProcessManagementService
								.getProcessingTaskJson(page, queryWord);
						// 已办流程
					} else if (Integer.parseInt(pageType) == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_HISTORY) {
						json = syncMobileProcessManagementService
								.getHistoryListJson(page, queryWord);
						// 通知传阅
					} else if (Integer.parseInt(pageType) == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_NOTICE) {
						json = syncMobileProcessManagementService
								.getNoticeListJson(page, queryWord);
						//已办任务跟踪 modify 2013-12-23 David.Yang
					}else if (Integer.parseInt(pageType) == ProcessDeskManagementConstant.PROCESS_TASK_TYPE_FINISH) {
						json = syncMobileProcessManagementService.getFinishMonitorListJson(from, title, isCreate, processStatus, Long.parseLong(pageSize), Long.parseLong(curPageNo));
					} 
					if (null != json) {
						ResponseUtil.writeTextUTF8(json);
					} else {
						ResponseUtil.writeTextUTF8("");
					}
				} else {
					ResponseUtil.writeTextUTF8("");
				}
		} else {
			ResponseUtil.writeTextUTF8("");
		}
		return null;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getQueryWord() {
		return queryWord;
	}

	public void setQueryWord(String queryWord) {
		this.queryWord = queryWord;
	}

	public SyncMobileProcessManagementService getSyncMobileProcessManagementService() {
		return syncMobileProcessManagementService;
	}

	public void setSyncMobileProcessManagementService(
			SyncMobileProcessManagementService syncMobileProcessManagementService) {
		this.syncMobileProcessManagementService = syncMobileProcessManagementService;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCurPageNo() {
		return curPageNo;
	}

	public void setCurPageNo(String curPageNo) {
		this.curPageNo = curPageNo;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getIsCreate() {
		return isCreate;
	}

	public void setIsCreate(Long isCreate) {
		this.isCreate = isCreate;
	}

	public Long getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Long processStatus) {
		this.processStatus = processStatus;
	}

}
