package com.iwork.app.scancode.service;

import java.util.Map;

import com.iwork.app.scancode.constant.ScanCodeConstant;
import com.iwork.app.scancode.factory.ScanCodeFactory;
import com.iwork.app.scancode.interceptor.ScanCodeInterface;

public class ScanCodeService {
	
	/**
	 * 执行扫码操作
	 * @param type
	 * @param codestr
	 * @return
	 */
	public String doScanCode(String type,String codestr){
		StringBuffer str = new StringBuffer();
		if(type!=null&&codestr!=null){
			
		}else if(type==null&&codestr!=null){
			type = codestr.substring(0,codestr.indexOf("_"));
		}
		if(type==null){
			str.append("error");
		}else{
			ScanCodeInterface sci =  ScanCodeFactory.getScanCodeImpl(type);
			String result = sci.getResult(codestr);
			str.append(result);
		}
		return str.toString();
	}
	
	/**
	 * 加载条形码/二维码
	 * @param map
	 * @return
	 */
	public String showQrCode(String type,Map map){
		ScanCodeInterface sci =  ScanCodeFactory.getScanCodeImpl(type);
		String content = sci.buildQRCode(map, ScanCodeConstant.CODE_TYPE_QRCODE);
		return content;
	}
	
	/**
	 * 加载条形码/二维码
	 * @param map
	 * @return
	 */
	public String showBarCode(String type,Map map){
		ScanCodeInterface sci =  ScanCodeFactory.getScanCodeImpl(type);
		String content = sci.buildBarCode(map, ScanCodeConstant.CODE_TYPE_BARCODE);
		return content;
	}
}
