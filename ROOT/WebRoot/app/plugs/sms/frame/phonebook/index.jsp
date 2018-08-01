<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>中文首字母排序</title>
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css"/>
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/public.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/iformpage.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
    <script src="http://demo.jb51.net/js/2011/listnav-jquery/js/jquery-3.0.4.min.js" type="text/javascript"></script>
	<!--获取中文首字母的函数，需要jQuery支持-->
    <script src="http://demo.jb51.net/js/2011/listnav-jquery/js/jquery.charfirst.pinyin.js" type="text/javascript"></script>
	<!--ListNav是一个用于创建按字母顺序分类导航的jQuery插件。-->
    <script src="http://demo.jb51.net/js/2011/listnav-jquery/js/jquery.listnav.min-2.1.js" type="text/javascript"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"> </script>
	<script type="text/javascript" src="iwork_js/engine/ifromworkbox.js"> </script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <link rel="stylesheet" type="text/css" href="iwork_plugs/artDialog/skins/blue.css"/>
	<script type="text/javascript" src="iwork_plugs/artDialog/artDialog.js"></script>
	<script type="text/javascript" src="iwork_plugs/artDialog/plugins/iframeTools.js"></script>
    <script type="text/javascript">
        $(function() {
            $('#myList').listnav({
                includeOther: true,
                noMatchText: '没有号码信息',
                prefixes: ['the', 'a']
            });
        });
        function addItem(){
			var formid = 121;
			var demId = 43; 
			 var pageurl = 'createFormInstance.action?formid='+formid+'&demId='+demId; 
			 var title = "";
			 var height=480;
			 var width = 600;
			 var dialogId = "projectItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;    
		}
        function addGroup(demId,formid){
			//var demId = $("#dhbfzDemId").val();
			//var formid = $("#dhbfzFormid").val();
			 var pageurl = 'createFormInstance.action?formid='+formid+'&demId='+demId; 
			 var title = "";
			 var height=480;
			 var width = 900;
			 var dialogId = "projectItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;    
		}
		function openExcelImp(){
		var pageurl = 'phonebooExcep';
			 var title = "";
			 var height=480;
			 var width = 600;
			 var dialogId = "projectItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
		}
		
		function removeItem(instanceid){
			if(confirm("确定执行删除操作吗?")) {
				var pageUrl = "phonebook_remove_item.action";
				$.post(pageUrl,{instanceid:instanceid},function(data){ 
			       			if(data=='ok'){
			       				window.location.reload();
			       			}else{
			       				alert("删除失败");;
			       			} 
			     }); 
			}
		}
		
		function editItem(instanceid){
			var formid = 121;
			var demId = 43; 
			 var pageurl = 'openFormPage.action?formid='+formid+'&demId='+demId+'&instanceId='+instanceid; 
			 var title = "";
			 var height=480;
			 var width = 600;
			 var dialogId = "projectItem";
			 parent.openWin(title,height,width,pageurl,this.location,dialogId);
			return;    
		}
		function impPhoneBook(){
			var pageUrl = "phonebook_index_impphonebook.action";
			var target = "_blank";
			var win_width = window.screen.width;
			var page = window.open('form/loader_frame.html',target,'width=' + win_width +',height=800,top=50,left=150,location=no,menubar=no,toolbar=no,status=no,directories=no,scrollbars=yes,resizable=yes');
			page.location = pageUrl;
			/* art.dialog.open(pageUrl,{
				id:'Category_show',
				cover:true, 
				title:'导入通讯录',
				loadingText:'正在加载中,请稍后...',
				bgcolor:'#999',
				rang:true,
				width:1000,
				cache:false,
				lock: true,
				height:550, 
				iconTitle:false,
				extendDrag:true,
				autoSize:false
			}); */
		}
		function expPhoneBook(){
			var url = encodeURI("phonebook_expphonebook.action?model=0");
	        window.location.href = url;
		}
		function expTempPBook(){
			var url = encodeURI("phonebook_expphonebook.action?model=1");
	        window.location.href = url;
		}
    </script>
	<style type="text/css">
		#myList li{
			border-bottom:1px dotted #efefef;
			padding-bottom:5px;
			list-style-type:none;
			height:20px;
			padding:5px;
		}
		#myList li:hover{
			background:#ffffee;
		}
		.grid td{
			text-align:left;
		}
		.item{
			width:120px;
			color:#666;
		}
		
		.title{ 
			font-size:13px;
			font-family:黑体;
			color:#000; 
		} 
	</style>
</head>
<body class="easyui-layout">
<div region="north" border="false" >
      	<div class="tools_nav">
      		<div style="float: left;">
	      		<a class="easyui-linkbutton" plain="true" iconCls="icon-add" href="javascript:void(0);" onClick="addItem()">添加联系人</a>
	      		<a class="easyui-linkbutton" plain="true" iconCls="icon-add" href="javascript:void(0);" onClick="addGroup(<s:property value="dhbfzDemId"/>,<s:property value="dhbfzFormid"/>)">添加分组</a>
				<a class="easyui-linkbutton" plain="true" iconCls="icon-reload" href="javascript:this.location.reload();">刷新</a>
				<a class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp" href="javascript:impPhoneBook();">导入</a>
				<a class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp" href="javascript:expPhoneBook();">导出</a>
				<a class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp" href="javascript:expTempPBook();">导出模板</a>
			</div>
			<div style="float:left;padding-left:20px;">号码簿|<a href="phoneBook_index_address.action">通讯录</a></div>
        </div> 
      </div> 
      <div region="center"  border="false" style="padding:10px;padding-top:20px">
    <div id="noticeMSG">
        <label>中文字母序：</label>
    </div>
    <br />
   
  <!--显示字母序的层。注：此层id必需是ul的id+"-nav"-->
    <div id="myList-nav">
    </div>
   <!-- 兼容IE6 加clear:both;-->
    <ul id="myList" style="clear:both;">
        <s:iterator value="list" status="status">  
					<li>
								
								<span class="item title"><s:property value="NAME"/></span>
								
								<span class="item">
								<s:if test="TITLE!=''">
									[<s:property value="TITLE"/>]
								</s:if>
								<s:else>
								&nbsp;
								</s:else>
								</span>
								<span class="item">
								<s:if test="TEL!=''"> 
									[<s:property value="TEL"/>]
								</s:if>
								<s:else>
								&nbsp;
								</s:else>
								</span>
								<span class="item">
									<s:if test="EMAIL!=''">
									[<s:property value="EMAIL"/>]
								</s:if>
								<s:else>
								&nbsp; 
								</s:else>
								</span> 
								<span class="item">
								<s:if test="COMPANY!=''">
									[<s:property value="COMPANY"/>]
								</s:if> 
								<s:else>
								&nbsp;
								</s:else>
								</span>
								<span style="float:right;padding-right:10px"><a href="javascript:removeItem( <s:property value="INSTANCEID"/>)"><img src="iwork_img/delete2.gif"></a></span>
								<span style="float:right;padding-right:10px"><a href="javascript:editItem( <s:property value="INSTANCEID"/>)"><img src="iwork_img/010538151.gif"></a></span>
					</li>
					</s:iterator>
    </ul>
    </div>
</body>
</html>
