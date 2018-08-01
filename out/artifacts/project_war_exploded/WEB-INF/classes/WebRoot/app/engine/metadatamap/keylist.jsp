<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/design_showlist.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script> 
	<script type="text/javascript" src="iwork_js/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript">
		function add(){
			var id = $("#metadataid").val();
				var pageUrl = "sysEngineMetadataKey_add.action?metadataid="+id;
				art.dialog.open(pageUrl,{
			    	id:'metadataBaseWinDiv',
			    	title:'索引设置', 
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:520,
				    height:350,
				    close:function(){
						location.reload();
					}
				 });
		}
		function del(id){
			if(confirm("确认删除当前[键/索引]吗？")){s
				var pageurl = "sysEngineMetadataKey_remove.action?id="+id;
				$.ajax({  
		            type:'POST',
		            url:pageurl,
		            success:function(msg){ 
		            	  if(msg=="success"){
		                  	alert("移除成功！");
		                  	reload();
		                  } 
		                  else if(responseText=="error"){
		                     alert("移除失败！");
		                  } 
		            }
		        });
			}
		
	}
	function reload(){
		this.location.reload();
	}
	</script>    
</head> 
<body class="easyui-layout">
	<div region="north" style="border:0px;">
		<div class="tools_nav">
				<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">新建</a>
				<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<a href="javascript:delDataMap();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
			</div>
	</div>
	<div region="center" style="padding:0px;border:0px;" >
	<s:form name="editForm" id="editForm" theme="simple"> 
	<s:if test="list==null || list.size()==0"> 
	            <div class="none_item"><img src="iwork_img/icon_key.gif" border="0"> 【空】</div>
	 </s:if>
	 <s:else>
	 	<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<s:iterator  value="list" status="status">
					<tr>
						<td  class="right_left" style="padding-left:5px;">
					   		 <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
					      <tr>
					        <td style="width:100px"  class="right_center">
					        	<s:if test="keyType==\"primarykey\"">
						        	 <table> 
												<tr><td><img src="iwork_img/engine/icon1.png" alt="主键索引" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
												<tr><td class="icon_txt">主键索引</td></tr>
									</table>
									</s:if>
								<s:elseif test="keyType==\"foreignkey\"">
									<table width="100%" >
											<tr><td><img src="iwork_img/engine/icon2.png" alt="外键索引" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">外键索引</td></tr>
									</table>
								</s:elseif>
								<s:elseif test="keyType==\"unique\"">
									<table width="100%" >
											<tr><td><img src="iwork_img/engine/data_form.png" alt="唯一约束" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">唯一约束</td></tr>
									</table>
								</s:elseif>
								<s:elseif test="keyType==\"index\"">
									<table width="100%" >
											<tr><td><img src="iwork_img/engine/editonline.png" alt="数据索引" width="74" height="61" style=" padding:10px 0px 10px 10px"/></td></tr>
											<tr><td class="icon_txt">数据索引</td></tr>
									</table>
								</s:elseif>
					        </td>
					         <td width="1%"  class="right_center" ><img src="iwork_img/engine/vertical.jpg" width="1" height="85" /></td>
					        <td class="right_center" width="90%"> 
					           		 <table  border="0" cellspacing="0" cellpadding="0" style="width:100%;height:100%">
					           		 	<tr>
					           		 		<td class="data_title"><s:property value="%{getText('{0,date,yyyy-MM-dd }',{updatadate})}"/><a href="javascript:del(<s:property value="id"/>);"><img src="iwork_img/engine/bin_del.png" style="margin-left:5px;margin-right:5px;" border="0" /></a></td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="title">索引名：<s:property value="keyName"/></td>
					           		 	</tr>
					           		 	<tr>
					           		 		<td class="memo">
										    		<span style="width:300px;">索引字段:[<s:property value="keyField"/>]</span>
					           		 		</td>
					           		 	</tr>
					           		 	
					           		 </table>
					          </td>
					        <td ><div class="right_right"></div></td>
					      </tr>
					    </table>
					    </td>
						</tr>
			</s:iterator>
			</table>
	 </s:else>
	 <s:hidden name="metadataid" id="metadataid"></s:hidden>
			</s:form>
			</div>
			<div region="south" style="border:0px;padding:10px;border-top:1px #efefef solid">
				<fieldset  class="td_memo" style="padding:5px;color:#666">
								<legend class="td_memo_title">使用说明</legend>
								 【主键约束】设置主键用于唯一地标识表中的某一条记录，一个表不能有多个主关键字，并且主关键字的列不能包含空值<br>
								 【外键约束】设置外键是用来实现参照完整性<br>
								 【唯一约束】设置主键用于唯一地标识表中的某一条记录，唯一约束可以是多个<br>
								 【索　　引】设置索引用于大数据量查询时，增加系统查询性能，注意索引本身有一定的系统开销，慎用<br>
							</fieldset>
			</div>
</body>
</html>
