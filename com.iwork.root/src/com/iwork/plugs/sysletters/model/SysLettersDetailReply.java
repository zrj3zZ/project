package com.iwork.plugs.sysletters.model;
import java.util.Date;

/**
 * 回复表MODEL
 * @author WangJianhui
 */

public class SysLettersDetailReply implements java.io.Serializable {

	// Fields

	private Long id;
	private Long detailDataId;
	private Long letterId;
	private String replyContent;
	private String userId;
	private String ts;
	private String def1;
	private String def2;
	private String def3;
	private String def4;
	private Date def5;
	private Long def6;
	private SysLettersDetailInfo syslettersdetailinfo;

	// Constructors

	/** default constructor */
	public SysLettersDetailReply() {
	}

	/** full constructor */
	public SysLettersDetailReply(Long detailDataId, Long letterId,
			String replyContent, String userId, String ts, String def1,
			String def2, String def3, String def4, Date def5, Long def6,SysLettersDetailInfo syslettersdetailinfo) {
		this.detailDataId = detailDataId;
		this.letterId = letterId;
		this.replyContent = replyContent;
		this.userId = userId;
		this.ts = ts;
		this.def1 = def1;
		this.def2 = def2;
		this.def3 = def3;
		this.def4 = def4;
		this.def5 = def5;
		this.def6 = def6;
		this.syslettersdetailinfo = syslettersdetailinfo;
	}

	// Property accessors



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDetailDataId() {
		return detailDataId;
	}

	public void setDetailDataId(Long detailDataId) {
		this.detailDataId = detailDataId;
	}

	public void setLetterId(Long letterId) {
		this.letterId = letterId;
	}

	public void setDef6(Long def6) {
		this.def6 = def6;
	}


	public String getReplyContent() {
		return this.replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTs() {
		return this.ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
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

	public String getDef3() {
		return this.def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return this.def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public Date getDef5() {
		return this.def5;
	}

	public void setDef5(Date def5) {
		this.def5 = def5;
	}

	public SysLettersDetailInfo getSyslettersdetailinfo() {
		return syslettersdetailinfo;
	}

	public void setSyslettersdetailinfo(SysLettersDetailInfo syslettersdetailinfo) {
		this.syslettersdetailinfo = syslettersdetailinfo;
	}

	public Long getLetterId() {
		return letterId;
	}

	public Long getDef6() {
		return def6;
	}

}