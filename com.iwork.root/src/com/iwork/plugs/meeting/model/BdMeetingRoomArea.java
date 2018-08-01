package com.iwork.plugs.meeting.model;

/**
 * BdMeetingRoomArea entity. @author MyEclipse Persistence Tools
 */

public class BdMeetingRoomArea implements java.io.Serializable {

	// Fields

	private long id;
	private String dqbh;
	private String dqmc;
	private String szwz;
	private String dqgly;

	// Constructors

	/** default constructor */
	public BdMeetingRoomArea() {
	}

	/** minimal constructor */
	public BdMeetingRoomArea(long id) {
		this.id = id;
	}

	/** full constructor */
	public BdMeetingRoomArea(long id, String dqbh, String dqmc,
			String szwz, String dqgly) {
		this.id = id;
		this.dqbh = dqbh;
		this.dqmc = dqmc;
		this.szwz = szwz;
		this.dqgly = dqgly;
	}

	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDqbh() {
		return this.dqbh;
	}

	public void setDqbh(String dqbh) {
		this.dqbh = dqbh;
	}

	public String getDqmc() {
		return this.dqmc;
	}

	public void setDqmc(String dqmc) {
		this.dqmc = dqmc;
	}

	public String getSzwz() {
		return this.szwz;
	}

	public void setSzwz(String szwz) {
		this.szwz = szwz;
	}

	public String getDqgly() {
		return this.dqgly;
	}

	public void setDqgly(String dqgly) {
		this.dqgly = dqgly;
	}

}