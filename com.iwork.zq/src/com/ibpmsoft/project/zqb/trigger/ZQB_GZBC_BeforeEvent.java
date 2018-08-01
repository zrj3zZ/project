package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;


/**
 * 
 *
 */
public class ZQB_GZBC_BeforeEvent  extends DemTriggerEvent {
	public ZQB_GZBC_BeforeEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		//HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String instanceId = map.get("instanceId").toString();//0  or 62565
		Object userid = map.get("USERID");//null   not null
		if(instanceId.equals("0") || userid==null){
			return true;
		}else{
			if(!instanceId.equals("0") && userid!=null){
				int num = DBUtil.getInt("SELECT COUNT(C.ID) NUM FROM ("
										+" SELECT INSTANCEID,DATAID,B.* FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GZSC')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID" 
										+" ) C "
										+" LEFT JOIN ("
										+" SELECT A.INSTANCEID,DATAID,B.* FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_GZSCSXFKRB')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID"
										+" ) D ON C.INSTANCEID = D.INSTANCEID "
										
										+" WHERE C.ID IS NOT NULL AND D.ID IS NOT NULL AND C.INSTANCEID="+instanceId+" AND D.USERID='"+userid+"'", "NUM");
				if(num==0){
					return true;
				}else{
					return false;
				}
			}else{
				return true;
				
			}
		}
	}
}