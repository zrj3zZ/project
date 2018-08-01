package com.iwork.app.conf;

public class FileServerConfig extends Config {
	private String path;
	private String bigTxtFilePath;
	private String formFilePath;
	private String talkfilePath;
	private String userPhotoPath;
	private String emailFilePath;
	private String kmFilePath;
	private String cmsFilePath;
	private String size;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getFormFilePath() {
		return formFilePath;
	}
	public void setFormFilePath(String formFilePath) {
		this.formFilePath = formFilePath;
	}
	public String getUserPhotoPath() {
		return userPhotoPath;
	}
	public void setUserPhotoPath(String userPhotoPath) {
		this.userPhotoPath = userPhotoPath;
	}
	public String getKmFilePath() {
		return kmFilePath;
	}
	public void setKmFilePath(String kmFilePath) {
		this.kmFilePath = kmFilePath;
	}
	public String getTalkfilePath() {
		return talkfilePath;
	}
	public void setTalkfilePath(String talkfilePath) {
		this.talkfilePath = talkfilePath;
	}
	public String getCmsFilePath() {
		return cmsFilePath;
	}
	public void setCmsFilePath(String cmsFilePath) {
		this.cmsFilePath = cmsFilePath;
	}
	public String getBigTxtFilePath() {
		return bigTxtFilePath;
	}
	public void setBigTxtFilePath(String bigTxtFilePath) {
		this.bigTxtFilePath = bigTxtFilePath;
	}
	public String getEmailFilePath() {
		return emailFilePath;
	}
	public void setEmailFilePath(String emailFilePath) {
		this.emailFilePath = emailFilePath;
	}
	
}
