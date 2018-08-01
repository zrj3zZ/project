package com.iwork.plugs.weboffice.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.internal.OracleResultSet;
import oracle.sql.BLOB;
import com.iwork.commons.util.FileUtil;
import org.apache.log4j.Logger;
public class IWorkPlugsWebRevisionService {
	private static Logger logger = Logger.getLogger(IWorkPlugsWebRevisionService.class);
	  private int mFileSize;
	    private byte[] mFileBody;
	    private String mFileName;
	    private String mFieldName;
	    private String mFileType;
	    private String mRecordID;
	    private String mDateTime;
	    private String mOption;
	    private String mMarkName;
	    private String mPassword;
	    private String mMarkList;
	    private String mHostName; 
	    private String mMarkGuid;
	    private String mFieldValue;
	    private String mUserName;
	    private String mFilePath;
	    private DBstep.iMsgServer2000 MsgObj;  
	    private DBstep.iDBManager2000 DbaObj;

	    //写入大字段信息
	    private void PutAtBlob(BLOB vField, int vSize) throws Exception {
	      try {
	        OutputStream outstream = vField.getBinaryOutputStream();
	        outstream.write(mFileBody, 0, vSize);
	        outstream.flush();
	        outstream.close();
	      }
	      catch (Exception e) {
	       logger.error(e,e);
	      }
	    }

	    //读取大字段信息
	    private void GetAtBlob(BLOB vField, int vSize) throws Exception {
	      try {
	        mFileBody = new byte[vSize];
	        InputStream instream = vField.getBinaryStream();
	        instream.read(mFileBody, 0, vSize);
	        instream.close();
	      }
	      catch (Exception e) {
	        logger.error(e,e);
	      }
	    }
	    

	    //保存签章数据信息
	    private boolean loadSignature() {
	      boolean mResult = false;
	 	  String filename = mUserName+".jpg";
	 	  String path = mFilePath+File.separator+"Signature"+File.separator+"USER_SIGN"+File.separator+filename;
	 	  File targetFile = new File(path);
		  if(targetFile.exists()){ 
			  mFileBody =  getBytes(path); 
			  MsgObj.MsgFileClear();
			  MsgObj.MsgFileBody(mFileBody);
			 
		  } 
	 	    	 mResult = true;
	      return (mResult);
	    }
	    /**
	    * 获得指定文件的byte数组
		 */
		public static byte[] getBytes(String filePath){
			byte[] buffer = null;
			try {
				filePath = filePath.replace("/",File.separator);
				File file = new File(filePath);
				
				FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
				byte[] b = new byte[1000];
				int n;
				while ((n = fis.read(b)) != -1) {
					bos.write(b, 0, n);
				}
				fis.close();
				bos.close();
				buffer = bos.toByteArray();
			} catch (FileNotFoundException e) {
				logger.error(e,e);
			} catch (Exception e) {
				logger.error(e,e);
			}
			return buffer;
		}
	    //保存签章数据信息
	    private boolean SaveSignature() {
	    	boolean mResult = false;
	    	mFileBody = mFieldValue.getBytes();
	    	mFileSize = mFileBody.length;
	    	if(mFileBody!=null){
	    		BufferedOutputStream stream = null;
	    		File file = null;
	    		String filename = mUserName+".png";
	    		try{ 
	    			file = new File(filename); 
	    			FileOutputStream fstream = new FileOutputStream(file);
	    			fstream.write(mFileBody);
	    		}catch(Exception e){
	    			
	    		}finally{
	    			if(stream!=null){
	    				try {
	    					stream.close();
	    				} catch (Exception e) {logger.error(e,e);
	    				}
	    			}
	    		}
	    		if(file!=null){
	    			String path = mFilePath+File.separator+"Signature"+File.separator+"USER_SIGN"+File.separator+filename;
	    			File targetFile = new File(path);
	    			File parent = targetFile.getParentFile(); 
	    			if(parent!=null&&!parent.exists()){ 
	    				parent.mkdirs(); 
	    			} 
	    			FileUtil.copyFile(file, targetFile) ;
	    			mResult = true;
	    		}
	    	}
	    	return (mResult);
	    }
	    //保存签章数据信息
	    private boolean SaveSignatureJPG() {
	    	boolean mResult = false;
	    	mFileSize = mFileBody.length;
	    	if(mFileBody!=null){
	    		BufferedOutputStream stream = null;
	    		File file = null;
	    		String filename = mRecordID+".jpg";
	    		try{ 
	    			file = new File(filename); 
	    			FileOutputStream fstream = new FileOutputStream(file);
	    			fstream.write(mFileBody);
	    		}catch(Exception e){
	    			
	    		}finally{
	    			if(stream!=null){
	    				try {
	    					stream.close();
	    				} catch (Exception e) {logger.error(e,e);
	    				}
	    			}
	    		}
	    		if(file!=null){
	    			String path = mFilePath+File.separator+"Signature"+File.separator+"USER_SIGN"+File.separator+filename;
	    			File targetFile = new File(path);
	    			File parent = targetFile.getParentFile(); 
	    			if(parent!=null&&!parent.exists()){ 
	    				parent.mkdirs();
	    			} 
	    			FileUtil.copyFile(file, targetFile) ;
	    			mResult = true;
	    		}
	    	}
	    	return (mResult);
	    }

	    //更新签章数据信息
	    private boolean UpdateSignature() {
	      boolean mResult = false;
	      if (DbaObj.OpenConnection()) {
	        PreparedStatement prestmt = null;
	        try {
	          mFileBody = mFieldValue.getBytes();
	          mFileSize = mFileBody.length;
	          String Sql = "update Document_Signature Set UserName=?,DateTime=?,HostName=?,FileBody=EMPTY_BLOB(),FileSize=? where RecordID = ? and FieldName=?";
	          DbaObj.Conn.setAutoCommit(false);
	          prestmt = DbaObj.Conn.prepareStatement(Sql);
	          prestmt.setString(1, mUserName);
	          prestmt.setString(2, mDateTime);
	          prestmt.setString(3, mHostName);
	          prestmt.setInt(4, mFileSize);
	          prestmt.setString(5, mRecordID);
	          prestmt.setString(6, mFieldName);
	          prestmt.execute();
	          prestmt.close();
	          PreparedStatement stmt = null;
	          String sql="select * from Document_Signature where RecordID = ? and FieldName=?";
	          stmt = DbaObj.Conn.prepareStatement(sql);
	          stmt.setString(1, mRecordID);
	          stmt.setString(2, mFieldName);
	          OracleResultSet update = (OracleResultSet) stmt.executeQuery();
	          if (update.next()) {
	            PutAtBlob(((oracle.jdbc.OracleResultSet) update).getBLOB("FileBody"), mFileSize);
	          }
	          update.close();
	          stmt.close();
	          DbaObj.Conn.commit();
	          mFileBody = null;
	          mResult = true;
	        }
	        catch (Exception e) {
	         logger.error(e,e);
	          mResult = false;
	        }
	        DbaObj.CloseConnection();
	      }
	      return (mResult);
	    }

	    //判断签章数据信息是否存在
	    private boolean ShowSignatureIS() {
	      boolean mResult = false;
	      return (mResult);
	    }


	    //取得客户端发来的数据包
	    private byte[] ReadPackage(HttpServletRequest request) {
	      byte mStream[] = null;
	      int totalRead = 0;
	      int readBytes = 0;
	      int totalBytes = 0;
	      try {
	        totalBytes = request.getContentLength();
	        mStream = new byte[totalBytes];
	        while (totalRead < totalBytes) {
	          request.getInputStream();
	          readBytes = request.getInputStream().read(mStream, totalRead, totalBytes - totalRead);
	          totalRead += readBytes;
	          continue;
	        }
	      }
	      catch (Exception e) {
	       logger.error(e,e);
	      }
	      return (mStream);
	    }

	    //发送处理后的数据包
	    private void SendPackage(HttpServletResponse response) {
	      try {
	        ServletOutputStream OutBinarry = response.getOutputStream();
	        OutBinarry.write(MsgObj.MsgVariant());
	        OutBinarry.flush();
	        OutBinarry.close();
	      }
	      catch (Exception e) {
	       logger.error(e,e);
	      }
	    }

	    //具体处理客户端控件请求的函数
	    public void executeRun(HttpServletRequest request, HttpServletResponse response) {
	      mOption = "";
	      mRecordID = "";
	      mFileBody = null;
	      mFileName = "";
	      mFileType = "";
	      mFileSize = 0;
	      mDateTime = "";
	      mMarkName = "";
	      mPassword = "";
	      mMarkList = "";
	      mMarkGuid = "";
	      mUserName = "";
	      mFieldName = "";
	      mHostName = "";
	      mFieldValue = ""; 
	      mFilePath = request.getSession().getServletContext().getRealPath("");     //取得服务器路径
	      DbaObj = new DBstep.iDBManager2000();
	      MsgObj = new DBstep.iMsgServer2000();
	      try {
	        if (request.getMethod().equalsIgnoreCase("POST")) {
	          MsgObj.MsgVariant(ReadPackage(request));
	          if (MsgObj.GetMsgByName("DBSTEP").equalsIgnoreCase("DBSTEP")) {       //检测客户端传递的数据包格式
	            mOption = MsgObj.GetMsgByName("OPTION");                            //取得操作类型
	           if (mOption.equalsIgnoreCase("SAVESIGNATURE")) {               //下面的代码为更新印章数据
	              mRecordID = MsgObj.GetMsgByName("RECORDID");                      //取得文档编号
	              mFieldName = MsgObj.GetMsgByName("FIELDNAME");                    //取得签章字段名称
	              mFieldValue = MsgObj.GetMsgByName("FIELDVALUE");                  //取得签章数据内容
	              mUserName = MsgObj.GetMsgByName("USERNAME");                      //取得用户名称
	              mDateTime = MsgObj.GetMsgByName("DATETIME");                      //取得签章日期时间
	              mFileBody = MsgObj.MsgFileBody();
	              mHostName = request.getRemoteAddr();                              //取得客户端IP
	              MsgObj.MsgTextClear();                                            //清除SetMsgByName设置的值
	              //System.out.println(mFilePath+"/"+mRecordID+"_"+mFieldName+".gif");
	              //MsgObj.MsgFileSave(mFilePath+"/"+mRecordID+"_"+mFieldName+".gif");    //在服务器保存输出成图片
	              if (ShowSignatureIS()) {                                          //判断是否已经存在签章记录
	                if (UpdateSignature()) {                                        //更新签章数据
	                  MsgObj.SetMsgByName("STATUS", "更新成功!");                   //设置状态信息
	                  MsgObj.MsgError("");                                          //清除错误信息
	                }
	                else {
	                  MsgObj.MsgError("保存签章信息失败!");                         //设置错误信息
	                }
	              } 
	              else {
	                if (SaveSignature()) {                                          //保存签章数据
	                  MsgObj.SetMsgByName("STATUS", "保存成功!");                   //设置状态信息
	                  MsgObj.MsgError("");                                          //清除错误信息
	                }
	                else {
	                  MsgObj.MsgError("保存签章信息失败!");                         //设置错误信息
	                }
	              }
	            }
	            else if (mOption.equalsIgnoreCase("LOADSIGNATURE")) {               //下面的代码为调入签章数据
	            	 mRecordID = MsgObj.GetMsgByName("RECORDID");                      //取得文档编号
		              mFieldName = MsgObj.GetMsgByName("FIELDNAME");                    //取得签章字段名称
		              mFieldValue = MsgObj.GetMsgByName("FIELDVALUE");                  //取得签章数据内容
		              mUserName = MsgObj.GetMsgByName("USERNAME");                      //取得用户名称
		              mDateTime = MsgObj.GetMsgByName("DATETIME");                      //取得签章日期时间
		              mHostName = request.getRemoteAddr();                              //取得客户端IP
	            	loadSignature();
	            }else if (mOption.equalsIgnoreCase("SAVEASIMG")) {
	            	 mRecordID = MsgObj.GetMsgByName("RECORDID");                      //取得文档编号
		              mFieldName = MsgObj.GetMsgByName("FIELDNAME");                    //取得签章字段名称
		              mFieldValue = MsgObj.GetMsgByName("FIELDVALUE");                  //取得签章数据内容
		              mUserName = MsgObj.GetMsgByName("USERNAME");                      //取得用户名称
		              mDateTime = MsgObj.GetMsgByName("DATETIME");                      //取得签章日期时间
		              mHostName = request.getRemoteAddr();                              //取得客户端IP
		              mFileBody = MsgObj.MsgFileBody();
		              MsgObj.MsgTextClear();                                            //清除SetMsgByName设置的值
		              this.SaveSignatureJPG();
		         }
	          }
	          else {
	            MsgObj.MsgError("客户端发送数据包错误!");
	            MsgObj.MsgTextClear();
	            MsgObj.MsgFileClear();
	          }
	        }
	        else {
	          MsgObj.MsgError("请使用Post方法");
	          MsgObj.MsgTextClear();
	          MsgObj.MsgFileClear();
	        }
	        SendPackage(response);
	      }
	      catch (Exception e) {
	       logger.error(e,e);
	      }
	    }
}
