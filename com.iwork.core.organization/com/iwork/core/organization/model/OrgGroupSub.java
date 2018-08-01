package com.iwork.core.organization.model;

import com.iwork.core.db.ObjectModel;

/**
 * OrgGroupSub entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class OrgGroupSub implements ObjectModel ,java.io.Serializable {
	public static String DATABASE_ENTITY = "OrgGroupSub";
	// Fields

	private String id;
	private String groupId;
	private String itemId;
	private String itemName;
	private String itemType;

	// Constructors

	/** default constructor */
	public OrgGroupSub() {
	}

	/** minimal constructor */
	public OrgGroupSub(String id) {
		this.id = id;
	}

	/** full constructor */
	public OrgGroupSub(String id, String groupId, String itemId, String itemName,
			String itemType) {
		this.id = id;
		this.groupId = groupId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemType = itemType;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

}
