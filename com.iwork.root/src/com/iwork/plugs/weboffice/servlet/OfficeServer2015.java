package com.iwork.plugs.weboffice.servlet;
import java.io.IOException;
import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.iwork.app.conf.SystemConfig;
import org.apache.log4j.Logger;

public class OfficeServer2015 extends HttpServlet {
	 private iMsgServer2015 MsgObj = new iMsgServer2015();
		String mOption;
		String mUserName;
		String mRecordID;
		String mFileName;
		String mFileType;
		byte[] mFileBody;	
		int mFileSize = 0;
	    String mFilePath; //取得服务器路径
	    private static Logger logger = Logger.getLogger(OfficeServer2015.class);
		protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			  mFilePath = request.getSession().getServletContext().getRealPath("");       //取得服务器路径
			   try{
				   if(request.getMethod().equalsIgnoreCase("POST")){//判断请求方式
					   MsgObj.setSendType("JSON");
					   MsgObj.Load(request); //解析请求
					   mOption = MsgObj.GetMsgByName("OPTION");//请求参数
					   mUserName = MsgObj.GetMsgByName("USERNAME");  //取得系统用户
					   if(mOption.equalsIgnoreCase("LOADFILE")){ 
						    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
					        mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
					        MsgObj.MsgTextClear();//清除文本信息
					        if (MsgObj.MsgFileLoad(mFilePath,mRecordID)){
					        }
					   }else if(mOption.equalsIgnoreCase("SAVEFILE")){
						    mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
					        mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
					        MsgObj.MsgTextClear();//清除文本信息
					        String f = SystemConfig._fileServerConf.getPath().replace("/",File.separator);
					 	      String filefullpath = mFilePath+File.separator+f+File.separator+mFileName;
					        MsgObj.MsgFileSave(filefullpath,mRecordID,mFileName);
					   }else if(mOption.equalsIgnoreCase("SAVEPDF")){
						   mRecordID = MsgObj.GetMsgByName("RECORDID");                        //取得文档编号
					       mFileName = MsgObj.GetMsgByName("FILENAME");//取得文档名称
					       MsgObj.MsgTextClear();//清除文本信息
					       String f = SystemConfig._fileServerConf.getPath().replace("/",File.separator);
					       String filefullpath = mFilePath+File.separator+f+File.separator+mFileName;
					       MsgObj.MsgFileSave(filefullpath,mRecordID,mFileName);
					   }
					 MsgObj.Send(response);   
				   }
				}catch (Exception e) {
					logger.error(e);   
			    }
		}
}
