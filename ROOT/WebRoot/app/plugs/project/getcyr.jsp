<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
	
	
	<script type="text/javascript">
		//加载导航树  
		$(function(){
				$('#xm').pagination({  
			    total:<s:property value="xmNum"/>,  
			    pageNumber:<s:property value="pageNumberXM"/>,
			    pageSize:<s:property value="pageSizeXM"/>,
			    onSelectPage:function(pageNumber, pageSize){
			    	submitXM(pageNumber,pageSize);
			    }
			});
		});
		function submitXM(pageNumber,pageSize){
		$("#pageNumberXM").val(pageNumber);
		$("#pageSizeXM").val(pageSize);
		$("#frmMainXM").submit();
		return ;
	    }

		 function expExcel(){
		   //导入excel
			var pageUrl = "doCYExcelExp.action";
			$("#ifrmMain").attr("action",pageUrl); 
			$("#ifrmMain").submit();
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
	     /* .grid {
  			 
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
  				border-bottom:1px solid #eee;
			border-top:1px dotted #fff;
			border-right:1px solid  #eee;
  			} */
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
	</style>
</head> 
<body class="easyui-layout">

 <div region="north" border="false">
	      	<div class="tools_nav" style="overflow-y: hidden">
	      	 <span style="float:right;padding-right:32px"><a href="zqb_project_getXM.action">以项目为基准统计</a>|以成员为基准统计</span>
	      	</div>
          </div>
           <div region="center" border="false">
      	<table width="100%">	 	     		
      		<tr>
      			<td  class="gridTitle" >以成员为基准统计</td>
      			<td align="right" style="padding-right:27px">
      			<a href="javascript:expExcel();" class="easyui-linkbutton" plain="true" iconCls="icon-excel-exp">导出</a>
      			</td>
      			
      				
      		</tr>
      		<tr>
      	<td class="grid" colspan="2">
      	 <form name='ifrmMain' id='ifrmMain'  method="post" >
        <table width="100%" style="border:1px solid #efefef" >
			    <tr class="header">
			    <td>
			         参与人名称
			    </td>
			      <td>
			     项目数量
			    </td>
			        <td>
			       项目名称
			    </td>
			      <td>
			       项目最新进展
			    </td>
			       <td>
			     最后更新时间
			    </td>
			    </tr>
			    <s:iterator value="list" status="ll">
			     <tr class="cell">   
			       <s:if test="#ll.index-1<0||NAME!=list[#ll.index-1].NAME">
			    <td rowspan="<s:property value="NUM"/>">
			    <s:property value="NAME"/>
			    </td>
			     <td rowspan="<s:property value="NUM"/>">
			      <s:property value="NUM"/>
			      </td>
			      </s:if>
			      <td>
			    <s:property value="PROJECTNAME"/>
			    </td>
			     <td>
			    <s:property value="XMJD"/>
			    </td>
			       <td>
			    <s:property value="GXSJ"/>
			    </td>
			    </tr>
			    </s:iterator>
			  </table>
			</form>
		 </td>
		</tr>
      		
      	</table>
      	<div style = "padding:5px">
      	       <form action="zqb_project_getCYR.action" method=post name=frmMainXM id=frmMainXM >
				<s:if test="xmNum!=0">
				       <form action="zqb_project_getCYR.action" method=post name=frmMainXM id=frmMainXM >
					<div id="xm" style="background:#efefef;text-align:right;border:1px solid #ccc;"></div>
						<s:hidden name="pageNumberXM" id="pageNumberXM"></s:hidden>
						<s:hidden name="pageSizeXM" id="pageSizeXM"></s:hidden>
						</form>
					</s:if>
		    </form>
      	</div>
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