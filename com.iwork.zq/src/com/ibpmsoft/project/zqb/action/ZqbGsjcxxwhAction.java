package com.ibpmsoft.project.zqb.action;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import com.ibpmsoft.project.zqb.service.ZqbGsjcxxwhService;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.StringUtil;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ZqbGsjcxxwhAction extends ActionSupport {
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static final long serialVersionUID = 1L;
	private ZqbGsjcxxwhService zqbGsjcxxwhService;
	private String name;
	private String id;
	private String instanceId;
	private String filename;
	private String temp;
	private String formid;
	private String demId;
	private String foid;
	
	/**
	 * 财务报表 删除
	 */
	public void delCubb() {
		String khbhzqjc = zqbGsjcxxwhService.delCubb(name);
	}
	/**
	 * 财务报表 新增提醒
	 */
	public void cwbbTyff() {
		String khbhzqjc = zqbGsjcxxwhService.cwbbTyff(name );
	}
	/**
	 * 增加/修改  制度清单提醒
	 */
	public void addOrUpdGszc() {
		String khbhzqjc = zqbGsjcxxwhService.addOrUpdGszc(instanceId,name,filename);
	}
	/**
	 * 增加/修改  公司基础信息维护其他的提醒通用方法
	 */
	public void addOrUpdTyff() {
		String khbhzqjc = zqbGsjcxxwhService.addOrUpdTyff(instanceId,name,filename);
	}
	/**
	 * 删除通用
	 */
	public void delJcxxwh() {
		String khbhzqjc = zqbGsjcxxwhService.delJcxxwh(temp,formid,demId);
	}
	/**
	 * 子表保存系统提醒
	 */
	public void zbtyff(){
		String khbhzqjc = zqbGsjcxxwhService.zbtyff(instanceId,formid,foid);
	}
	/**
	 *文件上传验证
	 * @throws Exception 
	 */
	public void webUploadyzfs() throws Exception {
	//	response.setCharacterEncoding("UTF-8");
		//request.setCharacterEncoding("UTF-8");
	//	PrintWriter writer = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			filename= java.net.URLDecoder.decode(filename, "utf-8");
		} catch (Exception e1) {
			filename="";
		}
		String success = "success";
		String illegalChar = "";
		int countStr = StringUtil.countStr(filename,".");
		if(countStr>=2){
			illegalChar+=(filename+":包含多个.");
			success="false";
		}
		if(StringUtil.mathcer("[@$%\\/]",filename)){
			int lastPoint = filename.lastIndexOf(".");
			String nakedName = filename.substring(0, lastPoint);
			String[] nakedNameArr = nakedName.split("");
			for (int i = 0; i < nakedNameArr.length; i++) {
				if("[@$%\\/]".contains(nakedNameArr[i])){
					if(illegalChar.equals("")){
						illegalChar+=(filename+"包含非法字符:");
					}
					illegalChar+=nakedNameArr[i];
				}
			}
			success="false";
			//illegalChar+=fileName+":包含非法字符";
		}
		if(filename!=null){
			if(!StringUtil.validata(FileUtil.getFileExt(filename))){
				illegalChar+=(filename+":不支持的文件格式");
				success="false";
			}
		}else{
			success="false";
		}
		try {
			json.put("success", success);
			json.put("illegalChar", illegalChar);
		} catch (JSONException e) {
			
		}
		
		//writer.write(json.toString());
		ResponseUtil.write(json.toString());
	}
	public ZqbGsjcxxwhService getZqbGsjcxxwhService() {
		return zqbGsjcxxwhService;
	}
	public void setZqbGsjcxxwhService(ZqbGsjcxxwhService zqbGsjcxxwhService) {
		this.zqbGsjcxxwhService = zqbGsjcxxwhService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public String getDemId() {
		return demId;
	}
	public void setDemId(String demId) {
		this.demId = demId;
	}
	public String getFoid() {
		return foid;
	}
	public void setFoid(String foid) {
		this.foid = foid;
	}

	

}
