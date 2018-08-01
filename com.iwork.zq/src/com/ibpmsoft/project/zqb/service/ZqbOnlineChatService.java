package com.ibpmsoft.project.zqb.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import net.sf.json.JSONArray;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibpmsoft.project.zqb.dao.ZqbOnlineChatDAO;
import com.ibpmsoft.project.zqb.util.UploadFileNameCodingUtil;
import com.iwork.core.organization.context.UserContext;
import com.iwork.core.organization.tools.UserContextUtil;

public class ZqbOnlineChatService {
	private static Logger logger = Logger.getLogger(ZqbOnlineChatService.class);
	private ZqbOnlineChatDAO zqbOnlineChatDAO;

	public ZqbOnlineChatDAO getZqbOnlineChatDAO() {
		return zqbOnlineChatDAO;
	}

	public void setZqbOnlineChatDAO(ZqbOnlineChatDAO zqbOnlineChatDAO) {
		this.zqbOnlineChatDAO = zqbOnlineChatDAO;
	}

	public String getUserName(){
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String sendUserName=uc._userModel.getUserid() + "["+ uc._userModel.getUsername() + "]";
		return sendUserName;
	}

	public String getOnlineNameList(String onlineName) {
		StringBuffer jsonHtml = new StringBuffer();
		List<String> testOnlineName= new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(onlineName,",");
		//看看此 tokenizer 的字符串中是否还有更多的可用标记。
		while(st.hasMoreTokens()){
			//返回此 string tokenizer 的下一个标记。
			testOnlineName.add(st.nextToken());
        }
		List<HashMap> datalist=new ArrayList<HashMap>();
		//可以看到的发送人员
		List<HashMap> sendUserList = this.getSendUserList();
		for (HashMap sendMap : sendUserList) {
			if(testOnlineName.contains(sendMap.get("sendname").toString())){
				sendMap.put("STATUS", "(在线)");
			}else{
				sendMap.put("STATUS", "<font color=\"red\">(离线)</font>");
			}
		}
		Collections.sort(sendUserList, new Comparator<HashMap>() {
		      public int compare(HashMap firstMapEntry,HashMap secondMapEntry) {
		    	  return firstMapEntry.get("STATUS").toString().compareTo(secondMapEntry.get("STATUS").toString())==0?firstMapEntry.get("zqdm").toString().compareTo(secondMapEntry.get("zqdm").toString()):firstMapEntry.get("STATUS").toString().compareTo(secondMapEntry.get("STATUS").toString());
		      }
		    });
		JSONArray json = JSONArray.fromObject(sendUserList);
		jsonHtml.append("{\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}
	
	public List<HashMap> getSendUserList(){
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> l = zqbOnlineChatDAO.getList();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		String username = uc.get_userModel().getUsername();
		for(HashMap map:l){
			HashMap hashMap = new HashMap();
			if((!userid.equals(map.get("USERID").toString()))&&(!username.equals(map.get("USERNAME").toString()))){
				String zqjc=map.get("ZQJC")==null?"":map.get("ZQJC").toString();
				String zqdm=map.get("ZQDM")==null?"":map.get("ZQDM").toString();
				String name=map.get("USERNAME").toString();
				hashMap.put("roleid",map.get("ROLEID"));
				hashMap.put("deptname",map.get("DEPTNAME"));
				hashMap.put("zqjc",zqjc);
				hashMap.put("zqdm",zqdm);
				hashMap.put("userid",map.get("USERID"));
				hashMap.put("username",name);
				hashMap.put("sendname",map.get("USERID").toString()+"["+map.get("USERNAME").toString()+"]");
				String roleid = map.get("ROLEID").toString();
				StringBuffer sb = new StringBuffer();
				if("3".equals(roleid)){
					if(!"".equals(zqjc)){
						sb.append(zqjc);
					}
					if(!"".equals(zqdm)){
						sb.append("("+zqdm+")");
					}
					sb.append("["+name+"]");
				}else{
					String cpname=map.get("CPNAME").toString().replace("有限公司", "").replace("股份", "");
					sb.append(cpname+"["+map.get("USERNAME")+"]");
				}
				hashMap.put("viewname",sb.toString());
				list.add(hashMap);
			}
		}
		Collections.sort(list, new Comparator<HashMap>() {
		      public int compare(HashMap firstMapEntry,HashMap secondMapEntry) {
		    	  return firstMapEntry.get("zqdm").toString().compareTo(secondMapEntry.get("zqdm").toString());
		      }
		    });
		return list;
	}

	public String getOnlineRecordList(String nickname,String chatName,String startdate,String enddate,String content,String sendname,String companyjc) {
		StringBuffer jsonHtml = new StringBuffer();
        UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
        Long roleid = uc._userModel.getOrgroleid();
        List<HashMap> onlineRecordList = zqbOnlineChatDAO.getOnlineRecordList(nickname,chatName,startdate,enddate,content,sendname,companyjc);
        for (HashMap hashMap : onlineRecordList) {
        	if(chatName.equals(hashMap.get("CHATRECORDNAME").toString())){
        		hashMap.put("ISME", true);
        		if("all".equals(hashMap.get("SENDNAME").toString())){
        			hashMap.put("ONLYCHATNAME", "");
        		}else{
        			hashMap.put("ONLYCHATNAME", hashMap.get("SENDNAME").toString());
        		}
        	}else{
        		hashMap.put("ISME", false);
        		if("all".equals(hashMap.get("SENDNAME").toString())){
        			hashMap.put("ONLYCHATNAME", "");
        		}else{
        			hashMap.put("ONLYCHATNAME", hashMap.get("SENDNAME").toString());
        		}
        	}
		}
        JSONArray json = JSONArray.fromObject(onlineRecordList);
		jsonHtml.append("{\"rows\":"+json.toString()+"}");
		return jsonHtml.toString();
	}

	public String chatRecordName() {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		List<HashMap> l = zqbOnlineChatDAO.getchatRecordList(userid);
		String chatRecordName="";
		for(HashMap map:l){
			String zqjc=map.get("ZQJC")==null?"":map.get("ZQJC").toString();
			String zqdm=map.get("ZQDM")==null?"":map.get("ZQDM").toString();
			String name=map.get("USERNAME").toString();
			String roleid = map.get("ROLEID").toString();
			StringBuffer sb = new StringBuffer();
			if("3".equals(roleid)){
				if(!"".equals(zqjc)){
					sb.append(zqjc);
				}
				if(!"".equals(zqdm)){
					sb.append("("+zqdm+")");
				}
				sb.append("["+name+"]");
			}else{
				String cpname=map.get("CPNAME").toString().replace("有限公司", "").replace("股份", "");
				sb.append(cpname+"["+map.get("USERNAME")+"]");
			}
			chatRecordName=sb.toString();
		}
		return chatRecordName;
	}
	public List<HashMap> getonlineChatCount(String startime,String endtime) {
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		String username = uc.get_userModel().getUsername();
		List<HashMap> l = new ArrayList<HashMap>();
		if(userid!=null && username!=null){
			l = zqbOnlineChatDAO.getonlineChatCount(userid,startime,endtime);
		}
		return l;
	}
	public List<HashMap> getAllName(){
		List<HashMap> list = new ArrayList<HashMap>();
		List<HashMap> l = zqbOnlineChatDAO.getList();
		UserContext uc = UserContextUtil.getInstance().getCurrentUserContext();
		String userid = uc.get_userModel().getUserid();
		String username = uc.get_userModel().getUsername();
		for(HashMap map:l){
			HashMap hashMap = new HashMap();
			if((!userid.equals(map.get("USERID").toString()))&&(!username.equals(map.get("USERNAME").toString()))){
				String zqjc=map.get("ZQJC")==null?"":map.get("ZQJC").toString();
				String zqdm=map.get("ZQDM")==null?"":map.get("ZQDM").toString();
				String name=map.get("USERNAME").toString();
				hashMap.put("roleid",map.get("ROLEID"));
				hashMap.put("deptname",map.get("DEPTNAME"));
				hashMap.put("zqjc",zqjc);
				hashMap.put("zqdm",zqdm);
				hashMap.put("userid",map.get("USERID"));
				hashMap.put("username",name);
				hashMap.put("sendname",map.get("USERID").toString()+"["+map.get("USERNAME").toString()+"]");
				String roleid = map.get("ROLEID").toString();
				StringBuffer sb = new StringBuffer();
				if("3".equals(roleid)){
					if(!"".equals(zqjc)){
						sb.append(zqjc);
					}
					if(!"".equals(zqdm)){
						sb.append("("+zqdm+")");
					}
					sb.append("["+name+"]");
				}else{
					String cpname=map.get("CPNAME").toString().replace("有限公司", "").replace("股份", "");
					sb.append(cpname+"["+map.get("USERNAME")+"]");
				}
				hashMap.put("viewname",sb.toString());
				list.add(hashMap);
			}
		}
		Collections.sort(list, new Comparator<HashMap>() {
		      public int compare(HashMap firstMapEntry,HashMap secondMapEntry) {
		    	  return firstMapEntry.get("zqdm").toString().compareTo(secondMapEntry.get("zqdm").toString());
		      }
		    });
		return list;
	}
	
	/**
	 * 挂牌企业处罚记录主方法
	 * @param zqdm 证券代码
	 * @param zqjc 证券简称
	 * @param fssjstart 发生开始时间
	 * @param fssjend   发生结束时间
	 * @param cfqksm    处罚情况说明
	 * @param pageSize  页码
	 * @param pageNow   当前页
	 * @return
	 */
	public List<HashMap> zqbGpqycfjlIndex(String zqjc,String zqdm,String customername,String fssjstart,String fssjend,String cfqksm,int pageSize, int pageNow){
			List<HashMap> list = new ArrayList<HashMap>();
			list = zqbOnlineChatDAO.zqbGpqycfjlIndex(zqjc,zqdm,customername,fssjstart,fssjend,cfqksm,pageSize,pageNow);
			return list;
	}
	/**
	 * 挂牌企业处罚记录证券代码联想方法
	 * @param zqdm 证券代码
	 * @return
	 */
	public String zqbGpqycfjlGetZqdmzqjclist(String zqdm){
		return zqbOnlineChatDAO.zqbGpqycfjlGetZqdmzqjclist(zqdm);
	}
	public String zqbGpqycfjlAddZqdmzqjclist(String zqdm){
		return zqbOnlineChatDAO.zqbGpqycfjlAddZqdmzqjclist(zqdm);
	}
	public String getAllCompany(){
		return zqbOnlineChatDAO.getAllCompany();
	}
	public void thxmexcfjlportexcl(HttpServletResponse response,String startdate,String enddate,String cfqksm,String zqjc,String zqdm){
		// 第一步，创建一个webbook，对应一个Excel文件
				HSSFWorkbook wb = new HSSFWorkbook();
				// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
				HSSFSheet sheet = wb.createSheet("处罚记录");
				
			
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
				style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
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
				cell.setCellValue("公司名称");
				cell.setCellStyle(style);
				cell = row.createCell((short) 1);
				cell.setCellValue("公司简称");
				cell.setCellStyle(style);
				
				cell = row.createCell((short) 2);
				cell.setCellValue("公司代码");
				cell.setCellStyle(style);
				
				cell = row.createCell((short) 3);
				cell.setCellValue("发生时间");
				cell.setCellStyle(style);
				
			
				
				cell = row.createCell((short) 4);
				cell.setCellValue("创建人");
				cell.setCellStyle(style);
				cell = row.createCell((short) 5);
				cell.setCellValue("处罚情况说明");
				cell.setCellStyle(style);
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date stdate=null;
				Date endate=null;
				try {
					if(startdate!=null&&!startdate.equals(""))
						 stdate= sdf.parse(startdate);
					 if(enddate!=null&&!enddate.equals(""))
						 endate=  sdf.parse(enddate);
				} catch (ParseException e) {
					logger.error(e,e);
				}
				List list = zqbOnlineChatDAO.getDgcfjlDc(startdate, enddate, cfqksm, zqjc,zqdm);
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
						cell1.setCellValue(map.get("CUSTOMERNAME").toString());
						cell1.setCellStyle(style1);
						HSSFCell cell2 = row.createCell((short) 1);
						cell2.setCellValue(map.get("ZQJC").toString());
						cell2.setCellStyle(style2);
						
						
						
						
						HSSFCell cell3 = row.createCell((short) 2);
						cell3.setCellValue(map.get("ZQDM").toString());
						cell3.setCellStyle(style2);
						HSSFCell cell4 = row.createCell((short) 3);
						cell4.setCellValue(map.get("FSSJ").toString());
						cell4.setCellStyle(style2);
						
					
						
						HSSFCell cell6 = row.createCell((short) 4);
						cell6.setCellValue(map.get("CREATEUSER").toString());
						cell6.setCellStyle(style2);
						HSSFCell cell7 = row.createCell((short) 5);
						cell7.setCellValue(map.get("CFQKSM").toString());
						cell7.setCellStyle(style2);
						
						m++;

				}
				sheet.setColumnWidth(0, 7000);
				sheet.setColumnWidth(1, 5000);
				sheet.setColumnWidth(2, 5000);
				sheet.setColumnWidth(3, 5000);
				sheet.setColumnWidth(4, 5000);
				sheet.setColumnWidth(5, 15000);
				
				OutputStream out1 = null;
				// 第六步，将文件存到指定位置
				try {
					String disposition = "attachment;filename=" + UploadFileNameCodingUtil.StringEncoding("处罚记录.xls");
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
