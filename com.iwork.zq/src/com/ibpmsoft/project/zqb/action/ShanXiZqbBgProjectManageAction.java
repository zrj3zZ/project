package com.ibpmsoft.project.zqb.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.service.ShanXiZqbBgProjectManageService;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ShanXiZqbBgProjectManageAction extends ActionSupport{
	/**
	 * 版本
	 */
	private static final long serialVersionUID = 1L;
	
	private ShanXiZqbBgProjectManageService shanXiZqbBgProjectManageService;
	
	public String index() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		formid=shanXiZqbBgProjectManageService.getConfig("bgxmxxformid");
		demId=shanXiZqbBgProjectManageService.getConfig("bgxmxxdemid");
		HashMap<String,List<HashMap<String, Object>>> listMap=shanXiZqbBgProjectManageService.getListMap(jyf,jydsf,czbm,cyrxm,runPageNumber,runPageSize,closePageNumber,closePageSize);
		bgRunList=listMap.get("RunList");
		bgCloseList=listMap.get("CloseList");
		if(!bgRunList.isEmpty()){
			runTotalNum=Integer.parseInt(bgRunList.get(0).get("CNUM").toString());
		}
		if(!bgCloseList.isEmpty()){
			closeTotalNum=Integer.parseInt(bgCloseList.get(0).get("CNUM").toString());
		}
		return SUCCESS;
	}
	
	public String cwIndex() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		formid=shanXiZqbBgProjectManageService.getConfig("cwgwxxformid");
		demId=shanXiZqbBgProjectManageService.getConfig("cwgwxxdemid");
		HashMap<String,List<HashMap<String, Object>>> listMap=shanXiZqbBgProjectManageService.getCwListMap(customername,clbm,czbm,cyrxm,runPageNumber,runPageSize,closePageNumber,closePageSize);
		bgRunList=listMap.get("RunList");
		bgCloseList=listMap.get("CloseList");
		if(!bgRunList.isEmpty()){
			runTotalNum=Integer.parseInt(bgRunList.get(0).get("CNUM").toString());
		}
		if(!bgCloseList.isEmpty()){
			closeTotalNum=Integer.parseInt(bgCloseList.get(0).get("CNUM").toString());
		}
		return SUCCESS;
	}
	/**
	 * 其他项目管理
	 * @return
	 */
	private String cuid;
	public String qtIndex() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		String qtDfid = DBUtil.getString("SELECT ID||'#'||FORMID DFID FROM SYS_DEM_ENGINE WHERE TITLE='其他项目'", "DFID");
		demId=Long.parseLong(qtDfid.split("#")[0]);
		formid=Long.parseLong(qtDfid.split("#")[1]);
		runList=shanXiZqbBgProjectManageService.getQtRunList(customername,clbm,czbm,cyrxm,runPageNumber,runPageSize);
		runTotalNum=shanXiZqbBgProjectManageService.getQtRunListSize(customername,clbm,czbm,cyrxm);
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		 cuid = user.getUserid();
		closeList=shanXiZqbBgProjectManageService.getQtCloseList(customername,clbm,czbm,cyrxm,closePageNumber,closePageSize);
		closeTotalNum=shanXiZqbBgProjectManageService.getQtCloseListSize(customername,clbm,czbm,cyrxm);
		
		return SUCCESS;
	}
	
	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public void qtClosePro(){
		System.out.println(instanceIdStr);
		String info = shanXiZqbBgProjectManageService.closeQtProject(instanceIdStr);
		if(info.equals("")){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write(info);
		}
	}
	private String username;
	private Long orgroleid;
	private String owner;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getOrgroleid() {
		return orgroleid;
	}

	public void setOrgroleid(Long orgroleid) {
		this.orgroleid = orgroleid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getQtJdContent(){
		String xmlxDFid =DBUtil.getString("SELECT ID||'#'||FORMID DFID FROM SYS_DEM_ENGINE WHERE TITLE='项目立项'", "DFID"); 
		String gzjdhbDFid = DBUtil.getString("SELECT ID||'#'||FORMID DFID FROM SYS_DEM_ENGINE WHERE TITLE='工作进度汇报'", "DFID");
		String zlgdDFid = DBUtil.getString("SELECT ID||'#'||FORMID DFID FROM SYS_DEM_ENGINE WHERE TITLE='资料归档'", "DFID");
		xmxldemid = xmlxDFid.split("#")[0];
		xmxlformid = xmlxDFid.split("#")[1];
		gzjdhbdemid = gzjdhbDFid.split("#")[0];
		gzjdhbformid = gzjdhbDFid.split("#")[1];
		zlgddemid = zlgdDFid.split("#")[0];
		zlgdformid = zlgdDFid.split("#")[1];
		String dfid_ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='其他初步尽调'", "DFID");
		if(dfid_.contains("#")){
		cbjddemid = dfid_.split("#")[0];
		cbjdformid = dfid_.split("#")[1];
		}
		taskList=shanXiZqbBgProjectManageService.getQtTaskList(xmbh);
		username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		orgroleid=(long) 0;
		if(username.equals(owner)||username.equals("杨光")||username.equals("吕红贞")||username.equals("刘慧珍")){
			orgroleid=(long) 5;	
			
		}
		return SUCCESS;
	}
	
	public void getMainContent(){
		String jsonString=shanXiZqbBgProjectManageService.getMainContent(xmbh);
		ResponseUtil.write(jsonString);
	}
	
	public String getJdContent(){
		xmlcServer=ProcessAPI.getInstance().getProcessActDefId("BGXMLXSPLC");
		fazlbsServer=ProcessAPI.getInstance().getProcessActDefId("BGFAZLBS");
		sbzlServer=ProcessAPI.getInstance().getProcessActDefId("BGSBZLSPLC");
		zlgdServer=ProcessAPI.getInstance().getProcessActDefId("BGZLGDSPLC");
		taskList=shanXiZqbBgProjectManageService.getTaskList(xmbh);
		return SUCCESS;
	}
	
	public void closeBgProject(){
		String info = shanXiZqbBgProjectManageService.closeBgProject(instanceIdStr);
		if(info.equals("")){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write(info);
		}
	}
	
	public void closeCwProject(){
		String info = shanXiZqbBgProjectManageService.closeCwProject(instanceIdStr);
		if(info.equals("")){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write(info);
		}
	}
	
	public String getCwJdContent(){
		ybcwXmlxServer=ProcessAPI.getInstance().getProcessActDefId("CWXMLXSPLC");
		ybcwGzjdhbServer=ProcessAPI.getInstance().getProcessActDefId("CWGZJDHB");
		ybcwZlgdServer=ProcessAPI.getInstance().getProcessActDefId("CWZLGDSPLC");
		taskList=shanXiZqbBgProjectManageService.getCwTaskList(xmbh);
		return SUCCESS;
	}
	
	private int runPageNumber; // 当前页数
	private int runTotalNum; // 总页数
	private int runPageSize = 10; // 每页条数
	private int closePageNumber; // 当前页数
	private int closeTotalNum; // 总页数
	private int closePageSize = 10; // 每页条数
	private List<HashMap<String, Object>> bgRunList;
	private List<HashMap<String, Object>> bgCloseList;
	private List<HashMap<String, Object>> taskList;
	private String jyf;
	private String jydsf;
	private String czbm;
	private String clbm;
	private String cyrxm;
	private int ymid;
	private String xmbh;
	private String xmlcServer;
	private String fazlbsServer;
	private String sbzlServer;
	private String zlgdServer;
	private String ybcwXmlxServer;
	private String ybcwGzjdhbServer;
	private String ybcwZlgdServer;
	private String instanceIdStr;
	private String customername;
	private Long formid;
	private Long demId;
	private String  xmmc;
	private String  thxmlc;
	private String  startdate;
	private String  enddate;
	private String  gjjd;
	private String  scbk;
	private int pageNumber = 1; // 当前页数
	private int pageSize = 10; // 每页条数
	private int totalNum; 
	private List<HashMap> list;
	private Long instanceid;
	private String actStepDefId;
	
	private List<HashMap> runList;
	private List<HashMap> closeList;
	private String xmxlformid;
	private String xmxldemid;
	private String gzjdhbformid;
	private String gzjdhbdemid;
	private String zlgdformid;
	private String zlgddemid;
	private String cbjddemid;
	private String cbjdformid;
	
	public String getCbjddemid() {
		return cbjddemid;
	}

	public void setCbjddemid(String cbjddemid) {
		this.cbjddemid = cbjddemid;
	}

	public String getCbjdformid() {
		return cbjdformid;
	}

	public void setCbjdformid(String cbjdformid) {
		this.cbjdformid = cbjdformid;
	}

	public String getXmxlformid() {
		return xmxlformid;
	}

	public void setXmxlformid(String xmxlformid) {
		this.xmxlformid = xmxlformid;
	}

	public String getXmxldemid() {
		return xmxldemid;
	}

	public void setXmxldemid(String xmxldemid) {
		this.xmxldemid = xmxldemid;
	}

	public String getGzjdhbformid() {
		return gzjdhbformid;
	}

	public void setGzjdhbformid(String gzjdhbformid) {
		this.gzjdhbformid = gzjdhbformid;
	}

	public String getGzjdhbdemid() {
		return gzjdhbdemid;
	}

	public void setGzjdhbdemid(String gzjdhbdemid) {
		this.gzjdhbdemid = gzjdhbdemid;
	}

	public String getZlgdformid() {
		return zlgdformid;
	}

	public void setZlgdformid(String zlgdformid) {
		this.zlgdformid = zlgdformid;
	}

	public String getZlgddemid() {
		return zlgddemid;
	}

	public void setZlgddemid(String zlgddemid) {
		this.zlgddemid = zlgddemid;
	}

	public List<HashMap> getRunList() {
		return runList;
	}

	public void setRunList(List<HashMap> runList) {
		this.runList = runList;
	}

	public List<HashMap> getCloseList() {
		return closeList;
	}

	public void setCloseList(List<HashMap> closeList) {
		this.closeList = closeList;
	}

	public String getActStepDefId() {
		return actStepDefId;
	}

	public void setActStepDefId(String actStepDefId) {
		this.actStepDefId = actStepDefId;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getGjjd() {
		return gjjd;
	}

	public void setGjjd(String gjjd) {
		this.gjjd = gjjd;
	}

	public String getScbk() {
		return scbk;
	}

	public void setScbk(String scbk) {
		this.scbk = scbk;
	}

	public List<HashMap> getList() {
		return list;
	}

	public void setList(List<HashMap> list) {
		this.list = list;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
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
	
	public String getThxmlc() {
		return thxmlc;
	}

	public void setThxmlc(String thxmlc) {
		this.thxmlc = thxmlc;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	public void setShanXiZqbBgProjectManageService(
			ShanXiZqbBgProjectManageService shanXiZqbBgProjectManageService) {
		this.shanXiZqbBgProjectManageService = shanXiZqbBgProjectManageService;
	}

	public List<HashMap<String, Object>> getBgRunList() {
		return bgRunList;
	}

	public void setBgRunList(List<HashMap<String, Object>> bgRunList) {
		this.bgRunList = bgRunList;
	}

	public List<HashMap<String, Object>> getBgCloseList() {
		return bgCloseList;
	}

	public void setBgCloseList(List<HashMap<String, Object>> bgCloseList) {
		this.bgCloseList = bgCloseList;
	}

	public String getJyf() {
		return jyf;
	}

	public void setJyf(String jyf) {
		this.jyf = jyf;
	}

	public String getJydsf() {
		return jydsf;
	}

	public void setJydsf(String jydsf) {
		this.jydsf = jydsf;
	}

	public String getCzbm() {
		return czbm;
	}

	public void setCzbm(String czbm) {
		this.czbm = czbm;
	}

	public String getCyrxm() {
		return cyrxm;
	}

	public void setCyrxm(String cyrxm) {
		this.cyrxm = cyrxm;
	}

	public int getYmid() {
		return ymid;
	}

	public void setYmid(int ymid) {
		this.ymid = ymid;
	}

	public List<HashMap<String, Object>> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<HashMap<String, Object>> taskList) {
		this.taskList = taskList;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getXmlcServer() {
		return xmlcServer;
	}

	public void setXmlcServer(String xmlcServer) {
		this.xmlcServer = xmlcServer;
	}

	public int getRunPageNumber() {
		return runPageNumber;
	}

	public void setRunPageNumber(int runPageNumber) {
		this.runPageNumber = runPageNumber;
	}

	public int getRunTotalNum() {
		return runTotalNum;
	}

	public void setRunTotalNum(int runTotalNum) {
		this.runTotalNum = runTotalNum;
	}

	public int getRunPageSize() {
		return runPageSize;
	}

	public void setRunPageSize(int runPageSize) {
		this.runPageSize = runPageSize;
	}

	public int getClosePageNumber() {
		return closePageNumber;
	}

	public void setClosePageNumber(int closePageNumber) {
		this.closePageNumber = closePageNumber;
	}

	public int getCloseTotalNum() {
		return closeTotalNum;
	}

	public void setCloseTotalNum(int closeTotalNum) {
		this.closeTotalNum = closeTotalNum;
	}

	public int getClosePageSize() {
		return closePageSize;
	}

	public void setClosePageSize(int closePageSize) {
		this.closePageSize = closePageSize;
	}

	public String getFazlbsServer() {
		return fazlbsServer;
	}

	public void setFazlbsServer(String fazlbsServer) {
		this.fazlbsServer = fazlbsServer;
	}

	public String getSbzlServer() {
		return sbzlServer;
	}

	public void setSbzlServer(String sbzlServer) {
		this.sbzlServer = sbzlServer;
	}

	public String getZlgdServer() {
		return zlgdServer;
	}

	public void setZlgdServer(String zlgdServer) {
		this.zlgdServer = zlgdServer;
	}

	public String getInstanceIdStr() {
		return instanceIdStr;
	}

	public void setInstanceIdStr(String instanceIdStr) {
		this.instanceIdStr = instanceIdStr;
	}

	public Long getFormid() {
		return formid;
	}

	public void setFormid(Long formid) {
		this.formid = formid;
	}

	public Long getDemId() {
		return demId;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public String getYbcwXmlxServer() {
		return ybcwXmlxServer;
	}

	public void setYbcwXmlxServer(String ybcwXmlxServer) {
		this.ybcwXmlxServer = ybcwXmlxServer;
	}

	public String getYbcwGzjdhbServer() {
		return ybcwGzjdhbServer;
	}

	public void setYbcwGzjdhbServer(String ybcwGzjdhbServer) {
		this.ybcwGzjdhbServer = ybcwGzjdhbServer;
	}

	public String getYbcwZlgdServer() {
		return ybcwZlgdServer;
	}

	public void setYbcwZlgdServer(String ybcwZlgdServer) {
		this.ybcwZlgdServer = ybcwZlgdServer;
	}

	public String getClbm() {
		return clbm;
	}

	public void setClbm(String clbm) {
		this.clbm = clbm;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}
	//加载投行项目列表页
		public String openthxm(){
			thxmlc = ProcessAPI.getInstance().getProcessActDefId("TXXMGLLC");
			//获取当前登录用户角色id
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String orgRoleId = uc._userModel.getUserid();
				if (pageNumber == 0)
					pageNumber = 1;
			xmmc=this.xmmc;
			startdate=this.startdate;
			enddate=this.enddate;
			gjjd=this.gjjd;
			scbk=this.scbk;
			list = shanXiZqbBgProjectManageService.getThxmList(xmmc,startdate,enddate,gjjd,scbk,pageSize, pageNumber,orgRoleId,thxmlc);
			totalNum=shanXiZqbBgProjectManageService.getThxmListSize(xmmc,startdate,enddate,gjjd,scbk,thxmlc);
			return SUCCESS;
		}
		//根据投行项目名称判断是否存在重复数据
		public void thxmBeforsave(){
			boolean flag = false;
			if (!"".equals(xmmc.trim())) {
				flag = shanXiZqbBgProjectManageService.thxmBeforsave(xmmc.trim(),instanceid==null?0:instanceid);
			}
			if (flag) {
				ResponseUtil.write(SUCCESS);
			} else {
				ResponseUtil.write(ERROR);
			}
		}
		//投行项目信息导出
		public String thxmexportexcl() {
			thxmlc = ProcessAPI.getInstance().getProcessActDefId("TXXMGLLC");
			HttpServletResponse response = ServletActionContext.getResponse();
			shanXiZqbBgProjectManageService.thxmexportexcl(response,xmmc,startdate,enddate,gjjd,scbk,thxmlc);
			return null;
		}
		//获取投行项目经办人
		public void hqthxmjbr(){
			String xmjbr="";
			HashMap  conmap = new HashMap();
			conmap.put("XMMC", xmmc.trim());
			List<HashMap> list = DemAPI.getInstance().getList(shanXiZqbBgProjectManageService.getConfigUUID("thxmuuid"), conmap, null);
			for(HashMap map:list){
				xmjbr=map.get("ZBSCBJBR").toString().split("\\[")[0];
			}
			ResponseUtil.write(xmjbr);
		}
		//删除投行项目
		public void removeexport(){
			if(instanceid!=null){ 
				Long formId = DemAPI.getInstance().getFormIdByInstanceId(
						new Long(instanceid), EngineConstants.SYS_INSTANCE_TYPE_DEM);
				HashMap hash = DemAPI.getInstance().getFromData(instanceid, formId,
						EngineConstants.SYS_INSTANCE_TYPE_DEM);
				String smsContent = "";
				String sysMsgContent = "";
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String value=hash.get("XMMC").toString().trim();
				Long dataId=Long.parseLong(hash.get("ID").toString());
				if(hash!=null){
				    smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.EVENT_REMOVE_KEY, hash); 
					sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.EVENT_REMOVE_KEY, hash);
					LogUtil.getInstance().addLog(dataId, "投行部融资项目基本信息维护", "删除投行项目："+value);
				}
				String customerno = "";
				if(hash.get("XMMC")!=null){
					customerno = hash.get("XMMC").toString();
				}
				String userid = hash.get("QCRID").toString();
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				UserContext target = UserContextUtil.getInstance().getUserContext(userid); 
				if(target!=null){
					if(!smsContent.equals("")){
						String mobile = target.get_userModel().getMobile();
						if(mobile!=null&&!mobile.equals("")){
							MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
						}
					}
					if(!sysMsgContent.equals("")){ 
							MessageAPI.getInstance().sendSysMsg(userid, "投行部融资项目删除提醒", sysMsgContent);
					}
				}
				
				HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
				String yxid = fromData.get("YXID").toString();
				Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(yxid));
				if(newTaskId!=null){
					String taskId = newTaskId.getId();
					String owner = newTaskId.getOwner();
					ProcessAPI.getInstance().setTaskAssignee(taskId, owner);
					//删除物理表数据
					DemAPI.getInstance().removeFormData(instanceid);
					boolean removeProcessInstance = ProcessAPI.getInstance().removeProcessInstance(taskId, owner);
				}
				//删除流程表数据
				ProcessAPI.getInstance().removeFormData(Long.parseLong(hash.get("LCINSTANCEID").toString()));
				ResponseUtil.write(SUCCESS);
			}else{
				ResponseUtil.write(ERROR);
			}
			
		}
	 public void isFirstnode(){
		 String xmmcs=xmmc.trim();
		 HashMap<String,Object> dataList=new HashMap<String,Object>();
		 dataList=shanXiZqbBgProjectManageService.getMap(xmmcs);
		/*StringBuffer sql=new StringBuffer();
		sql.append(" select * from ( ");
		sql.append(" select z.userid,z.username from orguser z where z.departmentid= ? ");
		sql.append(" (select s.departmentid from orguser s left join BD_XP_TXRZGLLCB t on s.userid=t.qcrid where t.xmmc=?) ");
		sql.append("  and z.Ismanager=1 order by z.id desc) where rownum=1; ");
			Connection conn = null;
			PreparedStatement pss = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				pss.setString(1,xmmcs );
				rs = pss.executeQuery();
				while (rs.next()) {
					HashMap<String,Object> result = new HashMap<String,Object>();
					String userid = rs.getString("userid");
					String username = rs.getString("username");
					result.put("userid", userid);
					result.put("username", username);
				}
			} catch (Exception e) {
			} finally{
				DBUtil.close(conn, pss, rs);
			}*/
		 
		
		 
		 String isShowKF = "";
		 String thxmjdid=shanXiZqbBgProjectManageService.getConfigUUID("dyjd");
		 HashMap conditionMap = new HashMap();
		 conditionMap.put("XMMC", xmmc.trim());
		 List list=DemAPI.getInstance().getList(shanXiZqbBgProjectManageService.getConfigUUID("thxmuuid"), conditionMap, null);
		 if(actStepDefId.equals(thxmjdid)&&list.size()==0){
				isShowKF = shanXiZqbBgProjectManageService.getConfigUUID("thxmcsr");
				 if(dataList!=null && dataList.size()>0){
					 String userid=(String) dataList.get("userid");
					 String username=(String) dataList.get("username");
					 String userAll=userid+"["+username+"]";
					 isShowKF=isShowKF+","+userAll;
				 }
		}
		
		 ResponseUtil.write(isShowKF);
	 }
		
}
