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
 * 关联方管理信息添加
 * @author wuyao
 *
 */
public class ZQB_GLF_SaveEvent  extends DemTriggerEvent {
	private static Logger logger = Logger.getLogger(ZQB_GLF_SaveEvent.class);
	public ZQB_GLF_SaveEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	
	/**com.ibpmsoft.project.zqb.trigger.ZQB_GLF_SaveEvent
	 * 执行触发器
	 */
	public boolean execute() { 
		HashMap formData = this.getFormData();
		HashMap<String,Object> map = ParameterMapUtil.getParameterMap(formData);
		//HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(), EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if(map.get("FZGSID") != null && !map.get("FZGSID").equals("")){
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE BD__FZGSGLZB SET GSMC=?,GSLX=?,ZCH=?,ZCDZ=?,JJXZ=?,CGBL=?,ZCZB=?,FRDB=?,GLGX=?,SCJYFW=?,BZXX=? WHERE ID=?");
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
				ps.setLong(12, Long.parseLong((String)map.get("FZGSID")));
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
/*下面这段代码实现功能和上面代码相同，调试不通，所以采用JDBC
{
			Long fzgsid = Long.parseLong((String)map.get("FZGSID"));
			String uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE = '分子公司管理表单'", "UUID");
			Long instanceid = DBUtil.getLong("SELECT INSTANCEID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '分子公司管理表单') AND DATAID = "+fzgsid+"", "INSTANCEID");
			HashMap<String,Object> cxddmap = new HashMap<String,Object>();
			cxddmap.put("GSMC", map.get("GSMC")==null ? "" :(String)map.get("GSMC")); 
					
			cxddmap.put("ZCH", map.get("ZCH")==null ? "" :(String)map.get("ZCH"));
			cxddmap.put("ZCDZ", map.get("ZCDZ")==null ? "" :(String)map.get("ZCDZ"));
			cxddmap.put("JJXZ", map.get("JJXZ")==null ? "" :(String)map.get("JJXZ"));
			cxddmap.put("CGBL", map.get("CGBL")==null ? "" :(String)map.get("CGBL"));
			cxddmap.put("ZCZB", map.get("ZCZB")==null ? "" :(String)map.get("ZCZB"));
			cxddmap.put("FRDB", map.get("FRDB")==null ? "" :(String)map.get("FRDB"));
			cxddmap.put("GLGX", map.get("GLGX")==null ? "" :(String)map.get("GLGX"));

			cxddmap.put("GSLX", map.get("GSLX")==null ? "" :(String)map.get("GSLX"));

			cxddmap.put("SCJYFW", map.get("SCJYFW")==null ? "" :(String)map.get("SCJYFW"));
			cxddmap.put("BZXX", map.get("BZXX")==null ? "" :(String)map.get("BZXX"));
			DemAPI.getInstance().updateFormData(
					uuid, 
					instanceid, 
					cxddmap, 
					fzgsid, 
					false);
			flag = true;
		}
*/

