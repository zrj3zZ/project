package com.iwork.plugs.extdbsrc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.plugs.extdbsrc.cache.SysExdbsrcCache;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;
import com.iwork.plugs.extdbsrc.util.ExtDBSourceUtil;

public class ExtDBSrcDao extends HibernateDaoSupport {
	private PreparedStatement pstmt = null;
	/**
	 * 获得model所对应数据库表的表名和字段名
	 * @param model
	 * @param sql
	 * @return
	 */
	public List<String> getTableFields(SysExdbsrcCenter model,String sql){
		List<String> list = new ArrayList<String>();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		if(model==null){			
			if(sql!=null){
				try {
					con = DBUtil.open();
					st = con.prepareStatement(sql.toUpperCase());
					rs = st.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					if(rsmd.getColumnCount()>0){
						list.add(rsmd.getTableName(1));
					}
					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					//	 String tableName = rsmd.getTableName(i);
						list.add(rsmd.getColumnName(i));      
		            }				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error(e);
				}finally{
					DBUtil.close(con, st, rs);
				}
			}		
		}else{			
			if(sql!=null){
				try {
					con = ExtDBSourceUtil.getInstance().open(model);
					pstmt = con.prepareStatement(sql);
					rs = pstmt.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData();
					if(rsmd.getColumnCount()>0){
						String table = rsmd.getTableName(1);
						list.add(table);
					}
					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
						 String tableName = rsmd.getTableName(i);
						list.add(rsmd.getColumnName(i));      
		            }				
				} catch (SQLException e) {
					logger.error(e);
				}finally{
					try{
					rs.close();
					st.close();
					ExtDBSourceUtil.getInstance().close(con);
					}catch(Exception e){logger.error(e);}
				}
			}		
		}
		
		return list;
	}
	/**
	 * 获得外部数据源列表
	 * @return
	 */
	public List<SysExdbsrcCenter> getList(){
		String hql="FROM "+SysExdbsrcCenter.DATABASE_ENTITY+" order by id";
		return this.getHibernateTemplate().find(hql);
	}
	/**
	 * 数据源sql语句的测试
	 * @param model
	 * @param sqlScript
	 * @return
	 */
	public String test(SysExdbsrcCenter model,String sqlScript){
		String msg = "error";
		if(model!=null&sqlScript!=null){
			
		}
		return msg;
	}
	/**
	 * 保存
	 */
	public void save(SysExdbsrcCenter model){
		this.getHibernateTemplate().save(model);
		SysExdbsrcCache.getInstance().putModel(model);
	}
	/**
	 * 获得单条数据
	 * @param id
	 * @return
	 */
	public SysExdbsrcCenter getModel(long id){
		SysExdbsrcCenter model = SysExdbsrcCache.getInstance().getModel(id);
		if(model==null){
			model = (SysExdbsrcCenter)this.getHibernateTemplate().get(SysExdbsrcCenter.class, id);
		}
		return  model;
	}
	/**
	 * 删除单条数据
	 * @param model
	 */
	public void delModel(SysExdbsrcCenter model){ 
		this.getHibernateTemplate().delete(model);
		SysExdbsrcCache.getInstance().remove(model.getId());
	}
	
	/**
	 * 根据uuid，获得外部数据源
	 * @param uuid
	 * @return
	 */
	
	public SysExdbsrcCenter getModelForUUID(String uuid){
		SysExdbsrcCenter model = null;
		String hql="FROM "+SysExdbsrcCenter.DATABASE_ENTITY+" where uuid=?";
		DBUtilInjection d=new DBUtilInjection();
	    
	    if ((uuid != null) && (!"".equals(uuid))) {
        	if(d.HasInjectionData(uuid)){
				return model;
			}
         
        }
		Object[] value = {uuid};
		List<SysExdbsrcCenter> list =  this.getHibernateTemplate().find(hql,value);
		if(list!=null&&list.size()>0){
			model = list.get(0);
		}
		return model;
	}
	/**
	 * 根据name，获得外部数据源
	 * @param uuid
	 * @return
	 */
	
	public SysExdbsrcCenter getModelForName(String title){
		SysExdbsrcCenter model = null;
		String hql="FROM "+SysExdbsrcCenter.DATABASE_ENTITY+" where dsrcTitle=?";
		DBUtilInjection d=new DBUtilInjection();
	  
	    if ((title != null) && (!"".equals(title))) {
        	if(d.HasInjectionData(title)){
				return model;
			}
         
        }
		Object[] value = {title};
		List<SysExdbsrcCenter> list =  this.getHibernateTemplate().find(hql,value);
		if(list!=null&&list.size()>0){
			model = list.get(0);
		}
		return model;
	}
	/**
	 * 更新单条数据
	 * @param model
	 */
	public void updateModle(SysExdbsrcCenter model){
		this.getHibernateTemplate().update(model);
		SysExdbsrcCache.getInstance().putModel(model);
	}
	/**
	 * 测试Oracle链接
	 * @param model
	 * @return 0:链接成功;1:加载驱动失败;2:注册驱动失败;3:用户名或密码错误;4:链接地址错误
	 */
	public int testOraCon(SysExdbsrcCenter model,String sql){
		int code=0;
		try {
			Class.forName(model.getDriverName());
		} catch (ClassNotFoundException e) {logger.error(e);
			code=1;
		}
		try {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {logger.error(e);
				code=2;
		}//注册oracle驱动	
		Connection con=null;
		PreparedStatement stmt=null;
		try {
			
			con=DriverManager.getConnection(model.getDsrcUrl(),model.getUsername(),model.getPassword());
			if("".equals(sql)){}
			else{
				stmt = con.prepareStatement(sql);
				if(sql.toLowerCase().indexOf("delete")>=0 || sql.toLowerCase().indexOf("update")>=0 || sql.toLowerCase().indexOf("inserts")>=0){
					stmt.executeUpdate();
				}else{
					stmt.executeQuery();
				}
				stmt.close();
			}
			con.close();
		} catch (SQLException e) {
			logger.error(e);
			if(e.getErrorCode()==1017){
				code=3;
			}
			else{
				code=4;
			}
			
		}finally {
			DBUtil.close(con, stmt, null);
		}
		return code;
		
	}
	/**
	 * 测试MySQL链接
	 * @param model
	 * @return 0:链接成功;1:加载驱动失败;2:注册驱动失败;3:用户名或密码错误;4:链接地址错误
	 */
	public int testMySQLCon(SysExdbsrcCenter model,String sql){
		int code=0;
		try {
			Class.forName(model.getDriverName());
		} catch (ClassNotFoundException e) {
			logger.error(e);
			code=1;
		}
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		}catch (SQLException e) {logger.error(e);
				code=2;
		}//注册MySQL驱动	
		Connection con=null;
		PreparedStatement stmt=null;
		try {
			
			con=DriverManager.getConnection(model.getDsrcUrl(),model.getUsername(),model.getPassword());
			if("".equals(sql)){}
			else{
				stmt = con.prepareStatement(sql);
				stmt.execute();
				stmt.close();
			}
			con.close();
		} catch (SQLException e) {
			logger.error(e);
			if(e.getErrorCode()==1045){
				code=3;
			}
			else
			{
				code=4;
			}
			
		}finally {
			DBUtil.close(con, stmt, null);
		}
		return code;
	}
	/**
	 * 测试Microsoft SQL Server2000链接
	 * @param model
	 * @return 0:链接成功;1:加载驱动失败;2:注册驱动失败;3:用户名或密码错误;4:链接地址错误
	 */
	public int testMssCon(SysExdbsrcCenter model,String sql){
		int code=0;
		try {
			Class.forName(model.getDriverName());
		} catch (ClassNotFoundException e) {logger.error(e);
			code=1;
		}
		if(model.getDsrcType().endsWith("2000")){
			  try {
				  DriverManager.registerDriver(new com.microsoft.jdbc.sqlserver.SQLServerDriver());
		      } catch (SQLException e) {logger.error(e);
				  code=2;
		      }//注册SQL Server2000驱动			
		}
		else if(model.getDsrcType().endsWith("2005/2008")){
			 try {
				   DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
		     } catch (SQLException e) {logger.error(e);
				   code=2;
		     }//注册SQL Server2005/2008驱动
		}
		Connection con=null;
		PreparedStatement stmt=null;
		try {
			
			con=DriverManager.getConnection(model.getDsrcUrl(),model.getUsername(),model.getPassword());
			if("".equals(sql)){}
			else{
				stmt = con.prepareStatement(sql);
				stmt.execute();
				stmt.close();
			}
			con.close();
		} catch (SQLException e) {
			logger.error(e);
			if(e.getErrorCode()==18456){
				code=3;
			}
			else{
				code=4;
			}
			
		}finally {
			DBUtil.close(con, stmt, null);
		}		
		return code;
	}

}
