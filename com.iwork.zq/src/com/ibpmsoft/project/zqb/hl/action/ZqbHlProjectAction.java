package com.ibpmsoft.project.zqb.hl.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.runtime.directive.Foreach;

import com.ibpmsoft.project.zqb.action.ZqbCheckAction;
import com.ibpmsoft.project.zqb.hl.service.ZqbHlProjectService;
import com.ibpmsoft.project.zqb.service.ZqbProjectManageService;
import com.iwork.app.weixin.process.action.qy.util.ListPageUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.ProcessAPI;
import com.iwork.wechat.pojo.AccessToken;
import com.iwork.wechat.util.WeixinUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ZqbHlProjectAction extends ActionSupport {
	/**
	 * version 1.0
	 */
	private static Logger logger = Logger.getLogger(ZqbHlProjectAction.class);
	private static final long serialVersionUID = 1L;
	private String projectno;
	private String owner;
	/*
	 * 华龙 需求项目负责人、部门负责人、项目承揽人 允许手工录入 进行判断的参数
	 * 20180627
	 */
	private String manager;
	private ZqbCheckAction zqbCheckAction;
	
	
	
	

	public ZqbCheckAction getZqbCheckAction() {
		return zqbCheckAction;
	}

	public void setZqbCheckAction(ZqbCheckAction zqbCheckAction) {
		this.zqbCheckAction = zqbCheckAction;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProjectno() {
		return projectno;
	}

	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}

	public String getYbcwJdContent() {
		hlybcwxmlxlcServer = ProcessAPI.getInstance().getProcessActDefId("YBCWXMLX");
		hlybcwgzjzlcServer = ProcessAPI.getInstance().getProcessActDefId("YBCWGZJDHB");
		String dfid = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='一般财务顾问项目'",
				"DFID");
		cxdddemid = dfid.split("#")[0];
		cxddformid = dfid.split("#")[1];
		String dfidgd = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='一般财务资料归档'",
				"DFID");
		zlgddemid = dfidgd.split("#")[0];
		zlgdformid = dfidgd.split("#")[1];
		String dfid_ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='一般财务初步尽调'",
				"DFID");
		if (dfid_.contains("#")) {
			cbjddemid = dfid_.split("#")[0];
			cbjdformid = dfid_.split("#")[1];
		}
		taskList = zqbHlProjectService.getYbcwTaskList(xmbh);
		username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		orgroleid=(long) 0;
		if(username.equals(owner)||username.equals("杨光")||username.equals("吕红贞")||username.equals("刘慧珍")){
			orgroleid=(long) 5;	
			
		}
		
		
		projectno = xmbh;
		return SUCCESS;
	}

	public void getYbcwMainContent() {
		String jsonString = zqbHlProjectService.getYbcwMainContent(xmbh);
		ResponseUtil.write(jsonString);
	}

	public void ybcwClosePro() {
		String info = zqbHlProjectService.ybcwClosePro(instanceIdStr);
		if (info.equals("")) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write(info);
		}
	}

	public String ybcwIndex() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		formid = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '一般财务顾问项目'", "FORMID");
		demId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '一般财务顾问项目'", "ID");
		runList = zqbHlProjectService.getYbcwRunlist(customername, clbm, czbm, cyrxm, runPageNumber, runPageSize);
		runTotalNum = zqbHlProjectService.getYbcwRunlistSize(customername, clbm, czbm, cyrxm);
		closeList = zqbHlProjectService.getYbcwCloselist(customername, clbm, czbm, cyrxm, closePageNumber,
				closePageSize);
		closeTotalNum = zqbHlProjectService.getYbcwCloselistSize(customername, clbm, czbm, cyrxm);
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		cuid = user.getUserid();
		return SUCCESS;
	}

	public String withoutTwoHundredIndex() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		formid = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目信息表单'", "FORMID");
		demId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目信息表单'", "ID");
		runList = zqbHlProjectService.getDzwothRunlist(customername,fxzt, clbm, czbm, cyrxm, runPageNumber, runPageSize);
		runTotalNum = zqbHlProjectService.getDzwothRunlistSize(customername,fxzt, clbm, czbm, cyrxm);
		closeList = zqbHlProjectService.getDzwothCloselist(customername, clbm, czbm, cyrxm, closePageNumber,
				closePageSize);
		closeTotalNum = zqbHlProjectService.getDzwothCloselistSize(customername, clbm, czbm, cyrxm);
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		cuid = user.getUserid();
		return SUCCESS;
	}

	public String withinTwoHundredIndex() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		formid = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目信息'", "FORMID");
		demId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目信息'", "ID");
		runList = zqbHlProjectService.getDzwthRunlist(customername,fxzt, clbm, czbm, cyrxm, runPageNumber, runPageSize);
		runTotalNum = zqbHlProjectService.getDzwthRunlistSize(customername,fxzt, clbm, czbm, cyrxm);
		closeList = zqbHlProjectService.getDzwthCloselist(customername, clbm, czbm, cyrxm, closePageNumber,
				closePageSize);
		closeTotalNum = zqbHlProjectService.getDzwthCloselistSize(customername, clbm, czbm, cyrxm);
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		cuid = user.getUserid();
		return SUCCESS;
	}

	public void dzwothClosePro() {
		String info = zqbHlProjectService.closeDzwothProject(instanceIdStr);
		if (info.equals("")) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write(info);
		}
	}

	public void dzwthClosePro() {
		String info = zqbHlProjectService.closeDzwthProject(instanceIdStr);
		if (info.equals("")) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write(info);
		}
	}

	public void getMainMoreContent() {
		String jsonString = zqbHlProjectService.getMainMoreContent(xmbh);
		ResponseUtil.write(jsonString);
	}

	public void getMainContent() {
		String jsonString = zqbHlProjectService.getMainContent(xmbh);
		ResponseUtil.write(jsonString);
	}

	public String getDzWothJdContent() {
		hldzxmlxlcServer = ProcessAPI.getInstance().getProcessActDefId("DZXMLXEBYS");
		hldzgpfxlcServer = ProcessAPI.getInstance().getProcessActDefId("GPFXEBYS");
		hldzyjgwlcServer = ProcessAPI.getInstance().getProcessActDefId("SBNHEBYS");
		hldzyjghlcServer = ProcessAPI.getInstance().getProcessActDefId("NHFKEBYS");
		hldzejgwlcServer = ProcessAPI.getInstance().getProcessActDefId("FKHFEBYS");
		hldzejghlcServer = ProcessAPI.getInstance().getProcessActDefId("GZBAEBYS");
		// hldzejghlcServer=ProcessAPI.getInstance().getProcessActDefId("DZEJDGZFKHF");
		String dfid = DBUtil
				.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='定增(200人以上)资料归档'", "DFID");
		zlgddemid = dfid.split("#")[0];
		zlgdformid = dfid.split("#")[1];
		String dfida = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='申报资料二百以上'",
				"DFID");
		cxdddemid = dfida.split("#")[0];
		cxddformid = dfida.split("#")[1];
		String dfid_ = DBUtil
				.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='定增(200人以上)初步尽调'", "DFID");
		if (dfid_.contains("#")) {
			cbjddemid = dfid_.split("#")[0];
			cbjdformid = dfid_.split("#")[1];
		}
		taskList = zqbHlProjectService.getDzWothTaskList(xmbh);
		username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		orgroleid=(long) 0;
		if(username.equals(owner)||username.equals("杨光")||username.equals("吕红贞")||username.equals("刘慧珍")){
			orgroleid=(long) 5;	
			
		}
		
		return SUCCESS;
	}

	public String getDzWthJdContent() {
		hldzxmlxlcServer = ProcessAPI.getInstance().getProcessActDefId("XMLXNFX");
		hldzgpfxlcServer = ProcessAPI.getInstance().getProcessActDefId("FAZLBS");
		hldzyjgwlcServer = ProcessAPI.getInstance().getProcessActDefId("DZSBNH");
		hldzyjghlcServer = ProcessAPI.getInstance().getProcessActDefId("DZNHFK");
		hldzsbzllcServer = ProcessAPI.getInstance().getProcessActDefId("SBZL");
		hldzejgwlcServer = ProcessAPI.getInstance().getProcessActDefId("DZEJDGZFKWT");
		hldzejghlcServer = ProcessAPI.getInstance().getProcessActDefId("DZEJDGZFKHF");
		String dfid = DBUtil
				.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='定增(200人以内)资料归档'", "DFID");
		zlgddemid = dfid.split("#")[0];
		zlgdformid = dfid.split("#")[1];
		String dfid_ = DBUtil
				.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='定增(200人以内)初步尽调'", "DFID");
		if (dfid_.contains("#")) {
			cbjddemid = dfid_.split("#")[0];
			cbjdformid = dfid_.split("#")[1];
		}
		taskList = zqbHlProjectService.getDzWthTaskList(xmbh);
		username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		orgroleid=(long) 0;
		if(username.equals(owner)||username.equals("杨光")||username.equals("吕红贞")||username.equals("刘慧珍")){
			orgroleid=(long) 5;	
			
		}
		return SUCCESS;
	}

	public String bgIndex() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		cuid = user.getUserid();
		formid = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '重组项目管理'", "FORMID");
		demId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '重组项目管理'", "ID");
		runList = zqbHlProjectService.getBgRunlist(customername,jydsf, clbm, czbm, cyrxm, runPageNumber, runPageSize);
		runTotalNum = zqbHlProjectService.getBgRunlistSize(customername,jydsf, clbm, czbm, cyrxm);
		closeList = zqbHlProjectService.getBgCloselist(customername, clbm, czbm, cyrxm, closePageNumber, closePageSize);
		closeTotalNum = zqbHlProjectService.getBgCloselistSize(customername, clbm, czbm, cyrxm);
		return SUCCESS;
	}

	public String sgIndex() {
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		formid = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '收购项目管理'", "FORMID");
		demId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '收购项目管理'", "ID");
		runList = zqbHlProjectService.getSgRunlist(customername,sgfs, clbm, czbm, cyrxm, runPageNumber, runPageSize);
		runTotalNum = zqbHlProjectService.getSgRunlistSize(customername,sgfs, clbm, czbm, cyrxm);
		closeList = zqbHlProjectService.getSgCloselist(customername, clbm, czbm, cyrxm, closePageNumber, closePageSize);
		closeTotalNum = zqbHlProjectService.getSgCloselistSize(customername, clbm, czbm, cyrxm);
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		cuid = user.getUserid();
		return SUCCESS;
	}

	public void bgClosePro() {
		String info = zqbHlProjectService.bgClosePro(instanceIdStr);
		if (info.equals("")) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write(info);
		}
	}

	public void sgClosePro() {
		String info = zqbHlProjectService.sgClosePro(instanceIdStr);
		if (info.equals("")) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write(info);
		}
	}

	public void getBgMainContent() {
		String jsonString = zqbHlProjectService.getBgMainContent(xmbh);
		ResponseUtil.write(jsonString);
	}

	public void getSgMainContent() {
		String jsonString = zqbHlProjectService.getSgMainContent(xmbh);
		ResponseUtil.write(jsonString);
	}

	public String JyOut() {

		return null;
	}

	public String getSgJdContent() {
		hlsgxmlxlcServer = ProcessAPI.getInstance().getProcessActDefId("SGLXXX");
		hlsgzlbslcServer = ProcessAPI.getInstance().getProcessActDefId("SGFAZLBS");
		hlsgfkwtlcServer = ProcessAPI.getInstance().getProcessActDefId("SGGZFKWTTZ");
		hlsgfkhflcServer = ProcessAPI.getInstance().getProcessActDefId("SGGZFKHFSH");
		String dfid = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='收购持续督导'",
				"DFID");
		cxdddemid = dfid.split("#")[0];
		cxddformid = dfid.split("#")[1];
		String dfidgd = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='收购资料归档'",
				"DFID");
		zlgddemid = dfidgd.split("#")[0];
		zlgdformid = dfidgd.split("#")[1];
		String dfid_ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='收购初步尽调'",
				"DFID");
		if (dfid_.contains("#")) {
			cbjddemid = dfid_.split("#")[0];
			cbjdformid = dfid_.split("#")[1];
		}
		taskList = zqbHlProjectService.getSgTaskList(xmbh);
		username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		orgroleid=(long) 0;
		if(username.equals(owner)||username.equals("杨光")||username.equals("吕红贞")||username.equals("刘慧珍")){
			orgroleid=(long) 5;	
			
		}
		return SUCCESS;
	}

	public String getBgJdContent() {
		hlbgxmlxlcServer = ProcessAPI.getInstance().getProcessActDefId("ZZXMLX");
		hlbgczyjlcServer = ProcessAPI.getInstance().getProcessActDefId("ZZYJ");
		hlbgyjwtlcServer = ProcessAPI.getInstance().getProcessActDefId("ZZYJDGZFKWT");
		hlbgyjhflcServer = ProcessAPI.getInstance().getProcessActDefId("ZZYJDGZFKHF");
		hlbgsbnhlcServer = ProcessAPI.getInstance().getProcessActDefId("ZZSBNH");
		hlbgnhhflcServer = ProcessAPI.getInstance().getProcessActDefId("ZZNHHF");
		hlbgejwtlcServer = ProcessAPI.getInstance().getProcessActDefId("ZZEJDGZFKWT");
		hlbgejhflcServer = ProcessAPI.getInstance().getProcessActDefId("ZZEJDGZFKHF");
		hlbgsbzllcServer = ProcessAPI.getInstance().getProcessActDefId("ZZSBBA");
		String dfid = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='重组后续持续督导年报披露'",
				"DFID");
		cxdddemid = dfid.split("#")[0];
		cxddformid = dfid.split("#")[1];
		String dfida = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='重组报告及财务顾问意见'",
				"DFID");
		czxdddemid = dfida.split("#")[0];
		czxddformid = dfida.split("#")[1];
		String dfidc = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='重组预案披露'",
				"DFID");
		czzxdddemid = dfidc.split("#")[0];
		czzxddformid = dfidc.split("#")[1];
		String dfidgd = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='重组资料归档'",
				"DFID");
		zlgddemid = dfidgd.split("#")[0];
		zlgdformid = dfidgd.split("#")[1];
		String dfid_ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='重组初步尽调'",
				"DFID");
		if (dfid_.contains("#")) {
			cbjddemid = dfid_.split("#")[0];
			cbjdformid = dfid_.split("#")[1];
		}
		taskList = zqbHlProjectService.getBgTaskList(xmbh);
		username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		orgroleid=(long) 0;
		if(username.equals(owner)||username.equals("杨光")||username.equals("吕红贞")||username.equals("刘慧珍")){
			orgroleid=(long) 5;	
			
		}
		return SUCCESS;
	}
	
	public String gpIndex() {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		cuid = user.getUserid();
		if (runPageNumber == 0)
			runPageNumber = 1;
		if (closePageNumber == 0)
			closePageNumber = 1;
		formid = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '项目立项表单'", "FORMID");
		demId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE = '项目立项表单'", "ID");
		runList = zqbHlProjectService.getRunList(customername,xmjd, cyrxm, runPageNumber, runPageSize);
		runTotalNum = zqbHlProjectService.getRunlistSize(customername,xmjd, cyrxm);
		closeList = zqbHlProjectService.getCloselist(customername, cyrxm, closePageNumber, closePageSize);
		closeTotalNum = zqbHlProjectService.getCloselistSize(customername, cyrxm);
		return SUCCESS;
	}

	public void closePro() {
		String info = zqbHlProjectService.closePro(instanceIdStr);
		if (info.equals("")) {
			ResponseUtil.write("success");
		} else {
			ResponseUtil.write(info);
		}
	}

	public void getGpMainContent() {
		String jsonString = zqbHlProjectService.getGpMainContent(xmbh);
		ResponseUtil.write(jsonString);
	}

	public String getGpJdContent() {
		hlgpxmlxlcServer = ProcessAPI.getInstance().getProcessActDefId("XMLXSPLC");
		hlgpsbzklcServer = ProcessAPI.getInstance().getProcessActDefId("XMGGSH");
		hlgpnhfklcServer = ProcessAPI.getInstance().getProcessActDefId("NHFKSH");
		hlgpsbgzlcServer = ProcessAPI.getInstance().getProcessActDefId("GPDJJGD");
		hlgpgzfklcServer = ProcessAPI.getInstance().getProcessActDefId("XMNHSH");
		hlgpfkhflcServer = ProcessAPI.getInstance().getProcessActDefId("GZFKJHF");
		hlgpscpllcServer = ProcessAPI.getInstance().getProcessActDefId("GPSCXXPL");
		hlgplspllcServer = ProcessAPI.getInstance().getProcessActDefId("GPLSXXPL");
		hlgpecpllcServer = ProcessAPI.getInstance().getProcessActDefId("GPECXXPL");
		String dfid = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌归档'", "DFID");
		zlgddemid = dfid.split("#")[0];
		zlgdformid = dfid.split("#")[1];
		String dfid_ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌股转反馈通知(物理)'",
				"DFID");
		if (dfid_.contains("#")) {
			gzfktzdemid = dfid_.split("#")[0];
			gzfktzformid = dfid_.split("#")[1];
		}
		String dfid__ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌提交初始登记'",
				"DFID");
		if (dfid__.contains("#")) {
			tjcsdemid = dfid__.split("#")[0];
			tjcsformid = dfid__.split("#")[1];
		}
		String dfid___ = DBUtil.getString("SELECT ID||'#'||FORMID AS DFID FROM SYS_DEM_ENGINE WHERE TITLE='挂牌初步尽调'",
				"DFID");
		if (dfid___.contains("#")) {
			cbjddemid = dfid___.split("#")[0];
			cbjdformid = dfid___.split("#")[1];
		}
		taskList = zqbHlProjectService.getGpTaskList(xmbh);
		username = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		orgroleid=(long) 0;
		
		if(owner.contains(username)||username.equals("杨光")||username.equals("吕红贞")||username.equals("刘慧珍")){
			orgroleid=(long) 5;	
				
		}
		return SUCCESS;
	}

	public String omIndex() {
		hlqtlcsplcServer = ProcessAPI.getInstance().getProcessActDefId("QTLCSP");
		if (pageNumber == 0)
			pageNumber = 1;
		list = zqbHlProjectService.getQtxsList(pageSize, pageNumber);
		totalNum = zqbHlProjectService.getQtxsListSize();
		return SUCCESS;
	}

	private ZqbHlProjectService zqbHlProjectService;
	

	public void setZqbHlProjectService(ZqbHlProjectService zqbHlProjectService) {
		this.zqbHlProjectService = zqbHlProjectService;
	}

	private String customername;
	private String clbm;
	private String jydsf;
	private String sgfs;
	private String fxzt;
	private String czbm;
	private String cyrxm;
	private String xmjd;
	private int runPageNumber;
	private int runPageSize = 10;
	private Long formid;
	private Long demId;
	private int closePageNumber;
	private int closePageSize = 10;
	private int runTotalNum;
	private int closeTotalNum;
	private int ymid;
	private List<HashMap> runList;
	private List<HashMap> closeList;
	private List<HashMap<String, Object>> taskList;
	private String xmbh;
	private String projectname;
	private String instanceIdStr;
	private String hlgpxmlxlcServer;
	private String hlgpsbzklcServer;
	private String hlgpnhfklcServer;
	private String hlgpsbgzlcServer;
	private String hlgpgzfklcServer;
	private String hlgpfkhflcServer;
	private String hlgpscpllcServer;
	private String hlgplspllcServer;
	private String hlgpecpllcServer;
	private String cuid;

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	private String hlbgxmlxlcServer;
	private String hlbgczyjlcServer;
	private String hlbgyjwtlcServer;
	private String hlbgyjhflcServer;
	private String hlbgsbnhlcServer;
	private String hlbgnhhflcServer;
	private String hlbgejwtlcServer;
	private String hlbgejhflcServer;
	private String hlbgsbzllcServer;

	private String hlybcwxmlxlcServer;
	private String hlybcwgzjzlcServer;

	public String getHlybcwxmlxlcServer() {
		return hlybcwxmlxlcServer;
	}

	public void setHlybcwxmlxlcServer(String hlybcwxmlxlcServer) {
		this.hlybcwxmlxlcServer = hlybcwxmlxlcServer;
	}

	public String getHlybcwgzjzlcServer() {
		return hlybcwgzjzlcServer;
	}

	public void setHlybcwgzjzlcServer(String hlybcwgzjzlcServer) {
		this.hlybcwgzjzlcServer = hlybcwgzjzlcServer;
	}

	private String hldzxmlxlcServer;
	private String hldzgpfxlcServer;
	private String hldzyjgwlcServer;
	private String hldzyjghlcServer;
	private String hldzsbzllcServer;
	private String hldzejgwlcServer;
	private String hldzejghlcServer;
	private String zlgddemid;
	private String zlgdformid;
	private String gzfktzdemid;
	private String gzfktzformid;
	private String tjcsdemid;
	private String tjcsformid;

	private String hlsgxmlxlcServer;
	private String hlsgzlbslcServer;
	private String hlsgfkwtlcServer;
	private String hlsgfkhflcServer;
	private String cxdddemid;
	private String cxddformid;
	private String czxdddemid;
	private String czzxdddemid;
	private String czxddformid;
	private String czzxddformid;
	private Long orgroleid;
	private String cbjddemid;
	private String cbjdformid;

	private String hlqtlcsplcServer;
	private int pageNumber;
	private int pageSize = 10;
	private int totalNum;
	private List<HashMap> list;

	public List<HashMap> getList() {
		return list;
	}

	public void setList(List<HashMap> list) {
		this.list = list;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getHlqtlcsplcServer() {
		return hlqtlcsplcServer;
	}

	public void setHlqtlcsplcServer(String hlqtlcsplcServer) {
		this.hlqtlcsplcServer = hlqtlcsplcServer;
	}

	public Long getOrgroleid() {
		return orgroleid;
	}

	public void setOrgroleid(Long orgroleid) {
		this.orgroleid = orgroleid;
	}

	public String getCzxddformid() {
		return czxddformid;
	}

	public void setCzxddformid(String czxddformid) {
		this.czxddformid = czxddformid;
	}

	public String getCzzxddformid() {
		return czzxddformid;
	}

	public void setCzzxddformid(String czzxddformid) {
		this.czzxddformid = czzxddformid;
	}

	public String getCbjddemid() {
		return cbjddemid;
	}

	public void setCbjddemid(String cbjddemid) {
		this.cbjddemid = cbjddemid;
	}

	public String getCbjdformid() {
		return cbjdformid;
	}

	public void setCbjdformid(String cbjdformid) {
		this.cbjdformid = cbjdformid;
	}

	public String getCzxdddemid() {
		return czxdddemid;
	}

	public void setCzxdddemid(String czxdddemid) {
		this.czxdddemid = czxdddemid;
	}

	public String getCzzxdddemid() {
		return czzxdddemid;
	}

	public void setCzzxdddemid(String czzxdddemid) {
		this.czzxdddemid = czzxdddemid;
	}

	public String getHlbgxmlxlcServer() {
		return hlbgxmlxlcServer;
	}

	public void setHlbgxmlxlcServer(String hlbgxmlxlcServer) {
		this.hlbgxmlxlcServer = hlbgxmlxlcServer;
	}

	public String getHlbgczyjlcServer() {
		return hlbgczyjlcServer;
	}

	public void setHlbgczyjlcServer(String hlbgczyjlcServer) {
		this.hlbgczyjlcServer = hlbgczyjlcServer;
	}

	public String getHlbgyjwtlcServer() {
		return hlbgyjwtlcServer;
	}

	public void setHlbgyjwtlcServer(String hlbgyjwtlcServer) {
		this.hlbgyjwtlcServer = hlbgyjwtlcServer;
	}

	public String getHlbgyjhflcServer() {
		return hlbgyjhflcServer;
	}

	public void setHlbgyjhflcServer(String hlbgyjhflcServer) {
		this.hlbgyjhflcServer = hlbgyjhflcServer;
	}

	public String getHlbgsbnhlcServer() {
		return hlbgsbnhlcServer;
	}

	public void setHlbgsbnhlcServer(String hlbgsbnhlcServer) {
		this.hlbgsbnhlcServer = hlbgsbnhlcServer;
	}

	public String getHlbgnhhflcServer() {
		return hlbgnhhflcServer;
	}

	public void setHlbgnhhflcServer(String hlbgnhhflcServer) {
		this.hlbgnhhflcServer = hlbgnhhflcServer;
	}

	public String getHlbgejwtlcServer() {
		return hlbgejwtlcServer;
	}

	public void setHlbgejwtlcServer(String hlbgejwtlcServer) {
		this.hlbgejwtlcServer = hlbgejwtlcServer;
	}

	public String getHlbgejhflcServer() {
		return hlbgejhflcServer;
	}

	public void setHlbgejhflcServer(String hlbgejhflcServer) {
		this.hlbgejhflcServer = hlbgejhflcServer;
	}

	public String getHlbgsbzllcServer() {
		return hlbgsbzllcServer;
	}

	public void setHlbgsbzllcServer(String hlbgsbzllcServer) {
		this.hlbgsbzllcServer = hlbgsbzllcServer;
	}

	public String getHldzxmlxlcServer() {
		return hldzxmlxlcServer;
	}

	public void setHldzxmlxlcServer(String hldzxmlxlcServer) {
		this.hldzxmlxlcServer = hldzxmlxlcServer;
	}

	public String getHldzgpfxlcServer() {
		return hldzgpfxlcServer;
	}

	public void setHldzgpfxlcServer(String hldzgpfxlcServer) {
		this.hldzgpfxlcServer = hldzgpfxlcServer;
	}

	public String getHldzyjgwlcServer() {
		return hldzyjgwlcServer;
	}

	public void setHldzyjgwlcServer(String hldzyjgwlcServer) {
		this.hldzyjgwlcServer = hldzyjgwlcServer;
	}

	public String getHldzyjghlcServer() {
		return hldzyjghlcServer;
	}

	public void setHldzyjghlcServer(String hldzyjghlcServer) {
		this.hldzyjghlcServer = hldzyjghlcServer;
	}

	public String getHldzsbzllcServer() {
		return hldzsbzllcServer;
	}

	public void setHldzsbzllcServer(String hldzsbzllcServer) {
		this.hldzsbzllcServer = hldzsbzllcServer;
	}

	public String getHldzejgwlcServer() {
		return hldzejgwlcServer;
	}

	public void setHldzejgwlcServer(String hldzejgwlcServer) {
		this.hldzejgwlcServer = hldzejgwlcServer;
	}

	public String getHldzejghlcServer() {
		return hldzejghlcServer;
	}

	public void setHldzejghlcServer(String hldzejghlcServer) {
		this.hldzejghlcServer = hldzejghlcServer;
	}

	public String getZlgddemid() {
		return zlgddemid;
	}

	public void setZlgddemid(String zlgddemid) {
		this.zlgddemid = zlgddemid;
	}

	public String getZlgdformid() {
		return zlgdformid;
	}

	public void setZlgdformid(String zlgdformid) {
		this.zlgdformid = zlgdformid;
	}

	public String getGzfktzdemid() {
		return gzfktzdemid;
	}

	public void setGzfktzdemid(String gzfktzdemid) {
		this.gzfktzdemid = gzfktzdemid;
	}

	public String getGzfktzformid() {
		return gzfktzformid;
	}

	public void setGzfktzformid(String gzfktzformid) {
		this.gzfktzformid = gzfktzformid;
	}

	public String getTjcsdemid() {
		return tjcsdemid;
	}

	public void setTjcsdemid(String tjcsdemid) {
		this.tjcsdemid = tjcsdemid;
	}

	public String getTjcsformid() {
		return tjcsformid;
	}

	public void setTjcsformid(String tjcsformid) {
		this.tjcsformid = tjcsformid;
	}

	public String getHlsgxmlxlcServer() {
		return hlsgxmlxlcServer;
	}

	public void setHlsgxmlxlcServer(String hlsgxmlxlcServer) {
		this.hlsgxmlxlcServer = hlsgxmlxlcServer;
	}

	public String getHlsgzlbslcServer() {
		return hlsgzlbslcServer;
	}

	public void setHlsgzlbslcServer(String hlsgzlbslcServer) {
		this.hlsgzlbslcServer = hlsgzlbslcServer;
	}

	public String getHlsgfkwtlcServer() {
		return hlsgfkwtlcServer;
	}

	public void setHlsgfkwtlcServer(String hlsgfkwtlcServer) {
		this.hlsgfkwtlcServer = hlsgfkwtlcServer;
	}

	public String getHlsgfkhflcServer() {
		return hlsgfkhflcServer;
	}

	public String getCxdddemid() {
		return cxdddemid;
	}

	public void setCxdddemid(String cxdddemid) {
		this.cxdddemid = cxdddemid;
	}

	public String getCxddformid() {
		return cxddformid;
	}

	public void setCxddformid(String cxddformid) {
		this.cxddformid = cxddformid;
	}

	public void setHlsgfkhflcServer(String hlsgfkhflcServer) {
		this.hlsgfkhflcServer = hlsgfkhflcServer;
	}

	public String getHlgpxmlxlcServer() {
		return hlgpxmlxlcServer;
	}

	public void setHlgpxmlxlcServer(String hlgpxmlxlcServer) {
		this.hlgpxmlxlcServer = hlgpxmlxlcServer;
	}

	public String getHlgpsbzklcServer() {
		return hlgpsbzklcServer;
	}

	public void setHlgpsbzklcServer(String hlgpsbzklcServer) {
		this.hlgpsbzklcServer = hlgpsbzklcServer;
	}

	public String getHlgpnhfklcServer() {
		return hlgpnhfklcServer;
	}

	public void setHlgpnhfklcServer(String hlgpnhfklcServer) {
		this.hlgpnhfklcServer = hlgpnhfklcServer;
	}

	public String getHlgpsbgzlcServer() {
		return hlgpsbgzlcServer;
	}

	public void setHlgpsbgzlcServer(String hlgpsbgzlcServer) {
		this.hlgpsbgzlcServer = hlgpsbgzlcServer;
	}

	public String getHlgpgzfklcServer() {
		return hlgpgzfklcServer;
	}

	public void setHlgpgzfklcServer(String hlgpgzfklcServer) {
		this.hlgpgzfklcServer = hlgpgzfklcServer;
	}

	public String getHlgpfkhflcServer() {
		return hlgpfkhflcServer;
	}

	public void setHlgpfkhflcServer(String hlgpfkhflcServer) {
		this.hlgpfkhflcServer = hlgpfkhflcServer;
	}

	public String getHlgpscpllcServer() {
		return hlgpscpllcServer;
	}

	public void setHlgpscpllcServer(String hlgpscpllcServer) {
		this.hlgpscpllcServer = hlgpscpllcServer;
	}

	public String getHlgplspllcServer() {
		return hlgplspllcServer;
	}

	public void setHlgplspllcServer(String hlgplspllcServer) {
		this.hlgplspllcServer = hlgplspllcServer;
	}

	public String getHlgpecpllcServer() {
		return hlgpecpllcServer;
	}

	public void setHlgpecpllcServer(String hlgpecpllcServer) {
		this.hlgpecpllcServer = hlgpecpllcServer;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getInstanceIdStr() {
		return instanceIdStr;
	}

	public void setInstanceIdStr(String instanceIdStr) {
		this.instanceIdStr = instanceIdStr;
	}

	public List<HashMap<String, Object>> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<HashMap<String, Object>> taskList) {
		this.taskList = taskList;
	}

	public List<HashMap> getRunList() {
		return runList;
	}

	public void setRunList(List<HashMap> runList) {
		this.runList = runList;
	}

	public List<HashMap> getCloseList() {
		return closeList;
	}

	public void setCloseList(List<HashMap> closeList) {
		this.closeList = closeList;
	}

	public String getActStepDefId() {
		return actStepDefId;
	}

	public void setActStepDefId(String actStepDefId) {
		this.actStepDefId = actStepDefId;
	}

	public ZqbHlProjectService getZqbHlProjectService() {
		return zqbHlProjectService;
	}

	public int getYmid() {
		return ymid;
	}

	public void setYmid(int ymid) {
		this.ymid = ymid;
	}

	public int getRunTotalNum() {
		return runTotalNum;
	}

	public void setRunTotalNum(int runTotalNum) {
		this.runTotalNum = runTotalNum;
	}

	public int getCloseTotalNum() {
		return closeTotalNum;
	}

	public void setCloseTotalNum(int closeTotalNum) {
		this.closeTotalNum = closeTotalNum;
	}

	public int getClosePageNumber() {
		return closePageNumber;
	}

	public void setClosePageNumber(int closePageNumber) {
		this.closePageNumber = closePageNumber;
	}

	public int getClosePageSize() {
		return closePageSize;
	}

	public void setClosePageSize(int closePageSize) {
		this.closePageSize = closePageSize;
	}

	public Long getFormid() {
		return formid;
	}

	public void setFormid(Long formid) {
		this.formid = formid;
	}

	public Long getDemId() {
		return demId;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getClbm() {
		return clbm;
	}

	public void setClbm(String clbm) {
		this.clbm = clbm;
	}
	
	
	public String getJydsf() {
		return jydsf;
	}

	public void setJydsf(String jydsf) {
		this.jydsf = jydsf;
	}

	public String getSgfs() {
		return sgfs;
	}

	public void setSgfs(String sgfs) {
		this.sgfs = sgfs;
	}

	public String getFxzt() {
		return fxzt;
	}

	public void setFxzt(String fxzt) {
		this.fxzt = fxzt;
	}

	public String getCzbm() {
		return czbm;
	}

	public void setCzbm(String czbm) {
		this.czbm = czbm;
	}

	public String getCyrxm() {
		return cyrxm;
	}

	public void setCyrxm(String cyrxm) {
		this.cyrxm = cyrxm;
	}
	
	
	public String getXmjd() {
		return xmjd;
	}

	public void setXmjd(String xmjd) {
		this.xmjd = xmjd;
	}

	public int getRunPageNumber() {
		return runPageNumber;
	}

	public void setRunPageNumber(int runPageNumber) {
		this.runPageNumber = runPageNumber;
	}

	public int getRunPageSize() {
		return runPageSize;
	}

	public void setRunPageSize(int runPageSize) {
		this.runPageSize = runPageSize;
	}

	private String actStepDefId;

	public void isGpLastnode() {
		String isShowKF = "";
		String gpxmlxsdid = zqbHlProjectService.getConfigUUID("gpxmlxsdid");
		String gpsbzksdid = zqbHlProjectService.getConfigUUID("gpsbzksdid");
		String gpscplsdid = zqbHlProjectService.getConfigUUID("gpscplsdid");
		String gpecplsdid = zqbHlProjectService.getConfigUUID("gpecplsdid");

		String user = null;
		if (actStepDefId.equals(gpxmlxsdid) || actStepDefId.equals(gpsbzksdid)) {
			user = "hlzhbid";

		} else if (actStepDefId.equals(gpscplsdid) || actStepDefId.equals(gpecplsdid)) {
			user = "hlddfkbmid";
		}
		isShowKF = zqbHlProjectService.getConfigUUID(user);
		if (isShowKF != null && "".equals(isShowKF)) {
			if (isShowKF.contains(",")) {
				String[] users = isShowKF.split(",");
				isShowKF = null;
				for (int i = 0; i < users.length; i++) {
					if (isShowKF == null) {
						isShowKF = users[0];
					} else {

						isShowKF = isShowKF + "," + users[i];
					}
				}
			}
			ResponseUtil.write(isShowKF);
		}
	}

	public void isDzLastnode() {
		String isShowKF = "";
		String dzxmlxsdid = zqbHlProjectService.getConfigUUID("dzxmlxsdid");
		String dzgpfxsdid = zqbHlProjectService.getConfigUUID("dzgpfxsdid");
		String dzyjhfsdid = zqbHlProjectService.getConfigUUID("dzyjhfsdid");
		String dzsbzksdid = zqbHlProjectService.getConfigUUID("dzsbzksdid");

		String user = null;
		if (actStepDefId.equals(dzxmlxsdid)) {
			user = "hlzhbid";
		} else if (actStepDefId.equals(dzgpfxsdid) || actStepDefId.equals(dzyjhfsdid)
				|| actStepDefId.equals(dzsbzksdid)) {
			user = "hlbmid";
		}
		isShowKF = zqbHlProjectService.getConfigUUID(user);

		ResponseUtil.write(isShowKF);
	}

	public void isDzwLastnode() {
		String isShowKF = "";
		String dzwxmlxsdid = zqbHlProjectService.getConfigUUID("dzwxmlxsdid");
		String dzwgpfxsdid = zqbHlProjectService.getConfigUUID("dzwgpfxsdid");
		String dzwsbnhsdid = zqbHlProjectService.getConfigUUID("dzwsbnhsdid");

		String user = null;
		if (actStepDefId.equals(dzwxmlxsdid) || actStepDefId.equals(dzwsbnhsdid)) {
			user = "hlzhbid";
		} else if (actStepDefId.equals(dzwgpfxsdid)) {
			user = "hlbmid";
		}
		isShowKF = zqbHlProjectService.getConfigUUID(user);

		ResponseUtil.write(isShowKF);
	}

	public void isCcLastnode() {
		String isShowKF = "";
		String ccyayjsdid = zqbHlProjectService.getConfigUUID("ccyayjsdid");
		;
		String ccyjhfsdid = zqbHlProjectService.getConfigUUID("ccyjhfsdid");
		;
		String ccejhfsdid = zqbHlProjectService.getConfigUUID("ccejhfsdid");
		;

		String user = null;
		if (actStepDefId.equals(ccyayjsdid) || actStepDefId.equals(ccyjhfsdid) || actStepDefId.equals(ccejhfsdid)) {
			user = "hlbmid";
		}
		isShowKF = zqbHlProjectService.getConfigUUID(user);

		ResponseUtil.write(isShowKF);
	}

	public void isSgLastnode() {
		String isShowKF = "";
		String sgfabssdid = zqbHlProjectService.getConfigUUID("sgfabssdid");
		String user = null;
		if (actStepDefId.equals(sgfabssdid)) {
			user = "hlbmid";
		}
		isShowKF = zqbHlProjectService.getConfigUUID(user);

		ResponseUtil.write(isShowKF);
	}

	public void isYbcwLastnode() {
		String isShowKF = "";
		String ybcwxmlxsdid = zqbHlProjectService.getConfigUUID("ybcwxmlxsdid");
		String ybcwjdhbsdid = zqbHlProjectService.getConfigUUID("ybcwjdhbsdid");
		String user = null;
		if (actStepDefId.equals(ybcwxmlxsdid)) {
			user = "hlzhbid";
		} else if (actStepDefId.equals(ybcwjdhbsdid)) {
			user = "hlbmid";
		}
		isShowKF = zqbHlProjectService.getConfigUUID(user);

		ResponseUtil.write(isShowKF);
	}

	private String actDefId;

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public void isXmcsYwbm() {
		String isShowKF = "";

		String user = null;
		if (actDefId.indexOf("XMGGSH") == 0 || actDefId.indexOf("GPDJJGD") == 0 || actDefId.indexOf("XMNHSH") == 0
				|| actDefId.indexOf("GZFKJHF") == 0 || actDefId.indexOf("DZEJDGZFKHF") == 0
				|| actDefId.indexOf("SBNHEBYS") == 0 || actDefId.indexOf("ZZSBNH") == 0) {
			user = "hlywbm";

		}
		isShowKF = zqbHlProjectService.getConfigUUID(user);

		ResponseUtil.write(isShowKF);

	}

	public void delHlDz() {
		String flag = zqbHlProjectService.delHlDz();
		ResponseUtil.write(flag);

	}

	public void delHlDzw() {
		String flag = zqbHlProjectService.delHlDzw();
		ResponseUtil.write(flag);

	}

	public void delHlBg() {
		String flag = zqbHlProjectService.delHlBg();
		ResponseUtil.write(flag);

	}

	public void delHlSg() {
		String flag = zqbHlProjectService.delHlSg();
		ResponseUtil.write(flag);

	}

	public void delHlGp() {
		String flag = zqbHlProjectService.delHlGp();
		ResponseUtil.write(flag);

	}

	public void delHlYbcw() {
		String flag = zqbHlProjectService.delHlYbcw();
		ResponseUtil.write(flag);

	}

	public void delHlQt() {
		String flag = zqbHlProjectService.delHlQt();
		ResponseUtil.write(flag);

	}

	public void checkdouble() {
		String sql = "SELECT COUNT FROM BD_ZQB_PJ_BASE WHERE CUSTOMERNAME=?";
		HashMap params = new HashMap();
		params.put(1, customername);
		int COUNT = DBUTilNew.getInt("COUNT", sql, params);
		if (COUNT > 0) {
			ResponseUtil.write("true");
		} else {
			ResponseUtil.write("false");
		}
	}

	private String jdmc;

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public void getTentUserDateForProTwo() {
		String jsonHtml = zqbHlProjectService.getTentUserDateForProTwo(jdmc);
		ResponseUtil.write(jsonHtml.toString());
	}

	public void getTentUserDateForRcTwo() {
		String jsonHtml = zqbHlProjectService.getTentUserDateForRcTwo(xmbh, jdmc);
		ResponseUtil.write(jsonHtml.toString());
	}

	Long bwlformid;
	Long bwlid;

	public String showBwl() {

		if (pageNumber == 0)
			pageNumber = 1;
		// bwlformid=zqbHlProjectService.getConfig("xmrbformid");
		// bwlid=zqbHlProjectService.getConfig("xmrbid");
		bwlformid = zqbHlProjectService.getConfig("bwlformid");
		bwlid = zqbHlProjectService.getConfig("bwlid");
		list = zqbHlProjectService.showBwlList(xmbh, pageNumber, pageSize, bwlformid, projectname);
		totalNum = zqbHlProjectService.showBwlListSize(xmbh, projectname);
		return SUCCESS;
	}

	public Long getBwlformid() {
		return bwlformid;
	}

	public void setBwlformid(Long bwlformid) {
		this.bwlformid = bwlformid;
	}

	public Long getBwlid() {
		return bwlid;
	}

	public void setBwlid(Long bwlid) {
		this.bwlid = bwlid;
	}

	Long bgformid;
	Long bgid;

	public String showBg() {

		if (pageNumber == 0)
			pageNumber = 1;
		// bwlformid=zqbHlProjectService.getConfig("formid");
		// bwlid=zqbHlProjectService.getConfig("xmrbid");
		bgformid = zqbHlProjectService.getConfig("bgformid");
		bgid = zqbHlProjectService.getConfig("bgid");
		list = zqbHlProjectService.showBwlList(xmbh, pageNumber, pageSize, bgformid, projectname);
		totalNum = zqbHlProjectService.showBwlListSize(xmbh, projectname);
		return SUCCESS;
	}

	private final String appid = "wx14d879cecf31a790";
	private final String appsecret = "eg7rPU72wza1oNGHuducq28g2Hc6PX0Oarjv8p5OJcI";
	private String token;
	private String endtime;
	private String starttime;
	private List useridlist;
	private int currpage;
	private List pagedList;
	private String departmentname;

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	/**
	 * 簽到首页
	 * 
	 * @return
	 */
	public String dkindex() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String enddate = df.format(new Date());
		String starttime = zqbHlProjectService.Date2TimeStamp(enddate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String endtime = zqbHlProjectService.Date2TimeStamp(enddate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		

		
		AccessToken accessToken = WeixinUtil.getInstance().getAccessToken(appid, appsecret);

		useridlist = zqbHlProjectService.getUseridlist(departmentname, username);
		
		if (token == null || "".equals(token)) {
			token = accessToken.getToken();
		}
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=ACCESS_TOKEN"
				.replace("ACCESS_TOKEN", token);
		HashMap hash = new HashMap();
		hash.put("opencheckindatatype", 3);
		hash.put("starttime", starttime);
		hash.put("endtime", endtime);

		hash.put("useridlist", useridlist);

		String postData = JSONArray.fromObject(hash).toString();
		// 数据转换json数据时自动加上了[],应除去，不然会报错
		int indexOf = postData.indexOf("[");
		int indexof = postData.lastIndexOf("]");
		postData = postData.substring(indexOf + 1, indexof);
		JSONObject jsonObject = WeixinUtil.getInstance().httpRequest(requestUrl, "POST", postData);

		List<Map<String, Object>> list = (List) jsonObject.get("checkindata");
		String errmsg = (String) jsonObject.get("errmsg");
		if(!"ok".equals(errmsg)){			
			logger.error("调取微信端签到接口获取相关信息时报出的异常:"+errmsg);
		}
		if (list != null && !list.isEmpty()) {//将微信端的数据和数据库中的数据关联
			list = getListName(list);
		}
		List newsuerList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {

			newsuerList.add(list.get(i).get("userid"));
		}
		useridlist.removeAll(newsuerList);
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		if (useridlist != null && useridlist.size() > 0) {// 当天没签到的情况

			for (int i = 0; i < useridlist.size(); i++) {
				Map<String, Object> hashMap = new HashMap<String, Object>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				String timeDate = sdf.format(new Date());
				HashMap map = zqbHlProjectService.getQjlcListByUserid(useridlist.get(i).toString(), timeDate);
				
				if (map != null && !map.isEmpty()) {
					String userid = useridlist.get(i).toString();
					
					String sql = "select USERNAME from orguser where weixin_code =?";
					Map params = new HashMap();
					params.put(1, userid);
					String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);

					hashMap.put("USERNAME", dataStr);
					hashMap.put("userid", userid);
					String LX = map.get("LX").toString();
					hashMap.put("checkin_timei", LX);
					hashMap.put("checkin_timej", LX);
					hashMap.put("notes", map.get("BZSM").toString());
					newList.add(hashMap);
				} else {
					String userid = useridlist.get(i).toString();
					
					String sql = "select USERNAME from orguser where weixin_code =?";
					Map params = new HashMap();
					params.put(1, userid);

					String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);

					hashMap.put("USERNAME", dataStr);
					hashMap.put("userid", userid);

					hashMap.put("checkin_timei", "未签到");
					hashMap.put("checkin_timej", "未签到");

					newList.add(hashMap);

				}

			}

		}
		list.addAll(newList);
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		ListPageUtil page=new ListPageUtil();
		pagedList = page.getPagedList(list, pageNumber, pageSize);

		totalNum = list.size();
		
		return SUCCESS;

	}

	private List<Map<String, Object>> getListName(List<Map<String, Object>> pagedList) {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();

		if (pagedList != null && !pagedList.isEmpty()) {// 将除当天外的时间合并上下午签到
			for (int i =0; i <pagedList.size(); i++) {

				for (int j = i +1; j <pagedList.size(); j++) {
					Map<String, Object> hashMap = new HashMap<String, Object>();
					Object userid = pagedList.get(i).get("userid");
					Object userid1 = pagedList.get(j).get("userid");
					String checkintime = pagedList.get(i).get("checkin_time") == null ? ""
							: pagedList.get(i).get("checkin_time").toString();
					String checkintime1 = pagedList.get(j).get("checkin_time") == null ? ""
							: pagedList.get(j).get("checkin_time").toString();

					if (!"".equals(checkintime) && !"".equals(checkintime1)) {

						String timeDate = zqbHlProjectService.TimeStamp2Date(checkintime, "yyyy-MM-dd");
						String timeDate1 = zqbHlProjectService.TimeStamp2Date(checkintime1, "yyyy-MM-dd");

						if (userid.equals(userid1) && timeDate.equals(timeDate1)) {
							hashMap.put("userid", pagedList.get(i).get("userid").toString());
							hashMap.put("checkintimei", pagedList.get(i).get("checkin_time").toString());
							hashMap.put("checkintimej", pagedList.get(j).get("checkin_time").toString());
							hashMap.put("checkin_typei", pagedList.get(i).get("checkin_type").toString());
							hashMap.put("checkin_typej", pagedList.get(j).get("checkin_type").toString());
							hashMap.put("exception_typei", pagedList.get(i).get("exception_type").toString());
							hashMap.put("exception_typej", pagedList.get(j).get("exception_type").toString());
							hashMap.put("checkintime", timeDate);		
							hashMap.put("notes", pagedList.get(j).get("notes") == null ? ""
									: pagedList.get(j).get("notes").toString() + pagedList.get(i).get("notes") == null
											? "" : pagedList.get(i).get("notes").toString());
							newList.add(hashMap);
							break;
						}

					}
				}

			}
			
			if (newList != null && !newList.isEmpty()) {//生成页面信息
				List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
				for (int j = 0; j < newList.size(); j++) {
					Object object = newList.get(j).get("userid");
					String sql = "select username from orguser where weixin_code =?";
					Map params = new HashMap();
					params.put(1, object);
					String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);

					newList.get(j).put("USERNAME", dataStr);
					Object checkintimei = newList.get(j).get("checkintimei");
					Object checkintimej = newList.get(j).get("checkintimej");
					Object checkintypei = newList.get(j).get("exception_typei");
					Object checkintypej = newList.get(j).get("exception_typej");
					if ("未打卡".equals(checkintypei.toString()) && "未打卡".equals(checkintypej.toString())) {
						String timeDate = zqbHlProjectService.TimeStamp2Date(checkintimei.toString(), "yyyy-MM-dd");
						HashMap map = zqbHlProjectService.getQjlcListByUserid(object.toString(), timeDate);

						if (map == null || map.isEmpty()) {
							newList.get(j).put("checkin_timei", "未签到");
							newList.get(j).put("checkin_timej", "未签到");
							
						} else {
							String LX = map.get("LX").toString();
							newList.get(j).put("checkin_timei", LX);
							newList.get(j).put("checkin_timej", LX);
							newList.get(j).put("notes", map.get("BZSM").toString());
						}
					} else if ("未打卡".equals(checkintypei.toString())) {
						newList.get(j).put("checkin_timei", "未签到");
						String timeDate = zqbHlProjectService.TimeStamp2Date(checkintimej.toString(),
								"yyyy-MM-dd HH:mm:ss");
						newList.get(j).put("checkin_timej", timeDate);
						
					} else if ("未打卡".equals(checkintypej.toString())) {
						newList.get(j).put("checkin_timej", "未签到");
						String timeDate = zqbHlProjectService.TimeStamp2Date(checkintimei.toString(),
								"yyyy-MM-dd HH:mm:ss");
						newList.get(j).put("checkin_timei", timeDate);
					} else {
						String timeDate = zqbHlProjectService.TimeStamp2Date(checkintimei.toString(),
								"yyyy-MM-dd HH:mm:ss");
						newList.get(j).put("checkin_timei", timeDate);
						String timeDate1 = zqbHlProjectService.TimeStamp2Date(checkintimej.toString(),
								"yyyy-MM-dd HH:mm:ss");
						newList.get(j).put("checkin_timej", timeDate1);
					}					
				}
				
			} else {// 当天上午下午签到情况
				Map<String, Object> hashMap = new HashMap<String, Object>();
				String sql = "select username from orguser where weixin_code =?";
				Map params = new HashMap();
				params.put(1, pagedList.get(0).get("userid").toString());
				String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);
				String checkintime=pagedList.get(0).get("checkin_time").toString();
				
				String timeDate = zqbHlProjectService.TimeStamp2Date(checkintime, "yyyy-MM-dd");
				hashMap.put("USERNAME", dataStr);
				hashMap.put("userid", pagedList.get(0).get("userid").toString());
				String timeDate1 = zqbHlProjectService.TimeStamp2Date(pagedList.get(0).get("checkin_time").toString(),
						"yyyy-MM-dd HH:mm:ss");
				String type = pagedList.get(0).get("checkin_type").toString();
				hashMap.put("checkin_timei",timeDate1);
				hashMap.put("checkin_timej","未签到");
				hashMap.put("checkin_typei", pagedList.get(0).get("checkin_type").toString());

				hashMap.put("exception_typei", pagedList.get(0).get("exception_type").toString());
				hashMap.put("checkintime",timeDate);
				hashMap.put("notes",
						pagedList.get(0).get("notes") == null ? "" : pagedList.get(0).get("notes").toString());
				newList.add(hashMap);				
			}

		}

		return newList;
	}

	public int getCurrpage() {
		return currpage;
	}

	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}

	public List getPagedList() {
		return pagedList;
	}

	public void setPagedList(List pagedList) {
		this.pagedList = pagedList;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public List getUseridlist() {
		return useridlist;
	}

	public void setUseridlist(List useridlist) {
		this.useridlist = useridlist;
	}

	/**
	 * 查看個人信息
	 */
	private String username;
	private String enddate;
	private String startdate;

	public String dkindexbyuserid() {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String dcsj = df1.format(new Date());
		
		if (token == null || "".equals(token)) {
			AccessToken accessToken = WeixinUtil.getInstance().getAccessToken(appid, appsecret);
			token = accessToken.getToken();
		}
		if (startdate == null) {
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			String month = String.valueOf(date.get(Calendar.MONTH));
			String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
			startdate = year + "-" + month + "-" + day;

		}
		if (enddate == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			enddate = df.format(new Date());
		}
		String st = zqbHlProjectService.Date2TimeStamp(startdate+ " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String et = zqbHlProjectService.Date2TimeStamp(enddate+ " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		String dc = zqbHlProjectService.Date2TimeStamp(dcsj, "yyyy-MM-dd");
		List newList = new ArrayList();
		if(Long.parseLong(st)<=Long.parseLong(dc)&&Long.parseLong(dc)<=Long.parseLong(et)){
			String sartt = zqbHlProjectService.Date2TimeStamp(dcsj + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			String entt = zqbHlProjectService.Date2TimeStamp(dcsj + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			
			
			AccessToken accessToken = WeixinUtil.getInstance().getAccessToken(appid, appsecret);

			
			
			if (token == null || "".equals(token)) {
				token = accessToken.getToken();
			}
			String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=ACCESS_TOKEN"
					.replace("ACCESS_TOKEN", token);
			HashMap hash = new HashMap();
			hash.put("opencheckindatatype", 3);
			hash.put("starttime", sartt);
			hash.put("endtime", entt);

			hash.put("useridlist", username);

			String postData = JSONArray.fromObject(hash).toString();
			// 数据转换json数据时自动加上了[],应除去，不然会报错
			int indexOf = postData.indexOf("[");
			int indexof = postData.lastIndexOf("]");
			postData = postData.substring(indexOf + 1, indexof);
			JSONObject jsonObject = WeixinUtil.getInstance().httpRequest(requestUrl, "POST", postData);

			List<Map<String, Object>> list = (List) jsonObject.get("checkindata");
			String errmsg = (String) jsonObject.get("errmsg");
			if(!"ok".equals(errmsg)){			
				logger.error("调取微信端签到接口获取相关信息时报出的异常:"+errmsg);
			}
			if (list != null && !list.isEmpty()) {
				list = getListName(list);
			}else{
				
				Map<String, Object> hashMap = new HashMap<String, Object>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				String timeDate = sdf.format(new Date());
				HashMap map = zqbHlProjectService.getQjlcListByUserid(username, timeDate);
				
				if (map != null && !map.isEmpty()) {
					
					
					String sql = "select USERNAME from orguser where weixin_code =?";
					Map params = new HashMap();
					params.put(1, username);
					String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);

					hashMap.put("USERNAME", dataStr);
					hashMap.put("userid", username);
					String LX = map.get("LX").toString();
					hashMap.put("checkin_timei", LX);
					hashMap.put("checkin_timej", LX);
					hashMap.put("checkintime", timeDate);
					hashMap.put("notes", map.get("BZSM").toString());
					newList.add(hashMap);
				} else {
					
					
					String sql = "select USERNAME from orguser where weixin_code =?";
					Map params = new HashMap();
					params.put(1, username);

					String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);

					hashMap.put("USERNAME", dataStr);
					hashMap.put("userid", username);
					hashMap.put("checkintime", timeDate);
					hashMap.put("checkin_timei", "未签到");
					hashMap.put("checkin_timej", "未签到");

					newList.add(hashMap);

				}
				
				
			}
			
			newList.addAll(list);
				

			}
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=ACCESS_TOKEN"
				.replace("ACCESS_TOKEN", token);
		HashMap hash = new HashMap();
		hash.put("opencheckindatatype", 3);
		hash.put("starttime", st);
		hash.put("endtime", et);

		hash.put("useridlist", username);

		String postData = JSONArray.fromObject(hash).toString();
		// 数据转换json数据时自动加上了[],应除去，不然会报错
		int indexOf = postData.indexOf("[");
		int indexof = postData.lastIndexOf("]");
		postData = postData.substring(indexOf + 1, indexof);
		JSONObject jsonObject = WeixinUtil.getInstance().httpRequest(requestUrl, "POST", postData);
		
		List<Map<String, Object>> list = (List) jsonObject.get("checkindata");
		String errmsg = (String) jsonObject.get("errmsg");
		if(!"ok".equals(errmsg)){			
			logger.error("调取微信端签到接口获取相关信息时报出的异常:"+errmsg);
		}
		list = getListName(list);
		
		if (pageNumber == 0) {
			pageNumber = 1;
		}
		if (pageSize == 0) {
			pageSize = 10;
		}
		if(newList!=null){
		list.addAll(newList);
		}
		ListPageUtil page=new ListPageUtil();
		pagedList = page.getPagedList(list, pageNumber, pageSize);
		totalNum = list.size();
		
		return SUCCESS;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String exproqdindex() {

		return SUCCESS;
	}

	public void exproqd() {
		
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String dcsj = df1.format(new Date());
		
		
		if (startdate == null) {
			Calendar date = Calendar.getInstance();
			String year = String.valueOf(date.get(Calendar.YEAR));
			String month = String.valueOf(date.get(Calendar.MONTH));
			String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
			startdate = year + "-" + month + "-" + day;

		}
		if (enddate == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			enddate = df.format(new Date());
		}
		
		String st = zqbHlProjectService.Date2TimeStamp(startdate+ " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String et = zqbHlProjectService.Date2TimeStamp(enddate+ " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		String dc = zqbHlProjectService.Date2TimeStamp(dcsj, "yyyy-MM-dd");
		List newsuerList = new ArrayList();
		
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		AccessToken accessToken = WeixinUtil.getInstance().getAccessToken(appid, appsecret);
		if (token == null || "".equals(token)) {
			token = accessToken.getToken();
		}
		String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=ACCESS_TOKEN"
				.replace("ACCESS_TOKEN", token);
		if(Long.parseLong(st)<=Long.parseLong(dc)&&Long.parseLong(dc)<=Long.parseLong(et)){//先将今天的签到情况查出来
			String sartt = zqbHlProjectService.Date2TimeStamp(dcsj + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			String entt = zqbHlProjectService.Date2TimeStamp(dcsj + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			useridlist = zqbHlProjectService.getUseridlist(departmentname, username);
			
			
			HashMap hash = new HashMap();
			hash.put("opencheckindatatype", 3);
			hash.put("starttime", sartt);
			hash.put("endtime", entt);
			hash.put("useridlist", useridlist);

			String postData = JSONArray.fromObject(hash).toString();
			// 数据转换json数据时自动加上了[],应除去，不然会报错
			int indexOf = postData.indexOf("[");
			int indexof = postData.lastIndexOf("]");
			postData = postData.substring(indexOf + 1, indexof);
			JSONObject jsonObject = WeixinUtil.getInstance().httpRequest(requestUrl, "POST", postData);

			List<Map<String, Object>> list = (List) jsonObject.get("checkindata");
			String errmsg = (String) jsonObject.get("errmsg");
			if(!"ok".equals(errmsg)){			
				logger.error("调取微信端签到接口获取相关信息时报出的异常:"+errmsg);
			}
			if (list != null && !list.isEmpty()) {
				list = getListName(list);//将请假和出差外出信息写入到嘻嘻当中
			}
			
			for (int i = 0; i < list.size(); i++) {

				newsuerList.add(list.get(i).get("userid"));
			}
			useridlist.removeAll(newsuerList);
			
			if (useridlist != null && useridlist.size() > 0) {// 当天没签到的情况

				for (int i = 0; i < useridlist.size(); i++) {
					Map<String, Object> hashMap = new HashMap<String, Object>();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

					String timeDate = sdf.format(new Date());
					HashMap map = zqbHlProjectService.getQjlcListByUserid(useridlist.get(i).toString(), timeDate);
					
					if (map != null && !map.isEmpty()) {
						String userid = useridlist.get(i).toString();
						
						String sql = "select USERNAME from orguser where weixin_code =?";
						Map params = new HashMap();
						params.put(1, userid);
						String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);

						hashMap.put("USERNAME", dataStr);
						hashMap.put("userid", userid);
						String LX = map.get("LX").toString();
						hashMap.put("checkin_timei", LX);
						hashMap.put("checkin_timej", LX);
						hashMap.put("checkintime", timeDate);
						hashMap.put("notes", map.get("BZSM").toString());
						newList.add(hashMap);
					} else {
						String userid = useridlist.get(i).toString();
						
						String sql = "select USERNAME from orguser where weixin_code =?";
						Map params = new HashMap();
						params.put(1, userid);

						String dataStr = DBUTilNew.getDataStr("USERNAME", sql, params);

						hashMap.put("USERNAME", dataStr);
						hashMap.put("userid", userid);
						hashMap.put("checkintime", timeDate);
						hashMap.put("checkin_timei", "未签到");
						hashMap.put("checkin_timej", "未签到");

						newList.add(hashMap);

					}

				}

			}
			newList.addAll(list);
			
		}
		
		useridlist = zqbHlProjectService.getUseridlist(departmentname, username);
	
		HashMap hash = new HashMap();
		hash.put("opencheckindatatype", 3);
		hash.put("starttime", st);
		hash.put("endtime", et);
		hash.put("useridlist", useridlist);

		String postData = JSONArray.fromObject(hash).toString();
		// 数据转换json数据时自动加上了[],应除去，不然会报错
		int indexOf = postData.indexOf("[");
		int indexof = postData.lastIndexOf("]");
		postData = postData.substring(indexOf + 1, indexof);
		JSONObject jsonObject = WeixinUtil.getInstance().httpRequest(requestUrl, "POST", postData);
		
		List<Map<String, Object>> list = (List) jsonObject.get("checkindata");
		String errmsg = (String) jsonObject.get("errmsg");
		if(!"ok".equals(errmsg)){			
			logger.error("调取微信端签到接口获取相关信息时报出的异常:"+errmsg);
		}
		
		list = getListName(list);
		if(newList!=null){		
			
			list.addAll(newList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		zqbHlProjectService.expDaily(response, list);

	}
	public void getqxdc(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username=uc.get_userModel().getUsername();
		String departmentname=uc.get_userModel().getDepartmentname();
		String  flag="不显示导出";
		if("吕红贞".equals(username)||"杨光".equals(username)||"综合管理部".equals(departmentname)){
			flag="显示导出";
		}
		ResponseUtil.writeTextUTF8(flag);
		
	}
	
	/*
	 * 主要是提取 中括号中的用户名
	 * 如果没有完整的中括号，则直接把传来的值当做名称
	 * 
	 */
	public String userFormat(String user){
		String username="";
		String regEx = "(\\[[^\\]]*\\])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(user);
		
		while (matcher.find()) {
			username = matcher.group().substring(1, matcher.group().length()-1);
			if(username.equals("")){
				username = user;
			}
		}
		return username;
	}
	
	/*
	 * 验证项目负责人、部门负责人是否存在使用
	 */
	public void validateUser(){
		JSONObject json = new JSONObject();
		String result ="";
		try {
			owner = URLDecoder.decode(owner, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			manager =URLDecoder.decode(manager, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(owner!=""&&owner!=null){
			owner = userFormat(owner);
			String id = zqbCheckAction.validateUser(owner);
			if(id.equals("")){
				result="业务部门负责人手动录入";
			}
		}
		if(manager!=""&&manager!=null){
			manager = userFormat(manager);
			String id = zqbCheckAction.validateUser(manager);
			if(id.equals("")){
				if(!result.equals("")){
					result+="，项目负责人手动录入";
				}else{
					result+="项目负责人手动录入";
				}
			}
		}
		if(!result.equals("")){
			result+= "无法进行流程审批，相关人员也无法查看项目信息";
		}
		json.put("result", result);
		ResponseUtil.write(json.toString());
	}
}
