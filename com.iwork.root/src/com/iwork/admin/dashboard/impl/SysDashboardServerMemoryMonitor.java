package com.iwork.admin.dashboard.impl;

import com.iwork.admin.dashboard.SysConsoleDashboardInterface;

public class SysDashboardServerMemoryMonitor implements SysConsoleDashboardInterface {

	public String getContent() {
		StringBuffer html = new StringBuffer();
		html.append("<iframe frameborder=\"no\" scrolling=\"no\" style=\"border:0px;\" src=\"systemServerMonitorChart.action\" width=\"100%\" height=\"100%\" border=\"0\"></iframe>");
		return html.toString();
	}

}
