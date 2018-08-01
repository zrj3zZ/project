package com.iwork.app.navigation.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.navigation.directory.service.SysNavDirectoryService;
import com.iwork.app.navigation.function.service.SysNavFunctionService;
import com.iwork.app.navigation.sys.service.SysNavSystemService;
import com.iwork.app.navigation.web.service.SysNavFrameService;
import com.iwork.app.persion.model.SysPersonConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.lookandfeel.factory.LookAndFeelFactory;
import com.iwork.core.lookandfeel.interceptor.LookAndFeelInterface;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.WebBrowserUtil;
import com.opensymphony.xwork2.ActionSupport;
public class SysNavFrameAction extends ActionSupport{
    private HttpServletResponse response;  
	private SysNavDirectoryService sysNavDirectoryService;
	private SysNavSystemService sysNavSystemService;
	private SysNavFunctionService sysNavFunctionService;
	private SysNavFrameService sysNavFrameService;
	private List<HashMap<String,Object>> list;
	private int pageNumber;
	private int pageSize;
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}
	private List systemList;
	protected List directoryList;
	protected String systemid;
	protected String id ;
	protected String pid ;     //树ID
	protected String navtype ;     //导航类型
	protected String type ;     //导航类型
	protected String userid;
	protected String treescript;
	protected String dateStr;
	protected String currentUserStr;
	protected int unreadCount = 0;
	protected int todoCount = 0;
	protected int height ;
	protected String json = "";
	private String nodeType;
	private String nodeId;
	private String systemTitle;
	private String searchkey;
	private String navurl;
	private String skinsKey;
	private String tabkey;
	private String navuuid;
	private String topMenuHtml;
	private String browserType;  //浏览器类型
	private int sysCount;
	private int hycount;
	private int sxcount;
	private int gzsccount;
	private String roleid;
	private String ipaddress;
	private String lastTime;
	private String gzscformid;
	private String gzscdemid;
	private String gzschfformid;
	private String gzschfdemid;
	
	private String menukey;
	private String actionPath;
	private String leftMenuHtml;
	private String logoutUrl;
	private int totalNum; 
	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getGzschfformid() {
		return gzschfformid;
	}

	public void setGzschfformid(String gzschfformid) {
		this.gzschfformid = gzschfformid;
	}

	public String getGzschfdemid() {
		return gzschfdemid;
	}

	public void setGzschfdemid(String gzschfdemid) {
		this.gzschfdemid = gzschfdemid;
	}

	public String getGzscformid() {
		return gzscformid;
	}

	public void setGzscformid(String gzscformid) {
		this.gzscformid = gzscformid;
	}

	public String getGzscdemid() {
		return gzscdemid;
	}

	public void setGzscdemid(String gzscdemid) {
		this.gzscdemid = gzscdemid;
	}

	public int getGzsccount() {
		return gzsccount;
	}

	public void setGzsccount(int gzsccount) {
		this.gzsccount = gzsccount;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public int getSxcount() {
		return sxcount;
	}

	public void setSxcount(int sxcount) {
		this.sxcount = sxcount;
	}

	public int getHycount() {
		return hycount;
	}

	public void setHycount(int hycount) {
		this.hycount = hycount;
	}
	private boolean isdm;
	public int getSysCount() {
		return sysCount;
	}

	public void setSysCount(int sysCount) {
		this.sysCount = sysCount;
	}
	private int dbCount;

	public int getDbCount() {
		return dbCount;
	}

	public void setDbCount(int dbCount) {
		this.dbCount = dbCount;
	}
	private int fspCount;
	/**
	 * 获得首页
	 * @return
	 */
	public String getMainPage()throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map params = request.getParameterMap();
		//获取浏览器类型
		SysPersonConfig spc = UserContextUtil.getInstance().getCurrentUserConfig(SysPersonConfig.SYS_CONF_TYPE_SKINS_LAYOUT);
		if(spc!=null){
			skinsKey = spc.getValue();
			if(skinsKey==null||skinsKey.equals("")){
				skinsKey = "default";  
			}
		}else{
			skinsKey = "default";  
		}
		skinsKey = sysNavFrameService.getConfigUUID("skin");  
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
		List<HashMap> list = sysNavSystemService.getIpaddress(userid);
		ipaddress=list.size()==0?"":list.get(0).get("IPADDRESS").toString();
		lastTime=list.size()==0?"":list.get(0).get("LASTTIME").toString();
		roleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid().toString();
		sysCount=sysNavFrameService.getSysCount();
		dbCount=sysNavFrameService.getDbCount();
		isdm=sysNavFrameService.isDm();
		if(isdm){
			fspCount=sysNavFrameService.getXpxxCount();
		}else{
			fspCount=sysNavFrameService.getHyCount()+sysNavFrameService.getGzscCount()+sysNavFrameService.getXpxxCount();
		}
		LookAndFeelInterface impl = LookAndFeelFactory.getInstance(UserContextUtil.getInstance().getCurrentUserContext(),params, skinsKey);
		if(impl==null){
			 impl = LookAndFeelFactory.getInstance(UserContextUtil.getInstance().getCurrentUserContext(),params, "default");
		}
		systemList=sysNavSystemService.getMyNavSystemList();
		actionPath = impl.getActionPath();
		leftMenuHtml = impl.getLeftMenuHtml(systemid);
		topMenuHtml = impl.getTopMenuHtml(systemList);
		currentUserStr = impl.getCurrentUserInfoHtml();
		systemTitle = SystemConfig._iworkServerConf.getTitle(); 
	//	this.getSysTopList();
		String roleMode = "";
		if(SystemConfig._securityConf!=null){
			roleMode = SystemConfig._securityConf.getRole_mode();
		}
		
		//如果当前登录为SSO，则退出URL设置为SSO的退出地址
		if(SystemConfig._ssoLoginConf.getSsoMode().equals("on")){
			StringBuffer url = new StringBuffer();
			url.append(SystemConfig._ssoLoginConf.getSsoMode()).append("?service=").append(SystemConfig._ssoLoginConf.getSsoClient()).toString();
			logoutUrl = url.toString();
		}
		
		if(roleMode!=null&&roleMode.equals("on")){
			if(SystemConfig._securityConf.getAudit_owner().equals(userid)){
				return "audit";
			}else if(SystemConfig._securityConf.getSecurity_owner().equals(userid)){
				return "security";
			}else if(SystemConfig._securityConf.getSys_owner().equals(userid)){
				return SUCCESS;
			}else
			{
				return SUCCESS;
			}
		}else{
			return SUCCESS;
		}
	}

	/**
	 * 设置浏览器类型
	 */
	public void setWebBrowserType(){
		if(browserType!=null){
			boolean flag = WebBrowserUtil.getInstance().setWebBrowserType(browserType);
			if(flag){
				ResponseUtil.write(SUCCESS);
			}
		}
	}
	
	public String frame2Page() throws Exception{
		if(height==0){
			height=300;
		}
		topMenuHtml = sysNavFrameService.getTopMenuHtml(tabkey);
		systemTitle = SystemConfig._iworkServerConf.getTitle();
		this.getSysTopList();
		systemList=sysNavSystemService.getMyNavSystemList();
		leftMenuHtml = sysNavFrameService.getLeftMenuHtml(menukey, systemList);
		return SUCCESS;	
	}
	
	/**
	 * 获得待办事宜|系统消息个数
	 * @return
	 */
	public String showTipsInfo(){
		if(type!=null){
			String json = sysNavFrameService.showTipsInfo(type);
			ResponseUtil.write(json);
		}
		return null;
	}
	/**
	 * 获得待办事宜|系统消息个数
	 * @return
	 */
	public void showNumInfo(){
			String json = sysNavFrameService.showNumInfo();
			ResponseUtil.write(json);
	}
	public void getTodolist(){
		String json = sysNavFrameService.getTodolistTop10();
		ResponseUtil.write(json);
	}
	public void getSystemlist(){
		String json = sysNavFrameService.getMsgList();
		ResponseUtil.write(json);
	}
	// 获取邮件列表
	public void getEmaillist(){
		String json = sysNavFrameService.getEmailList();
		ResponseUtil.write(json);
	}
	/**
	 * 获得异步菜单目录
	 * @return
	 * @throws Exception
	 */
	public void openjson()throws Exception{
		String JsonData = "";
		nodeId = id;  
		if(nodeType==null)this.setNodeType("");
		if(null==nodeId||"".equals(nodeId))this.setNodeId("0");
		 
		if("SYS".equals(nodeType)||"NODE".equals(nodeType)){
			JsonData = sysNavFrameService.getTreeNodeJson(nodeId,nodeType);
		}else{
			JsonData = sysNavFrameService.getTreeSysJson(nodeId);
		} 
		this.setNodeId("");
		this.setNodeType(""); 
		ResponseUtil.write(JsonData);
	}
	
	public int getFspCount() {
		return fspCount;
	}

	public void setFspCount(int fspCount) {
		this.fspCount = fspCount;
	}

	/**
	 * 执行导航菜单查询操作
	 */
	public void doSearchNodeJSON(){
		if(searchkey!=null){
			String json =  sysNavFrameService.doNodeSearchJSON(searchkey);
			ResponseUtil.write(json);
		}
	}
	/**
	 * 获得首级菜单
	 * @return
	 * @throws Exception
	 */
	public String getSysTopList() throws Exception {
		UserContext userContext = UserContextUtil.getInstance().getCurrentUserContext();
		//证券宝改造版本 2014-5-6 modify for Daivd.Yang
		if(topMenuHtml==null){
			topMenuHtml = sysNavFrameService.getTopMenuHtml(tabkey);
		}
		this.setUserid(userContext._userModel.getUserid());
		this.setDateStr(sysNavSystemService.getCurrentDateStr());
		this.setCurrentUserStr(sysNavSystemService.getCurrentUserInfo(userContext));
		this.setUnreadCount(sysNavFrameService.getUnReadCount(userContext._userModel.getUserid()));
		int taskcount = sysNavFrameService.getWorkflowCount(userContext.get_userModel().getUserid());
		this.setTodoCount(taskcount);
		systemList=sysNavSystemService.getMyNavSystemList();
		return SUCCESS;	 
	}
	
	public String buildingInfo(){
		return SUCCESS;
	}
	
	
	/**
	 * 点击导航菜单转向到指定URL
	 * @return
	 */
	public String navForward(){
		navurl = "kmdoc_Index.action";
		
		return SUCCESS; 
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SysNavFunctionService getSysNavFunctionService() {
		return sysNavFunctionService;
	}
	public void setSysNavFunctionService(SysNavFunctionService sysNavFunctionService) {
		this.sysNavFunctionService = sysNavFunctionService;
	}

	public SysNavSystemService getSysNavSystemService() {
		return sysNavSystemService;
	}

	public List getSystemList() {
		return systemList;
	}

	public void setSysNavSystemService(SysNavSystemService sysNavSystemService) {
		this.sysNavSystemService = sysNavSystemService;
	}

	public SysNavDirectoryService getSysNavDirectoryService() {
		return sysNavDirectoryService;
	}

	public void setSysNavDirectoryService(
			SysNavDirectoryService sysNavDirectoryService) {
		this.sysNavDirectoryService = sysNavDirectoryService;
	}

	public List getDirectoryList() {
		return directoryList;
	}

	public void setDirectoryList(List directoryList) {
		this.directoryList = directoryList;
	}

	public String getSystemid() {
		return systemid;
	}

	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}

	public void setSystemList(List systemList) {
		this.systemList = systemList;
	}

	public SysNavFrameService getSysNavFrameService() {
		return sysNavFrameService;
	}

	public void setSysNavFrameService(SysNavFrameService sysNavFrameService) {
		this.sysNavFrameService = sysNavFrameService;
	}

	public String getTreescript() {
		return treescript;
	}

	public void setTreescript(String treescript) {
		this.treescript = treescript;
	}
	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getCurrentUserStr() {
		return currentUserStr;
	}

	public void setCurrentUserStr(String currentUserStr) {
		this.currentUserStr = currentUserStr;
	}
	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getNavtype() {
		return navtype;
	}

	public void setNavtype(String navtype) {
		this.navtype = navtype;
	}
	public int getTodoCount() {
		return todoCount;
	}
	public void setTodoCount(int todoCount) {
		this.todoCount = todoCount;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getSystemTitle() {
		return systemTitle;
	}

	public void setSystemTitle(String systemTitle) {
		this.systemTitle = systemTitle;
	}

	public String getSearchkey() {
		return searchkey;
	}

	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}

	public String getNavurl() {
		return navurl;
	}

	public void setNavurl(String navurl) {
		this.navurl = navurl;
	}

	public void setNavuuid(String navuuid) {
		this.navuuid = navuuid;
	}

	public String getSkinsKey() {
		return skinsKey;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getTopMenuHtml() {
		return topMenuHtml;
	}

	public String getTabkey() {
		return tabkey;
	}

	public void setTabkey(String tabkey) {
		this.tabkey = tabkey;
	}
	public String getLeftMenuHtml() {
		return leftMenuHtml;
	}

	public String getMenukey() {
		return menukey;
	}

	public void setMenukey(String menukey) {
		this.menukey = menukey;
	}

	public String getActionPath() {
		return actionPath;
	}
	public String getLogoutUrl() {
		return logoutUrl;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public boolean isIsdm() {
		return isdm;
	}

	public void setIsdm(boolean isdm) {
		this.isdm = isdm;
	}
	
	public String getHyfsp(){
		if(pageNumber==0){
			pageNumber=1;
		}
		if(pageSize==0){
			pageSize=10;
		}
		sxcount=sysNavFrameService.getXpxxCount();
		hycount=sysNavFrameService.getHyCount();
		gzsccount=sysNavFrameService.getGzscCount();
		list=sysNavFrameService.getHyfsp(pageNumber,pageSize);
		return SUCCESS;
	}
	
	public String getZyddfsp(){
		if(pageNumber==0){
			pageNumber=1;
		}
		if(pageSize==0){
			pageSize=10;
		}
		sxcount=sysNavFrameService.getSxCount();
		hycount=sysNavFrameService.getHyCount();
		gzsccount=sysNavFrameService.getGzscCount();
		list=sysNavFrameService.getZyddfsp(pageNumber,pageSize);
		return SUCCESS;
	}
	
	public String getXpxx(){
		if(pageNumber==0){
			pageNumber=1;
		}
		if(pageSize==0){
			pageSize=10;
		}
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc == null) { return ERROR;} //xlj 漏洞扫描 2018年5月15日11:08:39
		isdm = sysNavFrameService.isDm();
		sxcount=sysNavFrameService.getXpxxCount();
		hycount=sysNavFrameService.getHyCount();
		gzsccount=sysNavFrameService.getGzscCount();
		list=sysNavFrameService.getXpxxList(pageNumber,pageSize);
		return SUCCESS;
	}
	public void getdbsl(){
		sxcount=sysNavFrameService.getXpxxCount();
		hycount=sysNavFrameService.getHyCount();
		gzsccount=sysNavFrameService.getGzscCount();
		String sb=sxcount+","+hycount+","+gzsccount;
		ResponseUtil.write(sb);
	}
	
	public String getGzscfsp(){
		if(pageNumber==0){
			pageNumber=1;
		}
		if(pageSize==0){
			pageSize=10;
		}
		sxcount=sysNavFrameService.getXpxxCount();
		hycount=sysNavFrameService.getHyCount();
		gzsccount=sysNavFrameService.getGzscCount();
		gzscformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "FORMID");
		gzscdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查'", "ID");
		gzschfformid = DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "FORMID");
		gzschfdemid = DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='工作备查回复'", "ID");
		list=sysNavFrameService.getGzsc(pageNumber,pageSize);
		return SUCCESS;
	}
	public String getRxAll(){
		if(pageNumber==0){
			pageNumber=1;
		}
		if(pageSize==0){
			pageSize=10;
		}
		
		list=sysNavFrameService.getGzscrx(pageNumber,pageSize);
		totalNum=sysNavFrameService.getGzscrxSize().size();
		return SUCCESS;
	}
	public String showRclb(){
		if(pageNumber==0){
			pageNumber=1;
		}
		if(pageSize==0){
			pageSize=10;
		}
		
		try {
			list=sysNavFrameService.getshowRclb(pageNumber,pageSize);
			totalNum=sysNavFrameService.getshowRclbSize().size();
		} catch (Exception e) {
			
		}
		return SUCCESS;
	}
	public String delRctx(){
		
		try {
			sysNavFrameService.delRctx(id);
		} catch (Exception e) {
			
		}
		return null;
	}
}
