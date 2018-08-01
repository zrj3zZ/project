package com.ibpmsoft.project.cbpmc.stationery.trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.process.runtime.pvm.trigger.ProcessStepTriggerEvent;

public class StationerySetSuccessStep4TransTrigger  extends ProcessStepTriggerEvent{
	private UserContext _me;
	private HashMap params;
	private String log = "";
	public StationerySetSuccessStep4TransTrigger(UserContext me,HashMap hash){
		super(me,hash);
		_me = me;
		params = hash;
	}
	
	public boolean execute()
	  {
		boolean flag = false;
	    return flag;
	  }
	
	 private Map getInventoryCount() throws Exception{
		StringBuffer sql = new StringBuffer();
	    sql.append(" select stationeryid, sum(entercount) as entercount, sum(outcount) as outcount, sum(count) as count from  (select t.stationeryid, t.count as entercount, 0 as outcount, t.count as count  from bo_enter_warehouse_info t  union ALL  select s.stationeryid, 0 as entercount, s.count as outcount, 0- s.count as count  from bo_out_warehouse_info s)  group by stationeryid ");
	    Connection conn = null;
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    Map m = new HashMap();
	    try {
	    	conn = DBUtil.open();
	      st = conn.prepareStatement(sql.toString());
	      rs = st.executeQuery();
	      while (rs.next())
	        m.put(rs.getString("stationeryid"), rs.getInt("Count"));
	    }
	    finally
	    {
	      DBUtil.close(conn,st,rs);
	    }
	    return m;
	  }
} 
