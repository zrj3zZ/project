package com.iwork.plugs.dictionary.action;

import java.util.ArrayList;
import java.util.List;

import com.iwork.core.engine.plugs.component.ConfigComponentModel;
import com.iwork.core.engine.plugs.component.IFormUIFactory;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.dictionary.service.SysDictionaryConditionService;
import com.opensymphony.xwork2.ActionSupport;
import com.iwork.plugs.dictionary.model.SysDictionaryCondition;

public class SysDictionaryConditionAction extends ActionSupport {
	private SysDictionaryConditionService sysDictionaryConditionService;
	private Long dictionaryId;
	private Long id;
	private SysDictionaryCondition model;
	private List<ConfigComponentModel> displayTypeList=new ArrayList<ConfigComponentModel>();
	private String fieldName;
	private String fieldTitle;
	private List<String> fieldsChoosen;
	private String ids;
	private int type;
	private String showtype;
	               
	
	/**
	 * 加载table
	 * @return
	 */
	public String getTable(){
		String jsonData = sysDictionaryConditionService.getTable(dictionaryId);		
		ResponseUtil.write( jsonData.substring(1, jsonData.length()-1));		
		return null;
	}

	/**
	 * 新增
	 * @return
	 */
	public String add(){
		fieldsChoosen = new ArrayList<String>();
		if(dictionaryId!=null){
			fieldsChoosen = sysDictionaryConditionService.getFeildsChossen(dictionaryId);
			type = 1;
		}
		
		return SUCCESS;
	}
	/**
	 * 快速添加，只添加字段名称和字段标题
	 * 通过树视图选择添加
	 * @return
	 */
	public void quickAdd(){
		String msg = "error";
		if(dictionaryId!=null&&fieldName!=null&&fieldTitle!=null){
			String[] fieldNames = fieldName.split(",");
			String[] fieldTitles = fieldTitle.split(",");
			for(int i=0;i<fieldNames.length;i++){				
				SysDictionaryCondition model = sysDictionaryConditionService.getSysDictionaryConditionDAO().findModel(dictionaryId, fieldNames[i]);
				if(model!=null){
					sysDictionaryConditionService.getSysDictionaryConditionDAO().updateModel(model);
				}//存在
				else{
					SysDictionaryCondition tempmodel = new SysDictionaryCondition();
					tempmodel = sysDictionaryConditionService.initConditionModel(dictionaryId,fieldNames[i],fieldTitles[i]);
					sysDictionaryConditionService.getSysDictionaryConditionDAO().addModel(tempmodel);
				}
			}			
			msg = "success";
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 编辑
	 * @return
	 */
	public String edit(){
		displayTypeList = IFormUIFactory.getComponentSortList();
		if(id!=null){
			model=sysDictionaryConditionService.getSysDictionaryConditionDAO().getModel(id);			
		}
		return SUCCESS;
	}
	/**
	 * 保存
	 * @return
	 */
	public String save(){ 
		if(model!=null&model.getId()!=null){
			if(sysDictionaryConditionService.getSysDictionaryConditionDAO().getModel(model.getId())!=null){
				sysDictionaryConditionService.getSysDictionaryConditionDAO().updateModel(model);
			}			
		}
		else{
			sysDictionaryConditionService.getSysDictionaryConditionDAO().addModel(model);
		}
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 上移
	 * @return
	 */
	public String moveUp(){
		sysDictionaryConditionService.move("up",dictionaryId,id);
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 下移
	 * @return
	 */
	public String moveDown(){
		sysDictionaryConditionService.move("down",dictionaryId,id);
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 置顶
	 * @return
	 */
	public String moveTop(){
		sysDictionaryConditionService.move("top",dictionaryId,id);
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 置底
	 * @return
	 */
	public String moveBottom(){
		sysDictionaryConditionService.move("bottom",dictionaryId,id);
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 删除
	 * @return
	 */
	public String del(){
		if(ids!=null){
			String[] idstrs = ids.split(",");
			for(String idstr:idstrs){
				if(idstr==null)continue;
				Long id = Long.parseLong(idstr);
				if(id!=null){
					sysDictionaryConditionService.del(id);
				}
			}
		}
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}

	//===============================//
	public SysDictionaryConditionService getSysDictionaryConditionService() {
		return sysDictionaryConditionService;
	}

	public void setSysDictionaryConditionService(
			SysDictionaryConditionService sysDictionaryConditionService) {
		this.sysDictionaryConditionService = sysDictionaryConditionService;
	}

	public Long getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysDictionaryCondition getModel() {
		return model;
	}

	public void setModel(SysDictionaryCondition model) {
		this.model = model;
	}

	public List<ConfigComponentModel> getDisplayTypeList() {
		return displayTypeList;
	}

	public void setDisplayTypeList(List<ConfigComponentModel> displayTypeList) {
		this.displayTypeList = displayTypeList; 
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public List<String> getFieldsChoosen() {
		return fieldsChoosen;
	}

	public void setFieldsChoosen(List<String> fieldsChoosen) {
		this.fieldsChoosen = fieldsChoosen;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	public String getShowtype() {
		return showtype;
	}
	
}
