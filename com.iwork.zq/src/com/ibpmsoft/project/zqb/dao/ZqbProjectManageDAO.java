package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
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

import oracle.jdbc.driver.OracleTypes;

import org.activiti.engine.task.Task;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.model.UploadDocModel;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.UtilDate;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.HibernateUtil;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.ProcessAPI;

public class ZqbProjectManageDAO extends HibernateDaoSupport {

	public List<UploadDocModel> getDocList(String searchkey, String owner) {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		String config = SystemConfig._xmlcConf.getConfig();
		List parameter=new ArrayList();//存放参数
		List<UploadDocModel> list = new ArrayList();
		String sql = "";
		StringBuffer sb = new StringBuffer();
		if (searchkey == null) {
			if ("".equals(owner)) {
				sb.append("select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,"
						+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
								+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
								+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC) C"
								+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc");
						sql=sb.toString();
			} else {
						sb.append("select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,"
						+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"
						//任务阶段负责人
						+ " select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
						+ " left join BD_ZQB_PJ_BASE b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ?"
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
						}else{
						sb.append(" select projectno from bd_zqb_pj_base"
		              	+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
		              	+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						parameter.add(userid);
						parameter.add(userid);
						parameter.add(userid);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
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
								+ " select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?"
								+ " union all"
		                        //任务阶段负责人
		                        + " select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"
		                        //项目及成员信息
		                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
		                        + " left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join ("
		                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
		                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ? ");
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
								parameter.add(userid);
		                        //分派项目审批人
								sb.append(" union all");
		                        //if 按多项目组审批
								if(config.equals("2")){
								sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN ("
										+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? "
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
								sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
										+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
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
				sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
				+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"
				+ " LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " ORDER BY A.ID DESC"
				+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
				+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
				+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
               
				sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
						+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO");
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
						+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
						+ " ORDER BY A.ID DESC) C"
						+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) a,(select * from sys_upload_file where file_src_name like ? or file_src_name like ?) b where a.jdzl like '%'||b.file_id||'%'");
				parameter.add(searchkey.toUpperCase());
				parameter.add(searchkey.toLowerCase());
				sql=sb.toString();
			} else {
				sb.append("select * from (select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(E.SSJE, 0)) WSJE,A.XMJD,"
				+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN ("
				//项目的负责人、现场负责人
				+ " select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"
				//任务阶段负责人
				+ " select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"
				//项目及成员信息
				+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
				+ " left join BD_ZQB_PJ_BASE b on a.dataid = b.id "
				+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ?"
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
						+ " select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) = ? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?"
						+ " union all"
                        //任务阶段负责人
                        + " select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"
                        //项目及成员信息
                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
                        + " left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join ("
                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ?");
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
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
								+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO");
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
					sublist1 = FileUploadAPI.getInstance().getFileUploads(file_id);
				}
				List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(attach);
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
		StringBuffer sql = new StringBuffer("select * from BD_ZQB_XMWTFK where taskno=? and xmbh=?");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setLong(1,taskno);
			stmt.setString(2,xmbh);
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
			stmt.setString(1,instanceid);
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
			sql.append(" AND SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)=? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)=?");
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
				stmt.setString(2, UserContextUtil.getInstance().getCurrentUserId());
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
		sql.append("select status,count(*) num from bd_zqb_pj_base where 1=1");
		if (userid != null) {
			sql.append(" AND SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)=? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)=?");
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
				stmt.setString(1, UserContextUtil.getInstance().getCurrentUserId());
				stmt.setString(2, UserContextUtil.getInstance().getCurrentUserId());
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
		sql.append("SELECT A.* FROM (SELECT XMJD,COUNT(*) AS NUM FROM (SELECT CASE STATUS||TYPENO WHEN '已完成1' THEN '持续督导' ELSE XMJD END XMJD,STATUS,TYPENO FROM BD_ZQB_PJ_BASE) ");
		sql.append("WHERE 1=1 AND XMJD IN(SELECT '持续督导' FROM DUAL UNION ALL SELECT DISTINCT JDMC FROM BD_ZQB_KM_INFO WHERE STATE=1)");
		if (pjNoList != null && pjNoList.size() > 0) {
			sql.append(" AND  (1=2 ");
			for (String projectNo : pjNoList) {
				sql.append(" OR PROJECTNO = ? ");
				parameter.add(projectNo);
			}
			sql.append(")");
		}
		sql.append(" AND ((STATUS='已完成' AND typeno='1') OR STATUS<>'已完成')");
		sql.append("GROUP BY XMJD) A LEFT JOIN BD_ZQB_KM_INFO B ON A.XMJD=B.JDMC WHERE B.STATE=1 OR B.STATE IS NULL ORDER BY B.SORTID");
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
		sql.append("SELECT A.* FROM (SELECT XMJD, COUNT(*) AS NUM FROM BD_ZQB_PJ_BASE ");
		sql.append(" WHERE 1=1 ");
		if (userid != null) {
			sql.append("AND ( SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)=? OR SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)=?) ");
		}
		sql.append(" AND (STATUS<>'已完成'  OR typeno='1')");
		sql.append(" GROUP BY XMJD) A LEFT JOIN BD_ZQB_KM_INFO B ON A.XMJD=B.JDMC WHERE B.STATE=1 OR B.STATE IS NULL ORDER BY B.SORTID");
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			if (userid != null) {
				stmt.setString(1,UserContextUtil.getInstance().getCurrentUserId());
				stmt.setString(2,UserContextUtil.getInstance().getCurrentUserId());
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

	public int getSSZKDataSize() {
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT COUNT(*) NUM FROM (select * from (select  p.projectno,p.projectname,p.htje,ssje num from (select * from bd_zqb_pj_base where status='执行中') p inner join (select projectno,sum(ssje) ssje from BD_PM_TASK group by projectno) s on p.projectno = s.projectno))");
		final String sql = sql1.toString();
		return DBUtil.getInt(sql, "NUM");
	}
	
	/**
	 * 获得实收账款
	 * 
	 * @param userids
	 * @return
	 */
	public List<HashMap> getSSZKData(int pageNumber,int pageSize) {
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT PROJECTNO,PROJECTNAME,SUM(HTJE) HTJE,SUM(NUM) YSKE FROM (select * from (select  p.projectno,p.projectname,p.htje,ssje num from (select * from bd_zqb_pj_base where status='执行中') p inner join (select projectno,sum(ssje) ssje from BD_PM_TASK group by projectno) s on p.projectno = s.projectno)) group by PROJECTNO,PROJECTNAME");
		final String sql = sql1.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setFirstResult(startRow1);
				query.setMaxResults(pageSize1);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				for (Object[] object : list) {
					map = new HashMap();
					String projectno = (String)object[0];
					String projectname = (String)object[1];
					BigDecimal htje = (BigDecimal)object[2];
					BigDecimal yske = (BigDecimal)object[3];
					
					map.put("PROJECTNO", projectno);
					map.put("PROJECTNAME", projectname);
					map.put("HTJE", htje);
					map.put("YSKE", yske);
					l.add(map);
				}
				return l;
			}
		});
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
	
	public HashMap<String,Object> getReqireData(int pageNumber,int pageSize,String customername,String sssyb,String cyrName,String xmlx){
		/**
		1、loginUserID varchar2,--大写用户ID
		2、XMLX IN OUT VARCHAR2, --项目类型
		3、CURSOR1 out PJ_CURSOR,--正在执行的项目
		CURSOR2 out PJ_CURSOR,--持续督导项目-------------333
		4、CURSOR3 out PJ_CURSOR,--关闭项目
		5、Total1 out NUMBER,--正在执行的项目总数
		Total2 out NUMBER,--持续督导项目总数------------666
		6、Total3 out NUMBER,--关闭项目总数
		7、CorpInfo VARCHAR2,--客户
		8、CZBM VARCHAR2,--承做部门
		9、CYR VARCHAR2, --参与人
		10、PageStart NUMBER,--举例说明：显示第1条到第10条数据，那么该值为0
		11、PageEnd NUMBER --举例说明：显示第1条到第10条数据，那么该值为10
		*/
		HashMap<String,Object> data = new HashMap<String,Object>();
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		int start = (pageNumber - 1) * pageSize;
		int end = pageNumber*pageSize;
		
		StringBuffer query = new StringBuffer("call DW_Porject.DW_GETXM(?,?,?,?,?,?,?,?,?,?,?)");
		
		Connection conn = null;
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(query.toString()); //CallableStatement 为调用存储过程的特有类
			cstmt.setString(1,userid);
			cstmt.setString(2,xmlx);
			cstmt.registerOutParameter(3, OracleTypes.CURSOR); //注册存储过程的输出参数为游标类型--即resultSet类型
			cstmt.registerOutParameter(4, OracleTypes.CURSOR);
			cstmt.registerOutParameter(5, OracleTypes.NUMBER);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
			cstmt.setString(7, customername);
			cstmt.setString(8, sssyb);
			cstmt.setString(9, cyrName);
			cstmt.setInt(10,start);
			cstmt.setInt(11,end);
			cstmt.execute(); //执行存储过程
			
			ResultSet rsrun = (ResultSet) cstmt.getObject(3); //获取数据集合
			List<HashMap> runlist = new ArrayList<HashMap>();
			HashMap runmap;
			String jd3 = SystemConfig._xmsplcConf.getJd3();
	        while (rsrun.next()){
	        	runmap = new HashMap();
	        	Object tag = rsrun.getObject("TAG");
	        	Object id = rsrun.getObject("ID");
	        	Object instanceid = rsrun.getObject("INSTANCEID");
	        	Object projectno = rsrun.getObject("PROJECTNO");
	        	Object customername_ = rsrun.getObject("CUSTOMERNAME");
	        	Object customerno = rsrun.getObject("CUSTOMERNO");
	        	Object bmfzr = rsrun.getObject("BMFZR");
	        	Object xmfzr = rsrun.getObject("XMFZR");
	        	Object htje = rsrun.getObject("HTJE");
	        	Object createdate = rsrun.getObject("CREATEDATE");
	        	Object tbr = rsrun.getObject("TBR");
	        	Object wsje = rsrun.getObject("WSJE");
	        	Object czbm = rsrun.getObject("CZBM");
	        	Object startdate = rsrun.getObject("STARTDATE");
	        	Object enddate = rsrun.getObject("ENDDATE");
	        	Object groupid = rsrun.getObject("GROUPID");
	        	Object ssje = rsrun.getObject("SSJE");
	        	//-----------
	        	Object jdmc = rsrun.getObject("JDMC");
	        	Object sortid = rsrun.getObject("SORTID");
	        	Object jdfzr = rsrun.getObject("JDFZR");
	        	String lcbh = rsrun.getObject("LCBH")==null?"":rsrun.getObject("LCBH").toString();
	        	
	        	Integer lcbs = rsrun.getObject("LCBS")==null?0:Integer.parseInt(rsrun.getObject("LCBS").toString());
	        	Object stepid = rsrun.getObject("STEPID");
	        	Object taskid = rsrun.getObject("TASKID");
	        	Object xmspzt = rsrun.getObject("XMSPZT");
	        	Object zbspzt = rsrun.getObject("ZBSPZT");
	        	Object zblcbh = rsrun.getObject("ZBLCBH");
	        	Object zblcbs = rsrun.getObject("ZBLCBS");
	        	Object zbtaskid = rsrun.getObject("ZBTASKID");
	        	Object zbstepid = rsrun.getObject("ZBSTEPID");
	        	Object rn = rsrun.getObject("RN");
	        	Object jdcount = rsrun.getObject("JDCOUNT");
	        	
	        	runmap.put("TAG",tag);
	        	runmap.put("ZBSPZT",zbspzt);
	        	runmap.put("ZBLCBH", zblcbh);
	        	runmap.put("ZBLCBS", zblcbs);
	        	runmap.put("ZBTASKID", zbtaskid);
	        	runmap.put("ZBSTEPID", zbstepid);
	        	runmap.put("LCBH", lcbh);
	        	runmap.put("LCBS", lcbs);
	        	if(lcbs!=null&&!lcbs.equals("")&&(Integer)lcbs!=0){
					if(lcbh!=null){
						try{
					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs.toString()));
					if(pro!=null&pro.size()>0){
				    	Long prc=pro.get(0).getPrcDefId();
				    	runmap.put("PRCID", prc);
				    }
						}catch(Exception e){
							logger.error(e,e);
						}
					}
				}
				Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(lcbs.toString()));
				if(newTaskId!=null&&!"".equals(newTaskId)){
					if(userid.equals(newTaskId.getAssignee())){
						runmap.put("TASKID", newTaskId.getId());
					}else{
						runmap.put("TASKID", taskid);
					}
				}else{
					runmap.put("TASKID", taskid);
				}
	        	runmap.put("STEPID", stepid);
	        	runmap.put("ISSTEPID", jd3.equals(stepid)?true:false);
	        	runmap.put("XMSPZT", xmspzt);
	        	runmap.put("XMCY", "");//////////////////////////////////
	        	runmap.put("STARTDATE", startdate);
	        	runmap.put("ENDDATE", enddate);
	        	runmap.put("OWNER", bmfzr);
	        	runmap.put("MANAGER", xmfzr);
	        	runmap.put("PROJECTNO", projectno);
	        	runmap.put("INSTANCEID", instanceid);
	        	runmap.put("XMJD", jdmc);
	        	runmap.put("RWJD", jdmc);
	        	runmap.put("SPZT", zbspzt);
	        	runmap.put("PJ", "");///////////////////////////////////			
	        	runmap.put("CUSTOMERNAME", customername_);
	        	runmap.put("HTJE", htje);
	        	runmap.put("SSJE", ssje);
	        	runmap.put("WSJE", wsje);
	        	runmap.put("JDFZR", czbm);
	        	runmap.put("NUM", jdcount);
	        	runmap.put("GROUPID", groupid);
	        	runmap.put("PJINSID", instanceid);
	        	runlist.add(runmap);
	        }
	        BigDecimal totalnum = (BigDecimal)cstmt.getObject(5);
	        data.put("RUNLIST", runlist);
	        data.put("TOTALNUM", totalnum);
	        
	        ResultSet rscloselist = (ResultSet) cstmt.getObject(4); //获取数据集合
			List<HashMap> closelist = new ArrayList<HashMap>();
			HashMap closelistmap;
			while (rscloselist.next()){
				closelistmap = new HashMap();
	        	Object tag = rscloselist.getObject("TAG");
	        	Object id = rscloselist.getObject("ID");
	        	Object instanceid = rscloselist.getObject("INSTANCEID");
	        	Object projectno = rscloselist.getObject("PROJECTNO");
	        	Object customername_ = rscloselist.getObject("CUSTOMERNAME");
	        	Object customerno = rscloselist.getObject("CUSTOMERNO");
	        	Object bmfzr = rscloselist.getObject("BMFZR");
	        	Object xmfzr = rscloselist.getObject("XMFZR");
	        	Object htje = rscloselist.getObject("HTJE");
	        	Object createdate = rscloselist.getObject("CREATEDATE");
	        	Object tbr = rscloselist.getObject("TBR");
	        	Object wsje = rscloselist.getObject("WSJE");
	        	Object czbm = rscloselist.getObject("CZBM");
	        	Object startdate = rscloselist.getObject("STARTDATE");
	        	Object enddate = rscloselist.getObject("ENDDATE");
	        	Object groupid = rscloselist.getObject("GROUPID");
	        	Object ssje = rscloselist.getObject("SSJE");
	        	//-----------
	        	Object jdmc = rscloselist.getObject("JDMC");
	        	Object sortid = rscloselist.getObject("SORTID");
	        	Object jdfzr = rscloselist.getObject("JDFZR");
	        	String lcbh = rscloselist.getObject("LCBH")==null?"":rscloselist.getObject("LCBH").toString();
	        	
	        	Integer lcbs = rscloselist.getObject("LCBS")==null?0:Integer.parseInt(rscloselist.getObject("LCBS").toString());
	        	Object stepid = rscloselist.getObject("STEPID");
	        	Object taskid = rscloselist.getObject("TASKID");
	        	Object xmspzt = rscloselist.getObject("XMSPZT");
	        	Object zbspzt = rscloselist.getObject("ZBSPZT");
	        	Object zblcbh = rscloselist.getObject("ZBLCBH");
	        	Object zblcbs = rscloselist.getObject("ZBLCBS");
	        	Object zbtaskid = rscloselist.getObject("ZBTASKID");
	        	Object zbstepid = rscloselist.getObject("ZBSTEPID");
	        	Object rn = rscloselist.getObject("RN");
	        	Object jdcount = rscloselist.getObject("JDCOUNT");
	        	
	        	closelistmap.put("TAG",tag);
	        	closelistmap.put("ZBSPZT",zbspzt);
	        	closelistmap.put("ZBLCBH", zblcbh);
	        	closelistmap.put("ZBLCBS", zblcbs);
	        	closelistmap.put("ZBTASKID", zbtaskid);
	        	closelistmap.put("ZBSTEPID", zbstepid);
	        	closelistmap.put("LCBH", lcbh);
	        	closelistmap.put("LCBS", lcbs);
	        	if(lcbs!=null&&!lcbs.equals("")&&(Integer)lcbs!=0){
					if(lcbh!=null){
						try{
					List<ProcessRuOpinion> pro=ProcessAPI.getInstance().getProcessOpinionList(lcbh, Long.parseLong(lcbs.toString()));
					if(pro!=null&pro.size()>0){
				    	Long prc=pro.get(0).getPrcDefId();
				    	closelistmap.put("PRCID", prc);
				    }
						}catch(Exception e){
							logger.error(e,e);
						}
					}
				}
				Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(lcbs.toString()));
				if(newTaskId!=null&&!"".equals(newTaskId)){
					if(userid.equals(newTaskId.getAssignee())){
						closelistmap.put("TASKID", newTaskId.getId());
					}else{
						closelistmap.put("TASKID", taskid);
					}
				}else{
					closelistmap.put("TASKID", taskid);
				}
				closelistmap.put("STEPID", stepid);
				closelistmap.put("ISSTEPID", jd3.equals(stepid)?true:false);
				closelistmap.put("XMSPZT", xmspzt);
				closelistmap.put("XMCY", "");//////////////////////////////////
				closelistmap.put("STARTDATE", startdate);
				closelistmap.put("ENDDATE", enddate);
				closelistmap.put("OWNER", bmfzr);
				closelistmap.put("MANAGER", xmfzr);
				closelistmap.put("PROJECTNO", projectno);
				closelistmap.put("INSTANCEID", instanceid);
				closelistmap.put("XMJD", jdmc);
				closelistmap.put("RWJD", jdmc);
				closelistmap.put("SPZT", zbspzt);
				closelistmap.put("PJ", "");///////////////////////////////////			
				closelistmap.put("CUSTOMERNAME", customername_);
				closelistmap.put("HTJE", htje);
				closelistmap.put("SSJE", ssje);
				closelistmap.put("WSJE", wsje);
				closelistmap.put("JDFZR", czbm);
				closelistmap.put("NUM", jdcount);
				closelistmap.put("GROUPID", groupid);
				closelistmap.put("PJINSID", instanceid);
				closelist.add(closelistmap);
	        }
			BigDecimal closenum = (BigDecimal)cstmt.getObject(6);
	        data.put("CLOSELIST", closelist);
	        data.put("CLOSENUM", closenum);
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, null, null);
		}
		return data;
	}

	//2016-03-16增加SORTID，每个项目按sortid排序
	public List<HashMap> getRunProjectList1(int pageNow, int pageSize,
			String projectName, String xmjd, String startDate,
			String customername,String dgzt, String sssyb, String cyrName) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		final String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();//2015-11-27修改SQL，将阶段负责人（E.MANAGER）换成所属事业部（A.SSSYB）
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(cyrName!=null&&!cyrName.equals("")){
			map.put("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)", "USERID");
			map.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", "USERID");
			map.put("createuserid", "USERID");
			map.put("userid", "USERID");
			map.put("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)", "USERID");
			map.put("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)", "USERID");
			parameter = getCyrUserMap(map,cyrName);
		}
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();
		sb.append(" select j.*,nvl(d.num,1) from (");
		sb.append(" select * from (");
		sb.append(" select b.*,rownum rnum from (");
		sb.append(" select * from ("); 
		sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
		sb.append(" SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID FROM (");
		sb.append(" SELECT * FROM (");
		sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID,"); 
		sb.append(" E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,");
		sb.append(" f.sortid,CASE WHEN A.ZBSPZT is null THEN '未提交' ELSE A.ZBSPZT END zbspzt,ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID"); 
		sb.append(" FROM (");
		sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J");  
		if(cyrName!=null&&!cyrName.equals("")){
			sb.append(" INNER JOIN ("); 
			sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owners = parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
			for (int i = 0; i < owners.length; i++) {
				if(i==(owners.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers.length; i++) {
				if(i==(managers.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers[i].replaceAll("'", ""));
			}
			sb.append(") or createuserid in (");
			String[] createuserids = parameter.get("SUBSTR(createuserid,0, instr(createuserid,'[',1)-1)").split(",");
			for (int i = 0; i < createuserids.length; i++) {
				if(i==(createuserids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserids[i].replaceAll("'", ""));
			}
			sb.append(")"); 
			sb.append(" union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers_ = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers_.length; i++) {
				if(i==(managers_.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers_[i].replaceAll("'", ""));
			}
			sb.append(")"); 
			sb.append(" union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
			sb.append(" inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
			String[] userids = parameter.get("SUBSTR(userid,0, instr(userid,'[',1)-1)").split(",");
			for (int i = 0; i < userids.length; i++) {
				if(i==(userids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userids[i].replaceAll("'", ""));
			}
			sb.append(") union all"); 
			//server.xml中项目流程设置值为2，就是每个项目都有不同的审核人
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
			}else{
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (");
				sb.append(" SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
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
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		}
		sb.append(" WHERE 1=1");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ?");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
		sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'"); 
		}else{
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
		sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"); 
		sb.append(" WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,f.sortid"); 
		sb.append(" ) WHERE 1=1");
		sb.append(" ) J"); 
		sb.append(" LEFT JOIN (");
		sb.append(" SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ");  
		sb.append(" INNER JOIN (");
		sb.append(" SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND"); 
		sb.append(" INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID"); 
		sb.append(" INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID");
		sb.append(" ) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID");
		params.add(userid);
		sb.append(" ) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid order by J.ID DESC,J.sortid");
		sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
		sb.append(" UNION ALL");
		sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
		sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
		sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
		sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
		sb.append(" ZBSTEPID,NULL PJINSTANCEID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
		sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
		sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
		if (!"".equals(customername) && customername != null) {
			sb.append(" and upper(JYF) like ?");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND sgfs like ?");
			params.add("%" + sssyb + "%");
		}
		sb.append(" AND PROSTATUS=1");
		sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
		String[] userid1 =userid.split(",");
		for (int i = 0; i < userid1.length; i++) {
			if(i==(userid1.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid1[i].replaceAll("'", ""));
		}
		sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
		for (int i = 0; i < userid1.length; i++) {
			if(i==(userid1.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid1[i].replaceAll("'", ""));
		}
		sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");
		
		for (int i = 0; i < userid1.length; i++) {
			if(i==(userid1.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid1[i].replaceAll("'", ""));
		}
		sb.append("))");
		
		sb.append(" )");
		sb.append(" )");
		sb.append(" order by createdate desc,sortid");
		sb.append(" ) b");
		sb.append(" ) where RNUM> ? AND RNUM <= ?");
		params.add(startRow1);
		params.add(pageSize1);
		sb.append(" ) j");
		
		
		sb.append(" LEFT JOIN (");
		sb.append(" SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (");
		sb.append(" select * from(");
		sb.append(" select b.*,rownum rnum from (");
		sb.append(" select * from ("); 
		sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
		sb.append(" SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID FROM (");
		sb.append(" SELECT * FROM (");
		sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID,"); 
		sb.append(" E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,");
		sb.append(" f.sortid,CASE WHEN A.ZBSPZT is null THEN '未提交' ELSE A.ZBSPZT END zbspzt,ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID"); 
		sb.append(" FROM (");
		sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J");  
		if(cyrName!=null&&!cyrName.equals("")){
			sb.append(" INNER JOIN ("); 
			sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owners = parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
			for (int i = 0; i < owners.length; i++) {
				if(i==(owners.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers.length; i++) {
				if(i==(managers.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers[i].replaceAll("'", ""));
			}
			sb.append(") or createuserid in (");
			String[] createuserids = parameter.get("SUBSTR(createuserid,0, instr(createuserid,'[',1)-1)").split(",");
			for (int i = 0; i < createuserids.length; i++) {
				if(i==(createuserids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserids[i].replaceAll("'", ""));
			}
			sb.append(")"); 
			sb.append(" union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers_ = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers_.length; i++) {
				if(i==(managers_.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers_[i].replaceAll("'", ""));
			}
			sb.append(")"); 
			sb.append(" union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
			sb.append(" inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
			String[] userids = parameter.get("SUBSTR(userid,0, instr(userid,'[',1)-1)").split(",");
			for (int i = 0; i < userids.length; i++) {
				if(i==(userids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userids[i].replaceAll("'", ""));
			}
			sb.append(") union all"); 
			
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
			}else{
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (");
				sb.append(" SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
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
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		}
		sb.append(" WHERE 1=1");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ?");
			params.add("%" + sssyb + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
		sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'"); 
		}else{
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
		sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"); 
		sb.append(" WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,f.sortid"); 
		sb.append(" ) WHERE 1=1");
		sb.append(" ) J"); 
		sb.append(" LEFT JOIN (");
		sb.append(" SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ");  
		sb.append(" INNER JOIN (");
		sb.append(" SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND"); 
		sb.append(" INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID"); 
		sb.append(" INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID");
		sb.append(" ) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID");
		params.add(userid);
		sb.append(" ) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid order by J.ID DESC,J.sortid");
		sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
		sb.append(" UNION ALL");
		sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
		sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
		sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
		sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
		sb.append(" ZBSTEPID,NULL PJINSTANCEID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
		sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
		sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
		if (!"".equals(customername) && customername != null) {
			sb.append(" and upper(JYF) like ?");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND sgfs like ?");
			params.add("%" + sssyb + "%");
		}
		sb.append(" AND PROSTATUS=1");
		sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
		String[] userid2 =userid.split(",");
		for (int i = 0; i < userid1.length; i++) {
			if(i==(userid2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid2[i].replaceAll("'", ""));
		}
		sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
		for (int i = 0; i < userid1.length; i++) {
			if(i==(userid2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid2[i].replaceAll("'", ""));
		}
		sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");
		for (int i = 0; i < userid1.length; i++) {
			if(i==(userid2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid2[i].replaceAll("'", ""));
		}
		sb.append("))");
		
		sb.append(" )");
		sb.append(" )");
		sb.append(" order by createdate desc,sortid");
		sb.append(" ) b");
		sb.append(" ) where RNUM> ? AND RNUM <= ?");
		params.add(startRow1);
		params.add(pageSize1);
		sb.append(" ) L GROUP BY PROJECTNO,CUSTOMERNAME");
		sb.append(" ) D ON D.PROJECTNO = J.PROJECTNO");

		final String sql1 = sb.toString();
		final String jd3 = SystemConfig._xmsplcConf.getJd3();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
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
					if (object[6] != null) {wsje = ((BigDecimal) object[6]).doubleValue();}
					Double ssje = 0.0;
					if (object[9] != null) {ssje = ((BigDecimal) object[9]).doubleValue();}
					String rwjd = (String) object[10];
					String spzt = (String) object[12];
					String manager1 = (String) object[13];
					Integer pjinsid = 0;
					if (object[36] != null) {pjinsid = Integer.valueOf(object[36].toString());}
					Integer xgwtinsid = 0;
					if (object[15] != null) {xgwtinsid = Integer.valueOf(object[15].toString());}				
					Integer instanceid = Integer.valueOf(object[16].toString());
					String xmjd = object[7] == null ? "" : object[7].toString();
					String projectNo = object[1] == null ? "" : object[1].toString();
					String groupid = object[8] == null ? "" : object[8].toString();
					String pj = object[17] == null ? "" : object[17].toString();
					Integer num = Integer.valueOf(object[36].toString());
					String lcbh = object[18] == null ? "" : object[18].toString();
					String stepid = object[20] == null ? "" : object[20].toString();
					String xmspzt = object[22] == null ? "" : object[22].toString();
					String xmcy = object[23] == null ? "" : object[23].toString();
					String startdate = object[24] == null ? "" : object[24].toString();
					String enddate = object[25] == null ? "" : object[25].toString();
					Integer lcbs=object[19]==null?0:Integer.valueOf(object[19].toString());
					Integer taskid=object[21]==null?0:Integer.valueOf(object[21].toString());
					String zbspzt = object[27] == null ? "" : object[27].toString();
					
					String zblcbh = object[28] == null ? "" : object[28].toString();
					Integer zblcbs=object[29]==null?0:Integer.valueOf(object[29].toString());
					Integer zbtaskid=object[30]==null?0:Integer.valueOf(object[30].toString());
					String zbstepid = object[31] == null ? "" : object[31].toString();
					String ptype = object[34].toString();
					
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
					Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(lcbs.toString()));
					if(newTaskId!=null&&!"".equals(newTaskId)){
						if(userid.equals(newTaskId.getAssignee())){
							map.put("TASKID", newTaskId.getId());
						}else{
							map.put("TASKID", taskid);
						}
					}else{
						map.put("TASKID", taskid);
					}
					map.put("ZBSPZT",zbspzt);
					map.put("ZBLCBH", zblcbh);
					map.put("ZBLCBS", zblcbs);
					map.put("ZBTASKID", zbtaskid);
					map.put("ZBSTEPID", zbstepid);
					map.put("PTYPE", ptype.equals("A")?"推荐挂牌":"并购重组");
					
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
					map.put("ISSTEPID", jd3.equals(stepid)?true:false);
					map.put("XMSPZT", xmspzt);
					map.put("XMCY", xmcy);
					map.put("STARTDATE", startdate);
					map.put("ENDDATE", enddate);
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
		List params = new ArrayList();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		StringBuffer sb = new StringBuffer("SELECT H.*, I.NUM FROM (SELECT J.* from (SELECT A.CUSTOMERNAME,A.OWNER,NVL(A.HTJE, 0),(NVL(A.HTJE, 0) - NVL(D.SSJE, 0)) WSJE,E.SSJE,F.jdmc TASK_NAME,E.SPZT,E.MANAGER,E.PJINSID,E.XGWTINSID,BINDTABLE.INSTANCEID,"
		+ "ROWNUM RN,A.XMJD,A.PROJECTNO,E.GROUPID, nvl(g.ldpj,'评价') ldpj FROM BD_ZQB_PJ_BASE A,    SYS_ENGINE_FORM_BIND BINDTABLE,(SELECT B.PROJECTNO, SUM(NVL(B.SSJE, 0)) SSJE FROM BD_PM_TASK B, BD_ZQB_PJ_BASE C WHERE B.PROJECTNO(+) = C.PROJECTNO"
		+ " GROUP BY B.PROJECTNO) D,BD_PM_TASK E,BD_ZQB_KM_INFO  F,( select b.projectno,b.groupid,a.ldpj from  	BD_ZQB_XMRWPJB  a, BD_PM_TASK b where a.projectno(+)=b.projectno and a.groupid(+)=b.groupid) g "
		+ " WHERE A.PROJECTNO = D.PROJECTNO(+) AND E.GROUPID=F.ID(+) AND g.PROJECTNO(+) = A.PROJECTNO and( g.groupid = e.groupid or g.groupid is null) AND (F.jdmc like '%持续督导%' or F.jdmc is null)"
		+ " AND E.PROJECTNO(+) = A.PROJECTNO AND  A.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91"
		+ " and BINDTABLE.metadataid = 101");

		sb.append(" AND A.STATUS = '执行中'");
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND A.PROJECTNAME like ");
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
		sb.append(" ORDER BY A.customername DESC) J"
				+ " WHERE RN > ? AND RN <= ?) H,"
				+ " (SELECT G.CUSTOMERNAME, COUNT(*) NUM FROM (SELECT A.CUSTOMERNAME,A.OWNER,NVL(A.HTJE, 0),E.SSJE,E.TASK_NAME,E.SPZT,E.MANAGER,E.PJINSID,E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN FROM BD_ZQB_PJ_BASE A,  SYS_ENGINE_FORM_BIND BINDTABLE,"
				+ " BD_PM_TASK E WHERE E.PROJECTNO(+) = A.PROJECTNO AND A.STATUS = '执行中' AND  A.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null   and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101");
		params.add(startRow1);
		params.add(pageNow * pageSize);
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND A.PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" AND A.XMJD <> '持续督导'");
		} else {
			sb.append(" AND A.XMJD like ?");
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
		params.add(pageNow * pageSize);
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

	//2016-03-16增加SORTID，每个项目按sortid排序
	public List<HashMap> getCloseProjectList(int pageNow, int pageSize,
			String projectName, String xmjd, String startDate,
			String customername,String dgzt,HashMap<String,List<String>> parameterMap, String sssyb, String cyrName,Long ismanager) {
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
		map.put("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)", "USERID");
		map.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", "USERID");
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
		StringBuffer sb = new StringBuffer("select j.*,nvl(d.num,0) num,nvl(PJDATA.INSTANCEID,0) pins from (select * from (select b.*,rownum rnum from (select * from (SELECT p.*,d.createdate,'A' ptype FROM (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,F.SORTID FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
			if(!flag){
				if(ismanager==1){
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
					String[] owners = parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
					String[] managers = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
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
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
					String[] managers_ = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
					for (int i = 0; i < managers_.length; i++) {
						if(i==(managers_.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers_[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
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
					}else{
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
					if(orgRoleId==11){
						sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				}else{
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
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
					sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
					String[] owners = parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
					String[] managers = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
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
					sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
					String[] managers_ = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
					for (int i = 0; i < managers_.length; i++) {
						if(i==(managers_.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers_[i].replaceAll("'", ""));
					}
					sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
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
					}else{
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
		if (sssyb!=null&&!sssyb.equals("")) {
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
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导' )");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ? )");
			params.add("%" + xmjd + "%");
		}
		sb.append(" F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='已完成' AND A.TYPENO is null ORDER BY A.PROJECTNO DESC,F.SORTID) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91 UNION ALL (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT, NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
				if (!"".equals(customername) && customername != null) {
					sb.append(" and upper(JYF) like ?");
					params.add("%" + customername + "%");
				}
				if (sssyb!=null&&!sssyb.equals("")) {
					sb.append(" AND sgfs like ?");
					params.add("%" + sssyb + "%");
				}
				sb.append(" AND PROSTATUS=3");
				sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
				String[] userid2 =userid.split(",");
				for (int i = 0; i < userid2.length; i++) {
					if(i==(userid2.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userid2[i].replaceAll("'", ""));
				}
				sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
				for (int i = 0; i < userid2.length; i++) {
					if(i==(userid2.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userid2[i].replaceAll("'", ""));
				}
				sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");
				for (int i = 0; i < userid2.length; i++) {
					if(i==(userid2.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userid2[i].replaceAll("'", ""));
				}
				sb.append("))");
				
				sb.append(" )) order by createdate desc,sortid) b) where RNUM> ? AND RNUM <= ? ) j LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (select * from (select b.*,rownum rnum from (select * from (SELECT p.*,d.createdate,'A' ptype FROM (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,F.SORTID FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
				params.add(startRow1);
				params.add(pageSize1);
	if(!flag){
		if(ismanager==1){
			sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owners = parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
			for (int i = 0; i < owners.length; i++) {
				if(i==(owners.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
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
			sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers_ = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers_.length; i++) {
				if(i==(managers_.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers_[i].replaceAll("'", ""));
			}
			sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
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
			}else{
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
			if(orgRoleId==11){
				sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是'");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		}else{
			sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
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
			sb.append(" INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owners= parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
			for (int i = 0; i < owners.length; i++) {
				if(i==(owners.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers= parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers.length; i++) {
				if(i==(managers.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers[i].replaceAll("'", ""));
			}
			sb.append(") or createuserid in (");
			String[] createuserids= parameter.get("createuserid").split(",");
			for (int i = 0; i < createuserids.length; i++) {
				if(i==(createuserids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserids[i].replaceAll("'", ""));
			}
			sb.append(") union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers_= parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers_.length; i++) {
				if(i==(managers_.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers_[i].replaceAll("'", ""));
			}
			sb.append(") union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
			String[] userids= parameter.get("userid").split(",");
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
				String[] csfzrs= parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < csfzrs.length; i++) {
					if(i==(csfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzrs= parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < fsfzrs.length; i++) {
					if(i==(fsfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzsprs= parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < zzsprs.length; i++) {
					if(i==(zzsprs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(zzsprs[i].replaceAll("'", ""));
				}
				sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
			}else{
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzrs= parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < csfzrs.length; i++) {
					if(i==(csfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzrs= parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < fsfzrs.length; i++) {
					if(i==(fsfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzsprs= parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
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
if (sssyb!=null&&!sssyb.equals("")) {
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
	sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导' )");
} else {
	sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ? )");
	params.add("%" + xmjd + "%");
}
sb.append(" F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='已完成' AND A.TYPENO is null ORDER BY A.PROJECTNO DESC,F.SORTID) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91 UNION ALL (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
		if (!"".equals(customername) && customername != null) {
			sb.append(" and upper(JYF) like ?");
			params.add("%" + customername + "%");
		}
		if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND sgfs like ?");
			params.add("%" + sssyb + "%");
		}
		sb.append(" AND PROSTATUS=3");
		sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
		String[] userid3 =userid.split(",");
		for (int i = 0; i < userid3.length; i++) {
			if(i==(userid3.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid3[i].replaceAll("'", ""));
		}
		sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
		for (int i = 0; i < userid3.length; i++) {
			if(i==(userid3.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid3[i].replaceAll("'", ""));
		}
		sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");
		for (int i = 0; i < userid3.length; i++) {
			if(i==(userid3.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid3[i].replaceAll("'", ""));
		}
		sb.append("))");

		sb.append(" )) order by createdate desc,sortid) b) where RNUM> ? AND RNUM <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid");
		params.add(startRow1);
		params.add(pageSize1);
		params.add(userid);
		final String sql1 = sb.toString();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
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
					//if (object[28] != null) {
						//pjinsid = Integer.valueOf(object[28].toString());
					//}
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
					Integer num = Integer.valueOf(object[30].toString());
					String lcbh = object[18] == null ? "" : object[18]
							.toString();
					String stepid = object[20] == null ? "" : object[20]
							.toString();
					String xmspzt = object[22] == null ? "" : object[22]
							.toString();
					String xmcy = object[23] == null ? "" : object[23]
							.toString();
					String startdate = object[24] == null ? "" : object[24]
							.toString();
					String enddate = object[25] == null ? "" : object[25]
							.toString();
					Integer lcbs=object[19]==null?0:Integer.valueOf(object[19].toString());
					Integer taskid=object[21]==null?0:Integer.valueOf(object[21].toString());
					String ptype = object[28].toString();
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
					Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(lcbs.toString()));
					if(newTaskId!=null&&!"".equals(newTaskId)){
						if(userid.equals(newTaskId.getAssignee())){
							map.put("TASKID", newTaskId.getId());
						}else{
							map.put("TASKID", taskid);
						}
					}else{
						map.put("TASKID", taskid);
					}
					map.put("PTYPE", ptype.equals("A")?"推荐挂牌":"并购重组");
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
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
					map.put("XMCY", xmcy);
					map.put("STARTDATE", startdate);
					map.put("ENDDATE", enddate);
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
			sb.append(" AND TYPENO is null ");
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
		sb.append(" inner join BD_PM_TASK c on (c.projectno=BOTABLE.projectno or c.projectno is null) group by BOTABLE.CREATEUSER, BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID, nvl(BOTABLE.htje,0),nvl(BOTABLE.htje,0)) A,BD_PM_TASK B    WHERE A.PROJECTNO=B.PROJECTNO OR  B.PROJECTNO IS NULL ORDER BY A.ID desc");
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
					String customername = (String) object[9];
					String customerno = (String) object[10];
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("PROJECTNAME", projectName);
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
		StringBuffer sb = new StringBuffer("SELECT A.*,B.TASK_NAME,B.MANAGER FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,nvl(BOTABLE.htje,0) htje,sum(nvl(ssje,0)) ssje,nvl(BOTABLE.htje,0)-sum(nvl(ssje,0)) wsje,count(*) num,BOTABLE.manager cwjl FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101  AND STATUS = '执行中' AND (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)=? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)=?)");
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
			sb.append(" AND STARTDATE = to_date(?,'yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" inner join BD_PM_TASK c on (c.projectno=BOTABLE.projectno or c.projectno is null) group by BOTABLE.CREATEUSER, BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,BOTABLE.manager,nvl(BOTABLE.htje,0)) A,BD_PM_TASK B WHERE A.PROJECTNO=B.PROJECTNO OR  B.PROJECTNO IS NULL ORDER BY A.ID desc");
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
					String customername = (String) object[9];
					String customerno = (String) object[10];
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("PROJECTNAME", projectName);
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

	//2016-03-16增加SORTID，每个项目按sortid排序
	public List<HashMap> getRunProjectList2(String owner, int pageNow,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt,HashMap<String,List<String>> parameterMap, String sssyb, String cyrName, Long ismanager) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		final String userid=userModel.getUserid().toString().trim();
		Long orgRoleId = userModel.getOrgroleid();
		String username=userModel.getUserid();//getUsername().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();//2015-11-27修改SQL，将阶段负责人（E.MANAGER）换成所属事业部（A.SSSYB）
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		if(ismanager==1){
			map.put("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)", "USERID");
			map.put("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)", "USERID");
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
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		if(ismanager==1){//部门管理者
			sb = new StringBuffer();
			sb.append(" select j.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,NVL(D.NUM,1) NUM from ("); 
			sb.append(" select * from (");
			sb.append(" select b.*,rownum rnum from (");
			sb.append(" select * from ("); 
			sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
			sb.append(" SELECT DISTINCT J.* FROM (");
			sb.append(" SELECT C.* from (");
			
			sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,");
			sb.append(" A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,f.sortid,CASE WHEN A.zbspzt is null THEN '未提交' ELSE A.zbspzt END zbspzt,ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID FROM (");
			sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (");
			sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owners = parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
			for (int i = 0; i < owners.length; i++) {
				if(i==(owners.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
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
			sb.append(") union all"); 
			sb.append(" select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in ('') union all"); 
			sb.append(" select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
			sb.append(" inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in ("+parameter.get("userid")+") union all"); 
			
			if(config.equals("2")){
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzrs = parameter.get("CSFZR").split(",");
				for (int i = 0; i < csfzrs.length; i++) {
					if(i==(csfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzrs = parameter.get("FSFZR").split(",");
				for (int i = 0; i < fsfzrs.length; i++) {
					if(i==(fsfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzsprs = parameter.get("ZZSPR").split(",");
				for (int i = 0; i < zzsprs.length; i++) {
					if(i==(zzsprs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(zzsprs[i].replaceAll("'", ""));
				}
				sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
			}else{
				//else
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzrs = parameter.get("CSFZR").split(",");
				for (int i = 0; i < csfzrs.length; i++) {
					if(i==(csfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzrs = parameter.get("FSFZR").split(",");
				for (int i = 0; i < csfzrs.length; i++) {
					if(i==(csfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzsprs = parameter.get("ZZSPR").split(",");
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
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO WHERE 1=1");  
			if (!"".equals(customername) && customername != null) {
				sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
				params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ?");
			params.add("%"+sssyb+"%");
			}
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
			sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"); 
			
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%"+xmjd+"%");
			}
			
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
			sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,F.SORTID"); 
			
			sb.append(" ) C");
			sb.append(" ) J"); 
			sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
			sb.append(" UNION ALL");
			sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
			sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
			sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
			sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
			sb.append(" ZBSTEPID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
			sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
			sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
			if (!"".equals(customername) && customername != null) {
				sb.append(" and upper(JYF) like ?");
				params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND sgfs like ?");
				params.add("%"+sssyb+"%");
			}
			sb.append(" AND PROSTATUS=1");
			sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] userid2 =userid.split(",");
			for (int i = 0; i < userid2.length; i++) {
				if(i==(userid2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid2[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			for (int i = 0; i < userid2.length; i++) {
				if(i==(userid2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid2[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");	
			
			for (int i = 0; i < userid2.length; i++) {
				if(i==(userid2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid2[i].replaceAll("'", ""));
			}
			sb.append("))");
			
			sb.append(" )");
			sb.append(" )");
			sb.append(" order by createdate desc,sortid");
			sb.append(" ) b");
			sb.append(" ) where rnum>? and rnum<=?");
			params.add(startRow1);
			params.add(pageSize1);
			sb.append(" ) j");
			
			sb.append(" LEFT JOIN (");
			
			sb.append(" SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (");
			sb.append(" select * from(");
			sb.append(" select b.*,rownum rnum from (");
			sb.append(" select * from ("); 
			sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
			sb.append(" SELECT DISTINCT J.* FROM (");
			sb.append(" SELECT C.* from (");
			
			sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,");
			sb.append(" A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,f.sortid,CASE WHEN A.zbspzt is null THEN '未提交' ELSE A.zbspzt END zbspzt,ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID FROM (");
			sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (");
			sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owners_ = parameter.get("SUBSTR(OWNER,0, instr(OWNER,'[',1)-1)").split(",");
			for (int i = 0; i < owners_.length; i++) {
				if(i==(owners_.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners_[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			String[] managers_ = parameter.get("SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1)").split(",");
			for (int i = 0; i < managers_.length; i++) {
				if(i==(managers_.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers_[i].replaceAll("'", ""));
			}
			sb.append(") or createuserid in (");
			String[] createuserids_ = parameter.get("createuserid").split(",");
			for (int i = 0; i < createuserids_.length; i++) {
				if(i==(createuserids_.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserids_[i].replaceAll("'", ""));
			}
			sb.append(") union all"); 
			sb.append(" select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in ('') union all"); 
			sb.append(" select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
			sb.append(" inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid in (");
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
			
			if(config.equals("2")){
				sb.append(" select projectno from bd_zqb_pj_base a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in (");
				String[] csfzrs = parameter.get("CSFZR").split(",");
				for (int i = 0; i < csfzrs.length; i++) {
					if(i==(csfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(csfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in (");
				String[] fsfzrs = parameter.get("FSFZR").split(",");
				for (int i = 0; i < fsfzrs.length; i++) {
					if(i==(fsfzrs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(fsfzrs[i].replaceAll("'", ""));
				}
				sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (");
				String[] zzsprs = parameter.get("ZZSPR").split(",");
				for (int i = 0; i < zzsprs.length; i++) {
					if(i==(zzsprs.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(zzsprs[i].replaceAll("'", ""));
				}
				sb.append(")) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
			}else{
				//else
				sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ("+parameter.get("CSFZR")+") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ("+parameter.get("FSFZR")+") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ("+parameter.get("ZZSPR")+"))");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO WHERE 1=1");  
			if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
			params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
			sb.append(" AND SSSYB like ?"); 
			params.add("%"+sssyb+"%");
			}
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
			sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"); 
			
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%"+xmjd+"%");
			}
			
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
			sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,F.SORTID"); 
			
			sb.append(" ) C");
			sb.append(" ) J"); 
			sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
			sb.append(" UNION ALL");
			sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
			sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
			sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
			sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
			sb.append(" ZBSTEPID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
			sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
			sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
			if (!"".equals(customername) && customername != null) {
				sb.append(" and upper(JYF) like ?");
				params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND sgfs like ?");
				params.add("%"+sssyb+"%");
			}
			sb.append(" AND PROSTATUS=1");
			sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");	
			String[] userid1 =userid.split(",");
			for (int i = 0; i < userid1.length; i++) {
				if(i==(userid1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid1[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");	
		
			for (int i = 0; i < userid1.length; i++) {
				if(i==(userid1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid1[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");
			for (int i = 0; i < userid1.length; i++) {
				if(i==(userid1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid1[i].replaceAll("'", ""));
			}
			sb.append("))");
			
			sb.append(" )");
			sb.append(" )");
			sb.append(" order by createdate desc,sortid");
			sb.append(" ) b");
			sb.append(" ) where rnum>? and rnum<=?");
			params.add(startRow1);
			params.add(pageSize1);
			sb.append(" ) L GROUP BY PROJECTNO,CUSTOMERNAME");
			
			sb.append(" ) D ON D.PROJECTNO = J.PROJECTNO");  
			
			sb.append(" LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ"); 
			sb.append(" INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND"); 
			sb.append(" INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND"); 
			sb.append(" ON PJ.ID=BIND.DATAID WHERE PJR='"+userid+"' ORDER BY ID) PJDATA"); 
			params.add(userid);
			sb.append(" ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid");
		}else{//非部门管理者
			sb = new StringBuffer();
			sb.append(" select j.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,NVL(D.NUM,1) NUM from ("); 
			sb.append(" select * from (");
			sb.append(" select b.*,rownum rnum from (");
			sb.append(" select * from ("); 
			sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
			sb.append(" SELECT DISTINCT J.* FROM (");
			sb.append(" SELECT C.* from (");
			sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,");
			sb.append(" A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,f.sortid,CASE WHEN A.zbspzt is null THEN '未提交' ELSE A.zbspzt END zbspzt,ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID FROM (");
			sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (");
			sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all"); 
			params.add(owner);
			params.add(owner);
			params.add(username);
			sb.append(" select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"); 
			params.add(owner);
			sb.append(" select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
			sb.append(" inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
			params.add(userid);
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
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO WHERE 1=1");
			if (!"".equals(customername) && customername != null) {
				sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
				params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND SSSYB like ? ");
				params.add("%"+sssyb+"%");
			}
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
			sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"); 
			
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%"+xmjd+"%");
			}
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
			sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"); 
			sb.append(" WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,F.SORTID"); 
			sb.append(" ) C");
			sb.append(" ) J"); 
			sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
			sb.append(" UNION ALL");
			sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
			sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
			sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
			sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
			sb.append(" ZBSTEPID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
			sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
			sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
			if (!"".equals(customername) && customername != null) {
				sb.append(" and upper(JYF) like ?");
				params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND sgfs like ?");  
				params.add("%"+sssyb+"%");				
			}
			sb.append(" AND PROSTATUS=1");
			sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owner1 =owner.split(",");
			for (int i = 0; i < owner1.length; i++) {
				if(i==(owner1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owner1[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			for (int i = 0; i < owner1.length; i++) {
				if(i==(owner1.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owner1[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");	
			String[] userid2 =userid.split(",");
			for (int i = 0; i < userid2.length; i++) {
				if(i==(userid2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid2[i].replaceAll("'", ""));
			}
			sb.append("))");
			
			sb.append(" )");
			sb.append(" )");
			sb.append(" order by createdate desc,sortid");
			sb.append(" ) b");
			sb.append(" ) where rnum>? and rnum<=?");
			params.add(startRow1);
			params.add(pageSize1);
			sb.append(" ) j");
			sb.append(" LEFT JOIN (");
			sb.append(" SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (");
			sb.append(" select * from(");
			sb.append(" select b.*,rownum rnum from (");
			sb.append(" select * from ("); 
			sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
			sb.append(" SELECT DISTINCT J.* FROM (");
			sb.append(" SELECT C.* from (");
			sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,");
			sb.append(" A.Taskid,A.SPZT xmspzt,A.XMCY,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,f.sortid,CASE WHEN A.zbspzt is null THEN '未提交' ELSE A.zbspzt END zbspzt,ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID FROM (");
			sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (");
			sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all"); 
			params.add(owner);
			params.add(owner);
			params.add(username);
			sb.append(" select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all"); 
			params.add(owner);
			sb.append(" select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
			sb.append(" inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
			params.add(userid);
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
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO WHERE 1=1");
			if (!"".equals(customername) && customername != null) {
				sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
				params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND SSSYB like ? ");
				params.add("%"+sssyb+"%");
			}
			if("1".equals(dgzt)){
				sb.append(" AND QRDG = '已完成' ");
			}else if("2".equals(dgzt)){
				sb.append(" AND QRDG is null ");
			}
			sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
			sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"); 
			
			if ("".equals(xmjd) || xmjd == null) {
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
			} else {
				sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
				params.add("%"+xmjd+"%");
			}
			sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
			sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"); 
			sb.append(" WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,F.SORTID"); 
			sb.append(" ) C");
			sb.append(" ) J"); 
			sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
			sb.append(" UNION ALL");
			sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
			sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
			sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
			sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
			sb.append(" ZBSTEPID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
			sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
			sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
			if (!"".equals(customername) && customername != null) {
				sb.append(" and upper(JYF) like ?");
				params.add("%"+customername+"%");
			}
			if (sssyb!=null&&!sssyb.equals("")) {
				sb.append(" AND sgfs like ?");  
				params.add("%"+sssyb+"%");
			}
			sb.append(" AND PROSTATUS=1");
			sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
			String[] owner2 =owner.split(",");
			for (int i = 0; i < owner2.length; i++) {
				if(i==(owner2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owner2[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
			for (int i = 0; i < owner2.length; i++) {
				if(i==(owner2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owner2[i].replaceAll("'", ""));
			}
			sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");	
			for (int i = 0; i < userid2.length; i++) {
				if(i==(userid2.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userid2[i].replaceAll("'", ""));
			}
			sb.append("))");
			
			sb.append(" )");
			sb.append(" )");
			sb.append(" order by createdate desc,sortid");
			sb.append(" ) b");
			sb.append(" ) where rnum>? and rnum<=?");
			params.add(startRow1);
			params.add(pageSize1);
			sb.append(" ) L GROUP BY PROJECTNO,CUSTOMERNAME");
			sb.append(" ) D ON D.PROJECTNO = J.PROJECTNO");  
			sb.append(" LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ"); 
			sb.append(" INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND");  
			sb.append(" INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID"); 
			sb.append(" INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID");
			params.add(userid);
			sb.append(" ) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid");
		}
		final String sql1 = sb.toString();
		final String jd3=SystemConfig._xmsplcConf.getJd3();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
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
					if (object[35] != null) {
						pjinsid = Integer.valueOf(object[35].toString());
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
					Integer num = Integer.valueOf(object[36].toString());
					
					String lcbh = object[18] == null ? "" : object[18]
							.toString();
					String stepid = object[20] == null ? "" : object[20]
							.toString();
					String xmspzt = object[22] == null ? "" : object[22]
							.toString();
					String xmcy = object[23] == null ? "" : object[23]
							.toString();
					String startdate = object[24] == null ? "" : object[24]
							.toString();
					String enddate = object[25] == null ? "" : object[25]
							.toString();
					Integer lcbs=object[19]==null?0:Integer.valueOf(object[19].toString());
					Integer taskid=object[21]==null?0:Integer.valueOf(object[21].toString());
					String zbspzt = object[27] == null ? "" : object[27].toString();
					String zblcbh = object[28] == null ? "" : object[28].toString();
					Integer zblcbs=object[29]==null?0:Integer.valueOf(object[29].toString());
					Integer zbtaskid=object[30]==null?0:Integer.valueOf(object[30].toString());
					String zbstepid = object[31] == null ? "" : object[31].toString();
					String ptype = object[33].toString();
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
					Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(lcbs.toString()));
					if(newTaskId!=null&&!"".equals(newTaskId)){
						if(userid.equals(newTaskId.getAssignee())){
							map.put("TASKID", newTaskId.getId());
						}else{
							map.put("TASKID", taskid);
						}
					}else{
						map.put("TASKID", taskid);
					}
					
					map.put("ZBSPZT", zbspzt);
					map.put("ZBLCBH", zblcbh);
					map.put("ZBLCBS", zblcbs);
					map.put("ZBTASKID", zbtaskid);
					map.put("ZBSTEPID", zbstepid);
					map.put("PTYPE", ptype.equals("A")?"推荐挂牌":"并购重组");
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
					map.put("ISSTEPID", jd3.equals(stepid)?true:false);
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
					map.put("PJ", pj);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					map.put("XMCY", xmcy);
					map.put("STARTDATE", startdate);
					map.put("ENDDATE", enddate);
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
			sb.append(" AND CUSTOMERNAME like  ");
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
	
	/**
	 * 获取项目信息记录
	 * 
	 * @param startRow
	 * @param pageSize
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
	
	/**导出项目-场外-正在执行-有阶段信息*/
	public List<HashMap> expProjectList1() {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,A.JDID GROUPID,C.SSJE,A.jdmc,C.SPZT,C.MANAGER,BINDTABLE.INSTANCEID,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma,A.SSSYB,to_char(A.CLSLR,'yyyy-MM-dd') clslr,to_char(A.SHTGR,'yyyy-MM-dd') shtgr,A.GZLXR,A.GZLXDH,A.GZYJDZ,to_char(A.YJSBSJ,'yyyy-MM-dd') yjsbsj,A.Sftxcl thcl,A.A01 FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND A.STATUS ='执行中' AND A.XMJD <> '持续督导') A LEFT JOIN BD_PM_TASK C ON A.JDID = C.GROUPID AND A.PROJECTNO = C.PROJECTNO LEFT JOIN (SELECT ZL.JDBH,ZL.PROJECTNO,to_char(wmsys.wm_concat(FF.file_src_name)) filename FROM BD_ZQB_XMRWZLB ZL INNER JOIN sys_upload_file ff on zl.jdzl=ff.file_id GROUP BY ZL.JDBH,ZL.PROJECTNO) E ON A.JDID = E.JDBH AND A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid) C)) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,A.jdmc FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND A.STATUS ='执行中' AND A.XMJD <> '持续督导') A) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
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
					String spzt=(String) object[10];
					String manager=(String) object[11];
					BigDecimal instanceid= (BigDecimal) object[12];
					long lcid = instanceid.longValue();
					//String pjjg=object[13] == null ? "" : object[13].toString();
					
					String startdate=(String) object[13];
					String enddate=(String) object[14];
					String khlxr=(String) object[15];
					String khlxdh=(String) object[16];
					String ggjzr=(String) object[17];
					String sbjzr=(String) object[18];
					String memo=(String) object[19];
					String xmbz=(String) object[20];
					String zclr=(String) object[21];
					String zclrdh=(String) object[22];
					String fzjgmc=(String) object[23];;
					String fzjglxr=(String) object[24];
					String wbclrjg=(String) object[25];
					String filename=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					String sssyb=(String) object[29];
					String clslr=(String) object[30];
					String shtgr=(String) object[31];
					String gzlxr=(String) object[32];
					String gzlxdh=(String) object[33];
					String gzyjdz=(String) object[34];
					String yjsbsj=(String) object[35];
					String thcl=(String) object[36];
					BigDecimal a01=(BigDecimal) object[37];
					map.put("THCL", thcl);
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
					map.put("A01", a01);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**导出项目-场外-正在执行-无阶段信息*/
	public List<HashMap> expProjectList1None() {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		StringBuffer sb = new StringBuffer("SELECT B.INSTANCEID,P.SSSYB,P.CLSLR,P.SHTGR,P.GZLXR,P.GZLXDH,P.GZYJDZ,P.YJSBSJ,P.PROJECTNO,P.CUSTOMERNAME,P.OWNER,P.HTJE,P.SPZT,P.MANAGER,P.STARTDATE,P.ENDDATE,P.KHLXR,P.KHLXDH,P.GGJZR,P.SBJZR,P.MEMO,P.XMBZ,P.ZCLR,P.ZCLRDH,P.FZJGMC,P.FZJGLXR,P.WBCLRJG,P.PROJECTNAME,P.MANAGER,P.A01 FROM BD_ZQB_PJ_BASE P LEFT JOIN SYS_ENGINE_FORM_BIND B ON P.ID=B.DATAID WHERE B.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='项目管理表单') AND (P.TYPENO!=1 OR P.TYPENO IS NULL)");
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
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid= (BigDecimal) object[0];
					String sssyb=(String) object[1];
					Date clslr=(Date) object[2];
					Date shtgr=(Date) object[3];
					String gzlxr=(String) object[4];
					String gzlxdh=(String) object[5];
					String gzyjdz=(String) object[6];
					String yjsbsj=(String) object[7];
					String projectno=(String) object[8];
					String customername=(String) object[9];
					String owner=(String) object[10];
					Double htje=object[11]==null?0.0:((BigDecimal) object[11]).doubleValue();
					String spzt=(String) object[12];
					String manager=(String) object[13];
					Date startdate=(Date) object[14];
					Date enddate=(Date) object[15];
					String khlxr=(String) object[16];
					String khlxdh=(String) object[17];
					Date ggjzr=(Date) object[18];
					Date sbjzr=(Date) object[19];
					String memo=(String) object[20];
					String xmbz=(String) object[21];
					String zclr=(String) object[22];
					String zclrdh=(String) object[23];
					String fzjgmc=(String) object[24];;
					String fzjglxr=(String) object[25];
					String wbclrjg=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					BigDecimal a01=(BigDecimal) object[29];
					String thcl="";
					Integer groupid=0;
					Double ssje=0.0;
					String jdmc="";
					long lcid = instanceid.longValue();
					String filename="";
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
					map.put("SPZT", spzt);
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
					map.put("PROJECTNAME", projectname);
					map.put("XMmanager", XMmanager);
					map.put("A01", a01);
					map.put("THCL", thcl);
					map.put("GROUPID", groupid);
					map.put("SSJE", ssje);
					map.put("JDMC", jdmc);
					map.put("LCID", lcid);
					map.put("FILENAME", filename);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**导出项目-场外-持续督导-有阶段信息*/
	public List<HashMap> expcxddProjectList1() {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,A.JDID GROUPID,C.SSJE,A.jdmc,C.SPZT,C.MANAGER,BINDTABLE.INSTANCEID ,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg ,E.filename,A.PROJECTNAME,A.MANAGER ma,A.SSSYB,to_char(A.CLSLR,'yyyy-MM-dd') clslr,to_char(A.SHTGR,'yyyy-MM-dd') shtgr,A.GZLXR,A.GZLXDH,A.GZYJDZ,to_char(A.YJSBSJ,'yyyy-MM-dd') yjsbsj,A.Sftxcl thcl,A.A01 FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND TYPENO=1) A LEFT JOIN BD_PM_TASK C ON A.JDID = C.GROUPID AND A.PROJECTNO = C.PROJECTNO LEFT JOIN (SELECT ZL.JDBH,ZL.PROJECTNO,to_char(wmsys.wm_concat(FF.file_src_name)) filename FROM BD_ZQB_XMRWZLB ZL INNER JOIN sys_upload_file ff on zl.jdzl=ff.file_id GROUP BY ZL.JDBH,ZL.PROJECTNO) E ON A.JDID = E.JDBH AND A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid) C)) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,A.jdmc FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND TYPENO=1) A) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
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
					String spzt=(String) object[10];
					String manager=(String) object[11];
					BigDecimal instanceid= (BigDecimal) object[12];
					long lcid = instanceid.longValue();
					//String pjjg=object[13] == null ? "" : object[13].toString();
					
					String startdate=(String) object[13];
					String enddate=(String) object[14];
					String khlxr=(String) object[15];
					String khlxdh=(String) object[16];
					String ggjzr=(String) object[17];
					String sbjzr=(String) object[18];
					String memo=(String) object[19];
					String xmbz=(String) object[20];
					String zclr=(String) object[21];
					String zclrdh=(String) object[22];
					String fzjgmc=(String) object[23];;
					String fzjglxr=(String) object[24];
					String wbclrjg=(String) object[25];
					String filename=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					String sssyb=(String) object[29];
					String clslr=(String) object[30];
					String shtgr=(String) object[31];
					String gzlxr=(String) object[32];
					String gzlxdh=(String) object[33];
					String gzyjdz=(String) object[34];
					String yjsbsj=(String) object[35];
					String thcl=(String) object[36];
					BigDecimal a01=(BigDecimal) object[37];
					map.put("THCL", thcl);
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
					map.put("A01", a01);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**导出项目-场外-持续督导-无阶段信息*/
	public List<HashMap> expcxddProjectList1None() {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		StringBuffer sb = new StringBuffer("SELECT B.INSTANCEID,P.SSSYB,P.CLSLR,P.SHTGR,P.GZLXR,P.GZLXDH,P.GZYJDZ,P.YJSBSJ,P.PROJECTNO,P.CUSTOMERNAME,P.OWNER,P.HTJE,P.SPZT,P.MANAGER,P.STARTDATE,P.ENDDATE,P.KHLXR,P.KHLXDH,P.GGJZR,P.SBJZR,P.MEMO,P.XMBZ,P.ZCLR,P.ZCLRDH,P.FZJGMC,P.FZJGLXR,P.WBCLRJG,P.PROJECTNAME,P.MANAGER,P.A01 FROM BD_ZQB_PJ_BASE P LEFT JOIN SYS_ENGINE_FORM_BIND B ON P.ID=B.DATAID WHERE B.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='项目管理表单') AND P.TYPENO=1");
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
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal instanceid= (BigDecimal) object[0];
					String sssyb=(String) object[1];
					Date clslr=(Date) object[2];
					Date shtgr=(Date) object[3];
					String gzlxr=(String) object[4];
					String gzlxdh=(String) object[5];
					String gzyjdz=(String) object[6];
					String yjsbsj=(String) object[7];
					String projectno=(String) object[8];
					String customername=(String) object[9];
					String owner=(String) object[10];
					Double htje=object[11]==null?0.0:((BigDecimal) object[11]).doubleValue();
					String spzt=(String) object[12];
					String manager=(String) object[13];
					Date startdate=(Date) object[14];
					Date enddate=(Date) object[15];
					String khlxr=(String) object[16];
					String khlxdh=(String) object[17];
					Date ggjzr=(Date) object[18];
					Date sbjzr=(Date) object[19];
					String memo=(String) object[20];
					String xmbz=(String) object[21];
					String zclr=(String) object[22];
					String zclrdh=(String) object[23];
					String fzjgmc=(String) object[24];;
					String fzjglxr=(String) object[25];
					String wbclrjg=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					BigDecimal a01=(BigDecimal) object[29];
					String thcl="";
					Integer groupid=0;
					Double ssje=0.0;
					String jdmc="";
					long lcid = instanceid.longValue();
					String filename="";
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
					map.put("SPZT", spzt);
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
					map.put("PROJECTNAME", projectname);
					map.put("XMmanager", XMmanager);
					map.put("A01", a01);
					map.put("THCL", thcl);
					map.put("GROUPID", groupid);
					map.put("SSJE", ssje);
					map.put("JDMC", jdmc);
					map.put("LCID", lcid);
					map.put("FILENAME", filename);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**导出项目-成员-正在执行-有阶段信息*/
	public List<HashMap> expProjectList2(String owner) {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,A.JDID GROUPID,C.SSJE,A.jdmc,C.SPZT, C.MANAGER,BINDTABLE.INSTANCEID,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma,A.SSSYB,to_char(A.CLSLR,'yyyy-MM-dd') clslr,to_char(A.SHTGR,'yyyy-MM-dd') shtgr,A.GZLXR,A.GZLXDH,A.GZYJDZ,to_char(A.YJSBSJ,'yyyy-MM-dd') yjsbsj,A.Sftxcl thcl,A.A01 FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND A.STATUS ='执行中' AND A.XMJD <> '持续督导') A INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) ) H ON H.PROJECTNO = A.PROJECTNO LEFT JOIN BD_PM_TASK C ON A.JDID = C.GROUPID AND A.PROJECTNO = C.PROJECTNO LEFT JOIN (SELECT ZL.JDBH,ZL.PROJECTNO,to_char(wmsys.wm_concat(FF.file_src_name)) filename FROM BD_ZQB_XMRWZLB ZL INNER JOIN sys_upload_file ff on zl.jdzl=ff.file_id GROUP BY ZL.JDBH,ZL.PROJECTNO) E ON A.JDID = E.JDBH AND A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid) C)) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,A.jdmc FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND A.STATUS ='执行中' AND A.XMJD <> '持续督导') A INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) ) H ON H.PROJECTNO = A.PROJECTNO) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(userid);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(userid);
		params.add(owner);
		params.add(owner);
		params.add(owner);
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
					String spzt=(String) object[10];
					String manager=(String) object[11];
					BigDecimal instanceid= (BigDecimal) object[12];
					long lcid = instanceid.longValue();
					//String pjjg=object[13] == null ? "" : object[13].toString();
					
					String startdate=(String) object[13];
					String enddate=(String) object[14];
					String khlxr=(String) object[15];
					String khlxdh=(String) object[16];
					String ggjzr=(String) object[17];
					String sbjzr=(String) object[18];
					String memo=(String) object[19];
					String xmbz=(String) object[20];
					String zclr=(String) object[21];
					String zclrdh=(String) object[22];
					String fzjgmc=(String) object[23];;
					String fzjglxr=(String) object[24];
					String wbclrjg=(String) object[25];
					String filename=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					String sssyb=(String) object[29];
					String clslr=(String) object[30];
					String shtgr=(String) object[31];
					String gzlxr=(String) object[32];
					String gzlxdh=(String) object[33];
					String gzyjdz=(String) object[34];
					String yjsbsj=(String) object[35];
					String thcl=(String) object[36];
					BigDecimal a01=(BigDecimal) object[37];
					map.put("THCL", thcl);
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
					map.put("A01", a01);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**导出项目-成员-正在执行-无阶段信息*/
	public List<HashMap> expProjectList2None(String owner) {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT B.INSTANCEID,P.SSSYB,P.CLSLR,P.SHTGR,P.GZLXR,P.GZLXDH,P.GZYJDZ,P.YJSBSJ,P.PROJECTNO,P.CUSTOMERNAME,P.OWNER,P.HTJE,P.SPZT,P.MANAGER,P.STARTDATE,P.ENDDATE,P.KHLXR,P.KHLXDH,P.GGJZR,P.SBJZR,P.MEMO,P.XMBZ,P.ZCLR,P.ZCLRDH,P.FZJGMC,P.FZJGLXR,P.WBCLRJG,P.PROJECTNAME,P.MANAGER,P.A01 FROM BD_ZQB_PJ_BASE P LEFT JOIN SYS_ENGINE_FORM_BIND B ON P.ID=B.DATAID WHERE B.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='项目管理表单') AND (P.TYPENO!=1 OR P.TYPENO IS NULL) AND P.PROJECTNO IN(SELECT PROJECTNO FROM BD_ZQB_PJ_BASE A WHERE SUBSTR(OWNER,0, INSTR(OWNER,'[',1)-1) =? OR SUBSTR(MANAGER,0, INSTR(MANAGER,'[',1)-1) = ?  OR CREATEUSERID=? UNION ALL SELECT PROJECTNO FROM BD_PM_TASK WHERE SUBSTR(MANAGER,0, INSTR(MANAGER,'[',1)-1) = ? UNION ALL SELECT DISTINCT PROJECTNO FROM (SELECT INSTANCEID,DATAID PROJECTID,B.PROJECTNAME,B.PROJECTNO,B.LCBS FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=101) A LEFT JOIN BD_ZQB_PJ_BASE B ON A.DATAID = B.ID  ) C INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME  FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) D ON C.LCBS = D.INSTANCEID OR C.INSTANCEID=D.INSTANCEID WHERE USERID = ? UNION ALL SELECT PROJECTNO FROM BD_ZQB_PJ_BASE WHERE EXISTS (SELECT ID FROM (SELECT * FROM BD_ZQB_XMSPRWH WHERE ROWNUM = 1 ORDER BY ID) A WHERE SUBSTR(CSFZR,0, INSTR(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, INSTR(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, INSTR(ZZSPR,'[',1)-1) = ?))");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
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
					BigDecimal instanceid= (BigDecimal) object[0];
					String sssyb=(String) object[1];
					Date clslr=(Date) object[2];
					Date shtgr=(Date) object[3];
					String gzlxr=(String) object[4];
					String gzlxdh=(String) object[5];
					String gzyjdz=(String) object[6];
					String yjsbsj=(String) object[7];
					String projectno=(String) object[8];
					String customername=(String) object[9];
					String owner=(String) object[10];
					Double htje=object[11]==null?0.0:((BigDecimal) object[11]).doubleValue();
					String spzt=(String) object[12];
					String manager=(String) object[13];
					Date startdate=(Date) object[14];
					Date enddate=(Date) object[15];
					String khlxr=(String) object[16];
					String khlxdh=(String) object[17];
					Date ggjzr=(Date) object[18];
					Date sbjzr=(Date) object[19];
					String memo=(String) object[20];
					String xmbz=(String) object[21];
					String zclr=(String) object[22];
					String zclrdh=(String) object[23];
					String fzjgmc=(String) object[24];;
					String fzjglxr=(String) object[25];
					String wbclrjg=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					BigDecimal a01=(BigDecimal) object[29];
					String thcl="";
					Integer groupid=0;
					Double ssje=0.0;
					String jdmc="";
					long lcid = instanceid.longValue();
					String filename="";
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
					map.put("SPZT", spzt);
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
					map.put("PROJECTNAME", projectname);
					map.put("XMmanager", XMmanager);
					map.put("A01", a01);
					map.put("THCL", thcl);
					map.put("GROUPID", groupid);
					map.put("SSJE", ssje);
					map.put("JDMC", jdmc);
					map.put("LCID", lcid);
					map.put("FILENAME", filename);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**导出项目-成员-持续督导-有阶段信息*/
	public List<HashMap> expcxddProjectList2(String owner) {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,A.JDID GROUPID,C.SSJE,A.jdmc,C.SPZT, C.MANAGER,BINDTABLE.INSTANCEID,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma,A.SSSYB,to_char(A.CLSLR,'yyyy-MM-dd') clslr,to_char(A.SHTGR,'yyyy-MM-dd') shtgr,A.GZLXR,A.GZLXDH,A.GZYJDZ,to_char(A.YJSBSJ,'yyyy-MM-dd') yjsbsj,A.Sftxcl thcl,A.A01 FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND A.TYPENO =1) A INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) ) H ON H.PROJECTNO = A.PROJECTNO LEFT JOIN BD_PM_TASK C ON A.JDID = C.GROUPID AND A.PROJECTNO = C.PROJECTNO LEFT JOIN (SELECT ZL.JDBH,ZL.PROJECTNO,to_char(wmsys.wm_concat(FF.file_src_name)) filename FROM BD_ZQB_XMRWZLB ZL INNER JOIN sys_upload_file ff on zl.jdzl=ff.file_id GROUP BY ZL.JDBH,ZL.PROJECTNO) E ON A.JDID = E.JDBH AND A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid) C)) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT distinct A.ID,A.PROJECTNO,CUSTOMERNAME,A.jdmc FROM (SELECT A.*,F.ID JDID,F.JDMC FROM BD_ZQB_PJ_BASE A,BD_ZQB_KM_INFO F WHERE F.STATE=1 AND A.TYPENO =1) A INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ?  or createuserid=? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) ) H ON H.PROJECTNO = A.PROJECTNO) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(userid);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(userid);
		params.add(owner);
		params.add(owner);
		params.add(owner);
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
					String spzt=(String) object[10];
					String manager=(String) object[11];
					BigDecimal instanceid= (BigDecimal) object[12];
					long lcid = instanceid.longValue();
					//String pjjg=object[13] == null ? "" : object[13].toString();
					
					String startdate=(String) object[13];
					String enddate=(String) object[14];
					String khlxr=(String) object[15];
					String khlxdh=(String) object[16];
					String ggjzr=(String) object[17];
					String sbjzr=(String) object[18];
					String memo=(String) object[19];
					String xmbz=(String) object[20];
					String zclr=(String) object[21];
					String zclrdh=(String) object[22];
					String fzjgmc=(String) object[23];;
					String fzjglxr=(String) object[24];
					String wbclrjg=(String) object[25];
					String filename=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					String sssyb=(String) object[29];
					String clslr=(String) object[30];
					String shtgr=(String) object[31];
					String gzlxr=(String) object[32];
					String gzlxdh=(String) object[33];
					String gzyjdz=(String) object[34];
					String yjsbsj=(String) object[35];
					String thcl=(String) object[36];
					BigDecimal a01=(BigDecimal) object[37];
					map.put("THCL", thcl);
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
					map.put("A01", a01);
					l.add(map);
				}
				return l;
			}
		});
	}
	/**导出项目-成员-持续督导-无阶段信息*/
	public List<HashMap> expcxddProjectList2None(String owner) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT B.INSTANCEID,P.SSSYB,P.CLSLR,P.SHTGR,P.GZLXR,P.GZLXDH,P.GZYJDZ,P.YJSBSJ,P.PROJECTNO,P.CUSTOMERNAME,P.OWNER,P.HTJE,P.SPZT,P.MANAGER,P.STARTDATE,P.ENDDATE,P.KHLXR,P.KHLXDH,P.GGJZR,P.SBJZR,P.MEMO,P.XMBZ,P.ZCLR,P.ZCLRDH,P.FZJGMC,P.FZJGLXR,P.WBCLRJG,P.PROJECTNAME,P.MANAGER,P.A01 FROM BD_ZQB_PJ_BASE P LEFT JOIN SYS_ENGINE_FORM_BIND B ON P.ID=B.DATAID WHERE B.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM S WHERE S.IFORM_TITLE='项目管理表单') AND P.TYPENO=1 AND P.PROJECTNO IN(SELECT PROJECTNO FROM BD_ZQB_PJ_BASE A WHERE SUBSTR(OWNER,0, INSTR(OWNER,'[',1)-1) =? OR SUBSTR(MANAGER,0, INSTR(MANAGER,'[',1)-1) = ?  OR CREATEUSERID=? UNION ALL SELECT PROJECTNO FROM BD_PM_TASK WHERE SUBSTR(MANAGER,0, INSTR(MANAGER,'[',1)-1) = ? UNION ALL SELECT DISTINCT PROJECTNO FROM (SELECT INSTANCEID,DATAID PROJECTID,B.PROJECTNAME,B.PROJECTNO,B.LCBS FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=101) A LEFT JOIN BD_ZQB_PJ_BASE B ON A.DATAID = B.ID  ) C INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME  FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) D ON C.LCBS = D.INSTANCEID OR C.INSTANCEID=D.INSTANCEID WHERE USERID = ? UNION ALL SELECT PROJECTNO FROM BD_ZQB_PJ_BASE WHERE EXISTS (SELECT ID FROM (SELECT * FROM BD_ZQB_XMSPRWH WHERE ROWNUM = 1 ORDER BY ID) A WHERE SUBSTR(CSFZR,0, INSTR(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, INSTR(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, INSTR(ZZSPR,'[',1)-1) = ?))");
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
		params.add(owner);
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
					BigDecimal instanceid= (BigDecimal) object[0];
					String sssyb=(String) object[1];
					Date clslr=(Date) object[2];
					Date shtgr=(Date) object[3];
					String gzlxr=(String) object[4];
					String gzlxdh=(String) object[5];
					String gzyjdz=(String) object[6];
					String yjsbsj=(String) object[7];
					String projectno=(String) object[8];
					String customername=(String) object[9];
					String owner=(String) object[10];
					Double htje=object[11]==null?0.0:((BigDecimal) object[11]).doubleValue();
					String spzt=(String) object[12];
					String manager=(String) object[13];
					Date startdate=(Date) object[14];
					Date enddate=(Date) object[15];
					String khlxr=(String) object[16];
					String khlxdh=(String) object[17];
					Date ggjzr=(Date) object[18];
					Date sbjzr=(Date) object[19];
					String memo=(String) object[20];
					String xmbz=(String) object[21];
					String zclr=(String) object[22];
					String zclrdh=(String) object[23];
					String fzjgmc=(String) object[24];;
					String fzjglxr=(String) object[25];
					String wbclrjg=(String) object[26];
					String projectname=(String) object[27];
					String XMmanager=(String) object[28];
					BigDecimal a01=(BigDecimal) object[29];
					String thcl="";
					Integer groupid=0;
					Double ssje=0.0;
					String jdmc="";
					long lcid = instanceid.longValue();
					String filename="";
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
					map.put("SPZT", spzt);
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
					map.put("PROJECTNAME", projectname);
					map.put("XMmanager", XMmanager);
					map.put("A01", a01);
					map.put("THCL", thcl);
					map.put("GROUPID", groupid);
					map.put("SSJE", ssje);
					map.put("JDMC", jdmc);
					map.put("LCID", lcid);
					map.put("FILENAME", filename);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> expProjectXGWTList(String projectno,
			String groupid) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select  orguser.username twusername,tw.question question,to_char(tw.createdate,'yyyy-MM-dd hh24:mi') twcreatedate,hf.username hfusername,hf.content content,to_char(hf.createdate,'yyyy-MM-dd hh24:mi') hfcreatedate from BD_ZQB_XMWTFK tw left join (select userid,username from orguser) orguser on tw.userid=orguser.userid left join bd_zqb_retalk hf on tw.id=hf.questionno where tw.xmbh=? and tw.taskno = ?");
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

	public List<HashMap> getShowDailyList(String projectNo, int pageNumber, int pageSize, Long formid,String createuser) {
		
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();;
		Long isManager = orgUser.getIsmanager();
		Long orgRoleid = orgUser.getOrgroleid();
		String userid = orgUser.getUserid();
//		String username=orgUser.getUsername();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT TO_CHAR(RB.PROJECTDATE,'yyyy-MM-dd') PROJECTDATE,RB.PROGRESS,RB.USERNAME,RB.TEL,RB.TRACKING,RB.PROJECTNO,BIND.INSTANCEID,RB.CREATEUSER,RB.CREATEUSERID,RB.EXTEND1,RB.XMBH,D.YJINS FROM BD_ZQB_XMRBB RB INNER JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID=?) BIND ON RB.ID=BIND.DATAID "
				+ " LEFT JOIN (SELECT B.GGINS,S.INSTANCEID AS YJINS FROM BD_XP_GGSMJ B LEFT JOIN SYS_ENGINE_FORM_BIND S ON B.ID=S.DATAID WHERE S.FORMID=148) D ON BIND.INSTANCEID=D.GGINS WHERE 1=1");
		params.add(formid);
//		params.add(projectNo);
//		if(orgRoleid!=5){
//			if(isManager==1l){
//				sb.append(" AND CREATEUSERID IN (SELECT O.USERID FROM ORGUSER O WHERE O.DEPARTMENTID IN (SELECT ID FROM ORGDEPARTMENT T START WITH T.ID=(SELECT DEPARTMENTID FROM ORGUSER O WHERE O.USERID= ?　) CONNECT BY PRIOR T.ID=T.PARENTDEPARTMENTID))");
//			}else{
//				sb.append(" AND CREATEUSERID = ?");
//			}
//			params.add(userid);
//		}
//		if (startdate != null && !"".equals(startdate)) {
//			sb.append(" AND TO_CHAR(PROJECTDATE,'yyyy-MM-dd')>= ?");
//			params.add(startdate);
//		}
//		if (enddate != null && !"".equals(enddate)) {
//			sb.append(" AND TO_CHAR(PROJECTDATE,'yyyy-MM-dd')<= ?");
//			params.add(enddate);
//		}
		if (projectNo != null && !"".equals(projectNo)) {
			sb.append(" AND XMBH like ?");
			params.add("%"+projectNo+"%");
		}
//		if (username != null && !"".equals(username)) {
//			sb.append(" AND CREATEUSER like ?");
//			params.add("%"+username+"%");
//		}
		sb.append(" ORDER BY RB.PROJECTDATE DESC,RB.ID DESC");
		final String sql1 = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				String cuid = UserContextUtil.getInstance().getCurrentUserId();
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
					BigDecimal instanceid = (BigDecimal) object[6];
					String createuser = (String) object[7];
					Object createuserid = object[8]==null?"":object[8];
					String extend1 = (String) object[9];
					String xmbh = (String) object[10];
					BigDecimal YJINS = (BigDecimal)object[11];
					map.put("PROJECTDATE", projectdate);
					map.put("PROGRESS", progress);
					map.put("USERNAME", username);
					map.put("TEL", tel);
					map.put("TRACKING", tracking);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid);
					map.put("CREATEUSER", createuser);
					map.put("SHOWBUTTON", createuserid.equals(cuid));
					map.put("EXTEND1", extend1);
					map.put("XMBH", xmbh);
					map.put("YJINS", YJINS==null?"":YJINS.intValue());
					l.add(map);
				}
				return l;
			}
		});
	}
	
	
	public List<HashMap> showDailyListForIndex(String projectNo, int pageNumber, int pageSize,String startdate,String enddate, Long formid,String createuser) {
		
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();;
		Long isManager = orgUser.getIsmanager();
		Long orgRoleid = orgUser.getOrgroleid();
		String userid = orgUser.getUserid();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT TO_CHAR(RB.PROJECTDATE,'yyyy-MM-dd') PROJECTDATE,RB.PROGRESS,RB.USERNAME,RB.TEL,RB.TRACKING,RB.PROJECTNO,BIND.INSTANCEID,RB.CREATEUSER,RB.CREATEUSERID,RB.EXTEND1,RB.CJSJ FROM BD_ZQB_XMRBB RB INNER JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID=?) BIND ON RB.ID=BIND.DATAID WHERE 1=1");
		params.add(formid);
		sb.append(" AND RB.EXTEND1 LIKE ?");
		params.add("%"+UserContextUtil.getInstance().getCurrentUserFullName()+"%");
		sb.append(" ORDER BY RB.PROJECTDATE DESC,RB.ID DESC");
		final String sql1 = sb.toString();
		final int pageSize1 = pageSize;
		final int startRow1 = (pageNumber - 1) * pageSize;
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				String cuid = UserContextUtil.getInstance().getCurrentUserId();
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
				for (Object[] object : list) {
					map = new HashMap();
					String projectdate = (String) object[0];
					String progress = (String) object[1];
					String username = (String) object[2];
					String tel = (String) object[3];
					String tracking = (String) object[4];
					String projectno = (String) object[5];
					BigDecimal instanceid = (BigDecimal) object[6];
					String createuser = (String) object[7];
					Object createuserid = object[8]==null?"":object[8];
					String extend1 = (String) object[9];
					Date cjsj = (Date) object[10];
					String fssj = "";
					String status = "";
					String hfid = "0";
					String gtcontent = "";
					String hfcontent = "";
					String  tzlx= "项目日志";
					String  sfyhf= "";
					map.put("ID", "0");
					map.put("TZBT", createuser+projectdate+"填写了"+progress+"项目的日志信息，请查看。");
					map.put("ZCHFSJ", cjsj);
					map.put("TZNR", tel);
					map.put("XGZL", tracking);
					map.put("HFR", projectno);
					map.put("SFTZ", instanceid);
					map.put("FSR", createuser);
					map.put("FSSJ", fssj);
					map.put("JSZT", createuserid);
					map.put("FSZT", extend1);
					map.put("STATUS", status==null?"":status);
					map.put("HFID", hfid==null?"0":hfid);
					map.put("GTCONTENT",gtcontent);
					map.put("HFCONTENT",hfcontent);
					map.put("INSTANCEID",instanceid==null?"0":instanceid);
					map.put("TZLX",tzlx);
					map.put("SFYHF",sfyhf);
					
					
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
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno ) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
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
	
	public List<String> getDailyRunProjectList1(String owner) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,NVL(D.NUM,1) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ? union all");
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
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导' ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) J LEFT JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN (select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ? union all");
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
						sb.append(" select projectno from bd_zqb_pj_base WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno left join (select wtfk.taskno taskno,wtfk.xmbh xmbh,wtfk.username twname,wtfk.question question,wtfk.createdate wcreatedate,retalk.username username,retalk.content content,retalk.createdate rcreatedate from BD_ZQB_XMWTFK wtfk left join BD_ZQB_RETALK retalk on wtfk.id=retalk.questionno) xgwt on task.projectno=xgwt.xmbh and task.groupid=xgwt.taskno) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导') F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C)) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<String> l = new ArrayList<String>();
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
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					l.add(projectNo);
				}
				return l;
			}
		});
	}

	public List<HashMap> getDaily(String projectNo) {
		List params = new ArrayList();
		OrgUser orgUser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();;
		Long isManager = orgUser.getIsmanager();
		Long orgRoleid = orgUser.getOrgroleid();
		String userid = orgUser.getUserid();
		StringBuffer sb = new StringBuffer("select to_char(rb.projectdate,'yyyy-MM-dd') projectdate,rb.progress,rb.username,rb.tel,rb.tracking,rb.projectno,rb.createuser from bd_zqb_xmrbb rb where 1=1 ");
//		if(orgRoleid!=5){
//			if(isManager==1l){
//				sb.append(" AND rb.CREATEUSERID IN (SELECT O.USERID FROM ORGUSER O WHERE O.DEPARTMENTID IN (select id from orgdepartment t start with t.id=(SELECT DEPARTMENTID FROM ORGUSER O WHERE O.USERID= ?　) connect by prior t.id=t.parentdepartmentid))");
//			}else{
//				sb.append(" AND rb.CREATEUSERID = ?");
//			}
//			params.add(userid);
//		}
//		if(startdate!=null&&!startdate.equals("")){
//			sb.append(" AND RB.PROJECTDATE>=TO_DATE(?,'yyyy-MM-dd')");
//			params.add(startdate);
//		}
//		if(enddate!=null&&!enddate.equals("")){
//			sb.append(" AND RB.PROJECTDATE<=TO_DATE(?,'yyyy-MM-dd')");
//			params.add(enddate);
//		}
//	
		if (projectNo != null && !"".equals(projectNo)) {
			sb.append(" AND XMBH like ?");
			params.add("%"+projectNo+"%");
		}
//		if (createuser != null && !"".equals(createuser)) {
//			sb.append(" AND CREATEUSER like ?");
//			params.add("%"+createuser+"%");
//		}
		sb.append(" order by rb.projectdate desc,rb.id desc");
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
					String createuser = (String) object[6];
					map.put("PROJECTDATE", projectdate);
					map.put("PROGRESS", progress);
					map.put("USERNAME", username);
					map.put("TEL", tel);
					map.put("TRACKING", tracking);
					map.put("PROJECTNO", projectno);
					map.put("CREATEUSER", createuser);
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
		sb.append(")");
//		if(startdate!=null&&!startdate.equals("")){
//			sb.append(" AND RB.PROJECTDATE>=TO_DATE(?,'yyyy-MM-dd')");
//			params.add(startdate);
//		}
//		if(enddate!=null&&!enddate.equals("")){
//			sb.append(" AND RB.PROJECTDATE<=TO_DATE(?,'yyyy-MM-dd')");
//			params.add(enddate);
//		}
		sb.append(" order by rb.projectno");
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

	//2016-03-16增加SORTID，每个项目按sortid排序
	public List<HashMap> getRunProjectList3(String owner, int pageNow,
			int pageSize, String projectName, String xmjd, String startDate,
			String customername,String dgzt) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		final String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		String username=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
				//.toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append(" select j.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,NVL(D.NUM,1) NUM from (");
		sb.append(" select * from (");
		sb.append(" select b.*,rownum rnum from (");
		sb.append(" select * from ("); 
		sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
		sb.append(" SELECT DISTINCT J.* FROM (");
		sb.append(" SELECT C.* from (");
		sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,");
		sb.append(" TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,A.XMCY,");
		sb.append(" to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,F.SORTID,CASE WHEN A.zbspzt is null THEN '未提交' ELSE A.zbspzt END zbspzt,");
		sb.append(" ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID FROM (");
		sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J"); 
		sb.append(" INNER JOIN (");
		sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? or createuserid=? union all");
		params.add(owner);
		params.add(owner);
		params.add(username);
		sb.append(" select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all");
		params.add(owner);
		sb.append(" select distinct projectno from (");
		sb.append(" select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"); 
		sb.append(" left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
		sb.append(" inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"); 
		sb.append(" left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
		params.add(userid);
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
		sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是' ) H ON H.PROJECTNO = J.PROJECTNO WHERE 1=1");  
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
			params.add("%"+customername+"%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
		sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"); 

		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%"+xmjd+"%");
		}

		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
		sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID");  
		sb.append(" WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,F.SORTID"); 
		sb.append(" ) C");
		sb.append(" ) J"); 
		sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
		sb.append(" UNION ALL");
		sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
		sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
		sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
		sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
		sb.append(" ZBSTEPID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
		sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
		sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
		if (!"".equals(customername) && customername != null) {
			sb.append(" and upper(JYF) like ?");
			params.add("%"+customername+"%");
		}
		sb.append(" AND PROSTATUS=1");
		sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");	
		String[] owner2 =owner.split(",");
		for (int i = 0; i < owner2.length; i++) {
			if(i==(owner2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(owner2[i].replaceAll("'", ""));
		}		
		sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");
		for (int i = 0; i < owner2.length; i++) {
			if(i==(owner2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(owner2[i].replaceAll("'", ""));
		}	
		sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");
		String[] userid2 =userid.split(",");
		for (int i = 0; i < userid2.length; i++) {
			if(i==(userid2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid2[i].replaceAll("'", ""));
		}
		sb.append("))");
		
		sb.append(" )");
		sb.append(" )");
		sb.append(" order by createdate desc,sortid");
		sb.append(" ) b");
		sb.append(" ) where rnum>? and rnum<=?");
		params.add(startRow1);
		params.add(pageSize1);
		sb.append(" ) j");
		sb.append(" LEFT JOIN (");
		sb.append(" SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (");
		sb.append(" select * from(");
		sb.append(" select b.*,rownum rnum from (");
		sb.append(" select * from ("); 
		sb.append(" SELECT p.*,d.createdate,'A' ptype FROM (");
		sb.append(" SELECT DISTINCT J.* FROM (");
		sb.append(" SELECT C.* from (");
		sb.append(" SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.MANAGER AMANAGER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD, E.GROUPID,E.SSJE,F.jdmc,");
		sb.append(" TASK_NAME,E.SPZT,A.SSSYB, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.Lcbh,A.lcbs,A.Stepid,A.Taskid,A.SPZT xmspzt,A.XMCY,");
		sb.append(" to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,F.SORTID,CASE WHEN A.zbspzt is null THEN '未提交' ELSE A.zbspzt END zbspzt,");
		sb.append(" ZBLCBH,ZBLCBS,ZBTASKID,ZBSTEPID FROM (");
		sb.append(" SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J"); 
		sb.append(" INNER JOIN (");
		sb.append(" select projectno from bd_zqb_pj_base a where SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) =? or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? or createuserid=? union all");
		params.add(owner);
		params.add(owner);
		params.add(username);
		sb.append(" select projectno from BD_PM_TASK where SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) = ? union all");
		params.add(owner);
		sb.append(" select distinct projectno from (");
		sb.append(" select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"); 
		sb.append(" left join BD_ZQB_PJ_BASE b on a.dataid = b.id  ) c"); 
		sb.append(" inner join (select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"); 
		sb.append(" left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid or c.instanceid=d.instanceid where userid = ? union all");
		params.add(userid);
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
		sb.append(" union all select projectno from bd_zqb_pj_base where sftxcl='是' ) H ON H.PROJECTNO = J.PROJECTNO WHERE 1=1");  
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper(?)");
			params.add("%"+customername+"%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"); 
		sb.append(" LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO"); 

		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_KM_INFO WHERE JDMC like ?");
			params.add("%"+xmjd+"%");
		}

		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE ON A.id = BINDTABLE.dataid"); 
		sb.append(" LEFT JOIN (select * from BD_ZQB_XMRWPJB a inner join (select max(id) id from BD_ZQB_XMRWPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID");  
		sb.append(" WHERE (F.STATE=1 OR F.STATE IS NULL) AND A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,F.SORTID"); 
		sb.append(" ) C");
		sb.append(" ) J"); 
		sb.append(" ) p LEFT JOIN SYS_ENGINE_FORM_BIND s on p.id=s.dataid left join sys_instance_data d on s.instanceid=d.id where s.formid=91");
		sb.append(" UNION ALL");
		sb.append(" (SELECT BG.ID,'PM0000-00-000'|| BG.ID PROJECTNO,BG.JYF CUSTOMERNAME,BG.OWNER OWNER,BG.MANAGER AMANAGER,0 HTJE,NULL WSJE,");
		sb.append(" NULL XMJD,0 GROUPID,NULL SSJE,NULL JDMC,NULL TASK_NAME,NULL SPZT,sgfs SSSYB,NULL PJINSID,");
		sb.append(" NULL XGWTINSID,s.instanceid INSTANCEID,NULL LDPJ,NULL LCBH,NULL LCBS,NULL STEPID,NULL TASKID,NULL XMSPZT,");
		sb.append(" NULL XMCY,NULL STARTDATE,NULL ENDDATE,NULL SORTID,ZBSPZT,ZBLCBH,ZBLCBS,ZBTASKID,");
		sb.append(" ZBSTEPID,d.createdate,'B' ptype FROM BD_ZQB_BGZZLXXX BG"); 
		sb.append(" LEFT JOIN SYS_ENGINE_FORM_BIND s on bg.id=s.dataid left join sys_instance_data d on s.instanceid=d.id"); 
		sb.append(" where s.formid=(select id from sys_engine_iform s where s.iform_title='并购重组立项信息')");
		if (!"".equals(customername) && customername != null) {
			sb.append(" and upper(JYF) like  ? ");
			params.add("%"+customername+"%");
		}
		sb.append(" AND PROSTATUS=1");
		sb.append(" and (SUBSTR(OWNER,0, instr(OWNER,'[',1)-1) in (");
		for (int i = 0; i < owner2.length; i++) {
			if(i==(owner2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(owner2[i].replaceAll("'", ""));
		}	
		sb.append(") or SUBSTR(MANAGER,0, instr(MANAGER,'[',1)-1) in (");	
		for (int i = 0; i < owner2.length; i++) {
			if(i==(owner2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(owner2[i].replaceAll("'", ""));
		}	
		sb.append(") or SUBSTR(TBRID,0, instr(TBRID,'[',1)-1) in (");
		for (int i = 0; i < userid2.length; i++) {
			if(i==(userid2.length-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
			params.add(userid2[i].replaceAll("'", ""));
		}
		sb.append("))");
		
		sb.append(" )");
		sb.append(" )");
		sb.append(" order by createdate desc,sortid");
		sb.append(" ) b");
		sb.append(" ) where rnum>? and rnum<=?");
		params.add(startRow1);
		params.add(pageSize1);
		sb.append(" ) L GROUP BY PROJECTNO,CUSTOMERNAME");
		sb.append(" ) D ON D.PROJECTNO = J.PROJECTNO");

		sb.append(" LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_XMRWPJB PJ"); 
		sb.append(" INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND"); 
		sb.append(" INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID");  
		sb.append(" INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_XMRWPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR=? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid");
		params.add(userid);

		final String sql1 = sb.toString();
		final String jd3 = SystemConfig._xmsplcConf.getJd3();
		final String DDLC_UUID=ProcessAPI.getInstance().getProcessActDefId("XMSPLC");
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
					if (object[35] != null) {
						pjinsid = Integer.valueOf(object[35].toString());
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
					Integer num = Integer.valueOf(object[36].toString());
					
					String lcbh = object[18] == null ? "" : object[18]
							.toString();
					String stepid = object[20] == null ? "" : object[20]
							.toString();
					String xmspzt = object[22] == null ? "" : object[22]
							.toString();
					String xmcy = object[23] == null ? "" : object[23]
							.toString();
					String startdate = object[24] == null ? "" : object[24]
							.toString();
					String enddate = object[25] == null ? "" : object[25]
							.toString();
					Integer lcbs=object[19]==null?0:Integer.valueOf(object[19].toString());
					Integer taskid=object[21]==null?0:Integer.valueOf(object[21].toString());
					String zbspzt = object[27] == null ? "" : object[27].toString();
					
					String zblcbh = object[28] == null ? "" : object[28].toString();
					Integer zblcbs=object[29]==null?0:Integer.valueOf(object[29].toString());
					Integer zbtaskid=object[30]==null?0:Integer.valueOf(object[30].toString());
					String zbstepid = object[31] == null ? "" : object[31].toString();
					String ptype = object[33].toString();
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
					Task newTaskId = ProcessAPI.getInstance().newTaskId(Long.parseLong(lcbs.toString()));
					if(newTaskId!=null&&!"".equals(newTaskId)){
						if(userid.equals(newTaskId.getAssignee())){
							map.put("TASKID", newTaskId.getId());
						}else{
							map.put("TASKID", taskid);
						}
					}else{
						map.put("TASKID", taskid);
					}
					
					map.put("ZBSPZT", zbspzt);
					map.put("ZBLCBH", zblcbh);
					map.put("ZBLCBS", zblcbs);
					map.put("ZBTASKID", zbtaskid);
					map.put("ZBSTEPID", zbstepid);
					map.put("PTYPE", ptype.equals("A")?"推荐挂牌":"并购重组");
					
					map.put("LCBH", lcbh);
					map.put("LCBS", lcbs);
					map.put("STEPID", stepid);
					map.put("ISSTEPID",jd3.equals(stepid)?true:false);
					map.put("XMSPZT", xmspzt);
					map.put("XMCY", xmcy);
					map.put("STARTDATE", startdate);
					map.put("ENDDATE", enddate);
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
					map.put("PJ", pj);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					l.add(map);
				}
				return l;
			}
		});
	}
	
	public List<HashMap> getPjSettlementSheetList(String sql, List<String> parameter, Map<String, Object> map) {
		Long proDefId = ProcessAPI.getInstance().getProcessOpinion(ProcessAPI.getInstance().getProcessActDefId("XMSPLC"));
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count=1;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				if(parameter.get(i).equals("CUSTOMERNAME")){
					ps.setString(count, "%"+map.get(parameter.get(i)).toString()+"%");
					count++;
				}else{
					ps.setString(count, map.get(parameter.get(i)).toString());
					count++;
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				value.put("CUSTOMERNAME", rs.getString("customername"));
				value.put("CUSTOMERNO", rs.getString("customerno"));
				value.put("XMLX", rs.getString("xmlx"));
				value.put("RZJE", rs.getBigDecimal("rzje"));
				value.put("RZRQ", rs.getDate("rzrq")!=null?(new SimpleDateFormat("yyyy-MM-dd").format(rs.getTimestamp("rzrq"))):"");
				value.put("INSTANCEID", rs.getBigDecimal("instanceid"));
				value.put("PJINSTANCEID", rs.getBigDecimal("pjinstanceid"));
				value.put("PROJECTNO", rs.getString("projectno"));
				value.put("LOCKED", rs.getLong("locked"));
				value.put("ZBSPZT", rs.getString("zbspzt")==null?"未审批":rs.getString("zbspzt"));
				value.put("ZBLCBH", rs.getString("zblcbh"));
				value.put("ZBLCBS", rs.getString("zblcbs"));
				value.put("ZBSTEPID", rs.getString("zbstepid"));
				value.put("ZBTASKID", rs.getString("zbtaskid"));
				value.put("ZBPDFID", proDefId);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public HashMap getPjSettlementSheet(String sql, Map<String, Object> map) {
		Long proDefId = ProcessAPI.getInstance().getProcessOpinion(ProcessAPI.getInstance().getProcessActDefId("XMSPLC"));
		HashMap<String, Object> data = new HashMap<String, Object>();
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		try {
			conn = DBUtil.open();
			CallableStatement cstmt = conn.prepareCall(sql.toString()); //CallableStatement 为调用存储过程的特有类
			cstmt.setString(1, map.get("CUSTOMERNAME")==null?"":map.get("CUSTOMERNAME").toString());
			cstmt.setString(2, map.get("RZLX")==null?"":map.get("RZLX").toString());
			cstmt.setString(3, map.get("RZY")==null?"":map.get("RZY").toString());
			cstmt.setInt(4, Integer.parseInt(map.get("PX")==null||map.get("PX").equals("")?"0":map.get("PX").toString()));
			cstmt.registerOutParameter(5, OracleTypes.CURSOR);
			cstmt.registerOutParameter(6, OracleTypes.NUMBER);
			cstmt.setInt(7, Integer.parseInt(map.get("PAGESTART").toString()));
			cstmt.setInt(8, Integer.parseInt(map.get("PAGEEND").toString()));
			cstmt.execute(); //执行存储过程
			
			ResultSet rs = (ResultSet) cstmt.getObject(5); //获取数据集合
			while (rs.next()) {
				HashMap value = new HashMap();
				value.put("CUSTOMERNAME", rs.getString("customername"));
				value.put("CUSTOMERNO", rs.getString("customerno"));
				value.put("XMLX", rs.getString("xmlx"));
				value.put("RZJE", rs.getBigDecimal("rzje"));
				value.put("SHJE", rs.getBigDecimal("rzje")==null?"":rs.getBigDecimal("rzje").multiply(new BigDecimal(10000)).divide(new BigDecimal("1.06"),2,BigDecimal.ROUND_HALF_UP));
				value.put("RZRQ", rs.getDate("rzrq")!=null?(new SimpleDateFormat("yyyy-MM-dd").format(rs.getTimestamp("rzrq"))):"");
				value.put("INSTANCEID", rs.getBigDecimal("instanceid"));
				value.put("PJINSTANCEID", rs.getBigDecimal("pjinstanceid"));
				value.put("PROJECTNO", rs.getString("projectno"));
				value.put("LOCKED", rs.getLong("locked"));
				value.put("ZBSPZT", rs.getString("projectno")==null?"未关联":rs.getString("zbspzt")==null?"未审批":rs.getString("zbspzt"));
				value.put("ZBLCBH", rs.getString("zblcbh"));
				value.put("ZBLCBS", rs.getString("zblcbs"));
				value.put("ZBSTEPID", rs.getString("zbstepid"));
				value.put("ZBTASKID", rs.getString("zbtaskid"));
				value.put("ZBPDFID", proDefId);
				result.add(value);
			}
			data.put("LIST", result);
			BigDecimal rsnum = (BigDecimal) cstmt.getObject(6); //获取数据集合数
			data.put("SIZE", rsnum.intValue());
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, null, null);
		}
		return data;
	}

	public int getPjSettlementSheetListSize(String sql,List<String> parameter, Map<String, Object> map) {
		Connection conn = null;
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count=1;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				if(parameter.get(i).equals("CUSTOMERNAME")){
					ps.setString(count, "%"+map.get(parameter.get(i)).toString()+"%");
					count++;
				}else{
					ps.setString(count, map.get(parameter.get(i)).toString());
					count++;
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				result=rs.getInt("CNUM");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

	public List<HashMap> getExpXmjsList(String xmlx,String prodate, String departmentname,Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT JS.RZJE RZJE,PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.PROJECTNO PROJECTNO,PJ.SSSYB SSSYB,PJ.LCBS LCBS,PJ.XYZCBL XYZCBL,PJ.INSTANCEID INSTANCEID,PJ.ZBSPZT,PJ.CREATEDATE FROM BD_ZQB_XMJSXXGLB JS LEFT JOIN (SELECT PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.CUSTOMERNO CUSTOMERNO,PJ.PROJECTNO PROJECTNO,PJ.SSSYB SSSYB,PJ.LCBS LCBS,PJ.A01 XYZCBL,BIND.INSTANCEID INSTANCEID,PJ.ZBSPZT,TO_CHAR(PJ.CREATEDATE,'yyyy-MM-dd HH24:MI:SS') CREATEDATE FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT INSTANCEID,DATAID,METADATAID,FORMID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=91 AND METADATAID=101) BIND ON PJ.ID=BIND.DATAID) PJ ON JS.CUSTOMERNO=PJ.CUSTOMERNO WHERE TO_CHAR(JS.RZRQ,'YYYY-MM') = ?");
		parameter.add(prodate);
		if(!isSuperMan){
			sql.append(" AND PJ.SSSYB=?");
			parameter.add(departmentname);
		}
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND JS.XMLX=?");
			parameter.add(xmlx);
		}
		sql.append(" AND PJ.SSSYB IS NOT NULL ORDER BY PJ.SSSYB,JS.ID DESC");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				BigDecimal rzje = rs.getBigDecimal("RZJE");
				Long id = rs.getLong("ID");
				String projectname = rs.getString("PROJECTNAME");
				String projectno = rs.getString("PROJECTNO");
				String sssyb = rs.getString("SSSYB");
				Long lcbs = rs.getLong("LCBS");
				Long instanceid = rs.getLong("INSTANCEID");
				BigDecimal xyzcbl = rs.getBigDecimal("XYZCBL");
				String zbspzt = rs.getString("ZBSPZT")==null?"未提交":rs.getString("ZBSPZT");
				String createdate = rs.getString("CREATEDATE");
				value.put("RZJE", rzje);
				value.put("ID", id);
				value.put("PROJECTNAME", projectname);
				value.put("PROJECTNO", projectno);
				value.put("SSSYB", sssyb);
				value.put("LCBS", lcbs);
				value.put("XYZCBL", xyzcbl);
				value.put("INSTANCEID", instanceid);
				value.put("ZBSPZT", zbspzt);
				value.put("CREATEDATE", createdate);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getExpJsfcList(String xmlx,String prodate, String departmentname,Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		
		sql.append(" SELECT * FROM (");

		sql.append(" SELECT JS.RZRQ,JS.RZJE RZJE,");
		sql.append(" GM.ID ID,GM.PROJECTNAME PROJECTNAME,GM.PROJECTNO PROJECTNO,");
		sql.append(" CASE WHEN (");
		sql.append(" select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=GM.CZBM");
		sql.append(" ) is not null THEN '投行直属事业部' ELSE GM.CZBM END sssyb,GM.CLBM,");
		sql.append(" GM.LCBS LCBS,");
		sql.append(" GM.SPZT ZBSPZT,GM.SJFXGPJG,TO_CHAR(GM.CREATEDATE,'YYYY-MM-DD HH24:MI:SS') CREATEDATE,");

		sql.append(" GMD.DZJE,GM.SJFXZE,GMD.ID GMDID ");
		sql.append(" FROM BD_ZQB_XMJSXXGLB JS ");


				        
		sql.append(" LEFT JOIN BD_ZQB_GPFXXMB GM ON JS.PROJECTNO=GM.PROJECTNO");
		sql.append(" LEFT JOIN BD_ZQB_XMDZ GMD ON GMD.ID=GM.FPBM");
				        
		sql.append(" WHERE TO_CHAR(JS.RZRQ,'YYYY-MM') = ?");
		parameter.add(prodate);
		sql.append(" AND GM.SPZT='审批通过'");

		sql.append(" )  WHERE SSSYB IS NOT NULL ORDER BY SSSYB,RZRQ DESC,ID");
		
		
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				BigDecimal rzje = rs.getBigDecimal("RZJE");
				Long id = rs.getLong("ID");
				String projectname = rs.getString("PROJECTNAME");
				String projectno = rs.getString("PROJECTNO");
				String sssyb = rs.getString("SSSYB");
				String clbm = rs.getString("CLBM");
				Long lcbs = rs.getLong("LCBS");
				String zbspzt = rs.getString("ZBSPZT")==null?"未提交":rs.getString("ZBSPZT");
				String createdate = rs.getString("CREATEDATE");
				String dzje = rs.getString("DZJE");
				String sjfxze = rs.getString("SJFXZE");
				BigDecimal gmdid = rs.getBigDecimal("GMDID");
				BigDecimal sjfxgpjg = rs.getBigDecimal("SJFXGPJG");
				value.put("RZJE", rzje);
				value.put("ID", id);
				value.put("PROJECTNAME", projectname);
				value.put("PROJECTNO", projectno);
				value.put("SSSYB", sssyb);
				value.put("CLBM", clbm);
				value.put("LCBS", lcbs);
				value.put("ZBSPZT", zbspzt);
				value.put("CREATEDATE", createdate);
				value.put("DZJE", dzje);
				value.put("SJFXZE", sjfxze);
				value.put("GMDID", gmdid);
				value.put("SJFXGPJG", sjfxgpjg);
				value.put("PROJECTNO", projectno);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getExpJsfcListNotSptg(String xmlx,String prodate) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		
		sql.append(" SELECT * FROM (");

		sql.append(" SELECT JS.RZRQ,JS.RZJE RZJE,");
		sql.append(" GM.ID ID,GM.PROJECTNAME PROJECTNAME,KH.CUSTOMERNAME,GM.PROJECTNO PROJECTNO,");
		sql.append(" CASE WHEN (");
		sql.append(" select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=GM.CZBM");
		sql.append(" ) is not null THEN '投行直属事业部' ELSE GM.CZBM END sssyb,GM.CLBM,");
		sql.append(" GM.LCBS LCBS,");
		sql.append(" GM.SPZT ZBSPZT,TO_CHAR(GM.CREATEDATE,'YYYY-MM-DD HH24:MI:SS') CREATEDATE,");

		sql.append(" GMD.DZJE,GM.SJFXZE,GMD.ID GMDID ");
		sql.append(" FROM BD_ZQB_XMJSXXGLB JS ");


				        
		sql.append(" LEFT JOIN BD_ZQB_GPFXXMB GM ON JS.PROJECTNO=GM.PROJECTNO");
		sql.append(" LEFT JOIN BD_ZQB_XMDZ GMD ON GMD.ID=GM.FPBM");
		sql.append(" LEFT JOIN BD_ZQB_KH_BASE KH ON JS.CUSTOMERNO=KH.CUSTOMERNO");
				        
		sql.append(" WHERE TO_CHAR(JS.RZRQ,'YYYY-MM') = ?");
		parameter.add(prodate);
		sql.append(" AND (GM.SPZT!='审批通过' OR GM.SPZT IS NULL)");
		
		sql.append(" AND XMLX=?");
		parameter.add(xmlx);
		sql.append(" )  ORDER BY SSSYB,RZRQ DESC");
		
		
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				BigDecimal rzje = rs.getBigDecimal("RZJE");
				Long id = rs.getLong("ID");
				String projectname = rs.getString("PROJECTNAME")==null?rs.getString("CUSTOMERNAME"):rs.getString("PROJECTNAME");
				String projectno = rs.getString("PROJECTNO");
				String sssyb = rs.getString("SSSYB");
				String clbm = rs.getString("CLBM");
				Long lcbs = rs.getLong("LCBS");
				String zbspzt = rs.getString("ZBSPZT")==null?"未提交":rs.getString("ZBSPZT");
				String createdate = rs.getString("CREATEDATE");
				String dzje = rs.getString("DZJE");
				String sjfxze = rs.getString("SJFXZE");
				BigDecimal gmdid = rs.getBigDecimal("GMDID");
				value.put("RZJE", rzje);
				value.put("ID", id);
				value.put("PROJECTNAME", projectname);
				value.put("PROJECTNO", projectno);
				value.put("SSSYB", sssyb);
				value.put("CLBM", clbm);
				value.put("LCBS", lcbs);
				value.put("ZBSPZT", zbspzt);
				value.put("CREATEDATE", createdate);
				value.put("DZJE", dzje);
				value.put("SJFXZE", sjfxze);
				value.put("GMDID", gmdid);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getSssybJsfcList(String xmlx,String prodate, String departmentname, Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append(" SELECT DISTINCT SSSYB FROM (");

		sql.append(" SELECT JS.RZRQ,JS.RZJE RZJE,");
		sql.append(" GM.ID ID,GM.PROJECTNAME PROJECTNAME,GM.PROJECTNO PROJECTNO,");
		sql.append(" CASE WHEN (");
		sql.append(" select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=GM.CZBM");
		sql.append(" ) is not null THEN '投行直属事业部' ELSE GM.CZBM END sssyb,GM.CLBM,");
		sql.append(" GM.LCBS LCBS,");
		sql.append(" GM.SPZT ZBSPZT,TO_CHAR(GM.CREATEDATE,'YYYY-MM-DD HH24:MI:SS') CREATEDATE,");

		sql.append(" GMD.DZJE,GM.SJFXZE,GMD.ID GMDID ");
		sql.append(" FROM BD_ZQB_XMJSXXGLB JS ");


				        
		sql.append(" LEFT JOIN BD_ZQB_GPFXXMB GM ON JS.PROJECTNO=GM.PROJECTNO");
		sql.append(" LEFT JOIN BD_ZQB_XMDZ GMD ON GMD.ID=GM.FPBM");
				        
		sql.append(" WHERE TO_CHAR(JS.RZRQ,'YYYY-MM') = ?");
		parameter.add(prodate);
		sql.append(" AND GM.SPZT='审批通过'");


		sql.append(" )  WHERE SSSYB IS NOT NULL");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				String sssyb = rs.getString("SSSYB");
				value.put("SSSYB", sssyb);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getExpXmjsListNoPro(String xmlx,String prodate) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT JS.ID,JS.USERID,JS.DQRQ,JS.XMLX,JS.RZJE,JS.RZRQ,JS.CUSTOMERNAME FROM BD_ZQB_XMJSXXGLB JS LEFT JOIN BD_ZQB_PJ_BASE P ON JS.CUSTOMERNO=P.CUSTOMERNO WHERE 1=1");
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND JS.XMLX=?");
			parameter.add(xmlx);
		}
		sql.append(" AND P.PROJECTNO IS NULL");
		
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			BigDecimal sl = new BigDecimal("1.06");
			while (rs.next()) {
				HashMap value = new HashMap();
				String customername = rs.getString("CUSTOMERNAME");
				String xml_x = rs.getString("XMLX");
				BigDecimal rzje = rs.getBigDecimal("RZJE").multiply(new BigDecimal("10000"));
				BigDecimal shje = rzje.divide(sl,2,BigDecimal.ROUND_HALF_UP);
				Date rzrq = new Date();rzrq.setTime(rs.getDate("RZRQ").getTime());
				String rzr_q = UtilDate.dateFormat(rzrq);
				value.put("CUSTOMERNAME", customername);
				value.put("XMLX", xml_x);
				value.put("RZJE", rzje.toString());
				value.put("SHJE", shje.toString());
				value.put("RZRQ", rzr_q);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getExpXmjsSptgList(String xmlx,String prodate, String departmentname,Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT * FROM (SELECT JS.RZRQ,JS.RZJE RZJE,PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.PROJECTNO PROJECTNO,CASE WHEN (select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=pj.sssyb) is not null THEN '投行直属事业部' ELSE pj.sssyb END sssyb,PJ.LCBS LCBS,PJ.XYZCBL XYZCBL,PJ.INSTANCEID INSTANCEID,PJ.ZBSPZT,TO_CHAR(PJ.CREATEDATE,'YYYY-MM-DD HH24:MI:SS') CREATEDATE FROM BD_ZQB_XMJSXXGLB JS INNER JOIN (SELECT PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.CUSTOMERNO CUSTOMERNO,PJ.PROJECTNO PROJECTNO,PJ.SSSYB SSSYB,PJ.LCBS LCBS,PJ.A01 XYZCBL,BIND.INSTANCEID INSTANCEID,PJ.ZBSPZT,PJ.CREATEDATE,SUBFORM.FPBL FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT INSTANCEID,DATAID,METADATAID,FORMID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=91 AND METADATAID=101) BIND ON PJ.ID=BIND.DATAID INNER JOIN (SELECT A.INSTANCEID,SUM(B.FPBL) FPBL FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYTITLE='承揽机构')) A INNER JOIN BD_ZQB_CLJG B ON A.DATAID = B.ID GROUP BY A.INSTANCEID) SUBFORM ON BIND.INSTANCEID=SUBFORM.INSTANCEID WHERE FPBL=100) PJ ON JS.CUSTOMERNO=PJ.CUSTOMERNO "
				+ " WHERE TO_CHAR(JS.RZRQ,'YYYY-MM') = ? AND (PJ.ZBSPZT='审批通过' OR TO_CHAR(PJ.CREATEDATE,'YYYY-MM-DD')<='2016-08-20') ");
		parameter.add(prodate);
		if(!isSuperMan){
			sql.append(" AND PJ.SSSYB = ? ");
			parameter.add(departmentname);
		}
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND JS.XMLX=?");
			parameter.add(xmlx);
		}	
		sql.append(" )  WHERE SSSYB IS NOT NULL ORDER BY SSSYB,RZRQ DESC");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				BigDecimal rzje = rs.getBigDecimal("RZJE");
				Long id = rs.getLong("ID");
				String projectname = rs.getString("PROJECTNAME");
				String projectno = rs.getString("PROJECTNO");
				String sssyb = rs.getString("SSSYB");
				Long lcbs = rs.getLong("LCBS");
				Long instanceid = rs.getLong("INSTANCEID");
				BigDecimal xyzcbl = rs.getBigDecimal("XYZCBL");
				String zbspzt = rs.getString("ZBSPZT")==null?"未提交":rs.getString("ZBSPZT");
				String createdate = rs.getString("CREATEDATE");
				value.put("RZJE", rzje);
				value.put("ID", id);
				value.put("PROJECTNAME", projectname);
				value.put("PROJECTNO", projectno);
				value.put("SSSYB", sssyb);
				value.put("LCBS", lcbs);
				value.put("XYZCBL", xyzcbl);
				value.put("INSTANCEID", instanceid);
				value.put("ZBSPZT", zbspzt);
				value.put("CREATEDATE", createdate);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getExpXmjsWspList(String xmlx,String prodate, String departmentname,Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT * FROM (SELECT JS.RZRQ,JS.RZJE RZJE,PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.PROJECTNO PROJECTNO,CASE WHEN (select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=pj.sssyb) is not null THEN '投行直属事业部' ELSE pj.sssyb END sssyb,PJ.LCBS LCBS,PJ.XYZCBL XYZCBL,PJ.INSTANCEID INSTANCEID,PJ.ZBSPZT,PJ.CREATEDATE FROM BD_ZQB_XMJSXXGLB JS LEFT JOIN (SELECT PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.CUSTOMERNO CUSTOMERNO,PJ.PROJECTNO PROJECTNO,PJ.SSSYB SSSYB,PJ.LCBS LCBS,PJ.A01 XYZCBL,BIND.INSTANCEID INSTANCEID,PJ.ZBSPZT,TO_CHAR(PJ.CREATEDATE,'YYYY-MM-DD HH24:MI:SS') CREATEDATE FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT INSTANCEID,DATAID,METADATAID,FORMID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=91 AND METADATAID=101) BIND ON PJ.ID=BIND.DATAID) PJ ON JS.CUSTOMERNO=PJ.CUSTOMERNO WHERE PJ.CREATEDATE >= '2016-08-20' AND (PJ.ZBSPZT<>'审批通过' OR PJ.ZBSPZT IS NULL) ");
		if(!isSuperMan){
			sql.append(" AND PJ.SSSYB=?");
			parameter.add(departmentname);
		}
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND JS.XMLX=?");
			parameter.add(xmlx);
		}
		sql.append(" AND PJ.SSSYB IS NOT NULL) ORDER BY SSSYB,RZRQ DESC");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				BigDecimal rzje = rs.getBigDecimal("RZJE");
				Long id = rs.getLong("ID");
				String projectname = rs.getString("PROJECTNAME");
				String projectno = rs.getString("PROJECTNO");
				String sssyb = rs.getString("SSSYB");
				Long lcbs = rs.getLong("LCBS");
				Long instanceid = rs.getLong("INSTANCEID");
				BigDecimal xyzcbl = rs.getBigDecimal("XYZCBL");
				String zbspzt = rs.getString("ZBSPZT")==null?"未提交":rs.getString("ZBSPZT");
				String createdate = rs.getString("CREATEDATE");
				value.put("RZJE", rzje);
				value.put("ID", id);
				value.put("PROJECTNAME", projectname);
				value.put("PROJECTNO", projectno);
				value.put("SSSYB", sssyb);
				value.put("LCBS", lcbs);
				value.put("XYZCBL", xyzcbl);
				value.put("INSTANCEID", instanceid);
				value.put("ZBSPZT", zbspzt);
				value.put("CREATEDATE", createdate);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getSssybList(String xmlx,String prodate, String departmentname, Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT PJ.SSSYB SSSYB FROM BD_ZQB_XMJSXXGLB JS LEFT JOIN BD_ZQB_PJ_BASE PJ ON JS.CUSTOMERNO=PJ.CUSTOMERNO WHERE TO_CHAR(JS.RZRQ,'YYYY-MM') = ?");
		parameter.add(prodate);
		if(!isSuperMan){
			sql.append(" AND PJ.SSSYB=?");
			parameter.add(departmentname);
		}
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND JS.XMLX=?");
			parameter.add(xmlx);
		}
		sql.append(" AND PJ.SSSYB is not null GROUP BY PJ.SSSYB");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				String sssyb = rs.getString("SSSYB");
				value.put("SSSYB", sssyb);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getSssybSptgList(String xmlx,String prodate, String departmentname, Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT SSSYB FROM (SELECT CASE WHEN (select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=pj.sssyb) is not null THEN '投行直属事业部' ELSE pj.sssyb END sssyb FROM BD_ZQB_XMJSXXGLB JS INNER JOIN (SELECT PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.CUSTOMERNO CUSTOMERNO,PJ.PROJECTNO PROJECTNO,PJ.SSSYB SSSYB,PJ.LCBS LCBS,PJ.A01 XYZCBL,BIND.INSTANCEID INSTANCEID,PJ.ZBSPZT,PJ.CREATEDATE,SUBFORM.FPBL FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT INSTANCEID,DATAID,METADATAID,FORMID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=91 AND METADATAID=101) BIND ON PJ.ID=BIND.DATAID INNER JOIN (SELECT A.INSTANCEID,SUM(B.FPBL) FPBL FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=(SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYTITLE='承揽机构')) A INNER JOIN BD_ZQB_CLJG B ON A.DATAID = B.ID GROUP BY A.INSTANCEID) SUBFORM ON BIND.INSTANCEID=SUBFORM.INSTANCEID WHERE FPBL=100) PJ ON JS.CUSTOMERNO=PJ.CUSTOMERNO "
				+ " WHERE TO_CHAR(JS.RZRQ,'YYYY-MM') = ? AND (PJ.ZBSPZT='审批通过' OR TO_CHAR(PJ.CREATEDATE,'YYYY-MM-DD')<='2016-08-20') ");
		parameter.add(prodate);
		if(!isSuperMan){
			sql.append(" AND PJ.SSSYB = ?");
			parameter.add(departmentname);
		}
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND JS.XMLX = ?");
			parameter.add(xmlx);
		}
		sql.append(" ) WHERE SSSYB IS NOT NULL GROUP BY SSSYB ORDER BY SSSYB");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				String sssyb = rs.getString("SSSYB");
				value.put("SSSYB", sssyb);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap> getSssybWspList(String xmlx,String prodate, String departmentname, Boolean isSuperMan) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		sql.append("SELECT SSSYB FROM (SELECT CASE WHEN (select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=pj.sssyb) is not null THEN '投行直属事业部' ELSE pj.sssyb END sssyb FROM BD_ZQB_XMJSXXGLB JS LEFT JOIN (SELECT PJ.ID ID,PJ.PROJECTNAME PROJECTNAME,PJ.CUSTOMERNO CUSTOMERNO,PJ.PROJECTNO PROJECTNO,PJ.SSSYB SSSYB,PJ.LCBS LCBS,PJ.A01 XYZCBL,BIND.INSTANCEID INSTANCEID,PJ.ZBSPZT,TO_CHAR(PJ.CREATEDATE,'YYYY-MM-DD HH24:MI:SS') CREATEDATE FROM BD_ZQB_PJ_BASE PJ INNER JOIN (SELECT INSTANCEID,DATAID,METADATAID,FORMID FROM SYS_ENGINE_FORM_BIND WHERE FORMID=91 AND METADATAID=101) BIND ON PJ.ID=BIND.DATAID) PJ ON JS.CUSTOMERNO=PJ.CUSTOMERNO WHERE PJ.CREATEDATE>='2016-08-20' AND (PJ.ZBSPZT<>'审批通过' OR PJ.ZBSPZT IS NULL) ");
		if(!isSuperMan){
			sql.append(" AND PJ.SSSYB=?");
			parameter.add(departmentname);
		}
		if(xmlx!=null&&!xmlx.equals("")){
			sql.append(" AND JS.XMLX=?");
			parameter.add(xmlx);
		}
		sql.append(" AND PJ.SSSYB IS NOT NULL) GROUP BY SSSYB");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				String sssyb = rs.getString("SSSYB");
				value.put("SSSYB", sssyb);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
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

	public List<HashMap> zqbAddxmcyDepartmentZtree() {
		StringBuffer sb = new StringBuffer("SELECT ORGDE.ID,ORGDE.DEPARTMENTNAME,ORGDE.PARENTDEPARTMENTID FROM ORGDEPARTMENT ORGDE WHERE ID!=51 AND PARENTDEPARTMENTID!=51 AND ID!=65 AND PARENTDEPARTMENTID!=65 ORDER BY ORGDE.PARENTDEPARTMENTID");
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				List<HashMap> l = new ArrayList<HashMap>();
				List<Object[]> list = query.list();
				HashMap map;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					BigDecimal id = (BigDecimal) object[0];
					String name = (String) object[1];
					BigDecimal pId = (BigDecimal) object[2];
					map.put("id", id);
					map.put("pId", pId);
					map.put("name", name);
					l.add(map);
				}
				return l;
			}
		});
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

	public HashMap<String,BigDecimal> getExpYwhz(String prodate) {
		HashMap<String,BigDecimal> map=new HashMap<String,BigDecimal>();
		StringBuffer sql = new StringBuffer("select sum(rzje) je,xmlx from BD_ZQB_XMJSXXGLB WHERE TO_CHAR(RZRQ,'YYYY-MM') = ? group by xmlx");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1,prodate);
			rset = stmt.executeQuery();
			while (rset.next()) {
				BigDecimal je = rset.getBigDecimal("je");
				String xmlx = rset.getString("xmlx");
				map.put(xmlx, je);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rset);
		}
		return map;
	}

	public List<HashMap> getYdqyList(String beginprodate, String endprodate) {
		StringBuffer sql=new StringBuffer();
		List<String> parameter=new ArrayList<String>();//存放参数
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
		sql.append("select kh.customername,kh.zqjc,kh.type,kh.zwmc,kh.zcqx,pp.sssyb,pp.fzjgmc,pp.khlxr,pp.startdate,pp.enddate,kh.sshy,kh.gb,kh.qnjlr,kh.qiannjlr,kh.jlnjlr,kh.Yxdk,kh.gqrz,kh.zqrz,kh.qtrz,numbase.num from (select pj.*,pjmonth.ksrq from (select pjbase.* from bd_zqb_pj_base pjbase inner join (select max(id) id,customerno from bd_zqb_pj_base where customerno is not null group by customerno) pjid on pjbase.id=pjid.id) pj inner join (select to_char(startdate,'yyyy-mm') as ksrq from bd_zqb_pj_base group by to_char(startdate,'yyyy-mm')) pjmonth on to_char(startdate,'yyyy-mm')=pjmonth.ksrq) pp inner join bd_zqb_kh_base kh on pp.customerno=kh.customerno left join (select count(*) num,to_char(startdate,'yyyy-mm') rq from (select pj.*,pjmonth.ksrq from (select pjbase.* from bd_zqb_pj_base pjbase inner join (select max(id) id,customerno from bd_zqb_pj_base where customerno is not null group by customerno) pjid on pjbase.id=pjid.id) pj inner join (select to_char(startdate,'yyyy-mm') as ksrq from bd_zqb_pj_base group by to_char(startdate,'yyyy-mm')) pjmonth on to_char(startdate,'yyyy-mm')=pjmonth.ksrq) pp inner join bd_zqb_kh_base kh on pp.customerno=kh.customerno group by to_char(startdate,'yyyy-mm')) numbase on pp.ksrq=numbase.rq"); 
		sql.append(" where 1=1 ");
		if(beginprodate!=null&&!beginprodate.equals("")){
			sql.append(" AND pp.ksrq >= ? ");
			parameter.add(beginprodate);
		}
		if(endprodate!=null&&!endprodate.equals("")){
			sql.append(" AND pp.ksrq <= ? ");
			parameter.add(endprodate);
		}
		
		sql.append(" order by pp.ksrq,pp.id");
		Connection conn = null;
		List<HashMap> result = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap value = new HashMap();
				String customername = rs.getString("customername");
				String zqjc = rs.getString("zqjc");
				String type = rs.getString("type");
				String zwmc = rs.getString("zwmc");
				String zcqx = rs.getString("zcqx");
				String sssyb = rs.getString("sssyb");
				String fzjgmc = rs.getString("fzjgmc");
				String khlxr = rs.getString("khlxr");
				String enddate = rs.getDate("enddate")==null?"":df.format(rs.getDate("enddate"));
				java.sql.Date date = rs.getDate("startdate");
				String sshy = rs.getString("sshy");
				BigDecimal gb = rs.getBigDecimal("gb");
				BigDecimal qnjlr = rs.getBigDecimal("qnjlr");
				BigDecimal qiannjlr = rs.getBigDecimal("qiannjlr");
				BigDecimal jlnjlr = rs.getBigDecimal("jlnjlr");
				BigDecimal yxdk = rs.getBigDecimal("yxdk");
				BigDecimal gqrz = rs.getBigDecimal("gqrz");
				BigDecimal zqrz = rs.getBigDecimal("zqrz");
				BigDecimal qtrz = rs.getBigDecimal("qtrz");
				Integer num = rs.getInt("num");
				value.put("customername", customername);
				value.put("zqjc", zqjc);
				value.put("type", type);
				value.put("zwmc", zwmc);
				value.put("zcqx", zcqx);
				value.put("sssyb", sssyb);
				value.put("fzjgmc", fzjgmc);
				value.put("khlxr", khlxr);
				value.put("enddate", enddate);
				value.put("sshy", sshy);
				value.put("gb", gb);
				value.put("qnjlr", qnjlr);
				value.put("qiannjlr", qiannjlr);
				value.put("jlnjlr", jlnjlr);
				value.put("yxdk", yxdk);
				value.put("gqrz", gqrz);
				value.put("zqrz", zqrz);
				value.put("qtrz", qtrz);
				value.put("num", num);
				value.put("date", date);
				result.add(value);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public HashMap getExpProjectWordDataDZ(Long instanceId) {
		StringBuffer sql=new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("M");
		sql.append(" SELECT XMJS.CUSTOMERNAME,XMJS.RZJE,XMJS.XMLX,RZRQ,");
		sql.append(" PJBASE.INSTANCEID,PJBASE.LCBS,PJBASE.PROJECTNAME,PJBASE.CUSTOMERNO,CASE WHEN (");
		sql.append(" select dept.departmentname from orgdepartment dept inner join (select id from orgdepartment where departmentname='投行直属事业部') zssyb on dept.parentdepartmentid=zssyb.id where dept.departmentname=PJBASE.CZBM");
		sql.append(" ) is not null THEN '投行直属事业部' ELSE PJBASE.CZBM END CZBM,PJBASE.SJFXGPJG,");
		sql.append(" CASE WHEN TO_NUMBER(DZ.DZJE) IS NULL THEN PJBASE.SJFXZE ELSE TO_NUMBER(DZ.DZJE) END FPBM,");
		sql.append(" SYSBIND.INSTANCEID SINSID,XMJS.ID,XMJS.PROJECTNO");
		sql.append(" FROM SYS_ENGINE_FORM_BIND SYSBIND INNER JOIN BD_ZQB_XMJSXXGLB XMJS ON SYSBIND.DATAID=XMJS.ID");
		sql.append(" LEFT JOIN (");
		sql.append("      SELECT BIND.INSTANCEID,A.* FROM BD_ZQB_GPFXXMB A ");
		sql.append("      INNER JOIN SYS_ENGINE_FORM_BIND BIND ON A.ID=BIND.DATAID WHERE FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股票发行项目')");
		sql.append(" ) PJBASE ON XMJS.PROJECTNO=PJBASE.PROJECTNO");
		sql.append(" LEFT JOIN BD_ZQB_XMDZ DZ ON PJBASE.FPBM=DZ.ID");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND SYSBIND.INSTANCEID = ?");
		sql.append(" AND SYSBIND.FORMID=(SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='项目结算信息管理')");
		sql.append(" ORDER BY XMJS.RZRQ DESC,XMJS.ID DESC");
		Connection conn = null;
		HashMap result = new HashMap();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceId);
			rs = ps.executeQuery();
			while (rs.next()) {
				String customername = rs.getString("customername");
				BigDecimal rzje = rs.getBigDecimal("rzje");
				String xmlx = rs.getString("xmlx");
				String rzrq = df.format(rs.getDate("rzrq"));
				Long instanceid = rs.getLong("instanceid");
				Long lcbs = rs.getLong("lcbs");
				String projectname = rs.getString("projectname");
				String customerno = rs.getString("customerno");
				String czbm = rs.getString("czbm");
				String sjfxgpjg = rs.getString("sjfxgpjg");
				String fpbm = rs.getString("fpbm");
				BigDecimal id = rs.getBigDecimal("id");
				String projectno = rs.getString("projectno");
				result.put("CUSTOMERNAME", customername);
				result.put("RZJE", rzje);
				result.put("XMLX", xmlx);
				result.put("RZRQ", rzrq);
				result.put("INSTANCEID", instanceid);
				result.put("LCBS", lcbs);
				result.put("PROJECTNAME", projectname);
				result.put("CUSTOMERNO", customerno);
				result.put("CZBM", czbm);
				result.put("SJFXGPJG", sjfxgpjg);
				result.put("FPBM", fpbm);
				result.put("ID", id);
				result.put("PROJECTNO", projectno);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}

	public HashMap getExpProjectWordData(Long instanceId) {
		StringBuffer sql=new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("M");
		sql.append("select xmjs.customername,xmjs.rzje,xmjs.xmlx,rzrq,pjbase.instanceid,pjbase.lcbs,pjbase.projectname,pjbase.customerno,CASE WHEN pjbase.xyzcbl is null THEN 0 ELSE pjbase.xyzcbl END xyzcbl,xmdz.dzje from SYS_ENGINE_FORM_BIND sysbind inner join bd_zqb_xmjsxxglb xmjs on sysbind.dataid=xmjs.id left join (select bind.instanceid,probase.lcbs,probase.projectname,probase.customerno,probase.a01 xyzcbl,probase.a03 dzid from bd_zqb_pj_base probase inner join SYS_ENGINE_FORM_BIND bind on probase.id=bind.dataid where formid=91) pjbase on xmjs.customerno=pjbase.customerno left join BD_ZQB_XMDZ xmdz on pjbase.dzid=xmdz.id where sysbind.instanceid = ? and sysbind.formid=(select formid from sys_dem_engine where title='项目结算信息管理')");
		Connection conn = null;
		HashMap result = new HashMap();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql.toString());
			ps.setLong(1, instanceId);
			rs = ps.executeQuery();
			while (rs.next()) {
				String customername = rs.getString("customername");
				BigDecimal rzje = rs.getBigDecimal("rzje");
				String xmlx = rs.getString("xmlx");
				String rzrq = df.format(rs.getDate("rzrq"));
				Long instanceid = rs.getLong("instanceid");
				Long lcbs = rs.getLong("lcbs");
				String projectname = rs.getString("projectname");
				String customerno = rs.getString("customerno");
				BigDecimal xyzcbl = rs.getBigDecimal("xyzcbl");
				String dzje = rs.getString("dzje");
				result.put("CUSTOMERNAME", customername);
				result.put("RZJE", rzje);
				result.put("XMLX", xmlx);
				result.put("RZRQ", rzrq);
				result.put("INSTANCEID", instanceid);
				result.put("LCBS", lcbs);
				result.put("PROJECTNAME", projectname);
				result.put("CUSTOMERNO", customerno);
				result.put("XYZCBL", xyzcbl);
				result.put("DZJE", dzje);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return result;
	}
	
	public List<HashMap<String, Long>> getProjectDataList(String sql) {
		List<HashMap<String, Long>> dataList=new ArrayList<HashMap<String,Long>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				Long lcbs = rs.getLong("LCBS");
				Long instanceId = rs.getLong("INSTANCEID");
				result.put("LCBS", lcbs);
				result.put("INSTANCEID", instanceId);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap> getZkList(String sql, List parameter, int pageNumber, int pageSize) {
		List<HashMap> dataList=new ArrayList<HashMap>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			ps.setInt(index++, startRow);
			ps.setInt(index++, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				Long zid = rs.getLong("zid");
				Long zinstanceid = rs.getLong("zinstanceid");
				String pjno = rs.getString("zpjno");
				String zkdate = rs.getString("zkdate");
				String zkr = rs.getString("zkr");
				String zkcbsj = rs.getString("zkcbsj");
				String pjname = rs.getString("pjname");
				String jdmc = rs.getString("jdmc");
				String owner = rs.getString("owner");
				String lcbh = rs.getString("lcbh");
				Integer lcbs = rs.getInt("lcbs");
				Integer taskid = rs.getInt("taskid");
				Integer rownum = rs.getInt("rm");
				String lcCreateUser = rs.getString("LCCREATEUSER");
				String szzkspr = rs.getString("SZZKSPR");
				String pjprono = rs.getString("PJPRONO");
				result.put("ZID", zid);
				result.put("ZINSTANCEID", zinstanceid);
				result.put("PJNO", pjno);
				result.put("ZKDATE", zkdate);
				result.put("ZKR", zkr);
				result.put("ZKCBSJ", zkcbsj);
				result.put("PJNAME", pjname);
				result.put("JDMC", jdmc);
				result.put("OWNER", owner);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", taskid);
				result.put("LCCREATEUSER", lcCreateUser);
				result.put("SZZKSPR", szzkspr);
				result.put("PJPRONO", pjprono);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public int getZkListSize(String sql, List parameter) {
		int count=0;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				count = rs.getInt("cnum");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}
	
	public List<HashMap> getNhList(String sql, List parameter, int pageNumber, int pageSize) {
		List<HashMap> dataList=new ArrayList<HashMap>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			ps.setInt(index++, startRow);
			ps.setInt(index++, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				Long nid = rs.getLong("nid");
				Long ninstanceid = rs.getLong("ninstanceid");
				String pjno = rs.getString("npjno");
				String nhdate = rs.getString("nhdate");
				String nhspr = rs.getString("nhspr");
				String nhcbsj = rs.getString("nhcbsj");
				String pjname = rs.getString("pjname");
				String jdmc = rs.getString("jdmc");
				String owner = rs.getString("owner");
				String lcbh = rs.getString("lcbh");
				String lccreateuser = rs.getString("LCCREATEUSER");
				String sznhspr = rs.getString("SZNHSPR");
				String pjprono = rs.getString("PJPRONO");
				Integer lcbs = rs.getInt("lcbs");
				Integer taskid = rs.getInt("taskid");
				Integer rownum = rs.getInt("rm");
				result.put("NID", nid);
				result.put("NINSTANCEID", ninstanceid);
				result.put("PJNO", pjno);
				result.put("NHDATE", nhdate);
				result.put("NHSPR", nhspr);
				result.put("NHCBSJ", nhcbsj);
				result.put("PJNAME", pjname);
				result.put("JDMC", jdmc);
				result.put("OWNER", owner);
				result.put("LCBH", lcbh);
				result.put("LCBS", lcbs);
				result.put("TASKID", taskid);
				result.put("LCCREATEUSER", lccreateuser);
				result.put("SZNHSPR", sznhspr);
				result.put("PJPRONO", pjprono);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public int getNhListSize(String sql, List parameter) {
		int count=0;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				count = rs.getInt("cnum");
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return count;
	}

	public List getJdmcList() {
		Session session = HibernateUtil.getSession();
		Query query = session.createSQLQuery("SELECT JDMC FROM BD_ZQB_KM_INFO WHERE STATE=1 ORDER BY SORTID");
		List list = query.list();
		HibernateUtil.closeSession();
		return list;
	}
	
	public List<HashMap> expZkList(String sql, List parameter) {
		List<HashMap> dataList=new ArrayList<HashMap>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				Long zid = rs.getLong("zid");
				Long zinstanceid = rs.getLong("zinstanceid");
				String pjno = rs.getString("zpjno");
				String zkdate = rs.getString("zkdate");
				String zkr = rs.getString("zkr");
				String zkcbsj = rs.getString("zkcbsj");
				String pjname = rs.getString("pjname");
				String jdmc = rs.getString("jdmc");
				String owner = rs.getString("owner");
				String lccreateuser = rs.getString("lccreateuser");
				Integer rownum = rs.getInt("rm");
				result.put("ZID", zid);
				result.put("ZINSTANCEID", zinstanceid);
				result.put("LCCREATEUSER", lccreateuser);
				result.put("PJNO", pjno);
				result.put("ZKDATE", zkdate);
				result.put("ZKR", zkr);
				result.put("ZKCBSJ", zkcbsj);
				result.put("PJNAME", pjname);
				result.put("JDMC", jdmc);
				result.put("OWNER", owner);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	public List<HashMap> expNhList(String sql, List parameter) {
		List<HashMap> dataList=new ArrayList<HashMap>();
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, "%"+parameter.get(i).toString().toUpperCase()+"%");
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				Long nid = rs.getLong("nid");
				Long ninstanceid = rs.getLong("ninstanceid");
				String pjno = rs.getString("npjno");
				String nhdate = rs.getString("nhdate");
				String nhspr = rs.getString("nhspr");
				String nhcbsj = rs.getString("nhcbsj");
				String pjname = rs.getString("pjname");
				String jdmc = rs.getString("jdmc");
				String owner = rs.getString("owner");
				String lccreateuser = rs.getString("lccreateuser");
				Integer rownum = rs.getInt("rm");
				result.put("NID", nid);
				result.put("LCCREATEUSER", lccreateuser);
				result.put("NINSTANCEID", ninstanceid);
				result.put("PJNO", pjno);
				result.put("NHDATE", nhdate);
				result.put("NHSPR", nhspr);
				result.put("NHCBSJ", nhcbsj);
				result.put("PJNAME", pjname);
				result.put("JDMC", jdmc);
				result.put("OWNER", owner);
				result.put("ROWNUM", rownum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
	
	public List<HashMap> getLogList(String sql, List<String> parameter) {
		List<HashMap> dataList=new ArrayList<HashMap>();
		Connection conn = null;
		int index=1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString().toUpperCase());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap result = new HashMap();
				String createdate = rs.getString("CREATEDATE");
				String username = rs.getString("USERNAME");
				String memo = rs.getString("MEMO");
				result.put("CREATEDATE", createdate);
				result.put("USERNAME", username);
				result.put("MEMO", memo);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List<HashMap<String, Object>> getXgwtList(String sql, int pageNumber, int pageSize, List<String> parameter) {
		List<HashMap<String,Object>> dataList=new ArrayList<HashMap<String,Object>>();
		int startRow = (pageNumber - 1) * pageSize;
		int endRow = pageNumber * pageSize;
		int index=1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(index++, parameter.get(i).toString().toUpperCase());
			}
			ps.setInt(index++, startRow);
			ps.setInt(index++, endRow);
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> result = new HashMap<String,Object>();
				Long id = rs.getLong("ID");
				String xmbh = rs.getString("XMBH");
				String xmmc = rs.getString("XMMC");
				String username = rs.getString("USERNAME");
				String question = rs.getString("QUESTION");
				Long kmid = rs.getLong("KMID");
				String jdmc = rs.getString("JDMC");
				Long instanceid = rs.getLong("INSTANCEID");
				Long lcbs = rs.getLong("LCBS");
				String lcbh = rs.getString("LCBH");
				Long taskid = rs.getLong("TASKID");
				Long rnum = rs.getLong("RNUM");
				result.put("ID", id);
				result.put("XMBH", xmbh);
				result.put("XMMC", xmmc);
				result.put("USERNAME", username);
				result.put("QUESTION", question);
				result.put("KMID", kmid);
				result.put("JDMC", jdmc);
				result.put("INSTANCEID", instanceid);
				result.put("LCBS", lcbs);
				result.put("LCBH", lcbh);
				result.put("TASKID", taskid);
				result.put("RNUM", rnum);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}

	public List getZkAndNhDate(String sql, List<String> parameter) {
		List<HashMap<String,String>> dataList=new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.open();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < parameter.size(); i++) {
				ps.setString(i+1, parameter.get(i).toString());
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap<String,String> result = new HashMap<String,String>();
				String type = rs.getString("TYPE");
				String rdate = rs.getString("RDATE")==null?"":rs.getString("RDATE").toString();
				result.put("TYPE", type);
				result.put("RDATE", rdate);
				dataList.add(result);
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally{
			DBUtil.close(conn, ps, rs);
		}
		return dataList;
	}
}
