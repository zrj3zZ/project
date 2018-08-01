package com.iwork.app.login.tools;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.log4j.Logger;
public class AdTools {
	/**
	  * AD 验证
	  * 
	  * @param userName
	  * @param passWord
	  * @return
	  */
	private static Logger logger = Logger.getLogger(AdTools.class);
	 public static boolean adTrueOrFalse(String userName, String passWord) {
	  String url = new String("ldap://xiaomi.net/MIOffice");
	  Hashtable env = new Hashtable();
	  DirContext ctx;
	  env.put(Context.SECURITY_AUTHENTICATION, "simple");
	  env.put(Context.SECURITY_PRINCIPAL, userName);
	  env.put(Context.SECURITY_CREDENTIALS, passWord);
	  env.put(Context.INITIAL_CONTEXT_FACTORY,
	    "com.sun.jndi.ldap.LdapCtxFactory");
	  env.put(Context.PROVIDER_URL, url);
	  try {
	   ctx = new InitialDirContext(env);
	   ctx.close();
	   return true;
	  } catch (NamingException e) {logger.error(e,e);
	   return false;
	  }
	 }
	 // 使用LDAP验证密码
	 public static void main(String[] args) {
	  String userName = "xiaomi\\shikai"; // 用户名称
	  String pwd = "abcd123,";
	  String url = new String("ldap://10.0.2.30:389/");
	  Hashtable<String, String> env = new Hashtable<String ,String>();
	  DirContext ctx = null;
	  env.put(Context.SECURITY_AUTHENTICATION, "simple");
	  env.put(Context.SECURITY_PRINCIPAL, userName);
	  env.put(Context.DNS_URL, "OU=MIOffice, DC=xiaomi, DC=net");
	  if ("".equals(pwd.trim())) {
	   pwd = null;
	  } 
	  env.put(Context.SECURITY_CREDENTIALS, pwd);
	  env.put(Context.SECURITY_AUTHENTICATION, "simple");
	  env.put(Context.INITIAL_CONTEXT_FACTORY,
	    "com.sun.jndi.ldap.LdapCtxFactory");
	  env.put(Context.PROVIDER_URL, url);
	  try {
	   ctx = new InitialDirContext(env);
	   ctx.close();
	  } catch (NamingException e) {
	   logger.error(e,e);
	  }
	 }
}
