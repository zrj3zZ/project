package com.iwork.app.mobile.accessControl.model;

import java.util.Date;


/**
 * SysMobileVisitset entity. @author MyEclipse Persistence Tools
 */

public class SysMobileVisitset  implements java.io.Serializable {


    // Fields    

     private long id;
     private String userid;
     private long isVisit;
     private String visitType;
     private long isBind;
     private String deviceIds;
     private String opeUser;
     private Date opeTime;


    // Constructors

    /** default constructor */
    public SysMobileVisitset() {
    }

	/** minimal constructor */
    public SysMobileVisitset(long id, String userid) {
        this.id = id;
        this.userid = userid;
    }
    
    /** full constructor */
    public SysMobileVisitset(long id, String userid, long isVisit, String visitType, long isBind, String deviceIds, String opeUser, Date opeTime) {
        this.id = id;
        this.userid = userid;
        this.isVisit = isVisit;
        this.visitType = visitType;
        this.isBind = isBind;
        this.deviceIds = deviceIds;
        this.opeUser = opeUser;
        this.opeTime = opeTime;
    }

   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getIsVisit() {
        return this.isVisit;
    }
    
    public void setIsVisit(long isVisit) {
        this.isVisit = isVisit;
    }

    public String getVisitType() {
        return this.visitType;
    }
    
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public long getIsBind() {
        return this.isBind;
    }
    
    public void setIsBind(long isBind) {
        this.isBind = isBind;
    }

    public String getDeviceIds() {
        return this.deviceIds;
    }
    
    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getOpeUser() {
        return this.opeUser;
    }
    
    public void setOpeUser(String opeUser) {
        this.opeUser = opeUser;
    }

    public Date getOpeTime() {
        return this.opeTime;
    }
    
    public void setOpeTime(Date opeTime) {
        this.opeTime = opeTime;
    }
   








}