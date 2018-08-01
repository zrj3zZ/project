<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>提问窗口</title>
    <link type="text/css" rel="stylesheet" href="iwork_css/formstyle.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css"/>
    <link type="text/css" rel="stylesheet" href="iwork_css/iwork/base.css"/>
    <link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
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
        var qcontent = $.trim($('#editForm_question_qcontent').val());
        $('#editForm_question_qcontent').val(qcontent);
        var qbigc = $('#editForm_question_iworkKnowClasses_id').val();
        var score = $.trim($('#editForm_question_score').val());
         $('#editForm_question_score').val(score);
        var answerbody = $.trim($('#editForm_question_answerbody').val());
        $('#editForm_question_answerbody').val(answerbody);
         var qtags = $.trim($('#editForm_question_qtags').val());
        $('#editForm_question_qtags').val(qtags); 
        
        var bool = $('#niming').attr('checked');
        if(bool){
             $('#editForm_question_shownametype').val(0);
        }else{
             $('#editForm_question_shownametype').val(1);
        }
        
        if(qcontent==""){
              W.lhgdialog.tips("请输入问题内容！",2);
               $('#editForm_question_qcontent').focus();
              return ;
          }
        if(qbigc==0){
              W.lhgdialog.tips("请选择问题分类！",2);
               $('#editForm_question_iworkKnowClasses_id').focus();
              return ;
          }  
        if(!/^\d+$/.test(score)){
             W.lhgdialog.tips("奖励积分必须为非负整数！",2);
              $('#editForm_question_score').focus();
             return ;
        }
        $.post('know_check_score_is_enough.action',{score:score},function(data){    
              if(data.length>0){
                  W.lhgdialog.tips(data,2);
              	  $('#editForm_question_score').focus();
            	  return ;
              }else{ 
                  if(qtags=='多个标签请用空格分隔'){
                       $('#editForm_question_qtags').val('');
                       qtags = '';
                  }
                  if(answerbody=='多个被邀请回答人请用空格分隔'){
                       $('#editForm_question_answerbody').val('');
                       answerbody = '';  
                  }
				  $.post('know_check_user_is_exist.action',{yg:answerbody},function(data){  
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
        }); 
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
					this.value = terms.join( " " );
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
   		if(answerbody=='多个被邀请回答人请用空格分隔'){
   		    $('#editForm_question_answerbody').css('color','#D1D1D1');  
   		}else{
   		    $('#editForm_question_answerbody').css('color','blue');  
   		}
   }	  
    </script>
  </head> 
  <body>
  <div style="overflow:auto;width:599px; margin:0px auto; padding-top:10px;" id="ask_con">
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
         <s:textfield name="question.answerbody"  onkeyup="getuserbycode(this);" cssClass="actionsoftInput" cssStyle="width:360px;" onblur="dropFocus(this,'多个被邀请回答人请用空格分隔');" onfocus="getFocus(this,'多个被邀请回答人请用空格分隔');"/>
         </td>
         <td >&nbsp;&nbsp;</td>
         <td ><input type=button value='地址簿' name='ANSWERBODY_Btn' class='Btn_s_a' onclick="multi_book('false','false','false','false','false','','','','','','editForm_question_answerbody');" border='0'>
         </td>
         </tr>
      </table>
   </td>
</tr>
<tr><td colspan=4 height=12 ></td></tr>
<tr><td align=right >提问内容：</td>
    <td colspan=3 align=left valign=bottom>
      <s:textarea  name='question.qcontent' cssClass ='actionsoftInput {string:true,required:true}' cssStyle="width:460px;" rows='6'/>
      <img src="/iwork_img/notNull.gif"/>
    </td>
</tr>
<tr><td colspan=4 height=12 ></td></tr>
<tr><td align=right>问题分类：</td>
    <td align=left >
        <s:select label="问题分类：" name="question.iworkKnowClasses.id"  list="%{classList}" listKey="id" listValue="cname" headerKey="0" headerValue="-请选择-"/>   
       <img src="/iwork_img/notNull.gif"/>
    </td>
    <td align=left >自定义标签：
         <s:textfield name="question.qtags" cssClass ='actionsoftInput' cssStyle="color:#D1D1D1;width:180px;height:18px;"  onblur="dropFocus(this,'多个标签请用空格分隔');" onfocus="getFocus(this,'多个标签请用空格分隔');"/>
    </td>
    <td align=left >
        奖励<s:textfield name="question.score"  cssClass ="actionsoftInput" cssStyle="height:18px;width:20px;"/>分
    </td>
</tr>
<tr><td colspan=4 height=16 ></td></tr>
<tr><td colspan=4 align=center ><input name="niming" id="niming"  type="checkbox" />&nbsp;匿名&nbsp;&nbsp;
    <input type="button" name="com_con" id="com_con" value="提  交" class ="Btn_s_a_r" border="0" onClick="save();" />&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" name="dorp_con" id="dorp_con" value="取  消" class ="Btn_s_a" border="0" onClick="closeDialog();" />
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
