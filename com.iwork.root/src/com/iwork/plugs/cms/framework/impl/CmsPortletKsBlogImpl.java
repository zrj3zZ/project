package com.iwork.plugs.cms.framework.impl;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class CmsPortletKsBlogImpl implements CmsPortletInterface {

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		int line = 10;
		String url = "http://news.baidu.com/n?cmd=1&class=internet&tn=rss";
		String result = this.indexFeedReader(line, url);
		return result;
	}

	/**
	 * 获取RSS订阅信息XML并解析
	 * 
	 * @param line
	 * @param url
	 * @return
	 */
	private static String indexFeedReader(int line, String url) {
		if (line > 10) { 
			line = 10;// 默认显示行数
		}
		StringBuffer sb = new StringBuffer();
		try {
			URLConnection feedUrl = new URL(url).openConnection();
			feedUrl.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			List list = feed.getEntries();
			sb.append("<ul class=\"item_task\" >\n");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (i == line) {
						break;
					} else {
						SyndEntry entry = (SyndEntry) list.get(i);
						String author = entry.getAuthor();
						Date date = entry.getPublishedDate();
						if (date == null) {
							sb.append("<li>");
							sb.append("<a href='")
									.append(entry.getLink())
									.append("' target='_blank' title='")
									.append(entry.getTitle()).append("'>")
									.append(entry.getTitle())
									.append("</a>");
							if(author!=null&&author.length()>6){
								author = author.substring(0,6);
							}
							
							sb.append("<span class=\"time\" title=\"")
									.append(entry.getAuthor()).append("\">")
									.append(author).append("</span>");
							sb.append("</li>");
						} else if (author == null || "".equals(author.trim())) {
							sb.append("<li>\n");
							sb.append("<a href='")
									.append(entry.getLink())
									.append("' target='_blank' title='")
									.append(entry.getTitle()).append("'>")
									.append(entry.getTitle())
									.append("</a>");
							sb.append("<span class=\"time\" >")
									.append(UtilDate.dateFormat(entry
											.getPublishedDate()))
									.append("&nbsp;</span>\n");
							sb.append("</li>\n");
						} else {
							sb.append("<li>\n");
							sb.append("<a href='")
									.append(entry.getLink())
									.append("' target='_blank' title='")
									.append(entry.getTitle()).append("'>")
									.append(entry.getTitle())
									.append("</a>");
							// sb.append("<span title=\"")
							// .append(entry.getAuthor()).append("\">")
							// .append(entry.getAuthor()).append("</span>");
							sb.append("<span class=\"time\" >")
									.append(UtilDate.dateFormat(entry
											.getPublishedDate()))
									.append("&nbsp;</span>\n");
							sb.append("</li>\n");
						}
					}
				}
			} else {
				sb.append("<li>没有数据</li>");
			}
			sb.append("</ul>");
		} catch (Exception e) {
			sb.append("<br><br><div align=center><font color=gray>读RSS数据时失败</font></div><br><br>");	
		}
		return sb.toString();
	}

	public String portletWeiXinPage(UserContext me, DomainModel domainModel,IworkCmsPortlet infoModel, HashMap params) {
		
		return "";
	}
}
