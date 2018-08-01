package com.iwork.plugs.cms.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.iwork.app.conf.SystemConfig;
import com.iwork.commons.util.FileUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.cms.model.IworkCmsContent;
import org.apache.log4j.Logger;
/**
 * CMS集成方法
 * 
 * @author WeiGuangjian
 * 
 */
public class CmsUtil {
	private static Logger logger = Logger.getLogger(CmsUtil.class);
	private static CmsUtil instance;
	// 文件保存目录路径
	String path = ServletActionContext.getServletContext().getRealPath(SystemConfig._fileServerConf.getCmsFilePath().replaceAll("\\.\\.", ""));
	private String savePath = path+File.separator+ "content";
	private static Object lock = new Object();

	public static CmsUtil getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new CmsUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * 保存正文
	 * 
	 * @param contentId
	 * @param content
	 * @return
	 */
	public boolean saveCmsContent(Long contentId, String content) {
		// 判断文件夹信息
		File dirFile = null;
		try {
			dirFile = new File(savePath);
			if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
				boolean creadok = dirFile.mkdirs();			
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		String file_path = savePath + File.separator + "CMS_CONTENT_" + contentId;
		// 创建文件
		try {
			File f = new File(file_path);
			f.createNewFile();
			BufferedWriter utput = new BufferedWriter(new FileWriter(f));
			utput.write(content);
			utput.close();
		} catch (Exception e) {
			logger.error(e,e);
		}
		return false;
	}

	/**
	 * 获得正文
	 * 
	 * @param contentId
	 * @return
	 */
	public String getCmsContent(Long contentId) {
		String file_path = savePath + File.separator + "CMS_CONTENT_" + contentId;
		String html = FileUtil.readFile(file_path);
		return html;
	}

	/**
	 * 判断当前用户是否为管理员
	 * 
	 * @param manager
	 * @return
	 */
	public static boolean isManager(String manager) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (manager != null && !manager.equals("")) {
			String[] m = manager.split(",");
			for (int i = 0; i < m.length; i++) {
				if (uc._userModel.getUserid().equals(m[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前用户的浏览权限
	 * 
	 * @param list
	 * @return
	 */
	public static boolean getContentSecurityList(String list) {
		boolean flag = false;
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		if (list == null || list.equals("")) {
			flag = true;
		} else {
			String[] temp = list.split(" ");
			for (int i = 0; i < temp.length; i++) {
				String[] temp2 = temp[i].split(":");
				if (temp2 != null && temp2.length > 1) {
					String[] temp3 = temp2[1].split(",");
					for (int j = 0; j < temp3.length; j++) {
						if (temp2[0].equals("user")) {// 如果是用户
							if (uc._userModel.getUserid().equals(temp3[j])) {
								flag = true;
							}
						} else if (temp2[0].equals("dept")) {// 如果是部门
							if (uc._userModel.getDepartmentid()
									.equals(temp3[j])) {
								flag = true;
							}
						} else if (temp2[0].equals("role")) {// 如果是角色
							if (uc._userModel.getOrgroleid().equals(temp3[j])) {
								flag = true;
							}
						}
					}
				} else {
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 时间格式转换：Date转String
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}

	/**
	 * 时间格式转换：Date转String
	 * 
	 * @param date
	 * @return
	 */
	public static String dateShortFormat(Date date) {
		String str = "";
		if (date != null) {
			DateFormat format = new SimpleDateFormat("MM-dd");
			str = format.format(date);
		}

		return str;
	}

	/**
	 * 时间格式转换：String转Date
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date dateFormat(String str){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try{
		 date = format.parse(str);
		}catch(Exception e){
			logger.error(e,e);
		}
		return date;
	}

	/**
	 * 时间格式转换：Date转String
	 * 
	 * @param date
	 * @return
	 */
	public static String timeFormat(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

	/**
	 * 时间格式转换：String转Date
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date timeFormat(String str) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(str);
		return date;
	}

	/**
	 * 求时间差
	 * 
	 * @param inputDate
	 * @param resultDate
	 * @return
	 */
	public static long getSubtractionDate(Date inputDate, Date resultDate) {
		if (!inputDate.equals("") && !resultDate.equals("")
				&& !resultDate.equals("&nbsp;")) {
			Calendar inputCalendar = Calendar.getInstance();
			Calendar resultCalendar = Calendar.getInstance();
			inputCalendar.setTime(inputDate);
			resultCalendar.setTime(resultDate);
			Date d1 = inputCalendar.getTime();
			Date d2 = resultCalendar.getTime();
			long daterange = d2.getTime() - d1.getTime();
			long time = 1000 * 3600 * 24; // A day in milliseconds
			return daterange / time;
		} else {
			return -1;
		}
	}

	/**
	 * 判断是否在有效期内
	 * 
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	public static boolean getEffect(Date begindate, Date enddate) {
		Date now = new Date();
		if (now.getTime() - begindate.getTime() >= 0
				&& enddate.getTime() - now.getTime() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 标题属性
	 * 
	 * @param contentModel
	 * @return
	 */
	public static String getCmsTitle(IworkCmsContent contentModel) {
		String title = "";
		String color_begin = "";
		String color_end = "";

		String strong_begin = "";
		String strong_end = "";
		// 标红
		if (contentModel.getMarkred() == 1) {
			color_begin = "<font color='#AC0A00'>";
			color_end = "</font>";
		}
		// 加粗
		if (contentModel.getMarkbold() == 1) {
			strong_begin = "<span style='font-weight:bold;'>";
			strong_end = "</span>";
		}
		title = color_begin + strong_begin + contentModel.getTitle()
				+ strong_end + color_end;
		return title;
	}

	/**
	 * 显示标题属性
	 * 
	 * @param contentModel
	 * @return
	 */
	public static String getCmsBriefTitle(IworkCmsContent contentModel) {
		String title = "";
	StringBuffer span = new StringBuffer();
		boolean isCss = false;
		span.append("<span class=\"");
		// 标红
		if (contentModel.getMarkred() == 1) {
			isCss = true;
			span.append("cms_title_red ");
		}
		// 加粗
		if (contentModel.getMarkbold() == 1) {
			isCss = true;
			span.append(" cms_title_b");
		}
		span.append("\" >");
		if(isCss){
			title = span.append(contentModel.getBrieftitle()).append("</span>").toString();	
		}else{
			title = contentModel.getBrieftitle();
		}
		return title.toString();
	}

	public static int getColumnPageShowNum(String keyWord){
		//1轮播图 2新闻 7行业新闻 11培训 15常用资料 17博客
		//0今日寿星 1天气预报 3视频
		//iworkMainPage.action?channelid=0&columnName=Video
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("FocusPic", 1);
		map.put("NewsInfo", 2);
		map.put("blj", 7);
		map.put("Train", 11);
		map.put("Appkm", 15);
		map.put("ksBlog", 17);
		map.put("BirthDay", 100);
		map.put("Stock", 101);
		map.put("Video", 102);
		
		Integer result = map.get(keyWord);
		if(result != null  && result > 0){
			return result;
		}else{
			return 0;
		}
	}
	
}
