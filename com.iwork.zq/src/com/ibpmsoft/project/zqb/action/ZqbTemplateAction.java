package com.ibpmsoft.project.zqb.action;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibpmsoft.project.zqb.service.ZqbTemplateService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 会议模板的Action
 * @author 
 *
 */
public class ZqbTemplateAction extends ActionSupport{


	private static final long serialVersionUID = -339931587092611450L; 
	private String id;
	private Long instanceid;
	private String companyno;
	private String companyname;
	private String templatename;
	private int flag;
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getTemplatename() {
		return templatename;
	}

	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}

	//会议类型
	private String hylx;
	//模板类型
	private String mblx;
	//节点类型
	private String nodeType;
	private List<HashMap> list;
	private String content;
	private String TYPE;
	
	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private ZqbTemplateService zqbTemplateService;
	
	public ZqbTemplateService getzqbTemplateService() {
		return zqbTemplateService;
	}

	public void setZqbTemplateService(ZqbTemplateService zqbTemplateService) {
		this.zqbTemplateService = zqbTemplateService;
	}


	/**
	 * 进入模板管理
	 * @return
	 */
	public String index(){
		return SUCCESS;
	}
	
	/**
	 * 构建模板类别树
	 */
	public void showTemplateJSON(){
		String str = zqbTemplateService.getTemplateTreeJson(id,nodeType);
		ResponseUtil.write(str); 
	}
	
	/**
	 * 删除模板分类
	 */
	public void deleteTemplateType(){
		boolean flag = false;
		if(instanceid!=null && id!=null){
			flag = zqbTemplateService.deleteTemplateType(instanceid,id);
		}
		if(flag){
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	
	/**
	 * 根据类型查询模板
	 * @return
	 */
	public String queryTemplateList(){
		list = zqbTemplateService.queryTemplatesByType(id,templatename);
		try {
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			if(user.getOrgroleid()==3){
				flag=0;
			}else{
				flag=1;
			}
		} catch (Exception e) {
			flag=1;
		}
		return SUCCESS;
	}
	
	public String queryTemplateListByTname(){
		if(templatename != null){
			if(checkLoginInfo(templatename)){
	    		return ERROR;
	    	}
			list = zqbTemplateService.queryTemplatesByTname(templatename);
		}
		return SUCCESS;
	}
	private static boolean checkLoginInfo(String info) {
    	if(info==null||info.equals("")){
    		return true;
    	}else{
    		String regEx = " and | exec | count | chr | mid | master | or | truncate | char | declare | join |insert |select |delete |update |create |drop ";
    		//Pattern pattern = Pattern.compile(regEx);
    		// 忽略大小写的写法
    		Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(info.trim());
    		// 字符串是否与正则表达式相匹配
    		
    		String regEx2="[`“”~!#$^%&*,+<>?）\\]\\[（—\"{};']";
    		Pattern pattern2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
    		// 字符串是否与正则表达式相匹配
    		Matcher matcher2 = pattern2.matcher(info.trim());
    		
    		int n = 0;
    		if(matcher.find()){
    			n++;
    		}
    		if(matcher2.find()){
    			n++;
    		}
    		if(n==0){
    			return false;
    		}else{
    			return true;
    		}
    	}
	}
	/**
	 * 删除模板
	 */
	public void deleteTemplate(){
		boolean flag = false;
		if(instanceid!=null && id!=null){
			flag = zqbTemplateService.deleteTemplate(instanceid,id);
		}
		if(flag){
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	
	/**
	 * 模板树界面
	 * @return
	 */
	public String tempplateTree(){
		return SUCCESS;
	}
	
	public String batchTemplate(){
		return SUCCESS;
	}
	
	/**
	 * 构建模板树
	 */
	public void showTemplateTreeJSON(){
		String str = zqbTemplateService.getTemplateTreeAllJson(id,nodeType);
		ResponseUtil.write(str); 
	}
	
	public void batchTemplateSave(){
		boolean flag=zqbTemplateService.batchTemplateSave(content);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	/**
	 * 判断是否存在默认模板
	 */
	public void validateIsDef(){
		boolean flag = zqbTemplateService.validateIsDef(instanceid,hylx,mblx);
		ResponseUtil.write(flag+""); 
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public List<HashMap> getList() {
		return list;
	}

	public void setList(List<HashMap> list) {
		this.list = list;
	}

	public String getCompanyno() {
		return companyno;
	}

	public void setCompanyno(String companyno) {
		this.companyno = companyno;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getHylx() {
		return hylx;
	}

	public void setHylx(String hylx) {
		this.hylx = hylx;
	}

	public String getMblx() {
		return mblx;
	}

	public void setMblx(String mblx) {
		this.mblx = mblx;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	} 
	
	public String loadPage(){
		content=zqbTemplateService.getMb(instanceid);
		return SUCCESS;
	}
}
