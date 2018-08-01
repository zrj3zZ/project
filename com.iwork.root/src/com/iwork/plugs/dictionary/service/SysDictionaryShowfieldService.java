package com.iwork.plugs.dictionary.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import com.iwork.plugs.dictionary.dao.SysDictionaryShowfieldDAO;
import com.iwork.plugs.dictionary.model.SysDictionaryShowfield;

public class SysDictionaryShowfieldService {
	private SysDictionaryShowfieldDAO sysDictionaryShowfieldDAO;
	
	public String getTable(long dictionaryId){  
		List<SysDictionaryShowfield> list=sysDictionaryShowfieldDAO.getList(dictionaryId);
		StringBuffer jsonstr = new StringBuffer();
		Map<String,Object> outMap = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		if(list!=null){
			for(SysDictionaryShowfield model:list){
				Map<String,Object> item = new HashMap<String,Object>();
				if(model==null)continue;
				item.put("ID", model.getId());
				item.put("FIELD_NAME", model.getFieldName());
				item.put("FIELD_TITLE", model.getFieldTitle());
				item.put("DICTIONARY_ID", model.getDictionaryId());
				item.put("TARGET_FIELD", model.getTargetField());
				item.put("DISPLAY_WIDTH", model.getDisplayWidth());
				item.put("ORDER_INDEX", model.getOrderIndex());
				item.put("EDIT", "<a href='javascript:edit("+model.getId()+","+model.getDictionaryId()+");'><img src='iwork_img/note_edit.png' style='border:0px solid #efefef;margin-right:3px;margin-top:3px;'>修改</a>&nbsp;");
				items.add(item);
			}
			outMap.put("total", list.size());
			outMap.put("rows", items);
			JSONArray json = JSONArray.fromObject(outMap);
			jsonstr.append(json.toString());
		}
		return jsonstr.toString();		
	}
	
	public SysDictionaryShowfield initConditionModel(Long dictionaryId,String fieldName,String fieldTitle){
		SysDictionaryShowfield model=new SysDictionaryShowfield();
		model.setDictionaryId(dictionaryId);
		model.setFieldName(fieldName);
		model.setFieldTitle(fieldTitle);
		long orderIndex=sysDictionaryShowfieldDAO.getMaxOrderIndex();
		model.setOrderIndex(orderIndex);
		model.setIsShow(new Long(0));   //0表示显示
		return model;
	}
	
	public void move(String type,Long dictionaryId,Long id){  
		sysDictionaryShowfieldDAO.updateOrderIndex(type,dictionaryId,id);
	}
	public void del(Long id){
		SysDictionaryShowfield model=sysDictionaryShowfieldDAO.getModel(id);
		sysDictionaryShowfieldDAO.del(model);
	}
	/**
	 * 该报表下查询条件设置下已选字段的字段ID
	 * @param reportId
	 * @return
	 */
	public List<String> getFeildsChossen(Long dictionaryId){
		List<String> list = new ArrayList<String>();
		if(dictionaryId!=null){
			List<String> listStr = sysDictionaryShowfieldDAO.getFeildsChossen(dictionaryId);
			if(listStr!=null){
				for(String str:listStr){
					if(str==null)continue;
					list.add("'"+str.trim()+"'");
				}
			}
		}
		return list;
	}
	//===================================//
	public SysDictionaryShowfieldDAO getSysDictionaryShowfieldDAO() {
		return sysDictionaryShowfieldDAO;
	}

	public void setSysDictionaryShowfieldDAO(
			SysDictionaryShowfieldDAO sysDictionaryShowfieldDAO) {
		this.sysDictionaryShowfieldDAO = sysDictionaryShowfieldDAO;
	}
	
	

}
