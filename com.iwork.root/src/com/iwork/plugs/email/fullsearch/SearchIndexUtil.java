package com.iwork.plugs.email.fullsearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.eaglesearch.constant.EaglesSearchConstant;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.model.EmailIndexModel;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.sdk.FileUploadAPI;

public class SearchIndexUtil {
	 private static SearchIndexUtil instance;  
	 private static Object lock = new Object();
	 private EaglesSearchEmailImpl esfdi;
	 public static SearchIndexUtil getInstance(){  
	        if (instance == null){  
	            synchronized( lock ){  
	                if (instance == null){  
	                    instance = new SearchIndexUtil();  
	                }
	            }  
	        }  
	        return instance;  
	 }
	 
	public boolean addDocIndex(Long id,String owner,Long mailBox,MailModel model){
		//判断是否是调试状态
		boolean flag = false;
		if(esfdi==null){
			 esfdi =  (EaglesSearchEmailImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_EMAIL_INDEX);
		}
			class MailIndexThread extends Thread {
				private Long id;
				private String owner;
				private Long mailBox;
				private Date createDate;
				private MailModel model;
				MailIndexThread(Long id,String owner,Long mailBox,MailModel model) {
					this.id = id; 
					this.owner = owner;
					this.mailBox = mailBox;
					this.model = model;
				}
				
				public void run() {
					EmailIndexModel eim = new EmailIndexModel();
					eim.setTitle(model.get_title());
					eim.setMailBox(mailBox);
					eim.setContent(model.get_content()); 
					eim.setType(EaglesSearchConstant.EAGLES_SEARCH_TYPE_EMAIL_INDEX);
					eim.setMailid(model.get_id());
					if(mailBox.equals(BoxTypeConst.TYPE_SEND)||mailBox.equals(BoxTypeConst.TYPE_DRAFT)){
						eim.setId("s_"+id);
					}else if(mailBox.equals(BoxTypeConst.TYPE_TRANSACT)){
						eim.setId("r_"+id);
					}else{
						eim.setId("o_"+id);
					}
					createDate = Calendar.getInstance().getTime();
					eim.setCreateDate(createDate); 
					eim.setMailFrom(model.get_mailFrom());
					eim.setMailTo(model.get_to());
					eim.setCclist(model.get_cc());
					eim.setOwner(owner);
					StringBuffer filestr = new StringBuffer();
					if(model.get_attachments()!=null){
						 List<FileUpload> filelist = FileUploadAPI.getInstance().getFileUploads(model.get_attachments());
						 List<String> attach = new ArrayList();
						 for(FileUpload upload:filelist){
							 filestr.append(upload.getFileSaveName());
						 }
						 eim.setAttach(filestr.toString());
					}
					esfdi.addDocument(eim);
				}
			} 
			new MailIndexThread(id,owner, mailBox,model).start();
			flag = true;
		return flag;
	}
	
	
	public boolean removeDocIndex(Long id,Long mailBox){
		//判断是否是调试状态
		boolean flag = false;
		if(esfdi==null){
			esfdi =  (EaglesSearchEmailImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_EMAIL_INDEX);
		}
		class MailIndexThread extends Thread {
			private Long id;
			private Long mailBox;
			MailIndexThread(Long id,Long mailBox) {
				this.id = id; 
				this.mailBox = mailBox;
			}
			 
			public void run() {
				EmailIndexModel eim = new EmailIndexModel();
				eim.setMailBox(mailBox);
				eim.setType(EaglesSearchConstant.EAGLES_SEARCH_TYPE_EMAIL_INDEX);
				if(mailBox.equals(BoxTypeConst.TYPE_SEND)||mailBox.equals(BoxTypeConst.TYPE_DRAFT)){
					eim.setId("s_"+id);
				}else if(mailBox.equals(BoxTypeConst.TYPE_TRANSACT)){
					eim.setId("r_"+id);
				}else{
					eim.setId("o_"+id);
				}
				esfdi.deleteDocument(eim);
			}
		} 
		new MailIndexThread(id,mailBox).start();
		flag = true;
		return flag;
	}
	
}
