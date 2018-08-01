package com.iwork.plugs.cms.model;

import java.util.Date;

/**
 * IworkCmsVideo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class IworkCmsVideo implements java.io.Serializable {

	// Fields

	private Long id;
	private String videofile;
	private String picfile;
	private String title;
	private String uploader;
	private Date uploadtime;
	private String description;
	private String download;
	private String videoload;

	// Constructors

	/** default constructor */
	public IworkCmsVideo() {
	}

	/** minimal constructor */
	public IworkCmsVideo(Long id) {
		this.id = id;
	}

	/** full constructor */
	public IworkCmsVideo(Long id, String videofile, String picfile, String title, String uploader, Date uploadtime, String description, String download, String videoload) {
		this.id = id;
		this.videofile = videofile;
		this.picfile = picfile;
		this.title = title;
		this.uploader = uploader;
		this.uploadtime = uploadtime;
		this.description = description;
		this.download = download;
		this.videoload = videoload;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVideofile() {
		return this.videofile;
	}

	public void setVideofile(String videofile) {
		this.videofile = videofile;
	}

	public String getPicfile() {
		return this.picfile;
	}

	public void setPicfile(String picfile) {
		this.picfile = picfile;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUploader() {
		return this.uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Date getUploadtime() {
		return this.uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDownload() {
		return this.download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getVideoload() {
		return this.videoload;
	}

	public void setVideoload(String videoload) {
		this.videoload = videoload;
	}

}