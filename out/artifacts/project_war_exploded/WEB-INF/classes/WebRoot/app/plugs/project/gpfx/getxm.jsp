<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>  
    <title>项目管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="iwork_css/common.css">
    <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
    <link rel="stylesheet" type="text/css" href="iwork_js/jqueryjs/easyui/themes/gray/easyui.css">
    <script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js" ></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
    <link href="iwork_css/message/sysmsgpage.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/engine/sysenginemetadata.css">
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
	<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="iwork_js/jqueryjs/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"></script>
    <script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true&skin=default"></script>
	
	<script type="text/javascript">
		//加载导航树  
		window.onload=function(){
				$('#xm').pagination({  
			    total:<s:property value="xmNum"/>,  
			    pageNumber:<s:property value="pageNumberXM"/>,
			    pageSize:<s:property value="pageSizeXM"/>,
			    onSelectPage:function(pageNumber,pageSize){
			    	submitXM(pageNumber,pageSize);
			    }
			});
		};
		function submitXM(pageNumber,pageSize){
		$("#pageNumberXM").val(pageNumber);
		$("#pageSizeXM").val(pageSize);
		$("#frmMainXM").submit();
		return ;
	}
   function expExcel(){
   //导入excel
	var pageUrl = "zqb_gpfx_project_doXMExcelExp.action";
	$("#ifrmMain").attr("action",pageUrl); 
	$("#ifrmMain").submit();
}
	function editUser(name){
		var userid=name.substring(0,name.indexOf("["));
		var pageUrl = "url:zqb_project_getXM_List.action?userid="+userid;
		$.dialog({ 
			title:'个人信息',
			loadingText:'正在加载中,请稍后...',
			bgcolor:'#999',
			rang:true,
			width:1100,
			cache:false,
			lock: true,
			height:180, 
			iconTitle:false,
			extendDrag:true,
			autoSize:false,
			content:pageUrl,
			close:function(){
				window.location.reload();
			}
		});
	}
		
		
		
	</script>
	<style type="text/css">
	.gridTitle{
  				padding-left:25px;
  				height:20px;
  				font-size:14px;
  				font-family:黑体;
  				background:transparent url(iwork_img/table_multiple.png) no-repeat scroll 5px 1px;
  			}
	     .grid {
  			 
			  padding:5px;
			  vertical-align:top;
			}
  			.grid table{
  				width:100%;
  				border:1px solid #efefef;
  			}
  			.grid th{
  				
  				padding:5px;
  				font-size:12px;
  				font-weight:500;
  				height:20px;
  				background-color:#ffffee;
  				border-bottom:1px solid #ccc;
  			}
  			.grid tr:hover{
  				background-color:#efefef;
  			}
  			.grid td{
  			
  				padding:5px;
  				line-height:16px;
  				
  			}
			.cell td{
			margin:0;
			padding:3px 4px;
			height:25px;
			font-size:12px;
			white-space:nowrap;
			word-wrap:normal;
			overflow:hidden;
			text-align:left;
			border-bottom:1px dotted #eee;
			border-top:1px dotted #fff;
			border-right:1px dotted #eee;
		}
		.cell:hover{
			background-color:#F0F0F0;
		}
  			.header td{
			height:30px;
			font-size:12px;
			padding:3px;
			white-space:nowrap;
			padding-left:5px;
			background:url('../../iwork_img/engine/tools_nav_bg.jpg') repeat-x left bottom;
			border-top:1px dotted #ccc;
			border-right:1px solid #eee;
		} 
	</style>
</head> 
<body class="easyui-layout">

	      	<div class="tools_nav" region="north" border="false">
	      	 <span style="float:right;padding-right:32px">以项目为基准统计|<a href="zqb_gpfx_project_getCYR.action">以成员为基准统计</a></span>
	      	</div>
           <div region="center" border="false">
                 <form name='ifrmMain' id='ifrmMain'  method="post" >
      	<table width="100%">	 	     		
      		<tr>
      			<td  class="gridTitle" >以项目为基准统计</td>
      			<td align="right" style="padding-right:27px">
      			<a href="javascript:expExcel();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
      			</td>
      			
      				
      		</tr>
      		<tr>
      			<td colspan="2"><s:property value="typePieData" escapeHtml="false"/></td>
      		</tr>
      		
      	</table>
      	 </form>
      	<div style = "padding:5px">
      	       <form action="zqb_gpfx_project_getXM.action" method=post name=frmMainXM id=frmMainXM >
				<s:if test="typePieData!=''">
					<div id="xm" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						<s:hidden name="pageNumberXM" id="pageNumberXM"></s:hidden>
						<s:hidden name="pageSizeXM" id="pageSizeXM"></s:hidden>
					</s:if>
		    </form>
      	</div>
      	    </form> 
		</div>
</body>
</html>
<!-- 新增查询过滤SQL注入关键字 -->
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