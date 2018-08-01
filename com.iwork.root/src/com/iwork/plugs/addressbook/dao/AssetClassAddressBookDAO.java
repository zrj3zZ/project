package com.iwork.plugs.addressbook.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.core.db.DBUtil;

public class AssetClassAddressBookDAO  extends HibernateDaoSupport{
	/**
	 * 获得资产类别
	 * @param unit 模糊查询条件
	 * @return
	 */
	public List<HashMap<String,String>> getAssetClass(String unit){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		StringBuffer sql = new StringBuffer("select * from bd_zckp_zclb lb where 1=1");
		if(unit!=null){
			sql.append(" and lb.typeno like '%").append(unit).append("%' or lb.typename like '%").append(unit).append("%'");
		}
		sql.append(" order by lb.id");
		Connection con= null;
		ResultSet rs = null;
		Statement stmt = null;
		try{		
			con = DBUtil.open();
			stmt = con.createStatement(); 
			rs = DBUtil.executeQuery(con, stmt, sql.toString());
		    while(rs.next()){
		    	HashMap<String,String> hash = new HashMap<String,String>();
		    	String typeno = rs.getString("TYPENO");
		    	String typename = rs.getString("TYPENAME");
		    	hash.put("TYPENO", typeno);
		    	hash.put("TYPENAME", typename);
		    	list.add(hash);
		    }   
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			DBUtil.close(con, stmt, rs);
		}
		return list;
	}
	
	/**
	 * 根据资产类别编号获得资产属性
	 * @param typeno
	 * @return
	 */
	public List<HashMap<String,Object>> getPropertyByTypeno(String typeno){
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		StringBuffer sql = new StringBuffer("select * from bd_zckp_zcsx t where 1=1");
		if(typeno!=null){
			sql.append(" and t.typeno='").append(typeno).append("'");
		}
		sql.append(" and t.state='是'");
		sql.append(" order by t.sort asc");
		Connection con= null;
		ResultSet rs = null;
		Statement stmt = null;
		try{		
			con = DBUtil.open();
			stmt = con.createStatement(); 
			rs = DBUtil.executeQuery(con, stmt, sql.toString());
		    while(rs.next()){
		    	HashMap<String,Object> hash = new HashMap<String,Object>();
		    	String propertyno = rs.getString("PROPERTYNO");
		    	String propertyname = rs.getString("PROPERTYNAME");
		    	String typeNo = rs.getString("TYPENO");
		    	int dl = rs.getInt("DL");
		    	String datatype = rs.getString("DATATYPE");
		    	String required = rs.getString("REQUIRED");
		    	String describe = rs.getString("DESCRIBE");
		    	int sort = rs.getInt("SORT");
		    	hash.put("PROPERTYNO", propertyno);
		    	hash.put("PROPERTYNAME", propertyname);
		    	hash.put("TYPENO", typeNo);
		    	hash.put("DL", dl);
		    	hash.put("DATATYPE", datatype);
		    	hash.put("REQUIRED", required);
		    	hash.put("DESCRIBE", describe);
		    	hash.put("SORT", sort);
		    	list.add(hash);
		    }   
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			DBUtil.close(con, stmt, rs);
		}
		return list;
	}
	
	public String getTypenameByNo(String typeno){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.typename from bd_zckp_zclb t where t.typeno='").append(typeno).append("'");
		return DBUtil.getString(sql.toString(), "TYPENAME");
	}
}
