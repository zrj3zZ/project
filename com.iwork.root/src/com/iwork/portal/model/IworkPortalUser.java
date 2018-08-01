package com.iwork.portal.model;


/**
 * IworkPortalUser entity. @author MyEclipse Persistence Tools
 */

public class IworkPortalUser implements java.io.Serializable {

	// Fields

	private Long id;
	private String userid;
	private Long type;
	private String itemOrder;
	private String extend1;
	private String extend2;
	private String extend3;

	// Constructors

	/** default constructor */
	public IworkPortalUser() {
	}

	/** full constructor */
	public IworkPortalUser(String userid, Long type, String itemOrder,
			String extend1, String extend2, String extend3) {
		this.userid = userid;
		this.type = type;
		this.itemOrder = itemOrder;
		this.extend1 = extend1;
		this.extend2 = extend2;
		this.extend3 = extend3;
	}

	// Property accessors

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

	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getItemOrder() {
		return this.itemOrder;
	}

	public void setItemOrder(String itemOrder) {
		this.itemOrder = itemOrder;
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

}