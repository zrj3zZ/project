package com.ibpmsoft.project.zqb.trigger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.ibpmsoft.project.zqb.common.ZQB_Notice_Constants;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ZQBNoticeUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import org.apache.log4j.Logger;
public class ZQB_ZKDATE_SaveEvent extends DemTriggerEvent{
	private static Logger logger = Logger.getLogger(ZQB_ZKDATE_SaveEvent.class);
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public ZQB_ZKDATE_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**
	 * 执行触发器
	 */
	public boolean execute(){ 
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		String jlsj=map.get("JLSJ")==null?"":map.get("JLSJ").toString();
		String zkdate=map.get("ZKDATE")==null?"":map.get("ZKDATE").toString();
		boolean flag=false;
		if(!zkdate.equals("")&&jlsj.equals("")){
			flag=true;
		}else if(!jlsj.equals("")&&!zkdate.equals("")&&!jlsj.equals(zkdate)){
			flag=true;
		}
		if(flag){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			final UserContext uct = UserContextUtil.getInstance().getCurrentUserContext();
			HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
			Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
			String zkUUID = config.get("zkuuid");
			final HashMap clone = (HashMap) hash.clone();
			clone.put("ZKUUID", zkUUID);
			//发送消息
			String smsContent = "";
			HashMap hashmap=new HashMap();
			hashmap.put("PROJECTNO", hash.get("PROJECTNO"));
			List<HashMap> list = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", hashmap, null);
			HashMap map2=new HashMap();
			map2.put("PROJECTNAME", list.get(0).get("PROJECTNAME"));
			map2.put("ZKDATE", hash.get("ZKDATE").toString());
			smsContent = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.ZKFK_ADD_KEY, map2);
			String user=list.get(0).get("MANAGER").toString().substring(0, 
					list.get(0).get("MANAGER").toString().indexOf("["));
			UserContext target = UserContextUtil.getInstance().getUserContext(
					user);
			if (target != null) {
				if (!smsContent.equals("")) {
					String mobile = target.get_userModel().getMobile();
					if (mobile != null && !mobile.equals("")) {
						MessageAPI.getInstance().sendSMS(uc, mobile, smsContent);
					}
				}
			}
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date d = null;
			try {
				d = df.parse(clone.get("ZKDATE").toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error(e,e);
			}
			long now = System.currentTimeMillis();
			long time = d.getTime()-now+8*60*60*1000;//当前时间离当天0点的毫秒数
			String smsContent1 = "";
			HashMap map3=new HashMap();
			map3.put("PROJECTNAME", list.get(0).get("PROJECTNAME"));
			map3.put("ZKDATE", clone.get("ZKDATE").toString());
			smsContent1 = ZQBNoticeUtil.getInstance().getNoticeSmsContent(ZQB_Notice_Constants.SPTX_ADD_KEY, map3);
			class DXThread extends Thread {
				private UserContext uct;
				private HashMap clone;
				private long time;
				private String smsContent;
				DXThread(HashMap clone, UserContext uct, long time,String smsContent) {
					this.clone = clone; 
					this.uct = uct;
					this.time = time;
					this.smsContent=smsContent;
				}

				public void run() { 
					Timer timer = new Timer(); 
					timer.schedule(new TimerTask() { 
					public void run() {
						HashMap hashmap=new HashMap();
						hashmap.put("PROJECTNO", clone.get("PROJECTNO"));
						String uuid=clone.get("ZKUUID").toString();
						List<HashMap> list = DemAPI.getInstance().getList(uuid, hashmap, null);
						String user=list.get(0).get("ZKR").toString().substring(0, 
								list.get(0).get("ZKR").toString().indexOf("["));
						UserContext target = UserContextUtil.getInstance().getUserContext(
								user);
						if (target != null) {
							if (!smsContent.equals("")) {
								String mobile = target.get_userModel().getMobile();
								if (mobile != null && !mobile.equals("")) {
									MessageAPI.getInstance().sendSMS(uct, mobile, smsContent);
								}
							}
						}
					} 
					}, time);
				}	
			} 
			if(time<0){
				return true;
			}
			new DXThread(clone,uct,time,smsContent1).start();
		}
		return true;
	}
}
