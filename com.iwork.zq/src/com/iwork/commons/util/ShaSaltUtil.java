package com.iwork.commons.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.service.OrgUserService;
import com.iwork.core.util.SpringBeanUtil;
import org.apache.log4j.Logger;
public class ShaSaltUtil {
	private static Logger logger = Logger.getLogger(ShaSaltUtil.class);
	private static OrgUserService orgUserService;
	public static String getStringSalt(){
		StringBuilder sb = new StringBuilder();
		String salt = getSalt();
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			byte[] bytes = md.digest();
			for(int i=0; i< bytes.length ;i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}  
		}catch(Exception e){  
			
		}
		return sb.toString();
	}
	/**
	 * 获取SHA加密方式对象
	 * @param msg
	 * @param userid
	 * @return
	 */
	public static String getEncryptedPwd(String msg,String userid){
		StringBuilder sb = new StringBuilder();
		try{
			orgUserService = (OrgUserService)SpringBeanUtil.getBean("orgUserService");
			OrgUser userModel = orgUserService.getUserModel(userid);
			String salt=userModel.getExtend3();
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			if(salt==null||"".equals(salt)){
				return "";
			}
			md.update(salt.getBytes());
			byte[] bytes = md.digest(msg.getBytes());
			for(int i=0; i< bytes.length ;i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
		}catch(Exception e){  
			
		}
		return sb.toString();
	}
	/**
	 * MD5加密方式登录后转换为SHA的加密方式
	 * @param msg
	 * @param userid
	 * @return
	 */
	public static String getEncryptedAddPwd(String msg,String userid){
		StringBuilder sb = new StringBuilder();
		boolean flag=false;
		try{
			orgUserService = (OrgUserService)SpringBeanUtil.getBean("orgUserService");
			OrgUser userModel = orgUserService.getUserModel(userid);
			String salt=userModel.getExtend3();
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			if(salt==null||"".equals(salt)){
				flag=true;
				salt=getSalt();
				userModel.setExtend3(salt);
			}
			md.update(salt.getBytes());
			byte[] bytes = md.digest(msg.getBytes());
			for(int i=0; i< bytes.length ;i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			if(flag){
				userModel.setPassword(sb.toString());
				orgUserService.updateBoData(userModel);
			}
		}catch(Exception e){  
			logger.error(e,e);
		}
		return sb.toString();
	}
	/**
	 * 根据SHA加密对象获取加密后的密码
	 * @param msg
	 * @param salt
	 * @param flag
	 * @return
	 */
	public static String getEncryptedPwd(String msg,String salt,boolean flag){
		StringBuilder sb = new StringBuilder();
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(msg.getBytes());
			for(int i=0; i< bytes.length ;i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
		}catch(Exception e){  
			
		}
		return sb.toString();
	}
	
	
	private static String getSalt(){  
        SecureRandom sr;  
        byte[] salt = new byte[16];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");  
            sr.nextBytes(salt);  
        } catch (Exception e) {  
            logger.error(e,e);  
        }   
        return salt.toString();  
    } 
}
