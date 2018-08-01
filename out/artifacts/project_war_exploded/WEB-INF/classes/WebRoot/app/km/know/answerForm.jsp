<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>   
    <title>编辑回答</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/km/know_form.css"/>   
    <link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.position.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="iwork_js/km/know.js"></script>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    	<script type="text/javascript" src="iwork_js/engine/iformpage.js"></script>
    <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
    <style>
	.ui-autocomplete-loading { background: white url('/iwork_css/jquerycss/ui/base/images/ui-anim_basic_16x16.gif') right center no-repeat; }
	.ui-autocomplete { 
		max-height: 230px; 
		overflow-y: auto; 
		overflow-x: hidden; 
		padding-right: 5px; 
		} 
	* html .ui-autocomplete { 
		height: 230px; 
		} 
	</style>
	 <script type="text/javascript">
var api = art.dialog.open.api, W = api.opener;
var mainFormValidator;
$().ready(function() {
mainFormValidator =  $("#editForm").validate({
 });
 mainFormValidator.resetForm();
});
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
        var aid = $('#editForm_answer_id').val();
        $.post('know_upload_answer_synchron.action',{oldUUIDs:oldUUIDs,aid:aid},function(data){
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
    	var valid = mainFormValidator.form(); //执行校验操作
		if(!valid){
			return false;
		}
    	var ACONTENT = $.trim($('#ACONTENT').val());
        $('#ACONTENT').val(ACONTENT); 
              
		var INVITEDMANE  = $.trim($('#INVITEDMANE').val());
		if(typeof(INVITEDMANE)=="undefined"||INVITEDMANE==null){
			INVITEDMANE='';
		}
		if(INVITEDMANE=='多个被邀请回答人请用逗号分隔'){
			INVITEDMANE = '';  
		}
		if(INVITEDMANE!=''&&INVITEDMANE.length>0){
			if(INVITEDMANE.charAt(INVITEDMANE.length-1)==','){
				INVITEDMANE=INVITEDMANE.substring(0,INVITEDMANE.length-1);
			}
        }//去掉最后一个逗号，如果有的话  
        $('#INVITEDMANE').val(INVITEDMANE);
        
        var oldUUIDs =  $('#oldHidId').val(); 
   		var newUUIDs = $('#parentHidId').val();
   		var totalUUIDs = insertUUID(oldUUIDs,newUUIDs);
   		$('#oldHidId').val(totalUUIDs);
   		
        if(ACONTENT==""){
              art.dialog.tips("请输入回答内容！",2);
               $('#ACONTENT').focus();
              return ;
          }
       $.post('know_check_user_is_exist.action',{yg:INVITEDMANE},function(data){  
                            if(data.length>0){
                                var tip='输入错误！<br/>'+data+ '请点击“地址簿”选择正确的员工。'
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
       /*多选地址薄*/
    function multi_book(isOrg, isRole, isGroup, parentDept, currentDept, startDept, targetUserId, targetUserName, targetDeptId, targetDeptName, defaultField) {
		var code = document.getElementById(defaultField).value;	
		if(code=="多个被邀请回答人请用逗号分隔"){
		     code="";
		}  
		var url = "multiAddressBookAction!index.action?code=" + encodeURI(encodeURI(code)) + "&parentDept=" + parentDept + "&currentDept=" + currentDept + "&startDept=" + startDept + "&isOrg=" + isOrg + "&isRole=" + isRole + "&isGroup=" + isGroup;
		 art.dialog.open(url,{
				 id:"radioBookDialog",
			title: '多选地址簿',
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width: 650,
					height: 550
				 });
	}
	/*邀请回答人，联想*/
 $(function() {
		function split( val ) {
			return val.split(",");
		}
		function extractLast( term ) {
			return split( term ).pop();
		}

		$( "#INVITEDMANE" )
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
			/*全局*/
       changeCss();
           /*全局*/
	});
	
   function changeCss(){
   		var INVITEDMANE = $('#INVITEDMANE').val(); 
   		if(INVITEDMANE=='多个被邀请回答人请用逗号分隔'){
   		    $('#INVITEDMANE').css('color','#D1D1D1');  
   		}else{
   		    $('#INVITEDMANE').css('color','#0000ff');  
   		}
   }
    //文件上传
   /* function showUploadifyPage(){ 
        var url = 'know_showUploadifyPage.action?parentHidId=parentHidId&parentFileDivId=parentFileDivId&multi=true';
        art.dialog.open(url,{
				 id:"radioBookDialog",
					title: '上传附件',
					background: '#999', // 背景色
				    opacity: 0.87,	// 透明度
				    width: 500,
					height: 400
				 });
		
		//$.dialog.data("temObj",obj);
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
  <s:form  name="editForm" id="editForm" action="know_save_answer.action" method="post" theme="simple">
     <table width=98% border=0 cellspacing=0 cellpadding=0>
  		<tr>
  			<td colspan=2 height=8 ></td>
  		</tr>
  		<tr>
  			<td width=16% align=right valign=top >邀请回答人：</td>
  			<td align=left>
  				<table cellspacing="0" cellpadding="0">
  				  <tr valign="middle">
  					<td>
  						<s:textfield id="INVITEDMANE" name="answer.invitedman"  onkeyup="getuserbycode(this);" cssClass="actionsoftInput {string:true}" cssStyle="color:#D1D1D1;width:360px;" onblur="dropFocus(this,'多个被邀请回答人请用逗号分隔');" onfocus="getFocus(this,'多个被邀请回答人请用逗号分隔');"/>
  					</td>
  					<td>&nbsp;&nbsp;</td>
  					<td>
  					    <input type=button value='地址簿' name='ANSWERBODY_Btn' class='Btn_s_a' onclick="multi_book('false','false','false','false','false','','','','','','INVITEDMANE');" border='0'>
 				    </td>
 				 </tr>
 				</table>
 			</td>
 		</tr>		
		<tr>
			<td colspan=2 height=12></td>
		</tr>
 		<tr>
 			<td width=16% align=right valign=top >回答内容：</td>
  			<td align=left>
  			    <s:textarea  name='answer.acontent' id='ACONTENT' cssClass ='actionsoftInput {string:true}' cssStyle="width:460px;" rows='6'/>
  				<img src="/iwork_img/notNull.gif"/>
           </td>
        </tr>
        
<tr><td colspan=2 height=12 ></td></tr>
<tr><td align=right >相关附件：</td>
    <td align=left valign=bottom>
      <div id='parentFileDivId'>
	      <div><s:hidden id='oldHidId' name='answer.fileuuids'/><s:hidden id='parentHidId' name='parentHidId' value=''/></div>
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
					uploadifyDialog('oldHidId','oldHidId','parentFileDivId','','true','','');
				}
			</script>
	      <s:iterator value='files'>
	      <div  id="<s:property value='fileId'/>" style="background-color: #F5F5F5;border-bottom: 1px solid #E5E5E5;font: 11px Verdana, Geneva, sans-serif;padding: 5px;width: 200px">
			<div style="align:right;float: right;">
	   			<a href="javascript:uploadifyReomve2('oldHidId','<s:property value='fileId'/>','<s:property value='fileId'/>');"><img src="/iwork_img/del3.gif"/></a>
			</div>
			<span><a href="know_uploadifyDownload.action?fileUUID=<s:property value='fileId'/>" target="_blank"><img src="/iwork_img/attach.png"/><s:property value='fileSrcName'/></a></span>
		 </div>
	      </s:iterator>	   
     </div>
    </td>
</tr>

  		<tr>
  			<td colspan=2 height=16></td>
  		</tr>
  		<tr>
  			<td colspan=2 align=center >
  				<input type="button" name="com_con" id="com_con" value="提  交" class ="Btn_s_a_r" border="0" onClick="save()" />&nbsp;&nbsp;&nbsp;&nbsp;
  				<input type="button" name="dorp_con" id="dorp_con" value="取  消" class ="Btn_s_a" border="0" onClick="cancle();" />
  			</td>
  		</tr>
  		<tr>
  			<td colspan=2 id="onready" height=16 ></td>
  		</tr>
  </table>
  
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
