package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.iform.model.SysEngineIform;

public class ShanXiZqbContractManageDAO extends HibernateDaoSupport {

	public int getListSize(String sql, List<String> parameter) {
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

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> getList(SysEngineIform sysEngineIform, String gsmc, String ssbm, String xylx, String xmlx, String qyrqbegin, String qyrqend, String orderIndex, String rqtype) {
		Long metadataid = sysEngineIform.getMetadataid();
		Long id = sysEngineIform.getId();
		List params = new ArrayList();
		StringBuffer xmqy = new StringBuffer("SELECT * FROM BD_ZQB_XMQY WHERE 1=1 ");
		// 公司名称
		if (gsmc != null && !gsmc.equals("")) {
			xmqy.append(" AND UPPER(GSMC) LIKE ?");
			params.add("%" + StringEscapeUtils.escapeSql(gsmc.toUpperCase().trim()) + "%");
		}
		// 所属部门
		if (ssbm != null && !ssbm.equals("")) {
			xmqy.append(" AND SSBM like ?");
			params.add("%" + StringEscapeUtils.escapeSql(ssbm.trim()) + "%");
		}
		// 协议类型
		if (xylx != null && !xylx.equals("")) {
			xmqy.append(" AND XYLX = ?");
			params.add(StringEscapeUtils.escapeSql(xylx.trim()));
		}
		// 项目类型
		if (xmlx != null && !xmlx.equals("")) {
			xmqy.append(" AND XMLX = ?");
			params.add(StringEscapeUtils.escapeSql(xmlx.trim()));
		}
		// 签约日期
		if (qyrqbegin != null && !qyrqbegin.equals("")) {
			if(rqtype.equals("1")){
				xmqy.append(" AND TO_CHAR(QSXYRQ,'yyyy-mm') >= ?");
				params.add(StringEscapeUtils.escapeSql(qyrqbegin.trim()));
			}else if(rqtype.equals("0")){
				xmqy.append(" AND TO_CHAR(QSXYRQ,'yyyy') >= ?");
				params.add(StringEscapeUtils.escapeSql(qyrqbegin.trim()));
			}
		}
		if (qyrqend != null && !qyrqend.equals("")) {
			if(rqtype.equals("1")){
				xmqy.append(" AND TO_CHAR(QSXYRQ,'yyyy-mm') <= ?");
				params.add(StringEscapeUtils.escapeSql(qyrqend.trim()));
			}else if(rqtype.equals("0")){
				xmqy.append(" AND TO_CHAR(QSXYRQ,'yyyy') <= ?");
				params.add(StringEscapeUtils.escapeSql(qyrqend.trim()));
			}
		}
		if((qyrqbegin == null&&qyrqend == null)||(qyrqbegin.equals("")&&qyrqend.equals(""))){
			SimpleDateFormat sdb = new SimpleDateFormat("yyyy-MM");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -2);
			String times = sdb.format(calendar.getTime());
			xmqy.append(" AND TO_CHAR(QSXYRQ,'yyyy-mm') >= ?");
			params.add(StringEscapeUtils.escapeSql(times.trim()));
		}
		final StringBuffer sql = new StringBuffer("SELECT XMQY.ID,XMQY.MONTHRQ,XMQY.GSMC,XMQY.XYLX,XMQY.XMLX,XMQY.XYJE,XMQY.DDJE");
		sql.append(",XMQY.SDXYRQ,XMQY.QSXYRQ,XMQY.LCJSRQ,XMQY.SSBM,XMQY.JBR,XMQY.FKJD,XMQY.YEARRQ,YEARTOTAL.YXYJE,MONTHTOTAL.MXYJE,BINDTABLE.INSTANCEID FROM BD_ZQB_KH_BASE KH");
		sql.append("	INNER JOIN (SELECT ID,CUSTOMERNO,GSMC,XYLX,XMLX,XYJE,DDJE,TO_CHAR(SDXYRQ,'YYYY-MM-DD') SDXYRQ,TO_CHAR(QSXYRQ,'YYYY-MM-DD') QSXYRQ");
		sql.append(",TO_CHAR(LCJSRQ,'YYYY-MM-DD') LCJSRQ,SSBM,JBR,FKJD,TO_CHAR(QSXYRQ,'YYYY-MM') MONTHRQ,TO_CHAR(QSXYRQ,'YYYY') YEARRQ ");
		sql.append("FROM (").append(xmqy).append(")) XMQY ON KH.CUSTOMERNO=XMQY.CUSTOMERNO");
		sql.append(" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON XMQY.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL");
		sql.append(" AND BINDTABLE.FORMID=? AND BINDTABLE.METADATAID= ?");
		params.add(id);
		params.add(metadataid);
		sql.append(" LEFT JOIN BD_ZQB_PJ_BASE XMLX ON KH.CUSTOMERNO=XMLX.CUSTOMERNO LEFT JOIN ");
		sql.append(" (SELECT QSXYRQ,SUM(XYJE) AS MXYJE FROM (SELECT TO_CHAR(XMQY.QSXYRQ,'YYYY-MM') QSXYRQ");
		sql.append(",XMQY.XYJE FROM (SELECT A.* FROM BD_ZQB_XMQY A INNER JOIN (SELECT MAX(ID) ID,CUSTOMERNO,MAX(QSXYRQ) QSXYRQ");
		sql.append(" FROM (SELECT ID,CUSTOMERNO,GSMC,XYLX,DECODE(XYLX,'改制融资推荐挂牌财务顾问协议',ID,'保密协议',ID,'定增财务顾问协议'");
		sql.append(",ID,'并购财务顾问协议',ID,'推荐挂牌并持续督导协议书','5','推荐挂牌并持续督导协议书之补充协议','5','其他',ID) XYLXTYPE,XMLX");
		sql.append(",QSXYRQ FROM BD_ZQB_XMQY) GROUP BY XYLXTYPE,CUSTOMERNO,XMLX) B ON A.ID=B.ID AND A.CUSTOMERNO=B.CUSTOMERNO) XMQY) GROUP BY QSXYRQ) MONTHTOTAL ON XMQY.MONTHRQ=MONTHTOTAL.QSXYRQ");
		sql.append(" LEFT JOIN (SELECT QSXYRQ,SUM(XYJE) AS YXYJE FROM (SELECT TO_CHAR(XMQY.QSXYRQ,'YYYY') QSXYRQ,XMQY.XYJE FROM (SELECT A.*");
		sql.append(" FROM BD_ZQB_XMQY A INNER JOIN (SELECT MAX(ID) ID,CUSTOMERNO,MAX(QSXYRQ) QSXYRQ FROM (SELECT ID,CUSTOMERNO,GSMC,XYLX");
		sql.append(",DECODE(XYLX,'改制融资推荐挂牌财务顾问协议',ID,'保密协议',ID,'定增财务顾问协议',ID,'并购财务顾问协议',ID,'推荐挂牌并持续督导协议书'");
		sql.append(",'5','推荐挂牌并持续督导协议书之补充协议','5','其他',ID) XYLXTYPE,XMLX,QSXYRQ FROM BD_ZQB_XMQY) GROUP BY XYLXTYPE,CUSTOMERNO,XMLX) B");
		sql.append(" ON A.ID=B.ID AND A.CUSTOMERNO=B.CUSTOMERNO) XMQY) GROUP BY QSXYRQ) YEARTOTAL ON XMQY.YEARRQ=YEARTOTAL.QSXYRQ ORDER BY MONTHRQ ").append(orderIndex).append(",XMQY.QSXYRQ ").append(orderIndex);
		
		/*sql.append(" WHERE KHDATA.RW>")
				.append(startRow).append(" AND KHDATA.RW<=").append(endRow)
				.append(" ORDER BY KHDATA.ID DESC,KMINFO.ID");*/
		final List final_params = params; 
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql.toString());
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < final_params.size(); i++) {
					if(final_params.get(i)!=null && !"".equals(final_params.get(i).toString())){
						String params=final_params.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, final_params.get(i));
				}
				List<Object[]> list = query.list();
				HashMap<String, Object> map;
				for (Object[] object : list) {
					map = new HashMap<String, Object>();
					BigDecimal qyid = (BigDecimal) object[0];
					String monthrq = (String) object[1];
					String gsmc = (String) object[2];
					String xylx = (String) object[3];
					String xmlx = (String) object[4];
					BigDecimal xyje = (BigDecimal) object[5];
					String ddje = (String) object[6];
					String sdxyrq = (String) object[7];
					String qsxyrq = (String) object[8];
					String lcjsrq = (String) object[9];
					String ssbm = (String) object[10];
					String jbr = (String) object[11];
					String fkjd = (String) object[12];
					String year = (String) object[13];
					BigDecimal yxyje = (BigDecimal) object[14];
					BigDecimal mxyje = (BigDecimal) object[15];
					BigDecimal instanceid=(BigDecimal) object[16];
					map.put("QYID", qyid);
					map.put("MONTHRQ", monthrq);
					map.put("GSMC", gsmc);
					map.put("XYLX", xylx);
					map.put("XMLX", xmlx);
					map.put("XYJE", xyje);
					map.put("DDJE", ddje);
					map.put("QSXYRQ", qsxyrq);
					map.put("SDXYRQ", sdxyrq);
					map.put("LCJSRQ", lcjsrq);
					map.put("SSBM", ssbm);
					map.put("JBR", jbr);
					map.put("FKJD", fkjd);
					map.put("YEAR", year);
					map.put("YXYJE", yxyje);
					map.put("MXYJE", mxyje);
					map.put("INSTANCEID", instanceid);
					l.add(map);
				}
				return l;
			}
		});
	}

}
