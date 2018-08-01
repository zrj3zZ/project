package com.iwork.plugs.resoucebook.model;

/**
 * IworkRmSpace entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkRmSpace implements java.io.Serializable {

	// Fields
	public static String DATABASE_ENTITY = "IworkRmSpace";
	private Long id;
	private String spacename;
	private Long type;
	private Long cycle;
	private String processId;
	private String manager;
	private Long status;
	private String memo;

	// Constructors

	/** default constructor */
	public IworkRmSpace() {
	}

	/** minimal constructor */
	public IworkRmSpace(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkRmSpace(Long id, String spacename, Long type,String processId, Long cycle,
			String manager, Long status, String memo) {
		this.id = id;
		this.spacename = spacename;
		this.type = type;
		this.processId = processId;
		this.cycle = cycle;
		this.manager = manager;
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

	public String getSpacename() {
		return this.spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getCycle() {
		return this.cycle;
	}

	public void setCycle(Long cycle) {
		this.cycle = cycle;
	}

	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
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

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

}
