<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>操作提示</title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/ui/base/jquery.ui.autocomplete.css">
<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
<link href="iwork_css/button.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js" charset="utf-8"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery-ui-1.8.16.custom.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js" ></script> 
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/engine/process_page.js"></script>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
	$.ajaxSetup({
        async: false
    });
	mainFormValidator =  $("#ifromMain").validate({});
	mainFormValidator.resetForm();
});
		var api = art.dialog.open.api, W = api.opener;		
        $(document).ready(function(){
        	 var actDefId = $("#actDefId").val();
			 var prcDefId = $("#prcDefId").val();
			 var actStepDefId = $("#actStepDefId").val();
			 var taskId = $("#taskId").val();
			 var backType = $("#backType").val();
			 var instanceId = $("#instanceId").val();
			 if(actDefId.indexOf("TXXMGLLC")==0){
			 $("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
			 var xmmc =parent.document.getElementById("XMMC").value;
			 	$.ajax({
						type: "POST",
						url: "sx_zqb_hqthxmjbr.action",//获取投行项目经办人信息
						data: {xmmc:xmmc},
						success: function(data){
						
							if(data!=""){
								
								$("input[type='checkbox'][name='receiveUser'][value="+data+"]").attr("checked",true);
							}
						}
				});
			 }
			
			if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0||actDefId.indexOf("RCYWCB")==0){
				var qcrid =parent.document.getElementById("QCRID").value;
				var qcrxm =parent.document.getElementById("CREATENAME").value;
				var tjwj = qcrid+"["+qcrxm+"]";
				$("#receiveUser").val(tjwj);
				$("#KFBS").hide();//董秘、督导提交公告不显示扣分项
				$.ajax({
					type: "POST",
					url: "sx_zqb_KFLX.action",//董秘、督导只显示扣分类型
					data: {},
					success: function(data){
						$("#checkKFLX").html(data);
					}
				});
			}else{
				if(actDefId.indexOf("RCYWCB")==0){
					var qcrid =parent.document.getElementById("QCRID").value;
					var qcrxm =parent.document.getElementById("CREATENAME").value;
					var tjwj = qcrid+"["+qcrxm+"]";
					$("#receiveUser").val(tjwj);
				}
				document.getElementById("KFLX").style.display="none";
			}
			$.ajax({
				type: "POST",
				url: "sx_zqb_isShowKF.action",//董秘、督导提交公告提示的几个阶段不显示扣分信息
				data: {actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,backType:backType},
				success: function(data){
					if(data=="false"){
						$("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
					}else{
						sxkf(actDefId,prcDefId,actStepDefId,taskId,backType,instanceId);
					}
				}
		 	});
        	showSysTips();
           //*
           if(document.addEventListener){//如果是Firefox
				document.addEventListener("keypress",execHandler, true);
		   }else{
		   		document.attachEvent("onkeypress",execHandler);
		   }//*/
		   //录入提示
		   	function split( val ) {
				return val.split( /,\s*/ );
			}
			function extractLast( term ) {
				return split( term ).pop();
			}
		   $("#ccUsers").bind( "keydown", function( event ) {
				if ( event.keyCode === $.ui.keyCode.TAB &&
						$( this ).data( "autocomplete" ).menu.active ) {
					event.preventDefault();
				}
			}).autocomplete({
				source: function( request, response ) {
					$.getJSON( "user_load_autocomplete_json.action", {
						term: extractLast( request.term )
					}, response );
				},
				search: function() {
					var term = extractLast( this.value );
					if ( term.length < 2 ) {
						return false;
					}
				},
				focus: function() {
					return false;
				},
				select: function( event, ui ) {
					var terms = split( this.value );
					terms.pop();
					terms.push( ui.item.value );
					terms.push( "" );
					this.value = terms.join( ", " );
					return false;
				}
			});
		   if(actDefId.indexOf("TXXMGLLC")==0){
		   	   var xmmc = parent.document.getElementById("XMMC").value;
			   $.ajax({
					type: "POST",
					url: "isFirstnode.action",//投行项目流程判断是否为第一节点 
					data: {actStepDefId:actStepDefId,xmmc:xmmc},
					success: function(data){
						if(data!="false"){
							 $("#ccUsers").val(data);
						}
					}
			 	});
		   }
		   $.ajax({
				type: "POST",
				url: "zqb_process_priority.action",//设置当前任务优先级
				data: {taskId:taskId},
				success: function(data){
					if(data!=null&&data!=""){
						if($("#Priority0").val()==data)
							$("#Priority0").attr("checked",true);
						if($("#Priority1").val()==data)
							$("#Priority1").attr("checked",true);
					}else{
						if($("#Priority0").val()==data)
							$("#Priority0").attr("checked",true);
					}
				}
		 	});
		 	 //如果是公告流程或日常业务呈报流程 那么判断common配置文件的         ggkfr 0：只显示董秘：1：显示各个节点办理人
			 if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0||actDefId.indexOf("RCYWCB")==0){
	        	 $.ajax({
						type: "POST",
						url: "sx_zqb_XSBKFR.action",//获取被扣分人列表,操作dom使其显示在提交页面
						data: {actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,backType:backType,instanceId:instanceId},
						success: function(data){
							$("#checkBKFR").html(data);
						}
				});
			}else{
				$("#KFBS").hide();$("#KFLX").hide();$("#BKFR").hide();
			}
       });
        var flag =0;
        function sxkf(actDefId,prcDefId,actStepDefId,taskId,backType,instanceId){
        	
     	   $.ajax({
 				type: "POST",
 				url: "sxkf_isShowKF.action",//董秘、督导提交公告提示的几个阶段不显示扣分信息
 				data: {actDefId:actDefId},
 				success: function(data){
 					if(data=="jffs"){
 						flag =1;
 						 $.ajax({
 							type: "POST",
 							url: "sx_zqb_XSBKFR.action",//获取被扣分人列表,操作dom使其显示在提交页面
 							data: {actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,backType:backType,instanceId:instanceId},
 							success: function(data){
 								$("#checkBKFR").html(data);
 							}
 						});
 						if($("#checkBKFR").html()!="" && $("#checkBKFR").html()!=null && typeof($("#checkBKFR").html())!="undefined"){
 							$("#KFBS").show();$("#KFLX").hide();$("#BKFR").show();
 						}
 						
 					}else if(data=="jflx"){
 						flag =1;
 						 $.ajax({
 							type: "POST",
 							url: "sx_zqb_XSBKFR.action",//获取被扣分人列表,操作dom使其显示在提交页面
 							data: {actDefId:actDefId,prcDefId:prcDefId,actStepDefId:actStepDefId,taskId:taskId,backType:backType,instanceId:instanceId},
 							success: function(data){
 								$("#checkBKFR").html(data);
 							}
 						});
 						if($("#checkBKFR").html()!="" && $("#checkBKFR").html()!=null && typeof($("#checkBKFR").html())!="undefined"){
 							$("#KFBS").hide();$("#KFLX").show();$("#BKFR").show();
 						}
 					}
 				}
 		 	});
        }
       function execHandler(evt){
		  if(evt.keyCode==13){
		//	exexuteSendAction();
			return false;
		  }
	    }
       function checkedTest(){
    	    var count = 0;
    	    var checkArry = document.getElementsByName("Fruit");
    	            for (var i = 0; i < checkArry.length; i++) { 
    	                if(checkArry[i].checked == true){
    	                    //选中的操作
    	                    count++; 
    	                }
    	            }
    	    return count;
    	}
       function checkradio(){
    	   var count=0;
    	   var val=$('input:radio[name="KFBS"]:checked').val();
           if(val!=null){
        	   count=1;
           }
           return count;
    	} 
		//执行发送动作
       function exexuteSendAction(){
			
    	   var valid = mainFormValidator.form(); //执行校验操作
    	   var serverflg = "<s:property value="serverflg" />";
    		if(!valid){
    		return;
    		}
       		//判断接收地址是否为空
       		$("#sendbtn").attr("disabled",true);
       		var steplist = new Array();
       		var checklist = new Array();
       		var isValidate = true;
       		var bs1=checkedTest();
       		var bs2=checkradio();
       		if(bs1!=0){
       			var slt=document.getElementById("checkbkfr");
       			if(slt.value==""){
       				alert("请选择被扣分人！");
       				$("#sendbtn").attr("disabled",false);
       				return;
       			}
       		}else if(bs2!=0){
       			var slt=document.getElementById("checkbkfr");
       			if(slt.value==""){
       				alert("请选择被扣分人！");
       				$("#sendbtn").attr("disabled",false);
       				return;
       			}
       		}
       		
   			var opinion=document.getElementById("opinion");
   			if(opinion!=null){
	      		if(opinion.value==""){
	      			alert("请填写办理意见！");
	      			$("#sendbtn").attr("disabled",false);
	      			return;
	      		}
   			}
       		var num = 0;
       		 $(".tempInput").each(function(item,obj){
       		 	var num_id = obj.id
       		 	num = num_id.replace("tmp_","");
       		 	 var tmp = $('#targetGatewayId'+num).val()+'|'+$(this).val();
       		 	$("#receiveUser-"+num).val(tmp);
       		 });
       		  
       		//判断每一个接收人是否已经填写完成
       		 $("input[name='receiveUser']").each(function(item,obj){
				    var id = $(obj).attr("id");
				    var type = $(obj).attr("type");
				    //alert(type);
				    if(type=='text'||type=='hidden'){
				    	var inputVal =  $(this).val();
				    	//alert(inputVal);
				    	if(inputVal==''){
				    		if(type=='text'){
				    			$("#"+id).focus();
				    		}else{
				    			$("#tmp_"+item).focus();
				    		}
				    		alert('节点办理人未填写完整!');
				    		isValidate = false;
				    		 return false;
				    	}else{
				    		var str = inputVal.split("|");
					    	if(str.length>1){
								if($.inArray(str[0],steplist)<0){
						    		steplist.push(str[0]);
						    	}
						    	checklist.push(str[0]);
					    	}
				    	}
				    }else if(type=='checkbox'){
				    	var value = $("#"+id).attr("value");
				    	var str = value.split("|");
				    	if(str.length>1){
							if($.inArray(str[0],steplist)<0){
					    		steplist.push(str[0]);
					    	}
					    	if($("#"+id).attr("checked")){
					    		checklist.push(str[0]);
					    	}				    	
				    	}
				    }
			  });
			  //alert(steplist.length);
			 if(steplist.length>0){
			 	for(var k=0;k<steplist.length;k++){
			 		if($.inArray(steplist[k],checklist)<0){
			 			$("#sendbtn").attr("disabled",false);
			 			alert('节点办理人未填写完整!');
			 			isValidate = false;
			 			return ;
			 		}
			 	}
			 	$("#sendbtn").attr("disabled",false);
			 }
			 if(!isValidate){
			 	$("#sendbtn").attr("disabled",false);
			 	return false;
			 }
       		var list = document.ifromMain.receiveUser;
       		if(typeof(list)=='undefined'){
       			alert('未找到接收人,请联系系统管理员');
       		}
       		var num = 0;
			for (i=0;i<list.length;i++){ 
				if(list[i].value==''){
					continue;
				}
				if($(list[i]).attr("type")=='checkbox'){
					if($(list[i]).attr("checked")){
					num++;
					}
				}else{
					num++;
				}
				
			}
			if(num==0&&list==''){
				alert('未找到接收人,请选择办理人!');
				$("#sendbtn").attr("disabled",false);
				return;
			}
			
			var maxUser = $("#maxUser").val();
			if(maxUser!=-1){
				if(maxUser<num){
					alert('超出办理人数限制，当前节点额定办理人数为'+maxUser+'人!');
					$("#sendbtn").attr("disabled",false);
					return;
				}
			}
			 var obj = $('#ifromMain').serialize();
			     $.post("processRuntimeExecuteHandle.action",obj,function(data){
			            var arr=data;
			             try{
			            	showSysTips();
			            }catch(e){}
						if(arr!=null){
							if(arr=="success"){
								//发送成功判断是否执行扣分
								 var actDef = $("#actDefId").val();
								 if(actDef.indexOf("GGSPLC")==0||actDef.indexOf("CXDDYFQLC")==0||actDef.indexOf("RCYWCB")==0 || flag==1){
									 //查看被扣分人是否是空
									 var value = $("select").find("option:selected").val();
									 if(typeof(value)!= undefined  && value!=null){
									 var list = value.split("|");
									 if(list!=null&&list!=""){
						          		 var bkfr=list[1];
						          		 var id = $("#dataid").val();
										 var instanceId = $("#instanceId").val();
										 var actDefId = $("#actDefId").val();
										 var kfbs = $("input[name='KFBS']:checked").val();
						          		 if(!bkfr==null||!bkfr==""){//若选择被扣分人则添加扣分信息
											var s=''; 
											var qttext ='';
											if(actDefId.indexOf("GGSPLC")==0||actDefId.indexOf("CXDDYFQLC")==0 ||actDefId.indexOf("RCYWCB")==0){//董秘、督导提交公告需要添加扣分类型
												var obj=document.getElementsByName('Fruit'); //选择所有name="'test'"的对象，返回数组 
												//取到对象数组后，我们来循环检测它是不是被选中
												for(var i=0; i<obj.length; i++){ 
													if(obj[i].checked) 
													s+=obj[i].value+','; //如果选中，将value添加到变量s中 
												}
												s = s.substring(0, s.length-1);
												var qttext =  $("#text").val();
											}
											var seachUrl = encodeURI("zqb_sx_tjscoreset.action?id="+id+"&countdownscore="+kfbs+"&instanceid="+instanceId+"&actDefId="+actDefId+"&type="+s+"&memo="+qttext+"&bkfr="+bkfr);
											$.post(seachUrl,function(result){});
						          		 }
						          	 }}
						          }
					          	 art.dialog.tips("发送成功",1); 
					          	addDailyLog();
								//刷新办理列表 
								try{
									window.parent.opener.reloadWorkList();
								}catch(err){
									setTimeout('window.parent.closeWin();',1000);
									setTimeout('api.close();',1000);
								}
								setTimeout('window.parent.closeWin();',1000);
								setTimeout('api.close();',1000);
							}else if(arr=="ERROR-10014"){ 
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常,任务接收人中包含了非法账号<br/>(错误编号:ERROR-00014)",3);
							}else if(arr=="ERROR-10015"){
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常,任务接收人地址异常<br/>(错误编号:ERROR-00015)",3); 
							}else if(arr=="ERROR-10005"){ 
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常,系统参数异常<br/>(错误编号:ERROR-00005)",3);
							}else if(arr=="ERROR-10017"){  
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常,接收人地址未填写完整，请检查<br/>(错误编号:ERROR-00017)",3);
							}else{
								$("#sendbtn").attr("disabled",false);
								art.dialog.tips("发送异常(错误编号:ERROR-00018)",3);
							}
						}else{
							$("#sendbtn").attr("disabled",false);
							art.dialog.tips("发送失败,返回值异常(错误编号:ERROR-10016)",3);
						}
						
			     });
			     return;
       }
		function addDailyLog(){
			var title = $("#title").val();
			var instanceId = $("#instanceId").val();
			var action = $("#action").val();
			var actStepDefId = $("#actStepDefId").val();
			var url = encodeURI("dg_zqb_process_addlog.action?title="+title+"&instanceId="+instanceId+"&action="+action+"&actStepDefId="+actStepDefId);
			$.post(url,function(data){
				
			});
		}
       function closeParentWin(){
			try{
				window.parent.opener = null;
				window.parent.close();
			}catch(err){
			}
		}
		
		//设置参数
		function setParams(str,val){
			document.getElementById(str).value = val;
			try{
				document.getElementById(str).onchange();
			}catch(e){}
			return true;
			//alert(document.getElementById("receiveUser").value);
		}
        function showAddress(fieldname){
				var pageUrl = "processRuntimeRadioAddress_show.action?inputField="+fieldname;
					art.dialog.open(pageUrl,{
						id:'addressDialog', 
						title:"地址簿",
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width: 500,
						height: 510,
						close:function(){
							$("#sendbtn").focus();
						}
					 });
       }
       
       
        function dept_multi_book(defaultField,type,param){
				var pageUrl = "address_diy_index.action?defaultField="+defaultField+"&nodeType="+type+"&params="+param;
				art.dialog.open(pageUrl,{
						id:'addressDialog', 
						title:"地址簿",
						lock:true,
						background: '#999', // 背景色
					    opacity: 0.87,	// 透明度
					    width: 500,
						height: 510,
						close:function(){
							$("#sendbtn").focus();
						}
					 });
       }
</script>
<style type="text/css">
body{
		 	margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
		 }
		.ui-menu-item{
			font-size:10px;
			color:#0000FF;
			background-color:#FFF;
			border-left:1px solid #f2f2f2;
			border-right:1px solid #f2f2f2;
			border-bottom:1px solid #f2f2f2;
			text-decoration:none;
			display:block;
			padding:.2em .4em;
			line-height:1.5;
			zoom:1;
			float: left;
			clear: left;
			width: 100%;
		}
		.ui-state-hover{
			font-size:10px;
			color:#0000FF;
			background-color:#EEEEEE;
			text-decoration:none;
			display:block;
			padding:.2em .4em;
			line-height:1.5;
			zoom:1;
			float: left;
			clear: left;
			width: 100%;
		}
	
	.ui-menu {
		list-style:none;
		padding: 2px;
		margin: 0;
		display:block;
		float: left;
	}
	.nextStep{
		font-size:16px;
		color:#0000FF;
		text-align: left;
		padding-left: 3px;
		font-family:"黑体";
		border-bottom:1px #999999 dotted;
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-bottom:5px;
	} 
	.StepTitle{
		font-size:13px;
		color:#666;
		text-align: right;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		line-height:16px;
		font-weight:800;
		white-space:nowrap;
		background-color:#F5F8F7;
		 text-align:left;
		 border-bottom:1px solid #EFEFEF
	}
	.ItemTitle{
		font-size:12px;
		color:#0000FF;
		text-align: right;
		padding: 10px;
		font-family:"宋体";
		vertical-align:top;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:800;
		line-height:12px;
		 white-space:nowrap;
	}
	.pageInfo{
		font-size:12px;
		color:#0000FF;
		text-align: left;
		padding-left: 10px;
		font-family:"宋体";
		vertical-align:middle;
		word-wrap:break-word;
		word-break:break-all;
		font-weight:500;
		line-height:12px;
		padding-right:20px;
	}
	.button{
		margin-top:10px;
		border-top:1px solid #990000;
		text-align:right;
		padding-top:20px;
		padding-right:20px;
		height:60px;
		vertical-align:middle;
	}
	#div{ width:450px; padding-top:0px; }
	ul,li{ list-style:none; padding:0; }
	li{ width:80px; float:left; margin:-1px 0 0 -1px;padding:0 2px; border:0px solid #000;line-height:25px}
	.trans_tip{
		padding:5px;
		border:1px solid #999999;
		background-color:#FFFFCC;
		margin-left:auto;
		margin-right:auto;
		color:red;
		height:260px;
		font-size:12px;
	}
	.trans_tip_title{
		font-size:12px;
		color:#666;
	}
</style>
</head>

<body >
<s:form name="ifromMain" id="ifromMain" method="post"  theme="simple">
<!-- TOP区 -->
<table width="100%"  border="0">
			<tr>
				<td colspan="2">
				<table width="100%"  border="0">
				<s:iterator value="sendList" status="status">
					<tr>
					<td class="nextStep" colspan="2">
						<IMG alt="任务节点"  style="width:40px;height:40px;"  src="iwork_img/gear3.gif" border="0"/>任务流转至：【<IMG alt="任务节点" src="iwork_img/domain.gif" border="0"/><s:property value="targetStepName"  escapeHtml="false"/>】
						
					</td>
				</tr> 
			
						<s:property value="addressHTML" escapeHtml="false"/> 
					<!-- 办理参数 -->
				<s:hidden id ="targetStepId" name="targetStepId"/>
				<s:hidden id ="targetStepName" name="targetStepName"/>
					</s:iterator>
					<s:if test="trans_tip!=''">
					</s:if>
					<s:if test="ccinstal==1">
						<tr>
							<td  class="ItemTitle">抄送人	:</td>
							<td  class="pageInfo"><s:textfield name="ccUsers" id="ccUsers" cssStyle="width:300px;color:#0000FF;" theme="simple"/>&nbsp;<a href="javascript:addCCUsers();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-multibook">地址簿</a></td>
						</tr>
					</s:if>

					<s:if test="RemindTypeList!=null">
					<tr>
						<td class="ItemTitle">提醒方式:	</td><td  class="pageInfo">
							 <s:checkboxlist name="remindType" id="remindType" list="remindTypeList" 
					         labelposition="top"
					         listKey="key"
					         listValue="value" 
					         value="remindTypeList"
					         >
					        </s:checkboxlist>
						</td>
					</tr>
					</s:if>
					<tr> 
						<td class="ItemTitle">优先级:</td><td  class="pageInfo"><s:radio value="" list="#{'0':'普通','1':'紧急'}" id="Priority" name="Priority" theme="simple"/></td>
					</tr>
					<s:if test="isOpinion==1">
						<tr> 
							<td  class="ItemTitle">办理意见:</td><td  class="pageInfo">
							<table width="100%">
								<tr>
									<td width="5%">
										<s:textarea name="opinion" id="opinion" class='{string:true,maxlength:2000}' cssStyle="width:300px;height:80px;border:1px solid #ccc;overflow:auto;background-color:#FEFFEC;font-size:16px;color:red"></s:textarea>
									</td>
									<td style="text-align:left;vertical-align:bottom">
										<div><a href="javascript:addAttach('attach','DIVATTACHMENT');" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-attach">附件上传</a>
										<input type=hidden size=100 id='attach'  class = '{maxlength:512}'  name='attach' value=''/>
										</div>
										<div><a href="javascript:addAuditOpinion();" style="padding-left:5px;" class="easyui-linkbutton" plain="true" iconCls="icon-process-addopinion">常用意见</a></div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td></td><td style="text-align:left;padding-left:10px;">
								<div id='DIVATTACHMENT'>
									<s:property value="opinionAttachHtml" escapeHtml="false"/>
								</div>
							</td>
						</tr>
					</s:if>
				<%-- 	<s:if test="backStepList!=null"> --%>
					<tr id="BKFR">
						<td class="ItemTitle">被减分人:</td><td class="pageInfo"><span id="checkBKFR"></td>
					</tr>
					<tr id="KFLX">
						<td class="ItemTitle">减分类型:</td><td class="pageInfo"><span id="checkKFLX"></td>
					</tr>
					<tr id="KFBS">
						<td  class="ItemTitle" >减分:</td><td  class="pageInfo">
						0.1<input type="radio" name="KFBS" value="0.1">
						0.3<input type="radio" name="KFBS" value="0.3">
						0.5<input type="radio" name="KFBS" value="0.5">
						</td>
					</tr>
				<%-- 	</s:if> --%>
				</table>
				</td>
				<s:if test="trans_tip!=''">
				<td style="vertical-align:top;padding:10px;">
							<fieldset  class="trans_tip">
								<legend class="trans_tip_title">办理提示</legend>
										<s:property value="trans_tip" escapeHtml="false"/>
							</fieldset>
				</td>
				</s:if>
			</tr>
			<tr style="display:none">
				<td> 
				
				<s:hidden id ="action" name="action"/>
				</td>
			</tr>
			
			<tr>
				<td style="padding-right:10px">
					<input id="sendbtn" type="button" onclick="exexuteSendAction();" value="发送"   class="button_ send"/>
					<input type="button"  onClick="_close()" value="取消"  class="button_ close"/>
				</td>
			</tr>
		
		</table>
						
					<!--表单参数-->
		<span style="display:none">
			<s:hidden id ="title" name="title"/>
			<s:hidden id ="maxUser" name="maxUser"/>
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/> 
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span>
		</s:form>
</body>
</html>
<script>
	$("#sendbtn").focus();
</script>
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