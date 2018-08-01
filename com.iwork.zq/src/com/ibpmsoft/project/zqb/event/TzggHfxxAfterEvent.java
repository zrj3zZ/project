package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.mq.util.MessageQueueUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.sdk.DemAPI;

public class TzggHfxxAfterEvent extends DemTriggerEvent {
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件	
	public TzggHfxxAfterEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}
	
	/**
	 * 保存前执行触发器
	 */
	public boolean execute() { 
		boolean saveFormData=true;
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("hfxxbuuid");
		HashMap formData = this.getFormData();
		List<HashMap> date = DemAPI.getInstance().getFromSubData(this.getInstanceId(), "SUBFORM_PXTZRYB");
		if(date!=null&&!date.equals("")){
			List<Object> check = new ArrayList<Object>();
			for(HashMap datemap:date){
				if(check.contains(datemap.get("RYMC").toString())){
					MessageQueueUtil.getInstance().putAlertMsg("当前参与培训人员存在重复信息，请核实！");
					return false;
				}
				check.add(datemap.get("RYMC").toString());
				
				/*int i=0;
				String usename=datemap.get("RYMC").toString();
				for(HashMap map:date){
					if(usename.equals(map.get("RYMC").toString())){
						i+=1;
						if(i==2){
							MessageQueueUtil.getInstance().putAlertMsg("当前参与培训人员存在重复信息，请核实！");
							return false;
						}
					}
				}*/
			}
		}
		return saveFormData;
	}
}