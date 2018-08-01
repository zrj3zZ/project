package com.iwork.app.mobile.information.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.dao.CmsPortletDAO;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.service.CmsInfoService;
import com.iwork.plugs.cms.util.CmsUtil;

public class SyncInformactionService {
	private static final long noticeId = 0;// 新闻公告ID
	private static final long industryId = 1;// 行业新闻ID
	private static final long trainId = 2;// 培训ID
	private static final long moreId = 0;// 新闻公告ID

	private CmsInfoDAO cmsInfoDAO;
	private CmsPortletDAO cmsPortletDAO;

	public String getList() {
		StringBuffer sb = new StringBuffer();
		List<IworkCmsPortlet> list = cmsPortletDAO.getAllList();
		sb.append("<ul class='item_ul' data-role=\"listview\">\n");
		for (int i = 0; i < list.size(); i++) {
			IworkCmsPortlet model = list.get(i);
			// 过滤非资讯类portlet
			if (model.getPortlettype() == 0) {
				sb.append("<li><a href='javascript:openLink(\"" + model.getPortletkey() + "\");'>" + model.getPortletname() + "</a></li>\n");
			} else {
				continue;
			}
		}
		sb.append("</ul>\n");
		return sb.toString();
	}

	public String getList(String type) {
		long id = -1;
		if ("notice".equals(type.trim())) {
			id = noticeId;
		} else if ("industry".equals(type.trim())) {
			id = industryId;
		} else if ("train".equals(type.trim())) {
			id = trainId;
		}

		if (cmsInfoDAO == null) {
			cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
		}
		StringBuffer sb = new StringBuffer();
		List<IworkCmsContent> cmsInfoList = cmsInfoDAO.getNewsList(id);
		sb.append("<ul class='item_ul' data-role=\"listview\">\n");
		for (int i = 0; i < cmsInfoList.size(); i++) {
			IworkCmsContent model = cmsInfoList.get(i);

			if (model.getStatus() == null)
				// 如果状态为空，默认为不发布
				model.setStatus(new Long(0));
			if (!CmsUtil.getContentSecurityList(model.getBrowse())) {
				// 判断用户浏览权限
				continue;
			}
			String releaseDate = null;// 发布时间

			if (model.getReleasedate() != null) {
				releaseDate = CmsUtil.dateShortFormat(model.getReleasedate());
			}
			String resultDate = UtilDate.dateFormat(new Date());
			String newImg = "";
			if (releaseDate != null && resultDate != null) {
				if (CmsUtil.getSubtractionDate(model.getReleasedate(), new Date()) <= 5) {
					newImg = "&nbsp;(新)";
				}
			}
			if (model.getBrieftitle().equals("") || model.getBrieftitle() == null) {
				String fulltitle = CmsUtil.getCmsTitle(model);
				sb.append("<li><a href='javascript:openCms(" + model.getId() + ")'>").append("·").append(fulltitle).append(newImg).append("</a>").append("<span class='time'>").append(releaseDate).append("</span>").append("</li>\n");
			} else {
				String fulltitle = CmsUtil.getCmsBriefTitle(model);
				sb.append("<li><a href='javascript:openCms(" + model.getId() + ")'>").append("·").append(fulltitle).append(newImg).append("</a>").append("<span class='time'>").append(releaseDate).append("</span>").append("</li>\n");
			}

		}
		sb.append("</ul>\n");
		// JSONArray json = JSONArray.fromObject(rows);
		// map.put("dataRows", rows);
		// JSONArray json = JSONArray.fromObject(map);
		// sb.append(json);
		return sb.toString();
	}

	public String getList(String type, int pageNum, int pageSize) {
		long id = -1;
		if ("notice".equals(type.trim())) {
			id = noticeId;
		} else if ("industry".equals(type.trim())) {
			id = industryId;
		} else if ("train".equals(type.trim())) {
			id = trainId;
		} else {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		int totalLineNum = 0; // 总行数
		int totalPageNum = 0; // 总页数

		Map<String, Object> total = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		if (userContext == null) {
			return null;
		} else {
			String userid = userContext.get_userModel().getUserid();
			if (userid == null) {
				return null;
			} else {
				if (cmsInfoDAO == null) {
					cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil.getBean("cmsInfoDAO");
				}
				List<IworkCmsContent> cmsInfoList = cmsInfoDAO.getNewsList(id);
				totalLineNum = cmsInfoList.size();
				int x = totalLineNum % pageSize;
				if (x == 0) {
					totalPageNum = totalLineNum / pageSize;
				} else {
					totalPageNum = totalLineNum / pageSize + 1;
				}
				int startRow = (pageNum - 1) * pageSize;// 开始行
				int endRow = startRow + pageSize;// 结束行

				if (totalPageNum == pageNum) {
					if (x == 0) {
						endRow = startRow + pageSize;
					} else {
						endRow = startRow + x;
					}
				}

				if (endRow <= totalLineNum) {
					List<IworkCmsContent> list = cmsInfoList.subList(startRow, endRow);// 取页面显示list
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> item = new HashMap<String, Object>();
						IworkCmsContent model = list.get(i);
						if (model.getStatus() == null)
							// 如果状态为空，默认为不发布
							model.setStatus(new Long(0));
						if (!CmsUtil.getContentSecurityList(model.getBrowse())) {
							// 判断用户浏览权限
							continue;
						}
						String releaseDate = null;// 发布时间
						if (model.getReleasedate() != null) {
							Date date = new Date();
							long releaseTime = model.getReleasedate().getTime();
							long timeNow = date.getTime();
							int min = 1000 * 60;
							int hour = 1000 * 60 * 60;
							int day = 1000 * 60 * 60 * 24;
							int month = 1000 * 60 * 60 * 24 * 30;
							int year = 1000 * 60 * 60 * 24 * 365;
							long timeDiff = timeNow - releaseTime;
							// 分钟，小时，日，月，年
							if (timeDiff < min) {
								releaseDate = (timeDiff / 1000 + 1) + "秒前";
							} else if (timeDiff < hour) {
								releaseDate = (timeDiff / min + 1) + "分钟前";
							} else if (timeDiff < day) {
								releaseDate = (timeDiff / hour + 1) + "小时前";
							} else {
								SimpleDateFormat sd = new SimpleDateFormat("MM月dd日");
								releaseDate = sd.format(model.getReleasedate());
							}
							item.put("time", releaseDate);
						}
						String resultDate = UtilDate.dateFormat(new Date());
						String newImg = "";
						if (releaseDate != null && resultDate != null) {
							if (CmsUtil.getSubtractionDate(model.getReleasedate(), new Date()) <= 5) {
								newImg = "(新)";
							}
						}
						if (model.getBrieftitle().equals("") || model.getBrieftitle() == null) {
							String fulltitle = newImg + model.getTitle();
							item.put("title", fulltitle);
						} else {
							String fulltitle = newImg + model.getBrieftitle();// 短标题
							item.put("title", fulltitle);
						}
						String preContent = null;
						if (model.getPrecontent() != null && !"".equals(model.getPrecontent())) {
							if (model.getPrecontent().length() > 50) {
								preContent = model.getPrecontent().substring(0, 50) + "...";
							} else {
								preContent = model.getPrecontent();
							}
						}
						item.put("preContent", preContent);
						String source = null;
						if (model.getSource() != null && !"".equals(model.getSource())) {
							source = model.getSource();
						}
						item.put("source", source);
						item.put("id", model.getId());
						items.add(item);
					}

				}
				total.put("total", totalLineNum);
				total.put("curPage", pageNum);
				total.put("pageSize", pageSize);
				total.put("totalPages", totalPageNum);
				total.put("totalRecords", totalLineNum);
				// 如果当前页数超过总页数
				if (pageNum > totalPageNum) {
					total.put("dataRows", "");
				} else {
					total.put("dataRows", items);
				}
				JSONArray json = JSONArray.fromObject(total);
				sb.append(json);
				return sb.toString();
			}
		}

	}

	public IworkCmsContent getContent(String id) {
		StringBuffer sb = new StringBuffer();
		CmsInfoService cmsInfoService = (CmsInfoService) SpringBeanUtil.getBean("cmsInfoService");
		IworkCmsContent model = cmsInfoService.getModel(id);
		return model;
	}

	public CmsInfoDAO getCmsInfoDAO() {
		return cmsInfoDAO;
	}

	public void setCmsInfoDAO(CmsInfoDAO cmsInfoDAO) {
		this.cmsInfoDAO = cmsInfoDAO;
	}

	public CmsPortletDAO getCmsPortletDAO() {
		return cmsPortletDAO;
	}

	public void setCmsPortletDAO(CmsPortletDAO cmsPortletDAO) {
		this.cmsPortletDAO = cmsPortletDAO;
	}

}
