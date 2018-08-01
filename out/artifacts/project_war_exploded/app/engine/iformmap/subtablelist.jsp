<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.4.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js" ></script>
	<script type="text/javascript" src="iwork_js/engine/iformmap_subgrid.js" ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
</head>
<body class="easyui-layout">
<div region="north" border="false" style="height:40px;">
	<div  class="tools_nav">
		<a href="javascript:addSubForm(<s:property value="formid"/>);" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加子表</a>
		<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
	</div> 
</div>
<div region="center" style="padding:3px;border-top:0px;border:0px;">
<s:form name="editForm" id="editForm" theme="simple" >
		<table width="100%"   border="0" cellpadding="0" cellspacing="0">
		<s:if test="sublist==null || sublist.size()==0">
	            <div class="none_item"><img src="iwork_img/table_delete.png" border="0"> 未绑定子表模型</div>
	 	</s:if> 
			<s:iterator  value="sublist" status="status">
				<tr>
						<td  class="right_left" style="padding-left:5px;" width="98%">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr>
					        <td style="width:100px"  class="right_center">
					        	 <table>
			        	 			<s:if test="type==0||type==null">
											<tr><td><img src="iwork_img/engine/editonline.png" alt="存储模型" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">[编辑子表]</td></tr>	
										</s:if><s:else>
											 <tr><td><img src="iwork_img/engine/openwin.png" alt="视图模型" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">[视窗子表]</td></tr>
										</s:else>
								</table>
					        </td>
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center"width="90%"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title"><a href="javascript:del(<s:property value="id"/>);"><img src="iwork_img/engine/bin_del.png" style="margin-left:5px;margin-right:5px;" border="0" /></a></td>
					           		 	</tr> 
					           		 	<tr>
					           		 		<td class="title"> 
												<s:property value="title"/>
						           		 		[<s:property value="key"/>]
												
					           		 		</td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="title">
					           		 			宽高设置：
					           		 			<s:if test="autoWidth==0">
					           		 				宽度自定义
												</s:if><s:else>
													宽度:<s:property value="gridWidth"/>
												</s:else>
												/
												<s:if test="isResize==0">
					           		 				高度自定义
												</s:if><s:else>
													行高:<s:property value="height"/>
												</s:else>
					           		 		</td>
					           		 	</tr>
					           		 	<td style="padding-bottom:5px">
					           		 			<a  href="#"   onclick="editSubForm(<s:property value="formid"/>,<s:property value="id"/>);"  class="easyui-linkbutton" plain="false" iconCls="icon-sysbtn">属性设置</a>
					           		 	</td>
					           		 </table> 
					          </td>
					        <td ><div class="right_right"></div></td>
					      </tr>
					    </table>
					    </td>
						</tr>
			</s:iterator>
		</table>
		<s:hidden id="formid" name="formid" value="%{formid}"></s:hidden> 
    </s:form>
    </div>
    
</body>
</html>
