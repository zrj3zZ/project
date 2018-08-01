package com.ibpmsoft.project.zqb.event;

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
/**股改流程第一步
 * @author 
 * 
 */
public class ShanXiZqbLuyouJumpBgZlgdEvent extends SysJumpTriggerEvent {
	private static Logger logger = Logger.getLogger(ShanXiZqbLuyouJumpBgZlgdEvent.class);
	public ShanXiZqbLuyouJumpBgZlgdEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		String actStepId = this.getActStepId();
		Long instanceId = this.getInstanceId();
		HashMap<String,String> spry = getSpry(instanceId);
		if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd1())){
			if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd1())){
				return SystemConfig._bgZlgdSpLcConf.getJd2();
			}else if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd5();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd6();
			}else{
				return SystemConfig._bgZlgdSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd5();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd6();
			}else{
				return SystemConfig._bgZlgdSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd3())){
			if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd5();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd6();
			}else{
				return SystemConfig._bgZlgdSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd4())){
			if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd5();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd6();
			}else{
				return SystemConfig._bgZlgdSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd5())){
			if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._bgZlgdSpLcConf.getJd6();
			}else{
				return SystemConfig._bgZlgdSpLcConf.getEnd();
			}
		}else{
			return SystemConfig._bgZlgdSpLcConf.getEnd();
		}
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		Long instanceId = this.getInstanceId();
		String actStepId = this.getActStepId();
		HashMap<String,String> spry = getSpry(instanceId);
		if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd1())){
			if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd1())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE2").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd3())){
			if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd4())){
			if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._bgZlgdSpLcConf.getJd5())){
			if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}
		}
		return uclist;
	}
	
	public HashMap<String, String> getSpry(Long instanceId) {
		StringBuffer sql=new StringBuffer("SELECT "
				+ "(SELECT BGZLGD.EXTEND1 FROM BD_ZQB_BGZLGD BGZLGD INNER JOIN BD_ZQB_BGZZLXXX BGCZ ON BGZLGD.XMBH=BGCZ.XMBH WHERE BGZLGD.LCBS = ?) AS NODE1,"
				+ "(SELECT BGCZ.MANAGER FROM BD_ZQB_BGZLGD BGZLGD INNER JOIN BD_ZQB_BGZZLXXX BGCZ ON BGZLGD.XMBH=BGCZ.XMBH WHERE BGZLGD.LCBS = ?) AS NODE2,"
				+ "(SELECT BGCZ.OWNER FROM BD_ZQB_BGZLGD BGZLGD INNER JOIN BD_ZQB_BGZZLXXX BGCZ ON BGZLGD.XMBH=BGCZ.XMBH WHERE BGZLGD.LCBS = ?) AS NODE3,"
				+ "NODE1 AS NODE4,"
				+ "NODE2 AS NODE5,"
				
				+ "EXTENDSFSP,"
				+ "NODE1SFSP AS NODE4SFSP,"
				+ "NODE2SFSP AS NODE5SFSP FROM BD_ZQB_SXSPRYSZ WHERE LCMC='资料归档' AND XMLX='并购项目'");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceId);
			ps.setLong(2, instanceId);
			ps.setLong(3, instanceId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long extendsfsp = rs.getLong("EXTENDSFSP");
				Long node4sfsp = rs.getLong("NODE4SFSP");
				Long node5sfsp = rs.getLong("NODE5SFSP");
				String node1 = "";
				String node2 = "";
				String node3 = "";
				String node4 = "";
				String node5 = "";
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node1=rs.getString("NODE1")==null?"":rs.getString("NODE1");
				}
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node2=rs.getString("NODE2")==null?"":rs.getString("NODE2");
				}
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node3=rs.getString("NODE3")==null?"":rs.getString("NODE3");
				}
				if(node4sfsp!=null&&node4sfsp.toString().equals("1")){
					node4=rs.getString("NODE4")==null?"":rs.getString("NODE4");
				}
				if(node5sfsp!=null&&node5sfsp.toString().equals("1")){
					node5=rs.getString("NODE5")==null?"":rs.getString("NODE5");
				}
				result.put("NODE1", node1);
				result.put("NODE2", node2);
				result.put("NODE3", node3);
				result.put("NODE4", node4);
				result.put("NODE5", node5);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
}

