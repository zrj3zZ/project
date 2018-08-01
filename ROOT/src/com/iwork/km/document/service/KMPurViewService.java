package com.iwork.km.document.service;

import com.iwork.commons.util.AddressBookUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.km.document.dao.KMDirectoryDAO;
import com.iwork.km.document.dao.KMDocDAO;
import com.iwork.km.document.dao.KMPurViewDAO;
import com.iwork.km.document.model.IworkKmDirectory;
import com.iwork.km.document.model.IworkKmDoc;
import com.iwork.km.document.model.IworkKmPurview;
import com.iwork.km.document.model.IworkKmPurviewItem;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;

public class KMPurViewService
{
  private FileUploadService uploadifyService;
  private KMDirectoryDAO kmDirectoryDAO;
  private KMDocDAO kmDocDAO;
  private KMFavService kmFavService;
  private OrgCompanyDAO orgCompanyDAO;
  private OrgDepartmentDAO orgDepartmentDAO;
  private OrgUserDAO orgUserDAO;
  private OrgUserMapDAO orgUserMapDAO;
  private KMPurViewDAO kmPurViewDAO;

  public String getCompanyNodeJson(String purviewGroup, Long purviewType, Long type, Long id)
  {
    StringBuffer jsonHtml = new StringBuffer();
    List items = new ArrayList();

    List orgcompanyList = this.orgCompanyDAO.getAll();
    for (int i = 0; i < orgcompanyList.size(); i++) {
      OrgCompany model = (OrgCompany)orgcompanyList.get(i);
      Map item = new HashMap();
      item.put("id", model.getId());
      item.put("name", model.getCompanyname());
      item.put("open", "true");
      item.put("nodeType", "com");
      item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
      item.put("iconClose", "iwork_img/ztree/diy/1_close.png");

      List departmentList = this.orgDepartmentDAO.getTopDepartmentList(model.getId());
      List childrenItems = new ArrayList();
      for (int j = 0; j < departmentList.size(); j++) {
        OrgDepartment dept = (OrgDepartment)departmentList.get(j);

        Map map = buildTreeDeptNode(dept, purviewGroup, purviewType, type, id);
        if (map != null) {
          childrenItems.add(map);
        }
      }
      if ((childrenItems != null) && (childrenItems.size() > 0)) {
        JSONArray childrenJson = JSONArray.fromObject(childrenItems);
        item.put("children", childrenJson.toString());
      }
      items.add(item);
    }
    JSONArray json = JSONArray.fromObject(items);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  private Map<String, Object> buildTreeDeptNode(OrgDepartment dept, String purviewGroup, Long purviewType, Long type, Long pid)
  {
    boolean flag = false;
    Map childrenItem = new HashMap();
    childrenItem.put("id", dept.getId());
    childrenItem.put("name", dept.getDepartmentname());
    childrenItem.put("open", "true");

    List deptlist = this.orgDepartmentDAO.getSubDepartmentList(dept.getId());
    if (purviewType.equals(new Long(0L))) {
      if ((deptlist != null) && (deptlist.size() > 0))
        childrenItem.put("isParent", Boolean.valueOf(true));
      else {
        childrenItem.put("isParent", Boolean.valueOf(false));
      }

      flag = isDeptPurview(dept, purviewGroup, purviewType, type, pid);
      if (flag)
        childrenItem.put("checked", Boolean.valueOf(true));
    }
    else {
      childrenItem.put("isParent", Boolean.valueOf(true));
      childrenItem.put("nocheck", Boolean.valueOf(true));
    }
    childrenItem.put("nodeType", "dept");
    childrenItem.put("icon", "iwork_img/ztree/diy/40.png");
    childrenItem.put("companyid", dept.getCompanyid());
    childrenItem.put("departmentdesc", dept.getDepartmentdesc());
    childrenItem.put("departmentno", dept.getDepartmentno());

    return childrenItem;
  }

  public String getDeptAndUserJson(Long deptId, String purviewGroup, Long purviewType, Long type, Long pid)
  {
    StringBuffer jsonHtml = new StringBuffer();
    List list = new ArrayList();

    List<OrgDepartment> deptlist = this.orgDepartmentDAO.getSubDepartmentList(deptId);
    for (OrgDepartment dept : deptlist) {
      Map childrenItem = buildTreeDeptNode(dept, purviewGroup, purviewType, type, pid);
      if (childrenItem != null) {
        list.add(childrenItem);
      }
    }

    if (purviewType.equals(new Long(1L))) {
      List currentUserList = new ArrayList();

      List<OrgUser> userList = this.orgUserDAO.getActiveUserList(deptId);
      Map item;
      for (OrgUser model : userList) {
        if ((model.getUsertype() != null) && (model.getUsertype().equals(new Long(0L)))) {
          item = buildTreeUserNode(model, purviewGroup, purviewType, type, pid);
          if (item != null) {
            list.add(item);
            currentUserList.add(model);
          }
        }
      }

      List<OrgUserMap> userMapList = this.orgUserMapDAO.getOrgUserMap_DeptId(deptId.intValue(), "0");
      for (OrgUserMap oum : userMapList) {
        String userid = oum.getUserid();
        if (userid != null)
        {
          OrgUser model = this.orgUserDAO.getUserModel(userid);
          if (model != null) {
            Long userstatus = model.getUserstate();
            if (userstatus.equals(new Long(0L)))
            {
              if (!currentUserList.contains(model))
              {
                if ((model.getUsertype() != null) && (model.getUsertype().equals(new Long(0L)))) {
                   item = new HashMap();
                  item.put("id", model.getUserid());
                  item.put("name", model.getUsername());
                  if ((oum.getIsmanager() == null) || (oum.getIsmanager().equals("0")))
                    item.put("icon", "iwork_img/user_business_boss.png");
                  else {
                    item.put("icon", "iwork_img/user.png");
                  }
                  item.put("nodeType", "user");
                  item.put("userName", model.getUsername());
                  item.put("userId", model.getUserid());
                  String useraddress = AddressBookUtil.generateUid(model.getUserid(), model.getUsername());

                  boolean flag = isUserPurview(model, purviewGroup, purviewType, type, pid);
                  if (flag) {
                    item.put("checked", Boolean.valueOf(true));
                  }

                  item.put("useraddress", useraddress);
                  item.put("userno", model.getUserno());
                  item.put("deptname", oum.getDepartmentname());
                  item.put("deptId", oum.getDepartmentid());
                  item.put("orgroleid", oum.getOrgroleid());
                  list.add(item);
                  currentUserList.add(model);
                }
              }
            }
          }
        }
      }
    }
    JSONArray json = JSONArray.fromObject(list);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  private Map<String, Object> buildTreeUserNode(OrgUser model, String purviewGroup, Long purviewType, Long type, Long pid)
  {
    Map item = new HashMap();
    item.put("id", model.getUserid());
    item.put("name", model.getUsername());
    if (model.getIsmanager().equals(new Long(1L)))
      item.put("icon", "iwork_img/user_business_boss.png");
    else if ((model.getUsertype() != null) && (model.getUsertype().equals(new Long(0L)))) {
      item.put("icon", "iwork_img/user.png");
    }

    String useraddress = AddressBookUtil.generateUid(model.getUserid(), model.getUsername());

    boolean flag = isUserPurview(model, purviewGroup, purviewType, type, pid);
    if (flag) {
      item.put("checked", Boolean.valueOf(true));
    }

    item.put("nodeType", "user");
    item.put("userName", model.getUsername());
    item.put("userId", model.getUserid());
    item.put("useraddress", useraddress);
    item.put("userno", model.getUserno());
    item.put("deptname", model.getDepartmentname());
    item.put("deptId", model.getDepartmentid());
    item.put("orgroleid", model.getOrgroleid());
    return item;
  }

  public void addPurview(String purviewGroup, Long purviewType, String ids, Long type, Long PId)
  {
    String[] idlist = ids.split(",");
    String userid = UserContextUtil.getInstance().getCurrentUserId();
    IworkKmPurview model = this.kmPurViewDAO.getPurview(purviewGroup, purviewType, PId);
    if (model == null) {
      model = new IworkKmPurview();
      model.setPurType(purviewType);
      model.setType(purviewGroup);
      model.setPUser(userid);
      model.setPTime(new Date());
      model.setPId(PId);
      model = this.kmPurViewDAO.addPurview(model);
    } else if (model != null) {
      model.setPurType(purviewType);
      model.setType(purviewGroup);
      model.setPUser(userid);
      model.setPTime(new Date());
      model.setPId(PId);
      model = this.kmPurViewDAO.updatePurview(model);
    }
    if (model != null)
    {
      this.kmPurViewDAO.deletePurviewItem(model.getId(), type);

      for (String id : idlist)
        if (!"".equals(id)) {
          IworkKmPurviewItem item = new IworkKmPurviewItem();
          item.setPurId(model.getId());
          item.setType(type);
          item.setPurview(id);
          this.kmPurViewDAO.addPurviewItem(item);
        }
    }
  }

  public boolean isSetPurview(String purviewGroup, Long pid)
  {
    return this.kmPurViewDAO.isSetPurview(pid, purviewGroup);
  }

  public boolean isDeptPurview(OrgDepartment dept, String purviewGroup, Long purviewType, Long type, Long pid)
  {
    boolean flag = false;
    IworkKmPurview model = this.kmPurViewDAO.getPurview(purviewGroup, purviewType, pid);
    if (model == null) {
      flag = false;
    }
    else {
      List<IworkKmPurviewItem> list = this.kmPurViewDAO.getPurviewItemList(model.getId(), type);
      for (IworkKmPurviewItem ikpi : list) {
        if (ikpi.getPurview() != null) {
          Long purviewId = new Long(0L);
          try {
            purviewId = Long.valueOf(Long.parseLong(ikpi.getPurview()));
          }
          catch (Exception localException) {
          }
          if (purviewId.equals(dept.getId())) {
            flag = true;
            break;
          }
        }
      }
    }
    return flag;
  }

  public boolean isUserPurview(OrgUser orguser, String purviewGroup, Long purviewType, Long type, Long pid)
  {
    boolean flag = false;
    IworkKmPurview model = this.kmPurViewDAO.getPurview(purviewGroup, purviewType, pid);
    if (model == null) {
      flag = false;
    }
    else {
      List<IworkKmPurviewItem> list = this.kmPurViewDAO.getPurviewItemList(model.getId(), type);
      for (IworkKmPurviewItem ikpi : list) {
        if ((ikpi.getPurview() != null) && 
          (ikpi.getPurview().equals(orguser.getUserid()))) {
          flag = true;
          break;
        }
      }
    }

    return flag;
  }

  public String getPurviewList(String purviewGroup, Long pid)
  {
    StringBuffer html = new StringBuffer();
    html.append("<table width=\"50%\" border=\"0\" style=\"border:1px solid #999;line-height:20px\"  cellspacing=\"0\" cellpadding=\"0\">").append("\n");
    html.append("<tr style='background:#efefef'>").append("\n");
    html.append("<th align=\"right\" style=\"padding-right:10px;\">").append("授权单元").append("</th>").append("\n");
    html.append("<th align=\"center\" style=\"padding-right:10px;\">").append("授权类型").append("</th>").append("\n");
    html.append("</tr>").append("\n");

    IworkKmPurview model = this.kmPurViewDAO.getPurview(purviewGroup, new Long(0L), pid);
    if (model != null)
    {
      List<IworkKmPurviewItem> list = this.kmPurViewDAO.getPurviewItemList(model.getId());
      for (IworkKmPurviewItem ikpi : list) {
        html.append("<tr >").append("\n");
        Long type = ikpi.getType();
        String deptid = ikpi.getPurview();
        OrgDepartment dept = this.orgDepartmentDAO.getBoData(Long.valueOf(Long.parseLong(deptid)));
        if (dept != null) {
          html.append("<td style=\"font-family:宋体;font-size:12px;text-align:right;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">").append(dept.getDepartmentname()).append("</td>").append("\n");
          if (type.equals(new Long(0L)))
            html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">只读权限</td>").append("\n");
          else {
            html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">管理权限</td>").append("\n");
          }
        }
        html.append("</tr>").append("\n");
      }

    }

    model = this.kmPurViewDAO.getPurview(purviewGroup, new Long(1L), pid);
    if (model != null) {
      List<IworkKmPurviewItem> list = this.kmPurViewDAO.getPurviewItemList(model.getId());
      for (IworkKmPurviewItem ikpi : list) {
        html.append("<tr >").append("\n");
        Long type = ikpi.getType();
        String userId = ikpi.getPurview();
        UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
        if (uc != null) {
          OrgUser orguser = uc.get_userModel();
          String str = orguser.getUsername() + "[" + orguser.getUserid() + "]";
          html.append("<td style=\"font-family:宋体;font-size:12px;text-align:right;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">").append(str).append("</td>").append("\n");
          if (type.equals(new Long(0L)))
            html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">只读权限</td>").append("\n");
          else {
            html.append("<td style=\"font-family:宋体;font-size:12px;text-align:center;padding-right:10px;line-height:20px;border-bottom:1px dotted #999\">管理权限</td>").append("\n");
          }
        }
        html.append("</tr>").append("\n");
      }
    }
    html.append("</table>").append("\n");
   // System.out.println(html.toString());
    return html.toString();
  }

  public String loadPurviewBtn(Long id, String purviewGroup)
  {
    StringBuffer html = new StringBuffer();
    OrgDepartment dept = UserContextUtil.getInstance().getCurrentUserContext()._deptModel;
    OrgUser orguser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();

    boolean flag = false;
    String createUser = "ADMIN";
    if (purviewGroup.equals("doc")) {
      IworkKmDoc model = this.kmDocDAO.getDocModel(id);
      if (model != null)
        createUser = model.getCreateUser();
    }
    else if (purviewGroup.equals("folder")) {
      IworkKmDirectory ikd = this.kmDirectoryDAO.getDirectoryModel(id);
      if (ikd != null) {
        createUser = ikd.getCreateuser();
      }
    }

    if ((orguser.getUserid().equals(createUser)) || (SecurityUtil.isSuperManager())) {
      flag = true;
    } else {
      flag = isDeptPurview(dept, purviewGroup, new Long(0L), new Long(1L), id);
      if (!flag) {
        flag = isUserPurview(orguser, purviewGroup, new Long(1L), new Long(1L), id);
      }
    }
    if (flag) {
      html.append("<div id=\"sqsx\" class=\"tools_nav\">");
      html.append("<a href=\"#\"  plain=\"true\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" onclick=\"javascript:addPurview(").append(id).append(",'").append(purviewGroup).append("',0);\" title=\"授权用户阅读文档\">浏览授权</a>\n");
      html.append("<a href=\"#\"  plain=\"true\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" onclick=\"javascript:addPurview(").append(id).append(",'").append(purviewGroup).append("',1);\" title=\"授权用户管理维护\">管理授权</a>\n");
      html.append("</div>");
    }

    return html.toString();
  }

  public void setUploadifyService(FileUploadService uploadifyService) {
    this.uploadifyService = uploadifyService;
  }
  public void setKmDirectoryDAO(KMDirectoryDAO kmDirectoryDAO) {
    this.kmDirectoryDAO = kmDirectoryDAO;
  }
  public void setKmDocDAO(KMDocDAO kmDocDAO) {
    this.kmDocDAO = kmDocDAO;
  }
  public void setKmFavService(KMFavService kmFavService) {
    this.kmFavService = kmFavService;
  }
  public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
    this.orgCompanyDAO = orgCompanyDAO;
  }
  public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
    this.orgDepartmentDAO = orgDepartmentDAO;
  }
  public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
    this.orgUserDAO = orgUserDAO;
  }
  public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
    this.orgUserMapDAO = orgUserMapDAO;
  }
  public void setKmPurViewDAO(KMPurViewDAO kmPurViewDAO) {
    this.kmPurViewDAO = kmPurViewDAO;
  }
  public KMPurViewDAO getKmPurViewDAO() {
    return this.kmPurViewDAO;
  }
}