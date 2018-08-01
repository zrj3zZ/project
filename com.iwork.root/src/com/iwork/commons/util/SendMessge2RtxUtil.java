package com.iwork.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import com.iwork.app.conf.SystemConfig;
import org.apache.log4j.Logger;

public class SendMessge2RtxUtil {
	private static Logger logger = Logger.getLogger(SendMessge2RtxUtil.class);
	/**
	 * 发送IM即时消息
	 * @param sender
	 * @param receiver
	 * @param info
	 */
	public static void sendIMforHttp(String sender, String receiver, String info) {
		int iRetCode = 0;
		String title = SystemConfig._imConf.getTitle();
		URLDecoder urld = new URLDecoder();
		try {
			StringBuffer postaddress = new StringBuffer(SystemConfig._imConf.getUrl()); 

			URL url = new URL(postaddress.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			StringBuffer pro = new StringBuffer("msg=").append(info)
					.append("&receiver=").append(receiver).append("&title=")
					.append(title);
			con.getOutputStream().write(pro.toString().getBytes());
			con.getOutputStream().flush();
			con.getOutputStream().close();
			InputStream in = con.getInputStream();
			in.close();
		} catch (MalformedURLException e) {
			logger.error(e,e);
		} catch (ProtocolException e) {
			logger.error(e,e);
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
}
