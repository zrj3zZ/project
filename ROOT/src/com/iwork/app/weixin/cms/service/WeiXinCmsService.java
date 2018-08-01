package com.iwork.app.weixin.cms.service;

import com.iwork.commons.ClassReflect;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.cms.constans.CmsConstans;
import com.iwork.plugs.cms.dao.CmsChannelDAO;
import com.iwork.plugs.cms.dao.CmsInfoDAO;
import com.iwork.plugs.cms.dao.CmsPortletDAO;
import com.iwork.plugs.cms.dao.CmsRelationDAO;
import com.iwork.plugs.cms.framework.CmsPortletInterface;
import com.iwork.plugs.cms.framework.CmsPortletModel;
import com.iwork.plugs.cms.framework.DomainModel;
import com.iwork.plugs.cms.model.IworkCmsChannel;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.model.IworkCmsRelation;
import com.iwork.plugs.cms.util.CmsUtil;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeiXinCmsService
{
  private CmsChannelDAO cmsChannelDAO;
  private CmsRelationDAO cmsRelationDAO;
  private CmsInfoDAO cmsInfoDAO;
  private CmsPortletDAO cmsPortletDAO;
  private Long currentportletid;
  
  public IworkCmsContent cmsOpen(Long infoid)
  {
    IworkCmsContent model = this.cmsInfoDAO.getBoData(infoid);
    return model;
  }
  
  public IworkCmsRelation getDefaultChannel()
  {
    IworkCmsRelation model = new IworkCmsRelation();
    Map root = new HashMap();
    IworkCmsChannel channelModel = this.cmsChannelDAO.getBoData(new Long(0L));
    

    List<IworkCmsRelation> relationList = this.cmsRelationDAO.getCmsList(new Long(0L));
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    DomainModel domodel = new DomainModel();
    HashMap param = new HashMap();
    for (IworkCmsRelation relationModel : relationList)
    {
      CmsPortletModel infoModel = new CmsPortletModel();
      Long portletid = relationModel.getPortletid();
      if (this.currentportletid == null) {
        this.currentportletid = portletid;
      }
      IworkCmsPortlet cmsPortletModel = this.cmsPortletDAO.getBoData(portletid);
      if (cmsPortletModel != null)
      {
        Long a = cmsPortletModel.getPortlettype();
        if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE1))
        {
          infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS1);
        }
        else
        {
          if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE2)) {
            continue;
          }
          if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE3)) {
            continue;
          }
        }
        model = relationModel;
        break;
      }
    }
    if (this.currentportletid != null) {
      this.currentportletid = this.currentportletid;
    }
    return model;
  }
  
  public String showCmsTab(Long channelid, Long currentportletid)
  {
    StringBuffer html = new StringBuffer();
    Map root = new HashMap();
    IworkCmsChannel channelModel = this.cmsChannelDAO.getBoData(channelid);
    if (channelModel == null) {
      return "";
    }
    if (!CmsUtil.getContentSecurityList(channelModel.getBrowse())) {
      return "";
    }
    if (!CmsUtil.getEffect(channelModel.getBegindate(), channelModel.getEnddate())) {
      return "";
    }
    if (channelModel.getStatus().longValue() == 1L) {
      return "";
    }
    List<IworkCmsRelation> relationList = this.cmsRelationDAO.getCmsList(channelid);
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    DomainModel domodel = new DomainModel();
    HashMap param = new HashMap();
    for (IworkCmsRelation relationModel : relationList)
    {
      CmsPortletModel infoModel = new CmsPortletModel();
      Long portletid = relationModel.getPortletid();
      if (currentportletid == null) {
        currentportletid = portletid;
      }
      IworkCmsPortlet cmsPortletModel = this.cmsPortletDAO.getBoData(portletid);
      if (cmsPortletModel != null)
      {
        Long a = cmsPortletModel.getPortlettype();
        if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE1))
        {
          infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS1);
        }
        else
        {
          if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE2)) {
            continue;
          }
          if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE3)) {
            continue;
          }
        }
        if ((currentportletid != null) && (currentportletid.equals(portletid))) {
          html.append("<li><a href=\"javascript:checkTab(").append(channelid).append(",").append(portletid).append(")\" data-icon=\"star\" class=\"ui-btn-active\">").append(cmsPortletModel.getPortletname()).append("</a></li>");
        } else {
          html.append("<li><a href=\"javascript:checkTab(").append(channelid).append(",").append(portletid).append(")\" data-icon=\"star\">").append(cmsPortletModel.getPortletname()).append("</a></li>");
        }
      }
    }
    if (currentportletid != null) {
      this.currentportletid = currentportletid;
    }
    return html.toString();
  }
  
  public String showCmsList(Long channelid)
  {
    Map root = new HashMap();
    IworkCmsChannel channelModel = this.cmsChannelDAO.getBoData(channelid);
    if (channelModel == null) {
      return "<div style='text-align:center;color:red;'>此频道不存在!</div>";
    }
    if (!CmsUtil.getContentSecurityList(channelModel.getBrowse())) {
      return "<div style='text-align:center;color:red;'>您无权查看此频道!</div>";
    }
    if (!CmsUtil.getEffect(channelModel.getBegindate(), channelModel.getEnddate())) {
      return "<div style='text-align:center;color:red;'>此频道已失效!</div>";
    }
    if (channelModel.getStatus().longValue() == 1L) {
      return "<div style='text-align:center;color:red;'>此频道已关闭!</div>";
    }
    List<IworkCmsRelation> relationList = this.cmsRelationDAO.getCmsList(channelid);
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    DomainModel domodel = new DomainModel();
    HashMap param = new HashMap();
    StringBuffer html = new StringBuffer();
    for (IworkCmsRelation relationModel : relationList)
    {
      CmsPortletModel infoModel = new CmsPortletModel();
      Long portletid = relationModel.getPortletid();
      IworkCmsPortlet cmsPortletModel = this.cmsPortletDAO.getBoData(portletid);
      if ((cmsPortletModel != null) && 
        (this.currentportletid != null) && (cmsPortletModel.getId().equals(this.currentportletid)))
      {
        Long a = cmsPortletModel.getPortlettype();
        if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE1))
        {
          infoModel.setInfoClass(CmsConstans.CMS_PORTLET_INPLAMENTS1);
        }
        else
        {
          if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE2)) {
            continue;
          }
          if (cmsPortletModel.getPortlettype().equals(CmsConstans.CMS_CHANNEL_TYPE3)) {
            continue;
          }
        }
        Constructor cons = null;
        Class[] parameterTypes = new Class[0];
        try
        {
          cons = ClassReflect.getConstructor(infoModel.getInfoClass(), parameterTypes);
          if (cons != null)
          {
            Object[] params = new Object[0];
            infoModel.setKsPortalInfoObject((CmsPortletInterface)cons.newInstance(params));
          }
        }
        catch (NullPointerException pe)
        {
          System.err.println("IWORK ClassLoader>>无info实现类!");
          pe.printStackTrace(System.err);
        }
        catch (ClassNotFoundException ce)
        {
          System.err.println("IWORK ClassLoader>>info实现类[" + infoModel.getInfoClass() + "]没有找到!");
          ce.printStackTrace(System.err);
        }
        catch (NoSuchMethodException ne)
        {
          System.err.println("IWORK ClassLoader>>info实现类[" + infoModel.getInfoClass() + "]构造方法不匹配!");
          ne.printStackTrace(System.err);
        }
        catch (InstantiationException ie)
        {
          System.err.println("IWORK ClassLoader>>info实现类[" + infoModel.getInfoClass() + "]构造实例时出错!");
          ie.printStackTrace(System.err);
        }
        catch (IllegalAccessException le)
        {
          System.err.println("IWORK ClassLoader>>info实现类[" + infoModel.getInfoClass() + "]抛出IllegalAccessException!");
        }
        catch (InvocationTargetException invoke)
        {
          System.err.println("IWORK ClassLoader>>info实现类[" + infoModel.getInfoClass() + "]抛出InvocationTargetException!");
          invoke.printStackTrace(System.err);
        }
        catch (Exception localException) {}
        CmsPortletInterface portlet = infoModel.getKsPortalInfoObject();
        if (portlet != null) {
          try
          {
            String content = portlet.portletWeiXinPage(uc, domodel, cmsPortletModel, param);
            if (content != null) {
              html.append(content);
            }
          }
          catch (Exception localException1) {}finally
          {
            cons = null;
            parameterTypes = (Class[])null;
            portlet = null;
          }
        }
      }
    }
    return html.toString();
  }
  
  public void setCmsChannelDAO(CmsChannelDAO cmsChannelDAO)
  {
    this.cmsChannelDAO = cmsChannelDAO;
  }
  
  public void setCmsRelationDAO(CmsRelationDAO cmsRelationDAO)
  {
    this.cmsRelationDAO = cmsRelationDAO;
  }
  
  public void setCmsInfoDAO(CmsInfoDAO cmsInfoDAO)
  {
    this.cmsInfoDAO = cmsInfoDAO;
  }
  
  public void setCmsPortletDAO(CmsPortletDAO cmsPortletDAO)
  {
    this.cmsPortletDAO = cmsPortletDAO;
  }
}
