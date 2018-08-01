package com.iwork.plugs.cms.model;

import java.util.Date;


/**
 * IworkCmsChannel entity. @author MyEclipse Persistence Tools
 */

public class IworkCmsChannel  implements java.io.Serializable {
	public static String DATABASE_ENTITY = "IworkCmsChannel";

    // Fields    

     private Long id;
     private String channelname;
     private String groupname;
     private String manager;
     private String template;
     private Long verifytype;
     private String browse;
     private Date begindate;
     private Date enddate;
     private Long status;
     private String memo;


    // Constructors

    /** default constructor */
    public IworkCmsChannel() {
    }

    
    /** full constructor */
    public IworkCmsChannel(String channelname, String groupname, String manager, String template, Long verifytype, String browse, Date begindate, Date enddate, Long status, String memo) {
        this.channelname = channelname;
        this.groupname = groupname;
        this.manager = manager;
        this.template = template;
        this.verifytype = verifytype;
        this.browse = browse;
        this.begindate = begindate;
        this.enddate = enddate;
        this.status = status;
        this.memo = memo;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelname() {
        return this.channelname;
    }
    
    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public String getGroupname() {
        return this.groupname;
    }
    
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getManager() {
        return this.manager;
    }
    
    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getTemplate() {
        return this.template;
    }
    
    public void setTemplate(String template) {
        this.template = template;
    }

    public Long getVerifytype() {
        return this.verifytype;
    }
    
    public void setVerifytype(Long verifytype) {
        this.verifytype = verifytype;
    }

    public String getBrowse() {
        return this.browse;
    }
    
    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public Date getBegindate() {
        return this.begindate;
    }
    
    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getEnddate() {
        return this.enddate;
    }
    
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Long getStatus() {
        return this.status;
    }
    
    public void setStatus(Long status) {
        this.status = status;
    }

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
   








}
