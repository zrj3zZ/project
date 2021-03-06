package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.MessageAPI;

/**
 * @author wuyao
 *
 */
public class ZQB_GZXTYJHF_SaveEvent  extends DemTriggerEvent {
	public ZQB_GZXTYJHF_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**com.ibpmsoft.project.zqb.trigger.ZQB_GZXTYJHF_SaveEvent
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap formData = this.getFormData();
		HashMap<String,Object> map = ParameterMapUtil.getParameterMap(formData);
		if(map.get("DXYJTZR")!=null&&map.get("instanceId").toString().equals("0")){
			String ggname="";
			String zqdm="";
			String customername="";
			if(map.get("GZYJID")!=null){
				String ggid = map.get("GZYJID").toString();
				//获得公告名称
				ggname = DBUtil.getString("SELECT A.NOTICENAME FROM BD_MEET_QTGGZL A LEFT JOIN BD_XP_GZXTYJB B ON A.ID=B.GGID WHERE B.ID="+ggid, "NOTICENAME");
			}
			if(map.get("KHBH")!=null){
				String khbh = map.get("KHBH").toString();
				//获得公司代码
				zqdm = DBUtil.getString("SELECT ZQDM FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"+khbh+"'", "ZQDM");
				//获得公司名称
				customername = DBUtil.getString("SELECT CUSTOMERNAME FROM BD_ZQB_KH_BASE WHERE CUSTOMERNO='"+khbh+"'", "CUSTOMERNAME");
			}
			
			String sms = (zqdm.equals("")?"":zqdm+",")+ggname+",回复了股转意见,请登录系统查看。";
			String mail = (zqdm.equals("")?"":zqdm+",")+customername+","+ggname+",回复了股转意见。"+"</br>回复信息:"+map.get("GZYJHFXX").toString()+"。</br>详情请您登录系统在公告呈报中查看并回复。";
			
			String [] dxyjtzrarray = map.get("DXYJTZR").toString().split(",");
			String currentuserfullname= UserContextUtil.getInstance().getCurrentUserContext().get_userModel().getUsername();
			for (int i = 0; i < dxyjtzrarray.length; i++) {
				UserContext usercon= UserContextUtil.getInstance().getUserContext(dxyjtzrarray[i]);
				if(usercon!=null){
					OrgUser user = usercon.get_userModel();
					String email = user.getEmail();
					String mobile = user.getMobile();
					if(email!=null&&!email.equals("")){
						MessageAPI.getInstance().sendSysMail(currentuserfullname, email, "公告审批提醒",mail);
					}
					if(mobile!=null&&!mobile.equals("")){
						MessageAPI.getInstance().sendSMS(usercon, mobile,sms);
					}
				}
			}
		}
		return true;
	}
}