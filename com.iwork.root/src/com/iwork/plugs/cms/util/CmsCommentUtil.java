package com.iwork.plugs.cms.util;

import java.text.SimpleDateFormat;
import java.util.List;

import com.iwork.core.security.SecurityUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.dao.CmsCommentDao;
import com.iwork.plugs.cms.model.IworkCmsComment;

public class CmsCommentUtil {

	private static CmsCommentUtil instance = null;

	private CmsCommentUtil() {
	}

	public static CmsCommentUtil getInstance() {
		if (instance == null) {
			instance = new CmsCommentUtil();
		}
		return instance;
	}

	/**
	 * 添加一个新评论
	 * @param comment
	 */
	public void addNewComment(IworkCmsComment comment) {

		CmsCommentDao dao = (CmsCommentDao) SpringBeanUtil
				.getBean("cmsCommentDao");
		dao.addBoData(comment);

	}

	/**
	 * 删除一条评论（管理员权限）
	 * @param commentId
	 * @return
	 */
	public boolean delComment(long commentId) {

		CmsCommentDao dao = (CmsCommentDao) SpringBeanUtil
				.getBean("cmsCommentDao");

		IworkCmsComment model = dao.getOneDataById(commentId);
		if (model != null) {
			dao.deleteBoData(model);
			return true;
		}

		return false;
	}

	/**
	 * 获得评论页面HTML
	 * @param infoid 评论ID
	 * @param userId 用户ID
	 * @param pageNo 当前页
	 * @param pageSize 每页显示个数
	 * @return
	 */
	public String getCommentHtmlStr(String infoid, String userId, int pageNo,
			int pageSize) {

		StringBuffer sb = new StringBuffer();

		CmsCommentDao dao = (CmsCommentDao) SpringBeanUtil
				.getBean("cmsCommentDao");
		List<IworkCmsComment> list = dao.getAllList(infoid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		int line = 0;
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			IworkCmsComment model = list.get(i);
			if (line >= (pageNo - 1) * pageSize) {
				if (line < pageNo * pageSize)
					flag = true;
			}
			if (flag) {
				sb.append(
						"<tr bgcolor=\"#F5F8FA\"><td width=\"90%\" align=\"left\" class=\"talkUser\"><img border=\"0\" src=\"../iwork_img/but_dark.gif\">")
						.append("&nbsp;&nbsp;" + model.getTalkname()
								+ "&nbsp;&nbsp;")
						.append(sdf.format(model.getTalktime()))
						.append("</td><td width=\"5%\" nowrap=\"nowrap\">" + (list.size() - i) + "楼&nbsp;");
				if (SecurityUtil.getInstance().isSuperManager(userId)) {
					sb.append("<a title=\"删除\" onclick='delCommmet("
							+ model.getId() + ")' href=\"###\">×</a></td></tr>");
				}
				sb.append("<tr><td width=\"90%\" align=\"left\" class=\"talkContent\">"
						+ model.getTalkcontent()
						+ "</td><td align=\"left\"></td></tr>");
				sb.append("<tr><td class=\"bordertop\" colspan=\"2\"></td></tr>");
			}

			line++;
			flag = false;
		}

		return sb.toString();
	}

	/**
	 * 获得评论总数
	 * @param infoid 新闻ID
	 * @return
	 */
	public int getCommentCount(String infoid) {

		CmsCommentDao dao = (CmsCommentDao) SpringBeanUtil
				.getBean("cmsCommentDao");
		List<IworkCmsComment> list = dao.getAllList(infoid);

		if (list != null && list.size() > 0) {
			return list.size();
		}

		return 0;
	}
}
