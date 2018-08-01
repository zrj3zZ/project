package com.iwork.plugs.synchroCus.event;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.UtilDate;
import com.iwork.plugs.notice.util.NoticeUtil;
import com.iwork.plugs.synchroCus.util.SynCusUtil;

public class UpdateRelations implements IWorkScheduleInterface {
	
	private static Logger logger = Logger.getLogger(UpdateRelations.class);
	public boolean executeBefore() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-即将开始客户信息关系同步......");
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-开始客户信息关系同步...... ");
		SetRelations();
		return true;
	}

	public boolean executeAfter() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.dateFormat(new Date()) + "]-客户信息关系同步完毕...... ");
		return true;
	}

	// 首次同步关系
	public void SetRelations() {
		try {
			List<HashMap> list = SynCusUtil.getInstance().GetNewCusByService();
			int iNum=0;
			for (HashMap ht : list) {
				String ALIAS_NAME = ht.get("ALIAS_NAME").toString(); // 客户名称
				if (ALIAS_NAME == "") {
					continue;
				}
				// 同步已挂牌的公司			
				if ("Y".equals(ht.get("X_LISTED_COMPANY_FLG").toString()
						.toUpperCase())) {
					// 本次根据客户名称（公司名称）判断
					List<HashMap> listHK = NoticeUtil.getInstance().IsExistsKH(
							ALIAS_NAME);
					// 如果三板数据库中存在该客户，那么需要创建其对应关系
					if (listHK.size() > 0) {
						long id = Long.parseLong(listHK.get(0).get("ID")
								.toString());
						String HT_KH_ID = ht.get("ROW_ID").toString(); // 华泰库对应主键ID
						// 添加对应关系
						int Res = SynCusUtil.getInstance().RelationsAdd(id,
								HT_KH_ID);
						if (Res < 1) {
							System.out.println("时间：["
									+ UtilDate.getNowDatetime()
									+ "]-公司：[" + ALIAS_NAME + "]同步关系出错");
						}
						else
						{
							iNum++;
						}
					}
				}
			}
			System.out.println("时间：[" + UtilDate.getNowDatetime()
					+ "]-共同步"+iNum+"条记录");
			List<HashMap> listError = SynCusUtil.getInstance().GetErrorInfo();
			if(listError.size()>0)
			{
				StringBuffer StrTip = new StringBuffer();
				StrTip.append("三板数据库中存在未匹配公司信息：");
				for(HashMap ht :listError)
				{
					StrTip.append("【"+ht.get("CUSTOMERNAME").toString()+"】");					
				}
				logger.error(StrTip.toString());
			}
			
		} catch (Exception e) {
			logger.error(e,e);
		}

	}

}
