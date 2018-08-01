package com.ibpmsoft.project.zqb.action;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;





public class ZqbChangeTEXT {
	private HttpServletRequest request;   
    private HttpServletResponse response;   
	public String zqbtest() throws Exception {
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		String cmd=request.getParameter("cmd");
		String uid=request.getParameter("uid");
		String msgid=request.getParameter("msgid");
		String psw=request.getParameter("psw");
		String mobiles=request.getParameter("mobiles");
		String msg=request.getParameter("msg");	
		
		request.setAttribute("cmd", cmd);
		request.setAttribute("uid", uid);
		request.setAttribute("msgid", msgid);
		request.setAttribute("psw", psw);
		request.setAttribute("mobiles", mobiles);
		request.setAttribute("msg", msg);
		return "success";
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public static final byte[] readBytes(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen- readLen);
					if (readLengthThisTime == -1) {// Should not happen.
						break;
						}
					readLen += readLengthThisTime;
					}
				return message;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return new byte[] {};
	}
}
