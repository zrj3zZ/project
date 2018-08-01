package com.iwork.process.tools.processopinion.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.process.tools.processopinion.model.ProcessRuOpinion;
import com.iwork.process.tools.processopinion.model.ProcessToolsOpinion;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.ProcessAPI;
/**
 * 
 * @author NEEQMANAGER  只给华龙升级
 *
 */
public class ProcessOpinionDAO extends HibernateDaoSupport
{
  public List<ProcessRuOpinion> getOpinionList(String userid, String actDefId, String actStepId, Long instanceid, String taskid, Long excutionid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
	 if( !DBUTilNew.validActStepId(actStepId) ){
		 return null;
	 }
    Session session = getSession();
    StringBuffer hql = new StringBuffer();
    hql.append("from ProcessRuOpinion where createuser='").append(userid).append("' and actDefId='").append(actDefId).append("' ");
    hql.append(" and actStepId='").append(actStepId).append("' and instanceid=").append(instanceid).append(" and taskid=").append(taskid).append(" and executionid=").append(excutionid);
    Query query = session.createQuery(hql.toString());
    List list = query.list();
    return list;
  }

  public List<ProcessRuOpinion> getProcessInstanceOpinionList(String actDefId, Long prcDefId, long instanceid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
		
	String hql = ""; 
	boolean flag = false;
	String khfzr = "";
	String qcr = "";
	
	if(actDefId.startsWith("GGSPLC")||actDefId.startsWith("CXDDYFQLC")||actDefId.startsWith("RCYWCB")){
		HashMap processDate = ProcessAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		if(processDate!=null){
			HashMap conditionMap = new HashMap();
			String khbh = processDate.get("KHBH")==null?"":processDate.get("KHBH").toString();
			conditionMap.put("KHBH", khbh);
			List<HashMap> dataList = DemAPI.getInstance().getAllList("84ff70949eac4051806dc02cf4837bd9", conditionMap, "ID");
			if(dataList.size()==1){
				HashMap data = dataList.get(0);
				khfzr = data.get("KHFZR")==null||data.get("KHFZR").equals("")?"":data.get("KHFZR").toString().substring(0, data.get("KHFZR").toString().indexOf("["));
				Map params = new HashMap();
				params.put(1,khbh);
				String cuid = UserContextUtil.getInstance().getCurrentUserContext()._userModel.getUserid();
				qcr = DBUtil.getDataStr("USERID", "SELECT USERID FROM ORGUSER WHERE EXTEND1=?", params);
				flag=qcr.equals(cuid);
			}
		}
	}
	List list = new ArrayList();
	Map<String,String> config=ConfigUtil.readAllProperties("/common.properties");
	if(flag&&config.get("zqServer")!=null&&config.get("zqServer").equals("hlzq")){
		hql = "from ProcessRuOpinion where  (actDefId= ?  and instanceid=" + instanceid + " and (createuser= ?  or (createuser= ? and action='驳回'))) or (actDefId= ?  and instanceid=" + instanceid + " and action='归档') order by id asc";
		Object[] values = {actDefId,qcr,khfzr,actDefId};
		list = getHibernateTemplate().find(hql,values);
	}else{
		hql = "from ProcessRuOpinion where  actDefId= ? and instanceid=" + instanceid + " order by id asc";
		Object[] values = {actDefId};
		list = getHibernateTemplate().find(hql,values);
	}

    return list;
  }

  public List<ProcessToolsOpinion> loadUserDefinedOpinions()
  {
    List opinions = new ArrayList();
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    opinions = getUserDatas(userid, Long.valueOf(1L));
    return opinions;
  }

  public void loadDefaultOpinions()
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    List nullitems = getUserDatas("null", Long.valueOf(0L));
    for (int i = 0; i < nullitems.size(); i++) {
      ProcessToolsOpinion nullitem = (ProcessToolsOpinion)nullitems.get(i);
      if (nullitem != null)
        addRowData(new ProcessToolsOpinion(userid, Long.valueOf(1L), nullitem.getContent(), nullitem.getOrderindex()));
    }
  }

  public String IsOpinExist(String opin)
  {
    String str = "1";
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    List useritems = getUserDatas(userid, Long.valueOf(1L));
    if (useritems.size() != 0) {
      for (int i = 0; i < useritems.size(); i++) {
        ProcessToolsOpinion useritem = (ProcessToolsOpinion)useritems.get(i);
        if ((useritem != null) && 
          (opin.equals(useritem.getContent()))) {
          str = "0";
          break;
        }
      }
    }

    return str;
  }

  public void exchangeOrderIndex(long index1, long index2)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    List useritems = getUserDatas(userid, Long.valueOf(1L));
    ProcessToolsOpinion model1 = (ProcessToolsOpinion)useritems.get((int)index1);
    ProcessToolsOpinion model2 = (ProcessToolsOpinion)useritems.get((int)index2);
    Long temp = model1.getOrderindex();
    model1.setOrderindex(model2.getOrderindex());
    model2.setOrderindex(temp);
    updateRowData(model1);
    updateRowData(model2);
  }

  public void MoveTop(long index1, long index2)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    List useritems = getUserDatas(userid, Long.valueOf(1L));
    Long temp = ((ProcessToolsOpinion)useritems.get((int)index2)).getOrderindex();
    for (int i = 0; i < useritems.size(); i++)
      if (useritems.get(i) != null)
        if (i == (int)index1) {
          ProcessToolsOpinion model1 = (ProcessToolsOpinion)useritems.get((int)index1);
          model1.setOrderindex(temp);
          updateRowData(model1);
        } else if (i < (int)index1) {
          ProcessToolsOpinion model2 = (ProcessToolsOpinion)useritems.get(i);
          model2.setOrderindex(Long.valueOf(model2.getOrderindex().longValue() + 1L));
          updateRowData(model2);
        }
  }

  public void MoveLow(long index1, long index2)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    List useritems = getUserDatas(userid, Long.valueOf(1L));
    Long temp = ((ProcessToolsOpinion)useritems.get((int)index2)).getOrderindex();
    for (int i = 0; i < useritems.size(); i++)
      if (useritems.get(i) != null)
        if (i == (int)index1) {
          ProcessToolsOpinion model1 = (ProcessToolsOpinion)useritems.get((int)index1);
          model1.setOrderindex(temp);
          updateRowData(model1);
        } else if (i > (int)index1) {
          ProcessToolsOpinion model2 = (ProcessToolsOpinion)useritems.get(i);
          model2.setOrderindex(Long.valueOf(model2.getOrderindex().longValue() - 1L));
          updateRowData(model2);
        }
  }

  public List<ProcessToolsOpinion> getUserDatas(String userid, Long type)
  {
    String sql = "from ProcessToolsOpinion where userid='" + userid + "' and type=" + type + " order by orderindex";
    List items = getHibernateTemplate().find(sql);
    return items;
  }

  public ProcessToolsOpinion getRowData(Long id)
  {
    ProcessToolsOpinion model = (ProcessToolsOpinion)getHibernateTemplate().get(ProcessToolsOpinion.class, id);
    return model;
  }

  public void updateRowData(ProcessToolsOpinion model)
  {
    getHibernateTemplate().update(model);
  }

  public void updateRowData(ProcessRuOpinion model)
  {
    getHibernateTemplate().update(model);
  }

  public Long getMaxID()
  {
    Long maxid = new Long(0L);
    String sql = "SELECT MAX(id)+1 FROM ProcessRuOpinion";
    String noStr = null;
    List ll = getHibernateTemplate().find(sql);
    Iterator itr = ll.iterator();
    if (itr.hasNext()) {
      Object noint = itr.next();
      if (noint == null)
        maxid = new Long(1L);
      else {
        maxid = Long.valueOf(Long.parseLong(noint.toString()));
      }
    }
    return maxid;
  }

  public void addRowData(ProcessToolsOpinion model)
  {
    if ((model != null) && (model.getId() == null)) {
      model.setId(getMaxID());
    }
    getHibernateTemplate().save(model);
  }

  public void addRowData(ProcessRuOpinion model)
  {
    if ((model != null) && (model.getId() == null)) {
      model.setId(getMaxID());
    }
    getHibernateTemplate().save(model);
  }

  public void deleteRowData(ProcessToolsOpinion model)
  {
    getHibernateTemplate().delete(model);
  }

  public String getUserOpinion(String userid, String actDefId, long prcDefId, String actStepId, long instanceid, String taskid, long excutionid)
  {
    ProcessRuOpinion model = getProData(userid, actDefId, Long.valueOf(prcDefId), actStepId, Long.valueOf(instanceid), taskid, Long.valueOf(excutionid));
    String userOpin = "";
    if (model != null) {
      userOpin = model.getContent();
    }
    return userOpin;
  }

  public ProcessRuOpinion getUserOpinionModel(String userid, String actDefId, long prcDefId, String actStepId, long instanceid, String taskid, long excutionid)
  {
    ProcessRuOpinion model = getProData(userid, actDefId, Long.valueOf(prcDefId), actStepId, Long.valueOf(instanceid), taskid, Long.valueOf(excutionid));
    return model;
  }

  public ProcessRuOpinion getProData(String userid, String actDefId, Long prcDefId, String actStepId, Long instanceid, String taskid, Long excutionid)
  {
	  String match="\\w{3,20}:\\d{1,5}:\\d{5,20}";
	  boolean matches=actDefId.matches(match);
	  if(!matches){
			return null;
	  }
		 if( !DBUTilNew.validActStepId(actStepId) ){
			 return null;
		 }
    Session session = getSession();
    StringBuffer hql = new StringBuffer();
    hql.append("from ProcessRuOpinion where createuser='").append(userid).append("' and actDefId='").append(actDefId).append("' ");
    hql.append(" and actStepId='").append(actStepId).append("' and instanceid=").append(instanceid).append(" and taskid=").append(taskid).append(" and executionid=").append(excutionid);
    Query query = session.createQuery(hql.toString());
    List list = query.list();
    ProcessRuOpinion model = null;
    if ((list.size() != 0) && 
      (list.get(0) != null)) {
      model = (ProcessRuOpinion)list.get(0);
    }

    session.close();
    return model;
  }

  public ProcessToolsOpinion addOpinion(String addopin)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    Session session = getSession();
    String hql = "select max(orderindex) from ProcessToolsOpinion where userid='" + userid + "'";
    Query query = session.createQuery(hql);
    List list = query.list();
    long maxOrderIndex = 0L;
    if ((list.size() != 0) && 
      (list.get(0) != null)) {
      maxOrderIndex = ((Long)list.get(0)).longValue();
    }

    session.close();
    ProcessToolsOpinion new_model = new ProcessToolsOpinion(userid, Long.valueOf(1L), addopin, Long.valueOf(maxOrderIndex + 1L));
    addRowData(new_model);
    return new_model;
  }

  public void modOpinion(int modindex, String newopin)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    List useritems = getUserDatas(userid, Long.valueOf(1L));
    ProcessToolsOpinion mod_model = (ProcessToolsOpinion)useritems.get(modindex);
    mod_model.setContent(newopin);
    updateRowData(mod_model);
  }

  public void delOpinion(Long delindex)
  {
    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
    String userid = uc.get_userModel().getUserid();
    ProcessToolsOpinion model = getRowData(delindex);
    if ((model != null) && 
      (model.getUserid().equals(userid)))
      deleteRowData(model);
  }
}