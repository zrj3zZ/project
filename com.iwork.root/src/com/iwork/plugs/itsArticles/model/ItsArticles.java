package com.iwork.plugs.itsArticles.model;

public class ItsArticles {
	
	public static String DATABASE_ENTITY = "ItsArticles";
	
	private Long id;
	private String fileSrcName;
	private String fileUUID;
	private String content;
	private Long zdqdid;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileSrcName() {
		return fileSrcName;
	}
	public void setFileSrcName(String fileSrcName) {
		this.fileSrcName = fileSrcName;
	}
	public String getFileUUID() {
		return fileUUID;
	}
	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}
	public Long getZdqdid() {
		return zdqdid;
	}
	public void setZdqdid(Long zdqdid) {
		this.zdqdid = zdqdid;
	}
}
