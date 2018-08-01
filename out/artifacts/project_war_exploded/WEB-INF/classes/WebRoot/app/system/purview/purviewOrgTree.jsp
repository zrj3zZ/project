<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK�综合应用管理系统ϵͳ</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">	
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/titleSelect.css">
	<link href="iwork_css/system/showpurvieworglist.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/system/showpurvieworglist.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
		var api = art.dialog.open.api, W = api.opener;
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			        setPurViewOrg(); return false;
		     }
}); //快捷键

</script>
</head>
<body>
<s:form name="editForm" theme="simple">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
     <td>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" >
	      <tr>
      	    <td style='padding-left:10px;height: 35px;border-bottom:2px #990033 solid;' width="1%"><img src="iwork_img/icon/industry.gif"></td>
          	<td style='height: 35px;border-bottom:2px #990033 solid;' align="left"><span id="title_span" style='font-size:18px;font-family:黑体'>权限组【用户】授权</span></td>
           </tr>
	    </table>
	  </td>  
  </tr>
  
  <tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="2">
    <tr> 
      <td valign="top">
      	<table class="layout-grid" cellspacing="0" cellpadding="0" style="width:100%;" >
	      <tr>
		    <td class="left-nav">
			   <dl class="demos-nav">
				  <dd><a  href="openpurview_web.action?id=<s:property value='id'/>">导航权限管理</a></dd>
				  <dd><a  href="openpurviewToOperation_web.action?id=<s:property value='id'/>" >操作权限管理</a></dd>
			      <dd><a  href="openpurviewToRole_web.action?id=<s:property value='id'/>" >角色权限管理</a></dd>
				  <dd><a  class="selected" href="openPurviewOrgTree.action?id=<s:property value='id'/>" >用户权限管理</a></dd>
			   </dl>
		    </td>
	      </tr>
        </table>
      </td>
    
   <td  width="85%" colspan="0"  valign="top"">
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">   
         <tr>
           <td align="left"  height='20' style="border-bottom:1px #CCCCCC solid;padding-bottom:5px;bgcolor:#9C9A69">
             <input type="button" value="授权"  onclick="setPurViewOrg();" class="save_btn" />&nbsp;&nbsp;
             <input type="button" value="退出" onclick="win_close();"  class="back_btn"/>
           </td>
         </tr>
         <tr>
           <td>   
	          <div id="tt" style="width:538px;height:300px;">
			     <div title="树视图" style="padding:5px;">				
				    <s:hidden name="purviewid" ></s:hidden>
					<input type=hidden name="nodelist">
					<a href="#" onclick="expandAll();">展开全部</a>&nbsp;&nbsp;
					<a href="#" onclick="collapseAll();">收起全部</a>
					<s:property value="Info"  escapeHtml="false"/>					
					<ul id="purviewOrgtree"></ul> 
			     </div>
		
		         <div title="权限列表" closable="false"  fit ="true" style="padding:5px;" >
			       <table id="pgrid" class="easyui-datagrid" fit="true">
				     <thead>
					   <tr>
						  <th field="ID" width="60">ID</th>
						  <th field="PTYPE" width="80">PTYPEID</th>
						  <th field="PTYPENAME" width="100">PTYPENAME</th>
						  <th field="ORGID" width="80">ORGID</th>
						  <th field="ORGNAME" width="100">ORGNAME</th>
					   </tr>
				    </thead>				
			       </table>				
			     </div>
	         </div>
	      </td>
	    </tr>
	 </table>
	        
     </td>
   </tr>
</table>
  </td>
    </tr>
      </table>	
         </s:form>
	
</body>
</html>
