package com.iwork.core.organization.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import net.sf.json.JSONArray;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.app.conf.SystemConfig;
import com.iwork.app.weixin.core.tools.SendMessageUtil;

import com.iwork.app.weixin.org.service.WeiXinOrgService;
import com.iwork.app.login.cache.LoginCache;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.commons.util.ShaSaltUtil;
import com.iwork.commons.util.UtilDate;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.dao.OrgCompanyDAO;
import com.iwork.core.organization.dao.OrgDepartmentDAO;
import com.iwork.core.organization.dao.OrgRoleDAO;
import com.iwork.core.organization.dao.OrgUserDAO;
import com.iwork.core.organization.dao.OrgUserMapDAO;
import com.iwork.core.organization.model.OrgCompany;
import com.iwork.core.organization.model.OrgDepartment;
import com.iwork.core.organization.model.OrgRole;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.model.OrgUserMap;
import com.iwork.core.organization.tools.PWDTools;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.security.SecurityUtil;
import com.iwork.eaglesearch.constant.EaglesSearchConstant;
import com.iwork.eaglesearch.factory.EaglesSearchFactory;
import com.iwork.eaglesearch.impl.EaglesSearchOrgInfoImpl;
import com.iwork.sdk.MessageAPI;

public class OrgUserService {
	private OrgCompanyDAO orgCompanyDAO;
	private OrgDepartmentDAO orgDepartmentDAO;
	private OrgUserDAO orgUserDAO;
	private OrgRoleDAO orgRoleDAO;
	private OrgUserMapDAO orgUserMapDAO;
	private WeiXinOrgService winxinOrgService;
	private EaglesSearchOrgInfoImpl esfdi  = null;
	private static Logger logger = Logger
			.getLogger(OrgUserService.class);
	/**
	 * 添加用户
	 * @param obj
	 */
	public void addBoData(OrgUser obj) {
		if(obj.getCompanyid()==null){
			if(obj.getDepartmentid()!=null){
				OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(obj.getDepartmentid());
				obj.setCompanyid(Long.parseLong(orgDepartment.getCompanyid()));
				OrgCompany orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
				obj.setCompanyname(orgCompany.getCompanyname());
			}
		}
		orgUserDAO.addBoData(obj);
		if(obj.getId()!=null){
			try{ 
				//添加微信账号
				if(SystemConfig._weixinConf.getServer().equals("on")){
//					winxinOrgService.addUser(obj);
				}
				//添加鹰眼索引
				if(esfdi==null){
					esfdi  =  (EaglesSearchOrgInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_ORGINFO_INDEX);
				}
				if(esfdi!=null){
					esfdi.addDocument(obj);
				}
			}catch(Exception e){
				logger.error(e,e);
			}
		}
		
		
	}
	
	/**
	 * 删除用户
	 * @param userid
	 */
	public boolean deleteBoData(OrgUser model) {
		List usermaplist =orgUserMapDAO.getOrgUserMapList(model.getUserid());
		if(usermaplist!=null&&usermaplist.size()>0){
			return false;
		}else{
			orgUserDAO.deleteBoData(model);
			try{
				//添加微信账号
				if(SystemConfig._weixinConf.getServer().equals("on")){
//					winxinOrgService.removeUser(model.getUserid());
				}
				
				//添加鹰眼索引
				if(esfdi==null){
					esfdi  =  (EaglesSearchOrgInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_ORGINFO_INDEX);
				}
				if(esfdi!=null){
					esfdi.delDocuemnt(model);
				}
			}catch(Exception e){
				logger.error(e,e);
			}
			return true;
		}
	}
	
	public void exportOrgExcelTemplate(HttpServletResponse response){
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("用户导入模板");
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		HSSFRow titleRow = sheet.createRow((short) 0);
		HSSFCell cell = null;
		String[] list = {"COMPANYID<组织ID>","COMPANYNAME<组织名称>","DEPARTEMNTID<部门ID>","DEPARTEMNTNAME<部门名称>","USERID<用户账号>","USERNAME<用户姓名>","ROLEID<角色ID>","ISOWNER<是否是部门管理者>","USERTYPE<用户类型>","USERNO<用户编号>","START_DATE<开始日期>","END_DATE<开始日期>","" +
				"OFFICETEL<办公室电话>","OFFICEFAX<办公室传真>","HOMETEL<家庭电话>","MOBILE<手机>","EMAIL<邮件>","QQ<QQ账号>","WEIXIN<微信>","EXTEND1<扩展参数1>","EXTEND2<扩展参数2>","EXTEND3<扩展参数3>","EXTEND4<扩展参数4>","EXTEND5<扩展参数5>","EXTEND5<扩展参数5>","EXTEND6<扩展参数6>","EXTEND7<扩展参数7>","EXTEND8<扩展参数8>","EXTEND9<扩展参数9>","EXTEND10<扩展参数10>"};
		if(titleRow!=null){
			cell = titleRow.createCell(0);
			cell.setCellStyle(style);
			for(int i=0;i<list.length;i++){
				String value = list[i];
				int column = i+1;
				cell = titleRow.createCell(column);
				cell.setCellType(1);
				cell.setCellValue(value);
				cell.setCellStyle(style);
				short colLength = (short) ((value.length()) * 256 * 2);
				if (sheet.getColumnWidth(column) < colLength) {
					sheet.setColumnWidth(column, colLength);
				}
			}
		}
		this.downExcel(wb,"USER_TEMPLATE", response);
	}
	
	
	/**
	 * 
	 * @param upFile
	 * @return
	 */
	public String doExcelImp(File file){
		StringBuffer msg = new StringBuffer();
		try {
				InputStream is = new FileInputStream(file);
				HSSFWorkbook hwk = new HSSFWorkbook(is);
				HSSFSheet sheet = hwk.getSheetAt(0);// 得到book第一个工作薄sheet
				// 第3行定义了导入动作
				int rowIndex = 0;
				HSSFRow row = sheet.getRow(rowIndex);
				HSSFCell cell = null;
					row = sheet.getRow(rowIndex);
					Hashtable fields = new Hashtable();
					for (short i = 0; i < row.getLastCellNum(); i++) {
						cell = row.getCell(i);
						if(cell==null||cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
							continue;
						}
						String fieldName = "";
						String temp = cell.getStringCellValue();
						if (temp.indexOf("<") > 0) {
							fieldName = temp.substring(0, cell.getStringCellValue().indexOf("<"));
						} else { 
							fieldName = temp;
						}
						fields.put(new Short(i), fieldName);
					}
					if(fields.size()<1){
						msg.append("导入EXCEL字段信息错误");
						return msg.toString();
					}
					
					// 循环excel的数据
					rowIndex++;
					StringBuffer bindData;
					int dataNum = 0;
					int erroecount = 0;   //导入异常行数
					for (int i = rowIndex; i <= sheet.getLastRowNum(); i++) {
						row = sheet.getRow(i);
						bindData = new StringBuffer();
						HashMap rowdata = new HashMap();
						// 循环列
						for (short column = 0; column < row.getLastCellNum(); column++) {
							cell = row.getCell(column);
							String value = "";
							Object ofieldName = fields.get(new Short(column));
							if (ofieldName == null)
								continue;
							String fieldName = ((String) ofieldName).toUpperCase();
							if (cell == null) {
								value = "";
							} else {
								if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
									if (HSSFDateUtil.isCellDateFormatted(cell)) {// 日期
										value = UtilDate.dateFormat(cell.getDateCellValue());
									} else {
										value = Double.toString(cell.getNumericCellValue());
										// 排除excel的幂
										if (value != null) {
											if (value.toUpperCase().indexOf("E") > 0) {
												//  解决常整型数据出现科学计数法问题
												value = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
												value = value.replaceAll(",", "");
											}
											// 小数位数
											if (value.indexOf(".0") > 0 && value.lastIndexOf("0") == value.length() - 1) {
												value = value.substring(0, value.length() - 2);
											} 
										} 
									}
								}else if(cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
									try {
										 value = String.valueOf(cell.getNumericCellValue());
									} catch (Exception e) {
										 value = String.valueOf(cell.getRichStringCellValue());
										 logger.error(e,e);
									}
								} else {
									value = cell.getStringCellValue();
									if (value == null)
										value = "";
								}
							}
							value = value.trim();
							rowdata.put(fieldName, value);
						}
						if(fields.size()<1){
							continue;
						}else{
							//账号
							OrgUser model = null;
							if(rowdata.get("USERID")!=null){
									String userid = ObjectUtil.getString(rowdata.get("USERID"));
									model = orgUserDAO.getUserModel(userid);
							}
							if(model==null){
								 model = new OrgUser();
							}
							
							//组织ID
							if(rowdata.get("COMPANYID")!=null){
								try{
									Long companyid = Long.parseLong(ObjectUtil.getString(rowdata.get("COMPANYID")));
									model.setCompanyid(companyid);
								}catch(Exception e){
									msg.append("第").append(i).append("行组织ID列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
									continue;
								}
							}else{
								msg.append("第").append(i).append("行组织ID列数据格式异常，导入失败").append("\n");
								continue;
							}
							//组织名称
							if(rowdata.get("COMPANYNAME")!=null){
								try{
									String companyname = ObjectUtil.getString(rowdata.get("COMPANYNAME"));
									model.setCompanyname(companyname);
								}catch(Exception e){
									msg.append("第").append(i).append("行“组织名称”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
									continue;
								}
							}else{
								msg.append("第").append(i).append("行““组织名称”列数据格式异常，导入失败").append("\n");
					continue;
							}
							
							//部门ID
							if(rowdata.get("DEPARTEMNTID")!=null){
								try{
									Long departmentid = Long.parseLong(ObjectUtil.getString(rowdata.get("DEPARTEMNTID")));
									model.setDepartmentid(departmentid);
								}catch(Exception e){
									msg.append("第").append(i).append("行部门ID列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
									continue;
								}
							}else{
								msg.append("第").append(i).append("行部门ID列数据格式异常，导入失败").append("\n");
								continue;
							}
							//部门名称
							if(rowdata.get("DEPARTEMNTNAME")!=null){
								try{
									String departmentname = ObjectUtil.getString(rowdata.get("DEPARTEMNTNAME"));
									model.setDepartmentname(departmentname);
								}catch(Exception e){
									msg.append("第").append(i).append("行“部门名称”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
									continue;
								}
							}else{
								msg.append("第").append(i).append("行“部门名称”列数据格式异常，导入失败").append("\n");
								continue;
							}
							
							//账号
							if(rowdata.get("USERID")!=null){
								try{
									String userid = ObjectUtil.getString(rowdata.get("USERID"));
									model.setUserid(userid);
								}catch(Exception e){
									msg.append("第").append(i).append("行用户账号列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
									continue;
								}
							}else{
								msg.append("第").append(i).append("行用户账号列数据格式异常，导入失败").append("\n");
								continue;
							}
							//姓名
							if(rowdata.get("USERNAME")!=null){
								try{
									String username = ObjectUtil.getString(rowdata.get("USERNAME"));
									model.setUsername(username);
								}catch(Exception e){
									msg.append("第").append(i).append("行用户姓名列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
									continue;
								}
							}else{
								msg.append("第").append(i).append("行用户姓名列数据格式异常，导入失败").append("\n");							
								continue;
							}
							//用户编号
							if(rowdata.get("USERNO")!=null){
								try{
									String userno = ObjectUtil.getString(rowdata.get("USERNO"));
									model.setUserno(userno);
								}catch(Exception e){
									msg.append("第").append(i).append("行用户编号列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}else{
								msg.append("第").append(i).append("行用户编号列数据格式异常，导入失败").append("\n");
							}
							if(rowdata.get("ROLEID")!=null){
								try{
									Long roleid = Long.parseLong(ObjectUtil.getString(rowdata.get("ROLEID")));
									model.setOrgroleid(roleid);
								}catch(Exception e){
									msg.append("第").append(i).append("行角色ID列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
									continue;
								}
							}else{
								model.setOrgroleid(new Long(1));
								msg.append("第").append(i).append("行”角色ID“设置默认值").append("\n");
							}
							//是否是部门管理者
							if(rowdata.get("ISOWNER")!=null){
								try{
									Long isowner = Long.parseLong(ObjectUtil.getString(rowdata.get("ISOWNER")));
									model.setIsmanager(isowner);
								}catch(Exception e){
									model.setIsmanager(new Long(0));
									msg.append("第").append(i).append("行“部门负责人”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}else{
								model.setIsmanager(new Long(0));
								msg.append("第").append(i).append("行”是否是部门负责人“设置默认值").append("\n");								
							}
							
							//是否是用户类型
							if(rowdata.get("USERTYPE")!=null){
								try{
									Long usertype = Long.parseLong(ObjectUtil.getString(rowdata.get("USERTYPE")));
									model.setUsertype(usertype);
								}catch(Exception e){
									msg.append("第").append(i).append("行”用户类型“列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}else{
								model.setUsertype(new Long(0));
								msg.append("第").append(i).append("行”用户类型“设置默认值").append("\n");
							}
							//是否是用户类型
							if(rowdata.get("USERTYPE")!=null){
								try{
									Long usertype = Long.parseLong(ObjectUtil.getString(rowdata.get("USERTYPE")));
									model.setUsertype(usertype);
								}catch(Exception e){
									msg.append("第").append(i).append("行”用户类型“列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}else{
								model.setUsertype(new Long(0));
								msg.append("第").append(i).append("行”用户类型“设置默认值").append("\n");
							}
							//开始日期
							if(rowdata.get("START_DATE")!=null){
								try{
									String start_date = ObjectUtil.getString(rowdata.get("START_DATE"));
									Date startdate = UtilDate.StringToDate(start_date, "yyyy-MM-dd");
									model.setStartdate(startdate);
								}catch(Exception e){
									msg.append("第").append(i).append("行”用户开始日期“设置默认值").append("\n");
									Date startdate = Calendar.getInstance().getTime();
									model.setStartdate(startdate);
									logger.error(e,e);
								}
							}else{
								Date startdate = Calendar.getInstance().getTime();
								model.setStartdate(startdate);
								msg.append("第").append(i).append("行”用户开始日期“设置默认值").append("\n");
							}
							//结束日期
							if(rowdata.get("END_DATE")!=null){
								try{
									String end_date = ObjectUtil.getString(rowdata.get("END_DATE"));
									Date enddate = UtilDate.StringToDate(end_date, "yyyy-MM-dd");
									model.setEnddate(enddate);
								}catch(Exception e){
									msg.append("第").append(i).append("行”用户开始日期“设置默认值").append("\n");
									Calendar c = Calendar.getInstance();
									c.add(Calendar.YEAR,10);
									Date enddate = c.getTime();
									model.setEnddate(enddate);
									logger.error(e,e);
								}
							}else{
								Calendar c = Calendar.getInstance();
								c.add(Calendar.YEAR,10);
								Date enddate = c.getTime();
								model.setEnddate(enddate);
								msg.append("第").append(i).append("行”用户开始日期“设置默认值").append("\n");
							}
							
							if(rowdata.get("OFFICETEL")!=null){
								try{
									String officetel = ObjectUtil.getString(rowdata.get("OFFICETEL"));
									model.setOfficetel(officetel);
								}catch(Exception e){
									msg.append("第").append(i).append("行“办公室电话”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							
							if(rowdata.get("OFFICEFAX")!=null){
								try{
									String officefax = ObjectUtil.getString(rowdata.get("OFFICEFAX"));
									model.setOfficefax(officefax);
								}catch(Exception e){
									msg.append("第").append(i).append("行“传真”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("HOMETEL")!=null){
								try{
									String hometel = ObjectUtil.getString(rowdata.get("HOMETEL"));
									model.setHometel(hometel);
								}catch(Exception e){
									msg.append("第").append(i).append("行“家庭电话”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("MOBILE")!=null){
								try{
									String mobile = ObjectUtil.getString(rowdata.get("MOBILE"));
									model.setMobile(mobile);
								}catch(Exception e){
									msg.append("第").append(i).append("行“手机”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							
							if(rowdata.get("EMAIL")!=null){
								try{
									String email = ObjectUtil.getString(rowdata.get("EMAIL"));
									model.setEmail(email);
								}catch(Exception e){
									msg.append("第").append(i).append("行“邮件”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							
							if(rowdata.get("QQ")!=null){
								try{
									String qq = ObjectUtil.getString(rowdata.get("QQ"));
									model.setQqmsn(qq);
								}catch(Exception e){
									msg.append("第").append(i).append("行“QQ”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							
							if(rowdata.get("WEIXIN")!=null){
								try{
									String weixin = ObjectUtil.getString(rowdata.get("WEIXIN"));
									model.setWeixinCode(weixin);
								}catch(Exception e){
									msg.append("第").append(i).append("行“微信”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							
							if(rowdata.get("EXTEND1")!=null){
								try{
									String extend1 = ObjectUtil.getString(rowdata.get("EXTEND1"));
									model.setExtend1(extend1);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数一”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
						
							if(rowdata.get("EXTEND2")!=null){
								try{
									String extend2 = ObjectUtil.getString(rowdata.get("EXTEND2"));
									model.setExtend2(extend2);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数2”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							
							if(rowdata.get("EXTEND3")!=null){
								try{
									String extend3 = ObjectUtil.getString(rowdata.get("EXTEND3"));
									model.setExtend3(extend3);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数3”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("EXTEND4")!=null){
								try{
									String extend4 = ObjectUtil.getString(rowdata.get("EXTEND4"));
									model.setExtend4(extend4);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数4”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("EXTEND5")!=null){
								try{
									String extend5 = ObjectUtil.getString(rowdata.get("EXTEND5"));
									model.setExtend5(extend5);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数5”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("EXTEND6")!=null){
								try{
									String extend6 = ObjectUtil.getString(rowdata.get("EXTEND6"));
									model.setExtend6(extend6);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数6”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("EXTEND7")!=null){
								try{
									String extend7 = ObjectUtil.getString(rowdata.get("EXTEND7"));
									model.setExtend7(extend7);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数7”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("EXTEND8")!=null){
								try{
									String extend8 = ObjectUtil.getString(rowdata.get("EXTEND8"));
									model.setExtend8(extend8);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数8”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("EXTEND9")!=null){
								try{
									String extend9 = ObjectUtil.getString(rowdata.get("EXTEND9"));
									model.setExtend9(extend9);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数9”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("EXTEND10")!=null){
								try{
									String extend10 = ObjectUtil.getString(rowdata.get("EXTEND10"));
									model.setExtend10(extend10);
								}catch(Exception e){
									msg.append("第").append(i).append("行“扩展参数10”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							if(rowdata.get("BOSSID")!=null){
								try{
									String bossid = ObjectUtil.getString(rowdata.get("BOSSID"));
									model.setBossid(bossid);
								}catch(Exception e){
									msg.append("第").append(i).append("行“上级领导账号”列数据格式异常，导入失败").append("\n");
									logger.error(e,e);
								}
							}
							model.setUserstate(new Long(0));
							if(model.getId()==null){
								orgUserDAO.addBoData(model);								
								msg.append(model.getUserid()+"<"+model.getUsername()+">账号添加成功\n");
							}else{
								orgUserDAO.updateBoData(model);
								msg.append(model.getUserid()+"<"+model.getUsername()+">账号更新成功\n");
							}
							if(model.getId()<0){
								erroecount++;
							}
							dataNum++;
						}
					} 
					if(dataNum==0){
						msg.append("error导入异常");
					}
					if(erroecount>0){
						msg.append("第").append(erroecount).append("条数据导入异常!");
					}
		} catch (FileNotFoundException e) {
			msg.append("error导入异常");
			logger.error(e,e);
		}catch (Exception e) {
			msg.append("error导入异常");
			logger.error(e,e);
		}
		if(msg.toString().equals("")){
			msg.append("导入成功！");
		}
		return msg.toString();
	}
	
	/**
	 * 把生成的excel导出到本地
	 * @param wb
	 * @param fileName
	 * @param response
	 */
	private void downExcel(HSSFWorkbook wb,String fileName,HttpServletResponse response){
		try {
			// 添加头信息，为"文件下载/另存为"对话框指定默认文件名 
			String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding(fileName+".xls");
			// 指定返回的是一个不能被客户端读取的流，必须被下载   
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-disposition", disposition);
			BufferedOutputStream bos = null; 
			bos = new BufferedOutputStream(response.getOutputStream());
			wb.write(bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			logger.error(e,e);
		}
	}
	/**
	 * 获得用户模型
	 * @param id
	 * @return
	 */
	public OrgUser getModel(String id) {
		return orgUserDAO.getModel(id);
	} 
	/**
	 * 获得用户模型
	 * @param userid
	 * @return
	 */
	public OrgUser getUserModel(String userid) {
		return orgUserDAO.getUserModel(userid);
	} 
	/**
	 * 获得指定部门下普通用户列表(手机号不为空)
	 * @param departmentid
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getDeptAllUserPhoneList(Long departmentid) {
		// TODO Auto-generated method stub
		return orgUserDAO.getDeptAllUserPhoneList(departmentid);
	}
	/**
	 * 获得用户列表
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getOrgUserList(int pageSize, int startRow) {
		// TODO Auto-generated method stub
		return orgUserDAO.getOrgUserList(pageSize, startRow);
	}
	/**
	 * 获得指定部门下兼职用户以及普通用户的合并列表
	 * @param departmentid
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getOrgUserList(Long departmentid) {
		 List<OrgUser> newUserList = new ArrayList<OrgUser>();
		 List<OrgUser> userList = orgUserDAO.getDeptAllUserList(departmentid);
		 List<OrgUserMap> userMapList = orgUserMapDAO.getOrgUserMap_DeptId(departmentid);
		 Map<String,OrgUser> userMap = new HashMap<String,OrgUser>();
		 if(userList!=null){
			 for(OrgUser user:userList){
					if(userMap.containsKey(user.getUserid())){
						continue;
					}else{
						userMap.put(user.getUserid(), user);
					}
				}
		 }
		 if(userMapList!=null){
			 for(OrgUserMap user:userMapList){
				 if(userMap.containsKey(user.getUserid())){
					 continue;
				 }else{
					 OrgUser model = orgUserDAO.getUserModel(user.getUserid());
					 if(model!=null){
						 userMap.put(user.getUserid(), model);
					 }
					 
				 }
			 }
		 }
		newUserList.addAll(userMap.values());
		return newUserList;
	}
	/**
	 * 获得指定部门下兼职用户列表
	 * @param departmentid
	 * @return
	 */
	public List<OrgUser> getOrgUserMapList(Long departmentid){
		 List<OrgUser> newUserList = new ArrayList<OrgUser>();
		 List<OrgUser> userList = orgUserDAO.getDeptAllUserList(departmentid);
		 List<OrgUserMap> userMapList = orgUserMapDAO.getOrgUserMap_DeptId(departmentid);
		 Map<String,OrgUser> userMap = new HashMap<String,OrgUser>();
		 if(userList!=null){
			 for(OrgUser user:userList){
					if(userMap.containsKey(user.getUserid())){
						continue;
					}else{
						userMap.put(user.getUserid(), user);
					}
				}
		 }
		 if(userMapList!=null){
			 for(OrgUserMap user:userMapList){
				 if(userMap.containsKey(user.getUserid())){
					 continue;
				 }else{
					 OrgUser model = orgUserDAO.getUserModel(user.getUserid());
					 if(model!=null){
						 newUserList.add(model);
					 }
				 }
			 }
		 }
		return newUserList;
	}
	/**
	 * 获得指定部门下兼职用户列表
	 * @param departmentid
	 * @return
	 */
	public List getOrgUserMapAndOrgRoleList(Long departmentid){
		List newUserList = new ArrayList();
		List<OrgUser> userList = orgUserDAO.getDeptAllUserList(departmentid);
		List<Object[]> userMapList = orgUserMapDAO.getOrgUserMapAndOrgRoleDeptId(departmentid);
		Map<String,OrgUser> userMap = new HashMap<String,OrgUser>();
		if(userList!=null){
			for(OrgUser user:userList){
				if(userMap.containsKey(user.getUserid())){
					continue;
				}else{
					userMap.put(user.getUserid(), user);
				}
			}
		}
		if(userMapList!=null){
			for (Object[] obj : userMapList) {
				OrgUserMap orgUserMap = (OrgUserMap) obj[0];
				String userid = orgUserMap.getUserid();
				if(userid!=null){
					if(userMap.containsKey(userid)){
						continue;
					}else{
						List modelList = orgUserDAO.getUserModelAndOrgRole(userid);
						if(!modelList.isEmpty()){
							newUserList.add(modelList.get(0));
						}
					}
				}
			}
		}
		return newUserList;
	}
	/**
	 * 获得指定部门下普通用户列表
	 * @param departmentid
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getDeptAllUserList(Long departmentid) {
		// TODO Auto-generated method stub
		return orgUserDAO.getDeptAllUserList(departmentid);
	}
	/**
	 * 获得指定部门下普通用户及角色列表
	 * @param departmentid
	 * @param pageSize
	 * @param startRow
	 * @return
	 */
	public List getDeptAllUserAndRoleList(Long departmentid) {
		// TODO Auto-generated method stub
		return orgUserDAO.getDeptAllUserAndRoleList(departmentid);
	}
	/**
	 * 获得人员序号
	 * @return
	 */
	public String getMaxID() {
		// TODO Auto-generated method stub
		return orgUserDAO.getMaxID();
	}
	/**
	 * 获取人员OrderIndex
	 */
	public String getMaxOrderIndex(){
		return orgUserDAO.getMaxOrderIndex();
	}
	/**
	 * 根据部门ID，获得部门负责人
	 * @param deptId
	 * @return
	 */
	public List<OrgUser> getDetpManager(Long deptId){
		List<OrgUser> list = orgUserDAO.getDeptManager(deptId);
		//如果未找到部门负责人，则依据组织结构继续向上查找
		if(list==null||list.size()==0){
			OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(deptId);
			if(!orgDepartment.getParentdepartmentid().equals(new Long(0))){
				list = this.getDetpManager(orgDepartment.getParentdepartmentid());
			}
		}
		return list;
	}

	/**
	 * 获取指定部门下用户列表
	 * @param departmentid
	 * @return
	 */
	public int getRows(Long departmentid) {
		return orgUserDAO.getRows(departmentid);
	}

	
	/**
	 * 获得隶属于指定角色的用户列表
	 * @param roleId
	 * @return
	 */
	public List<OrgUser> getUserListByRole(String roleId){
		return orgUserDAO.getUserListByRole(roleId);
	}

	/**
	 * 更新用户信息
	 * @param obj
	 */
	public void updateBoData(OrgUser obj) {
		if(obj.getCompanyid()==null){
			if(obj.getDepartmentid()!=null){
				OrgDepartment orgDepartment = orgDepartmentDAO.getBoData(obj.getDepartmentid());
				obj.setCompanyid(Long.parseLong(orgDepartment.getCompanyid()));
				OrgCompany orgCompany = orgCompanyDAO.getBoData(orgDepartment.getCompanyid());
				obj.setCompanyname(orgCompany.getCompanyname());
			}
		}
		
		orgUserDAO.updateBoData(obj);
		//更新微信账号
		if(SystemConfig._weixinConf.getServer().equals("on")){
//			winxinOrgService.updateUser(obj);
		}
		//更新索引
		if(esfdi==null){
			esfdi  =  (EaglesSearchOrgInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_ORGINFO_INDEX);
		}
		if(esfdi!=null){
			esfdi.updateDocument(obj);
		}
	}
	
	/**
	 * 执行用户注销
	 */
	public void disable(String userid){
		UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		if(uc!=null){
			orgUserDAO.execDisabled(userid);
			OrgUser orguser = uc.get_userModel();
			if(orguser!=null){
				try{
					//移除账户
					if(SystemConfig._weixinConf.getServer().equals("on")){
//						winxinOrgService.removeUser(userid);
					}
					//移除索引
					if(esfdi==null){
						esfdi  =  (EaglesSearchOrgInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_ORGINFO_INDEX);
					}
					if(esfdi!=null){
						esfdi.delDocuemnt(orguser);
					} 
				}catch(Exception e){
					logger.error(e,e);
				}
				
			} 
		}
	}
	
	/**
	 * 执行用户帐号激活
	 */
	public void activating(String userid){
		orgUserDAO.execActivating(userid);
		UserContext uc = UserContextUtil.getInstance().getUserContext(userid);
		if(uc!=null){ 
			OrgUser orguser = uc.get_userModel();
			if(orguser!=null){
				try{
					//移除账户
					if(SystemConfig._weixinConf.getServer().equals("on")){
//						winxinOrgService.addUser(orguser);
					}
					if(esfdi==null){
						esfdi  =  (EaglesSearchOrgInfoImpl)EaglesSearchFactory.getEaglesSearcherImpl(EaglesSearchConstant.EAGLES_SEARCH_TYPE_ORGINFO_INDEX);
					}
					if(esfdi!=null){
						esfdi.addDocument(orguser);
					}
				}catch(Exception e){
					logger.error(e,e);
				}
			} 
		}
	}
	/**
	 * 执行用户帐号解锁
	 */
	public void unlock(String userid){
		userid = userid.toUpperCase();
		HashMap hashMap = LoginCache.getInstance().getHashMap(userid);
		if(hashMap!=null){
			HashMap<String,Integer> map=new HashMap<String,Integer>();
			map.put("UnlockCount", 0);
			LoginCache.getInstance().putHashMap(userid, map);
		}
	}
	/**
	 * 执行用户帐号初始化
	 */
	public String initPWD(String userId){
		String msg = "";
		//判断当前用户是否为管理员
		if(SecurityUtil.isSuperManager()){
			UserContext model = UserContextUtil.getInstance().getUserContext(userId);
			String mailaddress = model.get_userModel().getEmail();
			//获取动态密码
			Integer passwordCount = SystemConfig._iworkServerConf.getUserDefaultPasswordCount();
			String pwd = PWDTools.getPassword(passwordCount, true, true);
			OrgUser orgUser = model._userModel;
			//执行MD5加密
			/*MD5 md5 = new MD5();
			String md5pwd = md5.getEncryptedPwd(pwd);*/
			//执行SHA-256+SALT加密
			String salt = ShaSaltUtil.getStringSalt();
			String shapwd=ShaSaltUtil.getEncryptedPwd(pwd,salt,true);
			orgUser.setPassword(shapwd);//更新密码
			orgUser.setExtend3(salt);
			orgUserDAO.updateBoData(orgUser);
			
			StringBuffer content = new StringBuffer();
			content.append(orgUser.getUsername()).append(",").append("您好！").append("\n")
			.append("您在").append(SystemConfig._iworkServerConf.getShortTitle()).append("中的登陆口令已初始化成功").append("<br>").append("\n")
			.append("  ").append("\n");
			content.append("<b><font size=2>【登录帐户】").append(orgUser.getUserid()).append("</font></b>\n").append("\n").append("<br>");
			content.append("<b><font size=2>【登录密码】").append(pwd).append("</font></b>\n").append("\n").append("<br><br><br><br>");
			content.append(" <b>【重要提醒】：为了保证登录安全，请及时修改您的密码</b>").append("\n");
			content.append("  <br><br>").append("\n");
			content.append("说明：").append("\n").append("<br>")
			.append("1.如果您在公司内网，请点击<a href=\"").append(SystemConfig._iworkServerConf.getLoginURL()).append("/\">").append(SystemConfig._iworkServerConf.getLoginURL()).append("</a>登录").append(SystemConfig._iworkServerConf.getShortTitle()).append("；如果您在公司外，请先点击<a href=\"https://ksvpn.kingsoft.com/\">https://ksvpn.kingsoft.com</a>登录公司VPN，然后点击“").append(SystemConfig._iworkServerConf.getShortTitle()).append("”。").append("\n").append("<br>")
			.append("2.此为系统自动发送的邮件，请不要直接回复；").append("<br>").append("\n")
			.append("3.").append(SystemConfig._iworkServerConf.getShortTitle()).append("为公司内部信息系统，包含公司保密信息，请勿将其中信息透漏给无关人员；同时请您妥善保管好自己的用户名和密码。").append("<br>").append("\n")
			.append("  ").append("\n") 
			.append("<b><font size=2.5>").append(SystemConfig._iworkServerConf.getTitle()).append("</font></b>");
			MessageAPI.getInstance().sendSysMail(SystemConfig._iworkServerConf.getShortTitle(), mailaddress, "系统密码初始化", content.toString());
			if(SystemConfig._weixinConf.getServer().equals("on")){
				StringBuffer weixinContent = new StringBuffer();
				weixinContent.append(orgUser.getUsername()).append(",").append("您好！").append("\n")
				.append("您在").append(SystemConfig._iworkServerConf.getShortTitle()).append("中的登陆口令已初始化成功").append("\n");
				weixinContent.append("【登录帐户】").append(orgUser.getUserid()).append("\n");
				weixinContent.append("【登录密码】").append(pwd).append("\n");
				weixinContent.append("【重要提醒】：为了保证登录安全，请及时修改您的密码").append("\n");
				SendMessageUtil.getInstance().sendTextMessage(model.get_userModel().getUserid(), weixinContent.toString());
			} 
			msg = "success";
		}else{
			msg = "noSecurity";
		}
		return msg;
	}
	
	/**
	 * 获得用户管理组织结构树
	 * @param pid
	 * @return
	 */
	public String getOrgUserTree(Long pid){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		
		if(pid==0){
			List list  = orgCompanyDAO.getAll();
			for(int i = 0;i<list.size();i++){
				OrgCompany orgCompany = (OrgCompany)list.get(i);

				Map<String,Object> company = new HashMap<String,Object>();
				company.put("id", orgCompany.getId());
				company.put("text", orgCompany.getCompanyname());
				company.put("iconCls", "icon-ok");
				company.put("children",this.getTopDeptJson(orgCompany.getId()));
				items.add(company);
			}
		}else{
			items = this.getChildrenJson(pid);
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 获得指定部门的指定角色的用户列表
	 * @param deptId
	 * @param roleIds
	 * @return
	 */
	public List<OrgUser> getUserListByDeptRole(Long deptId,String[] roleIds){
		List<OrgRole> arrayList = new ArrayList();
		
		for(String roleId:roleIds){
			OrgRole orgRole = orgRoleDAO.getBoData(roleId);
			if(orgRole!=null){
				arrayList.add(orgRole);
			} 
		}
		return orgUserDAO.getUserListByDeptRole(deptId,arrayList);
	}
	
	
	/**
	 * 获得指定部门的指定兼任角色的用户列表
	 * @param deptId
	 * @param roleIds
	 * @return
	 */
	public List<OrgUserMap> getUserMapListByDeptRole(Long deptId,String[] roleIds){
		List<OrgRole> arrayList = new ArrayList();
		
		for(String roleId:roleIds){
			OrgRole orgRole = orgRoleDAO.getBoData(roleId);
			if(orgRole!=null){
				arrayList.add(orgRole);
			} 
		} 
		return orgUserMapDAO.getUserMapListByDeptRole(deptId,arrayList);
	}
	/**
	 * 获得首级部门列表
	 * @param companyId
	 * @return
	 */
	private List<Map<String,Object>> getTopDeptJson(String companyId){
		List<Map<String,Object>> sub_items = new ArrayList<Map<String,Object>>();
		List list = orgDepartmentDAO.getTopDepartmentList(companyId);
		for(int i=0;i<list.size();i++){
			Map<String,Object> item = new HashMap<String,Object>();
			OrgDepartment orgDepartment = (OrgDepartment)list.get(i);
			item.put("id", orgDepartment.getId());
			item.put("text", orgDepartment.getDepartmentname());
			item.put("state", "closed");
			item.put("dnd", "false"); 
			if(this.isChildrenJson(orgDepartment.getId())){
				item.put("iconCls","icon-nav-sys");
			}
//			item.put("children",this.getChildrenJson(orgDepartment.getId()));
			sub_items.add(item);
		}
		return sub_items;
	}
	
	/**
	 * 判断是否存在子部门
	 * @param departmentid
	 * @return
	 */
	private boolean isChildrenJson(Long departmentid){
		boolean flag = false;
		List list = orgDepartmentDAO.getSubDepartmentList(departmentid);
		List userList = orgUserDAO.getActiveUserList(departmentid);
		if(list!=null&&userList!=null){
			if(list.size()>0||userList.size()>0){
				flag = true;
			}else{
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 获得子节点
	 * @param departmentid
	 * @return
	 */
	private List<Map<String,Object>> getChildrenJson(Long departmentid){
		List<Map<String,Object>> sub_items = new ArrayList<Map<String,Object>>();
			UserContext uc =  UserContextUtil.getInstance().getCurrentUserContext();
			//获取部门信息
			List deptlist = orgDepartmentDAO.getSubDepartmentList(departmentid);
			for(int i=0;i<deptlist.size();i++){
				Map<String,Object> item = new HashMap<String,Object>();
				OrgDepartment orgDepartment = (OrgDepartment)deptlist.get(i);
				item.put("id", orgDepartment.getId());
				item.put("text", orgDepartment.getDepartmentname());
				item.put("iconCls", "icon-ok");
				item.put("dnd", "false");
				item.put("state", "closed");
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("type", "dept");
				attributes.put("companyid", orgDepartment.getCompanyid());
				item.put("attributes", attributes);
//				item.put("children",this.getChildrenJson(orgDepartment.getId()));
				sub_items.add(item);
			}
			List userList = orgUserDAO.getActiveUserList(departmentid);
			for(int i=0;i<userList.size();i++){
				Map<String,Object> item = new HashMap<String,Object>();
				OrgUser orgUser = (OrgUser)userList.get(i);
				item.put("id", orgUser.getId());
				item.put("text", orgUser.getUsername()+"["+orgUser.getUserid()+"]");
				item.put("iconCls", "icon-org-user");
				item.put("state", "open");
				item.put("dnd", "true");
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("type", "dept");
				attributes.put("deptid", orgUser.getDepartmentid());
				item.put("attributes", attributes);
//				item.put("children",this.getChildrenJson(orgDepartment.getId()));
				sub_items.add(item);
			}
		return sub_items;
	}
	/**
	 * 获得组织结构菜单树脚本
	 * @return
	 */
	public String getNavTreeScript(){
		
		StringBuffer js = new StringBuffer();
		js.append("<script>").append("\n");
		js.append("foldersTree = gFld(\"用户管理维护\", \"department_list.action\",  \"diffFolder.gif\", \"diffFolder-0.gif\");").append("\n");
		List orgcompanyList = orgCompanyDAO.getAll();
		if(orgcompanyList!=null){
			for(int i=0;i<orgcompanyList.size();i++){
				
				OrgCompany oc = (OrgCompany)orgcompanyList.get(i);
				js.append(this.getCompanyScript(oc.getId(), oc.getCompanyname(), "department_list.action?companyId="+oc.getId()+"&parentdeptid=0&layer=0", "foldersTree","company_"));
				List departmentList = orgDepartmentDAO.getTopDepartmentList(oc.getId());
				//获得首级部门列表
				for(int j=0;j<departmentList.size();j++){
					
					StringBuffer Tips = new StringBuffer();   //子目录信息提示
					OrgDepartment topod = (OrgDepartment)departmentList.get(j);
					//获得子部门列表
					List list = orgDepartmentDAO.getSubDepartmentList(topod.getId());
					int userCount = orgUserDAO.getRows(topod.getId());
					if(list.size()>0){
						Tips.append("<img src='iwork_img/d-tree.gif' border=0>["+list.size()+"]");
					}
					if(userCount>0){
						Tips.append("<img src='iwork_img/user6.gif' border=0>["+userCount+"]");
					}
					
					js.append(this.getFolderScript(topod.getId(), topod.getDepartmentname()+Tips.toString(), "user_list.action?departmentid="+topod.getId(), "company_"+topod.getCompanyid(),"department_"));
					js.append(this.getSubDeptScript(list));
				}				
			}
		}
		js.append("initializeDocument();").append("\n");
		js.append("</script>").append("\n");
		return js.toString();
	}
	
	
	/**
	 * 获取树视图
	 * @param companyId
	 * @param parentid
	 * @param type
	 * @return
	 */
	public String getTreeJson(String companyId,String parentid,String type,boolean isRoot,String searchKey){
		StringBuffer html = new StringBuffer();
		List<Map<String,Object>> root = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
		if(type!=null&&type.equals("company")){
			List<OrgCompany> orgcompanyList = orgCompanyDAO.getCompanyList(companyId,searchKey);
			if(orgcompanyList!=null){
				for(OrgCompany oc:orgcompanyList){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id",oc.getId()); 
					item.put("nodeType","company");
					item.put("name", oc.getCompanyname());
					//item.put("pageurl","company_list.action?parentid="+oc.getId());
					item.put("pageurl","department_list.action?companyId="+oc.getId()+"&parentdeptid=0");
					item.put("iconOpen", "iwork_img/organization.gif"); 
					item.put("iconClose", "iwork_img/organization.gif"); 
					item.put("icon", "iwork_img/organization.gif"); 
					item.put("open", false);
					item.put("async", false); 
					List<OrgCompany> subCompanyList =  orgCompanyDAO.getCompanyList(oc.getId());
					
					if((subCompanyList!=null&&subCompanyList.size()>0)){
						item.put("children", this.getTreeJson(oc.getId(),oc.getId(),type,false,null)); 
					}
					List departmentList = orgDepartmentDAO.getTopDepartmentList(oc.getId());
					if(departmentList!=null&&departmentList.size()>0){
						item.put("isParent", true); 
					} 
					item.put("target", "companyFrame");  
					item.put("companyId", oc.getId()); 
					rows.add(item);
				}
			}
		}
		
		//获取部门列表
		List departmentList = null;
		if(type!=null&&type.equals("company")){ 
			departmentList = orgDepartmentDAO.getTopDepartmentList(companyId);
		}else{
			departmentList = orgDepartmentDAO.getSubDepartmentList(Long.parseLong(parentid)); 
		} 
		if(departmentList != null){  
			for(int j=0;j<departmentList.size();j++){
				if(departmentList.get(j)==null)continue;
				OrgDepartment od = (OrgDepartment)departmentList.get(j);
				Map<String,Object> subItem = new HashMap<String,Object>();
				subItem.put("id", od.getId());
				int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(od.getId());
				int userSize = this.getOrgUserList(od.getId()).size();
				if(subDepartmentSize>0){
					subItem.put("isParent","true"); 
					if(userSize>0){
						subItem.put("name", od.getDepartmentname()+"["+userSize+"]");
					}else{
						subItem.put("name", od.getDepartmentname());
					}
				}else{ 
					if(userSize>0){
						subItem.put("name", od.getDepartmentname()+"["+userSize+"]");
					}else{
						subItem.put("name", od.getDepartmentname());
					}
				} 
				subItem.put("async", true); 
				subItem.put("companyId",companyId); 
				subItem.put("nodeType","dept");
				subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
				subItem.put("iconClose", "iwork_img/images/folder.gif");
				subItem.put("icon", "iwork_img/images/folder-open.gif");
				subItem.put("pageurl","user_list.action?companyId="+parentid+"&departmentid="+od.getId());
				subItem.put("target", "departmenFrame");
				rows.add(subItem); 
			}
		}
		
		JSONArray json = null;
		if(isRoot){
			Map<String,Object> item = new HashMap<String,Object>();
			OrgCompany  orgCompany = orgCompanyDAO.getBoData(companyId);
			if(orgCompany!=null){
				item.put("id",companyId);
				item.put("nodeType","company");
				item.put("name", orgCompany.getCompanyname());
				item.put("companyId",companyId); 
				item.put("pageurl","department_list.action?companyId="+orgCompany.getId()+"&parentdeptid=0"); 
				item.put("iconOpen", "iwork_img/organization.gif"); 
				item.put("iconClose", "iwork_img/organization.gif"); 
				item.put("icon", "iwork_img/organization.gif"); 
				item.put("open", true);
				item.put("children", rows);
				root.add(item);
				json = JSONArray.fromObject(root); 
			}
		}else{
			json = JSONArray.fromObject(rows); 
		}
		html.append(json);
		return html.toString();
	}
	
	/**
	 * 加载导航树的Json
	 * @return
	 */
	public String getNode(Long departmentid){
		StringBuffer html = new StringBuffer();
		if(departmentid.equals(new Long(0))){
			List orgcompanyList = orgCompanyDAO.getAll();
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			if(orgcompanyList!=null){
				for(int i=0;i<orgcompanyList.size();i++){
					//加载公司节点
					if(orgcompanyList.get(i)==null)continue;
					OrgCompany oc = (OrgCompany)orgcompanyList.get(i);
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id",oc.getId());
					item.put("iconOpen", "iwork_img/organization.gif");
					item.put("iconClose", "iwork_img/organization.gif");
					item.put("open", true);
					item.put("nodeType","company");
					item.put("name", oc.getCompanyname());
					item.put("pageurl","department_list.action?companyId="+oc.getId()+"&parentdeptid=0");
					item.put("target", "orgUserFrame");
					//加载公司的首级部门节点 
					StringBuffer subHtml = new StringBuffer();
					List departmentList = orgDepartmentDAO.getTopDepartmentList(oc.getId());
					List<Map<String,Object>> subRows = new ArrayList<Map<String,Object>>();
					if(departmentList != null){
						for(int j=0;j<departmentList.size();j++){
							if(departmentList.get(j)==null)continue;
							OrgDepartment od = (OrgDepartment)departmentList.get(j);
							Map<String,Object> subItem = new HashMap<String,Object>();
							subItem.put("id", od.getId());
							subItem.put("nodeType","dept");
							subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
							subItem.put("iconClose", "iwork_img/images/folder.gif");
							subItem.put("icon", "iwork_img/images/folder-open.gif");
							int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(od.getId());
							int userSize = this.getOrgUserList(od.getId()).size();
							if(subDepartmentSize>0){
								subItem.put("isParent","true"); 
								if(userSize>0){
									subItem.put("name", od.getDepartmentname()+"["+userSize+"]");
								}else{
									subItem.put("name", od.getDepartmentname());
								}
							}else{ 
								if(userSize>0){
									subItem.put("name", od.getDepartmentname()+"["+userSize+"]");
								}else{
									subItem.put("name", od.getDepartmentname());
								}
							}
							subItem.put("pageurl","user_list.action?departmentid="+od.getId());
							subItem.put("target", "navUserlistFrame");
							subRows.add(subItem);
						}
					}
					JSONArray subjson = JSONArray.fromObject(subRows);
					subHtml.append(subjson);
					item.put("children",subHtml.toString());
					rows.add(item);
				}
			}
			JSONArray json = JSONArray.fromObject(rows);
			html.append(json);
		}else{
			List subDeptList = orgDepartmentDAO.getSubDepartmentList(departmentid);
			List<Map<String,Object>> subRows = new ArrayList<Map<String,Object>>();
			if(subDeptList != null){
				for(int i=0;i<subDeptList.size();i++){
					if(subDeptList.get(i)==null)continue;
					OrgDepartment od = (OrgDepartment)subDeptList.get(i);
					Map<String,Object> subItem = new HashMap<String,Object>();
					subItem.put("id", od.getId());
					subItem.put("nodeType","dept");
					subItem.put("iconOpen", "iwork_img/images/folder-open.gif");
					subItem.put("iconClose", "iwork_img/images/folder.gif");
					subItem.put("icon", "iwork_img/images/folder-open.gif");
					int subDepartmentSize = orgDepartmentDAO.getSubDepartmentSize(od.getId());
					int userSize = this.getOrgUserList(od.getId()).size();
					if(subDepartmentSize>0){
						subItem.put("isParent","true"); 
						if(userSize>0){
							subItem.put("name", od.getDepartmentname()+"["+userSize+"]");
						}else{
							subItem.put("name", od.getDepartmentname());
						}
					}else{
						if(userSize>0){
							subItem.put("name", od.getDepartmentname()+"["+userSize+"]");
						}else{
							subItem.put("name", od.getDepartmentname());
						}
					}
					subItem.put("pageurl","user_list.action?departmentid="+od.getId());
					subItem.put("target", "navUserlistFrame");
					subRows.add(subItem);
				}
			}
			JSONArray json = JSONArray.fromObject(subRows);
			html.append(json);
		}
 		return html.toString();
	}
	
	/**
	 * 获得指定单位的用户json
	 * @param companyId
	 * @return
	 */
	public String loadAllUserJson(String companyId,String searchKey){
		List<HashMap> list  = orgUserDAO.getCompanyActiveUserList(companyId,searchKey);
		StringBuffer jsonHtml = new StringBuffer();
		List jsonlist = new ArrayList();
		for(HashMap orguser:list){ 
			Map mapList = new HashMap();
			StringBuffer showInfo = new StringBuffer();
			StringBuffer useraddress = new StringBuffer();
			useraddress.append(orguser.get("userid")).append("[").append(orguser.get("username")).append("]");
			showInfo.append(orguser.get("username")).append("[").append(orguser.get("companyname")).append("/").append(orguser.get("departmentname")).append("]");
			mapList.put("name",showInfo.toString());
			mapList.put("to",useraddress.toString());
			mapList.put("username",orguser.get("username"));
			jsonlist.add(mapList);
		}
		JSONArray json = JSONArray.fromObject(jsonlist);
		jsonHtml.append(json);
		String result = jsonHtml.toString();
		return result;
	}
	/**
	 * 获得子部门脚本,循环递归
	 * @return
	 */
	private String getSubDeptScript(List list){
		StringBuffer js = new StringBuffer();
		int layer = 0; 
			if(list==null)return "";
			for(int i=0;i<list.size();i++){
				StringBuffer Tips = new StringBuffer("");  //子目录信息提示
				OrgDepartment model = (OrgDepartment)list.get(i);
				String url = "user_list.action?departmentid="+model.getId()+"&layer="+layer;
				//循环调用
				List nextlist = orgDepartmentDAO.getSubDepartmentList(model.getId());
				int userCount = orgUserDAO.getRows(model.getId());
				if(nextlist.size()>0){
					Tips.append("<img src='iwork_img/d-tree.gif' border=0>["+nextlist.size()+"]");
				}
				if(userCount>0){
					Tips.append("<img src='iwork_img/user6.gif' border=0>["+userCount+"]");
				}
				js.append("department_").append(model.getId()).append(" = insFld(").append("department_").append(model.getParentdepartmentid()).append(", gFld (\"").append(model.getDepartmentname()+Tips.toString()).append("\", \"").append(url).append("\", \"orgDept.gif\", \"orgDept.gif\"));").append("\n");
				js.append(this.getSubDeptScript(nextlist));
			}
		return js.toString();
	}
	
	
	/**
	 * 获得中间节点脚本
	 * @param id
	 * @param name
	 * @param url
	 * @param parentkey
	 * @return
	 */
	private String getCompanyScript(String id,String name,String url,String parentkey,String key){
		StringBuffer js = new StringBuffer();
		js.append(key).append(id).append(" = insFld(").append(parentkey).append(", gFld (\"").append(name).append("\", \"").append(url).append("\", \"mac_open.gif\", \"mac_closed.gif\"));").append("\n");
		return js.toString();
	}
	
	/**
	 * 获得中间节点脚本
	 * @param id
	 * @param name
	 * @param url
	 * @param parentkey
	 * @return
	 */
	private String getFolderScript(Long id,String name,String url,String parentkey,String key){
		StringBuffer js = new StringBuffer();
		js.append(key).append(id).append(" = insFld(").append(parentkey).append(", gFld (\"").append(name).append("\", \"").append(url).append("\", \"orgDept.gif\", \"orgDept.gif\"));").append("\n");
		return js.toString();
	}
	
	
	
	/**
	 * 向上移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveUp(Long departmentid,int id){
		String type="up";
		orgUserDAO.updateIndex(departmentid,id,type);
	}
	
	/**
	 * 向下移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveDown(Long departmentid,int id){
		String type="down";
		orgUserDAO.updateIndex(departmentid, id, type);
	}
	/**
	 * 置顶移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveTop(Long departmentid,int id){
		String type="up";
		orgUserDAO.updateTop(departmentid,id);
	}
	
	/**
	 * 置底移动排序
	 * @param orderindex
	 * @return
	 */
	public void moveBottem(Long departmentid,int id){
		orgUserDAO.updateBottem(departmentid, id);
	}
	//====================================================================================
	public void setOrgUserDAO(OrgUserDAO orgUserDAO) {
		this.orgUserDAO = orgUserDAO;
	}

	public void setOrgCompanyDAO(OrgCompanyDAO orgCompanyDAO) {
		this.orgCompanyDAO = orgCompanyDAO;
	}

	public void setOrgDepartmentDAO(OrgDepartmentDAO orgDepartmentDAO) {
		this.orgDepartmentDAO = orgDepartmentDAO;
	}
	public void setOrgUserMapDAO(OrgUserMapDAO orgUserMapDAO) {
		this.orgUserMapDAO = orgUserMapDAO;
	}

	public OrgUserDAO getOrgUserDAO() {
		return orgUserDAO;
	}

	public void setOrgRoleDAO(OrgRoleDAO orgRoleDAO) {
		this.orgRoleDAO = orgRoleDAO;
	}

	public WeiXinOrgService getWinxinOrgService() {
		return winxinOrgService;
	}

	public void setWinxinOrgService(WeiXinOrgService winxinOrgService) {
		this.winxinOrgService = winxinOrgService;
	}
}
