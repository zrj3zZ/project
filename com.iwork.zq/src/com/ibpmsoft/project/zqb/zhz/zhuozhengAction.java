package com.ibpmsoft.project.zqb.zhz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.engine.dem.dao.SysDemEngineDAO;
import com.iwork.core.engine.dem.service.SysDemTriggerService;
import com.iwork.core.engine.dem.service.SysDemWorkBoxService;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.engine.iform.service.SysEngineIFormMapService;
import com.iwork.core.engine.iform.service.SysEngineIFormService;
import com.iwork.core.engine.iform.service.SysEngineSubformService;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.definition.step.service.ProcessStepTriggerService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class zhuozhengAction extends ActionSupport{
	 
	
	  private SysEngineIFormService sysEngineIFormService;
	  private SysEngineIFormMapService sysEngineIFormMapService;
	  private ProcessStepTriggerService processStepTriggerService;
	  private SysEngineSubformService sysEngineSubformService;
	  private FreeMarkerConfigurer freemarderConfig;
	  private IFormService iformService;
	  private SysDemWorkBoxService sysDemWorkBoxService;
	  private SysDemTriggerService sysDemTriggerService;
	  private SysDemEngineDAO sysDemEngineDAO;
	 
	 


	
	private String NOTICEFILE;
	private String FILE_URL;
	private String RECORDID;
	private String username;
	private String FILENAME;
	private String  newfilepath; 
	private String  descript;
	
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getFILENAME() {
		return FILENAME;
	}
	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}
	public String getNOTICEFILE() {
		return NOTICEFILE;
	}
	public void setNOTICEFILE(String nOTICEFILE) {
		NOTICEFILE = nOTICEFILE;
	}
	
	public String getFILE_URL() {
		return FILE_URL;
	}
	
	public String getRECORDID() {
		return RECORDID;
	}
	public void setRECORDID(String rECORDID) {
		RECORDID = rECORDID;
	}
	public void setFILE_URL(String fILE_URL) {
		FILE_URL = fILE_URL;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	
	public String getNewfilepath() {
		return newfilepath;
	}
	public void setNewfilepath(String newfilepath) {
		this.newfilepath = newfilepath;
	}
	
	public String index() {
		
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	
		
		NOTICEFILE=this.NOTICEFILE;
		
	
		return SUCCESS;
	}
	 public String savepage() throws Exception 
	  {
		newfilepath=this.newfilepath;
		NOTICEFILE=this.NOTICEFILE;
		username=this.username;


		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		String realPath = request.getSession().getServletContext().getRealPath(newfilepath);
		
		String uuid =  UUID.randomUUID().toString().replaceAll("-", ""); 
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Integer lastIndexOf=null;
		String last=null;
		if(newfilepath.contains(".")){
			 lastIndexOf = newfilepath.lastIndexOf(".");
			 last=newfilepath.substring(lastIndexOf,newfilepath.length());
		}
		
		Integer lastIndexOf2=null;
		if(newfilepath.contains("\\")){
			 lastIndexOf2 = newfilepath.lastIndexOf("\\");
			
		}else{
			lastIndexOf2=newfilepath.lastIndexOf("/");
		};
		String name=newfilepath.substring(lastIndexOf2+1, lastIndexOf);
		String filepath=newfilepath.replace(name, uuid);
		String realfilePath = request.getSession().getServletContext().getRealPath(filepath);
		File file=new File(realfilePath);
		File f=new File(realPath);
		if(!file.exists()){
			file.createNewFile();
			
		};

				OutputStream out = null;
				InputStream content = null;
				content=new FileInputStream(f);
				out = new FileOutputStream(file, true);
				
					// TODO Auto-generated catch block
				
				int read = 0;
				byte[] bytes = new byte[10240];
				while ((read = content.read(bytes)) != -1) {
				out.write(bytes, 0, read);
				
				}
				out.flush();
				out.close();

		String uploadTime=sf.format(new Date());

		String sql3="INSERT INTO IWORK_WEBOFFCIE_MEMO (FILE_ID,DOTIME,USERNAME) VALUES (?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),?)";
		Map params2=new HashMap();
		params2.put(1, NOTICEFILE);
		params2.put(2, uploadTime);
		params2.put(3, username);
		DBUTilNew.update(sql3, params2);
		Map params=new HashMap();
		params.put(1, uuid);
		params.put(2, uuid+last);
		params.put(3, uploadTime.substring(0, 11));
		params.put(4, filepath);
		params.put(5, uuid+last);
		
		String sql=("INSERT INTO SYS_UPLOAD_FILE (FILE_ID, FILE_SAVE_NAME,UPLOAD_TIME,FILE_URL,FILE_SRC_NAME) VALUES (?,?,?,?,?)");
		DBUTilNew.update(sql, params);
		
						
		Integer last1 = uploadTime.lastIndexOf(":");
	    String desc=uploadTime.substring(0,last1);
	    String descript=username+desc;
	    Map params1=new HashMap();
	    descript=descript.replace("-", "");
	    params1.put(1, uuid);
		params1.put(2, NOTICEFILE);
		params1.put(3, uploadTime);
		params1.put(4, descript);
		params1.put(5, username);
		String sql1=("INSERT INTO iwork_weboffcie_version (FILE_ID, RECORDID,DOTIME,DESCRIPT,USERNAME) VALUES (?,?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),?,?)");
		DBUTilNew.update(sql1, params1);
		
		
		
		
		
		return "";
	  }	
	 public String getlsbb() {
		 
		 
		 
		 
		 NOTICEFILE=this.NOTICEFILE;
		 
		 
		 
		 return SUCCESS;
	 }
	 private String formid;
	 private String demId;
	 private String INSTANCEID;
	 private String NOTICETEXT;
	 public String getZhuoZLsbb(){
		 HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		  
		 NOTICEFILE=request.getParameter("id");
		 Map params=new HashMap();
		 params.put(1, NOTICEFILE);
		 String sql1="SELECT FILE_URL FROM SYS_UPLOAD_FILE  WHERE FILE_ID=?";
		 FILE_URL = DBUTilNew.getDataStr("FILE_URL", sql1, params);
		 request.setAttribute("fileurl", FILE_URL);
		 
		
		 return SUCCESS;
	}
	 public String getCjIndex(){
		 formid = this.formid; 
		 demId=this.demId;
		 INSTANCEID=this.INSTANCEID;
		 NOTICETEXT=this.NOTICETEXT;
		 NOTICEFILE=this.NOTICEFILE;
		 FILENAME=this.FILENAME;
		 return SUCCESS;
	 }
	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public String getDemId() {
		return demId;
	}
	public void setDemId(String demId) {
		this.demId = demId;
	}
	public String getINSTANCEID() {
		return INSTANCEID;
	}
	public void setINSTANCEID(String iNSTANCEID) {
		INSTANCEID = iNSTANCEID;
	}
	public String getNOTICETEXT() {
		return NOTICETEXT;
	}
	public void setNOTICETEXT(String nOTICETEXT) {
		NOTICETEXT = nOTICETEXT;
	}
	public String zhuoZhengPass(){
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
	    username = uc.get_userModel().getUserid();
	   
	    
	    NOTICEFILE=request.getParameter("id");
		
	    Map params=new HashMap();
		params.put(1, NOTICEFILE);
		String sql="SELECT FILE_URL FROM SYS_UPLOAD_FILE  WHERE FILE_ID=?";
		 FILE_URL = DBUTilNew.getDataStr("FILE_URL", sql, params);
		 int indexOf = FILE_URL.indexOf("FORM_FILE");
		 String startpath=FILE_URL.substring(0, indexOf+9);
		 String endpath=FILE_URL.substring(indexOf+9, FILE_URL.length());
		 endpath=endpath.replace("\\", "/");
		
		 Map map=new HashMap();
		 map.put("username",username);
		 map.put("NOTICEFILE",NOTICEFILE);
		 map.put("fileurl",FILE_URL);
		 map.put("startpath",startpath);
		 map.put("endpath",endpath);
		 
		 request.setAttribute("map", map);
		
		
		return SUCCESS;
	}
	public String testzhuozheng(){
		
		return SUCCESS;
	}	
		
	
}
