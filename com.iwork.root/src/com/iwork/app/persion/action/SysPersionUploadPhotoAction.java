package com.iwork.app.persion.action;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.upload.Fileupload;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.persion.service.SysPersionService;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;
public class SysPersionUploadPhotoAction extends ActionSupport {
	private static Logger logger = Logger.getLogger(SysPersionUploadPhotoAction.class);
	private static final long serialVersionUID = 572146812454l;
	private static final int BUFFER_SIZE = 16 * 1024;
	private SysPersionService sysPersionService;
	private File myFile;
	private String contentType;
	private String fileName;
	private String imageFileName;
	private String caption;
	private OrgUser userModel;
	private String imgInfo;// 剪切图片的信息
	private String userid;
	private String isUserImageExists;
	public static final int LARGE_WIDTH = 120;
	public static final int LARGE_HEIGH = 120;
	public static final int MIDDLE_WIDTH = 90;
	public static final int MIDDLE_HEIGH = 90;
	public static final int SMALL_WIDTH = 60;
	public static final int SMALL_HEIGH = 60;
	public static final String LARGE_PATH = File.separator+"large"+File.separator;
	public static final String MIDDLE_PATH = File.separator+"middle"+File.separator;
	public static final String SMALL_PATH = File.separator+"small"+File.separator;
	 public final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	 public String getConfigUUID(String parameter) {
			Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
			String result="";
			if(parameter!=null&&!"".equals(parameter)){
				result=config.get(parameter)==null?"":config.get(parameter);
			}
			return result;
		}
	/**
	 * 执行头像上传
	 */
	@Override
	public String execute() throws Exception {
		if (fileName == null) {
			fileName = "";
		}
		imageFileName = this.getUserid() + ".jpg";
		File imageFile = new File(ServletActionContext.getServletContext()
				.getRealPath(SystemConfig._fileServerConf.getUserPhotoPath())
				+ File.separator+ imageFileName);
		String fileurl=ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getUserPhotoPath())+ File.separator+ imageFileName;
		boolean flag = copy(myFile, imageFile);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		String zqserver = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
		if(zqserver.equals("gxzq")){
			Fileupload fileupload= new Fileupload();
			fileupload.fileScann(imageFileName,fileurl);
		}
		if (flag) {
			response.getWriter().print("{flag:true}");
			this.setIsUserImageExists(this.getIsUserImageExists());
		} else {
			response.getWriter().print("{flag:false}");
		}
		return null;
	}

	/**
	 * 保存头像
	 * 
	 * @return
	 */
	public String saveImage() {
		String imgInfoTemp = this.getImgInfo();
		imageFileName = this.getUserid() + ".jpg";
		String srcImageFile = ServletActionContext.getServletContext()
				.getRealPath(SystemConfig._fileServerConf.getUserPhotoPath())
				+ File.separator + imageFileName;
		String outLargePath = ServletActionContext.getServletContext()
				.getRealPath(SystemConfig._fileServerConf.getUserPhotoPath())
				+ LARGE_PATH + imageFileName;
		String outMiddlePath = ServletActionContext.getServletContext()
				.getRealPath(SystemConfig._fileServerConf.getUserPhotoPath())
				+ MIDDLE_PATH + imageFileName;
		String outSmallPath = ServletActionContext.getServletContext()
				.getRealPath(SystemConfig._fileServerConf.getUserPhotoPath())
				+ SMALL_PATH + imageFileName;
		File userImage = new File(srcImageFile);
		if ("".equals(imgInfoTemp)) {
			if (userImage.exists()) {// 如果图片存在,没有做任何剪切动作
				scaleAndSaveImg(srcImageFile, outLargePath, LARGE_WIDTH,
						LARGE_HEIGH);
				scaleAndSaveImg(srcImageFile, outMiddlePath, MIDDLE_WIDTH,
						MIDDLE_HEIGH);
				scaleAndSaveImg(srcImageFile, outSmallPath, SMALL_WIDTH,
						SMALL_HEIGH);
				ResponseUtil.write("success");
			}
		} else {
			String[] tempImgArr = imgInfoTemp.split("_");
			int boundx = Integer.parseInt(tempImgArr[0]);// 网页中图片为定宽,图片被同比例缩放,此参数为定宽的值
			int rx = Integer.parseInt(tempImgArr[1]);// 在定宽下截取的起始坐标X
			int ry = Integer.parseInt(tempImgArr[2]);// 在定宽下截取的起始坐标Y
			int rw = Integer.parseInt(tempImgArr[3]);// 在定宽情况下截取的长
			int rh = Integer.parseInt(tempImgArr[4]);// 在定宽情况下截取的宽
			if (!userImage.exists()) {// 如果用户图片不存在，加载default
				String defaultImageFile = ServletActionContext
						.getServletContext().getRealPath("/iwork_img")
						+ File.separator
						+ "default_userImg.jpg";
				File defaultImage = new File(defaultImageFile);
				copy(defaultImage, userImage);
			}
			String msg = cutImage(srcImageFile, boundx, rx, ry, rw, rh,
					this.getUserid());
			ResponseUtil.write(msg);
		}
		return null;
	}

	/**
	 * 删除头像
	 * 
	 * @return
	 */
	public String deleteImage() {
		userid = this.getUserid();
		if (userid != null) {
			imageFileName = userid + ".jpg";
			String srcImageFile = ServletActionContext.getServletContext()
					.getRealPath(
							SystemConfig._fileServerConf.getUserPhotoPath())
					+ File.separator + imageFileName;
			String outLargePath = ServletActionContext.getServletContext()
					.getRealPath(
							SystemConfig._fileServerConf.getUserPhotoPath())
					+ LARGE_PATH + imageFileName;
			String outMiddlePath = ServletActionContext.getServletContext()
					.getRealPath(
							SystemConfig._fileServerConf.getUserPhotoPath())
					+ MIDDLE_PATH + imageFileName;
			String outSmallPath = ServletActionContext.getServletContext()
					.getRealPath(
							SystemConfig._fileServerConf.getUserPhotoPath())
					+ SMALL_PATH + imageFileName;
			File userImage = new File(srcImageFile);
			File userLargeImage = new File(outLargePath);
			File userMiddleImage = new File(outMiddlePath);
			File userSmallImage = new File(outSmallPath);
			if (userImage.exists()) {
				userImage.delete();
			}
			if (userLargeImage.exists()) {
				userLargeImage.delete();
			}
			if (userMiddleImage.exists()) {
				userMiddleImage.delete();
			}
			if (userSmallImage.exists()) {
				userSmallImage.delete();
			}
		}
		return SUCCESS;
	}

	/**
	 * 用来复制图片的方法
	 * 
	 * @param src
	 * @param dst
	 */
	private boolean copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
			out.flush();
		} catch (Exception e) {
			logger.error(e,e);
			return false;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		return true;
	}

	/**
	 * 剪切图片的方法
	 * 
	 * @param srcImageFile
	 *            //源图路径
	 * @param boundx
	 *            ////网页中图片为定宽,图片被同比例缩放,此参数为定宽的值
	 * @param rx
	 *            //在定宽下截取的起始坐标X
	 * @param ry
	 *            //在定宽下截取的起始坐标Y
	 * @param rw
	 *            //在定宽情况下截取的长
	 * @param rh
	 *            //在定宽情况下截取的宽
	 */
	private static String cutImage(String srcImageFile, int boundx, int rx,
			int ry, int rw, int rh, String userid) {
		String result = "";
		try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			int x = Math.round((rx * srcWidth) / boundx);// 在实际图像中进行剪切时的起始x
			int y = Math.round((ry * srcHeight)
					/ ((boundx * srcHeight) / srcWidth));// 在实际图像中进行剪切时的起始y
			int destWidth = Math.round((rw * srcWidth) / boundx);
			;// 在实际图像中进行剪切时的目标长
			int destHeight = Math.round((rh * srcHeight)
					/ ((boundx * srcHeight) / srcWidth));// 在实际图像中进行剪切时的目标宽
			if (destWidth >= 50 && destHeight >= 50) {// 图片最小尺寸
				if (srcWidth >= destWidth && srcHeight >= destHeight) {
					Image image = bi.getScaledInstance(srcWidth, srcHeight,
							Image.SCALE_DEFAULT);
					cropFilter = new CropImageFilter(x, y, destWidth,
							destHeight);
					img = Toolkit.getDefaultToolkit().createImage(
							new FilteredImageSource(image.getSource(),
									cropFilter));
					BufferedImage tag = new BufferedImage(destWidth,
							destHeight, BufferedImage.TYPE_INT_RGB);
					Graphics g1 = tag.getGraphics();
					Graphics2D g = (Graphics2D) g1;
					g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g.drawImage(img, 0, 0, null); // 绘制剪切后的图
					g.dispose();
					// 输出为文件
					ImageIO.write(tag, "jpg", new File(srcImageFile));
					// 存储大中小三种尺寸图片
					File userImage = new File(srcImageFile);
					if (userImage.exists()) {// 如果图片存在,没有做任何剪切动作
						String outLargePath = ServletActionContext
								.getServletContext().getRealPath(
										SystemConfig._fileServerConf
												.getUserPhotoPath())
								+ LARGE_PATH + userid + ".jpg";
						String outMiddlePath = ServletActionContext
								.getServletContext().getRealPath(
										SystemConfig._fileServerConf
												.getUserPhotoPath())
								+ MIDDLE_PATH + userid + ".jpg";
						String outSmallPath = ServletActionContext
								.getServletContext().getRealPath(
										SystemConfig._fileServerConf
												.getUserPhotoPath())
								+ SMALL_PATH + userid + ".jpg";
						scaleAndSaveImg(srcImageFile, outLargePath,
								LARGE_WIDTH, LARGE_HEIGH);
						scaleAndSaveImg(srcImageFile, outMiddlePath,
								MIDDLE_WIDTH, MIDDLE_HEIGH);
						scaleAndSaveImg(srcImageFile, outSmallPath,
								SMALL_WIDTH, SMALL_HEIGH);
					}
					result = "success";
				} else {
					result = "ERROR-1002";
				}
			} else {
				result = "ERROR-1003";
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		return result;
	}

	/**
	 * 缩放图片的方法
	 * 
	 * @param inPath
	 *            输入路径
	 * @param outPath
	 *            输出路径
	 * @param w
	 *            缩放的尺寸
	 * @param h
	 *            缩放的尺寸
	 */
	private static void scaleAndSaveImg(String inPath, String outPath, int w,
			int h) {
		try {
			Image image = ImageIO.read(new File(inPath));
			BufferedImage tmp = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2.drawImage(image, 0, 0, w, h, null);
			g2.dispose();
			FileOutputStream newimage = new FileOutputStream(outPath);
			ImageIO.write(tmp, "JPEG", newimage);
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	// ==================POJO==============================================
	public SysPersionService getSysPersionService() {
		return sysPersionService;
	}

	public void setSysPersionService(SysPersionService sysPersionService) {
		this.sysPersionService = sysPersionService;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public OrgUser getUserModel() {
		return userModel;
	}

	public void setUserModel(OrgUser userModel) {
		this.userModel = userModel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getBufferSize() {
		return BUFFER_SIZE;
	}

	public String getImgInfo() {
		return imgInfo;
	}

	public void setImgInfo(String imgInfo) {
		this.imgInfo = imgInfo;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getIsUserImageExists() {
		return isUserImageExists;
	}

	public void setIsUserImageExists(String isUserImageExists) {
		this.isUserImageExists = isUserImageExists;
	}
}
