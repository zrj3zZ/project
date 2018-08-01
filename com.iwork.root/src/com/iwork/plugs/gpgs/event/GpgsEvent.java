package com.iwork.plugs.gpgs.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.gpgs.util.GpgsUtil;
import com.iwork.plugs.notice.util.NoticeUtil;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.OrganizationAPI;

public class GpgsEvent implements IWorkScheduleInterface {
	private static Logger logger = Logger.getLogger(GpgsEvent.class);
	public boolean executeAfter() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-挂牌公司导入结束......");
		return true;
	}

	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-挂牌公司导入前...... ");
		AddGPGSToDB();
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-挂牌公司导入中...... ");
		return true;
	}

	public void AddGPGSToDB() {
		try {
			List<HashMap> list = GpgsUtil.getInstance().GetGPGSInfo();
			String KH_UUID = NoticeUtil.getInstance().GetUUID("客户主数据维护");
			// 导入和未导入的记录数，用于输出
			int Daoru = 0;
			int WDaoru = 0;
			String KHMC="";
			for (HashMap ht : list) {
				// 如果数据库中该公告，跳过
				try
				{
				KHMC = ht.get("GSQC").toString();
				List<HashMap> listHK = NoticeUtil.getInstance()
						.IsExistsKH(KHMC);
				// 若存在，则跳过并记录跳过的数目
				if (listHK.size() > 0) {
					WDaoru++;
					continue;
				}
				// 客户编号
				String KHBH = "";
				int Seq = 0;				
				String str = NoticeUtil.getInstance().GetCustomerNo();
				String[] strArr = str.split(",");
				KHBH = strArr[0];
				Seq = Integer.parseInt(strArr[1]);

				// 客户联系人
				String UserName = ht.get("DM") == null?ht.get("GFDM").toString():"".equals(ht.get("DM").toString())?ht.get("GFDM").toString():ht.get("DM").toString();
				// 电话
				String Tel = ht.get("DMDH") == null ? "" : ht.get("DMDH")
						.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
				// 注册地址
				String BGDZ = ht.get("BGDZ") == null ? "" : ht.get("BGDZ")
						.toString();
				// 所属行业
				String SSHY = ht.get("HYFL") == null ? "" : ht.get("HYFL")
						.toString();
				// 公司网址
				String GSWZ = ht.get("WZ") == null ? "" : ht.get("WZ")
						.toString();
				//公司电话
				String PHONE = ht.get("DH") == null ? "" : ht.get("DH")
						.toString();
				//公司传真
				String FAX = ht.get("CZ") == null ? "" : ht.get("CZ")
						.toString();
				//董秘手机号码
				String Mobile=ht.get("Mobile")==null?"":ht.get("Mobile").toString();
				//客户表中客户联系方式如果为空，则用公司电话
				String KHDH=Mobile;
				if(KHDH=="")
				{
					KHDH = PHONE;
				}
				HashMap ht_KH = new HashMap();
				// ht_KH.put("ID", Integer.parseInt(ht.get("ID").toString()));
				ht_KH.put("CREATEUSER", "超级管理员");
				ht_KH.put("CREATEDATE", UtilDate.dateFormat(new Date()));
				ht_KH.put("CUSTOMERNAME", ht.get("GSQC").toString());
				ht_KH.put("CUSTOMERNO", KHBH);
				ht_KH.put("STATUS", "有效");
				ht_KH.put("USERNAME", UserName);
				ht_KH.put("TEL", KHDH);
				// ht_KH.put("EMAIL",ht.get("EMAIL")==null?"":ht.get("EMAIL").toString());
				ht_KH.put("ZQJC", ht.get("GSJC").toString());
				ht_KH.put("ZQDM", ht.get("GFDM").toString());
				ht_KH.put("YGP", "已挂牌");
				if(ht.get("YXGSRQ")!=null)
				{
					Date YXGSRQ = sdf.parse(ht.get("YXGSRQ").toString());
					ht_KH.put("YXGSRQ", YXGSRQ);
				}
				if(ht.get("GFGSRQ")!=null)
				{
					Date GFGSRQ = sdf.parse(ht.get("GFGSRQ").toString());
					ht_KH.put("GFGSRQ", GFGSRQ);
				}
				ht_KH.put("BGDZ", BGDZ);
				if(ht.get("GPSJ")!=null)
				{
					Date GPSJ = sdf.parse(ht.get("GPSJ").toString());
					ht_KH.put("GPSJ", GPSJ);
				}
				ht_KH.put("SSHY", SSHY);
				ht_KH.put("GSWZ", GSWZ);
				ht_KH.put("PHONE", PHONE);
				ht_KH.put("FAX", FAX);
				Long newInstanceId = DemAPI.getInstance().newInstance(KH_UUID,SecurityUtil.supermanager);
				DemAPI.getInstance().saveFormData(KH_UUID, newInstanceId,
						ht_KH, false);
				// ----------------部门添加----------
				OrgDepartment model = new OrgDepartment();
				model.setDepartmentname(ht.get("GSQC").toString());
				model.setDepartmentno(KHBH);
				model.setCompanyid("2");
				model.setParentdepartmentid(new Long(51));
				model.setDepartmentstate("0");
				OrganizationAPI.getInstance().addDepartment(model);
				// 保存完之后，获取保存的主键ID，即部门ID
				String DepID = DBUtil.getString(
						"select * from ORGDEPARTMENT where DEPARTMENTNAME='"
								+ model.getDepartmentname() + "'", "ID");
				// ----------------用户添加----------
				String sql = "SELECT MAX(id)+1 ID FROM OrgUser ";
				String noStr = null;
				String ll = DBUtil.getString(sql, "ID");
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
				OrgUser orguser = new OrgUser();
				orguser.setUserid(ht.get("GFDM").toString());
				orguser.setUsername(UserName);
				orguser.setPassword("E10ADC3949BA59ABBE56E057F20F883E");
				orguser.setDepartmentid(Long.parseLong(DepID));
				orguser.setDepartmentname(ht.get("GSQC").toString());
				orguser.setOrgroleid(3L);
				orguser.setIsmanager(0L);
				orguser.setMobile(Mobile);
				orguser.setExtend1(KHBH);
				orguser.setExtend2(ht.get("GSQC").toString());
				orguser.setUsertype(0L);
				orguser.setStartdate(sdf.parse("2014-01-01"));
				orguser.setEnddate(sdf.parse("2099-12-31"));
				orguser.setUserno(noStr);
				orguser.setUserstate(0L);
				orguser.setCompanyid(2L);
				orguser.setCompanyname(ht.get("ZBQS").toString());
				OrganizationAPI.getInstance().addUser(orguser);
				//--------------------持续督导------------------------
				String CXDD_UUID= NoticeUtil.getInstance().GetUUID("持续督导分派");
				HashMap ht_CXDD=new HashMap();
				ht_CXDD.put("KHBH", KHBH);
				ht_CXDD.put("KHMC", ht.get("GSQC").toString());
				ht_CXDD.put("TXDF", "否");
				ht_CXDD.put("SFFP", "是");					
				Long newInstanceId_CXDD = DemAPI.getInstance().newInstance(CXDD_UUID,SecurityUtil.supermanager);
				DemAPI.getInstance().saveFormData(CXDD_UUID, newInstanceId_CXDD, ht_CXDD,false);
				NoticeUtil.getInstance().UpdateSeqValue("BPM:0", Seq);
				Daoru++;
				}
				catch(Exception e)
				{
					logger.error(e,e);
				}
			}		
			System.out.println("导入完毕 ,时间：" + UtilDate.dateFormat(new Date()));
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
}
