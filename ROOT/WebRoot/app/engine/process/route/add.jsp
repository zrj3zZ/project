<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>  
<title></title>
<link rel="stylesheet" type="text/css" href="iwork_css/common.css"> 
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<style type="text/css">
		.form_title{  
			font-family:黑体;
			font-size:14px;
			text-align:right;
			vertical-align:top;
			padding-top:5px;
		}
		.form_data{
			font-family:宋体;
			font-size:12px;
			text-align:left;
			color:#0000FF; 
			line-height:30px;
		}
		.table_grid{
			width:100%;
			border:1px solid #999; 
		}
		.table_grid tr{
			line-height:30px;
		}
		.table_grid th{
			background-color:#ccc;
			border:1px solid #efefef;
			color:#333; 
			text-align:left;
			padding-left:5px;
		}
		.table_grid td{
			border:1px solid #efefef;
			padding:2px;
			padding-left:5px;
			color:#0000FF; 
		}
		</style>	
		<script type="text/javascript">
		 var api = frameElement.api, W = api.opener; 
		 var mainFormValidator;
		 //添加验证脚本
		 mainFormValidator =  $("#editForm").validate({
				debug:true
		 });
		 //提交
		function doSubmit(){
			if($('#title').val()==''){
				alert("流程标题不能为空");
				$('#title').foucs();
				return false;
			}else if($('#prcKey').val()==''){
				alert("流程键值不能为空");
				$('#prcKey').foucs();
				return false;
			}
            var options = {
				error:errorFunc,
				success:successFunc 
			   };
			$('#editForm').ajaxSubmit(options);
      		}
		 errorFunc=function(){
          		alert("保存失败！");
    		 }
      	successFunc=function(responseText, statusText, xhr, $form){
           if(responseText!="error"&&responseText!=""){
              alert("保存成功！");
              //setTimeout('cancel();',1000);
              var title = $("#title").val();
              var prcKey = $("#prcKey").val(); 
              showDesiner(title,prcKey,responseText);
           }
           else if(responseText=="error"){
              alert("保存失败！");
           } 
      	}
      	//加载
		function loadProcessKey(){
			var title = $("#title").val();
			var pageurl = 'processDeploy_processkey_load.action?processtitle='+encodeURI(title);
			if(title!=''){
				$.ajax({  
			            type:'POST',
			            url:pageurl,
			            success:function(msg){ 
			               if(msg!=''){
			               		$("#prcKey").val(msg); 
			               }else{
			               		alert("提取失败");
			               }
			            }
			        });
			}else{
				alert("请填写流程标题"); 
			}
		}
		
		function showDesiner(title,prcKey,proId){
			var pageUrl = 'processDeploy_show_designer.action?processKey='+prcKey+'&id='+proId;
			art.dialog.open(pageUrl,{
				id:"showDesinerDg",
				title: '流程设计器'+title,
				cover:true,
				parent:api,
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				cache:true,
				lock: true,
				iconTitle:false,
				extendDrag:true,
				autoSize:true,
				resize:true,
				pading: 0,
				width: 350,
				height: 550
			});
			dg.max(); 
			dg.ShowDialog();
		}
	     //执行退出
		function cancel(){
			api.close();
		}
		
		</script>
	</head>
	<body class="easyui-layout">
	<div region="north" split="false" style="border:0px;">
	<div class="tools_nav" >
		<a id="btnEp" class="easyui-linkbutton" icon="icon-save" plain="true" href="###" onclick="javascript:doSubmit();">保存</a> 
			<a id="btnCancel" class="easyui-linkbutton" plain="true" icon="icon-add" href="javascript:cancel();">汇报链模型设置</a>
			<a id="btnCancel" class="easyui-linkbutton" plain="true" icon="icon-add" href="javascript:cancel();">通用岗位模型设置</a>
			<a id="btnCancel" class="easyui-linkbutton" plain="true" icon="icon-add" href="javascript:cancel();">组织岗位模型设置</a>
			<a id="btnCancel" class="easyui-linkbutton" plain="true" icon="icon-cancel" href="javascript:cancel();">取消</a>
	</div>	
	</div>
	<div region="center"  style="border:0px;height:40px;padding:10px;background:#gegege" > 
	<s:form action="processDeploy_process_save" id="editForm" name="editForm" theme="simple">
			<table width="100%" border = "0">
				<tr> 
					<td class="form_title"><span style="color:red;">*</span>汇报链名称:</td>
					<td  class="form_data"><s:textfield id="title" name="model.title" style="width:300px;" theme="simple"/></td>
				</tr>
				<tr> 
					<td class="form_title"><span style="color:red;">*</span>业务类型:</td>
					<td class="form_data"><s:textfield id = "prcKey" name="model.prcKey" style="width:300px;" theme="simple"/></td>
				</tr>
				<tr>
					<td class="form_title">适用业务流程:</td>
					<td class="form_data"><s:textarea name="model.memo" cssStyle="width:450px;height:60px;" theme="simple"></s:textarea></td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>权限范围:</td>
					<td class="form_data">点击设置权限范围</td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>适用人员范围:</td>
					<td class="form_data">点击设置权限范围</td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>汇报链层级模型:</td>
					<td class="form_data">
						<table class="table_grid">
							<tr>
								<th>审批层级</th>
								<th>人员可选范围</th>
								<th>组织层级</th>
							</tr>
							<tr>
								<td>CEO审批</td>
								<td>张宏江</td>
								<td>公司级审批</td>
							</tr>
							<tr>
								<td>副总裁审批</td>
								<td>张宏江</td>
								<td>公司级审批</td>
							</tr>
							<tr>
								<td>一级部门负责人</td>
								<td>张宏江</td>
								<td>部门级审批</td>
							</tr>
							<tr>
								<td>二级部门负责人审批</td>
								<td>张宏江</td>
								<td>部门级审批</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>通用岗位模型:</td>
					<td class="form_data">
						<table class="table_grid">
							<tr>
								<th>岗位名称</th>
								<th>用户账号</th>
							</tr>
							<tr>
								<td>信息技术总监</td>
								<td>万勇[WANYONG]</td>
							</tr>
							<tr>
								<td>人力资源总监</td>
								<td>王佳玮[WANGJIAWEI]</td>
							</tr>
							<tr>
								<td>供应链采购专员</td>
								<td>黄黎明[HUANGLIMING]\高倩[GAOQIAN]\刘胤君[LIUYINJUN]</td>
							</tr>
							
						</table>
					</td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>组织岗位模型:</td>
					<td class="form_data">
						<table class="table_grid">
							<tr>
								<th>岗位名称</th>
								<th>可选人员范围</th>
							</tr>
							<tr>
								<td>财务经理</td>
								<td>任宁宁[RENNINGNING]\徐垠[XUYIN]</td>
							</tr>
							<tr>
								<td>ERP顾问</td>
								<td>朱琳[ZHULIN2]\李宏辉[LIHONGHUI]\谭红林[TANHONGLIN]</td>
							</tr>
							<tr>
								<td>供应链采购专员</td>
								<td>黄黎明[HUANGLIMING]\高倩[GAOQIAN]\刘胤君[LIUYINJUN]</td>
							</tr>
							
						</table>
					</td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>有效期:</td>
					<td class="form_data">
						<s:textfield name="model.startDate"></s:textfield>
					</td>
				</tr>
				<tr>
					<td class="form_title"><span style="color:red;">*</span>允许状态:</td>
					<td class="form_data">
						<s:radio list="#{'1':'开启','0':'关闭'}" name="model.status"></s:radio>
					</td>
				</tr>
			</table>
			<s:hidden name="model.groupid"></s:hidden> 
			<s:hidden name="model.uploader" theme="simple"/>
			<s:hidden name="model.uploadDate" theme="simple"/>
		</s:form>	
	</div>
</body> 
</html>