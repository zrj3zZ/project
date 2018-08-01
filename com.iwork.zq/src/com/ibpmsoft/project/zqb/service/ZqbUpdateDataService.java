package com.ibpmsoft.project.zqb.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import org.apache.log4j.Logger;
public class ZqbUpdateDataService {
    
    /**
     * 
     * @param entityName 表名
     * @param parameter 需要修改的字段名以及值
     * @param columnName 判断条件及值
     */
	private static Logger logger = Logger.getLogger(ZqbUpdateDataService.class);
    public void createSql(String entityName,HashMap<String,String> parameter,HashMap<String,String> columnName){
    	List<String> saveParameter=new ArrayList<String>();//存放参数
    	String sql1 ="";
    	StringBuffer sql=new StringBuffer("UPDATE "+entityName+" SET ");
    	Set<String> keySetParameter = parameter.keySet();
    	for (String string : keySetParameter) {
    		sql.append(string+"=?,");
			saveParameter.add(parameter.get(string).toString());
		}
    	sql1=sql.substring(0, sql.toString().length()-1);
    	sql=new StringBuffer();
    	sql.append(" WHERE 1=2 ");
    	Set<String> keySet = columnName.keySet();
    	for (String string : keySet) {
    		sql.append(" OR "+string+"=? ");
			saveParameter.add(columnName.get(string).toString());
		}
    	sql1 += sql.substring(0, sql.toString().length()-1);
    	Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		final String buildsql = sql1;
		try {
			ps = conn.prepareStatement(buildsql);
			for (int i = 0; i < saveParameter.size(); i++) {
				ps.setString(i+1, saveParameter.get(i));
			}
			int executeUpdate = ps.executeUpdate();
		} catch (Exception e) {
			
		} finally{
			DBUtil.close(conn, ps, rs);
		}
    }
    
    public void updateDataDepartment(OrgDepartment oldDept,OrgDepartment newDept){
    	String beforeDepartmentName = oldDept.getDepartmentname();
    	String afterDepartmentName = newDept.getDepartmentname();
    	String deptId = newDept.getId().toString();
    	String departmentno = newDept.getDepartmentno();
    	boolean isDeptName=false;
    	if(!beforeDepartmentName.equals(afterDepartmentName)&&!afterDepartmentName.equals("")){
    		isDeptName=true;
    	}
    	if(isDeptName){
    		HashMap<String,String> setMap = new HashMap<String,String>();
    		HashMap<String,String> columnMap = new HashMap<String,String>();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD__FZGSGLZB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_ZQB_GLF",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_ZQB_GGJBXXB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_ZQB_WBYH",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_GF_GPCGQK",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		/*setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_GF_CGXZ",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();*/
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_GF_JXSQK",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_GF_GFZRQK",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CUSTOMERNAME", afterDepartmentName);
    		columnMap.put("CUSTOMERNAME", beforeDepartmentName);
    		createSql("BD_ZQB_KH_BASE",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CUSTOMERNAME", afterDepartmentName);
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("CUSTOMERNAME", beforeDepartmentName);
    		createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("SSSYB", afterDepartmentName);
    		columnMap.put("SSSYB", beforeDepartmentName);
    		createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("FZJGMC", afterDepartmentName);
    		columnMap.put("FZJGMC", beforeDepartmentName);
    		createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("DEPARTMENTNAME", afterDepartmentName);
    		columnMap.put("DEPARTMENTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_GROUP",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_PM_GROUP",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_PM_TASK",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("XMMC", afterDepartmentName);
    		columnMap.put("XMMC", beforeDepartmentName);
    		createSql("BD_ZQB_XMWTFK",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_XMRWGLLCB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("XMMC", afterDepartmentName);
    		columnMap.put("XMMC", beforeDepartmentName);
    		createSql("BD_ZQB_XMSPRWH",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CUSTOMERNAME", afterDepartmentName);
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("FZJGMC", afterDepartmentName);
    		columnMap.put("FZJGMC", beforeDepartmentName);
    		createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("SSSYB", afterDepartmentName);
    		columnMap.put("SSSYB", beforeDepartmentName);
    		createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_ZKSPRQ",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_NHSPRQ",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CUSTOMERNAME", afterDepartmentName);
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CZBM", afterDepartmentName);
    		columnMap.put("CZBM", beforeDepartmentName);
    		createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CLBM", afterDepartmentName);
    		columnMap.put("CLBM", beforeDepartmentName);
    		createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_GPFXXMRWB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("PROJECTNAME", afterDepartmentName);
    		columnMap.put("PROJECTNAME", beforeDepartmentName);
    		createSql("BD_ZQB_GPFXXMRWLCB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("XMMC", afterDepartmentName);
    		columnMap.put("XMMC", beforeDepartmentName);
    		createSql("BD_ZQB_GPFXXGWTB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CUSTOMERNAME", afterDepartmentName);
    		columnMap.put("CUSTOMERNAME", beforeDepartmentName);
    		createSql("BD_ZQB_XMJSXXGLB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CUSTOMERNAME", afterDepartmentName);
    		columnMap.put("CUSTOMERNAME", beforeDepartmentName);
    		createSql("BD_MEET_PLAN",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_MEET_FKZLQD",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("CUSTOMERNAME", afterDepartmentName);
    		columnMap.put("CUSTOMERNAME", beforeDepartmentName);
    		createSql("BD_MEET_SHSPSM",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_XP_BASEINFO",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_BASE_ZDQDXX",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_XP_CNXXGL",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_MEET_QTGGZL",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_XP_QTGGZLLC",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("GSMC", afterDepartmentName);
    		columnMap.put("GSMC", beforeDepartmentName);
    		createSql("BD_XP_HFQKB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_XP_NMXX",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_XP_GGLJX",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("POSITION", afterDepartmentName);
    		columnMap.put("POSITION", beforeDepartmentName);
    		createSql("BD_XP_GZSCSXFKRB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("KHMC", afterDepartmentName);
    		columnMap.put("KHMC", beforeDepartmentName);
    		createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("DEPARTMENTNAME", afterDepartmentName);
    		setMap.put("EXTEND2", afterDepartmentName);
    		columnMap.put("DEPARTMENTNAME", beforeDepartmentName);
    		createSql("ORGUSER",setMap,columnMap);
    		setMap.clear();
    		columnMap.clear();
    		setMap.put("DEPARTMENTNAME", afterDepartmentName);
    		columnMap.put("DEPARTMENTID", deptId);
    		createSql("ORGUSERMAP",setMap,columnMap);
    	}
    }
    
    public void updateDataOrgUser(OrgUser oldUser,OrgUser newUser){
    	String beforeUsername = oldUser.getUsername();
    	String beforeDepartmentname = oldUser.getDepartmentname();
    	Long beforeOrgroleid = oldUser.getOrgroleid();
    	String beforeMobile = oldUser.getMobile();
    	String beforeEmail = oldUser.getEmail();
    	String beforeOfficetel = oldUser.getOfficetel();
    	
    	String afterUsername = newUser.getUsername();
    	String afterDepartmentname = newUser.getDepartmentname();
    	Long afterOrgroleid = newUser.getOrgroleid();
    	String afterMobile = newUser.getMobile();
    	String afterEmail = newUser.getEmail();
    	String afterOfficetel = newUser.getOfficetel();
    	String Extend1 = newUser.getExtend1();
    	String userid = newUser.getUserid();
    	
    	String userName=userid+"["+afterUsername+"]";
    	HashMap userRole = getUserRole();
    	boolean isUsername=false;
    	boolean isDeptName=false;
    	boolean isOrgroleid=false;
    	boolean isMobile=false;
    	boolean isEmail=false;
    	boolean isOfficetel=false;
    	if(!beforeUsername.equals(afterUsername)&&!afterUsername.equals("")){
    		isUsername=true;
    	}
    	if(!beforeDepartmentname.equals(afterDepartmentname)&&!afterDepartmentname.equals("")){
    		isDeptName=true;
    	}
    	if(beforeOrgroleid!=afterOrgroleid){
    		isOrgroleid=true;
    	}
    	if(beforeMobile!=null&&!beforeMobile.equals(afterMobile)&&afterMobile!=null&&!afterMobile.equals("")){
    		isMobile=true;
    	}
    	if(beforeEmail!=null&&!beforeEmail.equals(afterEmail)&&afterEmail!=null&&!afterEmail.equals("")){
    		isEmail=true;
    	}
    	if(beforeOfficetel!=null&&!beforeOfficetel.equals(afterOfficetel)&&afterOfficetel!=null&&!afterOfficetel.equals("")){
    		isOfficetel=true;
    	}
    	if(isUsername||isDeptName||isOrgroleid||isMobile||isEmail||isOfficetel){
    		HashMap<String,String> setMap = new HashMap<String,String>();
    		HashMap<String,String> columnMap = new HashMap<String,String>();
    		if(isUsername||isMobile||isEmail||isOfficetel||isDeptName||isOrgroleid){
    			if(isUsername){
    				setMap.put("NAME", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("PHONE", afterMobile);
    			}
    			if(isEmail){
    				setMap.put("EMAIL", afterEmail);
    			}
    			if(isOfficetel){
    				setMap.put("TEL", afterOfficetel);
    			}
    			if(isDeptName){
    				setMap.put("DEPARTMENTNAME", afterDepartmentname);
    			}
    			if(isOrgroleid){
    				setMap.put("POSITION", userRole.get(afterOrgroleid)==null?"":userRole.get(afterOrgroleid).toString());
    			}
    			columnMap.put("USERID", userid);
    			createSql("BD_ZQB_GROUP",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_PM_GROUP",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_PM_TASK",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ANSWERMAN", userName);
    			columnMap.put("SUBSTR(ANSWERMAN,0, instr(ANSWERMAN,'[',1)-1)", userid);
    			createSql("BD_ZQB_XMWTFK",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("USERNAME", afterUsername);
    			columnMap.put("USERID", userid);
    			createSql("BD_ZQB_XMWTFK",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("USERNAME", afterUsername);
    			columnMap.put("USERID", userid);
    			createSql("BD_ZQB_RETALK",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CJRXM", afterUsername);
    			columnMap.put("CJRZH", userid);
    			createSql("BD_ZQB_KM_INFO",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_ZQB_XMRWGLLCB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CSFZR", userName);
    			columnMap.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", userid);
    			createSql("BD_ZQB_XMSPRWH",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("FSFZR", userName);
    			columnMap.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", userid);
    			createSql("BD_ZQB_XMSPRWH",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ZZSPR", userName);
    			columnMap.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", userid);
    			createSql("BD_ZQB_XMSPRWH",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ZKR", userName);
    			columnMap.put("SUBSTR(ZKR,0, instr(ZKR,'[',1)-1)", userid);
    			createSql("BD_ZQB_ZKSPRQ",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("NHSPR", userName);
    			columnMap.put("SUBSTR(NHSPR,0, instr(NHSPR,'[',1)-1)", userid);
    			createSql("BD_ZQB_NHSPRQ",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ZKR", userName);
    			columnMap.put("SUBSTR(ZKR,0, instr(ZKR,'[',1)-1)", userid);
    			createSql("BD_ZQB_ZKNHSPR",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("NHSPR", userName);
    			columnMap.put("SUBSTR(NHSPR,0, instr(NHSPR,'[',1)-1)", userid);
    			createSql("BD_ZQB_ZKNHSPR",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CJRXM", afterUsername);
    			columnMap.put("CJRZH", userid);
    			createSql("BD_ZQB_TYXM_INFO",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXMRWB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXMRWLCB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ANSWERMAN", userName);
    			columnMap.put("SUBSTR(ANSWERMAN,0, instr(ANSWERMAN,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXGWTB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("USERNAME", afterUsername);
    			columnMap.put("USERID", userid);
    			createSql("BD_ZQB_GPFXXGWTB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("USERNAME", afterUsername);
    			columnMap.put("USERID", userid);
    			createSql("BD_ZQB_GPFXWTHFB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CSFZR", userName);
    			columnMap.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXMSPR",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("FSFZR", userName);
    			columnMap.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXMSPR",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ZZSPR", userName);
    			columnMap.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXMSPR",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("FSR", afterUsername);
    			columnMap.put("FSRZH", userid);
    			createSql("BD_XP_XXPLJLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CJRMC", afterUsername);
    			columnMap.put("CJRBH", userid);
    			createSql("BD_BASE_ZDQDXX",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CREATENAME", afterUsername);
    			columnMap.put("QCRID", userid);
    			createSql("BD_MEET_QTGGZL",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CREATENAME", afterUsername);
    			columnMap.put("QCRID", userid);
    			createSql("BD_XP_QTGGZLLC",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("SPR", userName);
    			columnMap.put("SUBSTR(SPR,0, instr(SPR,'[',1)-1)", userid);
    			createSql("BD_XP_ZYDDSXB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername||isMobile||isEmail||isOfficetel||isOrgroleid){
    			if(isUsername){
    				setMap.put("NAME", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("PHONE", afterMobile);
    			}
    			if(isEmail){
    				setMap.put("EMAIL", afterEmail);
    			}
    			if(isOfficetel){
    				setMap.put("TEL", afterOfficetel);
    			}
    			if(isOrgroleid){
    				setMap.put("POSITION", userRole.get(afterOrgroleid)==null?"":userRole.get(afterOrgroleid).toString());
    			}
    			columnMap.put("USERID", userid);
    			createSql("BD_XP_ZYDDSXFKRB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername||isMobile||isEmail||isOfficetel||isOrgroleid){
    			if(isUsername){
    				setMap.put("NAME", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("PHONE", afterMobile);
    			}
    			if(isEmail){
    				setMap.put("EMAIL", afterEmail);
    			}
    			if(isOfficetel){
    				setMap.put("TEL", afterOfficetel);
    			}
    			if(isOrgroleid){
    				setMap.put("POSITION", userRole.get(afterOrgroleid)==null?"":userRole.get(afterOrgroleid).toString());
    			}
    			columnMap.put("USERID", userid);
    			createSql("BD_XP_ZYDDSXCYRY",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("FSR", afterUsername);
    			columnMap.put("FSRID", userid);
    			createSql("BD_XP_TZGGB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("XM", afterUsername);
    			columnMap.put("USERID", userid);
    			createSql("BD_XP_HFQKB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("HFR", afterUsername);
    			columnMap.put("HFR", beforeUsername);
    			createSql("BD_XP_HFXXB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CREATENAME", afterUsername);
    			columnMap.put("QCRID", userid);
    			createSql("BD_XP_GGLJX",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("FSR", afterUsername);
    			columnMap.put("FSRID", userid);
    			createSql("BD_XP_GZSC",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername||isMobile||isEmail||isOfficetel||isDeptName){
    			if(isUsername){
    				setMap.put("NAME", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("PHONE", afterMobile);
    			}
    			if(isEmail){
    				setMap.put("EMAIL", afterEmail);
    			}
    			if(isOfficetel){
    				setMap.put("TEL", afterOfficetel);
    			}
    			if(isDeptName){
    				setMap.put("POSITION", afterDepartmentname);
    			}
    			columnMap.put("USERID", userid);
    			createSql("BD_XP_GZSCSXFKRB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("KHFZR", userName);
    			columnMap.put("SUBSTR(KHFZR,0, instr(KHFZR,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ZZCXDD", userName);
    			columnMap.put("SUBSTR(ZZCXDD,0, instr(ZZCXDD,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("FHSPR", userName);
    			columnMap.put("SUBSTR(FHSPR,0, instr(FHSPR,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("ZZSPR", userName);
    			columnMap.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("GGFBR", userName);
    			columnMap.put("SUBSTR(GGFBR,0, instr(GGFBR,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CWSCBFZR2", userName);
    			columnMap.put("SUBSTR(CWSCBFZR2,0, instr(CWSCBFZR2,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CWSCBFZR3", userName);
    			columnMap.put("SUBSTR(CWSCBFZR3,0, instr(CWSCBFZR3,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("QYNBRYSH", userName);
    			columnMap.put("SUBSTR(QYNBRYSH,0, instr(QYNBRYSH,'[',1)-1)", userid);
    			createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("KHFZR", userName);
    			columnMap.put("SUBSTR(KHFZR,0, instr(KHFZR,'[',1)-1)", userid);
    			createSql("BD_MDM_CXDDFPLCB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("QUNAME", afterUsername);
    			columnMap.put("QUID", userid);
    			createSql("IWORK_KNOW_QUESTION",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("AUNAME", afterUsername);
    			columnMap.put("AUID", userid);
    			createSql("IWORK_KNOW_ANSWER",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername||isMobile||isEmail||isOfficetel){
    			if(isUsername){
    				setMap.put("USERNAME", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("TEL", afterMobile);
    			}
    			if(isEmail){
    				setMap.put("EMAIL", afterEmail);
    			}
    			if(isOfficetel){
    				setMap.put("PHONE", afterOfficetel);
    			}
    			columnMap.put("CUSTOMERNO", Extend1);
    			createSql("BD_ZQB_KH_BASE",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			if(isUsername){
    				setMap.put("CREATEUSER", afterUsername);
    			}
    			columnMap.put("USERID", userid);
    			createSql("BD_ZQB_KH_BASE",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername||isMobile){
    			if(isUsername){
    				setMap.put("KHLXR", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("KHLXDH", afterMobile);
    			}
    			columnMap.put("CUSTOMERNO", Extend1);
    			createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("OWNER", userName);
    			columnMap.put("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)", userid);
    			createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CREATEUSER", afterUsername);
    			columnMap.put("CREATEUSERID", userid);
    			createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername||isMobile){
    			if(isUsername){
    				setMap.put("KHLXR", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("KHLXDH", afterMobile);
    			}
    			columnMap.put("CUSTOMERNO", Extend1);
    			createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("OWNER", userName);
    			columnMap.put("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)", userid);
    			createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CREATEUSER", afterUsername);
    			columnMap.put("CREATEUSERID", userid);
    			createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername||isMobile){
    			if(isUsername){
    				setMap.put("KHLXR", afterUsername);
    			}
    			if(isMobile){
    				setMap.put("KHLXDH", afterMobile);
    			}
    			columnMap.put("CUSTOMERNO", Extend1);
    			createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("MANAGER", userName);
    			columnMap.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("OWNER", userName);
    			columnMap.put("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)", userid);
    			createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CREATEUSER", afterUsername);
    			columnMap.put("CREATEUSERID", userid);
    			createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("USERNAME", userName);
    			columnMap.put("SUBSTR(USERNAME,0, instr(USERNAME,'[',1)-1)", userid);
    			createSql("BD_ZQB_WJTJ",setMap,columnMap);
    			setMap.clear();
    			columnMap.clear();
    		}
    		if(isUsername){
    			setMap.put("CREATENAME", afterUsername);
    			columnMap.put("KHBH", Extend1);
    			createSql("BD_XP_CNXXGL",setMap,columnMap);
    		}
    		if(isUsername){
    			setMap.put("CREATENAME", afterUsername);
    			columnMap.put("QCRID", userid);
    			createSql("BD_XP_GGLJX",setMap,columnMap);
    		}
    	}
    }
    
    public void updateDataCustomer(HashMap hash,HashMap map){
    	if(hash!=null&&!hash.isEmpty()){
    		String customerno = hash.get("CUSTOMERNO").toString();
    		String afterCustomername = map.get("CUSTOMERNAME")==null?"":map.get("CUSTOMERNAME").toString().trim();
    		String afterZqjc = map.get("ZQJC")==null?"":map.get("ZQJC").toString().trim();
    		String afterZqdm = map.get("ZQDM")==null?"":map.get("ZQDM").toString().trim();
    		String beforeCustomername = hash.get("CUSTOMERNAME")==null?"":hash.get("CUSTOMERNAME").toString().trim();
    		String beforeZqjc = hash.get("ZQJC")==null?"":hash.get("ZQJC").toString().trim();
    		String beforeZqdm = hash.get("ZQDM")==null?"":hash.get("ZQDM").toString().trim();
    		boolean isKhmc=false;
    		boolean isZqjc=false;
    		boolean isZqdm=false;
    		if(!beforeCustomername.equals(afterCustomername)&&!afterCustomername.equals("")){
    			isKhmc=true;
    		}
    		if(!beforeZqjc.equals(afterZqjc)&&!afterZqjc.equals("")){
    			isZqjc=true;
    		}
    		if(!beforeZqdm.equals(afterZqdm)&&!afterZqdm.equals("")){
    			isZqdm=true;
    		}
    		if(isKhmc||isZqjc||isZqdm){
    			HashMap<String,String> setMap = new HashMap<String,String>();
    			HashMap<String,String> columnMap = new HashMap<String,String>();
    			if(isKhmc){
    				setMap.put("CUSTOMERNAME", afterCustomername);
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_XP_GPQYCFJL",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD__FZGSGLZB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNO", "(SELECT PROJECTNO FROM BD_ZQB_PJ_BASE WHERE CUSTOMERNO='"+customerno+"')");
    				createSql("BD_ZQB_ZKSPRQ",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNO", "(SELECT PROJECTNO FROM BD_ZQB_PJ_BASE WHERE CUSTOMERNO='"+customerno+"')");
    				createSql("BD_ZQB_NHSPRQ",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_ZQB_GLF",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_ZQB_GGJBXXB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_ZQB_WBYH",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_GF_GPCGQK",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			/*if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_GF_CGXZ",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}*/
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_GF_JXSQK",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_GF_GFZRQK",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("CUSTOMERNAME", afterCustomername);
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_ZQB_PJ_BASE",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNAME", beforeCustomername);
    				createSql("BD_PM_GROUP",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNAME", beforeCustomername);
    				createSql("BD_PM_TASK",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("XMMC", afterCustomername);
    				columnMap.put("XMMC", beforeCustomername);
    				createSql("BD_ZQB_XMWTFK",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNAME", beforeCustomername);
    				createSql("BD_ZQB_XMRWGLLCB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("XMMC", afterCustomername);
    				columnMap.put("XMMC", beforeCustomername);
    				createSql("BD_ZQB_XMSPRWH",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("CUSTOMERNAME", afterCustomername);
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_ZQB_XMLCXXB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			/*if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNAME", beforeCustomername);
    				createSql("BD_ZQB_ZKSPRQ",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}*/
    			if(isKhmc){
    				setMap.put("CUSTOMERNAME", afterCustomername);
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_ZQB_GPFXXMB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNAME", beforeCustomername);
    				createSql("BD_ZQB_GPFXXMRWB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("PROJECTNAME", afterCustomername);
    				columnMap.put("PROJECTNAME", beforeCustomername);
    				createSql("BD_ZQB_GPFXXMRWLCB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("XMMC", afterCustomername);
    				columnMap.put("XMMC", beforeCustomername);
    				createSql("BD_ZQB_GPFXXGWTB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("CUSTOMERNAME", afterCustomername);
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_ZQB_XMJSXXGLB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("CUSTOMERNAME", afterCustomername);
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_MEET_PLAN",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_MEET_FKZLQD",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("CUSTOMERNAME", afterCustomername);
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_MEET_SHSPSM",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_XP_BASEINFO",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_BASE_ZDQDXX",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_XP_CNXXGL",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc||isZqdm||isZqjc){
    				if(isKhmc){
    					setMap.put("KHMC", afterCustomername);
    				}
    				if(isZqdm){
    					setMap.put("ZQDMXS", afterZqdm);
    				}
    				if(isZqjc){
    					setMap.put("ZQJCXS", afterZqjc);
    				}
    				columnMap.put("KHBH", customerno);
    				createSql("BD_MEET_QTGGZL",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc||isZqdm||isZqjc){
    				if(isKhmc){
    					setMap.put("KHMC", afterCustomername);
    				}
    				if(isZqdm){
    					setMap.put("ZQDMXS", afterZqdm);
    				}
    				if(isZqjc){
    					setMap.put("ZQJCXS", afterZqjc);
    				}
    				columnMap.put("KHBH", customerno);
    				createSql("BD_XP_QTGGZLLC",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc||isZqdm||isZqjc){
    				if(isKhmc){
    					setMap.put("KHMC", afterCustomername);
    				}
    				if(isZqdm){
    					setMap.put("ZQDMXS", afterZqdm);
    				}
    				if(isZqjc){
    					setMap.put("ZQJCXS", afterZqjc);
    				}
    				columnMap.put("KHBH", customerno);
    				createSql("BD_XP_GGLJX",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc||isZqdm){
    				if(isKhmc){
    					setMap.put("GSMC", afterCustomername);
    				}
    				if(isZqdm){
    					setMap.put("GSDM", afterZqdm);
    				}
    				columnMap.put("CUSTOMERNO", customerno);
    				createSql("BD_XP_HFQKB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_XP_NMXX",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isZqdm||isZqjc){
    				if(isZqdm){
    					setMap.put("ZQDM", afterZqdm);
    					columnMap.put("ZQDM", beforeZqdm);
    				}
    				if(isZqjc){
    					setMap.put("ZQJC", afterZqjc);
    					columnMap.put("ZQJC", beforeZqjc);
    				}
    				createSql("BD_XP_GPQYCFJL",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc||isZqdm||isZqjc){
    				if(isKhmc){
    					setMap.put("KHMC", afterCustomername);
    				}
    				if(isZqdm){
    					setMap.put("ZQDMXS", afterZqdm);
    				}
    				if(isZqjc){
    					setMap.put("ZQJCXS", afterZqjc);
    				}
    				columnMap.put("KHBH", customerno);
    				createSql("BD_XP_GGLJX",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("POSITION", afterCustomername);
    				columnMap.put("POSITION", beforeCustomername);
    				createSql("BD_XP_GZSCSXFKRB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("KHMC", afterCustomername);
    				columnMap.put("KHBH", customerno);
    				createSql("BD_MDM_KHQXGLB",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("DEPARTMENTNAME", afterCustomername);
    				columnMap.put("DEPARTMENTNO", customerno);
    				createSql("ORGDEPARTMENT",setMap,columnMap);
    				setMap.clear();
    				columnMap.clear();
    			}
    			if(isKhmc){
    				setMap.put("DEPARTMENTNAME", afterCustomername);
    				setMap.put("EXTEND2", afterCustomername);
    				columnMap.put("EXTEND1", customerno);
    				createSql("ORGUSER",setMap,columnMap);
    			}
    		}
    	}
    }
    
    public HashMap getUserRole(){
    	HashMap map = new HashMap();
    	StringBuffer sql=new StringBuffer("SELECT ID,ROLENAME FROM ORGROLE");
    	Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				Long roleId = rs.getLong("ID");
				String rolename = rs.getString("ROLENAME");
				map.put(roleId, rolename);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
    	return map;
    }
}
