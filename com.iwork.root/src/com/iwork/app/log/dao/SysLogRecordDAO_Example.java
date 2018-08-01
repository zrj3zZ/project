package com.iwork.app.log.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.log.model.SysLogRecord;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysLogRecord entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.iwork.app.log.model.SysLogRecord
 * @author MyEclipse Persistence Tools
 */

public class SysLogRecordDAO_Example extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SysLogRecordDAO_Example.class);
	// property constants
	public static final String TABLE_NAME = "tableName";
	public static final String FUNCTION_NAME = "functionName";
	public static final String DATA_PK = "dataPk";
	public static final String CREATE_USER = "createUser";
	public static final String OPERATE_TYPE = "operateType";
	public static final String LOG_TYPE = "logType";
	public static final String LOG_TEXT = "logText";
	public static final String STANDBY1 = "standby1";
	public static final String STANDBY2 = "standby2";
	public static final String STANDBY3 = "standby3";
	public static final String STANDBY4 = "standby4";
	public static final String STANDBY5 = "standby5";

	public void save(SysLogRecord transientInstance) {
		log.debug("saving SysLogRecord instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysLogRecord persistentInstance) {
		log.debug("deleting SysLogRecord instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysLogRecord findById(java.lang.String id) {
		log.debug("getting SysLogRecord instance with id: " + id);
		try {
			SysLogRecord instance = (SysLogRecord) getSession().get(
					"com.iwork.app.log.model.SysLogRecord", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SysLogRecord instance) {
		log.debug("finding SysLogRecord instance by example");
		try {
			List results = getSession().createCriteria(
					"com.iwork.app.log.model.SysLogRecord").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding SysLogRecord instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SysLogRecord as model where model."+propertyName+"= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTableName(Object tableName) {
		return findByProperty(TABLE_NAME, tableName);
	}

	public List findByFunctionName(Object functionName) {
		return findByProperty(FUNCTION_NAME, functionName);
	}

	public List findByDataPk(Object dataPk) {
		return findByProperty(DATA_PK, dataPk);
	}

	public List findByCreateUser(Object createUser) {
		return findByProperty(CREATE_USER, createUser);
	}

	public List findByOperateType(Object operateType) {
		return findByProperty(OPERATE_TYPE, operateType);
	}

	public List findByLogType(Object logType) {
		return findByProperty(LOG_TYPE, logType);
	}

	public List findByLogText(Object logText) {
		return findByProperty(LOG_TEXT, logText);
	}

	public List findByStandby1(Object standby1) {
		return findByProperty(STANDBY1, standby1);
	}

	public List findByStandby2(Object standby2) {
		return findByProperty(STANDBY2, standby2);
	}

	public List findByStandby3(Object standby3) {
		return findByProperty(STANDBY3, standby3);
	}

	public List findByStandby4(Object standby4) {
		return findByProperty(STANDBY4, standby4);
	}

	public List findByStandby5(Object standby5) {
		return findByProperty(STANDBY5, standby5);
	}

	public List findAll() {
		log.debug("finding all SysLogRecord instances");
		try {
			String queryString = "from SysLogRecord";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysLogRecord merge(SysLogRecord detachedInstance) {
		log.debug("merging SysLogRecord instance");
		try {
			SysLogRecord result = (SysLogRecord) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysLogRecord instance) {
		log.debug("attaching dirty SysLogRecord instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysLogRecord instance) {
		log.debug("attaching clean SysLogRecord instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}
