package com.iwork.plugs.sms.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.sms.bean.ConfigMst;
import com.iwork.plugs.sms.bean.MsgMst;
import com.iwork.plugs.sms.bean.PhonebookMst;
import com.iwork.plugs.sms.bean.TempMst;
import com.iwork.plugs.sms.service.MsgService;
import com.iwork.plugs.sms.util.MsgConst;

public class MsgDao extends HibernateDaoSupport {
	private static Logger logger = Logger.getLogger(MsgDao.class);
	MsgService msgService = new MsgService();

	/**
	 * 得到短信单价
	 * 
	 * @return
	 */
	public String getunitp() {
		String unitp = "";
		String sql = "select price from ConfigMst where type='DEFAULT_CHANNEL' and key='0' ";// and
																								// ConfigMst.value=SpMst.spid";
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery(sql);
			List list = q.list();
			for (int i = 0; i < list.size(); i++) {
				unitp = (String) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return unitp;
	}

	// 发送前过滤词检查
	public String filter(String content) {
		String ret = "";
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery("from ConfigMst where type=?");
			q.setString(0, MsgConst.TYPE_FILTER_WORD);
			ArrayList list = (ArrayList) q.list();
			int sice = list.size();
			for (int i = 0; i < list.size(); i++) {
				ConfigMst cfm = (ConfigMst) list.get(i);
				String filterword = cfm.getValue();
				if (content.indexOf(filterword) != -1) {
					if (ret.length() == 0)
						ret += filterword;
					else
						ret += "、 " + filterword;
				}
			}

		} catch (Exception e) {
			logger.error(e,e);
			// return "数据库错误: " + e.getLocalizedMessage() + ";\n";
		} finally {
			ss.close();
		}
		return ret;
	}

	// 短信加入到临时库
	public void addtemp(TempMst tm) {
		// this.getHibernateTemplate().save(tm);
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		ss.save(tm);
		tx.commit();
		ss.close();
	}

	// 通配符取号码簿的属性
	public Hashtable attrdb(String mobilenum) {
		Hashtable h = new Hashtable();
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery("from PhonebookMst where mobilenum=?");
			q.setString(0, mobilenum);
			ArrayList list = (ArrayList) q.list();
			for (int i = 0; i < list.size(); i++) {
				PhonebookMst pm = (PhonebookMst) list.get(i);
				String attr1 = pm.getAttr1() == null ? "" : pm.getAttr1();
				String attr2 = pm.getAttr2() == null ? "" : pm.getAttr2();
				String attr3 = pm.getAttr3() == null ? "" : pm.getAttr3();
				h.put("ATTR1", attr1);
				h.put("ATTR2", attr2);
				h.put("ATTR3", attr3);
			}

		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return h;
	}

	/**
	 * 查询号段等
	 * 
	 * @param sql
	 * @return
	 */
	public String qnumsection(String sql,String param) {
		Session ss = getSessionFactory().openSession();
		String rv = "";
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, param);
			List list = q.list();
			for (int i = 0; i < list.size(); i++) {
				rv = (String) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return rv;
	}

	/**
	 * 查询用户通道
	 * 
	 * @param sql
	 * @return
	 */
	public int qchannel(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		int rv = 0;
		try {
			Query q = ss.createQuery(sql);
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
			List list = q.list();
			for (int i = 0; i < list.size(); i++) {
				rv = (Integer) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return rv;
	}

	/**
	 * 查询用户通道
	 * 
	 * @param sql
	 * @return
	 */
	public String qchannel2(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		String rv = "";
		try {
			Query q = ss.createQuery(sql);
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
			List list = q.list();
			for (int i = 0; i < list.size(); i++) {
				rv = (String) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return rv;
	}

	/**
	 * 获取一批短信的号码temp表
	 * 
	 * @param sql
	 * @return
	 */
	public List qnums(String sql,String batchnum) {
		Session ss = getSessionFactory().openSession();
		ArrayList list1 = new ArrayList();
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, batchnum);
			ArrayList list = (ArrayList) q.list();
			int a = list.size();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = new Hashtable();
				TempMst tm = (TempMst) list.get(i);
				String mobile = tm.getMobilenum();
				ht.put("MOBILE", mobile);
				list1.add(ht);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return list1;
	}

	/**
	 * 获取一批短信的号码msg表
	 * 
	 * @param sql
	 * @return
	 */
	public List qnums1(String sql,String batchnum) {
		Session ss = getSessionFactory().openSession();
		ArrayList list1 = new ArrayList();
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, batchnum);
			ArrayList list = (ArrayList) q.list();
			int a = list.size();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = new Hashtable();
				MsgMst tm = (MsgMst) list.get(i);
				String mobile = tm.getMobilenum();
				ht.put("MOBILE", mobile);
				list1.add(ht);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return list1;
	}

	/**
	 * 获取一批短信的temp表以便插入到msgMst表中
	 * 
	 * @param sql
	 *            select
	 *            userid,batchnum,mobilenum,content,status,channelid,msgsp
	 *            ,sign,billinglen,price,submittime,pathname,msgcount from
	 *            TempMst "
	 * @return
	 */
	public List getTemp(String sql,String batchnum) {
		Session ss = getSessionFactory().openSession();
		ArrayList list1 = new ArrayList();
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, batchnum);
			ArrayList list = (ArrayList) q.list();
			int a = list.size();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = new Hashtable();
				TempMst tm = (TempMst) list.get(i);
				String userid = tm.getUserid() == null ? "" : tm.getUserid();
				String mobile = tm.getMobilenum();
				String content = tm.getContent();
				String status = String.valueOf(tm.getStatus());
				String chanelid = String.valueOf(tm.getChannelid());
				String msgsp = String.valueOf(tm.getMsgsp());
				String sign = tm.getSign();
				String billinglen = String.valueOf(tm.getBillinglen());
				String price = Double.toString(tm.getPrice());
				String submittime = tm.getSubmittime().toString();
				String msgcount = String.valueOf(tm.getMsgcount());
				ht.put("userid", userid);
				ht.put("content", content);
				ht.put("status", status);
				ht.put("chanelid", chanelid);
				ht.put("msgsp", msgsp);
				ht.put("sign", sign);
				ht.put("billinglen", billinglen);
				ht.put("price", price);
				ht.put("submittime", submittime);
				ht.put("msgcount", msgcount);
				ht.put("mobile", mobile);
				list1.add(ht);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return list1;
	}

	/**
	 * 写入msgmst
	 * 
	 * @param lm
	 */
	public void save(MsgMst lm) {
		this.getHibernateTemplate().save(lm);

	}

	/**
	 * 查询count(msgcount)
	 * 
	 * @param sql
	 * @return
	 */
	public long qcount(String sql,String batchnum) {
		Session ss = getSessionFactory().openSession();
		long rv = 0;
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, batchnum);
			List list = q.list();
			for (int i = 0; i < list.size(); i++) {
				rv = (Long) list.get(i);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return rv;
	}

	/**
	 * 短信 写入通道
	 * 
	 * @param sql
	 */
	public void doupdate1(String sql,List<Object> params) {
		Session ss = getSessionFactory().openSession();
		Transaction tx = ss.beginTransaction();
		Query qq = ss.createQuery(sql);
		for (int i = 0; i < params.size(); i++) {
			qq.setParameter(i, params.get(i));
		}
		int ret = qq.executeUpdate();
		tx.commit();
		ss.close();
	}

	public Hashtable qmsg1(String sql,String batchnum,String mobileNum) {
		Hashtable ht = new Hashtable();
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery(sql);
			q.setString(0, batchnum);
			q.setString(1, mobileNum);
			ArrayList list = (ArrayList) q.list();
			for (int i = 0; i < list.size(); i++) {
				MsgMst mm = (MsgMst) list.get(i);
				String content = mm.getContent() == null ? "" : mm.getContent();
				String price = Double.toString(mm.getPrice());
				String billinglen = String.valueOf(mm.getBillinglen());
				ht.put("content", content);
				ht.put("price", price);
				ht.put("billinglen", billinglen);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return ht;
	}

	/**
	 * 
	 * 号码簿选号 从数据库取号码簿分组
	 * 
	 * @return
	 */
	public List getGroupList() {
		ArrayList list = new ArrayList();
		String html = "";
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery("from PhonebookgroupMst");
			list = (ArrayList) q.list();
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return list;
	}

	/**
	 * 获得分组内联系人列表
	 * 
	 * @param groupname
	 * @param userid
	 * @return
	 */
	public List<PhonebookMst> getGroupForPhoneList(String groupname,
			String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("from PhonebookMst where  groupname = ? order by groupname,name");
		Object[] value = {groupname};
		List list = this.getHibernateTemplate().find(sql.toString(),value);
		return list;
	}

	/**
	 * 根据所选cid查询出姓名手机号（号码簿选号）
	 * 
	 * @return
	 */
	public ArrayList getSelectnum(String sql,List params) {
		ArrayList list = new ArrayList();
		Session ss = getSessionFactory().openSession();
		try {
			Query q = ss.createQuery(sql);
			for (int i = 0; i < params.size(); i++) {
				q.setParameter(i, params.get(i));
			}
			ArrayList list1 = (ArrayList) q.list();
			for (int i = 0; i < list1.size(); i++) {
				Hashtable ht = new Hashtable();
				PhonebookMst pb = (PhonebookMst) list1.get(i);
				String name = pb.getName() == null ? "" : pb.getName();
				String mobile = String.valueOf(pb.getMobilenum());
				ht.put("name", name);
				ht.put("mobile", mobile);
				list.add(ht);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			ss.close();
		}
		return list;
	}

	public List getDept(Long companyid,Long deptId) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long orgRoleId = uc.get_userModel().getOrgroleid();
		String userid = uc.get_userModel().getUserid();
		boolean flag = false;
		List<String> roleList = new ArrayList<String>();
		List<OrgUserMap> orgUserMapList = uc.get_userMapList();
		for (OrgUserMap orgUserMap : orgUserMapList) {
			roleList.add(orgUserMap.getOrgroleid());
		}
		String usernames=userid+"["+uc.get_userModel().getUsername()+"]";
		if(roleList.contains(ZQBRoleConstants.ISPURVIEW_ROLE_ID_DUDAO)&&!roleList.contains(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG)){//非场外角色兼职督导角色
			flag=true;
		}
		List lables = new ArrayList();
		lables.add("DEPTID");
		List<HashMap> lists = DBUTilNew.getDataList(lables, "select distinct(t.id) DEPTID,t.departmentname from ORGDEPARTMENT t left join ORGUSER s on s.departmentid=t.id where s.mobile is not null AND  s.USERSTATE=0  AND SYSDATE<s.ENDDATE  and t.companyid=2 start with t.id='51' connect by prior t.id=t.PARENTDEPARTMENTID ", null);
		List dataList=new ArrayList();
		StringBuffer sqls=new StringBuffer();
		sqls.append(" ");
		String sql = "";
		String sql1=" select distinct(t.id) DEPTID,t.departmentname DEPARTMENTNAME from ORGDEPARTMENT t left join ORGUSER s on s.departmentid=t.id where s.mobile is not null AND  s.USERSTATE=0  AND SYSDATE<s.ENDDATE and t.id!= ? and t.companyid= ?   start with t.id= ?  connect by prior t.id=t.PARENTDEPARTMENTID  ";
		
		if(orgRoleId==5l){
			sql=sql1;
		}else if(orgRoleId==4l||flag){
			if(deptId!=51l){
				sql=sql1;
			}else{
				sql="  select distinct(t.id) DEPTID,t.departmentname from ORGDEPARTMENT t left join ORGUSER s on s.departmentid=t.id left join bd_mdm_khqxglb z on z.khbh=s.extend1 where s.mobile is not null AND  s.USERSTATE=0  AND SYSDATE<s.ENDDATE  and t.id!= ?  and t.companyid= ?  "
						+ "  and (z.khfzr= ?  or z.zzcxdd = ? or z.fhspr= ? or z.zzspr = ? or z.ggfbr= ? or z.cwscbfzr2 = ? or z.cwscbfzr3 = ? or z.qynbrysh = ? ) "
						+ "    start with t.id=?  connect by prior t.id=t.PARENTDEPARTMENTID  ";
			}
				
		}else{
			if(deptId!=51l){
				sql=sql1;
			}
		}
		if(orgRoleId==3l){
			sql="";
		}
		if("".equals(sql)){
			return dataList;
		}
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			
			
			if(orgRoleId==5l){
				ps.setLong(1, deptId);
				ps.setLong(2, companyid);
				ps.setLong(3, deptId);
			}else if(orgRoleId==4l||flag){
				if(deptId!=51l){
					ps.setLong(1, deptId);
					ps.setLong(2, companyid);
					ps.setLong(3, deptId);
				}else{
					ps.setLong(1, deptId);
					ps.setLong(2, companyid);
					ps.setString(3, usernames);
					ps.setString(4, usernames);
					ps.setString(5, usernames);
					ps.setString(6, usernames);
					ps.setString(7, usernames);
					ps.setString(8, usernames);
					ps.setString(9, usernames);
					ps.setString(10, usernames);
					ps.setLong(11, deptId);
				}
				
			}else{
				if(deptId!=51l){
					ps.setLong(1, deptId);
					ps.setLong(2, companyid);
					ps.setLong(3, deptId);
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("DEPTID");
				String departmentname = rs.getString("DEPARTMENTNAME");
				result.put("DEPTID", id);
				result.put("DEPTNAME", departmentname);
				dataList.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap> getallphone(String userid) {
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ID,ZB FROM BD_GE_PHONEGROUP G");
		if(userid!=null&&!userid.equals("")){
			if(d.HasInjectionData(userid)){
				return lis;
			}
			sb.append(" WHERE G.CREATEUSER=?");
		}
		final String sql1 = sb.toString();
		final String userid_ = userid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				
				int i=0;
				if(userid_!=null&&!userid_.equals("")){
		        	
					query.setString(i, userid_);i++;
				}
				
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String name = (String) object[1];
					map.put("id", id);
					map.put("name", name);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getChildren(String userid,String zb) {
		StringBuffer sb = new StringBuffer();
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
		sb.append("SELECT ID,NAME,TEL,TITLE,EMAIL,COMPANY,USERID,ZB FROM (");
		sb.append(" SELECT ID,NAME,TEL,TITLE,EMAIL,COMPANY,USERID,REGEXP_SUBSTR(ZB, '[^,]+', 1, RN) ZB FROM (SELECT ID,NAME,TEL,TITLE,EMAIL,COMPANY,USERID,ZB FROM BD_GE_PHONEBOOK WHERE 1=1");
		if(userid!=null&&!userid.equals("")){
			if(d.HasInjectionData(userid)){
				return lis;
			}
			sb.append(" AND USERID=?");
		}
		sb.append("),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(ZB, '[^,]+', 1, RN) IS NOT NULL) WHERE 1=1");
		if(zb!=null&&!zb.equals("")){
			if(d.HasInjectionData(zb)){
				return lis;
			}
			sb.append(" AND ZB=?");
		}
		final String sql1 = sb.toString();
		final String userid_ = userid;
		final String zb_ = zb;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				
				int i=0;
				if(userid_!=null&&!userid_.equals("")){
					
					query.setString(i, userid_);i++;
				}
				if(zb_!=null&&!zb_.equals("")){
					
					query.setString(i, zb_);i++;
				}
				
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String name = (String) object[1];
					String value = (String) object[2]+"["+(String) object[1]+"];";
					map.put("id", id);
					map.put("value", value);
					map.put("name", name);
					map.put("icon","iwork_img/user.png");
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getGroupDepartmentid(String id) {
		String[] ids = null;
		if(id!=null&&!id.equals("")){
			if(id.endsWith(",")){
				id = id.substring(0, id.length()-1);
				ids = id.split(",");
			}else{
				ids = id.split(",");
			}
		}
		StringBuffer idstr = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT D.ID,D.DEPARTMENTNAME,D.COMPANYID,D.PARENTDEPARTMENTID,D.DEPARTMENTDESC,D.DEPARTMENTNO,D.LAYER,D.ORDERINDEX,D.ZONENO,D.ZONENAME,D.EXTEND1,D.EXTEND2,D.EXTEND3,D.EXTEND4,D.EXTEND5,D.DEPARTMENTSTATE FROM ORGDEPARTMENT D WHERE 1=1");
		int size = 0;
		if(id!=null&&!id.equals("")){
			sb.append(" AND D.PARENTDEPARTMENTID IN(");
			size = ids.length;
			for (int i = 0; i < size; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
		}
		final String sql1 = sb.toString();
		final String id_ = id;
		final String[] ids_ = ids;
		final int size_ = size;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
			    List lis=new ArrayList();
				if(id_!=null&&!id_.equals("")){
					for (int i = 0; i < size_; i++) {
						if ((ids_[i] != null) && (!"".equals(ids_[i]))) {
				        	if(d.HasInjectionData(ids_[i])){
			    				return lis;
			    			}
				          
				        }
						query.setInteger(i, Integer.parseInt(ids_[i]));
					}
				}
				
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					BigDecimal parentdepartmentid = (BigDecimal) object[3];
					map.put("ID", id);
					map.put("PARENTDEPARTMENTID", parentdepartmentid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getGroupData(String id,String id2) {
		String[] ids = null;
		StringBuffer idstr = new StringBuffer();
		if(id!=null&&!id.equals("")){
			if(id.endsWith(",")){
				id = id.substring(0, id.length()-1);
				ids = id.split(",");
			}else{
				ids = id.split(",");
			}
		}
		
		
		String[] id2s = null;
		StringBuffer id2str = new StringBuffer();
		if(id2!=null&&!id2.equals("")){
			if(id2.endsWith(",")){
				id2 = id2.substring(0, id2.length()-1);
				id2s = id2.split(",");
			}else{
				id2s = id2.split(",");
			}
		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ORG.ID,ORG.USERID,ORG.USERNAME,ORG.MOBILE FROM ORGUSER ORG WHERE 1=1 AND ORG.MOBILE IS NOT NULL AND USERSTATE=0 AND  SYSDATE<ENDDATE AND ORG.USERID!='NEEQMANAGER'");
		
		
		int size = 0;
		if(id!=null&&!id.equals("")){
			sb.append(" AND ORG.DEPARTMENTID IN(");
			size = ids.length;
			for (int i = 0; i < size; i++) {
				if(i==(ids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
		}
		
		
		int size2 = 0;
		if(id2!=null&&!id2.equals("")){
			sb.append(" AND ORG.ID IN(");
			size2 = id2s.length;
			for (int i = 0; i < size2; i++) {
				if(i==(id2s.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
			}
			sb.append(")");
		}
		
		
		final String sql1 = sb.toString();
		final String id_ = id;
		final String id2_ = id2;
		final String[] ids_ = ids;
		final String[] id2s_ = id2s;
		final int size_ = size;
		final int size2_ = size2;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				
				int i=0;
				if(id_!=null&&!id_.equals("")){
					for (int j = 0; j < size_; j++) {
						
						query.setInteger(i, Integer.parseInt(ids_[j]));i++;
					}
				}
				if(id2_!=null&&!id2_.equals("")){
					for (int j = 0; j < size2_; j++) {
						query.setInteger(i, Integer.parseInt(id2s_[j]));i++;
					}
				}
				
				
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String userid = (String) object[1];
					String username = (String) object[2];
					String mobile = (String) object[3];
					map.put("ID", id);
					map.put("USERID", userid);
					map.put("USERNAME", username);
					map.put("MOBILE", mobile);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getAllChildren(String userid) {
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ID,NAME,TEL,TITLE,EMAIL,COMPANY,USERID,ZB FROM BD_GE_PHONEBOOK");
		if(userid!=null&&!userid.equals("")){
			if(d.HasInjectionData(userid)){
				return lis;
			}
			sb.append(" WHERE USERID=?");
		}
		final String sql1 = sb.toString();
		final String userid_ = userid;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				if(userid_!=null&&!userid_.equals("")){
					query.setString(0, userid_);
				}
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String name = (String) object[1];
					String value = (String) object[2]+"["+(String) object[1]+"];";
					map.put("id", id);
					map.put("value", value);
					map.put("name", name);
					map.put("icon","iwork_img/user.png");
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<String> getphoneNum(String content, String[] phoneNums) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MOBILENUM FROM IWORK_SMS_MSG WHERE MOBILENUM IN(");
		//'18913172187','18310775673','13739179135','13739179135','13739179135','13739179135'
		//'苏州东奇信息科技股份有限公司的公告：董事、监事、高级管理人员换届公告，已审批通过' order by cid desc; 
		for (int i = 0; i < phoneNums.length; i++) {
			if(i==(phoneNums.length-1)){
				sql.append("?");
			}else{
				sql.append("?,");
			}
			params.add(phoneNums[i].indexOf("[")>0&&phoneNums[i].indexOf("]")>0?phoneNums[i].substring(0,phoneNums[i].indexOf("[")):phoneNums[i]);
		}
		sql.append(") AND CONTENT = ?");
		params.add(content);
		sql.append(" AND CEIL((SYSDATE - SENDTIME) * 24 * 60 * 60) < 300");
		
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				String mobilenum = rs.getString("MOBILENUM");
				list.add(mobilenum);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
}
