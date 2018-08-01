<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head> 
    <title>流程讨论回复</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">
    <link href="iwork_css/upload/uploadify.css" type="text/css" rel="stylesheet" />
    <link href="iwork_css/base-090628.css" rel="stylesheet" type="text/css" />
     <link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
    <script type="text/javascript" src="iwork_js/upload/swfobject.js"></script>
	<script type="text/javascript" src="iwork_js/upload/jquery.uploadify.v2.1.4.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/commons.js"   ></script>
   
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">  
    var errorIds=new Array();//上传队列中错误文件
    var api = art.dialog.open.api, W = api.opener;
    $(document).ready(function(){            
	       var uploadFileNames=new Array();//后台传过来的已经上传到服务器的文件名集合
	       //文件上传
	       var uploadMaxSize=$('#talk_reply_save_uploadMaxSize').val()*1024; //以bit为单位
	       var actDefId=$('#talk_reply_save_actDefId').val(); 
	       var instanceId=$('#talk_reply_save_instanceId').val(); 
	       
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
    							$('#talk_reply_save_uploadifyRealNames').val(uploadFileNames.join());
    							ajaxSub();					             							
						 }
		});
	});	
	//异步提交	
	function ajaxSub(){
	     var queryString = $('#talk_reply_save').serialize();
	     $.post("talk_reply_save.action",queryString,function(data){
	          art.dialog.tips(data,2);
		      api.close();
	     });
	}
	//表单的验证和发送 
    function validateAndSave(){
          var content=$.trim($('#talk_reply_save_talkReply_content').val());
          $('#talk_reply_save_talkReply_content').val(content);
          
          if(content==""){
                 art.dialog.tips("回复内容不能为空！",2);
                 $('#talk_reply_save_talkReply_content').focus();
                 return ;
          }
          if(!document.getElementById('talkReply.remindType-1').checked&!document.getElementById('talkReply.remindType-2').checked&!document.getElementById('talkReply.remindType-3').checked&!document.getElementById('talkReply.remindType-4').checked){
                 art.dialog.tips("请至少选择一种通知类型。",2);
                 return ;
          }
          if(length2(content)>500){
                 art.dialog.tips('回复内容不能超过500个字符（1个汉字2个字符）!',2);
                 $('#talk_reply_save_talkReply_content').focus();
                 return ;
          }
          if($('#fileQueue').html().length>0){
                 if(errorIds.length==0){
                      jQuery('#uploadify').uploadifyUpload();
                   }                      
          }//有文件需要上传
          else{
                 ajaxSub();
          }//不需要上传文件
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
	<s:form name="replyForm"  action="talk_reply_save" method="post" theme="simple" enctype="multipart/form-data">
                <table border="0"  cellspacing="0" cellpadding="0" width="95%">
   					<tr>
   					 	<td class="td_title" >我的回复：</td><td class="td_data"><s:textarea cssStyle="width:300px;height:100;" name="talkReply.content"/>&nbsp;<span style='color:red'>*</span></td>
   					</tr>
   					<tr>
   						<td class="td_title" >通知类型：</td><td class="td_data"><s:checkboxlist list="#{'sms':'短信','email':'邮件','im':'IM','sysmsg':'系统消息'}" value="'sysmsg'" listValue="value" listKey="key" name="talkReply.remindType"  id="talkReply.remindType" label="通知类型"/></td>
   					</tr>
  			  </table>
  			    
		 		<s:hidden name="talkId" value="%{talkId}"/>
		 		<s:hidden name="talkReply.reid" value="%{reid}"/>
		 		<s:hidden name="actDefId" value="%{actDefId}"/>
                <s:hidden name="instanceId" value="%{instanceId}"/>
                <s:hidden name="uploadMaxSize" value="%{uploadMaxSize}"/>
		 		<s:hidden name="uploadifyRealNames" value=""/>
		 		
	</s:form>
	
	<table border="0" cellspacing="0" cellpadding="0" width="95%" >
	       <tr>
   				<td class="td_title" ><img src=iwork_img/attach.png>附件上传：</td>
   				<td class="td_data">
   				   <s:file  name="uploadify" id="uploadify" theme="simple"/><span id="sizeLimit"></span>
   				</td>  					    
   		  </tr>
   		  <tr>
   		       <td class="td_title" >&nbsp;</td>
   		       <td><div id="fileQueue"></div></td>
   		  </tr>
   	</table>
   	
 </div> 
  <div region="south" border="false" style="text-align: right; height: 50px; line-height: 30px;padding-top:5px;padding-right:20px;margin-bottom:20px;">
	<a href="javascript:validateAndSave();" class="easyui-linkbutton"  iconCls="icon-save" plain="false">发送</a>
	<a href="javascript:cancelSave();" class="easyui-linkbutton"  iconCls="icon-Cancel" plain="false">取消</a>
 </div>   
</body>
</html>
