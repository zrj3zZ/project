package com.iwork.plugs.extdbsrc.action;

import java.util.List;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;
import com.iwork.plugs.extdbsrc.service.ExtDBSrcService;
import com.opensymphony.xwork2.ActionSupport;

public class ExtDBSrcAction extends ActionSupport{
	private ExtDBSrcService  extDBSrcService;
	private SysExdbsrcCenter model;
	private List<SysExdbsrcCenter> list;
	private long id;  //该id与前台编辑和删除时传过来的id相对应

	
	/**
	 * 加载外部数据源管理空间
	 * @return
	 */
	public String load(){ 
		list=extDBSrcService.getList();
		return SUCCESS;
	}
	/**
	 * 新增外部数据源
	 * @return
	 */
	public String add(){
		return SUCCESS;
	}
	/**
	 * 编辑外部数据源
	 * @return
	 */
	public String edit(){
		model=extDBSrcService.getModel(id);
		return SUCCESS;
	}
	/**
	 * 删除外部数据源
	 * @return
	 */
	public String delete(){
		SysExdbsrcCenter model=extDBSrcService.getModel(id);
		if(model!=null){
			extDBSrcService.delModel(model);
		}		
		return SUCCESS;
	}
	/**
	 * ajax保存
	 */
	public void save(){
		if(model.getId()==null){
			extDBSrcService.save(model);
		}
		else if(extDBSrcService.getModel(model.getId())!=null){
			extDBSrcService.updateModle(model);
		}
		ResponseUtil.writeTextUTF8("ok");
	}
	/**
	 * ajax测试能否链接到数据源
	 */
	public void testCon(){
		int errCode=extDBSrcService.testCon(model,"");
		ResponseUtil.writeTextUTF8(String.valueOf(errCode));
	}
	//===========================//

	public ExtDBSrcService getExtDBSrcService() {
		return extDBSrcService;
	}
	public void setExtDBSrcService(ExtDBSrcService extDBSrcService) {
		this.extDBSrcService = extDBSrcService;
	}
	public SysExdbsrcCenter getModel() {
		return model;
	}
	public void setModel(SysExdbsrcCenter model) {
		this.model = model;
	}
	public List<SysExdbsrcCenter> getList() {
		return list;
	}
	public void setList(List<SysExdbsrcCenter> list) {
		this.list = list;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}  
}
