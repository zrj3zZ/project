package com.iwork.plugs.appointment.dao;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.plugs.appointment.model.Appointment;
import com.iwork.plugs.calender.model.IworkSchCalendar;

public class AppointmentDAO extends HibernateDaoSupport {

	public List<String> getConfigTypeUsers() {
		StringBuffer sql = new StringBuffer();
		sql.append("select userid from orguser t1 where t1.usertype<>2 and not exists (SELECT DISTINCT USERID FROM SYS_PERSON_CONFIG t2 WHERE TYPE='CalendarIsShare' AND VALUE='0' and t1.userid = t2.userid )");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		List<String> list = new ArrayList<String>();
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String userid = rset.getString("USERID");
				list.add(userid);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	/**
	 * 获取指定用户的全部日程
	 * 
	 * @param userid
	 * @return
	 */
	public List<IworkSchCalendar> getUserCalendarList(String userid) {
		String sql = "FROM " + IworkSchCalendar.DATABASE_ENTITY + " WHERE USERID=? ORDER BY ID";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return l;
			}
		}
		Object[] value = {userid};
		List<IworkSchCalendar> list = this.getHibernateTemplate().find(sql,value);
		return list;
	}

	/**
	 * 获得时间段内指定用户的一般日程
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	public List<Appointment> getPeriodList(Date startdate, Date enddate,
			String userid) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Appointment.class);
		List<Appointment> list = criteria.list();
		session.close();
		return list;
	}

	/**
	 * 获得时间段内指定用户的重复事件
	 * 
	 * @param startdate
	 * @param enddate
	 * @param userid
	 * @return
	 */
	public List<Appointment> getPeriodList_Repeate(String userid) {
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Appointment.class);
		/*criteria.add(Restrictions.isNotNull("reStartdate"));*/
		criteria.add(Restrictions.eq("szr", userid));
		List<Appointment> list = criteria.list();
		session.close();
		return list;
	}

	/**
	 * 获取单条数据
	 * 
	 * @param id
	 * @return
	 */
	public Appointment getBoData(Long id) {
		Appointment model = (Appointment) this.getHibernateTemplate()
				.get(Appointment.class, id);
		return model;
	}

	/**
	 * 更新
	 * 
	 * @param model
	 */
	public void updateBoData(Appointment model) {
		this.getHibernateTemplate().update(model);
	}

	/**
	 * 插入
	 * 
	 * @param model
	 */
	public void addBoData(Appointment model) {
		this.getHibernateTemplate().save(model);
	}

	/**
	 * 删除
	 * 
	 * @param model
	 */
	public void deleteBoData(Appointment model) {
		this.getHibernateTemplate().delete(model);
	}

	public List<Map> getWorkLogList(int pageSize, int pageNow, String username,
			String startDate, String endDate) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		StringBuffer sql = new StringBuffer("select a.userid,a.username,a.num,b.title,to_char(b.startdate, 'yyyy-mm-dd')||' '||b.starttime startdate,to_char(b.enddate, 'yyyy-mm-dd')||' '||b.endtime enddate from (select a.userid, nvl(b.username, '') username, count(*) num from IWORK_SCH_CALENDAR a left join orguser b on a.userid = b.userid where 1=1  and (b.orgroleid <> 3 or b.orgroleid is null)");
		if (username != null && !"".equals(username)) {
			if(d.HasInjectionData(username)){
				return l;
			}
			sql.append(" and b.username=?");
		}
		sql.append(" group by a.userid, b.username) a,IWORK_SCH_CALENDAR b" + " where a.userid = b.userid");
		if (startDate != null && !"".equals(startDate)) {
			if(d.HasInjectionData(startDate)){
				return l;
			}
			sql.append(" and to_char(b.startdate,'yyyy-MM-dd')>= ?");
		}
		if (endDate != null && !"".equals(endDate)) {
			if(d.HasInjectionData(endDate)){
				return l;
			}
			sql.append(" and to_char(b.enddate,'yyyy-MM-dd')<= ?");
		}
		sql.append(" order by a.userid");
		final String sql1 = sql.toString();
		final String final_username = username;
		final String final_startDate = startDate;
		final String final_endDate = endDate;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int i=0;
				if (final_username != null && !"".equals(final_username)) {
					query.setParameter(i, final_username);i++;
				}
				if (final_startDate != null && !"".equals(final_startDate)) {
					query.setParameter(i, final_startDate);i++;
				}
				if (final_endDate != null && !"".equals(final_endDate)) {
					query.setParameter(i, final_endDate);i++;
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {

					String username = (String) object[1];
					if (m.get(username) != null) {
						b = new BigDecimal(m.get(username).toString());
						m.put(username, b.add(new BigDecimal(1)));
					} else {
						m.put(username, new BigDecimal(1));
					}

				}
				for (Object[] object : list) {
					map = new HashMap();
					String id = (String) object[0];
					String username = (String) object[1];
					BigDecimal num = (BigDecimal) object[2];
					String title = (String) object[3];
					String startDate = (String) object[4];
					String endDate = (String) object[5];
					map.put("username", username);
					map.put("num", m.get(username));
					map.put("startdate", startDate);
					map.put("endDate", endDate);
					map.put("title", title);
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getWorkLogRow() {
		int count = 0;
		String sql = "select count(*) as count FROM IWORK_SCH_CALENDAR";
		count = DBUtil.getInt(sql, "count");
		return count;
	}

	public int getWorkListSize(String username, String startDate, String endDate) {
		int count = 0;
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();
		int n = 1;
		sql.append("select COUNT(*)NUM from IWORK_SCH_CALENDAR a,orguser b WHERE  1=1 "
				+ " and a.userid=b.userid(+)  and (b.orgroleid <> 3 or b.orgroleid is null) ");
		if (username != null && !"".equals(username)) {
			sql.append(" and b.username= ? ");
			params.put(n, username);
			n++;
		}
		if (startDate != null && !"".equals(startDate)) {
			sql.append(" and to_char(a.startdate,'yyyy-mm-dd')>= ?  ");
			params.put(n, startDate);
			n++;
		}
		if (endDate != null && !"".equals(endDate)) {
			sql.append(" and to_char(a.enddate,'yyyy-mm-dd')<= ? ");
			params.put(n, endDate);
			n++;
		}
		count = DBUTilNew.getInt( "NUM",sql.toString(),params);
		return count;
	}

	public List<Map> getWorkLogList() {

		
		StringBuffer sql = new StringBuffer("select a.userid,a.username,a.num,b.title,to_char(b.startdate, 'yyyy-mm-dd')||' '|| b.starttime startdate,to_char(b.enddate, 'yyyy-mm-dd')||' '|| b.endtime  enddate from (select a.userid, nvl(b.username, '') username, count(*) num from IWORK_SCH_CALENDAR a left join orguser b on a.userid = b.userid where 1=1  and (b.orgroleid <> 3 or b.orgroleid is null) group by a.userid, b.username) a,IWORK_SCH_CALENDAR b where a.userid = b.userid order by a.userid");
		final String sql1 = sql.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<Map> l = new ArrayList<Map>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {

					String username = (String) object[1];
					if (m.get(username) != null) {
						b = new BigDecimal(m.get(username).toString());
						m.put(username, b.add(new BigDecimal(1)));
						
					} else {
						m.put(username, new BigDecimal(1));
						
					}

				}
				for (Object[] object : list) {
					map = new HashMap();
					String id = (String) object[0];
					String username = (String) object[1];
					BigDecimal num = (BigDecimal) object[2];
					String title = (String) object[3];
					String startDate = (String) object[4];
					String endDate = (String) object[5];
					map.put("username", username);
					map.put("num", m.get(username));
					map.put("startdate", startDate);
					map.put("endDate", endDate);
					map.put("title", title);
					l.add(map);
				}
				return l;
			}
		});
	}

	
	//获得提醒短信文本
	public String  tixingSMS(String title,String starttime){
		
		return title+";"+"开始时间"+starttime;
		
		
	}
	
	public void doExcelExp(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("工作日志");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("工作内容");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List list = getWorkLogList();
		List<Map> l;
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		Map person = new HashMap();

		for (int j = 0; j < list.size(); j++) {
			Map map = (HashMap) list.get(j);
			row = sheet.createRow((int) n++);
			// 第四步，创建单元格，并设置值
			HSSFCell cell1 = row.createCell((short) 0);
			cell1.setCellValue((map.get("username") != null ? map.get(
					"username").toString() : "")
					+ "(" + map.get("num").toString() + ")");
			cell1.setCellStyle(style2);
			// 如何记录已显示人员的map里没有记录，或者不等于当前的用户
			if (person.get("username") == null
					|| !person
							.get("username")
							.toString()
							.equals((map.get("username") != null ? map.get(
									"username").toString() : "")
									+ "(" + map.get("num").toString() + ")")) {
				// 单元格合并
				// 四个参数分别是：起始行，起始列，结束行，结束列
				if (person.get("username") != null) {
					sheet.addMergedRegion(new Region(Integer.parseInt(person
							.get("begin").toString()), (short) 0, n - 2,
							(short) 0));
				}
				person.put("username",
						(map.get("username") != null ? map.get("username")
								.toString() : "")
								+ "("
								+ map.get("num").toString() + ")");
				person.put("begin", n - 1);
				// 再把式样设置到cell中：
			}
			HSSFCell cell2 = row.createCell((short) 1);
			cell2.setCellValue(map.get("startdate").toString());
			cell2.setCellStyle(style1);
			HSSFCell cell3 = row.createCell((short) 2);
			cell3.setCellValue(map.get("title").toString());
			cell3.setCellStyle(style1);
			short colLength = (short) (map.get("title").toString().length() * 256 * 2);
			if (sheet.getColumnWidth(m) < colLength) {
				sheet.setColumnWidth(m, colLength);
			}
			m++;
		}
		sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")
				.toString()), (short) 0, n - 1, (short) 0));

		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("工作日志.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}

}
