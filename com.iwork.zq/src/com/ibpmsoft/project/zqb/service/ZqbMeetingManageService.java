package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;

import com.ibpmsoft.project.zqb.common.MeetingConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.dao.ZqbMeetingManageDAO;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.NumberUtils;
import com.iwork.commons.util.UtilDate;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.plugs.appointment.model.AppointmentNum;
import com.iwork.plugs.appointment.model.AppointmentYYSX;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;

public class ZqbMeetingManageService {
	
	private ZqbMeetingManageDAO zqbMeetingManageDAO;
	private static final String ZQB_MEET_PLAN_UUID = "ab33c7b0b1b04bb3adbd16eb77a43409";
	private static final String ZQB_MEET_RETURN_LIST_UUID = "10a08bfada6a4b808d6bf9fa3134bc2b";
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private static Logger logger=Logger.getLogger(ZqbMeetingManageService.class);
	
	private FileUploadService uploadifyService;
	
	//查询通知公告发送人信息
    public List getZqbTzggProjectCostormerSet(String name,String sszjj,String szbm,String gpdq,String gprqks,String gprqjs,String ssbm,String gpzt, String zczt){
    	List params = new ArrayList();
    	StringBuffer sql = new StringBuffer("select cx.* from (select b.userid,b.username,a.customerno,a.customername,a.extend4 sszjj,a.ZWMC ss,a.GPSJ,b.mobile,b.email,a.zcqx,a.ygp,a.cxddbg from bd_zqb_kh_base a,orguser b where a.customerno=b.extend1 and b.orgroleid='3' and b.userstate=0  and sysdate<b.enddate  UNION ALL select b.userid,b.username,to_char(b.departmentid),b.departmentname,null,null,null,b.mobile,b.email,null ,null,null from orguser b where b.orgroleid<>'3' and b.userstate=0  and sysdate<b.enddate ) cx where 1=1");
		if(name!=null&&!name.equals("")){
			sql.append(" AND cx.username LIKE ?");
			params.add("%"+name.trim().toUpperCase()+"%");
		}
		if(sszjj!=null&&!sszjj.equals("")){
			sql.append(" AND cx.sszjj LIKE ?");
			params.add("%"+sszjj.trim()+"%");
		}
		if(szbm!=null&&!szbm.equals("")){
			sql.append(" AND cx.customername LIKE ?");
			params.add("%"+szbm.trim()+"%");
		}
		if(gpdq!=null&&!gpdq.equals("")){
			sql.append(" AND cx.ss LIKE ?");
			params.add("%"+gpdq.trim()+"%");
		}
		if(gprqks!=null&&!gprqks.equals("")){
			sql.append(" AND ").append("cx.gpsj >= TO_DATE(?, 'yyyy-mm-dd')");
			params.add(gprqks);
		}
		if(gprqjs!=null&&!gprqjs.equals("")){
			sql.append(" AND ").append("cx.gpsj <= TO_DATE(?, 'yyyy-mm-dd')");
			params.add(gprqjs);
		}
		if(ssbm!=null&&!ssbm.equals("")){
			sql.append(" AND cx.zcqx LIKE ?");
			params.add("%"+ssbm.trim()+"%");
		}
		if(gpzt!=null&&!gpzt.equals("")){
			sql.append(" AND cx.ygp LIKE ?");
			params.add("%"+gpzt.trim()+"%");
		}
		if(zczt!=null&&!zczt.equals("")){
			sql.append(" AND cx.cxddbg LIKE ?");
			params.add("%"+zczt.trim()+"%");
		}
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap map;
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i+1, params.get(i));
			}
			rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				String userid= rs.getString("userid");			//账号
				String username = rs.getString("username");		//名称
				String szbm1 = rs.getString("customername");	//部门/公司名称
				String sszjj1 = rs.getString("sszjj");			//所属证监局
				String ss = rs.getString("ss");					//省市
				String gpsj = rs.getString("GPSJ");				//挂牌时间
				String moblie = rs.getString("mobile");			//电话
				String email = rs.getString("email");			//邮箱
				String ssbm1 = rs.getString("zcqx");			//所属部门
				String ygp = rs.getString("ygp");			//邮箱
				String cxddbg = rs.getString("cxddbg");			//所属部门
				map.put("USERID", userid==null ? "":userid);
				map.put("USERNAME", username==null ? "":username);
				map.put("SZBM", szbm1==null ? "":szbm1);
				map.put("SSZJJ", sszjj1==null ? "":sszjj1);
				map.put("GPDQ", ss==null ? "":ss);
				map.put("GPSJ", gpsj==null ? "":gpsj.substring(0, 10));
				map.put("MOBILE", moblie==null ? "":moblie);
				map.put("EMAIL", email==null ? "":email);
				map.put("SSBM", ssbm1==null ? "":ssbm1);
				map.put("ygp", ygp==null ? "":ygp);
				map.put("cxddbg", cxddbg==null ? "":cxddbg);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return list;
	}
	public String getChartBarData(){
		//判断当前用户权限

		List<String> list =  this.getChartBarLabelList();
		
		List<Integer> list1 = new ArrayList(); 
		List<Integer> list2 = new ArrayList(); 
		
		for(String userid :list){
			List<String> companylist = new ArrayList(); 
			//获取持续督导人员共计负责了多少家公司
			StringBuffer sql = new StringBuffer();
			sql.append("select count(1) cnum,khbh from (select distinct khbh from BD_MDM_KHQXGLB where khfzr=?) group by khbh");
			Connection conn =null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				 conn = DBUtil.open();
				 stmt =conn.prepareStatement(sql.toString());
				 stmt.setString(1, userid);
				 rs = stmt.executeQuery();
				 int count = 0;
				 while(rs.next()){
					 count = rs.getInt("cnum");
					 String khbh=rs.getString("khbh");
					 companylist.add(khbh);
				 }
				 list1.add(count);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}finally{
				if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}
				if(stmt!=null){
					try {
						stmt.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}
				if(conn!=null){
					try {
						conn.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}
			}
			/*List parameter=new ArrayList();//存放参数
			StringBuffer sql1 = new StringBuffer();
			sql1.append("select count(*) num from BD_XP_BASEINFO t1 where ");
			if(companylist.size()>0){
				sql1.append(" (1=2 ");
				for(String no:companylist){
					sql1.append(" or ").append("t1.khbh=? ");
					parameter.add(no);
				}
				sql1.append(")");
			}
			//sql1.append(" and status = '已召开'");
			try {
				stmt =conn.prepareStatement(sql1.toString());
				for (int i = 0; i < parameter.size(); i++) {
					stmt.setString(i+1, parameter.get(i).toString());
				}
				rs = stmt.executeQuery();
				int num =0;
				while(rs.next()){
					num = rs.getInt("num");
					list2.add(num);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}finally{
				DBUtil.close(conn, stmt, rs);
			}*/
			list2.add(0);
		}
			
		JSONArray json1 = JSONArray.fromObject(list1);
		JSONArray json2 = JSONArray.fromObject(list2);
		String label = "["+json1.toString()+","+json2.toString()+"]";
		return label;
	}
	/**
	 * 获取持续督导人员列表
	 * @return
	 */
	public List<String> getChartBarLabelList(){
		StringBuffer sql = new StringBuffer("select userid,username from orguser where orgroleid  = 4 order by userid");
		Connection conn=null;
		PreparedStatement stmt = null;
		 ResultSet rs = null;
		 List<String> list = new ArrayList();
		try {
			conn = DBUtil.open();
			 stmt =conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			 while(rs.next()){
				 String userid = rs.getString("userid");
				 String userName = rs.getString("username");
				 list.add(userid+"["+userName+"]");
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rs);
		}
		
		return list;
	}
	/**
	 * 获得当前客户列表
	 * @return
	 */
	public List<HashMap> getCurrentCustomerList(){
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		List<HashMap> list = new ArrayList<HashMap>();
//		//董秘人员
//		if(roleid.equals("5")){
//			list = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
//		}else{//其他人员
		   List<HashMap> l = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
			for(HashMap map:l){
				if((map.get("KHFZR")!=null&&!map.get("KHFZR").toString().equals("")&&map.get("KHFZR").toString().equals(userFullName))
						||(map.get("ZZCXDD")!=null&&!map.get("ZZCXDD").toString().equals("")&&map.get("ZZCXDD").toString().equals(userFullName))
						||(map.get("FHSPR")!=null&&!map.get("FHSPR").toString().equals("")&&map.get("FHSPR").toString().equals(userFullName))
						||(map.get("ZZSPR")!=null&&!map.get("ZZSPR").toString().equals("")&&map.get("ZZSPR").toString().equals(userFullName))){
					list.add(map);
				}
			}
			
//		}
	
		return list;
	}
	/**
	 * 获得当前客户列表
	 * @return
	 */
	public String getCustomerManagerUser(String customerno){
		HashMap conditionMap = new HashMap(); 
		String userid = null;
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		if(list!=null&&list.size()>0){
			HashMap hash = list.get(0);
			if(hash!=null&&hash.get("KHFZR")!=null){
				userid = hash.get("KHFZR").toString();
			}
		}
		return userid;
	}
	
	/**
	 * 
	 * @param year
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public String getMeetFilterBar(int year,int month, String meettype,String grouptype,String status){
		StringBuffer filterbar = new StringBuffer();
		filterbar.append("<table width=\"100%\"><tr>");
		filterbar.append("<td><select id=\"\" onChange=\"dofilterYear(this.value)\">");
		for(int i=(year-5);i<year+5;i++){
			if(i==year){
				filterbar.append("<option value=\"").append(i).append("\" selected=\"selected\">").append(i).append("年</option>");
			}else{
				filterbar.append("<option value=\"").append(i).append("\" >").append(i).append("年</option>");
			}
		}
		filterbar.append("</select></td>");
//		filterbar.append("<td><select id=\"\" onChange=\"dofilterMonth(this.value)\">");
//		for(int i=1;i<=12;i++){
//			if(i==month){
//				filterbar.append("<option value=\"").append(i).append("\" selected=\"selected\">").append(i).append("月</option>");
//			}else{
//				filterbar.append("<option value=\"").append(i).append("\" >").append(i).append("月</option>");
//			}
//		}
//		filterbar.append("</select></td>");
		
		
		
		
		filterbar.append("	<td>");
		int num1=0;
		if(meettype==null){meettype="全部";}
		for(String type:MeetingConstants.MEETING_TYPE){
			num1++;
			if(meettype.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterMeet('").append(type).append("')\" >").append(type).append("</a>");
			}
			
			if(num1<MeetingConstants.MEETING_TYPE.length){
				filterbar.append("|");
			}
			
		}
		filterbar.append("	</td>");
		filterbar.append("	<td>");
		int num=0;
		if(grouptype==null){grouptype="全部";}
		for(String type:MeetingConstants.GROUP_TYPE){
			num++;
			if(grouptype.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterGroup('").append(type).append("')\" >").append(type).append("</a>");

			}
			
			if(num<MeetingConstants.GROUP_TYPE.length){
				filterbar.append("|");
			}
			
		}
		filterbar.append("	</td>");
		
		filterbar.append("	<td>");
		int num2=0;
		if(status==null){status="全部";}
		for(String type:MeetingConstants.STATUS){
			num2++;
			if(status.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterStatus('").append(type).append("')\" >").append(type).append("</a>");
			}
			 
			if(num2<MeetingConstants.STATUS.length){
				filterbar.append("|");
			}
		}
		filterbar.append("	</td>");
		filterbar.append("		</tr>");
		filterbar.append("</table>");
		return filterbar.toString();
	}
	/**
	 * 
	 * @param year
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public String getRecentlyFilterBar(String meettype,String grouptype,String status){
		StringBuffer filterbar = new StringBuffer();
		filterbar.append("<table width=\"100%\"><tr>");
//		filterbar.append("<td><select id=\"\" onChange=\"dofilterYear(this.value)\">");
//		for(int i=(year-5);i<year+5;i++){
//			if(i==year){
//				filterbar.append("<option value=\"").append(i).append("\" selected=\"selected\">").append(i).append("年</option>");
//			}else{
//				filterbar.append("<option value=\"").append(i).append("\" >").append(i).append("年</option>");
//			}
//		}
//		filterbar.append("</select></td>");
		
		filterbar.append("	<td>");
		int num1=0;
		if(meettype==null){meettype="全部";}
		for(String type:MeetingConstants.MEETING_TYPE){
			num1++;
			if(meettype.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterMeet('").append(type).append("')\" >").append(type).append("</a>");
			}
			
			if(num1<MeetingConstants.MEETING_TYPE.length){
				filterbar.append("|");
			}
			
		}
		filterbar.append("	</td>");
		filterbar.append("	<td>");
		int num=0;
		if(grouptype==null){grouptype="全部";}
		for(String type:MeetingConstants.GROUP_TYPE){
			num++;
			if(grouptype.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterGroup('").append(type).append("')\" >").append(type).append("</a>");
				
			}
			
			if(num<MeetingConstants.GROUP_TYPE.length){
				filterbar.append("|");
			}
			
		}
		filterbar.append("	</td>");
		
		filterbar.append("	<td>");
		int num2=0;
		if(status==null){status="全部";}
		for(String type:MeetingConstants.STATUS){
			num2++;
			if(status.equals(type)){
				filterbar.append(type);
			}else{
				filterbar.append("<a href=\"javascript:dofilterStatus('").append(type).append("')\" >").append(type).append("</a>");
			}
			
			if(num2<MeetingConstants.STATUS.length){
				filterbar.append("|");
			}
		}
		filterbar.append("	</td>");
		filterbar.append("		</tr>");
		filterbar.append("</table>");
		return filterbar.toString();
	}
	
	
	/**
	 * 
	 * @param year
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public List<HashMap> getRecentlyList(List<String> customerlist,String meettype,String grouptype,String status){
		StringBuffer sql = new StringBuffer();
		List<HashMap> datalist = new ArrayList();
		//获取前后一个月的日期
		Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        String beginDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.MONTH, 1);
        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
		sql.append("SELECT BOTABLE.YEAR,BOTABLE.JC,BOTABLE.MEETTYPE,BOTABLE.ZYWYH,BOTABLE.HYSX,BOTABLE.HC,BOTABLE.MEETNAME,BOTABLE.PLANTIME,BOTABLE.STATUS,BOTABLE.JHCJR,BOTABLE.JHCJSJ,BOTABLE.CUSTOMERNO,BOTABLE.CUSTOMERNAME,BOTABLE.FJ,BOTABLE.ID,BINDTABLE.INSTANCEID FROM BD_MEET_PLAN  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=96 and BINDTABLE.metadataid=109 and  to_char(PLANTIME,'yyyy-MM-dd')<? and to_char(PLANTIME,'yyyy-MM-dd')>?");
		if(customerlist!=null&&customerlist.size()>0){
			sql.append(" AND (");
			for(String customerno:customerlist){
				sql.append("  CUSTOMERNO = ? OR ");
			}
			sql.append("1=2)");
		
		}else{
			sql.append(" AND CUSTOMERNO = ''");
		}
		sql.append(" ORDER BY BOTABLE.ID DESC ");
		Connection conn =null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			int i=1;
			stmt.setString(i, endDate);i++;
			stmt.setString(i, beginDate);i++;
			if(customerlist!=null&&customerlist.size()>0){
				for(String customerno:customerlist){
					stmt.setString(i, customerno);i++;
				}
			
			}
			rset = stmt.executeQuery();
			while(rset.next()){
				HashMap map = new HashMap();
				String meetType = rset.getString("MEETTYPE");
				if(meetType!=null)map.put("MEETTYPE",meetType);
				
				String hysx = rset.getString("HYSX");
				if(hysx!=null)map.put("HYSX",meetType);
				
				String meetName = rset.getString("MEETNAME");
				if(meetName!=null)map.put("MEETNAME",meetName);
				
				String khbh = rset.getString("CUSTOMERNO");
				if(khbh!=null)map.put("CUSTOMERNO",khbh);
				
				Date plantime = rset.getTimestamp("PLANTIME");
				if(plantime!=null)map.put("PLANTIME",plantime);
				
				String khmc = rset.getString("CUSTOMERNAME");
				if(khmc!=null)map.put("CUSTOMERNAME",khmc);
				
				Long instanceid = rset.getLong("INSTANCEID");
				if(instanceid!=null)map.put("INSTANCEID",instanceid);
				
				String jhcjsj = rset.getString("JHCJSJ");
				if(jhcjsj!=null)map.put("JHCJSJ",jhcjsj);
				
				String zt = rset.getString("STATUS");
				if(zt!=null)map.put("STATUS",zt);
				
				datalist.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rset);
		}
		return datalist;
	}
	/**
	 * 
	 * @param year
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public List<HashMap> getMeetRunList(String customerno,int year,int month ,String meettype,String grouptype,String status){	
		HashMap conditionMap = new HashMap(); 
		
		if(customerno!=null){
			conditionMap.put("CUSTOMERNO", customerno);
		}else{
			conditionMap.put("CUSTOMERNO", "null");
		}
		if(year!=0){
			conditionMap.put("YEAR", year);
		}
	    if(month!=0){
	    	conditionMap.put("MONTH", month);
	    }
		if(meettype!=null&&!meettype.equals("全部")){
			conditionMap.put("MEETTYPE", meettype);
		}
		if(grouptype!=null&&!grouptype.equals("全部")){
			conditionMap.put("HYSX", grouptype);
		}
		if(status!=null&&!status.equals("全部")){
			conditionMap.put("STATUS", status);
		}
		
		List<HashMap> list = DemAPI.getInstance().getList(ZQB_MEET_PLAN_UUID, conditionMap, "id");
		Collections.reverse(list);
		return list;
	}
	
	public List<HashMap> getMeetRunList1(String customerno,int year,int month ,String meettype,String grouptype,String status,int pageSize, int pageNow){
			List<HashMap> list = zqbMeetingManageDAO.getMeetRunList1(customerno, year, month, meettype, grouptype, status, pageSize, pageNow);	
		return list;
	}
	
	/**
	 * 获得会议次数
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public int getMeetingCount(String customerno,int jc,String meettype,String grouptype,int hc,String year,String meetpro){
		int count = zqbMeetingManageDAO.getMeetingCount(customerno,jc, meettype, grouptype,hc,year,meetpro);
		return count;
	}
	/**
	 * 获得会议次数
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public String getMeetingCount(String customerno,int jc,String meettype,String grouptype,int hc,String year){
		int count = zqbMeetingManageDAO.getMeetingCount(customerno,jc, meettype, grouptype,hc,year);
		String cnNum = NumberUtils.numberArab2CN(count);
		HashMap hash = new HashMap();
		hash.put("num",count);
		hash.put("cn",cnNum);
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(hash);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 判断同一会次次数记录是否存在
	 * @param companyno
	 * @param year
	 * @param meetjc
	 * @param meettype
	 * @param meetpro
	 * @param meetsx
	 * @return
	 */
	public boolean validateMeetNumber(String customerno,String year,int jc,String meettype,String meetpro,String grouptype,int hc){
		boolean flag = false;
		int num = 0;
		StringBuffer sql = new StringBuffer();
		HashMap map=new HashMap();
		sql.append("select count(*) from bd_meet_plan where 1=1 ");
		if(customerno!=null && !"".equals(customerno)){
			sql.append(" and customerno = ?");
			map.put("customerno", customerno);
		}
		if(year!=null && !"".equals(year)){
			sql.append(" and YEAR = ?");
			map.put("year", year);
		}
		if(meettype!=null && !"".equals(meettype)){
			sql.append(" and MEETTYPE = ?");
			map.put("meettype", meettype);
		}
		if(meetpro!=null && !"".equals(meetpro)){
			sql.append(" and zywyh = ?");
			map.put("meetpro", meetpro);
		}
		if("股东大会".equals(meettype)){
			if(grouptype!=null && !"".equals(grouptype)){
				sql.append(" and hysx = ?");
				map.put("grouptype", grouptype);
				//股东大会临时会议
				if("临时会议".equals(grouptype)){
					if(jc!=0){
						sql.append(" and jc = ?");
						map.put("jc", jc);
					}
				}
			}
		}else{
			if(grouptype!=null && !"".equals(grouptype)){
				sql.append(" and hysx = ?");
				map.put("grouptype", grouptype);
			}	
			if(hc!=0){
				sql.append(" and hc = ?");
				map.put("hc", hc);
			}
			if(jc!=0){
				sql.append(" and jc = ?");
				map.put("jc", jc);
			}
		}
		
		PreparedStatement stmt = null;
		Connection conn=null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			int index=1;
			stmt =conn.prepareStatement(sql.toString());
			if(customerno!=null && !"".equals(customerno)){
				stmt.setString(index++, map.get("customerno").toString());
			}
			if(year!=null && !"".equals(year)){
				stmt.setString(index++, map.get("year").toString());
			}
			if(meettype!=null && !"".equals(meettype)){
				stmt.setString(index++, map.get("meettype").toString());
			}
			if(meetpro!=null && !"".equals(meetpro)){
				stmt.setString(index++, map.get("meetpro").toString());
			}
			if("股东大会".equals(meettype)){
				if(grouptype!=null && !"".equals(grouptype)){
					stmt.setString(index++, map.get("grouptype").toString());
					//股东大会临时会议
					if("临时会议".equals(grouptype)){
						if(jc!=0){
							stmt.setString(index++, map.get("jc").toString());
						}
					}
				}
			}else{
				if(grouptype!=null && !"".equals(grouptype)){
					stmt.setString(index++, map.get("grouptype").toString());
				}	
				if(hc!=0){
					stmt.setString(index++, map.get("hc").toString());
				}
				if(jc!=0){
					stmt.setString(index++, map.get("jc").toString());
				}
			}
			rset = stmt.executeQuery(sql.toString());
			while(rset.next()){
				String str = rset.getString(1);
				if(str!=null && !"".equals(str)){
					num = Integer.parseInt(str);
				}
			} 
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rset);
		}
		
		if(num>=1){
			flag = true;
		}
		return flag;
	}
	/**
	 * 根据条件获取当前条件下最大的会议届次
	 * @param companyno
	 * @param year
	 * @param meettype
	 * @param meetpro
	 * @param meetsx
	 * @return
	 */
	public int getMeetSession(String customerno,String year,String meettype,String meetpro,String meetsx){
		int num = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select max(jc) num from bd_meet_plan where 1=1  ");
		if(customerno!=null && !"".equals(customerno)){
			sql.append(" and customerno = ?");
		}
		if(year!=null && !"".equals(year)){
			sql.append(" and YEAR = ?");
		}
		if(meettype!=null && !"".equals(meettype)){
			sql.append(" and MEETTYPE = ?");
		}
		if(meetpro!=null && !"".equals(meetpro)){
			sql.append(" and zywyh = ?");
		}
		if(meetsx!=null && !"".equals(meetsx)){
			sql.append(" and hysx = ?");
		}
		PreparedStatement stmt = null;
		Connection conn=null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			int index=1;
			if(customerno!=null && !"".equals(customerno)){
				stmt.setString(index++, customerno);
			}
			if(year!=null && !"".equals(year)){
				stmt.setString(index++, year);
			}
			if(meettype!=null && !"".equals(meettype)){
				stmt.setString(index++, meettype);
			}
			if(meetpro!=null && !"".equals(meetpro)){
				stmt.setString(index++, meetpro);
			}
			if(meetsx!=null && !"".equals(meetsx)){
				stmt.setString(index++, meetsx);
			}
			rset = stmt.executeQuery();
			while(rset.next()){
				String str = rset.getString(1);
				if(str!=null && !"".equals(str)){
					num = Integer.parseInt(str);
				}
			} 
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rset);
		}
		
		return num;
	}
	
	/**
	 * 获得届次次数 
	 * @param customerno
	 * @param meettype
	 * @param grouptype
	 * @return
	 */
	public String getMeetingJC(String customerno,String meettype,String hysx,int jc){ 
		HashMap hash = new HashMap();
		if(hysx==null&&jc==0){
			int count = zqbMeetingManageDAO.getMeetingJC(customerno, meettype);
			if(count==0){
				count=1;
			} 
			String cnNum = NumberUtils.numberArab2CN(count);
			hash.put("num",count);
			hash.put("cn",cnNum);
		}else{
			int count = zqbMeetingManageDAO.getMeetingCount(customerno,jc, meettype, hysx); 
			String cnNum = NumberUtils.numberArab2CN(count+1);
			hash.put("num",count+1); 
			hash.put("cn",cnNum);
		}
		
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(hash);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public boolean setStatus(String opreatorType,Long instanceid,Date date){
		boolean flag = false;
		HashMap hash = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(hash==null)return false;
		//获取会议干系人
		List<String> noticelist = new ArrayList();
		//获取计划创建人
		if(hash!=null&&hash.get("JHCJR")!=null){
			String user = hash.get("JHCJR").toString();
			OrgUser orguser = UserContextUtil.getInstance().getOrgUserInfo(user);
			if(orguser!=null){
				noticelist.add(orguser.getUserid());
			}
		}
		if(hash!=null&&hash.get("CUSTOMERNO")!=null){
			String customerManagerUserid = this.getCustomerManagerUser(hash.get("CUSTOMERNO").toString());
			if(customerManagerUserid!=null){
				noticelist.add(customerManagerUserid);
			}
		}
		String title = "";
		StringBuffer content = new StringBuffer();
		String url = "pageindex1.action?navurl=zqb_meeting_recently.action&tabkey=other&menukey=147";
		//获取持续督导人员
		if(opreatorType==null)
			opreatorType = "retime";
		if(opreatorType.equals("finish")){
			title = "【"+hash.get("CUSTOMERNAME").toString()+"】"+hash.get("MEETNAME").toString()+"【已召开】";
			content.append("所属公司:").append(hash.get("CUSTOMERNAME").toString()).append("<br>\n");
			content.append("会议名称:").append(hash.get("MEETNAME").toString()).append("<br\n");
			content.append("会议召开时间:").append(UtilDate.datetimeFormat(date)).append("<br\n");
			hash.put("STATUS", "已召开"); 
			hash.put("PLANTIME", UtilDate.datetimeFormat(date));
			Long dataid = Long.parseLong(hash.get("ID").toString());
			flag = DemAPI.getInstance().updateFormData(ZQB_MEET_PLAN_UUID, instanceid, hash, dataid, true);
			//发送提醒
		}else{
			if(hash.get("STATUS")!=null&&!hash.get("STATUS").toString().equals("已召开")){
			
				hash.put("PLANTIME", UtilDate.datetimeFormat(date));
				Long dataid = Long.parseLong(hash.get("ID").toString());
				flag = DemAPI.getInstance().updateFormData(ZQB_MEET_PLAN_UUID, instanceid, hash, dataid, true);
				title = "【"+hash.get("CUSTOMERNAME").toString()+"】"+hash.get("MEETNAME").toString()+"延期至"+UtilDate.datetimeFormat(date)+"【延期召开】";
				content.append("所属公司:").append(hash.get("CUSTOMERNAME").toString()).append("<br>\n");
				content.append("会议名称:").append(hash.get("MEETNAME").toString()).append("<br>\n");
				content.append("延期至:").append(UtilDate.datetimeFormat(date)).append("<br>\n");
			}
		}
		
		//发送提醒消息
		for(String usr:noticelist){
			String userid = UserContextUtil.getInstance().getUserId(usr);
			if(!usr.equals(UserContextUtil.getInstance().getCurrentUserId())&&userid!=null){
				UserContext target = UserContextUtil.getInstance().getUserContext(userid);
				String mobile = target.get_userModel().getMobile();
				if(mobile!=null&&!mobile.equals("")){
					MessageAPI.getInstance().sendSMS(UserContextUtil.getInstance().getUserContext(userid), mobile, title);
				}
				String email = target.get_userModel().getEmail();
				if (email != null && !email.equals("")) {
					 MessageAPI.getInstance().sendSysMail("", email, title, content.toString());
				}
				MessageAPI.getInstance().sendSysMsg(userid, title, content.toString(), url);
			};
			
		}
		return flag;
	}
	/**
	 * 检查指定的客户编号和会议实例ID，是否已设置了会议清单
	 * @param customerno
	 * @param instanceid
	 * @return
	 */
	public HashMap checkReturnListModel(String customerno,Long instanceid){
		HashMap conditionMap = new HashMap(); 
		
		if(customerno!=null){
			conditionMap.put("KHBH", customerno);
		}else{
			conditionMap.put("KHBH", "null");
		}
		
		if(instanceid!=null){
			conditionMap.put("HYBH",instanceid);
		}
		HashMap data = new HashMap();
		List<HashMap> list = DemAPI.getInstance().getList(ZQB_MEET_RETURN_LIST_UUID, conditionMap,null);
		if(list!=null&&list.size()>0){
			data = list.get(0);
		}
		
		return data;
	}
	
	
	
	/**
	 * 获取检查返回列表
	 * @param customerno
	 * @param instanceid
	 * @return
	 */
	public String getCheckReturnList(String customerno,Long instanceid){
		StringBuffer html = new StringBuffer();
		String baseInfoUUID = "ad87aa61-ef51-4aa1-b5c4-fbbf934b6bcd";
		HashMap conditionMap = new HashMap();
		conditionMap.put("TYPE", "提交会议资料清单");
		List<HashMap> list = DemAPI.getInstance().getList(baseInfoUUID, conditionMap, "ID");
		HashMap hash = this.checkReturnListModel(customerno, instanceid);
		String[] zl = null;
		String other = "";
		String fj = "";
		if(hash!=null){
			Object zlqd = hash.get("FKZLQD");
			if(zlqd!=null){
				zl=zlqd.toString().split(",");
			}
			
			if(hash.get("QTZLQD")!=null){
				other = hash.get("QTZLQD").toString();
			}
			if(hash.get("FKZL")!=null){
				fj = hash.get("FKZL").toString();
			}
		}
		
		for(HashMap hm:list){
			boolean flag =false;
			String name = hm.get("NAME").toString();
			if(zl!=null&&zl.length>0){
				for(String item:zl){
					if(item.equals(name)){
						flag = true;
						break;
					}
				}
			}
			if(flag){
				html.append("<span><img src=\"iwork_img/check-ok.gif\">").append(name).append("</span>");
			}
		}
		if(!other.equals("")){
			html.append("<div>").append("其他:").append(other).append("</div>");
		}
		
		if(fj!=null&&!fj.equals("")){
			String[] fjlist = fj.split(",");
			for(String str:fjlist){
				if(str.trim().equals("")){
					continue;
				}
				FileUpload upload = FileUploadAPI.getInstance().getFileUpload(str);
				if(upload!=null){
					html.append("<div class=\"titlefilelist\"><a href=\"uploadifyDownload.action?fileUUID=").append(upload.getFileId()).append("\">").append(upload.getFileSrcName()).append("</a></div>");
				}
			}
			
		}
		return html.toString();
	}
	
	
	public String getFileListHtml(String customerno,Long instanceid,boolean isOwner){
		StringBuffer html = new StringBuffer();
		
		HashMap hash = this.getMeetingMap(instanceid);
		if(hash!=null&&hash.get("FJ")!=null){
			String fj = hash.get("FJ").toString();
			if(!fj.equals("")){ 
				List<FileUpload> list =  uploadifyService.getFileUploads(ZqbMeetingManageService.class, fj);
				html.append("<table class=\"filelist\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">");
				int count = 0;
				for(FileUpload upload:list){
					count++;
					html.append("<tr>");
					html.append("<td>").append(count).append("</td>");
					String icon = "";
					String suffix = FileUtil.getFileExt(upload.getFileSaveName());
					icon = WebUIUtil.getLinkIcon(suffix);
					html.append("<td><a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID=").append(upload.getFileId()).append("\"><span>").append(icon).append("</span>").append(upload.getFileSrcName()).append("</a></td>");
					html.append("<td>").append(upload.getUploadTime()).append("</td>");
					if(upload.getMemo()==null)upload.setMemo("");
					html.append("<td>").append(upload.getMemo()).append("</td>");
					html.append("<td>");
					if(isOwner){
						html.append("<a href=\"javascript:removeFile('").append(upload.getFileId()).append("')\";><img src=\"iwork_img/close.gif\"></a>");
					}
					html.append("</td>");
					
					html.append("</tr>");
				}
				html.append("</table>");
			} 
		}
		return html.toString();
	}
	
	public void saveKmDoc(String uuid, Long directoryId, Long filesize, String filename, Long id,Long order_index, String time) {
		zqbMeetingManageDAO.saveKmDoc(uuid,directoryId,filesize,filename,id,order_index,time);
	}
	
	/**
	 * 获取是否为持续督导人员
	 * @return
	 */
	public boolean getChartBarLabelListFlag(String userid){
		int num=0;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) num from (select orgroleid from orguser where orgroleid  = 4 and userid=? union select  orgroleid from orgusermap where orgroleid  = 4 and userid=? ) ");
		PreparedStatement stmt = null;
		Connection conn =null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			stmt.setString(2, userid);
			rs = stmt.executeQuery();
			while(rs.next()){
				num = rs.getInt("num");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stmt, rs);
		}
		
		return num==0;
	}
	
	public HashMap getMeetingMap(Long instanceid){
		return DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
	}
	
	public boolean removeItem(String projectNo,Long taskid,Long instanceid){
		return  DemAPI.getInstance().removeFormData(instanceid);
	}
	public void setZqbMeetingManageDAO(ZqbMeetingManageDAO zqbMeetingManageDAO) {
		this.zqbMeetingManageDAO = zqbMeetingManageDAO;
	}

	public ZqbMeetingManageDAO getZqbMeetingManageDAO() {
		return zqbMeetingManageDAO;
	}

	public void setUploadifyService(FileUploadService uploadifyService) {
		this.uploadifyService = uploadifyService;
	}
	
	
	public String getFileList(String customerno,Long instanceid,boolean isOwner){
		StringBuffer html = new StringBuffer();
		
		HashMap hash = this.getMeetingMap(instanceid);
		if(hash!=null&&hash.get("YAZLFJ")!=null){
			String fj = hash.get("YAZLFJ").toString();
			if(!fj.equals("")){ 
				List<FileUpload> list =  uploadifyService.getFileUploads(ZqbMeetingManageService.class, fj);
				html.append("<table class=\"filelist\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">");
				int count = 0;
				for(FileUpload upload:list){
					count++;
					html.append("<tr>");
					//html.append("<td>").append(count).append("</td>");
					String icon = "";
					String suffix = FileUtil.getFileExt(upload.getFileSaveName());
					icon = WebUIUtil.getLinkIcon(suffix);
					html.append("<td><a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+upload.getFileId()+"\"><span>").append(icon).append("</span>").append(upload.getFileSrcName()).append("</a></td>");
					//html.append("<td>").append(upload.getUploadTime()).append("</td>");
					if(upload.getMemo()==null)upload.setMemo("");
					html.append("<td>").append(upload.getMemo()).append("</td>");				
					html.append("</tr>");
				}
				html.append("</table>");
			} 
		}
		return html.toString();
	}
	public String getQtFileList(String customerno,Long instanceid,boolean isOwner){
		StringBuffer html = new StringBuffer();
		
		HashMap hash = this.getMeetingMap(instanceid);
		if(hash!=null&&hash.get("FJ")!=null){
			String fj = hash.get("FJ").toString();
			if(!fj.equals("")){ 
				List<FileUpload> list =  uploadifyService.getFileUploads(ZqbMeetingManageService.class, fj);
				html.append("<table class=\"filelist\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">");
				int count = 0;
				for(FileUpload upload:list){
					count++;
					html.append("<tr>");
					//html.append("<td>").append(count).append("</td>");
					String icon = "";
					String suffix = FileUtil.getFileExt(upload.getFileSaveName());
					icon = WebUIUtil.getLinkIcon(suffix);
					html.append("<td><a target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+upload.getFileId()+"\"><span>").append(icon).append("</span>").append(upload.getFileSrcName()).append("</a></td>");
					//html.append("<td>").append(upload.getUploadTime()).append("</td>");
					if(upload.getMemo()==null)upload.setMemo("");
					html.append("<td>").append(upload.getMemo()).append("</td>");				
					html.append("</tr>");
				}
				html.append("</table>");
			} 
		}
		return html.toString();
	}
	public int getMeetRunListSize(String customerno,int year,int month ,String meettype,String grouptype,String status) {
		int meetRunListSize = zqbMeetingManageDAO.getMeetRunListSize(customerno,year,month,meettype,grouptype,status);
		return meetRunListSize;
	}
	
	public boolean removeItem( Long instanceid) {
		Long formId = DemAPI.getInstance().getFormIdByInstanceId(
				new Long(instanceid), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		HashMap hash = DemAPI.getInstance().getFromData(instanceid, formId,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String smsContent = "";
		String sysMsgContent = "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(hash!=null){
		    smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.MEET_REMOVE_KEY, hash); 
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.MEET_REMOVE_KEY, hash);
			String value=hash.get("MEETNAME")==null?"":hash.get("MEETNAME").toString();
			Long dataId=Long.parseLong(hash.get("ID").toString());
			LogUtil.getInstance().addLog(dataId, "三会计划", "删除会议："+value);
		}
		String customerno = "";
		if(hash.get("CUSTOMERNO")!=null){
			customerno = hash.get("CUSTOMERNO").toString();
		}
		String useraddress = ZQBNoticeUtil.getInstance().getDuDaoCustomer(customerno);
		String userid = UserContextUtil.getInstance().getUserId(useraddress);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		UserContext target = UserContextUtil.getInstance().getUserContext(userid); 
		if(target!=null){
			if(!smsContent.equals("")){
				String mobile = target.get_userModel().getMobile();
				if(mobile!=null&&!mobile.equals("")){
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
				}
				String email = target.get_userModel().getEmail();
				if(email!=null&&!email.equals("") && uc != null){
					MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, "会议信息取消提醒",smsContent,"");
				}
			}
			if(!sysMsgContent.equals("")){ 
					MessageAPI.getInstance().sendSysMsg(userid, "会议信息取消提醒", sysMsgContent);
			}
		}
		return DemAPI.getInstance().removeFormData(instanceid);
	}  

	/**
	 * 获得会议树json
	 * @return
	 * 
	 */
	public String getMeetingTreeJson(String companyno,String companytype){
		StringBuffer json = new StringBuffer();
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		List<HashMap> list = new ArrayList<HashMap>();
		   List<HashMap> l = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
			for(HashMap map:l){
				if((map.get("KHFZR")!=null&&!map.get("KHFZR").toString().equals("")&&map.get("KHFZR").toString().equals(userFullName))
						||(map.get("ZZCXDD")!=null&&!map.get("ZZCXDD").toString().equals("")&&map.get("ZZCXDD").toString().equals(userFullName))
						||(map.get("FHSPR")!=null&&!map.get("FHSPR").toString().equals("")&&map.get("FHSPR").toString().equals(userFullName))
						||(map.get("ZZSPR")!=null&&!map.get("ZZSPR").toString().equals("")&&map.get("ZZSPR").toString().equals(userFullName))){
					list.add(map);
				}
			}
	  if(list.size()<=0){
			OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			String	customerno = orgUser.getExtend1();
			HashMap map=new HashMap();
			map.put("KHBH",customerno);
			list.add(map);
		}
		JSONArray jsonArray = JSONArray.fromObject(list);
		json.append(jsonArray);
		return json.toString();
	}
	public Long getSInstanceID(String customerno) {
		Map params = new HashMap();
		params.put(1,customerno);
		Long id=DBUTilNew.getLong("INSTANCEID","SELECT BOTABLE.*,BINDTABLE.INSTANCEID FROM BD_MEET_SHSPSM  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=147 and BINDTABLE.metadataid=163 and CUSTOMERNO= ? ", params);
		return id;
	}
	public String updateClFlag(Long instanceid,Long id) {
		String msg="";
		HashMap hashdata=DemAPI.getInstance().getFormData("ab33c7b0b1b04bb3adbd16eb77a43409", id);
		hashdata.put("YCL", '1');//1为已处理
		boolean flag=DemAPI.getInstance().updateFormData("ab33c7b0b1b04bb3adbd16eb77a43409", instanceid, hashdata, id, false);
		if(flag){
			msg="更新成功！";
		}else{
			msg="更新失败！";
		}
		return msg;
	}
	public boolean getDataSee() {
		Long orgroleid = UserContextUtil.getInstance()
					.getCurrentUserContext()._userModel.getOrgroleid();
		if(orgroleid==3){
			return true;
		}
		return false;
	}
	
	
	//xlj 2015年6月12日11:01:48
	public List<HashMap> AppointInfo(String corpCode,String startDate,String endDate,int eventID,String cxdd,int pageSize, int pageNow){		
		List<HashMap> list = zqbMeetingManageDAO.getAppointList(corpCode, startDate, endDate, eventID, cxdd, pageSize, pageNow);	
		return list;
	}
	
	public List<HashMap> NewAppointInfo(String corpCode,String yyrq,int eventID){		
		List<HashMap> list = zqbMeetingManageDAO.getNewAppointList(corpCode, yyrq, eventID);	
		return list;
	}
	
	public int AppointInfoSize(String corpCode,String startDate,String endDate,int eventID,String cxdd) {
		int RunListSize = zqbMeetingManageDAO.getAppointListSize(corpCode, startDate, endDate, eventID, cxdd);
		return RunListSize;
	}
	
	public List<HashMap> sxmsConfig(String cxdd)
	{
		List<HashMap> list = zqbMeetingManageDAO.getSXMSList(cxdd);
		return list;
	}
	
	public List<HashMap> getcxddList()
	{
		List<HashMap> list = zqbMeetingManageDAO.getcxddList();
		return list;
	}
	
	public HashMap GETCorpCodeAndCXDD(String CorpID){
		HashMap hm = zqbMeetingManageDAO.GETCorpCodeAndCXDD(CorpID);
		return hm;
	}
	//xlj 2015年6月23日15:31:10 显示可以预约的列表
	//CorpCode 客户股票代码,DateStart 开始查询条件,SXID 预约事项ID,PageSize 一页多少条数据,显示第几页信息
	public List<HashMap> getCanAppoint(String CorpCode,String DateStart,int SXID,int PageSize,int PageIndex)
	{		
		List<HashMap> list = zqbMeetingManageDAO.getCanAppoint(CorpCode,DateStart , SXID, PageSize, PageIndex);
		return list;
	}
	
	/* xlj 2015年6月25日18:01:28 添加预约信息
	  CorpCode VARCHAR2,--客户股票代码
	  SXID IN NUMBER, --预约事项ID
	  SZR IN VARCHAR2, --设置人
	  CXDDID IN VARCHAR2, --持续督导人ID
	  YYDATE IN VARCHAR2 --预约日期*/
	public HashMap ADDYYXX(String CorpID,String CorpCode,int SXID,String Cxdd,String strYYDate)
	{		
		HashMap list = zqbMeetingManageDAO.ADDYYXX(CorpID,CorpCode,SXID, Cxdd, strYYDate);
		String sxms="";
		StringBuffer sql = new StringBuffer("SELECT SXMS FROM BD_XP_YYSX WHERE ID=? ");
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setInt(1, SXID);
			rset = stmt.executeQuery();
			while (rset.next()) {
				sxms = rset.getString("SXMS");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		String value=sxms;
		LogUtil.getInstance().addLog(0L, "预约事项添加", "添加预约事项："+value);
		return list;
	}
	
	//删除预约信息
	public String DeleteAppoint(String strID)
	{		
		List parameter=new ArrayList();//存放参数
		List<String> data=new ArrayList<String>();
		String[] split = strID.split(",");
		StringBuffer sql=new StringBuffer("SELECT SXMS FROM BD_XP_YYSX WHERE ID in (");
		for (int i = 0; i < split.length; i++) {
			if(split[i]!=null&&!"".equals(split[i].toString())){
				if (i == (split.length - 1)){
					sql.append("?");
			     }else if((i%999)==0 && i>0){
			    	 sql.append("?").append(") or ID in ("); 
			     }else{
			    	 sql.append("?").append(",");
			     }
				 parameter.add(split[i].toString());
			}
		}
		PreparedStatement stmt = null;
		Connection conn=null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int j = 0; j < parameter.size(); j++) {
				stmt.setInt(j+1, Integer.parseInt(parameter.get(j).toString()));
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String sxms = rset.getString("SXMS");
				data.add(sxms);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		zqbMeetingManageDAO.DeleteAppoint(strID);
		StringBuffer value=new StringBuffer();
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i)!=null&&"".equals(data.get(i).toString())){
				if (i == (data.size() - 1)){
					value.append(data.get(i).toString());
			     }else{
			    	 value.append(data.get(i).toString()).append(",");
			     }
			}
		}
		LogUtil.getInstance().addLog(0L, "预约事项", "删除预约事项："+value);
		return "";
	}
	
	//删除预约信息
	public String NewDeleteAppoint(String strID){		
		List parameter=new ArrayList();//存放参数
		List<String> data=new ArrayList<String>();
		String[] split = strID.split(",");
		StringBuffer sql=new StringBuffer("SELECT SXMS FROM BD_XP_YYSX WHERE ID = ?");
		PreparedStatement stmt = null;
		Connection conn=null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setInt(1, Integer.parseInt(strID));
			rset = stmt.executeQuery();
			while (rset.next()) {
				String sxms = rset.getString("SXMS");
				data.add(sxms);
			}
		}catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}

		zqbMeetingManageDAO.DeleteAppoint(strID);
		StringBuffer value=new StringBuffer();
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i)!=null&&"".equals(data.get(i).toString())){
				if (i == (data.size() - 1)){
					value.append(data.get(i).toString());
			     }else{
			    	 value.append(data.get(i).toString()).append(",");
			     }
			}
		}
		LogUtil.getInstance().addLog(0L, "预约事项", "删除预约事项："+value);
		return "";
	}
	
	//修改预约信息
	public String UPDATEAppoint(String strID,String strDate)
	{		
		return zqbMeetingManageDAO.UPDATEAppoint(strID,strDate);
	}
	
	public List<HashMap> getSxmcList() {
		List<HashMap> list = zqbMeetingManageDAO.getSxmcList();
		return list;
	}
	
	public void save(AppointmentNum model) {
		zqbMeetingManageDAO.addBoData(model);
	}
	
	public void update(AppointmentNum model) {
		zqbMeetingManageDAO.updateBoData(model);
	}
	
	public AppointmentNum getModel(String id) {
		long temp = Long.parseLong(id);
		AppointmentNum appointment = zqbMeetingManageDAO.getBoData(temp);
		return appointment;
	}
	
	public AppointmentNum getModel(long id) {
		AppointmentNum appointment = zqbMeetingManageDAO.getBoData(id);
		return appointment;
	}
	
	public AppointmentYYSX getModelAppointmentYYSX(long id) {
		AppointmentYYSX appointment = zqbMeetingManageDAO.getBoDataAppointmentYYSX(id);
		return appointment;
	}
	
	public AppointmentNum setAppointment(AppointmentNum appointmentNum,
			long accepPass) {
		UserContext uc = UserContextUtil.getInstance()
				.getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		if(accepPass!=0){
			int id = DBUtil.getInt("select ID from bd_xp_yys where szr='"+userid+"' and yysx="+accepPass+"", "ID");
			AppointmentNum boData = zqbMeetingManageDAO.getBoData((long)id);
			appointmentNum.setId(boData.getId());
			appointmentNum.setSzs(boData.getSzs());
			appointmentNum.setSzr(userid);
			appointmentNum.setYysx(accepPass);
		}else{
			appointmentNum.setSzr(userid);
			appointmentNum.setSzs(null);
		}
		return appointmentNum;
	}
	public List<HashMap> selectNum(int pageNumber, int pageSize) {
		List<HashMap> list=new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance()
				.getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		list=zqbMeetingManageDAO.selectNum(userid,pageNumber,pageSize);
		return list;
	}
	public int selectNumTotalNum() {
		UserContext uc = UserContextUtil.getInstance()
				.getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		return zqbMeetingManageDAO.selectNumTotalNum(userid);
	}
	public AppointmentYYSX setAppointmentYysx(AppointmentYYSX appointmentYysx, Long yysx) {
		UserContext uc = UserContextUtil.getInstance()
				.getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		if(yysx!=0){
			int id = DBUtil.getInt("select ID from bd_xp_yysx where cjr='"+userid+"' and id="+yysx+"", "ID");
			AppointmentYYSX boData = zqbMeetingManageDAO.getBoDataAppointmentYYSX((long)id);
			appointmentYysx.setCjr(userid);
			appointmentYysx.setId(boData.getId());
			appointmentYysx.setKsrq(boData.getKsrq());
			appointmentYysx.setJzrq(boData.getJzrq());
			appointmentYysx.setSxms(boData.getSxms());
		}else{
			appointmentYysx.setCjr(userid);
		}
		return appointmentYysx;
	}

	public AppointmentYYSX saveAppointmentYYSX(AppointmentYYSX model) {
		model=zqbMeetingManageDAO.addBoDataAppointmentYYSX(model);
		String value=model.getSxms();
		Long dataId=model.getId();
		LogUtil.getInstance().addLog(dataId, "预约事项维护", "添加预约事项："+value);
		return model;
	}
	
	public void updateAppointmentYYSX(AppointmentYYSX model) {
		zqbMeetingManageDAO.updateBoDataAppointmentYYSX(model);
		String value=model.getSxms();
		Long dataId=model.getId();
		LogUtil.getInstance().addLog(dataId, "预约事项维护", "更新预约事项："+value);
	}
	public void Sysmanagers(AppointmentYYSX appointmentYysx,AppointmentNum appointmentNum,String sftz) {
		String smsContent = "";
		String sysMsgContent = "";
		HashMap hashmap=new HashMap();
		hashmap.put("SXMS", appointmentYysx.getSxms());
		hashmap.put("CJR", appointmentYysx.getCjr());
		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.YYS_ADD, hashmap); 
		sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.YYS_ADD, hashmap);
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userId = uc._userModel.getUserid();
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		List<HashMap> list = zqbMeetingManageDAO.getList(userId);
		for (HashMap map : list) {
			String khbh = map.get("KHBH").toString();
			List<HashMap<String,String>> orgUserList = getOrgUserList(khbh);
			for (HashMap<String, String> hashMap : orgUserList) {
				if(!smsContent.equals("")&&"1".equals(sftz)){
					Object mobile = hashMap.get("MOBILE");
					if(mobile!=null&&!mobile.equals("")){
						MessageAPI.getInstance().sendSMS(uc, mobile.toString(), smsContent);
					}
				}
				if(!sysMsgContent.equals("")&&map.get("USERID")!=null){
					String userid = map.get("USERID").toString();
					MessageAPI.getInstance().sendSysMsg(userid, "预约提醒", sysMsgContent);
				}
			}
		}
	}
	public void sendMsg(String corpCode, int sXID, String startDate) {
		String smsContent = "";
		String sysMsgContent = "";
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		String userId = userModel.getUserid();
		String roleid = uc.get_orgRole().getId();
		if("3".equals(roleid)){
			Map params = new HashMap();
			params.put(1,corpCode);
			HashMap hashmap=new HashMap();
			String zqjc=corpCode==null||corpCode.equals("")?"":DBUTilNew.getDataStr("ZQJC","SELECT ZQJC FROM BD_ZQB_KH_BASE WHERE ZQDM= ? ", params);
			String cjrsxms=sXID==0?"":DBUtil.getString("SELECT CJR ||','|| SXMS CJRSXMS FROM BD_XP_YYSX WHERE ID="+sXID+"", "CJRSXMS");
			
			String userid = cjrsxms.substring(0,cjrsxms.indexOf(","));
			String sxms = cjrsxms.substring(cjrsxms.indexOf(",")+1);

			String mobile = userid==null||userid.equals("")?"":DBUtil.getString("SELECT MOBILE FROM ORGUSER WHERE USERID='"+userid+"'", "MOBILE");

			hashmap.put("GSMC", zqjc+corpCode);
			hashmap.put("SXMS", sxms);
			hashmap.put("CJSJ", startDate);
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.DMYY_ADD, hashmap); 
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.DMYY_ADD, hashmap); 
			
			if(!smsContent.equals("")&&mobile!=null&&!mobile.equals("")){
					MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
			}
			if(!sysMsgContent.equals("")&&userid!=null&&!userid.equals("")){
				MessageAPI.getInstance().sendSysMsg(userid, "预约信息提醒", sysMsgContent);
			}
		}else{
			HashMap hashmap=new HashMap();
			String sxms=DBUtil.getString("select SXMS from bd_xp_yysx where ID="+sXID+"", "SXMS");
			hashmap.put("SXMS", sxms);
			hashmap.put("CJSJ", startDate);
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.QSYY_ADD, hashmap); 
			sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.QSYY_ADD, hashmap); 
			List<HashMap> list = zqbMeetingManageDAO.getDmList(corpCode);
			for (HashMap map : list) {
				if(!smsContent.equals("")){
					String mobile = map.get("MOBILE").toString();
					if(mobile!=null&&!mobile.equals("")){
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				if(!sysMsgContent.equals("")){
					String userid = map.get("USERID").toString();
					MessageAPI.getInstance().sendSysMsg(userid, "预约提醒", sysMsgContent);
				}
			}
		}
	}
	public boolean deleteYYSX(Long sxid) {
		boolean falg=false;
		AppointmentYYSX boDataAppointmentYYSX = zqbMeetingManageDAO.getBoDataAppointmentYYSX(sxid);
		if(boDataAppointmentYYSX!=null){
			zqbMeetingManageDAO.deleteAppointmentYYSX(boDataAppointmentYYSX);
			List<HashMap> hashMap=zqbMeetingManageDAO.getAppointmentYYS(sxid);
			for (HashMap hashMap2 : hashMap) {
				AppointmentNum boData = zqbMeetingManageDAO.getBoData(Long.parseLong(hashMap2.get("yysid").toString()));
				zqbMeetingManageDAO.deleteBoData(boData);
			}
			String jg = zqbMeetingManageDAO.deleteAppointmentYYXX(sxid);
			if("".equals(jg)){
				falg=true;
			}else{
				falg=false;
			}
		}
		return falg;
	}
	public String getZqdmxs(String customerno) {
		Map params = new HashMap();
		params.put(1,customerno);
		String zqdm=DBUTilNew.getDataStr("ZQDM","SELECT ZQDM FROM BD_ZQB_KH_BASE where CUSTOMERNO= ? ", params);
		return zqdm==null?"":"".equals(zqdm)?"":zqdm;
	}
	public String getZqjcxs(String customerno) {
		Map params = new HashMap();
		params.put(1,customerno);
		String zqjc=DBUTilNew.getDataStr("ZQJC","SELECT ZQJC FROM BD_ZQB_KH_BASE where CUSTOMERNO= ? ",params);
		return zqjc==null?"":"".equals(zqjc)?"":zqjc;
	}
	public String zqbmeetingset(String plantime,String meettype,String hysx,String customerno) throws ParseException{
		String str = "";
		String lastoneplantime="";
		SimpleDateFormat sdflo = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Date lastplantime = null;
		String meetname = null;
		String status = null;
		StringBuffer sql = new StringBuffer("SELECT PLANTIME,HYSX,MEETNAME,STATUS FROM BD_MEET_PLAN WHERE ID =(SELECT ID FROM (SELECT ID,PLANTIME FROM BD_MEET_PLAN WHERE MEETTYPE='董事会' AND CUSTOMERNO=? ORDER BY PLANTIME DESC) WHERE ROWNUM=1)");
		PreparedStatement stmt = null;
		Connection conn =null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, customerno);
			rs = stmt.executeQuery();
			while(rs.next()){
				lastplantime = rs.getTimestamp("PLANTIME");
				lastoneplantime = sdflo.format(lastplantime);
				meetname = rs.getString("MEETNAME");
				status = rs.getString("STATUS");
			}
		} catch (Exception e1) {logger.error(e1.toString());} finally{DBUtil.close(conn, stmt, rs);}
		
		if(lastoneplantime != null&&meetname != null&&status != null && !lastoneplantime.equals("")&&!meetname.equals("")&&!status.equals("")){
			Date plandateout = null;
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				plandateout = sdf0.parse(plantime);
			} catch (ParseException e) {
				logger.error(e.toString());
			}
			SimpleDateFormat sdfthisdate = new SimpleDateFormat("yyyy-MM-dd");
			if((plantime != null && !plantime.equals("")) && (meettype != null && !meettype.equals("")) && (hysx != null && !hysx.equals("")) && (sdfthisdate.parse(sdfthisdate.format(new Date()))).getTime() > (sdfthisdate.parse(sdfthisdate.format(plandateout))).getTime()){
				return "设定时间小于设定时间,";
			}else if((plantime != null && !plantime.equals("")) && (meettype != null && !meettype.equals("")) && (hysx != null && !hysx.equals("")) && lastplantime.getTime() < plandateout.getTime()){
				if(hysx.equals("临时会议") && meettype.equals("股东大会")){
					Calendar cal = Calendar.getInstance();
					cal.setTime(lastplantime);
					cal.add(Calendar.DAY_OF_YEAR,15);
					lastplantime=cal.getTime();
					
					Date plandate = null;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						plandate = sdf.parse(plantime);
					} catch (ParseException e) {
						logger.error(e.toString());
					}
					if(status.equals("已召开")){
						str = "使用最后一次董事会召开时间,"+lastoneplantime+",";
					}else if(status.equals("未召开")){
						if((lastplantime.getTime() >= plandate.getTime())){
							str = "重置设定时间小于15天,"+meetname+",";
						}
					}
				}else if((hysx.equals("正式会议")||hysx.equals("正式（年度）会议")) && meettype.equals("股东大会")){
					Calendar cal = Calendar.getInstance();
					cal.setTime(lastplantime);
					cal.add(Calendar.DAY_OF_YEAR,20);
					lastplantime=cal.getTime();
					
					Date plandate = null;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						plandate = sdf.parse(plantime);
					} catch (ParseException e) {
						logger.error(e.toString());
					}
					if(status.equals("已召开")){
						str = "使用最后一次董事会召开时间,"+lastoneplantime+",";
					}else if(status.equals("未召开")){
						if((lastplantime.getTime() >= plandate.getTime())){
							str = "重置设定时间小于20天,"+meetname+",";
						}
					}
				}
		}
		}
		return str;
		/** 董事会后召开股东大会时，如果是临时的两个会间隔小于15天，则要给出提示性信息，正式会议于董事会间隔小于20天要给出提示性信息。
			设计思路：
			一、	在添加“临时”股东大会的计划召开时间后，判断最后一次添加的董事会的时间（如果已经召开了，那么使用已召开时间，如果没有召开，那么使用计划召开时间），是否距今已有15天，若没有那么弹出确认提示框：
			距离第x界董事会第X次会议（或者第x届董事会第x次临时会议）的召开日期（或计划召开日期，根据是否已召开判断）不满15天，是否确认添加？
			如果确认添加，保存即可，否则不进行保存。
			二、	在添加“正式”股东大会的计划召开时间后，判断最后一次添加的董事会的时间（如果已经召开了，那么使用已召开时间，如果没有召开，那么使用计划召开时间），是否距今已有20天，若没有那么弹出确认提示框：
			距离第x界董事会第X次会议（或者第x届董事会第x次临时会议）的召开日期（或计划召开日期，根据是否已召开判断）不满20天，是否确认添加？
			如果确认添加，保存即可，否则不进行保存。
			
			注意：董事会时间的使用，是最后一次添加的董事会，如果已经召开了，那么使用已召开时间，如果没有召开，那么使用计划召开时间*/
}
	
	public List<HashMap<String,String>> getOrgUserList(String customerno) {
		StringBuffer sql = new StringBuffer("SELECT USERID,MOBILE,EMAIL FROM ORGUSER ORG WHERE ORG.EXTEND1 =?");
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			 conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> map=new HashMap<String,String>();
				String userid = rs.getString("USERID");
				String mobile = rs.getString("MOBILE");
				String email = rs.getString("EMAIL");
				map.put("USERID", userid);
				map.put("MOBILE", mobile);
				map.put("EMAIL", email);
				dataList.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap<String,String>> getListqx(String userid) {
		StringBuffer sql = new StringBuffer("select s.orgroleid from orgusermap s where s.userid=?");
		List dataList=new ArrayList();
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> map=new HashMap<String,String>();
				String orgroleid = rs.getString("orgroleid");
				map.put("orgroleid", orgroleid);
				dataList.add(orgroleid);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	//删除扣分类型信息
	public String deletekflb(String strID){
		String[] striD = strID.split(",");
		try{
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();			
			String userID = uc.get_userModel().getUserid();
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM bd_xp_kflxb where ");
			Map params=new HashMap();
			for (int i=0;i<striD.length;i++){
				sql.append("id = ?  or ");
				params.put(i+1, striD[i]);
			}
			sql.append("id=''");
			DBUTilNew.update(sql.toString(),params);		
			
		}
		catch(Exception ex)
        {
        	return ex.getMessage();        	
        }
		return "";
	}
	//查询需要修改的扣分类别信息
		public List<HashMap> cxupdatekflb(String kflbid) {
			StringBuffer sql = new StringBuffer("select * from bd_xp_kflxb where id=?");
			List<HashMap> dataList=new ArrayList<HashMap>();
			PreparedStatement ps = null;
			Connection conn =null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, kflbid);
				rs = ps.executeQuery();
				while (rs.next()) {
					HashMap map=new HashMap<String,String>();
					String id = rs.getString("ID");
					String kflb = rs.getString("KFLB");
					String cjzh = rs.getString("CJZH");
					String chsj = rs.getString("CHSJ");
					BigDecimal fs = rs.getBigDecimal("COUNT");
					map.put("ID", id);
					map.put("KFLB", kflb);
					map.put("CJZH", cjzh);
					map.put("FS",fs);
					map.put("CHSJ", chsj.substring(0,10));
					dataList.add(map);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
			return dataList;
		}
		//查询需要修改的扣分类别信息
		public boolean updatekflb(String kflbid,String kflb,String cjzh,String fs) {
			StringBuffer sql = new StringBuffer("update bd_xp_kflxb t  set t.kflb =?, t.cjzh =? , t.chsj = to_date(?,'yyyy-MM-dd'), t.count=?  where t.id = ?");
			List<HashMap> dataList=new ArrayList<HashMap>();
			Date dd = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dt = sdf.format(dd);
			Connection conn =null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, kflb);
				ps.setString(2, cjzh);
				ps.setString(3, dt);
				ps.setDouble(4, Double.parseDouble(fs));
				ps.setString(5, kflbid);
				rs = ps.executeQuery();
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
			return true;
		}
	//查询所有的扣分类别信息
	public List<HashMap> cxkflb(String cxkflb) {
		StringBuffer sql = new StringBuffer();
		if(cxkflb==null||cxkflb.equals("")){
		 sql.append("select * from bd_xp_kflxb order by chsj desc");
		}else{
		 sql.append("select * from bd_xp_kflxb where KFLB like ? order by chsj desc");
		}
			
		List<HashMap> dataList=new ArrayList<HashMap>();
		PreparedStatement ps = null;
		Connection conn =null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			if(cxkflb!=null&&!cxkflb.equals(""))
			ps.setString(1, "%"+cxkflb+"%");
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap map=new HashMap<String,String>();
				String id = rs.getString("ID");
				String kflb = rs.getString("KFLB");
				String cjzh = rs.getString("CJZH");
				String chsj = rs.getString("CHSJ");
				BigDecimal fs = rs.getBigDecimal("COUNT");
				map.put("ID", id);
				map.put("KFLB", kflb);
				map.put("CJZH", cjzh);
				map.put("FS",fs);
				map.put("CHSJ", chsj.substring(0,10));
				dataList.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	//保存新增扣分类别信息 
	public boolean Savekflb(String kflb,String cjzh,String fs){
		boolean flag = true;
		StringBuffer sql = new StringBuffer("insert into bd_xp_kflxb (ID,KFLB,CJZH,CHSJ,COUNT)values(zhu_one.nextval,?,?,to_date(?,'yyyy-MM-dd'),?)");
		Date dd = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt = sdf.format(dd);
		PreparedStatement ps = null;
		Connection conn=null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, kflb);
			ps.setString(2, cjzh);
			ps.setString(3, (dt));
			ps.setDouble(4, Double.parseDouble(fs));
			rs = ps.executeQuery();
		} catch (Exception e) {
			flag = false;
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return flag;
		
	}
	public HashMap<String, List<HashMap<String, Object>>> getListMap(String customerno, String startDate, int eventID, int pageSize,	int pageNumber) {
		StringBuffer sql = new StringBuffer();
		List parameter = new ArrayList();//存放参数
		sql.append("SELECT 'YYCLNAME'||YYSX.ID AS CLNAME,YYSX.ID YYSXID,YYSX.SXMS,YYSX.CJR,TO_NUMBER(NVL(CASE WHEN (TO_DATE(YYSX.YYSJ,'YYYY-MM-DD HH24:MI') - SYSDATE) * 24 * 60 * 60 > 0 THEN (TO_DATE(YYSX.YYSJ,'YYYY-MM-DD HH24:MI') - SYSDATE) * 24 * 60 * 60 ELSE 0 END,0)) DJS,TO_CHAR(YYSX.KSRQ,'YYYY-MM-DD')||'--'||TO_CHAR(YYSX.JZRQ,'YYYY-MM-DD') YYSJFW,YYS.KHFZR,KHBASE.CUSTOMERNO,CASE WHEN");
		sql.append(" (TO_CHAR(YYSX.KSRQ,'YYYY-MM-DD')<=TO_CHAR(SYSDATE,'YYYY-MM-DD') AND TO_CHAR(YYSX.JZRQ,'YYYY-MM-DD')>=TO_CHAR(SYSDATE,'YYYY-MM-DD')) AND YYS.SZS>NVL(YYSXNUM.YNUM,0) AND (YYSX.ID!=YYXX.ID OR YYXX.ID IS NULL) THEN 1 ELSE 0 END SFYY,CASE WHEN TO_CHAR(YYSX.JZRQ,'YYYY-MM-DD')>TO_CHAR(SYSDATE,'YYYY-MM-DD') THEN 1 ELSE 0 END SFGQ,DATANUM.DNUM,YYXX.ID YYXXID,TO_CHAR(YYXX.YYRQ,'yyyy-MM-dd') YYRQ,YYFJ.INSTANCEID,YYFJ.FJ FROM BD_XP_YYSX YYSX"
				+ " INNER JOIN ("
				+ " SELECT * FROM ("
				+ " SELECT ROW_NUMBER()OVER(PARTITION BY KHBH,YYSX ORDER BY SZSJ DESC) RN,KHBH,USERID,KHFZR,YYSX,SZS FROM BD_XP_YYS YYS"
				+ " INNER JOIN ("
				+ " SELECT KHBH,SUBSTR(FHSPR,1,INSTR(FHSPR,'[',-1)-1) USERID,FHSPR KHFZR FROM BD_MDM_KHQXGLB"
				+ " UNION ALL"
				+ " SELECT KHBH,SUBSTR(KHFZR,1,INSTR(KHFZR,'[',-1)-1),KHFZR FROM BD_MDM_KHQXGLB) A ON YYS.SZR = A.USERID"
				+ " )  T WHERE T.RN = 1) YYS ON YYSX.ID=YYS.YYSX AND YYSX.CJR = YYS.USERID");
		
		sql.append(" INNER JOIN (SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO = ?) KHBASE ON KHBASE.CUSTOMERNO = YYS.KHBH");
		parameter.add(customerno);
		
		sql.append(" LEFT JOIN (SELECT YYSX,COUNT(1) YNUM FROM BD_XP_YYXX WHERE TO_CHAR(YYRQ,'YYYY-MM-DD')=TO_CHAR(SYSDATE,'YYYY-MM-DD') GROUP BY YYSX) YYSXNUM ON YYSX.ID=YYSXNUM.YYSX");
		sql.append(" INNER JOIN (SELECT COUNT(1) DNUM,KHBASE.CUSTOMERNO FROM BD_XP_YYSX YYSX INNER JOIN ("
				+ " SELECT * FROM (SELECT ROW_NUMBER()OVER(PARTITION BY KHBH,YYSX ORDER BY SZSJ DESC) RN,KHBH,USERID,KHFZR,YYSX,SZS FROM BD_XP_YYS YYS"
				+ " INNER JOIN ("
				+ " SELECT KHBH,SUBSTR(FHSPR,1,INSTR(FHSPR,'[',-1)-1) USERID,FHSPR KHFZR FROM BD_MDM_KHQXGLB"
				+ " UNION ALL "
				+ " SELECT KHBH,SUBSTR(KHFZR,1,INSTR(KHFZR,'[',-1)-1),KHFZR FROM BD_MDM_KHQXGLB) A ON YYS.SZR = A.USERID"
				+ " )  T WHERE T.RN = 1) YYS ON YYSX.ID=YYS.YYSX AND YYSX.CJR = YYS.USERID");
		sql.append(" INNER JOIN (SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO = ?) KHBASE ON KHBASE.CUSTOMERNO = YYS.KHBH");
		parameter.add(customerno);
		
		sql.append(" LEFT JOIN (SELECT YYSX,COUNT(1) YNUM FROM BD_XP_YYXX GROUP BY YYSX) YYSXNUM ON YYSX.ID=YYSXNUM.YYSX GROUP BY CUSTOMERNO) DATANUM ON KHBASE.CUSTOMERNO=DATANUM.CUSTOMERNO"
				+ " LEFT JOIN BD_XP_YYXX YYXX ON YYXX.YYSX=YYSX.ID AND KHBASE.CUSTOMERNO=YYXX.KHBH "
				+ " LEFT JOIN ( "
				+ " SELECT CD.INSTANCEID INSTANCEID,DG.YYID,DG.KHBH,DG.FJ FROM BD_XP_DQYYGGFJ DG"
				+ " INNER JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_DQYYGGFJ')) CD ON DG.ID=CD.DATAID"
				+ " ) YYFJ ON YYFJ.YYID=YYXX.ID AND YYFJ.KHBH=KHBASE.CUSTOMERNO");
		sql.append(" WHERE 1=1");
		if(eventID!=0){
			sql.append(" AND YYSX.ID = ? ");
			parameter.add(eventID);
		}
		if(startDate!=null&&!startDate.equals("")){
			sql.append(" AND TO_CHAR(YYSX.KSRQ,'YYYY-MM-DD') <= ? AND TO_CHAR(YYSX.JZRQ,'YYYY-MM-DD') >= ? ");
			parameter.add(startDate);
			parameter.add(startDate);
		}
		sql.append(" ORDER BY DJS,YYXX.YYRQ,SFGQ DESC,YYSX.YYSJ DESC");
		HashMap<String,List<HashMap<String, Object>>> listMap = zqbMeetingManageDAO.getListMap(sql.toString(),parameter);
		return listMap;
	}
			
	/**
	 * 导出  三会计划excl
	 */
			public void thxmexportexcl(HttpServletResponse response,String customerno){
				// 第一步，创建一个webbook，对应一个Excel文件
				HSSFWorkbook wb = new HSSFWorkbook();
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
				HSSFSheet sheet = wb.createSheet("三会计划");
				
			
				HSSFRow row = sheet.createRow((int) 0);
				row.setHeightInPoints(30);
				// 第四步，创建单元格，并设置值表头 设置表头居中
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
				style.setBorderBottom((short) 1);
				style.setBorderLeft((short) 1);
				style.setBorderRight((short) 1);
				style.setBorderTop((short) 1);
				style.setWrapText(true);
				HSSFCellStyle style1 = wb.createCellStyle();
				style1.setBorderBottom((short) 1);
				style1.setBorderLeft((short) 1);
				style1.setBorderRight((short) 1);
				style1.setBorderTop((short) 1);
				style1.setWrapText(true);
				HSSFCellStyle style2 = wb.createCellStyle();
				style2.setBorderBottom((short) 1);
				style2.setBorderLeft((short) 1);
				style2.setBorderRight((short) 1);
				style2.setBorderTop((short) 1);
				style2.setWrapText(true);
				style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
				
				HSSFCellStyle style3 = wb.createCellStyle();
				HSSFFont font = wb.createFont();
				style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
				font.setFontHeightInPoints((short) 12);
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style3.setFont(font);
				HSSFFont font2 = wb.createFont();
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style.setFont(font2);
				//yearCell.setCellStyle(style3);
				HSSFCell cell = row.createCell((short) 0);
				cell.setCellValue("公司代码");
				cell.setCellStyle(style);
				cell = row.createCell((short) 1);
				cell.setCellValue("公司简称");
				cell.setCellStyle(style);
				
				cell = row.createCell((short) 2);
				cell.setCellValue("会议名称");
				cell.setCellStyle(style);
				
				cell = row.createCell((short) 3);
				cell.setCellValue("会议类型");
				cell.setCellStyle(style);
				
			
				
				cell = row.createCell((short) 4);
				cell.setCellValue("届次");
				cell.setCellStyle(style);
				cell = row.createCell((short) 5);
				cell.setCellValue("会议属性");
				cell.setCellStyle(style);
				cell = row.createCell((short) 6);
				cell.setCellValue("计划创建时间");
				cell.setCellStyle(style);
				cell = row.createCell((short) 7);
				cell.setCellValue("计划召开时间");
				cell.setCellStyle(style);
				cell = row.createCell((short) 8);
				cell.setCellValue("会议状态");
				cell.setCellStyle(style);
			
			

				// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
				//获取当前用户权限
				List list = zqbMeetingManageDAO.getMeetExcl(customerno);
				int n = 1;
				if (list == null) {
					return;
				}
				int m = 0;
				for (int i = 0; i < list.size(); i++) {

						Map map = (HashMap) list.get(i);
						row = sheet.createRow((int) n++);
						// 第四步，创建单元格，并设置值
						HSSFCell cell1 = row.createCell((short) 0);
						cell1.setCellValue(map.get("zqdm").toString());
						cell1.setCellStyle(style2);
						HSSFCell cell2 = row.createCell((short) 1);
						cell2.setCellValue(map.get("zqjc").toString());
						cell2.setCellStyle(style2);
						
						
						
						
						HSSFCell cell3 = row.createCell((short) 2);
						cell3.setCellValue(map.get("meetname").toString());
						cell3.setCellStyle(style2);
						HSSFCell cell4 = row.createCell((short) 3);
						if(map.get("meettype").equals("专业委员会")){
							cell4.setCellValue(map.get("meettype").toString()+"--"+map.get("zywyh"));
						}else{
							cell4.setCellValue(map.get("meettype").toString());
						}
						
						cell4.setCellStyle(style2);
						
					
						
						HSSFCell cell6 = row.createCell((short) 4);
						cell6.setCellValue("第"+map.get("jc").toString()+"届");
						cell6.setCellStyle(style2);
						HSSFCell cell7 = row.createCell((short) 5);
						cell7.setCellValue(map.get("hysx").toString()+" 第"+map.get("hc").toString()+"次");
						cell7.setCellStyle(style2);
						HSSFCell cell8 = row.createCell((short) 6);
						cell8.setCellValue(map.get("ctime").toString());
						cell8.setCellStyle(style2);
						HSSFCell cell9 = row.createCell((short) 7);
						cell9.setCellValue(map.get("ptime").toString());
						cell9.setCellStyle(style2);
						
						HSSFCell cell10 = row.createCell((short) 8);
						cell10.setCellValue(map.get("status").toString());
						cell10.setCellStyle(style2);
						
						m++;

				}
				sheet.setColumnWidth(0, 4000);
				sheet.setColumnWidth(1, 4000);
				sheet.setColumnWidth(2, 8500);
				sheet.setColumnWidth(3, 9000);
				sheet.setColumnWidth(4, 4000);
				sheet.setColumnWidth(5, 7000);
				sheet.setColumnWidth(6, 6000);
				sheet.setColumnWidth(7, 6000);
				sheet.setColumnWidth(8, 4000);
				
				OutputStream out1 = null;
				// 第六步，将文件存到指定位置
				try {
					String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("三会计划.xls");
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
							logger.error(e,e);
						}
					}

				}
			}
//			public String getRoleGroupTreeJson(List<String> selected) throws ParseException {
//				StringBuffer jsonHtml = new StringBuffer();
//				List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
//				List lables=new ArrayList();
//				lables.add(0, "userid");
//				lables.add(1, "username");
//				String sql="SELECT * FROM orguser where orgroleid=4";
//				List<HashMap> dataList = DBUTilNew.getDataList(lables, sql, null);
//				
//				for (HashMap model : dataList) {
//					Map<String, Object> item = new HashMap<String, Object>();
//					item.put("id", model.get("userid")==null?"":model.get("userid").toString());
//					
//					item.put("text", model.get("username")==null?"":model.get("username").toString());
//					item.put("iconCls", "icon-ok");
//					item.put("children", this.getRoleTreeJsonById(model.getId().toString(), selected));
//					Map<String,Object> attributes = new HashMap<String,Object>();	
//					attributes.put("nodeTye","roleGroup");
//					item.put("attributes", attributes);	
//					items.add(item);
//				}
//				JSONArray json = JSONArray.fromObject(items);
//				jsonHtml.append(json);
//				return jsonHtml.toString();
//			}
//			
//			public List<Map<String,Object>> getRoleTreeJsonById(String groupId, List<String> selected) throws ParseException {
//				StringBuffer jsonHtml = new StringBuffer();
//				List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
//				List<OrgRole> list = orgRoleDAO.getRoleList(Long.parseLong(groupId));
//				for (OrgRole model : list) {
//					Map<String, Object> item = new HashMap<String, Object>();
//					item.put("id", model.getId() + "");
//					if (selected.contains(model.getId()+"")) {
//						item.put("checked", "true");
//					}
//					item.put("text", model.getRolename());
//					item.put("iconCls", "icon-ok");
//					Map<String,Object> attributes = new HashMap<String,Object>();	
//					attributes.put("nodeTye","roleLeaf");
//					item.put("attributes", attributes);	
//					items.add(item);
//				}
////				JSONArray json = JSONArray.fromObject(items);
////				jsonHtml.append(json);
//				return items;
//			}
			
			
}













