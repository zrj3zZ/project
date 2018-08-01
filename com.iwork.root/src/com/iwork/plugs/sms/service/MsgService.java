package com.iwork.plugs.sms.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.service.ZqbMeetingManageService;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgDepartmentService;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.sms.bean.MsgMst;
import com.iwork.plugs.sms.bean.TempMst;
import com.iwork.plugs.sms.dao.MsgDao;
import com.iwork.plugs.sms.util.MsgConst;
import com.iwork.plugs.sms.util.MsgResults;
import com.iwork.plugs.sms.util.SendSMSFactory;
import com.iwork.plugs.sms.util.model.ReturnModel;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import org.apache.log4j.Logger;
public class MsgService {
	private MsgDao msgDao;
	private ZqbMeetingManageService zqbMeetingManageService;
	private OrgUserService orgUserService;
	private OrgDepartmentService orgDepartmentService;
	private static Logger logger = Logger.getLogger(OrgUserService.class);
	static final char SBC_CHAR_START = 65281; // 全角！ 
	static final char SBC_CHAR_END = 65374; // 全角～
	static final int CONVERT_STEP = 65248; // 全角半角转换间隔
	static final char SBC_SPACE = 12288; // 全角空格 12288
	static final char DBC_SPACE = ' '; // 半角空格

	public String getDeptAndUserJson(Long deptId) {
		StringBuffer jsonHtml = new StringBuffer();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String companyId = uc.get_companyModel().getId();
		OrgUser userModel = uc.get_userModel();
		String userid = userModel.getUserid();
		String roleid = userModel.getOrgroleid().toString();
		List<HashMap> root = new ArrayList();
		HashMap company = new HashMap();
		OrgDepartmentDAO orgDepartmentDAO = orgDepartmentService.getOrgDepartmentDAO();
		if(!roleid.equals(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CUSTORMER)){
			//公司号码簿
			company.put("id","custormerphonebook"); 
			company.put("name","公司号码簿");
			company.put("open", "true"); 
			company.put("nodeType", "dept");
			company.put("icon","iwork_img/km/treeimg/folderopen.gif");
			company.put("click","");//getGroupValue('custormerphonebook');
			List<OrgUser> userList = null;
			List<HashMap> maplist = new ArrayList<HashMap>();
			//获得部门列表
			if(deptId==0){
				List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(companyId),deptId);
				for(OrgDepartment dept:deptlist){
					HashMap submap = new HashMap();
					submap.put("id", dept.getId());
					submap.put("name", dept.getDepartmentname());
					submap.put("open", "true");
					List<OrgUser> orguserlist = orgUserService.getDeptAllUserPhoneList(dept.getId());
					if(!orguserlist.isEmpty()){
						submap.put("isParent", true);
					}else{
						List<HashMap> usermaplist = this.getSubOrgNodeNew(Long.parseLong(companyId),dept.getId());
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
					maplist.add(submap);
				}
			}else{
				List<HashMap> deptlist = msgDao.getDept(Long.parseLong(companyId),deptId);
				Long deptid = 0L;
				String deptname = "";
				for(HashMap dept:deptlist){
					deptid = Long.parseLong(dept.get("DEPTID").toString());
					deptname = dept.get("DEPTNAME").toString();
					HashMap submap = new HashMap();
					submap.put("id", deptid);
					submap.put("name", deptname);
					submap.put("open", "true");
					/*List<OrgUser> orguserlist = orgUserService.getDeptAllUserPhoneList(deptid);
					if(!orguserlist.isEmpty()){*/
						submap.put("isParent", true);
					/*}else{
						List<HashMap> usermaplist = this.getSubOrgNodeNew(Long.parseLong(companyId),deptid);
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
					maplist.add(submap);
				}
			}
			//获得用户列表
			userList=orgUserService.getDeptAllUserPhoneList(deptId);
			for(OrgUser user:userList){
				if(user.getUsertype()!=null&&user.getUsertype().equals(new Long(0))){   //只显示组织用户，系统用户不显示 
					HashMap subdata = new HashMap();
					subdata.put("id", "item_"+user.getUserid());
					subdata.put("name", user.getUsername()); 
					subdata.put("open", "true");
					subdata.put("nodeType", "user");
					subdata.put("nodeId", user.getId());
					subdata.put("phone",user.getMobile()); 
					subdata.put("title",user.getPostsname()); 
					subdata.put("icon","iwork_img/user.png");
					maplist.add(subdata);
				}
			}
			if(maplist.size()>0){
				company.put("children",maplist);
				company.put("isParent", true);
			}else{
				company.put("isParent", false);
			}
			if(deptId!=0){
				root=maplist;
			}else{
				root.add(company);
			}
		}
		//【持续督导】可查阅，负责的客户人员号码信息及其本人号码簿联系信息
		if(roleid.equals(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CUSTORMER)){
			if(deptId==0){
				HashMap map = new HashMap();
				map.put("id","myphonebook"); 
				map.put("name","券商号码簿");
				map.put("open", "true");
				map.put("isParent", true);
				map.put("nodeType","dept");
				map.put("icon","iwork_img/km/treeimg/folderopen.gif");
				map.put("click","getGroupValue('myphonebook');");
				HashMap conditionMap = new HashMap();
				String khbh = uc.get_deptModel().getDepartmentno();
				conditionMap.put("KHBH", khbh);
				List<HashMap> list = DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", conditionMap, "ID");
				List<OrgUser> currentUserList = new ArrayList<OrgUser>();//当前部门用户列表（用于去除兼职列表中的重复用户）
				List<String> checkUserList = new ArrayList<String>();
				for (HashMap hashMap : list) {
					String ggfbr ="";
					String fhspr ="";
					String zzspr =""; 
					String zzcxdd = "";
					String khfzr = "";
					OrgUser FHSPR = new OrgUser();
					OrgUser ZZSPR = new OrgUser();
					OrgUser ZZCXDD = new OrgUser();
					OrgUser GGFBR = new OrgUser();
					OrgUser KHFZR = new OrgUser();
					if(!"".equals(hashMap.get("FHSPR").toString())){
						fhspr=hashMap.get("FHSPR").toString().substring(0, hashMap.get("FHSPR").toString().indexOf("["));
						FHSPR=orgUserService.getUserModel(fhspr);
						if(checkUserList.contains(fhspr)){ 
							continue;  //判断是否重复添加用户
						}else{
							currentUserList.add(FHSPR);
							checkUserList.add(fhspr);
						}
					}
					if(!"".equals(hashMap.get("ZZSPR").toString())){
						zzspr=hashMap.get("ZZSPR").toString().substring(0, hashMap.get("ZZSPR").toString().indexOf("["));
						ZZSPR=orgUserService.getUserModel(zzspr);
						if(checkUserList.contains(zzspr)){ 
							continue;  //判断是否重复添加用户
						}else{
							currentUserList.add(ZZSPR);
							checkUserList.add(zzspr);
						}
					}
					if(!"".equals(hashMap.get("KHFZR").toString())){
						khfzr=hashMap.get("KHFZR").toString().substring(0, hashMap.get("KHFZR").toString().indexOf("["));
						KHFZR=orgUserService.getUserModel(khfzr);
						if(checkUserList.contains(khfzr)){ 
							continue;  //判断是否重复添加用户
						}else{
							currentUserList.add(KHFZR);
							checkUserList.add(khfzr);
						}
					}
					if(!"".equals(hashMap.get("ZZCXDD").toString())){
						zzcxdd=hashMap.get("ZZCXDD").toString().substring(0, hashMap.get("ZZCXDD").toString().indexOf("["));
						ZZCXDD=orgUserService.getUserModel(zzcxdd);
						if(checkUserList.contains(zzcxdd)){ 
							continue;  //判断是否重复添加用户
						}else{
							currentUserList.add(ZZCXDD);
							checkUserList.add(zzcxdd);
						}
					}
					if(!"".equals(hashMap.get("GGFBR").toString())){
						ggfbr=hashMap.get("GGFBR").toString().substring(0, hashMap.get("GGFBR").toString().indexOf("["));
						GGFBR=orgUserService.getUserModel(ggfbr);
						if(checkUserList.contains(ggfbr)){ 
							continue;  //判断是否重复添加用户
						}else{
							currentUserList.add(GGFBR);
							checkUserList.add(ggfbr);
						}
					}
				}
				List<HashMap> ddlist = new ArrayList();
				for(OrgUser user:currentUserList){
					if(user.getMobile()==null||user.getMobile().equals("")){
						continue;
					}
					HashMap subdata = new HashMap();
					subdata.put("id", "item_"+user.getUserid());
					subdata.put("name", user.getUsername()); 
					subdata.put("open", "true");
					subdata.put("nodeType","user"); 
					subdata.put("nodeId", user.getId());
					subdata.put("phone",user.getMobile()); 
					subdata.put("title",user.getPostsname()); 
					subdata.put("icon","iwork_img/user.png");
					ddlist.add(subdata);
				}
				if(ddlist.isEmpty()){
					map.put("isParent", false);
				}else{
					map.put("isParent", true);
				}
				OrgDepartment department = orgDepartmentDAO.getModelForDepartmentNo(uc._userModel.getExtend1());
				HashMap submap = new HashMap();
				submap.put("id",department.getId()); 
				submap.put("name",department.getDepartmentname()); 
				submap.put("open", "true");
				submap.put("nodeType","dept");
				submap.put("nodeId", department.getId());
				submap.put("isParent", false);
				submap.put("icon","iwork_img/km/treeimg/folderopen.gif");
				submap.put("click","getGroupValue('"+department.getId()+"');");
				List<HashMap> usermaplist = new ArrayList<HashMap>();
				//获取用户列表
				List<OrgUser> orguserlist = orgUserService.getDeptAllUserPhoneList(department.getId());
				for(OrgUser user:orguserlist){
					if(uc._userModel.getUserid().equals(user.getUserid())){
						continue;
					}
					HashMap subdata = new HashMap();
					subdata.put("id", "item_"+user.getUserid());
					subdata.put("name", user.getUsername()); 
					subdata.put("open", "true");   
					subdata.put("nodeType","user");
					subdata.put("phone",user.getMobile()); 
					subdata.put("title",user.getPostsname()); 
					subdata.put("icon","iwork_img/user.png");
					usermaplist.add(subdata);
				}
				//获得部门列表
				//List<OrgDepartment> deptlist = orgDepartmentService.getOrgDepartmentDAO().getSubDepartmentList(Long.parseLong(companyId),department.getId());
				List<HashMap> deptlist = msgDao.getDept(Long.parseLong(companyId), department.getId());
				List<OrgUser> userList = null;
				Long deptid = 0L;
				String deptname = "";
				for(HashMap dept:deptlist){
					deptid = Long.parseLong(dept.get("DEPTID").toString());
					deptname = dept.get("DEPTNAME").toString();
					HashMap subdeptmap = new HashMap();
					subdeptmap.put("id", deptid);
					subdeptmap.put("name", deptname);
					subdeptmap.put("open", "true");
					List<OrgUser> suborguserlist = orgUserService.getDeptAllUserPhoneList(deptid);
					if(!suborguserlist.isEmpty()){
						subdeptmap.put("isParent", true);
					}else{
						List<HashMap> subusermaplist = this.getSubOrgNodeNew(Long.parseLong(companyId),deptid);
						if(!subusermaplist.isEmpty()){
							subdeptmap.put("isParent", true);
						}else{
							continue;
						}
					}
					subdeptmap.put("nodeType", "dept");
					subdeptmap.put("nodeId", deptid);
					subdeptmap.put("icon","iwork_img/km/treeimg/folderopen.gif");
					subdeptmap.put("click","getGroupValue('"+deptid+"');");
					usermaplist.add(subdeptmap);
				}
				
				if(usermaplist!=null&&usermaplist.size()>0){
					submap.put("isParent", true);
					submap.put("children",usermaplist);
					ddlist.add(submap);
				}
				if(ddlist!=null&&ddlist.size()>0){
					map.put("children",ddlist);
				}
				root.add(map);
			}else{
				//获得部门列表
				//List<OrgDepartment> deptlist = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(companyId),deptId);
				List<HashMap> deptlist = msgDao.getDept(Long.parseLong(companyId), deptId);
				List<HashMap> maplist = new ArrayList<HashMap>();
				Long deptid = 0L;
				String deptname = "";
				for(HashMap dept:deptlist){
					deptid = Long.parseLong(dept.get("DEPTID").toString());
					deptname = dept.get("DEPTNAME").toString();
					HashMap subdeptmap = new HashMap();
					subdeptmap.put("id", deptid);
					subdeptmap.put("name", deptname);
					subdeptmap.put("open", "true");
					List<OrgUser> suborguserlist = orgUserService.getDeptAllUserPhoneList(deptid);
					if(!suborguserlist.isEmpty()){
						subdeptmap.put("isParent", true);
					}else{
						List<HashMap> subusermaplist = this.getSubOrgNodeNew(Long.parseLong(companyId),deptid);
						if(!subusermaplist.isEmpty()){
							subdeptmap.put("isParent", true);
						}else{
							continue;
						}
					}
					subdeptmap.put("nodeType", "dept");
					subdeptmap.put("nodeId", deptid);
					subdeptmap.put("icon","iwork_img/km/treeimg/folderopen.gif");
					subdeptmap.put("click","getGroupValue('"+deptid+"');");
					maplist.add(subdeptmap);
				}
				List<OrgUser> orguserlist = orgUserService.getDeptAllUserPhoneList(deptId);
				for(OrgUser user:orguserlist){
					if(uc._userModel.getUserid().equals(user.getUserid())){
						continue;
					}
					HashMap subdata = new HashMap();
					subdata.put("id", "item_"+user.getUserid());
					subdata.put("name", user.getUsername()); 
					subdata.put("open", "true");   
					subdata.put("nodeType","user");
					subdata.put("phone",user.getMobile()); 
					subdata.put("title",user.getPostsname()); 
					subdata.put("icon","iwork_img/user.png");
					maplist.add(subdata);
				}
				if(maplist.size()>0){
					company.put("children",maplist);
				}
				root=maplist;
			}
		}
		JSONArray json = JSONArray.fromObject(root);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * 加载电话号码簿json
	 * @param searchkey
	 * @return
	 */
	public String showPhoneBookJson(String searchkey){
		StringBuffer jsonHtml = new StringBuffer();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		String roleid = uc._userModel.getOrgroleid().toString();
		List<HashMap> root = new ArrayList();
		HashMap company = new HashMap();
		if(!roleid.equals(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CUSTORMER)){
			//公司号码簿
			company.put("id","custormerphonebook"); 
			company.put("name","公司号码簿");
			company.put("open", true); 
			company.put("type","group");
			company.put("icon","iwork_img/km/treeimg/folderopen.gif");
			List<OrgDepartment> deptlist = orgDepartmentService.getOrgDepartmentDAO().getTopDepartmentList(uc.get_companyModel().getId());
			List<HashMap> maplist = new ArrayList();
			for(OrgDepartment dept:deptlist){
				HashMap submap = new HashMap();
				submap.put("id",dept.getId()); 
				submap.put("name",dept.getDepartmentname()); 
				submap.put("open", false); 
				submap.put("type","group");
				submap.put("icon","iwork_img/km/treeimg/folderopen.gif");
				//获取用户列表
				List<OrgUser> orguserlist = orgUserService.getDeptAllUserList(dept.getId());
				List<HashMap> usermaplist = this.getSubOrgNode(dept.getId(),searchkey);
				if( usermaplist==null) usermaplist = new ArrayList();
				for(OrgUser user:orguserlist){
					if(user.getMobile()==null||user.getMobile().equals("")){
						continue;
					}
					if(searchkey!=null && !"".equals(searchkey)){
						if(user.getUsername().contains(searchkey)){
							HashMap subdata = new HashMap();
							subdata.put("id", "item_"+user.getUserid());
							subdata.put("name", user.getUsername()); 
							subdata.put("open", true);   
							subdata.put("type","item"); 
							subdata.put("phone",user.getMobile()); 
							subdata.put("title",user.getPostsname()); 
							subdata.put("icon","iwork_img/user.png");
							usermaplist.add(subdata);
						}
					}else{
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+user.getUserid());
						subdata.put("name", user.getUsername()); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",user.getMobile()); 
						subdata.put("title",user.getPostsname()); 
						subdata.put("icon","iwork_img/user.png");
						usermaplist.add(subdata);
					}
				}
				if(usermaplist!=null&&usermaplist.size()>0){
					submap.put("children",usermaplist);
				}
				maplist.add(submap);
			}
			if(maplist.size()>0){
				company.put("children",maplist);
			}
			root.add(company);
		}
		//	【持续督导】可查阅，负责的客户人员号码信息及其本人号码簿联系信息
	
		if(roleid.equals(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CUSTORMER)){
			HashMap map = new HashMap();
			map.put("id","myphonebook"); 
			map.put("name","券商号码簿");
			map.put("open", true); 
			map.put("type","group");
			map.put("icon","iwork_img/km/treeimg/folderopen.gif");
			HashMap conditionMap = new HashMap();
			String khbh = uc.get_deptModel().getDepartmentno();
			conditionMap.put("KHBH", khbh);
			List<HashMap> list = DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", conditionMap, "ID");
			List<OrgUser> list1=new ArrayList<OrgUser>();
			for (HashMap hashMap : list) {
				String ggfbr ="";
				String fhspr ="";
				String zzspr =""; 
				String zzcxdd = "";
				String khfzr = "";
				OrgUser FHSPR = new OrgUser();
				OrgUser ZZSPR = new OrgUser();
				OrgUser ZZCXDD = new OrgUser();
				OrgUser GGFBR = new OrgUser();
				OrgUser KHFZR = new OrgUser();
				if(!"".equals(hashMap.get("FHSPR").toString())){
					fhspr=hashMap.get("FHSPR").toString().substring(0, hashMap.get("FHSPR").toString().indexOf("["));
					FHSPR=orgUserService.getUserModel(fhspr);
					list1.add(FHSPR);
				}
				if(!"".equals(hashMap.get("ZZSPR").toString())){
					zzspr=hashMap.get("ZZSPR").toString().substring(0, hashMap.get("ZZSPR").toString().indexOf("["));
					ZZSPR=orgUserService.getUserModel(zzspr);
					list1.add(ZZSPR);
				}
				if(!"".equals(hashMap.get("KHFZR").toString())){
					khfzr=hashMap.get("KHFZR").toString().substring(0, hashMap.get("KHFZR").toString().indexOf("["));
					KHFZR=orgUserService.getUserModel(khfzr);
					list1.add(KHFZR);
				}
				if(!"".equals(hashMap.get("ZZCXDD").toString())){
					zzcxdd=hashMap.get("ZZCXDD").toString().substring(0, hashMap.get("ZZCXDD").toString().indexOf("["));
					ZZCXDD=orgUserService.getUserModel(zzcxdd);
					list1.add(ZZCXDD);
				}
				if(!"".equals(hashMap.get("GGFBR").toString())){
					ggfbr=hashMap.get("GGFBR").toString().substring(0, hashMap.get("GGFBR").toString().indexOf("["));
					GGFBR=orgUserService.getUserModel(ggfbr);
					list1.add(GGFBR);
				}
			}
			for (int i = 0; i < list1.size(); i++)  //外循环是循环的次数
            {
                for (int j = list1.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
                {

                    if (list1.get(i).getUserid().toString().equals(list1.get(j).getUserid().toString()))
                    {
                    	list1.remove(j);
                    }

                }
            }
			List<HashMap> ddlist = new ArrayList();
			for(OrgUser user:list1){
				if(user.getMobile()==null||user.getMobile().equals("")){
					continue;
				}
				if(searchkey!=null && !"".equals(searchkey)){
					if(user.getUsername().contains(searchkey)){
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+user.getUserid());
						subdata.put("name", user.getUsername()); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",user.getMobile()); 
						subdata.put("title",user.getPostsname()); 
						subdata.put("icon","iwork_img/user.png");
						ddlist.add(subdata);
					}
				}else{
					HashMap subdata = new HashMap();
					subdata.put("id", "item_"+user.getUserid());
					subdata.put("name", user.getUsername()); 
					subdata.put("open", true);   
					subdata.put("type","item"); 
					subdata.put("phone",user.getMobile()); 
					subdata.put("title",user.getPostsname()); 
					subdata.put("icon","iwork_img/user.png");
					ddlist.add(subdata);
				}
			}
			OrgDepartment dept = orgDepartmentService.getOrgDepartmentDAO().getModelForDepartmentNo(uc._userModel.getExtend1());
			List<HashMap> dmlist = new ArrayList();
				HashMap submap = new HashMap();
				submap.put("id",dept.getId()); 
				submap.put("name",dept.getDepartmentname()); 
				submap.put("open", true); 
				submap.put("type","group");
				submap.put("icon","iwork_img/km/treeimg/folderopen.gif");
				//获取用户列表
				List<OrgUser> orguserlist = orgUserService.getDeptAllUserList(dept.getId());
				List<HashMap> usermaplist = this.getSubOrgNode(dept.getId(),searchkey);
				if( usermaplist==null) usermaplist = new ArrayList();
				for(OrgUser user:orguserlist){
					if(user.getMobile()==null||user.getMobile().equals("")){
						continue;
					}
					if(uc._userModel.getUserid().equals(user.getUserid())){
						continue;
					}
					if(searchkey!=null && !"".equals(searchkey)){
						if(user.getUsername().contains(searchkey)){
							HashMap subdata = new HashMap();
							subdata.put("id", "item_"+user.getUserid());
							subdata.put("name", user.getUsername()); 
							subdata.put("open", true);   
							subdata.put("type","item"); 
							subdata.put("phone",user.getMobile()); 
							subdata.put("title",user.getPostsname()); 
							subdata.put("icon","iwork_img/user.png");
							usermaplist.add(subdata);
						}
					}else{
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+user.getUserid());
						subdata.put("name", user.getUsername()); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",user.getMobile()); 
						subdata.put("title",user.getPostsname()); 
						subdata.put("icon","iwork_img/user.png");
						usermaplist.add(subdata);
					}
				}
				if(usermaplist!=null&&usermaplist.size()>0){
					submap.put("children",usermaplist);
				}
				ddlist.add(submap);
			if(ddlist!=null&&ddlist.size()>0){
				map.put("children",ddlist);
			}
			/*if(dmlist!=null&&dmlist.size()>0){
				map.put("children",dmlist);
			}*/
			root.add(map);
			/*for(HashMap book:list){
				if(searchkey!=null && !"".equals(searchkey)){
					
					if(book.get("NAME").toString().contains(searchkey)){
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+book.get("ID"));
						subdata.put("name", book.get("NAME")); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",book.get("TEL")); 
						subdata.put("title",book.get("TITLE")); 
						subdata.put("icon","iwork_img/user.png");
						subitemlist.add(subdata);	
					}
				}else{
					HashMap subdata = new HashMap();
					subdata.put("id", "item_"+book.get("ID"));
					subdata.put("name", book.get("NAME")); 
					subdata.put("open", true);   
					subdata.put("type","item"); 
					subdata.put("phone",book.get("TEL")); 
					subdata.put("title",book.get("TITLE")); 
					subdata.put("icon","iwork_img/user.png");
					subitemlist.add(subdata);
				}
			}*/
			//获取权限范围内用户信息
			/*List<HashMap> customerList = zqbMeetingManageService.getCurrentCustomerList();
			List<HashMap> subitemlist = new ArrayList();
			for(HashMap hash:customerList){
				String khbh = hash.get("KHBH").toString();
				String khmc = hash.get("KHMC").toString();
				HashMap submap = new HashMap();
				submap.put("id",khbh); 
				submap.put("name",khmc); 
				submap.put("open", true); 
				submap.put("type","group");
				submap.put("icon","iwork_img/km/treeimg/folderopen.gif");
				//获取客户外部人员号码簿信息
				HashMap outUserCondition = new HashMap();
				outUserCondition.put("KHBH", khbh);
				List<HashMap> outUserlist = DemAPI.getInstance().getList("499f1b08aadd4d1399ba202fb939b298",outUserCondition , null);
				List<HashMap> userMaplist = new ArrayList();
				for(HashMap outuser:outUserlist){
					if(outuser.get("SJ")==null||outuser.get("SJ").equals("")){
						continue;
					}
					if(searchkey!=null && !"".equals(searchkey)){
						if(outuser.get("XM").toString().contains(searchkey)){
							HashMap subdata = new HashMap();
							subdata.put("id", "item_"+outuser.get("ID"));
							subdata.put("name", outuser.get("XM")); 
							subdata.put("open", true);   
							subdata.put("type","item"); 
							subdata.put("phone",outuser.get("SJ")); 
							subdata.put("title",outuser.get("RZLX")); 
							subdata.put("icon","iwork_img/user.png");
							userMaplist.add(subdata);
						}
					}else{
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+outuser.get("ID"));
						subdata.put("name", outuser.get("XM")); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",outuser.get("SJ")); 
						subdata.put("title",outuser.get("RZLX")); 
						subdata.put("icon","iwork_img/user.png");
						userMaplist.add(subdata);
					}
				}
				
				//获取客户内部人员号码簿信息
				HashMap inUserCondition = new HashMap();
				inUserCondition.put("KHBH", khbh);
				List<HashMap> inUserlist = DemAPI.getInstance().getList("7d9f7112563d4fbfbc6056ed3e5019fe",inUserCondition , null);
				for(HashMap inuser:inUserlist){
					if(inuser.get("SJ")==null||inuser.get("SJ").equals("")){
						continue;
					}
					if(searchkey!=null && !"".equals(searchkey)){
						if(inuser.get("XM").toString().contains(searchkey)){
							HashMap subdata = new HashMap();
							subdata.put("id", "item_"+inuser.get("ID"));
							subdata.put("name", inuser.get("XM")); 
							subdata.put("open", true);   
							subdata.put("type","item"); 
							subdata.put("phone",inuser.get("SJ")); 
							subdata.put("title",inuser.get("RZLX")); 
							subdata.put("icon","iwork_img/user.png");
							userMaplist.add(subdata);
						}
					}else{
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+inuser.get("ID"));
						subdata.put("name", inuser.get("XM")); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",inuser.get("SJ")); 
						subdata.put("title",inuser.get("RZLX")); 
						subdata.put("icon","iwork_img/user.png");
						userMaplist.add(subdata);
					}
				} 
				if(userMaplist.size()>0){
					submap.put("children",userMaplist); 
				} 
				subitemlist.add(submap);
			}
			if(subitemlist.size()>0){
				map.put("children",subitemlist);
			}
			root.add(map);*/
		}
//		【场外市场经理】可查阅，项目人员、持续督导人员、客户信息、以及联系人信息
//		【客户】只能看到本人维护的号码信息
//	客户维护【内部人员】【外部人员】时，可将内部人员信息，动态同步至号码簿
			HashMap map = new HashMap();
			map.put("id","myphonebook"); 
			map.put("name","我的通讯录");
			map.put("open", true);   
			map.put("type","item");
			map.put("icon","iwork_img/km/treeimg/folderopen.gif");
			HashMap conditionMap = new HashMap();
			//85ded282ca3d457a80c0a3439ab112b5
			conditionMap.put("USERID",UserContextUtil.getInstance().getCurrentUserId());
			List<HashMap> list = DemAPI.getInstance().getList("5a47f41adc764690ae7f8258730e1618", conditionMap, "ID");
			String khbh = uc.get_deptModel().getDepartmentno();
			HashMap conditionMap1 = new HashMap();
			conditionMap1.put("KHBH",khbh);
			List<HashMap> list2 = DemAPI.getInstance().getList("7d9f7112563d4fbfbc6056ed3e5019fe", conditionMap1, "ID");
			List<HashMap> list3 = DemAPI.getInstance().getList("499f1b08aadd4d1399ba202fb939b298", conditionMap1, "ID");
			for (HashMap hashMap : list3) {
				List<HashMap> sublist=DemAPI.getInstance().getFromSubData(Long.parseLong(hashMap.get("INSTANCEID").toString()), "SUBFORM_JJLXR");
				for (HashMap hashMap2 : sublist) {
					for (int i = 0; i < list.size(); i++)  //外循环是循环的次数
					{
						if (list.get(i).get("NAME").toString().equals(hashMap2.get("XM").toString())&&list.get(i).get("TEL").toString().equals(hashMap2.get("SJ").toString()))
						{
							list.remove(i);
						}
					}
				}
			}
			
			for (HashMap hashMap : list2) {
				HashMap wbMap=new HashMap();
				wbMap.put("ID", hashMap.get("ID"));
				wbMap.put("NAME", hashMap.get("XM"));
				wbMap.put("TEL", hashMap.get("SJ"));
				list.add(wbMap);
			}
			List<HashMap> subitemlist = new ArrayList();
				for(HashMap book:list){
					if(book.get("TEL")==null||book.get("TEL").equals("")){
						continue;
					}
					if(searchkey!=null && !"".equals(searchkey)){
						
						if(book.get("NAME").toString().contains(searchkey)){
							HashMap subdata = new HashMap();
							subdata.put("id", "item_"+book.get("ID"));
							subdata.put("name", book.get("NAME")); 
							subdata.put("open", true);   
							subdata.put("type","item"); 
							subdata.put("phone",book.get("TEL")); 
							subdata.put("title",book.get("TITLE")); 
							subdata.put("icon","iwork_img/user.png");
							subitemlist.add(subdata);	
						}
					}else{
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+book.get("ID"));
						subdata.put("name", book.get("NAME")); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",book.get("TEL")); 
						subdata.put("title",book.get("TITLE")); 
						subdata.put("icon","iwork_img/user.png");
						for (HashMap hashMap : list3) {
							if(!book.get("NAME").toString().equals(hashMap.get("XM").toString())){
								continue;
							}
							List<HashMap> sublist=DemAPI.getInstance().getFromSubData(Long.parseLong(hashMap.get("INSTANCEID").toString()), "SUBFORM_JJLXR");
							for (HashMap hashMap2 : sublist) {
								List<HashMap> wbList = new ArrayList();
								String name = book.get("NAME").toString();
								String tel = book.get("TEL").toString();
									HashMap hashmap=new HashMap();
									if(hashMap2.get("SJ")==null||hashMap2.get("SJ").equals("")){
										continue;
									}
									if(searchkey!=null && !"".equals(searchkey)){
										if(hashMap2.get("XM").toString().contains(searchkey)){
											HashMap subdata1 = new HashMap();
											subdata1.put("id", "item_"+hashMap2.get("ID"));
											subdata1.put("name", hashMap2.get("XM")); 
											subdata1.put("open", true);   
											subdata1.put("type","item"); 
											subdata1.put("phone",hashMap2.get("SJ")); 
											subdata1.put("title",""); 
											subdata1.put("icon","iwork_img/user.png");
											wbList.add(subdata1);	
										}
									}else{
										HashMap subdata2 = new HashMap();
										subdata2.put("id", "item_"+hashMap2.get("ID"));
										subdata2.put("name", hashMap2.get("XM")); 
										subdata2.put("open", true);   
										subdata2.put("type","item"); 
										subdata2.put("phone",hashMap2.get("SJ")); 
										subdata2.put("title",""); 
										subdata2.put("icon","iwork_img/user.png");
										wbList.add(subdata2);
									}
									subdata.put("children", wbList);
							}
						}
						subitemlist.add(subdata);
					}
				}
				if(subitemlist!=null&&subitemlist.size()>0){
					map.put("children",subitemlist);
				}
			//root.add(map);
		JSONArray json = JSONArray.fromObject(root);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	/**
	 * JDBC通讯录分组列表
	 */
	public String getdepartmentJson(){
		StringBuffer sql = new StringBuffer("SELECT DEPARTMENTNAME FROM ORGDEPARTMENT O WHERE O.PARENTDEPARTMENTID != 51 ORDER BY O.PARENTDEPARTMENTID");
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs =null;
		String departmentname = "";
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					departmentname += (rs.getString(1) + ",");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return departmentname;
	}
	
	/**
	 * 获取所有的公司联系人信息
	 */
	public String getcompanyJson() {
		// 
		StringBuffer sql = new StringBuffer("SELECT B.USERNAME FROM ORGDEPARTMENT O INNER JOIN ORGUSER B ON O.DEPARTMENTNO=B.EXTEND1 WHERE O.PARENTDEPARTMENTID=51 ORDER BY O.PARENTDEPARTMENTID");
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs =null;
		String company = "";
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					company += (rs.getString(1) + ",");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return company;
	}
	
	/**
	 * 获取所有的场外信息
	 */
	public String getchangwaiJson() {
		// 
		StringBuffer sql = new StringBuffer("SELECT USERNAME FROM ORGUSER WHERE DEPARTMENTNAME='场外市场部' AND ORGROLEID != 1");
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs =null;
		String changwai = "";
		try {
			conn = DBUtil.open();
			stmt = conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					changwai += (rs.getString(1) + ",");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return changwai;
	}

	/**
	 * 获取所有的中介信息
	 */
	public String getzhongjieJson() {
		// 
		StringBuffer sql = new StringBuffer("SELECT USERNAME FROM ORGUSER WHERE DEPARTMENTNAME='中介机构' AND ORGROLEID != 1");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs =null;
		String zhongjie = "";
		try {
			stmt = conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					zhongjie += (rs.getString(1) + ",");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return zhongjie;
	}

	/**
	 * 获取所有的业务督导信息
	 */
	public String getyewududaoJson() {
		// 
		StringBuffer sql = new StringBuffer("SELECT USERNAME FROM ORGUSER WHERE DEPARTMENTNAME='业务督导部' AND ORGROLEID != 1");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs =null;
		String yewududao = "";
		try {
			stmt = conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					yewududao += (rs.getString(1) + ",");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return yewududao;
	}

	/**
	 * 获取所有的业务督导信息
	 */
	public String getyewufazhanJson() {
		// 
		StringBuffer sql = new StringBuffer("SELECT USERNAME FROM ORGUSER WHERE DEPARTMENTNAME='业务发展部' AND ORGROLEID != 1");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs =null;
		String yewufazhan = "";
		try {
			stmt = conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					yewufazhan += (rs.getString(1) + ",");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return yewufazhan;
	}
	
	private List<HashMap> getSubOrgNode(Long departmentid,String searchkey){
		List<OrgDepartment> deptlist = orgDepartmentService.getOrgDepartmentDAO().getSubDepartmentList(departmentid);
		List<HashMap> maplist = new ArrayList();
		for(OrgDepartment dept:deptlist){
			HashMap submap = new HashMap();
			submap.put("id",dept.getId());
			submap.put("name",dept.getDepartmentname());  
			submap.put("open", true); 
			submap.put("type","group");
			submap.put("icon","iwork_img/km/treeimg/folderopen.gif");
			//获取用户列表
			List<OrgUser> orguserlist = orgUserService.getDeptAllUserList(dept.getId());
			List<HashMap> usermaplist = this.getSubOrgNode(dept.getId(),searchkey);
			if( usermaplist==null) usermaplist = new ArrayList();
			boolean flag = false;
			int count=0;
			OrgUser orguser=new OrgUser();
			for (OrgUser user: orguserlist) {
				if(user.getOrgroleid().toString().equals("3")){
					HashMap customerCondition = new HashMap();
					customerCondition.put("CUSTOMERNO", user.getExtend1());
					List<HashMap> list = DemAPI.getInstance().getList("a243efd832bf406b9caeaec5df082e28",customerCondition , null);
					for (HashMap hashMap : list) {
						orguser.setUserid(hashMap.get("ID").toString());
						orguser.setUsertype(0L);
						orguser.setUsername(hashMap.get("USERNAME")==null?"":hashMap.get("USERNAME").toString());
						orguser.setMobile(hashMap.get("TEL")==null?"":hashMap.get("TEL").toString());
						orguser.setPostsname("");
						for (int i = 0; i < orguserlist.size(); i++) {
							if(orguserlist.get(i).getUsername()!=null&&orguserlist.get(i).getUsername().trim().equals(orguser.getUsername().trim())){
								if("".equals(orguserlist.get(i).getMobile())||orguserlist.get(i).getMobile()==null){
									orguserlist.get(i).setMobile(orguser.getMobile());
								}
								count=1;
							}else{
								count=2;
							}
						}
					}
				}
			}
			if(count==2){
				orguserlist.add(orguser);
			}
			for(OrgUser user:orguserlist){
				//if(user.getUsertype().equals(new Long(2)))continue;
				if(user.getMobile()==null||user.getMobile().equals("")){
					continue;
				}
				if(searchkey!=null&&!"".equals(searchkey)){
					if(user.getUsername().contains(searchkey)){
						HashMap subdata = new HashMap();
						subdata.put("id", "item_"+user.getUserid());
						subdata.put("name", user.getUsername()); 
						subdata.put("open", true);   
						subdata.put("type","item"); 
						subdata.put("phone",user.getMobile()); 
						subdata.put("title",user.getPostsname()); 
						subdata.put("icon","iwork_img/user.png");
						usermaplist.add(subdata);
					}
				}else{
					HashMap subdata = new HashMap();
					subdata.put("id", "item_"+user.getUserid());
					subdata.put("name", user.getUsername()); 
					subdata.put("open", true);   
					subdata.put("type","item"); 
					subdata.put("phone",user.getMobile()); 
					subdata.put("title",user.getPostsname()); 
					subdata.put("icon","iwork_img/user.png");
					usermaplist.add(subdata);
				}
				flag = true;
			}
			if(usermaplist!=null&&usermaplist.size()>0){
				submap.put("children",usermaplist);
			} 
			if(flag){
				maplist.add(submap);
			}
			
		}
		return maplist;
	}

	/**
	 * 获取短信单价 "select bo_msg_sp.BILLINGLEN as msglen,bo_msg_sp.sign as
	 dbsign,bo_msg_sp.PRICE as unitprice from bo_msg_config,bo_msg_sp where
	 bo_msg_config.type='DEFAULT_CHANNEL' and bo_msg_config.key='0' and
	 bo_msg_config.value=bo_msg_sp.spid";
	 * 
	 * @return
	 */
	public String getunitp() {
		return msgDao.getunitp();
	}

	// 处理短信发送
	public String handleMsg(String content, String phoneNum) {
		try {
			content=URLDecoder.decode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
		}
		StringBuffer jsonHtml = new StringBuffer();
		phoneNum=qj2bj(phoneNum);
		String[] phoneNums = phoneNum.split(",|;|\\n");
		//过滤五分钟内同一个手机号发送过的内容
		List<String> list = msgDao.getphoneNum(content,phoneNums);
		int totalNum = 0;
		int sNum = 0;
		int errNum = 0;
		boolean sendSMS = false;
		StringBuffer forbid = new StringBuffer();
		String errForbid = "";
		StringBuffer val = new StringBuffer();
		String errVal = "";
		List objNum = new ArrayList();
		String num = "";//手机号
		for(String numstr:phoneNums){
			if(!numstr.equals("")){
				String contentText=content;
				if(numstr.equals(""))continue;
				num = numstr;
				if(numstr.indexOf("[")>0&&numstr.indexOf("]")>0){
					num = numstr.substring(0,numstr.indexOf("["));					
					String name =  numstr.substring(numstr.indexOf("[")+1,numstr.indexOf("]"));
					contentText = contentText.replace("%name%", name);
				}
				if(objNum.contains(num)){continue;}//过滤每批发送人中的重复手机号
				objNum.add(num);
				if(!list.contains(num)){
					sendSMS = MessageAPI.getInstance().sendSMS(UserContextUtil.getInstance().getCurrentUserContext(), numstr, contentText);
					
				}else{
					forbid.append(numstr+",");
				}
				if(sendSMS){
					sNum++;
				}else {
					errNum++;
					val.append(numstr+",");
				}
			}
			totalNum++;
		}
		if(val.length()>0){
			errVal = val.toString().substring(0, val.length()-1);
		}
		if(forbid.length()>0){
			errForbid = forbid.toString().substring(0, forbid.length()-1);
		}
		jsonHtml.append("{\"errForbid\":\""+errForbid+"\",\"content\":\""+errVal+"\",\"totalNum\":\""+totalNum+"\",\"sNum\":\""+sNum+"\",\"errNum\":\""+errNum+"\"}");
		return jsonHtml.toString();
	}
 
	/**
	 * @author lee 短信通用功能 public class MsgUtil {
	 */

	// 敏感词检查
	public String filterWordCheck1(String content, String uid) {
		String ret = "";

		// 读取敏感词
		ret = msgDao.filter(content);
		if (ret.length() > 0) {
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, "发送中止：内容包含敏感词：" +
			// ret);
			ret = "发送内容中包含敏感词：" + ret + "，无法发送。请修改后发送。";
		}

		return ret;
	}

	// 手机号检查
	public String mobileNumCheck(String phoneNum,
			HashMap<String, String> h, ArrayList list) {
		phoneNum = removeInvalidStr(phoneNum);
		phoneNum = numFormat(phoneNum);
		String ret = splitNum(phoneNum, h, list);
		return ret;
	}

	// 分解群发手机号
	public String splitNum(String phoneNum, HashMap<String, String> h,
			ArrayList list) {
		String ret = "";
		if (phoneNum.length() == 0) {
			return ret;
		}

		// 拆分号码和姓名
		String[] numSec = phoneNum.split(",");
		for (int i = 0; i < numSec.length; i++) {
			String num = "";
			String name = "";
			String s = numSec[i];
			int begin = s.indexOf("[");
			if (begin != -1) {
				num = s.substring(0, begin);
				if (s.length() > begin) {
					name = s.substring(begin + 1,
							(s.endsWith("]") ? s.length() - 1 : s.length()));
				}
			} else {
				num = s;
			}
			if (!h.containsKey(num)) {
				// 检查号码
				if (numFormatCheck(num)) {
					h.put(num, name);
					if (list != null) {
						list.add(num);
					}
				} else {
					//ret += "手机号格式错误: " + s + ";\n";在inset_page.jsp中的location中的参数不能有；\n
					ret += "手机号格式错误: " + s +".";
				}
			} else
				//ret += "手机号重复: " + s + ";\n";
			   ret += "手机号重复: " + s+"." ;
		}

		if (ret.length() > 0) {
			h.clear();
			if (list != null) {
				list.clear();
			}
		}
		return ret;
	}

	// 检查手机号格式是否合规,true=合规
	public boolean numFormatCheck(String num) {
		return num.matches("^1\\d{10}$");
	}

	// 手机号字符串全角转半角
	public String numFormat(String s) {
		String[][] f = { { "０", "0" }, { "１", "1" }, { "２", "2" },
				{ "３", "3" }, { "４", "4" }, { "５", "5" }, { "６", "6" },
				{ "７", "7" }, { "８", "8" }, { "９", "9" }, { "，", "\\," },
				{ "【", "[" }, { "】", "]" }, { " ", "" }, { "　", "" } };
		for (int i = 0; i < f.length; i++) {
			s = s.replaceAll(f[i][0], f[i][1]);
		}
		return s;
	}

	// 分解短信，写入临时表
	public String splitMsg(String content, HashMap<String, String> nums,
			String uuid, String uid) {
		String ret = "";
		String num = "";
		String c = "";
		Iterator iter = nums.entrySet().iterator();
		while (iter.hasNext()) {
			TempMst tm = new TempMst();
			Map.Entry entry = (Map.Entry) iter.next();
			num = String.valueOf(entry.getKey());
			String name = (String) entry.getValue();
			 //通配符 
			c = replaceWildcard(uid, num, content, name);
			 if (c.length() == 0) {
			 continue;
			 }
			 tm.setContent(c);
			
			Date date = new Date();
			tm.setBatchnum(uuid);
			tm.setSubmittime(date);
			tm.setUserid(uid);
			//tm.setContent(content);
			tm.setMobilenum(num);
			msgDao.addtemp(tm);
		}
		
		return ret;
	}

	// 处理通配符(name, attr1,attr2,attr3)
	public String replaceWildcard(String uid, String num, String c, String name) {
		if (uid.length() == 0 || num.length() == 0 || c.length() == 0)
			return c;

		c = c.replaceAll(MsgConst.REGEX_NAME, name);

		// 数据库取号码簿属性
		Hashtable h = msgDao.attrdb(num);
		if (h != null) {
			String attr1 = (String)h.get("ATTR1")==null?"":(String)h.get("ATTR1");
			String attr2 = (String)h.get("ATTR2")==null?"":(String)h.get("ATTR2");
			String attr3 = (String)h.get("ATTR3")==null?"":(String)h.get("ATTR3");
			c = c.replaceAll(MsgConst.REGEX_ATTR1, attr1);
			c = c.replaceAll(MsgConst.REGEX_ATTR2, attr2);
			c = c.replaceAll(MsgConst.REGEX_ATTR3, attr3);
		}
		return c;

	}

	// 去除短信内容中的格式符号
	public String removeInvalidStr(String c) {
		String[][] f = { { "__eol__", "\n" } };
		for (int i = 0; i < f.length; i++) {
			c = c.replaceAll(f[i][0], f[i][1]);
		}
		return c;
	}

	// 选择通道和填写短信运营商信息
	public String chooseChannel(String batchnum, String uid) {
		String ret = "";

		// 获取该批短信的号码
		ArrayList nums = new ArrayList(); // 号码集合
		ret = getNums(batchnum, nums, true);
		if (ret.length() > 0) {
			nums.clear();
			return ret;
		}

		// 取号段的value String sql1 = "select value from bo_msg_config where
		// type='MOBILE_NUM_SECTION' and key=?";
		// String sql2 = "select value from bo_msg_config where
		// type='DEFAULT_CHANNEL' and key=?";
		// String sql5 = "select value from bo_msg_config where type='MOBILE_SP'
		// and key=?";
		// String sql3a = "select ";
		// String sql3b = "channel from bo_msg_limit where userid=?";
		// String sql4 = "update bo_mobile_msg_temp set status=1, channelid=? "
		// + " where batchnum=? and mobilenum=?";
		// String sql6 = "select msgsp from bo_msg_channel where channelid=?";
		// String sql7 = "update bo_mobile_msg_temp "
		// + "set (msgsp,sign,billinglen,price) = "
		// + "(select spid,sign,billinglen,price from bo_msg_sp where spid=?) "
		// + "where batchnum=? and mobilenum=?";
		// 循环处理每个号码
		int a=nums.size();
		for (int i = 0; i < nums.size(); i++) {
			// 获取电信运营商
			// 电信运营商id
			int mobileSp = 0; // 0是空标记
			// 通道id
			int channelid = 0; // 0是空标记

			String num = (String) nums.get(i); // 手机号
			String numPre = num.substring(0, 3); // 手机号段
			String qnumsection = "select value from ConfigMst where type='MOBILE_NUM_SECTION' and key=?";// 查询号段
			String mobileSps = msgDao.qnumsection(qnumsection,numPre);
			mobileSp = Integer.parseInt(mobileSps);

			if (mobileSp > 0 && mobileSp <= MsgConst.MOBILE_SP_MAX_ID) {
				// 获取电信运营商名
				String qspname = "select value from ConfigMst where type='MOBILE_SP' and key=?";
				String spName = msgDao.qnumsection(qspname,String.valueOf(mobileSp));

				if (spName.length() == 0) {
					break;
				}
				// 获取用户缺省通道
				String spName1 = spName.toLowerCase();
				String sqltemp = "select ? channel from LimitMst where userid='test'";
				List<Object> paramst = new ArrayList<Object>();
				paramst.add(spName1);
				// int ltest=msgDao.qchannel(sqltemp);
				channelid = msgDao.qchannel(sqltemp,paramst);

				// 获取全局缺省通道
				if (channelid == 0) {
					String sqlid = "select value from ConfigMst where type='DEFAULT_CHANNEL' and key=?";
					List<Object> params = new ArrayList<Object>();
					params.add(String.valueOf(mobileSp));
//					channelid = msgDao.qchannel(sqlid);
					String channelids = msgDao.qchannel2(sqlid,params);
					channelid=Integer.parseInt(channelids);

				}

				// 如未找到缺省通道，则使用全局缺省通道
				if (channelid == 0) {
					String sqlid2 = "select value from ConfigMst where type='DEFAULT_CHANNEL' and key='0'";
					//channelid = msgDao.qchannel(sqlid2);
					List<Object> params = new ArrayList<Object>();
					String channelids = msgDao.qchannel2(sqlid2,params);
					channelid=Integer.parseInt(channelids);

				}
				if (channelid == 0) {
					channelid = 1; // 如全局缺省通道未找到，则使用通道1
				}

				// 写入通道
				String sql4 = "update TempMst set status=1, channelid=? where batchnum=? and mobilenum=?";
				List<Object> params4 = new ArrayList<Object>();
				params4.add(channelid);
				params4.add(batchnum);
				params4.add(num);
				msgDao.doupdate1(sql4,params4);
				// 获取短信运营商id
				int msgSp = 0; // 0是空标记
				String sql6 = "select msgsp from ChannelMst where channelid=?";
				List<Object> params6 = new ArrayList<Object>();
				params6.add(channelid);
				msgSp = msgDao.qchannel(sql6,params6);

				if (msgSp == 0) {
					ret = "无法找到通道" + channelid + "对应的短信运营商;";

					return ret;
				}

				// 写入短信运营商信息
				// String sql7 = "update TempMst "
				// + "set (msgsp,sign,billinglen,price) = "
				// + "(select spid,sign,billinglen,price from SpMst where
				// spid='"+msgSp+"') "
				// + "where batchnum='"+ batchnum+"' and mobilenum='"+num+"'";
				//				
				// msgDao.doupdate1(sql7);
				String sql71 = "update TempMst set msgsp= (select spid from SpMst where spid=?) where batchnum=? and mobilenum=?";
				List<Object> params71 = new ArrayList<Object>();
				params71.add(msgSp);
				params71.add(batchnum);
				params71.add(num);
				msgDao.doupdate1(sql71,params71);
				String sql72 = "update TempMst set sign= (select sign from SpMst where spid=?) where batchnum=? and mobilenum=?";
				List<Object> params72 = new ArrayList<Object>();
				params72.add(msgSp);
				params72.add(batchnum);
				params72.add(num);
				msgDao.doupdate1(sql72,params72);
				String sql73 = "update TempMst set billinglen = (select billinglen from SpMst where spid=?) where batchnum=? and mobilenum=?";
				List<Object> params73 = new ArrayList<Object>();
				params73.add(msgSp);
				params73.add(batchnum);
				params73.add(num);
				msgDao.doupdate1(sql73,params73);
				String sql74 = "update TempMst set price = (select price from SpMst where spid=?) where batchnum=? and mobilenum=?";
				List<Object> params74 = new ArrayList<Object>();
				params74.add(msgSp);
				params74.add(batchnum);
				params74.add(num);
				msgDao.doupdate1(sql74,params74);
			}
		}
		nums.clear();

		// 更新折合条数

		String sql8 = "update TempMst set msgcount = ceil(length(content)/billinglen) where batchnum = ?";
		List<Object> params8 = new ArrayList<Object>();
		params8.add(batchnum);
		msgDao.doupdate1(sql8,params8);
		return ret;
	}

	// 检查用户限额
	public String limitCheck(String batchnum, String uid) {
		String ret = "";

		int limit = 0;
		long count = 0;
		String sql1 = "select limit from LimitMst where userid='test'";
		List<Object> params1 = new ArrayList<Object>();
		limit = msgDao.qchannel(sql1,params1);
		String sql2 = "select sum(msgcount) from TempMst where batchnum=?";
		count = msgDao.qcount(sql2,batchnum);

		if (limit < count) {
			ret = "用户短信额度不足。剩余额度：" + limit + "，发送短信数量：" + count;
		}

		return ret;
	}

	// 短信送入待发队列
	public String batchReady(String batchnum, String uid) {
		String ret = "";
		int limit = 0;
		int count = 0;
//		String sql1 = "insert into MsgMst(userid,batchnum,mobilenum,content,status,channelid,msgsp,sign,billinglen,price,submittime,pathname,msgcount)  "
//				+ "select userid,batchnum,mobilenum,content,status,channelid,msgsp,sign,billinglen,price,submittime,pathname,msgcount from TempMst "
//				+ "where batchnum='" + batchnum + "'";
		String sql="from TempMst where batchnum=?";
		ArrayList list=new ArrayList();
        list=(ArrayList)msgDao.getTemp(sql,batchnum);
		if(list!=null&&list.size()>0){
			Iterator it=(Iterator)list.iterator();
			while(it.hasNext()){
				Hashtable ht=(Hashtable)it.next();
				String userid=(String)ht.get("userid");
				String mobile=(String)ht.get("mobile");
				String content=(String)ht.get("content");
				String status=(String)ht.get("status");
				String chanelid=(String)ht.get("chanelid");
				String msgsp=(String)ht.get("msgsp");
				String sign=(String)ht.get("sign");
				String billinglen=(String)ht.get("billinglen");
				String price=(String)ht.get("price");
				String submittime=(String)ht.get("submittime");
				
				String msgcount=(String)ht.get("msgcount");
				MsgMst mm=new MsgMst();
				mm.setBatchnum(batchnum);
				mm.setUserid(userid);
				mm.setMobilenum(mobile);
				mm.setContent(content);
				mm.setStatus(Integer.parseInt(status));
				mm.setChannelid(Integer.parseInt(chanelid));
				mm.setMsgsp(Integer.parseInt(msgsp));
				mm.setSign(sign);
				mm.setBillinglen(Integer.parseInt(billinglen));
				mm.setPrice(Double.valueOf(price));
				mm.setMsgcount(Integer.parseInt(msgcount));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try{
					Date submittimes = sdf.parse(submittime);
					mm.setSubmittime(submittimes);
				}catch(Exception e){}
				msgDao.save(mm);
			}
		}

		String sql2 = "delete from TempMst where batchnum=?";
		//msgDao.doupdate1(sql1);
		List<Object> params2 = new ArrayList<Object>();
		params2.add(batchnum);
		msgDao.doupdate1(sql2,params2);

		return ret;
	}

	// 发送全部短信
	public String batchSend(String batchnum, String uid, MsgResults results) {
		String ret = "";

		// 号码集合
		ArrayList nums = new ArrayList();
		ret = getNums(batchnum, nums, false);
		if (ret.length() > 0) {
			nums.clear();
			return ret;
		}

		// 循环发送
		for (int i = 0; i < nums.size(); i++) {
			String num = (String) nums.get(i);
			send(batchnum, num, uid, results);
		}
		nums.clear();

		return ret;
	}

	// 减用户余额
	public int reduceLimit(String uid, int reduction) {
		int ret = 0;
		String sql0 = "update LimitMst set limit=limit-? where userid=?";
		List<Object> params0 = new ArrayList<Object>();
		params0.add(reduction);
		params0.add(uid);
		msgDao.doupdate1(sql0,params0);
		String sql1 = "select limit from LimitMst where userid=?";
		List<Object> params1 = new ArrayList<Object>();
		params1.add(uid);
		ret = msgDao.qchannel(sql1,params1);
		return ret;
	}

	// 发送一条短信
	public String send(String batchnum, String mobileNum, String uid,
			MsgResults results) {
		String ret = "";
		String content = "";
		double price = 0;
		int billinglen = 0;
		// String sql = "select content,price,billinglen from bo_mobile_msg
		// where batchnum='"+batchnum+"' and mobilenum='"+mobileNum+"'";
		String sql = "from MsgMst where batchnum=? and mobilenum=?";
		Hashtable ht = new Hashtable();
		ht = msgDao.qmsg1(sql,batchnum,mobileNum);

		content = (String) ht.get("content");
		price = Double.parseDouble((String) ht.get("price"));
		billinglen = Integer.parseInt((String) ht.get("billinglen"));

		if (content.length() > 0) {
			ret = send(batchnum, content, mobileNum, uid);
			if (ret.equals(MsgConst.SEND_INFO_OK)) {
				results.put(mobileNum, MsgConst.SEND_FLAG_OK);
				int msg_count = calcMsgCount(content.length(), billinglen);
				results.addMsgCount(msg_count);
				results.addFee(mobileNum, price * msg_count);
			} else {
				results.put(mobileNum, ret);
			}
		} else {
			ret = "内容为空";
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, "内容为空。批次：" + batchnum
			// + "，接收号码：" + mobileNum);
		}

		// 更新短信发送状态
		updateMsgStatus(batchnum, mobileNum, ret);

		return ret;
	}

	// 更新表中短信状态
	public void updateMsgStatus(String batchnum, String mobileNum, String ret) {
		int status = 0;
		if (ret.equals(MsgConst.SEND_INFO_OK))
			status = MsgConst.SEND_STATUS_OK;
		else if (ret.equals(MsgConst.SEND_INFO_EXIST_FILTER_WORD)
				|| ret.equals(MsgConst.SEND_INFO_INVALID_CONTENT))
			status = MsgConst.SEND_STATUS_INVALID_CONTENT;
		else if (ret.equals(MsgConst.SEND_INFO_INVALID_NUM))
			status = MsgConst.SEND_STATUS_INVALID_NUM;
		else if (ret.equals(MsgConst.SEND_INFO_NO_MONEY))
			status = MsgConst.SEND_STATUS_NO_MONEY;
		else if (ret.equals(MsgConst.SEND_INFO_INVALID_PARAM)
				|| ret.equals(MsgConst.SEND_INFO_INVALID_USER)
				|| ret.equals(MsgConst.SEND_INFO_PERMISSION_DENY))
			status = MsgConst.SEND_STATUS_API_ERR;
		else if (ret.equals(MsgConst.SEND_INFO_EXCEED_LIMIT))
			status = MsgConst.SEND_STATUS_EXCEED_LIMIT;
		else if (ret.equals(MsgConst.SEND_INFO_INVALID_SERVICE))
			status = MsgConst.SEND_STATUS_NO_SERVICE;
		else
			status = MsgConst.SEND_STATUS_OTHER_ERR;

		String sql = "update MsgMst set status=?,sendtime=sysdate where status=1 and batchnum=? and mobilenum=?";
		List<Object> params = new ArrayList<Object>();
		params.add(status);
		params.add(batchnum);
		params.add(mobileNum);
		msgDao.doupdate1(sql,params);
	}

	// 发送一条短信
	public String send(String batchnum, String content,
			String mobileNum, String uid) {
		String function = "LY";
		// content = UtilCode.decode(content);
		// mobileNum = UtilCode.decode(mobileNum);
		String successtitle = MsgConst.SEND_INFO_OK;
		String functionkey = "FUNCTION_" + function;
		SendSMSFactory factory = new SendSMSFactory();
		if (MsgConst.SEND_ON) {
			ReturnModel model = factory.factory(functionkey, content,
					mobileNum, successtitle);
			String err = model.getReturnvalue();
			if (err.equals(MsgConst.ERR_INFO_500)) {
				err = MsgConst.ERR_INFO_TOO_LONG;
			}
			// if (!MsgConst.SEND_INFO_OK.equals(err))
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, err + "。批次：" +
			// batchnum
			// + "，接收号码：" + mobileNum);
			return err;
		} else {
			return MsgConst.SEND_INFO_OK;
		}
	}

	// 获取一批短信的号码
	public String getNums(String batchnum, ArrayList nums, boolean isTemp) {
		String ret = "";

		String sql;
		if (isTemp) {
			sql = "from TempMst where batchnum=?";
			ArrayList list=new ArrayList();
			list=(ArrayList)msgDao.qnums(sql,batchnum);
			if(list!=null&&list.size()>0){
				Iterator it=list.iterator();
				while(it.hasNext()){
					Hashtable ht=(Hashtable)it.next();
					String num=ht.get("MOBILE").toString();
					nums.add(num);
				}
			}
		} else {
			sql = "from MsgMst where batchnum=?";
			ArrayList list=new ArrayList();
			list=(ArrayList)msgDao.qnums1(sql,batchnum);
			if(list!=null&&list.size()>0){
				Iterator it=list.iterator();
				while(it.hasNext()){
					Hashtable ht=(Hashtable)it.next();
					String num=ht.get("MOBILE").toString();
					nums.add(num);
				}
			}
		}
      
		return ret;
	}

	// 删除一批短信
	public int delBatch(String batchnum) {
		int ret = 0;
		String sql = "delete from TempMst where batchnum=?";
		List<Object> params = new ArrayList<Object>();
		params.add(batchnum);
		msgDao.doupdate1(sql,params);
		return ret;
	}

	
	// 向手机号hashmap添加或更新手机号，包括重复性检查
	// 返值：0=错误, 1=添加成功, 2=更新成功, 3=重复未更新
	public int addNumToHashMap(HashMap<String, String> map,
			ArrayList newlist, String name, String num) {
		if (map.containsKey(num)) {
			String oldname = map.get(num);
			if (name != null && name.length() > 0
					&& (oldname == null || oldname.length() == 0)) {
				map.put(num, name);
				return 2;
			} else {
				return 3;
			}
		} else {
			map.put(num, name);
			newlist.add(num);
			return 1;
		}
	}

	// 手机号hashmap转换为字符串,老手机号部分保持不变，新手机号重起一行排在后面
	public String hashMap2String(HashMap<String, String> map,
			ArrayList oldlist, ArrayList newlist, String oldNums) {
		StringBuffer ret = new StringBuffer();

		for (int i = 0; i < oldlist.size(); i++) {
			String num = String.valueOf(oldlist.get(i));
			String name = String.valueOf(map.get(num));
			addNumToStr(ret, name, num);
		}

		if (newlist.size() > 0) {
			if (ret.length() > 0) {
				//ret.append("\n\n");
				ret.append("");
			}

			for (int i = 0; i < newlist.size(); i++) {
				String num = String.valueOf(newlist.get(i));
				String name = String.valueOf(map.get(num));
				addNumToStr(ret, name, num);
			}
		}

		return ret.toString();
	}

	// 添加号码到字符串
	public void addNumToStr(StringBuffer s, String name, String num) {
		if (name.equals("null") || name.equals("")) {
			s.append(num + ", ");
		} else {
			s.append(num + "[" + name + "], ");
		}
	}

	// 从地址簿中取出手机号(号码簿)
	public String getNumsFromPhonebook(String newNums, String oldNums, StringBuffer nums) {
		String ret = "";

		// 新老手机号顺序list
		ArrayList oldlist = new ArrayList();
		ArrayList newlist = new ArrayList();

		// 新手机号map
		HashMap<String, String> newmap = new HashMap<String, String>();

		// 老手机号转换为HashMap
		HashMap<String, String> map = new HashMap<String, String>();
		ret = this.mobileNumCheck(oldNums, map, oldlist);
		if (ret.length() > 0) {
			nums.setLength(0);
			nums.append(oldNums);
			return ret;
		}

		// 从地址簿返回字符串中取出userid字符串
		String[] namelist = newNums.split(",");
		String nameSql = "";
		List params = new ArrayList();
		for (int i = 0; i < namelist.length; i++) {
			if (nameSql.length() == 0) {
				nameSql += "?";
			} else {
				nameSql += ",?";
			}
			params.add(namelist[i]);
		}

		int addCount = 0;
		int updateCount = 0;
		int errCount = 0;
		int dupCount = 0;

		// String sql = "select name, mobilenum from BO_PHONEBOOK where cid in
		// ("
		// + nameSql + ")";
		String sql = " from PhonebookMst where cid in (" + nameSql + ")";

		ArrayList list = new ArrayList();// 根据所选的cid返回姓名，电话号码信息
		list = msgDao.getSelectnum(sql,params);
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			String uid = "";
			String num = "";
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				uid = (String) ht.get("name");
				num = (String) ht.get("mobile");

				// 检查号码
				if (!numFormatCheck(num)) {
					errCount++;
					continue;
				}

				// 向手机号hashmap添加或更新手机号，包括重复性检查
				switch (addNumToHashMap(map, newlist, uid, num)) {
				case 1: // 添加成功
					addCount++;
					break;
				case 2: // 更新成功
					updateCount++;
					break;
				case 3: // 重复未更新
					dupCount++;
					break;
				default:
					errCount++;
				}
			}
		}

		nums.append(hashMap2String(map, oldlist, newlist, oldNums));

		if (addCount > 0) {
			ret = String.format("共新增%d人", addCount);
		}
		if (updateCount > 0) {
			if (ret.length() > 0) {
				ret = String.format("%s，更新%d人信息", ret, updateCount);
			} else {
				ret = String.format("共更新%d人信息", updateCount);
			}
		}
		if (dupCount > 0) {
			if (ret.length() > 0) {
				ret = String.format("%s，%d人信息重复未添加", ret, dupCount);
			} else {
				ret = String.format("共%d人信息重复未添加", dupCount);
			}
		}
		return ret;
	}

	// 获取批号
	public String getBatchnum(String itcode) {
		// String ret = String.format("%s-%2$ty%2$tm%2$td%2$tH%2$tM%2$tS",
		// itcode,
		// new Date());
		// String ret = String.format("%s-%2$tQ", itcode, new Date());

		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		String ret = String.format("%s-%2$ty%2$tm%2$td-%2$tH%2$tM%2$tS",
				itcode, Calendar.getInstance());

		return ret;
	}

	//
	// // 短信发送部分公用处理函数
	public String handleMsgUtil(String content, HashMap<String, String> nums,
			String batchnum, String uid) {
		String ret = "";

		// 分解短信，写入临时表
		ret = this.splitMsg(content, nums, batchnum, uid);
		if (ret.length() > 0) {
			nums.clear();
			this.delBatch(batchnum);
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, ret);
			return ret;
		}

		// 清除号码，后续操作均以数据库中数据为准
		nums.clear();

		// //// // 检查:24小时内不能给同一手机号发送相同短信
		// ret = this.check24hour(batchnum, uid);
		// if (ret.length() > 0) {
		// this.delBatch(batchnum);
		// //MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, ret);
		// return ret;
		// }
		// ////
		// 选择通道和填写运营商信息
		ret = this.chooseChannel(batchnum, uid);
		if (ret.length() > 0) {
			this.delBatch(batchnum);
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, ret);
			return ret;
		}

		// 检查用户限额
		ret = this.limitCheck(batchnum, uid);
		if (ret.length() > 0) {
			this.delBatch(batchnum);
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, ret);
			return ret;
		}

		// 短信送入待发队列
		ret = this.batchReady(batchnum, uid);
		if (ret.length() > 0) {
			this.delBatch(batchnum);
			// MsgLog.add(uid, MsgConst.LOG_TYPE_SEND_ERR, ret);
			return ret;
		}
		// 短信发送结果集合
		MsgResults results = new MsgResults();

		// 发送全部短信
		ret = this.batchSend(batchnum, uid, results);
		if (ret.length() > 0) {
			return ret;
		}

		// 减用户余额
		int limit = this.reduceLimit(uid, results.getMsgCount());

		ret = this.getResultStr(uid, results, limit);
		results.clear();

		ret = String.format("短信发送完成，短信批号=%s。\n%s", batchnum, ret);

		return ret;
	}

	public String getResultStr(String uid, MsgResults r, int limit) {
		String ret = "";
		int okCount = r.getOkCount();
		int errCount = r.getErrCount();
		int msgCount = r.getMsgCount();

		if (okCount > 0) {
			ret = String.format("发送成功%d人", okCount);
		}
		if (errCount > 0) {
			if (ret.length() > 0) {
				ret = String.format("%s，失败%d人", ret, errCount);
			} else {
				ret = String.format("失败%d人", errCount);
			}
		}
		if (msgCount > 0) {
			if (ret.length() > 0) {
				ret = String.format("%s。折合短信%d条", ret, msgCount);
			} else {
				ret = String.format("折合短信%d条", msgCount);
			}
		}
		if (ret.length() > 0) {
			ret = String.format("%s。剩余短信额度%d条。", ret, limit);
		} else {
			ret = String.format("剩余短信额度%d条。", limit);
		}
		if (errCount > 0) {
			ret = String.format("%s\n失败原因如下：\n%s", ret, r.getErrStr());
		}

		return ret;
	}

	public int calcMsgCount(int content_len, int billinglen) {
		int msg_count = (int) Math.ceil(((double) content_len / billinglen));
		return msg_count;
	}

	/**
	 * 从数据库取分组，分组list显示
	 * 
	 * @param list
	 * @return
	 */
	public String getListtype(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Hashtable hs = (Hashtable) it.next();

				// String groupid = hs.get("GROUPID").toString();
				String groupname = hs.get("GROUPNAME").toString();
				sb.append("<option value='").append(groupname).append("'>")
						.append(groupname).append("</option >");

			}
		}
		return sb.toString();
	}

	

	/**
	 * 号码簿选号 显示用户查询的结果页面
	 * 
	 * @param list
	 * @param rownum
	 * @param lineNumber
	 * @param pageNow
	 * @return
	 */
	public String getSelectList(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			int i = 1;
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				String id = ht.get("CID").toString();
				sb.append("<tr>\n");
				sb
						.append("<td class ='actionsoftReportData' align='center'>"
								+ i
								+ "&nbsp;<input type=checkbox name='chk' id='chk' value ="
								+ ht.get("CID").toString() + " ></td>");
				// sb.append("<td class ='actionsoftReportData' align='center'>"
				// + i +
				// "</td>");
				sb.append("<td align='center' nowrap>"
						+ ht.get("GROUPNAME").toString() + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ ht.get("NAME").toString() + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ ht.get("MOBILENUM").toString() + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ ht.get("EXTEND1").toString() + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ ht.get("EXTEND2").toString() + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ ht.get("EXTEND3").toString() + "</td>\n");
				sb.append("</tr>\n");
				i++;
			}
		}
		return sb.toString();
	}
	public void setMsgDao(MsgDao MsgDao) {
		this.msgDao = MsgDao;
	}


	public void setZqbMeetingManageService(
			ZqbMeetingManageService zqbMeetingManageService) {
		this.zqbMeetingManageService = zqbMeetingManageService;
	}
	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}
	public void setOrgDepartmentService(OrgDepartmentService orgDepartmentService) {
		this.orgDepartmentService = orgDepartmentService;
	}
	public String getcompanyphonelist(String useridname) {
		StringBuffer phone = new StringBuffer("");
		StringBuffer sql = new StringBuffer("SELECT B.USERNAME,B.MOBILE FROM ORGUSER B,BD_MDM_KHQXGLB A WHERE A.KHMC=B.DEPARTMENTNAME AND B.MOBILE IS NOT NULL AND (A.KHFZR=? OR A.FHSPR=? OR A.ZZCXDD=? OR A.ZZSPR=? OR A.GGFBR=? OR A.CWSCBFZR2=? OR A.CWSCBFZR3=?)");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs =null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, useridname);
			stmt.setString(2, useridname);
			stmt.setString(3, useridname);
			stmt.setString(4, useridname);
			stmt.setString(5, useridname);
			stmt.setString(6, useridname);
			stmt.setString(7, useridname);
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(2) != null){
					phone.append(rs.getString(2) + "[" + rs.getString(1) + "];");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return phone.toString();
	}
	public String getcompanyphonelistcw() {
		StringBuffer phone = new StringBuffer("");
		StringBuffer sql = new StringBuffer("SELECT B.USERNAME,B.MOBILE FROM ORGUSER B,BD_ZQB_KH_BASE A WHERE A.CUSTOMERNAME=B.DEPARTMENTNAME AND B.MOBILE IS NOT NULL");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs =null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(2) != null){
					phone.append(rs.getString(2) + "[" + rs.getString(1) + "];");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return phone.toString();
	}

	public String getphonegroup() {
		List<HashMap> zb = new ArrayList<HashMap>();
		HashMap map;
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer sql = new StringBuffer("SELECT B.ZB,S.INSTANCEID FROM BD_GE_PHONEGROUP B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='通讯录分组表单') AND B.CREATEUSER=?");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs =null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			 rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null){
					map = new HashMap();
					map.put("ZB",rs.getString(1));
					map.put("INSTANCEID",rs.getString(2));
					zb.add(map);
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		JSONArray json = JSONArray.fromObject(zb);
		return json.toString();
	}
	/*public String getphonegrouplist(String groupname) {
		StringBuffer zbdetail = new StringBuffer("");
		StringBuffer sql = new StringBuffer("SELECT NAME,TEL FROM BD_GE_PHONEBOOK  WHERE ZB LIKE ?");
		Connection conn = DBUtil.open();
		PreparedStatement stmt=null;
		ResultSet rs =null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, "%"+groupname+"%");
			rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getString(1) != null){
					zbdetail.append(rs.getString(1) + "," + rs.getString(2) + ",");
				}
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		return zbdetail.toString();
	}*/
	public List<HashMap> getallphone() {
		String userid = UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUserid();
		List<HashMap> root = new ArrayList();
			HashMap mapall = new HashMap();
			mapall.put("id",0);
			mapall.put("name","我的通讯录");
			mapall.put("open", false); 
			mapall.put("icon","iwork_img/km/treeimg/folderopen.gif");
			mapall.put("children", msgDao.getAllChildren(userid)); 
			root.add(mapall);
		
		List<HashMap> zball = msgDao.getallphone(userid);
		for (HashMap zbmap : zball) {
			HashMap map = new HashMap();
			map.put("id",zbmap.get("id").toString());
			map.put("name",zbmap.get("name")==null?"":zbmap.get("name").toString());
			map.put("open", false); 
			map.put("icon","iwork_img/km/treeimg/folderopen.gif");
			map.put("children", msgDao.getChildren(userid,zbmap.get("name").toString())); 
			root.add(map);
		}
		return root;
	}
	
	public StringBuffer getGroupValue(String id) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		int size = 0;
		if(id.equals("custormerphonebook")){
			
		}else if(id.equals("myphonebook")){
			getGroupIds(sb2);
		}else{
			getGroupDepartmentids(id, sb, size);
		}
		
		List<HashMap> groupData = msgDao.getGroupData(sb.toString(),sb2.toString());
		StringBuffer phone = new StringBuffer();
		for (HashMap data : groupData) {
			phone.append(data.get("MOBILE")).append("[").append(data.get("USERNAME")).append("]").append(";");
		}
		return phone;
	}
	
	/**myphonebook券商号码薄,处理方式不同,获得分组下的人员ID值
	 * @param sb2 拼接id查询条件
	 */
	private void getGroupIds(StringBuffer sb2) {
		HashMap conditionMap = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String khbh = uc.get_deptModel().getDepartmentno();
		conditionMap.put("KHBH", khbh);
		List<HashMap> list = DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", conditionMap, "ID");
		List<OrgUser> currentUserList = new ArrayList<OrgUser>();//当前部门用户列表（用于去除兼职列表中的重复用户）
		for (HashMap hashMap : list) {
			String ggfbr ="";
			String fhspr ="";
			String zzspr =""; 
			String zzcxdd = "";
			String khfzr = "";
			OrgUser FHSPR = new OrgUser();
			OrgUser ZZSPR = new OrgUser();
			OrgUser ZZCXDD = new OrgUser();
			OrgUser GGFBR = new OrgUser();
			OrgUser KHFZR = new OrgUser();
			if(!"".equals(hashMap.get("FHSPR").toString())){
				fhspr=hashMap.get("FHSPR").toString().substring(0, hashMap.get("FHSPR").toString().indexOf("["));
				FHSPR=orgUserService.getUserModel(fhspr);
				if(currentUserList.contains(FHSPR)){ 
					continue;  //判断是否重复添加用户
				}else{
					currentUserList.add(FHSPR);
					sb2.append(FHSPR.getId()).append(",");
				}
			}
			if(!"".equals(hashMap.get("ZZSPR").toString())){
				zzspr=hashMap.get("ZZSPR").toString().substring(0, hashMap.get("ZZSPR").toString().indexOf("["));
				ZZSPR=orgUserService.getUserModel(zzspr);
				if(currentUserList.contains(ZZSPR)){ 
					continue;  //判断是否重复添加用户
				}else{
					currentUserList.add(ZZSPR);
					sb2.append(ZZSPR.getId()).append(",");
				}
			}
			if(!"".equals(hashMap.get("KHFZR").toString())){
				khfzr=hashMap.get("KHFZR").toString().substring(0, hashMap.get("KHFZR").toString().indexOf("["));
				KHFZR=orgUserService.getUserModel(khfzr);
				if(currentUserList.contains(KHFZR)){ 
					continue;  //判断是否重复添加用户
				}else{
					currentUserList.add(KHFZR);
					sb2.append(KHFZR.getId()).append(",");
					
				}
			}
			if(!"".equals(hashMap.get("ZZCXDD").toString())){
				zzcxdd=hashMap.get("ZZCXDD").toString().substring(0, hashMap.get("ZZCXDD").toString().indexOf("["));
				ZZCXDD=orgUserService.getUserModel(zzcxdd);
				if(currentUserList.contains(ZZCXDD)){ 
					continue;  //判断是否重复添加用户
				}else{
					currentUserList.add(ZZCXDD);
					sb2.append(ZZCXDD.getId()).append(",");
				}
			}
			if(!"".equals(hashMap.get("GGFBR").toString())){
				ggfbr=hashMap.get("GGFBR").toString().substring(0, hashMap.get("GGFBR").toString().indexOf("["));
				GGFBR=orgUserService.getUserModel(ggfbr);
				if(currentUserList.contains(GGFBR)){ 
					continue;  //判断是否重复添加用户
				}else{
					currentUserList.add(GGFBR);
					sb2.append(GGFBR.getId()).append(",");
				}
			}
		}
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
		List<HashMap> groupIds = msgDao.getGroupDepartmentid(sb.toString());
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

	public List<HashMap<String, Object>> getList() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer sql = new StringBuffer();
		sql.append("select SMS.cid,ORG.username,SMS.mobilenum,decode(SMS.status,'0','发送失败','1','发送成功') status,SMS.pathname from IWORK_SMS_MSG SMS INNER JOIN ORGUSER ORG ON ORG.USERID=UPPER(SMS.USERID)"
				+ " where SMS.userid= ? order by SMS.cid desc ");
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				BigDecimal cid = rs.getBigDecimal(1);
				String userId = rs.getString(2);
				String mobile = rs.getString(3);
				String status = rs.getString(4);
				String name = rs.getString(5);
				map.put("ID", cid);
				map.put("USERID", userId);
				map.put("MOBILE", mobile);
				map.put("STATUS", status);
				map.put("NAME", name);
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
		StringBuffer sql = new StringBuffer();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		sql.append("select ORG.userid,ORG.username,SMS.mobilenum,SMS.content,SMS.pathname from IWORK_SMS_MSG SMS INNER JOIN ORGUSER ORG ON ORG.USERID=UPPER(SMS.USERID)"
				+ " where SMS.userid= ? and SMS.cid= ? order by SMS.cid desc ");
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs=null ;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			stmt.setString(2, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String userId = rs.getString(1);
				String username = rs.getString(2);
				String mobile = rs.getString(3);
				String content = rs.getString(4);
				String name = rs.getString(5);
				jsonHtml.append("<div style='width:750px;float:left;margin:0;padding:0;margin-top:20px;' region=\"center\" border=\"false\" align=\"center\"><form name=\"messagesend\" id=\"messagesend\" method=\"post\">"
						+ "<div style='width:500px;float:left;margin:0;padding:0;margin-left:5px;'><div id='up' style='width:100%;'><div style='text-align:left;width:100%;font-weight:bold;'>接收号码：<img src='/app/plugs/sms/images/help0.png'"
						+ "title='1. 接收号码栏中最多可写200个手机号，号码之间用逗号分隔。&#13;2. 接收人姓名写在手机号后，用方括号括起。如不使用通配符%name%，可省略。&#13;&#13;示例：&#13;13811111111[张三]，13611111111' /></div><div>"
						+ "<textarea rows='5' style='width:100%;font-size:12px;line-height:normal;' id='phone' name='phone' class=\"{string:true}\"  onkeyup='phone_keyup();return false;' onfocus='hiddenCheck();return false;'>"+mobile+"["+name+"]</textarea></div></div>"
						+ "<div id='down' style='width:100%;margin-top:7px;'><div style='width:100%;'><div id='up_title_left' style='text-align:left;float:left;width:50%;font-weight:bold;'> 短信内容：<img src='/app/plugs/sms/images/help0.png' title='1. 如短信内容超过64个字符，系统自动分成多条短信发送。&#13;2. 短信内容中可包含以下通配符：&#13;%name% 姓名&#13;%attr1% 属性一（仅限号码簿中手机号）&#13;%attr2% 属性二（仅限号码簿中手机号）&#13;%attr3% 属性三（仅限号码簿中手机号）' /></div>"
						+ "<div id='up_title_right' style='float:right;width:50%;text-align:right;'></div></div><div style='width:100%;'><textarea rows='13' style='width:100%;float:clear;font-size:12px;line-height:normal;' id='content' name='content' onkeyup='content_keyup();return false;'>"+content+"</textarea></div><div style='float:right;'>"
						+ "<input type='text' readonly=true id='content_len' style='color:red;border:none;text-align:right;width:250px;' /></div><div style='width:100%;text-align:center;margin-top:5px;'><input type='text' readonly=true id='phone_count' style='color:#f05050;border:none;text-align:right;width:370px;' /></div>"
						+ "</div></div></div>");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, stmt, rs);
		}
		return jsonHtml.toString();
	}
	
	private List<HashMap> getSubOrgNodeNew(Long companyid,Long departmentid){
		List<HashMap> deptlist = msgDao.getDept(companyid, departmentid);
		//List<OrgDepartment> deptlist = orgDepartmentService.getOrgDepartmentDAO().getSubDepartmentList(departmentid);
		List<HashMap> maplist = new ArrayList();
		Long deptid = 0L;
		String deptname = "";
		for(HashMap dept:deptlist){
			deptid = Long.parseLong(dept.get("DEPTID").toString());
			deptname = dept.get("DEPTNAME").toString();
			HashMap submap = new HashMap();
			//获取用户列表
			List<OrgUser> orguserlist = orgUserService.getDeptAllUserPhoneList(deptid);
			List<HashMap> usermaplist = new ArrayList<HashMap>();
			if(orguserlist.isEmpty()){
				usermaplist = this.getSubOrgNodeNew(companyid,deptid);
				if(!usermaplist.isEmpty()){
					submap.put("isParent", true);
				}else{
					continue;
				}
			}
			//List<OrgUser> orgKhList = new ArrayList<OrgUser>();
			if(usermaplist.isEmpty()){
				for (OrgUser user: orguserlist) {
					HashMap subdata = new HashMap();
					usermaplist.add(subdata);
					/*if(user.getOrgroleid().toString().equals("3")){
						List<HashMap> list = getKhList(user.getExtend1(),user.getUsername());
						for (HashMap hashMap : list) {
							HashMap data = new HashMap();
							usermaplist.add(subdata);
						}
					}*/
					break;
				}
			}
			
			if(usermaplist!=null&&usermaplist.size()>0){
				submap.put("children",usermaplist);
			}
			if((!orguserlist.isEmpty()||!usermaplist.isEmpty())){
				maplist.add(submap);
				break;
			}else{
				continue;
			}
		}
		return maplist;
	}
	
	public List<HashMap> getKhList(String customerno,String username) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer("SELECT ID,USERNAME,TEL FROM BD_ZQB_KH_BASE WHERE USERNAME IS NOT NULL AND TEL IS NOT NULL AND USERNAME != ?  and not exists (select username from orguser where extend1=?) AND CUSTOMERNO=? ");
		List<HashMap> dataList=new ArrayList<HashMap>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, username);
			ps.setString(2, customerno);
			ps.setString(3, customerno);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				Long id = rs.getLong("ID");
				String tel = rs.getString("TEL");
				String uname = rs.getString("USERNAME");
				result.put("ID", id);
				result.put("USERNAME", uname);
				result.put("TEL", tel);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public String qj2bj(String src) {  
        if (src == null) {  
            return src;  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < src.length(); i++) {  
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内  
                buf.append((char) (ca[i] - CONVERT_STEP));  
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格  
                buf.append(DBC_SPACE);  
            } else { // 不处理全角空格，全角！到全角～区间外的字符  
                buf.append(ca[i]);  
            }  
        }  
        return buf.toString();  
    }
	/**
	 * 敏感词汇过滤方法
	 * sensitive_words.txt为敏感词汇库
	 */
	public String messageSendBeforeContentCheck(String content) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		File file = new File(rootpath+"\\iwork_file\\CMS_FILE\\content\\sensitive_words.txt");
		StringBuffer returntext = new StringBuffer("");
		String textLine = null;
		int i=0;
		if(file.exists()){
			InputStream fis = null;
			InputStreamReader isr = null;
			try {
				fis = new FileInputStream(file);
				isr = new InputStreamReader(fis,"GBK");
				BufferedReader reader = new BufferedReader(isr);
				while((textLine=reader.readLine())!=""){
					if(textLine==null||textLine.toString().equals("")||textLine.toString().equals("null")){
						break;
					}else{
						if(content.contains(textLine.toString())){
							if(i==0){
								returntext.append(textLine);
								i++;
							}else{
								returntext.append(","+textLine);
								i++;
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				try {
					isr.close();
				} catch (Exception e) {logger.error(e,e);}
			}
			return returntext.toString();
		}else{
			return textLine.toString();
		}
	}

}
