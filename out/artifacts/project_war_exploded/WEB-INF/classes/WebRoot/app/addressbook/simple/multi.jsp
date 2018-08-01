<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>无标题文档</title>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/windowControl2.3.js"></script>	
<style type="text/css">
#all {
	padding-top:0px;	
	width:600px; height:500px;	
}
#top td {
	font-weight:bolder; font-family:微软雅黑; font-size:16px
}
#top a {
	text-decoration:none;	
}
#type {
	font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
	margin-left:20px;
}
#content {
	
}
#leftbox {
	font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
	width:250px;
	height:auto;
	margin-left:20px;
	float:left;
}

#rightbox {
	font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
	width:250px;
	height:auto;
	float:right;
	margin-right:20px;
	text-align:center;
}

#h_toolbar {
	font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:center;
	width:60px;	
	height:400px;
	vertical-align:middle;
	padding-top:200px;
	padding-bottom:200px;	
	float:left;
}

#v_toolbar {
	font-weight:bolder; font-family:微软雅黑; font-size:12px; text-align:left;
	width:50px;
	height:auto;
	margin-left:20px;
	margin-right:20px;
	float:left;
}

</style>

<script type="text/javascript">
		$(function(){
			//$("input:radio[name='r_type']").attr("checked", "group");;
			$('#dept').toggle();
			$('input:radio[name="r_type"][value="org"]').attr("checked", true);
			$('#leftlengendtitle').html("<b><font color='808080' >部门</font></b>");
			
			var code = document.getElementById('code').value;
			var objWin = window.dialogArguments;
			
			$('#groupTree').tree({
				checkbox: true,
				url: 'multiAddressBookAction!groupTree.action?code=' + code,
				onClick:function(node){
					$(this).tree('toggle', node.target);
				}
			});
			
			$('#deptTree').tree({
				checkbox: true,
				url: 'multiAddressBookAction!orgTree.action?code=' + code + '&parentDept=' + objWin.parentDept + '&currentDept=' + objWin.currentDept + '&startDept=' + objWin.startDept,
				onClick:function(node){
					$(this).tree('toggle', node.target);
				}
			});
			
			$('#roleTree').tree({
				checkbox: true,
				url: 'multiAddressBookAction!roleTree.action?code=' + code,
				onClick:function(node){
					$(this).tree('toggle', node.target);
				}
			});
			
		});
		
		
		function changeType(button) {
			var type = button.value;
			if (type == 'dept') {
				$('#leftlengendtitle').html("<b><font color='808080' >部门</font></b>");
				$('#group').hide();
				$('#role').hide();
				$('#per').hide();
				$('#dept').toggle();
			} else if (type == 'group') {
				$('#leftlengendtitle').html("<b><font color='808080' >团队</font></b>");
				$('#dept').hide();
				$('#role').hide();
				$('#per').hide();
				$('#group').toggle();
			} else if (type == 'role') {
				$('#leftlengendtitle').html("<b><font color='808080' >角色</font></b>");
				$('#group').hide();
				$('#dept').hide();
				$('#per').hide();
				$('#role').toggle();
			} else if (type == 'per') {
				$('#leftlengendtitle').html("<b><font color='808080' >人员</font></b>");
				$('#dept').hide();
				$('#role').hide();
				$('#group').hide();
				$('#per').toggle();
			}
		}		
		
		
		// 判断select选项中 是否存在Value="paraValue"的Item        
		function existItem(objSelect, objItemValue) {        
    		var exist = false;        
    		for (var i = 0; i < objSelect.options.length; i++) {        
       			if (objSelect.options[i].value == objItemValue) {        
            		exist = true;        
            		break;        
        		}        
    		}        
    		return exist;        
		} 
		
		function endWith(s1,s2)  
		{  
     		 if(s1.length<s2.length)  
        		return   false;  
      		 if(s1==s2)  
        		return   true;  
     		 if(s1.substring(s1.length-s2.length)==s2)  
         		 return   true;  
      		 return   false;  
		}
		
		// 添加指定位置到select
		function addAt(selectCtl,optionValue,optionText,position) { 
			var userAgent = window.navigator.userAgent;
			if (userAgent.indexOf("MSIE") > 0) {
				var option = document.createElement("option");
				option.value = optionValue;
				option.innerText = optionText;
				selectCtl.insertBefore(option, selectCtl.options[position]);
			} else {
				selectCtl.insertBefore(new Option(optionText,optionValue), selectCtl.options[position]);
			}
		}
		
		function typeAt(type) {
			var toSelect = document.getElementById('toSelect');
			
			for (var i = 0; i < toSelect.options.length; i++) {
				if (startWith(toSelect.options[i].value, type)) {					
					return i;
				}				      			       
    		}
    		return toSelect.options.length;
		}
		
		function startWith(s1, s2) {
			if(s2==null||s2==""||s1.length==0||s2.length>s1.length)
				return false;
			if(s1.substr(0,s2.length)==s2)
				return true;
			else
				return false;
			return true;
		}
		
		function addSelect(){
			
			var groupNodes = $('#groupTree').tree('getChecked');
			var deptNodes = $('#deptTree').tree('getChecked');
			var roleNodes = $('#roleTree').tree('getChecked');		
			
			
			var toSelect = document.getElementById('toSelect');
			
			for ( var i = 0; i < deptNodes.length; i++ ){
				
				if (deptNodes[i].attributes.nodeType != 'user') {					
					continue;
				}
				var dept = '' + deptNodes[i].id;
				var exist = existItem(toSelect, dept);
				
				if (exist == false) {
					var index = typeAt("dept_");				
					
					addAt(toSelect,dept,'' + deptNodes[i].text,index);
				}
			}
			
			for ( var i = 0; i < groupNodes.length; i++ ){
				
				if (groupNodes[i].attributes.nodeType != 'user') {					
					continue;
				}
				var group = '' + groupNodes[i].id;
				var exist = existItem(toSelect, group);
				
				if (exist == false) {
					//var opt = new Option('团队|' + groupNodes[i].text, group);
					//toSelect.options.add(opt);
					var index = typeAt("group_");
					addAt(toSelect,group,'' + groupNodes[i].text,index);
				}
			}
			
			for ( var i = 0; i < roleNodes.length; i++ ){
				
				if (roleNodes[i].attributes.nodeType != 'user') {					
					continue;
				}
				var role = '' + roleNodes[i].id; 
				var exist = existItem(toSelect, role);
				
				if (exist == false) {
					//var opt = new Option('角色|' + roleNodes[i].text, role);
					//toSelect.options.add(opt);
					var index = typeAt("role_");
					addAt(toSelect,role,'' + roleNodes[i].text,index);
				}
			}
			
			
			
		}
		
		function deleteSelect() {
			var toSelect = document.getElementById('toSelect');
			for (var i = 0; i < toSelect.options.length; i++) {  
				if(toSelect.options[i].selected) {
					toSelect.remove(i);
				}
			}
		}
		
		// replaceAll 函数
		String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
    		if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
        		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
    		} else {  
        		return this.replace(reallyDo, replaceWith);  
    		}
    	};
		
		function ok() {
			
			var toSelect = document.getElementById('toSelect');
			var userCode = "";			
			
    		
    		for (var i = 0; i < toSelect.options.length; i++) {
				//if (startWith(toSelect.options[i].value, 'user_')) {					
					userCode = userCode + "," + toSelect.options[i].value;
				//}        			       
    		}
    		if (userCode.length > 0) {
    			userCode = "USER:{" + userCode.substr(1) + "}";
    		} else {
    			//userCode = "USER:{}";
    		}
    		userCode = userCode.replaceAll("user_", "", true);
    		
    		var addressCode = userCode;    		
    		var reg = /\s{2,}/g;    		
    		addressCode = addressCode.replace(reg, " ");
    		
			var obj = window.dialogArguments;
			var p = obj.win;
			p.document.getElementById(obj.defaultField).value = addressCode;
			window.returnValue = addressCode;
            window.close(); 
		}
		
		
	</script>
	
</head>

<body>

    <div id="all" align="center">
    	<div id="top">
        	<table width="90%">
            <tr>
        		<td align="left">
                <font><b>多选地址簿</b></font>
                </td>
                <td align="right">
                <a href="#" onclick="ok();">确认</a>|
                
                <a href="#" onclick="window.close();">取消</a>
                </td>
            </tr>
            </table>
        </div>
        
        <div style="border:1px solid #C0C0C0; width:auto clear:both; margin-left:2px; margin-right:2px"></div>
        
        <div id="type">
        	<!--
        	<input type="radio" value="dept" onclick="" name="r_type" onchange="changeType(this)"/> 组织结构
            <input type="radio" value="group" onclick="" name="r_type" onchange="changeType(this)" /> 工作组
            <input type="radio" value="role" onclick="" name="r_type" onchange="changeType(this)" /> 角色
             -->  
            <s:property value="typeHtml" escapeHtml="false" />      	
        </div>
        
        <div id="content">
        	<div id="leftbox">
            	<fieldset>
            	<legend id="leftlengendtitle"><b><font color="808080" >发送设置</font></b></legend>
            	<div id="dept" style=" display:none; width:auto; height:400px; border:1px #C0C0C0 solid; overflow-x:auto; overflow-y:auto;">
                   <ul id="deptTree"></ul>                    
                </div>	
                <div id="group" style=" display:none; width:auto; height:400px; border:1px #C0C0C0 solid; overflow-x:scroll;">
                	<ul id="groupTree"></ul>
                </div>
                <div id="role" style=" display:none;  width:auto; height:400px; border:1px #C0C0C0 solid; overflow-x:scroll;">
                	<ul id="roleTree"></ul>
                </div>
                
            	</fieldset>
            </div>
            
            <div id="h_toolbar">
            	<a href='#' title='添加' onClick="addSelect();"><img src=iwork_img/e_forward.gif border='0'></a><br><br/>
		    	<a href='#' title='删除' onClick="deleteSelect();"><img src=iwork_img/e_back.gif border='0'></a>
            </div>
            
            <div id="rightbox">
            	<fieldset>
            	<legend id="rightlegendtitle"><b><font color="808080" >已选列表</font></b></legend>
            	<div style=" width:auto; height:400px; border:1px #C0C0C0 solid;">
            		<s:property value='selectHtml'  escapeHtml='false'/>
                </div>	
            	</fieldset>
            </div>
            
        	<div id="v_toolbar">
            	
            </div>
        </div>
    	<s:hidden name="code"></s:hidden>
    </div>
</body>
</html>
