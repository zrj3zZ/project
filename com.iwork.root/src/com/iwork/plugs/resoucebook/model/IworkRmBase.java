package com.iwork.plugs.resoucebook.model;

/**
 * IworkRmBase entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkRmBase implements java.io.Serializable {
 public static String DATABASE_ENTITY ="IworkRmBase";
	// Fields

	private Long id;
	private Long spaceid;
	private String spacename;
	private String resouceid;
	private String resoucename;
	private String picture;
	private String parameter1;
	private String parameter2;
	private String parameter3;
	private String parameter4;
	private String parameter5;
	private Long status;
	private String memo;

	// Constructors

	/** default constructor */
	public IworkRmBase() {
	}

	/** minimal constructor */
	public IworkRmBase(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkRmBase(Long id, Long spaceid, String spacename, String resouceid,
			String resoucename, String picture, String parameter1,
			String parameter2, String parameter3, String parameter4,
			String parameter5, Long status, String memo) {
		this.id = id;
		this.spaceid = spaceid;
		this.spacename = spacename;
		this.resouceid = resouceid;
		this.resoucename = resoucename;
		this.picture = picture;
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
		this.parameter3 = parameter3;
		this.parameter4 = parameter4;
		this.parameter5 = parameter5;
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

	public Long getSpaceid() {
		return this.spaceid;
	}

	public void setSpaceid(Long spaceid) {
		this.spaceid = spaceid;
	}

	public String getSpacename() {
		return this.spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public String getResouceid() {
		return this.resouceid;
	}

	public void setResouceid(String resouceid) {
		this.resouceid = resouceid;
	}

	public String getResoucename() {
		return this.resoucename;
	}

	public void setResoucename(String resoucename) {
		this.resoucename = resoucename;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getParameter1() {
		return this.parameter1;
	}

	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	public String getParameter2() {
		return this.parameter2;
	}

	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}

	public String getParameter3() {
		return this.parameter3;
	}

	public void setParameter3(String parameter3) {
		this.parameter3 = parameter3;
	}

	public String getParameter4() {
		return this.parameter4;
	}

	public void setParameter4(String parameter4) {
		this.parameter4 = parameter4;
	}

	public String getParameter5() {
		return this.parameter5;
	}

	public void setParameter5(String parameter5) {
		this.parameter5 = parameter5;
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
