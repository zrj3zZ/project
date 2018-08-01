package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.Region;

import com.ibpmsoft.project.zqb.dao.ShanXiZqbContractManageDAO;
import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.app.log.util.LogUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.engine.iform.dao.SysEngineIFormDAO;
import com.iwork.core.engine.iform.model.SysEngineIform;
import com.iwork.sdk.DemAPI;

@SuppressWarnings("deprecation")
public class ShanXiZqbContractManageService {
	
	private final static String CN_FILENAME = "/common.properties";// 抓取网站配置文件
	static final char SBC_CHAR_START = 65281; // 全角！ 
	static final char SBC_CHAR_END = 65374; // 全角～
	static final int CONVERT_STEP = 65248; // 全角半角转换间隔
	static final char SBC_SPACE = 12288; // 全角空格 12288
	static final char DBC_SPACE = ' '; // 半角空格
	private ShanXiZqbContractManageDAO shanXiZqbContractManageDAO;
	private SysEngineIFormDAO sysEngineIFormDAO;
	private static Logger logger = Logger.getLogger(ShanXiZqbContractManageService.class);
	public void setShanXiZqbContractManageDAO(ShanXiZqbContractManageDAO shanXiZqbContractManageDAO) {
		this.shanXiZqbContractManageDAO = shanXiZqbContractManageDAO;
	}

	public void setSysEngineIFormDAO(SysEngineIFormDAO sysEngineIFormDAO) {
		this.sysEngineIFormDAO = sysEngineIFormDAO;
	}

	public Long getConfig(String parameter) {
		Map<String,String> config=ConfigUtil.readAllProperties(CN_FILENAME);//获取连接网址配置
		long result=0L;
		if(parameter!=null&&!"".equals(parameter)){
			result=Long.parseLong(config.get(parameter));
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	public String deleteContract(Long instanceid) {
		String info = "";
		boolean flag = false;
		try {
			HashMap<String,Object> hash = DemAPI.getInstance().getFromData(instanceid, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			if(hash!=null){
				Long dataId=Long.parseLong(hash.get("ID").toString());
				String gsmc=hash.get("GSMC").toString();
				LogUtil.getInstance().addLog(dataId, "项目签约信息维护", gsmc+"删除项目签约信息。");
				// 删除项目签约信息
				flag = DemAPI.getInstance().removeFormData(instanceid);
				if (flag) {
					info = "success";
				} else {
					info = "删除失败！";
				}
			}else{
				info = "删除失败！";
			}
		} catch (Exception e) {
			info="删除失败！";
		}
		return info;
	}
	
	public List<HashMap<String, Object>> getList(String gsmc, String ssbm, String xylx, String xmlx, String qyrqbegin, String qyrqend, String rqtype) {
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		Long formid = getConfig("xmqyformid");
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		list = shanXiZqbContractManageDAO.getList(sysEngineIform,gsmc,ssbm,xylx,xmlx,qyrqbegin,qyrqend,"DESC",rqtype);
		return list;
	}

	public int getListSize(String gsmc, String ssbm, String xylx, String xmlx,String qyrqbegin, String qyrqend) {
		List<String> parameter=new ArrayList<String>();//存放参数
		StringBuffer sql = new StringBuffer("SELECT COUNT(1) CNUM FROM BD_ZQB_KH_BASE WHERE 1=1");
		// 公司名称
		if (gsmc != null && !gsmc.equals("")) {
			sql.append(" AND UPPER(GSMC) LIKE ?");
			parameter.add("%" + StringEscapeUtils.escapeSql(gsmc.toUpperCase().trim()) + "%");
		}
		// 所属部门
		if (ssbm != null && !ssbm.equals("")) {
			sql.append(" AND SSBM like ?");
			parameter.add("%" + StringEscapeUtils.escapeSql(ssbm.trim()) + "%");
		}
		// 协议类型
		if (xylx != null && !xylx.equals("")) {
			sql.append(" AND XYLX = ?");
			parameter.add(StringEscapeUtils.escapeSql(xylx.trim()));
		}
		// 项目类型
		if (xmlx != null && !xmlx.equals("")) {
			sql.append(" AND XMLX = ?");
			parameter.add(StringEscapeUtils.escapeSql(xmlx.trim()));
		}
		// 签约日期
		if (qyrqbegin != null && !qyrqbegin.equals("")) {
			sql.append(" AND TO_CHAR(QSXYRQ,'yyyy-mm-dd') >= ?");
			parameter.add(StringEscapeUtils.escapeSql(qyrqbegin.trim()));
		}
		if (qyrqend != null && !qyrqend.equals("")) {
			sql.append(" AND TO_CHAR(QSXYRQ,'yyyy-mm-dd') <= ?");
			parameter.add(StringEscapeUtils.escapeSql(qyrqend.trim()));
		}
		int count = shanXiZqbContractManageDAO.getListSize(sql.toString(),parameter);
		return count;
	}

	@SuppressWarnings("rawtypes")
	public boolean removQianYue(Long instanceid) {
		HashMap fromData = DemAPI.getInstance().getFromData(instanceid);
		boolean removeFormData=false;
		Long dataId = Long.parseLong(fromData.get("ID").toString());
		String gsmc = fromData.get("GSMC").toString();
		removeFormData = DemAPI.getInstance().removeFormData(instanceid);
		if(removeFormData){
			LogUtil.getInstance().addLog(dataId, "项目签约信息维护", "删除项目签约信息："+gsmc);
		}
		return removeFormData;
	}

	public void expProjectList(HttpServletResponse response, String gsmc,
			String ssbm, String xylx, String xmlx, String qyrqbegin,
			String qyrqend, String rqtype) {
		Long formid = getConfig("xmqyformid");
		SysEngineIform sysEngineIform = sysEngineIFormDAO.getSysEngineIformModel(formid);
		List<HashMap<String, Object>> list = shanXiZqbContractManageDAO.getList(sysEngineIform, gsmc, ssbm, xylx, xmlx, qyrqbegin, qyrqend, "ASC" ,rqtype);
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("项目签约汇总表");
		/*int defaultheight=30*20;
		sheet.setDefaultRowHeight((short)defaultheight);*/
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 10);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		
		HSSFFont contentfont = wb.createFont();
		contentfont.setFontName("宋体");
		contentfont.setFontHeightInPoints((short) 10);// 字体大小
		
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style1.setFillForegroundColor((short) 22);
		style1.setFillPattern((short) 1);
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setFillForegroundColor(HSSFColor.WHITE.index);
		style1.setFont(headfont);
		style1.setWrapText(true);
		
		HSSFCellStyle style2 = wb.createCellStyle();
		//style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style2.setFillForegroundColor((short) 22);
		style2.setFillPattern((short) 1);
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFont(headfont);
		style2.setWrapText(true);
		
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setFillForegroundColor((short) 22);
		style3.setFillPattern((short) 1);
		style3.setBorderBottom((short) 1);
		style3.setBorderLeft((short) 1);
		style3.setBorderRight((short) 1);
		style3.setBorderTop((short) 1);
		style3.setFillForegroundColor(HSSFColor.WHITE.index);
		style3.setFont(contentfont);
		style3.setWrapText(true);
		
		HSSFCellStyle style4 = wb.createCellStyle();
		//style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setFillForegroundColor((short) 22);
		style4.setFillPattern((short) 1);
		style4.setBorderBottom((short) 1);
		style4.setBorderLeft((short) 1);
		style4.setBorderRight((short) 1);
		style4.setBorderTop((short) 1);
		style4.setFillForegroundColor(HSSFColor.WHITE.index);
		style4.setFont(contentfont);
		style4.setWrapText(true);
		
		int z = 0;
		int m = 0;
		HSSFRow row = sheet.createRow((int) m++);
		HSSFCell cell = row.createCell((short) z++);
		cell.setCellValue("序号");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("月份");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("公司名称");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("协议类型");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("项目类型");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("协议金额（万元）");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("督导金额（万元）");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("收到协议日期");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("签署协议日期");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("流程结束日期");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("部门");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("经办人（流程提交人）");
		cell.setCellStyle(style1);
		cell = row.createCell((short) z++);
		cell.setCellValue("付款节点");
		cell.setCellStyle(style1);
		HashMap<String, Object> hashMap = null;
		for (int i = 0; i < list.size(); i++) {
			hashMap = list.get(i);
			String monthrq = hashMap.get("MONTHRQ").toString();
			String yearrq = hashMap.get("YEAR").toString();
			z=0;
			row = sheet.createRow((int) m++);
			cell = row.createCell((short) z++);
			cell.setCellValue(i+1);
			cell.setCellStyle(style3);
			cell = row.createCell((short) z++);
			cell.setCellValue(monthrq);
			cell.setCellStyle(style3);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("GSMC").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("XYLX").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("XMLX").toString());
			cell.setCellStyle(style3);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("XYJE")==null?"":hashMap.get("XYJE").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			StringBuffer ddjeContent=new StringBuffer();
			if(hashMap.get("DDJE")!=null){
				String ddje = hashMap.get("DDJE").toString();
				String[] str = qj2bj(ddje).split(";");
				for (int j = 0; j < str.length; j++) {
					if(j==str.length-1){
						ddjeContent.append(str[j].toString()+";");
					}else{
						ddjeContent.append(str[j].toString()+";\n");
					}
				}
			}
			cell.setCellValue(ddjeContent.toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("SDXYRQ")==null?"":hashMap.get("SDXYRQ").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("QSXYRQ")==null?"":hashMap.get("QSXYRQ").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("LCJSRQ")==null?"":hashMap.get("LCJSRQ").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("SSBM")==null?"":hashMap.get("SSBM").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("JBR")==null?"":hashMap.get("JBR").toString());
			cell.setCellStyle(style4);
			cell = row.createCell((short) z++);
			String fkjd = hashMap.get("FKJD")==null?"":hashMap.get("FKJD").toString();
			StringBuffer content=new StringBuffer();
			if(!fkjd.equals("")){
				String[] str = qj2bj(fkjd).split("\\d*、");
				for (int j = 1; j < str.length; j++) {
					if(j==str.length-1){
						content.append((j)+"、"+str[j].toString());
					}else{
						content.append((j)+"、"+str[j].toString()+"\n");
					}
				}
			}
			cell.setCellValue(content.toString());
			cell.setCellStyle(style4);
			if(i!=list.size()-1){
				HashMap<String, Object> nextHashMap= list.get(i+1);
				String nextmonthrq = nextHashMap.get("MONTHRQ").toString();
				String nextyearrq = nextHashMap.get("YEAR").toString();
				if(!monthrq.equals(nextmonthrq)){
					z=0;
					row = sheet.createRow((int) m);
					cell = row.createCell((short) z++);
					cell.setCellValue(monthrq+"月合计");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue(hashMap.get("MXYJE")==null?"":hashMap.get("MXYJE").toString());
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					sheet.addMergedRegion(new Region(m, (short) 0, m, (short) 4));
					m++;
				}
				if(!yearrq.equals(nextyearrq)){
					z=0;
					row = sheet.createRow((int) m);
					cell = row.createCell((short) z++);
					cell.setCellValue(yearrq+"年度合计");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style1);
					cell = row.createCell((short) z++);
					cell.setCellValue(hashMap.get("YXYJE")==null?"":hashMap.get("YXYJE").toString());
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					cell = row.createCell((short) z++);
					cell.setCellValue("");
					cell.setCellStyle(style2);
					sheet.addMergedRegion(new Region(m, (short) 0, m, (short) 4));
					m++;
				}
			}
		}
		if(hashMap!=null){
			String monthrq = hashMap.get("MONTHRQ").toString();
			String yearrq = hashMap.get("YEAR").toString();
			z=0;
			row = sheet.createRow((int) m);
			cell = row.createCell((short) z++);
			cell.setCellValue(monthrq+"月合计");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("MXYJE")==null?"":hashMap.get("MXYJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			sheet.addMergedRegion(new Region(m, (short) 0, m, (short) 4));
			m++;
			z=0;
			row = sheet.createRow((int) m);
			cell = row.createCell((short) z++);
			cell.setCellValue(yearrq+"年度合计");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style1);
			cell = row.createCell((short) z++);
			cell.setCellValue(hashMap.get("YXYJE")==null?"":hashMap.get("YXYJE").toString());
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell((short) z++);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			sheet.addMergedRegion(new Region(m, (short) 0, m, (short) 4));
		}
		sheet.autoSizeColumn((short)1);
		sheet.autoSizeColumn((short)2);
		sheet.autoSizeColumn((short)3);
		sheet.autoSizeColumn((short)4);
		sheet.autoSizeColumn((short)5);
		sheet.autoSizeColumn((short)6);
		sheet.autoSizeColumn((short)7);
		sheet.autoSizeColumn((short)8);
		sheet.autoSizeColumn((short)9);
		sheet.autoSizeColumn((short)10);
		sheet.autoSizeColumn((short)11);
		sheet.autoSizeColumn((short)12);
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("项目签约汇总表.xls");
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
					
				}
			}

		}
	}
	
	public String qj2bj(String src) {  
        if (src == null) {  
            return src;  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < src.length(); i++) {  
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内  
                buf.append((char) (ca[i] - CONVERT_STEP));  
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格  
                buf.append(DBC_SPACE);  
            } else { // 不处理全角空格，全角！到全角～区间外的字符  
                buf.append(ca[i]);  
            }  
        }  
        return buf.toString();  
    }
}
