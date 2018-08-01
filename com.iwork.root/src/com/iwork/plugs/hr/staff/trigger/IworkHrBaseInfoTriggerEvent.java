package com.iwork.plugs.hr.staff.trigger;

import java.util.HashMap;
import java.util.List;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.plugs.hr.org.constant.IWorkHrBaseInfoConstants;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.sdk.DemAPI;
/**
 * 
 * 员工基础信息填写
 * @author wangjh
 *
 */
public class IworkHrBaseInfoTriggerEvent extends DemTriggerEvent {
	
	private UserContext _me;
	private SpaceManageDao spaceManageDao;
	private HashMap params;
	public IworkHrBaseInfoTriggerEvent(UserContext me,HashMap hash) {
		super(me,hash);
		_me = me;
		params = hash;
	}
	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		boolean flag = true;
		String tableName = this.getTableName();
//		if(tableName!=null&&tableName.equals("BD_BASE_RYXX_LXFS")){
			flag = false;
			//获取主表 
			HashMap mainFormData = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
			//获取主表人员编号字段
			String perno = (String)mainFormData.get("PERNO");
			//获取主表人员名字 
			String pername = (String)mainFormData.get("PERNAME");
			//获取主表人员帐号
			String itcode = (String)mainFormData.get("ITCODE");
			//获得子表list,联系方式
			List<HashMap> list = DemAPI.getInstance().getFromSubData(this.getInstanceId(), IWorkHrBaseInfoConstants.HR_BASE_SUBFORM_KEY_LXFS);
			//若非第一次保存主表
			Long demId ;
			if(list.size()!=0 && list != null){ 
				boolean zflag ;
				for(HashMap map:list){
					map.put("PERNO", perno);
					//更新联系方式子表
					//获得模型ID
					demId = DemAPI.getInstance().getDemModel(IWorkHrBaseInfoConstants.HR_BASE_PERSON_UUID).getId();
					zflag = DemAPI.getInstance().updateSubFormData(demId, IWorkHrBaseInfoConstants.HR_BASE_SUBFORM_KEY_LXFS,this.getInstanceId(), map, false);
					flag = zflag;
				}
				
			}else{
				flag = true;
			}
//		}
		
		
		return flag;
	
	}

}
