package com.iwork.webservice.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import com.iwork.core.engine.group.model.SysEngineGroup;
import com.iwork.core.util.ResponseUtil;
import com.iwork.webservice.constants.WebServiceConstants;
import com.iwork.webservice.model.InterfaceInfo;
import com.iwork.webservice.model.SysWsBaseinfo;
import com.iwork.webservice.model.SysWsParams;
import com.iwork.webservice.service.WebServiceService;
import com.opensymphony.xwork2.ActionSupport;

public class WebServiceAction extends ActionSupport {
	private WebServiceService webServiceService;
	private Long groupid;
	private int pid;
	private int id;
	private String groupname;
	private String ids;
	private String inorout;
	private SysWsBaseinfo model;
	private SysWsParams param;
	private List<SysWsBaseinfo> list;
	private List<String> checkTypeList;
	private List<String> contentTypeList;
	
	// 调用示例
	private String demoURL;
	private String demoJSON;
	private String demoXML;
	private List<InterfaceInfo> interfaceInfoList;
	
	/**
	 * 获得管理首页
	 * @return
	 */
	public String index(){
		return SUCCESS;
	}
	/**
	 * 获得分类树json
	 */
	public void showGroupJson(){
		String json = webServiceService.getTreeJson((long)pid);
		ResponseUtil.write(json);
	}
	/**
	 * 获得包含模型对象的树视图json
	 */
	public void showJson(){
		String json = webServiceService.getAllTreeJson((long)pid);
		ResponseUtil.write(json);
	} 
	/**
	 * 新建接口
	 * @return
	 */
	public String add(){
		if(groupid!=null){
			SysEngineGroup group = webServiceService.getSysEngineGroupDAO().getSysEngineGroupModel(groupid);
			if(group!=null){
				groupname = group.getGroupname();
			}
			if(model==null){
				model = new SysWsBaseinfo();
				model.setStatus(1);
				model.setWsType("1");
				model.setWsLevel("1");
				model.setGroupId(Integer.parseInt(groupid.toString()));
			}
			
		}
		return SUCCESS;
	}
	/**
	 * 编辑
	 * @return
	 */
	public String edit(){
		if(id!=0){
			model = webServiceService.getWebServiceDAO().getModel(id); 
			if(model!=null){
				// 验证方式
				String checkType = model.getCheckType();
				if(null != checkType && !"".equals(checkType)){
					String[] checkTypeArr = checkType.split(",");
					for(int i = 0; i < checkTypeArr.length ; i++){
						if(null == checkTypeList){
							checkTypeList = new ArrayList<String>();
						}
						
						checkTypeList.add(checkTypeArr[i]);
					}
				}
				
				SysEngineGroup group = webServiceService.getSysEngineGroupDAO().getSysEngineGroupModel((long)model.getGroupId());
				if(group!=null){
					groupname = group.getGroupname();
				}
			}
		}
		return SUCCESS;
	}
	/**
	 * 接口基本信息保存
	 */
	public void save(){
		if(model!=null){
			// 校验方式
			String checkType = "";
			for(String item:checkTypeList){
				if(null != checkType && !"".equals(checkType)){
					checkType = checkType + "," + item;
				}else{
					checkType = item;
				}
			}
			
			model.setCheckType(checkType);
			webServiceService.saveBaseInfo(model);
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	/**
	 * 接口基本信息保存
	 */
	public void delete(){
		if(id!=0){
			webServiceService.delete(id);
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	/**
	 * 保存输入输出参数
	 */
	public void paramSave(){
		if(param!=null){
			webServiceService.saveParamModel(param);
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	/**
	 * 获得导航树JSON
	 */
	public String showlist(){
		if(groupid!=null){
			list = webServiceService.showList(groupid);
		}
		return SUCCESS;
	}
	/**
	 * 参数定义首页
	 */
	public String paramIndex(){
		return SUCCESS;
	}
	
	/**
	 * 获得参数列表json
	 */
	public void showParamsJSON(){
		if(pid!=0&&inorout!=null){
			String jsonData = webServiceService.showParamsJSON(pid, inorout);
			
			ResponseUtil.write( jsonData.substring(1, jsonData.length()-1));	
		}
		
	}
	/**
	 * 输入参数主界面
	 * @return
	 */
	public String inputIndex(){
		return SUCCESS;
	}
	/**
	 * 输入参数录入界面
	 * @return
	 */
	public String addPage(){
		if(pid!=0&&inorout!=null){
			param = new SysWsParams();
			param.setPid(pid); 
			param.setInorout(inorout);
			param.setType(WebServiceConstants.WS_FIELD_TYPE_CHAR);
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑参数
	 * @return
	 */
	public String editPage(){
		
		if(id!=0&&pid!=0){
			param = webServiceService.getWebServiceDAO().getParamModel(id);
			model = webServiceService.getWebServiceDAO().getModel(pid);
		}
		return SUCCESS;
	}
	
	/**
	 * 输出参数主界面
	 * @return
	 */
	public String outputIndex(){
		return SUCCESS;
	}
	/**
	 * 输出参数录入界面
	 * @return
	 */
	public String outputPage(){
		if(pid!=0&&inorout!=null){
			param = new SysWsParams();
			param.setPid(pid); 
			param.setInorout(inorout);
			param.setType(WebServiceConstants.WS_FIELD_TYPE_CHAR);
		}
		return SUCCESS;
	}
	/**
	 * 删除
	 * @return
	 */
	public String paramDel(){
		if(ids!=null){
			String[] idstrs = ids.split(",");
			for(String idstr:idstrs){
				if(idstr==null)continue;
				int id = Integer.parseInt(idstr);
				if(id!=0){
					webServiceService.delParams(id);
				}
			}
		}
		ResponseUtil.writeTextUTF8("ok");
		return null;
	}
	
	/**
	 * 上移
	 * @return
	 */
	public String moveUp(){
		if(inorout!=null&&id!=0&&pid!=0){
			webServiceService.move("up",pid,inorout,id);
			ResponseUtil.writeTextUTF8("ok");
		}
		return null;
	}
	/**
	 * 下移
	 * @return
	 */
	public String moveDown(){
		if(inorout!=null&&id!=0&&pid!=0){
			webServiceService.move("down",pid,inorout,id);
			ResponseUtil.writeTextUTF8("ok");
		}
		return null;
	}
	
	/**
	 * 调用示例
	 * @return
	 */
	public String demo(){
		if(id!=0){
			String required = WebServiceConstants.CONST_MESSAGE_REQUIRED;
			
			String demoValue = "";
			model = webServiceService.getWebServiceDAO().getModel(id); 
			List<SysWsParams> listFirst = webServiceService.getInputParamsList(id);
			
			// JSON
			JSONObject jsonObject = new JSONObject();
			if(null != listFirst && listFirst.size() > 0){
				for(int i = 0;i < listFirst.size(); i++){
					SysWsParams paramFirst = listFirst.get(i);
					
					List<SysWsParams> listSecond = webServiceService.getInputParamsList(paramFirst.getId());
					
					if(null != listSecond && listSecond.size() > 0){
						JSONObject jsonObjectSecond = new JSONObject();
						for(int j = 0; null != listSecond && j < listSecond.size(); j++){
							SysWsParams paramSecond = listSecond.get(j);
							jsonObjectSecond.put(paramSecond.getName(), demoValue);
						}
						jsonObject.put(paramFirst.getName(), jsonObjectSecond);
					}else{
						jsonObject.put(paramFirst.getName(), demoValue);
					}
				}
			}
			
			demoJSON=jsonObject.toString();
			
			String xml = "";
			
			JSON json = JSONSerializer.toJSON(jsonObject.toString());  
	        XMLSerializer xmlSerializer = new XMLSerializer();  
	        xmlSerializer.setTypeHintsEnabled( false );      
	        xmlSerializer.setRootName("request");     
	        xml = xmlSerializer.write( json );
	        
	        /*
			// 添加其他信息
			JSONObject jsonObject_executeContent = new JSONObject();
			jsonObject_executeContent.put("arg0", userName);
			jsonObject_executeContent.put("arg1", userPwd);
			jsonObject_executeContent.put("arg2", UUID);
			jsonObject_executeContent.put("arg3", xml);
			
			JSONObject jsonObject_execute = new JSONObject();
			jsonObject_execute.put("execute", jsonObject_executeContent);
			
//			JSONObject jsonObject_body = new JSONObject();
//			jsonObject_body.put("soapenv:Body", jsonObject_execute);
//			
//			JSONObject jsonObject_header = new JSONObject();
//			jsonObject_header.put("soapenv:Header", "");
			
//			JSONObject jsonObject_root = new JSONObject();
//			jsonObject_root.put("soapenv:Envelope", jsonObject_header);
//			jsonObject_root.put("soapenv:Envelope", jsonObject_body);
			
			JSONObject jsonForXML = new JSONObject();
			jsonForXML.put("soapenv:Body", jsonObject_execute);
			jsonForXML.put("soapenv:Header", "");
	        xmlSerializer.setTypeHintsEnabled( false );      
	        xmlSerializer.setRootName("soapenv:Envelope");     
	        xml = xmlSerializer.write( jsonForXML );    
	        
	        */
	        
			demoXML = xml;
			
			if(WebServiceConstants.WS_TYPE_COMMON.equals(model.getWsType())){
				HttpServletRequest request = ServletActionContext.getRequest();
				request.getLocalAddr();
				request.getServerPort();
				demoURL = "http://"+ request.getLocalAddr() +":" + request.getServerPort() + WebServiceConstants.COMMON_WEB_SERVICE_URL+"?WSDL";
			}else{
				demoURL = model.getUrl();
			}
			
			interfaceInfoList = new ArrayList<InterfaceInfo>();
			
			// URL
			InterfaceInfo interfaceInfo = new InterfaceInfo();
			interfaceInfo.setDescription("请求地址");
			interfaceInfo.setName("WebService地址");
			interfaceInfo.setValue(demoURL);
			interfaceInfoList.add(interfaceInfo);
			
			interfaceInfo = new InterfaceInfo();
			interfaceInfo.setDescription("请求方法名称");
			interfaceInfo.setName("请求方法名称");
			interfaceInfo.setValue("execute");
			interfaceInfoList.add(interfaceInfo);
			
			interfaceInfo = new InterfaceInfo();
			interfaceInfo.setDescription("请求方法参数：用户名");
			interfaceInfo.setName("arg0");
			interfaceInfo.setValue(model.getUsername());
			interfaceInfoList.add(interfaceInfo);
			
			interfaceInfo = new InterfaceInfo();
			interfaceInfo.setDescription("请求方法参数：密码");
			interfaceInfo.setName("arg1");
			interfaceInfo.setValue(model.getPassword());
			interfaceInfoList.add(interfaceInfo);
			
			interfaceInfo = new InterfaceInfo();
			interfaceInfo.setDescription("请求方法参数：UUID");
			interfaceInfo.setName("arg2");
			interfaceInfo.setValue(model.getUuid());
			interfaceInfoList.add(interfaceInfo);
			
			interfaceInfo = new InterfaceInfo();
			interfaceInfo.setDescription("请求方法参数：输入参数");
			interfaceInfo.setName("arg3");
			interfaceInfo.setValue("");
			interfaceInfoList.add(interfaceInfo);
			
			String paramType = "输入参数";
			if(null != listFirst && listFirst.size() > 0){
				for(int i = 0;i < listFirst.size(); i++){
					SysWsParams paramFirst = listFirst.get(i);
					
					List<SysWsParams> listSecond = webServiceService.getInputParamsList(paramFirst.getId());
					if(null != listSecond && listSecond.size() > 0){
						interfaceInfo = new InterfaceInfo();
						interfaceInfo.setDescription(paramType+"；格式："+paramFirst.getType()+"；"+((WebServiceConstants.YES+"").equals(paramFirst.getRequired())?(WebServiceConstants.CONST_MESSAGE_REQUIRED+"；"):""));
						interfaceInfo.setName(paramFirst.getName());
						interfaceInfo.setValue(demoValue);
						interfaceInfoList.add(interfaceInfo);
						
						for(int j = 0; null != listSecond && j < listSecond.size(); j++){
							SysWsParams paramSecond = listSecond.get(j);
							
							interfaceInfo = new InterfaceInfo();
							interfaceInfo.setDescription(paramType+"；格式："+paramSecond.getType()+"；"
									+((WebServiceConstants.YES+"").equals(paramSecond.getRequired())?(WebServiceConstants.CONST_MESSAGE_REQUIRED+"；"):"")
									+"父节点："+paramFirst.getName()+"；");
							interfaceInfo.setName(paramSecond.getName());
							interfaceInfo.setValue(demoValue);
							interfaceInfoList.add(interfaceInfo);
						}
					}else{
						interfaceInfo = new InterfaceInfo();
						interfaceInfo.setDescription(paramType+"；格式："+paramFirst.getType()+"；"+((WebServiceConstants.YES+"").equals(paramFirst.getRequired())?(WebServiceConstants.CONST_MESSAGE_REQUIRED+"；"):""));
						interfaceInfo.setName(paramFirst.getName());
						interfaceInfo.setValue(demoValue);
						interfaceInfoList.add(interfaceInfo);
					}
				}
			}
			
			paramType = "输出参数";
			listFirst = webServiceService.getOutputParamsList(id);
			if(null != listFirst && listFirst.size() > 0){
				for(int i = 0;i < listFirst.size(); i++){
					SysWsParams paramFirst = listFirst.get(i);
					
					List<SysWsParams> listSecond = webServiceService.getOutputParamsList(paramFirst.getId());
					if(null != listSecond && listSecond.size() > 0){
						interfaceInfo = new InterfaceInfo();
						interfaceInfo.setDescription(paramType+"；格式："+paramFirst.getType()+"；"+((WebServiceConstants.YES+"").equals(paramFirst.getRequired())?(WebServiceConstants.CONST_MESSAGE_REQUIRED+"；"):""));
						interfaceInfo.setName(paramFirst.getName());
						interfaceInfo.setValue(demoValue);
						interfaceInfoList.add(interfaceInfo);
						
						for(int j = 0; null != listSecond && j < listSecond.size(); j++){
							SysWsParams paramSecond = listSecond.get(j);
							
							interfaceInfo = new InterfaceInfo();
							interfaceInfo.setDescription(paramType+"；格式："+paramSecond.getType()+"；"
									+((WebServiceConstants.YES+"").equals(paramSecond.getRequired())?(WebServiceConstants.CONST_MESSAGE_REQUIRED+"；"):"")
									+"父节点："+paramFirst.getName()+"；");
							interfaceInfo.setName(paramSecond.getName());
							interfaceInfo.setValue(demoValue);
							interfaceInfoList.add(interfaceInfo);
						}
					}else{
						interfaceInfo = new InterfaceInfo();
						interfaceInfo.setDescription(paramType+"；格式："+paramFirst.getType()+"；"+((WebServiceConstants.YES+"").equals(paramFirst.getRequired())?(WebServiceConstants.CONST_MESSAGE_REQUIRED+"；"):""));
						interfaceInfo.setName(paramFirst.getName());
						interfaceInfo.setValue(demoValue);
						interfaceInfoList.add(interfaceInfo);
					}
				}
			}
		}
		
		
		
		/*if(id!=0){
			model = webServiceService.getWebServiceDAO().getModel(id); 
			if(model!=null){
				// 验证方式
				String checkType = model.getCheckType();
				if(null != checkType && !"".equals(checkType)){
					String[] checkTypeArr = checkType.split(",");
					for(int i = 0; i < checkTypeArr.length ; i++){
						if(null == checkTypeList){
							checkTypeList = new ArrayList<String>();
						}
						
						checkTypeList.add(checkTypeArr[i]);
					}
				}
				
				SysEngineGroup group = webServiceService.getSysEngineGroupDAO().getSysEngineGroupModel((long)model.getGroupId());
				if(group!=null){
					groupname = group.getGroupname();
				}
			}
		}*/
		return SUCCESS;
	}
	
	public void setwebServiceService(
			WebServiceService webServiceService) {
		this.webServiceService = webServiceService;
	}
	public Long getGroupid() {
		return groupid;
	}
	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public SysWsBaseinfo getModel() {
		return model;
	}
	public void setModel(SysWsBaseinfo model) {
		this.model = model;
	}
	public List<SysWsBaseinfo> getList() {
		return list;
	}
	public List<String> getCheckTypeList() {
		return checkTypeList;
	}
	public void setCheckTypeList(List<String> checkTypeList) {
		this.checkTypeList = checkTypeList;
	}
	public List<String> getContentTypeList() {
		return contentTypeList;
	}
	public void setContentTypeList(List<String> contentTypeList) {
		this.contentTypeList = contentTypeList;
	}
	public String getDemoURL() {
		return demoURL;
	}
	public void setDemoURL(String demoURL) {
		this.demoURL = demoURL;
	}
	public String getDemoJSON() {
		return demoJSON;
	}
	public void setDemoJSON(String demoJSON) {
		this.demoJSON = demoJSON;
	}
	public String getDemoXML() {
		return demoXML;
	}
	public void setDemoXML(String demoXML) {
		this.demoXML = demoXML;
	}
	public List<InterfaceInfo> getInterfaceInfoList() {
		return interfaceInfoList;
	}
	public void setInterfaceInfoList(List<InterfaceInfo> interfaceInfoList) {
		this.interfaceInfoList = interfaceInfoList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInorout() {
		return inorout;
	}
	public void setInorout(String inorout) {
		this.inorout = inorout;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) { 
		this.pid = pid;
	}

	public SysWsParams getParam() {
		return param;
	}
	public void setParam(SysWsParams param) {
		this.param = param;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
}
