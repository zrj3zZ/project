package com.ibpmsoft.project.zqb.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.dao.DdqylDao;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.commons.util.DBUTilNew;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.constant.EngineConstants;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.plugs.notice.util.NoticeUtil;
import com.iwork.sdk.DemAPI;

public class DdqyService {
	private DdqylDao ddqyDao;

	/**
	 * 督导签约导入
	 * @param upfile
	 * @param qydemid
	 * @param rzdemid
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings(value={"unchecked", "deprecation","unused","rawtypes"})
	public String doExcelImp(File upfile,Long qydemid,Long rzdemid) throws Exception{
		String khdemId=DBUtil.getString("SELECT ID FROM SYS_DEM_ENGINE WHERE TITLE ='客户主数据维护'", "ID");
		String userid = UserContextUtil.getInstance().getCurrentUserId();
	    UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
	    OrgUser user = uc.get_userModel();
	    String username=userid+"["+user.getUsername()+"]";
	    String msg = "success";
	    try {
	      InputStream is = new FileInputStream(upfile);
	      HSSFWorkbook hwk = new HSSFWorkbook(is);
	      HSSFSheet sheet = hwk.getSheetAt(0);

	      int rowIndex = 0;
	      HSSFRow row = sheet.getRow(rowIndex);
	      HSSFCell cell = null;
	      row = sheet.getRow(rowIndex);
	      Hashtable fields = new Hashtable();
	      for (short i = 0; i < row.getLastCellNum(); i = (short)(i + 1)) {
	        cell = row.getCell(i);
	        if ((cell != null) && (cell.getCellType() != 0))
	        {
	          String fieldName = "";
	          String temp = cell.getStringCellValue();
	          if (!"".equals(temp)) {
	            if (temp.indexOf("<") > 0)
	              fieldName = temp.substring(0, cell.getStringCellValue().indexOf("<"));
	            else {
	              fieldName = temp;
	            }
	            fields.put(new Short(i), fieldName);
	          }
	        }
	      }
	      if (fields.size() < 1) {
	        return "typeerror";
	      }
	      rowIndex++;
	      int dataNum = 0;
	      int erroecount = 0;
	      int count =1;
	      for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
	        row = sheet.getRow(i);
	        StringBuffer bindData = new StringBuffer();
	        HashMap rowdata = new HashMap();
	        for (short column = 0; column < row.getLastCellNum(); column = (short)(column + 1)) {
	          cell = row.getCell(column);
	          String value = "";
	          Object ofieldName = fields.get(new Short(column));
	          if (ofieldName != null)
	          {
	            String fieldName = ((String)ofieldName).toUpperCase();
	            if (cell == null) {
	              value = "";
	            }
	            else if (cell.getCellType() == 0)
	            {
	              if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                value = UtilDate.dateFormat(cell.getDateCellValue());
	              } else {
	                value = Double.toString(cell.getNumericCellValue());

	                if (value != null) {
	                  if (value.toUpperCase().indexOf("E") > 0)
	                  {
	                    value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
	                    value = value.replaceAll(",", "");
	                  }

	                  if ((value.indexOf(".0") > 0) && (value.lastIndexOf("0") == value.length() - 1))
	                    value = value.substring(0, value.length() - 2);
	                }
	              }
	            }
	            else if (cell.getCellType() == 2) {
	              try {
	                value = String.valueOf(cell.getNumericCellValue());
	              } catch (Exception e) {
	                value = String.valueOf(cell.getRichStringCellValue());
	              }
	            } else {
	              value = cell.getStringCellValue();
	              if (value == null) {
	                value = "";
	              }
	            }
	            value = value.trim();
	            rowdata.put(fieldName, value);
	          }
	        }
	        if (fields.size() >= 1){
	        	Date d = new Date();  
	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
	        	String GXSJ = sdf.format(d);  
	        	String nyr =s.format(d);
	        	List mobilelist=new ArrayList();
	        	String KHBH = "";
	        	String KHMC = "";
	        	boolean flag=false;
	        	String rz=rowdata.get("RZ")==null?"":rowdata.get("RZ").toString().replace(" ", "").replace("\n", "");
	        	String companyname=rowdata.get("COMPANYNAME")==null?"":rowdata.get("COMPANYNAME").toString().replace(" ", "");
	        	if(companyname!=""){
	        		Map params = new HashMap<Integer,String>();
	        		params.put(1, "%" + companyname + "%");
	        		List lables = new ArrayList();
	    			lables.add("CUSTOMERNAME");
	    			lables.add("CUSTOMERNO");
	    			String sql = "select customername,customerno from bd_zqb_kh_base  where customername like ?";
	    			mobilelist = com.iwork.commons.util.DBUtil.getDataList(lables, sql, params);
	    			if(mobilelist!=null && mobilelist.size()>0){
	    				KHBH=((HashMap)mobilelist.get(0)).get("CUSTOMERNO").toString();
	    				KHMC=((HashMap)mobilelist.get(0)).get("CUSTOMERNAME").toString();
	    				flag=true;
	    			}
	        	}
	        	boolean khflag=false;
	        	if(companyname!=""){
	        		khflag=true;
	        		if(!flag){
						int Seq = 0;				
						String str = NoticeUtil.getInstance().GetCustomerNo();
						String[] strArr = str.split(",");
						KHBH = strArr[0];
						Seq = Integer.parseInt(strArr[1]);
						KHMC=companyname;
						String KHJC=rowdata.get("KHJC")==null?"":rowdata.get("KHJC").toString().replace(" ", "");
						HashMap khdata = new HashMap();
						khdata.put("CREATEUSER",userid);
						khdata.put("CREATEDATE",nyr);
						khdata.put("CUSTOMERNAME",KHMC);
						khdata.put("CUSTOMERNO",KHBH);
						khdata.put("STATUS","有效");
						khdata.put("YGP","已挂牌");
						khdata.put("ZRFS","挂牌企业");
						khdata.put("USERID",userid);
						khdata.put("ZHXGR",username);
						khdata.put("ZHXGSJ",GXSJ);
						khdata.put("ZQJC", KHJC);
						Long khinstanceId = DemAPI.getInstance().newInstance(Long.parseLong(khdemId), userid);
						khflag=DemAPI.getInstance().saveFormData(Long.parseLong(khdemId), khinstanceId, khdata, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);	        	
						NoticeUtil.getInstance().UpdateSeqValue("BPM:0", Seq);
		        	}
	        	}
	        	
	        	if(khflag){
	        		String KSTXRQ=rowdata.get("LCBH")==null?"":rowdata.get("LCBH").toString().replace(" ", "");
	        		String str2="";
	        		if(KSTXRQ != null && !"".equals(KSTXRQ)){
	        			 if(KSTXRQ.indexOf("工作日")!=-1){
	        				 for(int j=0;j<KSTXRQ.length();j++){
	 	        				if(KSTXRQ.charAt(j)>=48 && KSTXRQ.charAt(j)<=57){
	 	        					str2+=KSTXRQ.charAt(j);
	 	        				}
	 	        			}
	        			 }
	        			
	        		}
	        		
	        		rowdata.remove("RZ");
	        		rowdata.remove("KHJC");
	        		rowdata.remove("LCBH");
	        		if(!"".equals(str2)){
	        			rowdata.put("LCBH", str2);
	        		}
		        	rowdata.put("COMPANYNO", KHBH);
		        	rowdata.put("COMPANYNAME", KHMC);
		        	rowdata.put("USERID", userid);
		        	rowdata.put("GXSJ", GXSJ);
		        	rowdata.put("CJR", user.getUsername());
		        	Long qyinstanceId = DemAPI.getInstance().newInstance(qydemid, userid);
		        	boolean isadd = DemAPI.getInstance().saveFormData(qydemid, qyinstanceId, rowdata, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
			        if(!isadd) {
			        	
			        }else{
			        	boolean rzisadd=false;
			        	HashMap qymap=DemAPI.getInstance().getFromData(qyinstanceId);
			        	if(rz!=""){
			        		String[] strs = rz.split("，");
				        	if(strs!=null && strs.length>0){
				        		HashMap rzdata=new HashMap();
				        		rzdata.put("CUSTOMERNAME",KHMC);
				        		rzdata.put("CUSTOMERNO",KHBH);
				        		rzdata.put("DEPARTMENTID",qymap.get("ID"));
				        		rzdata.put("XYLX",userid);
				        		rzdata.put("LRR",user.getUsername());
				        		rzdata.put("CJSJ",GXSJ);
				        		for (int j = 0; j < strs.length; j++) {
				        			String[] rzxx=strs[j].split("--");
				        			String rzsj=rzxx[0];
				        			String rzje=rzxx[1];
				        			rzdata.put("DZRQ",rzsj);
					        		rzdata.put("DZJE",rzje);
					        		Long rzinstanceId = DemAPI.getInstance().newInstance(rzdemid, userid);
					        		rzisadd= DemAPI.getInstance().saveFormData(rzdemid, rzinstanceId, rzdata, false, EngineConstants.SYS_INSTANCE_TYPE_DEM);
								}
				        		if(!rzisadd){
				        			erroecount++;
				        		}
				        	}
			        	}
			        }
	        	}else{
	        		erroecount++;
	        	}
	        	dataNum++;
	        	count++;
	        }
	      }
	      if (dataNum == 0) {
	        msg = "error";
	      }
	      if (erroecount > 0)
	        msg = "第" + erroecount + "条数据导入异常!";
	    }
	    catch (FileNotFoundException e) {
	      msg = "error";
	      e.printStackTrace(System.err);
	    } catch (Exception e) {
	      msg = "error";
	      e.printStackTrace(System.err);
	    }
	    return msg;
	  }
	/**
	 * 督导签约模板下载
	 */
	public void ddqyMbupload(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String filepath = request.getSession().getServletContext().getRealPath("/");
		filepath=filepath+"\\iwork_file\\督导签约导入模板.xls";
		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("督导签约导入模板.xls"));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			
		}
}
	/**
	 * 入账记录数
	 * @param id
	 * @return
	 */
	public int getrzListSize(String id) {
		return ddqyDao.getrzListSize(id).size();
	}
	/**
	 * 入账列表
	 * @param id
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getrzList(String id, int pageSize, int pageNumber) {
		return ddqyDao.getrzList(id, pageSize, pageNumber);
	}
	/**
	 * 督导签约列表
	 * @param khmc
	 * @param kssj
	 * @param jssj
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<HashMap> getddList(String khmc,String kssj,String jssj, int pageSize, int pageNumber){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(kssj!=null&&!kssj.equals(""))
				 stdate= sdf.parse(kssj);
			 if(jssj!=null&&!jssj.equals(""))
				 endate=  sdf.parse(jssj);
		} catch (ParseException e) {
		}
		return ddqyDao.getQyList(khmc, stdate, endate, pageSize, pageNumber);
	}
	/**
	 * 督导签约记录数
	 * @param khmc
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public int getddListSize(String khmc,String kssj,String jssj){
		return ddqyDao.getQyListSize(khmc, kssj, jssj).size();
	}
	/**
	 * 最后修改时间
	 * @param id
	 * @return
	 */
	public String getDdqy(int id) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();
		sql.append("  select s.extend3,s.gxsj from BD_XP_ZYDDSXB s where to_char(s.id)=? ");
		parameter.add(String.valueOf(id));
		List<HashMap<String,String>> customerAutoDataList = getqyxxList(sql.toString(),parameter);
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(customerAutoDataList);
		jsonHtml.append(json.toString());
		return jsonHtml.toString();
	}
	/**
	 * 最后修改时间
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public List<HashMap<String,String>> getqyxxList(String sql,List<String> parameter){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		list = ddqyDao.getqyxxList(sql,parameter);
		return list;
	}
	/**
	 * 客户名称，编号
	 * @param id
	 * @return
	 */
	public String getZbkh(int id) {
		StringBuffer sql = new StringBuffer();
		List<String> parameter=new ArrayList<String>();
		sql.append("    select s.companyname,s.companyno from BD_XP_ZYDDSXB s where to_char(s.id)=? ");
		parameter.add(String.valueOf(id));
		List<HashMap<String,String>> customerAutoDataList = getZbkhList(sql.toString(),parameter);
		StringBuffer jsonHtml = new StringBuffer();
		JSONArray json = JSONArray.fromObject(customerAutoDataList);
		jsonHtml.append(json.toString());
		return jsonHtml.toString();
	}
	/**
	 * 客户名称，编号
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public List<HashMap<String,String>> getZbkhList(String sql,List<String> parameter){
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		list = ddqyDao.getZbkhList(sql,parameter);
		return list;
	}
	public String jySave(String id,String jyyy) {
		Map params = new HashMap();
		params.put(1, jyyy);
		params.put(2, id);
		String sql = " update BD_XP_ZYDDSXB set lcbs= ?  where to_char(id) = ?  "; 
		DBUTilNew.update(sql,params);
		params = new HashMap();
		params.put(1, id);
		DBUTilNew.update("  update BD_XP_ZYDDSXB set EXTEND5='1' where to_char(id) = ? ",params);
		return "";
	}
	public DdqylDao getDdqyDao() {
		return ddqyDao;
	}

	public void setDdqyDao(DdqylDao ddqyDao) {
		this.ddqyDao = ddqyDao;
	}

	public void thxmexportexcl(HttpServletResponse response,String khmc,String kssj,String jssj, int pageSize, int pageNumber){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("督导签约");
		
	
		HSSFRow row = sheet.createRow((int) 0);
		row.setHeightInPoints(30);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setBorderTop((short) 1);
		style.setWrapText(true);
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderBottom((short) 1);
		style1.setBorderLeft((short) 1);
		style1.setBorderRight((short) 1);
		style1.setBorderTop((short) 1);
		style1.setWrapText(true);
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setBorderBottom((short) 1);
		style2.setBorderLeft((short) 1);
		style2.setBorderRight((short) 1);
		style2.setBorderTop((short) 1);
		style2.setWrapText(true);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCellStyle style3 = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style3.setFont(font);
		HSSFFont font2 = wb.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font2);
		//yearCell.setCellStyle(style3);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("客户名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("签约日期");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 2);
		cell.setCellValue("付款节点");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 3);
		cell.setCellValue("跟进人");
		cell.setCellStyle(style);
		
	
		
		cell = row.createCell((short) 4);
		cell.setCellValue("已入账（万元）");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("最后入账时间");
		cell.setCellStyle(style);
	
	
	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date stdate=null;
		Date endate=null;
		try {
			 if(kssj!=null&&!kssj.equals(""))
				 stdate= sdf.parse(kssj);
			 if(jssj!=null&&!jssj.equals(""))
				 endate=  sdf.parse(jssj);
		} catch (ParseException e) {
		}
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		//获取当前用户权限
		List list = ddqyDao.getQyList(khmc, stdate, endate, pageSize, pageNumber);
		int n = 1;
		if (list == null) {
			return;
		}
		int m = 0;
		for (int i = 0; i < list.size(); i++) {

				Map map = (HashMap) list.get(i);
				row = sheet.createRow((int) n++);
				// 第四步，创建单元格，并设置值
				HSSFCell cell1 = row.createCell((short) 0);
				cell1.setCellValue(map.get("zbkhmc").toString());
				cell1.setCellStyle(style2);
				HSSFCell cell2 = row.createCell((short) 1);
				cell2.setCellValue(map.get("qyrq").toString());
				cell2.setCellStyle(style2);
				
				
				
				
				HSSFCell cell3 = row.createCell((short) 2);
				cell3.setCellValue(map.get("fkjd").toString());
				cell3.setCellStyle(style2);
				HSSFCell cell4 = row.createCell((short) 3);
				cell4.setCellValue(map.get("gjrxm").toString());
				cell4.setCellStyle(style2);
				
			
				
				HSSFCell cell6 = row.createCell((short) 4);
				cell6.setCellValue(map.get("zrzje").toString());
				cell6.setCellStyle(style2);
				HSSFCell cell7 = row.createCell((short) 5);
				cell7.setCellValue(map.get("zhsj").toString());
				cell7.setCellStyle(style2);
				
				
				m++;

		}
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 4500);
		sheet.setColumnWidth(2, 12000);
		sheet.setColumnWidth(3, 4500);
		sheet.setColumnWidth(4, 4500);
		sheet.setColumnWidth(5, 4500);
	
		
		OutputStream out1 = null;
		// 第六步，将文件存到指定位置
		try {
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("督导签约.xls");
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			out1 = new BufferedOutputStream(response.getOutputStream());
			wb.write(out1);

		} catch (Exception e) {
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

}
