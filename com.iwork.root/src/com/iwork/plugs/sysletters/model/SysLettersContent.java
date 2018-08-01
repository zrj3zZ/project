package com.iwork.plugs.sysletters.model;

import java.util.Date;

/**
 * 站内信基本信息
 * @author WangJianhui
 */

@SuppressWarnings("serial")
public class SysLettersContent implements java.io.Serializable {

	// Fields

	private Long id;
	private String letterTitle;
	private String letterContent;
	private String letterLevel;
	private Date letterDate;
	private String ts;
	private String letterType;
	private String def1;
	private String def2;
	private Long def3;
	private Date def4;
	private String def5;
	private String def6;
	private String createUserId;
	private String createUserName;
	private String toUserIds;


	// Constructors

	/** default constructor */
	public SysLettersContent() {
	}

	/** full constructor */
	public SysLettersContent(String letterTitle, String letterContent,
			String letterLevel, Date letterDate, String ts, String letterType,
			String def1, String def2, Long def3, Date def4, String def5,
			String def6, String createUserId,String createUserName,String toUserIds) {
		this.letterTitle = letterTitle;
		this.letterContent = letterContent;
		this.letterLevel = letterLevel;
		this.letterDate = letterDate;
		this.ts = ts;
		this.letterType = letterType;
		this.def1 = def1;
		this.def2 = def2;
		this.def3 = def3;
		this.def4 = def4;
		this.def5 = def5;
		this.def6 = def6;
		this.createUserId = createUserId;
		this.createUserName = createUserName;
		this.toUserIds= toUserIds;
	}

	// Property accessors

	public String getLetterTitle() {
		return this.letterTitle;
	}

	public void setLetterTitle(String letterTitle) {
		this.letterTitle = letterTitle;
	}

	public String getLetterContent() {
		return this.letterContent;
	}

	public void setLetterContent(String letterContent) {
		this.letterContent = letterContent;
	}

	public String getLetterLevel() {
		return this.letterLevel;
	}

	public void setLetterLevel(String letterLevel) {
		this.letterLevel = letterLevel;
	}

	public Date getLetterDate() {
		return this.letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getLetterType() {
		return this.letterType;
	}

	public void setLetterType(String letterType) {
		this.letterType = letterType;
	}

	public String getDef1() {
		return this.def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return this.def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDef3() {
		return def3;
	}

	public void setDef3(Long def3) {
		this.def3 = def3;
	}

	public Date getDef4() {
		return this.def4;
	}

	public void setDef4(Date def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return this.def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getDef6() {
		return this.def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getToUserIds() {
		return toUserIds;
	}

	public void setToUserIds(String toUserIds) {
		this.toUserIds = toUserIds;
	}
	
	
}