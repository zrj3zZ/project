package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.dao.ShanXiZqbBgProjectManageDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.sdk.DemAPI;

import net.sf.json.JSONArray;


public class ShanXiZqbBgProjectManageService {
	private static Logger logger = Logger.getLogger(ShanXiZqbBgProjectManageService.class);
	private final static String CN_FILENAME = "/common.properties";
	private ShanXiZqbBgProjectManageDAO shanXiZqbBgProjectManageDAO;

	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter));
		}
		return result;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}

	public HashMap<String,List<HashMap<String, Object>>> getListMap(String jyf, String jydsf, String czbm, String cyrxm, int runPageNumber, int runPageSize, int closePageNumber, int closePageSize) {
		StringBuffer sql = new StringBuffer();
		List parameter=new ArrayList();//存放参数
		int runStartRow = (runPageNumber - 1) * runPageSize;
		int runEndRow = runPageNumber * runPageSize;
		int closeStartRow = (closePageNumber - 1) * closePageSize;
		int closeEndRow = closePageNumber * closePageSize;
		sql.append("SELECT * FROM (");
		sql.append("SELECT RUNDATA.* FROM (SELECT DATA.*,ROWNUM RNUM FROM (SELECT BG.ID,BG.INSTANCEID,BG.JYF,BG.JYDSF,BG.SGFS,BG.MANAGER,BG.OWNER,CASE WHEN XMINFO.JDMC IS NULL THEN '' ELSE XMINFO.JDMC||'【'||JDDT.SPZT||'】' END DT,BG.XMZT,NVL(COUNTDATA.CNUM,0) CNUM FROM BD_ZQB_BGZZLXXX BG ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT BG.XMBH FROM BD_ZQB_BGZZLXXX BG"
					+ " LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_BGZZLXXX')) BINDDATA ON BG.ID=BINDDATA.DATAID"
					+ " LEFT JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE 1=1 AND (CYDATA.NAME like ? OR BG.OWNER LIKE ? OR BG.MANAGER LIKE ?)"
					+ " ) BGDATA ON BG.XMBH=BGDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
		}
		
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGFAZLBS UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGSBZL UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGZLGD "
				+ " ) JDDT ON BG.XMBH=JDDT.XMBH AND BG.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		
		//获得正在执行与关闭项目的个数
		sql.append(" LEFT JOIN (SELECT XMZT,COUNT(1) CNUM FROM BD_ZQB_BGZZLXXX BG ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT BG.XMBH FROM BD_ZQB_BGZZLXXX BG"
					+ " LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_BGZZLXXX')) BINDDATA ON BG.ID=BINDDATA.DATAID"
					+ " LEFT JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE 1=1 AND (CYDATA.NAME like ? OR BG.OWNER LIKE ? OR BG.MANAGER LIKE ?)"
					+ " ) BGDATA ON BG.XMBH=BGDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
		}
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGFAZLBS UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGSBZL UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGZLGD "
				+ " ) JDDT ON BG.XMBH=JDDT.XMBH AND BG.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		sql.append(" WHERE 1=1 ");
		if(jyf!=null&&!jyf.equals("")){
			sql.append(" AND UPPER(BG.JYF) like ?");
			parameter.add("%"+jyf+"%");
		}
		if(jydsf!=null&&!jydsf.equals("")){
			sql.append(" AND UPPER(BG.JYDSF) like ?");
			parameter.add("%"+jydsf+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(BG.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" GROUP BY XMZT) COUNTDATA ON BG.XMZT=COUNTDATA.XMZT");
		
		sql.append(" WHERE BG.XMZT=1 ");
		if(jyf!=null&&!jyf.equals("")){
			sql.append(" AND UPPER(BG.JYF) like ?");
			parameter.add("%"+jyf+"%");
		}
		if(jydsf!=null&&!jydsf.equals("")){
			sql.append(" AND UPPER(BG.JYDSF) like ?");
			parameter.add("%"+jydsf+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(BG.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" ORDER BY BG.ID) DATA ) RUNDATA WHERE RUNDATA.RNUM > ? AND RUNDATA.RNUM <= ?");
		parameter.add(runStartRow);
		parameter.add(runEndRow);
		sql.append(" UNION ");
		sql.append(" SELECT CLOSEDATA.* FROM (SELECT DATA.*,ROWNUM RNUM FROM (SELECT BG.ID,BG.INSTANCEID,BG.JYF,BG.JYDSF,BG.SGFS,BG.MANAGER,BG.OWNER,CASE WHEN XMINFO.JDMC IS NULL THEN '' ELSE XMINFO.JDMC||'【'||JDDT.SPZT||'】' END DT,BG.XMZT,NVL(COUNTDATA.CNUM,0) CNUM FROM BD_ZQB_BGZZLXXX BG ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT BG.XMBH FROM BD_ZQB_BGZZLXXX BG"
					+ " LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_BGZZLXXX')) BINDDATA ON BG.ID=BINDDATA.DATAID"
					+ " LEFT JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE 1=1 AND (CYDATA.NAME like ? OR BG.OWNER LIKE ? OR BG.MANAGER LIKE ?)"
					+ " ) BGDATA ON BG.XMBH=BGDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
		}
		
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGFAZLBS UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGSBZL UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGZLGD "
				+ " ) JDDT ON BG.XMBH=JDDT.XMBH AND BG.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		
		//获得正在执行与关闭项目的个数
		sql.append(" LEFT JOIN (SELECT XMZT,COUNT(1) CNUM FROM BD_ZQB_BGZZLXXX BG ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT BG.XMBH FROM BD_ZQB_BGZZLXXX BG"
					+ " LEFT JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_BGZZLXXX')) BINDDATA ON BG.ID=BINDDATA.DATAID"
					+ " LEFT JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE 1=1 AND (CYDATA.NAME like ? OR BG.OWNER LIKE ? OR BG.MANAGER LIKE ?)"
					+ " ) BGDATA ON BG.XMBH=BGDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
			parameter.add("%"+cyrxm+"%");
		}
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGFAZLBS UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGSBZL UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_BGZLGD "
				+ " ) JDDT ON BG.XMBH=JDDT.XMBH AND BG.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		sql.append(" WHERE 1=1 ");
		if(jyf!=null&&!jyf.equals("")){
			sql.append(" AND UPPER(BG.JYF) like ?");
			parameter.add("%"+jyf+"%");
		}
		if(jydsf!=null&&!jydsf.equals("")){
			sql.append(" AND UPPER(BG.JYDSF) like ?");
			parameter.add("%"+jydsf+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(BG.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" GROUP BY XMZT) COUNTDATA ON BG.XMZT=COUNTDATA.XMZT");
		
		sql.append(" WHERE BG.XMZT=0 ");
		if(jyf!=null&&!jyf.equals("")){
			sql.append(" AND UPPER(BG.JYF) like ?");
			parameter.add("%"+jyf+"%");
		}
		if(jydsf!=null&&!jydsf.equals("")){
			sql.append(" AND UPPER(BG.JYDSF) like ?");
			parameter.add("%"+jydsf+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(BG.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" ORDER BY BG.ID) DATA ) CLOSEDATA WHERE CLOSEDATA.RNUM > ? AND CLOSEDATA.RNUM <= ?) ORDER BY XMZT DESC,ID");
		parameter.add(closeStartRow);
		parameter.add(closeEndRow);
		HashMap<String,List<HashMap<String, Object>>> listMap = shanXiZqbBgProjectManageDAO.getListMap(sql.toString(),parameter);
		return listMap;
	}
	

	public List<HashMap<String, Object>> getTaskList(String xmbh) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.LXTBR,JDDATA.LCBH,NVL(JDDATA.LCBS,0) LCBS,NVL(JDDATA.TASKID,0) TASKID,JDDATA.LXFJ,JDDATA.SPZT FROM (SELECT ID,SORTID,JDMC FROM BD_ZQB_TYXM_INFO WHERE XMLX='并购项目') TYXM"
				+ " LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME LXTBR,XMLX.XMBH,XMLX.LCBH,XMLX.LCBS,XMLX.TASKID,XMLX.SPZT,XMLX.JDBH,LXFILE.CONTENT LXFJ FROM BD_ZQB_BGXMLX XMLX"
				+ " INNER JOIN ORGUSER ORG ON XMLX.TBRID=ORG.USERID"
				+ " LEFT JOIN (SELECT ID,SUBSTR(REPLACE(MAX(CONTENT),'$,','</br>'), 0, LENGTH(REPLACE(MAX(CONTENT),'$,','</br>'))-1) AS CONTENT FROM (SELECT ID,FILE_SRC_NAME,to_char(WM_CONCAT('<a href=\"uploadifyDownload.action\\?fileUUID='||file_id||'\">'||file_src_name||'</a>$') OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC)) CONTENT,ROW_NUMBER() OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC) KEY FROM (SELECT XMLX.ID,UPFILE.FILE_SRC_NAME,UPFILE.FILE_ID,UPFILE.UPLOAD_TIME FROM (SELECT ID,REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) FJ FROM (select * from BD_ZQB_BGXMLX where xmbh=?),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) IS NOT NULL) XMLX INNER JOIN SYS_UPLOAD_FILE UPFILE ON XMLX.FJ=UPFILE.FILE_ID)) GROUP BY ID) LXFILE ON XMLX.ID=LXFILE.ID"
				+ " WHERE XMBH=? ");
		
		sql.append(" UNION SELECT ORG.USERNAME LXTBR,FAZLBS.XMBH,FAZLBS.LCBH,FAZLBS.LCBS,FAZLBS.TASKID,FAZLBS.SPZT,FAZLBS.JDBH,FAZLBSFILE.CONTENT LXFJ FROM BD_ZQB_BGFAZLBS FAZLBS"
				+ " INNER JOIN ORGUSER ORG ON FAZLBS.TBRID=ORG.USERID"
				+ " LEFT JOIN (SELECT ID,SUBSTR(REPLACE(MAX(CONTENT),'$,','</br>'), 0, LENGTH(REPLACE(MAX(CONTENT),'$,','</br>'))-1) AS CONTENT FROM (SELECT ID,FILE_SRC_NAME,to_char(WM_CONCAT('<a href=\"uploadifyDownload.action\\?fileUUID='||file_id||'\">'||file_src_name||'</a>$') OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC)) CONTENT,ROW_NUMBER() OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC) KEY FROM (SELECT FAZLBS.ID,UPFILE.FILE_SRC_NAME,UPFILE.FILE_ID,UPFILE.UPLOAD_TIME FROM (SELECT ID,REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) FJ FROM (select * from BD_ZQB_BGFAZLBS where xmbh=?),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) IS NOT NULL) FAZLBS INNER JOIN SYS_UPLOAD_FILE UPFILE ON FAZLBS.FJ=UPFILE.FILE_ID)) GROUP BY ID) FAZLBSFILE ON FAZLBS.ID=FAZLBSFILE.ID"
				+ " WHERE XMBH=? ");
		
		sql.append(" UNION SELECT ORG.USERNAME LXTBR,SBZL.XMBH,SBZL.LCBH,SBZL.LCBS,SBZL.TASKID,SBZL.SPZT,SBZL.JDBH,SBZLFILE.CONTENT LXFJ FROM BD_ZQB_BGSBZL SBZL"
				+ " INNER JOIN ORGUSER ORG ON SBZL.TBRID=ORG.USERID"
				+ " LEFT JOIN (SELECT ID,SUBSTR(REPLACE(MAX(CONTENT),'$,','</br>'), 0, LENGTH(REPLACE(MAX(CONTENT),'$,','</br>'))-1) AS CONTENT FROM (SELECT ID,FILE_SRC_NAME,to_char(WM_CONCAT('<a href=\"uploadifyDownload.action\\?fileUUID='||file_id||'\">'||file_src_name||'</a>$') OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC)) CONTENT,ROW_NUMBER() OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC) KEY FROM (SELECT SBZL.ID,UPFILE.FILE_SRC_NAME,UPFILE.FILE_ID,UPFILE.UPLOAD_TIME FROM (SELECT ID,REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) FJ FROM (select * from BD_ZQB_BGSBZL where xmbh=?),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) IS NOT NULL) SBZL INNER JOIN SYS_UPLOAD_FILE UPFILE ON SBZL.FJ=UPFILE.FILE_ID)) GROUP BY ID) SBZLFILE ON SBZL.ID=SBZLFILE.ID"
				+ " WHERE XMBH=? ");
		
		sql.append(" UNION SELECT ORG.USERNAME LXTBR,ZLGD.XMBH,ZLGD.LCBH,ZLGD.LCBS,ZLGD.TASKID,ZLGD.SPZT,ZLGD.JDBH,ZLGDFILE.CONTENT LXFJ FROM BD_ZQB_BGZLGD ZLGD"
				+ " INNER JOIN ORGUSER ORG ON ZLGD.TBRID=ORG.USERID"
				+ " LEFT JOIN (SELECT ID,SUBSTR(REPLACE(MAX(CONTENT),'$,','</br>'), 0, LENGTH(REPLACE(MAX(CONTENT),'$,','</br>'))-1) AS CONTENT FROM (SELECT ID,FILE_SRC_NAME,to_char(WM_CONCAT('<a href=\"uploadifyDownload.action\\?fileUUID='||file_id||'\">'||file_src_name||'</a>$') OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC)) CONTENT,ROW_NUMBER() OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC) KEY FROM (SELECT ZLGD.ID,UPFILE.FILE_SRC_NAME,UPFILE.FILE_ID,UPFILE.UPLOAD_TIME FROM (SELECT ID,REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) FJ FROM (select * from BD_ZQB_BGZLGD where xmbh=?),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) IS NOT NULL) ZLGD INNER JOIN SYS_UPLOAD_FILE UPFILE ON ZLGD.FJ=UPFILE.FILE_ID)) GROUP BY ID) ZLGDFILE ON ZLGD.ID=ZLGDFILE.ID"
				+ " WHERE XMBH=? ");
		
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH ");
		sql.append("ORDER BY TYXM.SORTID");
		List<HashMap<String, Object>> list = shanXiZqbBgProjectManageDAO.getTaskList(sql.toString(),xmbh);
		return list;
	}

	public void setShanXiZqbBgProjectManageDAO(
			ShanXiZqbBgProjectManageDAO shanXiZqbBgProjectManageDAO) {
		this.shanXiZqbBgProjectManageDAO = shanXiZqbBgProjectManageDAO;
	}

	public String closeBgProject(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String bgxmxxUUID = getConfigUUID("bgxmxxuuid");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("XMZT", "0");
						dataMap.put("TBSJ", dataMap.get("TBSJ")==null?UtilDate.getNowdate():dataMap.get("TBSJ").toString().substring(0, 19));
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(bgxmxxUUID, instanceId,dataMap, dataid, false);
						String jyf = dataMap.get("JYF").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "并购项目维护", "关闭并购项目："+jyf);
						}else{
							content.append(jyf).append(",");
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
	
	public String closeCwProject(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String cwgwxxUUID = getConfigUUID("cwgwxxuuid");
			for (String insId : arr) {
				if(insId!=null&&!insId.equals("")){
					try {
						Long instanceId = Long.parseLong(insId);
						HashMap dataMap = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_DEM);
						dataMap.put("XMZT", "0");
						dataMap.put("TBSJ", dataMap.get("TBSJ")==null?UtilDate.getNowdate():dataMap.get("TBSJ").toString().substring(0, 19));
						Long dataid = (Long) dataMap.get("ID");
						boolean updateFormData = DemAPI.getInstance().updateFormData(cwgwxxUUID, instanceId,dataMap, dataid, false);
						String customername = dataMap.get("CUSTOMERNAME").toString();
						if(updateFormData){
							LogUtil.getInstance().addLog(dataid, "一般性财务顾问项目维护", "关闭一般性财务顾问项目："+customername);
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

	public List<HashMap<String, Object>> getCwTaskList(String xmbh) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.LXTBR,JDDATA.LCBH,NVL(JDDATA.LCBS,0) LCBS,NVL(JDDATA.TASKID,0) TASKID,JDDATA.LXFJ,JDDATA.SPZT FROM (SELECT ID,SORTID,JDMC FROM BD_ZQB_TYXM_INFO WHERE XMLX='一般性财务顾问项目') TYXM"
				+ " LEFT JOIN (");
		sql.append(" SELECT ORG.USERNAME LXTBR,XMLX.XMBH,XMLX.LCBH,XMLX.LCBS,XMLX.TASKID,XMLX.SPZT,XMLX.JDBH,LXFILE.CONTENT LXFJ FROM BD_ZQB_CWXMLX XMLX"
				+ " INNER JOIN ORGUSER ORG ON XMLX.TBRID=ORG.USERID"
				+ " LEFT JOIN (SELECT ID,SUBSTR(REPLACE(MAX(CONTENT),'$,','</br>'), 0, LENGTH(REPLACE(MAX(CONTENT),'$,','</br>'))-1) AS CONTENT FROM (SELECT ID,FILE_SRC_NAME,to_char(WM_CONCAT('<a href=\"uploadifyDownload.action\\?fileUUID='||file_id||'\">'||file_src_name||'</a>$') OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC)) CONTENT,ROW_NUMBER() OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC) KEY FROM (SELECT XMLX.ID,UPFILE.FILE_SRC_NAME,UPFILE.FILE_ID,UPFILE.UPLOAD_TIME FROM (SELECT ID,REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) FJ FROM (select * from BD_ZQB_CWXMLX where xmbh=?),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) IS NOT NULL) XMLX INNER JOIN SYS_UPLOAD_FILE UPFILE ON XMLX.FJ=UPFILE.FILE_ID)) GROUP BY ID) LXFILE ON XMLX.ID=LXFILE.ID"
				+ " WHERE XMBH=? ");
		
		sql.append(" UNION SELECT ORG.USERNAME LXTBR,GZJDHB.XMBH,GZJDHB.LCBH,GZJDHB.LCBS,GZJDHB.TASKID,GZJDHB.SPZT,GZJDHB.JDBH,GZJDHBFILE.CONTENT LXFJ FROM BD_ZQB_CWGZJDHB GZJDHB"
				+ " INNER JOIN ORGUSER ORG ON GZJDHB.TBRID=ORG.USERID"
				+ " LEFT JOIN (SELECT ID,SUBSTR(REPLACE(MAX(CONTENT),'$,','</br>'), 0, LENGTH(REPLACE(MAX(CONTENT),'$,','</br>'))-1) AS CONTENT FROM (SELECT ID,FILE_SRC_NAME,to_char(WM_CONCAT('<a href=\"uploadifyDownload.action\\?fileUUID='||file_id||'\">'||file_src_name||'</a>$') OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC)) CONTENT,ROW_NUMBER() OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC) KEY FROM (SELECT GZJDHB.ID,UPFILE.FILE_SRC_NAME,UPFILE.FILE_ID,UPFILE.UPLOAD_TIME FROM (SELECT ID,REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) FJ FROM (select * from BD_ZQB_CWGZJDHB where xmbh=?),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) IS NOT NULL) GZJDHB INNER JOIN SYS_UPLOAD_FILE UPFILE ON GZJDHB.FJ=UPFILE.FILE_ID)) GROUP BY ID) GZJDHBFILE ON GZJDHB.ID=GZJDHBFILE.ID"
				+ " WHERE XMBH=? ");
		
		sql.append(" UNION SELECT ORG.USERNAME LXTBR,ZLGD.XMBH,ZLGD.LCBH,ZLGD.LCBS,ZLGD.TASKID,ZLGD.SPZT,ZLGD.JDBH,ZLGDFILE.CONTENT LXFJ FROM BD_ZQB_CWZLGD ZLGD"
				+ " INNER JOIN ORGUSER ORG ON ZLGD.TBRID=ORG.USERID"
				+ " LEFT JOIN (SELECT ID,SUBSTR(REPLACE(MAX(CONTENT),'$,','</br>'), 0, LENGTH(REPLACE(MAX(CONTENT),'$,','</br>'))-1) AS CONTENT FROM (SELECT ID,FILE_SRC_NAME,to_char(WM_CONCAT('<a href=\"uploadifyDownload.action\\?fileUUID='||file_id||'\">'||file_src_name||'</a>$') OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC)) CONTENT,ROW_NUMBER() OVER(PARTITION BY ID ORDER BY UPLOAD_TIME ASC) KEY FROM (SELECT ZLGD.ID,UPFILE.FILE_SRC_NAME,UPFILE.FILE_ID,UPFILE.UPLOAD_TIME FROM (SELECT ID,REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) FJ FROM (select * from BD_ZQB_CWZLGD where xmbh=?),(SELECT ROWNUM RN FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(FJ, '[^,]+', 1, RN) IS NOT NULL) ZLGD INNER JOIN SYS_UPLOAD_FILE UPFILE ON ZLGD.FJ=UPFILE.FILE_ID)) GROUP BY ID) ZLGDFILE ON ZLGD.ID=ZLGDFILE.ID"
				+ " WHERE XMBH=? ");
		
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH ");
		sql.append("ORDER BY TYXM.SORTID");
		List<HashMap<String, Object>> list = shanXiZqbBgProjectManageDAO.getCwTaskList(sql.toString(),xmbh);
		return list;
	}
	
	public List<HashMap> getQtRunList(String customername,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize){
		return shanXiZqbBgProjectManageDAO.getQtRunList(customername,clbm,czbm,cyrxm,runPageNumber,runPageSize);
	}
	
	public int getQtRunListSize(String customername,String clbm,String czbm,String cyrxm){
		return shanXiZqbBgProjectManageDAO.getQtRunListSize(customername,clbm,czbm,cyrxm).size();
	}
	
	public List<HashMap> getQtCloseList(String customername,String clbm,String czbm,String cyrxm,int runPageNumber,int runPageSize){
		return shanXiZqbBgProjectManageDAO.getQtCloseList(customername,clbm,czbm,cyrxm,runPageNumber,runPageSize);
	}
	
	public int getQtCloseListSize(String customername,String clbm,String czbm,String cyrxm){
		return shanXiZqbBgProjectManageDAO.getQtCloseListSize(customername,clbm,czbm,cyrxm).size();
	}
	
	public String closeQtProject(String instanceIdStr) {
		String info = "";
		if(instanceIdStr!=null&&!instanceIdStr.equals("")){
			StringBuffer content = new StringBuffer();
			String[] arr = instanceIdStr.split(",");
			String qtxmglUUID = com.iwork.core.db.DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='其他项目'", "UUID");
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
							LogUtil.getInstance().addLog(dataid, "其他项目维护", "关闭其他项目："+customername);
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
	
	public List<HashMap<String, Object>> getQtTaskList(String xmbh) {
		List<HashMap<String, Object>> list = shanXiZqbBgProjectManageDAO.getQtTaskList(xmbh);
		return list;
	}
	
	public String getMainContent(String xmbh){
		List<String> lables = new ArrayList<String>();
		lables.add("CUSTOMERNAME");
		lables.add("STARTDATE");
		lables.add("MANAGER");
		lables.add("OWNER");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM BD_ZQB_TYXM WHERE PROJECTNO=?");
		Map params = new HashMap();
		params.put(1, xmbh);
		List<HashMap> content = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		HashMap<String,String> result = new HashMap<String,String>();
		if(content.size()==1){
			HashMap data = content.get(0);
			String cutomername = data.get("CUSTOMERNAME")==null?"":data.get("CUSTOMERNAME").toString();
			
			String startdate = data.get("STARTDATE")==null?"":data.get("STARTDATE").toString();
			String manager = data.get("MANAGER")==null?"":data.get("MANAGER").toString();
			String owner = data.get("OWNER")==null?"":data.get("OWNER").toString();
			result.put("CUSTOMERNAME", cutomername);
			result.put("STARTDATE", startdate);
			
			result.put("MANAGER", manager);
			result.put("OWNER", owner);
		}else{
			
			result.put("CUSTOMERNAME", "");
			result.put("STARTDATE", "");
			result.put("MANAGER", "");
			result.put("OWNER", "");
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public HashMap<String, List<HashMap<String, Object>>> getCwListMap( String customername, String clbm, String czbm, String cyrxm,
			int runPageNumber, int runPageSize, int closePageNumber, int closePageSize) {
		StringBuffer sql = new StringBuffer();
		List parameter=new ArrayList();//存放参数
		int runStartRow = (runPageNumber - 1) * runPageSize;
		int runEndRow = runPageNumber * runPageSize;
		int closeStartRow = (closePageNumber - 1) * closePageSize;
		int closeEndRow = closePageNumber * closePageSize;
		sql.append("SELECT * FROM (");
		sql.append("SELECT RUNDATA.* FROM (SELECT DATA.*,ROWNUM RNUM FROM (SELECT CWGWXX.ID,CWGWXX.INSTANCEID,CWGWXX.CUSTOMERNAME,CWGWXX.MANAGER,CWGWXX.OWNER,CASE WHEN XMINFO.JDMC IS NULL THEN '' ELSE XMINFO.JDMC||'【'||JDDT.SPZT||'】' END DT,CWGWXX.XMZT,NVL(COUNTDATA.CNUM,0) CNUM FROM BD_ZQB_CWGWXMXX CWGWXX ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT CWGWXX.XMBH FROM BD_ZQB_CWGWXMXX CWGWXX"
					+ " INNER JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_CWGWXMXX')) BINDDATA ON CWGWXX.ID=BINDDATA.DATAID"
					+ " INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE CYDATA.NAME like ?"
					+ " ) CWDATA ON CWGWXX.XMBH=CWDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
		}
		
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWGZJDHB UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWZLGD "
				+ " ) JDDT ON CWGWXX.XMBH=JDDT.XMBH AND CWGWXX.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		
		//获得正在执行与关闭项目的个数
		sql.append(" LEFT JOIN (SELECT XMZT,COUNT(1) CNUM FROM BD_ZQB_CWGWXMXX CWGWXX ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT CWGWXX.XMBH FROM BD_ZQB_CWGWXMXX CWGWXX"
					+ " INNER JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_CWGWXMXX')) BINDDATA ON CWGWXX.ID=BINDDATA.DATAID"
					+ " INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE CYDATA.NAME like ?"
					+ " ) CWDATA ON CWGWXX.XMBH=CWDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
		}
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWGZJDHB UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWZLGD "
				+ " ) JDDT ON CWGWXX.XMBH=JDDT.XMBH AND CWGWXX.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		sql.append(" WHERE 1=1 ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND UPPER(CWGWXX.CUSTOMERNAME) like ?");
			parameter.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CLBM) like ?");
			parameter.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" GROUP BY XMZT) COUNTDATA ON CWGWXX.XMZT=COUNTDATA.XMZT");
		
		sql.append(" WHERE CWGWXX.XMZT=1 ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND UPPER(CWGWXX.CUSTOMERNAME) like ?");
			parameter.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CLBM) like ?");
			parameter.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" ORDER BY CWGWXX.ID) DATA ) RUNDATA WHERE RUNDATA.RNUM > ? AND RUNDATA.RNUM <= ?");
		parameter.add(runStartRow);
		parameter.add(runEndRow);
		sql.append(" UNION ");
		sql.append(" SELECT CLOSEDATA.* FROM (SELECT DATA.*,ROWNUM RNUM FROM (SELECT CWGWXX.ID,CWGWXX.INSTANCEID,CWGWXX.CUSTOMERNAME,CWGWXX.MANAGER,CWGWXX.OWNER,CASE WHEN XMINFO.JDMC IS NULL THEN '' ELSE XMINFO.JDMC||'【'||JDDT.SPZT||'】' END DT,CWGWXX.XMZT,NVL(COUNTDATA.CNUM,0) CNUM FROM BD_ZQB_CWGWXMXX CWGWXX ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT CWGWXX.XMBH FROM BD_ZQB_CWGWXMXX CWGWXX"
					+ " INNER JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_CWGWXMXX')) BINDDATA ON CWGWXX.ID=BINDDATA.DATAID"
					+ " INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE CYDATA.NAME like ?"
					+ " ) CWDATA ON CWGWXX.XMBH=CWDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
		}
		
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWGZJDHB UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWZLGD "
				+ " ) JDDT ON CWGWXX.XMBH=JDDT.XMBH AND CWGWXX.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		
		//获得正在执行与关闭项目的个数
		sql.append(" LEFT JOIN (SELECT XMZT,COUNT(1) CNUM FROM BD_ZQB_CWGWXMXX CWGWXX ");
		if(cyrxm!=null&&!cyrxm.equals("")){
			sql.append(" INNER JOIN"
					+ " ("
					+ " SELECT DISTINCT CWGWXX.XMBH FROM BD_ZQB_CWGWXMXX CWGWXX"
					+ " INNER JOIN (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID = (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME = 'BD_ZQB_CWGWXMXX')) BINDDATA ON CWGWXX.ID=BINDDATA.DATAID"
					+ " INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A"
					+ " LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) CYDATA ON BINDDATA.INSTANCEID=CYDATA.INSTANCEID"
					+ " WHERE CYDATA.NAME like ?"
					+ " ) CWDATA ON CWGWXX.XMBH=CWDATA.XMBH");
			parameter.add("%"+cyrxm+"%");
		}
		sql.append(" LEFT JOIN (SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWXMLX UNION"
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWGZJDHB UNION "
				+ " SELECT XMBH,SPZT,JDBH FROM BD_ZQB_CWZLGD "
				+ " ) JDDT ON CWGWXX.XMBH=JDDT.XMBH AND CWGWXX.JDBH=JDDT.JDBH LEFT JOIN BD_ZQB_TYXM_INFO XMINFO ON JDDT.JDBH=XMINFO.ID");
		sql.append(" WHERE 1=1 ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND UPPER(CWGWXX.CUSTOMERNAME) like ?");
			parameter.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CLBM) like ?");
			parameter.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" GROUP BY XMZT) COUNTDATA ON CWGWXX.XMZT=COUNTDATA.XMZT");
		
		sql.append(" WHERE CWGWXX.XMZT=0 ");
		if(customername!=null&&!customername.equals("")){
			sql.append(" AND UPPER(CWGWXX.CUSTOMERNAME) like ?");
			parameter.add("%"+customername+"%");
		}
		if(clbm!=null&&!clbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CLBM) like ?");
			parameter.add("%"+clbm+"%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sql.append(" AND UPPER(CWGWXX.CZBM) like ?");
			parameter.add("%"+czbm+"%");
		}
		sql.append(" ORDER BY CWGWXX.ID) DATA ) CLOSEDATA WHERE CLOSEDATA.RNUM > ? AND CLOSEDATA.RNUM <= ?) ORDER BY XMZT DESC,ID");
		parameter.add(closeStartRow);
		parameter.add(closeEndRow);
		HashMap<String,List<HashMap<String, Object>>> listMap = shanXiZqbBgProjectManageDAO.getCwListMap(sql.toString(),parameter);
		return listMap;
	}
	//投行项目根据项目名称判断是否重复
			public boolean thxmBeforsave(String xmmc,Long instanceid){
				boolean flag =false;
				StringBuffer sql = new StringBuffer();
				sql.append("select id from  BD_XP_TXRZGLLCB b where xmmc=? and b.instanceid<>?");
				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					conn = DBUtil.open();
					ps = conn.prepareStatement(sql.toString());
					ps.setString(1, xmmc);
					ps.setLong(2, instanceid);
					rs = ps.executeQuery();
					while (rs.next()) {
						flag=true;
					}
				} catch (Exception e) {
					logger.error(e,e);
				} finally{
					DBUtil.close(conn, ps, rs);
				}
				return flag;
						
			}
		//查询投行项目
		public List<HashMap> getThxmList(String xmmc, String startdate,String enddate,String gjjd,String scbk,int pageSize, int pageNumber,String userid,String thxmlc){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date stdate=null;
			Date endate=null;
			try {
				 if(startdate!=null&&!startdate.equals(""))
					 stdate= sdf.parse(startdate);
				 if(enddate!=null&&!enddate.equals(""))
					 endate=  sdf.parse(enddate);
			} catch (ParseException e) {
				logger.error(e,e);
			}
				return shanXiZqbBgProjectManageDAO.getThxmList(xmmc, stdate, endate, gjjd, scbk, pageSize, pageNumber,userid,thxmlc);
			}
		//查询投行项目数量
		public int getThxmListSize(String xmmc, String startdate,String enddate,String gjjd,String scbk,String thxmlc){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date stdate=null;
			Date endate=null;
			try {
				 if(startdate!=null&&!startdate.equals(""))
					 stdate= sdf.parse(startdate);
				 if(enddate!=null&&!enddate.equals(""))
					 endate=  sdf.parse(enddate);
			} catch (ParseException e) {
				logger.error(e,e);
			}
			return shanXiZqbBgProjectManageDAO.getThxmListSizels(xmmc, stdate, endate, gjjd, scbk,thxmlc).size();
		}
		//查询投行项目数量
		public HashMap<String,Object> getMap(String xmmc){
			return shanXiZqbBgProjectManageDAO.getMap(xmmc);
		}
		//投行项目导出excl
		public void thxmexportexcl(HttpServletResponse response,String xmmc, String startdate,String enddate,String gjjd,String scbk,String thxmlc){
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet("融资需求项目登记汇总表");
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row0 = sheet.createRow((int) 0);
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,13)); 
			row0.setHeightInPoints(35);
			HSSFCell yearCell=row0.createCell(0);
			yearCell.setCellValue("融资需求项目登记汇总表");
		
			HSSFRow row = sheet.createRow((int) 1);
			row.setHeightInPoints(30);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//	style.setFillForegroundColor((short) 22);
		/*	style.setFillPattern((short) 1);
			style.setBorderBottom((short) 1);
			style.setBottomBorderColor((short) 8);
			style.setBorderLeft((short) 1);
			style.setLeftBorderColor((short) 8);
			style.setBorderRight((short) 1);
			style.setRightBorderColor((short) 8);
			style.setBorderTop((short) 1);
			style.setTopBorderColor((short) 8);*/
			style.setBorderBottom((short) 1);
			style.setBorderLeft((short) 1);
			style.setBorderRight((short) 1);
			style.setBorderTop((short) 1);
			style.setWrapText(true);
			HSSFCellStyle style1 = wb.createCellStyle();
			style1.setBorderBottom((short) 1);
			style1.setBorderLeft((short) 1);
			style1.setBorderRight((short) 1);
			style1.setBorderTop((short) 1);
			style1.setWrapText(true);
			style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setBorderBottom((short) 1);
			style2.setBorderLeft((short) 1);
			style2.setBorderRight((short) 1);
			style2.setBorderTop((short) 1);
			style2.setWrapText(true);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			
			HSSFCellStyle style3 = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style3.setFont(font);
			HSSFFont font2 = wb.createFont();
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style.setFont(font2);
			yearCell.setCellStyle(style3);
			HSSFCell cell = row.createCell((short) 0);
			
			cell.setCellValue("流程发起日期");
			cell.setCellStyle(style);
			
			/*cell = row.createCell((short) 2);
			cell.setCellValue("所属部门");
			cell.setCellStyle(style);*/
			cell = row.createCell((short) 1);
			cell.setCellValue("资本市场部经办人");
			cell.setCellStyle(style);
			
			cell = row.createCell((short) 2);
			cell.setCellValue("部门");
			cell.setCellStyle(style);
			
			cell = row.createCell((short) 3);
			cell.setCellValue("项目名称");
			cell.setCellStyle(style);
			cell = row.createCell((short) 4);
			cell.setCellValue("跟进阶段");
			cell.setCellStyle(style);
			cell = row.createCell((short) 5);
			cell.setCellValue("所处版块");
			cell.setCellStyle(style);
			cell = row.createCell((short) 6);
			cell.setCellValue("融资需求描述");
			cell.setCellStyle(style);
			cell = row.createCell((short) 7);
			cell.setCellValue("项目综合评价");
			cell.setCellStyle(style);
			/*cell = row.createCell((short) 9);
			cell.setCellValue("项目跟进意见");
			cell.setCellStyle(style);*/
			
			cell = row.createCell((short) 8);
			cell.setCellValue("项目状态");
			cell.setCellStyle(style);
		
			cell = row.createCell((short) 9);
			cell.setCellValue("流程意见");
			cell.setCellStyle(style);
			cell = row.createCell((short) 10);
			cell.setCellValue("流程意见");
			cell.setCellStyle(style);
			cell = row.createCell((short) 11);
			cell.setCellValue("流程意见");
			cell.setCellStyle(style);
			cell = row.createCell((short) 12);
			cell.setCellValue("流程意见");
			cell.setCellStyle(style);
			cell = row.createCell((short) 13);
			cell.setCellValue("流程意见");
			cell.setCellStyle(style);
			
			HSSFRow row1 = sheet.createRow(2);
			HSSFCell c1 = row1.createCell(9);
			c1.setCellValue(new HSSFRichTextString("任务名称"));
			c1.setCellStyle(style2);
			HSSFCell c2 = row1.createCell(10);
			c2.setCellValue(new HSSFRichTextString("操作"));
			c2.setCellStyle(style2);
			HSSFCell c3 = row1.createCell(11);
			c3.setCellValue(new HSSFRichTextString("意见描述"));
			c3.setCellStyle(style2);
			HSSFCell c4 = row1.createCell(12);
			c4.setCellValue(new HSSFRichTextString("办理人"));
			c4.setCellStyle(style2);
			HSSFCell c5 = row1.createCell(13);
			c5.setCellValue(new HSSFRichTextString("办理时间"));
			c5.setCellStyle(style2);
			
			Region region1 = new Region(1, (short)0, 2, (short)0);
	        Region region2 = new Region(1, (short)1, 2, (short)1);
	        Region region3 = new Region(1, (short)2, 2, (short)2);
	        Region region4 = new Region(1, (short)3, 2, (short)3);
	        Region region5 = new Region(1, (short)4, 2, (short)4);
	        Region region6 = new Region(1, (short)5, 2, (short)5);
	        Region region7 = new Region(1, (short)6, 2, (short)6);
	        Region region8 = new Region(1, (short)7, 2, (short)7);
	        Region region9 = new Region(1, (short)8, 2, (short)8);
	        Region region12 = new Region(1, (short)9, 1, (short)13);
	        sheet.addMergedRegion(region1);
	        sheet.addMergedRegion(region2);
	        sheet.addMergedRegion(region3);
	        sheet.addMergedRegion(region4);
	        sheet.addMergedRegion(region5);
	        sheet.addMergedRegion(region6);
	        sheet.addMergedRegion(region7);
	        sheet.addMergedRegion(region8);
	        sheet.addMergedRegion(region9);
	        sheet.addMergedRegion(region12);
			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
			//获取当前用户权限
			List list = shanXiZqbBgProjectManageDAO.getThxmListSize(xmmc, startdate, enddate, gjjd, scbk ,thxmlc);
			int n = 3;
			if (list == null) {
				return;
			}
			int m = 0;
			Map person = new HashMap();
			for (int i = 0; i < list.size(); i++) {

					Map map = (HashMap) list.get(i);
					row = sheet.createRow((int) n++);
					// 第四步，创建单元格，并设置值
					
					
					
					
					HSSFCell cell2 = row.createCell((short) 0);
					cell2.setCellValue(map.get("fqrq").toString());
					cell2.setCellStyle(style1);
					
					// 如何记录已显示人员的map里没有记录，或者不等于当前的用户
					if (person.get("id") == null|| !person.get("id").toString().equals((map.get("id") != null ? map.get("id").toString() : ""))) {
						// 单元格合并
						// 四个参数分别是：起始行，起始列，结束行，结束列
						if (person.get("id") != null) {
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 0, n - 2,(short) 0));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 1, n - 2,(short) 1));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 2, n - 2,(short) 2));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 3, n - 2,(short) 3));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 4, n - 2,(short) 4));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 5, n - 2,(short) 5));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 6, n - 2,(short) 6));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 7, n - 2,(short) 7));
							sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin").toString()), (short) 8, n - 2,(short) 8));
						}
						person.put("id",	(map.get("id") != null ? map.get("id").toString() : "")	);
						person.put("begin", n - 1);
						// 再把式样设置到cell中：
					}
					
					HSSFCell cell10 = row.createCell((short) 1);
					cell10.setCellValue(map.get("zbscbjbr").toString()==""?"暂无":map.get("zbscbjbr").toString());
					cell10.setCellStyle(style1);
					
					HSSFCell cell9 = row.createCell((short) 2);
					cell9.setCellValue(map.get("bm")==null?"":map.get("bm").toString());
					cell9.setCellStyle(style1);
					
					HSSFCell cell11 = row.createCell((short) 3);
					cell11.setCellValue(map.get("xmmc").toString());
					cell11.setCellStyle(style1);
					
					HSSFCell cell3 = row.createCell((short) 4);
					cell3.setCellValue(map.get("gjjd").toString());
					cell3.setCellStyle(style1);
					HSSFCell cell4 = row.createCell((short) 5);
					cell4.setCellValue(map.get("scbk")==null?"":map.get("scbk").toString());
					cell4.setCellStyle(style1);
					HSSFCell cell5 = row.createCell((short) 6);
					cell5.setCellValue(map.get("rzxqms")==null?"":map.get("rzxqms").toString());
					cell5.setCellStyle(style1);
					HSSFCell cell6 = row.createCell((short) 7);
					cell6.setCellValue(map.get("smzhpj")==null?"":map.get("smzhpj").toString());
					cell6.setCellStyle(style1);
					HSSFCell cell7 = row.createCell((short) 8);
					cell7.setCellValue(map.get("smzt")==null?"":map.get("smzt").toString());
					cell7.setCellStyle(style1);
					HSSFCell cell8 = row.createCell((short) 9);
					cell8.setCellValue(map.get("title")==null?"":map.get("title").toString());
					cell8.setCellStyle(style1);
					HSSFCell cell13 = row.createCell((short) 10);
					cell13.setCellValue(map.get("action")==""?"待处理":map.get("action").toString());
					cell13.setCellStyle(style1);
					String cnt=map.get("content")==null?"":map.get("content").toString();
					if(!"".equals(map.get("filename"))){
						if(!"".equals(cnt))
							cnt= cnt+"\n"+map.get("filename");
						else cnt= map.get("filename").toString();
					}
					HSSFCell cell14 = row.createCell((short) 11);
					cell14.setCellValue(cnt);
					cell14.setCellStyle(style1);
					HSSFCell cell15 = row.createCell((short) 12);
					cell15.setCellValue(map.get("username")==null?"":map.get("username").toString());
					cell15.setCellStyle(style1);
					HSSFCell cell16 = row.createCell((short) 13);
					cell16.setCellValue(map.get("blsj")==null?"":map.get("blsj").toString());
					cell16.setCellStyle(style1);
					m++;

			}
			if(list!=null  && list.size()>0){
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 0, n - 1, (short) 0));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 1, n - 1, (short) 1));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 2, n - 1, (short) 2));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 3, n - 1, (short) 3));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 4, n - 1, (short) 4));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 5, n - 1, (short) 5));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 6, n - 1, (short) 6));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 7, n - 1, (short) 7));
				sheet.addMergedRegion(new Region(Integer.parseInt(person.get("begin")==null?"0":person.get("begin").toString()), (short) 8, n - 1, (short) 8));
			}
			for (int i = 0; i < 7; i++) {
				sheet.setColumnWidth(i, 7000);
			}
			sheet.setColumnWidth(0, 3500);
			sheet.setColumnWidth(1, 4500);
			sheet.setColumnWidth(4, 5500);
			sheet.setColumnWidth(5, 4500);
			sheet.setColumnWidth(6, 9500);
			sheet.setColumnWidth(7, 9500);
			sheet.setColumnWidth(8, 2500);
			sheet.setColumnWidth(9, 4500);
			sheet.setColumnWidth(11, 4500);
			sheet.setColumnWidth(13, 4500);
			OutputStream out1 = null;
			// 第六步，将文件存到指定位置
			try {
				String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("融资需求项目登记汇总表.xls");
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-disposition", disposition);
				out1 = new BufferedOutputStream(response.getOutputStream());
				wb.write(out1);
				// out1.flush();
				// out1.close();

			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				if (out1 != null) {
					try {
						out1.flush();
						out1.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e,e);
					}
				}

			}
		}
}
