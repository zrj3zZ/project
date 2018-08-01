package com.iwork.plugs.cms.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.upload.FileUploadUtil;
import com.iwork.commons.util.FileUtil;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.model.IworkCmsComment;
import com.iwork.plugs.cms.model.IworkCmsContent;
import com.iwork.plugs.cms.model.IworkCmsPortlet;
import com.iwork.plugs.cms.service.CmsChannelService;
import com.iwork.plugs.cms.service.CmsInfoXtService;
import com.iwork.plugs.cms.service.CmsPortletXtService;
import com.iwork.plugs.cms.util.CmsCommentUtil;
import com.iwork.plugs.cms.util.CmsPageModelUtil;
import com.iwork.plugs.cms.util.CmsUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * CMS内容管理跳转类
 * 
 * @author WeiGuangjian
 * 
 */
public class CmsInfoXtAction extends ActionSupport {
	private static final Log log = LogFactory.getLog(CmsInfoXtAction.class);
	private CmsPortletXtService cmsPortletXtService;
	public CmsPortletXtService getCmsPortletXtService() {
		return cmsPortletXtService;
	}

	public void setCmsPortletXtService(CmsPortletXtService cmsPortletXtService) {
		this.cmsPortletXtService = cmsPortletXtService;
	}

	private CmsChannelService cmsChannelService;
	private CmsInfoXtService cmsInfoXtService;
	public CmsInfoXtService getCmsInfoXtService() {
		return cmsInfoXtService;
	}

	public void setCmsInfoXtService(CmsInfoXtService cmsInfoXtService) {
		this.cmsInfoXtService = cmsInfoXtService;
	}

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
		String infolist = cmsInfoXtService.getGridScript(portletid);
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
		json = cmsInfoXtService.getDataBoxJson(portletid, page);
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
			String json = cmsInfoXtService.getCmsInfoListJson(portletid, pageSize,pageNo);
			ResponseUtil.write(json);
		}
	}
	
	/**
	 * @return 字符串
	 * @throws Exception
	 */
	public String cmsInfoXtRss() throws Exception{
		ServletContext sc = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);      
        String path = sc.getRealPath("/"); 
		cmsInfoXtService.cmsInfoGenerate(path,portletid,infoid);
	     
		return "index";	
	}
	/**
	 * 正文图片上传
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String uploadImg() throws Exception {
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
		} catch (Exception e1) {
			log.error(e1);
		}

		if (imgFile == null) {
			out.println(getError("请选择文件。"));
			return null;
		}
		FileUploadUtil file = new FileUploadUtil();
		String flag = file.uploadname(imgFileFileName);
		if (!flag.equals("true")) {
			out.println(getError("非法文件。"));
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
		cmsInfoXtService.cmsInfoRemove(infoid);
	}

	/**
	 * 创建资讯发布页面
	 * 
	 * @return
	 */
	public String newCmsInfoPage() {
		if (portletid != null) {
			model = cmsInfoXtService.initContentModel(Long.parseLong(portletid));
			portletList = cmsPortletXtService.getCmsPortletDAO().getAllList();
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
			flag = cmsInfoXtService.cmsInfoAdd(model);
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
		model = cmsInfoXtService.getModel(infoid);
		portletList = cmsPortletXtService.getCmsPortletDAO().getAllList();
		return "update";
	}

	/**
	 * 打开内容显示页面
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String cmsOpen() throws ParseException {
		IworkCmsContent model = cmsInfoXtService.getModel(infoid);
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
			this.setPrepicture("<div class=\"subpic\">" + model.getPrepicture()
					+ "</div>");
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
			this.setArchives("<div class=archive>附件: " + model.getArchives()
					+ "</div>");
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
		//评论部分
		commentHtml = CmsCommentUtil.getInstance().getCommentHtmlStr(infoid,
				user.getUserid(), pageNo, pageSize);
		userName = user.getUsername();
		int dataCount = CmsCommentUtil.getInstance().getCommentCount(infoid);
		//评论分页
		if(dataCount > 0){
			String url = "cmsOpen.action?infoid=" + infoid + "&";
			pagePagingHtml = CmsPageModelUtil.getInstance().getPageModelHtmlStr(
					url, dataCount, pageSize, pageNo);
		}
		
		return "cmsopen";
	}

	/**
	 * 更改讨论状态
	 * 
	 * @return
	 */
	public String cmsChangeTalk() {
		cmsInfoXtService.changeTalk(contentid);
		this.setPortletid(portletid);
		String infolist = cmsInfoXtService.getGridScript(portletid);
		this.setInfolist(infolist);
		return SUCCESS;
	}

	/**
	 * 更改状态
	 * 
	 * @return
	 */
	public String cmsChangeStatus() {
		cmsInfoXtService.changeStatus(contentid);
		this.setPortletid(portletid);
		String infolist = cmsInfoXtService.getGridScript(portletid);
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
		json = cmsInfoXtService.getTreeJson();
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
		boolean msg = cmsInfoXtService.cmsInfoAdd(model);
		if (infoid == null || infoid.equals("")) {
			infoid = cmsInfoXtService.getMaxId();
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
	
}
