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
/**项目内核流程第一步
 * @author 
 * 
 */
public class ShanXiZqbLuyouJumpXmnhEvent extends SysJumpTriggerEvent {
	private static Logger logger = Logger.getLogger(ShanXiZqbLuyouJumpXmnhEvent.class);
	public ShanXiZqbLuyouJumpXmnhEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		HashMap<String,String> spry = getSpry(this.getInstanceId());
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._xmnhLcConf.getJd1())){
			if(!spry.get("NODE1").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd2();
			}else if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd6();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd7();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd8();
			}else{
				return SystemConfig._xmnhLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd3();
			}else if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd6();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd7();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd8();
			}else{
				return SystemConfig._xmnhLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd3())){
			if(!spry.get("NODE3").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd6();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd7();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd8();
			}else{
				return SystemConfig._xmnhLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd4())){
			if(!spry.get("NODE3").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE3").toString())){
				return SystemConfig._xmnhLcConf.getJd5();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd6();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd7();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd8();
			}else{
				return SystemConfig._xmnhLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd5())){
			if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd6();
			}else if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd7();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd8();
			}else{
				return SystemConfig._xmnhLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd6())){
			if(!spry.get("NODE5").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd7();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._xmnhLcConf.getJd8();
			}else{
				return SystemConfig._xmnhLcConf.getEnd();
			}
		}else if(!spry.get("NODE6").toString().equals("")&&actStepId.equals(SystemConfig._xmnhLcConf.getJd7())){
			return SystemConfig._xmnhLcConf.getJd8();
		}else{
			return SystemConfig._xmnhLcConf.getEnd();
		}
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		HashMap<String,String> spry = getSpry(this.getInstanceId());
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._xmnhLcConf.getJd1())){
			if(!spry.get("NODE1").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE1").toString().substring(0,spry.get("NODE1").toString().indexOf("[")));
				uclist.add(uc);
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
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd2())){
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
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd3())){
			if(!spry.get("NODE3").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd4())){
			if(!spry.get("NODE3").toString().equals("")&&!EventUtil.checkUser(spry.get("NODE3").toString())){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE3").toString().substring(0,spry.get("NODE3").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd5())){
			if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._xmnhLcConf.getJd6())){
			if(!spry.get("NODE5").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE5").toString().substring(0,spry.get("NODE5").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(!spry.get("NODE6").toString().equals("")&&actStepId.equals(SystemConfig._xmnhLcConf.getJd7())){
			UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
			uclist.add(uc);
		}
		 return uclist;
	}
	
	public HashMap<String, String> getSpry(Long instanceid) {
		StringBuffer sql=new StringBuffer("SELECT "
				+ "(SELECT EXTEND1 FROM BD_ZQB_SQNH WHERE LCBS=?) AS NODE1,"
				+ "(SELECT PJ.OWNER FROM BD_ZQB_SQNH SQNH INNER JOIN BD_ZQB_PJ_BASE PJ ON SQNH.CUSTOMERNO=PJ.CUSTOMERNO WHERE SQNH.LCBS=?) AS NODE2,"
				+ "NODE1 AS NODE3,"
				+ "(SELECT A.NHZY FROM BD_ZQB_LCGG A LEFT JOIN BD_ZQB_SQNH B ON A.CUSTOMERNO=B.CUSTOMERNO WHERE B.LCBS=?) AS NODE4,"
				+ "NODE2 AS NODE5,"
				+ "NODE3 AS NODE6,"
				
				+ "EXTENDSFSP,"
				+ "NODE1SFSP AS NODE3SFSP,"
				+ "NODE2SFSP AS NODE5SFSP,"
				+ "NODE3SFSP AS NODE6SFSP FROM BD_ZQB_SXSPRYSZ WHERE LCMC='申报审核' AND XMLX='推荐挂牌项目'");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceid);
			ps.setLong(2, instanceid);
			ps.setLong(3, instanceid);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long extendsfsp = rs.getLong("EXTENDSFSP");
				Long node3sfsp = rs.getLong("NODE3SFSP");
				Long node5sfsp = rs.getLong("NODE5SFSP");
				Long node6sfsp = rs.getLong("NODE6SFSP");
				String node1 = "";
				String node2 = "";
				String node3 = "";
				String node4 = "";
				String node5 = "";
				String node6 = "";
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node1=rs.getString("NODE1")==null?"":rs.getString("NODE1");
				}
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node2=rs.getString("NODE2")==null?"":rs.getString("NODE2");
				}
				if(node3sfsp!=null&&node3sfsp.toString().equals("1")){
					node3=rs.getString("NODE3")==null?"":rs.getString("NODE3");
				}
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node4=rs.getString("NODE4")==null?"":rs.getString("NODE4");
				}
				if(node5sfsp!=null&&node5sfsp.toString().equals("1")){
					node5=rs.getString("NODE5")==null?"":rs.getString("NODE5");
				}
				if(node6sfsp!=null&&node6sfsp.toString().equals("1")){
					node6=rs.getString("NODE6")==null?"":rs.getString("NODE6");
				}
				result.put("NODE1", node1);
				result.put("NODE2", node2);
				result.put("NODE3", node3);
				result.put("NODE4", node4);
				result.put("NODE5", node5);
				result.put("NODE6", node6);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

}

