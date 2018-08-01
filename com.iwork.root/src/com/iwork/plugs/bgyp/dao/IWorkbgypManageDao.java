package com.iwork.plugs.bgyp.dao;

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
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SequenceUtil;
import com.iwork.plugs.bgyp.model.IWorkbgypModel;
import com.iwork.plugs.bgyp.model.IWorkbgypTypeModel;
import com.iwork.plugs.bgyp.model.ShopCarModel;
import com.iwork.plugs.resoucebook.model.IworkRmBase;
import com.iwork.plugs.resoucebook.model.IworkRmSpace;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;
import com.iwork.plugs.resoucebook.service.SpaceManageService;

public class IWorkbgypManageDao  extends HibernateDaoSupport{
	SpaceManageService spaceManageService=new SpaceManageService();
	public IWorkbgypManageDao(){
		
	}
	
	
/**
 * 基础信息列表
 * @param lbbh
 * @return
 */
public IWorkbgypModel getDataModel(Long id){
	IWorkbgypModel model = null;
	StringBuffer sql = new StringBuffer("select * from BD_BGYP_JCXXB where  id= ?");
	Connection conn = null;
	PreparedStatement stat = null;
	ResultSet rset = null;
	try {
		conn = DBUtil.open();
		stat = conn.prepareStatement(sql.toString());
		stat.setLong(1, id);
		rset = stat.executeQuery();
		if(rset.next()){
			model = new IWorkbgypModel();
			model.setId(rset.getLong(IWorkbgypModel.ID));
			model.setLbbh(rset.getString(IWorkbgypModel.LBBH));
			model.setLbmc(rset.getString(IWorkbgypModel.LBMC));
			model.setNo(rset.getString(IWorkbgypModel.NO));
			model.setJldw(rset.getString(IWorkbgypModel.JLDW));
			model.setGg(rset.getString(IWorkbgypModel.GG));
			model.setXh(rset.getString(IWorkbgypModel.XH));
			model.setTp(rset.getString(IWorkbgypModel.TP));
			model.setKcsl(rset.getLong(IWorkbgypModel.KCSL));
			model.setZdkc(rset.getLong(IWorkbgypModel.ZDKC));
			model.setDj(rset.getLong(IWorkbgypModel.DJ));
			model.setSfcy(rset.getString(IWorkbgypModel.SFCY));
			model.setName(rset.getString(IWorkbgypModel.NAME));
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally {
		DBUtil.close(conn, stat, rset);
	}
	return model;
}

/**
 * 基础信息列表
 * @param lbbh
 * @return
 */
public IWorkbgypModel getDataModel(String no){
	IWorkbgypModel model = null;
	StringBuffer sql = new StringBuffer("select * from BD_BGYP_JCXXB where  no=?");
	Connection conn = null;
	PreparedStatement stat = null;
	ResultSet rset = null;
	try {
		conn = DBUtil.open();
		stat = conn.prepareStatement(sql.toString());
		stat.setString(1, no);
		rset = stat.executeQuery();
		if(rset.next()){
			model = new IWorkbgypModel();
			model.setId(rset.getLong(IWorkbgypModel.ID));
			model.setLbbh(rset.getString(IWorkbgypModel.LBBH));
			model.setLbmc(rset.getString(IWorkbgypModel.LBMC));
			model.setNo(rset.getString(IWorkbgypModel.NO));
			model.setJldw(rset.getString(IWorkbgypModel.JLDW));
			model.setGg(rset.getString(IWorkbgypModel.GG));
			model.setXh(rset.getString(IWorkbgypModel.XH));
			model.setTp(rset.getString(IWorkbgypModel.TP));
			model.setKcsl(rset.getLong(IWorkbgypModel.KCSL));
			model.setZdkc(rset.getLong(IWorkbgypModel.ZDKC));
			model.setDj(rset.getLong(IWorkbgypModel.DJ));
			model.setSfcy(rset.getString(IWorkbgypModel.SFCY));
			model.setName(rset.getString(IWorkbgypModel.NAME));
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally {
		DBUtil.close(conn, stat, rset);
	}
	return model;
}

/**
 * 基础信息列表
 * @param lbbh
 * @return
 */
public List<IWorkbgypModel> getDataList(String lbbh,String content){
	List<IWorkbgypModel> list = new ArrayList<IWorkbgypModel>();
	StringBuffer sql = new StringBuffer("select * from BD_BGYP_JCXXB where 1=1 ");
	if(lbbh!=null){
		sql.append(" and lbbh=	?");
	} 
	if(content!=null && !"".equals(content)){
		sql.append(" and name like ?");
	}
	Connection conn = null;
	PreparedStatement stat = null;
	ResultSet rset = null;
	try {
		conn = DBUtil.open();
		stat = conn.prepareStatement(sql.toString());
		int i=1;
		if(lbbh!=null){
			stat.setString(i, lbbh);i++;
		} 
		if(content!=null && !"".equals(content)){
			stat.setString(i, "%"+content+"%");
		}
		rset = stat.executeQuery();
		while(rset.next()){
			IWorkbgypModel model = new IWorkbgypModel();
			model.setId(rset.getLong(IWorkbgypModel.ID));
			model.setLbbh(rset.getString(IWorkbgypModel.LBBH));
			model.setLbmc(rset.getString(IWorkbgypModel.LBMC));
			model.setNo(rset.getString(IWorkbgypModel.NO));
			model.setJldw(rset.getString(IWorkbgypModel.JLDW));
			model.setGg(rset.getString(IWorkbgypModel.GG));
			model.setXh(rset.getString(IWorkbgypModel.XH));
			model.setTp(rset.getString(IWorkbgypModel.TP));
			model.setKcsl(rset.getLong(IWorkbgypModel.KCSL));
			model.setZdkc(rset.getLong(IWorkbgypModel.ZDKC));
			model.setDj(rset.getLong(IWorkbgypModel.DJ));
			model.setSfcy(rset.getString(IWorkbgypModel.SFCY));
			model.setName(rset.getString(IWorkbgypModel.NAME));
			list.add(model);
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally {
		DBUtil.close(conn, stat, rset);
	}
	return list;
}
/**
 *类别列表
 * @param lbbh
 * @return 
 */
public List<IWorkbgypTypeModel> getTypeDataList(String spaceId){
	List<IWorkbgypTypeModel> list = new ArrayList();
	StringBuffer sql = new StringBuffer("select * from BD_BGYP_LB ");
	if(spaceId!=null){
		sql.append(" where KJBH= ? ");
	} 
	Connection conn = null;
	PreparedStatement stat = null;
	ResultSet rset = null;
	try {
		conn = DBUtil.open();
		stat = conn.prepareStatement(sql.toString());
		if(spaceId!=null){
			stat.setString(1, spaceId);
		}
		rset = stat.executeQuery();
		while(rset.next()){
			IWorkbgypTypeModel model = new IWorkbgypTypeModel();
			model.setId(rset.getLong(IWorkbgypTypeModel.ID));
			model.setLbbh(rset.getString(IWorkbgypTypeModel.LBBH));
			model.setLbmc(rset.getString(IWorkbgypTypeModel.LBMC));
			model.setKjbh(rset.getString(IWorkbgypTypeModel.KJBH));
			model.setKjmc(rset.getString(IWorkbgypTypeModel.KJMC));
			model.setBz(rset.getString(IWorkbgypTypeModel.BZ));
			list.add(model);
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally {
		DBUtil.close(conn, stat, rset);
	}
	return list;
}


/**
 * 获得购物车当前选择列表
 * @param spaceId
 * @return
 */
public List<ShopCarModel> getCurrentSelectList(){
	String userid = UserContextUtil.getInstance().getCurrentUserId();
	List<ShopCarModel> list = new ArrayList();
	StringBuffer sql = new StringBuffer("select * from BD_BGYP_GWC ");
		sql.append(" where USERID=?");
	Connection conn = null; 
	PreparedStatement stat = null;
	ResultSet rset = null;
	try {
		conn = DBUtil.open(); 
		stat = conn.prepareStatement(sql.toString());
		stat.setString(1, userid);
		rset = stat.executeQuery();
		while(rset.next()){
			ShopCarModel model = new ShopCarModel();
			model.setId(rset.getLong(ShopCarModel.ID));
			model.setName(rset.getString(ShopCarModel.NAME));
			model.setNo(rset.getString(ShopCarModel.NO));
			model.setNum(rset.getLong(ShopCarModel.NUM));
			model.setUserid(rset.getString(ShopCarModel.USERID));
			list.add(model);
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally {
		DBUtil.close(conn, stat, rset);
	}
	return list;
}

/**
 * 获得购物车当前选择列表
 * @param spaceId
 * @return
 */
public boolean addShopCar(ShopCarModel model){
	boolean flag = false;
	String userid = UserContextUtil.getInstance().getCurrentUserId();
	List<ShopCarModel> list = new ArrayList();
	int id = SequenceUtil.getInstance().getSequenceIndex("ShopCarModel");
	StringBuffer sql = new StringBuffer();
	sql.append("insert into BD_BGYP_GWC VALUES(?,?,?,?,?,)");
	Connection conn = null; 
	PreparedStatement stat = null;
	ResultSet rset = null;
	int i =0;
	try {
		conn = DBUtil.open(); 
		stat = conn.prepareStatement(sql.toString());
		stat.setInt(1, id);
		stat.setString(2, model.getUserid());
		stat.setString(3, model.getNo());
		stat.setString(4, model.getName());
		stat.setInt(5, 1);
		rset = stat.executeQuery();
		while(rset.next()){
			i++;
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally {
		DBUtil.close(conn, stat, rset);
	}
	if(i>0){
		flag = true;
	}
	return flag;
}

/**
 * 检查购物车内物品数量
 * @param userid
 * @param no
 * @return
 */
public boolean isCheckShopCarNum(String userid,String no){
	boolean flag = false;
	StringBuffer sql = new StringBuffer();
	sql.append("select count(*) num from BD_BGYP_GWC WHERE USERID=? and NO = ?");
	Connection conn = null; 
	PreparedStatement stat = null;
	ResultSet rset = null;
	int i =0;
	try {
		conn = DBUtil.open(); 
		stat = conn.prepareStatement(sql.toString());
		stat.setString(1, userid);
		stat.setString(2, no);
		rset = stat.executeQuery();
		while(rset.next()){
			i++;
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally {
		DBUtil.close(conn, stat, rset);
	}
	if(i>0){
		flag = true;
	}
	return flag;
}
/**
 * 根据单据编号查询对应数据 
 */
public List<HashMap> selectTabByDJBH(String djbh){
	List<HashMap> list = new ArrayList<HashMap>();
	StringBuffer sql = new StringBuffer("select * from BD_BGYP_SQMXB where DJBH=?");
	Connection conn = null; 
	PreparedStatement stat = null;
	ResultSet rset = null;
	try {
		conn = DBUtil.open(); 
		stat = conn.prepareStatement(sql.toString());
		stat.setString(1, djbh);
		rset = stat.executeQuery();
		while(rset.next()){
			HashMap map = new HashMap();
			map.put("SPBH", rset.getString("SPBH"));
			map.put("SPMC", rset.getString("SPMC"));
			map.put("DW", rset.getString("DW"));
			map.put("PRICE", rset.getString("PRICE"));
			map.put("SQSL", rset.getString("SQSL"));
			map.put("HJJG", rset.getString("HJJG"));
			map.put("LBBH", rset.getString("LBBH"));
			map.put("LBMC", rset.getString("LBMC"));
			list.add(map);
		}
	} catch (Exception e) {
		logger.error(e,e);
	}finally{
		DBUtil.close(conn, stat, rset);
	}
	return list;
}
/**
 * 根据商品编号查询数据  
 */
public List<IWorkbgypModel> selectTabByLBBH(List<HashMap> list1){
	return null;
}
/**
 * 获取对应领取商品数据
 */
public HashMap getSQSL(String djbh,String no){
	return null;
}
/**
 * 根据当前用户查找对应的办公用品领用记录
 * 
 */
public List<HashMap> selectTabs(){
	return null;
}
/**
 * 获得购物车当前选择列表
 * @param spaceId
 * @return
 */
public boolean updateShopCarNum(String userid,String no){
	return true;
}
/**
 * 移除
 * @param userid
 * @param no
 * @return
 */
public boolean removeShopItem(Long id){
	boolean flag = false;
	StringBuffer sql = new StringBuffer();
	sql.append("delete from  BD_BGYP_GWC where id=").append(id);
	int i = DBUtil.executeUpdate(sql.toString());
	if(i>0){
		flag = true;
	}
	return flag;
}
/**
 * 移除
 * @param userid
 * @param no
 * @return
 */
public boolean removeShopAll(){
	boolean flag = false;
	String userid = UserContextUtil.getInstance().getCurrentUserId();
	StringBuffer sql = new StringBuffer();
	sql.append("delete from  BD_BGYP_GWC where userid='").append(userid).append("'");
	int i = DBUtil.executeUpdate(sql.toString());
	if(i>0){
		flag = true;
	}
	return flag;
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
	return null;
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
