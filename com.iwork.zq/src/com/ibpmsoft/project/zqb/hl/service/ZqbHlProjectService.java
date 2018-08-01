package com.ibpmsoft.project.zqb.hl.service;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.Region;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.ibpmsoft.project.zqb.hl.dao.ZqbHlProjectDAO;
import com.ibpmsoft.project.zqb.service.ZqbProjectManageService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.wechat.pojo.AccessToken;
import com.iwork.wechat.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ZqbHlProjectService {
	private static Logger logger = Logger.getLogger(ZqbProjectManageService.class);
	private ZqbHlProjectDAO zqbHlProjectDAO;
	public ZqbHlProjectDAO getZqbHlProjectDAO() {
		return zqbHlProjectDAO;
	}
	public void setZqbHlProjectDAO(ZqbHlProjectDAO zqbHlProjectDAO) {
		this.zqbHlProjectDAO = zqbHlProjectDAO;
	}
	
	public List<HashMap> getDzwthRunlist(String customername,String fxzt, String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		return zqbHlProjectDAO.getDzwthRunlist(customername,fxzt,clbm,czbm,cyrxm,runPageNumber,runPageSize);
	}
	public int getDzwthRunlistSize(String customername,String fxzt,String clbm,String czbm,String cyrxm) {
		return zqbHlProjectDAO.getDzwthRunlistSize(customername,fxzt,clbm,czbm,cyrxm).size();
	}
	public List<HashMap> getDzwthCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		return zqbHlProjectDAO.getDzwthCloselist(customername,clbm,czbm,cyrxm,closePageNumber,closePageSize);
	}
	public int getDzwthCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		return zqbHlProjectDAO.getDzwthCloselistSize(customername,clbm,czbm,cyrxm).size();
	}
	public String closeDzwothProject(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String qtxmglUUID = com.iwork.core.db.DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目信息表单'", "UUID");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("STATUS", "已完成");
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(qtxmglUUID, instanceId,dataMap, dataid, false);
						String customername = dataMap.get("CUSTOMERNAME").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "定增(200人以上)项目维护", "关闭定增(200人以上)项目："+customername);
						}else{
							content.append(customername).append(",");
						}
					} catch (Exception e) {
						
					}
				}
			}
			if(content.length()>0){
				info = content.substring(0, content.length()-1);
			}
		}
		return info;
	}
	
	public String closeDzwthProject(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String qtxmglUUID = com.iwork.core.db.DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目信息'", "UUID");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("STATUS", "已完成");
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(qtxmglUUID, instanceId,dataMap, dataid, false);
						String customername = dataMap.get("CUSTOMERNAME").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "定增(200人以内)项目维护", "关闭定增(200人以内)项目："+customername);
						}else{
							content.append(customername).append(",");
						}
					} catch (Exception e) {
						
					}
				}
			}
			if(content.length()>0){
				info = content.substring(0, content.length()-1);
			}
		}
		return info;
	}
	public String getMainMoreContent(String xmbh){
		List<String> lables = new ArrayList<String>();
		lables.add("CUSTOMERNAME");
		
		lables.add("PROJECTNAME");
		lables.add("OWNER");
		lables.add("MANAGER");
		lables.add("KHLXR");
		lables.add("CLBM");
		lables.add("KHLXDH");
		lables.add("CZBM");
		lables.add("GPFXJZ");
		lables.add("STARTDATE");
//		lables.add("MEMO");
//		lables.add("ATTACH");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_GPFXXMB WHERE PROJECTNO=?");
		Map params = new HashMap();
		params.put(1, xmbh);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			HashMap data = content.get(0);
			String cutomername = data.get("CUSTOMERNAME")==null?"":data.get("CUSTOMERNAME").toString();
			
			String projectname = data.get("PROJECTNAME")==null?"":data.get("PROJECTNAME").toString();
			String owner = data.get("OWNER")==null?"":data.get("OWNER").toString();
			String manager = data.get("MANAGER")==null?"":data.get("MANAGER").toString();
			String khlxr = data.get("KHLXR")==null?"":data.get("KHLXR").toString();
			String clbm = data.get("CLBM")==null?"":data.get("CLBM").toString();
			String khlxdh = data.get("KHLXDH")==null?"":data.get("KHLXDH").toString();
			String czbm = data.get("CZBM")==null?"":data.get("CZBM").toString();
			String gpfxjz = data.get("GPFXJZ")==null?"":data.get("GPFXJZ").toString();
			String startdate = data.get("STARTDATE")==null?"":data.get("STARTDATE").toString();
			String extend1 ="";
			String extend2 = "";
			if(!"".equals(owner)){
				if(owner.contains("[")){
					
					int indexOf = owner.indexOf("[");
					extend1=owner.substring(0, indexOf);
					
				}
				
			}
			if(!"".equals(manager)){
				if(manager.contains("[")){
					
					int indexOf = manager.indexOf("[");
					extend2=manager.substring(0, indexOf);
					
				}
				
			}
			result.put("CUSTOMERNAME", cutomername);
			
			result.put("PROJECTNAME", projectname);
			result.put("OWNER", owner);
			result.put("MANAGER", manager);
			result.put("KHLXR", khlxr);
			result.put("CLBM", clbm);
			result.put("KHLXDH", khlxdh);
			result.put("CZBM", czbm);
			result.put("GPFXJZ", gpfxjz);
			result.put("STARTDATE", startdate);
			result.put("EXTEND1", extend1);
			result.put("EXTEND2", extend2);
		}else{
			result.put("CUSTOMERNAME", "");
			
			result.put("PROJECTNAME", "");
			result.put("OWNER", "");
			result.put("MANAGER", "");
			result.put("KHLXR", "");
			result.put("CLBM", "");
			result.put("KHLXDH", "");
			result.put("CZBM", "");
			result.put("GPFXJZ", "");
			result.put("STARTDATE", "");
			result.put("EXTEND1", "");
			result.put("EXTEND2", "");
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getMainContent(String xmbh){
		List<String> lables = new ArrayList<String>();
		lables.add("CUSTOMERNAME");
		
		lables.add("PROJECTNAME");
		lables.add("OWNER");
		lables.add("MANAGER");
		lables.add("KHLXR");
		lables.add("CLBM");
		lables.add("KHLXDH");
		lables.add("CZBM");
		lables.add("GPFXJZ");
		lables.add("STARTDATE");
		lables.add("MEMO");
		lables.add("ATTACH");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_GPFXXMB WHERE PROJECTNO=?");
		Map params = new HashMap();
		params.put(1, xmbh);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			HashMap data = content.get(0);
			String cutomername = data.get("CUSTOMERNAME")==null?"":data.get("CUSTOMERNAME").toString();
			
			String projectname = data.get("PROJECTNAME")==null?"":data.get("PROJECTNAME").toString();
			String owner = data.get("OWNER")==null?"":data.get("OWNER").toString();
			String manager = data.get("MANAGER")==null?"":data.get("MANAGER").toString();
			String khlxr = data.get("KHLXR")==null?"":data.get("KHLXR").toString();
			String clbm = data.get("CLBM")==null?"":data.get("CLBM").toString();
			String khlxdh = data.get("KHLXDH")==null?"":data.get("KHLXDH").toString();
			String czbm = data.get("CZBM")==null?"":data.get("CZBM").toString();
			String gpfxjz = data.get("GPFXJZ")==null?"":data.get("GPFXJZ").toString();
			String startdate = data.get("STARTDATE")==null?"":data.get("STARTDATE").toString();
			String extend1="";
			String extend2 ="";
			if(!"".equals(owner)){
				if(owner.contains("[")){
					
					int indexOf = owner.indexOf("[");
					extend1=owner.substring(0, indexOf);
					
				}
				
			}
			if(!"".equals(manager)){
				if(manager.contains("[")){
					
					int indexOf = manager.indexOf("[");
					extend2=manager.substring(0, indexOf);
					
				}
				
			}
			
			result.put("CUSTOMERNAME", cutomername);
			
			result.put("PROJECTNAME", projectname);
			result.put("OWNER", owner);
			result.put("MANAGER", manager);
			result.put("KHLXR", khlxr);
			result.put("CLBM", clbm);
			result.put("KHLXDH", khlxdh);
			result.put("CZBM", czbm);
			result.put("GPFXJZ", gpfxjz);
			result.put("STARTDATE", startdate);
			result.put("EXTEND1", extend1);
			result.put("EXTEND2", extend2);
		}else{
			result.put("CUSTOMERNAME", "");
			
			result.put("PROJECTNAME", "");
			result.put("OWNER", "");
			result.put("MANAGER", "");
			result.put("KHLXR", "");
			result.put("CLBM", "");
			result.put("KHLXDH", "");
			result.put("CZBM", "");
			result.put("GPFXJZ", "");
			result.put("STARTDATE", "");
			result.put("EXTEND1", "");
			result.put("EXTEND2", "");
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public List<HashMap<String, Object>> getDzWothTaskList(String xmbh) {
		List<HashMap<String, Object>> list = zqbHlProjectDAO.getDzWothTaskList(xmbh);
		return list;
	}
	public List<HashMap<String, Object>> getDzWthTaskList(String xmbh) {
		List<HashMap<String, Object>> list = zqbHlProjectDAO.getDzWthTaskList(xmbh);
		return list;
	}
	public List<HashMap> getBgRunlist(String customername,String jydsf,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		return zqbHlProjectDAO.getBgRunlist(customername,jydsf,clbm,czbm,cyrxm,runPageNumber,runPageSize);
	}
	public List<HashMap> getSgRunlist(String customername,String sgfs ,String khlxdh,String czbm,String cyrxm,int runPageNumber,int runPageSize) {
		return zqbHlProjectDAO.getSgRunlist(customername,sgfs,khlxdh,czbm,cyrxm,runPageNumber,runPageSize);
	}
	public int getBgRunlistSize(String customername,String jydsf,String clbm,String czbm,String cyrxm) {
		return zqbHlProjectDAO.getBgRunlistSize(customername,jydsf,clbm,czbm,cyrxm).size();
	}
	public int getSgRunlistSize(String customername,String sgfs,String clbm,String czbm,String cyrxm) {
		return zqbHlProjectDAO.getSgRunlistSize(customername,sgfs,clbm,czbm,cyrxm).size();
	}
	public List<HashMap> getBgCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		return zqbHlProjectDAO.getBgCloselist(customername,clbm,czbm,cyrxm,closePageNumber,closePageSize);
	}
	public List<HashMap> getSgCloselist(String customername,String clbm,String czbm,String cyrxm,int closePageNumber,int closePageSize) {
		return zqbHlProjectDAO.getSgCloselist(customername,clbm,czbm,cyrxm,closePageNumber,closePageSize);
	}
	public int getSgCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		return zqbHlProjectDAO.getSgCloselistSize(customername,clbm,czbm,cyrxm).size();
	}
	public int getBgCloselistSize(String customername,String clbm,String czbm,String cyrxm) {
		return zqbHlProjectDAO.getBgCloselistSize(customername,clbm,czbm,cyrxm).size();
	}
	public String sgClosePro(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String sgxmglUUID = com.iwork.core.db.DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='收购项目管理'", "UUID");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("XMZT", "0");
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(sgxmglUUID, instanceId,dataMap, dataid, false);
						String customername = dataMap.get("JYF").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "收购项目维护", "收购项目："+customername);
						}else{
							content.append(customername).append(",");
						}
					} catch (Exception e) {
						
					}
				}
			}
			if(content.length()>0){
				info = content.substring(0, content.length()-1);
			}
		}
		return info;
	}
	
	public String bgClosePro(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String sgxmglUUID = com.iwork.core.db.DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='重组项目管理'", "UUID");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("XMZT", "0");
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(sgxmglUUID, instanceId,dataMap, dataid, false);
						String customername = dataMap.get("JYF").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "重组项目维护", "重组项目："+customername);
						}else{
							content.append(customername).append(",");
						}
					} catch (Exception e) {
						
					}
				}
			}
			if(content.length()>0){
				info = content.substring(0, content.length()-1);
			}
		}
		return info;
	}
	public String getSgMainContent(String xmbh){
		List<String> lables = new ArrayList<String>();
		lables.add("JYF");
		lables.add("JYDSF");
		lables.add("SGFS");
		lables.add("CZBM");
		lables.add("SFGLJY");
		lables.add("GDYKZRBG");
		lables.add("SFCZ");
		lables.add("SFWG");
		lables.add("OWNER");
		lables.add("MANAGER");
		
		lables.add("COMPANYNAME");
		lables.add("KHLXDH");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_BGZZLXXX WHERE XMBH=?");
		Map params = new HashMap();
		params.put(1, xmbh);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			HashMap data = content.get(0);
			String jyf = data.get("JYF")==null?"":data.get("JYF").toString();
			String jydsf = data.get("JYDSF")==null?"":data.get("JYDSF").toString();
			String sgfs = data.get("SGFS")==null?"":data.get("SGFS").toString();
			String czbm = data.get("CZBM")==null?"":data.get("CZBM").toString();
			String sfgljy = data.get("SFGLJY")==null?"":data.get("SFGLJY").toString();
			String gdykzrbg = data.get("GDYKZRBG")==null?"":data.get("GDYKZRBG").toString();
			String sfcz = data.get("SFCZ")==null?"":data.get("SFCZ").toString();
			String sfwg = data.get("SFWG")==null?"":data.get("SFWG").toString();
			String owner = data.get("OWNER")==null?"":data.get("OWNER").toString();
			
			String KHLXDH = data.get("KHLXDH")==null?"":data.get("KHLXDH").toString();
			String manager = data.get("MANAGER")==null?"":data.get("MANAGER").toString();
			String extend1 = "";
			String extend2 = "";
			
			if(!"".equals(owner)){
				if(owner.contains("[")){
					
					int indexOf = owner.indexOf("[");
					extend1=owner.substring(0, indexOf);
					
				}
				
			}
			if(!"".equals(manager)){
				if(manager.contains("[")){
					
					int indexOf = manager.indexOf("[");
					extend2=manager.substring(0, indexOf);
					
				}
				
			}
			
			result.put("JYF", jyf);
			result.put("JYDSF", jydsf);
			result.put("SGFS", sgfs);
			result.put("CZBM", czbm);
			result.put("SFGLJY", sfgljy);
			result.put("GDYKZRBG", gdykzrbg);
			result.put("SFCZ", sfcz);
			result.put("SFWG", sfwg);
			result.put("OWNER", owner);
			
			result.put("MANAGER", manager);
			result.put("EXTEND1", extend1);
			result.put("EXTEND2", extend2);
			result.put("KHLXDH", KHLXDH);
		}else{
			result.put("JYF", "");
			
			result.put("JYDSF", "");
			result.put("SGFS", "");
			result.put("CZBM", "");
			result.put("SFGLJY", "");
			result.put("GDYKZRBG", "");
			result.put("SFCZ", "");
			result.put("SFWG", "");
			result.put("OWNER", "");
			result.put("MANAGER", "");
			result.put("EXTEND1", "");
			result.put("EXTEND2", "");
			result.put("KHLXDH", "");
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getBgMainContent(String xmbh){
		List<String> lables = new ArrayList<String>();
		lables.add("JYF");
		lables.add("JYDSF");
		lables.add("SGFS");
		lables.add("CZBM");
		lables.add("SFGLJY");
		lables.add("GDYKZRBG");
		lables.add("SFCZ");
		lables.add("SFWG");
		lables.add("OWNER");
		lables.add("MANAGER");
		
		lables.add("COMPANYNAME");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_BGZZLXXX WHERE XMBH=?");
		Map params = new HashMap();
		params.put(1, xmbh);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			HashMap data = content.get(0);
			String jyf = data.get("JYF")==null?"":data.get("JYF").toString();
			String jydsf = data.get("JYDSF")==null?"":data.get("JYDSF").toString();
			String sgfs = data.get("SGFS")==null?"":data.get("SGFS").toString();
			String czbm = data.get("CZBM")==null?"":data.get("CZBM").toString();
			String sfgljy = data.get("SFGLJY")==null?"":data.get("SFGLJY").toString();
			String gdykzrbg = data.get("GDYKZRBG")==null?"":data.get("GDYKZRBG").toString();
			String sfcz = data.get("SFCZ")==null?"":data.get("SFCZ").toString();
			String sfwg = data.get("SFWG")==null?"":data.get("SFWG").toString();
			String owner = data.get("OWNER")==null?"":data.get("OWNER").toString();
			String manager = data.get("MANAGER")==null?"":data.get("MANAGER").toString();
			
			String extend1 = "";
			String extend2 = "";
			if(!"".equals(owner)){
				if(owner.contains("[")){
					
					int indexOf = owner.indexOf("[");
					extend1=owner.substring(0, indexOf);
					
				}
				
			}
			if(!"".equals(manager)){
				if(manager.contains("[")){
					
					int indexOf = manager.indexOf("[");
					extend2=manager.substring(0, indexOf);
					
				}
				
			}
			
			
			result.put("JYF", jyf);
			result.put("JYDSF", jydsf);
			result.put("SGFS", sgfs);
			result.put("CZBM", czbm);
			result.put("SFGLJY", sfgljy);
			result.put("GDYKZRBG", gdykzrbg);
			result.put("SFCZ", sfcz);
			result.put("SFWG", sfwg);
			result.put("OWNER", owner);
			result.put("MANAGER", manager);
			result.put("EXTEND1", extend1);
			result.put("EXTEND2", extend2);
			
		}else{
			result.put("JYF", "");
			result.put("JYDSF", "");
			result.put("SGFS", "");
			result.put("CZBM", "");
			result.put("SFGLJY", "");
			result.put("GDYKZRBG", "");
			result.put("SFCZ", "");
			result.put("SFWG", "");
			result.put("OWNER", "");
			result.put("MANAGER", "");
			result.put("EXTEND1", "");
			result.put("EXTEND2", "");
			
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public List<HashMap<String, Object>> getSgTaskList(String xmbh) {
		List<HashMap<String, Object>> list = zqbHlProjectDAO.getSgTaskList(xmbh);
		return list;
	}
	public List<HashMap<String, Object>> getBgTaskList(String xmbh) {
		List<HashMap<String, Object>> list = zqbHlProjectDAO.getBgTaskList(xmbh);
		return list;
	}
	public List<HashMap> getRunList(String customername,String xmjd,String cyrxm,int runPageNumber,int runPageSize){
		return zqbHlProjectDAO.getRunList(customername,xmjd,cyrxm,runPageNumber,runPageSize);
	}
	public int getRunlistSize(String customername,String xmjd,String cyrxm){
		return zqbHlProjectDAO.getRunListSize(customername,xmjd,cyrxm).size();
	}
	public List<HashMap> getCloselist(String customername,String cyrxm,int runPageNumber,int runPageSize){
		return zqbHlProjectDAO.getCloselist(customername,cyrxm,runPageNumber,runPageSize);
	}
	public int getCloselistSize(String customername,String cyrxm){
		return zqbHlProjectDAO.getCloselistSize(customername,cyrxm).size();
	}
	public String closePro(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String qtxmglUUID = com.iwork.core.db.DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='项目立项表单'", "UUID");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("STATUS", "已完成");
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(qtxmglUUID, instanceId,dataMap, dataid, false);
						String customername = dataMap.get("CUSTOMERNAME").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "挂牌项目维护", "关闭挂牌项目："+customername);
						}else{
							content.append(customername).append(",");
						}
					} catch (Exception e) {
						
					}
				}
			}
			if(content.length()>0){
				info = content.substring(0, content.length()-1);
			}
		}
		return info;
	}
	
	public String getGpMainContent(String xmbh){
		List<String> lables = new ArrayList<String>();
		lables.add("PROJECTNAME");
		
		lables.add("XMBZ");
		lables.add("XMYS");
		lables.add("ZCLR");
		lables.add("HTJE");
		lables.add("SFXZCL");
		lables.add("CUSTOMERINFO");
		lables.add("GSGK");
		lables.add("OWNER");
		lables.add("MANAGER");
		lables.add("ZBTASKID");
		lables.add("ZBSTEPID");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=?");
		Map params = new HashMap();
		params.put(1, xmbh);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			HashMap data = content.get(0);
			String projectname = data.get("PROJECTNAME")==null?"":data.get("PROJECTNAME").toString();
			
			String xmbz = data.get("XMBZ")==null?"":data.get("XMBZ").toString();
			String xmys = data.get("XMYS")==null?"":data.get("XMYS").toString();
			String zclr = data.get("ZCLR")==null?"":data.get("ZCLR").toString();
			String htje = data.get("HTJE")==null?"":data.get("HTJE").toString();
			String sfxzcl = data.get("SFXZCL")==null?"":data.get("SFXZCL").toString();
			String customerinfo = data.get("CUSTOMERINFO")==null?"":data.get("CUSTOMERINFO").toString();
			String gsgk = data.get("GSGK")==null?"":data.get("GSGK").toString();
			String owner = data.get("OWNER")==null?"":data.get("OWNER").toString();
			String manager = data.get("MANAGER")==null?"":data.get("MANAGER").toString();
			String A02=data.get("ZBSTEPID")==null?"":data.get("ZBSTEPID").toString();
			String A03=data.get("ZBTASKID")==null?"":data.get("ZBTASKID").toString();
			result.put("PROJECTNAME", projectname);
			
			result.put("XMBZ", xmbz);
			result.put("XMYS", xmys);
			result.put("ZCLR", zclr);
			result.put("HTJE", htje);
			result.put("SFXZCL", sfxzcl);
			result.put("CUSTOMERINFO", customerinfo);
			result.put("GSGK", gsgk);
			result.put("OWNER", owner);
			result.put("MANAGER", manager);
			result.put("A02", A02);
			result.put("A03", A03);
		}else{
			result.put("PROJECTNAME", "");
			
			result.put("SSHY", "");
			result.put("XMYS", "");
			result.put("ZCLR", "");
			result.put("HTJE", "");
			result.put("SFXZCL", "");
			result.put("SFCZ", "");
			result.put("GSGK", "");
			result.put("OWNER", "");
			result.put("MANAGER", "");
			result.put("A02", "");
			result.put("A03", "");
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public List<HashMap<String, Object>> getGpTaskList(String xmbh) {
		List<HashMap<String, Object>> list = zqbHlProjectDAO.getGpTaskList(xmbh);
		return list;
	}
	public List<HashMap> getDzwothRunlist(String customername,String fxzt, String clbm, String czbm, String cyrxm,
			int runPageNumber, int runPageSize) {
		
		return zqbHlProjectDAO.getDzwothRunlist(customername,fxzt,clbm,czbm,cyrxm,runPageNumber,runPageSize);
	}
	public int getDzwothRunlistSize(String customername,String fxzt, String clbm, String czbm, String cyrxm) {
		
		return zqbHlProjectDAO.getDzwothRunlistSize(customername,fxzt,clbm,czbm,cyrxm).size();
	}
	public List<HashMap> getDzwothCloselist(String customername, String clbm, String czbm, String cyrxm,
			int closePageNumber, int closePageSize) {
		
		return zqbHlProjectDAO.getDzwothCloselist(customername,clbm,czbm,cyrxm,closePageNumber,closePageSize);
	}
	public int getDzwothCloselistSize(String customername, String clbm, String czbm, String cyrxm) {
		
		return zqbHlProjectDAO.getDzwothCloselistSize(customername,clbm,czbm,cyrxm).size();
	}
	public List<HashMap> getYbcwRunlist(String customername, String clbm, String czbm, String cyrxm,
			int runPageNumber, int runPageSize) {
		
		return zqbHlProjectDAO.getYbcwRunlist(customername,clbm,czbm,cyrxm,runPageNumber,runPageSize);
	}
	public int getYbcwRunlistSize(String customername, String clbm, String czbm, String cyrxm) {
		
		return zqbHlProjectDAO.getYbcwRunlistSize(customername,clbm,czbm,cyrxm).size();
	}
	public List<HashMap> getYbcwCloselist(String customername, String clbm, String czbm, String cyrxm,
			int closePageNumber, int closePageSize) {
		
		return zqbHlProjectDAO.getYbcwCloselist(customername,clbm,czbm,cyrxm,closePageNumber,closePageSize);
	}
	public int getYbcwCloselistSize(String customername, String clbm, String czbm, String cyrxm) {
		
		return zqbHlProjectDAO.getYbcwCloselistSize(customername,clbm,czbm,cyrxm).size();
	}
	public String getYbcwMainContent(String xmbh){
		List<String> lables = new ArrayList<String>();
		lables.add("CUSTOMERNAME");
		lables.add("CLBM");
		lables.add("XMLX");
		lables.add("CZBM");
		lables.add("XMFWNR");
		lables.add("CWGWZZ");
//		lables.add("SFCZ");
		lables.add("OWNER");
		lables.add("MANAGER");
		
		lables.add("COMPANYNAME");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_CWGWXMXX WHERE XMBH=?");
		Map params = new HashMap();
		params.put(1, xmbh);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			HashMap data = content.get(0);
			String jyf = data.get("CUSTOMERNAME")==null?"":data.get("CUSTOMERNAME").toString();
			String xmfwnr = data.get("XMFWNR")==null?"":data.get("XMFWNR").toString();
			String clbm = data.get("CLBM")==null?"":data.get("CLBM").toString();
			String czbm = data.get("CZBM")==null?"":data.get("CZBM").toString();
//			String sfgljy = data.get("SFGLJY")==null?"":data.get("SFGLJY").toString();
			String cwgwzz = data.get("CWGWZZ")==null?"":data.get("CWGWZZ").toString();
//			String sfcz = data.get("SFCZ")==null?"":data.get("SFCZ").toString();
			String xmlx = data.get("XMLX")==null?"":data.get("XMLX").toString();
			String owner = data.get("OWNER")==null?"":data.get("OWNER").toString();
			String manager = data.get("MANAGER")==null?"":data.get("MANAGER").toString();
			String a02 = "";
			String a03 ="";
			
			if(!"".equals(owner)){
				if(owner.contains("[")){
					
					int indexOf = owner.indexOf("[");
					a02=owner.substring(0, indexOf);
					
				}
				
			}
			if(!"".equals(manager)){
				if(manager.contains("[")){
					
					int indexOf = manager.indexOf("[");
					a03=manager.substring(0, indexOf);
					
				}
				
			}
			
			result.put("JYF", jyf);
			
			result.put("CLBM", clbm);
			result.put("XMLX", xmlx);
			result.put("CZBM", czbm);
			result.put("XMFWNR", xmfwnr);
//			result.put("SFGLJY", sfgljy);
			result.put("CWGWZZ", cwgwzz);
//			result.put("SFCZ", sfcz);
			result.put("OWNER", owner);
			result.put("MANAGER", manager);
			result.put("A02", a02);
			result.put("A03", a03);
		}else{
			
			result.put("JYF", "");
			result.put("XMFWNR", "");
			result.put("CLBM", "");
			result.put("CZBM", "");
			result.put("CWGWZZ", "");
			result.put("XMLX", "");
//			result.put("SFCZ", "");
//			result.put("SFWG", "");
			result.put("OWNER", "");
			result.put("MANAGER", "");
			result.put("A02", "");
			result.put("A03", "");
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String ybcwClosePro(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String sgxmglUUID = com.iwork.core.db.DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='一般性财务顾问项目'", "UUID");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("XMZT", "0");
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(sgxmglUUID, instanceId,dataMap, dataid, false);
						String customername = dataMap.get("JYF").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "一般性财务顾问项目维护", "一般性财务顾问项目："+customername);
						}else{
							content.append(customername).append(",");
						}
					} catch (Exception e) {
						
					}
				}
			}
			if(content.length()>0){
				info = content.substring(0, content.length()-1);
			}
		}
		return info;
	}
	public List<HashMap<String, Object>> getYbcwTaskList(String xmbh) {
		List<HashMap<String, Object>> list = zqbHlProjectDAO.getYbcwTaskList(xmbh);
		return list;
	}
	
	public List<HashMap> getQtxsList(int pageSize,int pageNumber){
		return zqbHlProjectDAO.getQtxsList(pageSize,pageNumber);
	}
	public int getQtxsListSize(){
		return zqbHlProjectDAO.getQtxsListSize().size();
	}
	
	private final static String CN_FILENAME = "/common.properties";
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	private String flag="";
	public String delHlDz() {
		flag="";
		
			try {
				Map params = new HashMap();
				params.put(1, "%PM%");
				
				DBUTilNew.update(" delete from BD_ZQB_GPFXXMB where PROJECTNO like ? ",params);
				DBUTilNew.update(" delete from BD_ZQB_GPFXXMNFXLCB where PROJECTNO like ? ",params);
				flag="success";
			} catch (Exception e) {
				flag="error";
			}
			return flag;
	}
	public String delHlDzw() {
		try {
			Map params = new HashMap();
			params.put(1,"%PT%");
			DBUTilNew.update(" delete from BD_ZQB_GPFXXMNFXXXLCB where PROJECTNO like ? ",params);
			DBUTilNew.update(" delete from BD_ZQB_GPFXXMB where PROJECTNO like ? ",params);
			flag="success";
		} catch (Exception e) {
			flag="error";
		}
		return flag;
	}
	public String delHlBg() {
		try {
			Map params = new HashMap();
			params.put(1, "%CZ%");
			
			DBUTilNew.update(" delete from BD_ZQB_BGXMLX where XMBH like ? ",params);
			DBUTilNew.update(" delete from BD_ZQB_BGZZLXXX where XMBH like ? ",params);
			flag="success";
		} catch (Exception e) {
			flag="error";
		}
		return flag;
	}
	public String delHlSg() {
		try {
			Map params = new HashMap();
			params.put(1, "%SG%");
			
			DBUTilNew.update(" delete from BD_ZQB_BGXMLX where XMBH like ? ",params);
			DBUTilNew.update(" delete from BD_ZQB_BGZZLXXX where XMBH like ? ",params);
			flag="success";
		} catch (Exception e) {
			flag="error";
		}
		return flag;
	}
	public String delHlGp() {
		try {
			Map params = new HashMap();
			params.put(1, "%PM%");
			DBUTilNew.update(" delete from BD_ZQB_PJ_BASE where PROJECTNO like ? ",params);
			DBUTilNew.update(" delete from BD_ZQB_XMLCXXB where PROJECTNO like ? ",params);
			flag="success";
		} catch (Exception e) {
			flag="error";
		}
		return flag;
	}
	public String delHlYbcw() {
		try {
			Map params = new HashMap();
			params.put(1,"%YBCW%");
			DBUTilNew.update(" delete from BD_ZQB_CWGWXMXX where XMBH like ? ",params);
			
			DBUTilNew.update(" delete from BD_ZQB_ZBSP where PROJECTNO like ? ",params);
			flag="success";
		} catch (Exception e) {
			flag="error";
		}
		return flag;
	}
	public String delHlQt() {
		try {
			Map params = new HashMap();
			params.put(1,"%HLQT%");
			DBUTilNew.update(" delete from BD_ZQB_TYXMJD where XMBH like ? ",params);
			
			DBUTilNew.update(" delete from BD_ZQB_TYXM where PROJECTNO like ? ",params);
			flag="success";
		} catch (Exception e) {
			flag="error";
		}
		return flag;
	}
	public String getTentUserDateForRcTwo(String xmbh,String jdmc){
		String xmlx=null;
		if(xmbh.contains("CZ")||xmbh.contains("BG")){
			xmlx="并购项目";
		}else if(xmbh.contains("SG")){
			xmlx="收购项目管理";
		}else if(xmbh.contains("YBCW")){
			xmlx="一般性财务顾问项目";
		}else if(xmbh.contains("PT")){
			xmlx="定增项目（200人以上）";
		}else if(xmbh.contains("PM")){
			xmlx="定增项目（200人以内）";
		}else{
			xmlx="其他项目";
		}
		StringBuffer sb = new StringBuffer("");
		List lables = new ArrayList();
		lables.add("CONTENT");
		
		//String sql = "SELECT CONTENT,CONDATE,CONUSER FROM BD_XP_GGSMJ WHERE GGINS=(SELECT INSTANCEID FROM BD_XP_RCYWCBZSJ WHERE YXID=?)";
		
		String sql="SELECT * FROM BD_ZQB_TYXM_INFO BZKI LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE" 
				+" FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE "+
				 "WHERE ENGINE.TITLE = '通用项目阶段提交资料清单')) SYSDA ON BZKI.ID=SYSDA.DATAID   WHERE 1=1 AND BZKI.STATE=1 AND XMLX=? AND BZKI.JDMC=?";
		Map params = new HashMap();
		params.put(1, xmlx);
		params.put(2, jdmc==null?"":jdmc.toString());
		List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
		String contenttext = "";
		
		if(data.size()==1){
			contenttext = data.get(0).get("CONTENT")==null?"":data.get(0).get("CONTENT").toString();
			
			sb.append(contenttext);
		}
		return sb.toString();
	}
	public Long getConfig(String parameter)
	  {
	    Map<String, String> config = ConfigUtil.readAllProperties("/common.properties");
	    long result = 0L;
	    if ((parameter != null) && (!"".equals(parameter))) {
	      result = Long.parseLong((String)config.get(parameter));
	    }
	    return Long.valueOf(result);
	  }
	public String getTentUserDateForProTwo(String jdmc){
//		String XMBH=null;
//		if(xmbh.contains("CZ")){
//			XMBH="并购项目";
//		}else if(xmbh.contains("SG")){
//			XMBH="收购项目管理";
//		}else if(xmbh.contains("YBCW")){
//			XMBH="一般性财务顾问项目";
//		}else if(xmbh.contains("PT")){
//			XMBH="定增项目（200人以上）";
//		}else if(xmbh.contains("PM")){
//			XMBH="定增项目（200人以内）";
//		}else{
//			XMBH="其他项目";
//		}
		StringBuffer sb = new StringBuffer("");
		List lables = new ArrayList();
		lables.add("CONTENT");
		
		//String sql = "SELECT B.CONTENT,B.CONDATE,B.CONUSER FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148 AND B.GGINS=?";
//		String sql ="SELECT BZKI.CJRXM CONUSER,BZKI.CJSJ CONDATE,BZKI.CONTENT FROM BD_ZQB_TYXM_INFO BZKI LEFT JOIN ("
//				 +"SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '通用项目阶段提交资料清单')) "
//		        +"SYSDA ON BZKI.ID=SYSDA.DATAID  "
//				+"  WHERE 1=1 AND BZKI.STATE=1 AND BZKI.XMLX=? AND BZKI.JDMC=? ORDER BY BZKI.SORTID";
		String sql="SELECT BZKI.CONTENT FROM BD_ZQB_KM_INFO BZKI LEFT JOIN ("
				+"SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE ENGINE WHERE ENGINE.TITLE = '项目阶段提交资料清单')) "
				+ "SYSDA ON BZKI.ID=SYSDA.DATAID "
				 +" WHERE BZKI.STATE=1 AND BZKI.JDMC=?";
		Map params = new HashMap();
		
//		params.put(1, XMBH);
		params.put(1, jdmc==null?"":jdmc.toString());
		/*HashMap datamap = null;
		if(instanceId!=null){
			datamap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		params.put(2, datamap==null?"0":datamap.get("KHBH").toString());*/
		List<HashMap> data = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
		String contenttext = "";
		
		if(data.size()==1){
			contenttext = data.get(0).get("CONTENT")==null?"":data.get(0).get("CONTENT").toString();
			
			sb.append(contenttext);
		}
		return sb.toString();
	}
public List<HashMap> showBwlList(String projectNo, int pageNumber, int pageSize,Long formid,String createuser) {
		
		
		
		
		
		return zqbHlProjectDAO.getShowBwlList(projectNo,pageNumber,pageSize,formid,createuser);
	}
	public int showBwlListSize(String projectNo,String projectname) {
		if(projectNo==null||"".equals(projectNo)){
			return 0;
		}
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();;
		Long isManager = orgUser.getIsmanager();
		Long orgRoleid = orgUser.getOrgroleid();
		String userid = orgUser.getUserid();
		StringBuffer sb=new StringBuffer();
		Map params = new HashMap();
//		params.put(1, projectNo);
		int n=1;
		sb.append("select count (*) count from (select projectdate,progress,username,tel,tracking,projectno from bd_zqb_xmrbb where 1=1 ");//and projectno= ? 
		if(orgRoleid!=5){
			if(isManager==1l){
				sb.append(" AND CREATEUSERID IN (SELECT O.USERID FROM ORGUSER O WHERE O.DEPARTMENTID IN (select id from orgdepartment t start with t.id=(SELECT DEPARTMENTID FROM ORGUSER O WHERE O.USERID=　?　) connect by prior t.id=t.parentdepartmentid))");
			}else{
				sb.append(" AND CREATEUSERID = ?");
			}
			params.put(n, userid);
			n++;
		}
		
		if (projectNo != null && !"".equals(projectNo)) {
			sb.append(" AND XMBH like ?");
			params.put(n,"%"+projectNo+"%");
			n++;
		}
		/*if (projectname != null && !"".equals(projectname)) {
			sb.append(" OR PROJECTNO like ?");
			params.put(n,"%"+projectname+"%");
			n++;
		}*/
		
		
		sb.append(")");
		int count=DBUTilNew.getInt("count",sb.toString(),params);
		return count;
	}
	private final String appid = "wx14d879cecf31a790";
	private final String appsecret = "eg7rPU72wza1oNGHuducq28g2Hc6PX0Oarjv8p5OJcI";
	public List getUseridlist(String department,String user) {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String enddate = df.format(new Date());
		
		
		List newList=new ArrayList();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String departmentname = uc.get_userModel().getDepartmentname();
		String username = uc.get_userModel().getUsername();
		Long ismanager=uc.get_userModel().getIsmanager();
		if("综合管理部".equals(departmentname)||"杨光".equals(username)||"吕红贞".equals(username)){
			Map hashMap=new HashMap();//AND MOBILE IS NOT NULL
			StringBuffer sql=new StringBuffer("SELECT WEIXIN_CODE FROM ORGUSER WHERE ORGROLEID<>3  AND WEIXIN_CODE IS NOT NULL AND STARTDATE<=to_date(?,'yyyy-MM-dd') AND ENDDATE>=to_date(?,'yyyy-MM-dd') ");
			int a=1;
			hashMap.put(a++, enddate);
			hashMap.put(a++, enddate);
			if(department!=null&&!"".equals(department)){
				sql.append(" AND DEPARTMENTNAME LIKE ? ");
				hashMap.put(a++, "%"+department+"%");
			}
			if(user!=null&&!"".equals(user)){
				sql.append(" AND USERNAME LIKE ? ");
				hashMap.put(a++, "%"+user+"%");
			}
			List list1=new ArrayList();
			list1.add("WEIXIN_CODE");			
			List<HashMap> dataList = DBUTilNew.getDataList(list1, sql.toString(), hashMap);
			if(dataList!=null&&dataList.size()>0){
			
					for (HashMap hashMap2 : dataList) {
						String weixincode=	hashMap2.get("WEIXIN_CODE").toString();
						/*验证微信端是否有这个人 start*/
						
//						AccessToken accessToken = WeixinUtil.getInstance().getAccessToken(appid, appsecret);
//						
//							String token = accessToken.getToken();
//							String starttime = Date2TimeStamp(enddate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
//							String endtime = Date2TimeStamp(enddate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
//						String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=ACCESS_TOKEN"
//								.replace("ACCESS_TOKEN", token);
//						HashMap hash = new HashMap();
//						hash.put("opencheckindatatype", 3);
//						hash.put("starttime", starttime);
//						hash.put("endtime", endtime);
//						hash.put("useridlist", weixincode);
//
//						String postData = JSONArray.fromObject(hash).toString();
//						// 数据转换json数据时自动加上了[],应除去，不然会报错
//						int indexOf = postData.indexOf("[");
//						int indexof = postData.lastIndexOf("]");
//						postData = postData.substring(indexOf + 1, indexof);
//						JSONObject jsonObject = WeixinUtil.getInstance().httpRequest(requestUrl, "POST", postData);
//						/*验证微信端是否有这个人 end*/
//						String errmsg = (String) jsonObject.get("errmsg");
//						if("ok".equals(errmsg)){
//							newList.add(weixincode);
//						}else{
//							System.out.println(weixincode);
//						}
						
						
						newList.add(weixincode);	
						
					}
			}
			
		}else if(ismanager==1){			
			Map hashMap=new HashMap();
			hashMap.put(1, departmentname);
			List list1=new ArrayList();
			list1.add("WEIXIN_CODE");
			StringBuffer sql=new StringBuffer("SELECT WEIXIN_CODE FROM ORGUSER WHERE DEPARTMENTNAME=? AND ORGROLEID<>3  AND WEIXIN_CODE IS NOT NULL AND STARTDATE<=to_date(?,'yyyy-MM-dd') AND ENDDATE>=to_date(?,'yyyy-MM-dd') ");
			hashMap.put(2, enddate);
			hashMap.put(3, enddate);
			if(user!=null&&!"".equals(user)){
				sql.append(" AND USERNAME LIKE ? ");
				hashMap.put(4, "%"+user+"%");
			}
			 List<HashMap> dataList = DBUTilNew.getDataList(list1, sql.toString(), hashMap);			
			 if(dataList!=null&&dataList.size()>0){					
					for (HashMap hashMap2 : dataList) {
						String weixincode=	hashMap2.get("WEIXIN_CODE").toString();
						newList.add(weixincode);
					}	
			}
		}else{
			newList.add(uc.get_userModel().getWeixinCode());
		}
		
		return newList;
	}
	public HashMap getQjlcListByUserid(String dataStr, String timeDate) {
		String sq="select username from orguser where weixin_code =?";
		Map params2=new HashMap();
		params2.put(1, dataStr);
		String username = DBUTilNew.getDataStr("USERNAME", sq, params2);
		HashMap map = new HashMap();
		List list= new ArrayList();
		list.add("MEMO");
		List list1= new ArrayList();
		list1.add("BSLX");
		list1.add("CCMDD");
		list1.add("CCSY");
		Map params=new HashMap();
		Map params1=new HashMap();
		StringBuffer sql =new  StringBuffer("select  MEMO from BD_ZQB_XMRWGLLCB  where SPZT='审批通过' ");
		StringBuffer sql1 =new  StringBuffer("select  BSLX,CCMDD,CCSY from BD_ZQB_CCYWCLCB  where ZT='审批通过'");
		if(timeDate!=null&&!"".equals(timeDate)){
			sql.append(" AND (ATTACH IS NULL OR ATTACH>to_date(?,'yyyy-MM-dd hh24:mi:ss') ) and  STARTDATE<=to_date(?,'yyyy-MM-dd') AND ENDDATE>=to_date(?,'yyyy-MM-dd') ");
			sql1.append("  and QSSJ<=to_date(?,'yyyy-MM-dd') AND JSSJ>=to_date(?,'yyyy-MM-dd') ");
			params.put(1, timeDate);
			params.put(2, timeDate);
			params.put(3, timeDate);
			params1.put(1, timeDate);
			params1.put(2, timeDate);
		
			if(username!=null&&!"".equals(username)){
				sql.append(" AND PROJECTNAME=? ");
				sql1.append(" AND YHM=? ");
				params.put(4, username);
				params1.put(3, username);
			}
		}else{
			if(username!=null&&!"".equals(username)){
				sql.append(" PROJECTNAME=? ");
				sql1.append(" YHM=? ");
				params.put(1, username);
				params1.put(1, username);
				
			}
		}
		
		List<HashMap> dataList = DBUTilNew.getDataList(list, sql.toString(), params);
		
		if(dataList!=null&&dataList.size()>0){
			String bzsm= dataList.get(0).get("MEMO").toString();
			
			map.put("BZSM", bzsm);
			map.put("LX","请假");
		
		}else{
			List<HashMap> dataList1 = DBUTilNew.getDataList(list1, sql1.toString(), params1);
			
			if(dataList1!=null&&dataList1.size()>0){
			String lx=	 dataList1.get(0).get("BSLX")==null?"":dataList1.get(0).get("BSLX").toString();
			if("外出".equals(lx)){
										
			map.put("BZSM", dataList1.get(0).get("CCSY")==null?"":dataList1.get(0).get("CCSY").toString());
			map.put("LX", "外出");
			}else{
				String ccmdd=dataList1.get(0).get("CCMDD")==null?"":dataList1.get(0).get("CCMDD").toString();
				String ccsy=dataList1.get(0).get("CCSY")==null?"":dataList1.get(0).get("CCSY").toString();
				map.put("BZSM", ccmdd+":"+ccsy);
				map.put("LX", "出差");
			}
			}
			
			
		}
		
		
		return map;
	}
	 /**
     * Java将Unix时间戳转换成指定格式日期字符串
     * @param timestampString 时间戳 如："1473048265";
     * @param formats 要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     *
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
	 public  String TimeStamp2Date(String timestampString, String formats) {
	        if (TextUtils.isEmpty(formats))
	            formats = "yyyy-MM-dd HH:mm:ss";
	        Long timestamp = Long.parseLong(timestampString) * 1000;
	        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
	        return date;
	    }
	 /**
	     * 日期格式字符串转换成时间戳
	     *
	     * @param dateStr 字符串日期
	     * @param format   如：yyyy-MM-dd HH:mm:ss
	     *
	     * @return
	     */
	 public  String Date2TimeStamp(String dateStr, String format) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(format);
	            return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "";
	    }
	public void expDaily(HttpServletResponse response, List<Map<String, Object>> list) {
		HashMap sheetMap=new HashMap();
		sheetMap.put("1", 0);
		sheetMap.put("2", 0);
		sheetMap.put("3", 0);
		sheetMap.put("4", 0);
		sheetMap.put("5", 0);
//		sheetMap.put("6", 0);
//		sheetMap.put("7", 0);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("签到明细");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor((short) 22);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);
		
		HSSFCellStyle style7 = wb.createCellStyle();
		style7.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style7.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style7.setFillForegroundColor((short) 22);
		style7.setFillPattern((short) 1);
		style7.setBorderBottom((short) 1);
		style7.setBorderLeft((short) 1);
		style7.setBorderRight((short) 1);
		style7.setBorderTop((short) 1);
		style7.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFFont errofont = wb.createFont();
		errofont.setFontName("宋体");
		errofont.setFontHeightInPoints((short) 11);
		errofont.setColor(HSSFColor.RED.index);
		style7.setFont(errofont);
		
		int m=0;
		int z=0;
		int n=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = row1.createCell((short) z++);
		cell1.setCellValue("姓名");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("日期");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("上班签到");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("下班签到");
		cell1.setCellStyle(style5);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("备注说明");
		cell1.setCellStyle(style5);
//		cell1 = row1.createCell((short) z++);
//		cell1.setCellValue("工作内容");
//		cell1.setCellStyle(style5);
//		cell1 = row1.createCell((short) z++);
//		cell1.setCellValue("进度说明");
//		cell1.setCellStyle(style5);
//		cell1 = row1.createCell((short) z++);
//		cell1.setCellValue("备注说明");
//		cell1.setCellStyle(style5);
		for (Map<String, Object> map : list) {
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2 = row2.createCell((short) n++);
			short colLength2 = (short) (map.get("USERNAME") == null ? 0: map.get("USERNAME").toString().length() * 256 * 2);
			cell2.setCellValue(map.get("USERNAME")==null?"":map.get("USERNAME").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("USERNAME") == null ? 0: map.get("USERNAME").toString().length() * 256 * 2);
			sheetMap.put("1", colLength2>Short.parseShort(sheetMap.get("1").toString())?colLength2:Short.parseShort(sheetMap.get("1").toString()));
			cell2 = row2.createCell((short) n++);
			
			cell2.setCellValue(map.get("checkintime")==null?"":map.get("checkintime").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("checkintime") == null ? 0	: map.get("checkintime").toString().length() * 256 * 2);
			sheetMap.put("2", colLength2>Short.parseShort(sheetMap.get("2").toString())?colLength2:Short.parseShort(sheetMap.get("2").toString()));
			cell2 = row2.createCell((short) n++);
			
			
			

			cell2.setCellValue(map.get("checkin_timei")==null?"":map.get("checkin_timei").toString());
			String exception_typei=map.get("exception_typei")==null?"":map.get("exception_typei").toString();
				if("时间异常".equals(exception_typei))	{//异常签到标红
					cell2.setCellStyle(style7);

				}else{
					
					cell2.setCellStyle(style4);
				}
			colLength2 = (short) (map.get("checkin_timei") == null ? 0: map.get("checkin_timei").toString().length() * 256 * 2);
			sheetMap.put("3", colLength2>Short.parseShort(sheetMap.get("3").toString())?colLength2:Short.parseShort(sheetMap.get("3").toString()));
			cell2 = row2.createCell((short) n++);
			
			
			cell2.setCellValue(map.get("checkin_timej")==null?"":map.get("checkin_timej").toString());
			String exception_typej=map.get("exception_typej")==null?"":map.get("exception_typej").toString();
			if("时间异常".equals(exception_typej))	{
				cell2.setCellStyle(style7);

			}else{
				
				cell2.setCellStyle(style4);
			}
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("checkin_timej") == null ? 0: map.get("checkin_timej").toString().length() * 256 * 2);
			sheetMap.put("4", colLength2>Short.parseShort(sheetMap.get("4").toString())?colLength2:Short.parseShort(sheetMap.get("4").toString()));
			cell2 = row2.createCell((short) n++);
			
			
			cell2.setCellValue(map.get("notes")==null?"":map.get("notes").toString());
			cell2.setCellStyle(style4);
			colLength2 = (short) (map.get("notes") == null ? 0: map.get("notes").toString().length() * 256 * 2);
			sheetMap.put("5", colLength2>Short.parseShort(sheetMap.get("5").toString())?colLength2:Short.parseShort(sheetMap.get("5").toString()));
			cell2 = row2.createCell((short) n++);
			
			
//			
//			cell2.setCellValue(map.get("USERNAME")==null?"":map.get("USERNAME").toString());
//			cell2.setCellStyle(style4);
//			colLength2 = (short) (map.get("USERNAME") == null ? 0: map.get("USERNAME").toString().length() * 256 * 2);
//			sheetMap.put("6", colLength2>Short.parseShort(sheetMap.get("6").toString())?colLength2:Short.parseShort(sheetMap.get("6").toString()));
//			cell2 = row2.createCell((short) n++);
//			
//			
//			cell2.setCellValue(map.get("TEL")==null?"":map.get("TEL").toString());
//			cell2.setCellStyle(style4);
//			colLength2 = (short) (map.get("TEL") == null ? 0: map.get("TEL").toString().length() * 256 * 2);
//			sheetMap.put("7", colLength2>Short.parseShort(sheetMap.get("7").toString())?colLength2:Short.parseShort(sheetMap.get("7").toString()));
			n=0;
		}
		int h=1;
//		for (Map<String, Object> hashMap : list) {
//			if(!"0".equals(hashMap.get("COUNT").toString())){
//				Integer count = Integer.parseInt(hashMap.get("COUNT").toString());
//				sheet.addMergedRegion(new Region(h, (short) 0, h-1+count, (short) 0));
//				h+=count;
//			}
//		}
		for(Object o : sheetMap.keySet()){
			if(Short.parseShort(sheetMap.get(o.toString()).toString())>0){
				sheet.setColumnWidth(Short.parseShort(o.toString())-1, Short.parseShort(sheetMap.get(o.toString()).toString()));
			}
		}
	/*	sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(2,8000);
		sheet.setColumnWidth(2,8000);
		sheet.setColumnWidth(2,8000);*/
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("签到明细表.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		
	}
	
}
