package com.ibpmsoft.project.zqb.trigger;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.util.FileReaderUtil;
import com.iwork.commons.util.ParameterMapUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.dem.trigger.DemTriggerEvent;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.itsArticles.dao.ItsArticlesDao;
import com.iwork.plugs.itsArticles.model.ItsArticles;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;

public class ZQB_Zdqd_BaseAddEvent extends DemTriggerEvent {
	private ItsArticlesDao itsArticlesDao;
	
	public ZQB_Zdqd_BaseAddEvent(UserContext me, HashMap hash) {
		super(me, hash);
	}

	/**
	 * 执行触发器
	 */
	public boolean execute() {
		// 插入流程表，并将数据更新到流程表中
		HashMap formData = this.getFormData();
		HashMap map = ParameterMapUtil.getParameterMap(formData);
		HashMap hash = DemAPI.getInstance().getFromData(this.getInstanceId(),
				EngineConstants.SYS_INSTANCE_TYPE_DEM);
		String value=hash.get("ZDMC")==null?"":hash.get("ZDMC").toString();
		Long dataId=Long.parseLong(hash.get("ID").toString());
		LogUtil.getInstance().addLog(dataId, "公司章程(制度)", "添加/修改公司章程："+value);
		String FILETEXT = hash.get("ZDFJ").toString();
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(FILETEXT);
		String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
		if(itsArticlesDao==null)
			itsArticlesDao = (ItsArticlesDao)SpringBeanUtil.getBean("itsArticlesDao");
		List findByZdqdid = itsArticlesDao.findByZdqdid(dataId);
		for (FileUpload fileUpload : sublist) {
			String uuid = fileUpload.getFileId();
			boolean contains = findByZdqdid.contains(uuid);
			if(!contains){
				String fileSrcName = fileUpload.getFileSrcName();
				String extension = fileUpload.getFileSrcName().substring(fileUpload.getFileSrcName().lastIndexOf("."), fileUpload.getFileSrcName().length());
				String fileUrl = fileUpload.getFileUrl().replace("/", "\\");
				String url=rootPath+"\\"+fileUrl;
				String text="";
				text=FileReaderUtil.fileReader(extension, url);
				ItsArticles ias=new ItsArticles();
				ias.setFileSrcName(fileSrcName);
				ias.setFileUUID(uuid);
				ias.setZdqdid(dataId);
				ias.setContent(text);
				itsArticlesDao.addBoData(ias);
			}
		}
		return true;
	}
}
