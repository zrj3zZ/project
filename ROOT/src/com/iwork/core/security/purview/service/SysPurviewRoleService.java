package com.iwork.core.security.purview.service;

import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgRoleGroup;
import com.iwork.core.security.purview.dao.SysPurGroupDAO;
import com.iwork.core.security.purview.dao.SysPurviewRoleDAO;
import com.iwork.core.security.purview.model.SysPurGroup;
import com.iwork.core.security.purview.model.SysPurviewRoleModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;

public class SysPurviewRoleService
{
  private SysPurGroupDAO sysPurGroupDAO;
  private SysPurviewRoleDAO sysPurviewRoleDAO;
  private OrgRoleDAO orgRoleDAO;

  public boolean setPurview(Long purviewId, String roleIds)
  {
    boolean flag = false;
    this.sysPurviewRoleDAO = new SysPurviewRoleDAO();
    String[] list = roleIds.split(",");
    if ((list != null) && (list.length > 0))
    {
      List<SysPurviewRoleModel> srclist = this.sysPurviewRoleDAO.getPurViewRoleList(purviewId.toString());
      boolean isAdd;
      Object localObject;
      SysPurviewRoleModel model;
      for (String roleId : list) {
        if (!"".equals(roleId)) {
          isAdd = true;
          for (localObject = srclist.iterator(); ((Iterator)localObject).hasNext(); ) { SysPurviewRoleModel spp = (SysPurviewRoleModel)((Iterator)localObject).next();
            if (roleId.equals(spp.getRoleid())) {
              isAdd = false;
            } else {
              isAdd = true;
              break;
            }
          }
          if (isAdd) {
            model = new SysPurviewRoleModel();
            model = new SysPurviewRoleModel();
            model.setPurviewid(purviewId);
            model.setRoleid(Long.valueOf(roleId));
            this.sysPurviewRoleDAO.addRoleModel(model.getPurviewid(), model.getRoleid());
          }
        }
      }

      /*for (SysPurviewRoleModel spr : srclist) {
        boolean isDel = true;
        model = (localObject = list).length; for (isAdd = false; isAdd < model; isAdd++) { String roleId = localObject[isAdd];
          if ((!"".equals(roleId)) && 
            (roleId.equals(spr.getRoleid()))) {
            isDel = false;
            break;
          }
        }

        if (isDel) {
          this.sysPurviewRoleDAO.deleteRoleModel(spr);
        }
      }*/
      flag = true;
    }
    return flag;
  }

  public boolean setPurviewList(String purviewList, Long roleId)
  {
    boolean flag = false;
    this.sysPurviewRoleDAO = new SysPurviewRoleDAO();
    String[] list = purviewList.split(",");
    if ((list != null) && (list.length > 0))
    {
      List srclist = this.sysPurviewRoleDAO.getPurViewRoleList(roleId);
      boolean isAdd;
      Object localObject;
      SysPurviewRoleModel spp;
      for (String purviewId : list) {
        if (!"".equals(purviewId)) {
          isAdd = true;
          for (localObject = srclist.iterator(); ((Iterator)localObject).hasNext(); ) { spp = (SysPurviewRoleModel)((Iterator)localObject).next();
            if (purviewId.equals(spp.getPurviewid())) {
              isAdd = false;
            } else {
              isAdd = true;
              break;
            }
          }
          if (isAdd) {
            this.sysPurviewRoleDAO.addRoleModel(Long.valueOf(purviewId), roleId);
          }
        }
      }

      /*for (SysPurviewRoleModel spr : srclist) {
        boolean isDel = true;
        spp = (localObject = list).length; 
        for (isAdd = false; isAdd < spp; isAdd++) {
        	String purviewId = localObject[isAdd];
          if ((!"".equals(purviewId)) && 
            (purviewId.equals(spr.getPurviewid()))) {
            isDel = false;
            break;
          }
        }

        if (isDel) {
          this.sysPurviewRoleDAO.deleteRoleModel(spr);
        }
      }*/
    }
    return flag;
  }

  public String showRoleTreeJson()
  {
    StringBuffer html = new StringBuffer();
    List rows = new ArrayList();

    List<OrgRoleGroup> grouplist = this.orgRoleDAO.getRoleGroupList();
    for (OrgRoleGroup model : grouplist) {
      HashMap hash = new HashMap();
      hash.put("id", model.getId());
      hash.put("type", "group");
      hash.put("open", Boolean.valueOf(true));
      hash.put("name", model.getGroupName());
      hash.put("children", getRoleJson(model.getId()));
      rows.add(hash);
    }
    JSONArray json = JSONArray.fromObject(rows);
    html.append(json);
    return html.toString();
  }

  private List getRoleJson(Long groupId)
  {
    StringBuffer html = new StringBuffer();
    List rows = new ArrayList();
    List<OrgRole> list = this.orgRoleDAO.getRoleList(groupId);
    for (OrgRole model : list) {
      HashMap hash = new HashMap();
      hash.put("id", model.getId());
      hash.put("name", model.getRolename());
      hash.put("type", "roleid");
      hash.put("icon", "iwork_img/user_business_boss.png");
      rows.add(hash);
    }

    return rows;
  }

  public String showGroupTreeJson(Long roleId)
  {
    StringBuffer html = new StringBuffer();
    List rows = new ArrayList();

    List purviewList = null;
    this.sysPurviewRoleDAO = new SysPurviewRoleDAO();
    if (roleId != null) {
      purviewList = this.sysPurviewRoleDAO.getPurViewRoleList(roleId);
    }
    List<String> list = this.sysPurGroupDAO.getCategoryList();
    int count = 0;
    for (String category : list) {
      HashMap hash = new HashMap();
      count++;
      if (category != null) {
        hash.put("id", count + "_category");
        hash.put("type", "category");
        hash.put("open", Boolean.valueOf(true));
        hash.put("name", category);
        hash.put("children", getCategoryJson(roleId, category, purviewList));
        rows.add(hash);
      }
    }
    JSONArray json = JSONArray.fromObject(rows);
    html.append(json);
    return html.toString();
  }

  private List getCategoryJson(Long roleId, String categoryname, List<SysPurviewRoleModel> purviewList)
  {
    StringBuffer html = new StringBuffer();
    List rows = new ArrayList();
    List<SysPurGroup> list = this.sysPurGroupDAO.getSysPurGroupByCategory(categoryname);
    for (SysPurGroup model : list) {
      HashMap hash = new HashMap();
      hash.put("id", model.getId());
      hash.put("name", model.getGroupname());
      if (isCheckPurviewUser(purviewList, model.getId())) {
        hash.put("checked", Boolean.valueOf(true));
      }
      hash.put("type", "purview");
      hash.put("icon", "iwork_img/shield.png");
      rows.add(hash);
    }
    return rows;
  }

  private boolean isCheckPurviewUser(List<SysPurviewRoleModel> purviewList, Long purviewId)
  {
    boolean flag = false;
    for (SysPurviewRoleModel spbm : purviewList) {
      if (spbm.getPurviewid().equals(purviewId)) {
        flag = true;
        break;
      }
    }
    return flag;
  }

  public OrgRoleDAO getOrgRoleDAO() {
    return this.orgRoleDAO;
  }
  public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO) {
    this.orgRoleDAO = orgRoleDAO;
  }

  public SysPurGroupDAO getSysPurGroupDAO() {
    return this.sysPurGroupDAO;
  }

  public void setSysPurGroupDAO(SysPurGroupDAO sysPurGroupDAO) {
    this.sysPurGroupDAO = sysPurGroupDAO;
  }
}