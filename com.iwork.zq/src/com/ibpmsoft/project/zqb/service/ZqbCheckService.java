package com.ibpmsoft.project.zqb.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
public class ZqbCheckService{
	private static Logger logger = Logger.getLogger(ZqbCheckService.class);
	private TaskService taskService;
	private ProcessEngine processEngine;
	private OrgUserService orgUserService;
	public static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private static String PJUUID="33833384d109463285a6a348813539f1";
	
	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public String getCheckOrguser(String userid) {
		String info="";
		orgUserService=(OrgUserService) SpringBeanUtil.getBean("orgUserService");
		OrgUser model = orgUserService.getUserModel(userid.toUpperCase().trim());
		if (model == null)
	    {
			return info;
	    }
	    else {
	    	info="该帐号已存在,请修改。";
	    }
		return info;
	}

	public String getcheckProject(String customerno, String customername) {
		String info="";
		HashMap<String,String> conditionMap=new HashMap<String,String>();
		conditionMap.put("CUSTOMERNO", customerno);
		conditionMap.put("CUSTOMERNAME", customername);
		List<HashMap> list = DemAPI.getInstance().getList(PJUUID, conditionMap, null);
		if(!list.isEmpty()){
			info="该客户已创建项目，无法创建多个。";
		}
		return info;
	}

	public String getcheckVote(String customerno, Long tzggid,Long instanceid) {
		String info="";
		if(instanceid==0){
			HashMap<String,Object> conditionMap=new HashMap<String,Object>();
			conditionMap.put("CUSTOMERNO", customerno);
			conditionMap.put("TZGGID", tzggid);
			Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
			String wjdcuuid = config.get("wjdcuuid");
			//List<HashMap> list = DemAPI.getInstance().getList(wjdcuuid, conditionMap, "ID");
			List lables_ = new ArrayList();
			for (int i = 1; i <= 50; i++) {
				lables_.add("WT"+i);
				lables_.add("AS"+i);
				lables_.add("BZ"+i);
				lables_.add("PL"+i);
				lables_.add("PZ"+i);
				lables_.add("YJXYSFFS"+i);
			}
			lables_.add("CUSTOMERNAME");
			lables_.add("CUSTOMERNO");
			lables_.add("USERNAME");
			lables_.add("USERID");
			lables_.add("TZGGID");
			lables_.add("ID");
			String sql_ = "SELECT * FROM BD_VOTE_WJDC WHERE CUSTOMERNO=? AND TZGGID=?";
			Map params__ = new HashMap();
			params__.put(1, customerno);
			params__.put(2, tzggid);
			List<HashMap> list = com.iwork.commons.util.DBUtil.getDataList(lables_, sql_, params__);
			if(!list.isEmpty()){
				info="该提示函您已回答过，不能再次回答。";
			}
		}
		return info;
	}

	public String getcheckPwd(String password,String userid) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer");
		String match="";
		if(zqServer!=null&&zqServer.equals("htzq")){
			match="(?=^.{12,}$)[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*((\\d+[a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|(\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+)|([a-zA-Z]+\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|([a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+[a-zA-Z]+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+\\d+))[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*";
		}else{
			if(zqServer.equals("hlzq")){
				//match="(?=^.{8,}$)[-\\da-zA-Z]*((\\d+[a-zA-Z]+)|(\\d+[a-zA-Z]+)|([a-zA-Z]+\\d+)|([a-zA-Z]+\\d+)|(\\d+[a-zA-Z]+)|([a-zA-Z]+\\d+))[-\\da-zA-Z]*";
				//上面的语句在华龙上不能创建带特殊字符的密码，所以使用下面的   20180709 王欢
				match="(?!^([0-9]|[^A-z])+$)(?!^([a-zA-Z]|[^0-9])+$)(?!^[^A-z0-9]+$)^.{8,}$";
				
			}else{
				match="(?=^.{8,}$)[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*((\\d+[a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|(\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+)|([a-zA-Z]+\\d+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+)|([a-zA-Z]+[-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+\\d+[a-zA-Z]+)|([-`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]+[a-zA-Z]+\\d+))[-\\da-zA-Z`=\\\\\\[\\];',./~!@#$%^&*()_+|{}:<>?]*";
			}
		}
		String info="";
		if(password.equals(userid)){
			return "namePwd";
		}
		
		Map params=new HashMap();
		params.put(1, userid);
		String oldPwd=DBUTilNew.getDataStr("password", "select password from orguser where userid= ?", params);
		String SHAPwd=ShaSaltUtil.getEncryptedPwd(password,userid.toUpperCase());
		if(SHAPwd.equals(oldPwd)){
			return "oldPwd";
		}
		boolean matches=password.matches(match);
		if(matches){
			info="true";
		}else if(zqServer != null && zqServer.equals("htzq")){
			info="htzq";
		}else{
			if(zqServer != null && zqServer.equals("hlzq")){
				info="hlzq";
			}else{
				info="false";
			}
			
		}
		return info;
	}

	public String getcheckDeptName(Long deptid,String deptname) {
		String info="";
		if(deptname!=null&&!deptname.equals("")){
			Map params = new HashMap();
			params.put(1, deptname.trim().toUpperCase());
			int count = 0;
			if(deptid==null||deptid==0){
				count=DBUTilNew.getInt("CNUM","SELECT COUNT(*) CNUM FROM ORGDEPARTMENT WHERE UPPER(TRIM(DEPARTMENTNAME))= ? ",params );
			}else{
				count=DBUTilNew.getInt("CNUM","SELECT COUNT(*) CNUM FROM ORGDEPARTMENT WHERE UPPER(TRIM(DEPARTMENTNAME))= ?  AND ID<>"+deptid,params);
			}
			if(count==0){
				info="success";
			}
		}
		return info;
	}

	public String getcheckProjectSubSP(Long instanceid) {
		String info="";
		HashMap fromData2 = DemAPI.getInstance().getFromData(instanceid);
		String createdate = fromData2.get("CREATEDATE").toString();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d1;
		Date d2;
		try {
			d1 = sf.parse(createdate);//把时间格式化
			d2 = sf.parse("2016-08-20 00:00:00");//把时间格式化
			if(d1.getTime() <= d2.getTime()){//比较大小；
				return "项目早于8月20日";
			}  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				d1 = sdf.parse(createdate);//把时间格式化
				d2 = sf.parse("2016-08-20 00:00:00");
				if(d1.getTime() <= d2.getTime()){//比较大小；
					return "项目早于8月20日";
				}  
			} catch (ParseException e1) {
			}
		}
		
		List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLR");
		List<HashMap> fromSubCljgData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLJG");
		if(fromSubClrData.isEmpty()&&fromSubCljgData.isEmpty()){
			info="空";
		}else{
			HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
			if(fromData!=null&&fromData.get("ZBSPZT")!=null&&!fromData.get("ZBSPZT").toString().equals("未提交")&&!fromData.get("ZBSPZT").toString().equals("")){
				info="存在流程";
			}
		}
		return info;
	}
	
	public String getcheckProjectBgczSP(Long instanceid) {
		String info="";
		List<HashMap> fromSubClrData = DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_CLRB");
		if(fromSubClrData.isEmpty()){
			info="空";
		}else{
			HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
			if(fromData!=null&&fromData.get("ZBSPZT")!=null&&!fromData.get("ZBSPZT").toString().equals("未提交")&&!fromData.get("ZBSPZT").toString().equals("")){
				info="存在流程";
			}
		}
		return info;
	}

	public String getcheckLcProjectSubSP(String projectno) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("PROJECTNO", projectno);
		List<HashMap> pjList = DemAPI.getInstance().getAllList(PJUUID, map, null);
		HashMap pjMap=pjList.get(0);
		Long instanceid = Long.parseLong(pjMap.get("INSTANCEID").toString());
		return getcheckProjectSubSP(instanceid);
	}

	public String getcheckXmjd(String customerno, String jdmc) {
		Map params = new HashMap();
		params.put(1, customerno);
		StringBuffer jsonHtml = new StringBuffer();
		if(customerno!=null&&!"".equals(customerno)&&jdmc!=null&&!"".equals(jdmc)){
			if(jdmc.equals("XMLX")){
				String xmlxUUID = ConfigUtil.readValue(CN_FILENAME, "xmlxuuid");
				HashMap conditionMap = new HashMap();
				conditionMap.put("CUSTOMERNO", customerno);
				List<HashMap> list = DemAPI.getInstance().getList(xmlxUUID, conditionMap, null);
				if(list!=null&&!list.isEmpty()){
					Long instanceid = Long.parseLong(list.get(0).get("INSTANCEID").toString());
					jsonHtml.append("{\"instanceId\":"+instanceid+"}");
				}else{
					jsonHtml.append("{\"instanceId\":0}");
				}
			}else if(jdmc.equals("XMGG")){
				Long lcggInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_LCGG WHERE CUSTOMERNO= ? ",params);
				if(lcggInsId==0){
					jsonHtml.append("{\"instanceId\":"+lcggInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(lcggInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+lcggInsId+"}");
				}
			}else if(jdmc.equals("XMNH")){
				Long xmnhInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_SQNH WHERE CUSTOMERNO= ? ",params);
				if(xmnhInsId==0){
					jsonHtml.append("{\"instanceId\":"+xmnhInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(xmnhInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+xmnhInsId+"}");
				}
			}else if(jdmc.equals("NHFK")){
				Long nhfkInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_NHFK WHERE CUSTOMERNO= ? ",params);
				if(nhfkInsId==0){
					jsonHtml.append("{\"instanceId\":"+nhfkInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(nhfkInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+nhfkInsId+"}");
				}
			}else if(jdmc.equals("XMGP")){
				HashMap<String,String> conditionMap = new HashMap<String,String>();
				conditionMap.put("CUSTOMERNO", customerno);
				String xmgpuuid = getConfigUUID("xmgpuuid");
				List<HashMap> list = DemAPI.getInstance().getList(xmgpuuid, conditionMap, null);
				if(list!=null&&!list.isEmpty()){
					Long instanceid = Long.parseLong(list.get(0).get("INSTANCEID").toString());
					jsonHtml.append("{\"instanceId\":"+instanceid+"}");
				}else{
					jsonHtml.append("{\"instanceId\":0}");
				}
			}else if(jdmc.equals("GZFKJHF")){
				Long gzfkInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_GZFKJHF WHERE CUSTOMERNO= ? ",params);
				if(gzfkInsId==0){
					jsonHtml.append("{\"instanceId\":"+gzfkInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(gzfkInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+gzfkInsId+"}");
				}
			}else if(jdmc.equals("GPDJJGD")){
				Long gpdjInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_GPDJJGD WHERE CUSTOMERNO= ? ",params);
				if(gpdjInsId==0){
					jsonHtml.append("{\"instanceId\":"+gpdjInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(gpdjInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+gpdjInsId+"}");
				}
			}
		}
		return jsonHtml.toString();
	}
	
	public String setLxxxReadonly(String projectno){
		StringBuffer jsonHtml = new StringBuffer();
		String username= UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername();
		String readonly = "";

		Map params = new HashMap();
		params.put(1,projectno);
		String spzt = DBUTilNew.getDataStr("spzt","SELECT SPZT FROM BD_ZQB_PJ_BASE WHERE PROJECTNO=? ", params);
		if(spzt!=null&&!spzt.equals("")){
			if(!spzt.equals("驳回")&&(spzt.equals("审批通过")||!username.equals(spzt))){
				readonly = "true";
			}else if(spzt.equals("驳回")){
				String xmfzr = DBUTilNew.getDataStr("USERNAME","SELECT O.USERNAME FROM BD_ZQB_PJ_BASE P LEFT JOIN ORGUSER O ON SUBSTR(P.MANAGER,0, INSTR(P.MANAGER,'[',1)-1)=O.USERID WHERE P.PROJECTNO= ? ", params);
				if(username.equals(xmfzr)){
					readonly = "false";
				}else{
					readonly = "true";
				}
			}else{
				readonly = "false";
			}
		}else{
			readonly = "false";
		}
		jsonHtml.append("{\"readonly\":"+readonly+"}");
		return jsonHtml.toString();
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter)==null?"":config.get(parameter);
		}
		return result;
	}
	
	//根据流程节点，判断删除按钮是否显示
	public String checkLcgg(String actDefId, String actStepDefId, Long instanceId, String khbh) {
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String zqServer = config.get("zqServer");
		boolean isDel=false;
		StringBuffer jsonHtml = new StringBuffer();
		if(instanceId!=null&&instanceId!=0&&zqServer.equals("dgzq")){
			if(actDefId!=null&&actStepDefId!=null&&khbh!=null){
				UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
				String userid = uc._userModel.getUserid().toUpperCase();
				if(actDefId.startsWith("CXDDYFQLC")){
					if(checkDuDao(khbh,userid)){
						isDel=true;
					}
				}else if(actDefId.startsWith("GGSPLC")){
					if(SystemConfig._ggsplcConf.getJd1().equals(actStepDefId)&&checkProcessTask(instanceId)){
						isDel=true;
					}else if(!SystemConfig._ggsplcConf.getJd1().equals(actStepDefId)){
						if(checkDuDao(khbh,userid)){
							isDel=true;
						}
					}
				}else if(actDefId.startsWith("RCYWCB")){
					if(SystemConfig._rcywcbLcConf.getJd1().equals(actStepDefId)&&checkProcessTask(instanceId)){
						isDel=true;
					}else if(!SystemConfig._rcywcbLcConf.getJd1().equals(actStepDefId)){
						if(checkDuDao(khbh,userid)){
							isDel=true;
						}
					}
					isDel=true;
				}
			}
		}else{
			isDel=true;
		}
		jsonHtml.append("{\"isDel\":"+isDel+"}");
		return jsonHtml.toString();
	}
	
	public String checkRycx() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String date="";
		date = sf.format(new Date());
		String flag="";
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		if(user.getOrgroleid()!=3){
			flag="0";
		}else{
			flag="1";
		}
		flag=flag+","+date;
		return flag;
		
	}
	public String checkyz( Long instanceId) {
		String yzid=getConfigUUID("zqServer");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String sql="select s.id,t.orgroleid,t.username from BD_MEET_QTGGZL s left join orguser t on s.qcrid = t.userid where s.yxid= ?  ";
		
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String flag="";
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceId);
			rs = ps.executeQuery();
			int num=0; 
			while(rs.next()){ num++; } 
			if(!yzid.equals("dgzq")){
				flag="2";
			}else{
				if(num>0){
					if(user.getOrgroleid()!=3){
						flag="2";
					}else{
						flag="1";
					}
					
				}else{
					flag="2";
				}
			}
			
		} catch (Exception e) {
			logger.error(e,e);
		}finally{
			DBUtil.close(conn, ps, rs);
		}
		return flag;
		
	}
	//验证登录人是否为当前客户的督导人员
	public Boolean checkDuDao(String customerno,String userid){
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) NUM FROM BD_MDM_KHQXGLB WHERE KHBH=? AND (");
		sql.append(" SUBSTR(KHFZR,0, INSTR(KHFZR,'[',1)-1)=? ");
		sql.append(" OR SUBSTR(FHSPR,0, INSTR(FHSPR,'[',1)-1)=? ");
		sql.append(" OR SUBSTR(ZZCXDD,0, INSTR(ZZCXDD,'[',1)-1)=? )");
		Integer num = 0;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, customerno);
			ps.setString(2, userid);
			ps.setString(3, userid);
			ps.setString(4, userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				num = rs.getInt("NUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return num>0?true:false;
	}
	
	//验证当前流程数据是否为起草状态
	public Boolean checkProcessTask(Long executionId){
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) PNUM FROM PROCESS_RU_TASK WHERE EXECUTION_ID_=? AND NAME_='起草'");
		Integer num = 0;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, executionId);
			rs = ps.executeQuery();
			while (rs.next()) {
				num = rs.getInt("PNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return num==1?true:false;
	}
	public String checkSfkfId(String actDefId, String actStepDefId) {
		boolean isView = false;
		StringBuffer jsonHtml = new StringBuffer();
		if(actDefId!=null&&actStepDefId!=null){
			 if(actDefId.startsWith("XMLXSPLC")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("XMLXSPLC");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._xmlxSpLcConf.getJd3().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","推荐挂牌项目");
					}else if(SystemConfig._xmlxSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","推荐挂牌项目");
					}else if(SystemConfig._xmlxSpLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","推荐挂牌项目");
					}
				}
			}else if(actDefId.startsWith("XMGGSH")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("XMGGSH");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._gugaiLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","股改","推荐挂牌项目");
					}else if(SystemConfig._gugaiLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","股改","推荐挂牌项目");
					}else if(SystemConfig._gugaiLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","股改","推荐挂牌项目");
					}
				}
			}else if(actDefId.startsWith("XMNHSH")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("XMNHSH");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._xmnhLcConf.getJd2().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报审核","推荐挂牌项目");
					}else if(SystemConfig._xmnhLcConf.getJd3().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报审核","推荐挂牌项目");
					}else if(SystemConfig._xmnhLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报审核","推荐挂牌项目");
					}else if(SystemConfig._xmnhLcConf.getJd6().equals(actStepDefId)){
						String index="NODE3";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报审核","推荐挂牌项目");
					}else if(SystemConfig._xmnhLcConf.getJd7().equals(actStepDefId)){
						String index="NODE4";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报审核","推荐挂牌项目");
					}else if(SystemConfig._xmnhLcConf.getJd8().equals(actStepDefId)){
						String index="NODE5";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报审核","推荐挂牌项目");
					}
				}
			}else if(actDefId.startsWith("NHFKSH")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("NHFKSH");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._nhfkLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","内核反馈及回复","推荐挂牌项目");
					}else if(SystemConfig._nhfkLcConf.getJd3().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","内核反馈及回复","推荐挂牌项目");
					}else if(SystemConfig._nhfkLcConf.getJd4().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","内核反馈及回复","推荐挂牌项目");
					}
				}
			}else if(actDefId.startsWith("GZFKJHF")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("GZFKJHF");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._gzfkLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","股转反馈及回复","推荐挂牌项目");
					}else if(SystemConfig._gzfkLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","股转反馈及回复","推荐挂牌项目");
					}else if(SystemConfig._gzfkLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","股转反馈及回复","推荐挂牌项目");
					}else if(SystemConfig._gzfkLcConf.getJd6().equals(actStepDefId)){
						String index="NODE3";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","股转反馈及回复","推荐挂牌项目");
					}
				}
			}else if(actDefId.startsWith("GPDJJGD")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("GPDJJGD");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._guapaiLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","挂牌登记及归档","推荐挂牌项目");
					}else if(SystemConfig._guapaiLcConf.getJd3().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","挂牌登记及归档","推荐挂牌项目");
					}else if(SystemConfig._guapaiLcConf.getJd4().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","挂牌登记及归档","推荐挂牌项目");
					}
				}
			}else if(actDefId.startsWith("XMLXNFX")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("XMLXNFX");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._xmlxnfxLcConf.getJd3().equals(actStepDefId)||SystemConfig._xmlxnfxLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","股票发行项目");
					}else if(SystemConfig._xmlxnfxLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","股票发行项目");
					}else{
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","股票发行项目");
					}
				}
			}else if(actDefId.startsWith("FAZLBS")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("FAZLBS");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._fazlbsLcConf.getJd3().equals(actStepDefId)||SystemConfig._fazlbsLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","方案资料报审","股票发行项目");
					}else if(SystemConfig._fazlbsLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","方案资料报审","股票发行项目");
					}else{
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","方案资料报审","股票发行项目");
					}
				}
			}else if(actDefId.startsWith("SBZL")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("SBZL");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._sbzlLcConf.getJd3().equals(actStepDefId)||SystemConfig._sbzlLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","股票发行项目");
					}else if(SystemConfig._sbzlLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","股票发行项目");
					}else if(SystemConfig._sbzlLcConf.getJd6().equals(actStepDefId)){
						String index="NODE3";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","股票发行项目");
					}else{
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","股票发行项目");
					}
				}
			}else if(actDefId.startsWith("ZLGDSJFX")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("ZLGDSJFX");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._zlgdsjfxLcConf.getJd5().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","股票发行项目");
					}else if(SystemConfig._zlgdsjfxLcConf.getJd6().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","股票发行项目");
					}else{
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","股票发行项目");
					}
				}
			}else if(actDefId.startsWith("BGXMLXSPLC")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("BGXMLXSPLC");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._bgXmlxSpLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","并购项目");
					}else if(SystemConfig._bgXmlxSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","并购项目");
					}
				}
			}else if(actDefId.startsWith("BGFAZLBS")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("BGFAZLBS");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._bgFangAnBSSpLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","方案资料报审","并购项目");
					}else if(SystemConfig._bgFangAnBSSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","方案资料报审","并购项目");
					}else if(SystemConfig._bgFangAnBSSpLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","方案资料报审","并购项目");
					}
				}
			}else if(actDefId.startsWith("BGSBZLSPLC")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("BGSBZLSPLC");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._bgSbzlSpLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","并购项目");
					}else if(SystemConfig._bgSbzlSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","并购项目");
					}else if(SystemConfig._bgSbzlSpLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","并购项目");
					}else if(SystemConfig._bgSbzlSpLcConf.getJd6().equals(actStepDefId)){
						String index="NODE3";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","申报资料","并购项目");
					}
				}
			}else if(actDefId.startsWith("BGZLGDSPLC")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("BGZLGDSPLC");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._bgZlgdSpLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","并购项目");
					}else if(SystemConfig._bgZlgdSpLcConf.getJd3().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","并购项目");
					}else if(SystemConfig._bgZlgdSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","并购项目");
					}
				}
			}else if(actDefId.startsWith("CWXMLXSPLC")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("CWXMLXSPLC");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._cwXmlxSpLcConf.getJd3().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","一般性财务顾问项目");
					}else if(SystemConfig._cwXmlxSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","项目立项","一般性财务顾问项目");
					}
				}
			}else if(actDefId.startsWith("CWGZJDHB")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("CWGZJDHB");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._cwGzjdhbSpLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","工作进度汇报","一般性财务顾问项目");
					}else if(SystemConfig._cwGzjdhbSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","工作进度汇报","一般性财务顾问项目");
					}else if(SystemConfig._cwGzjdhbSpLcConf.getJd5().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","工作进度汇报","一般性财务顾问项目");
					}
				}
			}else if(actDefId.startsWith("CWZLGDSPLC")){
				String processActDefId = ProcessAPI.getInstance().getProcessActDefId("CWZLGDSPLC");
				if(actDefId.equals(processActDefId)){
					if(SystemConfig._cwZlgdSpLcConf.getJd2().equals(actStepDefId)){
						String index="EXTEND";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","一般性财务顾问项目");
					}else if(SystemConfig._cwZlgdSpLcConf.getJd3().equals(actStepDefId)){
						String index="NODE1";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","一般性财务顾问项目");
					}else if(SystemConfig._cwZlgdSpLcConf.getJd4().equals(actStepDefId)){
						String index="NODE2";
						isView = getCheckData(index,"BD_ZQB_SXSPRYSZ","资料归档","一般性财务顾问项目");
					}
				}
			}else if(actDefId.startsWith("GGSPLC")){//(DM)公告流程
				isView=true;
			}else if(actDefId.startsWith("CXDDYFQLC")){//(DD)公告流程
				isView=true;
			}else if(actDefId.startsWith("RCYWCB")){//(DD)日常业务
				isView=true;
			}else if(actDefId.startsWith("TXXMGLLC")){//投行项目显示扣分标识（0.1 0.2 0.3）
				isView=true;
			}
		}
		String configParameter = ConfigUtil.readAllProperties("/common.properties").get("zqServer")==null?"":ConfigUtil.readAllProperties("/common.properties").get("zqServer");
		if(configParameter.equals("sxzq") || configParameter.equals("hlzq")){
			isView=true;
		}
		jsonHtml.append("{\"isView\":"+isView+"}");
		return jsonHtml.toString();
	}
	//驳回扣分
	public String scoreSet(Long instanceid, Long id, String actDefId, String countdownscore) {
		//if(actDefId.startsWith("TXXMGLLC")){return "1";} //投行项目不执行扣分操作
		String info="0";		
		if(instanceid!=null&&id!=null&&actDefId!=null){
			HashMap data = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(data!=null){
				BigDecimal oldscore;
				if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
					oldscore = new BigDecimal(data.get("GGDF").toString());
				}else if(actDefId.startsWith("RCYWCB")){
					oldscore = new BigDecimal(data.get("EXTEND1").toString());
				}else if(actDefId.startsWith("TXXMGLLC")){
					oldscore = new BigDecimal(data.get("EXTEND2").toString());
				}else{
					oldscore = new BigDecimal(data.get("SCORE").toString());
				}
				BigDecimal newscore = oldscore.subtract(new BigDecimal(countdownscore.equals("undefined")?"0.0":countdownscore));
				boolean flag = false;
				if(newscore.doubleValue()>0){
					if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
						data.put("GGDF", newscore.doubleValue());
					}else if (actDefId.startsWith("RCYWCB")){
						data.put("EXTEND1", newscore.doubleValue());
					}else if (actDefId.startsWith("TXXMGLLC")){
						data.put("EXTEND2", newscore.doubleValue());
					}else{
						data.put("SCORE", newscore.doubleValue());
						data.put("TJSJ", data.get("TJSJ")==null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.get("TJSJ")));
					}
					flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, data, id, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					if(flag){addLog(instanceid, actDefId, countdownscore, data.get("ID").toString());}
				}else{
					if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
						data.put("GGDF", 0);
					}else if (actDefId.startsWith("RCYWCB")){
						data.put("EXTEND1", 0);
					}else{
						data.put("SCORE", 0);
						data.put("TJSJ", data.get("TJSJ")==null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.get("TJSJ")));
					}
					flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, data, id, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					if(flag){addLog(instanceid, actDefId, oldscore.toString(), data.get("ID").toString());}
				}
				if(flag){
					if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
						DBUtil.executeUpdate("UPDATE BD_MEET_QTGGZL SET GGDF="+newscore.toString()+" WHERE LCBS="+instanceid);
					}else if(actDefId.startsWith("RCYWCB")){
						DBUtil.executeUpdate("UPDATE Bd_Xp_Rcywcbzsj SET EXTEND1="+newscore.toString()+" WHERE YXID="+instanceid);
					}else if(actDefId.startsWith("TXXMGLLC")){
						DBUtil.executeUpdate("UPDATE BD_XP_TXGLRZWLB SET EXTEND2="+newscore.toString()+" WHERE YXID="+instanceid);
					}
					info = "1";
				}
			}
		}
		return info;
	}
	//提交扣分
	public String tjscoreSet(Long instanceid, Long id, String actDefId, String countdownscore,String type,String memo,String bkfr) {
		//查询扣分人配置信息      0：只显示董秘：1：显示各个节点办理人
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String ggkfr = config.get("ggkfr");
		
		String info="0";
		if(countdownscore==null || countdownscore.equals("undefined") || countdownscore.equals(""))
		{
			countdownscore = "0";
		}
		if(instanceid!=null&&id!=null&&actDefId!=null){
			HashMap data = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			if(ggkfr.equals("0")&&(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC"))){
				bkfr=data.get("QCRID").toString();
			}
			
			if(data!=null){
				BigDecimal oldscore;//原最终得分
				String typeforscore = "0.00";
				if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
					oldscore = new BigDecimal(data.get("GGDF").toString());
					if(type==null || type.equals("undefined") || type.equals(""))
					{
						typeforscore = "0.00";
					}
					else{
						//typeforscore = DBUtil.getString("SELECT SUM(COUNT) NUM FROM BD_XP_KFLXB WHERE ID IN ("+type+")", "NUM");
						StringBuffer sql = new StringBuffer();
						Map params = new HashMap();
						sql.append(" SELECT SUM(COUNT) NUM FROM BD_XP_KFLXB WHERE ID IN ( ");
						String[] strIDs = type.split(",");
						int n=1;
						for (int i = 0; i < strIDs.length; i++) {
							if(i==(strIDs.length-1)){
								sql.append("?");
							}else{
								sql.append("?,");
							}
							params.put(n,strIDs[i].replaceAll("'", ""));
							n++;
						}
						sql.append(" )");
						typeforscore = DBUTilNew.getDataStr("NUM",sql.toString(), params);
					}
					
					countdownscore=typeforscore;									
				}else if(actDefId.startsWith("RCYWCB")){
					oldscore = new BigDecimal(data.get("EXTEND1").toString());
					if(type==null || type.equals("undefined") || type.equals(""))
					{
						typeforscore = "0.00";
					}
					else{
						//typeforscore = DBUtil.getString("SELECT SUM(COUNT) NUM FROM BD_XP_KFLXB WHERE ID IN ("+type+")", "NUM");
						StringBuffer sql = new StringBuffer();
						Map params = new HashMap();
						sql.append(" SELECT SUM(COUNT) NUM FROM BD_XP_KFLXB WHERE ID IN ( ");
						String[] strIDs = type.split(",");
						int n=1;
						for (int i = 0; i < strIDs.length; i++) {
							if(i==(strIDs.length-1)){
								sql.append("?");
							}else{
								sql.append("?,");
							}
							params.put(n,strIDs[i].replaceAll("'", ""));
							n++;
						}
						sql.append(" )");
						typeforscore = DBUTilNew.getDataStr("NUM",sql.toString(), params);
					}
					
					countdownscore=typeforscore;		
				}else if(actDefId.startsWith("TXXMGLLC")){
					oldscore = new BigDecimal(data.get("EXTEND2").toString());
				}
				else{
					oldscore = new BigDecimal(data.get("SCORE").toString());
				}				
				boolean flag = false;
				if(Float.parseFloat(countdownscore)>0)
				{
					BigDecimal newscore = oldscore.subtract(new BigDecimal(countdownscore));
					if(newscore.doubleValue()>0){
						if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
							data.put("GGDF", newscore.doubleValue());
						}else if (actDefId.startsWith("RCYWCB")){
							data.put("EXTEND1", newscore.doubleValue());
						}else if (actDefId.startsWith("TXXMGLLC")){
							data.put("EXTEND2", newscore.doubleValue());
						}else{
							data.put("SCORE", newscore.doubleValue());
							data.put("TJSJ", data.get("TJSJ")==null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.get("TJSJ")));
						}
						flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, data, id, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);					
					}else{
						if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
							data.put("GGDF", 0);
						}else if (actDefId.startsWith("RCYWCB")){
							data.put("EXTEND1", 0);
						}else if (actDefId.startsWith("TXXMGLLC")){
							data.put("EXTEND2", 0);
						}else{
							data.put("SCORE", 0);
							data.put("TJSJ", data.get("TJSJ")==null?"":new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data.get("TJSJ")));
						}
						flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceid, data, id, false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);				
						if(flag){
							newscore=new BigDecimal(0);
						}
					}
					if(flag){
						tjaddLog(instanceid, actDefId, countdownscore, data.get("ID").toString(),type,memo,bkfr);
						if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
							DBUtil.executeUpdate("UPDATE BD_MEET_QTGGZL SET GGDF="+newscore.toString()+" WHERE LCBS="+instanceid);
						}else if(actDefId.startsWith("RCYWCB")){
							DBUtil.executeUpdate("UPDATE Bd_Xp_Rcywcbzsj SET EXTEND1="+newscore.toString()+" WHERE YXID="+instanceid);
						}else if(actDefId.startsWith("TXXMGLLC")){
							DBUtil.executeUpdate("UPDATE BD_XP_TXGLRZWLB SET EXTEND2="+newscore.toString()+" WHERE YXID="+instanceid);
						}
					}
				}
				info = "1";
			}
		}
		return info;
	}

	private void addLog(Long instanceid, String actDefId, String countdownscore,String dataid) {
		Task t = ProcessAPI.getInstance().newTaskId(instanceid);
		Long formid = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
		
		List lables = new ArrayList();lables.add("TAG");lables.add("TABLENAME");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.ID AS TAG,B.ENTITYNAME AS TABLENAME FROM SYS_ENGINE_FORM_BIND A LEFT JOIN sys_Engine_Metadata B ON A.METADATAID=B.ID WHERE A.FORMID=? AND A.INSTANCEID=?");
		Map params = new HashMap();params.put(1, formid);params.put(2, instanceid);
		List<HashMap> dat = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String demUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='扣分日志记录表'", "UUID");
		
		String tag = dat.get(0).get("TAG").toString();//表ID
		String tablename = dat.get(0).get("TABLENAME").toString();
		String kfrid = uc._userModel.getUserid();
		String bkfrid = t.getAssignee();
		String kfsj = UtilDate.getNowDatetime();
		String fs = countdownscore;
		Long kfrzins = DemAPI.getInstance().newInstance(demUUID, kfrid);
		HashMap kfdata = new HashMap();
		kfdata.put("TAG", dataid);
		kfdata.put("TABLENAME", tablename);
		kfdata.put("KFRID", kfrid);
		kfdata.put("BKFRID", bkfrid);
		kfdata.put("KFSJ", kfsj);
		kfdata.put("FS", fs);
		kfdata.put("ACTDEFID", actDefId);
		kfdata.put("TASKID", t.getId());
		if(!fs.equals("undefined")){
			DemAPI.getInstance().saveFormData(demUUID, kfrzins, kfdata, false);
		}
		
	}
	private void tjaddLog(Long instanceid, String actDefId, String countdownscore,String dataid,String type,String memo,String bkfr) {
		Task t = ProcessAPI.getInstance().newTaskId(instanceid);
		Long formid = ProcessAPI.getInstance().getProcessDefaultFormId(actDefId);
		
		List lables = new ArrayList();lables.add("TAG");lables.add("TABLENAME");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.ID AS TAG,B.ENTITYNAME AS TABLENAME FROM SYS_ENGINE_FORM_BIND A LEFT JOIN sys_Engine_Metadata B ON A.METADATAID=B.ID WHERE A.FORMID=? AND A.INSTANCEID=?");
		Map params = new HashMap();params.put(1, formid);params.put(2, instanceid);
		List<HashMap> dat = com.iwork.commons.util.DBUtil.getDataList(lables, sql.toString(), params);
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String demUUID = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='扣分日志记录表'", "UUID");
		
		String tag = dat.get(0).get("TAG").toString();//表ID
		String tablename = dat.get(0).get("TABLENAME").toString();
		String kfrid = uc._userModel.getUserid();
		String kfsj = UtilDate.getNowDatetime();
		String typeforscore = null;
		if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")){
			//typeforscore = DBUtil.getString("SELECT SUM(COUNT) NUM FROM BD_XP_KFLXB WHERE ID IN ("+type+")", "NUM");
			StringBuffer sqls = new StringBuffer();
			Map params1 = new HashMap();
			sqls.append(" SELECT SUM(COUNT) NUM FROM BD_XP_KFLXB WHERE ID IN ( ");
			String[] strIDs = type.split(",");
			int n=1;
			for (int i = 0; i < strIDs.length; i++) {
				if(strIDs[i]!=null&&!strIDs[i].equals("")){
					if(i==(strIDs.length-1)){
						sqls.append("?");
					}else{
						sqls.append("?,");
					}
					params1.put(n,strIDs[i].replaceAll("'", ""));
					n++;
				}
			}
			sqls.append(" )");
			typeforscore = DBUTilNew.getDataStr("NUM",sqls.toString(), params1);
			
		}
		BigDecimal fs = new BigDecimal(countdownscore);
		Long kfrzins = DemAPI.getInstance().newInstance(demUUID, kfrid);
		HashMap kfdata = new HashMap();
		kfdata.put("TAG", dataid);
		kfdata.put("TABLENAME", tablename);
		kfdata.put("KFRID", kfrid);
		kfdata.put("BKFRID", bkfr);
		kfdata.put("KFSJ", kfsj);
		kfdata.put("FS", fs);
		kfdata.put("ACTDEFID", actDefId);
		kfdata.put("TASKID", t.getId());
		kfdata.put("TYPE", type);
		kfdata.put("MEMO", memo);
		DemAPI.getInstance().saveFormData(demUUID, kfrzins, kfdata, false);
	}
	
	public Boolean getCheckData(String index,String tableName,String key){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(index).append("SFKF").append(" FROM ").append(tableName).append(" WHERE LCMC=?");
		Long sfkf = 0L;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, key);
			rs = ps.executeQuery();
			while (rs.next()) {
				sfkf = rs.getLong(index+"SFKF");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return sfkf==Long.parseLong("1")?true:false;
	}
	
	public Boolean getCheckData(String index, String tableName, String key, String xmlx){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(index).append("SFKF").append(" FROM ").append(tableName).append(" WHERE LCMC=? AND XMLX=?");
		Long sfkf = 0L;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, key);
			ps.setString(2, xmlx);
			rs = ps.executeQuery();
			while (rs.next()) {
				sfkf = rs.getLong(index+"SFKF");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return sfkf==Long.parseLong("1")?true:false;
	}
	
	public Boolean getCheckDatanfx(String index,String tableName,String key){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(index).append("SFKF AS ").append(index).append("SFKF").append(" FROM ").append(tableName).append(" WHERE TYPE=?");
		Long sfkf = 0L;
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, key);
			rs = ps.executeQuery();
			while (rs.next()) {
				sfkf = rs.getLong(index+"SFKF");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return sfkf==Long.parseLong("1")?true:false;
	}
	
	public String getcheckBgXmjd(String xmbh, String jdmc) {
		Map params = new HashMap();
		params.put(1, xmbh);
		StringBuffer jsonHtml = new StringBuffer();
		if(xmbh!=null&&!"".equals(xmbh)&&jdmc!=null&&!"".equals(jdmc)){
			if(jdmc.equals("BGXMLX")){
				Long xmlxInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_BGXMLX WHERE XMBH= ? ",params);
				if(xmlxInsId==0){
					jsonHtml.append("{\"instanceId\":"+xmlxInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(xmlxInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+xmlxInsId+"}");
				}
			}else if(jdmc.equals("BGFAZLBS")){
				Long fazlbsInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_BGFAZLBS WHERE XMBH= ? ",params);
				if(fazlbsInsId==0){
					jsonHtml.append("{\"instanceId\":"+fazlbsInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(fazlbsInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+fazlbsInsId+"}");
				}
			}else if(jdmc.equals("BGSBZL")){
				Long sbzlInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_BGSBZL WHERE XMBH= ? ",params);
				if(sbzlInsId==0){
					jsonHtml.append("{\"instanceId\":"+sbzlInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(sbzlInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+sbzlInsId+"}");
				}
			}else if(jdmc.equals("BGZLGD")){
				Long zlgdInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_BGZLGD WHERE XMBH=? ",params);
				if(zlgdInsId==0){
					jsonHtml.append("{\"instanceId\":"+zlgdInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(zlgdInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+zlgdInsId+"}");
				}
			}
		}
		return jsonHtml.toString();
	}
	
	public String getcheckCwXmjd(String xmbh, String jdmc) {
		Map params = new HashMap();
		params.put(1,xmbh);
		StringBuffer jsonHtml = new StringBuffer();
		if(xmbh!=null&&!"".equals(xmbh)&&jdmc!=null&&!"".equals(jdmc)){
			if(jdmc.equals("CWXMLX")){
				
				Long xmlxInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_CWXMLX WHERE XMBH= ? ",params);
				if(xmlxInsId==0){
					jsonHtml.append("{\"instanceId\":"+xmlxInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(xmlxInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+xmlxInsId+"}");
				}
			}else if(jdmc.equals("CWGZJDHB")){
			
				Long fazlbsInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_CWGZJDHB WHERE XMBH= ? ",params);
				if(fazlbsInsId==0){
					jsonHtml.append("{\"instanceId\":"+fazlbsInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(fazlbsInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+fazlbsInsId+"}");
				}
			}else if(jdmc.equals("CWZLGD")){
				
				Long zlgdInsId = DBUTilNew.getLong("LCBS","SELECT LCBS FROM BD_ZQB_CWZLGD WHERE XMBH= ? ",params);
				if(zlgdInsId==0){
					jsonHtml.append("{\"instanceId\":"+zlgdInsId+"}");
				}else{
					HashMap<String,Object> processData = ProcessAPI.getInstance().getFromData(zlgdInsId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
					Long executionId = Long.parseLong(processData.get("LCBS").toString());
					Long taskid = Long.parseLong(processData.get("TASKID").toString());
					String actDefId = processData.get("LCBH").toString();
					jsonHtml.append("{\"executionId\":"+executionId+",\"taskid\":\""+taskid+"\",\"actDefId\":\""+actDefId+"\",\"instanceId\":"+zlgdInsId+"}");
				}
			}
		}
		return jsonHtml.toString();
	}
	private  HistoryService historyService;//历史服务	
	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public String getBackOtherList(String actDefId, Long prcDefId, String actStepId, String taskId, Long backType,Long instanceId)
	  {
		//查询扣分人配置信息      0：只显示董秘：1：显示各个节点办理人
		Map<String, String> config = ConfigUtil.readAllProperties(CN_FILENAME);// 获取连接网址配置
		String ggkfr = config.get("ggkfr");
		
		UserContext uci =  UserContextUtil.getInstance().getCurrentUserContext();
		//登录人
		String loginUser = uci._userModel.getUserid().toUpperCase();		
	    StringBuffer html = new StringBuffer();
	   // Task task = (Task)this.taskService.createTaskQuery().taskId(taskId).singleResult();
	    Task task= ProcessAPI.getInstance().getTask(taskId);
	    List<HistoricTaskInstance> list = null;
	    if (task != null) {
	     
	      HashMap data = new HashMap();
	      if(historyService==null){
	    	  historyService = (HistoryService) SpringBeanUtil.getBean("historyService");
		  }
	  	  //list = this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().processDefinitionId(actDefId).processInstanceId(task.getProcessInstanceId()).list();
	  	  list=historyService.createHistoricTaskInstanceQuery().processDefinitionId(actDefId).processInstanceId(task.getProcessInstanceId()).list();
	  	 	
	      ArrayList<String> alUsers = new ArrayList<String>();
	     
	      String strUserID = "";
	      //判断是否为重要督导事项流程
	      if(!actDefId.startsWith("ZYDDSXLC")){
		      //判断公告被扣分人配置 0：只给董秘扣分；1：正常扣分
		      if(!ggkfr.equals("0")){
			      for (HistoricTaskInstance historic : list) {
			        String lcUser = historic.getAssignee();	        
			        if(!alUsers.contains(lcUser) && !lcUser.equals(loginUser))
			        {
			        	strUserID += "'"+lcUser+"',";
			        	alUsers.add(lcUser);
			        }	          
			      }
		      }else{
			      HashMap processdate=ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
			      //当前登录人不等于被扣分人
			      if(!processdate.get("QCRID").toString().equals(loginUser)){
				      strUserID="'"+processdate.get("QCRID").toString()+"',";
				      alUsers.add(processdate.get("QCRID").toString());
			      }
		      }
	      }
		if(!alUsers.isEmpty())
		{
			 html.append("<select onchange=\"setParam(this)\" id=\"checkbkfr\">").append("\n");
			 html.append("<option value=\"\">--请选择--</option>\n");
			strUserID=strUserID.substring(0,strUserID.length()-1);
			List <String> userlist = new ArrayList();
			StringBuffer sql = new StringBuffer();
			sql.append("select userid,username from orguser where userid in("+strUserID+")");
			Connection conn = DBUtil.open();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql.toString());
				
				rs = ps.executeQuery();
				while (rs.next()) {				
					userlist.add(rs.getString("username")+"|"+rs.getString("userid"));
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, rs);
			}
			for (String user : userlist){
		      	html.append("<option value=\"").append(user).append("\">").append(user).append("</option>\n");
			      
			    }
			html.append("</select>").append("\n");
			}
	    }
	    return html.toString();
	  }
	//获取扣分类型
	public String getkflx() {
		List <HashMap> datalist = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select ID,KFLB from bd_xp_kflxb order by id");
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				result.put("ID", rs.getString("ID"));
				result.put("KFLB", rs.getString("KFLB"));
				datalist.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		StringBuffer html = new StringBuffer();
		html.append("<table>");
		int hs= datalist.size()/3;
		int gs= datalist.size()%3;
		if(gs!=0){
			hs=hs+1;
		}
		int hh=3-gs+1;
		int flag=0;
		for (int j = 0; j < hs; j++) {
			html.append("<tr>");
			for (int i = 0; i < 3; i++) {
				if(flag==datalist.size()){
					break;
				}else{
					if(flag==datalist.size()-1){
					/*	if(gs!=0){*/
							html.append("<td colspan='"+hh+"'>");
							html.append("<input name=\"Fruit\" type=\"checkbox\" value=\"").append(datalist.get(flag).get("ID")).append("\"/>").append(datalist.get(flag).get("KFLB"));
							html.append("<input type=\"text\" id=\"text\" style=\"width:100px;height:15px;\" maxlength=\"20\" name=\"fname\"/>");
							html.append("</td>");
						/*}*/
					}else{
						html.append("<td>");
						html.append("<input name=\"Fruit\" type=\"checkbox\" value=\"").append(datalist.get(flag).get("ID")).append("\"/>").append(datalist.get(flag).get("KFLB"));
						html.append("</td>");
					}
					
					flag++;
				}
				
			}
			html.append("</tr>");
			
		}
		
		html.append("</table>");
		
		//html.append("<input type=\"text\" id=\"text\" style=\"width:100px;height:15px;\" maxlength=\"20\" name=\"fname\"/>");
		//html.append("</br><label><input name=\"Fruit\" type=\"checkbox\" value=\"0\"/>").append("其他：<input type=\"text\" id=\"text\" style=\"width:100px;height:15px;\" maxlength=\"20\" name=\"fname\"/>").append(" </label>").append("\n");
		return html.toString();
	}
}
