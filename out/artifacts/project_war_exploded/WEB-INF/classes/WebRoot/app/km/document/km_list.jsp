<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>知识目录管理</title>

<link href="iwork_css/km/km_list.css" rel="stylesheet" type="text/css" />

</head>	
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script language="javascript" src="iwork_js/tablesort.js"></script>
<script language="javascript" src="iwork_js/kmjs/km_list.js"></script>
<script language="javascript" src="iwork_js/kmjs/km_common.js"></script>

<script language="javascript" src="iwork_js/km/km_list.js"></script>

<body >
<s:form name="frmMain">
<!-- 
<table align="center"  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr >
	<td  align="left" width="100%" height="30"> -->
	<div id="vista_toolbar">
		<ul>
				<li><a href='javascript:newFolder();'><span><img alt="新建文件夹" border="0" src="../iwork_img/km/newFolder.gif">新建文件夹</span></a></li>
				<li><a href='javascript:newFile();'><span><img alt="新建文件" border="0" src="../iwork_img/km/file_upload.gif">新建文件</span></a></li>
				<li><a href='javascript:newFolder();'><span><img alt="文档预览" border="0" src="../iwork_img/km/file_preview.gif">文档预览</span></a></li>
				<li><a href='javascript:newFolder();'><span><img alt="下载" border="0" src="../iwork_img/km/download.gif">下载</span></a></li>
				<li><a href='javascript:newFolder();'><span><img alt="批量下载" border="0" src="../iwork_img/file/rar.jpg">批量下载</span></a></li>
		</ul>
		</div>
	<!--</td> 
</tr>
</table>-->

	<table cellpadding="0" cellspacing="0" width='100%' cellspacing="0" onclick="sortColumn(event)">
	<THEAD>
    <tr >
        <td  width="55" class="tablehead"  style='cursor:pointer;cursor:hand;' nowrap="nowrap">序号</td>
        <td width="350" class="tablehead"   style='cursor:pointer;cursor:hand;'><span align="center" >名称</span></td>
        <td width="80" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >优先级</span></td>
        <td width="80" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >大小</span></td>
        <td width="169" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >创建人</span></td>
        <td width="163" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >更新时间</span></td>
    </tr>
    </THEAD>
    <tbody>
    <s:iterator value="folderItems" status="status">
        <tr  onMouseOver="over()"   onClick="change(this);lookupDirMethd(<s:property value="id" />);"   onMouseOut="out()" style="cursor:hand;" oncontextmenu = "showMenu(<s:property value="id" />)" >
	        <td  class="RowData2">
	         <s:checkbox id="id" name="id" onclick="change(this);"  theme="simple"/><s:property value="#status.count"/>
	        </td>
            <td class="RowData2">
	            <a href='<s:url action="km_list.action" ><s:param name="directoryid" value="id" /></s:url>' oncontextmenu = "showMenu('0')">
	            <img alt="目录" src="iwork_img/km/treeimg/ftv2link01.gif" border="0">
	            <s:property value="directoryname"/>(<s:property value="id"/>)
	            </a>
            </td>
            <td class="RowData2" oncontextmenu = "showMenu('0')"><s:property value="priority"/></td>
            <td class="RowData2" oncontextmenu = "showMenu('0')"><s:property value="filesize"/></td>
            <td class="RowData2" oncontextmenu = "showMenu('0')"><s:property value="createuser" /></td>
            <td class="RowData2" oncontextmenu = "showMenu('0')"><s:property value="updatedate" /></td>
            
        </tr>
    </s:iterator>
    <s:iterator value="fileItems" status="status">
        <tr onMouseOver="over()"   onClick="change(this)"   onMouseOut="out()" style="cursor:hand;">
	        <td class="RowData2">
	        <s:checkbox id="id" name="id" onclick="change(this);"  theme="simple"/><s:property value="#status.count"/>
	        </td>
            <td class="RowData2">
                        <a href="javascript:openFile(<s:property value="id"/>,'<s:property value="docname"/>');">

	            <img alt="文档" src="iwork_img/km/treeimg/file.gif" border="0">
	            <s:property value="docname"/>(<s:property value="id"/>)
	            </a>
            </td>
            <td class="RowData2"><s:property value="priority"/></td>
            <td class="RowData2"><s:property value="filesize"/></td>
            <td class="RowData2"><s:property value="createuser" /></td>
            <td class="RowData2"><s:property value="updatedate" /></td>
            
        </tr>
    </s:iterator>
    </tbody>
</table>
<s:hidden name="id"></s:hidden>
<!-- 这里用来定义需要显示的右键菜单 -->
<div id="itemMenu" style="display:none;">
       <table border="0" width="100%" height="100%"style="border:1px #005080 solid;" cellspacing="0">
        <tr>
                  <td style="width:30px;background-color:#cccccc">&nbsp;<br></td>
                  <td><br>
       <table border="0" width="120" height="100%" cellspacing="0" cellpadding="0">
              <tr >
                  <td style="font-size:12px;cursor:hand;border-bottom:1px #cccccc solid;border-left:1px #cccccc solid" align="center" onclick="parent.create()">新增<br></td>
              </tr>
              <tr >
                  <td style="font-size:12px;cursor:hand;border-bottom:1px #cccccc solid;border-left:1px #cccccc solid" align="center" onclick="parent.update();">修改<br></td>
              </tr>
              <tr><td   style="font-size:12px;cursor:hand;border-bottom:1px #cccccc solid;border-left:1px #cccccc solid" align="center" onclick="parent.del()">删除<br></td>
              </tr>
       </table>
       <br><br></td>
       </tr>
       </table>
</div>
<!-- 右键菜单结束-->
<s:hidden name="parentid"/>
</s:form>
</body>
</html>
<script language="JavaScript">
	
	//======设置tree焦点===========
	var parentNodeid = <s:property value="parentid"/>;
	if(parentNodeid==0)parentNodeid=1;
	parent.document.getElementById("KM_ENTERPRISE_TREE_IFRAME").contentWindow.tree.focus(parentNodeid);
	//===========END==============
</script>