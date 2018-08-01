package com.ibpmsoft.project.zqb.event;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUtil;

public class ZqtxEvent  implements IWorkScheduleInterface{

	@Override
	public boolean executeBefore() throws ScheduleException {
		return true;
	}

	@Override
	public boolean executeOn() throws ScheduleException {
		
		SendMsg();
		return true;
	}

	@Override
	public boolean executeAfter() throws ScheduleException {
		return true;
	}
	private void SendMsg() {
		Connection conn = null;
		
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall("{call proc_rctx}");
			cstmt.execute();  
		} catch (Exception e) {
			
		}finally{
			DBUtil.close(conn, null, null);
		}
		
		
	}
}
