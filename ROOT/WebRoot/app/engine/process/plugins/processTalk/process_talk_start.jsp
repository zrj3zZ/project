<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head> 
    <title>流程讨论发起窗口</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/base-090628.css" rel="stylesheet" type="text/css" />
    <link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
    <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
    <script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/commons.js"   ></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    
    <script type="text/javascript">
    //多选地址薄
   var api = art.dialog.open.api, W = api.opener;
    function multi_book2(isOrg, isRole, isGroup, parentDept, currentDept, startDept, targetUserId, targetUserName, targetDeptId, targetDeptName, defaultField) {
		var code = document.getElementById(defaultField).value;	
		var pageUrl = "multibook_index.action?input="+encodeURI(code)+"&defaultField="+defaultField;
		art.dialog.open(pageUrl,{
					id:'addressDialog', 
					title:"地址簿",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
	
	}
	var errorIds=new Array();//上传队列中错误文件	
	$(document).ready(function() {
	       document.getElementById('talkStarter.remindType-1').disabled="disabled";
	       document.getElementById('talkStarter.remindType-2').disabled="disabled";
	       document.getElementById('talkStarter.remindType-3').disabled="disabled";//功能未开发
	       
	       var uploadFileNames=new Array();//后台传过来的已经上传到服务器的文件名集合
	       //文件上传
	       var uploadMaxSize=$('#talk_starter_save_uploadMaxSize').val()*1024; //以bit为单位
	       var actDefId=$('#talk_starter_save_talkStarter_actDefId').val(); 
	       var instanceId=$('#talk_starter_save_talkStarter_instanceId').val(); 
	       
	       var sizeLimit=Math.round(uploadMaxSize/ 1024 * 100) * .01; //以KB为单位
	       var suffix='KB';
	       if(sizeLimit>= 1024){
	            sizeLimit  = Math.round(sizeLimit/ 1024 * 100) * .01; //以MB为单位
			    suffix = 'MB';
	       }	       
	       $('#sizeLimit').html("(每个文件不能超过"+sizeLimit+suffix+")");      
	       
		  $('#uploadify').uploadify({
		    'uploader'  : 'iwork_js/upload//uploadify.swf',
		    'script'    : 'process_talk_upload.action;jsessionid=<%=session.getId()%>',
		    'cancelImg' : 'iwork_img/del3.gif',
		    'fileDataName'   : 'uploadify',
			'queueID'   : 'fileQueue',
			'buttonText': 'aaa',
			'buttonImg' : 'iwork_img/upload/select.gif',
			'auto'      : false,
		    'multi'     : true,
	   'queueSizeLimit' : 3,
	   'simUploadLimit' : 3,
		    'sizeLimit' : uploadMaxSize,
		    'scriptData'  : {'actDefId':actDefId,'instanceId':instanceId},
			 'onError'  : function (event,queueId,fileObj,errorObj){
			                    if(errorObj.type=="File Size"){
		    						var Ksize  = Math.round(errorObj.info/ 1024 * 100) * .01;
		    						var suffix = 'KB';
									if (Ksize >= 1024) {
										Ksize  = Math.round(Ksize/ 1024 * 100) * .01;
										suffix = 'MB';
									}
		    						errorObj.type="文件大小不能超过"+Ksize+suffix;
		    					}else if(errorObj.type=="HTTP"){
		    						errorObj.type="文件上传失败";
		    					}
		    					errorIds.push(queueId); //错一个，加一个
		    					//setTimeout(function(){$('#uploadify').uploadifyCancel(queueId);},3000);	//三秒之后没有清除，就自动清除	    					
			                },
		    'onCancel' : function(event,queueId,fileObj,data){
		                         for(var i=0;i<errorIds.length;i++){
		                             if(errorIds[i]==queueId){
		                                   errorIds.splice(i,1);
		                             }
		                         }//从上传队列中删除一个错误的文件，errorIds也删除相应错误id
		                 },
		 'onQueueFull' : function (event,queueSizeLimit) {
                                art.dialog.tips("最多只能上传"+queueSizeLimit+"个文件！",2);
                                return false;
                         }, 
         'onComplete'  : function(event, ID, fileObj, response, data) {  							
      							uploadFileNames.push(response);                                 
   						 },                      
	  'onAllComplete'  : function(event, data) {   
    							//所有上传完之后提交表单
    							$('#talk_starter_save_uploadifyRealNames').val(uploadFileNames.join());
    							ajaxSub();					             							
						 }
		});
		 		
	});
	//异步提交	
	function ajaxSub(){
	     var queryString = $('#talk_starter_save').serialize();
	     $.post("talk_starter_save.action",queryString,function(data){
	          art.dialog.tips(data,2);
		      api.close();
	     });
	}
    //表单的验证和发送 
    function validateAndSave(){
          var title=$.trim($('#talk_starter_save_talkStarter_title').val());
          var content=$.trim($('#talk_starter_save_talkStarter_content').val());
          var talkUsers=$.trim($('#talk_starter_save_talkStarter_talkUsers').val());
          $('#talk_starter_save_talkStarter_title').val(title);
          $('#talk_starter_save_talkStarter_content').val(content);
          
          if(title==""){
                 art.dialog.tips("讨论主题不能为空！",2);
                 $('#talk_starter_save_talkStarter_title').focus();
                 return;
          }
          if(content==""){
                 art.dialog.tips("详细内容不能为空！",2);
                 $('#talk_starter_save_talkStarter_content').focus();
                 return ;
          }
          if(talkUsers==""){
                 art.dialog.tips("参与人不能为空，请点击右侧图标选择参与人。",2);
                 $('#talk_starter_save_talkStarter_talkUsers').focus();
                 return ;
          }
          if(!document.getElementById('talkStarter.remindType-1').checked&!document.getElementById('talkStarter.remindType-2').checked&!document.getElementById('talkStarter.remindType-3').checked&!document.getElementById('talkStarter.remindType-4').checked){
                 art.dialog.tips("请至少选择一种通知类型。",2);
                 return ;
          }
          if(length2(title)>120){
                 art.dialog.tips('讨论主题不能超过120个字符（1个汉字2个字符）!',2);
                 $('#talk_starter_save_talkStarter_title').focus();
                 return ;
          }
          if(length2(content)>250){
                 art.dialog.tips('详细内容不能超过250个字符（1个汉字2个字符）!',2);
                 $('#talk_starter_save_talkStarter_content').focus();
                 return ;
          }
          if(length2(talkUsers)>250){
                 art.dialog.tips('选择的参与人过多!',2);
                 $('#talk_starter_save_talkStarter_talkUsers').focus();
                 return ;
          }
          $.post('process_talk_checkTalkUsers.action',{talkUsers:talkUsers},function(data){
                 if(data=="true"){
                       if($('#fileQueue').html().length>0){
                            if(errorIds.length==0){
                                jQuery('#uploadify').uploadifyUpload();
                            }                      
                       }//有文件需要上传
                       else{
                            ajaxSub();
                       }//不需要上传文件
                 }
                 else if(data=="false"){
                       art.dialog.tips('参与人不能包含自己!',2);
                       return ;                 
                 }
          });
    }
    //取消
    function cancelSave(){
        api.close();
    }          
    </script>
    <style type="text/css">
    .td_title{
        width:23%;
    }
    .td_data{
        width:72%;
    }
    </style>
  </head>
  
<body class="easyui-layout">
 <div region="center" style="padding:3px;border:0px;margin-bottom:5px;overflow-y:auto;">
	<s:form name="startTalkForm"  action="talk_starter_save" method="post" theme="simple">
                <table border="0"  cellspacing="0" cellpadding="0" width="95%">
   			        <tr>
   				    	<td class="td_title" >讨论主题：</td><td class="td_data"><s:textfield  cssStyle="width:300px" name="talkStarter.title" value="%{processTitle}"/>&nbsp;<span style='color:red'>*</span></td>
   			 		</tr>
   					<tr>
   					 	<td class="td_title" >详细内容：</td><td class="td_data"><s:textarea  cssStyle="width:300px;height:100;" name="talkStarter.content"/>&nbsp;<span style='color:red'>*</span></td>
   					</tr>
   					<tr>
   						<td class="td_title" >参与人：</td><td class="td_data"><s:textfield  cssStyle="width:300px" name="talkStarter.talkUsers"/>&nbsp;<a href="javascript:multi_book2('false','false','false','false','false','','','','','','talk_starter_save_talkStarter_talkUsers');" class="easyui-linkbutton" iconCls="icon-add" plain="true"></a>&nbsp;<span style='color:red'>*</span></td>
   					</tr> 
   					<tr>
   						<td class="td_title" >通知类型：</td><td class="td_data"><s:checkboxlist list="#{'sms':'短信','email':'邮件','im':'IM','sysmsg':'系统消息'}" value="'sysmsg'" listKey="key" listValue="value" name="talkStarter.remindType"  id="talkStarter.remindType" label="通知类型"/></td>
   					</tr>
  			   </table>
  			    
		 		<s:hidden name="talkStarter.actDefId" value="%{actDefId}"/>
		 		<s:hidden name="talkStarter.proDefId" value="%{proDefId}"/>
		 		<s:hidden name="talkStarter.stepId" value="%{stepId}"/>
		 		<s:hidden name="talkStarter.stepName" value="%{stepName}"/>
		 		<s:hidden name="talkStarter.instanceId" value="%{instanceId}"/>
		 		<s:hidden name="uploadMaxSize" value="%{uploadMaxSize}"/>
		 		<s:hidden name="uploadifyRealNames" value=""/>
		 			
	</s:form>

    <table border="0" cellspacing="0" cellpadding="0" width="95%" >
	       <tr>
   				<td class="td_title"><img src=iwork_img/attach.png>附件上传：</td>
   				<td class="td_data">
   				   <s:file  name="uploadify" id="uploadify" theme="simple"/><span id="sizeLimit"></span>
   				</td>  					    
   		  </tr>
   		  <tr>
   		       <td class="td_title">&nbsp;</td>
   		       <td><div id="fileQueue"></div></td>
   		  </tr>
   	</table>
	           		
 </div>
 
 <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:20px;margin-bottom:20px;">
	<a href="javascript:validateAndSave();" class="easyui-linkbutton"  iconCls="icon-Save" plain="false">发送</a>
	<a href="javascript:cancelSave();" class="easyui-linkbutton"  iconCls="icon-Cancel" plain="false">取消</a>
 </div>     
</body>
</html>
