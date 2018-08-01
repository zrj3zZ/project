<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function save(){
		$.post('cmsPortletAction!save.action',{portletid:portletid,orderlist:orderlist},function(response){
    	 						if(response!='success'){ 
    	 							alert('移除错误，请稍候重试');
    	 						}
    					});
	}
	
	function closeWin(){
	
	}
</script>
  </head>
  <body class="easyui-layout"  >
  <div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
    <!-- 编辑窗口 -->
        <form id="portletform" method="post"> 
        <table width="100%" border="0" cellpadding="3" cellspacing="0">
        <tr>
	    <td align='right' width='30%'>键值：</td>
        <td width='30%'><s:textfield name= "portletkey" id="portletkey"  theme="simple"/></td>
        </tr>
        <tr>
	    <td align='right'>栏目名称：</td>
        <td colspan='2'><s:textfield name= "portletname" id="portletname"  theme="simple"/></td>
        </tr>
        <tr style="display:none">
	    <td align='right'>分组：</td>
        <td colspan='2'><input class="easyui-combobox" editable='false'	id="groupname" name="groupname" url="cmsPortletAction!combobox.action" valueField="text"  textField="text"  panelHeight="auto" required="true">&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>管理员：</td>
        <td colspan='2'><s:textfield name= "manager" id="manager"  theme="simple"/></td>
        </tr>
        <tr style="display:none">
	    <td align='right' >验证类型：</td>
        <td colspan='2'><input type="radio" checked id="verifytype" name="verifytype" value='0'>匿名访问<input type="radio" id="verifytype" name="verifytype" value='1'>登录验证&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr style="display:none">
	    <td align='right'>浏览权限：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="browse" name="browse"></input></td>
        </tr>
        <tr style="display:none">
	    <td align='right'>排序：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="orderindex" name="orderindex"></input></td>
        </tr>
        <tr>  
	    <td align='right'>类型：</td>
        <td colspan='2'>
        	
        	<input type="radio" checked id="portlettype0" name="portlettype" onclick='expandSub();' value='0' ><label for="portlettype0">资讯</lable>
       </td>
        </tr>     
        <tbody id=GROUP_SUB>
        <tr style="display:none">
	    <td align='right'><div id='paramtitle'></div></td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="param" name="param" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr style="display:none">
	    <td align='right'>More链接：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="morelink" name="morelink"></input></td>
        </tr>
        <tr style="display:none">
	    <td align='right'>提交目标：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="linktarget" name="linktarget"></input></td>
        </tr>
        </tbody>
        <tr style="display:none">
	    <td align='right'>扩展参数：</td>
        <td colspan='2'><input class="easyui-validatebox" type="text" id="template" name="template"></input></td>
        </tr>        
        <tr style="display:none">
	    <td align='right' >有效期：</td>
        <td colspan='2'><input class="easyui-datebox"  editable='false' id="begindate" name="begindate" style="width:100px;" required="true"></input>&nbsp;至&nbsp;<input class="easyui-datebox"  editable='false' id="enddate" name="enddate" style="width:100px;" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
        </tr>  
        <tr>
	    <td align='right'>状态：</td>
        <td colspan='2'><input type="radio" checked id="status" name="status" value='0'>开启<input type="radio" id="status" name="status" value='1'>关闭&nbsp;<span style='color:red'>*</span></td>
        </tr>
        <tr>
	    <td align='right'>备注：</td>
        <td colspan='2'><textarea id="memo" name="memo" style="height:60px;width:155px;"></textarea></td>
        </tr>
        </table>
        <s:hidden  id='portletid' name='portletid'></s:hidden>
        <s:hidden  id='type' name='type'></s:hidden>
        <s:hidden  id='gname' name='gname'></s:hidden>
        </form>	 
      </div>  
        <div region="south"  id="layoutSouth"  border="false" style="border-top:1px solid #efefef;padding:2px;height:35px;padding-left:10px;">
		<a  href="javascript:save();" class="easyui-linkbutton" iconCls="icon-save">保存</a>
		<a href="javascript:cancel();" class="easyui-linkbutton" iconCls="icon-cancel">取消</a> 
	 </div>     
  </body>
</html>
