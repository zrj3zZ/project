package com.ibpmsoft.project.zqb.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.lob.SerializableClob;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;

public class ZqbFileCompareDAO extends HibernateDaoSupport {

	private static Logger logger=Logger.getLogger(ZqbFileCompareDAO.class);
	
	public List<HashMap> getKnowAnswer() {
		final SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer("select q.qcontent,a.acontent,a.atime,c.cname from IWORK_KNOW_QUESTION q left join IWORK_KNOW_ANSWER a on q.id=a.qid left join IWORK_KNOW_CLASSES c on q.qbigc=c.id");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);		
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String qcontent=(String) object[0];
					String acontent=(String) object[1];
					String atime=(String) object[2];
					String format="";
					if(atime!=null){
						try {
							format = sd.format(sd.parse(atime));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							logger.error(e,e);
						}
					}
					String cname=(String) object[3];
					
					map.put("QCONTENT", qcontent==null?"":qcontent);
					map.put("ACONTENT", acontent==null?"":acontent);
					map.put("ATIME", format==null?"":format);
					map.put("CNAME", cname==null?"":cname);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getLTJL(String nickname, String chatName,String startdate, String enddate, String content,String sendname,String companyjc) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT A.ID ID,A.USERNAME USERNAME,TO_CHAR(A.DATATIME,'yyyy-MM-dd HH24:mi:ss') DATATIME,A.CONTENT CONTENT,CHATRECORDNAME CHATRECORDNAME,SENDNAME SENDNAME "
				+ "FROM (SELECT * FROM BD_GE_LTJL  WHERE (USERNAME=? OR SENDNAME=? OR SENDNAME='all') ");
		params.add(nickname);
		params.add(chatName);
		if(content != null &&!"".equals(content)){
			sb.append(" AND regexp_like(regexp_replace(content,'<img.*?>',''),'<a.*>.*?.*</a>') or (regexp_like(regexp_replace(content,'<img.*?>',''),'[^<a]') and regexp_replace(content,'<img.*?>','') like ?) ");
			params.add(content);
			params.add("%"+content+"%");
		}
		if (startdate != null && !"".equals(startdate)) {
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') >= ? ");
			params.add(startdate);
		}
		if (enddate != null && !"".equals(enddate)) {
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') <= ? ");
			params.add(enddate);
		}
		if(sendname != null && !sendname.equals("") && !sendname.equals("all")){
			sb.append("OR SENDNAME=?");
			params.add(sendname);
		}
		if(companyjc != null && !companyjc.equals("") && !companyjc.equals("null") && !companyjc.equals("all")){
			sb.append("AND SENDNAME LIKE ?");
			params.add("%"+companyjc+"%");
		}
		sb.append(" ORDER BY ID DESC) A");
		
		if((enddate == null || "".equals(enddate))&&(startdate == null || "".equals(startdate))){
			sb.append("  iNNER JOIN (SELECT TO_CHAR(MAX(DATATIME),'yyyy-MM-dd') SJ FROM BD_GE_LTJL WHERE (USERNAME=? OR SENDNAME=? OR SENDNAME='all')");
			params.add(nickname);
			params.add(chatName);
			sb.append(" ) B ON TO_CHAR(A.DATATIME,'yyyy-MM-dd')=B.SJ ");
		}
		
		sb.append(" ORDER BY ID");
		String gslx = "";
		final String sql=sb.toString();
		final List param = params;
		List<HashMap> l = new ArrayList<HashMap>();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
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
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				char[] buffer = null; 
				for (Object[] object : list) {
					map = new HashMap();
					Integer id=object[0]==null?0:Integer.valueOf(object[0].toString());
					String username=(String) object[1];
					String datatime=(String) object[2];
					SerializableClob sc = (SerializableClob)object[3];         
					try {             
					//根据CLOB长度创建字符数组     
					buffer = new char[(int)sc.length()];             
					//获取CLOB的字符流Reader,并将内容读入到字符数组中     
					sc.getCharacterStream().read(buffer); 
					} catch (Exception e) { 
					logger.error(e,e); 
					}
					//转换为字符串
					String content = String.valueOf(buffer);
					String CHATRECORDNAME=(String) object[4];
					String sendname=(String) object[5];
					map.put("ID", id);
					map.put("USERNAME", username);
					map.put("DATATIME", datatime);
					map.put("CONTENT", content);
					map.put("CHATRECORDNAME",CHATRECORDNAME);
					map.put("SENDNAME", sendname);
					l.add(map);
				}
				return l;
			}
		});
	}
	public Integer expDelete(String nickname, String chatName,String startdate, String enddate, String content,String sendname,String companyjc){
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("DELETE FROM BD_GE_LTJL WHERE ID IN(SELECT A.ID ID "
				+ "FROM (SELECT * FROM BD_GE_LTJL  WHERE (USERNAME=? OR SENDNAME=? OR SENDNAME='ALL') ");
		params.add(nickname);
		params.add(chatName);
		if(content != null &&!"".equals(content)){
			sb.append(" AND regexp_like(regexp_replace(content,'<img.*?>',''),'<a.*>.*?.*</a>') or (regexp_like(regexp_replace(content,'<img.*?>',''),'[^<a]') and regexp_replace(content,'<img.*?>','') like ?) ");
			params.add(content);
			params.add("%"+content+"%");
		}
		if (startdate != null && !"".equals(startdate)) {
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') >= ? ");
			params.add(startdate);
		}
		if (enddate != null && !"".equals(enddate)) {
			sb.append(" AND TO_CHAR(DATATIME,'yyyy-MM-dd') <= ? ");
			params.add(enddate);
		}
		if(sendname != null && !sendname.equals("") && !sendname.equals("all")){
			sb.append("AND SENDNAME=?");
			params.add(sendname);
		}
		if(companyjc != null && !companyjc.equals("") && !companyjc.equals("null") && !companyjc.equals("all")){
			sb.append("AND SENDNAME LIKE ?");
			params.add("%"+companyjc+"%");
		}
		sb.append(" ORDER BY ID DESC) A");
		
		if((enddate == null || "".equals(enddate))&&(startdate == null || "".equals(startdate))){
			sb.append("  iNNER JOIN (SELECT TO_CHAR(MAX(DATATIME),'yyyy-MM-dd') SJ FROM BD_GE_LTJL WHERE (USERNAME=? OR SENDNAME=? OR SENDNAME='all')");
			params.add(nickname);
			params.add(chatName);
			sb.append(" ) B ON TO_CHAR(A.DATATIME,'yyyy-MM-dd')=B.SJ ");
		}
		sb.append(" )");
		Integer num = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sb.toString());
			num = ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, null);
		}
		
		return num;
	}
	public String ClobToString(CLOB clob){
		String reString = "";
		Reader is = null;
		try {
			is = clob.getCharacterStream();
		} catch (Exception e) {
			logger.error(e,e);
		}// 得到流
		BufferedReader br = new BufferedReader(is);
		String s;
		try {
			s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {// 执行循环将字符串全部取出付值给 StringBuffer由StringBuffer转成STRING
				sb.append(s);
				s = br.readLine();
			}
			reString = sb.toString();
		} catch (Exception e) {
			logger.error(e,e);
		}
		return reString;
	}
}
