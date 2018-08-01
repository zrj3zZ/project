package com.ibpmsoft.project.zqb.zt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.zt.dao.ZqbZtProjectDao;
import com.iwork.commons.util.DBUTilNew;

public class ZqbZtProjectService {
//	private static final String ZQB_EVEVT_TALK_UUID = "c807510e83a0415cb37810bc2994d71a";
//	private static final String ZQB_CUSTOMER_PURVIEW_UUID = "84ff70949eac4051806dc02cf4837bd9";
	private static Logger logger = Logger.getLogger(ZqbZtProjectService.class);
	private ZqbZtProjectDao zqbZtProjectDao;
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件
	public ZqbZtProjectDao getZqbZtProjectDao() {
		return zqbZtProjectDao;
	}

	public void setZqbZtProjectDao(ZqbZtProjectDao zqbZtProjectDao) {
		this.zqbZtProjectDao = zqbZtProjectDao;
	}

	public List<HashMap> getMeetRunList(int pageSize, int pageNumber, String userid, String customerno, Long orgRoleId,
			String zqdm, String zqjc, String startdate, String enddate, String noticename, String spzt) {
		List<HashMap> list=zqbZtProjectDao.getMeetRunList(pageSize,pageNumber,userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);//DemAPI.getInstance().getList(this.getConfigUUID("rcywcbuuid"), conditionMap, "ID desc", pageSize,(pageNumber - 1) * pageSize);
		return list;
	}

	public int getEventRunListSize(String userid, String customerno, Long orgRoleId, String zqdm, String zqjc,
			String startdate, String enddate, String noticename, String spzt) {
		List<HashMap> list = zqbZtProjectDao.getEventRunListSize(userid,customerno,orgRoleId,zqdm,zqjc,startdate,enddate,noticename,spzt);
		int num=list.size();
		return num;
	}

	public String removeZtNbcb(Long instanceid) {
		String flag="";
		if(instanceid!=null && !"".equals(instanceid)){
			try {
				Map params = new HashMap();
				params.put(1, instanceid);
				DBUTilNew.update(" delete from BD_ZQB_GPJDNODE where INSTANCEID= ? ",params);
				flag="success";
			} catch (Exception e) {
				flag="error";
			}
		}
		
		return flag;
	}

}
