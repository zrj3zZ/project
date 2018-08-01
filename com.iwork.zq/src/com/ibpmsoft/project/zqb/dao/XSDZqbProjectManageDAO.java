package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.model.UploadDocModel;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.ProcessAPI;

public class XSDZqbProjectManageDAO extends HibernateDaoSupport {
	private static Logger logger = Logger.getLogger(XSDZqbProjectManageDAO.class);
	public List<UploadDocModel> getDocList(String searchkey, String owner) {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		String config = SystemConfig._xmlcConf.getConfig();
		List<UploadDocModel> list = new ArrayList();
		String sql = "";
		List parameter=new ArrayList();//存放参数
		StringBuffer sb = new StringBuffer();
		if (searchkey == null || "".equals(searchkey)) {
			if ("".equals(owner)) {
				sb.append("select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,"
						+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		              	//WHERE CUSTOMERNAME LIKE '%%'
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B")
								.append(" ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO")
								.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE")
								.append( " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID")
								.append(" ORDER BY A.ID DESC")
								.append(" ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (")
								.append(" SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj")
								.append(" FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		                       
								// WHERE CUSTOMERNAME LIKE '%%'
								sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO")
										.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE")
									.append(" ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID")
									.append(" ORDER BY A.ID DESC) C")
									.append(" ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc");
								sql=sb.toString();
			} else {
						sb.append("select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,")
								.append(" E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (")
						//项目的负责人、现场负责人
								.append(" select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all")
						//任务阶段负责人
								.append(" select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all")
						//项目及成员信息
								.append(" select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from")
								.append(" (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a")
								.append(" left join BD_ZQB_PJ_BASE b on a.dataid = b.id ")
								.append(" ) c inner join (select a.instanceid,dataid,userid,b.name  from")
								.append(" (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a")
								.append(" left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ?")
		              //分派项目审批人
								.append(" union all");
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
		              //if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
						//else
						}else{
						sb.append(" select projectno from bd_zqb_pj_base")
								.append(" WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a")
								.append( " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		              	//WHERE CUSTOMERNAME LIKE '%%'
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
								+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN ("
		                       //项目的负责人、现场负责人
								+ " select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ?"
								+ " union all"
		                        //任务阶段负责人
		                        + " select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
		                        //项目及成员信息
		                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
		                        + " left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join ("
		                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
		                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ?");
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
		                        //分派项目审批人
								sb.append(" union all");
		                        //if 按多项目组审批
								if(config.equals("2")){
								sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN ("
										+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?"
										+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
								}else{
		                        //else
								sb.append(" select projectno from bd_zqb_pj_base"
										+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)"
										+ " ");
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
								}
								sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
								// WHERE CUSTOMERNAME LIKE '%%'
								sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
										+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
										+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
										+ " ORDER BY A.ID DESC) C "
										+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc");
				sql=sb.toString();
			}
		} else {
			if ("".equals(owner)) {
				sb.append("select * from (select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,"
						+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		              	//WHERE CUSTOMERNAME LIKE '%%'
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
								+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		                       
								// WHERE CUSTOMERNAME LIKE '%%'
								sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
										+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
										+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
										+ " ORDER BY A.ID DESC) C"
										+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) a,(select * from sys_upload_file where file_src_name like ? or file_src_name like ?) b where a.jdzl like '%'||b.file_id||'%'");
								sql=sb.toString();
								parameter.add(searchkey.toUpperCase());
								parameter.add(searchkey.toLowerCase());
			} else {
				sb.append("select * from (select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,"
						+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//任务阶段负责人
						+ " select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
						+ " left join BD_ZQB_PJ_BASE b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ?"
		              //分派项目审批人
		              	+ " union all");
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
		              //if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
		              //else
						}else{
						sb.append(" select projectno from bd_zqb_pj_base"
		              	+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
		              	+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		              	//WHERE CUSTOMERNAME LIKE '%%'
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
								+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN ("
		                       //项目的负责人、现场负责人
								+ " select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ?"
								+ " union all"
		                        //任务阶段负责人
		                        + " select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
		                        //项目及成员信息
		                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
		                        + " left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join ("
		                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
		                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ?");
		                       
		                        //分派项目审批人
								sb.append(" union all");
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
		                        //if 按多项目组审批
								if(config.equals("2")){
								sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN ("
										+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?"
										+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
								}else{
		                        //else
								sb.append(" select projectno from bd_zqb_pj_base"
										+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)"
										+ " ");
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
								}
								sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
								// WHERE CUSTOMERNAME LIKE '%%'
								sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
										+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
										+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
										+ " ORDER BY A.ID DESC) C "
										+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) a,(select * from sys_upload_file where file_src_name like ? or file_src_name like ?) b where a.jdzl like '%'||b.file_id||'%'");
								parameter.add(searchkey.toUpperCase());
								parameter.add(searchkey.toLowerCase());
								sql=sb.toString();
			}
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql);
			if (searchkey == null || "".equals(searchkey)) {
				for (int i = 0; i < parameter.size(); i++) {
					stmt.setString(i+1, parameter.get(i).toString());
				}
			}else{
				for (int i = 0; i < parameter.size(); i++) {
					if(i<parameter.size()-2){
						stmt.setString(i+1, parameter.get(i).toString());
					}else{
						stmt.setString(i+1, "%"+parameter.get(i).toString()+"%");
					}
				}
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				UploadDocModel model = new UploadDocModel();
				String projectname = rset.getString("projectname");
				String projectno = rset.getString("projectno");
				String attach = rset.getString("jdzl");
				List<FileUpload> sublist1 = null;
				if (searchkey != null) {
					String file_id = rset.getString("file_id");

					sublist1 = FileUploadAPI.getInstance().getFileUploads(
							file_id);

				}
				List<FileUpload> sublist = FileUploadAPI.getInstance()
						.getFileUploads(attach);

				if (searchkey != null) {
					for (FileUpload f : sublist1) {
						String srcName = f.getFileSrcName();
						String fileurl = "uploadifyDownload.action?fileUUID="+f.getFileId();
						f.setFileUrl(fileurl);
						if (srcName != null && srcName.lastIndexOf(".") >= 0) {
							String extName = srcName.substring(
									srcName.lastIndexOf(".") + 1).toLowerCase();
							String icon = WebUIUtil.getLinkIcon(extName);
							f.setPrepare2(icon);
						}

					}

				} else {
					for (FileUpload f : sublist) {
						String srcName = f.getFileSrcName();
						String fileurl = "uploadifyDownload.action?fileUUID="+f.getFileId();
						f.setFileUrl(fileurl);
						if (srcName != null && srcName.lastIndexOf(".") >= 0) {
							String extName = srcName.substring(
									srcName.lastIndexOf(".") + 1).toLowerCase();
							String icon = WebUIUtil.getLinkIcon(extName);
							f.setPrepare2(icon);
						}

					}
				}
				model.setProjectName(projectname);
				model.setProjectNo(projectno);
				if (searchkey != null) {
					model.setList(sublist1);
				} else {
					model.setList(sublist);
				}
				list.add(model);

			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i).getProjectName()!=null&&list.get(i).getProjectName()
						.equals(list.get(j).getProjectName())) {
					// 合并
					list.get(i).getList().addAll(list.get(j).getList());
					// 移除重复的
					list.remove(j);
					j--;
				}

			}
		}
		return list;
	}

	public List<UploadDocModel> getQuestionDocList(String xmbh, Long taskno) {
		List<UploadDocModel> list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from BD_ZQB_XMWTFK where taskno=? and xmbh=?");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setLong(1, taskno);
			stmt.setString(2, xmbh);
			rset = stmt.executeQuery();
			while (rset.next()) {
				UploadDocModel model = new UploadDocModel();
				String projectname = rset.getString("XMMC");
				String projectno = rset.getString("XMBH");
				String question = rset.getString("QUESTION");
				String attach = rset.getString("ATTACH");
				List<FileUpload> sublist1 = null;
				List<FileUpload> sublist = FileUploadAPI.getInstance()
						.getFileUploads(attach);
				for (FileUpload f : sublist) {
					String srcName = f.getFileSrcName();

					if (srcName != null && srcName.lastIndexOf(".") >= 0) {
						String extName = srcName.substring(
								srcName.lastIndexOf(".") + 1).toLowerCase(); // 后缀名
						String icon = WebUIUtil.getLinkIcon(extName);
						f.setPrepare2(icon);
					}

				}
				model.setList(sublist);
				model.setProjectName(projectname);
				model.setProjectNo(projectno);
				model.setTaskName(question);
				list.add(model);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List<UploadDocModel> getCommitDocList(String instanceid) {
		List<UploadDocModel> list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT BOTABLE.PROJECTNO,BOTABLE.TASKNO,BOTABLE.CONTENT,BOTABLE.ATTACH,BOTABLE.USERID,BOTABLE.USERNAME,BOTABLE.QUESTIONNO,BOTABLE.CREATEDATE,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_ZQB_RETALK  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=98 and BINDTABLE.metadataid=111 AND BINDTABLE.INSTANCEID=? ORDER BY botable.ID");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, instanceid);
			rset = stmt.executeQuery();
			while (rset.next()) {
				UploadDocModel model = new UploadDocModel();
				String projectno = rset.getString("PROJECTNO");
				String taskno = rset.getString("TASKNO");
				String instanceId = rset.getString("BIND_INSTANCEID");
				String attach = rset.getString("ATTACH");
				List<FileUpload> sublist1 = null;
				List<FileUpload> sublist = FileUploadAPI.getInstance()
						.getFileUploads(attach);
				for (FileUpload f : sublist) {
					String srcName = f.getFileSrcName();

					if (srcName != null && srcName.lastIndexOf(".") >= 0) {
						String extName = srcName.substring(
								srcName.lastIndexOf(".") + 1).toLowerCase(); // 后缀名
						String icon = WebUIUtil.getLinkIcon(extName);
						f.setPrepare2(icon);
					}

				}
				model.setList(sublist);
				model.setProjectName(taskno);
				model.setProjectNo(projectno);
				model.setTaskName(instanceId);
				list.add(model);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	/**
	 * 获得项目组人员列表
	 * 
	 * @return
	 */
	public List<String> getProjectGroupList() {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct userid from orguser where orgroleid = 7 or orgroleid=6");
		List<String> list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String userid = rset.getString("userid");
				list.add(userid);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List getProjectTypeGroup(String userAddress) {
		StringBuffer sql = new StringBuffer();
		sql.append("select XMJD,count(*) num from bd_zqb_pj_base where 1=1");
		if (userAddress != null) {
			sql.append(" AND SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?");
		}
		sql.append("group by XMJD");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			if (userAddress != null) {
				stmt.setString(1, UserContextUtil.getInstance().getCurrentUserId());
				stmt.setString(2, userAddress);
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("XMJD");
				int count = rset.getInt("num");
				ArrayList itemlist = new ArrayList();
				itemlist.add(typename);
				itemlist.add(count);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}

		return list;
	}

	public List getProjectTypeStatus(String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select status,count(*) num from bd_zqb_pj_base ");
		sql.append(" where 1=1  ");
		if (userid != null) {
			sql.append("AND SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?");
		}
		sql.append(" and (typeno='1' and status='已完成') or (xmjd<>'持续督导' and status='执行中') group by status");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			if (userid != null) {
				stmt.setString(1, userid);
				stmt.setString(2, userid);
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("status");
				int count = rset.getInt("num");
				ArrayList itemlist = new ArrayList();
				itemlist.add(typename);
				itemlist.add(count);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}

		return list;
	}

	// 风险
	public List getProjectFengXian(List<String> projectNoList, String userid) {
		StringBuffer sql = new StringBuffer();
		StringBuffer sql1 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		StringBuffer sql3 = new StringBuffer();
		StringBuffer sql4 = new StringBuffer();
		List parameter1=new ArrayList();//存放参数
		List parameter2=new ArrayList();//存放参数
		List parameter3=new ArrayList();//存放参数
		sql1.append("select '关闭项目' as TITLE, count(*) as num from (select projectName, projectNo  from  BD_ZQB_PJ_BASE where status = '已完成' and typeno is null ");
		sql2.append("select  '超期项目' as TITLE,count(*)as num from (select projectName,projectNo  from BD_ZQB_PJ_BASE where (status<>'已完成' and sysdate>enddate)");
		sql3.append("select  '正常项目' as TITLE,count(*)as num from (select projectName,projectNo  from BD_ZQB_PJ_BASE where ( sysdate<enddate) ");
		sql4.append("select '持续督导项目' as TITLE, count(*) as num from (select projectName, projectNo from BD_ZQB_PJ_BASE  where  typeno='1' ) ");
		if (projectNoList != null && projectNoList.size() > 0) {
			sql1.append(" and (1=2 ");
			sql2.append(" and (1=2 ");
			sql3.append(" and (1=2 ");
			for (String projectNo : projectNoList) {
				sql1.append(" or projectno = ? ");
				sql2.append(" or projectno = ? ");
				sql3.append(" or projectno = ? ");
				parameter1.add(projectNo);
				parameter2.add(projectNo);
				parameter3.add(projectNo);
			}
			sql1.append(")) ");
			sql2.append(") ");
			sql3.append(") ");
		} else {
			sql1.append(") ");
		}
		sql2.append(" and status = '执行中'  and xmjd <>'持续督导' group by projectName,projectNo )");
		sql3.append(" and status = '执行中'  and xmjd <>'持续督导' group by projectName,projectNo )");
		sql.append(sql1).append(" union ").append(sql2).append(" union ")
				.append(sql3).append(" union ").append(sql4);
		parameter1.addAll(parameter2);
		parameter1.addAll(parameter3);
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter1.size(); i++) {
				stmt.setString(i+1, parameter1.get(i).toString());
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String title = rset.getString("TITLE");
				int num = rset.getInt("NUM");
				List itemlist = new ArrayList();
				itemlist.add(title);
				itemlist.add(num);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List getFindProjectFengXian(List<String> projectNoList) {
		projectNoList = null;
		StringBuffer sql = new StringBuffer();
		StringBuffer sql1 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		StringBuffer sql3 = new StringBuffer();
		StringBuffer sql4 = new StringBuffer();
		List parameter1=new ArrayList();//存放参数
		List parameter2=new ArrayList();//存放参数
		List parameter3=new ArrayList();//存放参数
		sql1.append("select '关闭项目' as TITLE, count(*) as num from (select projectName, projectNo  from  BD_ZQB_PJ_BASE where status = '已完成' ");
		sql2.append("select  '超期项目' as TITLE,count(*)as num from (select projectName,projectNo  from view_pj_task where (scale<>100 and sysdate>enddate)");
		sql3.append("select  '正常项目' as TITLE,count(*)as num from (select projectName,projectNo  from view_pj_task where ( sysdate<enddate) ");
		sql4.append("select '持续督导项目' as TITLE, count(*) as num from (select projectName, projectNo from BD_ZQB_PJ_BASE  where  typeno='1' ) ");
		if (projectNoList != null && projectNoList.size() > 0) {
			sql1.append(" and (1=2 ");
			sql2.append(" and (1=2 ");
			sql3.append(" and (1=2 ");
			for (String projectNo : projectNoList) {
				sql1.append(" or projectno = ? ");
				sql2.append(" or projectno = ? ");
				sql3.append(" or projectno = ? ");
				parameter1.add(projectNo);
				parameter2.add(projectNo);
				parameter3.add(projectNo);
			}
			sql1.append(")) ");
			sql2.append(") ");
			sql3.append(") ");
		} else {
			sql1.append(") ");
		}

		sql2.append(" and status = '执行中'  and xmjd <>'持续督导' group by projectName,projectNo )");
		sql3.append(" and status = '执行中'  and xmjd <>'持续督导' group by projectName,projectNo )");
		sql.append(sql1).append(" union ").append(sql2).append(" union ")
				.append(sql3).append(" union ").append(sql4);
		parameter1.addAll(parameter2);
		parameter1.addAll(parameter3);
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter1.size(); i++) {
				stmt.setString(i+1, parameter1.get(i).toString());
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String title = rset.getString("TITLE");
				int num = rset.getInt("NUM");
				List itemlist = new ArrayList();
				itemlist.add(title);
				itemlist.add(num);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List<String> getProjectFengXianList(List<HashMap> projectNoList,
			String type) {
		StringBuffer sql = new StringBuffer();
		List parameter=new ArrayList();//存放参数
		sql.append("select projectNo  from bd_pm_task where (scale<>100 and sysdate>enddate-5) and (");
		if (projectNoList != null && projectNoList.size() > 0) {
			for (HashMap pj : projectNoList) {
				if (pj.get("PROJECTNO") != null) {
					sql.append(" projectNo = ? or  ");
					parameter.add(pj.get("PROJECTNO").toString());
				}
			}
			sql.append(" 1=2 ");
		} else {
			sql.append(" 1=1 ");
		}
		sql.append(" ) group by projectNo");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				stmt.setString(i+1, parameter.get(i).toString());
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String title = rset.getString("projectNo");
				list.add(title);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	// 分数
	public List getProjectScore(List<String> projectNoList) {
		StringBuffer sql = new StringBuffer();
		List parameter=new ArrayList();//存放参数
		sql.append("select count(case when FXPGFS between 90 and 100 then 1 end) as A1,count(case when FXPGFS between 80 and 89 then 1 end) as A2,count(case when FXPGFS between 60 and 79 then 1 end) as A3,count(case when FXPGFS<60 then 1 end) as A4 from bd_zqb_pj_base ");
		if (projectNoList != null && projectNoList.size() > 0) {
			sql.append(" where 1=2 ");
			for (String projectNo : projectNoList) {
				sql.append(" or projectno = ? ");
				parameter.add(projectNo);
			}
		}
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				stmt.setString(i+1, parameter.get(i).toString());
			}
			rset = stmt.executeQuery();
			if (rset.next()) {
				int a1 = rset.getInt("A1");
				List itemlist = new ArrayList();
				itemlist.add("90-100分");
				itemlist.add(a1);
				list.add(itemlist);

				int a2 = rset.getInt("A2");
				itemlist = new ArrayList();
				itemlist.add("80-89分");
				itemlist.add(a2);
				list.add(itemlist);

				int a3 = rset.getInt("A3");
				itemlist = new ArrayList();
				itemlist.add("60-79分");
				itemlist.add(a3);
				list.add(itemlist);

				int a4 = rset.getInt("A4");
				itemlist = new ArrayList();
				itemlist.add("60分以下");
				itemlist.add(a4);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List getProjectUserMap(String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select owner, count(*) num from bd_zqb_pj_base ");
		sql.append("group by owner");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("owner");
				int count = rset.getInt("num");
				ArrayList itemlist = new ArrayList();
				itemlist.add(typename);
				itemlist.add(count);

				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}

		return list;
	}

	/**
	 * 按阶段分组
	 * 
	 * @param userids
	 * @return
	 */
	public HashMap getProjectStageGroup(List<String> pjNoList, String userid) {
		HashMap hash = new HashMap();
		StringBuffer sql = new StringBuffer();
		List parameter=new ArrayList();//存放参数
		sql.append("select xmjd,count(*) as num from bd_zqb_pj_base ");
		sql.append("where 1=1 ");
		if (pjNoList != null && pjNoList.size() > 0) {
			sql.append(" and  (1=2 ");
			for (String projectNo : pjNoList) {
				sql.append(" or projectno = ? ");
				parameter.add(projectNo);
			}
			sql.append(")");
		}
		sql.append(" and ((status='已完成' and typeno='1') or status<>'已完成')");
		sql.append("group by xmjd");
		List lablelist = new ArrayList();
		List valuelist = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				stmt.setString(i+1, parameter.get(i).toString());
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("xmjd");
				int count = rset.getInt("num");

				lablelist.add(typename);
				valuelist.add(count);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		hash.put("label", lablelist);
		hash.put("value", valuelist);
		return hash;
	}

	public List getListProjectStageGroup(List<String> pjNoList) {
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		List parameter=new ArrayList();//存放参数
		sql.append("select xmjd,count(*) as num from bd_zqb_pj_base ");
		sql.append("where 1=1 ");
		if (pjNoList != null && pjNoList.size() > 0) {
			sql.append(" and  (1=2 ");
			for (String projectNo : pjNoList) {
				sql.append(" or projectno = ? ");
				parameter.add(projectNo);
			}
			sql.append(")");
		}
		sql.append(" and (status='已完成' and typeno='1' or status<>'已完成')");
		sql.append("group by xmjd");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				stmt.setString(i+1, parameter.get(i).toString());
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("xmjd");
				int count = rset.getInt("num");
				ArrayList itemlist = new ArrayList();
				itemlist.add(typename);
				itemlist.add(count);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List getFindListProjectStageGroup() {
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select xmjd,count(*) as num from bd_zqb_pj_base where 1=1  and (status='已完成' and typeno='1' or status<>'已完成') group by xmjd");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("xmjd");
				int count = rset.getInt("num");
				ArrayList itemlist = new ArrayList();
				itemlist.add(typename);
				itemlist.add(count);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	/**
	 * 按阶段分组
	 * 
	 * @param userids
	 * @return
	 */
	public List<HashMap> getProjectStageList(String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select xmjd, count(*) as num from bd_zqb_pj_base ");
		sql.append(" where 1=1 ");
		if (userid != null) {
			sql.append("AND ( SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?) ");
		}
		sql.append("and (status<>'已完成'  or typeno='1')");
		sql.append("group by xmjd");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			if (userid != null) {
				stmt.setString(1,userid);
				stmt.setString(2,userid);
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String xmjd = rset.getString("xmjd");
				int count = rset.getInt("num");
				HashMap hash = new HashMap();
				hash.put("xmjd", xmjd);
				hash.put("count", count);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	/**
	 * 按阶段分组
	 * 
	 * @param userids
	 * @return
	 */
	public List<HashMap> getProjectManagerUserData() {
		StringBuffer sql = new StringBuffer();
		sql.append("select manager,sum(htje)as htje ,sum(ssje) as ssje from (select  p.projectno,p.projectname,p.htje,s.manager,ssje from bd_zqb_pj_base p inner join (select projectno,manager,sum(ssje) ssje from BD_PM_TASK group by projectno,manager) s on p.projectno = s.projectno) group by manager order by ssje,htje");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String manager = rset.getString("manager");
				Double htje = rset.getDouble("htje");
				Double ssje = rset.getDouble("ssje");
				HashMap hash = new HashMap();
				hash.put("manager", manager);
				hash.put("htje", htje);
				hash.put("ssje", ssje);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	/**
	 * 获得应收账款
	 * 
	 * @param userids
	 * @return
	 */
	public List<HashMap> getYSZKData() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PROJECTNO,PROJECTNAME,SUM(HTJE) HTJE,SUM(NUM) YSKE FROM (select PROJECTNO,PROJECTNAME,HTJE,(HTJE-num)  NUM from (select * from (select  p.projectno,p.projectname,p.htje,ssje num from (select * from bd_zqb_pj_base where status='执行中') p inner join (select projectno,sum(ssje) ssje from BD_PM_TASK group by projectno) s on p.projectno = s.projectno)) order by NUM desc) group by PROJECTNO,PROJECTNAME");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String PROJECTNO = rset.getString("PROJECTNO");
				String PROJECTNAME = rset.getString("PROJECTNAME");
				int htje = rset.getInt("HTJE");
				int yske = rset.getInt("YSKE");
				HashMap hash = new HashMap();
				hash.put("PROJECTNO", PROJECTNO);
				hash.put("PROJECTNAME", PROJECTNAME);
				hash.put("HTJE", htje);
				hash.put("YSKE", yske);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	/**
	 * 获得实收账款
	 * 
	 * @param userids
	 * @return
	 */
	public List<HashMap> getSSZKData() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PROJECTNO,PROJECTNAME,SUM(HTJE) HTJE,SUM(NUM) YSKE FROM (select * from (select  p.projectno,p.projectname,p.htje,ssje num from (select * from bd_zqb_pj_base where status='执行中') p inner join (select projectno,sum(ssje) ssje from BD_PM_TASK group by projectno) s on p.projectno = s.projectno)) group by PROJECTNO,PROJECTNAME");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String PROJECTNO = rset.getString("PROJECTNO");
				String PROJECTNAME = rset.getString("PROJECTNAME");
				Double htje = rset.getDouble("HTJE");
				Double yske = rset.getDouble("YSKE");
				HashMap hash = new HashMap();
				hash.put("PROJECTNO", PROJECTNO);
				hash.put("PROJECTNAME", PROJECTNAME);
				hash.put("HTJE", htje);
				hash.put("YSKE", yske);
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List<HashMap> getTaskMList(String groupid) {
		List list = new ArrayList();
		if(!"0".equals(groupid)){
			StringBuffer sql = new StringBuffer();
			sql.append("select jdmc from bd_zqb_km_info where id=?");
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rset = null;
			try {
				conn = DBUtil.open();
				stmt =conn.prepareStatement(sql.toString());
				stmt.setString(1,groupid);
				rset = stmt.executeQuery();
				while (rset.next()) {
					String jdmc = rset.getString("JDMC");
					HashMap hash = new HashMap();
					hash.put("JDMC", jdmc);
					list.add(hash);
				}
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				DBUtil.close(conn, stmt, rset);
			}
		}
		return list;
	}

	/**
	 * 得到人员姓名和项目角色
	 * 
	 * @param userid
	 * @return
	 */
	public List getUserJs(String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select NAME, POSITION  from BD_ZQB_GROUP  ");
		if (userid != null) {
			sql.append(" where 1=1 AND (POSITION IS NULL OR POSITION IS NOT NULL) ");
			sql.append("AND userid=?");
		}
		sql.append(" order by name");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			if (userid != null) {
				stmt.setString(1, userid);
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("POSITION");
				String name = rset.getString("NAME");
				ArrayList itemlist = new ArrayList();
				itemlist.add(typename);
				itemlist.add(name);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}

		return list;
	}

	public boolean getNumber(HashMap map) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) c from bd_pm_task  ");
		sql.append(" where  projectno=?");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		int c = 0;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, map.get("PROJECTNO").toString());
			rset = stmt.executeQuery();
			rset.next();
			c = rset.getInt("c");
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}

		return c > 0;

	}

	/**
	 * 判断是否为超期项目
	 * 
	 * @param projectNo
	 * @return
	 */
	public boolean isProjectCQXM(String projectNo) {
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select count(*) NUM  from BD_ZQB_PJ_BASE where (status<>'已完成' and sysdate>enddate)");
		if (!projectNo.equals("")) {
			sql2.append(" and (1=2 ");
			sql2.append(" or projectno = ? ");
			sql2.append(") ");
		}
		sql2.append(" and status = '执行中'  and xmjd <>'持续督导' group by projectName,projectNo ");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql2.toString());
			if (!projectNo.equals("")) {
				stmt.setString(1, projectNo);
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				int num = rset.getInt("NUM");
				return num > 0;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return false;
	}

	public boolean isProjectZCXM(String projectNo) {
		StringBuffer sql2 = new StringBuffer();
		sql2.append("select count(*) NUM  from bd_zqb_pj_base where ( sysdate<enddate)");
		if (!projectNo.equals("")) {
			sql2.append(" and (1=2 ");
			sql2.append(" or projectno = ? ");
			sql2.append(") ");
		}

		sql2.append(" and status = '执行中'  and xmjd <>'持续督导' group by projectName,projectNo ");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql2.toString());
			if (!projectNo.equals("")) {
				stmt.setString(1, projectNo);
			}
			rset = stmt.executeQuery();
			while (rset.next()) {
				int num = rset.getInt("NUM");
				return num > 0;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return false;
	}

	public List getWanChengLv(List<String> pjNoList) {
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select status,count(*) as num from bd_zqb_pj_base group by status");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				String typename = rset.getString("status");
				int count = rset.getInt("num");
				ArrayList itemlist = new ArrayList();
				itemlist.add(typename);
				itemlist.add(count);
				list.add(itemlist);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List getChiXU() {
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CUSTOMERNO as KHBH, CUSTOMERNAME as KHMC  FROM  BD_ZQB_KH_BASE WHERE YGP='已挂牌'");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			rset = stmt.executeQuery();
			while (rset.next()) {
				HashMap hash = new HashMap();
				hash.put("KHMC", rset.getString("KHMC"));
				hash.put("KHBH", rset.getObject("KHBH"));
				list.add(hash);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}

	public List<HashMap> getRunProjectList1(int pageNow, int pageSize,
			String projectName, String xmjd, String startDate,
			String customername,String dgzt, String sssyb, String cyrName) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		final String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(cyrName!=null&&!cyrName.equals("")){
			map.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			map.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			map.put("createuserid", "USERID");
			map.put("userid", "USERID");
			map.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", "USERID");
			parameter = getCyrUserMap(map,cyrName);
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		if(cyrName!=null&&!cyrName.equals("")){
			sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id ) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(") union all");
			// if 按多项目组审批
			if (config.equals("2")) {
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				// else
			} else {
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J");
		params.add(startRow1);
		params.add(pageSize1);
		if(cyrName!=null&&!cyrName.equals("")){
			sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
					String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append(") or createuserid in (");
					String[] createuserids = parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
					String[] managers1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
					String[] userids = parameter.get("userid").split(",");
					for (int i = 0; i < userids.length; i++) {
						if(i==(userids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids[i].replaceAll("'", ""));
					}
					sb.append(")");

			// 分派项目审批人
			sb.append(" union all");
			// if 按多项目组审批
			if (config.equals("2")) {
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
			} else {
				// else
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzrs = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzrs.length; i++) {
							if(i==(csfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzrs = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzrs.length; i++) {
							if(i==(fsfzrs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzrs[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzsprs = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzsprs.length; i++) {
							if(i==(zzsprs.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzsprs[i].replaceAll("'", ""));
						}
						sb.append("))");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO ");
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
			
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid order by J.ID DESC,J.groupid");
		params.add(startRow1);
		params.add(pageSize1);
		params.add(userid);
		final String sql1 = sb.toString();
		final List param = params;
		final String jd3=SystemConfig._xmsplcConf.getJd3();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					Double htje = ((BigDecimal) object[5]).doubleValue();
					Double wsje = 0.0;
					if (object[6] != null) {wsje = ((BigDecimal) object[6]).doubleValue();}
					Double ssje = 0.0;
					if (object[9] != null) {ssje = ((BigDecimal) object[9]).doubleValue();}
					String rwjd = (String) object[10];
					String spzt = (String) object[12];
					String manager1 = (String) object[13];
					Integer pjinsid = 0;
					if (object[24] != null) {pjinsid = Integer.valueOf(object[24].toString());}
					Integer xgwtinsid = 0;
					if (object[15] != null) {xgwtinsid = Integer.valueOf(object[15].toString());}				
					Integer instanceid = Integer.valueOf(object[16].toString());
					String xmjd = object[7] == null ? "" : object[7].toString();
					String projectNo = object[1] == null ? "" : object[1].toString();
					String groupid = object[8] == null ? "" : object[8].toString();
					String pj = object[17] == null ? "" : object[17].toString();
					Integer num = Integer.valueOf(object[25].toString());
					String lcbh = object[18] == null ? "" : object[18].toString();
					String stepid = object[20] == null ? "" : object[20].toString();
					String xmspzt = object[22] == null ? "" : object[22].toString();
					Integer lcbs=object[19]==null?0:Integer.valueOf(object[19].toString());
					Integer taskid=object[21]==null?0:Integer.valueOf(object[21].toString());
					if(lcbs!=null&&!lcbs.equals("")&&lcbs!=0){ 
						if(lcbh!=null){
							try{
						List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs.toString()));
					    if(pro!=null&pro.size()>0){
					    	Long prc=pro.get(0).getPrcDefId();
					    	map.put("PRCID", prc);
					    }
							}catch(Exception e){
								logger.error(e,e);
							}
						}
					}
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
					map.put("ISSTEPID",jd3.equals(stepid)?true:false);
					map.put("TASKID", taskid);
					map.put("XMSPZT", xmspzt);
		
					map.put("OWNER", owner);
					map.put("MANAGER", manager);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("PJ", pj);				
					map.put("CUSTOMERNAME", customername);
					map.put("HTJE", htje);
					map.put("SSJE", ssje);
					map.put("WSJE", wsje);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					l.add(map);
				}
				return l;
			}
		});
	}


	public List<HashMap> getFinishProjectList(int pageNow, int pageSize,
			String projectName, String xmjd, String startDate,
			String customername) {
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params  = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT H.*, I.NUM FROM (SELECT J.* from (SELECT A.CUSTOMERNAME,A.OWNER,NVL(A.HTJE, 0),(NVL(A.HTJE, 0) - NVL(D.SSJE, 0)) WSJE,E.SSJE,"
				+ "F.jdmc TASK_NAME,E.SPZT,E.MANAGER,E.PJINSID,E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,A.XMJD,A.PROJECTNO,E.GROUPID, nvl(g.ldpj,'评价') ldpj"
				+ " FROM BD_ZQB_PJ_BASE A,    SYS_ENGINE_FORM_BIND BINDTABLE,(SELECT B.PROJECTNO, SUM(NVL(B.SSJE, 0)) SSJE"
				+ " FROM BD_PM_TASK B, BD_ZQB_PJ_BASE C  WHERE B.PROJECTNO(+) = C.PROJECTNO GROUP BY B.PROJECTNO) D,BD_PM_TASK E,BD_ZQB_KM_INFO  F,( select b.projectno,b.groupid,a.ldpj from  	BD_ZQB_XMRWPJB  a, BD_PM_TASK b where a.projectno(+)=b.projectno and a.groupid(+)=b.groupid) g"
				+ " WHERE A.PROJECTNO = D.PROJECTNO(+) AND E.GROUPID=F.ID(+) AND g.PROJECTNO(+) = A.PROJECTNO and( g.groupid = e.groupid or g.groupid is null)   AND (F.jdmc like '%持续督导%' or F.jdmc is null)"
				+ " AND E.PROJECTNO(+) = A.PROJECTNO AND  A.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91"
				+ " and BINDTABLE.metadataid = 101");

		sb.append(" AND A.STATUS = '执行中'");
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND A.PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if (!"".equals(startDate) && startDate != null) {
			sb.append(" AND STARTDATE = to_date(?,'yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" ORDER BY A.customername DESC) J WHERE RN > ? AND RN <= ?) H,(SELECT G.CUSTOMERNAME, COUNT(*) NUM FROM (SELECT A.CUSTOMERNAME,"
				+ "A.OWNER,NVL(A.HTJE, 0),E.SSJE,E.TASK_NAME,E.SPZT,E.MANAGER,E.PJINSID,E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN FROM BD_ZQB_PJ_BASE A,SYS_ENGINE_FORM_BIND BINDTABLE,"
				+ "BD_PM_TASK E WHERE E.PROJECTNO(+) = A.PROJECTNO AND A.STATUS = '执行中' AND A.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91"
				+ " and BINDTABLE.metadataid = 101");
		params.add(startRow1);
		params.add(pageNow*pageSize);

		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND A.PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" AND A.TYPENO is null ");
		} else {
			sb.append(" AND A.TYPENO is null AND A.XMJD like ?");
			params.add("%" + xmjd + "%");
		}
		if (!"".equals(startDate) && startDate != null) {
			sb.append(" AND A.STARTDATE = to_date(?,'yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND A.CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" ORDER BY A.customername DESC) G WHERE RN > ? AND RN <= ? GROUP BY G.CUSTOMERNAME) I WHERE H.CUSTOMERNAME = I.CUSTOMERNAME order by h.customername,h.groupid");
		params.add(startRow1);
		params.add(pageNow*pageSize);
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				// String rwuuid="b25ca8ed0a5a484296f2977b50db8396";
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[0];
					String owner = (String) object[1];
					Double htje = ((BigDecimal) object[2]).doubleValue();
					Double wsje = 0.0;
					if (object[3] != null) {
						wsje = ((BigDecimal) object[3]).doubleValue();
					}
					Double ssje = 0.0;
					if (object[4] != null) {
						ssje = ((BigDecimal) object[4]).doubleValue();
					}
					String rwjd = (String) object[5];
					String spzt = (String) object[6];
					String manager1 = (String) object[7];
					Integer pjinsid = 0;
					if (object[8] != null) {
						pjinsid = Integer.valueOf(object[8].toString());
					}
					Integer xgwtinsid = 0;
					if (object[9] != null) {
						xgwtinsid = Integer.valueOf(object[9].toString());
					}
					Integer instanceid = Integer.valueOf(object[10].toString());
					String xmjd = object[12] == null ? "" : object[12]
							.toString();
					String projectNo = object[13] == null ? "" : object[13]
							.toString();
					String groupid = object[14] == null ? "" : object[14]
							.toString();
					String pj = object[15] == null ? "" : object[15].toString();
					Integer num = Integer.valueOf(object[16].toString());
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("PJ", pj);
					map.put("CUSTOMERNAME", customername);
					map.put("HTJE", htje);
					map.put("SSJE", ssje);
					map.put("WSJE", wsje);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getCloseProjectList(int pageNow, int pageSize,
			String projectName, String xmjd, String startDate,
			String customername,String dgzt, HashMap<String,List<String>> parameterMap, String sssyb, String cyrName, Long ismanager) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		Long orgRoleId = userModel.getOrgroleid();
		if (uc != null) {
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
				flag = true;
			}
		}
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		final String userid=userModel.getUserid().toString().trim();
		String username=userModel.getUserid();//getUsername().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();//2015-11-27修改SQL，将阶段负责人（E.MANAGER）换成所属事业部（A.SSSYB）
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		map.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
		map.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
		map.put("createuserid", "USERID");
		map.put("userid", "USERID");
		map.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", "USERID");
		map.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", "USERID");
		map.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", "USERID");
		if(ismanager==1){
			if(cyrName!=null&&!cyrName.equals("")){
				parameter = getCyrUserMap(map,cyrName);
			}else{
				parameter = getParameterMap(map,parameterMap);
			}
		}
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		if(!flag){
			if(ismanager==1){
				sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(") or createuserid in (");
				String[] createuserid = parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager1.length; i++) {
					if(i==(manager1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager1[i].replaceAll("'", ""));
				}
				sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
				String[] userids = parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(") union all");
						//if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						}else{
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append("))");
						}
						if(orgRoleId==11){
							sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}else{
				sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ? union all");
				params.add(owner);
				params.add(owner);
				params.add(username);
				params.add(owner);
				params.add(userid);
						//if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}else{
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}
						if(orgRoleId==11){
							sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}else{
			if(cyrName!=null&&!cyrName.equals("")){
				parameter = getCyrUserMap(map,cyrName);
				sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owner1 = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owner1.length; i++) {
					if(i==(owner1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owner1[i].replaceAll("'", ""));
				}
				sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(") or createuserid in (");
				String[] createuserid = parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager1.length; i++) {
					if(i==(manager1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager1[i].replaceAll("'", ""));
				}
				sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id ) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
				String[] userid1 = parameter.get("userid").split(",");
				for (int i = 0; i < userid1.length; i++) {
					if(i==(userid1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userid1[i].replaceAll("'", ""));
				}
						sb.append(")"
						//分派项目审批人
			          	+ " union all");
						//if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						}else{
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append("))");
						}
						if(orgRoleId==11){
							sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' AND A.TYPENO is null ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
		params.add(startRow1);
		params.add(pageSize1);
		if(!flag){
			if(ismanager==1){
				sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owner1 = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owner1.length; i++) {
					if(i==(owner1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owner1[i].replaceAll("'", ""));
				}
				sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(") or createuserid in (");
				String[] createuserid = parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager1.length; i++) {
					if(i==(manager1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager1[i].replaceAll("'", ""));
				}
				sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
				String[] userid1 = parameter.get("userid").split(",");
				for (int i = 0; i < userid1.length; i++) {
					if(i==(userid1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userid1[i].replaceAll("'", ""));
				}
				sb.append(") union all");
						//if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						}else{
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append("))");
						}
						if(orgRoleId==11){
							sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}else{
				sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ? union all");
				params.add(owner);
				params.add(owner);
				params.add(username);
				params.add(owner);
				params.add(userid);
						//if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}else{
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}
						if(orgRoleId==11){
							sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}else{
			if(cyrName!=null&&!cyrName.equals("")){
				sb.append("INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
				String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(") or createuserid in (");
				String[] createuserid = parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager1.length; i++) {
					if(i==(manager1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager1[i].replaceAll("'", ""));
				}
				sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
				String[] userid1 = parameter.get("userid").split(",");
				for (int i = 0; i < userid1.length; i++) {
					if(i==(userid1.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userid1[i].replaceAll("'", ""));
				}
				sb.append(") union all");
						//if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						}else{
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
						String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < csfzr.length; i++) {
							if(i==(csfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(csfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
						String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < fsfzr.length; i++) {
							if(i==(fsfzr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(fsfzr[i].replaceAll("'", ""));
						}
						sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
						String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < zzspr.length; i++) {
							if(i==(zzspr.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(zzspr[i].replaceAll("'", ""));
						}
						sb.append("))");
						}
						if(orgRoleId==11){
							sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
			params.add("%" + customername + "%");
		}
		if(sssyb!=null&&!sssyb.equals("")){
			sb.append(" AND SSSYB like ? ");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
			
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='已完成' AND A.TYPENO is null ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid order by J.ID DESC,J.groupid");
		params.add(startRow1);
		params.add(pageSize1);
		params.add(userid);
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[2];
					String owner = (String) object[3];
					String manager = (String) object[4];
					Double htje = ((BigDecimal) object[5]).doubleValue();
					Double wsje = 0.0;
					if (object[6] != null) {
						wsje = ((BigDecimal) object[6]).doubleValue();
					}
					Double ssje = 0.0;
					if (object[9] != null) {
						ssje = ((BigDecimal) object[9]).doubleValue();
					}
					String rwjd = (String) object[10];
					String spzt = (String) object[12];
					String manager1 = (String) object[13];
					Integer pjinsid = 0;
					if (object[24] != null) {
						pjinsid = Integer.valueOf(object[24].toString());
					}
					Integer xgwtinsid = 0;
					if (object[15] != null) {
						xgwtinsid = Integer.valueOf(object[15].toString());
					}
					Integer instanceid = Integer.valueOf(object[16].toString());
					String xmjd = object[7] == null ? "" : object[7]
							.toString();
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					String groupid = object[8] == null ? "" : object[8]
							.toString();
					String pj = object[17] == null ? "" : object[17].toString();
					Integer num = Integer.valueOf(object[25].toString());
					String lcbh = object[18] == null ? "" : object[18]
							.toString();
					String stepid = object[20] == null ? "" : object[20]
							.toString();
					String xmspzt = object[22] == null ? "" : object[22]
							.toString();
					Integer lcbs=object[19]==null?0:Integer.valueOf(object[19].toString());
					Integer taskid=object[21]==null?0:Integer.valueOf(object[21].toString());
					if(lcbs!=null&&!lcbs.equals("")&&lcbs!=0){ 
						if(lcbh!=null){
							try{
						List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs.toString()));
					    if(pro!=null&pro.size()>0){
					    	Long prc=pro.get(0).getPrcDefId();
					    	map.put("PRCID", prc);
					    }
							}catch(Exception e){
								logger.error(e,e);
							}
						}
					}
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
					map.put("TASKID", taskid);
					map.put("XMSPZT", xmspzt);
					map.put("OWNER", owner);
					map.put("MANAGER", manager);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("CUSTOMERNAME", customername);
					map.put("HTJE", htje);
					map.put("SSJE", ssje);
					map.put("WSJE", wsje);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getCloseProjectListSize(String projectName,
			String xmjd, String startDate, String customername) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.MANAGER,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101  AND STATUS = '已完成'");
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if (!"".equals(xmjd) && xmjd != null) {
			sb.append(" AND XMJD like ?");
			params.add("%" + xmjd + "%");
		}
		if (!"".equals(startDate) && startDate != null) {
			sb.append(" AND STARTDATE = to_date(?,'yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" ORDER BY botable.ID desc");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String owner = (String) object[4];
					String projectNo = (String) object[3];
					Integer instanceid = Integer.valueOf(object[36].toString());
					String xmjd = (String) object[14];
					String projectName = (String) object[2];
					String manager = (String) object[5];
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("PROJECTNAME", projectName);
					map.put("MANAGER", manager);
					String customername = (String) object[9];
					map.put("CUSTOMERNAME", customername);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getRunProjectListSize1(String projectName,
			String xmjd, String startDate, String customername) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT A.*,B.TASK_NAME,B.MANAGER FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,nvl(BOTABLE.htje,0) htje,sum(nvl(ssje,0)) ssje, nvl(BOTABLE.htje,0)- sum(nvl(ssje,0)) wsje,count(*) num FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101  AND STATUS = '执行中'");
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" AND TYPENO is null");
		} else {
			sb.append(" AND TYPENO is null AND XMJD like ?");
			params.add("%" + xmjd + "%");
		}
		if (!"".equals(startDate) && startDate != null) {
			sb.append(" AND STARTDATE = to_date(?,'yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" inner join BD_PM_TASK c on (c.projectno=BOTABLE.projectno or c.projectno is null) group by BOTABLE.CREATEUSER, BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID, nvl(BOTABLE.htje,0),nvl(BOTABLE.htje,0)) A,BD_PM_TASK B WHERE A.PROJECTNO=B.PROJECTNO OR  B.PROJECTNO IS NULL ORDER BY A.ID desc");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String owner = (String) object[4];
					String projectNo = (String) object[3];
					Integer instanceid = Integer.valueOf(object[36].toString());
					Double htje = ((BigDecimal) object[37]).doubleValue();
					Double ssje = ((BigDecimal) object[38]).doubleValue();
					Double wsje = ((BigDecimal) object[39]).doubleValue();
					Integer num = Integer.valueOf(object[40].toString());
					String rwjd = (String) object[41];
					String manager1 = (String) object[42];
					String xmjd = (String) object[14];
					String projectName = (String) object[2];
					// String manager = (String) object[5];
					String customername = (String) object[9];
					String customerno = (String) object[10];
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("PROJECTNAME", projectName);
					// map.put("MANAGER", manager);
					map.put("CUSTOMERNO", customerno);
					map.put("CUSTOMERNAME", customername);
					map.put("HTJE", htje);
					map.put("SSJE", ssje);
					map.put("WSJE", wsje);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getRunProjectListSize2(String owner,
			String projectName, String xmjd, String startDate,
			String customername) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT A.*,B.TASK_NAME,B.MANAGER FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,nvl(BOTABLE.htje,0) htje,sum(nvl(ssje,0)) ssje,nvl(BOTABLE.htje,0)-sum(nvl(ssje,0)) wsje,count(*) num,BOTABLE.manager cwjl FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101  AND STATUS = '执行中'");
		sb.append(" AND (SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?)");
		params.add(owner);
		params.add(owner);
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" AND TYPENO is null");
		} else {
			sb.append(" AND TYPENO is null AND XMJD like ?");
			params.add("%" + xmjd + "%");
		}
		if (!"".equals(startDate) && startDate != null) {
			sb.append(" AND STARTDATE = to_date('" + startDate + "','yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" inner join BD_PM_TASK c on (c.projectno=BOTABLE.projectno or c.projectno is null) group by BOTABLE.CREATEUSER, BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,BOTABLE.manager,nvl(BOTABLE.htje,0)) A,BD_PM_TASK B    WHERE A.PROJECTNO=B.PROJECTNO OR  B.PROJECTNO IS NULL ORDER BY A.ID desc");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String owner = (String) object[4];
					String projectNo = (String) object[3];
					Integer instanceid = Integer.valueOf(object[36].toString());
					Double htje = ((BigDecimal) object[37]).doubleValue();
					Double ssje = ((BigDecimal) object[38]).doubleValue();
					Double wsje = ((BigDecimal) object[39]).doubleValue();
					Integer num = Integer.valueOf(object[40].toString());
					String cwjl = (String) object[41];
					String rwjd = (String) object[42];
					String manager1 = (String) object[43];
					String xmjd = (String) object[14];
					String projectName = (String) object[2];
					// String manager = (String) object[5];
					String customername = (String) object[9];
					String customerno = (String) object[10];
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("PROJECTNAME", projectName);
					// map.put("MANAGER", manager);
					map.put("CUSTOMERNO", customerno);
					map.put("CUSTOMERNAME", customername);
					map.put("MANAGER", cwjl);
					map.put("HTJE", htje);
					map.put("SSJE", ssje);
					map.put("WSJE", wsje);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getRunProjectList2(String owner, int pageNow,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt, HashMap<String,List<String>> parameterMap, String sssyb, String cyrName, Long ismanager) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		final String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		String username=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();//getUsername()
				//.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();//2015-11-27修改SQL，将阶段负责人（E.MANAGER）换成所属事业部（A.SSSYB）
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			map.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
			map.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
			map.put("createuserid", "USERID");
			map.put("userid", "USERID");
			map.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", "USERID");
			if(cyrName!=null&&!cyrName.equals("")){
				parameter = getCyrUserMap(map, cyrName);
			}else{
				parameter = getParameterMap(map,parameterMap);
			}
		}
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();
		if(ismanager==1){
			sb.append("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
			String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
			for (int i = 0; i < owners.length; i++) {
				if(i==(owners.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
			String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < manager.length; i++) {
				if(i==(manager.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(manager[i].replaceAll("'", ""));
			}
			sb.append(") or createuserid in (");
			String[] createuserid = parameter.get("createuserid").split(",");
			for (int i = 0; i < createuserid.length; i++) {
				if(i==(createuserid.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserid[i].replaceAll("'", ""));
			}
			sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
			String[] manager1 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < manager1.length; i++) {
				if(i==(manager1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(manager1[i].replaceAll("'", ""));
			}
			sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
			String[] userid1 = parameter.get("userid").split(",");
			for (int i = 0; i < userid1.length; i++) {
				if(i==(userid1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid1[i].replaceAll("'", ""));
			}
			sb.append(")"
					//分派项目审批人
					+ " union all");
			//if 按多项目组审批
			if(config.equals("2")){
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < csfzr.length; i++) {
					if(i==(csfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < fsfzr.length; i++) {
					if(i==(fsfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < zzspr.length; i++) {
					if(i==(zzspr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(zzspr[i].replaceAll("'", ""));
				}
				sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				//else
			}else{
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < csfzr.length; i++) {
					if(i==(csfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < fsfzr.length; i++) {
					if(i==(fsfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < zzspr.length; i++) {
					if(i==(zzspr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(zzspr[i].replaceAll("'", ""));
				}
				sb.append("))");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			//WHERE CUSTOMERNAME LIKE '%%'
			sb.append(" WHERE 1=1 ");
			if (!"".equals(customername) && customername != null) {
				sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
				params.add("%" + customername + "%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND SSSYB like '%" + sssyb + "%' ");
				params.add("%" + sssyb + "%");
			}
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%" + sssyb + "%");
			}
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) in (");
			params.add(startRow1);
			params.add(pageSize1);
			String[] owner1 = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
			for (int i = 0; i < owner1.length; i++) {
				if(i==(owner1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owner1[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
			String[] manager2 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < manager2.length; i++) {
				if(i==(manager2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(manager2[i].replaceAll("'", ""));
			}
			sb.append(") or createuserid in (");
			String[] createuserid1 = parameter.get("createuserid").split(",");
			for (int i = 0; i < createuserid1.length; i++) {
				if(i==(createuserid1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserid1[i].replaceAll("'", ""));
			}
			sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
			String[] manager3 = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < manager3.length; i++) {
				if(i==(manager3.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(manager3[i].replaceAll("'", ""));
			}
			sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid in (");
			String[] userid2 = parameter.get("userid").split(",");
			for (int i = 0; i < userid2.length; i++) {
				if(i==(userid2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid2[i].replaceAll("'", ""));
			}
			sb.append(")");
			
			//分派项目审批人
			sb.append(" union all");
			//if 按多项目组审批
			if(config.equals("2")){
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < csfzr.length; i++) {
					if(i==(csfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < fsfzr.length; i++) {
					if(i==(fsfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < zzspr.length; i++) {
					if(i==(zzspr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(zzspr[i].replaceAll("'", ""));
				}
				sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
			}else{
				//else
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzr = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < csfzr.length; i++) {
					if(i==(csfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzr = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < fsfzr.length; i++) {
					if(i==(fsfzr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzr[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzspr = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < zzspr.length; i++) {
					if(i==(zzspr.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(zzspr[i].replaceAll("'", ""));
				}
				sb.append("))");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			// WHERE CUSTOMERNAME LIKE '%%'
			sb.append(" WHERE 1=1 ");
			if (!"".equals(customername) && customername != null) {
				sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
				params.add("%" + customername + "%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND SSSYB like ? ");
				params.add("%" + sssyb + "%");
			}
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
			
			// WHERE JDMC LIKE '%股改%'
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%" + xmjd + "%");
			}
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
			params.add(startRow1);
			params.add(pageSize1);
		}else{
			sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ? union all");
			params.add(owner);
			params.add(owner);
			params.add(username);
			params.add(owner);
			params.add(userid);
			//if 按多项目组审批
			if(config.equals("2")){
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = '"+owner+"') B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				params.add(owner);
				params.add(owner);
				params.add(owner);
				//else
			}else{
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
				params.add(owner);
				params.add(owner);
				params.add(owner);
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			//WHERE CUSTOMERNAME LIKE '%%'
			sb.append(" WHERE 1=1 ");
			if (!"".equals(customername) && customername != null) {
				sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
				params.add("%" + customername + "%");
			}
			
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%" + xmjd + "%");
			}
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ?");
			params.add(startRow1);
			params.add(pageSize1);
			params.add(owner);
			params.add(owner);
			params.add(username);
			params.add(owner);
			params.add(userid);
			//分派项目审批人
			sb.append(" union all");
			//if 按多项目组审批
			if(config.equals("2")){
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				params.add(owner);
				params.add(owner);
				params.add(owner);
			}else{
				//else
					sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			// WHERE CUSTOMERNAME LIKE '%%'
			sb.append(" WHERE 1=1 ");
			if (!"".equals(customername) && customername != null) {
				sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
				params.add("%" + customername + "%");
			}
			
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
			
			// WHERE JDMC LIKE '%股改%'
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%" + xmjd + "%");
			}
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid order by J.ID DESC,J.groupid");
			params.add(startRow1);
			params.add(pageSize1);
			params.add(userid);
		}
		final String sql1 = sb.toString();
		final List param = params;
		final String jd3=SystemConfig._xmsplcConf.getJd3();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[2];
					String owner = (String) object[3];
					Double htje = ((BigDecimal) object[4]).doubleValue();
					Double wsje = 0.0;
					if (object[5] != null) {
						wsje = ((BigDecimal) object[5]).doubleValue();
					}
					Double ssje = 0.0;
					if (object[8] != null) {
						ssje = ((BigDecimal) object[8]).doubleValue();
					}
					String rwjd = (String) object[9];
					String spzt = (String) object[11];
					String manager1 = (String) object[12];
					Integer pjinsid = 0;
					if (object[23] != null) {
						pjinsid = Integer.valueOf(object[23].toString());
					}
					Integer xgwtinsid = 0;
					if (object[14] != null) {
						xgwtinsid = Integer.valueOf(object[14].toString());
					}
					Integer instanceid = Integer.valueOf(object[15].toString());
					String xmjd = object[6] == null ? "" : object[6]
							.toString();
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					String groupid = object[7] == null ? "" : object[7]
							.toString();
					String pj = object[16] == null ? "" : object[16].toString();
					Integer num = Integer.valueOf(object[24].toString());
					String lcbh = object[17] == null ? "" : object[17]
							.toString();
					String stepid = object[19] == null ? "" : object[19]
							.toString();
					String xmspzt = object[21] == null ? "" : object[21]
							.toString();
					Integer lcbs=object[18]==null?0:Integer.valueOf(object[18].toString());
					Integer taskid=object[20]==null?0:Integer.valueOf(object[20].toString());
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
					map.put("ISSTEPID", jd3.equals(stepid)?true:false);
					map.put("TASKID", taskid);
					map.put("XMSPZT", xmspzt);
					
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("CUSTOMERNAME", customername);
					map.put("HTJE", htje);
					map.put("SSJE", ssje);
					map.put("WSJE", wsje);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("PJ", pj);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					l.add(map);
				}
				return l;
			}
		});
	}


	public List<HashMap> getFinishProjectListSize(String projectName,
			String xmjd, String startDate, String customername) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.MANAGER,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101");
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" AND typeno='1'");
		} else {
			sb.append(" AND XMJD like ?");
			params.add("%" + xmjd + "%");
		}
		if (!"".equals(startDate) && startDate != null) {
			sb.append(" AND STARTDATE = to_date(?,'yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" ORDER BY botable.ID desc");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String owner = (String) object[4];
					String projectNo = (String) object[3];
					Integer instanceid = Integer.valueOf(object[36].toString());
					String xmjd = (String) object[14];
					String projectName = (String) object[2];
					String manager = (String) object[5];
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("PROJECTNAME", projectName);
					map.put("MANAGER", manager);
					String customername = (String) object[9];
					map.put("CUSTOMERNAME", customername);
					l.add(map);
				}
				return l;
			}
		});
	}

	/**
	 * 获取项目信息记录
	 * 
	 * @param sql
	 * @return
	 */
	public List<HashMap<String, Object>> getListSize(final String sql,final List params) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
						String param=params.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(param)){
							return l;
						}
						}
					query.setParameter(i, params.get(i));
				}
				List<Object[]> list = query.list();
				for (Object[] object : list) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					String name = (String) object[0];
					BigDecimal instanceid = (BigDecimal) object[1];
					String xmjd = (String) object[2];
					String projectno = (String) object[3];
					String owner = (String) object[4];
					String manager = (String) object[5];
					map.put("PROJECTNAME", name);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("PROJECTNO", projectno);
					map.put("OWNER", owner);
					map.put("MANAGER", manager);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	/**
	 * 获取项目信息记录
	 * 
	 * @param startRow
	 * @param pageSize
	 * @param sql
	 * @return
	 */
	public List<HashMap<String, Object>> getList(final int startRow,
			final int pageSize, final String sql,final List params) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
						String param=params.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(param)){
							return l;
						}
						}
					query.setParameter(i, params.get(i));
				}
				query.setFirstResult(startRow);
				query.setMaxResults(pageSize);
				List<Object[]> list = query.list();
				for (Object[] object : list) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					String name = (String) object[0];
					BigDecimal instanceid = (BigDecimal) object[1];
					String xmjd = (String) object[2];
					String projectno = (String) object[3];
					String owner = (String) object[4];
					String manager = (String) object[5];
					map.put("PROJECTNAME", name);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("PROJECTNO", projectno);
					map.put("OWNER", owner);
					map.put("MANAGER", manager);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap<String, Object>> getListCYR(final int startRow,
			final int pageSize, final String sql,final List params) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < params.size(); i++) {
					if(params.get(i)!=null && !"".equals(params.get(i).toString())){
						String param=params.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(param)){
							return l;
						}
						}
					query.setParameter(i, params.get(i));
				}
				query.setFirstResult(startRow);
				query.setMaxResults(pageSize);
				List<Object[]> list = query.list();
				for (Object[] object : list) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					String name = (String) object[0];
					BigDecimal instanceid = (BigDecimal) object[1];
					String xmjd = (String) object[2];
					String projectno = (String) object[3];
					String owner = (String) (object[4] != null ? object[4] : "");
					map.put("PROJECTNAME", name);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("PROJECTNO", projectno);
					map.put("NAME", owner);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getXMCYList(String userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select username,officetel,officefax,mobile,email from orguser where userid = ? order by id desc");
		List<HashMap> list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			rset = stmt.executeQuery();
			while (rset.next()) {
				HashMap map = new HashMap();
				map.put("NAME", rset.getString("username"));
				map.put("TEL", rset.getString("officetel"));
				map.put("PHONE", rset.getString("mobile"));
				map.put("EMAIL", rset.getString("email"));
				map.put("FAX", rset.getString("officefax"));
				list.add(map);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return list;
	}
	
	public List<HashMap> expProjectList1() {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();

		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma,A.SSSYB sssyb,to_char(A.CLSLR,'yyyy-MM-dd') clslr,to_char(A.SHTGR,'yyyy-MM-dd') shtgr,A.GZLXR gzlxr,A.GZLXDH gzlxdh,A.GZYJDZ gzyjdz,to_char(A.YJSBSJ,'yyyy-MM-dd') yjsbsj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno ) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);		
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				// String rwuuid="b25ca8ed0a5a484296f2977b50db8396";
				for (Object[] object : list) {
					map = new HashMap();
					String projectno=(String) object[1];
					String customername=(String) object[2];
					String owner=(String) object[3];
					Double htje=((BigDecimal) object[4]).doubleValue();
					Integer groupid=object[7]==null?0:Integer.valueOf(object[7].toString());
					Double ssje=0.0;
					if (object[8] != null) {
						ssje = ((BigDecimal) object[8]).doubleValue();
					}
					String jdmc=(String) object[9];
					String spzt=(String) object[11];
					String manager=(String) object[12];
					BigDecimal instanceid= (BigDecimal) object[15];
					long lcid = instanceid.longValue();
					String pjjg=object[16] == null ? "" : object[16].toString();
					
					String startdate=(String) object[17];
					String enddate=(String) object[18];
					String khlxr=(String) object[19];
					String khlxdh=(String) object[20];
					String ggjzr=(String) object[21];
					String sbjzr=(String) object[22];
					String memo=(String) object[23];
					String xmbz=(String) object[24];
					String zclr=(String) object[25];
					String zclrdh=(String) object[26];
					String fzjgmc=(String) object[27];;
					String fzjglxr=(String) object[28];
					String wbclrjg=(String) object[29];
					String filename=(String) object[30];
					String projectname=(String) object[31];
					String XMmanager=(String) object[32];
					String sssyb=(String) object[33];
					String clslr=(String) object[34];
					String shtgr=(String) object[35];
					String gzlxr=(String) object[36];
					String gzlxdh=(String) object[37];
					String gzyjdz=(String) object[38];
					String yjsbsj=(String) object[39];
					map.put("SSSYB", sssyb);
					map.put("CLSLR", clslr);
					map.put("SHTGR", shtgr);
					map.put("GZLXR", gzlxr);
					map.put("GZLXDH", gzlxdh);
					map.put("GZYJDZ", gzyjdz);
					map.put("YJSBSJ", yjsbsj);
					
					map.put("PROJECTNO", projectno);
					map.put("CUSTOMERNAME", customername);
					map.put("OWNER", owner);
					map.put("HTJE", htje);
					map.put("GROUPID", groupid);
					map.put("SSJE", ssje);
					map.put("JDMC", jdmc);
					map.put("SPZT", spzt);
					map.put("LCID", lcid);
					map.put("MANAGER", manager);
					map.put("STARTDATE", startdate);
					map.put("ENDDATE", enddate);
					map.put("KHLXR", khlxr);
					map.put("KHLXDH", khlxdh);
					map.put("GGJZR", ggjzr);
					map.put("SBJZR", sbjzr);
					map.put("MEMO", memo);
					map.put("XMBZ", xmbz);
					map.put("ZCLR", zclr);
					map.put("ZCLRDH", zclrdh);
					map.put("FZJGMC", fzjgmc);
					map.put("FZJGLXR", fzjglxr);
					map.put("WBCLRJG", wbclrjg);
					map.put("FILENAME", filename);
					map.put("PROJECTNAME", projectname);
					map.put("XMmanager", XMmanager);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> expProjectList2(String owner) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma,A.SSSYB sssyb,to_char(A.CLSLR,'yyyy-MM-dd') clslr,to_char(A.SHTGR,'yyyy-MM-dd') shtgr,A.GZLXR gzlxr,A.GZLXDH gzlxdh,A.GZYJDZ gzyjdz,to_char(A.YJSBSJ,'yyyy-MM-dd') yjsbsj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ? union all");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(userid);
        //if 按多项目组审批
		if(config.equals("2")){
		sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
		params.add(owner);
		params.add(owner);
		params.add(owner);
        //else
		}else{
		sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		}
		sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ? union all");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(userid);
        //if 按多项目组审批
		if(config.equals("2")){
		sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		}else{
        //else
		sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		}
		sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno left join (select wtfk.taskno taskno,wtfk.xmbh xmbh,wtfk.username twname,wtfk.question question,wtfk.createdate wcreatedate,retalk.username username,retalk.content content,retalk.createdate rcreatedate from BD_ZQB_XMWTFK wtfk left join BD_ZQB_RETALK retalk on wtfk.id=retalk.questionno) xgwt on task.projectno=xgwt.xmbh and task.groupid=xgwt.taskno) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String projectno=(String) object[1];
					String customername=(String) object[2];
					String owner=(String) object[3];
					Double htje=((BigDecimal) object[4]).doubleValue();
					Integer groupid=object[7]==null?0:Integer.valueOf(object[7].toString());
					Double ssje=0.0;
					if (object[8] != null) {
						ssje = ((BigDecimal) object[8]).doubleValue();
					}
					String jdmc=(String) object[9];
					String spzt=(String) object[11];
					String manager=(String) object[12];
					BigDecimal instanceid= (BigDecimal) object[15];
					long lcid = instanceid.longValue();
					String pjjg=object[16] == null ? "" : object[16].toString();
					
					String startdate=(String) object[17];
					String enddate=(String) object[18];
					String khlxr=(String) object[19];
					String khlxdh=(String) object[20];
					String ggjzr=(String) object[21];
					String sbjzr=(String) object[22];
					String memo=(String) object[23];
					String xmbz=(String) object[24];
					String zclr=(String) object[25];
					String zclrdh=(String) object[26];
					String fzjgmc=(String) object[27];;
					String fzjglxr=(String) object[28];
					String wbclrjg=(String) object[29];
					String filename=(String) object[30];
					String projectname=(String) object[31];
					String XMmanager=(String) object[32];
					String sssyb=(String) object[33];
					String clslr=(String) object[34];
					String shtgr=(String) object[35];
					String gzlxr=(String) object[36];
					String gzlxdh=(String) object[37];
					String gzyjdz=(String) object[38];
					String yjsbsj=(String) object[39];
					map.put("SSSYB", sssyb);
					map.put("CLSLR", clslr);
					map.put("SHTGR", shtgr);
					map.put("GZLXR", gzlxr);
					map.put("GZLXDH", gzlxdh);
					map.put("GZYJDZ", gzyjdz);
					map.put("YJSBSJ", yjsbsj);
					
					map.put("PROJECTNO", projectno);
					map.put("CUSTOMERNAME", customername);
					map.put("OWNER", owner);
					map.put("HTJE", htje);
					map.put("GROUPID", groupid);
					map.put("SSJE", ssje);
					map.put("JDMC", jdmc);
					map.put("SPZT", spzt);
					map.put("LCID", lcid);
					map.put("MANAGER", manager);
					map.put("STARTDATE", startdate);
					map.put("ENDDATE", enddate);
					map.put("KHLXR", khlxr);
					map.put("KHLXDH", khlxdh);
					map.put("GGJZR", ggjzr);
					map.put("SBJZR", sbjzr);
					map.put("MEMO", memo);
					map.put("XMBZ", xmbz);
					map.put("ZCLR", zclr);
					map.put("ZCLRDH", zclrdh);
					map.put("FZJGMC", fzjgmc);
					map.put("FZJGLXR", fzjglxr);
					map.put("WBCLRJG", wbclrjg);
					map.put("FILENAME", filename);
					map.put("PROJECTNAME", projectname);
					map.put("XMmanager", XMmanager);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> expProjectXGWTList(String projectno,String groupid) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select  orguser.username twusername,tw.question question,to_char(tw.createdate,'yyyy-MM-dd hh24:mi') twcreatedate,hf.username hfusername,hf.content content,to_char(hf.createdate,'yyyy-MM-dd hh24:mi') hfcreatedate from BD_ZQB_XMWTFK tw left join (select userid,username from orguser) orguser on tw.userid=orguser.userid left join bd_zqb_retalk hf on tw.id=hf.questionno where tw.xmbh=? and tw.taskno = ?");
		params.add(projectno);
		params.add(groupid);
		final List param = params;
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String twusername = (String) object[0];
					String question = (String) object[1];
					String twcreatedate = (String) object[2];
					String hfusername = (String) object[3];
					String content = (String) object[4];
					String hfcreatedate = (String) object[5];
					map.put("TWUSERNAME", twusername);
					map.put("QUESTION", question);
					map.put("TWCREATEDATE", twcreatedate);
					map.put("HFUSERNAME", hfusername);
					map.put("CONTENT", content);
					map.put("HFCREATEDATE", hfcreatedate);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> expProjectPJList(String projectno,String groupid) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select orguser.username pjr,pj.pjsm,pj.ldpj pjjg,to_char(pj.pjsj,'yyyy-MM-dd hh24:mi') pjsj from BD_ZQB_XMRWPJB pj left join (select userid,username from orguser) orguser on pj.pjr=orguser.userid where pj.projectno=? and pj.groupid = ?");
		params.add(projectno);
		params.add(groupid);
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String pjr = (String) object[0];
					String pjsm = (String) object[1];
					String pjjg = (String) object[2];
					String pjsj = (String) object[3];
					map.put("PJR", pjr);
					map.put("PJSM", pjsm);
					map.put("PJJG", pjjg);
					map.put("PJSJ", pjsj);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getShowDailyList(String projectNo, int pageNow, int pageSize,String startdate,String enddate) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select to_char(projectdate,'yyyy-MM-dd') projectdate,progress,username,tel,tracking,projectno from bd_zqb_xmrbb where projectno=?");
		params.add(projectNo);
		if (startdate != null && !"".equals(startdate)) {
			sb.append(" and to_char(projectdate,'yyyy-MM-dd')>= ?");
			params.add(startdate);
		}
		if (enddate != null && !"".equals(enddate)) {
			sb.append(" and to_char(projectdate,'yyyy-MM-dd')<= ?");
			params.add(enddate);
		}
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String projectdate = (String) object[0];
					String progress = (String) object[1];
					String username = (String) object[2];
					String tel = (String) object[3];
					String tracking = (String) object[4];
					String projectno = (String) object[5];
					map.put("PROJECTDATE", projectdate);
					map.put("PROGRESS", progress);
					map.put("USERNAME", username);
					map.put("TEL", tel);
					map.put("TRACKING", tracking);
					map.put("PROJECTNO", projectno);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<String> getDailyRunProjectList() {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		//select * from (select z.*,rownum as rm from (
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno ) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);		
				List<String> l = new ArrayList<String>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String projectNo = object[1] == null ? "" : object[1].toString();
					l.add(projectNo);
				}
				return l;
			}
		});
	}
	
	public List<String> getDailyRunProjectList1(String owner) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ? union all");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(userid);
              //if 按多项目组审批
				if(config.equals("2")){
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				params.add(owner);
				params.add(owner);
				params.add(owner);
              //else
				}else{
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
				params.add(owner);
				params.add(owner);
				params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(owner,0, instr(owner,'[',1)-1) =? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid=d.instanceid where userid = ?");
                       
                        //分派项目审批人
						sb.append(" union all");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						params.add(userid);
                        //if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}else{
                        //else
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno left join (select wtfk.taskno taskno,wtfk.xmbh xmbh,wtfk.username twname,wtfk.question question,wtfk.createdate wcreatedate,retalk.username username,retalk.content content,retalk.createdate rcreatedate from BD_ZQB_XMWTFK wtfk left join BD_ZQB_RETALK retalk on wtfk.id=retalk.questionno) xgwt on task.projectno=xgwt.xmbh and task.groupid=xgwt.taskno) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<String> l = new ArrayList<String>();
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					l.add(projectNo);
				}
				return l;
			}
		});
	}

	public List<HashMap> getDaily(List<String> tmplist) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select to_char(rb.projectdate,'yyyy-MM-dd') projectdate,rb.progress,rb.username,rb.tel,rb.tracking,rb.projectno,pj.projectname from bd_zqb_xmrbb rb inner join bd_zqb_pj_base pj on rb.projectno=pj.projectno where rb.projectno in (");
		for (int i = 0; i < tmplist.size(); i++) {
			if (i == (tmplist.size() - 1)) {  
				sb.append("?");
		    }else{  
		    	sb.append("?,");  
		    }
			params.add(tmplist.get(i));
		}
		sb.append(")");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String projectdate = (String) object[0];
					String progress = (String) object[1];
					String username = (String) object[2];
					String tel = (String) object[3];
					String tracking = (String) object[4];
					String projectno = (String) object[5];
					String projectname = (String) object[6];
					map.put("PROJECTDATE", projectdate);
					map.put("PROGRESS", progress);
					map.put("USERNAME", username);
					map.put("TEL", tel);
					map.put("TRACKING", tracking);
					map.put("PROJECTNO", projectno);
					map.put("PROJECTNAME", projectname);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getDailySize(List<String> tmplist) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select count(rb.projectno) count from bd_zqb_xmrbb rb inner join bd_zqb_pj_base pj on rb.projectno=pj.projectno where rb.projectno in (");
		for (int i = 0; i < tmplist.size(); i++) {
			if (i == (tmplist.size() - 1)) {  
				 sb.append("?");
		     }else{  
		    	 sb.append("?,");  
		     }
			params.add(tmplist.get(i));
		}
		sb.append(") order by rb.projectno");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object object : list) {
					map = new HashMap();
					BigDecimal count=(BigDecimal) object;
					map.put("COUNT", count);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<HashMap> getRunProjectList3(String owner, int pageNow,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		final String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		String username=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUsername().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J WHERE 1=1 and sftxcl='是' ");
				if (!"".equals(customername) && customername != null) {
					sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
					params.add("%" + customername + "%");
				}
				
				if("1".equals(dgzt)){
					sb.append(" AND QRDG = '已完成' ");
				}else if("2".equals(dgzt)){
					sb.append(" AND QRDG is null ");
				}
				
				sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
				if ("".equals(xmjd) || xmjd == null) {
					sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
				} else {
					sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
					params.add("%" + xmjd + "%");
				}
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ?) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J");
				params.add(startRow1);
				params.add(pageSize1);
						sb.append(" WHERE 1=1 and sftxcl='是' ");
						if (!"".equals(customername) && customername != null) {
							sb.append(" AND upper(CUSTOMERNAME) like upper(?) ");
							params.add("%" + customername + "%");
						}

						if("1".equals(dgzt)){
							sb.append(" AND QRDG = '已完成' ");
						}else if("2".equals(dgzt)){
							sb.append(" AND QRDG is null ");
						}
						
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO ");
							
						if ("".equals(xmjd) || xmjd == null) {
							sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
						} else {
							sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
							params.add("%" + xmjd + "%");
						}
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C) WHERE RN > ? AND RN <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid order by J.ID DESC,J.groupid");
						params.add(startRow1);
						params.add(pageSize1);
						params.add(userid);
		final String sql1 = sb.toString();
		final String jd3=SystemConfig._xmsplcConf.getJd3();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[2];
					String owner = (String) object[3];
					Double htje = ((BigDecimal) object[4]).doubleValue();
					Double wsje = 0.0;
					if (object[5] != null) {
						wsje = ((BigDecimal) object[5]).doubleValue();
					}
					Double ssje = 0.0;
					if (object[8] != null) {
						ssje = ((BigDecimal) object[8]).doubleValue();
					}
					String rwjd = (String) object[9];
					String spzt = (String) object[11];
					String manager1 = (String) object[12];
					Integer pjinsid = 0;
					if (object[23] != null) {
						pjinsid = Integer.valueOf(object[23].toString());
					}
					Integer xgwtinsid = 0;
					if (object[14] != null) {
						xgwtinsid = Integer.valueOf(object[14].toString());
					}
					Integer instanceid = Integer.valueOf(object[15].toString());
					String xmjd = object[6] == null ? "" : object[6]
							.toString();
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					String groupid = object[7] == null ? "" : object[7]
							.toString();
					String pj = object[16] == null ? "" : object[16].toString();
					Integer num = Integer.valueOf(object[24].toString());
					String lcbh = object[17] == null ? "" : object[17]
							.toString();
					String stepid = object[19] == null ? "" : object[19]
							.toString();
					String xmspzt = object[21] == null ? "" : object[21]
							.toString();
					Integer lcbs=object[18]==null?0:Integer.valueOf(object[18].toString());
					Integer taskid=object[20]==null?0:Integer.valueOf(object[20].toString());
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
					map.put("ISSTEPID", jd3.equals(stepid)?true:false);
					map.put("TASKID", taskid);
					map.put("XMSPZT", xmspzt);
					
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("CUSTOMERNAME", customername);
					map.put("HTJE", htje);
					map.put("SSJE", ssje);
					map.put("WSJE", wsje);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("PJ", pj);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					l.add(map);
				}
				return l;
			}
		});
	}
	public Integer deleteXYZLList(String zlxxid){
		Map params = new HashMap();
		params.put(1, zlxxid);
		return DBUTilNew.update("UPDATE BD_XP_XYZLXXB SET ZT='已删除' WHERE ID= ? ",params);
	}
	public Integer updateXYZLList(String zlxxid){
		Map params = new HashMap();
		params.put(1, zlxxid);
		return DBUTilNew.update("UPDATE BD_XP_XYZLXXB SET ZZHTSFYGD='已归档' WHERE ID= ? ",params);
	}
	public Integer delatexmzllist(String listid){
		Map params = new HashMap();
		params.put(1, listid);
		return DBUTilNew.update("DELETE FROM BD_XP_XYQD WHERE ID= ? ",params);
	}
	public Integer savaxmzllist(String chk_list,String projectNo){
		if(chk_list != null && !chk_list.equals("")){
			String[] chklists = chk_list.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < chklists.length; i++) {
				Map params = new HashMap();
				params.put(1, chklists[i].trim().toString());
				params.put(2, projectNo);
				String xyzlmc=DBUTilNew.getDataStr("XYZLMC", "SELECT XYZLMC FROM BD_XP_XYZLXXB WHERE ZT IS NULL AND XYZLMC= ?  AND XMBH= ? ", params);
				if(xyzlmc.equals("")){
					list.add(chklists[i].trim().toString());
				}
			}
			Integer id = DBUtil.getInt("SELECT MAX(ID) ID FROM BD_XP_XYZLXXB", "ID");
			for (int i = 0; i < list.size(); i++) {
				Map params = new HashMap();
				params.put(1, projectNo);
				DBUTilNew.update("INSERT INTO BD_XP_XYZLXXB(ID,XMBH,XYZLMC) VALUES("+(id+1+i)+", ? ,'"+list.get(i)+"')",params);
				DBUtil.executeUpdate("INSERT INTO SYS_ENGINE_FORM_BIND VALUES((SELECT （SELECT MAX(ID) FROM SYS_ENGINE_FORM_BIND)+1 FROM DUAL),(SELECT （SELECT MAX(INSTANCEID) FROM SYS_ENGINE_FORM_BIND)+1 FROM DUAL),(SELECT GROUPID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XYZLXXB'),(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XYZLXXB'),"+(id+1+i)+",0)");
			}
		}
		return 1;
	}
	
	public List<HashMap> getXMZLList(String projectNo,String xmzlListformid) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT ID,XYMC,ZT,INSTANCEID FROM (SELECT INSTANCEID,DATAID,METADATAID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=? AND METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XYQD')) A LEFT JOIN BD_XP_XYQD B ON A.DATAID = B.ID WHERE ID IS NOT NULL AND XYMC NOT IN(SELECT XYZLMC FROM BD_XP_XYZLXXB WHERE ZT IS NULL AND XMBH=?)");
		params.add(xmzlListformid);
		params.add(projectNo);
		final List param = params;
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap> l = new ArrayList<HashMap>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal) object[0];
					String XYMC = (String) object[1];
					String ZT = (String) object[2];
					BigDecimal INSTANCEID = (BigDecimal) object[3];
					map.put("ID", ID);
					map.put("XYMC", XYMC);
					map.put("ZT", ZT);
					map.put("INSTANCEID", INSTANCEID);
					l.add(map);
				}
				return l;
			}
		});
	}
	public List<HashMap> showXYZLList(String projectNo) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT ID,XMBH,XYZLMC,HTQDRQ,HTZZRQ,HTFZR,ZLFS,ZZHTSFYGD,HTWJ,YJDZ,SJR,SJRDH,YB,KDDH,INSTANCEID,ZT FROM (SELECT INSTANCEID,DATAID,METADATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_XP_XYZLXXB')) A LEFT JOIN BD_XP_XYZLXXB B ON A.DATAID = B.ID) WHERE ID IS NOT NULL AND XMBH=? ORDER BY XYZLMC");
		params.add(projectNo);
		final List param = params;
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				DBUtilInjection d=new DBUtilInjection();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
				}
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal ID = (BigDecimal) object[0];
					String XMBH = (String) object[1];
					String XYZLMC = (String) object[2];
					Date HTQDRQ = (Date) object[3];
					String HTQDRQstr="";
					if(HTQDRQ!=null){
						HTQDRQstr = sdf.format(HTQDRQ);
					}
					Date HTZZRQ = (Date) object[4];
					String HTZZRQstr="";
					if(HTZZRQ!=null){
						HTZZRQstr = sdf.format(HTZZRQ);
					}
					String HTFZR = (String) object[5];
					BigDecimal ZLFS = (BigDecimal) object[6];
					String ZZHTSFYGD = (String) object[7];
					String HTWJ = (String) object[8];
					String YJDZ = (String) object[9];
					String SJR = (String) object[10];
					String SJRDH = (String) object[11];
					String YB = (String) object[12];
					String KDDH = (String) object[13];
					BigDecimal INSTANCEID = (BigDecimal) object[14];
					String ZT = (String) object[15];
					map.put("ID", ID);
					map.put("XMBH", XMBH);
					map.put("XYZLMC", XYZLMC);
					map.put("HTQDRQ", HTQDRQstr);
					map.put("HTZZRQ", HTZZRQstr);
					map.put("HTFZR", HTFZR);
					map.put("ZLFS", ZLFS);
					map.put("ZZHTSFYGD", ZZHTSFYGD);
					map.put("HTWJ", HTWJ);
					map.put("YJDZ", YJDZ);
					map.put("SJR", SJR);
					map.put("SJRDH", SJRDH);
					map.put("YB", YB);
					map.put("KDDH", KDDH);
					map.put("INSTANCEID", INSTANCEID);
					map.put("ZT", ZT);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public HashMap<String,String> getParameterMap(HashMap<String,String> map,HashMap<String,List<String>> parameterMap){
		HashMap<String,String> hashMap=new HashMap<String,String>();
		if(parameterMap!=null&&!parameterMap.isEmpty()){
			for (String key : map.keySet()) {
				List<String> list = parameterMap.get(map.get(key));
				StringBuffer text=new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					if (i == (list.size() - 1)) {  
						text.append("'"+list.get(i)+"'"); 
					}else if((i%999)==0 && i>0){  
						text.append("'"+list.get(i)+"'").append(") or "+key+" in ("); 
					}else{  
						text.append("'"+list.get(i)+"'").append(",");
					}  
				}
				hashMap.put(key, text.toString());
			}
		}
		return hashMap;
	}
	
	public HashMap<String, String> getCyrUserMap(HashMap<String, String> map,String name) {
		HashMap<String,String> hashMap=new HashMap<String,String>();
		HashMap<String,List<String>> searcheName = getSearcheName(name);
		for (String key : map.keySet()) {
			List<String> list = searcheName.get(map.get(key));
			StringBuffer text=new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				if (i == (list.size() - 1)) {  
					text.append("'"+list.get(i)+"'"); 
				}else if((i%999)==0 && i>0){  
					text.append("'"+list.get(i)+"'").append(") or "+key+" in ("); 
			    }else{  
			    	text.append("'"+list.get(i)+"'").append(",");
			    }  
			}
			hashMap.put(key, text.toString().equals("")?"''":text.toString());
        }
		return hashMap;
	}
	
	public HashMap<String,List<String>> getSearcheName(String name) {
		HashMap<String,List<String>> listMap=new HashMap<String,List<String>>();
		List<String> owner=new ArrayList<String>();
		List<String> userid=new ArrayList<String>();
		List<String> username=new ArrayList<String>();
		StringBuffer sql=new StringBuffer("SELECT USERID userid,USERNAME username FROM ORGUSER WHERE USERNAME LIKE ?");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, "%"+name+"%");
			rset = stmt.executeQuery();
			while (rset.next()) {
				String uid = rset.getString("userid");
				String uname = rset.getString("username");
				String ownerTemp = uid+"["+uname+"]";
				owner.add(ownerTemp);
				userid.add(uid);
				username.add(uname);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		listMap.put("OWNER", owner);
		listMap.put("USERID", userid);
		listMap.put("USERNAME", username);
		return listMap;
	}
}
