package com.iwork.plugs.synchroCus.event;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.notice.util.NoticeUtil;
import com.iwork.plugs.synchroCus.util.SynCusUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.OrganizationAPI;

public class SynCusEvent implements IWorkScheduleInterface {

	private static Logger logger = Logger.getLogger(SynCusEvent.class);
	private String regex="^(-?\\d+[.]?\\d+)$|^(-?\\d+)$";

	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-即将开始客户信息同步......");
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-即将开始客户信息同步...... ");
		SynchroCus();
		return true;
	}

	public boolean executeAfter() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-客户信息同步完毕...... ");
		return true;
	}

	private void SynchroCus() {
		try {
			int UpdateNum=0;
			// 从华泰的MSSql数据库中读取客户数据源			
			List<HashMap> list = SynCusUtil.getInstance().GetNewCusByService();
			// 读取出来处理数据
			for (HashMap ht : list) {
				String ALIAS_NAME = ht.get("ALIAS_NAME").toString().trim(); // 客户名称
				String X_SECURITIES_CODE = ht.get("X_SECURITIES_CODE")
						.toString().trim(); // 证券代码
				// 如果客户名称或公司代码为空，跳过。||X_SECURITIES_CODE==""
				if (ALIAS_NAME == "") {
					continue;
				}			
				// 公司名称过长，记录日志并提示
				if (ALIAS_NAME.length() > 64) {
					logger.error("公司全称:【"
							+ ALIAS_NAME
							+ "】证券代码:【"
							+ X_SECURITIES_CODE
							+ "】名称长度过长，超过64个字符，请检查原始数据。"
							+ "方法：SynchroCus()");
					continue;
				}				
				try {
					// 根据主键ID判断是否存在该关系 xlj 注掉，2016年6月14日10:16:13，查看关联数据发现华泰的row_id并不是唯一建,判断重复的方法改为使用客户全称
//					List<HashMap> listHK = SynCusUtil.getInstance().isExistRelations(ht.get("ROW_ID").toString());
					List<HashMap> listHK = SynCusUtil.getInstance().isExist(ALIAS_NAME);
					// 若存在，更新记录
					if (listHK.size() > 0) {
						// ID,YGP,CUSTOMERNO
						long ID = Long.parseLong(listHK.get(0).get("HK_ID")
								.toString());
						String YGP = listHK.get(0).get("YGP").toString();
						String CUSTOMERNO = listHK.get(0).get("CUSTOMERNO")
								.toString();
						String bl = "";
						// 三板中是未挂牌，MSSql为已挂牌，需要更新状态以及添加其他信息等
						if (YGP == "未挂牌" && ht.get("X_LISTED_COMPANY_FLG").toString() == "Y") {
							if (X_SECURITIES_CODE == "") {
								logger.error("公司全称:【"+ ALIAS_NAME+ "】已挂牌，但证券代码为空。"+ "方法：SynchroCus()");
								continue;
							}		
							bl = SetHashMap(ht, "2", "1", ID, CUSTOMERNO);
						} else {
							bl = SetHashMap(ht, "2", "2", ID, CUSTOMERNO);
						}					
					}
					// 不存在
					else {		
							//xlj 2016年6月14日10:46:51注掉，未挂牌的应该也能导入
//							if(!"Y".equals(ht.get("X_LISTED_COMPANY_FLG").toString()))
//							{
//								continue;
//							}
							String bl = SetHashMap(ht, "1", "1", 0L, "");
							//xlj 2016年6月14日10:48:27 注掉，逻辑更改，不需要了
//							if (bl!="") {
//								String ID=SynCusUtil.getInstance().IsExistsKHByNO(bl).get(0).get("ID").toString();								
//								SynCusUtil.getInstance().RelationsAdd(Long.parseLong(ID), ht.get("ROW_ID").toString());							
//							}

						}					

				} catch (Exception e) {
					logger.error(e,e);
					System.out.println("公司全称:【" + ALIAS_NAME + "】证券代码:【"
							+ X_SECURITIES_CODE + "】更新失败"
							+ UtilDate.getNowdate());
				}
				UpdateNum++;
			}
			System.out.println("本次共同步"+UpdateNum+"条记录，时间："+UtilDate.getNowdate());
		} 
		catch (Exception e) {
			logger.error(e,e);
			System.out.println("客户信息同步失败；Fun【SynchroCus】"+e.getMessage()
					+ UtilDate.dateFormat(new Date()));
		}

	}

	// 添加
	// iType_KH 客户信息主表 1:添加 2:修改
	// iType_Other 其他信息  如部门 用户 持续督导分派 1:添加 2:修改
	// 客户表主键ID，修改时，需要该值
	// 客户编号 修改时，需要该值
	private String SetHashMap(HashMap<String, Object> ht, String iType_KH,
			String iType_Other, long KeyID, String CustomerNo) {
		String bl = "";
		try {
			// 获取 客户主数据维护UUID
			String KH_UUID = NoticeUtil.getInstance().GetUUID("客户主数据维护");
			String ALIAS_NAME = ht.get("ALIAS_NAME")==null?"":ht.get("ALIAS_NAME").toString().trim();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String LAST_NAME = ht.get("LAST_NAME") == null ? "" : ht.get(
					"LAST_NAME").toString().trim(); // 客户联系人
			String MAIN_BUSINESS = ht.get("MAIN_BUSINESS") == null ? "" : ht
					.get("MAIN_BUSINESS").toString(); // 主营业务
			String CELL_PH_NUM = ht.get("CELL_PH_NUM") == null ? "" : ht.get(
					"CELL_PH_NUM").toString(); // 客户联系电话
			String EMAIL_ADDR = ht.get("EMAIL_ADDR") == null ? "" : ht.get(
					"EMAIL_ADDR").toString(); // 客户联系邮箱
			String X_SECURITIES_CODE = ht.get("X_SECURITIES_CODE") == null ? ""
					: ht.get("X_SECURITIES_CODE").toString().trim(); // 证券代码
			String BELONG_INDUSTRY = ht.get("BELONG_INDUSTRY") == null ? ""
					: ht.get("BELONG_INDUSTRY").toString(); // 所属行业
			Double REGISTERED_CAPITAL = ht.get("REGISTERED_CAPITAL") == null ? 0
					: new BigDecimal(Double.parseDouble(ht.get("REGISTERED_CAPITAL").toString().trim().matches(regex)?ht.get("REGISTERED_CAPITAL").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("100000000"))==1?new BigDecimal(Double.parseDouble(ht.get("REGISTERED_CAPITAL").toString().trim().matches(regex)?ht.get("REGISTERED_CAPITAL").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("0.0001")).doubleValue():new BigDecimal(Double.parseDouble(ht.get("REGISTERED_CAPITAL").toString().trim().matches(regex)?ht.get("REGISTERED_CAPITAL").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); // 注册资本
			String CAMPANY_MAIN_PROD = ht.get("CAMPANY_MAIN_PROD") == null ? ""
					: ht.get("CAMPANY_MAIN_PROD").toString(); // 主要产品
			String COMPETITIVE_EDGE = ht.get("COMPETITIVE_EDGE") == null ? ""
					: ht.get("COMPETITIVE_EDGE").toString(); // 公司竞争优势
			Double EXP_YEAR_RECEIVABLE = ht.get("EXP_YEAR_RECEIVABLE") == null ? 0
					: new BigDecimal(Double.parseDouble(ht.get("EXP_YEAR_RECEIVABLE").toString().trim().matches(regex)?ht.get("EXP_YEAR_RECEIVABLE").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("100000000"))==1?new BigDecimal(Double.parseDouble(ht.get("EXP_YEAR_RECEIVABLE").toString().trim().matches(regex)?ht.get("EXP_YEAR_RECEIVABLE").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("0.0001")).doubleValue(): new BigDecimal(Double.parseDouble(ht.get("EXP_YEAR_RECEIVABLE").toString().trim().matches(regex)?ht.get("EXP_YEAR_RECEIVABLE").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); // 预计年度应收
			Double NET_INCOME = ht.get("NET_INCOME") == null ? 0 : new BigDecimal(Double.parseDouble(ht.get("NET_INCOME").toString().trim().matches(regex)?ht.get("NET_INCOME").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal("100000000"))==1?new BigDecimal(Double.parseDouble(ht.get("NET_INCOME").toString().trim().matches(regex)?ht.get("NET_INCOME").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("0.0001")).doubleValue(): new BigDecimal(Double.parseDouble(ht.get("NET_INCOME").toString().trim().matches(regex)?ht.get("NET_INCOME").toString().trim():"0")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); // 净利润
			String PROFIT_MODEL = ht.get("PROFIT_MODEL") == null ? "" : ht.get(
					"PROFIT_MODEL").toString(); // 盈利模式
			String RTE_TO_MKT_DESC = ht.get("RTE_TO_MKT_DESC") == null ? ""
					: ht.get("RTE_TO_MKT_DESC").toString(); // 历史沿革
			String REMARK = ht.get("REMARK") == null ? "" : ht.get("REMARK")
					.toString(); // 备注
			String GRWTH_STRTGY_DESC = ht.get("GRWTH_STRTGY_DESC") == null ? ""
					: ht.get("GRWTH_STRTGY_DESC").toString(); // 公司概况
			String DIST_OP_DESC = ht.get("DIST_OP_DESC") == null ? "" : ht.get(
					"DIST_OP_DESC").toString(); // 注册地址
			String SHORT_NAME = ht.get("SHORT_NAME") == null ? "" : ht.get(
					"SHORT_NAME").toString().trim(); // 证券简称
			String ENG_NAME = ht.get("ENG_NAME") == null ? "" : ht.get(
					"ENG_NAME").toString(); // 英文名称
			String FRGHT_TERMS_INFO = ht.get("FRGHT_TERMS_INFO") == null ? ""
					: ht.get("FRGHT_TERMS_INFO").toString(); // 经营许可项目
			String X_WEBSITE_ADDR = ht.get("X_WEBSITE_ADDR") == null ? "" : ht
					.get("X_WEBSITE_ADDR").toString(); // 公司网址
			String LEGAL_PERSON = ht.get("LEGAL_PERSON") == null ? "" : ht.get(
					"LEGAL_PERSON").toString(); // 法定代表人
			String MAIN_PH_NUM = ht.get("MAIN_PH_NUM") == null ? "" : ht.get(
					"MAIN_PH_NUM").toString(); // 电话
			String MAIN_FAX_PH_NUM = ht.get("MAIN_FAX_PH_NUM") == null ? ""
					: ht.get("MAIN_FAX_PH_NUM").toString(); // 传真
			String X_REG_CITY = ht.get("X_REG_CITY") == null ? "" : ht.get(
					"X_REG_CITY").toString(); // 注册地市
			String X_POSTAL_CODE = ht.get("X_POSTAL_CODE") == null ? "" : ht
					.get("X_POSTAL_CODE").toString(); // 邮编
			String X_MAJORITY = ht.get("X_MAJORITY") == null ? "" : ht.get(
					"X_MAJORITY").toString(); // 控股股东
			String X_RATIO = ht.get("X_RATIO") == null ? "" : ht.get("X_RATIO")
					.toString(); // 持股比例
			String X_LISTED_COMPANY_FLG = ht.get("X_LISTED_COMPANY_FLG") == null ? ""
					: ht.get("X_LISTED_COMPANY_FLG").toString(); // 已挂牌
			String OFFICE_ADDR = ht.get("OFFICE_ADDR") == null ? "" : ht.get(
					"OFFICE_ADDR").toString(); // 办公地址
			String X_REG_STATE = ht.get("X_REG_STATE") == null ? "" : ht.get(
					"X_REG_STATE").toString(); // 注册省份
			// -------------//
			X_LISTED_COMPANY_FLG = "Y".equals(X_LISTED_COMPANY_FLG
					.toUpperCase()) ? "是" : "否";
			String KHBH = "";
			// 添加
			if (iType_KH == "1") {
				HashMap<String, Object> hash = new HashMap<String, Object>();
				hash.put("CUSTOMERNAME", ALIAS_NAME);
				hash.put("USERNAME", LAST_NAME);
				hash.put("ZYYW", MAIN_BUSINESS);
				hash.put("TEL", CELL_PH_NUM);
				hash.put("EMAIL", EMAIL_ADDR);
				hash.put("ZQDM", X_SECURITIES_CODE);
				hash.put("SSHY", BELONG_INDUSTRY);
				hash.put("ZCZB", REGISTERED_CAPITAL);
				hash.put("ZYCP", CAMPANY_MAIN_PROD);
				hash.put("JZYS", COMPETITIVE_EDGE);
				hash.put("NDYS", EXP_YEAR_RECEIVABLE);
				hash.put("JLR", NET_INCOME);
				hash.put("SLQK", PROFIT_MODEL);
				hash.put("CZJGQBD", RTE_TO_MKT_DESC);
				// ------
				// 时间：挂牌时间
				if (ht.get("Listing_time") != null&&!ht.get("Listing_time").toString().trim().equals("")) {
					Date Listing_time = sdf.parse(ht.get("Listing_time")
							.toString().trim().replace("/", "-"));
					hash.put("GPSJ", Listing_time);
				}
				// 时间：股份公司成立日期
				if (ht.get("ESTABLISH_DT") != null&&!ht.get("ESTABLISH_DT").toString().trim().equals("")) {
					Date ESTABLISH_DT = sdf.parse(ht.get("ESTABLISH_DT")
							.toString().trim().replace("/", "-"));
					hash.put("GFGSRQ", ESTABLISH_DT);
				}
				// 时间：有限公司成立日期
				if (ht.get("LTD_ESTABLISH_DT") != null&&!ht.get("LTD_ESTABLISH_DT").toString().trim().equals("")) {
					Date LTD_ESTABLISH_DT = sdf.parse(ht.get("LTD_ESTABLISH_DT").toString().trim().replace("/", "-"));
					hash.put("YXGSRQ", LTD_ESTABLISH_DT);
				}
				// ------
				hash.put("MEMO", REMARK);
				hash.put("CUSTOMERDESC", GRWTH_STRTGY_DESC);
				hash.put("ZCDZ", DIST_OP_DESC);
				hash.put("ZQJC", SHORT_NAME);
				hash.put("YWMC", ENG_NAME);
				hash.put("JYXKXM", FRGHT_TERMS_INFO);
				hash.put("GSWZ", X_WEBSITE_ADDR);
				hash.put("FDDBR", LEGAL_PERSON);
				hash.put("PHONE", MAIN_PH_NUM);
				hash.put("FAX", MAIN_FAX_PH_NUM);
				hash.put("ZWMC", X_REG_CITY);
				hash.put("POST", X_POSTAL_CODE);
				hash.put("KGGD", X_MAJORITY);
				hash.put("CGBL", X_RATIO);
				hash.put("YGP", X_LISTED_COMPANY_FLG);
				hash.put("BGDZ", OFFICE_ADDR);
				hash.put("TYPE", X_REG_STATE);
				// 时间：创建时间
				hash.put("CREATEDATE", UtilDate.dateFormat(new Date()));
				
				// 获取新的客户编号
				int Seq = 0;
				String str = NoticeUtil.getInstance().GetCustomerNo();
				String[] strArr = str.split(",");
				KHBH = strArr[0];
				Seq = Integer.parseInt(strArr[1]);
				hash.put("CUSTOMERNO", KHBH);
				hash.put("CREATEUSER", "超级管理员");
				hash.put("STATUS", "有效");
				//xlj update 2017年4月18日13:57:29 表结构更改后需要处理的内容
				hash.put("USERID", "NEEQMANAGER");
				hash.put("ZHXGR", "NEEQMANAGER[超级管理员]");
				hash.put("ZHXGSJ", UtilDate.dateFormat(new Date()));
				// 添加客户信息
				Long newInstanceId = DemAPI.getInstance().newInstance(KH_UUID,SecurityUtil.supermanager);
				DemAPI.getInstance().saveFormData(KH_UUID, newInstanceId, hash, false);
				// 更新序列表中的值
				NoticeUtil.getInstance().UpdateSeqValue("BPM:0", Seq);
				bl=KHBH;
			}
			// 修改
			else {
				HashMap h = new HashMap();
				h.put("CUSTOMERNO", CustomerNo);
				List<HashMap> list = DemAPI.getInstance().getList(KH_UUID, h,
						null);
				HashMap formData = list.get(0);
				if (formData.get("USERNAME") == null
						|| formData.get("USERNAME").toString().trim().equals("")) {
					formData.put("USERNAME", LAST_NAME);
				}
				if (formData.get("ZYYW") == null
						|| formData.get("ZYYW").toString().trim().equals("")) {
					formData.put("ZYYW", MAIN_BUSINESS);
				}
				if (formData.get("TEL") == null
						|| formData.get("TEL").toString().trim().equals("")) {
					formData.put("TEL", CELL_PH_NUM);
				}
				if (formData.get("EMAIL") == null
						|| formData.get("EMAIL").toString().trim().equals("")) {
					formData.put("EMAIL", EMAIL_ADDR);
				}			
				if (formData.get("SSHY") == null
						|| formData.get("SSHY").toString().trim().equals("")) {
					formData.put("SSHY", BELONG_INDUSTRY);
				}
				if (formData.get("ZCZB") == null
						|| formData.get("ZCZB").toString().trim().equals("0")) {
					formData.put("ZCZB", REGISTERED_CAPITAL);
				}
				if (formData.get("ZYCP") == null
						|| formData.get("ZYCP").toString().trim().equals("")) {
					formData.put("ZYCP", CAMPANY_MAIN_PROD);
				}
				if (formData.get("JZYS") == null
						|| formData.get("JZYS").toString().trim().equals("")) {
					formData.put("JZYS", COMPETITIVE_EDGE);
				}
				if (formData.get("NDYS") == null
						|| formData.get("NDYS").toString().trim().equals("0")) {
					formData.put("NDYS", EXP_YEAR_RECEIVABLE);
				}
				if (formData.get("JLR") == null
						|| formData.get("JLR").toString().trim().equals("0")) {
					formData.put("JLR", NET_INCOME);
				}
				if (formData.get("SLQK") == null
						|| formData.get("SLQK").toString().trim().equals("")) {
					formData.put("SLQK", PROFIT_MODEL);
				}
				if (formData.get("CZJGQBD") == null
						|| formData.get("CZJGQBD").toString().trim().equals("")) {
					formData.put("CZJGQBD", RTE_TO_MKT_DESC);
				}
				// ------
				// 时间：挂牌时间
				if (formData.get("GPSJ") == null
						|| formData.get("GPSJ").toString().trim().equals("")) {
					if (ht.get("Listing_time") != null&&!ht.get("Listing_time").toString().trim().equals("")) {
						Date Listing_time = sdf.parse(ht.get("Listing_time").toString().trim().replace("/", "-"));
						formData.put("GPSJ", Listing_time);
					}
				}
				// 时间：股份公司成立日期
				if (formData.get("GFGSRQ") == null
						|| formData.get("GFGSRQ").toString().trim().equals("")) {
					if (ht.get("ESTABLISH_DT") != null&&!ht.get("ESTABLISH_DT").toString().trim().equals("")) {
						Date ESTABLISH_DT = sdf.parse(ht.get("ESTABLISH_DT").toString().trim().replace("/", "-"));
						formData.put("GFGSRQ", ESTABLISH_DT);
					}
				}
				// 时间：有限公司成立日期
				if (formData.get("YXGSRQ") == null
						|| formData.get("YXGSRQ").toString().trim().equals("")) {
					if (ht.get("LTD_ESTABLISH_DT") != null&&!ht.get("LTD_ESTABLISH_DT").toString().trim().equals("")) {
						Date LTD_ESTABLISH_DT = sdf.parse(ht.get("LTD_ESTABLISH_DT").toString().trim().replace("/", "-"));
						formData.put("YXGSRQ", LTD_ESTABLISH_DT);
					}
				}
				// ------
				if (formData.get("MEMO") == null
						|| formData.get("MEMO").toString().trim().equals("")) {
					formData.put("MEMO", REMARK);
				}
				if (formData.get("CUSTOMERDESC") == null
						|| formData.get("CUSTOMERDESC").toString().trim().equals("")) {
					formData.put("CUSTOMERDESC", GRWTH_STRTGY_DESC);
				}
				if (formData.get("ZCDZ") == null
						|| formData.get("ZCDZ").toString().trim().equals("")) {
					formData.put("ZCDZ", DIST_OP_DESC);
				}				
				
				if (formData.get("YWMC") == null
						|| formData.get("YWMC").toString().trim().equals("")) {
					formData.put("YWMC", ENG_NAME);
				}
				if (formData.get("JYXKXM") == null
						|| formData.get("JYXKXM").toString().trim().equals("")) {
					formData.put("JYXKXM", FRGHT_TERMS_INFO);
				}
				if (formData.get("GSWZ") == null
						|| formData.get("GSWZ").toString().trim().equals("")) {
					formData.put("GSWZ", X_WEBSITE_ADDR);
				}
				if (formData.get("FDDBR") == null
						|| formData.get("FDDBR").toString().trim().equals("")) {
					formData.put("FDDBR", LEGAL_PERSON);
				}
				if (formData.get("PHONE") == null
						|| formData.get("PHONE").toString().trim().equals("")) {
					formData.put("PHONE", MAIN_PH_NUM);
				}
				if (formData.get("FAX") == null
						|| formData.get("FAX").toString().trim().equals("")) {
					formData.put("FAX", MAIN_FAX_PH_NUM);
				}
				if (formData.get("ZWMC") == null
						|| formData.get("ZWMC").toString().trim().equals("")) {
					formData.put("ZWMC", X_REG_CITY);
				}
				if (formData.get("POST") == null
						|| formData.get("POST").toString().trim().equals("")) {
					formData.put("POST", X_POSTAL_CODE);
				}
				if (formData.get("KGGD") == null
						|| formData.get("KGGD").toString().trim().equals("")) {
					formData.put("KGGD", X_MAJORITY);
				}
				if (formData.get("CGBL") == null
						|| formData.get("CGBL").toString().trim().equals("")) {
					formData.put("CGBL", X_RATIO);
				}
				if (formData.get("YGP") == null
						|| formData.get("YGP").toString().trim().equals("")) {
					formData.put("YGP", X_LISTED_COMPANY_FLG);
				}
				if (formData.get("BGDZ") == null
						|| formData.get("BGDZ").toString().trim().equals("")) {
					formData.put("BGDZ", OFFICE_ADDR);
				}
				if (formData.get("TYPE") == null
						|| formData.get("TYPE").toString().trim().equals("")) {
					formData.put("TYPE", X_REG_STATE);
				}
				//更新证券简称和证券代码
				formData.put("ZQDM", X_SECURITIES_CODE);
				formData.put("ZQJC", SHORT_NAME);
				formData.put("CUSTOMERNAME", ALIAS_NAME);
				KHBH = CustomerNo;				
				long instanceid = Long.parseLong(formData.get("INSTANCEID")
						.toString());				
				//xlj update 2017年4月18日13:57:29 表结构更改后需要处理的内容
				formData.put("USERID", "NEEQMANAGER");
				formData.put("ZHXGR", "NEEQMANAGER[超级管理员]");
				formData.put("ZHXGSJ", UtilDate.dateFormat(new Date()));
				DemAPI.getInstance().updateFormData(KH_UUID, instanceid,
						formData, KeyID, false);
				bl=KHBH;
			}

			// 如果是已经挂牌的公司，需要添加部门。登录用户，持续督导分派记录
			if (X_LISTED_COMPANY_FLG.equals("是") && iType_Other.equals("1")) {
				// 保存完之后，获取保存的主键ID，即部门ID
				String DepID = DBUtil.getString(
						"select * from ORGDEPARTMENT where DEPARTMENTNO='"
								+ bl + "'", "ID");
				if(DepID==null||"".equals(DepID)){
					// ----------------部门添加----------
					OrgDepartment model = new OrgDepartment();
					model.setDepartmentname(ALIAS_NAME);
					model.setDepartmentno(KHBH);
					model.setCompanyid("2");
					model.setParentdepartmentid(new Long(51));
					model.setDepartmentstate("0");
					OrganizationAPI.getInstance().addDepartment(model);
					// 保存完之后，获取保存的主键ID，即部门ID
					DepID = DBUtil.getString(
							"select * from ORGDEPARTMENT where DEPARTMENTNO='"
									+ bl + "'", "ID");
				}
				// ----------------用户添加----------				
				OrgUser orguserModel = OrganizationAPI.getInstance().getOrguserModel(X_SECURITIES_CODE);
				if(orguserModel==null){
					String sql = "SELECT MAX(id)+1 ID FROM OrgUser ";
					String noStr = null;
					String ll = DBUtil.getString(sql, "ID");
					if (ll == null)
						noStr = "1";
					else {
						noStr = ll.toString();
					}
					if (noStr.length() == 1)
						noStr = "000" + noStr;
					else if (noStr.length() == 2)
						noStr = "00" + noStr;
					else if (noStr.length() == 3)
						noStr = "0" + noStr;
					else {
						noStr = noStr;
					}
					OrgUser orguser = new OrgUser();
					orguser.setUserid(X_SECURITIES_CODE);
					orguser.setUsername(LAST_NAME);
					orguser.setPassword("E10ADC3949BA59ABBE56E057F20F883E");
					orguser.setDepartmentid(Long.parseLong(DepID));
					orguser.setDepartmentname(ALIAS_NAME);
					orguser.setOrgroleid(3L);
					orguser.setIsmanager(0L);
					orguser.setMobile(""); // 手机号码???????????
					orguser.setEmail(EMAIL_ADDR);
					orguser.setExtend1(KHBH);
					orguser.setExtend2(ALIAS_NAME);
					orguser.setUsertype(0L);
					orguser.setStartdate(sdf.parse("2014-01-01"));
					orguser.setEnddate(sdf.parse("2099-12-31"));
					orguser.setUserno(noStr);
					orguser.setUserstate(0L);
					orguser.setCompanyid(2L);
					orguser.setCompanyname("华泰证券股份有限公司");
					OrganizationAPI.getInstance().addUser(orguser);
				}

				// --------------------持续督导------------------------
				String strKHBH = DBUtil.getString(
						"select * from bd_mdm_khqxglb where KHBH='"
								+ bl + "'", "KHBH");				
				if(strKHBH==null||"".equals(strKHBH)){				
					String CXDD_UUID = NoticeUtil.getInstance().GetUUID("持续督导分派");
					HashMap<String, Object> ht_CXDD = new HashMap<String, Object>();
					ht_CXDD.put("KHBH", KHBH);
					ht_CXDD.put("KHMC", ALIAS_NAME);
					ht_CXDD.put("TXDF", "否");
					ht_CXDD.put("SFFP", "是");
					Long newInstanceId_CXDD = DemAPI.getInstance().newInstance(
							CXDD_UUID, SecurityUtil.supermanager);
					DemAPI.getInstance().saveFormData(CXDD_UUID,
							newInstanceId_CXDD, ht_CXDD, false);
				}
			} else if (X_LISTED_COMPANY_FLG.equals("是") && iType_Other.equals("2")) {
				SynCusUtil.getInstance().UpdateDepartment(ALIAS_NAME, bl);
				SynCusUtil.getInstance().UpdateCXDD(ALIAS_NAME, bl);
			}
//			bl = KHBH;
		} catch (Exception ex) {
			System.out.println("客户信息同步异常；Fun【SetHashMap】" + ex.getMessage()
					+ UtilDate.getNowDatetime());
			bl = "";
		}
		return bl;
	}

}
