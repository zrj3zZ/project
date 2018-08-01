package com.iwork.core.engine.iform.service;

import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormMapDAO;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataMapDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.engine.plugs.component.ConfigComponentModel;
import com.iwork.core.engine.plugs.component.IFormUIFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SysEngineIFormMapService
{
  private static final Log log = LogFactory.getLog(SysEngineIFormMapService.class);
  private SysEngineIFormDAO sysEngineIFormDAO;
  private SysEngineIFormMapDAO sysEngineIFormMapDAO;
  private SysEngineMetadataMapDAO sysEngineMetadataMapDAO;
  private SysEngineMetadataDAO sysEngineMetadataDAO;

  public String getTreeJson(Long formid)
  {
    StringBuffer jsonHtml = new StringBuffer();
    JSONArray json = null;
    List root = new ArrayList();
    List items = new ArrayList();
    SysEngineIform model = this.sysEngineIFormDAO.getSysEngineIformModel(formid);

    List list = this.sysEngineIFormMapDAO.getDataList(formid);
    for (int index = 0; index < list.size(); index++) {
      SysEngineIformMap sysEngineIformMap = (SysEngineIformMap)list.get(index);
      Map item = new HashMap();
      item.put("id", sysEngineIformMap.getId());

      item.put("name", sysEngineIformMap.getFieldTitle());
      item.put("fieldName", sysEngineIformMap.getFieldName());
      item.put("formId", sysEngineIformMap.getId());
      item.put("title", sysEngineIformMap.getFieldTitle());
      item.put("iconOpen", "iwork_img/accept.png");
      item.put("iconClose", "iwork_img/accept.png");
      item.put("icon", "iwork_img/accept.png");
      item.put("type", "formField");
      items.add(item);
    }

    Map node = new HashMap();
    node.put("id", Integer.valueOf(999999));
    node.put("name", model.getIformTitle());
    node.put("iconOpen", "iwork_img/page_add.png");
    node.put("iconClose", "iwork_img/page_add.png");
    node.put("open", Boolean.valueOf(true));
    node.put("children", items);
    node.put("master", "");
    node.put("type", "group");
    root.add(node);
    json = JSONArray.fromObject(root);
    jsonHtml.append(json);
    return jsonHtml.toString();
  }

  public List<SysEngineIformMap> getMapList(Long formid)
  {
    List list = this.sysEngineIFormMapDAO.getDataList(formid);
    return list;
  }

  public String getDataList(Long formid)
  {
    StringBuffer griddata = new StringBuffer();

    SysEngineIform iformModel = this.sysEngineIFormDAO.getSysEngineIformModel(formid);
    if (iformModel != null) {
      initFormMapList(iformModel.getMetadataid(), formid);

      autoDeleteNonExistedIformMap(formid);

      if ("2".equals(iformModel.getMetadataType())) {
        SysEngineIformMap instanceid_iformMapModel = this.sysEngineIFormMapDAO.getModel(formid, "INSTANCEID");
        if ((instanceid_iformMapModel != null) && (new Long(1L).equals(instanceid_iformMapModel.getFieldNotnull()))) {
          instanceid_iformMapModel.setFieldNotnull(new Long(0L));
          this.sysEngineIFormMapDAO.update(instanceid_iformMapModel);
        }
        SysEngineIformMap companyno_iformMapModel = this.sysEngineIFormMapDAO.getModel(formid, "COMPANYNO");
        if ((companyno_iformMapModel != null) && (new Long(1L).equals(companyno_iformMapModel.getFieldNotnull()))) {
          companyno_iformMapModel.setFieldNotnull(new Long(0L));
          this.sysEngineIFormMapDAO.update(companyno_iformMapModel);
        }
      }
    }
    List maplist = this.sysEngineIFormMapDAO.getDataList(formid);
    List rows = new ArrayList();
    Map total = new HashMap();
    total.put("total", "28");
    Long upid = new Long(0L);
    long downid = new Long(0L).longValue();
    for (int i = 0; i < maplist.size(); i++) {
      SysEngineIformMap model = (SysEngineIformMap)maplist.get(i);
      Map item = new HashMap();
      item.put("ID", model.getId());
      item.put("FIELD_NAME", model.getFieldName());
      item.put("FIELD_TITLE", model.getFieldTitle());
      ConfigComponentModel ccModel = IFormUIFactory.getUIComponentModel(model.getDisplayType());
      if (ccModel != null)
        item.put("DISPLAY_TYPE", ccModel.getTitle());
      else
        item.put("DISPLAY_TYPE", "");
      item.put("DISPLAY_WIDTH", model.getDisplaywidth());
      item.put("INPUT_WIDTH", model.getInputWidth());
      item.put("INPUT_HEIGHT", model.getInputHeight());
      if ((model.getMapType() != null) && (model.getMapType().equals(new Long(1L))))
        item.put("MAP_TYPE", "实体域<img src='iwork_img/icon/database.png'>");
      else {
        item.put("MAP_TYPE", "虚拟域<img src='iwork_img/engine/component/hidden.png'>");
      }

      if (model.getFieldNotnull() != null) {
        if (model.getFieldNotnull().longValue() == 0L)
          item.put("FIELD_NOTNULL", "是");
        else {
          item.put("FIELD_NOTNULL", "否");
        }
      }

      StringBuffer toolbar = new StringBuffer();
      toolbar.append("<a href='javascript:openMapModify(").append(model.getId()).append(",").append(iformModel.getMetadataType()).append(")'><img  title='设置外观属性'  src='iwork_img/icon/confg_16.gif' border='0'/>设置</a>");
      item.put("DO", toolbar.toString());
      StringBuffer orderby = new StringBuffer();

      upid = model.getId();
      downid = model.getId().longValue();
      rows.add(item);
    }

    total.put("rows", rows);
    JSONArray json = JSONArray.fromObject(total);
    griddata.append(json);
    return griddata.toString();
  }

  public void initFormMapList(Long metadataid, Long formId)
  {
    List list = this.sysEngineMetadataMapDAO.getDataList(metadataid);
    for (int index = 0; index < list.size(); index++) {
      SysEngineMetadataMap semm = (SysEngineMetadataMap)list.get(index);

      boolean flag = this.sysEngineIFormMapDAO.isHaving(formId, semm.getFieldname());
      if (!flag)
      {
        SysEngineIformMap formMap = new SysEngineIformMap();
        formMap.setFieldName(semm.getFieldname());
        formMap.setFieldTitle(semm.getFieldtitle());
        if ((semm.getDisplaytype() == null) || ("".equals(semm.getDisplaytype()))) {
          if ("日期".equals(semm.getFieldtype()))
            semm.setDisplaytype("TxtBox_dateISO");
          else if ("日期时间".equals(semm.getFieldtype()))
            semm.setDisplaytype("TxtBox_dateTime");
          else if ("文本".equals(semm.getFieldtype()))
            semm.setDisplaytype("TxtArea");
          else {
            semm.setDisplaytype("TxtBox");
          }
          formMap.setDisplayType(semm.getDisplaytype());
        } else {
          formMap.setDisplayType(semm.getDisplaytype());
        }
        formMap.setDisplaywidth(Long.valueOf(semm.getDisplaywidth() == null ? 80L : semm.getDisplaywidth().longValue()));
        formMap.setInputHeight(Long.valueOf(semm.getInputheight() == null ? 25L : semm.getInputheight().longValue()));
        formMap.setInputWidth(Long.valueOf(semm.getInputwidth() == null ? 100L : semm.getInputwidth().longValue()));
        formMap.setDisplayEnum(semm.getDisplayenum());
        formMap.setFieldNotnull(semm.getFieldnotnull());
        formMap.setIformId(formId);
        formMap.setParam1("");
        formMap.setParam2("");
        formMap.setParam3("");
        formMap.setParam4("");
        formMap.setParam5("");
        formMap.setHtmlInner("");
        formMap.setMapType(new Long(1L));
        this.sysEngineIFormMapDAO.save(formMap);
      }
    }
  }

  public String loadFormBaseInfo(Long id)
  {
    SysEngineIform sysEngineIform = this.sysEngineIFormDAO.getSysEngineIformModel(id);
    StringBuffer formdata = new StringBuffer();
    Map item = new HashMap();
    item.put("sysEngineIform.id", sysEngineIform.getId());
    item.put("sysEngineIform.metadataid", sysEngineIform.getMetadataid());
    item.put("sysEngineIform.iformTitle", sysEngineIform.getIformTitle());
    item.put("sysEngineIform.metadataType", sysEngineIform.getMetadataType());
    item.put("sysEngineIform.visitType", sysEngineIform.getVisitType());
    if (sysEngineIform.getMaster() == null) sysEngineIform.setMaster("");
    item.put("sysEngineIform.master", sysEngineIform.getMaster());
    item.put("sysEngineIform.iformTemplate", sysEngineIform.getIformTemplate());
    if (sysEngineIform.getMemo() == null) sysEngineIform.setMemo("");
    item.put("sysEngineIform.memo", sysEngineIform.getMemo());
    item.put("sysEngineIform.groupid", sysEngineIform.getGroupid());
    JSONArray json = JSONArray.fromObject(item);
    formdata.append(json);
    return formdata.toString();
  }

  public String getMaxID()
  {
    return this.sysEngineIFormMapDAO.getMaxID();
  }

  public SysEngineIformMap getSysEngineIformMap(Long id)
  {
    return this.sysEngineIFormMapDAO.getModel(id);
  }

  public SysEngineIformMap getSysEngineIformMap(Long formid, String fieldName)
  {
    return this.sysEngineIFormMapDAO.getModel(formid, fieldName);
  }

  public void save(SysEngineIformMap temp)
  {
    SysEngineIformMap sysEngineIformMap = temp;

    if (temp.getFieldTitle() != null) sysEngineIformMap.setFieldTitle(temp.getFieldTitle());
    if (temp.getDisplayEnum() != null) sysEngineIformMap.setDisplayEnum(temp.getDisplayEnum());
    if (temp.getDisplayType() != null) sysEngineIformMap.setDisplayType(temp.getDisplayType());
    if (temp.getDisplaywidth() != null) sysEngineIformMap.setDisplaywidth(temp.getDisplaywidth());
    if (temp.getFieldDefault() != null) sysEngineIformMap.setFieldDefault(temp.getFieldDefault());
    if (temp.getFieldNotnull() != null) sysEngineIformMap.setFieldNotnull(temp.getFieldNotnull());
    if (temp.getHtmlInner() != null) sysEngineIformMap.setHtmlInner(temp.getHtmlInner());
    if (temp.getInputHeight() != null) sysEngineIformMap.setInputHeight(temp.getInputHeight());
    if (temp.getInputWidth() != null) sysEngineIformMap.setInputWidth(temp.getInputWidth());
    if (temp.getMapType() == null) sysEngineIformMap.setMapType(new Long(1L));

    this.sysEngineIFormMapDAO.save(sysEngineIformMap);
  }

 /* public String loadFormMapDisplayTypeJSON()
  {
    StringBuffer html = new StringBuffer();
    List items = new ArrayList();
    HashMap hash = IFormUIFactory.getComponentGroupList();
    if (hash != null) {
      Iterator iterator = hash.entrySet().iterator();
      while (iterator.hasNext()) {
        Object obj = iterator.next();
        if (obj != null) {
          Map.Entry entry = (Map.Entry)obj;
          Map item = new HashMap();

          String groupname = (String)entry.getValue();
          item.put("id", "group" + entry.getKey());
          item.put("name", groupname);
          item.put("open", Boolean.valueOf(true));
          item.put("type", "group");

          item.put("children", getDisplayTypeListByGroupName(groupname));
          items.add(item);
        }
      }
    }

    JSONArray json = JSONArray.fromObject(items);
    html.append(json);
    return html.toString();
  }*/
  public String loadFormMapDisplayTypeJSON()
  {
    StringBuffer html = new StringBuffer();
    List items = new ArrayList();
    HashMap hash = IFormUIFactory.getComponentGroupList();
    if (hash != null) {
      Iterator iterator = hash.entrySet().iterator();
      while (iterator.hasNext()) {
        Object obj = iterator.next();
        if (obj != null) {
          Map.Entry entry = (Map.Entry)obj;
          Map item = new HashMap();

          String groupname = (String)entry.getValue();
          item.put("id", "group" + entry.getKey());
          item.put("name", groupname);
          item.put("open", Boolean.valueOf(true));
          item.put("type", "group");
//          if(entry.getKey().equals(0)){
        	  items.addAll(getDisplayTypeListByGroupName(groupname));
//          }
        }
      }
    }
    JSONArray json = JSONArray.fromObject(items);
    html.append(json);
    return html.toString();
  }
  private List getDisplayTypeListByGroupName(String groupname)
  {
    StringBuffer jsonHtml = new StringBuffer();
    List<ConfigComponentModel> displayTypeList = IFormUIFactory.getComponentSortList();
    List subitems = new ArrayList();
    for (ConfigComponentModel model : displayTypeList)
    {
      if ((model.getGroupName() != null) && (model.getGroupName().equals(groupname))) {
        Map subitem = new HashMap();
        subitem.put("id", model.getKey());
        subitem.put("name", model.getTitle());
        subitem.put("groupname", model.getGroupName());
        if ((model.getIcon() != null) && (!"".equals(model.getIcon())))
          subitem.put("icon", model.getIcon());
        else {
          subitem.put("icon", "iwork_img/plugin_disabled.png");
        }
        subitem.put("desc", model.getDesc());
        subitem.put("setting", Boolean.valueOf(model.getSetting()));
        subitems.add(subitem);
      }
    }
//    JSONArray json = JSONArray.fromObject(subitems);
//    jsonHtml.append(json);
    return subitems;
  }

  public String loadMapInfo(Long formid)
  {
    SysEngineIformMap sysEngineIformMap = this.sysEngineIFormMapDAO.getModel(formid);
    StringBuffer formdata = new StringBuffer();
    Map item = new HashMap();
    item.put("iformStyleMap.id", sysEngineIformMap.getId());
    item.put("iformStyleMap.iformId", sysEngineIformMap.getIformId());
    item.put("iformStyleMap.fieldName", sysEngineIformMap.getFieldName());
    item.put("iformStyleMap.fieldTitle", sysEngineIformMap.getFieldTitle());
    if (sysEngineIformMap.getDisplaywidth() == null) sysEngineIformMap.setDisplaywidth(new Long(0L));
    item.put("iformStyleMap.displaywidth", sysEngineIformMap.getDisplaywidth());
    if (sysEngineIformMap.getDisplayType() == null) sysEngineIformMap.setDisplayType("");
    item.put("iformStyleMap.displayType", sysEngineIformMap.getDisplayType());
    if (sysEngineIformMap.getHtmlInner() == null) sysEngineIformMap.setHtmlInner("");
    item.put("iformStyleMap.htmlInner", sysEngineIformMap.getHtmlInner());
    if (sysEngineIformMap.getInputHeight() == null) sysEngineIformMap.setInputHeight(new Long(0L));
    item.put("iformStyleMap.inputHeight", sysEngineIformMap.getInputHeight());
    if (sysEngineIformMap.getInputWidth() == null) sysEngineIformMap.setInputWidth(new Long(0L));
    item.put("iformStyleMap.inputWidth", sysEngineIformMap.getInputWidth());
    if (sysEngineIformMap.getDisplayEnum() == null) sysEngineIformMap.setDisplayEnum("");
    item.put("iformStyleMap.displayEnum", sysEngineIformMap.getDisplayEnum());
    if (sysEngineIformMap.getFieldNotnull() == null) sysEngineIformMap.setFieldNotnull(new Long(0L));
    item.put("iformStyleMap.fieldNotnull", sysEngineIformMap.getFieldNotnull());

    if (sysEngineIformMap.getFieldDefault() == null) sysEngineIformMap.setFieldDefault("");
    item.put("iformStyleMap.fieldDefault", sysEngineIformMap.getFieldDefault());

    JSONArray json = JSONArray.fromObject(item);
    formdata.append(json);
    return formdata.toString();
  }

  public void add(SysEngineIformMap temp)
  {
    SysEngineIformMap sysEngineIformMap = temp;

    if (temp.getFieldTitle() != null) sysEngineIformMap.setFieldTitle(temp.getFieldTitle());
    if (temp.getDisplayEnum() != null) sysEngineIformMap.setDisplayEnum(temp.getDisplayEnum());
    if (temp.getDisplayType() != null) sysEngineIformMap.setDisplayType(temp.getDisplayType());
    if (temp.getDisplaywidth() != null) sysEngineIformMap.setDisplaywidth(temp.getDisplaywidth());
    if (temp.getFieldDefault() != null) sysEngineIformMap.setFieldDefault(temp.getFieldDefault());
    if (temp.getFieldNotnull() != null) sysEngineIformMap.setFieldNotnull(temp.getFieldNotnull());
    if (temp.getHtmlInner() != null) sysEngineIformMap.setHtmlInner(temp.getHtmlInner());
    if (temp.getInputHeight() != null) sysEngineIformMap.setInputHeight(temp.getInputHeight());
    if (temp.getInputWidth() != null) sysEngineIformMap.setInputWidth(temp.getInputWidth());
    if (temp.getMapType() == null) sysEngineIformMap.setMapType(new Long(1L));

    this.sysEngineIFormMapDAO.save(sysEngineIformMap);
  }

  public void update(SysEngineIformMap temp)
  {
    SysEngineIformMap sysEngineIformMap = this.sysEngineIFormMapDAO.getModel(temp.getId());

    if (temp.getFieldTitle() != null) sysEngineIformMap.setFieldTitle(temp.getFieldTitle());
    if (temp.getDisplayEnum() != null) sysEngineIformMap.setDisplayEnum(temp.getDisplayEnum());
    if (temp.getDisplayType() != null) sysEngineIformMap.setDisplayType(temp.getDisplayType());
    if (temp.getDisplaywidth() != null)
      sysEngineIformMap.setDisplaywidth(temp.getDisplaywidth());
    else {
      sysEngineIformMap.setDisplaywidth(new Long(0L));
    }
    if (temp.getFieldDefault() != null) sysEngineIformMap.setFieldDefault(temp.getFieldDefault());
    if (temp.getFieldNotnull() != null) sysEngineIformMap.setFieldNotnull(temp.getFieldNotnull());
    if (temp.getHtmlInner() != null) sysEngineIformMap.setHtmlInner(temp.getHtmlInner());
    if (temp.getInputHeight() != null)
      sysEngineIformMap.setInputHeight(temp.getInputHeight());
    else {
      sysEngineIformMap.setInputHeight(new Long(0L));
    }
    if (temp.getInputWidth() != null)
      sysEngineIformMap.setInputWidth(temp.getInputWidth());
    else {
      sysEngineIformMap.setInputWidth(new Long(0L));
    }
    if (temp.getMapType() == null) sysEngineIformMap.setMapType(new Long(1L));

    this.sysEngineIFormMapDAO.update(sysEngineIformMap);
  }

  public void delete(String ids)
  {
    String[] list = ids.split(",");
    for (int i = 0; i < list.length; i++)
      if (!list[i].trim().equals("")) {
        SysEngineIformMap sysEngineIformMap = this.sysEngineIFormMapDAO.getModel(new Long(list[i].trim()));
        if (sysEngineIformMap != null)
          this.sysEngineIFormMapDAO.delete(sysEngineIformMap);
      }
  }

  public void delete(Long id)
  {
    if (id != null) {
      SysEngineIformMap sysEngineIformMap = this.sysEngineIFormMapDAO.getModel(id);
      if (sysEngineIformMap != null)
        this.sysEngineIFormMapDAO.delete(sysEngineIformMap);
    }
  }

  public void moveUp(Long formid, Long id)
  {
    String type = "up";
    this.sysEngineIFormMapDAO.updateIndex(formid, id, type);
  }

  public void moveDown(Long formid, Long id)
  {
    String type = "down";
    this.sysEngineIFormMapDAO.updateIndex(formid, id, type);
  }

  public void moveFirst(Long formid, Long id)
  {
    String type = "first";
    this.sysEngineIFormMapDAO.updateIndex(formid, id, type);
  }

  public void moveLast(Long formid, Long id)
  {
    String type = "last";
    this.sysEngineIFormMapDAO.updateIndex(formid, id, type);
  }

  public void autoDeleteNonExistedIformMap(Long formid)
  {
    List<SysEngineIformMap> list = this.sysEngineIFormMapDAO.getDeletedIformMap(formid);
    if ((list != null) && (list.size() > 0))
      for (SysEngineIformMap map : list)
        this.sysEngineIFormMapDAO.delete(map);
  }

  public SysEngineIFormDAO getSysEngineIFormDAO()
  {
    return this.sysEngineIFormDAO;
  }
  public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO) {
    this.sysEngineIFormDAO = sysEngineIFormDAO;
  }

  public SysEngineIFormMapDAO getSysEngineIFormMapDAO() {
    return this.sysEngineIFormMapDAO;
  }

  public void setSysEngineIFormMapDAO(SysEngineIFormMapDAO sysEngineIFormMapDAO)
  {
    this.sysEngineIFormMapDAO = sysEngineIFormMapDAO;
  }

  public SysEngineMetadataMapDAO getSysEngineMetadataMapDAO() {
    return this.sysEngineMetadataMapDAO;
  }

  public void setSysEngineMetadataMapDAO(SysEngineMetadataMapDAO sysEngineMetadataMapDAO)
  {
    this.sysEngineMetadataMapDAO = sysEngineMetadataMapDAO;
  }

  public SysEngineMetadataDAO getSysEngineMetadataDAO() {
    return this.sysEngineMetadataDAO;
  }

  public void setSysEngineMetadataDAO(SysEngineMetadataDAO sysEngineMetadataDAO) {
    this.sysEngineMetadataDAO = sysEngineMetadataDAO;
  }
}