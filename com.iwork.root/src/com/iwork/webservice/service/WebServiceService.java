package com.iwork.webservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.commons.util.UUIDUtil;

import com.iwork.core.engine.group.dao.SysEngineGroupDAO;
import com.iwork.core.engine.group.model.SysEngineGroup;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.util.SequenceUtil;

import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.dao.WebServiceDAO;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.model.SysWsParams;

public class WebServiceService {

	private WebServiceDAO webServiceDAO;
	private SysEngineGroupDAO sysEngineGroupDAO;
	
	/**
	 * 获得目录结构树视图
	 * @param parentid
	 * @return
	 */
	public String getTreeJson(Long parentid){
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = null;
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
//		if(parentid==0){
			List<SysEngineGroup> list = sysEngineGroupDAO.getSysEngineGroupList(parentid);
			for(int i=0;i<list.size();i++){ 
				SysEngineGroup model = list.get(i);
				if(model!=null&&model.getMaster()!=null&&!model.getMaster().equals("")){
					//判断权限
					boolean flag = SecurityUtil.getInstance().checkUserSecurity(userid, model.getMaster());
					if(flag){
						Map<String,Object> item = new HashMap<String,Object>();
						item.put("id", ""+model.getId());
						List<SysWsBaseinfo> clist = webServiceDAO.getList(model.getId());
						String groupName = "";
						if(clist!=null&&clist.size()>0){
							groupName = model.getGroupname()+"("+clist.size()+")";
						}else{
							groupName = model.getGroupname();
						}
						clist = null;
						item.put("name",groupName);
						item.put("iconOpen", "iwork_img/package_add.png");
						item.put("iconClose", "iwork_img/package_delete.png");
						item.put("icon", "iwork_img/package.png");
						item.put("children",this.getTreeJson(model.getId()));
						item.put("master", model.getMaster());
						item.put("parentid", model.getParentid());
						item.put("groupmemo", model.getGroupmemo());
						items.add(item);
					}
				}
			}
//			
			//未分类目录
			if(parentid.equals(new Long(0))){ 
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id", 999999);  
				node.put("name", "接口模型");
				node.put("iconOpen", "iwork_img/target.jpg"); 
				node.put("iconClose", "iwork_img/target.jpg"); 
				node.put("open", true);  
				node.put("children",items); 
				node.put("master",""); 
				node.put("type","group");
				root.add(node);
				node = new HashMap<String,Object>();
				node.put("id", -1);  
				node.put("name", "未分类");
				node.put("iconOpen", "iwork_img/package_add.png"); 
				node.put("iconClose", "iwork_img/package_delete.png");
				node.put("icon", "iwork_img/package.png");
				node.put("master",""); 
				node.put("type","group");
				root.add(node); 
				json = JSONArray.fromObject(root); 
			}else{
				json = JSONArray.fromObject(items); 
			}
//		}else{
//			
//		}
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 获得包含对象模型的树视图
	 * @param parentid
	 * @return
	 */
	public String getAllTreeJson(Long parentid){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
//		if(parentid==0){
		List<SysEngineGroup> list = sysEngineGroupDAO.getSysEngineGroupList(parentid);
		for(int i=0;i<list.size();i++){ 
			SysEngineGroup model = list.get(i);
			if(model!=null&&model.getMaster()!=null&&!model.getMaster().equals("")){
				//判断权限
				boolean flag = SecurityUtil.getInstance().checkUserSecurity(userid, model.getMaster());
				if(flag){
					//判断是否有子目录
					 List<SysWsBaseinfo> baseinfolist = webServiceDAO.getList(model.getId());
					 if(baseinfolist!=null&&baseinfolist.size()>0){
						 Map<String,Object> item = new HashMap<String,Object>();
							item.put("id", ""+model.getId());
							item.put("name", model.getGroupname());
							item.put("iconOpen", "iwork_img/package_add.png");
							item.put("iconClose", "iwork_img/package_delete.png");
							item.put("icon", "iwork_img/package.png");
							item.put("children",this.getAllTreeJson(model.getId()));
							item.put("type","group"); 
							item.put("master", model.getMaster()); 
							item.put("parentid", model.getParentid());
							item.put("groupmemo", model.getGroupmemo());
							items.add(item);
					 }
				}
			} 
			
		}
		//获取模型
		 List<SysWsBaseinfo> baseinfolist = webServiceDAO.getList(parentid);
		 for(SysWsBaseinfo itemModel:baseinfolist){
			 Map<String,Object> item = new HashMap<String,Object>(); 
				item.put("id", "item_"+itemModel.getId());

				item.put("type","DS");
				item.put("uuid",itemModel.getUuid());   
				item.put("dataid", itemModel.getId()); 
				item.put("icon", "iwork_img/server_link.png");
				items.add(item);
		 }
		

		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 获得接口列表
	 * @param groupid
	 * @return
	 */
	public List<SysWsBaseinfo> showList(Long groupid){
		
		return webServiceDAO.getList(groupid);
	}

	/**
	 * 基本信息保存
	 * @param model
	 */
	public void saveBaseInfo(SysWsBaseinfo model){
		
		if(model!=null){
			if(model.getUuid()==null||"".equals(model.getUuid())){
				model.setUuid(UUIDUtil.getUUID());
			}
			if(model.getId()==0){ 
				int id = SequenceUtil.getInstance().getSequenceIndex(WebServiceConstants.WS_MODEL_ID_SEQUENCT_KEY);
				model.setId(id);
				
				webServiceDAO.saveBaseInfo(model);
			}else{
				webServiceDAO.updateBaseInfo(model);
			}
			
			/*
			//判断输入参数是否为空
			List<SysWsParams> inputlist = webServiceDAO.getWsParamsList(ConnectionConstants.CONN_PARAMS_TYPE_INPUT, model.getId());
			if(inputlist==null||inputlist.size()==0){
				//当前接口的输入参数初始化(inputParams)
				ConnectionInterface init_interface = ConnectionFactory.getInstance(model.getInType());
				if(init_interface!=null){
					List<SysConnParams> list = init_interface.initInputParams(model);
					if(list!=null){
						for(SysConnParams param:list){
							webServiceDAO.saveParam(param); 
							if(param.getSubList()!=null&&param.getSubList().size()>0){
								for(SysConnParams item:param.getSubList()){
									item.setPid(param.getId());
									webServiceDAO.saveParam(item);
								}
							}
						}
					}
				}
			}
			//判断输入参数是否为空
			List<SysConnParams> outlist = webServiceDAO.getConnParamsList(ConnectionConstants.CONN_PARAMS_TYPE_OUTPUT, model.getId());
			if(outlist==null||outlist.size()==0){ 
				//当前接口的输入参数初始化(inputParams)
				ConnectionInterface init_interface = ConnectionFactory.getInstance(model.getInType());
				if(init_interface!=null){
					List<SysConnParams> list = init_interface.initOutputParams(model);
					if(list!=null){
						for(SysConnParams param:list){
							webServiceDAO.saveParam(param); 
							if(param.getSubList()!=null&&param.getSubList().size()>0){
								for(SysConnParams item:param.getSubList()){
									item.setPid(param.getId());
									webServiceDAO.saveParam(item);
								}
							}
						}
					}
				}
			}
			*/
			
		}
	}
	
	
	/**
	 * 集成接口基本信息删除
	 * @param model
	 */
	public boolean delete(int  id){
		boolean flag = false;
		if(id!=0){
			//删除基本信息
			SysWsBaseinfo model = webServiceDAO.getModel(id);
			if(model!=null){
				webServiceDAO.delete(model);
				flag = true; 
				/*//删除输入输出参数
				 List<SysWsParams> list = webServiceDAO.getWsParamsAllList(id);
				 for(SysWsParams param:list){
					//判断是否有子参数，一并删除
						List<SysConnParams> sublist = webServiceDAO.getWsParamsAllList(param.getId());
						if(sublist!=null){
							for(SysConnParams subParam:sublist){
								webServiceDAO.deleteParam(subParam);
							}
						}
				 }*/
			}
		}
		return flag;
	}
	
	/**
	 * 集成接口基本信息保存
	 * @param model
	 */
	public boolean delParams(int  id){
		boolean flag = false;
		
		if(id!=0){
			//删除基本信息
			SysWsParams model = webServiceDAO.getParamModel(id);
			if(model!=null){
				webServiceDAO.deleteParam(model);
				flag = true;
				/*//删除输入输出参数
				List<SysConnParams> list = webServiceDAO.getConnParamsAllList(model.getId());
				for(SysConnParams param:list){
					webServiceDAO.deleteParam(param);
				}*/
			}
		}
		return flag;
	}
	
	
	/**
	 * 保存输入输出参数模型
	 * @param model
	 */
	public void saveParamModel(SysWsParams model){
		
		if(model!=null){
			if(model.getId()==0){
				int id = SequenceUtil.getInstance().getSequenceIndex(WebServiceConstants.WS_MODEL_ID_SEQUENCT_KEY);
				model.setId(id);
				
				if(model.getUuid()==null||"".equals(model.getUuid())){
					model.setUuid(UUIDUtil.getUUID());
				}
				webServiceDAO.saveParam(model);
			}else{
				//if(model.getOrderIndex()==0){
				//	model.setOrderIndex(webServiceDAO.getMaxParamsOrderIndex());
				//}
				webServiceDAO.updateParam(model);
			}
		}
	}
	/**
	 * 移动
	 * @param type 'up'表示上移，'down'表示下移
	 * @param dictionaryId
	 * @param id
	 */
	public void move(String type,int pid,String inorout,int id){   
		webServiceDAO.updateOrderIndex(type,pid,inorout,id); 
	} 
	/**
	 * 获得参数列表json
	 * @param id
	 * @param inorout
	 * @return
	 */
	public String showParamsJSON(int pid,String inorout){
		StringBuffer jsonstr = new StringBuffer();

		List<SysWsParams> list = webServiceDAO.getWsParamsList(inorout, pid);

		Map<String,Object> outMap = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		if(list!=null){
			for(SysWsParams model:list){
				Map<String,Object> item = new HashMap<String,Object>();
				if(model==null)continue;
				item.put("ID", model.getId());
				item.put("PID", model.getPid());
				item.put("TITLE", model.getTitle());
				item.put("HAVING_NAME", model.getHavingName());
				item.put("DESCRIPTION", model.getDescription());
				item.put("NAME", model.getName()); 
				item.put("TYPE", model.getType()); 
				item.put("VALUE", model.getValue());
				item.put("REQUIRED", model.getRequired());
				item.put("UUID", model.getUuid());
				item.put("INOROUT", model.getInorout());
				item.put("ORDER_INDEX", model.getOrderIndex());
				
				if(model.getType().equals(WebServiceConstants.WS_FIELD_TYPE_STRUCTURE)){
					item.put("EDIT", "<a href='javascript:edit("+model.getId()+","+model.getPid()+");'><img src='iwork_img/note_edit.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>修改</a><a href='javascript:linkSub("+model.getId()+","+model.getPid()+");'><img src='iwork_img/note_edit.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>查看详细</a>");
				}else{
					item.put("EDIT", "<a href='javascript:edit("+model.getId()+","+model.getPid()+");'><img src='iwork_img/note_edit.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>修改</a>");
				}
				
				items.add(item); 
			}
			outMap.put("total", list.size());
			outMap.put("rows", items);
			JSONArray json = JSONArray.fromObject(outMap);
			jsonstr.append(json.toString());
		}
		return jsonstr.toString();	
	}
	
	/**
	 * 根据webservcie地址，获取对此webservice的配置信息
	 * @param url
	 * @return
	 */
	public SysWsBaseinfo getModelByURI(String URI){
		return this.webServiceDAO.getModelByURI(URI);
	};
	
	/**
	 * 根据UUID，获取WebService的配置信息
	 * @param UUID
	 * @return
	 */
	public SysWsBaseinfo getModelByUUID(String UUID){
		return this.webServiceDAO.getModelByUUID(UUID);
	};
	
	/**
	 * 对于通用WebService,根据UUID，获取对此webservice的配置信息
	 * @param UUID
	 * @return
	 */
	public List<SysWsBaseinfo> getModelListByUUID(String UUID){
		List<SysWsBaseinfo> list = this.webServiceDAO.getModelListByUUID(UUID);
		
		return list;
	};
	
	/**
	 * 获取输入参数模型列表
	 * @param bid 基础模型ID
	 * @return
	 */
	public List<SysWsParams> getInputParamsList(int pid){
		return webServiceDAO.getInputParamsList(pid);
	}
	
	/**
	 * 获取输出参数模型列表
	 * @param bid 基础模型ID
	 * @return
	 */
	public List<SysWsParams> getOutputParamsList(int pid){
		return webServiceDAO.getOutputParamsList(pid);
	}
	

	public WebServiceDAO getWebServiceDAO() {
		return webServiceDAO;
	}

	public void setWebServiceDAO(WebServiceDAO webServiceDAO) {
		this.webServiceDAO = webServiceDAO;
	}

	public void setSysEngineGroupDAO(SysEngineGroupDAO sysEngineGroupDAO) {
		this.sysEngineGroupDAO = sysEngineGroupDAO;
	}


	public SysEngineGroupDAO getSysEngineGroupDAO() {
		return sysEngineGroupDAO;
	}
	
}
