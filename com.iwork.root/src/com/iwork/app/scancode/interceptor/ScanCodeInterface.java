package com.iwork.app.scancode.interceptor;

import java.util.Map;

public interface ScanCodeInterface {
	/**
	 * 获取二维码结果
	 * @param codeStr
	 * @return 
	 */
	public String getResult(String codeStr);
	
	
	/**
	 * 构建条形码或者条形码
	 * @param map
	 */
	public String buildQRCode(Map map,String codeType);
	
	/**
	 * 构建条形码或者条形码
	 * @param map
	 */
	public String buildBarCode(Map map,String codeType);

}
