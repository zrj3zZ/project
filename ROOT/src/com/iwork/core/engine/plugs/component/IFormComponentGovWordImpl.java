
package com.iwork.core.engine.plugs.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iwork.core.engine.iform.model.SysEngineIformMap;
import com.iwork.core.engine.metadata.model.SysEngineMetadataMap;
import com.iwork.core.organization.tools.UserContextUtil;
import com.iwork.core.upload.model.FileUpload;
import com.iwork.plugs.weboffice.tools.WebOfficeTools;
import com.iwork.sdk.FileUploadAPI;
import com.opensymphony.xwork2.ActionContext;


/**
 * @author David.Yang
 * 
 * HTML Text component Tag
 * 
 * @preserve 声明此方法不被JOC混淆.
 */
public class IFormComponentGovWordImpl extends IFormUIComponentAbst {
	/**
	 * 获得移动端显示的组件代码
	 */
	public String getMobileHtmlDefine(HashMap params) {
		return this.getModifyHtmlDefine(params);
	}
	/**
	 * @param value
	 * @param metaDataMapModel
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public IFormComponentGovWordImpl(SysEngineMetadataMap metadataMap,SysEngineIformMap iformMap,String value) {
		super(metadataMap,iformMap, value);
	}

	/**
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public String getModifyHtmlDefine(HashMap params) {
		String icon = "doc";
		if(params.get("PROCESS_TASK_FORMDATA")!=null){
			if(((HashMap)params.get("PROCESS_TASK_FORMDATA")).get("FILENAME")!=null){
				String fileName = ((HashMap)params.get("PROCESS_TASK_FORMDATA")).get("FILENAME").toString();
				icon = fileName.substring(fileName.lastIndexOf(".")+1);
			}
		}
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer fieldHtml = new StringBuffer();
		Long inputWidth = this.getIformMapModel().getInputWidth();
		if(inputWidth==null||inputWidth.equals(new Long(0)))inputWidth=new Long(100);
		Long inputHeight = this.getIformMapModel().getInputHeight();
		if(inputHeight==null||inputHeight.equals(new Long(0)))inputHeight=new Long(550); 
		String value = this.getValue();
		boolean isFirst=false;
		if(value==null||"".equals(value)){
			value = UUID.randomUUID().toString().replace("-","");
			this.setValue(value);
			isFirst=true;
		}
		ActionContext actionContext = ActionContext.getContext();
    	//获得response对象
		HttpServletRequest request=(HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);  
		   String   url=request.getScheme()+"://";   
	        url+=request.getHeader("host");   
	    List<FileUpload> sublist = FileUploadAPI.getInstance()
					.getFileUploads(value);
	    String fileSrcName="";
	    String filename="";
	    if(sublist.size()>0){
	    	fileSrcName = sublist.get(0).getFileSrcName();
	    	filename = fileSrcName.substring(0, fileSrcName.indexOf("."));
	    }
	    
		fieldHtml.append("<script src='iwork_js/plugs/iweboffice_load.js'></script>");
		//IE浏览器下响应工具栏事件
		fieldHtml.append("<script language=\"javascript\" for=WebOffice event=\"OnToolsClick(vIndex,vCaption)\"> ").append("\n");
		fieldHtml.append("  if (vIndex==1){").append("\n");
		fieldHtml.append("    saveForm();").append("\n");       //隐藏或显示iWebOffice工具栏 true显示  false隐藏"
		fieldHtml.append("	}");
		fieldHtml.append("  if (vIndex==2){").append("\n");
		fieldHtml.append("    pageClose();").append("\n");       //隐藏或显示iWebOffice工具栏 true显示  false隐藏"
		fieldHtml.append("	}");
		fieldHtml.append("  if (vIndex==3){").append("\n");
		fieldHtml.append("    downloadFile();").append("\n");       //隐藏或显示iWebOffice工具栏 true显示  false隐藏"
		fieldHtml.append("	}");
		fieldHtml.append("	</script>");
		//非IE浏览器下响应工具栏事件。
		fieldHtml.append("<script language=\"javascript\"> ").append("\n");
		fieldHtml.append("  function OnToolsClick(vIndex,vCaption){").append("\n");
		fieldHtml.append("  if (vIndex==1){").append("\n");
		fieldHtml.append("    saveForm();").append("\n");       //隐藏或显示iWebOffice工具栏 true显示  false隐藏"
		fieldHtml.append("	}");
		fieldHtml.append("  if (vIndex==2){").append("\n");
		fieldHtml.append("    pageClose();").append("\n");       //隐藏或显示iWebOffice工具栏 true显示  false隐藏"
		fieldHtml.append("	}");
		fieldHtml.append("  if (vIndex==3){").append("\n");
		fieldHtml.append("    downloadFile();").append("\n");       //隐藏或显示iWebOffice工具栏 true显示  false隐藏"
		fieldHtml.append("	}");
		fieldHtml.append("	}");
		fieldHtml.append("	</script>");
		
		fieldHtml.append("  <script type=\"text/javascript\">").append("\n");
		fieldHtml.append("  $(function(){").append("\n");
		fieldHtml.append("  	try{").append("\n");
		fieldHtml.append("  	Load();").append("\n");
		fieldHtml.append("  		}catch(e){").append("\n");
		fieldHtml.append("  		}").append("\n");
		fieldHtml.append("  	}); ").append("\n");
		fieldHtml.append("  function Load(){").append("\n");
		fieldHtml.append("	try{").append("\n");
		fieldHtml.append("	iformMain.WebOffice.WebUrl=\"").append(url).append("/weboffice/\"").append("\n");
		fieldHtml.append("	iformMain.WebOffice.RecordID=\"").append(value).append("\"; ").append("\n");           //RecordID:本文档记录编号
		//fieldHtml.append("	iformMain.WebOffice.Template=\"2344444\";").append("\n");            //Template:模板编号
		//fieldHtml.append("	iformMain.WebOffice.FileName=\"").append(this.getIformMapModel().getFieldTitle()).append("\"; ").append("\n");           //FileName:文档名称 
		fieldHtml.append("	iformMain.WebOffice.FileName=\"").append(filename).append("\"; ").append("\n");           //FileName:文档名称 
		//fieldHtml.append("	iformMain.WebOffice.FileName=\"").append(value).append(".doc").append("\"; ").append("\n");           //FileName:文档名称 
		fieldHtml.append("	iformMain.WebOffice.Compatible=false;").append("\n");            //FileType:文档类型  .doc  .xls  .wps
		fieldHtml.append("	iformMain.WebOffice.FileType=\".").append(icon).append("\";").append("\n");            //FileType:文档类型  .doc  .xls  .wps
		fieldHtml.append("	iformMain.WebOffice.UserName=\"").append(userid).append("\"; ").append("\n");           //UserName:操作用户名，痕迹保留需要
		fieldHtml.append("	iformMain.WebOffice.EditType=\""+(isFirst?"1,1":"3,1")+"\"; ").append("\n");           //第一次加载为编辑模式，后续进入时为修订模式  EditType:编辑类型  方式一、方式二  <参考技术文档>
		//fieldHtml.append("	iformMain.WebOffice.EditType=\"1,1"+"\"; ").append("\n");           //EditType:编辑类型  方式一、方式二  <参考技术文档>
		fieldHtml.append("	iformMain.WebOffice.MaxFileSize = 8 * 1024; ").append("\n");           //EditType:编辑类型  方式一、方式二  <参考技术文档>
		fieldHtml.append("	iformMain.WebOffice.Language=\"CH\";").append("\n");                        //Language:多语言支持显示选择   CH简体 TW繁体 EN英文
		//fieldHtml.append("	iformMain.WebOffice.ShowWindow = true;").append("\n");                  //控制显示打开或保存文档的进度窗口，默认不显示
		fieldHtml.append("	iformMain.WebOffice.PenColor=\"#FF0000\";").append("\n");                   //PenColor:默认批注颜色
		fieldHtml.append("	iformMain.WebOffice.PenWidth=\"1\";").append("\n");                         //PenWidth:默认批注笔宽
		fieldHtml.append("	iformMain.WebOffice.Print=\"1\";").append("\n");                            //Print:默认是否可以打印:1可以打印批注,0不可以打印批注
		fieldHtml.append("	iformMain.WebOffice.ShowToolBar=\"1\";").append("\n");                      //ShowToolBar:是否显示工具栏:1显示,0不显示
		fieldHtml.append("	iformMain.WebOffice.ShowMenu=\"0\";").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.height=\"800\";").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.width=\"100%\";").append("\n");                          //控制整体菜单显示
		//fieldHtml.append("	iformMain.WebOffice.FullSize();").append("\n");                          //控制整体菜单显示
		//fieldHtml.append("	iformMain.WebOffice.VisibleTools('新建文件',false);").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.VisibleTools('新建文件',false);").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.VisibleTools('保存文件',false);").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.VisibleTools('打开文件',false);").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.VisibleTools('文字批注',false);").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.VisibleTools('手写批注',false);").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.VisibleTools('文档清稿',false);").append("\n");                          //控制整体菜单显示
		fieldHtml.append("	iformMain.WebOffice.VisibleTools('重新批注',false);").append("\n");                          //控制整体菜单显示
		
		fieldHtml.append("	iformMain.WebOffice.AppendTools(\"2\",\"关闭\",0);").append("\n");
		fieldHtml.append("	iformMain.WebOffice.AppendTools(\"3\",\"下载\",0);").append("\n");
		fieldHtml.append("	iformMain.WebOffice.AppendTools(\"1\",\"保存\",2);").append("\n");
		
//	    //WebSetRibbonUIXML();      
//	    //控制OFFICE2007的选项卡显示
		fieldHtml.append("	iformMain.WebOffice.WebRepairMode = true;").append("\n"); 
		fieldHtml.append("	iformMain.WebOffice.WebOpen();").append("\n");                            //打开该文档    交互OfficeServer  调出文档OPTION="LOADFILE"    调出模板OPTION="LOADTEMPLATE"     <参考技术文档>
		fieldHtml.append("	iformMain.WebOffice.ShowType=1;").append("\n");              //文档显示方式  1:表示文字批注  2:表示手写批注  0:表示文档核稿
		fieldHtml.append("	}catch(e){").append("\n");
		fieldHtml.append("		alert(e);").append("\n");
//		fieldHtml.append("		$(\"#DivID\").html(\"<img src=\"iwork_img/notice.jpg\"><span style=\"font-size:14px;\">当前浏览器非IE浏览器，无法浏览正文,请“点击”下载安装此插件</span>\")").append("\n");
		fieldHtml.append("	}").append("\n");
		fieldHtml.append("}").append("\n");   
		fieldHtml.append("  </script>").append("\n");
		fieldHtml.append("<div id=\"DivID\">").append("\n"); 
		fieldHtml.append("<table width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\"><tr><td style=\"background:#efefef;vertical-align:top;border:1px solid #ccc;padding:2px\">").append("\n");
		//fieldHtml.append("<a  href=\"#\"  onclick='unLoad()' text='Ctrl+s' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-save\">关闭</a>");
		//fieldHtml.append("<a  href=\"#\"  onclick='downloadFile()' text='Ctrl+s' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-save\">下载</a>");//保存到本地
		//fieldHtml.append("<a  href=\"#\"  onclick='WebSaveVersionByFileID(0)' text='Ctrl+s' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-save\">保存版本</a>");
		//fieldHtml.append("<a  href=\"#\"  onclick='WebOpenVersion()' text='Ctrl+s' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-search\">打开版本历史</a>");
//		fieldHtml.append("<a  href=\"#\"  onclick='WebOpenPrint()' text='Ctrl+s' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-print\"> </a>");
		//fieldHtml.append("<a  href=\"#\"  onclick='openTemplate()' text='Ctrl+s' class=\"easyui-linkbutton\" plain=\"true\" iconCls=\"icon-dictionary\">选择模板</a>");
		fieldHtml.append("</td></tr><tr><td>").append("\n");
//		fieldHtml.append("<div id=\"divMsg\" style=\"display:none;\"><iframe></iframe></div>").append("\n"); 
	//fieldHtml.append("<object id=\"WebOffice\" width=\"100%\" height=\"").append(inputHeight).append("\" classid=\"clsid:8B23EA28-2009-402F-92C4-59BE0E063499\" codebase=\"iwork_plugs/iWebOffice2009.cab#version=10,7,2,8\" style=\"POSITION: relative;top:-20;bottommargin:0\">").append("\n");
	//	fieldHtml.append("<object id=\"WebOffice\" width=\"100%\" height=\"").append(inputHeight).append("\" classid=\"clsid:8B23EA28-2009-402F-92C4-59BE0E063499\" codebase=\"iwork_plugs/iWebOffice2009.cab#version=10,7,2,8\">").append("\n");
		fieldHtml.append(WebOfficeTools.getInstance().getWebBrowserObjHtml());
	//	fieldHtml.append("<object id=\"WebOffice\" TYPE=\"application/kg-activex\" ALIGN=\"baseline\" BORDER=\"0\" WIDTH=\"100%\" HEIGHT=\"600px\" clsid=\"{8B23EA28-2009-402F-92C4-59BE0E063499}\" copyright=\"HNA Group CoLtd\" event_OnMenuClick=\"OnMenuClick\" event_OnToolsClick=\"OnToolsClick\">").append("\n");
//		fieldHtml.append("<PARAM name=\"WebUrl\" value=\"weboffice/\">").append("\n");   
//		fieldHtml.append(" <PARAM name=\"RecordID\" value=\"").append(value).append("\">").append("\n");
//		fieldHtml.append(" <PARAM name=\"FileName\" value=\"").append(this.getIformMapModel().getFieldTitle()).append("\">").append("\n");  
//		fieldHtml.append(" <PARAM name=\"UserName\" value=\"").append(userid).append("\">").append("\n");  
//		fieldHtml.append(" <PARAM name=\"PenColor\" value=\"#FF0000\">").append("\n");
//		fieldHtml.append(" <PARAM name=\"FileType\" value=\".doc\">").append("\n"); 
//		fieldHtml.append(" <PARAM name=\"PenWidth\" value=\"1\">").append("\n"); 
//		fieldHtml.append(" <PARAM name=\"EditType\" value=\"1,1\">").append("\n");  
//		fieldHtml.append(" <PARAM name=\"ExtParam\" value=\"\">").append("\n");
//		fieldHtml.append(" <PARAM name=\"Print\" value=\"1\">").append("\n");
//		fieldHtml.append("</object>").append("\n");
//		fieldHtml.append("<object").append("\n");
//		fieldHtml.append("id=\"Control\"").append("\n");
//		fieldHtml.append("TYPE=\"application/kg-activex\"").append("\n");
//		fieldHtml.append("ALIGN=\"baseline\" BORDER=\"0\"").append("\n");
//		fieldHtml.append("WIDTH=\"800\" HEIGHT=\"600\"").append("\n");
//		fieldHtml.append("clsid=\"{8B23EA28-2009-402F-92C4-59BE0E063499}\"").append("\n");
//		fieldHtml.append("copyright=\"Jiang Xi KingGrid Technology Co., Ltd.[DEMO]\">").append("\n");
//		fieldHtml.append("</object>").append("\n");
//		fieldHtml.append("<script language=\"JavaScript\">").append("\n");
//		fieldHtml.append(" var MYplugin = document.getElementById(\"Control\");").append("\n");
//		fieldHtml.append(" function WebOpenLocal(){MYplugin.WebOpenLocal();}").append("\n");
//		fieldHtml.append("</script>").append("\n");
		fieldHtml.append("</td></tr></table>").append("\n");
		fieldHtml.append("</div>").append("\n"); 
		String htmlInner = getIformMapModel().getHtmlInner();
		if(htmlInner==null)htmlInner=""; 
		fieldHtml.append("<input type='hidden'").append(htmlInner).append(" name='").append(getIformMapModel().getFieldName()).append("'  id='").append(getIformMapModel().getFieldName()).append("' ").append(" value='").append(getValue()).append("' >");
		

		return fieldHtml.toString();
	}

	/**
	 * @preserve 声明此方法不被JOC混淆.
	 */
	public String getReadHtmlDefine(HashMap params) {
		String userid = UserContextUtil.getInstance().getCurrentUserId();
		StringBuffer fieldHtml = new StringBuffer();
		String value = this.getValue();
		if(value==null||"".equals(value)){
			value = UUID.randomUUID().toString().replace("-","");
			this.setValue(value);
		} 
		Long inputWidth = this.getIformMapModel().getInputWidth();
		if(inputWidth==null||inputWidth.equals(new Long(0)))inputWidth=new Long(100);
		Long inputHeight = this.getIformMapModel().getInputHeight();
		if(inputHeight==null||inputHeight.equals(new Long(0)))inputHeight=new Long(30); 
		fieldHtml.append("<div id=\"DivID\">").append("\n");
		fieldHtml.append("<object id=\"WebOffice\" width=\"100%\" height=\"100%\" classid=\"clsid:8B23EA28-2009-402F-92C4-59BE0E063499\" codebase=\"iwork_plugs/iWebOffice2009.cab#version=10,7,2,8\">").append("\n");
		fieldHtml.append("<PARAM name=\"WebUrl\" value=\"weboffice/\">").append("\n"); 
		fieldHtml.append(" <PARAM name=\"RecordID\" value=\"").append(value).append("\">").append("\n");
		fieldHtml.append(" <PARAM name=\"FileName\" value=\"").append(this.getIformMapModel().getFieldTitle()).append("\">").append("\n");  
		fieldHtml.append(" <PARAM name=\"UserName\" value=\"").append(userid).append("\">").append("\n");  
		fieldHtml.append(" <PARAM name=\"PenColor\" value=\"#FF0000\">").append("\n");
		fieldHtml.append(" <PARAM name=\"PenWidth\" value=\"1\">").append("\n");
		fieldHtml.append(" <PARAM name=\"EditType\" value=\"0,0\">").append("\n"); 
		fieldHtml.append(" <PARAM name=\"ExtParam\" value=\"\">").append("\n");
		fieldHtml.append(" <PARAM name=\"Print\" value=\"1\">").append("\n");
		fieldHtml.append("</object>").append("\n");
		fieldHtml.append("</div>").append("\n");
		String htmlInner = getIformMapModel().getHtmlInner();
		if(htmlInner==null)htmlInner="";
		fieldHtml.append("<input type='hidden'").append(htmlInner).append(" name='").append(getIformMapModel().getFieldName()).append("'  id='").append(getIformMapModel().getFieldName()).append("' ").append(" value='").append(getValue()).append("' >");
		return fieldHtml.toString();
	}

	public String getSubFormColumnModelScript(String columnModelType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> parseToJson(
			String displayString,String columnModelType,Map<String,Object> rootMap) {
		if(rootMap==null){
			rootMap = new HashMap<String,Object>();
		}
		rootMap.put("edittype", "text");
		Map<String,Object> editrulesMap = null;
		if(rootMap.containsKey("editrules")){
			editrulesMap = (Map<String,Object>)rootMap.get("editrules");
		}else{
			editrulesMap = new HashMap<String,Object>();
		}
		editrulesMap.put("url", true);
		rootMap.put("editrules", editrulesMap);
		return rootMap;
	}

}
