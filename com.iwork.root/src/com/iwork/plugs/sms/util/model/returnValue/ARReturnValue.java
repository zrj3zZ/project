package com.iwork.plugs.sms.util.model.returnValue;
import org.apache.log4j.Logger;

/**
 * 处理奥软发送短信的返回的键值对应关系的工具类
 * @author WangRongtao
 *
 */

public class ARReturnValue {
	private static Logger logger = Logger.getLogger(ARReturnValue.class);
	private final String SUCCESS_1 = "发送成功";
	private final String FAIL_1 = "不能初始化";
	private final String FAIL_2 = "网络不通";
	private final String FAIL_3 = "一次发送的手机号码过多";
	private final String FAIL_4 = "内容包含不合法文字";
	private final String FAIL_5 = "登录账户错误";
	private final String FAIL_6 = "通信数据传送";
	private final String FAIL_7 = "没有进行参数初始化";
	private final String FAIL_8 = "扩展号码长度不对";
	private final String FAIL_9 = "手机号码不合法";
	private final String FAIL_10 = "号码太长";
	private final String FAIL_11 = "内容太长";
	private final String FAIL_12 = "关键字异常";
	private final String FAIL_13 = "余额不足";
	private final String FAIL_14 = "扩展号不正确";
	private final String FAIL_50 = "配置参数错误";
	public String getSUCCESS_1() {
		return SUCCESS_1;
	}
	public String getFAIL_1() {
		return FAIL_1;
	}
	public String getFAIL_2() {
		return FAIL_2;
	}
	public String getFAIL_3() {
		return FAIL_3;
	}
	public String getFAIL_4() {
		return FAIL_4;
	}
	public String getFAIL_5() {
		return FAIL_5;
	}
	public String getFAIL_6() {
		return FAIL_6;
	}
	public String getFAIL_7() {
		return FAIL_7;
	}
	public String getFAIL_8() {
		return FAIL_8;
	}
	public String getFAIL_9() {
		return FAIL_9;
	}
	public String getFAIL_10() {
		return FAIL_10;
	}
	public String getFAIL_11() {
		return FAIL_11;
	}
	public String getFAIL_12() {
		return FAIL_12;
	}
	public String getFAIL_13() {
		return FAIL_13;
	}
	public String getFAIL_14() {
		return FAIL_14;
	}
	public String getFAIL_50() {
		return FAIL_50;
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
					  case 1 : returnValue = successtitle;break;
					  case -1 : returnValue = wrongno+FAIL_1;break;
					  case -2 : returnValue = wrongno+FAIL_2;break;
					  case -3 : returnValue = wrongno+FAIL_3;break;
					  case -4 : returnValue = wrongno+FAIL_4;break;
					  case -5 : returnValue = wrongno+FAIL_5;break;
					  case -6 : returnValue = wrongno+FAIL_6;break;
					  case -7 : returnValue = wrongno+FAIL_7;break;
					  case -8 : returnValue = wrongno+FAIL_8;break;
					  case -9 : returnValue = wrongno+FAIL_9;break;
					  case -10 : returnValue = wrongno+FAIL_10;break;
					  case -11 : returnValue = wrongno+FAIL_11;break;
					  case -12 : returnValue = wrongno+FAIL_12;break;
					  case -13 : returnValue = wrongno+FAIL_13;break;
					  case -14 : returnValue = wrongno+FAIL_14;break;
					  case -50 : returnValue = wrongno+FAIL_50;break;
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
