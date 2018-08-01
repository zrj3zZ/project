<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>公告呈报管理</title>
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css"/>
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
		<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"   charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.src.js"  charset="utf-8"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"  charset="utf-8" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"  charset="utf-8"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"  ></script>
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/engine/iformpage.js"  charset="utf-8" ></script>
  <script type="text/javascript">
    //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){    
      if(evt.ctrlKey&&evt.ShiftKey){
        return false;
      }
      else if(evt.ctrlKey && event.keyCode==83){ //Ctrl+s /保存操作
        saveForm(); return false;
      } 
      else if(evt.ctrlKey && event.keyCode==13){  //Ctrl+回车 顺序办理操作
          executeHandle(); return false;
      }
    }); //快捷键
    $(function(){
    	window.alert = function(str) {
    		return ;
    		}
    	
    	 $.post('zqb_rycx.action',function(data){
    			var strs= new Array();
    			strs=data.split(",");
    		document.getElementById("data").value=strs[1];
    			if(strs[0]=='1'){
    	  			document.getElementById("tjtx").style.display = "none";	
    				
    			}			
    	           });
   
    	 document.getElementById("gddh").style.display ="none";
    		var sfxs=$("#COMPANYNO").val();
    		if(sfxs!=null && sfxs!=""){
    			document.getElementById("gddh").style.display ="inline";
    		}
    
    });
    function checkGlhy(id) {
        var pageUrl = "zqb_glhy_check.action?id=" + id;
        art.dialog.open(pageUrl,{ 
            title: '会议计划表单',
            loadingText: '正在加载中,请稍后...',
            bgcolor: '#999',
            rang: true,
            width: 890,
            cache: false,
            lock: true,
            height: 530,
            iconTitle: false,
            extendDrag: true,
            autoSize: false
        });
    }
    function checkXpsx(id) {
        var pageUrl = "zqb_xpsxt_check.action?id=" + id;
        art.dialog.open(pageUrl,{
            title: '信披事项表单',
            loadingText: '正在加载中,请稍后...',
            bgcolor: '#999',
            rang: true,
            width: 890,
            cache: false,
            lock: true,
            height: 530,
            iconTitle: false,
            extendDrag: true,
            autoSize: false
        });
    }
    function downloadThisNoticeFile() {
    	  var instanceId = $("#instanceId").val();
          var noticefile = "";
          var companyname = "";
   
          if (typeof ($("#NOTICEFILE").val()) == 'undefined' && typeof ($("#COMPANYNAME").val()) == 'undefined' ) {

              $.post("zqb_nmsx_getnoticefileuuid.action", {
                  instanceId: instanceId
              }, function (data) {
                  noticefile = data;
                  if (instanceId == 0 || noticefile == "") {
                      alert("请先上传公告附件!");
                      return;
                  }
                  var strs = new Array();
                  strs = noticefile.split(",");
                  var url = encodeURI("zqb_nmsx_downloadthisnoticefile.action?instanceId=" + instanceId);
                 /*  if (strs.length > 1) {
                      var url = encodeURI("zqb_nmsx_downloadthisnoticefile.action?instanceId=" + instanceId);
                  } else {
                      var url = "uploadifyDownload.action?fileUUID=" + noticefile
                  } */
                  window.location.href = url;
              });
          } else {
              noticefile = $("#NOTICEFILE").val();
              companyname =$("#COMPANYNAME").val();
              var flag="";
              if (instanceId == 0 || (noticefile == "" && companyname=="" ) || (typeof (companyname) == 'undefined' && typeof (noticefile) == 'undefined')) {
                  alert("请先上传公告附件!")
                  return;
              }
              if(noticefile!=""){
              	flag=noticefile;
              }else{
              	flag=companyname;
              }
 			 var url = encodeURI("zqb_nmsx_downloadthisnoticefile.action?instanceId=" + instanceId);

             /*  if (strs.length +comp.length> 1) {
                  var url = encodeURI("zqb_nmsx_downloadthisnoticefile.action?instanceId=" + instanceId);
              } else {
                  var url = "uploadifyDownload.action?fileUUID=" + noticefile
              } */
              window.location.href = url;
          }
    }

    function addAdvance(){
    			var title="";
    			var startDate = $("#data").val();
    			var endDate = $("#data").val();
    			var startTime = "09:00";
    			var endTime = "17:00";
    			var allDay = 1;
    			if(startTime==endTime&&startTime=="00:00"){
    				allDay = 1;
    			}else{
    				allDay = 0;
    			}
    		var pageUrl ="addSchCalendar_Advancezs.action?startDate="+startDate+"&endDate="+endDate+"&startTime="+startTime+"&endTime="+endTime+"&allDay="+allDay+"&title=1"+encodeURI(encodeURI(title));
    				art.dialog.open(pageUrl,{
    					title:'创建日程',
    					loadingText:'正在加载中,请稍后...',
    					rang:true,
    				
    					cache:false,
    					lock: true,
    					height:600, 
    					
    					close:function(){
    						window.location.reload();
    					}
    				});
    			

    		}
    window.onload=function(){
    	var zqdm=$("#zqdms").val();
    	var zqjc=$("#zqjcs").val();
    	 document.getElementById("bt").innerHTML = zqjc+"("+zqdm+")公告呈报管理";
    }
  </script>
	
	<style> 
		pre {
			width:80;
    		overflow: auto; 
			white-space: pre-wrap; /* css-3 */
			white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
			white-space: -pre-wrap; /* Opera 4-6 */
			white-space: -o-pre-wrap; /* Opera 7 */
			word-wrap: break-word; /* Internet Explorer 5.5+ */
		}
	</style>
	
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css"/>
	
</head>
<body class="easyui-layout">
	<div id="blockPage" class="black_overlay" style="display:none"></div> 
	<div region="north" style="height:40px;" border="false" >
		<div  class="tools_nav">
		<table width="100%"><tr>
			<td align="left">
			<a href="javascript:this.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
			<a href="javascript:pageClose();" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
			</td>
			<td style="text-align:right;padding-right:10px">
				<!-- <a href="#" onclick='setMenuLayout()'>布局设置</a>&nbsp;&nbsp; -->
				
				
			</td>
		</tr></table>
		
		</div>
	</div>
	<div region="center" style="text-align:center;border-left:1px #999 dotted;border-right:1px #999 dotted;border-top:1px #999 dotted;border-bottom:0px #999 dotted;padding:2px;">
	<form   id="iformMain" name="iformMain" method="post" action='saveIform.action'>
		<!--表单参数-->
		<span style="display:none">
			<input type="hidden" name="modelId" value="" id="modelId"/>
			<input type="hidden" name="modelType" value="" id="modelType"/> 
			<input type="hidden" name="isLog" value="" id="isLog"/>		
		</span>
		<div id="border">
	<table border="0" cellpadding="0" cellspacing="0" style="margin-bottom:5px;" width="100%" class="ke-zeroborder">
		<tbody>
			<tr>
				<td class="formpage_title" id="bt">
					
				</td>
			</tr>
			<tr>
				<td align="right" id="help">
				<input type="hidden" id="data">
				<a id="tjtx" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconcls="icon-search" onclick="addAdvance();">添加事项提醒</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconcls="icon-search" onclick="downloadThisNoticeFile();">批量下载</a>
				</td>
			</tr>
			<tr>
				<td align="right" class="line">
				</td>
			</tr>
			<tr>
				<td align="left">
					<s:property value="content" escapeHtml="false"/>
				</td>
			</tr>
		</tbody>
	</table>
</div>


	</form>
	</div>
	
</body>
</html>
 
