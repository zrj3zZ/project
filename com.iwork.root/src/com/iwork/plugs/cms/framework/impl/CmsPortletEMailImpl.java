package com.iwork.plugs.cms.framework.impl;

import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.service.RecIWorkMailClientService;

public class CmsPortletEMailImpl implements CmsPortletInterface {

	private RecIWorkMailClientService recIWorkMailClientService;
	
	
	@SuppressWarnings("unchecked")
	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		
		StringBuffer sb = new StringBuffer();
		
		// div
		sb.append("<span id='emailDiv'>").append("\n");
		sb.append("  正在加载中...").append("\n");
		sb.append("</span>").append("\n");
		
		// js脚本
		sb.append("<script type=\"text/javascript\">").append("\n");
		sb.append("    $(function(){").append("\n");
		sb.append("        $.post('showEmailMini.action',{},function(data){").append("\n");
		sb.append("            $(\"#emailDiv\").html(data);").append("\n");
		sb.append("        });").append("\n");
		sb.append("    });").append("\n");
		sb.append("</script>");
		
//		sb.append("<ul class='item_task'>").append("\n");
//		
//		// 获取当前登录用户ID
//		String userId = UserContextUtil.getInstance().getCurrentUserId();
//		
//		if (recIWorkMailClientService == null) {
//			recIWorkMailClientService = (RecIWorkMailClientService) SpringBeanUtil.getBean("reciWorkMailClientService");
//		}
//		
//		// 查询当前登陆用户状态为"0"的收件箱邮件
//		List<MailTaskModel> list = recIWorkMailClientService.queryPageMails(userId, 10, 0, 0, 9, -2);
//		
//		// 定义是否存在更多记录
//		boolean isMore = false;
//		
//		for(int i=0; i<list.size(); i++){
//			
//			MailTaskModel mail = list.get(i);
//			if(mail!=null && !"".equals(mail)){
//				if(mail.getIsRead()==0){
//					sb.append("<li><a href=\"###\" onclick=\"openEmailDetailInfo(").append(mail.getBindId()).append(",").append(mail.getId()).append(",'").append(mail.getTitle()).append("',").append(mail.getMailBox()).append(");").append("\" target=\"_blank\" >").append("<img border=\"0\" src=\"iwork_img/sysletter/mail_no_readed.png\"/>&nbsp;").append(mail.getTitle()).append("</a><span style='float:right'>").append(UtilDate.getDaysBeforeNow(mail.getCreateTime())).append("</span>").append("\n");
//					sb.append("</li>");
//				}else{
//					sb.append("<li><a href=\"###\" onclick=\"openEmailDetailInfo(").append(mail.getBindId()).append(",").append(mail.getId()).append(",'").append(mail.getTitle()).append("',").append(mail.getMailBox()).append(");").append("\" target=\"_blank\" >").append("<img border=\"0\" src=\"iwork_img/sysletter/mail_have_readed.png\"/>&nbsp;").append(mail.getTitle()).append("</a><span style='float:right'>").append(UtilDate.getDaysBeforeNow(mail.getCreateTime())).append("</span>").append("\n");
//					sb.append("</li>");
//				}
//			}
//			
//			if(i>=10){
//				isMore = true;
//				break;
//			}
//		}
//		
//		if(isMore){
//			sb.append("<li><a  style=\"float:right\" target=\"_self\" href=\"iwork_email_index.action\"").append("\" target=\"_blank\" >查看更多</a>").append("\n");
//		}
//		sb.append("</ul>").append("\n");
		
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String portletWeiXinPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<ul class='item_task'>").append("\n");
		
		// 获取当前登录用户ID
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		
		if (recIWorkMailClientService == null) {
			recIWorkMailClientService = (RecIWorkMailClientService) SpringBeanUtil.getBean("reciWorkMailClientService");
		}
		
		// 查询当前登陆用户状态为"0"的收件箱邮件
		List<MailTaskModel> list = recIWorkMailClientService.queryPageMails(userId, 10, 0, 0, 9, -2);
		
		// 定义是否存在更多记录
		boolean isMore = false;
		
		for(int i=0; i<list.size(); i++){
			
			MailTaskModel mail = list.get(i);
			if(mail!=null && !"".equals(mail)){
				if(mail.getIsRead()==0){
					sb.append("<li><a href=\"###\" onclick=\"openEmailDetailInfo(").append(mail.getBindId()).append(",").append(mail.getId()).append(",'").append(mail.getTitle()).append("',").append(mail.getMailBox()).append(");").append("\" target=\"_blank\" >").append("<img border=\"0\" src=\"iwork_img/sysletter/mail_no_readed.png\"/>&nbsp;").append(mail.getTitle()).append("</a><span style='float:right'>").append(UtilDate.getDaysBeforeNow(mail.getCreateTime())).append("</span>").append("\n");
					sb.append("</li>");
				}else{
					sb.append("<li><a href=\"###\" onclick=\"openEmailDetailInfo(").append(mail.getBindId()).append(",").append(mail.getId()).append(",'").append(mail.getTitle()).append("',").append(mail.getMailBox()).append(");").append("\" target=\"_blank\" >").append("<img border=\"0\" src=\"iwork_img/sysletter/mail_have_readed.png\"/>&nbsp;").append(mail.getTitle()).append("</a><span style='float:right'>").append(UtilDate.getDaysBeforeNow(mail.getCreateTime())).append("</span>").append("\n");
					sb.append("</li>");
				}
			}
			
			if(i>=10){
				isMore = true;
				break;
			}
		}
		
		if(isMore){
			sb.append("<li><a  style=\"float:right\" target=\"_self\" href=\"iwork_email_index.action\"").append("\" target=\"_blank\" >查看更多</a>").append("\n");
		}
		sb.append("</ul>").append("\n");
		
		return sb.toString();
	}
}
