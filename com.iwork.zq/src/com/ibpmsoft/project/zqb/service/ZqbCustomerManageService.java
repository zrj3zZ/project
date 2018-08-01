package com.ibpmsoft.project.zqb.service;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iwork.core.util.SpringBeanUtil;
import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.dao.ZqbCustomerManageDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.commons.util.UtilString;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.runtime.el.ExpressionParamsModel;
import com.iwork.core.engine.runtime.tools.RuntimeELUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.MD5;
import com.iwork.core.util.ResponseUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.MessageAPI;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
public class ZqbCustomerManageService {
	private static Logger logger = Logger.getLogger(ZqbCustomerManageService.class);
	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private static final String ZQB_CUSTOMER_UUID = "a243efd832bf406b9caeaec5df082e28";
	public final static String SHOW_PROJECT_FILENAME = "/isshowproject.properties";// 抓取网站配置文件

	private ZqbCustomerManageDAO zqbCustomerManageDAO;

	public ZqbCustomerManageDAO getZqbCustomerManageDAO() {
		return zqbCustomerManageDAO;
	}

	public void setZqbCustomerManageDAO(
			ZqbCustomerManageDAO zqbCustomerManageDAO) {
		this.zqbCustomerManageDAO = zqbCustomerManageDAO;
	}

	public List<HashMap> getCurrentCustomerListAll(String customername,
			String zqdm, String zrfs, String cxddbg, String status,
			String type, String zwmc, BigDecimal jlr, String gpsj,
			BigDecimal zczbbegin, BigDecimal zczbend, String gfgsrqbegin,
			String gfgsrqend, String orderbygpsj, String orderbyzqdm,String ygp) {
		// 客户管理 与 信息查询功能 的联系与区别
		// 客户管理 与 信息查询 2个模块 都是客户资料的显示部分
		// 客户管理 显示与登录人员有关的所有客户信息，举例
		// 场外市场经理登录，显示所有客户信息，
		// 项目负责人登录，显示他自己填的所有客户信息，
		// 项目负责人+督导人员（pjdu）登录，显示与自己有关的所有客户信息，包括自己增加的以及分派给自己的，
		// 督导人员登录，显示内容同上
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		HashMap conditionMap = new HashMap();
		String userFullName = UserContextUtil.getInstance()
				.getCurrentUserFullName();
		long orgid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getOrgroleid();
		String username = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getUsername();
		if (customername != null && !customername.equals("")) {
			conditionMap.put("CUSTOMERNAME", customername);
		}
		if (zqdm != null && !zqdm.equals("")) {
			conditionMap.put("ZQDM", zqdm);
		}
		if (zrfs != null && !zrfs.equals("")) {
			conditionMap.put("ZRFS", zrfs);
		}
		if (cxddbg != null && !cxddbg.equals("")) {
			conditionMap.put("CXDDBG", cxddbg);
		}
		if (status != null && !status.equals("")) {
			conditionMap.put("STATUS", status);
		}
		if (type != null && !type.equals("")) {
			conditionMap.put("TYPE", type);
		}
		if (zwmc != null && !zwmc.equals("")) {
			conditionMap.put("ZWMC", zwmc);
		}
		if (jlr != null && !jlr.equals("")) {
			conditionMap.put("JLR", jlr);
		}
		if (ygp != null && !ygp.equals("")) {
			conditionMap.put("YGP", ygp);
		}
		if (gpsj != null && !gpsj.equals("")) {
			conditionMap.put("GPSJ", gpsj);
		}
		if (zczbbegin != null && !zczbbegin.equals("")) {
			conditionMap.put("ZCZBBEGIN", zczbbegin);
		}
		if (zczbend != null && !zczbend.equals("")) {
			conditionMap.put("ZCZBEND", zczbend);
		}
		if (gfgsrqbegin != null && !gfgsrqbegin.equals("")) {
			conditionMap.put("GFGSRQBEGIN", gfgsrqbegin);
		}
		if (gfgsrqend != null && !gfgsrqend.equals("")) {
			conditionMap.put("GFGSRQEND", gfgsrqend);
		}
		if (getIsSuperMan()) {
			// list = DemAPI.getInstance().getList(ZQB_CUSTOMER_UUID,
			// conditionMap, "ID");
			list = zqbCustomerManageDAO.getCurrentCustomerListAll(customername,
					zqdm, zrfs, cxddbg, status, type, zwmc, jlr, gpsj,
					zczbbegin, zczbend, gfgsrqbegin, gfgsrqend, orderbygpsj,
					orderbyzqdm,ygp);
			// Collections.reverse(list);
		} else if (orgid == (long) 4 || uc.get_userMapList().size() > 0) {
			boolean flag = (orgid == (long) 4);
			for (OrgUserMap org : uc.get_userMapList()) {
				if (org.getOrgroleid().equals("4")) {
					flag = true;
					break;
				}
			}
			list = zqbCustomerManageDAO.getCurrentCustomerListDDAll(username,
					userFullName, customername, zqdm, zrfs, cxddbg, status,
					type, zwmc, jlr, gpsj, zczbbegin, zczbend, gfgsrqbegin,
					gfgsrqend, flag, orderbygpsj, orderbyzqdm,ygp);

		} else {
			conditionMap.put("CREATEUSER", username);
			// List<HashMap> blllist =
			// DemAPI.getInstance().getList(ZQB_CUSTOMER_UUID, conditionMap,
			// "ID");
			List<HashMap> blllist = zqbCustomerManageDAO
					.getCurrentCustomerListPjAll(username, userFullName,
							customername, zqdm, zrfs, cxddbg, status, type,
							zwmc, jlr, gpsj, zczbbegin, zczbend, gfgsrqbegin,
							gfgsrqend, orderbygpsj, orderbyzqdm,ygp);
			// Collections.reverse(list);
			for (HashMap m : blllist) {
				if (m.get("CREATEUSER").toString().trim().equals(username)
						|| m.get("USERNAME").toString().trim()
								.equals(userFullName)) {
					list.add(m);
				}
			}
		}
		return list;
	}

	public List<HashMap> getCurrentCustomerList(String cusername,String customername,
			String zqdm, String zrfs, String cxddbg, String status,
			String type, String zwmc, BigDecimal jlr, String gpsjBEGIN,
			String zczbbegin, String zczbend, String gfgsrqbegin,
			String gfgsrqend, int pageSize, int pageNow, String orderbygpsj,
			String orderbyzqdm,String ygp
			,String extend4,String zcqx,String sshy,String gpsjEND,String innovation,String classification,HashMap<String,Integer> params,String orderbygpzt) {
		// 客户管理 与 信息查询功能 的联系与区别
		// 客户管理 与 信息查询 2个模块 都是客户资料的显示部分
		// 客户管理 显示与登录人员有关的所有客户信息，举例
		// 场外市场经理登录，显示所有客户信息，
		// 项目负责人登录，显示他自己填的所有客户信息，
		// 项目负责人+督导人员（pjdu）登录，显示与自己有关的所有客户信息，包括自己增加的以及分派给自己的，
		// 督导人员登录，显示内容同上
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		//HashMap conditionMap = new HashMap();
		String userFullName = UserContextUtil.getInstance()
				.getCurrentUserFullName();
		long orgid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getOrgroleid();
		String username = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getUsername();	
		int numOther = params.get("numOther");
		int numKhfzr = params.get("numKhfzr");
		list = zqbCustomerManageDAO.getCurrentCustomerList(cusername,customername,
				zqdm, zrfs, cxddbg, status, type, zwmc, jlr, gpsjBEGIN,
				zczbbegin, zczbend, gfgsrqbegin, gfgsrqend, pageSize,
				pageNow, orderbygpsj, orderbyzqdm,ygp
				,extend4,zcqx,sshy,gpsjEND,innovation,classification,numOther,numKhfzr,orderbygpzt);	
		return list;
	}

	public boolean getIsSuperMan() {
		String roleTyle = "";
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
				flag = true;
			}
		}

		return flag;
	}

	public String deleteCustomer(Long instanceid) {
		String info = "";
		boolean flag = false;
		try {
			Long formId = DemAPI.getInstance()
					.getFormIdByInstanceId(new Long(instanceid),
							EngineConstants.SYS_INSTANCE_TYPE_DEM);
			HashMap hash = DemAPI.getInstance().getFromData(instanceid, formId,
					EngineConstants.SYS_INSTANCE_TYPE_DEM);
			// 如果该用户为已挂牌，则需要判断是否给该挂牌公司建立了用户或者在持续督导分派下分了持续督导专员，如果已经分派了，则不进行删除，直接提示不可删除
			if (hash.get("CUSTOMERNO") != null && hash.get("YGP") != null
					&& !hash.get("YGP").equals("")
					&& hash.get("YGP").equals("已挂牌")) {
				String customerno = hash.get("CUSTOMERNO").toString();
				String username = DBUtil
						.getString(
								"select USERNAME from OrgUser where departmentid=(select  id from orgdepartment where departmentno='"
										+ customerno + "')", "USERNAME");
				// 已存在用户则不可删除，否则删除
				if (username != null && !username.equals("")) {
					info = "该客户下已存在用户，不可删除！";
					return info;
				} else {
					String sql = "delete from  orgdepartment where departmentno='"
							+ customerno + "'";
					int i = DBUtil.executeUpdate(sql);
				}
				// 查询是否已进行过持续督导分派
				HashMap conditionMap = new HashMap();
				conditionMap.put("KHBH", customerno);
				List<HashMap> list = DemAPI.getInstance().getList(
						ZQB_CUSTOMER_PURVIEW_UUID, conditionMap, null);
				if (list.size() > 0) {
					if (list.size() == 1) {
						if (list.get(0).get("KHFZR") != null
								&& !list.get(0).get("KHFZR").equals("")) {
							return "已经分派过持续督导专员，无法删除！";
						} else {
							Long instanceId = Long.parseLong(list.get(0)
									.get("INSTANCEID").toString());
							boolean isdel = DemAPI.getInstance()
									.removeFormData(instanceId);
						}
					} else {
						return "存在多条持续督导分派记录！";
					}
				}
			}
			//查询该客户下是否存在项目
			int pjnum=0;
			if(hash.get("CUSTOMERNO")!=null){
				pjnum = DBUtil.getInt("SELECT COUNT(*) NUM FROM BD_ZQB_PJ_BASE P WHERE P.CUSTOMERNO='"+hash.get("CUSTOMERNO").toString()+"'", "NUM");
			}
			if(pjnum!=0){
				return "该客户已存在项目！";
			}
			
			//查询该客户下是否存在结算信息
			int jxnum=0;
			String isDwPj = ConfigUtil.readAllProperties("/common.properties").get("isDwPj");
			if(hash.get("CUSTOMERNO")!=null&&isDwPj!=null&&isDwPj.equals("1")){
				jxnum = DBUtil.getInt("SELECT COUNT(*) NUM FROM BD_ZQB_XMJSXXGLB B WHERE B.CUSTOMERNO='"+hash.get("CUSTOMERNO").toString()+"'", "NUM");
			}
			if(jxnum!=0){
				return "该客户已存在结算信息！";
			}
			Long dataId=Long.parseLong(hash.get("ID").toString());
			String customername=hash.get("CUSTOMERNAME").toString();
			LogUtil.getInstance().addLog(dataId, "客户信息维护", "删除客户："+customername);
			Long orgroleid = UserContextUtil.getInstance()
					.getCurrentUserContext().get_userModel().getOrgroleid();
			if (orgroleid != 5) {
				String smsContent = "";
				String sysMsgContent = "";
				if (hash != null) {
					smsContent = ZQBNoticeUtil.getInstance()
							.getNoticeSmsContent(
									ZQB_Notice_Constants.CUSTOMER_REMOVE_KEY,
									hash);
					sysMsgContent = ZQBNoticeUtil.getInstance()
							.getNoticeSysMsgContent(
									ZQB_Notice_Constants.CUSTOMER_REMOVE_KEY,
									hash);
				}
				// 删除下发短信
				String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(
						ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
				UserContext uc = UserContextUtil.getInstance()
						.getCurrentUserContext();
				UserContext target = UserContextUtil.getInstance()
						.getUserContext(userid);
				if (target != null) {
					if (!smsContent.equals("")) {
						String mobile = target.get_userModel().getMobile();
						if (mobile != null && !mobile.equals("")) {
							MessageAPI.getInstance().sendSMS(uc, mobile,
									smsContent);
						}
					}
					if (!sysMsgContent.equals("")) {
						MessageAPI.getInstance().sendSysMsg(userid,
								"客户基本信息维护提醒", sysMsgContent);
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
		} catch (Exception e) {
			logger.error(e,e);
		}
		return info;
	}
	
	private String getObjectvalue(Long formid, UserContext uc, String entityname ,String sequence) {
		ExpressionParamsModel model = new ExpressionParamsModel();
		model.setFormid(formid); 
		model.setEntityname(entityname);
		model.setInstanceid(0l);
		model.setContext(uc);
		model.setEngineType(0l);
		String objectvalue = RuntimeELUtil.getInstance().convertMacrosValue(sequence,model);
		return objectvalue;
	}
	
	private String getValue(Cell cell) {
		if(cell!=null){
			if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {  
				return String.valueOf(cell.getBooleanCellValue());  
			} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
				BigDecimal value = new BigDecimal(cell.getNumericCellValue());
				return String.valueOf(value.toString());  
			} else {  
				return String.valueOf(cell.getStringCellValue());  
			}
		}else{
			return "";
		}
    }

	public void impCustomer(String filename) {
		Long starttime = new Date().getTime();
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(filename);
		UserContext uc =UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		String rootPath=request.getRealPath("/");
		//String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
		String role = null;//角色(包括兼职,逗号分开)
		String[] rolearr = null;//角色数组
		String pdname = null;//父级部门名称
		String ismanager = null;//是否部门管理员
		String departname = null;//部门名称
		Long formid = 0l;//客户主数据维护formid
		String demUUID = null;//客户主数据维护UUID
		String demUUIDFP = null;//持续督导分派UUID
		Long khinstanceid = 0l;//客户instanceid
		Long fpinstanceid = 0l;//分派instanceid
		String now = UtilDate.getNowDatetime();//当前时间
		String nowdate = UtilDate.getNowdate();//当前日期
		java.sql.Date startdate = new java.sql.Date(UtilDate.StringToDate(now, "yyyy-MM-dd").getTime());//添加用户使用
		java.sql.Date enddate = new java.sql.Date(UtilDate.StringToDate("9999-12-31", "yyyy-MM-dd").getTime());//添加用户使用
		String userid = user.getUserid();//当前用户userid
		String username = user.getUsername();//当前用户username
		String khlxr = null;//客户联系人(用户名)
		String tel = null;//办公电话
		String email = null;//邮箱
		String zqjc = null;//证券简称
		String zqdm = null;//证券代码
		String weixin_code = null;//微信号
		String userid_ = null;//账号
		String orguserid_ = null;//插入用账号
		String mobile = null;//手机号
		String customerno = null;//客户编号
		Long guapaiid = 0l;//挂牌公司部门id
		String companyname = null;//组织机构名称
		
		File file = null;
		Workbook workBook = null;
		Sheet sheet = null;
		//客户名称集合，用于判定客户名称是否存在
		Set<String> customerset = new HashSet<String>();
		//部门名称集合，用于判定部门名称是否存在
		Set<String> deprtmentname = new HashSet<String>();
		
		//ZQDM集合，用于判ZQDM是否存在
		Set<String> zqdmset = new HashSet<String>();
		//ZQJC集合，用于判ZQJC是否存在
		Set<String> zqjcset = new HashSet<String>();
		//ZHANGHAO集合，用于判ZHANGHAO是否存在
		Set<String> zhanghaoset = new HashSet<String>();
		//NAME集合，用于判NAME是否存在
		Set<String> nameset = new HashSet<String>();
		
		//分类Map，用于获取部门id
		Map<String,Long> deprtmentname_id = new HashMap<String,Long>();
		Map<String,Long> deprtmentname_pid = new HashMap<String,Long>();
		//账号集合用于判断用户是否存在
		Set<String> useridset = new HashSet<String>();
		//角色信息
		Map<String,Long> rolename_id = new HashMap<String,Long>();
		//组织机构id
		Long companyid=null;
		//生成动态客户编号条件
		String entityname = "BD_ZQB_KH_BASE";
		String sequence = "CNO%sequence:y-m-no%";
		//获得server.xml里默认密码
		String pwd = SystemConfig._iworkServerConf.getUserDefaultPassword();
		MD5 md5 = new MD5();
		String passwoprd = md5.getEncryptedPwd(pwd);
		
		StringBuffer reduplicateMsg = new StringBuffer();
		
		Long orguserid_max=0l;//本次导入时,获得的最大orguser表id
		Long orgdepartmentid_max=0l;//本次导入时,获得的最大orgdepartment表id
		Long orgusermapid_max=0l;//本次导入时,获得的最大orgusermap表id
		Long orguserid;Long orgdepartmentid;Long orgusermapid;
		//兼职信息导入
		StringBuffer orgusermapsql = new StringBuffer();
		orgusermapsql.append("INSERT INTO ORGUSERMAP(ID,USERID,DEPARTMENTID,DEPARTMENTNAME,ORGROLEID,ORGROLENAME,ISMANAGER,USERMAPTYPE) VALUES(?,?,?,?,?,?,?,?)");
		//用户信息导入
		StringBuffer orgusersql = new StringBuffer();
		orgusersql.append("INSERT INTO ORGUSER (ID,USERID,USERNAME,PASSWORD,DEPARTMENTID,DEPARTMENTNAME,ORGROLEID,ISMANAGER,OFFICETEL,MOBILE,EMAIL,");
		orgusersql.append("USERNO,ORDERINDEX,EXTEND1,EXTEND2,USERTYPE,STARTDATE,ENDDATE,USERSTATE,COMPANYID,COMPANYNAME,WEIXIN_CODE)");
		orgusersql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		//部门信息导入
		StringBuffer orgdepartmentsql = new StringBuffer();
		orgdepartmentsql.append("INSERT INTO ORGDEPARTMENT(ID,DEPARTMENTNAME,COMPANYID,PARENTDEPARTMENTID,DEPARTMENTNO,ORDERINDEX,DEPARTMENTSTATE) VALUES(?,?,?,?,?,?,?)");
		//获得
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT TRIM(CUSTOMERNAME) CUSTOMERNAME FROM BD_ZQB_KH_BASE");
		
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT TRIM(DEPARTMENTNAME) DEPARTMENTNAME,ID,PARENTDEPARTMENTID PID FROM ORGDEPARTMENT");
		
		StringBuffer sql3 = new StringBuffer();
		sql3.append("SELECT USERID FROM ORGUSER");
		
		StringBuffer sql4 = new StringBuffer();
		sql4.append("SELECT ROLENAME,ID FROM ORGROLE");
		
		StringBuffer sql5 = new StringBuffer();
		sql5.append("SELECT ID,COMPANYNAME FROM ORGCOMPANY");
		
		StringBuffer sql6 = new StringBuffer();
		sql6.append("SELECT FORMID,UUID FROM SYS_DEM_ENGINE WHERE TITLE='客户主数据维护'");
		
		StringBuffer sql7 = new StringBuffer();
		sql7.append("SELECT ID FROM ORGDEPARTMENT WHERE DEPARTMENTNAME='挂牌公司'");
		
		StringBuffer sql8 = new StringBuffer();
		sql8.append("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '持续督导分派'");
		
		StringBuffer sql9 = new StringBuffer();
		sql9.append("SELECT MAX(ID) ID FROM ORGUSER");
		
		StringBuffer sql10 = new StringBuffer();
		sql10.append("SELECT MAX(ID) ID FROM ORGDEPARTMENT");
		
		StringBuffer sql11 = new StringBuffer();
		sql11.append("SELECT MAX(ID) ID FROM ORGUSERMAP");
		
		StringBuffer sql12 = new StringBuffer();
		sql12.append("SELECT ZQDM,ZQJC FROM BD_ZQB_KH_BASE");
		
		StringBuffer sql13 = new StringBuffer();
		sql13.append("SELECT USERID,USERNAME FROM ORGUSER");
		
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		PreparedStatement psorgusermap = null;
		PreparedStatement psorguser = null;
		PreparedStatement psorgdepartment = null;
		ResultSet rs = null;
		try {
			/**获得客户名称*/
			ps = conn.prepareStatement(sql1.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				customerset.add(rs.getString(1));
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得部门名称*/
			ps = conn.prepareStatement(sql2.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				deprtmentname.add(rs.getString(1));
				deprtmentname_id.put(rs.getString(1), rs.getLong(2));
				deprtmentname_pid.put(rs.getString(1), rs.getLong(3));
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得USERID*/
			ps = conn.prepareStatement(sql3.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				useridset.add(rs.getString(1));
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得角色名称*/
			ps = conn.prepareStatement(sql4.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				rolename_id.put(rs.getString(1), rs.getLong(2));
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得companyid*/
			ps = conn.prepareStatement(sql5.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				companyid = rs.getLong(1);
				companyname = rs.getString(2);
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得客户formid、uuid*/
			ps = conn.prepareStatement(sql6.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				formid = rs.getLong(1);
				demUUID = rs.getString(2);
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得挂牌公司部门id*/
			ps = conn.prepareStatement(sql7.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				guapaiid = rs.getLong(1);
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得持续督导分派UUID*/
			ps = conn.prepareStatement(sql8.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				demUUIDFP = rs.getString(1);
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得ORGUSER最大ID*/
			ps = conn.prepareStatement(sql9.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				orguserid_max = rs.getLong(1);
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得ORGDEPARTMENT最大ID*/
			ps = conn.prepareStatement(sql10.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				orgdepartmentid_max = rs.getLong(1);
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得ORGUSERMAP最大ID*/
			ps = conn.prepareStatement(sql11.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				orgusermapid_max = rs.getLong(1);
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得ZQDM、ZQJC*/
			ps = conn.prepareStatement(sql12.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString(1)!=null&&!rs.getString(1).equals(""))
					zqdmset.add(rs.getString(1));
				if(rs.getString(2)!=null&&!rs.getString(2).equals(""))
					zqjcset.add(rs.getString(2));
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			/**获得USERID、USERNAME*/
			ps = conn.prepareStatement(sql13.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString(1)!=null&&!rs.getString(1).equals(""))
					zhanghaoset.add(rs.getString(1));
				if(rs.getString(2)!=null&&!rs.getString(2).equals(""))
					nameset.add(rs.getString(2));
			}
			//初始化orguser、orgdepartment表id增加数
			orguserid=1l;orgdepartmentid=1l;orgusermapid=1L;
			psorgusermap = conn.prepareStatement(orgusermapsql.toString());
			psorguser = conn.prepareStatement(orgusersql.toString());
			psorgdepartment = conn.prepareStatement(orgdepartmentsql.toString());
			conn.setAutoCommit(true);
			//遍历上传文件
			for (FileUpload fileUpload : sublist) {
				//加载xls文件
				file = new File(rootPath+File.separator+fileUpload.getFileUrl());
				
				try {workBook = new XSSFWorkbook(rootPath+File.separator+fileUpload.getFileUrl());  
				} catch (Exception ex) {  
					try {workBook = new HSSFWorkbook(new FileInputStream(rootPath+File.separator+fileUpload.getFileUrl()));
					} catch (FileNotFoundException e) {} catch (Exception e) {}
				}
				if(workBook==null){continue;}
				//遍历sheet
				for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {  
					sheet = workBook.getSheetAt(numSheet);  
					if (sheet == null) {continue;}
					// 循环行Row  
					for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
						int g = sheet.getLastRowNum();
						Row row = sheet.getRow(rowNum);  
						if (row == null) {continue;}  
						// 循环列Cell  
						Cell cell = row.getCell(0);  
						
						rolearr = getValue(cell).split("，");
						role = rolearr[0];
						
						cell = row.getCell(1);
						pdname = getValue(cell);
						cell = row.getCell(2);
						ismanager = getValue(cell);
						cell = row.getCell(3);
						departname = getValue(cell);
						cell = row.getCell(4);
						zqdm = getValue(cell);
						cell = row.getCell(5);
						zqjc = getValue(cell);
						cell = row.getCell(6);
						khlxr = getValue(cell);
						cell = row.getCell(7);
						userid_ = getValue(cell);
						if((rolename_id.get(role)!=null&&!rolename_id.get(role).equals("")&&departname!=null&&!departname.equals(""))||((zqdm!=null&&!zqdm.equals(""))||(khlxr!=null&&!khlxr.equals(""))||(userid_!=null&&!userid_.equals("")))){
							cell = row.getCell(8);
							mobile = getValue(cell);
							cell = row.getCell(9);
							tel = getValue(cell);
							cell = row.getCell(10);
							email = getValue(cell);
							cell = row.getCell(11);
							weixin_code = getValue(cell);
							//判断信息获得USERID
							orguserid_ = role.equals("董秘")?
									zqdm==null||zqdm.equals("")?
											userid_==null||userid_.equals("")?
													UtilString.getPingYin(khlxr).toUpperCase()
													:
													userid_
											:
											zqdm
									:
									userid_==null||userid_.equals("")?
										UtilString.getPingYin(khlxr).toUpperCase()
										:
										userid_;
							
							//判断是否是董秘
							if(role.equals("董秘")){
								//判断公司名称是否存在
								if(!customerset.contains(departname)){
									customerno = getObjectvalue(formid, uc, entityname, sequence);
									khinstanceid = DemAPI.getInstance().newInstance(demUUID, userid);
									HashMap hashdata = new HashMap();
									hashdata.put("CREATEUSER", userid);
									hashdata.put("CREATEDATE", nowdate);
									hashdata.put("CUSTOMERNAME", departname);
									hashdata.put("CUSTOMERNO", customerno);
									hashdata.put("STATUS", "有效");
									hashdata.put("USERNAME", khlxr);
									hashdata.put("TEL", tel);
									hashdata.put("EMAIL", email);
									hashdata.put("NDYS", "0");
									hashdata.put("JLR", "0");
									hashdata.put("ZQJC", zqjc);
									hashdata.put("ZQDM", zqdm);
									hashdata.put("YGP", "已挂牌");
									hashdata.put("ZRFS", "挂牌企业");
									hashdata.put("USERID", userid);
									hashdata.put("ZHXGR", userid+"["+username+"]");
									hashdata.put("ZHXGSJ", now);
									boolean flag = DemAPI.getInstance().saveFormData(demUUID, khinstanceid, hashdata, false);
									if(flag){
										fpinstanceid = DemAPI.getInstance().newInstance(demUUIDFP, userid);
										HashMap datamap = new HashMap();
										datamap.put("KHBH", customerno);
										datamap.put("KHMC", departname);
										datamap.put("CJSJ", UtilDate.getNowDatetime());
										boolean f = DemAPI.getInstance().saveFormData(demUUIDFP, fpinstanceid, datamap, false);
									}
								}
							}
							//判断部门是否存在
							if(departname!=null&&!departname.equals("")&&!deprtmentname.contains(departname)){
								psorgdepartment.setLong(1,orgdepartmentid_max+orgdepartmentid);
								psorgdepartment.setString(2,departname);
								psorgdepartment.setLong(3,companyid);
								psorgdepartment.setLong(4,deprtmentname_id.get(pdname)==null||deprtmentname_id.get(pdname).equals("")?role.equals("董秘")?guapaiid:0l:deprtmentname_id.get(pdname));
								psorgdepartment.setString(5,role.equals("董秘")?customerno:"");
								psorgdepartment.setString(6,(orgdepartmentid_max+orgdepartmentid+""));
								psorgdepartment.setLong(7,0l);
								psorgdepartment.addBatch();
								deprtmentname_id.put(departname, orgdepartmentid_max+orgdepartmentid);
							}
							//判断用户是否存在
							if(orguserid_!=null&&!orguserid_.equals("")&&!useridset.contains(orguserid_)){
								psorguser.setLong(1,orguserid_max+orguserid);
								psorguser.setString(2,orguserid_);
								psorguser.setString(3,khlxr);
								psorguser.setString(4,passwoprd);
								psorguser.setLong(5,deprtmentname_id.get(departname));
								psorguser.setString(6,departname);
								psorguser.setLong(7,rolename_id.get(role));
								psorguser.setLong(8,ismanager==null||ismanager.equals("")?0l:ismanager.equals("是")?1l:0l);
								psorguser.setString(9,tel);
								psorguser.setString(10,mobile);
								psorguser.setString(11,email);
								psorguser.setString(12,orguserid_);
								psorguser.setLong(13,orguserid_max+orguserid);
								psorguser.setString(14,role.equals("董秘")?customerno:"");
								psorguser.setString(15,departname);
								psorguser.setLong(16,0l);
								psorguser.setDate(17, startdate);
								psorguser.setDate(18, enddate);
								psorguser.setLong(19,0l);
								psorguser.setLong(20,companyid);
								psorguser.setString(21,companyname);
								psorguser.setString(22,weixin_code);
								psorguser.addBatch();
								for (int i = 1; i < rolearr.length; i++) {
									psorgusermap.setLong(1, orgusermapid_max+orgusermapid);
									psorgusermap.setString(2, orguserid_);
									psorgusermap.setLong(3, deprtmentname_id.get(departname));
									psorgusermap.setString(4, departname);
									psorgusermap.setLong(5, rolename_id.get(rolearr[i]));
									psorgusermap.setString(6, rolearr[i]);
									psorgusermap.setLong(7, 0l);
									psorgusermap.setString(8, "0");
									psorgusermap.addBatch();
									orgusermapid++;
								}
							}
							
							//重复信息用于提示操作人员-----------------华丽分割线-----------------
							if(departname!=null&&!departname.equals("")&&customerset.contains(departname)){
								reduplicateMsg.append("第").append(rowNum+1).append("行的公司名称/部门名称:").append(departname).append(",已存在数据库或当前导入文件\r");
							}
							if(zqdm!=null&&!zqdm.equals("")&&zqdmset.contains(zqdm)){
								reduplicateMsg.append("第").append(rowNum+1).append("行的公司代码:").append(zqdm).append(",已存在数据库或当前导入文件\r");
							}else{
								if(zqdm!=null&&!zqdm.equals("")){
									zqdmset.add(zqdm);
								}
							}
							if(zqjc!=null&&!zqjc.equals("")&&zqjcset.contains(zqjc)){
								reduplicateMsg.append("第").append(rowNum+1).append("行的公司简称:").append(zqjc).append(",已存在数据库或当前导入文件\r");
							}else{
								if(zqjc!=null&&!zqjc.equals("")){
									zqjcset.add(zqjc);
								}
							}
							if(userid_!=null&&!userid_.equals("")&&zhanghaoset.contains(userid_)){
								reduplicateMsg.append("第").append(rowNum+1).append("行的账号:").append(userid_).append(",已存在数据库或当前导入文件\r");
							}else{
								if(userid_!=null&&!userid_.equals("")){
									zhanghaoset.add(userid_);
								}
							}
							if(khlxr!=null&&!khlxr.equals("")&&nameset.contains(khlxr)){
								reduplicateMsg.append("第").append(rowNum+1).append("行的公司简称:").append(khlxr).append(",已存在数据库或当前导入文件\r");
							}else{
								if(khlxr!=null&&!khlxr.equals("")){
									nameset.add(khlxr);
								}
							}
							//重复信息用于提示操作人员-----------------华丽分割线-----------------
							
							//向对应的集合中加入刚插入的信息
							if(role.equals("董秘")){
								if(!customerset.contains(departname)){
									customerset.add(departname);
								}
							}
							//orguser、orgdepartment id增加数自增,并向对应的集合中加入刚插入的信息
							if(departname!=null&&!departname.equals("")&&!deprtmentname.contains(departname)){
								orgdepartmentid++;
								deprtmentname.add(departname);
							}
							if(orguserid_!=null&&!orguserid_.equals("")&&!useridset.contains(orguserid_)){
								orguserid++;
								useridset.add(orguserid_);
							}
							
						}
					}
				}
			}
			psorgdepartment.executeBatch();
			psorguser.executeBatch();
			psorgusermap.executeBatch();
		} catch (Exception e) {}finally{
			try {
				if(psorgdepartment!=null){
					psorgdepartment.close();
				}
				if(psorguser!=null){
					psorguser.close();
				}
			} catch (Exception e) {
				
			}
			DBUtil.close(conn, ps, rs);
		}
		Long endtime = new Date().getTime();
		ResponseUtil.write("导入完毕,用时"+(endtime-starttime)+"毫秒*"+reduplicateMsg.toString());
		System.gc();
		System.out.println("导入完毕,用时"+(endtime-starttime)+"毫秒");
	}

	public int getCurrentCustomerListSize(String cusername,String customername, String zqdm,
			String zrfs, String cxddbg, String status, String type,
			String zwmc, BigDecimal jlr, String gpsjBEGIN, String zczbbegin,
			String zczbend, String gfgsrqbegin, String gfgsrqend,String ygp
			,String extend4,String zcqx,String sshy,String gpsjEND,String innovation,String classification,HashMap<String,Integer> params) {
		List<HashMap> list = new ArrayList<HashMap>();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userFullName = UserContextUtil.getInstance()
				.getCurrentUserFullName();
		long orgid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getOrgroleid();
		String username = UserContextUtil.getInstance().getCurrentUserContext()
				.get_userModel().getUsername();	
		int numOther = params.get("numOther");
		int numKhfzr = params.get("numKhfzr");
		list = zqbCustomerManageDAO.getCurrentCustomerListSize(cusername,
				customername, zqdm, zrfs, cxddbg, status, type, zwmc, jlr,
				gpsjBEGIN, zczbbegin, zczbend, gfgsrqbegin, gfgsrqend,ygp
				,extend4,zcqx,sshy,gpsjEND,innovation,classification,numOther,numKhfzr);
		Collections.reverse(list);		
		return list.size();
	}
    public void expkhmc(HttpServletResponse response,String cusername,String customername, String zqdm,
                        String zrfs, String cxddbg, String status, String type,
                        String zwmc, BigDecimal jlr, String gpsjBEGIN, String zczbbegin,
                        String zczbend, String gfgsrqbegin, String gfgsrqend,String ygp
            ,String extend4,String zcqx,String sshy,String gpsjEND,String innovation,String classification,HashMap<String,Integer> params) {
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        String userFullName = UserContextUtil.getInstance()
                .getCurrentUserFullName();
        long orgid = UserContextUtil.getInstance().getCurrentUserContext()
                .get_userModel().getOrgroleid();
        String username = UserContextUtil.getInstance().getCurrentUserContext()
                .get_userModel().getUsername();
        int numOther = params.get("numOther");
        int numKhfzr = params.get("numKhfzr");
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("客户列表");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);

        HSSFFont headfont = wb.createFont();
        headfont.setFontName("宋体");
        headfont.setFontHeightInPoints((short) 11);// 字体大小
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setBorderTop((short) 1);
        style.setWrapText(true);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom((short) 1);
        style2.setBorderLeft((short) 1);
        style2.setBorderRight((short) 1);
        style2.setBorderTop((short) 1);
        style2.setWrapText(true);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFFont font2 = wb.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font2);
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        List<HashMap> list = new ArrayList<HashMap>();
        List<HashMap> list1 = new ArrayList<HashMap>();
        int m=0;
        int z=0;
        HSSFRow row1 = sheet.createRow((int) m++);
        HSSFCell
        cell1 = row1.createCell((short) z++);cell1.setCellValue("客户名称");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("证券简称\n(公司简称)");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("证券代码\n(股票代码)");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("信披人");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("信披人电话");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("所属部门");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("所属证监局");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("客户状态");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("挂牌时间");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("挂牌状态");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("创新层");cell1.setCellStyle(style);
        cell1 = row1.createCell((short) z++);cell1.setCellValue("客户评级");cell1.setCellStyle(style);
        list=zqbCustomerManageDAO.getCurrentCustomerListSize(cusername,
                customername, zqdm, zrfs, cxddbg, status, type, zwmc, jlr,
                gpsjBEGIN, zczbbegin, zczbend, gfgsrqbegin, gfgsrqend,ygp
                ,extend4,zcqx,sshy,gpsjEND,innovation,classification,numOther,numKhfzr);
        for (HashMap map : list) {
            z=0;
            row1 = sheet.createRow((int) m++);
            HSSFCell
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("khmc") == null ? "" : map.get("khmc").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("zqjc") == null ? "" : map.get("zqjc").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("zqdm") == null ? "" : map.get("zqdm").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("xpr") == null ? "" : map.get("xpr").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("xprdh") == null ? "" : map.get("xprdh").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ssbm") == null ? "" : map.get("ssbm").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("sszjj") == null ? "" : map.get("sszjj").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("khzt") == null ? "" : map.get("khzt").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("gpsj") == null ? "" : map.get("gpsj").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("gpzt") == null ? "" : map.get("gpzt").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("cxc") == null ? "" : map.get("cxc").toString());cell2.setCellStyle(style2);
            cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("gspj") == null ? "" : map.get("gspj").toString());cell2.setCellStyle(style2);
        }
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 3000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 2500);
        sheet.setColumnWidth(8, 6000);
        sheet.setColumnWidth(9, 2500);
        sheet.setColumnWidth(10, 2500);
        sheet.setColumnWidth(11, 2500);
        OutputStream out1 = null;
        // 第六步，将文件存到指定位置
        try {
            String disposition = "attachment;filename="
                    + URLEncoder.encode(
                    new StringBuilder("客户列表").append(".xls")
                            .toString(), "UTF-8");

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
	public HashMap<String,Integer> getCusParams(){
		HashMap<String,Integer> params = new HashMap<String,Integer>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		//客户权限(其他节点人员看全部)
		StringBuffer other = new StringBuffer("SELECT COUNT(*) AS NUM FROM BD_MDM_KHQXGLB B WHERE B.KHFZR IS NOT NULL AND (SUBSTR(B.FHSPR,0, INSTR(B.FHSPR,'[',1)-1) = '").append(userid).append("' OR SUBSTR(B.ZZSPR,0, INSTR(B.ZZSPR,'[',1)-1) = '").append(userid).append("' OR SUBSTR(B.CWSCBFZR2,0, INSTR(B.CWSCBFZR2,'[',1)-1) = '").append(userid).append("' OR SUBSTR(B.CWSCBFZR3,0, INSTR(B.CWSCBFZR3,'[',1)-1) = '").append(userid).append("' OR SUBSTR(B.FBBWJSHR,0, INSTR(B.FBBWJSHR,'[',1)-1) = '").append(userid).append("' OR SUBSTR(B.GGFBR,0, INSTR(B.GGFBR,'[',1)-1) = '").append(userid).append("')");
		int numOther = DBUtil.getInt(other.toString(), "NUM");
		params.put("numOther", numOther);
		
		//客户权限(KHFZR、ZZCXDD看自己创建的和自己督导的)
		StringBuffer Khfzr = new StringBuffer("SELECT COUNT(*) AS NUM FROM BD_MDM_KHQXGLB B WHERE B.KHFZR IS NOT NULL AND (SUBSTR(B.KHFZR,0, INSTR(B.KHFZR,'[',1)-1) = '").append(userid).append("' OR SUBSTR(B.ZZCXDD,0, INSTR(B.ZZCXDD,'[',1)-1) = '").append(userid).append("')");
		int numKhfzr = DBUtil.getInt(Khfzr.toString(), "NUM");
		params.put("numKhfzr", numKhfzr);
		
		return params;
	}

	public String checkCFXX(String khmc, String zqdm, String zqjc,
			Long instanceid) {
		String info = "";
		if(instanceid==0){
			if (khmc != null && !khmc.equals("")) {
				List<HashMap> list = getList("CUSTOMERNAME",khmc);
				if(!list.isEmpty()){
					info = "公司全称在系统中已存在!\n";
					return info;
				}
			}
			if (zqdm != null && !zqdm.equals("")) {
				List<HashMap> list = getList("ZQDM",zqdm);
				if(!list.isEmpty()){
					info = "证券代码在系统中已存在!\n";
					return info;
				}
			}
			if (zqjc != null && !zqjc.equals("")) {
				List<HashMap> list = getList("ZQJC",zqjc);
				if(!list.isEmpty()){
					info = "证券简称在系统中已存在!\n";
					return info;
				}
			}
		}else{
			if (khmc != null && !khmc.equals("")) {
				List<HashMap> list = getList("CUSTOMERNAME",khmc);
				for (HashMap hashMap : list) {
					String string = hashMap.get("INSTANCEID").toString();
					if(instanceid!=Long.parseLong(string)){
						info = "公司全称在系统中已存在!\n";
						return info;
					}
				}
			}
			if (zqdm != null && !zqdm.equals("")) {
				List<HashMap> list = getList("ZQDM",zqdm);
				for (HashMap hashMap : list) {
					String string = hashMap.get("INSTANCEID").toString();
					if(instanceid!=Long.parseLong(string)){
						info = "证券代码在系统中已存在!\n";
						return info;
					}
				}
			}
			if (zqjc != null && !zqjc.equals("")) {
				List<HashMap> list = getList("ZQJC",zqjc);
				for (HashMap hashMap : list) {
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
	
	public String Associate(String khmc){
		StringBuffer jsonHtml = new StringBuffer();	
		List<HashMap> items = zqbCustomerManageDAO.getAssociateCustomerList(khmc);
		HashMap<String,Object> item = new HashMap<String,Object>();
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String UserAssociate(String commonstr){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String customerno=user.getExtend1();
		
		StringBuffer jsonHtml = new StringBuffer();	
		List<HashMap> items = zqbCustomerManageDAO.getAssociateUserList(commonstr,customerno);
		HashMap<String,Object> item = new HashMap<String,Object>();
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public boolean getIsShowProject() {
		String isShowProject = ConfigUtil.readValue(SHOW_PROJECT_FILENAME,
				"isShowProject");// 获取连接网址配置
		return "1".equals(isShowProject) ? true : false;
	}
	
	public List<HashMap> getList(String key,String value){
		StringBuffer sql = new StringBuffer();
		sql.append("select bind.Instanceid INSTANCEID from bd_zqb_kh_base kh inner join (select * from  SYS_ENGINE_FORM_BIND where formid=88 and metadataid=102) bind on kh.id=bind.dataid where kh."+key+" = ?");
		List<HashMap> list = new ArrayList<HashMap>();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			HashMap map;
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, value);
			rset = stmt.executeQuery();
			while(rset.next()){
				map = new HashMap();
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
}
