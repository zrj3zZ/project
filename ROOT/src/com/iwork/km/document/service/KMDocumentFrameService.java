package com.iwork.km.document.service;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.ScatterSample;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.util.FileUploadUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.km.document.dao.KMDirectoryDAO;
import com.iwork.km.document.dao.KMDocDAO;
import com.iwork.km.document.model.IworkKmDirectory;
import com.iwork.km.document.model.IworkKmDoc;
import com.iwork.plugs.cms.dao.CmsInfoDAO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.input.NullInputStream;
import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;

public class KMDocumentFrameService
{
	public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
  private KMDirectoryDAO kmDirectoryDAO;
  private KMDocDAO kmDocDAO;
  private FileUploadDAO uploadifyDAO;
  private FileUploadUtil fileUploadUtil;
  private KMPurViewService kmPurViewService;

  
  public String showdmTreeJson(Long directoryid)
  {
    List list = new ArrayList();
      HashMap hash = new HashMap();
      hash.put("id", Integer.valueOf(0));
      hash.put("directoryid", Integer.valueOf(0));
      hash.put("name", "知识目录");
      hash.put("iconOpen", "iwork_img/km/treeimg/ftv2folderopen02.gif");
      hash.put("iconClose", "iwork_img/ztree/diy/40.png");
      hash.put("type", "ROOT");
      hash.put("isParent", "true");
      hash.put("parentid", null);
      if (SecurityUtil.isSuperManager()) {
        hash.put("PURVIEW", "1");
      }
      list.add(hash);
    	List lables=new ArrayList();
		lables.add("id");
		lables.add("parentid");
		lables.add("directoryname");
	
      OrgDepartment dept = UserContextUtil.getInstance().getCurrentUserContext()._deptModel;
      OrgUser orguser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
      String sql="   select * from Iwork_Km_Directory start with id in "
      		+ "   (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"')"
      		+ " connect by prior id=parentid union "
      		+ "   select * from Iwork_Km_Directory start with id in "
      		+ "    (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"')"
      		+ "    connect by prior parentid=id"
      		+ " union select * from Iwork_Km_Directory start with id in "
      		+ "   (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview=to_char((select s.departmentid from orguser s where s.userid='"+orguser.getUserid()+"')))"
      		+ " connect by prior id=parentid union "
      		+ "   select * from Iwork_Km_Directory start with id in "
      		+ "    (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview=to_char((select s.departmentid from orguser s where s.userid='"+orguser.getUserid()+"')))"
      		+ "    connect by prior parentid=id";
  	  List<HashMap> lists=DBUTilNew.getDataList(lables, sql, null);
  	  for (int i = 0; i < lists.size(); i++) {
  		 hash = new HashMap();
  		 hash.put("PURVIEW", "1");
         hash.put("id", lists.get(i).get("id"));
         hash.put("directoryid", lists.get(i).get("id"));
         hash.put("name", lists.get(i).get("directoryname"));
         hash.put("iconOpen", "iwork_img/km/treeimg/ftv2folderopen02.gif");
         hash.put("iconClose", "iwork_img/ztree/diy/40.png");
         hash.put("icon", "iwork_img/ztree/diy/40.png");
         hash.put("type", "DIR");
         hash.put("isParent", "false");
         hash.put("parentid", lists.get(i).get("parentid"));
         list.add(hash);
	 }
    
    
    JSONArray json = JSONArray.fromObject(list);
    return json.toString();
  }
  public String showTreeJson(Long directoryid)
  {
    List list = new ArrayList();
    if (directoryid == null) {
      HashMap hash = new HashMap();
      hash.put("id", Integer.valueOf(0));
      hash.put("directoryid", Integer.valueOf(0));
      hash.put("name", "知识目录");
      hash.put("iconOpen", "iwork_img/km/treeimg/ftv2folderopen02.gif");
      hash.put("iconClose", "iwork_img/ztree/diy/40.png");
      hash.put("type", "ROOT");
      hash.put("isParent", "true");
      hash.put("parentid", null);
      if (SecurityUtil.isSuperManager()) {
        hash.put("PURVIEW", "1");
      }
      list.add(hash);
    }  else  {
    	List lables=new ArrayList();
		lables.add("id");
		lables.add("parentid");
		lables.add("directoryname");
	
      List<IworkKmDirectory> dirlist = this.kmDirectoryDAO.getDirectoryList(directoryid);
      OrgDepartment dept = UserContextUtil.getInstance().getCurrentUserContext()._deptModel;
      OrgUser orguser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
      String sql="   select * from Iwork_Km_Directory start with id in "
      		+ "   (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"')"
      		+ " connect by prior id=parentid union "
      		+ "   select * from Iwork_Km_Directory start with id in "
      		+ "    (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"')"
      		+ "    connect by prior parentid=id"
      		+ " union select * from Iwork_Km_Directory start with id in "
      		+ "   (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview=to_char((select s.departmentid from orguser s where s.userid='"+orguser.getUserid()+"')))"
      		+ " connect by prior id=parentid union "
      		+ "   select * from Iwork_Km_Directory start with id in "
      		+ "    (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview=to_char((select s.departmentid from orguser s where s.userid='"+orguser.getUserid()+"')))"
      		+ "    connect by prior parentid=id";
  	  List<HashMap> lists=DBUTilNew.getDataList(lables, sql, null);
   /*  String sql="select distinct  id,parentid,directoryname from Iwork_Km_Directory"
     		+ " WHERE parentid <>(select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"') "
     		+ "  start with parentid =(select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"')  connect by prior parentid = id ";
     List<HashMap> listTwo=DBUTilNew.getDataList(lables,sql,null);*/
     for (IworkKmDirectory model : dirlist) {
        boolean flag = false;
        HashMap hash = new HashMap();
        if ((orguser.getUserid().equals(model.getCreateuser())) || (SecurityUtil.isSuperManager())) {
          flag = true;

          hash.put("PURVIEW", "1");
        } else {
     //     boolean issetpur = this.kmPurViewService.isSetPurview("folder", model.getId());
        	 boolean issetpur =true;
          if (issetpur)
          {
            flag = this.kmPurViewService.isDeptPurview(dept, "folder", new Long(0L), new Long(1L), model.getId());
            if (!flag) {
              flag = this.kmPurViewService.isUserPurview(orguser, "folder", new Long(1L), new Long(1L), model.getId());
            }
            if(lists!=null && lists.size()>0){
            	for (int i = 0; i < lists.size(); i++) {
					if(lists.get(i).get("id").toString().equals(model.getId().toString())){
						flag=true;
						break;
					}
				}
            }
            /*if(listTwo!=null && listTwo.size()>0){
            	for (int i = 0; i < listTwo.size(); i++) {
					if(listTwo.get(i).get("id").toString().equals(model.getId().toString())){
						flag=true;
						break;
					}
				}
            }*/
            if (flag) {
              hash.put("PURVIEW", "1");
            }
            else {
              flag = this.kmPurViewService.isDeptPurview(dept, "folder", new Long(0L), new Long(0L), model.getId());
              if (!flag) {
                flag = this.kmPurViewService.isUserPurview(orguser, "folder", new Long(1L), new Long(0L), model.getId());
              }
              if (flag)
                hash.put("PURVIEW", "0");
            }
          }
          else
          {
            flag = true;
            hash.put("PURVIEW", "0");
          }
        }
        if (flag) {
          hash.put("id", model.getId());
          hash.put("directoryid", model.getId());
          hash.put("name", model.getDirectoryname());
          hash.put("iconOpen", "iwork_img/km/treeimg/ftv2folderopen02.gif");
          hash.put("iconClose", "iwork_img/ztree/diy/40.png");
          hash.put("type", "DIR");
          hash.put("isParent", "true");
          hash.put("parentid", model.getParentid());
          list.add(hash);
        
        }
      }
    }
    JSONArray json = JSONArray.fromObject(list);
    return json.toString();
  }

  public String showGridJson(Long fid, String type)
  {    
	  OrgUser orguser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();

	  List lables=new ArrayList();
		lables.add("id");
		lables.add("parentid");
		lables.add("directoryname");
		 String sql="   select * from Iwork_Km_Directory start with id in "
		      		+ "   (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"')"
		      		+ " connect by prior id=parentid union "
		      		+ "   select * from Iwork_Km_Directory start with id in "
		      		+ "    (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview='"+orguser.getUserid()+"')"
		      		+ "    connect by prior parentid=id"
		      		+ " union select * from Iwork_Km_Directory start with id in "
		      		+ "   (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview=to_char((select s.departmentid from orguser s where s.userid='"+orguser.getUserid()+"')))"
		      		+ " connect by prior id=parentid union "
		      		+ "   select * from Iwork_Km_Directory start with id in "
		      		+ "    (select t.id from Iwork_Km_Purview_item s left join Iwork_Km_Purview p on p.id = s.pur_id left join Iwork_Km_Directory t on t.id=p.p_id where s.purview=to_char((select s.departmentid from orguser s where s.userid='"+orguser.getUserid()+"')))"
		      		+ "    connect by prior parentid=id";
//List<HashMap> lists=DBUTilNew.getDataList(lables, "select id,parentid,directoryname from Iwork_Km_Directory  where id<>" + fid + " start with id=" + fid + " connect by prior id=parentid   order by order_Index", null);
		 List<HashMap> lists=DBUTilNew.getDataList(lables, sql, null);
    List item = new ArrayList();
    Map total = new HashMap();
    OrgDepartment dept = UserContextUtil.getInstance().getCurrentUserContext()._deptModel;

    int totalRecord = 0;
    HashMap hash;
    if ("file".equals(type)) {
      IworkKmDoc model = this.kmDocDAO.getDocModel(fid);
      boolean flag = false;
      hash = new HashMap();
      if ((orguser.getUserid().equals(model.getCreateUser())) || (SecurityUtil.isSuperManager())) {
        flag = true;

        hash.put("PURVIEW", "1");
      } else {
        boolean issetpur = this.kmPurViewService.isSetPurview("doc", model.getId());
        if (issetpur)
        {
          flag = this.kmPurViewService.isDeptPurview(dept, "doc", new Long(0L), new Long(1L), model.getId());
          if (!flag) {
            flag = this.kmPurViewService.isUserPurview(orguser, "doc", new Long(1L), new Long(1L), model.getId());
          }
          
          if (flag) {
            hash.put("PURVIEW", "1");
          }
          else {
            flag = this.kmPurViewService.isDeptPurview(dept, "doc", new Long(0L), new Long(0L), model.getId());
            if (!flag) {
              flag = this.kmPurViewService.isUserPurview(orguser, "doc", new Long(1L), new Long(0L), model.getId());
            }
            if (flag)
              hash.put("PURVIEW", "0");
          }
        }
        else
        {
          flag = true;
          hash.put("PURVIEW", "0");
        }

      }

      if (!flag) return "";
      if (flag) {
        hash.put("ID", model.getId());
        hash.put("INDEX", Integer.valueOf(totalRecord + 1));
        FileUpload fu = this.uploadifyDAO.getFileUpload(FileUpload.class, model.getDocEnum());
        if (fu != null) {
          String fsn = fu.getFileSrcName();
          String suffix = FileUtil.getFileExt(fsn);
          String icon = WebUIUtil.getLinkIcon(suffix);
          hash.put("ICON", icon);
        } else {
          hash.put("ICON", "<img src='iwork_img/page.png' border='0'>");
          hash.put("NAME", model.getDocName());
        }

        Long docType = model.getDocType();
        if (docType.longValue() == 0L)
        {
          hash.put("NAME", "<a target=\"_blank\"  href=\"uploadifyDownload.action?fileUUID=" + fu.getFileId() + "\">" + model.getDocName() + "</a>");
          hash.put("TYPE", "DOC");
          hash.put("TYPENAME", "文档");
        } else if (docType.longValue() == 1L) {
          String url = model.getDocEnum();
          if (url.indexOf(",") > -1) {
            url = url.replace(",", "");
          }
          if (url.indexOf("http://") < 0) {
            url = "http://";
          }
          hash.put("NAME", "<a  target=\"_blank\" class=\"nameLink\"  href=\"" + url.trim() + "\">" + model.getDocName() + "</a>");
          hash.put("TYPE", "LINK");
          hash.put("TYPENAME", "链接");
        } else if (docType.longValue() == 2L) {
          hash.put("TYPE", "FORM");
          hash.put("NAME", "<a class=\"nameLink\"  href=\"" + model.getDocEnum() + "\">" + model.getDocName() + "</a>");
          hash.put("TYPENAME", "表单");
        }
        hash.put("DOCENUM", model.getDocEnum());
        hash.put("PARENTID", model.getDirectoryid());
        hash.put("SIZE", changeUnit(model.getFilesize()));
        hash.put("VERSION", "v" + model.getVersion() + ".0");
        hash.put("UPDATEUSER", model.getCreateUser());
        hash.put("UPDATEDATE", UtilDate.getShowTime(model.getCreateDate()));
        hash.put("STATUS", model.getStatus());
        hash.put("WJXZ", "<a href=\"javascript:uploadXxzcMb('"+model.getId()+"');\" class=\"easyui-linkbutton l-btn l-btn-plain\" plain=\"true\" id=\"mbxz\" ><span class=\"l-btn-left\"><span class=\"l-btn-text icon-excel-exp\" style=\"padding-left: 20px;\">打包下载</span></span></a>");
        if (model.getStatus().longValue() == 1L)
          hash.put("STATUSICON", "<img src='iwork_img/km/icon-unlock.png' title='解锁状态' border='0'>");
        else {
          hash.put("STATUSICON", "<img src='iwork_img/km/icon-lock.png' title='锁定状态' border='0'>");
        }
        if(!hash.get("TYPE").toString().equals("DIR")){
        	hash.put("WJXZ", "");
        }
        item.add(hash);
        totalRecord++;
      }

    }
    else if ("folder".equals(type))
    {
      List<IworkKmDirectory> dirlist = this.kmDirectoryDAO.getDirectoryList(fid);
      boolean flag;
      for (IworkKmDirectory model : dirlist) {
        flag = false;
         hash = new HashMap();
        if ((orguser.getUserid().equals(model.getCreateuser())) || (SecurityUtil.isSuperManager())) {
          flag = true;

          hash.put("PURVIEW", "1");
        }
        else {
         // boolean issetpur = this.kmPurViewService.isSetPurview("folder", model.getId());
          boolean issetpur =true;
          if (issetpur)
          {
            flag = this.kmPurViewService.isDeptPurview(dept, "folder", new Long(0L), new Long(1L), model.getId());
            if (!flag) {
              flag = this.kmPurViewService.isUserPurview(orguser, "folder", new Long(1L), new Long(1L), model.getId());
            }
            if(lists!=null && lists.size()>0){
              	for (int i = 0; i < lists.size(); i++) {
    					if(lists.get(i).get("id").toString().equals(model.getId().toString())){
    						flag=true;
    						break;
    					}
    				}
              }
            if (flag) {
              hash.put("PURVIEW", "1");
            }
            else {
              flag = this.kmPurViewService.isDeptPurview(dept, "folder", new Long(0L), new Long(0L), model.getId());
              if (!flag) {
                flag = this.kmPurViewService.isUserPurview(orguser, "folder", new Long(1L), new Long(0L), model.getId());
              }
              if (flag)
                hash.put("PURVIEW", "0");
            }
          }
          else
          {
            flag = true;
            hash.put("PURVIEW", "0");
          }

        }

        if (flag)
        {
          hash.put("ID", model.getId());
          hash.put("INDEX", Integer.valueOf(totalRecord + 1));
          hash.put("ICON", "<img src='iwork_img/ztree/diy/40.png' title='单击打开' border='0'>");
          hash.put("NAME", model.getDirectoryname());
          hash.put("TYPE", "DIR");
          hash.put("PARENTID", model.getParentid());
          hash.put("SIZE", "");
        //  hash.put("WJXZ", "<a href=\"javascript:uploadXxzcMb('"+model.getId()+"');\" class=\"easyui-linkbutton l-btn l-btn-plain\" plain=\"true\" id=\"mbxz\" ><span class=\"l-btn-left\"><span class=\"l-btn-text icon-excel-exp\" style=\"padding-left: 20px;\">打包下载</span></span></a>");
          hash.put("WJXZ", "<a href=\"javascript:uploadXxzcMb('"+model.getId()+"');\"  >打包下载</a>");
          hash.put("UPDATEUSER", model.getCreateuser());
          hash.put("UPDATEDATE", UtilDate.getShowTime(model.getCreatedate()));
          if(!hash.get("TYPE").toString().equals("DIR")){
          	hash.put("WJXZ", "");
          }
          item.add(hash);
          totalRecord++;
        }
      }
      List<IworkKmDoc> doclist = this.kmDocDAO.getDocList(fid);
      for (IworkKmDoc model : doclist) {
        flag = false;
        hash = new HashMap();
        if ((orguser.getUserid().equals(model.getCreateUser())) || (SecurityUtil.isSuperManager())) {
          flag = true;

          hash.put("PURVIEW", "1");
        }
        else {
          boolean issetpur = this.kmPurViewService.isSetPurview("doc", model.getId());
          if (issetpur)
          {
            flag = this.kmPurViewService.isDeptPurview(dept, "doc", new Long(0L), new Long(1L), model.getId());
            if (!flag) {
              flag = this.kmPurViewService.isUserPurview(orguser, "doc", new Long(1L), new Long(1L), model.getId());
            }
            if(lists!=null && lists.size()>0){
              	for (int i = 0; i < lists.size(); i++) {
    					if(lists.get(i).get("id").toString().equals(model.getId().toString())){
    						flag=true;
    						break;
    					}
    				}
              }
            if (flag) {
              hash.put("PURVIEW", "1");
            }
            else {
              flag = this.kmPurViewService.isDeptPurview(dept, "doc", new Long(0L), new Long(0L), model.getId());
              if (!flag) {
                flag = this.kmPurViewService.isUserPurview(orguser, "doc", new Long(1L), new Long(0L), model.getId());
              }
              if (flag)
                hash.put("PURVIEW", "0");
            }
          }
          else
          {
            flag = true;
            hash.put("PURVIEW", "0");
          }

        }

        if (flag)
        {
          hash.put("ID", model.getId());
          hash.put("INDEX", Integer.valueOf(totalRecord + 1));

          Long docType = model.getDocType();

          if (docType.longValue() == 0L) {
            String uuid = "";
            if (model.getDocEnum() != null) {
              uuid = model.getDocEnum().replace(",", "").trim();
            }
            FileUpload fu = this.uploadifyDAO.getFileUpload(FileUpload.class, uuid);
            if (fu != null) {
              String fsn = fu.getFileSrcName();
              String suffix = FileUtil.getFileExt(fsn);
              String icon = WebUIUtil.getLinkIcon(suffix);

              hash.put("NAME", "<a target=\"_blank\"  href=\"uploadifyDownload.action?fileUUID=" + fu.getFileId() + "\">" + model.getDocName() + "</a>");

              hash.put("ICON", icon);
            } else {
              hash.put("NAME", model.getDocName());
            }
            hash.put("TYPE", "DOC");
            hash.put("TYPENAME", "文档");
          } else if (docType.longValue() == 1L) {
            hash.put("TYPE", "LINK");
            hash.put("TYPENAME", "链接");
            String url = model.getDocEnum();
            if (url.indexOf(",") > -1) {
              url = url.replace(",", "");
            }
            if (url.indexOf("http://") < 0) {
              url = "http://";
            }
            hash.put("NAME", "<a  target=\"_blank\" class=\"nameLink\"  href=\"" + url.trim() + "\">" + model.getDocName() + "</a>");
            hash.put("ICON", "<img src='iwork_img/link.png' border='0'>");
          } else if (docType.longValue() == 2L) {
            hash.put("TYPE", "FORM");
            hash.put("TYPENAME", "表单");
            hash.put("ICON", "<img src='iwork_img/page.png' border='0'>");
          }
          hash.put("DOCENUM", model.getDocEnum());
          hash.put("PARENTID", model.getDirectoryid());
          hash.put("SIZE", changeUnit(model.getFilesize()));
          hash.put("VERSION", "v" + model.getVersion() + ".0");
          hash.put("UPDATEUSER", model.getCreateUser());
          hash.put("UPDATEDATE", UtilDate.getShowTime(model.getCreateDate()));
          hash.put("STATUS", model.getStatus());
          hash.put("WJXZ", "<a href=\"javascript:uploadXxzcMb('"+model.getId()+"');\"  >打包下载</a>");
          
          
       
          if (model.getStatus().longValue() == 1L)
            hash.put("STATUSICON", "<img src='iwork_img/km/icon-unlock.png' title='解锁状态' border='0'>");
          else {
            hash.put("STATUSICON", "<img src='iwork_img/km/icon-lock.png' title='锁定状态' border='0'>");
          }
          if(!hash.get("TYPE").toString().equals("DIR")){
          	hash.put("WJXZ", "");
          }
          item.add(hash);
          totalRecord++;
        }

      }

    }

    total.put("total", Integer.valueOf(totalRecord));
    total.put("dataRows", item);
    JSONArray json = JSONArray.fromObject(total);
    return json.toString();
  }
  
  /**
	 * 取得培训材料
	 * @return
	 */
	public String getPxcl(){
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		if(config.get("pxclDir")==null || "".equals(config.get("pxclDir"))) return "";
		Long fid = Long.parseLong(config.get("pxclDir")) ;
		
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		StringBuffer sb = new StringBuffer();
		
		int line = 0;
		int rows = 0;
		sb.append("<table width=\"99%\" id=\"tab6\"  class=\"table \" style=\"font-size:15px;\" >").append("\n");
		sb.append("  <tr>").append("\n");
		
		
		List<HashMap<String, Object>> questionList = null;
		List<IworkKmDoc> doclist = this.kmDocDAO.getDocList(fid);
		sb.append("    <col width=\"70%\" />").append("\n");
		
		sb.append("    <col width=\"30%\" />").append("\n");
		sb.append("  </tr>").append("\n");
		int n=0;
		for (IworkKmDoc model : doclist) {
			if(n>2) break;
			Long docType = model.getDocType();
			 if (docType.longValue() == 0L) {
				  String uuid = "";
		            if (model.getDocEnum() != null) {
		              uuid = model.getDocEnum().replace(",", "").trim();
		            }
		        	FileUpload fu = this.uploadifyDAO.getFileUpload(FileUpload.class, uuid);
					 if (fu != null) {
						 // hash.put("NAME", "<a target=\"_blank\"  href=\"uploadifyDownload.action?fileUUID=" + fu.getFileId() + "\">" + model.getDocName() + "</a>");
						 sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
						 sb.append("    <td style=\"font-size:15px;\"><a target=\"_blank\"  href=\"uploadifyDownload.action?fileUUID=" + fu.getFileId() + "\">").append( model.getDocName()+"</a></td>").append("\n");
						 sb.append("		<td style=\"font-size:15px;\">"+model.getCreateDate()+"</td>\n");
						 sb.append("  </tr>").append("\n");
						 n++;
					 }
			 }
		}
		/*if(questionList.size()>0){
			for(HashMap<String, Object> model : questionList) {
				
					sb.append("  <tr onmouseover=\"changeTRbgcolor(this);\" onmouseout=\"dorpTRbgcolor(this);\">").append("\n");
					sb.append("    <td style=\"line-height:22px;font-size:15px;color:#056ea4;\"><a href=\"javascript:addAdvance('"+model.get("tid")+"')\">").append(model.get("title")).append("--"+model.get("sj")+"</a></td>").append("\n");
					sb.append("		<td style=\"font-size:15px;\">"+model.get("zqjc")+"</td>\n");
					sb.append("		<td style=\"font-size:15px;\">"+model.get("zqdm")+"</td>\n");
					sb.append("		<td style=\"font-size:15px;\"><a href=\"javascript:rctxWc('"+model.get("id")+"','"+model.get("tid")+"','"+model.get("flag")+"','"+model.get("sj")+"')\">确认</a> </td>\n");
				
					sb.append("  </tr>").append("\n");
					
					if (line == rows - 1) {
						line++;
						break;
					}
					line++;
				}
		}*/
		
		sb.append("  <tr>").append("\n");
		sb.append("    <td></td> <td><a style=\"float:right;cursor:pointer;font-size:15px;\" onclick=\"openPXCL();\" href=\"#\">>>更多</a></td>").append("\n");
		sb.append("  </tr>").append("\n");
		sb.append("</table>").append("\n");
		return sb.toString();
	}
  
  
  
	public String votedownload(List<HashMap> list) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		Date date = new Date();
		
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		OrgUser user = uc.get_userModel();
		String foldername = user.getUserid()+sdf.format(date);
		//删除原有文件夹
		File filedel = new File(filepath+foldername);
    	delChange(filedel);
    	String ghfry="";
    	int i=1;
    	for(HashMap map:list){
    		String srcname = map.get("docname").toString();
    		if(srcname!=null && !"".equals(srcname)){
	    		String[] mc=srcname.split("\\.");
	    		String newsrcname=mc[0]+i+"."+""+mc[1];
	    		//循环复制文件
	    		File f = new File(filepath+map.get("fileurl").toString());
	    		if(f.exists()){
	    			ghfry = map.get("lj").toString();	
	    			File fnew = new File(filepath+foldername+"\\"+ghfry);
	    			int byteread = 0;
					int bytesum = 0;
					if(newsrcname!=null && !"".equals(newsrcname)){
						
						InputStream inStream = new FileInputStream(f);
						if(!fnew.exists()){
							fnew.mkdirs();
							StringBuffer sb = new StringBuffer();
							ghfry = map.get("lj").toString();
							sb.append(filepath).append(foldername.replaceAll(" ", "")).append(ghfry).append("\\").append(newsrcname);
							fnew = new File(sb.toString());
							fnew.createNewFile();
							FileOutputStream fsot = new FileOutputStream(fnew);
							byte[] buffer = new byte[1444]; 
							while ( (byteread = inStream.read(buffer)) != -1) { 
								bytesum += byteread; 
								fsot.write(buffer, 0, byteread);
							} 
							fsot.flush();
							inStream.close(); 
							fsot.close();
						}else{
							StringBuffer sb = new StringBuffer();
							sb.append(filepath).append(foldername.replaceAll(" ", "")).append(ghfry).append("\\").append(newsrcname);
							fnew = new File(sb.toString());
							fnew.createNewFile();
							FileOutputStream fsot = new FileOutputStream(fnew);
							byte[] buffer = new byte[1444]; 
							while ( (byteread = inStream.read(buffer)) != -1) { 
								bytesum += byteread; 
								fsot.write(buffer, 0, byteread);
							} 
							fsot.flush();
							inStream.close(); 
							fsot.close();
						}
					}
	    		}
	    		i++;
	    	}
    	}
    	try {
			final File result = new File(filepath+foldername+".zip");  
			createZipFile(filepath+foldername, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	        return foldername;
	}
	public String returnSuperior(String id){
	  Map params=new HashMap();
	  params.put(1,id);
	  String pid=DBUTilNew.getDataStr("parentid","select s.parentid from iwork_km_directory s where id= ? ",params);
	  return pid;
    }
  public List<HashMap> getgghfvotesj(String wjid){
	  List list = new ArrayList<HashMap>();
	  StringBuffer sql = new StringBuffer();
	  sql.append("  select t.directoryname,s.doc_name,z.file_url,y.lj from Iwork_Km_Directory t left join Iwork_Km_Doc s on t.id=s.directoryid left join sys_upload_file z on s.doc_enum=z.file_id ");
	  sql.append("  left join (select id,SYS_CONNECT_BY_PATH(directoryname,'\\') lj from Iwork_Km_Directory start with id= ? connect by prior id=parentid) y on y.id=t.id ");
	  sql.append("  where t.id in (select r.id from Iwork_Km_Directory r start with r.id= ?  connect by prior r.id=r.parentid ) ");
	  Connection conn =null;
	  PreparedStatement ps = null;
	  ResultSet rs=null;
	  try {
		conn=DBUtil.open();
		ps = conn.prepareStatement(sql.toString());
		ps.setString(1, wjid);
		ps.setString(2, wjid);
		rs = ps.executeQuery();
		while (rs.next()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String directoryname = rs.getString("directoryname");	
			String docname = rs.getString("doc_name");		
			String fileurl = rs.getString("file_url");		
			String lj = rs.getString("lj");			
			
			map.put("directoryname", directoryname);
			map.put("docname", docname==null?"":docname);
			map.put("fileurl", fileurl==null?"":fileurl);
			map.put("lj", lj);
			
			list.add(map);
		}
	 } catch (Exception e) {
		 e.printStackTrace();
	 } finally{
		DBUtil.close(conn, ps, null);
	 }	
	   return list;
  }
  public void down(String foldername){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String foldernames = "知识库批量下载文件";
		String filepath = request.getSession().getServletContext().getRealPath("/");
		//下载压缩文件
      try {
	        InputStream fis = new BufferedInputStream(new FileInputStream(filepath+foldername+".zip"));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(foldernames+".zip"));
	        
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
      } catch (Exception ex) {
      	
      }finally{
      	//删除文件夹、压缩文件文件
      	File filedel1 = new File(filepath+foldername);
      	delChange(filedel1);
      	File filedel2 = new File(filepath+foldername+".zip");
      	if(filedel2.exists()){
      		filedel2.delete();
      	}
      }
	}
  public void delChange(File file) {
      File[] files = file.listFiles();
      if (files != null)
          for (File f : files)
              delChange(f);
      file.delete();
  }
  public String changeUnit(Long fileSize)
  {
    String fileSizeStr = "";
    if (fileSize == null) {
      fileSizeStr = "0";
    }
    else if (fileSize.longValue() < 1024L) {
      fileSizeStr = fileSize + "B";
    }
    else if ((1024L < fileSize.longValue()) && (fileSize.longValue() < 1048576L)) {
      fileSizeStr = Math.round((float)(fileSize.longValue() / 1024L * 100L)) * 0.01D + "KB";
    }
    else if ((1048576L < fileSize.longValue()) && (fileSize.longValue() < 1073741824L)) {
      fileSizeStr = Math.round((float)(fileSize.longValue() / 1048576L * 100L)) * 0.01D + "MB";
    }
    else if ((1073741824L < fileSize.longValue()) && (fileSize.longValue() < 0L)) {
      fileSizeStr = Math.round((float)(fileSize.longValue() / 1073741824L * 100L)) * 0.01D + "GB";
    }

    return fileSizeStr;
  }

  private void createZipFile(final String rootPath, final File result) throws Exception {  
      File dstFolder = new File(result.getParent());  
      if (!dstFolder.isDirectory()) {  
          dstFolder.mkdirs();  
      }  
      File rootDir = new File(rootPath);  
      final ScatterSample scatterSample = new ScatterSample(rootDir.getAbsolutePath());  
      compressCurrentDirectory(rootDir, scatterSample);  
      final ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(result);  
      scatterSample.writeTo(zipArchiveOutputStream);  
      zipArchiveOutputStream.close();  
  }  
private void compressCurrentDirectory(File dir, ScatterSample scatterSample) throws Exception {  
      if (dir == null) {  
          throw new IOException("源路径不能为空！");  
      }  
      String relativePath = "";  
      if (dir.isFile()) {  
          relativePath = dir.getName();  
          addEntry(relativePath, dir, scatterSample);  
          return;  
      }  


      // 空文件夹  
      if (dir.listFiles().length == 0) {  
          relativePath = dir.getAbsolutePath().replace(scatterSample.getRootPath(), "");  
          addEntry(relativePath + File.separator, dir, scatterSample);  
          return;  
      }  
      for (File f : dir.listFiles()) {  
          if (f.isDirectory()) {  
              compressCurrentDirectory(f, scatterSample);  
          } else {  
              relativePath = f.getParent().replace(scatterSample.getRootPath(), "");  
              addEntry(relativePath + File.separator + f.getName(), f, scatterSample);  
          }  
      }  
  }  
private void addEntry(String entryName, File currentFile, ScatterSample scatterSample) throws Exception {  
      ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);  
      archiveEntry.setMethod(ZipEntry.DEFLATED);  
      final InputStreamSupplier supp = new CustomInputStreamSupplier(currentFile);  
      scatterSample.addEntry(archiveEntry, supp);  
  }  
class CustomInputStreamSupplier implements InputStreamSupplier {  
      private File currentFile;  


      public CustomInputStreamSupplier(File currentFile) {  
          this.currentFile = currentFile;  
      }  


      @Override  
      public InputStream get() {  
          try {  
              // InputStreamSupplier api says:  
              // 返回值：输入流。永远不能为Null,但可以是一个空的流  
              return currentFile.isDirectory() ? new NullInputStream(0) : new FileInputStream(currentFile);  
          } catch (FileNotFoundException e) {  
              e.printStackTrace();  
          }  
          return null;  
      }  
  }  
  public KMDocDAO getKmDocDAO()
  {
    return this.kmDocDAO;
  }
  public void setKmDocDAO(KMDocDAO kmDocDAO) {
    this.kmDocDAO = kmDocDAO;
  }
  public KMDirectoryDAO getKmDirectoryDAO() {
    return this.kmDirectoryDAO;
  }
  public void setKmDirectoryDAO(KMDirectoryDAO kmDirectoryDAO) {
    this.kmDirectoryDAO = kmDirectoryDAO;
  }
  public FileUploadDAO getUploadifyDAO() {
    return this.uploadifyDAO;
  }
  public void setUploadifyDAO(FileUploadDAO uploadifyDAO) {
    this.uploadifyDAO = uploadifyDAO;
  }
  public FileUploadUtil getFileUploadUtil() {
    return this.fileUploadUtil;
  }
  public void setFileUploadUtil(FileUploadUtil fileUploadUtil) {
    this.fileUploadUtil = fileUploadUtil;
  }
  public KMPurViewService getKmPurViewService() {
    return this.kmPurViewService;
  }
  public void setKmPurViewService(KMPurViewService kmPurViewService) {
    this.kmPurViewService = kmPurViewService;
  }
}