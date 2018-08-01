package com.iwork.plugs.cms.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.model.IworkCmsComment;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.service.CmsChannelService;
import com.iwork.plugs.cms.service.CmsInfoService;
import com.iwork.plugs.cms.service.CmsPortletService;
import com.iwork.plugs.cms.util.CmsCommentUtil;
import com.iwork.plugs.cms.util.CmsPageModelUtil;
import com.iwork.plugs.cms.util.CmsUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionSupport;

/**
 * CMS内容管理跳转类
 * 
 * @author WeiGuangjian
 * 
 */
public class CmsInfoAction extends ActionSupport {
	private static final Log log = LogFactory.getLog(CmsInfoAction.class);
	private CmsPortletService cmsPortletService;
	private CmsChannelService cmsChannelService;
	private CmsInfoService cmsInfoService;
	private String ch;
	private String portletid;
	private String infoid;
	private String infotitle;
	private String brieftitle;
	private String browse;
	private String source;
	private String prepicture;
	private String precontent;
	private String content;
	private long istalk;
	private long iscopy;
	private long markred;
	private long markbold;
	private long marktop;
	private String keyword;
	private long status;
	private String archives;
	private String pstrScript;
	private String type;
	private String release;
	private IworkCmsContent model;
	private List<IworkCmsPortlet> portletList;
	private String infolist;
	private String contentid;
	private String copy;
	private String cmsdate;
	private File imgFile;
	private String releaseman;//获取发布人
	private String title;//获取发布标题
	private String releasedate;//获取开始发布日期
	private String enddate;//获取结束发布日期
	private static final String SEENUUID = "ef95ed9b94fe4a12b48bbd77982e8c26";  //已阅人主数据模型UUID
	private static final String OAUUID = "8a6060e4ddb74e269239d4a259afe57d";  //OA栏目分类主数据模型UUID
	private List<HashMap> list;//获取已阅人
	private int total;//获取已阅人总数
	private String sort;//获取栏目分类
	private List<IworkCmsContent> sorts ;
	/**
	 * 文件名称
	 */
	private String imgFileFileName;

	/**
	 * 图片宽度
	 */
	private String imgWidth;

	/**
	 * 图片高度
	 */
	private String imgHeight;

	/**
	 * 图片对齐方式
	 */
	private String align;

	/**
	 * 图片标题
	 */
	private String imgTitle;

	/**
	 * 分页类
	 */
	protected Page page = new Page();

	/**
	 * 总页数
	 */
	protected int totalPages;

	/**
	 * 显示第几页
	 */
	protected int curPage;

	/**
	 * 总记录数
	 */
	protected int totalRecords;

	/**
	 * 保存实际的数据
	 */
	protected List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();

	/**
	 * 评论相关
	 */
	int contentId;
	String talkName;
	String talkContent;
	String commentHtml;
	long commentId;
	String userName;
	int pageNo;
	int pageSize;
	String pagePagingHtml;

	/**
	 * 主页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String index() throws Exception {
		String infolist = cmsInfoService.getGridScript(portletid, releaseman,title,releasedate,enddate);
		this.setPortletid(portletid);
		this.setInfolist(infolist);
		return "index";
	}

	/**
	 * 获取表格内容
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cmsInfoGrid() throws Exception {
		Page page = this.getPage();
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = cmsInfoService.getDataBoxJson(portletid, page,releaseman,title,releasedate,enddate);
		request.setAttribute("Cms_Info_Grid",
				json.substring(1, json.length() - 1));
		return SUCCESS;
	}
	/**
	 * 获得资讯列表数据
	 */
	public void showCmsInfoList(){
		if(portletid!=null){
			if(pageSize==0){pageSize = 10;}
			String json = cmsInfoService.getCmsInfoListJson(portletid, pageSize,pageNo);
			ResponseUtil.write(json);
		}
	}
	/**
	 * 正文图片上传
	 * 
	 * @return
	 */
	public String uploadImg() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		// 文件保存目录路径
		String savePath = ServletActionContext.getServletContext().getRealPath(
				"/")
				+ "iwork_file/CMS_FILE/";
		// 文件保存目录URL
		String saveUrl = request.getContextPath() + "/iwork_file/CMS_FILE/";
		// 定义允许上传的文件扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };
		// 最大文件大小
		long maxSize = 2000000;
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (Exception e) {
			log.error(e);
		}

		if (imgFile == null) {
			out.println(getError("请选择文件。"));
			return null;
		}

		// 检查目录
		File uploadDir = new File(savePath);
		if (!uploadDir.isDirectory()) {
			out.println(getError("上传目录不存在。"));
			return null;
		}
		// 检查目录写权限
		if (!uploadDir.canWrite()) {
			out.println(getError("上传目录没有写权限。"));
			return null;
		}
		// 创建文件夹
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String fileExt = imgFileFileName.substring(
				imgFileFileName.lastIndexOf(".") + 1).toLowerCase();
		if (!Arrays.<String> asList(fileTypes).contains(fileExt)) {
			out.println(getError("上传文件扩展名[" + fileExt + "]是不允许的扩展名。"));
			return null;
		}
		if (imgFile.length() > maxSize) {
			out.println(getError("[ " + imgFileFileName + " ]超过单个文件大小限制，文件大小[ "
					+ imgFile.length() + " ]，限制为[ " + maxSize + " ] "));
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_"
				+ new Random().nextInt(1000) + "." + fileExt;
		File uploadedFile = new File(savePath, newFileName);
		try {
			FileUtil.copyFile(imgFile, uploadedFile);
			JSONObject obj = new JSONObject();
			obj.put("error", 0);
			obj.put("url", saveUrl + newFileName);
			out.println(obj.toString());
		} catch (Exception e) {
			log.error("图片上传失败:" + e);
		}
		return null;
	}

	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toString();
	}

	/**
	 * 删除内容
	 * 
	 * @return
	 * @throws Exception
	 */
	public void cmsInfoRemove() throws Exception {
		cmsInfoService.cmsInfoRemove(infoid);
	}

	/**
	 * 创建资讯发布页面
	 * 
	 * @return
	 */
	public String newCmsInfoPage() {
		List<HashMap> sortList=null;
		HashMap conditionMap = new HashMap();
		 sorts=new ArrayList<IworkCmsContent>();
		//IworkCmsContent iworkCmsContent=new IworkCmsContent();
		if (portletid != null) {
			model = cmsInfoService.initContentModel(Long.parseLong(portletid));
			portletList = cmsPortletService.getCmsPortletDAO().getAllList();
			/*//根据所属栏目查询其下分类
			conditionMap.put("LMMC", portletid);
			//获取栏目下对应的分类
			sortList=DemAPI.getInstance().getAllList(OAUUID, conditionMap, null);
		  if(sortList!=null&&sortList.size()>0){
			  for(HashMap hash:sortList){
				  IworkCmsContent cmsContent = new IworkCmsContent();
				  String sort=hash.get("FL")==null?"":hash.get("FL").toString();
				  cmsContent.setSort(sort);
				  sorts.add(cmsContent);
				}
		  }*/
			
		}
		return SUCCESS;
	}

	/**
	 * 发布内容页面
	 * 
	 * @return
	 */
	public void contentDeploy() {
		boolean flag = false;
		if (model != null) {
			flag = cmsInfoService.cmsInfoAdd(model);
		}
		if (flag) {
			ResponseUtil.write(SUCCESS);
		} else {
			ResponseUtil.write(ERROR);
		}

	}

	/**
	 * 发布内容页面
	 * 
	 * @return
	 */
	public String cmsRelease() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String sb = uc._userModel.getDepartmentname() + "/"
				+ uc._userModel.getUsername();
		this.setRelease(sb);
		this.setPortletid(portletid);
		return "release";
	}

	/**
	 * 更新内容页面
	 * 
	 * @return
	 */
	public String cmsUpdate() {
		List<HashMap> sortList=null;
		HashMap conditionMap = new HashMap();
		model = cmsInfoService.getModel(infoid);
		sorts=new ArrayList<IworkCmsContent>();
		//根据所属栏目查询其下分类
		conditionMap.put("LMMC",model.getChannelid());
		//获取栏目下对应的分类
		sortList=DemAPI.getInstance().getAllList(OAUUID, conditionMap, null);
	    if(sortList!=null&&sortList.size()>0){
		  for(HashMap hash:sortList){
			  IworkCmsContent cmsContent = new IworkCmsContent();
			  String sort=hash.get("FL")==null?"":hash.get("FL").toString();
			  cmsContent.setSort(sort);
			  sorts.add(cmsContent);
			}
	  }
		portletList = cmsPortletService.getCmsPortletDAO().getAllList();
		return "update";
	}

	/**
	 * 打开内容显示页面
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String cmsOpen() throws ParseException {
		istalk = 1l;
		IworkCmsContent model = cmsInfoService.getModel(infoid);
		
		if(model==null){
			return ERROR;
		}
		String copy = "";
		if (model.getIscopy() == 1) {
			copy = "oncontextmenu='return false;' ondragstart='return false;' onselectstart ='return false' onselect='document.selection.empty()' oncopy='document.selection.empty();' onbeforecopy='return false;' onmouseup='document.selection.empty()'";
		}
		if (model.getReleasedate() != null)
			this.setCmsdate(CmsUtil.timeFormat(model.getReleasedate()));
		if (model.getPrepicture() != null && model.getPrepicture() != "") {
			StringBuffer pic = new StringBuffer();
			FileUpload file = FileUploadAPI.getInstance().getFileUpload(model.getPrepicture());
			if(file!=null){
				pic.append("<img id=\"cmsFileUploadUrl\" onerror=\"setNopic(this)\" src=\"").append(file.getFileUrl()).append("\" class=\"news_img\"></img>");
			}
			
			this.setPrepicture("<div class=\"subpic\">"+pic.toString()+"</div>");
		}
		if (model.getPrecontent() != null && model.getPrecontent() != "") {
			this.setPrecontent("<div class=\"guide\">" + model.getPrecontent()
					+ "</div>");
		}
		if (model.getContent() != null && model.getContent().toString() != "") {
			this.setContent("<div class=content>" + model.getContent()
					+ "</div>");
		}
		if (model.getArchives() != null && model.getArchives() != "") {
			StringBuffer filelist = new StringBuffer();
			List<FileUpload>  list = FileUploadAPI.getInstance().getFileUploads(model.getArchives());
			if(list!=null&&list.size()>0){
				filelist.append("<div class=archive>");
				filelist.append("附件:");
				for(FileUpload upload:list){
					String suffix = FileUtil.getFileExt(upload.getFileSrcName());
					String img = WebUIUtil.getLinkIcon(suffix);
					filelist.append(img).append("<span><a href=\"uploadifyDownload.action?fileUUID=").append(upload.getFileId()).append("\">").append(upload.getFileSrcName()).append("</a></span><br/>");
				}
			}
			filelist.append("</div>");
			this.setArchives(filelist.toString());
		}
		this.setModel(model);
		this.setCopy(copy);
		//评论
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();
		if(pageSize == 0){
			pageSize = 10;
		}
		if(pageNo == 0){
			pageNo = 1;
		}
		
		if(model.getIstalk()!=null&&model.getIstalk().equals(new Long(0))){
			istalk = model.getIstalk();
			commentHtml = CmsCommentUtil.getInstance().getCommentHtmlStr(infoid,
					user.getUserid(), pageNo, pageSize);
			userName = user.getUsername();
			int dataCount = CmsCommentUtil.getInstance().getCommentCount(infoid);
			//评论分页
			if(dataCount > 0){
				String url = "cmsOpen.action?infoid=" + infoid + "&";
				pagePagingHtml = CmsPageModelUtil.getInstance().getPageModelHtmlStr(url, dataCount, pageSize, pageNo);
			}
		}else{
			commentHtml = "";
		}
		StringBuffer log = new StringBuffer();
		log.append("ID：").append(model.getId()).append(",标题:").append(model.getTitle());
		Long indexid = DBUtil.getLong("select max(indexid) as indexid from Sys_Operate_Log", "indexid");
		LogUtil.getInstance().addLog((indexid+1), "阅读了新闻", log.toString());
		//评论部分
		return "cmsopen";
		
	}

	/**
	 * 更改讨论状态
	 * 
	 * @return
	 */
	public String cmsChangeTalk() {
		cmsInfoService.changeTalk(contentid);
		this.setPortletid(portletid);
		this.setTitle(title);
		String infolist = cmsInfoService.getGridScript(portletid, releaseman,title,releasedate,enddate);
		this.setInfolist(infolist);
		return SUCCESS;
	}

	/**
	 * 更改状态
	 * 
	 * @return
	 */
	public String cmsChangeStatus() {
		cmsInfoService.changeStatus(contentid);
		this.setPortletid(portletid);
		this.setTitle(title);
		String infolist = cmsInfoService.getGridScript(portletid, releaseman,title,releasedate,enddate);
		this.setInfolist(infolist);
		return SUCCESS;
	}

	/**
	 * 获取所属栏目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String cmsChannel() throws Exception {
		String json = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		json = cmsInfoService.getTreeJson();
		request.setAttribute("Cms_Info_Grid", json);
		return SUCCESS;
	}

	/**
	 * 暂存操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public void cmsInfoSave() throws Exception {
		String pstrScript = "<script>$(function(){";
		boolean msg = cmsInfoService.cmsInfoAdd(model);
		if (infoid == null || infoid.equals("")) {
			infoid = cmsInfoService.getMaxId();
		}
		pstrScript += "window.parent.document.forms[0].infoid.value='" + infoid
				+ "';\n";
		if (msg) {
			pstrScript += "window.parent.$.messager.alert('系统提示', '暂存成功！', 'info');";// 弹出提示
		} else {
			pstrScript += "window.parent.$.messager.alert('系统提示', '暂存失败！', 'error');";// 弹出提示
		}
		pstrScript += "});</script>";
		ResponseUtil.write(pstrScript);
	}

	/**
	 * 导读图片上传页面
	 * 
	 * @return
	 */
	public String prepictureindex() {
		return SUCCESS;
	}

	/**
	 * 添加一条新评论
	 * 
	 * @return
	 */

	public String addNewCmsComment() {

		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel();

		IworkCmsComment comment = new IworkCmsComment();
		comment.setContentid(contentId);
		comment.setTalkcontent(talkContent);
		comment.setTalkname(talkName);
		comment.setReaduser(user.getUsername());
		comment.setTalktime(new Date());
		CmsCommentUtil.getInstance().addNewComment(comment);

		ResponseUtil.writeTextUTF8("评论成功");

		return null;
	}

	/**
	 * 删除一条评论 ADMIN使用
	 * 
	 * @return
	 */

	public String delCmsComment() {

		boolean flag = CmsCommentUtil.getInstance().delComment(commentId);
		if (flag) {
			ResponseUtil.writeTextUTF8("删除成功");
		} else {
			ResponseUtil.writeTextUTF8("删除的对象不存在");
		}

		return null;
	}
	
	/**
	 * 查看已阅用户
	 * @yanglianfeng
	 * @return
	 */

	public String openSeenPage() {
		//List<HashMap> list = null;
		HashMap conditionMap = new HashMap();
		conditionMap.put("INFOID", infoid);
		list=DemAPI.getInstance().getAllList(SEENUUID, conditionMap, null);
		total=list.size();
		return SUCCESS;
	}
	
	
	

	public CmsInfoService getCmsInfoService() {
		return cmsInfoService;
	}

	public void setCmsInfoService(CmsInfoService cmsInfoService) {
		this.cmsInfoService = cmsInfoService;
	}

	public String getPortletid() {
		return portletid;
	}

	public void setPortletid(String portletid) {
		this.portletid = portletid;
	}

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getInfotitle() {
		return infotitle;
	}

	public void setInfotitle(String infotitle) {
		this.infotitle = infotitle;
	}

	public String getBrieftitle() {
		return brieftitle;
	}

	public void setBrieftitle(String brieftitle) {
		this.brieftitle = brieftitle;
	}

	public String getBrowse() {
		return browse;
	}

	public void setBrowse(String browse) {
		this.browse = browse;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPrepicture() {
		return prepicture;
	}

	public void setPrepicture(String prepicture) {
		this.prepicture = prepicture;
	}

	public String getPrecontent() {
		return precontent;
	}

	public void setPrecontent(String precontent) {
		this.precontent = precontent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getIstalk() {
		return istalk;
	}

	public void setIstalk(long istalk) {
		this.istalk = istalk;
	}

	public long getIscopy() {
		return iscopy;
	}

	public void setIscopy(long iscopy) {
		this.iscopy = iscopy;
	}

	public long getMarkred() {
		return markred;
	}

	public void setMarkred(long markred) {
		this.markred = markred;
	}

	public long getMarkbold() {
		return markbold;
	}

	public void setMarkbold(long markbold) {
		this.markbold = markbold;
	}

	public long getMarktop() {
		return marktop;
	}

	public void setMarktop(long marktop) {
		this.marktop = marktop;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getArchives() {
		return archives;
	}

	public void setArchives(String archives) {
		this.archives = archives;
	}

	public String getPstrScript() {
		return pstrScript;
	}

	public void setPstrScript(String pstrScript) {
		this.pstrScript = pstrScript;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public IworkCmsContent getModel() {
		return model;
	}

	public void setModel(IworkCmsContent model) {
		this.model = model;
	}

	public String getInfolist() {
		return infolist;
	}

	public void setInfolist(String infolist) {
		this.infolist = infolist;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<Map<String, Object>> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<Map<String, Object>> dataRows) {
		this.dataRows = dataRows;
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getCopy() {
		return copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}

	public String getCmsdate() {
		return cmsdate;
	}

	public void setCmsdate(String cmsdate) {
		this.cmsdate = cmsdate;
	}

	public List<IworkCmsPortlet> getPortletList() {
		return portletList;
	}

	public void setPortletList(List<IworkCmsPortlet> portletList) {
		this.portletList = portletList;
	}

	public CmsChannelService getCmsChannelService() {
		return cmsChannelService;
	}

	public void setCmsChannelService(CmsChannelService cmsChannelService) {
		this.cmsChannelService = cmsChannelService;
	}

	public CmsPortletService getCmsPortletService() {
		return cmsPortletService;
	}

	public void setCmsPortletService(CmsPortletService cmsPortletService) {
		this.cmsPortletService = cmsPortletService;
	}

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileFileName() {
		return imgFileFileName;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public String getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}

	public String getImgTitle() {
		return imgTitle;
	}

	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public String getTalkName() {
		return talkName;
	}

	public void setTalkName(String talkName) {
		this.talkName = talkName;
	}

	public String getTalkContent() {
		return talkContent;
	}

	public void setTalkContent(String talkContent) {
		this.talkContent = talkContent;
	}

	public String getCommentHtml() {
		return commentHtml;
	}

	public void setCommentHtml(String commentHtml) {
		this.commentHtml = commentHtml;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPagePagingHtml() {
		return pagePagingHtml;
	}

	public void setPagePagingHtml(String pagePagingHtml) {
		this.pagePagingHtml = pagePagingHtml;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getReleaseman() {
		return releaseman;
	}

	public void setReleaseman(String releaseman) {
		this.releaseman = releaseman;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public List<HashMap> getList() {
		return list;
	}

	public void setList(List<HashMap> list) {
		this.list = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public List<IworkCmsContent> getSorts() {
		return sorts;
	}

	public void setSorts(List<IworkCmsContent> sorts) {
		this.sorts = sorts;
	}


	
	
}
