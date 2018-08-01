package com.iwork.app.persion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;

public class SysPersonDAO extends HibernateDaoSupport{
	/**
	 * 获取指定用户的配置
	 * @param userid
	 * @param condition//查询条件  
	 * @return
	 */
	public List<SysPersonConfig> getUserConfigList(String userid,String[] condition){
		ArrayList<SysPersonConfig> configList = new ArrayList<SysPersonConfig>();
		int l = condition.length;
		if(l>0){
			for(int i=0;i<l;i++){
				Session session=getSessionFactory().openSession();
				Criteria criteria=session.createCriteria(SysPersonConfig.class);
				criteria.add(Restrictions.eq("userid", userid));
				criteria.add(Restrictions.eq("type", condition[i]));
				List<SysPersonConfig> list = criteria.list();
				session.close();
				if(list!=null&&list.size()>0){
					SysPersonConfig temp = list.get(0);
					configList.add(temp);
				}
			}
		}
		return configList;
	}
	
	/**
	 * 此方法用于获取数据库中当前用户的密码和加密对象
	 * 使用此办法为了与通过框架获取得知作区分
	 */
	public Map getPassword$Extend3(String userid) {
		Map map = new HashMap();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("SELECT * FROM ORGUSER WHERE USERID=?");
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, userid);
			rs = ps.executeQuery();
			while(rs.next()){
				map.put("extend3",rs.getString("extend3"));
				map.put("password",rs.getString("password"));
			}
		} catch (Exception e) {
			
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return map;
	}
	
	/**
	 * 获得用户配置信息
	 * @param userid
	 * @param type
	 * @return
	 */
	public SysPersonConfig getUserConfig(String userid,String type){
		String sql = "From SysPersonConfig where userid = ? and type = ? ";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return null;
			}
		}
		if(type!=null&&!"".equals(type)){
			if(d.HasInjectionData(type)){
				return null;
			}
		}
		Object[] value = {userid,type};
		SysPersonConfig temp = null;
				List<SysPersonConfig> list =this.getHibernateTemplate().find(sql,value);
				if(list!=null&&list.size()>0){
					 temp = list.get(0);
				}
		return temp;
	}
	
	
	/**
	 * 删除单条记录
	 * @param id
	 * @return
	 */
	public void delBoData(SysPersonConfig model){
		this.getHibernateTemplate().delete(model);
	}
	
	/**
	 * 获得单条数据
	 * @param id
	 * @return
	 */
	public SysPersonConfig getBoData(Long id){
		SysPersonConfig model = (SysPersonConfig) this.getHibernateTemplate().get(SysPersonConfig.class, id);
		return model;
	}
	/**
	 * 更新
	 * @param model
	 */
	public void updateBoData(SysPersonConfig model){
		this.getHibernateTemplate().update(model);
	}
	/**
	 * 插入
	 * @param model
	 */
	public void addBoData(SysPersonConfig model) {
		this.getHibernateTemplate().save(model);
	}
}
