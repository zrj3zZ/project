package com.ibpmsoft.project.zqb.upload;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import com.ibpmsoft.project.zqb.util.ConfigUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Fileupload {

    private static Logger logger = Logger.getLogger(Fileupload.class);
    public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
    public String getConfigUUID(String parameter) {
  		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
  		String result="";
  		if(parameter!=null&&!"".equals(parameter)){
  			result=config.get(parameter)==null?"":config.get(parameter);
  		}
  		return result;
  	}
    public static String fileload(String url, File file, String filename) {
        String body = null;
/*
        if (url == null || url.equals("")) {
            return "参数不合法";
        }
        if (!file.exists()) {
            return "要上传的文件名不存在";
        }*/

        PostMethod postMethod = new PostMethod(url);

        try {

            // FilePart：用来上传文件的类,file即要上传的文件
            FilePart fp = new FilePart(filename, file);
            Part[] parts = { fp };

            // 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
            MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
            postMethod.setRequestEntity(mre);

            HttpClient client = new HttpClient();
            // 由于要上传的文件可能比较大 , 因此在此设置最大的连接超时时间
            client.getHttpConnectionManager().getParams() .setConnectionTimeout(50000);

            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                body = "success";

            } else {
                body = "fail";
            }
        } catch (Exception e) {
            logger.warn("上传文件异常", e);
        } finally {
            // 释放连接
            postMethod.releaseConnection();
        }

        return body;
    }


    /*这段代码是国信提供的源码
     * 配置文件里   gxbdurl   这个参数是    https://10.33.192.79/api/v1/file/scan
     * 
     * public static void main(String[] args) throws Exception {
        String apikey = "";
        String filename = "index.html";
        String filepath = "/target/index.html";

        StringBuilder stringBuilder =
                        new StringBuilder("https://10.33.192.79/api/v1/file/scan")
                        .append("?apikey=").append(apikey).append("&file=")
                        .append(filename);
        String url = stringBuilder.toString();
        System.out.println(url);

        String body = fileload(url, new File(filepath), filename);
        System.out.println(body);
    }*/
    public String fileScann(String filename,String filepath){
    	String apikey = getConfigUUID("gxbdsm")==null?"":getConfigUUID("gxbdsm");
    	String bdsmurl = getConfigUUID("gxbdurl")==null?"":getConfigUUID("gxbdurl");
    	String apikeyname = getConfigUUID("apikeyname")==null?"":getConfigUUID("apikeyname");
    	StringBuilder stringBuilder =
                new StringBuilder(bdsmurl)
                .append("?"+apikeyname+"=").append(apikey).append("&file=")
                .append(filename);
		String url = stringBuilder.toString();
		//System.out.println(url);
		
		String body = fileload(url, new File(filepath), filename);
		System.out.println("扫描文件---"+filename+"："+body);
    	return body;
    }
}
