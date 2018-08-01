package com.iwork.plugs.cms.action;

import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.cms.model.IworkCmsVideo;
import com.iwork.plugs.cms.service.CmsVideoService;
import com.opensymphony.xwork2.ActionSupport;

public class CmsVideoAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private CmsVideoService cmsVideoService;
	private IworkCmsVideo iworkCmsVideo;
	private String list;// 视频列表
	private String name;// 视频名称
	private String pageNoList;// 分页列表
	private String pageNum;// 当前页数

	private String id;
	private String title;// 视频名称
	private String videofile;// 播放视频文件名
	private String picfile;// 预览图片文件名
	private String uploader;// 上传人
	private String uploadtime;// 上传时间
	private String description;// 视频描述
	private String type;

	/**
	 * 视频播放
	 * 
	 * @return
	 */
	public String videoPlay() {
		String value = cmsVideoService.getVideoPlay(iworkCmsVideo);
		int x = value.indexOf("||");
		list = value.substring(0, x);
		name = value.substring(x + 2, value.length());
		ResponseUtil.writeTextUTF8(list);
		ResponseUtil.writeTextUTF8(name);
		return SUCCESS;
	}

	/**
	 * 视频列表--More页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String videoList() throws Exception {
		if (pageNum == null || "".equals(pageNum)) {
			pageNum = "1";
		}
		list = cmsVideoService.getVideoList(Integer.parseInt(pageNum));
		pageNoList = cmsVideoService.getPagination(Integer.parseInt(pageNum));
		ResponseUtil.writeTextUTF8(list);
		ResponseUtil.writeTextUTF8(pageNoList);
		return SUCCESS;
	}

	/**
	 * 视频列表--管理页面
	 * 
	 * @return
	 */
	public String videoEditList() {
		return SUCCESS;
	}

	public void videoEdit() {
		String json = cmsVideoService.getList();
		ResponseUtil.write(json); 
	}

	/**
	 * 视频列表编辑
	 * 
	 * @return
	 */
	public String videoEditAction() throws Exception {
		cmsVideoService.videoEdit(id, title, videofile, picfile, uploader, uploadtime, description, type);
		return SUCCESS;
	}

	/*------------------GET/SET--------------------*/

	public CmsVideoService getCmsVideoService() {
		return cmsVideoService;
	}

	public void setCmsVideoService(CmsVideoService cmsVideoService) {
		this.cmsVideoService = cmsVideoService;
	}

	public IworkCmsVideo getIworkCmsVideo() {
		return iworkCmsVideo;
	}

	public void setIworkCmsVideo(IworkCmsVideo iworkCmsVideo) {
		this.iworkCmsVideo = iworkCmsVideo;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getPageNoList() {
		return pageNoList;
	}

	public void setPageNoList(String pageNoList) {
		this.pageNoList = pageNoList;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideofile() {
		return videofile;
	}

	public void setVideofile(String videofile) {
		this.videofile = videofile;
	}

	public String getPicfile() {
		return picfile;
	}

	public void setPicfile(String picfile) {
		this.picfile = picfile;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
