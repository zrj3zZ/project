package com.ibpmsoft.project.zqb.trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import org.apache.log4j.Logger;
/**
 * 分子公司信息修改
 * @author wuyao
 *
 */
public class ZQB_FZGS_SaveEvent  extends DemTriggerEvent {
	private static Logger logger = Logger.getLogger(ZQB_FZGS_SaveEvent.class);
	public ZQB_FZGS_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**com.ibpmsoft.project.zqb.trigger.ZQB_Fzgs_SaveEvent
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap formData = this.getFormData();
		HashMap<String,Object> map = ParameterMapUtil.getParameterMap(formData);
		if(map.get("GLFID") != null && !map.get("GLFID").equals("")){
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE BD_ZQB_GLF SET GSMC=?,GSLX=?,ZCH=?,ZCDZ=?,JJXZ=?,CGBL=?,ZCZB=?,FRDB=?,GLGX=?,SCJYFW=?,BZXX=? WHERE ID=?");
			Connection conn = DBUtil.open();
			PreparedStatement ps = null;
			try {
				conn.setAutoCommit(true);
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, map.get("GSMC")==null?"":(String)map.get("GSMC"));
				ps.setString(2, map.get("GSLX")==null?"":(String)map.get("GSLX"));
				ps.setString(3, map.get("ZCH")==null?"":(String)map.get("ZCH"));
				ps.setString(4, map.get("ZCDZ")==null?"":(String)map.get("ZCDZ"));
				ps.setString(5, map.get("JJXZ")==null?"":(String)map.get("JJXZ"));
				ps.setString(6, map.get("CGBL")==null?"":(String)map.get("CGBL"));
				ps.setString(7, map.get("ZCZB")==null?"":(String)map.get("ZCZB"));
				ps.setString(8, map.get("FRDB")==null?"":(String)map.get("FRDB"));
				ps.setString(9, map.get("GLGX")==null?"":(String)map.get("GLGX"));
				ps.setString(10, map.get("SCJYFW")==null?"":(String)map.get("SCJYFW"));
				ps.setString(11, map.get("BZXX")==null?"":(String)map.get("BZXX"));
				ps.setLong(12, Long.parseLong((String)map.get("GLFID")));
				ps.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				logger.error(e,e);
			} finally{
				DBUtil.close(conn, ps, null);
			}
		}
		return true;
	}
}