<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>我的收藏夹</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/navigation/myfav.css">
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/plugins/jquery.portal.js"></script>
<script type="text/javascript" src="iwork_js/metadata_index.js" ></script>
<script type="text/javascript" src="iwork_js/selectListTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script>	
<script type="text/javascript" src="iwork_js/navigation/myfav.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
		$(function(){
		   //加载系统菜单导航树
		   var setting = {
					async: {
						enable: true, 
						url:"openjson.action",
						dataType:"json",
						autoParam:["id","nodeType"]
					},
					callback: { 
						onClick:onClick
					} 
				};
			$.fn.zTree.init($("#systree"), setting);
			//点击事件
			function onClick(event, treeId, treeNode, clickFlag){
					if(treeNode.isParent){
						var zTree = $.fn.zTree.getZTreeObj("systree");
				 		zTree.expandNode(treeNode, true, null, null, true);
					}else{
						moveSelect(treeNode,document.all.to);
					} 
			}
	});
     //保存操作
	 function save(oSourceSel){
		 var list = "";
		 for(var i=0; i<oSourceSel.options.length; i++){
			list+=oSourceSel.options[i].value+',';
		 }
		 list+=":";
		 for(var i=0; i<oSourceSel.options.length; i++){
			list+=oSourceSel.options[i].text+',';
		 }
		 
		 var url='savemyfav.action';
		 document.forms[0].favlist.value=list;
         document.forms[0].action=url;  
         document.forms[0].submit();
         return false;		
	 }
     //添加外部链接
	 function addurl(){
		 $('#idialog').dialog('open');		
	 }
	 
	 //保存外部链接
 	 function saveUrl(oSourceSel){
     //校验有效性和不为空
	 if(document.getElementsByName("funname")[0].value==''){
		 art.dialog.tips('系统提示','请填写功能名称!','info'); 
		 return false;
		}
	 for(var i=0; i<oSourceSel.options.length; i++){
	  //if(oSourceSel.options[i].value==0){
	    if(document.getElementsByName("funname")[0].value==oSourceSel.options[i].text){
	    art.dialog.tips('系统提示','此链接名称已存在,请更换!','info'); 
		return false;
		}
	  //}
	 }
	 if(document.getElementsByName("funurl")[0].value==''){
	    art.dialog.tips('系统提示','请填写URL链接!','info'); 
		return false;
	 }
	 if (document.getElementsByName("funurl")[0].value.match(/(http[s]?|ftp|file):\/\/[^\/\.]+?\..+\w$/i) == null&&document.getElementsByName("funurl")[0].value.match(/^\\\\[^\/\.]+?\..+\w$/i) == null) {
	    art.dialog.tips('系统提示','请填写正确的URL链接!','info'); 
		return false;
	 }
	    //拼凑字符串
	     var list = "";
		 for(var i=0; i<oSourceSel.options.length; i++){
			list+=oSourceSel.options[i].value+',';
		 }
		 list+=":";
		 for(var i=0; i<oSourceSel.options.length; i++){ 
			list+=oSourceSel.options[i].text+',';
		 }
		 
		 //提交
	     var url='saveurl.action';
	     document.forms[0].favlist.value=list;
	     document.forms[0].ifunname.value=document.getElementsByName("funname")[0].value;
	     document.forms[0].ifunurl.value=document.getElementsByName("funurl")[0].value;
	     document.forms[0].itarget.value=document.getElementsByName("target")[0].value;
	     document.forms[0].imemo.value=document.getElementsByName("memo")[0].value;
         document.forms[0].action=url;  
         //document.forms[0].method="post";  
         //document.forms[0].enctype="multipart/form-data" 
         //document.forms[0].target="AWS_AJAX_OPTER";
         document.forms[0].submit();
 		 return false; 	
	}
	
	//添加操作
	function addSelect(){
		var treeObj = $.fn.zTree.getZTreeObj("systree");
			var nodes = treeObj.getSelectedNodes();
			if(nodes.length<1){
				art.dialog.tips("请选择您要添加的菜单",2);
				return;
			}else{
				moveSelect(nodes[0],document.all.to);
			}
	}
	
	//添加代码
    function moveSelect(oSourceSel,oTargetSel){
	       //建立存储value和text的缓存数组
	        var arrSelValue = new Array();
	        var arrSelText = new Array();
	       //用来辅助建立缓存数组
	        var index = 0;
	        if(oSourceSel==null){
	        	 art.dialog.tips("请选择系统菜单",2);
	        	 return false;
	        }
		        //检查是否重复
			    for(var j=0;j<oTargetSel.options.length;j++){
				 if(oSourceSel.id==oTargetSel.options[j].value){				
				 art.dialog.tips("此项在收藏夹中已存在,不允许重复添加",2);
				 return false;
				 }
				}
                //存储
                arrSelValue[index] = oSourceSel.id;
                arrSelText[index] = oSourceSel.name;
                index ++;
            
	        //增加缓存的数据到目的列表框中
	        for(var i=0; i<arrSelText.length; i++)  
	        {	  
	            //增加
	            var oOption = document.createElement("option");
	            oOption.text = arrSelText[i];
	            oOption.value = arrSelValue[i];	
	            if (window.navigator.userAgent.indexOf("MSIE")>=1)
	 			oTargetSel.add(oOption);
				else
	            oTargetSel.appendChild(oOption);
	        }	   
        }
        //删除操作
        function deleteSelect(obj){
	         if(obj.value==0){ 
	         	$.dialog.confirm('移除的外部链接将不再为您保存，确定删除吗?', function(){
				    deleteSelectItem(obj);
				}, function(){
				   
				});
	         }else{
	        	 deleteSelectItem(obj);
	         }
        }
</script>  
<s:property value='pstrScript'  escapeHtml='false'/>
</head>
<body bgcolor="#FFFFFF">
<s:form action="#" method="post" name="myfav">
<table  width="98%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td align="left" style="font-family: '黑体', 'Arial', 'Helvetica', 'sans-serif'; font-size:18px; color: #999999;padding-left:10px;">
我的收藏夹
</td>
<td align="right" >
<a class="easyui-linkbutton" iconCls="icon-save" plain="true" href="javascript:void(0)" onclick="save(document.all.to)">保存</a>
<a class="easyui-linkbutton" iconCls="icon-add" plain="true" href="javascript:void(0)" onclick="addurl()">添加外部链接</a>
<a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" href="javascript:void(0)" onclick="closewin()">关闭窗口</a>
</td>
</tr>
</table>		       			
<div style="width:90%;margin-top:5px;padding:10px;background:#fff;border:1px solid #ccc;">
	<table width='100%' border='0'>
		<tr >
			<td width='10%'>&nbsp;</td>
			<td width='30%' class='td_title'>系统菜单</td>
			<td width='10%'>&nbsp;</td>
			<td width='30%' class='td_title'>我的收藏夹</td>
			<td width='20%'>&nbsp;</td>
		</tr>
		<tr>
		<td width='5%'>&nbsp;</td>
		<td valgin='top'>
			<div id='sysFrame' style='border:1px #CCCCCC solid;;width:210px;height:360px;OVERFLOW-X:auto;OVERFLOW:scroll'>
				<ul id="systree" class="ztree"></ul> 
			</div>
		</td>
		<td align=center> 
		    <a href='#' title='添加' onClick="addSelect();"><img src=iwork_img/e_forward.gif border='0'></a><br><br>
		    <a href='#' title='删除' onClick="deleteSelect(document.all.to);"><img src=iwork_img/e_back.gif border='0'></a>
		</td>
	    <td valgin='top'>
	     	<s:property value='html'  escapeHtml='false'/>
	    </td>
	    <td>
	        &nbsp;&nbsp;&nbsp;&nbsp;<a href='#' title='置顶' onClick='moveUp(document.all.to,true);document.all.to.focus();'><img src=iwork_img/arr_up.gif border='0'>&nbsp;&nbsp;置顶</a><br>
	        &nbsp;&nbsp;&nbsp;&nbsp;<a href='#' title='上移' onClick='moveUp(document.all.to);document.all.to.focus();'><img src=iwork_img/arr_u.gif border='0'>&nbsp;&nbsp;上移</a><br>
	        &nbsp;&nbsp;&nbsp;&nbsp;<a href='#' title='下移' onClick='moveDown(document.all.to);document.all.to.focus();'><img src=iwork_img/arr_d.gif border='0'>&nbsp;&nbsp;下移</a><br>
	        &nbsp;&nbsp;&nbsp;&nbsp;<a href='#' title='置底' onClick='moveDown(document.all.to,true);document.all.to.focus();'><img src=iwork_img/arr_down.gif border='0'>&nbsp;&nbsp;置底</a>
	    </td>
	    </tr>
	  </table> 
</div>

	<input type='hidden' name='favlist'>
	<input type='hidden' name='ifunname'>
	<input type='hidden' name='ifunurl'>
	<input type='hidden' name='itarget'>
	<input type='hidden' name='imemo'>
</s:form>
</body>
</html>