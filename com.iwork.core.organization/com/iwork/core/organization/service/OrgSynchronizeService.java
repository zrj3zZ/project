package com.iwork.core.organization.service;

import java.util.HashMap;
import java.util.List;
import com.iwork.core.organization.dao.OrgSynchronizeDAO;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.plugs.extdbsrc.model.SysExdbsrcCenter;
import com.iwork.plugs.extdbsrc.util.ExtDBSourceUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.SysLogAPI;


public class OrgSynchronizeService {
	
	public static final String exDB_UUID = "52202c0b74f04ae2b7100236b700b53f";
	private OrgCompanyService orgCompanyService;
	private OrgUserService orgUserService;
	private OrgRoleService orgRoleService;
	private OrgDepartmentService orgDepartmentService;
	private OrgSynchronizeDAO orgSynchronizeDAO;
	private OrgUserMapService orgUserMapService;
	
	public String doExcute(){
		SysExdbsrcCenter model = ExtDBSourceUtil.getInstance().getExdbModel(exDB_UUID);
		//获取部门列表  
		List<OrgDepartment> deptlist =orgSynchronizeDAO.getTargetDeptList(model);
		for(OrgDepartment dept:deptlist){
			//判断当前组织机构中是否存在
			OrgDepartment localDept = orgDepartmentService.getBoData(dept.getId());
			if(localDept==null||dept.getId().equals(new Long(0))){
				//添加初始化部门 
				orgDepartmentService.addBoData(dept);
				//创建添加日志
				String logText = "新增部门["+dept.getDepartmentname()+"]";
				SysLogAPI.getInstance().addOperationLog("[组织同步]新增部门", "OrgDepartment", "0", dept.getId()+"", logText);
			}else{
				String log = this.checkDept(localDept, dept);
				if(!log.equals("")){
					orgDepartmentService.updateBoData(localDept);
					SysLogAPI.getInstance().addOperationLog("[组织同步]变更部门", "OrgDepartment", "0", dept.getId()+"", log);
				}
			} 
		} 
		//初始化一级角色列表
		List<OrgRole> roleOneList = orgSynchronizeDAO.getTargetRoleOneList(model);
		for(OrgRole role:roleOneList){ 
			OrgRole localRole = orgRoleService.getBoData(role.getId());
			if(localRole==null){
				orgRoleService.addBoData(role);
				String logText = "新增一级角色["+role.getRolename()+"]";
				SysLogAPI.getInstance().addOperationLog("[组织同步]新增角色", "OrgRole", "0", role.getId()+"", logText);
			}else{
				String log = this.checkOrgRole(localRole, role);
				if(!log.equals("")){
					orgRoleService.updateBoData(role);
					SysLogAPI.getInstance().addOperationLog("[组织同步]变更角色", "OrgRole", "0", role.getId()+"", log);
				}
			}
		}
		//初始化二级角色列表
		List<OrgRole> roleTwoList = orgSynchronizeDAO.getTargetRoleTwoList(model);
		for(OrgRole role:roleTwoList){ 
			OrgRole localRole = orgRoleService.getBoData(role.getId());
			if(localRole==null){
				orgRoleService.addBoData(role); 
				String logText = "新增二级角色["+role.getRolename()+"]";
				SysLogAPI.getInstance().addOperationLog("[组织同步]新增角色", "OrgRole", "0", role.getId()+"", logText);
			}else{
				String log = this.checkOrgRole(localRole, role);
				if(!log.equals("")){
					orgRoleService.updateBoData(role);
					SysLogAPI.getInstance().addOperationLog("[组织同步]变更角色", "OrgRole", "0", role.getId()+"", log);
				}
			}
		} 
		
		//初始化用户列表
		List<OrgUser> userlist = orgSynchronizeDAO.getTargetUserList(model);
		for(OrgUser usermodel:userlist){
			OrgUser localUserModel = orgUserService.getUserModel(usermodel.getUserid());
			if(localUserModel==null){
				orgUserService.addBoData(usermodel);
				String logText = "新增账户["+usermodel.getUsername()+"]";
				SysLogAPI.getInstance().addOperationLog("[组织同步]新增账号", "OrgUser", "0", usermodel.getUserid(), logText);
			}else{
				String log = this.checkOrgUser(localUserModel, usermodel);
				if(!log.equals("")){
					int deptid = orgSynchronizeDAO.getCurentDeptId(usermodel.getDepartmentid().intValue());
					OrgDepartment od =  orgDepartmentService.getBoData(new Long(deptid));
					OrgUser um = orgUserService.getUserModel(usermodel.getUserid());
					if(um!=null){
						usermodel.setId(um.getId());
					}
					usermodel.setDepartmentid(new Long(deptid));
					usermodel.setDepartmentname(od.getDepartmentname());
					orgUserService.updateBoData(usermodel);
					SysLogAPI.getInstance().addOperationLog("[组织同步]更新账号", "OrgUser", "0", usermodel.getUserid(), log);
				}
			}
			//初始化兼任角色
			List<OrgUserMap> userMapList = orgSynchronizeDAO.getUserMapTwoRoleForUser(model,usermodel.getUserid());
			
			//获取当前用户兼职列表
			if(userMapList!=null&&userMapList.size()>0){
				List<OrgUserMap> localRoleList = orgUserMapService.getOrgUserMapDAO().getOrgUserMapListForUser(usermodel.getUserid());
				if(localRoleList==null||localRoleList.size()==0){
					//创建角色列表
					for(OrgUserMap map:userMapList){
						 //补充部门信息
						map.setDepartmentid(usermodel.getDepartmentid()+"");
						map.setDepartmentname(usermodel.getDepartmentname());
						orgUserMapService.addBoData(map);
						String log = "添加角色["+map.getOrgrolename()+"]到["+usermodel.getUsername()+"("+usermodel.getUserid()+")]";
						SysLogAPI.getInstance().addOperationLog("[组织同步]添加兼职", "OrgUserMap", "0", usermodel.getUserid(), log);
					}
				}else{
					for(OrgUserMap map:userMapList){
						 //补充部门信息
						map.setDepartmentid(usermodel.getDepartmentid()+"");
						map.setDepartmentname(usermodel.getDepartmentname());
						boolean ishave = false;
						for(OrgUserMap localMap:localRoleList){
							if(map.getOrgroleid()!=null&&localMap.getOrgroleid()!=null&&map.getOrgroleid().equals(localMap.getOrgroleid())){
								ishave = true;
								break;
							}
						} 
						if(!ishave){
							orgUserMapService.addBoData(map);
							String log = "添加角色["+map.getOrgrolename()+"]到["+usermodel.getUsername()+"("+usermodel.getUserid()+")]";
							SysLogAPI.getInstance().addOperationLog("[组织同步]添加兼职", "OrgUserMap", "0", usermodel.getUserid(), log);
						}
					}
				}
			}
		}
		//人员期初数据导入
		List<HashMap> list = DemAPI.getInstance().getList("c492c7557d5c41f8ac0caa1c5f739746", null, null);
		if(list!=null){
			for(HashMap data:list){
				if(data.get("SFZHM")!=null){
					String idcard = data.get("SFZHM").toString();
					OrgUser orguser = orgSynchronizeDAO.getOrgUserForExtend4(idcard);
					if(orguser!=null){
						String cwUserno = "";
						String bankName = "";
						String bankNo = "";
						String rylx = "";   //人员类型
						if(data.get("CWYGBH")!=null){
							cwUserno = data.get("CWYGBH").toString();
							orguser.setExtend3(cwUserno);
						}
						if(data.get("BANK")!=null){
							bankName = data.get("BANK").toString();
							orguser.setExtend5(bankName);
						}
						if(data.get("YXZH")!=null){
							bankNo = data.get("YXZH").toString();
							orguser.setExtend6(bankNo); 
						}
						if(data.get("RYLX")!=null){
							rylx = data.get("RYLX").toString();
							orguser.setExtend7(rylx);  
						}
						orgUserService.updateBoData(orguser);
						//
						System.out.println(orguser.getUsername()+"["+orguser.getUserid()+"]银行信息同步成功---------------------------------------------------OK");
					} 
				}
			}
		}
		return "";
	}

	
	/**
	 * 检查用户
	 * @param localUserModel
	 * @param targetUserModel
	 * @return
	 */
	private String checkOrgUser(OrgUser localUserModel,OrgUser targetUserModel){
		StringBuffer log = new StringBuffer();
		if(localUserModel!=null&&targetUserModel!=null){
			//=====本地账号判断
			if(localUserModel.getUserid()==null)localUserModel.setUserid("");
			if(localUserModel.getUsername()==null)localUserModel.setUsername("");
			if(localUserModel.getDepartmentid()==null)localUserModel.setDepartmentid(new Long(0));
			if(localUserModel.getDepartmentname()==null)localUserModel.setDepartmentname("");
			if(localUserModel.getExtend1()==null)localUserModel.setExtend1("");
			if(localUserModel.getExtend2()==null)localUserModel.setExtend2("");
			//同步账号判断
			if(targetUserModel.getUserid()==null)targetUserModel.setUserid("");
			if(targetUserModel.getUsername()==null)targetUserModel.setUsername("");
			if(targetUserModel.getDepartmentid()==null)targetUserModel.setDepartmentid(new Long(0));
			if(targetUserModel.getDepartmentname()==null)targetUserModel.setDepartmentname("");
			if(targetUserModel.getExtend1()==null)targetUserModel.setExtend1("");
			if(targetUserModel.getExtend2()==null)targetUserModel.setExtend2("");
			
			Long localDepartmentId = new Long(0);
			OrgDepartment dept = orgDepartmentService.getBoData(localUserModel.getDepartmentid());
			if(dept!=null){
				localDepartmentId = Long.parseLong(dept.getDepartmentno());
			}
			
			if(!localDepartmentId.equals(targetUserModel.getDepartmentid())){
				log.append("部门编号：").append(localDepartmentId).append("->").append(targetUserModel.getDepartmentid());
			}
			if(!localUserModel.getUserno().equals(targetUserModel.getUserno())){
				log.append("用户编号：").append(localUserModel.getUserno()).append("->").append(targetUserModel.getUserno());
			}
			if(!localUserModel.getUsername().equals(targetUserModel.getUsername())){
				log.append("账号中文姓名：").append(localUserModel.getUsername()).append("->").append(targetUserModel.getUsername());
				
			}
			if(!localUserModel.getOrgroleid().equals(targetUserModel.getOrgroleid())){
				log.append("角色ID：").append(localUserModel.getOrgroleid()).append("->").append(targetUserModel.getOrgroleid());
			}
			if(!localUserModel.getExtend1().equals(targetUserModel.getExtend1())){
				log.append("工作组编号：").append(localUserModel.getExtend1()).append("->").append(targetUserModel.getExtend1());
			}
			if(!localUserModel.getExtend2().equals(targetUserModel.getExtend2())){
				log.append("工作组名称：").append(localUserModel.getExtend2()).append("->").append(targetUserModel.getExtend2());
			}
			if(!localUserModel.getUserstate().equals(targetUserModel.getUserstate())){
				log.append("用户状态：").append(localUserModel.getUserstate()).append("->").append(targetUserModel.getUserstate());
			}
			if(!localUserModel.getUsertype().equals(targetUserModel.getUsertype())){
				log.append("用户类型：").append(localUserModel.getUsertype()).append("->").append(targetUserModel.getUsertype());
			}
		}
		return log.toString();
	}
	
	private String checkOrgRole(OrgRole localRole,OrgRole targetRole){
		StringBuffer log = new StringBuffer();
		if(localRole!=null&&targetRole!=null){
			//=====本地角色判断
			if(localRole.getRolename()==null)localRole.setRolename("");
			if(localRole.getRoletype()==null)localRole.setRoletype("");
			if(localRole.getRoledesc()==null)localRole.setRoledesc("");
			if(localRole.getMemo()==null)localRole.setMemo("");
			//=====同步角色判断
			if(targetRole.getRolename()==null)targetRole.setRolename("");
			if(targetRole.getRoletype()==null)targetRole.setRoletype("");
			if(targetRole.getRoledesc()==null)targetRole.setRoledesc("");
			if(targetRole.getMemo()==null)targetRole.setMemo("");
			
			
			if(!localRole.getRolename().equals(targetRole.getRolename())){
				log.append("角色名称:").append(localRole.getRolename()+"->"+targetRole.getRolename());
			}
			if(!localRole.getRoletype().equals(targetRole.getRoletype())){
				log.append("角色类型:").append(localRole.getRolename()+"->"+targetRole.getRolename());
			}
			if(!localRole.getRoledesc().equals(targetRole.getRoledesc())){
				log.append("角色描述:").append(localRole.getRoledesc()+"->"+targetRole.getRoledesc());
			}
			if(!localRole.getMemo().equals(targetRole.getMemo())){
				log.append("扩展Memo:").append(localRole.getMemo()+"->"+targetRole.getMemo());
			}
		}
		return log.toString();
	}
	
	/**
	 * 检查当前部门与同步部门信息是否一致
	 * @param localDept
	 * @param targetDept
	 * @return
	 */
	private String checkDept(OrgDepartment localDept,OrgDepartment targetDept){
		StringBuffer log = new StringBuffer();
		if(localDept!=null&&targetDept!=null){
			if(localDept.getDepartmentname()==null)localDept.setDepartmentname("");
			if(localDept.getDepartmentstate()==null)localDept.setDepartmentstate("");
			if(localDept.getDepartmentdesc()==null)localDept.setDepartmentdesc("");
			if(localDept.getExtend1()==null)localDept.setExtend1("");
			if(localDept.getExtend2()==null)localDept.setExtend2("");
			
			if(targetDept.getDepartmentname()==null)targetDept.setDepartmentname("");
			if(targetDept.getDepartmentstate()==null)targetDept.setDepartmentstate("");
			if(targetDept.getDepartmentdesc()==null)targetDept.setDepartmentdesc("");
			if(targetDept.getExtend1()==null)targetDept.setExtend1("");
			if(targetDept.getExtend2()==null)targetDept.setExtend2("");
			
			if(!localDept.getDepartmentname().equals(targetDept.getDepartmentname())){
				log.append(localDept.getDepartmentname()+"->"+targetDept.getDepartmentname());
			}
			
			if(!localDept.getDepartmentstate().equals(targetDept.getDepartmentstate())){
				log.append(localDept.getDepartmentstate()+"->"+targetDept.getDepartmentstate());
			}
			
			if(!localDept.getDepartmentdesc().equals(targetDept.getDepartmentdesc())){
				log.append(localDept.getDepartmentdesc()+"->"+targetDept.getDepartmentdesc());
			}
			if(!localDept.getExtend1().equals(targetDept.getExtend1())){
				log.append(localDept.getExtend1()+"->"+targetDept.getExtend1());
			}
			if(!localDept.getExtend2().equals(targetDept.getExtend2())){
				log.append(localDept.getExtend2()+"->"+targetDept.getExtend2());
			}
		}
		return log.toString();
	}
	
	
	/**
	 * 初始化部门
	 * @param model
	 */
	public void initDept(SysExdbsrcCenter model){
		
		
	}
	/**
	 * 初始化角色
	 * @param model
	 */
	public void initRole(SysExdbsrcCenter model){
		
		
	}
	
	/**
	 * 初始化用户
	 * @param model
	 */
	public void initUser(SysExdbsrcCenter model){
		
		
	}
	
	/**
	 * 初始化角色及用户对应关系
	 * @param model
	 */
	public void intRoleToUser(SysExdbsrcCenter model){
		
	}
	
	
	
	
	public void setOrgSynchronizeDAO(OrgSynchronizeDAO orgSynchronizeDAO) {
		this.orgSynchronizeDAO = orgSynchronizeDAO;
	}



	public OrgSynchronizeDAO getOrgSynchronizeDAO() {
		return orgSynchronizeDAO;
	}

	public void setOrgCompanyService(OrgCompanyService orgCompanyService) {
		this.orgCompanyService = orgCompanyService;
	}



	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}



	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}



	public void setOrgDepartmentService(OrgDepartmentService orgDepartmentService) {
		this.orgDepartmentService = orgDepartmentService;
	}

	public void setOrgUserMapService(OrgUserMapService orgUserMapService) {
		this.orgUserMapService = orgUserMapService;
	}
	

}
