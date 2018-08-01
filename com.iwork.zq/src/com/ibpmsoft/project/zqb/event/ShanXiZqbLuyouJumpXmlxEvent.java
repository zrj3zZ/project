package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.activiti.engine.task.Task;
import com.ibpmsoft.project.zqb.util.EventUtil;
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
public class ShanXiZqbLuyouJumpXmlxEvent extends SysJumpTriggerEvent {
	private static Logger logger = Logger.getLogger(ShanXiZqbLuyouJumpXmlxEvent.class);
	public ShanXiZqbLuyouJumpXmlxEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		String actStepId = this.getActStepId();
		Long instanceId = this.getInstanceId();
		HashMap<String,String> spry = getSpry(instanceId);
		if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd1())){
			if(!spry.get("NODE1").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE1").toString())){
				return SystemConfig._xmlxSpLcConf.getJd2();
			}else if(!spry.get("NODE2").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE2").toString())){
				return SystemConfig._xmlxSpLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd5();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd6();
			}else{
				return SystemConfig._xmlxSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE2").toString())){
				return SystemConfig._xmlxSpLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd5();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd6();
			}else{
				return SystemConfig._xmlxSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd3())){
			if(!spry.get("NODE2").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE2").toString())){
				return SystemConfig._xmlxSpLcConf.getJd4();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd5();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd6();
			}else{
				return SystemConfig._xmlxSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd4())){
			if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd5();
			}else{
				return SystemConfig._xmlxSpLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd5())){
			if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmlxSpLcConf.getJd6();
			}else{
				return SystemConfig._xmlxSpLcConf.getEnd();
			}
		}else{
			return SystemConfig._xmlxSpLcConf.getEnd();
		}
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		Long instanceId = this.getInstanceId();
		String actStepId = this.getActStepId();
		HashMap<String,String> spry = getSpry(instanceId);
		if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd1())){
			if(!spry.get("NODE1").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE1").toString())){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE1").toString().substring(0,spry.get("NODE1").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE2").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE2").toString())){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE2").toString())){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd3())){
			if(!spry.get("NODE2").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE2").toString())){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd4())){
			if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmlxSpLcConf.getJd5())){
			if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}
		}
		 return uclist;
	}
	
	public HashMap<String, String> getSpry(Long instanceId) {
		StringBuffer sql=new StringBuffer("SELECT "
				+ "(SELECT OWNER FROM BD_ZQB_PJ_BASE WHERE LCBS=?) AS NODE1,"
				+ "NODE1 AS NODE2,"
				+ "NODE2 AS NODE3,"
				+ "NODE3 AS NODE4,"
				
				+ "EXTENDSFSP,"
				+ "NODE1SFSP AS NODE2SFSP,"
				+ "NODE2SFSP AS NODE3SFSP,"
				+ "NODE3SFSP AS NODE4SFSP FROM BD_ZQB_SXSPRYSZ WHERE LCMC='项目立项' AND XMLX='推荐挂牌项目'");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long extendsfsp = rs.getLong("EXTENDSFSP");
				Long node2sfsp = rs.getLong("NODE2SFSP");
				Long node3sfsp = rs.getLong("NODE3SFSP");
				Long node4sfsp = rs.getLong("NODE4SFSP");
				String node1 = "";
				String node2 = "";
				String node3 = "";
				String node4 = "";
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node1=rs.getString("NODE1")==null?"":rs.getString("NODE1");
				}
				if(node2sfsp!=null&&node2sfsp.toString().equals("1")){
					node2=rs.getString("NODE2")==null?"":rs.getString("NODE2");
				}
				if(node3sfsp!=null&&node3sfsp.toString().equals("1")){
					node3=rs.getString("NODE3")==null?"":rs.getString("NODE3");
				}
				if(node4sfsp!=null&&node4sfsp.toString().equals("1")){
					node4=rs.getString("NODE4")==null?"":rs.getString("NODE4");
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

