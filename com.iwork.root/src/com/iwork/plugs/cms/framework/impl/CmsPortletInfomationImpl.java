package com.iwork.plugs.cms.framework.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.util.CmsCommentUtil;
import com.iwork.plugs.cms.util.CmsUtil;

/**
 * CMS资讯页面实现类
 * 
 * @author WeiGuangjian
 * 
 */
public class CmsPortletInfomationImpl implements CmsPortletInterface {
	private CmsInfoDAO cmsInfoDAO;

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		// TODO Auto-generated method stub
		StringBuffer html = new StringBuffer();
		long rows = infoModel.getProws();
		if (rows <= 0) {
			rows = 10;
		}
		String width = "100%";
		if (infoModel.getPwidth() != null && infoModel.getPwidth() > 0) {
			width = infoModel.getPwidth().toString();
		}
		String height = "100%";
		if (infoModel.getPheight() != null && infoModel.getPheight() > 0) {
			height = infoModel.getPheight().toString();
		}
		String border = "";
		if (infoModel.getIsborder() == 0) {
			border = "style='border:1px solid #DDDDDD;'";
		}
		html.append("<div class=\"item_Title\">").append("\n");
		html.append("<table width="
				+ width
				+ " height="
				+ height
				+ "  "
				+ border
				+ "   cellpadding=0 cellspacing=0><tr><td style=\"vertical-align:top;\"  class='lft_item'>\n");
		if (infoModel.getIstitle() == 0) {
			html.append("<table width=\"100%\" border=\"0\"><tr><td class=\"lft_tit\">"
					+ infoModel.getPortletname()
					+ "</td><td class=\"title_more\"><a href='###' onclick=\"openMoreHtml("
					+ infoModel.getId()
					+ ");\"  title='查看更多'><img src='../iwork_img/more.jpg' /></a></td></tr></table>\n");
		}
		if (CmsUtil.getContentSecurityList(infoModel.getBrowse())) {
			if (CmsUtil.getEffect(infoModel.getBegindate(),
					infoModel.getEnddate())) {
				if (infoModel.getStatus() != null
						&& infoModel.getStatus().equals(new Long(0))) {
					if (cmsInfoDAO == null)
						cmsInfoDAO = (CmsInfoDAO) SpringBeanUtil
								.getBean("cmsInfoDAO");
					List<IworkCmsContent> cmsInfoList = cmsInfoDAO
							.getNewsList(infoModel.getId());
					int line = 0;
					html.append("<ul class='item_ul'>\n");
					for (IworkCmsContent model : cmsInfoList) {
						if (model.getStatus() == null)
							model.setStatus(new Long(0)); // 如果状态为空，默认为不发布
						if (!CmsUtil.getContentSecurityList(model.getBrowse())) {
							continue;
						}
						String releaseDate = null;
						if (model.getReleasedate() != null) {
							releaseDate = CmsUtil.dateShortFormat(model
									.getReleasedate());
						}
						String resultDate = UtilDate.dateFormat(new Date());
						String newImg = "";
						if (releaseDate != null && resultDate != null) {
							if (CmsUtil.getSubtractionDate(
									model.getReleasedate(), new Date()) <= 5) {
								newImg = "&nbsp;(新)";
								;
							}
						}
						if (model.getBrieftitle().equals("")
								|| model.getBrieftitle() == null) {
							String fulltitle = CmsUtil.getCmsTitle(model);
							html.append(
									"<li><a href='#' onClick='openCms("
											+ model.getId() + ")' title='")
									.append(model.getTitle() + newImg)
									.append("'>").append("·").append(fulltitle);

							int commentCount = CmsCommentUtil.getInstance()
									.getCommentCount(model.getId() + "");
							if (commentCount > 0) {
								html.append("&nbsp;(" + commentCount + ")");
							}

							html.append(newImg).append("</a>")
									.append("<span class='time'>")
									.append(releaseDate).append("</span>")
									.append("</li>\n");
						} else {
							String fulltitle = CmsUtil.getCmsBriefTitle(model);
							html.append(
									"<li><a href='#' onClick='openCms("
											+ model.getId() + ")' title='")
									.append(model.getTitle() + newImg)
									.append("'>").append("·").append(fulltitle)
									.append(newImg);

							int commentCount = CmsCommentUtil.getInstance()
									.getCommentCount(model.getId() + "");
							if (commentCount > 0) {
								html.append("&nbsp;(" + commentCount + ")");
							}

							html.append("</a>").append("<span class='time'>")
									.append(releaseDate).append("</span>")
									.append("</li>\n");
						}
						if (line == rows - 1) {
							line++;
							break;
						}
						line++;
					}
					html.append("</ul>\n");
				} else {
					html.append("<div style='text-align:center;color:red;'></div>");
				}
			} else {
				html.append("<div style='text-align:center;color:red;'></div>");
			}
		} else {
			html.append("<div style='text-align:center;color:red;'></div>");
		}
		if (infoModel.getIstitle() == 0) {
			html.append("</div>\n");
		}
		html.append("</td></tr></table>\n");
		html.append("</div>\n");
		return html.toString();
	}
	
	public String portletWeiXinPage(UserContext me, DomainModel domainModel,IworkCmsPortlet infoModel, HashMap params) {
		// TODO Auto-generated method stub
		StringBuffer html=new StringBuffer();
		long rows=infoModel.getProws();
		if(rows<=0){
			rows=10;
		}
		if(CmsUtil.getContentSecurityList(infoModel.getBrowse())){
			if(CmsUtil.getEffect(infoModel.getBegindate(),infoModel.getEnddate())){
				if(infoModel.getStatus()==0){				
					CmsInfoDAO cmsInfoDAO= (CmsInfoDAO)SpringBeanUtil.getBean("cmsInfoDAO");
					List<IworkCmsContent> cmsInfoList=cmsInfoDAO.getNewsList(infoModel.getId());
					int line = 0;
//					html.append("<div data-role=\"header\"  data-position=\"fixed\">");
//					html.append("<h2 >").append(infoModel.getPortletname()).append("</h2>");
//					html.append("</div >");
					html.append("<ul data-role=\"listview\" data-inset=\"true\">");
					html.append("<li data-role=\"list-divider\" data-icon=\"grid\">").append(infoModel.getPortletname()).append("  </a></li>\n");
			        for(int i=0;i<cmsInfoList.size();i++){ 
			        	IworkCmsContent model=cmsInfoList.get(new Integer(i));
			        	if(!CmsUtil.getContentSecurityList(model.getBrowse())||model.getStatus()==1){
			        		continue;
			        	}     	 
			        	String releaseDate = CmsUtil.dateFormat(model.getReleasedate());
						String newImg="";
						if (CmsUtil.getSubtractionDate(model.getReleasedate(), new Date()) <= 5) {
							newImg = "&nbsp;(新)"; 
						}
							html.append("<li>"); 
							html.append("<a href=\"weixin_cms_page.action?infoid=").append(model.getId()).append("\">").append("\n");
							if(model.getPrepicture()!=null){
								html.append("<img class=\"prepic\" style=\"margin:10px;\" ").append("src=\"").append(model.getPrepicture()).append("\"").append(">");
							}
							html.append(" <h4>").append(newImg).append(model.getTitle()).append("</h4>").append("\n");
							html.append("<p>").append(model.getBrieftitle()).append("</p>").append("\n");
							html.append("<p >").append(releaseDate).append("</p>").append("\n");
							html.append("</a>").append("\n");
							html.append("</li>").append("\n");
						if (line == rows-1) { 
							line++;
							break;
						}
						line++;
			        }
			        html.append("</ul>").append("\n");
				}
			}
		}		
		return html.toString();
	}

}
