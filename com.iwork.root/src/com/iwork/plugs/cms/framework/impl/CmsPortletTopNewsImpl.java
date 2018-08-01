package com.iwork.plugs.cms.framework.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.util.CmsUtil;

/**
 * CMS资讯页面实现类
 * @author WeiGuangjian
 *
 */
public class CmsPortletTopNewsImpl implements CmsPortletInterface {

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
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
					html.append("<ul class=\"item_ul sep_ul\">\n");
			        for(int i=0;i<cmsInfoList.size();i++){
			        	IworkCmsContent model=cmsInfoList.get(new Integer(i));
			        	if(!CmsUtil.getContentSecurityList(model.getBrowse())||model.getStatus()==1){
			        		continue;
			        	}     	 
			        	String releaseDate = CmsUtil.dateShortFormat(model.getReleasedate());
						String newImg="";
						if (CmsUtil.getSubtractionDate(model.getReleasedate(), new Date()) <= 5) {
							newImg = "&nbsp;(新)"; 
						}
						if(model.getBrieftitle().equals("")||model.getBrieftitle()==null){
							String fulltitle=CmsUtil.getCmsTitle(model);
							html.append("<li><a href='#' onClick='openCms("+model.getId()+")' title='").append(model.getTitle()+newImg).append("'>").append("").append(fulltitle).append(newImg).append("</a>").append("<span class='time'>").append(releaseDate).append("</span>").append("</li>\n");
						}else{
							String fulltitle=CmsUtil.getCmsBriefTitle(model);
							html.append("<li><a href='#' onClick='openCms("+model.getId()+")' title='").append(model.getTitle()+newImg).append("'>").append("").append(fulltitle).append(newImg).append("</a>").append("<span class='time'>").append(releaseDate).append("</span>").append("</li>\n");
						}		
						if (line == rows-1) { 
							line++;
							break;
						}
						line++;
			        }
			        html.append("</ul>\n"); 
				}
			}
		}		
		return html.toString();
	}

	public String portletWeiXinPage(UserContext me, DomainModel domainModel,IworkCmsPortlet infoModel, HashMap params) {
		// TODO Auto-generated method stub
		StringBuffer html=new StringBuffer();
//		long rows=infoModel.getProws();
//		if(rows<=0){
//			rows=10;
//		}
//		if(CmsUtil.getContentSecurityList(infoModel.getBrowse())){
//			if(CmsUtil.getEffect(infoModel.getBegindate(),infoModel.getEnddate())){
//				if(infoModel.getStatus()==0){				
//					CmsInfoDAO cmsInfoDAO= (CmsInfoDAO)SpringBeanUtil.getBean("cmsInfoDAO");
//					List<IworkCmsContent> cmsInfoList=cmsInfoDAO.getNewsList(infoModel.getId());
//					int line = 0;
//					html.append("<li data-role=\"list-divider\" data-theme=\"c\">新闻公告</li>\n");
//			        for(int i=0;i<cmsInfoList.size();i++){
//			        	IworkCmsContent model=cmsInfoList.get(new Integer(i));
//			        	if(!CmsUtil.getContentSecurityList(model.getBrowse())||model.getStatus()==1){
//			        		continue;
//			        	}     	 
//			        	String releaseDate = CmsUtil.dateShortFormat(model.getReleasedate());
//						String newImg="";
//						if (CmsUtil.getSubtractionDate(model.getReleasedate(), new Date()) <= 5) {
//							newImg = "&nbsp;(新)"; 
//						}
//							String fulltitle=CmsUtil.getCmsTitle(model);
//							html.append("<li>");
//							html.append("<a href=\"index.html\">").append("\n");
//							html.append(" <h2>").append(newImg).append(fulltitle).append("</h2>").append("\n");
//							html.append("<p>").append(model.getBrieftitle()).append("</p>");
//							html.append("<p class=\"ui-li-aside\">").append(releaseDate).append("</p>");
//							
//						if (line == rows-1) { 
//							line++;
//							break;
//						}
//						line++;
//			        }
//				}
//			}
//		}		
		return html.toString();
	}
}
