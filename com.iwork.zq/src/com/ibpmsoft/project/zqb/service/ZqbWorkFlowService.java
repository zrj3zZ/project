package com.ibpmsoft.project.zqb.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.ibpmsoft.project.zqb.dao.ZqbWorkFlowDAO;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import org.apache.log4j.Logger;
public class ZqbWorkFlowService {
	private static Logger logger = Logger.getLogger(ZqbWorkFlowService.class);
	private ZqbWorkFlowDAO zqbWorkFlowDAO;
	private static final String ZQB_FLOW_UUID = "2b66638dc9ed42e3b6223ee6437751ff";
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	
	/**
	 * 获得当前客户列表
	 * @param pageNumber 
	 * @param pageSize 
	 * @return
	 */
	public List<HashMap> getCurrentFolwList(int pageSize, int pageNow){
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserId();
//		conditionMap.put("KHFZR", userFullName);
		List<HashMap> list1 = null;
		HashMap condition=new HashMap();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		HashMap conditionList=new HashMap();
		List<HashMap> list=new ArrayList<HashMap>();
		List<HashMap> sublist=null;
		String customername="";
		String customerno="";
		//list1 = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		if(roleid.equals("3")){//如果是董秘的话，可以看到持续督导人员添加的和自己添加的
			//if(orgUser.getUsertype().equals(new Long(2))){
				customerno = orgUser.getExtend1();
				 customername=orgUser.getExtend2();
			//} 
			 conditionMap.put("KHBH",customerno);
			 conditionMap.put("KHMC",customername);
			list1 = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
			List<HashMap> dm=new ArrayList<HashMap>();
			sublist=zqbWorkFlowDAO.getCurrentFolwList(pageSize, pageNow,userFullName);
			for(HashMap submap:sublist){
				dm.add(submap);
			}
			list.addAll(dm);
			for(HashMap map:list1){
				if(customerno.equals(map.get("KHBH"))){
				List l=new ArrayList();
				if(!"".equals(map.get("KHFZR").toString())){
					String khfzr=map.get("KHFZR").toString().substring(0, map.get("KHFZR").toString().indexOf("["));
					condition.put("CJR", khfzr);
					//sublist=DemAPI.getInstance().getList(ZQB_FLOW_UUID, condition, "CJSJ DESC");
					sublist=zqbWorkFlowDAO.getCurrentFolwList(pageSize, pageNow,khfzr);
					for(HashMap submap:sublist){
						l.add(submap);
					}
				}
				list.addAll(l);
				}
			}
			
			
		}else if(roleid.equals("5")){
			List<HashMap> ll=null;
			ll = zqbWorkFlowDAO.getCurrentFolwList(pageSize, pageNow);
			for(HashMap m:ll){
				if(m.get("EXTEND1")!=null){
					continue;
				}
				list.add(m);
			}
		}
		else{//其他人员可以看到自己负责的客户下的事务指引和自己添加的
			List<HashMap> currentlist=getCurrentCustomerList();//获取当前用户分辖的客户
			String sql="";
			List<HashMap> kk=new ArrayList<HashMap>();
			if(currentlist.size()>0){//分管的客户
			sublist=zqbWorkFlowDAO.getCurrentFolwList();
			for(HashMap submap:sublist){
				if(submap.get("EXTEND1")!=null){
					continue;
				}
				kk.add(submap);
			}
			list.addAll(kk);
				for(HashMap current:currentlist){
					if(current.get("KHFZR").toString().contains(userFullName)){//如果当前客户是
					sql="select * from orguser where extend1='"+current.get("KHBH").toString()+"'";// 
					String userid=DBUtil.getString(sql, "USERID");
					List<HashMap> l=new ArrayList<HashMap>();
					if(userid!=null&&!userid.equals("")){
							condition.put("CJR", userid);
							//sublist=DemAPI.getInstance().getList(ZQB_FLOW_UUID, condition, "CJSJ DESC");
							sublist=zqbWorkFlowDAO.getCurrentFolwList(pageSize, pageNow,userid);
							for(HashMap submap:sublist){
									l.add(submap);
							}
							list.addAll(l);
						}
					
				   }		
				
			 }
			
			}else{
				List<HashMap> ll=null;
				ll = zqbWorkFlowDAO.getCurrentFolwList(pageSize, pageNow);
				//StringBuffer sb = new StringBuffer("SELECT BOTABLE.CJR,BOTABLE.CJSJ,BOTABLE.LCSWBH,BOTABLE.TYPE,BOTABLE.SWMS,BOTABLE.ZYLC,BOTABLE.STATUS,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_FLOW_LCSWZY  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=115 and BINDTABLE.metadataid=131  ORDER BY botable.CJSJ DESC");
						//DemAPI.getInstance().getList(ZQB_FLOW_UUID, conditionList, "CJSJ DESC");
				List l=new ArrayList();
				for(HashMap m:ll){
					if(m.get("EXTEND1")!=null){
						continue;
					}
							list.add(m);
				}
			}
		}
		return list;
	}
	/**
	 * 获得当前客户列表
	 * @return
	 */
	public List<HashMap> getCurrentCustomerList(){
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserFullName();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap>	l = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		for(HashMap map:l){
			if((map.get("KHFZR")!=null&&!map.get("KHFZR").toString().equals("")&&map.get("KHFZR").toString().equals(userFullName))
					||(map.get("ZZCXDD")!=null&&!map.get("ZZCXDD").toString().equals("")&&map.get("ZZCXDD").toString().equals(userFullName))
					||(map.get("FHSPR")!=null&&!map.get("FHSPR").toString().equals("")&&map.get("FHSPR").toString().equals(userFullName))
					||(map.get("ZZSPR")!=null&&!map.get("ZZSPR").toString().equals("")&&map.get("ZZSPR").toString().equals(userFullName))){
				list.add(map);
			}
		}			
		return list;
	}
	public String getFlowJson(HashMap hash){
		String json = "";
		if(hash!=null&&hash.get("ZYLC")!=null){
			json = hash.get("ZYLC").toString();
		}
		return json;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTreeJson(){
		//基础信息表
		final String uuid = "ad87aa61-ef51-4aa1-b5c4-fbbf934b6bcd";
		List<Map> root = new ArrayList();
		
		HashMap conditionMap = new HashMap(); 
		HashMap conditionMap1 = new HashMap(); 
		conditionMap.put("TYPE", "事务指引类别");
		String customername="";
		String customerno="";
		HashMap conditionList=new HashMap();
		HashMap condition=new HashMap();
		List<HashMap> list1 = null;
		List<HashMap> sublist1=null;
		//List<HashMap> groupList=new ArrayList<HashMap>();
		String userFullName = UserContextUtil.getInstance().getCurrentUserId();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		//if(orgUser.getUsertype().equals(new Long(2))){
			customerno = orgUser.getExtend1();
			 customername=orgUser.getExtend2();
			 conditionMap1.put("KHBH",customerno);
			 conditionMap1.put("KHMC",customername);
		//} 
		
//		conditionList.put("TYPE", "事务指引类别");
//		
//		List<HashMap> ll= DemAPI.getInstance().getList(uuid, conditionList, null);
//		for(HashMap m:ll){
//			if(!groupList.contains(m)){
//			groupList.add(m);
//			}
//		}
		
		List<HashMap> groupList = DemAPI.getInstance().getList(uuid, conditionMap, null);
		for(HashMap map:groupList){
			HashMap group = new HashMap();
			group.put("id", "group"+map.get("ID"));
			group.put("name", map.get("NAME"));
			group.put("nodeType", "group");
			group.put("iconOpen", "iwork_img/package_add.png"); 
			group.put("iconClose", "iwork_img/package_delete.png");
			group.put("icon", "iwork_img/package.png");
			group.put("open",true); 
			HashMap conditionMap2 = new HashMap(); 
			List<HashMap> list=new ArrayList<HashMap>();
			/*if(roleid.equals("3")){//如果是董秘的话，可以看到持续督导人员添加的和自己添加的
				//if(orgUser.getUsertype().equals(new Long(2))){
					customerno = orgUser.getExtend1();
					 customername=orgUser.getExtend2();
				//} 
				 conditionMap.put("KHBH",customerno);
				 conditionMap.put("KHMC",customername);
				list1 = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
				for(HashMap map1:list1){
					String khfzr=map1.get("KHFZR").toString();
					condition.put("CJR", khfzr);
					sublist1= DemAPI.getInstance().getList(uuid, conditionMap, null);
					for(HashMap submap:sublist1){
						groupList.add(submap);
					}
				}
				
				
			}else{//其他人员可以看到自己负责的客户下的事务指引和自己添加的
				List<HashMap> currentlist=getCurrentCustomerList();//获取当前用户分辖的客户
				String sql="";
				for(HashMap current:currentlist){
					sql="select userid from orguser where extend1='"+current.get("KHBH").toString()+"'";
					Connection conn = DBUtil.open();
					Statement stmt = null;
					ResultSet rset = null;
					try {
						stmt = conn.createStatement();
						rset = stmt.executeQuery(sql.toString());
						List l=new ArrayList();
						while(rset.next()){
							condition.put("CJR", rset.getString("userid"));
							sublist1= DemAPI.getInstance().getList(uuid, conditionMap, null);
							for(HashMap submap:sublist1){
									if(!groupList.contains(submap)){
										groupList.add(submap);
									}
									
								
								
							}
						}
					
			} catch (Exception e) {
				logger.error(e,e);
			}finally{
				DBUtil.close(conn, stmt, rset);
			}
					
				}
				
			}*/
			conditionMap2.put("CJR", userFullName);
			conditionMap2.put("TYPE", map.get("NAME"));
			List<HashMap> lll = DemAPI.getInstance().getList(ZQB_FLOW_UUID, conditionMap2, null);
			list1 = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap1, null);
			for(HashMap m:lll){
				for (HashMap hashMap : list1) {
					if(m.get("CJR").toString().contains(userFullName)||m.get("CJR").toString().contains(hashMap.get("KHFZR").toString())){
						list.add(m);
					}
				}
			}
			List sublist = new ArrayList();
			for(HashMap data:list){
				HashMap item = new HashMap();
				item.put("id", "item"+data.get("ID"));
				item.put("name", data.get("SWMS"));
				item.put("nodeType", "item");
				item.put("iconOpen", "iwork_img/package_add.png"); 
				item.put("iconClose", "iwork_img/package_delete.png");
				item.put("icon", "iwork_img/bullet2.gif");  
				item.put("instanceid",data.get("INSTANCEID"));  
				sublist.add(item);
			}
			if(sublist!=null&&sublist.size()>0){
				group.put("children", sublist);
			}
			root.add(group);
		}
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(root);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String getTreeJson1(){
		//基础信息表
		final String uuid = "ad87aa61-ef51-4aa1-b5c4-fbbf934b6bcd";
		List<Map> root = new ArrayList();
		HashMap conditionMap = new HashMap(); 
		HashMap conditionMap1 = new HashMap(); 
		conditionMap.put("TYPE", "事务指引类别");
		String customername="";
		String customerno="";
		HashMap conditionList=new HashMap();
		HashMap condition=new HashMap();
		List<HashMap> list1 = null;
		List<HashMap> sublist1=null;
		String userFullName = UserContextUtil.getInstance().getCurrentUserId();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		List<HashMap> groupList = DemAPI.getInstance().getList(uuid, conditionMap, null);
		for(HashMap map:groupList){
			HashMap group = new HashMap();
			group.put("id", "group"+map.get("ID"));
			group.put("name", map.get("NAME"));
			group.put("nodeType", "group");
			group.put("iconOpen", "iwork_img/package_add.png"); 
			group.put("iconClose", "iwork_img/package_delete.png");
			group.put("icon", "iwork_img/package.png");
			group.put("open",true); 
			HashMap conditionMap2 = new HashMap(); 
			List<HashMap> list=new ArrayList<HashMap>();
			if(roleid.equals("3")){//如果是董秘的话，可以看到持续督导人员添加的和自己添加的
			customerno = orgUser.getExtend1();
			customername=orgUser.getExtend2();
			conditionMap1.put("KHBH",customerno);
			conditionMap1.put("KHMC",customername);
			conditionMap2.put("TYPE", map.get("NAME"));
			List<HashMap> lll = DemAPI.getInstance().getList(ZQB_FLOW_UUID, conditionMap2, null);
			list1 = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap1, null);
			for(HashMap m:lll){
				for (HashMap hashMap : list1) {
					if(m.get("CJR").toString().contains(userFullName)||hashMap.get("KHFZR").toString().contains(m.get("CJR").toString())){
						list.add(m);
					}
				}
			}
		}else if(roleid.equals("5")){
			conditionMap2.put("TYPE", map.get("NAME"));
			List<HashMap> lll = DemAPI.getInstance().getList(ZQB_FLOW_UUID, conditionMap2, null);
			String sql="";
			for(HashMap m:lll){
				sql="select * from orguser where userid='"+m.get("CJR").toString()+"'";// 
				String extend1=DBUtil.getString(sql, "extend1");
				if(extend1!=null){
					continue;
				}
						list.add(m);
			}
		}
		else{//其他人员可以看到自己负责的客户下的事务指引和自己添加的
			List<HashMap> currentlist=getCurrentCustomerList();//获取当前用户分辖的客户
			String sql="";
			if(currentlist.size()>0){//分管的客户
				conditionMap2.put("TYPE", map.get("NAME"));
				List<HashMap> lll = DemAPI.getInstance().getList(ZQB_FLOW_UUID, conditionMap2, null);
				for(HashMap m:lll){
					sql="select * from orguser where userid='"+m.get("CJR").toString()+"'";// 
					String extend1=DBUtil.getString(sql, "extend1");
					if(extend1==null){
						list.add(m);
					}else{
						for (HashMap hashMap : currentlist) {
							if(hashMap.get("KHFZR").toString().contains(userFullName)){
								sql="select * from orguser where extend1='"+hashMap.get("KHBH").toString()+"'";// 
								String userid=DBUtil.getString(sql, "USERID");
								if(m.get("CJR").toString().contains(userid)){
									list.add(m);
									continue;
								}
							}
						}
					}
				}
			}else{
				conditionMap2.put("TYPE", map.get("NAME"));
				List<HashMap> lll = DemAPI.getInstance().getList(ZQB_FLOW_UUID, conditionMap2, null);
				for(HashMap m:lll){
					sql="select * from orguser where userid='"+m.get("CJR").toString()+"'";// 
					String extend1=DBUtil.getString(sql, "extend1");
					if(extend1!=null){
						continue;
					}
						list.add(m);
				}
			}
		}
			List sublist = new ArrayList();
			for(HashMap data:list){
				HashMap item = new HashMap();
				item.put("id", "item"+data.get("ID"));
				item.put("name", data.get("SWMS"));
				item.put("nodeType", "item");
				item.put("iconOpen", "iwork_img/package_add.png"); 
				item.put("iconClose", "iwork_img/package_delete.png");
				item.put("icon", "iwork_img/bullet2.gif");  
				item.put("instanceid",data.get("INSTANCEID"));  
				sublist.add(item);
			}
			if(sublist!=null&&sublist.size()>0){
				group.put("children", sublist);
			}
			root.add(group);
		}
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(root);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 保存
	 * @param instanceid
	 * @param json
	 * @return
	 */
	public boolean  saveFlowJson(Long instanceid,String json){
		boolean flag = false;
		HashMap hash =  DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(hash!=null){
			hash.put("ZYLC", json);
			flag = DemAPI.getInstance().updateFormData(ZQB_FLOW_UUID, instanceid, hash, Long.parseLong(hash.get("ID").toString()),false);
		}
		return flag;
	}

	public int getCurrentFolwListSize(){
		HashMap conditionMap = new HashMap(); 
		String userFullName = UserContextUtil.getInstance().getCurrentUserId();
//		conditionMap.put("KHFZR", userFullName);
		List<HashMap> list1 = null;
		HashMap condition=new HashMap();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext().get_orgRole().getId();
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		HashMap conditionList=new HashMap();
		List<HashMap> list=new ArrayList<HashMap>();
		List<HashMap> sublist=null;
		String customername="";
		String customerno="";
		//list1 = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
		if(roleid.equals("3")){//如果是董秘的话，可以看到持续督导人员添加的和自己添加的
			//if(orgUser.getUsertype().equals(new Long(2))){
				customerno = orgUser.getExtend1();
				 customername=orgUser.getExtend2();
			//} 
			 conditionMap.put("KHBH",customerno);
			 conditionMap.put("KHMC",customername);
			list1 = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
			for(HashMap map:list1){
				if(customerno.equals(map.get("KHBH"))){
				List l=new ArrayList();
				String khfzr=map.get("KHFZR").toString();
				condition.put("CJR", khfzr);
				//sublist=DemAPI.getInstance().getList(ZQB_FLOW_UUID, condition, "ID DESC");
				sublist=zqbWorkFlowDAO.getCurrentFolwList();
				for(HashMap submap:sublist){
					if(submap.get("CJR").toString().equals(userFullName) || khfzr.contains(submap.get("CJR").toString())){
						l.add(submap);
					}
				}
				list.addAll(l);
				}
			}
		}else if(roleid.equals("5")){
			List<HashMap> ll=null;
			ll = zqbWorkFlowDAO.getCurrentFolwList();
			for(HashMap m:ll){
				if(m.get("EXTEND1")!=null){
					continue;
				}
				list.add(m);
			}
		}else{//其他人员可以看到自己负责的客户下的事务指引和自己添加的
			List<HashMap> currentlist=getCurrentCustomerList();//获取当前用户分辖的客户
			StringBuffer sql = new StringBuffer();
			List<HashMap> kk=new ArrayList<HashMap>();
			if(0<currentlist.size()){
				sublist=zqbWorkFlowDAO.getCurrentFolwList();
				for(HashMap submap:sublist){
					if(submap.get("EXTEND1")!=null){
						continue;
					}
					kk.add(submap);
				}
				list.addAll(kk);
			Connection conn = DBUtil.open();
			for(HashMap current:currentlist){
				if(current.get("KHFZR").toString().contains(userFullName)){
				sql.append("select userid,extend1 from orguser where extend1=?");
				PreparedStatement stmt = null;
				ResultSet rset = null;
				try {
					stmt =conn.prepareStatement(sql.toString());
					stmt.setString(1, current.get("KHBH").toString());
					rset = stmt.executeQuery();
					List l=new ArrayList();
					while(rset.next()){
						condition.put("CJR", rset.getString("userid"));
						sublist=DemAPI.getInstance().getList(ZQB_FLOW_UUID, condition, "ID DESC");
						//sublist = zqbWorkFlowDAO.getCurrentFolwList(pageSize, pageNow);
						for(HashMap submap:sublist){
							if(submap.get("CJR").toString().contains(rset.getString("userid"))){
								l.add(submap);
							}
						}
						list.addAll(l);
					}
				} catch (Exception e) {
					logger.error(e,e);
				}finally{
					DBUtil.close(conn, stmt, rset);
				}
							
				}
			}
			
			}else{
				List<HashMap> ll=null;
				//ll = zqbWorkFlowDAO.getCurrentFolwList(pageSize, pageNow);
				//StringBuffer sb = new StringBuffer("SELECT BOTABLE.CJR,BOTABLE.CJSJ,BOTABLE.LCSWBH,BOTABLE.TYPE,BOTABLE.SWMS,BOTABLE.ZYLC,BOTABLE.STATUS,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_FLOW_LCSWZY  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=115 and BINDTABLE.metadataid=131  ORDER BY botable.CJSJ DESC");
				ll = zqbWorkFlowDAO.getCurrentFolwList();
				for(HashMap m:ll){
					if(m.get("EXTEND1")!=null){
						continue;
					}
					list.add(m);
				}
			}
		}
		return list.size();
	}
	public void setZqbWorkFlowDAO(ZqbWorkFlowDAO zqbWorkFlowDAO) {
		this.zqbWorkFlowDAO = zqbWorkFlowDAO;
	}	
}