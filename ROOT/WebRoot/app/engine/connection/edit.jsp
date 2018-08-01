<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
    <script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.ztree.core-3.4.min.js"></script> 
	 <script type="text/javascript" src="iwork_js/engine/conn_page.js"></script>    
	 <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	 <script type="text/javascript">
	var api = art.dialog.open.api, W = api.opener;
	 </script>
	<style type="text/css"> 
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
			width:200px;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:0000FF; 
		}
	</style>	
</head>
<body class="easyui-layout">
            <div region="center" border="false" style="padding: 15px; background: #fff; border:0px solid #ccc;">
            <s:form name="editForm" id="editForm" action="conn_design_save.action"  theme="simple">
            	<table width="100%"  border="0" cellpadding="5" cellspacing="0"> 
            	<tr>  
            			<td class="form_title"><span style="color:red;">*</span>目录分组:</td>
            			<td class="form_data">
								<input id="citySel" type="text"  disabled value="<s:property value="groupname"/>" style="width:120px;background-color:#efefef"/> 
									&nbsp;<a id="menuBtn" href="#" onclick="showMenu(); ">选择</a>
										<s:hidden  id="groupid"  name="model.groupId" theme="simple"/> 
						</td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>接口描述:</td>
            			<td class="form_data">
            			<s:textarea name="model.inMemo" cssStyle="width:350px;height:50px;" cssClass="{maxlength:200,required:true}" ></s:textarea></td>
            		</tr>
            		<tr>  
            			<td class="form_title" style="vertical-align:top"><span style="color:red;">*</span>接口名称:</td>
            			<td class="form_data"><s:textfield name="model.inName" cssStyle="width:350px" cssClass="{maxlength:32,required:true}" theme="simple"/>
            			<div style="margin-top:10px;color:#ccc">注:SAP接口填写RFC接口名称，webservice填写调用的方法名称</div></td>
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>接口类型:</td>
            			<td  class="form_data">   
            					<s:radio  name="model.inType" cssStyle="border:0px;" onclick="setInterfaceType();" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'sap':'SAP接口','webservice':'WebService接口'}" value="model.inType" theme="simple"/>
            			</td>
            		</tr>
            		<tr id="in_addressTr">   
            			<td class="form_title" style="vertical-align:top">接口地址:</td>
            			<td class="form_data"><s:textfield name="model.inAddress" cssStyle="width:350px;" theme="simple"/><br/>
            			<div style="margin-top:10px;color:#ccc">注:适用于接口为webservice时，填写webservice地址，<br/>例如：http://mynamespace/test?wsdl</div>
            			</td>
            		</tr>  
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>是否记录缓存:</td>
            			<td  class="form_data">   
            					<s:radio  name="model.isCache" cssStyle="border:0px;" onclick="setInterfaceType();" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'1':'是','0':'否'}" value="model.isCache" theme="simple"/>
            			</td>
            		</tr> 
            		<tr>
            			<td class="form_title">缓存时间:</td>
            			<td  class="form_data">  
            					<s:textfield name="model.cacheTime" cssStyle="width:50px"  theme="simple"/> <span>默认缓存时间为3小时</span> 
            			</td> 
            		</tr> 
            		<tr>
            			<td class="form_title"><span style="color:red;">*</span>接口状态:</td>
            			<td class="form_data">
            					<s:radio  name="model.status" cssStyle="border:0px;" cssClass="{maxlength:32,required:true}" listKey="key" listValue="value"  list="#{'0':'关闭','1':'开启'}" value="model.status" theme="simple"/>
            			</td>
            		</tr>
            		<tr>
            			<td class="form_title">唯一标识:</td>
            			<td class="form_data"><s:property value="model.uuid"/></td>
            		</tr>
            	</table>
                <s:hidden name="model.id"></s:hidden>
                <s:hidden   name="model.uuid"/>
                <s:hidden name="groupid"/> 
               </s:form>  
            </div> 
            <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:5px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:doSubmit();" >
                    确定</a> <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:cancel()">关闭</a>
            </div> 
        <div id="menuContent" class="menuContent" style="display:none; background-color:#fff;border:1px solid #efefef; position: absolute;"> 
								<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul> 
							</div>
		<div id="metadataMenu" class="menuContent" style="display:none;background-color:#fff;border:1px solid #efefef;height:280px;width:250px;overflow:auto;position: absolute;"> 
								<ul id="metadatatree" class="ztree" style="margin-top:0; width:160px;"></ul> 
				</div>
</body>
</html>
