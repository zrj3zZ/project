package com.ibpmsoft.project.zqb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.ResultSet;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;

import com.ibpmsoft.project.zqb.dao.MailWithAttachmentDAO;
import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.service.FileUploadService;
import com.iwork.plugs.sms.bean.MailLog;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;

import org.apache.log4j.Logger;
public class MailWithAttachmentService {
	private final static String TXL_UUID = "5a47f41adc764690ae7f8258730e1618";
	private final static String CXDD_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private final static String XM_UUID = "33833384d109463285a6a348813539f1";
	private OrgCompanyDAO orgCompanyDAO;
	private FileUploadService fileUploadService;
	private MailWithAttachmentDAO mailWithAttachmentDAO;
	private static Logger logger = Logger.getLogger(MailWithAttachmentService.class);
	public void setMailWithAttachmentDAO(MailWithAttachmentDAO mailWithAttachmentDAO) {
		this.mailWithAttachmentDAO = mailWithAttachmentDAO;
	}

	public FileUploadService getFileUploadService() {
		return fileUploadService;
	}

	public void setFileUploadService(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

	public OrgCompanyDAO getOrgCompanyDAO() {
		return orgCompanyDAO;
	}

	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}

	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}

	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}

	public OrgDepartmentDAO getOrgDepartmentDAO() {
		return orgDepartmentDAO;
	}

	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}

	public OrgUserMapDAO getOrgUserMapDAO() {
		return orgUserMapDAO;
	}

	public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
		this.orgUserMapDAO = orgUserMapDAO;
	}

	private OrgUserDAO orgUserDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserMapDAO orgUserMapDAO;

	public List getDMTree() {
		List<HashMap> returnList = new ArrayList<HashMap>();
		Long roleid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
		String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		String customerno = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend1();
		// 董秘人员
		// 通讯录
		HashMap txl = new HashMap();
		txl.put("icon", "iwork_img/organization.gif");
		txl.put("id", "txl");
		txl.put("name", "通讯录");
		txl.put("open", "true");
		txl.put("isParent", false);
		txl.put("nodeType", "dept");
		HashMap conditionMap = new HashMap();
		conditionMap.put("USERID", userid);
		List<HashMap> txllist = DemAPI.getInstance().getList(TXL_UUID,conditionMap, null);
		List<HashMap> txlreturnlist = new ArrayList<HashMap>();
		for (HashMap mapXH : txllist) {
			HashMap map = new HashMap();
			map.put("id", mapXH.get("ID") == null ? "" : mapXH.get("ID").toString());
			map.put("name", mapXH.get("NAME"));
			map.put("email", mapXH.get("EMAIL"));
			map.put("open", "true");
			map.put("nodeType", "user");
			map.put("icon","iwork_img/user.png");
			txlreturnlist.add(map);
		}
		txl.put("children", txlreturnlist);
		returnList.add(txl);
		// 持续督导
		HashMap cxdd = new HashMap();
		cxdd.put("id", "cxdd");
		cxdd.put("name", "持续督导");
		cxdd.put("open", "true");
		cxdd.put("nodeType", "dept");
		cxdd.put("icon", "iwork_img/organization.gif");
		HashMap cxddMap = new HashMap();
		cxddMap.put("KHBH", customerno);
		List<HashMap> cxddlist = DemAPI.getInstance().getList(CXDD_UUID,cxddMap, null);
		List<HashMap> cxddreturnlist = new ArrayList<HashMap>();
		if (cxddlist.size() > 0) {
			for (HashMap mapXH : cxddlist) {
				// 根据所取得的持续督导查询其用户名和邮箱
				if (mapXH.get("KHFZR") != null && !mapXH.get("KHFZR").toString().equals("")) {
					String khfzr = mapXH.get("KHFZR").toString();
					String user = khfzr.substring(0, khfzr.indexOf("["));
					UserContext uc = UserContextUtil.getInstance().getUserContext(user);
					if(uc.get_userModel().getEmail()!=null&&!"".equals(uc.get_userModel().getEmail())){
					HashMap map = new HashMap();
					map.put("id", uc.get_userModel().getUserid());
					map.put("name", uc.get_userModel().getUsername());
					map.put("email", uc.get_userModel().getEmail());
					map.put("open", "true");
					map.put("nodeType", "user");
					cxddreturnlist.add(map);
					}
				}

			}
		}
		cxdd.put("children", cxddreturnlist);
		returnList.add(cxdd);
		// 项目负责人及项目成员
		HashMap xm = new HashMap();
		xm.put("id", "xm");
		xm.put("name", "项目负责人及成员");
		xm.put("open", "true");
		xm.put("nodeType", "dept");
		xm.put("icon", "iwork_img/organization.gif");
		HashMap xmConditionMap = new HashMap();
		xmConditionMap.put("CUSTOMERNO", customerno);
		List<HashMap> xmcylist = DemAPI.getInstance().getList(XM_UUID,xmConditionMap, null);
		List<HashMap> xmcyreturnlist = new ArrayList<HashMap>();
		for (HashMap mapXH : xmcylist) {
			HashMap map = new HashMap();
			if (mapXH.get("OWNER") != null && !mapXH.get("OWNER").toString().equals("")) {
				String owner = mapXH.get("OWNER").toString();
				String user = owner.substring(0, owner.indexOf("["));
				UserContext uc = UserContextUtil.getInstance().getUserContext(user);
				map.put("id", uc.get_userModel().getUserid());
				map.put("name", uc.get_userModel().getUsername());
				map.put("email", uc.get_userModel().getEmail());
				map.put("open", "true");
				if (!xmcyreturnlist.contains(map)) {
					xmcyreturnlist.add(map);
				}
				// 获取项目成员
				List<HashMap> sublist = DemAPI.getInstance().getFromSubData(Long.parseLong(mapXH.get("INSTANCEID").toString()),"SUBFORM_XMCYLB");
				if (sublist.size() > 0) {
					for (HashMap sub : sublist) {
						if (sub.get("USERID") != null && !sub.get("USERID").toString().equals("")) {
							String id = sub.get("USERID").toString();
							UserContext ucc = UserContextUtil.getInstance().getUserContext(id);
							HashMap map1 = new HashMap();
							map1.put("id", ucc.get_userModel().getUserid());
							map1.put("name", ucc.get_userModel().getUsername());
							map1.put("email", ucc.get_userModel().getEmail());
							map1.put("open", "true");
							map1.put("nodeType", "user");
							if (!xmcyreturnlist.contains(map1)) {
								xmcyreturnlist.add(map1);
							}
						}
					}
				}

			}
		}
		xm.put("children", xmcyreturnlist);
		returnList.add(xm);
		return returnList;
	}

	public String getTreeJson(String cid, String nodeType) {
		// 如果是董秘人员，返回通讯录、持续督导、项目负责人及项目成员
		// 如果是其他人员则返回通讯录及组织结构
		// 判断当前登陆用户所属角色
		List<HashMap> returnList = new ArrayList<HashMap>();
		Long roleid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
		String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		String customerno = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend1();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String companyId = uc.get_companyModel().getId();
		if (roleid == 3) {
			// 董秘角色树
			returnList = getDMTree();
		} else {
			// 非董秘人员返回tree
			returnList = getFDMTree(companyId, cid, nodeType);
		}
		StringBuffer json = new StringBuffer();
		JSONArray jsonArray = JSONArray.fromObject(returnList);
		json.append(jsonArray);
		return json.toString();
	}
	
	public StringBuffer getGroupData(String id){
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		int size = 0;
		if(id.equals("companyname")){
			
		}else if(id.equals("txl")){
			return getGroupEmails();
		}else{
			getGroupDepartmentids(id, sb, size);
		}
		//张建伟<1182712801@qq.com>;
		List<HashMap> groupData = mailWithAttachmentDAO.getGroupData(sb.toString(),sb2.toString());
		StringBuffer phone = new StringBuffer();
		for (HashMap data : groupData) {
			phone.append(data.get("USERNAME")).append("<").append(data.get("EMAIL")).append(">").append(";");
		}
		return phone;
	}
	
	private StringBuffer getGroupEmails(){
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		List<HashMap> emails = mailWithAttachmentDAO.getGroupEmails(userid);
		StringBuffer sb = new StringBuffer();
		for (HashMap email : emails) {
			sb.append(email.get("USERNAME")).append("<").append(email.get("EMAIL")).append(">").append(";");
		}
		return sb;
	}
	
	/**
	 * 递归查询部门id,获得id1,id2,id3,...结果
	 * @param id
	 * @param sb 拼接id查询条件
	 * @param size id结果集大小,用于退出递归,当当前结果及大小遇上一次结果及大小相同时退出
	 * @return
	 */
	private Set<String> getGroupDepartmentids(String id, StringBuffer sb, int size) {
		Set<String> ids = new HashSet<String>();
		if(id!=null){
			ids.add(id);
			sb.append(id).append(",");
		}
		List<HashMap> groupIds = mailWithAttachmentDAO.getGroupDepartmentid(sb.toString());
		if(size!=groupIds.size()){
			for (int i = 0; i < groupIds.size(); i++) {
				//sb拼接id部门下的所有部门id,包括当前id部门,用于查询人员信息
				sb.append(groupIds.get(i).get("ID").toString()).append(",");
				//将子部门存入不可重复集合ids
				ids.add(groupIds.get(i).get("ID").toString());
			}
			size=groupIds.size();
			return getGroupDepartmentids(null, sb, size);
		}else{
			return ids;
		}
	}

	public List getFDMTree(String companyid, String cid,String nodeType) {
		List<HashMap> returnList = new ArrayList<HashMap>();
		Long roleid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getOrgroleid();
		String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		String customerno = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend1();
		// 组织结构
		List<HashMap> zzjg = this.getSubTree(companyid, cid, nodeType);
		for (HashMap map : zzjg) {
			returnList.add(map);
		}
		// 非董秘人员,通讯录及组织结构
		if (nodeType.equals("company")) {
			HashMap txl = new HashMap();
			txl.put("id", "txl");
			txl.put("name", "通讯录");
			txl.put("icon", "iwork_img/organization.gif");
			txl.put("click","");//getGroupValue('txl');
			txl.put("open", "true");
			txl.put("nodeType", "dept");
			HashMap conditionMap = new HashMap();
			conditionMap.put("USERID", userid);
			List<HashMap> txllist = DemAPI.getInstance().getList(TXL_UUID,conditionMap, null);
			List<HashMap> txlreturnlist = new ArrayList<HashMap>();
			for (HashMap mapXH : txllist) {
				HashMap map = new HashMap();
				map.put("id", mapXH.get("ID") == null ? "" : mapXH.get("ID").toString());
				map.put("name", mapXH.get("NAME"));
				map.put("email", mapXH.get("EMAIL"));
				map.put("open", "true");
				map.put("nodeType", "user");
				map.put("icon","iwork_img/user.png");
				txlreturnlist.add(map);
			}
			txl.put("children", txlreturnlist);
			returnList.add(txl);
		}
		return returnList;
	}

	public List getSubTree(String companyId, String parentid, String nodeType) {
		StringBuffer html = new StringBuffer();
		List root = new ArrayList();
		List rows = new ArrayList();
		if(nodeType.equals("company")){
			List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(companyId),Long.parseLong(parentid));
			for(OrgDepartment dept:deptlist){
				HashMap submap = new HashMap();
				submap.put("id", dept.getId());
				submap.put("name", dept.getDepartmentname());
				submap.put("open", "true");
				List<OrgUser> orguserlist = orgUserDAO.getDeptAllUserEmailList(dept.getId());
				if(!orguserlist.isEmpty()){
					submap.put("isParent", true);
				}else{
					List<HashMap> usermaplist = getSubTree(companyId,dept.getId().toString(),"dept");
					if(!usermaplist.isEmpty()){
						submap.put("isParent", true);
					}else{
						continue;
					}
				}
				submap.put("nodeType", "dept");
				submap.put("nodeId", dept.getId());
				submap.put("icon","iwork_img/km/treeimg/folderopen.gif");
				submap.put("click","getGroupValue('"+dept.getId()+"');");
				rows.add(submap);
			}
		}else{
			List<HashMap> deptlist = mailWithAttachmentDAO.getDept(Long.parseLong(companyId), Long.parseLong(parentid));
			Long deptid = 0L;
			String deptname = "";
			for(HashMap dept:deptlist){
				deptid = Long.parseLong(dept.get("DEPTID").toString());
				deptname = dept.get("DEPTNAME").toString();
				HashMap submap = new HashMap();
				submap.put("id", deptid);
				submap.put("name", deptname);
				submap.put("open", "true");
				/*List<OrgUser> orguserlist = orgUserDAO.getDeptAllUserEmailList(deptid);
				if(!orguserlist.isEmpty()){*/
					submap.put("isParent", true);
				/*}else{
					List<HashMap> usermaplist = getSubTree(companyId,deptid.toString(),"dept");
					if(!usermaplist.isEmpty()){
						submap.put("isParent", true);
					}else{
						continue;
					}
				}*/
				submap.put("nodeType", "dept");
				submap.put("nodeId", deptid);
				submap.put("icon","iwork_img/km/treeimg/folderopen.gif");
				submap.put("click","getGroupValue('"+deptid+"');");
				rows.add(submap);
			}
		}
		//获得用户列表
		List<OrgUser> userList = null;
		userList=orgUserDAO.getDeptAllUserEmailList(Long.parseLong(parentid));
		for(OrgUser user:userList){
			if(user.getUsertype()!=null&&user.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示 
				HashMap subdata = new HashMap();
				subdata.put("id", "item_"+user.getUserid());
				subdata.put("name", user.getUsername()); 
				subdata.put("open", "true");
				subdata.put("nodeType", "user");
				subdata.put("nodeId", user.getId());
				subdata.put("email", user.getEmail());
				subdata.put("title",user.getPostsname()); 
				subdata.put("icon","iwork_img/user.png");
				rows.add(subdata);
			}
		}
		if (nodeType.equals("company")) {
			Object item = new HashMap();
			OrgCompany orgCompany = this.orgCompanyDAO.getBoData(companyId);
			if (orgCompany != null) {
				((Map) item).put("id", companyId);
				((Map) item).put("name", orgCompany.getCompanyname());
				((Map) item).put("icon", "iwork_img/organization.gif");
				((Map) item).put("click", "");
				((Map) item).put("open", "true");
				((Map) item).put("children", rows);
				((Map) item).put("nodeType", "dept");
				root.add(item);
			}
			return root;
		} else {
			return rows;
		}

	}

	public Map getSubItem(String companyId, Long id, String departmentname) {
		List rows = new ArrayList();
		Map subItem = new HashMap();
		subItem.put("id", id);
		int subDepartmentSize = this.orgDepartmentDAO.getSubDepartmentSize(id);
		int userSize = getOrgUserList(id).size();
		// 放置用户
		if (userSize > 0) {
			subItem.put("name", departmentname + "[" + userSize + "]");
		} else {
			subItem.put("name", departmentname);
		}
		List<Map> rylist = new ArrayList();
		List<OrgUser> userlist = getOrgUserList(id);
		for (OrgUser user : userlist) {
			if(user!=null){
			Map ryItem = new HashMap();
			if(user.getEmail()==null){
				continue;
			}
			ryItem.put("name", user.getUsername());
			ryItem.put("email", user.getEmail());
			ryItem.put("nodeType", "user");
			ryItem.put("open", "true");
			rylist.add(ryItem);
			}

		}
		// 放置子部门
		List<OrgDepartment> subDepartment = this.orgDepartmentDAO.getSubDepartmentList(id);
		// 如果有子部门
		if (subDepartmentSize > 0) {
			subItem.put("isParent", "true");
			for (OrgDepartment depart : subDepartment) {
				Map ryItem = getSubItem(companyId, depart.getId(),
						depart.getDepartmentname());
				// Map ryItem = new HashMap();
				// ryItem.put("async", Boolean.valueOf(true));
				// ryItem.put("nodeType", "dept");
				// ryItem.put("iconOpen", "iwork_img/package_add.png");
				// ryItem.put("iconClose", "iwork_img/package_delete.png");
				// ryItem.put("open", false);
				// ryItem.put("companyId",companyId );
				// ryItem.put("target", "departmenFrame");
				// ryItem.put("id", depart.getId());
				// int userSize1 = getOrgUserList(depart.getId()).size();
				// if (userSize1 > 0) {
				// ryItem.put("isParent", "true");
				// ryItem.put("name", depart.getDepartmentname() + "["
				// + userSize1 + "]");
				// ryItem.put("icon", "iwork_img/images/folder-open.gif");
				// List<Map> rylist1 = new ArrayList();
				// List<OrgUser> userlist1 = getOrgUserList(depart.getId());
				// for (OrgUser user : userlist1) {
				// Map ryItem1 = new HashMap();
				// ryItem1.put("async", Boolean.valueOf(true));
				// ryItem1.put("name", user.getUsername());
				// ryItem1.put("email", user.getEmail());
				// ryItem1.put("nodeType", "ry");
				// ryItem1.put("iconOpen", "iwork_img/package_add.png");
				// ryItem1.put("iconClose", "iwork_img/package_delete.png");
				// ryItem1.put("open", false);
				// rylist1.add(ryItem1);
				//
				// }
				// ryItem.put("children", rylist1);
				// } else {
				// ryItem.put("name",depart.getDepartmentname());
				// ryItem.put("icon", "iwork_img/images/folder-open.gif");
				// }

				rylist.add(ryItem);

			}

		}
		subItem.put("children", rylist);
		subItem.put("async", Boolean.valueOf(true));
		subItem.put("companyId", companyId);
		subItem.put("nodeType", "dept");
		subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
		subItem.put("iconClose", "iwork_img/images/folder.gif");
		subItem.put("icon", "iwork_img/images/folder-open.gif");
		subItem.put("target", "departmenFrame");
		subItem.put("open", Boolean.valueOf(false));

		// rows.add(subItem);
		return subItem;

	}

	public List getOrgUserList(Long departmentid) {
		List newUserList = new ArrayList();
		List<OrgUser> userList = this.orgUserDAO.getDeptAllUserList(departmentid);
		List<OrgUserMap> userMapList = this.orgUserMapDAO.getOrgUserMap_DeptId(departmentid);
		Map userMap = new HashMap();
		boolean isexist=false;
		if (userList != null) {
			OrgUser orguser=new OrgUser();
			for (OrgUser user: userList) {
				if(user.getOrgroleid().toString().equals("3")){
					HashMap customerCondition = new HashMap();
					customerCondition.put("CUSTOMERNO", user.getExtend1());
					List<HashMap> list = DemAPI.getInstance().getList("a243efd832bf406b9caeaec5df082e28",customerCondition , null);
					for (HashMap hashMap : list) {
						orguser.setUserid(hashMap.get("ID").toString());
						orguser.setUsertype(0L);
						orguser.setUsername(hashMap.get("USERNAME").toString());
						orguser.setEmail(hashMap.get("EMAIL")==null?"":hashMap.get("EMAIL").toString());
						orguser.setPostsname("");
						orguser.setIsmanager(0L);
						for (int i = 0; i < userList.size(); i++) {
							if(userList.get(i).getUsername()!=null&&userList.get(i).getUsername().trim().equals(orguser.getUsername().trim())){
								if("".equals(userList.get(i).getEmail())||userList.get(i).getEmail()==null){
									userList.get(i).setEmail(orguser.getEmail());
									isexist=false;
									continue;
								}
							}else{
								isexist=true;
							}
						}
					}
				}
			}
			if(isexist){
				if(!"".equals(orguser.getEmail())||orguser.getEmail()!=null){
					userMap.put(orguser.getUserid(), orguser);
				}
			}
			for (OrgUser user : userList) {
				if (!userMap.containsKey(user.getUserid())) {
					if(user.getEmail()!=null&&!"".equals(user.getEmail())){
						userMap.put(user.getUserid(), user);
					}
				}
			}
		}
		if (userMapList != null) {
			for (OrgUserMap user : userMapList) {
				if (!userMap.containsKey(user.getUserid())&&user!=null) {
					OrgUser model = this.orgUserDAO.getUserModel(user.getUserid());
					if(model.getEmail()!=null&&!"".equals(model.getEmail())){
						userMap.put(user.getUserid(), model);
					}
				}
			}
		}
		newUserList.addAll(userMap.values());
		return newUserList;
	}

	/**
	 * * 复制单个文件 * @param oldPath String 原文件路径 如：c:/fqf.txt * @param newPath
	 * String 复制后路径 如：f:/fqf.txt * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			//if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			//}
		} catch (Exception e) {
			
			logger.error(e,e);
		}

	}

	public String mailsend(String sjr, String content, String file, String title) {
		StringBuffer jsonHtml = new StringBuffer();
		StringBuffer forbid = new StringBuffer();
		String errForbid = "";
		String msg = "";
		String[] sjrarr = sjr.split(";");
		String[] fjarr =null;
		if(!"".equals(file)){
			fjarr = file.split(";");
		}
		String fileurl = "";
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		boolean flag = false;
		String rootPath = ServletActionContext.getServletContext().getRealPath(
				"");
		List<String> list = mailWithAttachmentDAO.getEmailNum(content,sjrarr);
		for (String namemail : sjrarr) {
			String filename = "";
			if(namemail.indexOf("<")!=-1){
				String name = namemail.substring(0, namemail.indexOf("<"));
				String mail = namemail.substring(namemail.indexOf("<") + 1,
						namemail.indexOf(">"));
				if(fjarr!=null){
					for (String fj : fjarr) {
						if(!"".equals(fj)&&fj!=null){
							FileUpload model = fileUploadService.getFileUpload(
									FileUpload.class, fj);
							fileurl = rootPath + File.separator
									+ model.getFileUrl().replaceAll("\\.\\.", "");
							String srcname = DBUtil.getString(
									"select FILE_SRC_NAME from sys_upload_file where file_id='"
											+ fj + "'", "FILE_SRC_NAME");
							String newFile=fileurl.substring(0,fileurl.lastIndexOf("\\")+1)+srcname;
							copyFile(fileurl,newFile);
							filename = filename + newFile + ";";
						}
						
					}
				}
				if(!list.contains(mail)){
					flag = MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), mail, title,
							content,filename);
					//写入日志重复
					// 获取id
					MailLog mlg=new MailLog();
					mlg.setEmail(mail);
					mlg.setTitle(title);
					mlg.setContent(content);
					mlg.setAttach(file);
					mlg.setUserid(userid);
					mlg.setStatus(flag?1:0);
					mlg.setName(name);
					mlg.setSendtime(new Date());
					mlg.setSubmittime(new Date());
					mailWithAttachmentDAO.addBoData(mlg);
				}else{
					forbid.append(namemail+",");
				}
				
			}else{
				if(fjarr!=null&&!fjarr[0].equals("undefined")){
					for (String fj : fjarr) {
						if(!"".equals(fj)&&fj!=null&&!fj.equals("undefined")){
							FileUpload model = fileUploadService.getFileUpload(
									FileUpload.class, fj);
							fileurl = rootPath + File.separator
									+ model.getFileUrl().replaceAll("\\.\\.", "");
							String srcname = DBUtil.getString(
									"select FILE_SRC_NAME from sys_upload_file where file_id='"
											+ fj + "'", "FILE_SRC_NAME");
							String newFile=fileurl.substring(0,fileurl.lastIndexOf("\\")+1)+srcname;
							copyFile(fileurl,newFile);
							filename = filename + newFile + ";";
						}
						
					}
				}
				if(!list.contains(namemail)){
					flag = MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), namemail, title,
							content,filename);
					MailLog mlg=new MailLog();
					mlg.setEmail(namemail);
					mlg.setTitle(title);
					mlg.setContent(content);
					mlg.setAttach(file);
					mlg.setUserid(userid);
					mlg.setSendtime(new Date());
					mlg.setSubmittime(new Date());
					mlg.setStatus(flag?1:0);
					mlg.setName("");
					mailWithAttachmentDAO.addBoData(mlg);
				}else{
					forbid.append(namemail+",");
				}
			}
		}
		if (flag) {
			msg = "发送成功!";
		} else {
			msg = "发送失败！";
		}
		if(forbid.length()>0){
			errForbid = forbid.substring(0, forbid.length()-1);
		}
		jsonHtml.append("{\"errForbid\":\""+errForbid+"\",\"msg\":\""+msg+"\"}");
		return jsonHtml.toString();
	}

	public List<HashMap<String, Object>> getList() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer sql = new StringBuffer();
		sql.append("select cid,name,email,decode(status,'0','未发送','1','已发送') status from iwork_mail_log where userid= ? order by cid desc");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				BigDecimal cid = rs.getBigDecimal(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				String status = rs.getString(4);
				map.put("ID", cid.toString());
				map.put("NAME", name);
				map.put("EMAIL", email);
				map.put("STATUS", status);
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return list;
	}

	public String getHtml(String id) {
		StringBuffer jsonHtml = new StringBuffer();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer sql = new StringBuffer();
		sql.append( "select name,email,title,content,attach status from iwork_mail_log where userid= ? and cid= ? order by cid desc");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs=null ;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			stmt.setString(2, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String name = rs.getString(1);
				String email = rs.getString(2);
				String title = rs.getString(3);
				String content = rs.getString(4);
				String attach = rs.getString(5)==null?"":rs.getString(5).toString();
				jsonHtml.append("	<table width=\"100%\">");
				jsonHtml.append("<tr>");
				jsonHtml.append("		<td width=\"5%\" valign=\"top\"><div align=\"left\">收件人</div></td>");
				jsonHtml.append("		<td width=\"40%\" align=\"left\"><div>");
				jsonHtml.append("					<textarea name=\"sjr\" cols=\"40\" rows=\"10\" id=\"sjr\">"
						+ name + "<" + email + "></textarea>");
				jsonHtml.append("					</div></td>");
				jsonHtml.append("			</tr>");
				jsonHtml.append("			<tr>");
				jsonHtml.append("			<td width=\"5%\" valign=\"top\"><div align=\"left\">主题</div></td>");
				jsonHtml.append("				<td width=\"40%\" align=\"left\"><div>");
				jsonHtml.append("						<input name=\"subject\" type=\"text\" id=\"subject\" size=\"40\" "
						+ "value=\"" + title + "\"");
				jsonHtml.append("							maxlength=\"40\">");
				jsonHtml.append("					</div></td>");
				jsonHtml.append("			</tr>");
				jsonHtml.append("			<tr height=\"50px\">");
				jsonHtml.append("			<td width=\"5%\" valign=\"top\"><div align=\"left\">邮件内容</div></td>");
				jsonHtml.append("				<td width=\"40%\" align=\"left\"><div>");
				jsonHtml.append("						<textarea name=\"body\" cols=\"40\" rows=\"10\" id=\"body\">"
						+ content + "</textarea>");
				jsonHtml.append("					</div></td>");
				jsonHtml.append("			</tr>");
				jsonHtml.append("			<tr height=\"100px\">");
				jsonHtml.append("				<td width=\"5%\" valign=\"top\"><div align=\"left\">附件</div></td>");
				jsonHtml.append("			<td width=\"40%\" align=\"left\"><div id='DIVYJFS'>");
				String[] fjarr = attach.split(";");
				for (String s : fjarr) {
					String srcname = DBUtil.getString(
							"select FILE_SRC_NAME from sys_upload_file where file_id='"
									+ s + "'", "FILE_SRC_NAME");
					jsonHtml.append("<div  id=\"" + s + "\"" + ">");
					jsonHtml.append("<span><a href=\"uploadifyDownload.action?fileUUID="
							+ s
							+ "\" target=\"_blank\">"
							+ srcname
							+ "</a></span>");
					jsonHtml.append("</div>");
				}
				jsonHtml.append("					</div></td>");
				jsonHtml.append("			</tr>");

				jsonHtml.append("		</table>");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, stmt, rs);
		}
		return jsonHtml.toString();
	}
}
