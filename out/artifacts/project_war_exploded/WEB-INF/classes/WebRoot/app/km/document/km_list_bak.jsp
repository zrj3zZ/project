<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head><title>知识目录管理</title><style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
tr			{background: window;}
td 			{color: windowtext; font: menu; padding: 1px; padding-left: 5px; padding-right: 5px;}

table		{
			 border-top: 1px solid buttonshadow;
			 border-left: 1px solid buttonshadow;
			 border-right: 1px solid buttonhighlight;
			 border-bottom: 1px solid buttonhighlight;
			 }
thead td	{background: buttonface; font: menu; border: 1px outset white;
			 cursor: default; padding-top: 0; padding: bottom: 0;
			 border-top: 1px solid buttonhighlight;
			 border-left: 1px solid buttonhighlight;
			 border-right: 1px solid buttonshadow;
			 border-bottom: 1px solid buttonshadow;
			 height: 22px;
			 }
thead .arrow{font-family: webdings; color: black; padding: 0; font-size: 10px;
			height: 11px; width: 10px; overflow: hidden;
			margin-bottom: 2; margin-top: -3; padding: 0; padding-top: 0; padding-bottom: 2;}
			/*nice vertical positioning :-) */
#itemMenu{
	padding-left:10px;
	border:1px #005080 solid;
}
ul {
	padding-top: 5px;
	padding-bottom: 5px;
	margin: 0px 0;
	list-style: none;
	float: left;
	clear: left;
	width:100%;
}

ul li {
	float: left;
	display: inline; /*For ignore double margin in IE6*/
	margin: 0 3px;
}

ul li a {
	text-decoration: none;
	float:left;
	margin: 0px 0;
	color: #999;
	cursor: pointer;
	font: 900 12px "Arial", Helvetica, sans-serif;
}
/*button select*/

ul.select li a.current, ul.select li a:hover  {
 	color: #576d07;
	background: url(iwork_img/km/scren_btn.png) no-repeat top right;
}

ul.select li a.current span, ul.select li a:hover span {
	background: url(iwork_img/km/scren_btn.png) no-repeat top left;
}
-->

</style>
</head>	
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script language="javascript" src="iwork_js/tablesort.js"></script>
<script language="javascript" src="iwork_js/kmjs/km_list.js"></script>
<script language="javascript">
	function tr_bgcolor(c){
		var tr = c.parentNode.parentNode;
		tr.rowIndex % 2 == 0 ? tr.style.backgroundColor = c.checked ? '#add6a6' : '#eee' : tr.style.backgroundColor = c.checked ? '#add6d6' : '';
}
function selall(obj){
	for (var i=0; i<obj.form.elements.length; i++)
	if (obj.form.elements[i].type == 'checkbox' && obj.form.elements[i] != obj){
		obj.form.elements[i].checked = obj.checked;
		tr_bgcolor(obj.form.elements[i]);
	}
}

//右键菜单
</script>
<body >
<s:form name="frmMain">
<table align="center"  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr >
	<td  align="left" width="100%" height="30">
		<ul class="select">
				<li><a href='javascript:newFolder();'><img alt="新建文件夹" border="0" src="iwork_img/km/newFolder.gif">新建文件夹</a></li>
				<li><a href='javascript:newFile();'><img alt="新建文件" border="0" src="iwork_img/km/file_upload.gif">新建文件</a></li>
				<li><a href='javascript:newFolder();'><img alt="文档预览" border="0" src="iwork_img/km/file_preview.gif">文档预览</a></li>
				<li><a href='javascript:newFolder();'><img alt="下载" border="0" src="iwork_img/km/download.gif">下载</a></li>
				<li><a href='javascript:newFolder();'><img alt="批量下载" border="0" src="iwork_img/file/rar.jpg">批量下载</a></li>
		</ul>
	</td> 
</tr>
</table>

	<table cellpadding="0" cellspacing="0" width='100%' cellspacing="0" onclick="sortColumn(event)">
	<THEAD>
    <tr >
        <td  width="55" class="tablehead"  nowrap="nowrap">序号</td>
        <td width="350" class="tablehead"   style='cursor:pointer;cursor:hand;'><span align="center" >名称</span></td>
        <td width="80" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >优先级</span></td>
        <td width="80" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >大小</span></td>
        <td width="169" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >创建人</span></td>
        <td width="163" class="tablehead"  style='cursor:pointer;cursor:hand;' ><span align="center" >更新时间</span></td>
    </tr>
    </THEAD>
    <tbody>
    <s:iterator value="folderItems" status="status">
        <tr  onMouseOver="over()"   onClick="change(this)"   onMouseOut="out()" style="cursor:hand;" oncontextmenu = "showMenu('0')" >
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
	            <a href='<s:url action="km_doc_load" ><s:param name="id" value="id" /></s:url>' rel="gb_page_center[540,300]">
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
                  <td style="width:30px;background-color:#cccccc">&nbsp;</td>
                  <td>
       <table border="0" width="120" height="100%" cellspacing="0" cellpadding="0">
              <tr >
                  <td style="font-size:12px;cursor:hand;border-bottom:1px #cccccc solid;border-left:1px #cccccc solid" align="center" onclick="parent.create()">新增</td>
              </tr>
              <tr >
                  <td style="font-size:12px;cursor:hand;border-bottom:1px #cccccc solid;border-left:1px #cccccc solid" align="center" onclick="parent.update();">修改</td>
              </tr>
              <tr><td   style="font-size:12px;cursor:hand;border-bottom:1px #cccccc solid;border-left:1px #cccccc solid" align="center" onclick="parent.del()">删除</td>
              </tr>
       </table>
       </td>
       </tr>
       </table>
</div>
<!-- 右键菜单结束-->
<s:hidden name="parentid"/>
</s:form>
</body>
</html>
<script language="JavaScript">
	/**
	*根据传入的id显示右键菜单
	*/
	function showMenu(id){
		frmMain.id.value = id;
		if("" == id){
		    popMenu(itemMenu,150,"100");
		}else{
		    popMenu(itemMenu,150,"111");
		}
		event.returnValue=false;
		event.cancelBubble=true;
		return false;
	}
	/**
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString){
			//创建弹出菜单
			var pop=window.createPopup();
			//设置弹出菜单的内容
			pop.document.body.innerHTML=menuDiv.innerHTML;
			var rowObjs=pop.document.body.all[0].rows;
			//获得弹出菜单的行数
			var rowCount=rowObjs.length;
			//循环设置每行的属性
			for(var i=0;i<rowObjs.length;i++)
			{
			    //如果设置该行不显示，则行数减一
			    var hide=rowControlString.charAt(i)!='1';
			    if(hide){
			        rowCount--;
			    }
			    //设置是否显示该行
			    rowObjs[i].style.display=(hide)?"none":"";
			    //设置鼠标滑入该行时的效果
			    rowObjs[i].cells[0].onmouseover=function()
			    {
			       // this.style.background="#818181";
			        this.style.color="#005080";
			    }
			    //设置鼠标滑出该行时的效果
			    rowObjs[i].cells[0].onmouseout=function(){
			       // this.style.background="#cccccc";
			        this.style.color="black";
			    }
			}
			//屏蔽菜单的菜单
			pop.document.oncontextmenu=function()
			{
			        return false;
			}
			//选择右键菜单的一项后，菜单隐藏
			pop.document.onclick=function()
			{
			        pop.hide();
			}
			//显示菜单
			pop.show(event.clientX-1,event.clientY,width,rowCount*85,document.body);
			return true;
	}
	function create()
	{
		alert("create" + frmMain.id.value + "!");
	}
	function update()
	{
		alert("update" + frmMain.id.value + "!");
	}
	function del()
	{
	  	alert("delete" + frmMain.id.value + "!");
	}
	function clickMenu()
	{
	  	alert("you click a menu!");
	}
	//======设置tree焦点===========
	var parentNodeid = <s:property value="parentid"/>;
	if(parentNodeid==0)parentNodeid=1;
	parent.document.getElementById("KM_ENTERPRISE_TREE_IFRAME").contentWindow.tree.focus(parentNodeid);
	//===========END==============
</script>