package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;

import com.ibpmsoft.project.zqb.common.ZQBRoleConstants;
import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.OrganizationAPI;

public class ZQB_Customer_BaseAddEvent  extends DemTriggerEvent{
	public ZQB_Customer_BaseAddEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){ 
		HashMap formData = this.getFormData();
		HashMap map = null;
		if(formData!=null){
			 map = ParameterMapUtil.getParameterMap(formData);
		}
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String smsContent = "";
		String sysMsgContent = "";
		Long dataId = Long.parseLong(hash.get("ID").toString());
		String customername = hash.get("CUSTOMERNAME").toString();
		UserContext uc = this.getUserContext();
		List<HashMap> list=DemAPI.getInstance().getFromSubData(this.getInstanceId(), "SUBFORM_GQJGMXB");
		if(uc.get_userModel().getOrgroleid()!=5){
			if(!"0".equals(map.get("instanceId").toString())){
				smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.CUSTOMER_UPDATE_KEY, map); 
				sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.CUSTOMER_UPDATE_KEY, map); 
			}else{
				smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.CUSTOMER_ADD_KEY, map); 
				sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.CUSTOMER_ADD_KEY, map); 
			}
			String userid = ZQBNoticeUtil.getInstance().getNoticeUserId(ZQBRoleConstants.ISPURVIEW_ROLE_ID_CHANG);
			UserContext target = UserContextUtil.getInstance().getUserContext(userid);
			if(target!=null){
				if(!smsContent.equals("")){
					String mobile = target.get_userModel().getMobile();
					if(mobile!=null&&!mobile.equals("")){
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				if(!sysMsgContent.equals("")){
						MessageAPI.getInstance().sendSysMsg(userid, "客户基本信息维护提醒", sysMsgContent);
				}
			}
		}
		if(!"0".equals(map.get("instanceId").toString())){
			LogUtil.getInstance().addLog(dataId, "客户信息维护", "修改客户："+customername);
		}else{
			LogUtil.getInstance().addLog(dataId, "客户信息维护", "添加客户："+customername);
		}
		if(map.get("YGP").equals("已挂牌")){
			//如果是已挂牌，直接往部门表中插入一条记录
			OrgDepartment model=new OrgDepartment();
			model.setDepartmentname(map.get("CUSTOMERNAME").toString());
			model.setDepartmentno(map.get("CUSTOMERNO").toString());
			model.setCompanyid("2");
			model.setParentdepartmentid(new Long(51));
			model.setDepartmentstate("0");
			String id=DBUtil.getString("select * from ORGDEPARTMENT where DEPARTMENTNO='"+model.getDepartmentno()+"'", "ID");
			if(id!=null&&!id.equals("")){
				model.setId(Long.parseLong(id));
				OrganizationAPI.getInstance().updateDepartment(model);
				DBUtil.executeUpdate("UPDATE ORGUSER SET DEPARTMENTNAME='"+map.get("CUSTOMERNAME").toString()+"',EXTEND2='"+map.get("CUSTOMERNAME").toString()+"' WHERE DEPARTMENTID="+id+" and EXTEND1='"+map.get("CUSTOMERNO").toString()+"'");
			}else{
			  OrganizationAPI.getInstance().addDepartment(model);
			}
			//如果是已挂牌，直接往持续督导分派表中插入一条
			String demUUID = "84ff70949eac4051806dc02cf4837bd9";// 持续督导分派
			//判断是否存在，如果存在则不进行添加,进行修改
			String cxduid=DBUtil.getString("select * from BD_MDM_KHQXGLB where KHBH='"+hash.get("CUSTOMERNO")+"'", "ID");
			if(cxduid==null||cxduid.equals("")){
				HashMap hashmap = new HashMap();
				Long instanceid = DemAPI.getInstance()
						.newInstance(
								demUUID,
								UserContextUtil.getInstance()
										.getCurrentUserContext()._userModel
										.getUserid());
				hashmap.put("instanceId", instanceid);
				hashmap.put("formid", 114);
				hashmap.put("metadataid", 130);
				hashmap.put("modelType", "DEM");
				hashmap.put("KHBH", hash.get("CUSTOMERNO"));
				hashmap.put("KHMC", hash.get("CUSTOMERNAME"));
				hashmap.put("TXDF", "否");
				hashmap.put("SFFP", "否");
				DemAPI.getInstance().saveFormData(demUUID, instanceid, hashmap,
						false);
				
			}else{
				HashMap conditionMap =new HashMap();
				conditionMap.put("KHBH",  hash.get("CUSTOMERNO"));
				List<HashMap> cxddList=DemAPI.getInstance().getList(demUUID, conditionMap, null);
				if(cxddList.size()>0&&!cxddList.get(0).get("KHMC").equals("")&&!cxddList.get(0).get("KHMC").equals( hash.get("CUSTOMERNAME"))){
					HashMap cxddmap=cxddList.get(0);
					cxddmap.put("KHMC", hash.get("CUSTOMERNAME"));
					Long instance=Long.parseLong(cxddmap.get("INSTANCEID").toString());
					DemAPI.getInstance().updateFormData(demUUID, instance, cxddmap, Long.parseLong(cxduid), false);			

				}
			}
			
		}
		return true;
	}
	

}
