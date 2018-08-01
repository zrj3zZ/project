package com.iwork.oa.event;

import java.util.Date;
import java.util.HashMap;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
/**
 * 印章申请流程
 * @author YangDayong
 *
 */
public class YZSQTriggerEvent extends ProcessStepTriggerEvent {
	private UserContext _me;
	private SpaceManageDao spaceManageDao;
	private HashMap params;
	public YZSQTriggerEvent(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	} 
	public boolean execute() {
		boolean flag = false;
		if(params!=null){
			try{
				Object obj = params.get(this.PROCESS_PARAMETER_INSTANCEID);
				if(obj!=null){
					Long instanceId = (Long)obj;
					HashMap hash = DemAPI.getInstance().getFromData(instanceId,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					//获取添加预定API
					if(spaceManageDao==null)
						spaceManageDao = (SpaceManageDao)SpringBeanUtil.getBean("spaceManageDao");
					if(spaceManageDao!=null&&hash!=null){
						IworkRmWeb irw = new IworkRmWeb();
						irw.setSpaceid(new Long(6));
						irw.setSpacename("证照管理");
						Object yzbh = hash.get("RESOURCEID");  //印章ID
						Object yzmc = hash.get("RESOURCENAME");  //印章ID
						Object sqksrq = hash.get("SQKSRQ");  //印章ID 
						Object sqjsrq = hash.get("SQJSRQ");  //印章ID
						if(yzbh!=null)
							irw.setResouceid(yzbh.toString());
						if(yzmc!=null)
							irw.setResouce(yzmc.toString());
						if(sqksrq!=null)
							irw.setBegintime((Date)sqksrq);
						if(sqjsrq!=null)
							irw.setEndtime((Date)sqjsrq);
						UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
						irw.setUserid(uc.get_userModel().getUserid());
						irw.setUsername(uc.get_userModel().getUsername());
						irw.setStatus(new Long(1)); 
						spaceManageDao.addWeb(irw);
						flag =true;
					}
				}
				
			}catch(Exception e){}
		}
		return flag;
	}
}
