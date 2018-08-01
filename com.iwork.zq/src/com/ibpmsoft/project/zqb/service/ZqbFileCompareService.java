package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.dao.ZqbFileCompareDAO;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.ResponseUtil;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.km.know.service.KnowService;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
public class ZqbFileCompareService {
	
	private KnowService knowService;
	private ZqbFileCompareDAO zqbFileCompareDAO;
	private List<HashMap> fileCompareList;
	private ZqbCustomerManageService zqbCustomerManageService;
	private static Logger logger = Logger.getLogger(ZqbFileCompareService.class);
	public void impKnow(String filename) {
		Long starttime = new Date().getTime();
		List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(filename);
		OrgUser user =UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
		String rootPath=request.getRealPath("/");
		//String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
		String question = null;
		String answer = null;
		String typename = null; 
		String now = UtilDate.getNowDatetime();
		String userid = user.getUserid();
		String username = user.getUsername();
		File file = null;
		Workbook workBook = null;
		Sheet sheet = null;
		//问题集合，用于判定问题是否存在
		Set<String> qcontent = new HashSet<String>();
		//分类集合，用于判定分类是否存在
		Set<String> cname = new HashSet<String>();
		//分类Map，用于获取分类id
		Map<String,Long> cname_id = new HashMap<String,Long>();
		
		Long aid_max=DBUtil.getLong("SELECT MAX(ID) ID FROM IWORK_KNOW_ANSWER", "ID");
		Long qid_max=DBUtil.getLong("SELECT MAX(ID) ID FROM IWORK_KNOW_QUESTION", "ID");
		Long tid_max=DBUtil.getLong("SELECT MAX(ID) ID FROM IWORK_KNOW_CLASSES", "ID");
		Long aid;Long qid;Long tid;
		
		StringBuffer qtype = new StringBuffer();
		qtype.append("INSERT INTO IWORK_KNOW_CLASSES(ID,CNAME,CORDER,CTYPE,CEXPERT) VALUES(?,?,?,?,?)");
		StringBuffer qes = new StringBuffer();
		qes.append( "INSERT INTO IWORK_KNOW_QUESTION(ID,QUID,QCONTENT,QUNAME,QADDCONTENT,QBEGINTIME,QSOLVETIME,QTYPE,QBIGC,QLETC,QTAGS,ANSWERBODY,SHOWNAMETYPE,SCORE,CLICKCOUNT,FILEUUIDS)"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		StringBuffer ans = new StringBuffer();
		ans.append("INSERT INTO IWORK_KNOW_ANSWER(ID,QID,AUID,AUNAME,ACONTENT,AADDCONTENT,ATIME,ATYPE,INVITEDMAN,FILEUUIDS) VALUES(?,?,?,?,?,?,?,?,?,?)");
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT TRIM(QCONTENT) QCONTENT FROM IWORK_KNOW_QUESTION WHERE QTYPE=1");
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT TRIM(CNAME),ID FROM IWORK_KNOW_CLASSES WHERE CTYPE='可用'");
		
		Connection conn = DBUtil.open();
		PreparedStatement ps = null;
		PreparedStatement pst = null;
		PreparedStatement psq = null;
		PreparedStatement psa = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql1.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				qcontent.add(rs.getString(1).replaceAll("[^\u4E00-\u9FA5]", ""));
			}
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			ps = conn.prepareStatement(sql2.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				cname.add(rs.getString(1));
				cname_id.put(rs.getString(1), rs.getLong(2));
			}
			//初始化三表id增加数
			aid=1l;qid=1l;tid=1l;
			
			pst = conn.prepareStatement(qtype.toString());
			psq = conn.prepareStatement(qes.toString());
			psa = conn.prepareStatement(ans.toString());
			conn.setAutoCommit(true);
			//遍历上传文件
			for (FileUpload fileUpload : sublist) {
				//加载xls文件
				file = new File(rootPath+File.separator+fileUpload.getFileUrl());
				
				try {workBook = new XSSFWorkbook(rootPath+File.separator+fileUpload.getFileUrl());  
				} catch (Exception ex) {  
					try {workBook = new HSSFWorkbook(new FileInputStream(rootPath+File.separator+fileUpload.getFileUrl()));
					} catch (FileNotFoundException e) {} catch (Exception e) {}
				}
				if(workBook==null){continue;}
				//遍历sheet
				for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {  
					sheet = workBook.getSheetAt(numSheet);  
					if (sheet == null) {continue;}
					// 循环行Row  
					for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {  
						Row row = sheet.getRow(rowNum);  
						if (row == null) {continue;}  
						// 循环列Cell  
						Cell cell = row.getCell(0);  
						if (cell == null) {continue;}
						question = getValue(cell);
						//问题不存在导入
						if(!qcontent.contains(question.replaceAll("[^\u4E00-\u9FA5]", ""))){
							qcontent.add(question.replaceAll("[^\u4E00-\u9FA5]", ""));
							cell = row.getCell(3);
							typename = getValue(cell);
							cell = row.getCell(1);
							answer = getValue(cell);
							if(!cname.contains(typename.trim())){
								pst.setLong(1,tid_max+tid);
								pst.setString(2,typename.trim());
								pst.setLong(3,tid_max+tid);
								pst.setString(4,"可用");
								pst.setString(5,"");
								pst.addBatch();
								
								cname.add(typename.trim());
								cname_id.put(typename.trim(), tid_max+tid);
								//分类表id增加数自增
								tid++;
							}
							psq.setLong(1,qid_max+qid);
							psq.setString(2,userid);
							psq.setString(3,question.trim());
							psq.setString(4,username);
							psq.setString(5,"");
							psq.setString(6,now);
							psq.setString(7,"");
							psq.setLong(8,1l);
							psq.setLong(9,cname_id.get(typename.trim()));
							psq.setLong(10,0);
							psq.setString(11,"");
							psq.setString(12,"");
							psq.setLong(13,0l);
							psq.setLong(14,0l);
							psq.setLong(15,0l);
							psq.setString(16,"");
							psq.addBatch();
							
							psa.setLong(1,aid_max+aid);
							psa.setLong(2,qid_max+qid);
							psa.setString(3,"");
							psa.setString(4,"");
							psa.setString(5,answer);
							psa.setString(6,"");
							psa.setString(7,now);
							psa.setLong(8,1l);
							psa.setString(9,"");
							psa.setString(10,"");
							psa.addBatch();
							//问题表、答案表id增加数自增
							qid++;aid++;
						}
					}
				}
			}
			pst.executeBatch();
			psq.executeBatch();
			psa.executeBatch();
		} catch (Exception e) {}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(psq!=null){
					psq.close();
				}
				if(psa!=null){
					psa.close();
				}
			} catch (Exception e) {
				
			}
			DBUtil.close(conn, ps, rs);
		}
		Long endtime = new Date().getTime();
		ResponseUtil.write("导入完毕,用时"+(endtime-starttime)+"毫秒");
		starttime = null;endtime = null;sublist = null;
		user = null;actionContext = null;request = null;rootPath = null;
		question = null;answer = null;typename = null;now = null;userid = null;
		username = null;sheet = null;qcontent = null;cname = null;cname_id = null;
		aid_max = null;qid_max = null;tid_max = null;aid = null;qid = null;
		tid = null;ps = null;pst = null;psq = null;qtype = null;
		qes = null;ans = null;rs = null;file = null;workBook = null;
		System.gc();
	}
	public void expkhmc(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("客户列表");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style4.setFont(headfont);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("客户名称");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券简称(公司简称)");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("证券代码(股票代码)");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("客户联系人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("客户联系电话");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("客户状态");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("挂牌时间");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("已挂牌");cell1.setCellStyle(style4);
		if(zqbCustomerManageService==null){
			zqbCustomerManageService = (ZqbCustomerManageService)SpringBeanUtil.getBean("zqbCustomerManageService");
		}
		list=zqbCustomerManageService.getCurrentCustomerListAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		for (HashMap map : list) {
			z=0;
			row1 = sheet.createRow((int) m++);
			HSSFCell 
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CUSTOMERNAME") == null ? "" : map.get("CUSTOMERNAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQJC") == null ? "" : map.get("ZQJC").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("ZQDM") == null ? "" : map.get("ZQDM").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("USERNAME") == null ? "" : map.get("USERNAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("TEL") == null ? "" : map.get("TEL").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("STATUS") == null ? "" : map.get("STATUS").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("GPSJ") == null ? "" : map.get("GPSJ").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("YGP") == null ? "" : map.get("YGP").toString());cell2.setCellStyle(style4);
		}
		for (int i = 0; i < 8; i++) {
			sheet.setColumnWidth(i, 5000);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename="
					+ URLEncoder.encode(
							new StringBuilder("客户列表").append(".xls")
									.toString(), "UTF-8");

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
	//导出聊天记录
	public void expLTJL(HttpServletResponse response,String senduser,String nickname, String chatName,String startdate, String enddate, String content,String sendname,String companyjc) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("ChatRecord");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style4.setFont(headfont);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell 
		cell1 = row1.createCell((short) z++);cell1.setCellValue("发送时间");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("发送人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("接收人");cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);cell1.setCellValue("发送内容");cell1.setCellStyle(style4);
		list=zqbFileCompareDAO.getLTJL(nickname, chatName,startdate,enddate,content,sendname,companyjc);
		for (HashMap map : list) {
			z=0;
			row1 = sheet.createRow((int) m++);
			HSSFCell 
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("DATATIME") == null ? "" : map.get("DATATIME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("USERNAME") == null ? "" : map.get("USERNAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("SENDNAME") == null ? "" : "all".equals(map.get("SENDNAME").toString())?"-所有人-":map.get("SENDNAME").toString());cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);cell2.setCellValue(map.get("CONTENT") == null ? "" : map.get("CONTENT").toString());cell2.setCellStyle(style4);
		}
		for (int i = 0; i < 4; i++) {
			sheet.setColumnWidth(i, 10000);
		}
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename="
					+ URLEncoder.encode(
							new StringBuilder("ChatRecord").append(".xls")
									.toString(), "UTF-8");

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
	public Integer expDelete(String senduser,String nickname, String chatName,String startdate, String enddate, String content,String sendname,String companyjc) {
		Integer num = 0;
		num = zqbFileCompareDAO.expDelete(nickname, chatName,startdate,enddate,content,sendname,companyjc);
		return num;
	}
	public void expKnow(HttpServletResponse response) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("问答");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setWrapText(true);
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		style4.setFont(headfont);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> list1 = new ArrayList<HashMap>();
		int m=0;
		int z=0;
		HSSFRow row1 = sheet.createRow((int) m++);
		HSSFCell cell1 = row1.createCell((short) z++);
		cell1.setCellValue("问题");
		cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("答复");
		cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("发布时间");
		cell1.setCellStyle(style4);
		cell1 = row1.createCell((short) z++);
		cell1.setCellValue("分类");
		cell1.setCellStyle(style4);
		list=zqbFileCompareDAO.getKnowAnswer();
		for (HashMap map : list) {
			z=0;
			row1 = sheet.createRow((int) m++);
			HSSFCell cell2 = row1.createCell((short) z++);
			cell2.setCellValue(map.get("QCONTENT").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);
			cell2.setCellValue(map.get("ACONTENT").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);
			cell2.setCellValue(map.get("ATIME").toString());
			cell2.setCellStyle(style4);
			cell2 = row1.createCell((short) z++);
			cell2.setCellValue(map.get("CNAME").toString());
			cell2.setCellStyle(style4);
		}
		sheet.setColumnWidth(0, 12000);
		sheet.setColumnWidth(1, 12000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 6000);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("问答表.xls");
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
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}

		}
	}
	
	private String getValue(Cell cell) {  
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {  
            return String.valueOf(cell.getBooleanCellValue());  
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {  
            return String.valueOf(cell.getNumericCellValue());  
        } else {  
            return String.valueOf(cell.getStringCellValue());  
        }  
    }

	public KnowService getKnowService() {
		return knowService;
	}

	public void setKnowService(KnowService knowService) {
		this.knowService = knowService;
	}

	public ZqbFileCompareDAO getZqbFileCompareDAO() {
		return zqbFileCompareDAO;
	}

	public void setZqbFileCompareDAO(ZqbFileCompareDAO zqbFileCompareDAO) {
		this.zqbFileCompareDAO = zqbFileCompareDAO;
	}

	public String fileCompare(String FILETEXT) {
		List<FileUpload> sublist = FileUploadAPI.getInstance()
				.getFileUploads(FILETEXT);
		String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
		fileCompareList=new ArrayList<HashMap>();
		for (FileUpload fileUpload : sublist) {
			String extension = fileUpload.getFileSrcName().substring(fileUpload.getFileSrcName().lastIndexOf("."), fileUpload.getFileSrcName().length());
			String fileUrl = fileUpload.getFileUrl();
			String fileSrcName = fileUpload.getFileSrcName();
			if(extension.equals(".wps")){
				try {
					FileInputStream in = new FileInputStream(rootPath+fileUrl);// 加载文档  D:\\文档检验\\宏发有色-2015-半年报.doc
					POIFSFileSystem pfs = new POIFSFileSystem(in);
					HWPFDocument hwpf = new HWPFDocument(pfs);
					Range range = hwpf.getRange();// 获取文档的读取范围
					TableIterator it = new TableIterator(range);//迭代文档中的表格
					int count=0;
					while (it.hasNext()) {
						Table tb = (Table) it.next();
						int numRows = tb.getRow(0).numCells();
						TableRow row = tb.getRow(0);
						if(numRows==4&&(row.getCell(1).text().toString().trim().equals("本期")||row.getCell(1).text().toString().trim().equals("本期期末"))&&(row.getCell(2).text().toString().trim().equals("上年同期")||row.getCell(2).text().toString().trim().equals("上年期末"))&&row.getCell(3).text().toString().trim().equals("增减比例")){
							count++;
							for (int i = 1; i < tb.numRows(); i++) {
								TableRow tr = tb.getRow(i);
								HashMap hashMap=new HashMap();
								hashMap.put("ID", count);
								hashMap.put("MC", fileSrcName);
								hashMap.put("BT", tr.getCell(0).text().toString().trim());
								hashMap.put("BQ", tr.getCell(1).text().toString().trim());
								hashMap.put("SNTQ", tr.getCell(2).text().toString().trim());
								hashMap.put("ZJBL", tr.getCell(3).text().toString().trim());
								//迭代列，默认从0开始
								// 取得单元格的内容
								if(!"-".equals(tr.getCell(3).text().toString().trim())){
									double bq=Double.valueOf(tr.getCell(1).text().toString().trim().replace(",", ""));
									double sntq=Double.valueOf(tr.getCell(2).text().toString().trim().replace(",", ""));
									double percent = (bq-sntq)/sntq;
									//输出一下，确认你的小数无误
									//获取格式化对象
									NumberFormat nt = NumberFormat.getPercentInstance();
									//设置百分数精确度2即保留两位小数
									nt.setMinimumFractionDigits(2);
									//最后格式化并输出
									String percentBj=tr.getCell(3).text().toString().trim();
									String percentString = nt.format(percent);
									if(!percentString.equals(percentBj)){
										hashMap.put("ZJBL", percentString);
										fileCompareList.add(hashMap);
									}
								}
							}
						}
						//迭代行，默认从0开始
					}
					in.close();
				} catch (Exception e) {
					logger.error(e,e);  
				} 
			}else if(extension.equals(".docx")){
				try {
					FileInputStream in = new FileInputStream(rootPath+fileUrl);
					XWPFDocument document = new XWPFDocument(in);
					int pages = document.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
					XWPFTable tableArray = document.getTableArray(0);
					List<XWPFTable> tables = document.getTables();
					int count=0;
					for (XWPFTable table : tables) {
						int size = table.getRow(0).getTableCells().size();
						XWPFTableRow row = table.getRow(0);
						if(size==4&&(table.getRow(0).getCell(1).getText().toString().trim().equals("本期")||table.getRow(0).getCell(1).getText().toString().trim().equals("本期期末"))&&(table.getRow(0).getCell(2).getText().toString().trim().equals("上年同期")||table.getRow(0).getCell(2).getText().toString().trim().equals("上年期末"))&&table.getRow(0).getCell(3).getText().toString().trim().equals("增减比例")){
							count++;
							for (int i = 1; i < table.getNumberOfRows(); i++) {
								XWPFTableRow tr = table.getRow(i);
								HashMap hashMap=new HashMap();
								hashMap.put("ID", count);
								hashMap.put("MC", fileSrcName);
								hashMap.put("BT", tr.getCell(0).getText().toString().trim());
								hashMap.put("BQ", tr.getCell(1).getText().toString().trim());
								hashMap.put("SNTQ", tr.getCell(2).getText().toString().trim());
								hashMap.put("ZJBL", tr.getCell(3).getText().toString().trim());
								// 取得单元格的内容
								if(!"-".equals(tr.getCell(3).getText().toString().trim())){
									double bq=Double.valueOf(tr.getCell(1).getText().toString().trim().replace(",", ""));
									double sntq=Double.valueOf(tr.getCell(2).getText().toString().trim().replace(",", ""));
									double percent = (bq-sntq)/sntq;
									//获取格式化对象
									NumberFormat nt = NumberFormat.getPercentInstance();
									//设置百分数精确度2即保留两位小数
									nt.setMinimumFractionDigits(2);
									//最后格式化并输出
									String percentBj=tr.getCell(3).getText().toString().trim();
									String percentString = nt.format(percent);
									if(!percentString.equals(percentBj)){
										hashMap.put("ZJBL", percentString);
										fileCompareList.add(hashMap);
									}
								}
							}
						}
					}
					in.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e,e);
				}
			}else if(extension.equals(".doc")){
				try {
					FileInputStream in = new FileInputStream(rootPath+fileUrl);// 加载文档  D:\\文档检验\\宏发有色-2015-半年报.doc
					POIFSFileSystem pfs = new POIFSFileSystem(in);
					HWPFDocument hwpf = new HWPFDocument(pfs);
					Range range = hwpf.getRange();// 获取文档的读取范围
					TableIterator it = new TableIterator(range);//迭代文档中的表格
					int count=0;
					while (it.hasNext()) {
						Table tb = (Table) it.next();
						int numRows = tb.getRow(0).numCells();
						TableRow row = tb.getRow(0);
						if(numRows==4&&(row.getCell(1).text().toString().trim().equals("本期")||row.getCell(1).text().toString().trim().equals("本期期末"))&&(row.getCell(2).text().toString().trim().equals("上年同期")||row.getCell(2).text().toString().trim().equals("上年期末"))&&row.getCell(3).text().toString().trim().equals("增减比例")){
							count++;
							for (int i = 1; i < tb.numRows(); i++) {
								TableRow tr = tb.getRow(i);
								HashMap hashMap=new HashMap();
								hashMap.put("ID", count);
								hashMap.put("BT", tr.getCell(0).text().toString().trim());
								hashMap.put("BQ", tr.getCell(1).text().toString().trim());
								hashMap.put("SNTQ", tr.getCell(2).text().toString().trim());
								hashMap.put("ZJBL", tr.getCell(3).text().toString().trim());
								//迭代列，默认从0开始
								// 取得单元格的内容
								if(!"-".equals(tr.getCell(3).text().toString().trim())){
									double bq=Double.valueOf(tr.getCell(1).text().toString().trim().replace(",", ""));
									double sntq=Double.valueOf(tr.getCell(2).text().toString().trim().replace(",", ""));
									double percent = (bq-sntq)/sntq;
									//获取格式化对象
									NumberFormat nt = NumberFormat.getPercentInstance();
									//设置百分数精确度2即保留两位小数
									nt.setMinimumFractionDigits(2);
									//最后格式化并输出
									String percentBj=tr.getCell(3).text().toString().trim();
									String percentString = nt.format(percent);
									if(!percentString.equals(percentBj)){
										hashMap.put("ZJBL", percentString);
										fileCompareList.add(hashMap);
									}
								}
							}
						}
						//迭代行，默认从0开始
					}
					in.close();
				} catch (Exception e) {
					logger.error(e,e);  
				}
			}
		}
		StringBuffer sb=new StringBuffer();
		if(fileCompareList.size()>0){
			sb.append("<table id='iform_grid'  width=\"100%\" style=\"border:1px solid #efefef\"><tr  class=\"header\">"
					+ "<td style=\"width:5%;\">标题</td><td style=\"width:20%;\">本期</td><td style=\"width:10%;\">上年同期</td>"
					+ "<td style=\"width:15%;\">增加比例</td></tr>");
			for (HashMap map : fileCompareList) {
				sb.append("<tr class=\"cell\"><td>"+map.get("BT").toString()+"</td><td>"+map.get("BQ").toString()
						+ "</td><td>"+map.get("SNTQ").toString()+"</td><td>"+map.get("ZJBL").toString()+"</td></tr>");
			}
			sb.append("</table>");
		}
		return sb.toString();
	}

}
