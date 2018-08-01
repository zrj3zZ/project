

package com.iwork.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.channels.FileChannel;
import org.apache.log4j.Logger;
/**
 * 文件工具
 * 
 * @author David.Yang
 * @version 2.2.1
 * @preserve 声明此方法不被JOC混淆
 */
public class UtilFile extends File {
	/***/
	protected int m_nMode;

	/***/
	protected FileInputStream m_reader;

	/***/
	protected FileOutputStream m_writer;
	private static Logger logger = Logger.getLogger(UtilFile.class);
	
	/**
	 * 获得驶入文件名称及路径，返回文件名
	 * @param strFileName
	 * @return
	 */
    public static String getFileName(String strFileName)
    {
        if(strFileName == null)
            return "";
        if(strFileName.length() == 0)
            return "";
        int nLen = strFileName.length();
        for(int i = nLen - 1; i >= 0; i--)
            if(strFileName.charAt(i) == '\\')
                return strFileName.substring(i + 1);

        return strFileName;
    }

	/**
	 * 
	 * @param path
	 *            文件路径
	 * @preserve 声明此方法不被JOC混淆
	 */
	public UtilFile(String path) {
		super(path);
		m_nMode = 0;
		m_reader = null;
		m_writer = null;
	}

	/**
	 * @param path
	 * @param nMode
	 * @preserve 声明此方法不被JOC混淆
	 */
	public UtilFile(String path, int nMode) {
		super(path);
		m_nMode = nMode;
		m_reader = null;
		if (super.exists()) {
			m_writer = null;
		} else if (nMode > 0) {
			try {
				m_writer = new FileOutputStream(super.getAbsolutePath(), (nMode == 2));
				String str = "";
				byte[] b = str.getBytes();
				m_writer.write(b);
			} catch (Exception e) {
				/* debug to err.logging */
				logger.error(e,e);
			}
		}
	}

	/**
	 * @param dir
	 * @param name
	 * @param nMode
	 * @preserve 声明此方法不被JOC混淆
	 */
	public UtilFile(File dir, String name, int nMode) {
		super(dir, name);
		m_nMode = nMode;
		m_reader = null;
		if (super.exists()) {
			m_writer = null;
		} else if (nMode > 0) {
			try {
				m_writer = new FileOutputStream(super.getAbsolutePath(), (nMode == 2));
				String str = "";
				byte[] b = str.getBytes();
				m_writer.write(b);
			} catch (Exception e) {
				/* debug to err.logging */
				logger.error(e,e);
			}
		}
	}

	/**
	 * @param path
	 * @param name
	 * @param nMode
	 * @preserve 声明此方法不被JOC混淆
	 */
	public UtilFile(String path, String name, int nMode) {
		super(path, name);
		m_nMode = nMode;
		m_reader = null;
		if (super.exists()) {
			m_writer = null;
		} else if (nMode > 0) {
			try {
				m_writer = new FileOutputStream(super.getAbsolutePath(), (nMode == 2));
				String str = "";
				byte[] b = str.getBytes();
				m_writer.write(b);
			} catch (Exception e) {
				/* debug to err.logging */
				logger.error(e,e);
			}
		}
	}

	/**
	 * 将当前文件另存到另一个文件
	 * @param path
	 * @return
	 * @author Administrator
	 */
	public int saveAs(String path){
//		File tmpFile=new File(path);
//		if(tmpFile.exists())tmpFile.deleteOnExit();
		try{
			byte[] bytes=this.readBytes();
			FileOutputStream os=new FileOutputStream(path,false);
			os.write(bytes);
			os.flush();
			os.close();
			return bytes.length;
		}catch(Exception e){
			logger.error(e,e);
			return -1;
		}
	}
	
	/**
	 * 复制文件
	 * 通过管道到管道的方式(性能最优)
	 * @param f1
	 * @param f2
	 * @return
	 * @throws Exception
	 */
	public static long copyFile(File f1,File f2) throws Exception{
        int length=2097152;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        int i=0;
        while(true){
            if(inC.position()==inC.size()){
            	in.close();
            	out.close();
                inC.close();
                outC.close();
                break;
            }
            if((inC.size()-inC.position())<20971520)
                length=(int)(inC.size()-inC.position());
            else
                length=20971520;
            inC.transferTo(inC.position(),length,outC);
            inC.position(inC.position()+length);
            i++;
        }
        return length;
    }
	
	/**
	 * @param off
	 * @param b
	 * @param buffersize
	 * @preserve 声明此方法不被JOC混淆
	 */
	public int read(int off, byte[] b, int buffersize) {
		try {
			if (m_reader == null) {
				m_reader = new FileInputStream(this);
			}
			return m_reader.read(b, off, buffersize);
		} catch (Exception e) {logger.error(e,e);
			return -1;
		}
	}

	/**
	 * @preserve 声明此方法不被JOC混淆
	 * @return
	 */
	public String readLine() {
		try {
			if (m_reader == null) {
				m_reader = new FileInputStream(this);
			} else {
				String buffer = "";
				int temp = m_reader.read();
				String flag = System.getProperty("line.separator");
				int counter = 0;
				while (temp != -1) {
					if ((char) temp != flag.charAt(counter)) {
						counter++;
					}
					if (counter < (flag.length() - 1)) {
						buffer += (char) temp;
					} else {
					}
				}
				return new String(buffer);
			}
			return null;
		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
			return null;
		}

	}

	/**
	 * @param string
	 * @preserve 声明此方法不被JOC混淆
	 */
	public synchronized void writeLine(String string) {
		try {
			if (m_writer == null) {
				if (m_nMode == 1) {
					m_writer = new FileOutputStream(this);
				} else if (m_nMode == 2) {
					m_writer = new FileOutputStream(this.getAbsolutePath(), true);
				}
			}
			string += "\n";
			byte[] b = string.getBytes();
			m_writer.write(b);
			m_writer.flush();

		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
		}
	}

	/**
	 * @param fileName
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static String readAll(String fileName) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			String res;
			int len = in.available();
			if (len > 0) {
				byte[] buf = new byte[len];
				in.read(buf, 0, len);
				res = (new String(buf));
			} else {
				res = null;
			}
			return res;
		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
			return null;
		} finally {
			try {
				in.close();
				in = null;
			} catch (Exception e) {logger.error(e,e);
			}
		}
	}

	/**
	 * @param fileName
	 * @param b
	 * @preserve 声明此方法不被JOC混淆
	 */
	public static void readAll(String fileName, byte[] b) {
		try {
			FileInputStream in = new FileInputStream(fileName);
			int len = in.available();
			if (len > 0) {
				in.read(b, 0, len);
			} else {
				b = null;
			}
			in.close();
		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
			b = null;
		}
	}

	/**
	 * @preserve 声明此方法不被JOC混淆
	 * @return
	 */
	public String readAll() {
		try {
			if (m_reader == null) {
				FileInputStream in = new FileInputStream(this);
				int len = in.available();
				byte[] buf = new byte[len];
				in.read(buf, 0, len);
				in.close();
				return (new String(buf));
			}
			return null;
		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
			return null;
		}

	}
	
	/**
	 * @preserve 声明此方法不被JOC混淆
	 * @return
	 */
	public byte[] readBytes() {
		try {
			if (m_reader == null) {
				FileInputStream in = new FileInputStream(this);
				int len = in.available();
				byte[] buf = new byte[len];
				in.read(buf, 0, len);
				in.close();
				return buf;
			}
			return null;
		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
			return null;
		}
		
	}

	/**
	 * @param b
	 * @preserve 声明此方法不被JOC混淆
	 */
	public synchronized void write(byte[] b) {
		try {
			if (m_writer == null) {
				if (m_nMode == 1) {
					m_writer = new FileOutputStream(this);
				} else if (m_nMode == 2) {
					m_writer = new FileOutputStream(this.getAbsolutePath(), true);
				}
			}
			m_writer.write(b);
			m_writer.flush();

		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
		}
	}

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public int GetMode() {
		return m_nMode;
	}

	/**
	 * @param string
	 * @preserve 声明此方法不被JOC混淆
	 */
	public synchronized void write(String string) {
		try {
			if (m_writer == null) {
				if (m_nMode == 1) {
					m_writer = new FileOutputStream(this);
				} else if (m_nMode == 2) {
					m_writer = new FileOutputStream(this.getAbsolutePath(), true);
				}
			}
			byte[] b = string.getBytes();
			m_writer.write(b);
			m_writer.flush();

		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
		}
	}

	/**
	 * @preserve 声明此方法不被JOC混淆
	 */
	public void close() {
		try {
			if (m_reader != null) {
				m_reader.close();
				m_reader = null;
			}
			if (m_writer != null) {
				m_writer.close();
				m_writer = null;
			}

		} catch (Exception e) {
			/* debug to err.logging */
			logger.error(e,e);
		}
	}

}
