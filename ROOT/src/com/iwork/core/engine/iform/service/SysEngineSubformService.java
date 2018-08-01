package com.iwork.core.engine.iform.service;

import com.iwork.commons.util.PinYinUtil;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.dao.SysEngineIFormMapDAO;
import com.iwork.core.engine.iform.dao.SysEngineSubformDAO;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.iform.model.SysEngineSubform;
import com.iwork.core.engine.iform.util.Page;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataDAO;
import com.iwork.core.engine.metadata.dao.SysEngineMetadataMapDAO;
import com.iwork.core.engine.metadata.model.SysEngineMetadata;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.dictionary.constant.DictionaryConstant;
import com.iwork.plugs.dictionary.dao.SysDictionaryBaseInfoDAO;
import com.iwork.plugs.dictionary.model.SysDictionaryBaseinfo;
import com.iwork.process.definition.step.model.ProcessStepFormMap;
import com.iwork.sdk.FileUploadAPI;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import org.activiti.engine.TaskService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

public class SysEngineSubformService
{
  private static final Log log = LogFactory.getLog(SysEngineSubformService.class);
  private SysEngineSubformDAO sysEngineSubformDAO;
  private SysEngineIFormDAO sysEngineIFormDAO;
  private SysEngineMetadataDAO sysEngineMetadataDAO;
  private SysEngineMetadataMapDAO sysEngineMetadataMapDAO;
  private SysEngineIFormMapDAO sysEngineIFormMapDAO;
  private SysDictionaryBaseInfoDAO sysDictionaryBaseInfoDAO;
  private IFormService iformService;
  private TaskService taskService;

  public String getSubFormDataJson(Long subformid, String subformkey, Long instanceid, Page page)
  {
    HttpServletRequest request = ServletActionContext.getRequest();
    SysEngineIform subformModel = this.sysEngineIFormDAO.getSysEngineIformModel(subformid);
    SysEngineMetadata metadata = this.sysEngineMetadataDAO.getModel(subformModel.getMetadataid());
    List subFormMapList = this.sysEngineIFormMapDAO.getDataList(subformid);
    Map total = new HashMap();
    List list = null;
    int totalRecord = 0;
    int totalNum = 0;
    int count = 0;
    try {
      totalRecord = this.sysEngineSubformDAO.getSubFormDataSize(subformModel, subFormMapList, metadata.getEntityname(), instanceid);
      BigDecimal b1 = new BigDecimal(totalRecord);
      BigDecimal b2 = new BigDecimal(page.getPageSize());
      totalNum = b1.divide(b2, 0, 0).intValue();
      int startRow = page.getPageSize() * (page.getCurPageNo() - 1);
//王欢 2018-06-13 因为行项目子表翻页时数据少查询了一条  故而把以下代码注释
//      if (startRow != 0) {
//        startRow++;
//      }
      count = startRow;
      String orderby = "";
      if ((page != null) && (page.getOrderBy() != null)) {
        orderby = page.getOrderBy() + " " + page.getOrder();
      }
      if ((orderby == null) || (orderby.equals(""))) {
        SysEngineSubform subform = this.sysEngineSubformDAO.getModel(subformid, subformkey);
        if ((subform != null) && (subform.getOrderColumn() != null) && (subform.getOrderColumn() != "")) {
          orderby = subform.getOrderColumn();
        }
      }

      if (totalRecord != 0)
        list = this.sysEngineSubformDAO.getSubFormDataList(subformModel, subFormMapList, metadata.getEntityname(), instanceid, page.getPageSize(), startRow, orderby);
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
    }
    if (list == null) return "";

    List rows = new ArrayList();
    StringBuffer html = new StringBuffer();
    for (ListIterator iterator = list.listIterator(); iterator.hasNext(); ) {
      HashMap hash = (HashMap)iterator.next();

      if (hash != null)
      {
        Iterator iter = hash.entrySet().iterator();

        Map item = new HashMap();
        while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry)iter.next();
          Object key = entry.getKey();
          Object val = entry.getValue();

          if ((key != null) && (val != null)) {
            SysEngineIformMap formMap = this.sysEngineIFormMapDAO.getModel(subformid, key.toString());
            if ((formMap != null) && (formMap.getDisplayType() != null) && (formMap.getDisplayType().equals("upload"))) {
              String upfileStr = val.toString();
              String[] uuid = upfileStr.split(",");
              StringBuffer value = new StringBuffer();
              for (String id : uuid) {
                FileUpload upload = FileUploadAPI.getInstance().getFileUpload(id);
                if (upload != null) {
                  value.append("<a href='uploadifyDownload.action?fileUUID=").append(id).append("'><img src='iwork_img/attach.gif'>").append(upload.getFileSrcName()).append("</a><br/>");
                  item.put(key.toString(), value.toString());
                }
              }
            }
            else if ((formMap != null) && (formMap.getDisplayType() != null) && (formMap.getDisplayType().equals("imgUpload"))) {
              String upfileStr = val.toString();
              String[] uuid = upfileStr.split(",");
              StringBuffer value = new StringBuffer();
              for (String id : uuid) {
                FileUpload upload = FileUploadAPI.getInstance().getFileUpload(id);
                if (upload != null) {
                  value.append("<a href='uploadifyDownload.action?fileUUID=").append(id).append("'><img height='40' src='").append(upload.getFileUrl()).append("'></a><br/>");
                  item.put(key.toString(), value.toString());
                }
              }
            } else if ((formMap != null) && (formMap.getDisplayType() != null) && (formMap.getDisplayType().equals("TxtBox_Money"))) {
              String value = "";
              String v = "";
              if (val.toString().equals(""))
                v = "0";
              else {
                v = val.toString();
              }

              SysEngineMetadataMap semm = this.sysEngineMetadataMapDAO.getModel(metadata.getId(), key.toString());
              if ((semm != null) && (semm.getFieldLength().indexOf(",") > 0)) {
                String fl = semm.getFieldLength().substring(semm.getFieldLength().indexOf(",") + 1);
                int pointNumber = Integer.parseInt(fl);
                BigDecimal b = new BigDecimal(v);
                BigDecimal one = new BigDecimal("1");
                BigDecimal b3 = b.divide(one, pointNumber, 4);
                value = b3.toPlainString();
              } else {
                value = v;
              }

              item.put(key.toString(), value);
            } else {
              item.put(key.toString(), val.toString());
            }
          }
        }

        count++;

        rows.add(item);
      }
    }
    total.put("total", Integer.valueOf(list.size()));
    total.put("instanceId", instanceid);
    total.put("subformid", subformid);
    total.put("curPage", Integer.valueOf(page.getCurPageNo()));
    total.put("pageSize", Integer.valueOf(page.getPageSize()));
    total.put("totalPages", Integer.valueOf(totalNum));
    total.put("totalRecords", Integer.valueOf(totalRecord));
    total.put("dataRows", rows);
    JSONArray json = JSONArray.fromObject(total);
    html.append(json);
    return html.toString();
  }

  public String getMobileSubFormData(Long subformid, Long instanceid, String taskId, boolean isPurview, Page page)
  {
    SysEngineIform subformModel = this.sysEngineIFormDAO.getSysEngineIformModel(subformid);
    SysEngineMetadata metadata = this.sysEngineMetadataDAO.getModel(subformModel.getMetadataid());
    List<SysEngineIformMap> subFormMapList = this.sysEngineIFormMapDAO.getDataList(subformid);
    Map total = new HashMap();
    List list = null;
    int totalRecord = 0;
    int totalNum = 0;
    int count = 0;
    try {
      totalRecord = this.sysEngineSubformDAO.getSubFormDataSize(subformModel, subFormMapList, metadata.getEntityname(), instanceid);
      BigDecimal b1 = new BigDecimal(totalRecord);
      BigDecimal b2 = new BigDecimal(page.getPageSize());
      totalNum = b1.divide(b2, 0, 0).intValue();
      int startRow = page.getPageSize() * (page.getCurPageNo() - 1);
      count = startRow;
      String orderby = "";
      if ((page != null) && (page.getOrderBy() != null)) {
        orderby = page.getOrderBy() + " " + page.getOrder();
      }
      if (totalRecord != 0)
        list = this.sysEngineSubformDAO.getSubFormDataList(subformModel, subFormMapList, metadata.getEntityname(), instanceid, page.getPageSize(), startRow, orderby);
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
    }
    if (list == null) return "";

    List rows = new ArrayList();
    StringBuffer html = new StringBuffer();
    for (ListIterator iterator = list.listIterator(); iterator.hasNext(); )
    {
      HashMap hash = (HashMap)iterator.next();

      if (hash != null)
      {
        BigDecimal id = (BigDecimal)hash.get("ID");
        Iterator iter = hash.entrySet().iterator();

        html.append("<div class=\"subGridDiv\">");
        html.append("<table  style=\"margin:0px;\" width=\"100%\" > ").append("\n");

        Map item = new HashMap();
        while (iter.hasNext()) {
          Map.Entry entry = (Map.Entry)iter.next();
          Object key = entry.getKey();
          Object val = entry.getValue();
          String upfileStr;
          if ((key != null) && (val != null)) {
            SysEngineIformMap formMap = this.sysEngineIFormMapDAO.getModel(subformid, key.toString());
            if ((formMap != null) && (formMap.getDisplayType() != null) && (formMap.getDisplayType().equals("imgUpload"))) {
              upfileStr = val.toString();
              String[] uuid = upfileStr.split(",");
              StringBuffer value = new StringBuffer();
              int num = 1;
              for (String dateid : uuid) {
                FileUpload upload = FileUploadAPI.getInstance().getFileUpload(dateid);
                if (upload != null) {
                  value.append("<img height='40' weight='40' src='").append(upload.getFileUrl()).append("'>&nbsp;");
                  if (num % 5 == 0) {
                    value.append("<br/>");
                  }
                  val = value.toString();
                  num++;
                }
              }
            }
          }
          if ((key != null) && (val != null) && 
            (!val.equals(""))) {
            for (SysEngineIformMap seim : subFormMapList) {
              if (seim.getFieldName().equals(key)) {
                html.append("<tr  class=\"subGridItemDiv\"> ").append("\n");
                html.append("<td class=\"mb_title\">").append(seim.getFieldTitle()).append("</td> ").append("\n");
                html.append("<td class=\"mb_data\">").append(val).append("</td> ").append("\n");
                html.append("</tr> ").append("\n");
                break;
              }
            }
          }
        }

        if (isPurview) {
          html.append("<tr> ").append("\n");
          html.append("<td colspan=\"2\" style=\"text-align:right\"> ").append("\n");
          html.append("<a href=\"javascript:editSubForm(").append(id).append(");\" data-role=\"button\" data-icon=\"edit\"  data-inline=\"true\">编辑</a>");
          html.append("<a href=\"javascript:delSubForm(").append(id).append(");\" data-role=\"button\" data-icon=\"delete\" data-inline=\"true\">删除</a>");

          html.append("</td> ").append("\n");
          html.append("</tr> ").append("\n");
        }
        html.append("</table>").append("\n");
        html.append("</div>").append("\n");
        count++;
      }
    }
    return html.toString();
  }

  public String getSubFormToolbarHtml(SysEngineSubform subform, Long instanceid, String taskId)
  {
    StringBuffer toolbar = new StringBuffer();
    String newRowFunctionname = "newRow";
    String selectRowsFunctionname = "selectRows";
    toolbar.append("<div data-role=\"footer\"  style=\"padding:10px;text-align:center\" data-position=\"fixed\">");
    boolean toolbarIsShow = false;
    boolean isDic = false;

    if ((subform.getDictionaryId() != null) && (!subform.getDictionaryId().equals(new Long(0L)))) {
      toolbarIsShow = true;
      isDic = true;
      toolbar.append("<a href=\"javascript:openSubDictionary('").append(subform.getDictionaryId()).append("')\" data-role=\"button\" data-icon=\"search\"  data-inline=\"true\" data-theme=\"b\"  >　选　择　</a>");
    }

    if (((subform.getIsAdd() != null) && (!subform.getIsAdd().equals(new Long(0L)))) || (isDic)) {
      toolbarIsShow = true;
      toolbar.append("<a href=\"javascript:dosave();\" style=\"width:130px;\" data-role=\"button\" data-icon=\"check\" data-inline=\"true\" >保&nbsp;&nbsp;存</a>");
      toolbar.append("<a href=\"javascript:back();\" style=\"width:130px;\"  data-role=\"button\"  data-icon=\"back\" data-inline=\"true\">返&nbsp;&nbsp;回</a>");
    }

    toolbar.append("</div>");
    return toolbar.toString();
  }

  public String getMobileSubFormItemPage(Long formId, Long subformid, String taskId, Long instanceid, Long id)
  {
    List rows = new ArrayList();
    StringBuffer html = new StringBuffer();

    SysEngineIform sysEngineIform = this.sysEngineIFormDAO.getSysEngineIformModel(subformid);
    if (sysEngineIform == null) {
      return "子表模型设置错误";
    }

    List<SysEngineIformMap> maplist = this.sysEngineIFormMapDAO.getDataList(subformid);

    SysEngineMetadata metadataModel = this.sysEngineMetadataDAO.getModel(sysEngineIform.getMetadataid());
    HashMap formdata = null;
    try {
      if (id != null)
        formdata = this.sysEngineSubformDAO.getSubFormDataListItem(sysEngineIform, maplist, metadataModel.getEntityname(), instanceid, id);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    HashMap taskParam = null;
    if ((taskId != null) && (!taskId.equals("0")))
      try {
        taskParam = (HashMap)this.taskService.getVariables(taskId.toString());
      } catch (Exception localException1) {
      }
    StringBuffer mobileHTML = new StringBuffer();
    mobileHTML.append("<table width=\"97%\">");
    try
    {
      for (SysEngineIformMap semm : maplist)
      {
        String field = semm.getFieldName();
        String value = "";
        if ((formdata != null) && 
          (formdata.get(field) != null)) {
          Object obj = formdata.get(field);
          if ((obj instanceof Date)) {
            Calendar cal = Calendar.getInstance();
            Date date = (Date)obj;
            cal.setTime(date);
            int hours = cal.get(10);
            int minutes = cal.get(12);
            if ((hours == 0) && (minutes == 0)) {
              SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
              value = dateformat1.format((Date)obj);
            } else {
              SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              value = dateformat1.format((Date)obj);
            }
          }
          else {
            value = formdata.get(field).toString();
          }

        }

        String uiDefine = "";

        HashMap psfmList = null;
        int status = 10;
        try
        {
          if (psfmList != null) {
            ProcessStepFormMap psfm = (ProcessStepFormMap)psfmList.get(subformid + field);
            if (psfm != null)
              if (psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_MODIFY))
                status = 20;
              else if (psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_READONLY))
                status = 10;
              else if (psfm.getStatus().equals(ProcessStepFormMap.PRC_STEP_FORM_STATUS_HIDDEN))
                status = 30;
          }
          else
          {
            status = 20;
          }
        } catch (Exception e) {
          e.printStackTrace(System.err);
        }
        String temp = value;

        uiDefine = this.iformService.getMobileUICompoment(sysEngineIform, metadataModel, taskParam, instanceid, field, value, status);

        if ((id != null) || (id == null))
        {
          mobileHTML.append("<tr >");
          mobileHTML.append("<td class=\"mb_title\">");
          mobileHTML.append(semm.getFieldTitle());
          mobileHTML.append("</td>");
          mobileHTML.append("</tr>");
          mobileHTML.append("<tr>");
          mobileHTML.append("<td class=\"mb_data\">");
          mobileHTML.append(uiDefine);
          mobileHTML.append("</td>");
          mobileHTML.append("</tr>");
        }
      }
      mobileHTML.append("</table>");
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return mobileHTML.toString();
  }

  public String getSubTableList(Long formid)
  {
    StringBuffer griddata = new StringBuffer();

    SysEngineIform iformModel = this.sysEngineIFormDAO.getSysEngineIformModel(formid);

    SysEngineMetadata sysEngineMetadata = this.sysEngineMetadataDAO.getModel(iformModel.getMetadataid());

    List list = this.sysEngineSubformDAO.getDataList(formid);

    List items = new ArrayList();
    List rows = new ArrayList();
    Map total = new HashMap();
    total.put("total", "28");
    items.add(total);

    for (int i = 0; i < list.size(); i++) {
      SysEngineSubform model = (SysEngineSubform)list.get(i);
      Map item = new HashMap();
      item.put("ID", model.getId());
      item.put("TITLE", model.getTitle());
      item.put("KEY", model.getSubtablekey());
      if (model.getType() != null) {
        if (model.getType().longValue() == 0L)
          item.put("TYPE", "编辑子表");
        else {
          item.put("TYPE", "视窗子表");
        }
      }
      item.put("IFORM_ID", model.getIformId());
      if (model.getIsResize() != null) {
        if (model.getIsResize().longValue() == 0L)
          item.put("IS_RESIZE", "是");
        else {
          item.put("IS_RESIZE", "否");
        }
      }
      item.put("METADATA_NAME", sysEngineMetadata.getEntityname());
      item.put("METADATA_TITLE", sysEngineMetadata.getEntitytitle());
      item.put("HEIGHT", model.getHeight());
      item.put("FIXEDROW", model.getFixedrow());
      String toolbar = "<a href='javascript:openMapModify(" + model.getId() + ")'><img src='iwork_img/icon/confg_16.gif' border='0'>设置</a>";
      item.put("DO", toolbar);
      rows.add(item);
    }

    total.put("rows", rows);
    JSONArray json = JSONArray.fromObject(total);
    griddata.append(json);
    return griddata.toString();
  }

  public String getSubTableModelJson(Long subformid)
  {
    SysEngineSubform model = this.sysEngineSubformDAO.getModel(subformid);
    StringBuffer formdata = new StringBuffer();
    Map item = new HashMap();
    item.put("subformTitle", model.getTitle());
    item.put("subForm_key", model.getSubtablekey());
    item.put("subForm_title", model.getTitle());
    item.put("subForm_type", model.getType());
    item.put("isResize", model.getIsResize());
    if (model.getIsResize() != null) model.setIsResize(new Long(0L));
    item.put("subForm_height", model.getIsResize());
    if (model.getAutowidth() != null) model.setAutowidth(new Long(0L));
    item.put("subForm_autoWidth", model.getAutowidth());
    if (model.getFixedrow() != null) model.setFixedrow(new Long(0L));
    item.put("subForm_fixedrow", model.getFixedrow());
    JSONArray json = JSONArray.fromObject(item);
    formdata.append(json);
    return formdata.toString();
  }

  public String getMaxID()
  {
    return this.sysEngineSubformDAO.getMaxID();
  }

  public SysEngineSubform getSysEngineSubform(Long id)
  {
    return this.sysEngineSubformDAO.getModel(id);
  }

  public void save(SysEngineSubform sysEngineSubform)
  {
    if (sysEngineSubform != null) {
      if (sysEngineSubform.getOrderType() == null) {
        sysEngineSubform.setOrderType("ASC");
      }
      this.sysEngineSubformDAO.save(sysEngineSubform);
    }
  }

  public void update(SysEngineSubform temp)
  {
    if (temp != null) {
      if (temp.getOrderType() == null) {
        temp.setOrderType("ASC");
      }
      this.sysEngineSubformDAO.update(temp);
    }
  }

  public void delete(Long id)
  {
    SysEngineSubform sysEngineSubform = this.sysEngineSubformDAO.getModel(id);
    if (sysEngineSubform != null)
      this.sysEngineSubformDAO.delete(sysEngineSubform);
  }

  public String createSubFormKey(Long formId, String title)
  {
    title = title.replace(" ", "_").replace("$", "").replace("#", "").replace("\"", "").replace("'", "").replace("!", "").replace("@", "").replace("%", "").replace("&", "").replace("$", "*").replace("?", "").replace(">", "").replace("<", "");

    String subformkey = "SUBFORM_" + PinYinUtil.zh_CnToPinyinHeadParser(title);

    int tableindex = 0;
    while (true)
    {
      boolean flag = ishaving(formId, subformkey);
      if (!flag) break;
      tableindex++;
      subformkey = "SUBFORM_" + PinYinUtil.zh_CnToPinyinHeadParser(title) + "_" + tableindex;
    }

    return subformkey;
  }

  public SysEngineSubform initSubModel(Long formId)
  {
    SysEngineSubform model = new SysEngineSubform();
    model.setType(new Long(0L));
    model.setExcelExp(new Long(0L));
    model.setExcelImp(new Long(0L));
    model.setAutowidth(new Long(0L));
    model.setIsResize(new Long(0L));
    model.setIformId(formId);
    model.setIsCopy(new Long(0L));
    model.setIsAdd(new Long(1L));
    model.setIsSave(new Long(1L));
    model.setIsDel(new Long(1L));
    return model;
  }

  public List<SysDictionaryBaseinfo> getDictionaryList(Long formId)
  {
    List list = this.sysDictionaryBaseInfoDAO.getListByType(formId, DictionaryConstant.DICTIONARY_TYPE_SUBGRID);
    return list;
  }

  private boolean ishaving(Long formId, String subformkey)
  {
    boolean flag = false;
    List list = this.sysEngineSubformDAO.getSubFormListforFormKey(formId, subformkey);
    if ((list != null) && (list.size() > 0)) {
      flag = true;
    }
    return flag;
  }

  public void setSysEngineSubformDAO(SysEngineSubformDAO sysEngineSubformDAO) {
    this.sysEngineSubformDAO = sysEngineSubformDAO;
  }

  public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO)
  {
    this.sysEngineIFormDAO = sysEngineIFormDAO;
  }

  public void setSysEngineMetadataDAO(SysEngineMetadataDAO sysEngineMetadataDAO)
  {
    this.sysEngineMetadataDAO = sysEngineMetadataDAO;
  }
  public void setSysEngineIFormMapDAO(SysEngineIFormMapDAO sysEngineIFormMapDAO) {
    this.sysEngineIFormMapDAO = sysEngineIFormMapDAO;
  }

  public SysEngineSubformDAO getSysEngineSubformDAO() {
    return this.sysEngineSubformDAO;
  }

  public void setSysEngineMetadataMapDAO(SysEngineMetadataMapDAO sysEngineMetadataMapDAO) {
    this.sysEngineMetadataMapDAO = sysEngineMetadataMapDAO;
  }

  public void setSysDictionaryBaseInfoDAO(SysDictionaryBaseInfoDAO sysDictionaryBaseInfoDAO) {
    this.sysDictionaryBaseInfoDAO = sysDictionaryBaseInfoDAO;
  }

  public void setIformService(IFormService iformService) {
    this.iformService = iformService;
  }

  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }
}