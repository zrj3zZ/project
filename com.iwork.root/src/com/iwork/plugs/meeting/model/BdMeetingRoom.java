package com.iwork.plugs.meeting.model;

/**
 * BdMeetingRoom entity. @author MyEclipse Persistence Tools
 */

public class BdMeetingRoom implements java.io.Serializable {

	// Fields

	private long id;
	private String meetingno;
	private String meetingname;
	private String meetingtype;
	private String meetingsize;
	private String meetingconf;
	private String assetno;
	private String hyszt;
	private String dimensions;
	private String meetingdq;
	private String meetingwz;
	private String meetingdqbm;
	private String ifsp;
	private String meetinglc;

	// Constructors

	/** default constructor */
	public BdMeetingRoom() {
	}

	/** minimal constructor */
	public BdMeetingRoom(long id) {
		this.id = id;
	}

	/** full constructor */
	public BdMeetingRoom(long id, String meetingno, String meetingname,
			String meetingtype, String meetingsize, String meetingconf,
			String assetno, String hyszt, String dimensions, String meetingdq,
			String meetingwz, String meetingdqbm, String ifsp, String meetinglc) {
		this.id = id;
		this.meetingno = meetingno;
		this.meetingname = meetingname;
		this.meetingtype = meetingtype;
		this.meetingsize = meetingsize;
		this.meetingconf = meetingconf;
		this.assetno = assetno;
		this.hyszt = hyszt;
		this.dimensions = dimensions;
		this.meetingdq = meetingdq;
		this.meetingwz = meetingwz;
		this.meetingdqbm = meetingdqbm;
		this.ifsp = ifsp;
		this.meetinglc = meetinglc;
	}

	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMeetingno() {
		return this.meetingno;
	}

	public void setMeetingno(String meetingno) {
		this.meetingno = meetingno;
	}

	public String getMeetingname() {
		return this.meetingname;
	}

	public void setMeetingname(String meetingname) {
		this.meetingname = meetingname;
	}

	public String getMeetingtype() {
		return this.meetingtype;
	}

	public void setMeetingtype(String meetingtype) {
		this.meetingtype = meetingtype;
	}

	public String getMeetingsize() {
		return this.meetingsize;
	}

	public void setMeetingsize(String meetingsize) {
		this.meetingsize = meetingsize;
	}

	public String getMeetingconf() {
		return this.meetingconf;
	}

	public void setMeetingconf(String meetingconf) {
		this.meetingconf = meetingconf;
	}

	public String getAssetno() {
		return this.assetno;
	}

	public void setAssetno(String assetno) {
		this.assetno = assetno;
	}

	public String getHyszt() {
		return this.hyszt;
	}

	public void setHyszt(String hyszt) {
		this.hyszt = hyszt;
	}

	public String getDimensions() {
		return this.dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getMeetingdq() {
		return this.meetingdq;
	}

	public void setMeetingdq(String meetingdq) {
		this.meetingdq = meetingdq;
	}

	public String getMeetingwz() {
		return this.meetingwz;
	}

	public void setMeetingwz(String meetingwz) {
		this.meetingwz = meetingwz;
	}

	public String getMeetingdqbm() {
		return this.meetingdqbm;
	}

	public void setMeetingdqbm(String meetingdqbm) {
		this.meetingdqbm = meetingdqbm;
	}

	public String getIfsp() {
		return this.ifsp;
	}

	public void setIfsp(String ifsp) {
		this.ifsp = ifsp;
	}

	public String getMeetinglc() {
		return this.meetinglc;
	}

	public void setMeetinglc(String meetinglc) {
		this.meetinglc = meetinglc;
	}

}