package com.iwork.plugs.email.model;

import java.util.Date;

/**
 * IworkMailGroupItem entity. @author MyEclipse Persistence Tools
 */

public class SearchModel implements java.io.Serializable {
		private String sender;
	    private String recever;
	    private String position;
	    private String folderid;
	    private Date begindate;
	    private Date enddate;
	    private String keyword;
	    
	    
		public String getSender() {
			return sender;
		}
		public void setSender(String sender) {
			this.sender = sender;
		}
		public String getRecever() {
			return recever;
		}
		public void setRecever(String recever) {
			this.recever = recever;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getFolderid() {
			return folderid;
		}
		public void setFolderid(String folderid) {
			this.folderid = folderid;
		}
		public Date getBegindate() {
			return begindate;
		}
		public void setBegindate(Date begindate) {
			this.begindate = begindate;
		}
		public Date getEnddate() {
			return enddate;
		}
		public void setEnddate(Date enddate) {
			this.enddate = enddate;
		}
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
	    
	    

}