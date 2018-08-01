package com.ibpmsoft.project.zqb.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.log.util.LogUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import org.apache.log4j.Logger;
public class ZqbCxddManagementService {
	private static Logger logger = Logger.getLogger(ZqbCxddManagementService.class);
	public List<HashMap<String, Object>> doSearch(String col,String rule,String cxddzy, String zzcxdd,
			String khmc, String khbh,String zqdm, int pageNumber, int pageSize) {
		List list = new ArrayList<HashMap<String, Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		// “项目负责人”只显示登陆用户负责的项目的客户信息
		// 场外经理看到所有
		boolean isSuperMan = this.getIsSuperMan();
		if (isSuperMan) {// 场外经理看到所有
			String gpdm="";
			String gpdm1="";
			if(zqdm!=null&&!"".equals(zqdm)){
				String[] split = zqdm.split(",");
				for (String string : split) {
					if(!"".equals(string)&&string!=null){
						gpdm+=string+",";
					}
				}
				gpdm1 = gpdm.substring(0, gpdm.length()-1);
			}
			HashMap mapData=new HashMap();
			StringBuffer sql = new StringBuffer("SELECT * FROM (SELECT  B.*,ROWNUM R FROM (SELECT A.ID,KHBH,KHMC,");
			sql.append("SUBSTR(KHFZR,INSTR(KHFZR,'[',1,1)+1,INSTR(KHFZR,']',1,1)-INSTR(KHFZR,'[',1,1)-1) KHFZR,");
			sql.append("SUBSTR(ZZCXDD,INSTR(ZZCXDD,'[',1,1)+1,INSTR(ZZCXDD,']',1,1)-INSTR(ZZCXDD,'[',1,1)-1) ZZCXDD,");
			sql.append("SUBSTR(FHSPR,INSTR(FHSPR,'[',1,1)+1,INSTR(FHSPR,']',1,1)-INSTR(FHSPR,'[',1,1)-1) FHSPR,");
			sql.append("SUBSTR(ZZSPR,INSTR(ZZSPR,'[',1,1)+1,INSTR(ZZSPR,']',1,1)-INSTR(ZZSPR,'[',1,1)-1) ZZSPR,");
			sql.append("SUBSTR(GGFBR,INSTR(GGFBR,'[',1,1)+1,INSTR(GGFBR,']',1,1)-INSTR(GGFBR,'[',1,1)-1) GGFBR,");
			sql.append("C.INSTANCEID,");
			sql.append("SUBSTR(CWSCBFZR2,INSTR(CWSCBFZR2,'[',1,1)+1,INSTR(CWSCBFZR2,']',1,1)-INSTR(CWSCBFZR2,'[',1,1)-1) CWSCBFZR2,");
			sql.append("SUBSTR(CWSCBFZR3,INSTR(CWSCBFZR3,'[',1,1)+1,INSTR(CWSCBFZR3,']',1,1)-INSTR(CWSCBFZR3,'[',1,1)-1) CWSCBFZR3,");
			sql.append("F.ZQDM,");
			sql.append("SUBSTR(QYNBRYSH,INSTR(QYNBRYSH,'[',1,1)+1,INSTR(QYNBRYSH,']',1,1)-INSTR(QYNBRYSH,'[',1,1)-1) QYNBRYSH,");
			sql.append("SUBSTR(A.FBBWJSHR,INSTR(A.FBBWJSHR,'[',1,1)+1,INSTR(A.FBBWJSHR,']',1,1)-INSTR(A.FBBWJSHR,'[',1,1)-1) FBBWJSHR");
			sql.append(" FROM BD_MDM_KHQXGLB A,");
			sql.append("SYS_ENGINE_FORM_BIND C,BD_ZQB_KH_BASE F WHERE 1=1 ");
			sql.append(" AND FORMID = '114'        AND METADATAID = '130'    AND A.ID = C.DATAID  AND C.INSTANCEID IS NOT NULL  AND A.KHBH=F.CUSTOMERNO ");
			if(!"".equals(gpdm1)){
				String[] gpdmArr = gpdm1.split(",");
				String zc="";
				for (String string : gpdmArr) {
					zc += "?,";
				}
				String zzc = zc.substring(0, zc.length() - 1);
				mapData.put("zqdm", gpdm1);
				sql.append(" AND  KHBH IN(SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE ZQDM IN (");
				sql.append(zzc + "))");
			}
			if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
				sql.append(" AND KHBH=? ");
				mapData.put("khbh", khbh);
			}
			if (khmc != null && !khmc.equals("")) {
				sql.append(" AND KHMC LIKE ? ");
				mapData.put("khmc", khmc);
			}
			if (cxddzy != null && !cxddzy.equals("")) {
				sql.append(" AND UPPER(TRIM(KHFZR)) LIKE ? ");
				mapData.put("cxddzy", cxddzy.trim().toUpperCase());
			}
			if (zzcxdd != null && !zzcxdd.equals("")) {
				sql.append(" AND UPPER(TRIM(FHSPR)) LIKE ? ");
				mapData.put("zzcxdd", zzcxdd.trim().toUpperCase());
			}
			if(col!=null&&!col.equals("")){
				if(col.equals("j")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY ZQDM ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY ZQDM DESC");
				}
				if(col.equals("a")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY QYNBRYSH ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY QYNBRYSH DESC");
				}
				if(col.equals("b")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY KHFZR ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY KHFZR DESC");
				}
				if(col.equals("c")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY ZZCXDD ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY ZZCXDD DESC");
				}
				if(col.equals("d")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY FHSPR ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY FHSPR DESC");
				}
				if(col.equals("e")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY ZZSPR ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY ZZSPR DESC");
				}
				if(col.equals("f")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY CWSCBFZR2 ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY CWSCBFZR2 DESC");
				}
				if(col.equals("g")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY CWSCBFZR3 ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY CWSCBFZR3 DESC");
				}
				if(col.equals("h")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY FBBWJSHR ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY FBBWJSHR DESC");
				}
				if(col.equals("i")){
					if(rule!=null&&!rule.equals("")&&rule.equals("1")) sql.append(" ORDER BY GGFBR ASC");
					if(rule!=null&&!rule.equals("")&&rule.equals("2")) sql.append(" ORDER BY GGFBR DESC");
				}
			}else{
				sql.append(" ORDER BY ID DESC");
			}
			sql.append(") B ) WHERE  R>? AND R<=?");
			
			mapData.put("startRow", startRow);
			mapData.put("endRow", endRow);
			// 查询数据库
			Connection conn = DBUtil.open();
			PreparedStatement stmt=null;
			ResultSet rs =null;
			try {
				int index=1;
				stmt =conn.prepareStatement(sql.toString());
				if(!"".equals(gpdm1)){
					String zqdmcs=mapData.get("zqdm").toString();
					String[] split = zqdmcs.split(",");
					for (int i = 0; i < split.length; i++) {
						stmt.setString(index++, split[i]);
					}
				}
				if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
					stmt.setString(index++, mapData.get("khbh").toString());
				}
				if (khmc != null && !khmc.equals("")) {
					stmt.setString(index++, "%"+mapData.get("khmc").toString()+"%");
				}
				if (cxddzy != null && !cxddzy.equals("")) {
					stmt.setString(index++, "%"+mapData.get("cxddzy").toString()+"%");
				}
				if (zzcxdd != null && !zzcxdd.equals("")) {
					stmt.setString(index++, "%"+mapData.get("zzcxdd").toString()+"%");
				}
				stmt.setInt(index++, startRow);
				stmt.setInt(index++, endRow);
				rs = stmt.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					BigDecimal id = rs.getBigDecimal(1);
					String bh = rs.getString(2);
					String mc = rs.getString(3);
					String fzr = rs.getString(4);
					String zzcx = rs.getString(5);
					String fhsp = rs.getString(6);
					String zzsp = rs.getString(7);
					String ggfb = rs.getString(8);
					BigDecimal instanceid = rs.getBigDecimal(9);
					String cwscbfzr2 = rs.getString(10);
					String cwscbfzr3 = rs.getString(11);
					String dZqdm = rs.getString(12);
					String qynbrysh = rs.getString(13);
					String fbbwjshr = rs.getString(14);
					map.put("ID", id);
					map.put("KHBH", bh);
					map.put("KHMC", mc);
					map.put("KHFZR", fzr);
					map.put("ZZCXDD", zzcx);
					map.put("FHSPR", fhsp);
					map.put("ZZSPR", zzsp);
					map.put("GGFBR", ggfb);
					map.put("CWSCBFZR2", cwscbfzr2);
					map.put("CWSCBFZR3", cwscbfzr3);
					map.put("INSTANCEID", instanceid.toString());
					map.put("ZQDM", dZqdm);
					map.put("QYNBRYSH", qynbrysh);
					map.put("FBBWJSHR", fbbwjshr);
					list.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rs);
			}
		} else {// 其他角色只看到自己负责的
			// String userid = UserContextUtil.getInstance().getCurrentUserId();
			String userfullname = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			String gpdm="";
			String gpdm1="";
			if(zqdm!=null&&!"".equals(zqdm)){
				String[] split = zqdm.split(",");
				for (String string : split) {
					if(!"".equals(string)&&string!=null){
						gpdm+=string+",";
					}
				}
				gpdm1 = gpdm.substring(0, gpdm.length()-1);
			}
			HashMap mapData=new HashMap();
			StringBuffer sql = new StringBuffer(
					"select * from (select  b.*,rownum r from (select a.id,khbh,khmc,khfzr,zzcxdd,fhspr,zzspr,ggfbr,c.instanceid,CWSCBFZR2,CWSCBFZR3,f.zqdm,a.fbbwjshr from BD_MDM_KHQXGLB a,");
			sql.append("SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f where 1=1 ");
			sql.append(" and formid = '114'        and metadataid = '130'    and a.id = c.dataid  and c.instanceid is not null  and a.khbh=f.customerno ");
			sql.append(" and  khbh in(select bp.customerno from BD_ZQB_PJ_BASE bp");
			if(zqdm!=null&&!"".equals(zqdm)){
				String[] gpdmArr = gpdm1.split(",");
				String zc="";
				for (String string : gpdmArr) {
					zc += "?,";
				}
				String zzc = zc.substring(0, zc.length() - 1);
				mapData.put("zqdm", gpdm1);
				sql.append(",(select customerno from bd_zqb_kh_base where zqdm in ("+zzc+")) bk ");
			}
			sql.append(" where owner=? ");
			mapData.put("userfullname", userfullname);
			if(zqdm!=null&&!"".equals(zqdm)){
				sql.append(" and bp.customerno=bk.customerno");
			}
			sql.append(") ");
			if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
				sql.append(" and khbh=? ");
				mapData.put("khbh", khbh);
			}
			if (khmc != null && !khmc.equals("")) {
				sql.append(" and khmc like ? ");
				mapData.put("khmc", khmc);
			}
			if (cxddzy != null && !cxddzy.equals("")) {
				sql.append(" and UPPER(TRIM(khfzr)) like ? ");
				mapData.put("cxddzy", cxddzy.trim().toUpperCase());
			}
			if (zzcxdd != null && !zzcxdd.equals("")) {
				sql.append(" and UPPER(TRIM(FHSPR)) like ? ");
				mapData.put("zzcxdd", zzcxdd.trim().toUpperCase());
			}
			sql.append(") b) where  r>? and r<=?");
			mapData.put("startRow",startRow);
			mapData.put("endRow", endRow);
			// 查询数据库
			Connection conn = DBUtil.open();
			PreparedStatement stmt=null;
			ResultSet rs =null;
			try {
				int index=1;
				stmt =conn.prepareStatement(sql.toString());
				if(zqdm!=null&&!"".equals(zqdm)){
					String zqdmcs=mapData.get("zqdm").toString();
					String[] split = zqdmcs.split(",");
					for (int i = 0; i < split.length; i++) {
						stmt.setString(index++, split[i]);
					}
				}
				stmt.setString(index++, mapData.get("userfullname").toString());
				if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
					stmt.setString(index++, mapData.get("khbh").toString());
				}
				if (khmc != null && !khmc.equals("")) {
					stmt.setString(index++, "%"+mapData.get("khmc").toString()+"%");
				}
				if (cxddzy != null && !cxddzy.equals("")) {
					stmt.setString(index++, "%"+mapData.get("cxddzy").toString()+"%");
				}
				if (zzcxdd != null && !zzcxdd.equals("")) {
					stmt.setString(index++, "%"+mapData.get("zzcxdd").toString()+"%");
				}
				stmt.setInt(index++, startRow);
				stmt.setInt(index++, endRow);
				rs = stmt.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					BigDecimal id = rs.getBigDecimal(1);
					String bh = rs.getString(2);
					String mc = rs.getString(3);
					String fzr = rs.getString(4);
					String zzcx = rs.getString(5);
					String fhsp = rs.getString(6);
					String zzsp = rs.getString(7);
					String ggfb = rs.getString(8);
					BigDecimal instanceid = rs.getBigDecimal(9);
					String cwscbfzr2 = rs.getString(10);
					String cwscbfzr3 = rs.getString(11);
					String dZqdm = rs.getString(12);
					String fbbwjshr = rs.getString(13);
					map.put("ID", id);
					map.put("KHBH", bh);
					map.put("KHMC", mc);
					map.put("KHFZR", fzr);
					map.put("ZZCXDD", zzcx);
					map.put("FHSPR", fhsp);
					map.put("ZZSPR", zzsp);
					map.put("GGFBR", ggfb);
					map.put("CWSCBFZR2", cwscbfzr2);
					map.put("CWSCBFZR3", cwscbfzr3);
					map.put("INSTANCEID", instanceid);
					map.put("ZQDM", dZqdm);
					map.put("FBBWJSHR", fbbwjshr);
					list.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rs);
			}

		}
		return list;
	}

	public boolean getIsSuperMan() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			//xlj update 2017年1月4日09:49:59 更改为场外、质控部负责人、持续督导专员角色能看到所有分配信息
			if (orgRoleId.equals(new Long(5))||orgRoleId.equals(new Long(9))||orgRoleId.equals(new Long(4))) {
				flag = true;
			}
		}
		return flag;
	}

	public String delete(Long instanceId) {
		HashMap map = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value=map.get("KHMC")==null?"":map.get("KHMC").toString();
		Long dataId=Long.parseLong(map.get("ID").toString());
		boolean flag = DemAPI.getInstance().removeFormData(instanceId);
		String info = "";
		if (!flag) {
			info = "删除失败";
		} else {
			info = "success";
			LogUtil.getInstance().addLog(dataId, "持续督导分派", "删除持续督导分派："+value);
		}
		return info;
	}

	public int getTotalNum(String cxddzy, String zzcxdd, String khmc,
			String khbh,String zqdm) {
		int totalNum = 0;
		// “项目负责人”只显示登陆用户负责的项目的客户信息
		// 场外经理看到所有
		boolean isSuperMan = this.getIsSuperMan();
		if (isSuperMan) {// 场外经理看到所有
			String gpdm="";
			String gpdm1="";
			if(zqdm!=null&&!"".equals(zqdm)){
				String[] split = zqdm.split(",");
				for (String string : split) {
					if(!"".equals(string)&&string!=null){
						gpdm+=string+",";
					}
				}
				gpdm1 = gpdm.substring(0, gpdm.length()-1);
			}
			HashMap mapData=new HashMap();
			StringBuffer sql = new StringBuffer(
					"select count(*) from (select  b.*,rownum r from (select a.id,khbh,khmc,khfzr,zzcxdd,fhspr,zzspr,ggfbr,c.instanceid,CWSCBFZR2,CWSCBFZR3,f.zqdm from BD_MDM_KHQXGLB a,");
			sql.append("SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f where 1=1 ");
			sql.append(" and formid = '114'        and metadataid = '130'    and a.id = c.dataid  and c.instanceid is not null  and a.khbh=f.customerno ");
			if(!"".equals(gpdm1)){
				String[] gpdmArr = gpdm1.split(",");
				String zc="";
				for (String string : gpdmArr) {
					zc += "?,";
				}
				String zzc = zc.substring(0, zc.length() - 1);
				mapData.put("zqdm", gpdm1);
				sql.append(" and  khbh in(select customerno from bd_zqb_kh_base where zqdm in (");
				sql.append(zzc + "))");
			}
			if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
				sql.append(" and khbh=? ");
				mapData.put("khbh", khbh);
			}
			if (khmc != null && !khmc.equals("")) {
				sql.append(" and khmc like ? ");
				mapData.put("khmc", khmc);
			}
			if (cxddzy != null && !cxddzy.equals("")) {
				sql.append(" and UPPER(TRIM(khfzr)) like ? ");
				mapData.put("cxddzy", cxddzy.trim().toUpperCase());
			}
			if (zzcxdd != null && !zzcxdd.equals("")) {
				sql.append(" and UPPER(TRIM(FHSPR)) like ? ");
				mapData.put("zzcxdd", zzcxdd.trim().toUpperCase());
			}
			sql.append("  order by id desc) b )");
			// 查询数据库
			Connection conn = DBUtil.open();
			PreparedStatement stmt=null;
			ResultSet rs =null;
			try {
				int index=1;
				stmt =conn.prepareStatement(sql.toString());
				if(!"".equals(gpdm1)){
					String zqdmcs=mapData.get("zqdm").toString();
					String[] split = zqdmcs.split(",");
					for (int i = 0; i < split.length; i++) {
						stmt.setString(index++, split[i]);
					}
				}
				if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
					stmt.setString(index++, mapData.get("khbh").toString());
				}
				if (khmc != null && !khmc.equals("")) {
					stmt.setString(index++, "%"+mapData.get("khmc").toString()+"%");
				}
				if (cxddzy != null && !cxddzy.equals("")) {
					stmt.setString(index++, "%"+mapData.get("cxddzy").toString()+"%");
				}
				if (zzcxdd != null && !zzcxdd.equals("")) {
					stmt.setString(index++, "%"+mapData.get("zzcxdd").toString()+"%");
				}
				rs = stmt.executeQuery();
				if (rs.next()) {
					totalNum = rs.getInt(1);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rs);
			}
		} else {// 其他角色只看到自己负责的
			// String userid = UserContextUtil.getInstance().getCurrentUserId();
			String userfullname = UserContextUtil.getInstance()
					.getCurrentUserFullName();
			String gpdm="";
			String gpdm1="";
			if(zqdm!=null&&!"".equals(zqdm)){
				String[] split = zqdm.split(",");
				for (String string : split) {
					if(!"".equals(string)&&string!=null){
						gpdm+=string+",";
					}
				}
				gpdm1 = gpdm.substring(0, gpdm.length()-1);
			}
			HashMap mapData=new HashMap();
			StringBuffer sql = new StringBuffer(
					"select count(*) from (select  b.*,rownum r from (select a.id,khbh,khmc,khfzr,zzcxdd,fhspr,zzspr,ggfbr,c.instanceid,CWSCBFZR2,CWSCBFZR3,f.zqdm from BD_MDM_KHQXGLB a,");
			sql.append("SYS_ENGINE_FORM_BIND c,bd_zqb_kh_base f where 1=1 ");
			sql.append(" and formid = '114'        and metadataid = '130'    and a.id = c.dataid  and c.instanceid is not null  and a.khbh=f.customerno ");
			sql.append(" and  khbh in(select bp.customerno from BD_ZQB_PJ_BASE bp");
			if(zqdm!=null&&!"".equals(zqdm)){
				String[] gpdmArr = gpdm1.split(",");
				String zc="";
				for (String string : gpdmArr) {
					zc += "?,";
				}
				String zzc = zc.substring(0, zc.length() - 1);
				mapData.put("zqdm", gpdm1);
				sql.append(",(select customerno from bd_zqb_kh_base where zqdm in ("+zzc+")) bk ");
			}
			sql.append(" where owner=? ");
			mapData.put("userfullname", userfullname);
			if(zqdm!=null&&!"".equals(zqdm)){
			sql.append(" and bp.customerno=bk.customerno");
			}
			sql.append(") ");
			if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
				sql.append(" and khbh=? ");
				mapData.put("khbh", khbh);
			}
			if (khmc != null && !khmc.equals("")) {
				sql.append(" and khmc like ? ");
				mapData.put("khmc", khmc);
			}
			if (cxddzy != null && !cxddzy.equals("")) {
				sql.append(" and UPPER(TRIM(khfzr)) like ? ");
				mapData.put("cxddzy", cxddzy.trim().toUpperCase());
			}
			if (zzcxdd != null && !zzcxdd.equals("")) {
				sql.append(" and UPPER(TRIM(FHSPR)) like ?");
				mapData.put("zzcxdd", zzcxdd.trim().toUpperCase());
			}
			sql.append(") b)");
			// 查询数据库
			Connection conn = DBUtil.open();
			PreparedStatement stmt=null;
			ResultSet rs =null;
			try {
				int index=1;
				stmt =conn.prepareStatement(sql.toString());
				if(zqdm!=null&&!"".equals(zqdm)){
					String zqdmcs=mapData.get("zqdm").toString();
					String[] split = zqdmcs.split(",");
					for (int i = 0; i < split.length; i++) {
						stmt.setString(index++, split[i]);
					}
				}
				stmt.setString(index++, mapData.get("userfullname").toString());
				if (khbh != null && !khbh.equals("") && !"undefined".equals(khbh)) {
					stmt.setString(index++, mapData.get("khbh").toString());
				}
				if (khmc != null && !khmc.equals("")) {
					stmt.setString(index++, "%"+mapData.get("khmc").toString()+"%");
				}
				if (cxddzy != null && !cxddzy.equals("")) {
					stmt.setString(index++, "%"+mapData.get("cxddzy").toString()+"%");
				}
				if (zzcxdd != null && !zzcxdd.equals("")) {
					stmt.setString(index++, "%"+mapData.get("zzcxdd").toString()+"%");
				}
				rs = stmt.executeQuery();
				if (rs.next()) {
					totalNum = rs.getInt(1);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rs);
			}

		}
		return totalNum;
	}

	public String getform(String wlins) {
		Long instanceid = Long.parseLong(wlins);
		HashMap l = DemAPI.getInstance().getFromData(instanceid,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);// 主表数据
		StringBuffer html = new StringBuffer();
		html.append("<table class=\"ke-zeroborder\" style=\"margin-bottom:5px;\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"0\">");
		html.append("<tbody>");
		html.append("<tr>");
		html.append("<td id=\"help\" align=\"right\">");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td class=\"line\" align=\"right\">");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td align=\"left\">");
		html.append("<table class=\"ke-zeroborder\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"0\">");
		html.append("<tbody>");
		html.append("<tr id=\"itemTr_1357\">");
		html.append("<td id=\"title_KHMC\" class=\"td_title\" width=\"180\">");
		html.append("客户名称");
		html.append("</td>");
		html.append("<td id=\"data_KHMC\" class=\"td_data\">");
		html.append("<input type='text' class = '{maxlength:200,required:false}' readonly"
				+ " onfocus='this.blur()'='readonly onfocus='this.blur()'' style=\"background-color:#efefef;width:100px\"  name=\'KHMC\' id=\'KHMC\'  value='"+l.get("KHMC")+"' >"
				);
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr id=\"itemTr_1535\">");
		html.append("<td id=\"title_ZZCXDD\" class=\"td_title\" width=\"180\">");
		html.append("投行审核人员");
		html.append("</td>");
		html.append("<td id=\"data_ZZCXDD\" class=\"td_data\">");
		html.append("<div style=\" white-space:nowrap;vertical-align:bottom;\"><input type='text'   name='ZZCXDD'"
				+ " class = '{maxlength:32,required:false}'  id='ZZCXDD' value='"+l.get("ZZCXDD")+"'  style=\"width:100px;margin-right:5px;\""
				+ "  form-type=\"radioAddress\" readonly >&nbsp;");
		html.append("			</td>");
		html.append("			</tr>");
		html.append("<tr id=\"itemTr_1536\">");
		html.append("<td id=\"title_FHSPR\" class=\"td_title\" width=\"180\">");
		html.append("专职持续督导");
		html.append("	</td>");
		html.append("<td id=\"data_FHSPR\" class=\"td_data\">");
		html.append("<div style=\" white-space:nowrap;vertical-align:bottom;\"><input type='text'   name='FHSPR' "
				+ "class = '{maxlength:32,required:false}'  id='FHSPR' value='"+l.get("FHSPR")+"'  style=\"width:100px;margin-right:5px;\""
				+ "  form-type=\"radioAddress\" readonly>");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr id=\"itemTr_1602\">");
		html.append("<td id=\"title_ZZSPR\" class=\"td_title\" width=\"180\">");
		html.append("质控部负责人");
		html.append("</td>");
		html.append("<td id=\"data_ZZSPR\" class=\"td_data\">");
		html.append("	<div style=\" white-space:nowrap;vertical-align:bottom;\"><input type='text'   name='ZZSPR' "
				+ "class = '{maxlength:32,required:false}'  id='ZZSPR' value='"+l.get("ZZSPR")+"'  style=\"width:100px;margin-right:5px;\"  "
				+ "form-type=\"radioAddress\" readonly>");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr id=\"itemTr_1603\">");
		html.append("<td id=\"title_CWSCBFZR2\" class=\"td_title\" width=\"180\">");
		html.append("场外市场部负责人1");
		html.append("</td>");
		html.append("<td id=\"data_CWSCBFZR2\" class=\"td_data\">");
		html.append("	<div style=\" white-space:nowrap;vertical-align:bottom;\"><input type='text'   name='ZZSPR' "
				+ "class = '{maxlength:32,required:false}'  id='CWSCBFZR2' value='"+l.get("CWSCBFZR2")+"'  style=\"width:100px;margin-right:5px;\"  "
				+ "form-type=\"radioAddress\" readonly>");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr id=\"itemTr_1603\">");
		html.append("<td id=\"title_CWSCBFZR3\" class=\"td_title\" width=\"180\">");
		html.append("场外市场部负责人2");
		html.append("</td>");
		html.append("<td id=\"data_CWSCBFZR3\" class=\"td_data\">");
		html.append("	<div style=\" white-space:nowrap;vertical-align:bottom;\"><input type='text'   name='ZZSPR' "
				+ "class = '{maxlength:32,required:false}'  id='CWSCBFZR3' value='"+l.get("CWSCBFZR3")+"'  style=\"width:100px;margin-right:5px;\"  "
				+ "form-type=\"radioAddress\" readonly>");
		html.append("</td>");
		html.append("</tr>");
		html.append("		<tr id=\"itemTr_1812\">");
		html.append("			<td id=\"title_GGFBR\" class=\"td_title\" width=\"180\">");
		html.append("			公告发布人");
		html.append("		</td>");
		html.append("		<td id=\"data_GGFBR\" class=\"td_data\">");
		html.append("		<div style=\" white-space:nowrap;vertical-align:bottom;\">"
				+ "<input type='text'   name='GGFBR' class = '{maxlength:32,required:false}'  id='GGFBR' value='"+l.get("GGFBR")+"'  "
				+ "style=\"width:100px;margin-right:5px;\"  form-type=\"radioAddress\" readonly>");
		html.append("		</td>");
		html.append("	</tr>");
		html.append("	<tr id=\"itemTr_1802\">");
		html.append("	<td id=\"title_BZ\" class=\"td_title\" width=\"180\">");
		html.append("		备注");
		html.append("	</td>");
		html.append("	<td id=\"data_BZ\" class=\"td_data\">");
		html.append("		<input type='text' class = '{maxlength:512,required:false}' "
				+ " style=\"width:100px;\" name='BZ' id='BZ'  value='"+l.get("BZ")+"'   form-type='al_textbox' readonly>&nbsp;");
		html.append("		</td>");
		html.append("	</tr>");
		html.append("	<tr id=\"itemTr_1805\">");
		html.append("		<td id=\"title_TXDF\" class=\"td_title\" width=\"180\">");
		html.append("			提醒对方");
		html.append("			</td>");
		html.append("			<td id=\"data_TXDF\" class=\"td_data\">");
		String istx=l.get("TXDF")==null?"":l.get("TXDF").toString();
		html.append("				<input type=radio  disabled  id='TXDF0' name='TXDF' value='是' "+(istx.equals("是")?"checked=\"checked\"":"")+">"
				+ "<label  id=\"lbl_TXDF0\"  for=\"TXDF0\">是</label>&nbsp;");
		html.append("<input type=radio disabled id='TXDF1' name='TXDF' value='否' "
				+(istx.equals("否")?"checked=\"checked\"":"")+" ><label  id=\"lbl_TXDF1\" for=\"TXDF1\">否</label>&nbsp;");
		html.append("&nbsp;");
		html.append("			</td>");
		html.append("		</tr>");
		html.append("				</tbody>");
		html.append("	</table>");
		html.append("		</td>");
		html.append("	</tr>");
		html.append("</tbody>");
		html.append("</table>");
		return html.toString();
	}

	public String checkCXDD(Long instanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		HashMap hashmap = new HashMap();
		hashmap.put("KHBH", fromData.get("CUSTOMERNO").toString());
		hashmap.put("KHMC", fromData.get("CUSTOMERNAME").toString());
		List<HashMap> list = DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", hashmap, null);
		if(list.size()==1){
			String LCID=list.get(0).get("INSTANCEID").toString();
			return LCID;
		}
		return "";
	}

}
