package com.iwork.plugs.resoucebook.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.db.DBUtil;
import com.iwork.plugs.resoucebook.model.IworkRmBase;
import com.iwork.plugs.resoucebook.model.IworkRmSpace;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;
import com.iwork.plugs.resoucebook.service.SpaceManageService;

public class SpaceManageDao  extends HibernateDaoSupport{
	SpaceManageService spaceManageService=new SpaceManageService();
	public SpaceManageDao(){
		
	}
	
	/**
	 * 查询空间的周期,车辆预定的id=1
	 */
	public int getLength(long id){
		String sql="FROM IworkRmSpace where id=?";
		Object[] values = {id};
		List list = this.getHibernateTemplate().find(sql,values);
		int rcycle=0;
		if(list.size()>0){
			IworkRmSpace iworkRmSpace = (IworkRmSpace)list.get(0);
			long cycle= iworkRmSpace.getCycle();
			rcycle=Integer.parseInt(String.valueOf(cycle));
		}
		return rcycle;
	}
	/**
	 * 获得空间列表
	 * @return
	 */

	public List getSpaceList(String userid){
		String sql=""; 
		Object[] values = null;
		if(userid==null||"".equals(userid)){ 
		   sql = " FROM " + IworkRmSpace.DATABASE_ENTITY + " order by id";
		   values = new Object[0];
		}else{
			DBUtilInjection d=new DBUtilInjection();
		    List lis=new ArrayList(); 
		    if ((userid != null) && (!"".equals(userid))) {
	        	if(d.HasInjectionData(userid)){
    				return lis;
    			}
	         
	        }
			sql= " FROM " + IworkRmSpace.DATABASE_ENTITY+" where manager=? order by id";
			values = new Object[1];
			values[0] = userid;
		}
		List list = this.getHibernateTemplate().find(sql,values);
		return list;	
	}
	
//	public List getSpaceList(){
//		Session session = getSessionFactory().openSession();
//		Criteria criteria = session.createCriteria(IworkRmSpace.class);
//		List list = criteria.list();
//		return list;
//	}
	
	/**
	 * 空间名称是否重复
	 * @return
	 */
	public String checkSpace(String space){	
		String rvalue="";
		String sql = " FROM " + IworkRmSpace.DATABASE_ENTITY +" where spacename=?";
		DBUtilInjection d=new DBUtilInjection();
	    
	    if ((space != null) && (!"".equals(space.trim()))) {
        	if(d.HasInjectionData(space)){
				return rvalue;
			}
        }
		Object[] values = {space};
		List list = this.getHibernateTemplate().find(sql,values);
		int listsize=list.size();
		if(listsize!=0){
			rvalue="此空间名称已存在";
		}
		return rvalue;
	}
	/**
	 * 增加空间，保存到数据库
	 * @param rms
	 */
	public void addSpace(IworkRmSpace rms) {
		this.getHibernateTemplate().save(rms);
	}
	/*函数说明：根据id获得一条空间信息
	 * 参数说明： ID
	 * 返回值：对象
	 */
	public IworkRmSpace getSpaceModel(long id){
	String sql="FROM IworkRmSpace where id=?";
	Object[] values = {id};
	List list = this.getHibernateTemplate().find(sql,values);
	if(list.size()>0){
		IworkRmSpace iworkRmSpace = (IworkRmSpace)list.get(0);
		return iworkRmSpace;
	}else{
		return null;
	}
	}

	/**
	 * 删除空间
	 * @param rms
	 */
	public void removeSpace(IworkRmSpace rms) {
		this.getHibernateTemplate().delete(rms);
	}
	/**
	 * 空间名称是否重复(修改空间)
	 * @return
	 */
	public String checkSpace2(long id,String space){	
		String rvalue="";
		String sql = " FROM " + IworkRmSpace.DATABASE_ENTITY +" where spacename=? and id<>?";
		DBUtilInjection d=new DBUtilInjection();
	    
	    if ((space != null) && (!"".equals(space.trim()))) {
        	if(d.HasInjectionData(space)){
				return rvalue;
			}
        }
		Object[] values = {space,id};
		List list = this.getHibernateTemplate().find(sql,values);
		int listsize=list.size();
		if(listsize!=0){
			rvalue="此空间名称已存在";
		}
		return rvalue;
	}
	/**
	 * 修改空间
	 * @param rms
	 */
	public void updateSpace(IworkRmSpace rms) {
		this.getHibernateTemplate().update(rms);
	}
	/**
	 * 内容管理，内容显示列表
	 * @return
	 */
	public List getContentList(long id){
		String sql = " FROM " + IworkRmWeb.DATABASE_ENTITY +" where spaceid=?";
		Object[] values = {id};
		List list = this.getHibernateTemplate().find(sql,values);
		return list;	
	}
	
	/*函数说明：根据id获得一条空间记录信息（内容管理）
	 * 参数说明： ID
	 * 返回值：对象
	 */
	public IworkRmWeb getContentModel(long id){
	String sql="FROM IworkRmWeb where id=?";
	Object[] values = {id};
	List list = this.getHibernateTemplate().find(sql,values);
	if(list.size()>0){
		IworkRmWeb iworkRmWeb = (IworkRmWeb)list.get(0);
		return iworkRmWeb;
	}else{
		return null;
	}
	}
	/**
	 * 修改空间记录
	 * @param rms
	 */
	public void updateContent(IworkRmWeb rmw) {
		this.getHibernateTemplate().update(rmw);
	}
	/**
	 * 删除记录
	 * @param rms
	 */
	public void removeContents(IworkRmWeb rms) {
		this.getHibernateTemplate().delete(rms);
	}
	/**
	 * 基础信息，内容显示列表
	 * @return
	 */
	public List getBaseList(long id){
		String sql = " FROM " + IworkRmBase.DATABASE_ENTITY +" where spaceid=?";
		Object[] values = {id};
		List list = this.getHibernateTemplate().find(sql,values);
		return list;	
	}
	/**
	 * 检查同一空间中资源编号是否重复
	 * @return
	 */
	public String checkBase(long spaceid,String resouceid){	
		String rvalue="";
		String sql = " FROM " + IworkRmBase.DATABASE_ENTITY +" where spaceid=? and resouceid=?";
		Object[] values = {spaceid,resouceid};
		List list = this.getHibernateTemplate().find(sql,values);
		int listsize=list.size();
		if(listsize!=0){
			rvalue="此资源编号已存在";
		}
		return rvalue;
	}
	/**
	 * 增加基础信息，保存到数据库
	 * @param rms
	 */
	public void addBase(IworkRmBase rmb) {
		this.getHibernateTemplate().save(rmb);
	}
	/*函数说明：根据id获得一条基础信息（基础信息）
	 * 参数说明： ID
	 * 返回值：对象
	 */
	public IworkRmBase getBaseModel(long id){
	String sql="FROM IworkRmBase where id=?";
	Object[] values = {id};
	List list = this.getHibernateTemplate().find(sql,values);
	if(list.size()>0){
		IworkRmBase iworkRmBase = (IworkRmBase)list.get(0);
		return iworkRmBase;
	}else{
		return null;
	}
	}
	/**
	 * 删除基础信息
	 * @param rms
	 */
	public void removeBases(IworkRmBase rmb) {
		this.getHibernateTemplate().delete(rmb);
	}
	

	/**
	 * 检查同一空间中资源编号是否重复（基础信息修改）
	 * @return
	 */
	public String checkBase2(long id,long spaceid,String resouceid){	
		String rvalue="";
		String sql = " FROM " + IworkRmBase.DATABASE_ENTITY +" where spaceid=? and resouceid=? and id<>?";
		DBUtilInjection d=new DBUtilInjection();
	    
	    if ((resouceid != null) && (!"".equals(resouceid))) {
        	if(d.HasInjectionData(resouceid)){
				return rvalue;
			}
        }
		Object[] values = {spaceid,resouceid,id};
		List list = this.getHibernateTemplate().find(sql,values);
		int listsize=list.size();
		if(listsize!=0){
			rvalue="此资源编号已存在";
		}
		return rvalue;
	}
	/**
	 * 修改基础信息
	 * @param rms
	 */
	public void updateBase(IworkRmBase rmw) {
		this.getHibernateTemplate().update(rmw);
	}
	/**
	 * 得到基本信息列表
	 * @return
	 */
public List<IworkRmBase> getSpaceList(Long spaceId){
	String sql = "From IworkRmBase where status = '1' and spaceid = ?";//增加status = '1' 来判断是否停用
	Object[] values = {spaceId};
	List<IworkRmBase> list= this.getHibernateTemplate().find(sql,values);
	return list;
}

/**
 * 判断资源预定状态,是否有预定记录
 * @param day
 * @return
 */
public List isReserveStatus(Long spaceId,String resouceid,String day){
		List<HashMap> allDelList = new ArrayList<HashMap>();
		List param = new ArrayList();
		HashMap starMap =null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			StringBuffer sql= new StringBuffer();
			sql.append("select * FROM Iwork_Rm_Web where spaceid = ? and  resouceid = ? and status = '1'");
			param.add(spaceId);
			param.add(resouceid);
			if (day != null && !"".equals(day)) { 
				String begin=day+" 00:00:00";
				String end=day+" 23:59:59";
					sql.append(" and  begintime BETWEEN to_date(?,'yyyy-MM-dd HH24:MI:SS') and to_date (?,'yyyy-MM-dd HH24:MI:SS') ORDER BY id desc");
					param.add(begin);
					param.add(end);
			}
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < param.size(); i++) {
				ps.setObject(i+1, param.get(i));
			}
			rset = ps.executeQuery();
			while(rset.next()){
				starMap = new HashMap();
				starMap.put("SPACEID", rset.getLong("SPACEID"));
				starMap.put("SPACENAME", rset.getString("SPACENAME"));
				starMap.put("RESOUCEID", rset.getString("RESOUCEID"));
				starMap.put("RESOUCE", rset.getString("RESOUCE"));
				starMap.put("USERNAME", rset.getString("USERNAME"));
				starMap.put("USERID", rset.getString("USERID"));
				starMap.put("BEGINTIME", rset.getString("BEGINTIME"));
				starMap.put("ENDTIME", rset.getString("ENDTIME"));
				starMap.put("STATUS", rset.getString("STATUS"));
				starMap.put("MEMO", rset.getString("MEMO"));
				
				allDelList.add(starMap);  
			}
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rset);
		}
		return allDelList;	
}
/**
 * 车辆预定，保存到数据库
 * @param rms
 */
public void addWeb(IworkRmWeb rmb) {
	this.getHibernateTemplate().save(rmb);
}
/**
 * 资源预定信息查询
 * @return
 */
public List getWebList(String manager,long spaceid,String spacename,String resouceid,String resoucename,String userid,String username,String date,long status){
	List param=new ArrayList();
	StringBuffer sql= new StringBuffer(); 
	//判断spaceid的manager是不是admin
	String manager1=this.getManager(spaceid);
	if(manager.equals("admin")||manager.equals(manager)){
	   sql.append( " FROM IworkRmWeb where userid!=' '");
	}else{
		sql.append(" FROM IworkRmWeb where userid=?");
		param.add(userid);
	}
	if(spaceid!=0&& !"".equals(spaceid)){
		sql.append(" and spaceid  like ?");
		param.add(spaceid);
	}
	if(spacename!=null&&!"".equals(spacename)){
		sql.append(" and spacename=?");
		param.add(spacename);
	}
	if(resouceid!=null&& !"".equals(resouceid)){
		sql.append(" and resouceid=?");
		param.add(resouceid);
	}
	if(resoucename!=null&& !"".equals(resoucename)){
		sql.append(" and resouce=?");
		param.add(resoucename);
	}
	if(userid!=null&& !"".equals(userid)){
		sql.append(" and userid=?");
		param.add(userid);
	}
	if(username!=null&& !"".equals(username)){
		sql.append(" and username=?");
		param.add(username);
	}
	if(date!=null&& !"".equals(date)){
		String begin=date+" 00:00:00";
		String end=date+" 23:59:59";
			sql.append(" and  begintime BETWEEN to_date(?,'yyyy-MM-dd HH24:MI:SS') and to_date (?,'yyyy-MM-dd HH24:MI:SS') ORDER BY id desc");
			param.add(begin);
			param.add(end);
	}
	if(status!=0&& !"".equals(status)){
		sql.append(" and status=?");
		param.add(status);
	}
	DBUtilInjection d=new DBUtilInjection();
    List lis=new ArrayList();
	Object[] values = new Object[param.size()];
	for (int i = 0; i < param.size(); i++) {
		values[i] = param.get(i);
		 if ((values[i] != null) && (!"".equals(values[i]))) {
	        	if(d.HasInjectionData(values[i].toString())){
 				return lis;
 			}
	          
	        }
	}
	List list = this.getHibernateTemplate().find(sql.toString(),values);
	return list;	
}
/**
 * 根据spaceid得到manager
 * @param spaceid
 * @return
 */
public String getManager(long spaceid){
	String rmanager="";
	Session session = getSessionFactory().openSession();
	Criteria criteria = session.createCriteria(IworkRmSpace.class);
	criteria.add(Restrictions.eq("id",spaceid));
	ArrayList list1= (ArrayList)criteria.list();
	for(int i=0;i<list1.size();i++){
		IworkRmSpace web= (IworkRmSpace)list1.get(i);
		rmanager=web.getManager();
	}
	return rmanager;
}
/**
 * 根据resouceid得到spaceid
 * @param resouceid
 * @return
 */
public long getSpaceid(String resouceid){
	long rspaceid=0l;
	Session session=getSessionFactory().openSession();
	Criteria criteria=session.createCriteria(IworkRmBase.class);
	criteria.add(Restrictions.eq("resouceid",resouceid));
	ArrayList list1= (ArrayList)criteria.list();
	for(int i=0;i<list1.size();i++){
		IworkRmBase web= (IworkRmBase)list1.get(i);
		rspaceid=web.getSpaceid();
	}
	return rspaceid;	
}
/**
 * 根据resouceid得到spacename
 * @param resouceid
 * @return
 */
public String getSpacename(String resouceid){
	String rspacename="";
	Session session=getSessionFactory().openSession();
	Criteria criteria=session.createCriteria(IworkRmBase.class);
	criteria.add(Restrictions.eq("resouceid",resouceid));
	ArrayList list1= (ArrayList)criteria.list();
	for(int i=0;i<list1.size();i++){
		IworkRmBase web= (IworkRmBase)list1.get(i);
		rspacename=web.getSpacename();
	}
	return rspacename;	
}
/**
 * 根据id得到spacename
 * @param resouceid
 * @return
 */
public String getSpaceName(long spaceid){
	String rspacename="";
	Session session=getSessionFactory().openSession();
	Criteria criteria=session.createCriteria(IworkRmSpace.class);
	criteria.add(Restrictions.eq("id",spaceid));
	ArrayList list1= (ArrayList)criteria.list();
	for(int i=0;i<list1.size();i++){
		IworkRmSpace web= (IworkRmSpace)list1.get(i);
		rspacename=web.getSpacename();
	}
	return rspacename;	
}
/**
 * 根据id得到提示信息
 * @param resouceid
 * @return
 */
public String getSpaceMemo(long spaceid){
	String rmemo="";
	Session session=getSessionFactory().openSession();
	Criteria criteria=session.createCriteria(IworkRmSpace.class);
	criteria.add(Restrictions.eq("id",spaceid));
	ArrayList list1= (ArrayList)criteria.list();
	for(int i=0;i<list1.size();i++){
		IworkRmSpace web= (IworkRmSpace)list1.get(i);
		rmemo=web.getMemo();
	}
	return rmemo;	
}
}
