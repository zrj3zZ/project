package com.ibpmsoft.project.zqb.action;

import java.util.*;

import com.ibpmsoft.project.zqb.service.ZqbFileUploadCommitService;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;

import javax.servlet.http.HttpServletResponse;

public class ZqbFileUploadCommitAction extends ActionSupport {
	private ZqbFileUploadCommitService zqbFileUploadCommitService;
	private String fileUUID;
	private List<HashMap> fileCommitList;
	private List<HashMap> fileList;
	private List<HashMap> fileDetailsList;
	private List<String> userNameList;
	private List<String> titleList;
	private String username;
	private String titleName;
	private String beginSj;
	private String endSj;
	private String uploadtime;
	private int pageNumber=1;
	private int pageSize=10;
	private int totalNum;
	private int filePageNumber=1;
	private int filePageSize=20;
	private int fileTotalNum;
	private String gnmk;
	private String gsmc;
	private String xfmk;
	private List downlist;
	private String filename;
	private String fileuuidlist;
	private String customername;
	private String zqdm;
	private String zqjc;
	private String gn;
	private String sja;
	private String sjb;
	private String chkvalue;
    private String xznr;
private Long qb;

    public Long getQb() {
        return qb;
    }

    public void setQb(Long qb) {
        this.qb = qb;
    }

    public String getXznr() {
        return xznr;
    }

    public void setXznr(String xznr) {
        this.xznr = xznr;
    }

	private Set gnlist;
	
	public Set getGnlist() {
		return gnlist;
	}

	public void setGnlist(Set gnlist) {
		this.gnlist = gnlist;
	}

	public String getChkvalue() {
		return chkvalue;
	}

	public void setChkvalue(String chkvalue) {
		this.chkvalue = chkvalue;
	}

	public String getZqjc() {
		return zqjc;
	}

	public void setZqjc(String zqjc) {
		this.zqjc = zqjc;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getZqdm() {
		return zqdm;
	}

	public void setZqdm(String zqdm) {
		this.zqdm = zqdm;
	}

	public String getGn() {
		return gn;
	}

	public void setGn(String gn) {
		this.gn = gn;
	}

	public String getSja() {
		return sja;
	}

	public void setSja(String sja) {
		this.sja = sja;
	}

	public String getSjb() {
		return sjb;
	}

	public void setSjb(String sjb) {
		this.sjb = sjb;
	}

	public String getFileuuidlist() {
		return fileuuidlist;
	}

	public void setFileuuidlist(String fileuuidlist) {
		this.fileuuidlist = fileuuidlist;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getGnmk() {
		return gnmk;
	}

	public void setGnmk(String gnmk) {
		this.gnmk = gnmk;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getXfmk() {
		return xfmk;
	}

	public void setXfmk(String xfmk) {
		this.xfmk = xfmk;
	}

	public List getDownlist() {
		return downlist;
	}

	public void setDownlist(List downlist) {
		this.downlist = downlist;
	}

	public void commitFile(){
		boolean flag=zqbFileUploadCommitService.saveFile(fileUUID);
	}
	
	public void removeFile(){
		boolean flag=zqbFileUploadCommitService.removeFile(fileUUID);
		if(flag){
			ResponseUtil.write("success");
		}else{
			ResponseUtil.write("error");
		}
	}
	
	public String index(){
		HashMap data = zqbFileUploadCommitService.getFileCommit(username,titleName,beginSj,endSj,filePageNumber,filePageSize);
		fileCommitList=(List<HashMap>)data.get("FILECOMMITLIST");
		fileTotalNum=(Integer)data.get("FILETOTALNUM");//zqbFileUploadCommitService.getFileCommitSize(username, titleName, beginSj, endSj);
		fileList=(List<HashMap>)data.get("FILELIST");//zqbFileUploadCommitService.getFileList();
		userNameList=zqbFileUploadCommitService.getUserNameList(fileList);
		titleList=zqbFileUploadCommitService.getTitleList(fileList);
		return SUCCESS;
	}
	
	public String getFileDetails(){
		HashMap data =zqbFileUploadCommitService.getFileDetails(username,titleName,uploadtime,pageNumber,pageSize);
		fileDetailsList = (List<HashMap>)data.get("fileDetailsList");
		totalNum = (Integer)data.get("totalNum");
		return SUCCESS;
	}

	public List<HashMap> getFileCommitList() {
		return fileCommitList;
	}

	public void setFileCommitList(List<HashMap> fileCommitList) {
		this.fileCommitList = fileCommitList;
	}

	public String getFileUUID() {
		return fileUUID;
	}

	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}

	public ZqbFileUploadCommitService getZqbFileUploadCommitService() {
		return zqbFileUploadCommitService;
	}

	public void setZqbFileUploadCommitService(
			ZqbFileUploadCommitService zqbFileUploadCommitService) {
		this.zqbFileUploadCommitService = zqbFileUploadCommitService;
	}

	public List<String> getUserNameList() {
		return userNameList;
	}

	public void setUserNameList(List<String> userNameList) {
		this.userNameList = userNameList;
	}

	public List<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBeginSj() {
		return beginSj;
	}

	public void setBeginSj(String beginSj) {
		this.beginSj = beginSj;
	}

	public String getEndSj() {
		return endSj;
	}

	public void setEndSj(String endSj) {
		this.endSj = endSj;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public List<HashMap> getFileDetailsList() {
		return fileDetailsList;
	}

	public void setFileDetailsList(List<HashMap> fileDetailsList) {
		this.fileDetailsList = fileDetailsList;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getFilePageNumber() {
		return filePageNumber;
	}

	public void setFilePageNumber(int filePageNumber) {
		this.filePageNumber = filePageNumber;
	}

	public int getFilePageSize() {
		return filePageSize;
	}

	public void setFilePageSize(int filePageSize) {
		this.filePageSize = filePageSize;
	}

	public int getFileTotalNum() {
		return fileTotalNum;
	}

	public void setFileTotalNum(int fileTotalNum) {
		this.fileTotalNum = fileTotalNum;
	}
	public String zqbFileListAndDownload(){
		return SUCCESS;
	}
	
	
	public void zqbFileDownloadZip(){
		zqbFileUploadCommitService.zqbFileDownloadZip(filename,fileuuidlist);
	}
	public void zqbFileDownloadZipChange(){
        customername="'"+customername+"'";
		zqbFileUploadCommitService.zqbFileDownloadZipChange2(customername,zqjc,zqdm,gn,sja,sjb,xznr);
	}

    /**
     * 压缩附件
     */
    public void zpbFileYsZip(){
        customername="'"+customername+"'";
        zqbFileUploadCommitService.zpbFileYsZip(customername,zqjc,zqdm,gn,sja,sjb,xznr,gsmc);
    }
    public void zqb_filedowm(){

        zqbFileUploadCommitService.dowmfile(filename);
    }
    public String zpbDownLoadItem(){
        downlist=zqbFileUploadCommitService.zpbDownLoadItem(pageSize,pageNumber);
        totalNum = zqbFileUploadCommitService.zpbDownLoadItemSize();
        return SUCCESS;
    }
	public String zqbFileDownloadChange(){
	    Map map =new HashMap();
	    map= zqbFileUploadCommitService.zqbFileDownloadListChange(customername,zqdm,gn,sja,sjb,pageNumber,pageSize,xznr);
		downlist=(List) map.get("list1");

        List<HashMap> list = (List<HashMap>) map.get("list2");
		gnlist = new HashSet();
		for (HashMap object : list) {
			if(object.get("GN")!=null){
				gnlist.add(object.get("GN"));
			}
		}
		return SUCCESS;
	}
	public void zqbFileDownloadList(){
		zqbFileUploadCommitService.zqbFileDownloadListZip(chkvalue,gn,sja,sjb,xznr,qb);
	}
	public void delfj(){
        zqbFileUploadCommitService.delfj(chkvalue,customername);
    }
	private List<String> getList(String userid) {
		List<String> orgRoleidList = zqbFileUploadCommitService.getList(userid);
		return orgRoleidList;
	}
}
