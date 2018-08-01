<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>员工履历</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/process-icon.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
	<link rel="stylesheet" type="text/css" href="iwork_css/formstyle.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/ui.jqgrid.css"/>
	<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/jqgrid/jquery-ui-1.8.2.custom.css"/>
	<link href="iwork_css/reset.css" rel="stylesheet" type="text/css"/>
	<link href="iwork_css/pformpage.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" href="iwork_js/kindeditor/themes/simple/simple.css" />
	<link rel="stylesheet" href="iwork_js/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="iwork_js/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="iwork_js/kindeditor/plugins/code/prettify.js"></script>
	<script type="text/javascript" src="iwork_js/commons.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/grid.locale-cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.jqGrid.min.js"  > </script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/jquery.form.js"></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/My97DatePicker/WdatePicker.js"   ></script>
	<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
	<script type="text/javascript" src="iwork_js/lhgdialog/lhgdialog.min.js?self=true"></script>
	<script type="text/javascript" src="iwork_js/pformpage.js"></script>
	<script type="text/javascript" src="iwork_js/json.js"></script>
	<style type="text/css">
		.td_title1 {
					line-height: 30px;
					font-size: 12px;
					text-align: center;
					letter-spacing: 0.1em;
					padding-right:10px;
					white-space:nowrap;
					border-bottom:1px #999999 thick;
					vertical-align:middle;
					font-family:"宋体";
			}
			/*数据字段内容样式*/
			.td_data1 {
				color:#0000FF;
				line-height: 23px;
				text-align: center;
				padding-left: 3px;
				font-size: 12px;
				font-family:"宋体";
				border-bottom:1px #999999 dotted;
				vertical-align:middle;
				word-wrap:break-word;
				word-break:break-all;
				font-weight:500;
				line-height:15px;
				padding-top:5px;
				color:0000FF; 
			}
	</style>
  </head>
  
  <body>
  		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='2'><b>个人基本信息</b></font>
	<table  width='95%' align='center'   cellpadding= '0'   cellspacing= '0'  frame="box" >
		<tr height='25' >
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif' width='20%'>&nbsp;&nbsp;性别：</td>
			<td class="td_data" width='30%'>${iworkhrygbaseinfomodel.gender }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif' width='22%'>&nbsp;&nbsp;出生日期：</td>
			<td class="td_data" width='28%'>${iworkhrygbaseinfomodel.birthday }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;证件类别：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.zjlbmc }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;证件号码：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.zjhm }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;婚姻状况：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.hyzkmc }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;政治面貌：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.zzmmmc }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;国籍：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.nationname }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;籍贯：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.jg }</td>
		</tr>
		<tr height='25'><td class="td_title" background='/iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;户籍性质：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.hjxzmc }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;户口所在地：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.hjszd }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif' >&nbsp;&nbsp;民族：</td>
			<td  class="td_data" >${iworkhrygbaseinfomodel.mzmc }</td>
			<td class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;职称：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.zc }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;最高学历：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.zhxlmc }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;最高学位：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.zhxwmc }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;最高学历获得院校：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.byyx }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;最高学历对应专业：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.zy }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;英语水平：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.yysp }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;其他外语及水平：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.qtwyjsp }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;当前工作地点：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.dqgzdd }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;期望工作地点：</td>
			<td class="td_data" >${iworkhrygbaseinfomodel.qwgzdd }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;首次加入金山日期：</td>
			<td  class="td_data" >${iworkhrygbaseinfomodel.fjoindate }</td>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;合同到期日：</td>
			<td  class="td_data" >暂无</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;最新入职日期：</td>
			<td  class="td_data" colspan='3'>暂无</td>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;专业特长：</td>
			<td  class="td_data" colspan='3'>${iworkhrygbaseinfomodel.zytz }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;兴趣爱好：</td>
			<td  class="td_data" colspan='3'>${iworkhrygbaseinfomodel.xqah }</td>
		</tr>
		<tr height='25'>
			<td class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;职业期望：</td>
			<td  class="td_data" colspan='3'>${iworkhrygbaseinfomodel.zyqw }</td>
		</tr>

</table>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='2'><b>联系方式</b></font>
<table width='95%' align='center'  cellpadding= '0'   cellspacing= '0' frame="box" >
	<tr height='25'>
		<td  class="td_title"  width='20%' background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;现住址：</td>
		<td  class="td_data"  width='30%'>${iworkhrryxxlxfsmodel.xzz }</td>
		<td class="td_title"  width='22%' background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;家庭固定电话：</td>
		<td class="td_data"  width='28%'>${iworkhrryxxlxfsmodel.jtgddh }</td>
	</tr>
	<tr height='25'>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;移动电话：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.mobile }</td>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;个人常用E-MAIL(非公司)：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.email }</td>
	</tr>
	<tr height='25'>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人姓名：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.jjlxr }</td>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人与本人关系：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.jjlxrgx }</td>
	</tr>
	<tr height='25'>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人电话：</td>
		<td  class="td_data"  >${iworkhrryxxlxfsmodel.jjlxrdh }</td>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人2姓名：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.jjlxr2 }</td>
	</tr>
	<tr height='25'>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人2与本人关系：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.jjlxrgx2 }</td>
		<td  class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人2电话：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.jjlxrdh }</td>
	</tr>
	<tr height='25'>
		<td  class="td_title"  background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人3姓名：</td>
		<td class="td_data" >${iworkhrryxxlxfsmodel.jjlxr3 }</td>
		<td class="td_title"   background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人3与本人关系：</td>
		<td  class="td_data" >${iworkhrryxxlxfsmodel.jjlxrgx3 }</td>
	</tr>
	<tr height='25'>
		<td  class="td_title" background='iwork_img/rszz/report-bg-blue2.gif'>&nbsp;&nbsp;紧急联系人3电话：</td>
		<td  class="td_data" colspan='3'>${iworkhrryxxlxfsmodel.jjlxrdh3 }</td>
	</tr>
	</table>
	<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='2'><b>外部工作经历</b></font>
<table width='95%' style='WORD-WRAP: break-word;word-break:break-all' align='center'  cellpadding= '0'   cellspacing= '0' frame="box">
	<tr height = '25'>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>开始日期</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>结束日期</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>单位名称</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='9%' align='center'>工作地点</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='8%' align='center'>部门</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='8%' align='center'>职位</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>核心职责</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>工作成就</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>离职原因</td>
	</tr>
	<s:iterator  value="wbgzList" status="status">
	<tr height = '25'>
		<td class="td_data1" ><s:property value='ksrq'/></td>
		<td class="td_data1" ><s:property value='jsrq'/></td>
		<td class="td_data1" ><s:property value='dwmc'/></td>
		<td class="td_data1" ><s:property value='gzdd'/></td>
		<td class="td_data1" ><s:property value='bm'/></td>
		<td class="td_data1" ><s:property value='zw'/></td>
		<td class="td_data1" ><s:property value='hszz'/></td>
		<td class="td_data1" ><s:property value='gzcj'/></td>
		<td class="td_data1" ><s:property value='lzyy'/></td>
	</tr>
	</s:iterator>
</table>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='2'><b>金山工作经历</b></font>
<table width='95%' style='WORD-WRAP: break-word;word-break:break-all' align='center'  cellpadding= '0'   cellspacing= '0' frame="box">
	<tr height = '25'>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>开始日期</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>结束日期</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>集团/公司/事业部</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='8%' align='center'>工作地点</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>部门</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>职位</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='12%' align='center'>核心职责</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='12%' align='center'>工作成就</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='12%' align='center'>获奖情况</td>
		<td class="td_title1"  background='iwork_img/rszz/report-bg-blue2.gif' width='6%' align='center'>直接上级</td>
	</tr>
	<s:iterator  value="jsgzList" status="status">
	<tr height = '25'>
		<td class="td_data1" align='center'><s:property value='ksrq'/></td>
		<td class="td_data1" align='center'><s:property value='jsrq'/></td>
		<td class="td_data1" align='center'><s:property value='dwmc'/></td>
		<td class="td_data1" align='center'><s:property value='gzdd'/></td>
		<td class="td_data1" align='center'><s:property value='bm'/></td>
		<td class="td_data1" align='center'><s:property value='zw'/></td>
		<td class="td_data1" align='center'><s:property value='hszz'/></td>
		<td class="td_data1" align='center'><s:property value='gzcj'/></td>
		<td class="td_data1" align='center'><s:property value='hjck'/></td>
		<td class="td_data1" align='center'><s:property value='dszjsj'/></td>
	</tr>
	</s:iterator>
</table>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='2'><b>个人教育经历</b></font>
<table width='95%' style='WORD-WRAP: break-word;word-break:break-all' align='center'  cellpadding= '0'   cellspacing= '0' frame="box">
	<tr height = '25'>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>开始日期</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>结束日期</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>教育类型</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='20%' align='center'>证书名称</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='10%' align='center'>国家</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='13%' align='center'>学校名称</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='13%' align='center'>院系</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='14%' align='center'>专业</td>
	</tr>
	<s:iterator  value="jyjlList" status="status">
	<tr height = '25'>
		<td  class="td_data1" ><s:property value='ksrq'/></td>
		<td  class="td_data1" ><s:property value='jsrq'/></td>
		<td  class="td_data1" ><s:property value='jylxmc'/></td>
		<td  class="td_data1" ><s:property value='zsmc'/></td>
		<td  class="td_data1" ><s:property value='gjmc'/></td>
		<td  class="td_data1" ><s:property value='xxmc'/></td>
		<td  class="td_data1" ><s:property value='yx'/></td>
		<td  class="td_data1" ><s:property value='zy'/></td>
	</tr>
	</s:iterator>
</table>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='2'><b>职业资格、技能证书</b></font>
<table width='95%' style='WORD-WRAP: break-word;word-break:break-all' align='center'  cellpadding= '0'   cellspacing= '0' frame="box">
	<tr height = '25'>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>证书名称</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>生效日期</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>失效日期</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>颁发机构</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='15%' align='center'>颁发日期</td>
	</tr>
	<s:iterator  value="zyjnList" status="status">
	<tr height = '25'>
		<td  class="td_data1" ><s:property value='zsmc'/></td>
		<td  class="td_data1" ><s:property value='begindate'/></td>
		<td  class="td_data1" ><s:property value='enddate'/></td>
		<td  class="td_data1" ><s:property value='bfjg'/></td>
		<td  class="td_data1" ><s:property value='bfrq'/></td>
	</tr>
	</s:iterator>
</table>
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size='2'><b>家庭情况</b></font>
<table width='95%' style='WORD-WRAP: break-word;word-break:break-all' align='center'  cellpadding= '0'   cellspacing= '0' frame="box">
	<tr height = '25'>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='20%' align='center'>姓名</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='20%' align='center'>与本人关系</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='30%' align='center'>工作单位</td>
		<td  class="td_title1" background='iwork_img/rszz/report-bg-blue2.gif' width='30%' align='center'>联系方式</td>
	</tr>
	<s:iterator  value="jtqkList" status="status">
	<tr height = '25'>
		<td  class="td_data1" ><s:property value='name'/></td>
		<td  class="td_data1" ><s:property value='gx'/></td>
		<td  class="td_data1" ><s:property value='gzdw'/></td>
		<td  class="td_data1" ><s:property value='dh'/></td>
	</tr>
	</s:iterator>
</table><br>
  </body>
</html>
