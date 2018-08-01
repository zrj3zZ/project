package com.iwork.webservice.model;


/**
 * SysWsParams entity. @author MyEclipse Persistence Tools
 */

public class SysWsParams  implements java.io.Serializable {


    // Fields    

     private int id;
     private int pid;
     private String title;
     private String havingName;
     private String description;
     private String name;
     private String type;
     private String value;
     private String required;
     private String uuid;
     private String inorout;
     private int orderIndex;

    // Constructors

    /** default constructor */
    public SysWsParams() {
    }

    
    /** full constructor */
    public SysWsParams(int pid, String title, String havingName, String description, String name, String type, String value, String required, String uuid, String inorout) {
        this.pid = pid;
        this.title = title;
        this.havingName = havingName;
        this.description = description;
        this.name = name;
        this.type = type;
        this.value = value;
        this.required = required;
        this.uuid = uuid;
        this.inorout = inorout;
    }

   
    // Property accessors

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getPid() {
		return pid;
	}


	public void setPid(int pid) {
		this.pid = pid;
	}


	public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getHavingName() {
        return this.havingName;
    }
    
    public void setHavingName(String havingName) {
        this.havingName = havingName;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    public String getRequired() {
        return this.required;
    }
    
    public void setRequired(String required) {
        this.required = required;
    }

    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getInorout() {
        return this.inorout;
    }
    
    public void setInorout(String inorout) {
        this.inorout = inorout;
    }


	public int getOrderIndex() {
		return orderIndex;
	}


	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
   








}