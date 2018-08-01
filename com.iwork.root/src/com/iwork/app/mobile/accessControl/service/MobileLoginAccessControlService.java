package com.iwork.app.mobile.accessControl.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.iwork.app.log.dao.SysUserLoginLogDAO;
import com.iwork.app.log.model.SysUserLoginLog;
import com.iwork.app.mobile.accessControl.bean.AccessBindModel;
import com.iwork.app.mobile.accessControl.bean.AccessControlTreeModel;
import com.iwork.app.mobile.accessControl.dao.SysMobileVisitsetDao;
import com.iwork.app.mobile.accessControl.model.SysMobileVisitset;
import com.iwork.app.mobile.accessControl.util.AccessControlUtil;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;

public class MobileLoginAccessControlService {

	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserService orgUserService;
	private SysMobileVisitsetDao sysMobileVisitsetDao;
	private SysUserLoginLogDAO sysUserLoginLogDAO;
	private List<AccessControlTreeModel> list;

	private static final String FOLDER_OPEN_ICON = "iwork_img/mans.gif";
	private static final String FOLDER_CLOSE_ICON = "iwork_img/mans.gif";
	private static final String USER_BIND_ICON = "iwork_img/user_suit.png";
	private static final String USER_COMMON_ICON = "iwork_img/user_silhouette.png";

	@SuppressWarnings("unchecked")
	public String getIndexTreeJsonStr(String keyWord) {

		boolean open = false;
		boolean flag = false;
		boolean subflag = false;
		if (keyWord.trim().length() > 0) {
			open = true;
		}

		if (list == null) {
			list = new ArrayList<AccessControlTreeModel>();
		}

		List<OrgCompany> orgcompanyList = orgCompanyDAO.getAll();
		for (OrgCompany orgCompany : orgcompanyList) {
			// 设置公司节点
			AccessControlTreeModel rootmodel = new AccessControlTreeModel();
			rootmodel.setpId(0);
			rootmodel.setId(Integer.parseInt("10" + orgCompany.getId()));
			rootmodel.setName(orgCompany.getCompanyname());
			rootmodel.setOpen(true);
			rootmodel.setIconOpen(FOLDER_OPEN_ICON);
			rootmodel.setIconClose(FOLDER_CLOSE_ICON);
			list.add(rootmodel);

			List<OrgDepartment> topDeptList = orgDepartmentDAO
					.getTopDepartmentList(orgCompany.getId() + "");
			for (OrgDepartment topDepartment : topDeptList) {
				// 设置一级节点
				List<OrgDepartment> subDeptList = orgDepartmentDAO
						.getSubDepartmentList(topDepartment.getId());

				if (subDeptList != null && subDeptList.size() == 0) {
					// 指定部门下的普通用户列表
					List<OrgUser> userList = orgUserService
							.getDeptAllUserList(topDepartment.getId());
					if (userList != null) {
						for (OrgUser orgUser : userList) {
							// 设置User节点
							AccessControlTreeModel userModel = this
									.userSetTreeModel(orgUser, 3,
											topDepartment.getId());

							if (keyWord.trim().length() > 0) {
								if (userModel.getName().indexOf(keyWord) != -1) {
									list.add(userModel);
									flag = true;
								}
							} else {
								list.add(userModel);
							}
						}
					}

					AccessControlTreeModel topmodel = this.depSetTreeModel(
							topDepartment, 2, orgCompany.getId(), open);

					if (keyWord.trim().length() > 0) {
						if (flag) {
							list.add(topmodel);
							flag = false;
						}
					} else {
						list.add(topmodel);
					}

				} else {

					for (OrgDepartment subDepartment : subDeptList) {
						// 指定部门下的普通用户列表
						List<OrgUser> userList = orgUserService
								.getDeptAllUserList(subDepartment.getId());
						// 指定部门下的兼职用户列表
						List<OrgUser> partTimeUserList = orgUserService
								.getOrgUserMapList(subDepartment.getId());
						userList.addAll(partTimeUserList);
						if (userList != null) {
							for (OrgUser orgUser : userList) {
								// 设置User节点
								AccessControlTreeModel userModel = this
										.userSetTreeModel(orgUser, 4,
												subDepartment.getId());

								if (keyWord.trim().length() > 0) {
									if (userModel.getName().indexOf(keyWord) != -1) {
										list.add(userModel);
										flag = true;
										subflag = true;
									}
								} else {
									list.add(userModel);
								}
							}
						}
						// 设置二级节点
						AccessControlTreeModel subModel = this.depSetTreeModel(
								subDepartment, 3, topDepartment.getId()
										.toString(), open);

						if (keyWord.trim().length() > 0) {
							if (flag) {
								list.add(subModel);
								flag = false;
							}
						} else {
							list.add(subModel);
						}
					}

					AccessControlTreeModel topmodel = this.depSetTreeModel(
							topDepartment, 2, orgCompany.getId(), open);

					if (keyWord.trim().length() > 0) {
						if (subflag) {
							list.add(topmodel);
							subflag = false;
						}
					} else {
						list.add(topmodel);
					}
				}
			}
		}

		JSONArray json = JSONArray.fromObject(list);
		return json.toString();
	}

	private AccessControlTreeModel depSetTreeModel(OrgDepartment dep,
			int stepNo, String topStepId, boolean open) {

		AccessControlTreeModel model = new AccessControlTreeModel();

		model.setpId(Integer.parseInt((stepNo - 1) + "0" + topStepId));
		model.setId(Integer.parseInt(stepNo + "0" + dep.getId()));
		model.setName(dep.getDepartmentname());
		model.setOpen(open);
		model.setIcon(FOLDER_OPEN_ICON);

		return model;
	}

	private AccessControlTreeModel userSetTreeModel(OrgUser user, int stepNo,
			Long topStepId) {

		AccessControlTreeModel model = new AccessControlTreeModel();

		model.setpId(Integer.parseInt((stepNo - 1) + "0" + topStepId));
		model.setId(Integer.parseInt(stepNo + "0" + user.getId()));
		model.setName(user.getUsername());
		model.setOpen(false);
		model.setClick("openOneItem('" + user.getUserid() + "')");
		model.setUserId(user.getUserid());

		String modelIconFlag = AccessControlUtil.getInstance()
				.getUserVisitsetData(user.getUserid());
		if (modelIconFlag != null && modelIconFlag.trim().length() > 0) {
			model.setIcon(USER_BIND_ICON);
		} else {
			model.setIcon(USER_COMMON_ICON);
		}

		return model;
	}

	public SysMobileVisitset getMobileAccessSetByUserId(String userId) {
		return sysMobileVisitsetDao.findModelByUserId(userId);
	}

	public void saveMobileVisitset(SysMobileVisitset mobileVisitset) {

		sysMobileVisitsetDao.save(mobileVisitset);
	}

	public void updateMobileVisitset(SysMobileVisitset mobileVisitset) {

		sysMobileVisitsetDao.update(mobileVisitset);
	}

	public void updateMoreUserAccess(String userIds, int isBind, int isVisit,
			String visitType) {

		String[] ids = userIds.split(",");

		if (ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				SysMobileVisitset userMobileVisitset = getMobileAccessSetByUserId(ids[i]);
				userMobileVisitset.setIsBind(isBind);
				userMobileVisitset.setIsVisit(isVisit);
				userMobileVisitset.setVisitType(visitType);
				userMobileVisitset.setUserid(ids[i]);

				if (userMobileVisitset.getId() > 0) {
					updateMobileVisitset(userMobileVisitset);
				} else {
					saveMobileVisitset(userMobileVisitset);
				}

			}
		}

	}

	/**
	 * 获得报表显示列名称
	 * 
	 * @param reportId
	 * @return
	 */
	public String getColModel() {

		String[] titles = { "LOGINUSER", "LOGINTIME", "IPADDRESS", "SESSIONID" };

		return this.getColModelJson(titles);
	}

	public String getColName() {

		String[] titles = { "登陆用户名", "登录时间", "登陆IP", "SessionId" };

		return this.getColNameJson(titles);
	}

	public String getBindColModel() {

		String[] titles = { "TYPE", "BIDNID" };

		return this.getColModelJson(titles);
	}

	public String getBindColName() {

		String[] titles = { "设备类型", "设备ID" };

		return this.getColNameJson(titles);
	}

	private String getColModelJson(String[] titles) {

		StringBuffer html = new StringBuffer();
		List<HashMap<String, Object>> maplist = new ArrayList<HashMap<String, Object>>();
		int count = 0;
		for (int i = 0; i < titles.length; i++) {
			count++;
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("index", count);
			hash.put("align", "center");
			hash.put("name", titles[i]);
			if ("LOGINUSER".equals(titles[i])) {
				hash.put("width", new Long(80));
			} else {
				hash.put("width", new Long(130));
			}

			maplist.add(hash);
		}
		JSONArray json = JSONArray.fromObject(maplist);
		html.append(json);
		return html.toString();
	}

	private String getColNameJson(String[] titles) {

		List<String> maplist = new ArrayList<String>();
		for (int i = 0; i < titles.length; i++) {
			maplist.add(titles[i]);
		}
		JSONArray json = JSONArray.fromObject(maplist);
		StringBuffer html = new StringBuffer();
		html.append(json);
		return html.toString();
	}


	public String logDoSearch(Page page, String userId, String paperno) {

		if (page == null) {
			page = new Page();
			page.setCurPageNo(0);
			page.setPageSize(20);
		}

		List<SysUserLoginLog> userexamList = sysUserLoginLogDAO
				.searchMobileLog(userId);

		int totalRecord = 0;
		int totalNum = 0;
		HashMap<String, Object> total = new HashMap<String, Object>();
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		String[] titles = { "LOGINUSER", "LOGINTIME", "IPADDRESS", "SESSIONID" };
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int maxCount = (page.getCurPageNo() + 1) * page.getPageSize();
		if (maxCount > userexamList.size()) {
			maxCount = userexamList.size();
		}

		if (userexamList != null && userexamList.size() > 0) {
			for (int i = (page.getCurPageNo() - 1) * page.getPageSize(); i < maxCount; i++) {
				SysUserLoginLog model = userexamList.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(titles[0],
						model.getLoginUser() == null ? "" : model
								.getLoginUser());
				map.put(titles[1],
						model.getLoginTime() == null ? "" : sdf.format(model
								.getLoginTime()));
				map.put(titles[2],
						model.getIpadress() == null ? "" : model.getIpadress());
				map.put(titles[3],
						model.getSessionid() == null ? "" : model
								.getSessionid());

				datalist.add(map);
			}

			totalRecord = userexamList.size();
			BigDecimal b1 = new BigDecimal(totalRecord);
			BigDecimal b2 = new BigDecimal(page.getPageSize());
			// 合起来的总页数 向上取整
			totalNum = b1.divide(b2, 0, BigDecimal.ROUND_UP).intValue();
		}

		total.put("total", totalRecord);
		total.put("curPageNo", page.getCurPageNo());
		total.put("pageSize", page.getPageSize());
		total.put("totalPages", totalNum);
		total.put("totalRecords", totalRecord);
		total.put("dataRows", datalist);
		JSONArray json = JSONArray.fromObject(total);
		return json.toString();
	}

	public String bindDoSearch(Page page, String userId, String paperno) {

		if (page == null) {
			page = new Page();
			page.setCurPageNo(0);
			page.setPageSize(20);
		}

		List<AccessBindModel> userexamList = AccessControlUtil.getInstance()
				.getUserBindModelList(userId);

		int totalRecord = 0;
		int totalNum = 0;
		HashMap<String, Object> total = new HashMap<String, Object>();
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		String[] titles = { "TYPE", "BIDNID" };
		int maxCount = (page.getCurPageNo() + 1) * page.getPageSize();
		if (maxCount > userexamList.size()) {
			maxCount = userexamList.size();
		}

		if (userexamList != null && userexamList.size() > 0) {
			for (int i = (page.getCurPageNo() - 1) * page.getPageSize(); i < maxCount; i++) {
				AccessBindModel model = userexamList.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(titles[0],
						model.getPhoneType() == null ? "" : model
								.getPhoneType());
				map.put(titles[1],
						model.getBindId() == null ? "" : model.getBindId());
				datalist.add(map);
			}

			totalRecord = userexamList.size();
			BigDecimal b1 = new BigDecimal(totalRecord);
			BigDecimal b2 = new BigDecimal(page.getPageSize());
			// 合起来的总页数 向上取整
			totalNum = b1.divide(b2, 0, BigDecimal.ROUND_UP).intValue();
		}

		total.put("total", totalRecord);
		total.put("curPageNo", page.getCurPageNo());
		total.put("pageSize", page.getPageSize());
		total.put("totalPages", totalNum);
		total.put("totalRecords", totalRecord);
		total.put("dataRows", datalist);
		JSONArray json = JSONArray.fromObject(total);
		return json.toString();
	}

	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}

	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}

	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}

	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public SysMobileVisitsetDao getSysMobileVisitsetDao() {
		return sysMobileVisitsetDao;
	}

	public void setSysMobileVisitsetDao(
			SysMobileVisitsetDao sysMobileVisitsetDao) {
		this.sysMobileVisitsetDao = sysMobileVisitsetDao;
	}

	public SysUserLoginLogDAO getSysUserLoginLogDAO() {
		return sysUserLoginLogDAO;
	}

	public void setSysUserLoginLogDAO(SysUserLoginLogDAO sysUserLoginLogDAO) {
		this.sysUserLoginLogDAO = sysUserLoginLogDAO;
	}

}
