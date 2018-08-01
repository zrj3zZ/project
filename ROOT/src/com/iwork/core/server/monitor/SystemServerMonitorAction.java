package com.iwork.core.server.monitor;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.server.monitor.service.SystemServerMonitorService;
import com.iwork.core.util.JVMInfo;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemServerMonitorAction extends ActionSupport
{
  private static final Logger logger = LoggerFactory.getLogger(SystemServerMonitorAction.class);
  private String list;
  private String currentTime;
  private String freeM;
  private String maxM;
  private String tM;
  private DataSource dataSource;
  private SystemServerMonitorService systemServerMonitorService;

  public String showSysThreadMonitorWeb()
  {
    Map activeThreads = Thread.getAllStackTraces();
    StringBuffer list = new StringBuffer();
    int c = 0;
    setCurrentTime(UtilDate.getNowDatetime());
    for (Iterator it = activeThreads.keySet().iterator(); it.hasNext(); ) {
      c++;
      String bgcolor = "#ffffff";
      String icon = "iwork_img" + File.separator + "gear_16.png";
      Thread t = (Thread)it.next();
      if (t.getState().name().equals("RUNNABLE")) {
        bgcolor = "#66B3FF";
      }
      if (t.getName().indexOf("uid=[") == 0) {
        icon = "iwork_img/user_suit.png alt=来自Web的用户请求";
        bgcolor = "#93FF93";
      }
      list.append("<tr bgcolor=").append(bgcolor).append(">");
      list.append("<td><img src=").append(icon).append(">" + c + ".</td>");
      list.append("<td>").append(t.getThreadGroup().getName()).append("</td>");
      list.append("<td>").append(t.getName()).append("</td>");
      list.append("<td>").append(t.hashCode()).append("</td>");
      String status = "";
      if (t.getState() != null) {
        if (t.getState().name().equals("RUNNABLE"))
          status = "运行中";
        else if (t.getState().name().equals("WAITING"))
          status = "等待";
        else if (t.getState().name().equals("TIMED_WAITING"))
          status = "定时等待";
        else if (t.getState().name().equals("TERMINATED")) {
          status = "已退出";
        }
      }
      list.append("<td>").append(status).append("</td>");
      list.append("</tr>");
    }
    setList(list.toString());
    return "success";
  }

  public String systemDBPoolMonitor()
  {
    StringBuffer listHTML = new StringBuffer();
    this.currentTime = UtilDate.getNowDatetime();
    if (this.dataSource == null) {
      SpringBeanUtil.getInstance(); this.dataSource = ((DataSource)SpringBeanUtil.getBean("dataSource"));
    }
    if (!(this.dataSource instanceof DataSource))
    {
      listHTML.append("Not a c3p0 PooledDataSource!");
    }this.list = listHTML.toString();

    return "success";
  }

  public String getJVMInfo()
  {
    setCurrentTime(UtilDate.getNowDatetime());
    StringBuffer list = new StringBuffer();

    JVMInfo jvmInfo = new JVMInfo();
    HashMap hash = jvmInfo.getSystemInfo();
    Iterator iterator = hash.entrySet().iterator();
    int c = 0;
    Runtime lRuntime = Runtime.getRuntime();
    long freeM = lRuntime.freeMemory() / 1024L / 1024L;
    long maxM = lRuntime.maxMemory() / 1024L / 1024L;
    long tM = lRuntime.totalMemory() / 1024L / 1024L;
    OperatingSystemMXBean osmb = (OperatingSystemMXBean)
      ManagementFactory.getOperatingSystemMXBean();
    c++;
    list.append("<tr class=\"item\">");
    list.append("<td ><img src=iwork_img/arrow.png>" + c + ".</td>");
    list.append("<td >APP应用服务器最大内存:</td>");
    list.append("<td >").append(maxM).append("MB</td>");
    list.append("<td >&nbsp;</td>");
    list.append("</tr>");
    c++;
    list.append("<tr class=\"item\">");
    list.append("<td ><img src=iwork_img/arrow.png>" + c + ".</td>");
    list.append("<td >APP应用服务器可用内存:</td>");
    list.append("<td >").append(freeM).append("MB</td>");
    list.append("<td >&nbsp;</td>");
    list.append("</tr>");
    c++;
    list.append("<tr class=\"item\">");
    list.append("<td ><img src=iwork_img/arrow.png>" + c + ".</td>");
    list.append("<td >APP应用服务器一共内存:</td>");
    list.append("<td >").append(tM).append("MB</td>");
    list.append("<td >&nbsp;</td>");
    list.append("</tr>");
    c++;
    list.append("<tr class=\"item\">");
    list.append("<td ><img src=iwork_img/arrow.png>" + c + ".</td>");
    list.append("<td >系统物理内存总计:</td>");
    list.append("<td >").append(osmb.getTotalPhysicalMemorySize() / 1024L / 1024L + "MB").append("</td>");
    list.append("<td >&nbsp;</td>");
    list.append("</tr>");
    c++;
    list.append("<tr class=\"item\">");
    list.append("<td ><img src=iwork_img/arrow.png>" + c + ".</td>");
    list.append("<td >系统物理可用内存总计:</td>");
    list.append("<td >").append(osmb.getFreePhysicalMemorySize() / 1024L / 1024L + "MB").append("</td>");
    list.append("<td >&nbsp;</td>");
    list.append("</tr>");

    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)iterator.next();
      c++;
      list.append("<tr class=\"item\">");
      list.append("<td ><img src=iwork_img/arrow.png>" + c + ".</td>");
      list.append("<td >").append(entry.getKey()).append("</td>");
      list.append("<td >").append(entry.getValue()).append("</td>");
      list.append("<td >&nbsp;</td>");
      list.append("</tr>");
    }

    setList(list.toString());
    return "success";
  }

  public String getJVMInfoChar()
  {
    setCurrentTime(UtilDate.getNowDatetime());
    StringBuffer list = new StringBuffer();
    JVMInfo jvmInfo = new JVMInfo();
    HashMap hash = jvmInfo.getSystemInfo();
    Iterator iterator = hash.entrySet().iterator();
    int c = 0;
    Runtime lRuntime = Runtime.getRuntime();
    this.freeM = String.valueOf((lRuntime.totalMemory() / 1024L / 1024L));
    this.maxM = String.valueOf((lRuntime.maxMemory() / 1024L / 1024L));
    this.tM = String.valueOf((lRuntime.freeMemory() / 1024L / 1024L));
    return "success";
  }
  public void getJVMFreeJSON() {
    setCurrentTime(UtilDate.getNowDatetime());
    StringBuffer list = new StringBuffer();
    JVMInfo jvmInfo = new JVMInfo();
    HashMap hash = jvmInfo.getSystemInfo();
    Iterator iterator = hash.entrySet().iterator();
    int c = 0;
    Runtime lRuntime = Runtime.getRuntime();
    long freeM = lRuntime.freeMemory() / 1024L / 1024L;
    ResponseUtil.write(String.valueOf(freeM));
  }

  public String getServerInfoChar()
  {
    OperatingSystemMXBean osmb = (OperatingSystemMXBean)
      ManagementFactory.getOperatingSystemMXBean();
    this.freeM = String.valueOf(((osmb.getTotalPhysicalMemorySize() - osmb.getFreePhysicalMemorySize()) / 1024L / 1024L));
    this.maxM = String.valueOf((osmb.getTotalPhysicalMemorySize() / 1024L / 1024L));
    return "success";
  }
  public void getServerFreeJSON() {
    OperatingSystemMXBean osmb = (OperatingSystemMXBean)
      ManagementFactory.getOperatingSystemMXBean();
    this.freeM = String.valueOf(((osmb.getTotalPhysicalMemorySize() - osmb.getFreePhysicalMemorySize()) / 1024L / 1024L));
    ResponseUtil.write(this.freeM);
  }
  public String getOrgUserMonitor() {
    this.list = this.systemServerMonitorService.showOrgUserInfo();
    return "success";
  }
  public String getLoginInfo() {
    this.list = this.systemServerMonitorService.showLoginInfo();
    return "success";
  }
  public String getEngineModelMonitor() {
    this.list = this.systemServerMonitorService.showEngineModelMonitor();
    return "success";
  }

  public String getProcessTaskMonitor() {
    this.list = this.systemServerMonitorService.showProcessTaskMonitor();
    return "success";
  }

  private String getConnectInfo(Hashtable activeTrackList, String hashcode)
  {
    String threadInfo = "";
    for (Enumeration e = activeTrackList.keys(); e.hasMoreElements(); ) {
      threadInfo = (String)e.nextElement();
      if (threadInfo.indexOf(",hashcode=" + hashcode) > -1) {
        return threadInfo;
      }
    }
    return threadInfo;
  }

  private String getConnectDebug(Hashtable activeTrackList, String hashcode)
  {
    String debugInfo = "";
    for (Enumeration e = activeTrackList.keys(); e.hasMoreElements(); ) {
      debugInfo = (String)e.nextElement();
      if (debugInfo.indexOf(",hashcode=" + hashcode) > -1) {
        return (String)activeTrackList.get(debugInfo);
      }
    }
    return debugInfo;
  }
  public String getList() {
    return this.list;
  }
  public void setList(String list) {
    this.list = list;
  }
  public String getCurrentTime() {
    return this.currentTime;
  }
  public void setCurrentTime(String currentTime) {
    this.currentTime = currentTime;
  }

  public String getFreeM()
  {
    return this.freeM;
  }

  public String getMaxM() {
    return this.maxM;
  }

  public String gettM() {
    return this.tM;
  }
public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
  public void setSystemServerMonitorService(SystemServerMonitorService systemServerMonitorService)
  {
    this.systemServerMonitorService = systemServerMonitorService;
  }
}