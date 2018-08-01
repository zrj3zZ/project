package com.iwork.plugs.cms.framework.impl;

import java.util.HashMap;
import java.util.List;

import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.process.desk.handle.model.TodoTaskModel;
import com.iwork.process.desk.handle.service.ProcessDeskService;

public class CmsPortletTodoTaskImpl implements CmsPortletInterface {

	private ProcessDeskService processDeskService;

	public String portletPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer sb = new StringBuffer();

		// div
		sb.append("<div id='todoDiv'>").append("\n");
		sb.append("  正在加载中...").append("\n");
		sb.append("</div>").append("\n");
		
		// js脚本
		sb.append("<script type=\"text/javascript\">").append("\n");
		sb.append("    $(function(){").append("\n");
		sb.append("        $.post('showTodoMini.action',{},function(data){").append("\n");
		sb.append("            $(\"#todoDiv\").html(data);").append("\n");
		sb.append("        });").append("\n");
		sb.append("    });").append("\n");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	public String portletWeiXinPage(UserContext me, DomainModel domainModel,
			IworkCmsPortlet infoModel, HashMap params) {
		StringBuffer sb = new StringBuffer();
		//sb.append(this.getTitleHtmlStr()); 常用资料标题
		sb.append("<ul class='item_task'>").append("\n");
		if (processDeskService == null) {
			processDeskService = (ProcessDeskService) SpringBeanUtil.getBean("processDeskService");
		}
		List<TodoTaskModel> list = processDeskService.getTaskList(null);
		boolean isMore = false;
		for (int i = 0; i < list.size(); i++) {
			TodoTaskModel model = list.get(i);
			if(model.getIsRead()!=null&&model.getIsRead().equals(new Long(0))){
				sb.append("<li><a href=\"javascript:openTaskPage('").append(model.getTitle()).append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" target=\"_blank\" >").append("<img border=\"0\" src=\"iwork_img/min_form.gif\"/>&nbsp;").append(model.getTitle()).append("</a><span style='float:right'>").append(UtilDate.getDaysBeforeNow(model.getCreateDate())).append("</span>").append("\n");
				sb.append("</li>");
			}else{
				sb.append("<li><a href=\"javascript:openTaskPage('").append(model.getTitle()).append("','").append(model.getActDefId()).append("',").append(model.getInstanceId()).append(",").append(model.getExcutionId()).append(",").append(model.getTaskId()).append(");").append("\" target=\"_blank\" ><b>").append("<img border=\"0\" src=\"iwork_img/min_form.gif\"/>&nbsp;").append(model.getTitle()).append("</b></a><span style='float:right'>").append(UtilDate.getDaysBeforeNow(model.getCreateDate())).append("</span>").append("\n");
				sb.append("</li>");
			}
			
			if(i>=10){
				isMore = true;
				break;
			}
		}
		if(isMore){
			sb.append("<li><a  style=\"float:right\" target=\"_self\" href=\"process_desk_index.action\"").append("\" target=\"_blank\" >查看更多</a>").append("\n");
		}
		sb.append("</ul>").append("\n");
		return sb.toString();
	}

	private String getTitleHtmlStr() {

		String resultStr = "<table width=\"100%\" border=\"0\"><tbody>"
				+ "<tr><td class=\"lft_tit\">待办流程</td></tr>"
				+ "</tbody></table>";
		return resultStr;
	}

}
