package com.iwork.eaglesearch.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;

import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.dao.SysInstanceDataDAO;
import com.iwork.core.engine.iform.model.SysInstanceData;
import com.iwork.core.engine.iform.service.IFormService;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.eaglesearch.analyzer.lucene.IKAnalyzer;
import com.iwork.eaglesearch.filter.EaglesSearchFormFilter;
import com.iwork.eaglesearch.interceptor.EaglesSearchInterface;
import com.iwork.eaglesearch.model.IndexFormDataModel;
import com.iwork.eaglesearch.model.IndexModel;
import com.iwork.eaglesearch.pool.IndexReaderPool;
import com.iwork.eaglesearch.pool.IndexWriterPool;

public class EaglesSearchFormDataImpl extends EaglesSearchAbst
  implements EaglesSearchInterface
{
  private final String indexDirName = "FORMINDEX";
  Highlighter highlighter = null;

  public EaglesSearchFormDataImpl(IndexReaderPool indexReaderPool, IndexWriterPool indexWriterPool, String type) { super(indexReaderPool, indexWriterPool, type); }


  public boolean addDocument(IndexFormDataModel model)
  {
    boolean flag = false;
    if (model != null)
    {
      IndexModel indexModel = searcherForId(model.getId());
      if (indexModel != null) {
        return updateDocument(model);
      }
      IndexWriterPool indexWriterPool = getIndexWriterPool();

      IndexWriter indexWriter = indexWriterPool.getIndexWriter("FORMINDEX");
      Document doc = new Document();
      try {
        if (model.getId() == null) {
          return false;
        }
        doc.add(new Field("ID", model.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("type", "FORMINDEX", Field.Store.YES, Field.Index.NOT_ANALYZED));
        if (model.getActDefId() != null) {
          doc.add(new Field("ACT_DEF_ID", model.getActDefId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getActDefStepId() != null) {
          doc.add(new Field("ACT_DEFSTEP_ID", model.getActDefStepId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getTaskId() != null) {
          doc.add(new Field("taskId", model.getTaskId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getInstanceId() != null) {
          doc.add(new Field("instanceId", model.getInstanceId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getExcuteId() != null) {
          doc.add(new Field("excuteId", model.getExcuteId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getTitle() != null) {
          doc.add(new Field("title", model.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (model.getOwner() != null) {
          doc.add(new Field("owner", model.getOwner(), Field.Store.YES, Field.Index.ANALYZED));
        }
        StringBuffer content = new StringBuffer();

        if ((model.getContent() == null) || ("".equals(model.getContent()))) {
          HashMap params = model.getParams();
          if (params != null) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
              Map.Entry obj = (Map.Entry)iterator.next();
              if ((obj != null) && 
                (obj.getKey() != null) && (obj.getValue() != null)) {
                String str = obj.getValue().toString();
                content.append(str);
              }
            }

            doc.add(new Field("content", content.toString(), Field.Store.YES, Field.Index.ANALYZED));
          }
        } else {
          doc.add(new Field("content", model.getContent(), Field.Store.YES, Field.Index.ANALYZED));
        }
        indexWriter.addDocument(doc);
        indexWriter.commit();
        getIndexReaderPool().init();
        flag = true;
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
    return flag;
  }

  public boolean updateDocument(IndexFormDataModel model)
  {
    boolean flag = false;
    if (model != null)
    {
      IndexModel indexModel = searcherForId(model.getId());
      if (indexModel == null) {
        return addDocument(model);
      }
      IndexWriterPool indexWriterPool = getIndexWriterPool();

      IndexWriter indexWriter = indexWriterPool.getIndexWriter("FORMINDEX");
      Document doc = new Document();
      try {
        doc.add(new Field("ID", model.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("type", "FORMINDEX", Field.Store.YES, Field.Index.NOT_ANALYZED));
        if (model.getActDefId() != null) {
          doc.add(new Field("ACT_DEF_ID", model.getActDefId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getActDefStepId() != null) {
          doc.add(new Field("ACT_DEFSTEP_ID", model.getActDefStepId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getTaskId() != null) {
          doc.add(new Field("taskId", model.getTaskId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getInstanceId() != null) {
          doc.add(new Field("instanceId", model.getInstanceId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getExcuteId() != null) {
          doc.add(new Field("excuteId", model.getExcuteId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        if (model.getTitle() != null) {
          doc.add(new Field("title", model.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (model.getOwner() != null) {
          doc.add(new Field("owner", model.getOwner(), Field.Store.YES, Field.Index.ANALYZED));
        }
        StringBuffer content = new StringBuffer();

        if ((model.getContent() == null) || ("".equals(model.getContent())))
        {
          HashMap params = model.getParams();
          if (params != null) {
            Iterator iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
              Map.Entry obj = (Map.Entry)iterator.next();
              if ((obj != null) && 
                (obj.getKey() != null) && (obj.getValue() != null)) {
                String str = obj.getValue().toString();
                content.append(str);
              }
            }

            doc.add(new Field("content", content.toString(), Field.Store.YES, Field.Index.ANALYZED));
          }
        } else {
          doc.add(new Field("content", model.getContent(), Field.Store.YES, Field.Index.ANALYZED));
        }
        TermQuery query = new TermQuery(new Term("ID", indexModel.getId()));
        indexWriter.deleteDocuments(query);
        indexWriter.addDocument(doc);
        indexWriter.commit();
        getIndexReaderPool().init();
        flag = true;
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
    return flag;
  }

  public List searcher(String text)
  {
    List list = null;
    try {
      String userid = UserContextUtil.getInstance().getCurrentUserId();
      list = new ArrayList();
      long startTime = System.currentTimeMillis();
      IndexReaderPool indexReaderPool = getIndexReaderPool();
      IndexReader indexReader = indexReaderPool.getIndexReader("FORMINDEX");
      if (indexReader != null) {
        IndexSearcher search = new IndexSearcher(indexReader);
        if (indexReader == null) return null;
        Analyzer analyzer = new IKAnalyzer();

        String[] field = { "title", "content", "owner" };
        QueryParser parse = new MultiFieldQueryParser(Version.LUCENE_36, field, analyzer);
        Query query = parse.parse(text);

        TopDocs it = search.search(query, 1000);

        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span class=\"select_index\">", "</span>");
        this.highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
        this.highlighter.setTextFragmenter(new SimpleFragmenter(150));
        ScoreDoc[] docs = it.scoreDocs;
        for (int i = 0; i < docs.length; i++) {
          IndexFormDataModel model = new IndexFormDataModel();
          Document targetDoc = search.doc(docs[i].doc);
          model.setTaskId(targetDoc.get("taskId"));
          model.setInstanceId(targetDoc.get("instanceId"));
          model.setExcuteId(targetDoc.get("excuteId"));
          model.setOwner(targetDoc.get("owner"));
          model.setActDefId(targetDoc.get("ACT_DEF_ID"));
          String title = targetDoc.get("title");

          EaglesSearchFormFilter esff = new EaglesSearchFormFilter();
          if (esff.getVisitpurview(model.getActDefId(), model.getInstanceId())) {
            if (title != null) {
              String temp = this.highlighter.getBestFragment(analyzer, "title", title);
              if (temp != null) {
                title = temp;
              }
              StringBuffer url = new StringBuffer();
              url.append("<a target=\"_blank\" href=\"loadProcessFormPage.action?actDefId=").append(model.getActDefId()).append("&instanceId=").append(model.getInstanceId()).append("&excutionId=").append(model.getExcuteId()).append("&taskId=").append(model.getTaskId()).append("").append("\" >").append(title).append("</a>");
              model.setTitle(url.toString());
            }

            model.setId(targetDoc.get("ID"));
            String content = targetDoc.get("content");
            if (content != null) {
              String temp = "";
              temp = this.highlighter.getBestFragment(analyzer, "content", content);
              if (temp == null)
                model.setContent(content);
              else {
                model.setContent(temp);
              }
            }

            model.setType("流程表单");
            list.add(model);
          }
        }
      }
      long endTime = System.currentTimeMillis();
      System.out.println("total time: " + (endTime - startTime) + " ms");
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return list;
  }

  public boolean delDocuemnt(Long id)
  {
    boolean flag = false;
    if (id != null) {
      IndexWriterPool indexWriterPool = getIndexWriterPool();

      IndexWriter indexWriter = indexWriterPool.getIndexWriter("FORMINDEX");
      try {
        TermQuery query = new TermQuery(new Term("ID", String.valueOf(id)));
        indexWriter.deleteDocuments(query);
        indexWriter.forceMerge(100);
        indexWriter.commit();
        flag = true;
      } catch (Exception e) {
        flag = false;
      }
    }
    return flag;
  }

  public boolean delDocuemnt(IndexFormDataModel model)
  {
    boolean flag = false;
    if ((model != null) && (model.getId() != null)) {
      delDocuemnt(Long.valueOf(Long.parseLong(model.getId())));
    }
    return flag;
  }

  public boolean delDocuemnt(String id)
  {
    boolean flag = false;
    if (id != null) {
      IndexWriterPool indexWriterPool = getIndexWriterPool();

      IndexWriter indexWriter = indexWriterPool.getIndexWriter("FORMINDEX");
      try {
        TermQuery query = new TermQuery(new Term("ID", id));
        indexWriter.deleteDocuments(query);
        indexWriter.forceMerge(100);
        indexWriter.commit();
        flag = true;
      } catch (Exception e) {
        flag = false;
      }
    }
    return flag;
  }

  public boolean reBuildIndex()
  {
    ProcessEngine processEngine = (ProcessEngine)SpringBeanUtil.getBean("processEngine");
    IFormService iformService = (IFormService)SpringBeanUtil.getBean("iformService");
    SysInstanceDataDAO instanceDataDAO = (SysInstanceDataDAO)SpringBeanUtil.getBean("instanceDataDAO");
    TaskService taskService = processEngine.getTaskService();
    List<Task> list = taskService.createTaskQuery().list();
    for (Task task : list) {
      IndexFormDataModel model = new IndexFormDataModel();
      Long instanceid = Long.valueOf(Long.parseLong(task.getProcessInstanceId()));
      SysInstanceData sid = instanceDataDAO.getModel(instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
      model.setInstanceId(task.getProcessInstanceId());
      model.setActDefId(task.getProcessDefinitionId());
      model.setExcuteId(task.getExecutionId());
      model.setActDefStepId(task.getTaskDefinitionKey());
      model.setOwner(task.getOwner());
      model.setCreateDate(task.getCreateTime());
      model.setId(task.getProcessInstanceId());
      model.setTaskId(task.getId());
      if (task.getDescription() != null)
        model.setTitle(task.getDescription());
      else {
        model.setTitle(task.getName());
      }
      if ((sid != null) && (sid.getFormid() != null)) {
        String page = iformService.getFormPage(sid.getFormid(), instanceid, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
        model.setContent(page);
      }
      addDocument(model);
      System.out.println("》》》》》》》》》》》》》》一个索引添加成功");
    }
    System.out.println("》》》》》》》》》》》》》》索引构建完毕");
    return false;
  }
}