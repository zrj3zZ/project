package com.ibpmsoft.project.zqb.event;

import java.util.HashMap;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

/**归档
 * @author duqq
 *
 */
public class zqbCXDDAfterSaveEvent extends ProcessTriggerEvent {
	public zqbCXDDAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	private static final String CXDD_UUID = "84ff70949eac4051806dc02cf4837bd9";

	public boolean execute() {
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		// 获取流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(
				instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if (dataMap != null) {
			//归档时回写督导表中的字段
			// 1.先查询 2.再更新
			String wlins=dataMap.get("WLINS")!=null?dataMap.get("WLINS").toString():"";
			 HashMap map=DemAPI.getInstance().getFromData(Long.parseLong(wlins), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if (map != null ) {
				String khfzr=dataMap.get("KHFZR")!=null?dataMap.get("KHFZR").toString():"";
				map.put("KHFZR", khfzr);
				String id=map.get("ID")!=null?map.get("ID").toString():"";
				flag=DemAPI.getInstance().updateFormData(CXDD_UUID, Long.parseLong(wlins), map, Long.parseLong(id), true);//更新持续督导专员
				//更新之后，进行短信发送
				//发送消息
				if(flag){
					String smsContent = "";
					String sysMsgContent = "";
					if(map.get("TXDF").equals("是")){
						smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.DDFP_ADD_KEY, map); 
						sysMsgContent = ZQBNoticeUtil.getInstance().getNoticeSysMsgContent(ZQB_Notice_Constants.DDFP_ADD_KEY, map); 
						String customerno=map.get("KHBH")!=null?map.get("KHBH").toString():"";
						String userAddress=ZQBNoticeUtil.getInstance().getDuDaoCustomer(customerno);
						sendMsg(userAddress,smsContent,sysMsgContent);
					}
				}
			}
		}

		return flag;
	}
	/**发送短信和系统消息
	 * @param userAddress
	 * @param smsContent
	 * @param sysMsgContent
	 */
	public void sendMsg(String userAddress,String smsContent,String sysMsgContent){
		String userid = UserContextUtil.getInstance().getUserId(userAddress);
		UserContext target = UserContextUtil.getInstance().getUserContext(userid);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if(target!=null){
				//“场外市场部经理”会分配客户给“专职持续督导”,如果也填写了 “持续督导专员”并且选择了“提醒对方”，那么应该给这2个人（注意如果同一人，只发送一条）对应的短信、系统消息通知
				if(!smsContent.equals("")){
					String mobile = target.get_userModel().getMobile();
					if(mobile!=null&&!mobile.equals("")){
						mobile = target.get_userModel().getMobile()+"["+target.get_userModel().getUsername()+"]";
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
				if(!sysMsgContent.equals("")){ 
						MessageAPI.getInstance().sendSysMsg(userid, "持续督导分派提醒", sysMsgContent);
				}
		}
	}
}

