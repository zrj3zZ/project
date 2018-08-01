package com.ibpmsoft.project.zqb.upload;
import java.util.UUID;
import java.io.IOException;

public class TokenUtil
{
  public static String generateToken(String name, String size)
    throws IOException
  {
    if ((name == null) || (size == null))
      return "";
    int code = name.hashCode();
    try {
      String token = new StringBuilder().append(code > 0 ? "A" : "B").append(Math.abs(code)).append("_").append(size.trim()).toString();

      IoUtil.storeToken(token);

      return token;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
  //生成新token
  public static String getNewToken(String name,String size){
	  if ((name == null) || (size == null))
	      return "";
	  int code=name.hashCode();
	  String uuid=UUID.randomUUID().toString().replace("-", "")+code;  
	  return uuid;
  }

}