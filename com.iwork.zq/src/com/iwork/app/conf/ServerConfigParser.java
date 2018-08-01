package com.iwork.app.conf;

import org.apache.commons.digester.Digester;

import org.xml.sax.SAXException;
import java.io.*;

/**
 * 将system.xml配置文件解析成
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class ServerConfigParser {
	private static ServerConfigParser configParser = null;
	public synchronized static ServerConfigParser getInstance(){
		  if(configParser==null){
			  configParser = new ServerConfigParser();
		  }
		  return configParser;
	 }
	
    /**
     * 初始化XML解析器，将server.xml文件解析成一个SERVERConfig实例，并将实例
     * 影响的值传递给SystemConfig类的静态类变量。对于AWF程序员，要获取server.xml
     * 的配置信息，可直接访问SystemConfig类的各个静态类即可。
     * 
     * @throws ConfigParserException
     * @preserve 声明此方法不被JOC混淆
     */
    public  void parseXML() throws ConfigParserException {
         String web_inf_Path=new File(ServerConfigParser.class.getResource("/").getPath()).getParent();
      //   web_inf_Path = web_inf_Path.replace("%20", " ");//替换空格
        File file = new File(web_inf_Path+File.separator+"server.xml");
        try {
        	SystemConfig iworkConfig = parseXML(new FileInputStream(file));
            //set static filed value
        	SystemConfig._iworkServerConf = iworkConfig.getIworkServerConf();
        	//SystemConfig._databaseServerConf = iworkConfig.getDataBaseServerConf();
            SystemConfig._logConf = iworkConfig.getLogConf();
            SystemConfig._optimizeConf = iworkConfig.getOptimizeConf();
            SystemConfig._fileServerConf = iworkConfig.getFileServerConf();
            SystemConfig._iformConfig = iworkConfig.getIformConf();
            SystemConfig._fullSearchConf = iworkConfig.getFullSearchConf();
            SystemConfig._mailServerConf = iworkConfig.getMailServerConf();
            SystemConfig._imConf = iworkConfig.getImConf();
            SystemConfig._smsConf = iworkConfig.getSmsConf();
            SystemConfig._designConf = iworkConfig.getDesignConf();
            SystemConfig._crawlerConf=iworkConfig.getCrawlerConf();
            SystemConfig._ggsplcConf=iworkConfig.getGgsplcConf();
            SystemConfig._xmlxnfxLcConf=iworkConfig.getXmlxnfxLcConf();
            SystemConfig._zlgdsjfxLcConf=iworkConfig.getZlgdsjfxLcConf();
            SystemConfig._sbzlLcConf=iworkConfig.getSbzlLcConf();
            SystemConfig._fazlbsLcConf=iworkConfig.getFazlbsLcConf();
            SystemConfig._cxddfqlcConf=iworkConfig.getCxddfqlcConf();
            SystemConfig._cxddyfplcConf=iworkConfig.getCxddyfplcConf();
            SystemConfig._xmlcConf=iworkConfig.getXmlcConf();
            SystemConfig._gpfxXmlcConf=iworkConfig.getGpfxXmlcConf();
            SystemConfig._xmsplcConf=iworkConfig.getXmsplcConf();
            SystemConfig._xmzbConf=iworkConfig.getXmzbConf();
            SystemConfig._bmfzrspConf=iworkConfig.getBmfzrspConf();
            SystemConfig._gpfxfpbmConf=iworkConfig.getGpfxfpbmConf();
            SystemConfig._gugaiLcConf=iworkConfig.getGugaiLcConf();
            SystemConfig._weixinConf = iworkConfig.getWeixinConf();
            SystemConfig._ssoLoginConf = iworkConfig.getSsoLoginConf();
            SystemConfig._xmnhLcConf=iworkConfig.getXmnhLcConf();
            SystemConfig._nhfkLcConf=iworkConfig.getNhfkLcConf();
            SystemConfig._xmlxSpLcConf=iworkConfig.getXmlxSpLcConf();
            SystemConfig._gzfkLcConf=iworkConfig.getGzfkLcConf();
            SystemConfig._guapaiLcConf=iworkConfig.getGuapaiLcConf();
            SystemConfig._bgXmlxSpLcConf=iworkConfig.getBgXmlxSpLcConf();
            SystemConfig._bgFangAnBSSpLcConf=iworkConfig.getBgFangAnBSSpLcConf();
            SystemConfig._bgSbzlSpLcConf=iworkConfig.getBgSbzlSpLcConf();
            SystemConfig._bgZlgdSpLcConf=iworkConfig.getBgZlgdSpLcConf();
            SystemConfig._cwXmlxSpLcConf=iworkConfig.getCwXmlxSpLcConf();
            SystemConfig._cwGzjdhbSpLcConf=iworkConfig.getCwGzjdhbSpLcConf();
            SystemConfig._cwZlgdSpLcConf=iworkConfig.getCwZlgdSpLcConf();
            SystemConfig._rcywcbLcConf=iworkConfig.getRcywcbLcConf();
            SystemConfig._cwrzsplcConf=iworkConfig.getCwrzsplcConf();
            SystemConfig._qjlcConf=iworkConfig.getQjlcConf();
            
            SystemConfig._hlDzxmlxLcConf=iworkConfig.getHlDzxmlxLcConf();
            SystemConfig._hlDzgpfxLcConf=iworkConfig.getHlDzgpfxLcConf();
            SystemConfig._hlDzyjgwLcConf=iworkConfig.getHlDzyjgwLcConf();
            SystemConfig._hlDzyjghLcConf=iworkConfig.getHlDzyjghLcConf();
            SystemConfig._hlDzsbzlLcConf=iworkConfig.getHlDzsbzlLcConf();
            SystemConfig._hlDzejgwLcConf=iworkConfig.getHlDzejgwLcConf();
            SystemConfig._hlDzejghLcConf=iworkConfig.getHlDzejghLcConf();
            /**
             * 定增200人以上
             */
            SystemConfig._hlDzwxmlxLcConf=iworkConfig.getHlDzwxmlxLcConf();
            SystemConfig._hlDzwgpfxLcConf=iworkConfig.getHlDzwgpfxLcConf();
            SystemConfig._hlDzwgzbaLcConf=iworkConfig.getHlDzwgzbaLcConf();
            SystemConfig._hlDzwnhfkLcConf=iworkConfig.getHlDzwnhfkLcConf();
            SystemConfig._hlDzwsbnhLcConf=iworkConfig.getHlDzwsbnhLcConf();
            SystemConfig._hlDzwfkhfLcConf=iworkConfig.getHlDzwfkhfLcConf();
            
            SystemConfig._hlSgxmlxLcConf=iworkConfig.getHlSgxmlxLcConf();
            SystemConfig._hlSgzlbsLcConf=iworkConfig.getHlSgzlbsLcConf();
            SystemConfig._hlSgfkwtLcConf=iworkConfig.getHlSgfkwtLcConf();
            SystemConfig._hlSgfkhfLcConf=iworkConfig.getHlSgfkhfLcConf();
            /**
             * 重组
             */
            SystemConfig._hlBgxmlxLcConf=iworkConfig.getHlBgxmlxLcConf();
            SystemConfig._hlBgCzyjhfLcConf=iworkConfig.getHlBgCzyjhfLcConf();
            SystemConfig._hlBgCzyjLcConf=iworkConfig.getHlBgCzyjLcConf();
            SystemConfig._hlBgCzyjwtLcConf=iworkConfig.getHlBgCzyjwtLcConf();
            SystemConfig._hlBgEjhfLcConf=iworkConfig.getHlBgEjhfLcConf();
            SystemConfig._hlBgEjwtLcConf=iworkConfig.getHlBgEjwtLcConf();
            SystemConfig._hlBgNhhfLcConf=iworkConfig.getHlBgNhhfLcConf();
            SystemConfig._hlBgSbnhLcConf=iworkConfig.getHlBgSbnhLcConf();
            SystemConfig._hlBgSbzlLcConf=iworkConfig.getHlBgSbzlLcConf();
            
            SystemConfig._hlGpxmlxLcConf=iworkConfig.getHlGpxmlxLcConf();
            SystemConfig._hlGpsbzkLcConf=iworkConfig.getHlGpsbzkLcConf();
            SystemConfig._hlGpnhfkLcConf=iworkConfig.getHlGpnhfkLcConf();
            SystemConfig._hlGpsbgzLcConf=iworkConfig.getHlGpsbgzLcConf();
            SystemConfig._hlGpgzfkLcConf=iworkConfig.getHlGpgzfkLcConf();
            SystemConfig._hlGpfkhfLcConf=iworkConfig.getHlGpfkhfLcConf();
            SystemConfig._hlGpscplLcConf=iworkConfig.getHlGpscplLcConf();
            SystemConfig._hlGplsplLcConf=iworkConfig.getHlGplsplLcConf();
            SystemConfig._hlGpecplLcConf=iworkConfig.getHlGpecplLcConf();
            
            /**
             * 一般财务
             */
            SystemConfig._hlYbcwxmlxLcConf=iworkConfig.getHlYbcwxmlxLcConf();
            SystemConfig._hlYbcwCwgzjdLcConf=iworkConfig.getHlYbcwCwgzjdLcConf();
            
            SystemConfig._hlQtsxLcConf=iworkConfig.getHlQtsxLcConf();
            
        } catch (FileNotFoundException e) {
            throw new ConfigParserException(e.toString());
        }
        
    } 

    /**
     * XML 2 SystemConfig
     * 
     * @param input
     *            inputStream
     * @return SystemConfig instance
     * @throws ConfigParserException
     * @preserve 声明此方法不被JOC混淆
     *  
     */
    private  SystemConfig parseXML(InputStream input) throws ConfigParserException {
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("config", SystemConfig.class);

        digester.addObjectCreate("config/base-config", IWorkServerConf.class);
        digester.addSetProperties("config/base-config", "platform.title", "title");
        digester.addSetProperties("config/base-config", "platform.short.title", "shortTitle");
        digester.addSetProperties("config/base-config", "platform.version", "version");
        digester.addSetProperties("config/base-config", "platform.login.url", "loginURL");
        digester.addSetProperties("config/base-config", "platform.debug", "debug");
        digester.addSetProperties("config/base-config", "platform.src.path", "srcPath");
        digester.addSetProperties("config/base-config", "platform", "platform");
        digester.addSetProperties("config/base-config", "shortmessage.server", "shortmessageServer");
        digester.addSetProperties("config/base-config", "login.class.adapter", "loginClassAdapter");
        //digester.addSetProperties("config/base-config", "sso.class.adapter", "ssoClassAdapter");  //统一身份认证适配器
        digester.addSetProperties("config/base-config", "org.class.adapter", "orgClassAdapter");  //组织机构适配器 
        digester.addSetProperties("config/base-config", "pause.login.verify", "loginVerify");
        digester.addSetProperties("config/base-config", "language.locale.default", "defaultLocale");
        digester.addSetProperties("config/base-config", "language.multi", "isMultiLanguage");
        digester.addSetProperties("config/base-config", "group.mode", "groupMode");
        digester.addSetProperties("config/base-config", "multi.role", "multiRole");
        digester.addSetProperties("config/base-config", "user.default.password", "userDefaultPassword");
        digester.addSetProperties("config/base-config", "user.default.passswordcount", "userDefaultPasswordCount");
        digester.addSetProperties("config/base-config", "user.default.lookandfeel", "userDefaultLookAndFeel");
        digester.addSetProperties("config/base-config", "task.server.time", "taskServerTime");
        digester.addSetProperties("config/base-config", "pause.login.count", "logincont");
        digester.addSetProperties("config/base-config", "cache.times", "cacheTime"); 
        digester.addSetProperties("config/base-config", "session.times", "sessionTime");
        digester.addSetProperties("config/base-config", "login.https", "isHttps");
        digester.addSetProperties("config/base-config", "login.isRSA", "isRSA");
        digester.addSetProperties("config/base-config", "jdbc.encrypt", "jdbcEncrypt");
        digester.addSetNext("config/base-config", "setIworkServerConf");

        /**三员分立设置**/
        digester.addObjectCreate("config/security-config", SecurityConf.class);
        digester.addSetProperties("config/security-config", "role.mode", "role_mode");
        digester.addSetProperties("config/security-config", "sys.owner", "sys_owner");
        digester.addSetProperties("config/security-config", "security.owner", "security_owner");
        digester.addSetProperties("config/security-config", "audit.owner", "audit_owner");
        digester.addSetProperties("config/security-config", "log.auto.clear", "log_auto_clear");
        digester.addSetProperties("config/security-config", "log.days", "log_days");
        digester.addSetNext("config/security-config", "setSecurityConf");
        
        
        /**SSO单点登录设置**/
        digester.addObjectCreate("config/sso-config", SSOLoginConf.class);
        digester.addSetProperties("config/sso-config", "sso.mode", "ssoMode");
        digester.addSetProperties("config/sso-config", "sso.server", "ssoServer");
        digester.addSetProperties("config/sso-config", "sso.logout", "ssoLogout");  
        digester.addSetProperties("config/sso-config", "sso.client", "ssoClient");  
        digester.addSetProperties("config/sso-config", "sso.class.adapter", "ssoClassAdapter");
        digester.addSetNext("config/sso-config", "setSsoLoginConf");
//        digester.addObjectCreate("config/database-config", DataBaseServerConf.class);
//        digester.addSetProperties("config/database-config", "supply", "supply");
//        digester.addSetProperties("config/database-config", "driver", "driver");
//        digester.addSetProperties("config/database-config", "url", "url");
//        digester.addSetProperties("config/database-config", "username", "userName");
//        digester.addSetProperties("config/database-config", "password", "password");
//        digester.addSetProperties("config/database-config", "debug", "debug");
//        digester.addSetNext("config/database-config", "setDataBaseServerConf");

//        digester.addObjectCreate("config/optimize", OptimizeConf.class);
//        digester.addSetProperties("config/optimize", "max.client", "maxClient");
//        digester.addSetProperties("config/optimize", "thread.overtime", "threadOvertime");
//        digester.addSetProperties("config/optimize", "database.max.session", "maxSession");
//        digester.addSetProperties("config/optimize", "database.pool.init", "databasePoolInit");
//        digester.addSetProperties("config/optimize", "database.pool.max.workload", "databasePoolMaxWorkload");
//        digester.addSetNext("config/optimize", "setOptimizeConf"); 

        digester.addObjectCreate("config/log", LogConf.class);
        digester.addSetProperties("config/log", "runtime.server", "runtimeServer");
        digester.addSetProperties("config/log", "runtime.filename", "runtimeFileName");
        digester.addSetProperties("config/log", "error.filename", "errorFileName");
        digester.addSetNext("config/log", "setLogConf");
 
        digester.addObjectCreate("config/fileserver-config", FileServerConfig.class);
        digester.addSetProperties("config/fileserver-config", "path", "path");
        digester.addSetProperties("config/fileserver-config", "bigTxtfilePath", "bigTxtFilePath");
        digester.addSetProperties("config/fileserver-config", "formfilePath", "formFilePath");
        digester.addSetProperties("config/fileserver-config", "talkfilePath", "talkfilePath");
        digester.addSetProperties("config/fileserver-config", "userPhotoPath", "userPhotoPath");
        digester.addSetProperties("config/fileserver-config", "kmfilePath", "kmFilePath");
        digester.addSetProperties("config/fileserver-config", "cmsFilePath", "cmsFilePath");
        digester.addSetProperties("config/fileserver-config", "size", "size");
        digester.addSetNext("config/fileserver-config", "setFileServerConf");

        digester.addObjectCreate("config/iform", IformConf.class);
        digester.addSetProperties("config/iform", "template.src", "template_src");
        digester.addSetProperties("config/iform", "template.home", "template_home");
        digester.addSetProperties("config/iform", "template.history", "template_history");
        digester.addSetProperties("config/iform", "diy.path", "is_diy_path");
        digester.addSetNext("config/iform", "setIformConf");
        
        digester.addObjectCreate("config/eagles-search", FullSearchConf.class);
        digester.addSetProperties("config/eagles-search", "indexRootDirPath", "indexRootDirPath");
        digester.addSetNext("config/eagles-search", "setFullSearchConf");
        
        //加载流程设计器配置
        digester.addObjectCreate("config/design-config", WfDesignConf.class);
        digester.addSetProperties("config/design-config", "design.ip", "ip");
        digester.addSetProperties("config/design-config", "design.port", "port");
        digester.addSetNext("config/design-config", "setDesignConf");
        
        //加载IM配置
        digester.addObjectCreate("config/im-server", IMConf.class);
        digester.addSetProperties("config/im-server", "im.server", "server");
        digester.addSetProperties("config/im-server", "im.src", "source");
        digester.addSetProperties("config/im-server", "im.url", "url");
        digester.addSetProperties("config/im-server", "im.title", "title");
        digester.addSetProperties("config/im-server", "im.title", "title");
        digester.addSetProperties("config/im-server", "im.debug", "isDebug");
        digester.addSetProperties("config/im-server", "im.debug.user", "debugUser");
        digester.addSetNext("config/im-server", "setImConf"); 
        
        //加载邮件配置
        digester.addObjectCreate("config/mail-server", MailServerConf.class);
        digester.addSetProperties("config/mail-server", "mail.from", "mailFrom");
        digester.addSetProperties("config/mail-server", "mail.user", "mailuser");
        digester.addSetProperties("config/mail-server", "mail.password", "password");
        digester.addSetProperties("config/mail-server", "smtp.services", "smtp_services");
        digester.addSetProperties("config/mail-server", "smtp.host", "smtp_host");
        digester.addSetProperties("config/mail-server", "smtp.port", "smtp_port");
        digester.addSetProperties("config/mail-server", "smtp.auth", "smtp_auth");
        digester.addSetProperties("config/mail-server", "smtp.ssl", "smtp_ssl");
        digester.addSetProperties("config/mail-server", "pop3.services", "pop3_services");
        digester.addSetProperties("config/mail-server", "pop3.host", "pop3_host");
        digester.addSetProperties("config/mail-server", "pop3.port", "pop3_port");
        digester.addSetProperties("config/mail-server", "pop3.auth", "pop3_auth");
        digester.addSetProperties("config/mail-server", "pop3.ssl", "pop3_ssl");
        digester.addSetProperties("config/mail-server", "pop3.backup", "pop3_backup");
        digester.addSetProperties("config/mail-server", "mail.debug", "isDebug");
        digester.addSetProperties("config/mail-server", "mail.debug.address", "debug_address"); 
        digester.addSetNext("config/mail-server", "setMailServerConf");  
        //加载短信配置
        digester.addObjectCreate("config/sms-server", SMSConf.class);
        digester.addSetProperties("config/sms-server", "sms.server", "server");
        digester.addSetProperties("config/sms-server", "sms.src", "source");
        digester.addSetProperties("config/sms-server", "sms.sn", "sn");
        digester.addSetProperties("config/sms-server", "sms.pwd", "pwd");
        digester.addSetProperties("config/sms-server", "sms.type", "type");
        digester.addSetProperties("config/sms-server", "sms.url", "url");
        digester.addSetProperties("config/sms-server", "sms.debug", "isDebug"); 
        digester.addSetProperties("config/sms-server", "sms.debug.mobile", "debugMobile");
        digester.addSetProperties("config/sms-server", "dgsms.url", "dgurl");
        digester.addSetProperties("config/sms-server", "dgsms.cmd", "cmd");
        digester.addSetProperties("config/sms-server", "dgsms.uid", "uid");
        digester.addSetProperties("config/sms-server", "dgsms.msgid", "msgid");
        digester.addSetProperties("config/sms-server", "dgsms.psw", "psw");
        
        digester.addSetProperties("config/sms-server", "xnsms.xnurl", "xnurl");
        digester.addSetProperties("config/sms-server", "xnsms.lx", "lx");
        digester.addSetProperties("config/sms-server", "xnsms.dlzh", "dlzh");
        digester.addSetProperties("config/sms-server", "xnsms.dlmm", "dlmm");
        
        digester.addSetProperties("config/sms-server", "xnsms.dxlx", "dxlx");
        digester.addSetProperties("config/sms-server", "xnsms.dssj", "dssj");
        digester.addSetProperties("config/sms-server", "xnsms.shyh", "shyh");
        
        digester.addSetProperties("config/sms-server", "xnsms.fhls", "fhls");
        digester.addSetProperties("config/sms-server", "xnsms.yybdm", "yybdm");
        digester.addSetNext("config/sms-server", "setSmsConf");

        //加载微信配置
        digester.addObjectCreate("config/weixin-server", WeiXinConf.class);
        digester.addSetProperties("config/weixin-server", "weixin.server", "server");
        digester.addSetProperties("config/weixin-server", "weixin.url", "url");
        digester.addSetProperties("config/weixin-server", "weixin.agentid", "agentid");
        digester.addSetProperties("config/weixin-server", "weixin.token", "token");
        digester.addSetProperties("config/weixin-server", "weixin.EncodingAESKey", "encodingAESKey");
        digester.addSetProperties("config/weixin-server", "weixin.CorpID", "corpId");
        digester.addSetProperties("config/weixin-server", "weixin.message.Secret", "messageSecret");
        digester.addSetProperties("config/weixin-server", "weixin.account.Secret", "accountSecret"); 
        digester.addSetProperties("config/weixin-server", "weixin.config.Secret", "configSecret");
        digester.addSetNext("config/weixin-server", "setWeixinConf");
        //加载抓取服务配置
         digester.addObjectCreate("config/crawler-server", CrawlerConf.class);
         digester.addSetProperties("config/crawler-server", "crawler.url", "url");
         digester.addSetNext("config/crawler-server", "setCrawlerConf");
         //加载董秘公告配置
         digester.addObjectCreate("config/ggsplc-server", GgsplcConf.class);
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd0", "jd0");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd1", "jd1");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd2", "jd2"); 
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd3", "jd3");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd4", "jd4"); 
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd5", "jd5");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd6", "jd6"); 
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd7", "jd7");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd8", "jd8");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd9", "jd9");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd10", "jd10");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd11", "jd11");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.jd12", "jd12");
         digester.addSetProperties("config/ggsplc-server", "ggsplc.end", "end"); 
         digester.addSetNext("config/ggsplc-server", "setGgsplcConf");   
         //加载持续督导发起流程配置
         digester.addObjectCreate("config/cxddfqlc-server", CxddfqlcConf.class);
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd1", "jd1");
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd2", "jd2"); 
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd3", "jd3");
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd4", "jd4"); 
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd5", "jd5");
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd6", "jd6"); 
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd7", "jd7"); 
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd8", "jd8"); 
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd9", "jd9"); 
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.jd10", "jd10"); 
         digester.addSetProperties("config/cxddfqlc-server", "cxddfqlc.end", "end"); 
         digester.addSetNext("config/cxddfqlc-server", "setCxddfqlcConf");   
         

         //加载持续督导分派流程配置
         digester.addObjectCreate("config/cxddyfplc-server", CxddyfplcConf.class);
         digester.addSetProperties("config/cxddyfplc-server", "cxddyfplc.jd1", "jd1");
         digester.addSetProperties("config/cxddyfplc-server", "cxddyfplc.jd2", "jd2"); 
         digester.addSetProperties("config/cxddyfplc-server", "cxddyfplc.jd3", "jd3");
         digester.addSetProperties("config/cxddyfplc-server", "cxddyfplc.jd4", "jd4"); 
         digester.addSetProperties("config/cxddyfplc-server", "cxddyfplc.end", "end"); 
         digester.addSetNext("config/cxddyfplc-server", "setCxddyfplcConf");   
         //项目流程配置
         digester.addObjectCreate("config/gpfxxmlc-server", GpfxXmlcConf.class);
         digester.addSetProperties("config/gpfxxmlc-server", "gpfxxmlc.config", "config");
         digester.addSetProperties("config/gpfxxmlc-server", "gpfxxmlc.jd1", "jd1");
         digester.addSetProperties("config/gpfxxmlc-server", "gpfxxmlc.jd2", "jd2");
         digester.addSetProperties("config/gpfxxmlc-server", "gpfxxmlc.jd3", "jd3");
         digester.addSetProperties("config/gpfxxmlc-server", "gpfxxmlc.jd4", "jd4");
         digester.addSetProperties("config/gpfxxmlc-server", "gpfxxmlc.jd5", "jd5");
         digester.addSetProperties("config/gpfxxmlc-server", "gpfxxmlc.end", "end");
         digester.addSetNext("config/gpfxxmlc-server", "setGpfxXmlcConf");
         
         //股票发行项目任务流程配置
         digester.addObjectCreate("config/xmlc-server", XmlcConf.class);
         digester.addSetProperties("config/xmlc-server", "xmlc.config", "config");
         digester.addSetProperties("config/xmlc-server", "xmlc.jd1", "jd1");
         digester.addSetProperties("config/xmlc-server", "xmlc.jd2", "jd2");
         digester.addSetProperties("config/xmlc-server", "xmlc.jd3", "jd3");
         digester.addSetProperties("config/xmlc-server", "xmlc.jd4", "jd4");
         digester.addSetProperties("config/xmlc-server", "xmlc.jd5", "jd5");
         digester.addSetProperties("config/xmlc-server", "xmlc.end", "end");
         digester.addSetNext("config/xmlc-server", "setXmlcConf");
         
         //股票发行拟发行
         digester.addObjectCreate("config/xmlxnfx-server", XmlxnfxLcConf.class);
         digester.addSetProperties("config/xmlxnfx-server", "xmlxnfx.jd1", "jd1");
         digester.addSetProperties("config/xmlxnfx-server", "xmlxnfx.jd2", "jd2");
         digester.addSetProperties("config/xmlxnfx-server", "xmlxnfx.jd3", "jd3");
         digester.addSetProperties("config/xmlxnfx-server", "xmlxnfx.jd4", "jd4");
         digester.addSetProperties("config/xmlxnfx-server", "xmlxnfx.jd5", "jd5");
         digester.addSetProperties("config/xmlxnfx-server", "xmlxnfx.end", "end");
         digester.addSetNext("config/xmlxnfx-server", "setXmlxnfxLcConf");
         
         //股票发行实际发行
         digester.addObjectCreate("config/zlgdsjfx-server", ZlgdsjfxLcConf.class);
         digester.addSetProperties("config/zlgdsjfx-server", "zlgdsjfx.jd1", "jd1");
         digester.addSetProperties("config/zlgdsjfx-server", "zlgdsjfx.jd2", "jd2");
         digester.addSetProperties("config/zlgdsjfx-server", "zlgdsjfx.jd3", "jd3");
         digester.addSetProperties("config/zlgdsjfx-server", "zlgdsjfx.jd4", "jd4");
         digester.addSetProperties("config/zlgdsjfx-server", "zlgdsjfx.jd5", "jd5");
         digester.addSetProperties("config/zlgdsjfx-server", "zlgdsjfx.jd6", "jd6");
         digester.addSetProperties("config/zlgdsjfx-server", "zlgdsjfx.end", "end");
         digester.addSetNext("config/zlgdsjfx-server", "setZlgdsjfxLcConf");
         //股票发行方案资料报审
         digester.addObjectCreate("config/fazlbs-server", FazlbsLcConf.class);
         digester.addSetProperties("config/fazlbs-server", "fazlbs.jd1", "jd1");
         digester.addSetProperties("config/fazlbs-server", "fazlbs.jd2", "jd2");
         digester.addSetProperties("config/fazlbs-server", "fazlbs.jd3", "jd3");
         digester.addSetProperties("config/fazlbs-server", "fazlbs.jd4", "jd4");
         digester.addSetProperties("config/fazlbs-server", "fazlbs.jd5", "jd5");
         digester.addSetProperties("config/fazlbs-server", "fazlbs.end", "end");
         digester.addSetNext("config/fazlbs-server", "setFazlbsLcConf");
         //股票发行申报资料
         digester.addObjectCreate("config/sbzl-server", SbzlLcConf.class);
         digester.addSetProperties("config/sbzl-server", "sbzl.jd1", "jd1");
         digester.addSetProperties("config/sbzl-server", "sbzl.jd2", "jd2");
         digester.addSetProperties("config/sbzl-server", "sbzl.jd3", "jd3");
         digester.addSetProperties("config/sbzl-server", "sbzl.jd4", "jd4");
         digester.addSetProperties("config/sbzl-server", "sbzl.jd5", "jd5");
         digester.addSetProperties("config/sbzl-server", "sbzl.jd6", "jd6");
         digester.addSetProperties("config/sbzl-server", "sbzl.end", "end");
         digester.addSetNext("config/sbzl-server", "setSbzlLcConf");
         
         //项目审批流程配置
         digester.addObjectCreate("config/xmsplc-server", XmsplcConf.class);
         digester.addSetProperties("config/xmsplc-server", "xmsplc.jd1", "jd1");
         digester.addSetProperties("config/xmsplc-server", "xmsplc.jd2", "jd2");
         digester.addSetProperties("config/xmsplc-server", "xmsplc.jd3", "jd3");
         digester.addSetProperties("config/xmsplc-server", "xmsplc.end", "end");
         digester.addSetNext("config/xmsplc-server", "setXmsplcConf");
  
         //项目子表审批流程配置
         digester.addObjectCreate("config/xmzbsplc-server", XmzbConf.class);
         digester.addSetProperties("config/xmzbsplc-server", "xmzbsplc.jd1", "jd1");
         digester.addSetProperties("config/xmzbsplc-server", "xmzbsplc.jd2", "jd2");
         digester.addSetProperties("config/xmzbsplc-server", "xmzbsplc.end", "end");
         digester.addSetNext("config/xmzbsplc-server", "setXmzbConf");   
         
         digester.addObjectCreate("config/bmfzrsp-server", BmfzrspConf.class);
         digester.addSetProperties("config/bmfzrsp-server", "bmfzrsp.jd1", "jd1");
         digester.addSetProperties("config/bmfzrsp-server", "bmfzrsp.jd2", "jd2");
         digester.addSetProperties("config/bmfzrsp-server", "bmfzrsp.end", "end");
         digester.addSetNext("config/bmfzrsp-server", "setBmfzrspConf");
         
         digester.addObjectCreate("config/cwrzsp-server", CwrzsplcConf.class);
         digester.addSetProperties("config/cwrzsp-server", "cwrzsp.jd1", "jd1");
         digester.addSetProperties("config/cwrzsp-server", "cwrzsp.jd2", "jd2");
         digester.addSetProperties("config/cwrzsp-server", "cwrzsp.end", "end");
         digester.addSetNext("config/cwrzsp-server", "setCwrzsplcConf");
         
         //股票发行分配部门流程配置
         digester.addObjectCreate("config/gpfxfpbm-server", GpfxfpbmConf.class);
         digester.addSetProperties("config/gpfxfpbm-server", "gpfxfpbm.jd1", "jd1");
         digester.addSetProperties("config/gpfxfpbm-server", "gpfxfpbm.jd2", "jd2");
         digester.addSetProperties("config/gpfxfpbm-server", "gpfxfpbm.jd3", "jd3");
         digester.addSetProperties("config/gpfxfpbm-server", "gpfxfpbm.end", "end");
         digester.addSetNext("config/gpfxfpbm-server", "setGpfxfpbmConf");
         
         //项目流程配置
         digester.addObjectCreate("config/gugailc-server", GuGaiLcConf.class);
         digester.addSetProperties("config/gugailc-server", "gugailc.jd1", "jd1");
         digester.addSetProperties("config/gugailc-server", "gugailc.jd2", "jd2");
         digester.addSetProperties("config/gugailc-server", "gugailc.jd3", "jd3");
         digester.addSetProperties("config/gugailc-server", "gugailc.jd4", "jd4");
         digester.addSetProperties("config/gugailc-server", "gugailc.jd5", "jd5");
         digester.addSetProperties("config/gugailc-server", "gugailc.end", "end");
         digester.addSetNext("config/gugailc-server", "setGugaiLcConf");

         //项目内核流程配置
         digester.addObjectCreate("config/xmnh-server", XmnhLcConf.class);
         digester.addSetProperties("config/xmnh-server", "xmnh.jd1", "jd1");
         digester.addSetProperties("config/xmnh-server", "xmnh.jd2", "jd2");
         digester.addSetProperties("config/xmnh-server", "xmnh.jd3", "jd3");
         digester.addSetProperties("config/xmnh-server", "xmnh.jd4", "jd4");
         digester.addSetProperties("config/xmnh-server", "xmnh.jd5", "jd5");
         digester.addSetProperties("config/xmnh-server", "xmnh.jd6", "jd6");
         digester.addSetProperties("config/xmnh-server", "xmnh.jd7", "jd7");
         digester.addSetProperties("config/xmnh-server", "xmnh.jd8", "jd8");
         digester.addSetProperties("config/xmnh-server", "xmnh.end", "end");
         digester.addSetNext("config/xmnh-server", "setXmnhLcConf");
         
         //内核反馈流程配置
         digester.addObjectCreate("config/nhfk-server", NhfkLcConf.class);
         digester.addSetProperties("config/nhfk-server", "nhfk.jd1", "jd1");
         digester.addSetProperties("config/nhfk-server", "nhfk.jd2", "jd2");
         digester.addSetProperties("config/nhfk-server", "nhfk.jd3", "jd3");
         digester.addSetProperties("config/nhfk-server", "nhfk.jd4", "jd4");
         digester.addSetProperties("config/nhfk-server", "nhfk.jd5", "jd5");
         digester.addSetProperties("config/nhfk-server", "nhfk.jd6", "jd6");
         digester.addSetProperties("config/nhfk-server", "nhfk.end", "end");
         digester.addSetNext("config/nhfk-server", "setNhfkLcConf");
         
         //项目立项流程配置
         digester.addObjectCreate("config/xmlxsplc-server", XmlxSpLcConf.class);
         digester.addSetProperties("config/xmlxsplc-server", "xmlxsplc.jd1", "jd1");
         digester.addSetProperties("config/xmlxsplc-server", "xmlxsplc.jd2", "jd2");
         digester.addSetProperties("config/xmlxsplc-server", "xmlxsplc.jd3", "jd3");
         digester.addSetProperties("config/xmlxsplc-server", "xmlxsplc.jd4", "jd4");
         digester.addSetProperties("config/xmlxsplc-server", "xmlxsplc.jd5", "jd5");
         digester.addSetProperties("config/xmlxsplc-server", "xmlxsplc.jd6", "jd6");
         digester.addSetProperties("config/xmlxsplc-server", "xmlxsplc.end", "end");
         digester.addSetNext("config/xmlxsplc-server", "setXmlxSpLcConf");
         
         //股转反馈流程配置
         digester.addObjectCreate("config/gzfk-server", GzfkLcConf.class);
         digester.addSetProperties("config/gzfk-server", "gzfk.jd1", "jd1");
         digester.addSetProperties("config/gzfk-server", "gzfk.jd2", "jd2");
         digester.addSetProperties("config/gzfk-server", "gzfk.jd3", "jd3");
         digester.addSetProperties("config/gzfk-server", "gzfk.jd4", "jd4");
         digester.addSetProperties("config/gzfk-server", "gzfk.jd5", "jd5");
         digester.addSetProperties("config/gzfk-server", "gzfk.jd6", "jd6");
         digester.addSetProperties("config/gzfk-server", "gzfk.end", "end");
         digester.addSetNext("config/gzfk-server", "setGzfkLcConf");
         
         //挂牌流程配置
         digester.addObjectCreate("config/guapai-server", GuaPaiLcConf.class);
         digester.addSetProperties("config/guapai-server", "guapai.jd1", "jd1");
         digester.addSetProperties("config/guapai-server", "guapai.jd2", "jd2");
         digester.addSetProperties("config/guapai-server", "guapai.jd3", "jd3");
         digester.addSetProperties("config/guapai-server", "guapai.jd4", "jd4");
         digester.addSetProperties("config/guapai-server", "guapai.jd5", "jd5");
         digester.addSetProperties("config/guapai-server", "guapai.jd6", "jd6");
         digester.addSetProperties("config/guapai-server", "guapai.jd7", "jd7");
         digester.addSetProperties("config/guapai-server", "guapai.jd8", "jd8");
         digester.addSetProperties("config/guapai-server", "guapai.jd9", "jd9");
         digester.addSetProperties("config/guapai-server", "guapai.jd10", "jd10");
         digester.addSetProperties("config/guapai-server", "guapai.end", "end");
         digester.addSetNext("config/guapai-server", "setGuapaiLcConf");
         
         //并购立项流程配置
         digester.addObjectCreate("config/bgxmlx-server", BgXmlxSpLcConf.class);
         digester.addSetProperties("config/bgxmlx-server", "bgxmlx.jd1", "jd1");
         digester.addSetProperties("config/bgxmlx-server", "bgxmlx.jd2", "jd2");
         digester.addSetProperties("config/bgxmlx-server", "bgxmlx.jd3", "jd3");
         digester.addSetProperties("config/bgxmlx-server", "bgxmlx.jd4", "jd4");
         digester.addSetProperties("config/bgxmlx-server", "bgxmlx.jd5", "jd5");
         digester.addSetProperties("config/bgxmlx-server", "bgxmlx.end", "end");
         digester.addSetNext("config/bgxmlx-server", "setBgXmlxSpLcConf");
         
         //方案资料报审流程配置
         digester.addObjectCreate("config/bgfanganbs-server", BgFangAnBSSpLcConf.class);
         digester.addSetProperties("config/bgfanganbs-server", "bgfanganbs.jd1", "jd1");
         digester.addSetProperties("config/bgfanganbs-server", "bgfanganbs.jd2", "jd2");
         digester.addSetProperties("config/bgfanganbs-server", "bgfanganbs.jd3", "jd3");
         digester.addSetProperties("config/bgfanganbs-server", "bgfanganbs.jd4", "jd4");
         digester.addSetProperties("config/bgfanganbs-server", "bgfanganbs.jd5", "jd5");
         digester.addSetProperties("config/bgfanganbs-server", "bgfanganbs.end", "end");
         digester.addSetNext("config/bgfanganbs-server", "setBgFangAnBSSpLcConf");
         
         //申报资料流程配置
         digester.addObjectCreate("config/bgsbzl-server", BgSbzlSpLcConf.class);
         digester.addSetProperties("config/bgsbzl-server", "bgsbzl.jd1", "jd1");
         digester.addSetProperties("config/bgsbzl-server", "bgsbzl.jd2", "jd2");
         digester.addSetProperties("config/bgsbzl-server", "bgsbzl.jd3", "jd3");
         digester.addSetProperties("config/bgsbzl-server", "bgsbzl.jd4", "jd4");
         digester.addSetProperties("config/bgsbzl-server", "bgsbzl.jd5", "jd5");
         digester.addSetProperties("config/bgsbzl-server", "bgsbzl.jd6", "jd6");
         digester.addSetProperties("config/bgsbzl-server", "bgsbzl.end", "end");
         digester.addSetNext("config/bgsbzl-server", "setBgSbzlSpLcConf");
         
         //资料归档流程配置
         digester.addObjectCreate("config/bgzlgd-server", BgZlgdSpLcConf.class);
         digester.addSetProperties("config/bgzlgd-server", "bgzlgd.jd1", "jd1");
         digester.addSetProperties("config/bgzlgd-server", "bgzlgd.jd2", "jd2");
         digester.addSetProperties("config/bgzlgd-server", "bgzlgd.jd3", "jd3");
         digester.addSetProperties("config/bgzlgd-server", "bgzlgd.jd4", "jd4");
         digester.addSetProperties("config/bgzlgd-server", "bgzlgd.jd5", "jd5");
         digester.addSetProperties("config/bgzlgd-server", "bgzlgd.jd6", "jd6");
         digester.addSetProperties("config/bgzlgd-server", "bgzlgd.end", "end");
         digester.addSetNext("config/bgzlgd-server", "setBgZlgdSpLcConf");
         
         //财务立项流程配置
         digester.addObjectCreate("config/cwxmlx-server", CwXmlxSpLcConf.class);
         digester.addSetProperties("config/cwxmlx-server", "cwxmlx.jd1", "jd1");
         digester.addSetProperties("config/cwxmlx-server", "cwxmlx.jd2", "jd2");
         digester.addSetProperties("config/cwxmlx-server", "cwxmlx.jd3", "jd3");
         digester.addSetProperties("config/cwxmlx-server", "cwxmlx.jd4", "jd4");
         digester.addSetProperties("config/cwxmlx-server", "cwxmlx.jd5", "jd5");
         digester.addSetProperties("config/cwxmlx-server", "cwxmlx.end", "end");
         digester.addSetNext("config/cwxmlx-server", "setCwXmlxSpLcConf");
         
         //财务工作进度汇报流程配置
         digester.addObjectCreate("config/cwgzjdhb-server", CwGzjdhbSpLcConf.class);
         digester.addSetProperties("config/cwgzjdhb-server", "cwgzjdhb.jd1", "jd1");
         digester.addSetProperties("config/cwgzjdhb-server", "cwgzjdhb.jd2", "jd2");
         digester.addSetProperties("config/cwgzjdhb-server", "cwgzjdhb.jd3", "jd3");
         digester.addSetProperties("config/cwgzjdhb-server", "cwgzjdhb.jd4", "jd4");
         digester.addSetProperties("config/cwgzjdhb-server", "cwgzjdhb.jd5", "jd5");
         digester.addSetProperties("config/cwgzjdhb-server", "cwgzjdhb.end", "end");
         digester.addSetNext("config/cwgzjdhb-server", "setCwGzjdhbSpLcConf");
         
         //财务资料归档流程配置
         digester.addObjectCreate("config/cwzlgd-server", CwZlgdSpLcConf.class);
         digester.addSetProperties("config/cwzlgd-server", "cwzlgd.jd1", "jd1");
         digester.addSetProperties("config/cwzlgd-server", "cwzlgd.jd2", "jd2");
         digester.addSetProperties("config/cwzlgd-server", "cwzlgd.jd3", "jd3");
         digester.addSetProperties("config/cwzlgd-server", "cwzlgd.jd4", "jd4");
         digester.addSetProperties("config/cwzlgd-server", "cwzlgd.end", "end");
         digester.addSetNext("config/cwzlgd-server", "setCwZlgdSpLcConf");
         
         //日常业务呈报批流程配置
         digester.addObjectCreate("config/rcywcblc-server", RcywcbLcConf.class);
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd0", "jd0");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd1", "jd1");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd2", "jd2");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd3", "jd3");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd4", "jd4");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd5", "jd5");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd6", "jd6");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd7", "jd7");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd8", "jd8");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd9", "jd9");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.jd12", "jd12");
         digester.addSetProperties("config/rcywcblc-server", "rcywcblc.end", "end");
         digester.addSetNext("config/rcywcblc-server", "setRcywcbLcConf");
         
         //请假流程
         digester.addObjectCreate("config/qjlc-server", QjlcConf.class);
         digester.addSetProperties("config/qjlc-server", "qjlc.jd1", "jd1");
         digester.addSetProperties("config/qjlc-server", "qjlc.jd2", "jd2");
         digester.addSetProperties("config/qjlc-server", "qjlc.jd3", "jd3");
         digester.addSetProperties("config/qjlc-server", "qjlc.jd4", "jd4");
         digester.addSetProperties("config/qjlc-server", "qjlc.end", "end");
         digester.addSetNext("config/qjlc-server", "setQjlcConf");
         
         /**华龙定增项目流程开始*/
         //定增项目立项
         digester.addObjectCreate("config/hldzxmlxlc-server", HlDzxmlxLcConf.class);
         digester.addSetProperties("config/hldzxmlxlc-server", "hldzxmlxlc.jd1", "jd1");
         digester.addSetProperties("config/hldzxmlxlc-server", "hldzxmlxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzxmlxlc-server", "hldzxmlxlc.jd3", "jd3");
         digester.addSetProperties("config/hldzxmlxlc-server", "hldzxmlxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzxmlxlc-server", "hldzxmlxlc.jd5", "jd5");
         digester.addSetProperties("config/hldzxmlxlc-server", "hldzxmlxlc.jd6", "jd6"); 
         digester.addSetProperties("config/hldzxmlxlc-server", "hldzxmlxlc.end", "end"); 
         digester.addSetNext("config/hldzxmlxlc-server", "setHlDzxmlxLcConf");
         //定增股票发行方案
         digester.addObjectCreate("config/hldzgpfxlc-server", HlDzgpfxLcConf.class);
         digester.addSetProperties("config/hldzgpfxlc-server", "hldzgpfxlc.jd1", "jd1");
         digester.addSetProperties("config/hldzgpfxlc-server", "hldzgpfxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzgpfxlc-server", "hldzgpfxlc.jd3", "jd3");
         digester.addSetProperties("config/hldzgpfxlc-server", "hldzgpfxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzgpfxlc-server", "hldzgpfxlc.jd5", "jd5");
         digester.addSetProperties("config/hldzgpfxlc-server", "hldzgpfxlc.end", "end"); 
         digester.addSetNext("config/hldzgpfxlc-server", "setHlDzgpfxLcConf");
         //定增一阶段股转反馈问题
         digester.addObjectCreate("config/hldzyjgwlc-server", HlDzyjgwLcConf.class);
         digester.addSetProperties("config/hldzyjgwlc-server", "hldzyjgwlc.jd1", "jd1");
         digester.addSetProperties("config/hldzyjgwlc-server", "hldzyjgwlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzyjgwlc-server", "hldzyjgwlc.jd3", "jd3");
         digester.addSetProperties("config/hldzyjgwlc-server", "hldzyjgwlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzyjgwlc-server", "hldzyjgwlc.jd5", "jd5");
         digester.addSetProperties("config/hldzyjgwlc-server", "hldzyjgwlc.jd6", "jd6"); 
         digester.addSetProperties("config/hldzyjgwlc-server", "hldzyjgwlc.end", "end"); 
         digester.addSetNext("config/hldzyjgwlc-server", "setHlDzyjgwLcConf");
         //定增一阶段股转反馈回复
         digester.addObjectCreate("config/hldzyjghlc-server", HlDzyjghLcConf.class);
         digester.addSetProperties("config/hldzyjghlc-server", "hldzyjghlc.jd1", "jd1");
         digester.addSetProperties("config/hldzyjghlc-server", "hldzyjghlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzyjghlc-server", "hldzyjghlc.jd3", "jd3");
         digester.addSetProperties("config/hldzyjghlc-server", "hldzyjghlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzyjghlc-server", "hldzyjghlc.jd5", "jd5");
         digester.addSetProperties("config/hldzyjghlc-server", "hldzyjghlc.end", "end"); 
         digester.addSetNext("config/hldzyjghlc-server", "setHlDzyjghLcConf");
         //定增申报资料（备案）
         digester.addObjectCreate("config/hldzsbzllc-server", HlDzsbzlLcConf.class);
         digester.addSetProperties("config/hldzsbzllc-server", "hldzsbzllc.jd1", "jd1");
         digester.addSetProperties("config/hldzsbzllc-server", "hldzsbzllc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzsbzllc-server", "hldzsbzllc.jd3", "jd3");
         digester.addSetProperties("config/hldzsbzllc-server", "hldzsbzllc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzsbzllc-server", "hldzsbzllc.jd5", "jd5");
         digester.addSetProperties("config/hldzsbzllc-server", "hldzsbzllc.end", "end"); 
         digester.addSetNext("config/hldzsbzllc-server", "setHlDzsbzlLcConf");
         //定增二阶段股转反馈问题
         digester.addObjectCreate("config/hldzejgwlc-server", HlDzejgwLcConf.class);
         digester.addSetProperties("config/hldzejgwlc-server", "hldzejgwlc.jd1", "jd1");
         digester.addSetProperties("config/hldzejgwlc-server", "hldzejgwlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzejgwlc-server", "hldzejgwlc.jd3", "jd3");
         digester.addSetProperties("config/hldzejgwlc-server", "hldzejgwlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzejgwlc-server", "hldzejgwlc.jd5", "jd5");
         digester.addSetProperties("config/hldzejgwlc-server", "hldzejgwlc.jd6", "jd6"); 
         digester.addSetProperties("config/hldzejgwlc-server", "hldzejgwlc.end", "end"); 
         digester.addSetNext("config/hldzejgwlc-server", "setHlDzejgwLcConf");
         //定增二阶段股转反馈回复
         digester.addObjectCreate("config/hldzejghlc-server", HlDzejghLcConf.class);
         digester.addSetProperties("config/hldzejghlc-server", "hldzejghlc.jd1", "jd1");
         digester.addSetProperties("config/hldzejghlc-server", "hldzejghlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzejghlc-server", "hldzejghlc.jd3", "jd3");
         digester.addSetProperties("config/hldzejghlc-server", "hldzejghlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzejghlc-server", "hldzejghlc.jd5", "jd5");
         digester.addSetProperties("config/hldzejghlc-server", "hldzejghlc.jd6", "jd6"); 
         digester.addSetProperties("config/hldzejghlc-server", "hldzejghlc.end", "end"); 
         digester.addSetNext("config/hldzejghlc-server", "setHlDzejghLcConf");
         /**华龙定增项目流程结束*/
         
         /**华龙收购项目管理开始*/
         //收购立项
         digester.addObjectCreate("config/hlsgxmlxlc-server", HlSgxmlxLcConf.class);
         digester.addSetProperties("config/hlsgxmlxlc-server", "hlsgxmlxlc.jd1", "jd1");
         digester.addSetProperties("config/hlsgxmlxlc-server", "hlsgxmlxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlsgxmlxlc-server", "hlsgxmlxlc.jd3", "jd3");
         digester.addSetProperties("config/hlsgxmlxlc-server", "hlsgxmlxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlsgxmlxlc-server", "hlsgxmlxlc.jd5", "jd5");
         digester.addSetProperties("config/hlsgxmlxlc-server", "hlsgxmlxlc.jd6", "jd6"); 
         digester.addSetProperties("config/hlsgxmlxlc-server", "hlsgxmlxlc.end", "end"); 
         digester.addSetNext("config/hlsgxmlxlc-server", "setHlSgxmlxLcConf");
         //收购方案资料报审
         digester.addObjectCreate("config/hlsgzlbslc-server", HlSgzlbsLcConf.class);
         digester.addSetProperties("config/hlsgzlbslc-server", "hlsgzlbslc.jd1", "jd1");
         digester.addSetProperties("config/hlsgzlbslc-server", "hlsgzlbslc.jd2", "jd2"); 
         digester.addSetProperties("config/hlsgzlbslc-server", "hlsgzlbslc.jd3", "jd3");
         digester.addSetProperties("config/hlsgzlbslc-server", "hlsgzlbslc.jd4", "jd4"); 
         digester.addSetProperties("config/hlsgzlbslc-server", "hlsgzlbslc.jd5", "jd5");
         digester.addSetProperties("config/hlsgzlbslc-server", "hlsgzlbslc.end", "end"); 
         digester.addSetNext("config/hlsgzlbslc-server", "setHlSgzlbsLcConf");
         //收购股转反馈问题通知
         digester.addObjectCreate("config/hlsgfkwtlc-server", HlSgfkwtLcConf.class);
         digester.addSetProperties("config/hlsgfkwtlc-server", "hlsgfkwtlc.jd1", "jd1");
         digester.addSetProperties("config/hlsgfkwtlc-server", "hlsgfkwtlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlsgfkwtlc-server", "hlsgfkwtlc.jd3", "jd3");
         digester.addSetProperties("config/hlsgfkwtlc-server", "hlsgfkwtlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlsgfkwtlc-server", "hlsgfkwtlc.jd5", "jd5");
         digester.addSetProperties("config/hlsgfkwtlc-server", "hlsgfkwtlc.end", "end"); 
         digester.addSetNext("config/hlsgfkwtlc-server", "setHlSgfkwtLcConf");
         //收购股转反馈回复审核
         digester.addObjectCreate("config/hlsgfkhflc-server", HlSgfkhfLcConf.class);
         digester.addSetProperties("config/hlsgfkhflc-server", "hlsgfkhflc.jd1", "jd1");
         digester.addSetProperties("config/hlsgfkhflc-server", "hlsgfkhflc.jd2", "jd2"); 
         digester.addSetProperties("config/hlsgfkhflc-server", "hlsgfkhflc.jd3", "jd3");
         digester.addSetProperties("config/hlsgfkhflc-server", "hlsgfkhflc.jd4", "jd4"); 
         digester.addSetProperties("config/hlsgfkhflc-server", "hlsgfkhflc.jd5", "jd5");
         digester.addSetProperties("config/hlsgfkhflc-server", "hlsgfkhflc.end", "end"); 
         digester.addSetNext("config/hlsgfkhflc-server", "setHlSgfkhfLcConf");
         /**华龙收购项目管理结束*/
         
         /**华龙挂牌项目流程开始*/
         //挂牌立项
         digester.addObjectCreate("config/hlgpxmlxlc-server", HlGpxmlxLcConf.class);
         digester.addSetProperties("config/hlgpxmlxlc-server", "hlgpxmlxlc.jd1", "jd1");
         digester.addSetProperties("config/hlgpxmlxlc-server", "hlgpxmlxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpxmlxlc-server", "hlgpxmlxlc.jd3", "jd3");
         digester.addSetProperties("config/hlgpxmlxlc-server", "hlgpxmlxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpxmlxlc-server", "hlgpxmlxlc.jd5", "jd5");
         digester.addSetProperties("config/hlgpxmlxlc-server", "hlgpxmlxlc.jd6", "jd6"); 
         digester.addSetProperties("config/hlgpxmlxlc-server", "hlgpxmlxlc.end", "end"); 
         digester.addSetNext("config/hlgpxmlxlc-server", "setHlGpxmlxLcConf");
         //申报质控
         digester.addObjectCreate("config/hlgpsbzklc-server", HlGpsbzkLcConf.class);
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.jd1", "jd1");
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.jd3", "jd3");
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.jd5", "jd5");
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.jd6", "jd6");
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.jd7", "jd7");
         digester.addSetProperties("config/hlgpsbzklc-server", "hlgpsbzklc.end", "end"); 
         digester.addSetNext("config/hlgpsbzklc-server", "setHlGpsbzkLcConf");
         //内核反馈及回复
         digester.addObjectCreate("config/hlgpnhfklc-server", HlGpnhfkLcConf.class);
         digester.addSetProperties("config/hlgpnhfklc-server", "hlgpnhfklc.jd1", "jd1");
         digester.addSetProperties("config/hlgpnhfklc-server", "hlgpnhfklc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpnhfklc-server", "hlgpnhfklc.jd3", "jd3");
         digester.addSetProperties("config/hlgpnhfklc-server", "hlgpnhfklc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpnhfklc-server", "hlgpnhfklc.end", "end"); 
         digester.addSetNext("config/hlgpnhfklc-server", "setHlGpnhfkLcConf");
         //申报股转
         digester.addObjectCreate("config/hlgpsbgzlc-server", HlGpsbgzLcConf.class);
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd1", "jd1");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd3", "jd3");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd5", "jd5");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd6", "jd6");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd7", "jd7");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd8", "jd8");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd9", "jd9");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.jd10", "jd10");
         digester.addSetProperties("config/hlgpsbgzlc-server", "hlgpsbgzlc.end", "end"); 
         digester.addSetNext("config/hlgpsbgzlc-server", "setHlGpsbgzLcConf");
         //股转反馈通知
         digester.addObjectCreate("config/hlgpgzfklc-server", HlGpgzfkLcConf.class);
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd1", "jd1");
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd3", "jd3");
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd5", "jd5");
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd6", "jd6");
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd7", "jd7");
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.jd8", "jd8");
         digester.addSetProperties("config/hlgpgzfklc-server", "hlgpgzfklc.end", "end"); 
         digester.addSetNext("config/hlgpgzfklc-server", "setHlGpgzfkLcConf");
         //股转反馈及回复
         digester.addObjectCreate("config/hlgpfkhflc-server", HlGpfkhfLcConf.class);
         digester.addSetProperties("config/hlgpfkhflc-server", "hlgpfkhflc.jd1", "jd1");
         digester.addSetProperties("config/hlgpfkhflc-server", "hlgpfkhflc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpfkhflc-server", "hlgpfkhflc.jd3", "jd3");
         digester.addSetProperties("config/hlgpfkhflc-server", "hlgpfkhflc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpfkhflc-server", "hlgpfkhflc.jd5", "jd5");
         digester.addSetProperties("config/hlgpfkhflc-server", "hlgpfkhflc.jd6", "jd6"); 
         digester.addSetProperties("config/hlgpfkhflc-server", "hlgpfkhflc.end", "end"); 
         digester.addSetNext("config/hlgpfkhflc-server", "setHlGpfkhfLcConf");
         //首次信息披露
         digester.addObjectCreate("config/hlgpscpllc-server", HlGpscplLcConf.class);
         digester.addSetProperties("config/hlgpscpllc-server", "hlgpscpllc.jd1", "jd1");
         digester.addSetProperties("config/hlgpscpllc-server", "hlgpscpllc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpscpllc-server", "hlgpscpllc.jd3", "jd3");
         digester.addSetProperties("config/hlgpscpllc-server", "hlgpscpllc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpscpllc-server", "hlgpscpllc.jd5", "jd5");
         digester.addSetProperties("config/hlgpscpllc-server", "hlgpscpllc.end", "end"); 
         digester.addSetNext("config/hlgpscpllc-server", "setHlGpscplLcConf");
         //临时信息披露
         digester.addObjectCreate("config/hlgplspllc-server", HlGplsplLcConf.class);
         digester.addSetProperties("config/hlgplspllc-server", "hlgplspllc.jd1", "jd1");
         digester.addSetProperties("config/hlgplspllc-server", "hlgplspllc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgplspllc-server", "hlgplspllc.jd3", "jd3");
         digester.addSetProperties("config/hlgplspllc-server", "hlgplspllc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgplspllc-server", "hlgplspllc.jd5", "jd5");
         digester.addSetProperties("config/hlgplspllc-server", "hlgplspllc.jd6", "jd6"); 
         digester.addSetProperties("config/hlgplspllc-server", "hlgplspllc.end", "end"); 
         digester.addSetNext("config/hlgplspllc-server", "setHlGplsplLcConf");
         //二次信息披露
         digester.addObjectCreate("config/hlgpecpllc-server", HlGpecplLcConf.class);
         digester.addSetProperties("config/hlgpecpllc-server", "hlgpecpllc.jd1", "jd1");
         digester.addSetProperties("config/hlgpecpllc-server", "hlgpecpllc.jd2", "jd2"); 
         digester.addSetProperties("config/hlgpecpllc-server", "hlgpecpllc.jd3", "jd3");
         digester.addSetProperties("config/hlgpecpllc-server", "hlgpecpllc.jd4", "jd4"); 
         digester.addSetProperties("config/hlgpecpllc-server", "hlgpecpllc.jd5", "jd5");
         digester.addSetProperties("config/hlgpecpllc-server", "hlgpecpllc.end", "end"); 
         digester.addSetNext("config/hlgpecpllc-server", "setHlGpecplLcConf");
         /**华龙挂牌项目流程结束*/
         
         
        /**
         * 华龙定增200人以上 开始
         */
         //定增200人以上项目立项
         digester.addObjectCreate("config/hldzwxmlxlc-server", HlDzwxmlxLcConf.class);
         digester.addSetProperties("config/hldzwxmlxlc-server", "hldzwxmlxlc.jd1", "jd1");
         digester.addSetProperties("config/hldzwxmlxlc-server", "hldzwxmlxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzwxmlxlc-server", "hldzwxmlxlc.jd3", "jd3");
         digester.addSetProperties("config/hldzwxmlxlc-server", "hldzwxmlxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzwxmlxlc-server", "hldzwxmlxlc.jd5", "jd5");
         digester.addSetProperties("config/hldzwxmlxlc-server", "hldzwxmlxlc.jd6", "jd6");
         digester.addSetProperties("config/hldzwxmlxlc-server", "hldzwxmlxlc.end", "end"); 
         digester.addSetNext("config/hldzwxmlxlc-server", "setHlDzwxmlxLcConf");
         //股票发行方案
         digester.addObjectCreate("config/hldzwgpfxlc-server", HlDzwgpfxLcConf.class);
         digester.addSetProperties("config/hldzwgpfxlc-server", "hldzwgpfxlc.jd1", "jd1");
         digester.addSetProperties("config/hldzwgpfxlc-server", "hldzwgpfxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzwgpfxlc-server", "hldzwgpfxlc.jd3", "jd3");
         digester.addSetProperties("config/hldzwgpfxlc-server", "hldzwgpfxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzwgpfxlc-server", "hldzwgpfxlc.jd5", "jd5");
         digester.addSetProperties("config/hldzwgpfxlc-server", "hldzwgpfxlc.end", "end"); 
         digester.addSetNext("config/hldzwgpfxlc-server", "setHlDzwgpfxLcConf");
         //申报内核
         digester.addObjectCreate("config/hldzwsbnhlc-server", HlDzwsbnhLcConf.class);
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.jd1", "jd1");
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.jd3", "jd3");
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.jd5", "jd5");
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.jd6", "jd6"); 
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.jd7", "jd7");
         digester.addSetProperties("config/hldzwsbnhlc-server", "hldzwsbnhlc.end", "end"); 
         digester.addSetNext("config/hldzwsbnhlc-server", "setHlDzwsbnhLcConf");
         //内核反馈
         digester.addObjectCreate("config/hldzwnhfklc-server", HlDzwnhfkLcConf.class);
         digester.addSetProperties("config/hldzwnhfklc-server", "hldzwnhfklc.jd1", "jd1");
         digester.addSetProperties("config/hldzwnhfklc-server", "hldzwnhfklc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzwnhfklc-server", "hldzwnhfklc.jd3", "jd3");
         digester.addSetProperties("config/hldzwnhfklc-server", "hldzwnhfklc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzwnhfklc-server", "hldzwnhfklc.end", "end"); 
         digester.addSetNext("config/hldzwnhfklc-server", "setHlDzwnhfkLcConf");
         //反馈回复
         digester.addObjectCreate("config/hldzwfkhflc-server", HlDzwfkhfLcConf.class);
         digester.addSetProperties("config/hldzwfkhflc-server", "hldzwfkhflc.jd1", "jd1");
         digester.addSetProperties("config/hldzwfkhflc-server", "hldzwfkhflc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzwfkhflc-server", "hldzwfkhflc.jd3", "jd3");
         digester.addSetProperties("config/hldzwfkhflc-server", "hldzwfkhflc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzwfkhflc-server", "hldzwfkhflc.jd5", "jd5");
         digester.addSetProperties("config/hldzwfkhflc-server", "hldzwfkhflc.end", "end"); 
         digester.addSetNext("config/hldzwfkhflc-server", "setHlDzwfkhfLcConf");
         //股转备案
         digester.addObjectCreate("config/hldzwgzbalc-server", HlDzwgzbaLcConf.class);
         digester.addSetProperties("config/hldzwgzbalc-server", "hldzwgzbalc.jd1", "jd1");
         digester.addSetProperties("config/hldzwgzbalc-server", "hldzwgzbalc.jd2", "jd2"); 
         digester.addSetProperties("config/hldzwgzbalc-server", "hldzwgzbalc.jd3", "jd3");
         digester.addSetProperties("config/hldzwgzbalc-server", "hldzwgzbalc.jd4", "jd4"); 
         digester.addSetProperties("config/hldzwgzbalc-server", "hldzwgzbalc.jd5", "jd5");
         digester.addSetProperties("config/hldzwgzbalc-server", "hldzwgzbalc.end", "end"); 
         digester.addSetNext("config/hldzwgzbalc-server", "setHlDzwgzbaLcConf");
         /**
          * 华龙定增200人以上结束
          */
         /**
          * 华龙重组项目开始
          */
         //项目立项
         digester.addObjectCreate("config/hlbgxmlxlc-server", HlBgxmlxLcConf.class);
         digester.addSetProperties("config/hlbgxmlxlc-server", "hlbgxmlxlc.jd1", "jd1");
         digester.addSetProperties("config/hlbgxmlxlc-server", "hlbgxmlxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgxmlxlc-server", "hlbgxmlxlc.jd3", "jd3");
         digester.addSetProperties("config/hlbgxmlxlc-server", "hlbgxmlxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgxmlxlc-server", "hlbgxmlxlc.jd5", "jd5");
         digester.addSetProperties("config/hlbgxmlxlc-server", "hlbgxmlxlc.jd6", "jd6");
         digester.addSetProperties("config/hlbgxmlxlc-server", "hlbgxmlxlc.end", "end"); 
         digester.addSetNext("config/hlbgxmlxlc-server", "setHlBgxmlxLcConf");
         //重组预案+意见
         digester.addObjectCreate("config/hlbgczyjlc-server", HlBgCzyjLcConf.class);
         digester.addSetProperties("config/hlbgczyjlc-server", "hlbgczyjlc.jd1", "jd1");
         digester.addSetProperties("config/hlbgczyjlc-server", "hlbgczyjlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgczyjlc-server", "hlbgczyjlc.jd3", "jd3");
         digester.addSetProperties("config/hlbgczyjlc-server", "hlbgczyjlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgczyjlc-server", "hlbgczyjlc.jd5", "jd5");
         digester.addSetProperties("config/hlbgczyjlc-server", "hlbgczyjlc.jd6", "jd6");
         digester.addSetProperties("config/hlbgczyjlc-server", "hlbgczyjlc.end", "end"); 
         digester.addSetNext("config/hlbgczyjlc-server", "setHlBgCzyjLcConf");
         //一阶段股转反馈
         digester.addObjectCreate("config/hlbgczyjwtlc-server", HlBgCzyjwtLcConf.class);
         digester.addSetProperties("config/hlbgczyjwtlc-server", "hlbgczyjwtlc.jd1", "jd1");
         digester.addSetProperties("config/hlbgczyjwtlc-server", "hlbgczyjwtlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgczyjwtlc-server", "hlbgczyjwtlc.jd3", "jd3");
         digester.addSetProperties("config/hlbgczyjwtlc-server", "hlbgczyjwtlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgczyjwtlc-server", "hlbgczyjwtlc.jd5", "jd5");
         digester.addSetProperties("config/hlbgczyjwtlc-server", "hlbgczyjwtlc.jd6", "jd6");
         digester.addSetProperties("config/hlbgczyjwtlc-server", "hlbgczyjwtlc.end", "end"); 
         digester.addSetNext("config/hlbgczyjwtlc-server", "setHlBgCzyjwtLcConf");
         //一阶段股转回复
         digester.addObjectCreate("config/hlbgczyjhflc-server", HlBgCzyjhfLcConf.class);
         digester.addSetProperties("config/hlbgczyjhflc-server", "hlbgczyjhflc.jd1", "jd1");
         digester.addSetProperties("config/hlbgczyjhflc-server", "hlbgczyjhflc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgczyjhflc-server", "hlbgczyjhflc.jd3", "jd3");
         digester.addSetProperties("config/hlbgczyjhflc-server", "hlbgczyjhflc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgczyjhflc-server", "hlbgczyjhflc.jd5", "jd5");
         digester.addSetProperties("config/hlbgczyjhflc-server", "hlbgczyjhflc.end", "end"); 
         digester.addSetNext("config/hlbgczyjhflc-server", "setHlBgCzyjhfLcConf");
         //申报内核
         digester.addObjectCreate("config/hlbgsbnhlc-server", HlBgSbnhLcConf.class);
         digester.addSetProperties("config/hlbgsbnhlc-server", "hlbgsbnhlc.jd1", "jd1");
         digester.addSetProperties("config/hlbgsbnhlc-server", "hlbgsbnhlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgsbnhlc-server", "hlbgsbnhlc.jd3", "jd3");
         digester.addSetProperties("config/hlbgsbnhlc-server", "hlbgsbnhlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgsbnhlc-server", "hlbgsbnhlc.jd5", "jd5");
         digester.addSetProperties("config/hlbgsbnhlc-server", "hlbgsbnhlc.jd6", "jd6");
         digester.addSetProperties("config/hlbgsbnhlc-server", "hlbgsbnhlc.end", "end"); 
         digester.addSetNext("config/hlbgsbnhlc-server", "setHlBgSbnhLcConf");
         //内核回复
         digester.addObjectCreate("config/hlbgnhhflc-server", HlBgNhhfLcConf.class);
         digester.addSetProperties("config/hlbgnhhflc-server", "hlbgnhhflc.jd1", "jd1");
         digester.addSetProperties("config/hlbgnhhflc-server", "hlbgnhhflc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgnhhflc-server", "hlbgnhhflc.jd3", "jd3");
         digester.addSetProperties("config/hlbgnhhflc-server", "hlbgnhhflc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgnhhflc-server", "hlbgnhhflc.end", "end"); 
         digester.addSetNext("config/hlbgnhhflc-server", "setHlBgNhhfLcConf");
         //二阶段股转反馈问题
         digester.addObjectCreate("config/hlbgejwtlc-server", HlBgEjwtLcConf.class);
         digester.addSetProperties("config/hlbgejwtlc-server", "hlbgejwtlc.jd1", "jd1");
         digester.addSetProperties("config/hlbgejwtlc-server", "hlbgejwtlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgejwtlc-server", "hlbgejwtlc.jd3", "jd3");
         digester.addSetProperties("config/hlbgejwtlc-server", "hlbgejwtlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgejwtlc-server", "hlbgejwtlc.jd5", "jd5");
         digester.addSetProperties("config/hlbgejwtlc-server", "hlbgejwtlc.jd6", "jd6");
         digester.addSetProperties("config/hlbgejwtlc-server", "hlbgejwtlc.end", "end"); 
         digester.addSetNext("config/hlbgejwtlc-server", "setHlBgEjwtLcConf");
         //二阶段股转反馈回复
         digester.addObjectCreate("config/hlbgejhflc-server", HlBgEjhfLcConf.class);
         digester.addSetProperties("config/hlbgejhflc-server", "hlbgejhflc.jd1", "jd1");
         digester.addSetProperties("config/hlbgejhflc-server", "hlbgejhflc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgejhflc-server", "hlbgejhflc.jd3", "jd3");
         digester.addSetProperties("config/hlbgejhflc-server", "hlbgejhflc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgejhflc-server", "hlbgejhflc.jd5", "jd5");
         digester.addSetProperties("config/hlbgejhflc-server", "hlbgejhflc.end", "end"); 
         digester.addSetNext("config/hlbgejhflc-server", "setHlBgEjhfLcConf");
         //申报备案
         digester.addObjectCreate("config/hlbgsbzllc-server", HlBgSbzlLcConf.class);
         digester.addSetProperties("config/hlbgsbzllc-server", "hlbgsbzllc.jd1", "jd1");
         digester.addSetProperties("config/hlbgsbzllc-server", "hlbgsbzllc.jd2", "jd2"); 
         digester.addSetProperties("config/hlbgsbzllc-server", "hlbgsbzllc.jd3", "jd3");
         digester.addSetProperties("config/hlbgsbzllc-server", "hlbgsbzllc.jd4", "jd4"); 
         digester.addSetProperties("config/hlbgsbzllc-server", "hlbgsbzllc.jd5", "jd5");
         digester.addSetProperties("config/hlbgsbzllc-server", "hlbgsbzllc.end", "end"); 
         digester.addSetNext("config/hlbgsbzllc-server", "setHlBgSbzlLcConf");
         /**
          * 华龙重组项目结束
          */
         /**
          * 一般性财务顾问开始
          */
         //项目立项
         digester.addObjectCreate("config/hlybcwxmlxlc-server", HlYbcwxmlxLcConf.class);
         digester.addSetProperties("config/hlybcwxmlxlc-server", "hlybcwxmlxlc.jd1", "jd1");
         digester.addSetProperties("config/hlybcwxmlxlc-server", "hlybcwxmlxlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlybcwxmlxlc-server", "hlybcwxmlxlc.jd3", "jd3");
         digester.addSetProperties("config/hlybcwxmlxlc-server", "hlybcwxmlxlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlybcwxmlxlc-server", "hlybcwxmlxlc.jd5", "jd5");
         digester.addSetProperties("config/hlybcwxmlxlc-server", "hlybcwxmlxlc.end", "end"); 
         digester.addSetNext("config/hlybcwxmlxlc-server", "setHlYbcwxmlxLcConf");
         //工作进度
         digester.addObjectCreate("config/hlybcwcwgzjdlc-server", HlYbcwCwgzjdLcConf.class);
         digester.addSetProperties("config/hlybcwcwgzjdlc-server", "hlybcwcwgzjdlc.jd1", "jd1");
         digester.addSetProperties("config/hlybcwcwgzjdlc-server", "hlybcwcwgzjdlc.jd2", "jd2"); 
         digester.addSetProperties("config/hlybcwcwgzjdlc-server", "hlybcwcwgzjdlc.jd3", "jd3");
         digester.addSetProperties("config/hlybcwcwgzjdlc-server", "hlybcwcwgzjdlc.jd4", "jd4"); 
         digester.addSetProperties("config/hlybcwcwgzjdlc-server", "hlybcwcwgzjdlc.jd5", "jd5");
         digester.addSetProperties("config/hlybcwcwgzjdlc-server", "hlybcwcwgzjdlc.end", "end"); 
         digester.addSetNext("config/hlybcwcwgzjdlc-server", "setHlYbcwCwgzjdLcConf");
         /**
          * 一般性财务顾问结束
          */
         
         digester.addObjectCreate("config/hlqtlcsplc-server", HlQtsxLcConf.class);
         digester.addSetProperties("config/hlqtlcsplc-server", "hlqtlcsplc.jd1", "jd1");
         digester.addSetProperties("config/hlqtlcsplc-server", "hlqtlcsplc.jd2", "jd2"); 
         digester.addSetProperties("config/hlqtlcsplc-server", "hlqtlcsplc.jd3", "jd3");
         digester.addSetProperties("config/hlqtlcsplc-server", "hlqtlcsplc.jd4", "jd4"); 
         digester.addSetProperties("config/hlqtlcsplc-server", "hlqtlcsplc.jd5", "jd5");
         digester.addSetProperties("config/hlqtlcsplc-server", "hlqtlcsplc.end", "end"); 
         digester.addSetNext("config/hlqtlcsplc-server", "setHlQtsxLcConf");
        try {
            return (SystemConfig) digester.parse(input);
        } catch (SAXException e) {
            throw new ConfigParserException(e.toString());
        } catch (Exception e) {
            throw new ConfigParserException(e.toString());
        }
        
    }
}
