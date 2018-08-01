package com.ibpmsoft.project.zqb.action;

import com.ibpmsoft.project.zqb.service.ZqbShenPiService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbShenPiAction extends ActionSupport {
	private ZqbShenPiService zqbShenPiService;
	
	public ZqbShenPiService getZqbShenPiService() {
		return zqbShenPiService;
	}

	public void setZqbShenPiService(ZqbShenPiService zqbShenPiService) {
		this.zqbShenPiService = zqbShenPiService;
	}

	public void index(){
		String json= zqbShenPiService.index();
		ResponseUtil.write(json);
	}
	
}
