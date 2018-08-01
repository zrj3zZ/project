package com.iwork.plugs.sms.util;



import org.hibernate.Query;
import org.hibernate.Session;

import com.iwork.core.util.HibernateUtil;

public class PageUtil {
private Page page;    //   分页逻辑处理的对象是页面实体
private int start;    // start是点击“上一页”或“下一页”传递进来的页码
private String hql;    // 检索的HQL语句

 public PageUtil(int start,String sql){

    // 实例化一个PageUtil对象。在这里调用了除了最后一个方法getPage()以外的，该PageUtil类的所有实现方法，具体实现可以看下面的方法实现

    // 注意：必须保证其中方法的顺序如下所示
   page = new Page();    // 初始化一个Page，即指定了pageSize=10，这个可以到Page类中自定义
   this.start = start;
   this.hql = sql;
   setRowCount();    // 设置page的属性rowCount值，即该次查询的总记录数
   setTotalPage();    // 设置总页数
   setCurrentPage();    // 设置当前页
   setPrePage();    //   设置上一页
   setNextPage();    //   设置下一页
   setPreOrNextBoolean();    // 设置是否有“上一页”或者是否有下一页的boolean型标识
  
}

  public void setPreOrNextBoolean(){     // 设置是否有“上一页”或者是否有下一页的boolean型标识
   if(page.getCurrentPage()<=1){    // 第一页时，没有上一页，则上一页链接失效
    page.setHasPreviousPage(false);
   }
   else{
    page.setHasPreviousPage(true);
   }
   if(page.getCurrentPage()>=page.getTotalPage()){    // 最后一页时，没有下一页，则下一页链接失效  
    page.setHasNextPage(false);
   }
   else{
    page.setHasNextPage(true);
   }
}

  public void setCurrentPage(){      // 设置当前页
   if(start<1){
    page.setCurrentPage(1);
   }
   if(start>page.getTotalPage()){
    page.setCurrentPage(page.getTotalPage());
   }
   page.setCurrentPage(start);
}

public void setPrePage(){       // 设置上一页
   page.setPrePage(page.getCurrentPage()-1);
}

public void setNextPage(){
   page.setNextPage(page.getCurrentPage()+1);
}

public void setTotalPage(){       // 设置总页数
   int rowCount = getRowCount();
   int pageSize = page.getPageSize();
   if(rowCount>pageSize){
    if(rowCount%pageSize == 0){
     page.setTotalPage(rowCount/pageSize);
    }
    else{
     page.setTotalPage(1+(rowCount/pageSize));
    }
   }
   else{
    page.setTotalPage(1);
   }
}

public void setRowCount(){     // 设置page的属性rowCount值，即该次查询的总记录数

   page.setRowCount(getRowCount());
}

public int getRowCount(){       // 获取总记录数
   Session session = HibernateUtil.getSession();
   Query query = session.createQuery(hql);       //   执行检索
   int size = query.list().size();
   session.close();
   return size;
}

public int getStartIndex(){       //   当传递进来一个页码，根据这个页码设置执行后台检索的起始索引
   int startIndex = 0;
   if(start<0){
    startIndex = 0;
   }
   else{
    if(start>page.getTotalPage()){
     startIndex = page.getPageSize()*(page.getTotalPage()-1);
    }
    else{
     startIndex = page.getPageSize()*(start-1);
    }
   }
   return startIndex;
} 
public void setTiao(int tiao1){
	int pagemax=page.getTotalPage();
	if(tiao1<1){
		page.setTiao(1);
	}
	if(tiao1>pagemax){
		page.setTiao(pagemax);
	}
	else{
		page.setTiao(tiao1);
	}
	
}

public Page getPage(){       //   这个方法不是在初始化PageUtil时调用的，而是初始化之后调用，返回一个页(主要是一个页所要显示的记录列表query.list())
   Session session = HibernateUtil.getSession();
   Query query = session.createQuery(hql);
   query.setFetchSize(10);
   query.setFirstResult(getStartIndex());
   query.setMaxResults(page.getPageSize());
   page.setList(query.list());
   session.close();
   return page;
}
}

