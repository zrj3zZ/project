package com.iwork.app.message.sysmsg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.app.message.sysmsg.model.SysMessage;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;

public class SysMessageDAO extends HibernateDaoSupport {
	public SysMessageDAO() {
	}
	/**
	 * 设置全部已读
	 * @param userid
	 */
	public void removeAllMsg(String userid){
		String sql = "delete from sys_message where receiver=?";
		Map params = new HashMap();
		params.put(1, userid);
		com.iwork.commons.util.DBUtil.update(sql, params);
	}
	
	

	/**
	 * 设置全部已读
	 * @param userid
	 */
	public void setAllRead(String userid){
		String sql = "update sys_message set status=? where receiver=?"; 
		Map params = new HashMap();
		params.put(1, SysMessage.MSG_STATUS_READ_YES);
		params.put(2, userid);
		com.iwork.commons.util.DBUtil.update(sql, params);
	}
	
	/**
	 * 获得消息总数
	 * @param userId
	 * @param type
	 * @param status
	 * @return
	 */
	public int getSyMsgListSize(String userId,String type,String status){
		int count = 0;
		StringBuffer sql = new StringBuffer();
		List parameter=new ArrayList();//存放参数
		sql.append("select COUNT(*) NUM from sys_message WHERE  receiver=?");
		parameter.add(userId);
		if(type!=null&&!"".equals(type)){
			sql.append("AND TYPE=?");
			parameter.add(type);
		}
		if(status!=null&&!"".equals(status)){
			sql.append(" AND STATUS=?");
			parameter.add(status);
		}
		sql.append(" AND STATUS<>-1");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				stmt.setString(i+1, parameter.get(i).toString());
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				count=rset.getInt("NUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return count;
	}
	/**
	 * 获得指定用户的消息列表
	 * @param type
	 * @param status
	 * @param pageSize
	 * @param pageNow
	 * @return
	 */
	public List<SysMessage> getSyMsgList(String userId,String type,String status,int pageSize,int startRow){
		final int pageSize1=pageSize;
		final int startRow1=startRow;
		final String userId1=userId;
		final String type1=type;
		final String status1=status;
		return this.getHibernateTemplate().executeFind(new HibernateCallback(){
			public List doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				DBUtilInjection d=new DBUtilInjection();
				List l=new ArrayList();
				hql.append("From SysMessage where  receiver=?");
				if(userId1!=null&&!"".equals(userId1)){
					if(d.HasInjectionData(userId1)){
						return l;
					}
				}
				if(status1!=null&&!"".equals(status1)){
					if(d.HasInjectionData(status1)){
						return l;
					}
					hql.append(" AND status=?");
				}
				if(type1!=null&&!"".equals(type1)){
					
						if(d.HasInjectionData(type1)){
							return l;
						}
					hql.append(" AND type=?");
				}
				hql.append(" and status<> ? ORDER BY senddate DESC");
				Query query=session.createQuery(hql.toString());
				int i = 0;
				query.setString(i, userId1);i++;
				if(status1!=null&&!"".equals(status1)){
					query.setString(i, status1);i++;
				}
				if(type1!=null&&!"".equals(type1)){
					query.setString(i, type1);i++;
				}
				query.setInteger(i, SysMessage.MSG_STATUS_DELETED);i++;
				
				query.setFirstResult((startRow1-1)*pageSize1);
				query.setMaxResults(pageSize1);
				return query.list(); 
			}
		});
	}
	/**
	 * 获得某条系统消息
	 * @param id
	 * @return
	 */
	public SysMessage getSysMsgById(Long id) {
		SysMessage model = (SysMessage) this.getHibernateTemplate().get(SysMessage.class, id);
		return model;
	}
	
	/**
	 * 更新
	 * @param SysMessage
	 */
	public void updateBoData(SysMessage model){
		this.getHibernateTemplate().update(model);
	}
	
	/**
	 * 获得用户信息总条数
	 * @param userid
	 * @return
	 */
	public int getAllRow(String userid) {
		String sql = " FROM " + SysMessage.DATABASE_ENTITY + " where receiver=? ORDER BY senddate DESC";
		DBUtilInjection d=new DBUtilInjection();
		
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return 0;
			}
		}
		Object[] value = {userid};
		List list = this.getHibernateTemplate().find(sql,value);
		return list.size();
	}
	
	/**
	 * 获得用户所有信息的集合
	 * @param userid
	 * @return
	 */
	public List getAllMsgs(String userid){
		String sql = " FROM " + SysMessage.DATABASE_ENTITY + " where receiver=? ORDER BY senddate DESC";
		DBUtilInjection d=new DBUtilInjection();
		List l=new ArrayList();
		if(userid!=null&&!"".equals(userid)){
			if(d.HasInjectionData(userid)){
				return l;
			}
		}
		Object[] value = {userid};
		List list = this.getHibernateTemplate().find(sql,value);
		return list;
	}
	
	/**
	 * 获得未读信息条数
	 * @param userId
	 * @return
	 */
	public int getUnReadMsgRow(String userId) {
        String sql = " FROM " + SysMessage.DATABASE_ENTITY + " where status = ? and receiver = ?";
        DBUtilInjection d=new DBUtilInjection();
		
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return 0;
			}
		}
        Object[] value = {SysMessage.MSG_STATUS_READ_NO,userId};
        List list = this.getHibernateTemplate().find(sql,value);
        return list.size();
	}
	/**
	 * 获得未读消息的集合
	 * @param userId
	 * @return
	 */
	public List getUnReadMsgs(String userId){
		String sql = " FROM " + SysMessage.DATABASE_ENTITY + " where status = ? and receiver = ?";
		 DBUtilInjection d=new DBUtilInjection();
			List l=new ArrayList();
			if(userId!=null&&!"".equals(userId)){
				if(d.HasInjectionData(userId)){
					return l;
				}
			}
		Object[] value = {SysMessage.MSG_STATUS_READ_NO,userId};
        List list = this.getHibernateTemplate().find(sql,value);
        return list;
	}
	
	/**
	 * 创建系统消息
	 * 
	 * @param sysMessage
	 * @return
	 */
	public void createSysMessage(SysMessage sysMsg) {
		String curTime = UtilDate.getNowDatetime();
		sysMsg.setSendDate(curTime);
		this.getHibernateTemplate().save(sysMsg);
	}
	
	/**
	 * 获得用户指定阅读状态下得消息记录条数
	 * @param name
	 * @param status
	 * @return
	 */
	public int getMsgRowByStatus(String userId, int status) {
        String sql = " FROM " + SysMessage.DATABASE_ENTITY + " where status = ? and receiver = ?";
        DBUtilInjection d=new DBUtilInjection();
		
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return 0;
			}
		}
        Object[] value = {status,userId};
        List list = this.getHibernateTemplate().find(sql,value);
        return list.size();
	}
	
	/**
	 * 获得已读信息条数
	 * @param userId
	 * @return
	 */
	public int getReadMsgRow(String userId) {
        String sql = " FROM " + SysMessage.DATABASE_ENTITY + " where status = ? and receiver = ?";
        DBUtilInjection d=new DBUtilInjection();
		
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return 0;
			}
		}
        Object[] value = {SysMessage.MSG_STATUS_READ_YES,userId};
        List list = this.getHibernateTemplate().find(sql,value);
        return list.size();
	}
	
	/**
	 * 分页查询用户未读消息
	 * @param userid 
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getMsgPageList(String userid, int pageSize, int pageNow, String sql) {
		final int pageSize1 = pageSize;
		final int startRow1 = ( pageNow - 1 ) * pageSize;
		final String sql1 = sql;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(sql1);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				return query.list();
			}
		});
	}	
	/**
	 * 获得指定用户特定条件的消息条数
	 * @param userId
	 * @param sql
	 * @return
	 */
	public int getMessageCount(String userId, String sql) {
		List list = this.getHibernateTemplate().find(sql);
        return list.size();
	}
	
	/**
     * 批量Insert消息
     * @param modelArray
     * @throws SQLException
     */
    public void batchInsertMessage (SysMessage[] modelArray) throws SQLException {
    	Session session = getHibernateTemplate().getSessionFactory().openSession();
    	Transaction tx = session.beginTransaction();
    	try{
	    	if(modelArray.length>0){
	    		for(int i=0;i<modelArray.length;i++){
	    			SysMessage model = modelArray[i];
	    			model.setSendDate(UtilDate.getNowDatetime());
	    			session.save(model);
	    			if(i%100==0){
						session.flush();
						session.clear();
					}
	    		}
	    		tx.commit();
	    	}
    	}catch(HibernateException   e){
			tx.rollback();
			logger.error(e,e);
		}finally{
			session.close();
		}
    }
    
    /**
     * 获得第一条未读消息
     * @param userContext
     * @return
     */
    public String getFirstUnreadMsg(UserContext userContext) {
        String userId = userContext.get_userModel().getUserid();
        String userName = userContext.get_userModel().getUsername();
        DBUtilInjection d=new DBUtilInjection();
		
		if(userId!=null&&!"".equals(userId)){
			if(d.HasInjectionData(userId)){
				return null;
			}
		}
		
        String sql = " FROM " + SysMessage.DATABASE_ENTITY + " where status = ? and receiver = ? order by id desc";
        Object[] value = {SysMessage.MSG_STATUS_READ_NO,userId};
        List list = this.getHibernateTemplate().find(sql,value);
        int msgNums = list.size();
        String title = "";
        String url = "";
        Long id = 0L;
        String content = "";
        if(list.size()>0){
        	SysMessage model = (SysMessage)list.get(0);
        	title = model.getTitle();
        	url = model.getUrl();
        	id = model.getId();
        	content = model.getContent();
        	String topMsgTip = "<b><a href=\"\" onClick=\"TopClick('SysMsg');return false;\" title='系统消息' ><img src='../aws_img/logoSysMsg.gif' width=\"16\" height=\"15\" border='0'>(" + msgNums + ")</a></b>&nbsp;&nbsp;";
        	StringBuffer msgJson = new StringBuffer("");
            JSONObject jsonObject = new JSONObject();  
            jsonObject.put("topMsgTip", topMsgTip);
            jsonObject.put("title", getFormatedTitle(title));
            jsonObject.put("content", getFormatedPopupMsg(content==null?"":content, url, id));
            jsonObject.put("popupId", model.getId());
            jsonObject.put("msgNums", msgNums);
            jsonObject.put("href", url==null||url.equals("")?"":url);
            jsonObject.put("userName", userName);
            msgJson.append(jsonObject);
            return msgJson.toString();
        }else{
        	return "";
        }
    }
    
    public String getFirstGGUnreadMsg(UserContext userContext) {
        String userId = userContext.get_userModel().getUserid();
        String userName = userContext.get_userModel().getUsername();
        List lables = new ArrayList();
        lables.add("URL");
        lables.add("TITLE");
        lables.add("ACTDEFID");
        lables.add("BT");
        lables.add("LX");
        lables.add("ID");
        StringBuffer sb = new StringBuffer();
       /* sb.append("SELECT 'loadProcessFormPage.action?actDefId='||P.PROC_DEF_ID_||CHR(38)||'instanceId='||P.PROC_INST_ID_||CHR(38)||'excutionId='||P.EXECUTION_ID_||CHR(38)||'taskId='||P.ID_ AS URL,");
        sb.append("P.DESCRIPTION_ AS TITLE,");
        sb.append("P.PROC_DEF_ID_ AS ACTDEFID");
        sb.append(" FROM PROCESS_RU_TASK P");
        sb.append(" WHERE 1=1 AND P.ASSIGNEE_=? AND SUBSTR(P.PROC_DEF_ID_,0,INSTR(P.PROC_DEF_ID_,':',1)-1) IN ('GGSPLC','CXDDYFQLC','RCYWCB') ORDER BY TO_NUMBER(ID_) DESC");*/
        
        sb.append(" select * from (SELECT to_char('loadProcessFormPage.action?actDefId=' || P.PROC_DEF_ID_ || CHR(38) || 'instanceId=' || P.PROC_INST_ID_ || CHR(38) || 'excutionId=' || P.EXECUTION_ID_ || CHR(38) || 'taskId=' || P.ID_) AS URL, ");
        sb.append("   to_char( P.DESCRIPTION_) AS TITLE,to_char( P.PROC_DEF_ID_) AS ACTDEFID, to_date(TO_CHAR(P.create_time_,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd HH24:mi:ss') CJSJ, '' BT, '消息' LX,'' ID FROM PROCESS_RU_TASK P WHERE 1 = 1 ");
        sb.append("  AND P.ASSIGNEE_ = ? AND SUBSTR(P.PROC_DEF_ID_, 0, INSTR(P.PROC_DEF_ID_, ':', 1) - 1) IN  ('GGSPLC', 'CXDDYFQLC', 'RCYWCB') union  ");
        sb.append(" select 'zqb_announcement_notice_reply_add.action?ggid=' || t.id ||  CHR(38) || 'hfqkid=' || 0 AS URL, t.Tznr AS TITLE,'TZGG' AS ACTDEFID,T.FSSJ CJSJ,T.TZBT BT, '重要通知' LX,to_char(s.ggid) ID ");
        sb.append("  from bd_xp_tzggb t left join Bd_Xp_Hfqkb s on t.id=s.ggid  where t.tzlx='弹窗通知' and s.userid= ? and s.sfck is null ) z order by z.cjsj desc  ");
        
        Map params = new HashMap();
        params.put(1, userId);
        params.put(2, userId);
        List<HashMap> list = com.iwork.commons.util.DBUtil.getDataList(lables, sb.toString(), params);
        
        int msgNums = list.size();
        String title = "";
        String url = "";
        String actDefId = "";
        Long id = 0L;
        String content = "";
        if(list.size()>0){
        	HashMap model = list.get(0);
        	title = model.get("TITLE")==null?"":model.get("TITLE").toString();
        	url = model.get("URL")==null?"":model.get("URL").toString();
        	actDefId = model.get("ACTDEFID")==null?"":model.get("ACTDEFID").toString();
        	id = 0l;
        	content = model.get("TITLE")==null?"":model.get("TITLE").toString();
        	String topMsgTip = "<b><a href=\"\" onClick=\"TopClick('SysMsg');return false;\" title='系统消息' ><img src='../aws_img/logoSysMsg.gif' width=\"16\" height=\"15\" border='0'>(" + msgNums + ")</a></b>&nbsp;&nbsp;";
        	StringBuffer msgJson = new StringBuffer("");
            JSONObject jsonObject = new JSONObject();  
            jsonObject.put("topMsgTip", topMsgTip);
            String bt="";
            if(actDefId.startsWith("TZGG")){
            	bt="<span style=\"color:red\">"+(model.get("BT")==null?"":model.get("BT").toString())+"</span>";
            }else if(actDefId.startsWith("GGSPLC")){
            	bt="公告流程提醒";
            }else if(actDefId.startsWith("CXDDYFQLC")){
            	bt="日常业务呈报提醒";
            }
           // jsonObject.put("title", actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")?"公告流程提醒":"日常业务呈报提醒");//getFormatedTitle(title==null?"":title)
            jsonObject.put("title", bt);
            if(actDefId.startsWith("TZGG")){
            	 jsonObject.put("content", getFormatedPopupMsg2(content==null?"":content, url, Long.valueOf(model.get("ID")==null?"":model.get("ID").toString())));
            }else{
            	 jsonObject.put("content", getFormatedPopupMsg(content==null?"":content, url, id));
            }
           
            jsonObject.put("popupId", id);
            jsonObject.put("lx", model.get("LX")==null?"":model.get("LX").toString());
            jsonObject.put("msgNums", msgNums);
            jsonObject.put("href", url==null||url.equals("")?"":url);
            jsonObject.put("userName", userName);
            msgJson.append(jsonObject);
            return msgJson.toString();
        }else{
        	return "";
        }
    }
    
    
    private String getFormatedTitle(String title) {
        title = title.replaceAll("&nbsp;", " ");
        if (title.length() > 16) {
            return title.substring(0, 16) + "...";
        } else {
            return title;
        }
    }
    /**
     * 
     * @param msg
     * @return
     */
    private String getFormatedPopupMsg2(String msg, String url ,Long id) {
    	msg = msg.replaceAll("&nbsp;", " ");
        String formatedMsg = "";
        if (msg.length() > 60) {
            formatedMsg = msg.substring(0, 60) + "……";
        } else {
            formatedMsg = msg;
        }

        formatedMsg = "<style>a.pop{text-decoration:none}a:hover.pop{text-decoration:underline} a{color:red}</style><a class='pop' onclick='showtzgg(" + id + ");' href='#' style=\"font-size:13px;\" hidefocus=\"true\" >" + formatedMsg + "</a>";
        
        return formatedMsg;
    }
    /**
     * 
     * @param msg
     * @return
     */
    private String getFormatedPopupMsg(String msg, String url ,Long id) {
    	msg = msg.replaceAll("&nbsp;", " ");
        String formatedMsg = "";
        if (msg.length() > 60) {
            formatedMsg = msg.substring(0, 60) + "……";
        } else {
            formatedMsg = msg;
        }

        formatedMsg = "<style>a.pop{text-decoration:none}a:hover.pop{text-decoration:underline}</style><a class='pop' onclick='ReadOne(" + id + ");' "+(url==null||url.equals("")?"":" target='_blank' href='"+url+"'")+" style=\"font-size:13px;\" hidefocus=\"true\" >" + formatedMsg + "</a>";
        
        return formatedMsg;
    }
    /**
	 * 获得项目管理未标记的消息
	 * @param instanceid

	 * @return
	 */
	public boolean getSyMsgSize(String instanceid){
		int count = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) AS NUM FROM SYS_MESSAGE WHERE  INSTANCEID=? AND STATUS='0'");
		Map params = new HashMap();
		params.put(1, instanceid);
		com.iwork.commons.util.DBUtil.getDataStr("", sql.toString(), params);
		count = DBUtil.getInt(sql.toString(), "NUM");
		return count>0;
	}
}
