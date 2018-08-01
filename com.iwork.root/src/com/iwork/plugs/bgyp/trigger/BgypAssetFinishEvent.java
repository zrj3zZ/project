package com.iwork.plugs.bgyp.trigger;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessTriggerEvent;
import com.iwork.sdk.ProcessAPI;
/**
 * 流程触发器
 * @author davidyang
 *
 */
public class BgypAssetFinishEvent extends ProcessTriggerEvent {
	private static Logger logger = Logger.getLogger(BgypAssetFinishEvent.class);
	public BgypAssetFinishEvent(UserContext me,HashMap hash){
		super(me,hash);
	}
	
	public boolean execute() {
		String subformkey = "SUBFORM_BGYPSQDZB";
		List<HashMap> list = ProcessAPI.getInstance().getFromSubData(this.getInstanceId(), subformkey);
		Connection conn = null;
		boolean flag = false;
		PreparedStatement stmt = null;
		try{
			conn = DBUtil.open();
			conn.setAutoCommit(false);
			for(HashMap hm:list){
				String spbh = (String)hm.get("SPBH");
				Long number = (Long)hm.get("SQSL");
				try {
					 StringBuffer sql = new StringBuffer();
					 sql.append("update BD_BGYP_JCXXB set KCSL=(KCSL-?) WHERE NO=? ");
					 stmt = conn.prepareStatement(sql.toString());
					 stmt.setLong(1, number);
					 stmt.setString(2, spbh);
					 stmt.executeUpdate(sql.toString());
					 DBUtil.close(null, stmt, null);
				} catch (Exception e) {logger.error(e,e);
				}
			}
			conn.commit();
			flag = true;
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (Exception ex) {logger.error(ex);
			}
			
		}finally{
			 DBUtil.close(conn, stmt, null);
		}
		return flag;
	}
}
