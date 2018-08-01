package com.ibpmsoft.project.zqb.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.DBUtil;

public class ZhaipaiEvent implements IWorkScheduleInterface{
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
	public void SendMsg(){
		List lables = new ArrayList();
		lables.add("sid");
		lables.add("customerno");
		lables.add("extend5");
		lables.add("cxddbgsj");
		lables.add("tid");
		String sql="select s.id sid,s.customerno,s.extend5,s.cxddbgsj,t.id tid from bd_zqb_kh_base s left join bd_mdm_khqxglb t on t.khbh=s.customerno  where s.extend5 <=to_char(sysdate,'yyyy-MM-dd') or  to_char(s.cxddbgsj,'yyyy-MM-dd')<=to_char(sysdate,'yyyy-MM-dd')";
		Connection conn = DBUtil.open();
		Statement  ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.createStatement();
			List<HashMap> list = DBUtil.getDataList(lables,sql, null);
			for (HashMap data : list) {
				String sid=data.get("sid")==null?"":data.get("sid").toString();
				String customerno=data.get("customerno")==null?"":data.get("customerno").toString();
				String extend5=data.get("extend5")==null?"":data.get("extend5").toString();
				String cxddbgsj=data.get("cxddbgsj")==null?"":data.get("cxddbgsj").toString();
				String tid=data.get("tid")==null?"":data.get("tid").toString();
				if(extend5!=null && !"".equals(extend5)){
					ps.addBatch(" update bd_zqb_kh_base s set s.cxddbg='转出',s.ygp='摘牌' where s.id="+sid);
					ps.addBatch(" update orguser s set s.enddate=sysdate where s.extend1='"+customerno+"'");
					ps.addBatch(" update bd_mdm_khqxglb s set s.khfzr='',s.zzcxdd='',s.fhspr='',s.zzspr='',s.ggfbr='' ,s.cwscbfzr2='',s.cwscbfzr3='',s.qynbrysh='',s.fbbwjshr='',s.khfzrdq='',s.zzcxdddq='',s.fhsprdq='',s.zzsprdq='',s.ggfbrdq='',s.cwscbfzr2dq='',s.cwscbfzr3dq='' where s.id="+tid);
				}else if(cxddbgsj!=null && !"".equals(cxddbgsj)){
					ps.addBatch(" update bd_zqb_kh_base s set s.cxddbg='转出',s.ygp='转出'  where s.id="+sid);
					ps.addBatch(" update orguser s set s.enddate=sysdate where s.extend1='"+customerno+"'");
					ps.addBatch(" update bd_mdm_khqxglb s set s.khfzr='',s.zzcxdd='',s.fhspr='',s.zzspr='',s.ggfbr='' ,s.cwscbfzr2='',s.cwscbfzr3='',s.qynbrysh='',s.fbbwjshr='',s.khfzrdq='',s.zzcxdddq='',s.fhsprdq='',s.zzsprdq='',s.ggfbrdq='',s.cwscbfzr2dq='',s.cwscbfzr3dq='' where s.id="+tid);
				}
			}
			ps.executeBatch();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, null, null);
				try {
					if(ps!=null) ps.close();
				} catch (Exception e) {
					
				}
		}
	}
}
