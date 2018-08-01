package com.iwork.plugs.dictionary.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.connection.constants.ConnectionConstants;
import com.iwork.connection.model.SysConnBaseinfo;
import com.iwork.connection.model.SysConnParams;
import com.iwork.connection.service.ConnectionRuntimeService;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.group.dao.SysEngineGroupDAO;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.dictionary.constant.DictionaryConstant;
import com.iwork.plugs.dictionary.dao.SysDictionaryBaseInfoDAO;
import com.iwork.plugs.dictionary.dao.SysDictionaryConditionDAO;
import com.iwork.plugs.dictionary.dao.SysDictionaryShowfieldDAO;
import com.iwork.plugs.dictionary.model.SysDictionaryBaseinfo;
import com.iwork.plugs.dictionary.model.SysDictionaryCondition;
import com.iwork.plugs.dictionary.model.SysDictionaryShowfield;
import com.iwork.plugs.dictionary.util.DictionaryUtil;
import com.iwork.plugs.extdbsrc.dao.ExtDBSrcDao;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;
import com.iwork.plugs.extdbsrc.service.ExtDBSrcService;


public class SysDictionaryDesignService {
	private SysEngineGroupDAO sysEngineGroupDAO;
	private ConnectionRuntimeService connectionRuntimeService;
	private SysDictionaryBaseInfoDAO sysDictionaryBaseInfoDAO;
	private SysDictionaryShowfieldDAO sysDictionaryShowfieldDAO;
	private SysDictionaryConditionDAO sysDictionaryConditionDAO;
	private ExtDBSrcDao extDBSrcDao;
	private ExtDBSrcService extDBSrcService;
	
	/** 
	 * 加载树
	 * @return
	 */
	public List<SysDictionaryBaseinfo> getDirBaseInfoList(Long parentid){
			//获得存储列表
			List<SysDictionaryBaseinfo> dictionaryList = sysDictionaryBaseInfoDAO.getList(parentid);
		return dictionaryList;
	}
	
	/**
	 * 初始化基本信息模型对象
	 * @return
	 */
	public SysDictionaryBaseinfo initBaseInfoModel(Long groupid){
		SysDictionaryBaseinfo model = new SysDictionaryBaseinfo();
		model.setDicType(new Long(1));//单选
		model.setDsId(new Long(0));//本地数据源 
		model.setGroupid(groupid);
		model.setIsAutoShow(SysConst.on);
		model.setRowNum(new Long(10));
		model.setMaster(SecurityUtil.supermanager);
		return model;
	}
	/**
	 * 
	 * @param groupid
	 * @return
	 */
	public String getTableJson(Long groupid){
		StringBuffer jsonStrBuf = new StringBuffer();
		Map<String,Object> outMap = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		List<SysDictionaryBaseinfo> dicList = sysDictionaryBaseInfoDAO.getList(groupid);
		if(dicList!=null){
			for(int i=0;i<dicList.size();i++){
				Map<String,Object> item = new HashMap<String,Object>();
				SysDictionaryBaseinfo model = dicList.get(i);
				if(model==null)continue;
				item.put("ID", i+1);
				item.put("ITEMID", model.getId());
				String icon = this.getIcon(model.getDicType());
				item.put("DICNAME",icon+ model.getDicName());
				String dicType = this.interpretDicType(model.getDicType());
				item.put("DICTYPESTR", dicType);            
				item.put("DICTYPE", model.getDicType());    
		/*		String status = "关闭";
				if(model.getStatus()==1){
					status = "开启";
				}
				item.put("STATUS", status);  */
				item.put("MASTER", model.getMaster());
				items.add(item);
			}
			outMap.put("total", dicList.size());
			outMap.put("rows",items);
			JSONArray json = JSONArray.fromObject(outMap);
			jsonStrBuf.append(json);
		}
	  return jsonStrBuf.toString();
	}
	/**
	 * 图标
	 * @param type
	 * @return
	 */
	public String getIcon(Long type){
		String icon = "";
		if(type!=null){
			if(type==1){
				icon = "<img src='iwork_img/ztree/diy/blueprint.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>";
			}
			else if(type==2){
				icon = "<img src='iwork_img/page_code.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>";
			}
			else if(type==3){
				icon = "<img src='iwork_img/ztree/diy/8.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>";
			}
			else{
				icon = "<img src='iwork_img/ztree/diy/9.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>";
			}
		}		
		return icon;
	}
    /**
     * 类型
     * @param type
     * @return
     */
	public String interpretDicType(Long type){
		String DicType = "";
		if(type!=null){
			if(type==1){
				DicType = "表单选择器(单选)";
			}
			else if(type==2){
				DicType = "表单选择器(复选)";
			}
			else if(type==3){
				DicType = "行项目选择器";
			}
			else{
				DicType = "";
			}
		}		
		return DicType;
	}
	/**
	 * 
	 * @param dictionaryId
	 * @param dicType
	 * @return
	 */
	public String getTabList(Long dictionaryId,Long dicType){
		StringBuffer html = new StringBuffer();
		html.append("<dl class=\"demos-nav\">").append("\n");
		for(int i=0;i<DictionaryConstant.dictionaryTab.length;i++){
			String name = DictionaryConstant.dictionaryTab[i];
			String url = DictionaryConstant.dictionaryUrl[i];
			String select ="";
			if(name.equals("基本设置")){ 
				select = " class=\"selected\" ";
			}
			html.append("<dd><a id=\"sysFlowDefStepDesign").append(i).append("\" ").append(select).append(" href=\"").append(url).append("?dictionaryId=").append(dictionaryId).append("&dicType=").append(dicType).append("\" target=\"dictionary_Designer_right\">").append(name).append("</a></dd>\n");
		}
		html.append("</dl>").append("\n");
		return html.toString();
	}
	
	/**
	 * 保存基本信息
	 * @param model
	 */
	public void saveBaseInfo(SysDictionaryBaseinfo model){
		if(model!=null){
			if(model.getUuid()==null||"".equals(model.getUuid())){
				model.setUuid(UUIDUtil.getUUID());
			}
			if(model.getId()==null){
				sysDictionaryBaseInfoDAO.addModel(model);
			}else{
				sysDictionaryBaseInfoDAO.updateModel(model);
			}
		}
	}
	/**
	 * 
	 * @param dictionaryId
	 * @return
	 */
	public String getDSFieldListJson(Long dictionaryId,String type){
		StringBuffer jsonBuffer = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		if(dictionaryId!=null){
			SysDictionaryBaseinfo model = sysDictionaryBaseInfoDAO.getModel(dictionaryId);
			if(model!=null&model.getDsId()!=null&model.getDsSql()!=null&model.getDicName()!=null){
				Map<String,Object> item1 = new HashMap<String,Object>();
				item1.put("id", 0);
				item1.put("name", model.getDicName());
				item1.put("type", "dic");
				item1.put("open", "true");
				item1.put("iconOpen", "iwork_img/ztree/diy/1_open.png");
				item1.put("iconClose", "iwork_img/ztree/diy/1_close.png");
				items.add(item1);
				//判断是否来源于数据集成平台
				if(model.getDsId().equals(new Long(999999))){
					if(type==null){
						type=ConnectionConstants.CONN_PARAMS_TYPE_OUTPUT;
					}
					SysConnBaseinfo sysConnBaseinfo = connectionRuntimeService.getConnectionDesignService().getConnectionDesignDAO().getModel(model.getDsSql());
					if(sysConnBaseinfo!=null){
						List<SysConnParams> paramlist = connectionRuntimeService.getConnectionDesignService().getConnectionDesignDAO().getConnParamsList(type, sysConnBaseinfo.getId());
						if(paramlist!=null){
							for(SysConnParams param:paramlist){
								if(param.getType()!=null&&(param.getType().equals(ConnectionConstants.CONN_SAP_FIELD_TYPE_TABLE)||param.getType().equals(ConnectionConstants.CONN_SAP_FIELD_TYPE_STRUCTURE))){
									//获取table下的数据结构
									List<SysConnParams>  sublist = null;
									sublist = connectionRuntimeService.getConnectionDesignService().getConnectionDesignDAO().getConnParamsList(type, param.getId());
									for(SysConnParams subParam:sublist){
										Map<String,Object> item2 = new HashMap<String,Object>();
										item2.put("id", subParam.getId());
										item2.put("pid", 0);
										item2.put("name", subParam.getTitle()+"["+subParam.getName()+"]");
										item2.put("fieldName", subParam.getName()); 
										item2.put("tableName", "");
										item2.put("type", "field");
										item2.put("icon", "iwork_img/page_code.png");
										items.add(item2);
									}
								}else{
									Map<String,Object> item2 = new HashMap<String,Object>();
									item2.put("id", param.getId());
									item2.put("pid", 0);
									item2.put("name", param.getTitle()+"["+param.getName()+"]");
									item2.put("fieldName", param.getName()); 
									item2.put("tableName", "");
									item2.put("type", "field");
									item2.put("icon", "iwork_img/page_code.png"); 
									items.add(item2);
								}
								
							}
						}
					}
					
				}else{
					
					SysExdbsrcCenter dbsrc = new SysExdbsrcCenter();
					List<String> list = null;
					if(model.getDsId()==0){
						//dbsrc = extDBSrcService.getLocalDbsrc();
						String sql = model.getDsSql();
						sql = DictionaryUtil.getInstance().convertMacrosValue(sql, null);
						list = extDBSrcDao.getTableFields(null,sql); 
					}   //本地数据源
					else{
						dbsrc = extDBSrcDao.getModel(model.getDsId());     //外部数据源
						 list = extDBSrcDao.getTableFields(dbsrc,model.getDsSql());
					}//外部数据源	
						if(list!=null&list.size()>0){												
							String tableName = list.get(0);						
							for(int i=1;i<list.size();i++){
								Map<String,Object> item2 = new HashMap<String,Object>();
								String fieldName = list.get(i);
								item2.put("id", i);
								item2.put("pid", 0);
								item2.put("name", tableName+"["+fieldName+"]");
								item2.put("fieldName", fieldName);
								item2.put("tableName", tableName);
								item2.put("type", "field");
								item2.put("icon", "iwork_img/page_code.png");
								items.add(item2);
							}
						}	
				}
			}
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonBuffer.append(json);
		return jsonBuffer.toString();
	}
	/**
	 * 删除数据选择器
	 * @param id
	 */
	public void delDic(Long dictionaryId){
		if(dictionaryId!=null){
			//删除条件列表
			List<SysDictionaryCondition> conditionList =  sysDictionaryConditionDAO.getList(dictionaryId);
			for(SysDictionaryCondition cmodel:conditionList){
				sysDictionaryConditionDAO.del(cmodel);
			}
			//删除显示列表
			List<SysDictionaryShowfield> showlist =sysDictionaryShowfieldDAO.getList(dictionaryId);
			for(SysDictionaryShowfield smodel:showlist){
				sysDictionaryShowfieldDAO.del(smodel);
			}
			sysDictionaryBaseInfoDAO.delDic(dictionaryId);
		}
	}
//====================================//	
	public SysDictionaryBaseInfoDAO getSysDictionaryBaseInfoDAO() {
		return sysDictionaryBaseInfoDAO;
	}
	public void setSysDictionaryBaseInfoDAO(
			SysDictionaryBaseInfoDAO sysDictionaryBaseInfoDAO) {
		this.sysDictionaryBaseInfoDAO = sysDictionaryBaseInfoDAO;
	}
	public SysEngineGroupDAO getSysEngineGroupDAO() {
		return sysEngineGroupDAO;
	}
	public void setSysEngineGroupDAO(SysEngineGroupDAO sysEngineGroupDAO) {
		this.sysEngineGroupDAO = sysEngineGroupDAO;
	}

	public ExtDBSrcDao getExtDBSrcDao() {
		return extDBSrcDao;
	}

	public void setExtDBSrcDao(ExtDBSrcDao extDBSrcDao) {
		this.extDBSrcDao = extDBSrcDao;
	}

	public ExtDBSrcService getExtDBSrcService() {
		return extDBSrcService;
	}

	public void setExtDBSrcService(ExtDBSrcService extDBSrcService) {
		this.extDBSrcService = extDBSrcService;
	}

	public SysDictionaryShowfieldDAO getSysDictionaryShowfieldDAO() {
		return sysDictionaryShowfieldDAO;
	}

	public void setSysDictionaryShowfieldDAO(
			SysDictionaryShowfieldDAO sysDictionaryShowfieldDAO) {
		this.sysDictionaryShowfieldDAO = sysDictionaryShowfieldDAO;
	}

	public SysDictionaryConditionDAO getSysDictionaryConditionDAO() {
		return sysDictionaryConditionDAO;
	}

	public void setSysDictionaryConditionDAO(
			SysDictionaryConditionDAO sysDictionaryConditionDAO) {
		this.sysDictionaryConditionDAO = sysDictionaryConditionDAO;
	}

	public void setConnectionRuntimeService(
			ConnectionRuntimeService connectionRuntimeService) {
		this.connectionRuntimeService = connectionRuntimeService;
	}
	
	
}
