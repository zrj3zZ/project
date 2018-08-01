package com.iwork.process.runtime.pvm.impl.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;

public class SysRouteUtil
{
  public static final Long ROUTE_TYPE_FIXED = new Long(1L);

  public static final Long ROUTE_TYPE_OPTION = new Long(2L);
  public static final String ADDRESS_WEB_HTML_STR = "<input type=\"text\" name=\"receiveUser\" style=\"width:300px\" id=\"receiveUser\" value=\"\"><a href=\"###\" title=\"单选地址薄\" onclick=\"showAddress('receiveUser');\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-radiobook\"></a>";
  public static final String ADDRESS_MOBILE_HTML_STR = "<table width=\"100%\"  border=\"0\"><tr><td class=\"pageInfo\"><input type=\"text\"   data-clear-btn=\"true\" name=\"receiveUser\" id=\"receiveUser\" value=\"\"></td><td><a href=\"###\" onclick=\"showAddress('receiveUser');\" data-role=\"button\" data-rel=\"dialog\" data-transition=\"slide\" data-inline=\"true\">地址簿</a></td></tr></table>";

  public static String getAddressHTML(HashMap<String, OrgUser> userList, Long routeType, String deviceType)
  {
    List list = new ArrayList();
    if (userList != null) {
      Iterator iter = userList.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry)iter.next();
        OrgUser val = (OrgUser)entry.getValue();
        list.add(val);
      }
      if (list.size() > 0) {
        return getAddressHTML(list, routeType, deviceType);
      }
    }
    return "";
  }

  public static String getAddressHTML(List<OrgUser> userList, Long routeType, String deviceType)
  {
    StringBuffer html = new StringBuffer();
    int num = 1;

    if (routeType.equals(ROUTE_TYPE_FIXED)) {
      if (deviceType.equals("WEB")) {
        html.append("<tr>").append("\n");
        html.append("<td class=\"ItemTitle\">固定办理人:</td>").append("\n");
        html.append("<td   class=\"pageInfo\">").append("\n");
        if (userList != null) {
          if (userList.size() > 0) {
            html.append("<ul>").append("\n");
            for (OrgUser model : userList) {
              html.append("<input type=\"checkbox\"  name=\"receiveUser\" checked title=\"当前设置为固定办理人不能取消接收人\" onclick=\"return false\"   value=\"").append(model.getUserid()).append("\" id=\"receiveUser-").append(num).append("\"/>").append("<label title=\"当前设置为固定办理人不能取消接收人\"  for=\"receiveUser-").append(num).append("\" class=\"checkboxLabel\">").append(model.getUsername()).append("</label>").append("\n");
              num++;
            }
            html.append("</ul>").append("\n");
          } else {
            html.append(getAddressHTML(deviceType));
          }
        }
        else html.append(getAddressHTML(deviceType));

        html.append("</td>").append("\n");
        html.append("</tr>").append("\n");
      } else {
        html.append("<fieldset data-role=\"controlgroup\">").append("\n");
        html.append("<legend>固定办理人:</legend>").append("\n");

        if ((userList != null) && 
          (userList.size() > 0))
        {
          for (OrgUser model : userList) {
            html.append("<input type=\"checkbox\"  name=\"receiveUser\" checked title=\"当前设置为固定办理人不能取消接收人\" onclick=\"return false\"   value=\"").append(model.getUserid()).append("\" id=\"receiveUser-").append(num).append("\"/>").append("<label title=\"当前设置为固定办理人不能取消接收人\"  for=\"receiveUser-").append(num).append("\" class=\"checkboxLabel\">").append(model.getUsername()).append("</label>").append("\n");
            num++;
          }

        }

        html.append("</fieldset>").append("\n");
      }
    } else if (routeType.equals(ROUTE_TYPE_OPTION)) {
      if ((deviceType == null) || (deviceType.equals("WEB"))) {
        deviceType = "WEB";
        html.append("<tr>").append("\n");
        html.append("<td class=\"ItemTitle\">可选办理人:</td>").append("\n");
        html.append("<td   class=\"pageInfo\">").append("\n");

        if (userList != null) {
          if (userList.size() > 0) {
            String ischeck = "";
            if (userList.size() == 1) {
              ischeck = "checked";
            }
            html.append("<ul>").append("\n");
            for (OrgUser model : userList) {
              html.append("<li><input type=\"checkbox\" name=\"receiveUser\" ").append(ischeck).append(" value=\"").append(model.getUserid()).append("\" id=\"receiveUser-").append(num).append("\"/>").append("<label for=\"receiveUser-").append(num).append("\" class=\"checkboxLabel\">").append(model.getUsername()).append("</label></li>").append("\n");
              num++;
            }
            html.append("</ul>").append("\n");
          } else {
            html.append(getAddressHTML(deviceType));
          }
        }
        else getAddressHTML(deviceType);

        html.append("</td>").append("\n");
        html.append("</tr>").append("\n");
      } else {
        html.append("<fieldset data-role=\"controlgroup\">").append("\n");
        html.append("<legend>可选办理人:</legend>").append("\n");

        if (userList != null) {
          if (userList.size() > 0) {
            String ischeck = "";
            if (userList.size() == 1) {
              ischeck = "checked";
            }
            for (OrgUser model : userList) {
              html.append("<input type=\"checkbox\" name=\"receiveUser\" ").append(ischeck).append(" value=\"").append(model.getUserid()).append("\" id=\"receiveUser-").append(num).append("\"/>").append("<label for=\"receiveUser-").append(num).append("\" class=\"checkboxLabel\">").append(model.getUsername()).append("</label></li>").append("\n");
              num++;
            }
          } else {
            return getAddressHTML(deviceType);//html.append(getAddressHTML(deviceType));
          }
        }
        else html.append(getAddressHTML(deviceType));

        html.append("</fieldset>").append("\n");
      }
    }
    return html.toString();
  }

  public static String getAddressHTML(String deviceType)
  {
    String html = "";
    if (!deviceType.equals("WEB"))
      html = "<tr><td class=\"ItemTitle\"><a href=\"###\" onclick=\"showAddress('receiveUser');\" data-role=\"button\" data-rel=\"dialog\" data-transition=\"slide\" data-inline=\"true\">地址簿</a></td><td class=\"pageInfo\"><input type=\"text\"  readonly=\"readonly\" data-clear-btn=\"true\" name=\"receiveUser\" id=\"receiveUser\" value=\"\"></td></tr>";
    else {
      html = "<input type=\"text\" name=\"receiveUser\" readonly=\"readonly\" style=\"width:300px\" id=\"receiveUser\" value=\"\"><a href=\"###\" title=\"单选地址薄\" onclick=\"showAddress('receiveUser');\" class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-radiobook\"></a>";
    }
    return html;
  }

  public static String getAddressHTML(UserContext uc)
  {
    StringBuffer html = new StringBuffer();
    if (uc != null) {
      html.append("<tr>").append("\n");
      html.append("<td class=\"ItemTitle\">固定办理人:</td>").append("\n");
      html.append("<td   class=\"pageInfo\">").append("\n");
      html.append("<ul>").append("\n");
      html.append("<li><input type=\"checkbox\" name=\"receiveUser\" checked title=\"本节点为固定办理人不能取消接收人选项\" onclick=\"return false\"   value=\"").append(uc.get_userModel().getUserid()).append("\" id=\"receiveUser-1\"/>").append("<label title=\"驳回操作，不能取消接收人选项\"  for=\"receiveUser-1\" class=\"checkboxLabel\">").append(uc.get_userModel().getUsername()).append("</label></li>").append("\n");
      html.append("</ul>").append("\n");
      html.append("</td>").append("\n");
      html.append("</tr>").append("\n");
    }
    return html.toString();
  }
}