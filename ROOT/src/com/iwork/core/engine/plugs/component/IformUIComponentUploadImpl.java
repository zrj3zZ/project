package com.iwork.core.engine.plugs.component;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ibpmsoft.project.zqb.util.ConfigUtil;
import com.iwork.commons.FileType;
import com.iwork.commons.util.FileUtil;
import com.iwork.commons.util.WebUIUtil;
import com.iwork.core.db.DBUtil;
import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.dao.FileUploadDAO;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.core.util.SpringBeanUtil;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionContext;

import org.apache.log4j.Logger;
public class IformUIComponentUploadImpl extends IFormUIComponentAbst{
	private static final String CN_FILENAME = "/common.properties"; //抓取网站配置文件    
	public static final String getConfigUUID(String parameter){
		String config=ConfigUtil.readValue(CN_FILENAME,parameter);
		return config;
	}
	private static final String ZQSERVER = getConfigUUID("zqServer")==null?"":getConfigUUID("zqServer");
	private static final String FJSCLX = getConfigUUID("fjsclx")==null?"":getConfigUUID("fjsclx");
	private static Logger logger = Logger.getLogger(IformUIComponentUploadImpl.class);
	private FileUploadDAO fileUploadDao;
	public IformUIComponentUploadImpl(SysEngineMetadataMap metadataMap,SysEngineIformMap iformMap, String value) {
		super(metadataMap, iformMap, value);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 获得移动端显示的组件代码
	 */
	public String getMobileHtmlDefine(HashMap params) {
		return getReadHtmlDefine(params);
	}
	/**
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public String getModifyHtmlDefine(HashMap params) {
		String sm="";
		String xm="";
		if(ZQSERVER.equals("xnzq")){
			sm="公告附件上传";
			xm="佐证文件上传";
		}else{
			sm="披露文件";
			xm="底稿文件";
		}
		Long fieldNotnull = getIformMapModel().getFieldNotnull();
		StringBuffer fieldHtml = new StringBuffer();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String enumValue = getIformMapModel().getDisplayEnum()==null?"":getIformMapModel().getDisplayEnum().toUpperCase();//参考值
		String sizeLimit = "";
		String multi = "true";
		String fileExt ="";
		String fileDesc="";
		if(enumValue!=null&&!"".equals(enumValue)){
			String[] arr1 = enumValue.split("&");
			for(int i=0;i<arr1.length;i++){
				String[] arr2 = arr1[i].split("=");
				if(arr2.length==2){
					if(arr2[0].equals("SIZELIMIT")){
						int mSize = Integer.parseInt(arr2[1]==null?"0":arr2[1]);
						sizeLimit=(mSize*1024*1024)+"";
					}else if(arr2[0].equals("MULTI")){
						multi=arr2[1];
					}else if(arr2[0].equals("FILEEXT")){
						fileExt=arr2[1];
					}else if(arr2[0].equals("FILEDESC")){
						fileDesc=arr2[1];
					}
				}
			}
		}
		StringBuffer validateStr = new StringBuffer(" class = '{");
		if(fieldNotnull!=null&&fieldNotnull==1L){
			validateStr.append("required:true,");
		}
		validateStr.append("maxlength:"+getMetadataMapModel().getFieldLength()+"}' ");
		String value = super.getValue();
		String fieldName = getIformMapModel().getFieldName();
		String divId="DIV"+fieldName;
		ActionContext actionContext = ActionContext.getContext();
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		String   url=request.getScheme()+"://";   
	    url+=request.getHeader("host");
	    String userid = UserContextUtil.getInstance().getCurrentUserId();
		if(!"SXFJ".equals(params.get("fieldName").toString())&&!"NOTICEFILE".equals(params.get("fieldName").toString())&&!"COMPANYNAME".equals(params.get("fieldName").toString())){
			fieldHtml.append("<div id='"+divId+"'>").append("\n");
			fieldHtml.append("	<div><input type=hidden size=100 id='"+fieldName+"' "+validateStr+" name='"+fieldName+"' value='"+value+"'/></div>").append("\n");
			fieldHtml.append("	<div><button onclick='showUploadifyPage"+fieldName+"();return false;' >附件上传</button>");
			if(fieldNotnull!=null&&fieldNotnull==1L){
				fieldHtml.append("<font color=\"red\">*</font>");
			}
			fieldHtml.append("</div>").append("\n");
		}else{
			fieldHtml.append("<div>").append("\n");
			fieldHtml.append("	<div><input type=hidden size=100 id='"+fieldName+"' "+validateStr+" name='"+fieldName+"' value='"+value+"'/></div>").append("\n");
		}
		fieldHtml.append("</div>").append("\n");
		fieldHtml.append("	<script>").append("\n");
		fieldHtml.append("		function showUploadifyPage"+fieldName+"(){").append("\n");
		fieldHtml.append("			mainFormAlertFlag=false;").append("\n");
		fieldHtml.append("			saveSubReportFlag=false;").append("\n");
		if(fieldNotnull!=null&&fieldNotnull==0L){
			fieldHtml.append("			var valid = mainFormValidator.form();").append("\n");
			fieldHtml.append("			if(!valid){").append("\n");
			fieldHtml.append("				return false;").append("\n");
			fieldHtml.append("			}").append("\n");
		}
		fieldHtml.append("			mainFormAlertFlag=false;").append("\n");
		fieldHtml.append("			saveSubReportFlag=false;").append("\n"); 
		//fieldHtml.append("			document.getElementById('submitbtn').click();").append("\n");
		if(!FJSCLX.equals("1")){
			fieldHtml.append("			uploadifyDialog('"+fieldName+"','"+fieldName+"','"+divId+"','"+sizeLimit+"','"+multi+"','"+fileExt+"','"+fileDesc+"');").append("\n");
			
		}else{
			fieldHtml.append("			webUploadifyDialog('"+fieldName+"','"+fieldName+"','"+divId+"','"+sizeLimit+"','"+multi+"','"+fileExt+"','"+fileDesc+"');").append("\n");
		}
		
		fieldHtml.append("		}").append("\n"); 
		fieldHtml.append("	</script>").append("\n");
		if("SXFJ".equals(params.get("fieldName").toString())){
			sm="附件上传";
		}
		if("SXFJ".equals(params.get("fieldName").toString())||"NOTICEFILE".equals(params.get("fieldName").toString())){
			String fj=null;
			if("SXFJ".equals(params.get("fieldName").toString())){
				fj="SXFJ";
			}else{
				fj="NOTICEFILE";
			}
			if(value!=null&&!"".equals(value)&&!"0".equals(params.get("instanceid"))){ 
				String[] arr = value.split(",");
				List<FileUpload> fileUploads = FileUploadAPI.getInstance().getFileUploads(value);
					fieldHtml.append("<table id=\""+divId+"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">"
									+ "<tr align=\"center\">"
										+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"120px;\">类型</td>"
										+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"230px;\">文件名称</td>"
										+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"200px;\">操　作</td>"
										+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"150px;\">上传时间</td>"
										+ "<td style=\"border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"70px;\">备　注</td>"
									+ "</tr>");
				int j=0;
				int z=0;
				for(int i=0;i<arr.length;i++){
					FileUpload fileUpload = getFileUpload(arr[i]);
					if(fileUpload!=null){
						String fileDivId = fileUpload.getFileId();
						String fileSrcName=fileUpload.getFileSrcName();
						String fileExtName = FileUtil.getFileExt(fileSrcName);
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						List<HashMap> list=new ArrayList<HashMap>();
						StringBuffer sql = new StringBuffer();
						sql.append("select * from (select * from IWORK_WEBOFFCIE_MEMO where file_id=? order by dotime desc) where rownum=1");
						Connection conn=DBUtil.open();
						PreparedStatement stmt=null;
						ResultSet rs=null;
						try {
							stmt =conn.prepareStatement(sql.toString());
							stmt.setString(1, fileDivId);
							rs = stmt.executeQuery();
							while(rs.next()){
								String file_id=rs.getString("FILE_ID");
								String username=rs.getString("USERNAME");
								Timestamp dotime=rs.getTimestamp("DOTIME");
								String name=DBUtil.getString("select USERNAME from orguser where userid='"+username+"'", "USERNAME");//文件名称
								HashMap<String,Object> map=new HashMap<String,Object>();
								map.put("file_id",file_id);
								map.put("dotime",sd.format(dotime));
								map.put("username",username);
								map.put("name",name);
								list.add(map);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}finally{
							DBUtil.close(conn, stmt, rs);
						}
						StringBuffer sbs=new StringBuffer();
						if(list.size()>0){
							sbs.append(list.get(0).get("name").toString()+"("+list.get(0).get("dotime")+")  更新");
						}
						fieldHtml.append("<tr id="+fileDivId+">");
						if(j==0){
							fieldHtml.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" rowspan=\""+arr.length+"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\">"
												+ "<a class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPage"+params.get("fieldName").toString()+"();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\">"+sm+"</a>"
											+ "</td>");
						}
						j++;
							fieldHtml.append("<td title=\""+fileUpload.getFileSrcName()+"\" align=\"left\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\">"
												+ (z+1)+".<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
													+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a>&nbsp;"//(fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())
											+ "</td>"
											+ "<td  align=\"center\" width=\"200px;\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\">");
							//fileExtName
							z++;
							if(fileExtName!=null&&(fileExtName.equals("doc")||fileExtName.equals("docx"))){
								fieldHtml.append("<a style=\"text-decoration:none;\" href=\"javascript:updateUploadify('"+fileDivId+"','"+fileSrcName+"');\">【修改】</a>");
								fieldHtml.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('"+fileDivId+"','"+userid+"','"+url+"','"+fj+"');\">【历史版本】</a>");
							}
							fieldHtml.append("<a class=\"delButton\" style=\"text-decoration:none;\" href=\"javascript:aloneUploadifyReomve('").append(fieldName).append("','").append(fileUpload.getFileId()).append("','").append(fileDivId).append("');\">【删除】</a>&nbsp;");
							fieldHtml.append("</td>"
											+ "<td width=\"150px;\" align=\"center\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\">&nbsp;"+fileUpload.getUploadTime()+"&nbsp;"
											+ "</td>"
											+ "<td width=\"70px;\" align=\"center\" style=\"border-bottom:1px solid #eee;color:#004080;\">&nbsp;"+sbs.toString()+"&nbsp;"
											+ "</td>"
										+ "</tr>");
					
						fieldHtml.append("</div>\n");
					}
				}
				if(j==0){
					fieldHtml.append("<tr style=\"height:0px;\">");
						fieldHtml.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\" width=\"120px;\">"
											+ "<a class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPage"+params.get("fieldName").toString()+"();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\">"+sm+"</a>"
										+ "</td>");
						fieldHtml.append("<td title=\"\" align=\"left\" style=\"border-right:1px solid #eee;\" width=\"230px;\"></td>"
										+ "<td  align=\"center\" width=\"200px;\" style=\"border-right:1px solid #eee;\"></td>"
										+ "<td width=\"150px;\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\">&nbsp;</td>"
										+ "<td width=\"70px;\" align=\"center\" style=\"color:#004080;\">&nbsp;</td>"
									+ "</tr>");
				}
				fieldHtml.append("</table>");
			}else{
				fieldHtml.append("<table id=\""+divId+"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">"
								+ "<tr align=\"center\">"
									+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"120px;\">类型</td>"
									+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"230px;\">文件名称</td>"
									+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"200px;\">操　作</td>"
									+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"150px;\">上传时间</td>"
									+ "<td style=\"border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;\" width=\"70px;\">备　注</td>"
								+ "</tr>");
					fieldHtml.append("<tr style=\"height:0px;\">");
						fieldHtml.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\" width=\"120px;\">"
										+ "<a class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPage"+params.get("fieldName").toString()+"();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\">"+sm+"</a>"
										+ "</td>");
						fieldHtml.append("<td title=\"\" align=\"left\" style=\"border-right:1px solid #eee;\" width=\"230px;\">"
										+ "</td>"
										+ "<td  align=\"center\" width=\"200px;\" style=\"border-right:1px solid #eee;\">"
										+ "</td>"
										+ "<td width=\"150px;\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\">&nbsp;&nbsp;"
										+ "</td>"
										+ "<td width=\"90px;\" align=\"center\" style=\"color:#004080;\">&nbsp;&nbsp;"
										+ "</td>"
								+ "</tr>");
				fieldHtml.append("</table>");
			}
		}else if("COMPANYNAME".equals(params.get("fieldName").toString())){
			if(value!=null&&!"".equals(value)&&!"0".equals(params.get("instanceid"))){ 
				String[] arr = value.split(",");
				List<FileUpload> fileUploads = FileUploadAPI.getInstance().getFileUploads(value);
					fieldHtml.append("<table id=\""+divId+"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">");
				int j=0;
				int z=0;
				for(int i=0;i<arr.length;i++){
					FileUpload fileUpload = getFileUpload(arr[i]);
					if(fileUpload!=null){
						String fileDivId = fileUpload.getFileId();
						String fileSrcName=fileUpload.getFileSrcName();
						String fileExtName = FileUtil.getFileExt(fileSrcName);
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						List<HashMap> list=new ArrayList<HashMap>();
						StringBuffer sql = new StringBuffer();
						sql.append("select * from (select * from IWORK_WEBOFFCIE_MEMO where file_id=? order by dotime desc) where rownum=1");
						Connection conn=DBUtil.open();
						PreparedStatement stmt=null;
						ResultSet rs=null;
						try {
							stmt =conn.prepareStatement(sql.toString());
							stmt.setString(1, fileDivId);
							rs = stmt.executeQuery();
							while(rs.next()){
								String file_id=rs.getString("FILE_ID");
								String username=rs.getString("USERNAME");
								Timestamp dotime=rs.getTimestamp("DOTIME");
								String name=DBUtil.getString("select USERNAME from orguser where userid='"+username+"'", "USERNAME");//文件名称
								HashMap<String,Object> map=new HashMap<String,Object>();
								map.put("file_id",file_id);
								map.put("dotime",sd.format(dotime));
								map.put("username",username);
								map.put("name",name);
								list.add(map);
							}
						} catch (Exception e) {
							logger.error(e,e);
						}finally{
							DBUtil.close(conn, stmt, rs);
						}
						StringBuffer sbs=new StringBuffer();
						if(list.size()>0){
							sbs.append(list.get(0).get("name").toString()+"("+list.get(0).get("dotime")+")  更新");
						}
						fieldHtml.append("<tr id="+fileDivId+">");
						if(j==0){
							fieldHtml.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" rowspan=\""+arr.length+"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"120px;\">"
												+ "<a class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPage"+params.get("fieldName").toString()+"();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\">"+xm+"</a>"
											+ "</td>");
						}
						j++;
							fieldHtml.append("<td title=\""+fileUpload.getFileSrcName()+"\" align=\"left\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\">"
												+ (z+1)+".<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
													+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a>&nbsp;"//(fileUpload.getFileSrcName().length()>22?fileUpload.getFileSrcName().substring(0, 19)+"...":fileUpload.getFileSrcName())
											+ "</td>"
											+ "<td  align=\"center\" width=\"200px;\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\">");
							//fileExtName
							z++;
							if(fileExtName!=null&&(fileExtName.equals("doc")||fileExtName.equals("docx"))){
								fieldHtml.append("<a style=\"text-decoration:none;\" href=\"javascript:updateUploadify('"+fileDivId+"','"+fileSrcName+"');\">【修改】</a>");
								fieldHtml.append("<a style=\"text-decoration:none;\" href=\"javascript:aloneWebOpenVersion('"+fileDivId+"','"+userid+"','"+url+"');\">【历史版本】</a>");
							}
							fieldHtml.append("<a class=\"delButton\" style=\"text-decoration:none;\" href=\"javascript:aloneUploadifyReomve('").append(fieldName).append("','").append(fileUpload.getFileId()).append("','").append(fileDivId).append("');\">【删除】</a>&nbsp;");
							fieldHtml.append("</td>"
											+ "<td width=\"150px;\" align=\"center\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\">&nbsp;"+fileUpload.getUploadTime()+"&nbsp;"
											+ "</td>"
											+ "<td width=\"70px;\" align=\"center\" style=\"border-bottom:1px solid #eee;color:#004080;\">&nbsp;"+sbs.toString()+"&nbsp;"
											+ "</td>"
										+ "</tr>");
				
						fieldHtml.append("</div>\n");
					}
				}
				if(j==0){
					fieldHtml.append("<tr style=\"height:0px;\">");
						fieldHtml.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\" width=\"120px;\">"
											+ "<a class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPage"+params.get("fieldName").toString()+"();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\">"+xm+"</a>"
										+ "</td>");
						fieldHtml.append("<td title=\"\" align=\"left\" style=\"border-right:1px solid #eee;\" width=\"230px;\"></td>"
										+ "<td  align=\"center\" width=\"200px;\" style=\"border-right:1px solid #eee;\"></td>"
										+ "<td width=\"150px;\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\">&nbsp;</td>"
										+ "<td width=\"70px;\" align=\"center\" style=\"color:#004080;\">&nbsp;</td>"
									+ "</tr>");
				}
				fieldHtml.append("</table>");
			}else{
				fieldHtml.append("<table id=\""+divId+"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">");
					fieldHtml.append("<tr style=\"height:0px;\">");
						fieldHtml.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\" width=\"120px;\">"
										+ "<a class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-add\" onclick=\"showUploadifyPage"+params.get("fieldName").toString()+"();return false;\" style=\"color:#5e4db0;text-decoration:underline;cursor:pointer;\">"+xm+"</a>"
										+ "</td>");
						fieldHtml.append("<td title=\"\" align=\"left\" style=\"border-right:1px solid #eee;\" width=\"230px;\">"
										+ "</td>"
										+ "<td  align=\"center\" width=\"200px;\" style=\"border-right:1px solid #eee;\">"
										+ "</td>"
										+ "<td width=\"150px;\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\">&nbsp;&nbsp;"
										+ "</td>"
										+ "<td width=\"90px;\" align=\"center\" style=\"color:#004080;\">&nbsp;&nbsp;"
										+ "</td>"
								+ "</tr>");
				fieldHtml.append("</table>");
			}
		}else{
			if(value!=null&&!"".equals(value)){ 
				String[] arr = value.split(",");
				int n=1;
				for(int i=0;i<arr.length;i++){
					FileUpload fileUpload = getFileUpload(arr[i]);
					if(fileUpload!=null){
						String fileDivId = fileUpload.getFileId();
						String fileSrcName=fileUpload.getFileSrcName();
						fieldHtml.append("<div  id=\"").append(fileDivId).append("\" style=\"background-color: #F5F5F5;vertical-align:middle;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px\">\n");
						fieldHtml.append("<div style=\"align:right;float:right;\">&nbsp;&nbsp;\n");
						/*<a href=\"javascript:updateUploadify('"+fileDivId+"','"+fileSrcName+"');\">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*/fieldHtml.append("<a href=\"javascript:uploadifyReomve('").append(fieldName).append("','").append(fileUpload.getFileId()).append("','").append(fileDivId).append("');\"><img src=\"/iwork_img/del3.gif\"/></a>\n");
						fieldHtml.append("</div>\n");
						String icon = "iwork_img/attach.png";
						if(fileUpload.getFileSrcName()!=null){
							icon = FileType.getFileIcon(fileUpload.getFileSrcName());
						}
						fieldHtml.append("<span>"+n+"、<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\"><img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a></span>\n");
						fieldHtml.append("</div>\n");
						n++;
//					fieldHtml.append("		if($(\"#"+fileDivId+"\").html()==null){$('#"+divId+"').append(buildFileElementHtml('"+fieldName+"','"+fileDivId+"','"+fileUpload.getFileSrcName()+"','"+fileUpload.getFileId()+"','"+fileUpload.getFileUrl()+"',true));}").append("\n");
					}
				} 
			}
		}
		
		return fieldHtml.toString();
	}

	public FileUpload getFileUpload(Serializable id) {
		if(fileUploadDao==null){
			fileUploadDao = (FileUploadDAO)SpringBeanUtil.getBean("uploadifyDAO");
		}
		return fileUploadDao.getFileUpload(FileUpload.class, id);
	}
	/**
	 * @preserve 声明此方法不被JOC混淆.
	 */
	
	public String getReadHtmlDefine(HashMap params) {
		String sm="";
		String xm="";
		if(ZQSERVER.equals("xnzq")){
			sm="公告附件";
			xm="佐证文件";
		}else{
			sm="披露文件";
			xm="底稿文件";
		}
		StringBuffer html = new StringBuffer();
		String id = getValue();
		
		String fieldName = getIformMapModel().getFieldName();
		if(id!=null&&!id.trim().equals("")){
			String[] filelist = id.split(","); 
			html.append("<div><input type=hidden size=100 id='"+fieldName+"' name='"+fieldName+"' value='"+id+"'/></div>\n");
			if(getIformMapModel().getFieldName()!=null&&!getIformMapModel().getFieldName().equals("")&&(getIformMapModel().getFieldName().equals("SXFJ")||getIformMapModel().getFieldName().equals("NOTICEFILE"))){
				html.append("<table id=\"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">"
						+ "<tr align=\"center\">"
							+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"90px;\">类型</td>"
							+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"230px;\">文件名称</td>"
							+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"175px;\">操　作</td>"
							+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"135px;\">上传时间</td>"
							+ "<td style=\"border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"90px;\">备　注</td>"
						+ "</tr>");
			}else if(getIformMapModel().getFieldName().equals("COMPANYNAME")){
				html.append("<table id=\"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">");
			}
			int n=1;
			for(int i=0;i<filelist.length;i++){
				String fileid = filelist[i];
				if("".equals(fileid.trim())){
					continue;
				}
				FileUpload fileUpload = this.getFileUpload(fileid.trim());
				if(fileUpload!=null){
					String suffix = FileUtil.getFileExt(fileUpload.getFileSrcName());
					String img = WebUIUtil.getLinkIcon(suffix);
					String url = ""+fileUpload.getFileUrl();
					String icon = "iwork_img/attach.png";
					if(fileUpload.getFileSrcName()!=null){
						icon = FileType.getFileIcon(fileUpload.getFileSrcName());
					}
					if(getIformMapModel().getFieldName()!=null&&!getIformMapModel().getFieldName().equals("")&&(getIformMapModel().getFieldName().equals("SXFJ")||getIformMapModel().getFieldName().equals("NOTICEFILE")||getIformMapModel().getFieldName().equals("COMPANYNAME"))){
						if(i==0){
							if("NOTICEFILE".equals(params.get("fieldName").toString())){
								html.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" rowspan=\""+filelist.length+"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\">"
										+ ""+sm+"</br></td>");
							}else if(("COMPANYNAME".equals(params.get("fieldName").toString()))){
								html.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" rowspan=\""+filelist.length+"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\">"
										+ ""+xm+"</br></td>");
							}else{
								html.append("<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" rowspan=\""+filelist.length+"\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\" width=\"90px;\">"
										+ ""+"附件上传"+"</br></td>");
							}
							
						}
							html.append("<td title=\""+fileUpload.getFileSrcName()+"\" align=\"left\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\" width=\"230px;\">"
												+ "<a href=\"uploadifyDownload.action?fileUUID=").append(fileUpload.getFileId()).append("\" target=\"_blank\">"
													+ "<img style=\"margin:3px\" src=\"").append(icon).append("\"/>").append(fileUpload.getFileSrcName()).append("</a>&nbsp;"
											+ "</td>"
											+ "<td  align=\"center\" width=\"175px;\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;\">"
											+ "</td>"
											+ "<td width=\"135px;\" align=\"center\" style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;\">&nbsp;"+fileUpload.getUploadTime()+"&nbsp;"
											+ "</td>"
											+ "<td width=\"90px;\" align=\"center\" style=\"border-bottom:1px solid #eee;color:#004080;\">&nbsp;&nbsp;"
											+ "</td>"
										+ "</tr>");
					}else{
						//已办页面下载附件名称乱码问题
						html.append("<input type=hidden name='").append(getIformMapModel().getFieldName()).append("' value=\"").append(getValue()).append("\">");
						html.append(n+"、<a target='_blank' href=\"uploadifyDownload.action?fileUUID=").append(fileid).append("\" target=\"_blank\">").append(img).append(fileUpload.getFileSrcName()).append("</a><br>");
						n++;
					}
				}
			}
			if(getIformMapModel().getFieldName()!=null&&!getIformMapModel().getFieldName().equals("")&&(getIformMapModel().getFieldName().equals("SXFJ")||getIformMapModel().getFieldName().equals("NOTICEFILE")||getIformMapModel().getFieldName().equals("COMPANYNAME"))){
				html.append("</table>");
			}
		}else{
			if(getIformMapModel().getFieldName()!=null&&!getIformMapModel().getFieldName().equals("")&&(getIformMapModel().getFieldName().equals("SXFJ")||getIformMapModel().getFieldName().equals("NOTICEFILE"))){
				html.append("<table id=\"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">"
							+ "<tr align=\"center\">"
								+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"90px;\">类型</td>"
								+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"230px;\">文件名称</td>"
								+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"175px;\">操　作</td>"
								+ "<td style=\"border-right:1px solid #eee;border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"135px;\">上传时间</td>"
								+ "<td style=\"border-bottom:1px solid #eee;color:#004080;height: 30px;font-size:12px;white-space:nowrap;background:url('../../iwork_img/engine/tools_nav_bg.jpg')repeat-x left bottom;\" width=\"90px;\">备　注</td>"
							+ "</tr>"
							+ "<tr>"
								+ "<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" rowspan=\"1\" style=\"border-right:1px solid #eee;color:#004080;\" width=\"90px;\">"+sm+"</br></td>"
								+ "<td title=\"\" align=\"left\" style=\"border-right:1px solid #eee;\" width=\"230px;\"></td>"
								+ "<td  align=\"center\" width=\"175px;\" style=\"border-right:1px solid #eee;\"></td>"
								+ "<td width=\"135px;\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\">&nbsp;&nbsp;</td>"
								+ "<td width=\"90px;\" align=\"center\" style=\"color:#004080;\">&nbsp;&nbsp;</td>"
							+ "</tr>"
							+"</table>");
			}else if(getIformMapModel().getFieldName().equals("COMPANYNAME")){
				html.append("<table id=\"\" style=\"margin-left:left;font-size:12px;margin-right:left;border:1px solid #eee;\" cellspacing=\"0\" cellpadding=\"0\" width=\"725px;\">"
						+ "<tr>"
							+ "<td id=\"rowspantd"+params.get("fieldName").toString()+"\" align=\"center\" rowspan=\"1\" style=\"border-right:1px solid #eee;color:#004080;\" width=\"90px;\">"+xm+"</br></td>"
							+ "<td title=\"\" align=\"left\" style=\"border-right:1px solid #eee;\" width=\"230px;\"></td>"
							+ "<td  align=\"center\" width=\"175px;\" style=\"border-right:1px solid #eee;\"></td>"
							+ "<td width=\"135px;\" align=\"center\" style=\"border-right:1px solid #eee;color:#004080;\">&nbsp;&nbsp;</td>"
							+ "<td width=\"90px;\" align=\"center\" style=\"color:#004080;\">&nbsp;&nbsp;</td>"
						+ "</tr>"
						+"</table>");
			}else{
				html.append("<input type=hidden name='").append(getIformMapModel().getFieldName()).append("' value=\"").append(getValue()).append("\">").append(getValue());
			}
		}  
		return html.toString(); 
	}
	public String getSubFormColumnModelScript(String columnModelType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> parseToJson(String displayString,String columnModelType, Map<String, Object> rootMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
