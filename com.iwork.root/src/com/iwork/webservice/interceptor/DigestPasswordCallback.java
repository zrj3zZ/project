package com.iwork.webservice.interceptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
/**
 * 对WS-Security中Digest式密码的处理Handler.
 * 
 * @author calvin
 */
public class DigestPasswordCallback implements CallbackHandler {
	private Map<String, String> passwords;   
	 
	 public DigestPasswordCallback(){
		 	passwords=new HashMap<String, String>();
	     	passwords.put("admin", "admin");   
	        passwords.put("test", "test");   
	        passwords.put("userName", "password"); 
	       
	 }

	/**
	 * 根据用户名查出数据库中用户的明文密码,交由框架进行处理并与客户端提交的Digest进行比较.
	 */
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
        //为admin用户设置密码"admin", 为其他用户统一设置密码"user".
        if (pc.getIdentifier().equals("admin")) {
                pc.setPassword("admin");
        } else {
                pc.setPassword("user");
        }
	}
}