package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.FileUploadAPI;

public class DongGuanZqbProjectManageDAO extends HibernateDaoSupport {
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	public List<HashMap> getScgkProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		//-----------------------
		sql.append(" SELECT B.CUSTOMERNAME,B.PROJECTNAME,B.MANAGER,B.OWNER,B.A08 AS TAG,B.STARTDATE,B.SSSYB,B.HTJE,B.A06,B.KHLXR,B.LXRZW,B.KHLXDH,B.XMJD,B.EXTEND1,B.EXTEND2,B.SSXQ,B.SSHY,B.ZCLR,B.XMLS,B.YJZXYNJLR,B.GZYJDZ,B.FXPGFS,B.XMBZ,B.PROJECTNO,S.INSTANCEID,I.CREATEDATE AS IC,");
		if(stagesinfo){
			sql.append("JD.JDMC,JD.TBR,JD.CJSJ,JD.FJ,JD.SORTID,JD.NUM");
		}else{
			sql.append("NULL JDMC,NULL TBR,NULL CJSJ,NULL FJ,1 SORTID,1 NUM");
		}
		sql.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
		
		if(stagesinfo){
		sql.append(" LEFT JOIN (");
		
		sql.append(" SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.CJSJ,TYXM.SORTID,Z.NUM FROM (SELECT ID,SORTID,JDMC,SXZLQD FROM BD_ZQB_TYXM_INFO");
		sql.append(" WHERE XMLX='东莞-IPO项目' AND STATE=1");
		sql.append(" AND JDMC IN('立项','股改','辅导','申报','反馈','上会','发行','上市','持续督导期间','项目终止')");
		sql.append(" ) TYXM");
		sql.append(" LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID in(SELECT ID FROM SYS_ENGINE_IFORM"); 
		sql.append(" WHERE IFORM_TITLE IN('IPO项目立项','IPO股改','公告发行方案','大会决议','权力机构决议','IPO辅导','IPO申报','IPO反馈','IPO上会','IPO发行','IPO上市','IPO持续督导','核准','封卷','实施','新增股份上市','IPO项目终止'))");
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH");
		sql.append(" LEFT JOIN (SELECT COUNT(XMBH) AS NUM,XMBH FROM BD_ZQB_IPOJD GROUP BY XMBH) Z ON JDDATA.XMBH=Z.XMBH");
		
		sql.append(" ) jd on b.projectno=jd.XMBH");
		}
		
		sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08 IN('IPO','改制','辅导')");
		
		sql.append(" AND B.A01='ssgs'");
		
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
					
			+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
			+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
			+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
			+ ") A INNER JOIN"
			+ " ("
			+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
			+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
			+ " AND B.USERID = ?"
			+ ") B ON A.INSTANCEID=B.INSTANCEID)");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			if(isManager==1l){
				sql.append(" OR B.SSSYB = ?");
				params.add(departmentName);
			}
			sql.append(")");
		}
		
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.SSSYB LIKE ?");
			params.add("%"+sssyb+"%");
		}
		
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN (");
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String)object[0];
					String projectname = (String)object[1];
					String manager = (String)object[2];
					String owner = (String)object[3];
					String a08 = (String)object[4];
					Date startdate = (Date)object[5];
					String sssyb = (String)object[6];
					BigDecimal htje = (BigDecimal)object[7];
					String a06 = (String)object[8];
					String khlxr = (String)object[9];
					String lxrzw = (String)object[10];
					String khlxdh = (String)object[11];
					String xmjd = (String)object[12];
					BigDecimal extend1 = (BigDecimal)object[13];
					BigDecimal extend2 = (BigDecimal)object[14];
					String ssxq = (String)object[15];
					String sshy = (String)object[16];
					String zclr = (String)object[17];
					String xmls = (String)object[18];
					BigDecimal yjzxynjlr = (BigDecimal)object[19];
					String gzyjdz = (String)object[20];
					BigDecimal fxpgfs = (BigDecimal)object[21];
					String xmbz = (String)object[22];
					String projectno = (String)object[23];
					BigDecimal instanceid = (BigDecimal)object[24];
					Date createdate = (Date)object[25];
					String jdmc = (String)object[26];
					String tbr = (String)object[27];
					Date cjsj = (Date)object[28];
					String fj = (String)object[29];
					BigDecimal sortid = (BigDecimal)object[30];
					BigDecimal num = (BigDecimal)object[31];

					map.put("CUSTOMERNAME", customername);
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					map.put("OWNER", owner);
					map.put("A08", a08);
					map.put("STARTDATE", startdate);
					map.put("SSSYB", sssyb);
					map.put("HTJE", htje==null?"0":htje.toString());
					map.put("A06", a06);
					map.put("KHLXR", khlxr);
					map.put("LXRZW", lxrzw);
					map.put("KHLXDH", khlxdh);
					map.put("XMJD", xmjd);
					map.put("EXTEND1", extend1==null?"0":extend1.toString());
					map.put("EXTEND2", extend2==null?"0":extend2.toString());
					map.put("SSXQ", ssxq);
					map.put("SSHY", sshy);
					map.put("ZCLR", zclr);
					map.put("XMLS", xmls);
					map.put("YJZXYNJLR", yjzxynjlr==null?"0":yjzxynjlr.toString());
					map.put("GZYJDZ", gzyjdz);
					map.put("FXPGFS", fxpgfs==null?"0":fxpgfs.toString());
					map.put("XMBZ", xmbz);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid.toString());
					map.put("CREATEDATE", createdate);
					map.put("JDMC", jdmc);
					map.put("TBR", tbr);
					map.put("CJSJ", cjsj);
					map.put("FJ", fj);
					map.put("SORTID", sortid==null?"0":sortid.toString());
					map.put("NUM", num==null?"1":num.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getZrzProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		//-----------------------
		sql.append(" SELECT B.CUSTOMERNAME,B.PROJECTNAME,B.MANAGER,B.OWNER,B.A08 AS TAG,B.STARTDATE,B.SSSYB,B.HTJE,B.A06,B.KHLXR,B.LXRZW,B.KHLXDH,B.XMJD,B.EXTEND1,B.EXTEND2,B.SSXQ,B.SSHY,B.ZCLR,B.XMLS,B.YJZXYNJLR,B.GZYJDZ,B.FXPGFS,B.XMBZ,B.PROJECTNO,S.INSTANCEID,I.CREATEDATE AS IC,");
		if(stagesinfo){
			sql.append("JD.JDMC,JD.TBR,JD.CJSJ,JD.FJ,JD.SORTID,JD.NUM");
		}else{
			sql.append("NULL JDMC,NULL TBR,NULL CJSJ,NULL FJ,1 SORTID,1 NUM");
		}
		sql.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
		
		if(stagesinfo){
		sql.append(" LEFT JOIN (");
		
		sql.append(" SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.CJSJ,TYXM.SORTID,Z.NUM FROM (SELECT ID,SORTID,JDMC,SXZLQD FROM BD_ZQB_TYXM_INFO");
		sql.append(" WHERE XMLX='东莞-IPO项目' AND STATE=1");
		sql.append(" AND JDMC IN('立项','公告发行方案','股东大会决议','申报','反馈','核准','封卷','发行实施','新增股份上市','持续督导期间','项目终止')");
		sql.append(" ) TYXM");
		sql.append(" LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID in(SELECT ID FROM SYS_ENGINE_IFORM"); 
		sql.append(" WHERE IFORM_TITLE IN('IPO项目立项','IPO股改','公告发行方案','大会决议','权力机构决议','IPO辅导','IPO申报','IPO反馈','IPO上会','IPO发行','IPO上市','IPO持续督导','核准','封卷','实施','新增股份上市','IPO项目终止'))");
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH");
		sql.append(" LEFT JOIN (SELECT COUNT(XMBH) AS NUM,XMBH FROM BD_ZQB_IPOJD GROUP BY XMBH) Z ON JDDATA.XMBH=Z.XMBH");
		
		sql.append(" ) jd on b.projectno=jd.XMBH");
		}
		
		sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08 IN('增发','配股','非公开发行','可转债')");
		
		sql.append(" AND B.A01='ssgs'");
		
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
					
			+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
			+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
			+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
			+ ") A INNER JOIN"
			+ " ("
			+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
			+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
			+ " AND B.USERID = ?"
			+ ") B ON A.INSTANCEID=B.INSTANCEID)");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			if(isManager==1l){
				sql.append(" OR B.SSSYB = ?");
				params.add(departmentName);
			}
			sql.append(")");
		}
		
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.SSSYB LIKE ?");
			params.add("%"+sssyb+"%");
		}
		
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN (");
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String)object[0];
					String projectname = (String)object[1];
					String manager = (String)object[2];
					String owner = (String)object[3];
					String a08 = (String)object[4];
					Date startdate = (Date)object[5];
					String sssyb = (String)object[6];
					BigDecimal htje = (BigDecimal)object[7];
					String a06 = (String)object[8];
					String khlxr = (String)object[9];
					String lxrzw = (String)object[10];
					String khlxdh = (String)object[11];
					String xmjd = (String)object[12];
					BigDecimal extend1 = (BigDecimal)object[13];
					BigDecimal extend2 = (BigDecimal)object[14];
					String ssxq = (String)object[15];
					String sshy = (String)object[16];
					String zclr = (String)object[17];
					String xmls = (String)object[18];
					BigDecimal yjzxynjlr = (BigDecimal)object[19];
					String gzyjdz = (String)object[20];
					BigDecimal fxpgfs = (BigDecimal)object[21];
					String xmbz = (String)object[22];
					String projectno = (String)object[23];
					BigDecimal instanceid = (BigDecimal)object[24];
					Date createdate = (Date)object[25];
					String jdmc = (String)object[26];
					String tbr = (String)object[27];
					Date cjsj = (Date)object[28];
					String fj = (String)object[29];
					BigDecimal sortid = (BigDecimal)object[30];
					BigDecimal num = (BigDecimal)object[31];

					map.put("CUSTOMERNAME", customername);
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					map.put("OWNER", owner);
					map.put("A08", a08);
					map.put("STARTDATE", startdate);
					map.put("SSSYB", sssyb);
					map.put("HTJE", htje==null?"0":htje.toString());
					map.put("A06", a06);
					map.put("KHLXR", khlxr);
					map.put("LXRZW", lxrzw);
					map.put("KHLXDH", khlxdh);
					map.put("XMJD", xmjd);
					map.put("EXTEND1", extend1==null?"0":extend1.toString());
					map.put("EXTEND2", extend2==null?"0":extend2.toString());
					map.put("SSXQ", ssxq);
					map.put("SSHY", sshy);
					map.put("ZCLR", zclr);
					map.put("XMLS", xmls);
					map.put("YJZXYNJLR", yjzxynjlr==null?"0":yjzxynjlr.toString());
					map.put("GZYJDZ", gzyjdz);
					map.put("FXPGFS", fxpgfs==null?"0":fxpgfs.toString());
					map.put("XMBZ", xmbz);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid.toString());
					map.put("CREATEDATE", createdate);
					map.put("JDMC", jdmc);
					map.put("TBR", tbr);
					map.put("CJSJ", cjsj);
					map.put("FJ", fj);
					map.put("SORTID", sortid==null?"0":sortid.toString());
					map.put("NUM", num==null?"1":num.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getJhzProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		//-----------------------
		sql.append(" SELECT B.CUSTOMERNAME,B.PROJECTNAME,B.MANAGER,B.OWNER,B.A08 AS TAG,B.STARTDATE,B.SSSYB,B.HTJE,B.A06,B.KHLXR,B.LXRZW,B.KHLXDH,B.XMJD,B.EXTEND1,B.EXTEND2,B.SSXQ,B.SSHY,B.ZCLR,B.XMLS,B.YJZXYNJLR,B.GZYJDZ,B.FXPGFS,B.XMBZ,B.PROJECTNO,S.INSTANCEID,I.CREATEDATE AS IC,");
		if(stagesinfo){
			sql.append("JD.JDMC,JD.TBR,JD.CJSJ,JD.FJ,JD.SORTID,JD.NUM");
		}else{
			sql.append("NULL JDMC,NULL TBR,NULL CJSJ,NULL FJ,1 SORTID,1 NUM");
		}
		sql.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
		
		if(stagesinfo){
		sql.append(" LEFT JOIN (");
		
		sql.append(" SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.CJSJ,TYXM.SORTID,Z.NUM FROM (SELECT ID,SORTID,JDMC,SXZLQD FROM BD_ZQB_TYXM_INFO");
		sql.append(" WHERE XMLX='东莞-IPO项目' AND STATE=1");
		sql.append(" AND JDMC IN('立项','公告发行方案','权力机构决议','申报','反馈','核准','封卷','发行实施','持续督导期间','项目终止')");
		sql.append(" ) TYXM");
		sql.append(" LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID in(SELECT ID FROM SYS_ENGINE_IFORM"); 
		sql.append(" WHERE IFORM_TITLE IN('IPO项目立项','IPO股改','公告发行方案','大会决议','权力机构决议','IPO辅导','IPO申报','IPO反馈','IPO上会','IPO发行','IPO上市','IPO持续督导','核准','封卷','实施','新增股份上市','IPO项目终止'))");
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH");
		sql.append(" LEFT JOIN (SELECT COUNT(XMBH) AS NUM,XMBH FROM BD_ZQB_IPOJD GROUP BY XMBH) Z ON JDDATA.XMBH=Z.XMBH");
		
		sql.append(" ) jd on b.projectno=jd.XMBH");
		}
		
		sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08='可交换债'");
		
		sql.append(" AND B.A01='zq'");
		
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
					
			+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
			+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
			+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
			+ ") A INNER JOIN"
			+ " ("
			+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
			+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
			+ " AND B.USERID = ?"
			+ ") B ON A.INSTANCEID=B.INSTANCEID)");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			if(isManager==1l){
				sql.append(" OR B.SSSYB = ?");
				params.add(departmentName);
			}
			sql.append(")");
		}
		
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.SSSYB LIKE ?");
			params.add("%"+sssyb+"%");
		}
		
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN (");
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String)object[0];
					String projectname = (String)object[1];
					String manager = (String)object[2];
					String owner = (String)object[3];
					String a08 = (String)object[4];
					Date startdate = (Date)object[5];
					String sssyb = (String)object[6];
					BigDecimal htje = (BigDecimal)object[7];
					String a06 = (String)object[8];
					String khlxr = (String)object[9];
					String lxrzw = (String)object[10];
					String khlxdh = (String)object[11];
					String xmjd = (String)object[12];
					BigDecimal extend1 = (BigDecimal)object[13];
					BigDecimal extend2 = (BigDecimal)object[14];
					String ssxq = (String)object[15];
					String sshy = (String)object[16];
					String zclr = (String)object[17];
					String xmls = (String)object[18];
					BigDecimal yjzxynjlr = (BigDecimal)object[19];
					String gzyjdz = (String)object[20];
					BigDecimal fxpgfs = (BigDecimal)object[21];
					String xmbz = (String)object[22];
					String projectno = (String)object[23];
					BigDecimal instanceid = (BigDecimal)object[24];
					Date createdate = (Date)object[25];
					String jdmc = (String)object[26];
					String tbr = (String)object[27];
					Date cjsj = (Date)object[28];
					String fj = (String)object[29];
					BigDecimal sortid = (BigDecimal)object[30];
					BigDecimal num = (BigDecimal)object[31];

					map.put("CUSTOMERNAME", customername);
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					map.put("OWNER", owner);
					map.put("A08", a08);
					map.put("STARTDATE", startdate);
					map.put("SSSYB", sssyb);
					map.put("HTJE", htje==null?"0":htje.toString());
					map.put("A06", a06);
					map.put("KHLXR", khlxr);
					map.put("LXRZW", lxrzw);
					map.put("KHLXDH", khlxdh);
					map.put("XMJD", xmjd);
					map.put("EXTEND1", extend1==null?"0":extend1.toString());
					map.put("EXTEND2", extend2==null?"0":extend2.toString());
					map.put("SSXQ", ssxq);
					map.put("SSHY", sshy);
					map.put("ZCLR", zclr);
					map.put("XMLS", xmls);
					map.put("YJZXYNJLR", yjzxynjlr==null?"0":yjzxynjlr.toString());
					map.put("GZYJDZ", gzyjdz);
					map.put("FXPGFS", fxpgfs==null?"0":fxpgfs.toString());
					map.put("XMBZ", xmbz);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid.toString());
					map.put("CREATEDATE", createdate);
					map.put("JDMC", jdmc);
					map.put("TBR", tbr);
					map.put("CJSJ", cjsj);
					map.put("FJ", fj);
					map.put("SORTID", sortid==null?"0":sortid.toString());
					map.put("NUM", num==null?"1":num.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getQyzProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		//-----------------------
		sql.append(" SELECT B.CUSTOMERNAME,B.PROJECTNAME,B.MANAGER,B.OWNER,B.A08 AS TAG,B.STARTDATE,B.SSSYB,B.HTJE,B.A06,B.KHLXR,B.LXRZW,B.KHLXDH,B.XMJD,B.EXTEND1,B.EXTEND2,B.SSXQ,B.SSHY,B.ZCLR,B.XMLS,B.YJZXYNJLR,B.GZYJDZ,B.FXPGFS,B.XMBZ,B.PROJECTNO,S.INSTANCEID,I.CREATEDATE AS IC,");
		if(stagesinfo){
			sql.append("JD.JDMC,JD.TBR,JD.CJSJ,JD.FJ,JD.SORTID,JD.NUM");
		}else{
			sql.append("NULL JDMC,NULL TBR,NULL CJSJ,NULL FJ,1 SORTID,1 NUM");
		}
		sql.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
		
		if(stagesinfo){
		sql.append(" LEFT JOIN (");
		
		sql.append(" SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.CJSJ,TYXM.SORTID,Z.NUM FROM (SELECT ID,SORTID,JDMC,SXZLQD FROM BD_ZQB_TYXM_INFO");
		sql.append(" WHERE XMLX='东莞-IPO项目' AND STATE=1");
		sql.append(" AND JDMC IN('立项','公告发行方案','权力机构决议','申报','反馈','核准','封卷','发行实施','持续督导期间','项目终止')");
		sql.append(" ) TYXM");
		sql.append(" LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID in(SELECT ID FROM SYS_ENGINE_IFORM"); 
		sql.append(" WHERE IFORM_TITLE IN('IPO项目立项','IPO股改','公告发行方案','大会决议','权力机构决议','IPO辅导','IPO申报','IPO反馈','IPO上会','IPO发行','IPO上市','IPO持续督导','核准','封卷','实施','新增股份上市','IPO项目终止'))");
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH");
		sql.append(" LEFT JOIN (SELECT COUNT(XMBH) AS NUM,XMBH FROM BD_ZQB_IPOJD GROUP BY XMBH) Z ON JDDATA.XMBH=Z.XMBH");
		
		sql.append(" ) jd on b.projectno=jd.XMBH");
		}
		
		sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08='企业债'");
		
		sql.append(" AND B.A01='zq'");
		
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
					
			+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
			+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
			+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
			+ ") A INNER JOIN"
			+ " ("
			+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
			+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
			+ " AND B.USERID = ?"
			+ ") B ON A.INSTANCEID=B.INSTANCEID)");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			if(isManager==1l){
				sql.append(" OR B.SSSYB = ?");
				params.add(departmentName);
			}
			sql.append(")");
		}
		
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.SSSYB LIKE ?");
			params.add("%"+sssyb+"%");
		}
		
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN (");
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String)object[0];
					String projectname = (String)object[1];
					String manager = (String)object[2];
					String owner = (String)object[3];
					String a08 = (String)object[4];
					Date startdate = (Date)object[5];
					String sssyb = (String)object[6];
					BigDecimal htje = (BigDecimal)object[7];
					String a06 = (String)object[8];
					String khlxr = (String)object[9];
					String lxrzw = (String)object[10];
					String khlxdh = (String)object[11];
					String xmjd = (String)object[12];
					BigDecimal extend1 = (BigDecimal)object[13];
					BigDecimal extend2 = (BigDecimal)object[14];
					String ssxq = (String)object[15];
					String sshy = (String)object[16];
					String zclr = (String)object[17];
					String xmls = (String)object[18];
					BigDecimal yjzxynjlr = (BigDecimal)object[19];
					String gzyjdz = (String)object[20];
					BigDecimal fxpgfs = (BigDecimal)object[21];
					String xmbz = (String)object[22];
					String projectno = (String)object[23];
					BigDecimal instanceid = (BigDecimal)object[24];
					Date createdate = (Date)object[25];
					String jdmc = (String)object[26];
					String tbr = (String)object[27];
					Date cjsj = (Date)object[28];
					String fj = (String)object[29];
					BigDecimal sortid = (BigDecimal)object[30];
					BigDecimal num = (BigDecimal)object[31];

					map.put("CUSTOMERNAME", customername);
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					map.put("OWNER", owner);
					map.put("A08", a08);
					map.put("STARTDATE", startdate);
					map.put("SSSYB", sssyb);
					map.put("HTJE", htje==null?"0":htje.toString());
					map.put("A06", a06);
					map.put("KHLXR", khlxr);
					map.put("LXRZW", lxrzw);
					map.put("KHLXDH", khlxdh);
					map.put("XMJD", xmjd);
					map.put("EXTEND1", extend1==null?"0":extend1.toString());
					map.put("EXTEND2", extend2==null?"0":extend2.toString());
					map.put("SSXQ", ssxq);
					map.put("SSHY", sshy);
					map.put("ZCLR", zclr);
					map.put("XMLS", xmls);
					map.put("YJZXYNJLR", yjzxynjlr==null?"0":yjzxynjlr.toString());
					map.put("GZYJDZ", gzyjdz);
					map.put("FXPGFS", fxpgfs==null?"0":fxpgfs.toString());
					map.put("XMBZ", xmbz);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid.toString());
					map.put("CREATEDATE", createdate);
					map.put("JDMC", jdmc);
					map.put("TBR", tbr);
					map.put("CJSJ", cjsj);
					map.put("FJ", fj);
					map.put("SORTID", sortid==null?"0":sortid.toString());
					map.put("NUM", num==null?"1":num.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getGszProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		//-----------------------
		sql.append(" SELECT B.CUSTOMERNAME,B.PROJECTNAME,B.MANAGER,B.OWNER,B.A08 AS TAG,B.STARTDATE,B.SSSYB,B.HTJE,B.A06,B.KHLXR,B.LXRZW,B.KHLXDH,B.XMJD,B.EXTEND1,B.EXTEND2,B.SSXQ,B.SSHY,B.ZCLR,B.XMLS,B.YJZXYNJLR,B.GZYJDZ,B.FXPGFS,B.XMBZ,B.PROJECTNO,S.INSTANCEID,I.CREATEDATE AS IC,");
		if(stagesinfo){
			sql.append("JD.JDMC,JD.TBR,JD.CJSJ,JD.FJ,JD.SORTID,JD.NUM");
		}else{
			sql.append("NULL JDMC,NULL TBR,NULL CJSJ,NULL FJ,1 SORTID,1 NUM");
		}
		sql.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
		
		if(stagesinfo){
		sql.append(" LEFT JOIN (");
		
		sql.append(" SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.CJSJ,TYXM.SORTID,Z.NUM FROM (SELECT ID,SORTID,JDMC,SXZLQD FROM BD_ZQB_TYXM_INFO");
		sql.append(" WHERE XMLX='东莞-IPO项目' AND STATE=1");
		sql.append(" AND JDMC IN('立项','公告发行方案','权力机构决议','申报','反馈','核准','封卷','发行实施','持续督导期间','项目终止')");
		sql.append(" ) TYXM");
		sql.append(" LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID in(SELECT ID FROM SYS_ENGINE_IFORM"); 
		sql.append(" WHERE IFORM_TITLE IN('IPO项目立项','IPO股改','公告发行方案','大会决议','权力机构决议','IPO辅导','IPO申报','IPO反馈','IPO上会','IPO发行','IPO上市','IPO持续督导','核准','封卷','实施','新增股份上市','IPO项目终止'))");
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH");
		sql.append(" LEFT JOIN (SELECT COUNT(XMBH) AS NUM,XMBH FROM BD_ZQB_IPOJD GROUP BY XMBH) Z ON JDDATA.XMBH=Z.XMBH");
		
		sql.append(" ) JD ON B.PROJECTNO=JD.XMBH");
		}
		
		sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08='公司债'");
		
		sql.append(" AND B.A01='zq'");
		
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
					
			+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
			+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
			+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
			+ ") A INNER JOIN"
			+ " ("
			+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
			+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
			+ " AND B.USERID = ?"
			+ ") B ON A.INSTANCEID=B.INSTANCEID)");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			if(isManager==1l){
				sql.append(" OR B.SSSYB = ?");
				params.add(departmentName);
			}
			sql.append(")");
		}
		
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.SSSYB LIKE ?");
			params.add("%"+sssyb+"%");
		}
		
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN (");
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String)object[0];
					String projectname = (String)object[1];
					String manager = (String)object[2];
					String owner = (String)object[3];
					String a08 = (String)object[4];
					Date startdate = (Date)object[5];
					String sssyb = (String)object[6];
					BigDecimal htje = (BigDecimal)object[7];
					String a06 = (String)object[8];
					String khlxr = (String)object[9];
					String lxrzw = (String)object[10];
					String khlxdh = (String)object[11];
					String xmjd = (String)object[12];
					BigDecimal extend1 = (BigDecimal)object[13];
					BigDecimal extend2 = (BigDecimal)object[14];
					String ssxq = (String)object[15];
					String sshy = (String)object[16];
					String zclr = (String)object[17];
					String xmls = (String)object[18];
					BigDecimal yjzxynjlr = (BigDecimal)object[19];
					String gzyjdz = (String)object[20];
					BigDecimal fxpgfs = (BigDecimal)object[21];
					String xmbz = (String)object[22];
					String projectno = (String)object[23];
					BigDecimal instanceid = (BigDecimal)object[24];
					Date createdate = (Date)object[25];
					String jdmc = (String)object[26];
					String tbr = (String)object[27];
					Date cjsj = (Date)object[28];
					String fj = (String)object[29];
					BigDecimal sortid = (BigDecimal)object[30];
					BigDecimal num = (BigDecimal)object[31];

					map.put("CUSTOMERNAME", customername);
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					map.put("OWNER", owner);
					map.put("A08", a08);
					map.put("STARTDATE", startdate);
					map.put("SSSYB", sssyb);
					map.put("HTJE", htje==null?"0":htje.toString());
					map.put("A06", a06);
					map.put("KHLXR", khlxr);
					map.put("LXRZW", lxrzw);
					map.put("KHLXDH", khlxdh);
					map.put("XMJD", xmjd);
					map.put("EXTEND1", extend1==null?"0":extend1.toString());
					map.put("EXTEND2", extend2==null?"0":extend2.toString());
					map.put("SSXQ", ssxq);
					map.put("SSHY", sshy);
					map.put("ZCLR", zclr);
					map.put("XMLS", xmls);
					map.put("YJZXYNJLR", yjzxynjlr==null?"0":yjzxynjlr.toString());
					map.put("GZYJDZ", gzyjdz);
					map.put("FXPGFS", fxpgfs==null?"0":fxpgfs.toString());
					map.put("XMBZ", xmbz);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid.toString());
					map.put("CREATEDATE", createdate);
					map.put("JDMC", jdmc);
					map.put("TBR", tbr);
					map.put("CJSJ", cjsj);
					map.put("FJ", fj);
					map.put("SORTID", sortid==null?"0":sortid.toString());
					map.put("NUM", num==null?"1":num.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getOtherProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		//-----------------------
		sql.append(" SELECT B.CUSTOMERNAME,B.PROJECTNAME,B.MANAGER,B.OWNER,B.A08 AS TAG,B.STARTDATE,B.SSSYB,B.HTJE,B.A06,B.KHLXR,B.LXRZW,B.KHLXDH,B.XMJD,B.EXTEND1,B.EXTEND2,B.SSXQ,B.SSHY,B.ZCLR,B.XMLS,B.YJZXYNJLR,B.GZYJDZ,B.FXPGFS,B.XMBZ,B.PROJECTNO,S.INSTANCEID,I.CREATEDATE AS IC,");
		if(stagesinfo){
			sql.append("JD.JDMC,JD.TBR,JD.CJSJ,JD.FJ,JD.SORTID,JD.NUM");
		}else{
			sql.append("NULL JDMC,NULL TBR,NULL CJSJ,NULL FJ,1 SORTID,1 NUM");
		}
		sql.append(" FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
		
		if(stagesinfo){
		sql.append(" LEFT JOIN (");
		
		sql.append(" SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.CJSJ,TYXM.SORTID,Z.NUM FROM (SELECT ID,SORTID,JDMC,SXZLQD FROM BD_ZQB_TYXM_INFO");
		sql.append(" WHERE XMLX='东莞-IPO项目' AND STATE=1");
		sql.append(" AND JDMC IN('立项','公告方案','权力机构决议','申报','反馈','核准','封卷','实施','持续督导期间','项目终止')");
		sql.append(" ) TYXM");
		sql.append(" LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID in(SELECT ID FROM SYS_ENGINE_IFORM"); 
		sql.append(" WHERE IFORM_TITLE IN('IPO项目立项','IPO股改','公告发行方案','大会决议','权力机构决议','IPO辅导','IPO申报','IPO反馈','IPO上会','IPO发行','IPO上市','IPO持续督导','核准','封卷','实施','新增股份上市','IPO项目终止'))");
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH");
		sql.append(" LEFT JOIN (SELECT COUNT(XMBH) AS NUM,XMBH FROM BD_ZQB_IPOJD GROUP BY XMBH) Z ON JDDATA.XMBH=Z.XMBH");
		
		sql.append(" ) jd on b.projectno=jd.XMBH");
		}
		
		sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08='其他'");
		
		if("xsb".equals(type)){
			sql.append(" AND B.A01='xsb'");
		}else if("ssgs".equals(type)){
			sql.append(" AND B.A01='ssgs'");
		}else if("zq".equals(type)){
			sql.append(" AND B.A01='zq'");
		}
		
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
					
			+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
			+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
			+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
			+ ") A INNER JOIN"
			+ " ("
			+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
			+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
			+ " AND B.USERID = ?"
			+ ") B ON A.INSTANCEID=B.INSTANCEID)");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			if(isManager==1l){
				sql.append(" OR B.SSSYB = ?");
				params.add(departmentName);
			}
			sql.append(")");
		}
		
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.SSSYB LIKE ?");
			params.add("%"+sssyb+"%");
		}
		
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN (");
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String)object[0];
					String projectname = (String)object[1];
					String manager = (String)object[2];
					String owner = (String)object[3];
					String a08 = (String)object[4];
					Date startdate = (Date)object[5];
					String sssyb = (String)object[6];
					BigDecimal htje = (BigDecimal)object[7];
					String a06 = (String)object[8];
					String khlxr = (String)object[9];
					String lxrzw = (String)object[10];
					String khlxdh = (String)object[11];
					String xmjd = (String)object[12];
					BigDecimal extend1 = (BigDecimal)object[13];
					BigDecimal extend2 = (BigDecimal)object[14];
					String ssxq = (String)object[15];
					String sshy = (String)object[16];
					String zclr = (String)object[17];
					String xmls = (String)object[18];
					BigDecimal yjzxynjlr = (BigDecimal)object[19];
					String gzyjdz = (String)object[20];
					BigDecimal fxpgfs = (BigDecimal)object[21];
					String xmbz = (String)object[22];
					String projectno = (String)object[23];
					BigDecimal instanceid = (BigDecimal)object[24];
					Date createdate = (Date)object[25];
					String jdmc = (String)object[26];
					String tbr = (String)object[27];
					Date cjsj = (Date)object[28];
					String fj = (String)object[29];
					BigDecimal sortid = (BigDecimal)object[30];
					BigDecimal num = (BigDecimal)object[31];

					map.put("CUSTOMERNAME", customername);
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					map.put("OWNER", owner);
					map.put("A08", a08);
					map.put("STARTDATE", startdate);
					map.put("SSSYB", sssyb);
					map.put("HTJE", htje==null?"0":htje.toString());
					map.put("A06", a06);
					map.put("KHLXR", khlxr);
					map.put("LXRZW", lxrzw);
					map.put("KHLXDH", khlxdh);
					map.put("XMJD", xmjd);
					map.put("EXTEND1", extend1==null?"0":extend1.toString());
					map.put("EXTEND2", extend2==null?"0":extend2.toString());
					map.put("SSXQ", ssxq);
					map.put("SSHY", sshy);
					map.put("ZCLR", zclr);
					map.put("XMLS", xmls);
					map.put("YJZXYNJLR", yjzxynjlr==null?"0":yjzxynjlr.toString());
					map.put("GZYJDZ", gzyjdz);
					map.put("FXPGFS", fxpgfs==null?"0":fxpgfs.toString());
					map.put("XMBZ", xmbz);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid.toString());
					map.put("CREATEDATE", createdate);
					map.put("JDMC", jdmc);
					map.put("TBR", tbr);
					map.put("CJSJ", cjsj);
					map.put("FJ", fj);
					map.put("SORTID", sortid==null?"0":sortid.toString());
					map.put("NUM", num==null?"1":num.toString());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getBgczProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		//-------------------------
		sql.append(" SELECT B.JYF,B.JYDSF,B.SGFS,B.CZBM,B.SFGLJY,B.GDYKZRBG,B.SFCZ,B.SFWG,B.OWNER,B.MANAGER,B.KHLXR,B.LXRZW,B.KHLXDH,B.ZYSD,B.TPRQ,B.XMJD,B.JYJGA"
		+ ",B.JYJGB,B.YJZFFS,B.PTMJZJ,B.SSHY,B.GLGXMSWZ,B.KZQBG,B.TYJZBG,B.CJQYBD,B.CJZYYWBG,B.SSXQ,B.GZTK,B.CNTK,B.XMFX,B.BZ"
		+ ",B.XMFQRQ,K.CUSTOMERNAME,TO_CHAR(3) AS TAG,S.INSTANCEID,I.CREATEDATE AS IC,B.XMBH,");
		if(stagesinfo){
			sql.append("JD.SORTID,JD.JDMC,JD.TBR,JD.TBSJ,JD.FJ,JD.NUM");
		}else{
			sql.append("1 SORTID,NULL JDMC,NULL TBR,NULL TBSJ,NULL FJ,1 NUM");
		}
		
		sql.append(" FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
		
		if(stagesinfo){
		sql.append(" LEFT JOIN (");
			sql.append(" SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.TBSJ,TYXM.SORTID,GP.NUM");
			sql.append(" FROM (SELECT ID,SORTID,JDMC,SXZLQD,STATE FROM BD_ZQB_TYXM_INFO WHERE XMLX='东莞-并购重组') TYXM");
			sql.append(" LEFT JOIN (");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGXMLX B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购立项')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='公告并购重组方案')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购股东大会决议')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购申报')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGFAZLBS B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购反馈')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGZLGD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购补充披露')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSSQKB B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购实施情况')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='持续督导期间')");
				sql.append(" UNION");
				sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_XMZZ B INNER JOIN ORGUSER ORG ON B.USERID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购项目终止')");
			sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH"); 
			sql.append(" LEFT JOIN(");
				sql.append(" SELECT XMBH,COUNT(XMBH) AS NUM FROM (");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGXMLX B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购立项')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='公告并购重组方案')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购股东大会决议')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购申报')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGFAZLBS B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购反馈')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZLGD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购补充披露')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGSSQKB B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购实施情况')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='持续督导期间')");
					sql.append(" UNION ALL");
					sql.append(" SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_XMZZ B INNER JOIN ORGUSER ORG ON B.USERID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购项目终止')");
				sql.append(" ) GROUP BY XMBH");
			sql.append(" ) GP ON JDDATA.XMBH=GP.XMBH");
			sql.append(" WHERE TYXM.STATE=1");
		sql.append(" ) JD ON B.XMBH=JD.XMBH");
		}
		
		sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='1'");
		if("xsb".equals(type)){
			sql.append(" AND B.COMPANYNAME='xsb'");
		}else if("ssgs".equals(type)){
			sql.append(" AND B.COMPANYNAME='ssgs'");
		}
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
				+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
					
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
			params.add("["+userid+"]");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			if(isManager==1l){
				sql.append(" OR B.CZBM = ?");
				params.add(departmentName);
			}
			sql.append(")");
		}
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.CZBM LIKE ?");
			params.add("%"+sssyb+"%");
		}
	
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.XMBH IN (");
			sql.append("SELECT A.XMBH FROM (");
			
			sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN"); 
			
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String JYF = (String)object[0];
					String JYDSF = (String)object[1];
					String SGFS = (String)object[2];
					String CZBM = (String)object[3];
					String SFGLJY = (String)object[4];
					String GDYKZRBG = (String)object[5];
					String SFCZ = (String)object[6];
					String SFWG = (String)object[7];
					String OWNER = (String)object[8];
					String MANAGER = (String)object[9];
					String KHLXR = (String)object[10];
					String LXRZW = (String)object[11];
					String KHLXDH = (String)object[12];
					String ZYSD = (String)object[13];
					//String TPRQ = (String)object[14];
					Date TPRQ = (Date)object[14];
					String XMJD = (String)object[15];
					BigDecimal JYJGA = (BigDecimal)object[16];
					//String JYJGA = (String)object[16];
					//String JYJGB = (String)object[17];
					BigDecimal JYJGB = (BigDecimal)object[17];
					String YJZFFS = (String)object[18];
					//String PTMJZJ = (String)object[19];
					BigDecimal PTMJZJ = (BigDecimal)object[19];
					String SSHY = (String)object[20];
					String GLGXMSWZ = (String)object[21];
					String KZQBG = (String)object[22];
					String TYJZBG = (String)object[23];
					String CJQYBD = (String)object[24];
					String CJZYYWBG = (String)object[25];
					String SSXQ = (String)object[26];
					String GZTK = (String)object[27];
					String CNTK = (String)object[28];
					String XMFX = (String)object[29];
					String BZ = (String)object[30];
					
					Date XMFQRQ = (Date)object[31];
					String CUSTOMERNAME = (String)object[32];
					String TAG = (String)object[33];
					BigDecimal INSTANCEID = (BigDecimal)object[34];
					Date CREATEDATE = (Date)object[35];
					String XMBH = (String)object[36];
					BigDecimal SORTID = (BigDecimal)object[37];
					String JDMC = (String)object[38];
					String TBR = (String)object[39];
					Date TBSJ = (Date)object[40];
					String FJ = (String)object[41];
					BigDecimal NUM = (BigDecimal)object[42];
					
					map.put("JYF", JYF);
					map.put("JYDSF", JYDSF);
					map.put("SGFS", SGFS);
					map.put("CZBM", CZBM);
					map.put("SFGLJY", SFGLJY);
					map.put("GDYKZRBG", GDYKZRBG);
					map.put("SFCZ", SFCZ);
					map.put("SFWG", SFWG);
					map.put("OWNER", OWNER);
					map.put("MANAGER", MANAGER);
					map.put("KHLXR", KHLXR);
					map.put("LXRZW", LXRZW);
					map.put("KHLXDH", KHLXDH);
					map.put("ZYSD", ZYSD);
					map.put("TPRQ", TPRQ);
					map.put("XMJD", XMJD);
					map.put("JYJGA", JYJGA);
					map.put("JYJGB", JYJGB);
					map.put("YJZFFS", YJZFFS);
					map.put("PTMJZJ", PTMJZJ);
					map.put("SSHY", SSHY);
					map.put("GLGXMSWZ", GLGXMSWZ);
					map.put("KZQBG", KZQBG);
					map.put("TYJZBG", TYJZBG);
					map.put("CJQYBD", CJQYBD);
					map.put("CJZYYWBG", CJZYYWBG);
					map.put("SSXQ", SSXQ);
					map.put("GZTK", GZTK);
					map.put("CNTK", CNTK);
					map.put("XMFX", XMFX);
					map.put("BZ", BZ);
					
					map.put("XMFQRQ", XMFQRQ);
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					map.put("TAG", TAG);
					map.put("INSTANCEID", INSTANCEID);
					map.put("CREATEDATE", CREATEDATE);
					map.put("XMBH", XMBH);
					map.put("SORTID", SORTID);
					map.put("JDMC", JDMC);
					map.put("TBR", TBR);
					map.put("TBSJ", TBSJ);
					map.put("FJ", FJ);
					map.put("NUM", NUM);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getDxzfProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		if("xsb".equals(type)){
			//-------------------------------------------
			sql.append(" SELECT B.CUSTOMERNAME,B.PROJECTNAME,B.OWNER,B.MANAGER,B.KHLXR,B.LXRZW,B.KHLXDH,B.CLBM,B.CZBM,B.JSYY,B.MEMO,B.GPFXSL,B.FXMDCS,B.MJZJZE,B.GPFXJZ,B.XYGDRS,B.ZRFS,B.YJSR,"
					+ "B.YJFY,B.SSXQ,B.SSHY,B.XMFXFX,B.ZYZJQK,TO_CHAR(2) AS TAG,B.STARTDATE,B.ENDDATE,S.INSTANCEID,I.CREATEDATE AS IC,B.PROJECTNO,");
			if(stagesinfo){
				sql.append("JD.JDMC,JD.CJRXM,JD.CJSJ,JD.FJ,JD.SORTID,JD.NUM");
			}else{
				sql.append("NULL JDMC,NULL CJRXM,NULL CJSJ,NULL FJ,1 SORTID,1 NUM");
			}
			
			sql.append(" FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
			
			if(stagesinfo){
			sql.append(" LEFT JOIN (");
				sql.append(" SELECT B.JDMC,B.CJRXM,B.CJSJ,DZJD.FJ,DZJD.PROJECTNO,B.SORTID,JDNUM.NUM FROM (");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.NFXFILE AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMNFXB BOTABLE"); 
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行立项')");
					sql.append(" UNION ALL");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.FABSZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMFAZLBSB BOTABLE");
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行概况')");
					sql.append(" UNION ALL");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.SBZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE");
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行备案')");
					sql.append(" UNION ALL");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.SBZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE");
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股东大会决议')");
					sql.append(" UNION ALL");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.SBZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE");
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='认购起始日')");
					sql.append(" UNION ALL");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.NFXFILE AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_DXFXFK BOTABLE");
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行反馈')");
					sql.append(" UNION ALL");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.NFXFILE AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSJFXB BOTABLE");
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行备案结果')");
					sql.append(" UNION ALL");
					sql.append(" SELECT BOTABLE.PROJECTNO,BOTABLE.USERID,BOTABLE.USERNAME,BOTABLE.FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_XMZZ BOTABLE");
					sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行项目终止')");
				sql.append(" ) DZJD LEFT JOIN(");
					sql.append(" SELECT PROJECTNO,COUNT(PROJECTNO) AS NUM FROM (");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMNFXB BOTABLE"); 
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行立项')");
						sql.append(" UNION ALL");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMFAZLBSB BOTABLE");
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行概况')");
						sql.append(" UNION ALL");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE");
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行备案')");
						sql.append(" UNION ALL");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE");
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股东大会决议')");
						sql.append(" UNION ALL");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE");
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='认购起始日')");
						sql.append(" UNION ALL");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_DXFXFK BOTABLE");
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行反馈')");
						sql.append(" UNION ALL");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSJFXB BOTABLE");
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行备案结果')");
						sql.append(" UNION ALL");
						sql.append(" SELECT BOTABLE.PROJECTNO,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_XMZZ BOTABLE");
						sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行项目终止')");
					sql.append(" ) GROUP BY PROJECTNO");
					sql.append(" ) JDNUM ON DZJD.PROJECTNO=JDNUM.PROJECTNO");
				sql.append(" LEFT JOIN BD_ZQB_TYXM_INFO B ON B.ID=DZJD.GROUPID");
			sql.append(" ) JD ON B.PROJECTNO=JD.PROJECTNO");
			}
			
			
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目') AND B.STATUS='执行中'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN("
						+ " SELECT A.PROJECTNO FROM ("
						
				+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ "  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')"
				+ " ) A INNER JOIN"
				+ "  ("
				+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ "  WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ "  AND B.USERID = ?"
				+ "  ) B ON A.INSTANCEID=B.INSTANCEID"
				
				+ "  )");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append(" OR B.CLBM = ?");
					sql.append(" OR B.CZBM = ?");
					params.add(departmentName);
					params.add(departmentName);
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				sql.append("SELECT A.PROJECTNO FROM (");
				
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ?)");
				sql.append(") A LEFT JOIN"); 
				sql.append(" (");
				sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?");
				sql.append(" ) B ON A.INSTANCEID=B.INSTANCEID");
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String CUSTOMERNAME = (String)object[0];
					String PROJECTNAME = (String)object[1];
					String OWNER = (String)object[2];
					String MANAGER = (String)object[3];
					String KHLXR = (String)object[4];
					String LXRZW = (String)object[5];
					String KHLXDH = (String)object[6];
					String CLBM = (String)object[7];
					String CZBM = (String)object[8];
					String JSYY = (String)object[9];
					String MEMO = (String)object[10];
					BigDecimal GPFXSL = (BigDecimal)object[11];
					String FXMDCS = (String)object[12];
					BigDecimal MJZJZE = (BigDecimal)object[13];
					String GPFXJZ = (String)object[14];
					BigDecimal XYGDRS = (BigDecimal)object[15];
					BigDecimal ZRFS = (BigDecimal)object[16];
					BigDecimal YJSR = (BigDecimal)object[17];
					BigDecimal YJFY = (BigDecimal)object[18];
					String SSXQ = (String)object[19];
					String SSHY = (String)object[20];
					String XMFXFX = (String)object[21];
					String ZYZJQK = (String)object[22];
					
					String TAG = (String)object[23];
					Date STARTDATE = (Date)object[24];
					//String STARTDATE = (String)object[24];
					Date ENDDATE = (Date)object[25];
					//String ENDDATE = (String)object[25];
					BigDecimal INSTANCEID = (BigDecimal)object[26];
					Date IC = (Date)object[27];
					String PROJECTNO = (String)object[28];
					String JDMC = (String)object[29];
					String CJRXM = (String)object[30];
					Date CJSJ = (Date)object[31];
					String FJ = (String)object[32];
					BigDecimal SORTID = (BigDecimal)object[33];
					BigDecimal NUM = (BigDecimal)object[34];
					
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					map.put("PROJECTNAME", PROJECTNAME);
					map.put("OWNER", OWNER);
					map.put("MANAGER", MANAGER);
					map.put("KHLXR", KHLXR);
					map.put("LXRZW", LXRZW);
					map.put("KHLXDH", KHLXDH);
					map.put("CLBM", CLBM);
					map.put("CZBM", CZBM);
					map.put("JSYY", JSYY);
					map.put("MEMO", MEMO);
					map.put("GPFXSL", GPFXSL);
					map.put("FXMDCS", FXMDCS);
					map.put("MJZJZE", MJZJZE);
					map.put("GPFXJZ", GPFXJZ);
					map.put("XYGDRS", XYGDRS);
					map.put("ZRFS", ZRFS);
					map.put("YJSR", YJSR);
					map.put("YJFY", YJFY);
					map.put("SSXQ", SSXQ);
					map.put("SSHY", SSHY);
					map.put("XMFXFX", XMFXFX);
					map.put("ZYZJQK", ZYZJQK);
					
					map.put("TAG", TAG);
					map.put("STARTDATE", STARTDATE);
					map.put("ENDDATE", ENDDATE);
					map.put("INSTANCEID", INSTANCEID);
					map.put("IC", IC);
					map.put("PROJECTNO", PROJECTNO);
					map.put("JDMC", JDMC);
					map.put("CJRXM", CJRXM);
					map.put("CJSJ", CJSJ);
					map.put("FJ", FJ);
					map.put("SORTID", SORTID);
					map.put("NUM", NUM);
					
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getTjgpProList(boolean stagesinfo,String customername,String sssyb,String cyrName,String type,String xmlx){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		if("xsb".equals(type)){
			sql.append("SELECT B.PROJECTNAME,B.XMBZ,B.XMYS,B.ZCLR,B.HTJE,B.SFXZCL,B.CUSTOMERINFO,B.GSGK,B.FZJGMC,B.A01,B.YJSBSJ,B.XMCY,B.XMJD,B.SSXQ,B.LXRZW,B.SSHY,B.GZYJDZ,"
					+ "B.OWNER,B.MANAGER,B.DDAP,B.XMZH,B.XMXY,B.XMLS,"
					+ "B.GGJZR,B.SBJZR,B.CLSLR,B.SHTGR,B.ENDDATE,"
					+ "B.A03,B.A04,B.A05,B.A06,B.A07,B.A08,B.YJZXYNJLR,B.FXPGFS,"
					+ "K.CUSTOMERNAME,TO_CHAR(1) AS TAG,B.STARTDATE,S.INSTANCEID,I.CREATEDATE AS IC,B.PROJECTNO,");
			if(stagesinfo){
				sql.append("JD.JDMC,JD.TBR,JD.TBSJ,JD.JDZL,JD.NUM,JD.SORTID");
			}else{
				sql.append("NULL JDMC,NULL TBR,NULL TBSJ,NULL JDZL,1 NUM,1 SORTID");
			}
			
			sql.append(" FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID");
			
			if(stagesinfo){
			sql.append(" LEFT JOIN (");
				sql.append(" SELECT J.JDMC,JD.TBR,JD.TBSJ,JD.JDZL,J.SORTID,JDNUM.NUM,JD.PROJECTNO FROM (");
					sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.TJRXM AS JDZL,B.TBR||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_XMLXLCB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项')");
					sql.append(" UNION");
					sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_LCGG B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股改')");
					sql.append(" UNION");
					sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_SQNH B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='申报')");
					sql.append(" UNION");
					sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_NHFK B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='反馈')");
					sql.append(" UNION");
					sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_GZFKJHF B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='同意挂牌')");
					sql.append(" UNION");
					sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.GPZL AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_GPDJJGD B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='正式挂牌')");
					sql.append(" UNION");
					sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.USERNAME||'['||B.USERID||']' AS TBR,B.CJSJ FROM BD_ZQB_XMZZ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目终止')");
				sql.append(" ) JD LEFT JOIN BD_ZQB_TYXM_INFO J ON J.ID=JD.JDID");
				sql.append(" LEFT JOIN (");
					sql.append(" SELECT PROJECTNO,COUNT(PROJECTNO) AS NUM FROM (");
						sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO FROM BD_ZQB_XMLXLCB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项')");
						sql.append(" UNION SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO FROM BD_ZQB_LCGG B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股改')");
						sql.append(" UNION SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO FROM BD_ZQB_SQNH B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='申报')");
						sql.append(" UNION SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO FROM BD_ZQB_NHFK B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='反馈')");
						sql.append(" UNION SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO FROM BD_ZQB_GZFKJHF B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='同意挂牌')");
						sql.append(" UNION SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO FROM BD_ZQB_GPDJJGD B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='正式挂牌')");
						sql.append(" UNION SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO FROM BD_ZQB_XMZZ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目终止')");
					sql.append(" ) GROUP BY PROJECTNO");
				sql.append(" ) JDNUM ON JDNUM.PROJECTNO=JD.PROJECTNO");
			sql.append(" ) JD ON B.PROJECTNO=JD.PROJECTNO");
			}
			
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况') AND B.STATUS='执行中'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID = ? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR SUBSTR(B.ZCLR,0, instr(B.ZCLR,'[',1)-1)=?"
						+ " OR SUBSTR(B.DDAP,0, instr(B.DDAP,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMZH,0, instr(B.XMZH,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMXY,0, instr(B.XMXY,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMLS,0, instr(B.XMLS,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN(SELECT A.PROJECTNO FROM ("
						+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO" 
						+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')"
						+ " ) A INNER JOIN"
						+ " ("
						+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
						+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
						+ " AND B.USERID = ?"
						+ " ) B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append(" OR B.GSGK = ?");
					params.add(departmentName);
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.GSGK LIKE '%"+sssyb+"%'");
				params.add("%"+sssyb+"%");
			}
			
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')");
				sql.append(" AND (B.ZCLR LIKE ?");
				sql.append(" OR B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ?");
				sql.append(" OR B.DDAP LIKE ?");
				sql.append(" OR B.XMZH LIKE ?");
				sql.append(" OR B.XMXY LIKE ?");
				sql.append(" OR B.XMLS LIKE ?)");
				sql.append(") A LEFT JOIN");
				sql.append(" (");
				sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?");
				sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}
		sql.append(") WHERE 1=1");
		sql.append(" ORDER BY IC DESC,SORTID ASC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String PROJECTNAME = (String)object[0];
					String XMBZ = (String)object[1];
					String XMYS = (String)object[2];
					String ZCLR = (String)object[3];
					BigDecimal HTJE = (BigDecimal)object[4];
					String SFXZCL = (String)object[5];
					String CUSTOMERINFO = (String)object[6];
					String GSGK = (String)object[7];
					String FZJGMC = (String)object[8];
					BigDecimal A01 = (BigDecimal)object[9];
					String YJSBSJ = (String)object[10];
					String XMCY = (String)object[11];
					String XMJD = (String)object[12];
					String SSXQ = (String)object[13];
					String LXRZW = (String)object[14];
					String SSHY = (String)object[15];
					String GZYJDZ = (String)object[16];
					String OWNER = (String)object[17];
					String MANAGER = (String)object[18];
					String DDAP = (String)object[19];
					String XMZH = (String)object[20];
					String XMXY = (String)object[21];
					String XMLS = (String)object[22];
					Date GGJZR = (Date)object[23];
					Date SBJZR = (Date)object[24];
					Date CLSLR = (Date)object[25];
					Date SHTGR = (Date)object[26];
					Date ENDDATE = (Date)object[27];
					BigDecimal A03 = (BigDecimal)object[28];
					BigDecimal A04 = (BigDecimal)object[29];
					BigDecimal A05 = (BigDecimal)object[30];
					BigDecimal A06 = (BigDecimal)object[31];
					BigDecimal A07 = (BigDecimal)object[32];
					BigDecimal A08 = (BigDecimal)object[33];
					BigDecimal YJZXYNJLR = (BigDecimal)object[34];
					BigDecimal FXPGFS = (BigDecimal)object[35];
					
					String CUSTOMERNAME = (String)object[36];
					String TAG = (String)object[37];
					String STARTDATE = (String)object[38];
					BigDecimal INSTANCEID = (BigDecimal)object[39];
					Date IC = (Date)object[40];
					String PROJECTNO = (String)object[41];
					
					String JDMC = (String)object[42];
					String TBR = (String)object[43];
					Date TBSJ = (Date)object[44];
					String JDZL = (String)object[45];
					BigDecimal NUM = (BigDecimal)object[46];

					map.put("PROJECTNAME",PROJECTNAME);
					map.put("XMBZ",XMBZ);
					map.put("XMYS",XMYS);
					map.put("ZCLR",ZCLR);
					map.put("HTJE",HTJE);
					map.put("SFXZCL",SFXZCL);
					map.put("CUSTOMERINFO",CUSTOMERINFO);
					map.put("GSGK",GSGK);
					map.put("FZJGMC",FZJGMC);
					map.put("A01",A01);
					map.put("YJSBSJ",YJSBSJ);
					map.put("XMCY",XMCY);
					map.put("XMJD",XMJD);
					map.put("SSXQ",SSXQ);
					map.put("LXRZW",LXRZW);
					map.put("SSHY",SSHY);
					map.put("GZYJDZ",GZYJDZ);
					
					map.put("OWNER",OWNER);
					map.put("MANAGER",MANAGER);
					map.put("DDAP",DDAP);
					map.put("XMZH",XMZH);
					map.put("XMXY",XMXY);
					map.put("XMLS",XMLS);
					
					map.put("GGJZR",GGJZR);
					map.put("SBJZR",SBJZR);
					map.put("CLSLR",CLSLR);
					map.put("SHTGR",SHTGR);
					map.put("ENDDATE",ENDDATE);
					
					map.put("A03",A03);
					map.put("A04",A04);
					map.put("A05",A05);
					map.put("A06",A06);
					map.put("A07",A07);
					map.put("A08",A08);
					map.put("YJZXYNJLR",YJZXYNJLR);
					map.put("FXPGFS",FXPGFS);
					
					map.put("CUSTOMERNAME",CUSTOMERNAME);
					map.put("TAG",TAG);
					map.put("STARTDATE",STARTDATE);
					map.put("INSTANCEID",INSTANCEID);
					map.put("IC",IC);
					map.put("PROJECTNO",PROJECTNO);
					
					map.put("JDMC",JDMC);
					map.put("TBR",TBR);
					map.put("TBSJ",TBSJ);
					map.put("JDZL",JDZL);
					map.put("NUM",NUM);

					l.add(map);
				}
				return l;
			}
		});
	}
	/**
	 * 判断是否指定用户查看所有的项目
	 * @return
	 */
	public  boolean checkUser(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		boolean flag=false;
		String users1=this.getConfigUUID("appointdd")==null?"":this.getConfigUUID("appointdd").toString();
		if(users1!=null && !"".equals(users1)){
			String users[]=users1.split(",");
			if(users!=null && users.length>0){
				for (int i = 0; i < users.length; i++) {
					if(users[i].equals(userid)){
						flag=true;
						break;
					}
				}
			}
		}
		return flag;
	}
	public List<HashMap> getRunList(int pageSize,int pageNumber,String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		String formid = "";
		String  demid = "";
		String  jdid = "";
		if("xsb".equals(type)){
			formid = this.getConfigUUID("dgzzformid")==null?"":this.getConfigUUID("dgzzformid");
			demid = this.getConfigUUID("dgzzdemid")==null?"":this.getConfigUUID("dgzzdemid");
			jdid = this.getConfigUUID("xmzzjdid")==null?"":this.getConfigUUID("xmzzjdid");
			sql.append("SELECT K.CUSTOMERNAME,TO_CHAR(1) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.GSGK,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,B.PROJECTNAME,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid,"+jdid+" jdid,B.PROJECTNO FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID  LEFT JOIN BD_ZQB_XMZZ T   ON T.EXTEND2 = B.PROJECTNO WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况') AND B.STATUS='执行中'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID = ? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR SUBSTR(B.ZCLR,0, instr(B.ZCLR,'[',1)-1)=?"
						+ " OR SUBSTR(B.DDAP,0, instr(B.DDAP,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMZH,0, instr(B.XMZH,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMXY,0, instr(B.XMXY,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMLS,0, instr(B.XMLS,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN(SELECT A.PROJECTNO FROM ("
						+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO" 
						+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')"
						+ " ) A INNER JOIN"
						+ " ("
						+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
						+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
						+ " AND B.USERID = ?"
						+ " ) B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
					
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.GSGK LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')");
				sql.append(" AND (B.ZCLR LIKE ?");
				sql.append(" OR B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ?");
				sql.append(" OR B.DDAP LIKE ?");
				sql.append(" OR B.XMZH LIKE ?");
				sql.append(" OR B.XMXY LIKE ?");
				sql.append(" OR B.XMLS LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-------------------------------------------
			formid = this.getConfigUUID("dxfxzzformid")==null?"":this.getConfigUUID("dxfxzzformid");
			demid = this.getConfigUUID("dxfxzzdemid")==null?"":this.getConfigUUID("dxfxzzdemid");
			jdid = this.getConfigUUID("dxzfjdid")==null?"":this.getConfigUUID("dxzfjdid");
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(2) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.GPFXJZ AS XMJD,B.PROJECTNAME,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid,"+jdid+" jdid,B.PROJECTNO FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN BD_ZQB_XMZZ T on  T.Projectno = B.PROJECTNO WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目') AND B.STATUS='执行中'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN("
						+ " SELECT A.PROJECTNO FROM ("
						
				+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ "  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')"
				+ " ) A INNER JOIN"
				+ "  ("
				+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ "  WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ "  AND B.USERID = ?"
				+ "  ) B ON A.INSTANCEID=B.INSTANCEID"
				
				+ "  )");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.GPFXJZ LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				sql.append("SELECT A.PROJECTNO FROM (");
				
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-------------------------
			
			formid = this.getConfigUUID("bgzzzzformid")==null?"":this.getConfigUUID("bgzzzzformid");
			demid = this.getConfigUUID("bgzzzzdemid")==null?"":this.getConfigUUID("bgzzzzdemid");
			jdid = this.getConfigUUID("dgczjdid")==null?"":this.getConfigUUID("dgczjdid");
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,TO_DATE(B.JDBH,'yyyy-MM-dd'),B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,K.CUSTOMERNAME PROJECTNAME,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid,"+jdid+" jdid,B.xmbh FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO");
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN BD_ZQB_XMZZ T   ON T.Xmbh = B.XMBH WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='1' AND B.COMPANYNAME='xsb'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or  replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','') in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-----------------------
			formid = this.getConfigUUID("ipozzformid")==null?"":this.getConfigUUID("ipozzformid");
			demid = this.getConfigUUID("ipozzdemid")==null?"":this.getConfigUUID("ipozzdemid");
			jdid = this.getConfigUUID("ipojdid")==null?"":this.getConfigUUID("ipojdid");
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.ENDDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,B.PROJECTNAME,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid,"+jdid+" jdid,B.PROJECTNO FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO");
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID  LEFT JOIN (SELECT T.* FROM BD_ZQB_IPOJD T INNER JOIN bd_zqb_tyxm_info Z ON Z.ID = T.JDBH WHERE Z.Jdmc = '项目终止' AND INSTANCEID IS NOT NULL) T ON T.XMBH = B.PROJECTNO   WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08='其他' AND B.A01='xsb'   ");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("ssgs".equals(type)){
			formid = this.getConfigUUID("bgzzzzformid")==null?"":this.getConfigUUID("bgzzzzformid");
			demid = this.getConfigUUID("bgzzzzdemid")==null?"":this.getConfigUUID("bgzzzzdemid");
			jdid = this.getConfigUUID("dgczjdid")==null?"":this.getConfigUUID("dgczjdid");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,TO_DATE(B.JDBH,'yyyy-MM-dd'),B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,K.CUSTOMERNAME PROJECTNAME,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid,"+jdid+" jdid,B.xmbh FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO");
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID  LEFT JOIN BD_ZQB_XMZZ T   ON T.Xmbh = B.XMBH WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='1' AND B.COMPANYNAME='ssgs'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','')  in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-----------------------
			//IPO 改制 辅导 增发 配股 非公开发行 可转债 其他
			formid = this.getConfigUUID("ipozzformid")==null?"":this.getConfigUUID("ipozzformid");
			demid = this.getConfigUUID("ipozzdemid")==null?"":this.getConfigUUID("ipozzdemid");
			jdid = this.getConfigUUID("ipojdid")==null?"":this.getConfigUUID("ipojdid");
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.ENDDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,B.PROJECTNAME,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid,"+jdid+" jdid,B.PROJECTNO FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO");
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN (SELECT T.* FROM BD_ZQB_IPOJD T INNER JOIN bd_zqb_tyxm_info Z ON Z.ID = T.JDBH WHERE Z.Jdmc = '项目终止' AND INSTANCEID IS NOT NULL) T ON T.XMBH = B.PROJECTNO  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中'  AND B.A08 IN('IPO','改制','辅导','增发','配股','非公开发行','可转债','其他') AND B.A01='ssgs'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("zq".equals(type)){
			formid = this.getConfigUUID("ipozzformid")==null?"":this.getConfigUUID("ipozzformid");
			demid = this.getConfigUUID("ipozzdemid")==null?"":this.getConfigUUID("ipozzdemid");
			jdid = this.getConfigUUID("ipojdid")==null?"":this.getConfigUUID("ipojdid");
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.ENDDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,B.PROJECTNAME,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid,"+jdid+" jdid,B.PROJECTNO FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO");
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN (SELECT T.* FROM BD_ZQB_IPOJD T INNER JOIN bd_zqb_tyxm_info Z ON Z.ID = T.JDBH WHERE Z.Jdmc = '项目终止' AND INSTANCEID IS NOT NULL) T ON T.XMBH = B.PROJECTNO  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中'  AND B.A08 IN('公司债','企业债','可交换债','其他') AND B.A01='zq'");
		
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}
		sql.append(") WHERE 1=1");
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND TAG=?");
			params.add(xmlx);
		}
		sql.append(" ORDER BY IC DESC");
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				int n=1;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[0];
					String tag = (String) object[1];
					String manager = (String) object[2];
					Date sdate = (Date) object[3];
					Date edate = (Date) object[4];
					String czbm = (String) object[5];
					BigDecimal instanceid = (BigDecimal) object[6];
					String xmjd = (String) object[8];
					
					String xmmc = (String) object[9];
					BigDecimal insid = (BigDecimal) object[10];
					BigDecimal formid = (BigDecimal) object[11];
					BigDecimal demid = (BigDecimal) object[12];
					BigDecimal jdid = (BigDecimal) object[13];
					String xmbh = (String) object[14];
					map.put("CUSTOMERNAME", customername);
					map.put("TAG", tag);
					map.put("MANAGER", manager);
					map.put("SDATE", sdate);
					map.put("EDATE", edate);
					map.put("CZBM", czbm);
					map.put("INSTANCEID", instanceid==null||instanceid.equals("")?"0":instanceid.toString());
					map.put("XMJD", xmjd);
					map.put("xmmc", xmmc);
					map.put("insid", insid==null||insid.equals("")?"0":insid.toString());
					map.put("formid", formid.toString());
					map.put("demid", demid.toString());
					map.put("jdid", jdid.toString());
					map.put("xmbh", xmbh);
					map.put("js", n);
					n++;
					l.add(map);
				}
				return l;
			}
		});
		
	}
	public List<HashMap> getRunListSize(String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		if("xsb".equals(type)){
			sql.append("SELECT K.CUSTOMERNAME,TO_CHAR(1) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.GSGK,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况') AND B.STATUS='执行中'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID = ? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR SUBSTR(B.ZCLR,0, instr(B.ZCLR,'[',1)-1)=?"
						+ " OR SUBSTR(B.DDAP,0, instr(B.DDAP,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMZH,0, instr(B.XMZH,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMXY,0, instr(B.XMXY,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMLS,0, instr(B.XMLS,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN(SELECT A.PROJECTNO FROM ("
						+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO" 
						+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')"
						+ " ) A INNER JOIN"
						+ " ("
						+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
						+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
						+ " AND B.USERID = ?"
						+ " ) B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.GSGK LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')");
				sql.append(" AND (B.ZCLR LIKE ?");
				sql.append(" OR B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ?");
				sql.append(" OR B.DDAP LIKE ?");
				sql.append(" OR B.XMZH LIKE ?");
				sql.append(" OR B.XMXY LIKE ?");
				sql.append(" OR B.XMLS LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-------------------------------------------
			
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(2) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.GPFXJZ AS XMJD FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目') AND B.STATUS='执行中'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN("
						+ " SELECT A.PROJECTNO FROM ("
						
				+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ "  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')"
				+ " ) A INNER JOIN"
				+ "  ("
				+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ "  WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ "  AND B.USERID = ?"
				+ "  ) B ON A.INSTANCEID=B.INSTANCEID"
				
				+ "  )");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.GPFXJZ LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				sql.append("SELECT A.PROJECTNO FROM (");
				
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-------------------------
			
			
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,B.XMFQRQ,B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='1' AND B.COMPANYNAME='xsb'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','') in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-----------------------
			
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08='其他' AND B.A01='xsb'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("ssgs".equals(type)){
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,B.XMFQRQ,B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='1' AND B.COMPANYNAME='ssgs'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or  replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','') in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-----------------------
			//IPO 改制 辅导 增发 配股 非公开发行 可转债 其他
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08 IN('IPO','改制','辅导','增发','配股','非公开发行','可转债','其他') AND B.A01='ssgs'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("zq".equals(type)){
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中' AND B.A08 IN('公司债','企业债','可交换债','其他') AND B.A01='zq'");
			if(orgroleid!=5L && !checkUser()){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}
		sql.append(") WHERE 1=1");
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND TAG=?");
			params.add(xmlx);
		}
		sql.append(" ORDER BY IC DESC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[0];
					String tag = (String) object[1];
					String manager = (String) object[2];
					Date sdate = (Date) object[3];
					Date edate = (Date) object[4];
					String czbm = (String) object[5];
					BigDecimal instanceid = (BigDecimal) object[6];
					String xmjd = (String) object[8];
					map.put("CUSTOMERNAME", customername);
					map.put("TAG", tag);
					map.put("MANAGER", manager);
					map.put("SDATE", sdate);
					map.put("EDATE", edate);
					map.put("CZBM", czbm);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					l.add(map);
				}
				return l;
			}
		});
		
	}
	
	public List<HashMap> getCloseList(int pageSize,int pageNumber,String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		boolean dgxgzt=false;
		 String rylb=this.getConfigUUID("dgxmxgzt");
		 if(rylb!=null && !"".equals(rylb)){
			 String[] ry=rylb.split(",");
			 for (int i = 0; i < ry.length; i++) {
				if(ry[i].equals(uc.get_userModel().getUserid())){
					dgxgzt=true;
					break;
				}
			}
		 }
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		String formid = "";
		String  demid = "";
		sql.append("SELECT * FROM (");
		if("xsb".equals(type)){
			formid = this.getConfigUUID("dgzzformid")==null?"":this.getConfigUUID("dgzzformid");    
			demid = this.getConfigUUID("dgzzdemid")==null?"":this.getConfigUUID("dgzzdemid"); 
			sql.append("SELECT B.PROJECTNAME CUSTOMERNAME,TO_CHAR(1) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.GSGK,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,   B.STARTDATE  CZSJ, T.ZZSJ, T.ZZYY,T.EXTEND3,T.EXTEND4,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID  LEFT JOIN BD_ZQB_XMZZ T ON T.EXTEND2=B.PROJECTNO  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况') AND B.STATUS='已完成'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID = ? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR SUBSTR(B.ZCLR,0, instr(B.ZCLR,'[',1)-1)=?"
						+ " OR SUBSTR(B.DDAP,0, instr(B.DDAP,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMZH,0, instr(B.XMZH,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMXY,0, instr(B.XMXY,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMLS,0, instr(B.XMLS,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN(SELECT A.PROJECTNO FROM ("
						+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO" 
						+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')"
						+ " ) A INNER JOIN"
						+ " ("
						+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
						+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
						+ " AND B.USERID = ?"
						+ " ) B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.GSGK LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')");
				sql.append(" AND (B.ZCLR LIKE ?");
				sql.append(" OR B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ?");
				sql.append(" OR B.DDAP LIKE ?");
				sql.append(" OR B.XMZH LIKE ?");
				sql.append(" OR B.XMXY LIKE ?");
				sql.append(" OR B.XMLS LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-------------------------------------------
			formid = this.getConfigUUID("dxfxzzformid")==null?"":this.getConfigUUID("dxfxzzformid");
			demid = this.getConfigUUID("dxfxzzdemid")==null?"":this.getConfigUUID("dxfxzzdemid"); 
			sql.append(" UNION ALL");
			sql.append(" SELECT B.PROJECTNAME CUSTOMERNAME,TO_CHAR(2) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.GPFXJZ AS XMJD  ,B.STARTDATE CZSJ, T.ZZSJ, T.ZZYY,T.EXTEND3,T.EXTEND4,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN BD_ZQB_XMZZ T ON T.Projectno=B.PROJECTNO WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目') AND B.STATUS='已完成'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN("
						+ " SELECT A.PROJECTNO FROM ("
						
				+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ "  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')"
				+ " ) A INNER JOIN"
				+ "  ("
				+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ "  WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ "  AND B.USERID = ?"
				+ "  ) B ON A.INSTANCEID=B.INSTANCEID"
				
				+ "  )");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.GPFXJZ LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				sql.append("SELECT A.PROJECTNO FROM (");
				
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-------------------------
			
			formid = this.getConfigUUID("bgzzzzformid")==null?"":this.getConfigUUID("bgzzzzformid");
			demid = this.getConfigUUID("bgzzzzdemid")==null?"":this.getConfigUUID("bgzzzzdemid");
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,B.XMFQRQ,B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD ,to_DATE(B.JDBH,'YYYY-MM-DD') CZSJ , T.ZZSJ, T.ZZYY,T.EXTEND3,T.EXTEND4 ,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN BD_ZQB_XMZZ T ON T.Xmbh=B.XMBH WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='0' AND B.COMPANYNAME='xsb'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','') in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-----------------------
			formid = this.getConfigUUID("ipozzformid")==null?"":this.getConfigUUID("ipozzformid");
			demid = this.getConfigUUID("ipozzdemid")==null?"":this.getConfigUUID("ipozzdemid");  
			sql.append(" UNION ALL");
			sql.append(" SELECT  B.PROJECTNAME CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD ,B.ENDDATE CZSJ,T.ZZSJ, T.JCXYYY ZZYY,T.FD4 EXTEND3,T.FD5 EXTEND4 ,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN (SELECT T.* FROM BD_ZQB_IPOJD T INNER JOIN bd_zqb_tyxm_info Z ON Z.ID = T.JDBH WHERE Z.Jdmc = '项目终止' AND INSTANCEID IS NOT NULL) T ON T.XMBH = B.PROJECTNO  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='已完成'  AND B.A08='其他' AND B.A01='xsb'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("ssgs".equals(type)){
			formid = this.getConfigUUID("bgzzzzformid")==null?"":this.getConfigUUID("bgzzzzformid");
			demid = this.getConfigUUID("bgzzzzdemid")==null?"":this.getConfigUUID("bgzzzzdemid");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,B.XMFQRQ,B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,to_DATE(B.JDBH,'YYYY-MM-DD') CZSJ , T.ZZSJ, T.ZZYY,T.EXTEND3,T.EXTEND4 ,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN BD_ZQB_XMZZ T ON T.Xmbh=B.XMBH WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='0' AND B.COMPANYNAME='ssgs'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or  replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','') in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-----------------------
			//IPO 改制 辅导 增发 配股 非公开发行 可转债 其他
			formid = this.getConfigUUID("ipozzformid")==null?"":this.getConfigUUID("ipozzformid");
			demid = this.getConfigUUID("ipozzdemid")==null?"":this.getConfigUUID("ipozzdemid");  
			sql.append(" UNION ALL");
			sql.append(" SELECT  B.PROJECTNAME CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,B.ENDDATE CZSJ,T.ZZSJ, T.JCXYYY ZZYY,T.FD4 EXTEND3,T.FD5 EXTEND4,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID  LEFT JOIN (SELECT T.* FROM BD_ZQB_IPOJD T INNER JOIN bd_zqb_tyxm_info Z ON Z.ID = T.JDBH WHERE Z.Jdmc = '项目终止' AND INSTANCEID IS NOT NULL) T ON T.XMBH = B.PROJECTNO  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='已完成'  AND B.A08 IN('IPO','改制','辅导','增发','配股','非公开发行','可转债','其他') AND B.A01='ssgs'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("zq".equals(type)){
			formid = this.getConfigUUID("ipozzformid")==null?"":this.getConfigUUID("ipozzformid");
			demid = this.getConfigUUID("ipozzdemid")==null?"":this.getConfigUUID("ipozzdemid");  
			sql.append(" SELECT  B.PROJECTNAME CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD,B.ENDDATE CZSJ,T.ZZSJ, T.JCXYYY ZZYY,T.FD4 EXTEND3,T.FD5 EXTEND4 ,T.INSTANCEID insid,"+formid+" formid,"+demid+" demid FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID LEFT JOIN (SELECT T.* FROM BD_ZQB_IPOJD T INNER JOIN bd_zqb_tyxm_info Z ON Z.ID = T.JDBH WHERE Z.Jdmc = '项目终止' AND INSTANCEID IS NOT NULL) T ON T.XMBH = B.PROJECTNO WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='已完成'  AND B.A08 IN('公司债','企业债','可交换债','其他') AND B.A01='zq'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}
		sql.append(") WHERE 1=1");
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND TAG=?");
			params.add(xmlx);
		}
		sql.append(" ORDER BY IC DESC");
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[0];
					String tag = (String) object[1];
					String manager = (String) object[2];
					Date sdate = (Date) object[3];
					Date edate = (Date) object[4];
					String czbm = (String) object[5];
					BigDecimal instanceid = (BigDecimal) object[6];
					String xmjd = (String) object[8];
					Date czsj = (Date) object[9];
					Date zzsj = (Date) object[10];
					String zzyy = (String) object[11];
					String ext3 = (String) object[12];
					String ext4 = (String) object[13];
					BigDecimal insid = (BigDecimal) object[14];
					BigDecimal formid = (BigDecimal) object[15];
					BigDecimal demid = (BigDecimal) object[16];
					map.put("CUSTOMERNAME", customername);
					map.put("TAG", tag);
					map.put("MANAGER", manager);
					map.put("SDATE", sdate);
					map.put("EDATE", edate);
					map.put("CZBM", czbm);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("CZSJ", czsj);
					map.put("ZZSJ", zzsj);
					map.put("ZZYY", zzyy);
					map.put("EXT3", ext3);
					map.put("EXT4", ext4);
					map.put("insid", insid);
					map.put("formid", formid);
					map.put("demid", demid);
					l.add(map);
				}
				return l;
			}
		});
		
	}
	public List<HashMap> getCloseListSize(String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		boolean dgxgzt=false;
		 String rylb=this.getConfigUUID("dgxmxgzt");
		 if(rylb!=null && !"".equals(rylb)){
			 String[] ry=rylb.split(",");
			 for (int i = 0; i < ry.length; i++) {
				if(ry[i].equals(uc.get_userModel().getUserid())){
					dgxgzt=true;
					break;
				}
			}
		 }
		String userid = uc._userModel.getUserid();
		Long orgroleid = uc._userModel.getOrgroleid();
		Long isManager = uc._userModel.getIsmanager();
		String departmentName =uc._userModel.getDepartmentname();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		if("xsb".equals(type)){
			sql.append("SELECT K.CUSTOMERNAME,TO_CHAR(1) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.GSGK,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况') AND B.STATUS='已完成'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID = ? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR SUBSTR(B.ZCLR,0, instr(B.ZCLR,'[',1)-1)=?"
						+ " OR SUBSTR(B.DDAP,0, instr(B.DDAP,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMZH,0, instr(B.XMZH,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMXY,0, instr(B.XMXY,'[',1)-1)=?"
						+ " OR SUBSTR(B.XMLS,0, instr(B.XMLS,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN(SELECT A.PROJECTNO FROM ("
						+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO" 
						+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')"
						+ " ) A INNER JOIN"
						+ " ("
						+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
						+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
						+ " AND B.USERID = ?"
						+ " ) B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.GSGK LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')");
				sql.append(" AND (B.ZCLR LIKE ?");
				sql.append(" OR B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ?");
				sql.append(" OR B.DDAP LIKE ?");
				sql.append(" OR B.XMZH LIKE ?");
				sql.append(" OR B.XMXY LIKE ?");
				sql.append(" OR B.XMLS LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-------------------------------------------
			
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(2) AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.GPFXJZ AS XMJD FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目') AND B.STATUS='已完成'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.PROJECTNO IN("
						+ " SELECT A.PROJECTNO FROM ("
						
				+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ "  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')"
				+ " ) A INNER JOIN"
				+ "  ("
				+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ "  WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ "  AND B.USERID = ?"
				+ "  ) B ON A.INSTANCEID=B.INSTANCEID"
				
				+ "  )");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.GPFXJZ LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN(");
				sql.append("SELECT A.PROJECTNO FROM (");
				
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-------------------------
			
			
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,B.XMFQRQ,B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='0' AND B.COMPANYNAME='xsb'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or   replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','') in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			
			//-----------------------
			
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='已完成' AND B.A08='其他' AND B.A01='xsb'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("ssgs".equals(type)){
			sql.append(" SELECT K.CUSTOMERNAME,TO_CHAR(3) AS TAG,B.MANAGER,B.XMFQRQ,B.TPRQ,B.CZBM,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='0' AND B.COMPANYNAME='ssgs'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
						
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add("["+userid+"]");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or   replace(SUBSTR(b.tbrid,instr(b.tbrid, '[', 1)+1),']','') in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.CZBM LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.XMBH IN (");
				sql.append("SELECT A.XMBH FROM (");
				
				sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A"); 
				
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
			//-----------------------
			//IPO 改制 辅导 增发 配股 非公开发行 可转债 其他
			sql.append(" UNION ALL");
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='已完成' AND B.A08 IN('IPO','改制','辅导','增发','配股','非公开发行','可转债','其他') AND B.A01='ssgs'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}else if("zq".equals(type)){
			sql.append(" SELECT K.CUSTOMERNAME,B.A08 AS TAG,B.MANAGER,B.STARTDATE,B.ENDDATE,B.SSSYB,S.INSTANCEID,I.CREATEDATE AS IC,B.XMJD FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='已完成' AND B.A08 IN('公司债','企业债','可交换债','其他') AND B.A01='zq'");
			if(orgroleid!=5L && !checkUser() && !dgxgzt){
				sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
						+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
						
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)");
				params.add(userid);
				params.add(userid);
				params.add(userid);
				params.add(userid);
				if(isManager==1l){
					sql.append("  or b.createuserid in ( select s.userid from orguser s where s.departmentid in (  select t.id from orgdepartment t start with t.id="+uc.get_userModel().getDepartmentid()+" connect by prior t.id=t.parentdepartmentid) ) ");
				}
				sql.append(")");
			}
			
			if(customername!=null&&!customername.equals("")){
				sql.append(" AND K.CUSTOMERNAME LIKE ?");
				params.add("%"+customername+"%");
			}
			if(sssyb!=null&&!sssyb.equals("")){
				sql.append(" AND B.SSSYB LIKE ?");
				params.add("%"+sssyb+"%");
			}
			if(xmjd!=null&&!xmjd.equals("")){
				sql.append(" AND B.XMJD LIKE ?");
				params.add("%"+xmjd+"%");
			}
			if(ssxq!=null&&!ssxq.equals("")){
				sql.append(" AND B.ssxq LIKE ?");
				params.add("%"+ssxq+"%");
			}
			if(cyrName!=null&&!cyrName.equals("")){
				sql.append(" AND B.PROJECTNO IN (");
				sql.append("SELECT A.PROJECTNO FROM (");
				sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
				sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
				sql.append(" AND (B.OWNER LIKE ?");
				sql.append(" OR B.MANAGER LIKE ? OR S.INSTANCEID IN(SELECT S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
				sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
				sql.append(" AND B.NAME LIKE ?))");
				sql.append(") A");
				sql.append(" )");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
				params.add("%"+cyrName+"%");
			}
		}
		sql.append(") WHERE 1=1");
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND TAG=?");
			params.add(xmlx);
		}
		sql.append(" ORDER BY IC DESC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[0];
					String tag = (String) object[1];
					String manager = (String) object[2];
					Date sdate = (Date) object[3];
					Date edate = (Date) object[4];
					String czbm = (String) object[5];
					BigDecimal instanceid = (BigDecimal) object[6];
					String xmjd = (String) object[8];
					map.put("CUSTOMERNAME", customername);
					map.put("TAG", tag);
					map.put("MANAGER", manager);
					map.put("SDATE", sdate);
					map.put("EDATE", edate);
					map.put("CZBM", czbm);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					l.add(map);
				}
				return l;
			}
		});
		
	}
	
	public List<HashMap> getPjList(String customername,String sssyb,String cyrName,String xmlx){
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		Long orgroleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM (");
		sql.append("SELECT CAST('SUBFORM_XMCYLBDG' AS VARCHAR(32)) AS XMCY,CAST('SUBFORM_XMZJJG' AS VARCHAR(32)) AS JGKEY,S.INSTANCEID,B.PROJECTNAME,CAST('股转挂牌' AS VARCHAR(32)) AS TYPE,B.CREATEDATE,B.XMJD,B.GSGK,B.ZCLR,B.OWNER,B.MANAGER,B.DDAP,B.XMZH,B.XMXY,B.XMLS,SB.TASKID,SB.LCBS,SB.CUSTOMERNAME,ZS.DDAP AS ZDAP,ZS.TJRXM,I.CREATEDATE AS IC,TO_CHAR(1) AS TAG FROM BD_ZQB_PJ_BASE B LEFT JOIN BD_ZQB_SQNH SB ON B.PROJECTNO=SB.EXTEND2 LEFT JOIN BD_ZQB_GPDJJGD ZS ON B.PROJECTNO=ZS.EXTEND2 LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况') AND B.STATUS='执行中'");
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID = ? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
					+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
					+ " OR SUBSTR(B.ZCLR,0, instr(B.ZCLR,'[',1)-1)=?"
					+ " OR SUBSTR(B.DDAP,0, instr(B.DDAP,'[',1)-1)=?"
					+ " OR SUBSTR(B.XMZH,0, instr(B.XMZH,'[',1)-1)=?"
					+ " OR SUBSTR(B.XMXY,0, instr(B.XMXY,'[',1)-1)=?"
					+ " OR SUBSTR(B.XMLS,0, instr(B.XMLS,'[',1)-1)=?"
					+ " OR B.PROJECTNO IN(SELECT A.PROJECTNO FROM ("
					+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO" 
					+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')"
					+ " ) A INNER JOIN"
					+ " ("
					+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
					+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
					+ " AND B.USERID = ?"
					+ " ) B ON A.INSTANCEID=B.INSTANCEID)"
					+ ")");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
		}
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.GSGK LIKE ?");
			params.add("%"+sssyb+"%");
		}
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN(");
			
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_PJ_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目基本情况')");
			sql.append(" AND (B.ZCLR LIKE ?");
			sql.append(" OR B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?");
			sql.append(" OR B.DDAP LIKE ?");
			sql.append(" OR B.XMZH LIKE ?");
			sql.append(" OR B.XMXY LIKE ?");
			sql.append(" OR B.XMLS LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}

		//-------------------------------------------

		sql.append(" UNION ALL");
		sql.append(" SELECT CAST('SUBFORM_DGXMCYLB' AS VARCHAR(32)) AS XMCY,CAST('SUBFORM_DGXMZJJG' AS VARCHAR(32)) AS JGKEY,S.INSTANCEID,B.PROJECTNAME,CAST('股转定增' AS VARCHAR(32)) AS TYPE,B.STARTDATE,B.GPFXJZ,B.CZBM,B.OWNER,B.MANAGER,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,I.CREATEDATE AS IC,TO_CHAR(2) AS TAG FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目') AND B.STATUS='执行中'");
		if(orgroleid!=5L && !checkUser()){
		sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
				+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
				+ " OR B.PROJECTNO IN("
				+ " SELECT A.PROJECTNO FROM ("
					
				+ " SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ "  WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')"
				+ " ) A INNER JOIN"
				+ "  ("
				+ " SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ "  WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ "  AND B.USERID = ?"
				+ "  ) B ON A.INSTANCEID=B.INSTANCEID"
					
				+ "  )"
				+ ")");
		params.add(userid);
		params.add(userid);
		params.add(userid);
		params.add(userid);
		}
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.CZBM LIKE ?");
			params.add("%"+sssyb+"%");
		}
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN(");
			sql.append("SELECT A.PROJECTNO FROM (");
			
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_GPFXXMB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向增发项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN"); 
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(" ) B ON A.INSTANCEID=B.INSTANCEID");
			
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		//-------------------------


		sql.append(" UNION ALL");
		sql.append(" SELECT CAST('SUBFORM_DGXMCYLB' AS VARCHAR(32)) AS XMCY,CAST('SUBFORM_DGXMZJJG' AS VARCHAR(32)) AS JGKEY,S.INSTANCEID,B.JYF,CAST('股转并购' AS VARCHAR(32)) AS TYPE,B.XMFQRQ,B.XMJD,B.CZBM,B.OWNER,B.MANAGER,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,I.CREATEDATE AS IC,TO_CHAR(3) AS TAG FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目') AND B.XMZT='1'");
		if(orgroleid!=5L && !checkUser()){
		sql.append(" AND (SUBSTR(B.TBRID,INSTR(B.TBRID,'[',1), INSTR(B.TBRID,']',1)-1)=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=? OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
				+ " OR B.XMBH IN(SELECT A.XMBH FROM ("
			
				+ "SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')"
				+ ") A INNER JOIN"
				
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID))");
		params.add("["+userid+"]");
		params.add(userid);
		params.add(userid);
		params.add(userid);
		}
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.CZBM LIKE ?");
			params.add("%"+sssyb+"%");
		}
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.XMBH IN (");
			sql.append("SELECT A.XMBH FROM (");
			
			sql.append("SELECT B.XMBH,S.INSTANCEID FROM BD_ZQB_BGZZLXXX B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.COMPANYNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购重组项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN"); 
			
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}

		//-----------------------

		sql.append(" UNION ALL");
		sql.append(" SELECT CAST('SUBFORM_IPOXMCYR' AS VARCHAR(32)) AS XMCY,CAST('SUBFORM_IPOXMZJJG' AS VARCHAR(32)) AS JGKEY,S.INSTANCEID,B.PROJECTNAME,B.A08 AS TYPE,B.STARTDATE,B.XMJD,B.SSSYB,B.OWNER,B.MANAGER,JD.FDCY,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,I.CREATEDATE AS IC,B.A08 AS TAG FROM BD_ZQB_TYXM B LEFT JOIN BD_ZQB_IPOJD JD ON B.PROJECTNO=JD.XMBH LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
		sql.append(" LEFT JOIN SYS_INSTANCE_DATA I ON S.INSTANCEID=I.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目') AND B.STATUS='执行中'");
		if(orgroleid!=5L && !checkUser()){
			sql.append(" AND (B.CREATEUSERID=? OR SUBSTR(B.OWNER,0, instr(B.OWNER,'[',1)-1)=?"
				+ " OR SUBSTR(B.MANAGER,0, instr(B.MANAGER,'[',1)-1)=?"
				
				+ " OR PROJECTNO IN(SELECT A.PROJECTNO FROM ("
				+ "SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"
				+ " WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')"
				+ ") A INNER JOIN"
				+ " ("
				+ "SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID" 
				+ " WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')"
				+ " AND B.USERID = ?"
				+ ") B ON A.INSTANCEID=B.INSTANCEID)"
				
				+ ")");
			params.add(userid);
			params.add(userid);
			params.add(userid);
			params.add(userid);
		}
		
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND K.CUSTOMERNAME LIKE ?");
			params.add("%"+customername+"%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sql.append(" AND B.SSSYB LIKE ?");
			params.add("%"+sssyb+"%");
		}
		if(cyrName!=null&&!cyrName.equals("")){
			sql.append(" AND B.PROJECTNO IN (");
			sql.append("SELECT A.PROJECTNO FROM (");
			sql.append("SELECT B.PROJECTNO,S.INSTANCEID FROM BD_ZQB_TYXM B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO"); 
			sql.append(" WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目')");
			sql.append(" AND (B.OWNER LIKE ?");
			sql.append(" OR B.MANAGER LIKE ?)");
			sql.append(") A LEFT JOIN");
			sql.append(" (");
			sql.append("SELECT B.NAME,S.INSTANCEID FROM BD_ZQB_GROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID"); 
			sql.append(" WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目成员列表')");
			sql.append(" AND B.NAME LIKE ?");
			sql.append(") B ON A.INSTANCEID=B.INSTANCEID");
			sql.append(" )");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
			params.add("%"+cyrName+"%");
		}
		sql.append(") WHERE 1=1");
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND TAG=?");
			params.add(xmlx);
		}
		sql.append(" ORDER BY IC DESC");
		//获取中介机构DemAPI.getInstance().getFromSubData(0l, "");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String XMCY = (String) object[0];
					String JGKEY = (String) object[1];
					BigDecimal INSTANCEID = (BigDecimal) object[2];
					String PROJECTNAME = (String) object[3];
					String TYPE = (String) object[4];
					Date CREATEDATE = (Date) object[5];
					String XMJD = (String) object[6];
					String GSGK = (String) object[7];
					String ZCLR = (String) object[8];
					String OWNER = (String) object[9];
					String MANAGER = (String) object[10];
					String DDAP = (String) object[11];
					String XMZH = (String) object[12];
					String XMXY = (String) object[13];
					String XMLS = (String) object[14];
					BigDecimal TASKID = (BigDecimal) object[15];
					BigDecimal LCBS = (BigDecimal) object[16];
					String CUSTOMERNAME = (String) object[17];
					String DDAP_ = (String) object[18];
					String TJRXM = (String) object[19];
					map.put("XMCY", XMCY);
					map.put("JGKEY", JGKEY);
					map.put("INSTANCEID", INSTANCEID);
					map.put("PROJECTNAME", PROJECTNAME);
					map.put("TYPE", TYPE);
					map.put("CREATEDATE", CREATEDATE);
					map.put("XMJD", XMJD);
					map.put("GSGK", GSGK);
					map.put("ZCLR", ZCLR);
					map.put("OWNER", OWNER);
					map.put("MANAGER", MANAGER);
					map.put("DDAP", DDAP);
					map.put("XMZH", XMZH);
					map.put("XMXY", XMXY);
					map.put("XMLS", XMLS);
					map.put("TASKID", TASKID);
					map.put("LCBS", LCBS);
					map.put("CUSTOMERNAME", CUSTOMERNAME);
					map.put("DDAP_", DDAP_);
					map.put("TJRXM", TJRXM);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getJdxx(String projectno){
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT A.ID AS GOUPID,A.JDMC,A.SXZLQD AS ZLMB,B.* FROM BD_ZQB_TYXM_INFO A LEFT JOIN (");

		sql.append(" SELECT * FROM (");
		sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.TJRXM AS JDZL,B.TBR||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_XMLXLCB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目立项')");
		sql.append(" UNION");
		sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_LCGG B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股改')");
		sql.append(" UNION");
		sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_SQNH B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='申报')");
		sql.append(" UNION");
		sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_NHFK B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='反馈')");
		sql.append(" UNION");
		sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_GZFKJHF B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='同意挂牌')");
		sql.append(" UNION");
		sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.GPZL AS JDZL,B.EXTEND5||'['||B.TBRID||']' AS TBR,B.TBSJ FROM BD_ZQB_GPDJJGD B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='正式挂牌')");
		sql.append(" UNION");
		sql.append(" SELECT S.INSTANCEID,B.EXTEND2 AS PROJECTNO,B.EXTEND1 AS JDID,B.FJ AS JDZL,B.USERNAME||'['||B.USERID||']' AS TBR,B.ZZSJ FROM BD_ZQB_XMZZ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目终止')");
		sql.append(" UNION");
		sql.append(" SELECT S.INSTANCEID,B.Projectno AS PROJECTNO,B.Groupid AS JDID,B.Jdzl AS JDZL,B.Manager||'['||B.Sxyzlmb||']' AS TBR,B.Gxsj FROM BD_ZQB_GPFXXMRWLCB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目组工作记录') ");
		sql.append(" UNION");		
		sql.append(" SELECT S.INSTANCEID,B.Projectno AS PROJECTNO,B.Groupid AS JDID,B.Attach AS JDZL,B.Manager||'['||B.Jdzl||']' AS TBR,B.Gxsj FROM BD_ZQB_GPFXXMRWB B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他文件') ");
		sql.append(") WHERE PROJECTNO=?");
		params.add(projectno);

		sql.append(" ) B ON A.ID=B.JDID WHERE A.XMLX='东莞-推荐挂牌' AND A.STATE=1 ORDER BY A.SORTID");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal groupid = (BigDecimal) object[0];
					String jdmc = (String) object[1];
					String zlmb = (String) object[2];
					BigDecimal instanceid = (BigDecimal) object[3];
					String projectno = (String) object[4];
					String jdid = (String) object[5];
					String jdzl = (String) object[6];
					String tbr = (String) object[7];
					String tbsj = (Date) object[8]==null?"":sdf.format((Date) object[8]);
					map.put("GROUPID", groupid);
					map.put("JDMC", jdmc);
					map.put("ZLMB", zlmb);
					map.put("INSTANCEID", instanceid);
					map.put("PROJECTNO", projectno);
					map.put("JDID", jdid);
					map.put("JDZL", jdzl);
					map.put("TBR", tbr);
					map.put("TBSJ", tbsj);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getCustomerByCustomerno(String customerno) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT GFGSRQ,GB,USERNAME,TEL,TYPE,ZYYW,QTSM FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?");
		final String sql1 = sql.toString();
		final String final_customerno = customerno;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				
					if(final_customerno!=null && !"".equals(final_customerno)){
						
						if(d.HasInjectionData(final_customerno)){
							return l;
						}
						}
					
				query.setString(0, final_customerno);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					Date gfgsrq = (Date) object[0];
					String gb = (String) object[1];
					String username = (String) object[2];
					String tel = (String) object[3];
					String type = (String) object[4];
					String zyyw = (String) object[5];
					String qtsm = (String) object[6];
					map.put("GFGSRQ", gfgsrq);
					map.put("GB", gb);
					map.put("USERNAME", username);
					map.put("TEL", tel);
					map.put("TYPE", type);
					map.put("ZYYW", zyyw);
					map.put("QTSM", qtsm);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getCusMsgByProno(String projectno) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT K.USERNAME,B.KHLXDH TEL FROM BD_ZQB_GPFXXMB B LEFT JOIN BD_ZQB_KH_BASE K ON B.CUSTOMERNO=K.CUSTOMERNO WHERE B.PROJECTNO=?");
		params.add(projectno);
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String username = (String) object[0];
					String tel = (String) object[1];
					map.put("USERNAME", username);
					map.put("TEL", tel);
					l.add(map);
				}
				return l;
			}
		});
	}
	public String getCusIDFIdByCusno(String customerno){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT S.INSTANCEID||'-'||D.ID||'-'||D.FORMID IDFID FROM BD_ZQB_KH_BASE B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID LEFT JOIN SYS_DEM_ENGINE D ON S.FORMID=D.FORMID WHERE S.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE ='客户主数据维护') AND B.CUSTOMERNO=?");
		Map params = new HashMap();
		params.put(1, customerno);
		return com.iwork.commons.util.DBUtil.getDataStr("IDFID", sql.toString(), params);
	}
	public List<HashMap> getProjectByProjectno(String projectno) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PROJECTNAME,MANAGER FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=?");
		final String sql1 = sql.toString();
		final String final_projectno = projectno;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				
					if(final_projectno!=null && !"".equals(final_projectno)){
						
						if(d.HasInjectionData(final_projectno)){
							return l;
						}
						}
					
				query.setString(0, final_projectno);
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String projectname = (String) object[0];
					String manager = (String) object[1];
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getProjectByIPOPro(String projectno) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PROJECTNAME,MANAGER,CUSTOMERNO FROM BD_ZQB_TYXM WHERE PROJECTNO=?");
		final String sql1 = sql.toString();
		final String final_projectno = projectno;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				
					if(final_projectno!=null && !"".equals(final_projectno)){
						
						if(d.HasInjectionData(final_projectno)){
							return l;
						}
						}
				query.setString(0, final_projectno);
				
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String projectname = (String) object[0];
					String manager = (String) object[1];
					String customerno = (String) object[2];
					map.put("PROJECTNAME", projectname);
					map.put("MANAGER", manager);
					map.put("CUSTOMERNO", customerno);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap<String, Object>> getTaskList(String sql,String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			ps.setString(1, xmbh);
			ps.setString(2, xmbh);
			ps.setString(3, xmbh);
			ps.setString(4, xmbh);
			ps.setString(5, xmbh);
			ps.setString(6, xmbh);
			ps.setString(7, xmbh);
			ps.setString(8, xmbh);
			ps.setString(9, xmbh);
			ps.setString(10, xmbh);
			ps.setString(11, xmbh);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String tbr = rs.getString("TBR");
				String tbsj = rs.getDate("TBSJ")==null?"":sdf.format(rs.getDate("TBSJ"));
				String xm_bh = rs.getString("XMBH");
				String sxzlqd = rs.getString("SXZLQD")==null?"":rs.getString("SXZLQD");
				String fj = rs.getString("FJ")==null?"":rs.getString("FJ");
				Long instanceid = rs.getLong("INSTANCEID");
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("TBR", tbr);
				result.put("TBSJ", tbsj);
				result.put("XMBH", xm_bh);
				result.put("MB", getFileHtml(sxzlqd).toString());
				result.put("FJ", getFileHtml(fj).toString());
				result.put("INSTANCEID", instanceid);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap<String, Object>> getIpoList(String sql,String xmbh) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 1; i <= 19; i++) {
				ps.setString(i, xmbh);	
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String jdmc = rs.getString("JDMC");
				String tbr = rs.getString("TBR");
				String cjsj = rs.getDate("CJSJ")==null?"":sdf.format(rs.getDate("CJSJ"));
				String xm_bh = rs.getString("XMBH");
				String sxzlqd = rs.getString("SXZLQD")==null?"":rs.getString("SXZLQD");
				String fj = rs.getString("FJ")==null?"":rs.getString("FJ");
				Long instanceid = rs.getLong("INSTANCEID");
				result.put("ID", id);
				result.put("JDMC", jdmc);
				result.put("TBR", tbr);
				result.put("CJSJ", cjsj);
				result.put("XMBH", xm_bh);
				result.put("MB", getFileHtml(sxzlqd).toString());
				result.put("FJ", getFileHtml(fj).toString());
				result.put("INSTANCEID", instanceid);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	private StringBuffer getFileHtml(String fj) {
		List<FileUpload> l;
		StringBuffer content = new StringBuffer("");
		if(!fj.equals("")){
			l = FileUploadAPI.getInstance().getFileUploads(fj);
			if(l!=null){
				for (int i = 0; i < l.size(); i++) {
					if(i==0){
						content.append("<a title=\""+l.get(i).getFileSrcName()+"\" target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+l.get(i).getFileId()+"\">"+l.get(i).getFileSrcName()+"</a>");
					}else{
						content.append("</br><a title=\""+l.get(i).getFileSrcName()+"\" target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+l.get(i).getFileId()+"\">"+l.get(i).getFileSrcName()+"</a>");
					}
				}
			}
		}
		return content;
	}
	
	public List<HashMap> getQjlcListByUserid(int pageSize, int pageNumber, String username, String departmentname, String workstatus, String userid) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		sql.append("SELECT CASE WHEN B.INSTANCEID IS NULL THEN 0 ELSE B.INSTANCEID END INSTANCEID,O.ID,O.USERID,O.USERNAME,O.DEPARTMENTNAME,B.TASK_NAME,B.STARTDATE,B.ENDDATE,B.SCALE,B.ATTACH,B.MEMO,");
		sql.append("B.MANAGER,B.GROUPID,B.PROJECTNO,B.PROJECTNAME,B.ORDERINDEX,B.PRIORITY,B.HTJE,B.SSJE,B.JDZL,B.GXSJ,B.SXYZLMB,B.LCBH,B.LZJD,B.SPZT,B.LCBS,B.LZWZ,B.RWID,");
		sql.append("Z.DUTIER,O.MOBILE,O.EMAIL FROM ORGUSER O LEFT JOIN (SELECT * FROM BD_ZQB_XMRWGLLCB WHERE PRIORITY=1) B ON O.USERID=B.PROJECTNO  LEFT JOIN SYS_INSTANCE_DATA D ON B.INSTANCEID=D.ID");

		sql.append(" LEFT JOIN (SELECT O.DEPARTMENTID, to_char(WMSYS.WM_CONCAT(O.USERNAME||' '||O.MOBILE)) DUTIER FROM ORGUSER O WHERE O.ISMANAGER=1 GROUP BY O.DEPARTMENTID) Z ON O.DEPARTMENTID=Z.DEPARTMENTID");
		
		sql.append(" WHERE (D.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='请假流程单') OR D.ID IS NULL) AND O.ORGROLEID<>3 AND O.ID<>1");
		
		if(userid!=null&&!userid.equals("")){
		sql.append(" AND B.PROJECTNO = ?");
		params.add(userid);
		}
		if(username!=null&&!username.equals("")){
		sql.append(" AND O.USERNAME LIKE ?");
		params.add("%"+username+"%");
		}
		if(departmentname!=null&&!departmentname.equals("")){
		sql.append(" AND O.DEPARTMENTNAME LIKE ?");
		params.add("%"+departmentname+"%");
		}
		if(workstatus!=null&&!workstatus.equals("")){
			if(workstatus.equals("离岗")){
				sql.append(" AND B.SPZT='审批通过' AND B.TASK_NAME IS NULL");
			}else{
				sql.append(" AND (B.SPZT='审批中' OR B.SPZT='驳回' OR B.SPZT IS NULL OR B.TASK_NAME IS NOT NULL)");
			}
		}
		sql.append(")");
		sql.append(" ORDER BY INSTANCEID DESC");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				String cuserid = uc.get_userModel().getUserid();
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal INSTANCEID=(BigDecimal)object[0];
					BigDecimal ID=(BigDecimal)object[1];
					String USERID=(String)object[2];
					String USERNAME=(String)object[3];
					String DEPARTMENTNAME=(String)object[4];
					String TASK_NAME=(String)object[5];
					Date STARTDATE=(Date)object[6];
					Date ENDDATE=(Date)object[7];
					String SCALE=(String)object[8];
					Date ATTACH=(Date)object[9];
					String MEMO=(String)object[10];
					String MANAGER=(String)object[11];
					String GROUPID=(String)object[12];
					String PROJECTNO=(String)object[13];
					String PROJECTNAME=(String)object[14];
					BigDecimal ORDERINDEX=(BigDecimal)object[15];
					String PRIORITY=(String)object[16];
					String HTJE=(String)object[17];
					String SSJE=(String)object[18];
					String JDZL=(String)object[19];
					Date GXSJ=(Date)object[20];
					BigDecimal SXYZLMB=(BigDecimal)object[21];
					String LCBH=(String)object[22];
					String LZJD=(String)object[23];
					String SPZT=(String)object[24];
					String LCBS=(String)object[25];
					String LZWZ=(String)object[26];
					String RWID=(String)object[27];
					String DUTIER=(String)object[28];
					String MOBILE=(String)object[29];
					String EMAIL=(String)object[30];
					
					map.put("INSTANCEID",INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("ID",ID==null?"0":ID.toString());
					map.put("USERID",USERID);
					map.put("USERNAME",USERNAME);
					map.put("DEPARTMENTNAME",DEPARTMENTNAME);
					map.put("TASK_NAME",TASK_NAME);
					map.put("STARTDATE",STARTDATE==null?"":sdf.format(STARTDATE));
					map.put("ENDDATE",ENDDATE==null?"":sdf.format(ENDDATE));
					map.put("SCALE",SCALE);
					map.put("ATTACH",ATTACH);
					map.put("MEMO",MEMO);
					map.put("MANAGER",MANAGER);
					map.put("GROUPID",GROUPID);
					map.put("PROJECTNO",PROJECTNO);
					map.put("PROJECTNAME",PROJECTNAME);
					map.put("ORDERINDEX",ORDERINDEX==null?"0":ORDERINDEX.toString());
					map.put("PRIORITY",PRIORITY);
					map.put("HTJE",HTJE);
					map.put("SSJE",SSJE);
					map.put("JDZL",JDZL);
					map.put("GXSJ",GXSJ);
					map.put("SXYZLMB",SXYZLMB==null?"0":SXYZLMB.toString());
					map.put("LCBH",LCBH);
					map.put("LZJD",LZJD);
					map.put("SPZT",SPZT);
					map.put("LCBS",LCBS);
					map.put("LZWZ",LZWZ);
					map.put("RWID",RWID);
					map.put("DUTIER",DUTIER!=null&&!DUTIER.equals("")?DUTIER.replaceAll(",", ";<br>"):"");
					map.put("MOBILE",MOBILE);
					map.put("EMAIL",EMAIL);
					map.put("SHOWBUTTON", !USERID.equals(cuserid));
					map.put("CURRUSERID", cuserid);
					map.put("CURRDEPARTMENTNAME", uc.get_userModel().getDepartmentname());
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getQjlcListByUseridSize(String username, String departmentname, String workstatus, String userid) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		sql.append("SELECT CASE WHEN B.INSTANCEID IS NULL THEN 0 ELSE B.INSTANCEID END INSTANCEID,O.ID,O.USERID,O.USERNAME,O.DEPARTMENTNAME,B.TASK_NAME,B.STARTDATE,B.ENDDATE,B.SCALE,B.ATTACH,B.MEMO,");
		sql.append("B.MANAGER,B.GROUPID,B.PROJECTNO,B.PROJECTNAME,B.ORDERINDEX,B.PRIORITY,B.HTJE,B.SSJE,B.JDZL,B.GXSJ,B.SXYZLMB,B.LCBH,B.LZJD,B.SPZT,B.LCBS,B.LZWZ,B.RWID,");
		sql.append("Z.DUTIER,O.MOBILE,O.EMAIL FROM ORGUSER O LEFT JOIN (SELECT * FROM BD_ZQB_XMRWGLLCB WHERE PRIORITY=1) B ON O.USERID=B.PROJECTNO  LEFT JOIN SYS_INSTANCE_DATA D ON B.INSTANCEID=D.ID");

		sql.append(" LEFT JOIN (SELECT O.DEPARTMENTID, to_char(WMSYS.WM_CONCAT(O.USERNAME||' '||O.MOBILE)) DUTIER FROM ORGUSER O WHERE O.ISMANAGER=1 GROUP BY O.DEPARTMENTID) Z ON O.DEPARTMENTID=Z.DEPARTMENTID");
		
		sql.append(" WHERE (D.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='请假流程单') OR D.ID IS NULL) AND O.ORGROLEID<>3 AND O.ID<>1");

		if(userid!=null&&!userid.equals("")){
		sql.append(" AND B.PROJECTNO = ?");
		params.add(userid);
		}
		if(username!=null&&!username.equals("")){
		sql.append(" AND O.USERNAME LIKE ?");
		params.add("%"+username+"%");
		}
		if(departmentname!=null&&!departmentname.equals("")){
		sql.append(" AND O.DEPARTMENTNAME LIKE ?");
		params.add("%"+departmentname+"%");
		}
		if(workstatus!=null&&!workstatus.equals("")){
			if(workstatus.equals("离岗")){
				sql.append(" AND B.SPZT='审批通过' AND B.TASK_NAME IS NULL");
			}else{
				sql.append(" AND (B.SPZT='审批中' OR B.SPZT='驳回' OR B.SPZT IS NULL OR B.TASK_NAME IS NOT NULL)");
			}
		}
		sql.append(")");
		sql.append(" ORDER BY INSTANCEID DESC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				String cuserid = uc.get_userModel().getUserid();
				for (Object[] object : list) {
					map = new HashMap();
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getQjlcList(int pageSize, int pageNumber, String username, String departmentname, String workstatus) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		sql.append("SELECT CASE WHEN B.INSTANCEID IS NULL THEN 0 ELSE B.INSTANCEID END INSTANCEID,O.ID,O.USERID,O.USERNAME,O.DEPARTMENTNAME,B.TASK_NAME,B.STARTDATE,B.ENDDATE,B.SCALE,B.ATTACH,B.MEMO,");
		sql.append("B.MANAGER,B.GROUPID,B.PROJECTNO,B.PROJECTNAME,B.ORDERINDEX,B.PRIORITY,B.HTJE,B.SSJE,B.JDZL,B.GXSJ,B.SXYZLMB,B.LCBH,B.LZJD,B.SPZT,B.LCBS,B.LZWZ,B.RWID,");
		sql.append("Z.DUTIER,O.MOBILE,O.EMAIL FROM ORGUSER O LEFT JOIN (SELECT * FROM BD_ZQB_XMRWGLLCB WHERE PRIORITY=1 AND ID IN(SELECT MAX(ID) FROM BD_ZQB_XMRWGLLCB B GROUP BY B.PROJECTNO)) B ON O.USERID=B.PROJECTNO  LEFT JOIN SYS_INSTANCE_DATA D ON B.INSTANCEID=D.ID");

		sql.append(" LEFT JOIN (SELECT O.DEPARTMENTID, to_char(WMSYS.WM_CONCAT(O.USERNAME||' '||O.MOBILE)) DUTIER FROM ORGUSER O WHERE O.ISMANAGER=1 GROUP BY O.DEPARTMENTID) Z ON O.DEPARTMENTID=Z.DEPARTMENTID");
		
		sql.append(" WHERE (D.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='请假流程单') OR D.ID IS NULL) AND O.ORGROLEID<>3 AND O.ID<>1");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		
		OrgUser user = uc.get_userModel();
		
		Long ismanager = user.getIsmanager();
		
		if(!("综合管理部".equals(user.getDepartmentname())||"吕红贞".equals(user.getUsername())||"杨光".equals(user.getUsername()))){
			
				sql.append(" AND O.DEPARTMENTNAME LIKE ?");
				params.add("%"+user.getDepartmentname()+"%");
				
			
			if(ismanager==0){
				
					sql.append(" AND O.USERNAME LIKE ?");
					params.add("%"+user.getUsername()+"%");
					
			}else{
				if(username!=null&&!username.equals("")){
					sql.append(" AND O.USERNAME LIKE ?");
					params.add("%"+username+"%");
					}
			}
			
			
		}else{
		if(departmentname!=null&&!departmentname.equals("")){
			sql.append(" AND O.DEPARTMENTNAME LIKE ?");
			params.add("%"+departmentname+"%");
			}
		if(username!=null&&!username.equals("")){
			sql.append(" AND O.USERNAME LIKE ?");
			params.add("%"+username+"%");
			}
		}
		if(workstatus!=null&&!workstatus.equals("")){
			if(workstatus.equals("离岗")){
				sql.append(" AND B.SPZT='审批通过' AND B.TASK_NAME IS NULL");
			}else{
				sql.append(" AND (B.SPZT='审批中' OR B.SPZT='驳回' OR B.SPZT IS NULL OR B.TASK_NAME IS NOT NULL)");
			}
		}
		sql.append(")");
		sql.append(" ORDER BY INSTANCEID DESC");
		final String sql1 = sql.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				String cuserid = uc.get_userModel().getUserid();
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal INSTANCEID=(BigDecimal)object[0];
					BigDecimal ID=(BigDecimal)object[1];
					String USERID=(String)object[2];
					String USERNAME=(String)object[3];
					String DEPARTMENTNAME=(String)object[4];
					String TASK_NAME=(String)object[5];
					Date STARTDATE=(Date)object[6];
					Date ENDDATE=(Date)object[7];
					String SCALE=(String)object[8];
					Date ATTACH=(Date)object[9];
					String MEMO=(String)object[10];
					String MANAGER=(String)object[11];
					String GROUPID=(String)object[12];
					String PROJECTNO=(String)object[13];
					String PROJECTNAME=(String)object[14];
					BigDecimal ORDERINDEX=(BigDecimal)object[15];
					String PRIORITY=(String)object[16];
					String HTJE=(String)object[17];
					String SSJE=(String)object[18];
					String JDZL=(String)object[19];
					Date GXSJ=(Date)object[20];
					BigDecimal SXYZLMB=(BigDecimal)object[21];
					String LCBH=(String)object[22];
					String LZJD=(String)object[23];
					String SPZT=(String)object[24];
					String LCBS=(String)object[25];
					String LZWZ=(String)object[26];
					String RWID=(String)object[27];
					String DUTIER=(String)object[28];
					String MOBILE=(String)object[29];
					String EMAIL=(String)object[30];
					
					map.put("INSTANCEID",INSTANCEID==null?"0":INSTANCEID.toString());
					map.put("ID",ID==null?"0":ID.toString());
					map.put("USERID",USERID);
					map.put("USERNAME",USERNAME);
					map.put("DEPARTMENTNAME",DEPARTMENTNAME);
					map.put("TASK_NAME",TASK_NAME);
					map.put("STARTDATE",STARTDATE==null?"":sdf.format(STARTDATE));
					map.put("ENDDATE",ENDDATE==null?"":sdf.format(ENDDATE));
					map.put("SCALE",SCALE);
					map.put("ATTACH",ATTACH);
					map.put("MEMO",MEMO);
					map.put("MANAGER",MANAGER);
					map.put("GROUPID",GROUPID);
					map.put("PROJECTNO",PROJECTNO);
					map.put("PROJECTNAME",PROJECTNAME);
					map.put("ORDERINDEX",ORDERINDEX==null?"0":ORDERINDEX.toString());
					map.put("PRIORITY",PRIORITY);
					map.put("HTJE",HTJE);
					map.put("SSJE",SSJE);
					map.put("JDZL",JDZL);
					map.put("GXSJ",GXSJ);
					map.put("SXYZLMB",SXYZLMB==null?"0":SXYZLMB.toString());
					map.put("LCBH",LCBH);
					map.put("LZJD",LZJD);
					map.put("SPZT",SPZT);
					map.put("LCBS",LCBS);
					map.put("LZWZ",LZWZ);
					map.put("RWID",RWID);
					map.put("DUTIER",DUTIER!=null&&!DUTIER.equals("")?DUTIER.replaceAll(",", ";<br>"):"");
					map.put("MOBILE",MOBILE);
					map.put("EMAIL",EMAIL);
					map.put("SHOWBUTTON", !USERID.equals(cuserid));
					
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getQjlcListSize(String username, String departmentname, String workstatus) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM (");
		sql.append("SELECT CASE WHEN B.INSTANCEID IS NULL THEN 0 ELSE B.INSTANCEID END INSTANCEID,O.ID,O.USERID,O.USERNAME,O.DEPARTMENTNAME,B.TASK_NAME,B.STARTDATE,B.ENDDATE,B.SCALE,B.ATTACH,B.MEMO,");
		sql.append("B.MANAGER,B.GROUPID,B.PROJECTNO,B.PROJECTNAME,B.ORDERINDEX,B.PRIORITY,B.HTJE,B.SSJE,B.JDZL,B.GXSJ,B.SXYZLMB,B.LCBH,B.LZJD,B.SPZT,B.LCBS,B.LZWZ,B.RWID,");
		sql.append("Z.DUTIER,O.MOBILE,O.EMAIL FROM ORGUSER O LEFT JOIN (SELECT * FROM BD_ZQB_XMRWGLLCB WHERE PRIORITY=1 AND ID IN(SELECT MAX(ID) FROM BD_ZQB_XMRWGLLCB B GROUP BY B.PROJECTNO)) B ON O.USERID=B.PROJECTNO  LEFT JOIN SYS_INSTANCE_DATA D ON B.INSTANCEID=D.ID");

		sql.append(" LEFT JOIN (SELECT O.DEPARTMENTID, to_char(WMSYS.WM_CONCAT(O.USERNAME||' '||O.MOBILE)) DUTIER FROM ORGUSER O WHERE O.ISMANAGER=1 GROUP BY O.DEPARTMENTID) Z ON O.DEPARTMENTID=Z.DEPARTMENTID");
		
		sql.append(" WHERE (D.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='请假流程单') OR D.ID IS NULL) AND O.ORGROLEID<>3 AND O.ID<>1");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		
		Long ismanager = user.getIsmanager();
		if(!("综合管理部".equals(user.getDepartmentname())||"吕红贞".equals(user.getUsername())||"杨光".equals(user.getUsername()))){
			
				sql.append(" AND O.DEPARTMENTNAME LIKE ?");
				params.add("%"+user.getDepartmentname()+"%");
				
			
			if(ismanager==0){
				
					sql.append(" AND O.USERNAME LIKE ?");
					params.add("%"+user.getUsername()+"%");
					
			}else{
				if(username!=null&&!username.equals("")){
					sql.append(" AND O.USERNAME LIKE ?");
					params.add("%"+username+"%");
					}
			}
			
			
		}else{
			if(departmentname!=null&&!departmentname.equals("")){
				sql.append(" AND O.DEPARTMENTNAME LIKE ?");
				params.add("%"+departmentname+"%");
				}
			if(username!=null&&!username.equals("")){
				sql.append(" AND O.USERNAME LIKE ?");
				params.add("%"+username+"%");
				}
			}
		
		if(workstatus!=null&&!workstatus.equals("")){
			if(workstatus.equals("离岗")){
				sql.append(" AND B.SPZT='审批通过' AND B.TASK_NAME IS NULL");
			}else{
				sql.append(" AND (B.SPZT='审批中' OR B.SPZT='驳回' OR B.SPZT IS NULL OR B.TASK_NAME IS NOT NULL)");
			}
		}
		sql.append(")");
		sql.append(" ORDER BY INSTANCEID DESC");
		final String sql1 = sql.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				List<HashMap> l = new ArrayList<HashMap>();
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					l.add(map);
				}
				return l;
			}
		});
	}
}
