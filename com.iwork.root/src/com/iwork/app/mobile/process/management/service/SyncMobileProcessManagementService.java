package com.iwork.app.mobile.process.management.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;

import com.iwork.app.mobile.constants.MobileServerConstants;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.desk.handle.service.ProcessDeskService;
import com.iwork.process.runtime.constant.ProcessTaskConstant;
import com.iwork.process.runtime.dao.ProcessRuntimeCcDAO;
import com.iwork.process.runtime.model.ProcessRuCc;

import net.sf.json.JSONArray;
public class SyncMobileProcessManagementService {
	private TaskService taskService;// 任务服务
	private HistoryService historyService;// 历史服务
	private RuntimeService runtimeService;
	private ProcessRuntimeCcDAO processRuntimeCcDAO;
	private ProcessDeskService processDeskService;
	private static Logger logger = Logger.getLogger(SyncMobileProcessManagementService.class);
	/**
	 * 获得待办列表的JSON
	 * 
	 * @param page
	 * @param sessionId
	 * @param taskOwner_query
	 * @param taskName_query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getTodoListJson(Page page,String taskName_query) {
		UserContext userContext = UserContextUtil.getInstance()
				.getCurrentUserContext();
		int totalRecord = 0; // 总记录行数
		int totalNum = 0; // 总记录页数
		StringBuffer jsonHtml = new StringBuffer();
		Map<String, Object> total = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		if (null == userContext) {
			return null;
		} else {
			String userid = userContext.get_userModel().getUserid();
			if (null == userid) {
				return null;
			} else {
				// 获得等待用户列表的查询对象
				TaskQuery taskQuery_candidate = taskService.createTaskQuery()
						.taskCandidateUser(userid);
				// 获得用户待办列表的查询对象
				TaskQuery taskQuery_todo = taskService.createTaskQuery()
						.taskAssignee(userid);
				// 加入任务名称查询信息
				if (null != taskName_query && !"".equals(taskName_query)) {
					taskName_query = taskName_query.trim();
					taskQuery_candidate = taskQuery_candidate.taskDescriptionLike("%" + taskName_query + "%");
					taskQuery_todo = taskQuery_todo.taskDescriptionLike("%" + taskName_query + "%");
				}
				// 获得任务列表(不包含分页,包含排序信息)
				List<Task> CandidateUserlist = new ArrayList<Task>();// 要显示到前台的等待用户领取的任务列表的LIST
				List<Task> toDoList = new ArrayList<Task>();// 要显示到前台的用户代办列表的LIST
				// 默认按日期时间排序
				CandidateUserlist = taskQuery_candidate.orderByTaskCreateTime()
						.desc().list();
				toDoList = taskQuery_todo.orderByTaskCreateTime().desc().list();
				// 总的行数
				totalRecord = CandidateUserlist.size() + toDoList.size();
				BigDecimal b1 = new BigDecimal(totalRecord);
				BigDecimal b2 = new BigDecimal(page.getPageSize());
				// 合起来的总页数 向上取整
				totalNum = b1.divide(b2, 0, BigDecimal.ROUND_UP).intValue();
				// 如果当前页数超过总页数
				if (page.getCurPageNo() > totalNum) {
					total.put("dataRows", "");
				} else {
					// 默认起始行
					int startRow = page.getPageSize()
							* (page.getCurPageNo() - 1);
					// 默认结束行
					int endRow = startRow + page.getPageSize();
					// 等待用户领取的任务列表的总行数
					int candidateUserTotalRecord = CandidateUserlist.size();
					BigDecimal cb1 = new BigDecimal(candidateUserTotalRecord);
					// 等待用户领取的任务列表的页数 向上取整
					int candidateTotalNum = cb1.divide(b2, 0,
							BigDecimal.ROUND_UP).intValue();
					// 如果是等待用户领取的任务列表的最后一页
					if (candidateTotalNum == page.getCurPageNo()) {
						if (CandidateUserlist.size() % page.getPageSize() == 0) {
							endRow = startRow + page.getPageSize();
						} else {
							endRow = startRow + CandidateUserlist.size()
									% page.getPageSize();
						}
					}
					// 获得等待用户领取的任务列表(包含分页,以及排序)
					if (endRow <= CandidateUserlist.size()) {
						List<Task> CandidateUserlist_IncludePage = CandidateUserlist
								.subList(startRow, endRow);
						for (Task task : CandidateUserlist_IncludePage) {
							Map<String, Object> item = new HashMap<String, Object>();
							// 装载发起人
							if (task.getOwner() != null) {
								String owner = task.getOwner();
								UserContext ownerContext = UserContextUtil
										.getInstance().getUserContext(owner);
								item.put("owner",
										ownerContext._userModel.getUsername());
							} else
								item.put("owner", "");
							// 装载标题
							if (task.getDescription() != null) {
								item.put("title", "(" + task.getName() + ")"
										+ task.getDescription());

							} else {
								item.put("title", "(" + task.getName() + ")");
							}
							// 装载节点名称
							item.put("wfName", task.getName());
							// 装载创建时间和流程历时
							if (task.getCreateTime() != null) {
								item.put("dateTime",
										getSimpleTime(task.getCreateTime()));
							} else {
								item.put("dateTime", "");
							}
							item.put("type", "抢签");
							// 装在任务参数
							item.put("actDefId", task.getProcessDefinitionId());
							item.put("instanceId", task.getProcessInstanceId());
							item.put("excutionId", task.getExecutionId());
							item.put("taskId", task.getId());
							items.add(item);
						}
					}

					// 用户代办列表的分页信息
					// 当前页还没有用户待办任务
					if (page.getCurPageNo() < candidateTotalNum) {
						startRow = 0;
						endRow = 0;
					}
					// 等待用户领取的任务列表的最后一页
					else if (page.getCurPageNo() == candidateTotalNum) {
						startRow = 0;
						if (CandidateUserlist.size() % page.getPageSize() == 0) {
							endRow = 0;
						} else {
							endRow = page.getPageSize()
									- CandidateUserlist.size()
									% page.getPageSize();
						}
					} else {// 当前页全部都是待办任务
						if (CandidateUserlist.size() % page.getPageSize() == 0) {
							startRow = (page.getCurPageNo() - candidateTotalNum - 1)
									* page.getPageSize();
						} else {
							startRow = (page.getCurPageNo() - candidateTotalNum - 1)
									* page.getPageSize()
									+ (page.getPageSize() - CandidateUserlist
											.size() % page.getPageSize());
						}
						endRow = startRow + page.getPageSize();
					}

					if (endRow > toDoList.size()) {
						endRow = toDoList.size();
					}
					List<Task> toDoList_IncludePage = toDoList.subList(
							startRow, endRow);
					for (Task task : toDoList_IncludePage) {
						Map<String, Object> item = new HashMap<String, Object>();
						Long flag = (Long) taskService
								.getVariable(
										task.getId(),
										ProcessTaskConstant.PROCESS_TASK_READ_STATUS_KEY);
						// 装载发起人
						if (task.getOwner() != null) {
							String owner = task.getOwner();
							UserContext ownerContext = UserContextUtil
									.getInstance().getUserContext(owner);
							item.put("owner",
									ownerContext._userModel.getUsername());
						} else
							item.put("owner", "");
						// 装载标题
						if (task.getDescription() != null) {
							item.put(
									"title",
									"(" + task.getName() + ")"
											+ task.getDescription());
						} else {
							item.put("title", "(" + task.getName() + ")");
						}
						// 装载节点名称
						item.put("wfName", task.getName());
						// 装载创建时间
						if (task.getCreateTime() != null) {
							item.put("dateTime",
									getSimpleTime(task.getCreateTime()));
						} else {
							item.put("dateTime", "");
						}
						if (ProcessTaskConstant.PROCESS_EXCUTION_UNREAD
								.equals(flag)) {
							item.put("type", "未读");
						} else {
							item.put("type", "待办");
						}
						// 装在任务参数
						item.put("actDefId", task.getProcessDefinitionId());
						item.put("instanceId", task.getProcessInstanceId());
						item.put("excutionId", task.getExecutionId());
						item.put("taskId", task.getId());
						items.add(item);
					}
					total.put("dataRows", items);
				}
				total.put("total", CandidateUserlist.size() + toDoList.size());
				total.put("curPage", page.getCurPageNo());
				total.put("pageSize", page.getPageSize());
				total.put("totalPages", totalNum);
				total.put("totalRecords", totalRecord);
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			}
		}
	}

	/**
	 * 获得在办未结列表的JSON
	 * 
	 * @param page
	 * @param sessionId
	 * @param taskOwner_query
	 * @param taskName_query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getProcessingTaskJson(Page page,  String taskName_query) {
		UserContext userContext = UserContextUtil.getInstance()
				.getCurrentUserContext();
		int totalRecord = 0; // 总记录行数
		int totalNum = 0; // 总记录页数
		StringBuffer jsonHtml = new StringBuffer();
		Map<String, Object> total = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		if (null == userContext) {
			return null;
		} else {
			String userid = userContext.get_userModel().getUserid();
			if (null == userid) {
				return null;
			} else {
				// 下面的代码片段获得查询的对象
				HistoricTaskInstanceQuery taskQuery_Processing = historyService
						.createHistoricTaskInstanceQuery().taskAssignee(userid)
						.finished();
				// 加入任务名称查询信息
				if (null != taskName_query && !"".equals(taskName_query)) {
					taskName_query = taskName_query.trim();
					taskQuery_Processing = taskQuery_Processing.taskDescriptionLike("%" + taskName_query + "%");
				}
				// 获得LIST(不包含分页)
				List<HistoricTaskInstance> histTaskList = new ArrayList<HistoricTaskInstance>();
				// 默认按时间逆序排列
				histTaskList = taskQuery_Processing
						.orderByHistoricActivityInstanceStartTime().desc()
						.list();
				// 定义一个HashMap用来去除流程重复的
				Map<String, HistoricTaskInstance> historicTaskInstanceMap = new HashMap<String, HistoricTaskInstance>();
				List<HistoricTaskInstance> newList = new ArrayList<HistoricTaskInstance>();
				for (int i = 0; i < histTaskList.size(); i++) {
					HistoricTaskInstance task = histTaskList.get(i);
					if (historicTaskInstanceMap.containsKey(task
							.getProcessInstanceId())) {
						continue;
					} else {
						historicTaskInstanceMap.put(
								task.getProcessInstanceId(), task);
						newList.add(task);
					}
				}
				histTaskList = newList;

				totalRecord = histTaskList.size();
				BigDecimal b1 = new BigDecimal(totalRecord);
				BigDecimal b2 = new BigDecimal(page.getPageSize());
				totalNum = b1.divide(b2, 0, BigDecimal.ROUND_UP).intValue(); // 计算页数,向上取整
				int startRow = page.getPageSize() * (page.getCurPageNo() - 1);// 开始行
				int endRow = startRow + page.getPageSize();// 结束行
				if (totalNum == page.getCurPageNo()) {// 如果是领取的最后一页
					endRow = startRow + histTaskList.size()
							% page.getPageSize();
					if (histTaskList.size() % page.getPageSize() == 0) {
						endRow = startRow + page.getPageSize();
					}
				}
				if (endRow <= histTaskList.size()) {
					// 获得LIST(包含分页)
					List<HistoricTaskInstance> histTaskList_includePage = histTaskList
							.subList(startRow, endRow);
					for (HistoricTaskInstance task : histTaskList_includePage) {
						Map<String, Object> item = new HashMap<String, Object>();
						// 装载发起人
						if (task.getOwner() != null) {
							String owner = task.getAssignee();
							UserContext ownerContext = UserContextUtil
									.getInstance().getUserContext(owner);
							item.put("owner",
									ownerContext._userModel.getUsername());
						} else
							item.put("owner", "");
						// 装载标题
						if (task.getDescription() != null) {
							item.put(
									"title",
									"(" + task.getName() + ")"
											+ task.getDescription());
						} else {
							item.put("title", "(" + task.getName() + ")");
						}
						// 装载节点名称
						item.put("wfName", task.getName());
						// 装载创建时间
						if (task.getStartTime() != null) {
							item.put("dateTime",
									getSimpleTime(task.getStartTime()));
						} else {
							item.put("dateTime", "");
						}
						// 装载类型
						item.put("type", "在办");
						// 装在任务参数
						item.put(MobileServerConstants.WF_JSON_KEY_ACTDEFID, task.getProcessDefinitionId());
						item.put(MobileServerConstants.WF_JSON_KEY_INSTANCEID, task.getProcessInstanceId());
						item.put(MobileServerConstants.WF_JSON_KEY_EXCUTIONID, task.getExecutionId());
						item.put(MobileServerConstants.WF_JSON_KEY_TASKID, task.getId());
						items.add(item);
					}
				}
				total.put(MobileServerConstants.WF_JSON_KEY_TOTAL, histTaskList.size());
				total.put(MobileServerConstants.WF_JSON_KEY_CURPAGE, page.getCurPageNo());
				total.put(MobileServerConstants.WF_JSON_KEY_PAGESIZE, page.getPageSize());
				total.put(MobileServerConstants.WF_JSON_KEY_TOTALPAGES, totalNum);
				total.put(MobileServerConstants.WF_JSON_KEY_TOTAL_RECORDS, totalRecord);
				// 如果当前页数超过总页数
				if (page.getCurPageNo() > totalNum) {
					total.put(MobileServerConstants.WF_JSON_KEY_TOTAL_DATAROWS, "");
				} else {
					total.put(MobileServerConstants.WF_JSON_KEY_TOTAL_DATAROWS, items);
				}
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			}
		}
	}

	/**
	 * 获得已办历史的JSON
	 * 
	 * @param page
	 * @param sessionId
	 * @param taskOwner_query
	 * @param taskName_query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getHistoryListJson(Page page, String taskName_query) {
		UserContext userContext = UserContextUtil.getInstance()
				.getCurrentUserContext();
		int totalRecord = 0; // 总记录行数
		int totalNum = 0; // 总记录页数
		StringBuffer jsonHtml = new StringBuffer();
		Map<String, Object> total = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		if (null == userContext) {
			return null;
		} else {
			String userid = userContext.get_userModel().getUserid();
			if (null == userid) {
				return null;
			} else {
				// 下面的代码片段获得查询的对象
				HistoricTaskInstanceQuery taskQuery_history = historyService
						.createHistoricTaskInstanceQuery().taskAssignee(userid);
				// 加入任务名称查询信息
				if (taskName_query != null && !"".equals(taskName_query)) {
					taskName_query = taskName_query.trim();
					taskQuery_history = taskQuery_history
							.taskDescriptionLike("%" + taskName_query + "%");
				}
				// 获得LIST(不包含分页)
				List<HistoricTaskInstance> histTaskList = new ArrayList<HistoricTaskInstance>();
				// 默认按照时间倒序排列
				histTaskList = taskQuery_history
						.orderByHistoricActivityInstanceStartTime().desc()
						.list();
				// 下面是分页控制
				totalRecord = histTaskList.size();
				BigDecimal b1 = new BigDecimal(totalRecord);
				BigDecimal b2 = new BigDecimal(page.getPageSize());
				totalNum = b1.divide(b2, 0, BigDecimal.ROUND_UP).intValue(); // 计算页数
																				// 向上取整
				int startRow = page.getPageSize() * (page.getCurPageNo() - 1);// 开始行
				int endRow = startRow + page.getPageSize();// 结束行
				if (totalNum == page.getCurPageNo()) {// 如果是领取的最后一页
					endRow = startRow + histTaskList.size()
							% page.getPageSize();
					if (histTaskList.size() % page.getPageSize() == 0) {
						endRow = startRow + page.getPageSize();
					}
				}
				int num = startRow;
				// 获得LIST(包含分页)
				if (endRow <= histTaskList.size()) {
					List<HistoricTaskInstance> histTaskList_includePage = histTaskList
							.subList(startRow, endRow);
					for (HistoricTaskInstance task : histTaskList_includePage) {
						num++;
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("id", num);
						// 装载发起人
						if (task.getOwner() != null) {
							String owner = task.getOwner();
							UserContext ownerContext = UserContextUtil
									.getInstance().getUserContext(owner);
							item.put(MobileServerConstants.WF_JSON_KEY_OWNER,
									ownerContext._userModel.getUsername());
						} else
							item.put(MobileServerConstants.WF_JSON_KEY_OWNER, "");
						// 装载标题
						if (task.getDescription() != null) {
							item.put(
									MobileServerConstants.WF_JSON_KEY_TITLE,
									"(" + task.getName() + ")"
											+ task.getDescription());
						} else {
							item.put(MobileServerConstants.WF_JSON_KEY_TITLE, "(" + task.getName() + ")");
						}
						// 装载节点名称
						item.put(MobileServerConstants.WF_JSON_KEY_STEPNAME, task.getName());
						// 装载创建时间
						if (task.getStartTime() != null) {
							item.put(MobileServerConstants.WF_JSON_KEY_CREATETIME,
									getSimpleTime(task.getStartTime()));
						} else {
							item.put(MobileServerConstants.WF_JSON_KEY_CREATETIME, "");
						}
						item.put(MobileServerConstants.WF_JSON_KEY_TYPE,"已办");
						// 装在任务参数
						item.put(MobileServerConstants.WF_JSON_KEY_ACTDEFID, task.getProcessDefinitionId());
						item.put(MobileServerConstants.WF_JSON_KEY_INSTANCEID, task.getProcessInstanceId());
						item.put(MobileServerConstants.WF_JSON_KEY_EXCUTIONID, task.getExecutionId());
						item.put(MobileServerConstants.WF_JSON_KEY_TASKID, task.getId());
						items.add(item);
					}
				}
				total.put(MobileServerConstants.WF_JSON_KEY_TOTAL, histTaskList.size());
				total.put(MobileServerConstants.WF_JSON_KEY_CURPAGE, page.getCurPageNo());
				total.put(MobileServerConstants.WF_JSON_KEY_PAGESIZE, page.getPageSize());
				total.put(MobileServerConstants.WF_JSON_KEY_TOTALPAGES, totalNum);
				total.put(MobileServerConstants.WF_JSON_KEY_TOTAL_RECORDS, totalRecord);
				// 如果当前页数超过总页数
				if (page.getCurPageNo() > totalNum) {
					total.put(MobileServerConstants.WF_JSON_KEY_TOTAL_DATAROWS, "");
				} else {
					total.put(MobileServerConstants.WF_JSON_KEY_TOTAL_DATAROWS, items);
				}
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			}
		}
	}
	
	/**
	 * 获得通知传阅列表 
	 * @param page
	 * @param sessionId
	 * @param taskOwner_query
	 * @param taskName_query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getNoticeListJson(Page page, String taskName_query) {
		UserContext userContext = UserContextUtil.getInstance()
				.getCurrentUserContext();
		int totalRecord = 0; // 总记录行数
		int totalNum = 0; // 总记录页数
		StringBuffer jsonHtml = new StringBuffer();
		Map<String, Object> total = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		if (null == userContext) {
			return null;
		} else {
			String userid = userContext.get_userModel().getUserid();
			if (null == userid) {
				return null;
			} else {
				//获得用户待办任务(不包含分页)
				List<ProcessRuCc> list = processRuntimeCcDAO.getUserCCList(userid);
				//默认按发起时间排序
				String orderByStr = "ccTime";
				if(list!=null){
					totalRecord = list.size();
					BigDecimal b1 = new BigDecimal(totalRecord); 
					BigDecimal b2 = new BigDecimal(page.getPageSize()); 
					totalNum =  b1.divide(b2,0,BigDecimal.ROUND_UP).intValue();  //计算页数  向上取整
					int startRow =  page.getPageSize()*(page.getCurPageNo()-1);
					//获得用户待办任务(包含分页)
					List<ProcessRuCc> list_includePage = processRuntimeCcDAO.getUserCCList(null, userid, null, taskName_query, page.getPageSize(), startRow,orderByStr, page.getOrder() );
					for(ProcessRuCc task:list_includePage){ 
						boolean isread = false;  //判断当前任务是否已读
						Map<String,Object> item = new HashMap<String,Object>();
						
						if(task.getReadtime()==null){
							item.put(MobileServerConstants.WF_JSON_KEY_TYPE,"未读");
							isread = false;
						}else{
							item.put(MobileServerConstants.WF_JSON_KEY_TYPE,"通知传阅");
							isread = true;
						}
						//装载发起人
						if(task.getCcUser()!=null){
							String owner = task.getCcUser();
							UserContext ownerContext = UserContextUtil.getInstance().getUserContext(owner);
							item.put(MobileServerConstants.WF_JSON_KEY_OWNER,ownerContext._userModel.getUsername());
						}
						else
							item.put(MobileServerConstants.WF_JSON_KEY_OWNER,"");
						//装载标题
						if(task.getTitle()!=null){
//							item.put(MobileServerConstants.WF_JSON_KEY_TITLE,ProcessTaskUtil.getIsReadStr(isread, task.getTitle()));
							item.put(MobileServerConstants.WF_JSON_KEY_TITLE,task.getTitle());
						}else{
							item.put(MobileServerConstants.WF_JSON_KEY_TITLE,"");
						}
						//装载节点名称
						item.put(MobileServerConstants.WF_JSON_KEY_STEPNAME,task.getActStepId());
						//装载创建时间
						if(task.getCcTime()!=null){
//							item.put(MobileServerConstants.WF_JSON_KEY_CREATETIME,ProcessTaskUtil.getIsReadStr(isread, getSimpleTime(task.getCcTime())));
							item.put(MobileServerConstants.WF_JSON_KEY_CREATETIME,getSimpleTime(task.getCcTime()));
						}else{
							item.put(MobileServerConstants.WF_JSON_KEY_CREATETIME,"");
						}
						//装在任务参数
						item.put("isReak",isread); 
						item.put("actDefId",task.getActDefId());
						item.put("instanceId",task.getInstanceid());
						item.put("excutionId",task.getExcutionid());
						item.put("taskId",task.getTaskid());
						item.put("dataid",task.getId());
						items.add(item);
					}
				}
				total.put("total", totalRecord);
				total.put("curPage", page.getCurPageNo());
				total.put("pageSize", page.getPageSize());
				total.put("totalPages",totalNum);
				total.put("totalRecords", totalRecord);
				total.put("dataRows", items);
				
				JSONArray json = JSONArray.fromObject(total);
				jsonHtml.append(json);
				return jsonHtml.toString();
			}
		}
	}
	/**
	 * 获取已办跟踪任务列表
	 * @param from
	 * @param title
	 * @param isCreate
	 * @param processStatus
	 * @param pageSize
	 * @param curPageNo
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public String getFinishMonitorListJson(String from, String title, Long isCreate, Long processStatus,Long pageSize,Long curPageNo) {
		String json = "";
		if(pageSize!=null&&curPageNo!=null){
			Page page = new Page();
			page.setPageSize(pageSize.intValue());
			page.setCurPageNo(curPageNo.intValue());
			page.setOrderBy(""); 
			//json = processDeskService.getHistoryTaskJson(from, title, isCreate, processStatus, page);
		}
		return json;
	}

	/**
	 * 将时间转换为MM-dd HH:mm 格式 并显示"今天"、"昨天"
	 * 
	 * @param date
	 * @return
	 */
	public static String getSimpleTime(Date old_date) {
		String dateStr = "";
		SimpleDateFormat mdhmf = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat hmf = new SimpleDateFormat("HH:mm");
		SimpleDateFormat mdf = new SimpleDateFormat("MM-dd");
		String tadayDateStr = mdf.format(new Date());
		String old_dateStr = mdf.format(old_date);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);
		String yestodayDateStr = mdf.format(c.getTime());
		if (null != tadayDateStr && null != old_dateStr
				
				&& tadayDateStr.equals(old_dateStr)) {
			dateStr = "今天  " + hmf.format(old_date);
		} else if (null != yestodayDateStr && null != old_dateStr
				&& yestodayDateStr.equals(old_dateStr)) {
			dateStr = "昨天  " + hmf.format(old_date);
		} else {
			dateStr = mdhmf.format(old_date);
		}
		return dateStr;
	}
	
	/**
	 * 将时间转换为MM-dd HH:mm 格式 并显示"今天"、"昨天"
	 * @param old_date 输入字符串格式:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getSimpleTime(String old_date_str){
		SimpleDateFormat ymdhmsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date old_date = new Date();
		try {
			old_date = ymdhmsf.parse(old_date_str);
			return getSimpleTime(old_date);
		} catch (ParseException e) {
			logger.error(e,e);
			return "";
		}
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public ProcessRuntimeCcDAO getProcessRuntimeCcDAO() {
		return processRuntimeCcDAO;
	}

	public void setProcessRuntimeCcDAO(ProcessRuntimeCcDAO processRuntimeCcDAO) {
		this.processRuntimeCcDAO = processRuntimeCcDAO;
	}

	public ProcessDeskService getProcessDeskService() {
		return processDeskService;
	}

	public void setProcessDeskService(ProcessDeskService processDeskService) {
		this.processDeskService = processDeskService;
	}
	
}
