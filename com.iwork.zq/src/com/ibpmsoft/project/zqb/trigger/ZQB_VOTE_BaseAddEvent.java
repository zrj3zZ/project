package com.ibpmsoft.project.zqb.trigger;


import java.util.HashMap;


import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;

public class ZQB_VOTE_BaseAddEvent extends DemTriggerEvent {
	public ZQB_VOTE_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		// 插入流程表，并将数据更新到流程表中
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		Long instanceId = this.getInstanceId();
		HashMap hashMap = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String tablename = this.getTableName();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String username = UserContextUtil.getInstance().getCurrentUserContext()._userModel
				.getUsername();
		String extend1 = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getExtend1();
		String tzggid = map.get("TZGGID").toString();
		String sql = "update Bd_Xp_Hfqkb set status='已回复',hfsj=sysdate where customerno='"+extend1+"' and ggid=" + tzggid;
		int updateInt = DBUtil.executeUpdate(sql);
		String smsContent = "";
		String getTzggid = map.get("TZGGID").toString();
		String tzbt=DBUtil.getString("SELECT TZBT FROM BD_XP_TZGGB WHERE ID="+getTzggid, "TZBT");
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("CUSTOMERNAME",map.get("CUSTOMERNAME") == null ? "" : map.get("CUSTOMERNAME"));
		dataMap.put("TZBT", tzbt);
		smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.VOTE_ADD_KEY, dataMap);
		String customerno = "";
		if (map.get("CUSTOMERNO") != null) {
			customerno = map.get("CUSTOMERNO").toString();
		}
		String useraddress = ZQBNoticeUtil.getInstance().getDuDaoCustomer(customerno);
		String userid = UserContextUtil.getInstance().getUserId(useraddress);
		String name = "";
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		//更新时间与创建时间的时间差大于300后，连续点击保存才允许一直发送短信，否则只发送第一次保存时的短信
		int status = DBUtil.getInt("SELECT CASE WHEN UPDATEDATE-CREATEDATE = 0 THEN 1 WHEN (UPDATEDATE-CREATEDATE) * 24 * 60 * 60 > =  300 THEN 1 ELSE 0 END STATUS FROM SYS_INSTANCE_DATA WHERE ID="+instanceId, "STATUS");
		if(status==1){
			if (target != null) {
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
					String email = target.get_userModel().getEmail();
					if(email!=null&&!email.equals("") && uc != null){
						MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), email, "挂牌公司重大事项温馨提示函",smsContent,"");
					}
				}
			}
		}
		LogUtil.getInstance().addLog(Long.parseLong(hashMap.get("ID").toString()), "信披自查反馈信息维护", "新增信披自查反馈信息：通知公告ID："+tzggid+",客户编号为"+customerno+"的信披自查反馈信息。");
		return true;
	}
}
