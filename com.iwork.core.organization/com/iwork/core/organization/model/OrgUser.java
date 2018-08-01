package com.iwork.core.organization.model;

import java.util.Date;
/**
 * Orguser entity. @author MyEclipse Persistence Tools
 */

public class OrgUser  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String userid;
     private String username;
     private String password;
     private Long departmentid;
     private String departmentname;
     private Long orgroleid;
     private Long logincounter;
     private String bossid;
     private String costcenterid;
     private String costcentername;
     private Long ismanager;
     private String postsid;
     private String postsname;
     private Long isroving;
     private Long issinglelogin;
     private String officetel;
     private String officefax;
     private String hometel;
     private String mobile;
     private String email;
     private String jjlinkman;
     private String jjlinktel;
     private String userno;
     private Long orderindex;
     private String workStatus;
     private String portalmodel;
     private String extend1;
     private String extend2;
     private String extend3;
     private String extend4;
     private String extend5;
     private String extend6;
     private String extend7;
     private String extend8;
     private String extend9;
     private String extend10;
     private Long usertype;
     private Date startdate;
     private Date enddate;
     private Long userstate;
     private Long companyid;
     private String companyname;
     private String menulayouttype;
     private String postsresponsibility;
     private String qqmsn;
     private String selfdesc;
     private String weixinCode;
     private Long priority;
     private Long mailSize;
     
    // Constructors

    /** default constructor */
    public OrgUser() {
    }

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDepartmentid() {
        return this.departmentid;
    }
    
    public void setDepartmentid(Long departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartmentname() {
        return this.departmentname;
    }
    
    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public Long getOrgroleid() {
        return this.orgroleid;
    }
    
    public void setOrgroleid(Long orgroleid) {
        this.orgroleid = orgroleid;
    }

    public Long getLogincounter() {
        return this.logincounter;
    }
    
    public void setLogincounter(Long logincounter) {
        this.logincounter = logincounter;
    }

    public String getBossid() {
        return this.bossid;
    }
    
    public void setBossid(String bossid) {
        this.bossid = bossid;
    }

    public String getCostcenterid() {
        return this.costcenterid;
    }
    
    public void setCostcenterid(String costcenterid) {
        this.costcenterid = costcenterid;
    }

    public String getCostcentername() {
        return this.costcentername;
    }
    
    public void setCostcentername(String costcentername) {
        this.costcentername = costcentername;
    }

    public Long getIsmanager() {
        return this.ismanager;
    }
    
    public void setIsmanager(Long ismanager) {
        this.ismanager = ismanager;
    }

    public String getPostsid() {
        return this.postsid;
    }
    
    public void setPostsid(String postsid) {
        this.postsid = postsid;
    }

    public String getPostsname() {
        return this.postsname;
    }
    
    public void setPostsname(String postsname) {
        this.postsname = postsname;
    }

    public Long getIsroving() {
        return this.isroving;
    }
    
    public void setIsroving(Long isroving) {
        this.isroving = isroving;
    }

    public Long getIssinglelogin() {
        return this.issinglelogin;
    }
    
    public void setIssinglelogin(Long issinglelogin) {
        this.issinglelogin = issinglelogin;
    }

    public String getOfficetel() {
        return this.officetel;
    }
    
    public void setOfficetel(String officetel) {
        this.officetel = officetel;
    }

    public String getOfficefax() {
        return this.officefax;
    }
    
    public void setOfficefax(String officefax) {
        this.officefax = officefax;
    }

    public String getHometel() {
        return this.hometel;
    }
    
    public void setHometel(String hometel) {
        this.hometel = hometel;
    }

    public String getMobile() {
        return this.mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getJjlinkman() {
        return this.jjlinkman;
    }
    
    public void setJjlinkman(String jjlinkman) {
        this.jjlinkman = jjlinkman;
    }

    public String getJjlinktel() {
        return this.jjlinktel;
    }
    
    public void setJjlinktel(String jjlinktel) {
        this.jjlinktel = jjlinktel;
    }

    public String getUserno() {
        return this.userno;
    }
    
    public void setUserno(String userno) {
        this.userno = userno;
    }

    public Long getOrderindex() {
        return this.orderindex;
    }
    
    public void setOrderindex(Long orderindex) {
        this.orderindex = orderindex;
    }

    public String getWorkStatus() {
        return this.workStatus;
    }
    
    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getPortalmodel() {
        return this.portalmodel;
    }
    
    public void setPortalmodel(String portalmodel) {
        this.portalmodel = portalmodel;
    }

    public String getExtend1() {
        return this.extend1;
    }
    
    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return this.extend2;
    }
    
    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public String getExtend3() {
        return this.extend3;
    }
    
    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public String getExtend4() {
        return this.extend4;
    }
    
    public void setExtend4(String extend4) {
        this.extend4 = extend4;
    }

    public String getExtend5() {
        return this.extend5;
    }
    
    public void setExtend5(String extend5) {
        this.extend5 = extend5;
    }

    public String getExtend6() {
        return this.extend6;
    }
    
    public void setExtend6(String extend6) {
        this.extend6 = extend6;
    }

    public String getExtend7() {
        return this.extend7;
    }
    
    public void setExtend7(String extend7) {
        this.extend7 = extend7;
    }

    public String getExtend8() {
        return this.extend8;
    }
    
    public void setExtend8(String extend8) {
        this.extend8 = extend8;
    }

    public String getExtend9() {
        return this.extend9;
    }
    
    public void setExtend9(String extend9) {
        this.extend9 = extend9;
    }

    public String getExtend10() {
        return this.extend10;
    }
    
    public void setExtend10(String extend10) {
        this.extend10 = extend10;
    }

    public Long getUsertype() {
        return this.usertype;
    }
    
    public void setUsertype(Long usertype) {
        this.usertype = usertype;
    }

    public Date getStartdate() {
        return this.startdate;
    }
    
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }
    public Date getEnddate() {
        return this.enddate;
    }
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
    public Long getUserstate() {
        return this.userstate;
    }
    public void setUserstate(Long userstate) {
        this.userstate = userstate;
    }
    public Long getCompanyid() {
        return this.companyid;
    }
    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }
    public String getCompanyname() {
        return this.companyname;
    }
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
    public String getMenulayouttype() {
        return this.menulayouttype;
    }
    public void setMenulayouttype(String menulayouttype) {
        this.menulayouttype = menulayouttype;
    }
	public String getPostsresponsibility() {
		return postsresponsibility;
	}
	public void setPostsresponsibility(String postsresponsibility) {
		this.postsresponsibility = postsresponsibility;
	}
	public String getQqmsn() {
		return qqmsn;
	}
	public void setQqmsn(String qqmsn) {
		this.qqmsn = qqmsn;
	}
	public String getSelfdesc() {
		return selfdesc;
	}
	public void setSelfdesc(String selfdesc) {
		this.selfdesc = selfdesc;
	}
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getWeixinCode() {
		return weixinCode;
	}
	public void setWeixinCode(String weixinCode) {
		this.weixinCode = weixinCode;
	}

	public Long getMailSize() {
		return mailSize;
	}

	public void setMailSize(Long mailSize) {
		this.mailSize = mailSize;
	}

}
