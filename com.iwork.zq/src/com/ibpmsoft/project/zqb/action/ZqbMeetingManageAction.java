package com.ibpmsoft.project.zqb.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.service.ZqbMeetingManageService;
import com.ibpmsoft.project.zqb.upload.FileUploadUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.StringUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.appointment.model.AppointmentNum;
import com.iwork.plugs.appointment.model.AppointmentYYSX;
import com.iwork.plugs.meeting.util.UtilDate;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbMeetingManageAction extends ActionSupport {
	private HttpServletRequest request;
	private ZqbMeetingManageService zqbMeetingManageService;
	private List<HashMap> runList;
	private List<HashMap> finishList;
	private List<HashMap<String, Object>> dataList;
	private int dataListNum;
	private AppointmentNum appointmentNum;
	private AppointmentYYSX appointmentYysx;
	private Long yys;
	private Long yysx;
	private String roleCid;
	private Long sxid;
	private String sftz;
	private String plantime;
	private String uuid;
	private Long directoryId;
	private Long filesize;
	private String filename;
	private String yyrq;
	private String kflbid;
	private String userID;
	private String KFLB;
	private String CJZH;
	private String FS;
	private String name; 
	private String sszjj; 
	private String szbm; 
	private String gpdq;
	private String gprqks;
	private String gprqjs;
	private String ssbm;
	private String gpzt;
	private String zczt;
	private String startdate;
	private String enddate;
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

	public String getGpzt() {
		return gpzt;
	}

	public void setGpzt(String gpzt) {
		this.gpzt = gpzt;
	}

	public String getZczt() {
		return zczt;
	}

	public void setZczt(String zczt) {
		this.zczt = zczt;
	}

	private List<HashMap> listxjxzq;
	
	public List<HashMap> getListxjxzq() {
		return listxjxzq;
	}

	public void setListxjxzq(List<HashMap> listxjxzq) {
		this.listxjxzq = listxjxzq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSszjj() {
		return sszjj;
	}

	public void setSszjj(String sszjj) {
		this.sszjj = sszjj;
	}

	public String getSzbm() {
		return szbm;
	}

	public void setSzbm(String szbm) {
		this.szbm = szbm;
	}

	public String getGpdq() {
		return gpdq;
	}

	public void setGpdq(String gpdq) {
		this.gpdq = gpdq;
	}

	public String getGprqks() {
		return gprqks;
	}

	public void setGprqks(String gprqks) {
		this.gprqks = gprqks;
	}

	public String getGprqjs() {
		return gprqjs;
	}

	public void setGprqjs(String gprqjs) {
		this.gprqjs = gprqjs;
	}

	public String getSsbm() {
		return ssbm;
	}

	public void setSsbm(String ssbm) {
		this.ssbm = ssbm;
	}

	private static Logger logger = Logger.getLogger(ZqbMeetingManageAction.class);
	public String getFS() {
		return FS;
	}

	public void setFS(String  fS) {
		FS = fS;
	}

	public String getKFLB() {
		return KFLB;
	}

	public void setKFLB(String kFLB) {
		KFLB = kFLB;
	}

	public String getCJZH() {
		return CJZH;
	}

	public void setCJZH(String cJZH) {
		CJZH = cJZH;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getKflbid() {
		return kflbid;
	}

	public void setKflbid(String kflbid) {
		this.kflbid = kflbid;
	}

	public String getYyrq() {
		return yyrq;
	}

	public void setYyrq(String yyrq) {
		this.yyrq = yyrq;
	}

	public List<HashMap<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<HashMap<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public int getDataListNum() {
		return dataListNum;
	}

	public void setDataListNum(int dataListNum) {
		this.dataListNum = dataListNum;
	}

	public Long getDirectoryId() {
		return directoryId;
	}

	public void setDirectoryId(Long directoryId) {
		this.directoryId = directoryId;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPlantime() {
		return plantime;
	}

	public void setPlantime(String plantime) {
		this.plantime = plantime;
	}

	public String getSftz() {
		return sftz;
	}

	public void setSftz(String sftz) {
		this.sftz = sftz;
	}

	public Long getSxid() {
		return sxid;
	}

	public void setSxid(Long sxid) {
		this.sxid = sxid;
	}

	public String getRoleCid() {
		return roleCid;
	}

	public AppointmentYYSX getAppointmentYysx() {
		return appointmentYysx;
	}

	public void setAppointmentYysx(AppointmentYYSX appointmentYysx) {
		this.appointmentYysx = appointmentYysx;
	}

	public void setRoleCid(String roleCid) {
		this.roleCid = roleCid;
	}

	public AppointmentNum getAppointmentNum() {
		return appointmentNum;
	}

	public void setAppointmentNum(AppointmentNum appointmentNum) {
		this.appointmentNum = appointmentNum;
	}

	public Long getYys() {
		return yys;
	}

	public void setYys(Long yys) {
		this.yys = yys;
	}

	public Long getYysx() {
		return yysx;
	}

	public void setYysx(Long yysx) {
		this.yysx = yysx;
	}

	public String getCxdd() {
		return cxdd;
	}

	public ZqbMeetingManageService getZqbMeetingManageService() {
		return zqbMeetingManageService;
	}

	private List<HashMap> customerList;
	public List<HashMap> getSxmsList() {
		return sxmsList;
	}

	public void setSxmsList(List<HashMap> sxmsList) {
		this.sxmsList = sxmsList;
	}

	private List<HashMap> sxmsList;
	private List<HashMap> cxddList;
	public List<HashMap> getCxddList() {
		return cxddList;
	}

	public void setCxddList(List<HashMap> cxddList) {
		this.cxddList = cxddList;
	}

	private HashMap hash;
	private String projectNo;
	private String json;
	private String type;
	private String meetpro;
	public String getMeetpro() {
		return meetpro;
	}

	public void setMeetpro(String meetpro) {
		this.meetpro = meetpro;
	}

	public String getMeetyear() {
		return meetyear;
	}

	public void setMeetyear(String meetyear) {
		this.meetyear = meetyear;
	}

	private String meetyear;
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private Long id;
	private Long instanceid;
	private Long taskid;
	private String startDate;
	private String endDate;
	private String typePieData;
	private String typePieData2;
	private String typePieData3;
	private String typeBarData;
	private String typeBarLabel;
	private boolean readonly;
	private boolean superman;
	private boolean isOwner;
	private int month;
	private int year; 
	private String meettype;
	private String status;
	private String grouptype;
	private String filterbar;
	private String customerno;
	public String getCorpCode() {
		return corpCode;
	}

	private String customername;
	private boolean data;
	private String ggsplc;
	private String dmggsplc;
	public String getGgsplc() {
		return ggsplc;
	}

	public void setGgsplc(String ggsplc) {
		this.ggsplc = ggsplc;
	}

	public String getDmggsplc() {
		return dmggsplc;
	}

	public void setDmggsplc(String dmggsplc) {
		this.dmggsplc = dmggsplc;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	private String hybh;
	private String opreatorType;
	private Date meettime;
	private int jc;
	private int hc;
	private String hysx;
	private String html;
	private String fileHtml;
	
	/********以下关于文件上传**********/
	private File uploadify;
	private String uploadifyFileName;
	private String fileUUID;
	private FileUploadService uploadifyService;
	private String sizeLimit = "";
	private String multi = "true";
	private String fileExt ="";
	private String fileDesc="";
	private List<FileUpload> files;     //附件
	private String fileMaxNum;          //批量上传，最多上传个数
	private String fileMaxSize;         //批量上传，最大上传大小
	private String fileSize;  
	private Long parentid;
	private Long shenPi;
	private String zqdmxs;
	private String zqjcxs;
	
	public String getZqdmxs() {
		return zqdmxs;
	}

	public void setZqdmxs(String zqdmxs) {
		this.zqdmxs = zqdmxs;
	}

	public String getZqjcxs() {
		return zqjcxs;
	}

	public void setZqjcxs(String zqjcxs) {
		this.zqjcxs = zqjcxs;
	}

	public Long getShenPi() {
		return shenPi;
	}

	public void setShenPi(Long shenPi) {
		this.shenPi = shenPi;
	}
	private int flag;
	private int pageNumber; // 当前页数
	private int totalNum; // 总页数
	private int pageSize = 10; // 每页条数
	
public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	public void checkdate(){
		if(startdate==null || "".equals(startdate) || enddate==null || "".equals(enddate) ){
			ResponseUtil.write("error");
		}else{
			 int res=startdate.compareTo(enddate);
		        if(res>0)
		        	ResponseUtil.write("error1");
		        else if(res==0)
		        	ResponseUtil.write("success");
		        else
		        	ResponseUtil.write("success");
		}
	}
	//	/***************************************/
	public String index(){
		if(year==0)year = UtilDate.getYear(new Date());
		if(month==0)month=UtilDate.getMonth(new Date());
		if(status==null){status="全部";}
		if(meettype==null){meettype="全部";}
		if(grouptype==null){grouptype="全部";}
		if(customerno!=null){
			if (pageNumber == 0)
				pageNumber = 1;
			runList = zqbMeetingManageService.getMeetRunList1(customerno,year,month, meettype, grouptype, status,pageSize, pageNumber);
			filterbar = zqbMeetingManageService.getMeetFilterBar(year,month,meettype,grouptype,status);
			totalNum=zqbMeetingManageService.getMeetRunListSize(customerno,year,month,meettype, grouptype, status);
			shenPi=zqbMeetingManageService.getSInstanceID(customerno);
			data=zqbMeetingManageService.getDataSee();
			zqdmxs=zqbMeetingManageService.getZqdmxs(customerno);
			zqjcxs=zqbMeetingManageService.getZqjcxs(customerno);
		}
		readonly=false;
		ggsplc= ProcessAPI.getInstance().getProcessActDefId("CXDDYFQLC");
		dmggsplc= ProcessAPI.getInstance().getProcessActDefId("GGSPLC");
		try {
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			yys=user.getOrgroleid();
			if(user.getOrgroleid()==4 || user.getOrgroleid()==5){
				flag=0;
			}else{
				flag=1;
			}
			List list=zqbMeetingManageService.getListqx(user.getUserid());
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					if(Integer.parseInt(list.get(i).toString())==4 || Integer.parseInt(list.get(i).toString())==5){
						flag=0;
					}
				}
			}
		} catch (Exception e) {
			flag=1;
		}
		return SUCCESS; 
	}
	/**
	 * 导出  三会计划
	 */
	public void toExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbMeetingManageService.thxmexportexcl(response,customerno);
	}
	/**
	 * 查看已召开的会议
	 * @return
	 */
	public String loadConveneMeeting(){
		if(instanceid!=null){
			html= zqbMeetingManageService.getQtFileList(customerno, instanceid,isOwner); 
			 fileHtml = zqbMeetingManageService.getFileList(customerno, instanceid,isOwner); 
			hash = zqbMeetingManageService.getMeetingMap(instanceid);
		}
		return SUCCESS;
	}
	/**
	 * 
	 * @return
	 */
	public String recentlylist(){
		if(year==0)year = UtilDate.getYear(new Date());
		
		if(status==null){status="全部";}
		if(meettype==null){meettype="全部";}
		if(grouptype==null){grouptype="全部";}
		//获取客户列表
		List<String> customerNoList = new ArrayList();
		customerList = 

zqbMeetingManageService.getCurrentCustomerList();
		if(customerList==null||customerList.size()==0){
			OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext()._userModel;		
				customerno = orgUser.getExtend1();
				customerNoList.add(customerno);
		}else{
			for(HashMap map:customerList){
				if(map.get("KHBH")!=null){
					customerNoList.add(map.get

("KHBH").toString());
				}
			}
		}
		filterbar = zqbMeetingManageService.getRecentlyFilterBar

(meettype, grouptype, status);
		runList = zqbMeetingManageService.getRecentlyList

(customerNoList,meettype, grouptype, status);
		return SUCCESS;
	}
	
	/**
	 * 检索中心
	 * @return
	 */
	public String searchCenter(){
		customerList = 

zqbMeetingManageService.getCurrentCustomerList();
		
		return SUCCESS;
	}
	
	public String charIndex(){
		typeBarData = zqbMeetingManageService.getChartBarData();
		List<String> list 

=zqbMeetingManageService.getChartBarLabelList();
		typeBarLabel = JSONArray.fromObject(list).toString();
		return SUCCESS;
	}
	public String gridIndex(){
		
		return SUCCESS;
	}
	
	public String itemIndex(){
		
		return SUCCESS;
	}
	/**
	 * 编辑会议状态
	 * @return
	 */
	public String meetEdit(){
		if(instanceid!=null){
			opreatorType = "finish";
			hash = zqbMeetingManageService.getMeetingMap

(instanceid);
		}
		return SUCCESS;
	}
	
	/**
	 * 编辑会议状态
	 * @return
	 */
	public String meetRetime(){
		if(instanceid!=null){
			opreatorType = "retime";
			hash = zqbMeetingManageService.getMeetingMap

(instanceid);
		}
		return SUCCESS;
	}
	
	public void setStatus(){
		if(opreatorType!=null&&instanceid!=null&&meettime!=null){
			boolean flag = zqbMeetingManageService.setStatus(opreatorType, instanceid, meettime);
			if(flag){
				ResponseUtil.write(SUCCESS);
			}else{
				ResponseUtil.write(ERROR);
			}
		}
	}
	/**
	 * 检查是否已添加了会议提交资料清单
	 */
	public void checkReturnList(){
		if(customerno!=null&&instanceid!=null){
			HashMap hm = 

zqbMeetingManageService.checkReturnListModel(customerno, instanceid);
			if(hm!=null){
				Object obj = hm.get("INSTANCEID");
				if(obj!=null){
					ResponseUtil.write(obj.toString());
				}else{
					ResponseUtil.write("null");
				}
			}else{
				ResponseUtil.write("null");
			}
		}
	}
	public void removeItem(){
		boolean flag = false;
		if(instanceid!=null){
			flag = zqbMeetingManageService.removeItem(instanceid);
		}
		if(flag){
			ResponseUtil.write("success"); 
		}else{
			ResponseUtil.write("error"); 
		}
		
	}
	public void zqbmeetingset(){
		try {
			ResponseUtil.write(zqbMeetingManageService.zqbmeetingset(plantime,meettype,hysx,customerno));
		} catch (ParseException e) {
			logger.error(e,e);
		} 
	}
	/**
	 * 获取会议召开次数
	 */
	public void getMeetingCount(){
		//获取会次
		if(customerno!=null&&meettype!=null&&grouptype!=null){
			int num = 0;
			num = zqbMeetingManageService.getMeetingCount(customerno, jc,meettype, grouptype,hc, meetyear, meetpro);
			// 获取最大值加1为本次会议会次
			num = num+1;
			ResponseUtil.write(String.valueOf(num));
		}
		
	}
	/**
	 * 获取会议召开次数
	 */
	public void getMeetingJC(){
		 String meettype = "";
		 if(this.getMeettype()==null){
			 this.setMeettype("");
		 }
		if(customerno!=null&&!meettype.equals("")){
			String json = zqbMeetingManageService.getMeetingJC

(customerno, meettype,hysx,jc); 
			ResponseUtil.write(json);
		}
		
	}
	/**
	 * 获取要提交的文件清单页面
	 * @return
	 */
	public String commitFile(){
		if(customerno!=null&&instanceid!=null){
			hybh = instanceid.toString();
			 html = zqbMeetingManageService.getCheckReturnList(customerno, instanceid);
			 fileHtml = zqbMeetingManageService.getFileListHtml(customerno, instanceid,isOwner); 
		}
		return SUCCESS;
	}
	
	public void doCommitFile(){
		String demUUID = DBUtil.getDataStr("UUID", "SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='会议计划管理'", null);
		
		HashMap data = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		
		Long dataid = Long.parseLong(data.get("ID").toString());
		//附件uuid值更新,避免只过长，保存不上
		String fj = data.get("FJ")==null?"":data.get("FJ").toString();
		List<FileUpload> files = FileUploadAPI.getInstance().getFileUploads(fj);
		fj="";
		for (int i = 0; i < files.size(); i++) {
			if(i==0){
				fj+=files.get(i).getFileId();
			}else{
				fj+=(","+files.get(i).getFileId());
			}
		}
		if(fj.equals("")){
			fj = uuid;
		}else{
			fj+=(","+uuid);
		}
		//计划时间处理,避免出现日期转换格式错误
		String plantime = data.get("PLANTIME").toString();
		plantime = plantime.substring(0, plantime.lastIndexOf(".")==-1?plantime.length():plantime.lastIndexOf("."));
		//执行更新操作
		data.put("PLANTIME", plantime);
		data.put("FJ", fj);
		DemAPI.getInstance().updateFormData(demUUID, instanceid, data, dataid, false);
	}
	
	public void saveKmDoc(){
		Long id = com.iwork.core.db.DBUtil.getLong("SELECT MAX(ID)+1 AS NEWID FROM IWORK_KM_DOC","NEWID");
		Long order_index = com.iwork.core.db.DBUtil.getLong("SELECT MAX(ORDER_INDEX)+1 AS NEWORDER_INDEX FROM IWORK_KM_DOC","NEWORDER_INDEX");
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());		
		zqbMeetingManageService.saveKmDoc(uuid,directoryId,filesize,filename,id,order_index,time);
	}
	
	/**
	 * 上传页面
	 * @return
	 */
	public String uploadPage(){
		
		return SUCCESS;
	}
	/**
	 * 根据会议年度、会议类型、单位编号、专业委员会、会议届次、会议属性

校验同一会议是否存在
	 */
	public void validateMeetNumber(){
		boolean flag = false;
		if(customerno!=null && meetyear!=null && jc!=0 && 

meettype!=null && grouptype!=null && hc!=0){
			flag = zqbMeetingManageService.validateMeetNumber

(customerno, meetyear, jc, meettype, meetpro, grouptype,hc);
		}
		if(flag){
			ResponseUtil.write(SUCCESS);
		}else{
			ResponseUtil.write(ERROR);
		}
	}
	/**
	 * 根据会议年度、会议类型、客户编号、专业委员会、会议属性
	 */
	public void getMeetSession(){
		int num  = 0;
		num = zqbMeetingManageService.getMeetSession(customerno,meetyear, meettype, meetpro, grouptype);
		if(num==0){
			num = num+1;
		}
		ResponseUtil.write(String.valueOf(num));
	}
	/**
	 * 上传附件
	 * @return
	 * @throws Exception
	 */
	public String upload() throws Exception {
		boolean success=true;
		int countStr = StringUtil.countStr(uploadifyFileName,".");
		 FileUploadUtil file = new FileUploadUtil();
         String flag=file.uploadname(uploadifyFileName);
         if(!flag.equals("true")){
       	  success=false;
         }
		if(countStr>=2){
			success=false;
		}
		if(StringUtil.mathcer("[@$%\\/]",uploadifyFileName)){
			success=false;
		}
		if(uploadifyFileName!=null){
			if(!StringUtil.validata(FileUtil.getFileExt(uploadifyFileName))){
				success=false;
			}
		}else{
			success=false;
		}
		if(success){
			FileUpload model = uploadifyService.save(uploadify, uploadifyFileName);
			if(parentid!=null){
				HashMap hash = DemAPI.getInstance().getFromData(parentid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
				if(hash!=null){
					String fj = "";
					Date plantime = null;
					if(hash.get("FJ")!=null){
						fj = hash.get("FJ").toString();
					}
					if(fj.equals("")){
						fj = model.getFileId();
					}else{
						fj = fj+","+model.getFileId();
					}
					if(hash.get("PLANTIME")!=null){
						plantime = (Date) hash.get("PLANTIME");
					}
					hash.put("FJ",fj);
					hash.put("PLANTIME", UtilDate.datetimeFormat(plantime));
					DemAPI.getInstance().updateFormData("ab33c7b0b1b04bb3adbd16eb77a43409", parentid, hash, Long.parseLong(hash.get("ID").toString()), false);
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			if (model!=null) {
				//上传成功后，添加系统消息
				HashMap hash = DemAPI.getInstance().getFromData(parentid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
				String sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.MEET_DOC_UPLOAD_KEY, hash); 
				if(hash!=null){
					//获取会议干系人
					List<String> noticelist = new ArrayList();
					if(hash!=null&&hash.get("CUSTOMERNO")!=null){
						String customerManagerUserid = zqbMeetingManageService.getCustomerManagerUser(hash.get("CUSTOMERNO").toString());
						if(customerManagerUserid!=null){
							noticelist.add(customerManagerUserid);
						}
					}
					//发送提醒消息
					for(String usr:noticelist){
						String userid = UserContextUtil.getInstance().getUserId(usr);
						MessageAPI.getInstance().sendSysMsg(userid, "会议资料上传提醒", sysMsgContent);
					}
				}
				String uuid = model.getFileId();
				String url = model.getFileUrl();
				response.getWriter().print("{flag:true,uuid:'"+uuid+"',url:'"+url+"'}");
				
			} else {
				response.getWriter().print("{flag:false}");
				throw new Exception(uploadifyFileName + "上传失败");
			}
		}else{
			throw new Exception(uploadifyFileName + "上传失败");
		}
		return null; // 这里不需要页面转向，所以返回空就可以了
	}
	
	public void sendmeetmsg(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		Long orgroleid = user.getOrgroleid();
		if(orgroleid==3l){
			HashMap hash = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			String sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.MEET_DOC_UPLOAD_KEY, hash); 
			if(hash!=null){
				//获取会议干系人
				List<String> noticelist = new ArrayList();
				if(hash!=null&&hash.get("CUSTOMERNO")!=null){
					String customerManagerUserid = zqbMeetingManageService.getCustomerManagerUser(hash.get("CUSTOMERNO").toString());
					if(customerManagerUserid!=null){
						noticelist.add(customerManagerUserid);
					}
				}
				//发送提醒消息
				for(String usr:noticelist){
					String userid = UserContextUtil.getInstance().getUserId(usr);
					MessageAPI.getInstance().sendSysMsg(userid, "会议资料上传提醒", sysMsgContent);
				}
			}
		}
	}

	public void setZqbMeetingManageService(
			ZqbMeetingManageService zqbMeetingManageService) {
		this.zqbMeetingManageService = zqbMeetingManageService;
	}

	public List<HashMap> getRunList() {
		return runList;
	}

	public List<HashMap> getFinishList() {
		return finishList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public HashMap getHash() {
		return hash;
	}
	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getJson() {
		return json;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public Long getInstanceid() {
		return instanceid;
	}

	public void setInstanceid(Long instanceid) {
		this.instanceid = instanceid;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getTypePieData() {
		return typePieData;
	}

	public String getTypeBarData() {
		return typeBarData;
	}

	public void setTypeBarData(String typeBarData) {
		this.typeBarData = typeBarData;
	}

	public String getTypeBarLabel() {
		return typeBarLabel;
	}

	public void setTypeBarLabel(String typeBarLabel) {
		this.typeBarLabel = typeBarLabel;
	}

	public String getTypePieData2() {
		return typePieData2;
	}

	public String getTypePieData3() {
		return typePieData3;
	}

	public boolean isSuperman() {
		return superman;
	}

	public void setSuperman(boolean superman) {
		this.superman = superman;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMeettype() {
		return meettype;
	}
	public void setMeettype(String meettype) {
		this.meettype = meettype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGrouptype() {
		return grouptype;
	}
	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
	}
	public String getFilterbar() {
		return filterbar;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public void setJc(int jc) {
		this.jc = jc;
	}

	public String getOpreatorType() {
		return opreatorType;
	}

	public void setOpreatorType(String opreatorType) {
		this.opreatorType = opreatorType;
	}

	public Date getMeettime() {
		return meettime;
	}

	public void setMeettime(Date meettime) {
		this.meettime = meettime;
	}

	public List<HashMap> getCustomerList() {
		return customerList;
	}

	public String getHtml() {
		return html;
	}

	public String getHybh() {
		return hybh;
	}

	public void setHybh(String hybh) {
		this.hybh = hybh;
	}

	public File getUploadify() {
		return uploadify;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public String getUploadifyFileName() {
		return uploadifyFileName;
	}

	public void setUploadifyFileName(String uploadifyFileName) {
		this.uploadifyFileName = uploadifyFileName;
	}

	public String getFileUUID() {
		return fileUUID;
	}

	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}

	public FileUploadService getUploadifyService() {
		return uploadifyService;
	}

	public void setUploadifyService(FileUploadService uploadifyService) 

{
		this.uploadifyService = uploadifyService;
	}

	public String getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(String sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public List<FileUpload> getFiles() {
		return files;
	}

	public void setFiles(List<FileUpload> files) {
		this.files = files;
	}

	public String getFileMaxNum() {
		return fileMaxNum;
	}

	public void setFileMaxNum(String fileMaxNum) {
		this.fileMaxNum = fileMaxNum;
	}

	public String getFileMaxSize() {
		return fileMaxSize;
	}

	public void setFileMaxSize(String fileMaxSize) {
		this.fileMaxSize = fileMaxSize;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;	
	}

	public String getFileHtml() {
		return fileHtml;
	}

	public void setFileHtml(String fileHtml) {
		this.fileHtml = fileHtml;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setIsOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public String getHysx() {
		return hysx;
	}

	public void setHysx(String hysx) {
		this.hysx = hysx;
	}

	public int getHc() {
		return hc;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}

	public void setHc(int hc) {
		this.hc = hc;
	}
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * 获取会议召开次数
	 */
	public void getMeeting(){
		if(customerno!=null&&meettype!=null&&grouptype!=null){
			String json =zqbMeetingManageService.getMeetingCount(customerno, jc, meettype, grouptype, hc, meetyear);
			ResponseUtil.write(json);
		}
		
	}
	public void updateClFlag(){
		String msg= zqbMeetingManageService.updateClFlag(instanceid,id);
		ResponseUtil.write(msg);
	}
	
	//xlj 2015年6月12日10:43:20 预约信息
	private String corpCode;
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	private String cxdd;
	public void setCxdd(String cxdd) {
		this.cxdd = cxdd;
	}
	private int eventID;
	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	private String yyformid;
	private String yydemid;
	public String getYyformid() {
		return yyformid;
	}

	public void setYyformid(String yyformid) {
		this.yyformid = yyformid;
	}

	public String getYydemid() {
		return yydemid;
	}

	public void setYydemid(String yydemid) {
		this.yydemid = yydemid;
	}

	public String AppointInfo(){
		yyformid = com.iwork.core.db.DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='定期预约公告附件'", "FORMID");
		yydemid = com.iwork.core.db.DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='定期预约公告附件'", "ID");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String roleid = uc._userModel.getOrgroleid().toString();
		readonly = false;
		roleCid=roleid;
		
		if(corpCode==null){corpCode="";}
		if(startDate==null){startDate="";}
		if(endDate==null){endDate="";}
		if (pageNumber == 0)
			pageNumber = 1;
		String strLoginUser=uc.get_userModel().getUserid();//登录人
			
		if("3".equals(roleid)){//如果是董秘登陆，查看其公司代码
			customerno=uc.get_userModel().getExtend1();		
			HashMap getCorpCodeAndCXDD = zqbMeetingManageService.GETCorpCodeAndCXDD(customerno);			
			corpCode = getCorpCodeAndCXDD.get("corpCode")==null?"":getCorpCodeAndCXDD.get("corpCode").toString();			
		}else if(!"5".equals(roleid)){
			cxdd=strLoginUser;//是督导				
		}
		if(cxdd == null){cxdd="";}
		sxmsList = zqbMeetingManageService.sxmsConfig(cxdd);	
		if(eventID == 0 && sxmsList.size() == 0)
		{
			return SUCCESS; 
		}
		
		if(sxmsList.size() > 0)
		{
			if(eventID == 0)
			eventID = Integer.parseInt(sxmsList.get(0).get("ID").toString());
			else
			{
				boolean bExists = false; 
				for(int i=0;i<sxmsList.size();i++)
				{
					int strID = Integer.parseInt(sxmsList.get(i).get("ID").toString());
					if(strID == eventID)
					{
						bExists = true;
						break;
					}
				}
				if(!bExists)
				{
					eventID = Integer.parseInt(sxmsList.get(0).get("ID").toString());	
				}				
			}
		}
		
		//获取预约信息
		runList = zqbMeetingManageService.AppointInfo(corpCode,startDate,endDate, eventID, cxdd, pageSize, pageNumber);	
		totalNum=zqbMeetingManageService.AppointInfoSize(corpCode, startDate, endDate, eventID, cxdd);
		return SUCCESS; 
	}	
	
	public String newAppointInfo(){
		yyformid = com.iwork.core.db.DBUtil.getString("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='定期预约公告附件'", "FORMID");
		yydemid = com.iwork.core.db.DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='定期预约公告附件'", "ID");
		if(customerno==null){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			customerno=uc.get_userModel().getExtend1();
		}
		HashMap getCorpCodeAndCXDD = zqbMeetingManageService.GETCorpCodeAndCXDD(customerno);			
		sxmsList = zqbMeetingManageService.sxmsConfig("");
		runList = zqbMeetingManageService.NewAppointInfo(customerno,yyrq, eventID);
		return SUCCESS;
	}
	
	private int SXID;
	public int getSXID() {
		return SXID;
	}

	public void setSXID(int sXID) {
		SXID = sXID;
	}

	//获取可以预约的日期信息
	public String getCanAppoint()
	{		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String roleid = uc.get_orgRole().getId();
		if("3".equals(roleid)){
			customerno=uc.get_userModel().getExtend1();
			HashMap getCorpCodeAndCXDD = zqbMeetingManageService.GETCorpCodeAndCXDD(customerno);			
			corpCode = getCorpCodeAndCXDD.get("corpCode").toString();
			/*cxdd = getCorpCodeAndCXDD.get("cxdd").toString();*/
		}
		if (pageNumber == 0)
			pageNumber = 1;
		roleCid=roleid;
		if(startDate==null){startDate=String.format("%1$tY-%1$tm-%1$td",new Date());}
		finishList = zqbMeetingManageService.getCanAppoint(corpCode,startDate, SXID, pageSize, pageNumber);		
		SXID = Integer.parseInt(finishList.get(0).get("SXID").toString());
		int Success = Integer.parseInt(finishList.get(0).get("Success").toString());
		runList = (List<HashMap>)finishList.get(1).get("listInfo");
		sxmsList = (List<HashMap>)finishList.get(2).get("listSX");
		json = "";
		switch(Success)
		{
			case -1:
				json = "没有此股票代码的挂牌公司信息,请在客户信息管理中进行录入，或者联系督导老师进行处理。";
				break;
			case -2:
				json = "此股票代码的挂牌公司信息有多条，请在客户信息管理中进行修改，或者联系督导老师进行处理。";
				break;
			case -3:
				json = "对不起，系统中未设置此挂牌公司的督导老师，请联系督导老师进行处理。";
				break;
			case -4:
				json = "对不起，您的督导老师没有设置此事项允许的最大预约数，请联系督导老师.";
				break;
			case -5:
				json = "对不起，没有需要预约的事项信息.";
				break;
			case -6:
				json = "对不起，预计预约日期大于此事项截止日期.";
				break;
			case -7:
				json = "对不起，此挂牌公司不是您负责督导的企业.";
				break;
			case 2:
				status = finishList.get(0).get("ExistInfo").toString();	
				break;
			case 0:
				json = "查询有误，请重试";
				break;				
		}
		return SUCCESS;
	}
	
	//获取可以预约的日期信息
	public String getNewCanAppoint(){		
		HashMap getCorpCodeAndCXDD = zqbMeetingManageService.GETCorpCodeAndCXDD(customerno);			
		corpCode = getCorpCodeAndCXDD.get("corpCode").toString();
		if (pageNumber == 0)
			pageNumber = 1;
		if(startDate==null){startDate=String.format("%1$tY-%1$tm-%1$td",new Date());}
		finishList = zqbMeetingManageService.getCanAppoint(corpCode,startDate, SXID, pageSize, pageNumber);		
		SXID = Integer.parseInt(finishList.get(0).get("SXID").toString());
		int Success = Integer.parseInt(finishList.get(0).get("Success").toString());
		runList = (List<HashMap>)finishList.get(1).get("listInfo");
		sxmsList = (List<HashMap>)finishList.get(2).get("listSX");
		json = "";
		switch(Success)
		{
		case -1:
			json = "没有此股票代码的挂牌公司信息,请在客户信息管理中进行录入，或者联系督导老师进行处理。";
			break;
		case -2:
			json = "此股票代码的挂牌公司信息有多条，请在客户信息管理中进行修改，或者联系督导老师进行处理。";
			break;
		case -3:
			json = "对不起，系统中未设置此挂牌公司的督导老师，请联系督导老师进行处理。";
			break;
		case -4:
			json = "对不起，您的督导老师没有设置此事项允许的最大预约数，请联系督导老师.";
			break;
		case -5:
			json = "对不起，没有需要预约的事项信息.";
			break;
		case -6:
			json = "对不起，预计预约日期大于此事项截止日期.";
			break;
		case -7:
			json = "对不起，此挂牌公司不是您负责督导的企业.";
			break;
		case 2:
			status = finishList.get(0).get("ExistInfo").toString();	
			break;
		case 0:
			json = "查询有误，请重试";
			break;				
		}
		return SUCCESS;
	}
	
	
	//添加预约信息
	public void SaveAppoint()
	{
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String roleid = uc.get_orgRole().getId();
		if("3".equals(roleid)){//如果董秘，传空
			cxdd="";
		}
		else
		{
			cxdd = uc.get_userModel().getUserid();//登录人		
		}
		HashMap list = zqbMeetingManageService.ADDYYXX(customerno,corpCode,SXID, cxdd, startDate);
		int success =  Integer.parseInt(list.get("SUCCESS").toString());
		if(success>0){
			zqbMeetingManageService.sendMsg(corpCode,SXID,startDate);
		}
		String strErr = "";
		switch(success)
		{
		case -1:
			strErr = "没有此股票代码的挂牌公司信息,请在客户信息管理中进行录入，或者联系督导老师进行处理。";
			break;
		case -2:
			strErr = "此股票代码的挂牌公司信息有多条，请在客户信息管理中进行修改，或者联系督导老师进行处理。";
			break;
		case -3:
			strErr = "对不起，您的督导老师没有设置此事项允许的最大预约数，请联系督导老师.";
			break;
		case -4:
			strErr = "对不起，今天已经预约满了，请更换预约日期.";
			break;
		case 0:
			strErr = "保存失败，请重试。";
			break;				
		}
		if(success > 0)
		{
			ResponseUtil.write("success");
		}
		else
			ResponseUtil.write(strErr);
	}
	
	//删除预约信息
	public void DeleteAppoint()
	{
		projectNo = projectNo.substring(0,projectNo.length()-1);
		String str = zqbMeetingManageService.DeleteAppoint(projectNo);
		if(str == "")
		{
			ResponseUtil.write("success");
		}
		ResponseUtil.write(str);
	}
	
	//删除预约信息
	public void NewDeleteAppoint()
	{
		String str = zqbMeetingManageService.NewDeleteAppoint(projectNo);
		if(str == "")
		{
			ResponseUtil.write("success");
		}
		ResponseUtil.write(str);
	}
	
	//更新预约信息
	public void UPDATEAppoint()
	{		
		String str = zqbMeetingManageService.UPDATEAppoint(projectNo,startDate);
		if(str == "")
		{
			ResponseUtil.write("success");
		}
		ResponseUtil.write(str);
	}
	
	public String appointmentSetNum(){
		/*sxmsList = zqbMeetingManageService.getSxmcList();*/
		appointmentNum=new AppointmentNum();
		appointmentYysx=new AppointmentYYSX();
		if(yysx==null){
			yysx=(long) 0;
		}
		appointmentYysx=zqbMeetingManageService.setAppointmentYysx(appointmentYysx,yysx);
		appointmentNum=zqbMeetingManageService.setAppointment(appointmentNum,yysx);
		return SUCCESS;
	}
	@SuppressWarnings("unchecked")
	public String zqbTzggProjectCostormerSet(){
		listxjxzq=zqbMeetingManageService.getZqbTzggProjectCostormerSet(name,sszjj,szbm,gpdq,gprqks,gprqjs,ssbm,gpzt,zczt);
		return SUCCESS;
	}
	public String cxzqbTzggProjectCostormerSet(){
		name=this.name;
		sszjj=this.sszjj;
		szbm=this.szbm;
		gpdq=this.gpdq;
		gprqks=this.gprqks;
		gprqjs=this.gprqjs;
		ssbm=this.ssbm;
		listxjxzq=zqbMeetingManageService.getZqbTzggProjectCostormerSet(name,sszjj,szbm,gpdq,gprqks,gprqjs,ssbm,gpzt,zczt);
		
		return SUCCESS;
	}
	public String selectNum(){
		if (pageNumber == 0)
			pageNumber = 1;
		runList=zqbMeetingManageService.selectNum(pageNumber,pageSize);
		totalNum=zqbMeetingManageService.selectNumTotalNum();
		return SUCCESS;
	}
	public void saveAppointmentNum(){
		int iTmp = 0;//xlj 涓存椂璺熻釜纭畾闂浣跨敤
		try{
		UserContext uc = UserContextUtil.getInstance()
				.getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		String szr="";
		if(null==this.getAppointmentYysx().getId()){
			iTmp = 1;
			AppointmentYYSX model = this.getAppointmentYysx();
			model.setCjsj(new Date());
			szr=model.getYydd();
			model.setCjr(userid);
			appointmentYysx=zqbMeetingManageService.saveAppointmentYYSX(model);
			String[] szrsz=szr.split(",");
			AppointmentNum modelNum=this.getAppointmentNum();
			for (int i = 0; i < szrsz.length; i++) {
				modelNum.setSzr(szrsz[i]);
				modelNum.setSzsj(new Date());
				modelNum.setYysx(model.getId());
				zqbMeetingManageService.save(modelNum);
			}
		}else{
			iTmp = 2;
			this.getAppointmentYysx().setCjsj(new Date());
			this.getAppointmentYysx().setCjr(userid);
			szr=this.getAppointmentYysx().getYydd();
			zqbMeetingManageService.updateAppointmentYYSX(this.getAppointmentYysx());
			Map map=new HashMap();
			map.put(1,this.getAppointmentYysx().getId());
			DBUTilNew.update( "DELETE FROM BD_XP_YYS WHERE YYSX=?", map);
//			com.iwork.core.db.DBUtil.executeUpdate(" DELETE FROM BD_XP_YYS WHERE YYSX='"+this.getAppointmentYysx().getId()+"' ");
			
			String[] szrsz=szr.split(",");
			AppointmentNum modelNum=this.getAppointmentNum();
			for (int i = 0; i < szrsz.length; i++) {
				modelNum.setSzr(szrsz[i]);
				modelNum.setSzsj(new Date());
				modelNum.setYysx(this.getAppointmentYysx().getId());
				zqbMeetingManageService.save(modelNum);
			}			
		}	
		zqbMeetingManageService.Sysmanagers(this.getAppointmentYysx(),this.getAppointmentNum(),sftz);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());		
		}
		finally
		{
			System.out.println(iTmp);	
		}
	}

//	public void saveAppointmentNum(){
//		int iTmp = 0;//xlj 临时跟踪确定问题使用
//		try{
//		UserContext uc = UserContextUtil.getInstance()
//				.getCurrentUserContext();
//		String userid = uc.get_userModel().getUserid();
//		if(null==this.getAppointmentYysx().getId()){
//			iTmp = 1;
//			AppointmentYYSX model = this.getAppointmentYysx();
//			
//			model.setCjsj(new Date());
//			appointmentYysx=zqbMeetingManageService.saveAppointmentYYSX(model);
//		}else{
//			iTmp = 2;
//			this.getAppointmentYysx().setCjsj(new Date());
//			zqbMeetingManageService.updateAppointmentYYSX(this.getAppointmentYysx());
//		}
//		if(null==this.getAppointmentNum().getId()){
//			iTmp = 3;
//			AppointmentNum model=this.getAppointmentNum();
//			if(model.getYysx()==null&&yysx==null){
//				model.setYysx(appointmentYysx.getId());
//			}else if(model.getYysx()==null&&yysx!=null){
//				model.setYysx(yysx);
//			}
//			model.setSzsj(new Date());
//			model.setSzr(userid);
//			zqbMeetingManageService.save(model);
//		}else{
//			iTmp = 4;			
//			this.getAppointmentNum().setSzsj(new Date());			
//			zqbMeetingManageService.update(this.getAppointmentNum());
//		}
//		zqbMeetingManageService.Sysmanagers(this.getAppointmentYysx(),this.getAppointmentNum(),sftz);
//		}
//		catch(Exception e)
//		{
//			logger.error(e,e);
//		}
//	}
//	public String getAppointmentYYSX(){
//		appointmentYysx=zqbMeetingManageService.getModelAppointmentYYSX(sxid);
//		
//		List lables = new ArrayList();
//		lables.add("yydd");
//		lables.add("szs");
//		String sql = "  SELECT TO_CHAR(WMSYS.WM_CONCAT(DISTINCT SZR)) yydd,szs FROM BD_XP_YYS WHERE YYSX="+sxid+"  GROUP BY szs ";
//		String szrid = "";
//		String szs = "";
//		
//		List mobilelist = com.iwork.commons.util.DBUtil.getDataList(lables, sql, null);
//		if(mobilelist.size()==1){
//			szrid = ((HashMap)mobilelist.get(0)).get("yydd")==null?"":((HashMap)mobilelist.get(0)).get("yydd").toString();
//			szs = ((HashMap)mobilelist.get(0)).get("szs")==null?"":((HashMap)mobilelist.get(0)).get("szs").toString();
//		}
//		Long l=Long.parseLong(szs);
//		appointmentYysx.setYydd(szrid);
//		AppointmentNum model=new AppointmentNum();
//		model.setSzs(l);
//		appointmentNum=model;		
//		return SUCCESS;
//	}

	public String getAppointmentYYSX(){
		appointmentYysx=zqbMeetingManageService.getModelAppointmentYYSX(sxid);
		
		List lables = new ArrayList();
		lables.add("yydd");
		lables.add("szs");
		String sql = "  SELECT TO_CHAR(WMSYS.WM_CONCAT(DISTINCT SZR)) yydd,szs FROM BD_XP_YYS WHERE YYSX=? GROUP BY szs ";
		String szrid = "";
		String szs = "";
		Map map=new HashMap();
		map.put(1,sxid);
		List mobilelist = com.iwork.commons.util.DBUtil.getDataList(lables, sql, map);
		if(mobilelist.size()==1){
			szrid = ((HashMap)mobilelist.get(0)).get("yydd")==null?"":((HashMap)mobilelist.get(0)).get("yydd").toString();
			szs = ((HashMap)mobilelist.get(0)).get("szs")==null?"":((HashMap)mobilelist.get(0)).get("szs").toString();
		}
		Long l=Long.parseLong(szs);
		appointmentYysx.setYydd(szrid);
		AppointmentNum model=new AppointmentNum();
		model.setSzs(l);
		appointmentNum=model;		
		return SUCCESS;
	}

	
	public void YYDate()
	{
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String roleid = uc.get_orgRole().getId();
		if("3".equals(roleid)){
			customerno=uc.get_userModel().getExtend1();
			HashMap getCorpCodeAndCXDD = zqbMeetingManageService.GETCorpCodeAndCXDD(customerno);			
			corpCode = /*getCorpCodeAndCXDD.get("corpCode")==null?"":*/getCorpCodeAndCXDD.get("corpCode").toString();
			cxdd = /*getCorpCodeAndCXDD.get("cxdd")==null?"":*/getCorpCodeAndCXDD.get("cxdd").toString();
		}
		if (pageNumber == 0)
			pageNumber = 1;
		roleCid=roleid;
		if(startDate==null){startDate=String.format("%1$tY-%1$tm-%1$td",new Date());}
		finishList = zqbMeetingManageService.getCanAppoint(corpCode,startDate, SXID, pageSize, pageNumber);
		totalNum = Integer.parseInt(finishList.get(0).get("totalNum").toString());
		SXID = Integer.parseInt(finishList.get(0).get("SXID").toString());
		int Success = Integer.parseInt(finishList.get(0).get("Success").toString());
		runList = (List<HashMap>)finishList.get(1).get("listInfo");
		sxmsList = (List<HashMap>)finishList.get(2).get("listSX");
		json = "";		
	}
	public void deleteYYSX(){
		boolean flag=zqbMeetingManageService.deleteYYSX(sxid);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	//查询所有扣分类别
	public String cxkflb(){
		runList =  zqbMeetingManageService.cxkflb("");
		return SUCCESS;
	}
	//查询扣分类别
	public String  czkflb(){
		runList =  zqbMeetingManageService.cxkflb(KFLB);
		this.KFLB=KFLB;
		return SUCCESS;
	}
	//显示新增扣分类别
	public String xinzengkflb(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();			
		userID = uc.get_userModel().getUserid();
		return SUCCESS;
	}
	//保存新增扣分类别
	public void Savekflb(){
		try {
			KFLB = java.net.URLDecoder.decode(KFLB, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e,e);
		}
		boolean flag = zqbMeetingManageService.Savekflb(KFLB.trim(),CJZH,FS);
		if(flag)
		ResponseUtil.write("success");
		else
		ResponseUtil.write("error");
	}
	//查询需要修改的扣分类别
	public String cxupdatekflb(){
		runList = zqbMeetingManageService.cxupdatekflb(kflbid);
		return SUCCESS;
	}
	//修改扣分类别
	public void updatekflb(){
		try {
			KFLB = java.net.URLDecoder.decode(KFLB, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e,e);
		}
		boolean flag = zqbMeetingManageService.updatekflb(kflbid,KFLB.trim(),CJZH,FS);
		if(flag)
		ResponseUtil.write("success");
		else
		ResponseUtil.write("error");	
	}
	//删除扣分类别
	public String deletekflb(){
		zqbMeetingManageService.deletekflb(kflbid);
		return SUCCESS;
	}
}