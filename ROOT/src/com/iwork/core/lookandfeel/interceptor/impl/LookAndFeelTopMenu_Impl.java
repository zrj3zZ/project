package com.iwork.core.lookandfeel.interceptor.impl;

import com.iwork.app.navigation.node.dao.SysNavNodeDAO;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.core.lookandfeel.interceptor.LookAndFeelAbst;
import com.iwork.core.lookandfeel.model.LookAndFeel;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.NavSecurityUtil;
import com.iwork.core.util.SpringBeanUtil;

import java.util.List;
import java.util.Map;

public class LookAndFeelTopMenu_Impl extends LookAndFeelAbst
{
  private SysNavNodeDAO sysNavNodeDAO;

  public LookAndFeelTopMenu_Impl(UserContext uc, Map params, String key)
  {
    super(uc, params, key);
  }

  public String getActionPath() {
    String actionPath = null;
    LookAndFeel model = getModel();
    if (model != null) {
      actionPath = model.getWelcome();
    }
    return actionPath;
  }

  public String getTopMenuHtml(List<SysNavSystem> list) {
    return getNavToolBar(list);
  }
  /**
 * 获得当前人显示信息
 */
public String getCurrentUserInfoHtml(){
	StringBuffer html = new StringBuffer();
	UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
	if(userContext!=null){
		html.append("<img class=\"nav-user-photo\"  onerror=\"this.src='iwork_img/nopic.gif'\"  src='iwork_file/USER_PHOTO/").append(userContext.get_userModel().getUserid()).append(".jpg'/>");
		html.append(userContext.get_userModel().getUsername());
	} 
	return html.toString();
}
  public String getLeftMenuHtml(List<SysNavSystem> list)
  {
    return null;
  }
  public String getNavToolBar(List<SysNavSystem> list) {
    StringBuffer html = new StringBuffer();
    StringBuffer subMenu = new StringBuffer();
    for (SysNavSystem system : list) {
      html.append("<li class=\"dropdown\"><a href=\"#\" class=\"easyui-menubutton\" style=\"font-size:8px;padding-right:0px;padding-left:0px;\" data-options=\"menu:'#menu").append(system.getId()).append("'");
      String sysicon = system.getSysIcon();
      /*if ((sysicon != null) && (!sysicon.equals("/"))) {
        html.append(",iconCls:'").append(sysicon).append("'");
      }*/
      html.append("\">").append(system.getSysName()).append("</a></li>").append("\n");
      subMenu.append(getSubMenuHtml(system.getId()));
    }
    html.append(subMenu);
    return html.toString();
  }

  private String getSubMenuHtml(String systemId)
  {
    StringBuffer html = new StringBuffer();
    if (this.sysNavNodeDAO == null) {
      SpringBeanUtil.getInstance(); this.sysNavNodeDAO = ((SysNavNodeDAO)SpringBeanUtil.getBean("sysNavNodeDAO"));
    }

    List<SysNavNode> list = this.sysNavNodeDAO.getChildListBySystemId(systemId);
    if ((list != null) && (list.size() > 0)) {
      html.append("<div id=\"menu").append(systemId).append("\" style=\"width:150px;\">\n");
      int num = 0;
      for (SysNavNode node : list) {
        boolean flag = NavSecurityUtil.getInstance().isCheckSecurity(node);
        if (flag)
        {
          num++;

          List<SysNavNode> sublist = this.sysNavNodeDAO.getChildListByParentId(node.getId().toString());
          StringBuffer subHtml = new StringBuffer();
          int count = 0;
          if ((sublist != null) && (sublist.size() > 0)) {
            subHtml.append("<div class=\"menu-sep\"></div>\n");
            subHtml.append("<div>\n");
            subHtml.append("<span>").append(node.getNodeName()).append("</span>\n");
            subHtml.append("<div>\n");
            for (SysNavNode subNode : sublist) {
              boolean subflag = NavSecurityUtil.getInstance().isCheckSecurity(node);
              if (subflag)
              {
                count++;

                List<SysNavNode> sublist2 = this.sysNavNodeDAO.getChildListByParentId(subNode.getId().toString());
                int subCount = 0;
                StringBuffer subHtml2 = new StringBuffer();
                if ((sublist2 != null) && (sublist2.size() > 0)) {
                  subHtml2.append("<div class=\"menu-sep\"></div>\n");
                  subHtml2.append("\t<div>\n");
                  subHtml2.append("\t<span>").append(subNode.getNodeName()).append("</span>\n");
                  subHtml2.append("\t<div>\n");
                  for (SysNavNode subNode2 : sublist2) {
                    boolean sub2flag = NavSecurityUtil.getInstance().isCheckSecurity(subNode2);
                    if (sub2flag)
                    {
                      subCount++;
                      String icon = "";
                      if ((subNode2.getNodeIcon() != null) && (!subNode2.getNodeIcon().equals("/"))) {
                        icon = subNode2.getNodeIcon();
                      }
                      subHtml2.append("<div data-options=\"iconCls:'").append(icon).append("'\" onclick=\"setRedirect('','").append(subNode2.getNodeUrl()).append("','").append(subNode2.getNodeName()).append("');\">").append(subNode2.getNodeName()).append("</div>\n");
                    }
                  }
                  subHtml2.append("\t</div>\n");
                  subHtml2.append("\t</div>\n");
                }
                if (subCount > 0) {
                  subHtml.append(subHtml2);
                } else {
                  if (count > 1) {
                    subHtml.append("<div class=\"menu-sep\"></div>\n");
                  }
                  String icon = "";
                  if ((subNode.getNodeIcon() != null) && (!subNode.getNodeIcon().equals("/"))) {
                    icon = subNode.getNodeIcon();
                  }
                  subHtml.append("<div").append(" onclick=\"setRedirect('','").append(subNode.getNodeUrl()).append("','").append(subNode.getNodeName()).append("');\">").append(subNode.getNodeName()).append("</div>\n");
                }
              }
            }
            subHtml.append("</div>\n");
            subHtml.append("</div>\n");
          }
          if (count > 0) {
            html.append(subHtml);
          } else {
            if (num > 1) {
              html.append("<div class=\"menu-sep\"></div>\n");
            }
            String icon = "";
            if ((node.getNodeIcon() != null) && (!node.getNodeIcon().equals("/"))) {
              icon = node.getNodeIcon();
            }
            html.append("<div data-options=\"iconCls:'").append(icon).append("'\" onclick=\"setRedirect('','").append(node.getNodeUrl()).append("','").append(node.getNodeName()).append("');\">").append(node.getNodeName()).append("</div>\n");
          }
        }
      }
      html.append("</div>\n");
    }

    return html.toString();
  }
}