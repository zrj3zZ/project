package com.iwork.plugs.meeting.bean;

import java.util.Date;

public class MeetingTypebean implements Comparable<MeetingTypebean> {
	//public static final int TIME_BLOCKS = 48;
	private String meetingNo;
	private String meetingName;
	private String title;
	private boolean isApprovalNedded;
	private Date date;
	private String assetNo;
	private MeetingBlock[] blocks;

	public MeetingTypebean(int maxNum) {
		this.blocks = new MeetingBlock[maxNum];
		for (int i = 0; i < this.blocks.length; i++) {
			MeetingBlock b = new MeetingBlock(this);
			b.setIndex(i);
			this.blocks[i] = b;
		}
	}

	public boolean isApprovalNedded() {
		return this.isApprovalNedded;
	}

	public void setApprovalNedded(boolean isApprovalNedded) {
		this.isApprovalNedded = isApprovalNedded;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMeetingName() {
		return this.meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public String getMeetingNo() {
		return this.meetingNo;
	}

	public void setMeetingNo(String meetingNo) {
		this.meetingNo = meetingNo;
	}

	public MeetingBlock getBlock(int i) {
		return this.blocks[i];
	}

	public void setBlock(MeetingBlock block, int i) {
		this.blocks[i] = block;
	}

	public MeetingBlock[] getBlocks() {
		return this.blocks;
	}

	public String getAssetNo() {
		return this.assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public int compareTo(MeetingTypebean o) {
		return this.assetNo.compareTo(o.assetNo);
	}
}