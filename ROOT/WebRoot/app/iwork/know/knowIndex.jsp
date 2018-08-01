<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>OA知道</title>   
<link rel="stylesheet" type="text/css" href="iwork_css/iwork/oaknow.css"/>
<link type="text/css" rel="stylesheet" href="iwork_css/iwork/base.css"/>
<link type="text/css" rel="stylesheet" href="iwork_css/iwork/common.css"/>
<link rel="stylesheet" href="iwork_css/jquerycss/ui/base/jquery.ui.all.css">
<link rel="stylesheet" href="iwork_css/jquerycss/default/easyui.css">
<link rel="stylesheet" href="iwork_css/jquerycss/icon.css">
<link href="iwork_css/jquerycss/cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css"/>
	 
<script type="text/javascript" src="iwork_js/commons.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script> 
<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=false&skin=default"></script> 
<script src="iwork_js/jqueryjs/ui/jquery.ui.core.js"></script>
<script src="iwork_js/jqueryjs/ui/jquery.ui.widget.js"></script>
<script src="iwork_js/jqueryjs/ui/jquery.ui.tabs.js"></script>	
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.cluetip.min.js" ></script>
<script type="text/javascript" src="iwork_js/iwork/oaknow.js"></script>
<link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style>
	.news table td {
	    padding: 0px;
	}
	
	.tr_bg{
	    background-color:#f5f5f5;
	}
	#tabs {height: 500px; background-color:#f5f5f5;} 
    .ui-tabs .ui-tabs-panel{background-color:#f5f5f5; }
    .ui-tabs-panel { height: 460px; overflow-y: scroll; }	
</style>
<script type="text/javascript">
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
	});
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
					id:'dg',
					cover:true,
					title:'我要提问',
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
	tabReload();
}
//刷新
function refresh(){
	location.reload();
}
</script>
  </head>
  
  <body>
      <body style="background-color:#f5f5f5">
	
	  <!-- 搜索开始-->
		<div style="width: 700px; margin:0 auto; min-width:700px;" >
		  <div class="oasearch">
			<label for="text"></label>
			<input type="text"  name="text" id="keyword"  class="input_oa" onkeydown="if(event.keyCode==13){return searchit();}"/>
	        <input type="text" onkeydown="if(event.keyCode==13){return false;}" style="display: none;" />
	        <input type="button" name="searchA" id="searchA" value="&nbsp;"  class="btn_oasearch" onclick="searchit();"/>
	        <input type="button" name="allask" id="allask"  class="btn_view" value="&nbsp;" onclick="refresh();" style="margin-left: 4px;" />
	        <input type="button" name="ask"   class="btn_ask" id="ask" value="&nbsp;" onclick="openAskQuestionDialog('','');return false;"/>
		  </div>
		</div>
	 <!-- 搜索结束 -->
	
		<div style="width:100%">
		<div style="float:left;margin-left: 10px;" id="let_list">
				<div class="div_bgi">
				  <div class="div_tit">
					  <span class="div_span_tit_left" style="cursor:pointer;bottom: 3px;" onclick="refresh();">问题分类</span>
				  </div>
				  <table width="95%" border="0" cellspacing="0" cellpadding="0">
					<!-- 分类开始 -->
					<s:if test="questionClasses==null || questionClasses.size()==0">
			          	<tr><td >暂无分类</td></tr>
			 		 </s:if>
					 
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
					          <td style="padding-left:5px;line-height:18px;" ><s:property value='#stat.index'/></td>
					          <td><a class="jTip" id='tip1' rel="know_show_userinfo.action?userid=<s:property value='suid'/>" href="know_show_userinfo.action?userid=<s:property value='suid'/>" onclick="return false;"><s:property value='suname'/></a></td>
					          <td align="right" ><s:property value='score'/>&nbsp;</td>
					          </tr>  
					    </s:iterator>
					  <!-- 积分结束 -->
				  </table>
				</div>
			</div>
		<div id="oa_main" class="oa_main"  style="overflow: auto;padding-top: 3px;">
		      <div id="tabs">
				<ul>
					<li><a href="know_question_tabContent.action?type=0">待解决问题</a></li>
					<li><a href="know_question_tabContent.action?type=1">已解决问题</a></li>
					<li><a href="know_question_tabContent.action?type=2">我的提问</a></li>
					<li><a href="know_question_tabContent.action?type=3">我的回答</a></li>
					<li><a href="know_question_tabContent.action?type=4">问我的问题</a></li>
				</ul>
			</div>
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
</body>
</html>
