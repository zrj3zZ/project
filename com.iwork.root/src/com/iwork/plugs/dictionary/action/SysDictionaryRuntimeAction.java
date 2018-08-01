package com.iwork.plugs.dictionary.action;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.iwork.core.constant.SysConst;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.dictionary.service.SysDictionaryRuntimeService;
import com.opensymphony.xwork2.ActionSupport;
import com.iwork.plugs.dictionary.constant.DictionaryConstant;
import com.iwork.plugs.dictionary.model.SysDictionaryBaseinfo;
import com.iwork.process.managementcenter.util.PageBean;

public class SysDictionaryRuntimeAction extends ActionSupport {
	private SysDictionaryRuntimeService sysDictionaryRuntimeService;
	private String dictionaryUUID;
	private Long dictionaryId;
	private String colNames;
	private String colModel;
	private String dictionaryName;
	private String conditionsHtml;
	private String toolbarHtml;
	private String insertscript;
	private String searchscript;
	private String subformid;
	private String subformkey;
	private String initFieldScript;
	private String rowIdPrefix;
	
	private int currTodoPage; //当前待办页
	private int totalTodoPage;//待办总页数
	private int currTaskPage; //当前任务页
	private int totalTaskPage; //任务总页数
	private int perTodoPage = 10; // 每页待办数
	private Long isAutoShow;
	private PageBean todoListBean; // 待办分页
	/**
	 * 分页类
	 */
	protected Page page = new Page();
	/**
	 * 加载数据字典列表
	 * @return
	 */
	public String index(){
		String msg = "radio";
		 if(dictionaryUUID!=null&&!"".equals(dictionaryUUID)&&!"null".equals(dictionaryUUID)){
			 SysDictionaryBaseinfo model = sysDictionaryRuntimeService.getSysDictionaryBaseInfoDAO().getModel(dictionaryUUID);
			 if(model!=null){
				 dictionaryId = model.getId();
			 }
		 }
		 if(dictionaryId!=null){
			 HttpServletRequest request = ServletActionContext.getRequest();
				Map params = request.getParameterMap(); 
			 SysDictionaryBaseinfo model = sysDictionaryRuntimeService.getSysDictionaryBaseInfoDAO().getModel(dictionaryId);
			 if(model!=null){
				 if( model.getIsAutoShow()!=null){
					 isAutoShow = model.getIsAutoShow();
				 }else{
					 isAutoShow = SysConst.on;
				 }
				//获取操作按钮
				 toolbarHtml = sysDictionaryRuntimeService.getToolbarHtml(model);
				 //列表标题脚本
				 colNames = sysDictionaryRuntimeService.getColNamesJSON(model);
				 //模型脚本 
				 colModel = sysDictionaryRuntimeService.getColModelJSON(model);
				 //条件区域显示HTML
				 conditionsHtml = sysDictionaryRuntimeService.getConditionsHtml(dictionaryId,params); 
				 //执行查询脚本 
				 searchscript = sysDictionaryRuntimeService.getSearchScript(dictionaryId); 
				 //获取表单对象的脚本
				 initFieldScript = sysDictionaryRuntimeService.getFieldParamMapScript(dictionaryId); 
				 //
				 dictionaryName = model.getDicName();
				 if(model.getDicType().equals(DictionaryConstant.DICTIONARY_TYPE_RADIO)){
					 msg = "radio";
				 }else if(model.getDicType().equals(DictionaryConstant.DICTIONARY_TYPE_MULTI)){
					 msg = "multi"; 
				 }else if(model.getDicType().equals(DictionaryConstant.DICTIONARY_TYPE_SUBGRID)){
					 msg = "subgrid";
				 }
			 }else{
				 dictionaryName = "数据选择器"; 
			 } 
			 //获取插入脚本 
				 insertscript = sysDictionaryRuntimeService.getInsertScript(new Long(dictionaryId),subformkey,subformid,rowIdPrefix); 
		 }else{
			 conditionsHtml = "<div style=\"text-align:center\"><h3>未定义的数据选择器,请联系管理员</h3></div>";
		 }
		return msg;
	}
	
	
	/**
	 * 获得数据列表json
	 */
	public void showDataJson(){
		if(dictionaryId!=null){
			HttpServletRequest request = ServletActionContext.getRequest();
			Map params = request.getParameterMap(); 
			String json = sysDictionaryRuntimeService.getDataJSON(new Long(dictionaryId),page,params);
			ResponseUtil.write(json.substring(1, json.length()-1)); 
		 }  
	}
	
	
	public Long getDictionaryId() {
		return dictionaryId;
	}
	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}
	public SysDictionaryRuntimeService getSysDictionaryRuntimeService() {
		return sysDictionaryRuntimeService;
	}

	public void setSysDictionaryRuntimeService(
			SysDictionaryRuntimeService sysDictionaryRuntimeService) {
		this.sysDictionaryRuntimeService = sysDictionaryRuntimeService;
	}

	public String getColNames() {
		return colNames;
	}

	public void setColNames(String colNames) {
		this.colNames = colNames;
	}

	public String getColModel() {
		return colModel;
	}

	public void setColModel(String colModel) {
		this.colModel = colModel;
	}

	public int getCurrTodoPage() {
		return currTodoPage;
	}

	public void setCurrTodoPage(int currTodoPage) {
		this.currTodoPage = currTodoPage;
	}

	public int getTotalTodoPage() {
		return totalTodoPage;
	}

	public void setTotalTodoPage(int totalTodoPage) {
		this.totalTodoPage = totalTodoPage;
	}

	public int getCurrTaskPage() {
		return currTaskPage;
	}

	public void setCurrTaskPage(int currTaskPage) {
		this.currTaskPage = currTaskPage;
	}

	public int getTotalTaskPage() {
		return totalTaskPage;
	}

	public void setTotalTaskPage(int totalTaskPage) {
		this.totalTaskPage = totalTaskPage;
	}

	public int getPerTodoPage() {
		return perTodoPage;
	}

	public void setPerTodoPage(int perTodoPage) {
		this.perTodoPage = perTodoPage;
	}

	public PageBean getTodoListBean() {
		return todoListBean;
	}

	public void setTodoListBean(PageBean todoListBean) {
		this.todoListBean = todoListBean;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getDictionaryName() {
		return dictionaryName;
	} 

	public void setDictionaryName(String dictionaryName) {
		this.dictionaryName = dictionaryName;
	}
	public String getInsertscript() {
		return insertscript;
	}

	public void setInsertscript(String insertscript) {
		this.insertscript = insertscript;
	}

	public String getConditionsHtml() {
		return conditionsHtml;
	}

	public void setConditionsHtml(String conditionsHtml) {
		this.conditionsHtml = conditionsHtml;
	}

	public String getSearchscript() {
		return searchscript;
	}

	public void setSearchscript(String searchscript) {
		this.searchscript = searchscript;
	}

	public String getSubformkey() {
		return subformkey;
	}

	public void setSubformkey(String subformkey) {
		this.subformkey = subformkey;
	}
	public String getSubformid() {
		return subformid;
	}
	public void setSubformid(String subformid) {
		this.subformid = subformid;
	}

	public String getRowIdPrefix() {
		return rowIdPrefix;
	}

	public void setRowIdPrefix(String rowIdPrefix) {
		this.rowIdPrefix = rowIdPrefix;
	}

	public String getDictionaryUUID() {
		return dictionaryUUID;
	}

	public void setDictionaryUUID(String dictionaryUUID) {
		this.dictionaryUUID = dictionaryUUID;
	}


	public String getInitFieldScript() {
		return initFieldScript;
	}


	public Long getIsAutoShow() {
		return isAutoShow;
	}


	public void setIsAutoShow(Long isAutoShow) {
		this.isAutoShow = isAutoShow;
	}


	public String getToolbarHtml() {
		return toolbarHtml;
	}
	
}
