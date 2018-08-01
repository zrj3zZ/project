<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>操作管理维护</title>
	<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="iwork_css/navigation/operation_edit.css" rel="stylesheet" type="text/css" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script language="javascript" src="iwork_js/commons.js"></script>
<script language="javascript" src="iwork_js/FormCheck.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/navigation/operation_edit.js"   ></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键

		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			        $('#operation_save').submit(); return false;
		     }
}); //快捷键

	</script>
</head>
<body>
<s:form name="editForm" action="operation_save" validate="true">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pagetitle"><img src="iwork_img/icon/system.gif" border="0">
        <s:if test="null == model.oname">
            增加操作项
        </s:if>
        <s:else>
            编辑操作项
        </s:else>
	</td>
  </tr>
  <tr>
    <td >
			<table border="0" cellpadding="0" cellspacing="0" class="table1">
			  <tr>
				<td class="comtit1ogo3">&nbsp;</td>
				<td class="comtitbg">信息录入</td>
				<td class="comtitright">&nbsp;</td>
			  </tr>
			</table>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table2" >
  <tr>
    <td class="td3"><table width="100%" border="0" cellpadding="2" cellspacing="0">
      
      <tr>
	  <td width='20%'>操作名称</td>
        <td width='80%'><s:textfield name="model.oname" cssClass="{validate:{required:true,maxlength:64,messages:{required:'必填字段',maxlength:'操作名称过长'}}}" theme="simple"/><img src="iwork_img/notNull.gif" border="0"></td>
        </tr>
        <tr>
	  <td>操作action</td>
        <td><s:textfield name="model.actionurl" cssClass="{validate:{required:true,maxlength:64,messages:{required:'必填字段',maxlength:'操作action过长'}}}" theme="simple"/><img src="iwork_img/notNull.gif" border="0"></td>
        </tr>
        <tr>
		 <td>关联ID</td>
        <td>
        	<s:if test="null == model.pid">
           			 <s:textfield  name="model.pid" cssClass="{validate:{required:true,digits:true,messages:{required:'必填字段',digits:'请输入数字格式的角色组ID'}}}" theme="simple"/><img src="iwork_img/notNull.gif" border="0">
        	</s:if>
	        <s:else>
	            		<s:textfield readonly="true" name="model.pid" theme="simple" value="%{model.pid}"  cssClass="no_input" cssStyle="width:80px;color:#999999"/>
	        </s:else>
		</td>
        </tr>
      <tr>
	  <td>关联类型</td>
        <td>
        <s:if test="null == model.ptype">
           			<s:select label="" cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.ptype" value="model.ptype" list="#{'FUN':'模块菜单','DIR':'目录菜单','SYS':'系统菜单'}" theme="simple" headerKey="" headerValue="--请选择--"></s:select><img src="iwork_img/notNull.gif" border="0">
        	</s:if>
	        <s:else>
	            	<s:select label="" cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.ptype"  value="model.ptype" list="#{'FUN':'模块菜单','DIR':'目录菜单','SYS':'系统菜单'}" theme="simple"  headerKey="" headerValue="--请选择--"></s:select><img src="iwork_img/notNull.gif" border="0">
	        </s:else>
        </td>
        </tr>
      <tr>
	  <td>操作描述</td>
        <td><s:textarea name="model.odesc" cssClass="{validate:{maxlength:800,messages:{maxlength:'操作描述过长'}}}" theme="simple"></s:textarea></td>
        </tr>
      
    </table>
    </td>
  </tr>
  
</table>
	</td>
  </tr>
  <tr>
  		<td>
  			<table>
  					<tr>
  						<td width="150"><br><br></td>
  					<td>
  						<s:submit value="保存" cssClass="save_btn"  theme="simple"/>
  					</td><td>
  						<input type="button" class="back_btn" value="返回" onclick="javascript:parent.parent.GB_hide();window.location.href='<s:url action="operation_list"/>;'">  				
  					</td>
  					</tr>
  			</table>
  		</td>
  </tr>
</table>
		<s:if test="null == model">
		 	<s:hidden name="model.id" value="%{id}"/>
		 	<s:hidden name="model.orderindex" value="%{id}"/>
		 </s:if>	 	
	     <s:else>
		 	<s:hidden name="model.id" />
		 	<s:hidden name="model.orderindex"/>
		 </s:else>
		 <s:hidden name="queryName" />
		 <s:hidden name="queryValue" />	     
</s:form>
</body>
</html>
