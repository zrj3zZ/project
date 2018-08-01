package com.ibpmsoft.project.zqb.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.app.weixin.process.action.qy.util.TestSendMes;
import com.iwork.commons.util.DBUtil;
import com.iwork.sdk.MessageAPI;

public class RctxSendMsgEvent implements IWorkScheduleInterface{
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

		lables.add("title");
		lables.add("re_startdate");
		lables.add("re_enddate");
		lables.add("qs");
		lables.add("dm");
		lables.add("jc");
		lables.add("startdate");
		lables.add("enddate");
		lables.add("ddsj");
		lables.add("dmsj");
		lables.add("isalert");
		lables.add("txrq");
		lables.add("userid");
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from (  select s.title,s.re_startdate,s.re_enddate,1 qs, (select zqdm from bd_zqb_kh_base y where y.customerno=z.khbh ) dm ,(select zqjc from bd_zqb_kh_base y where y.customerno=z.khbh ) jc,s.startdate,s.enddate  ,(select y.mobile from orguser y where y.userid=s.userid) ddsj, ");
		sb.append(" (select y.mobile from orguser y where y.userid=(select zqdm from bd_zqb_kh_base y where y.customerno=z.khbh)) dmsj ,s.isalert ,ss.txrq txrq,s.userid from  iwork_sch_calendar s left join bd_xp_tyrcglb t on to_char(s.id)=to_char(t.mainid)   ");
		sb.append("left join BD_XP_QTGGZLLC z on  to_char(z.instanceid)=to_char(t.subid) left join bd_xp_xpsxqtb ss on ss.xpsxid=s.id where  z.id is not null  ");
		sb.append("  UNION");
		sb.append("    select s.title,s.re_startdate,s.re_enddate,1 qs,(select zqdm from bd_zqb_kh_base y where y.customerno=z.khbh ) dm ,(select zqjc from bd_zqb_kh_base y where y.customerno=z.khbh ) jc, ");
		sb.append("  s.startdate,s.enddate  ,(select y.mobile from orguser y where y.userid=s.userid) ddsj ,(select y.mobile from orguser y where y.userid=(select zqdm from bd_zqb_kh_base y where y.customerno=z.khbh)) dmsj ,s.isalert    ");
		sb.append("  ,ss.txrq txrq,s.userid  from  iwork_sch_calendar s left join bd_xp_tyrcglb t on to_char(s.id)=to_char(t.mainid)  left join RCYWCB z on  to_char(z.instanceid)=to_char(t.subid) left join bd_xp_xpsxqtb ss on ss.xpsxid=s.id where  z.id is not null  ");
		sb.append("   UNION  ");
		sb.append(" select s.title,s.re_startdate,s.re_enddate,0 qs,'0' dm,'0' jc ,s.startdate,s.enddate  ,(select y.mobile from orguser y where y.userid=s.userid) ddsj,'0' dmsj ,s.isalert ,ss.txrq txrq  ");
		sb.append("    ,s.userid from  iwork_sch_calendar s left join  bd_xp_tyrcglb t on t.mainid=s.id  left join bd_xp_xpsxqtb ss on ss.xpsxid=s.id where t.id is null    ) tt  ");
		sb.append("    where  to_char(tt.startdate,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') or   to_char(tt.txrq,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd')     ");
		List<HashMap> targetmsg = DBUtil.getDataList(lables, sb.toString(), null);
		for (HashMap data : targetmsg) {
			String title = data.get("title")==null?"":data.get("title").toString();
			String qs = data.get("qs")==null?"":data.get("qs").toString();
			String dm = data.get("dm")==null?"":data.get("dm").toString();
			String jc = data.get("jc")==null?"":data.get("jc").toString();
			String ddsj = data.get("ddsj")==null?"":data.get("ddsj").toString();
			String dmsj = data.get("dmsj")==null?"":data.get("dmsj").toString();
			String isalert = data.get("isalert")==null?"":data.get("isalert").toString();
			String userid = data.get("userid")==null?"":data.get("userid").toString();
			TestSendMes tms=new TestSendMes();
			if(isalert.equals("1")){
				if(qs.equals("1")){
					if(!"".equals(ddsj) && ddsj!=null){
						MessageAPI.getInstance().sendSMS(ddsj.toString(),dm+jc+","+title);
						tms.sendDcwjMsgList(userid, title);
					}
					if(!"".equals(dmsj) && dmsj!=null){
						MessageAPI.getInstance().sendSMS(dmsj.toString(),title);
						tms.sendDcwjMsgList(dm, title);
					}
				}else if(qs.equals("0")){
					if(!"".equals(ddsj) && ddsj!=null){
						MessageAPI.getInstance().sendSMS(ddsj.toString(),title);
						tms.sendDcwjMsgList(userid, title);
					}
					
				}
			}
		}
	}
}
