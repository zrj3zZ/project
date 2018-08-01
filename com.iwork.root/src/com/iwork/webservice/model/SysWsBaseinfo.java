package com.iwork.webservice.model;

// default package



/**
 * SysWsBaseinfo entity. @author MyEclipse Persistence Tools
 */

public class SysWsBaseinfo  implements java.io.Serializable {


    // Fields    

     private int id;
     private String title;
     private String wsType;
     private String wsLevel;
     private String contentType;
     private String name;
     private String url;
     private String description;
     private String checkType;
     private String permitIp;
     private String forbidIp;
     private String username;
     private String password;
     private int status;
     private String uuid;
     private int groupId;
     private int isCache;
     private int cacheTime;


    // Constructors

    /** default constructor */
    public SysWsBaseinfo() {
    }

    
    /** full constructor */
    public SysWsBaseinfo(String wsType, String name, String url, String description, String checkType, String permitIp, String forbidIp, String username, String password, int status, String uuid, int groupId) {
        this.wsType = wsType;
        this.name = name;
        this.url = url;
        this.description = description;
        this.checkType = checkType;
        this.permitIp = permitIp;
        this.forbidIp = forbidIp;
        this.username = username;
        this.password = password;
        this.status = status;
        this.uuid = uuid;
        this.groupId = groupId;
    }

   
    // Property accessors

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getWsType() {
        return this.wsType;
    }
    
    public void setWsType(String wsType) {
        this.wsType = wsType;
    }

    public String getWsLevel() {
		return wsLevel;
	}


	public void setWsLevel(String wsLevel) {
		this.wsLevel = wsLevel;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheckType() {
        return this.checkType;
    }
    
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getPermitIp() {
        return this.permitIp;
    }
    
    public void setPermitIp(String permitIp) {
        this.permitIp = permitIp;
    }

    public String getForbidIp() {
        return this.forbidIp;
    }
    
    public void setForbidIp(String forbidIp) {
        this.forbidIp = forbidIp;
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

    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }

    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


	public int getIsCache() {
		return isCache;
	}


	public void setIsCache(int isCache) {
		this.isCache = isCache;
	}


	public int getCacheTime() {
		return cacheTime;
	}


	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}
   








}