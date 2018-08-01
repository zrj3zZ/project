package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.service.ZqbUpdateDataService;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
/**
 * 项目内核流程保存后触发
 * 
 */
public class ShanXiZqbGuaPaiAfterSaveEvent extends ProcessStepTriggerEvent {
	private static Logger logger = Logger.getLogger(ShanXiZqbGuaPaiAfterSaveEvent.class);
	private ZqbUpdateDataService zqbUpdateDataService;
	private OrgUserService orgUserService;
	private static String KHUUID = "a243efd832bf406b9caeaec5df082e28";

	public ShanXiZqbGuaPaiAfterSaveEvent(UserContext uc, HashMap hash) {
		super(uc, hash);
	}

	public boolean execute() {
		boolean updateFormData = false;
		boolean flag = false;
		Long instanceId = this.getInstanceId();
		// 获取挂牌及归档流程表单数据
		HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(dataMap!=null){
			String customerno = dataMap.get("CUSTOMERNO").toString();
			HashMap<String,String> dataMap2 = getDataMap(customerno);
			//股改
			Object xmggzl1 = dataMap.get("XMGGZL");
			String xmggzl2 = dataMap2.get("XMGGZL");
			boolean xmggzlFlag=false;
			if(xmggzl1!=null&&!xmggzl1.toString().equals(xmggzl2)){
				Long lcggInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_LCGG WHERE CUSTOMERNO='"+customerno+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(lcggInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					processData.put("FJ", xmggzl1.toString());
					Long lcggId = Long.parseLong(processData.get("ID").toString());
					processData.put("TJSJ", sdf.format(date));
					updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), lcggInsId, processData, lcggId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			//申报审核
			Object xmnhzl1 = dataMap.get("SBSHZL");
			String xmnhzl2 = dataMap2.get("SBSHZL");
			if(xmnhzl1!=null&&!xmnhzl1.toString().equals(xmnhzl2)){
				Long xmnhInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_SQNH WHERE CUSTOMERNO='"+customerno+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(xmnhInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					processData.put("FJ", xmnhzl1.toString());
					Long xmnhId = Long.parseLong(processData.get("ID").toString());
					processData.put("TJSJ", sdf.format(date));
					updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), xmnhInsId, processData, xmnhId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			//内核反馈及回复
			Object nhfkzl1 = dataMap.get("NHFKZL");
			String nhfkzl2 = dataMap2.get("NHFKZL");
			if(nhfkzl1!=null&&!nhfkzl1.toString().equals(nhfkzl2)){
				Long nhfkInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_NHFK WHERE CUSTOMERNO='"+customerno+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(nhfkInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					processData.put("FJ", nhfkzl1.toString());
					Long nhfkId = Long.parseLong(processData.get("ID").toString());
					processData.put("TJSJ", sdf.format(date));
					updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), nhfkInsId, processData, nhfkId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			//股转反馈及回复
			Object gzfkzl1 = dataMap.get("GZFKZL");
			String gzfkzl2 = dataMap2.get("GZFKZL");
			if(gzfkzl1!=null&&!gzfkzl1.toString().equals(gzfkzl2)){
				Long gzfkInsId = DBUtil.getLong("SELECT LCBS FROM BD_ZQB_GZFKJHF WHERE CUSTOMERNO='"+customerno+"'","LCBS");
				HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(gzfkInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				if(processData!=null&&!processData.isEmpty()){
					processData.put("FJ", gzfkzl1.toString());
					Long gzfkId = Long.parseLong(processData.get("ID").toString());
					processData.put("TJSJ", sdf.format(date));
					updateFormData = ProcessAPI.getInstance().updateFormData(processData.get("LCBH").toString(), gzfkInsId, processData, gzfkId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				}
			}
			//项目立项信息 xlj update 以下内容更新都增加判断，只有挂牌阶段对应信息有值时才更新
			HashMap<String,String> xmlxMap = new HashMap<String,String>();
			String xmclr1 = dataMap.get("XMCLR")==null?"":dataMap.get("XMCLR").toString();
			String xmclr2 = dataMap2.get("ZCLR");
			if(!xmclr1.equals("") && !xmclr1.equals(xmclr2)){
				xmlxMap.put("ZCLR", xmclr1);
			}
			String dqfzr1 = dataMap.get("DQFZR")==null?"":dataMap.get("DQFZR").toString();
			String dqfzr2 = dataMap2.get("OWNER");
			if(!dqfzr1.equals("") && !dqfzr1.equals(dqfzr2)){
				xmlxMap.put("OWNER", dqfzr1);
			}
			String xmfzr1 = dataMap.get("XMFZR")==null?"":dataMap.get("XMFZR").toString();
			String xmfzr2 = dataMap2.get("MANAGER");
			if(!xmfzr1.equals("") && !xmfzr1.equals(xmfzr2)){
				xmlxMap.put("MANAGER", xmfzr1);
			}
			String gpddap = dataMap.get("DDAP")==null?"":dataMap.get("DDAP").toString();
			String lxddap = dataMap2.get("CXDD");
			if(!gpddap.equals("") && !gpddap.equals(lxddap)){
				xmlxMap.put("DDAP", gpddap);
			}
			String xmzk1 = dataMap.get("XMZK")==null?"":dataMap.get("XMZK").toString();
			String xmzk2 = dataMap2.get("XMZH");
			if(!xmzk1.equals("") && !xmzk1.equals(xmzk2)){
				xmlxMap.put("XMZH", xmzk1);
			}
			String xmhy1 = dataMap.get("XMHY")==null?"":dataMap.get("XMHY").toString();
			String xmhy2 = dataMap2.get("XMXY");
			if(!xmhy1.equals("") && !xmhy1.equals(xmhy2)){
				xmlxMap.put("XMXY", xmhy1);
			}
			String xmls1 = dataMap.get("XMLS")==null?"":dataMap.get("XMLS").toString();
			String xmls2 = dataMap2.get("XMLS");
			if(!xmls1.equals("") && !xmls1.equals(xmls2)){
				xmlxMap.put("XMLS", xmls1);
			}
			if(!xmlxMap.isEmpty()){
				String xmlxuuid = ConfigUtil.readValue("/common.properties", "xmlxuuid");
				HashMap<String,String> conditionMap = new HashMap<String,String>();
				conditionMap.put("CUSTOMERNO", customerno);
				List<HashMap> xmlxList = DemAPI.getInstance().getList(xmlxuuid, conditionMap, null);
				if(xmlxList!=null&&xmlxList.size()>=1){
					HashMap lxMap = xmlxList.get(0);
					for (String key : xmlxMap.keySet()) {
						lxMap.put(key, xmlxMap.get(key));
			        }
					Long lxInsId = Long.parseLong(lxMap.get("INSTANCEID").toString());
					Long lxId = Long.parseLong(lxMap.get("ID").toString());
					DemAPI.getInstance().updateFormData(xmlxuuid, lxInsId, lxMap, lxId, false);
				}
			}
			//客户信息
			HashMap<String,String> khMap = new HashMap<String,String>();
			String gsmc1 = dataMap.get("GSMC")==null?"":dataMap.get("GSMC").toString();
			String gsmc2 = dataMap2.get("CUSTOMERNAME");
			if(!gsmc1.equals("") && !gsmc1.equals(gsmc2)){
				khMap.put("CUSTOMERNAME", gsmc1);
			}
			String zqdm1 = dataMap.get("ZQDM")==null?"":dataMap.get("ZQDM").toString();
			String zqdm2 = dataMap2.get("ZQDM");
			if(!zqdm1.equals("") && !zqdm1.equals(zqdm2)){
				khMap.put("ZQDM", zqdm1);
			}
			String zqjc1 = dataMap.get("ZQJC")==null?"":dataMap.get("ZQJC").toString();
			String zqjc2 = dataMap2.get("ZQJC");
			if(!zqjc1.equals("") && !zqjc1.equals(zqjc2)){
				khMap.put("ZQJC", zqjc1);
			}
			String khlxr1 = dataMap.get("KHLXR")==null?"":dataMap.get("KHLXR").toString();
			String khlxr2 = dataMap2.get("USERNAME");
			if(!khlxr1.equals("") && !khlxr1.equals(khlxr2)){
				khMap.put("USERNAME", khlxr1);
			}
			String khlxdh1 = dataMap.get("KHLXDH")==null?"":dataMap.get("KHLXDH").toString();
			String khlxdh2 = dataMap2.get("KHTEL");
			if(!khlxdh1.equals("") && !khlxdh1.equals(khlxdh2)){
				khMap.put("TEL", khlxdh1);
			}
			String khemail1 = dataMap.get("EMAIL")==null?"":dataMap.get("EMAIL").toString();
			String khemail2 = dataMap2.get("KHEMAIL");
			if(!khemail1.equals("") && !khemail1.equals(khemail2)){
				khMap.put("EMAIL", khemail1);
			}
			//xlj update 2017年3月16日11:33:33，缺少状态更新			
			String khYGP2 = dataMap2.get("YGP");
			if(!khYGP2.equals("已挂牌")){
				khMap.put("YGP", "已挂牌");
			}			
			if(!khMap.isEmpty()){
				HashMap<String,String> conditionMap = new HashMap<String,String>();
				conditionMap.put("CUSTOMERNO", customerno);
				List<HashMap> khList = DemAPI.getInstance().getList(KHUUID, conditionMap, null);
				if(khList!=null&&khList.size()>=1){
					HashMap khListMap = khList.get(0);
					HashMap oldListMap = new HashMap();
					oldListMap.put("CUSTOMERNO", customerno);
					oldListMap.put("CUSTOMERNAME", khListMap.get("CUSTOMERNAME"));
					oldListMap.put("ZQJC", khListMap.get("ZQJC"));
					oldListMap.put("ZQDM", khListMap.get("ZQDM"));
					for (String key : khMap.keySet()) {
						khListMap.put(key, khMap.get(key));
			        }
					Long khInsId = Long.parseLong(khListMap.get("INSTANCEID").toString());
					Long khId = Long.parseLong(khListMap.get("ID").toString());
					DemAPI.getInstance().updateFormData(KHUUID, khInsId, khListMap, khId, false);
					if(zqbUpdateDataService==null){
						zqbUpdateDataService = (ZqbUpdateDataService)SpringBeanUtil.getBean("zqbUpdateDataService");
					}
					zqbUpdateDataService.updateDataCustomer(oldListMap, khListMap);
				}
			}
			
			//更新持续督导个人信息
			//xlj update 没有判断立项和挂牌中的督导是否是同一个人，同一个人才能更新其联系方式
			if(!gpddap.equals("") && gpddap.equals(lxddap)){
				HashMap<String,String> orguserMap = new HashMap<String,String>();
				String cxddmobile1 = dataMap.get("CXDDMOBILE")==null?"":dataMap.get("CXDDMOBILE").toString();
				String cxddmobile2 = dataMap2.get("CXDDMOBILE");
				if(!cxddmobile1.equals("") && !cxddmobile1.equals(cxddmobile2)){
					orguserMap.put("MOBILE", cxddmobile1);
				}
				String cxddphone1 = dataMap.get("CXDDPHONE")==null?"":dataMap.get("CXDDPHONE").toString();
				String cxddphone2 = dataMap2.get("CXDDOFFICETEL");
				if(!cxddphone1.equals("") && !cxddphone1.equals(cxddphone2)){
					orguserMap.put("OFFICETEL", cxddphone1);
				}
				String cxddemail1 = dataMap.get("CXDDEMAIL")==null?"":dataMap.get("CXDDEMAIL").toString();
				String cxddemail2 = dataMap2.get("CXDDEMAIL");
				if(!cxddemail1.equals("") && !cxddemail1.equals(cxddemail2)){
					orguserMap.put("EMAIL", cxddemail1);
				}
				if(!orguserMap.isEmpty()){
					if(orgUserService==null){
						orgUserService = (OrgUserService)SpringBeanUtil.getBean("orgUserService");
					}
					String userid = dataMap2.get("CXDDUSERID");//立项的持续督导USERID
					OrgUser userModel = orgUserService.getUserModel(userid);
					if(userModel!=null){
						for (String key : orguserMap.keySet()) {
							if(key.equals("EMAIL")){
								userModel.setEmail(orguserMap.get(key));
							}else if(key.equals("OFFICETEL")){
								userModel.setOfficetel(orguserMap.get(key));
							}else if(key.equals("MOBILE")){
								userModel.setMobile(orguserMap.get(key));
							}
						}
						orgUserService.updateBoData(userModel);
					}
				}
			}
			
			//更新挂牌表的流程相关信息
			Long dataId=Long.parseLong(dataMap.get("ID").toString());
			String customername = dataMap.get("GSMC").toString();
			String processActDefId = this.getActDefId();
			Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
			dataMap.put("LCBH", processActDefId);
			dataMap.put("LCBS", instanceId);
			dataMap.put("TASKID", newTaskId.getId());
			dataMap.put("SPZT", newTaskId.getName());
			LogUtil.getInstance().addLog(dataId, "挂牌登记及归档", customername+"变更挂牌登记及归档审核信息");
			dataMap.put("TJSJ", sdf.format(date));
			flag=ProcessAPI.getInstance().updateFormData(processActDefId, instanceId, dataMap, dataId, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		}
		return flag;
	}
	
	public HashMap<String,String> getDataMap(String customerno){
		StringBuffer sql = new StringBuffer();
		HashMap<String,String> result = new HashMap<String,String>();
		sql.append("SELECT LCGG.FJ XMGGZL,SBSH.FJ SBSHZL,NHFK.FJ NHFKZL,GZFK.FJ GZFKZL,"
				+ " PJ.ZCLR,PJ.OWNER,PJ.MANAGER,PJ.DDAP,PJ.XMZH,PJ.XMXY,PJ.XMLS,"
				+ " KH.CUSTOMERNAME,KH.ZQDM,KH.ZQJC,KH.USERNAME,KH.TEL KHTEL,KH.EMAIL KHEMAIL,KH.YGP YGP,"
				+ " ORGUSER.USERID,ORGUSER.MOBILE,ORGUSER.OFFICETEL,ORGUSER.EMAIL"
				+ " FROM (SELECT CUSTOMERNO FROM BD_ZQB_GPDJJGD WHERE CUSTOMERNO = ?) GP "
				+ " INNER JOIN BD_ZQB_PJ_BASE PJ ON GP.CUSTOMERNO = PJ.CUSTOMERNO "
				+ " LEFT JOIN BD_ZQB_LCGG LCGG ON GP.CUSTOMERNO = LCGG.CUSTOMERNO "
				+ " LEFT JOIN BD_ZQB_SQNH SBSH ON GP.CUSTOMERNO = SBSH.CUSTOMERNO "
				+ " LEFT JOIN BD_ZQB_NHFK NHFK ON GP.CUSTOMERNO = NHFK.CUSTOMERNO "
				+ " LEFT JOIN BD_ZQB_KH_BASE KH ON GP.CUSTOMERNO = KH.CUSTOMERNO "
				+ " LEFT JOIN BD_ZQB_GZFKJHF GZFK ON GP.CUSTOMERNO = GZFK.CUSTOMERNO "
				+ " LEFT JOIN ORGUSER ORGUSER ON SUBSTR(PJ.DDAP,0, INSTR(PJ.DDAP,'[',1)-1) = ORGUSER.USERID");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			rs = ps.executeQuery();
			while (rs.next()) {
				String xmggzl = rs.getString("XMGGZL");
				String sbshzl = rs.getString("SBSHZL");
				String nhfkzl = rs.getString("NHFKZL");
				String gzfkzl = rs.getString("GZFKZL");
				String zclr = rs.getString("ZCLR");
				String owner = rs.getString("OWNER");
				String manager = rs.getString("MANAGER");
				String cxdd = rs.getString("DDAP");
				String xmzh = rs.getString("XMZH");
				String xmxy = rs.getString("XMXY");
				String xmls = rs.getString("XMLS");
				String customername = rs.getString("CUSTOMERNAME");
				String zqdm = rs.getString("ZQDM");
				String zqjc = rs.getString("ZQJC");
				String username = rs.getString("USERNAME");
				String khTel = rs.getString("KHTEL");
				String khEmail = rs.getString("KHEMAIL");
				String cxddUserid = rs.getString("USERID");
				String cxddMobile = rs.getString("MOBILE");
				String cxddOfficetel = rs.getString("OFFICETEL");
				String cxddEmail = rs.getString("EMAIL");
				
				result.put("XMGGZL", xmggzl);
				result.put("SBSHZL", sbshzl);
				result.put("NHFKZL", nhfkzl);
				result.put("GZFKZL", gzfkzl);
				result.put("ZCLR", zclr);
				result.put("OWNER", owner);
				result.put("MANAGER", manager);
				result.put("CXDD", cxdd);
				result.put("XMZH", xmzh);
				result.put("XMXY", xmxy);
				result.put("XMLS", xmls);
				result.put("CUSTOMERNAME", customername);
				result.put("ZQDM", zqdm);
				result.put("ZQJC", zqjc);
				result.put("USERNAME", username);
				result.put("KHTEL", khTel);
				result.put("KHEMAIL", khEmail);
				result.put("CXDDUSERID", cxddUserid);
				result.put("CXDDMOBILE", cxddMobile);
				result.put("CXDDOFFICETEL", cxddOfficetel);
				result.put("CXDDEMAIL", cxddEmail);
				result.put("YGP", rs.getString("YGP"));
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
}
