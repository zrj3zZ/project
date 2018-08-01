package com.test;

import org.tempuri.Service1;


public class SendAction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String username = "messageuser029";
		String pwd = "djq04dq1d0q";
		MD5 md5 = new MD5();
		String md5pwd = md5.getEncryptedPwd(username+pwd);
		Service1 service = new Service1();
		int num = service.getBasicHttpBindingIService1().smsSend("messageuser029", "djq04dq1d0q", md5pwd, "13691397991", "˯����");
	}

}
