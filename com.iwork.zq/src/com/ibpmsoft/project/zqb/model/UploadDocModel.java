package com.ibpmsoft.project.zqb.model;

import java.util.List;

import com.iwork.core.upload.model.FileUpload;

public class UploadDocModel {
	private String projectNo;
	private String projectName;
	private String taskName;
	private List<FileUpload> list; 
	
	
	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public List<FileUpload> getList() {
		return list;
	}
	public void setList(List<FileUpload> list) {
		this.list = list;
	}

}
