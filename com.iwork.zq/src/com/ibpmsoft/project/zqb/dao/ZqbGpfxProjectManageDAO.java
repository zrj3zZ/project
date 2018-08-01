package com.ibpmsoft.project.zqb.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.model.UploadDocModel;
import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.DBUtilInjection;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.FileUploadAPI;

public class ZqbGpfxProjectManageDAO extends HibernateDaoSupport {

	public List<UploadDocModel> getDocList(String searchkey, String owner) {
		String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		String config = SystemConfig._gpfxXmlcConf.getConfig();
		List<UploadDocModel> list = new ArrayList();
		String sql = "";
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		List parameter=new ArrayList();//存放参数
		StringBuffer sb = new StringBuffer();
		if (searchkey == null) {
			// sql = "select * from View_pj_Attach";
			if ("".equals(owner)) {
				sb.append("select * from (select * from (select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,A.XMJD,"
						+ " E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
						sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
						parameter.add(gpfxFormId);
								sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
										+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
										+ " ORDER BY A.ID DESC) C"
										+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_TYXMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) union all SELECT projectname,projectno,REGEXP_SUBSTR(attach, '[^,]+', 1, rn) jdzl FROM bd_zqb_gpfxxmb, (SELECT ROWNUM rn FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(attach, '[^,]+', 1, rn) IS NOT NULL) a,(select * from sys_upload_file) b where a.jdzl like '%'||b.file_id||'%'");
								
								/*BD_ZQB_TYXMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) a,(select * from sys_upload_file) b where a.jdzl like '%'||b.file_id||'%'");*/
						parameter.add(gpfxFormId);
						sql=sb.toString();
				/*sql = "select base.projectname,base.projectno,task.jdzl from BD_ZQB_PJ_BASE base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc";*/
			} else {
						sb.append("select * from (select * from (select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,A.XMJD,"
						+ " E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ?"
		              //分派项目审批人
		              	+ " union all");
						parameter.add(owner);
						parameter.add(userid);
						parameter.add(owner);
						parameter.add(owner);
						parameter.add(gpfxFormId);
						parameter.add(userid);
		              //if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						parameter.add(owner);
						parameter.add(owner);
						parameter.add(owner);
		              //else
						}else{
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
		              	+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
		              	+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						parameter.add(owner);
						parameter.add(owner);
						parameter.add(owner);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
						sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ("
		                       //项目的负责人、现场负责人
								+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ?"
								+ " union all"
		                        //任务阶段负责人
		                        + " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
		                        //项目及成员信息
		                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
		                        + " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
		                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
		                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ?");
						parameter.add(gpfxFormId);
						parameter.add(owner);
						parameter.add(userid);
						parameter.add(owner);
						parameter.add(owner);
						parameter.add(gpfxFormId);
						parameter.add(userid);
		                        //分派项目审批人
								sb.append(" union all");
		                        //if 按多项目组审批
								if(config.equals("2")){
								sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
										+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?"
										+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
								parameter.add(owner);
								parameter.add(owner);
								parameter.add(owner);
								}else{
		                        //else
								sb.append(" select projectno from BD_ZQB_GPFXXMB"
										+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)"
										+ " ");
								parameter.add(owner);
								parameter.add(owner);
								parameter.add(owner);
								}
								sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
								sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
										+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
										+ " ORDER BY A.ID DESC) C "
										+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_TYXMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) union all SELECT projectname,projectno,REGEXP_SUBSTR(attach, '[^,]+', 1, rn) jdzl FROM bd_zqb_gpfxxmb, (SELECT ROWNUM rn FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(attach, '[^,]+', 1, rn) IS NOT NULL) a,(select * from sys_upload_file) b where a.jdzl like '%'||b.file_id||'%'");
								parameter.add(gpfxFormId);
				sql=sb.toString();
								/*sql = "select base.projectname,base.projectno,task.jdzl from BD_ZQB_PJ_BASE base, BD_ZQB_XMRWZLB task where base.projectno = task.projectno  and (base.owner='"
						+ owner
						+ "' or base.manager='"
						+ owner
						+ "') and task.jdzl is not null order by task.id desc";*/
			}
		} else {
			if ("".equals(owner)) {
				sb.append("select * from (select * from (select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,A.XMJD,"
						+ " E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
						sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
						parameter.add(gpfxFormId);
								sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
										+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
										+ " ORDER BY A.ID DESC) C"
										+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_TYXMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) union all SELECT projectname,projectno,REGEXP_SUBSTR(attach, '[^,]+', 1, rn) jdzl FROM bd_zqb_gpfxxmb, (SELECT ROWNUM rn FROM DUAL CONNECT BY ROWNUM <= 50) WHERE REGEXP_SUBSTR(attach, '[^,]+', 1, rn) IS NOT NULL) a,(select * from sys_upload_file where file_src_name like ? or file_src_name like ?) b where a.jdzl like '%'||b.file_id||'%'");
						parameter.add(gpfxFormId);
						parameter.add(searchkey.toUpperCase());
						parameter.add(searchkey.toLowerCase());
								sql=sb.toString();
				
			} else {
				sb.append("select * from (select base.projectname,base.projectno,task.jdzl from (SELECT DISTINCT J.PROJECTNO,J.PROJECTNAME,J.ID,D.NUM FROM (SELECT * from (SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,A.XMJD,"
						+ " E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ?"
		              //分派项目审批人
		              	+ " union all");
						parameter.add(owner);
						parameter.add(userid);
						parameter.add(owner);
						parameter.add(owner);
						parameter.add(gpfxFormId);
						parameter.add(userid);
		              //if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						parameter.add(owner);
						parameter.add(owner);
						parameter.add(owner);
						//else
						}else{
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
		              	+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
		              	+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						parameter.add(owner);
						parameter.add(owner);
						parameter.add(owner);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
						sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO"
								+ " LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " ORDER BY A.ID DESC"
								+ " ) C ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM ("
								+ " SELECT A.ID,A.PROJECTNO,A.PROJECTNAME,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,ROWNUM RN,nvl(g.ldpj,'评价') ldpj"
								+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ("
		                       //项目的负责人、现场负责人
								+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ?"
								+ " union all"
		                        //任务阶段负责人
		                        + " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
		                        //项目及成员信息
		                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
		                        + " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
		                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
		                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ?");
								parameter.add(gpfxFormId);
								parameter.add(owner);
								parameter.add(userid);
								parameter.add(owner);
								parameter.add(owner);
								parameter.add(gpfxFormId);
								parameter.add(userid);
		                        //分派项目审批人
								sb.append(" union all");
		                        //if 按多项目组审批
								if(config.equals("2")){
								sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
										+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?"
										+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
									parameter.add(owner);
									parameter.add(owner);
									parameter.add(owner);
								}else{
		                        //else
								sb.append(" select projectno from BD_ZQB_GPFXXMB"
										+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)"
										+ " ");
								parameter.add(owner);
								parameter.add(owner);
								parameter.add(owner);
								}
								sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
								sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
									
								// WHERE JDMC LIKE '%股改%'
								sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
										+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
										+ " ORDER BY A.ID DESC) C "
										+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid) base, BD_ZQB_TYXMRWZLB task where base.projectno = task.projectno and task.jdzl is not null order by task.id desc) a,(select * from sys_upload_file where file_src_name like ? or file_src_name like ?) b where a.jdzl like '%'||b.file_id||'%'");
								parameter.add(gpfxFormId);
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
				/* String task_name = rset.getString("task_name"); */
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
				/* model.setTaskName(task_name); */
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
		sql.append("select * from BD_ZQB_GPFXXGWTB where taskno=? and xmbh=?");
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
		Long formid = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE = '股票发行问题回复表单'", "FORMID");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT BOTABLE.PROJECTNO,BOTABLE.TASKNO,BOTABLE.CONTENT,BOTABLE.ATTACH,BOTABLE.USERID,BOTABLE.USERNAME,BOTABLE.QUESTIONNO,BOTABLE.CREATEDATE,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_ZQB_GPFXWTHFB  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid= ? AND BINDTABLE.INSTANCEID= ? ORDER BY botable.ID");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = DBUtil.open();
			stmt =conn.prepareStatement(sql.toString());
			stmt.setLong(1, formid);
			stmt.setString(2, instanceid);
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
		sql.append("select XMJD,count(*) num from BD_ZQB_GPFXXMB ");
		if (userAddress != null) {
			sql.append(" where 1=1 ");
			sql.append("AND SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?");
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
		sql.append("select status,count(*) num from BD_ZQB_GPFXXMB ");
		sql.append(" where 1=1  ");
		if (userid != null) {
			sql.append("AND SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?");
		}
		sql.append(" group by status");
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
		sql1.append("select '关闭项目' as TITLE, count(*) as num from (select projectName, projectNo  from  BD_ZQB_GPFXXMB where status = '已完成' ");
		sql3.append("select  '正常项目' as TITLE,count(*)as num from (select projectName,projectNo  from BD_ZQB_GPFXXMB where ( 1=1) ");
		if (projectNoList != null && projectNoList.size() > 0) {
			sql1.append(" and (1=2 ");
			sql3.append(" and (1=2 ");
			for (String projectNo : projectNoList) {
				sql1.append(" or projectno = ? ");
				sql3.append(" or projectno = ? ");
				parameter1.add(projectNo);
				parameter2.add(projectNo);
			}
			sql1.append(")) ");
			sql3.append(") ");
		} else {
			sql1.append(") ");
		}

		sql3.append(" and status = '执行中' group by projectName,projectNo )");
		sql.append(sql1).append(" union ")
				.append(sql3);
		parameter1.addAll(parameter2);
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
		sql4.append("select '持续督导项目' as TITLE, count(*) as num from (select projectName, projectNo from BD_ZQB_PJ_BASE  where  xmjd = '持续督导' ) ");
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
			// sql2.append(") ");
			// sql3.append(") ");
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
		sql.append("select count(case when FXPGFS between 90 and 100 then 1 end) as A1,count(case when FXPGFS between 80 and 89 then 1 end) as A2,count(case when FXPGFS between 60 and 79 then 1 end) as A3,count(case when FXPGFS<60 then 1 end) as A4 from BD_ZQB_GPFXXMB ");
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
		sql.append("select owner, count(*) num from BD_ZQB_GPFXXMB group by owner");
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
				// typename =
				// UserContextUtil.getInstance().getUserName(typename);
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
		sql.append("select (case xmjd when '项目开发' then '无阶段' else xmjd end) xmjd,count(*) as num from BD_ZQB_GPFXXMB where 1=1 ");
		if (pjNoList != null && pjNoList.size() > 0) {
			sql.append(" and  (1=2 ");
			for (String projectNo : pjNoList) {
				sql.append(" or projectno = ? ");
				parameter.add(projectNo);
			}
			sql.append(")");
		}
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
		sql.append("select xmjd,count(*) as num from BD_ZQB_GPFXXMB where 1=1 ");
		if (pjNoList != null && pjNoList.size() > 0) {
			sql.append(" and  (1=2 ");
			for (String projectNo : pjNoList) {
				sql.append(" or projectno = ? ");
				parameter.add(projectNo);
			}
			sql.append(")");
		}
		sql.append(" and (status='已完成' and xmjd='持续督导' or status<>'已完成')");
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
		sql.append("select xmjd,count(*) as num from BD_ZQB_GPFXXMB where 1=1  and (status='已完成' and xmjd='持续督导' or status<>'已完成')group by xmjd");

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
		sql.append("select (case xmjd when '项目开发' then '无阶段' else xmjd end) xmjd, count(*) as num from BD_ZQB_GPFXXMB where 1=1 ");
		if (userid != null) {
			sql.append("AND ( SUBSTR(owner,0, instr(owner,'[',1)-1)=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?) ");
		}
		sql.append("group by xmjd");
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
		sql.append("select manager,sum(htje)as htje ,sum(ssje) as ssje from (select  p.projectno,p.projectname,p.htje,s.manager,ssje from BD_ZQB_GPFXXMB p inner join (select projectno,manager,sum(ssje) ssje from BD_PM_TASK group by projectno,manager) s on p.projectno = s.projectno) group by manager order by ssje,htje");
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
		//sql.append("  SELECT PROJECTNO,PROJECTNAME,SUM(HTJE) HTJE,SUM(NUM) YSKE FROM (select PROJECTNO,PROJECTNAME,HTJE,(HTJE-SSJE)  NUM from view_pj_money order by NUM desc) group by PROJECTNO,PROJECTNAME");
		sql.append("SELECT PROJECTNO,PROJECTNAME,SUM(HTJE) HTJE,SUM(NUM) YSKE FROM (select PROJECTNO,PROJECTNAME,HTJE,(HTJE-num)  NUM from (select * from (select  p.projectno,p.projectname,p.htje,ssje num from (select * from BD_ZQB_GPFXXMB where status='执行中') p inner join (select projectno,sum(ssje) ssje from BD_PM_TASK group by projectno) s on p.projectno = s.projectno)) order by NUM desc) group by PROJECTNO,PROJECTNAME");
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
		//sql.append("SELECT PROJECTNO,PROJECTNAME,SUM(HTJE) HTJE,SUM(NUM) YSKE FROM (select PROJECTNO,PROJECTNAME,HTJE,SSJE  NUM from view_pj_money order by NUM desc) group by PROJECTNO,PROJECTNAME");
		sql.append("SELECT PROJECTNO,PROJECTNAME,SUM(HTJE) HTJE,SUM(NUM) YSKE FROM (select * from (select  p.projectno,p.projectname,p.htje,ssje num from (select * from BD_ZQB_GPFXXMB where status='执行中') p inner join (select projectno,sum(ssje) ssje from BD_PM_TASK group by projectno) s on p.projectno = s.projectno)) group by PROJECTNO,PROJECTNAME");
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
				stmt.setString(1, groupid);
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
			sql.append(" where 1=1 AND (POSITION IS NULL OR POSITION IS NOT NULL) AND userid=?");
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
		sql.append("select count(*) c from bd_pm_task where  projectno=?");
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
		sql2.append("select count(*) NUM  from BD_ZQB_GPFXXMB where ( 1=1)");
		if (!projectNo.equals("")) {
			sql2.append(" and (1=2 ");
			sql2.append(" or projectno = ? ");
			sql2.append(") ");
		}
		sql2.append(" and status = '执行中' group by projectName,projectNo ");
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
		sql.append("select status,count(*) as num from BD_ZQB_GPFXXMB group by status");
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
			String customername,String dgzt, String czbm, String cyrName) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		final String userid=UserContextUtil.getInstance()
				.getCurrentUserContext()._userModel.getUserid()
				.toString().trim();
		List params=new ArrayList();
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
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD, E.GROUPID,F.jdmc,E.SPZT, E.MANAGER,E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.SPZT ZBSPZT FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
		if(cyrName!=null&&!cyrName.equals("")){
			sb.append(" INNER JOIN ( select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
			String[] owners = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
			for (int i = 0; i < owners.length; i++) {
				if(i==(owners.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owners[i].replaceAll("'", ""));
			}
			sb.append( " )  or createuserid in ( ");
			String[] createuserid = parameter.get("createuserid").split(",");
			for (int i = 0; i < createuserid.length; i++) {
				if(i==(createuserid.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserid[i].replaceAll("'", ""));
			}
			sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
			String[] manager = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < manager.length; i++) {
				if(i==(manager.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(manager[i].replaceAll("'", ""));
			}
			sb.append(" ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
			String[] managers = parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < managers.length; i++) {
				if(i==(managers.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers[i].replaceAll("'", ""));
			}
			sb.append(" ) union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a");
			params.add(gpfxFormId);
			sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
					+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
					+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
					+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
			String[] userids = parameter.get("userid").split(",");
			for (int i = 0; i < userids.length; i++) {
				if(i==(userids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userids[i].replaceAll("'", ""));
			}
			sb.append(" ) union all");
			
			if (config.equals("2")) {
				sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
				String[] CSFZR = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < CSFZR.length; i++) {
					if(i==(CSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(CSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");	
				String[] FSFZR = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < FSFZR.length; i++) {
					if(i==(FSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(FSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");	
				String[] ZZSPR = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < ZZSPR.length; i++) {
					if(i==(ZZSPR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(ZZSPR[i].replaceAll("'", ""));
				}
				sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
			} else {
				sb.append(" select projectno from BD_ZQB_GPFXXMB"
						+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
						+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");	
				String[] CSFZR = parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < CSFZR.length; i++) {
					if(i==(CSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(CSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
				String[] FSFZR = parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < FSFZR.length; i++) {
					if(i==(FSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(FSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
				String[] ZZSPR = parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < ZZSPR.length; i++) {
					if(i==(ZZSPR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(ZZSPR[i].replaceAll("'", ""));
				}
				sb.append(" ))");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND CZBM like ? ");
			params.add("%"+czbm+"%");
		}
		sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
			+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID "
			+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
			+ " ) C"
			+") WHERE RN > ? AND RN <=  ? ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
			+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER,E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.SPZT ZBSPZT"
			+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J");
		params.add(gpfxFormId);
		params.add(startRow1);
		params.add(pageSize1);
		if(cyrName!=null&&!cyrName.equals("")){
			sb.append(" INNER JOIN ("
				// 项目的负责人、现场负责人
				+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
			
			String[] owner = parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
			for (int i = 0; i < owner.length; i++) {
				if(i==(owner.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(owner[i].replaceAll("'", ""));
			}
			sb.append(" )  or createuserid in ( ");
			String[] createuserid =parameter.get("createuserid").split(",");
			for (int i = 0; i < createuserid.length; i++) {
				if(i==(createuserid.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(createuserid[i].replaceAll("'", ""));
			}
			sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
			String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < manager.length; i++) {
				if(i==(manager.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(manager[i].replaceAll("'", ""));
			}
			sb.append(" ) union all"
				// 任务阶段负责人
				+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
			String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
			for (int i = 0; i < managers.length; i++) {
				if(i==(managers.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(managers[i].replaceAll("'", ""));
			}
			sb.append(") union all"
				// 项目及成员信息
				+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a");
			params.add(gpfxFormId);
			sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
				+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
			String[] userids =parameter.get("userid").split(",");
			for (int i = 0; i < userids.length; i++) {
				if(i==(userids.length-1)){
					sb.append("?");
				}else{
					sb.append("?,");
				}
				params.add(userids[i].replaceAll("'", ""));
			}
			sb.append(" ) union all");
			
			if (config.equals("2")) {
				sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
				String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < CSFZR.length; i++) {
					if(i==(CSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(CSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
				String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < FSFZR.length; i++) {
					if(i==(FSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(FSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
				String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < ZZSPR.length; i++) {
					if(i==(ZZSPR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(ZZSPR[i].replaceAll("'", ""));
				}
				sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
			} else {
				sb.append(" select projectno from BD_ZQB_GPFXXMB"
						+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
						+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
				String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < CSFZR.length; i++) {
					if(i==(CSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(CSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");	
				String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
				for (int i = 0; i < FSFZR.length; i++) {
					if(i==(FSFZR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(FSFZR[i].replaceAll("'", ""));
				}
				sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
				String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
				for (int i = 0; i < ZZSPR.length; i++) {
					if(i==(ZZSPR.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(ZZSPR[i].replaceAll("'", ""));
				}
				sb.append(" ))");
			}
			sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		if(czbm!=null&&!czbm.equals("")){
			sb.append(" AND CZBM like ? ");
			params.add("%"+czbm+"%");
		}
		sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");
		
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
			+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
			+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
			+") WHERE RN > ?  AND RN <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO "
			+ " LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_GPFXXMPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID "
			+ " INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GPFXXMPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR= ?  ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid"
			+ " order by J.ID DESC,J.groupid");
		params.add(gpfxFormId);
		params.add(startRow1);
		params.add(pageSize1);
		params.add(userid);
		final List param =params;
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				//股票发行评价表单
				Long gpfxXMFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
				Long gpfxXMDemId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "ID");
				Long gpfxPjFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行评价表单'", "FORMID");
				Long gpfxPjDemId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行评价表单'", "ID");
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
					String rwjd = (String) object[6];
					String spzt = (String) object[7];
					String manager1 = (String) object[8];
					Integer pjinsid = 0;
					if (object[13] != null) {
						pjinsid = Integer.valueOf(object[14].toString());
					}
					Integer instanceid = Integer.valueOf(object[10].toString());
					String xmjd = object[4] == null ? "" : object[4]
							.toString();
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					String groupid = object[5] == null ? "" : object[5]
							.toString();
					
					String pj = object[10] == null ? "" : object[11].toString();
					String zbspzt = object[12] == null ? "" : object[12].toString();
					Integer num = Integer.valueOf(object[15].toString());
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("PJ", pj);				
					map.put("ZBSPZT", zbspzt);				
					map.put("CUSTOMERNAME", customername);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("GROUPID", groupid);
					map.put("XMFORMID", gpfxXMFormId);
					map.put("XMDEMID", gpfxXMDemId);
					map.put("PJFORMID", gpfxPjFormId);
					map.put("PJDEMID", gpfxPjDemId);
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
		// StringBuffer sb = new StringBuffer(
		// "SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.MANAGER,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101");
		// if (!"".equals(projectName) && projectName != null) {
		// sb.append(" AND PROJECTNAME like '%" + projectName + "%'");
		// }
		// if ("".equals(xmjd) || xmjd == null) {
		// sb.append(" AND XMJD='持续督导'");
		// } else {
		// sb.append(" AND XMJD like '%" + xmjd + "%'");
		// }
		// if (!"".equals(startDate) && startDate != null) {
		// sb.append(" AND STARTDATE = to_date('" + startDate
		// + "','yyyy-mm-dd')");
		// }
		// if (!"".equals(customername) && customername != null) {
		// sb.append(" AND CUSTOMERNAME like '%" + customername + "%' ");
		// }
		// sb.append(" ORDER BY botable.ID desc");
		StringBuffer sb = new StringBuffer(
				"SELECT H.*, I.NUM FROM (SELECT J.* from (SELECT A.CUSTOMERNAME,A.OWNER,NVL(A.HTJE, 0),(NVL(A.HTJE, 0) - NVL(D.SSJE, 0)) WSJE,E.SSJE,F.jdmc TASK_NAME,E.SPZT,E.MANAGER,E.PJINSID,E.XGWTINSID,BINDTABLE.INSTANCEID,"
						+ "ROWNUM RN,A.XMJD,A.PROJECTNO,E.GROUPID, nvl(g.ldpj,'评价') ldpj FROM BD_ZQB_PJ_BASE A,    SYS_ENGINE_FORM_BIND BINDTABLE,(SELECT B.PROJECTNO, SUM(NVL(B.SSJE, 0)) SSJE FROM BD_PM_TASK B, BD_ZQB_PJ_BASE C"
						+ " WHERE B.PROJECTNO(+) = C.PROJECTNO GROUP BY B.PROJECTNO) D,BD_PM_TASK E,BD_ZQB_KM_INFO  F,( select b.projectno,b.groupid,a.ldpj from BD_ZQB_XMRWPJB  a, BD_PM_TASK b where a.projectno(+)=b.projectno and a.groupid(+)=b.groupid) g"
						+ " WHERE A.PROJECTNO = D.PROJECTNO(+) AND E.GROUPID=F.ID(+) AND g.PROJECTNO(+) = A.PROJECTNO and( g.groupid = e.groupid or g.groupid is null)   AND (F.jdmc like '%持续督导");
		// if (!"".equals(xmjd) && xmjd != null) {
		// sb.append(xmjd);
		// // sb.append(" AND XMJD= '持续督导'");
		// }
		// else {
		// sb.append(" AND XMJD like '%" + xmjd + "%'");
		// }
		sb.append("%' or F.jdmc is null) AND E.PROJECTNO(+) = A.PROJECTNO AND A.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91 and BINDTABLE.metadataid = 101");

		sb.append(" AND A.STATUS = '执行中'");
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND A.PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		// if ("".equals(xmjd) || xmjd == null) {
		// sb.append(" AND XMJD <> '持续督导'");
		// } else {
		// sb.append(" AND XMJD like '%" + xmjd + "%'");
		// }
		if (!"".equals(startDate) && startDate != null) {
			sb.append(" AND STARTDATE = to_date(?,'yyyy-mm-dd')");
			params.add(startDate);
		}
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND CUSTOMERNAME like ? ");
			params.add("%" + customername + "%");
		}
		sb.append(" ORDER BY A.customername DESC) J"
				+ "  WHERE RN > ? AND RN <= ?) H,(SELECT G.CUSTOMERNAME, COUNT(*) NUM"
				+ " FROM (SELECT A.CUSTOMERNAME,A.OWNER,NVL(A.HTJE, 0),E.SSJE,E.TASK_NAME,E.SPZT,E.MANAGER,E.PJINSID,E.XGWTINSID,BINDTABLE.INSTANCEID,ROWNUM RN FROM BD_ZQB_PJ_BASE A,  SYS_ENGINE_FORM_BIND BINDTABLE,"
				+ " BD_PM_TASK E WHERE E.PROJECTNO(+) = A.PROJECTNO AND A.STATUS = '执行中' AND A.id = BINDTABLE.dataid and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid = 91"
				+ " and BINDTABLE.metadataid = 101");
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
					// String projectNo = (String) object[3];
					Integer instanceid = Integer.valueOf(object[10].toString());
					String xmjd = object[12] == null ? "" : object[12]
							.toString();
					String projectNo = object[13] == null ? "" : object[13]
							.toString();
					String groupid = object[14] == null ? "" : object[14]
							.toString();
					String pj = object[15] == null ? "" : object[15].toString();
					Integer num = Integer.valueOf(object[16].toString());
					// Integer groupid = Integer.valueOf(object[42].toString());

					// String xmjd = (String) object[14];
					// String projectName = (String) object[2];
					// String manager = (String) object[5];
					// String customerno = (String) object[10];
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("PJ", pj);
					// map.put("PROJECTNAME", projectName);
					// map.put("MANAGER", manager);
					// map.put("CUSTOMERNO", customerno);
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
			String customername,String dgzt,HashMap<String,List<String>> parameterMap, String czbm, String cyrName,Long ismanager) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser userModel = uc._userModel;
		if (uc != null) {
			Long orgRoleId = userModel.getOrgroleid();
			if (orgRoleId.equals(new Long(5)) || orgRoleId.equals(new Long(9))) {
				flag = true;
			}
		}
		List params = new ArrayList();
		String owner = userModel.getUserid();// + "[" + userModel.getUsername() + "]";
		final String userid=userModel.getUserid()	.toString().trim();
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		map.put("SUBSTR(owner,0, instr(owner,'[',1)-1)", "USERID");
		map.put("SUBSTR(manager,0, instr(manager,'[',1)-1)", "USERID");
		map.put("userid", "USERID");
		map.put("createuserid", "USERID");
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
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD, E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
		if(!flag){
			if(ismanager==1){
				sb.append("INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(")  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(") union all"
						//任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in (");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a" );
				params.add(gpfxFormId);
				sb.append( " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append( " ) union all");
				
				if(config.equals("2")){
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( "); 
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				}else{
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))");
					
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}else{
				sb.append("INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) = ?  or createuserid= ? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? "
						//分派项目审批人
						+ " union all");
				params.add(owner);
				params.add(userid);
				params.add(owner);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(userid);
				if(config.equals("2")){
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}else{
					sb.append(" select projectno from BD_ZQB_GPFXXMB WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? )");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}else{
			if(cyrName!=null&&!cyrName.equals("")){
				parameter = getCyrUserMap(map,cyrName);
				sb.append("INNER JOIN ( select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");	
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a" );	
				params.add(gpfxFormId);
				sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
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
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append("  )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				
				}else{
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in (  ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		if (czbm!=null&&!czbm.equals("")) {
			sb.append(" AND CZBM like ? ");
			params.add("%" + czbm + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
				+ " ) C) WHERE RN > ? AND RN <= ? ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
				+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj"
				+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
		params.add(gpfxFormId);
		params.add(startRow1);
		params.add(pageSize1);
		if(!flag){
			if(ismanager==1){
				sb.append("INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(" ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a");
				params.add(gpfxFormId);
				sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append(" )");
				
				sb.append(" union all");
				//if 按多项目组审批
				if(config.equals("2")){
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
							+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( "); 
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( "); 
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(") ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					
				}else{
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( "); 
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(")) ");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}else{
				sb.append("INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid= ? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? "
						+ " union all"
						//任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a"
						+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
						+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? ");
				params.add(owner);
				params.add(userid);
				params.add(owner);
				params.add(owner);
				params.add(gpfxFormId);
				params.add(userid);
				sb.append(" union all");
				//if 按多项目组审批
				if(config.equals("2")){
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
							+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? "
							+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}else{
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)"
							+ " ");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}else{
			if(cyrName!=null&&!cyrName.equals("")){
				sb.append("INNER JOIN ("
						//项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
				String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
				for (int i = 0; i < owners.length; i++) {
					if(i==(owners.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(owners[i].replaceAll("'", ""));
				}
				sb.append(" )  or createuserid in ( ");
				String[] createuserid =parameter.get("createuserid").split(",");
				for (int i = 0; i < createuserid.length; i++) {
					if(i==(createuserid.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(createuserid[i].replaceAll("'", ""));
				}
				sb.append(" ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < manager.length; i++) {
					if(i==(manager.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(manager[i].replaceAll("'", ""));
				}
				sb.append(") union all"
						//任务阶段负责人
						+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
				String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
				for (int i = 0; i < managers.length; i++) {
					if(i==(managers.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(managers[i].replaceAll("'", ""));
				}
				sb.append(" ) union all"
						//项目及成员信息
						+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a");
				params.add(gpfxFormId);
				sb.append( " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
						+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
						+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
						+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
				String[] userids =parameter.get("userid").split(",");
				for (int i = 0; i < userids.length; i++) {
					if(i==(userids.length-1)){
						sb.append("?");
					}else{
						sb.append("?,");
					}
					params.add(userids[i].replaceAll("'", ""));
				}
				sb.append( " ) union all");
				
				if(config.equals("2")){
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					sb.append(") OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				
				}else{
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
							+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
							+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
					String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < CSFZR.length; i++) {
						if(i==(CSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(CSFZR[i].replaceAll("'", ""));
					}
					
					sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
					String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
					for (int i = 0; i < FSFZR.length; i++) {
						if(i==(FSFZR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(FSFZR[i].replaceAll("'", ""));
					}
					
					sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
					
					String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
					for (int i = 0; i < ZZSPR.length; i++) {
						if(i==(ZZSPR.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(ZZSPR[i].replaceAll("'", ""));
					}
					sb.append(" ))");
					
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!"".equals(customername) && customername != null) {
			sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
			params.add("%" + customername + "%");
		}
		if (czbm!=null&&!czbm.equals("")) {
			sb.append(" AND CZBM like ? ");
			params.add("%" + czbm + "%");
		}
		if("1".equals(dgzt)){
			sb.append(" AND QRDG = '已完成' ");
		}else if("2".equals(dgzt)){
			sb.append(" AND QRDG is null ");
		}
		
		sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
		} else {
			sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
			params.add("%" + xmjd + "%");
		}
		sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
				+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
				+ " WHERE A.STATUS ='已完成' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
				+ " ) WHERE RN > ? AND RN <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO "
				+ " LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_GPFXXMPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID "
				+ " INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GPFXXMPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR= ? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid"
				+ " order by J.ID DESC,J.groupid");
		params.add(gpfxFormId);
		params.add(startRow1);
		params.add(pageSize1);
		params.add(userid);
		final List param=params;
		final String sql1 = sb.toString();
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Long gpfxXMFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
				Long gpfxXMDemId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "ID");
				Long gpfxPjFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行评价表单'", "FORMID");
				Long gpfxPjDemId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行评价表单'", "ID");
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
					String rwjd = (String) object[6];
					String spzt = (String) object[7];
					String manager1 = (String) object[8];
					Integer pjinsid = 0;
					if (object[9] != null) {
						pjinsid = Integer.valueOf(object[9].toString());
					}
					Integer instanceid = Integer.valueOf(object[10].toString());
					String xmjd = object[4] == null ? "" : object[4]
							.toString();
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					String groupid = object[5] == null ? "" : object[5]
							.toString();
					String pj = object[10] == null ? "" : object[11].toString();
					Integer num = Integer.valueOf(object[14].toString());
					String pjr = object[12]==null?"":object[12].toString();
					if(!pjr.equals(userid)&&groupid!=null&&!groupid.equals("")){
						Long zPjid = DBUtil.getLong("SELECT INSTANCEID FROM BD_ZQB_GPFXXMPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GPFXXMPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PROJECTNO='"+projectNo+"' AND PJR='"+userid+"' AND GROUPID="+groupid+" ORDER BY ID", "INSTANCEID");
						if(zPjid!=null){
							pjinsid=Integer.parseInt(zPjid.toString());
						}else{
							pjinsid=0;
						}
					}
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("PJ", pj);				
					map.put("CUSTOMERNAME", customername);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("GROUPID", groupid);
					map.put("XMFORMID", gpfxXMFormId);
					map.put("XMDEMID", gpfxXMDemId);
					map.put("PJFORMID", gpfxPjFormId);
					map.put("PJDEMID", gpfxPjDemId);
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
		StringBuffer sb = new StringBuffer(
				"SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.MANAGER,BOTABLE.STARTDATE,BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BOTABLE.ID,BINDTABLE.INSTANCEID as BIND_INSTANCEID  FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND  BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101  AND STATUS = '已完成'");
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
		StringBuffer sb = new StringBuffer(
				"SELECT A.*,B.TASK_NAME,B.MANAGER FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,"
						+ "BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,"
						+ "BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,"
						+ "BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,nvl(BOTABLE.htje,0) htje,    "
						+ "sum(nvl(ssje,0)) ssje, nvl(BOTABLE.htje,0)- sum(nvl(ssje,0)) wsje,count(*) num FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND "
						+ " BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101  AND STATUS = '执行中'");
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" AND XMJD <> '持续督导'");
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
		sb.append("  inner join BD_PM_TASK c   on (c.projectno=BOTABLE.projectno or c.projectno is null) group by BOTABLE.CREATEUSER, BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,"
				+ "BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,"
				+ "BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,"
				+ "BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID, nvl(BOTABLE.htje,0),nvl(BOTABLE.htje,0)) A,BD_PM_TASK B    WHERE A.PROJECTNO=B.PROJECTNO OR  B.PROJECTNO IS NULL ");
		sb.append(" ORDER BY A.ID desc");
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
		StringBuffer sb = new StringBuffer(
				"SELECT A.*,B.TASK_NAME,B.MANAGER FROM (SELECT BOTABLE.CREATEUSER,BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,"
						+ "BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,"
						+ "BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,"
						+ "BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,nvl(BOTABLE.htje,0) htje,    "
						+ "sum(nvl(ssje,0)) ssje,nvl(BOTABLE.htje,0)-sum(nvl(ssje,0)) wsje,count(*) num,BOTABLE.manager cwjl FROM BD_ZQB_PJ_BASE  BOTABLE inner join SYS_ENGINE_FORM_BIND "
						+ " BINDTABLE on BOTABLE.id = BINDTABLE.dataid  and BINDTABLE.INSTANCEID is not null and BINDTABLE.formid=91 and BINDTABLE.metadataid=101  AND STATUS = '执行中'");
		sb.append(" AND (OWNER=?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1)=?)");
		params.add(owner);
		params.add(UserContextUtil.getInstance().getCurrentUserId());
		params.add(owner);
		if (!"".equals(projectName) && projectName != null) {
			sb.append(" AND PROJECTNAME like ?");
			params.add("%" + projectName + "%");
		}
		if ("".equals(xmjd) || xmjd == null) {
			sb.append(" AND XMJD <> '持续督导'");
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
		sb.append("  inner join BD_PM_TASK c   on (c.projectno=BOTABLE.projectno or c.projectno is null) group by BOTABLE.CREATEUSER, BOTABLE.CREATEDATE,BOTABLE.PROJECTNAME,BOTABLE.PROJECTNO,BOTABLE.OWNER,BOTABLE.ID,BOTABLE.STARTDATE,"
				+ "BOTABLE.ENDDATE,BOTABLE.ATTACH,BOTABLE.CUSTOMERNAME,BOTABLE.CUSTOMERNO,BOTABLE.TYPENO,BOTABLE.MEMO,BOTABLE.CUSTOMERINFO,BOTABLE.XMJD,BOTABLE.STATUS,BOTABLE.KHLXR,"
				+ "BOTABLE.KHLXDH,BOTABLE.GSGK,BOTABLE.CZWT,BOTABLE.LSYG,BOTABLE.CZHGQ,BOTABLE.GSZYYW,BOTABLE.GSZYCP,BOTABLE.GSJZYS,BOTABLE.FXPGFS,BOTABLE.A01,BOTABLE.A02,BOTABLE.A03,"
				+ "BOTABLE.A04,BOTABLE.A05,BOTABLE.A06,BOTABLE.A07,BOTABLE.A08,BOTABLE.YJZXYNJLR,BINDTABLE.FORMID,BINDTABLE.INSTANCEID,BOTABLE.manager,nvl(BOTABLE.htje,0)) A,BD_PM_TASK B    WHERE A.PROJECTNO=B.PROJECTNO OR  B.PROJECTNO IS NULL ");
		sb.append(" ORDER BY A.ID desc");
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
			String customername,String dgzt,HashMap<String,List<String>> parameterMap, String czbm, String cyrName,Long ismanager) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		final String userid=userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		String config = SystemConfig._xmlcConf.getConfig();
		Long orgRoleId = userModel.getOrgroleid();
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> parameter = new HashMap<String,String>();
		List params = new ArrayList();
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
		StringBuffer sb = null;
		if(ismanager==1){
			sb = new StringBuffer("SELECT DISTINCT J.*,nvl(J.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD, E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.SPZT ZBSPZT FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN (");
		}else{
			sb = new StringBuffer("SELECT DISTINCT J.*,nvl(PJDATA.instanceid,'0') PJINSTANCEID,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD, E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.SPZT ZBSPZT FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN (");
		}
		if(ismanager==1){
					//项目的负责人、现场负责人
					sb.append( " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
					String[] owners =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners.length; i++) {
						if(i==(owners.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners[i].replaceAll("'", ""));
					}
					
					sb.append( " )  or createuserid in ( ");
					String[] createuserid =parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserid.length; i++) {
						if(i==(createuserid.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserid[i].replaceAll("'", ""));
					}
					
					sb.append( " ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
					
					String[] manager =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
									for (int i = 0; i < manager.length; i++) {
										if(i==(manager.length-1)){
											sb.append("?");
										}else{
											sb.append("?,");
										}
										params.add(manager[i].replaceAll("'", ""));
									}
					sb.append( " ) union all"
							//任务阶段负责人
							+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( "); 
					String[] managers =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers.length; i++) {
						if(i==(managers.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers[i].replaceAll("'", ""));
					}
					sb.append( ") union all"
							//项目及成员信息
							+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
							+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a");
					params.add(gpfxFormId);
					sb.append( " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
							+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
							+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
							+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
					String[] userids =parameter.get("userid").split(",");
									for (int i = 0; i < userids.length; i++) {
										if(i==(userids.length-1)){
											sb.append("?");
										}else{
											sb.append("?,");
										}
										params.add(userids[i].replaceAll("'", ""));
									}
									
					sb.append( " ) union all");
				
				
					params.add(parameter.get("userid"));
					if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
						String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < CSFZR.length; i++) {
							if(i==(CSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(CSFZR[i].replaceAll("'", ""));
						}
						
						sb.append(") OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
						String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < FSFZR.length; i++) {
							if(i==(FSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(FSFZR[i].replaceAll("'", ""));
						}
						sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
						
						String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
											for (int i = 0; i < ZZSPR.length; i++) {
												if(i==(ZZSPR.length-1)){
													sb.append("?");
												}else{
													sb.append("?,");
												}
												params.add(ZZSPR[i].replaceAll("'", ""));
											}
						sb.append(" )) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						
					}else{
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
								+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
								+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
						String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < CSFZR.length; i++) {
							if(i==(CSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(CSFZR[i].replaceAll("'", ""));
						}
						
						sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
						String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < FSFZR.length; i++) {
							if(i==(FSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(FSFZR[i].replaceAll("'", ""));
						}
						sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
						String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
						for (int i = 0; i < ZZSPR.length; i++) {
							if(i==(ZZSPR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(ZZSPR[i].replaceAll("'", ""));
						}
						sb.append(" ))");
					
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
					sb.append(" WHERE 1=1 ");
					if (!"".equals(customername) && customername != null) {
						sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
						params.add("%" + customername + "%");
					}
					if(czbm!=null&&!czbm.equals("")){
						sb.append(" AND CZBM like ? ");
						params.add("%" + czbm + "%");
					}
					sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO");
					if ("".equals(xmjd) || xmjd == null) {
						sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
					} else {
						sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
						params.add("%" + xmjd + "%");
					}
					sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE ");
					params.add(gpfxFormId);
					sb.append(" ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
							+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
							+ " ) C) WHERE RN > ? AND RN <= ? ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
							+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.SPZT ZBSPZT"
							+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ( ");
					params.add(startRow1);
					params.add(pageSize1);
							//项目的负责人、现场负责人
					sb.append( " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) in ( ");
					String[] owners1 =parameter.get("SUBSTR(owner,0, instr(owner,'[',1)-1)").split(",");
					for (int i = 0; i < owners1.length; i++) {
						if(i==(owners1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(owners1[i].replaceAll("'", ""));
					}
					sb.append( " )  or createuserid in ( ");
					String[] createuserids =parameter.get("createuserid").split(",");
					for (int i = 0; i < createuserids.length; i++) {
						if(i==(createuserids.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(createuserids[i].replaceAll("'", ""));
					}
					
					sb.append( " ) or SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
					String[] manager1 =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < manager1.length; i++) {
						if(i==(manager1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(manager1[i].replaceAll("'", ""));
					}
					sb.append( " ) union all select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) in ( ");
					String[] managers1 =parameter.get("SUBSTR(manager,0, instr(manager,'[',1)-1)").split(",");
					for (int i = 0; i < managers1.length; i++) {
						if(i==(managers1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(managers1[i].replaceAll("'", ""));
					}
					sb.append( " ) union all select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a ");
					params.add(gpfxFormId);
					sb.append(" left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
							+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
							+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid in ( ");
					String[] userids1 =parameter.get("userid").split(",");
					for (int i = 0; i < userids1.length; i++) {
						if(i==(userids1.length-1)){
							sb.append("?");
						}else{
							sb.append("?,");
						}
						params.add(userids1[i].replaceAll("'", ""));
					}
					sb.append(" )");
					
				
					//分派项目审批人
					sb.append(" union all");
					//if 按多项目组审批
					if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
								+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
						String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < CSFZR.length; i++) {
							if(i==(CSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(CSFZR[i].replaceAll("'", ""));
						}
						sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( ");
						String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < FSFZR.length; i++) {
							if(i==(FSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(FSFZR[i].replaceAll("'", ""));
						}
						sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( ");
						
						String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
											for (int i = 0; i < ZZSPR.length; i++) {
												if(i==(ZZSPR.length-1)){
													sb.append("?");
												}else{
													sb.append("?,");
												}
												params.add(ZZSPR[i].replaceAll("'", ""));
											}
						sb.append(" ) ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					
					}else{
						//else
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
								+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) in ( ");
						String[] CSFZR =parameter.get("SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < CSFZR.length; i++) {
							if(i==(CSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(CSFZR[i].replaceAll("'", ""));
						}
						
						sb.append(" ) OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) in ( "); 
						String[] FSFZR =parameter.get("SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1)").split(",");
						for (int i = 0; i < FSFZR.length; i++) {
							if(i==(FSFZR.length-1)){
								sb.append("?");
							}else{
								sb.append("?,");
							}
							params.add(FSFZR[i].replaceAll("'", ""));
						}
						sb.append(" ) OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) in ( "); 
						
						String[] ZZSPR =parameter.get("SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1)").split(",");
											for (int i = 0; i < ZZSPR.length; i++) {
												if(i==(ZZSPR.length-1)){
													sb.append("?");
												}else{
													sb.append("?,");
												}
												params.add(ZZSPR[i].replaceAll("'", ""));
											}
						sb.append(" ))");
						
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
					sb.append(" WHERE 1=1 ");
					if (!"".equals(customername) && customername != null) {
						sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
						params.add("%" + customername + "%");
					}
					if(czbm!=null&&!czbm.equals("")){
						sb.append(" AND CZBM like ? ");
						params.add("%" + czbm + "%");
					}
					if("1".equals(dgzt)){
						sb.append(" AND QRDG = '已完成' ");
					}else if("2".equals(dgzt)){
						sb.append(" AND QRDG is null ");
					}
					
					sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");
					
					// WHERE JDMC LIKE '%股改%'
					if ("".equals(xmjd) || xmjd == null) {
						sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
					} else {
						sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
						params.add("%" + xmjd + "%");
					}
					sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
							+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
							+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
							+ " ) WHERE RN > ? AND RN <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
					params.add(gpfxFormId);
					params.add(startRow1);
					params.add(pageSize1);
				}else{
					//项目的负责人、现场负责人
					sb.append( " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
							//任务阶段负责人
							+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
							//项目及成员信息
							+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
							+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a"
							+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
							+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
							+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
							+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? "
							//分派项目审批人
							+ " union all");
					params.add(owner);
					params.add(userid);
					params.add(owner);
					params.add(owner);
					params.add(gpfxFormId);
					params.add(userid);
					//if 按多项目组审批
					if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}else{
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
								+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
								+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? )");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
					sb.append(" WHERE 1=1 ");
					if (!"".equals(customername) && customername != null) {
						sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
						params.add("%" + customername + "%");
					}
					
					sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO");
					if ("".equals(xmjd) || xmjd == null) {
						sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
					} else {
						sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
						params.add("%" + xmjd + "%");
					}
					sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
							+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
							+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
							+ " ) C) WHERE RN > ? AND RN <= ? ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
							+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,A.SPZT ZBSPZT"
							+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ("
							//项目的负责人、现场负责人
							+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? "
							+ " union all"
							//任务阶段负责人
							+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
							//项目及成员信息
							+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid= ? ) a"
							+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
							+ " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
							+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ? ");
					params.add(gpfxFormId);
					params.add(startRow1);
					params.add(pageSize1);
					params.add(owner);
					params.add(userid);
					params.add(owner);
					params.add(owner);
					params.add(gpfxFormId);
					params.add(userid);
					//分派项目审批人
					sb.append(" union all");
					//if 按多项目组审批
					if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
								+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? "
								+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}else{
						//else
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
								+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ?  OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ? )");
						params.add(owner);
						params.add(owner);
						params.add(owner);
					}
					sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
					sb.append(" WHERE 1=1 ");
					if (!"".equals(customername) && customername != null) {
						sb.append(" AND upper(CUSTOMERNAME) like upper( ? ) ");
						params.add("%" + customername + "%");
					}
					
					if("1".equals(dgzt)){
						sb.append(" AND QRDG = '已完成' ");
					}else if("2".equals(dgzt)){
						sb.append(" AND QRDG is null ");
					}
					
					sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");
					
					// WHERE JDMC LIKE '%股改%'
					if ("".equals(xmjd) || xmjd == null) {
						sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
					} else {
						sb.append(" INNER JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC like ? ");
						params.add("%" + xmjd + "%");
					}
					sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
							+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
							+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
							+ " ) WHERE RN > ? AND RN <= ? ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO "
							+ " LEFT JOIN (SELECT INSTANCEID,PJ.ID,PJ.Groupid,PJ.Projectno,PJ.Pjr FROM BD_ZQB_GPFXXMPJB PJ INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_FORM_BIND BIND INNER JOIN SYS_ENGINE_IFORM IFORM ON BIND.FORMID=IFORM.ID "
							+ " INNER JOIN (SELECT ID FROM SYS_ENGINE_METADATA WHERE ENTITYNAME='BD_ZQB_GPFXXMPJB') META ON IFORM.METADATAID=META.ID) BIND ON PJ.ID=BIND.DATAID WHERE PJR= ? ORDER BY ID) PJDATA ON J.Projectno=PJDATA.Projectno and J.Groupid=PJDATA.Groupid"
							+ " order by J.ID DESC,J.groupid");
					params.add(gpfxFormId);
					params.add(startRow1);
					params.add(pageSize1);
					params.add(userid);
				}
		final List param=params;
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
				Long gpfxXMFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
				Long gpfxXMDemId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "ID");
				Long gpfxPjFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行评价表单'", "FORMID");
				Long gpfxPjDemId = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行评价表单'", "ID");
				List<Object[]> list = query.list();
				HashMap map;
				HashMap m = new HashMap();
				BigDecimal b;
				int n = 0;
				for (Object[] object : list) {
					map = new HashMap();
					String customername = (String) object[2];
					String owner = (String) object[3];
					String rwjd = (String) object[6];
					String spzt = (String) object[7];
					String manager1 = (String) object[8];
					Integer pjinsid = 0;
					if (object[13] != null) {
						pjinsid = Integer.valueOf(object[14].toString());
					}
					Integer instanceid = Integer.valueOf(object[10].toString());
					String xmjd = object[4] == null ? "" : object[4]
							.toString();
					String projectNo = object[1] == null ? "" : object[1]
							.toString();
					String groupid = object[5] == null ? "" : object[5]
							.toString();
					String pj = object[10] == null ? "" : object[11].toString();
					Integer num = Integer.valueOf(object[15].toString());
					String zbspzt = object[12] == null ? "" : object[12].toString();
					map.put("OWNER", owner);
					map.put("PROJECTNO", projectNo);
					map.put("INSTANCEID", instanceid);
					map.put("XMJD", xmjd);
					map.put("RWJD", rwjd);
					map.put("SPZT", spzt);
					map.put("CUSTOMERNAME", customername);
					map.put("JDFZR", manager1);
					map.put("NUM", num);
					map.put("ZBSPZT", zbspzt);
					map.put("PJ", pj);
					map.put("GROUPID", groupid);
					map.put("PJINSID", pjinsid);
					map.put("XMFORMID", gpfxXMFormId);
					map.put("XMDEMID", gpfxXMDemId);
					map.put("PJFORMID", gpfxPjFormId);
					map.put("PJDEMID", gpfxPjDemId);
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
			sb.append(" AND XMJD='持续督导'");
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
	 * @param startRow
	 * @param pageSize
	 * @param sql
	 * @return
	 */
	public List<HashMap<String, Object>> getListSize(final String sql,final List param) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
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
			final int pageSize, final String sql,final List param) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				DBUtilInjection d=new DBUtilInjection();
				List<HashMap<String, Object>> l = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < param.size(); i++) {
					if(param.get(i)!=null && !"".equals(param.get(i).toString())){
						String params=param.get(i).toString().replace("%", "").trim();
						if(d.HasInjectionData(params)){
							return l;
						}
						}
					query.setParameter(i, param.get(i));
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
		//select * from (select z.*,rownum as rm from (
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,"
				+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
				sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
						+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno ) E ON A.PROJECTNO = E.PROJECTNO"
						+ " LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
					sb.append(" WHERE JDMC <> '持续督导'");
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
						+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
						+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
						+ " ) C"
						+")"
						+") J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
						+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj"
						+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J ");
                       
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
								+ " LEFT JOIN BD_PM_TASK E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
							
						// WHERE JDMC LIKE '%股改%'
							sb.append(" WHERE JDMC <> '持续督导'");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
								+")"
								+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
//								+ ") z) y WHERE rm > "+startRow1+" AND rm <= "+pageSize1
		
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
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,"
				+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma FROM (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN ("
				//项目的负责人、现场负责人
				+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
				//任务阶段负责人
				+ " select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
				//项目及成员信息
				+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
				+ " left join BD_ZQB_PJ_BASE b on a.dataid = b.id "
				+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ?"
              //分派项目审批人
              	+ " union all");
				params.add(owner);
				params.add(userid);
				params.add(owner);
				params.add(owner);
				params.add(userid);
              //if 按多项目组审批
				if(config.equals("2")){
					sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
					params.add(owner);
					params.add(owner);
					params.add(owner);
              //else
				}else{
					sb.append(" select projectno from BD_ZQB_GPFXXMB"
	              	+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
	              	+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) =  OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) =  OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = )");
					params.add(owner);
					params.add(owner);
					params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B"
						+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno) E ON A.PROJECTNO = E.PROJECTNO"
						+ " LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
					sb.append(" WHERE JDMC <> '持续督导'");
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
						+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
						+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
						+ " ) C) ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
						+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME"
						+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_PJ_BASE J INNER JOIN ("
                       //项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ?"
						+ " union all"
                        //任务阶段负责人
                        + " select projectno from BD_PM_TASK where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
                        //项目及成员信息
                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno,b.lcbs from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=101) a"
                        + " left join BD_ZQB_PJ_BASE b on a.dataid = b.id) c inner join ("
                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.lcbs = d.instanceid where userid = ?");
                       
                        //分派项目审批人
						sb.append(" union all");
						params.add(owner);
						params.add(userid);
						params.add(owner);
						params.add(owner);
						params.add(userid);
                        //if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
								+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?"
								+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}else{
                        //else
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
								+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_PM_TASK GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
								+ " LEFT JOIN (select * from BD_PM_TASK task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_XMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno left join (select wtfk.taskno taskno,wtfk.xmbh xmbh,wtfk.username twname,wtfk.question question,wtfk.createdate wcreatedate,retalk.username username,retalk.content content,retalk.createdate rcreatedate from BD_ZQB_XMWTFK wtfk left join BD_ZQB_RETALK retalk on wtfk.id=retalk.questionno) xgwt on task.projectno=xgwt.xmbh and task.groupid=xgwt.taskno) E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_KM_INFO");
							
						// WHERE JDMC LIKE '%股改%'
							sb.append(" WHERE JDMC <> '持续督导'");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = 91 and metadataid = 101 and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
								+ " ) ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
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
		List params = new ArrayList();
		// 因为有分页，所以查询必须写到一块儿
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
		List params = new ArrayList();
		// 因为有分页，所以查询必须写到一块儿
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

	public List<HashMap> getShowDailyList(String projectNo, int pageNow, int pageSize,String startdate,String enddate, Long formid) {
		final int pageSize1 = pageNow*pageSize;
		final int startRow1 = (pageNow - 1) * pageSize;
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("select to_char(rb.projectdate,'yyyy-MM-dd') projectdate,rb.progress,rb.username,rb.tel,rb.tracking,rb.projectno,bind.instanceid from bd_zqb_xmrbb rb inner join (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE FORMID=?) bind on rb.id=bind.dataid where projectno=?");
		params.add(formid);
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
					BigDecimal instanceid = (BigDecimal) object[6];
					map.put("PROJECTDATE", projectdate);
					map.put("PROGRESS", progress);
					map.put("USERNAME", username);
					map.put("TEL", tel);
					map.put("TRACKING", tracking);
					map.put("PROJECTNO", projectno);
					map.put("INSTANCEID", instanceid);
					l.add(map);
				}
				return l;
			}
		});
	}

	public List<String> getDailyRunProjectList() {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		//select * from (select z.*,rownum as rm from (
		/*StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,"
				+ " E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj,to_char(A.STARTDATE,'yyyy-MM-dd') STARTDATE,to_char(A.ENDDATE,'yyyy-MM-dd') ENDDATE,A.Khlxr,A.Khlxdh,to_char(A.Ggjzr,'yyyy-MM-dd ') Ggjzr,to_char(A.Sbjzr,'yyyy-MM-dd ') Sbjzr,A.Memo,A.xmbz,A.Zclr,A.Zclrdh,A.Fzjgmc,A.Fzjglxr,A.Wbclrjg,E.filename,A.PROJECTNAME,A.MANAGER ma FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J ");
				sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_ZQB_GPFXXMRWB GROUP BY PROJECTNO) B"
						+ " ON A.PROJECTNO = B.PROJECTNO LEFT JOIN (select * from BD_ZQB_GPFXXMRWB task left join (select zl.projectno bh,zl.jdbh jdbh,ff.file_src_name filename from BD_ZQB_TYXMRWZLB zl LEFT JOIN sys_upload_file ff on zl.jdzl=ff.file_id) xmzl on xmzl.jdbh=task.groupid and xmzl.bh=task.projectno ) E ON A.PROJECTNO = E.PROJECTNO"
						+ " LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
					sb.append(" WHERE JDMC <> '持续督导'");
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = " + gpfxFormId + " and INSTANCEID is not null) BINDTABLE"
						+ " ON A.id = BINDTABLE.dataid LEFT JOIN BD_ZQB_GPFXXMPJB G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
						+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
						+ " ) C"
						+")"
						+") J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
						+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,NVL(A.HTJE, 0) HTJE,(NVL(A.HTJE, 0) - NVL(B.SSJE, 0)) WSJE,A.XMJD,E.GROUPID,E.SSJE,F.jdmc,TASK_NAME,E.SPZT, E.MANAGER, E.PJINSID, E.XGWTINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj"
						+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMRWB J ");
						sb.append(" ) A LEFT JOIN (SELECT PROJECTNO,SUM(SSJE) SSJE FROM BD_ZQB_GPFXXMRWB GROUP BY PROJECTNO) B ON A.PROJECTNO = B.PROJECTNO"
								+ " LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO");
						sb.append(" WHERE JDMC <> '持续督导'");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = " + gpfxFormId + " and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN BD_ZQB_GPFXXMPJB G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
								+")"
								+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");*/
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,"
				+ " E.GROUPID,F.jdmc,E.SPZT, E.MANAGER,E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J WHERE 1=1 ");
				
				sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO");
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
						+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
						+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
						+ " ) C"
						+") "
						+") J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
						+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER,E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj"
						+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J");
				params.add(gpfxFormId);
                       
						sb.append(" WHERE 1=1 ");
						sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");
							
						// WHERE JDMC LIKE '%股改%'
						sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
								+") "
								+ " ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
						params.add(gpfxFormId);
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
	
	public List<String> getDailyRunProjectList1(String owner) {
		String userid=UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid().toString().trim();
		// 因为有分页，所以查询必须写到一块儿
		String config = SystemConfig._xmlcConf.getConfig();
		List params = new ArrayList();
		Long gpfxFormId = DBUtil.getLong("SELECT FORMID FROM SYS_DEM_ENGINE WHERE TITLE='股票发行项目'", "FORMID");
		StringBuffer sb = new StringBuffer("SELECT DISTINCT J.*,D.NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from (SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,"
				+ " E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj FROM (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ("
				//项目的负责人、现场负责人
				+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
				//任务阶段负责人
				+ " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
				//项目及成员信息
				+ " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
				+ " left join BD_ZQB_GPFXXMB b on a.dataid = b.id "
				+ " ) c inner join (select a.instanceid,dataid,userid,b.name  from"
				+ " (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
				+ " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ?"
              //分派项目审批人
              	+ " union all");
		params.add(owner);
		params.add(userid);
		params.add(owner);
		params.add(owner);
		params.add(gpfxFormId);
		params.add(userid);
              //if 按多项目组审批
				if(config.equals("2")){
				sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN (select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
				params.add(owner);
				params.add(owner);
				params.add(owner);
              //else
				}else{
				sb.append(" select projectno from BD_ZQB_GPFXXMB"
              	+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a"
              	+ " where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
				params.add(owner);
				params.add(owner);
				params.add(owner);
				}
				sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
				sb.append(" WHERE 1=1 ");
				
				sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO");
				sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
				sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
						+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
						+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID"
						+ " ) C) ) J INNER JOIN (SELECT PROJECTNO,CUSTOMERNAME, COUNT(*) NUM FROM (SELECT * FROM (SELECT C.*,ROWNUM RN from ("
						+ " SELECT A.ID,A.PROJECTNO,CUSTOMERNAME,OWNER,A.XMJD,E.GROUPID,F.jdmc,E.SPZT, E.MANAGER, E.PJINSID,BINDTABLE.INSTANCEID,nvl(g.ldpj,'评价') ldpj"
						+ " FROM  (SELECT DISTINCT J.* FROM BD_ZQB_GPFXXMB J INNER JOIN ("
                       //项目的负责人、现场负责人
						+ " select projectno from BD_ZQB_GPFXXMB a where SUBSTR(owner,0, instr(owner,'[',1)-1) =?  or createuserid=? or SUBSTR(manager,0, instr(manager,'[',1)-1) = ?"
						+ " union all"
                        //任务阶段负责人
                        + " select projectno from BD_ZQB_GPFXXMRWB where SUBSTR(manager,0, instr(manager,'[',1)-1) = ? union all"
                        //项目及成员信息
                        + " select distinct projectno from (select instanceid,dataid projectid,b.projectname,b.projectno from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where formid=?) a"
                        + " left join BD_ZQB_GPFXXMB b on a.dataid = b.id) c inner join ("
                        + " select a.instanceid,dataid,userid,b.name  from (select instanceid,dataid from SYS_ENGINE_FORM_BIND where metadataid=105) a"
                        + " left join BD_ZQB_GROUP b on a.dataid = b.id) d on c.instanceid = d.instanceid where userid = ?");
						params.add(gpfxFormId);
						params.add(owner);
						params.add(userid);
						params.add(owner);
						params.add(owner);
						params.add(gpfxFormId);
						params.add(userid);
                        //分派项目审批人
						sb.append(" union all");
                        //if 按多项目组审批
						if(config.equals("2")){
						sb.append(" select projectno from BD_ZQB_GPFXXMB a INNER JOIN ("
								+ " select CSFZR from BD_ZQB_XMSPRWH where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?"
								+ " ) B ON SUBSTR(A.OWNER,0, instr(A.OWNER,'[',1)-1) = SUBSTR(B.CSFZR,0, instr(B.CSFZR,'[',1)-1)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}else{
                        //else
						sb.append(" select projectno from BD_ZQB_GPFXXMB"
								+ " WHERE EXISTS (SELECT ID FROM (select * from BD_ZQB_XMSPRWH where rownum = 1 order by ID) a where SUBSTR(CSFZR,0, instr(CSFZR,'[',1)-1) = ? OR SUBSTR(FSFZR,0, instr(FSFZR,'[',1)-1) = ? OR SUBSTR(ZZSPR,0, instr(ZZSPR,'[',1)-1) = ?)");
						params.add(owner);
						params.add(owner);
						params.add(owner);
						}
						sb.append(" ) H ON H.PROJECTNO = J.PROJECTNO");
						sb.append(" WHERE 1=1 ");
						sb.append(" ) A LEFT JOIN BD_ZQB_GPFXXMRWB E ON A.PROJECTNO = E.PROJECTNO ");
							
						// WHERE JDMC LIKE '%股改%'
						sb.append(" LEFT JOIN (SELECT * FROM BD_ZQB_TYXM_INFO WHERE JDMC <> '持续督导'");
						sb.append(" ) F ON E.GROUPID = F.ID LEFT JOIN (SELECT * FROM SYS_ENGINE_FORM_BIND WHERE formid = ? and INSTANCEID is not null) BINDTABLE"
								+ " ON A.id = BINDTABLE.dataid LEFT JOIN (select * from BD_ZQB_GPFXXMPJB a inner join (select max(id) id from BD_ZQB_GPFXXMPJB group by groupid,projectno) b on a.id=b.id) G ON A.PROJECTNO = G.PROJECTNO AND E.GROUPID = G.GROUPID"
								+ " WHERE A.STATUS ='执行中' AND A.XMJD <> '持续督导' ORDER BY A.PROJECTNO DESC,GROUPID) C"
								+ " ) ) L GROUP BY PROJECTNO,CUSTOMERNAME) D ON D.PROJECTNO = J.PROJECTNO order by J.ID DESC,J.groupid");
						params.add(gpfxFormId);
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql1);
				DBUtilInjection d=new DBUtilInjection();
				List<String> l = new ArrayList<String>();
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

	public List<HashMap> getDaily(List<String> tmplist) {
		StringBuffer sb = new StringBuffer("select to_char(rb.projectdate,'yyyy-MM-dd') projectdate,rb.progress,rb.username,rb.tel,rb.tracking,rb.projectno,pj.projectname from bd_zqb_xmrbb rb inner join BD_ZQB_GPFXXMB pj on rb.projectno=pj.projectno where rb.projectno in (");
		for (int i = 0; i < tmplist.size(); i++) {
			if(i==(tmplist.size()-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
		}
		sb.append(")");
		final String sql1 = sb.toString();
		final List<String> param = tmplist;
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
		StringBuffer sb = new StringBuffer("select count(rb.projectno) count from bd_zqb_xmrbb rb inner join BD_ZQB_GPFXXMB pj on rb.projectno=pj.projectno where rb.projectno in (");
		for (int i = 0; i < tmplist.size(); i++) {
			if(i==(tmplist.size()-1)){
				sb.append("?");
			}else{
				sb.append("?,");
			}
		}
		sb.append(") group by rb.projectno order by rb.projectno");
		final String sql1 = sb.toString();
		final List<String> param = tmplist;
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
	
	public List<HashMap> gpfxproAssociate(String customerno){
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ROWNUM RNUM,A.* FROM (");
		sb.append("    SELECT ");
		sb.append("    TO_CHAR(A.STARTDATE,'yyyy-MM-dd') STARTDATE,A.MANAGER,A.CZBM,");
		sb.append("    A.SJFXGPJG,B.RZJE,(CASE WHEN C.DZJE IS NULL THEN A.SJFXZE ELSE to_number(C.DZJE) END) DZJE,A.PROJECTNO");
		sb.append("    FROM BD_ZQB_GPFXXMB A");
		sb.append("    LEFT JOIN BD_ZQB_XMDZ C ON A.FPBM=C.ID"); 
		sb.append("    LEFT JOIN (");
		sb.append("        SELECT PROJECTNO,SUM(RZJE) AS RZJE FROM BD_ZQB_XMJSXXGLB WHERE PROJECTNO IS NOT NULL GROUP BY PROJECTNO");
		sb.append("    ) B ON A.PROJECTNO=B.PROJECTNO WHERE 1=1 AND (B.RZJE<A.SJFXGPJG OR B.RZJE IS NULL)");
		if(customerno!=null&&!customerno.equals("")){
			sb.append(" AND A.CUSTOMERNO=?");
			params.add(customerno);
		}
		sb.append("    ORDER BY A.CREATEDATE ASC,A.ID ASC");
		sb.append(") A");
		final String sql1 = sb.toString();
		final List param = params;
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
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
					BigDecimal rnum = (BigDecimal)object[0];
					String lxsj = (String)object[1];
					String xmfzr = (String)object[2];
					String czbm = (String)object[3];
					BigDecimal htje = (BigDecimal)object[4];
					BigDecimal yrz = (BigDecimal)object[5];
					BigDecimal qyrzbys = (BigDecimal)object[6];
					String projectno = (String)object[7];
					map.put("DZSJ", rnum);
					map.put("LXSJ", lxsj==null?"":lxsj);
					map.put("XMFZR", xmfzr==null?"":xmfzr);
					map.put("CZBM", czbm==null?"":czbm);
					map.put("HTJE", htje==null?"":htje);
					map.put("YRZ", yrz==null?"":yrz);
					map.put("QYRZBYS", qyrzbys==null?"":qyrzbys);
					map.put("PROJECTNO", projectno);
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

}
