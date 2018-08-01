package com.ibpmsoft.project.zqb.trigger;

import java.util.HashMap;
import java.util.List;


import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.FormDataUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;

public class ZQB_GPFX_Project_QuestionAddEvent extends DemTriggerEvent {
	public ZQB_GPFX_Project_QuestionAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		String XMRWUUID=DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行项目任务'", "UUID");
		HashMap formData = this.getFormData();
		String roleid = UserContextUtil.getInstance().getCurrentUserContext()
				.get_orgRole().getId();
		if (formData != null) {
			Object obj = formData.get("SFTZ");
			HashMap conditionMap = new HashMap();
			String projectno = FormDataUtil.getInstance().getFormData(
					formData.get("XMBH"));
			String taskno = FormDataUtil.getInstance().getFormData(
					formData.get("TASKNO"));
			conditionMap.put("PROJECTNO", projectno);
			conditionMap.put("ID", taskno);
			List<HashMap> list = DemAPI.getInstance().getList(XMRWUUID,
					conditionMap, null);
			for (int i = 0; i < list.size(); i++) {
				if (!list.get(i).get("ID").toString().equals(taskno)) {
					list.remove(i);
					i--;
				}
			}
			if (obj != null) {
				String sftz = FormDataUtil.getInstance().getFormData(obj);
				if (sftz.equals("是")) {
					StringBuffer content = new StringBuffer();
					Object xmmc = formData.get("XMMC");
					Object xmbh = formData.get("XMBH");
					Object question = formData.get("QUESTION");
					String sql="select jdmc from BD_ZQB_TYXM_INFO where id="+ FormDataUtil.getInstance().getFormData(formData.get("TASKNO"));
					String taskname=DBUtil.getString(sql, "JDMC")==null?"":
						DBUtil.getString(sql, "JDMC");
					String xmmc_txt = FormDataUtil.getInstance()
							.getFormData(xmmc).trim();
					String task_name = FormDataUtil.getInstance().getFormData(
							taskname);
					String xmbh_txt = FormDataUtil.getInstance().getFormData(
							xmbh);
					String question_txt = FormDataUtil.getInstance()
							.getFormData(question);
					String title = "【" + xmmc_txt + "】股票发行项目问题反馈";
					content.append(question_txt);
					HashMap hash = new HashMap();
					hash.put("TASKNAME", task_name);
					hash.put("XMMC", xmmc_txt);
					hash.put("QUESTION", question_txt);
					hash.put("USERNAME", UserContextUtil.getInstance()
							.getCurrentUserContext()._userModel.getUsername());

					// 判断发送短信
					String smsContent = "";
					String sysMsgContent = "";
					smsContent = ZQBNoticeUtil
							.getInstance()
							.getNoticeSmsContent(
									ZQB_Notice_Constants.PROJECT_QUESTION_ADD_KEY,
									hash);
					sysMsgContent = ZQBNoticeUtil
							.getInstance()
							.getNoticeSysMsgContent(
									ZQB_Notice_Constants.PROJECT_QUESTION_ADD_KEY,
									hash);
					String userfullname = FormDataUtil.getInstance().getFormData(formData.get("ANSWERMAN"));// 获取解答人
					String userid=userfullname!=null&&userfullname.contains("[")?userfullname.substring(
							0,
							userfullname
									.indexOf("[")):"";
					UserContext uc = this.getUserContext();
					UserContext target = UserContextUtil.getInstance()
							.getUserContext(userid);
					List<HashMap> list2 = DemAPI.getInstance().getList(XMRWUUID, conditionMap,
							null);
					if (target != null) {
						if (!smsContent.equals("")) {
							String mobile = target.get_userModel().getMobile();
							if (mobile != null && !mobile.equals("")) {
								MessageAPI.getInstance().sendSMS(uc, mobile,
										smsContent);
							}
						}
						if (!sysMsgContent.equals("")) {
							MessageAPI.getInstance().sendSysMsg(userid, title,
									sysMsgContent);
						}
					}
				}
			}
		}
		return true;
	}
}
