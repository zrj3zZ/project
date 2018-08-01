package com.iwork.app.weixin.process.action.qy.util;

import com.iwork.app.conf.SystemConfig;

public class Constants {
	/**微信配置，得修改
	 * 常量说明：
	 * 此处定义的常量需要持久化，可以保存在数据库中，在需要的地方读取。
	 * 在多企业号中，最好以每个应用来定义。	SystemConfig._weixinConf.getServer().equals("on");
	 */
	public static final int AGENTID = Integer.parseInt(SystemConfig._weixinConf.getAgentid());
	public static final String TOKEN = SystemConfig._weixinConf.getToken();
	public static final String CORPID = SystemConfig._weixinConf.getCorpId();
	public static final String SECRET = SystemConfig._weixinConf.getConfigSecret();
	public static final String encodingAESKey =  SystemConfig._weixinConf.getEncodingAESKey();
}
