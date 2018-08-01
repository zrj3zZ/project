package com.iwork.plugs.dictionary.model;

/**
 * SysDictionaryShowfield entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysDictionaryShowfield implements java.io.Serializable {

	// Fields

	private Long id;
	private String fieldName;
	private String fieldTitle;
	private Long dictionaryId;
	private String targetField;
	private Long isShow;
	private Long displayWidth;
	private Long orderIndex;

	// Constructors

	/** default constructor */
	public SysDictionaryShowfield() {
	}

	/** full constructor */
	public SysDictionaryShowfield(String fieldName, String fieldTitle,
			Long dictionaryId, String targetField, Long isShow,
			Long displayWidth, Long orderIndex) {
		this.fieldName = fieldName;
		this.fieldTitle = fieldTitle;
		this.dictionaryId = dictionaryId;
		this.targetField = targetField;
		this.isShow = isShow;
		this.displayWidth = displayWidth;
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

	public String getTargetField() {
		return this.targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public Long getIsShow() {
		return this.isShow;
	}

	public void setIsShow(Long isShow) {
		this.isShow = isShow;
	}

	public Long getDisplayWidth() {
		return this.displayWidth;
	}

	public void setDisplayWidth(Long displayWidth) {
		this.displayWidth = displayWidth;
	}

	public Long getOrderIndex() {
		return this.orderIndex;
	}

	public void setOrderIndex(Long orderIndex) {
		this.orderIndex = orderIndex;
	}

}
