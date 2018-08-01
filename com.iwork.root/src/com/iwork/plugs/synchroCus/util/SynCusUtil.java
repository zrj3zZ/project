package com.iwork.plugs.synchroCus.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.htsc.soa.basesoapobject.v1.RequestHeaderType;
import com.htsc.soa.custmanage.v10.investmentcourtaccounttype.InvestmentCourtAccountType;
import com.htsc.soa.service.queryinvestmentcourtaccount.QueryInvestmentCourtAccountPtt;
import com.htsc.soa.service.queryinvestmentcourtaccount.QueryInvestmentCourtAccountReq;
import com.htsc.soa.service.queryinvestmentcourtaccount.QueryInvestmentCourtAccountResp;
import com.htsc.soa.service.queryinvestmentcourtaccount.QueryInvestmentCourtAccountService;
import com.htsc.soa.service.queryinvestmentcourtaccount.QueryInvestmentCourtAccountReq.RequestBody;
import com.htsc.soa.service.queryinvestmentcourtaccount.QueryInvestmentCourtAccountResp.ResponseBody;
import com.iwork.core.db.DBUtil;

public class SynCusUtil {

	private static Logger logger = Logger.getLogger(SynCusUtil.class);
	private static SynCusUtil instance = null;

	private SynCusUtil() {

	}

	public static SynCusUtil getInstance() {
		if (instance == null) {
			instance = new SynCusUtil();
		}
		return instance;
	}
	//通过查询数据库获取客户信息
	public List<HashMap> GetNewCus() {
		List<HashMap> list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,t.Num from cust_neeq_info a join( SELECT COUNT(1) Num,X_SECURITIES_CODE FROM cust_neeq_info group by X_SECURITIES_CODE) t on a.X_SECURITIES_CODE=t.X_SECURITIES_CODE");
		ResultSet rset = null;
		PreparedStatement sqlObj = null;
		Connection dbConn = null;
		try {
			try {
				dbConn = DBUtil.open();
			} catch (Exception e) {
				logger.error(e,e);
				return list;
			}
			sqlObj = dbConn.prepareStatement(sql.toString());
			rset = sqlObj.executeQuery();
			while (rset.next()) {
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("ROW_ID", rset.getString("ROW_ID"));
				hash.put("ALIAS_NAME", rset.getString("ALIAS_NAME"));
				hash.put("LAST_NAME", rset.getString("LAST_NAME"));
				hash.put("MAIN_BUSINESS", rset.getString("MAIN_BUSINESS"));
				hash.put("CELL_PH_NUM", rset.getString("CELL_PH_NUM"));
				hash.put("EMAIL_ADDR", rset.getString("EMAIL_ADDR"));
				hash.put("X_SECURITIES_CODE",
						rset.getString("X_SECURITIES_CODE"));
				hash.put("BELONG_INDUSTRY", rset.getString("BELONG_INDUSTRY"));
				hash.put("REGISTERED_CAPITAL",
						rset.getBigDecimal("REGISTERED_CAPITAL"));
				hash.put("CAMPANY_MAIN_PROD",
						rset.getString("CAMPANY_MAIN_PROD"));
				hash.put("COMPETITIVE_EDGE", rset.getString("COMPETITIVE_EDGE"));// 公司竞争优势
				hash.put("EXP_YEAR_RECEIVABLE",
						rset.getBigDecimal("EXP_YEAR_RECEIVABLE"));
				hash.put("NET_INCOME", rset.getBigDecimal("NET_INCOME"));
				hash.put("PROFIT_MODEL", rset.getString("PROFIT_MODEL"));
				hash.put("RTE_TO_MKT_DESC", rset.getString("RTE_TO_MKT_DESC"));
				hash.put("Listing_time", rset.getDate("LISTING_TIME"));
				hash.put("ESTABLISH_DT", rset.getDate("ESTABLISH_DT"));
				hash.put("LTD_ESTABLISH_DT", rset.getDate("LTD_ESTABLISH_DT"));
				hash.put("REMARK", rset.getString("REMARK"));
				hash.put("GRWTH_STRTGY_DESC",
						rset.getString("GRWTH_STRTGY_DESC"));
				hash.put("DIST_OP_DESC", rset.getString("DIST_OP_DESC"));
				hash.put("SHORT_NAME", rset.getString("SHORT_NAME"));
				hash.put("ENG_NAME", rset.getString("ENG_NAME"));
				hash.put("FRGHT_TERMS_INFO", rset.getString("FRGHT_TERMS_INFO"));
				hash.put("X_WEBSITE_ADDR", rset.getString("X_WEBSITE_ADDR"));
				hash.put("LEGAL_PERSON", rset.getString("LEGAL_PERSON"));
				hash.put("MAIN_PH_NUM", rset.getString("MAIN_PH_NUM"));
				hash.put("MAIN_FAX_PH_NUM", rset.getString("MAIN_FAX_PH_NUM"));
				hash.put("X_REG_CITY", rset.getString("X_REG_CITY"));
				hash.put("X_POSTAL_CODE", rset.getString("X_POSTAL_CODE"));
				hash.put("X_MAJORITY", rset.getString("X_MAJORITY"));
				hash.put("X_RATIO", rset.getString("X_RATIO"));
				hash.put("X_LISTED_COMPANY_FLG",
						rset.getString("X_LISTED_COMPANY_FLG"));
				hash.put("OFFICE_ADDR", rset.getString("OFFICE_ADDR"));
				hash.put("X_REG_STATE", rset.getString("X_REG_STATE"));
				hash.put("DATA_DT", rset.getString("DATA_DT"));
				hash.put("NUM", rset.getInt("NUM"));
				list.add(hash);
			}
		} catch (Exception ex) {
			logger.error("读取数据库客户信息错误" + ex.toString()
					+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())
					+ "方法：GetNewCus()");
		} finally {
			DBUtil.close(dbConn, sqlObj, rset);
		}
		return list;

	}
	//通过调用WebService获取客户信息。
	public List<HashMap> GetNewCusByService() {
		List<HashMap> list = new ArrayList();
		try {
			QueryInvestmentCourtAccountService qict = new QueryInvestmentCourtAccountService();
			QueryInvestmentCourtAccountPtt port = qict
					.getQueryInvestmentCourtAccountPort();
			QueryInvestmentCourtAccountReq req = new QueryInvestmentCourtAccountReq();
			RequestHeaderType rtype = new RequestHeaderType();
			// header验证都是必填项
			rtype.setReqNo("");
			rtype.setOrgId("");
			rtype.setEmpId("");
			rtype.setTranTime("");
			rtype.setServerId("");
			rtype.setEmpId("");
			rtype.setSysId("102345");
			rtype.setUserToken("53A8FE551A548FD71DC97F16CD1C0A27");
			rtype.setServiceId("CrmQueryInvestmentCourtAccount");
			req.setRequestHeader(rtype);
			Integer StartNum = 0;
			boolean bl = true;
			while (bl) {
				RequestBody rbody = new RequestBody();
				rbody.setAccountName("");
				rbody.setStartRowNum(StartNum.toString());
				req.setRequestBody(rbody);
				QueryInvestmentCourtAccountResp cQIC = port
						.crmQueryInvestmentCourtAccount(req);
				ResponseBody rsb = cQIC.getResponseBody();
				List<InvestmentCourtAccountType> iCA = rsb
						.getInvestmentCourtAccount();
				List<HashMap> r_list = analysisObjforIndex(iCA);
				list.addAll(r_list);
				if (r_list.size() < 500) {
					bl = false;
					break;
				}
				StartNum += 500;
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return list;
	}
	//序列化 返回的数据。
	public synchronized static List<HashMap> analysisObjforIndex(
			List<InvestmentCourtAccountType> itcats) {
		List<HashMap> companymsglist = new ArrayList<HashMap>();
		try {
			/*
			 * 遍历集合，获取每一个<emp>标签
			 */
			for (InvestmentCourtAccountType element : itcats) {
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("ROW_ID", element.getRowId());
				hash.put("ALIAS_NAME", element.getAccountName()); // 公司全称
				hash.put("LAST_NAME", element.getContactName()); // 客户联系人
				hash.put("MAIN_BUSINESS", element.getMainBusiness()); // 公司主营业务
				hash.put("CELL_PH_NUM", element.getContactMobilePhone()); // 客户联系电话
				hash.put("EMAIL_ADDR", element.getContactEmai()); // 客户联系邮箱
				hash.put("X_SECURITIES_CODE", element.getSecuritiesCode()); // 证券代码
				hash.put("BELONG_INDUSTRY", element.getIndustry()); // 所属行业
				hash.put("REGISTERED_CAPITAL", element.getRegisteredCapital()); // 注册资本
				hash.put("CAMPANY_MAIN_PROD", element.getMainProduct()); // 主要产品
				hash.put("COMPETITIVE_EDGE", element.getSuperiority());// 公司竞争优势
				hash.put("EXP_YEAR_RECEIVABLE", element.getAnnualReceivable()); // 预计年度应收
				hash.put("NET_INCOME", element.getNetProfit()); // 净利润
				hash.put("PROFIT_MODEL", element.getProfitModel()); // 盈利模式
				hash.put("RTE_TO_MKT_DESC", element.getHistory()); // 历史沿革
				hash.put("Listing_time", element.getListingDate()); // 挂牌时间
				hash.put("ESTABLISH_DT", element.getFoundedDate());// 股份公司成立日期
				hash.put("LTD_ESTABLISH_DT", element.getLimitedFoundedDate()); // 有限公司成立日期
				hash.put("REMARK", element.getComments()); // 备注
				hash.put("GRWTH_STRTGY_DESC", element.getDescription()); // 公司概况
				hash.put("DIST_OP_DESC", element.getRegAddress()); // 注册地址
				hash.put("SHORT_NAME", element.getShortName()); // 公司简称
				hash.put("ENG_NAME", element.getEnglishName()); // 英文名称
				hash.put("FRGHT_TERMS_INFO", element.getFreightTermsInfo()); // 经营许可项目
				hash.put("X_WEBSITE_ADDR", element.getWebsiteAddress()); // 公司网址
				hash.put("LEGAL_PERSON", element.getLegalContact()); // 法人
				hash.put("MAIN_PH_NUM", element.getMainPhoneNumber()); // 电话
				hash.put("MAIN_FAX_PH_NUM", element.getMainFaxNumber()); // 传真
				hash.put("X_REG_CITY", element.getRegCity()); // 注册城市
				hash.put("X_POSTAL_CODE", element.getPostalCode()); // 邮编
				hash.put("X_MAJORITY", element.getMajorityShareholder()); // 控股股东
				hash.put("X_RATIO", element.getRatio()); // 持股比例
				hash.put("X_LISTED_COMPANY_FLG", element.getListedCompanyFlag()); // 是否挂牌
				hash.put("OFFICE_ADDR", element.getOfficeAddress()); // 办公地址
				hash.put("X_REG_STATE", element.getRegState()); // 注册省份
				// hash.put("DATA_DT", element.elementText("DATA_DT"));
				// hash.put("NUM", element.elementText("NUM"));
				// 存入集合
				companymsglist.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return companymsglist;
	}
	// 更新挂牌公司是否挂牌的状态
	public int SetYGP(long KeyID) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BD_ZQB_KH_BASE SET YGP='未挂牌' WHERE ID =?");
		int Res = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setLong(1, KeyID);
			Res = stmt.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return Res;
	}
	// 添加关系 KH_ID：新三板对应客户编号
	// HT_KH_ID 华泰对应客户编号
	public int RelationsAdd(long KH_ID, String HT_KH_ID) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO HT_KH_RELATIONS(HK_ID,HT_KH_ID)VALUES(?,?)");
		int Res = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setLong(1, KH_ID);
			stmt.setString(2, HT_KH_ID);
			Res = stmt.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return Res;
	}
	// 获取关系
	public List<HashMap> isExistRelations(String HT_KH_ID) {
		StringBuffer sql = new StringBuffer();
		List list = new ArrayList();
		sql.append("SELECT HK_ID,bd_zqb_kh_base.YGP,bd_zqb_kh_base.CUSTOMERNO from HT_KH_RELATIONS JOIN bd_zqb_kh_base "
				+ " ON bd_zqb_kh_base.ID=HT_KH_RELATIONS.HK_ID"
				+ " where HT_KH_ID=?");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, HT_KH_ID);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String HK_ID = rset.getString("HK_ID");
				String YGP = rset.getString("YGP");
				String CUSTOMERNO = rset.getString("CUSTOMERNO");
				HashMap hash = new HashMap();
				hash.put("HK_ID", HK_ID);
				hash.put("YGP", YGP);
				hash.put("CUSTOMERNO", CUSTOMERNO);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	// 更新部门的名称
	public int UpdateDepartment(String GSName, String KHBH) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE orgdepartment SET departmentname= ? Where departmentNO =?");
		int Res = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, GSName);
			stmt.setString(2, KHBH);
			Res = stmt.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return Res;
	}
	// 更新持续督导分派记录中的公司名字
	public int UpdateCXDD(String GSName, String KHBH) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BD_MDM_KHQXGLB SET KHMC= ? Where KHBH =?");
		int Res = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, GSName);
			stmt.setString(2, KHBH);
			Res = stmt.executeUpdate();
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return Res;
	}
	// 查询三板数据库中的多余数据
	public List<HashMap> GetErrorInfo() {
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT BD_ZQB_KH_BASE.ID,BD_ZQB_KH_BASE.CUSTOMERNAME, HT_KH_RELATIONS.HK_ID  FROM BD_ZQB_KH_BASE "
				+ "LEFT JOIN HT_KH_RELATIONS ON BD_ZQB_KH_BASE.ID=HT_KH_RELATIONS.HK_ID "
				+ "WHERE HT_KH_RELATIONS.HK_ID IS NULL");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String ID = rset.getString("ID");
				String CUSTOMERNAME = rset.getString("CUSTOMERNAME");
				String HK_ID = rset.getString("HK_ID");
				HashMap hash = new HashMap();
				hash.put("ID", ID);
				hash.put("CUSTOMERNAME", CUSTOMERNAME);
				hash.put("HK_ID", HK_ID);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	// 判断客户是否存在根据客户编号
	public List<HashMap> IsExistsKHByNO(String CUSTOMERNO) {
		StringBuffer sql = new StringBuffer();
		List list = new ArrayList();
		sql.append("SELECT ID from BD_ZQB_KH_BASE where CUSTOMERNO=?");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, CUSTOMERNO);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String ID = rset.getString("ID");
				HashMap hash = new HashMap();
				hash.put("ID", ID);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	
	//xlj add 2016年6月14日10:19:03 使用客户全称判断客户是否存在
	// 获取关系
		public List<HashMap> isExist(String HT_KH_Name) {
			StringBuffer sql = new StringBuffer();
			List list = new ArrayList();
			sql.append("SELECT ID,YGP,CUSTOMERNO bd_zqb_kh_base where CUSTOMERNAME=?");
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rset = null;
			try {
				conn = DBUtil.open();
				stmt = conn.prepareStatement(sql.toString());
				stmt.setString(1, HT_KH_Name);
				rset = stmt.executeQuery();
				while (rset.next()) {
					String ID = rset.getString("ID");
					String YGP = rset.getString("YGP");
					String CUSTOMERNO = rset.getString("CUSTOMERNO");
					HashMap hash = new HashMap();
					hash.put("HK_ID", ID);
					hash.put("YGP", YGP);
					hash.put("CUSTOMERNO", CUSTOMERNO);
					list.add(hash);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rset);
			}
			return list;
		}
}
