package com.iwork.plugs.sms.util.model.returnValue;

import org.apache.log4j.Logger;

/**
 * 处理天润融通发送短信的返回的键值对应关系的工具类
 * @author WangRongtao
 *
 */

public class TlrtReturnValue {
	private static Logger logger = Logger.getLogger(TlrtReturnValue.class);
	private final String SUCCESS_0 = "发送成功";
	private final String FAIL_500 = "参数无效";
	private final String FAIL_501 = "用户验证没有通过";
	private final String FAIL_502 = "验证没通过";
	private final String FAIL_503 = "资费不足";
	private final String FAIL_504 = "指定的业务尚未开通";
	private final String FAIL_505 = "非法的接收用户";
	private final String FAIL_506 = "短信息内容中含有非法信息";
	private final String FAIL_507 = "内容非法";
	private final String FAIL_508 = "发送限额已满";
	private final String FAIL_550 = "数据库操作错误";
	private final String FAIL_600 = "其他错误";
	public String getSUCCESS_0() {
		return SUCCESS_0;
	}
	public String getFAIL_500() {
		return FAIL_500;
	}
	public String getFAIL_501() {
		return FAIL_501;
	}
	public String getFAIL_502() {
		return FAIL_502;
	}
	public String getFAIL_503() {
		return FAIL_503;
	}
	public String getFAIL_504() {
		return FAIL_504;
	}
	public String getFAIL_505() {
		return FAIL_505;
	}
	public String getFAIL_506() {
		return FAIL_506;
	}
	public String getFAIL_507() {
		return FAIL_507;
	}
	public String getFAIL_508() {
		return FAIL_508;
	}
	public String getFAIL_550() {
		return FAIL_550;
	}
	public String getFAIL_600() {
		return FAIL_600;
	}
	/**
	 * 根据发送短信后的返回值，取相应的返回信息
	 * @param returnNo 发送短信后返回的值
	 * @return
	 */
	public String returnValue(String returnNo,String successtitle){
		String returnValue = "发送密码失败,请重新获取!";
		try{
			if(returnNo!=null&&!"".equals(returnNo)){
				int no = Integer.parseInt(returnNo);
				String wrongno = "错误编号："+no;
				switch(no){
					  case 0 : returnValue = successtitle;break;
					  case 500 : returnValue = wrongno+FAIL_500;break;
					  case 501 : returnValue = wrongno+FAIL_501;break;
					  case 502 : returnValue = wrongno+FAIL_502;break;
					  case 503 : returnValue = wrongno+FAIL_503;break;
					  case 504 : returnValue = wrongno+FAIL_504;break;
					  case 505 : returnValue = wrongno+FAIL_505;break;
					  case 506 : returnValue = wrongno+FAIL_506;break;
					  case 507 : returnValue = wrongno+FAIL_507;break;
					  case 508 : returnValue = wrongno+FAIL_508;break;
					  case 550 : returnValue = wrongno+FAIL_550;break;
					  case 600 : returnValue = wrongno+FAIL_600;break;
					  default : returnValue = "发送密码失败!"+wrongno;
				}
			}
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			return returnValue;
		}
	}
}
