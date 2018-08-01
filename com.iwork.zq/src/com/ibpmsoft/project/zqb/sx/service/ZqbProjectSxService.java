package com.ibpmsoft.project.zqb.sx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.ibpmsoft.project.zqb.sx.dao.ZqbProjectSxDAO;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.sdk.DemAPI;

public class ZqbProjectSxService {
	private ZqbProjectSxDAO zqbProjectSxDAO;

	public ZqbProjectSxDAO getZqbProjectSxDAO() {
		return zqbProjectSxDAO;
	}

	public void setZqbProjectSxDAO(ZqbProjectSxDAO zqbProjectSxDAO) {
		this.zqbProjectSxDAO = zqbProjectSxDAO;
	}

	public List<HashMap> getSxQusans(String projectno) {
		return zqbProjectSxDAO.getSxQusans(projectno);
	}

	public String industryMsgAssociate(String xmbz) {
		StringBuffer jsonHtml = new StringBuffer();	
		List<HashMap> items = zqbProjectSxDAO.industryMsgAssociate(xmbz);
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}

	public boolean qusAnsDel(String contentstr) {
		String[] qinsAins = contentstr.split("#");
		Long qins = Long.parseLong(qinsAins[0]);
		Long ains = Long.parseLong(qinsAins[1]);
		boolean aflag = DemAPI.getInstance().removeFormData(qins);
		boolean qflag = DemAPI.getInstance().removeFormData(ains);
		return aflag&&qflag;
	}
	
	public void qusAnsUpdate(String qins,String ains,String question,String content, String date) {
		//String qusuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='问题反馈表单'", "UUID");
		Long qusdemid = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='问题反馈表单'", "ID");
		//String ansuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='问题回复表'", "UUID");
		Long ansdemid = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='问题回复表'", "ID");
		String qusnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String ansnow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		HashMap qusmap = DemAPI.getInstance().getFromData(Long.parseLong(qins));
		qusmap.put("QUESTION",question);
		qusmap.put("CREATEDATE",date);
		DemAPI.getInstance().updateFormData(qusdemid, Long.parseLong(qins), qusmap, Long.parseLong(qusmap.get("ID").toString()), false);//updateFormData(qusuuid, Long.parseLong(qins), qusmap, Long.parseLong(qusmap.get("ID").toString()), false);
		
		HashMap ansmap = DemAPI.getInstance().getFromData(Long.parseLong(ains));
		ansmap.put("CONTENT",content);
		ansmap.put("CREATEDATE",date);
		DemAPI.getInstance().updateFormData(ansdemid, Long.parseLong(ains), ansmap, Long.parseLong(ansmap.get("ID").toString()), false);//(ansuuid, Long.parseLong(ains), ansmap, Long.parseLong(ansmap.get("ID").toString()), false);
	}
	
	public void qusAnsSave(String projectno, String question, String content, String date) {
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
		String userid = user.getUserid();
		String username = user.getUsername();
		//String qusnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String ansnow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		//String qusuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='问题反馈表单'", "UUID");
		Long qusdemid = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='问题反馈表单'", "ID");
		//String ansuuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='问题回复表'", "UUID");
		Long ansdemid = DBUtil.getLong("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE='问题回复表'", "ID");
		Map params = new HashMap();
		params.put(1,projectno);
		String projectname = DBUTilNew.getDataStr("PROJECTNAME","SELECT PROJECTNAME FROM BD_ZQB_PJ_BASE WHERE PROJECTNO= ? ", params);
		
		Long qusins = DemAPI.getInstance().newInstance(qusdemid, userid);
		HashMap qusmap = new HashMap();
		qusmap.put("XMBH",projectno);
		qusmap.put("XMMC",projectname);
		qusmap.put("USERID",userid);
		qusmap.put("USERNAME",username);
		qusmap.put("QUESTION",question);
		qusmap.put("CREATEDATE",date);
		DemAPI.getInstance().saveFormData(qusdemid, qusins, qusmap, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
		
		Long ansins = DemAPI.getInstance().newInstance(ansdemid, userid);
		String questionno = DemAPI.getInstance().getFromData(qusins).get("ID").toString();
		HashMap ansmap = new HashMap();
		ansmap.put("PROJECTNO",projectno);
		ansmap.put("CONTENT",content);
		ansmap.put("CREATEDATE",ansnow);
		ansmap.put("USERID",userid);
		ansmap.put("USERNAME",username);
		ansmap.put("QUESTIONNO",questionno);
		DemAPI.getInstance().saveFormData(ansdemid, ansins, ansmap, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
	}
	
}
