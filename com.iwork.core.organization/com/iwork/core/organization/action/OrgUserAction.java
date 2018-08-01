package com.iwork.core.organization.action;
import java.io.File;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibpmsoft.project.zqb.service.ZqbUpdateDataService;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.constant.UserStateConst;
import com.iwork.core.organization.constant.UserTypeConst;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgCompanyService;
import com.iwork.core.organization.service.OrgDepartmentService;
import com.iwork.core.organization.service.OrgRoleService;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.opensymphony.xwork2.ActionSupport;
/**
 * @author David.Yang
 */
public class OrgUserAction extends ActionSupport {

	private static final Logger logger = LoggerFactory.getLogger(OrgUserAction.class);

	private OrgUserService orgUserService;
	private OrgRoleService orgRoleService;
	private OrgDepartmentService orgDepartmentService;
	private OrgCompanyService orgCompanyService;
	private ZqbUpdateDataService zqbUpdateDataService;
	
	private OrgUser model;
	protected Long departmentid;
	protected String departmentname;
	protected Collection availableItems;
	protected List companyList;
	private File upFile ;   //上传excel文件
	protected String sendType ;
	protected String searchkey ;
	protected String id ;
	protected String pid;
	protected String q;
	protected String term;
	protected String queryMap;
	protected String navTree;
	protected String delInfo;
	protected String info;
	protected String nodeType;
	private String companyId;
	protected Collection userMapList;//兼职人员List 
	protected List userList;
	protected List<OrgDepartment> deptList;
	private String userImgPath;
	private String isUserImageExists;
	protected String userid ;
	protected String username ;
	protected String roleid ;
	protected String postsid;
	protected String postsname;
	protected String email;
	protected Long orderindex;
	protected String searchKey;
	protected String customerNO;
	public String getCustomerNO() {
		return customerNO;
	}

	public void setCustomerNO(String customerNO) {
		this.customerNO = customerNO;
	}

	/**
	 * 获得设计列表
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		//返回赋值
		if(departmentid==null){
			if(this.getModel()!=null){
				this.setDepartmentid(this.getModel().getDepartmentid());
			}
			
		}
		if(!"".equals(departmentid)&&departmentid!=null){
			    deptList = orgDepartmentService.getOrgDepartmentDAO().getSubDepartmentList(departmentid);
				availableItems=orgUserService.getDeptAllUserList(departmentid);
				userMapList = orgUserService.getOrgUserMapList(departmentid);
		}
		return SUCCESS;	
	}	
	
	/**
	 * 编辑加载用户信息
	 * @return
	 * @throws Exception 
	 */
	public String load() throws Exception {
		roleid=UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid()+"";
		if(userid!=null&&!userid.equals("")){
			model = orgUserService.getUserModel(userid);
			if(model!=null){
				this.setDepartmentid(model.getDepartmentid());				
				this.setDepartmentname(model.getDepartmentname());
				String imageFileName = userid+".jpg";//判断用户头像是否存在
				String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getUserPhotoPath()) + "/" + imageFileName;
			    File userImage = new File(path);
			    if(!userImage.exists()){
			    	this.setUserImgPath("iwork_img/default_userImg.jpg");
			    	this.setIsUserImageExists("0");
			    }else{
			    	//如果用户头像存在,将用户头像的路径传到前台
			    	String imgPath = SystemConfig._fileServerConf.getUserPhotoPath()+"/"+imageFileName;
			    	this.setUserImgPath(imgPath);
			    	this.setIsUserImageExists("1");
			    }
			}
		}else{
			id=orgUserService.getMaxID();
		}
		availableItems = orgRoleService.getAll();
	    return SUCCESS;
	}
	
	/**
	 * 悬停显示用户详细信息
	 * @return
	 * @throws Exception
	 */
	public String tip() throws Exception{
		if(userid!=null){
			model = orgUserService.getUserModel(userid);
			String imageFileName = userid+".jpg";//判断用户头像是否存在
			String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getUserPhotoPath()) + "/" + imageFileName;
		    File userImage = new File(path);
		    if(!userImage.exists()){
		    	this.setUserImgPath("iwork_img/default_userImg.jpg");
		    	this.setIsUserImageExists("0");
		    }else{
		    	//如果用户头像存在,将用户头像的路径传到前台
		    	String imgPath = SystemConfig._fileServerConf.getUserPhotoPath()+"/"+imageFileName;
		    	this.setUserImgPath(imgPath);
		    	this.setIsUserImageExists("1");
		    }
		    return SUCCESS;
		}else{
			return ERROR;
		}
	}
	
	
	/**
	 * 获得指定组织的全部活跃用户JSON
	 * @return
	 */
	public void loadAllUserJson(){
		Map map = ServletActionContext.getRequest().getParameterMap();
		if(q!=null){
			String userJson = null;
			if(companyId==null){
				companyId = UserContextUtil.getInstance().getCurrentUserContext().get_companyModel().getCompanyno(); 
			}
			userJson = orgUserService.loadAllUserJson(companyId,q); 
			if(userJson!=null){
				//因为主表保存是无刷新保存，不做页面跳转，所以return null.用response返回执行结果
				JSONArray obj  = JSONArray.fromObject(userJson); 
				ResponseUtil.write(obj.toString()); 
			}
		}
	}
	/**
	 * 添加子用户信息
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		id=orgUserService.getMaxID();
		model = null;
		userid = null;
		availableItems = orgRoleService.getAll();
		if(departmentid!=null&&!"".equals(departmentid)){
			OrgDepartment deptmodel = (OrgDepartment)orgDepartmentService.getBoData(departmentid);
			if(deptmodel!=null){
				this.setDepartmentname(deptmodel.getDepartmentname());
				this.setCustomerNO(deptmodel.getDepartmentno());
				this.setUserImgPath("iwork_img/default_userImg.jpg");
				this.setIsUserImageExists("0");
				String startdate = UtilDate.getNowdate();
				java.util.Date sdate = UtilDate.StringToDate(startdate, "yyyy-MM-dd");
				String enddate = "9999-12-31";
				java.util.Date edate = UtilDate.StringToDate(enddate, "yyyy-MM-dd");
				model = new OrgUser();
				model.setStartdate(sdate);
				model.setEnddate(edate);
				model.setOrderindex(orderindex);
				return SUCCESS;
			}
		}
		 return ERROR;
	} 
	
	/**
	 * 执行用户注销
	 * @return
	 */
	public void disable(){
		String msg = "";
		if(this.getModel()!=null)
			this.setUserid(this.getModel().getUserid());
			this.setDepartmentid(this.getModel().getDepartmentid());
		if(userid!=null){
			orgUserService.disable(userid);
			msg = SUCCESS;
		}else{
			msg = ERROR;
		}
		ResponseUtil.write(msg);
	}
	/**
	 * 执行用户激活
	 * @return
	 */
	public void activating(){
		String msg = "";
		if(this.getModel()!=null)
			this.setUserid(this.getModel().getUserid());
			this.setDepartmentid(this.getModel().getDepartmentid());
		if(userid!=null){
			orgUserService.activating(userid);
			msg = SUCCESS;
		}else{
			msg = ERROR;
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 执行用户解锁
	 * @return
	 */
	public void unlock(){
		String msg = "";
		if(this.getModel()!=null)
			this.setUserid(this.getModel().getUserid());
			this.setDepartmentid(this.getModel().getDepartmentid());
		if(userid!=null){
			orgUserService.unlock(userid);
			msg = SUCCESS;
		}else{
			msg = ERROR;
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 执行查询
	 * @return
	 */
	
	public String searchUser(){

		

		if(searchkey!=null){
			if(checkLoginInfo(searchkey)){
	    		return ERROR;
	    	}
			availableItems = orgUserService.getOrgUserDAO().doSearch(departmentid, searchkey);
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
	 * 账户密码初始化
	 * @return
	 */
	public void sendpwd(){ 
		String msg = "";
		if(userid!=null&&sendType!=null){
			msg = orgUserService.initPWD(userid);
		}else{
			msg = ERROR;
		}
		ResponseUtil.write(msg);
	}
	
	/**
	 * 快速添加用户
	 */
	public void quickAddUser(){
		StringBuffer log = new StringBuffer();
		OrgUser model= new OrgUser();
		if(userid==null)log.append("用户账号为空").append("\n");
		if(username==null)log.append("用户姓名为空").append("\n");
		if(roleid==null)log.append("角色ID为空").append("\n");
		model.setUserid(userid);
		model.setUsername(username);
		model.setOrgroleid(Long.parseLong(roleid));
		model.setDepartmentid(departmentid);
		model.setDepartmentname(departmentname);
		model.setPostsid(postsid);
		model.setPostsname(postsname); 
		model.setEmail(email); 
		model.setUsertype(UserTypeConst.USER_TYPE_ORGUSER);
		model.setUserstate(UserStateConst.ORGUSER_STATE_ACTIVE); 
		//根据部门ID
		if(departmentid!=null){
			OrgDepartment orgDepartment = orgDepartmentService.getBoData(departmentid);
			if(orgDepartment!=null){
				model.setCompanyid(Long.parseLong(orgDepartment.getCompanyid()));
				OrgCompany orgCompany = orgCompanyService.getBoData(orgDepartment.getCompanyid());
				if(orgCompany!=null){
					model.setCompanyname(orgCompany.getCompanyname());
				}else{
					log.append("单位名称未找到").append("\n");
				}
			}else{
				log.append("部门信息未找到").append("\n");
			}
		}else{
			log.append("部门ID为空").append("\n");
		}
		if(log.toString().equals("")){
			OrgUser orguser = orgUserService.getUserModel(userid);
			if(orguser==null){
				orgUserService.addBoData(model);
				log.append(SUCCESS);
			}else{
				log.append("当前用户已存在").append("\n");
			}
		}
		ResponseUtil.write(log.toString());
	}
	/**
	 * 执行用户信息保存
	 * @return
	 * @throws Exception
	 */
	public void save() throws Exception {
		
		String flag="";
		 if(this.getModel().getMobile()!=null && !"".equals(this.getModel().getMobile())){
			 Pattern p = Pattern.compile("^1\\d{10}$");
			 if(!p.matcher(this.getModel().getMobile()).matches()){
				 flag="model";
			 }
		}else if(this.getModel().getEmail()!=null && !"".equals(this.getModel().getEmail())){
			 String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"; 
			 Pattern p = Pattern.compile(pattern1);
			 if(!p.matcher(this.getModel().getEmail()).matches()){
				 flag="email";
			 }
		}else if(this.getModel().getStartdate()==null || "".equals(this.getModel().getStartdate())){
			flag="startisnull";
		}else if(this.getModel().getEnddate()==null || "".equals(this.getModel().getEnddate())){
			flag="endisnull";
		}else if(this.getModel().getUserid()!=null && !"".equals(this.getModel().getUserid())){
			if(this.getModel().getUserid().length()>20 || this.getModel().getUserid().length()<2){
				flag="zLength";
			}else{
				Pattern p = Pattern.compile("[0-9A-Za-z_]*");
				 if(!p.matcher(this.getModel().getUserid()).matches()){
					 flag="zhangh";
				 }
			}
				 
			
			
		}
		 if("".equals(flag)){
			 if(model!=null&&model.getId()!=null){
				
					OrgUser model=orgUserService.getUserModel(this.getModel().getUserid().toUpperCase().trim());
					this.getModel().setPassword(model.getPassword());
					this.getModel().setUserid(model.getUserid().toUpperCase());
					/*this.getModel().setExtend2(model.getDepartmentname());
					Map params=new HashMap();
					params.put(1, model.getDepartmentid());
					String departmentNo=DBUTilNew.getDataStr("departmentno", "select s.departmentno from orgdepartment s where s.id=?", params);
					if(departmentNo!=null && !"".equals(departmentNo)){
						this.getModel().setExtend1(departmentNo);
					}*/
					orgUserService.updateBoData(this.getModel());
					LogUtil.getInstance().addLog(this.model.getId(), "用户信息", "更新用户信息："+this.model.getUserid()+"["+this.model.getUsername()+"]");
					if(zqbUpdateDataService==null){
						zqbUpdateDataService = (ZqbUpdateDataService)SpringBeanUtil.getBean("zqbUpdateDataService");
					}
					zqbUpdateDataService.updateDataOrgUser(model, this.getModel());
					 ResponseUtil.write(SUCCESS);
				
			}else{
				
				//加载初始化密码
				OrgUser model=orgUserService.getUserModel(this.getModel().getUserid().toUpperCase().trim());
				if(model==null){
					if(this.getModel().getPassword()==null||"".equals(this.getModel().getPassword())){			
						String salt = ShaSaltUtil.getStringSalt();
						this.getModel().setExtend3(salt);
						this.getModel().setPassword(ShaSaltUtil.getEncryptedPwd(SystemConfig._iworkServerConf.getUserDefaultPassword(),salt,true));
						this.getModel().setUserstate(new Long(0));
					}
					String useridTemp = this.getModel().getUserid();
					this.getModel().setUserid(useridTemp.toUpperCase().trim());
					orgUserService.addBoData(this.getModel());
					LogUtil.getInstance().addLog(this.model.getId(), "用户信息", "新增用户信息："+this.model.getUserid()+"["+this.model.getUsername()+"]");
					this.setDepartmentid(this.getModel().getDepartmentid());
					 ResponseUtil.write(SUCCESS);
				}else{
					ResponseUtil.write(ERROR);
				}
				
			
			}
		 }else{
			 ResponseUtil.write(flag);
		 }
	}
	public void user_customerNo(){
		Map params=new HashMap();
		params.put(1,departmentid);
		String departmentNo=DBUTilNew.getDataStr("departmentno", "select s.departmentno from orgdepartment s where s.id=?", params);
		if(departmentNo!=null && !"".equals(departmentNo)){
			 ResponseUtil.write(departmentNo);
		}
	}
	/**
	 * 删除数据
	 * @return
	 * @throws Exception
	 */
	public void delete()throws Exception{
		boolean flag = false;
		delInfo = "";//清空信息
		if(this.getId() != null){
			OrgUser model = orgUserService.getModel(this.getId());
			this.setDepartmentid(model.getDepartmentid());
			if(model != null ){
				boolean temp = orgUserService.deleteBoData(model);
				if(temp){
					flag = true;
					LogUtil.getInstance().addLog(model.getId(), "用户信息", "删除用户信息："+model.getUserid()+"["+model.getUsername()+"]");
					delInfo = "1";
				}else{
					flag = false;
					delInfo = "2";
				}
			}else{
				flag = false;
				delInfo = "3";
			}
		}else{
			flag = false;
			delInfo = "4";
		}
		if(flag){
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	
	/**
	 * 加载导航树
	 * @return
	 * @throws Exception
	 */
	public String showTree() throws Exception{
		navTree = orgUserService.getNavTreeScript();
		return SUCCESS;
	}
	/**
	 * 加载导航树JSON
	 * @return
	 */
	public void showTree_Json() throws Exception{
		if(searchKey!=null && !searchKey.equals("")){
			this.searchKey = URLDecoder.decode(searchKey,"UTF-8");
		}
		boolean isRoot = false;
		if(id==null||id.equals("")){id = "0";}
		if(nodeType==null)nodeType = "company"; 
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(companyId==null){
			companyId = uc.get_companyModel().getId(); 
			isRoot = true;
		} 
		String JsonData =orgUserService.getTreeJson(companyId,id+"",nodeType,isRoot,searchKey);
		ResponseUtil.write(JsonData); 
	}
	
	
	public String loadTree(){
		String json = "";
		if(pid==null)pid = "0";
		json = orgUserService.getOrgUserTree(Long.parseLong(pid));
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("orgUserTreeJson", json);
		return SUCCESS;
	}
	
	/**
	 * 兼任角色列表
	 * @return
	 */
	public String getOrgUserMapList(String userid){
		
		return SUCCESS;
	}
	
	
	/**
	 * 执行下载
	 */
	public void tmpDownload(){
		HttpServletResponse response = ServletActionContext.getResponse();
		orgUserService.exportOrgExcelTemplate(response);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public String doExcelImp(){
		String msg = "";
		if(upFile!=null){
			msg = orgUserService.doExcelImp(upFile);
		} 
		ResponseUtil.write(msg);
		return null; 
	}
	/**
	 * 向上置顶
	 * @return
	 * @throws Exception
	 */
	public String moveTop() throws Exception {
		if("".equals(id))id = orgUserService.getMaxID();
		OrgUser model = orgUserService.getModel(id);
		if(model!=null){
			departmentid = model.getDepartmentid();
		}
		orgUserService.moveTop(departmentid, Integer.parseInt(id));
		if(departmentid==null&&model!=null){
			departmentid = model.getDepartmentid();
		}
		return SUCCESS;
	}
	
	/**
	 * 向上置底
	 * @return
	 * @throws Exception
	 */
	public String moveBottem() throws Exception {
		if("".equals(id))id = orgUserService.getMaxID();
		OrgUser model = orgUserService.getModel(id);
		if(model!=null){
			departmentid = model.getDepartmentid();
		}
		orgUserService.moveBottem(departmentid, Integer.parseInt(id));
		if(departmentid==null&&model!=null){
			departmentid = model.getDepartmentid();
		}
		return SUCCESS;
	}
	/**
	 * 向上移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveUp() throws Exception {
		if("".equals(id))id = orgUserService.getMaxID();
		OrgUser model = orgUserService.getModel(id);
		if(model!=null){
			departmentid = model.getDepartmentid();
		}
		orgUserService.moveUp(departmentid, Integer.parseInt(id));
		if(departmentid==null&&model!=null){
			departmentid = model.getDepartmentid();
		}
		return SUCCESS;
	}
	/**
	 * 向下移动位置
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		if("".equals(id))id = orgUserService.getMaxID();
		OrgUser model = orgUserService.getModel(id);
		if(model!=null){
			departmentid = model.getDepartmentid();
		}
		orgUserService.moveDown(departmentid, Integer.parseInt(id));
		if(departmentid==null&&model!=null){
			departmentid = model.getDepartmentid();
		}
		return SUCCESS;
	}
	//###############################################################################
	//##             SET/GET方法
	//###############################################################################

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}


	public OrgUser getModel() {
		return model;
	}

	public void setModel(OrgUser model) {
		this.model = model;
	}

	public Collection getAvailableItems() {
		return availableItems;
	}

	public void setAvailableItems(Collection availableItems) {
		this.availableItems = availableItems;
	}

	public List getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List companyList) {
		this.companyList = companyList;
	}

	public String getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(String queryMap) {
		this.queryMap = queryMap;
	}

	public Long getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(Long departmentid) {
		this.departmentid = departmentid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNavTree() {
		return navTree;
	}

	public void setNavTree(String navTree) {
		this.navTree = navTree;
	}

	public OrgDepartmentService getOrgDepartmentService() {
		return orgDepartmentService;
	}

	public void setOrgDepartmentService(OrgDepartmentService orgDepartmentService) {
		this.orgDepartmentService = orgDepartmentService;
	}

	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public String getDelInfo() {
		return delInfo;
	}

	public void setDelInfo(String delInfo) {
		this.delInfo = delInfo;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Collection getUserMapList() {
		return userMapList;
	}

	public void setUserMapList(Collection userMapList) {
		this.userMapList = userMapList;
	}

	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getUserImgPath() {
		return userImgPath;
	}

	public void setUserImgPath(String userImgPath) {
		this.userImgPath = userImgPath;
	}

	public String getIsUserImageExists() {
		return isUserImageExists;
	}

	public void setIsUserImageExists(String isUserImageExists) {
		this.isUserImageExists = isUserImageExists;
	}

	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public List getUserList() {
		return userList;
	}

	public String getSearchkey() {
		return searchkey;
	}

	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getPostsid() {
		return postsid;
	}

	public void setPostsid(String postsid) {
		this.postsid = postsid;
	}

	public String getPostsname() {
		return postsname;
	}

	public void setPostsname(String postsname) {
		this.postsname = postsname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOrgCompanyService(OrgCompanyService orgCompanyService) {
		this.orgCompanyService = orgCompanyService;
	}

	public List<OrgDepartment> getDeptList() {
		return deptList;
	}

	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public Long getOrderindex() {
		return orderindex;
	}

	public void setOrderindex(Long orderindex) {
		this.orderindex = orderindex;
	}
	
	
}
