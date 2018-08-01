

package com.iwork.plugs.email.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.iwork.commons.util.DBUtilInjection;
import com.iwork.core.util.SequenceUtil;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.util.EmailContentUtil;

public  class IWorkMailDAO extends HibernateDaoSupport{
	private static final String SEQUENCE_MAILBOX = "_MAILBOX";
	private Class<MailModel> entityClass;
	
	/**
	 * 添加邮件信息
	 * @param model
	 */
	public boolean save(MailModel model){
		boolean flag = false;
		if(model!=null){
			if(model.get_id()==null){
				model._id = new Long(this.getSequenceValue());
				if(model.get_content()!=null&&!"".equals(model.get_content())){
					String pathname = EmailContentUtil.getInstance().saveText(model._id+"", model.get_content());
					if(pathname!=null){
						model.set_content(pathname);
					}
				}
				this.getHibernateTemplate().save(model);
			}else{
				String pathname = EmailContentUtil.getInstance().saveText(model._id+"", model.get_content());
				if(pathname!=null){
					model.set_content(pathname);
				}
				this.getHibernateTemplate().update(model);
			}
			
		    flag = true;
		}
		return flag;
	}
	
	/**
	 * 添加收件箱信息
	 * @param model
	 */
	public boolean save(MailOwnerModel model){
		boolean flag = false;
		if(model!=null){
			
			model.id = new Long(this.getSequenceValue());
			this.getHibernateTemplate().save(model);
		    flag = true;
		}
		return flag;
	}
	
	/**
	 * 删除邮件信息
	 * @author wenpengyu 
	 * */
   public boolean delete(MailModel model){
	   boolean flag = false;
	   if(model!=null){
		   this.getHibernateTemplate().delete(model);
		   flag = true;
	   }

	   
	   return flag;
   }

   /**
    * 按id查询相应的邮件信息
    * @author 杨连峰
    * */
   public MailModel searchEmail(Long id){
	   String sql="FROM MailModel where id='"+id+"'";
	   MailModel mailModel = new MailModel();
	   //按id查找邮件箱中的当前记录数的所有属性值
	    List<MailModel> list =  this.getHibernateTemplate().find(sql);
	    for(MailModel Model: list){
	    	mailModel = Model;
	    }
		return mailModel;
	   
	   /*MailModel mailModel = (MailModel) this.getHibernateTemplate().get(entityClass, id);
	   mailModel.get_content();
	   mailModel.get_createDate();
	   mailModel.get_id();
	   mailModel.get_isSend();
	   mailModel.get_title();

	   return mailModel;		*/
	}
  

   /**
    * 按id查询相应的邮件信息
    * @author wenpengyu
    * 
    **/
   public List<MailModel> findById(long id){
	   String sql = "From MailModel where  _id = '"+id+"'"; 
	  
	  List<MailModel> list = this.getHibernateTemplate().find(sql);
	   //MailModel instance = (MailModel) this.getHibernateTemplate().get(entityClass, id);
	   return list;		
	}
   /**
    *按照id查找邮件信息
    * @author wenpengyu
    * */
	public MailModel getMailModelById(long id){
		String sql="FROM MailModel where _id='"+id+"'";
		MailModel model = new MailModel();
	    List<MailModel> list =  this.getHibernateTemplate().find(sql);
	    if(list!=null && list.size()>0){
		    for(MailModel taskModel: list){
		    	model = taskModel;
		    	if(model!=null&&model.get_content()!=null){
		    		String pathname = model.get_content();
		    		String content = EmailContentUtil.getInstance().getText(model.get_id()+"",pathname);
		    		model.set_content(content);
		    	}
		    }
	    }
		return model;
	}
   
	//查询所用
	public List<MailModel> searchAll() {

		String hql = "From MailModel";

		return this.getHibernateTemplate().find(hql);

	}
	
	public void update(MailTaskModel model) {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().update(model);
		
	}
   
	public List<MailModel> getMailModelList(String taskid){
		String sql = "from MailTaskModel where id = ? ";
		DBUtilInjection d=new DBUtilInjection();
	    List lis=new ArrayList(); 
	    if ((taskid != null) && (!"".equals(taskid.trim()))) {
        	if(d.HasInjectionData(taskid)){
				return lis;
			}
          
        }
		Object[] values = {taskid};
		List<MailTaskModel> bindIdList = this.getHibernateTemplate().find(sql,values);
		List list = new ArrayList();
		MailModel mail = null;
		for(MailTaskModel mailModel:bindIdList){
			mail = getMailModelById(mailModel.getBindId());
			list.add(mail);
		}
	  return list;
	}
   
	public int getSequenceValue(){
		return SequenceUtil.getInstance().getSequenceIndex(SEQUENCE_MAILBOX);
	}
}
