package com.ibpmsoft.project.zqb.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.ibpmsoft.project.zqb.dao.ZqbImpEventDAO;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.MessageAPI;
import com.iwork.sdk.ProcessAPI;

public class ZqbImpEventService {
	private final static String SXDD_UUID = "a6a3202f5b294bb79acd578ddbe05e55";
    private ZqbImpEventDAO zqbImpEventDAO;
	public ZqbImpEventDAO getZqbImpEventDAO() {
		return zqbImpEventDAO;
	}

	public void setZqbImpEventDAO(ZqbImpEventDAO zqbImpEventDAO) {
		this.zqbImpEventDAO = zqbImpEventDAO;
	}

	public List<HashMap> getList(int pageSize, int pageNumber, String sxmc,
			String startdate, String enddate) {
		List<HashMap> returnList=zqbImpEventDAO.getList(pageSize, pageNumber, sxmc, startdate, enddate);
		return returnList;
	}
	public List<HashMap> getcxddList(int pageSize, int pageNumber, String sxmc,
			String startdate, String enddate) {
		List<HashMap> returnList=zqbImpEventDAO.getcxddList(pageSize, pageNumber, sxmc, startdate, enddate);
		return returnList;
	}

	/**
	 * 是否场外
	 * 
	 * @return
	 */
	public boolean getIsSuperMan() {
		String roleTyle = "";
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (uc != null) {
			if( uc.get_userModel()!=null&& uc.get_userModel().getOrgroleid()!=null){
				Long orgRoleId = uc.get_userModel().getOrgroleid();
				if (orgRoleId.equals(new Long(5))) {
					flag = true;
				}
			}
		}
		return flag;
	}

	public int getTotalListSize(int pageSize, int pageNumber, String sxmc,
			String startdate, String enddate) {
		int count = 0;
		int n=1;
		Map params = new HashMap();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		String userfullname = userid + "[" + uc.get_userModel().getUsername()+ "]";
		StringBuffer sql = new StringBuffer(
				"select count(*) as count FROM BD_XP_ZYDDSXB a,SYS_ENGINE_FORM_BIND c where 1=1 "
						+ " and formid = '142'        and metadataid = '156'    and a.id = c.dataid  and c.instanceid is not null ");
		if (sxmc != null && !sxmc.equals("")) {
			params.put(n, sxmc);
			sql.append(" and a.sxmc= ? ");
			n++;

		}
		if (startdate != null && !"".equals(startdate)) {
			params.put(n, startdate);
			sql.append(" and to_char(a.ZCFKSJ,'yyyy-MM-dd')>= ? ");
			n++;
		}
		if (enddate != null && !"".equals(enddate)) {
			params.put(n, enddate);
			sql.append(" and to_char(a.ZCFKSJ,'yyyy-MM-dd')<=  ? ");
			n++;
		}
		sql.append(" and (cjr='"
						+ userid
						+ "' or spr='"
						+ userfullname
						+ "' "
						+ " or instanceid in (select instanceid   from BD_XP_ZYDDSXFKRB a    inner join SYS_ENGINE_FORM_BIND b on formid ="
						+ "        (select subformid    from sys_engine_subform t       where subtablekey =      'SUBFORM_SXFKRBD')"
						+ "     and metadataid = '159'     and a.id = b.dataid))");
		count = DBUTilNew.getInt("count",sql.toString(),params);
		return count;
	}

	public String sendMail(String instanceid) {
		String title = "重要督导事项提醒";
		String content = "";
		String info = "";
		String sxmc = "";
		Long id = Long.parseLong(instanceid);
		HashMap jbxx = DemAPI.getInstance().getFromData(id,
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		if (jbxx != null && jbxx.get("SXMC") != null) {
			sxmc = jbxx.get("SXMC").toString();
			// 获取反馈人列表
			List<HashMap> list = DemAPI.getInstance().getFromSubData(
					Long.parseLong(instanceid), "SUBFORM_SXFKRBD");
			if (list != null && list.size() > 0) {
				for (HashMap map : list) {
					Object userid = map.get("USERID");
					if (userid != null) {
						UserContext uc = UserContextUtil.getInstance()
								.getUserContext(userid.toString());
						content = sxmc + "事项请您登录系统在待办事宜中查看。";
						String mobile = uc.get_userModel().getMobile();
						String email = uc._userModel.getEmail();
						if (mobile != null && !mobile.equals("")) {
							MessageAPI.getInstance().sendSMS(uc, mobile,
									content);
						}
						if (email != null && !email.equals("")) {
							boolean flag = MessageAPI.getInstance()
									.sendSysMail("", email, title, content);
							if (!flag) {
								info = "发邮件存在问题";
							}
						}
					} else {
						info = "未获取到用户账号";
						break;
					}
				}
			} else {
				info = "该记录没有事项反馈人员！";
			}
		} else {
			info = "未获取事项名称！";
		}

		return info;
	}

	public String getZydd(String wlins) {
		Long instanceid = 0l;
		if(wlins!=null&&!wlins.equals("")){
			instanceid=Long.parseLong(wlins);
		}
		if(instanceid==0l){
			return "";
		}else{
			HashMap l=DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);//主表数据
			//ID
			List<HashMap> sublist=DemAPI.getInstance().getFromSubData(instanceid, "SUBFORM_SXFKRBD");//获取子表数据
			StringBuffer html=new StringBuffer("	<table border=\"0\" cellpadding=\"0\" " +
					"cellspacing=\"0\" width=\"100%\" class=\"ke-zeroborder\" style=\"font-size: 1em;padding-left:85px;\">" +
					"<tr class=\"ui-jqgrid-labels\" role=\"rowheader\" >   ");
			html.append(" <td width=\"180\" class=\"td_title\" id=\"title_SXMC\">");
			html.append("<span style=\"color:red;\">*</span>事项名称</td>");
			html.append("<td class=\"td_data\" id=\"data_SXMC\">");
			//事项名称
			String sxmc=l.get("SXMC")!=null?l.get("SXMC").toString():"";
			html.append(sxmc);
			html.append("		</td>");
			html.append("	</tr>");
			html.append("	<tr id=\"itemTr_1744\">");
			html.append("		<td width=\"180\" class=\"td_title\" id=\"title_ZCFKSJ\">");
			html.append("			最迟反馈时间");
			html.append("		</td>");
			html.append("		<td class=\"td_data\" id=\"data_ZCFKSJ\">");
			String zcfksj=l.get("ZCFKSJ")!=null?UtilDate.datetimeFormat2((Timestamp)l.get("ZCFKSJ")):"";
			html.append(zcfksj);
			html.append("		</td>");
			html.append("	</tr>");
			html.append("	<tr id=\"itemTr_1745\">");
			html.append("		<td width=\"180\" class=\"td_title\" id=\"title_SPR\">");
			html.append("			审批人");
			html.append("		</td>");
			html.append("		<td class=\"td_data\" id=\"data_SPR\">");
			String spr=l.get("SPR")!=null?l.get("SPR").toString():"";
			html.append(spr);
			html.append("		</td>");
			html.append("	</tr>");
			html.append("	<tr id=\"itemTr_1746\">");
			html.append("		<td width=\"180\" class=\"td_title\" id=\"title_SXGS\">");
			html.append("			事项概述");
			html.append("		</td>");
			html.append("		<td class=\"td_data\" id=\"data_SXGS\">");
			//事项概述
			String sxgs=l.get("SXGS")!=null?l.get("SXGS").toString():"";
			html.append(sxgs);
			html.append("		</td>");
			html.append("	</tr>");
			html.append("	<tr id=\"itemTr_1768\">");
			html.append("		<td width=\"180\" class=\"td_title\" id=\"title_SXZL\">");
			html.append("			事项资料");
			html.append("		</td>");
			html.append("		<td class=\"td_data\" id=\"data_SXZL\">");
			String sxzl=l.get("SXZL")!=null?l.get("SXZL").toString():"";
			String[] sxzlarr=sxzl.split(",");
			for(String s:sxzlarr){
				if(s!=null&&!"".equals(s)){
					String name=DBUtil.getString("select FILE_SRC_NAME from sys_upload_file where file_id='"+s+"'", "FILE_SRC_NAME");
					html.append("<div  id=\"eb266039238b422b9922fa3d49ae881f\" style=\"background-color: #F5F5F5;vertical-align:middle;"+
							"border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">");
					html.append("<span><a href=\"uploadifyDownload.action?fileUUID="+s+"\" target=\"_blank\">" +
							name+"</a></span>");
					html.append("</div>");
				}
			}
			
		//	html.append("			${SXZL}&nbsp;");
			html.append("		</td>");
			html.append("	</tr>");
			html.append("	<tr id=\"txtAreaTr_141\">");
			html.append("		<td class=\"td_title\">");
			html.append("			事项反馈人表单");
			html.append("		</td>");
			html.append("		<td width=\"100%\" class=\"td_data\">");
			//html.append("			${SUBFORM_SXFKRBD}&nbsp;");
			html.append("<table text-align:center;>");
			html.append("<tr  class=\"header\" height=\"30px\" " +
					" style=\"border-right: #efefef 1px solid;\">");
			html.append("  <td  style=\"text-align: center;\">账号</td>");
			html.append("  <td  style=\"text-align: center;\">姓名</td>");
			html.append("  <td style=\"text-align: center;\">联系电话</td>");
			html.append("    <td  style=\"text-align: center;\">手机</td>");
			html.append(  " <td style=\"text-align: center;\">邮箱</td>");
			html.append(   "  <td style=\"text-align: center;\">备注</td>");
			html.append(   "     </tr>");
			for(HashMap sub:sublist){
				html.append("  <tr class=\"cell\"> ");
				html.append(" <td   style=\"text-align: center;\"> "+(sub.get("USERID")==null?"":sub.get("USERID").toString())+"</td>");
				html.append("<td   style=\"text-align: center;\">"+(sub.get("NAME")==null?"":sub.get("NAME").toString())+"</td>");
						html.append("<td   style=\"text-align: center;\">"+(sub.get("TEL")==null?"":sub.get("TEL").toString())+"</td>");
						html.append("<td  style=\"text-align: center;\">"+(sub.get("PHONE")==null?"":sub.get("PHONE").toString())+"</td>");
						html.append("<td   style=\"text-align: center;\">"+(sub.get("EMAIL")==null?"":sub.get("EMAIL").toString())+"</td>");
						html.append("<td  style=\"text-align: center;\">"+(sub.get("MEMO")==null?"":sub.get("MEMO").toString())+"</td>");
						html.append("</tr>");
	
			}
			html.append("	</table>");
			html.append("		</td>");
			html.append("</tr>	</table>");
			return html.toString();
		}
	}

	public boolean deleteDdsj(String instanceid) {
		HashMap hashMap=ProcessAPI.getInstance().getFromData(Long.parseLong(instanceid), EngineConstants.SYS_INSTANCE_TYPE_PROCESS);//表中数据
		Long lcbs=null;
		if(hashMap.get("LCBS")!=null&&!hashMap.get("LCBS").toString().equals("")){
			lcbs=Long.parseLong(hashMap.get("LCBS").toString());
		}
		//删除流程表的数据
		StringBuffer sql = new StringBuffer("delete from BD_XP_ZYDDSXB where instanceid='"+instanceid+"'");
		DBUtil.executeUpdate(sql.toString());
		//删除流程
		if(lcbs!=null){
			List<OrgUser> userlist=ProcessAPI.getInstance().getProcessTransUserList(lcbs);
			if(userlist!=null&&userlist.size()>0){
				for(int i=0;i<userlist.size();i++){
				String userid=userlist.get(i).getUserid();
					List<Task> tasklist = ProcessAPI.getInstance()
					.getUserProcessTaskList("ZYDDSXLC:1:81304", "usertask6",
							userid);
					for (Task task : tasklist) {
						if (Long.parseLong(task.getProcessInstanceId()) ==lcbs) {
							//删除流程表数据
							ProcessAPI.getInstance().removeProcessInstance(task.getId(), userid);//lcbs.toString()
						}
					}
				}
			}
		}
		return true;
	}

	public boolean getFkzl(String instanceid) {
		if(instanceid.equals("0")){
			return false;
		}else{
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			String userid = uc._userModel.getUserid();
			List<HashMap> list = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_SXFKRBD");
			if(list.size()>0){
				for (HashMap hashMap : list) {
					if(userid.equals(hashMap.get("USERID").toString())){
						return true;
					}
				}
			}
			return true;
		}
	}

}
