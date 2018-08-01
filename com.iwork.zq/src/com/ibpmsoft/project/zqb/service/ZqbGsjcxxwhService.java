package com.ibpmsoft.project.zqb.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.MessageAPI;

public class ZqbGsjcxxwhService {
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	public final static Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);
	public final static String zqServer = config.get("zqServer")==null?"":config.get("zqServer").toString();
	private static Logger logger = Logger.getLogger(ZqbGsjcxxwhService.class);
	
	/**
	 * 分子公司 财务报表
	 * @param name
	 * @param filename
	 * @return
	 */
	public String delCubb(String name){
		
		if(zqServer.equals("hlzq")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			if(user.getOrgroleid()==3){
				String name1="";
				try {
					if(name!=null && !"".equals(name)){
						name1= java.net.URLDecoder.decode(name, "utf-8");
					}
				} catch (UnsupportedEncodingException e) {
					
				}
				Map params = new HashMap();
				List lables = new ArrayList();
				lables.add("zqdm");
				lables.add("zqjc");
				lables.add("fg");
				params = new HashMap();
				params.put(1, user.getUserid());
				String sql="select s.zqdm,s.zqjc,z.fg from bd_zqb_kh_base s left join orguser t on s.customerno=t.extend1 left join"
						+ "  (select SUBSTR(s.khfzr, 1, INSTR(s.khfzr, '[', 1, 1) - 1) fg,s.khbh from bd_mdm_khqxglb s ) z on z.khbh=s.customerno where t.userid= ?  ";
				List<HashMap> ddxx = DBUtil.getDataList(lables, sql, params);
				String zqdm="";
				String zqjc="";
				String fg="";
				if(ddxx.size()>0){
					zqdm=ddxx.get(0).get("zqdm")==null?"":ddxx.get(0).get("zqdm").toString();
					zqjc=ddxx.get(0).get("zqjc")==null?"":ddxx.get(0).get("zqjc").toString();
					fg=ddxx.get(0).get("fg")==null?"":ddxx.get(0).get("fg").toString();
				}
				if(!"".equals(fg)){
					MessageAPI.getInstance().sendSysMsg(fg, "分子公司信息"+"维护提醒", zqdm+zqjc+"更新了"+"分子公司信息"+","+name1+"--财务报表"+"，请查看详情");
				}
			}
		}
		return null;
	}
	/**
	 * 分子公司 财务报表
	 * @param name
	 * @param filename
	 * @return
	 */
	public String cwbbTyff(String name ){
		
		if(zqServer.equals("hlzq")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			if(user.getOrgroleid()==3){
				String name1="";
				try {
					if(name!=null && !"".equals(name)){
						name1= java.net.URLDecoder.decode(name, "utf-8");
					}
				} catch (UnsupportedEncodingException e) {
					
				}
				Map params = new HashMap();
				List lables = new ArrayList();
				lables.add("zqdm");
				lables.add("zqjc");
				lables.add("fg");
				params = new HashMap();
				params.put(1, user.getUserid());
				String sql="select s.zqdm,s.zqjc,z.fg from bd_zqb_kh_base s left join orguser t on s.customerno=t.extend1 left join"
						+ "  (select SUBSTR(s.khfzr, 1, INSTR(s.khfzr, '[', 1, 1) - 1) fg,s.khbh from bd_mdm_khqxglb s ) z on z.khbh=s.customerno where t.userid= ?  ";
				List<HashMap> ddxx = DBUtil.getDataList(lables, sql, params);
				String zqdm="";
				String zqjc="";
				String fg="";
				if(ddxx.size()>0){
					zqdm=ddxx.get(0).get("zqdm")==null?"":ddxx.get(0).get("zqdm").toString();
					zqjc=ddxx.get(0).get("zqjc")==null?"":ddxx.get(0).get("zqjc").toString();
					fg=ddxx.get(0).get("fg")==null?"":ddxx.get(0).get("fg").toString();
				}
				if(!"".equals(fg)){
					MessageAPI.getInstance().sendSysMsg(fg, "分子公司信息"+"维护提醒", zqdm+zqjc+"更新了"+"分子公司信息"+","+name1+"--财务报表"+"，请查看详情");
				}
			}
		}
		return null;
	}
	/**
	 *  增加/修改  公司基础信息维护其他的提醒通用方法
	 * @param instanceid
	 * @param name
	 * @return
	 */
	public String addOrUpdTyff(String instanceid,String name,String filename){
		if(zqServer.equals("hlzq")){
			String name1="";
			String filename1="";
			try {
				filename1= java.net.URLDecoder.decode(filename, "utf-8");
				if(name!=null && !"".equals(name)){
					name1= java.net.URLDecoder.decode(name, "utf-8");
				}
			} catch (UnsupportedEncodingException e) {
				
			}
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			if(user.getOrgroleid()==3){
				String dxnn="";
				Map params = new HashMap();
				List lables = new ArrayList();
				lables.add("zqdm");
				lables.add("zqjc");
				lables.add("fg");
				params = new HashMap();
				params.put(1, user.getUserid());
				String sql="select s.zqdm,s.zqjc,z.fg from bd_zqb_kh_base s left join orguser t on s.customerno=t.extend1 left join"
						+ "  (select SUBSTR(s.khfzr, 1, INSTR(s.khfzr, '[', 1, 1) - 1) fg,s.khbh from bd_mdm_khqxglb s ) z on z.khbh=s.customerno where t.userid= ?  ";
				List<HashMap> ddxx = DBUtil.getDataList(lables, sql, params);
				String zqdm="";
				String zqjc="";
				String fg="";
				if(ddxx.size()>0){
					zqdm=ddxx.get(0).get("zqdm")==null?"":ddxx.get(0).get("zqdm").toString();
					zqjc=ddxx.get(0).get("zqjc")==null?"":ddxx.get(0).get("zqjc").toString();
					fg=ddxx.get(0).get("fg")==null?"":ddxx.get(0).get("fg").toString();
				}
				if(instanceid.equals("0")){
					if(!"".equals(fg))
						MessageAPI.getInstance().sendSysMsg(fg, filename1+"维护提醒", zqdm+zqjc+"新增了"+filename1+"，"+name1+"，请查看详情");
				}else{
					if(!"".equals(fg))
						MessageAPI.getInstance().sendSysMsg(fg, filename1+"维护提醒", zqdm+zqjc+"修改了"+filename1+"，"+name1+"，请查看详情");
				}
			}
		}
		return null;
	}
	/**
	 * 增加或修改制度清单
	 * @param instanceid
	 * @param name
	 * @param filenames
	 * @return
	 */
	public String addOrUpdGszc(String instanceid,String name,String filenames){
		
		if(zqServer.equals("hlzq")){
			String name1="";
			String filename="";
			try {
				filename= java.net.URLDecoder.decode(name, "utf-8");
				name1=java.net.URLDecoder.decode(filenames, "utf-8");
			} catch (UnsupportedEncodingException e) {
				
			}
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			
			String dxnn="";
			Map params = new HashMap();
			List lables = new ArrayList();
			lables.add("zqdm");
			lables.add("zqjc");
			lables.add("fg");
			params = new HashMap();
			params.put(1, user.getUserid());
			String sql="select s.zqdm,s.zqjc,z.fg from bd_zqb_kh_base s left join orguser t on s.customerno=t.extend1 left join"
					+ "  (select SUBSTR(s.khfzr, 1, INSTR(s.khfzr, '[', 1, 1) - 1) fg,s.khbh from bd_mdm_khqxglb s ) z on z.khbh=s.customerno where t.userid= ?  ";
			List<HashMap> ddxx = DBUtil.getDataList(lables, sql, params);
			String zqdm="";
			String zqjc="";
			String fg="";
			if(ddxx.size()>0){
				zqdm=ddxx.get(0).get("zqdm")==null?"":ddxx.get(0).get("zqdm").toString();
				zqjc=ddxx.get(0).get("zqjc")==null?"":ddxx.get(0).get("zqjc").toString();
				fg=ddxx.get(0).get("fg")==null?"":ddxx.get(0).get("fg").toString();
			}
			//MessageAPI.getInstance().sendSysMsg(userid, title, content);
			if(user.getOrgroleid()==3){
				if(!"".equals(name1) && name1!=null && !instanceid.equals("0")){
					dxnn=zqdm+zqjc+"修改了公司章程，"+name1+"，请查看详情";
					if(!"".equals(fg))
						MessageAPI.getInstance().sendSysMsg(fg, "公司章程维护提醒", dxnn);
				}else{
					if(!filename.equals("0")){
						if(instanceid.equals("0")){
							dxnn=zqdm+zqjc+"新增了公司章程，"+filename+"，请查看详情";
							if(!"".equals(fg))
								MessageAPI.getInstance().sendSysMsg(fg, "公司章程维护提醒", dxnn);
						}else{
							dxnn=zqdm+zqjc+"修改了公司章程，"+filename+"，请查看详情";
							if(!"".equals(fg))
								MessageAPI.getInstance().sendSysMsg(fg, "公司章程维护提醒", dxnn);
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 子表保存系统提醒
	 * @param instanceId
	 * @param formid
	 * @param foid
	 * @return
	 */
	public String zbtyff(String instanceId,String formid,String foid){
		if(zqServer.equals("hlzq")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			Map params = null;
			List lables = new ArrayList();
			if(user.getOrgroleid()==3){
				
				params = new HashMap();
				params.put(1, formid);
			//	String jspTitle=DBUTilNew.getDataStr("iform_title", "select iform_title from sys_engine_iform where id = ? ", params);
				
				params = new HashMap();
				lables = new ArrayList();
				lables.add("entityname");
				lables.add("entitytitle");
				lables.add("dataid");
				StringBuffer sql1 = new StringBuffer();
				sql1.append(" select s.entityname,s.entitytitle,t.dataid from sys_engine_metadata s left join sys_engine_form_bind t on t.metadataid=s.id where t.instanceid=? order by s.id ");
				String entityname="";
				String entitytitle="";
				String dataid="";
				params.put(1, instanceId);
				List<HashMap> targetmsg = DBUtil.getDataList(lables, sql1.toString(), params);
				//if(jspTitle.equals("高管基本信息表")){
				if(targetmsg.size()>0){
					entityname=targetmsg.get(0).get("entityname")==null?"":targetmsg.get(0).get("entityname").toString();
					entitytitle=targetmsg.get(0).get("entitytitle")==null?"":targetmsg.get(0).get("entitytitle").toString();
					dataid=targetmsg.get(0).get("dataid")==null?"":targetmsg.get(0).get("dataid").toString();
				}
				//}
				if(entitytitle.equals("高管基本信息表")){
					zbtyffs("董监高信息",dataid,entityname,"xm",foid);
				}else if(entitytitle.equals("分子公司管理主表")){
					zbtyffs("分子公司信息",dataid,entityname,"gsmc",foid);
				}
			}
		}
		return null;
	}
	public String zbtyffs(String title,String dataid,String entityname,String lm,String foid ){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		Map params = new HashMap();
		params.put(1, foid);
		String jspTitle=DBUTilNew.getDataStr("iform_title", "select iform_title from sys_engine_iform where id = ? ", params);
		params = new HashMap();
		List lables = new ArrayList();
		lables.add("zqdm");
		lables.add("zqjc");
		lables.add("fg");
		params = new HashMap();
		params.put(1, user.getUserid());
		String sql="select s.zqdm,s.zqjc,z.fg from bd_zqb_kh_base s left join orguser t on s.customerno=t.extend1 left join"
				+ "  (select SUBSTR(s.khfzr, 1, INSTR(s.khfzr, '[', 1, 1) - 1) fg,s.khbh from bd_mdm_khqxglb s ) z on z.khbh=s.customerno where t.userid= ?  ";
		List<HashMap> ddxx = DBUtil.getDataList(lables, sql, params);
		String zqdm="";
		String zqjc="";
		String fg="";
		if(ddxx.size()>0){
			zqdm=ddxx.get(0).get("zqdm")==null?"":ddxx.get(0).get("zqdm").toString();
			zqjc=ddxx.get(0).get("zqjc")==null?"":ddxx.get(0).get("zqjc").toString();
			fg=ddxx.get(0).get("fg")==null?"":ddxx.get(0).get("fg").toString();
		}
		if(!"".equals(fg)){
			String bt=DBUTilNew.getDataStr(lm, "select "+lm+" from "+entityname+" where id ="+dataid, null);
			MessageAPI.getInstance().sendSysMsg(fg, title+"维护提醒", zqdm+zqjc+"更新了"+title+","+bt+"--"+jspTitle+"，请查看详情");
		}
		return null;
	}
	/**
	 * 通用删除
	 * @param temp
	 * @param formid
	 * @param demId
	 * @return
	 */
	public String delJcxxwh(String temp,String formid,String demId){
		if(zqServer.equals("hlzq")){
			UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
			OrgUser user = uc.get_userModel();
			if(user.getOrgroleid()==3){
				String[] temps=null;
				if(temp!=null && !"".equals(temp)){
					temps=temp.split(",");
				}
				if(temps!=null && temps.length>0){
					Map params = null;
					List lables = new ArrayList();
					lables.add("entityname");
					lables.add("entitytitle");
					lables.add("dataid");
					StringBuffer sql = new StringBuffer();
					sql.append(" select s.entityname,s.entitytitle,t.dataid from sys_engine_metadata s left join sys_engine_form_bind t on t.metadataid=s.id where t.instanceid=? ");
					String entityname="";
					String entitytitle="";
					String dataid="";
					for (int i = 0; i < temps.length; i++) {
						params = new HashMap();
						params.put(1, temps[i]);
						List<HashMap> targetmsg = DBUtil.getDataList(lables, sql.toString(), params);
						
						if(targetmsg.size()>0){
							entityname=targetmsg.get(0).get("entityname")==null?"":targetmsg.get(0).get("entityname").toString();
							entitytitle=targetmsg.get(0).get("entitytitle")==null?"":targetmsg.get(0).get("entitytitle").toString();
							if("".equals(dataid)){
								dataid=targetmsg.get(0).get("dataid")==null?"":targetmsg.get(0).get("dataid").toString();
							}else{
								dataid=dataid+","+(targetmsg.get(0).get("dataid")==null?"":targetmsg.get(0).get("dataid").toString());
							}
						}
					}
					if(entitytitle.equals("制度清单信息")){
						deltyff("公司章程（制度）",dataid,entityname,"zdmc");
					}else if(entitytitle.equals("承诺信息管理")){
						deltyff("对外承诺情况",dataid,entityname,"cnzt");
					}else if(entitytitle.equals("关联方")){
						deltyff("关联方信息",dataid,entityname,"gsmc");
					}else if(entitytitle.equals("分子公司管理主表")){
						deltyff("分子公司信息",dataid,entityname,"gsmc");
					}else if(entitytitle.equals("高管基本信息表")){
						deltyff("董监高信息",dataid,entityname,"xm");
					}else if(entitytitle.equals("内幕信息")){
						deltyff("内幕知情人上报",dataid,entityname,"nmsx");
					}
				}
			}
		}
		return null;
	}
	
	/**
	 *  通用删除
	 * @param entitytitle
	 * @param dataid
	 * @param entityname
	 * @param lm
	 */
	public void deltyff(String entitytitle,String dataid,String entityname,String lm){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		Map params = new HashMap();
		List lables = new ArrayList();
		lables.add("zqdm");
		lables.add("zqjc");
		lables.add("fg");
		params = new HashMap();
		params.put(1, user.getUserid());
		String sql1="select s.zqdm,s.zqjc,z.fg from bd_zqb_kh_base s left join orguser t on s.customerno=t.extend1 left join"
				+ "  (select SUBSTR(s.khfzr, 1, INSTR(s.khfzr, '[', 1, 1) - 1) fg,s.khbh from bd_mdm_khqxglb s ) z on z.khbh=s.customerno where t.userid= ?  ";
		List<HashMap> ddxx = DBUtil.getDataList(lables, sql1, params);
		String zqdm="";
		String zqjc="";
		String fg="";
		if(ddxx.size()>0){
			zqdm=ddxx.get(0).get("zqdm")==null?"":ddxx.get(0).get("zqdm").toString();
			zqjc=ddxx.get(0).get("zqjc")==null?"":ddxx.get(0).get("zqjc").toString();
			fg=ddxx.get(0).get("fg")==null?"":ddxx.get(0).get("fg").toString();
		}
		String bt="";
		if(!"".equals(dataid) && !"".equals(entityname)){
			String[] zdid=dataid.split(",");
			if(zdid!=null && zdid.length>0){
				for (int j = 0; j < zdid.length; j++) {
					Map param = new HashMap();
					param.put(1, zdid[j]);
					String zid=DBUTilNew.getDataStr(lm, "select "+lm+" from "+entityname+" where id = ? ", param);
					if(j==0){
						bt=zid;
					}else{
						bt=bt+"、"+zid;
					}
				}
				if(!"".equals(fg))
					MessageAPI.getInstance().sendSysMsg(fg, entitytitle+"维护提醒", zqdm+zqjc+"刪除了"+entitytitle+","+bt+"，请查看详情");
			}
		}
	}
	
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		ZqbGsjcxxwhService.logger = logger;
	}

}
