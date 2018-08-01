<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title><s:property value='title' escapeHtml='false'/></title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/> 
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
		<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
	<script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/webuploader.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
	<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		$.ajaxSetup({
	        async: false
	    });
		mainFormValidator =  $("#iformMain").validate({});
		mainFormValidator.resetForm();
	});
		var foid="";
		var api,W;
		try{
			api=  art.dialog.open.api;
			W = api.opener;	
		}catch(e){}
		$(window).unload(function() {
			unLoad();
		});
		//==========================装载快捷键===============================//快捷键
		jQuery(document).bind('keydown', function(evt) {
			if (evt.ctrlKey && evt.ShiftKey) {
				return false;
			} else if (evt.ctrlKey && event.keyCode == 83) { //Ctrl+s /保存操作
				saveForm();
				return false;
			} else if (evt.ctrlKey && event.keyCode == 13) { //Ctrl+回车 顺序办理操作
				executeHandle();
				return false;
			}
		}); //快捷键
		function unLoad() {
			if (iformMain.WebOffice != undefined) {
				if(navigator.userAgent.indexOf("Firefox") > -1){
					return ;
				}else if(navigator.userAgent.indexOf("QQBrowser") > -1){
					return ;
				}
				try {
					if (!iformMain.WebOffice.WebClose()) {
						StatusMsg(iformMain.WebOffice.Status);
					} else {
						StatusMsg("关闭文档...");
					}
				} catch (e) {
				}
			}
		}
	
		window.onbeforeunload = function() {
			if (is_form_changed()) {
				
			}
		}
		function is_form_changed() {
			var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

			if (t_save.length > 0) { //检测到保存按钮,继续检测元素是否修改
				var is_changed = false;
				jQuery("#border input, #border textarea, #border select").each(
						function() {
							var _v = jQuery(this).attr('_value');
							if (typeof (_v) == 'undefined')
								_v = '';
							if (_v != jQuery(this).val())
								is_changed = true;
						});
				return is_changed;
			}
			return false;
		}
		jQuery(document).ready(
				function() {
					jQuery("#border input, #border textarea, #border select")
							.each(
									function() {
										jQuery(this).attr('_value',
												jQuery(this).val());
									});
				});
		<s:property value='script' escapeHtml='false'/>
		$(function(){  
			if($("#dgyc").val()=='2'){
				$("input[id!=''][name!=''],textarea[id!=''][name!=''],select[id!=''][name!='']").each(function() {
					$(this).attr("disabled", true);
				});
				$(".tools_nav").css("display","none");
				$("div.tools_nav").css("display","none");
				document.getElementById("iframepage").onload = function() {
					document.getElementById('iframepage').contentWindow.document.getElementById('tools_nav').style.display='none';
				};
				document.getElementById("URL").onload = function() {
                    if(document.title=="项目基本情况"){
                        var table = document.getElementById('URL').contentWindow.document.getElementById('iform_grid');
                        var table1 = document.getElementById('URL').contentWindow.document.getElementById('iform_grid1');
                        var trs = table.rows;
                        var trs2 = table1.rows;
                        var cell2 = trs2[0].cells[5];
                        cell2.style.display = 'none';
                        for(var i = 0, len = trs.length; i < len; i++){
                            var cell = trs[i].cells[5];
                            cell.style.display = 'none';
                        }
                    }else{
                        var table = document.getElementById('URL').contentWindow.document.getElementById('iform_grid');
                        var trs = table.rows;
                        for(var i = 0, len = trs.length; i < len; i++){
                            var cell = trs[i].cells[4];
                            cell.style.display = 'none';
                        }
                    }
                    if(document.title=="定向增发项目"){
                        var table1 = document.getElementById('URL').contentWindow.document.getElementById('iform_grid1');
                        var trs2 = table1.rows;
                        var cell2 = trs2[0].cells[4];
                        cell2.style.display = 'none';
                    }

				}
			}
			  var isDialogDisabled=$("#isDialogDisabled").val();
			  if(isDialogDisabled==1){
				  document.getElementById("bcid").style.display="none";
			  }
		}); 
		//项目阶段   点保存后刷新再点保存   项目阶段会重复插入
		function winreload(rld){
			var ins=$("#instanceId").val();
			if(ins!=null && ins!=""){
				rld.location.reload()
			}
		}
		function yourFunction(){
			document.getElementById('iframepage').contentWindow.document.getElementById('tools_nav').style.display='none';
		}
	</script>
	
	<style> 
		<s:property value="style" escapeHtml="false"/>
	</style>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	
</head>
<body >
	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	<s:if test="formid!=156">
		<div region="north" style="height:40px;" border="false" >
			<div  class="tools_nav">
			<span>
			<span id="bcid">
				<a href="javascript:void(0);" id="savebtn" onclick='saveForm()' class="easyui-linkbutton" plain="true" iconCls="icon-save">保存</a>	</span>
			<!-- 	<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a> -->
			<a href="javascript:void(0);" onclick="winreload(this)" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
				<!-- <a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-print">打印</a> -->
				<a id="close" href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
				</span> 
				<span style="float:right">
					<!-- <a href="#" onclick='setMenuLayout()'>布局设置</a>&nbsp;&nbsp; -->
					<s:if test="isShowLog==1&&isLog==1">
						<a href="#" onclick='showlog()'><img src="iwork_img/min_monitor.gif" border="0">修改日志</a>
					</s:if>
				</span>
			</div>
		</div>
	</s:if>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<div class="form-wrapper"  >
	<form   id="iformMain" name="iformMain" method="post" action='saveIform.action'>
		<!--表单参数-->
		<span style="display:none">
			<s:hidden name="modelId"/>
			<s:hidden name="modelType"/> 
			<s:hidden name="isLog"/> 
			<s:hidden name="formid"/>
			<s:hidden name="instanceId"/> 
			<s:hidden name="dataid"/> 
			<s:hidden name="formContent"/>
			<input value="<% out.print(request.getParameter("cid"));%>" id="cid" name="cid" type="hidden" class="{string:true}" />
			<input value="<% out.print(request.getParameter("did"));%>" id="did" name="did" type="hidden" class="{string:true}" />
			<input name='submitbtn' id='submitbtn' type="submit" />
			<s:property value='param' escapeHtml='false'/>
		</span>
		<s:property value='content' escapeHtml='false'/>
		<s:property value='NOTICETEXT' escapeHtml='false'/>
	</form>
	<input value="<% out.print(request.getParameter("PROJECTNO"));%>" id="PROJECTNO" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("isDialogDisabled"));%>" id="isDialogDisabled" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("isHFRandHFNRdiaplsy"));%>" id="isHFRandHFNRdiaplsy" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("projectNo"));%>" id="projectNo" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("instanceId"));%>" id="instanceId" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("projectname"));%>" id="projectname" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("dggbxz"));%>" id="dggbxz" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("dgxmlx"));%>" id="dgxmlx" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("dginsid"));%>" id="dginsid" type="hidden" class="{string:true}"/>
	<input value="<% out.print(request.getParameter("dgyc"));%>" id="dgyc" type="hidden" class="{string:true}"/>
	</div>
	</div>
</body>
</html>
 
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
    	  var patrn=/[`~!#$^&*+<>?"{};'[\]\\]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>