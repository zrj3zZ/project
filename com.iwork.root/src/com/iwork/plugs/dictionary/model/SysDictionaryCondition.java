package com.iwork.plugs.dictionary.model;

/**
 * SysDictionaryCondition entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysDictionaryCondition implements java.io.Serializable {

	// Fields

	private Long id;
	private String fieldName;
	private String fieldTitle;
	private Long dictionaryId;
	private String compareType;
	private Long dsType;
	private Long fieldType;
	private String displayType;
	private String displayEnum;
	private Long orderIndex;

	// Constructors

	/** default constructor */
	public SysDictionaryCondition() {
	}

	/** full constructor */
	public SysDictionaryCondition(String fieldName, String fieldTitle,
			Long dictionaryId, String compareType,Long dsType,Long fieldType, String displayType,
			String displayEnum, Long orderIndex) {
		this.fieldName = fieldName;
		this.fieldTitle = fieldTitle;
		this.dictionaryId = dictionaryId;
		this.compareType = compareType; 
		this.dsType = dsType;
		this.fieldType = fieldType;
		this.displayType = displayType;
		this.displayEnum = displayEnum;
		this.orderIndex = orderIndex;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTitle() {
		return this.fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public Long getDictionaryId() {
		return this.dictionaryId;
	}

	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public String getCompareType() {
		return this.compareType;
	}

	public void setCompareType(String compareType) {
		this.compareType = compareType;
	}

	public String getDisplayType() {
		return this.displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getDisplayEnum() {
		return this.displayEnum;
	}

	public void setDisplayEnum(String displayEnum) {
		this.displayEnum = displayEnum;
	}

	public Long getOrderIndex() {
		return this.orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}

	public Long getDsType() {
		return dsType;
	}
	public void setDsType(Long dsType) {
		this.dsType = dsType;
	}

	public Long getFieldType() {
		return fieldType;
	} 

	public void setFieldType(Long fieldType) {
		this.fieldType = fieldType;
	}

}
