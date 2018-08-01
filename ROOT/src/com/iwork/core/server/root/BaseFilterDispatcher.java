package com.iwork.core.server.root;

import com.iwork.admin.dashboard.DashboardFactory;
import com.iwork.app.conf.ConfigParserException;
import com.iwork.app.conf.ServerConfigParser;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.cache.CacheFactory;
import com.iwork.core.db.upgrade.SystemUpgradeFactory;
import com.iwork.core.engine.iform.template.IFormTemplateFactory;
import com.iwork.core.engine.plugs.component.IFormUIFactory;
import com.iwork.core.engine.runtime.CheckInfo;
import com.iwork.core.engine.runtime.el.ExpressionFactory;
import com.iwork.core.engine.runtime.tools.Check;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.process.runtime.pvm.impl.route.SysRouteFactory;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;

public class BaseFilterDispatcher extends StrutsPrepareAndExecuteFilter
{
  public void init(FilterConfig arg0)
    throws ServletException
  {
    super.init(arg0);

    String iwork_home = arg0.getServletContext().getRealPath("/");
    System.out.println("________________________________________________________________________________________\n");
    System.out.println("");
    System.out.println("==☆★==BPM系统应用开发平台(v2.1)");
    System.out.println("==☆★==公司名称:" + CheckInfo.getInstance().getCompanyname());
    System.out.println("==☆★==版本类型:" + CheckInfo.getInstance().getVersionName());
    System.out.println("==☆★==许可有效期:" + CheckInfo.getInstance().getStartdate() + "至" + CheckInfo.getInstance().getEnddate());
    System.out.println("==☆★==启动时间:" + UtilDate.getNowDatetime());
    System.out.println("==☆★==IWORK_HOME:" + iwork_home);
    try {
      ServerConfigParser.getInstance().parseXML();
      System.out.println("==☆★==加载配置文件......[OK]");
    } catch (ConfigParserException e) {
      System.out.println("==☆★==配置文件加载异常，请检查server.xml位置是否正确......[ERROR]"); 
    }
    try {
      IFormUIFactory.reloadComponent();
      ExpressionFactory.reloadComponent();
      System.out.println("==☆★==加载表单引擎外观组件......[OK]");
    } catch (Exception e) {
      System.out.println("==☆★==表单引擎外观组件加载异常，请检查iform-component.xml位置或格式是否正确......[ERROR]");
    }
    try
    {
      SysRouteFactory.reloadRoute();
      System.out.println("==☆★==加载路由策略组件......[OK]");
    } catch (Exception e) {
      System.out.println("==☆★==路由策略组件加载异常，请检查路由策略配置文件格式是否正确......[ERROR]");
      
    }
    try {
      CacheFactory.reloadCache();
      System.out.println("==☆★==加载cache缓存列表......[OK]");
    } catch (Exception e) {
      System.out.println("==☆★==cache缓存列表加载异常，请检查cache-config.xml文件格式是否正确......[ERROR]");
      
    }
    try {
      EaglesSearchFactory.reloadEaglesSearch();
      System.out.println("==☆★==初始化鹰眼全文检索引擎......[OK]");
    } catch (Exception e) {
      System.out.println("==☆★==初始化全文检索引擎异常......[ERROR]");
      
    }
    try {
      IFormTemplateFactory.reload();
      System.out.println("==☆★==初始表单模板列表......[OK]");
    } catch (Exception e) {
      System.out.println("==☆★==初始表单模板列表异常......[ERROR]");
      
    }
    try {
      DashboardFactory.reloadDashboard();
      System.out.println("==☆★==初始后台首页控制面板portlet......[OK]");
    } catch (Exception e) {
      System.out.println("==☆★==初始后台首页控制面板portlet......[ERROR]");
      
    }
    try {
        System.out.println("==☆★==数据库升级......[开始]");
        SystemUpgradeFactory.initUpgrade();
        System.out.println("==☆★==数据库升级......[结束]");
    } catch (Exception e) {
        System.out.println("==☆★==数据库升级......[ERROR]");
        
    }
    CheckInfo.getInstance();
    if (!Check.getInstance().isRunning()) {
      Check.getInstance().start();
    }
    System.out.println("____________________________________________________________________________________\n");
  }
  
  @Override
  public void doFilter(ServletRequest req, ServletResponse res,
          FilterChain chain) throws IOException, ServletException {
      HttpServletRequest request = (HttpServletRequest) req;
      String url = request.getRequestURI();
      if (url.contains("/ueditor/jsp/")) {             
          chain.doFilter(req, res);         
      }else{             
          super.doFilter(req, res, chain);         
      } 
  }
}