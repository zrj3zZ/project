<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
<title>子系统管理维护</title>
<s:head/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/navigation/system_edit.css" type="text/css" rel="stylesheet">
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/navigation/system_edit.js"   ></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//
		 jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
			         $("#system_save").submit(); return false;
		     }
}); //快捷键
</script>

</head>
<body>
		<s:form name="editForm" action="system_save" validate="true"
			theme="simple">
			<table width="500" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pagetitle">
						<img src="iwork_img/icon/system.gif" border="0">
						<s:if test="null == model">
            				增加子系统
        				</s:if>
						<s:else>
            				编辑子系统
        				</s:else>
					</td>
				</tr>
				<tr>
					<td>
						<div region="center" style="padding: 3px; border: 0px">
							<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<table width="100%" border="0" cellpadding="2" cellspacing="0">
											<tr>
												<td class="td_title">系统名称:</td>
												<td class="td_data">
													<s:textfield label="系统名称" cssClass="{validate:{required:true,maxlength:64,messages:{required:'必填字段',maxlength:'字段过长'}}}" name="model.sysName" />&nbsp;<span style='color: red'>*</span>
												</td>
											</tr>
											<tr>
												<td class="td_title">系统图标:</td>
												<td class="td_data">
													<s:textfield label="系统图标" cssClass="{validate:{maxlength:256,messages:{maxlength:'字段过长'}}}" name="model.sysIcon" />
												</td>
											</tr>
											<tr>
												<td class="td_title">系统URL:</td>
												<td class="td_data">
													<s:textfield label="系统URL" cssClass="{validate:{maxlength:256,messages:{maxlength:'字段过长'}}}" name="model.sysUrl" />
												</td>
											</tr>
											<tr>
												<td class="td_title">提交目标:</td>
												<td class="td_data">
													<s:select cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.sysTarget" value="model.sysTarget" list="#{'_blank':'弹出窗口【_blank】','_self':'当前窗口【_self】','_parent':'父窗口【_parent】','mainFrame':'主框架【mainFrame】','tree':'左侧框架【tree】'}"	theme="simple" headerKey="" headerValue="--请选择--"></s:select>&nbsp;<span style='color: red'>*</span>
												</td>
											</tr>
											<tr>
												<td class="td_title">服务模型:</td>
												<td class="td_data">
													<s:select name="model.sysService" value="model.sysService" list="serviceList" theme="simple" headerKey="" headerValue="--请选择--"></s:select>
												</td>
											</tr>
											<tr>
												<td class="td_title">是否显示:</td>
												<td class="td_data">
													<s:select cssClass="{validate:{required:true,messages:{required:'必填字段'}}}" name="model.sysIsHidden" value="model.sysIsHidden"	list="#{'0':'隐藏','1':'显示'}" theme="simple"></s:select>
												</td>
											</tr>
										</table>
									</td>
								</tr>

							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px; padding-top: 5px; padding-right: 15px; margin-bottom: 20px;">
							<a href='javascript:$("#system_save").submit();' class="easyui-linkbutton" iconCls="icon-ok">保存</a>
							<a href='javascript:closeWin();' class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
						</div>
					</td>
				</tr>
			</table>
			<s:if test="null == model">
				<s:hidden name="model.id" value="%{id}" />
				<s:hidden name="model.orderindex" value="%{id}" />
			</s:if>
			<s:else>
				<s:hidden name="model.id" />
				<s:hidden name="model.orderindex" />
			</s:else>
			<s:hidden name="queryName" />
			<s:hidden name="queryValue" />
		</s:form>
	</body>
</html>
