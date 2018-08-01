package com.iwork.plugs.cms.framework.impl;

import java.util.HashMap;

import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.util.CmsUtil;

/**
 * CMS链接页面实现类
 * @author WeiGuangjian
 *
 */
public class CmsPortletIFormLinkImpl implements CmsPortletInterface {

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		// TODO Auto-generated method stub
		StringBuffer html=new StringBuffer();
		String width="100%";
		if(infoModel.getPwidth()!=null&&infoModel.getPwidth()>0){
			width=infoModel.getPwidth().toString();
		}
		String height="100%";
		if(infoModel.getPheight()!=null&&infoModel.getPheight()>0){
			height=infoModel.getPheight().toString();
		}
        String border="";
		if(infoModel.getIsborder()==0){			
			border="style='border:1px solid #DDDDDD;'";		
		}
		html.append("<table width="+width+" height="+height+"  "+border+"  cellpadding=0 cellspacing=0><tr><td>\n");
		if(infoModel.getIstitle()==0){	
			String linktarget="target='"+infoModel.getLinktarget()+"'";
			if(infoModel.getLinktarget()==null||infoModel.getLinktarget().equals("")){
				linktarget="";
			}
			String morelink="<a href='"+infoModel.getMorelink()+"' "+linktarget+" title='查看更多'><img src='iwork_img/more.jpg' /></a>";
			if(infoModel.getMorelink()==null||infoModel.getMorelink().equals("")){
				morelink="";
			}
			html.append("<div class='lft_item'>\n");
			html.append("<div class='lft_tit'><div class='lft_t_l'><span class='f_w'>"+infoModel.getPortletname()+"</span></div>"+morelink+"</div>\n");
		}
		if(CmsUtil.getContentSecurityList(infoModel.getBrowse())){
			if(CmsUtil.getEffect(infoModel.getBegindate(),infoModel.getEnddate())){
				if(infoModel.getStatus()==0){
					String url=infoModel.getParam();
					html.append("<iframe width=100% height=100%  src='"+url+"' frameborder=0  scrolling=no  marginheight=0 marginwidth=0></iframe>\n");
				}else{
					html.append("<div style='text-align:center;color:red;'>此栏目已关闭!</div>");
				}
			}else{
				html.append("<div style='text-align:center;color:red;'>此栏目已失效!</div>");
			}
		}else{
			html.append("<div style='text-align:center;color:red;'>您无权查看此栏目!</div>");
		}		
		if(infoModel.getIstitle()==0){
	        html.append("</div>\n"); 
	    }
	    html.append("</td></tr></table>\n");
		return html.toString();
	}
	
	public String portletWeiXinPage(UserContext me, DomainModel domainModel,IworkCmsPortlet infoModel, HashMap params) {
		
		return "";
	}
}
