<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<title>短信平台-短信日志查询</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		
		<link rel="stylesheet" type="text/css" href="iwork_css/plugs/loginlog.css">
		
		</head>
<body>
<!-- TOP区 -->
	<div region="north" border="false" style="padding:0px;overflow:no">
	<table class="font"><tr><td>
类别：<%=request.getAttribute("type-checkbox") %></td><td>
详情：<input type=text  size='12' name='keywords' value='<s:property value='hvalue'  escapeHtml='false'/>' />
</td><td>操作者：</td><td><input type=text size='8' id='sender' name='sender' value='<s:property value='hoprator'  escapeHtml='false'/>'></td></tr><tr><td>
时间：<input type=text  name='startDate' id='startDate' value='<s:property value='hstartd'  escapeHtml='false'/>' style='width:80px;' class='easyui-datebox' editable='false' >
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
<input type=text  style='width:80px;' name='endDate'  id='endDate' class='easyui-datebox' editable='false' value='<s:property value='hendd'  escapeHtml='false'/>'/>
<select name="endHour" id="endHour" value='<s:property value='hendh'  escapeHtml='false'/>'>
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
</select>分</td><td><input type=hidden name=typepage id=typepage value='<s:property value="checktype"/>'>
<input type=button value='查 询'  class="font" onClick="querylog()" name='buttonquery'  border='0'>
</td></tr></table>

</div>

<div region="center" style="padding:3px;">
		<table id="metadata_grid" style="margin:2px;"></table>
		<div style="text-align:right;padding-right:5px;">
		<table>
		<tr>
			    	<td colspan="9" class="font">
			    		共<s:property value="totalRows"/>行&nbsp;
			    		第<s:property value="currentPage"/>页&nbsp;
			    		共<s:property value="pager.getTotalPages()"/>页&nbsp;
			    		<a href="javascript:querylog2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">首页</a>
			    		<a href="javascript:querylog2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">上一页</a>
			    		<a href="javascript:querylog2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">下一页</a>
			    		<a href="javascript:querylog2('<s:property value="currentPage"/>','<s:property value="pagerMethod"/>');">尾页</a>
			    	</td>
			    </tr>	</table>
			    </div>
	</div>
<s:form>
  <input type='hidden' name='checktype' id='checktype'>
  <!--查询日志-->
  <input type=hidden name=hoprator id=hoprator>
  <input type=hidden name=hvalue id=hvalue>
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
	             	url:"qlogj.action?checktype=<s:property value='checktype'  escapeHtml='false'/>&hoprator=<s:property value='hoprator'  escapeHtml='false'/>&hstartd=<s:property value='hstartd'  escapeHtml='false'/>&hstarth=<s:property value='hstarth'  escapeHtml='false'/>&hstartm=<s:property value='hstartm'  escapeHtml='false'/>&hendd=<s:property value='hendd'  escapeHtml='false'/>&hendh=<s:property value='hendh'  escapeHtml='false'/>&hendm=<s:property value='hendm'  escapeHtml='false'/>&hvalue="+encodeURI(encodeURI('<s:property value='hvalue'  escapeHtml='false'/>')),
					loadMsg: "正在加载数据...",
					fitColumns: true,
					rownumbers:true,
					singleSelect:true,
					columns:[[
						{field:'type',title:'类别',width:40},
						{field:'time',title:'时间',width:70,align:'left'},
						{field:'operator',title:'操作者',width:30,align:'left'},
						{field:'value',title:'详情',width:350,align:'left'}
						
					]]
				});	
		});	

</script>

<script type="text/javascript" src="iwork_js/plugs/loginlog.js"></script>

</html>