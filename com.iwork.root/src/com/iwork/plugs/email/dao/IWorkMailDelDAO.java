package com.iwork.plugs.email.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.util.SequenceUtil;
import com.iwork.plugs.email.model.MailDelModel;

/**
 * 邮件删除DAO
 * @author zouyalei
 *
 */
public class IWorkMailDelDAO extends HibernateDaoSupport{

	private static final String SEQUENCE_MAILDEL = "_MAILDEL";
    public IWorkMailDelDAO(){
		
	}
    
	/**
	 * 根据当前用户名获取总记录条数
	 * @author wanglei
	 */
	public int getMailDelListSize(String userId){
		int size = 0;
    	Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			//获取总记录条数
			String sql = "select a.id,a.name,a.title,a.time,a.isstar,a.isread,a.bindid from ( select o.id as id,o.owner as name,o.title as title,o.create_time as time,o.is_star as isstar,o.is_archives as isread,o.bind_id as bindId from iwork_mail_owner o where o.owner=? and o.id in (select d.task_id as id from iwork_mail_del d where d.type='-1') union all select t.id as id,t.mail_form as name,t.title as title,t.create_time as time,t.is_star as isstar,t.is_read as isread,t.bind_id as bindId from iwork_mail_task t where t.owner=? and t.id in (select e.task_id as id from iwork_mail_del e where e.type='-2') )a ";
			stat = conn.prepareStatement(sql);
			stat.setString(1, userId);
			stat.setString(2, userId);
			rset = stat.executeQuery();
			while(rset.next())    
			{    
				size++;    
			}  
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stat, rset);
		}
    	return size;
	}

    
 
    /**
     * 获取已删除
     * @author wanglei
     */
    public List<HashMap> getMailDelAllList(String userId,int pageSize,int pageNumber){
    	List<HashMap> allDelList = new ArrayList<HashMap>();
		HashMap starMap =null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from(select A.*, rownum rn from (");
			sql.append("select a.id,a.name,a.title,a.time,a.isstar,a.isread,a.bindid,a.is_reader,a.type,a.read from ( select o.id as id,o.owner as name,o.title as title,o.create_time as time,o.is_star as isstar,o.is_archives as isread,o.bind_id as bindId,1 as is_reader,-1 as type,1 as read from iwork_mail_owner o where o.owner=? and o.id in (select d.task_id as id from iwork_mail_del d where d.type='-1') union all select t.id as id,t.mail_form as name,t.title as title,t.create_time as time,t.is_star as isstar,t.is_read as isread,t.bind_id as bindId,t.is_read as is_reader,-2 as type,t.is_read as read from iwork_mail_task t where t.owner=? and t.id in (select e.task_id as id from iwork_mail_del e where e.type='-2') )a order by a.time DESC");
			sql.append(") A where rownum <= ?) where rn > ?");
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, userId);
			stat.setString(2, userId);
			stat.setInt(3, pageSize*pageNumber);
			stat.setInt(4, (pageNumber-1)*pageSize);
			rset = stat.executeQuery();
			while(rset.next()){
				starMap = new HashMap();
				starMap.put("id", rset.getLong("id"));
				starMap.put("owner", rset.getString("name"));
				starMap.put("read", rset.getString("read"));
				//截取字符串
				String str=rset.getString("title");
				if(str.length()>40){
					String title=str.substring(0,40)+"...";
					starMap.put("title", title);
				}else{
					starMap.put("title", rset.getString("title"));
				}
				//对时间进行转换
				if(rset.getString("time")!=null){
					Date createTime=UtilDate.StringToDate(rset.getString("time"),"yyyy-MM-dd HH:mm:ss");
					starMap.put("createTime", createTime);
				}else{
					starMap.put("createTime", rset.getDate("time"));
				}
				starMap.put("isstar", rset.getLong("isstar"));
				starMap.put("isread", rset.getLong("isread"));
				starMap.put("bindid", rset.getLong("bindid"));
				starMap.put("titles", rset.getString("title"));
				starMap.put("type", rset.getString("type"));
				starMap.put("isreader", rset.getString("is_reader"));
				allDelList.add(starMap);  
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, stat, rset);
		}
		return allDelList;
    }
    /**
     * 根据taskid查询数据
     */
    public MailDelModel getModeByTaskId(Long taskId){
    	String sql = "FROM MailDelModel where task_id='" + taskId + "'";
    	MailDelModel mailDelModel = new MailDelModel();
		List<MailDelModel> list = this.getHibernateTemplate().find(sql);
		for (MailDelModel mailDelModels : list) {
			mailDelModel = mailDelModels;
		}
		return mailDelModel;
    }
    /**
	 * 获取单条信息
	 * @param id
	 * @return
	 */
	public MailDelModel getBoData(Long id) {
		MailDelModel model = (MailDelModel)this.getHibernateTemplate().get(MailDelModel.class,id);			
		return model;
	}
	
	/**
	 * 修改
	 * @param model
	 */
	public void updateBoData(MailDelModel model) {
		this.getHibernateTemplate().update(model);
	}
	
	/**
	 * 添加
	 * @param model
	 */
	public void addBoData(MailDelModel model) {
		model.setId(this.getSequenceValue());
		this.getHibernateTemplate().save(model);
	}
	
	/**
	 * 删除
	 * @param model
	 */
	public void deleteBoData(MailDelModel model) {
		this.getHibernateTemplate().delete(model);	
	}
	
	/**
	 * ID生成
	 * @return
	 */
	public Long getSequenceValue(){
		int id = SequenceUtil.getInstance().getSequenceIndex(SEQUENCE_MAILDEL);
		return new Long(id);
	}
	
	/**
	 * 获取所有
	 * @return
	 */
	public List<MailDelModel> getDelModelAll(){
		String sql="FROM MailDelModel";
		return getHibernateTemplate().find(sql);
	}
}
