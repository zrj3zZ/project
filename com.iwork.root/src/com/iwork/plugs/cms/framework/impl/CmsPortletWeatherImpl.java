package com.iwork.plugs.cms.framework.impl;

import java.util.HashMap;

import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.weather.bean.CmsWeatherModel;
import com.iwork.plugs.weather.util.WeatherUtil;

/**
 * CMS链接页面实现类 天气预报
 * 
 * @author WeiGuangjian
 * 
 */
public class CmsPortletWeatherImpl implements CmsPortletInterface {

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {

		StringBuffer html = new StringBuffer();
		String sql = "SELECT template FROM IWORK_CMS_PORTLET where portletkey = 'Stock'";
		String cityName = DBUtil.getString(sql, "template");
		if(cityName == null){
			cityName = "北京";
		}
		CmsWeatherModel model = WeatherUtil.getInstance().getCmsWeatherModelByCityName(cityName);
		html.append("<div style=\"padding-left:10px;color:#666;float:left;width: 92%;padding-bottom: 5px;\">");
		html.append("<div style=\"font-weight:bold\">" + model.getCity() + "</div>\n").append("<br/>");
		html.append("今日温度：").append(model.getHighest()).append("℃").append("&nbsp;～&nbsp;").append(model.getMinimum()).append("℃").append("<br/>");
		html.append("天气情况：").append(model.getWeather()).append("&nbsp;&nbsp;&nbsp;").append(model.getZwx_s()).append("<br/>").append("<br/>");;
		html.append("穿衣说明：").append(model.getChy_shuoming()).append("<br/>").append("<br/>");
		html.append("活动建议：").append(model.getSsd_s()).append("<br/>").append("<br/>");
		html.append("洗车注意：").append(model.getXcz_s()).append("<br/>").append("<br/>");
		html.append("</div>");
		html.append("</div>");
		return html.toString();
	}

	public String portletWeiXinPage(UserContext me, DomainModel domainModel,IworkCmsPortlet infoModel, HashMap params) {
		
		return "";
	}
}
