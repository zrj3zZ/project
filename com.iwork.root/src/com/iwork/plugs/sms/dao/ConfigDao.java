package com.iwork.plugs.sms.dao;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.plugs.sms.bean.ConfigMst;
import com.iwork.plugs.sms.bean.LogMst;
import com.iwork.plugs.sms.service.ConfigService;

public class ConfigDao extends HibernateDaoSupport {
	ConfigService configService=new ConfigService();
	public void savelog(LogMst lm){
		this.getHibernateTemplate().save(lm);
	}
	
	/**
	 * 系统设置-数据库里取类型
	 * 
	 * @return
	 */
	public String configall() {
		Session ss = getSessionFactory().openSession();
		List<Hashtable> list1 = new ArrayList<Hashtable>();
		try{
			String sql="select distinct type from ConfigMst order by type";
			Query qq=ss.createQuery(sql);
			List list=qq.list();
			int j=list.size();
			for(int i=0;i<list.size();i++){
				Hashtable ht=new Hashtable();
				String type=(String)list.get(i);
				ht.put("TYPE", type);
				list1.add(ht);			
				}
	
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			ss.close();
		}
		
		String html = configService.getconfiglist(list1);
		return html;		
		
 }
	/**
	 * 系统设置-数据库里取类型(已经选定一个类型)
	 * 
	 * @return
	 */
	public String configall2(String typen) {
		Session ss = getSessionFactory().openSession();
		ArrayList<Hashtable> list1 = new ArrayList<Hashtable>();
		try{
			String sql="select distinct type from ConfigMst order by type";
			Query qq=ss.createQuery(sql);
			List list=qq.list();
			for(int i=0;i<list.size();i++){
				Hashtable ht=new Hashtable();
				String type=(String)list.get(i);
				ht.put("TYPE", type);
				list1.add(ht);			
				}
	
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			ss.close();
		}
		
		String html = configService.getconfiglist2(list1,typen);
		return html;		
		
 }
	/**
	 * 类型1的列表显示
	 * @param type1
	 * @return
	 */
	public String qtypelist(String type1){
		Session ss=getSessionFactory().openSession();
		ArrayList<Hashtable> list1 = new ArrayList<Hashtable>();
		try{
			Query qq=ss.createQuery("from ConfigMst where TYPE=? order by to_number(ckey) desc").setString(0, type1);
			ArrayList<ConfigMst> list=(ArrayList)qq.list();
			for(int i=0;i<list.size();i++){
				Hashtable ht=new Hashtable();
				ConfigMst cm=(ConfigMst)list.get(i);
				String cidb=String.valueOf(cm.getCid());
				String keyb=cm.getCkey()==null?"":cm.getCkey();
				String valueb=cm.getValue()==null?"":cm.getValue();
				ht.put("CID", cidb);
				ht.put("PARAMETERID", keyb);
				ht.put("PARAMETERNAME", valueb);
				list1.add(ht);				
			}
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			ss.close();
		}
		String html=configService.getlistc(list1);
		return html;
	}
	
	/**
	 * 新增系统参数重复性检查
	 * @return
	 */
	public int getrepeatnum(String sql,String param1,String param2){
		Session ss=getSessionFactory().openSession();
		int rvalue=0;
		try{
			Query qq=ss.createQuery(sql);
			qq.setString(0, param1);
			qq.setString(1, param2);
			ArrayList list=(ArrayList)qq.list();
			rvalue=list.size();
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			ss.close();
		}
		return rvalue;
	}
	
	/**
	 * 系统参数新增加到数据库
	 * @param userid
	 * @param typeadd
	 * @param keyadd
	 * @param valueadd
	 */
	public void addconfigDB(String typeadd, String keyadd,String valueadd ){
		ConfigMst cm=new ConfigMst();
		cm.setType(typeadd);
		cm.setCkey(keyadd);
		cm.setValue(valueadd); 
		this.getHibernateTemplate().save(cm);
	}
	/**
	 * 删除系统设置条目
	 * @param cid
	 */
	public String delnumc(String cid){
		Hashtable ht=new Hashtable();
		ht=this.qdelnum(cid);
		String typeold=(String)ht.get("type");//显示这个类别的列表
		String key=(String)ht.get("key");
		String value=(String)ht.get("value");
//		AddlogAction aa=new AddlogAction();//写入日志
//		aa.add("test", MsgConst.LOG_TYPE_MSG_CONFIG,
//				"删除配置参数 type:" + typeold + ", cid:" + cid + ", key:" + key + ", value:"
//				+ value);
		Session ss=getSessionFactory().openSession();
		Transaction tx=ss.beginTransaction();
		Query qq=ss.createQuery("delete ConfigMst where cid=?");
		qq.setParameter(0, cid);
		int ret=qq.executeUpdate();
		tx.commit();
		ss.close();
		return typeold;
	}
	/**
	 * 查询被删除条目的类型，id，value
	 * @param cid
	 * @return
	 */
	public Hashtable qdelnum(String cid){
		Hashtable ht=new Hashtable();
		Session ss=getSessionFactory().openSession();
		try{
			Query qq=ss.createQuery("from ConfigMst where cid=?");
			qq.setParameter(0, cid);
			ArrayList list=(ArrayList)qq.list();
			for(int i=0;i<list.size();i++){
				ConfigMst cm=(ConfigMst)list.get(i);
				String type=cm.getType();
				String parid=cm.getCkey();
				String par=cm.getValue();
				ht.put("type", type);
				ht.put("key", parid);
				ht.put("value", par);
			}
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			ss.close();
		}
		
		return ht;
	}
	/**
	 * 修改条目保存
	 * @param sql
	 */
	public void edit(String sql,List param){
		Session ss=getSessionFactory().openSession();
		Transaction tx=ss.beginTransaction();
		Query qq=ss.createQuery(sql);
		for (int i = 0; i < param.size(); i++) {
			qq.setParameter(i, param.get(i));
		}
		int ret=qq.executeUpdate();
		tx.commit();
		ss.close();
	}
}
