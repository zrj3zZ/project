package com.iwork.plugs.meeting.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.meeting.model.BdMeetingRoom;
import com.iwork.plugs.meeting.model.BdMeetingRoomArea;
import com.iwork.plugs.meeting.model.IworkMeetingRoomOrder;

public class MeetingRoomDao extends HibernateDaoSupport {

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateMeetingRoom(BdMeetingRoom model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 保存
	 * 
	 * @param model
	 */
	public void saveMeetingRoom(BdMeetingRoom model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateMeetingRoomOrder(IworkMeetingRoomOrder model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 保存
	 * 
	 * @param model
	 */
	public void saveMeetingRoomOrder(IworkMeetingRoomOrder model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateMeetingRoomArea(BdMeetingRoomArea model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 保存
	 * 
	 * @param model
	 */
	public void saveMeetingRoomArea(BdMeetingRoomArea model) {
		this.getHibernateTemplate().save(model);
	}

	public IworkMeetingRoomOrder getIworkMeetingRoomOrder(Long orderId) {
		return (IworkMeetingRoomOrder) this.getHibernateTemplate().get(
				IworkMeetingRoomOrder.class, orderId);
	}
}
