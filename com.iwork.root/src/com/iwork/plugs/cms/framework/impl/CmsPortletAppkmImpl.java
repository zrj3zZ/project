package com.iwork.plugs.cms.framework.impl;

import java.util.HashMap;
import java.util.List;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.dao.CmsAppkmDAO;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsAppkm;
import com.iwork.plugs.cms.model.IworkCmsPortlet;

public class CmsPortletAppkmImpl implements CmsPortletInterface {

	private CmsAppkmDAO cmsAppkmDAO;

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer sb = new StringBuffer();
		// sb.append("<div class='item_km'>").append("\n");
		sb.append("<ul class='item_task'>").append("\n");
		if (cmsAppkmDAO == null) {
			cmsAppkmDAO = (CmsAppkmDAO) SpringBeanUtil.getBean("cmsAppkmDAO");
		}
		List<IworkCmsAppkm> list = cmsAppkmDAO.getList();
		for (int i = 0; i < list.size(); i++) {
			IworkCmsAppkm model = list.get(i);
			String url = model.getUrl();
			// sb.append("<span>").append("<a href=\"").append(url).append("\" target=\"_blank\" >").append(model.getTitle()).append("</a>").append("</span>").append("\n");
			sb.append("<li>").append("<a href=\"").append(url).append("\" target=\"_blank\" >").append(model.getTitle()).append("</a>").append("</li>").append("\n");
		}
		sb.append("</ul>").append("\n");
		// sb.append("</div>").append("\n");
		return sb.toString();
	}
	
	public String portletWeiXinPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer sb = new StringBuffer();
		//sb.append(this.getTitleHtmlStr()); 常用资料标题
		sb.append("<div class=\"lft_u_cnt\">").append("\n");
		if (cmsAppkmDAO == null) {
			cmsAppkmDAO = (CmsAppkmDAO) SpringBeanUtil.getBean("cmsAppkmDAO");
		}
		List<IworkCmsAppkm> list = cmsAppkmDAO.getList();
		for (int i = 0; i < list.size(); i++) {
			IworkCmsAppkm model = list.get(i);
			String url = model.getUrl();
			sb.append("<span class=\"Item\">");
			sb.append("<a href=\"").append(url)
					.append("\" target=\"_blank\" >").append(model.getTitle())
					.append("</a>").append("\n");
			sb.append("</span>");
		}
		sb.append("</div>").append("\n");
		return sb.toString();
	}
}
