package com.iwork.wechat.util;

import com.iwork.app.conf.SystemConfig;
import com.iwork.app.conf.WeiXinConf;
import com.iwork.app.weixin.core.pojo.Menu;
import com.iwork.app.weixin.core.pojo.UserCodeModel;
import com.iwork.app.weixin.core.pojo.WeixinMedia;
import com.iwork.wechat.pojo.AccessToken;
import com.iwork.wechat.token.TokenThread;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletContext;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinUtil
{
  private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
  public static final String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APPID&corpsecret=APPSECRET";
  public static String menu_create_url = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN&agentid=" + SystemConfig._weixinConf.getAgentid();
  public static String get_userid_for_code_url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE&agentid=AGENTID";
  public static String token = SystemConfig._weixinConf.getToken();
  public static String encodingAESKey = SystemConfig._weixinConf.getEncodingAESKey();
  public static String corpId = SystemConfig._weixinConf.getCorpId();
  private static Object lock = new Object();
  private static WeixinUtil instance;

  public static WeixinUtil getInstance()
  {
    if (instance == null) {
      synchronized (lock) {
        if (instance == null) {
          instance = new WeixinUtil();
        }
      }
    }
    return instance;
  }

  public static String getFileEndWitsh(String contentType)
  {
    String fileEndWitsh = "";
    if ("image/jpeg".equals(contentType))
      fileEndWitsh = ".jpg";
    else if ("audio/mpeg".equals(contentType))
      fileEndWitsh = ".mp3";
    else if ("audio/amr".equals(contentType))
      fileEndWitsh = ".amr";
    else if ("video/mp4".equals(contentType))
      fileEndWitsh = ".mp4";
    else if ("video/mpeg4".equals(contentType))
      fileEndWitsh = ".mp4";
    return fileEndWitsh;
  }

  public static WeixinMedia uploadMedia(String accessToken, String type, String mediaFileUrl)
  {
    WeixinMedia weixinMedia = null;

    String uploadMediaUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);

    String boundary = "------------7da2e536604c8";
    try {
      URL uploadUrl = new URL(uploadMediaUrl);
      HttpURLConnection uploadConn = (HttpURLConnection)uploadUrl.openConnection();
      uploadConn.setDoOutput(true);
      uploadConn.setDoInput(true);
      uploadConn.setRequestMethod("POST");

      uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

      OutputStream outputStream = uploadConn.getOutputStream();

      URL mediaUrl = new URL(mediaFileUrl);
      HttpURLConnection meidaConn = (HttpURLConnection)mediaUrl.openConnection();
      meidaConn.setDoOutput(true);
      meidaConn.setRequestMethod("GET");

      String contentType = meidaConn.getHeaderField("Content-Type");

      String fileExt = getFileEndWitsh(contentType);

      outputStream.write(("--" + boundary + "\r\n").getBytes());
      outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n", new Object[] { fileExt }).getBytes());
      outputStream.write(String.format("Content-Type: %s\r\n\r\n", new Object[] { contentType }).getBytes());

      BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
      byte[] buf = new byte[8096];
      int size = 0;
      while ((size = bis.read(buf)) != -1)
      {
        outputStream.write(buf, 0, size);
      }

      outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
      outputStream.close();
      bis.close();
      meidaConn.disconnect();

      InputStream inputStream = uploadConn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      StringBuffer buffer = new StringBuffer();
      String str = null;
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferedReader.close();
      inputStreamReader.close();

      inputStream.close();
      inputStream = null;
      uploadConn.disconnect();

      JSONObject jsonObject = JSONObject.fromObject(buffer.toString());

      System.out.println("打印测试结果" + jsonObject);
      weixinMedia = new WeixinMedia();
      weixinMedia.setType(jsonObject.getString("type"));

      if ("thumb".equals(type))
        weixinMedia.setMediaId(jsonObject.getString("thumb_media_id"));
      else
        weixinMedia.setMediaId(jsonObject.getString("media_id"));
      weixinMedia.setCreatedAt(jsonObject.getInt("created_at"));
    } catch (Exception e) {
      weixinMedia = null;
      String error = String.format("上传媒体文件失败：%s", new Object[] { e });
      System.out.println(error);
    }
    return weixinMedia;
  }

  public String downloadMedia(String accessToken, String mediaId, String savePath)
  {
    String filePath = null;

    String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
    requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
    System.out.println(requestUrl);
    try {
      URL url = new URL(requestUrl);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setDoInput(true);
      conn.setRequestMethod("GET");
      savePath = ServletActionContext.getServletContext().getRealPath(savePath.replaceAll("\\.\\.", ""));
      if (!savePath.endsWith("/")) {
        savePath = savePath + "/";
      }

      String fileExt = getFileEndWitsh(conn.getHeaderField("Content-Type"));

      filePath = savePath + mediaId + fileExt;
      BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
      FileOutputStream fos = new FileOutputStream(new File(filePath));
      byte[] buf = new byte[8096];
      int size = 0;
      while ((size = bis.read(buf)) != -1)
        fos.write(buf, 0, size);
      fos.close();
      bis.close();
      conn.disconnect();
      String info = String.format("下载媒体文件成功，filePath=" + filePath, new Object[0]);
      System.out.println(info);
    } catch (Exception e) {
      filePath = null;
      String error = String.format("下载媒体文件失败：%s", new Object[] { e });
      System.out.println(error);
    }
    return filePath;
  }

  public UserCodeModel getUserIdForCode(String code)
  {
    String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + TokenThread.getToken() + "&agentid=" + SystemConfig._weixinConf.getAgentid() + "&code=" + code;

    JSONObject obj = httpRequest(url, "GET", null);
    UserCodeModel model = null;
    String userid = "";
    String deviceid = "";
    if (obj != null) {
      model = new UserCodeModel();
      if(obj.containsKey("UserId"))
    	  userid = obj.getString("UserId");
      if(obj.containsKey("DeviceId"))
    	  deviceid = obj.getString("DeviceId");
      model.setUserid(userid);
      model.setDeviceid(deviceid);
    }
    return model;
  }

  public static String getRedirectURL(String url)
  {
    StringBuffer str = new StringBuffer();
    str.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=").append(SystemConfig._weixinConf.getCorpId());
    str.append("&redirect_uri=");
    if (url.indexOf("http:") > -1)
      str.append(url);
    else {
      str.append(SystemConfig._weixinConf.getUrl()).append(url);
    }
    str.append("&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");
    return str.toString();
  }

  public int postMessage(String postUrl, String postData)
  {
    int result = 0;
    AccessToken token = TokenThread.accessToken;
    if (token == null) {
      token = getAccessToken(SystemConfig._weixinConf.getCorpId(), SystemConfig._weixinConf.getMessageSecret());
    }

    if ((token != null) && (!"".equals(token))) {
      String url = postUrl.replace("ACCESS_TOKEN", token.getToken());

      JSONObject jsonObject = httpRequest(url, "POST", postData);
      if ((jsonObject != null) && 
        (jsonObject.getInt("errcode") != 0)) {
        result = jsonObject.getInt("errcode");
        log.error(" errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
      }
    }

    return result;
  }

  public HashMap readStringXmlOut(String xml)
  {
    HashMap map = new HashMap();
    Document doc = null;
    try {
      StringBuffer content = new StringBuffer();
      StringReader sr = new StringReader(xml);
      BufferedReader br = new BufferedReader(sr);
      String line = null;
      while ((line = br.readLine()) != null) {
        content.append(line + "\n");
      }
      br.close();
      doc = DocumentHelper.parseText(content.toString());

      Element rootElt = doc.getRootElement();
      List<Element> list = rootElt.elements();
      int num = 0;
      for (Element e : list) {
        String name = e.getName();
        String txt = e.getText();
        num++;
        if ((txt != null) && (!txt.equals(""))) {
          map.put(e.getName(), e.getText());
        } else {
          List sublist = e.elements();
          if ((sublist != null) && (sublist.size() > 0)) {
            HashMap hash = getSubListItem(sublist);
            map.put(name + "_" + num, hash);
          }
        }
      }
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  private HashMap getSubListItem(List<Element> list)
  {
    HashMap map = new HashMap();
    for (Element e : list) {
      String txt = e.getText();
      String name = e.getName();
      if ((txt != null) && (!txt.equals(""))) {
        map.put(e.getName(), e.getText());
      } else {
        List sublist = e.elements();
        if ((sublist != null) && (sublist.size() > 0)) {
          HashMap hash = getSubListItem(sublist);
          map.put(name, hash);
        }
      }
    }
    return map;
  }

  public int createMenu(Menu menu, String accessToken)
  {
    int result = 0;

    String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);

    String jsonMenu = JSONObject.fromObject(menu).toString();

    JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

    if ((jsonObject != null) && 
      (jsonObject.getInt("errcode") != 0)) {
      result = jsonObject.getInt("errcode");
      log.error("创建菜单失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
    }

    return result;
  }

  public AccessToken getAccessToken(String appid, String appsecret)
  {
    AccessToken accessToken = null;

    String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APPID&corpsecret=APPSECRET".replace("APPID", appid).replace("APPSECRET", appsecret);
    JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

    if (jsonObject != null) {
      try {
        accessToken = new AccessToken();
        accessToken.setToken(jsonObject.getString("access_token"));
        accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
      }
      catch (JSONException e)
      {
        accessToken = null;

        log.error("获取token失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInt("errcode")), jsonObject.getString("errmsg"));
      }
    }
    return accessToken;
  }

  public static JSONObject request(String requestUrl, String requestMethod, String outputStr)
  {
    JSONObject jsonObject = null;
    StringBuffer buffer = new StringBuffer();
    try
    {
      TrustManager[] tm = { new MyX509TrustManager() };
      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
      sslContext.init(null, tm, new SecureRandom());

      SSLSocketFactory ssf = sslContext.getSocketFactory();

      URL url = new URL(requestUrl);
      HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
      httpUrlConn.setSSLSocketFactory(ssf);

      httpUrlConn.setDoOutput(true);
      httpUrlConn.setDoInput(true);
      httpUrlConn.setUseCaches(false);

      httpUrlConn.setRequestMethod(requestMethod);

      if ("GET".equalsIgnoreCase(requestMethod)) {
        httpUrlConn.connect();
      }

      if (outputStr != null) {
        OutputStream outputStream = httpUrlConn.getOutputStream();

        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }

      InputStream inputStream = httpUrlConn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      String str = null;
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferedReader.close();
      inputStreamReader.close();

      inputStream.close();
      inputStream = null;
      httpUrlConn.disconnect();
      jsonObject = JSONObject.fromObject(buffer.toString());
    } catch (ConnectException ce) {
      log.error("Weixin server connection timed out.");
    } catch (Exception e) {
      log.error("https request error:{}", e);
    }

    return jsonObject;
  }

  public JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr)
  {
    JSONObject jsonObject = null;
    StringBuffer buffer = new StringBuffer();
    try
    {
      TrustManager[] tm = { new MyX509TrustManager() };
      SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
      sslContext.init(null, tm, new SecureRandom());

      SSLSocketFactory ssf = sslContext.getSocketFactory();

      URL url = new URL(requestUrl);
      HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
      httpUrlConn.setSSLSocketFactory(ssf);

      httpUrlConn.setDoOutput(true);
      httpUrlConn.setDoInput(true);
      httpUrlConn.setUseCaches(false);

      httpUrlConn.setRequestMethod(requestMethod);

      if ("GET".equalsIgnoreCase(requestMethod)) {
        httpUrlConn.connect();
      }

      if (outputStr != null) {
        OutputStream outputStream = httpUrlConn.getOutputStream();

        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
      }

      InputStream inputStream = httpUrlConn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      String str = null;
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferedReader.close();
      inputStreamReader.close();

      inputStream.close();
      inputStream = null;
      httpUrlConn.disconnect();
      jsonObject = JSONObject.fromObject(buffer.toString());
    } catch (ConnectException ce) {
      log.error("Weixin server connection timed out.");
    } catch (Exception e) {
      log.error("https request error:{}", e.getMessage());
    }
    return jsonObject;
  }

  private static class Holder {
    private static final WeixinUtil SINGLETON = new WeixinUtil();
  }
}