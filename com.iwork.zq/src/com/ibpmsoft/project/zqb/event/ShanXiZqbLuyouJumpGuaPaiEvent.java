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
/**项目内核流程第一步
 * @author 
 * 
 */
public class ShanXiZqbLuyouJumpGuaPaiEvent extends SysJumpTriggerEvent {
	private static Logger logger = Logger.getLogger(ShanXiZqbLuyouJumpGuaPaiEvent.class);
	public ShanXiZqbLuyouJumpGuaPaiEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		HashMap<String,String> spry = getSpry(this.getInstanceId());
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._guapaiLcConf.getJd1())){
			if(actStepId.equals(SystemConfig._guapaiLcConf.getJd1())){
				return SystemConfig._guapaiLcConf.getJd2();
			}else if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd3();
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
				return SystemConfig._guapaiLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd5();
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				return SystemConfig._guapaiLcConf.getJd6();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd7();
			}else if(!spry.get("NODE7").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd8();
			}else if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd3();
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
				return SystemConfig._guapaiLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd5();
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				return SystemConfig._guapaiLcConf.getJd6();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd7();
			}else if(!spry.get("NODE7").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd8();
			}else if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
			if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
				return SystemConfig._guapaiLcConf.getJd4();
			}else if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd5();
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				return SystemConfig._guapaiLcConf.getJd6();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd7();
			}else if(!spry.get("NODE7").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd8();
			}else if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd4())){
			if(!spry.get("NODE4").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd5();
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				return SystemConfig._guapaiLcConf.getJd6();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd7();
			}else if(!spry.get("NODE7").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd8();
			}else if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
			if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				return SystemConfig._guapaiLcConf.getJd6();
			}else if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd7();
			}else if(!spry.get("NODE7").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd8();
			}else if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd6())){
			if(!spry.get("NODE6").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd7();
			}else if(!spry.get("NODE7").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd8();
			}else if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd7())){
			if(!spry.get("NODE7").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd8();
			}else if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd8())){
			if(!spry.get("NODE8").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd9();
			}else if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd9())){
			if(!spry.get("NODE9").toString().equals("")){
				return SystemConfig._guapaiLcConf.getJd10();
			}else{
				return SystemConfig._guapaiLcConf.getEnd();
			}
		}else{
			return SystemConfig._guapaiLcConf.getEnd();
		}
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		HashMap<String,String> spry = getSpry(this.getInstanceId());
		String actStepId = this.getActStepId();
		if(actStepId.equals(SystemConfig._guapaiLcConf.getJd1())){
			if(actStepId.equals(SystemConfig._guapaiLcConf.getJd1())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE2").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE7").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE7").toString().substring(0,spry.get("NODE7").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd2())){
			if(!spry.get("NODE2").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE2").toString().substring(0,spry.get("NODE2").toString().indexOf("[")));
				uclist.add(uc);
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE7").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE7").toString().substring(0,spry.get("NODE7").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
			if(actStepId.equals(SystemConfig._guapaiLcConf.getJd3())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE7").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE7").toString().substring(0,spry.get("NODE7").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd4())){
			if(!spry.get("NODE4").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE4").toString().substring(0,spry.get("NODE4").toString().indexOf("[")));
				uclist.add(uc);
			}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE7").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE7").toString().substring(0,spry.get("NODE7").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
			if(actStepId.equals(SystemConfig._guapaiLcConf.getJd5())){
				HashMap<String, Object> dataMap = ProcessAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
				Object bmpuserid = dataMap.get("EXTEND1");
				if(bmpuserid!=null&&!"".equals(bmpuserid)){
					UserContext uc = UserContextUtil.getInstance().getUserContext(bmpuserid.toString());
					uclist.add(uc);
				}
			}else if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE7").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE7").toString().substring(0,spry.get("NODE7").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd6())){
			if(!spry.get("NODE6").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE6").toString().substring(0,spry.get("NODE6").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE7").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE7").toString().substring(0,spry.get("NODE7").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd7())){
			if(!spry.get("NODE7").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE7").toString().substring(0,spry.get("NODE7").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd8())){
			if(!spry.get("NODE8").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE8").toString().substring(0,spry.get("NODE8").toString().indexOf("[")));
				uclist.add(uc);
			}else if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}else if(actStepId.equals(SystemConfig._guapaiLcConf.getJd9())){
			if(!spry.get("NODE9").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE9").toString().substring(0,spry.get("NODE9").toString().indexOf("[")));
				uclist.add(uc);
			}
		}
		return uclist;
	}
	
	public HashMap<String, String> getSpry(Long instanceid) {
		 StringBuffer sql=new StringBuffer("SELECT ");
								//sql.append("NODE1 AS NODE1, ");///////////////////
								sql.append("(SELECT GP.XMFZR FROM BD_ZQB_GPDJJGD GP INNER JOIN BD_ZQB_PJ_BASE PJ ON GP.CUSTOMERNO=PJ.CUSTOMERNO WHERE GP.LCBS=?) AS NODE2, "); 
								//sql.append("NODE1 AS NODE3, ");///////////////////
								sql.append("(SELECT GP.XMFZR FROM BD_ZQB_GPDJJGD GP INNER JOIN BD_ZQB_PJ_BASE PJ ON GP.CUSTOMERNO=PJ.CUSTOMERNO WHERE GP.LCBS=?) AS NODE4, "); 
								//sql.append("NODE1 AS NODE5, ");///////////////////
								sql.append("(SELECT GP.XMFZR FROM BD_ZQB_GPDJJGD GP INNER JOIN BD_ZQB_PJ_BASE PJ ON GP.CUSTOMERNO=PJ.CUSTOMERNO WHERE GP.LCBS=?) AS NODE6, "); 
								sql.append("(SELECT PJ.OWNER FROM BD_ZQB_GPDJJGD GP INNER JOIN BD_ZQB_PJ_BASE PJ ON GP.CUSTOMERNO=PJ.CUSTOMERNO WHERE GP.LCBS=?) AS NODE7, ");
								sql.append("NODE1 AS NODE8, ");
								sql.append("NODE2 AS NODE9, ");
								
								//sql.append("NODE1SFSP AS NODE1SFSP, ");/////////////////
								//sql.append("NODE1SFSP AS NODE3SFSP, ");////////////////
								//sql.append("NODE1SFSP AS NODE5SFSP, ");//////////////////
								sql.append("NODE1SFSP AS NODE8SFSP, ");
								sql.append("NODE2SFSP AS NODE9SFSP, ");
								sql.append("EXTENDSFSP FROM BD_ZQB_SXSPRYSZ WHERE LCMC='挂牌登记及归档' AND XMLX='推荐挂牌项目'");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceid);
			ps.setLong(2, instanceid);
			ps.setLong(3, instanceid);
			ps.setLong(4, instanceid);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long extendsfsp = rs.getLong("EXTENDSFSP");
				//Long node1sfsp = rs.getLong("NODE1SFSP");
				//Long node3sfsp = rs.getLong("NODE3SFSP");
				//Long node5sfsp = rs.getLong("NODE5SFSP");
				Long node8sfsp = rs.getLong("NODE8SFSP");
				Long node9sfsp = rs.getLong("NODE9SFSP");
				String node1 = "";
				String node2 = "";
				String node3 = "";
				String node4 = "";
				String node5 = "";
				String node6 = "";
				String node7 = "";
				String node8 = "";
				String node9 = "";
				/*if(node1sfsp!=null&&node1sfsp.toString().equals("1")){
					node1=rs.getString("NODE1")==null?"":rs.getString("NODE1");
				}*/
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node2=rs.getString("NODE2")==null?"":rs.getString("NODE2");
				}
				/*if(node3sfsp!=null&&node3sfsp.toString().equals("1")){
					node3=rs.getString("NODE3")==null?"":rs.getString("NODE3");
				}*/
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node4=rs.getString("NODE4")==null?"":rs.getString("NODE4");
				}
				/*if(node5sfsp!=null&&node5sfsp.toString().equals("1")){
					node5=rs.getString("NODE5")==null?"":rs.getString("NODE5");
				}*/
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node6=rs.getString("NODE6")==null?"":rs.getString("NODE6");
				}
				if(extendsfsp!=null&&extendsfsp.toString().equals("1")){
					node7=rs.getString("NODE7")==null?"":rs.getString("NODE7");
				}
				if(node8sfsp!=null&&node8sfsp.toString().equals("1")){
					node8=rs.getString("NODE8")==null?"":rs.getString("NODE8");
				}
				if(node9sfsp!=null&&node9sfsp.toString().equals("1")){
					node9=rs.getString("NODE9")==null?"":rs.getString("NODE9");
				}
				result.put("NODE1", node1);
				result.put("NODE2", node2);
				result.put("NODE3", node3);
				result.put("NODE4", node4);
				result.put("NODE5", node5);
				result.put("NODE6", node6);
				result.put("NODE7", node7);
				result.put("NODE8", node8);
				result.put("NODE9", node9);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

}

