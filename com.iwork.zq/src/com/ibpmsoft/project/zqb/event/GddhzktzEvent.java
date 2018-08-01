package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.process.runtime.service.ProcessRuntimeExcuteService;
import com.iwork.sdk.MessageAPI;

public class GddhzktzEvent implements IWorkScheduleInterface{
	  private static final Log log = LogFactory.getLog(GddhzktzEvent.class);

	@Override
	public boolean executeBefore() throws ScheduleException {
		return true;
	}

	@Override
	public boolean executeOn() throws ScheduleException {
		
		SendMsg();
		return true;
	}

	@Override
	public boolean executeAfter() throws ScheduleException {
		return true;
	}
	private void SendMsg() {
		try {
			Map<String,String> config=ConfigUtil.readAllProperties("/common.properties");
			String zqServer = config.get("zqServer")==null?"":config.get("zqServer").toString();
			List lables = new ArrayList();
			lables.add("id");
			lables.add("noticeno");
			lables.add("noticename");
			lables.add("zqdmxs");
			lables.add("zqjcxs");
			lables.add("khbh");
			
			StringBuffer sb = new StringBuffer();
			if(zqServer.equals("dgzq") || zqServer.equals("sxzq")){
				sb.append(" select s.id,s.noticeno,s.noticename,s.zqdmxs,s.zqjcxs,s.khbh from bd_meet_qtggzl s where s.spzt='审批通过' and (s.noticeflag=0 or s.noticeflag is null) and TO_DATE(s.companyno,'yyyy-MM-dd')-1=TO_DATE(?,'yyyy-MM-dd')  ");
			}else{			
				sb.append(" select s.id,s.noticeno,s.noticename,s.zqdmxs,s.zqjcxs,s.khbh from bd_meet_qtggzl s where s.spzt='审批通过' and  (s.noticeflag=0 or s.noticeflag is null) and TO_DATE(s.companyno,'yyyy-MM-dd')=TO_DATE(?,'yyyy-MM-dd')  ");
			}
			
			
			Map params = new HashMap();
			params.put(1, UtilDate.getNowdate());
			List<HashMap> targetmsg = DBUtil.getDataList(lables, sb.toString(), params);
			
			List lables1 = new ArrayList();
			List lables2 = new ArrayList();
			
			String sql1 = "";
			
			if(!zqServer.equals("sxzq")){
				if(zqServer.equals("dgzq")){
					sql1 = "SELECT T.USERID,T.USERNAME,T.MOBILE FROM ORGUSER T WHERE to_char(enddate,'yyyy-MM-dd')>to_char(sysdate,'yyyy-MM-dd') and userstate=0 and T.USERID IN (SELECT FG FROM (SELECT SUBSTR(S.KHFZR, 1, INSTR(S.KHFZR, '[', 1, 1) - 1) FG,S.KHBH FROM BD_MDM_KHQXGLB S UNION SELECT SUBSTR(S.FHSPR, 1, INSTR(S.FHSPR, '[', 1, 1) - 1) FG,S.KHBH FROM BD_MDM_KHQXGLB S) WHERE KHBH = ?)";
				}else{
					sql1 = "SELECT T.USERID,T.USERNAME,T.MOBILE FROM ORGUSER T WHERE to_char(enddate,'yyyy-MM-dd')>to_char(sysdate,'yyyy-MM-dd') and userstate=0 and T.USERID IN (SELECT SUBSTR(S.KHFZR, 1, INSTR(S.KHFZR, '[', 1, 1) - 1) FG FROM BD_MDM_KHQXGLB S WHERE  S.KHBH= ?)";
				}
				
			}else{
				sql1 = "SELECT T.USERID,T.USERNAME,T.MOBILE FROM ORGUSER T WHERE to_char(enddate,'yyyy-MM-dd')>to_char(sysdate,'yyyy-MM-dd') and userstate=0 and T.USERID IN (SELECT FG FROM (SELECT SUBSTR(S.ZZCXDD, 1, INSTR(S.ZZCXDD, '[', 1, 1) - 1) FG,S.KHBH FROM BD_MDM_KHQXGLB S UNION SELECT SUBSTR(S.FHSPR, 1, INSTR(S.FHSPR, '[', 1, 1) - 1) FG,S.KHBH FROM BD_MDM_KHQXGLB S) WHERE KHBH = ?)";
			}
			
			Map params1 = new HashMap();
			Map params2 = new HashMap();
			for (HashMap data : targetmsg) {
				String khbh = data.get("khbh").toString();
				lables1 = new ArrayList();
				lables1.add("userid");
				lables1.add("username");
				lables1.add("mobile");
				params1 = new HashMap();
				params1.put(1, khbh);
				List<HashMap> ddxx = DBUtil.getDataList(lables1, sql1, params1);
				String mobile="";
				if(ddxx.size()>0){
					//xlj update 2018年5月31日11:19:52 发送给多个督导
					for(int i=0;i<ddxx.size();i++)
					{
						mobile=ddxx.get(i).get("mobile")==null?"":ddxx.get(i).get("mobile").toString();				
						if(mobile!=null &&  !"".equals(mobile)){
							String dxnn=data.get("zqdmxs").toString()+data.get("zqjcxs").toString()+"在"+data.get("noticename").toString()+"公告中填写的股东大会召开时间已到，请知悉。";
							MessageAPI.getInstance().sendSMS(mobile.toString(),dxnn);
						}
					}
				}
				lables2 = new ArrayList();
				lables2.add("mobile");
				params2 = new HashMap();
				params2.put(1, khbh);				
				//xlj update 2018年5月31日11:32:10 增加用户有效性判断
				List<HashMap> dmxx = DBUtil.getDataList(lables2, " select s.mobile from orguser s where s.extend1= ? and to_char(enddate,'yyyy-MM-dd')>to_char(sysdate,'yyyy-MM-dd') and userstate=0 order by id desc  ", params2);
				String dmmobile="";
				if(dmxx.size()>0){
					dmmobile=dmxx.get(0).get("mobile")==null?"":dmxx.get(0).get("mobile").toString();
				}
				if(dmmobile!=null &&  !"".equals(dmmobile)){
					String dxnn="您在"+data.get("noticename").toString()+"公告中填写的股东大会召开时间已到，请知悉。";
					MessageAPI.getInstance().sendSMS(dmmobile.toString(),dxnn);
				}
				Map params3 = new HashMap();
				params3.put(1, data.get("id").toString());
				String sql = "update bd_meet_qtggzl set noticeflag=1 where id =? ";
				int i = DBUTilNew.update(sql,params3);
			}
		} catch (Exception e) {
			log.error(e);
		}
		
		
	}

}
