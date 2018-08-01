package com.iwork.plugs.rss.service;

import java.net.URL;
import org.apache.log4j.Logger;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iwork.app.persion.dao.SysPersonDAO;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.rss.dao.IworkPlugsRssDao;
import com.iwork.plugs.rss.model.BdInfRssdyjlb;
import com.iwork.plugs.rss.util.IworkPlugsRssUtil;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
public class IworkPlugsRssService {
	private static Logger logger = Logger.getLogger(IworkPlugsRssService.class);
	private IworkPlugsRssDao rssInformationDao;// rssDao
	private SysPersonDAO sysPersonDAO;// 系统配置Dao

	/**
	 * 获得用户配置信息
	 * 
	 * @return
	 */
	public String getUserProfile(String uid) {
		String userProfile = rssInformationDao.getUserProfile(uid);
		return userProfile;
	}

	/**
	 * 设置用户配置信息
	 * 
	 * @param value
	 */
	public void setUserProfile(String value) {
		String uid = UserContextUtil.getInstance().getCurrentUserId();
		SysPersonConfig model = new SysPersonConfig(uid, "RSS", value);
		String[] condition = { "RSS" };
		List<SysPersonConfig> list = sysPersonDAO.getUserConfigList(uid, condition);
		if (list == null || list.size() == 0) {
			sysPersonDAO.addBoData(model);
		} else {
			model.setId(list.get(0).getId());
			sysPersonDAO.updateBoData(model);
		}
	}

	/**
	 * 获得初始化页面信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getIndexList() {
		String uid = UserContextUtil.getInstance().getCurrentUserId();
		String list = this.getRssList(uid);// 获得当前用户RSS订阅信息
		if ("您还没有添加消息订阅".equals(list) && !SecurityUtil.isSuperManager(uid)) {
			// 非管理员账户，未填加订阅，默认显示ADMIN订阅消息
			list = this.getRssList(SecurityUtil.supermanager);
		}
		return list;
	}

	/**
	 * 获得订阅消息列表
	 * 
	 * @param uid
	 * @return
	 */
	public String getRssList(String uid) {
		StringBuffer sb = new StringBuffer();
		List<BdInfRssdyjlb> list = rssInformationDao.getIndexList(uid);
		if (list == null || list.size() == 0) {
			sb.append("您还没有添加消息订阅");
		} else {
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			sb1.append(" <ul id=\"column1\" class=\"column\">");
			sb2.append(" <ul id=\"column2\" class=\"column\">");
			try {
				for (int i = 0; i < list.size(); i++) {
					BdInfRssdyjlb model = list.get(i);
					int line = Integer.parseInt(model.getLinesize().toString());
					String groupName = model.getGroupname();
					String type = model.getType();
					String keyWord = model.getKeyword();
					String url = model.getRssurl();

					// 非RSS订阅方式，获取URL --搜索引擎+关键字
					if (!type.equals("RSS")) {
						if (type.equals("BAIDU")) {
							keyWord = keyWord.replaceAll("\\s+", "+");
						}
						url = (String) IworkPlugsRssUtil.hash.get(type) + keyWord;
					}
					if (i % 2 == 0) {
						sb1.append("<li class=\"widget color-red\" id=\"" + model.getId() + "\">").append("\n");
						sb1.append("<div class=\"widget-head\">").append("\n");
						sb1.append("<h3>" + model.getTitle() + "</h3>").append("\n");
						sb1.append("</div>").append("\n");
						sb1.append("<div class=\"widget-content\">").append("\n");
						sb1.append("<p>").append(feedReader(line, url)).append("</p>").append("\n");
						sb1.append("</div>").append("\n");
						sb1.append("</li>").append("\n");
					} else {
						sb2.append("<li class=\"widget color-red\" id=\"" + model.getId() + "\">").append("\n");
						sb2.append("<div class=\"widget-head\">").append("\n");
						sb2.append("<h3>" + model.getTitle() + "</h3>").append("\n");
						sb2.append("</div>").append("\n");
						sb2.append("<div class=\"widget-content\">").append("\n");
						sb2.append("<p>").append(feedReader(line, url)).append("</p>").append("\n");
						sb2.append("</div>").append("\n");
						sb2.append("</li>").append("\n");
					}
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
			sb1.append("</ul>").append("\n");
			sb2.append("</ul>").append("\n");
			sb.append(sb1).append(sb2);
		}
		return sb.toString();
	}

	/**
	 * 获取RSS订阅信息XML并解析
	 * 
	 * @param line
	 * @param url
	 * @return
	 */
	public static String feedReader(int line, String url) {
		if (line > 10) {
			line = 10;// 默认显示行数
		}
		StringBuffer sb = new StringBuffer();
		try {
			// String urlStr = "http://rss.sina.com.cn/news/marquee/ddt.xml";
			URLConnection feedUrl = new URL(url).openConnection();
			feedUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			List list = feed.getEntries();
			sb.append("<table width=100% border=0 class=\"tab\" cellspacing=2>\n");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (i == line) {
						break;
					} else {
						SyndEntry entry = (SyndEntry) list.get(i);
						String author = entry.getAuthor();
						Date date = entry.getPublishedDate();
						if (date == null) {
							sb.append("<tr>\n");
							sb.append("<td width=80%><a href='").append(entry.getLink()).append("' target='_blank' title='").append(entry.getTitle()).append("'>").append(entry.getTitle()).append("</a></td>");
							sb.append("<td width=20% title=\"").append(entry.getAuthor()).append("\">").append(entry.getAuthor()).append("</td>");
							sb.append("</tr>\n");
						} else if (author == null || "".equals(author.trim())) {
							sb.append("<tr>\n");
							sb.append("<td width=80%><a href='").append(entry.getLink()).append("' target='_blank' title='").append(entry.getTitle()).append("'>").append(entry.getTitle()).append("</a></td>");
							sb.append("<td width=20% align='center'>").append(UtilDate.dateFormat(entry.getPublishedDate())).append("&nbsp;</td>\n");
							sb.append("</tr>\n");
						} else {
							sb.append("<tr>\n");
							sb.append("<td width=70%><a href='").append(entry.getLink()).append("' target='_blank' title='").append(entry.getTitle()).append("'>").append(entry.getTitle()).append("</a></td>");
							sb.append("<td width=15% title=\"").append(entry.getAuthor()).append("\">").append(entry.getAuthor()).append("</td>");
							sb.append("<td width=15% align='center'>").append(UtilDate.dateFormat(entry.getPublishedDate())).append("&nbsp;</td>\n");
							sb.append("</tr>\n");
						}
					}
				}
			} else {
				sb.append("<br><br><div align=center><font color=gray>没有数据</font></div><br><br>");
			}
			sb.append("</table>\n");
		} catch (Exception e) {
			sb.append("<br><br><div align=center><font color=gray>读RSS数据时失败</font></div><br><br>");
			logger.error(e,e);
		}
		return sb.toString();
	}

	/**
	 * 填加RSS订阅
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String addRssSubscription(BdInfRssdyjlb model) throws Exception {
		String uid = UserContextUtil.getInstance().getCurrentUserId();
		String Profile = "";
		List<Object> leftList = new ArrayList<Object>();
		List<Object> rightList = new ArrayList<Object>();
		List<BdInfRssdyjlb> list = rssInformationDao.getIndexList(uid);// 用户自定义订阅
		if (list == null || list.size() == 0) {
			// 复制admin订阅消息
			String userProfile = this.getUserProfile(uid);// 用户自定义配置文件
			if (userProfile == null || "".equals(userProfile.trim())) {
				this.addDefaultRssInfo(uid);// 填加默认订阅
				Long id = (long) rssInformationDao.getIdSequcence();
				String groupname = "personal";
				model.setId(id);
				model.setCreateuser(uid);
				model.setGroupname(groupname);
				rssInformationDao.addRssSubscription(model);
			} else {
				int x = userProfile.indexOf("|");
				if (x > -1) {
					String left = userProfile.substring(0, x);
					String[] leftStrings = left.split(";");
					for (int i = 0; i < leftStrings.length; i++) {
						String value = leftStrings[i];
						int m = value.indexOf(",");
						if (m > -1) {
							String id = value.substring(0, m);
							leftList.add(id);
						}
					}
					Profile = this.addUserRssInfo(uid, leftList, Profile);
					Profile = Profile + "|";
					String right = userProfile.substring(x + 1, userProfile.length());
					String[] rightStrings = right.split(";");
					for (int i = 0; i < rightStrings.length; i++) {
						String value = rightStrings[i];
						int n = value.indexOf(",");
						if (n > -1) {
							String id = value.substring(0, n);
							rightList.add(id);
						}
					}
					Profile = this.addUserRssInfo(uid, rightList, Profile);

					Long id = (long) rssInformationDao.getIdSequcence();
					String groupname = "personal";
					model.setId(id);
					model.setCreateuser(uid);
					model.setGroupname(groupname);
					rssInformationDao.addRssSubscription(model);
					userProfile = Profile + ";" + id + ",color-red," + model.getTitle() + ",not-collapsed";
					rssInformationDao.updateUserProfile(uid, userProfile);
				}
			}
		} else {
			Long id = (long) rssInformationDao.getIdSequcence();
			String groupname = "personal";
			model.setId(id);
			model.setCreateuser(uid);
			model.setGroupname(groupname);
			rssInformationDao.addRssSubscription(model);
			String userProfile = rssInformationDao.getUserProfile(uid);
			userProfile = userProfile + ";" + id + ",color-red," + model.getTitle() + ",not-collapsed";
			rssInformationDao.updateUserProfile(uid, userProfile);
		}
		return "success";
	}

	/**
	 * 填加系统默认订阅
	 * 
	 * @param uid
	 * @return
	 */
	public String addDefaultRssInfo(String uid) {
		List<BdInfRssdyjlb> list = rssInformationDao.getIndexList(SecurityUtil.supermanager);
		for (int i = 0; i < list.size(); i++) {
			BdInfRssdyjlb model = list.get(i);
			Long id = (long) rssInformationDao.getIdSequcence();
			String groupname = "personal";
			model.setId(id);
			model.setCreateuser(uid);
			model.setGroupname(groupname);
			rssInformationDao.addRssSubscription(model);
		}
		return "success";
	}

	/**
	 * 填加用户自定义订阅
	 * 
	 * @param uid
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public String addUserRssInfo(String uid, List<Object> idList, String userProfile) throws Exception {
		List<BdInfRssdyjlb> list = rssInformationDao.getIndexList(SecurityUtil.supermanager);
		for (int i = 0; i < list.size(); i++) {
			BdInfRssdyjlb model = list.get(i);
			String ID = model.getId().toString();
			boolean flag = IworkPlugsRssUtil.isItEquals(idList, ID);
			if (flag) {
				Long id = (long) rssInformationDao.getIdSequcence();
				String groupname = "personal";
				model.setId(id);
				model.setCreateuser(uid);
				model.setGroupname(groupname);
				rssInformationDao.addRssSubscription(model);
				userProfile = this.addUserProfile(userProfile, uid, id.toString(), model.getTitle());
			} else {
				continue;
			}
		}
		return userProfile;
	}

	public String addUserProfile(String userProfile, String uid, String id, String title) {
		if (userProfile == null || "".endsWith(userProfile.trim())) {
			userProfile = id + ",color-red," + title + ",not-collapsed";
		} else {
			userProfile = userProfile + ";" + id + ",color-red," + title + ",not-collapsed";
		}
		rssInformationDao.updateUserProfile(uid, userProfile);
		return userProfile;
	}

	/**
	 * 删除RSS订阅
	 * 
	 * @param id
	 * @return
	 */
	public String deleteRssSubscription(String id) {
		rssInformationDao.deleteRssSubscription(id);
		return "success";
	}

	/**
	 * 重置配置信息
	 * 
	 * @return
	 */
	public String resetRssUserProfile() {
		String uid = UserContextUtil.getInstance().getCurrentUserId();
		String[] condition = { "RSS" };
		List<SysPersonConfig> list = sysPersonDAO.getUserConfigList(uid, condition);
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				SysPersonConfig model = list.get(i);
				sysPersonDAO.delBoData(model);
			}
		}
		rssInformationDao.resetRssSubscription();
		return "success";
	}

	/*----------------------GET/SET----------------------*/
	public IworkPlugsRssDao getRssInformationDao() {
		return rssInformationDao;
	}

	public void setRssInformationDao(IworkPlugsRssDao rssInformationDao) {
		this.rssInformationDao = rssInformationDao;
	}

	public SysPersonDAO getSysPersonDAO() {
		return sysPersonDAO;
	}

	public void setSysPersonDAO(SysPersonDAO sysPersonDAO) {
		this.sysPersonDAO = sysPersonDAO;
	}

}
