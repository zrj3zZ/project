<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>编辑回答</title>
    <link type="text/css" rel="stylesheet" href="iwork_css/formstyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css"/>
    <link type="text/css" rel="stylesheet" href="iwork_css/iwork/base.css"/>
    <link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
    <link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script> 
	<script src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.position.js"></script>
	<script src="iwork_js/jqueryjs/ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="iwork_js/iwork/oaknow.js"></script>
    <script type="text/javascript" src="iwork_js/commons.js"></script>
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
    var api = frameElement.api, W = api.opener; 
    var mainFormValidator;
    $().ready(function() {
    mainFormValidator =  $("#editForm").validate({
     });
     mainFormValidator.resetForm();
    });
    /*关闭窗口*/
    function closeDialog(){
         api.close();
    }
    errorFunc=function(){
           W.lhgdialog.tips("提交失败！",2);
      }
    successFunc=function(responseText, statusText, xhr, $form){
           if(responseText=="ok"){
              W.lhgdialog.tips("提交成功！",2);
              W.location.reload();
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
        $('#INVITEDMANE').val(INVITEDMANE);
        if(ACONTENT==""){
              W.lhgdialog.tips("请输入回答内容！",2);
               $('#ACONTENT').focus();
              return ;
          }
        if(INVITEDMANE=='多个被邀请回答人请用空格分隔'){
                       $('#INVITEDMANE').val('');
                       INVITEDMANE = '';  
                  }
				  $.post('know_check_user_is_exist.action',{yg:INVITEDMANE},function(data){  
                            if(data.length>0){
                                var tip='输入错误！<br/>'+data+ '请点击“地址簿”选择正确的员工。'
                                W.lhgdialog.tips(tip,2);
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
		if(code=="多个被邀请回答人请用空格分隔"){
		     code="";
		}  
		var url = "multiAddressBookAction!index.action?code=" + code + "&parentDept=" + parentDept + "&currentDept=" + currentDept + "&startDept=" + startDept + "&isOrg=" + isOrg + "&isRole=" + isRole + "&isGroup=" + isGroup;
		
		var obj = new Object();
		obj.parentDept = parentDept;
		obj.currentDept = currentDept;
		obj.startDept = startDept;
		obj.defaultField = defaultField;
		obj.targetUserId = targetUserId;
		obj.targetUserName = targetUserName;
		obj.targetDeptId = targetDeptId;
		obj.targetDeptName = targetDeptName;
		obj.win = window;
		W.$.dialog({
			id:"radioBookDialog",
			title: '多选地址簿',
			content: 'url:'+url,
			pading: 0,
			lock: true,
			width: 650,
			height: 550,
			top:'25%',
			left:'5%'
		});
		$.dialog.data("paramObj",obj);
	}
	/*邀请回答人，联想*/
 $(function() {
		function split( val ) {
			return val.split(" ");
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
					this.value = terms.join( " " );
					return false;
				}
			});
	});
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
  						<s:textfield id="INVITEDMANE" name="answer.invitedman"  onkeyup="getuserbycode(this);" cssClass="actionsoftInput {string:true}" cssStyle="color:#D1D1D1;width:360px;" onblur="dropFocus(this,'多个被邀请回答人请用空格分隔');" onfocus="getFocus(this,'多个被邀请回答人请用空格分隔');"/>
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
  		<tr>
  			<td colspan=2 height=16></td>
  		</tr>
  		<tr>
  			<td colspan=2 align=center >
  				<input type="button" name="com_con" id="com_con" value="提  交" class ="Btn_s_a_r" border="0" onClick="save()" />&nbsp;&nbsp;&nbsp;&nbsp;
  				<input type="button" name="dorp_con" id="dorp_con" value="取  消" class ="Btn_s_a" border="0" onClick="closeDialog();" />
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