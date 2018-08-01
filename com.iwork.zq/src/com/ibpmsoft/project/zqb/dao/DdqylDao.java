package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class DdqylDao extends HibernateDaoSupport{
	private static Logger logger = Logger.getLogger(DdqylDao.class);
	
	/**
	 * 入账记录数
	 * @param id
	 * @return
	 */
	public List<HashMap> getrzListSize(String id) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("     select s.id,s.lrr,s.xylx,s.dzje,s.dzrq, BINDTABLE.Instanceid from BD_ZQB_CWRZB s inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid = s.id   ");
		sql.append("    and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 235 where to_char(s.departmentid)=? order by s.dzrq desc  ");
		parameter.add(id);
		List<HashMap> dataList=new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int j=0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> map = new HashMap<String,Object>();
				String sxmcs = rs.getString("id");
				map.put("ycl", sxmcs);
				dataList.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	/**
	 * 入账列表
	 * @param id
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getrzList(String id, int pageSize, int pageNumber) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		
		sql.append("     select s.id,s.lrr,s.xylx,s.dzje,to_char(s.dzrq,'yyyy-MM-dd hh24:mi'), BINDTABLE.Instanceid from BD_ZQB_CWRZB s inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid = s.id   ");
		sql.append("    and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 235 where to_char(s.departmentid)=? order by s.dzrq desc  ");
		parameter.add(id);
		
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List<String> list = parameter;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i)!=null && !"".equals(list.get(i).toString())){
						String params=list.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, list.get(i));
				}
				List<Object[]> list = query.list();
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				OrgUser user = uc.get_userModel();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal zbid = (BigDecimal) object[0];
					String lrr = (String)object[1];
					String xylx = (String)object[2];
					BigDecimal dzje=(BigDecimal)object[3];
					String dzrq = (String)object[4];
					BigDecimal insId = (BigDecimal)object[5];
					map.put("rzid", zbid.toString());
					map.put("lrr", lrr);
					map.put("xylx", xylx);
					map.put("dzje", dzje.toString());
					map.put("dzrq", dzrq);
					map.put("insId", insId.toString());
					if(user.getUserid().equals(xylx)){
						map.put("flag", 0);
					}else{
						map.put("flag", 1);
					}
					l.add(map);
				}
				return l;
			}
		});
	}
	/**
	 * 取督导列表签约信息
	 * @param gsdm
	 * @param gsjc
	 * @param wgfxnr
	 * @param wgfxlx
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getQyList(String khmc,Date kssj,Date jssj, int pageSize, int pageNumber) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();// 存放参数
		
		sql.append("   select distinct * from (");
		sql.append("   select * from (");
		sql.append("     select * from （select s.id,s.companyname,to_char(s.zcfksj,'yyyy-MM-dd') zcfksj,s.spzt,s.extend1,s.prcid,s.spr,(select sum(z.dzje) from BD_ZQB_CWRZB z where z.departmentid=s.id) dzje,to_char(z.dzrq,'yyyy-MM-dd') dzrq");
		sql.append("  , BINDTABLE.Instanceid,s.companyno,s.extend5 from BD_XP_ZYDDSXB s left join BD_ZQB_CWRZB z on z.departmentid=s.id inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid = s.id   ");
		sql.append("   and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 310 order by s.gxsj desc）  a  ");
		sql.append(" Where Not exists(select 1 from （select s.id,s.companyname,to_char(s.zcfksj,'yyyy-MM-dd') zcfksj,s.spzt,s.extend1,s.prcid,s.spr,(select sum(z.dzje) from BD_ZQB_CWRZB z where z.departmentid=s.id) dzje,to_char(z.dzrq,'yyyy-MM-dd') dzrq ");
		sql.append("     , BINDTABLE.Instanceid,s.companyno,s.extend5 from BD_XP_ZYDDSXB s left join BD_ZQB_CWRZB z on z.departmentid=s.id inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid = s.id  ");
		sql.append("   and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 310 order by s.gxsj desc）  x  ");
		sql.append("   Where x.id=a.id And x.dzrq>a.dzrq   ) ");
		sql.append(" )) y where 1=1");
		if (khmc != null && !khmc.equals("")) {
			sql.append("and y.companyname like ? ");
			parameter.add("%"+khmc+"%");
		}
		if (kssj != null && !kssj.equals("")) {
			sql.append("and  to_date(y.zcfksj,'yyyy-MM-dd') > ? ");
			parameter.add(kssj);
		}
		if (jssj != null && !jssj.equals("")) {
			sql.append("and to_date(y.zcfksj,'yyyy-MM-dd') < ? ");
			parameter.add(jssj);
		}
		
		sql.append(" order by y.dzrq desc,y.zcfksj desc ");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List<String> list = parameter;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i)!=null && !"".equals(list.get(i).toString())){
						String params=list.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, list.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal zbid = (BigDecimal) object[0];
					String khmc = (String)object[1];
					String qyrq = (String)object[2];
					String scyf=(String)object[3];
					String hxsj = (String)object[4];
					String hxyf = (String)object[5];
					String gjrxm = (String)object[6];
					BigDecimal zrzje = (BigDecimal)object[7];
					String zhsj = (String)object[8];
					BigDecimal insId = (BigDecimal)object[9];
					String khbh = (String)object[10];
					String jy = (String)object[11];
					map.put("zbid", zbid.toString());
					map.put("zbkhmc", khmc);
					map.put("qyrq", qyrq);
					map.put("scyf", scyf);
					map.put("hxsj", hxsj);
					map.put("hxyf", hxyf);
					map.put("gjrxm", gjrxm);
					map.put("zrzje", zrzje==null?"暂无":zrzje.toString());
					map.put("zhsj", zhsj==null?"暂无":zhsj);
					map.put("insId", insId.toString());
					map.put("fkjd", "首次应付"+scyf+"万元，后续每年"+hxsj+"日前应收"+hxyf+"万元");
					map.put("khbh", khbh);
					map.put("jy", jy);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**
	 * 督导签约数据条数
	 * @param gsdm
	 * @param gsjc
	 * @param wgfxnr
	 * @param wgfxlx
	 * @return
	 */
	public List<HashMap> getQyListSize(String khmc,String kssj,String jssj) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		int count=0;
		sql.append("   select distinct * from (");
		sql.append("   select * from (");
		sql.append("     select * from （select s.id,s.companyname,to_char(s.zcfksj,'yyyy-MM-dd') zcfksj,s.spzt,s.extend1,s.prcid,s.spr,(select sum(z.dzje) from BD_ZQB_CWRZB z where z.departmentid=s.id) dzje,to_char(z.dzrq,'yyyy-MM-dd') dzrq");
		sql.append("  , BINDTABLE.Instanceid,s.companyno,s.extend5 from BD_XP_ZYDDSXB s left join BD_ZQB_CWRZB z on z.departmentid=s.id inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid = s.id   ");
		sql.append("   and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 310 order by s.gxsj desc）  a  ");
		sql.append(" Where Not exists(select 1 from （select s.id,s.companyname,to_char(s.zcfksj,'yyyy-MM-dd') zcfksj,s.spzt,s.extend1,s.prcid,s.spr,(select sum(z.dzje) from BD_ZQB_CWRZB z where z.departmentid=s.id) dzje,to_char(z.dzrq,'yyyy-MM-dd') dzrq ");
		sql.append("     , BINDTABLE.Instanceid,s.companyno,s.extend5 from BD_XP_ZYDDSXB s left join BD_ZQB_CWRZB z on z.departmentid=s.id inner join SYS_ENGINE_FORM_BIND BINDTABLE on BINDTABLE.Dataid = s.id  ");
		sql.append("   and BINDTABLE.INSTANCEID is not null  and BINDTABLE.formid = 310 order by s.gxsj desc）  x  ");
		sql.append("   Where x.id=a.id And x.dzrq>a.dzrq   ) ");
		sql.append(" )) y where 1=1");
		if (khmc != null && !khmc.equals("")) {
			sql.append("and y.companyname like ? ");
			parameter.add("%"+khmc+"%");
		}
		if (kssj != null && !kssj.equals("")) {
			sql.append("and to_char(to_date(y.zcfksj,'yyyy-MM-dd'),'yyyy-MM-dd') > ? ");
			parameter.add(kssj);
		}
		if (jssj != null && !jssj.equals("")) {
			sql.append("and to_char(to_date(y.zcfksj,'yyyy-MM-dd'),'yyyy-MM-dd') < ? ");
			parameter.add(jssj);
		}
		
		sql.append(" order by y.dzrq desc,y.zcfksj desc ");
		List<HashMap> dataList=new ArrayList<HashMap>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int j=0;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			int i=1;
			while (rs.next()) {
				HashMap<String,Object> map = new HashMap<String,Object>();
				String sxmcs = rs.getString("id");
				
				map.put("ycl", sxmcs);
				
				dataList.add(map);
				
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String, String>> getqyxxList(String sql,List<String> parameter){
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=sdf.format( new  Date());
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String extend3 = rs.getString("extend3");
				result.put("extend3", extend3);
				result.put("userid",user.getUserid() );
				result.put("username", user.getUsername());
				result.put("time",time);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	/**
	 * 客户名称，编号
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public List<HashMap<String, String>> getZbkhList(String sql,List<String> parameter){
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=sdf.format( new  Date());
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for(int i=0;i<parameter.size();i++){
				ps.setString(i+1, parameter.get(i));
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String companyname = rs.getString("companyname");
				String companyno = rs.getString("companyno");
				result.put("companyno", companyno);
				result.put("companyname", companyname);
				result.put("userid",user.getUserid() );
				result.put("username", user.getUsername());
				result.put("time",time);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
}
