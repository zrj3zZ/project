package com.iwork.plugs.meeting.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MeetingBlock {
	private MeetingTypebean parentRoom;
	private boolean isReserved;
	private int roomOrderBindid;
	private String uid;
	private String blockTitle;
	private int blockIndex;
	private int index;
	private int orderId;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public MeetingBlock(MeetingTypebean parent) {
		this.parentRoom = parent;
	}

	public String getBlockTitle() {
		return this.blockTitle;
	}

	public void setBlockTitle(String blockTitle) {
		this.blockTitle = blockTitle;
	}

	public int getRoomOrderBindid() {
		return this.roomOrderBindid;
	}

	public void setRoomOrderBindid(int roomOrderBindid) {
		this.roomOrderBindid = roomOrderBindid;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean isReserved() {
		return this.isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public MeetingTypebean getParentRoom() {
		return this.parentRoom;
	}

	public int getBlockIndex() {
		return this.blockIndex;
	}

	public void setBlockIndex(int blockIndex) {
		this.blockIndex = blockIndex;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getBlockTime(int index) {
		int a = 9;
		int b = 0;
		a += index / 4;
		b += index % 4 * 15;
		String c = Integer.toString(a);
		String d = Integer.toString(b);
		if (c.equals("9"))
			c = "09";
		if (d.equals("0"))
			d = "00";
		String time = c + ":" + d;
		return time;
	}

	public String getUnsubscribeScript(String uid) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(this.parentRoom
				.getDate());
		String time = date + " " + getBlockTime(getIndex());
		String curtime = new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.format(new Date());
		if (time.compareTo(curtime) < 0) {
			return "onDblclick=\"alert('对不起！此会议室预订时间已过,不可退订！');\"";
		}
		if (!this.uid.equals(uid)) {
			return "onDblclick=\"alert('对不起，您无权取消或修改该会议室预订！');\"";
		}
		return "onDblclick=\"openUpdateOrderHtml("+ this.orderId + ",'" + date + "');\" ";
	}
}