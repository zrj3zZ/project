package com.iwork.webservice.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.webservice.model.SysWsRuLog;
import com.iwork.webservice.model.SysWsRuParams;

public class WebServiceRuntimeDAO  extends HibernateDaoSupport{
	public WebServiceRuntimeDAO(){
		
	}
	
	
	/**
	 * 获得日志列表
	 * @param pid
	 * @return
	 */
	public List<SysWsRuLog> getLogList(int pid,Date startdate,Date enddate){
		String sql = "From SysWsRuLog  where pid=? and   createdate<=? and createdate>?  order by createdate desc ";
		final String sql1=sql.toString();
		final Integer final_pid = pid;
		final Date s=startdate;
		final Date d=enddate;
		DBUtilInjection du=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((startdate != null) && (!"".equals(startdate))) {
        	if(du.HasInjectionData(startdate.toString())){
				return lis;
			}
          
        }
	    if ((enddate != null) && (!"".equals(enddate))) {
        	if(du.HasInjectionData(enddate.toString())){
				return lis;
			}
          
        }
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				// TODO 自动生成方法存根
				Query query=session.createQuery(sql1);
				query.setInteger(0, final_pid);
				query.setDate(1,s);  
				query.setDate(2,d);
				return query.list();
			}
		});
	}
	
	
	/**
	 * 获得模型
	 * @param id
	 * @return
	 */
	public SysWsRuLog getLogModel(int id){
		return (SysWsRuLog)this.getHibernateTemplate().get(SysWsRuLog.class, id);
	}
	
	/**
	 * 获取输入输出参数
	 * @param id
	 * @param inorout
	 * @return
	 */
	public List<SysWsRuParams> getRuParamsList(int id,String inorout){
		String sql = "From SysWsRuParams where inOrOut = ? and logId=?";
		Object[] value = {inorout,id};
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList();
	    if ((inorout != null) && (!"".equals(inorout))) {
        	if(d.HasInjectionData(inorout)){
				return lis;
			}
          
        }
		return this.getHibernateTemplate().find(sql,value);
	}
	
	/**
	 * 添加运行时接口调用日志
	 * @param log
	 */
	public void saveLogInfo(SysWsRuLog log){
		this.getHibernateTemplate().save(log);
	}
	
	/**
	 * 添加或更新运行时接口调用日志
	 * @param log
	 */
	public void saveOrUpdateLogInfo(SysWsRuLog log){
		if(log.getId() == 0){
			this.getHibernateTemplate().save(log);
		}else{
			this.getHibernateTemplate().update(log);
		}
	}
	
	/**
	 *添加运行时输入输出参数
	 */
	public void saveLogParams(SysWsRuParams model){
		this.getHibernateTemplate().save(model);
	}
	
}
