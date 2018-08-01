package com.iwork.app.login.adapter;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.iwork.app.login.constant.LoginConst;
import com.iwork.app.login.control.LoginContext;
import com.iwork.app.login.control.LoginInterface;
import org.apache.log4j.Logger;
public class KoaADLoginAdapterImpl implements LoginInterface {
	private static Logger logger = Logger.getLogger(KoaADLoginAdapterImpl.class);
	LoginContext _logincontext;
	public String getUserId() {
		String uid = "";
		if(_logincontext!=null){
			uid = _logincontext.getUid();
		}
		return uid;
	}

	public int login(LoginContext context) {
		String userName = "UBOX\\"+context.getUid(); // 用户名称
		  String passwd = context.getPwd(); 
		  String url = new String("ldap://192.168.8.2:389/");
		  Hashtable<String, String> env = new Hashtable<String ,String>();
		  DirContext ctx = null; 
		  env.put(Context.SECURITY_AUTHENTICATION, "simple");
		  env.put(Context.SECURITY_PRINCIPAL, userName);
		  env.put(Context.DNS_URL, "OU=Ubox, DC=ubox, DC=ad");
		  if ("".equals(passwd.trim())) {
		   passwd = null;
		  } 
		  env.put(Context.SECURITY_CREDENTIALS, passwd);
		  env.put(Context.SECURITY_AUTHENTICATION, "simple");
		  env.put(Context.INITIAL_CONTEXT_FACTORY,
		    "com.sun.jndi.ldap.LdapCtxFactory");
		  env.put(Context.PROVIDER_URL, url);
		  try {
		   ctx = new InitialDirContext(env);
		   ctx.close();
		   return LoginConst.LOGIN_STATUS_OK;
		  } catch (NamingException e) {
		   logger.error(e,e);
		   return LoginConst.LOGIN_STATUS_PWD_ERROR;
		  }
		
	}

}
