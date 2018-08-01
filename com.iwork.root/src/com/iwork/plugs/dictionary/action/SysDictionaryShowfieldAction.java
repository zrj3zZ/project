package com.iwork.plugs.dictionary.action;

import java.util.ArrayList;
import java.util.List;
import com.iwork.plugs.dictionary.model.SysDictionaryShowfield;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.dictionary.service.SysDictionaryShowfieldService;
import com.opensymphony.xwork2.ActionSupport;

public class SysDictionaryShowfieldAction extends ActionSupport {
	private SysDictionaryShowfieldService sysDictionaryShowfieldService;
	private Long dictionaryId;
	private Long id;
	private SysDictionaryShowfield model;
	private List<String> fieldsChoosen;
	private String ids;
	private String fieldName;
	private String fieldTitle;
	private int type;
	private String showtype;
	

	/**
	 * 加载table
	 * @return
	 */
	public String getTable(){
		String jsonData = sysDictionaryShowfieldService.getTable(dictionaryId);    
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
			fieldsChoosen = sysDictionaryShowfieldService.getFeildsChossen(dictionaryId);
			type = 2;
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
				SysDictionaryShowfield model = sysDictionaryShowfieldService.getSysDictionaryShowfieldDAO().findModel(dictionaryId, fieldNames[i]);
				if(model!=null){
					sysDictionaryShowfieldService.getSysDictionaryShowfieldDAO().updateModel(model);
				}//存在
				else{
					SysDictionaryShowfield tempmodel = new SysDictionaryShowfield();
					tempmodel = sysDictionaryShowfieldService.initConditionModel(dictionaryId,fieldNames[i],fieldTitles[i]);
					sysDictionaryShowfieldService.getSysDictionaryShowfieldDAO().addModel(tempmodel);
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
		if(id!=null){
			model=sysDictionaryShowfieldService.getSysDictionaryShowfieldDAO().getModel(id);
			if(model.getTargetField()==null){
				model.setTargetField(model.getFieldName());
			}
			if(model.getDisplayWidth()==null){
				model.setDisplayWidth(new Long(100));
			}
		}
		return SUCCESS;
	}
	/**
	 * 保存
	 * @return
	 */
	public String save(){ 
		if(model!=null&model.getId()!=null){
			if(sysDictionaryShowfieldService.getSysDictionaryShowfieldDAO().getModel(model.getId())!=null){
				sysDictionaryShowfieldService.getSysDictionaryShowfieldDAO().updateModel(model);
			}			
		}
		else{
			sysDictionaryShowfieldService.getSysDictionaryShowfieldDAO().addModel(model);
		}
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 上移
	 * @return
	 */
	public String moveUp(){
		sysDictionaryShowfieldService.move("up",dictionaryId,id);
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 下移
	 * @return
	 */
	public String moveDown(){
		sysDictionaryShowfieldService.move("down",dictionaryId,id);
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 置顶
	 * @return
	 */
	public String moveTop(){
		sysDictionaryShowfieldService.move("top",dictionaryId,id);
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	/**
	 * 置底
	 * @return
	 */
	public String moveBottom(){
		sysDictionaryShowfieldService.move("bottom",dictionaryId,id);
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
					sysDictionaryShowfieldService.del(id);
				}
			}
		}
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	//=================================//
	public SysDictionaryShowfieldService getSysDictionaryShowfieldService() {
		return sysDictionaryShowfieldService;
	}

	public void setSysDictionaryShowfieldService(
			SysDictionaryShowfieldService sysDictionaryShowfieldService) {
		this.sysDictionaryShowfieldService = sysDictionaryShowfieldService;
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
	public SysDictionaryShowfield getModel() {
		return model;
	}
	public void setModel(SysDictionaryShowfield model) {
		this.model = model;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getShowtype() {
		return showtype;
	}
	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}
    
	
}
