package com.ibpmsoft.project.zqb.action;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.ibpmsoft.project.zqb.service.DGCDService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.upload.util.DownloadFileUtil;
import com.iwork.core.util.ResponseUtil;

import org.apache.bcel.generic.RETURN;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.service.DongGuanZqbDgdyService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.ProcessAPI;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.mozilla.javascript.ContextAction;

public class DongGuanZqbDgdyAction extends ActionSupport{
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private DongGuanZqbDgdyService dongGuanZqbDgdyService;
	private int runPageNumber; // 当前页数
	private int runTotalNum; // 总页数
	private int runPageSize = 10; // 每页条数
	private int closePageNumber; // 当前页数
	private int closeTotalNum; // 总页数
	private int closePageSize = 10; // 每页条数
	private int pageNumber = 1; // 当前页数
	private int pageSize = 10; // 每页条数
	private int totalNum;
	private List<HashMap> list;
	private String  startdate;
	private String  enddate;
	private String  status;
	private String dyrId;
	private String  thxmlc;
	private Long orgroleid;
	private DGCDService dgcdService;//底稿存档的service
	private String fileUUID;//文件的uuid
	private String filesize;//文件的大小
	private String XMQY_ID;//目录id
	private String DAJYLCB_ID;//项目id
	private String DGCDURL;
	private List<HashMap> DGCDFileList;//文件集合
	private String uuid;
	private List<HashMap> DGCDList;
	private String dgcdID;//底稿存档的ID
	private String SaveOrUpdate;
	private String lockDGCD;
	private String fileName;
	private String DIVXGZL;
	private String JYRMC;
	private String INSTANCEID;
	private String ProID;
	private String ProStatus;
	private String type;
	//客户编号
	private String COMPANYNO;
	//项目类型
	private String COMPANYNAME;
	//项目名称
	private String DAMC;
	//项目编号
	private String DABH;
	//锁定人账号[锁定人姓名]
	private String JYSY;
	//是否锁定
	private String JYXS;
	//锁定时间
	private String JCSJ;
	//填报时间
	private String GHSJ;
	//填报人ID
	private String JYRID;
	
	
	private String zTreeName;//目录名
	private String XYJE;//目录父id
	private int zTreeId;//目录id
	private int DDJE; //目录顺序
	private File upload; // 上传的文件
	private String uploadContentType; // 上传文件的mimeType类型
	private String uploadFileName; // 上传文件的名称
	
	
	/*
	 * 底稿存档添加表单页面左侧文件树
	 * 王欢
	 */
	public void dgcdLeftIframe(){

		StringBuffer json = new StringBuffer();
		if(COMPANYNAME != null && !COMPANYNAME.equals("")){
		List<String> parameter = new ArrayList<String>();// 存放参数
		Map params = new HashMap();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,NAME,DDJE,"
				+ "((CASE WHEN PID=0 THEN '第'||XH||'部分' ELSE MLJB(PID,XH,?) END) || ' ' ) prefix,"
				+ "PID,XH,xmlx "
				+ " FROM ( "
				+ "SELECT ID,xmlx,DDJE,FKJD AS NAME,XYJE AS PID,ROW_NUMBER() OVER(PARTITION BY XYJE ORDER BY DDJE) XH "
				+ "FROM BD_ZQB_XMQY "
				+ " where 1=1 "
				);

		if (COMPANYNAME != null && !COMPANYNAME.equals("")) {
			sql.append(" and xmlx= ? ");
			params.put(1, COMPANYNAME);
			params.put(2, COMPANYNAME);
		}
		sql.append(
				  "START WITH ID=0 CONNECT BY XYJE = PRIOR ID"
				+ " ORDER BY PID,DDJE "
				+ ") A" );

		List lables = new ArrayList();
		lables.add("id");
		lables.add("pId");
		lables.add("name");
		lables.add("prefix");
		lables.add("DDJE");

		List<HashMap> list = DBUTilNew.getDataList(lables, sql.toString(), params);
		//手动添加目录根
		HashMap mapRoot = new HashMap();
		mapRoot.put("id", 0);
		mapRoot.put("pId", -1);
		mapRoot.put("name", "目录根");
		mapRoot.put("prefix", 1 );
		mapRoot.put("DDJE", 1 );
		list.add(mapRoot);
		
		
		JSONArray jsonArray =JSONArray.fromObject(list);
		json.append(jsonArray);
		json.toString();
		}else{
			json.append("");
		}
		ResponseUtil.write(json.toString());
	}
	
	/*
	 * 修改项目的目录树 数据
	 */
	public void showProjectTree() {
		StringBuffer json = new StringBuffer();
		if (dgcdID != null && !dgcdID.equals("")) {
			List<String> parameter = new ArrayList<String>();// 存放参数
			Map params = new HashMap();

			StringBuffer sql = new StringBuffer();
			
//			sql.append("select CGS as id ,CGBL as pId,KHMC as name,XSGFS  from BD_GF_CGXZ where 1=1 ");
			sql.append(" SELECT ID,NAME,((CASE WHEN PID = 0 THEN "
					+ " '第' || XH || '部分' ELSE MLJBXM(PID, XH,?) "
					+ " END) || ' ') prefix, PID, XH,XSGFS, xmlx FROM ( "
					+ " SELECT  ID,xmlx,NAME,XSGFS,PID,ROW_NUMBER() OVER(PARTITION BY PID ORDER BY XH) XH FROM "
					+ " (SELECT CGS ID,KHBH xmlx,KHMC AS NAME,CGBL AS PID,XSGFS XH,XSGFS FROM BD_GF_CGXZ  where WXSGF=?) A "
					+ " START WITH ID = 0 CONNECT BY PID = PRIOR ID ORDER BY PID,XH) A ");
			
			params.put(1, dgcdID );
			params.put(2, dgcdID );
			
			List lables = new ArrayList();
			lables.add("id");
			lables.add("pId");
			lables.add("name");
			lables.add("prefix");
			lables.add("XSGFS");
			
			List<HashMap> list = DBUTilNew.getDataList(lables, sql.toString(), params);
			
			JSONArray jsonArray =JSONArray.fromObject(list);
			json.append(jsonArray);
			json.toString();
			}else{
				json.append("");
			}
			ResponseUtil.write(json.toString());
		
	}

	
	/*
	 * 跳转修改项目 树jsp
	 */
	public String showProject() {return SUCCESS;}
	

	//王欢创建修改项目节点名称方法
	public void dgcdZreeProjectRename(){
		JSONObject json =new JSONObject();
		
		String sql2= "update BD_GF_CGXZ set KHMC =? where WXSGF =? and CGS=?";
		Map params = new HashMap();
		params.put(1, zTreeName);
		params.put(2, dgcdID);
		params.put(3, zTreeId);

		int num = DBUTilNew.update(sql2, params);
		if(num==0){
			String reslut = "修改失败";
			json.put("reslut", "error");
			json.put("message", reslut);
		}else{
			json.put("reslut", "success");
			json.put("message", "修改成功");
		}
		ResponseUtil.write(json.toString());
	}
	
	

	// 王欢创建删除项目节点方法
	public void dgcdZreeProjectDelete() {
		JSONObject json = new JSONObject();
		if (dgcdID != null && !dgcdID.equals("")) {
			String reslut = dongGuanZqbDgdyService.dgcdZreeProjectDelete(zTreeId, dgcdID.trim());
			if (reslut.length() > 0) {
				json.put("reslut", "error");
				json.put("message", reslut);
			} else {
				json.put("reslut", "success");
				json.put("message", "删除成功");
			}
		} else {
			json.put("reslut", "error");
			json.put("message", "项目id为空");
		}
		ResponseUtil.write(json.toString());
	}
	

	
	//王欢创建添加项目目录
		public void dgcdZreeProjectAdd(){
			JSONObject json =new JSONObject();
			/*参数  父id 以及目录名 
			 * 函数 ADDCATALOGITEM(父级id,项目id,排序id,目录名字)
			 * 父级id number  目录名字 VARCHAR2   排序id 默认在在最后则传-1
			*/
			if(dgcdID!=null&&!dgcdID.equals("")){
				String sql = "select ADDCATALOGITEM(?,?,-1,?) as id from dual";
				Map params = new HashMap();
				params.put(1, XYJE);  //父id
				params.put(2, dgcdID); //项目id
				params.put(3, "默认目录名"); //目录名字
				int num = DBUTilNew.getInt("id", sql, params);
				json.put("reslut", num);
			}else{
				json.put("reslut", 0);
			}
			ResponseUtil.write(json.toString());
		}
		
		
		/*
		 * 在选中锁定过的项目目录树选中节点
		 * 上方插入一个目录
		 */
		public void dgcdZreeProjectInsert(){
				JSONObject json =new JSONObject();
				/*参数  父id 以及目录名 
				 * 函数 ADDCATALOGITEM(父级id,项目id,排序id,目录名字)
				 * 父级id number  目录名字 VARCHAR2   排序id 默认在在最后则传-1
				*/
				String sql = "select ADDCATALOGITEM(?,?,?,?) as id from dual";
				Map params = new HashMap();
				params.put(1, XYJE);  //父id
				params.put(2, dgcdID); //项目id
				params.put(3, DDJE); //排序id
				params.put(4, "默认目录名"); //目录名字
				int num = DBUTilNew.getInt("id", sql, params);
				json.put("reslut", num);
				ResponseUtil.write(json.toString());
			}
	
	
	
	//王欢创建修改节点名称方法
	public void dgcdZreeRename(){
		JSONObject json =new JSONObject();
		
		String sql2= "update BD_ZQB_XMQY set FKJD =? where ID =?";
		Map params = new HashMap();
		params.put(1, zTreeName);
		params.put(2, zTreeId);

		int num = DBUTilNew.update(sql2, params);
		if(num==0){
			String reslut = "修改失败";
			json.put("reslut", "error");
			json.put("message", reslut);
		}else{
			json.put("reslut", "success");
			json.put("message", "修改成功");
		}
		ResponseUtil.write(json.toString());
	}
	
	
	
	
	//王欢创建删除节点方法
	public void dgcdZreeDelete(){
		JSONObject json =new JSONObject();
		String reslut = dongGuanZqbDgdyService.dgcdZreeDelete(zTreeId);
		if(reslut.length()>0){
			json.put("reslut", "error");
			json.put("message", reslut);
		}else{
			json.put("reslut", "success");
			json.put("message", "删除成功");
		}
		ResponseUtil.write(json.toString());
	}
	//王欢创建添加目录
	public void dgcdZreeAdd(){
		JSONObject json =new JSONObject();
		/*参数  父id 以及目录名 
		 * 函数 ADDCATALOG(父级id,目录名字,排序id)
		 * 父级id number  目录名字 VARCHAR2   排序id 默认在在最后则传-1
		*/
		String sql = "select ADDCATALOG(?,?,-1,?,0) as id from dual";
		Map params = new HashMap();
		params.put(1, XYJE);
		params.put(2, "默认目录名");
		params.put(3, COMPANYNAME);
		int num = DBUTilNew.getInt("id", sql, params);
		json.put("reslut", num);
		ResponseUtil.write(json.toString());
	}
	
	/*
	 * 在选中目录上方插入一个目录
	 */
	public void dgcdZreeInsert(){
			JSONObject json =new JSONObject();
			/*参数  父id 以及目录名 
			 * 函数 ADDCATALOG(父级id,目录名字,排序id)
			 * 父级id number  目录名字 VARCHAR2   排序id 默认在在最后则传-1
			*/
			String sql = "select ADDCATALOG(?,?,?,?,0) as id from dual";
			Map params = new HashMap();
			params.put(1, XYJE);
			params.put(2, "默认目录名");
			params.put(3, DDJE);
			params.put(4, COMPANYNAME);
			int num = DBUTilNew.getInt("id", sql, params);
			json.put("reslut", num);
			ResponseUtil.write(json.toString());
		}
	
	
	//excel 导入目录 王欢
	public void uploadFile() {
		// 获取文件保存的地址
		String path = ServletActionContext.getServletContext().getRealPath("/upload"); // 得到upload目录在tomcat的绝对磁盘路径
		String msg = dgcdService.subSheetExcelImp(upload);
		//插入表成功 执行存储过程
		if(msg.indexOf("TREE_EXCEL")>=0){
			msg = dgcdService.inuptExcel(msg,COMPANYNAME);
		}
		if(msg.indexOf("error")>=0){
			ResponseUtil.write("error");
		}else{
			ResponseUtil.write(msg);
		}
		
		
	}
	
	
	public String dgcdZreeExcelImp()
	{
		return SUCCESS;
	}
	
	public String add_dgcdZtree(){
		return SUCCESS;
	}


	//底稿存档的上传跳转
	public String uploadifyDGCDFile() {
		return "success";
	}
	//这个是项目档案查询 2018-6-19  11:16
	public String dgcdAction() {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		//获取指定用户用于>>锁定权限的显示
		String userStr=config.get("GZDGCDSDR");
		String[] gzdgcdsdr = new String[0];
		if(userStr!=null){
			gzdgcdsdr = config.get("GZDGCDSDR").split(",");
		}
		//默认锁定按钮>>隐藏
		this.lockDGCD = "NO";
		//获取用户信息
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		//特定用户拥有锁定权限
		for (String name : gzdgcdsdr) {
			if (user.getUserid().equalsIgnoreCase(name)) {
				this.lockDGCD = "YES";
				break;
			}
		}
		if (pageNumber == 0) pageNumber = 1;
		//获取底稿存档信息
		List<Object> list = dgcdService.getDGCDList(DAMC, type, pageSize, pageNumber);
		//获得页面所有信息数据
		if (list.get(0) != null) this.DGCDList = (List<HashMap>) list.get(0);
		//获得分页的信息总数信息总数
		if (list.get(1) != null) this.totalNum = Integer.parseInt(list.get(1).toString());
		return SUCCESS;
	}
	//上传文件
	public String upfileDGCD() throws Exception {
	    //用户信息
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String format1 = format.format(date);
		//fileUUID:文件上传表的id;user.getUsername():用户名称;user.getUserid():用户id; XMQY_ID:目录ID;DAJYLCB_ID : 底稿存档项目id
		boolean b = dgcdService.InsertDGCDFile(fileUUID, user.getUsername(), user.getUserid(), filesize, XMQY_ID, DAJYLCB_ID, format1);
		if(!b) throw new Exception("上传底稿存档文件异常");
		boolean b1 = dgcdService.updateDGCDURL(fileUUID, DGCDURL);
		if(!b1) throw new Exception("上传底稿存档文件异常");
		return SUCCESS;
	}

	//根据目录获取文件
	public String selectDGCDFile() throws Exception {
		List<HashMap> list = dgcdService.selectDGCDFile(XMQY_ID,DAJYLCB_ID);
		this.DGCDFileList=list;
		return SUCCESS;
	}
	//根据uuid下载文件
	public String downloadDGCDFile() throws Exception {
		//通过uuid数组得到上传表的信息
		List<HashMap> list = dgcdService.downloadDGCDFile(uuid.split(","));
		String XMQY_NAME = "默认名称" ;
		if (XMQY_ID != null)XMQY_NAME = dgcdService.getXMQYName(XMQY_ID);
		String realPath1 = ServletActionContext.getServletContext().getRealPath("/");
		HttpServletResponse response = ServletActionContext.getResponse();
		if (list.size() > 1) {
			DownloadFileUtil downloadFile = null;
			try {
				//打包下载
				//创建工具类,传根目录的绝对路径,和response对象
				downloadFile = new DownloadFileUtil(realPath1, XMQY_NAME+".zip", XMQY_NAME, response);
				//进入工具类查看具体情况
				boolean files = downloadFile.getFiles(list);
				if (files) {
					downloadFile.compressDir(null);
					boolean b = downloadFile.toQianTai();
					downloadFile.deleteTemporary();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (downloadFile != null) downloadFile.deleteTemporary();
			}
		} else if (list.size() == 1) {
			//单个下载
			//创建工具类,reponse
			String file_url = realPath1 + File.separator + (String) list.get(0).get("FILE_URL");
			DownloadFileUtil downloadFile = new DownloadFileUtil(file_url, (String) list.get(0).get("FILE_SRC_NAME"), response);
			boolean file_src_name = downloadFile.toQianTai();
			if (!file_src_name) {
				System.out.println("不存在");
			}
		}
		return SUCCESS;
	}
	//根据底稿存档ID下载文件  getXMQYParrentName
	public String downloadDGCDFileForDGCDID() throws Exception {
		//去除小数点
		if(dgcdID.contains("."))this.dgcdID=dgcdID.substring(0,dgcdID.indexOf("."));
		//获得文件的路径到项目id文件夹
		String filePath ="iwork_file"+File.separator+"XMCD"+File.separator;
		//从数据库中查询到的文件名称
		String sqlFileName="";
		//获取项目的名称(DAMC)和编号(DABH)
		List<HashMap> list= dgcdService.getDAMCToDAJYLCBID(dgcdID);
		//在BD_ZQB_DAJYLCB中根据项目id获取客户存储文件名称
		List<HashMap> listName= dgcdService.getDGCDIDFileName(dgcdID);
		//根据项目的id查询文件的信息,在这里获取该项目所涉及的所有目录
		List<HashMap> ZKNHXZRYB = dgcdService.selectBD_ZQB_ZKNHXZRYB(null, dgcdID);
		//根据目录id查询所有目录id  然后把获得的目录id做成非重复的set集合;
		TreeSet<String> setID=new TreeSet<String>();
		for (int i=0;i<ZKNHXZRYB.size();i++){
			setID.add(ZKNHXZRYB.get(i).get("roleid").toString());
		}
		//
		filePath +=(String)list.get(0).get("DABH");
		//项目根的绝对路径
		String realPath = ServletActionContext.getServletContext().getRealPath("/");
		HttpServletResponse response = ServletActionContext.getResponse();
		DownloadFileUtil downloadFile=null;
		try{
			if(list.size()>=1){
				downloadFile=new DownloadFileUtil(realPath,(String)list.get(0).get("DAMC")+".zip",response,(String)list.get(0).get("DAMC"));
				//通过集合多次查询得到每次查询的目录id的父节点压缩替换再次查询
				for(String aa:setID){
					//根据目录id查询所有的有关父节点
					List<HashMap> xmqyParrentName = dgcdService.getXMQYParrentName(aa);
					//拼接的目录路径
					String CompressCatalog=realPath + filePath +File.separator+aa;
					//拼接父节点
					String ParentNode="";
					for(int j=0;j<xmqyParrentName.size();j++){
						ParentNode=xmqyParrentName.get(j).get("FKJD")+File.separator+ParentNode;
					}
					downloadFile.compressDir(listName,CompressCatalog,ParentNode);
				}
					downloadFile.closeZipOutPutStream();

				boolean b = downloadFile.toQianTai();
				downloadFile.deleteTemporary();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(downloadFile!=null)downloadFile.deleteTemporary();
		}
		return SUCCESS;
	}
	//根据uuid删除文件(按照项目id删除文件是调用此方法)
	public String deleteDGCDFile() throws Exception {
		DownloadFileUtil down=new DownloadFileUtil(ServletActionContext.getResponse());
		HashMap map=new HashMap();
		if(uuid != null && uuid !=""){
		List<HashMap> urlMap = dgcdService.deleteDGCDFile(uuid.split(","));
			if(urlMap!=null){
				int success=0;
				int fail=0;
				String failStr="";
				for(int i=0;i<urlMap.size();i++){
					String realPath = ServletActionContext.getServletContext().getRealPath((String) urlMap.get(i).get("FILE_URL"));
					File file=new File(realPath);
					if (file.exists() && file.isFile()) {
						file.delete();
						success++;
					}else{
						fail++;
						failStr+=(String) urlMap.get(i).get("FILE_SRC_NAME")+",";
					}
				}
				map.put("success","删除成功:"+success);
				map.put("fail","删除失败:"+fail);
				map.put("failStr","删除失败文件名称:"+failStr);
			}else{
				map.put("fail","删除文件出现错误");
			}
		}else{
			map.put("fail","删除文件接收参数为空");
		}
		down.setQianTaiDate(map);
		return SUCCESS;

	}

	//这个是修改项目档案的跳转页面 2018-6-19  11:16
	public String dgcdOneAction(){
		if(dgcdID.contains("."))this.dgcdID=dgcdID.substring(0,dgcdID.indexOf("."));
		DGCDList=dgcdService.getDGCDList(dgcdID);
		return SUCCESS;
	}
	//这个是添加底稿
	public void dgcdInsert() throws Exception {
		//添加的信息
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String name=user.getUsername();
		String id=user.getUserid();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String format1 = format.format(date);
		boolean b = dgcdService.InsertDGCD(DAMC, COMPANYNAME,DABH,id,name,format1);
		//添加的信息结束
		//这个是修改页面信息
		this.SaveOrUpdate="update";
		//这个是获取最大的id  也就是刚刚添加的id 并且返回给修改页面
		Integer maxIDDGCD = dgcdService.getMaxIDDGCD();
		this.dgcdID=""+maxIDDGCD;
		HttpServletResponse response = ServletActionContext.getResponse();
		String data="{\"data\":{\"COMPANYNAME\":\""+COMPANYNAME+"\",\"dgcdID\":\""+dgcdID+"\",\"SaveOrUpdate\":\"update\",\"DABH\":\""+DABH+"\"} ,\"status\":\"insert\"}";
		//String data="{data:{dgcdID:"+dgcdID+",SaveOrUpdate:update,DABH:"+DABH+"} ,status:insert}";
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(data);
		writer.flush();
		writer.close();
	}
	//这个是添加底稿中选择项目
	public String  dgcdInsertSelectPro() throws Exception {
		this.list = dgcdService.dgcdInsertSelectPro(ProID, ProStatus, pageSize, pageNumber);
		this.totalNum=dgcdService.getDGCDSelectMaxID(ProID, ProStatus);
		return SUCCESS;
	}
	//这个是锁定的开关 2018-6-22 16:19
	public void lockDGCD() throws Exception {
		if(dgcdID.contains("."))this.dgcdID=dgcdID.substring(0,dgcdID.indexOf("."));
		if(this.JYXS.equals("Y")){
			//锁表传数据到
			boolean b1 = dgcdService.locaCGXZ(dgcdID,COMPANYNAME);
		}
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String format1 = format.format(date);
		Boolean b=dgcdService.lockDGCD(dgcdID, JYXS,user.getUsername(),format1);
		if(b) return;
		throw new Exception("锁定修改异常");
	}
	//这个是修改底稿存档 2018-6-22 16:19
	public void updateDGCD() throws Exception {
		if(dgcdID.contains("."))this.dgcdID=dgcdID.substring(0,dgcdID.indexOf("."));
		Boolean b=dgcdService.updateDGCD(dgcdID, COMPANYNAME, DAMC, DABH, JYSY, JYXS, JCSJ, GHSJ, JYRMC);
		this.DGCDList = dgcdService.getDGCDList(dgcdID);
		if(b) return;
		throw new Exception("删除底稿异常");
	}
	//这个是删除底稿存档 2018-6-21 15:19
	public void deleteDGCD() throws Exception {
		//在BD_ZQB_DAJYLCB中根据项目id获取客户存储uuid>>文件表中的id
		List<HashMap> ZKNHXZRYB = dgcdService.selectBD_ZQB_ZKNHXZRYB(null, dgcdID);
		String str="uuid";
		for (HashMap mapUUID:ZKNHXZRYB) {
			str+=","+(String)mapUUID.get("departmentname");
		}
		this.uuid=str;
		this.deleteDGCDFile();
		//以上是调用uuid删除方法用于删除文件和表
		//这个用于获取项目的编号的名字
//		List<HashMap> listName= dgcdService.getDAMCToDAJYLCBID(dgcdID);
//		if(listName!=null){
//			File file=new File(ServletActionContext.getServletContext().getRealPath("/")+File.separator+"iwork_file"+File.separator+"XMCD"+File.separator+listName.get(0).get("DABH"));
//			if(file.exists() && file.mkdir()){
//				file.delete();
//			}
//		}
		if(dgcdID.contains("."))this.dgcdID=dgcdID.substring(0,dgcdID.indexOf("."));
		Boolean b=dgcdService.deleteDGCD(dgcdID);
		if(b) return;
		throw new Exception("删除底稿存档异常");
	}
	//跳转到添加页面
	public String dgcdAddAction(){
		DGCDList=null;
		return "success";
	}
	//这个是dgcdMain中右侧的iframe跳转路径以及获取要显示下载文件路径
	public String dgcdIframeAction(){
		List<HashMap> list = dgcdService.selectDGCDFile(XMQY_ID,DAJYLCB_ID);
		this.DGCDFileList=list;
		return "success";
	}

	public String getDgdyList(){
		thxmlc = ProcessAPI.getInstance().getProcessActDefId("DGDYLC");
		list=dongGuanZqbDgdyService.getDgdyList(startdate,enddate,status,dyrId,pageSize, pageNumber);
		totalNum=dongGuanZqbDgdyService.getDgdyListSize(startdate,enddate,status,dyrId);
		orgroleid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getOrgroleid();
		return SUCCESS;
	}
	public void dgdyToExcl(){
		HttpServletResponse response = ServletActionContext.getResponse();
		dongGuanZqbDgdyService.thxmexportexcl(response,startdate,enddate,dyrId,status);
	}

	
	public String getThxmlc() {
		return thxmlc;
	}

	public Long getOrgroleid() {
		return orgroleid;
	}
	public void setOrgroleid(Long orgroleid) {
		this.orgroleid = orgroleid;
	}
	public void setThxmlc(String thxmlc) {
		this.thxmlc = thxmlc;
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

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public List<HashMap> getList() {
		return list;
	}

	public void setList(List<HashMap> list) {
		this.list = list;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDyrId() {
		return dyrId;
	}

	public void setDyrId(String dyrId) {
		this.dyrId = dyrId;
	}

	public DongGuanZqbDgdyService getDongGuanZqbDgdyService() {
		return dongGuanZqbDgdyService;
	}

	public void setDongGuanZqbDgdyService(
			DongGuanZqbDgdyService dongGuanZqbDgdyService) {
		this.dongGuanZqbDgdyService = dongGuanZqbDgdyService;
	}

	public DGCDService getDgcdService() {
		return dgcdService;
	}

	public void setDgcdService(DGCDService dgcdService) {
		this.dgcdService = dgcdService;
	}

	public String getFileUUID() {
		return fileUUID;
	}

	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getXMQY_ID() {
		return XMQY_ID;
	}

	public void setXMQY_ID(String XMQY_ID) {
		this.XMQY_ID = XMQY_ID;
	}

	public String getDAJYLCB_ID() {
		return DAJYLCB_ID;
	}

	public void setDAJYLCB_ID(String DAJYLCB_ID) {
		this.DAJYLCB_ID = DAJYLCB_ID;
	}

	public String getDGCDURL() {
		return DGCDURL;
	}

	public void setDGCDURL(String DGCDURL) {
		this.DGCDURL = DGCDURL;
	}

	public List<HashMap> getDGCDFileList() {
		return DGCDFileList;
	}

	public void setDGCDFileList(List<HashMap> DGCDFileList) {
		this.DGCDFileList = DGCDFileList;
	}

	public List<HashMap> getDGCDList() {
		return DGCDList;
	}

	public void setDGCDList(List<HashMap> DGCDList) {
		this.DGCDList = DGCDList;
	}

	public String getDgcdID() {
		return dgcdID;
	}

	public void setDgcdID(String dgcdID) {
		this.dgcdID = dgcdID;
	}

	public String getSaveOrUpdate() {
		return SaveOrUpdate;
	}

	public void setSaveOrUpdate(String saveOrUpdate) {
		SaveOrUpdate = saveOrUpdate;
	}

	public String getLockDGCD() {
		return lockDGCD;
	}

	public void setLockDGCD(String lockDGCD) {
		this.lockDGCD = lockDGCD;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDIVXGZL() {
		return DIVXGZL;
	}

	public void setDIVXGZL(String DIVXGZL) {
		this.DIVXGZL = DIVXGZL;
	}

	public String getJYRMC() {
		return JYRMC;
	}

	public void setJYRMC(String JYRMC) {
		this.JYRMC = JYRMC;
	}

	public String getINSTANCEID() {
		return INSTANCEID;
	}

	public void setINSTANCEID(String INSTANCEID) {
		this.INSTANCEID = INSTANCEID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCOMPANYNO() {
		return COMPANYNO;
	}

	public void setCOMPANYNO(String COMPANYNO) {
		this.COMPANYNO = COMPANYNO;
	}

	public String getCOMPANYNAME() {
		return COMPANYNAME;
	}

	public void setCOMPANYNAME(String COMPANYNAME) {
		this.COMPANYNAME = COMPANYNAME;
	}

	public String getDAMC() {
		return DAMC;
	}

	public void setDAMC(String DAMC) {
		this.DAMC = DAMC;
	}

	public String getDABH() {
		return DABH;
	}

	public void setDABH(String DABH) {
		this.DABH = DABH;
	}

	public String getJYSY() {
		return JYSY;
	}

	public void setJYSY(String JYSY) {
		this.JYSY = JYSY;
	}

	public String getJYXS() {
		return JYXS;
	}

	public void setJYXS(String JYXS) {
		this.JYXS = JYXS;
	}

	public String getJCSJ() {
		return JCSJ;
	}

	public void setJCSJ(String JCSJ) {
		this.JCSJ = JCSJ;
	}

	public String getGHSJ() {
		return GHSJ;
	}

	public void setGHSJ(String GHSJ) {
		this.GHSJ = GHSJ;
	}

	public String getJYRID() {
		return JYRID;
	}

	public void setJYRID(String JYRID) {
		this.JYRID = JYRID;
	}

	public String getProID() {
		return ProID;
	}

	public void setProID(String proID) {
		ProID = proID;
	}

	public String getProStatus() {
		return ProStatus;
	}

	public void setProStatus(String proStatus) {
		ProStatus = proStatus;
	}

	public String getzTreeName() {
		return zTreeName;
	}

	public void setzTreeName(String zTreeName) {
		this.zTreeName = zTreeName;
	}

	public String getXYJE() {
		return XYJE;
	}

	public void setXYJE(String xYJE) {
		XYJE = xYJE;
	}

	public int getzTreeId() {
		return zTreeId;
	}

	public void setzTreeId(int zTreeId) {
		this.zTreeId = zTreeId;
	}

	public int getDDJE() {
		return DDJE;
	}

	public void setDDJE(int dDJE) {
		DDJE = dDJE;
	}
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
