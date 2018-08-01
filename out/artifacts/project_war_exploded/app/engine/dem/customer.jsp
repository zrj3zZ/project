<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>IWORK综合应用管理系统</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script type="text/javascript" src="iwork_js/bindclick.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
    $.ajaxSetup({
        async: false
    });
mainFormValidator =  $("#ifrmMain").validate({
 });
 mainFormValidator.resetForm();
});
	function addProject(customername,customerno,username,tel){
		var seachUrl="zqb_project_check.action?customerno="+customerno+"&customername="+customername;
		$.ajax({
		    type:'POST',
		    async:false,
		    url:seachUrl,
		    success:function(data){
		    	if(data!=""){
		    		alert(data);
		    		window.location.reload();
		    		return;
		    	}else{
		    		var formid = 91;
		    		var demId = 22;
		    		var xmsplcServer = $("#xmsplcServer").val();
		    		// 东吴项目带审批		    		
		    		var url = "createFormInstance.action?formid="+formid+"&demId="+demId+"&CUSTOMERNAME="+encodeURI(customername)+"&CUSTOMERNO="+encodeURI(customerno)+"&KHLXR="+encodeURI(username)+"&KHLXDH="+tel+"&PROJECTNAME="+encodeURI(customername)+"&isDwPj=true";
		    		//齐鲁项目不带审批
		    		//var url = 'createFormInstance.action?formid='+formid+'&demId='+demId+'&CUSTOMERNAME='+encodeURI(customername)+'&CUSTOMERNO='+encodeURI(customerno)+'&KHLXR='+encodeURI(username)+'&KHLXDH='+tel+'&PROJECTNAME='+encodeURI(customername);
		    		var target = "_blank";
		    		var win_width = window.screen.width;
		    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		    		page.location = url;
		    		return;
		    	}
		    }
		});
	}
	
	function submitMsg(pageNumber,pageSize){
		$("#pageNumber").val(pageNumber);
		$("#pageSize").val(pageSize);
		$("#frmMain").submit();
		return ;
	}
	$(function(){
		//分页
		$('#pp').pagination({
		    total:<s:property value="totalNum"/>,  
		    pageNumber:<s:property value="pageNumber"/>,
		    pageSize:<s:property value="pageSize"/>,
		    onSelectPage:function(pageNumber, pageSize){
		    	submitMsg(pageNumber,pageSize);
		    }
		});
	    //查询
	    $("#search").click(function(){
	    	var valid = mainFormValidator.form(); //执行校验操作
	    	if(!valid){
	    	return false;
	    	}
	    	var customerName = $("#CUSTOMERNAME").val();//客户名称
			var zqdm = $("#ZQDM").val();//股票代码
			var username = $("#USERNAME").val();//信披人
			var gfgsrqbegin = $("#GFGSRQBEGIN").val();
			var gfgsrqend = $("#GFGSRQEND").val();
			var type = $("#TYPE").val();
			var zwmc = $("#ZWMC").val();//住所地省市
			var sszjj = $("#EXTEND4").val();//所属证监局
			var zczbbegin = $("#ZCZBBEGIN").val();
			var zczbend = $("#ZCZBEND").val();
			var zrfs = $("#ZRFS").val();
			var zcqx=$("#ZCQX").val();//所属部门或所属大区
			var sshy=$("#SSHY").val();//所属行业		
			var gpsjBEGIN = $("#GPSJBEGIN").val();
			var gpsjEND = $("#GPSJEND").val();
			var innovation = $("#INNOVATION").val();//创新层
			var classification = $("#CLASSIFICATION").val();//分类		
			var ygp = $("#YGP").val();		
			var status = $("#STATUS").val();
			var cxddbg = "";		
			var pagesize=$("#pageSize").val();
			var pagenumber=$("#pageNumber").val();
			        
	        var orderbyzqdm = $("#orderbyzqdm").val();
	        var orderbygpsj = $("#orderbygpsj").val();
	        var seachUrl = encodeURI("loadCustomer.action?customername="+customerName+"&zqdm="+zqdm
					+"&username="+username+"&gfgsrqbegin="+gfgsrqbegin+"&gfgsrqend="+gfgsrqend
					+"&type="+type+"&zwmc="+zwmc+"&extend4="+sszjj
					+"&zczbbegin="+zczbbegin+"&zczbend="+zczbend+"&zrfs="+zrfs
					+"&zcqx="+zcqx+"&sshy="+sshy+"&gpsjBEGIN="+gpsjBEGIN+"&gpsjEND="+gpsjEND
					+"&innovation="+innovation+"&classification="+classification+"&ygp="+ygp
					+"&status="+status+"&cxddbg="+cxddbg+"&orderbyzqdm="+orderbyzqdm+"&orderbygpsj="+orderbygpsj);
	        window.location.href = seachUrl;
	    });
	    //复选/全选
        $("#chkAll").bind("click", function () {
        	$("[name =colname]:checkbox").attr("checked", this.checked);
        });
    });
		
		function edit(instanceid){		
    		var pageUrl = "openFormPage.action?formid=88&demId=21&instanceId="+instanceid;
    		var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;
		}
		function addItem(){			
			var pageUrl = "createFormInstance.action?formid=88&demId=21";
    		var target = "_blank";
    		var win_width = window.screen.width;
    		var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
    		page.location = pageUrl;
		}	
		function remove(){
			 $.messager.confirm('确认','确认删除?',function(result){ 
			  
			 	if(result){
			 	   var list = $('[name=colname]').length;
					var a=0;
					
					 for( var n = 0; n < list; n++){
						 var flag = new Boolean();
						 if($('[name=colname]')[n].checked==false&&$('[name=colname]')[n].id!='chkAll'){
						 a++;
							 if(a==list){
							  $.messager.alert('提示信息','请选择您要删除的行项目!','info');  
								return;
							 }
						 }
						  //console.log(typeof ($('[name=colname]')[n].id)!=String('chkAll')+","+typeof ($('[name=colname]')[n].id));
					 if($('[name=colname]')[n].checked==true&&String($('[name=colname]')[n].id)!=String('chkAll')){
						 var deleteUrl = "deleteCustomer.action";
						$.post(deleteUrl,{instanceid:$('[name=colname]')[n].id},function(data){ 
		       			if(data=='success'){
		       				window.location.reload();
		       			}else{
		       				flag=false;
		       				alert(data);
		       			}
		     		 });
					 } 
					 if(!flag){break;}
               }
       }
	});
	}
		window.onbeforeunload = function() {
			if(is_form_changed()) {
			return "您的修改内容还没有保存，您确定离开吗？";
			}
			}
			function is_form_changed() {
			var t_save = jQuery("#savebtn"); //检测页面是否要保存按钮

			if(t_save.length>0) { //检测到保存按钮,继续检测元素是否修改
			var is_changed = false;
			jQuery("#border input, #border textarea, #border select").each(function() {
			var _v = jQuery(this).attr('_value');
			if(typeof(_v) == 'undefined') _v = '';
			if(_v != jQuery(this).val()) is_changed = true;
			});
			return is_changed;
			}
			return false;
			}
			jQuery(document).ready(function(){
			jQuery("#border input, #border textarea, #border select").each(function() {
			jQuery(this).attr('_value', jQuery(this).val());
			});
			}); 
	</script>
<script type="text/javascript">
	function OrderGPSJ() {	
		//排序   1正序   2倒序   3还原
		$("#ordzqdm").val("0");
		$("#ordgpzt").val("0");
		if($("#ordgpsj").val()=="0"){
			$("#ordgpsj").val("1");
			document.getElementById("ogpsj").innerHTML = "挂牌时间↑";
		}else if($("#ordgpsj").val()=="1"){
			$("#ordgpsj").val("2");
			document.getElementById("ogpsj").innerHTML = "挂牌时间↓";
		}else{
			$("#ordgpsj").val("0");
			document.getElementById("ogpsj").innerHTML = "挂牌时间";
		}
		var ordgpsj=$("#ordgpsj").val();
		var customerName = $("#CUSTOMERNAME").val();//客户名称
		var zqdm = $("#ZQDM").val();//股票代码
		var username = $("#USERNAME").val();//信披人
		var gfgsrqbegin = $("#GFGSRQBEGIN").val();
		var gfgsrqend = $("#GFGSRQEND").val();
		var type = $("#TYPE").val();
		var zwmc = $("#ZWMC").val();//住所地省市
		var sszjj = $("#EXTEND4").val();//所属证监局
		var zczbbegin = $("#ZCZBBEGIN").val();
		var zczbend = $("#ZCZBEND").val();
		var zrfs = $("#ZRFS").val();
		var zcqx=$("#ZCQX").val();//所属部门或所属大区
		var sshy=$("#SSHY").val();//所属行业		
		var gpsjBEGIN = $("#GPSJBEGIN").val();
		var gpsjEND = $("#GPSJEND").val();
		var innovation = $("#INNOVATION").val();//创新层
		var classification = $("#CLASSIFICATION").val();//分类		
		var ygp = $("#YGP").val();		
		var status = $("#STATUS").val();
		var cxddbg = "";		
		var pagesize=$("#pageSize").val();
		var pagenumber=$("#pageNumber").val();
		var seachUrl = encodeURI("loadCustomer.action?customername="+customerName+"&zqdm="+zqdm
				+"&username="+username+"&gfgsrqbegin="+gfgsrqbegin+"&gfgsrqend="+gfgsrqend
				+"&type="+type+"&zwmc="+zwmc+"&extend4="+sszjj
				+"&zczbbegin="+zczbbegin+"&zczbend="+zczbend+"&zrfs="+zrfs
				+"&zcqx="+zcqx+"&sshy="+sshy+"&gpsjBEGIN="+gpsjBEGIN+"&gpsjEND="+gpsjEND
				+"&innovation="+innovation+"&classification="+classification+"&ygp="+ygp
				+"&status="+status+"&cxddbg="+cxddbg+"&orderbyzqdm="+0+"&orderbygpsj="+ordgpsj+"&pageSize="+pagesize+"&pageNumber="+pagenumber+"&orderbygpzt="+0);
        window.location.href = seachUrl;
	}
	function OrderZQDM() {
		//排序   1正序   2倒序   3还原
		$("#ordgpsj").val("0");
		$("#ordgpzt").val("0");
		if($("#ordzqdm").val()=="0"){
			$("#ordzqdm").val("1");
			document.getElementById("ozqdm").innerHTML = "证券代码(股票代码)↑";
		}else if($("#ordzqdm").val()=="1"){
			$("#ordzqdm").val("2");
			document.getElementById("ozqdm").innerHTML = "证券代码(股票代码)↓";
		}else{
			$("#ordzqdm").val("0");
			document.getElementById("ozqdm").innerHTML = "证券代码(股票代码)";
		}
		var ordzqdm=$("#ordzqdm").val();
		var customerName = $("#CUSTOMERNAME").val();//客户名称
		var zqdm = $("#ZQDM").val();//股票代码
		var username = $("#USERNAME").val();//信披人
		var gfgsrqbegin = $("#GFGSRQBEGIN").val();
		var gfgsrqend = $("#GFGSRQEND").val();
		var type = $("#TYPE").val();
		var zwmc = $("#ZWMC").val();//住所地省市
		var sszjj = $("#EXTEND4").val();//所属证监局
		var zczbbegin = $("#ZCZBBEGIN").val();
		var zczbend = $("#ZCZBEND").val();
		var zrfs = $("#ZRFS").val();
		var zcqx=$("#ZCQX").val();//所属部门或所属大区
		var sshy=$("#SSHY").val();//所属行业		
		var gpsjBEGIN = $("#GPSJBEGIN").val();
		var gpsjEND = $("#GPSJEND").val();
		var innovation = $("#INNOVATION").val();//创新层
		var classification = $("#CLASSIFICATION").val();//分类		
		var ygp = $("#YGP").val();		
		var status = $("#STATUS").val();
		var cxddbg = "";		
		var pagesize=$("#pageSize").val();
		var pagenumber=$("#pageNumber").val();
		var seachUrl = encodeURI("loadCustomer.action?customername="+customerName+"&zqdm="+zqdm
				+"&username="+username+"&gfgsrqbegin="+gfgsrqbegin+"&gfgsrqend="+gfgsrqend
				+"&type="+type+"&zwmc="+zwmc+"&extend4="+sszjj
				+"&zczbbegin="+zczbbegin+"&zczbend="+zczbend+"&zrfs="+zrfs
				+"&zcqx="+zcqx+"&sshy="+sshy+"&gpsjBEGIN="+gpsjBEGIN+"&gpsjEND="+gpsjEND
				+"&innovation="+innovation+"&classification="+classification+"&ygp="+ygp
				+"&status="+status+"&cxddbg="+cxddbg+"&orderbyzqdm="+ordzqdm+"&orderbygpsj="+0+"&pageSize="+pagesize+"&pageNumber="+pagenumber+"&orderbygpzt="+0);
        window.location.href = seachUrl;
	}
	function OrderGPZT(){
		//排序   1正序   2倒序   3还原
		$("#ordgpsj").val("0");
		$("#ordzqdm").val("0");
		var ordzqdm=$("#ordzqdm").val();
		if($("#ordgpzt").val()=="0"){
			$("#ordgpzt").val("1");
			document.getElementById("ogpzt").innerHTML = "挂牌状态↑";
		}else if($("#ordgpzt").val()=="1"){
			$("#ordgpzt").val("2");
			document.getElementById("ogpzt").innerHTML = "挂牌状态↓";
		}else{
			$("#ordgpzt").val("0");
			document.getElementById("ogpzt").innerHTML = "挂牌状态";
		}
		var ordgpzt=$("#ordgpzt").val();
		var customerName = $("#CUSTOMERNAME").val();//客户名称
		var zqdm = $("#ZQDM").val();//股票代码
		var username = $("#USERNAME").val();//信披人
		var gfgsrqbegin = $("#GFGSRQBEGIN").val();
		var gfgsrqend = $("#GFGSRQEND").val();
		var type = $("#TYPE").val();
		var zwmc = $("#ZWMC").val();//住所地省市
		var sszjj = $("#EXTEND4").val();//所属证监局
		var zczbbegin = $("#ZCZBBEGIN").val();
		var zczbend = $("#ZCZBEND").val();
		var zrfs = $("#ZRFS").val();
		var zcqx=$("#ZCQX").val();//所属部门或所属大区
		var sshy=$("#SSHY").val();//所属行业		
		var gpsjBEGIN = $("#GPSJBEGIN").val();
		var gpsjEND = $("#GPSJEND").val();
		var innovation = $("#INNOVATION").val();//创新层
		var classification = $("#CLASSIFICATION").val();//分类		
		var ygp = $("#YGP").val();		
		var status = $("#STATUS").val();
		var cxddbg = "";		
		var pagesize=$("#pageSize").val();
		var pagenumber=$("#pageNumber").val();
		var seachUrl = encodeURI("loadCustomer.action?customername="+customerName+"&zqdm="+zqdm
				+"&username="+username+"&gfgsrqbegin="+gfgsrqbegin+"&gfgsrqend="+gfgsrqend
				+"&type="+type+"&zwmc="+zwmc+"&extend4="+sszjj
				+"&zczbbegin="+zczbbegin+"&zczbend="+zczbend+"&zrfs="+zrfs
				+"&zcqx="+zcqx+"&sshy="+sshy+"&gpsjBEGIN="+gpsjBEGIN+"&gpsjEND="+gpsjEND
				+"&innovation="+innovation+"&classification="+classification+"&ygp="+ygp
				+"&status="+status+"&cxddbg="+cxddbg+"&orderbyzqdm="+ordzqdm+"&orderbygpsj="+0+"&pageSize="+pagesize+"&pageNumber="+pagenumber+"&orderbygpzt="+ordgpzt);
        window.location.href = seachUrl;
	}
	//导出
	function expkh(){
        var customerName = $("#CUSTOMERNAME").val();//客户名称
        var zqdm = $("#ZQDM").val();//股票代码
        var username = $("#USERNAME").val();//信披人
        var gfgsrqbegin = $("#GFGSRQBEGIN").val();
        var gfgsrqend = $("#GFGSRQEND").val();
        var type = $("#TYPE").val();
        var zwmc = $("#ZWMC").val();//住所地省市
        var sszjj = $("#EXTEND4").val();//所属证监局
        var zczbbegin = $("#ZCZBBEGIN").val();
        var zczbend = $("#ZCZBEND").val();
        var zrfs = $("#ZRFS").val();
        var zcqx=$("#ZCQX").val();//所属部门或所属大区
        var sshy=$("#SSHY").val();//所属行业
        var gpsjBEGIN = $("#GPSJBEGIN").val();
        var gpsjEND = $("#GPSJEND").val();
        var innovation = $("#INNOVATION").val();//创新层
        var classification = $("#CLASSIFICATION").val();//分类
        var ygp = $("#YGP").val();
        var status = $("#STATUS").val();
        var cxddbg = "";
        var pagesize=$("#pageSize").val();
        var pagenumber=$("#pageNumber").val();

        var orderbyzqdm = $("#orderbyzqdm").val();
        var orderbygpsj = $("#orderbygpsj").val();
        var seachUrl = encodeURI("zqb_khmc_know.action?customername="+customerName+"&zqdm="+zqdm
            +"&username="+username+"&gfgsrqbegin="+gfgsrqbegin+"&gfgsrqend="+gfgsrqend
            +"&type="+type+"&zwmc="+zwmc+"&extend4="+sszjj
            +"&zczbbegin="+zczbbegin+"&zczbend="+zczbend+"&zrfs="+zrfs
            +"&zcqx="+zcqx+"&sshy="+sshy+"&gpsjBEGIN="+gpsjBEGIN+"&gpsjEND="+gpsjEND
            +"&innovation="+innovation+"&classification="+classification+"&ygp="+ygp
            +"&status="+status+"&cxddbg="+cxddbg+"&orderbyzqdm="+orderbyzqdm+"&orderbygpsj="+orderbygpsj);
        window.location.href = seachUrl;


	//	var pageUrl = encodeURI("zqb_khmc_know.action");
	//	window.location.href = pageUrl;
	}
	$(function(){
		if($("#ordzqdm").val()=="1"){
			document.getElementById("ozqdm").innerHTML = "证券代码(股票代码)↑";
		}else if($("#ordzqdm").val()=="2"){
			document.getElementById("ozqdm").innerHTML = "证券代码(股票代码)↓";
		}else if($("#ordgpsj").val()=="1"){
			document.getElementById("ogpsj").innerHTML = "挂牌时间↑";
		}else if($("#ordgpsj").val()=="2"){
			document.getElementById("ogpsj").innerHTML = "挂牌时间↓";
		}else if($("#ordgpzt").val()=="1"){
			document.getElementById("ogpzt").innerHTML = "挂牌状态↑";
		}else if($("#ordgpzt").val()=="2"){
			document.getElementById("ogpzt").innerHTML = "挂牌状态↓";
		}
	});
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
	text-align: left;
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
		<s:if test="orgroleid != 3">
			<a href="javascript:addItem();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
			<s:if test="orgroleid==5 || orgroleid == 4">
			<a href="javascript:expkh();" class="easyui-linkbutton" plain="true" iconCls="icon-edit">导出</a>
			</s:if>
			<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
			<a href="javascript:window.location.reload();" class="easyui-linkbutton" plain="true" iconCls="icon-reload">刷新</a>
		</s:if>
		</div>
	</div>
	<div region="center" style="padding-left:0px;padding-right:0px;border:0px;background-position:top">
		<s:if test="orgroleid != 3">
		<form name='ifrmMain' id='ifrmMain' method="post">
			<div style="padding:5px;text-align:center;">
				<div style="padding:0px;border:1px solid #ccc;margin-bottom:5px;background:#FFFFEE; width:99%">
					<table width='100%' border='0' cellpadding='0' cellspacing='0'>
						<tr>
							<td style='padding-top:10px;padding-bottom:10px;'>
								<table width='100%' border='0' cellpadding='0' cellspacing='0'>
									<tr>
										<td class="searchtitle">客户名称</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:128,required:false,string:true}' style="width:100px"
											name='CUSTOMERNAME' id='CUSTOMERNAME' value=''></td>
										<td class="searchtitle">股票代码</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:64,required:false,string:true}' style="width:100px"
											name='ZQDM' id='ZQDM' value=''></td>
										<td class="searchtitle">信披人</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:64,required:false,string:true}' style="width:100px"
											name='USERNAME' id='USERNAME' value=''></td>										
										<td class="searchtitle">股份公司成立日期</td>
										<td class="searchdata">
										<input type='text' onfocus="var GFGSRQEND=$dp.$('GFGSRQEND');WdatePicker({onpicked:function(){GFGSRQEND.focus();},maxDate:'#F{$dp.$D(\'GFGSRQEND\')}'})" class='{maxlength:64,required:false}' style="width:80px;" name='GFGSRQBEGIN' id='GFGSRQBEGIN' value=''>
										到
										<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'GFGSRQBEGIN\')}'})" class='{maxlength:64,required:false}' style="width:80px;" name='GFGSRQEND' id='GFGSRQEND' value=''></td>
									</tr>	
									<tr>
										<td class="searchtitle">注册类型</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:64,required:false,string:true}' style="width:100px"
											name='TYPE' id='TYPE' value=''></td>
										<td class="searchtitle">住所地省市</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:64,required:false,string:true}' style="width:100px"
											name='ZWMC' id='ZWMC' value=''></td>
										<td class="searchtitle">所属证监局</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:64,required:false,string:true}' style="width:100px"
											name='EXTEND4' id='EXTEND4' value=''></td>
										<td class="searchtitle">注册资本</td>
										<td class="searchdata">
											<input type='text' class='{maxlength:8,required:false,number:true}' style="width:80px;" name='ZCZBBEGIN' id='ZCZBBEGIN' value=''>
											到
											<input type='text' class='{maxlength:8,required:false,number:true}' style="width:80px;" name='ZCZBEND' id='ZCZBEND' value=''>万元</td>
									</tr>
									<tr>
										<td class="searchtitle">转让方式</td>
										<td class="searchdata">
										<select name='ZRFS' id='ZRFS'>
												<option value=''>空</option>
												<option value='协议'>协议</option>
												<option value='做市'>做市</option>
												<option value='挂牌企业'>挂牌企业</option>
										</select>
										</td>
										<td class="searchtitle">所属部门</td>
										<td class="searchdata">
										<input type='text' class='{maxlength:64,required:false}' style="width:100px" name='ZCQX' id='ZCQX' value=''></td>										
										<td class="searchtitle">所属行业</td>
										<td class="searchdata"><input type='text'
											class='{maxlength:64,required:false}' style="width:100px"
											name='SSHY' id='SSHY' value=''></td>
										<td class="searchtitle">挂牌日期</td>
										<td class="searchdata">
										<input type='text' onfocus="var GPSJEND=$dp.$('GPSJEND');WdatePicker({onpicked:function(){GPSJEND.focus();},maxDate:'#F{$dp.$D(\'GPSJEND\')}'})" class='{maxlength:64,required:false}' style="width:80px;" name='GPSJBEGIN' id='GPSJBEGIN' value=''>
										到
										<input type='text' onfocus="WdatePicker({minDate:'#F{$dp.$D(\'GPSJBEGIN\')}'})" class='{maxlength:64,required:false}' style="width:80px;" name='GPSJEND' id='GPSJEND' value=''></td></td>
									</tr>
									<tr>
										<td class="searchtitle">创新层</td>
										<td class="searchdata">
										<select name='INNOVATION' id='INNOVATION'>
												<option value=''>空</option>
												<option value='是'>是</option>
												<option value='否'>否</option>
										</select>
										</td>
																		
										<td class="searchtitle">挂牌状态</td>
										<td class="searchdata"><select name='YGP' id='YGP'>
												<option value=''>空</option>
												<option value='已挂牌'>已挂牌</option>
												<option value='未挂牌'>未挂牌</option>
												<option value='摘牌'>摘牌</option>
												<option value='转出'>转出</option>
										</select>
										</td>
										<td class="searchtitle">状态</td>
										<td class="searchdata">
											<select name='STATUS' id='STATUS'>
												<option value=''>空</option>
												<option value='有效'>有效</option>
												<option value='无效'>无效</option>
											</select>
										</td>
										<td class="searchtitle">公司评级</td>
										<td class="searchdata">
										<select name='CLASSIFICATION' id='CLASSIFICATION'>
												<option value=''>空</option>
												<option value='高风险'>高风险</option>
												<option value='次高风险'>次高风险</option>
												<option value='关注'>关注</option>
												<option value='正常'>正常</option>
										</select></td>	
									</tr>
								</table>
							<td>
							<td valign='bottom' style='padding-bottom:5px;'><a
								id="search" class="easyui-linkbutton" icon="icon-search"
								href="javascript:void(0);">查询</a></td>
						<tr>
					</table>
				</div>
			</div>
			<span style="disabled:none">
				 <input type="hidden" name="formid" value="88" id="formid" />
				 <input type="hidden" name="demId" value="21" id="demId" /> 
				 <input type="hidden" name="idlist" id="idlist" value='11'>
				
				 <input type="hidden" name="ordzqdm" id="ordzqdm" value="<s:property value="orderbyzqdm" />" />
				 <input type="hidden" name="ordgpsj" id="ordgpsj" value="<s:property value="orderbygpsj" />"  /> 
				 <input type="hidden" name="ordgpzt" id="ordgpzt" value="<s:property value="orderbygpzt" />"  /> 
			</span>
			 
		</form>
		</s:if>
		<div style="padding:5px;text-align:center;">


			<table id='iform_grid' width="99%" style="border:1px solid #efefef">
				<tr class="header">
					<td style="width:5%;" class="header"><input type="checkbox"
						name="colname" id="chkAll" /></td>
					<td style="width:5%;">序号</td>
					<td style="width:15%;">客户名称</td>
					<td style="width:5%;">证券简称(公司简称)</td>
					<td id="ozqdm" style="width:5%;<s:if test="orgroleid != 3">cursor:pointer;" onclick="OrderZQDM();</s:if>">证券代码(股票代码)</td>
					<td style="width:10%;">信披人</td>
					<td style="width:10%;">信披人电话</td>
					<td style="width:10%;">所属部门</td>
					<td style="width:10%;">所属证监局</td>
					<td style="width:10%;">客户状态</td>
					<td id="ogpsj" style="width:10%;<s:if test="orgroleid != 3">cursor:pointer;" onclick="OrderGPSJ();</s:if>">挂牌时间</td>
					<td id="ogpzt" style="width:10%;<s:if test="orgroleid != 3">cursor:pointer;" onclick="OrderGPZT();</s:if>">挂牌状态</td>
					<s:if test="showProject!=false&&orgroleid != 3">
						<td style="width:20%;">操作</td>
					</s:if>
				</tr>
				<s:iterator value="customerList" status="status">
					<tr class="cell">
						<td style="width:05%;"><input type="checkbox"
							id="<s:property value='INSTANCEID'/>" name="colname"
							value="<s:property value='INSTANCEID'/>" /></td>
						<td><s:property value="#status.count" /></td>
						<td><a href="javascript:void(0)"
							onclick="edit(<s:property value='INSTANCEID'/>)"><s:property
									value="CUSTOMERNAME" /></a></td>
						<td><s:property value="ZQJC" /></td>
						<td><s:property value="ZQDM" /></td>
						<td><s:property value="USERNAME" /></td>
						<td><s:property value="TEL" /></td>
						<td><s:property value="ZCQX" /></td>
						<td><s:property value="EXTEND4" /></td>
						<td><s:property value="STATUS" /></td>
						<td><s:property value="GPSJ" /></td>
						<td><s:property value="YGP" /></td>
						<s:if test="showProject!=false&&orgroleid != 3">
								<td>
									<%--<s:if test="VIEW!=true">
									<a href="javascript:addProject('<s:property value="CUSTOMERNAME"/>','<s:property value="CUSTOMERNO"/>','<s:property value="USERNAME"/>','<s:property value="TEL"/>')"
									style="color:blue;"><u>添加项目及成员</u></a>
									</s:if>--%>
								</td>
						</s:if>
					</tr>
				</s:iterator>
			</table>

			<form action="loadCustomer.action" method=post name=frmMain
				id=frmMain>
				<s:hidden name="pageNumber" id="pageNumber"></s:hidden>
				<s:hidden name="pageSize" id="pageSize"></s:hidden>
				<s:hidden name="customerno" id="customerno"></s:hidden>
				<s:hidden name="zqdm" id="zqdm"></s:hidden>
				<s:hidden name="zqjc" id="zqjc"></s:hidden>
				<s:hidden name="zrfs" id="zrfs"></s:hidden>
				<s:hidden name="cxddbg" id="cxddbg"></s:hidden>
				<s:hidden name="customername" id="customername"></s:hidden>
				<s:hidden name="ygp" id="ygp"></s:hidden>
				<s:hidden name="status" id="status"></s:hidden>
				<s:hidden name="type" id="type"></s:hidden>
				<s:hidden name="zwmc" id="zwmc"></s:hidden>		
				<s:hidden name="username" id="username"></s:hidden>
				<s:hidden name="zczbbegin" id="zczbbegin"></s:hidden>
				<s:hidden name="zczbend" id="zczbend"></s:hidden>
				<s:hidden name="gfgsrqbegin" id="gfgsrqbegin"></s:hidden>
				<s:hidden name="gfgsrqend" id="gfgsrqend"></s:hidden>
				<s:hidden name="xmsplcServer" id="xmsplcServer"></s:hidden>
				<s:hidden name="orderbyzqdm" id="orderbyzqdm"></s:hidden>
				<s:hidden name="orderbygpsj" id="orderbygpsj"></s:hidden>				
				<s:hidden name="gpsjBEGIN" id="gpsjBEGIN"></s:hidden>				
				<s:hidden name="gpsjEND" id="gpsjEND"></s:hidden>
				<s:hidden name="extend4" id="extend4"></s:hidden>
				<s:hidden name="innovation" id="innovation"></s:hidden>
				<s:hidden name="zcqx" id="zcqx"></s:hidden>
				<s:hidden name="sshy" id="sshy"></s:hidden>
				<s:hidden name="classification" id="classification"></s:hidden>
			</form>
			<div id='prowed_ifrom_grid'></div>
		</div>
	</div>
	<div region="south"
		style="vertical-align:bottom;height:60px;border-top:1px solid #efefef;color:#0000FF;font-size:12px;padding-top:10px;padding-left:10px;"
		border="false">
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
		$("#ZRFS").val($("#zrfs").val());
		$("#ZQJC").val($("#zqjc").val());
		$("#STATUS").attr("value",$("#status").val());
		/* $("#CXDDBG").val($("#cxddbg").val()); */
		$("#TYPE").attr("value",$("#type").val()); 
		$("#ZWMC").attr("value",$("#zwmc").val()); 
		$("#USERNAME").attr("value",$("#username").val());		 
		$("#ZCZBBEGIN").attr("value",$("#zczbbegin").val()); 
		$("#ZCZBEND").attr("value",$("#zczbend").val());
		$("#GFGSRQBEGIN").attr("value",$("#gfgsrqbegin").val());
		$("#GFGSRQEND").attr("value",$("#gfgsrqend").val());
		$("#YGP").attr("value",$("#ygp").val());
		$("#GPSJBEGIN").attr("value",$("#gpsjBEGIN").val());
		$("#GPSJEND").attr("value",$("#gpsjEND").val());
		$("#EXTEND4").attr("value",$("#extend4").val());
		$("#INNOVATION").attr("value",$("#innovation").val());
		$("#ZCQX").attr("value",$("#zcqx").val());
		$("#SSHY").attr("value",$("#sshy").val());
		$("#CLASSIFICATION").attr("value",$("#classification").val());
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
						break;
					}
                }
                if(!flag){
                	return "success";
                }
            }
        }, "包含非法字符!");
</script>