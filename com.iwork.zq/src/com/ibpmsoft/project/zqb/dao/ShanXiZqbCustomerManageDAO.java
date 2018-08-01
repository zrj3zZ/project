package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class ShanXiZqbCustomerManageDAO extends HibernateDaoSupport {
	private final static String CN_FILENAME = "/common.properties";
	private static Logger logger = Logger.getLogger(ShanXiZqbCustomerManageDAO.class);
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> getCurrentCustomerList(
			String customername, String zqdm, String type, String zwmc,
			BigDecimal zczbbegin, BigDecimal zczbend, String gfgsrqbegin,
			String gfgsrqend, int pageSize, int pageNow,String dq) {
		int startRow = (pageNow - 1) * pageSize;
		int endRow = pageNow * pageSize;
		Long xmlxformid = Long.parseLong(ConfigUtil.readValue(CN_FILENAME, "xmlxformid"));
		final StringBuffer sql = new StringBuffer("SELECT KHDATA.*,KMINFO.JDMC FROM (SELECT DATA.*,ROWNUM RW FROM (");
		sql.append("SELECT KH.ID,KH.CUSTOMERNAME,KH.CUSTOMERNO,KH.TYPE,KH.ZQJC,KH.ZQDM,KH.SSHY,KH.ZWMC,NVL(KH.ZCZB,'') ZCZB,TO_CHAR(KH.GFGSRQ,'yyyy-mm-dd') GFGSRQ,");
		sql.append("BINDTABLE.INSTANCEID,NVL(PJDATA.ID,0) PID,NVL(PJDATA.PJINSID,0) PJINSID,PJDATA.PROJECTNO PJNO,NVL(PJDATA.SPZT,'提交审批') XMLXSPZT,NUMDATA.CNUM,");
		sql.append("GGLC.LCBH,NVL(GGLC.LCBS,0) GGLCBS,NVL(GGLC.TASKID,0) GGLCTASKID,NVL(GGLC.SPZT,'') GGLCSPZT,XMNH.LCBH XMNHLCBH,NVL(XMNH.LCBS,0) XMNHLCBS,");
		sql.append("NVL(XMNH.TASKID,0) XMNHLCTASKID,NVL(XMNH.SPZT,'') XMNHLCSPZT,NHFK.LCBH NHFKLCBH,NVL(NHFK.LCBS,0) NHFKLCBS,NVL(NHFK.TASKID,0) NHFKLCTASKID,");
		sql.append("NVL(NHFK.SPZT,'') NHFKLCSPZT,GZFK.LCBH GZFKLCBH,NVL(GZFK.LCBS,0) GZFKLCBS,NVL(GZFK.TASKID,0) GZFKLCTASKID,NVL(GZFK.SPZT,'') GZFKLCSPZT,");
		sql.append("GP.LCBH GPLCBH,NVL(GP.LCBS,0) GPLCBS,NVL(GP.TASKID,0) GPLCTASKID,NVL(GP.SPZT,'') GPLCSPZT,PJDATA.LXLCBH,NVL(PJDATA.LXLCBS,0) LXLCBS,");
		sql.append("NVL(PJDATA.LXTASKID,0) LXTASKID,KH.ZCQX,KH.JLNJLR FROM ");
		sql.append(" (");
		sql.append("SELECT ID,CUSTOMERNAME,CUSTOMERNO,TYPE,ZQJC,ZQDM,SSHY,ZWMC,ZCZB,GFGSRQ,ZCQX,JLNJLR FROM BD_ZQB_KH_BASE WHERE 1=1");
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = userModel.getOrgroleid();		
		// 判断角色ID是否为董秘
		String extend1 = "";
		if (orgroleid == 3) {	
			extend1 = userModel.getExtend1();
			sql.append(" AND CUSTOMERNO = ?");//'" + extend1 + "'
		}
		final String final_extend1 = extend1;
		// 客户名称
		final String final_customername = customername;
		if (customername != null && !customername.equals("")) {
			sql.append(" AND UPPER(CUSTOMERNAME) LIKE ?");//'%" + StringEscapeUtils.escapeSql(customername.toUpperCase().trim()) + "%'
		}
		// 证券代码
		final String final_zqdm = zqdm;
		if (zqdm != null && !zqdm.equals("")) {
			sql.append(" AND ZQDM like ?");//'%" + StringEscapeUtils.escapeSql(zqdm.trim()) + "%'
		}
		// 注册类型
		final String final_type = type;
		if (type != null && !type.equals("")) {
			sql.append(" AND TYPE like ?");//'%" + StringEscapeUtils.escapeSql(type.trim()) + "%'
		}
		// 住所地
		final String final_zwmc = zwmc;
		if (zwmc != null && !zwmc.equals("")) {
			sql.append(" AND ZWMC like ?");//'%" + StringEscapeUtils.escapeSql(zwmc.trim()) + "%'
		}
		// 注册资本
		final BigDecimal final_zczbbegin = zczbbegin;
		final BigDecimal final_zczbend = zczbend;
		if (zczbbegin != null && !zczbbegin.equals("")) {
			sql.append(" AND ZCZB>=?");// + StringEscapeUtils.escapeSql(zczbbegin.toString().trim())
		}
		if (zczbend != null && !zczbend.equals("")) {
			sql.append(" AND ZCZB<=?");// + StringEscapeUtils.escapeSql(zczbend.toString().trim())
		}
		// 成立日期
		final String final_gfgsrqbegin = gfgsrqbegin;
		final String final_gfgsrqend = gfgsrqend;
		if (gfgsrqbegin != null && !gfgsrqbegin.equals("")) {
			sql.append(" AND TO_CHAR(GFGSRQ,'yyyy-mm-dd') >= ?");//'" + StringEscapeUtils.escapeSql(gfgsrqbegin.trim()) +"'
		}
		if (gfgsrqend != null && !gfgsrqend.equals("")) {
			sql.append(" AND TO_CHAR(GFGSRQ,'yyyy-mm-dd') <= ?");//'" + StringEscapeUtils.escapeSql(gfgsrqend.trim()) +"'
		}
		// 所属大区
		final String final_dq = dq;
		if (dq != null && !dq.equals("")) {
			sql.append(" AND UPPER(ZCQX) LIKE ?");//'%" + StringEscapeUtils.escapeSql(dq.toUpperCase().trim()) + "%'
		}
		final long final_xmlxformid = xmlxformid;
		sql.append(") KH INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON KH.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL");
		sql.append(" AND BINDTABLE.FORMID=88 AND BINDTABLE.METADATAID=102 LEFT JOIN (SELECT PJ.ID,PJ.CUSTOMERNO,PJBINDTABLE.INSTANCEID PJINSID,");
		sql.append("PJ.PROJECTNO,PJ.SPZT,PJ.LCBH LXLCBH,PJ.LCBS LXLCBS,PJ.TASKID LXTASKID FROM BD_ZQB_PJ_BASE PJ ");
		sql.append("INNER JOIN SYS_ENGINE_FORM_BIND PJBINDTABLE ON PJ.ID = PJBINDTABLE.DATAID AND PJBINDTABLE.INSTANCEID IS NOT NULL ");
		sql.append("AND PJBINDTABLE.FORMID=? AND PJBINDTABLE.METADATAID=101) PJDATA ON KH.CUSTOMERNO=PJDATA.CUSTOMERNO");//"+xmlxformid+"
		sql.append(" LEFT JOIN (SELECT KHDATA.CUSTOMERNO,COUNT(KHDATA.CUSTOMERNO) CNUM FROM (SELECT DATA.*,ROWNUM RW FROM (");
		sql.append("SELECT KH.ID,KH.CUSTOMERNAME,KH.CUSTOMERNO,KH.TYPE,KH.ZQJC,KH.ZQDM,KH.SSHY,KH.ZWMC,NVL(KH.ZCZB,'') ZCZB,");
		sql.append("TO_CHAR(KH.GFGSRQ,'yyyy-mm-dd') GFGSRQ,BINDTABLE.INSTANCEID,NVL(PJDATA.ID,'') PID,NVL(PJDATA.PJINSID,'') PJINSID FROM ");
		sql.append(" (");
		sql.append("SELECT ID,CUSTOMERNAME,CUSTOMERNO,TYPE,ZQJC,ZQDM,SSHY,ZWMC,ZCZB,GFGSRQ FROM BD_ZQB_KH_BASE WHERE 1=1");
		// 判断角色ID是否为董秘
		if (orgroleid == 3) {
			sql.append(" AND CUSTOMERNO = ?");//'" + extend1 + "'
		}
		// 客户名称
		if (customername != null && !customername.equals("")) {
			sql.append(" AND UPPER(CUSTOMERNAME) LIKE ?");//'%" + StringEscapeUtils.escapeSql(customername.toUpperCase().trim()) + "%'
		}
		// 证券代码
		if (zqdm != null && !zqdm.equals("")) {
			sql.append(" AND ZQDM like ?");//'%" + StringEscapeUtils.escapeSql(zqdm.trim()) + "%'
		}
		// 注册类型
		if (type != null && !type.equals("")) {
			sql.append(" AND TYPE like ?");//'%" + StringEscapeUtils.escapeSql(type.trim()) + "%'
		}
		// 住所地
		if (zwmc != null && !zwmc.equals("")) {
			sql.append(" AND ZWMC like ?");//'%" + StringEscapeUtils.escapeSql(zwmc.trim()) + "%'
		}
		// 注册资本
		if (zczbbegin != null && !zczbbegin.equals("")) {
			sql.append(" AND ZCZB>=?");// + StringEscapeUtils.escapeSql(zczbbegin.toString().trim())
		}
		if (zczbend != null && !zczbend.equals("")) {
			sql.append(" AND ZCZB<=?");//+ StringEscapeUtils.escapeSql(zczbend.toString().trim())
		}
		// 成立日期
		if (gfgsrqbegin != null && !gfgsrqbegin.equals("")) {
			sql.append(" AND TO_CHAR(GFGSRQ,'yyyy-mm-dd') >= ?");//'" + StringEscapeUtils.escapeSql(gfgsrqbegin.trim()) +"'
		}
		if (gfgsrqend != null && !gfgsrqend.equals("")) {
			sql.append(" AND TO_CHAR(GFGSRQ,'yyyy-mm-dd') <= ?");//'" + StringEscapeUtils.escapeSql(gfgsrqend.trim()) +"'
		}
		// 所属大区
		
		if (dq != null && !dq.equals("")) {
			sql.append(" AND UPPER(ZCQX) LIKE ?");//'%" + StringEscapeUtils.escapeSql(dq.toUpperCase().trim()) + "%'			
		}
		sql.append(")");
		sql.append(" KH INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON KH.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL ");
		sql.append("AND BINDTABLE.FORMID=88 AND BINDTABLE.METADATAID=102 LEFT JOIN (SELECT PJ.ID,PJ.CUSTOMERNO,PJBINDTABLE.INSTANCEID PJINSID ");
		sql.append("FROM BD_ZQB_PJ_BASE PJ INNER JOIN SYS_ENGINE_FORM_BIND PJBINDTABLE ON PJ.ID = PJBINDTABLE.DATAID ");
		sql.append("AND PJBINDTABLE.INSTANCEID IS NOT NULL AND PJBINDTABLE.FORMID= ? AND PJBINDTABLE.METADATAID=101) PJDATA");//"+xmlxformid+"
		sql.append(" ON KH.CUSTOMERNO=PJDATA.CUSTOMERNO");
		sql.append(" ORDER BY KH.ID DESC) DATA) KHDATA,BD_ZQB_KM_INFO KMINFO");
		sql.append(" WHERE KHDATA.RW>")
				.append(startRow).append(" AND KHDATA.RW<=").append(endRow)
				.append(" GROUP BY CUSTOMERNO) NUMDATA ON KH.CUSTOMERNO=NUMDATA.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT LCGG.CUSTOMERNO,LCGG.LCBH,LCGG.LCBS,LCGG.SPZT,CASE WHEN TASK.ID_ IS NULL THEN CONCAT(LCGG.TASKID, '') WHEN TASK.ID_ IS NOT NULL THEN CONCAT(TRANSLATE(TASK.ID_ USING CHAR_CS),'') ELSE '0' END AS TASKID FROM BD_ZQB_LCGG LCGG LEFT JOIN PROCESS_RU_TASK TASK ON LCGG.LCBS=TASK.EXECUTION_ID_) GGLC ON KH.CUSTOMERNO=GGLC.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT XMNH.CUSTOMERNO,XMNH.LCBH,XMNH.LCBS,XMNH.SPZT,CASE WHEN TASK.ID_ IS NULL THEN CONCAT(XMNH.TASKID, '') WHEN TASK.ID_ IS NOT NULL THEN CONCAT(TRANSLATE(TASK.ID_ USING CHAR_CS),'') ELSE '0' END AS TASKID FROM BD_ZQB_SQNH XMNH LEFT JOIN PROCESS_RU_TASK TASK ON XMNH.LCBS=TASK.EXECUTION_ID_) XMNH ON KH.CUSTOMERNO=XMNH.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT NHFK.CUSTOMERNO,NHFK.LCBH,NHFK.LCBS,NHFK.SPZT,CASE WHEN TASK.ID_ IS NULL THEN CONCAT(NHFK.TASKID, '') WHEN TASK.ID_ IS NOT NULL THEN CONCAT(TRANSLATE(TASK.ID_ USING CHAR_CS),'') ELSE '0' END AS TASKID FROM BD_ZQB_NHFK NHFK LEFT JOIN PROCESS_RU_TASK TASK ON NHFK.LCBS=TASK.EXECUTION_ID_) NHFK ON KH.CUSTOMERNO=NHFK.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT GZFK.CUSTOMERNO,GZFK.LCBH,GZFK.LCBS,GZFK.SPZT,CASE WHEN TASK.ID_ IS NULL THEN CONCAT(GZFK.TASKID, '') WHEN TASK.ID_ IS NOT NULL THEN CONCAT(TRANSLATE(TASK.ID_ USING CHAR_CS),'') ELSE '0' END AS TASKID FROM BD_ZQB_GZFKJHF GZFK LEFT JOIN PROCESS_RU_TASK TASK ON GZFK.LCBS=TASK.EXECUTION_ID_) GZFK ON KH.CUSTOMERNO=GZFK.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT GP.CUSTOMERNO,GP.LCBH,GP.LCBS,GP.SPZT,CASE WHEN TASK.ID_ IS NULL THEN CONCAT(GP.TASKID, '') WHEN TASK.ID_ IS NOT NULL THEN CONCAT(TRANSLATE(TASK.ID_ USING CHAR_CS),'') ELSE '0' END AS TASKID FROM BD_ZQB_GPDJJGD GP LEFT JOIN PROCESS_RU_TASK TASK ON GP.LCBS=TASK.EXECUTION_ID_) GP ON KH.CUSTOMERNO=GP.CUSTOMERNO");
		sql.append(" ORDER BY KH.ID DESC) DATA) KHDATA,BD_ZQB_KM_INFO KMINFO");
		sql.append(" WHERE KHDATA.RW>")
				.append(startRow).append(" AND KHDATA.RW<=").append(endRow)
				.append(" ORDER BY KHDATA.ID DESC,KMINFO.SORTID");		
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
				Query query = session.createSQLQuery(sql.toString());
				int i=0;
				// 判断角色ID是否为董秘				
				if (final_extend1!= null && !final_extend1.equals("")) {
					if(d.HasInjectionData(final_extend1)){
						return l;
					}
					query.setString(i, final_extend1);i++;
				}				
				// 客户名称
				if (final_customername != null && !final_customername.equals("")) {
					if(d.HasInjectionData(final_customername)){
						return l;
					}
					query.setString(i, "%" + StringEscapeUtils.escapeSql(final_customername.toUpperCase().trim()) + "%");i++;
				}
				// 证券代码
				if (final_zqdm != null && !final_zqdm.equals("")) {
					if(d.HasInjectionData(final_zqdm)){
						return l;
					}
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_zqdm.trim()) + "%");i++;
				}
				// 注册类型
				if (final_type != null && !final_type.equals("")) {
					if(d.HasInjectionData(final_type)){
						return l;
					}
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_type.trim()) + "%");i++;
				}
				// 住所地
				if (final_zwmc != null && !final_zwmc.equals("")) {
					if(d.HasInjectionData(final_zwmc)){
						return l;
					}
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_zwmc.trim()) + "%");i++;
				}
				// 注册资本
				if (final_zczbbegin != null && !final_zczbbegin.equals("")) {
					if(d.HasInjectionData(final_zczbbegin.toString())){
						return l;
					}
					query.setString(i,StringEscapeUtils.escapeSql(final_zczbbegin.toString().trim()));i++;
				}
				if (final_zczbend != null && !final_zczbend.equals("")) {
					if(d.HasInjectionData(final_zczbend.toString())){
						return l;
					}
					query.setString(i,StringEscapeUtils.escapeSql(final_zczbend.toString().trim()));i++;
				}
				// 成立日期
				if (final_gfgsrqbegin != null && !final_gfgsrqbegin.equals("")) {
					if(d.HasInjectionData(final_gfgsrqbegin.toString())){
						return l;
					}
					query.setString(i,StringEscapeUtils.escapeSql(final_gfgsrqbegin.trim()));i++;
				}
				if (final_gfgsrqend != null && !final_gfgsrqend.equals("")) {
					if(d.HasInjectionData(final_gfgsrqend.toString())){
						return l;
					}
					query.setString(i,StringEscapeUtils.escapeSql(final_gfgsrqend.trim()));i++;
				}
				// 所属大区
				if (final_dq != null && !final_dq.equals("")) {
					if(d.HasInjectionData(final_dq.toString())){
						return l;
					}
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_dq.toUpperCase().trim()) + "%");i++;
				}
				query.setLong(i, final_xmlxformid);i++;
				// 判断角色ID是否为董秘				
				if (final_extend1!= null && !final_extend1.equals("")) {			
					query.setString(i, final_extend1);i++;
				}				
				// 客户名称
				if (final_customername != null && !final_customername.equals("")) {
					query.setString(i, "%" + StringEscapeUtils.escapeSql(final_customername.toUpperCase().trim()) + "%");i++;
				}
				// 证券代码
				if (final_zqdm != null && !final_zqdm.equals("")) {
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_zqdm.trim()) + "%");i++;
				}
				// 注册类型
				if (final_type != null && !final_type.equals("")) {
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_type.trim()) + "%");i++;
				}
				// 住所地
				if (final_zwmc != null && !final_zwmc.equals("")) {
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_zwmc.trim()) + "%");i++;
				}
				// 注册资本
				if (final_zczbbegin != null && !final_zczbbegin.equals("")) {
					query.setString(i,StringEscapeUtils.escapeSql(final_zczbbegin.toString().trim()));i++;
				}
				if (final_zczbend != null && !final_zczbend.equals("")) {
					query.setString(i,StringEscapeUtils.escapeSql(final_zczbend.toString().trim()));i++;
				}
				// 成立日期
				if (final_gfgsrqbegin != null && !final_gfgsrqbegin.equals("")) {
					query.setString(i,StringEscapeUtils.escapeSql(final_gfgsrqbegin.trim()));i++;
				}
				if (final_gfgsrqend != null && !final_gfgsrqend.equals("")) {
					query.setString(i,StringEscapeUtils.escapeSql(final_gfgsrqend.trim()));i++;
				}
				// 所属大区
				if (final_dq != null && !final_dq.equals("")) {
					query.setString(i,"%" + StringEscapeUtils.escapeSql(final_dq.toUpperCase().trim()) + "%");i++;
				}
				query.setLong(i, final_xmlxformid);i++;
				
				List<Object[]> list = query.list();
				HashMap<String, Object> map;
				for (Object[] object : list) {
					map = new HashMap<String, Object>();
					BigDecimal kid = (BigDecimal) object[0];
					String customername = (String) object[1];
					String customerno = (String) object[2];
					String type = (String) object[3];
					String zqjc = (String) object[4];
					String zqdm = (String) object[5];
					String sshy = (String) object[6];
					String zwmc = (String) object[7];
					BigDecimal zczb = (BigDecimal) object[8];
					String gfgsrq = (String) object[9];
					BigDecimal instanceid = (BigDecimal) object[10];
					BigDecimal pjid = (BigDecimal) object[11];
					BigDecimal pjinsid = (BigDecimal) object[12];
					String pjno = (String) object[13];
					String xmlxspzt = (String) object[14];
					BigDecimal cnum = (BigDecimal) object[15];
					
					String gglcbh = (String) object[16];
					BigDecimal gglcbs = (BigDecimal) object[17];
					String gglctaskid = (String) object[18];
					String gglcspzt = (String) object[19];
					
					String xmnhlcbh = (String) object[20];
					BigDecimal xmnhlcbs = (BigDecimal) object[21];
					String xmnhlctaskid = (String) object[22];
					String xmnhlcspzt = (String) object[23];
					
					String nhfklcbh = (String) object[24];
					BigDecimal nhfklcbs = (BigDecimal) object[25];
					String nhfklctaskid = (String) object[26];
					String nhfklcspzt = (String) object[27];
					
					String gzfklcbh = (String) object[28];
					BigDecimal gzfklcbs = (BigDecimal) object[29];
					String gzfklctaskid = (String) object[30];
					String gzfklcspzt = (String) object[31];
					
					String gplcbh = (String) object[32];
					BigDecimal gplcbs = (BigDecimal) object[33];
					String gplctaskid = (String) object[34];
					String gplcspzt = (String) object[35];
					
					String lxlcbh = (String) object[36];
					String lxlcbs = (String) object[37];
					String lxlctaskid = (String) object[38];
					
					String zcqx = (String) object[39];
					String jlnjlr = (String) object[40];
					
					BigDecimal rnum = (BigDecimal) object[41];
					String jdmc = (String) object[42];
					map.put("KID", kid==null?"0":kid.toString());
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("TYPE", type);
					map.put("ZQJC", zqjc);
					map.put("ZQDM", zqdm);
					map.put("SSHY", sshy);
					map.put("ZWMC", zwmc);
					map.put("ZCZB", zczb==null?"0":zczb.toString());
					map.put("GFGSRQ", gfgsrq);
					map.put("INSTANCEID", instanceid==null?"0":instanceid.toString());
					map.put("PJID", pjid==null?"0":pjid.toString());
					map.put("PJINSID", pjinsid==null?"0":pjinsid.toString());
					map.put("PJNO", pjno);
					map.put("XMLXSPZT", xmlxspzt);
					
					map.put("GGLCBH", gglcbh);
					map.put("GGLCBS", gglcbs==null?"0":gglcbs.toString());
					map.put("GGLCTASKID", gglctaskid);
					map.put("GGLCSPZT", gglcspzt);
					
					map.put("XMNHLCBH", xmnhlcbh);
					map.put("XMNHLCBS", xmnhlcbs==null?"0":xmnhlcbs.toString());
					map.put("XMNHLCTASKID", xmnhlctaskid);
					map.put("XMNHLCSPZT", xmnhlcspzt);
					
					map.put("NHFKLCBH", nhfklcbh);
					map.put("NHFKLCBS", nhfklcbs==null?"0":nhfklcbs.toString());
					map.put("NHFKLCTASKID", nhfklctaskid);
					map.put("NHFKLCSPZT", nhfklcspzt);
					
					map.put("GZFKLCBH", gzfklcbh);
					map.put("GZFKLCBS", gzfklcbs==null?"0":gzfklcbs.toString());
					map.put("GZFKLCTASKID", gzfklctaskid);
					map.put("GZFKLCSPZT", gzfklcspzt);
					
					map.put("GPDJJGDLCBH", gplcbh);
					map.put("GPDJJGDLCBS", gplcbs==null?"0":gplcbs.toString());
					map.put("GPDJJGDLCTASKID", gplctaskid);
					map.put("GPDJJGDLCSPZT", gplcspzt);
					
					map.put("XMLXLCBH", lxlcbh);
					map.put("XMLXLCBS", lxlcbs);
					map.put("XMLXLCTASKID", lxlctaskid);
					
					map.put("ZCQX", zcqx);
					map.put("JLNJLR", jlnjlr);
					
					map.put("CNUM", cnum==null?"0":cnum.toString());
					//map.put("GPINSID", gpinsid);
					map.put("RNUM", rnum==null?"0":rnum.toString());
					map.put("JDMC", jdmc);
					l.add(map);
				}
				return l;
			}
		});
	}

	public int getCurrentCustomerListSize(String sql, List<String> parameter) {
		int count=0;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getXmlxProjectList(String sql, int pageSize, int pageNumber) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, startRow);
			ps.setInt(2, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long rnum = rs.getLong("RNUM");
				String customername = rs.getString("CUSTOMERNAME");
				String zhxgsj = rs.getString("ZHXGSJ");
				String zclr = rs.getString("ZCLR");
				String owner = rs.getString("OWNER");
				String manager = rs.getString("MANAGER");
				String ddap = rs.getString("DDAP");
				String xmzk = rs.getString("XMZK");
				String xmhy = rs.getString("XMHY");
				String xmls = rs.getString("XMLS");
				result.put("RNUM", rnum);
				result.put("CUSTOMERNAME", customername);
				result.put("ZHXGSJ", zhxgsj);
				result.put("ZCLR", zclr);
				result.put("OWNER", owner);
				result.put("MANAGER", manager);
				result.put("DDAP", ddap);
				result.put("XMZK", xmzk);
				result.put("XMHY", xmhy);
				result.put("XMLS", xmls);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getXmlxProjectListSize(String sql) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getXmlxCyrProjectList(String sql, int pageSize, int pageNumber) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, startRow);
			ps.setInt(2, endRow);
			ps.setInt(3, startRow);
			ps.setInt(4, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long num = rs.getLong("NUM");
				Long cnum = rs.getLong("CNUM");
				String customername = rs.getString("CUSTOMERNAME");
				String name = rs.getString("NAME");
				String type = rs.getString("TYPE");
				String zhxgsj = rs.getString("ZHXGSJ");
				result.put("NUM", num);
				result.put("CNUM", cnum);
				result.put("CUSTOMERNAME", customername);
				result.put("NAME", name);
				result.put("TYPE", type);
				result.put("ZHXGSJ", zhxgsj);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getXmlxCyrProjectListSize(String sql) {
		int count=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List<HashMap<String, Object>> getExpXmlxProjectList(String sql) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String customername = rs.getString("PROJECTNAME");
				String zhxgsj = rs.getString("ZHXGSJ");
				String zclr = rs.getString("ZCLR");
				String owner = rs.getString("OWNER");
				String manager = rs.getString("MANAGER");
				String ddap = rs.getString("DDAP");
				String xmzk = rs.getString("XMZK");
				String xmhy = rs.getString("XMHY");
				String xmls = rs.getString("XMLS");
				result.put("CUSTOMERNAME", customername);
				result.put("ZHXGSJ", zhxgsj);
				result.put("ZCLR", zclr);
				result.put("OWNER", owner);
				result.put("MANAGER", manager);
				result.put("DDAP", ddap);
				result.put("XMZK", xmzk);
				result.put("XMHY", xmhy);
				result.put("XMLS", xmls);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, Object>> getExpXmlxCyrProjectList(String sql) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long num = rs.getLong("NUM");
				Long xmnum = rs.getLong("XMNUM");
				String customername = rs.getString("CUSTOMERNAME");
				String name = rs.getString("NAME");
				String type = rs.getString("TYPE");
				String zhxgsj = rs.getString("ZHXGSJ");
				result.put("NUM", num);
				result.put("XMNUM", xmnum);
				result.put("CUSTOMERNAME", customername);
				result.put("NAME", name);
				result.put("TYPE", type);
				result.put("ZHXGSJ", zhxgsj);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, String>> getCustomerAutoDataList(String sql) {
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String customername = rs.getString("CUSTOMERNAME");
				String customerno = rs.getString("CUSTOMERNO");
				result.put("CUSTOMERNAME", customername);
				result.put("CUSTOMERNO", customerno);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public static List<HashMap<String, Object>> getExpCustomerList(String sql) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				String customername = rs.getString("CUSTOMERNAME");
				String customerno = rs.getString("CUSTOMERNO");
				String type = rs.getString("TYPE");
				String zqjc = rs.getString("ZQJC");
				String zqdm = rs.getString("ZQDM");
				String sshy = rs.getString("SSHY");
				String zwmc = rs.getString("ZWMC");
				BigDecimal zczb = rs.getBigDecimal("ZCZB");
				String gfgsrq = rs.getString("GFGSRQ");
				String xmlxspzt = rs.getString("XMLXSPZT");
				Integer cnum = rs.getInt("CNUM");
				String gglcspzt = rs.getString("GGLCSPZT");
				String xmnhlcspzt = rs.getString("XMNHLCSPZT");
				String nhfklcspzt = rs.getString("NHFKLCSPZT");
				String jdmc = rs.getString("JDMC");
				
				result.put("CUSTOMERNAME", customername);
				result.put("CUSTOMERNO", customerno);
				result.put("TYPE", type);
				result.put("ZQJC", zqjc);
				result.put("ZQDM", zqdm);
				result.put("SSHY", sshy);
				result.put("ZWMC", zwmc);
				result.put("ZCZB", zczb);
				result.put("GFGSRQ", gfgsrq);
				result.put("XMLXSPZT", xmlxspzt);
				result.put("CNUM", cnum);
				result.put("GGLCSPZT", gglcspzt);
				result.put("XMNHLCSPZT", xmnhlcspzt);
				result.put("NHFKLCSPZT", nhfklcspzt);
				result.put("JDMC", jdmc);
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
