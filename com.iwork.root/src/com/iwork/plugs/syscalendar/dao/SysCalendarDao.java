package com.iwork.plugs.syscalendar.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.plugs.syscalendar.model.SysCalendarBaseInfoModel;
import com.iwork.plugs.syscalendar.model.SysCalendarDetailInfoModel;
import com.iwork.plugs.syscalendar.util.SysCalendarUtil;

public class SysCalendarDao extends HibernateDaoSupport{
	
	/**
	 * 获取日历列表
	 * @return
	 */
	public List<SysCalendarBaseInfoModel> queryCalendarList(){
		List<SysCalendarBaseInfoModel> list = new ArrayList<SysCalendarBaseInfoModel>();
		String sql = "FROM SysCalendarBaseInfoModel order by calendarType desc,createTime desc";
		list = this.getHibernateTemplate().find(sql);
		return list;
	}
	/**
	 * 获取启用并且在有效期内的日历列表
	 * @return
	 */
	public List<SysCalendarBaseInfoModel> queryCalendarListIsStatus(){
		List<SysCalendarBaseInfoModel> list = new ArrayList<SysCalendarBaseInfoModel>();
		SysCalendarUtil util = new SysCalendarUtil();
		String tempDate = util.dateToStr(new Date());
		// 2014-6-16 增加有效期限制
		String sql = "FROM SysCalendarBaseInfoModel where status='1'";
		sql = sql + " and expDateFrom<=to_date(?,'yyyy-mm-dd') and expDateTo>=to_date(?,'yyyy-mm-dd')) order by calendarType desc,createTime desc";
		Object[] value = {tempDate,tempDate};
		list = this.getHibernateTemplate().find(sql,value);
		return list;
	}
	/**
	 * 获得系统默认日历
	 * @return
	 */
	public SysCalendarBaseInfoModel getCalendarDefault(){
		String sql = " FROM SysCalendarBaseInfoModel where calendarType='0' and status='1'";
		SysCalendarUtil util = new SysCalendarUtil();
		String tempDate = util.dateToStr(new Date());
		// 2014-6-16
		sql = sql + " and expDateFrom<=to_date(?,'yyyy-mm-dd') and expDateTo>=to_date(?,'yyyy-mm-dd'))";
		List<SysCalendarBaseInfoModel> list = new ArrayList<SysCalendarBaseInfoModel>();
		Object[] value = {tempDate,tempDate};
		list = this.getHibernateTemplate().find(sql,value);
		return list.get(0);
		
	}
	/**
	 * 根据日历ID查询系统日历
	 * @param id
	 * @return
	 */
	public SysCalendarBaseInfoModel queryCalendarById(Long id){
		SysCalendarBaseInfoModel model = new SysCalendarBaseInfoModel();
		String sql = "FROM SysCalendarBaseInfoModel where id=?";
		Object[] value = {id};
		List<SysCalendarBaseInfoModel> list = this.getHibernateTemplate().find(sql,value);
		if(list!=null&&list.size()>0){
			model = list.get(0);
		}else{
			model = null;
		}
		return model;
	}
	/**
	 * 增加节日或工作日
	 * @param id 主表ID
	 * @param _date 日期 yyyy-MM-dd
	 * @return msg 插入信息
	 */
	public String saveHolidaysOrWorkDays(SysCalendarDetailInfoModel detailCalendar){
		String msg = "success";
		Long calendar_id = detailCalendar.getCalendarId();
		Date date = detailCalendar.getCDate();
		//判断该天是否已经设置过
		List<SysCalendarDetailInfoModel> list = getExsitsDateBySetded(calendar_id,date);
		if(list!=null&&list.size()>0){
			removeDateBySeted(list);
		}
		this.getHibernateTemplate().saveOrUpdate(detailCalendar);
		return msg;
	}
	/**
	 * 获得该日已经设置过的日期
	 * @param calendar_id
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysCalendarDetailInfoModel> getExsitsDateBySetded(Long calendar_id , final Date date){
		
		List<SysCalendarDetailInfoModel> list = new ArrayList<SysCalendarDetailInfoModel>();
		final String sql = " FROM SysCalendarDetailInfoModel WHERE  calendarId=? and CDate=:date";
		final Long final_calendar_id = calendar_id;
		list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				Query query = session.createQuery(sql);
				query.setLong(0, final_calendar_id);
				if(date!=null){
					DBUtilInjection d=new DBUtilInjection();
				    List lis=new ArrayList();
				    if ((date != null) && (!"".equals(date))) {
			        	if(d.HasInjectionData(date.toString())){
		    				return lis;
		    			}
			         
			        }
					query.setTimestamp("date", date);
				}
				List<SysCalendarDetailInfoModel> list = query.list();
				// TODO Auto-generated method stub
				return list;
			}
		});
		return list;
	}
	
	/**
	 * 移除已经存在的设置日期
	 * @param list
	 */
	public void removeDateBySeted(List<SysCalendarDetailInfoModel> list){
		
		for(SysCalendarDetailInfoModel model:list){
			this.getHibernateTemplate().delete(model);
		}

	}
	/**
	 * 刪除选中日历
	 * @param ids
	 * @return
	 */
	public int deleteCalendar(String[] ids){
		int count = 0;
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		String hql_main = "delete SysCalendarBaseInfoModel WHERE id=?";
		String hql_sub = "delete SysCalendarDetailInfoModel WHERE calendarId=?";
		Query query_main = session.createQuery(hql_main);
		Query query_sub = session.createQuery(hql_sub);
		if(ids!=null&&ids.length>0){
			
			for(int i =0;i<ids.length;i++){
				Long id = Long.valueOf(ids[i]);
				query_main.setLong(0, id);
				query_sub.setLong(0, id);
				int t_count = query_main.executeUpdate();
				if(t_count>0){
					query_sub.executeUpdate();
				}
				count++;
			}
		}
		tx.commit();
		session.close();
		return count;
	}
	/**
	 * 查询当年当月设置过的日期
	 * @param canlendar_id
	 * @param month
	 * @param year
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysCalendarDetailInfoModel> queryHolidaysBySeted(Long canlendar_id,int month,int year){
		final String sql =  " FROM SysCalendarDetailInfoModel WHERE calendarId=? and to_number(to_char(c_date,'mm')) =? and to_number(to_char(c_date,'yyyy')) =?";
		
		final Long final_canlendar_id = canlendar_id;
		final int final_month = month;
		final int final_year = year;
		List<SysCalendarDetailInfoModel> list =new ArrayList<SysCalendarDetailInfoModel>();
		list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createQuery(sql);
				query.setLong(0, final_canlendar_id);
				query.setInteger(1, final_month);
				query.setInteger(2, final_year);
				List<SysCalendarDetailInfoModel> list = query.list();
				// TODO Auto-generated method stub
				return list;
			}
		});
		return list;
		
	}
	//判断是否已经存在系统默认日历
	public boolean isExsitsDefaultCal(Date dateFrom,Date dateTo){
		SysCalendarUtil util = new SysCalendarUtil();
		String DateFromS = util.dateToStr(dateFrom);
		String DateToS = util.dateToStr(dateTo);
		boolean flag = false;
		List<SysCalendarBaseInfoModel> list = null;
		String sql = "FROM SysCalendarBaseInfoModel where calendarType='0' and status='1' and ((expDateFrom<=to_date(?,'yyyy-mm-dd') and expDateTo>=to_date(?,'yyyy-mm-dd')))";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((DateFromS != null) && (!"".equals(DateFromS))) {
        	if(d.HasInjectionData(DateFromS)){
				return flag;
			}
         
        }
	    if ((DateToS != null) && (!"".equals(DateToS))) {
        	if(d.HasInjectionData(DateToS)){
				return flag;
			}
         
        }
		Object[] value = {DateToS,DateFromS};
		list = this.getHibernateTemplate().find(sql,value);
		if(list!=null&&list.size()>0){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}
	//判断是否已经存在系统默认日历(更新数据专用)
	public boolean isExsitsDefaultCal_update(Long self_id,SysCalendarBaseInfoModel calendar){
		boolean flag = false;
		SysCalendarUtil util = new SysCalendarUtil();
		List<SysCalendarBaseInfoModel> list = null;
		Date dateFromD = calendar.getExpDateFrom();
		Date dateToD = calendar.getExpDateTo();
		String DateFromS = util.dateToStr(dateFromD);
		String DateToS = util.dateToStr(dateToD);
		String sql = "FROM SysCalendarBaseInfoModel where calendarType='0' and id!=? and status='1' and ((expDateFrom<=to_date(?,'yyyy-mm-dd') and expDateTo>=to_date(?,'yyyy-mm-dd')))";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((DateFromS != null) && (!"".equals(DateFromS))) {
        	if(d.HasInjectionData(DateFromS)){
				return flag;
			}
         
        }
	    if ((DateToS != null) && (!"".equals(DateToS))) {
        	if(d.HasInjectionData(DateToS)){
				return flag;
			}
         
        }
		Object[] value = {self_id,DateToS,DateFromS};
		list = this.getHibernateTemplate().find(sql,value);
		if(list!=null&&list.size()>0){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}
	public Long addCalendar(SysCalendarBaseInfoModel infoModel){
		this.getHibernateTemplate().saveOrUpdate(infoModel);
		Long id = infoModel.getId();
		return id;
	}
	public Long updateCalendarById(SysCalendarBaseInfoModel infoModel){
		this.getHibernateTemplate().update(infoModel);
		Long id = infoModel.getId();
		return id;
		
	}
	/**
	 * 查询时间段的设置日期
	 * @param calendarId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<SysCalendarDetailInfoModel> queryDatesSetedByCalendarId(Long calendarId,final Date startDate,final Date endDate){
		List<SysCalendarDetailInfoModel> list = new ArrayList<SysCalendarDetailInfoModel>();
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
		StringBuffer sb = new StringBuffer(" FROM SysCalendarDetailInfoModel WHERE 1=1");
		if(calendarId!=null){
			sb.append(" and calendarId=?");
		}
		if(startDate!=null){
			if(d.HasInjectionData(startDate.toString())){
				return lis;
			}
			sb.append(" and CDate>=?");
		}
		if(endDate!=null){
			if(d.HasInjectionData(endDate.toString())){
				return lis;
			}
			sb.append(" and CDate<=?");
		}
		final String sql = sb.toString();
		final Long final_calendarId = calendarId;
		list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				Query query = session.createQuery(sql);
				int i= 0;
				if(final_calendarId!=null){
					query.setLong(i, final_calendarId);i++;
				}
				if(startDate!=null){
					query.setTimestamp(i, startDate);i++;
				}
				if(endDate!=null){
					query.setTimestamp(i, endDate);i++;
				}
				List<SysCalendarDetailInfoModel> list = query.list();
				// TODO Auto-generated method stub
				return list;
			}
		});
		return list;
	}
	
}
