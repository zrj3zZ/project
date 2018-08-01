package com.iwork.core.security.purview.service;

import com.iwork.app.log.util.LogUtil;
import com.iwork.app.navigation.node.dao.SysNavNodeDAO;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.app.navigation.operation.dao.SysNavOperationDAO;
import com.iwork.app.navigation.operation.model.SysNavOperation;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgRoleGroup;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.security.purview.dao.SysPurGroupDAO;
import com.iwork.core.security.purview.dao.SysPurviewDemDAO;
import com.iwork.core.security.purview.dao.SysPurviewOrgDAO;
import com.iwork.core.security.purview.dao.SysPurviewProcessDAO;
import com.iwork.core.security.purview.dao.SysPurviewReportDAO;
import com.iwork.core.security.purview.dao.SysPurviewRoleDAO;
import com.iwork.core.security.purview.dao.SysPurviewSchemaDAO;
import com.iwork.core.security.purview.model.SysPurGroup;
import com.iwork.core.server.servicemanager.dao.SysServiceManagerDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class SysPurGroupService
{
  private SysPurGroupDAO sysPurGroupDAO;
  private SysNavOperationDAO sysNavOperationDAO;
  private SysNavSystemDAO sysNavSystemDAO;
  private SysPurviewSchemaDAO sysPurviewSchemaDAO;
  private SysNavNodeDAO sysNavNodeDAO;
  private OrgRoleDAO orgRoleDAO;
  private OrgUserDAO orgUserDAO;
  private OrgDepartmentDAO orgDepartmentDAO;
  private OrgCompanyDAO orgCompanyDAO;
  private SysServiceManagerDAO sysServiceManagerDAO;
  private SysPurviewDemDAO sysPurviewDemDAO;
  private SysPurviewOrgDAO sysPurviewOrgDAO;
  private SysPurviewProcessDAO sysPurviewProcessDAO;
  private SysPurviewReportDAO sysPurviewReportDAO;
  private SysPurviewRoleDAO sysPurviewRoleDAO;

  public String getTreeSysJson(String nodeId, String purviewid, boolean isCheckBox)
  {
    StringBuffer html = new StringBuffer();
    String operTree = "";
    if (nodeId.equals("0")) {
      List systemList = this.sysNavSystemDAO.getAll();
      List sysSecuritylist = this.sysPurviewSchemaDAO.getPurViewList("SYS", purviewid);
      List rows = new ArrayList();
      if (systemList != null) {
        for (int i = 0; i < systemList.size(); i++)
          if (systemList.get(i) != null) {
            SysNavSystem sns = (SysNavSystem)systemList.get(i);
            Map item = new HashMap();

            boolean flag = SecurityUtil.isPurviewSchemaCheck(sysSecuritylist, Long.valueOf(Long.parseLong(sns.getId())));
            if (flag)
              item.put("checked", Boolean.valueOf(true));
            else {
              item.put("checked", Boolean.valueOf(false));
            }
            if (!isCheckBox) {
              item.put("nocheck", Boolean.valueOf(true));
            }

            item.put("id", sns.getId());
            item.put("name", sns.getSysName());
            List nodeList = this.sysNavNodeDAO.getChildListBySystemId(sns.getId());
            if (nodeList != null) {
              int count = nodeList.size();
              if (count > 0)
                item.put("isParent", "true");
              else {
                item.put("isParent", "false");
              }
            }

            item.put("iconOpen", "iwork_img/images/folder-open.gif");
            item.put("iconClose", "iwork_img/images/folder.gif");
            item.put("nodeType", "SYS");
            item.put("target", sns.getSysTarget());
            item.put("pageurl", sns.getSysUrl());
            rows.add(item);
          }
      }
      JSONArray json = JSONArray.fromObject(rows);
      html.append(json);
    }
    return html.toString();
  }

  public String getTreeNodeJson(String nodeId, String nodeType, String purviewid)
  {
    StringBuffer html = new StringBuffer();
    List list = null;
    if (nodeType.equals("SYS"))
      list = this.sysNavNodeDAO.getChildListBySystemId(nodeId);
    else {
      list = this.sysNavNodeDAO.getChildListByParentId(nodeId);
    }
    List sysSecuritylist = this.sysPurviewSchemaDAO.getPurViewList("NODE", purviewid);
    List rows = new ArrayList();
    if (list != null) {
      for (int i = 0; i < list.size(); i++)
        if (list.get(i) != null) {
          SysNavNode snd = (SysNavNode)list.get(i);
          Map item = new HashMap();
          item.put("id", snd.getId());
          item.put("name", snd.getNodeName());
          item.put("iconOpen", "iwork_img/images/folder-open.gif");
          item.put("iconClose", "iwork_img/images/folder.gif");
          item.put("icon", "iwork_img/domain.gif");

          boolean flag = SecurityUtil.isPurviewSchemaCheck(sysSecuritylist, snd.getId());
          if (flag)
            item.put("checked", Boolean.valueOf(true));
          else {
            item.put("checked", Boolean.valueOf(false));
          }
          int count = this.sysNavNodeDAO.getChildListByParentId(snd.getId().toString()).size();
          if (count > 0)
            item.put("isParent", "true");
          else {
            item.put("isParent", "false");
          }
          item.put("target", snd.getNodeTarget());
          item.put("pageurl", snd.getNodeUrl());
          item.put("nodeType", "NODE");
          rows.add(item);
        }
    }
    JSONArray json = JSONArray.fromObject(rows);
    html.append(json);
    return html.toString();
  }

  public String getOperationNodeJson(String nodeId, String nodeType, String purviewid)
  {
    StringBuffer html = new StringBuffer();
    List list = null;
    if (nodeType.equals("SYS"))
      list = this.sysNavNodeDAO.getChildListBySystemId(nodeId);
    else {
      list = this.sysNavNodeDAO.getChildListByParentId(nodeId);
    }
    List sysSecuritylist = this.sysPurviewSchemaDAO.getPurViewList("NODE", purviewid);
    List rows = new ArrayList();
    if (list != null)
    {
      for (int i = 0; i < list.size(); i++) {
        if (list.get(i) != null) {
          SysNavNode snd = (SysNavNode)list.get(i);

          boolean flag = SecurityUtil.isPurviewSchemaCheck(sysSecuritylist, snd.getId());
          if (!flag) {
            continue;
          }
          HashMap item = new HashMap();
          item.put("id", snd.getId());
          item.put("name", snd.getNodeName());
          item.put("iconOpen", "iwork_img/images/folder-open.gif");
          item.put("iconClose", "iwork_img/images/folder.gif");
          item.put("icon", "iwork_img/domain.gif");

          item.put("isParent", "true");

          item.put("nodeType", "NODE");
          item.put("nocheck", Boolean.valueOf(true));
          rows.add(item);
        }
      }
      List operSecList = this.sysPurviewSchemaDAO.getPurViewList("OPERA", purviewid);

      List<SysNavOperation> operationList = this.sysNavOperationDAO.getOperationList(nodeType, nodeId);
      for (SysNavOperation model : operationList) {
        Map item = new HashMap();
        item.put("id", model.getId());
        item.put("name", model.getOname());
        item.put("icon", "iwork_img/domain.gif");
        item.put("iconOpen", "iwork_img/images/folder-open.gif");
        item.put("iconClose", "iwork_img/images/folder.gif");

        boolean flag = false;
        if (operSecList != null) {
          flag = SecurityUtil.isPurviewSchemaCheck(operSecList, Long.valueOf(Long.parseLong(model.getId())));
        }
        if (flag)
          item.put("checked", Boolean.valueOf(true));
        else {
          item.put("checked", Boolean.valueOf(false));
        }
        item.put("isParent", "false");
        item.put("nodeType", "OPERA");
        rows.add(item);
      }
    }

    JSONArray json = JSONArray.fromObject(rows);
    html.append(json);
    return html.toString();
  }

  public String showGroupList(String gorupname)
  {
    StringBuffer html = new StringBuffer();
    HashMap hash = new HashMap();
    List<SysPurGroup> list = this.sysPurGroupDAO.getAll();
    for (SysPurGroup model : list) {
      String category = model.getCategoryname();
      if (category != null) {
        Object obj = hash.get(category);
        if (obj == null) {
          hash.put(category, category);
        }
      }
    }
    if (hash.size() > 0) {
      Iterator iterator = hash.entrySet().iterator();
      int count = 0;
      html.append("<table class=\"layout-grid\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:170px;\" > ").append("\n");
      html.append("\t<tr>").append("\n");
      html.append("\t\t<td class=\"left-nav\">").append("\n");
      html.append("\t\t\t<dl class=\"demos-nav\">").append("\n");
      if (gorupname == null)
        html.append("\t\t\t\t<dd><a class=\"selected\" href=\"purgroup_index.action\" target=\"_self\">全部</a></dd>").append("\n");
      else {
        html.append("\t\t\t\t<dd><a href=\"purgroup_index.action\" target=\"_self\">全部</a></dd>").append("\n");
      }
      while (iterator.hasNext()) {
        Map.Entry entry = (Map.Entry)iterator.next();
        Object value = entry.getValue();
        Object key = entry.getKey();
        if (key.equals(gorupname))
          html.append("\t\t\t\t<dd><a class=\"selected\" href=\"purgroup_index.action?categoryname=").append(value).append("\" target=\"_self\">").append(value).append("</a></dd>").append("\n");
        else {
          html.append("\t\t\t\t<dd><a href=\"purgroup_index.action?categoryname=").append(value).append("\"  target=\"_self\">").append(value).append("</a></dd>").append("\n");
        }
      }
      html.append("\t\t\t</dl>").append("\n");
      html.append("\t\t</td>").append("\n");
      html.append("\t</tr>").append("\n");
      html.append("</table> ").append("\n");
    }

    return html.toString();
  }

  public void addBoData(SysPurGroup obj) {
    this.sysPurGroupDAO.addBoData(obj);
    StringBuffer log = new StringBuffer();
    log.append(",创建了一个新的名称为[").append(obj.getGroupname()).append("]权限组（").append("id:").append(obj.getId()).append(")");
    LogUtil.getInstance().addLog(obj.getId(), "【权限组创建】", log.toString());
  }

  public void deleteBoData(Long id)
  {
    SysPurGroup model = this.sysPurGroupDAO.getBoData(id);
    if (model != null)
    {
      this.sysPurviewReportDAO.removePurviewReportList(id);

      this.sysPurviewSchemaDAO.removePurViewList(id);

      this.sysPurviewProcessDAO.removePurviewProcessList(id);

      this.sysPurviewDemDAO.removePurviewDemList(id);

      this.sysPurviewOrgDAO.removePurviewList(id);

      this.sysPurviewRoleDAO = new SysPurviewRoleDAO();
      this.sysPurviewRoleDAO.removePurViewRoleList(id);

      this.sysPurGroupDAO.deleteBoData(model);
    }
    StringBuffer log = new StringBuffer();
    log.append("删除了一个新的名称为[").append(model.getGroupname()).append("]权限组（").append("id:").append(model.getId()).append(")");
    LogUtil.getInstance().addLog(model.getId(), "【权限组删除】", log.toString());
  }

  public List getAll() {
    return this.sysPurGroupDAO.getAll();
  }

  public SysPurGroup getBoData(Long id)
  {
    return this.sysPurGroupDAO.getBoData(id);
  }

  public List getBoDatas(int pageSize, int startRow)
  {
    return this.sysPurGroupDAO.getBoDatas(pageSize, startRow);
  }

  public String getMaxID()
  {
    return this.sysPurGroupDAO.getMaxID();
  }

  public int getRows()
  {
    return this.sysPurGroupDAO.getRows();
  }

  public void updateBoData(SysPurGroup obj) {
    this.sysPurGroupDAO.updateBoData(obj);
  }

  public void moveUp(int id)
  {
    String type = "up";
    this.sysPurGroupDAO.updateIndex(id, type);
  }

  public void moveDown(int id)
  {
    String type = "down";
    this.sysPurGroupDAO.updateIndex(id, type);
  }

  public boolean setPurviewGroup(String purviewid, String schemalist)
  {
    boolean flag = false;
    if (schemalist!=null) {
      String[] list = schemalist.split(",");
      flag = this.sysPurviewSchemaDAO.setPurViewModel(purviewid, list);
    }else{
    	
    }
    return flag;
  }

  public boolean setPurviewOperation(String purviewid, String schemalist)
  {
    boolean flag = false;
    if (schemalist!=null) {
      String[] list = schemalist.split(",");
      flag = this.sysPurviewSchemaDAO.setOperationPurview(purviewid, list);
    }
    return flag;
  }

  public String getPurviewRoleJson(String purviewid)
  {
    StringBuffer html = new StringBuffer();
    List rows = new ArrayList();

    List<OrgRoleGroup> grouplist = this.orgRoleDAO.getRoleGroupList();
    List sysSecurityRolelist = new SysPurviewRoleDAO().getPurViewRoleList(purviewid);
    for (OrgRoleGroup model : grouplist) {
      HashMap hash = new HashMap();
      hash.put("id", model.getId());
      hash.put("type", "group");
      hash.put("open", Boolean.valueOf(true));
      hash.put("name", model.getGroupName());
      hash.put("children", getRoleJson(model.getId(), purviewid, sysSecurityRolelist));
      rows.add(hash);
    }
    JSONArray json = JSONArray.fromObject(rows);
    html.append(json);
    return html.toString();
  }

  private List getRoleJson(Long groupId, String purviewid, List sysSecurityRolelist)
  {
    StringBuffer html = new StringBuffer();
    List rows = new ArrayList();
    List<OrgRole> list = this.orgRoleDAO.getRoleList(groupId);
    for (OrgRole model : list) {
      HashMap hash = new HashMap();
      hash.put("id", model.getId());
      hash.put("name", model.getRolename());

      if (SecurityUtil.isPurviewRoleCheck(sysSecurityRolelist, Integer.parseInt(model.getId()))) {
        hash.put("checked", Boolean.valueOf(true));
      }
      hash.put("type", "roleid");
      hash.put("icon", "iwork_img/user_business_boss.png");
      rows.add(hash);
    }
    JSONArray json = JSONArray.fromObject(rows);
    html.append(json);
    return rows;
  }

  public boolean setPurviewToRole(Long roleId, String purviewList)
  {
    boolean flag = false;
    SysPurviewRoleDAO spdao = new SysPurviewRoleDAO();
    if (purviewList == null) {
      purviewList = "";
    }
    String[] list = purviewList.split(",");
    flag = spdao.setRolePurViewList(roleId, list);
    return flag;
  }

  public String getPurviewOrgTree(String purviewid)
  {
    StringBuffer htmltree = new StringBuffer();
    if (purviewid == null) {
      UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
      OrgCompany localOrgCompany = uc._companyModel;
    }

    return "";
  }

  public String getOrgTreeJson(String purviewid, Long parentid)
  {
    StringBuffer jsonHtml = new StringBuffer();

    List items = new ArrayList();
    if ((parentid != null) && (parentid.equals(new Long(0L)))) {
      UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

      if (SecurityUtil.isSuperManager()) {
        List orgCompanyList = this.orgCompanyDAO.getAll();
        for (int i = 0; i < orgCompanyList.size(); i++) {
          OrgCompany orgCompany = (OrgCompany)orgCompanyList.get(i);
          if (orgCompany != null) {
            Map company = new HashMap();
            company.put("id", "c" + orgCompany.getId());
            company.put("text", orgCompany.getCompanyname());
            company.put("iconCls", "icon-ok");
            company.put("children", getTopDeptJson(orgCompany.getId(), purviewid));
            Map attributes = new HashMap();

            attributes.put("id", orgCompany.getId());
            attributes.put("type", "company");
            company.put("attributes", attributes);
            items.add(company);
          }
        }
      } else {
        OrgCompany orgCompany = uc.get_companyModel();
        Map company = new HashMap();
        company.put("id", "c" + orgCompany.getId());
        company.put("text", orgCompany.getCompanyname());
        company.put("iconCls", "icon-ok");
        company.put("children", getTopDeptJson(orgCompany.getId(), purviewid));
        Map attributes = new HashMap();

        attributes.put("id", orgCompany.getId());
        attributes.put("type", "company");
        company.put("attributes", attributes);

        items.add(company);
      }
    } else {
      items = getChildrenJson(parentid, purviewid);
    }

    JSONArray json = JSONArray.fromObject(items);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  private List<Map<String, Object>> getTopDeptJson(String companyId, String purviewid)
  {
    List sub_items = new ArrayList();
    SysPurviewOrgDAO sysPurviewOrgDAO = new SysPurviewOrgDAO();
    List list = this.orgDepartmentDAO.getTopDepartmentList(companyId);
    for (int i = 0; i < list.size(); i++) {
      Map item = new HashMap();
      OrgDepartment orgDepartment = (OrgDepartment)list.get(i);
      item.put("id", orgDepartment.getId());
      item.put("text", orgDepartment.getDepartmentname());
      item.put("state", "closed");
      boolean flag = sysPurviewOrgDAO.isOrgCheck("dept", Long.valueOf(orgDepartment.getId().longValue()), purviewid);
      item.put("checked", Boolean.valueOf(flag));

      Map attributes = new HashMap();

      attributes.put("id", orgDepartment.getId());
      attributes.put("type", "dept");

      item.put("attributes", attributes);
      sub_items.add(item);
    }
    return sub_items;
  }

  private boolean isChildrenJson(Long departmentid)
  {
    boolean flag = false;
    List list = this.orgDepartmentDAO.getSubDepartmentList(departmentid);
    if (list != null) {
      if (list.size() > 0)
        flag = true;
      else {
        flag = false;
      }
    }
    return flag;
  }

  private List<Map<String, Object>> getChildrenJson(Long departmentid, String purviewid)
  {
    List sub_items = new ArrayList();
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();

    List deptlist = this.orgDepartmentDAO.getSubDepartmentList(departmentid);
    SysPurviewOrgDAO sysPurviewOrgDAO = new SysPurviewOrgDAO();
    for (int i = 0; i < deptlist.size(); i++) {
      Map item = new HashMap();
      OrgDepartment orgDepartment = (OrgDepartment)deptlist.get(i);
      item.put("id", orgDepartment.getId());
      item.put("text", orgDepartment.getDepartmentname());
      item.put("iconCls", "icon-ok");
      item.put("state", "closed");
      boolean flag = sysPurviewOrgDAO.isOrgCheck("dept", Long.valueOf(orgDepartment.getId().longValue()), purviewid);
      item.put("checked", Boolean.valueOf(flag));
      Map attributes = new HashMap();

      attributes.put("id", orgDepartment.getId());
      attributes.put("type", "dept");
      item.put("attributes", attributes);

      sub_items.add(item);
    }
    List userList = this.orgUserDAO.getActiveUserList(departmentid);
    for (int i = 0; i < userList.size(); i++) {
      Map item = new HashMap();
      OrgUser orgUser = (OrgUser)userList.get(i);
      item.put("id", orgUser.getId());
      item.put("text", orgUser.getUsername() + "[" + orgUser.getUserid() + "]");
      item.put("iconCls", "icon-org-user");
      item.put("state", "open");
      boolean flag = sysPurviewOrgDAO.isOrgCheck("user", orgUser.getId(), purviewid);
      item.put("checked", Boolean.valueOf(flag));

      Map attributes = new HashMap();

      attributes.put("id", orgUser.getId());
      attributes.put("type", "user");
      item.put("attributes", attributes);

      sub_items.add(item);
    }
    return sub_items;
  }

  public String getPurviewOrgGridData(String purviewid)
  {
    StringBuffer jsonHtml = new StringBuffer();
    Map item = new HashMap();
    item.put("total", Integer.valueOf(28));
    SysPurviewOrgDAO dao = new SysPurviewOrgDAO();
    item.put("rows", dao.getPurviewOrgList(purviewid));
    JSONArray json = JSONArray.fromObject(item);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public boolean setPurviewOrg(String purviewid, String[] nodelist)
  {
    boolean flag = false;
    SysPurviewOrgDAO dao = new SysPurviewOrgDAO();
    flag = dao.setPurViewRoleList(purviewid, nodelist);
    return flag;
  }
  public void setSysPurGroupDAO(SysPurGroupDAO sysPurGroupDAO) {
    this.sysPurGroupDAO = sysPurGroupDAO;
  }

  public SysPurGroupDAO getSysPurGroupDAO() {
    return this.sysPurGroupDAO;
  }
  public void setSysNavOperationDAO(SysNavOperationDAO sysNavOperationDAO) {
    this.sysNavOperationDAO = sysNavOperationDAO;
  }

  public void setSysNavSystemDAO(SysNavSystemDAO sysNavSystemDAO)
  {
    this.sysNavSystemDAO = sysNavSystemDAO;
  }

  public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO)
  {
    this.orgRoleDAO = orgRoleDAO;
  }

  public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
    this.orgUserDAO = orgUserDAO;
  }

  public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
    this.orgDepartmentDAO = orgDepartmentDAO;
  }

  public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
    this.orgCompanyDAO = orgCompanyDAO;
  }

  public void setSysNavNodeDAO(SysNavNodeDAO sysNavNodeDAO) {
    this.sysNavNodeDAO = sysNavNodeDAO;
  }
  public SysPurviewSchemaDAO getSysPurviewSchemaDAO() {
    return this.sysPurviewSchemaDAO;
  }
  public void setSysPurviewSchemaDAO(SysPurviewSchemaDAO sysPurviewSchemaDAO) {
    this.sysPurviewSchemaDAO = sysPurviewSchemaDAO;
  }
  public SysServiceManagerDAO getSysServiceManagerDAO() {
    return this.sysServiceManagerDAO;
  }
  public void setSysServiceManagerDAO(SysServiceManagerDAO sysServiceManagerDAO) {
    this.sysServiceManagerDAO = sysServiceManagerDAO;
  }
  public void setSysPurviewDemDAO(SysPurviewDemDAO sysPurviewDemDAO) {
    this.sysPurviewDemDAO = sysPurviewDemDAO;
  }
  public void setSysPurviewOrgDAO(SysPurviewOrgDAO sysPurviewOrgDAO) {
    this.sysPurviewOrgDAO = sysPurviewOrgDAO;
  }
  public void setSysPurviewProcessDAO(SysPurviewProcessDAO sysPurviewProcessDAO) {
    this.sysPurviewProcessDAO = sysPurviewProcessDAO;
  }
  public void setSysPurviewReportDAO(SysPurviewReportDAO sysPurviewReportDAO) {
    this.sysPurviewReportDAO = sysPurviewReportDAO;
  }
  public void setSysPurviewRoleDAO(SysPurviewRoleDAO sysPurviewRoleDAO) {
    this.sysPurviewRoleDAO = sysPurviewRoleDAO;
  }
}