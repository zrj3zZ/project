package com.iwork.plugs.dictionary.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.core.engine.plugs.component.ConfigComponentModel;
import com.iwork.core.engine.plugs.component.IFormUIFactory;
import com.iwork.plugs.dictionary.dao.SysDictionaryConditionDAO;
import com.iwork.plugs.dictionary.model.SysDictionaryCondition;

public class SysDictionaryConditionService {
	private SysDictionaryConditionDAO sysDictionaryConditionDAO;
	/**
	 * 加载table
	 * @param dictionaryId
	 * @return
	 */
	public String getTable(long dictionaryId){
		List<SysDictionaryCondition> list=sysDictionaryConditionDAO.getList(dictionaryId);
		StringBuffer jsonstr = new StringBuffer();
		Map<String,Object> outMap = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		if(list!=null){
			for(SysDictionaryCondition model:list){
				Map<String,Object> item = new HashMap<String,Object>();
				if(model==null)continue;
				item.put("ID", model.getId());
				item.put("FIELD_NAME", model.getFieldName());
				item.put("FIELD_TITLE", model.getFieldTitle());
				String display = this.interpretDisplay(model.getDisplayType());
				item.put("DISPLAY_TYPE", display);
				String comparison = this.interpretComparison(model.getCompareType());
				item.put("COMPARE_TYPE", comparison);
				item.put("DISPLAY_ENUM", model.getDisplayEnum());
				item.put("ORDER_INDEX", model.getOrderIndex());
				item.put("EDIT", "<a href='javascript:edit("+model.getId()+","+model.getDictionaryId()+");'><img src='iwork_img/note_edit.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>修改</a>");
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
	 * 条件对象初始化
	 * @param dictionaryId
	 * @param fieldName
	 * @param fieldTitle
	 * @return
	 */
	public SysDictionaryCondition initConditionModel(Long dictionaryId,String fieldName,String fieldTitle){
		SysDictionaryCondition model=new SysDictionaryCondition();
		model.setDictionaryId(dictionaryId);
		model.setFieldName(fieldName);
		model.setFieldTitle(fieldTitle);
		long orderIndex=sysDictionaryConditionDAO.getMaxOrderIndex();
		model.setOrderIndex(orderIndex);
		model.setCompareType("equal");
		model.setDisplayType("TxtBox");
		model.setDsType(new Long(1)); //默认用户输入
		model.setFieldType(new Long(1)); //默认为文本类型
		return model;
	}  

	/**
	 * 移动
	 * @param type 'up'表示上移，'down'表示下移
	 * @param dictionaryId
	 * @param id
	 */
	public void move(String type,Long dictionaryId,Long id){  
		sysDictionaryConditionDAO.updateOrderIndex(type,dictionaryId,id);
	}
	/**
	 * 删除
	 * @param id
	 */
	public void del(Long id){
		SysDictionaryCondition model=sysDictionaryConditionDAO.getModel(id);
		sysDictionaryConditionDAO.del(model);
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String interpretDisplay(String key){
		ConfigComponentModel model = IFormUIFactory.getUIComponentModel(key);
		if(model!=null){
			return model.getTitle();
		}else{
			return "";
		}
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String interpretComparison(String key){
		String keys[]={"equal","bigThan","smallThan","bigEqual","smallEqual","included(fuzzyMatch)","included(firstFuzzyMatch)"};
		String values[]={"等于","大于","小于","大于等于","小于等于","包含于（模糊匹配）","包含于（从第一个位置开始模糊匹配）"};
		String value=""; 
		for(int i=0;i<keys.length;i++){
			if(keys[i].equals(key)){
				value=values[i];
				break;
			}
		}
		return value;       
	}
	/**
	 * 该报表下查询条件设置下已选字段的字段ID
	 * @param dictionaryId
	 * @return
	 */
	public List<String> getFeildsChossen(Long dictionaryId){
		List<String> list = new ArrayList<String>();
		if(dictionaryId!=null){
			List<String> listStr = sysDictionaryConditionDAO.getFeildsChossen(dictionaryId);
			if(listStr!=null){
				for(String str:listStr){
					if(str==null)continue;
					list.add("'"+str.trim()+"'");
				}
			}
		}
		return list;
	}
	//====================================//
	public SysDictionaryConditionDAO getSysDictionaryConditionDAO() {
		return sysDictionaryConditionDAO;
	}

	public void setSysDictionaryConditionDAO(
			SysDictionaryConditionDAO sysDictionaryConditionDAO) {
		this.sysDictionaryConditionDAO = sysDictionaryConditionDAO;
	}
	
	

}
