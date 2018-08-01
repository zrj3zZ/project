package com.ibpmsoft.project.zqb.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import freemarker.core.TemplateElement;

public class ZQBNoticeUtil {
	private FreeMarkerConfigurer mailfreemarderConfig;
	private static final String uuid = "6941de10e9f7403f828886314ea46334";
	private static Object lock = new Object();
	private static ZQBNoticeUtil instance;

	public static ZQBNoticeUtil getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ZQBNoticeUtil();
				}
			}
		}
		return instance;
	}

	public String getNoticeUserId(String roleId) {
		// 获取市场部总经理
		String sql = "select * from OrgUser  where orgroleid =  ? " ;
		Map params = new HashMap();
		params.put(1, roleId);
		String userid = DBUTilNew.getDataStr("USERID", sql, params);
		return userid;
	}

	/**
	 * 获得指定客户的持续督导专员
	 * 
	 * @return
	 */
	public String getQYNBSHer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			userid = hash.get("QYNBRYSH").toString();
			if (userid == null || "".equals(userid)) {
				userid = hash.get("KHFZR").toString();
				if (userid == null || "".equals(userid)) {
					userid = hash.get("ZZCXDD").toString();
					if (userid == null || "".equals(userid)) {
						userid = hash.get("FHSPR").toString();
						if (userid == null || "".equals(userid)) {
							userid = hash.get("ZZSPR").toString();
							if (userid == null || "".equals(userid)) {
								userid = hash.get("CWSCBFZR2").toString();
								if (userid == null || "".equals(userid)) {
									userid = hash.get("CWSCBFZR3").toString();
								}
							}
						}
					}
				}
			}
		}
		return userid;
	}

	public String getDuDaoCustomer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			userid = hash.get("KHFZR").toString();
			if (userid == null || "".equals(userid)) {
				userid = hash.get("ZZCXDD").toString();
				if (userid == null || "".equals(userid)) {
					userid = hash.get("FHSPR").toString();
					if (userid == null || "".equals(userid)) {
						userid = hash.get("ZZSPR").toString();
						if (userid == null || "".equals(userid)) {
							userid = hash.get("CWSCBFZR2").toString();
							if (userid == null || "".equals(userid)) {
								userid = hash.get("CWSCBFZR3").toString();
							}
						}
					}
				}
			}
		}
		return userid;
	}

	public String getTouHangCustomer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			userid = hash.get("ZZCXDD").toString();
			if (userid == null || "".equals(userid)) {
				userid = hash.get("FHSPR").toString();
				if (userid == null || "".equals(userid)) {
					userid = hash.get("ZZSPR").toString();
					if (userid == null || "".equals(userid)) {
						userid = hash.get("CWSCBFZR2").toString();
						if (userid == null || "".equals(userid)) {
							userid = hash.get("CWSCBFZR3").toString();
						}
					}
				}
			}
		}
		return userid;
	}

	public String getZZCustomer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			userid = hash.get("FHSPR").toString();
			if (userid == null || "".equals(userid)) {
				userid = hash.get("ZZSPR").toString();
				if (userid == null || "".equals(userid)) {
					userid = hash.get("CWSCBFZR2").toString();
					if (userid == null || "".equals(userid)) {
						userid = hash.get("CWSCBFZR3").toString();
					}
				}
			}
		}
		return userid;
	}

	public String getZKCustomer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			userid = hash.get("ZZSPR").toString();
			if (userid == null || "".equals(userid)) {
				userid = hash.get("CWSCBFZR2").toString();
				if (userid == null || "".equals(userid)) {
					userid = hash.get("CWSCBFZR3").toString();
				}
			}
		}
		return userid;
	}

	public String getCYFZR1Customer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			userid = hash.get("CWSCBFZR2").toString();
			if (userid == null || "".equals(userid)) {
				userid = hash.get("CWSCBFZR3").toString();
			}
		}
		return userid;
	}

	public String getCYFZR2Customer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			userid = hash.get("CWSCBFZR3").toString();
		}
		return userid;
	}

	/**
	 * 获得指定客户的专职持续督导
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getZYZDCustomer(String customerno) {
		String demUUID = "84ff70949eac4051806dc02cf4837bd9";
		HashMap conditionMap = new HashMap();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		conditionMap.put("KHBH", customerno);
		List<HashMap> list = DemAPI.getInstance().getList(demUUID,
				conditionMap, null);
		String userid = "";
		for (HashMap hash : list) {
			// 返回专职持续督导和持续督导专员
			Map<String, Object> map = new HashMap<String, Object>();
			String fhspr = hash.get("FHSPR") != null ? hash.get("FHSPR")
					.toString() : "";
			map.put("FHSPR", fhspr);
			String khfzr = hash.get("KHFZR") != null ? hash.get("KHFZR")
					.toString() : "";
			map.put("KHFZR", khfzr);
			if (khfzr.equals(fhspr)) {
				map.put("ISSAME", true);
			} else {
				map.put("ISSAME", false);
			}
			returnList.add(map);
		}
		return returnList;
	}

	/**
	 * 获得短信文本
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	public String getNoticeSmsContent(String key, HashMap map) {
		HashMap conditionMap = new HashMap();
		conditionMap.put("JZ", key);
		if (!key.equals(ZQB_Notice_Constants.HYJH_TX_KEY)) {
			map.put("USERNAME", UserContextUtil.getInstance()
					.getCurrentUserContext().get_userModel().getUsername());
		}
		String txt = "";
		HashMap noticeMap = null;
		List<HashMap> list = DemAPI.getInstance().getList(uuid, conditionMap,
				null);
		if (list != null && list.size() > 0) {
			noticeMap = list.get(0);
		}
		if (noticeMap != null) {
			if (noticeMap.get("SMS") != null
					&& noticeMap.get("SMS").toString().equals("是")) {
				String content = noticeMap.get("DXTXWB").toString();
				txt = this.getBuildContent(content, map);

			}
		}
		return txt;
	}

	/**
	 * 获得系统消息文本
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	public String getNoticeSysMsgContent(String key, HashMap map) {

		HashMap conditionMap = new HashMap();
		conditionMap.put("JZ", key);
		map.put("USERNAME", UserContextUtil.getInstance()
				.getCurrentUserContext().get_userModel().getUsername());
		String txt = "";
		HashMap noticeMap = null;
		List<HashMap> list = DemAPI.getInstance().getList(uuid, conditionMap,
				null);
		if (list != null && list.size() > 0) {
			noticeMap = list.get(0);
		}
		if (noticeMap != null) {
			if (noticeMap.get("SYSMSG") != null
					&& noticeMap.get("SYSMSG").toString().equals("是")) {
				String content = noticeMap.get("XTXXTXWB").toString();
				txt = this.getBuildContent(content, map);

			}
		}
		return txt;
	}

	/**
	 * 获得邮件消息文本
	 */
	public String getNoticeEmailContent(String key, HashMap map) {
		HashMap conditionMap = new HashMap();
		conditionMap.put("JZ", key);
		map.put("USERNAME", UserContextUtil.getInstance()
				.getCurrentUserContext().get_userModel().getUsername());
		String txt = "";
		HashMap noticeMap = null;
		List<HashMap> list = DemAPI.getInstance().getList(uuid, conditionMap,
				null);
		if (list != null && list.size() > 0) {
			noticeMap = list.get(0);
		}
		if (noticeMap != null) {
			if (noticeMap.get("EMAIL") != null
					&& noticeMap.get("EMAIL").toString().equals("是")) {
				String content = noticeMap.get("YJTXWB").toString();
				txt = this.getBuildContent(content, map);
			}
		}
		return txt;
	}

	/**
	 * 获得装载后的正文
	 * 
	 * @param templateName
	 * @param root
	 * @return
	 */
	private String getBuildContent(String content, Map root) {
		StringWriter stringWriter = new StringWriter();
		String patternString = "\\{([^\"]+)\\}";
		Iterator iterator = root.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator
					.next();
			Object value = entry.getValue();
			if (value instanceof String) {
				String key = "${" + entry.getKey() + "}";
				content = content.replace(key, value.toString());
			}
		}
		stringWriter.append(content);
		return stringWriter.toString();
	}

	/**
	 * 获得质控与内核审批人
	 */
	public int getZkAndNhCount() {
		int count = 0;
		String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		count = DBUtil.getInt("SELECT COUNT(*) CNUM FROM BD_ZQB_ZKNHSPR WHERE SUBSTR(ZKR,0, INSTR(ZKR,'[',1)-1) = '"
						+ userid
						+ "' OR SUBSTR(NHSPR,0, INSTR(NHSPR,'[',1)-1) = '"
						+ userid + "'", "CNUM");
		return count;
	}
	//判断登录人是否是场外且不是质控与内核。
	public boolean getIsCw() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if (orgRoleId != null && orgRoleId.equals(new Long(5))&&getZkAndNhCount()<1) {
				flag = true;
			}
		}
		return flag;
	}
	//判断登录人是否是场外或质控或内核。
	public boolean getIsCwAndZkNh() {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			Long orgRoleId = uc.get_userModel().getOrgroleid();
			if ((orgRoleId != null && orgRoleId.equals(new Long(5)))||getZkAndNhCount()>=1) {
				flag = true;
			}
		}
		return flag;
	}

}
