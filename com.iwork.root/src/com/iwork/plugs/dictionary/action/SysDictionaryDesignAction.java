package com.iwork.plugs.dictionary.action;

import java.util.List;
import com.iwork.core.constant.SysConst;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.dictionary.model.SysDictionaryBaseinfo;
import com.iwork.plugs.dictionary.service.SysDictionaryDesignService;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;
import com.iwork.plugs.extdbsrc.service.ExtDBSrcService;
import com.opensymphony.xwork2.ActionSupport;

public class SysDictionaryDesignAction extends ActionSupport {
	private SysDictionaryDesignService sysDictionaryDesignService;
	private ExtDBSrcService  extDBSrcService;
	protected Long parentid;
	protected Long dictionaryId;
	protected Long dicType;
	protected String tabList;
	protected String type;
	protected Long groupid;
	protected SysDictionaryBaseinfo model;
	protected List<SysDictionaryBaseinfo> list;
	protected List<SysExdbsrcCenter> datasourceList;
	protected Long id; 
	
	
	/**
	 * 加载基本信息列表
	 * @return
	 */
	public String index(){
		if(groupid!=null){
			list = sysDictionaryDesignService.getDirBaseInfoList(groupid);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 加载基本信息列表
	 * @return
	 */
	public void getTableJson(){
		String json="";
		if(groupid!=null){
			String json2 = sysDictionaryDesignService.getTableJson(groupid);
			json=json2.substring(1, json2.length()-1);
		}
	
		ResponseUtil.write(json);
	}
	/**
	 * 删除数据选择器
	 */
	public void delDic(){
		if(id!=null){
			sysDictionaryDesignService.delDic(id);
		}
		ResponseUtil.write(SUCCESS);
	}
	/**
	 * 
	 * @return
	 */
	public String showTabList(){
		if(dictionaryId!=null&dicType!=null){
			tabList = sysDictionaryDesignService.getTabList(dictionaryId,dicType); 
		}  
		return SUCCESS;
	}
	/**
	 * 添加数据选择器
	 * @return
	 */
	public String addDictionary(){
		datasourceList=extDBSrcService.getList();
		if(groupid!=null){
			model = sysDictionaryDesignService.initBaseInfoModel(groupid); 
		}
		return SUCCESS;
	}
	
	/**
	 * 打开数据选择器
	 * @return
	 */
	public String showBaseInfo(){
			if(dictionaryId!=null){
				model = sysDictionaryDesignService.getSysDictionaryBaseInfoDAO().getModel(dictionaryId);
				if(model!=null&&model.getIsAutoShow()==null){
					model.setIsAutoShow(SysConst.on);
				}
			}
			datasourceList =  extDBSrcService.getList();
			//扩展系统平台数据源
			if(datasourceList!=null){
				SysExdbsrcCenter item = new SysExdbsrcCenter();
				item.setId(new Long(999999));
				item.setDsrcTitle("系统集成平台数据源");
				datasourceList.add(item); 
			}
			return SUCCESS;
		
	}
	
	/**
	 * 加载选择器设置主框架
	 * @return
	 */
	public String showFrame(){
		
		return SUCCESS;
	}
	
	/**
	 * 保存基本信息
	 * @return
	 */
	public void saveBaseInfo(){
		if(model!=null){
				sysDictionaryDesignService.saveBaseInfo(model);
				ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	
	/**
	 * 
	 */
	public void getFieldTreeJson(){
		if(dictionaryId!=null){
			String json = sysDictionaryDesignService.getDSFieldListJson(dictionaryId,type);
			ResponseUtil.write(json);
		}
	}    
//==============================================//	
	public SysDictionaryDesignService getSysDictionaryDesignService() {
		return sysDictionaryDesignService;
	}
	public void setSysDictionaryDesignService(
			SysDictionaryDesignService sysDictionaryDesignService) {
		this.sysDictionaryDesignService = sysDictionaryDesignService;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public Long getDictionaryId() {
		return dictionaryId;
	}
	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public Long getGroupid() {
		return groupid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public SysDictionaryBaseinfo getModel() {
		return model;
	}

	public void setModel(SysDictionaryBaseinfo model) {
		this.model = model;
	}

	

	public List<SysExdbsrcCenter> getDatasourceList() {
		return datasourceList;
	}

	public void setDatasourceList(List<SysExdbsrcCenter> datasourceList) {
		this.datasourceList = datasourceList;
	}

	public ExtDBSrcService getExtDBSrcService() {
		return extDBSrcService;
	}

	public void setExtDBSrcService(ExtDBSrcService extDBSrcService) {
		this.extDBSrcService = extDBSrcService;
	}

	public Long getDicType() {
		return dicType;
	}

	public void setDicType(Long dicType) {
		this.dicType = dicType;
	}

	public String getTabList() {
		return tabList;
	}

	public void setTabList(String tabList) {
		this.tabList = tabList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<SysDictionaryBaseinfo> getList() {
		return list;
	}

	public void setList(List<SysDictionaryBaseinfo> list) {
		this.list = list;
	}


	public void setType(String type) {
		this.type = type;
	}
	
}
