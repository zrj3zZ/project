package com.iwork.plugs.sms.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import oracle.jdbc.driver.OracleTypes;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.plugs.sms.bean.PhonebookMst;
import com.iwork.plugs.sms.bean.PhonebookgroupMst;
import com.iwork.plugs.sms.service.PhonebookService;

public class PhonebookDao extends HibernateDaoSupport {
	PhonebookService phonebookservice=new PhonebookService();
	
	/**
	 * 获得所有分组的信息(jquery)
	 * @return
	 */
	public List getAlltype() {			 
		String sql="FROM "+PhonebookgroupMst.DATABASE_ENTITY;
	    List list =  this.getHibernateTemplate().find(sql);
		return list;
}
	/**
	 * 根据分组获得所有号码的信息(jquery)
	 * @return
	 */
	public List getTypeNums(String ctype1, String cname, String cmobilenum,
			String cattr1, String cattr2, String cattr3) {	
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();
		sb.append(" where 1='1' ");
		if (!ctype1.equals("")) {
			sb.append(" and groupname=? ");
			params.add(ctype1);
		}
		if (!cname.equals("")) {
			sb.append(" and name like ?");
			params.add("%"+cname+"%");
		}
		if (!cmobilenum.equals("")) {
			sb.append(" and mobilenum like ? ");
			params.add("%"+cmobilenum+"%");
		}
		if (!cattr1.equals("")) {
			sb.append(" and attr1 like ? ");
			params.add("%"+cattr1+"%");
		}
		if (!cattr2.equals("")) {
			sb.append(" and attr2 like ? ");
			params.add("%"+cattr2+"%");
		}
		if (!cattr3.equals("")) {
			sb.append(" and attr3 like ? ");
			params.add("%"+cattr3+"%");
		}
		
	    String sql="FROM "+PhonebookMst.DATABASE_ENTITY+sb.toString();
	    List list =  new ArrayList();
	    int size = params.size();
	    if(size>0){
	    	DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList(); 
	    	Object[] value = new Object[size];
	    	for (int i = 0; i < size; i++) {
				value[i] = params.get(i);
				if ((value[i] != null) && (!"".equals(value[i]))) {
		        	if(d.HasInjectionData(value[i].toString())){
	    				return lis;
	    			}
		          
		        }
			}
	    	list =  this.getHibernateTemplate().find(sql,value);
	    }else{
	    	list =  this.getHibernateTemplate().find(sql);
	    }
		return list;
}
	public void adddb(PhonebookMst pm) {
//		Session ss = getSessionFactory().openSession();
//		Transaction tx = ss.beginTransaction();
//		ss.save(pm);
//		tx.commit();
//		ss.close();
		this.getHibernateTemplate().save(pm);
	}
/**
 * 号码簿查询号码
 * @param ctype1
 * @param cname
 * @param cmobilenum
 * @param cattr1
 * @param cattr2
 * @param cattr3
 * @return
 */
	public String querydb(String ctype1, String cname, String cmobilenum,
			String cattr1, String cattr2, String cattr3) {
		ArrayList list1 = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append(" where 1='1' ");
		String html = "";
		Session ss = getSessionFactory().openSession();
		if (!ctype1.equals("")) {
			sb.append(" and groupname=? ");
		}
		if (!cname.equals("")) {
			sb.append(" and name like ?");
		}
		if (!cmobilenum.equals("")) {
			sb.append(" and mobilenum like ?");
		}
		if (!cattr1.equals("")) {
			sb.append(" and ATTR1 like ? ");
		}
		if (!cattr2.equals("")) {
			sb.append(" and ATTR2 like ? ");
		}
		if (!cattr3.equals("")) {
			sb.append(" and ATTR3 like ? ");
		}
		try {
			Query q = ss.createQuery("from PhonebookMst " + sb.toString());
			int j=0;
			if (!ctype1.equals("")) {
				q.setString(j, ctype1);j++;
			}
			if (!cname.equals("")) {
				q.setString(j, "%"+cname+"%");j++;
			}
			if (!cmobilenum.equals("")) {
				q.setString(j, "%"+cmobilenum+"%");j++;
			}
			if (!cattr1.equals("")) {
				q.setString(j, "%"+cattr1+"%");j++;
			}
			if (!cattr2.equals("")) {
				q.setString(j, "%"+cattr2+"%");j++;
			}
			if (!cattr3.equals("")) {
				q.setString(j, "%"+cattr3+"%");j++;
			}
			ArrayList list = (ArrayList) q.list();
			int sice = list.size();
			for (int i = 0; i < list.size(); i++) {
				// if(list.get(i) instanceof PhonebookAction) {
				// int d=3;
				// }
				// if(list.get(i) instanceof PhonebookMst) {
				// int d=4;
				// }

				PhonebookMst user = (PhonebookMst) list.get(i);
				Hashtable ht = new Hashtable();
				// int typeid = user.getGroupid();
				int cid = user.getCid();
				String typename = user.getGroupname()==null?"":user.getGroupname();
				String name = user.getName() == null ? " " : user.getName();
				long phone = user.getMobilenum();
				String attr1 = user.getAttr1() == null ? "" : user.getAttr1();
				String attr2 = user.getAttr2() == null ? "" : user.getAttr2();
				String attr3 = user.getAttr3() == null ? "" : user.getAttr3();
				ht.put("GROUPNAME", typename);
				ht.put("CID", cid);
				ht.put("NAME", name);
				ht.put("MOBILENUM", phone);
				ht.put("EXTEND1", attr1);
				ht.put("EXTEND2", attr2);
				ht.put("EXTEND3", attr3);
				list1.add(ht);

			}
			
			html = phonebookservice.getList(list1);

		} catch (Exception e) {
			logger.error(e,e);

		}finally{
			ss.close();
		}
		return html;
	}

	
	/**
	 * 
	 * 从数据库取号码簿分组
	 * 
	 * @return
	 */
	public String querytype() {
		ArrayList list1 = new ArrayList();
		String html = "";
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery("from PhonebookgroupMst");
			ArrayList list = (ArrayList) q.list();
			int sice = list.size();
			for (int i = 0; i < list.size(); i++) {
				// if(list.get(i) instanceof PhonebookAction) {
				// int d=3;
				// }
				// if(list.get(i) instanceof PhonebookMst) {
				// int d=4;
				// }

				PhonebookgroupMst user = (PhonebookgroupMst) list.get(i);
				Hashtable ht = new Hashtable();
				int typeid = user.getGroupid();
				String name = user.getGroupname() == null ? "" : user
						.getGroupname();
				ht.put("GROUPID", typeid);
				ht.put("GROUPNAME", name);

				list1.add(ht);

			}
			html = phonebookservice.getListtype(list1);

		} catch (Exception e) {
			logger.error(e,e);

		}finally{
			ss.close();
		}
		return html;
	}

	
	/**
	 * 增加分组
	 * 
	 * @param pm
	 */

	public void addtype(PhonebookgroupMst pm) {
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		ss.save(pm);
		tx.commit();
		ss.close();
	}

	/**
	 * 分组设置页面，分组的查询与显示
	 * 
	 * @return
	 */
	public String querytypeall() {
		ArrayList list1 = new ArrayList();
		String html = "";
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery("from PhonebookgroupMst where groupid!=0");
			ArrayList list = (ArrayList) q.list();
			int sice = list.size();
			for (int i = 0; i < list.size(); i++) {
				PhonebookgroupMst user = (PhonebookgroupMst) list.get(i);
				Hashtable ht = new Hashtable();
				int typeid = user.getGroupid();
				String name = user.getGroupname() == null ? " " : user
						.getGroupname();
				ht.put("GROUPID", typeid);
				ht.put("ID", i + 1);
				ht.put("GROUPNAME", name);
				list1.add(ht);
			}
			html = phonebookservice.getListtypeall(list1);

		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			ss.close();
		}
		return html;
	}
	
/**
 * 号码簿删除号码
 * @param cid
 */
	public void delnum(String cid) {
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		Query q = ss.createQuery("delete from PhonebookMst where CID=? ");
		q.setString(0, cid);
		int ret = q.executeUpdate();
		tx.commit();
		ss.close();

	}

	public PhonebookMst getPhonebookMst(int cid) {
		PhonebookMst model = new PhonebookMst();
		Session ss = getSessionFactory().openSession();
		String sql = null;
		try {
			Query q = ss.createQuery("from PhonebookMst where CID=?");
			q.setInteger(0, cid);
			ArrayList list = (ArrayList) q.list();
			int sice = list.size();
			for (int i = 0; i < list.size(); i++) {
				PhonebookMst rs = (PhonebookMst) list.get(i);
				model.setName(rs.getName());
				if (model.getName() == null)
					model.setName("");
				model.setMobilenum(rs.getMobilenum());
				if (model.getMobilenum() == 0)
					model.setMobilenum(0);
				model.setAttr1(rs.getAttr1());
				if (model.getAttr1() == null)
					model.setAttr1("");
				model.setAttr2(rs.getAttr2());
				if (model.getAttr2() == null)
					model.setAttr2("");
				model.setAttr3(rs.getAttr3());
				if (model.getAttr3() == null)
					model.setAttr3("");
				model.setGroupname(rs.getGroupname());
				if (model.getGroupname() == null)
					model.setGroupname("");

			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			ss.close();
		}
		return model;
	}
	/**
	 * 号码簿修改号码的时候分组的显示
	 * @param type
	 * @return
	 */
	public String querytype(String type) {
		ArrayList list1 = new ArrayList();
		String html = "";
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery("from PhonebookgroupMst");
			ArrayList list = (ArrayList) q.list();
			int sice = list.size();
			for (int i = 0; i < list.size(); i++) {
				PhonebookgroupMst user = (PhonebookgroupMst) list.get(i);
				Hashtable ht = new Hashtable();
				int typeid = user.getGroupid();
				String name = user.getGroupname() == null ? " " : user
						.getGroupname();
				ht.put("GROUPID", typeid);
				ht.put("GROUPNAME", name);
				list1.add(ht);
			}
			html = phonebookservice.getListtypenum(list1,type);

		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			ss.close();
		}
		return html;
	}

	/**
	 * 号码簿修改号码后保存号码
	 * @param cid
	 */
	public void addeditnum(String cid,String nameedit,String mobileedit,String extend1edit,String extend2edit,String extend3edit,String typeedit){
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		long mobilenum=Long.valueOf(mobileedit).longValue();
		Query q = ss.createQuery("update PhonebookMst set name=?,mobilenum=?,groupname=?,attr1=?,attr2=?,attr3=? where CID=?");
		q.setString(0, nameedit);
		q.setLong(1, mobilenum);
		q.setString(2, typeedit);
		q.setString(3, extend1edit);
		q.setString(4, extend2edit);
		q.setString(5, extend3edit);
		q.setString(6, cid);
		int ret = q.executeUpdate();
		tx.commit();
		ss.close();
	}
	/**
	 * 号码簿删除分组
	 * @param cid
	 */
		public void deltype(String cid) {
			changetype(cid);
			Session ss = getSessionFactory().openSession();
			Transaction tx = ss.beginTransaction();
			Query q = ss.createQuery("delete from PhonebookgroupMst where groupid=?").setString(0, cid);
			int ret = q.executeUpdate();
			tx.commit();
			ss.close();

		}
		/**
		 * 号码簿删除分组的时候，把号码移到未分组
		 * @param groupid
		 */
		public void changetype(String groupid){
			String groupname=querydbtype(groupid);
			Session ss = getSessionFactory().openSession();
			Transaction tx = ss.beginTransaction();
			Query q = ss.createQuery("update PhonebookMst set groupname='未分组' where groupname=? ").setString(0, groupname);
			int ret = q.executeUpdate();
			tx.commit();
			ss.close();
			
			
		}
		/**
		 * 根据分组的groupid取分组的名称groupname
		 * @param groupid
		 * @return
		 */
		public String querydbtype(String groupid){
			Session ss = getSessionFactory().openSession();
			Query q=ss.createQuery("from PhonebookgroupMst where groupid=?").setString(0, groupid);
			List list=q.list();
			String groupname="";
			for(int i=0;i<list.size();i++){
				PhonebookgroupMst pg=(PhonebookgroupMst)list.get(i);
				groupname=pg.getGroupname();
			}
			ss.close();
			return groupname;
		}
		/**
		 * 号码簿增加号码的时候重复性检查
		 * @param repeatnum
		 * @return
		 */
		public String checkrepeatnum(long repeatnum,String repeattype){
			Session ss = getSessionFactory().openSession();
			Query q=ss.createQuery("from PhonebookMst where mobilenum=? and groupname=?");
			q.setLong(0, repeatnum);
			q.setString(1, repeattype);
			List list=q.list();
			int nums=list.size();
			ss.close();
			if(nums==0){
				return "";
			}else{
				return "此号码已经存在";
			}	
			
		}
		/**
		 * 号码簿增加号码的时候重复性检查
		 * @param repeatnum
		 * @return
		 */
		public String checkrepeatnum2(String cid,long repeatnum,String repeattype){
			Session ss = getSessionFactory().openSession();
			Query q=ss.createQuery("from PhonebookMst where mobilenum=? and groupname=? and cid<>?");
			q.setLong(0, repeatnum);
			q.setString(1, repeattype);
			q.setString(2, cid);
			List list=q.list();
			int nums=list.size();
			ss.close();
			if(nums==0){
				return "";
			}else{
				return "此号码已经存在";
			}		
		}
		/**
		 * 分组的重复性检查
		 * @param repeattype
		 * @return
		 */
	public String checkrepeatnum(String repeattype){
		Session ss=getSessionFactory().openSession();
		Query q=ss.createQuery("from PhonebookgroupMst where groupname=?");
		q.setString(0, repeattype);
		List list=q.list();
		int nums=list.size();
		ss.close();
		if(nums==0){
			return "";
		}else{
			return "此分组已经存在";
		}
	}	
	/**
	 * 修改分组的时候的重复性检查
	 * @param repeattype
	 * @return
	 */
public String checkrepeatnum2(String groupid,String repeattype){
	Session ss=getSessionFactory().openSession();
	Query q=ss.createQuery("from PhonebookgroupMst where groupname=? and groupid<>?");
	q.setString(0, repeattype);
	q.setString(1, groupid);
	List list=q.list();
	int nums=list.size();
	ss.close();
	if(nums==0){
		return "";
	}else{
		return "此分组已经存在";
	}
}	
	public void addtypeedit(String groupid,String groupname){
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		Query q = ss.createQuery("update PhonebookgroupMst set groupname=? where groupid=? ");
		q.setString(0, groupname);
		q.setString(1, groupid);
		int ret = q.executeUpdate();
		tx.commit();
		ss.close();
	}
		
	public List<HashMap> listCustomer(String owner,String userid,int pageSize, int pageNow,String zqdm,String gsmc){
		StringBuffer sb = new StringBuffer("select customer.customerno,rownum from (select distinct customerno from (select customerno from bd_zqb_pj_base a "
	+ "where owner =? or manager = ?"
	+ "union all select c.customerno from (select projectno from BD_PM_TASK "
	+ "where manager = ?) b inner join bd_zqb_pj_base c "
	+ "on b.projectno = c.projectno union all select KHBH from BD_MDM_KHQXGLB "
	+ "where KHFZR =? or ZZCXDD =? or FHSPR = ? or ZZSPR =? or GGFBR = ? or CWSCBFZR2 =? or CWSCBFZR3 = ?"
	+ " union all select distinct customerno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id "
	+ " ) c inner join (select a.instanceid,dataid,b.name,b.userid  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
	+ " left join BD_ZQB_GROUP b on a.dataid = b.id ) d on c.instanceid = d.instanceid inner join bd_zqb_pj_base e on c.projectno = e.projectno"
	+ " where d.userid = ?) d order by customerno) customer ");
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
		if((zqdm!=null&&!"".equals(zqdm))||(gsmc!=null&&!"".equals(gsmc))){
			sb.append(" inner join bd_zqb_kh_base base on customer.customerno=base.customerno ");
			if(zqdm!=null&&!zqdm.equals("")){
				if(d.HasInjectionData(zqdm)){
    				return lis;
    			}
				sb.append(" AND base.ZQDM like ?");
			}
			if(gsmc!=null&&!gsmc.equals("")){
				if(d.HasInjectionData(gsmc)){
    				return lis;
    			}
				sb.append(" AND base.CUSTOMERNAME like ?");
			}
		}
		if(owner!=null&&!owner.equals("")){
			if(d.HasInjectionData(owner)){
				return lis;
			}
			
		}
		if(userid!=null&&!userid.equals("")){
			if(d.HasInjectionData(userid)){
				return lis;
			}
			
		}
		final String final_owner = owner;
		final String final_userid = userid;
		final String final_zqdm = zqdm;
		final String final_gsmc = gsmc;
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int i=0;
				for (int j = 0; j < 10; j++) {
					query.setString(i, final_owner);i++;
				}
				query.setString(i, final_userid);i++;
				if((final_zqdm!=null&&!"".equals(final_zqdm))||(final_gsmc!=null&&!"".equals(final_gsmc))){
					if(final_zqdm!=null&&!final_zqdm.equals("")){
						query.setString(i, "%"+final_zqdm+"%");i++;
					}
					if(final_gsmc!=null&&!final_gsmc.equals("")){
						query.setString(i, "%"+final_gsmc+"%");i++;
					}
				}
				
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<HashMap> l = new ArrayList<HashMap>();
				HashMap map;
				List<Object[]> list2 = query.list();
				for (Object[] object : list2) {
					map = new HashMap();
					String customerno = (String) object[0];
					if(customerno!=null){
						map.put("CUSTOMERNO", customerno);
						l.add(map);
					}
				}
				return l;
			}
		});
	}
	
	public List<HashMap> listCustomer(String owner,String userid){
		StringBuffer sb = new StringBuffer("select customerno,rownum from (select distinct customerno from (select customerno from bd_zqb_pj_base a "
				+ "where owner =? or manager = ?"
				+ "union all select c.customerno from (select projectno from BD_PM_TASK "
				+ "where manager = ?) b inner join bd_zqb_pj_base c "
				+ "on b.projectno = c.projectno union all select KHBH from BD_MDM_KHQXGLB "
				+ "where KHFZR =? or ZZCXDD =? or FHSPR = ? or ZZSPR =? or GGFBR = ? or CWSCBFZR2 =? or CWSCBFZR3 = ?"
				+ " union all select distinct customerno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id "
				+ " ) c inner join (select a.instanceid,dataid,b.name,b.userid  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id ) d on c.instanceid = d.instanceid inner join bd_zqb_pj_base e on c.projectno = e.projectno"
				+ " where userid = ?) d order by customerno)");
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
	    if(owner!=null&&!owner.equals("")){
			if(d.HasInjectionData(owner)){
				return lis;
			}
			
		}
		if(userid!=null&&!userid.equals("")){
			if(d.HasInjectionData(userid)){
				return lis;
			}
			
		}
		final String sql1 = sb.toString();
		final String final_owner = owner;
		final String final_userid = userid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int i=0;
				for (int j = 0; j < 10; j++) {
					query.setString(i, final_owner);i++;
				}
				query.setString(i, final_userid);i++;
				List<HashMap> l = new ArrayList<HashMap>();
				HashMap map;
				List<Object[]> list2 = query.list();
				for (Object[] object : list2) {
					map = new HashMap();
					String customerno = (String) object[0];
					if(customerno!=null){
						map.put("CUSTOMERNO", customerno);
						l.add(map);
					}
				}
				return l;
			}
		});
	}
	
	public List<HashMap> listCustomerSize(String owner,String userid,String zqdm,String gsmc){
		StringBuffer sb = new StringBuffer("select customer.customerno,rownum from (select distinct customerno from (select customerno from bd_zqb_pj_base a "
				+ "where owner =? or manager = ?"
				+ "union all select c.customerno from (select projectno from BD_PM_TASK "
				+ "where manager = ?) b inner join bd_zqb_pj_base c "
				+ "on b.projectno = c.projectno union all select KHBH from BD_MDM_KHQXGLB "
				+ "where KHFZR =? or ZZCXDD =? or FHSPR = ? or ZZSPR =? or GGFBR = ? or CWSCBFZR2 =? or CWSCBFZR3 = ?"
				+ " union all select distinct customerno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id "
				+ " ) c inner join (select a.instanceid,dataid,b.name,b.userid  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id ) d on c.instanceid = d.instanceid inner join bd_zqb_pj_base e on c.projectno = e.projectno"
				+ " where d.userid = ?) d order by customerno) customer ");
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
		if((zqdm!=null&&!"".equals(zqdm))||(gsmc!=null&&!"".equals(gsmc))){
			sb.append(" inner join bd_zqb_kh_base base on customer.customerno=base.customerno ");
			if(zqdm!=null&&!zqdm.equals("")){
				if(d.HasInjectionData(zqdm)){
					return lis;
				}
				sb.append(" AND base.ZQDM like ?");
			}
			if(gsmc!=null&&!gsmc.equals("")){
				if(d.HasInjectionData(gsmc)){
					return lis;
				}
				sb.append(" AND base.CUSTOMERNAME like ?");
			}
		}
		if(owner!=null&&!owner.equals("")){
			if(d.HasInjectionData(owner)){
				return lis;
			}
			
		}
		if(userid!=null&&!userid.equals("")){
			if(d.HasInjectionData(userid)){
				return lis;
			}
			
		}
		final String sql1 = sb.toString();
		final String final_owner = owner;
		final String final_userid = userid;
		final String final_zqdm = zqdm;
		final String final_gsmc = gsmc;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int i=0;
				for (int j = 0; j < 10; j++) {
					query.setString(i, final_owner);i++;
				}
				query.setString(i, final_userid);i++;
				if((final_zqdm!=null&&!"".equals(final_zqdm))||(final_gsmc!=null&&!"".equals(final_gsmc))){
					if(final_zqdm!=null&&!final_zqdm.equals("")){
						query.setString(i, "%"+final_zqdm+"%");i++;
					}
					if(final_gsmc!=null&&!final_gsmc.equals("")){
						query.setString(i, "%"+final_gsmc+"%");i++;
					}
				}
				List<HashMap> l = new ArrayList<HashMap>();
				HashMap map;
				List<Object[]> list2 = query.list();
				for (Object[] object : list2) {
					map = new HashMap();
					String customerno = (String) object[0];
					if(customerno!=null){
						map.put("CUSTOMERNO", customerno);
						l.add(map);
					}
				}
				return l;
			}
		});
	}
	
	public List listSize(String sql,String zqdm,String gsmc){
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.CREATEUSER,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.ZQDM FROM BD_ZQB_KH_BASE  BOTABLE WHERE 1=1");
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
		if(sql!=null&&!sql.equals("")){
			if(d.HasInjectionData(sql)){
				return lis;
			}
			sb.append(" AND ?");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			if(d.HasInjectionData(zqdm)){
				return lis;
			}
			sb.append(" AND ZQDM like ?");
		}
		if(gsmc!=null&&!gsmc.equals("")){
			if(d.HasInjectionData(gsmc)){
				return lis;
			}
			sb.append(" AND CUSTOMERNAME like ?");
		}
		sb.append(" and (tel is not null)");
		sb.append(" ORDER BY BOTABLE.ID desc");
		/*StringBuffer sb = new StringBuffer("SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.TYPE,BOTABLE.ZYYW,BOTABLE.ZYCP,BOTABLE.JZYS,BOTABLE.NDYS,BOTABLE.JLR,BOTABLE.SLQK,BOTABLE.TEL,BOTABLE.EMAIL,BOTABLE.CZJGQBD,BOTABLE.GPSJ,BOTABLE.GFGSRQ,BOTABLE.YXGSRQ,BOTABLE.GDDH,BOTABLE.SHTZFS,BOTABLE.DSHSPQX,BOTABLE.MEMO,BOTABLE.CUSTOMERDESC,BOTABLE.ZQDM,BOTABLE.ZCDZ,BOTABLE.ZQJC,BOTABLE.SSHY,BOTABLE.ZWMC,BOTABLE.YWMC,BOTABLE.ZCZB,BOTABLE.JYXKXM,BOTABLE.GSWZ,BOTABLE.FDDBR,BOTABLE.PHONE,BOTABLE.FAX,BOTABLE.YGP,BOTABLE.XGZL,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_ZQB_KH_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND CUSTOMERNAME like '%"+customername+"%'");
		}
		if(customerno!=null&&!customerno.equals("")){
			sb.append(" AND CUSTOMERNO like '%"+customerno+"%'");
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS='"+status+"'");
		}*/
		final String sql1 = sb.toString();
		final String final_sql = sql;
		final String final_zqdm = zqdm;
		final String final_gsmc = gsmc;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				int i = 0;
				if(final_sql!=null&&!final_sql.equals("")){
					
				}
				if(final_zqdm!=null&&!final_zqdm.equals("")){
					query.setString(i, "%"+final_zqdm+"%");i++;
				}
				if(final_gsmc!=null&&!final_gsmc.equals("")){
					query.setString(i, "%"+final_gsmc+"%");i++;
				}
				// Query query = session.createQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					//CREATEUSER,CUSTOMERNAME,CUSTOMERNO,USERNAME,ZQDM
					String createuser = (String) object[0];
					String customername = (String) object[1];
					String customerno = (String) object[2];
					String username = (String) object[3];
					String zqdm = (String) object[4];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERNAME", username);
					map.put("ZQDM", zqdm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public HashMap getlist(int startRow,int endRow,String zqdm,String gsmc,String leibie,String userid,int roleId,String customerno) {
		HashMap data=new HashMap();
		StringBuffer query = new StringBuffer("CALL PHONEBOOK.GETPHONEBOOKLIST(?,?,?,?,?,?,?,?,?,?)");
		Connection conn = null;
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString());
			cstmt.setString(1,zqdm);
			cstmt.setString(2,gsmc);
			cstmt.setString(3,leibie);
			cstmt.setInt(4,roleId);
			cstmt.setString(5,userid);
			cstmt.setString(6,customerno);
			cstmt.setInt(7,startRow);
			cstmt.setInt(8,endRow);
			cstmt.registerOutParameter(9,OracleTypes.NUMBER);
			cstmt.registerOutParameter(10,OracleTypes.CURSOR);
			cstmt.execute();
			ResultSet rs = (ResultSet) cstmt.getObject(10);
			List<HashMap> list = new ArrayList<HashMap>();
			while (rs.next()) {
				HashMap map = new HashMap();
				Object customername = rs.getObject("CUSTOMERNAME");
				Object username = rs.getObject("USERNAME");
				Object mobile = rs.getObject("MOBILE");
				Object email = rs.getObject("EMAIL");
				Object rolename = rs.getObject("ROLENAME");
				Object zq_dm = rs.getObject("ZQDM");
				Object count = rs.getObject("COUNT");
				Object extend1 = rs.getObject("EXTEND1");
				Object extend2 = rs.getObject("EXTEND2");
				Object extend3 = rs.getObject("EXTEND3");
				map.put("CUSTOMERNAME", customername==null?"":customername);
				map.put("USERNAME", username==null?"":username);
				map.put("MOBILE", mobile==null?"":mobile);
				map.put("EMAIL", email==null?"":email);
				map.put("ROLENAME", rolename==null?"":rolename);
				map.put("ZQDM", zq_dm==null?"":zq_dm);
				map.put("COUNT", count);
				map.put("EXTEND1", extend1);
				map.put("EXTEND2", extend2);
				map.put("EXTEND3", extend3);
				list.add(map);
			}
			data.put("LIST", list);
			BigDecimal totalnum = (BigDecimal)cstmt.getObject(9);
			data.put("TOTALNUM", totalnum.intValue());
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, null, null);
		}
		return data;
	}
	public List<HashMap> getExpList(String sql, List<HashMap<String, Object>> parameter) {
		List<HashMap> dataList=new ArrayList<HashMap>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				HashMap<String,Object> hashMap = parameter.get(i);
				if(hashMap.get("type").equals("string")){
					ps.setString(index++, hashMap.get("value").toString());
				}else if(hashMap.get("type").equals("int")){
					ps.setInt(index++, Integer.parseInt(hashMap.get("value").toString()));
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String customername = rs.getString("CUSTOMERNAME");
				String username = rs.getString("USERNAME");
				String mobile = rs.getString("MOBILE");
				String email = rs.getString("EMAIL");
				String rolename = rs.getString("ROLENAME");
				String zqdm = rs.getString("ZQDM");
				result.put("CUSTOMERNAME", customername==null?"":customername);
				result.put("USERNAME", username==null?"":username);
				result.put("MOBILE", mobile==null?"":mobile);
				result.put("EMAIL", email==null?"":email);
				result.put("ROLENAME", rolename==null?"":rolename);
				result.put("ZQDM", zqdm==null?"":zqdm);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap> getPnoneBookList(String userid){
		List<HashMap> datalist=new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String,String> data = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement("SELECT NAME,TEL,TITLE,EMAIL,COMPANY,ZB FROM BD_GE_PHONEBOOK WHERE USERID=?");
			ps.setString(1, userid);
			rs = ps.executeQuery();
			while(rs.next()){
				data = new HashMap<String, String>();
				data.put("NAME",rs.getString(1));
				data.put("TEL",rs.getString(2));
				data.put("TITLE",rs.getString(3));
				data.put("EMAIL",rs.getString(4));
				data.put("COMPANY",rs.getString(5));
				data.put("ZB",rs.getString(6));
				datalist.add(data);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return datalist;
	}
}
