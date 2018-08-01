package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;

import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.dao.ShanXiZqbCustomerManageDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;

import org.apache.log4j.Logger;
@SuppressWarnings("deprecation")
public class ShanXiZqbCustomerManageService {
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	//private final static String XMLXUUID = "33833384d109463285a6a348813539f1";
	private static Logger logger = Logger.getLogger(ShanXiZqbCustomerManageService.class);
	private ShanXiZqbCustomerManageDAO shanXiZqbCustomerManageDAO;

	public void setShanXiZqbCustomerManageDAO(ShanXiZqbCustomerManageDAO shanXiZqbCustomerManageDAO) {
		this.shanXiZqbCustomerManageDAO = shanXiZqbCustomerManageDAO;
	}
	
	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter));
		}
		return result;
	}

	public List<HashMap<String,Object>> getCurrentCustomerList(String customername,String zqdm,String type, String zwmc,BigDecimal zczbbegin
			, BigDecimal zczbend, String gfgsrqbegin,String gfgsrqend, int pageSize, int pageNow,String dq) {
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		list = shanXiZqbCustomerManageDAO.getCurrentCustomerList(customername,zqdm,type,zwmc,zczbbegin,zczbend,gfgsrqbegin,gfgsrqend,pageSize,pageNow,dq);
		return list;
	}

	public int getCurrentCustomerListSize(String customername, String zqdm,
			String type, String zwmc, BigDecimal zczbbegin, BigDecimal zczbend,
			String gfgsrqbegin, String gfgsrqend,String dq) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) CNUM FROM BD_ZQB_KH_BASE WHERE 1=1");
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		Long orgroleid = userModel.getOrgroleid();
		// 判断角色ID是否为董秘
		if (orgroleid == 3) {
			String extend1 = userModel.getExtend1();
			sql.append(" AND CUSTOMERNO = ?");
			parameter.add(extend1);
		}
		// 客户名称
		if (customername != null && !customername.equals("")) {
			sql.append(" AND UPPER(CUSTOMERNAME) LIKE ?");
			parameter.add("%" + StringEscapeUtils.escapeSql(customername.toUpperCase().trim()) + "%");
		}
		// 证券代码
		if (zqdm != null && !zqdm.equals("")) {
			sql.append(" AND ZQDM like ?");
			parameter.add("%" + StringEscapeUtils.escapeSql(zqdm.trim()) + "%");
		}
		// 注册类型
		if (type != null && !type.equals("")) {
			sql.append(" AND TYPE like ?");
			parameter.add("%" + StringEscapeUtils.escapeSql(type.trim()) + "%");
		}
		// 住所地
		if (zwmc != null && !zwmc.equals("")) {
			sql.append(" AND ZWMC like ?");
			parameter.add("%" + StringEscapeUtils.escapeSql(zwmc.trim()) + "%");
		}
		// 注册资本
		if (zczbbegin != null && !zczbbegin.equals("")) {
			sql.append(" AND ZCZB >= ?");
			parameter.add(StringEscapeUtils.escapeSql(zczbbegin.toString().trim()));
		}
		if (zczbend != null && !zczbend.equals("")) {
			sql.append(" AND ZCZB <= ?");
			parameter.add(StringEscapeUtils.escapeSql(zczbend.toString().trim()));
		}
		// 成立日期
		if (gfgsrqbegin != null && !gfgsrqbegin.equals("")) {
			sql.append(" AND TO_CHAR(GFGSRQ,'yyyy-mm-dd') >= ?");
			parameter.add(StringEscapeUtils.escapeSql(gfgsrqbegin.trim()));
		}
		if (gfgsrqend != null && !gfgsrqend.equals("")) {
			sql.append(" AND TO_CHAR(GFGSRQ,'yyyy-mm-dd') <= ?");
			parameter.add(StringEscapeUtils.escapeSql(gfgsrqend.trim()));
		}
		//所属大区
		if (dq != null && !dq.equals("")) {
			sql.append(" AND UPPER(ZCQX) LIKE ?");
			parameter.add("%" + StringEscapeUtils.escapeSql(dq.toUpperCase().trim()) + "%");
		}
		int count = shanXiZqbCustomerManageDAO.getCurrentCustomerListSize(sql.toString(),parameter);
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String deleteCustomer(Long instanceid) {
		String info = "";
		boolean flag = false;
		try {
			HashMap<String,Object> hash = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if(hash!=null){
				String customerno = hash.get("CUSTOMERNO").toString();
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT NVL((SELECT COUNT(1) QYNUM FROM (SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?) KH INNER JOIN BD_ZQB_XMQY QY ON KH.CUSTOMERNO=QY.CUSTOMERNO GROUP BY KH.CUSTOMERNO),0) QYNUM,");
				sql.append(" NVL((SELECT COUNT(1) RZNUM FROM (SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?) KH INNER JOIN BD_ZQB_CWRZB RZ ON KH.CUSTOMERNO=RZ.CUSTOMERNO GROUP BY KH.CUSTOMERNO),0) RZNUM,");
				sql.append(" NVL((SELECT COUNT(1) LXNUM FROM (SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?) KH INNER JOIN BD_ZQB_PJ_BASE PJ ON KH.CUSTOMERNO=PJ.CUSTOMERNO GROUP BY KH.CUSTOMERNO),0) LXNUM,");
				sql.append(" NVL((SELECT COUNT(1) USERNUM FROM (SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?) KH INNER JOIN ORGUSER ORG ON KH.CUSTOMERNO=ORG.EXTEND1 INNER JOIN ORGDEPARTMENT DEPT ON KH.CUSTOMERNO=DEPT.DEPARTMENTNO),0) USERNUM,");
				sql.append(" NVL((SELECT COUNT(1) GPFXNUM FROM (SELECT CUSTOMERNO FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?) KH INNER JOIN BD_ZQB_GPFXXMB GPFX ON KH.CUSTOMERNO=GPFX.CUSTOMERNO),0) GPFXNUM");
				sql.append(" FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO=?");
				List<Object> params = new ArrayList<Object>();
				for (int i = 0; i < 6; i++) {
					params.add(customerno);
				}
				HashMap<String,Integer> numMap = getNumMap(sql.toString(),params);
				Integer lxnum = numMap.get("LXNUM");
				//查询该客户下是否存在项目
				if(lxnum>0){
					return "该客户已项目立项！";
				}
				Integer rznum = numMap.get("RZNUM");
				if(rznum>0){
					return "该客户已存在入账信息！";
				}
				Integer qynum = numMap.get("QYNUM");
				if(qynum>0){
					return "该客户已存在签约信息！";
				}
				Integer gpfxnum = numMap.get("GPFXNUM");
				if(gpfxnum>0){
					return "该客户已存在股票发行项目信息！";
				}
				Integer usernum = numMap.get("USERNUM");
				if(usernum>0){
					info = "该客户下已存在用户，不可删除！";
					return info;
				}else{
					String delsql = "DELETE FROM  ORGDEPARTMENT WHERE DEPARTMENTNO='" + customerno + "'";
					DBUtil.executeUpdate(delsql);
				}
				// 如果该用户为已挂牌，则需要判断是否给该挂牌公司建立了用户或者在持续督导分派下分了持续督导专员，如果已经分派了，则不进行删除，直接提示不可删除
				if (hash.get("YGP") != null && !hash.get("YGP").toString().equals("") && hash.get("YGP").toString().equals("已挂牌")) {
					// 查询是否已进行过持续督导分派
					HashMap<String,Object> conditionMap = new HashMap<String,Object>();
					conditionMap.put("KHBH", customerno);
					List<HashMap> list = DemAPI.getInstance().getList(ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
					if (list.size() > 0) {
						if (list.size() == 1) {
							if (list.get(0).get("KHFZR") != null && !list.get(0).get("KHFZR").equals("")) {
								return "已经分派过持续督导专员，无法删除！";
							} else {
								Long instanceId = Long.parseLong(list.get(0).get("INSTANCEID").toString());
								DemAPI.getInstance().removeFormData(instanceId);
							}
						} else {
							return "存在多条持续督导分派记录！";
						}
					}
				}
				Long dataId=Long.parseLong(hash.get("ID").toString());
				String customername=hash.get("CUSTOMERNAME").toString();
				LogUtil.getInstance().addLog(dataId, "客户信息维护", "删除客户："+customername);
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				Long orgroleid = uc.get_userModel().getOrgroleid();
				if (orgroleid != 5) {
					String smsContent = "";
					String sysMsgContent = "";
					if (hash != null) {
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.CUSTOMER_REMOVE_KEY,hash);
						sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.CUSTOMER_REMOVE_KEY,hash);
					}
					// 删除下发短信
					String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
					UserContext target = UserContextUtil.getInstance().getUserContext(userid);
					if (target != null) {
						if (!smsContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,smsContent);
							}
						}
						if (!sysMsgContent.equals("")) {
							MessageAPI.getInstance().sendSysMsg(userid,"客户基本信息维护提醒", sysMsgContent);
						}
					}
				}
				// 删除客户信息
				flag = DemAPI.getInstance().removeFormData(instanceid);
				if (flag) {
					info = "success";
				} else {
					info = "删除失败！";
				}
			}else{
				info = "删除失败！";
			}
		} catch (Exception e) {
			info="删除失败！";
		}
		return info;
	}
	
	public String checkCFXX(String khmc, String zqdm, String zqjc, Long instanceid) {
		String info = "";
		if(instanceid==0){
			if (khmc != null && !khmc.equals("")) {
				List<HashMap<String,Long>> list = getList("CUSTOMERNAME",khmc);
				if(!list.isEmpty()){
					info = "公司全称在系统中已存在!\n";
					return info;
				}
			}
			if (zqdm != null && !zqdm.equals("")) {
				List<HashMap<String,Long>> list = getList("ZQDM",zqdm);
				if(!list.isEmpty()){
					info = "证券代码在系统中已存在!\n";
					return info;
				}
			}
			if (zqjc != null && !zqjc.equals("")) {
				List<HashMap<String,Long>> list = getList("ZQJC",zqjc);
				if(!list.isEmpty()){
					info = "证券简称在系统中已存在!\n";
					return info;
				}
			}
		}else{
			if (khmc != null && !khmc.equals("")) {
				List<HashMap<String,Long>> list = getList("CUSTOMERNAME",khmc);
				for (HashMap<String,Long> hashMap : list) {
					String string = hashMap.get("INSTANCEID").toString();
					if(instanceid!=Long.parseLong(string)){
						info = "公司全称在系统中已存在!\n";
						return info;
					}
				}
			}
			if (zqdm != null && !zqdm.equals("")) {
				List<HashMap<String,Long>> list = getList("ZQDM",zqdm);
				for (HashMap<String,Long> hashMap : list) {
					String string = hashMap.get("INSTANCEID").toString();
					if(instanceid!=Long.parseLong(string)){
						info = "证券代码在系统中已存在!\n";
						return info;
					}
				}
			}
			if (zqjc != null && !zqjc.equals("")) {
				List<HashMap<String,Long>> list = getList("ZQJC",zqjc);
				for (HashMap<String,Long> hashMap : list) {
					String string = hashMap.get("INSTANCEID").toString();
					if(instanceid!=Long.parseLong(string)){
						info = "证券简称在系统中已存在!\n";
						return info;
					}
				}
			}
		}
		return info;
	}
	
	public List<HashMap<String,Long>> getList(String key,String value){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT BIND.INSTANCEID INSTANCEID FROM BD_ZQB_KH_BASE KH INNER JOIN (SELECT * FROM  SYS_ENGINE_FORM_BIND WHERE FORMID=88 AND METADATAID=102) BIND ON KH.ID=BIND.DATAID WHERE KH.? = ?");
		List<HashMap<String,Long>> list = new ArrayList<HashMap<String,Long>>();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			HashMap<String,Long> map;
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, key);
			stmt.setString(2, value);
			rset = stmt.executeQuery();
			while(rset.next()){
				map = new HashMap<String,Long>();
				Long instanceid=rset.getLong("INSTANCEID");
				map.put("INSTANCEID", instanceid);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public String getJdzl(String jdmc,String xmlx,String customerno) {
		Map params = new HashMap();
		params.put(1, jdmc);
		String content = DBUTilNew.getDataStr("CONTENT","SELECT CONTENT FROM BD_ZQB_KM_INFO WHERE JDMC= ? ", params);
		params = new HashMap();
		params.put(1, customerno);
		String manager = DBUTilNew.getDataStr("MANAGER","SELECT P.MANAGER FROM BD_ZQB_PJ_BASE P WHERE P.CUSTOMERNO= ? ", params);
		String customername = DBUTilNew.getDataStr("CUSTOMERNAME","SELECT K.CUSTOMERNAME FROM BD_ZQB_KH_BASE K WHERE K.CUSTOMERNO= ? ", params);
		content=content.replace("\n", "<br/>");
		HashMap<String,String> returnMap = new HashMap<String,String>();
		returnMap.put("CONTENT", content.toString());
		returnMap.put("MANAGER", manager);
		returnMap.put("CUSTOMERNAME", customername);
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String getBgJdzl(String xmbh,String jdmc,String xmlx) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.*,B.* FROM (SELECT JYF,JYDSF,OWNER,MANAGER,SGFS,CZBM,SFGLJY,GDYKZRBG,SFCZ,SFWG,KHLXR,KHLXDH,TO_CHAR(XMFQRQ,'YYYY-MM-DD') XMFQRQ,TO_CHAR(TPRQ,'YYYY-MM-DD') TPRQ,BZ,XMFX FROM BD_ZQB_BGZZLXXX WHERE XMBH=?) A,(SELECT CONTENT FROM BD_ZQB_TYXM_INFO WHERE JDMC=? AND XMLX=?) B");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			ps.setString(2, jdmc);
			ps.setString(3, xmlx);
			rs = ps.executeQuery();
			while (rs.next()) {
				String jyf = rs.getString("JYF");
				String jydsf = rs.getString("JYDSF");
				String owner = rs.getString("OWNER");
				String manager = rs.getString("MANAGER");
				String sgfs = rs.getString("SGFS");
				String czbm = rs.getString("CZBM");
				String sfgljy = rs.getString("SFGLJY");
				String gdykzrbg = rs.getString("GDYKZRBG");
				String sfcz = rs.getString("SFCZ");
				String sfwg = rs.getString("SFWG");
				String khlxr = rs.getString("KHLXR");
				String khlxdh = rs.getString("KHLXDH");
				String xmfqrq = rs.getString("XMFQRQ");
				String tprq = rs.getString("TPRQ");
				String bz = rs.getString("BZ")==null?"":rs.getString("BZ").replace("\n", "<br/>");
				String xmfx = rs.getString("XMFX")==null?"":rs.getString("XMFX").replace("\n", "<br/>");
				String content = rs.getString("CONTENT")==null?"":rs.getString("CONTENT").replace("\n", "<br/>");
				result.put("JYF", jyf);
				result.put("JYDSF", jydsf);
				result.put("OWNER", owner);
				result.put("MANAGER", manager);
				result.put("SGFS", sgfs);
				result.put("CZBM", czbm);
				result.put("SFGLJY", sfgljy);
				result.put("GDYKZRBG", gdykzrbg);
				result.put("SFCZ", sfcz);
				result.put("SFWG", sfwg);
				result.put("KHLXR", khlxr);
				result.put("KHLXDH", khlxdh);
				result.put("XMFQRQ", xmfqrq);
				result.put("TPRQ", tprq);
				result.put("BZ", bz);
				result.put("XMFX", xmfx);
				result.put("CONTENT", content);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	//XLJ 2017年3月8日11:07:57 获取并购项目资料归档中显示的所有阶段内容
		public String getBgZLGD(String xmbh,String jdmc,String xmlx) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT PJ.*,LXJD.CONTENT LXCONTENT,BSJD.CONTENT BSCONTENT,SBJD.CONTENT SBCONTENT,GDJD.CONTENT GDCONTENT,");
			sql.append("LXXX.FJ LXFJ,SBXX.FJ SBFJ,BSXX.FJ BSFJ,GDXX.FJ GDFJ FROM ");
			sql.append("(SELECT JYF,JYDSF,OWNER,MANAGER,XMBH FROM BD_ZQB_BGZZLXXX WHERE XMBH=?) PJ");
			sql.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='并购项目' AND JDMC='项目立项') LXJD ON 1=1");
			sql.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='并购项目' AND JDMC='方案资料报审') BSJD ON 1=1");
			sql.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='并购项目' AND JDMC='申报资料') SBJD ON 1=1");
			sql.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='并购项目' AND JDMC='资料归档') GDJD ON 1=1");
			sql.append(" LEFT JOIN BD_ZQB_BGXMLX LXXX ON LXXX.XMBH = PJ.XMBH");
			sql.append(" LEFT JOIN BD_ZQB_BGFAZLBS BSXX ON BSXX.XMBH = PJ.XMBH");
			sql.append(" LEFT JOIN BD_ZQB_BGSBZL SBXX ON SBXX.XMBH = PJ.XMBH");
			sql.append(" LEFT JOIN BD_ZQB_BGZLGD GDXX ON GDXX.XMBH = PJ.XMBH");
			HashMap<String,String> result = new HashMap<String,String>();
			Connection conn = DBUtil.open();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, xmbh);				
				rs = ps.executeQuery();
				while (rs.next()) {
					String jyf = rs.getString("JYF");
					String jydsf = rs.getString("JYDSF");
					String owner = rs.getString("OWNER");
					String manager = rs.getString("MANAGER");				
					result.put("JYF", jyf);
					result.put("JYDSF", jydsf);
					result.put("OWNER", owner);
					result.put("MANAGER", manager);

					StringBuffer fileContent = new StringBuffer();
					/*项目立项*/
					String ggcontent = rs.getString("LXCONTENT")==null?"":rs.getString("LXCONTENT").replace("\n", "<br/>");
					String LXFJ = rs.getString("LXFJ");
					List<FileUpload> fileUploads = FileUploadAPI.getInstance().getFileUploads(LXFJ);
					for (FileUpload fileUpload : fileUploads) {
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
							+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
					}
					result.put("LXCONTENT", ggcontent);
					result.put("LXFJUUID", LXFJ);
					result.put("LXFJZL", fileContent.toString());
					
					/*方案资料报审*/
					fileContent=new StringBuffer();
					String BSCONTENT = rs.getString("BSCONTENT")==null?"":rs.getString("BSCONTENT").replace("\n", "<br/>");
					String BSFJ = rs.getString("BSFJ");
					fileUploads = FileUploadAPI.getInstance().getFileUploads(BSFJ);
					for (FileUpload fileUpload : fileUploads) {
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
							+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
					}
					result.put("BSCONTENT", BSCONTENT);
					result.put("BSFJUUID", BSFJ);
					result.put("BSFJZL", fileContent.toString());
					
					/*申报资料*/
					fileContent=new StringBuffer();
					String SBCONTENT = rs.getString("SBCONTENT")==null?"":rs.getString("SBCONTENT").replace("\n", "<br/>");
					String SBFJ = rs.getString("SBFJ");
					fileUploads = FileUploadAPI.getInstance().getFileUploads(SBFJ);
					for (FileUpload fileUpload : fileUploads) {
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
							+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
					}
					result.put("SBCONTENT", SBCONTENT);
					result.put("SBFJUUID", SBFJ);
					result.put("SBFJZL", fileContent.toString());
					
					/*资料归档*/
					fileContent=new StringBuffer();
					String GDCONTENT = rs.getString("GDCONTENT")==null?"":rs.getString("GDCONTENT").replace("\n", "<br/>");
					String GDFJ = rs.getString("GDFJ");
					fileUploads = FileUploadAPI.getInstance().getFileUploads(GDFJ);
					for (FileUpload fileUpload : fileUploads) {
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
							+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
					}
					result.put("GDCONTENT", GDCONTENT);
					result.put("GDFJUUID", GDFJ);
					result.put("GDFJZL", fileContent.toString());					
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
			JSONArray json = JSONArray.fromObject(result);
			StringBuffer jsonHtml = new StringBuffer();
			jsonHtml.append(json);
			return jsonHtml.toString();
		}
		
		//XLJ 2017年3月8日11:07:57 获取一般财务项目资料归档中显示的所有阶段内容
		public String getCwZLGD(String xmbh,String jdmc,String xmlx) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT PJ.*,LXJD.CONTENT LXCONTENT,HBJD.CONTENT HBCONTENT,GDJD.CONTENT GDCONTENT,");
			sql.append("LXXX.FJ LXFJ,HBXX.FJ HBFJ,GDXX.FJ GDFJ FROM ");
			sql.append("(SELECT CUSTOMERNAME,MANAGER,OWNER,CZBM,CLBM,XMLX,KHLXR,KHLXDH,TO_CHAR(XMFQRQ,'YYYY-MM-DD') XMFQRQ,XMFWNR,CWGWZZ,XMBH");
			sql.append(" FROM Bd_Zqb_Cwgwxmxx WHERE XMBH=?) PJ");
			sql.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='一般性财务顾问项目' AND JDMC='项目立项') LXJD ON 1=1");
			sql.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='一般性财务顾问项目' AND JDMC='工作进度汇报') HBJD ON 1=1");			
			sql.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE XMLX='一般性财务顾问项目' AND JDMC='资料归档') GDJD ON 1=1");
			sql.append(" LEFT JOIN Bd_Zqb_Cwxmlx LXXX ON LXXX.XMBH = PJ.XMBH");
			sql.append(" LEFT JOIN Bd_Zqb_Cwgzjdhb HBXX ON HBXX.XMBH = PJ.XMBH");			
			sql.append(" LEFT JOIN Bd_Zqb_Cwzlgd GDXX ON GDXX.XMBH = PJ.XMBH");
			HashMap<String,String> result = new HashMap<String,String>();
			Connection conn = DBUtil.open();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, xmbh);
				rs = ps.executeQuery();
				while (rs.next()) {
					String CUSTOMERNAME = rs.getString("CUSTOMERNAME");
					String XMFQRQ = rs.getString("XMFQRQ");
					String owner = rs.getString("OWNER");
					String manager = rs.getString("MANAGER");				
					result.put("CUSTOMERNAME", CUSTOMERNAME);
					result.put("XMFQRQ", XMFQRQ);
					result.put("OWNER", owner);
					result.put("MANAGER", manager);

					StringBuffer fileContent = new StringBuffer();
					/*项目立项*/
					String ggcontent = rs.getString("LXCONTENT")==null?"":rs.getString("LXCONTENT").replace("\n", "<br/>");
					String LXFJ = rs.getString("LXFJ");
					List<FileUpload> fileUploads = FileUploadAPI.getInstance().getFileUploads(LXFJ);
					for (FileUpload fileUpload : fileUploads) {
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
							+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
					}
					result.put("LXCONTENT", ggcontent);
					result.put("LXFJUUID", LXFJ);
					result.put("LXFJZL", fileContent.toString());				
				
					
					/*工作进度汇报*/
					fileContent=new StringBuffer();
					String HBCONTENT = rs.getString("HBCONTENT")==null?"":rs.getString("HBCONTENT").replace("\n", "<br/>");
					String HBFJ = rs.getString("HBFJ");
					fileUploads = FileUploadAPI.getInstance().getFileUploads(HBFJ);
					for (FileUpload fileUpload : fileUploads) {
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
							+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
					}
					result.put("HBCONTENT", HBCONTENT);
					result.put("HBFJUUID", HBFJ);
					result.put("HBFJZL", fileContent.toString());
					
					/*资料归档*/
					fileContent=new StringBuffer();
					String GDCONTENT = rs.getString("GDCONTENT")==null?"":rs.getString("GDCONTENT").replace("\n", "<br/>");
					String GDFJ = rs.getString("GDFJ");
					fileUploads = FileUploadAPI.getInstance().getFileUploads(GDFJ);
					for (FileUpload fileUpload : fileUploads) {
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
							+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
					}
					result.put("GDCONTENT", GDCONTENT);
					result.put("GDFJUUID", GDFJ);
					result.put("GDFJZL", fileContent.toString());					
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
			JSONArray json = JSONArray.fromObject(result);
			StringBuffer jsonHtml = new StringBuffer();
			jsonHtml.append(json);
			return jsonHtml.toString();
		}
				
	public String getCwJdzl(String xmbh,String jdmc,String xmlx) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.*,B.* FROM (SELECT CUSTOMERNAME,MANAGER,OWNER,CZBM,CLBM,XMLX,KHLXR,KHLXDH,TO_CHAR(XMFQRQ,'YYYY-MM-DD') XMFQRQ,XMFWNR,CWGWZZ FROM BD_ZQB_CWGWXMXX WHERE XMBH = ?) A,(SELECT CONTENT FROM BD_ZQB_TYXM_INFO WHERE JDMC = ? AND XMLX = ?) B");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, xmbh);
			ps.setString(2, jdmc);
			ps.setString(3, xmlx);
			rs = ps.executeQuery();
			while (rs.next()) {
				String customername = rs.getString("CUSTOMERNAME");
				String manager = rs.getString("MANAGER");
				String owner = rs.getString("OWNER");
				String czbm = rs.getString("CZBM");
				String clbm = rs.getString("CLBM");
				String xmlxValue = rs.getString("XMLX");
				String khlxr = rs.getString("KHLXR");
				String khlxdh = rs.getString("KHLXDH");
				String xmfqrq = rs.getString("XMFQRQ");
				String xmfwnr = rs.getString("XMFWNR")==null?"":rs.getString("XMFWNR").replace("\n", "<br/>");
				String cwgwzz = rs.getString("CWGWZZ")==null?"":rs.getString("CWGWZZ").replace("\n", "<br/>");
				String content = rs.getString("CONTENT")==null?"":rs.getString("CONTENT").replace("\n", "<br/>");
				result.put("CUSTOMERNAME", customername);
				result.put("MANAGER", manager);
				result.put("OWNER", owner);
				result.put("CZBM", czbm);
				result.put("CLBM", clbm);
				result.put("XMLXVALUE", xmlxValue);
				result.put("KHLXR", khlxr);
				result.put("KHLXDH", khlxdh);
				result.put("XMFQRQ", xmfqrq);
				result.put("XMFWNR", xmfwnr);
				result.put("CWGWZZ", cwgwzz);
				result.put("KHLXDH", khlxdh);
				result.put("CONTENT", content);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public String getGuaPaiContent(String customerno) {
		StringBuffer sql = new StringBuffer("SELECT KH.CUSTOMERNAME,KH.ZQJC,KH.ZQDM,KH.USERNAME,KH.TEL,KH.EMAIL,PJ.ZCLR,PJ.OWNER,PJ.MANAGER,PJ.DDAP,PJ.XMZH,PJ.XMXY,PJ.XMLS,"
				+ " LCGG.FJ LCGGFJ,(SELECT CONTENT FROM BD_ZQB_KM_INFO WHERE JDMC='股改') GGCONTENT,"
				+ " SBSH.FJ SBSHFJ,(SELECT CONTENT FROM BD_ZQB_KM_INFO WHERE JDMC='申报审核') SBSHCONTENT,"
				+ " NHFK.FJ NHFKFJ,(SELECT CONTENT FROM BD_ZQB_KM_INFO WHERE JDMC='内核反馈及回复') NHFKCONTENT,"
				+ " GZFK.FJ GZFKFJ,(SELECT CONTENT FROM BD_ZQB_KM_INFO WHERE JDMC='股转反馈及回复') GZFKCONTENT,"
				+ " XMGP.GPZL GPZL,(SELECT CONTENT FROM BD_ZQB_KM_INFO WHERE JDMC='挂牌登记及归档') GPCONTENT"
				+ " FROM BD_ZQB_KH_BASE KH LEFT JOIN BD_ZQB_PJ_BASE PJ ON KH.CUSTOMERNO=PJ.CUSTOMERNO "
				+ " LEFT JOIN (SELECT CUSTOMERNO,FJ FROM BD_ZQB_LCGG) LCGG ON KH.CUSTOMERNO=LCGG.CUSTOMERNO "
				+ " LEFT JOIN (SELECT CUSTOMERNO,FJ FROM BD_ZQB_SQNH) SBSH ON KH.CUSTOMERNO=SBSH.CUSTOMERNO "
				+ " LEFT JOIN (SELECT CUSTOMERNO,FJ FROM BD_ZQB_NHFK) NHFK ON KH.CUSTOMERNO=NHFK.CUSTOMERNO "
				+ " LEFT JOIN (SELECT CUSTOMERNO,FJ FROM BD_ZQB_GZFKJHF) GZFK ON KH.CUSTOMERNO=GZFK.CUSTOMERNO "
				+ " LEFT JOIN (SELECT CUSTOMERNO,GPZL FROM BD_ZQB_GPDJJGD) XMGP ON KH.CUSTOMERNO=XMGP.CUSTOMERNO "
				+ " WHERE KH.CUSTOMERNO= ? ");
		StringBuffer fileContent = new StringBuffer();
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			rs = ps.executeQuery();
			while (rs.next()) {
				String customername = rs.getString("CUSTOMERNAME");
				String zqjc = rs.getString("ZQJC");
				String zqdm = rs.getString("ZQDM");
				String username = rs.getString("USERNAME");
				String tel = rs.getString("TEL");
				String email = rs.getString("EMAIL");
				String zclr = rs.getString("ZCLR");
				String owner = rs.getString("OWNER");
				String manager = rs.getString("MANAGER");
				String ddap = rs.getString("DDAP");
				String xmzh = rs.getString("XMZH");
				String xmxy = rs.getString("XMXY");
				String xmls = rs.getString("XMLS");
				/*项目股改*/
				String ggcontent = rs.getString("GGCONTENT")==null?"":rs.getString("GGCONTENT").replace("\n", "<br/>");
				String lcggfj = rs.getString("LCGGFJ");
				List<FileUpload> fileUploads = FileUploadAPI.getInstance().getFileUploads(lcggfj);
				for (FileUpload fileUpload : fileUploads) {
					String icon = "iwork_img/attach.png";
					if(fileUpload.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fileUpload.getFileSrcName());
					}
					fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
						+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
				}
				result.put("GGCONTENT", ggcontent);
				result.put("XMGGZLUUID", lcggfj);
				result.put("XMGGFILEZL", fileContent.toString());
				
				/*申报审核*/
				fileContent=new StringBuffer();
				String sbshcontent = rs.getString("SBSHCONTENT")==null?"":rs.getString("SBSHCONTENT").replace("\n", "<br/>");
				String sbshfj = rs.getString("SBSHFJ");
				fileUploads = FileUploadAPI.getInstance().getFileUploads(sbshfj);
				for (FileUpload fileUpload : fileUploads) {
					String icon = "iwork_img/attach.png";
					if(fileUpload.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fileUpload.getFileSrcName());
					}
					fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
						+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
				}
				result.put("SBSHCONTENT", sbshcontent);
				result.put("SBSHZLUUID", sbshfj);
				result.put("SBSHFILEZL", fileContent.toString());
				
				/*内核反馈及回复*/
				fileContent=new StringBuffer();
				String nhfkcontent = rs.getString("NHFKCONTENT")==null?"":rs.getString("NHFKCONTENT").replace("\n", "<br/>");
				String nhfkfj = rs.getString("NHFKFJ");
				fileUploads = FileUploadAPI.getInstance().getFileUploads(nhfkfj);
				for (FileUpload fileUpload : fileUploads) {
					String icon = "iwork_img/attach.png";
					if(fileUpload.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fileUpload.getFileSrcName());
					}
					fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
						+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
				}
				result.put("NHFKCONTENT", nhfkcontent);
				result.put("NHFKZLUUID", nhfkfj);
				result.put("NHFKFILEZL", fileContent.toString());
				
				/*股转反馈及回复*/
				fileContent=new StringBuffer();
				String gzfkcontent = rs.getString("GZFKCONTENT")==null?"":rs.getString("GZFKCONTENT").replace("\n", "<br/>");
				String gzfkfj = rs.getString("GZFKFJ");
				fileUploads = FileUploadAPI.getInstance().getFileUploads(gzfkfj);
				for (FileUpload fileUpload : fileUploads) {
					String icon = "iwork_img/attach.png";
					if(fileUpload.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fileUpload.getFileSrcName());
					}
					fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
						+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
				}
				result.put("GZFKCONTENT", gzfkcontent);
				result.put("GZFKZLUUID", gzfkfj);
				result.put("GZFKFILEZL", fileContent.toString());
				
				/*挂牌登记及归档*/
				fileContent=new StringBuffer();
				String gpcontent = rs.getString("GPCONTENT");
				String gpzl = rs.getString("GPZL")==null?"":rs.getString("GPZL").replace("\n", "<br/>");
				fileUploads = FileUploadAPI.getInstance().getFileUploads(gpzl);
				for (FileUpload fileUpload : fileUploads) {
					String icon = "iwork_img/attach.png";
					if(fileUpload.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fileUpload.getFileSrcName());
					}
					fileContent.append("<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
						+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append((fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())).append("</a>&nbsp;<br/>");
				}
				result.put("GPCONTENT", gpcontent);
				result.put("GPFILEZL", fileContent.toString());
				
				result.put("CUSTOMERNAME", customername);
				result.put("ZQJC", zqjc);
				result.put("ZQDM", zqdm);
				result.put("USERNAME", username);
				result.put("TEL", tel);
				result.put("EMAIL", email);
				result.put("ZCLR", zclr);
				result.put("OWNER", owner);
				result.put("MANAGER", manager);
				result.put("DDAP", ddap);
				result.put("XMZH", xmzh);
				result.put("XMXY", xmxy);
				result.put("XMLS", xmls);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		JSONArray json = JSONArray.fromObject(result);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public List<HashMap<String, Object>> getXmlxProjectList(int pageSize, int pageNumber) {
		StringBuffer sql = new StringBuffer("SELECT * FROM (SELECT CUSTOMERNAME,ROWNUM RNUM,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ,ZCLR AS ZCLR,OWNER AS OWNER,MANAGER AS MANAGER,DDAP AS DDAP,XMZH AS XMZK,XMXY AS XMHY,XMLS AS XMLS FROM BD_ZQB_PJ_BASE) WHERE RNUM> ? AND RNUM <= ?");
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		list = shanXiZqbCustomerManageDAO.getXmlxProjectList(sql.toString(),pageSize,pageNumber);
		return list;
	}

	public int getXmlxProjectListSize() {
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) CNUM FROM BD_ZQB_PJ_BASE");
		int count = shanXiZqbCustomerManageDAO.getXmlxProjectListSize(sql.toString());
		return count;
	}

	public List<HashMap<String, Object>> getXmlxCyrProjectList(int pageSize, int pageNumber) {
		StringBuffer content = new StringBuffer();
		StringBuffer sql = new StringBuffer("SELECT DATA.*,DATANUM.CNUM FROM (");
		content.append(" SELECT * FROM (SELECT CUSTOMERNAME,NAME,TYPE,ZHXGSJ,NUM,ROWNUM RNUM FROM (SELECT A.*,B.NUM FROM (SELECT CUSTOMERNAME,ZCLR NAME,'项目承揽人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,OWNER NAME,'大区负责人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,MANAGER NAME,'项目负责人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,DDAP NAME,'督导安排' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMZH NAME,'项目注会' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMXY NAME,'项目行研' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMLS NAME,'项目律师' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " ) A "
				+ " INNER JOIN (SELECT NAME,COUNT(*) NUM FROM ( "
				+ " SELECT CUSTOMERNAME,ZCLR NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,OWNER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,MANAGER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,DDAP NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMZH NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMXY NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMLS NAME FROM BD_ZQB_PJ_BASE) GROUP BY NAME) B ON A.NAME=B.NAME "
				+ " ORDER BY A.NAME,A.CUSTOMERNAME)) "
				+ " WHERE RNUM>? AND RNUM <=?");
		sql.append(content);
		sql.append(" ) DATA INNER JOIN (SELECT NAME,COUNT(1) CNUM FROM (");
		sql.append(content);
		sql.append(") GROUP BY NAME) DATANUM ON DATA.NAME=DATANUM.NAME ORDER BY DATA.NAME,DATA.CUSTOMERNAME");
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		list = shanXiZqbCustomerManageDAO.getXmlxCyrProjectList(sql.toString(),pageSize,pageNumber);
		return list;
	}

	public int getXmlxCyrProjectListSize() {
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) CNUM FROM (SELECT A.*,B.NUM FROM (SELECT CUSTOMERNAME,ZCLR NAME,'项目承揽人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,OWNER NAME,'大区负责人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,MANAGER NAME,'项目负责人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,DDAP NAME,'督导安排' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMZH NAME,'项目注会' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMXY NAME,'项目行研' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMLS NAME,'项目律师' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " ) A INNER JOIN (SELECT NAME,COUNT(*) NUM FROM ( "
				+ " SELECT CUSTOMERNAME,ZCLR NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,OWNER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,MANAGER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,DDAP NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,XMZH NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,XMXY NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,XMLS NAME FROM BD_ZQB_PJ_BASE) GROUP BY NAME) B ON A.NAME=B.NAME)");
		int count = shanXiZqbCustomerManageDAO.getXmlxCyrProjectListSize(sql.toString());
		return count;
	}

	public void doExcelExpXM(HttpServletResponse response) {
		List<HashMap<String,Object>> expXmlxProjectList = getExpXmlxProjectList();
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("以项目为基准统计");
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 10);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		style.setWrapText(true);
		style.setFont(contentfont);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setFont(contentfont);
		style1.setWrapText(true);
		int m=0;
		int z=0;
		HSSFRow row = sheet.createRow((int) m++);
		HSSFCell cell = row.createCell((short) z++);
		cell.setCellValue("公司名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目承揽人");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("大区负责人");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目负责人");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("督导安排");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目注会");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目行研");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目注会");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("最后修改时间");
		cell.setCellStyle(style);

		for (HashMap<String, Object> hashMap : expXmlxProjectList) {
			z=0;
			row = sheet.createRow((int) m++);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("CUSTOMERNAME").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZCLR")==null?"":hashMap.get("ZCLR").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("OWNER")==null?"":hashMap.get("OWNER").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("MANAGER")==null?"":hashMap.get("MANAGER").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("DDAP")==null?"":hashMap.get("DDAP").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("XMZK")==null?"":hashMap.get("XMZK").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("XMHY")==null?"":hashMap.get("XMHY").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("XMLS")==null?"":hashMap.get("XMLS").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZHXGSJ")==null?"":hashMap.get("ZHXGSJ").toString());
			cell.setCellStyle(style1);
		}
		sheet.autoSizeColumn((short)0);
		sheet.autoSizeColumn((short)1);
		sheet.autoSizeColumn((short)2);
		sheet.autoSizeColumn((short)3);
		sheet.autoSizeColumn((short)4);
		sheet.autoSizeColumn((short)5);
		sheet.autoSizeColumn((short)6);
		sheet.autoSizeColumn((short)7);
		sheet.autoSizeColumn((short)8);
		sheet.autoSizeColumn((short)9);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("以项目为基准统计.xls");
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
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}
	
	public List<HashMap<String, Object>> getExpXmlxProjectList() {
		StringBuffer sql = new StringBuffer("SELECT PROJECTNAME,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ,ZCLR AS ZCLR,OWNER AS OWNER,MANAGER AS MANAGER,DDAP AS DDAP,XMZH AS XMZK,XMXY AS XMHY,XMLS AS XMLS FROM BD_ZQB_PJ_BASE");
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		list = shanXiZqbCustomerManageDAO.getExpXmlxProjectList(sql.toString());
		return list;
	}

	public void doExcelCYExp(HttpServletResponse response) {
		List<HashMap<String,Object>> expXmlxCyrProjectList = getExpXmlxCyrProjectList();
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("以参与人为基准统计");
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 10);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		style.setWrapText(true);
		style.setFont(contentfont);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setFont(contentfont);
		style1.setWrapText(true);
		int m=0;
		int z=0;
		HSSFRow row = sheet.createRow((int) m++);
		HSSFCell cell = row.createCell((short) z++);
		cell.setCellValue("参与人姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目数量");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("公司名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("职务");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("最后修改时间");
		cell.setCellStyle(style);
		HashMap<String,Object> dataMap = new HashMap<String,Object>();
		for (HashMap<String, Object> hashMap : expXmlxCyrProjectList) {
			String name = hashMap.get("NAME").toString();
			String tmpName="";
			if(!dataMap.isEmpty()){
				tmpName = dataMap.get("NAME").toString();
			}
			z=0;
			row = sheet.createRow((int) m);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("NAME").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("XMNUM").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("CUSTOMERNAME").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("TYPE").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZHXGSJ")==null?"":hashMap.get("ZHXGSJ").toString());
			cell.setCellStyle(style1);
			/*if(!tmpName.equals("")&&name.equals(tmpName)){
				hbcount++;
			}else */
			if(!tmpName.equals("")&&!name.equals(tmpName)){
				Integer hbnum = Integer.parseInt(dataMap.get("NUM").toString());
				sheet.addMergedRegion(new Region(m-hbnum, (short) 0, m-1, (short) 0));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 1, m-1, (short) 1));
			}
			m++;
			dataMap=hashMap;
		}
		Integer hbnum = Integer.parseInt(dataMap.get("NUM").toString());
		sheet.addMergedRegion(new Region(m-hbnum, (short) 0, m-1, (short) 0));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 1, m-1, (short) 1));
		sheet.setColumnWidth(0, 15 * 256 * 2);
		sheet.autoSizeColumn((short)1);
		sheet.autoSizeColumn((short)2);
		sheet.autoSizeColumn((short)3);
		sheet.autoSizeColumn((short)4);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("以参与人为基准统计.xls");
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
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}
	
	public List<HashMap<String, Object>> getExpXmlxCyrProjectList() {
		StringBuffer sql = new StringBuffer("SELECT CUSTOMERNAME,NAME,TYPE,ZHXGSJ,NUM,XMNUM FROM (SELECT A.*,B.NUM,C.XMNUM FROM (SELECT CUSTOMERNAME,ZCLR NAME,'项目承揽人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,OWNER NAME,'大区负责人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,MANAGER NAME,'项目负责人' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,DDAP NAME,'督导安排' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMZH NAME,'项目注会' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMXY NAME,'项目行研' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMLS NAME,'项目律师' AS TYPE,TO_CHAR(ZHXGSJ,'YYYY-MM-DD HH24:MI:SS') ZHXGSJ FROM BD_ZQB_PJ_BASE "
				+ " ) A "
				+ " INNER JOIN (SELECT NAME,COUNT(*) NUM FROM ( "
				+ " SELECT CUSTOMERNAME,ZCLR NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,OWNER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,MANAGER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,DDAP NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,XMZH NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,XMXY NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION ALL "
				+ " SELECT CUSTOMERNAME,XMLS NAME FROM BD_ZQB_PJ_BASE) GROUP BY NAME) B ON A.NAME=B.NAME "
				+ " INNER JOIN (SELECT NAME,COUNT(*) XMNUM FROM ( "
				+ " SELECT CUSTOMERNAME,ZCLR NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,OWNER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,MANAGER NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,DDAP NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMZH NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMXY NAME FROM BD_ZQB_PJ_BASE "
				+ " UNION "
				+ " SELECT CUSTOMERNAME,XMLS NAME FROM BD_ZQB_PJ_BASE) GROUP BY NAME) C ON A.NAME=C.NAME "
				+ " ORDER BY A.NAME,A.CUSTOMERNAME)");
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		list = shanXiZqbCustomerManageDAO.getExpXmlxCyrProjectList(sql.toString());
		return list;
	}

	public String getCustomerAutoData() {
		StringBuffer sql = new StringBuffer("SELECT * FROM (SELECT CUSTOMERNAME,CUSTOMERNO FROM BD_ZQB_KH_BASE UNION "
				+ " SELECT COMPANYNAME,'000000' CUSTOMERNO FROM ORGCOMPANY) ORDER BY CUSTOMERNAME");
		List<HashMap<String,String>> customerAutoDataList = getCustomerAutoDataList(sql.toString());
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(customerAutoDataList);
		jsonHtml.append("{\"success\":true,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	public List<HashMap<String,String>> getCustomerAutoDataList(String sql){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		list = shanXiZqbCustomerManageDAO.getCustomerAutoDataList(sql);
		return list;
	}
	
	private HashMap<String, Integer> getNumMap(String sql,List<Object> params) {
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.size(); i++) {
					ps.setObject(i+1, params.get(i));
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Integer qynum = rs.getInt("QYNUM");
				Integer rznum = rs.getInt("RZNUM");
				Integer lxnum = rs.getInt("LXNUM");
				Integer usernum = rs.getInt("USERNUM");
				Integer gpfxnum = rs.getInt("GPFXNUM");
				result.put("QYNUM", qynum);
				result.put("RZNUM", rznum);
				result.put("LXNUM", lxnum);
				result.put("USERNUM", usernum);
				result.put("GPFXNUM", gpfxnum);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

	public void expCustomer(HttpServletResponse response) {
		
		Long xmlxformid = Long.parseLong(ConfigUtil.readValue(CN_FILENAME, "xmlxformid"));
		StringBuffer sql = new StringBuffer("SELECT KHDATA.*,KMINFO.JDMC FROM (SELECT DATA.* FROM (SELECT KH.ID,KH.CUSTOMERNAME,KH.CUSTOMERNO,KH.TYPE,KH.ZQJC,KH.ZQDM,KH.SSHY,KH.ZWMC,NVL(KH.ZCZB,'') ZCZB,TO_CHAR(KH.GFGSRQ,'yyyy-mm-dd') GFGSRQ,NVL(PJDATA.SPZT,'') XMLXSPZT,NUMDATA.CNUM,NVL(GGLC.SPZT,'') GGLCSPZT,NVL(XMNH.SPZT,'') XMNHLCSPZT,NVL(NHFK.SPZT,'') NHFKLCSPZT FROM ");
		sql.append(" (");
		sql.append("SELECT ID,CUSTOMERNAME,CUSTOMERNO,TYPE,ZQJC,ZQDM,SSHY,ZWMC,ZCZB,GFGSRQ FROM BD_ZQB_KH_BASE WHERE 1=1");
		sql.append(")");
		sql.append(" KH LEFT JOIN (SELECT PJ.CUSTOMERNO,PJ.SPZT FROM BD_ZQB_PJ_BASE PJ ) PJDATA ON KH.CUSTOMERNO=PJDATA.CUSTOMERNO");
		
		sql.append( " LEFT JOIN (SELECT KHDATA.CUSTOMERNO,COUNT(KHDATA.CUSTOMERNO) CNUM FROM (SELECT DATA.*,ROWNUM RW FROM (SELECT KH.ID,KH.CUSTOMERNAME,KH.CUSTOMERNO,KH.TYPE,KH.ZQJC,KH.ZQDM,KH.SSHY,KH.ZWMC,NVL(KH.ZCZB,'') ZCZB,TO_CHAR(KH.GFGSRQ,'yyyy-mm-dd') GFGSRQ FROM ");
		sql.append(" (");
		sql.append("SELECT ID,CUSTOMERNAME,CUSTOMERNO,TYPE,ZQJC,ZQDM,SSHY,ZWMC,ZCZB,GFGSRQ FROM BD_ZQB_KH_BASE WHERE 1=1");
		sql.append(")");
		sql.append(" KH LEFT JOIN (SELECT PJ.CUSTOMERNO FROM BD_ZQB_PJ_BASE PJ) PJDATA ON KH.CUSTOMERNO=PJDATA.CUSTOMERNO");
		sql.append(" ORDER BY KH.ID DESC) DATA) KHDATA,BD_ZQB_KM_INFO KMINFO");
		sql.append(" GROUP BY CUSTOMERNO) NUMDATA ON KH.CUSTOMERNO=NUMDATA.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT LCGG.CUSTOMERNO,LCGG.SPZT FROM BD_ZQB_LCGG LCGG) GGLC ON KH.CUSTOMERNO=GGLC.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT XMNH.CUSTOMERNO,XMNH.SPZT FROM BD_ZQB_SQNH XMNH) XMNH ON KH.CUSTOMERNO=XMNH.CUSTOMERNO");
		sql.append(" LEFT JOIN (SELECT NHFK.CUSTOMERNO,NHFK.SPZT FROM BD_ZQB_NHFK NHFK) NHFK ON KH.CUSTOMERNO=NHFK.CUSTOMERNO");
		sql.append(" ORDER BY KH.ID DESC) DATA) KHDATA,BD_ZQB_KM_INFO KMINFO");
		sql.append(" ORDER BY KHDATA.ID DESC,KMINFO.SORTID");
		List<HashMap<String,Object>> list = ShanXiZqbCustomerManageDAO.getExpCustomerList(sql.toString());
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("客户信息");
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 10);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor((short) 22);
		style.setFillPattern((short) 1);
		style.setBorderBottom((short) 1);
		style.setBottomBorderColor((short) 8);
		style.setBorderLeft((short) 1);
		style.setLeftBorderColor((short) 8);
		style.setBorderRight((short) 1);
		style.setRightBorderColor((short) 8);
		style.setBorderTop((short) 1);
		style.setTopBorderColor((short) 8);
		style.setWrapText(true);
		style.setFont(contentfont);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setFont(contentfont);
		style1.setWrapText(true);
		int m=0;
		int z=0;
		HSSFRow row = sheet.createRow((int) m++);
		HSSFCell cell = row.createCell((short) z++);
		cell.setCellValue("客户名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("证券简称(公司简称)");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("证券代码(股票代码)");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("所属行业");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("住所地");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("注册类型");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("注册资本(万元)");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("成立日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目阶段");
		cell.setCellStyle(style);
		cell = row.createCell((short) z++);
		cell.setCellValue("阶段审批状态");
		cell.setCellStyle(style);
		HashMap<String,Object> dataMap = new HashMap<String,Object>();
		//CUSTOMERNAME,TYPE,ZQJC,ZQDM,SSHY,ZWMC,ZCZB,GFGSRQ,XMLXSPZT,CNUM,GGLCSPZT,XMNHLCSPZT,NHFKLCSPZT,JDMC
		for (HashMap<String, Object> hashMap : list) {
			String name = hashMap.get("CUSTOMERNO")==null?"":hashMap.get("CUSTOMERNO").toString();
			String tmpName="";
			if(!dataMap.isEmpty()){
				tmpName = dataMap.get("CUSTOMERNO")==null?"":hashMap.get("CUSTOMERNO").toString();
			}
			z=0;
			row = sheet.createRow((int) m);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("CUSTOMERNAME")==null?"":hashMap.get("CUSTOMERNAME").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZQJC")==null?"":hashMap.get("ZQJC").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZQDM")==null?"":hashMap.get("ZQDM").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("SSHY")==null?"":hashMap.get("SSHY").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZWMC")==null?"":hashMap.get("ZWMC").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("TYPE")==null?"":hashMap.get("TYPE").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("ZCZB")==null?"":hashMap.get("ZCZB").toString());
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("GFGSRQ")==null?"":hashMap.get("GFGSRQ").toString());
			cell.setCellStyle(style1);
			String jdmc = hashMap.get("JDMC")==null?"":hashMap.get("JDMC").toString();
			cell = row.createCell((short) z++);
			cell.setCellValue(jdmc);
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			if(jdmc.equals("项目立项")){
				cell.setCellValue(hashMap.get("XMLXSPZT")==null?"未提交":hashMap.get("XMLXSPZT").toString().equals("审批通过")?hashMap.get("XMLXSPZT").toString():hashMap.get("XMLXSPZT").toString().equals("起草人")?"起草人申报中":hashMap.get("XMLXSPZT").toString().indexOf("驳回")>0?hashMap.get("XMLXSPZT").toString():hashMap.get("XMLXSPZT").toString()+"审批中");
			}else if (jdmc.equals("股改")){
				cell.setCellValue(hashMap.get("GGLCSPZT")==null?"未提交":hashMap.get("GGLCSPZT").toString().equals("审批通过")?hashMap.get("GGLCSPZT").toString():hashMap.get("GGLCSPZT").toString().equals("起草人")?"起草人申报中":hashMap.get("GGLCSPZT").toString().indexOf("驳回")>0?hashMap.get("GGLCSPZT").toString():hashMap.get("GGLCSPZT").toString()+"审批中");
			}else if (jdmc.equals("项目内核")){
				cell.setCellValue(hashMap.get("XMNHLCSPZT")==null?"未提交":hashMap.get("XMNHLCSPZT").toString().equals("审批通过")?hashMap.get("XMNHLCSPZT").toString():hashMap.get("XMNHLCSPZT").toString().equals("起草人")?"起草人申报中":hashMap.get("XMNHLCSPZT").toString().indexOf("驳回")>0?hashMap.get("XMNHLCSPZT").toString():hashMap.get("XMNHLCSPZT").toString()+"审批中");
			}else if (jdmc.equals("内核反馈")){
				cell.setCellValue(hashMap.get("NHFKLCSPZT")==null?"未提交":hashMap.get("NHFKLCSPZT").toString().equals("审批通过")?hashMap.get("NHFKLCSPZT").toString():hashMap.get("NHFKLCSPZT").toString().equals("起草人")?"起草人申报中":hashMap.get("NHFKLCSPZT").toString().indexOf("驳回")>0?hashMap.get("NHFKLCSPZT").toString():hashMap.get("NHFKLCSPZT").toString()+"审批中");
			}else{
				cell.setCellValue("");
			}
			cell.setCellStyle(style1);
			if(!tmpName.equals("")&&!name.equals(tmpName)){
				Integer hbnum = Integer.parseInt(dataMap.get("CNUM").toString());
				sheet.addMergedRegion(new Region(m-hbnum, (short) 0, m-1, (short) 0));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 1, m-1, (short) 1));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 2, m-1, (short) 2));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 3, m-1, (short) 3));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 4, m-1, (short) 4));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 5, m-1, (short) 5));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 6, m-1, (short) 6));
				sheet.addMergedRegion(new Region(m-hbnum, (short) 7, m-1, (short) 7));
			}
			m++;
			dataMap=hashMap;
		}
		
		Integer hbnum = Integer.parseInt(dataMap.get("CNUM").toString());
		sheet.addMergedRegion(new Region(m-hbnum, (short) 0, m-1, (short) 0));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 1, m-1, (short) 1));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 2, m-1, (short) 2));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 3, m-1, (short) 3));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 4, m-1, (short) 4));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 5, m-1, (short) 5));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 6, m-1, (short) 6));
		sheet.addMergedRegion(new Region(m-hbnum, (short) 7, m-1, (short) 7));
		
		sheet.setColumnWidth(0, 20 * 256 * 2);
		sheet.setColumnWidth(1, 10 * 256 * 2);
		sheet.setColumnWidth(2, 10 * 256 * 2);
		sheet.setColumnWidth(3, 10 * 256 * 2);
		sheet.setColumnWidth(4, 6 * 256 * 2);
		sheet.setColumnWidth(5, 6 * 256 * 2);
		sheet.setColumnWidth(6, 10 * 256 * 2);
		sheet.setColumnWidth(7, 6 * 256 * 2);
		sheet.setColumnWidth(8, 6 * 256 * 2);
		sheet.setColumnWidth(9, 10 * 256 * 2);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("客户信息.xls");
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
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}

	/*public String commitProject(Long instanceid, String projectNo) {
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String userid = userModel.getUserid();
		String username = userModel.getUsername();
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		HashMap lcFromData = (HashMap) fromData.clone();
		Long lcbs=fromData.get("LCBS")==null?0:Long.parseLong(fromData.get("LCBS").toString().equals("")?"0":fromData.get("LCBS").toString());
		String taskid = "";
		String executionId = "";
		String actDefId ="";
		Long instanceId = 0L;
		if(lcbs==0){
			actDefId= ProcessAPI.getInstance().getProcessActDefId("LCGGSP");
			lcFromData.put("CREATEUSER", username);
			lcFromData.put("CREATEUSERID", userid);
			lcFromData.put("CREATEDATE", UtilDate.getNowdate());
			Long formId = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
			instanceId = ProcessAPI.getInstance().newInstance(actDefId, formId, userid);
			ProcessAPI.getInstance().saveFormData(actDefId, instanceId, lcFromData, false);// 保存流程
			String jd1 = SystemConfig._gugaiLcConf.getJd1();
			List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			for (Task task : tasklist) {
				if (Long.parseLong(task.getProcessInstanceId()) == instanceId) {
					taskid = task.getId();
					executionId = task.getExecutionId();
				}
			}
			fromData.put("LCBH", actDefId);
			fromData.put("LCBS", instanceId);
			fromData.put("STEPID", jd1);
			fromData.put("TASKID", taskid);
			Long dataid = Long.parseLong(fromData.get("ID").toString());
			DemAPI.getInstance().updateFormData(XMLXUUID, instanceid, fromData, dataid, false);
		}else{
			executionId=lcbs.toString();
			instanceId=lcbs;
			String jd1=fromData.get("STEPID").toString();
			actDefId= ProcessAPI.getInstance().getProcessActDefId("LCGGSP");
			List<Task> tasklist = ProcessAPI.getInstance().getUserProcessTaskList(actDefId, jd1,userid);
			for (Task task : tasklist) {
				if (Long.parseLong(task.getProcessInstanceId()) == instanceId) {
					taskid = task.getId();
					executionId = task.getExecutionId();
				}

			}
		}
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+instanceId+"}");
		return jsonHtml.toString();
	}*/
}
