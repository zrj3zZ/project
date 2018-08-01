package com.iwork.core.db.upgrade;

import com.iwork.app.conf.ServerConfigParser;
import com.iwork.core.db.DBUtil;
import com.iwork.core.db.upgrade.model.UpgradeModel;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SystemUpgradeFactory
{
  private static Logger logger = Logger.getLogger(SystemUpgradeFactory.class);
  private static Hashtable<Object, UpgradeModel> _list = null;
  private static List<UpgradeModel> _sortList = null;

  static { loadUpgrade(); }


  public static void initUpgrade()
  {
    for (UpgradeModel um : _sortList) {
      boolean upgradeSuccess = false;
      String checkSql = "select count(*) num from DB_UPGRADE_LOG where UPGRADE_KEY = ?";
      Map params = new HashMap();
      params.put(1, um.getUpgradeKey());
      String str = com.iwork.commons.util.DBUtil.getDataStr("NUM", checkSql, params);
      int num  = str==null||str.equals("")?0:Integer.parseInt(str);
      if (num <= 0)
      {
        List<String> sqllist = um.getSql();
        if ((sqllist != null) && (sqllist.size() > 0)) {
          Connection conn = DBUtil.open();
          PreparedStatement stmt = null;
          try {
            conn.setAutoCommit(false);
            for (String sql : sqllist) {
            	
              try {
                stmt = conn.prepareStatement(checkSql);
                stmt.setString(1, um.getUpgradeKey());
                int i = stmt.executeUpdate();
                /*if (i < 0)
                  conn.rollback();*/
              }
              catch (Exception e) {
            	 logger.error(e,e);
              }
            }
            conn.commit();
            upgradeSuccess = true;
          } catch (Exception e) {
            logger.error("设置数据库链接AutoCommit时出现错误");
          }
          finally {
            DBUtil.close(conn, stmt, null);
          }
        }
        if (upgradeSuccess)
          try {
            System.out.println("/****************** ******************************************************************************************************************/");
            System.out.println("/*****************数据库升级**********************************************************************************************************/");
            System.out.println("升级索引:" + um.getUpgradeKey());
            System.out.println("升级类型:" + um.getUpgradeType());
            System.out.println("升级描述:" + um.getDesc());
            System.out.println("升级状态:成功");
            System.out.println("/*************************************************************************************************************************************/");

            StringBuffer upgradeSql = new StringBuffer();
            upgradeSql.append("insert into DB_UPGRADE_LOG values(");
            if (um.getUpgradeKey() != null)
              upgradeSql.append("'").append(um.getUpgradeKey()).append("'").append(",");
            else {
              upgradeSql.append("'-1'").append(",");
            }
            upgradeSql.append("sysdate").append(",");
            if (um.getUpgradeType() != null)
              upgradeSql.append("'").append(um.getUpgradeType()).append("'").append(",");
            else {
              upgradeSql.append("''").append(",");
            }
            if (um.getJiraCode() != null)
              upgradeSql.append("'").append(um.getJiraCode()).append("'").append(",");
            else {
              upgradeSql.append("''").append(",");
            }
            if (um.getDesc() != null)
              upgradeSql.append("'").append(um.getDesc()).append("'");
            else {
              upgradeSql.append("''");
            }
            upgradeSql.append(")");
            DBUtil.executeUpdate(upgradeSql.toString());
          } catch (Exception e) {
            StringBuffer error = new StringBuffer();
            error.append("数据库升级异常[JIRA-CODE:").append(um.getJiraCode()).append("]").append("[UPGRADE-KEY").append(um.getUpgradeKey()).append("]");
            logger.error(error.toString());
          }
      }
    }
  }

  public static void reloadUpgrade()
  {
    loadUpgrade();
  }

  private static void loadUpgrade()
  {
    _list = new Hashtable();
    _sortList = new ArrayList();
    Class[] parameterTypes = new Class[0];
    String xml = "upgrade-db.xml";
    String web_inf_Path = new File(ServerConfigParser.class.getResource("/").getPath()).getParent();
    web_inf_Path = web_inf_Path.replace("%20", " ");
    File file = new File(web_inf_Path + File.separator + xml);
    SAXReader saxreader = new SAXReader();
    Document doc = DocumentFactory.getInstance().createDocument();
    try {
      doc = saxreader.read(file);
      Iterator it = doc.getRootElement().elementIterator();
      Hashtable hs = new Hashtable();
      it = doc.getRootElement().elementIterator("upgrade-item");
      int i = 0;
      while (it.hasNext()) {
        Element element = (Element)it.next();
        if (element.getName().equals("upgrade-item")) {
          Iterator iit = element.elementIterator();
          UpgradeModel upgradeModel = new UpgradeModel();
          while (iit.hasNext()) {
            Element ielement = (Element)iit.next();
            if ((ielement.getName().equals("upgrade-key")) && 
              (ielement.getText() != null)) {
              try {
                upgradeModel.setUpgradeKey(ielement.getText());
              } catch (Exception e) {
                logger.error(e,e);
                logger.error(e,e);
                upgradeModel.setUpgradeKey("");
              }
            }

            if (ielement.getName().equals("upgrade-type"))
              upgradeModel.setUpgradeType(ielement.getText());
            if (ielement.getName().equals("version"))
              upgradeModel.setVersion(ielement.getText());
            if (ielement.getName().equals("sql")) {
              List sqllist = upgradeModel.getSql();
              if (sqllist == null) {
                sqllist = new ArrayList();
              }
              sqllist.add(ielement.getText());
              upgradeModel.setSql(sqllist);
            }if (ielement.getName().equals("desc"))
              upgradeModel.setDesc(ielement.getText());
            if (ielement.getName().equals("jira-code")) {
              upgradeModel.setJiraCode(ielement.getText());
            }
          }
          _list.put(upgradeModel.getUpgradeKey(), upgradeModel);
          _sortList.add(upgradeModel);
        }
      }
    } catch (Exception e) {
      logger.error(e,e);
    }
  }

  public static List getSortList()
  {
    return _sortList;
  }

  public static void setSortList(List sortList) {
    _sortList = sortList;
  }
}