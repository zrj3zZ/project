<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset="GBK">
	${style}
	${script}
	<script type="text/javascript">
		//关闭窗口
		function close{
			window.close();
		}
		//打印
		function print(){
			window.print();
		}
		//隐藏信息栏
		function hiddenInfo{
		
		}
		//显示信息栏
		function showInfo{
		
		} 
	</script>
	<!--统一配置-->
	<link href="iwork_css/formstyle.css" rel="stylesheet" type="text/css" />
	<style>
		body { font-family:Verdana; font-size:14px; margin:0;}
		#container {margin:0 auto; width:100%;}
		#header { height:25px;  margin-bottom:1px;padding-left:5px; border-bottom:1px solid #CCCCCC; background-color:#F4FCFF}
		#mainContent { height:100%; margin-bottom:1px;}
		#sidebar { float:left; width:200px;  background:#FEFEFE;padding:5px;}
		#content { margin-left:201px !important; margin-left:198px;  background:#F4FEE9; padding:5px;}
		#iforminfo { width:200px; height:120px; background:#EDF7FE;}
		#infotitle{ background:#FEFDEF; padding:3px;border-bottom:1px solid #666;}
		.iformContent {background:#EDF7FE;padding:5px;overflow:auto;display:table;border-collapse:separate;}
		.info_row{display:table-row;}
		.info_row div {display:table-cell;border-bottom:1px dotted #666}
		.info_row .info_name {width:60px;font-size:12px;}
		.info_row .info_value {width:120px;font-size:12px;color:#E8A300;}
		.iformContent ul {margin:0 0 0 2px;padding:0;list-style:inside left;color:#E8A300;}
		.iformContent li {font-size:12px;display:block; margin:0 0 0 3px; LIST-STYLE-TYPE:disc}
	</style>
	</head>
	<body>
		<form method="post" name="frmMain">
			<div id="container">
			  <div id="header"><a href="javascript:print();"><img src="iwork_img/printer.png" border="0" /> 打印</a>|<img src='iwork_img/application_view_list.gif' alt='隐藏预览日志' border='0'>|<img src='iwork_img/overlays.png' alt='显示预览日志' border='0'>|<a href="javascript:close();">关闭</a></div>
			  <div id="mainContent">
			    <div id="sidebar">
					<div id="iforminfo">
						<div id="infotitle">基本信息</div>
						<div class="iformContent">
							${baseinfo}
						</div>
					</div>
					<div id="infotitle">域绑定异常列表</div>
					<div id="iforminfo" style="height:200px;overflow:auto;">
						<div class="iformContent">${verifyErrorInfo}</div>
					</div> 
					<div id="infotitle">域绑定信息列表</div>
					<div id="iforminfo"  style="height:200px;overflow:auto;">
						<div class="iformContent">${verifySuccessInfo}</div>
					</div>
				</div>
			    <div id="content">
			    	${content}
			    </div>
			</div>
	</form>
	</body>
	</head>
</html>
