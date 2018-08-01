<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>OA知道</title>   
<link rel="stylesheet" type="text/css" href="iwork_css/km/know_form.css"/>  
<link type="text/css" rel="stylesheet" href="iwork_css/km/know_index.css"/>
<link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
<link rel="stylesheet" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" href="iwork_css/jquerycss/icon.css">
<link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script> 
<script src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
<script src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
<script src="iwork_js/jqueryjs/ui/jquery.ui.tabs.js"></script>	
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
<script type="text/javascript" src="iwork_js/km/know.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style type="text/css">
	#tabs {height:100%;}
    .ui-tabs-panel { height:90%; overflow-y: scroll; }	
</style>
<script type="text/javascript">
var mainFormValidator;
$().ready(function() {
		mainFormValidator =  $("#editForm").validate({
		 });
		 mainFormValidator.resetForm();
	});
     //动态获得隐藏字段
    function getJsonParam(){
        var keyword = $.trim($('#keyword').val());     //关键字
	    var bcid = $('#bcid').val();                   //分类id
	    var pageNow = $('#pageNow').val();             //第几页
	    var pageSize = $('#pageSize').val();           //每页多少条记录
	    var jsonParam ={keyword:keyword,bcid:bcid,pageNow:pageNow,pageSize:pageSize};
	    return jsonParam;
    }
    //tab的ajaxOptions的设置
    function getAjaxOptions(){
        var jsonParam = getJsonParam();
        var ajaxOptions  =  
           {
			    data:jsonParam,
			     type: "POST",
				error: function( xhr, status, index, anchor ) {
					$( anchor.hash ).html("加载失败！");
				}
		   };
	   return ajaxOptions;	   
    }
    //
	$(function() {	    
	    var ajaxOptions = getAjaxOptions();
	  $( "#tabs" ).tabs({
			ajaxOptions: ajaxOptions
		});
	  $('a.basic').cluetip({
				cluetipClass: 'rounded', 
				dropShadow: false, 
				cursor: 'pointer',
				arrows: true,
				positionBy: 'mouse',
				showTitle: false
			});	
	  $('a.load-local').cluetip({
	  			local:true, 
	  			cursor: 'pointer',
	  			arrows: true,
	  			cluetipClass: 'rounded', 
	  			showTitle: false
	  		});		
	  var mb = myBrowser();
	  /* if ("IE" == mb||"QQBrowser" == mb||"Other" == mb) {
	      $("#keyword").css('margin-left','-50px');
	  } */
	});
	function myBrowser(){
	    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	    var isOpera = userAgent.indexOf("Opera") > -1;
	    if (isOpera) {
	        return "Opera"
	    } //判断是否Opera浏览器
	    else if (userAgent.indexOf("Firefox") > -1) {
	        return "FF";
	    } //判断是否Firefox浏览器
	    else if (userAgent.indexOf("Chrome") > -1){
	  		return "Chrome";
	 	}
	    else if (userAgent.indexOf("Safari") > -1) {
	        return "Safari";
	    } //判断是否Safari浏览器
	    else if (userAgent.indexOf("rv:") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
	        return "IE";
	    } //判断是否IE浏览器
	    else if (userAgent.indexOf("QQBrowser") > -1) {
	        return "QQBrowser";
	    } //判断是否QQ浏览器
	    else{
	    	return "Other";
	    }
	}
	//打开问题窗口
	function openQuestionWin(qid){  
	    var url = 'know_open_question.action?qid='+qid;  
	    window.open(url);
	}
	//删除问题
	function delQuestion(qid){  
	   lhgdialog.confirm("确定删除该提问？",function(){
 	  	 $.post('know_delete_question.action',{qid:qid},function(data){  
	    	 if(data=='ok'){
	    	      location.reload();
	    	   	//tabReload();
	    	 }
	     });	  
 	   },function(){});   
	}
	//刷新当前选中的tab
	function tabReload(){
	   var $tabs = $('#tabs').tabs();
	   $tabs.tabs( "option", "ajaxOptions", getAjaxOptions() );   //重新取隐藏字段
	   var selected = $tabs.tabs('option', 'selected');
	   $tabs.tabs('load', selected);                 //刷新
	   $('#pageNow').val($('#firstPage').val());       //首页
	   $('#pageSize').val($('#defaultPageSize').val());   //默认每页记录数
	   $tabs.tabs( "option", "ajaxOptions", getAjaxOptions() );   //还原为首页设置
	}
/*
**   提问
**   bcid:大分类ID
**   yg:请教达人的id
*/
function openAskQuestionDialog(bcid,yg){  
	var keyword = $.trim($('#keyword').val());  
	var url = 'know_ask_question.action?keyword='+encodeURI(encodeURI(keyword))+'&bcid='+bcid+'&yg='+yg;
	 art.dialog.open(url,{
			title : '我要提问',
			loadingText : '正在加载中,请稍后...',
			bgcolor : '#999',
			rang : true,
			width : 850,
			cache : false,
			lock : true,
			height : 600,
			iconTitle : false,
			extendDrag : true,
			autoSize : false
		});
}
//按分类分组
function searchbcid(bcid){
    $('#keyword').val('');
    $('#bcid').val(bcid);
    tabReload();
}
//查询关键字和分类
function searchbcidAndKeyword(bcid){
    $('#bcid').val(bcid);
    tabReload();
}
//搜索
function searchit(){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}
	tabReload();
}
//刷新
function refresh(){
	location.reload();
}
//导入
function impKnow(){
	var pageUrl = "zqb_imp_know_index.action";			 
	var target = "_blank";
	var win_width = window.screen.width;
	var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
	page.location = pageUrl;	
}

//导出
function expKnow(){
		var pageUrl = "zqb_exp_know.action";
		$("#ifrmMain").attr("action",pageUrl); 
		$("#ifrmMain").submit();
	}
</script>
  </head>
      <body class="easyui-layout">
	  <!-- north区-->
   <div region="north"  border="false" style="width:100%;overflow:no;background-color:#f5f5f5;text-align:center;">
   		<form id="editForm" name="editForm">
		<div style="width:817px; margin:0 auto;height:auto;" >
		  <div class="oasearch">
		  	<table>
		  		<tr>
		  			<td style="width:169px;"><img src="../../iwork_img/know/oasearch-new.jpg"></td>
		  			<td><input type="text" onkeydown="if(event.keyCode==13){return false;}" style="display: none;" /></td>
		  			<td style="width:290px;"><input type="text" name="text" id="keyword"  class="input_oa {string:true}" onkeydown="if(event.keyCode==13){return searchit();}"/></td>
		  			<td style="width:71px;"><input type="button" name="searchA" id="searchA" value="搜索"  class="btn_oasearch" onclick="searchit();"></td>
		  			<td style="width:71px;"><input type="button" name="allask" id="allask" class="btn_view" value="&nbsp;" onclick="refresh();"/></td>
		  			<td style="width:71px;"><input type="button" name="ask" class="btn_ask" id="ask" value="&nbsp;" onclick="openAskQuestionDialog('','');return false;"/></td>
		  			<s:if test="roleid==1">
			        	<td style="width:71px;"><input type="button" name="impknow" class="btn_imp" id="impknow" value="&nbsp;" onclick="impKnow();return false;"/></td>
			  			<td style="width:71px;"><input type="button" name="expknow" class="btn_exp" id="expknow" value="&nbsp;" onclick="expKnow();return false;"/></td>
			        </s:if>
		  			<s:else>
			        	<td style="width:71px;"><input type="button" name="impknow" class="btn_imp" style="background-image: none;background-color: #f5f5f5;cursor:default;" id="impknow" value="&nbsp;"/></td>
			  			<td style="width:71px;"><input type="button" name="expknow" class="btn_exp" style="background-image: none;background-color: #f5f5f5;cursor:default;" id="expknow" value="&nbsp;"/></td>
			        </s:else>
		  		</tr>
		  	</table>
		        <div style="clear:right;"></div>
		  </div>
		</div>
		</form>
   </div>	
	 <!-- north区结束 -->
     <!-- west区 -->	
   <div region="west" style="width:200px;padding-left:10px;background-color:#f5f5f5" border="false">
<form name='ifrmMain' id='ifrmMain'  method="post" ></form>
				<div class="div_bgi">
				  <div class="div_tit">
					  <span class="div_span_tit_left" style="cursor:pointer;bottom: 3px;" onclick="refresh();">问题分类</span>
				  </div>
				  <table width="95%" border="0" cellspacing="0" cellpadding="0">
					<!-- 分类开始 -->
					<s:if test="questionClasses==null || questionClasses.size()==0">
			          	<tr><td >暂无分类</td></tr>
			 		 </s:if>
					 
					 <tr>
					<td>
						<span style="cursor:pointer;" onClick="searchbcid(0);" >全部</span>
					</td>
					<td></td>
					</tr> 
					 <s:iterator value="questionClasses" status="status">
					 <s:if test="experts==null || experts.size()==0">
					<tr>
					<td>
						<span style="cursor:pointer;" onClick="searchbcid(<s:property value='id'/>);" ><s:property value='cname'/></span> (<s:property value='qcount'/>)
					</td>
					<td></td>
					</tr> 
					</s:if>
                    <s:else>
					<s:iterator value="experts" status="sta">
					<s:if test="#sta.first">
					<tr>
					<td>
						<span style="cursor:pointer;" onClick="searchbcid(<s:property value='id'/>);" ><s:property value='cname'/></span> (<s:property value='qcount'/>)
					</td>
					<td >
						<a class="basic" rel="user_tip.action?userid=<s:property value='uid'/>" href="user_tip.action?userid=<s:property value='uid'/>" onclick="openAskQuestionDialog(<s:property value='id'/>,'<s:property value='uid'/>');return false;"><s:property value='uname'/></a>
					</td>
					</tr>
					</s:if>
					<s:else>
					<tr>
					<td></td>
					<td>
						<a class="basic" rel="user_tip.action?userid=<s:property value='uid'/>" href="user_tip.action?userid=<s:property value='uid'/>" onclick="openAskQuestionDialog(<s:property value='id'/>,'<s:property value='uid'/>');return false;"><s:property value='uname'/></a>
					</td>
					</tr>
					</s:else>
					</s:iterator>
					</s:else>
					
					</s:iterator>
					<!-- 分类结束 -->
				  </table>
				</div>
				<div class="div_bgi">
				  <div class="div_tit">
					   <span class="div_span_tit_left">积分榜</span>
					   <a class="load-local" href="#scorerules" rel="#scorerules" style="font-weight:normal; color:#056ea4;">(规则)</a>
				  </div>
				  <table width="95%" border="0" cellspacing="0" cellpadding="0">
					  <!-- 积分开始 -->
					    <tr>
					    <td style="padding-left:5px;padding-top:5px;" colspan="2" >我的积分</td>
					    <td align="right" ><s:property value='myscore'/>&nbsp;</td>
					    </tr>
					    <tr>
					    <td colspan="3" ><div class="lineClean"></div></td>
					    </tr>
					    <s:iterator value="top10ScoreUserList" status="stat">
					          <tr>
					          <td style="padding-left:5px;line-height:18px;" ><s:property value='#stat.count'/></td>
					          <td><a class="basic"  rel="user_tip.action?userid=<s:property value='suid'/>" href="user_tip.action?userid=<s:property value='suid'/>" onclick="return false;"><s:property value='suname'/></a></td>
					          <td align="right" ><s:property value='score'/>&nbsp;</td>
					          </tr>  
					    </s:iterator>
					  <!-- 积分结束 -->
				  </table>
				</div>
			
	</div>
<!-- west区结束 -->			
<!-- center区 -->			
	 <div region="center" style="width:100%;padding:0px 10px 0px 10px;overflow:hidden;background-color:#f5f5f5;" border="false">		
		<div id="oa_main" class="oa_main">
		      <div id="tabs" style="height: 330px;">
				<ul>
					<li><a href="know_question_tabContent.action?type=1">已解决问题</a></li>
					<li><a href="know_question_tabContent.action?type=0">待解决问题</a></li>
					<li><a href="know_question_tabContent.action?type=2">我的提问</a></li>
					<li><a href="know_question_tabContent.action?type=3">我的回答</a></li>
					<li><a href="know_question_tabContent.action?type=4">问我的问题</a></li>
					<li><a href="know_question_tabContent.action?type=5">全部</a></li>
				</ul>
			 </div>
		</div>


<s:hidden id="bcid" name="bcid" theme="simple"></s:hidden>
<s:hidden id="pageNow" name="pageNow"  theme="simple"></s:hidden>
<s:hidden id="pageSize" name="pageSize"  theme="simple"></s:hidden>
<s:hidden id="firstPage" name="firstPage"  value="%{pageNow}" theme="simple"></s:hidden>
<s:hidden id="defaultPageSize" name="defaultPageSize" value="%{pageSize}" theme="simple"></s:hidden>

	<!-- 规则开始 -->
<div style="display:none;">
	<div id="scorerules">
	<table border=0>
  <tr>
    <td>
	        1、每提问一次，系统奖励5分
	</td>
  </tr>
  <tr>
    <td>
	        2、每回答一次，系统奖励2分
	</td>
  </tr>
  <tr>
	<td>
	  	  	3、每个问题的第一个回答人，系统奖励5分
	</td>
  </tr>
  <tr>
	<td>
	   		4、被选为最佳答案的回答人，系统奖励10分
	</td>
  </tr>
  <tr>
    <td>
	   		5、被选为最佳答案的回答人，如果提问人设置了奖励积分，则该回答人同时获得奖励积分
	</td>
  </tr>
  <tr>
    <td>
	   		6、提问人与最佳答案回答人是同一人时，该用户不能获得系统奖励积分
	</td>
  </tr>
  <tr>
    <td>
	  	    7、回答被“赞”一次，系统奖励回答人2分
	</td>
  </tr>
  <tr>
    <td>
	  	     8、每评论一次，系统奖励评论人2分
	</td>
  </tr>
</table>
</div>
</div>
<!-- 规则结束 -->
</div>
<!-- center区结束 -->
<div region="south" style="background-color:#f5f5f5;height:45px;padding-left:212px;padding-right:13px;width:100%;" border="false">
 <!-- 分页工具 -->
	<div id="page" style="background:#efefef;border:1px solid #ccc;">
	</div>
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