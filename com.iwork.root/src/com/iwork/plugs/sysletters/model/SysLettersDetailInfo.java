package com.iwork.plugs.sysletters.model;

import java.util.Date;


/**
 * 站内信详细信息
 * @author WangJianhui
 */

public class SysLettersDetailInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private Long letterId;
	private String checkStatus;
	private Date letterDate;
	private String sentUserId;
	private String sentUserName;
	private String receiveUserId;
	private String receiveUserName;
	private String createUserId;
	private String createUserName;
	private String ts;
	private String letterType;
	private String def1;
	private String def2;
	private Long def3;
	private Date def4;
	private String def5;
	private String def6;
	private String ownerId;
	private SysLettersContent sysletterscontent;
	private SysLettersDetailReply syslettersdetailreply;
	

	// Constructors

	/** default constructor */
	public SysLettersDetailInfo() {
	}

	/** full constructor */
	public SysLettersDetailInfo(Long letterId, String checkStatus,
			Date letterDate, String sentUserId,String sentUserName, String receiveUserId,
			String receiveUserName,String createUserId,String createUserName, String ts, String letterType, String def1,
			String def2, Long def3, Date def4, String def5, String def6,SysLettersContent sysletterscontent,SysLettersDetailReply syslettersdetailreply) {
		this.letterId = letterId;
		this.checkStatus = checkStatus;
		this.letterDate = letterDate;
		this.sentUserId = sentUserId;
		this.sentUserName = sentUserName;
		this.receiveUserId = receiveUserId;
		this.receiveUserName = receiveUserName;
		this.createUserId = createUserId;
		this.createUserName = createUserName;
		this.ts = ts;
		this.letterType = letterType;
		this.def1 = def1;
		this.def2 = def2;
		this.def3 = def3;
		this.def4 = def4;
		this.def5 = def5;
		this.def6 = def6;
		this.sysletterscontent = sysletterscontent;
		this.syslettersdetailreply = syslettersdetailreply;
	}

	// Property accessors

//	//重写排序方法
//	public int compareTo(SysLettersDetailInfo model){
//		return this.getTs().compareTo(model.getTs());
//	}


	public String getCheckStatus() {
		return this.checkStatus;
	}

	public SysLettersContent getSysletterscontent() {
		return sysletterscontent;
	}

	public void setSysletterscontent(SysLettersContent sysletterscontent) {
		this.sysletterscontent = sysletterscontent;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Date getLetterDate() {
		return this.letterDate;
	}

	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}

	public String getSentUserId() {
		return this.sentUserId;
	}

	public void setSentUserId(String sentUserId) {
		this.sentUserId = sentUserId;
	}

	public String getReceiveUserId() {
		return this.receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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

	public Long getLetterId() {
		return letterId;
	}

	public void setLetterId(Long letterId) {
		this.letterId = letterId;
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

	public String getSentUserName() {
		return sentUserName;
	}

	public void setSentUserName(String sentUserName) {
		this.sentUserName = sentUserName;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public SysLettersDetailReply getSyslettersdetailreply() {
		return syslettersdetailreply;
	}

	public void setSyslettersdetailreply(SysLettersDetailReply syslettersdetailreply) {
		this.syslettersdetailreply = syslettersdetailreply;
	}

	
}