package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class ZqbCustomerManageDAO extends HibernateDaoSupport {
	
	public List<HashMap> getCurrentCustomerListAll(String customername,String zqdm,String zrfs,String cxddbg,
			String status,String type,String zwmc,BigDecimal jlr,String gpsj,
			BigDecimal zczbbegin,BigDecimal zczbend,
			String gfgsrqbegin,String gfgsrqend,
			String orderbygpsj,String orderbyzqdm,String ygp){
			List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT CUSTOMER.*,BASE.ID BID FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.TYPE,BOTABLE.ZYYW,BOTABLE.ZYCP,BOTABLE.JZYS,BOTABLE.NDYS,BOTABLE.JLR,BOTABLE.SLQK,BOTABLE.TEL,BOTABLE.EMAIL,BOTABLE.CZJGQBD,BOTABLE.GPSJ,BOTABLE.GFGSRQ,BOTABLE.YXGSRQ,BOTABLE.GDDH,BOTABLE.SHTZFS,BOTABLE.DSHSPQX,BOTABLE.MEMO,BOTABLE.CUSTOMERDESC,BOTABLE.ZQDM,BOTABLE.ZCDZ,BOTABLE.ZQJC,BOTABLE.SSHY,BOTABLE.ZWMC,BOTABLE.YWMC,BOTABLE.ZCZB,BOTABLE.JYXKXM,BOTABLE.GSWZ,BOTABLE.FDDBR,BOTABLE.PHONE,BOTABLE.FAX,BOTABLE.YGP,BOTABLE.XGZL,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_ZQB_KH_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%"+customername+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like ? ");
			params.add("%"+zqdm+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like  ? ");
			params.add("%"+zwmc+"%");
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(gpsj!=null&&!gpsj.equals("")){
			sb.append(" AND GPSJ= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gpsj);
		}
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("1")&&orderbyzqdm.equals("0")){
			sb.append(" ORDER BY botable.gpsj desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("0")&&orderbyzqdm.equals("1")){
			sb.append(" ORDER BY to_number(botable.zqdm) desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else{
			sb.append(" ORDER BY botable.ID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}
		final List param=params;
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[2];
					String customerno = (String) object[3];
					String username = (String) object[4];
					String status = (String) object[5];
					String type = (String) object[6];
					String tel=(String)object[13];
					Date gpsj=(Date)object[16];
					String zqdm=(String)object[24];
					String zqjc=(String)object[26];
					String zwmc=(String)object[28];
					String ygp = (String) object[36];
					BigDecimal instanceid = (BigDecimal) object[39];
					BigDecimal baseid = (BigDecimal) object[40];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERNAME", username);
					map.put("STATUS", status);
					map.put("TYPE", type);
					map.put("TEL", tel);
					map.put("GPSJ", gpsj);
					map.put("YGP", ygp);
					map.put("ZQDM", zqdm);
					map.put("ZQJC", zqjc);
					map.put("ZWMC", zwmc);
					map.put("INSTANCEID", instanceid);
					map.put("VIEW", baseid==null?false:"".equals(baseid)?false:true);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getCurrentCustomerList(String cusername,String customername,String zqdm,String zrfs,String cxddbg,
			String status,String type,String zwmc,BigDecimal jlr,String gpsjBEGIN,
			String zczbbegin,String zczbend,
			String gfgsrqbegin,String gfgsrqend,
			int pageSize, int pageNow,String orderbygpsj,String orderbyzqdm,String ygp
			,String extend4,String zcqx,String sshy,String gpsjEND,String innovation,String classification,int numOther,int numKhfzr,String orderbygpzt){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 if(gfgsrqbegin!=null && !"".equals(gfgsrqbegin) && gfgsrqend!=null && !"".equals(gfgsrqend) && gpsjBEGIN!=null && !"".equals(gpsjBEGIN) && gpsjEND!=null && !"".equals(gpsjEND)){ 
		try {
			 
			java.util.Date Date1 = format.parse(gfgsrqbegin);
			java.util.Date Date2 = format.parse(gfgsrqend);
			java.util.Date Date3 = format.parse(gpsjBEGIN);
			java.util.Date Date4 = format.parse(gpsjEND);
			int ct1 = Date1.compareTo(Date2);
			int ct2 = Date3.compareTo(Date4);
			if(ct1<0||ct2<0){
				return null;
			}
		} catch (ParseException e) {
			
			e.printStackTrace();
		}}
		
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT CUSTOMER.*,BASE.ID BID FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.TYPE,BOTABLE.ZYYW,BOTABLE.ZYCP,BOTABLE.JZYS,BOTABLE.NDYS,BOTABLE.JLR,BOTABLE.SLQK,BOTABLE.TEL,BOTABLE.EMAIL,BOTABLE.CZJGQBD,BOTABLE.GPSJ,BOTABLE.GFGSRQ,BOTABLE.YXGSRQ,BOTABLE.GDDH,BOTABLE.SHTZFS,BOTABLE.DSHSPQX,BOTABLE.MEMO,BOTABLE.CUSTOMERDESC,BOTABLE.ZQDM,BOTABLE.ZCDZ,BOTABLE.ZQJC,BOTABLE.SSHY,BOTABLE.ZWMC,BOTABLE.YWMC,BOTABLE.ZCZB,BOTABLE.JYXKXM,BOTABLE.GSWZ,BOTABLE.FDDBR,BOTABLE.PHONE,BOTABLE.FAX,BOTABLE.YGP,BOTABLE.XGZL,BOTABLE.ID,BINDTABLE.INSTANCEID,BOTABLE.Zcqx,  BOTABLE.Extend4 FROM BD_ZQB_KH_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		OrgUser uc = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		/*----------------华丽分割线,权限控制-------------*/
		if(numOther==0&&numKhfzr>0&&uc.getOrgroleid()!=5l&&uc.getOrgroleid()!=3l){/**在前两个节点的审批人员看自己督导的和自己创建的*/
			sb.append(" AND (CUSTOMERNO IN(SELECT B.KHBH FROM BD_MDM_KHQXGLB B WHERE B.KHFZR IS NOT NULL AND (SUBSTR(B.KHFZR,0, INSTR(B.KHFZR,'[',1)-1) = ? OR SUBSTR(B.ZZCXDD,0, INSTR(B.ZZCXDD,'[',1)-1) = ?)) OR USERID =?)");
			params.add(uc.getUserid());
			params.add(uc.getUserid());
			params.add(uc.getUserid());
		}else if(uc.getOrgroleid()==(long)3){/**董秘看自己*/
			sb.append(" AND CUSTOMERNO = ? ");
			params.add(uc.getExtend1());
		}else if(numOther==0&&numKhfzr==0&&uc.getOrgroleid()!=5l){/**不在所有审批节点中的非场外看不到*/
			sb.append(" AND CREATEUSER = '"+uc.getUsername()+"' ");
		}
		/**场外和除前两个节点外的人看全部*/
		/*----------------华丽分割线,权限控制-------------*/
		
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) LIKE ? ");
			params.add("%"+customername.toUpperCase()+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like ? ");
			params.add("%"+zqdm+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like  ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}			
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		
		
		
		if(cusername!=null&&!cusername.equals("")){
			sb.append(" AND BOTABLE.USERNAME like ? ");
			params.add("%"+cusername+"%");
		}
		if(gpsjBEGIN!=null&&!gpsjBEGIN.equals("")){
			sb.append(" AND GPSJ>= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gpsjBEGIN);
		}
		if (gpsjEND != null && !gpsjEND.equals("")) {
			sb.append(" AND GPSJ<= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gpsjEND);
		}
		if (extend4 != null && !extend4.equals("")) {				
			sb.append(" AND EXTEND4 like  ? ");//所属证监局
			params.add("%"+extend4+"%");
		}
		if (zcqx != null && !zcqx.equals("")) {//所属部门或所属大区				
			sb.append(" AND ZCQX like ? ");
			params.add("%"+zcqx+"%");
		}
		if (sshy != null && !sshy.equals("")) {				
			sb.append(" AND SSHY like ? ");//所属行业
			params.add("%"+sshy+"%");
		}
		if (innovation != null && !innovation.equals("")) {				
			sb.append(" AND INNOVATION= ? ");//创新层
			params.add(innovation);
		}
		if (classification != null && !classification.equals("")) {				
			sb.append(" AND CLASSIFICATION= ? ");//分类
			params.add(classification);
		}
		if(orderbyzqdm!=null || orderbygpsj!=null || orderbygpzt!=null){
			if(orderbyzqdm.equals("1")){
				sb.append(" ORDER BY to_number(botable.zqdm) ) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
			}else if(orderbyzqdm.equals("2")){
				sb.append(" ORDER BY to_number(botable.zqdm) desc ) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
			}else if(orderbygpsj.equals("1")){
				sb.append(" ORDER BY botable.gpsj ) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
			}else if(orderbygpsj.equals("2")){
				sb.append(" ORDER BY botable.gpsj desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
			}else if(orderbygpzt.equals("1")){
				sb.append(" ORDER BY botable.ygp ) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
			}else if(orderbygpzt.equals("2")){
				sb.append(" ORDER BY botable.ygp desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
			}else{
				sb.append(" ORDER BY botable.ID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
			}
		}else{
			sb.append(" ORDER BY botable.ID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}
		/*if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("1")&&orderbyzqdm.equals("0")){
			sb.append(" ORDER BY botable.gpsj desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("0")&&orderbyzqdm.equals("1")){
			sb.append(" ORDER BY to_number(botable.zqdm) desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else{
			sb.append(" ORDER BY botable.ID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}*/
		String escapeSql = StringEscapeUtils.escapeSql(sb.toString());
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[2];
					String customerno = (String) object[3];
					String username = (String) object[4];
					String status = (String) object[5];
					String type = (String) object[6];
					String tel=(String)object[13];
					Date gpsj=(Date)object[16];
					String zqdm=(String)object[24];
					String zqjc=(String)object[26];
					String zwmc=(String)object[28];
					String ygp = (String) object[36];
					BigDecimal instanceid = (BigDecimal) object[39];
					String zcqx=(String)object[40];
					String extend4 = (String) object[41];
					BigDecimal baseid = (BigDecimal) object[42];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERNAME", username);
					map.put("STATUS", status);
					map.put("TYPE", type);
					map.put("TEL", tel);
					map.put("GPSJ", gpsj);
					map.put("YGP", ygp);
					map.put("ZQDM", zqdm);
					map.put("ZQJC", zqjc);
					map.put("ZWMC", zwmc);
					map.put("INSTANCEID", instanceid==null?"":instanceid.toString());
					map.put("VIEW", baseid==null?false:"".equals(baseid)?false:true);
					map.put("ZCQX", zcqx);
					map.put("EXTEND4", extend4);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getCurrentCustomerListPjAll(String username,String userFullName,String customername,String zqdm,String zrfs,String cxddbg,String status,
			String type,String zwmc,BigDecimal jlr,String gpsj,BigDecimal zczbbegin,BigDecimal zczbend,String gfgsrqbegin,String gfgsrqend,
			String orderbygpsj,String orderbyzqdm,String ygp){
		StringBuffer sb = new StringBuffer("SELECT CUSTOMER.*,BASE.ID BID FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.TYPE,BOTABLE.ZYYW,BOTABLE.ZYCP,BOTABLE.JZYS,BOTABLE.NDYS,BOTABLE.JLR,BOTABLE.SLQK,BOTABLE.TEL,BOTABLE.EMAIL,BOTABLE.CZJGQBD,BOTABLE.GPSJ,BOTABLE.GFGSRQ,BOTABLE.YXGSRQ,BOTABLE.GDDH,BOTABLE.SHTZFS,BOTABLE.DSHSPQX,BOTABLE.MEMO,BOTABLE.CUSTOMERDESC,BOTABLE.ZQDM,BOTABLE.ZCDZ,BOTABLE.ZQJC,BOTABLE.SSHY,BOTABLE.ZWMC,BOTABLE.YWMC,BOTABLE.ZCZB,BOTABLE.JYXKXM,BOTABLE.GSWZ,BOTABLE.FDDBR,BOTABLE.PHONE,BOTABLE.FAX,BOTABLE.YGP,BOTABLE.XGZL,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_ZQB_KH_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		List params = new ArrayList();
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%"+customername+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like ? ");
			params.add("%"+zqdm+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like  ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(gpsj!=null&&!gpsj.equals("")){
			sb.append(" AND GPSJ= to_date( ? , 'yyyy-mm-dd')");
			params.add(gpsj);
		}
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		sb.append(" AND (rtrim(userid)= ? )");
		params.add(UserContextUtil.getInstance().getCurrentUserId());
		if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("1")&&orderbyzqdm.equals("0")){
			sb.append(" ORDER BY botable.gpsj desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		}else if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("0")&&orderbyzqdm.equals("1")){
			sb.append(" ORDER BY to_number(botable.zqdm) desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		}else{
			sb.append(" ORDER BY botable.ID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		}
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[2];
					String customerno = (String) object[3];
					String username = (String) object[4];
					String status = (String) object[5];
					String type = (String) object[6];
					String tel = (String) object[13];
					Date gpsj = (Date) object[16];
					String zqdm = (String) object[24];
					String zqjc = (String) object[26];
					String zwmc = (String) object[28];
					String ygp = (String) object[36];
					BigDecimal instanceid = (BigDecimal) object[39];
					BigDecimal baseid = (BigDecimal) object[40];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERNAME", username);
					map.put("STATUS", status);
					map.put("TYPE", type);
					map.put("TEL", tel);
					map.put("GPSJ", gpsj);
					map.put("ZQDM", zqdm);
					map.put("ZQJC", zqjc);
					map.put("YGP", ygp);
					map.put("ZWMC", zwmc);
					map.put("INSTANCEID", instanceid);
					map.put("VIEW", baseid==null?false:"".equals(baseid)?false:true);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getCurrentCustomerListPj(String cusername,String username,String userFullName,String customername,String zqdm,String zrfs,String cxddbg,String status,
			String type,String zwmc,BigDecimal jlr,String gpsj,BigDecimal zczbbegin,BigDecimal zczbend,String gfgsrqbegin,String gfgsrqend,
			int pageSize, int pageNow,String orderbygpsj,String orderbyzqdm,String ygp){
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		StringBuffer sb = new StringBuffer("SELECT CUSTOMER.*,BASE.ID BID FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERID,BOTABLE.STATUS,BOTABLE.TYPE,BOTABLE.ZYYW,BOTABLE.ZYCP,BOTABLE.JZYS,BOTABLE.NDYS,BOTABLE.JLR,BOTABLE.SLQK,BOTABLE.TEL,BOTABLE.EMAIL,BOTABLE.CZJGQBD,BOTABLE.GPSJ,BOTABLE.GFGSRQ,BOTABLE.YXGSRQ,BOTABLE.GDDH,BOTABLE.SHTZFS,BOTABLE.DSHSPQX,BOTABLE.MEMO,BOTABLE.CUSTOMERDESC,BOTABLE.ZQDM,BOTABLE.ZCDZ,BOTABLE.ZQJC,BOTABLE.SSHY,BOTABLE.ZWMC,BOTABLE.YWMC,BOTABLE.ZCZB,BOTABLE.JYXKXM,BOTABLE.GSWZ,BOTABLE.FDDBR,BOTABLE.PHONE,BOTABLE.FAX,BOTABLE.YGP,BOTABLE.XGZL,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_ZQB_KH_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		List params = new ArrayList();
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) like ? ");
			params.add("%"+customername.toUpperCase()+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like ? ");
			params.add("%"+zqdm+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(gpsj!=null&&!gpsj.equals("")){
			sb.append(" AND GPSJ= to_date( ? , 'yyyy-mm-dd')");
			params.add(gpsj);
		}
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		if(cusername!=null&&!cusername.equals("")){
			sb.append(" AND BOTABLE.USERNAME like  ? ");
			params.add("%"+cusername+"%");
		}
		sb.append(" AND (rtrim(BOTABLE.userid)='"+UserContextUtil.getInstance().getCurrentUserId()+"')");
		if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("1")&&orderbyzqdm.equals("0")){
			sb.append(" ORDER BY botable.gpsj desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		}else if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("0")&&orderbyzqdm.equals("1")){
			sb.append(" ORDER BY to_number(botable.zqdm) desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		}else{
			sb.append(" ORDER BY botable.ID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		}
		final List param = params;
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[2];
					String customerno = (String) object[3];
					String userid = (String) object[4];
					String status = (String) object[5];
					String type = (String) object[6];
					String tel = (String) object[13];
					Date gpsj = (Date) object[16];
					String zqdm = (String) object[24];
					String zqjc = (String) object[26];
					String zwmc = (String) object[28];
					String ygp = (String) object[36];
					BigDecimal instanceid = (BigDecimal) object[39];
					BigDecimal baseid = (BigDecimal) object[40];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERID", userid);
					map.put("STATUS", status);
					map.put("TYPE", type);
					map.put("TEL", tel);
					map.put("GPSJ", gpsj);
					map.put("ZQDM", zqdm);
					map.put("ZQJC", zqjc);
					map.put("YGP", ygp);
					map.put("ZWMC", zwmc);
					map.put("INSTANCEID", instanceid);
					map.put("VIEW", baseid==null?false:"".equals(baseid)?false:true);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getCurrentCustomerListPjSize(String cusername,String username,String userFullName,String customername,String customerno,String zrfs,String cxddbg,String status,
			String type,String zwmc,BigDecimal jlr,String gpsj,BigDecimal zczbbegin,BigDecimal zczbend,String gfgsrqbegin,String gfgsrqend,String ygp){
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.TYPE,BOTABLE.ZYYW,BOTABLE.ZYCP,BOTABLE.JZYS,BOTABLE.NDYS,BOTABLE.JLR,BOTABLE.SLQK,BOTABLE.TEL,BOTABLE.EMAIL,BOTABLE.CZJGQBD,BOTABLE.GPSJ,BOTABLE.GFGSRQ,BOTABLE.YXGSRQ,BOTABLE.GDDH,BOTABLE.SHTZFS,BOTABLE.DSHSPQX,BOTABLE.MEMO,BOTABLE.CUSTOMERDESC,BOTABLE.ZQDM,BOTABLE.ZCDZ,BOTABLE.ZQJC,BOTABLE.SSHY,BOTABLE.ZWMC,BOTABLE.YWMC,BOTABLE.ZCZB,BOTABLE.JYXKXM,BOTABLE.GSWZ,BOTABLE.FDDBR,BOTABLE.PHONE,BOTABLE.FAX,BOTABLE.YGP,BOTABLE.XGZL,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_ZQB_KH_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		List params = new ArrayList();
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) LIKE  ? ");
			params.add("%"+customername.toUpperCase()+"%");
		}
		if(customerno!=null&&!customerno.equals("")){
			sb.append(" AND CUSTOMERNO like  ? ");
			params.add("%"+customerno+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND ygp= ? ");
			params.add(ygp);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(gpsj!=null&&!gpsj.equals("")){
			sb.append(" AND GPSJ= to_date( ? , 'yyyy-mm-dd')");
			params.add(gpsj);
		}
		
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		if(cusername!=null&&!cusername.equals("")){
			sb.append(" AND BOTABLE.USERNAME like ? ");
			params.add("%"+cusername+"%");
		}
		sb.append(" AND (rtrim(userid)= ? )");
		params.add(UserContextUtil.getInstance().getCurrentUserId());
		sb.append(" ORDER BY botable.ID");
		final List param = params;
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[2];
					String customerno = (String) object[3];
					String username = (String) object[4];
					String status = (String) object[5];
					String tel = (String) object[13];
					String type = (String) object[6];
					BigDecimal instanceid = (BigDecimal) object[39];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERNAME", username);
					map.put("STATUS", status);
					map.put("TEL", tel);
					map.put("TYPE", type);
					map.put("INSTANCEID", instanceid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getCurrentCustomerListDuDao(String userFullName,int pageSize, int pageNow){
		final int pageSize1 = pageSize;
		final int startRow1 = pageNow;
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.KHFZR,BOTABLE.KHBH,BOTABLE.KHMC,BOTABLE.BZ,BOTABLE.TXDF,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_MDM_KHQXGLB  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=114 and BINDTABLE.metadataid=130 ");
		sb.append(" AND KHFZR = ? ");
		sb.append(" ORDER BY botable.ID desc");
		final String sql1 = sb.toString();
		final String cs = userFullName;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				
					if(cs!=null && !"".equals(cs)){
						
						if(d.HasInjectionData(cs)){
							return l;
						}
					
					
				}
				query.setParameter(0, cs);
				// Query query = session.createQuery(sql1);
				query.setFirstResult((startRow1-1)*pageSize1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String khfzr = (String) object[0];
					String khbh = (String) object[1];
					String khmc = (String) object[2];
					String bz = (String) object[3];
					String txdf = (String) object[4];
					BigDecimal id=(BigDecimal)object[5];
					BigDecimal instanceid=(BigDecimal)object[6];
					map.put("KHFZR", khfzr);
					map.put("KHBH", khbh);
					map.put("KHMC", khmc);
					map.put("BZ", bz);
					map.put("TXDF", txdf);
					map.put("ID", id);
					map.put("INSTANCEID", instanceid);
					l.add(map);
				}
				return l;
			}
		});
		}
	
	public List<HashMap> getCurrentCustomerListSize(String cusername,String customername,
			String zqdm,String zrfs,String cxddbg,String status,
			String type,String zwmc,BigDecimal jlr,String gpsjBEGIN,String zczbbegin,String zczbend,String gfgsrqbegin,String gfgsrqend,String ygp
			,String extend4,String zcqx,String sshy,String gpsjEND,String innovation,String classification,int numOther,int numKhfzr) {
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.TYPE,BOTABLE.ZYYW,BOTABLE.ZYCP,BOTABLE.JZYS,BOTABLE.NDYS,BOTABLE.JLR,BOTABLE.SLQK,BOTABLE.TEL,BOTABLE.EMAIL,BOTABLE.CZJGQBD,BOTABLE.GPSJ,BOTABLE.GFGSRQ,BOTABLE.YXGSRQ,BOTABLE.GDDH,BOTABLE.SHTZFS,BOTABLE.DSHSPQX,BOTABLE.MEMO,BOTABLE.CUSTOMERDESC,BOTABLE.ZQDM,BOTABLE.ZCDZ,BOTABLE.ZQJC,BOTABLE.SSHY,BOTABLE.ZWMC,BOTABLE.YWMC,BOTABLE.ZCZB,BOTABLE.JYXKXM,BOTABLE.GSWZ,BOTABLE.FDDBR,BOTABLE.PHONE,BOTABLE.FAX,BOTABLE.YGP,BOTABLE.XGZL,BOTABLE.ID,BINDTABLE.INSTANCEID , BOTABLE.Zcqx, BOTABLE.Extend4, BOTABLE.Innovation,  BOTABLE.Classification FROM BD_ZQB_KH_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		OrgUser uc = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		List params = new ArrayList();
		/*----------------华丽分割线,权限控制-------------*/
		if(numOther==0&&numKhfzr>0&&uc.getOrgroleid()!=5l){
			sb.append(" AND (CUSTOMERNO IN(SELECT B.KHBH FROM BD_MDM_KHQXGLB B WHERE B.KHFZR IS NOT NULL AND (SUBSTR(B.KHFZR,0, INSTR(B.KHFZR,'[',1)-1) = ? OR SUBSTR(B.KHFZR,0, INSTR(B.KHFZR,'[',1)-1) = ?)) OR USERID =?)");
			params.add(uc.getUserid());
			params.add(uc.getUserid());
			params.add(uc.getUserid());
		}else if(uc.getOrgroleid()==(long)3){
			sb.append(" AND CUSTOMERNO = ? ");
			params.add(uc.getExtend1());
		}else if(numOther==0&&numKhfzr==0&&uc.getOrgroleid()!=5l){
			sb.append(" AND CUSTOMERNO = '' ");
		}
		/*----------------华丽分割线,权限控制-------------*/
		
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) LIKE ? ");
			params.add("%"+customername.toUpperCase()+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like ? ");
			params.add("%"+zqdm+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		if(cusername!=null&&!cusername.equals("")){
			sb.append(" AND BOTABLE.USERNAME like ? ");
			params.add("%"+cusername+"%");
		}
		if(gpsjBEGIN!=null&&!gpsjBEGIN.equals("")){
			sb.append(" AND GPSJ>= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gpsjBEGIN);
		}
		if (gpsjEND != null && !gpsjEND.equals("")) {
			sb.append(" AND GPSJ<= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gpsjEND);
		}
		if (extend4 != null && !extend4.equals("")) {				
			sb.append(" AND EXTEND4 like ? ");//所属证监局
			params.add("%"+extend4+"%");
		}
		if (zcqx != null && !zcqx.equals("")) {//所属部门或所属大区				
			sb.append(" AND ZCQX like ? ");
			params.add("%"+zcqx+"%");
		}
		if (sshy != null && !sshy.equals("")) {				
			sb.append(" AND SSHY like  ? ");//所属行业
			params.add("%"+sshy+"%");
		}
		if (innovation != null && !innovation.equals("")) {				
			sb.append(" AND INNOVATION= ? ");//创新层
			params.add(innovation);
		}
		if (classification != null && !classification.equals("")) {				
			sb.append(" AND CLASSIFICATION= ? ");//分类
			params.add(classification);
		}
		sb.append(" ORDER BY botable.ID desc");
		final List param= params;
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String khmc = (String) object[2];
					String zqjc = (String) object[26];
					String zqdm = (String) object[24];
					String xpr = (String) object[4];
					String xprdh = (String) object[13];
					String ssbm=(String)object[40];
					String sszjj=(String)object[41];
					String khzt = (String) object[5];
                    Date gpsj = (Date) object[16];
                    String gpzt = (String) object[36];
                    String cxc = (String) object[42];
                    String gspj = (String) object[43];
					map.put("khmc", khmc);
					map.put("zqjc", zqjc);
					map.put("zqdm", zqdm);
					map.put("xpr", xpr);
					map.put("xprdh", xprdh);
					map.put("ssbm", ssbm);
					map.put("sszjj", sszjj);
					map.put("khzt", khzt);
                    map.put("gpsj", gpsj);
                    map.put("gpzt", gpzt);
                    map.put("cxc", cxc);
                    map.put("gspj", gspj);

					l.add(map);
				}
				return l;
			}
		});
	}


	public List<HashMap> getCurrentCustomerListDuDaoSize(String cusername,String userFullName, String customername, String customerno, String zrfs,String cxddbg,String status,
			String type,String zwmc,BigDecimal jlr,String gpsj,BigDecimal zczbbegin,BigDecimal zczbend,String gfgsrqbegin,String gfgsrqend,String ygp) {
		//浠ヤ笅涓鸿嚜宸卞垱寤虹殑瀹㈡埛
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.CREATEUSER,BOTABLE.CUSTOMERNAME," +
				"BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.YGP,BOTABLE.TEL,BINDTABLE.INSTANCEID FROM BD_ZQB_KH_BASE  " +
				"BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID " +
				"is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		List params = new ArrayList();
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) LIKE ? ");
			params.add("%"+customername.toUpperCase()+"%");
		}
		if(customerno!=null&&!customerno.equals("")){
			sb.append(" AND CUSTOMERNO like ? ");
			params.add("%"+customerno+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(gpsj!=null&&!gpsj.equals("")){
			sb.append(" AND GPSJ= to_date( ? , 'yyyy-mm-dd')");
			params.add(gpsj);
		}
		
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		if(cusername!=null&&!cusername.equals("")){
			sb.append(" AND BOTABLE.USERNAME like  ? ");
			params.add("%"+cusername+"%");
		}
		sb.append(" AND (rtrim(userid)= ? )");
		params.add(UserContextUtil.getInstance().getCurrentUserId());
		//浠ヤ笅涓烘寔缁潱瀵煎垎娲剧粰鍏剁殑
		sb.append("union  SELECT distinct BOTABLE.CREATEUSER,BOTABLE.CUSTOMERNAME," +
				"BOTABLE.CUSTOMERNO,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.YGP,BOTABLE.TEL,BINDTABLE.INSTANCEID FROM BD_MDM_KHQXGLB  BOTABLECXDUFP right join" +
				"    BD_ZQB_KH_BASE   " +
				"BOTABLE on  BOTABLECXDUFP.KHBH=BOTABLE.CUSTOMERNO  left join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID " +
				"is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 where 1=1 and  (BOTABLECXDUFP.KHFZR = ? or BOTABLECXDUFP.ZZCXDD= ? or BOTABLECXDUFP.FHSPR= ? or BOTABLECXDUFP.ZZSPR= ? )");
		params.add(userFullName);
		params.add(userFullName);
		params.add(userFullName);
		params.add(userFullName);
		final List param = params;
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[1];
					String customerno = (String) object[2];
					String username = (String) object[3];
					String status = (String) object[4];
					String ygp = (String) object[5];
					String tel = (String) object[6];
					BigDecimal instanceid = (BigDecimal) object[7];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("USERNAME", username);
					map.put("STATUS", status);
					map.put("TEL", tel);
					map.put("YGP", ygp);
					map.put("INSTANCEID", instanceid);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getCurrentCustomerListDDAll(String username,String userFullName,String customername,
			String zqdm,String zrfs,String cxddbg,String status,String type,String zwmc,BigDecimal jlr,String gpsj,BigDecimal zczbbegin,BigDecimal zczbend,String gfgsrqbegin,String gfgsrqend,
			boolean flag,String orderbygpsj,String orderbyzqdm,String ygp){
		//浠ヤ笅涓鸿嚜宸卞垱寤虹殑瀹㈡埛
		StringBuffer sb = new StringBuffer("SELECT CUSTOMER.*,BASE.ID FROM (SELECT * FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CUSTOMERNAME," +
				"BOTABLE.ZQDM,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.YGP,BOTABLE.TEL,BINDTABLE.INSTANCEID,BOTABLE.CUSTOMERNO,BOTABLE.ID BID,BOTABLE.GPSJ BGPSJ FROM BD_ZQB_KH_BASE  " +
				"BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID " +
				"is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		List params = new ArrayList();
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%"+customername+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like ? ");
			params.add("%"+zqdm+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		sb.append(" AND (rtrim(userid)= ? )");
		params.add(UserContextUtil.getInstance().getCurrentUserId());
		//浠ヤ笅涓烘寔缁潱瀵煎垎娲剧粰鍏剁殑
		if(flag){
			sb.append("union  SELECT distinct BOTABLE.CREATEUSER,BOTABLE.CUSTOMERNAME," +
				"BOTABLE.ZQDM,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.YGP,BOTABLE.TEL,BINDTABLE.INSTANCEID,BOTABLE.CUSTOMERNO,BOTABLE.ID BID,BOTABLE.GPSJ BGPSJ FROM BD_MDM_KHQXGLB  BOTABLECXDUFP right join" +
				"    BD_ZQB_KH_BASE   " +
				"BOTABLE on  BOTABLECXDUFP.KHBH=BOTABLE.CUSTOMERNO  left join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID " +
				"is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 where 1=1 and  (BOTABLECXDUFP.KHFZR = ? or BOTABLECXDUFP.ZZCXDD=? or BOTABLECXDUFP.FHSPR=? or BOTABLECXDUFP.ZZSPR=?)");
			params.add(userFullName);
			params.add(userFullName);
			params.add(userFullName);
			params.add(userFullName);
		}
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%"+customername+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like  ? ");
			params.add("%"+zqdm+"%");
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE= ? ");
			params.add(type);
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC= ? ");
			params.add(zwmc);
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(gpsj!=null&&!gpsj.equals("")){
			sb.append(" AND GPSJ= to_date( ? , 'yyyy-mm-dd')");
			params.add(gpsj);
		}
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		//sb.append(" ) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("1")&&orderbyzqdm.equals("0")){
			sb.append(" ) ORDER BY BGPSJ desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("0")&&orderbyzqdm.equals("1")){
			sb.append(" ) ORDER BY zqdm desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else{
			sb.append(" ) ORDER BY BID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}
		final List param =params;
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[1];
					String zqdm = (String) object[2];
					String username = (String) object[3];
					String status = (String) object[4];
					String ygp = (String) object[5];
					String tel = (String) object[6];
					BigDecimal instanceid = (BigDecimal) object[7];
					String customerno = (String) object[8];
					BigDecimal baseid = (BigDecimal) object[11];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("ZQDM", zqdm);
					map.put("USERNAME", username);
					map.put("STATUS", status);
					map.put("TEL", tel);
					map.put("YGP", ygp);
					map.put("INSTANCEID", instanceid);
					map.put("VIEW", baseid==null?false:"".equals(baseid)?false:true);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getCurrentCustomerListDD(String cusername,String username,String userFullName,String customername,
			String zqdm,String zrfs,String cxddbg,String status,String type,String zwmc,BigDecimal jlr,String gpsj,BigDecimal zczbbegin,BigDecimal zczbend,String gfgsrqbegin,String gfgsrqend,
			int pageSize, int pageNow,boolean flag,String orderbygpsj,String orderbyzqdm,String ygp){
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		//浠ヤ笅涓鸿嚜宸卞垱寤虹殑瀹㈡埛
		StringBuffer sb = new StringBuffer("SELECT CUSTOMER.*,BASE.ID FROM (SELECT * FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CUSTOMERNAME," +
				"BOTABLE.ZQDM,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.YGP,BOTABLE.TEL,BINDTABLE.INSTANCEID,BOTABLE.CUSTOMERNO,BOTABLE.ID BID,BOTABLE.GPSJ BGPSJ FROM BD_ZQB_KH_BASE  " +
				"BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID " +
				"is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 ");
		List params=new ArrayList();
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND UPPER(CUSTOMERNAME) LIKE ? ");
			params.add("%"+customername.toUpperCase()+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like  ? ");
			params.add( "%"+zqdm+"%");
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE like  ? ");
			params.add("%"+type+"%");
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC like ? ");
			params.add("%"+zwmc+"%");
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
			
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
	
		}
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add( gfgsrqend);
		}
		if(cusername!=null&&!cusername.equals("")){
			sb.append(" AND BOTABLE.USERNAME like ? ");
			params.add("%"+cusername+"%");
		}
		sb.append(" AND (rtrim(userid)='"+UserContextUtil.getInstance().getCurrentUserId()+"')");
	
		//浠ヤ笅涓烘寔缁潱瀵煎垎娲剧粰鍏剁殑
		if(flag){
			sb.append("union  SELECT distinct BOTABLE.CREATEUSER,BOTABLE.CUSTOMERNAME," +
				"BOTABLE.ZQDM,BOTABLE.USERNAME,BOTABLE.STATUS,BOTABLE.YGP,BOTABLE.TEL,BINDTABLE.INSTANCEID,BOTABLE.CUSTOMERNO,BOTABLE.ID BID,BOTABLE.GPSJ BGPSJ FROM BD_MDM_KHQXGLB  BOTABLECXDUFP right join" +
				"    BD_ZQB_KH_BASE   " +
				"BOTABLE on  BOTABLECXDUFP.KHBH=BOTABLE.CUSTOMERNO  left join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID " +
				"is not null and BINDTABLE.formid=88 and BINDTABLE.metadataid=102 where 1=1 and  (BOTABLECXDUFP.KHFZR = ? or BOTABLECXDUFP.ZZCXDD=? or BOTABLECXDUFP.FHSPR=? or BOTABLECXDUFP.ZZSPR= ? )");
			params.add(userFullName);
			params.add(userFullName);
			params.add(userFullName);
			params.add(userFullName);
		}
		if(zrfs!=null&&!zrfs.equals("")){
			sb.append(" AND ZRFS= ? ");
			params.add(zrfs);
		}
		if(cxddbg!=null&&!cxddbg.equals("")){
			sb.append(" AND CXDDBG= ? ");
			params.add(cxddbg);
		}
		if(customername!=null&&!customername.equals("")){
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%"+customername+"%");
		}
		if(zqdm!=null&&!zqdm.equals("")){
			sb.append(" AND ZQDM like ? ");
			params.add("%"+zqdm+"%");
		}
		if(status!=null&&!status.equals("")){
			sb.append(" AND STATUS= ? ");
			params.add(status);
		}
		if(type!=null&&!type.equals("")){
			sb.append(" AND TYPE=? ");
			params.add( type);
		}
		if(zwmc!=null&&!zwmc.equals("")){
			sb.append(" AND ZWMC= ? ");
			params.add(zwmc);
		}
		if(jlr!=null&&!jlr.equals("")){
			sb.append(" AND JLR= ? ");
			params.add(jlr);
		}
		if(ygp!=null&&!ygp.equals("")){
			sb.append(" AND YGP= ? ");
			params.add(ygp);
		}
		if(gpsj!=null&&!gpsj.equals("")){
			sb.append(" AND GPSJ= to_date(? , 'yyyy-mm-dd')");
			params.add(gpsj);
		}
		if(zczbbegin!=null&&!zczbbegin.equals("")){
			sb.append(" AND ZCZB>= ? ");
			params.add(zczbbegin);
		}
		if(zczbend!=null&&!zczbend.equals("")){
			sb.append(" AND ZCZB<= ? ");
			params.add(zczbend);
		}
		if(gfgsrqbegin!=null&&!gfgsrqbegin.equals("")){
			sb.append(" AND GFGSRQ >= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqbegin);
		}
		if(gfgsrqend!=null&&!gfgsrqend.equals("")){
			sb.append(" AND GFGSRQ <= TO_DATE( ? , 'yyyy-mm-dd')");
			params.add(gfgsrqend);
		}
		if(cusername!=null&&!cusername.equals("")){
			sb.append(" AND BOTABLE.USERNAME like ? ");
			params.add("%"+cusername+"%");
		}
		//sb.append(" ) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.CUSTOMERNO=BASE.CUSTOMERNO");
		if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("1")&&orderbyzqdm.equals("0")){
			sb.append(" ) ORDER BY BGPSJ desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else if(orderbygpsj != null && orderbyzqdm !=null &&orderbygpsj.equals("0")&&orderbyzqdm.equals("1")){
			sb.append(" ) ORDER BY zqdm desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}else{
			sb.append(" ) ORDER BY BID desc) CUSTOMER LEFT JOIN BD_ZQB_PJ_BASE BASE ON CUSTOMER.Customerno=BASE.CUSTOMERNO");
		}
		final List param=params;
		final String sql1 = sb.toString();
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
				// Query query = session.createQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String createuser = (String) object[0];
					String customername = (String) object[1];
					String zqdm = (String) object[2];
					String username = (String) object[3];
					String status = (String) object[4];
					String ygp = (String) object[5];
					String tel = (String) object[6];
					BigDecimal instanceid = (BigDecimal) object[7];
					String customerno = (String) object[8];
					BigDecimal baseid = (BigDecimal) object[11];
					map.put("CREATEUSER", createuser);
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					map.put("ZQDM", zqdm);
					map.put("USERNAME", username);
					map.put("STATUS", status);
					map.put("TEL", tel);
					map.put("YGP", ygp);
					map.put("INSTANCEID", instanceid);
					map.put("VIEW", baseid==null?false:"".equals(baseid)?false:true);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getAssociateCustomerList(String khmc){
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		sql.append("SELECT BIND.INSTANCEID INSTANCEID,KH.* FROM BD_ZQB_KH_BASE KH INNER JOIN (SELECT * FROM  SYS_ENGINE_FORM_BIND WHERE FORMID=88 AND METADATAID=102) BIND ON KH.ID=BIND.DATAID WHERE 1=1");
		if(khmc!=null&&!khmc.equals("")){
			String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]\\\\.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’、？]";
			sql.append(" AND KH.CUSTOMERNAME LIKE ?");
			params.add("%"+khmc.replaceAll(regEx, "")+"%");
		}
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
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					map = new HashMap();
					BigDecimal instanceid = (BigDecimal)object[0];
					String customername = (String)object[4];
					String customerno = (String)object[5];
					map.put("INSTANCEID", instanceid.toString());
					map.put("CUSTOMERNAME", customername);
					map.put("CUSTOMERNO", customerno);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> getAssociateUserList(String commonstr, String customerno){
		StringBuffer sql = new StringBuffer();
		List params = new ArrayList();
		sql.append("SELECT USERNAME,DEPARTMENTNAME,MOBILE,EMAIL FROM ORGUSER WHERE 1=1 AND MOBILE IS NOT NULL AND EMAIL IS NOT NULL");
		if(customerno!=null&&!customerno.equals("")){
			sql.append(" AND EXTEND1=?");
			params.add(customerno);
		}
		if(commonstr!=null&&!commonstr.equals("")){
			String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]\\\\.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’、？]";
			sql.append(" AND (USERNAME LIKE ? OR DEPARTMENTNAME LIKE ?)");
			params.add("%"+commonstr.replaceAll(regEx, "")+"%");
			params.add("%"+commonstr.replaceAll(regEx, "")+"%");
		}
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
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String USERNAME = (String)object[0];
					String DEPARTMENTNAME = (String)object[1];
					String MOBILE = (String)object[2];
					String EMAIL = (String)object[3];
					map.put("USERNAME", USERNAME);
					map.put("DEPARTMENTNAME", DEPARTMENTNAME);
					map.put("MOBILE", MOBILE);
					map.put("EMAIL", EMAIL);
					l.add(map);
				}
				return l;
			}
		});
	}
}
