<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	   <title>短信平台-个人短信查询</title>
	   <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		<link rel="stylesheet" type="text/css" href="iwork_css/plugs/loginprimsg.css">
		</head>
	<body class="easyui-layout">

<br><br><!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<div style="padding:2px;background:#efefef;border-bottom:1px ">
	<div style='width:782px;text-align:left;margin-top:1px;margin-bottom:1px;margin-left:1px;margin-right:1px;border:1px dashed #ccc;float:left;'>
	<div style='width:600px;border:none;border-right:1px dashed #ccc;float:left;'>
	<div style='margin-top:5px;margin-bottom:1px;margin-left:5px;margin-top:5px;'>
批&nbsp;&nbsp;&nbsp;&nbsp;号：<input type=text size='8' class="easyui-validatebox" name='batchnum' id='batchnum' value='<s:property value='hbatch'  escapeHtml='false'/>'>
接收号码：<input type=text size='8' class="easyui-validatebox" name='mobilenum' id='mobilenum' value='<s:property value='hmobile'  escapeHtml='false'/>'>
内容：<input type=text size='10' class="easyui-validatebox" name='keywords' id='keywords' value='<s:property value='hcontent'  escapeHtml='false'/>'>
状态：<%=request.getAttribute("statuses")%>
</div>
<div style='margin-top:0px;margin-bottom:5px;margin-left:5px;'>
提交时间：<input type=text  size='10' id='startDate' name='startDate'  style='width:80px;' class='easyui-datebox' value='<s:property value='hstartd'  escapeHtml='false'/>' editable='false'>
<select name="startHour" id="startHour" onChange="Menu(this.form,1);" value='<s:property value='hstarth'  escapeHtml='false'/>'>
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option>
	<option value='04'>04</option><option value='05'>05</option><option value='06'>06</option><option value='07'>07</option>
	<option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option>
	<option value='16'>16</option><option value='17'>17</option><option value='18'>18</option><option value='19'>19</option>
	<option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
</select>时
<select name="startMin" id="startMin" value='<s:property value='hstartm'  escapeHtml='false'/>'>
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option> <option value='04'>04</option><option value='05'>05</option>
	<option value='06'>06</option><option value='07'>07</option><option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option><option value='16'>16</option><option value='17'>17</option>
	<option value='18'>18</option><option value='19'>19</option><option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
	<option value='24'>24</option><option value='25'>25</option><option value='26'>26</option><option value='27'>27</option><option value='28'>28</option><option value='29'>29</option>
	<option value='30'>30</option><option value='31'>31</option><option value='32'>32</option><option value='33'>33</option><option value='34'>34</option><option value='35'>35</option>
	<option value='36'>36</option><option value='37'>37</option><option value='38'>38</option><option value='39'>39</option><option value='40'>40</option><option value='41'>41</option>
	<option value='42'>42</option><option value='43'>43</option><option value='44'>44</option><option value='45'>45</option><option value='46'>46</option><option value='47'>47</option>
	<option value='48'>48</option><option value='49'>49</option><option value='50'>50</option><option value='51'>51</option><option value='52'>52</option><option value='53'>53</option>
	<option value='54'>54</option><option value='55'>55</option><option value='56'>56</option><option value='57'>57</option><option value='58'>58</option><option value='59'>59</option>
</select>分至
<input type=text  size='10' id='endDate' name='endDate'  style='width:80px;' class='easyui-datebox' value='<s:property value='hendd'  escapeHtml='false'/>'><select name="endHour" id="endHour" value='<s:property value='hendh'  escapeHtml='false'/>' editable='false'>
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option>
	<option value='04'>04</option><option value='05'>05</option><option value='06'>06</option><option value='07'>07</option>
	<option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option>
	<option value='16'>16</option><option value='17'>17</option><option value='18'>18</option><option value='19'>19</option>
	<option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
</select>时
<select name="endMin" id="endMin" value='<s:property value='hendm'  escapeHtml='false'/>'>
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option> <option value='04'>04</option><option value='05'>05</option>
	<option value='06'>06</option><option value='07'>07</option><option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option><option value='16'>16</option><option value='17'>17</option>
	<option value='18'>18</option><option value='19'>19</option><option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
	<option value='24'>24</option><option value='25'>25</option><option value='26'>26</option><option value='27'>27</option><option value='28'>28</option><option value='29'>29</option>
	<option value='30'>30</option><option value='31'>31</option><option value='32'>32</option><option value='33'>33</option><option value='34'>34</option><option value='35'>35</option>
	<option value='36'>36</option><option value='37'>37</option><option value='38'>38</option><option value='39'>39</option><option value='40'>40</option><option value='41'>41</option>
	<option value='42'>42</option><option value='43'>43</option><option value='44'>44</option><option value='45'>45</option><option value='46'>46</option><option value='47'>47</option>
	<option value='48'>48</option><option value='49'>49</option><option value='50'>50</option><option value='51'>51</option><option value='52'>52</option><option value='53'>53</option>
	<option value='54'>54</option><option value='55'>55</option><option value='56'>56</option><option value='57'>57</option><option value='58'>58</option><option value='59'>59</option>
</select>
<input type=button value='查 询'  onClick="privateMSG()" name='buttonquery'  border='0'>
<br>
</div>	
</div>
<div style='width:auto;border:none;margin-left:1px;margin-right:1px;margin-top:1px;margin-bottom:10px;line-height:100%;'>
	<br><%=request.getAttribute("html2") %>
</div>
</div>	
	</div>
	</div>     
    <div region="center" border="false" style="padding:3px;">
		<table id="metadata_grid" style="margin:2px;"></table>
		<div style="text-align:right;padding-right:5px;">
		<table>
		<tr>			    	
		<td colspan="11" class="font1">
			    		共<s:property value="totalRows"/>行&nbsp;
			    		第<s:property value="currentPage"/>页&nbsp;
			    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
			    		<a href="javascript:privateMSG1('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">首页</a>
			    		<a href="javascript:privateMSG1('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">上一页</a>
			    		<a href="javascript:privateMSG1('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">下一页</a>
			    		<a href="javascript:privateMSG1('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">尾页</a>
			    	</td>
			    </tr>	</table>
			    </div>
	</div>
	

   
  <s:form>
  <!--查询短信 -->
  <input type=hidden name=hbatch id=hbatch>
  <input type=hidden name=hmobile id=hmobile>
  <input type=hidden name=hcontent id=hcontent>
  <input type=hidden name=hstatus id=hstatus>
  <input type=hidden name=hstartd id=hstartd>
  <input type=hidden name=hstarth id=hstarth>
  <input type=hidden name=hstartm id=hstartm>
   <input type=hidden name=hendd id=hendd>
  <input type=hidden name=hendh id=hendh>
  <input type=hidden name=hendm id=hendm>
  </s:form>
</body>
<script>
		$(function(){	
		                  
		$('#metadata_grid').datagrid({
	             	url:"privatemsgj.action?hmobile=<s:property value='hmobile'  escapeHtml='false'/>&hbatch=<s:property value='hbatch'  escapeHtml='false'/>&hstartd=<s:property value='hstartd'  escapeHtml='false'/>&hstarth=<s:property value='hstarth'  escapeHtml='false'/>&hstartm=<s:property value='hstartm'  escapeHtml='false'/>&hendd=<s:property value='hendd'  escapeHtml='false'/>&hendh=<s:property value='hendh'  escapeHtml='false'/>&hendm=<s:property value='hendm'  escapeHtml='false'/>&hcontent="+encodeURI(encodeURI('<s:property value='hcontent'  escapeHtml='false'/>'))+"&hstatus="+encodeURI(encodeURI('<s:property value='hstatus'  escapeHtml='false'/>')),
					loadMsg: "正在加载数据...",
					fitColumns: true,
					rownumbers:true,
					singleSelect:true,
					//pagination:true,
				
					columns:[[
						{field:'batch',title:'批号',width:80},
						{field:'submitt',title:'提交时间',width:90,align:'left'},
						{field:'sendt',title:'发送时间',width:90,align:'left'},
						{field:'phone',title:'接收号码',width:50,align:'left'},
						{field:'content',title:'短信内容',width:200,align:'left'},
						{field:'status',title:'状态',width:20,align:'left'}
					]]
				});	
		});	
</script>

<script type="text/javascript" src="iwork_js/plugs/loginprimsg.js"></script>

</html>