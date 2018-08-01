package com.iwork.plugs.email.fullsearch;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.apache.struts2.ServletActionContext;

import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.HtmlRegexpUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.eaglesearch.analyzer.lucene.IKAnalyzer;
import com.iwork.eaglesearch.impl.EaglesSearchAbst;
import com.iwork.eaglesearch.interceptor.EaglesSearchInterface;
import com.iwork.eaglesearch.model.CMSIndexModel;
import com.iwork.eaglesearch.model.IndexFormDataModel;
import com.iwork.eaglesearch.model.IndexModel;
import com.iwork.eaglesearch.pool.IndexReaderPool;
import com.iwork.eaglesearch.pool.IndexWriterPool;
import com.iwork.km.document.util.ReadFileUtil;
import com.iwork.plugs.email.constant.BoxTypeConst;
import com.iwork.plugs.email.dao.IWorkMailDAO;
import com.iwork.plugs.email.dao.IWorkMailOwnerDAO;
import com.iwork.plugs.email.dao.IWorkMailTaskDAO;
import com.iwork.plugs.email.model.EmailIndexModel;
import com.iwork.plugs.email.model.MailModel;
import com.iwork.plugs.email.model.MailOwnerModel;
import com.iwork.plugs.email.model.MailTaskModel;
import com.iwork.plugs.email.model.SearchModel;
import com.iwork.plugs.meeting.util.UtilDate;

/**
 * 邮箱全文检索
 * @author zouyalei
 *
 */
public class EaglesSearchEmailImpl  extends EaglesSearchAbst implements EaglesSearchInterface {
	public static final String indexDirName = "EMAIL"; 
	Highlighter highlighter = null;
	private IWorkMailDAO iWorkMailDAO;
	private static Logger logger = Logger.getLogger(EaglesSearchEmailImpl.class);
	public EaglesSearchEmailImpl(IndexReaderPool indexReaderPool,IndexWriterPool indexWriterPool,String type){
		super(indexReaderPool,indexWriterPool,type);
	}

	/**
	 * 添加索引
	 * @param content
	 * @return
	 */
	public boolean addDocument(IndexModel indexModel) {
		boolean flag = false; 

		return flag;
	}
	/**
	 * 删除索引
	 * @param content
	 * @return
	 */
	public boolean deleteDocument(IndexModel model) {
		boolean flag = false;

		return flag;
	}

	/**
	 * 更新索引
	 * @param content
	 * @return
	 */
	public boolean updateDocument(IndexModel indexModel) {
		boolean flag = false; 

		return flag;
	}



	/**
	 * 添加索引
	 * @param content
	 * @return
	 */
	public boolean addDocument(EmailIndexModel indexModel) {
		boolean flag = false; 
		if(indexModel!=null){
			//判断当前索引是否存在
			IndexModel  dataModel = this.searcherForId(indexModel.getId()+"");
			if(dataModel!=null){  
				return this.updateDocument(indexModel);
			}
			IndexWriterPool indexWriterPool = this.getIndexWriterPool();
			//获得索引池中，获得写索引对象
			IndexWriter indexWriter = indexWriterPool.getIndexWriter(indexDirName);
			Document doc= new Document(); 
			try{     
				doc = buildDocument(doc,indexModel);
				indexWriter.addDocument(doc);
				indexWriter.commit();
				this.getIndexReaderPool().init();
				flag = true;
			}catch(Exception e){
				logger.error(e,e);
			} 
		}
		return flag;
	}

	/**
	 * 构建文档结构
	 * @param doc
	 * @param instanceId
	 * @return
	 */
	private Document buildDocument(Document doc,EmailIndexModel model){
		if(doc!=null){ 
			doc.add(new Field(IndexModel.INDEX_ID,model.getId(),Field.Store.YES,Field.Index.NOT_ANALYZED));
			doc.add(new Field(IndexModel.INDEX_TYPE,model.getType(),Field.Store.YES,Field.Index.NOT_ANALYZED));
			doc.add(new Field(EmailIndexModel.EMAIL_DATA_OWNER,model.getOwner(),Field.Store.YES,Field.Index.NOT_ANALYZED));
			doc.add(new Field(EmailIndexModel.EMAIL_DATA_MAILBOX,model.getMailBox()+"",Field.Store.YES,Field.Index.NOT_ANALYZED));
			doc.add(new NumericField(EmailIndexModel.EMAIL_DATA_ID,Field.Store.YES,true).setLongValue(model.getMailid()));
			if(model.getCreateDate()==null){
				model.setCreateDate(Calendar.getInstance().getTime());
			}
			//			doc.add(new NumericField(EmailIndexModel.EMAIL_DATA_CREATEDATE, Field.Store.YES,true).setDoubleValue(model.getCreateDate().getTime()));
			//			doc.add(new Field(EmailIndexModel.EMAIL_DATA_CREATEDATE,UtilDate.dateFormat(model.getCreateDate()),Field.Store.YES,Field.Index.ANALYZED));
			doc.add(new Field(EmailIndexModel.EMAIL_DATA_CREATEDATE,UtilDate.dateFormat(model.getCreateDate()), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("createNumber",model.getCreateDate().getTime()+"", Field.Store.YES, Field.Index.NOT_ANALYZED));
			if(model.getCclist()!=null){
				String cclist = model.getCclist().replace("[", "").replace("]","");
				doc.add(new Field(EmailIndexModel.EMAIL_DATA_CC,cclist,Field.Store.YES,Field.Index.ANALYZED));
			}
			if(model.getMailFrom()!=null){ 
				String mailFrom = model.getMailFrom().replace("[", "").replace("]","");
				doc.add(new Field(EmailIndexModel.EMAIL_DATA_FROM,mailFrom,Field.Store.YES,Field.Index.ANALYZED));
			}
			if(model.getMailTo()!=null){
				String mailTo = model.getMailTo().replace("[", "").replace("]","");
				doc.add(new Field(EmailIndexModel.EMAIL_DATA_TO,mailTo,Field.Store.YES,Field.Index.ANALYZED));
			}
			doc.add(new Field(EmailIndexModel.INDEX_TITLE,model.getTitle(),Field.Store.YES,Field.Index.ANALYZED));
			if(model.getContent()!=null){
				String content = HtmlRegexpUtil.filterHtml(model.getContent()); 
				doc.add(new Field(EmailIndexModel.INDEX_CONTENT,content,Field.Store.YES,Field.Index.ANALYZED)); 
			} 
			if(model.getAttach()!=null){
				doc.add(new Field(EmailIndexModel.EMAIL_DATA_ATTACH,model.getAttach(),Field.Store.YES,Field.Index.ANALYZED));
			}
		}
		return doc;
	}

	/**
	 * 删除索引
	 * @param content
	 * @return
	 */
	public boolean deleteDocument(EmailIndexModel model) {
		boolean flag = false;
		if(model!=null){
			IndexWriterPool indexWriterPool = this.getIndexWriterPool();
			//获得索引池中，获得写索引对象
			IndexWriter indexWriter = indexWriterPool.getIndexWriter(indexDirName);
			try{
				TermQuery query = new TermQuery(new Term(EmailIndexModel.INDEX_ID,model.getId()+""));
				indexWriter.deleteDocuments(query);
				indexWriter.forceMerge(100);   
				indexWriter.commit();
				flag = true;
			}catch(Exception e){logger.error(e,e);
				flag = false;
			}
		} 
		return flag;
	}


	/**
	 * 更新索引
	 * @param content
	 * @return
	 */
	public boolean updateDocument(EmailIndexModel indexModel) {
		boolean flag = false; 
		if(indexModel!=null){
			IndexWriterPool indexWriterPool = this.getIndexWriterPool();
			//获得索引池中，获得写索引对象
			IndexWriter indexWriter = indexWriterPool.getIndexWriter(indexDirName);
			Document doc= new Document(); 
			try{     
				doc = buildDocument(doc,indexModel);
				indexWriter.updateDocument(new Term(IndexModel.INDEX_ID,indexModel.getId()), doc);
				indexWriter.commit();
				this.getIndexReaderPool().init();
				flag = true; 
			}catch(Exception e){
				logger.error(e,e);
			} 
		}
		return flag;
	}


	/**
	 * 
	 * @param keyword
	 * @param boxType
	 * @param owner
	 * @param recever
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<EmailIndexModel> searcher(SearchModel sm){
		List<EmailIndexModel> list = null;
		try{  
			list = new ArrayList();
			long startTime = System.currentTimeMillis();
			IndexReaderPool indexReaderPool = 	this.getIndexReaderPool();
			IndexReader indexReader = indexReaderPool.getIndexReader(indexDirName);
			if(indexReader==null)return null;
			Analyzer analyzer = new IKAnalyzer();
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			BooleanQuery booleanQuery = new BooleanQuery(); 
			//所有人
			Query ownerQuery=new TermQuery(new Term(EmailIndexModel.EMAIL_DATA_OWNER,userid));
			booleanQuery.add(ownerQuery, BooleanClause.Occur.MUST);
			if(sm.getFolderid()!=null){
				if(sm.getFolderid().equals("all")){

				}else{
					Term term3 = new Term(EmailIndexModel.EMAIL_DATA_MAILBOX,sm.getFolderid());
					TermQuery mailBox = new TermQuery(term3);
					BooleanClause clause=new BooleanClause(mailBox, BooleanClause.Occur.MUST);   
					booleanQuery.add(clause);      
				}
			}

			if(sm.getSender()!=null&&!"".equals(sm.getSender())){
				String[] field = {EmailIndexModel.EMAIL_DATA_FROM};
				QueryParser parse = new MultiFieldQueryParser(Version.LUCENE_36,field,analyzer);   
				String sender = UserContextUtil.getInstance().getUserId(sm.getSender());
				if(sender==null)sender = sm.getSender();
				Query query = parse.parse(sender);
				booleanQuery.add(query, BooleanClause.Occur.MUST); 
			}

			if(sm.getRecever()!=null&&!"".equals(sm.getRecever())){
				String sender = UserContextUtil.getInstance().getUserId(sm.getRecever());
				if(sender==null)sender = sm.getRecever();
				String[] field = {EmailIndexModel.EMAIL_DATA_TO,EmailIndexModel.EMAIL_DATA_CC};
				QueryParser parse = new MultiFieldQueryParser(Version.LUCENE_36,field,analyzer);   
				Query query = parse.parse(sender);
				booleanQuery.add(query, BooleanClause.Occur.MUST); 
			}
			if(sm.getKeyword()!=null&&!"".equals(sm.getKeyword())){
				if(sm.getPosition().equals("0")){
					String[] field = {EmailIndexModel.INDEX_TITLE,EmailIndexModel.INDEX_CONTENT,EmailIndexModel.EMAIL_DATA_ATTACH};
					QueryParser parse = new MultiFieldQueryParser(Version.LUCENE_36,field,analyzer);   
					Query query = parse.parse(sm.getKeyword());
					booleanQuery.add(query, BooleanClause.Occur.MUST); 
				}else if(sm.getPosition().equals("2")){
//					Term term4 = new Term(EmailIndexModel.INDEX_TITLE,sm.getKeyword());
//					TermQuery parserTitle = new TermQuery(term4);
//					booleanQuery.add(parserTitle, BooleanClause.Occur.MUST);
					String[] field = {EmailIndexModel.INDEX_TITLE,EmailIndexModel.EMAIL_DATA_ATTACH};//改为拆词
					QueryParser parse = new MultiFieldQueryParser(Version.LUCENE_36,field,analyzer);   
					Query query = parse.parse(sm.getKeyword());
					booleanQuery.add(query, BooleanClause.Occur.MUST);
				}else if(sm.getPosition().equals("1")){
					//					 Term term4 = new Term(EmailIndexModel.INDEX_CONTENT,sm.getKeyword());
					//					 TermQuery parserTitle = new TermQuery(term4);
					//				        booleanQuery.add(parserTitle, BooleanClause.Occur.MUST);
					String[] field = {EmailIndexModel.INDEX_CONTENT,EmailIndexModel.EMAIL_DATA_ATTACH};//改为拆词
					QueryParser parse = new MultiFieldQueryParser(Version.LUCENE_36,field,analyzer);   
					Query query = parse.parse(sm.getKeyword());
					booleanQuery.add(query, BooleanClause.Occur.MUST); 
				}else if(sm.getPosition().equals("3")){
					Term term4 = new Term(EmailIndexModel.EMAIL_DATA_ATTACH,sm.getKeyword());
					TermQuery parserTitle = new TermQuery(term4); 
					booleanQuery.add(parserTitle, BooleanClause.Occur.MUST);
				}

			}

			if(sm.getBegindate()!=null){
				//按日期范围搜索(对日期搜索)  
				if(sm.getEnddate()==null)sm.setEnddate(Calendar.getInstance().getTime());
				Query query5 = new TermRangeQuery(EmailIndexModel.EMAIL_DATA_CREATEDATE,UtilDate.dateFormat(sm.getBegindate()), UtilDate.dateFormat(sm.getEnddate()), true, true);  
				booleanQuery.add(query5, BooleanClause.Occur.MUST);  
			}

			IndexSearcher search = new IndexSearcher(indexReader);  
			Sort sort = new Sort(new SortField[]{new SortField("createNumber", SortField.DOC, false)});
			TopDocs it = search.search(booleanQuery,100,sort);  
			//高亮显示设置  
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style=\"color:red\"> ","</span>");
			highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(booleanQuery));
			highlighter.setTextFragmenter(new SimpleFragmenter(150));//这个100是指定关键字字符串的context的长度，你可以自己设定，因为不可能返回整篇正文内容
			ScoreDoc[] docs = it.scoreDocs; 
			for (int i = 0; i < docs.length; i++) {   
				EmailIndexModel model = new EmailIndexModel();
				Document targetDoc = search.doc(docs[i].doc);
				//装载文档标题
				String id = targetDoc.get(EmailIndexModel.INDEX_ID);
				if(id!=null){
					id = id.replace("s_","");
					id = id.replace("r_","");
					id = id.replace("o_","");
					model.setId(id);
				}

				String dataFrom = targetDoc.get(EmailIndexModel.EMAIL_DATA_FROM);
				if(dataFrom!=null)model.setMailFrom(dataFrom);
				String dataCC = targetDoc.get(EmailIndexModel.EMAIL_DATA_CC);
				if(dataCC!=null)model.setCclist(dataCC);
				String dataAttach = targetDoc.get(EmailIndexModel.EMAIL_DATA_ATTACH);
				if(dataAttach!=null)model.setAttach(dataAttach);
				String owner = targetDoc.get(EmailIndexModel.EMAIL_DATA_OWNER);
				if(owner!=null)model.setOwner(owner);
				String dataTo = targetDoc.get(EmailIndexModel.EMAIL_DATA_TO);
				if(dataTo!=null)model.setMailTo(dataTo);
				String boxType = targetDoc.get(EmailIndexModel.EMAIL_DATA_MAILBOX);
				if(boxType!=null)model.setMailBox(Long.parseLong(boxType));
				String mailId = targetDoc.get(EmailIndexModel.EMAIL_DATA_MAILID);
				if(mailId!=null){
					model.setMailid(Long.parseLong(mailId));
				}
				//装载标题
				String title = targetDoc.get(EmailIndexModel.INDEX_TITLE);

				if(boxType!=null&&boxType.equals(BoxTypeConst.TYPE_SEND+"")){
					title= "[发件箱]"+title;
				}else if(boxType!=null&&boxType.equals(BoxTypeConst.TYPE_TRANSACT+"")){
					title= "[收件箱]"+title;
				}else{
					title= "[草件箱]"+title;
				}

				model.setTitle(title);
				String temp = "";
				String content = targetDoc.get(EmailIndexModel.INDEX_CONTENT);
				if(content.length()>200){
					temp = content.substring(0, 200)+"...";
				}else{
					temp = content;
				}
				model.setContent(temp);
				//装载正文
				model.setType("<img src=\"iwork_img/newspaper.png\" border=\"0\"\">内部邮箱 "); 
				list.add(model);
			} 
			search.close();
		}catch(Exception e){ 
			logger.error(e,e);
		} 
		return list;
	}
	/**
	 * 执行检索
	 */
	public List searcher(String text) {		
		List<IndexModel> list = null;
		try{  
			list = new ArrayList();
			IndexReaderPool indexReaderPool = 	this.getIndexReaderPool();
			IndexReader indexReader = indexReaderPool.getIndexReader(indexDirName);
			if(indexReader==null)return null;
			IndexSearcher search = new IndexSearcher(indexReader);  
			Analyzer analyzer = new IKAnalyzer();
			String[] field = {EmailIndexModel.INDEX_TITLE,EmailIndexModel.INDEX_CONTENT};
			QueryParser parse = new MultiFieldQueryParser(Version.LUCENE_36,field,analyzer);   
			Query query = parse.parse(text);
			TopDocs it = search.search(query,100);  
			//高亮显示设置  
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span class=\"select_index\">","</span>");
			highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));
			highlighter.setTextFragmenter(new SimpleFragmenter(150));//这个100是指定关键字字符串的context的长度，你可以自己设定，因为不可能返回整篇正文内容
			ScoreDoc[] docs = it.scoreDocs; 
			for (int i = 0; i < docs.length; i++) {   
				IndexModel model = new IndexModel(); 
				Document targetDoc = search.doc(docs[i].doc);
				//装载文档标题
				String id = targetDoc.get(EmailIndexModel.INDEX_ID);
				model.setId(id);
				//装载标题
				String title = targetDoc.get(CMSIndexModel.INDEX_TITLE);
				if(title!=null){
					String temp = highlighter.getBestFragment(analyzer,IndexFormDataModel.INDEX_TITLE,title); 
					if(temp!=null){ 
						title = temp;
					}
				}
				model.setTitle(title);
				String temp = "";
				String content = targetDoc.get(IndexModel.INDEX_CONTENT);
				if(content.length()>200){
					temp = content.substring(0, 200)+"...";
				}else{
					temp = content;
				}
				model.setContent(temp);
				//装载正文
				model.setType("<img src=\"iwork_img/newspaper.png\" border=\"0\"\">内部邮箱"); 
				list.add(model);
			} 
			search.close();	 
		}catch(Exception e){ 
			logger.error(e,e);
		} 
		return list;
	}
	/**
	 * 设置索引内容
	 * @param model
	 * @return
	 */
	public String getIndexModelContent(FileUpload fileUpload) {
		String content = "附件不存在";
		if (fileUpload != null) {
			String srcName = fileUpload.getFileSrcName();
			if (srcName != null && srcName.lastIndexOf(".") >= 0) {
				String extName = srcName.substring(srcName.lastIndexOf(".") + 1).toLowerCase(); // 后缀名
				if (extName.equals("txt") || extName.equals("pdf")
						|| extName.equals("xls") || extName.equals("xlsx")
						|| extName.equals("doc") || extName.equals("docx")
						|| extName.equals("xml") || extName.equals("ppt")
						|| extName.equals("htm") || extName.equals("html")
						|| extName.equals("dps") || extName.equals("et")
						|| extName.equals("wps")) {
					String rootPath = ServletActionContext.getServletContext().getRealPath("\\");
					String realPath = rootPath+ fileUpload.getFileUrl().replaceAll("\\.\\.", "");
					content = ReadFileUtil.handleFile(realPath, extName);
				} else {
					content = "&nbsp;&nbsp;"; // 占个位
				}
			}
		}
		if (content != null) {
			if (content.equals("附件不存在")) {
				content = "";
			}
			if ("".equals(content)) {
				content = "&nbsp;&nbsp;"; // 占个位
			}
		}
		return content;
	}

	/**
	 * 重新构建索引
	 */
	public boolean reBuildIndex(){
		// 邮件信息DAO
		IWorkMailDAO iWorkMailDAO = (IWorkMailDAO)SpringBeanUtil.getBean("iWorkMailDAO");
		// 收件箱DAO
		IWorkMailOwnerDAO iwokMailOwnerDAO = (IWorkMailOwnerDAO) SpringBeanUtil.getBean("iWorkMailOwnerDAO"); 
		// 发件箱DAO
		IWorkMailTaskDAO iWorkMailTaskDAO = (IWorkMailTaskDAO) SpringBeanUtil.getBean("iWorkMailTaskDAO");

		OrgDepartmentDAO orgDepartmentDAO = (OrgDepartmentDAO) SpringBeanUtil.getBean("orgDepartmentDAO");
		OrgUserDAO orgUserDAO = (OrgUserDAO) SpringBeanUtil.getBean("orgUserDAO");
		List<OrgDepartment> deptlist = orgDepartmentDAO.getAll();
		for(OrgDepartment dept:deptlist){
			List<OrgUser> userlist = orgUserDAO.getActiveUserList(dept.getId());
			for(OrgUser user:userlist){
				String userid = user.getUserid();
				//初始化发件箱
				List<MailOwnerModel>  ownerlist  = iwokMailOwnerDAO.getSendListEmails(userid);
				for(MailOwnerModel ownerModel:ownerlist){
					MailModel mm = iWorkMailDAO.getMailModelById(ownerModel.getBindId());
					SearchIndexUtil.getInstance().addDocIndex(ownerModel.getId(),ownerModel.getOwner(), BoxTypeConst.TYPE_SEND, mm);
				}

				//收件箱
				// 查询发件箱信息
				List<MailTaskModel> mailTaskModelList = iWorkMailTaskDAO.getReceiveListEmails(userid);
				for(MailTaskModel mailTaskModel:mailTaskModelList){
					MailModel mm = iWorkMailDAO.getMailModelById(mailTaskModel.getBindId());
					SearchIndexUtil.getInstance().addDocIndex(mailTaskModel.getId(),mailTaskModel.getOwner(), BoxTypeConst.TYPE_TRANSACT, mm);
				}
			}
		}
		return false;
	}


	/**
	 * 根据ID查询邮件信息
	 * @param id
	 * @return
	 */
	public EmailIndexModel getEmailInfoById(String id){
		EmailIndexModel indexModel = null;
		if(id.split(",")[0].equals("")){
			// 邮件信息DAO
			if(iWorkMailDAO==null){
				iWorkMailDAO = (IWorkMailDAO)SpringBeanUtil.getBean("iWorkMailDAO");
			}
			indexModel = new EmailIndexModel();
			indexModel.setId(id);
			MailModel model = iWorkMailDAO.getMailModelById(Long.parseLong(id));
			String mailFrom = model.get_mailFrom();
			indexModel.setMailFrom(mailFrom);
			String mailTo = model.get_to();
			indexModel.setMailTo(mailTo);
			indexModel.setCclist(model.get_cc());
			String mailContent = model.get_content();
			if(mailContent!=null){
				indexModel.setContent(mailContent);
			}
			indexModel.setAttach(model.get_attachments());
		}
		return indexModel;
	}
}
