package com.iwork.app.schedule.quartz;

import com.iwork.commons.util.UtilDate;

/**
 * 系统服务优化指定调度程序，每天晚上2点执行
 * 1、将过期用户，设置为注销状态
 * @author davidyang
 *
 */
public class SeverOptimizaTask{
    public void execute() {
    	
        System.out.println("Quartz的任务调度！"+UtilDate.getNowDatetime());
    }
}
