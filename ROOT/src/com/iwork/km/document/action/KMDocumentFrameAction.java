package com.iwork.km.document.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.ResponseUtil;
import com.iwork.km.document.service.KMDirectoryService;
import com.iwork.km.document.service.KMDocService;
import com.iwork.km.document.service.KMDocumentFrameService;
import com.opensymphony.xwork2.ActionSupport;

public class KMDocumentFrameAction extends ActionSupport
{
  public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
  private KMDocumentFrameService kmDocumentFrameService;
  private KMDirectoryService kmDirectoryService;
  private KMDocService kmDocService;
  private String kmEngerpriseTree;
  protected Collection folderItems;
  protected Collection fileItems;
  private Long directoryid;
  private Long parentid;
  private String id;
  private Long pid;
  private Long purviewType;
  private String nodeType;
  private String type;
  private String filename;
  private String flag;
  private String dir;

  public String getDir() {
	return dir;
}
public void setDir(String dir) {
	this.dir = dir;
}
public String getDirid() {
	return dirid;
}
public void setDirid(String dirid) {
	this.dirid = dirid;
}
private String dirid;

  public String getFlag() {
	return flag;
}
public void setFlag(String flag) {
	this.flag = flag;
}
public void down(){
	  kmDocumentFrameService.down(filename);
		
	}
	public void returnSuperior(){
        String json = this.kmDocumentFrameService.returnSuperior(id);
        ResponseUtil.write(json);
    }
  /**
   * 打包下载文件
   */
	public void downloadFile(){
		String filenames="";
		List<HashMap> list =this.kmDocumentFrameService.getgghfvotesj(id);
		try {
			 filenames=kmDocumentFrameService.votedownload(list);
		} catch (Exception e) {
		}
		ResponseUtil.write(filenames);
	}
  public String openDocumentFrame()   throws Exception {
	Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
	if(dir!=null && !"".equals(dir) && flag!=null && "1".equals(flag)){
		 OrgUser orguser = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		 if(dir.equals("ywgzDir")){
			 if(orguser.getOrgroleid()==3)  dirid = config.get("dmywzgDir");
			 else  dirid = config.get(dir);
		 }else dirid = config.get(dir);
		return "dmsy";
	}else
		return "success";
  }

  public void showTreeJson()
  {
    String json = this.kmDocumentFrameService.showTreeJson(this.directoryid);
    ResponseUtil.write(json);
  }
  public void showdmTreeJson()
  {
    String json = this.kmDocumentFrameService.showdmTreeJson(this.directoryid);
    ResponseUtil.write(json);
  }
  public void showGridJson()
  {
    if (this.directoryid == null) {
      this.directoryid = new Long(0L);
    }
    if (this.type == null) {
      this.type = "folder";
    }
    String json = this.kmDocumentFrameService.showGridJson(this.directoryid, this.type);
    json = json.substring(1, json.length() - 1);
    ResponseUtil.write(json);
  }

  public KMDocumentFrameService getKmDocumentFrameService()
  {
    return this.kmDocumentFrameService;
  }
  /**
	 * 查询培训材料
	 */
	public void getPxcl(){
		OrgUser user = UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		String knowHtml = kmDocumentFrameService.getPxcl();
		
		ResponseUtil.writeTextUTF8(knowHtml);
	}
	
  public void setKmDocumentFrameService(KMDocumentFrameService kmDocumentFrameService) {
    this.kmDocumentFrameService = kmDocumentFrameService;
  }
  public String getKmEngerpriseTree() {
    return this.kmEngerpriseTree;
  }
  public void setKmEngerpriseTree(String kmEngerpriseTree) {
    this.kmEngerpriseTree = kmEngerpriseTree;
  }
  public KMDirectoryService getKmDirectoryService() {
    return this.kmDirectoryService;
  }
  public void setKmDirectoryService(KMDirectoryService kmDirectoryService) {
    this.kmDirectoryService = kmDirectoryService;
  }
  public KMDocService getKmDocService() {
    return this.kmDocService;
  }
  public void setKmDocService(KMDocService kmDocService) {
    this.kmDocService = kmDocService;
  }
  public void setFolderItems(Collection folderItems) {
    this.folderItems = folderItems;
  }
  public void setFileItems(Collection fileItems) {
    this.fileItems = fileItems;
  }
  public void setDirectoryid(Long directoryid) {
    this.directoryid = directoryid;
  }
  public Collection getFolderItems() {
    return this.folderItems;
  }
  public Collection getFileItems() {
    return this.fileItems;
  }
  public Long getDirectoryid() {
    return this.directoryid;
  }
  public Long getParentid() {
    return this.parentid;
  }
  public void setParentid(Long parentid) {
    this.parentid = parentid;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getPurviewType() {
    return this.purviewType;
  }

  public void setPurviewType(Long purviewType) {
    this.purviewType = purviewType;
  }

  public String getNodeType() {
    return this.nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public Long getPid() {
    return this.pid;
  }

  public void setPid(Long pid) {
    this.pid = pid;
  }
public String getFilename() {
	return filename;
}
public void setFilename(String filename) {
	this.filename = filename;
}
}