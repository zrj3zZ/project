package com.iwork.plugs.vote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.app.weixin.process.action.qy.util.TestSendMes;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;

public class VoteHalfMonEvent implements IWorkScheduleInterface {
	
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static Logger logger = Logger.getLogger(VoteHalfMonEvent.class);
	public boolean executeAfter() throws ScheduleException {
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		AddGGToDB();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		return true;
	}
	
	public void AddGGToDB() {
		/*各挂牌公司、主办券商： 根据《关于开展挂牌公司股权质押、冻结信息披露情况核查工作的通知》，挂牌公司董秘或信息披露负责人应至少每半个月通过中国结算Ukey对挂牌公司股权质押、冻结信息进行一次查询，发现达到信息披露标准的，应当及时编制并披露相关公告，做好挂牌公司信息披露工作。
		每半月查询股东质押
		是否查询(选择是否)
		情况说明框
		附件上传按钮*/
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String userid = "NEEQMANAGER";
		
		String username = "超级管理员";
		
		String tzlx = "股东质押查询";
		
		String sftz = "否";
		
		String wjfile = "";
		
		String dctm = "";
		
		String xgzl = "";
		
		String tzbt = "每半月查询股东质押("+sdf.format(new Date())+")";
		
		String tznr = "各挂牌公司、主办券商： 根据《关于开展挂牌公司股权质押、冻结信息披露情况核查工作的通知》，挂牌公司董秘或信息披露负责人应至少每半个月通过中国结算Ukey对挂牌公司股权质押、冻结信息进行一次查询，发现达到信息披露标准的，应当及时编制并披露相关公告，做好挂牌公司信息披露工作。";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 15);
		String zchfsj = sd.format(cal.getTime());
		
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String demUUID = config.get("tzggbuuid");
		
		Long instanceId = DemAPI.getInstance().newInstance(demUUID, userid);
		
		HashMap hashmap = new HashMap();
		hashmap.put("instanceId", instanceId);
		hashmap.put("formid", config.get("tzggbformid"));
		hashmap.put("TZBT", tzbt);
		hashmap.put("ZCHFSJ", zchfsj);
		hashmap.put("TZNR", tznr);
		hashmap.put("XGZL", xgzl);
		hashmap.put("HFR", "");
		hashmap.put("SFTZ", sftz);
		hashmap.put("FSR", username);
		String format = sd.format(new Date());
		hashmap.put("FSSJ", format);
		hashmap.put("TZLX", tzlx);
		hashmap.put("WJFILE", wjfile);
		hashmap.put("DCTM", dctm);
		hashmap.put("FSZT", "未完成");
		hashmap.put("JSZT", "未回复");
		hashmap.put("FSRID", userid);
		//新增通知公告
		boolean saveFormData = DemAPI.getInstance().saveFormData(demUUID,instanceId, hashmap, false);
		String smsContent="您好，"+SystemConfig._iworkServerConf.getTitle()+"，发送了"+tzbt+"，请尽快填写，谢谢！";
		if(saveFormData){
			HashMap data = DemAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			//DemAPI.getInstance().removeFormData(instanceId);
			
			String ggid = data.get("ID").toString();
			List<HashMap> list = new ArrayList();
			Connection conn = DBUtil.open();;
			PreparedStatement stmt = null;
			ResultSet rset = null;
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.USERID AS USERID,A.EXTEND1 AS CUSTOMERNO,A.USERNAME AS USERNAME,A.DEPARTMENTNAME AS DEPARTMENTNAME,A.ORGROLEID AS ORGROLEID,A.MOBILE AS MOBILE,B.ZQDM AS ZQDM,B.TEL AS TEL FROM ORGUSER A LEFT JOIN BD_ZQB_KH_BASE B ON A.EXTEND1=B.CUSTOMERNO WHERE A.ORGROLEID = 3 AND   b.ygp='已挂牌' and  b.status='有效'   and (b.cxddbg<>'转出' or b.cxddbg is null) ");
			try {
				stmt =conn.prepareStatement(sql.toString());
				rset = stmt.executeQuery();
				while (rset.next()) {
					HashMap map = new HashMap();
					String uname = rset.getString("username");
					String uid = rset.getString("userid");
					String customerno = rset.getString("customerno");
					String departmentname = rset.getString("departmentname");
					String orgroleid = rset.getString("orgroleid");
					String mobile = rset.getString("mobile");
					String zqdm = rset.getString("zqdm");
					String tel = rset.getString("tel");
					map.put("USERID", uid);
					map.put("CUSTOMERNO", customerno);
					map.put("XM", uname);
					map.put("GSMC", departmentname);
					map.put("GSDM", zqdm);
					map.put("SJH", mobile);
					
					map.put("formid", config.get("hfqkbformid"));
					map.put("GGID", ggid);
					map.put("STATUS", "未回复");
					list.add(map);
					if(mobile!=null && !"".equals(mobile)){
						try {
							TestSendMes tms=new TestSendMes();
							tms.sendDcwjMsgList(uid, smsContent);
						} catch (Exception e) {}
						MessageAPI.getInstance().sendSMS(mobile, smsContent);
					}
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rset);
			}
			//新增回复信息
			for (HashMap map : list) {
				Long instanceidTz = DemAPI.getInstance().newInstance(config.get("hfqkbuuid"),userid);
				DemAPI.getInstance().saveFormData(config.get("hfqkbuuid"),instanceidTz, map, false);
			}
		}
	}
}

