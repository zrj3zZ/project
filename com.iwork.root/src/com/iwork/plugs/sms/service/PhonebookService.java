package com.iwork.plugs.sms.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.Region;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.organization.model.OrgUser;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.ResponseUtil;
import com.iwork.plugs.sms.bean.PhonebookMst;
import com.iwork.plugs.sms.bean.PhonebookgroupMst;
import com.iwork.plugs.sms.dao.PhonebookDao;
import com.iwork.sdk.DemAPI;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
public class PhonebookService {
	private PhonebookDao phonebookDao;
	private static Logger logger = Logger.getLogger(PhonebookService.class);
	public int count;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * 获得分组列表
	 * @return
	 */
	public String getTypeTreeJson(){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			List list = phonebookDao.getAlltype();
			for(int i = 0;i<list.size();i++){
				PhonebookgroupMst pbook = (PhonebookgroupMst)list.get(i);	
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("id", pbook.getGroupid());
					item.put("text", pbook.getGroupname());		
					item.put("iconCls","icon-nav-sys");
			
					items.add(item);			
			}
		}catch(Exception e){
			logger.error(e,e);
		}
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append(json);
		return jsonHtml.toString();
	}
	/**
	 * 根据分组获得号码列表
	 * @return
	 */
	public String getNumsTreeJson(String type,String name, String mobilenum,
			String attr1, String attr2, String attr3){
		StringBuffer jsonHtml = new StringBuffer();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			List list = phonebookDao.getTypeNums(type,name, mobilenum,attr1, attr2,attr3);
			for(int i = 0;i<list.size();i++){
				PhonebookMst pbook = (PhonebookMst)list.get(i);	
					Map<String,Object> item = new HashMap<String,Object>();
					//item.put("id", pbook.getCid());
					int cid=pbook.getCid();
					StringBuffer operateBtn = new StringBuffer();
					operateBtn.append("<a  href='###' onclick=\"dataEdit(").append(cid).append(")\"").append(">修改</a>\n");
					operateBtn.append("<a  href='###' onclick=\"removeItem(").append(cid).append(")\"").append(">删除</a>\n");
//					
					item.put("TYPE", pbook.getGroupname());		
					item.put("NAME",pbook.getName());
					item.put("PHONE", pbook.getMobilenum());
					item.put("ATTR1",pbook.getAttr1());
					item.put("ATTR2", pbook.getAttr2());
					item.put("ATTR3", pbook.getAttr3());
					item.put("OPERATE",operateBtn.toString() );
					items.add(item);			
			}
		}catch(Exception e){
			logger.error(e,e);
		}		
		JSONArray json = JSONArray.fromObject(items);
		jsonHtml.append("{\"total\":200,\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	public void setPhonebookDao(PhonebookDao PhonebookDao) {
		this.phonebookDao = PhonebookDao;
	}

	public void adddb(PhonebookMst pm) {
		phonebookDao.adddb(pm);
	}

	public String querydb(String type1name, String name, String mobilenum,
			String attr1, String attr2, String attr3) {
		return phonebookDao.querydb(type1name, name, mobilenum, attr1, attr2,
				attr3);
	}

	public String querytype() {
		return phonebookDao.querytype();
	}

	public void addtype(PhonebookgroupMst pm) {
		phonebookDao.addtype(pm);
	}

	public String querytypeall() {
		return phonebookDao.querytypeall();
	}

	/**
	 * 号码簿删除号码
	 * 
	 * @param cid
	 */
	public void delnum(String cid) {
		phonebookDao.delnum(cid);
	}

	public PhonebookMst getPhonebookMst(int cid) {
		return phonebookDao.getPhonebookMst(cid);
	}

	/**
	 * 号码簿修改号码的时候分组显示
	 * 
	 * @param type
	 * @return
	 */
	public String querytype(String type) {
		return phonebookDao.querytype(type);
	}

	/**
	 * 号码簿修改号码后保存号码
	 * 
	 * @param cid
	 */
	public void addeditnum(String cid, String nameedit, String mobileedit,
			String extend1edit, String extend2edit, String extend3edit,
			String typeedit) {
		phonebookDao.addeditnum(cid, nameedit, mobileedit, extend1edit,
				extend2edit, extend3edit, typeedit);
	}

	/**
	 * 号码簿分组删除分组
	 * 
	 * @param cid
	 */
	public void deltype(String cid) {
		phonebookDao.deltype(cid);
	}

	/**
	 * 号码簿查询号码返回列表
	 * 
	 * @param list
	 * @return
	 */
	public String getList(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			int i = 1;
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				String cid = ht.get("CID").toString();
				String updatenum = "<a  href='###' onclick=\"dataEdit('" + cid
						+ "')\">修改</a>";
		
				String delnum = "<a  href='###' onclick=\"remove('" + cid
				+ "')\">删除</a>";
				sb.append("<tr>\n");
				sb.append("<td class ='actionsoftReportData' align='center'>"
						+ i + "</td>");
				sb.append("<td align='center' nowrap>"
						+ (ht.get("GROUPNAME").toString()) + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ (ht.get("NAME").toString()) + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ (ht.get("MOBILENUM").toString()) + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ (ht.get("EXTEND1").toString()) + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ (ht.get("EXTEND2").toString()) + "</td>\n");
				sb.append("<td align='center' nowrap>"
						+ (ht.get("EXTEND3").toString()) + "</td>\n");
				sb.append("<td class='td_data' align='center'>").append(
						"&nbsp;").append(updatenum).append("&nbsp;").append(
						delnum).append("&nbsp;").append("</td>");
				sb.append("</tr>\n");
				i++;
			}
		}
		return sb.toString();
	}

	/**
	 * 从数据库取分组，分组list显示
	 * 
	 * @param list
	 * @return
	 */
	public String getListtype(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Hashtable hs = (Hashtable) it.next();

				// String groupid = hs.get("GROUPID").toString();
				String groupname = hs.get("GROUPNAME").toString();
				sb.append("<option value='").append(groupname).append("'>")
						.append(groupname).append("</option>");

			}
		}
		return sb.toString();
	}
	/**
	 * 分组设置页面，分组的查询显示list
	 * @param list
	 * @return
	 */
	public String getListtypeall(ArrayList list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Hashtable ht = (Hashtable) it.next();
				String groupid = ht.get("GROUPID").toString();
				String updatenum = "<a  href='###' onclick=\"typeEdit('"
					+ groupid + "')\">修改</a>";
			String delnum = "<a href='deltype.action?cid=" + groupid
					+ "'>删除</a>";
				sb.append("<tr>\n");
				sb.append("<td class ='actionsoftReportData' align='center'>"
						+ ht.get("ID").toString() + "</td>");
				sb.append("<td align='center' nowrap>"
						+ ht.get("GROUPNAME").toString() + "</td>\n");
				sb.append("<td class='td_data' align='center'>").append(
						"&nbsp;").append(updatenum).append("&nbsp;").append(
						delnum).append("&nbsp;").append("</td>");
				sb.append("</tr>\n");
			}
		}

		return sb.toString();

	}
	/**
	 * 号码簿修改号码的时候分组的显示
	 * @param list
	 * @param type
	 * @return
	 */
		public String getListtypenum(ArrayList list,String type) {
			StringBuffer sb = new StringBuffer();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				sb.append("<select name='typeedit1' id=\"typeedit1\">");
				while (it.hasNext()) {
					Hashtable hs = (Hashtable) it.next();
					// String groupid = hs.get("GROUPID").toString();
					String groupname = hs.get("GROUPNAME").toString();
					if(groupname.equals(type)){
					sb.append("<option value='").append(groupname).append("' selected>")
							.append(groupname).append("</option>");
					}else{
					sb.append("<option value='").append(groupname).append("' >")
						.append(groupname).append("</option>");
					}

				}
			}
			sb.append("</select>");
			return sb.toString();
		}
		/**
		 * 号码簿增加号码的时候重复性检查
		 * @param repeatnum
		 * @return
		 */
		public String checkrepeatnum(long repeatnum,String repeattype){
			return phonebookDao.checkrepeatnum(repeatnum,repeattype);
			
		}
		/**
		 * 号码簿修改号码的时候重复性检查
		 * @param repeatnum
		 * @return
		 */
		public String checkrepeatnum2(String cid,long repeatnum,String repeattype){
			return phonebookDao.checkrepeatnum2(cid,repeatnum,repeattype);
			
		}
		/**
		 * 号码簿增加分组的时候重复性检查分组
		 * @param repeatnum
		 * @return
		 */
		public String checkrepeattype(String repeattype){
			return phonebookDao.checkrepeatnum(repeattype);
			
		}
		/**
		 * 号码簿修改分组的时候重复性检查分组
		 * @param repeatnum
		 * @return
		 */
		public String checkrepeattype2(String groupid,String repeattype){
			return phonebookDao.checkrepeatnum2(groupid,repeattype);
			
		}
		/**
		 * 根据分组的groupid查询分组的名称
		 * @param cid
		 * @return
		 */
		public String querydbtype(String cid){
			return phonebookDao.querydbtype(cid);
			
		}
		/**
		 * 号码簿修改分组名称
		 * @param groupid
		 * @param groupname
		 */
		public void addtypeedit(String groupid,String groupname){
			phonebookDao.addtypeedit(groupid,groupname);
		}
		public HashMap getPhoneList(int pageNumber, int pageSize,String zqdm,String gsmc,String leibie) {
			int endRow = pageNumber*pageSize;
			int startRow = (pageNumber - 1) * pageSize;
			OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			String userid=userModel.getUserid();
			int roleId = userModel.getOrgroleid().intValue();
			String customerno = userModel.getExtend1();
			HashMap phoneBook = new HashMap();
			phoneBook=phonebookDao.getlist(startRow,endRow,zqdm,gsmc,leibie,userid,roleId,customerno);
			return phoneBook;
		}
		
	
		public HashMap getDDRY(HashMap a,HashMap b){
			List<HashMap> ddry = DemAPI.getInstance().getList("84ff70949eac4051806dc02cf4837bd9", b, "ID");
			if(ddry.size()>0){
				if(ddry.get(0).get("KHFZR")!=null&&!"".equals(ddry.get(0).get("KHFZR").toString())){
					a.put("CXDDRY", ddry.get(0).get("KHFZR").toString().substring(ddry.get(0).get("KHFZR").toString().indexOf("[")+1,ddry.get(0).get("KHFZR").toString().indexOf("]")));
					a.put("CXDDRYTEL",DBUtil.getString("select MOBILE from OrgUser where USERNAME='"+ddry.get(0).get("KHFZR").toString().substring(ddry.get(0).get("KHFZR").toString().indexOf("[")+1,ddry.get(0).get("KHFZR").toString().indexOf("]"))+"'", "MOBILE"));
					a.put("CXDDRYEMAIL",DBUtil.getString("select EMAIL from OrgUser where USERNAME='"+ddry.get(0).get("KHFZR").toString().substring(ddry.get(0).get("KHFZR").toString().indexOf("[")+1,ddry.get(0).get("KHFZR").toString().indexOf("]"))+"'", "EMAIL"));
				}
			}
			if(ddry.size()>0){
				if(ddry.get(0).get("ZZCXDD")!=null&&!"".equals(ddry.get(0).get("ZZCXDD").toString())){
					a.put("ZZDDRY", ddry.get(0).get("ZZCXDD").toString().substring(ddry.get(0).get("ZZCXDD").toString().indexOf("[")+1,ddry.get(0).get("ZZCXDD").toString().indexOf("]")));
					a.put("ZZDDRYTEL",DBUtil.getString("select MOBILE from OrgUser where USERNAME='"+ddry.get(0).get("ZZCXDD").toString().substring(ddry.get(0).get("ZZCXDD").toString().indexOf("[")+1,ddry.get(0).get("ZZCXDD").toString().indexOf("]"))+"'", "MOBILE"));
					a.put("ZZDDRYEMAIL",DBUtil.getString("select EMAIL from OrgUser where USERNAME='"+ddry.get(0).get("ZZCXDD").toString().substring(ddry.get(0).get("ZZCXDD").toString().indexOf("[")+1,ddry.get(0).get("ZZCXDD").toString().indexOf("]"))+"'", "EMAIL"));
				}
			}
			return a;
		}
		
		public String getFZR(HashMap a){
			String dm="";
			List<HashMap> fuzeren = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", a, "ID");
			StringBuffer fzr=new StringBuffer();
			for (HashMap hashMap3 : fuzeren) {
				if(hashMap3.get("OWNER").toString().indexOf("[")==-1){
					fzr.append(hashMap3.get("OWNER").toString());
				}else{
					fzr.append(hashMap3.get("OWNER").toString().substring(hashMap3.get("OWNER").toString().indexOf("[")+1, hashMap3.get("OWNER").toString().indexOf("]"))+",");
				}
			}
			if(fzr.length()!=0){
				dm=fzr.substring(0, fzr.length()-1);
			}
			return dm;
		}
		
		public String getFZRPhone(HashMap a){
			String phone="";
			List<HashMap> fuzeren = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", a, "ID");
			StringBuffer fzr=new StringBuffer();
			for (HashMap hashMap3 : fuzeren) {
				if(hashMap3.get("OWNER").toString().indexOf("[")==-1){
					phone = DBUtil.getString("select MOBILE from OrgUser where USERNAME='"+hashMap3.get("OWNER").toString()+"'", "MOBILE");
				}else{
					phone = DBUtil.getString("select MOBILE from OrgUser where USERNAME='"+hashMap3.get("OWNER").toString().substring(hashMap3.get("OWNER").toString().indexOf("[")+1, hashMap3.get("OWNER").toString().indexOf("]"))+"'", "MOBILE");
				}
				if(phone!=null&&!"".equals(phone)){
					fzr.append(phone+",");
				}
			}
			if(fzr.length()!=0){
				phone=fzr.substring(0, fzr.length()-1);
			}
			return phone;
		}
		
		public String getFZREmail(HashMap a){
			String email="";
			List<HashMap> fuzeren = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", a, "ID");
			StringBuffer fzr=new StringBuffer();
			for (HashMap hashMap3 : fuzeren) {
				if(hashMap3.get("OWNER").toString().indexOf("[")==-1){
					email = DBUtil.getString("select EMAIL from OrgUser where USERNAME='"+hashMap3.get("OWNER").toString()+"'", "EMAIL");
				}else{
					email = DBUtil.getString("select EMAIL from OrgUser where USERNAME='"+hashMap3.get("OWNER").toString().substring(hashMap3.get("OWNER").toString().indexOf("[")+1, hashMap3.get("OWNER").toString().indexOf("]"))+"'", "EMAIL");
				}
				if(email!=null&&!"".equals(email)){
					fzr.append(email+",");
				}
			}
			if(fzr.length()!=0){
				email=fzr.substring(0, fzr.length()-1);
			}
			return email;
		}
		
		public String getKH(HashMap a){
			String khlxr="";
			List<HashMap> fuzeren = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", a, "ID");
			StringBuffer fzr=new StringBuffer();
			for (HashMap hashMap3 : fuzeren) {
				fzr.append(hashMap3.get("KHLXR").toString());
			}
			if(fzr.length()!=0){
				khlxr=fzr.toString();
			}
			return khlxr;
		}
		
		public String getKHPhone(HashMap a){
			String khphone="";
			List<HashMap> fuzeren = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", a, "ID");
			StringBuffer fzr=new StringBuffer();
			for (HashMap hashMap3 : fuzeren) {
				fzr.append(hashMap3.get("KHLXDH").toString());
			}
			if(fzr.length()!=0){
				khphone=fzr.toString();
			}
			return khphone;
		}
		
		public String getKHEmail(HashMap a){
			String khemail="";
			List<HashMap> fuzeren = DemAPI.getInstance().getList("33833384d109463285a6a348813539f1", a, "ID");
			StringBuffer fzr=new StringBuffer();
			for (HashMap hashMap3 : fuzeren) {
				if(hashMap3.get("OWNER").toString().indexOf("[")==-1){
					khemail = DBUtil.getString("select EMAIL from OrgUser where USERNAME='"+hashMap3.get("OWNER").toString()+"'", "EMAIL");
				}else{
					khemail = DBUtil.getString("select EMAIL from OrgUser where USERNAME='"+hashMap3.get("OWNER").toString().substring(hashMap3.get("OWNER").toString().indexOf("[")+1, hashMap3.get("OWNER").toString().indexOf("]"))+"'", "EMAIL");
				}
				if(khemail!=null&&!"".equals(khemail)){
					fzr.append(khemail+",");
				}
			}
			if(fzr.length()!=0){
				khemail=fzr.substring(0, fzr.length()-1);
			}
			return khemail==null?"":khemail;
		}
		
		public List fenYe(List a,int pageSize,int pageNumber){
			List list=new ArrayList();
			for (int i = pageSize*(pageNumber-1); i <pageSize*pageNumber ; i++) {
				if(i>a.size()-1){
					continue;
				}else{
					list.add(a.get(i));
				}
			}
			return list;
		}

	public List removeDuplicate(List list1) {
		HashSet h = new HashSet(list1);
		list1.clear();
		list1.addAll(h);
		return list1;
	}
		public void doExcelExp(HttpServletResponse response) {
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet("通讯录");
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row = sheet.createRow((int) 0);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setFillForegroundColor((short) 22);
			style.setFillPattern((short) 1);
			style.setBorderBottom((short) 1);
			style.setBottomBorderColor((short) 8);
			style.setBorderLeft((short) 1);
			style.setLeftBorderColor((short) 8);
			style.setBorderRight((short) 1);
			style.setRightBorderColor((short) 8);
			style.setBorderTop((short) 1);
			style.setTopBorderColor((short) 8);
			HSSFCellStyle style1 = wb.createCellStyle();
			style1.setBorderBottom((short) 1);
			style1.setBottomBorderColor((short) 8);
			style1.setBorderLeft((short) 1);
			style1.setLeftBorderColor((short) 8);
			style1.setBorderRight((short) 1);
			style1.setRightBorderColor((short) 8);
			style1.setBorderTop((short) 1);
			style1.setTopBorderColor((short) 8);
			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style2.setBorderBottom((short) 1);
			style2.setBottomBorderColor((short) 8);
			style2.setBorderLeft((short) 1);
			style2.setLeftBorderColor((short) 8);
			style2.setBorderRight((short) 1);
			style2.setRightBorderColor((short) 8);
			style2.setBorderTop((short) 1);
			style2.setTopBorderColor((short) 8);
			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setBorderTop((short) 1);
			style3.setTopBorderColor((short) 8);
			HSSFCell cell = row.createCell((short) 0);
			cell.setCellValue("挂牌公司");
			cell.setCellStyle(style);
			cell = row.createCell((short) 1);
			cell.setCellValue("类别");
			cell.setCellStyle(style);
			cell = row.createCell((short) 2);
			cell.setCellValue("姓名");
			cell.setCellStyle(style);
			cell = row.createCell((short) 3);
			cell.setCellValue("电话");
			cell.setCellStyle(style);
			cell = row.createCell((short) 4);
			cell.setCellValue("邮箱");
			cell.setCellStyle(style);
			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
			List<HashMap> list = new ArrayList<HashMap>();
			list=getPhoneList();
			int n = 1;
			if (list == null) {
				return;
			}
			String customername="";
			List<Integer> mergedList=new ArrayList<Integer>();
			for (HashMap map : list) {
				row = sheet.createRow((int) n++);
				HSSFCell cell1 = row.createCell((short) 0);
				cell1.setCellValue(map.get("ZQDM").toString()+"\n"+map.get("CUSTOMERNAME").toString());
				cell1.setCellStyle(style2);
				cell1 = row.createCell((short) 1);
				cell1.setCellValue(map.get("ROLENAME").toString());
				cell1.setCellStyle(style1);
				cell1 = row.createCell((short) 2);
				cell1.setCellValue(map.get("USERNAME").toString());
				cell1.setCellStyle(style1);
				cell1 = row.createCell((short) 3);
				cell1.setCellValue(map.get("MOBILE").toString());
				cell1.setCellStyle(style1);
				cell1 = row.createCell((short) 4);
				cell1.setCellValue(map.get("EMAIL").toString());
				cell1.setCellStyle(style1);
				String cusname=map.get("CUSTOMERNAME").toString();
				if(!cusname.equals(customername)){
					mergedList.add(n-1);
					customername=cusname;
				}
			}
			mergedList.add(n);
			//mergedList.add(n+1);
			for (int i = 0; i < mergedList.size()-1; i++) {
				Integer begin = mergedList.get(i);
				Integer end = mergedList.get(i+1)-1;
				sheet.addMergedRegion(new Region(begin.shortValue(), (short) 0, end.shortValue(),(short) 0));
			}
			sheet.setColumnWidth(0, 25*256*2);
			sheet.setColumnWidth(1, 25*256*2);
			sheet.setColumnWidth(2, 25*256*2);
			sheet.setColumnWidth(3, 25*256*2);
			sheet.setColumnWidth(4, 25*256*2);
			OutputStream out1 = null;
			// 第六步，将文件存到指定位置
			try {
				String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("通讯录.xls");
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
		public List<HashMap> getPhoneList() {
			OrgUser userModel = UserContextUtil.getInstance().getCurrentUserContext()._userModel;
			List<HashMap<String,Object>> parameter=new ArrayList<HashMap<String,Object>>();//存放参数
			HashMap<String,Object> valueMap = new HashMap<String,Object>();
			String userid=userModel.getUserid();
			Long roleId = userModel.getOrgroleid();
			List<HashMap> customerList = new ArrayList<HashMap>();
			StringBuffer sql=new StringBuffer("SELECT CUSTOMER.CUSTOMERNAME CUSTOMERNAME,CUSTOMERDATA.USERNAME USERNAME,CUSTOMERDATA.MOBILE MOBILE,CUSTOMERDATA.EMAIL EMAIL,CUSTOMERDATA.ROLENAME ROLENAME,CUSTOMER.ZQDM ZQDM FROM (SELECT * FROM (SELECT CUSTOMERNAME,ZQDM,CUSTOMERNO,ROWNUM RW FROM (SELECT * FROM ( ");
			if(roleId==5){
				sql.append(" SELECT KH.CUSTOMERNAME,KH.ZQDM,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH ");
			}else if(roleId==3){
				sql.append(" SELECT KH.CUSTOMERNAME,KH.ZQDM,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH ");
			}else{
				sql.append(" SELECT DISTINCT * FROM (SELECT KH.CUSTOMERNAME,KH.ZQDM,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH INNER JOIN BD_MDM_KHQXGLB QX ON KH.CUSTOMERNO=QX.KHBH AND (SUBSTR(QX.KHFZR,0, INSTR(QX.KHFZR,'[',1)-1)=? OR SUBSTR(QX.ZZCXDD,0, INSTR(QX.ZZCXDD,'[',1)-1)=? OR SUBSTR(QX.FHSPR,0, INSTR(QX.FHSPR,'[',1)-1)=? OR SUBSTR(QX.ZZSPR,0, INSTR(QX.ZZSPR,'[',1)-1)=? OR SUBSTR(QX.GGFBR,0, INSTR(QX.GGFBR,'[',1)-1)=? OR SUBSTR(QX.CWSCBFZR2,0, INSTR(QX.CWSCBFZR2,'[',1)-1)=? OR SUBSTR(QX.CWSCBFZR3,0, INSTR(QX.CWSCBFZR3,'[',1)-1)=?) "
						+ " UNION ALL "
						+ " SELECT KH.CUSTOMERNAME,KH.ZQDM,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH INNER JOIN BD_ZQB_PJ_BASE PJ ON KH.CUSTOMERNO=PJ.CUSTOMERNO AND (SUBSTR(PJ.OWNER,0, INSTR(PJ.OWNER,'[',1)-1)=? OR SUBSTR(PJ.MANAGER,0, INSTR(PJ.MANAGER,'[',1)-1)=?) "
						+ " UNION ALL "
						+ " SELECT KH.CUSTOMERNAME,KH.ZQDM,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH INNER JOIN (SELECT PJ.CUSTOMERNO FROM BD_ZQB_PJ_BASE PJ INNER JOIN BD_PM_TASK TASK ON PJ.PROJECTNO=TASK.PROJECTNO AND SUBSTR(TASK.MANAGER,0, INSTR(TASK.MANAGER,'[',1)-1)=?) PJTASK ON KH.CUSTOMERNO=PJTASK.CUSTOMERNO "
						+ " UNION ALL "
						+ " SELECT KH.CUSTOMERNAME,KH.ZQDM,KH.CUSTOMERNO FROM BD_ZQB_KH_BASE KH INNER JOIN (SELECT DISTINCT CUSTOMERNO FROM (SELECT INSTANCEID,DATAID PROJECTID,B.CUSTOMERNO,B.LCBS FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=101 AND FORMID=91) A LEFT JOIN BD_ZQB_PJ_BASE B ON A.DATAID = B.ID) C INNER JOIN (SELECT A.INSTANCEID,DATAID,USERID,B.NAME  FROM (SELECT INSTANCEID,DATAID FROM SYS_ENGINE_FORM_BIND WHERE METADATAID=105) A LEFT JOIN BD_ZQB_GROUP B ON A.DATAID = B.ID) D ON C.LCBS = D.INSTANCEID OR C.INSTANCEID=D.INSTANCEID WHERE USERID = ?) PJGROUP ON KH.CUSTOMERNO=PJGROUP.CUSTOMERNO) ");
				valueMap.put("value", userid);
				valueMap.put("type", "string");
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
				parameter.add(valueMap);
			}
			sql.append(" WHERE 1=1 ");
			if(roleId==3){
				String customerno = userModel.getExtend1();
				sql.append(" AND CUSTOMERNO=?");
				valueMap.put("value", customerno);
				valueMap.put("type", "string");
				parameter.add(valueMap);
				valueMap.clear();
			}
			sql.append(" ) ORDER BY CUSTOMERNAME))) CUSTOMER INNER JOIN (SELECT DISTINCT * FROM ( ");
			sql.append( " SELECT CUS.CUSTOMERNO CUSTOMERNO,CUS.USERNAME USERNAME,CUS.TEL MOBILE,CUS.EMAIL EMAIL,'客户联系人' ROLENAME,1 SORTID FROM BD_ZQB_KH_BASE CUS "
					+ "	UNION ALL "
					+ " SELECT PJ.CUSTOMERNO CUSTOMERNO,PJ.KHLXR USERNAME,PJ.KHLXDH MOBILE,ORG.EMAIL EMAIL,'客户联系人' ROLENAME,1 SORTID FROM (SELECT PROJECT.CUSTOMERNO,PROJECT.KHLXR KHLXR,PROJECT.KHLXDH KHLXDH FROM BD_ZQB_PJ_BASE PROJECT WHERE PROJECT.CUSTOMERNO IS NOT NULL) PJ INNER JOIN ORGUSER ORG ON PJ.CUSTOMERNO=ORG.EXTEND1 AND ORG.USERNAME=PJ.KHLXR "
					+ " UNION ALL "
					+ " SELECT CUS.CUSTOMERNO CUSTOMERNO,ORGER.USERNAME USERNAME,ORGER.MOBILE MOBILE,ORGER.EMAIL EMAIL,'客户联系人' ROLENAME,1 SORTID FROM BD_ZQB_KH_BASE CUS INNER JOIN ORGUSER ORGER ON CUS.CUSTOMERNO=ORGER.EXTEND1 ");
			sql.append(" UNION ALL "
					+ " SELECT DISTINCT PJ.CUSTOMERNO CUSTOMERNO,ORGER.USERNAME USERNAME,ORGER.MOBILE MOBILE,ORGER.EMAIL EMAIL,'项目负责人' ROLENAME,2 SORTID FROM (SELECT ORG.EXTEND1,ORG.USERID,ORG.USERNAME,ORG.ORGROLEID,ORG.MOBILE,ORG.EMAIL FROM ORGUSER ORG WHERE USERNAME IS NOT NULL) ORGER INNER JOIN (SELECT PROJECT.CUSTOMERNO,PROJECT.OWNER OWNER FROM BD_ZQB_PJ_BASE PROJECT WHERE PROJECT.CUSTOMERNO IS NOT NULL) PJ ON SUBSTR(PJ.OWNER,0, INSTR(PJ.OWNER,'[',1)-1)=ORGER.USERID ");
			sql.append(" UNION ALL "
					+ " SELECT CXDD.KHBH CUSTOMERNO,ORGER.USERNAME USERNAME,ORGER.MOBILE MOBILE,ORGER.EMAIL EMAIL,'持续督导专员' ROLENAME,3 SORTID FROM (SELECT KHQX.KHBH,KHQX.KHFZR FROM BD_MDM_KHQXGLB KHQX INNER JOIN BD_ZQB_KH_BASE KHBASE ON KHQX.KHBH=KHBASE.CUSTOMERNO) CXDD INNER JOIN ORGUSER ORGER ON SUBSTR(CXDD.KHFZR,0, INSTR(CXDD.KHFZR,'[',1)-1)=ORGER.USERID ");
			sql.append(" UNION ALL "
					+ " SELECT CXDD.KHBH CUSTOMERNO,ORGER.USERNAME USERNAME,ORGER.MOBILE MOBILE,ORGER.EMAIL EMAIL,'专职督导人员' ROLENAME,4 SORTID FROM (SELECT KHQX.KHBH,KHQX.FHSPR FROM BD_MDM_KHQXGLB KHQX INNER JOIN BD_ZQB_KH_BASE KHBASE ON KHQX.KHBH=KHBASE.CUSTOMERNO) CXDD INNER JOIN ORGUSER ORGER ON SUBSTR(CXDD.FHSPR,0, INSTR(CXDD.FHSPR,'[',1)-1)=ORGER.USERID ");
			sql.append(" ) A) CUSTOMERDATA ON CUSTOMER.CUSTOMERNO=CUSTOMERDATA.CUSTOMERNO ORDER BY CUSTOMER.CUSTOMERNAME,CUSTOMERDATA.SORTID");
			customerList=phonebookDao.getExpList(sql.toString(),parameter);
			return customerList;
		}
		
		public HashMap remove(HashMap hashMap2,String leibie){
			if(leibie!=null&&!"".equals(leibie)){
				if("董秘".equals(leibie)){
					hashMap2.put("KHLXR", "");
					hashMap2.put("FZR", "");
					hashMap2.put("CXDDRY", "");
					hashMap2.put("ZZDDRY", "");
				}
				if("客户联系人".equals(leibie)){
					hashMap2.put("USERNAME", "");
					hashMap2.put("FZR", "");
					hashMap2.put("CXDDRY", "");
					hashMap2.put("ZZDDRY", "");
				}
				if("项目负责人".equals(leibie)){
					hashMap2.put("USERNAME", "");
					hashMap2.put("KHLXR", "");
					hashMap2.put("CXDDRY", "");
					hashMap2.put("ZZDDRY", "");
				}
				if("持续督导专员".equals(leibie)){
					hashMap2.put("USERNAME", "");
					hashMap2.put("KHLXR", "");
					hashMap2.put("FZR", "");
					hashMap2.put("ZZDDRY", "");
				}
				if("专职督导人员".equals(leibie)){
					hashMap2.put("USERNAME", "");
					hashMap2.put("KHLXR", "");
					hashMap2.put("FZR", "");
					hashMap2.put("CXDDRY", "");
				}
			}
			return hashMap2;
		}
		
		public List<HashMap> removeList(List<HashMap> a){
			for (HashMap hashMap2 : a) {
				if((hashMap2.get("FZR")!=null&&!"".equals(hashMap2.get("FZR").toString()))&&(hashMap2.get("FZRPHONE")!=null&&!"".equals(hashMap2.get("FZRPHONE").toString()))&&
						(hashMap2.get("DM")!=null&&!"".equals(hashMap2.get("DM").toString()))&&(hashMap2.get("DMTEL")!=null&&!"".equals(hashMap2.get("DMTEL").toString()))&&
						(hashMap2.get("USERNAME")!=null&&!"".equals(hashMap2.get("USERNAME").toString()))&&(hashMap2.get("TEL")!=null&&!"".equals(hashMap2.get("TEL").toString()))&&
						(hashMap2.get("CXDDRY")!=null&&!"".equals(hashMap2.get("CXDDRY").toString()))&&(hashMap2.get("CXDDRYTEL")!=null&&!"".equals(hashMap2.get("CXDDRYTEL").toString()))&&
						(hashMap2.get("ZZDDRY")!=null&&!"".equals(hashMap2.get("ZZDDRY").toString()))&&(hashMap2.get("ZZDDRYTEL")!=null&&!"".equals(hashMap2.get("ZZDDRYTEL").toString()))){
					a.remove(hashMap2);
				}
			}
			return a;
		}
		private String getValue(Cell cell) {  
	        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {  
	            return String.valueOf(cell.getBooleanCellValue());
	        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
	        	BigDecimal b = new BigDecimal(cell.getNumericCellValue());
	            return b.toPlainString();
	        } else {  
	            return String.valueOf(cell.getStringCellValue());
	        }  
	    }
		public void doImpPhoneBook(String filename) {
			Long starttime = new Date().getTime();
			HashMap hashdata = new HashMap();
			List<FileUpload> sublist = FileUploadAPI.getInstance().getFileUploads(filename);
			OrgUser user =UserContextUtil.getInstance().getCurrentUserContext().get_userModel();
			ActionContext actionContext = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
			String rootPath=request.getRealPath("/");
			//String rootPath =  ServletActionContext.getServletContext().getRealPath(File.separator);
			String zb = "";
			String name = "";
			String company = "";
			String tel = "";
			String title = "";
			String email = "";
			String phonebookuuid = "";
			String phonegroupuuid = "";
			String userid = user.getUserid();
			String username = user.getUsername();
			Sheet sheet = null;
			//组别集合，用于判定组别是否存在
			Set<String> zbs = new HashSet<String>();
			//好友集合，用于判定好友是否存在
			Set<String> names = new HashSet<String>();
			Cell cell;
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = DBUtil.open();
				//获得所有组别
				ps = conn.prepareStatement("SELECT TRIM(ZB) FROM BD_GE_PHONEGROUP WHERE CREATEUSER=?");
				ps.setString(1, userid);
				rs = ps.executeQuery();
				while(rs.next()){
					zbs.add(rs.getString(1).trim());
				}
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				//获得所有好友
				ps = conn.prepareStatement("SELECT TRIM(NAME) FROM BD_GE_PHONEBOOK WHERE USERID=?");
				ps.setString(1, userid);
				rs = ps.executeQuery();
				while(rs.next()){
					names.add(rs.getString(1).trim());
				}
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				//获得个人通讯录uuid
				ps = conn.prepareStatement("SELECT UUID FROM SYS_DEM_ENGINE WHERE TITLE='个人通讯录'");
				rs = ps.executeQuery();
				while(rs.next()){
					phonebookuuid = rs.getString(1);
				}
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				//获得通讯录分组表单uuid
				ps = conn.prepareStatement("select uuid from sys_dem_engine where title='通讯录分组表单'");
				rs = ps.executeQuery();
				while(rs.next()){
					phonegroupuuid = rs.getString(1);
				}
			} catch (Exception e) {logger.error(e,e);}finally{
				DBUtil.close(conn, ps, rs);
			}
			
			//遍历上传文件
			for (FileUpload fileUpload : sublist) {
				//加载xls文件
				File file = new File(rootPath+File.separator+fileUpload.getFileUrl());
				Workbook workBook = null;  
				try {workBook = new XSSFWorkbook(rootPath+File.separator+fileUpload.getFileUrl());  
				} catch (Exception ex) {  
					try {workBook = new HSSFWorkbook(new FileInputStream(rootPath+File.separator+fileUpload.getFileUrl()));
					} catch (FileNotFoundException e) {logger.error(e,e);} catch (Exception e) {logger.error(e,e);}
				}
				if(workBook==null){continue;}
				//遍历sheet
				for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {  
					sheet = workBook.getSheetAt(numSheet);  
					if (sheet == null) {continue;}
					// 循环行Row  
					for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {  
						Row row = sheet.getRow(rowNum);  
						if (row == null) {continue;}  
						cell = row.getCell(0);
						if(cell!=null)
							zb = getValue(cell);
						cell = row.getCell(1);
						if(cell!=null)
							name = getValue(cell);
						cell = row.getCell(2);
						if(cell!=null)
							company = getValue(cell);
						cell = row.getCell(3);
						if(cell!=null)
							tel = getValue(cell).replace(".", "");
						cell = row.getCell(4);
						if(cell!=null)
							title = getValue(cell);
						cell = row.getCell(5);
						if(cell!=null)
							email = getValue(cell);
						if(!names.contains(name)){
							names.add(name.trim());
							if(zb!=null&&!zb.equals("")&&!zbs.contains(zb.trim())){
								zbs.add(zb.trim());
								hashdata.clear();
								hashdata.put("ZB", zb);
								hashdata.put("CREATEUSER", userid);
								DemAPI.getInstance().saveFormData(phonegroupuuid, DemAPI.getInstance().newInstance(phonegroupuuid, userid), hashdata, false);
							}
							hashdata.clear();
							hashdata.put("NAME",name);
							hashdata.put("TEL",tel);
							hashdata.put("TITLE",title);
							hashdata.put("EMAIL",email);
							hashdata.put("COMPANY",company);
							hashdata.put("USERID",userid);
							hashdata.put("ZB",zb);
							DemAPI.getInstance().saveFormData(phonebookuuid, DemAPI.getInstance().newInstance(phonebookuuid, userid), hashdata, false);
						}
					}
				}
			}
			
			Long endtime = new Date().getTime();
			ResponseUtil.write("导入完毕,用时"+(endtime-starttime)+"毫秒");
		}
		public void expPnoneBook(HttpServletResponse response,String model){
			// 第一步，创建一个webbook，对应一个Excel文件
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet;
			if(model.equals("0")){
				sheet = wb.createSheet("个人通讯录");
			}else{
				sheet = wb.createSheet("个人通讯录模板");
			}
			sheet.setAutobreaks(true);
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
			HSSFRow row = sheet.createRow((int) 0);
			sheet.setColumnWidth(0, 5120);
			sheet.setColumnWidth(1, 5120);
			sheet.setColumnWidth(2, 5120);
			sheet.setColumnWidth(3, 5120);
			sheet.setColumnWidth(4, 5120);
			sheet.setColumnWidth(5, 5120);
			HSSFFont headfont = wb.createFont();
			headfont.setFontName("宋体");
			headfont.setFontHeightInPoints((short) 11);// 字体大小
			headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			HSSFFont contentfont = wb.createFont();
			contentfont.setFontName("宋体");
			contentfont.setFontHeightInPoints((short) 11);// 字体大小
			// 第四步，创建单元格，并设置值表头 设置表头居中
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
			
			HSSFCellStyle style6 = wb.createCellStyle();
			style6.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style6.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style6.setFillForegroundColor((short) 22);
			style6.setFillPattern((short) 1);
			style6.setBorderBottom((short) 1);
			style6.setBorderLeft((short) 1);
			style6.setBorderRight((short) 1);
			style6.setBorderTop((short) 1);
			style6.setFillForegroundColor(HSSFColor.WHITE.index);
			
			// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
			String userid = UserContextUtil.getInstance().getCurrentUserId();
			List<HashMap> list = new ArrayList<HashMap>();
			
			if(model.equals("0")){
				list = phonebookDao.getPnoneBookList(userid);
			}
			
			int z = 0;
			int m = 0;
			HSSFRow row1 = sheet.createRow((int) m++);
			
			HSSFCell 
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("组别");
			cell1.setCellStyle(style5);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("姓名");
			cell1.setCellStyle(style5);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("所属公司");
			cell1.setCellStyle(style5);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("电话");
			cell1.setCellStyle(style5);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("职务");
			cell1.setCellStyle(style5);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			cell1 = row1.createCell((short) z++);
			cell1.setCellValue("邮箱");
			cell1.setCellStyle(style5);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
			z=0;
			
			if(model.equals("1")){
				row1 = sheet.createRow((int) m++);
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue("我的好友");
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue("王小明");
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue("北京王小明投资有限公司");
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue("13888888888");
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue("场外经理");
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue("13888888888@qq.com");
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				z=0;
			}
			
			for (HashMap hashMap : list) {
				row1 = sheet.createRow((int) m++);
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue(hashMap.get("ZB")==null?"":hashMap.get("ZB").toString());
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue(hashMap.get("NAME")==null?"":hashMap.get("NAME").toString());
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue(hashMap.get("COMPANY")==null?"":hashMap.get("COMPANY").toString());
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue(hashMap.get("TEL")==null?"":hashMap.get("TEL").toString());
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue(hashMap.get("TITLE")==null?"":hashMap.get("TITLE").toString());
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				cell1 = row1.createCell((short) z++);
				cell1.setCellValue(hashMap.get("EMAIL")==null?"":hashMap.get("EMAIL").toString());
				cell1.setCellStyle(style6);
				cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
				z=0;
			}
			
			OutputStream out1 = null;
			// 第六步，将文件存到指定位置
			try {
				String disposition;
				if(model.equals("0")){
					disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("个人通讯录.xls");
				}else{
					disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("个人通讯录模板.xls");
				}
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
		public int count(HashMap hashMap2){
			int i=0;
			if((hashMap2.get("FZR")!=null&&!"".equals(hashMap2.get("FZR").toString()))&&(hashMap2.get("FZRPHONE")!=null&&!"".equals(hashMap2.get("FZRPHONE").toString()))){
				i++;
			}
			if((hashMap2.get("KHLXR")!=null&&!"".equals(hashMap2.get("KHLXR").toString()))&&(hashMap2.get("KHLXRPHONE")!=null&&!"".equals(hashMap2.get("KHLXRPHONE").toString()))){
				i++;
			}
			if((hashMap2.get("USERNAME")!=null&&!"".equals(hashMap2.get("USERNAME").toString()))&&(hashMap2.get("TEL")!=null&&!"".equals(hashMap2.get("TEL").toString()))){
				i++;
			}
			if((hashMap2.get("CXDDRY")!=null&&!"".equals(hashMap2.get("CXDDRY").toString()))&&(hashMap2.get("CXDDRYTEL")!=null&&!"".equals(hashMap2.get("CXDDRYTEL").toString()))){
				i++;
			}
			if((hashMap2.get("ZZDDRY")!=null&&!"".equals(hashMap2.get("ZZDDRY").toString()))&&(hashMap2.get("ZZDDRYTEL")!=null&&!"".equals(hashMap2.get("ZZDDRYTEL").toString()))){
				i++;
			}
			return i;
		}
}


