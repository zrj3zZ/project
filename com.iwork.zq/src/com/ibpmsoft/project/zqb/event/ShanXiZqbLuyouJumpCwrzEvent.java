package com.ibpmsoft.project.zqb.event;
import org.apache.log4j.Logger;
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
/**
 * @author 
 * 
 */
public class ShanXiZqbLuyouJumpCwrzEvent extends SysJumpTriggerEvent {
	private static Logger logger = Logger.getLogger(ShanXiZqbLuyouJumpCwrzEvent.class);
	public ShanXiZqbLuyouJumpCwrzEvent(UserContext me, Task task) {
		super(me, task);
	}
	
	/**
	 * 获得下一个办理节点
	 */
	public String getNextStepId(){
		String actStepId = this.getActStepId();
		Long instanceId = this.getInstanceId();
		HashMap<String,String> spry = getSpry(instanceId);
		
		if(actStepId.equals(SystemConfig._cwrzsplcConf.getJd1())){		
			if(!spry.get("NODE1").toString().equals("")){
				return SystemConfig._cwrzsplcConf.getJd2();
			}else{
				return SystemConfig._cwrzsplcConf.getEnd();
			}
		}else{
			return SystemConfig._cwrzsplcConf.getEnd();
		}
	}
	
	public  List<UserContext> getNextUserList(){
		List<UserContext> uclist = new ArrayList<UserContext>();//返回用户
		Long instanceId = this.getInstanceId();
		String actStepId = this.getActStepId();
		HashMap<String,String> spry = getSpry(instanceId);
		if(actStepId.equals(SystemConfig._cwrzsplcConf.getJd1())){
			if(!spry.get("NODE1").toString().equals("")){
				UserContext uc = UserContextUtil.getInstance().getUserContext(spry.get("NODE1").toString().substring(0,spry.get("NODE1").toString().indexOf("[")));
				uclist.add(uc);				
			}
		}
		return uclist;
	}
	
	public HashMap<String, String> getSpry(Long instanceId) {
		StringBuffer sql=new StringBuffer("SELECT NODE1 AS NODE1,NODE1SFSP AS NODE1SFSP FROM BD_ZQB_SXSPRYSZ WHERE LCMC='财务入账' AND XMLX='财务入账项目'");
		HashMap<String,String> result = new HashMap<String,String>();
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				Long node1sfsp = rs.getLong("NODE1SFSP");
				String node1 = "";
				if(node1sfsp!=null&&node1sfsp.toString().equals("1")){
					node1=rs.getString("NODE1")==null?"":rs.getString("NODE1");
				}
				result.put("NODE1", node1);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
}

