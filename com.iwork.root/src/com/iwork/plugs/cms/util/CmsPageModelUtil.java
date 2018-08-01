package com.iwork.plugs.cms.util;

/**
 * 分页工具类
 * 
 * @author liuyiyang
 * 
 */

public class CmsPageModelUtil {

	private static CmsPageModelUtil instance = null;

	private CmsPageModelUtil() {
	}

	public static CmsPageModelUtil getInstance() {
		if (instance == null) {
			instance = new CmsPageModelUtil();
		}
		return instance;
	}

	public String getPageModelHtmlStr(String url, int count, int pageSize,
			int pageNo) {

		StringBuffer sb = new StringBuffer();
		int maxPage = 0;
		if (count % pageSize == 0) {
			maxPage = count / pageSize;
		} else {
			maxPage = count / pageSize + 1;
		}

		int showPageNum = 5;
		int pageFlag = (showPageNum - 1) / 2;

		sb.append("<li class=\"fenye_bar\" onclick=\"changePage('" + url + "pageNo="
				+ maxPage + "&pageSize=" + pageSize + "')\">末页</li>");
		
		if (pageNo < maxPage) {
			sb.append("<li class=\"fenye_bar\" onclick=\"changePage('" + url + "pageNo="
				+ (pageNo + 1) + "&pageSize=" + pageSize + "')\">下一页</li>");
		}
		
		if (maxPage <= showPageNum) {
			for (int i = maxPage; i >= 1; i--) {
				sb.append(getOnePageHtml(url, i, pageSize, i == pageNo));
			}
		} else {
			if (pageNo - pageFlag <= 0) {
				for (int i = showPageNum; i >= 1; i--) {
					sb.append(getOnePageHtml(url, i, pageSize, i == pageNo));
				}
			} else if (pageNo + pageFlag >= maxPage) {
				for (int i = maxPage; i >= (maxPage - pageFlag * 2); i--) {
					sb.append(getOnePageHtml(url, i, pageSize, i == pageNo));
				}
			} else {
				for (int i = (pageNo + pageFlag); i >= (pageNo - pageFlag); i--) {
					sb.append(getOnePageHtml(url, i, pageSize, i == pageNo));
				}
			}
		}

		if (pageNo >= 2) {
			sb.append("<li class=\"fenye_bar\" onclick=\"changePage('" + url + "pageNo="
				+ (pageNo - 1) + "&pageSize=" + pageSize + "')\">上一页</li>");
		}

		sb.append("<li class=\"fenye_bar\" onclick=\"changePage('" + url + "pageNo="
				+ 1 + "&pageSize=" + pageSize + "')\">首页</li>");


		return sb.toString();
	}

	/**
	 * 分页样式拼装
	 * 
	 * @param url
	 * @param pageNo
	 * @param pageSize
	 * @param flag
	 * @return
	 */
	private String getOnePageHtml(String url, int pageNo, int pageSize,
			boolean flag) {

		StringBuffer sb = new StringBuffer();
		String hrefStr = url + "pageNo=" + pageNo + "&pageSize=" + pageSize;
		if (flag) {
			sb.append("<li class=\"fenye_bar_selected\">");
			sb.append(pageNo);
			sb.append("</li>");
		} else {
			sb.append("<li class=\"fenye_bar\" onclick=\"changePage('" + hrefStr + "')\">");
			sb.append(pageNo);
			sb.append("</li>");
		}

		return sb.toString();
	}
}
