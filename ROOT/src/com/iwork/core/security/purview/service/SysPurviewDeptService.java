// Decompiled by DJ v3.12.12.101 Copyright 2016 Atanas Neshkov  Date: 2017-11-6 16:40:57
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SysPurviewDeptService.java

package com.iwork.core.security.purview.service;

import com.iwork.core.organization.dao.*;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.security.purview.dao.*;
import com.iwork.core.security.purview.model.SysPurGroup;
import com.iwork.core.security.purview.model.SysPurviewOrg;
import java.util.*;
import net.sf.json.JSONArray;

public class SysPurviewDeptService
{

    public SysPurviewDeptService()
    {
    }

    public boolean setPurview(Long purviewId, String userList)
    {
        boolean flag = false;
        String list[] = userList.split(",");
        if(list != null && list.length > 0)
        {
            List srclist = sysPurviewOrgDAO.getPurviewOrgList(purviewId, "dept");
            String as[];
            int j = (as = list).length;
            for(int i = 0; i < j; i++)
            {
                String dept = as[i];
                if(!"".equals(dept))
                {
                    boolean isAdd = true;
                    Iterator iterator1 = srclist.iterator();
                    while(iterator1.hasNext()) 
                    {
                        SysPurviewOrg org = (SysPurviewOrg)iterator1.next();
                        if(dept.equals(org.getOrgid()))
                        {
                            isAdd = false;
                            continue;
                        }
                        isAdd = true;
                        break;
                    }
                    if(isAdd)
                    {
                        SysPurviewOrg model = new SysPurviewOrg();
                        model.setOrgid(dept);
                        model.setPtype("dept");
                        model.setPurviewid(purviewId);
                        sysPurviewOrgDAO.addPurviewOrg(model);
                    }
                }
            }

            for(Iterator iterator = srclist.iterator(); iterator.hasNext();)
            {
                SysPurviewOrg org = (SysPurviewOrg)iterator.next();
                boolean isDel = true;
                String as1[];
                int l = (as1 = list).length;
                for(int k = 0; k < l; k++)
                {
                    String dept = as1[k];
                    if("".equals(dept) || !dept.equals(org.getOrgid()))
                        continue;
                    isDel = false;
                    break;
                }

                if(isDel)
                    sysPurviewOrgDAO.deletePurviewOrg(org);
            }

            flag = true;
        }
        return flag;
    }

    public boolean setPurviewList(Long deptId, String purviewIds)
    {
        boolean flag = false;
        String list[] = purviewIds.split(",");
        if(list != null && list.length > 0)
        {
            List srclist = sysPurviewOrgDAO.getOrgPurviewList("dept", (new StringBuilder()).append(deptId).toString());
            String as[];
            int j = (as = list).length;
            for(int i = 0; i < j; i++)
            {
                String purviewId = as[i];
                if(!"".equals(purviewId))
                {
                    boolean isAdd = true;
                    Iterator iterator1 = srclist.iterator();
                    while(iterator1.hasNext()) 
                    {
                        SysPurviewOrg org = (SysPurviewOrg)iterator1.next();
                        if(purviewId.equals((new StringBuilder()).append(org.getPurviewid()).toString()))
                        {
                            isAdd = false;
                            continue;
                        }
                        isAdd = true;
                        break;
                    }
                    if(isAdd)
                    {
                        SysPurviewOrg model = new SysPurviewOrg();
                        model.setOrgid(deptId.toString());
                        model.setPtype("dept");
                        model.setPurviewid(Long.valueOf(Long.parseLong(purviewId)));
                        sysPurviewOrgDAO.addPurviewOrg(model);
                    }
                }
            }

            for(Iterator iterator = srclist.iterator(); iterator.hasNext();)
            {
                SysPurviewOrg org = (SysPurviewOrg)iterator.next();
                boolean isDel = true;
                String as1[];
                int l = (as1 = list).length;
                for(int k = 0; k < l; k++)
                {
                    String purviewId = as1[k];
                    if("".equals(purviewId) || !purviewId.equals((new StringBuilder()).append(org.getPurviewid()).toString()))
                        continue;
                    isDel = false;
                    break;
                }

                if(isDel)
                    sysPurviewOrgDAO.deletePurviewOrg(org);
            }

            flag = true;
        }
        return flag;
    }

    public String getDeptJson(Long deptId, Long purviewid)
    {
        if(userlist == null && purviewid != null)
            userlist = sysPurviewOrgDAO.getPurviewOrgList(purviewid, "dept");
        StringBuffer jsonHtml = new StringBuffer();
        List list = new ArrayList();
        List deptlist = orgDepartmentDAO.getSubDepartmentList(deptId);
        for(Iterator iterator = deptlist.iterator(); iterator.hasNext();)
        {
            OrgDepartment dept = (OrgDepartment)iterator.next();
            Map childrenItem = buildTreeDeptNode(dept);
            if(childrenItem != null)
                list.add(childrenItem);
        }

        JSONArray json = JSONArray.fromObject(list);
        jsonHtml.append(json);
        return jsonHtml.toString();
    }

    private boolean isCheckDeptList(Long departmentId)
    {
        boolean flag = false;
        if(userlist != null)
        {
            for(Iterator iterator = userlist.iterator(); iterator.hasNext();)
            {
                SysPurviewOrg model = (SysPurviewOrg)iterator.next();
                if(model.getOrgid() != null && departmentId != null && model.getOrgid().equals(departmentId.toString()))
                {
                    flag = true;
                    break;
                }
            }

        }
        return flag;
    }

    private Map buildTreeDeptNode(OrgDepartment dept)
    {
        Map childrenItem = new HashMap();
        childrenItem.put("id", dept.getId());
        childrenItem.put("name", dept.getDepartmentname());
        childrenItem.put("open", "true");
        childrenItem.put("isParent", Boolean.valueOf(true));
        if(isCheckDeptList(dept.getId()))
            childrenItem.put("checked", Boolean.valueOf(true));
        childrenItem.put("nodeType", "dept");
        childrenItem.put("nodeId", dept.getId());
        childrenItem.put("companyid", dept.getCompanyid());
        childrenItem.put("departmentdesc", dept.getDepartmentdesc());
        childrenItem.put("departmentno", dept.getDepartmentno());
        childrenItem.put("layer", dept.getLayer());
        childrenItem.put("orderindex", dept.getOrderindex());
        childrenItem.put("zoneno", dept.getZoneno());
        childrenItem.put("zonename", dept.getZonename());
        childrenItem.put("extend1", dept.getExtend1());
        childrenItem.put("extend2", dept.getExtend2());
        childrenItem.put("extend3", dept.getExtend3());
        childrenItem.put("extend4", dept.getExtend4());
        childrenItem.put("extend5", dept.getExtend5());
        return childrenItem;
    }

    public String showGroupTreeJson(Long deptId)
    {
        StringBuffer html = new StringBuffer();
        List rows = new ArrayList();
        List purviewList = null;
        if(deptId != null)
            purviewList = sysPurviewOrgDAO.getOrgPurviewList("dept", deptId.toString());
        List list = sysPurGroupDAO.getCategoryList();
        int count = 0;
        HashMap hash;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); rows.add(hash))
        {
            String category = (String)iterator.next();
            hash = new HashMap();
            count++;
            hash.put("id", (new StringBuilder(String.valueOf(count))).append("_category").toString());
            hash.put("type", "category");
            hash.put("open", Boolean.valueOf(true));
            hash.put("name", category);
            hash.put("children", getCategoryJson(category, purviewList));
        }

        JSONArray json = JSONArray.fromObject(rows);
        html.append(json);
        return html.toString();
    }

    private List getCategoryJson(String categoryname, List purviewList)
    {
        StringBuffer html = new StringBuffer();
        List rows = new ArrayList();
        List list = sysPurGroupDAO.getSysPurGroupByCategory(categoryname);
        HashMap hash;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); rows.add(hash))
        {
            SysPurGroup model = (SysPurGroup)iterator.next();
            hash = new HashMap();
            hash.put("id", model.getId());
            hash.put("name", model.getGroupname());
            if(isCheckPurview(purviewList, model.getId()))
                hash.put("checked", Boolean.valueOf(true));
            hash.put("type", "purview");
            hash.put("icon", "iwork_img/shield.png");
        }

      
        return rows;
    }

    private boolean isCheckPurview(List purviewList, Long purviewId)
    {
        boolean flag = false;
        for(Iterator iterator = purviewList.iterator(); iterator.hasNext();)
        {
            SysPurviewOrg spbm = (SysPurviewOrg)iterator.next();
            if(spbm.getPurviewid().equals(purviewId))
            {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public String getCompanyNodeJson(String id, String nodeType, boolean flag, Long purviewid)
    {
        StringBuffer jsonHtml = new StringBuffer();
        List root = new ArrayList();
        List items = new ArrayList();
        List orgCompanyList = new ArrayList();
        if(userlist == null && purviewid != null)
            userlist = sysPurviewOrgDAO.getPurviewOrgList(purviewid, "dept");
        if(nodeType != null && nodeType.equals("com"))
        {
            orgCompanyList = orgCompanyDAO.getCompanyList(id);
            if(orgCompanyList != null && orgCompanyList.size() > 0)
            {
                Map item;
                for(Iterator iterator = orgCompanyList.iterator(); iterator.hasNext(); items.add(item))
                {
                    OrgCompany model = (OrgCompany)iterator.next();
                    item = new HashMap();
                    item = buildTreeCompanyNode(model);
                    List subCompanyList = orgCompanyDAO.getCompanyList(model.getId());
                    if(subCompanyList != null && subCompanyList.size() > 0)
                        item.put("children", getCompanyNodeJson(model.getId(), nodeType, false, purviewid));
                    List departmentList = orgDepartmentDAO.getTopDepartmentList(model.getId());
                    if(departmentList != null && departmentList.size() > 0)
                        item.put("isParent", Boolean.valueOf(true));
                }

            }
        }
        List departmentList = null;
        if(nodeType != null && nodeType.equals("com"))
            departmentList = orgDepartmentDAO.getTopDepartmentList(id);
        else
            departmentList = orgDepartmentDAO.getSubDepartmentList(Long.valueOf(Long.parseLong(id)));
        if(departmentList != null)
        {
            for(int j = 0; j < departmentList.size(); j++)
            {
                Map subItem = new HashMap();
                OrgDepartment dept = (OrgDepartment)departmentList.get(j);
                subItem = buildTreeDeptNode(dept);
                items.add(subItem);
            }

        }
        JSONArray json = null;
        if(flag)
        {
            Map item = new HashMap();
            OrgCompany orgCompany = orgCompanyDAO.getBoData(id);
            if(orgCompany != null)
            {
                item = buildTreeCompanyNode(orgCompany);
                item.put("children", items);
                root.add(item);
                json = JSONArray.fromObject(root);
            }
        } else
        {
            json = JSONArray.fromObject(items);
        }
        jsonHtml.append(json);
        return jsonHtml.toString();
    }

    public Map buildTreeCompanyNode(OrgCompany model)
    {
        Map item = new HashMap();
        item.put("id", model.getId());
        item.put("name", model.getCompanyname());
        item.put("open", "true");
        item.put("nodeType", "com");
        item.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
        item.put("iconClose", "iwork_img/ztree/diy/1_close.png");
        item.put("companyid", model.getId());
        item.put("companytype", model.getCompanytype());
        item.put("companyno", model.getCompanyno());
        item.put("companydesc", model.getCompanydesc());
        item.put("orderindex", model.getOrderindex());
        item.put("lookandfeel", model.getLookandfeel());
        item.put("orgadress", model.getOrgadress());
        item.put("post", model.getPost());
        item.put("email", model.getEmail());
        item.put("tel", model.getTel());
        item.put("zoneno", model.getZoneno());
        item.put("zonename", model.getZonename());
        item.put("extend1", model.getExtend1());
        item.put("extend2", model.getExtend2());
        item.put("extend3", model.getExtend3());
        item.put("extend4", model.getExtend4());
        item.put("extend5", model.getExtend5());
        return item;
    }

    public String getDeptJson(Long companyid, Long deptId, Long purviewid)
    {
        if(userlist == null && purviewid != null)
            userlist = sysPurviewOrgDAO.getPurviewOrgList(purviewid, "dept");
        StringBuffer jsonHtml = new StringBuffer();
        List list = new ArrayList();
        List deptlist = orgDepartmentDAO.getSubDepartmentList(companyid, deptId);
        for(Iterator iterator = deptlist.iterator(); iterator.hasNext();)
        {
            OrgDepartment dept = (OrgDepartment)iterator.next();
            Map childrenItem = buildTreeDeptNode(dept);
            if(childrenItem != null)
                list.add(childrenItem);
        }

        JSONArray json = JSONArray.fromObject(list);
        jsonHtml.append(json);
        return jsonHtml.toString();
    }

    public SysPurGroupDAO getSysPurGroupDAO()
    {
        return sysPurGroupDAO;
    }

    public void setSysPurGroupDAO(SysPurGroupDAO sysPurGroupDAO)
    {
        this.sysPurGroupDAO = sysPurGroupDAO;
    }

    public SysPurviewSchemaDAO getSysPurviewSchemaDAO()
    {
        return sysPurviewSchemaDAO;
    }

    public void setSysPurviewSchemaDAO(SysPurviewSchemaDAO sysPurviewSchemaDAO)
    {
        this.sysPurviewSchemaDAO = sysPurviewSchemaDAO;
    }

    public OrgRoleDAO getOrgRoleDAO()
    {
        return orgRoleDAO;
    }

    public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO)
    {
        this.orgRoleDAO = orgRoleDAO;
    }

    public OrgUserDAO getOrgUserDAO()
    {
        return orgUserDAO;
    }

    public void setOrgUserDAO(OrgUserDAO orgUserDAO)
    {
        this.orgUserDAO = orgUserDAO;
    }

    public OrgDepartmentDAO getOrgDepartmentDAO()
    {
        return orgDepartmentDAO;
    }

    public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO)
    {
        this.orgDepartmentDAO = orgDepartmentDAO;
    }

    public OrgCompanyDAO getOrgCompanyDAO()
    {
        return orgCompanyDAO;
    }

    public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO)
    {
        this.orgCompanyDAO = orgCompanyDAO;
    }

    public OrgUserMapDAO getOrgUserMapDAO()
    {
        return orgUserMapDAO;
    }

    public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO)
    {
        this.orgUserMapDAO = orgUserMapDAO;
    }

    public void setSysPurviewOrgDAO(SysPurviewOrgDAO sysPurviewOrgDAO)
    {
        this.sysPurviewOrgDAO = sysPurviewOrgDAO;
    }

    private SysPurGroupDAO sysPurGroupDAO;
    private SysPurviewOrgDAO sysPurviewOrgDAO;
    private SysPurviewSchemaDAO sysPurviewSchemaDAO;
    private OrgRoleDAO orgRoleDAO;
    private OrgUserDAO orgUserDAO;
    private OrgUserMapDAO orgUserMapDAO;
    private OrgDepartmentDAO orgDepartmentDAO;
    private OrgCompanyDAO orgCompanyDAO;
    private List userlist;
}
