package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.Region;

import com.ibpmsoft.project.zqb.dao.DongGuanZqbProjectManageDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.iwork.sdk.ProcessAPI;
public class DongGuanZqbProjectManageService {
	private static Logger logger = Logger.getLogger(DongGuanZqbProjectManageService.class);
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	private DongGuanZqbProjectManageDAO dongGuanZqbProjectManageDAO;

	public void setDongGuanZqbProjectManageDAO(
			DongGuanZqbProjectManageDAO dongGuanZqbProjectManageDAO) {
		this.dongGuanZqbProjectManageDAO = dongGuanZqbProjectManageDAO;
	}
	
	public String getConfigUUID(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		String result="";
		if(parameter!=null&&!"".equals(parameter)){
			result=config.get(parameter);
		}
		return result;
	}
	
	public List<HashMap> getRunList(int pageSize,int pageNumber,String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		return dongGuanZqbProjectManageDAO.getRunList(pageSize,pageNumber,customername,sssyb,cyrName,xmlx,xmjd,ssxq,type);
	}
	public int getRunListSize(String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		return dongGuanZqbProjectManageDAO.getRunListSize(customername,sssyb,cyrName,xmlx,xmjd,ssxq,type).size();
	}
	
	public List<HashMap> getCloseList(int pageSize,int pageNumber,String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		return dongGuanZqbProjectManageDAO.getCloseList(pageSize,pageNumber,customername,sssyb,cyrName,xmlx,xmjd,ssxq,type);
	}
	public int getCloseListSize(String customername,String sssyb,String cyrName,String xmlx,String xmjd,String ssxq,String type){
		return dongGuanZqbProjectManageDAO.getCloseListSize(customername,sssyb,cyrName,xmlx,xmjd,ssxq,type).size();
	}
	
	public boolean projectClose(String tag, String instanceId) {
		String uuid = "";
		Long ins = Long.parseLong(instanceId);
		HashMap hash = DemAPI.getInstance().getFromData(ins,EngineConstants.SYS_INSTANCE_TYPE_DEM);
		Long dataid = (Long) hash.get("ID");
		String projectname = "";
		if(tag.equals("3")){
			hash.put("XMZT", "0");
			uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='并购重组项目'", "UUID");
			projectname = hash.get("JYF").toString();
		}else{
			hash.put("STATUS", "已完成");
			if(tag.equals(1)){
				uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='项目基本情况'", "UUID");//1
			}else if(tag.equals(2)){
				uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='定向增发项目'", "UUID");//2
			}else if(tag.equals(4)){
				uuid = DBUtil.getString("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='IPO项目'", "UUID");//4
			}
			projectname = hash.get("PROJECTNAME").toString();
		}
		boolean updateFormData = DemAPI.getInstance().updateFormData(uuid, ins,hash, dataid, false);
		if(updateFormData){
			LogUtil.getInstance().addLog(dataid, "东莞项目管理维护", "关闭项目："+projectname);
		}
		return updateFormData;
	}
	
	public void expPj(HttpServletResponse response,String expfields,String customername,String sssyb,String cyrName,String type,String xmlx) {
		List<String> listExpfields = new ArrayList<String>();
		String[] arrExpfields = expfields.split(",");
		for (int i = 0; i < arrExpfields.length; i++) {
			listExpfields.add(arrExpfields[i]);
		}
		List<HashMap> dailyList = null;
		boolean stagesinfo = listExpfields.contains("PROJECTSTAGESINFO");
		if(type.equals("xsb")){
			if(xmlx.equals("推荐挂牌")){
				dailyList = dongGuanZqbProjectManageDAO.getTjgpProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expTjgpPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("定增")){
				dailyList = dongGuanZqbProjectManageDAO.getDxzfProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expDxzfPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("并购重组")){
				dailyList = dongGuanZqbProjectManageDAO.getBgczProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expBgczPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("其他")){
				dailyList = dongGuanZqbProjectManageDAO.getOtherProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}
		}else if(type.equals("ssgs")){
			if(xmlx.equals("首次公开发行股票")){
				dailyList = dongGuanZqbProjectManageDAO.getScgkProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("再融资")){
				dailyList = dongGuanZqbProjectManageDAO.getZrzProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("并购重组")){
				dailyList = dongGuanZqbProjectManageDAO.getBgczProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expBgczPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("其他")){
				dailyList = dongGuanZqbProjectManageDAO.getOtherProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}
		}else if(type.equals("zq")){
			if(xmlx.equals("公司债")){
				dailyList = dongGuanZqbProjectManageDAO.getGszProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("企业债")){
				dailyList = dongGuanZqbProjectManageDAO.getQyzProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("可交换债")){
				dailyList = dongGuanZqbProjectManageDAO.getJhzProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}else if(xmlx.equals("其他")){
				dailyList = dongGuanZqbProjectManageDAO.getOtherProList(stagesinfo,customername,sssyb,cyrName,type,xmlx);
				expOtherPro(response, dailyList, listExpfields);
			}
		}
		
	}

	private void expTjgpPro(HttpServletResponse response, List<HashMap> dailyList, List<String> listExpfields) {
		HashMap<Integer,Integer> colMap = new HashMap<Integer,Integer>();//存储每列的最大长度,用于设置列宽
		List<Integer> colRegion = new ArrayList<Integer>();//存储需要合并的列
		int colNum = 0;
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目基本情况汇总");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setFillForegroundColor((short) 22);
		style3.setFillPattern((short) 1);
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 1);
		style3.setBorderTop((short) 1);
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setWrapText(true);
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setWrapText(true);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor((short) 22);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);
		
		int m=0;
		int z=0;
		int n=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = null;
		if(listExpfields.contains("PROJECTBASEINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目名称".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属行业");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属行业".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("2年累计净利润（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "2年累计净利润（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目承揽人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目承揽人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("总收费（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "总收费（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("是否需要协同");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "是否需要协同".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("住所地");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "住所地".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交叉销售部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交叉销售部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交叉销售分成(元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交叉销售分成(元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("协议支出单位");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "协议支出单位".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("协议支出(%)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "协议支出(%)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("进展阶段");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "进展阶段".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属证监局");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属证监局".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人职务");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系人职务".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("证监会行业分类");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "证监会行业分类".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所在地(省市)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所在地(省市)".length());colNum++;
		}else{
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目名称".length());colNum++;
		}
		if(listExpfields.contains("PROJECTJOINERSINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("保荐业务部负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "保荐业务部负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("督导安排");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "督导安排".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目注会");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目注会".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目行研");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目行研".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目律师");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目律师".length());colNum++;
		}
		if(listExpfields.contains("PROJECTPROCESSINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("股改基准日");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "股改基准日".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("申报时间");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "申报时间".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("受理日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "受理日期".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("内核日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "内核日期".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("通知挂牌日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "通知挂牌日期".length());colNum++;
		}
		if(listExpfields.contains("PROJECTCHARGEINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("首付（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "首付（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("股改完成（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "股改完成（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("内核完成（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "内核完成（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("挂牌完成（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "挂牌完成（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("挂牌当年（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "挂牌当年（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("后续（万元/年）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "后续（万元/年）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("股权（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "股权（万元）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("债权（万元）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "债权（万元）".length());colNum++;
		}
		if(listExpfields.contains("PROJECTSTAGESINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("填报人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段资料");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTMEMBERINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目成员列表");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTAGENCYINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目中介机构");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("FINANCEDATA")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("财务数据");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTERRORRECORD")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目异常情况记录");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		colNum = 0;
		
		String projectnoBase = "";
		List<HashMap> projectErrorRecordList =  null;
		List lables = new ArrayList();
		lables.add("QUESTION");
		lables.add("CONTENT");
		Map params = new HashMap();
		String sql = "SELECT A.QUESTION,B.CONTENT FROM(SELECT B.QUESTION,B.TASKNO,B.ID,B.XMBH FROM BD_ZQB_XMWTFK B) A LEFT JOIN (SELECT C.CONTENT,C.QUESTIONNO FROM BD_ZQB_RETALK C) B ON A.ID=B.QUESTIONNO WHERE 1=1 AND A.XMBH=?";
		
		for (HashMap map : dailyList) {
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2= null;
			if(listExpfields.contains("PROJECTBASEINFO")){
				cell2 = row2.createCell((short) n++);
				String PROJECTNAME = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
				cell2.setCellValue(PROJECTNAME);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<PROJECTNAME.length()?PROJECTNAME.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMBZ = map.get("XMBZ")==null?"":map.get("XMBZ").toString();
				cell2.setCellValue(XMBZ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMBZ.length()?XMBZ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMYS = map.get("XMYS")==null?"":map.get("XMYS").toString();
				cell2.setCellValue(XMYS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMYS.length()?XMYS.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String ZCLR = map.get("ZCLR")==null?"":map.get("ZCLR").toString();
				cell2.setCellValue(ZCLR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<ZCLR.length()?ZCLR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String HTJE = map.get("HTJE")==null?"":map.get("HTJE").toString();
				cell2.setCellValue(HTJE);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<HTJE.length()?HTJE.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SFXZCL = map.get("SFXZCL")==null?"":map.get("SFXZCL").toString();
				cell2.setCellValue(SFXZCL);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SFXZCL.length()?SFXZCL.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CUSTOMERINFO = map.get("CUSTOMERINFO")==null?"":map.get("CUSTOMERINFO").toString();
				cell2.setCellValue(CUSTOMERINFO);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CUSTOMERINFO.length()?CUSTOMERINFO.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String GSGK = map.get("GSGK")==null?"":map.get("GSGK").toString();
				cell2.setCellValue(GSGK);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GSGK.length()?GSGK.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String FZJGMC = map.get("FZJGMC")==null?"":map.get("FZJGMC").toString();
				cell2.setCellValue(FZJGMC);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<FZJGMC.length()?FZJGMC.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String A01 = map.get("A01")==null?"":map.get("A01").toString();
				cell2.setCellValue(A01);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<A01.length()?A01.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String YJSBSJ = map.get("YJSBSJ")==null?"":map.get("YJSBSJ").toString();
				cell2.setCellValue(YJSBSJ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<YJSBSJ.length()?YJSBSJ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMCY = map.get("XMCY")==null?"":map.get("XMCY").toString();
				cell2.setCellValue(XMCY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMCY.length()?XMCY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMJD = map.get("XMJD")==null?"":map.get("XMJD").toString();
				cell2.setCellValue(XMJD);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMJD.length()?XMJD.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SSXQ = map.get("SSXQ")==null?"":map.get("SSXQ").toString();
				cell2.setCellValue(SSXQ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SSXQ.length()?SSXQ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String LXRZW = map.get("LXRZW")==null?"":map.get("LXRZW").toString();
				cell2.setCellValue(LXRZW);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<LXRZW.length()?LXRZW.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SSHY = map.get("SSHY")==null?"":map.get("SSHY").toString();
				cell2.setCellValue(SSHY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SSHY.length()?SSHY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String GZYJDZ = map.get("GZYJDZ")==null?"":map.get("GZYJDZ").toString();
				cell2.setCellValue(GZYJDZ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GZYJDZ.length()?GZYJDZ.length():colMap.get(colNum));colNum++;
				
			}else{
				cell2 = row2.createCell((short) n++);
				String PROJECTNAME = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
				cell2.setCellValue(PROJECTNAME);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<PROJECTNAME.length()?PROJECTNAME.length():colMap.get(colNum));colNum++;
			}
			if(listExpfields.contains("PROJECTJOINERSINFO")){
				cell2 = row2.createCell((short) n++);
				String OWNER = map.get("OWNER")==null?"":map.get("OWNER").toString();
				cell2.setCellValue(OWNER);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<OWNER.length()?OWNER.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String MANAGER = map.get("MANAGER")==null?"":map.get("MANAGER").toString();
				cell2.setCellValue(MANAGER);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<MANAGER.length()?MANAGER.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String DDAP = map.get("DDAP")==null?"":map.get("DDAP").toString();
				cell2.setCellValue(DDAP);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<DDAP.length()?DDAP.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMZH = map.get("XMZH")==null?"":map.get("XMZH").toString();
				cell2.setCellValue(XMZH);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMZH.length()?XMZH.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMXY = map.get("XMXY")==null?"":map.get("XMXY").toString();
				cell2.setCellValue(XMXY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMXY.length()?XMXY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMLS = map.get("XMLS")==null?"":map.get("XMLS").toString();
				cell2.setCellValue(XMLS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMLS.length()?XMLS.length():colMap.get(colNum));colNum++;
			}
			if(listExpfields.contains("PROJECTPROCESSINFO")){
				cell2 = row2.createCell((short) n++);
				String GGJZR = map.get("GGJZR")==null?"":map.get("GGJZR").toString();
				cell2.setCellValue(GGJZR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GGJZR.length()?GGJZR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SBJZR = map.get("SBJZR")==null?"":map.get("SBJZR").toString();
				cell2.setCellValue(SBJZR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SBJZR.length()?SBJZR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CLSLR = map.get("CLSLR")==null?"":map.get("CLSLR").toString();
				cell2.setCellValue(CLSLR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CLSLR.length()?CLSLR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SHTGR = map.get("SHTGR")==null?"":map.get("SHTGR").toString();
				cell2.setCellValue(SHTGR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SHTGR.length()?SHTGR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String ENDDATE = map.get("ENDDATE")==null?"":map.get("ENDDATE").toString();
				cell2.setCellValue(ENDDATE);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<ENDDATE.length()?ENDDATE.length():colMap.get(colNum));colNum++;
			}
			if(listExpfields.contains("PROJECTCHARGEINFO")){
				cell2 = row2.createCell((short) n++);
				String A03 = map.get("A03")==null?"":map.get("A03").toString();
				cell2.setCellValue(A03);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<A03.length()?A03.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String A04 = map.get("A04")==null?"":map.get("A04").toString();
				cell2.setCellValue(A04);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<A04.length()?A04.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String A05 = map.get("A05")==null?"":map.get("A05").toString();
				cell2.setCellValue(A05);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<A05.length()?A05.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String A06 = map.get("A06")==null?"":map.get("A06").toString();
				cell2.setCellValue(A06);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<A06.length()?A06.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String A07 = map.get("A07")==null?"":map.get("A07").toString();
				cell2.setCellValue(A07);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<A07.length()?A07.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String A08 = map.get("A08")==null?"":map.get("A08").toString();
				cell2.setCellValue(A08);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<A08.length()?A08.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String YJZXYNJLR = map.get("YJZXYNJLR")==null?"":map.get("YJZXYNJLR").toString();
				cell2.setCellValue(YJZXYNJLR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<YJZXYNJLR.length()?YJZXYNJLR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String FXPGFS = map.get("FXPGFS")==null?"":map.get("FXPGFS").toString();
				cell2.setCellValue(FXPGFS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<FXPGFS.length()?FXPGFS.length():colMap.get(colNum));colNum++;
			}
			if(listExpfields.contains("PROJECTSTAGESINFO")){
				cell2 = row2.createCell((short) n++);
				String jdmc = map.get("JDMC")==null?"":map.get("JDMC").toString();
				cell2.setCellValue(jdmc);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<jdmc.length()?jdmc.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String tbr = map.get("TBR")==null?"":map.get("TBR").toString();
				cell2.setCellValue(tbr);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<tbr.length()?tbr.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String cjsj = map.get("TBSJ")==null?"":map.get("TBSJ").toString();
				cell2.setCellValue(cjsj);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<cjsj.length()?cjsj.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String fj = map.get("JDZL")==null?"":map.get("JDZL").toString();
				String fjName = "";
				String fileSrcName = "";
				List<FileUpload> fileObjs = FileUploadAPI.getInstance().getFileUploads(fj);
				for (int i = 0; i < fileObjs.size(); i++) {
					fileSrcName = fileObjs.get(i).getFileSrcName();
					if(i==0){
						fjName+=fileSrcName;
					}else{
						fjName+=("\n"+fileSrcName);
					}
					colMap.put(colNum, colMap.get(colNum)<fileSrcName.length()?fileSrcName.length():colMap.get(colNum));
				}
				colNum++;
				cell2.setCellValue(fjName);
				cell2.setCellStyle(style4);
				
			}
			if(listExpfields.contains("PROJECTMEMBERINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proMemberList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_XMCYLBDG");
				StringBuffer memberStr = new StringBuffer();
				for (HashMap member : proMemberList) {
					memberStr.append("姓名:").append(member.get("NAME")).append("---->>").append("所属部门:").append(member.get("DEPARTMENTNAME")).append("---->>")
					.append("职务:").append(member.get("POSITION")).append("---->>").append("联系电话:").append(member.get("TEL")).append("---->>")
					.append("手机:").append(member.get("PHONE")).append("---->>").append("邮箱:").append(member.get("EMAIL")).append("\n");
				}
				cell2.setCellValue(memberStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("PROJECTAGENCYINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proAgencyList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_XMZJJG");
				StringBuffer agencyStr = new StringBuffer();
				for (HashMap agency : proAgencyList) {
					agencyStr.append("中介机构名称:").append(agency.get("ZJJGMC")).append("---->>").append("联系人:").append(agency.get("LXR")).append("---->>")
					.append("联系电话:").append(agency.get("LXDH")).append("---->>").append("联系邮箱:").append(agency.get("LXYX")).append("\n");
				}
				cell2.setCellValue(agencyStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("FINANCEDATA")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proFinancEDataList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_BDGSCWQKJ");
				StringBuffer financEDataStr = new StringBuffer();
				for (HashMap financEData : proFinancEDataList) {
					financEDataStr.append("年份:").append(financEData.get("YEAR")).append("---->>").append("类型:").append(financEData.get("TYPE")).append("---->>")
					.append("总股本:").append(financEData.get("LRZE")).append("---->>").append("总资产:").append(financEData.get("ZZC")).append("---->>")
					.append("净资产:").append(financEData.get("JZC")).append("---->>").append("营业收入:").append(financEData.get("YYSR")).append("---->>")
					.append("净利润:").append(financEData.get("JLR")).append("---->>").append("经营活动现金流量净额:").append(financEData.get("XJLLJE")).append("\n");
				}
				cell2.setCellValue(financEDataStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
			}
			if(listExpfields.contains("PROJECTERRORRECORD")){
				String projectno = map.get("PROJECTNO")==null?"":map.get("PROJECTNO").toString();
				if(projectnoBase.equals("")||!projectno.equals(projectnoBase)){//判断是否第一条数据或是否下一个项目,避免多余的查询
					params.put(1, projectno);
					projectErrorRecordList =  com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
				}
				cell2 = row2.createCell((short) n++);
				StringBuffer projectErrorRecordStr = new StringBuffer();
				for (HashMap projectErrorRecord : projectErrorRecordList) {
					projectErrorRecordStr.append("主要问题:").append(projectErrorRecord.get("QUESTION")).append("---->>").append("解决方案:").append(projectErrorRecord.get("CONTENT")).append("\n");
				}
				cell2.setCellValue(projectErrorRecordStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
				
				projectnoBase = projectno;
			}
			n=0;
			colNum = 0;
		}
		
		for (int i = 0; i < colMap.size(); i++) {
			if(Short.parseShort(colMap.get(i).toString())>0){
				sheet.setColumnWidth((short)i, (short)Integer.parseInt(colMap.get(i).toString())*576);
			}
		}
		//Region(int left, int top, int right, int bottom) 
		
		int startRow = 1;//从第二行开始判断合并
		int num = 0;
		for (int i = 0; i < dailyList.size(); i+=num) {//i+=num  跳到下一组第一行
			num = Integer.parseInt(dailyList.get(i).get("NUM")==null?"1":dailyList.get(i).get("NUM").toString());//获得每组数量
				for (Integer o : colRegion) {//遍历需要合并的列
				sheet.addMergedRegion(new Region(startRow, Short.parseShort(o.toString()), startRow+num-1, Short.parseShort(o.toString())));
			}
			startRow+=num;//根据每组数量跳到下一组第一行
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目基本情况汇总.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
	}
	
	private void expDxzfPro(HttpServletResponse response, List<HashMap> dailyList, List<String> listExpfields) {
		HashMap<Integer,Integer> colMap = new HashMap<Integer,Integer>();//存储每列的最大长度,用于设置列宽
		List<Integer> colRegion = new ArrayList<Integer>();//存储需要合并的列
		int colNum = 0;
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目基本情况汇总");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setFillForegroundColor((short) 22);
		style3.setFillPattern((short) 1);
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 1);
		style3.setBorderTop((short) 1);
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setWrapText(true);
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setWrapText(true);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor((short) 22);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);
		
		int m=0;
		int z=0;
		int n=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = null;
		if(listExpfields.contains("PROJECTBASEINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户名称".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目名称".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("保荐业务部负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "保荐业务部负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人职务");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系人职务".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系电话");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系电话".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("承揽部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "承揽部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("承做部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "承做部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目承揽人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目承揽人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交叉销售部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交叉销售部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交叉销售分成(元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交叉销售分成(元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("协议单位");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "协议单位".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("协议支出(%)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "协议支出(%)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("进展阶段");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "进展阶段".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("现有股东人数");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "现有股东人数".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("每股净资产(元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "每股净资产(元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计收入(万元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "预计收入(万元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计费用(万元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "预计费用(万元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属证监局");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属证监局".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属行业");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属行业".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目风险分析");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目风险分析".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("占用资金情况");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "占用资金情况".length());colNum++;
		}else{
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目名称".length());colNum++;
		}
		if(listExpfields.contains("PROJECTSTAGESINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("填报人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段资料");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTAGENCYINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("中介机构");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTPLACEMENTINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("定增对象");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("FINANCEDATA")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("财务数据");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTERRORRECORD")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目异常情况记录");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		colNum = 0;
		
		String projectnoBase = "";
		List<HashMap> projectErrorRecordList =  null;
		List lables = new ArrayList();
		lables.add("QUESTION");
		lables.add("CONTENT");
		Map params = new HashMap();
		String sql = "SELECT A.QUESTION,B.CONTENT FROM(SELECT B.QUESTION,B.TASKNO,B.ID,B.XMBH FROM BD_ZQB_XMWTFK B) A LEFT JOIN (SELECT C.CONTENT,C.QUESTIONNO FROM BD_ZQB_RETALK C) B ON A.ID=B.QUESTIONNO WHERE 1=1 AND A.XMBH=?";
		
		for (HashMap map : dailyList) {
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2= null;
			if(listExpfields.contains("PROJECTBASEINFO")){
				cell2 = row2.createCell((short) n++);
				String CUSTOMERNAME = map.get("CUSTOMERNAME")==null?"":map.get("CUSTOMERNAME").toString();
				cell2.setCellValue(CUSTOMERNAME);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CUSTOMERNAME.length()?CUSTOMERNAME.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String PROJECTNAME = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
				cell2.setCellValue(PROJECTNAME);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<PROJECTNAME.length()?PROJECTNAME.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String OWNER = map.get("OWNER")==null?"":map.get("OWNER").toString();
				cell2.setCellValue(OWNER);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<OWNER.length()?OWNER.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String MANAGER = map.get("MANAGER")==null?"":map.get("MANAGER").toString();
				cell2.setCellValue(MANAGER);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<MANAGER.length()?MANAGER.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String KHLXR = map.get("KHLXR")==null?"":map.get("KHLXR").toString();
				cell2.setCellValue(KHLXR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<KHLXR.length()?KHLXR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String LXRZW = map.get("LXRZW")==null?"":map.get("LXRZW").toString();
				cell2.setCellValue(LXRZW);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<LXRZW.length()?LXRZW.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String KHLXDH = map.get("KHLXDH")==null?"":map.get("KHLXDH").toString();
				cell2.setCellValue(KHLXDH);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<KHLXDH.length()?KHLXDH.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CLBM = map.get("CLBM")==null?"":map.get("CLBM").toString();
				cell2.setCellValue(CLBM);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CLBM.length()?CLBM.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CZBM = map.get("CZBM")==null?"":map.get("CZBM").toString();
				cell2.setCellValue(CZBM);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CZBM.length()?CZBM.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String JSYY = map.get("JSYY")==null?"":map.get("JSYY").toString();
				cell2.setCellValue(JSYY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<JSYY.length()?JSYY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String MEMO = map.get("MEMO")==null?"":map.get("MEMO").toString();
				cell2.setCellValue(MEMO);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<MEMO.length()?MEMO.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String GPFXSL = map.get("GPFXSL")==null?"":map.get("GPFXSL").toString();
				cell2.setCellValue(GPFXSL);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GPFXSL.length()?GPFXSL.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String FXMDCS = map.get("FXMDCS")==null?"":map.get("FXMDCS").toString();
				cell2.setCellValue(FXMDCS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<FXMDCS.length()?FXMDCS.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String MJZJZE = map.get("MJZJZE")==null?"":map.get("MJZJZE").toString();
				cell2.setCellValue(MJZJZE);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<MJZJZE.length()?MJZJZE.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String GPFXJZ = map.get("GPFXJZ")==null?"":map.get("GPFXJZ").toString();
				cell2.setCellValue(GPFXJZ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GPFXJZ.length()?GPFXJZ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XYGDRS = map.get("XYGDRS")==null?"":map.get("XYGDRS").toString();
				cell2.setCellValue(XYGDRS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XYGDRS.length()?XYGDRS.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String ZRFS = map.get("ZRFS")==null?"":map.get("ZRFS").toString();
				cell2.setCellValue(ZRFS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<ZRFS.length()?ZRFS.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String YJSR = map.get("YJSR")==null?"":map.get("YJSR").toString();
				cell2.setCellValue(YJSR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<YJSR.length()?YJSR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String YJFY = map.get("YJFY")==null?"":map.get("YJFY").toString();
				cell2.setCellValue(YJFY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<YJFY.length()?YJFY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SSXQ = map.get("SSXQ")==null?"":map.get("SSXQ").toString();
				cell2.setCellValue(SSXQ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SSXQ.length()?SSXQ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SSHY = map.get("SSHY")==null?"":map.get("SSHY").toString();
				cell2.setCellValue(SSHY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SSHY.length()?SSHY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMFXFX = map.get("XMFXFX")==null?"":map.get("XMFXFX").toString();
				cell2.setCellValue(XMFXFX);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMFXFX.length()?XMFXFX.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String ZYZJQK = map.get("ZYZJQK")==null?"":map.get("ZYZJQK").toString();
				cell2.setCellValue(ZYZJQK);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<ZYZJQK.length()?ZYZJQK.length():colMap.get(colNum));colNum++;
			}else{
				cell2 = row2.createCell((short) n++);
				String PROJECTNAME = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
				cell2.setCellValue(PROJECTNAME);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<PROJECTNAME.length()?PROJECTNAME.length():colMap.get(colNum));colNum++;
			}
			if(listExpfields.contains("PROJECTSTAGESINFO")){
				cell2 = row2.createCell((short) n++);
				String jdmc = map.get("JDMC")==null?"":map.get("JDMC").toString();
				cell2.setCellValue(jdmc);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<jdmc.length()?jdmc.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String cjrxm = map.get("CJRXM")==null?"":map.get("CJRXM").toString();
				cell2.setCellValue(cjrxm);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<cjrxm.length()?cjrxm.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String cjsj = map.get("CJSJ")==null?"":map.get("CJSJ").toString();
				cell2.setCellValue(cjsj);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<cjsj.length()?cjsj.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String fj = map.get("FJ")==null?"":map.get("FJ").toString();
				String fjName = "";
				String fileSrcName = "";
				List<FileUpload> fileObjs = FileUploadAPI.getInstance().getFileUploads(fj);
				for (int i = 0; i < fileObjs.size(); i++) {
					fileSrcName = fileObjs.get(i).getFileSrcName();
					if(i==0){
						fjName+=fileSrcName;
					}else{
						fjName+=("\n"+fileSrcName);
					}
					colMap.put(colNum, colMap.get(colNum)<fileSrcName.length()?fileSrcName.length():colMap.get(colNum));
				}
				colNum++;
				cell2.setCellValue(fjName);
				cell2.setCellStyle(style4);
				
			}
			if(listExpfields.contains("PROJECTAGENCYINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proAgencyList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_DGXMZJJG");
				StringBuffer agencyStr = new StringBuffer();
				for (HashMap agency : proAgencyList) {
					agencyStr.append("中介机构名称:").append(agency.get("ZJJGMC")).append("---->>").append("联系人:").append(agency.get("LXR")).append("---->>")
					.append("联系电话:").append(agency.get("LXDH")).append("---->>").append("联系邮箱:").append(agency.get("LXYX")).append("\n");
				}
				cell2.setCellValue(agencyStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("PROJECTPLACEMENTINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proAgencyList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_DGDZDX");
				StringBuffer agencyStr = new StringBuffer();
				for (HashMap agency : proAgencyList) {
					agencyStr.append("姓名/公司名:").append(agency.get("XMGSM")).append("---->>").append("股东类型:").append(agency.get("GDLX")).append("---->>")
					.append("获取渠道:").append(agency.get("HQQD")).append("\n");
				}
				cell2.setCellValue(agencyStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("FINANCEDATA")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proFinancEDataList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_BDGSCWQKJ");
				StringBuffer financEDataStr = new StringBuffer();
				for (HashMap financEData : proFinancEDataList) {
					financEDataStr.append("年份:").append(financEData.get("YEAR")).append("---->>").append("类型:").append(financEData.get("TYPE")).append("---->>")
					.append("总股本:").append(financEData.get("LRZE")).append("---->>").append("总资产:").append(financEData.get("ZZC")).append("---->>")
					.append("净资产:").append(financEData.get("JZC")).append("---->>").append("营业收入:").append(financEData.get("YYSR")).append("---->>")
					.append("净利润:").append(financEData.get("JLR")).append("---->>").append("经营活动现金流量净额:").append(financEData.get("XJLLJE")).append("\n");
				}
				cell2.setCellValue(financEDataStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
			}
			if(listExpfields.contains("PROJECTERRORRECORD")){
				String projectno = map.get("PROJECTNO")==null?"":map.get("PROJECTNO").toString();
				if(projectnoBase.equals("")||!projectno.equals(projectnoBase)){//判断是否第一条数据或是否下一个项目,避免多余的查询
					params.put(1, projectno);
					projectErrorRecordList =  com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
				}
				cell2 = row2.createCell((short) n++);
				StringBuffer projectErrorRecordStr = new StringBuffer();
				for (HashMap projectErrorRecord : projectErrorRecordList) {
					projectErrorRecordStr.append("主要问题:").append(projectErrorRecord.get("QUESTION")).append("---->>").append("解决方案:").append(projectErrorRecord.get("CONTENT")).append("\n");
				}
				cell2.setCellValue(projectErrorRecordStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
				
				projectnoBase = projectno;
			}
			n=0;
			colNum = 0;
		}
		
		for (int i = 0; i < colMap.size(); i++) {
			if(Short.parseShort(colMap.get(i).toString())>0){
				sheet.setColumnWidth((short)i, (short)Integer.parseInt(colMap.get(i).toString())*576);
			}
		}
		//Region(int left, int top, int right, int bottom) 
		
		int startRow = 1;//从第二行开始判断合并
		int num = 0;
		for (int i = 0; i < dailyList.size(); i+=num) {//i+=num  跳到下一组第一行
			num = Integer.parseInt(dailyList.get(i).get("NUM")==null?"1":dailyList.get(i).get("NUM").toString());//获得每组数量
				for (Integer o : colRegion) {//遍历需要合并的列
				sheet.addMergedRegion(new Region(startRow, Short.parseShort(o.toString()), startRow+num-1, Short.parseShort(o.toString())));
			}
			startRow+=num;//根据每组数量跳到下一组第一行
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目基本情况汇总.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
	}
	
	private void expBgczPro(HttpServletResponse response, List<HashMap> dailyList, List<String> listExpfields) {
		HashMap<Integer,Integer> colMap = new HashMap<Integer,Integer>();//存储每列的最大长度,用于设置列宽
		List<Integer> colRegion = new ArrayList<Integer>();//存储需要合并的列
		int colNum = 0;
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目基本情况汇总");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setFillForegroundColor((short) 22);
		style3.setFillPattern((short) 1);
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 1);
		style3.setBorderTop((short) 1);
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setWrapText(true);
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setWrapText(true);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor((short) 22);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);
		
		int m=0;
		int z=0;
		int n=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = null;
		if(listExpfields.contains("PROJECTBASEINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交易方（客户名称）");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交易方（客户名称）".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交易对手方");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交易对手方".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("收购方式");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "收购方式".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("承做部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "承做部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("是否构成关联交易");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "是否构成关联交易".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("控股股东、实际控制是否变更");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "控股股东、实际控制是否变更".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("是否构成重大资产重组");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "是否构成重大资产重组".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交易对手方在最近两年是否违法违规");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交易对手方在最近两年是否违法违规".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("保荐业务部负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "保荐业务部负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人职务");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系人职务".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系电话");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系电话".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("自愿锁定");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "自愿锁定".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("停牌日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "停牌日期".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("进展阶段");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "进展阶段".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计交易价格(万元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "预计交易价格(万元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计支付方式");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "预计支付方式".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计配套募集资金(万元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "预计配套募集资金(万元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属行业");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属行业".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("关联关系描述完整");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "关联关系描述完整".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("控制权变更");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "控制权变更".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("同业竞争变更");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "同业竞争变更".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("触及权益变动");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "触及权益变动".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("触及主营业务变更");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "触及主营业务变更".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属证监局");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属证监局".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("估值条款");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "估值条款".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("对赌业绩承诺条款");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "对赌业绩承诺条款".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目风险");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目风险".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("备注");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "备注".length());colNum++;
		}else{
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交易对手方");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交易对手方".length());colNum++;
		}
		if(listExpfields.contains("PROJECTSTAGESINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("填报人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段资料");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTJOINERSINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目参与人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTAGENCYINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("中介机构");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("FINANCEDATA")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交易方财务情况");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("FINANCEDATAOPPONENT")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交易方对手方财务情况");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTERRORRECORD")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目异常情况记录");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		colNum = 0;
		
		String projectnoBase = "";
		List<HashMap> projectErrorRecordList =  null;
		List lables = new ArrayList();
		lables.add("QUESTION");
		lables.add("CONTENT");
		Map params = new HashMap();
		String sql = "SELECT A.QUESTION,B.CONTENT FROM(SELECT B.QUESTION,B.TASKNO,B.ID,B.XMBH FROM BD_ZQB_XMWTFK B) A LEFT JOIN (SELECT C.CONTENT,C.QUESTIONNO FROM BD_ZQB_RETALK C) B ON A.ID=B.QUESTIONNO WHERE 1=1 AND A.XMBH=?";
		
		for (HashMap map : dailyList) {
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2= null;
			if(listExpfields.contains("PROJECTBASEINFO")){
				cell2 = row2.createCell((short) n++);
				String JYF = map.get("JYF")==null?"":map.get("JYF").toString();
				cell2.setCellValue(JYF);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<JYF.length()?JYF.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String JYDSF = map.get("JYDSF")==null?"":map.get("JYDSF").toString();
				cell2.setCellValue(JYDSF);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<JYDSF.length()?JYDSF.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SGFS = map.get("SGFS")==null?"":map.get("SGFS").toString();
				cell2.setCellValue(SGFS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SGFS.length()?SGFS.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CZBM = map.get("CZBM")==null?"":map.get("CZBM").toString();
				cell2.setCellValue(CZBM);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CZBM.length()?CZBM.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SFGLJY = map.get("SFGLJY")==null?"":map.get("SFGLJY").toString();
				cell2.setCellValue(SFGLJY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SFGLJY.length()?SFGLJY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String GDYKZRBG = map.get("GDYKZRBG")==null?"":map.get("GDYKZRBG").toString();
				cell2.setCellValue(GDYKZRBG);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GDYKZRBG.length()?GDYKZRBG.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SFCZ = map.get("SFCZ")==null?"":map.get("SFCZ").toString();
				cell2.setCellValue(SFCZ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SFCZ.length()?SFCZ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SFWG = map.get("SFWG")==null?"":map.get("SFWG").toString();
				cell2.setCellValue(SFWG);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SFWG.length()?SFWG.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String OWNER = map.get("OWNER")==null?"":map.get("OWNER").toString();
				cell2.setCellValue(OWNER);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<OWNER.length()?OWNER.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String MANAGER = map.get("MANAGER")==null?"":map.get("MANAGER").toString();
				cell2.setCellValue(MANAGER);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<MANAGER.length()?MANAGER.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String KHLXR = map.get("KHLXR")==null?"":map.get("KHLXR").toString();
				cell2.setCellValue(KHLXR);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<KHLXR.length()?KHLXR.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String LXRZW = map.get("LXRZW")==null?"":map.get("LXRZW").toString();
				cell2.setCellValue(LXRZW);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<LXRZW.length()?LXRZW.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String KHLXDH = map.get("KHLXDH")==null?"":map.get("KHLXDH").toString();
				cell2.setCellValue(KHLXDH);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<KHLXDH.length()?KHLXDH.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String ZYSD = map.get("ZYSD")==null?"":map.get("ZYSD").toString();
				cell2.setCellValue(ZYSD);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<ZYSD.length()?ZYSD.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String TPRQ = map.get("TPRQ")==null?"":map.get("TPRQ").toString();
				cell2.setCellValue(TPRQ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<TPRQ.length()?TPRQ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMJD = map.get("XMJD")==null?"":map.get("XMJD").toString();
				cell2.setCellValue(XMJD);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMJD.length()?XMJD.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String JYJGA = map.get("JYJGA")==null?"0":map.get("JYJGA").toString();
				String JYJGB = map.get("JYJGB")==null?"0":map.get("JYJGB").toString();
				String JYJG = JYJGA+"到"+JYJGB;
				cell2.setCellValue(JYJG);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<JYJG.length()?JYJG.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String YJZFFS = map.get("YJZFFS")==null?"":map.get("YJZFFS").toString();
				cell2.setCellValue(YJZFFS);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<YJZFFS.length()?YJZFFS.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String PTMJZJ = map.get("PTMJZJ")==null?"":map.get("PTMJZJ").toString();
				cell2.setCellValue(PTMJZJ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<PTMJZJ.length()?PTMJZJ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SSHY = map.get("SSHY")==null?"":map.get("SSHY").toString();
				cell2.setCellValue(SSHY);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SSHY.length()?SSHY.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String GLGXMSWZ = map.get("GLGXMSWZ")==null?"":map.get("GLGXMSWZ").toString();
				cell2.setCellValue(GLGXMSWZ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GLGXMSWZ.length()?GLGXMSWZ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String KZQBG = map.get("KZQBG")==null?"":map.get("KZQBG").toString();
				cell2.setCellValue(KZQBG);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<KZQBG.length()?KZQBG.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String TYJZBG = map.get("TYJZBG")==null?"":map.get("TYJZBG").toString();
				cell2.setCellValue(TYJZBG);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<TYJZBG.length()?TYJZBG.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CJQYBD = map.get("CJQYBD")==null?"":map.get("CJQYBD").toString();
				cell2.setCellValue(CJQYBD);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CJQYBD.length()?CJQYBD.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CJZYYWBG = map.get("CJZYYWBG")==null?"":map.get("CJZYYWBG").toString();
				cell2.setCellValue(CJZYYWBG);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CJZYYWBG.length()?CJZYYWBG.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String SSXQ = map.get("SSXQ")==null?"":map.get("SSXQ").toString();
				cell2.setCellValue(SSXQ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<SSXQ.length()?SSXQ.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String GZTK = map.get("GZTK")==null?"":map.get("GZTK").toString();
				cell2.setCellValue(GZTK);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<GZTK.length()?GZTK.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String CNTK = map.get("CNTK")==null?"":map.get("CNTK").toString();
				cell2.setCellValue(CNTK);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<CNTK.length()?CNTK.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String XMFX = map.get("XMFX")==null?"":map.get("XMFX").toString();
				cell2.setCellValue(XMFX);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<XMFX.length()?XMFX.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String BZ = map.get("BZ")==null?"":map.get("BZ").toString();
				cell2.setCellValue(BZ);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<BZ.length()?BZ.length():colMap.get(colNum));colNum++;
			}else{
				cell2 = row2.createCell((short) n++);
				String JYDSF = map.get("JYDSF")==null?"":map.get("JYDSF").toString();
				cell2.setCellValue(JYDSF);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<JYDSF.length()?JYDSF.length():colMap.get(colNum));colNum++;
			}
			if(listExpfields.contains("PROJECTSTAGESINFO")){
				cell2 = row2.createCell((short) n++);
				String jdmc = map.get("JDMC")==null?"":map.get("JDMC").toString();
				cell2.setCellValue(jdmc);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<jdmc.length()?jdmc.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String tbr = map.get("TBR")==null?"":map.get("TBR").toString();
				cell2.setCellValue(tbr);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<tbr.length()?tbr.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String tbsj = map.get("TBSJ")==null?"":map.get("TBSJ").toString();
				cell2.setCellValue(tbsj);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<tbsj.length()?tbsj.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String fj = map.get("FJ")==null?"":map.get("FJ").toString();
				String fjName = "";
				String fileSrcName = "";
				List<FileUpload> fileObjs = FileUploadAPI.getInstance().getFileUploads(fj);
				for (int i = 0; i < fileObjs.size(); i++) {
					fileSrcName = fileObjs.get(i).getFileSrcName();
					if(i==0){
						fjName+=fileSrcName;
					}else{
						fjName+=("\n"+fileSrcName);
					}
					colMap.put(colNum, colMap.get(colNum)<fileSrcName.length()?fileSrcName.length():colMap.get(colNum));
				}
				colNum++;
				cell2.setCellValue(fjName);
				cell2.setCellStyle(style4);
				
			}
			if(listExpfields.contains("PROJECTJOINERSINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proMemberList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_DGXMCYLB");
				StringBuffer memberStr = new StringBuffer();
				for (HashMap member : proMemberList) {
					memberStr.append("姓名:").append(member.get("NAME")).append("---->>").append("所属部门:").append(member.get("DEPARTMENTNAME")).append("---->>")
					.append("职务:").append(member.get("POSITION")).append("---->>").append("联系电话:").append(member.get("TEL")).append("---->>")
					.append("手机:").append(member.get("PHONE")).append("---->>").append("邮箱:").append(member.get("EMAIL")).append("\n");
				}
				cell2.setCellValue(memberStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("PROJECTAGENCYINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proAgencyList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_DGXMZJJG");
				StringBuffer agencyStr = new StringBuffer();
				for (HashMap agency : proAgencyList) {
					agencyStr.append("中介机构名称:").append(agency.get("ZJJGMC")).append("---->>").append("联系人:").append(agency.get("LXR")).append("---->>")
					.append("联系电话:").append(agency.get("LXDH")).append("---->>").append("联系邮箱:").append(agency.get("LXYX")).append("\n");
				}
				cell2.setCellValue(agencyStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("FINANCEDATA")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proFinancEDataList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_BDGSCWQKJ");
				StringBuffer financEDataStr = new StringBuffer();
				for (HashMap financEData : proFinancEDataList) {
					financEDataStr.append("年份:").append(financEData.get("YEAR")).append("---->>").append("类型:").append(financEData.get("TYPE")).append("---->>")
					.append("总股本:").append(financEData.get("LRZE")).append("---->>").append("总资产:").append(financEData.get("ZZC")).append("---->>")
					.append("净资产:").append(financEData.get("JZC")).append("---->>").append("营业收入:").append(financEData.get("YYSR")).append("---->>")
					.append("净利润:").append(financEData.get("JLR")).append("---->>").append("经营活动现金流量净额:").append(financEData.get("XJLLJE")).append("\n");
				}
				cell2.setCellValue(financEDataStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
			}
			if(listExpfields.contains("FINANCEDATAOPPONENT")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proFinancEDataList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_BDGSCWQK");
				StringBuffer financEDataStr = new StringBuffer();
				for (HashMap financEData : proFinancEDataList) {
					financEDataStr.append("年份:").append(financEData.get("YEAR")).append("---->>").append("类型:").append(financEData.get("TYPE")).append("---->>")
					.append("总股本:").append(financEData.get("LRZE")).append("---->>").append("总资产:").append(financEData.get("ZZC")).append("---->>")
					.append("净资产:").append(financEData.get("JZC")).append("---->>").append("营业收入:").append(financEData.get("YYSR")).append("---->>")
					.append("净利润:").append(financEData.get("JLR")).append("---->>").append("经营活动现金流量净额:").append(financEData.get("XJLLJE")).append("\n");
				}
				cell2.setCellValue(financEDataStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
			}
			if(listExpfields.contains("PROJECTERRORRECORD")){
				String projectno = map.get("XMBH")==null?"":map.get("XMBH").toString();
				if(projectnoBase.equals("")||!projectno.equals(projectnoBase)){//判断是否第一条数据或是否下一个项目,避免多余的查询
					params.put(1, projectno);
					projectErrorRecordList =  com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
				}
				cell2 = row2.createCell((short) n++);
				StringBuffer projectErrorRecordStr = new StringBuffer();
				for (HashMap projectErrorRecord : projectErrorRecordList) {
					projectErrorRecordStr.append("主要问题:").append(projectErrorRecord.get("QUESTION")).append("---->>").append("解决方案:").append(projectErrorRecord.get("CONTENT")).append("\n");
				}
				cell2.setCellValue(projectErrorRecordStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
				
				projectnoBase = projectno;
			}
			n=0;
			colNum = 0;
		}
		
		for (int i = 0; i < colMap.size(); i++) {
			if(Short.parseShort(colMap.get(i).toString())>0){
				sheet.setColumnWidth((short)i, (short)Integer.parseInt(colMap.get(i).toString())*576);
			}
		}
		//Region(int left, int top, int right, int bottom) 
		
		int startRow = 1;//从第二行开始判断合并
		int num = 0;
		for (int i = 0; i < dailyList.size(); i+=num) {//i+=num  跳到下一组第一行
			num = Integer.parseInt(dailyList.get(i).get("NUM")==null?"1":dailyList.get(i).get("NUM").toString());//获得每组数量
				for (Integer o : colRegion) {//遍历需要合并的列
				sheet.addMergedRegion(new Region(startRow, Short.parseShort(o.toString()), startRow+num-1, Short.parseShort(o.toString())));
			}
			startRow+=num;//根据每组数量跳到下一组第一行
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目基本情况汇总.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
	}
	
	private void expOtherPro(HttpServletResponse response, List<HashMap> dailyList, List<String> listExpfields) {
		HashMap<Integer,Integer> colMap = new HashMap<Integer,Integer>();//存储每列的最大长度,用于设置列宽
		List<Integer> colRegion = new ArrayList<Integer>();//存储需要合并的列
		int colNum = 0;
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目基本情况汇总");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setFillForegroundColor((short) 22);
		style3.setFillPattern((short) 1);
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 1);
		style3.setBorderTop((short) 1);
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setWrapText(true);
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setWrapText(true);
		HSSFCellStyle style6 = wb.createCellStyle();
		style6.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style6.setFillForegroundColor((short) 22);
		style6.setFillPattern((short) 1);
		style6.setBorderBottom((short) 1);
		style6.setBorderLeft((short) 1);
		style6.setBorderRight((short) 1);
		style6.setBorderTop((short) 1);
		style6.setFillForegroundColor(HSSFColor.WHITE.index);
		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setFillPattern((short) 1);
		style5.setBorderBottom((short) 1);
		style5.setBorderLeft((short) 1);
		style5.setBorderRight((short) 1);
		style5.setBorderTop((short) 1);
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//style5.setFillForegroundColor(HSSFColor.LIME.index);
		style5.setFillForegroundColor((short) 22);
		//style5.setFillForegroundColor(HSSFColor.WHITE.index);
		style5.setFillPattern((short) 1);
		
		int m=0;
		int z=0;
		int n=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = null;
		if(listExpfields.contains("PROJECTBASEINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户名称".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目名称".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("保荐业务部负责人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "保荐业务部负责人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目类型");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目类型".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目立项时间");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目立项时间".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("承做部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "承做部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("合同金额(万元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "合同金额(万元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("公司角色");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "公司角色".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系人职务");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系人职务".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("客户联系电话");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "客户联系电话".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("进展阶段");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "进展阶段".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计收入");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "预计收入".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("预计费用");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "预计费用".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属证监局");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属证监局".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属行业");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "所属行业".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目承揽人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目承揽人".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交叉销售部门");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交叉销售部门".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("交叉销售分成(元)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "交叉销售分成(元)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("协议单位");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "协议单位".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("协议支出(%)");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "协议支出(%)".length());colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("承揽条件");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "承揽条件".length());colNum++;
		}else{
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, "项目名称".length());colNum++;
		}
		if(listExpfields.contains("PROJECTSTAGESINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段名称");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("填报人");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("日期");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("阶段资料");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTMEMBERINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目成员列表");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTAGENCYINFO")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目中介机构");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("FINANCEDATA")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("财务数据");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		if(listExpfields.contains("PROJECTERRORRECORD")){
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("项目异常情况记录");
			cell1.setCellStyle(style5);
			colMap.put(colNum, 0);colNum++;
		}
		colNum = 0;
		
		String projectnoBase = "";
		List<HashMap> projectErrorRecordList =  null;
		List lables = new ArrayList();
		lables.add("QUESTION");
		lables.add("CONTENT");
		Map params = new HashMap();
		String sql = "SELECT A.QUESTION,B.CONTENT FROM(SELECT B.QUESTION,B.TASKNO,B.ID,B.XMBH FROM BD_ZQB_XMWTFK B) A LEFT JOIN (SELECT C.CONTENT,C.QUESTIONNO FROM BD_ZQB_RETALK C) B ON A.ID=B.QUESTIONNO WHERE 1=1 AND A.XMBH=?";
		
		for (HashMap map : dailyList) {
			HSSFRow row2 = sheet.createRow((int) m++);
			HSSFCell cell2= null;
			if(listExpfields.contains("PROJECTBASEINFO")){
				cell2 = row2.createCell((short) n++);
				String customername = map.get("CUSTOMERNAME")==null?"":map.get("CUSTOMERNAME").toString();
				cell2.setCellValue(customername);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<customername.length()?customername.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String projectname = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
				cell2.setCellValue(projectname);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<projectname.length()?projectname.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String manager = map.get("MANAGER")==null?"":map.get("MANAGER").toString();
				cell2.setCellValue(manager);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<manager.length()?manager.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String owner = map.get("OWNER")==null?"":map.get("OWNER").toString();
				cell2.setCellValue(owner);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<owner.length()?owner.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String a08 = map.get("A08")==null?"":map.get("A08").toString();
				cell2.setCellValue(a08);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<a08.length()?a08.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String startdate = map.get("STARTDATE")==null?"":map.get("STARTDATE").toString();
				cell2.setCellValue(startdate);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<startdate.length()?startdate.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String sssyb = map.get("SSSYB")==null?"":map.get("SSSYB").toString();
				cell2.setCellValue(sssyb);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<sssyb.length()?sssyb.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String htje = map.get("HTJE")==null?"":map.get("HTJE").toString();
				cell2.setCellValue(htje);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<htje.length()?htje.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String a06 = map.get("A06")==null?"":map.get("A06").toString();
				cell2.setCellValue(a06);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<a06.length()?a06.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String khlxr = map.get("KHLXR")==null?"":map.get("KHLXR").toString();
				cell2.setCellValue(khlxr);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<khlxr.length()?khlxr.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String lxrzw = map.get("LXRZW")==null?"":map.get("LXRZW").toString();
				cell2.setCellValue(lxrzw);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<lxrzw.length()?lxrzw.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String khlxdh = map.get("KHLXDH")==null?"":map.get("KHLXDH").toString();
				cell2.setCellValue(khlxdh);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<khlxdh.length()?khlxdh.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String xmjd = map.get("XMJD")==null?"":map.get("XMJD").toString();
				cell2.setCellValue(xmjd);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<xmjd.length()?xmjd.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String extend1 = map.get("EXTEND1")==null?"":map.get("EXTEND1").toString();
				cell2.setCellValue(extend1);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<extend1.length()?extend1.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String extend2 = map.get("EXTEND2")==null?"":map.get("EXTEND2").toString();
				cell2.setCellValue(extend2);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<extend2.length()?extend2.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String ssxq = map.get("SSXQ")==null?"":map.get("SSXQ").toString();
				cell2.setCellValue(ssxq);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<ssxq.length()?ssxq.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String sshy = map.get("SSHY")==null?"":map.get("SSHY").toString();
				cell2.setCellValue(sshy);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<sshy.length()?sshy.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String zclr = map.get("ZCLR")==null?"":map.get("ZCLR").toString();
				cell2.setCellValue(zclr);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<zclr.length()?zclr.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String xmls = map.get("XMLS")==null?"":map.get("XMLS").toString();
				cell2.setCellValue(xmls);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<xmls.length()?xmls.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String yjzxynjlr = map.get("YJZXYNJLR")==null?"":map.get("YJZXYNJLR").toString();
				cell2.setCellValue(yjzxynjlr);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<yjzxynjlr.length()?yjzxynjlr.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String gzyjdz = map.get("GZYJDZ")==null?"":map.get("GZYJDZ").toString();
				cell2.setCellValue(gzyjdz);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<gzyjdz.length()?gzyjdz.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String fxpgfs = map.get("FXPGFS")==null?"":map.get("FXPGFS").toString();
				cell2.setCellValue(fxpgfs);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<fxpgfs.length()?fxpgfs.length():colMap.get(colNum));colNum++;
				
				cell2 = row2.createCell((short) n++);
				String xmbz = map.get("XMBZ")==null?"":map.get("XMBZ").toString();
				cell2.setCellValue(xmbz);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<xmbz.length()?xmbz.length():colMap.get(colNum));colNum++;
			}else{
				cell2 = row2.createCell((short) n++);
				String projectname = map.get("PROJECTNAME")==null?"":map.get("PROJECTNAME").toString();
				cell2.setCellValue(projectname);
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, colMap.get(colNum)<projectname.length()?projectname.length():colMap.get(colNum));colNum++;
			}
			if(listExpfields.contains("PROJECTSTAGESINFO")){
				cell2 = row2.createCell((short) n++);
				String jdmc = map.get("JDMC")==null?"":map.get("JDMC").toString();
				cell2.setCellValue(jdmc);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<jdmc.length()?jdmc.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String tbr = map.get("TBR")==null?"":map.get("TBR").toString();
				cell2.setCellValue(tbr);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<tbr.length()?tbr.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String cjsj = map.get("CJSJ")==null?"":map.get("CJSJ").toString();
				cell2.setCellValue(cjsj);
				cell2.setCellStyle(style4);
				colMap.put(colNum, colMap.get(colNum)<cjsj.length()?cjsj.length():colMap.get(colNum));colNum++;
				cell2 = row2.createCell((short) n++);
				String fj = map.get("FJ")==null?"":map.get("FJ").toString();
				String fjName = "";
				String fileSrcName = "";
				List<FileUpload> fileObjs = FileUploadAPI.getInstance().getFileUploads(fj);
				for (int i = 0; i < fileObjs.size(); i++) {
					fileSrcName = fileObjs.get(i).getFileSrcName();
					if(i==0){
						fjName+=fileSrcName;
					}else{
						fjName+=("\n"+fileSrcName);
					}
					colMap.put(colNum, colMap.get(colNum)<fileSrcName.length()?fileSrcName.length():colMap.get(colNum));
				}
				colNum++;
				cell2.setCellValue(fjName);
				cell2.setCellStyle(style4);
				
			}
			if(listExpfields.contains("PROJECTMEMBERINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proMemberList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_IPOXMCYR");
				StringBuffer memberStr = new StringBuffer();
				for (HashMap member : proMemberList) {
					memberStr.append("姓名:").append(member.get("NAME")).append("---->>").append("所属部门:").append(member.get("DEPARTMENTNAME")).append("---->>")
					.append("职务:").append(member.get("POSITION")).append("---->>").append("联系电话:").append(member.get("TEL")).append("---->>")
					.append("手机:").append(member.get("PHONE")).append("---->>").append("邮箱:").append(member.get("EMAIL")).append("\n");
				}
				cell2.setCellValue(memberStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("PROJECTAGENCYINFO")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proAgencyList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_IPOXMZJJG");
				StringBuffer agencyStr = new StringBuffer();
				for (HashMap agency : proAgencyList) {
					agencyStr.append("中介机构名称:").append(agency.get("ZJJGMC")).append("---->>").append("联系人:").append(agency.get("LXR")).append("---->>")
					.append("联系电话:").append(agency.get("LXDH")).append("---->>").append("联系邮箱:").append(agency.get("LXYX")).append("\n");
				}
				cell2.setCellValue(agencyStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 50);colNum++;
			}
			if(listExpfields.contains("FINANCEDATA")){
				cell2 = row2.createCell((short) n++);
				String instanceid = map.get("INSTANCEID")==null?"":map.get("INSTANCEID").toString();
				List<HashMap> proFinancEDataList = DemAPI.getInstance().getFromSubData(Long.parseLong(instanceid), "SUBFORM_BDGSCWQKJ");
				StringBuffer financEDataStr = new StringBuffer();
				for (HashMap financEData : proFinancEDataList) {
					financEDataStr.append("年份:").append(financEData.get("YEAR")).append("---->>").append("类型:").append(financEData.get("TYPE")).append("---->>")
					.append("总股本:").append(financEData.get("LRZE")).append("---->>").append("总资产:").append(financEData.get("ZZC")).append("---->>")
					.append("净资产:").append(financEData.get("JZC")).append("---->>").append("营业收入:").append(financEData.get("YYSR")).append("---->>")
					.append("净利润:").append(financEData.get("JLR")).append("---->>").append("经营活动现金流量净额:").append(financEData.get("XJLLJE")).append("\n");
				}
				cell2.setCellValue(financEDataStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
			}
			if(listExpfields.contains("PROJECTERRORRECORD")){
				String projectno = map.get("PROJECTNO")==null?"":map.get("PROJECTNO").toString();
				if(projectnoBase.equals("")||!projectno.equals(projectnoBase)){//判断是否第一条数据或是否下一个项目,避免多余的查询
					params.put(1, projectno);
					projectErrorRecordList =  com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
				}
				cell2 = row2.createCell((short) n++);
				StringBuffer projectErrorRecordStr = new StringBuffer();
				for (HashMap projectErrorRecord : projectErrorRecordList) {
					projectErrorRecordStr.append("主要问题:").append(projectErrorRecord.get("QUESTION")).append("---->>").append("解决方案:").append(projectErrorRecord.get("CONTENT")).append("\n");
				}
				cell2.setCellValue(projectErrorRecordStr.toString());
				cell2.setCellStyle(style4);
				colRegion.add(colNum);
				colMap.put(colNum, 60);colNum++;
				
				projectnoBase = projectno;
			}
			n=0;
			colNum = 0;
		}
		
		for (int i = 0; i < colMap.size(); i++) {
			if(Short.parseShort(colMap.get(i).toString())>0){
				sheet.setColumnWidth((short)i, (short)Integer.parseInt(colMap.get(i).toString())*576);
			}
		}
		//Region(int left, int top, int right, int bottom) 
		
		int startRow = 1;//从第二行开始判断合并
		int num = 0;
		for (int i = 0; i < dailyList.size(); i+=num) {//i+=num  跳到下一组第一行
			num = Integer.parseInt(dailyList.get(i).get("NUM")==null?"1":dailyList.get(i).get("NUM").toString());//获得每组数量
				for (Integer o : colRegion) {//遍历需要合并的列
				sheet.addMergedRegion(new Region(startRow, Short.parseShort(o.toString()), startRow+num-1, Short.parseShort(o.toString())));
			}
			startRow+=num;//根据每组数量跳到下一组第一行
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目基本情况汇总.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			if (out1 != null) {
				try {
					out1.flush();
					out1.close();
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
	}
	
	public String getJdxx(String projectno){
		StringBuffer content = new StringBuffer();
   content.append("<table   id=\"iform_grid1\" class=\"ui-jqgrid-htable ke-zeroborder\" role=\"grid\" aria- labelledby=\"gbox_subformSUBFORM_BGSRZXX\" style=\"width:850px;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
				+ " <thead>           "
				+ " 	<tr class=\"ui-jqgrid-labels\" role=\"rowheader\">       "
				
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_rn\" role=\"columnheader\" style=\"width:5px;\">                               "
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_rn\">          "
				+ " 				<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "					</span>"
				+ " 			</div><br/>"
				+ " 		</th>"
				
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZLX\" role=\"columnheader\" style=\"width:60px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "				<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZLX\">"
				+ "					阶段名称"
				+ "					<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "					</span>"
				+ " 			</div>"
				+ "			</th>"
				
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_ZW\" role=\"columnheader\" style=\"width:60px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "				<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_ZW\">"
				+ "					填报人"
				+ "					<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "					</span>"
				+ " 			</div>"
				+ "			</th>"
				
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\" style=\"width:60px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "				<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">"
				+ "					日期"
				+ "					<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "					</span>"
				+ " 			</div>"
				+ "			</th>"
				
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\" style=\"width:170px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "				<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">"
				+ "					上传资料"
				+ "					<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span></span>                     "
				+ " 			</div>"
				+ "			</th>"
				
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZJSRQ\" role=\"columnheader\" style=\"width:55px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "				<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZJSRQ\">"
				+ "					操作"
				+ "					<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "						</span>"
				+ " 			</div>"
				+ "			</th>"
				
				+ " 	</tr>"
				+ " </thead>"
				+ "</table>");
		
   content.append("<div style=\"height: 100%; width: 850px;\" class=\"ui-jqgrid-bdiv\">"
				+ "		<div style=\"position:relative;\">"
				+ "		<div>"
				+ "		</div>"
				+ "			<table  id=\"iform_grid\" style=\"width: 850px;\" class=\"ui-jqgrid-btable\" aria-labelledby=\"gbox_subformSUBFORM_SXCYRY\" aria-multiselectable=\"true\" role=\"grid\" tabindex=\"1\" id=\"subformSUBFORM_SXCYRY\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
				+ "				<tbody>");
		
		
		List<HashMap> ldxxlist = dongGuanZqbProjectManageDAO.getJdxx(projectno);
		String groupid = "";
		String jdmc = "";
		String tbr = "";
		String zlmb = "";
		String jdzl = "";
		String instanceid = "";
		String tbsj = "";
		String icon = "";
		String formid = "";
		String demid = "";
		for (HashMap data : ldxxlist) {
			groupid = data.get("GROUPID")==null?"":data.get("GROUPID").toString();
			jdmc = data.get("JDMC")==null?"":data.get("JDMC").toString();
			tbr = data.get("TBR")==null?"":data.get("TBR").toString();
			zlmb = data.get("ZLMB")==null?"":data.get("ZLMB").toString();
			jdzl = data.get("JDZL")==null?"":data.get("JDZL").toString();
			instanceid = data.get("INSTANCEID")==null?"":data.get("INSTANCEID").toString();
			tbsj = data.get("TBSJ")==null?"":data.get("TBSJ").toString();
			if(jdmc.equals("项目立项")){
				formid = this.getConfigUUID("dglxformid");
				demid = this.getConfigUUID("dglxdemid");
			}else if(jdmc.equals("股改")){
				formid = this.getConfigUUID("dgggformid");
				demid = this.getConfigUUID("dgggdemid");
			}else if(jdmc.equals("申报")){
				formid = this.getConfigUUID("dgsbformid");
				demid = this.getConfigUUID("dgsbdemid");
			}else if(jdmc.equals("反馈")){
				formid = this.getConfigUUID("dgfkformid");
				demid = this.getConfigUUID("dgfkdemid");
			}else if(jdmc.equals("同意挂牌")){
				formid = this.getConfigUUID("dgtyformid");
				demid = this.getConfigUUID("dgtydemid");
			}else if(jdmc.equals("正式挂牌")){
				formid = this.getConfigUUID("dgzsformid");
				demid = this.getConfigUUID("dgzsdemid");
			}else if(jdmc.equals("项目终止")){
				formid = this.getConfigUUID("dgzzformid");
				demid = this.getConfigUUID("dgzzdemid");
			}else if(jdmc.equals("项目组工作记录")){
				formid = this.getConfigUUID("dgxmzjuformid");
				demid = this.getConfigUUID("dgxmzjudemid");
			}else if(jdmc.equals("其他文件")){
				formid = this.getConfigUUID("dgqtwjformid");
				demid = this.getConfigUUID("dgqtwjdemid");
			}
			content.append("		<tr aria-selected=\"true\" id=\"29\" tabindex=\"-1\" class=\"ui-widget-content jqgrow ui-row-ltr \">");
			content.append("			<td role=\"gridcell\" style=\"width:5px;\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_\"></td>");
			content.append("			<td role=\"gridcell\" style=\"width:60px;\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_JDMC\">").append(jdmc).append("</td>");
			//填报人
			content.append("			<td role=\"gridcell\" style=\"width:60px;\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\">").append(tbr).append("</td>");
			//资料模板
			content.append("			<td role=\"gridcell\" style=\"width:60px;\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_ZLMB\">");
			/*if(!zlmb.equals("")){
				List<FileUpload> list = FileUploadAPI.getInstance().getFileUploads(zlmb);
				for (int i = 0; i < list.size(); i++) {
					icon = FileType.getFileIcon(list.get(i).getFileSrcName());
							content.append("<a title=\"").append(list.get(i).getFileSrcName()).append("\" target=\"_blank\" href=\"uploadifyDownload.action?fileUUID=").append(list.get(i).getFileId()).append("\">").append("<img src=\"").append(icon).append("\" style=\"margin:3px\">").append(list.get(i).getFileSrcName()).append("</a>");
					if(i<(list.size()-1)){
						content.append("<br>");
					}
				}
			}*/
			content.append(tbsj);
			content.append("			</td>");
			//阶段资料
			content.append("			<td role=\"gridcell\" style=\"width:170px;\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_ZL\">");
			if(!jdzl.equals("")){
				List<FileUpload> list = FileUploadAPI.getInstance().getFileUploads(jdzl);
				for (int i = 0; i < list.size(); i++) {
					icon = FileType.getFileIcon(list.get(i).getFileSrcName());
							content.append("<a title=\"").append(list.get(i).getFileSrcName()).append("\" target=\"_blank\" href=\"uploadifyDownload.action?fileUUID=").append(list.get(i).getFileId()).append("\">").append("<img src=\"").append(icon).append("\" style=\"margin:3px\">").append(list.get(i).getFileSrcName()).append("</a>");
					if(i<(list.size()-1)){
						content.append("<br>");
					}
				}
			}
			content.append("</td>");
			//操作
			content.append("			<td role=\"gridcell\" style=\"width:55px;\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_ACTION\">");
			if(instanceid.equals("")){
							content.append("<a href=\"javascript:addItem(").append(groupid).append(",").append(formid).append(",").append(demid).append(",'").append(jdmc).append("')\">上传</a>");
			}else{
							content.append("<a href=\"javascript:loadItem(").append(instanceid).append(",").append(groupid).append(",").append(formid).append(",").append(demid).append(")\">编辑</a>");
			}
			content.append("			</td>");
			
			content.append("		</tr>");
		}
		
		
   content.append("				</tbody>"
   		+ "					</table>"
   		+ "				</div>"
   		+ "				</div>");
		return content.toString().replace("rowspan=\"0\"", "");
	}
	
	public String substring(String text, int length, String encode)
			throws UnsupportedEncodingException {
		if (text == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int currentLength = 0;
		for (char c : text.toCharArray()) {
			currentLength += String.valueOf(c).getBytes(encode).length;
			if (currentLength <= length) {
				sb.append(c);
			} else {
				break;
			}
		}
		return sb.toString();

	}
	public String loadPj(String projectNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT INFO.JDMC TASK_NAME,PJ.PJR,PJ.PJSJ,PJ.PJSM,INFO.ID GROUPID,BIND2.INSTANCEID PJINSID,PJ.ID,PJ.LDPJ,ORG.USERNAME FROM BD_ZQB_XMRWPJB PJ");
		sql.append(" INNER JOIN BD_ZQB_TYXM_INFO INFO ON PJ.GROUPID=INFO.ID");
		sql.append(" INNER JOIN (SELECT BIND.INSTANCEID,BIND.DATAID FROM SYS_ENGINE_IFORM IFORM INNER JOIN SYS_DEM_ENGINE ENGINE ON IFORM.ID=ENGINE.FORMID AND ENGINE.TITLE='阶段评价' INNER JOIN SYS_ENGINE_FORM_BIND BIND ON IFORM.ID=BIND.FORMID AND IFORM.METADATAID=BIND.METADATAID)");
		sql.append(" BIND2 ON PJ.ID=BIND2.DATAID");
		sql.append(" LEFT JOIN ORGUSER ORG ON PJ.PJR=ORG.USERID WHERE PJ.PROJECTNO=? AND INFO.XMLX='东莞-推荐挂牌' AND INFO.STATE=1 ORDER BY INFO.ID,PJ.ID");
		StringBuffer content = new StringBuffer();
		content.append("<table class=\"ui-jqgrid-htable ke-zeroborder\" role=\"grid\" aria- labelledby=\"gbox_subformSUBFORM_BGSRZXX\" style=\"width:850px;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
				+ " <thead>"
				+ " 	<tr class=\"ui-jqgrid-labels\" role=\"rowheader\">"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_rn\" role=\"columnheader\" style=\"width:20px;\">"
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_rn\">"
				+ " 				<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "					</span>"
				+ " 			</div><br/>"
				+ " 		</th>"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_cb\" role=\"columnheader\" style=\"width:20px;\">"
				+ " 			<div id=\"jqgh_subformSUBFORM_BGSRZXX_cb\">"
				+ " 				<input class=\"cbox\" id=\"chkAll\" role=\"checkbox\" type=\"checkbox\" name=\"colname\"  onclick=\"selectAll()\"/>"
				+ "					<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "					</span>"
				+ " 			</div>"
				+ " 		</th>"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZLX\" role=\"columnheader\" style=\"width:50px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "					<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZLX\">"
				+ "						阶段名称"
				+ "						<span class=\"s-ico\" style=\"display:none;\">"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\"sort=\"asc\"></span>"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "						</span>"
				+ " 			</div>"
				+ "			</th>"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZQSRQ\" role=\"columnheader\" style=\"width:80px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "					<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZQSRQ\">"
				+ "						评价人"
				+ "						<span class=\"s-ico\" style=\"display:none;\">"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "						</span>"
				+ "					</div>"
				+ "			</th>"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_BZ\" role=\"columnheader\" style=\"width:80px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "					<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_BZ\">"
				+ "						评价时间"
				+ "						<span class=\"s-ico\" style=\"display:none;\">"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "						</span>"
				+ "					</div>"
				+ "			</th>"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\" style=\"width:80px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "					<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">"
				+ "						评价结果"
				+ "						<span class=\"s-ico\" style=\"display:none;\">"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "						</span>"
				+ "					</div>"
				+ "			</th>"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_RZZT\" role=\"columnheader\" style=\"width:100px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ "					<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_RZZT\">"
				+ "						评价说明"
				+ "						<span class=\"s-ico\" style=\"display:none;\">"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "							<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "						</span>"
				+ "					</div>"
				+ "			</th>"
				+ " 		<th class=\"ui-state-default ui-th-column ui-th-ltr\" id=\"subformSUBFORM_BGSRZXX_XC\" role=\"columnheader\" style=\"width:100px;\">"
				+ "				<span class=\"ui-jqgrid-resize ui-jqgrid-resize-ltr\" style=\"cursor:col-resize;\">&nbsp;</span>"
				+ " 			<div class=\"ui-jqgrid-sortable\" id=\"jqgh_subformSUBFORM_BGSRZXX_XC\">"
				+ " 				操作"
				+ "					<span class=\"s-ico\" style=\"display:none;\">"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr\" sort=\"asc\"></span>"
				+ "						<span class=\"ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr\" sort=\"desc\"></span>"
				+ "					</span>"
				+ " 			</div>"
				+ "			</th>"
				+ " 	</tr>"
				+ " </thead>"
				+ "</table>");
	content.append("<div style=\"height: 100%; width: 850px;\" class=\"ui-jqgrid-bdiv\">"
				+ "		<div style=\"position:relative;\">"
				+ "		<div></div>"
				+ "			<table style=\"width: 850px;\" class=\"ui-jqgrid-btable\" aria-multiselectable=\"true\" role=\"grid\" tabindex=\"1\" id=\"subformSUBFORM_SXCYRY\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
				+ "				<tbody> ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, projectNo);
			rs = stmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				String task_name = rs.getString("TASK_NAME") == null ? "" : rs.getString("TASK_NAME");
				String pjr = rs.getString("PJR") == null ? "" : rs.getString("PJR");
				String pjrname = rs.getString("USERNAME") == null ? "" : rs.getString("USERNAME");
				Date pjsj = rs.getDate("PJSJ");
				String pjsjstr = pjsj != null ? sdf.format(pjsj) : "";
				String pjsm = rs.getString("PJSM") == null ? "" : rs.getString("PJSM");
				String groupid = rs.getString("GROUPID") == null ? "" : rs.getString("GROUPID");
				String pjinsid = rs.getString("PJINSID") == null ? "" : rs.getString("PJINSID");
				String id = rs.getString("ID") == null ? "" : rs.getString("ID");
				String pjjg = rs.getString("LDPJ") == null ? "" : rs.getString("LDPJ").toString();
				try {
			content.append("<tr class=\"jqgfirstrow\" role=\"row\" style=\"height:auto\">"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:20px;\"></td>"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:20px;\"></td>"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:50px;\"></td>"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:80px;\"></td>"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:80px;\"></td>"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:80px;\"></td>"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>"
					+ "			<td role=\"gridcell\" style=\"height:0px;width:100px;\"></td>"
					+ "		</tr>"
					+ "		<tr class=\" ui-widget-content jqgrow  \" >                                                   "
					+ "			<td role=\"gridcell\" style=\"width:20px;text-align:center;\" class=\"ui-state-default jqgrid-rownum\" title=\"" + i + "\" aria-describedby=\"subformSUBFORM_XMBASE_rn\">" + i + "</td>"
					+ "			<td role=\"gridcell\" style=\"width:20px;padding-left:8px;\" aria-describedby=\"subformSUBFORM_SXFKRBD_cb\">"
					+ "<input class=\"cbox\" id=\""+pjinsid+"\" role=\"checkbox\" type=\"checkbox\" name=\"colname\"/>"
					+ "			</td>"
					+ "			<td role=\"gridcell\" style=\"width:50px;text-align:left;padding-left:10px;\" title=\"" + task_name + "\" aria-describedby=\"subformSUBFORM_SXCYRY_POSITION\" ><xmp>" + task_name + "</xmp></td>"
					+ "			<td role=\"gridcell\" style=\"width:80px;text-align:left;\" title=\"" + pjr+"["+pjrname+"]" + "\" aria-describedby=\"subformSUBFORM_SXCYRY_USERID\"><xmp>" + pjr+"["+pjrname+"]" + "</xmp></td>"
					+ "			<td role=\"gridcell\" style=\"width:80px;\" title=\"" + pjsj + "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\" style=\"text-align:left;\"><xmp>" + pjsj + "</xmp></td>                   "
					+ "			<td role=\"gridcell\" style=\"width:80px;\" title=\"" + pjjg + "\" aria-describedby=\"subformSUBFORM_SXCYRY_NAME\" style=\"text-align:left;\"><xmp>" + pjjg + "</xmp></td>                   "
					+ "			<td role=\"gridcell\" style=\"width:100px;\" title=\"" + pjsm + "\" aria-describedby=\"subformSUBFORM_SXCYRY_TEL\"><xmp>" + substring(pjsm, 12, "UTF-8") + (pjsm.length() > 12 ? "..." : "") + "</xmp></td>"
					+ "			<td role=\"gridcell\" style=\"width:100px;\" title=\"\" aria-describedby=\"subformSUBFORM_SXCYRY_TEL\">");
					/**只可编辑自己添加的评价信息*/
					if(pjr.equals(UserContextUtil.getInstance().getCurrentUserId())){
						content.append("<a href=\"javascript:newPJ('"+groupid+"','"+pjinsid+"')\">编辑</a>");
					}
				content.append("</td>"
					+ "		</tr>");
				} catch (UnsupportedEncodingException e) {
					logger.error(e,e);
				}
				i++;
			}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rs);
		}
		content.append("</tbody>"
				+ "</table>");
		return content.toString();
	}

	public String getCustomermsg(String customerno) {
		List<HashMap> customerData = dongGuanZqbProjectManageDAO.getCustomerByCustomerno(customerno);
		HashMap<String,String> returnMap = new HashMap<String,String>();
		if(customerData.size()>0){
			HashMap cusmap = customerData.get(0);
			returnMap.put("GFGSRQ", cusmap.get("GFGSRQ")==null?"":cusmap.get("GFGSRQ").toString());
			returnMap.put("GB", cusmap.get("GB")==null?"":cusmap.get("GB").toString());
			returnMap.put("USERNAME", cusmap.get("USERNAME")==null?"":cusmap.get("USERNAME").toString());
			returnMap.put("TEL", cusmap.get("TEL")==null?"":cusmap.get("TEL").toString());
			returnMap.put("TYPE", cusmap.get("TYPE")==null?"":cusmap.get("TYPE").toString());
			returnMap.put("ZYYW", cusmap.get("ZYYW")==null?"":cusmap.get("ZYYW").toString());
			returnMap.put("QTSM", cusmap.get("QTSM")==null?"":cusmap.get("QTSM").toString());
		}
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getCusMsgByProno(String projectno) {
		List<HashMap> customerData = dongGuanZqbProjectManageDAO.getCusMsgByProno(projectno);
		HashMap<String,String> returnMap = new HashMap<String,String>();
		if(customerData.size()>0){
			HashMap cusmap = customerData.get(0);
			returnMap.put("USERNAME", cusmap.get("USERNAME")==null?"":cusmap.get("USERNAME").toString());
			returnMap.put("TEL", cusmap.get("TEL")==null?"":cusmap.get("TEL").toString());
		}
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getCutomeUrl(String customerno) {
		String idfid = dongGuanZqbProjectManageDAO.getCusIDFIdByCusno(customerno);
		String[] i_d_fid = idfid.split("-");
		HashMap<String,String> returnMap = new HashMap<String,String>();
		if(i_d_fid.length==3){
			returnMap.put("DEMID", i_d_fid[1]);
			returnMap.put("FORMID", i_d_fid[2]);
			returnMap.put("INSTANCEID", i_d_fid[0]);
		}else{
			returnMap.put("DEMID", "21");
			returnMap.put("FORMID", "88");
			returnMap.put("INSTANCEID", "0");
		}
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getProjectmsg(String projectno) {
		List<HashMap> projectData = dongGuanZqbProjectManageDAO.getProjectByProjectno(projectno);
		HashMap<String,String> returnMap = new HashMap<String,String>();
		if(projectData.size()>0){
			HashMap cusmap = projectData.get(0);
			returnMap.put("MANAGER", cusmap.get("MANAGER")==null?"":cusmap.get("MANAGER").toString());
			returnMap.put("PROJECTNAME", cusmap.get("PROJECTNAME")==null?"":cusmap.get("PROJECTNAME").toString());
		}
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	public String getIPOProjectmsg(String projectno) {
		List<HashMap> projectData = dongGuanZqbProjectManageDAO.getProjectByIPOPro(projectno);
		HashMap<String,String> returnMap = new HashMap<String,String>();
		if(projectData.size()>0){
			HashMap cusmap = projectData.get(0);
			returnMap.put("MANAGER", cusmap.get("MANAGER")==null?"":cusmap.get("MANAGER").toString());
			returnMap.put("PROJECTNAME", cusmap.get("PROJECTNAME")==null?"":cusmap.get("PROJECTNAME").toString());
			returnMap.put("CUSTOMERNO", cusmap.get("CUSTOMERNO")==null?"":cusmap.get("CUSTOMERNO").toString());
		}
		JSONArray json = JSONArray.fromObject(returnMap);
		StringBuffer jsonHtml = new StringBuffer();
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	
	public String projectXmjdContent(String projectno) {
		StringBuffer sql = new StringBuffer(
				"SELECT A.PROJECTNO,"
				+ "NVL(A.CREATEUSERID,'') CREATEUSERID,"
			    + "NVL(A.CREATEUSER,'') CREATEUSER,"
			    + "NVL(A.FJ,'') FJ," //阶段资料
			    + "A.CJSJ,"
			    + "NVL(A.INSTANCEID,'') INSTANCEID,"
			    + "NVL(B.JDMC,'') TASK_NAME,"
			    + "NVL(B.SXZLQD,'') SXZLQD," //阶段模板
			    + "B.ID JDBH"
			+" FROM BD_ZQB_TYXM_INFO B LEFT JOIN ("
			     +" SELECT * FROM ("
			     +" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.NFXFILE AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMNFXB BOTABLE" 
			     +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行立项')"
			     
				 +" union all"
				 +" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.FABSZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMFAZLBSB BOTABLE"
			     +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行概况')"
			     
				 +" union all"
				 +" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.SBZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE"
		         +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行备案')"
				 
				 +" union all"
				 +" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.SBZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE"
				 +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='股东大会决议')"
				
				 +" union all"
				 +" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.SBZL AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSBZLSB BOTABLE"
				 +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='认购起始日')"
		         
		         +" union all"
				 +" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.NFXFILE AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_DXFXFK BOTABLE"
				 +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行反馈')"
			     
				 +" union all"
			     +" SELECT BOTABLE.PROJECTNO,BOTABLE.CREATEUSERID,BOTABLE.CREATEUSER,BOTABLE.NFXFILE AS FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.CJSJ FROM BD_ZQB_GPFXXMSJFXB BOTABLE"
			     +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行备案结果')"
			     
				 +" union all"
				 +" SELECT BOTABLE.PROJECTNO,BOTABLE.USERID,BOTABLE.USERNAME,BOTABLE.FJ,BOTABLE.GROUPID,BINDTABLE.INSTANCEID,BOTABLE.ZZSJ FROM BD_ZQB_XMZZ BOTABLE"
				 +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='定向发行项目终止')"
				 
				 +" union all"
				 +"  SELECT BOTABLE.PROJECTNO,BOTABLE.Sxyzlmb CREATEUSERID,BOTABLE.Manager CREATEUSER,BOTABLE.Jdzl AS FJ,TO_NUMBER(BOTABLE.GROUPID),BINDTABLE.INSTANCEID,BOTABLE.Gxsj as CJSJ FROM BD_ZQB_GPFXXMRWLCB BOTABLE"
				 +" INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目组工作记录')"
				 
				 +" union all"
				 +" SELECT BOTABLE.PROJECTNO,BOTABLE.Jdzl as CREATEUSERID,BOTABLE.Manager as CREATEUSER,BOTABLE.Attach  AS FJ,TO_NUMBER(BOTABLE.GROUPID),BINDTABLE.INSTANCEID,BOTABLE.Gxsj as CJSJ FROM BD_ZQB_GPFXXMRWB BOTABLE"
				 +"  INNER JOIN SYS_ENGINE_FORM_BIND BINDTABLE ON BOTABLE.ID = BINDTABLE.DATAID AND BINDTABLE.INSTANCEID IS NOT NULL AND BINDTABLE.FORMID = (SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他文件')"
			     
			     +" )"
			     +" WHERE PROJECTNO=?"
			     
			     +" ) A ON B.ID = A.GROUPID" 

			+" WHERE B.XMLX='东莞-定向发行' AND B.STATE=1 ORDER BY B.SORTID"
			);
		StringBuffer content = new StringBuffer();
		Connection conn = DBUtil.open();
		PreparedStatement stmt = null;
		ResultSet rsZL = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			stmt =conn.prepareStatement(sql.toString());
			stmt.setString(1, projectno);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				content.append("<tr class=\"ui-widget-content jqgrow ui-row-ltr\" role=\"row\">");
					content.append("<td title=\""+rs.getString("TASK_NAME")+"\" style=\"padding-left:10px;\" role=\"gridcell\">"+rs.getString("TASK_NAME")+"</td>");
					content.append("<td title=\""+(rs.getString("CREATEUSER")==null?"":rs.getString("CREATEUSER"))+"\" style=\"\" role=\"gridcell\">"+(rs.getString("CREATEUSER")==null?"":rs.getString("CREATEUSER"))+"</td>");
					
					content.append("<td title=\"模板资料\" role=\"gridcell\">");
					String sxzlqd = rs.getString("SXZLQD")==null?"":rs.getString("SXZLQD");
					String cjsj = rs.getDate("CJSJ")==null?"":sdf.format(rs.getDate("CJSJ"));
					content.append(cjsj);//getFileHtml(sxzlqd)
					content.append("</td>");
					
					content.append("<td title=\"阶段资料\" role=\"gridcell\">");
					String mb = rs.getString("FJ")==null?"":rs.getString("FJ");
					content.append(getFileHtml(mb));
					content.append("</td>");
					content.append("<td role=\"gridcell\">");
						if(rs.getString("INSTANCEID")==null){
							content.append("<a href=\"javascript:reportXfx("+rs.getString("JDBH")+",'"+rs.getString("TASK_NAME")+"')\">上传</a>");
						}else{
							content.append("<a href=\"javascript:submitXfx("+rs.getString("INSTANCEID")+",'"+rs.getString("TASK_NAME")+"')\">编辑</a>");
						}
					content.append("</td>");
				content.append("</tr>");
				}
		} catch (Exception e) {
			logger.error(e,e);
		} finally {
			DBUtil.close(conn, stmt, rsZL);
		}
		return content.toString();
	}

	private StringBuffer getFileHtml(String mb) {
		List<FileUpload> l = null;
		StringBuffer content = new StringBuffer("");
		if(!mb.equals("")){
			l = FileUploadAPI.getInstance().getFileUploads(mb);
			if(l!=null){
				for (int i = 0; i < l.size(); i++) {
					if(i==0){
						content.append("<a title=\""+l.get(i).getFileSrcName()+"\" target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+l.get(i).getFileId()+"\">"+l.get(i).getFileSrcName()+"</a>");
					}else{
						content.append("</br><a title=\""+l.get(i).getFileSrcName()+"\" target=\"_blank\" href=\"uploadifyDownload.action?fileUUID="+l.get(i).getFileId()+"\">"+l.get(i).getFileSrcName()+"</a>");
					}
				}
			}
		}
		return content;
	}
	
	public List<HashMap<String, Object>> getTaskList(String xmbh) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT"
				+ " TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.TBSJ"
				+ " FROM (SELECT ID,SORTID,JDMC,SXZLQD,STATE FROM BD_ZQB_TYXM_INFO WHERE XMLX='东莞-并购重组') TYXM"
				+ " LEFT JOIN (");
		sql.append("SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGXMLX B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购立项') AND B.XMBH=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='公告并购重组方案') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购股东大会决议') AND B.XMBH=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购申报') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGFAZLBS B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购反馈') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGZLGD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购补充披露') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSSQKB B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购实施情况') AND B.XMBH=?");

		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.TBSJ FROM BD_ZQB_BGSBZL B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='持续督导期间') AND B.XMBH=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_XMZZ B INNER JOIN ORGUSER ORG ON B.USERID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='并购项目终止') AND B.XMBH=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,TO_NUMBER(B.Groupid) JDBH,B.Jdzl FJ,B.Projectno XMBH,B.Gxsj CJSJ FROM BD_ZQB_GPFXXMRWLCB B INNER JOIN ORGUSER ORG ON B.Sxyzlmb=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目组工作记录') AND B.Projectno=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,TO_NUMBER(B.Groupid) JDBH,B.Attach FJ,B.Projectno XMBH,B.Gxsj CJSJ FROM BD_ZQB_GPFXXMRWB B  INNER JOIN ORGUSER ORG ON B.jdzl=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他文件') AND B.Projectno=?");
		
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH WHERE TYXM.STATE=1 ");
		sql.append("ORDER BY TYXM.SORTID");
		List<HashMap<String, Object>> list = dongGuanZqbProjectManageDAO.getTaskList(sql.toString(),xmbh);
		return list;
	}
	
	public List<HashMap<String, Object>> getIpoList(String xmbh,String type) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TYXM.ID,TYXM.JDMC,JDDATA.TBR,JDDATA.XMBH,TYXM.SXZLQD,JDDATA.FJ,JDDATA.INSTANCEID,JDDATA.CJSJ FROM (SELECT ID,SORTID,JDMC,SXZLQD FROM BD_ZQB_TYXM_INFO");
		sql.append(" WHERE XMLX='东莞-IPO项目' AND STATE=1");
		if(type!=null&&type.equals("zrz")){
			sql.append(" AND JDMC IN('立项','公告发行方案','股东大会决议','申报','反馈','核准','封卷','发行实施','新增股份上市','持续督导期间','项目终止','项目组工作记录','其他文件')");
		}else if(type!=null&&type.equals("scgkfx")){
			sql.append(" AND JDMC IN('立项','股改','辅导','申报','反馈','上会','发行','上市','持续督导期间','项目终止','项目组工作记录','其他文件')");
		}
		else if(type!=null&&(type.equals("kjhz")||type.equals("qyz")||type.equals("gsz"))){
			sql.append(" AND JDMC IN('立项','公告发行方案','权力机构决议','申报','反馈','核准','封卷','发行实施','持续督导期间','项目终止','项目组工作记录','其他文件')");
		}
		//else if(type!=null&&type.equals("qt")){
		else {
			sql.append(" AND JDMC IN('立项','公告方案','权力机构决议','申报','反馈','核准','封卷','实施','持续督导期间','项目终止','项目组工作记录','其他文件')");
		}
		sql.append(") TYXM LEFT JOIN (");
		
		sql.append("SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目立项') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO股改') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='公告发行方案') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='大会决议') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='权力机构决议') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO辅导') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO申报') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO反馈') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO上会') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO发行') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO上市') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO持续督导') AND B.XMBH=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='核准') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='封卷') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='实施') AND B.XMBH=?");
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.CJSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='新增股份上市') AND B.XMBH=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,B.JDBH,B.FJ,B.XMBH,B.ZZSJ FROM BD_ZQB_IPOJD B INNER JOIN ORGUSER ORG ON B.TBRID=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='IPO项目终止') AND B.XMBH=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,TO_NUMBER(B.Groupid) JDBH,B.Jdzl FJ,B.Projectno XMBH,B.Gxsj CJSJ FROM BD_ZQB_GPFXXMRWLCB B INNER JOIN ORGUSER ORG ON B.Sxyzlmb=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='项目组工作记录') AND B.Projectno=?");
		
		sql.append(" UNION");
		sql.append(" SELECT ORG.USERNAME TBR,S.INSTANCEID,TO_NUMBER(B.Groupid) JDBH,B.Attach FJ,B.Projectno XMBH,B.Gxsj CJSJ FROM BD_ZQB_GPFXXMRWB B  INNER JOIN ORGUSER ORG ON B.jdzl=ORG.USERID LEFT JOIN SYS_ENGINE_FORM_BIND S ON S.DATAID=B.ID WHERE S.FORMID=(SELECT ID FROM SYS_ENGINE_IFORM WHERE IFORM_TITLE='其他文件') AND B.Projectno=?");
		
		sql.append(" ) JDDATA ON TYXM.ID=JDDATA.JDBH ");
		sql.append("ORDER BY TYXM.SORTID");
		List<HashMap<String, Object>> list = dongGuanZqbProjectManageDAO.getIpoList(sql.toString(),xmbh);
		return list;
	}

	public boolean checkProName(String projectname, String instanceid, String type) {
		boolean flag = true;
		StringBuffer sql = new StringBuffer();
		if(type.equals("tjgp")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_PJ_BASE WHERE PROJECTNAME=?");
		}else if(type.equals("dxzf")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_GPFXXMB WHERE PROJECTNAME=?");
		}else if(type.equals("xsb")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_BGZZLXXX WHERE JYF=? AND COMPANYNAME='xsb'");
		}else if(type.equals("qtxsb")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='xsb' AND A08 IN('其他')");
		}
		
		else if(type.equals("scssgs")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='ssgs' AND A08 IN('IPO','改制','辅导')");
		}else if(type.equals("zrzssgs")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='ssgs' AND A08 IN('增发','配股','非公开发行','可转债')");
		}else if(type.equals("ssgs")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_BGZZLXXX WHERE JYF=? AND COMPANYNAME='ssgs'");
		}else if(type.equals("qtssgs")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='ssgs' AND A08 IN('其他')");
		}
		
		else if(type.equals("gszzq")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='zq' AND A08 IN('公司债')");
		}else if(type.equals("qyzzq")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='zq' AND A08 IN('企业债')");
		}else if(type.equals("kjhzzq")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='zq' AND A08 IN('可交换债')");
		}else if(type.equals("qtzq")){
			sql.append("SELECT COUNT(ID) AS NUM FROM BD_ZQB_TYXM WHERE PROJECTNAME=? AND A01='zq' AND A08 IN('其他')");
		}
		Map params = new HashMap();
		params.put(1, projectname);
		String num = com.iwork.commons.util.DBUtil.getDataStr("NUM", sql.toString(), params);
		
		flag = instanceid.equals("0")?!num.equals("0")? false:true:!num.equals("1")?false:true;
		
		return flag;
	}

	public List<HashMap> getQjlcListByUserid(int pageSize, int pageNumber, String username, String departmentname, String workstatus, String userid) {
		return dongGuanZqbProjectManageDAO.getQjlcListByUserid(pageSize,pageNumber,username,departmentname,workstatus,userid);
	}
	public int getQjlcListByUseridSize(String username, String departmentname, String workstatus, String userid) {
		return dongGuanZqbProjectManageDAO.getQjlcListByUseridSize(username,departmentname,workstatus,userid).size();
	}
	
	public List<HashMap> getQjlcList(int pageSize, int pageNumber, String username, String departmentname, String workstatus) {
		return dongGuanZqbProjectManageDAO.getQjlcList(pageSize,pageNumber,username,departmentname,workstatus);
	}
	public int getQjlcListSize(String username, String departmentname, String workstatus) {
		return dongGuanZqbProjectManageDAO.getQjlcListSize(username,departmentname,workstatus).size();
	}
	public boolean sickLeave(String instanceid,String sicktime){
		boolean flag = true;
		Long instanceId = Long.parseLong(instanceid);
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		String actDefId = ProcessAPI.getInstance().getProcessActDefId("QJLC");
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String sickLeaveUserid = uc._userModel.getUserid();
		String now = UtilDate.getNowDatetime();
		dataMap.put("TASK_NAME", sickLeaveUserid);
		dataMap.put("JDZL", now);
		dataMap.put("ATTACH", sicktime);
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceId, dataMap, Long.parseLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		return flag;
	}
	public boolean sickDel(String instanceid){
		boolean flag = true;
		Long instanceId = Long.parseLong(instanceid);
		//更新删除状态
		HashMap<String,Object> dataMap = ProcessAPI.getInstance().getFromData(instanceId, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		dataMap.put("PRIORITY", "0");
		String actDefId = ProcessAPI.getInstance().getProcessActDefId("QJLC");
		flag = ProcessAPI.getInstance().updateFormData(actDefId, instanceId, dataMap, Long.parseLong(dataMap.get("ID").toString()), false, EngineConstants.SYS_INSTANCE_TYPE_PROCESS);
		//删除待审核信息
		Task newTaskId = ProcessAPI.getInstance().newTaskId(instanceId);
		String assignee = newTaskId.getAssignee();
		String taskId = newTaskId.getId();
		ProcessAPI.getInstance().setTaskAssignee(taskId, "");
		
		return flag;
	}
}
