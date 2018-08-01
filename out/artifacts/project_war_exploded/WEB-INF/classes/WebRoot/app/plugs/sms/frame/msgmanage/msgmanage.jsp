<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>短信平台-短信管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.1.0.min.js"></script>
	   <script type="text/javascript" src="iwork_js/jqueryjs/jquery.easyui.min.js"></script>
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/icon.css">
	   <link rel="stylesheet" type="text/css" href="iwork_css/jquerycss/default/easyui.css">		
		
		<link rel="stylesheet" type="text/css" href="iwork_css/plugs/loginmanage.css">
		
		</head>
	
<body>
<form>
<table width="98%"  style="border: 1px dotted #000000" cellspacing="0" cellpadding="0"  >
<tr><td>
<table class="font" > 
<tr><td>提 交 人：<input type=text  id='sender' size=10 name='sender' value=''>
</td><td>号码：<input type=text id='mobilenum' size=10 name='mobilenum' value=''>
</td><td>批号：<input type=text name=batchnum size=10 id=batchnum  value=''>
</td><td>状态：<select name='status' id='status' ><option value='' selected>全部</option> <%=request.getAttribute("statusb4") %> </select>

</td></tr><tr><td colspan='4'>提交时间：<input type=text   name='startDate'  id='startDate' value='' class='easyui-datebox' editable='false'>
<select name="startHour" id="startHour" onChange="Menu(this.form,1);">
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option>
	<option value='04'>04</option><option value='05'>05</option><option value='06'>06</option><option value='07'>07</option>
	<option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option>
	<option value='16'>16</option><option value='17'>17</option><option value='18'>18</option><option value='19'>19</option>
	<option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
</select>时<select name="startMin" id="startMin">
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
</select>分至<input type=text  name='endDate' id='endDate' class='easyui-datebox' editable='false' />
<select name="endHour" id="endHour">
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option>
	<option value='04'>04</option><option value='05'>05</option><option value='06'>06</option><option value='07'>07</option>
	<option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option>
	<option value='16'>16</option><option value='17'>17</option><option value='18'>18</option><option value='19'>19</option>
	<option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
</select>时<select name="endMin" id="endMin">
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
</select>分</td></tr><tr><td>
通  &nbsp;&nbsp;&nbsp;道：<select name='chanel' id='chanel' ><option value='' selected>全部</option> <%=request.getAttribute("channelb4") %> </select>
</td><td>　内容：<input type=text size=10 name='keywords'>
</td><td>提供商：<select name='supplier' id='supplier' ><option value='' selected>全部</option><%=request.getAttribute("spb4")%> </select>
</td><td><input type=button class="font" value='查 询'  onClick="Query()" name='buttonquery'  border='0'/>
</td></tr></table>
</td></tr></table>
<br>
<iframe name='MSG_PHONEBOOK_FRAME' id='MSG_PHONEBOOK_FRAME' width='98%' frameborder=0 scrolling=no marginheight=0 marginwidth=0
	 src='qbegin.action' onload='Javascript:SetWinHeight(this)'></iframe>
	 
</form>
</body>

<script type="text/javascript" src="iwork_js/plugs/loginmanage.js"></script>

</html>