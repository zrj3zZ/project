<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>流程表单</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
 <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  	<link rel="stylesheet" href="mobile/assets/css/style.css">
  	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/mobile/jquery.mobile-1.4.5.min.css">
	<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css" /> 
	<link rel="stylesheet" href="mobile/assets/lib/weui.min.css">
	<link rel="stylesheet" href="mobile/assets/css/jquery-weui.css">
	<script src="iwork_js/commons.js"></script>  
	<script type="text/javascript"  src="iwork_js/jqueryjs/jquery-3.0.8.min.js"></script>  
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="mobile/assets/js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script> 
	<script type="text/javascript">
	   <s:property value="initWeiXinScript" escapeHtml="false"/>
	   //保存脚本触发器事件
		function formSaveEventScript(){
			<s:property value='saveScriptEvent' escapeHtml='false'/>
		}
		//办理脚本触发器事件
		function formTransEventScript(){
			<s:property value='transScriptEvent' escapeHtml='false'/>
		}
		 <s:property value='script' escapeHtml='false'/>
	     var images = {
              localIds: [],
              serverId: []
            };
	   function uploadImages(fieldName){
            var instanceId = $("#instanceId").val();
            if(instanceId==0){
            	singleSave();
            }

	     wx.chooseImage({
	     		sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
    			sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (res) {
                        images.localIds = res.localIds;
                        //3.上传选择的图片(递归)
                        var i = 0, length = images.localIds.length;
                        var imgs_html  = new Array();
                        var upload = function(){
                            wx.uploadImage({
                                localId:images.localIds[i],
                                isShowProgressTips:1,
                                success: function(res) {
                                    images.serverId.push(res.serverId);
                                    var imgdiv = "<img class=\"imgitem\" src=\""+images.localIds[i]+"\"/>";
                                     //4.预览上传的图片
                                    $("#"+fieldName+"_view").append(imgdiv);
                                    uploadImgs(fieldName,res.serverId);
                                    //如果还有照片，继续上传
                                    i++;
                                    if (i < length) {
                                        upload(); 
                                    }
                                    else {
                                       
                                    } 
                                }
                            });                     
                        };
                        upload();
                  },
fail: function (res){
alert(res.errMsg);
}
                });
         }
         
       function uploadImgs(fieldName,serverId){
            var actDefId = $("#actDefId").val();
            var instanceId = $("#instanceId").val();
            var formId =$("#formId").val();
              var dataid =$("#dataid").val();
	      		$.post('wx_process_fileUpload.action',{actDefId:actDefId,instanceId:instanceId,dataid:dataid,formId:formId,fieldName:fieldName,serverId:serverId}, function (text, status) {
			      		$("#"+fieldName).val(text); 
			      		//alert($("#"+fieldName).val());
	      		});
	      
      			   /*
	   		wx.chooseImage({
	            success: function(res) {
	            	 images.localIds = res.localIds;
	            	 var ul = document.getElementById(fielName+"_view");
	            	  for (i=0;i<localIds.length ;i++ )   
    				{	   
	            	 	i++;
				　　　　//添加 li
				　　　　var li = document.createElement("li");
				　　　　//添加 img
				　　　　var img = document.createElement("img");
				　　　　//设置 img 属性，如 id
				　　　　img.setAttribute("id", "newImg"+i);
				　　　　//设置 img 图片地址
						alert(localIds[i]);
				　　　　img.src = localIds[i];
				　　　　li.appendChild(img);
				　　　　ul.appendChild(li); 
	            	 }
	            	 alert(ul.innerHTML);
	            }
        	});*/
	   }
	   	function showTodoList(){
	   		    var pageUrl = "weixin_processdeskIndex.action";
	   			this.showSendWindow(pageUrl,"待办流程"); 
	   	}
		function showSubform(taskId,instanceId,subformId,subtitle){
			if($("#dataid").val()==0){
				alert('请保存主表单后编辑行项目子表');
				return;
			}
			var actDefId = $("#actDefId").val();
			var actStepDefId = $("#actStepDefId").val();
			var prcDefId = $("#prcDefId").val();
			var excutionId = $("#excutionId").val();
			var pageUrl = "wechat_pr_subform.action?actDefId="+actDefId+"&actStepDefId="+actStepDefId+"&prcDefId="+prcDefId+"&taskId="+taskId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&formId="+subformId;
			this.showSendWindow(pageUrl,"任务办理",800,400); 
		//	window.FrameLink.loadLink(subtitle,pageUrl);    
		}
		
		function dosearchAddress(fieldName,obj){
			if(obj.length>2){
				$.ajax({ 
						url: 'ifrom_mb_radio_address_search.action?fieldName='+fieldName+'&searchkey='+obj, 
						data: $('#iformMain').serialize(), 
						type: "post", 
						cache : false, 
						success: function(data)  
						{
							if(data==""){
								$("#searchLister").html("<li>未发现账户地址</li>");
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}else{
								$("#searchLister").html(data);
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}
						} 
					}
				);
			}
		}
		function dosearchMultiAddress(fieldName,obj){
			if(obj.length>2){
				$.ajax({ 
						url: 'ifrom_mb_multi_address_search.action?fieldName='+fieldName+'&searchkey='+obj, 
						type: "post", 
						cache : false, 
						success: function(data)  
						{
							if(data==""){
								$("#searchLister").html("<li>未发现账户地址</li>");
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}else{
								$("#searchLister").html(data);
								$("#searchLister").listview("refresh"); 
								$("#searchLister").trigger("create");
							}
						} 
					}
				);
			}
		}
		
		//保存表单
		function saveWeiXinForm(){
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}else{
				try{	
					var flag = formSaveEventScript();
						if(flag==false){
							return flag;
						}
				}catch(e){} 
				if(singleSave()){
					var actDefId = $("#actDefId").val();
					var instanceId = $("#instanceId").val();
					var taskId = $("#taskId").val();
					var excutionId = $("#excutionId").val(); 
					window.location ="wechat_pr_formpage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
				}else{ 
					alert('保存失败，请稍后再试!');
				}
			}
		}
		function setAddress(fieldname,obj){
			$('#'+fieldname).val(obj); 
			 $('.ui-dialog').dialog('close'); 
		}
		function singleSave(){
			var isModify = $("#formIsModify").val();
			if(isModify==0){
				return true;
			}
			var url = 'wechat_pr_formsave.action';
			var para = $("#iformMain").serialize();
			var flag = false;
			$.ajax({
				   url:url,
				   data:para,
				   type:"post",
				   async:false,//(默认: true) 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
				   success: function(data){
						if(data.indexOf('success')>-1){
							var arr=data.split(',');
							if(arr!=null&&arr.length==5){
									//更新instanceId和dataid
									document.getElementById('instanceId').value=arr[1];
									document.getElementById('dataid').value=arr[2];
									document.getElementById('taskId').value=arr[3];
									document.getElementById('excutionId').value=arr[4];
									//重新记录主表页面元素初始值
									//logInitPageElementValue();
								flag = true;
							}else{
								flag = false;
							} 
				    	}else{
				    		flag = false;
				    	}
				  }
			});
			return flag;
		}
		function openMonitorPage(){
			var actDefId = $("#actDefId").val();
					var instanceId = $("#instanceId").val();
					var taskId = $("#taskId").val();
					var excutionId = $("#excutionId").val(); 
					window.location ="wechat_pr_mornitor.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
		}
		function openOpinionPage(){
			var actDefId = $("#actDefId").val();
					var instanceId = $("#instanceId").val();
					var taskId = $("#taskId").val();
					var excutionId = $("#excutionId").val(); 
					window.location ="wechat_pr_opinlist.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
		}
		function showFormPage(){
			var actDefId = $("#actDefId").val();
			var instanceId = $("#instanceId").val();
			var taskId = $("#taskId").val();
			var excutionId = $("#excutionId").val(); 
			window.location ="loadProcessFormPage.action?actDefId="+actDefId+"&instanceId="+instanceId+"&excutionId="+excutionId+"&taskId="+taskId;
		}
		//删除图片
		function uploadifyRemoveImg(colName,uuid){ 
			var conf = confirm("确定删除？");
			if(conf){
				var flag = uploadifyRemoveServer(uuid);
				if(flag){
					reSetUUIDs(colName,uuid);
					singleSave();
					this.location.reload();
				}else{
					alert("删除失败");
				}
			}
		}
		//删除后台附件
		function uploadifyRemoveServer(uuid){
			var flag = true;
			$.ajax({
				   type: "POST",
				   async : false,
				   url: "uploadifyRemove.action",
				   data: "fileUUID="+uuid,
				   success: function(msg){
					 if(msg!=null&&msg!=""){
						var resJson = strToJson(msg);
						var r = resJson.flag;
						if(r==true||r=="true"){
							flag=true;
						}else{
							flag=false;
						}
					 }
				   },
				   error:function(msg){
						flag=false;
				   }
			});
			return flag;
		}
		//重置附件字段UUID的值
		function reSetUUIDs(colName,fileUUID){
			var uuids = $("#"+colName).attr("value");
			var newuuids = removeUUID(uuids,fileUUID);
			$("#"+colName).attr("value",newuuids);
		}
		//把json字符串转换成json对象
		function strToJson(str){
		    var json = (new Function("return " + str))();
		    return json;
		}
    </script>
	<style type="text/css">
		.transBtn{
			width:400px;
		}
		.transBtn li{
			line-height:30px;
		}
		.transBtn li:hover{
			background-color:#999;
		}
		.mb_title{ 
			color:#999;
			font-weight:bold;
			font-size:16px;
		}
		.mb_data{
			color:#0000ff;
			font-size:16px;
			padding-left:20px;
			border-bottom:1px solid #efefef;
			
		}
		.imageslist{
			list-style:none;
		}
	</style>
</head>
<body>
	<div data-role="page" class="type-interior"> 
	<div data-role="header" class="ui-body-d ui-body"　style="overflow:hidden;" data-position="fixed">
        <div data-role="controlgroup" data-type="horizontal">
		     <s:property value="pageTab" escapeHtml="false"/>
		</div>
	</div>
	<div data-role="content"> 
		<form id="iformMain" name="iformMain" method="post" >
			<s:property value='content' escapeHtml='false'/> 
			<!--  
			<a href="javascript:executeHandle();" data-role="button" data-icon="check">办理</a>
			<a href="#popupBasic" data-rel="popup">Open Popup</a> -->
			<!--表单参数-->
		<span style="display:none">
			<s:hidden name="modelId"/>
			<s:hidden name="modelType"/> 
			<s:hidden name="taskType"/> 
			<s:hidden name="formIsModify"/>
			<s:hidden name="isLog"/> 
			<s:hidden id ="actDefId" name="actDefId"/>
			<s:hidden  id ="prcDefId" name="prcDefId"/>
			<s:hidden id ="actStepDefId" name="actStepDefId"/>
			<s:hidden id ="formId" name="formId"/>
			<s:hidden id ="taskId" name="taskId"/>
			<s:hidden id ="instanceId" name="instanceId"/>
			<s:hidden id ="excutionId" name="excutionId"/>
			<s:hidden id ="dataid" name="dataid"/>
			<s:hidden id ="deviceType" name="deviceType"/>
			<input name='submitbtn' id='submitbtn' type="submit" />
		</span> 
	</form>
	</div><!-- /footer -->
		
	<div data-role="footer"  data-position="fixed" style="padding-left:10px;" >
		   <s:property value="transButton" escapeHtml="false"/>
	    </div><!-- /navbar -->  
	</div><!-- /content --> 
</body>
</html>
<script>
function hideOption(){
	showSysTips();
	wx.hideOptionMenu();
}
setTimeout("hideOption()",500);
</script>