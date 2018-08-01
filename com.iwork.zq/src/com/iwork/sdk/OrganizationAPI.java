package com.iwork.sdk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgCompanyService;
import com.iwork.core.organization.service.OrgDepartmentService;
import com.iwork.core.organization.service.OrgUserMapService;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import org.apache.log4j.Logger;
/**
 * 组织用户管理
 * @author YangDayong
 *
 */
public class OrganizationAPI {
	private static Logger logger = Logger.getLogger(OrganizationAPI.class);
	private static Object lock = new Object();  
    private static OrganizationAPI instance; 
    private static OrgUserMapService orgUserMapService; 
    private static OrgUserService orgUserService; 
    private static OrgDepartmentService orgDepartmentService; 
    private static OrgCompanyService orgCompanyService;
    private static OrgDepartmentDAO orgDepartmentDAO;
    
    public OrganizationAPI(){
    	if(orgUserService==null){
    		orgUserService = (OrgUserService) SpringBeanUtil.getBean("orgUserService");
    	}
    	if(orgUserMapService==null){
    		orgUserMapService = (OrgUserMapService) SpringBeanUtil.getBean("orgUserMapService");
    	}
    	if(orgDepartmentService==null){
    		orgDepartmentService = (OrgDepartmentService) SpringBeanUtil.getBean("orgDepartmentService");
    	}
    	if(orgCompanyService==null){
    		orgCompanyService = (OrgCompanyService) SpringBeanUtil.getBean("orgCompanyService");
    	}
    	if(orgDepartmentDAO==null){
    		orgDepartmentDAO = (OrgDepartmentDAO) SpringBeanUtil.getBean("orgDepartmentDAO");
    	}
    }
    
    /**
     * 实例化组织管理API
     * @return
     */
    public static OrganizationAPI getInstance(){  
        if (instance == null){  
            synchronized( lock ){  
                if (instance == null){  
                    instance = new OrganizationAPI();  
                }
            }  
        }  
        return instance;  
    }
    
    
    /**
     * 添加用户
     * @param model
     * @return
     */
    public void addUser(OrgUser model){
    	orgUserService.addBoData(model);
    }
    /**
     * 注销指定用户
     * @param userid
     * @return
     */
    public boolean disabledUser(String userId){
    	boolean flag = false;
    	flag = UserContextUtil.getInstance().checkAddress(userId);
    	orgUserService.disable(userId);
    	return flag;
    }
    
    /**
     * 激活指定用户
     * @param userid
     * @return
     */
    public boolean activeUser(String userId){
    	boolean flag = false;
    	UserContext uc = UserContextUtil.getInstance().getUserContext(userId);
    	OrgUser model = uc.get_userModel();
    	if(model!=null&&model.getUserstate()!=null&&model.getUserstate().equals(new Long(1))){
    		orgUserService.activating(userId);
    		flag = true;
    	}
    	return flag;
    }
    
    
    /**
     * 更新用户帐号信息
     * @param model
     */
    public void updateUser(OrgUser model){
    	orgUserService.updateBoData(model);
    }
    
    /**
     * 修改指定用户所在的部门
     * @param userId
     * @param targetDeptId  要移动到的目标部门id
     */
    public boolean updateUser(String userId,Long targetDeptId){
    	boolean flag = false;
    	OrgUser model = orgUserService.getUserModel(userId);
    	if(model!=null){
    		OrgDepartment deptModel = orgDepartmentService.getBoData(targetDeptId);
    		if(deptModel!=null){
    			model.setDepartmentid(targetDeptId);
    			orgUserService.updateBoData(model);
    			flag = true;
    		}
    	}
    	return flag;
    }
    /**
     * 添加部门
     * @param model
     */
    public void addDepartment(OrgDepartment model){
    	orgDepartmentService.addBoData(model);
    }
    
    /**
     * 更新部门
     * @param model
     */
    public void updateDepartment(OrgDepartment model){
    	orgDepartmentService.updateBoData(model);
    }
    
    /**
     * 变更部门路径
     * @param deptId 要变更的部门
     * @param newParentDeptId 新的父部门ID
     * @return
     */
    public boolean updateDepartment(Long deptId,Long newParentDeptId){
    	boolean flag = false;
    	OrgDepartment model = orgDepartmentService.getBoData(deptId);
    	if(model!=null){
    		model.setParentdepartmentid(newParentDeptId);
        	orgDepartmentService.updateBoData(model);
        	flag = true;
    	}
    	return flag;
    } 
    
    /**
     * 查询用户
     */
    public OrgUser getOrguserModel(String userid){
    	OrgUser orguser = orgUserService.getUserModel(userid);
    	if(orguser!=null){
    		return orguser;
    	}
    	return null;
    }
    
    /**
     * 查询子部门
     */
    public List<OrgDepartment> getSubDepartmentList(String companyId,Long parentid){
    	return orgDepartmentDAO.getSubDepartmentList(Long.parseLong(companyId), parentid);
    }
    /**
     * 查询部门下所有用户
     * 
     */
    public List<OrgUser> getDeptAllUserList(Long deptId){
    	return orgUserService.getDeptAllUserList(deptId);
    }
    
    public List<HashMap<String, String>> getOrgUserMap(Long deptId,String userid) {
		List<HashMap<String,String>> listMap=new ArrayList<HashMap<String,String>>();
		StringBuffer sql=new StringBuffer("select distinct org.username username,org.userid userid from orguser org left join orgusermap orgmap on org.userid=orgmap.userid where (org.departmentid=? or orgmap.departmentid=?) and org.userid<>?");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setLong(1, deptId);
			stmt.setLong(2, deptId);
			stmt.setString(3, userid);
			rset = stmt.executeQuery();
			while (rset.next()) {
				String uname = rset.getString("username")==null?"":rset.getString("username").toString();
				String uid = rset.getString("userid")==null?"":rset.getString("userid").toString();
				HashMap<String,String> map=new HashMap<String,String>();
				map.put("USERNAME", uname);
				map.put("USERID", uid);
				listMap.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return listMap;
	}
}
