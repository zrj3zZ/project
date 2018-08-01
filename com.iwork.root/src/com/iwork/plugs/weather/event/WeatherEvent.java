package com.iwork.plugs.weather.event;

import java.util.Date;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.plugs.weather.util.WeatherUtil;

public class WeatherEvent implements IWorkScheduleInterface {

	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-天气信息抓取结束......");
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-天气信息抓取前...... ");
		WeatherUtil.getInstance().updateWeatherInfo();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-天气信息抓取中...... ");
		return true;
	}

}
