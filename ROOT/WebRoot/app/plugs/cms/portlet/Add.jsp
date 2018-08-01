<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
<html>
<head>
</head>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"> 
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<script type="text/javascript" src="iwork_js/orguser_form.js"></script>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="easyuiDemo/js/jquery.easyui.min.1.2.2.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/org/user_edit.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
var api = art.dialog.open.api, W = api.opener;
$(document).ready(function(){
		$('#Total').tabs({
		});
		shouType();
    });
//添加与修改
function doAdd(){
	if(checkForm()){
		var obj = $('#portletform').serialize(); 
		     $.post("cmsPortletEdit.action",obj,function(data){
		           if(data=="success"){ 
	                	alert("保存成功");
	                	parent.document.location.reload();
	                	api.close();
	            	}
		     });
	}
}
function checkForm(){
	var portletkey=document.forms[0].portletkey.value;
			if(portletkey==""){
				art.dialog.tips('系统提示','键值不允许为空!','info'); 
		 		return false;
			}
			var key=document.getElementById("portletkeyspan").innerHTML;
					 if(key!="*"){
					 art.dialog.tips('系统提示','键值已存在，请更换!','info'); 
		 			  return false;
					 }			
			if(document.forms[0].portletname.value==""){
				art.dialog.tips('系统提示','栏目名称不允许为空!','info'); 
		 		return false;
			}
			if($('#groupname').combobox('getValue')==""){
				art.dialog.tips('系统提示','分组名称不允许为空!','info'); 
		 		return false;
			}
			var type=document.getElementsByName("portlettype");
				    var a;
					for(var i=0;i<type.length;i++){
						if(type[i].checked){
							a=type[i].value;
						}
					}
			 if(a==1){
				 if(document.forms[0].param.value==""){
						art.dialog.tips('系统提示','外部链接URL不允许为空!','info'); 
				 		return false;
					}
				}else if(a==2){
				 	     if(document.forms[0].param.value==""){
							  art.dialog.tips('系统提示','接口实现类不允许为空!','info'); 
				 			  return false;
							 }
						}
			if($('#begindate').datebox('getValue')==""){
					art.dialog.tips('系统提示','开始时间不允许为空!','info'); 
		 			return false;
			}
			if($('#enddate').datebox('getValue')==""){ 
			        art.dialog.tips('系统提示','结束时间不允许为空!','info'); 
		 			return false;
			}
			return true;
}
function keyVal(){
	$.ajax({
             type: "post",
             url: "cmsPortletAction!cmsKeyVal.action",
             data: {key:$("#portletkey").val(), type:$("#type").val(),portletid:$("#portletid").val()},
             cache:false,
             success: function(msg){
                     if(msg){
                     	document.getElementById("portletkeyspan").innerText = msg;
                     }else{
                     	document.getElementById("portletkeyspan").innerText = "*";
                     }
                         }
         });
}
//显示类型
function shouType(){
   
	if($("#portlettype0").attr("checked")){
		$("#GROUP_SUB").hide();
		$("#prows_tr").show();
	}
	if($("#portlettype1").attr("checked")){
		document.getElementById("paramspan").innerText = "外部链接URL";
		$("#GROUP_SUB").show();
		$("#prows_tr").hide();
	}
	if($("#portlettype2").attr("checked")){
		document.getElementById("paramspan").innerText = "接口实现类";
		$("#GROUP_SUB").show();
		$("#prows_tr").hide();
	}
	if($("#portlettype3").attr("checked")){
		document.getElementById("paramspan").innerText = "接口实现类";
		$("#GROUP_SUB").show();
		$("#prows_tr").hide();
	}
}
</script>
<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:15px;
			text-align:right;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:#FF0000; 
			padding:3px; 
		}
	</style>
<body class="easyui-layout" >
<div region="north" border="false" split="false" >
	<div class="tools_nav">
			<a href="javascript:doAdd()" class="easyui-linkbutton"  plain="true" iconCls="icon-save" >保存</a>
			<a href="javascript:this.location.reload();" class="easyui-linkbutton"  plain="true" iconCls="icon-reload" >刷新</a>
	</div>
</div>
<div region="center" border="false" split="false" >
<form name="portletform" id="portletform" action="cmsPortletEdit.action"  method="post">
<div style="color:red">
    <s:fielderror />
</div> 
                    <div id="Total"  style="width:715px;margin-left:3px;border:0px">
                    <div title="基础信息" selected="true" >
                      <table width="100%" border="0" style="height:460px" cellpadding="0" cellspacing="0" align="center">
                        <tr  >
                          <td colspan="2" width="60%" style="vertical-align:top;padding-top:30px"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td width="1"  ></td>
                                <td width="99%"  ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" align="right" class="form_title">键值:</td>
                                      <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="portletkey" name="portletkey" onKeyUp="keyVal();" value="<s:property value="model.portletkey"/>" required="true"></input>&nbsp;<span id="portletkeyspan" style='color:red'>*</span></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr> 
                                     <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">栏目名称:</td>
                                      <td width="74%"  class="form_data"><input class="easyui-validatebox" type="text" id="portletname" name="portletname" value="<s:property value="model.portletname"/>" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">分组:</td>
                                      <td width="74%" class="form_data"><input class="easyui-combobox" editable='false'	id="groupname" name="groupname" url="cmsPortletAction!combobox.action" value="<s:property value="model.groupname"/>" valueField="text"  textField="text"  panelHeight="auto" required="true">&nbsp;<span style='color:red'>*</span>
                                      </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">管理员:</td>
                                      <td width="74%"  class="form_data">
                                      <input class="easyui-validatebox" type="text" id="manager" name="manager" value="<s:property value="model.manager"/>"></input>
                                       </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">验证类型:</td>
                                      <td width="74%"  class="form_data"><input type="radio" <s:if test='model.verifytype=="0"'> checked </s:if> checked id="verifytype" name="verifytype" value='0'>匿名访问</input>
                                                                         <input type="radio" <s:if test='model.verifytype=="1"'> checked </s:if> id="verifytype" name="verifytype" value='1'>登录验证</input>
                                                                         &nbsp;<span style='color:red'>*</span>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">浏览权限:</td>
                                      <td width="74%"  class="form_data"><input class="easyui-validatebox" type="text" id="browse" name="browse" value="<s:property value="model.portletname"/>"></input></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                      <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                     <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">排序:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="orderindex" name="orderindex" value="<s:property value="model.orderindex"/>"></input></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">类型:</td>
                                        <td width="74%" class="form_data"><input type="radio" id="portlettype0" name="portlettype" <s:if test='model.portlettype==null||model.portlettype=="0"'> checked </s:if> checked onclick='javascript:shouType();' value='0' ><label for="portlettype0">资讯</lable>
        																  <input type="radio" id="portlettype1" name="portlettype" <s:if test='model.portlettype=="1"'> checked </s:if> onclick='javascript:shouType();' value='1'><label for="portlettype1">外部链接</lable>
        	 															  <input type="radio" id="portlettype2" name="portlettype" <s:if test='model.portlettype=="2"'> checked </s:if> onclick='javascript:shouType();' value='2'><label for="portlettype2">实现接口&nbsp;</lable>
        																  <input type="radio" id="portlettype3" name="portlettype" <s:if test='model.portlettype=="3"'> checked </s:if> onclick='javascript:shouType();' value='3'><label for="portlettype3">RSS资讯&nbsp;</lable>
        																  <span style='color:red'>*</span></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tbody id=GROUP_SUB style="display:none">
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title"><span id="paramspan"></span>:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="param" name="param" value="<s:property value="model.param"/>" required="true"></input>&nbsp;<span style='color:red'>*</span></td>
                                        <td width="2%">&nbsp;</td> 
                                      </tr>
                                       <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">More链接:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="morelink" name="morelink" value="<s:property value="model.morelink"/>"></input></td>
                                        <td width="2%">&nbsp;</td> 
                                      </tr>
                                       <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">提交目标:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="linktarget" name="linktarget" value="<s:property value="model.linktarget"/>"></input></td>
                                        <td width="2%">&nbsp;</td> 
                                      </tr>
                                      </tbody>
                                       <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">扩展参数:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="template" name="template" value="<s:property value="model.template"/>"></input></td>
                                        <td width="2%">&nbsp;</td> 
                                      </tr>
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                     <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">有效期:</td>
                                      <td width="74%" class="form_data">
                                     <input class="easyui-datebox"  editable='false' id="begindate" name="begindate" value="<s:property value="model.begindate"/>" style="width:100px;" required="true"></input>&nbsp;至&nbsp;<input class="easyui-datebox"  editable='false' id="enddate" name="enddate" value="<s:property value="model.enddate"/>" style="width:100px;" required="true"></input>&nbsp;<span style='color:red'>*</span>
                                     </td>
                                      <td width="2%">&nbsp;</td>
                                    </tr> 
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">状态:</td>
                                      <td width="74%"  class="form_data"><input type="radio" <s:if test='model.status=="0" || model.status==""'> checked </s:if> checked id="status" name="status" value='0'>开启</input>
                                                                         <input type="radio" <s:if test='model.status=="1"'> checked </s:if> id="status" name="status" value='1'>关闭</input>
                                                                         &nbsp;<span style='color:red'>*</span></td>
                                      <td width="2%">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td width="2%">&nbsp;</td>
                                      <td width="30%" class="form_title">备注:</td>  
                                      <td width="74%"  class="form_data"><textarea id="memo" name="memo"  style="height:60px;width:155px;"><s:property value="model.memo"/></textarea></td> 
                                      <td width="2%">&nbsp;</td> 
                                    </tr>  
                                    <tr height="1">
                                      <td colspan="4" align="center"  ></td>
                                    </tr>
                                </table></td>
                                <td width="1"></td>
                              </tr>
                          </table></td>
                        </tr>
                      </table>
                      </div>
                      <div title="扩展信息"  >
                        <table width="80%" style="height:460px" border="0" cellpadding="0" cellspacing="0" align="center">
                          <tr  id="rowData2" >
                            <td colspan="3"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td width="1"  ></td>
                                  <td width="99%"  ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                      <tr id="prows_tr" style="display:none">
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">行数:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="prows" name="prows" value="<s:property value="model.prows"/>"  size=10></input></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">宽度:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="pwidth" name="pwidth" value="<s:property value="model.pwidth"/>" size=10></input></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">高度:</td>
                                        <td width="74%" class="form_data"><input class="easyui-validatebox" type="text" id="pheight" name="pheight" value="<s:property value="model.pheight"/>" size=10></input></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                      <tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">显示边框:</td>
                                        <td width="74%" class="form_data"><input type="radio" <s:if test='model.isborder=="0"'> checked </s:if> checked id="isborder" name="isborder" value='0'>是</input>
                                                                          <input type="radio" <s:if test='model.isborder=="1"'> checked </s:if> id="isborder" name="isborder" value='1'>否</input></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      <tr height="1">
                                        <td colspan="4" align="center"  ></td>
                                      </tr>
                                     	<tr>
                                        <td width="2%">&nbsp;</td>
                                        <td width="22%" class="form_title">显示标题栏:</td>
                                        <td width="74%" class="form_data"><input type="radio" <s:if test='model.istitle=="0"'> checked </s:if> checked id="istitle" name="istitle" value='0'>是</input>
                                                                          <input type="radio" <s:if test='model.istitle=="1"'> checked </s:if> id="istitle" name="istitle" value='1'>否</input></td></td>
                                        <td width="2%">&nbsp;</td>
                                      </tr>
                                      	<input type='hidden' id='portletid' name='portletid' value="<s:property value="model.id"/>">
        								<input type='hidden' id='type' name='type' value="< s:property value ="type" />">
       									<input type='hidden' id='gname' name='gname'>
                                  </table></td>
                                  <td width="1"></td>
                                </tr>
                            </table></td>
                          </tr>
                        </table></div>
</form>
</div>
</body>
</html>
