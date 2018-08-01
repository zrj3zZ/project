package com.iwork.plugs.itsArticles.event;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import com.iwork.app.conf.ServerConfigParser;
import com.iwork.app.schedule.IWorkScheduleInterface;
import com.iwork.app.schedule.ScheduleException;
import com.iwork.commons.util.FileReaderUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.plugs.itsArticles.dao.ItsArticlesDao;
import com.iwork.plugs.itsArticles.model.ItsArticles;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
public class ItsArticlesEvent implements IWorkScheduleInterface {
	private ItsArticlesDao itsArticlesDao;
	
	public boolean executeBefore() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-即将开始公司章程(制度)信息同步...... ");
		return true;
	}

	public boolean executeOn() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-即将开始公司章程(制度)信息同步...... ");
		AddItsArticles();
		return true;
	}

	public boolean executeAfter() throws ScheduleException {
		// TODO Auto-generated method stub
		System.out.println("服务启动=======================提示：["
				+ UtilDate.getNowDatetime() + "]-公司章程(制度)信息同步完毕...... ");
		return true;
	}
	
	public void AddItsArticles() {
		String rootPath = new File(ServerConfigParser.class.getResource("/").getPath()).getParent();
		List<HashMap> allList = DemAPI.getInstance().getAllList("187c11e8291148848fcbff24cbf6c17c", null, null);
		for (HashMap hash : allList) {
			Long dataId=Long.parseLong(hash.get("ID").toString());
			String zdmc=hash.get("ZDMC")==null?"":hash.get("ZDMC").toString();
			String FILETEXT = hash.get("ZDFJ").toString();
			List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(FILETEXT);
			if(itsArticlesDao==null)
				itsArticlesDao = (ItsArticlesDao)SpringBeanUtil.getBean("itsArticlesDao");
			List findByZdqdid = itsArticlesDao.findByZdqdid(dataId);
			for (FileUpload fileUpload : sublist) {
				String uuid = fileUpload.getFileId();
				boolean contains = findByZdqdid.contains(uuid);
				if(!contains){
					String fileSrcName = fileUpload.getFileSrcName();
					String extension = fileUpload.getFileSrcName().substring(fileUpload.getFileSrcName().lastIndexOf("."), fileUpload.getFileSrcName().length());
					if(extension.equals(".docx")||extension.equals(".doc")||extension.equals(".wps")){
						String fileUrl = fileUpload.getFileUrl().replace("/", "\\");
						String url=rootPath+fileUrl;
						String text="";
						text=FileReaderUtil.fileReader(extension, url);
						if(!"".equals(text)&&text!=null){
							ItsArticles ias=new ItsArticles();
							ias.setFileSrcName(fileSrcName);
							ias.setFileUUID(uuid);
							ias.setZdqdid(dataId);
							ias.setContent(text);
							itsArticlesDao.addBoData(ias);
						}else{
							String khmc = hash.get("KHMC").toString();
							String fileSaveName = fileUpload.getFileSaveName();
						}
					}else{
						String khmc = hash.get("KHMC").toString();
						String fileSaveName = fileUpload.getFileSaveName();
					}
				}
			}
		}
	}	
}
