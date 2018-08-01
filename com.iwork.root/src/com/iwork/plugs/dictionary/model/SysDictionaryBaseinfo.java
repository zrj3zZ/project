package com.iwork.plugs.dictionary.model;



/**
 * SysDictionaryBaseinfo entity. @author MyEclipse Persistence Tools
 */

public class SysDictionaryBaseinfo  implements java.io.Serializable {
    // Fields    
     private Long id;
     private String dicName;
     private Long groupid;
     private Long dicType;
     private Long rowNum;
     private Long dsId;
     private Long isAutoShow;
     private String dsSql;
     private String master;
     private String memo;
     private String uuid;
     private String formUUID;
     private Long isDem;
     private String demUUID;
    // Constructors

    /** default constructor */
    public SysDictionaryBaseinfo() {
    	
    }
   
    // Property accessors
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getDicName() {
        return this.dicName;
    }
    
    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public Long getGroupid() {
        return this.groupid;
    }
    
    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public Long getDicType() {
        return this.dicType;
    }
    
    public void setDicType(Long dicType) {
        this.dicType = dicType;
    }

    public Long getRowNum() {
        return this.rowNum;
    }
    
    public void setRowNum(Long rowNum) {
        this.rowNum = rowNum;
    }

    public Long getDsId() {
        return this.dsId;
    }
    
    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public String getDsSql() {
        return this.dsSql;
    }
    
    public void setDsSql(String dsSql) {
        this.dsSql = dsSql;
    }

    public String getMaster() {
        return this.master;
    }
    
    public void setMaster(String master) {
        this.master = master;
    }

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
	public String getFormUUID() {
		return formUUID;
	}
	public void setFormUUID(String formUUID) {
		this.formUUID = formUUID;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getIsAutoShow() {
		return isAutoShow;
	}

	public void setIsAutoShow(Long isAutoShow) {
		this.isAutoShow = isAutoShow;
	}

	public Long getIsDem() {
		return isDem;
	}

	public void setIsDem(Long isDem) {
		this.isDem = isDem;
	}

	public String getDemUUID() {
		return demUUID;
	}

	public void setDemUUID(String demUUID) {
		this.demUUID = demUUID;
	}
    
}
