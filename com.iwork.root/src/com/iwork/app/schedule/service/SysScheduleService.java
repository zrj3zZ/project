package com.iwork.app.schedule.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import net.sf.json.JSONArray;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.app.schedule.dao.SysScheduleDAO;
import com.iwork.app.schedule.dao.SysScheduleLogDAO;
import com.iwork.app.schedule.log.model.SysScheduleLog;
import com.iwork.app.schedule.model.SysSchedule;
import com.iwork.app.schedule.quartz.AutoExecuteSchedule;
import com.iwork.app.schedule.quartz.ScheduleQuartzJob;
import com.iwork.app.schedule.util.IWorkScheduleUtil;
import com.iwork.commons.constant.SecurityConstant;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.util.SequenceUtil;
import org.apache.log4j.Logger;
/**
 * 系统计划任务Service
 * @author LuoChuan
 *
 */
public class SysScheduleService {
	private static Logger logger = Logger.getLogger(SysScheduleService.class);
	private SysScheduleDAO sysScheduleDAO; 
	private SysScheduleLogDAO sysScheduleLogDAO;

	/**
	 * 获得系统计划任务
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getSchedule() {
		
		String imagePath = "";
		String status = "";
		String ruleType = "";
		String operate ="";
		String executeTime = "";
		String hour = "";
		String minute = "";
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		
		List scheduleList = sysScheduleDAO.getAllData();		
		for (int i = 0;i < scheduleList.size();i++) {
			SysSchedule sysSchedule = (SysSchedule)scheduleList.get(i);
			Map<String,Object> rows = new HashMap<String,Object>();
			
			//根据数据库的ISDISABLED字段值确定列表上状态列的显示内容
			if (0 == sysSchedule.getIsDisabled()) {
				imagePath = "<image src='iwork_img/gray.ball.gif'>";
			} else {
				imagePath = "<image src='iwork_img/green.ball.gif'>";
			}
			status = imagePath + IWorkScheduleUtil.filterString(String.valueOf(sysSchedule.getIsDisabled()), IWorkScheduleUtil.FLAG);
			//根据数据库的RULE_TYPE字段值确定列表上执行频率列的显示内容
			ruleType = IWorkScheduleUtil.filterString(String.valueOf(sysSchedule.getRuleType()), IWorkScheduleUtil.RULE_TYPE);
			//操作按钮
			operate = "<a href='' onclick=\"disableTask('" + sysSchedule.getIsDisabled() + "','" + sysSchedule.getId() + "');return false; \"><img src=iwork_img/mssetqrate.gif border=0>启停</a>" +
					  "<a href='' onclick=\"simulate('" + sysSchedule.getIsDisabled() + "','" + sysSchedule.getId() + "');return false; \"><img src=iwork_img/runonce.gif border=0>手动执行</a>" +
					  "<a href='' onclick=\"testClass('" + sysSchedule.getClassz() + "');return false; \"><img src=iwork_img/cog1.png border=0>测试</a>";
			executeTime = sysSchedule.getExecuteTime();
			if (executeTime.length()>=3) {//当执行频率为服务启动时，不需要制定执行时间，executeTime的长度为1
				hour = executeTime.substring(0, 2);//executeTime的最后两位数就是执行时间的小时数值
				minute = executeTime.substring(executeTime.length()-2);//executeTime的最后两位数就是执行时间的分钟数值
			}			
			
			rows.put("planname", sysSchedule.getPlanName());//任务名称
			rows.put("plandesc", sysSchedule.getPlanDesc());//说明
			rows.put("status", status);//状态
			rows.put("rule_type", ruleType);//执行频率
			rows.put("rule_type_value", sysSchedule.getRuleType());//执行频率value,用于打开详细页面时和radio的value比较]
			if(sysSchedule.getExecutePoint()==null){sysSchedule.setExecutePoint("");}
			rows.put("execute_point", sysSchedule.getExecutePoint());//指哪天执行改任务
			if(sysSchedule.getIntervalMinutes()==null){
				sysSchedule.setIntervalMinutes("0");
			}
			rows.put("intervalMinutes", sysSchedule.getIntervalMinutes());//时间间隔
			rows.put("hour", hour);
			rows.put("minute", minute);			
			rows.put("operate", operate);//操作
			rows.put("add_time", UtilDate.dateFormat(sysSchedule.getAddTime()));//创建日期
			rows.put("id", sysSchedule.getId());//计划任务ID
			rows.put("classz", sysSchedule.getClassz());//计划任务执行类
			rows.put("planPri", sysSchedule.getPlanPri());//优先级
			rows.put("usefulLife_start", sysSchedule.getUsefulLifeStart());//有效期开始日期
			rows.put("usefulLife_end", sysSchedule.getUsefulLifeEnd());//有效期结束日期
			rows.put("repeatNum", sysSchedule.getRepeatNum());//失败补偿次数
			item.add(rows);
		}
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append(json.toString());
//		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	/**
	 * 保存系统计划任务
	 * @param type				操作类型
	 * @param id				计划任务ID 	
	 * @param planname			计划任务名称
	 * @param planpri			计划任务优先级
	 * @param classz			计划任务执行类
	 * @param usefullife_start	有效期开始日期
	 * @param usefullife_end	有限期结束日期
	 * @param plandesc			计划任务描述
	 * @param repeatnum			失败补偿次数
	 * @param rule_type			执行频率
	 * @param execute_time		执行时间
	 * @param execute_poin		执行点
	 * @param intervalminutes	频率为服务启动时的间隔时间
	 * @throws ParseException 
	 * @throws SchedulerException 
	 */
	public void saveSchedule(String type, String id, String planName,
							 String planPri, String classz, String usefulLife_start,
							 String usefulLife_end, String planDesc, String repeatNum,
							 String ruleType, String intervalMinutes, String executeTime, String executePoint) throws SchedulerException, ParseException {
		
		repeatNum = null == repeatNum ? "" : repeatNum;
		ruleType = null == ruleType ? "" : ruleType;
		intervalMinutes = null == intervalMinutes ? "" : intervalMinutes;
		int count = 0;
		int logCount = 0;
		
		if (null != type && ("edit").equals(type)) {//编辑
			count = sysScheduleDAO.update(id, planName, planPri, classz,
					usefulLife_start, usefulLife_end, planDesc, repeatNum,
					ruleType, executeTime, executePoint, intervalMinutes, null);
			if (count != 0) {
				// 修改任务调度实例
				String flag = "3";//设为1，表示是编辑状态
				modifyJob(id, flag, ruleType, executeTime, executePoint, intervalMinutes);
			}
		} else if (null != type && ("add").equals(type)) {//添加
			SysSchedule scheduleModel = new SysSchedule();
			scheduleModel.setPlanName(planName);
			scheduleModel.setPlanPri(Integer.valueOf(planPri));
			scheduleModel.setClassz(classz);
			scheduleModel.setUsefulLifeStart(usefulLife_start);
			scheduleModel.setUsefulLifeEnd(usefulLife_end);
			scheduleModel.setPlanDesc(planDesc);
			scheduleModel.setRepeatNum(Integer.valueOf(repeatNum));
			scheduleModel.setRuleType(Integer.valueOf(ruleType));
			scheduleModel.setExecuteTime(executeTime);
			scheduleModel.setExecutePoint(executePoint);
			scheduleModel.setIntervalMinutes(intervalMinutes);
			scheduleModel.setIsSystem(0);
			scheduleModel.setIsDisabled(1);
			scheduleModel.setTaskLock("1");
			scheduleModel.setAddTime(new Date());
//			scheduleModel.setId(UUID.randomUUID().toString());
			scheduleModel.setId(String.valueOf(SequenceUtil.getInstance().getSequenceIndex(SecurityConstant.SYS_SCHEDULE)));
			sysScheduleDAO.add(scheduleModel);
			//添加一个任务实例
			addJob(scheduleModel.getId(), scheduleModel.getRepeatNum(), ruleType, executeTime, executePoint, intervalMinutes);
		} else if (null != type && ("delete").equals(type)) {//删除
			count = sysScheduleDAO.delete(id);//删除计划任务
			if (count != 0) {
				logCount = sysScheduleLogDAO.delete(id);//删除任务日志
				//删除任务调度实例
				removeJob(id);
			}
		}
	}
	
	/**
	 * 改变计划任务状态
	 * @return
	 * @throws ParseException 
	 * @throws SchedulerException 
	 */
	public void changeStatus(String flag, String id, String ruleType,
							 String intervalMinutes, String executeTime, String executePoint) throws SchedulerException, ParseException {
		int count = sysScheduleDAO.update(id, null, null, null, null, null,	null, null, null, null, null, null, flag);

		if (count != 0) {
			// 修改任务调度实例
			modifyJob(id, flag, ruleType, executeTime, executePoint, intervalMinutes);
		}
	}
	
	/**
	 * 获得计划任务Model
	 * @return SysSchedule
	 */
	@SuppressWarnings("unchecked")
	public List getIsDisabledSchedule() {
		String isDisabled = "1"; //状态为启用
		List list = sysScheduleDAO.getListByStatus(isDisabled);
		return list;
		
	}
	
	/**
	 * 模拟使用
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String simulate(String id, String flag, String executeType, String repeatNum) {
		
		SysSchedule sysSchedule = null;
		if ("1".equals(flag)) { //状态为启动
			List list = sysScheduleDAO.getListById(id);
			if (null != list && list.size() > 0) {
				sysSchedule = (SysSchedule)list.get(0);
				sysSchedule.setExecuteType(executeType);
				Thread t = new Thread(new IWorkScheduleEngine(sysSchedule),"任务计划正在执行["+sysSchedule.getPlanName()+"]");
				t.setPriority(sysSchedule.getPlanPri());//线程优先级
				t.start();
			}
		} 
		
		return null;
	}
	
	/**
	 * 执行测试类
	 * @return
	 */
	public int testClass(String classz) {
		int flag = 0;
		try {
			Class classzz = Class.forName(classz);
			Class[] clzz = classzz.getInterfaces();
			for (int i = 0; i < clzz.length; i++) {
				String interfaceName = clzz[i].getName();
				if (interfaceName.equals("com.iwork.app.schedule.IWorkScheduleInterface")) {//执行类必须继承自该接口
					return 2;
				}
			}
		} catch (ClassNotFoundException e) {logger.error(e,e);
			return flag = 0;
		}
		return flag = 1;
	
	}
	
	/**
	 * 将list转换为model
	 * @param List
	 * @return SysSchedule
	 */
	@SuppressWarnings("unchecked")
	public SysSchedule list2model(List list) {
		//TODO
		SysSchedule sysSchedule = (SysSchedule)list.get(0);
		return sysSchedule;
		
	}
	/**
	 * 获取内容列表JS
	 * @param id
	 * @return
	 */
	public String getGridScript(String id){
		StringBuffer script = new StringBuffer();
		script.append("<script>").append("\n");
		script.append("jQuery(\"#sysscheduleloggrid\").jqGrid({").append("\n");
		script.append("   	url:'sys_schedule_getLogList.action?id=").append(id).append("',").append("\n");//获得数据URL
		script.append("		datatype: \"json\",").append("\n");
		script.append("		mtype: \"POST\",").append("\n");
		script.append("		autowidth:true,").append("\n");
		script.append("   	colNames:").append(this.getLogColumTitle()).append(",").append("\n");//获得列标题
		script.append("   	colModel:").append(this.getLogColumModel()).append(",").append("\n");
		script.append("   	rowNum:20,").append("\n");
		script.append("   	rowList:[20,40,60,80],").append("\n");
		script.append("   	multiselect: false,").append("\n");
		script.append("   	pager: '#prowed_info_grid',").append("\n");
		script.append("   	prmNames:{rows:\"page.pageSize\",page:\"page.curPageNo\",sort:\"page.orderBy\",order:\"page.order\"},").append("\n");
		script.append("     jsonReader: {").append("\n");
		script.append("     	root: \"dataRows\",").append("\n");
		script.append("     	page: \"curPage\",").append("\n");
		script.append("     	total: \"totalPages\",").append("\n");
		script.append("     	records: \"totalRecords\",").append("\n");
		script.append("     	repeatitems: false,").append("\n");
		script.append("     	userdata: \"userdata\"").append("\n");
		script.append("    },").append("\n");
		script.append("    	viewrecords: true,").append("\n");
		script.append("    	resizable:true,").append("\n");
		script.append("     height: \"380\"").append("\n");
		script.append("});").append("\n"); 
		script.append("jQuery(\"#sysscheduleloggrid\").jqGrid('navGrid',\"#prowed_info_grid").append("\",{edit:false,closeOnEscape:true,add:false,del:false,search:false});").append("\n");
		script.append("</script>").append("\n");
		return script.toString();
	}
	
	/**
	 * 列表标题
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getLogColumTitle(){
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		item.add("任务名称");
		item.add("日志信息");
		item.add("状态");
		item.add("执行时间");
		item.add("结束时间");
		item.add("执行方式");
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 列表模型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getLogColumModel(){
		StringBuffer html = new StringBuffer();
		List item = new ArrayList();
		
		//装载任务名称
		Map planName = new HashMap();
		planName.put("name","planname");
		planName.put("index","1");
		planName.put("width","20");	
		item.add(planName);
		
		//装载日志信息
		Map memo = new HashMap();
		memo.put("name", "memo");
		memo.put("index","2"); 
		memo.put("width", "50");
		item.add(memo);
		
		//装载状态
		Map status = new HashMap();
		status.put("name", "status");
		status.put("index","3"); 
		status.put("width", "10");
		status.put("align", "center");
		item.add(status);
		
		//装载执行时间	
		Map startTime = new HashMap();
		startTime.put("name", "starttime");
		startTime.put("index","4"); 
		startTime.put("width", "15");
		item.add(startTime);
		
		//装载结束时间
		Map endTime = new HashMap();
		endTime.put("name", "endtime");
		endTime.put("index","5"); 
		endTime.put("width", "15");
		item.add(endTime);
		
		//装载执行方式		
		Map executeType = new HashMap();
		executeType.put("name", "type");
		executeType.put("index","6"); 
		executeType.put("width", "10");
		executeType.put("align", "center");
		item.add(executeType);
		
//		rownum = new HashMap();
		JSONArray json = JSONArray.fromObject(item);
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 查看日志
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String showLog(String id, Page page) {
		
		StringBuffer jsonHtml = new StringBuffer();
		String status = "";
		String type = "";
		SysSchedule schedule = new SysSchedule();
		int totalRecord = 0;   //总记录行数
		int totalNum = 0;   //总记录页数
		int count = 0;
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		Map<String,Object> total = new HashMap<String,Object>();
		
		List allLogList = sysScheduleLogDAO.getLogByCondition(id, null, null, null);//没有进行分页，只需要取出总条数
		List scheduleList = sysScheduleDAO.getListById(id);
		//获取总行数
		totalRecord = allLogList.size();
		BigDecimal b1 = new BigDecimal(totalRecord); 
		BigDecimal b2 = new BigDecimal(page.getPageSize()); 
		totalNum =  b1.divide(b2,0,BigDecimal.ROUND_UP).intValue();  //计算页数  向上取整
		int startRow =  page.getPageSize()*(page.getCurPageNo()-1);
		count = startRow;
		List logList = sysScheduleLogDAO.getLogByCondition(id, null, String.valueOf(page.getPageSize()), String.valueOf(startRow));//进行分页
		if (null != scheduleList && scheduleList.size()>0) {
			schedule = (SysSchedule)scheduleList.get(0);
		}
		
		for (int i=0;i < logList.size();i++) {
			SysScheduleLog log = (SysScheduleLog)logList.get(i);
			Map<String,Object> rows = new HashMap<String,Object>();
			status = IWorkScheduleUtil.filterString(log.getStatus(), IWorkScheduleUtil.STATUS);
			type = IWorkScheduleUtil.filterString(log.getExecuteType(), IWorkScheduleUtil.EXECUTE_TYPE);
			
			rows.put("planname", schedule.getPlanName());//计划名称
			rows.put("memo", log.getLogMemo());//日志信息
			rows.put("status", status);//状态
			rows.put("starttime", log.getExecuteTime());//执行开始时间
//			rows.put("starttime", UtilDate.getTimes(log.getExecuteTime(),"yyyy-MM-dd HH:mm:ss"));//执行开始时间
			rows.put("endtime", log.getEndTime());//执行结束时间
			rows.put("type", type);//执行方式
			item.add(rows);
		}

		total.put("total", logList.size());
		total.put("curPage", page.getCurPageNo());
		total.put("pageSize", page.getPageSize()); 
		total.put("totalPages",totalNum);
		total.put("totalRecords", totalRecord);
		total.put("dataRows", item);

		JSONArray json = JSONArray.fromObject(total);
//		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		jsonHtml.append(json.toString());
		return jsonHtml.toString();
		
	}
	
	/**
     *  添加一个定时任务，使用默认的任务组名触发器组名
     * @param id 					计划任务ID
     * @param ruleType      		执行频率
     * @param executeTime   		执行时间
     * @param executePoint			执行日期
     * @param intervalMinutes		间隔时间
     * @throws SchedulerException
     * @throws ParseException
     */
    public void addJob(String id, int repeatNum, String ruleType, String executeTime, String executePoint, String intervalMinutes) throws SchedulerException, ParseException {
        
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();  
		Scheduler scheduler = schedulerFactory.getScheduler(); 
		String cronExpression = "";
		//任务
        JobDetail jobDetail = new JobDetail("job_" + id, Scheduler.DEFAULT_GROUP,ScheduleQuartzJob.class);//任务名，任务组，任务执行类
        //触发器
        CronTrigger trigger = new CronTrigger("trigger_" + id, Scheduler.DEFAULT_GROUP);//触发器名,触发器组
        AutoExecuteSchedule sch = new AutoExecuteSchedule();
        cronExpression = sch.changeFormat(Integer.valueOf(ruleType), executeTime, executePoint, intervalMinutes);
        //传参数
        jobDetail.getJobDataMap().put("id", id);  
		jobDetail.getJobDataMap().put("flag", "1");
		jobDetail.getJobDataMap().put("repeatNum", repeatNum);
        trigger.setCronExpression(cronExpression);//触发器时间设定
        scheduler.scheduleJob(jobDetail, trigger);
        //启动
        if (!scheduler.isShutdown())
        	scheduler.start();
    }
	
    /**
     * 修改一个任务的触发时间(使用默认的任务组名，触发器组名)
     * @param id 					计划任务ID
     * @param ruleType      		执行频率
     * @param executeTime   		执行时间
     * @param executePoint			执行日期
     * @param intervalMinutes		间隔时间
     * @throws SchedulerException
     * @throws ParseException
     */
    public void modifyJob(String id, String flag, String ruleType, String executeTime, String executePoint, String intervalMinutes) throws SchedulerException, ParseException {

    	SchedulerFactory schedulerFactory = new StdSchedulerFactory();  
		Scheduler scheduler = schedulerFactory.getScheduler(); 
		String cronExpression = "";
//		JobDetail job = scheduler.getJobDetail("job_"+id, Scheduler.DEFAULT_GROUP);
        Trigger trigger = scheduler.getTrigger("trigger_" + id, Scheduler.DEFAULT_GROUP);
        if (trigger != null) {
            CronTrigger ct = (CronTrigger) trigger;
            AutoExecuteSchedule sch = new AutoExecuteSchedule();
            cronExpression = sch.changeFormat(Integer.valueOf(ruleType), executeTime, executePoint, intervalMinutes);
            ct.setCronExpression(cronExpression);
            if ("1".equals(flag)) {//启用计划任务，重启触发器
            	 scheduler.resumeTrigger("trigger_" + id, Scheduler.DEFAULT_GROUP);
            } else if ("0".equals(flag)) {//停用计划任务，停止触发器
            	scheduler.pauseTrigger("trigger_" + id, Scheduler.DEFAULT_GROUP);
            } else if ("3".equals(flag)) {//编辑计划任务，重新加载触发器
            	scheduler.rescheduleJob(ct.getName(), Scheduler.DEFAULT_GROUP, ct);
            }
            
        }
    }
    
    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * @param jobName
     * @throws SchedulerException
     */
    public void removeJob(String id) throws SchedulerException {
    	
    	SchedulerFactory schedulerFactory = new StdSchedulerFactory();  
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.pauseTrigger("job_" + id, Scheduler.DEFAULT_GROUP);//停止触发器
		scheduler.unscheduleJob("job_" + id, Scheduler.DEFAULT_GROUP);//移除触发器
		scheduler.deleteJob("job_" + id, Scheduler.DEFAULT_GROUP);//删除任务
    }
    
	public SysScheduleDAO getSysScheduleDAO() {
		return sysScheduleDAO;
	}

	public void setSysScheduleDAO(SysScheduleDAO sysScheduleDAO) {
		this.sysScheduleDAO = sysScheduleDAO;
	}
	
 	public class IWorkScheduleEngine extends TimerTask implements Runnable {
		private SysSchedule model;
		private int repeatNum;

		/**
		 * @param model 一个SysSchedule类
		 */
		public IWorkScheduleEngine(SysSchedule model) {
			super();
			this.model = model;
			this.repeatNum = model.getRepeatNum();
		}
		
		public IWorkScheduleEngine(SysSchedule model, SysScheduleDAO dao, SysScheduleLogDAO logDao) {
			super();
			this.model = model;
			sysScheduleDAO = dao;
			sysScheduleLogDAO = logDao;
		}
		
		public void run() {// 执行一次
			try {
				execute();
			} catch (ScheduleException e) {
				logger.error(e,e);
			}
		}

		/**
		 * 执行一个任务
		 * 
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private synchronized void execute() throws ScheduleException {

			String statusMsg = "成功";
			String status = "1"; // 1 正常 2 异常抛出 3 执行正常结束
			String start = UtilDate.getNowDatetime();
			
			// 加载实现类 classz
			try {
				String taskId = model.getId();
				boolean lock = true;
				if (lock) {
					lock = false; // 设置任务锁为flase，关闭当前任务执行代码
					String date = UtilDate.getNowdate();
//					String dbstat = "";
//					String executeType = "";
//					List logList = sysScheduleLogDAO.getLogByCondition(taskId, date);
//					for (int i = 0; i < logList.size(); i++) {
//						SysScheduleLog model = (SysScheduleLog) logList.get(i);
//						dbstat = model.getStatus();
//						executeType = model.getExecuteType();
//						break;
//					}
					
					Boolean executeSuccess = false;
					int executeCount = 0;
					while (executeSuccess == false && executeCount <= this.repeatNum) {
						try {
							IWorkScheduleInterface plan = (IWorkScheduleInterface) Class.forName(model.getClassz()).newInstance(); // 此处应该是加载的类实例
							if (plan.executeBefore()) {
								if (plan.executeOn()) {
									plan.executeAfter();
									executeSuccess = true;
								} else {
									statusMsg = "终止";
									status = "3";
									executeCount++;
								}
							} else {
								statusMsg = "终止";
								status = "3";
								executeCount++;
							}
						} catch(Exception e) {logger.error(e,e);
							executeCount++;
						}
					}
					
					Thread.sleep(1000);
					lock = sysScheduleLogDAO.unlock(taskId);; // 设置任务锁为true，打开当前任务执行代码
				} else { // 没有拿到任务锁，直接返回，不操作。
					return;
				}
			} catch (Exception e) {
				logger.error(e,e);
				statusMsg = "异常:" + e.getMessage();
				status = "2";
			}
			String end = UtilDate.getNowDatetime();
			// 写入到日志表
			SysScheduleLog sysScheduleLog = recordLog(model, start, end,statusMsg, status);
			model.setSysScheduleLog(sysScheduleLog);
		}

		public SysScheduleLog recordLog(SysSchedule model, String startTime,String endTime, String statusMsg, String status) {
			
			SysScheduleLog logModel = new SysScheduleLog();
			
			logModel.setId(String.valueOf(SequenceUtil.getInstance().getSequenceIndex(SecurityConstant.SYS_SCHEDULE_LOG)));
			logModel.setScheduleId(model.getId());
			logModel.setExecuteTime(String.valueOf(startTime));
			logModel.setEndTime(String.valueOf(endTime));
			logModel.setLogMemo("信息：[" + UtilDate.datetimeFormat(new Date()) + "]一个任务被系统调度执行[任务名=" + model.getPlanName() + "][状态="
										 + statusMsg + "][开始时间=" + startTime + "][结束时间=" + endTime);
			logModel.setExecuteType(model.getExecuteType());
			logModel.setLogType(model.getExecuteType());
			logModel.setStatus(status);
			
			sysScheduleLogDAO.add(logModel);
			return logModel;
		}
	}

	public SysScheduleLogDAO getSysScheduleLogDAO() {
		return sysScheduleLogDAO;
	}

	public void setSysScheduleLogDAO(SysScheduleLogDAO sysScheduleLogDAO) {
		this.sysScheduleLogDAO = sysScheduleLogDAO;
	}
}
