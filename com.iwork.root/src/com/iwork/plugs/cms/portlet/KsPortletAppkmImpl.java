package com.iwork.plugs.cms.portlet;

import java.util.HashMap;

import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;

/**
 * 首页常用资料实现类
 * @author WeiGuangjian
 *
 */
public class KsPortletAppkmImpl implements CmsPortletInterface {

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		// TODO Auto-generated method stub
		StringBuffer html=new StringBuffer();		  
		html.append("<div class='lft_user'>\n");
		html.append("<div class='lft_u_cnt'>\n");
		html.append("常用资料");
		html.append("</div>\n");
		html.append("</div>\n");		
		return html.toString();
	}
	
	public String portletWeiXinPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		
		return "";
	}
}
