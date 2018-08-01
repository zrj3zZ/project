package com.iwork.app.schedule.quartz;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;

import com.iwork.app.schedule.model.SysSchedule;
import com.iwork.app.schedule.service.SysScheduleService;
import com.iwork.core.util.SpringBeanUtil;

public class AutoExecuteSchedule extends CronTriggerBean implements Serializable{
	private static Logger logger = Logger.getLogger(AutoExecuteSchedule.class);
	private static final long serialVersionUID = -3121870088867769992L;
	private SysScheduleService sysScheduleService;
	
	public AutoExecuteSchedule(){
		
	}
	
	/** 
     * 容器启动时初始化任务 
     * @throws SchedulerException  
     * @throws ParseException  
     */ 
	@SuppressWarnings({ "unchecked", "static-access" })
	public void execute() throws SchedulerException, ParseException {
		
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();  
		Scheduler scheduler = schedulerFactory.getScheduler();  

		SysScheduleService sysScheduleService =(SysScheduleService)SpringBeanUtil.getBean("sysScheduleService");
		if(sysScheduleService==null)return ;
		List scheduleList = sysScheduleService.getIsDisabledSchedule();
		if(null != scheduleList && scheduleList.size() > 0){  
			Iterator ite = scheduleList.iterator();  
			while(ite.hasNext()){  
				String cronExpression = "0/5 * * * * ?";

				//任务对象  
				SysSchedule schedule = (SysSchedule)ite.next();  
				int ruleType = schedule.getRuleType();//执行类型,每天、每周、每月、每季度、服务启动时
				String executeTime = schedule.getExecuteTime();  //执行时间

				String executePoint = schedule.getExecutePoint();

				String intervalMinutes = schedule.getIntervalMinutes();//间隔时间
				//将执行时间转换为CronExpression类型
				cronExpression = changeFormat(ruleType, executeTime, executePoint, intervalMinutes);
				//新建任务，任务组为默认的Scheduler.DEFAULT_GROUP，需要执行的任务类为ScheduleQuartzJob.class  
//				JobDetail jobDetail =  new JobDetail("job_" + UUID.randomUUID(), Scheduler.DEFAULT_GROUP,ScheduleQuartzJob.class);  
				JobDetail jobDetail =  new JobDetail("job_" + schedule.getId(), Scheduler.DEFAULT_GROUP,ScheduleQuartzJob.class);  
				//新建触发器，触发器为默认的Scheduler.DEFAULT_GROUP  
//				CronTrigger cronTrigger = new CronTrigger("trigger_" + UUID.randomUUID(), Scheduler.DEFAULT_GROUP); 
				CronTrigger cronTrigger = new CronTrigger("trigger_" + schedule.getId(), Scheduler.DEFAULT_GROUP); 
				//传参数
				jobDetail.getJobDataMap().put("id", schedule.getId());  
				jobDetail.getJobDataMap().put("flag", schedule.getIsDisabled());
				jobDetail.getJobDataMap().put("repeatNum", schedule.getRepeatNum());
				//为触发器设置定时表达式  
				cronTrigger.setCronExpression(cronExpression); 
				cronTrigger.setPriority(schedule.getPlanPri());//设定优先级
				try{  
					//启动新增定时器任务   
					scheduler.scheduleJob(jobDetail, cronTrigger);
				}catch(SchedulerException e){  
					logger.error(e,e);
				} 
			}  
		}  
		//初始化任务只需要执行一次，执行一次后移除初始化触发器  
		scheduler.unscheduleJob("cronTrigger", Scheduler.DEFAULT_GROUP);  
		//任务启动 
		if (!scheduler.isShutdown()) {
			scheduler.start(); 
		}
	}
	
	/**
	 * 转换执行时间格式
	 * @param ruleType			执行频率
	 * @param executeTime		执行时间
	 * @param executePoint		执行日期
	 * @param intervalMinutes	执行时间间隔
	 * @return String
	 */
	public String changeFormat(int ruleType, String executeTime, String executePoint, String intervalMinutes) {
		
		StringBuffer cronExpression = new StringBuffer("0 ");
		String hour = "";
		String minute = "";
		
		if (null != executeTime && !"".equals(executeTime)) {
			hour = executeTime.substring(0, 2);//取得执行时间的小时
			minute = executeTime.substring(2,4);//取得执行时间的分钟
		}

		if (ruleType == 0) {//每天
			cronExpression.append(minute).append(" ").append(hour).append(" * * ?");
		} else if (ruleType == 1) {//每周
			cronExpression.append(minute).append(" ").append(hour).append(" ? * ").append(executePoint);
		} else if (ruleType == 2) {//每月
			cronExpression.append(minute).append(" ").append(hour).append(" ").append(executePoint).append(" * ?");
		} else if (ruleType == 3) {//每季度
			String[] date = executePoint.split(",");
			String monthOfQuarter = "";
			String dayOfQuarterMonth = date[1];
			if ("1".equals(date[0])) {//每季度的第一个月，就是指1、4、7、10月
				monthOfQuarter = "1,4,7,10";
			} else if ("2".equals(date[0])) {//每季度的第二个月，就是指2、5、8、11
				monthOfQuarter = "2,5,8,11";
			} else if ("3".equals(date[0])) {//每季度的第三个月，就是指3、6、9、12
				monthOfQuarter = "3,6,9,12";
			}
			cronExpression.append(minute).append(" ").append(hour).append(" ").append(dayOfQuarterMonth).append(" ").append(monthOfQuarter).append(" ?");
		} else if (ruleType == 4) {//每年
			String[] date = executePoint.split(",");
			cronExpression.append(minute).append(" ").append(hour).append(" ").append(date[1]).append(" ").append(date[0]).append(" ? *");
		} else if (ruleType == 5) {//服务启动时
			cronExpression.append("0/").append(intervalMinutes).append(" * * * ?");
		}
		
		return cronExpression.toString();
	}

	public SysScheduleService getSysScheduleService() {
		return sysScheduleService;
	}

	public void setSysScheduleService(SysScheduleService sysScheduleService) {
		this.sysScheduleService = sysScheduleService;
	}
}
