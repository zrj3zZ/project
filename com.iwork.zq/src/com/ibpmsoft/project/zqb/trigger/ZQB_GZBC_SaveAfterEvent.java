package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

/**
 * 关联方管理信息添加
 * @author wuyao
 *
 */
public class ZQB_GZBC_SaveAfterEvent  extends DemTriggerEvent {
	public ZQB_GZBC_SaveAfterEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(!hash.get("HFNR").toString().equals("")||!hash.get("HFZL").toString().equals("")){
			String cid = hash.get("CID").toString();
			String did = hash.get("DID").toString();
			String msg="";
			int num=0;
			if(did != null&&!did.equals("")){
				num = DBUtil.executeUpdate("UPDATE BD_XP_GZSCSXFKRB SET FKZT='1' WHERE ID="+did);
			}
			if(num==1){
				msg="更新成功！";
			}else{
				msg="更新失败！";
			}
			//判断当前工作审查是否所有人都已反馈,查询未反馈数量,若等于0，则都已反馈
			int allFK = DBUtil.getInt("SELECT COUNT(*) RNUM FROM (SELECT INSTANCEID,B.ID,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查')) A LEFT JOIN BD_XP_GZSC B ON A.DATAID = B.ID) C INNER JOIN (SELECT A.INSTANCEID,B.ID,B.FKZT FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='工作备查事项反馈人表表单')) A LEFT JOIN BD_XP_GZSCSXFKRB B ON A.DATAID = B.ID) D ON C.INSTANCEID = D.INSTANCEID AND D.ID IS NOT NULL AND C.ID IS NOT NULL AND D.FKZT = 0 WHERE C.ID="+cid, "RNUM");
			if(allFK==0){
				DBUtil.executeUpdate("UPDATE BD_XP_GZSC SET FKZT='1' WHERE ID="+cid);
			}
		}
		return true;
	}
}