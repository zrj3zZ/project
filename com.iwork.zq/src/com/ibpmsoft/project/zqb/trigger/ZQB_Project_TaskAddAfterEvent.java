package com.ibpmsoft.project.zqb.trigger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;

public class ZQB_Project_TaskAddAfterEvent  extends DemTriggerEvent {
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static final String PROJECT_TASK_UUID = "b25ca8ed0a5a484296f2977b50db8396";
	
	public ZQB_Project_TaskAddAfterEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	/**
	 * 执行触发器
	 */
	public boolean execute(){
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		Long instanceId = Long.valueOf(String.valueOf(map.get("instanceId"))).longValue();
		if (!map.get("ATTACH").equals("")) {
			String[] sxzlmb = map.get("ATTACH").toString()
					.split(";");
			String userid = this.getUserContext().get_userModel()
					.getUserid();
			for (int i = 0; i < sxzlmb.length; i++) {
				String aa="JDZL"+i;
				if(!"".equals(map.get(aa).toString())){
					String[] zllist = sxzlmb[i].split(":");
					if(zllist.length>=1){
						String[] zll = map.get(aa).toString().split(",");
						String mb = zllist[0];
						for(int j=0;j<zll.length;j++){
							String zl=zll[j];
							HashMap conditionMap= new HashMap();
							conditionMap.put("JDZL", zl);
							List<HashMap> list = DemAPI.getInstance().getList("db9b82e9d24a491aba9239cc67284b75", conditionMap, null);
							if(list.isEmpty()){
								HashMap map1 = new HashMap();
								map1.put("JDZL", zl);
								map1.put("PROJECTNO", map.get("PROJECTNO")
										.toString());
								map1.put("SXZL", mb);
								map1.put("JDBH", map.get("GROUPID").toString());
								Long ins = DemAPI.getInstance().newInstance(
										"db9b82e9d24a491aba9239cc67284b75", userid);
								DemAPI.getInstance().saveFormData(
										"db9b82e9d24a491aba9239cc67284b75", ins,
										map1, true);// 物理表单
							}
						}
						}
				}
			}
		}
		HashMap hash=new HashMap();
		String projectno = map.get("PROJECTNO").toString();
		hash.put("PROJECTNO", projectno);
		List<HashMap> list = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", hash, null);
		if(list.size()==1){
			Map params = new HashMap();
			params.put(1,projectno);
			String jdmc = DBUTilNew.getDataStr("jdmc","select jdmc from BD_ZQB_KM_INFO where id=(select max(groupid) id from BD_PM_TASK where projectno= ? )", params);
			for (HashMap hashMap : list) {
				hashMap.put("XMJD", jdmc);
				DemAPI.getInstance().updateFormData("33833384d109463285a6a348813539f1",
						Long.parseLong(hashMap.get("INSTANCEID").toString()),
						hashMap, Long.parseLong(hashMap.get("ID").toString()),
						false);
				String actStepId=hashMap.get("STEPID").toString();
				String actDefId=hashMap.get("LCBH").toString();
				String userid=uc._userModel.getUserid();
				List<Task> tasklist = ProcessAPI.getInstance()
						.getUserProcessTaskList(actDefId,actStepId,userid);
				List<Task> userProcessTaskList = ProcessAPI.getInstance().getUserProcessTaskList(actDefId,actStepId,userid);
				for (Task task : tasklist) {
					if(task.getProcessInstanceId().equals(hashMap.get("LCBS").toString())){
						HashMap data = ProcessAPI.getInstance().getFromData(Long.parseLong(task.getProcessInstanceId()),EngineConstants.SYS_INSTANCE_TYPE_PROCESS);//流程表中的数据
						data.put("XMJD", jdmc);
						boolean flag=ProcessAPI.getInstance().updateFormData(actDefId, Long.parseLong(task.getProcessInstanceId()), data, Long.parseLong(data.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
						if(flag){
							return flag;
						}
					}
				}
				String customerno = hashMap.get("CUSTOMERNO").toString();
				String readValue = ConfigUtil.readValue(CN_FILENAME, "isDwPj");
				if(readValue!=null&&readValue.equals("1")){
					BigDecimal xmjs = getXmjs(customerno, projectno);
					if(xmjs.compareTo(new BigDecimal(0))==1){
						HashMap fromData = DemAPI.getInstance().getFromData(instanceId);
						fromData.put("SSJE", xmjs.doubleValue());
						Long dataid = Long.parseLong(fromData.get("ID").toString());
						DemAPI.getInstance().updateFormData(PROJECT_TASK_UUID, instanceId, fromData, dataid, false);
					}
				}
			}
		}
		return true;
	}
	
	public BigDecimal getXmjs(String customerno,String projectno){
		BigDecimal bd=new BigDecimal(0L);
		BigDecimal xmjdTotal=new BigDecimal(DBUtil.getDouble("SELECT SUM(SSJE) TOTAL FROM BD_PM_TASK WHERE PROJECTNO='"+projectno+"'", "TOTAL"));
		BigDecimal xmjsTotal=new BigDecimal(DBUtil.getDouble("SELECT SUM(RZJE) TOTAL FROM BD_ZQB_XMJSXXGLB WHERE XMLX='推荐挂牌' AND CUSTOMERNO='"+customerno+"'", "TOTAL"));
		int compareTo = xmjsTotal.compareTo(xmjdTotal);
		if(compareTo==1){
			bd = xmjsTotal.subtract(xmjdTotal);
		}
		return bd;
	}
}
