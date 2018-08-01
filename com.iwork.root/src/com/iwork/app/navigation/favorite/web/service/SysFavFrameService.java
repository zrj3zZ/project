package com.iwork.app.navigation.favorite.web.service;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.navigation.favorite.myfav.bean.Sysfavfunction;
import com.iwork.app.navigation.favorite.myfav.dao.SysFavFunctionDAO;
import com.iwork.app.navigation.favorite.sysfav.bean.Sysfavsystem;
import com.iwork.app.navigation.favorite.sysfav.dao.SysFavSystemDAO;
import com.iwork.app.navigation.node.dao.SysNavNodeDAO;
import com.iwork.app.navigation.node.model.SysNavNode;
import com.iwork.app.navigation.sys.dao.SysNavSystemDAO;
import com.iwork.app.navigation.sys.model.SysNavSystem;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
/**
 * 收藏夹业务实现类
 * @author WeiGuangjian
 *
 */
public class SysFavFrameService {
	private static Logger logger = Logger.getLogger(SysFavFrameService.class);

	private SysNavSystemDAO sysNavSystemDAO;
	private SysFavSystemDAO sysFavSystemDAO;
	private SysFavFunctionDAO sysFavFunctionDAO;
	private SysNavNodeDAO sysNavNodeDAO ;
	
	/**
	 * 获得收藏夹列表
	 * @return
	 * @throws Exception 
	 */
	public String getSysFavMenuJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id", 0);
					item.put("name", "常用功能");		
					item.put("state", "open");
					item.put("iconOpen", "iwork_img/images/folder-open.gif");
					item.put("iconClose", "iwork_img/ztree/diy/40.png");
					item.put("open", true);
					item.put("children", this.getSysFavTreeJson());
					Map<String,Object> attributes = new HashMap<String,Object>();
					//装载导航信息
					attributes.put("url",null);
					attributes.put("target","_blank");
					item.put("attributes", attributes);
					items.add(item);
					Map<String,Object> item2 = new HashMap<String,Object>();
					item2.put("id", 1);
					item2.put("name", "我的收藏夹");
					item2.put("open", true); 
					item2.put("iconOpen", "iwork_img/images/folder-open.gif");
					item2.put("iconClose", "iwork_img/ztree/diy/40.png"); 
					item2.put("state", "open");
					
					item2.put("children", this.getMyFavTreeJson());
					Map<String,Object> attributes2 = new HashMap<String,Object>();
					//装载导航信息
					attributes2.put("url",null);
					attributes2.put("target", "_blank");
					item2.put("attributes", attributes2);
					items.add(item2);			
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获得系统收藏夹列表
	 * @return
	 */
	public String getSysFavTreeJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			List list = sysFavSystemDAO.getAll();
			for(int i = 0;i<list.size();i++){
				Sysfavsystem sysFavSystem = (Sysfavsystem)list.get(i);	
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id", sysFavSystem.getId());
					item.put("name", sysFavSystem.getSysName());
					item.put("icon","iwork_img/star_full.png");
					//装载导航信息
					item.put("pageurl",sysFavSystem.getSysUrl());
					item.put("target", sysFavSystem.getSysTarget());
					items.add(item);			
			}
		}catch(Exception e){
			logger.error(e,e);
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获得我的收藏夹列表
	 * @return
	 */
	public String getMyFavTreeJson(){	
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		try{
		List list = sysFavFunctionDAO.getUserAll(userContext._userModel.getUserid());
			for(int i = 0;i<list.size();i++){
				Sysfavfunction sysFavFunction = (Sysfavfunction)list.get(i);
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id", sysFavFunction.getId());
					item.put("name", sysFavFunction.getFunName());		
					item.put("icon","iwork_img/star_empty.png");
					//装载导航信息
					item.put("pageurl",sysFavFunction.getFunUrl());
					item.put("target",sysFavFunction.getFunTarget());
					items.add(item); 				
			}		
		}catch(Exception e){
			logger.error(e,e);
		}		
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 整理我的收藏夹
	 * @return
	 */
	public String getMyFav(){
		StringBuffer list = new StringBuffer();
		//我的收藏夹
		list.append("<select name='to' size='10' multiple style='width:210px;height:360px;' ondblclick='deleteSelect(document.all.to);'>");	
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		List myfavlist = sysFavFunctionDAO.getUserAll(userContext._userModel.getUserid());
		for(int i=0;i<myfavlist.size();i++){
			Sysfavfunction favmodel = (Sysfavfunction)myfavlist.get(i);
			list.append("<option value='"+favmodel.getFunId()+"'>");
			list.append(favmodel.getFunRname()).append("</option>\n");
		}
		list.append("</select>");
		
		return list.toString();
	}
	
	/**
	 * 保存我的收藏夹
	 * @param List
	 * @return
	 */
	public String saveMyFav(String List){
		String list = "";
		long num=0;
		String msg="success";
		UserContext userContext =UserContextUtil.getInstance().getCurrentUserContext();
		String uid=userContext._userModel.getUserid();
		
		//切割字符串	
		String[] temp = List.split(":");
		String temp2[] = {};
		String temp3[] = {};
		if(temp.length==2){
			 temp2 = temp[0].split(",");//fun_id列表
			 temp3 = temp[1].split(",");//fun_rname列表
		}else{
			return null;
		}
		
		try{
		//删除非外部链接数据	
		for(int i=0;i<temp3.length;i++){
			if(i!=temp3.length-1){
			if(!"".equals(temp3[i]))list += temp3[i]+"','";
			}else{
			if(!"".equals(temp3[i]))list += temp3[i];	
			}
		}
		sysFavFunctionDAO.delMyFav(uid,list);
			
		for(int i=0;i<temp2.length;i++){		
			if(temp2[i].equals("0")){
				//更新外部链接数据
				sysFavFunctionDAO.updateIndex(temp3[i],i+1,uid);
			    num++;
			}else{
				//提取系统菜单数据重新插入
				String temp4[]=temp2[i].split("_");			
				if(temp4.length>1&&temp4[1].equals("sys")){
				List syslist=sysNavSystemDAO.getSysList(temp4[0]);		
					for(int j=0;j<syslist.size();j++){
							SysNavSystem sysmodel = (SysNavSystem)syslist.get(j);	
							num++;
						    Sysfavfunction favmodel =new Sysfavfunction();
						    favmodel.setUserId(uid);
							favmodel.setFunId(sysmodel.getId()+"_sys");
							favmodel.setFunName(sysmodel.getSysName());
							favmodel.setFunRname(sysmodel.getSysName());
							favmodel.setFunTarget(sysmodel.getSysTarget());
							favmodel.setFunUrl(sysmodel.getSysUrl());
							favmodel.setFunMemo(sysmodel.getSysName());
							favmodel.setFunIndex(num);
							sysFavFunctionDAO.addBoData(favmodel);						
					}										
				}else{
					SysNavNode model=sysNavNodeDAO.getBodata(Long.parseLong(temp4[0]));	
					if(model!=null){
						Sysfavfunction favmodel =new Sysfavfunction();
					    favmodel.setUserId(uid);
						favmodel.setFunId(model.getId()+"_node");
						favmodel.setFunName(model.getNodeName());
						favmodel.setFunRname(model.getNodeName());
						favmodel.setFunTarget(model.getNodeTarget());
						favmodel.setFunUrl(model.getNodeUrl());
						favmodel.setFunMemo(model.getNodeDesc());
						favmodel.setFunIndex(num);
						sysFavFunctionDAO.addBoData(favmodel);	
					}
					    					
				}
				
			}
							
		}
		
		}catch(Exception e){logger.error(e,e);
			msg="failure";			
		}		
		return msg;
	}
	
	/**
	 * 保存外部链接
	 * @param funname
	 * @param funurl
	 * @param target
	 * @param memo
	 * @return
	 */
	public String saveUrl(String funname,String funurl,String target,String memo){	
		String msg="success";
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		String uid=userContext._userModel.getUserid();
		try{
		Sysfavfunction favmodel =new Sysfavfunction();
	    favmodel.setUserId(uid);
		favmodel.setFunId("0");
		favmodel.setFunName(funname);
		favmodel.setFunRname(funname);
		favmodel.setFunTarget(target);
		favmodel.setFunUrl(funurl);
		favmodel.setFunMemo(memo);	
		favmodel.setFunIndex(sysFavFunctionDAO.getMaxIndex());
		sysFavFunctionDAO.addBoData(favmodel);
		}catch(Exception e){logger.error(e,e);
			  msg="failure";			
		}
		return msg;
	}
	
	/**
	 * 添加至收藏夹
	 * @param funid
	 * @param funtext
	 * @return
	 */
	public String addFav(String funid,String funtext){	
		String list="";
		String list1="";
		String list2="";
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		List myfavlist = sysFavFunctionDAO.getUserAll(userContext._userModel.getUserid());
		for(int i=0;i<myfavlist.size();i++){
			Sysfavfunction favmodel = (Sysfavfunction)myfavlist.get(i);
			if(favmodel.getFunId().equals(funid)){
				return "repeat";
		    }
		    list1+=favmodel.getFunId()+",";
		    list2+=favmodel.getFunRname()+",";
		}
		list1+=funid+",";
		list2+=funtext+",";
		list=list1+":"+list2;
		return saveMyFav(list);
	}
	
	/**
	 * 获取系统收藏夹数据
	 * @return
	 */
	public String getSysFav(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> item = new ArrayList<Map<String,Object>>();
		List list = sysFavSystemDAO.getAll();
			for(int i = 0;i<list.size();i++){
				Sysfavsystem sysFavSystem = (Sysfavsystem)list.get(i);
				Sysfavsystem sysFavSystem1 = null;
				Sysfavsystem sysFavSystem2 = null;
				if(i!=0){
					sysFavSystem1 = (Sysfavsystem)list.get(i-1);
				}		
				if(i!=list.size()-1){
					sysFavSystem2 = (Sysfavsystem)list.get(i+1);
				}
				   Map<String,Object> rows = new HashMap<String,Object>();
					rows.put("id",sysFavSystem.getId());
					rows.put("sys_name", sysFavSystem.getSysName());
					rows.put("sys_url", sysFavSystem.getSysUrl());
					rows.put("sys_target", sysFavSystem.getSysTarget());
					rows.put("sys_memo", sysFavSystem.getSysMemo());
					rows.put("sys_index", sysFavSystem.getSysIndex());		
					if(i==list.size()-1){
						rows.put("index", "<a href='#' title='上移' onClick='move("+sysFavSystem.getId()+","+sysFavSystem.getSysIndex()+","+sysFavSystem1.getId()+","+sysFavSystem1.getSysIndex()+");'><img src=iwork_img/u_forward.gif border='0'></a>");
					}else if(i==0){
						rows.put("index", "<a href='#' title='下移' onClick='move("+sysFavSystem.getId()+","+sysFavSystem.getSysIndex()+","+sysFavSystem2.getId()+","+sysFavSystem2.getSysIndex()+");'><img src=iwork_img/d_forward.gif border='0'></a>");	
					}else{
						rows.put("index", "<a href='#' title='上移' onClick='move("+sysFavSystem.getId()+","+sysFavSystem.getSysIndex()+","+sysFavSystem1.getId()+","+sysFavSystem1.getSysIndex()+");'><img src=iwork_img/u_forward.gif border='0'></a><a href='#' title='下移' onClick='move("+sysFavSystem.getId()+","+sysFavSystem.getSysIndex()+","+sysFavSystem2.getId()+","+sysFavSystem2.getSysIndex()+");'><img src=iwork_img/d_forward.gif border='0'></a>");	
					}
				    item.add(rows);	
				    
			}	  		
		JSONArray json = JSONArray.fromObject(item);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	/**
	 * 保存系统收藏夹
	 * @param sys_id
	 * @param sys_name
	 * @param sys_url
	 * @param sys_target
	 * @param sys_memo
	 * @param sys_index
	 * @param type
	 */
	public void saveSysFav(long sys_id,String sys_name,String sys_url,String sys_target,String sys_memo,long sys_index,String type){
		if(type.equals("edit")){
			sysFavSystemDAO.update(sys_id, sys_name, sys_url, sys_target, sys_memo, sys_index);
		}else if(type.equals("add")){
			Sysfavsystem favmodel =new Sysfavsystem();
		    favmodel.setSysName(sys_name);
			favmodel.setSysUrl(sys_url);
			favmodel.setSysTarget(sys_target);
			favmodel.setSysMemo(sys_memo);
			long index=sysFavSystemDAO.getMaxIndex();
			favmodel.setSysIndex(index+1);
			sysFavSystemDAO.addBoData(favmodel);
		}else if(type.equals("delete")){
			sysFavSystemDAO.delete(sys_id);
		}
	}
	
	/**
	 * 排序
	 * @param sys_id
	 * @param sys_index
	 * @param isys_id
	 * @param isys_index
	 */
	public void move(long sys_id,long sys_index,long isys_id,long isys_index){
		sysFavSystemDAO.move(sys_id,isys_index);
		sysFavSystemDAO.imove(sys_index,isys_id);
    }
	
			
	public void setSysNavSystemDAO(SysNavSystemDAO sysNavSystemDAO) {
		this.sysNavSystemDAO = sysNavSystemDAO;
	}

	public void setSysFavSystemDAO(SysFavSystemDAO sysFavSystemDAO) {
		this.sysFavSystemDAO = sysFavSystemDAO;
	}
	
	public void setSysFavFunctionDAO(SysFavFunctionDAO sysFavFunctionDAO) {
		this.sysFavFunctionDAO = sysFavFunctionDAO;
	}

	public SysNavNodeDAO getSysNavNodeDAO() {
		return sysNavNodeDAO;
	}

	public void setSysNavNodeDAO(SysNavNodeDAO sysNavNodeDAO) {
		this.sysNavNodeDAO = sysNavNodeDAO;
	}
	
}
