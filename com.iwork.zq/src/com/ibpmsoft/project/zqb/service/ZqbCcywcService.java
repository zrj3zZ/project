package com.ibpmsoft.project.zqb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.dao.ZqbCcywcDao;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;

public class ZqbCcywcService {
	private static Logger logger = Logger.getLogger(ShanXiZqbBgProjectManageService.class);
	private final static String CN_FILENAME = "/common.properties";
	private ZqbCcywcDao zqbCcywcDao;
	public String getConfigUUID(String parameter) {
		String config = ConfigUtil.readValue(CN_FILENAME, parameter);
		return config;
	}
	//出差与外出查询
	public List<HashMap<String, Object>> getCcwcList(String ccsy, String zt,String bslx, int pageSize, int pageNumber) {
		List<String> parameter=new ArrayList<String>();//存放参数WHERE a.rn>1 AND a.rn<=10
		StringBuffer sql=new StringBuffer();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		
		OrgUser user = uc.get_userModel();
		String departmentname = user.getDepartmentname();
		Long ismanager = user.getIsmanager();
		String configUUID = this.getConfigUUID("HLGDKSY")==null?"":this.getConfigUUID("HLGDKSY");
		boolean flag=false;
		if(!configUUID.contains(",")){
			if(user.getUserid().equals(configUUID)){
				flag=true;
				
			}
			
		}else{
			
			String[] split = configUUID.split(",");
			for (String string : split) {
				if(user.getUserid().equals(string)){
					flag=true;
					break;
				}
			}
		}
		
		
//		if(flag|| user.getOrgroleid()==5){
			sql.append("  select * from ( select   ID,yhm,ccsy,ccmdd,zt,sqsj,jssj,qssj,ccfj,lcbh,rwid,lzjd,instanceid,bslx,rownum rn from   ");//
			sql.append("(   select CASE WHEN ID IS NULL THEN 0 ELSE ID END ID,o.username yhm,s.yhid,s.ccsy ccsy,s.ccmdd ccmdd,s.zt zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj, "
					+ " to_char(s.jssj,'yyyy-MM-dd HH:mm') jssj,to_char(s.qssj,'yyyy-MM-dd HH:mm') qssj,s.ccfj ccfj,s.lcbh lcbh,s.rwid rwid,s.lzjd  ");
			sql.append(" lzjd,s.instanceid instanceid,s.bslx bslx from (select username from orguser where orgroleid<>3 ) O LEFT JOIN BD_ZQB_CCYWCLCB s  ");
			sql.append(" on o.username=s.yhm  and ID in (SELECT MAX(ID) FROM BD_ZQB_CCYWCLCB B GROUP BY B.YHM ) ");
			if(ccsy!=null&&!ccsy.equals("")){//
				sql.append(" and s.ccsy like  ?");
				parameter.add("%"+ccsy+"%");
			}
			
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt =  ?");
				parameter.add(zt);
			}
			if(bslx!=null&&!bslx.equals("")){
				sql.append(" and s.bslx =  ?");
				parameter.add(bslx);
			}
			
			
			if(!("综合管理部".equals(departmentname)||"吕红贞".equals(user.getUsername())||"杨光".equals(user.getUsername()))){
			sql.append("and YHID in( select userid from orguser where departmentname=?) ");
			
			parameter.add(departmentname);
			if(ismanager==0){
				sql.append("and YHM=? ");
				parameter.add(user.getUsername());
			}
			}
			

		//   
		sql.append("  order by id desc  ) s  ) WHERE rn>? AND rn<=?  ");
		List<HashMap<String, Object>> list = zqbCcywcDao.getCcwcList(sql.toString(),parameter,pageNumber,pageSize);
		return list;
	}

	public int getCcwcListSize(String ccsy,String zt,String bslx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		Long ismanager = user.getIsmanager();
		String departmentname = user.getDepartmentname();
//		if(user.getOrgroleid()==13 || user.getOrgroleid()==5){
			
			sql.append("  select s.id id,o.username yhm,s.yhid,s.ccsy ccsy,s.ccmdd ccmdd,s.zt zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj,   ");
			sql.append("  to_char(s.jssj,'yyyy-MM-dd HH:mm') jssj,to_char(s.qssj,'yyyy-MM-dd HH:mm') qssj,s.ccfj ccfj,s.lcbh lcbh,s.rwid rwid,s.lzjd    ");
			sql.append(" lzjd,s.instanceid instanceid,s.bslx bslx from (select username from orguser where orgroleid<>3 ) O LEFT JOIN BD_ZQB_CCYWCLCB s  ");
			sql.append(" on o.username=s.yhm  and ID in (SELECT MAX(ID) FROM BD_ZQB_CCYWCLCB B GROUP BY B.YHM )     ");
			if(ccsy!=null&&!ccsy.equals("")){
				sql.append(" and s.ccsy like  ?");
				parameter.add("%"+ccsy+"%");
			}
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt like  ?");
				parameter.add("%"+zt+"%");
			}
			if(bslx!=null&&!bslx.equals("")){
				sql.append(" and s.bslx like  ?");
				parameter.add("%"+bslx+"%");
			}
			if(!("综合管理部".equals(departmentname)||"吕红贞".equals(user.getUsername())||"杨光".equals(user.getUsername()))){
				sql.append("and YHID in( select userid from orguser where departmentname=?) ");
				
				parameter.add(departmentname);
				if(ismanager==0){
					sql.append("and YHM=? ");
					parameter.add(user.getUsername());
				}
				}
//		}else if(user.getOrgroleid()!=3){
//			sql.append("  select s.* ,rownum rn from (  ");//,s.zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj
//			sql.append(" select s.id,s.yhm yhm,s.ccsy ccsy,s.ccmdd ccmdd,s.ccfj  ccfj from BD_ZQB_CCYWCLCB s  where 1=1");
//			if(ccsy!=null&&!ccsy.equals("")){
//				sql.append(" and s.ccsy like  ?");
//				parameter.add("%"+ccsy+"%");
//			}
//			
//			if(zt!=null&&!zt.equals("")){
//				sql.append(" and s.zt =  ?");
//				parameter.add(zt);
//			}
//			if(bslx!=null&&!bslx.equals("")){
//				sql.append(" and s.bslx =  ?");
//				parameter.add(bslx);
//			}
//			sql.append(" and s.yhid =  ? ");
//			parameter.add(user.getUserid());
//		}
		//order by  s.sqsj desc
			
		int count = zqbCcywcDao.getCcwcListSize(sql.toString(),parameter);
		return count;
	}
	public List<HashMap<String, Object>> getCcwcbyuseridList(String yhm,String ccsy, String zt,String bslx, int pageSize, int pageNumber) {
		List<String> parameter=new ArrayList<String>();//存放参数WHERE a.rn>1 AND a.rn<=10
		StringBuffer sql=new StringBuffer();
		
		
		
	
			sql.append("  select * from ( select id,yhm,ccsy,ccmdd,zt,sqsj,jssj,qssj,ccfj,lcbh,rwid,lzjd,instanceid,bslx,rownum rn from (  ");//
			sql.append(" select s.id id,s.yhm yhm,s.yhid,s.ccsy ccsy,s.ccmdd ccmdd,s.zt zt,to_char(s.sqsj,'yyyy-MM-dd') sqsj, to_char(s.jssj,'yyyy-MM-dd HH:mm') jssj,to_char(s.qssj,'yyyy-MM-dd HH:mm') qssj,"
					+ "s.ccfj ccfj,s.lcbh lcbh,s.rwid rwid,s.lzjd lzjd,s.instanceid instanceid,s.bslx bslx from BD_ZQB_CCYWCLCB s  where 1=1   ");
			if(ccsy!=null&&!ccsy.equals("")){//
				sql.append(" and s.ccsy like  ?");
				parameter.add("%"+ccsy+"%");
			}
			
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt =  ?");
				parameter.add(zt);
			}
			if(bslx!=null&&!bslx.equals("")){
				sql.append(" and s.bslx =  ?");
				parameter.add(bslx);
			}
			
			
		
				sql.append("and YHM=? ");
				parameter.add(yhm);
			
		
			

		sql.append("  order by  sqsj desc  ) s ) WHERE rn>? AND rn<=?   ");
		List<HashMap<String, Object>> list = zqbCcywcDao.getCcwcList(sql.toString(),parameter,pageNumber,pageSize);
		return list;
	}
	
	
	public int getCcwcbyuseridListSize(String yhm,String ccsy,String zt,String bslx) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql=new StringBuffer();
		
		

			sql.append("   select s.* ,rownum rn from (  ");//,to_char(s.sqsj,'yyyy-MM-dd') sqsj
			sql.append(" select s.id,s.yhm yhm,s.ccsy ccsy,s.ccmdd ccmdd,s.ccfj  ccfj from BD_ZQB_CCYWCLCB s  where 1=1");
			if(ccsy!=null&&!ccsy.equals("")){
				sql.append(" and s.ccsy like  ?");
				parameter.add("%"+ccsy+"%");
			}
			if(zt!=null&&!zt.equals("")){
				sql.append(" and s.zt like  ?");
				parameter.add("%"+zt+"%");
			}
			if(bslx!=null&&!bslx.equals("")){
				sql.append(" and s.bslx like  ?");
				parameter.add("%"+bslx+"%");
			}
		
					sql.append("and YHM=? ");
					parameter.add(yhm);
			

			sql.append("  ) s     ");
		int count = zqbCcywcDao.getCcwcListSize(sql.toString(),parameter);
		return count;
	}
	public ZqbCcywcDao getZqbCcywcDao() {
		return zqbCcywcDao;
	}

	public void setZqbCcywcDao(ZqbCcywcDao zqbCcywcDao) {
		this.zqbCcywcDao = zqbCcywcDao;
	}

	public String delCc(String ccid) {
		
			String flag="";
			if(ccid!=null && !"".equals(ccid)){
				try {
					Map params = new HashMap();
					params.put(1, ccid);
					DBUTilNew.update(" delete from BD_ZQB_CCYWCLCB where id= ? ",params);
					flag="success";
				} catch (Exception e) {
					flag="error";
				}
			}
			
			return flag;
		}
	

}
