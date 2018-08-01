<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <title>信息资讯</title>
    <meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

<meta name="description" content="Write an awesome description for your new site here. You can edit this line in _config.yml. It will appear in your document head meta (for Google search results) and in your feed.xml site description.
">
<link rel="stylesheet" href="mobile/assets/lib/weui.min.css">
<link rel="stylesheet" href="mobile/assets/css/jquery-weui.css">
<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
<style type="text/css">
	.memo{
		font-size:12px;
		color:#666;
		padding-left:20px;
		background-image:url(iwork_img/user.png);
		background-repeat:no-repeat;
	}
	.item_title{
		font-size:16px;
		font-weight:normal;
		padding-left:5px;
	}
</style>

<script type="text/javascript">
    function openNoticePage(title,actDefId,instanceId,excutionId,taskId,dataid){
	var url = "wechat_process_notice.action";
	redirectUrl(url);
}
function openSignsPage(title,actDefId,instanceId,excutionId,taskId){
	var url = 'loadSignsPage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
	redirectUrl(url); 
}
function openTaskPage(title,actDefId,instanceId,excutionId,taskId){
	var url = 'wechat_pr_formpage.action?actDefId='+actDefId+'&instanceId='+instanceId+'&excutionId='+excutionId+'&taskId='+taskId;
	redirectUrl(url);
}
    function finish(){
    	var url = "weixin_opqyyq_account.action";
    	redirectUrl(url);
    }
    function toscxw(){
    	var url = "weixin_opscxw_account.action";
    	redirectUrl(url);
    }  
	function redirectUrl(url){
		this.location.href = url;
	}
	function openMoreHtml(showId){
				
				$.post("getMoreTitleStr.action",{portletId:showId},function(data){
					var title = data;
					var url = "getMoreCommonTypeHtmlStr.action?showId=" + showId + "&pageSize=10&pageNo=1";
					window.parent.addTab(title,url,''); 
				});
			}
			function addTalk(formid,demid,instanceid,ggid,hfqkid) {
			
				var pageUrl ="";
			    $.ajax({
					type: "POST",
					url: "zqb_announcement_notice_tzlx.action",
					data: {ggid:ggid},
					success: function(data){
						
						updckqk(ggid);
						if(data=="培训通知"){
								
							if(instanceid==0){
								
								pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&ggid=" + ggid;
							}else{
							    pageUrl = "openTZGG.action?demId="+demid+"&ggid=" + ggid + "&formid="+formid+"&hfqkid="+hfqkid;
							}
							var win_width = window.screen.width;
var target = "_blank";
var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;
							/*art.dialog.open(pageUrl,{
								title : '通知公告回复',
								loadingText : '正在加载中,请稍后...',
								bgcolor : '#999',
								rang : true,
								width : screen.availWidth,
								cache : false,
								lock : true,
								height :screen.availHeight,
								iconTitle : false,
								extendDrag : true,
								autoSize : false,
								close:function(){
									window.location.reload();
								}
							});*/
						}else if(data=="问卷调查"){
							
	
					
		
					
							pageUrl = "createFormInstance.action?formid="+formid+"&demId="+demid+"&TZGGID=" + ggid;
var win_width = window.screen.width;
var target = "_blank";
var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
		page.location = pageUrl;

						/*
							art.dialog.open(pageUrl,{
								title : '问卷调查',
								loadingText : '正在加载中,请稍后...',
								bgcolor : '#999',
								rang : true,
								width : 1100,
								cache : false,
								lock : true,
								height : 580,
								iconTitle : false,
								extendDrag : true,
								autoSize : false,
								close:function(){
									window.location.reload();
								}
							});*/
						}else{
							
							pageUrl = "zqb_announcement_notice_reply_add.action?ggid=" + ggid + "&hfqkid="+hfqkid;
							var target = "_blank";
							var win_width = window.screen.width;
							var page = window.open('form/loader_frame.html',target,'width='+ win_width+ ',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
							page.location = pageUrl;
							/*art.dialog.open(pageUrl,{
								title : '通知公告回复',
								loadingText : '正在加载中,请稍后...',
								bgcolor : '#999',
								rang : true,
								width : 1100,
								cache : false,
								lock : true,
								height : 580,
								iconTitle : false,
								extendDrag : true,
								autoSize : false,
								close:function(){
									window.location.reload();
								}
							});*/
						}
					}
				});
			
				/*art.dialog.open(pageUrl,{
					title : '通知公告回复',
					loadingText : '正在加载中,请稍后...',
					bgcolor : '#999',
					rang : true,
					width : 1100,
					cache : false,
					lock : true,
					height : 580,
					iconTitle : false,
					extendDrag : true,
					autoSize : false,
					close:function(){
						window.location.reload();
					}
				});*/
				
			}
			function addCustomer(tznr,tzggid){
				var pageUrl = tznr+"&TZGGID="+tzggid;
				/*art.dialog.open(pageUrl,{
					title:'持续督导日常工作反馈',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1100,
					cache:false,
					lock: true,
					height:800, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){
						window.location.reload();
					}
				});*/
				var target = "_blank";
				var win_width = window.screen.width;
				var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.location = pageUrl;
			}
			function showCxddfkgzxx(ggid){
				
				var pageUrl = "zqb_cxdd_customerList.action?ggid="+ggid;
				art.dialog.open(pageUrl,{
					title:'持续督导日常工作反馈',
					loadingText:'正在加载中,请稍后...',
					bgcolor:'#999',
					rang:true,
					width:1100,
					cache:false,
					lock: true,
					height:800, 
					iconTitle:false,
					extendDrag:true,
					autoSize:false,
					close:function(){
						window.location.reload();
					}
				});
			}
			function updckqk(ggid){
				
				$.ajax({
					type: "POST",
					url: "zqb_announcement_upd_ckqk.action",
					data: {ggid:ggid}
				});
			}
			function showWJDC(ggid){
				var url = 'zqb_vote_wjdc.action?ggid='+ggid;
				var target = "_blank";
				var win_width = window.screen.width-50;
				var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.document.location = url;
				return;
			}
			function showCommunicate(customerno,ggid){
				alert(customerno);
				var pageUrl = "showCommunicate.action?customerno="+customerno+"&ggid="+ggid;
				var target = "_blank";
				var win_widths = window.screen.width-50;
				var page = window.open(pageUrl,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.document.location = pageUrl;
				
				return;
			}
			function updateCommunicate(customerno,ggid){
				var pageUrl = "updateCommunicate.action?customerno="+customerno+"&ggid="+ggid;
				var target = "_blank";
				var win_width = window.screen.width-50;
				var page = window.open('form/loader_frame.html',target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.document.location = pageUrl;
			}
			function showCustomer(customerno,ggid,dqrq){
				var url = 'zqb_vote_customerList.action?khbh='+customerno+'&ggid='+ggid+'&dqrq='+dqrq;
				var target = "_blank";
				var win_width = window.screen.width-50;
				var page = window.open(url,target,'width='+win_width+',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
				page.document.location = url;
				return;
			}
</script>
  </head>

  <body ontouchstart>
     <div class="weui_tab">
      <div class="weui_navbar">
        <div class="weui_navbar_item weui_bar_item_on">
        通知公告
        </div>
        <div class="weui_navbar_item" onclick="finish()">
          企业舆情
        </div>
        <div class="weui_navbar_item" onclick="toscxw()">
         市场新闻
        </div>
      </div>
      <div class="weui_tab_bd">
			<div  id="list" class="weui_cells weui_cells_access ">
				
		     </div>
      </div>
    </div>
    <div id="tip" class="weui-infinite-scroll" >
      <div  class="infinite-preloader"></div>
      正在加载
    </div>
    
   	 	<script src="mobile/assets/lib/jquery-2.1.4.js"></script>
		<script src="mobile/assets/lib/fastclick.js"></script>
		<%-- <script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
		<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
		<script type="text/javascript">
			window.jQuery || document.write("<script src='../assets/js/jquery.min.js'>"+"<"+"/script>");
		</script>
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='../assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="../assets/js/bootstrap.min.js"></script>

		<!-- page specific plugin scripts -->
		<script src="../assets/js/jquery-ui.custom.min.js"></script>
		<script src="../assets/js/jquery.ui.touch-punch.min.js"></script> --%>
<script>
  $(function() {
    FastClick.attach(document.body);
  });
</script>
<script src="mobile/assets/js/jquery-weui.js"></script>
 <script>
 	var page = 1;
 	var pagesize = 15;
      var loading = false;
      $(document.body).infinite().on("infinite", function() {
        if(loading) return;
        $("#tips").show();
        loading = true;
        setTimeout(function() {
        page++;
        showlist(page);
          loading = false;
        }, 500);
      });
      
      function showlist(page){
        var searchKey = "";
        var itemNum = pagesize*(page-1);
      	var url = "weixin_TZGG_account.action"; 
      	$.post(url, {
	    	pageNo : page++,
	    	itemNum :itemNum,
	    	searchKey :searchKey
		}, function(rText) {
			$('#loading').fadeOut();
			if(rText==""){
				var loading = true;
				$("#tip").text("已到达底部");
			}else{
				$("#list").append(rText);
				$("#tip").hide();
			}
		});  
      }
      showlist(page);
    </script>
  </body>
</html>
