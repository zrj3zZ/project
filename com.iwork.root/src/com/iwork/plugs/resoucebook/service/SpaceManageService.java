package com.iwork.plugs.resoucebook.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.iwork.core.security.SecurityUtil;
import com.iwork.plugs.resoucebook.dao.SpaceManageDao;
import com.iwork.plugs.resoucebook.model.DateModel;
import com.iwork.plugs.resoucebook.model.IworkRmBase;
import com.iwork.plugs.resoucebook.model.IworkRmSpace;
import com.iwork.plugs.resoucebook.model.IworkRmWeb;
import com.iwork.plugs.resoucebook.util.RMConst;
import org.apache.log4j.Logger;
public class SpaceManageService {
private SpaceManageDao spaceManageDao;
private static Logger logger = Logger.getLogger(SpaceManageService.class);

RMConst rmconst=new RMConst();

/**
 * 查询资源空间列表
 * @return
 */
public String getSpaceJson(String userid){
	StringBuffer jsonHtml = new StringBuffer();
	List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
	try{
		List list = null;
		if(SecurityUtil.isSuperManager()){
			list = spaceManageDao.getSpaceList("");
		}else{
			list = spaceManageDao.getSpaceList(userid);
		}
		for(int i = 0;i<list.size();i++){
			IworkRmSpace space= (IworkRmSpace)list.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				long cid=space.getId();
				String spacename =space.getSpacename()==null?"":space.getSpacename();
				long type=space.getType();
				String typeshow=rmconst.spacetype.get(String.valueOf(type)).toString();
				long cycle=space.getCycle();
				String cycleshow=rmconst.spacecycle.get(String.valueOf(cycle)).toString();
				String manager=space.getManager();
				long status=space.getStatus();
				String statusshow=rmconst.spacestatus.get(String.valueOf(status)).toString();
				String memo=space.getMemo()==null?"":space.getMemo();
				String operate="<a  href='###' onclick=\"contentf('" + cid
				+ "','"+spacename+"')\">内容管理</a>  <a  href='###' onclick=\"basef('" + cid
			    + "','"+spacename+"')\">基础信息</a>" ;
				item.put("id", cid);
				item.put("spacename",spacename );		
				item.put("type",typeshow);
				item.put("cycle", cycleshow);
				item.put("manager",manager);
				item.put("status",statusshow);
				item.put("memo", memo);
				item.put("operate",operate);
				items.add(item);			
		}
	}catch(Exception e){
		logger.error(e,e);
	}		
	JSONArray json = JSONArray.fromObject(items);
	jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
	return jsonHtml.toString();
}
/**
 * 空间名称是否重复
 * @return
 */
public String checkSpace(String space){	
	return spaceManageDao.checkSpace(space);
}
/**
 * 新增空间，保存到数据库
 * @param rms
 */
public void addSpace(IworkRmSpace rms){
	if(rms!=null){
		if(rms.getId()==null){
			spaceManageDao.addSpace(rms);
		}else{
			spaceManageDao.updateSpace(rms);
		}
	}
	
}
/**
 * 删除空间
 * @param rms
 */
public void removeSpace(long id){
	IworkRmSpace model=spaceManageDao.getSpaceModel(id);
	if(model!=null)
	spaceManageDao.removeSpace(model);
}
/**
 * 空间名称是否重复(修改空间)
 * @return
 */
public String checkSpace2(long id,String space){	
	return spaceManageDao.checkSpace2(id,space);
}
/**
 * 修改空间时根据id得到model
 * @param rms
 */
public IworkRmSpace getSpacem(long id){
	IworkRmSpace model=spaceManageDao.getSpaceModel(id);
	return model;
}
/**
 *修改空间
 * @param rms
 */
public void editSpace(IworkRmSpace model){
	spaceManageDao.updateSpace(model);
}

/**
 * 查询资源空间中内容管理列表
 * @return
 */
public String getContentJson(String id){
	long idl=Long.parseLong(id);
	StringBuffer jsonHtml = new StringBuffer();
	List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
	try{
		List list = spaceManageDao.getContentList(idl);
		for(int i = 0;i<list.size();i++){
			IworkRmWeb content= (IworkRmWeb)list.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				long cid=content.getId();
				long spaceid=content.getSpaceid();
				String spacename=content.getSpacename();
				String resouceid=content.getResouceid();
				String resoucename=content.getResouce()==null?"":content.getResouce();
				String userid=content.getUserid()==null?"":content.getUserid();
				String username=content.getUsername()==null?"":content.getUsername();
				Date begin=content.getBegintime();
				Date end=content.getEndtime();
				long status=content.getStatus();
				String statusshow=rmconst.spacestatus2.get(String.valueOf(status)).toString();
				String memo=content.getMemo()==null?"":content.getMemo();
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String beginshow=df.format(begin);
				String endshow=df.format(end);
				if(!beginshow.equals("")){
					beginshow=beginshow.substring(0,16);
				}
				if(!endshow.equals("")){
					endshow=endshow.substring(0,16);
				}
				item.put("id", cid);
				item.put("spaceid",spaceid );	
				item.put("spacename",spacename );		
				item.put("resouceid",resouceid);
				item.put("resoucename", resoucename);
				item.put("userid",userid);
				item.put("username",username);
				item.put("begintime",beginshow);
				item.put("endtime",endshow);
				item.put("status",statusshow);
				item.put("memo", memo);
				items.add(item);			
		}
	}catch(Exception e){
		logger.error(e,e);
	}		
	JSONArray json = JSONArray.fromObject(items);
	jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
	return jsonHtml.toString();
}
/**
 * 修改空间记录时根据id得到model
 * @param rms
 */
public IworkRmWeb getContentm(Long id){
	IworkRmWeb model=spaceManageDao.getContentModel(id);
	return model;
}
/**
 *修改空间记录
 * @param rms
 */
public void editContent(IworkRmWeb model){
	spaceManageDao.updateContent(model);
}
/**
 * 删除记录
 * @param rms
 */
public void removeContents(String id){
	long idl=Long.parseLong(id);
	IworkRmWeb model=spaceManageDao.getContentModel(idl);
	if(model!=null){
	spaceManageDao.removeContents(model);
	}
}
/**
 * 查询资源空间中基础信息列表
 * @return
 */
public String getBaseJson(String id){
	long idl=Long.parseLong(id);
	StringBuffer jsonHtml = new StringBuffer();
	List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
	try{
		List list = spaceManageDao.getBaseList(idl);
		for(int i = 0;i<list.size();i++){
			IworkRmBase base= (IworkRmBase)list.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				long cid=base.getId();
				long spaceid=base.getSpaceid();
				String spacename=base.getSpacename();
				String resouceid=base.getResouceid();
				String resoucename=base.getResoucename()==null?"":base.getResoucename();
				String pictureurl=base.getPicture()==null?"":base.getPicture();
				String picture="<img src="+pictureurl+"  width=16 height=16 onmouseout=\"hiddenPic();\"  onMouseOver=\"showPic('"+pictureurl+"');\" /> ";
				String para1=base.getParameter1()==null?"":base.getParameter1();
				String para2=base.getParameter2()==null?"":base.getParameter2();
				String para3=base.getParameter3()==null?"":base.getParameter3();
				String para4=base.getParameter4()==null?"":base.getParameter4();
				String para5=base.getParameter5()==null?"":base.getParameter5();
				long status=base.getStatus();
				String statusshow=rmconst.spacestatus2.get(String.valueOf(status)).toString();
				String memo=base.getMemo()==null?"":base.getMemo();
				
				item.put("id", cid);
				item.put("spaceid",spaceid );	
				item.put("spacename",spacename );		
				item.put("resouceid",resouceid);
				item.put("resoucename", resoucename);
				item.put("picture",picture);
				item.put("para1",para1);
				item.put("para2",para2);
				item.put("para3",para3);
				item.put("para4",para4);
				item.put("para5",para5);
				item.put("status",statusshow);
				item.put("memo", memo);
				items.add(item);			
		}
	}catch(Exception e){
		logger.error(e,e);
	}		
	JSONArray json = JSONArray.fromObject(items);
	jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
	return jsonHtml.toString();
}
/**
 * 同一空间中资源编号是否重复
 * @return
 */
public String checkBase(String spaceid,String resouceid){
	long spaceidl=Long.parseLong(spaceid);
	return spaceManageDao.checkBase(spaceidl,resouceid);
}
/**
 * 新增基础信息，保存到数据库
 * @param rms
 */
public void addBase(IworkRmBase rmb){
	spaceManageDao.addBase(rmb);
}
/**
 * 删除基础信息
 * @param rms
 */
public void removeBases(long id){
	IworkRmBase model=spaceManageDao.getBaseModel(id);
	if(model!=null)
	spaceManageDao.removeBases(model);
	//spaceManageDao.removeBase(id);
}
/**
 * 同一空间中资源编号是否重复（修改基础信息）
 * @return
 */
public String checkBase2(long id,String spaceid,String resouceid){
	long spaceidl=Long.parseLong(spaceid);
	return spaceManageDao.checkBase2(id,spaceidl,resouceid);
}
/**
* 修改基础信息时根据id得到model
* @param rms
*/
public IworkRmBase getBasem(long id){	
	IworkRmBase model=spaceManageDao.getBaseModel(id);
	return model;
}
/**
 *修改基础信息保存
 * @param rms
 */
public void editBase(IworkRmBase model){
	spaceManageDao.updateBase(model);
}


/**
 * 获得当前日期开始之后的7天
 * @return
 */
private ArrayList getAfterWeekList(){
	int LENGHT = spaceManageDao.getLength(1L);

	ArrayList list = new ArrayList();
	Calendar c = Calendar.getInstance();
	SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
	String[] oneWeekDay={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	for(int i=0;i<LENGHT;i++){
		DateModel  dm = new DateModel();
		dm.setDate(sdf.format(c.getTime()));
		int week = c.get(Calendar.DAY_OF_WEEK)-1;
		dm.setWeek(oneWeekDay[week]);
		list.add(dm);
		c.add(Calendar.DATE, 1);
		
	}
	
	return list;
}
/**
 * @preserve 声明此方法不被JOC混淆
 * @param model
 * @return
 */
public String getPopupInfo(List list) {
	if (list != null) {
		
			StringBuffer sb = new StringBuffer();
//			sb.append("<table border=0>");
//			sb.append("<tr><td valign=top><img src=app/plugs/resoucebook/images/car_icon.gif><b class=font>当前车辆预定状态</b></td>");
//			sb.append("<td valign=top>");
//			
//			
//			sb.append("</td></tr>");
//			sb.append("<tr><td colspan=2>");
			
			for(int i=0;i<list.size();i++){
				IworkRmWeb model = (IworkRmWeb)list.get(i);
				String begin=model.getBegintime().toString().substring(0,16);
				String end=model.getEndtime().toString().substring(0,16);
//				sb.append("<li><div class=STYLE1><font color=#000000><b>").append(model.getUserid()).append("/").append(model.getUsername()).append("</b>&nbsp;").append("预定时间&nbsp;").append(begin).append("到").append(end).append("</font></div></li>");
				sb.append("·").append(model.getUserid()).append("/").append(model.getUsername()).append("\n").append("预定时间&nbsp;").append(begin).append("到").append(end).append("");
				sb.append("\n");
			}
//			sb.append("</td></tr>");
//			sb.append("</table>");
			return sb.toString();
		}
	return "";
}
public SpaceManageDao getSpaceManageDao() {
	return spaceManageDao;
}
public void setSpaceManageDao(SpaceManageDao spaceManageDao) {
	this.spaceManageDao = spaceManageDao;
}

/**
 * 车辆预定，保存到数据库
 * @param rms
 */
public void addWeb(IworkRmWeb rmb){
	spaceManageDao.addWeb(rmb);
}

public String checkBase(long spaceid, String resouceid) {
	return spaceManageDao.checkBase(spaceid, resouceid);
}
public String checkBase2(long id, long spaceid, String resouceid) {
	return spaceManageDao.checkBase2(id, spaceid, resouceid);
}
public List getBaseList(long id) {
	return spaceManageDao.getBaseList(id);
}
public IworkRmBase getBaseModel(long id) {
	return spaceManageDao.getBaseModel(id);
}

public List getContentList(long id) {
	return spaceManageDao.getContentList(id);
}
public IworkRmWeb getContentModel(long id) {
	return spaceManageDao.getContentModel(id);
}

public List getSpaceList(String userid) {
	return spaceManageDao.getSpaceList(userid);
}
public IworkRmSpace getSpaceModel(long id) {
	return spaceManageDao.getSpaceModel(id);
}
public void removeBases(IworkRmBase rmb) {
	spaceManageDao.removeBases(rmb);
}
public void removeContents(IworkRmWeb rms) {
	spaceManageDao.removeContents(rms);
}
public void removeSpace(IworkRmSpace rms) {
	spaceManageDao.removeSpace(rms);
}
public void updateBase(IworkRmBase rmw) {
	spaceManageDao.updateBase(rmw);
}
public void updateContent(IworkRmWeb rmw) {
	spaceManageDao.updateContent(rmw);
}
public void updateSpace(IworkRmSpace rms) {
	spaceManageDao.updateSpace(rms);
}
public final HibernateTemplate getHibernateTemplate() {
	return spaceManageDao.getHibernateTemplate();
}
public final SessionFactory getSessionFactory() {
	return spaceManageDao.getSessionFactory();
}
public final void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
	spaceManageDao.setHibernateTemplate(hibernateTemplate);
}
public final void setSessionFactory(SessionFactory sessionFactory) {
	spaceManageDao.setSessionFactory(sessionFactory);
}
public final void afterPropertiesSet() throws IllegalArgumentException,
		BeanInitializationException {
	spaceManageDao.afterPropertiesSet();
}
public boolean equals(Object obj) {
	return spaceManageDao.equals(obj);
}
public int hashCode() {
	return spaceManageDao.hashCode();
}
public String toString() {
	return spaceManageDao.toString();
}
/**
 * 查询资源空间中基础信息列表
 * @return
 */
public String getWebJson(String manager,String spaceid,String spacename,String resouceid,String resoucename,String userid,String username,String date,String status){
	long spaceidl=0l;
	long statusl=0l;
	if(!spaceid.equals("")){
	 spaceidl=Long.parseLong(spaceid);
	}
	if(!status.equals("")){
	 statusl=Long.parseLong(status);
	}
	StringBuffer jsonHtml = new StringBuffer();
	List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
	try{
		List list = spaceManageDao.getWebList(manager,spaceidl,spacename,resouceid,resoucename,userid,username,date,statusl);
		for(int i = 0;i<list.size();i++){
			IworkRmWeb web= (IworkRmWeb)list.get(i);	
				Map<String,Object> item = new HashMap<String,Object>();
				long spaceids=web.getSpaceid();
				String spacenames=web.getSpacename();
				String resouceids=web.getResouceid();
				String resoucenames=web.getResouce()==null?"":web.getResouce();
				String userids=web.getUserid()==null?"":web.getUserid();
				String usernames=web.getUsername()==null?"":web.getUsername();
				String begintime=web.getBegintime().toString().substring(0,16);
				String endtime=web.getEndtime().toString().substring(0,16);
				long statuss=web.getStatus();
				String statusshow=rmconst.spacestatus2.get(String.valueOf(statuss)).toString();
				String memo=web.getMemo()==null?"":web.getMemo();
				
				item.put("SPACEID", spaceids);
				item.put("SPACENAME",spacenames );	
				item.put("RESOUCEID",resouceids );		
				item.put("RESOUCENAME",resoucenames);
				item.put("USERID", userids);
				item.put("USERNAME",usernames);
				item.put("BEGINTIME",begintime);
				item.put("ENDTIME",endtime);
				item.put("STATUS",statusshow);
				item.put("MEMO", memo);
				items.add(item);			
		}
	}catch(Exception e){
		logger.error(e,e);
	}		
	JSONArray json = JSONArray.fromObject(items);
	jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
	return jsonHtml.toString();
}
public long getSpaceid(String resouceid){	
	return spaceManageDao.getSpaceid(resouceid);
}
public String getSpacename(String resouceid){	
	return spaceManageDao.getSpacename(resouceid);
}
public String getSpaceName(long spaceid){	
	return spaceManageDao.getSpaceName(spaceid);
}
public String getSpaceMemo(long spaceid){	
	return spaceManageDao.getSpaceMemo(spaceid);
}
}
