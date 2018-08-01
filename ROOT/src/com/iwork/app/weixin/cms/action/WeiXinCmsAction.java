package com.iwork.app.weixin.cms.action;

import com.iwork.app.weixin.cms.service.WeiXinCmsService;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsRelation;
import com.iwork.plugs.cms.util.CmsUtil;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;

public class WeiXinCmsAction
  extends ActionSupport
{
  private Long channelid;
  private String html;
  private String tab;
  private Long portletid;
  private Long infoid;
  private WeiXinCmsService weiXinCmsService;
  private String cmsdate;
  private String prepicture;
  private String precontent;
  private String content;
  private String archives;
  private IworkCmsContent model;
  
  public String index()
  {
    if (getChannelid() == null)
    {
      IworkCmsRelation model = this.weiXinCmsService.getDefaultChannel();
      this.channelid = model.getChannelid();
      this.portletid = model.getPortletid();
    }
    this.tab = this.weiXinCmsService.showCmsTab(this.channelid, this.portletid);
    this.html = this.weiXinCmsService.showCmsList(this.channelid);
    return "success";
  }
  
  public String showPage()
  {
    this.model = this.weiXinCmsService.cmsOpen(this.infoid);
    if (this.model != null)
    {
      String content = CmsUtil.getInstance().getCmsContent(this.infoid);
      this.model.setContent(content);
    }
    if (this.model.getReleasedate() != null) {
      setCmsdate(CmsUtil.timeFormat(this.model.getReleasedate()));
    }
    if ((this.model.getPrepicture() != null) && (this.model.getPrepicture() != "")) {
      setPrepicture("<div class=\"subpic\">" + this.model.getPrepicture() + 
        "</div>");
    }
    if ((this.model.getPrecontent() != null) && (this.model.getPrecontent() != "")) {
      setPrecontent("<div class=\"guide\">" + this.model.getPrecontent() + 
        "</div>");
    }
    if ((this.model.getArchives() != null) && (this.model.getArchives() != ""))
    {
      StringBuffer filelist = new StringBuffer();
      List<FileUpload> list = FileUploadAPI.getInstance().getFileUploads(this.model.getArchives());
      if ((list != null) && (list.size() > 0))
      {
        filelist.append("<div class=archive style=\"border:1px solid #EBEBEB\">");
        filelist.append("附件:<br/>");
        for (FileUpload upload : list)
        {
          String suffix = FileUtil.getFileExt(upload.getFileSrcName());
          String img = WebUIUtil.getLinkIcon(suffix);
          

          filelist.append("<span><a href=").append("\"uploadifyDownload.action?fileUUID=").append(upload.getFileId()).append("\"").append(">").append(img).append(upload.getFileSrcName()).append("</a></span><br/>");
        }
      }
      filelist.append("</div>");
      setArchives(filelist.toString());
    }
    return "success";
  }
  
  public WeiXinCmsService getWeiXinCmsService()
  {
    return this.weiXinCmsService;
  }
  
  public void setWeiXinCmsService(WeiXinCmsService weiXinCmsService)
  {
    this.weiXinCmsService = weiXinCmsService;
  }
  
  public Long getChannelid()
  {
    return this.channelid;
  }
  
  public void setChannelid(Long channelid)
  {
    this.channelid = channelid;
  }
  
  public String getHtml()
  {
    return this.html;
  }
  
  public void setHtml(String html)
  {
    this.html = html;
  }
  
  public String getTab()
  {
    return this.tab;
  }
  
  public void setTab(String tab)
  {
    this.tab = tab;
  }
  
  public Long getPortletid()
  {
    return this.portletid;
  }
  
  public void setPortletid(Long portletid)
  {
    this.portletid = portletid;
  }
  
  public Long getInfoid()
  {
    return this.infoid;
  }
  
  public void setInfoid(Long infoid)
  {
    this.infoid = infoid;
  }
  
  public String getCmsdate()
  {
    return this.cmsdate;
  }
  
  public void setCmsdate(String cmsdate)
  {
    this.cmsdate = cmsdate;
  }
  
  public String getPrepicture()
  {
    return this.prepicture;
  }
  
  public void setPrepicture(String prepicture)
  {
    this.prepicture = prepicture;
  }
  
  public String getPrecontent()
  {
    return this.precontent;
  }
  
  public void setPrecontent(String precontent)
  {
    this.precontent = precontent;
  }
  
  public String getContent()
  {
    return this.content;
  }
  
  public void setContent(String content)
  {
    this.content = content;
  }
  
  public String getArchives()
  {
    return this.archives;
  }
  
  public void setArchives(String archives)
  {
    this.archives = archives;
  }
  
  public IworkCmsContent getModel()
  {
    return this.model;
  }
  
  public void setModel(IworkCmsContent model)
  {
    this.model = model;
  }
}
