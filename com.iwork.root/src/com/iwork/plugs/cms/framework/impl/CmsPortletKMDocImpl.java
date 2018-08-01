package com.iwork.plugs.cms.framework.impl;

import java.util.HashMap;
import com.iwork.core.organization.context.UserContext;
import com.iwork.km.document.dao.KMDocDAO;
import com.iwork.km.document.service.KMPurViewService;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;

public class CmsPortletKMDocImpl implements CmsPortletInterface {

	private KMDocDAO kmDocDAO; 
	private KMPurViewService kmPurViewService;
	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer sb = new StringBuffer();
	
		return sb.toString();
	}
	
	public String portletWeiXinPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}

	private String getTitleHtmlStr() {

		String resultStr = "<table width=\"100%\" border=\"0\"><tbody>"
				+ "<tr><td class=\"lft_tit\">常用资料</td></tr>"
				+ "</tbody></table>";
		return resultStr;
	}

}
