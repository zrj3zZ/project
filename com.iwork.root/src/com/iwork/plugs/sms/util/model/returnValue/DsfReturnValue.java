package com.iwork.plugs.sms.util.model.returnValue;

import org.apache.log4j.Logger;
/**
 * 处理东时方发送短信的返回的键值对应关系的工具类
 * @author WangRongtao
 *
 */

public class DsfReturnValue {
	private static Logger logger = Logger.getLogger(DsfReturnValue.class);
	private final String SUCCESS_1 = "发送成功";
	private final String FAIL_1 = "参数无效";
	private final String FAIL_2 = "通道不存在或者当前业务不支持此通道";
	private final String FAIL_3 = "定时格式错误";
	private final String FAIL_4 = "接收号码无效";
	private final String FAIL_5 = "提交号码个数超过上限,每个通道都有批量提交的上限.详细值请参考通道说明";
	private final String FAIL_6 = "发送短信内容长度不符合要求,参考通道要求长度";
	private final String FAIL_7 = "当前账户余额不足";
	private final String FAIL_8 = "网关发送短信时出现异常";
	private final String FAIL_9 = "用户或者密码没输入";
	private final String FAIL_10 = "企业ID或者会员账号不存在";
	private final String FAIL_11 = "密码错误";
	private final String FAIL_12 = "账户锁定";
	private final String FAIL_13 = "网关状态关闭";
	private final String FAIL_14 = "验证用户时执行异常";
	private final String FAIL_15 = "网关初始化失败";
	private final String FAIL_16 = "当前IP已被系统屏蔽,可能是与您设置的接入IP不同或者是失败次数太多";
	private final String FAIL_17 = "发送异常";
	private final String FAIL_18 = "账号未审核";
	private final String FAIL_19 = "当前时间不允许此通道工作";
	private final String FAIL_20 = "传输密钥未设置，请登陆平台设置";
	private final String FAIL_21 = "提取密钥异常";
	private final String FAIL_22 = "签名验证失败";
	private final String FAIL_23 = "发现屏蔽关键字";
	private final String FAIL_24 = "运营商返回失败代码";
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
	public String getFAIL_15() {
		return FAIL_15;
	}
	public String getFAIL_16() {
		return FAIL_16;
	}
	public String getFAIL_17() {
		return FAIL_17;
	}
	public String getFAIL_18() {
		return FAIL_18;
	}
	public String getFAIL_19() {
		return FAIL_19;
	}
	public String getFAIL_20() {
		return FAIL_20;
	}
	public String getFAIL_21() {
		return FAIL_21;
	}
	public String getFAIL_22() {
		return FAIL_22;
	}
	public String getFAIL_23() {
		return FAIL_23;
	}
	public String getFAIL_24() {
		return FAIL_24;
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
				if(no>0){
					returnValue = successtitle;
				}else if(no>=-199&&no<=-100){
					returnValue = this.FAIL_24+":"+no;
				}else{
					switch(no){
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
					  case -15 : returnValue = wrongno+FAIL_15;break;
					  case -16 : returnValue = wrongno+FAIL_16;break;
					  case -17 : returnValue = wrongno+FAIL_17;break;
					  case -18 : returnValue = wrongno+FAIL_18;break;
					  case -19 : returnValue = wrongno+FAIL_19;break;
					  case -20 : returnValue = wrongno+FAIL_20;break;
					  case -21 : returnValue = wrongno+FAIL_21;break;
					  case -22 : returnValue = wrongno+FAIL_22;break;
					  case -23 : returnValue = wrongno+FAIL_23;break;
					  default : returnValue = "发送密码失败!"+wrongno;
					}
				}
			}
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			return returnValue;
		}
	}
}
