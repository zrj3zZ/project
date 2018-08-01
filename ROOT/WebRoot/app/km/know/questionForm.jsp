<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html> 
  <head>   
    <title>提问窗口</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/km/know_form.css"/>  
    <link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/> 
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.position.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="iwork_js/km/know.js"></script>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
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
	</style>
    <script type="text/javascript">
    var api = art.dialog.open.api, W = api.opener;
    /*关闭窗口*/
    function cancle(){
         var newUUIDs = $('#parentHidId').val();
         if(newUUIDs!=''){
             var newUUIDArray = newUUIDs.split(',');
             for(var i=0;i<newUUIDArray.length;i++){
                 if(newUUIDArray[i]!=''){
                      uploadifyReomve2('parentHidId',newUUIDArray[i],newUUIDArray[i]);
                 }
             }
         }//点击取消时，删除新上传的文件
        var oldUUIDs =  $('#oldHidId').val();   
        var qid = $('#editForm_question_id').val();
        $.post('know_upload_synchron.action',{oldUUIDs:oldUUIDs,qid:qid},function(data){
               if(data=='ok'){
                   
               }
        }); //将oldUUIDs同步到question的fileuuids字段  
        setTimeout('closeDialog();',1000);   
    }
   function closeDialog(){
   		api.close(); 
   } 
    errorFunc=function(){
           art.dialog.tips("提交失败！",2);
      }
    successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              art.dialog.tips("提交成功！",2);
              setTimeout('closeDialog();',1000);
           }
      }
      
    /*保存*/
    function save(){
        var qcontent = $.trim($('#editForm_question_qcontent').val());
        $('#editForm_question_qcontent').val(qcontent);
        var qbigc = $('#editForm_question_iworkKnowClasses_id').val();
        var score = $.trim($('#editForm_question_score').val());
         $('#editForm_question_score').val(score);
         
        var answerbody = $.trim($('#editForm_question_answerbody').val());
		if(answerbody=='多个被邀请回答人请用逗号分隔'){
             answerbody = '';                                                
         }    
        if(answerbody.length>0){ 
        	if(answerbody.charAt(answerbody.length-1)==','){
        		answerbody=answerbody.substring(0,answerbody.length-1); 
        	}
        }//去掉最后一个逗号，如果有的话     
        $('#editForm_question_answerbody').val(answerbody);
        
         var qtags = $.trim($('#editForm_question_qtags').val());
         if(qtags=='多个标签请用逗号分隔'){
              qtags = '';
         }
        $('#editForm_question_qtags').val(qtags); 
        
        var bool = $('#niming').attr('checked');
        if(bool){
             $('#editForm_question_shownametype').val(0);
        }else{
             $('#editForm_question_shownametype').val(1);
        }
        
        var oldUUIDs =  $('#oldHidId').val(); 
   		var newUUIDs = $('#parentHidId').val();
   		var totalUUIDs = insertUUID(oldUUIDs,newUUIDs);
   		
        if(qcontent==""){
              art.dialog.tips("请输入问题内容！",2);
               $('#editForm_question_qcontent').focus();
              return ;
          }
        if(qbigc==0){
              art.dialog.tips("请选择问题分类！",2);
               $('#editForm_question_iworkKnowClasses_id').focus();
              return ;
          }  
        if(!/^\d+$/.test(score)){
             art.dialog.tips("奖励积分必须为非负整数！",2);
              $('#editForm_question_score').focus();
             return ;
        }                       
        $.post('know_check_score_is_enough.action',{score:score},function(data){    
              if(data.length>0){
                  art.dialog.tips(data,2);
              	  $('#editForm_question_score').focus();
            	  return ;
              }else{ 
                  
				  $.post('know_check_user_is_exist.action',{yg:answerbody},function(data){  
                            if(data.length>0){
                                var tip='输入错误！<br/>'+data+ '请点击“地址簿”选择正确的员工。'
                                art.dialog.tips(tip,2);
                                return ;
                            }
                            else{
                                 $('#oldHidId').val(totalUUIDs);                                
        						 var options = {
									error:errorFunc,
									success:successFunc 
			   					};  
								$('#editForm').ajaxSubmit(options);  
							 	
                            }
                       });  //检查邀请回答人是否存在
                 
              }       
        }); 
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
					    width: 500,
						height: 510,
						close:function(){
						}
					 });

	//	$.dialog.data("paramObj",obj);
	}
	/*邀请回答人，联想*/
 $(function() {
		function split( val ) {
			return val.split(",");  //间隔符
		}
		function extractLast( term ) {
			return split(term).pop();
		}

		$( "#editForm_question_answerbody" )
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
					this.value = terms.join( "," );      //间隔符
					return false;
				}
			});
			
			/*全局*/        
       isShowName(); 
       changeCss();
           /*全局*/
	});
 
	function isShowName(){
	    var shownametype = $('#editForm_question_shownametype').val();
        if(shownametype==0){
            $('#niming').attr('checked',true);
        }else{
            $('#niming').attr('checked',false);
        }	
	}
   function changeCss(){
   		var answerbody = $('#editForm_question_answerbody').val(); 
   		if(answerbody=='多个被邀请回答人请用逗号分隔'){
   		    $('#editForm_question_answerbody').css('color','#D1D1D1');  
   		}else{
   		    $('#editForm_question_answerbody').css('color','#0000ff');  
   		}
   }                    
   //文件上传
   /* function showUploadifyPage(){ 
        var pageUrl = 'know_showUploadifyPage.action?parentHidId=parentHidId&parentFileDivId=parentFileDivId&multi=true';
        art.dialog.open(pageUrl,{
			    	id:"knowDlg",
					title: '上传附件',
					lock:true,
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width:500,
				    height:350
				 });
	//	$.dialog.data("temObj",obj);
	}  */
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
        var oldUUIDs =  $('#oldHidId').val(); 
   		var newUUIDs = $('#parentHidId').val();
   		var totalUUIDs = insertUUID(oldUUIDs,newUUIDs);
   		if(length2(totalUUIDs)>500){
   		    return false;
   		}
   		return true;
   } 	 	  
    </script>
  </head> 
  <body>
  <div style="overflow:auto;width:580px; margin:0px auto; padding-top:10px;" id="ask_con">
  <s:form  name="editForm" id="editForm" action="know_save_question.action" theme="simple">
     <table width=98% align=center border=0 cellspacing=0 cellpadding=0>
     <col width="16%"/>
     <col width="18%"/>
     <col width="50%"/>
     <col width="16%"/>
<tr><td align=right>邀请回答人：</td>
    <td colspan=3 align=left >
       <table cellspacing="0" cellpadding="0">
         <tr valign="middle">
         <td >
         <s:textfield name="question.answerbody"  onkeyup="getuserbycode(this);" cssClass="actionsoftInput" cssStyle="width:360px;" onblur="dropFocus(this,'多个被邀请回答人请用逗号分隔');" onfocus="getFocus(this,'多个被邀请回答人请用逗号分隔');"/>
         </td>
         <td >&nbsp;&nbsp;</td>
         <td ><input type=button value='地址簿' name='ANSWERBODY_Btn' class='Btn_s_a' onclick="multi_book('editForm_question_answerbody');" border='0'>
         </td>
         </tr>
      </table>
   </td>
</tr>
<tr><td colspan=4 height=12 ></td></tr>
			<tr>
				<td align="right">提问内容：</td>
				<td colspan="3" align="left" valign="bottom"><s:textarea name='question.qcontent' cssClass='actionsoftInput {string:true}' cssStyle="width:460px;" rows='6' /></td>
			</tr>
			<tr>
				<td colspan="4" height="12"></td>
			</tr>
			<tr>
				<td align="right">问题分类：</td>
				<td align="left">
					<s:select label="问题分类：" name="question.iworkKnowClasses.id" list="%{classList}" listKey="id" listValue="cname" headerKey="0" headerValue="-请选择-" />
				</td>
				<td align="left" valign="bottom">
					自定义标签：
					<s:textfield name="question.qtags" cssClass='actionsoftInput {string:true}' cssStyle="color:#D1D1D1;width:140px;height:18px;" onblur="dropFocus(this,'多个标签请用逗号分隔');" onfocus="getFocus(this,'多个标签请用逗号分隔');"/>
				</td>
				<td align="right">
					奖励
					<s:textfield name="question.score" cssClass="actionsoftInput {string:true}" cssStyle="height:18px;width:20px;" />分
				</td>
			</tr>

<tr><td colspan=4 height=16 ></td></tr>
		<tr>
				<td align="right">相关附件：</td>
				<td align="left" valign="bottom">
					<div id='parentFileDivId'>
						<div>
							<s:hidden id='oldHidId' name='question.fileuuids' />
							<s:hidden id='parentHidId' name='parentHidId' value='' />
						</div>
						<div>
							<input type="button" value='上传附件' onclick='showUploadifyPageXGZL()' border='0'>
						</div>
						<script>
							function showUploadifyPageXGZL(){
								mainFormAlertFlag=false;
								saveSubReportFlag=false;
								uploadifyDialog('parentHidId','parentHidId','parentFileDivId','','true','','');
							}
						</script>
						<s:iterator value='files'>
							<div id="<s:property value='fileId'/>"
								style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px">
								<div style="align:right;float: right;">
									<a
										href="javascript:uploadifyReomve('oldHidId','<s:property value='fileId'/>','<s:property value='fileId'/>');"><img
										src="/iwork_img/del3.gif" /></a>
								</div>
								<span><a
									href="know_uploadifyDownload.action?fileUUID=<s:property value='fileId'/>"
									target="_blank"><img src="/iwork_img/attach.png" />
									<s:property value='fileSrcName' /></a></span>
							</div>
						</s:iterator>
					</div>
				</td>
				<td colspan="2" height="12"></td>
			</tr>

<tr><td colspan=4 height=16 ></td></tr>
<tr><td colspan=4 align=center ><input name="niming" id="niming"  type="checkbox" />&nbsp;匿名&nbsp;&nbsp;
    <input type="button" name="com_con" id="com_con" value="提  交" class ="Btn_s_a_r" border="0" onClick="save();" />&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="dorp_con" id="dorp_con" value="取  消" class ="Btn_s_a" border="0" onClick="cancle();" />
    </td>
</tr>
<tr><td colspan=4 id="onready" height=16 ></td></tr>
   </table>
   
   <s:hidden  name="question.shownametype"/>
   
   <s:hidden  name="question.id"/>
   <s:hidden  name="question.quid"/>
   <s:hidden  name="question.qattachment"/>
   <s:hidden  name="question.quname"/>
   <s:hidden  name="question.qaddcontent"/>
   <s:hidden  name="question.qaddattachment"/>
   <s:hidden  name="question.qbegintime"/>
   <s:hidden  name="question.qsolvetime"/>
   <s:hidden  name="question.qtype"/>
   <s:hidden  name="question.qletc"/>
   <s:hidden  name="question.clickcount"/>
   
</s:form>   
</div>   
  </body>
</html>
