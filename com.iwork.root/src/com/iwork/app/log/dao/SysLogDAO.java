package com.iwork.app.log.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.log.common.LogConstant;
import com.iwork.app.log.model.SysLogRecord;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;

/**
 * 操作日志数据操作类
 * @author LuoChuan
 *
 */
public class SysLogDAO extends HibernateDaoSupport implements ISysLogDAO,Runnable {
	
	private static final Log log = LogFactory.getLog(SysLogDAO.class);
	private SessionFactory sessionFactory;
	
	public SysLogDAO() {
		super();
		sessionFactory = (SessionFactory)SpringBeanUtil.getBean("sessionFactory");
		this.setHibernateTemplate(new HibernateTemplate(sessionFactory));
	}


	public void addOperationLog(String functionName, String tableName, String operateType, String dataPk, String logText) {
		this.addOperationLog(functionName, tableName, operateType, dataPk, logText, null, null, null, null, null);
	}

	public void addOperationLog(String functionName, String tableName, String operateType, String dataPk, Object model) {
		this.addOperationLog(functionName, tableName, operateType, dataPk, model, null, null, null, null, null);
	}
	
	public void addOperationLog(String functionName, String tableName, String operateType, String dataPk, String logText, 
								String extend1, String extend2, String extend3, String extend4, String extend5) {
		this.addLog(functionName, tableName, LogConstant.OPERATIONLOG, operateType, dataPk, logText, extend1, extend2, extend3, extend4, extend5);
	}

	public void addOperationLog(String functionName, String tableName, String operateType, String dataPk, Object model, 
								String extend1, String extend2, String extend3, String extend4, String extend5) {

		JSONArray json = JSONArray.fromObject(model);
		this.addOperationLog(functionName, tableName, operateType, dataPk, json.toString(), extend1, extend2, extend3, extend4, extend5);
	}

	public void addErrorLog(String logText) {
		this.addErrorLog(logText, null, null, null, null, null);
	}
	
	public void addErrorLog(String logText, String extend1,String extend2,String extend3,String extend4,String extend5) {
		this.addLog(null, null, LogConstant.ERRORLOG, null, null, logText, extend1, extend2, extend3, extend4, extend5);
	}
	
	public void addLog(String functionName, String tableName, String logType, String operateType, String dataPk, String logText, 
								String extend1, String extend2, String extend3, String extend4, String extend5) {
		
		String recordLog = "";//系统设置是否记录日志的表示
		UserContext uc =UserContextUtil.getInstance().getCurrentUserContext();
		String userId = "";
		String userName = "";
		if(uc!=null){
			 userId = uc.get_userModel().getUserid();
			 userName = uc.get_userModel().getUsername();
		}
		if(userId.equals("")){
			userId = "SYS";
		}
		if(userName.equals("")){
			userName = "系统";
		}
		String currentDateTime = UtilDate.getNowDatetime();
		if (LogConstant.LOGOFF.equals(recordLog)) {//系统设定不记录日志时,直接返回
			return;
		}
		Thread t = new Thread();
		t.start();
		
//		for (int i=0;i<tableName.length;i++) {
		SysLogRecord instance = new SysLogRecord();
		//instance.setLogId(String.valueOf(SequenceUtil.getInstance().getSequenceIndex(LogConstant.SYS_LOG_RECORD)));
		instance.setLogText(logText);
		instance.setCreateUser(userId+"<"+userName+">");
		instance.setCreateTime(currentDateTime);
		Long tmp = new Long(0);
		try{
			tmp = Long.valueOf(logType);
		}catch(Exception e){logger.error(e,e);}
		instance.setLogType(tmp); 
		if (null != functionName && !"".equals(functionName)) {
			instance.setFunctionName(functionName);
		}
		if (null != tableName && !"".equals(tableName)) {
			instance.setTableName(tableName);
		}
		if (null != operateType && !"".equals(operateType)) {
			instance.setOperateType(Long.valueOf(operateType));
		}
		if (null != dataPk && !"".equals(dataPk)) {
			instance.setDataPk(dataPk);
		}
		if (null != extend1 && !"".equals(extend1)) {
			instance.setStandby1(extend1);
		}
		if (null != extend2 && !"".equals(extend2)) {
			instance.setStandby1(extend2);
		}
		if (null != extend3 && !"".equals(extend3)) {
			instance.setStandby1(extend3);
		}
		if (null != extend4 && !"".equals(extend4)) {
			instance.setStandby1(extend4);
		}
		if (null != extend5 && !"".equals(extend5)) {
			instance.setStandby1(extend5);
		}

		this.save(instance);
//		}
		if(t.isAlive()) {
			t.stop();
		}
	}

	/**
	 * 根据ID删除日志表中的一条记录
	 * @param id					主键
	 */
	public void deleteLog(Long id) {
		
		SysLogRecord instance = this.findById(id);
		this.deleteLog(instance);
	}

	/**
	 * 删除SysLogRecord实体Bean
	 * @param persistentInstance	实体bean
	 */
	public void deleteLog(SysLogRecord persistentInstance) {
		log.debug("deleting SysLogRecord instance");
		try {
			this.getHibernateTemplate().delete(persistentInstance);
		} catch (RuntimeException e) {
			logger.error(e,e);			
		}
	}

	/**
	 * 获得全部数据列表
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List findAll() {
		log.debug("finding all SysLogRecord instances");
		try {
			StringBuffer queryStrB = new StringBuffer("FROM ").append(SysLogRecord.DATABASE_ENTITY);
			Query queryObject = getSession().createQuery(queryStrB.toString());
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/**
	 * 获得某一种类型的日志数据
	 * @param type		日志分为操作日志、错误日志两种类型，"0"代表操作日志,"1"代表错误日志
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List findByType(String type) {
		log.debug("finding SysLogRecord instances with type: " + type);
		try {
			StringBuffer queryStrB = new StringBuffer("FROM ").append(SysLogRecord.DATABASE_ENTITY).append(" WHERE LOGTYPE = ?");
			Query queryObject = getSession().createQuery(queryStrB.toString());
			DBUtilInjection d=new DBUtilInjection();
			List l=new ArrayList();
			if(type!=null&&!"".equals(type)){
			if(d.HasInjectionData(type)){
				return l;
			}
			queryObject.setString(0, type);
			}else{
				queryObject.setString(0, type);
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by type failed", re);
			throw re;
		}
	}
	
	/**
	 * 根据条件获得数据列表
	 * @param operateUser			操作人
	 * @param operateDateStart		查询条件操作日期的开始时间
	 * @param operateDateEnd		查询条件操作日期的结束时间
	 * @param functionName			功能名称
	 * @param operateType			操作类型
	 * @param tableName				操作表名
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List findByCondition(String operateUser, String operateDateStart,
			String operateDateEnd, String functionName, String operateType,
			String tableName, String type) {
		return this.findByConditionWithPage(operateUser, operateDateStart, operateDateEnd, functionName, operateType, tableName, type, null, null);
	}

	/**
	 * 根据条件获得数据列表分页显示
	 * @param operateUser			操作人
	 * @param operateDateStart		查询条件操作日期的开始时间
	 * @param operateDateEnd		查询条件操作日期的结束时间
	 * @param functionName			功能名称
	 * @param operateType			操作类型
	 * @param tableName				操作表名
	 * @param type					日志类型
	 * @param pageSize				页数
	 * @param startRow				分页开始行数
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List findByConditionWithPage(String operateUser, String operateDateStart,
			String operateDateEnd, String functionName, String operateType,
			String tableName, String type, String pageSize, String startRow) {
		log.debug("finding SysLogRecord instance with condition and page");
		try {
			StringBuffer sql = new StringBuffer("FROM ").append(SysLogRecord.DATABASE_ENTITY).append(" WHERE 1=1");
			if (null != operateUser && !"".equals(operateUser)) {
				sql.append(" AND CREATEUSER LIKE ?");
			}
			if (null != functionName && !"".equals(functionName)) {
				sql.append(" AND FUNCTIONNAME LIKE ?");
			}
			if (LogConstant.OPERATIONLOG.equals(type)) {//操作日志才有以下两个参数
				if (null != operateType && !"".equals(operateType)) {
					sql.append(" AND OPERATETYPE = ?");
				}
				if (null != tableName && !"".equals(tableName)) {
					sql.append(" AND TABLENAME LIKE ?");
				}
			}
			if (null != operateDateStart && !"".equals(operateDateStart)) {
				sql.append(" AND CREATETIME >=?");
			}
			if (null != operateDateEnd && !"".equals(operateDateEnd)) {
				sql.append(" AND CREATETIME <=? ");
			}
			if (null != type && !"".equals(type)) {
				sql.append(" AND LOGTYPE = ?");
			}
			sql.append(" ORDER BY CREATETIME");
			
			final String sql1=sql.toString();
			final String final_operateUser = operateUser;
			final String final_functionName = functionName;
			final String final_type = type;
			final String final_operateType = operateType;
			final String final_tableName = tableName;
			final String final_operateDateStart = operateDateStart;
			final String final_operateDateEnd = operateDateEnd;
			return this.getHibernateTemplate().executeFind(new HibernateCallback(){
				public Object doInHibernate(org.hibernate.Session session)
				throws HibernateException, SQLException {
					// TODO 自动生成方法存根
					DBUtilInjection d=new DBUtilInjection();
					Query query=session.createQuery(sql1);
					int i = 0;
					List l=new ArrayList();
					if(final_operateUser!=null&&!final_operateUser.equals("")){
						if(d.HasInjectionData(final_operateUser)){
							return l;
						}
						query.setString(i, "%"+final_operateUser+"%");i++;
					}
					if(final_functionName!=null&&!final_functionName.equals("")){
						if(d.HasInjectionData(final_functionName)){
							return l;
						}
						query.setString(i, "%"+final_functionName+"%");i++;
					}
					if (LogConstant.OPERATIONLOG.equals(final_type)) {//操作日志才有以下两个参数
						if(final_operateType!=null&&!final_operateType.equals("")){
							if(d.HasInjectionData(final_operateType)){
								return l;
							}
							query.setString(i, final_operateType);i++;
						}
						if(final_tableName!=null&&!final_tableName.equals("")){
							if(d.HasInjectionData(final_tableName)){
								return l;
							}
							query.setString(i, "%"+final_tableName+"%");i++;
						}
					}
					if (null != final_operateDateStart && !"".equals(final_operateDateStart)){ 
						if(d.HasInjectionData(final_operateDateStart)){
							return l;
						}
						query.setDate(i,UtilDate.StringToDate(final_operateDateStart, "yyyy-MM-dd hh:mm:ss"));i++;
					}
					if (null != final_operateDateEnd && !"".equals(final_operateDateEnd)){ 
						if(d.HasInjectionData(final_operateDateEnd)){
							return l;
						}
						query.setDate(i,UtilDate.StringToDate(final_operateDateEnd, "yyyy-MM-dd hh:mm:ss"));i++;
					}
					if(final_type!=null&&!final_type.equals("")){
						if(d.HasInjectionData(final_type)){
							return l;
						}
						query.setString(i, final_type);i++;
					}
					return query.list();
				}
			});
			
		} catch (RuntimeException re) {
			log.error("find by condition failed", re);
			throw re;
		}
	}
	
	/**
	 * 根据id取得日志实例
	 * @param id
	 * @return SysLogRecord bean
	 */
	public SysLogRecord findById(Long id) {
		log.debug("getting SysLogRecord instance with id: " + id);
		try {
			SysLogRecord instance = (SysLogRecord) this.getHibernateTemplate().get(SysLogRecord.DATABASE_ENTITY_PACKAGE_PATH, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * 保存日志
	 * @param transientInstance
	 */
	private void save(SysLogRecord transientInstance) {
		log.debug("saving SysLogRecord instance");
		try {
			this.getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void run() {
		// TODO Auto-generated method stub
	}

}
