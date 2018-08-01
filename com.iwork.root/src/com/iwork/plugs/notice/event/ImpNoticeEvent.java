package com.iwork.plugs.notice.event;

import java.util.Date;
import com.ibm.icu.text.SimpleDateFormat;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.notice.util.NoticeUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.OrganizationAPI;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ImpNoticeEvent implements IWorkScheduleInterface {

	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-公告导入结束......");
		return true;
	}

	public boolean executeBefore() throws ScheduleException {	
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-公告导入前...... ");
		AddGGToDB();
		AddKHToDB();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-公告导入中...... ");
		return true;
	}

	// 将公告数据添加至数据库
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void AddGGToDB() {
		try {
			List<HashMap> list = NoticeUtil.getInstance().GetNoticeInfo();
			String GG_UUID = NoticeUtil.getInstance().GetUUID("公告呈报管理");
			//导入和未导入的记录数，用于输出
			int Daoru = 0;
			int WDaoru = 0;
			for (HashMap ht : list) {
				//如果数据库中该公告，跳过
				String KHMC = ht.get("KHMC").toString();
				String NoticeName = ht.get("NOTICENAME").toString();			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date parse = sdf.parse(ht.get("NOTICEDATE").toString());
				String format = sdf.format(parse);
				ht.put("NOTICEDATE", format);
				parse = sdf.parse(ht.get("CREATEDATE").toString());
				ht.put("CREATEDATE", format);
				//新增字段-公告序列（32位字符）
				String NoticeSQ =  UUID.randomUUID().toString().replaceAll("-", "");
				ht.put("NOTICESQ", NoticeSQ);
				Long newInstanceId = DemAPI.getInstance().newInstance(GG_UUID,SecurityUtil.supermanager);
				DemAPI.getInstance().saveFormData(GG_UUID, newInstanceId, ht,
						false);
				Daoru++;
		}

			System.out.println("导入完毕 ,时间："+ UtilDate.dateFormat(new Date()));
		} catch (Exception e) {
		}
	}

	// 添加客户信息
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void AddKHToDB()
	{
		try
		{
			//从临时客户表中读取出客户信息
			List<HashMap> list = NoticeUtil.getInstance().GetKHInfo();		
			for (HashMap ht : list) 
			{
				String CUSTOMERNAME = ht.get("CUSTOMERNAME").toString();				
				String KHBH = ht.get("CUSTOMERNO").toString();					
				List<HashMap> listHK = NoticeUtil.getInstance().IsExistsKH(CUSTOMERNAME);
				//若存在，则需要更新已插入的公告数据中的客户编号
				if (listHK.size() > 0) {
					//这是已存在的客户编号
					String CUSTOMERNO = listHK.get(0).get("CUSTOMERNO").toString();
					NoticeUtil.getInstance().UpdateNotice(KHBH,CUSTOMERNO,CUSTOMERNAME);
					continue;
				}
				//若不存在，那么添加该客户以及它所涉及到的部门，用户， 持续督导分派信息等等
				else
				{   
					List<HashMap> listKHBH= NoticeUtil.getInstance().IsExistsKHByNO(KHBH);
					boolean bl=false;
					int Seq=0;
					String OldKHBH="";
					//说明该客户编号在数据库中已经被使用，需要重新生成
					if(listKHBH.size()>0)
					{
						bl=true;
						OldKHBH=KHBH;
						String str= NoticeUtil.getInstance().GetCustomerNo();
						String[] strArr=str.split(",");
						KHBH=strArr[0];
						Seq=Integer.parseInt(strArr[1]);
					}
					//取值
					String USERID = ht.get("ZQDM").toString();
					String USERNAME = ht.get("USERNAME")==null?"":ht.get("USERNAME").toString();
					String PASSWORD = ht.get("PASSWORD").toString();
					Long ORGROLEID=Long.parseLong(ht.get("ORGROLEID").toString());
					Long ISMANAGER=Long.parseLong(ht.get("ISMANAGER").toString());
					String MOBILE = ht.get("TEL")==null?"":ht.get("TEL").toString();
					//String USERNO = ht.get("USERNO").toString();
					String EXTEND1 = KHBH;
					String EXTEND2 = CUSTOMERNAME;
					Long USERTYPE = Long.parseLong(ht.get("USERTYPE").toString());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date STARTDATE = sdf.parse(ht.get("STARTDATE").toString());
					Date ENDDATE = sdf.parse(ht.get("ENDDATE").toString());
					Long USERSTATE = Long.parseLong(ht.get("USERSTATE").toString());
					Long COMPANYID = Long.parseLong(ht.get("COMPANYID").toString());
					String COMPANYNAME = ht.get("COMPANYNAME").toString();
					//获取模型的UUID
					String KH_UUID = NoticeUtil.getInstance().GetUUID("客户主数据维护");							
					String CXDD_UUID= NoticeUtil.getInstance().GetUUID("持续督导分派");
					//客户						
					HashMap ht_KH=new HashMap();
					//ht_KH.put("ID", Integer.parseInt(ht.get("ID").toString()));
					ht_KH.put("CREATEUSER", ht.get("CREATEUSER").toString());
					ht_KH.put("CREATEDATE", UtilDate.dateFormat(new Date()));
					ht_KH.put("CUSTOMERNAME", CUSTOMERNAME);
					ht_KH.put("CUSTOMERNO",KHBH);
					ht_KH.put("STATUS", ht.get("STATUS").toString());
					ht_KH.put("USERNAME", ht.get("USERNAME")==null?"":ht.get("USERNAME").toString());
					ht_KH.put("TEL", ht.get("TEL")==null?"":ht.get("TEL").toString());
					ht_KH.put("EMAIL", ht.get("EMAIL")==null?"":ht.get("EMAIL").toString());
					ht_KH.put("ZQJC", ht.get("ZQJC").toString());
					ht_KH.put("ZQDM", ht.get("ZQDM").toString());
					ht_KH.put("YGP", ht.get("YGP").toString());
					
					Long newInstanceId_KH = DemAPI.getInstance().newInstance(KH_UUID,SecurityUtil.supermanager);
					DemAPI.getInstance().saveFormData(KH_UUID, newInstanceId_KH, ht_KH,false);
					//部门					
					OrgDepartment model=new OrgDepartment();					
					model.setDepartmentname(CUSTOMERNAME);
					model.setDepartmentno(KHBH);
					model.setCompanyid("2");
					model.setParentdepartmentid(new Long(51));
					model.setDepartmentstate("0");
					OrganizationAPI.getInstance().addDepartment(model);					
					
					//保存完之后，获取保存的主键ID
					String DepID=DBUtil.getString("select * from ORGDEPARTMENT where DEPARTMENTNAME='"+model.getDepartmentname()+"'", "ID");
					//持续督导
					HashMap ht_CXDD=new HashMap();
					ht_CXDD.put("KHBH", KHBH);
					ht_CXDD.put("KHMC", CUSTOMERNAME);
					ht_CXDD.put("TXDF", "否");
					ht_CXDD.put("SFFP", "是");					
					Long newInstanceId_CXDD = DemAPI.getInstance().newInstance(CXDD_UUID,SecurityUtil.supermanager);
					DemAPI.getInstance().saveFormData(CXDD_UUID, newInstanceId_CXDD, ht_CXDD,false);
					//用户
				    String sql = "SELECT MAX(id)+1 ID FROM OrgUser ";
				    String noStr = null;
				    String ll = DBUtil.getString(sql,"ID");				  
				    if (ll == null)
				        noStr = "1";
				    else {
				        noStr = ll.toString();
				    }
				    if (noStr.length() == 1)
				      noStr = "000" + noStr;
				    else if (noStr.length() == 2)
				      noStr = "00" + noStr;
				    else if (noStr.length() == 3)
				      noStr = "0" + noStr;
				    else {
				      noStr = noStr;
				    }
					OrgUser orguser=new OrgUser();					
					orguser.setUserid(USERID);
					orguser.setUsername(USERNAME);
					orguser.setPassword(PASSWORD);
					orguser.setDepartmentid(Long.parseLong(DepID));
					orguser.setDepartmentname(CUSTOMERNAME);
					orguser.setOrgroleid(ORGROLEID);
					orguser.setIsmanager(ISMANAGER);
					orguser.setMobile(MOBILE);
					orguser.setExtend1(EXTEND1);
					orguser.setExtend2(EXTEND2);
					orguser.setUsertype(USERTYPE);
					orguser.setStartdate(STARTDATE);
					orguser.setEnddate(ENDDATE);
					orguser.setUserno(noStr);
					orguser.setUserstate(USERSTATE);
					orguser.setCompanyid(COMPANYID);
					orguser.setCompanyname(COMPANYNAME);
					OrganizationAPI.getInstance().addUser(orguser);
					//最后处理，如果我们自动生成了一个公告编号，那么需要修改数据库中存的索引，
					//除此之外，还要更改已有的公告数据中客户编号
					if(bl)
					{
						NoticeUtil.getInstance().UpdateNotice(OldKHBH,KHBH,CUSTOMERNAME);
						NoticeUtil.getInstance().UpdateSeqValue("BPM:0", Seq);
					}
				}			
			}
		}
		 catch(Exception e)
		 {
			
		 }		
	}
}
