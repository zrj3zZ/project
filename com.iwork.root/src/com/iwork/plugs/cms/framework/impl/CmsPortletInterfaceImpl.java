package com.iwork.plugs.cms.framework.impl;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.iwork.commons.ClassReflect;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.CmsPortletModel;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.util.CmsUtil;
import org.apache.log4j.Logger;
/**
 * CMS接口页面实现类
 * 
 * @author WeiGuangjian
 * 
 */
public class CmsPortletInterfaceImpl implements CmsPortletInterface {
	private static Logger logger = Logger.getLogger(CmsPortletInterfaceImpl.class);
	public String portletPage(UserContext me, DomainModel domainModel, IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer html = new StringBuffer();
		String width = "100%;";
		if (infoModel.getPwidth() != null && infoModel.getPwidth() > 0) {
			width = infoModel.getPwidth().toString()+"px;";
		}
		String height = "100%;";
		if (infoModel.getPheight() != null && infoModel.getPheight() > 0) {
			height = infoModel.getPheight().toString()+"px;";
		}
		String border = "";
		if (infoModel.getIsborder() == 0) {
			border = "border:1px solid #DDDDDD;";
		}
		html.append("<div style=\"width:" + width + ";height:" + height +  border + "\">\n");
		html.append("<div class=\"item_Title\">").append("\n"); 
		if (infoModel.getIstitle() == 0) {
			String linktarget = "target='" + infoModel.getLinktarget() + "'";
			if (infoModel.getLinktarget() == null || infoModel.getLinktarget().equals("")) {
				linktarget = "";
			}
			String morelink = ""; 
			if(linktarget.equals("")){
				morelink = "<a href=\"javascript:parent.addTab('"+infoModel.getPortletname()+"','" + infoModel.getMorelink() + "')\" title='查看更多'><img src='iwork_img/more.jpg' /></a>";
			}else{ 
				morelink = "<a href='" + infoModel.getMorelink() + "' " + linktarget + " title='查看更多'><img src='iwork_img/more.jpg' /></a>";
			}
			if (infoModel.getMorelink() == null || infoModel.getMorelink().equals("")) {
				morelink = "";
			}
			html.append("<span class=\"lft_tit\">" + infoModel.getPortletname() + "</span><span class=\"title_more\">" + morelink + "</span>\n");
		}
		html.append("</div>").append("\n");
		html.append("<div clss=\"ItemContent\">").append("\n");
		if (CmsUtil.getContentSecurityList(infoModel.getBrowse())) {
			if (CmsUtil.getEffect(infoModel.getBegindate(), infoModel.getEnddate())) {
				if (infoModel.getStatus() == 0) {
					CmsPortletModel apiModel = new CmsPortletModel();
					Constructor cons = null;
					Class[] parameterTypes = {};
					try {
						cons = ClassReflect.getConstructor(infoModel.getParam(), parameterTypes);
						if (cons != null) {
							Object[] param = {};
							apiModel.setKsPortalInfoObject((CmsPortletInterface) cons.newInstance(param));
						}					
					} catch (Exception e) {
						logger.error(e,e);
					}
					CmsPortletInterface portlet = apiModel.getKsPortalInfoObject();
					html.append(portlet.portletPage(me, domainModel, infoModel, params));
				} else {
					html.append("<div style='text-align:center;color:red;'>此栏目已关闭!</div>");
				}
			} else {
				html.append("<div style='text-align:center;color:red;'>此栏目已失效!</div>");
			}
		} else {
			html.append("<div style='text-align:center;color:red;'>您无权查看此栏目!</div>");
		}
		html.append("	</div>").append("\n");
		html.append("<div>\n");
		return html.toString();
	}

	public String portletWeiXinPage(UserContext me, DomainModel domainModel,IworkCmsPortlet infoModel, HashMap params) {
		
		return "";
	}
}
