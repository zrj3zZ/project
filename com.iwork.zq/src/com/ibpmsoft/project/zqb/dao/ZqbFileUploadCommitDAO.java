package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.iwork.core.organization.context.UserContext;
import oracle.jdbc.driver.OracleTypes;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.tools.UserContextUtil;

public class ZqbFileUploadCommitDAO extends HibernateDaoSupport {


    public List<HashMap> getListSize() {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        StringBuffer sql = new StringBuffer();
        sql.append("select s.id,s.zqdm,s.zqjc,s.gsmc,s.mkmc,s.startdate,s.enddate,s.dbrname,s.dbsj,s.ysbmc from  BD_ZQB_DBWJ s where 1=1");
        if(uc.get_userModel().getOrgroleid()!=5){
            sql.append(" and s.dbrid="+uc.get_userModel().getUserid());
        }
        sql.append(" order by id desc");
        List params = new ArrayList();

        final String sql1 = sql.toString();
        final List param = params;
        return this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql1);
                List<HashMap> l = new ArrayList<HashMap>();
                List<Object[]> list = query.list();
                HashMap map;
                for (Object[] object : list) {
                    map = new HashMap();
                    BigDecimal id = (BigDecimal) object[0];
                    map.put("id", id);
                    l.add(map);
                }
                return l;
            }
        });
    }
    public List<HashMap> getList(int pageSize, int pageNumber ) {
        List l=new ArrayList();
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        final String userid = uc.get_userModel().getUserid();
        final int pageSize1 = pageSize;
        final int startRow1 = (pageNumber - 1) * pageSize;
        List params = new ArrayList();
        StringBuffer sql = new StringBuffer();
        sql.append("select s.id,s.zqdm,s.zqjc,s.gsmc,s.mkmc,s.startdate,s.enddate,s.dbrname,s.dbsj,s.ysbmc from  BD_ZQB_DBWJ s where 1=1");
        if(uc.get_userModel().getOrgroleid()!=5){
            sql.append(" and s.dbrid="+uc.get_userModel().getUserid());
        }
        sql.append(" order by id desc");
        final String sql1 = sql.toString();
        final List param = params;
        return this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(org.hibernate.Session session)
                    throws HibernateException, SQLException {
                Query query = session.createSQLQuery(sql1);
                List<Map> l = new ArrayList<Map>();
                query.setFirstResult(startRow1);
                query.setMaxResults(pageSize1);
                List<Object[]> li = query.list();
                HashMap map;
                HashMap m = new HashMap();
                BigDecimal b;
                int n = 0;
                Long orgroleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
                for (Object[] object : li) {
                    map = new HashMap();
                    BigDecimal id = (BigDecimal) object[0];
                    String zqdm = (String) object[1];
                    String zqjc = (String) object[2];
                    String gsmc = (String) object[3];
                    String mkmc=(String)object[4]==null?"":(String) object[4];;
                    String  startdate= (String) object[5]==null?"":(String) object[5];
                    String enddate = (String) object[6]==null?"":(String) object[6];;
                    String dbrname=(String)object[7];
                    Date  dbsj= (Date) object[8];
                    String  ysbmc= (String) object[9];
                    String xsgsmc="";
                    String cxtj="";
                    if(zqdm==null || "".equals(zqdm) || zqjc==null || "".equals(zqjc)){
                        xsgsmc=gsmc;
                    }else{
                        xsgsmc=zqdm+"【"+zqjc+"】";
                    }
                    if(startdate!="" && enddate!="" && mkmc!=""){
                        cxtj="查询从"+startdate+"到"+enddate+"的"+mkmc+"信息";
                    }else if(startdate!="" && enddate!="" && mkmc==""){
                        cxtj="查询从"+startdate+"到"+enddate+"的所有模块信息";
                    }else if(startdate!="" && enddate=="" && mkmc==""){
                        cxtj="查询从"+startdate+"开始的所有模块信息";
                    }else if(startdate!="" && enddate=="" && mkmc!=""){
                        cxtj="查询从"+startdate+"开始的"+mkmc+"信息";
                    }else if(startdate=="" && enddate=="" && mkmc!=""){
                        cxtj=mkmc+"信息";
                    }else if(startdate=="" && enddate!="" && mkmc!=""){
                        cxtj="截止至"+enddate+mkmc+"信息";
                    }  else if(startdate=="" && enddate!="" && mkmc==""){
                        cxtj="截止至"+enddate+"的所有信息";
                    }
                    map.put("id", id.toString());
                    map.put("xsgsmc", xsgsmc);
                    map.put("cxtj", cxtj);
                    map.put("dbrname", dbrname);
                    map.put("dbsj", dbsj);
                    map.put("ysbmc", ysbmc);
                    l.add(map);
                }
                return l;
            }
        });
    }
	public HashMap getFileCommit(String username,String titleName,String beginSj,String endSj,int pageNumber,int pageSize) {
		int pageSize1 = pageNumber*pageSize;
		int startRow1 = (pageNumber - 1) * pageSize;
		HashMap data = new HashMap();
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> _list = new ArrayList<HashMap>();
		StringBuffer query = new StringBuffer("call FILESTATISTICS.GETSTATISTICSLIST(?,?,?,?,?,?,?,?,?,?,?,?)");
		
		Connection conn = null;
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString()); /*CallableStatement 为调用存储过程的特有类*/
			cstmt.setString(1,username);
			cstmt.setString(2,titleName); /*注册存储过程的输出参数为游标类型--即resultSet类型*/
			cstmt.setString(3,beginSj);
			cstmt.setString(4,endSj);
			cstmt.setInt(5,startRow1);
			cstmt.setInt(6,pageSize1);
			cstmt.registerOutParameter(7, OracleTypes.NUMBER);
			cstmt.registerOutParameter(8, OracleTypes.CURSOR);
			cstmt.registerOutParameter(9, OracleTypes.CURSOR);
			cstmt.registerOutParameter(10, OracleTypes.CURSOR);
			cstmt.setString(11,null);
			cstmt.registerOutParameter(12, OracleTypes.NUMBER);
			cstmt.execute(); //执行存储过程
			
			BigDecimal totalnum = (BigDecimal)cstmt.getObject(7);
			data.put("FILETOTALNUM", totalnum.intValue());
			ResultSet rs = (ResultSet) cstmt.getObject(8); //获取数据集合
			HashMap map;
	        while (rs.next()){
	        	map = new HashMap();
	        	Object user_name = rs.getObject("USERNAME");
	        	Object uploadtime = rs.getObject("UPLOAD_TIME");
	        	Object title = rs.getObject("IFORM_TITLE");
	        	Object count = rs.getObject("NUM");
	        	Object total = rs.getObject("TOTAL");
	        	Object rnum = rs.getObject("CNUM");
				map.put("USERNAME", user_name);
				map.put("UPLOADTIME", uploadtime);
				map.put("TITLE", title);
				map.put("COUNT", count);
				map.put("TOTAL", total);
				map.put("RNUM", rnum);
	        	list.add(map);
	        }
	        data.put("FILECOMMITLIST", list);
	        
	        ResultSet _rs = (ResultSet) cstmt.getObject(9); //获取数据集合
	        HashMap _map;
	        while (_rs.next()){
	        	_map = new HashMap();
	        	Object user_name = _rs.getObject("USERNAME");
	        	Object uploadtime = _rs.getObject("UPLOAD_TIME");
	        	Object title = _rs.getObject("IFORM_TITLE");
				Object count = _rs.getObject("NUM");
				_map.put("USERNAME", user_name);
				_map.put("UPLOADTIME", uploadtime);
				_map.put("TITLE", title);
				_map.put("COUNT", count);
	        	_list.add(_map);
	        }
	        data.put("FILELIST", _list);
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, null, null);
		}
		return data;
	}
	public HashMap getFileDetails(String username,String titleName,String uploadtime,int pageNow,int pageSize) {
		int pageSize1 = pageNow*pageSize;
		int startRow1 = (pageNow - 1) * pageSize;
		HashMap data = new HashMap();
		List<HashMap> list = new ArrayList<HashMap>();
		StringBuffer query = new StringBuffer("call FILESTATISTICS.GETSTATISTICSLIST(?,?,?,?,?,?,?,?,?,?,?,?)");
		
		Connection conn = null;
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString());
			cstmt.setString(1,username);
			cstmt.setString(2,titleName);
			cstmt.setString(3,null);
			cstmt.setString(4,null);
			cstmt.setInt(5,startRow1);
			cstmt.setInt(6,pageSize1);
			cstmt.registerOutParameter(7, OracleTypes.NUMBER);
			cstmt.registerOutParameter(8, OracleTypes.CURSOR);
			cstmt.registerOutParameter(9, OracleTypes.CURSOR);
			cstmt.registerOutParameter(10, OracleTypes.CURSOR);
			cstmt.setString(11,uploadtime);
			cstmt.registerOutParameter(12, OracleTypes.NUMBER);
			cstmt.execute(); //执行存储过程
			
			ResultSet rs = (ResultSet) cstmt.getObject(10); //获取数据集合
			HashMap map;
	        while (rs.next()){
	        	map = new HashMap();
				Object user_name = rs.getObject("USERNAME");
				Object file_Name = rs.getObject("FILE_SRC_NAME");
				Object upload_time = rs.getObject("UPLOAD_TIME");
				Object iform_title = rs.getObject("IFORM_TITLE");
				Object attach = rs.getObject("ATTACH");
				map.put("USERNAME", user_name);
				map.put("FILENAME", file_Name);
				map.put("UPLOADTIME", upload_time);
				map.put("TITLE", iform_title);
				map.put("FILEID", attach);
	        	list.add(map);
	        }
	        data.put("fileDetailsList", list);
	        BigDecimal totalnum = (BigDecimal)cstmt.getObject(12);
	        data.put("totalNum", totalnum.intValue());
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, null, null);
		}
		return data;
	}
	
	//列表页
	public Map zqbFileDownloadListChange(List<String> orgroleidlist,String customername,String zqdm,String gn,String sja,String sjb,String zqServer, int pageNumber, int pageSize,String xznr) {
        Map map=new HashMap();
		List<HashMap> dlist = new ArrayList<HashMap>();
        List<HashMap> dlist2 = new ArrayList<HashMap>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		String user = UserContextUtil.getInstance().getCurrentUserFullName();
		StringBuffer query = new StringBuffer("call FILEDOWNLOAD.GETDOWNLOADLIST(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		Connection conn = null;
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString()); //CallableStatement 为调用存储过程的特有类
			cstmt.setString(1,user);
			cstmt.setString(2,userid); //注册存储过程的输出参数为游标类型--即resultSet类型
			cstmt.setString(3,customername);
			cstmt.setString(4,zqdm);
			cstmt.setString(5,gn);
			cstmt.setString(6,sja);
			cstmt.setString(7,sjb);
			cstmt.registerOutParameter(8, OracleTypes.CURSOR);
			cstmt.setString(9,"list");
			cstmt.setString(10,zqServer);
            cstmt.setString(11,xznr);
            cstmt.setString(12,"");
            cstmt.registerOutParameter(13, OracleTypes.CURSOR);
			cstmt.execute(); //执行存储过程
			
			ResultSet rsrun = (ResultSet) cstmt.getObject(8); //获取数据集合
            ResultSet rSXset = (ResultSet) cstmt.getObject(13); //获取数据集合
			HashMap runmap;
	        while (rsrun.next()){
	        	runmap = new HashMap();
	        	Object customer_name = rsrun.getObject("CUSTOMERNAME");
	        	Object zq_jc = rsrun.getObject("ZQJC");
	        	Object zq_dm = rsrun.getObject("ZQDM");
                Object khbh = rsrun.getObject("CUSTOMERNO");
	        	runmap.put("CUSTOMERNAME", customer_name);
	        	runmap.put("ZQJC", zq_jc);
	        	runmap.put("ZQDM", zq_dm);
                runmap.put("KHBH", khbh);
	        	dlist.add(runmap);
	        }
            while (rSXset.next()){
                runmap = new HashMap();
                Object g_n = rSXset.getObject("GN");
                runmap.put("GN",g_n);
                dlist2.add(runmap);
            }
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, null, null);
		}
        map.put("list1",dlist);
        map.put("list2",dlist2);
		return map;
	}

	//单个下载and批量下载
	public List zqbFileDownloadListPackage(List<String> orgroleidlist,String customername,String zqjc,String zqdm,String gn,String sja,String sjb,String zqServer,String khbh,String xzlx,Long qb) {
        if(khbh!=null && !"".equals(khbh)){
            khbh.indexOf(",");
            if(khbh.indexOf(",")!=-1){
                khbh= "'"+khbh.substring(0,khbh.length()-1).replaceAll(",","','")+"'";
            }
        }
        if(qb==1){
            khbh="";
        }
		List<HashMap> dlist = new ArrayList<HashMap>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		String user = UserContextUtil.getInstance().getCurrentUserFullName();
		
		StringBuffer query = new StringBuffer("call FILEDOWNLOAD.GETDOWNLOADLIST(?,?,?,?,?,?,?,?,?,?,?,?,?)");

		Connection conn = null;
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString()); //CallableStatement 为调用存储过程的特有类
			cstmt.setString(1,user);
			cstmt.setString(2,userid); //注册存储过程的输出参数为游标类型--即resultSet类型
			cstmt.setString(3,"");
			cstmt.setString(4,"");
			cstmt.setString(5,gn);
			cstmt.setString(6,sja);
			cstmt.setString(7,sjb);
			cstmt.registerOutParameter(8, OracleTypes.CURSOR);
			cstmt.setString(9,"download");
			cstmt.setString(10,zqServer);
            cstmt.setString(11,xzlx);
            cstmt.setString(12,khbh);
            cstmt.registerOutParameter(13, OracleTypes.CURSOR);
			cstmt.execute(); //执行存储过程
			
			ResultSet rsrun = (ResultSet) cstmt.getObject(8); //获取数据集合
			HashMap map;
			int n=0;
	        while (rsrun.next()){
	        	map = new HashMap();
	        	Object instanceid = rsrun.getObject("INSTANCEID");
	        	Object customer_name = rsrun.getObject("CUSTOMERNAME");
	        	Object zq_jc = rsrun.getObject("ZQJC");
	        	Object zq_dm = rsrun.getObject("ZQDM");
	        	Object g_n = rsrun.getObject("GN");
	        	Object xfmk = rsrun.getObject("XFMK");
	        	Object file_src_name = rsrun.getObject("FILE_SRC_NAME");
				Object sj = rsrun.getObject("SJ");
				Object file_url = rsrun.getObject("FILE_URL");
				if(xzlx.equals("审批意见")){
                    Object id = rsrun.getObject("ID");
                    Object content = rsrun.getObject("CONTENT");
                    Object action = rsrun.getObject("ACTION");
                    Object username = rsrun.getObject("USERNAME");
                    Object createtime = rsrun.getObject("CREATETIME");
                    Object name = rsrun.getObject("NAME");
                    map.put("ID", id);
                    map.put("CONTENT", content);
                    map.put("ACTION", action);
                    map.put("USERNAME", username);
                    map.put("CREATETIME", createtime);
                    map.put("NAME", name);
                }

				map.put("INSTANCEID", instanceid);
				map.put("CUSTOMERNAME", customer_name);
				map.put("XFMK", xfmk);
				map.put("FILE_SRC_NAME", file_src_name);
				map.put("GN", g_n);
				map.put("ZQJC", zq_jc);
				map.put("ZQDM", zq_dm);
				map.put("SJ", sj==null?"":sj.toString());
				map.put("FILE_URL", file_url);

	        	dlist.add(map);
	        }
	        
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, null, null);
		}
		return dlist;
	}
}
