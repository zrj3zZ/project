package com.iwork.plugs.weboffice.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.UUIDUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.upload.util.FileUploadUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.FileUploadAPI;

import oracle.jdbc.internal.OracleResultSet;
import oracle.sql.BLOB;
public class IWorkPlugsWebOfficeService {
	private static Logger logger = Logger.getLogger(IWorkPlugsWebOfficeService.class);
	private int mFileSize;
	private FileUploadUtil fileUploadUtil;
	private FileUploadDAO uploadifyDAO;
	private byte[] mFileBody;
	private String mFileName;
	private String mFileType;
	private String mFileDate;
	private String mFileID;
	private int mTemplateId;
	private String mRecordID;
	private String mTemplate;
	private String mDateTime;
	private String mOption;
	private String mMarkName;
	private String mPassword;
	private String mMarkList;
	private String mBookmark;
	private String mDescript;
	private String mHostName;
	private String mMarkGuid;
	private String mCommand;
	private String mContent;
	private String mHtmlName;
	private String mDirectory;
	private String mFilePath;
	private String mUserName;
	private int mColumns;
	private int mCells;
	private String mMyDefine1;
	private String mLocalFile;
	private String mRemoteFile;
	private String mLabelName;
	private String mImageName;
	private String mTableContent;
	private String Sql;
	// 打印控制
	private String mOfficePrints;
	private int mCopies;
	// 自定义信息传递
	private String mInfo;
	private DBstep.iMsgServer2000 MsgObj;
	private DBstep.iDBManager2000 DbaObj;

	// ==============================↓Bolb字段处理代码【开始】↓==============================
	// 向数据库写文档数据内容
	private void PutAtBlob(BLOB vField, int vSize) throws Exception {
		try {
			OutputStream outstream = vField.getBinaryOutputStream();
			outstream.write(mFileBody, 0, vSize);
			outstream.close();
		} catch (Exception e) {logger.error(e,e);
		}
	}

	// 从数据库取文档数据内容
	private void GetAtBlob(BLOB vField, int vSize) throws Exception {
		try {
			mFileBody = new byte[vSize];
			InputStream instream = vField.getBinaryStream();
			instream.read(mFileBody, 0, vSize);
			instream.close();
		} catch (Exception e) {logger.error(e,e);
		}
	}

	// 格式化日期时间
	public String FormatDate(String DateValue, String DateType) {
		String Result;
		SimpleDateFormat formatter = new SimpleDateFormat(DateType);
		try {
			Date mDateTime = formatter.parse(DateValue);
			Result = formatter.format(mDateTime);
		} catch (Exception e) {
			Result = e.getMessage();
		}
		if (Result.equalsIgnoreCase("1900-01-01")) {
			Result = "";
		}
		return Result;
	}

	// ==============================↑Bolb字段处理代码【结束】↑==============================

	// ==============================↓文档、模板管理代码【开始】↓==============================
	// 调出文档，将文档内容保存在mFileBody里，以便进行打包
	private boolean LoadFile() {
		boolean mResult = true;
		FileUpload fileUpload = FileUploadAPI.getInstance().getFileUpload(
				mRecordID);
		if (fileUpload != null) {
			String fileurl = fileUpload.getFileUrl();
			File file = new File(fileurl);
			if (file != null && file.exists()) {
				mFileBody = this.getBytes(fileurl);
			} else {
				String filepath = mFilePath + File.separator + fileurl;
				mFileBody = this.getBytes(filepath);
			}
		} else {
			mResult = false;
		}
		return (mResult);
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			filePath = filePath.replace("/", File.separator);
			File file = new File(filePath);
			String parent = file.getParent();
			File parentFile = new File(parent);
			if (!parentFile.exists()) {
				parentFile.mkdir();
			}
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
		} catch (Exception e) {		
			logger.error(e,e);
		}
		return buffer;
	}

	// 保存文档，如果文档存在，则覆盖，不存在，则添加
	private boolean SaveFile(byte[] docFileBody) {
		boolean mResult = false;
		if (docFileBody != null) {
			BufferedOutputStream stream = null;
			FileOutputStream fstream = null;
			File file = null;
			String f = SystemConfig._fileServerConf.getFormFilePath().replace(
					"/", File.separator);
			Date data = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String format = sdf.format(data);
			String filefullpath = mFilePath + File.separator + f
					+ File.separator + format + File.separator + mRecordID
					+ mFileType;
			filefullpath = filefullpath.replace("\\", "/");
			try {
				String filename = mFileName + mFileType;
				file = new File(filefullpath);
				if (!file.getParentFile().exists()) {
					if (!file.getParentFile().mkdirs()) {
						return false;
					}
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				fstream = new FileOutputStream(file);
				stream = new BufferedOutputStream(fstream);
				stream.write(docFileBody);
				stream.flush();
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (Exception e) {logger.error(e,e);
					}
				}
				if (fstream != null) {
					try {
						fstream.close();
					} catch (Exception e) {logger.error(e,e);
					}
				}
			}
			StringBuffer sql = new StringBuffer();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			// mUserName
			sql.append(
					"insert into IWORK_WEBOFFCIE_MEMO (FILE_ID,DOTIME,USERNAME) values (")
					.append("'")
					.append(mRecordID)
					.append("',to_date('" + sd.format(new Date())
							+ "','YYYY-MM-DD HH24:MI'),'").append(mUserName)
					.append("')");
			DBUtil.executeUpdate(sql.toString());
			if (file != null) {
				file = new File(filefullpath);
				if (file != null) {
					String fileDirfullpath = SystemConfig._fileServerConf
							.getFormFilePath().replace("/", File.separator);
					saveForPath(file, fileDirfullpath, mFileName + mFileType,
							mRecordID);
				}
				mResult = true;
			}
		}

		return mResult;
	}

	public FileUpload getFileUpload(Serializable id) {
		if (uploadifyDAO == null) {
			uploadifyDAO = (FileUploadDAO) SpringBeanUtil
					.getBean("uploadifyDAO");
		}
		return uploadifyDAO.getFileUpload(FileUpload.class, id);
	}

	public FileUpload saveForPath(File uploadify, String filePath,
			String mFileName, String uuid) {
		FileUpload fileModel = uploadForPath(uploadify, filePath, mFileName,
				uuid);
		FileUpload model = getFileUpload(uuid);
		if (model != null) {
			fileModel.setFileId(model.getFileId());
			uploadifyDAO.update(fileModel);
		} else {
			FileUpload fu = uploadifyDAO.save(fileModel);
			if (fu == null) {
				fileUploadUtil.deleteFile(fileModel);
			}
		}
		return fileModel;
	}

	public FileUpload uploadForPath(File uploadify, String fileDirFullPath,
			String mFileName, String uuid) {
		FileUpload fileModel = null;
		String extName = "";
		String newFileName = "";
		if (uuid == null) {
			uuid = UUID.randomUUID().toString().replaceAll("-", "");
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date data = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String format = sdf.format(data);
		File dirFile = new File(fileDirFullPath);
		if (!dirFile.isDirectory()) {
			dirFile.mkdirs();
		}
		if (uploadify != null) {
			extName = FileUtil.getFileExt(uploadify.getName());
		}
		newFileName = uuid + "." + extName;
		String fileFullPath = fileDirFullPath + File.separator + format
				+ File.separator + newFileName;
		File targetFile = new File(mFilePath + File.separator + fileFullPath);
		if (targetFile != null) {
			boolean flag = !targetFile.exists()?copy(uploadify, targetFile):true;
			if (flag) {
				fileModel = new FileUpload();
				fileModel.setFileId(uuid);
				fileModel.setFileUrl(fileFullPath);
				fileModel.setFileSaveName(newFileName);
				fileModel.setFileSrcName(mFileName);
				fileModel.setUploadTime(sf.format(new Date()));
			}
		}
		return fileModel;
	}

	private boolean copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), 16384);
			out = new BufferedOutputStream(new FileOutputStream(dst), 16384);
			byte[] buffer = new byte[16384];
			int offset = 0;
			while ((offset = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, offset);
			}
			out.flush();
		} catch (Exception e) {
			logger.error(e,e);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		return true;
	}

	// 调出模板文档，将模板文档内容保存在mFileBody里，以便进行打包
	private boolean LoadTemplate() {
		boolean mResult = true;
		FileUpload fileUpload = FileUploadAPI.getInstance().getFileUpload(
				mTemplate);
		if (fileUpload != null) {
			String fileurl = fileUpload.getFileUrl();
			File file = new File(fileurl);
			if (file != null && file.exists()) {
				mFileBody = this.getBytes(fileurl);
			} else {
				String filepath = mFilePath + File.separator + fileurl;
				mFileBody = this.getBytes(filepath);
			}

		} else {
			mResult = false;
		}
		return (mResult);
	}

	// 保存模板文档，如果模板文档存在，则覆盖，不存在，则添加
	private boolean SaveTemplate() {
		boolean mResult = false;
		List params = new ArrayList();
		String Sql = "SELECT * FROM Template_File WHERE RecordID=?";
		params.add(mTemplate);
		try {
			if (DbaObj.OpenConnection()) {
				try {
					ResultSet result = DbaObj.ExecuteQuery(Sql);
					if (result.next()) {
						Sql = "update Template_File set TemplateID=?,RecordID=?,FileName=?,FileType=?,FileSize=?,FileDate=?,FileBody=EMPTY_BLOB(),FilePath=?,UserName=?,Descript=? WHERE RecordID=?";
						params.clear();
						params.add(mTemplateId);
						params.add(mTemplate);
						params.add(mFileName);
						params.add(mFileType);
						params.add(mFileSize);
						params.add(DbaObj.GetDate());
						params.add(mFilePath);
						params.add(mUserName);
						params.add(mDescript);
						params.add(mTemplate);
						mTemplateId = result.getInt("TemplateId");
					} else {
						Sql = "insert into Template_File (TemplateID,RecordID,FileName,FileType,FileSize,FileDate,FileBody,FilePath,UserName,Descript) values (?,?,?,?,?,?,EMPTY_BLOB(),?,?,? )";
						params.clear();
						params.add(mTemplateId);
						params.add(mTemplate);
						params.add(mFileName);
						params.add(mFileType);
						params.add(mFileSize);
						params.add(DbaObj.GetDate());
						params.add(mFilePath);
						params.add(mUserName);
						params.add(mDescript);
						mTemplateId = DbaObj.GetMaxID("Template_File","TemplateId");
					}
					result.close();
				} catch (Exception e) {
					logger.error(e,e);
					mResult = false;
				}
				java.sql.PreparedStatement prestmt = null;
				try {
					prestmt = DbaObj.Conn.prepareStatement(Sql);
					for (int i = 0; i < params.size(); i++) {
						prestmt.setObject(i+1, params.get(i));
					}
					/*prestmt.setInt(1, mTemplateId);
					prestmt.setString(2, mTemplate);
					prestmt.setString(3, mFileName);
					prestmt.setString(4, mFileType);
					prestmt.setInt(5, mFileSize);
					prestmt.setDate(6, DbaObj.GetDate());
					prestmt.setString(7, mFilePath);
					prestmt.setString(8, mUserName);
					prestmt.setString(9, mDescript);*/ // "通用版本"
					DbaObj.Conn.setAutoCommit(true);
					prestmt.execute();
					DbaObj.Conn.commit();
					prestmt.close();
					PreparedStatement stmt = null;
					DbaObj.Conn.setAutoCommit(false);
					stmt = DbaObj.Conn
							.prepareStatement("select FileBody from Template_File where TEMPLATEID=? for update");
					stmt.setString(1, String.valueOf(mTemplateId));
					OracleResultSet update = (OracleResultSet) stmt
							.executeQuery();
					if (update.next()) {
						try {
							mFileSize = mFileBody.length;
							PutAtBlob(
									((oracle.jdbc.OracleResultSet) update)
											.getBLOB("FileBody"),
									mFileSize);
						} catch (Exception e) {
							logger.error(e,e);
							mResult = false;
						}
					}
					update.close();
					stmt.close();
					DbaObj.Conn.commit();
					mFileBody = null;
					mResult = true;
				} catch (Exception e) {
					logger.error(e,e);
					mResult = false;
				}
			}
		} finally {
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// ==============================↑文档、模板管理代码【结束】↑==============================

	// ==============================↓版本管理代码【开始】↓==============================
	// 列出所有版本信息
	private boolean ListVersion() {
		boolean mResult = false;
		String mDesc = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *  FROM IWORK_WEBOFFCIE_VERSION WHERE RecordID=?");
		mFileID = "\r\n";
		mDateTime = "保存时间\r\n";
		mUserName = "用户名\r\n";
		mDescript = "版本说明\r\n";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			conn = DBUtil.open();
			try {
				stmt = conn.prepareStatement(sql.toString());
				stmt.setString(1, mRecordID);
				result = stmt.executeQuery();
				while (result.next()) {
					try {
						mFileID += result.getString("File_ID") + "\r\n"; // 文件号列表
						mDateTime += result.getString("DOTIME") + "\r\n"; // 日期列表
						mUserName += result.getString("USERNAME") + "\r\n"; // 日期列表
						mDesc = result.getString("Descript"); // 如果说明信息里有回车，则将回车变成>符号
						mDesc = mDesc.replace('\r', '>');
						mDesc = mDesc.replace('\n', '>');
						mDescript += mDesc + "\r\n";
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
				mResult = true;
			} catch (Exception e) {
				logger.error(e,e);
				mResult = false;
			}
		} finally {
			try {
				if (result != null) {
					result.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
		return (mResult);
	}

	// 调入选中版本，通过文件号调用mFileID,并把文件放入mFileBody里，以便进行打包
	private boolean LoadVersion(String mFileID) {
		boolean mResult = false;
		String sql = "SELECT * FROM IWORK_WEBOFFCIE_VERSION WHERE   File_ID='"
				+ mFileID + "'";
		String fileid = DBUtil.getString(sql, "FILE_ID");
		FileUpload fileUpload = FileUploadAPI.getInstance().getFileUpload(
				fileid);
		if (fileUpload != null) {
			String fileurl = fileUpload.getFileUrl();
			File file = new File(fileurl);
			if (file != null && file.exists()) {
				mFileBody = this.getBytes(fileurl);
			} else {
				String filepath = mFilePath + File.separator + fileurl;
				mFileBody = this.getBytes(filepath);
			}
			mResult = true;
		} else {
			mResult = false;
		}
		return (mResult);
	}

	// 保存版本，将该版本文件存盘，并将说明信息也保存起来
	private boolean SaveVersion() {
		boolean mResult = false;
		if (mFileBody != null) {
			byte[] docFileBody  =  MsgObj.ToDocument(mFileBody);
			BufferedOutputStream stream = null;
			FileOutputStream fstream = null;
			File file = null;
			String f = SystemConfig._fileServerConf.getFormFilePath().replace("/", File.separator);
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
			Date data = new Date();
			String format = sd.format(data);
			String FileID = UUIDUtil.getUUID();
			String filefullpath = mFilePath + File.separator + f
					+ File.separator + format + File.separator + FileID
					+ mFileType;
			try {
				String filename = mFileName + mFileType;
				file = new File(filefullpath);
				if (!file.getParentFile().exists()) {
					if (!file.getParentFile().mkdirs()) {
						return false;
					}
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				fstream = new FileOutputStream(file);
				stream = new BufferedOutputStream(fstream);
				stream.write(docFileBody);
				stream.flush();
			} catch (Exception e) {
				logger.error(e,e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (Exception e) {logger.error(e,e);
					}
				}
				if (fstream != null) {
					try {
						fstream.close();
					} catch (Exception e) {logger.error(e,e);
					}
				}
			}
			if (file != null) {
				file = new File(filefullpath);
				if (file != null) {
					String fileDirfullpath = SystemConfig._fileServerConf
							.getFormFilePath().replace("/", File.separator)
							+ File.separator + format;
					FileUploadAPI.getInstance().saveForPath(file,
							fileDirfullpath, FileID);
				}
				// 插入版本列表
				StringBuffer sql = new StringBuffer();
				sql.append(
						"insert into IWORK_WEBOFFCIE_VERSION (FILE_ID,RecordID,DOTIME,DESCRIPT,USERNAME) values (")
						.append("'").append(FileID).append("','")
						.append(mRecordID).append("',sysdate,'")
						.append(mDescript).append("','").append(mUserName)
						.append("')");
				DBUtil.executeUpdate(sql.toString());
				mResult = true;
			}
		}
		return (mResult);
	}

	// ==============================↑版本管理代码【结束】↑==============================

	// ==============================↓标签管理代码【开始】↓==============================
	// 取得书签列表
	private boolean ListBookmarks() {
		boolean mResult = false;
		String Sql = "SELECT * FROM Bookmarks ";
		mBookmark = "";
		mDescript = "";
		try {
			if (DbaObj.OpenConnection()) {
				try {
					ResultSet result = DbaObj.ExecuteQuery(Sql);
					while (result.next()) {
						try {
							mBookmark += result.getString("BookMarkName")
									+ "\r\n"; // 用户名列表
							mDescript += result.getString("BookMarkDesc")
									+ "\r\n"; // 如果说明信息里有回车，则将回车变成>符号
						} catch (Exception e) {
							logger.error(e,e);
						}
					}
					result.close();
					mResult = true;
				} catch (Exception e) {
					logger.error(e,e);
					mResult = false;
				}
			}
		} finally {
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// 装入书签
	private boolean LoadBookMarks() {
		boolean mResult = false;
		String Sql = " select b.BookMarkName,b.BookMarkText from Template_BookMarks a,BookMarks b where a.BookMarkname=b.BookMarkName and a.RecordID='"
				+ mTemplate + "'";
		try {
			if (DbaObj.OpenConnection()) {
				try {
					ResultSet result = DbaObj.ExecuteQuery(Sql);
					while (result.next()) {
						try {
							// 说明：我们测试程序把SQL语句直接写到替换标签内容
							// 实际使用中，这个标签内容是通过Sql语句得到的。
							// 生成SQL查询语句 result.getString("BookMarkText") & "条件"
							// 当前纪录号位 mRecordID
							// BookMarkValue=生成SQL运行结果
							String mBookMarkName = result
									.getString("BookMarkName");
							String mBookMarkValue = result
									.getString("BookMarkText");
							MsgObj.SetMsgByName(mBookMarkName, mBookMarkValue);
						} catch (Exception e) {
							logger.error(e,e);
						}
					}
					result.close();
					mResult = true;
				} catch (Exception e) {
					logger.error(e,e);
					mResult = false;
				}
			}
		} finally {
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// 保存书签
	private boolean SaveBookMarks() {
		boolean mResult = false;
		String mBookMarkName;
		int mBookMarkId;
		int mIndex;
		try {
			if (DbaObj.OpenConnection()) {
				try {
					java.sql.PreparedStatement prestmt = null;
					String Sql = "DELETE FROM Template_BookMarks Where RecordID=?";
					prestmt = DbaObj.Conn.prepareStatement(Sql);
					prestmt.setString(1, mTemplate);
					DbaObj.Conn.setAutoCommit(true);
					prestmt.execute();
					DbaObj.Conn.commit();
					prestmt.close();
					for (mIndex = 7; mIndex <= MsgObj.GetFieldCount() - 1; mIndex++) {
						java.sql.PreparedStatement prestmtx = null;
						mBookMarkName = MsgObj.GetFieldName(mIndex);
						mBookMarkId = DbaObj.GetMaxID("Template_BookMarks",
								"BookMarkId");
						Sql = "insert into Template_BookMarks (BookMarkId,RecordId,BookMarkName) values (?,?,?)";
						prestmtx = DbaObj.Conn.prepareStatement(Sql);
						prestmtx.setString(1, String.valueOf(mBookMarkId));
						prestmtx.setString(2, mTemplate);
						prestmtx.setString(3, mBookMarkName);
						DbaObj.Conn.setAutoCommit(true);
						prestmtx.execute();
						DbaObj.Conn.commit();
						prestmtx.close();
					}
					mResult = true;
				} catch (Exception e) {
					logger.error(e,e);
					mResult = false;
				}
			}
		} finally {
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// ==============================↑标签管理代码【结束】↑==============================

	// ==============================↓签章管理代码【开始】↓==============================
	// 取得签名列表
	private boolean LoadMarkList() {
		String Sql = "SELECT MarkName FROM Signature";
		mMarkList = "";
		boolean mResult = false;
		try {
			if (DbaObj.OpenConnection()) {
				try {
					ResultSet result = DbaObj.ExecuteQuery(Sql);
					while (result.next()) {
						try {
							mMarkList += result.getString("MarkName") + "\r\n";
						} catch (Exception e) {
							logger.error(e,e);
						}
					}
					result.close();
					mResult = true;
				} catch (Exception e) {
					logger.error(e,e);
					mResult = false;
				}
			}
		} finally {
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// 调入签名纪录
	private boolean LoadMarkImage(String vMarkName, String vPassWord) {
		String Sql = "SELECT MarkBody,MarkType,MarkSize FROM Signature WHERE MarkName='"
				+ vMarkName + "' and PassWord='" + vPassWord + "'";
		boolean mResult = false;
		try {
			if (DbaObj.OpenConnection()) {
				try {
					ResultSet result = DbaObj.ExecuteQuery(Sql);
					if (result.next()) {
						try {
							mFileSize = result.getInt("MarkSize");
							GetAtBlob(
									((OracleResultSet) result)
											.getBLOB("MarkBody"),
									mFileSize);
							mFileType = result.getString("MarkType");
							mResult = true;
						} catch (Exception e) {
							logger.error(e,e);
						}
					}
					result.close();
				} catch (Exception e) {
					logger.error(e,e);
					mResult = false;
				}
			}
		} finally {
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// 保存签名
	private boolean SaveSignature() {
		boolean mResult = false;
		int iSignatureID;
		String Sql = "insert into Document_Signature (SignatureID,RecordID,MarkName,UserName,DateTime,HostName,MarkGuid) values (?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,? ) ";
		iSignatureID = DbaObj.GetMaxID("Document_Signature", "SignatureID");
		if (DbaObj.OpenConnection()) {
			java.sql.PreparedStatement prestmt = null;
			try {
				prestmt = DbaObj.Conn.prepareStatement(Sql);
				prestmt.setInt(1, iSignatureID);
				prestmt.setString(2, mRecordID);
				prestmt.setString(3, mMarkName);
				prestmt.setString(4, mUserName);
				prestmt.setString(5, mDateTime);
				prestmt.setString(6, mHostName);
				prestmt.setString(7, mMarkGuid);
				DbaObj.Conn.setAutoCommit(true);
				prestmt.execute();
				DbaObj.Conn.commit();
				prestmt.close();
				mResult = true;
			} catch (Exception e) {
				logger.error(e,e);
				mResult = false;
			}
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// 列出所有签名
	private boolean LoadSignature() {
		boolean mResult = false;
		String Sql = "SELECT MarkName,UserName,DateTime,HostName,MarkGuid FROM Document_Signature WHERE RecordID='"
				+ mRecordID + "'";
		mMarkName = "印章名称\r\n";
		mUserName = "签名人\r\n";
		mDateTime = "签章时间\r\n";
		mHostName = "客户端IP\r\n";
		mMarkGuid = "序列号\r\n";
		if (DbaObj.OpenConnection()) {
			try {
				mResult = true;
				ResultSet result = DbaObj.ExecuteQuery(Sql);
				while (result.next()) {
					mMarkName += result.getString("MarkName") + "\r\n"; // 文件号列表
					mUserName += result.getString("UserName") + "\r\n"; // 日期列表
					mDateTime += FormatDate(result.getString("DateTime"),
							"yyyy-MM-dd HH:mm:ss") + "\r\n";
					mHostName += result.getString("HostName") + "\r\n";
					mMarkGuid += result.getString("MarkGuid") + "\r\n";
				}
				result.close();
			} catch (Exception e) {
				logger.error(e,e);
				mResult = false;
			}
			DbaObj.CloseConnection();
		}
		return (mResult);
	}

	// ==============================↑签章管理代码【结束】↑==============================

	// ==============================↓扩展功能代码【开始】↓==============================
	// 调出所对应的文本
	private boolean LoadContent() {
		boolean mResult = false;
		// 打开数据库
		// 根据 mRecordID 或 mFileName 等信息
		// 提取文本信息付给mContent即可。
		// 本演示假设取得的文本信息如下：
		mContent = "";
		mContent += "本文的纪录号：" + mRecordID + "\n";
		mContent += "本文的文件名：" + mFileName + "\n";
		mContent += "    这个部分请自己加入，和你们的数据库结合起来就可以了\n";
		mResult = true;
		return (mResult);
	}

	// 保存所对应的文本
	private boolean SaveContent() {
		boolean mResult = false;
		// 打开数据库
		// 根据 mRecordID 或 mFileName 等信息
		// 插入文本信息 mContent里的文本到数据库中即可。
		mResult = true;
		return (mResult);
	}

	// 增加行并填充表格内容
	private boolean GetWordTable() {
		int i, n;
		String strI, strN;
		boolean mResult;
		mColumns = 3;
		mCells = 8;
		MsgObj.MsgTextClear();
		MsgObj.SetMsgByName("COLUMNS", String.valueOf(mColumns)); // 设置表格行
		MsgObj.SetMsgByName("CELLS", String.valueOf(mCells)); // 设置表格列
		// 该部分内容可以从数据库中读取
		try {
			for (i = 1; i <= mColumns; i++) {
				strI = String.valueOf(i);
				for (n = 1; n <= mCells; n++) {
					MsgObj.SetMsgByName(String.valueOf(i) + String.valueOf(n),
							"内容" + DbaObj.GetDateTime());
				}
			}
			mResult = true;
		} catch (Exception e) {
			logger.error(e,e);
			mResult = false;
		}
		return (mResult);
	}

	// 更新打印份数
	private boolean UpdataCopies(int mLeftCopies) {
		boolean mResult = true;
		// 该函数可以把打印减少的次数记录到数据库
		// 根据自己的系统进行扩展该功能
		return mResult;
	}

	// ==============================↑扩展功能代码【结束】↑==============================

	// ==============================↓接收流、写回流代码【开始】↓==============================
	// 取得客户端发来的数据包
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
				readBytes = request.getInputStream().read(mStream, totalRead,
						totalBytes - totalRead);
				totalRead += readBytes;
				continue;
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return (mStream);
	}

	// 发送处理后的数据包
	private void SendPackage(HttpServletResponse response) {
		try {
			ServletOutputStream OutBinarry = response.getOutputStream();
			OutBinarry.write(MsgObj.MsgVariant());
			OutBinarry.flush();
			OutBinarry.close();
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	// ==============================↑接收流、写回流代码【结束】↑==============================

	public void executeRun(HttpServletRequest request,
			HttpServletResponse response) {
		MsgObj = new DBstep.iMsgServer2000(); // 创建信息包对象
		mOption = "";
		mRecordID = "";
		mTemplate = "";
		mFileBody = null;
		mFileName = "";
		mFileType = "";
		mFileSize = 0;
		mFileID = "";
		mDateTime = "";
		mMarkName = "";
		mPassword = "";
		mMarkList = "";
		mBookmark = "";
		mMarkGuid = "";
		mDescript = "";
		mCommand = "";
		mContent = "";
		mLabelName = "";
		mImageName = "";
		mTableContent = "";
		mMyDefine1 = "";
		mOfficePrints = "0";
		mFilePath = request.getSession().getServletContext().getRealPath(""); // 取得服务器路径
		try {
			if (request.getMethod().equalsIgnoreCase("POST")) {
				// MsgObj.MsgVariant(ReadPackage(request));
				// //老版本后台类，不支持UTF-8编码自适应功能
				MsgObj.Load(request); // 8.1.0.2版后台类新增解析接口，可支持UTF-8编码自适应功能
			
				if (MsgObj.GetMsgByName("DBSTEP").equalsIgnoreCase("DBSTEP")) { // 判断是否是合法的信息包，或者数据包信息是否完整
					mOption = MsgObj.GetMsgByName("OPTION"); // 取得操作信息
					mUserName = MsgObj.GetMsgByName("USERNAME"); // 取得系统用户
					if (mOption.equalsIgnoreCase("LOADFILE")) { // 下面的代码为打开服务器数据库里的文件
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						MsgObj.MsgTextClear(); // 清除文本信息
						// if (MsgObj.MsgFileLoad(mFilePath+"\\"+mFileName))
						// //从文件夹调入文档
						if (LoadFile()) { // 从数据库调入文档
							if (mFileBody != null) {
								MsgObj.MsgFileBody(mFileBody); // 将文件信息打包
								MsgObj.SetMsgByName("STATUS", "打开成功!"); // 设置状态信息
								MsgObj.MsgError(""); // 清除错误信息
							}
						} else {
							MsgObj.MsgError("打开失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("SAVEFILE")) { // 下面的代码为保存文件在服务器的数据库里
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						mDescript = "通用版本"; // 版本说明
						mFileSize = MsgObj.MsgFileSize(); // 取得文档大小
						mFileBody = MsgObj.MsgFileBody(); // 取得文档内容
						byte[] docFileBody = MsgObj.ToDocument(mFileBody);
						MsgObj.MsgTextClear(); // 清除文本信息
						if (SaveFile(docFileBody)) { // 保存文档内容到数据库中
							MsgObj.SetMsgByName("STATUS", "保存成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear(); // 清除文档内容
					}

					else if (mOption.equalsIgnoreCase("LOADTEMPLATE")) { // 下面的代码为打开服务器数据库里的模板文件
						mTemplate = MsgObj.GetMsgByName("TEMPLATE"); // 取得模板文档类型
						// 本段处理是否调用文档时打开模版，还是套用模版时打开模版。
						mCommand = MsgObj.GetMsgByName("COMMAND"); // 取得客户端定义的变量COMMAND值
						if (mCommand.equalsIgnoreCase("INSERTFILE")) {
							if (MsgObj.MsgFileLoad(mFilePath + "\\Document\\"
									+ mTemplate)) { // 从服务器文件夹中调入模板文档
								MsgObj.SetMsgByName("STATUS", "打开模板成功!"); // 设置状态信息
								MsgObj.MsgError(""); // 清除错误信息
							} else {
								MsgObj.MsgError("打开模板失败!"); // 设置错误信息
							}
						} else {
							MsgObj.MsgTextClear(); // 清除文本信息
							if (LoadTemplate()) { // 调入模板文档
								MsgObj.MsgFileBody(mFileBody); // 将文件信息打包
								MsgObj.SetMsgByName("STATUS", "打开模板成功!"); // 设置状态信息
								MsgObj.MsgError(""); // 清除错误信息
							} else {
								MsgObj.MsgError("打开模板失败!"); // 设置错误信息
							}
						}
					}

					else if (mOption.equalsIgnoreCase("SAVETEMPLATE")) { // 下面的代码为保存模板文件在服务器的数据库里
						mTemplate = MsgObj.GetMsgByName("TEMPLATE"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						mUserName = mUserName; // 取得保存用户名称
						mDescript = "通用模板"; // 版本说明
						mFileSize = MsgObj.MsgFileSize(); // 取得文档大小
						mFileDate = DbaObj.GetDateTime(); // 取得文档时间
						mFileBody = MsgObj.MsgFileBody(); // 取得文档内容
						MsgObj.MsgTextClear();
						if (SaveTemplate()) { // 保存模板文档内容
							MsgObj.SetMsgByName("STATUS", "保存模板成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存模板失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear();
					}

					else if (mOption.equalsIgnoreCase("LISTVERSION")) { // 下面的代码为打开版本列表
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						MsgObj.MsgTextClear();
						if (ListVersion()) { // 生成版本列表
							MsgObj.SetMsgByName("FILEID", mFileID); // 将文档号列表打包
							MsgObj.SetMsgByName("DATETIME", mDateTime); // 将日期时间列表打包
							MsgObj.SetMsgByName("USERNAME", mUserName); // 将用户名列表打包
							MsgObj.SetMsgByName("DESCRIPT", mDescript); // 将说明信息列表打包
							MsgObj.SetMsgByName("STATUS", "版本列表成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("版本列表失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("LOADVERSION")) { // 下面的代码为打开版本文档
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileID = MsgObj.GetMsgByName("FILEID"); // 取得版本文档号
						MsgObj.MsgTextClear();
						if (LoadVersion(mFileID)) { // 调入该版本文档
							MsgObj.MsgFileBody(mFileBody); // 将文档信息打包
							MsgObj.SetMsgByName("STATUS", "打开版本成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("打开版本失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("SAVEVERSION")) { // 下面的代码为保存版本文档
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileID = MsgObj.GetMsgByName("FILEID"); // 取得版本文档号
																	// 如:WebSaveVersionByFileID，则FileID值存在
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						mUserName = mUserName; // 取得保存用户名称
						mDescript = MsgObj.GetMsgByName("DESCRIPT"); // 取得说明信息
						mFileSize = MsgObj.MsgFileSize(); // 取得文档大小
						mFileBody = MsgObj.MsgFileBody(); // 取得文档内容
						MsgObj.MsgTextClear();
						if (SaveVersion()) { // 保存版本文档
							MsgObj.SetMsgByName("STATUS", "保存版本成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存版本失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear(); // 清除文档内容
					}

					else if (mOption.equalsIgnoreCase("LOADBOOKMARKS")) { // 下面的代码为取得文档标签
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mTemplate = MsgObj.GetMsgByName("TEMPLATE"); // 取得模板编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						MsgObj.MsgTextClear();
						if (LoadBookMarks()) {
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("装入标签信息失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("SAVEBOOKMARKS")) { // 下面的代码为取得标签文档内容
						mTemplate = MsgObj.GetMsgByName("TEMPLATE"); // 取得模板编号
						if (SaveBookMarks()) {
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存标签信息失败!"); // 设置错误信息
						}
						MsgObj.MsgTextClear(); // 清除文本信息
					}

					else if (mOption.equalsIgnoreCase("LISTBOOKMARKS")) { // 下面的代码为显示标签列表
						MsgObj.MsgTextClear(); // 清除文本信息
						if (ListBookmarks()) {
							MsgObj.SetMsgByName("BOOKMARK", mBookmark); // 将用户名列表打包
							MsgObj.SetMsgByName("DESCRIPT", mDescript); // 将说明信息列表打包
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("调入标签失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("LOADMARKLIST")) { // 下面的代码为创建印章列表
						MsgObj.MsgTextClear(); // 清除文本信息
						if (LoadMarkList()) {
							MsgObj.SetMsgByName("MARKLIST", mMarkList); // 显示签章列表
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("创建印章列表失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("LOADMARKIMAGE")) { // 下面的代码为打开印章文件
						mMarkName = MsgObj.GetMsgByName("IMAGENAME"); // 取得签名名称
						mUserName = MsgObj.GetMsgByName("USERNAME"); // 取得用户名称
						mPassword = MsgObj.GetMsgByName("PASSWORD"); // 取得用户密码
						MsgObj.MsgTextClear(); // 清除文本信息
						if (LoadMarkImage(mMarkName, mPassword)) { // 调入签名信息
							MsgObj.SetMsgByName("IMAGETYPE", mFileType); // 设置签名类型
							MsgObj.MsgFileBody(mFileBody); // 将签名信息打包
							MsgObj.SetMsgByName("POSITION", "Manager"); // 插入位置
																		// 在文档中标签"Manager"
							MsgObj.SetMsgByName("ZORDER", "5"); // 4:在文字上方
																// 5:在文字下方
							MsgObj.SetMsgByName("STATUS", "打开成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("签名或密码错误!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("SAVESIGNATURE")) { // 下面的代码为保存签章基本信息
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文件名称
						mMarkName = MsgObj.GetMsgByName("MARKNAME"); // 取得签名名称
						mUserName = MsgObj.GetMsgByName("USERNAME"); // 取得用户名称
						mDateTime = MsgObj.GetMsgByName("DATETIME"); // 取得签名时间
						mHostName = request.getRemoteAddr(); // 取得用户IP
						mMarkGuid = MsgObj.GetMsgByName("MARKGUID"); // 取得唯一编号
						MsgObj.MsgTextClear(); // 清除文本信息
						if (SaveSignature()) { // 保存签章
							MsgObj.SetMsgByName("STATUS", "保存印章成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存印章失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("LOADSIGNATURE")) { // 下面的代码为调出签章基本信息
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						MsgObj.MsgTextClear(); // 清除文本信息
						if (LoadSignature()) { // 调出签章
							MsgObj.SetMsgByName("MARKNAME", mMarkName); // 将签名名称列表打包
							MsgObj.SetMsgByName("USERNAME", mUserName); // 将用户名列表打包
							MsgObj.SetMsgByName("DATETIME", mDateTime); // 将时间列表打包
							MsgObj.SetMsgByName("HOSTNAME", mHostName); // 将盖章IP地址列表打包
							MsgObj.SetMsgByName("MARKGUID", mMarkGuid); // 将唯一编号列表打包
							MsgObj.SetMsgByName("STATUS", "调入印章成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("调入印章失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("SAVEPDF")) { // 下面的代码为保存PDF文件
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						MsgObj.MsgTextClear(); // 清除文本信息
						if (MsgObj.MsgFileSave(mFilePath + "\\Document\\"
								+ mRecordID + ".pdf")) { // 保存文档到文件夹中
							MsgObj.SetMsgByName("STATUS", "保存成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear(); // 清除文档内容
					}

					else if (mOption.equalsIgnoreCase("SAVEASHTML")) { // 下面的代码为将OFFICE存为HTML页面
						mHtmlName = MsgObj.GetMsgByName("HTMLNAME"); // 取得文件名称
						mDirectory = MsgObj.GetMsgByName("DIRECTORY"); // 取得目录名称
						MsgObj.MsgTextClear();
						if (mDirectory.trim().equalsIgnoreCase("")) {
							mFilePath = mFilePath + "\\HTML";
						} else {
							mFilePath = mFilePath + "\\HTML\\" + mDirectory;
						}
						MsgObj.MakeDirectory(mFilePath); // 创建路径
						if (MsgObj.MsgFileSave(mFilePath + "\\" + mHtmlName)) { // 保存HTML文件
							MsgObj.MsgError(""); // 清除错误信息
							MsgObj.SetMsgByName("STATUS", "保存HTML成功!"); // 设置状态信息
						} else {
							MsgObj.MsgError("保存HTML失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear();
					}

					else if (mOption.equalsIgnoreCase("SAVEIMAGE")) { // 下面的代码为将OFFICE存为HTML图片页面
						mHtmlName = MsgObj.GetMsgByName("HTMLNAME"); // 取得文件名称
						mDirectory = MsgObj.GetMsgByName("DIRECTORY"); // 取得目录名称
						MsgObj.MsgTextClear();
						if (mDirectory.trim().equalsIgnoreCase("")) {
							mFilePath = mFilePath + "\\HTMLIMAGE";
						} else {
							mFilePath = mFilePath + "\\HTMLIMAGE\\"
									+ mDirectory;
						}
						MsgObj.MakeDirectory(mFilePath); // 创建路径
						if (MsgObj.MsgFileSave(mFilePath + "\\" + mHtmlName)) { // 保存HTML文件
							MsgObj.MsgError(""); // 清除错误信息
							MsgObj.SetMsgByName("STATUS", "保存HTML图片成功!"); // 设置状态信息
						} else {
							MsgObj.MsgError("保存HTML图片失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear();
					}

					else if (mOption.equalsIgnoreCase("SAVEASPAGE")) { // 下面的代码为将手写批注存为HTML图片页面
						mHtmlName = MsgObj.GetMsgByName("HTMLNAME"); // 取得文件名称
						mDirectory = MsgObj.GetMsgByName("DIRECTORY"); // 取得目录名称
						MsgObj.MsgTextClear();
						if (mDirectory.trim().equalsIgnoreCase("")) {
							mFilePath = mFilePath + "\\HTML";
						} else {
							mFilePath = mFilePath + "\\HTML\\" + mDirectory;
						}
						MsgObj.MakeDirectory(mFilePath); // 创建路径
						if (MsgObj.MsgFileSave(mFilePath + "\\" + mHtmlName)) { // 保存HTML文件
							MsgObj.MsgError(""); // 清除错误信息
							MsgObj.SetMsgByName("STATUS", "保存批注HTML图片成功!"); // 设置状态信息
						} else {
							MsgObj.MsgError("保存批注HTML图片失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear();
					}

					else if (mOption.equalsIgnoreCase("INSERTFILE")) { // 下面的代码为插入文件
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						MsgObj.MsgTextClear();
						if (LoadFile()) { // 调入文档
							MsgObj.MsgFileBody(mFileBody); // 将文件信息打包
							MsgObj.SetMsgByName("POSITION", "Content"); // 设置插入的位置[书签]
							MsgObj.SetMsgByName("STATUS", "插入文件成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("插入文件成功!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("UPDATEFILE")) { // 下面的代码为更新保存文件
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						mUserName = mUserName; // 取得保存用户名称
						mDescript = "定稿版本"; // 版本说明
						mFileSize = MsgObj.MsgFileSize(); // 取得文档大小
						mFileDate = DbaObj.GetDateTime(); // 取得文档时间
						mFileBody = MsgObj.MsgFileBody(); // 取得文档内容
						MsgObj.MsgTextClear();
						if (SaveVersion()) { // 保存文档内容
							MsgObj.SetMsgByName("STATUS", "保存定稿版本成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存定稿版本失败!"); // 设置错误信息
						}
						MsgObj.MsgFileClear();
					}

					else if (mOption.equalsIgnoreCase("INSERTIMAGE")) { // 下面的代码为插入服务器图片
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mLabelName = MsgObj.GetMsgByName("LABELNAME"); // 标签名
						mImageName = MsgObj.GetMsgByName("IMAGENAME"); // 图片名
						mFilePath = mFilePath + "\\Document\\" + mImageName; // 图片在服务器的完整路径
						mFileType = mImageName.substring(
								mImageName.length() - 4).toLowerCase(); // 取得文件的类型
						MsgObj.MsgTextClear();
						if (MsgObj.MsgFileLoad(mFilePath)) { // 调入图片
							MsgObj.SetMsgByName("IMAGETYPE", mFileType); // 指定图片的类型
							MsgObj.SetMsgByName("POSITION", mLabelName); // 设置插入的位置[书签对象名]
							MsgObj.SetMsgByName("STATUS", "插入图片成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("插入图片失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("PUTFILE")) { // 下面的代码为请求上传文件操作
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileBody = MsgObj.MsgFileBody(); // 取得文档内容
						mLocalFile = MsgObj.GetMsgByName("LOCALFILE"); // 取得本地文件名称
						mRemoteFile = MsgObj.GetMsgByName("REMOTEFILE"); // 取得远程文件名称
						MsgObj.MsgTextClear(); // 清除文本信息
						mFilePath = mFilePath + "\\Document\\" + mRemoteFile;
						if (MsgObj.MsgFileSave(mFilePath)) { // 调入文档
							MsgObj.SetMsgByName("STATUS", "保存上传文件成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("上传文件失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("GETFILE")) { // 下面的代码为请求下载文件操作
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mLocalFile = MsgObj.GetMsgByName("LOCALFILE"); // 取得本地文件名称
						mRemoteFile = MsgObj.GetMsgByName("REMOTEFILE"); // 取得远程文件名称
						MsgObj.MsgTextClear(); // 清除文本信息
						mFilePath = mFilePath + "\\Document\\" + mRemoteFile;
						if (MsgObj.MsgFileLoad(mFilePath)) { // 调入文档内容
							MsgObj.SetMsgByName("STATUS", "保存下载文件成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("下载文件失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("DATETIME")) { // 下面的代码为请求取得服务器时间
						MsgObj.MsgTextClear(); // 清除文本信息
						MsgObj.SetMsgByName("DATETIME", DbaObj.GetDateTime()); // 标准日期格式字串，如
																				// 2005-8-16
																				// 10:20:35
					}

					else if (mOption.equalsIgnoreCase("SENDMESSAGE")) { // 下面的代码为Web页面请求信息[扩展接口]
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						mFileName = MsgObj.GetMsgByName("FILENAME"); // 取得文档名称
						mFileType = MsgObj.GetMsgByName("FILETYPE"); // 取得文档类型
						mCommand = MsgObj.GetMsgByName("COMMAND"); // 取得自定义的操作类型
						mContent = MsgObj.GetMsgByName("CONTENT"); // 取得文本信息
																	// Content
						mOfficePrints = MsgObj.GetMsgByName("OFFICEPRINTS"); // 取得Office文档的打印次数
						mInfo = MsgObj.GetMsgByName("TESTINFO"); // 取得客户端传来的自定义信息
						MsgObj.MsgTextClear();
						MsgObj.MsgFileClear();
						if (mCommand.equalsIgnoreCase("INPORTTEXT")) { // 导入文本内容功能
							if (LoadContent()) {
								MsgObj.SetMsgByName("CONTENT", mContent);
								MsgObj.SetMsgByName("STATUS", "导入成功!"); // 设置状态信息
								MsgObj.MsgError(""); // 清除错误信息
							} else {
								MsgObj.MsgError("导入失败!"); // 设置错误信息
							}
						} else if (mCommand.equalsIgnoreCase("EXPORTTEXT")) { // 导出文本内容功能
							if (SaveContent()) {
								MsgObj.SetMsgByName("STATUS", "导出成功!"); // 设置状态信息
								MsgObj.MsgError(""); // 清除错误信息
							} else {
								MsgObj.MsgError("导出失败!"); // 设置错误信
							}
						} else if (mCommand.equalsIgnoreCase("WORDTABLE")) { // 插入远程表格功能
							if (GetWordTable()) {
								MsgObj.SetMsgByName("COLUMNS",
										String.valueOf(mColumns)); // 列
								MsgObj.SetMsgByName("CELLS",
										String.valueOf(mCells)); // 行
								MsgObj.SetMsgByName("WORDCONTENT",
										mTableContent); // 表格内容
								MsgObj.SetMsgByName("STATUS", "增加和填充成功成功!"); // 设置状态信息
								MsgObj.MsgError(""); // 清除错误信息
							} else {
								MsgObj.MsgError("增加表格行失败!"); // 设置错误信息
							}
						} else if (mCommand.equalsIgnoreCase("COPIES")) { // 打印份数控制功能
							mCopies = Integer.parseInt(mOfficePrints); // 获得客户需要打印的份数
							if (mCopies <= 2) { // 比较打印份数，拟定该文档允许打印的总数为2份，注：可以在数据库中设置好文档允许打印的份数
								if (UpdataCopies(2 - mCopies)) { // 更新打印份数
									MsgObj.SetMsgByName("STATUS", "1"); // 设置状态信息，允许打印
									MsgObj.MsgError(""); // 清除错误信息
								}
							} else {
								MsgObj.SetMsgByName("STATUS", "0"); // 不允许打印
								MsgObj.MsgError("超过打印限度不允许打印!"); // 设置错误信息
							}
						} else if (mCommand.equalsIgnoreCase("SELFINFO")) {
							mInfo = "服务器端收到客户端传来的信息：“" + mInfo + "” | ";
							mInfo = mInfo + "当前服务器时间：" + DbaObj.GetDateTime(); // 组合返回给客户端的信息
							MsgObj.SetMsgByName("RETURNINFO", mInfo); // 将返回的信息设置到信息包中
						} else {
							MsgObj.MsgError("客户端Web发送数据包命令没有合适的处理函数!["
									+ mCommand + "]");
							MsgObj.MsgTextClear();
							MsgObj.MsgFileClear();
						}
					}

					else if (mOption.equalsIgnoreCase("SAVEPAGE")) { // 下面的代码为保存为全文批注格式文件
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						MsgObj.MsgTextClear(); // 清除文本信息
						mFilePath = mFilePath + "\\Document\\" + mRecordID
								+ ".pgf"; // 全文批注文件的完整路径
						if (MsgObj.MsgFileSave(mFilePath)) { // 保存全文批注文件
							MsgObj.SetMsgByName("STATUS", "保存全文批注成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("保存全文批注失败!"); // 设置错误信息
						}
					}

					else if (mOption.equalsIgnoreCase("LOADPAGE")) { // 下面的代码为调入全文批注格式文件
						mRecordID = MsgObj.GetMsgByName("RECORDID"); // 取得文档编号
						MsgObj.MsgTextClear(); // 清除文本信息
						mFilePath = mFilePath + "\\Document\\" + mRecordID
								+ ".pgf"; // 全文批注文件的完整路径
						if (MsgObj.MsgFileLoad(mFilePath)) { // 调入文档内容
							MsgObj.SetMsgByName("STATUS", "打开全文批注成功!"); // 设置状态信息
							MsgObj.MsgError(""); // 清除错误信息
						} else {
							MsgObj.MsgError("打开全文批注失败!"); // 设置错误信息
						}
					}
				} else {
					MsgObj.MsgError("客户端发送数据包错误!");
					MsgObj.MsgTextClear();
					MsgObj.MsgFileClear();
				}
			} else {
				MsgObj.MsgError("请使用Post方法");
				MsgObj.MsgTextClear();
				MsgObj.MsgFileClear();
			}
			// SendPackage(response); //老版后台类返回信息包数据方法
			MsgObj.Send(response); // 8.1.0.2新版后台类新增的功能接口，返回信息包数据
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

}
