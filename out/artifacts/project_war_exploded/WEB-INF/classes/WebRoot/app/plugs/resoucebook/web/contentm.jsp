<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>资源预定空间管理-内容管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
		<script type="text/javascript"
			src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/icon.css">
		<link rel="stylesheet" type="text/css"
			href="iwork_css/jquerycss/default/easyui.css">
		
		<link rel="stylesheet" type="text/css"
			href="iwork_css/plugs/loadcontentm.css">
		<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		
	</head>
	<body>
		<!-- TOP区 -->
		<div region="north" border="false" style="padding: 0px; overflow: no">
			<div
				style="padding: 2px; background: #efefef; border-bottom: 1px solid #efefef">
				<a href="javascript:editContent();" class="easyui-linkbutton"
					plain="true" iconCls="icon-edit">批量修改状态</a>
				<a href="javascript:removeContents();" class="easyui-linkbutton"
					plain="true" iconCls="icon-remove">批量删除预定</a>
				<a href="javascript:this.location.reload();"
					class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>

			</div>
		</div>
		<div region="center" style="padding: 3px;">
			<table id="metadata_grid" style="margin: 2px;"></table>
		</div>


		<!--修改内容 -->
		<div id="contentedit"
			style="padding: 15px; width: 380px; height: 400px;">
			<s:form>
				<table class="font">
					<tr>
						<td align=right>
							预定空间ID/名称：
							</td><td><input type=text class="easyui-validatebox" disabled name=espaceid id=espaceid size=2 style="font-size: 12px">
							/<input type=text class="easyui-validatebox" editable='false'
								name=espacename id=espacename size=12 style="font-size: 12px" disabled>
						</td>
						</tr>
						<tr>
						<td align=right>
							资源ID/资源名称：
							</td><td><input type=text class="easyui-validatebox" 
								name=eresouceid id=eresouceid size=5 style="font-size: 12px" >
							/<input type=text class="easyui-validatebox" 
								name=eresouce id=eresouce size=10 style="font-size: 12px" >
						</td>
					</tr>		
					<tr>
						<td align=right>
							预定人帐号/姓名：
							</td><td><input type=text class="easyui-validatebox" 
								name=euserid id=euserid size=10 style="font-size: 12px" >
							/<input type=text class="easyui-validatebox" 
								name=eusername id=eusername size=10 style="font-size: 12px" >
						</td>
					</tr>					
					<tr>
						<td align=right>
							预定开始时间：
							</td><td>
							<input type=text name="ebegin" id="ebegin" size=16
								style="font-size: 12px" >
						</td>
					</tr>
					<tr>
						<td align=right>
							预定结束时间：
							</td><td>
							<input type=text name="eend" id="eend" size=16
								style="font-size: 12px" >
						</td>
					</tr>
					<tr>
						<td align=right>
							状态：
							</td><td>
							<input type="radio" value="1" name="estatus" id="estatus"
								checked>
							有效
							<input type="radio" value="2" name="estatus" id="estatus">
							作废
						</td>
					</tr>
					<tr>
						<td valign="top" align=right>
							备注：
							</td><td>
							<textarea cols=23 rows=6 name="ememo" id="ememo"
								style="font-size: 12px; overflow: auto"></textarea>
						</td>
					</tr>
				</table>
				<iframe name='hidden_frame' id="hidden_frame" width="0" height="0"
					style="VISIBILITY: hidden">
				</iframe>
				<input type=hidden name=hspaceid id=hspaceid>
			</s:form>
		</div>
<!--批量修改状态 -->
		<div id="contentsedit"
			style="padding: 15px; width: 250px; height: 170px;">
			<s:form>
				<table class="font">
					<tr>
						<td>
							状态：
							<input type="radio" value="1" name="estatuss" id="estatuss"
								checked>有效
	                     <input type="radio" value="2" name="estatuss" id="estatuss">
							作废
						</td>
					</tr>
				</table>
				<iframe name='hidden_frame' id="hidden_frame" width="0" height="0"
					style="VISIBILITY: hidden">
				</iframe>
			</s:form>
		</div>

	</body>
	<script>
$(function(){	                        
		$('#metadata_grid').datagrid({
	             	url:"qRMWeb.action?cspaceid="+"<s:property value='cspaceid'  escapeHtml='false'/>",
					loadMsg: "正在加载数据...",
					fitColumns: true,
					rownumbers:true,
					onDblClickRow:function(){ 
					    var node=$('#metadata_grid').datagrid('getSelected');
					    document.forms[0].espaceid.value=node.spaceid;
					    document.forms[0].espacename.value=node.spacename;
					    document.forms[0].eresouceid.value=node.resouceid;
					    document.forms[0].eresouce.value=node.resoucename;
					    document.forms[0].euserid.value=node.userid;
					    document.forms[0].eusername.value=node.username;
					    document.forms[0].ebegin.value=node.begintime;
					    document.forms[0].eend.value=node.endtime;
					    document.forms[0].ememo.value=node.memo;
		        		var statusc=node.status;
		        		if(statusc=="开启"){
		        			statusc="1";
		        		}else if(statusc=="关闭"){
		        			statusc="2";
		        		}
		        		var ridaolen1=document.forms[0].estatus.length;
		        		for(var i=0;i<ridaolen1;i++){
		            		if(statusc==document.forms[0].estatus[i].value){
		                	document.forms[0].estatus[i].checked=true
		            		}
		        		}					  
	               		$('#contentedit').dialog('open');
	               		},
					columns:[[
					    {field:'ck',checkbox:true},
						{field:'spacename',title:'空间名称',width:60,align:'center'},
						{field:'resouceid',title:'资源ID',width:40,align:'center'},
						{field:'resoucename',title:'资源名称',width:50,align:'center'},
						{field:'userid',title:'预定人帐号',width:60,align:'center'},
						{field:'username',title:'预定人姓名',width:60,align:'center'},
						{field:'begintime',title:'开始时间',width:85,align:'center'},
						{field:'endtime',title:'结束时间',width:85,align:'center'},
						{field:'status',title:'状态',width:30,align:'center'}
						
						
					]]
					
				});	
		
				
					
		//单条修改
			$('#contentedit').dialog({
			    title:"资源预定记录管理",
			    resizable:true,
	            buttons:[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){					
					    var node=$('#metadata_grid').datagrid('getSelected');																	
						var url='editContent.action?id='+node.id;
						var resouceid=$('#eresouceid').val();	
						var resouce=$('#eresouce').val();	
						var userid=$('#euserid').val();
						var username=$('#eusername').val();
						var date1=$('#ebegin').val();
						var date2=$('#eend').val();
						if(resouceid==""){
						$.messager.alert('警告','资源ID不能为空!');
						}else if(resouce==""){
						$.messager.alert('警告','资源名称不能为空!');
						}else if(userid==""){
						$.messager.alert('警告','预定人帐号不能为空!');
						}else if(username==""){
						$.messager.alert('警告','预定人姓名不能为空!');
						}else if(date1==""){
						$.messager.alert('警告','请填写正确的预定开始时间!');						
						}else if(date2==""){
						$.messager.alert('警告','请填写正确的预定结束时间!');						
						}else if(date1>date2){
						$.messager.alert('警告','预定开始时间不能大于预定结束时间!');						
						}else{
						document.forms[0].action=url;  
						document.forms[0].method="post"
		                document.forms[0].target="hidden_frame";
		                document.forms[0].submit();
		                $('#contentedit').dialog('close');
		                
		                 $('#metadata_grid').datagrid('reload');
		                }						
					}
				},{
					text:'取消',
					handler:function(){
						$('#contentedit').dialog('close');
					}
					
				}]
			      });
			$('#contentedit').dialog('close');
			
		
		//批量状态修改
			$('#contentsedit').dialog({
			    title:"资源预定记录管理",
			    resizable:true,
	            buttons:[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){					
					   var idss = [];
						var rows = $('#metadata_grid').datagrid('getSelections');		
						for(var i=0;i<rows.length;i++){
							idss.push(rows[i].id);
						}																						
						var url='updateContents.action?ids='+idss;			
						document.forms[1].action=url;  
						document.forms[1].method="post"
		                document.forms[1].target="hidden_frame";
		                document.forms[1].submit();
		                $('#contentsedit').dialog('close');	
		               
					}
				},{
					text:'取消',
					handler:function(){
						$('#contentsedit').dialog('close');
					}
				}]
			      });
			$('#contentsedit').dialog('close');
			
		});	

		
	//批量删除记录
function removeContents(){
		var rows = $('#metadata_grid').datagrid('getSelections');
		if(rows.length==0){
		   $.messager.alert('警告','请选择要删除的记录！');
		   }else{
				 $.messager.confirm('确定','确定要删除吗?',function(result){  
				 	if(result){
	                	var idsremove = [];
						var rows = $('#metadata_grid').datagrid('getSelections');		
						for(var i=0;i<rows.length;i++){
							idsremove.push(rows[i].id);
						 }				
	                    	document.forms[0].action="removeContents.action?idsr="+idsremove;
	                    	document.forms[0].hspaceid.value='<s:property value='cspaceid'  escapeHtml='false'/>';
			 				document.forms[0].submit();			 				
			 			}
                    
                         })
            }
		
		}	
</script>

<script type="text/javascript"
			src="iwork_js/plugs/loadcontentm.js"></script>

</html>