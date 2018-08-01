package com.iwork.km.document.action;

import com.iwork.core.organization.action.OrgCompanyAction;
import com.iwork.core.util.ResponseUtil;
import com.iwork.km.document.service.KMPurViewService;
import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMPurViewAction extends ActionSupport
{
  private KMPurViewService kmPurViewService;
  private static final Logger logger = LoggerFactory.getLogger(OrgCompanyAction.class);
  private String id;
  private Long pid;
  private Long purviewType;
  private String purviewGroup;
  private String purviewIdList;
  private String purviewHTML;
  private String purviewBtn;
  private Long type;

  public String purviewList()
  {
    if ((this.purviewGroup != null) && (this.id != null)) {
      this.purviewBtn = this.kmPurViewService.loadPurviewBtn(Long.valueOf(Long.parseLong(this.id)), this.purviewGroup);
      this.purviewHTML = this.kmPurViewService.getPurviewList(this.purviewGroup, Long.valueOf(Long.parseLong(this.id)));
    }
    return "success";
  }

  public String purviewTree()
  {
    return "success";
  }

  public void showOrgPurviewJson()
  {
    String json = "";
    if ((this.pid != null) && (this.purviewType != null) && (this.id == null))
      json = this.kmPurViewService.getCompanyNodeJson(this.purviewGroup, this.purviewType, this.type, this.pid);
    else if ((this.pid != null) && (this.purviewType != null) && (this.id != null)) {
      json = this.kmPurViewService.getDeptAndUserJson(Long.valueOf(Long.parseLong(this.id)), this.purviewGroup, this.purviewType, this.type, this.pid);
    }
    ResponseUtil.write(json);
  }

  public void setPurview()
  {
    if ((this.purviewType != null) && (this.purviewGroup != null) && (this.purviewIdList != null) && (this.type != null) && (this.pid != null)) {
      this.kmPurViewService.addPurview(this.purviewGroup, this.purviewType, this.purviewIdList, this.type, this.pid);
    }
    ResponseUtil.write("success");
  }
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Long getPid() {
    return this.pid;
  }
  public void setPid(Long pid) {
    this.pid = pid;
  }
  public Long getPurviewType() {
    return this.purviewType;
  }
  public void setPurviewType(Long purviewType) {
    this.purviewType = purviewType;
  }
  public Long getType() {
    return this.type;
  }
  public void setType(Long type) {
    this.type = type;
  }

  public KMPurViewService getKmPurViewService() {
    return this.kmPurViewService;
  }

  public void setKmPurViewService(KMPurViewService kmPurViewService) {
    this.kmPurViewService = kmPurViewService;
  }
  public String getPurviewGroup() {
    return this.purviewGroup;
  }
  public void setPurviewGroup(String purviewGroup) {
    this.purviewGroup = purviewGroup;
  }

  public String getPurviewIdList() {
    return this.purviewIdList;
  }

  public void setPurviewIdList(String purviewIdList) {
    this.purviewIdList = purviewIdList;
  }

  public String getPurviewHTML() {
    return this.purviewHTML;
  }

  public void setPurviewHTML(String purviewHTML) {
    this.purviewHTML = purviewHTML;
  }

  public String getPurviewBtn() {
    return this.purviewBtn;
  }
  public void setPurviewBtn(String purviewBtn) {
    this.purviewBtn = purviewBtn;
  }
}