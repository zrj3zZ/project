package com.iwork.app.schedule.quartz;

import java.io.Serializable;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.CronTriggerBean;
import com.iwork.app.schedule.service.SysScheduleService;
import com.iwork.core.util.SpringBeanUtil;
import org.apache.log4j.Logger;

public class ScheduleQuartzJob extends CronTriggerBean implements Job,Serializable {

	private static final long serialVersionUID = 8932888379464350550L;
	private static Logger logger = Logger.getLogger(ScheduleQuartzJob.class);
    public ScheduleQuartzJob() {
    }

    @SuppressWarnings("static-access")
	public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
        	//获得所需参数
            JobDataMap data = context.getJobDetail().getJobDataMap();
            String id= data.getString("id");
            String flag = data.get("flag").toString();
            String repeatNum = data.get("repeatNum").toString();
            String executeType = "0";//自动执行
            SysScheduleService sysScheduleService =(SysScheduleService)SpringBeanUtil.getBean("sysScheduleService");
            sysScheduleService.simulate(id, flag, executeType,repeatNum);
        } catch (Exception e ) {
        	logger.error(e,e);
        }
        
    }
}
