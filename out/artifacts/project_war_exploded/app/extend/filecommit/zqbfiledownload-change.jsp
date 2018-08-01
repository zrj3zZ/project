<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link href="iwork_js/jqueryjs/Progressbar-css/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css"/>
<link href="iwork_js/jqueryjs/Progressbar-css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>

<%-- <script type="text/javascript" src="iwork_js/jqueryjs/Progressbar-js/jquery-1.6.2.min.js"></script>
 --%><script type="text/javascript" src="iwork_js/jqueryjs/Progressbar-js/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/Progressbar-js/script.js"></script>
<script type="text/javascript">
	var mainFormValidator;
	$().ready(function() {
		$.ajaxSetup({
			async: false
		});
		mainFormValidator =  $("#ifrmMain").validate({});
		mainFormValidator.resetForm();
	});
	//单个下载审批意见
    function downloadSpyjFile(customername,zqdm,zqjc) {
        //	animProgressbar(zqdm);
        var gn = $("#GN").val();
        var sja = $("#SJA").val();
        var sjb = $("#SJB").val();
        var xznr ="审批意见";
        var downUrl = encodeURI("zqb_file_downloadZipChange.action?customername="+customername+"&zqdm="+zqdm+"&gn="+gn+"&sja="+sja+"&sjb="+sjb+"&xznr="+xznr+"&zqjc="+zqjc);
         window.open(downUrl);

    }
    //压缩文件
	function downloadFile(customername,zqdm,zqjc,gsmc) {
        $('#dgwj'+zqdm).text('压缩中...');
        document.getElementById("dgwj"+zqdm).onclick = function(){}
        alert("因压缩文件过大所需时间过长，请稍后点击“下载压缩的底稿文件”再行下载底稿压缩文件。");
	//	animProgressbar(zqdm);
		var gn = $("#GN").val();
	    var sja = $("#SJA").val();
	    var sjb = $("#SJB").val();
        var xznr ="所有附件";
	    var downUrl = encodeURI("zqb_file_yszip.action?customername="+customername+"&zqdm="+zqdm+"&gn="+gn+"&sja="+sja+"&sjb="+sjb+"&xznr="+xznr+"&zqjc="+zqjc+"&gsmc="+gsmc);
	    $.post(downUrl,function(data){ 
   			if(data=='0'){
                $('#dgwj'+zqdm).text('压缩底稿文件');
				alert("实体文件不存在!");

   			}
		});
	}
	function downZip() {
		var fileuuidarray=document.getElementsByName('fileuuid'); 
		var fileuuidlist='';
		for(var i=0; i<fileuuidarray.length; i++){
			if(fileuuidarray[i].checked){
				fileuuidlist+=fileuuidarray[i].value+'\\\\';
			}
		}
		var filename=parent.document.getElementById("filename").value;
		var downUrl = encodeURI("zqb_file_downloadZip.action?filename="+filename+"+"+"&fileuuidlist="+fileuuidlist);
		if(fileuuidlist!=''){
			window.location.href = downUrl;	
		}else{
			alert("未勾选文件!");
		}
	}
	$(function(){
		$("#chkAll").bind("click", function () {
	    	$("[name =czz]:checkbox").attr("checked", this.checked);
	    });
		$('#pp').pagination({  
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
		$("#search").click(function(){
			var pageNumber = $("#pageNumber").val();
			var pageSize = $("#pageSize").val();
			var customername = $("#CUSTOMERNAME").val();
		    var zqdm = $("#ZQDM").val();
		    var gn = $("#GN").val();
		    var sja = $("#SJA").val();
		    var sjb = $("#SJB").val();
            var xznr = $("#xznr option:selected").val();
		    var seachUrl = encodeURI("zqb_file_download_change.action?customername="+customername+"&zqdm="+zqdm+"&gn="+gn+"&sja="+sja+"&sjb="+sjb+"&pageNumber="+pageNumber+"&pageSize="+pageSize+"&xznr="+xznr);
	        window.location.href = seachUrl;
		});
		$("#package").click(function(){
			//alert(123);
			//var chk_value =[];
			var chkvalue = "";
			var gn = $("#GN").val();
		    var sja = $("#SJA").val();
		    var sjb = $("#SJB").val();
            var xznr ="审批意见";
			$('input[name="czz"]:checked').each(function(){
				chkvalue+=($(this).val()+",");
			});
			if(chkvalue.length>999){
			    alert("最多选择50家公司");
			    return false;
            }
			if(chkvalue!=""){
				var downUrl = encodeURI("zqb_file_downloadList.action?chkvalue="+chkvalue+"&gn="+gn+"&sja="+sja+"&sjb="+sjb+"&xznr="+xznr+"&qb=0");
                window.open(downUrl);
			}else{
				alert("未勾选公司!");
			}
			//alert(chk_value.length==0 ?'你还没有选择任何内容！':chk_value); 
		});
        $("#qbpackage").click(function(){
            //alert(123);
            //var chk_value =[];
            var chkvalue = "";
            var gn = $("#GN").val();
            var sja = $("#SJA").val();
            var sjb = $("#SJB").val();
            var xznr =$("#xznr option:selected").val();
            $('input[name="czz"]:checked').each(function(){
                chkvalue+=($(this).val()+",");
            });

            var downUrl = encodeURI("zqb_file_downloadList.action?chkvalue="+chkvalue+"&gn="+gn+"&sja="+sja+"&sjb="+sjb+"&xznr="+xznr+"&qb=1");
            window.open(downUrl);


        });
	});
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	function downloadItem(){
        var pageUrl = "zqb_downloaditem.action";
        var target = "_blank";
        var win_width = window.screen.width;
        var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
        page.location = pageUrl;
    }
</script>
<style type="text/css">
.searchtitle {
	text-align: right;
	padding: 5px;
}

.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
	height: 28px;
	font-size: 12px;
	vertical-align: text-middle;
	padding-top: 2px;
}

.header td {
	height: 35px;
	font-size: 12px;
	padding: 3px;
	white-space: nowrap;
	padding-left: 5px;
	background: url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left
		bottom;
	border-top: 1px dotted #ccc;
	border-right: 1px solid #eee;
}

.cell td {
	margin: 0;
	padding: 3px 4px;
	height: 25px;
	font-size: 12px;
	white-space: nowrap;
	word-wrap: normal;
	overflow: hidden;
	text-align: center;
	border-bottom: 1px dotted #eee;
	border-top: 1px dotted #fff;
	border-right: 1px dotted #eee;
}

.cell:hover {
	background-color: #F0F0F0;
}
</style>
</head>
<body class="easyui-layout">
<div region="north" border="false">
    <div class="tools_nav">

       <a href="javascript:downloadItem();" class="easyui-linkbutton"
               plain="true" iconCls="icon-add">下载压缩的底稿文件</a>

        <a href="javascript:window.location.reload();"
           class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>

    </div>
</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top;width:100%;">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px">
				<div
					style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE;">
					<table width='98%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">证券代码</td>
										<td class="searchdata"><input type='text' class='{maxlength:128,required:false}' style="width:100px" name='ZQDM' id='ZQDM' value=''></td>
										<td class="searchtitle">公司名称</td>
										<td class="searchdata"><input type='text' class='{maxlength:64,required:false}' style="width:100px" name='CUSTOMERNAME' id='CUSTOMERNAME' value=''></td>
                                       <%-- <td class="searchtitle">下载内容</td>--%>
                                       <%-- <td class="searchdata">
                                            <select id="xznr">
                                                <option value="审批意见">审批意见</option>
                                                <option value="所有附件">所有附件</option>
                                            </select>
                                        </td>--%>
                                        <td class="searchdata"  colspan="2" style="color: red;vertical-align: top;">注：压缩的底稿文件会 ”每周日21:00清理“，请即时下载。</td>
									</tr>
									<tr>
										<td class="searchtitle">模块名称</td>
										<td class="searchdata">
											<s:select list="gnlist" theme="simple" headerKey="" headerValue="-空-" name="GN" id="GN"></s:select>
										</td>
										<td class="searchtitle">日期从</td>
										<td class="searchdata">
										<input type='text' onfocus="WdatePicker();" class='{maxlength:64,required:false}' style="width:80px;" name='SJA' id='SJA' value=''>
										到
										<input type='text' onfocus="WdatePicker();" class='{maxlength:64,required:false}' style="width:80px;" name='SJB' id='SJB' value=''></td>
                                        <td valign='bottom' style='padding-bottom:5px;'><a id="search" class="easyui-linkbutton" icon="icon-search" href="javascript:void(0);">查询</a></td>
                                        <td valign='bottom' style='padding-bottom:5px;'><a id="package" class="easyui-linkbutton"  href="javascript:void(0);">批量下载审批意见</a></td>
									</tr>
								</table>
							<td>

                         <%--   <td valign='bottom' style='padding-bottom:5px;'><a id="qbpackage" class="easyui-linkbutton"  href="javascript:void(0);">全部下载</a></td>--%>
						<tr>
					</table>
				</div>
			</div>
			<span style="disabled:none"> <input type="hidden"
				name="formid" value="88" id="formid" /> <input type="hidden"
				name="demId" value="21" id="demId" /> <input type="hidden"
				name="idlist" id="idlist" value='11'>

			</span>
		</form>
		<div style="padding:5px;text-align: center;width:98%;">
			<table id='iform_grid' style="border:1px solid #efefef;width:100%;">
				<tr class="header">
					<td style="width:2.5%;" align="center"><input type="checkbox" name="colname" id="chkAll" /></td>
					<td style="width:14.5%;" align="center">证券代码</td>
					<td style="width:14.5%;" align="center">证券简称</td>
					<td style="width:22%;" align="center">公司名称</td>
					<td colspan="2" style="width:12%;" align="center">下载</td>
				</tr>
				<s:iterator value="downlist" status="status">
					<tr class="cell">
					<%--	<td><input name="czz" type="checkbox" value="<s:property value="CUSTOMERNAME" />/-/<s:property value="ZQJC" />/-/<s:property value="ZQDM" />"/></td>--%>
                        <td><input name="czz" type="checkbox" value="<s:property value='KHBH' />"/></td>
						<td><s:property value="ZQDM" /></td>
						<td><s:property value="ZQJC" /></td> 
						<td><s:property value="CUSTOMERNAME" /></td>
<%--
						<td style="border-right:none;width:7%;"><div class="<s:property value="ZQDM" />"><div class="pbar"></div></div></td>
--%>
						<td >
                            <button id="dgwj<s:property value="ZQDM" />" onclick="downloadFile('<s:property value="KHBH" />','<s:property value="ZQDM" />','<s:property value="ZQJC" />','<s:property value="CUSTOMERNAME" />');">压缩底稿文件</button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button onclick="downloadSpyjFile('<s:property value="KHBH" />','<s:property value="ZQDM" />','<s:property value="ZQJC" />');">下载审批意见</button></td>
					</tr>
				</s:iterator>
				<tr style="clear:left;"></tr>
			</table>

			<form action="zqb_file_download_change.action" method=post name=frmMain id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
				<s:hidden name="gn" id="gn"></s:hidden>
				<s:hidden name="sja" id="sja"></s:hidden>
				<s:hidden name="sjb" id="sjb"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south" style="vertical-align:bottom;height:40px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;display:none;" border="false">
		<div style="padding:5px">
			<s:if test="totalNum==0">

			</s:if>
			<s:else>
				<div id="pp"
					style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
			</s:else>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$("#CUSTOMERNAME").val($("#customername").val());
		$("#ZQDM").val($("#zqdm").val());
		$("#GN").val($("#gn").val());
		$("#SJA").val($("#sja").val());
		$("#SJB").val($("#sjb").val());
	});
	</script>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
<script language="JavaScript"> 
  jQuery.validator.addMethod("string", function(value, element) {
          var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
          var patrn=/[`~!#$%^&*+<>?"{},;'[\]]/im;
    	    if(patrn.test(value)){
        	}else{
            	var flag = false;
            	var tmp = value.toLowerCase();
            	for(var i=0;i<sqlstr.length;i++){
                	var str = sqlstr[i];
					if(tmp.indexOf(str)>-1){
						flag = true;
						alert("str:"+tmp+"s:"+str);
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>