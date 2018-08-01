<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <title>问题详情</title>

    <link rel="stylesheet" type="text/css" href="iwork_css/km/know_form.css"/>    
    <link rel="stylesheet" type="text/css" href="iwork_css/km/know_question_detail.css"/> 
    <link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
    <link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/commons.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.position.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="iwork_js/km/know.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
  <style>
	.ui-autocomplete-loading { background: white url('/iwork_css/jquerycss/ui/base/images/ui-anim_basic_16x16.gif') right center no-repeat; }
	.ui-autocomplete { 
		max-height: 250px; 
		overflow-y: auto; 
		overflow-x: hidden; 
		padding-right: 5px; 
		} 
	* html .ui-autocomplete { 
		height: 250px; 
		}
	.td_data {
		color: blue;
		line-height: 23px;
		text-align: left;
		padding-left: 3px;
		font-size: 12px;
		font-family: "宋体";
		vertical-align: middle;
		word-wrap: break-word;
		word-break: break-all;
		font-weight: 500;
		line-height: 15px;
		padding-top: 5px;
	}	 
  </style>
  <script type="text/javascript">
  var mainFormValidator;
  $().ready(function() {
  mainFormValidator =  $("#editForm").validate({
   });
   mainFormValidator.resetForm();
  });
  
  //编辑问题
    function editQuestion(qid){
			  var pageUrl = 'know_edit_question.action?qid='+qid;
			  art.dialog.open(pageUrl,{
			    	id:'dg_question',
			    	title:'编辑问题',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:850,
				    height:600
				 });
    }
  //修改回答
    function editAnswer(aid,qid){
    	var url = 'know_edit_answer.action?aid='+aid+'&qid='+qid;
	    art.dialog.open(url,{
				id:'dg',
				cover:true,
				title:'编辑问题',
				loadingText:'加载中……',
				bgcolor:'#FFFFFF',
				rang:true,
				width:850,
				cache:false,
				lock: true,
				height:600, 
				iconTitle:false, 
				extendDrag:true,
				autoSize:true,
				max: false, 
				min: false
			});	
	}
  //赞一个
	function holdout(aid){  
	    $.post('know_answer_holdout.action',{aid:aid},function(data){  
	    	 if(data=='ok'){
	    	     location.reload();
	    	 }
	    });
    }
  //打开评论
    function talkopen(aid,type){
    document.getElementById('comment_div_'+aid).style.display = '';
    document.getElementById('comment_content_'+aid).style.display = '';
    document.getElementById('comment_submit_'+aid).style.display = '';
    document.getElementById('talk_span_'+aid).innerHTML = '<a class="holdout_span_c1" href="#" onclick="talkclose(\''+aid+'\');return false;">评论</a>';
  } 
 //关闭评论
 function talkclose(aid){ 	
 	document.getElementById('comment_div_'+aid).style.display = 'none'; 	
 	document.getElementById('talk_span_'+aid).innerHTML = '<a class="holdout_span_c1" href="#" onclick="talkopen(\''+aid+'\');return false;">评论</a>'; 
  }
 //保存评论
   talkErrorFunc=function(){
           art.dialog.tips("评论失败！",2);
      }
   talkSuccessFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("评论成功！",2);
              location.reload();
           }
      }
 function talksave(aid){
     var options = {
					  error:talkErrorFunc,
					  success:talkSuccessFunc 
			   	   }; 
	 var content = $.trim($('#talkcontent_'+aid).val());
	 $('#talkcontent_'+aid).val(content);
	 if(content==''){
	      art.dialog.tips("请填写评论内容",2);
	      $('#talkcontent_'+aid).focus();
	      return;
	  }		   	   
	 $('#talkForm'+aid).ajaxSubmit(options); 
 }
 //删除评论
 function deletetalk(tid){
 	  art.dialog.confirm("确定删除该评论？",function(){
 	  	 $.post('know_talk_delete.action',{tid:tid},function(data){  
	    	 if(data=='ok'){
	    	     location.reload();
	    	 }
	    });	  
 	  },function(){});      
 }   
 //打开邀请回答人
   function showman(cmd){
	document.getElementById('line_space').style.visibility='visible';
	$('#INVITEDMAN').focus();
 }
 //邀请回答人，联想
 $(function() {
		function split( val ) {
			return val.split(",");
		}
		function extractLast( term ) {
			return split( term ).pop();
		}

		$( "#INVITEDMAN" )
			// don't navigate away from the field on tab when selecting an item
			.bind( "keydown", function( event ) {
				if ( event.keyCode === $.ui.keyCode.TAB &&
						$( this ).data( "autocomplete" ).menu.active ) {
					event.preventDefault();
				}
			})
			.autocomplete({
				source: function( request, response ) {
					$.getJSON( "know_liveSearch_userInfo.action", {
						term: extractLast( request.term )
					},
					 function( data ) {
						response( $.map( data, function( item ) {
							return {
								label: item.userId+'['+item.userName+']',
								value: item.userId+'['+item.userName+']'
							}
						}));
					}
					
					);
				},
				search: function() {
					// custom minLength
					var term = extractLast( this.value );
					if ( term.length < 2 ) {
						return false;
					}
				},
				focus: function() {
					// prevent value inserted on focus
					return false;
				},
				select: function( event, ui ) {
					var terms = split( this.value );
					// remove the current input
					terms.pop();
					// add the selected item
					terms.push( ui.item.value );
					// add placeholder to get the comma-and-space at the end
					terms.push( "" );
					this.value = terms.join( "," );
					return false;
				}
			});
		$('a.basic').cluetip({
				cluetipClass: 'rounded', 
				dropShadow: false, 
				cursor: 'pointer',
				positionBy: 'mouse',
				arrows: true,
				showTitle: false
			});	
	});
  //保存回答
      errorFunc=function(){
           art.dialog.tips("回答失败！",2);
      }
    successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("回答成功！",2);
              location.reload();
           }
      }
   /*保存回答*/
    function save(){
    	var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
			return false;
		}
    	 var ACONTENT = $.trim($('#ACONTENT').val());
        $('#ACONTENT').val(ACONTENT);
               
         var INVITEDMAN  = $.trim($('#INVITEDMAN').val());
         if(INVITEDMAN=='多个被邀请回答人请用逗号分隔'){
               INVITEDMAN = '';  
            }
         if(INVITEDMAN.length>0){
        	if(INVITEDMAN.charAt(INVITEDMAN.length-1)==','){
        		INVITEDMAN=INVITEDMAN.substring(0,INVITEDMAN.length-1);
        	}
        }//去掉最后一个逗号，如果有的话 
        $('#INVITEDMAN').val(INVITEDMAN);
        
        if(ACONTENT=="" || ACONTENT=="请填写您的回答"){
              art.dialog.tips("请输入回答内容！",2);
               $('#ACONTENT').focus();
              return ;
          }      
		$.post('know_check_user_is_exist.action',{yg:INVITEDMAN},function(data){  
               if(data.length>0){
                      var tip='输入错误！<br/>'+data+ '请点击“地址簿”选择正确的员工。';
                      art.dialog.tips(tip,2);
                      return ;
                 }
              else{                                
        			var options = {
							error:errorFunc,
							success:successFunc 
			   			};  
					$('#editForm').ajaxSubmit(options);  							 	
                 }
        });  //检查邀请回答人是否存在  
    
    }	
  //修改回答
    function editAnswer(aid,qid){
	 var pageUrl = 'know_edit_answer.action?aid='+aid+'&qid='+qid;
	  art.dialog.open(pageUrl,{
			    	id:'dg_editDlg',
			    	title:'编辑问题',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:600,
				    height:410
				 });
	}
  //修改回答
    function editAnswer(aid,qid){
	 var url = 'know_edit_answer.action?aid='+aid+'&qid='+qid;
	 art.dialog.open(url,{
					id:'dg',
					cover:true,
					title:'编辑问题',
					loadingText:'加载中……',
					bgcolor:'#FFFFFF',
					rang:true,
					width:600,
					cache:false,
					lock: true,
					height:300, 
					iconTitle:false, 
					extendDrag:true,
					autoSize:true,
					max: false, 
    				min: false
				});	
	}
  //删除回答
    function deleteAnswer(aid,able){
        if(able==0){
            art.dialog.tips("您邀请了其他人回答该问题，不能删除",2);
            return;
        }
        else{
            art.dialog.confirm("确定删除该回答？",function(){
            	$.post('know_answer_delete.action',{aid:aid},function(data){  
	    	 		if(data=='ok'){
	    	    		 location.reload();
	    			 }
	    		});
             },function(){});
        }		
    }
  //选为最佳回答
   function setBestAnswer(aid){
   		art.dialog.confirm("确定设置该回答为最佳答案？",function(){
            	$.post('know_answer_setbest.action',{aid:aid},function(data){   
	    	 		if(data=='ok'){
	    	    		 location.reload();
	    			 }
	    		});
             },function(){});   
   }   	
   /*多选地址薄*/
   function multi_book(defaultField) {
		var code = document.getElementById(defaultField).value;	
		if(code=="多个被邀请回答人请用逗号分隔"){
		     code=""; 
		}  
		var pageUrl = "multibook_index.action?input=" + encodeURI(code) + "&defaultField="+defaultField;
		art.dialog.open(pageUrl,{
					id:'addressDialog', 
					title:"地址簿",
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:510
				 });
	//	$.dialog.data("paramObj",obj);
	}
	 //文件上传
   /* function showUploadifyPage(){ 
        var pageUrl = 'know_showUploadifyPage.action?parentHidId=parentHidId&parentFileDivId=parentFileDivId&multi=true';
         art.dialog.open(pageUrl,{
			    	id:'dg_upload',
			    	title: '上传附件',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:410
				 });
	} */
	function uploadifyDialog(dialogId,fieldName,divId,sizeLimit,multi,fileExt,fileDesc){
		if(dialogId==null||dialogId==""||fieldName==null||fieldName==""||divId==null||divId==""){
			alert('参数不正确');
			return ;
		}
        var pageUrl = 'showUploadifyPage.action?parentColId='+fieldName+'&parentDivId='+divId+'&sizeLimit='+sizeLimit+'&multi='+multi+'&fileExt='+fileExt+'&fileDesc='+fileDesc+'';
		art.dialog.open(pageUrl,{
			id:dialogId,
			title: '上传附件',
			pading: 0,
			lock: true,
			width: 650,
			height: 500, 
			close:function(){
				this.focus();
			}
		}); 
		return ;
	}
	 //验证UUID串的长度
   function ValidateUUIDsLength(){
   		var uuids = $('#parentHidId').val();
   		if(length2(uuids)>500){
   		    return false;
   		}
   		return true;
   } 	
  </script>
  </head>
    
  <body style="text-align:center; margin-top:0px;">
  <div class="main">
     	
     <div style="text-align:left; margin-top:0px; margin-bottom:10px; padding-left:20px; height:50px;"><img src="/iwork_img/know/oaknow_logo.gif" align="left"/></div>
     
     
     <s:iterator value="questionDetail">   
       <div class="content" id="q_content">          
         <div class="content_title">
         	<span style="font-size:14px; font-weight:bold; margin-left:10px; color:#333;">
         		<s:if test="qtype==0">
         		问题内容
         		</s:if>
         		<s:elseif test="qtype==1">
         		已回答的问题
         		</s:elseif>
         	</span>
         	<span style="margin-left:10px;"> 
         		<span class="span_m" style="line-height:20px;">问题分类：</span>
         		<span style="color:#bf0102;">[<s:property value='className'/>]</span>
         		<s:if test="score!=0">
         		&nbsp;&nbsp;<img src="/iwork_img/know/repo_rep.gif" />
         		<span class="span_m">&nbsp;<s:property value='score'/></span>
         		</s:if>
         		<s:if test="qtags!=null && qtags!=''">
         		&nbsp;&nbsp;<span class="span_m">自定义标签：<s:property value='qtags'/></span>
         		</s:if>
         		&nbsp;&nbsp;<span class="span_m">点击量：<s:property value='clickcount'/></span>
         	</span>
       	</div>             
       	<div class="content_frame" id="content_frame">               
       		<div class="content_main">                 
       			<div style="margin-top:7px; margin-bottom:7px;vertical-align:top;padding-right:30px;">
       				<img src="/iwork_img/know/question.gif"/><span style="font-size:12px;">&nbsp;&nbsp;<s:property value='qcontent'/></span>
       			</div>
       			
       			 <table border="0" cellspacing="0" cellpadding="0">
                         <s:if test="qfileList!=null && qfileList.size()>0">
                            <tr><td class="td_data"><img border="0" src="iwork_img/attach.gif"/>附件：</td></tr>  
                         </s:if>
                         <s:iterator value='qfileList'>                                 				                       
                            <tr><td class="td_data">                         
                                     <a href="know_uploadifyDownload.action?fileUUID=<s:property value='fileId'/>" target="_blank">
                                        <img onerror="this.src='iwork_img/attach.gif'" border="0" src="iwork_img/attach.gif"/> 
                                        <s:property value='fileSrcName'/>
                                     </a>
                            </td></tr> 
                         </s:iterator>                                      
                 </table>
                      
       			<s:if test="answerbody!=null && answerbody!=''">                
       			<div style="margin-top:7px; margin-bottom:7px; vertical-align:top;">
       				<img src="/iwork_img/know/invited.jpg"/><span style="font-size:12px;">&nbsp;&nbsp;邀请回答人：<s:property value='answerbody'/></span>
       			</div> 
       			</s:if>                             
       		</div>               
       		<div class="content_main_bottom">                   
       			<table cellspacing="0" cellpadding="0" width="100%" align="center" border="0">                     
       				<col width="50%"/>                     
       				<col width="50%"/>                     
       				<tr>                         
       					<td align="left" valign="top" style="padding-left:20px;">
       						<s:if test="qtype==0 && loginuid==quid">
       					 	<div style="margin-top:7px; margin-bottom:8px;vertical-align:bottom;">
       					 		<img src="/iwork_img/know/edit_question.jpg"/><a class="holdout_span_c1" style="padding-left:5px;" href="#" onClick="editQuestion(<s:property value='qid'/>);">编辑我的提问</a>	
       					 	</div>
       					 	</s:if>
       					</td>                         
       					<td align="right" style="padding-right:20px;">
       						<div style="margin-top:7px; margin-bottom:8px;">提问者：<s:if test="shownametype==0">匿名</s:if><s:else><s:property value='quname'/></s:else> | <s:property value='beginTime'/></div>	
       					</td>                     
       				</tr>                   
       			</table>              
       		 </div>             
       	</div> 

<!-- 我来回答 -->       	
     <s:if test="qtype==0">  
     <s:form name="editForm" id="editForm" action="know_save_answer.action" method="post" theme="simple"> 	          
       	<div class="ask_main_top" id="answer_commit">                 
       		<table border="0" cellspacing="0" cellpadding="0" width="650" id="answer_tab">                     
       			<tr>                         
       				<td align="left" style="width:140px;">
       				 <span style="font-size:14px; font-weight:bold; color:#333;">我来回答</span>&nbsp;&nbsp;&nbsp;&nbsp;
       				 <a class="holdout_span_c1" href="#" onclick="showman();return false;" title="邀请其他人回答这个问题">邀请回答人</a>
       				</td>                         
       				<td align="left">                             
       					<table border="0" cellpadding="0" cellspacing="0" id="line_space" style="visibility:hidden;">
       						<tr>
       							<td align="left" style="width:359px;">
       							    <s:textfield id="INVITEDMAN" name="answer.invitedman"  onkeyup="getuserbycode(this);" cssClass="actionsoftInput {string:true}" cssStyle="color:#D1D1D1;width:339px;" onblur="dropFocus(this,'多个被邀请回答人请用逗号分隔');" onfocus="getFocus(this,'多个被邀请回答人请用逗号分隔');"/>       							
       							</td>
       							<td align="left">
       								<input type=button value='地址簿' name='ANSWERBODY_Btn' class='Btn_s_a'  onclick="multi_book('INVITEDMAN');" border='0'>
       							</td>
       						</tr>
       					</table>                       
       				</td>                         
       				<td align="right">
       				  <input type="button" name="answer" class ="Btn_s_a_r" value="提交回答" onClick="save();"/>
       				 </td>                     
       		  </tr>                 
       	  </table>             
        </div>                     
        <div class="ask_main" id="answer_commit_content">  
             <s:textarea  name='answer.acontent' id='ACONTENT' value="请填写您的回答" cssStyle="height:55px; width:630px; border:2px solid #056ea4; padding:10px; color:#d1d1d1" rows="5" cols="120" onblur="getInfo(this);" onfocus="setInfo(this);" cssClass="{string:true}"/>
  			 <img src="/iwork_img/notNull.gif"/> 
  		 <table>
  			 <tr>
  			    <td align=right >相关附件：</td>
    			<td align=left valign=bottom>
      			  
	      				<div id='parentFileDivId'>
	      				<div><s:hidden id='parentHidId' name='answer.fileuuids'/></div>
	     			    <div><input type=button value='上传附件' class='Btn_s_a' onclick='showUploadifyPageXGZL()' border='0'></div>
						<script>
							function showUploadifyPageXGZL(){
								mainFormAlertFlag=false;
								saveSubReportFlag=false;
								var valid = mainFormValidator.form();
								if(!valid){
									return false;
								}
								mainFormAlertFlag=false;
								saveSubReportFlag=false;
								uploadifyDialog('parentHidId','parentHidId','parentFileDivId','','true','','');
							}
						</script>
                  </div>
              </td>
            </tr>
  		</table> 			            
        </div> 
        
      <s:hidden name="answer.id"/>
   	  <s:hidden name="qid" value="%{qid}"/>
      <s:hidden name="answer.auid"/>
      <s:hidden name="answer.auname"/>
      <s:hidden name="answer.aattachment"/>
      <s:hidden name="answer.aaddcontent"/>
      <s:hidden name="answer.aaddattachment"/>
      <s:hidden name="answer.atime"/> 
      <s:hidden name="answer.atype"/>
   </s:form>      
   </s:if>
<!-- 我来回答end --> 
 
<!-- 最佳答案及其评论 -->        
        <s:elseif test="qtype==1">
        <s:iterator value="bestanswer">
        <div class="answer_main">
          <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">         
              <tr>
              	<td align="left" id="<s:property value='aid'/>" style="padding-left:20px;padding-right:20px; line-height:20px;" ><s:property value='acontent'/></td>
              </tr>
              
              <tr><td>              
                      <table border="0" cellspacing="0" cellpadding="0">
                         <s:if test="afileList!=null && afileList.size()>0">
                            <tr><td class="td_data"><img border="0" src="iwork_img/attach.gif"/>附件：</td></tr> 
                         </s:if>
                         <s:iterator value='afileList'>                                  				                       
                            <tr><td class="td_data">                         
                                     <a href="know_uploadifyDownload.action?fileUUID=<s:property value='fileId'/>" target="_blank">
                                        <img onerror="this.src='iwork_img/attach.gif'" border="0" src="iwork_img/attach.gif"/> 
                                        <s:property value='fileSrcName'/>
                                     </a>
                            </td></tr>
                         </s:iterator>                                       
                     </table>                
              </td></tr>
                
              <tr>
              	<td align="right" style="padding-right:20px;" >
              		<div style="margin-top:7px; margin-bottom:8px;">
              			回答者：<a class="basic" style="text-align:left;"  rel='user_tip.action?userid=<s:property value='auid'/>' href="user_tip.action?userid=<s:property value='auid'/>" onclick="return false;"><s:property value='auname'/></a> | <s:property value='atime'/>
              			<s:if test="ishold=='true'">
              			<%--  点赞功能缺陷可无限点赞  &nbsp;&nbsp;<a style="color:#bbbbbb;" href="#" onclick="return false;">赞</a>
			   		 	   			<span style="color:red;" id="holdout_num_<s:property value='aid'/>">[<s:property value='holdout'/>]</span> --%>
			   		 	</s:if>
			   		 	<s:else>
			   		 	&nbsp;&nbsp;<span id="holdout_span_<s:property value='aid'/>">
			   		 	   				<%-- <a class="holdout_span_c1" href="#" onclick="holdout(<s:property value='aid'/>);">赞</a>
			   		 	   			</span>
			   		 	   			<span style="color:red;" id="holdout_num_<s:property value='aid'/>">[<s:property value='holdout'/>]</span> --%>
			   		 	</s:else>             			
              			&nbsp;&nbsp;<span id="talk_span_<s:property value='aid'/>"><a class="holdout_span_c1" href="#" onclick="talkopen(<s:property value='aid'/>);return false;">评论</a></span>
              			<span style="color:red;" id="talk_num_<s:property value='aid'/>">(<s:property value='talkcount'/>)</span>
              		</div>
              	</td>
              </tr>
              <tr>
              	<td colspan=2 id="talk_table_<s:property value='aid'/>" align=right style="padding-left:60px;">
              	
              	   <s:if test="talklist==null || talklist.size()==0">
              	   
              	 <form id="talkForm<s:property value='aid'/>" action="know_talk_save.action" method="post">
              		<div class="comment" id="comment_div_<s:property value='aid'/>" style="display:none;" >   <!-- style="display:none;"要控制 -->
              			<div class="comment_title">评论（<s:property value='talkcount'/>）</div>
              			<div id="comment_content_<s:property value='aid'/>" style="text-align:left;margin-bottom:5px;">
              			    <textarea  name="talk.talkcontent" id="talkcontent_<s:property value='aid'/>" style="width:600px;" rows="3" class="{string:true}"></textarea>
              			</div>
              			<div id="comment_submit_<s:property value='aid'/>" style="text-align:left;margin-top:5px;">
              				<input type="button" class ="Btn_s_a_r" value="提交评论" onclick="talksave(<s:property value='aid'/>);" />
              			</div>
              	   </div>
              	   
              	   <s:hidden name="talk.id"/>
              	   <s:hidden name="aid" value="%{aid}"/>
              	   <s:hidden name="talk.talkman"/>
              	   <s:hidden name="talk.talkname"/>
              	   <s:hidden name="talk.status"/>
              	   <s:hidden name="talk.talktime"/>
               </form>   
              	   
              </s:if>
              	
              	<s:else>
              	   <div class="comment" id="comment_div_<s:property value='aid'/>">
              			<div class="comment_title">评论（<s:property value='talkcount'/>）</div>              			
             			
             			<s:iterator value="talklist">
             			  <div class="comment_main">
              				<div style="margin-top:7px; margin-bottom:8px;vertical-align:top;">
              				   <s:if test="isAdmin=='true'">
              					<span style="cursor:pointer;" onclick="deletetalk(<s:property value='tid'/>);"><img src="/iwork_img/know/delete2.gif"/></span>&nbsp;&nbsp;
              				   </s:if>
              				   <s:if test="status==1">
              				     该评论已删除
              				   </s:if>
              				   <s:else>
              				     <s:property value="talkcontent"/>
              				   </s:else>               				   
              				 </div>
              			</div>	
              			<div class="comment_bottom"><s:property value="talkname"/>&nbsp;<s:property value="talktime"/></div>    
              		 </s:iterator>
              			
              	 <form id="talkForm<s:property value='aid'/>" action="know_talk_save.action" method="post">
              		    <div id="comment_content_<s:property value='aid'/>" style="text-align:left;margin-bottom:5px;display:none;"><!-- style加上"display:none;"要控制 -->
              				<textarea class="{string:true}"  name="talk.talkcontent" id="talkcontent_<s:property value='aid'/>" style="width:600px;" rows="3"></textarea>
              			</div>
              			<div id="comment_submit_<s:property value='aid'/>" style="text-align:left;margin-top:5px;display:none;"><!-- style加上"display:none;"要控制 -->
              				<input type="button" class ="Btn_s_a_r" value="提交评论" onclick="talksave(<s:property value='aid'/>);" />
              			</div>      				
              	   
              	   <s:hidden name="talk.id"/>
              	   <s:hidden name="aid" value="%{aid}"/>
              	   <s:hidden name="talk.talkman"/>
              	   <s:hidden name="talk.talkname"/>
              	   <s:hidden name="talk.status"/>
              	   <s:hidden name="talk.talktime"/>
               </form>	
              	
              	</div>             	                 			 
              </s:else>
              	 
              	</td>
              </tr>             
          </table>
      </div>
      </s:iterator>
      </s:elseif>
<!-- 最佳答案及其评论end -->       
      </div>
      
      <div class="other" id="other_answer">
		    <s:if test="qtype==1">
			<div class="other_title">其他回答（<s:property value='answerCount'/>）</div>
			</s:if>
			<s:else>
			 <div class="other_title">回答（<s:property value='answerCount'/>）</div>
			</s:else>
			 
			 <s:iterator value="answerlist" status="answerSta">
			   <div class="other_main" id="<s:property value='aid'/>">   <!-- id为answerid？ -->
			        <s:if test="atype==2">
			   		 <div style="margin-top:7px; margin-bottom:7px;vertical-align:top;padding:0 20px;">该回答已被回答者本人删除！</div>
			   		</s:if>
			   		<s:elseif test="atype==3">
			   		 <div style="margin-top:7px; margin-bottom:7px;vertical-align:top;padding:0 20px;">该回答已被管理员删除！</div>
			   		</s:elseif>
			   		<s:else>
			   		 <div style="margin-top:7px; margin-bottom:7px;vertical-align:top;padding:0 20px;"><s:property value='acontent'/></div>
			   		</s:else>			   				   	
               
                      <table border="0" cellspacing="0" cellpadding="0">
                          <s:if test="afileList!=null && afileList.size()>0">
                            <tr><td class="td_data"><img border="0" src="iwork_img/attach.gif"/>附件：</td></tr> 
                          </s:if>
                          <s:iterator value='afileList'>                                 				                       
                            <tr><td class="td_data">                         
                                     <a href="know_uploadifyDownload.action?fileUUID=<s:property value='fileId'/>" target="_blank">
                                        <img onerror="this.src='iwork_img/attach.gif'" border="0" src="iwork_img/attach.gif"/> 
                                        <s:property value='fileSrcName'/>
                                     </a>
                            </td></tr> 
                          </s:iterator>                                    
                      </table>               
                     
                    <s:if test="invitedman!=null && invitedman!=''">
			   		 <div style="margin-top:7px; margin-bottom:7px;vertical-align:top;">
			   		  	<img src="/iwork_img/know/invited.jpg"/>
			   		  	<span style="margin-left:5px; vertical-align:bottom;">邀请回答人：<s:property value='invitedman'/></span>
			   		 </div>			   		  	
			   		</s:if>	
			   		 <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
			   		 	<col width=50%>
			   		 	<col width=50%>
			   		 	<tr>
			   		 		<td align="left" style="padding-left:20px;" >
			   		 			<div style="margin-top:7px; margin-bottom:8px;vertical-align:top;">
			   		 			<s:if test="loginuid==auid && qtype==0 && atype==0">
			   		 				<img src="/iwork_img/know/edit_anwser.jpg"/>
			   		 				<span style="margin-left:5px;">
			   		 					<a class="holdout_span_c1" href="#" onClick="editAnswer(<s:property value='aid'/>,<s:property value='qid'/>);">修改回答</a>
			   		 				</span>&nbsp;&nbsp;&nbsp;&nbsp;
			   		 				<img src="/iwork_img/know/delete_anwser.jpg"/>
			   		 				<span style="margin-left:5px;">
			   		 					<a class="holdout_span_c1" href="#" onClick="deleteAnswer(<s:property value='aid'/>,<s:property value='delNum'/>);">删除回答</a>
			   		 				</span>&nbsp;&nbsp;&nbsp;&nbsp;
			   		 			</s:if>
			   		 			<s:elseif test="isAdmin=='true' && qtype==0 && atype==0">	
			   		 				<img src="/iwork_img/know/delete_anwser.jpg"/>
			   		 				<span style="margin-left:5px;">
			   		 					<a class="holdout_span_c1" href="#" onClick="deleteAnswer(<s:property value='aid'/>,1);">删除回答</a>
			   		 				</span>&nbsp;&nbsp;&nbsp;&nbsp;
			   		 			</s:elseif>
			   		 			<s:if test="loginuid==quid && qtype==0 && atype==0">
			   		 				<img src="/iwork_img/know/set_best.jpg"/>
			   		 				<span style="margin-left:5px;">
			   		 					<a class="holdout_span_c1" href="#" onClick="setBestAnswer(<s:property value='aid'/>);">选为最佳回答 </a>
			   		 				</span>
			   		 			</s:if>
			   		 			 </div>
			   		 	   </td>
			   		 	   <td align="right" style="padding-right:20px;" >
			   		 	   		<div style="margin-top:7px; margin-bottom:8px;">回答者：
			   		 	   			<a class="basic" style="text-align:left;" rel="user_tip.action?userid=<s:property value='auid'/>" href="user_tip.action?userid=<s:property value='auid'/>" onclick="return false;"><s:property value='auname'/></a> | <s:property value='atime'/>
			   		 	   			<s:if test="ishold=='false'">
			   		 	   			<%-- &nbsp;&nbsp;<span id="holdout_span_<s:property value='aid'/>">
			   		 	   							<a class="holdout_span_c1" href="#" onclick="holdout(<s:property value='aid'/>);">赞</a>
			   		 	   						</span>
			   		 	   						<span style="color:red;" id="holdout_num_<s:property value='aid'/>">[<s:property value='holdout'/>]</span>	 --%>
			   		 	   			</s:if>
			   		 	   			<s:elseif test="ishold=='true'">	
			   		 	   			<%-- &nbsp;&nbsp;<a style="color:#bbbbbb;" href="#" onclick="return false;">赞</a>
			   		 	   						<span style="color:red;" id="holdout_num_<s:property value='aid'/>">[<s:property value='holdout'/>]</span>	 --%>
			   		 	   			</s:elseif>
			   		 	   			&nbsp;&nbsp;<span id="talk_span_<s:property value='aid'/>">
			   		 	   							<a class="holdout_span_c1" href="#" onclick="talkopen(<s:property value='aid'/>);return false;">评论</a>
			   		 	   						</span>
			   		 	   						<span style="color:red;" id="talk_num_<s:property value='aid'/>">(<s:property value='talkcount'/>)</span>	
			   		 	   		</div>
			   		 	   	</td>
			   		 	</tr>
			   		 	<tr>
			   		 		<td colspan=2 id="talk_table_<s:property value='aid'/>" align=right style="padding-left:60px;">
			   		 			<!-- 答案评论 -->
			   		 			
              	   			<s:if test="talklist==null || talklist.size()==0">
              					<div class="comment" id="comment_div_<s:property value='aid'/>" style="display:none;" ><!-- style="display:none;"要控制 -->
              						
              					 <form id="talkForm<s:property value='aid'/>" action="know_talk_save.action" method="post">	
              						<div class="comment_title">评论（<s:property value='talkcount'/>）</div>
              						<div id="comment_content_<s:property value='aid'/>" style="text-align:left;margin-bottom:5px;">
              							<textarea class="{string:true}"  name="talk.talkcontent" id="talkcontent_<s:property value='aid'/>" style="width:600px;" rows="3"></textarea>
              						</div>
              						<div id="comment_submit_<s:property value='aid'/>" style="text-align:left;margin-top:5px;">
              							<input type="button" class ="Btn_s_a_r" value="提交评论" onclick="talksave(<s:property value='aid'/>);" />
              						</div>
              						
              						<s:hidden name="talk.id"/>
              	  			   		<s:hidden name="aid" value="%{aid}"/>
              	  			   		<s:hidden name="talk.talkman"/>
              	  			   		<s:hidden name="talk.talkname"/>
              	  			   		<s:hidden name="talk.status"/>
              	  			   		<s:hidden name="talk.talktime"/>
               				  </form>	
              					
              					</div>
              	 			</s:if>	
              	 			<s:else> 
              	   			<div class="comment" id="comment_div_<s:property value='aid'/>">
              						<div class="comment_title">评论（<s:property value='talkcount'/>）</div>              			
             						
             					 <s:iterator value="talklist">
             			 		    <div class="comment_main">
              							<div style="margin-top:7px; margin-bottom:8px;vertical-align:top;">
              				  			 <s:if test="isAdmin=='true'">
              								<span style="cursor:pointer;" onclick="deletetalk(<s:property value='tid'/>);"><img src="/iwork_img/know/delete2.gif"/></span>&nbsp;&nbsp;
              				  			 </s:if>
              				  			 <s:if test="status==1">
              				    			 该评论已删除
              				  			 </s:if>
              				  			 <s:else>
              				   			     <s:property value='talkcontent'/>
              				   			 </s:else>            				   
              				 			</div>
              						</div>	
              						<div class="comment_bottom"><s:property value='talkname'/>&nbsp;<s:property value='talktime'/></div>    
              					</s:iterator>
              					
              					<form id="talkForm<s:property value='aid'/>" action="know_talk_save.action" method="post">             						
              		   			    <div id="comment_content_<s:property value='aid'/>" style="text-align:left;margin-bottom:5px;display:none;"><!-- style加上"display:none;"要控制 -->
              							<textarea class="{string:true}"  name="talk.talkcontent" id="talkcontent_<s:property value='aid'/>" style="width:600px;" rows="3"></textarea>
              						</div>
              						<div id="comment_submit_<s:property value='aid'/>" style="text-align:left;margin-top:5px;display:none;"><!-- style加上"display:none;"要控制 -->
              							<input type="button" class ="Btn_s_a_r" value="提交评论" onclick="talksave(<s:property value='aid'/>);" />
              						</div> 
              						
              					<s:hidden name="talk.id"/>
              	   				<s:hidden name="aid" value="%{aid}"/>
              	   				<s:hidden name="talk.talkman"/>
              	   				<s:hidden name="talk.talkname"/>
              	   				<s:hidden name="talk.status"/>
              	   				<s:hidden name="talk.talktime"/>
               				 </form>	
              						     				
              					</div>	 
              	 			</s:else>		
              	 	   		 			
			   		 		<!-- 答案评论 -->
			   		 		</td>
			   		 	</tr>
			   		 </table>
			   	</div>		   		 				
			 </s:iterator>
			 
			 <s:if test="answerlist==null || answerlist.size()==0">
			 <div class="other_main">该问题暂时还没有人回答！</div>
			 </s:if>
 	  </div>  
 	</s:iterator> 

  </div>
 </body>
</html> 
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
