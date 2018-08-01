<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>系统计划任务</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="iwork_css/common.css">
<link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
<link rel="stylesheet" type="text/css" href="iwork_themes/easyui/gray/easyui.css">
<link href="iwork_css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js" ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="iwork_js/sys_schedule.js"></script>
<script type="text/javascript" src="iwork_sms/js/calendar.js"></script>
<script type="text/javascript" src="iwork_js/system/sys_schedule.js"></script>
<script type="text/javascript">
 //==========================装载快捷键===============================//快捷键
    jQuery(document).bind('keydown',function (evt){		
		    if(evt.ctrlKey&&evt.shiftKey){
			return false;
		   }
		   else if(evt.ctrlKey && event.keyCode==82){ //Ctrl+r /刷新操作
			         this.location.reload(); return false;
		     } 
		  else if(evt.shiftKey && event.keyCode==78){ //Shift+n 新增操作
					add(); return false;
				}
		 else if(evt.ctrlKey && event.keyCode==83){ //ctrl+s保存
					doSubmit(); return false;
				}		
}); //快捷键
	</script>
</head>
<body class="easyui-layout"> 
<div region="north" border="false" style="padding:0px;overflow:no">
<div class="tools_nav">
<a href="javascript:add();" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
<a href="javascript:remove();" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
<a href="javascript:showlog();" class="easyui-linkbutton" plain="true" iconCls="icon-search">查看日志</a>
</div>
</div>
<div region="center" style="padding:3px;">
<table id="sysschedulegrid"></table>
</div>
<!-- 编辑窗口 -->
<div id="idialog" icon="icon-ok"  style="width:700px;height:350px;padding-top:10px;background: #fafafa;">			
 <form id="iform" method="post">
 	<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
   		<td align='right'><label for="planname">任务名称：</label></td>
      	<td><input class="easyui-validatebox" required="true" validType="maxLength[64]" type="text" id="planName" name="planName"></input><img src=iwork_img/notNull.gif alt=必须填写></td>
      	<td align='right'>优先级：</td>
      	<td><select name='planPri'>
      			<option value='0'>0</option>
      			<option value='1'>1</option>
      			<option value='2'>2</option>
      			<option value='3'>3</option>
      			<option value='4'>4</option>
      			<option value='5' selected>5</option>
      			<option value='6'>6</option>
      			<option value='7'>7</option>
      			<option value='8'>8</option>
      			<option value='9'>9</option>
      		</select></td>
      </tr>
      <tr>
  		<td align='right'><label for="classz">任务执行类：</label></td>
      	<td>
      		<input class="easyui-validatebox" validType="maxLength[200]" type="text" id="classz" name="classz"></input>
      		<!-- <font color="#FF0000">*</font> -->
      		<!-- <input type="button" name="testBtn" onClick="testClass();" value="测试" class ='actionsoftButton'> -->
			<!-- <div  id="messageDiv" ><div> -->
		</td>
		<td align='right'>开始时间/结束时间：</td>
		<td>
	            	<input type='text'  id='usefulLife_start' name='usefulLife_start'  style='width:90px;' class='easyui-datebox' required="true" editable='false'>
	            <input type="button" onClick="$('#usefulLife_start').datebox('setValue','');" name="clear1" value="清空" class ='actionsoftButton'>/
	            	<input type='text'  id='usefulLife_end' name='usefulLife_end'  style='width:90px;' class='easyui-datebox' required="true" editable='false'>
				<input type="button" onClick="$('#usefulLife_end').datebox('setValue','');" name="clear2" value="清空" class ='actionsoftButton'>
		</td>
      </tr>
      <tr>
   		<td align='right'><label for="planDesc">任务描述：</label></td>
   		<td><input class="easyui-validatebox" validType="maxLength[220]" type="text" id="planDesc" name="planDesc"></input></td>
      	<td align='right'>失败补偿次数：</td>
      	<td><select name='repeatNum'>
      			<option value='0' selected>0</option>
      			<option value='1'>1</option>
      			<option value='2'>2</option>
      			<option value='3'>3</option>
      			<option value='4'>4</option>
      			<option value='5'>5</option>
      			<option value='6'>6</option>
      			<option value='7'>7</option>
      			<option value='8'>8</option>
      			<option value='9'>9</option>
      			<option value='10'>10</option>
      		</select></td>
      </tr>
    </table>
    <fieldset style="padding:5px;border:1px solid gray; line-height:2.0;">
		<legend style='padding:8px;background-color:ffffff;color:000000' >执行设置</legend>
	    <table  border="0" width="100%">
	    	 <tr>
			  	<td align="left">执行频率</td>
				<td colspan="3" align="left">
				&nbsp;<input type="radio" name="ruleType" value="0" onClick="showCycleDiv(this.value);" checked="checked">每天
				&nbsp;<input type="radio" name="ruleType" value="1" onClick="showCycleDiv(this.value);">每周
				&nbsp;<input type="radio" name="ruleType" value="2" onClick="showCycleDiv(this.value);">每月
				&nbsp;<input type="radio" name="ruleType" value="3" onClick="showCycleDiv(this.value);">每季
				&nbsp;<input type="radio" name="ruleType" value="4" onClick="showCycleDiv(this.value);">每年
				&nbsp;<input type="radio" name="ruleType" value="5" onClick="showCycleDiv(this.value);">服务启动时										
				</td>
			 </tr>
			 <tr>
				<td align="left" width='70'>执行时间：</td>
				<td colspan="2" width='600'>
				  <div id="dayDiv" style="display:block">每天</div>
				  <div id="weekDiv" style="display:none;">
				 	<table>
				 		<td width="100%">&nbsp;每周第<input id="dayOfWeekStr" name="dayOfWeekStr" type="text" size="15" onkeyup= "this.value=this.value.replace(/\D/g, ' ') " onafterpaste= "this.value=this.value.replace(/\D/g, ' ') ">日<br>[小写数字,用空格分开.从周日到周六(1,2,3,4,5,6,7)
							周一是2,周六是7]</td>
					</table>
				  </div>
				  <div id="monthDiv" style="display:none">
					<table>
						<td width="100%">&nbsp;每月第
						<input type="text" id="dayOfMonthStr" name="dayOfMonthStr" size="15" onkeyup= "this.value=this.value.replace(/\D/g, ' ') " onafterpaste= "this.value=this.value.replace(/\D/g, ' ') ">号(小写数字,用空格分开.例如：2 4 5)</td>
					</table>
				  </div>
				  <div id="quarterDiv" style="display:none">
					<table>
						<td width="100%">&nbsp;第
						<select id="monthOfQuarter" name="monthOfQuarter">
							<option value="1" selected>1</option>
							<option value="2">2</option>
							<option value="3">3</option>
						</select>
						月第
						<select id="dayOfQuarterMonth" name="dayOfQuarterMonth">
							<option value="1" selected>1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
						</select>
						天												
						</td>
					</table>
				  </div>
				  <div id="yearDiv" style="display:none">
					<table>
						<td width="100%">每年第
						<select id="monthOfYear" name="monthOfYear">
							<option value="1" selected>1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
						</select>
						月第
						<select id="dayOfYearMonth" name="dayOfYearMonth">
							<option value="1" selected>1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
						</select>
						</td>
					</table>
				  </div>
				  <div id="awsDiv" style="display:none">
					<table>
						<td width="100%">服务启动时立即执行,每隔<input id="intervalMinutes" name="intervalMinutes" type="text" size="5">分钟执行一次
						</td>
					</table>
				  </div>
				   <div  style="display:block" id="hhmmssDiv">
						<table>
							<td width="80%">
							<select id="hour" name="hour">
								<option value="">小时</option>
								<option value="00" selected>0</option>
								<option value="01">1</option>
								<option value="02">2</option>
								<option value="03">3</option>
								<option value="04">4</option>
								<option value="05">5</option>
								<option value="06">6</option>
								<option value="07">7</option>
								<option value="08">8</option>
								<option value="09">9</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
							</select>时
							<select id="minute" name="minute">
								<option value="">分钟</option>
								<option value="00" selected>0</option>
								<option value="01">1</option>
								<option value="02">2</option>
								<option value="03">3</option>
								<option value="04">4</option>
								<option value="05">5</option>
								<option value="06">6</option>
								<option value="07">7</option>
								<option value="08">8</option>
								<option value="09">9</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option>
								<option value="25">25</option>
								<option value="26">26</option>
								<option value="27">27</option>
								<option value="28">28</option>
								<option value="29">29</option>
								<option value="30">30</option>
								<option value="31">31</option>
								<option value="32">32</option>
								<option value="33">33</option>
								<option value="34">34</option>
								<option value="35">35</option>
								<option value="36">36</option>
								<option value="37">37</option>
								<option value="38">38</option>
								<option value="40">40</option>
								<option value="41">42</option>
								<option value="43">43</option>
								<option value="44">44</option>
								<option value="45">45</option>
								<option value="46">46</option>
								<option value="47">47</option>
								<option value="48">48</option>
								<option value="49">49</option>
								<option value="50">50</option>
								<option value="51">51</option>
								<option value="52">52</option>
								<option value="53">53</option>
								<option value="54">54</option>
								<option value="55">55</option>
								<option value="56">56</option>
								<option value="57">57</option>
								<option value="58">58</option>
								<option value="59">59</option>
							</select>
							分 
							<font color="#FF0000">*</font>
							<input type="hidden" id="second" name="second" value="0">
							</td>
							</table>
							</div>
				  </td>
				  <td >
							</td>
							</tr>
							</table>
							</fieldset>
    <input type='hidden' id='type' name='type'>
    <input type="hidden" name="issystem" value="0">
	<input type=hidden name=cmd value="IWork_Sys_Schedule_Create">
	<input type=hidden id='id' name='id'>
	<input type=hidden id='flag' name='flag'>    
  </form>	   
</div>
</body>
</html>