package com.ibpmsoft.project.zqb.event.sxgpfx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.task.Task;

import com.iwork.app.conf.SystemConfig;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import com.iwork.sdk.ProcessAPI;
import org.apache.log4j.Logger;
/**拟发行流程
 * @author 
 * 
 */
public class ZqbSXGpfxfazlbsLuyouJumpEvent extends SysJumpTriggerEvent {
	private static Logger logger = Logger.getLogger(ZqbSXGpfxfazlbsLuyouJumpEvent.class);
	public ZqbSXGpfxfazlbsLuyouJumpEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		String actStepId = this.getActStepId();
		Long instanceid = this.getInstanceId();
		HashMap<String, Object> map = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String projectno = map.get("PROJECTNO").toString();
		HashMap<String,String> spry = getSpry(projectno);
		if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd1())){
			if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd2();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd5();
			}else{
				return SystemConfig._fazlbsLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd2())){
			if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd5();
			}else{
				return SystemConfig._fazlbsLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd3())){
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			if(!spry.get("NODE3").toString().equals("")&&!userid.equals(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")))){
				return SystemConfig._fazlbsLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")&&!spry.get("NODE3").toString().equals("")&&userid.equals(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")))){
				return SystemConfig._fazlbsLcConf.getJd5();
			}else{
				return SystemConfig._fazlbsLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd4())){
			if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._fazlbsLcConf.getJd5();
			}else{
				return SystemConfig._fazlbsLcConf.getEnd();
			}
		}else{
			return SystemConfig._fazlbsLcConf.getEnd();
		}
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		String actStepId = this.getActStepId();
		Long instanceid = this.getInstanceId();
		HashMap<String, Object> map = ProcessAPI.getInstance().getFromData(instanceid,EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String projectno = map.get("PROJECTNO").toString();
		HashMap<String,String> spry = getSpry(projectno);
		
		if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd1())){
			if(!spry.get("NODE2").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else{
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd2())){
			if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else{
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd3())){
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			if(!spry.get("NODE3").toString().equals("")&&!userid.equals(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")))){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")&&!spry.get("NODE3").toString().equals("")&&userid.equals(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")))){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._fazlbsLcConf.getJd4())){
			if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}
		 return uclist;
	}
	
	public HashMap<String, String> getSpry(String projectno) {
		StringBuffer sql=new StringBuffer("SELECT (SELECT MANAGER FROM BD_ZQB_GPFXXMB WHERE PROJECTNO=?) AS NODE1,(SELECT OWNER FROM BD_ZQB_GPFXXMB WHERE PROJECTNO=?) AS NODE2,NODE1 AS NODE3,NODE2 AS NODE4,EXTENDSFSP,NODE1SFSP AS NODE3SFSP,NODE2SFSP AS NODE4SFSP FROM BD_ZQB_SXSPRYSZ WHERE LCMC='方案资料报审' AND XMLX='股票发行项目'");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			for(int i = 1;i <= 2;i++){
				ps.setString(i, projectno);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Long extendsfsp = rs.getLong("EXTENDSFSP");
				Long node3sfsp = rs.getLong("NODE3SFSP");
				Long node4sfsp = rs.getLong("NODE4SFSP");
				String node1 = "";
				String node2 = "";
				String node3 = "";
				String node4 = "";
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node1 = rs.getString("NODE1")==null?"":rs.getString("NODE1");
				}
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node2 = rs.getString("NODE2")==null?"":rs.getString("NODE2");
				}
				if(node3sfsp!=null&&node3sfsp.toString().equals("1")){
					node3 = rs.getString("NODE3")==null?"":rs.getString("NODE3");
				}
				if(node4sfsp!=null&&node4sfsp.toString().equals("1")){
					node4 = rs.getString("NODE4")==null?"":rs.getString("NODE4");
				}
				result.put("NODE1", node1);
				result.put("NODE2", node2);
				result.put("NODE3", node3);
				result.put("NODE4", node4);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

}

