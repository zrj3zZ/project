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
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.runtime.pvm.impl.system.SysJumpTriggerEvent;
import org.apache.log4j.Logger;
/**股改流程第一步
 * @author 
 * 
 */
public class ShanXiZqbLuyouJumpGuGaiEvent extends SysJumpTriggerEvent {
	private static Logger logger = Logger.getLogger(ShanXiZqbLuyouJumpGuGaiEvent.class);
	public ShanXiZqbLuyouJumpGuGaiEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		HashMap<String,String> spry = getSpry(this.getInstanceId());
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._gugaiLcConf.getJd1())){
			if(!spry.get("NODE1").toString().equals("")){
				return SystemConfig._gugaiLcConf.getJd2();
			}else if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._gugaiLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._gugaiLcConf.getJd5();
			}else{
				return SystemConfig._gugaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._gugaiLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._gugaiLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._gugaiLcConf.getJd5();
			}else{
				return SystemConfig._gugaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._gugaiLcConf.getJd3())){
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			if(!spry.get("NODE2").toString().equals("")&&!userid.equals(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")))){
				return SystemConfig._gugaiLcConf.getJd4();
			}else if(!spry.get("NODE3").toString().equals("")&&!spry.get("NODE2").toString().equals("")&&userid.equals(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")))){
				return SystemConfig._gugaiLcConf.getJd5();
			}else{
				return SystemConfig._gugaiLcConf.getEnd();
			}
		}else if(!spry.get("NODE3").toString().equals("")&&actStepId.equals(SystemConfig._gugaiLcConf.getJd4())){
			return SystemConfig._gugaiLcConf.getJd5();
		}else{
			return SystemConfig._gugaiLcConf.getEnd();
		}
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		HashMap<String,String> spry = getSpry(this.getInstanceId());
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._gugaiLcConf.getJd1())){
			if(!spry.get("NODE1").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE1").toString().substring(0,spry.get("NODE1").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE2").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._gugaiLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._gugaiLcConf.getJd3())){
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			if(!spry.get("NODE2").toString().equals("")&&!userid.equals(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")))){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")&&!spry.get("NODE2").toString().equals("")&&userid.equals(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")))){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(!spry.get("NODE3").toString().equals("")&&actStepId.equals(SystemConfig._gugaiLcConf.getJd4())){
			UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
			uclist.add(uc);
		}
		 return uclist;
	}
	
	public HashMap<String, String> getSpry(Long instanceid) {
		StringBuffer sql=new StringBuffer("SELECT "
				+ "(SELECT PJ.OWNER FROM BD_ZQB_LCGG LCGG INNER JOIN BD_ZQB_PJ_BASE PJ ON LCGG.CUSTOMERNO=PJ.CUSTOMERNO WHERE LCGG.LCBS= ?) AS NODE1,"
				+ "NODE1 AS NODE2,"
				+ "NODE2 AS NODE3,"
				
				+ "EXTENDSFSP,"
				+ "NODE1SFSP AS NODE2SFSP,"
				+ "NODE2SFSP AS NODE3SFSP FROM BD_ZQB_SXSPRYSZ WHERE LCMC='股改' AND XMLX='推荐挂牌项目'");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceid);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long extendsfsp = rs.getLong("EXTENDSFSP");
				Long node2sfsp = rs.getLong("NODE2SFSP");
				Long node3sfsp = rs.getLong("NODE3SFSP");
				String node1 = "";
				String node2 = "";
				String node3 = "";
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node1=rs.getString("NODE1")==null?"":rs.getString("NODE1");
				}
				if(node2sfsp!=null&&node2sfsp.toString().equals("1")){
					node2=rs.getString("NODE2")==null?"":rs.getString("NODE2");
				}
				if(node3sfsp!=null&&node3sfsp.toString().equals("1")){
					node3=rs.getString("NODE3")==null?"":rs.getString("NODE3");
				}
				result.put("NODE1", node1);
				result.put("NODE2", node2);
				result.put("NODE3", node3);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

}

