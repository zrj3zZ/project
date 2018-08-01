<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>授权地址簿</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/portal.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css"/>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/windowControl2.3.js"></script>	
	<script type="text/javascript" src="iwork_js/addressbook/authorityaddressbookaction.js"></script>
    <link rel="stylesheet" type="text/css" href="iwork_css/addressbook/authorityaddressbookaction.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script> 
</head>
<body>
	<form>
	    <div id="all" align="center">
	    	<div id="top">
	        	<table width="90%">
	            <tr>
	        		<td align="left">
	                <font><b>授权地址簿</b></font>
	                </td>
	                <td align="right">
	                <a href="#" onclick="ok();">确认</a>|
	                
	                <a href="#" onclick="closeWin()">取消</a>
	                </td>
	            </tr>
	            </table>
	        </div>
	        
	        <div style="border:1px solid #C0C0C0; width:auto clear:both; margin-left:2px; margin-right:2px"></div>
	        
	        <div id="type">
	        	<input type="radio" value="dept" onclick="" name="r_type" onchange="changeType(this)"/> 部门
	            <input type="radio" value="group" onclick="" name="r_type" onchange="changeType(this)" /> 团队
	            <input type="radio" value="role" onclick="" name="r_type" onchange="changeType(this)" /> 角色
	            <input type="radio" value="user" onclick="" name="r_type" onchange="changeType(this)" /> 人员        	
	        </div>
	        
	        <div id="content">
	        	<div id="leftbox">
	            	<fieldset>
	            	<legend id="leftlengendtitle"><b><font color="808080" >发送设置</font></b></legend>
	            	
	            	<div id="dept" style=" display:none; width:auto; height:400px; border:1px #C0C0C0 solid; overflow-x:auto; overflow-y:auto;">
	                   <ul id="deptTree"></ul>  
	                   <div id="dept_loading" style="display:none;">
							<div style="font-size:12px;padding-left:20px;padding-top:150px;margin:3px;height:240px;background-color:#EEEEEE;">
								<img src="../iwork_img/wait.gif"/>正在加载数据...
							</div>
						</div>                  
	                </div>	
	                <div id="group" style=" display:none; width:auto; height:400px; border:1px #C0C0C0 solid; overflow-x:scroll;">
	                	<ul id="groupTree"></ul>
	                	<div id="group_loading" style="display:none;">
							<div style="font-size:12px;padding-left:20px;padding-top:150px;margin:3px;height:240px;background-color:#EEEEEE;">
								<img src="../iwork_img/wait.gif"/>正在加载数据...
							</div>
						</div>  
	                </div>
	                <div id="role" style=" display:none;  width:auto; height:400px; border:1px #C0C0C0 solid; overflow-x:scroll;">
	                	<ul id="roleTree"></ul>
	                	<div id="role_loading" style="display:none;">
							<div style="font-size:12px;padding-left:20px;padding-top:150px;margin:3px;height:240px;background-color:#EEEEEE;">
								<img src="../iwork_img/wait.gif"/>正在加载数据...
							</div>
						</div>  
	                </div>
	                <div id="user" style=" display:none;  width:auto; height:400px; border:1px #C0C0C0 solid; overflow-x:scroll;">
	                	<ul id="userTree"></ul>
	                	<div id="user_loading" style="display:none;">
							<div style="font-size:12px;padding-left:20px;padding-top:150px;margin:3px;height:240px;background-color:#EEEEEE;">
								<img src="../iwork_img/wait.gif"/>正在加载数据...
							</div>
						</div>  
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
	    	<s:hidden name="target" ></s:hidden>
	    </div>
    </form>
</body>
</html>
<script>
		var nameSymbolPrifix="|";
		var idSymbolPrifix="_";
		var comName="公司";
		var comId="com";
		var groupName="团队";
		var groupId="group";
		var deptName="部门";
		var deptId="dept";
		var roleName="角色";
		var roleId="role";
		var userName="人员";
		var userId="user";
		var comNamePrifix=comName+nameSymbolPrifix;
		var comIdPrifix=comId+idSymbolPrifix;
		var groupNamePrifix=groupName+nameSymbolPrifix;
		var groupIdPrifix=groupId+idSymbolPrifix;
		var deptNamePrifix=deptName+nameSymbolPrifix;
		var deptIdPrifix=deptId+idSymbolPrifix;
		var roleNamePrifix=roleName+nameSymbolPrifix;
		var roleIdPrifix=roleId+idSymbolPrifix;
		var userNamePrifix=userName+nameSymbolPrifix;
		var userIdPrifix=userId+idSymbolPrifix;
		$(function(){
			initTree();
			var code = document.getElementById('code').value;
			var isExpanded = true;
			
			
			$('#deptTree').tree({
				checkbox: true,
				url: 'authorityAddressBookAction!deptTree.action?code=' + "",
				onBeforeLoad:function(node, param){
					document.getElementById("dept_loading").style.display="";
				},
				onLoadSuccess:function(node, data){
					document.getElementById("dept_loading").style.display="none";
				},
				onClick:function(node){
					$(this).tree('toggle', node.target);
					if(node.checked){
							$(this).tree('uncheck', node.target);
					}else{
							$(this).tree('check', node.target);
					}
				}
			});

			$('#groupTree').tree({
				checkbox: true,
				url: 'authorityAddressBookAction!groupTree.action?code=' + "",
				onBeforeLoad:function(node, param){
					document.getElementById("group_loading").style.display="";
				},
				onLoadSuccess:function(node, data){
					document.getElementById("group_loading").style.display="none";
				},
				onClick:function(node){
					$(this).tree('toggle', node.target);
					if(node.checked){
							$(this).tree('uncheck', node.target);
					}else{
							$(this).tree('check', node.target);
					}
				}
			});
			$('#roleTree').tree({
				checkbox: true,
				url: 'authorityAddressBookAction!roleTree.action?code=' + "",
				onBeforeLoad:function(node, param){
					document.getElementById("role_loading").style.display="";
				},
				onLoadSuccess:function(node, data){
					document.getElementById("role_loading").style.display="none";
				},
				onClick:function(node){
					$(this).tree('toggle', node.target);
					if(node.checked){
							$(this).tree('uncheck', node.target);
					}else{
							$(this).tree('check', node.target);
					}
				}
			});
			$('#userTree').tree({
				checkbox: true,
				url: 'authorityAddressBookAction!personTree.action?code=' + "",
				onBeforeLoad:function(node, param){
					document.getElementById("user_loading").style.display="";
				},
				onLoadSuccess:function(node, data){
					document.getElementById("user_loading").style.display="none";
				},
				onClick:function(node){
					$(this).tree('toggle', node.target);
					if(node.checked){
							$(this).tree('uncheck', node.target);
					}else{
							$(this).tree('check', node.target);
					}
				}
			});
			/*$('#deptTree').tree({
				checkbox: true,
				cascadeCheck: false,
				url: 'authorityAddressBookAction!deptTree.action?code=' + code,
				onBeforeLoad:function(node, param){
					document.getElementById("dept_loading").style.display="";
				},
				onLoadSuccess:function(node, data){
					document.getElementById("dept_loading").style.display="none";
				},
				onClick:function(node){
					$(this).tree('toggle', node.target);
					if(node.checked){
							$(this).tree('uncheck', node.target);
					}else{
							$(this).tree('check', node.target);
					}
				},
				onCheck:function(node, checked) {				
					if (checked) {
						$(this).tree('collapse', node.target);	
						var nodes = $(this).tree('getChildren', node.target);
						for (var i = 0; i < nodes.length; i++) {							
							unCheckChildrenNodes(this, nodes[i]);							
						}			
					} else {
						$(this).tree('expand', node.target);
					}	
				}
				
			});*/
			
			
			
		});
		
</script>
