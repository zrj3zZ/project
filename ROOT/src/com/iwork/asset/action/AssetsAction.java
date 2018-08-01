package com.iwork.asset.action;

import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.iwork.asset.constant.StatusConstant;
import com.iwork.asset.service.AssetsService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 固定资产管理模块测试使用
 * 
 * @author caoxufeng
 * 
 */
public class AssetsAction extends ActionSupport {
	
	//资产管理服务
	private AssetsService assetsService;
	//页面显示条数
	private int pageSize = 10; 
	//合计数量
	private int total;
	//当前页码
	private int pageNumber=1;
	//结果集
	private List<HashMap> list;
	private Long instanceid;
	private String taskId;
	private String excutionId;
	private String no;
	private String actDefId;
	/**
	 * 我的资产卡片列表信息
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public String myCardList() {
		
		int startRow = 0;
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if (pageNumber > 1) {
			startRow = (pageNumber - 1) * pageSize;
		}
		
		String userId = UserContextUtil.getInstance().getCurrentUserId();
		list = assetsService.getAssetsCardList(userId,pageSize, startRow);
		total = assetsService.getAssetsCardSize(userId);
		
		return SUCCESS;
	}
	
	/**
	 * 打开流程页面
	 * @return
	 * @author yanglianfeng
	 */
	public String showFlowPage(){ 	
		boolean flag = false;
		HashMap<String,Object> newHashMap=new HashMap<String,Object>();
		HashMap<String, Object> paramsMap = new HashMap<String,Object>();
		Long formId = null;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		ProcessAPI processAPI = ProcessAPI.getInstance();
		formId = processAPI.getProcessDefaultFormId(actDefId);
		instanceid = processAPI.newInstance(actDefId,formId, userid);//获取新的流程实例ID
		Task task = processAPI.newTaskId(instanceid);
		if(task!=null){
			taskId = task.getId();
			excutionId = task.getExecutionId();
		}  
		//获取查询条件（资源卡片编号）
		HashMap conditionMap=new HashMap();
		conditionMap.put("NO", no);
		List<HashMap> list=DemAPI.getInstance().getAllList(StatusConstant.ZYKP_UUID, conditionMap, null);
		//将资源卡片编号获取的详细信息插入到流程中注意：其流程表单的字段与卡片字段相一致，否则要重新装载HashMap
		HashMap<String,Object> hash=list.get(0);
		//获取固定资产卡片编号
		String cardno=hash.get("NO")==null?"":hash.get("NO").toString();
		//获取固定资产卡片名称
		String name=hash.get("NAME")==null?"":hash.get("NAME").toString();
		//获取使用人账号
		String ouid=hash.get("OUID")==null?"":hash.get("OUID").toString();
		//获取使用人名称
		String ouname=hash.get("OUNAME")==null?"":hash.get("OUNAME").toString();
		//获取资产类别
		String category=hash.get("CATEGORY")==null?"":hash.get("CATEGORY").toString();
		//获取类别编号
		String categoryno=hash.get("CATEGORYNO")==null?"":hash.get("CATEGORYNO").toString();
		//获取计量单位
		String unit=hash.get("UNIT")==null?"":hash.get("UNIT").toString();
		//获取规格
		String specification=hash.get("SPECIFICATION")==null?"":hash.get("SPECIFICATION").toString();
		//获取型号
		String models=hash.get("MODELS")==null?"":hash.get("MODELS").toString();
		//获取ABC分类
		String abc=hash.get("ABC")==null?"":hash.get("ABC").toString();
		//获取主要配置说明
		String configure=hash.get("CONFIGURE")==null?"":hash.get("CONFIGURE").toString();
		//获取资产介绍
		String description=hash.get("DESCRIPTION")==null?"":hash.get("DESCRIPTION").toString();
		//获取立卡日期
		String carddate=hash.get("CARDDATE")==null?"":hash.get("CARDDATE").toString();
		//添加卡片编号
		newHashMap.put("CARDNO", cardno);
		//添加卡片名称
		newHashMap.put("NAME", name);
		//添加规格
		newHashMap.put("SPECIFICATION", specification);
		//添加单位
		newHashMap.put("UNIT", unit);
		//添加型号
		newHashMap.put("MODELS", models);
		//获取主要配置说明
		//newHashMap.put("MODELS", models);
		flag=processAPI.saveFormData(actDefId, instanceid, newHashMap, false);
		return SUCCESS;
	}

	
	
	
	
	public String staffIndex(){
		
		return SUCCESS;
	}
	
	
	public AssetsService getAssetsService() {
		return assetsService;
	}

	public void setAssetsService(AssetsService assetsService) {
		this.assetsService = assetsService;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public List<HashMap> getList() {
		return list;
	}

	public void setList(List<HashMap> list) {
		this.list = list;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExcutionId() {
		return excutionId;
	}

	public void setExcutionId(String excutionId) {
		this.excutionId = excutionId;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
}
