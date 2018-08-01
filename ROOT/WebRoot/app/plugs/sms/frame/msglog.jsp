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
		<script type="text/javascript" src="iwork_js/calendar.js"></script>	
		</head>
<body>
<form> 
类别：<%=request.getAttribute("type-checkbox") %>
&nbsp;&nbsp;详情：<input type=text class='actionsoftInput' width=6 name='keywords' />
<table border='0'><tr><td>操作者：</td><td><input type=text class='actionsoftInput' length='10' id='sender' name='sender' value=''>
　时间：<input type=text  name='startDate' id='startDate' value='' style='width:80px;' class='easyui-datebox' editable='false' >
<select name="startHour" id="startHour" onChange="Menu(this.form,1);">
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option>
	<option value='04'>04</option><option value='05'>05</option><option value='06'>06</option><option value='07'>07</option>
	<option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option>
	<option value='16'>16</option><option value='17'>17</option><option value='18'>18</option><option value='19'>19</option>
	<option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
</select>时
<select name="startMin" id="startMin">
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
</select>分&nbsp;至&nbsp;
<input type=text  style='width:80px;' name='endDate'  id='endDate' class='easyui-datebox' editable='false' />
<select name="endHour" id="endHour">
	<option value="00" selected>00</option><option value='01'>01</option><option value='02'>02</option><option value='03'>03</option>
	<option value='04'>04</option><option value='05'>05</option><option value='06'>06</option><option value='07'>07</option>
	<option value='08'>08</option><option value='09'>09</option><option value='10'>10</option><option value='11'>11</option>
	<option value='12'>12</option><option value='13'>13</option><option value='14'>14</option><option value='15'>15</option>
	<option value='16'>16</option><option value='17'>17</option><option value='18'>18</option><option value='19'>19</option>
	<option value='20'>20</option><option value='21'>21</option><option value='22'>22</option><option value='23'>23</option>
</select>时
<select name="endMin" id="endMin">
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
</select>分</td></tr></table>
<input type=button value='查 询'   onClick="querylog()" name='buttonquery'  border='0'>
<br><br>


<!--<iframe name='MSG_PHONEBOOK_FRAME' id='MSG_PHONEBOOK_FRAME' width='700px' height=900 frameborder=0 scrolling=no marginheight=0 marginwidth=0
	 src='<#SelectUrl>'></iframe>-->
	 <iframe name='MSG_PHONEBOOK_FRAME' id='MSG_PHONEBOOK_FRAME' width='98%' frameborder=0 scrolling=no marginheight=0 marginwidth=0
	 src='' onload='Javascript:SetWinHeight(this)'></iframe>
 
  
  <input type='hidden' name='checktype' id='checktype'>
</form>
</body>
<script>
function SetWinHeight(obj)
{
	var win=obj;
	if(document.getElementById)
	{
		if(win && !window.opera)
	  {
	  	if(win.contentDocument && win.contentDocument.body.offsetHeight) 
	    {
	    	win.width = "98%";
	    	//alert('win.width='+win.width);
	    	//alert('win.contentDocument.body.offsetWidth='+win.contentDocument.body.offsetWidth);
	      win.height = win.contentDocument.body.offsetHeight + 20; 
	      //win.width = win.contentDocument.body.offsetWidth;
	    }
	    else if(win.Document && win.Document.body.scrollHeight)
	    {
	    	win.width = "98%";
	    	//alert('win.width='+win.width);
	    	//alert('win.Document.body.scrollWidth='+win.Document.body.scrollWidth);
	      win.height = win.Document.body.scrollHeight + 20;
	     // win.width = win.Document.body.scrollWidth;
	    }
	  }
	}
}
function getType() {
	var objs = document.getElementsByTagName("input"); 
	var ret = '';
	for(var i=0; i<objs.length; i++)   
	{
		if(objs[i].type=="checkbox" && objs[i].checked) 
		{ 
			ret = ret + ' ' + objs[i].value;
			
		} 
	}

	return ret;
}
function querylog(){
var startdate=document.getElementsByName("startDate");
	var enddate=document.getElementsByName("endDate");
	var starthour=document.getElementsByName("startHour");
	var endhour=document.getElementsByName("endHour");
	var startmin=document.getElementsByName("startMin");
	var endmin=document.getElementsByName("endMin");
	if(startdate[0].value!=""&&enddate[0].value==""){
		alert("请填写时间段!");
		return false;
	}
	
	if(startdate[0].value>enddate[0].value){
		alert("请输入正确的时间段!");
		return false;
	}
	if(startdate[0].value!=""&&startdate[0].value==enddate[0].value&&starthour[0].value>endhour[0].value){
		alert("请输入正确的时间段!");
		return false;
	}
	if(startdate[0].value!=""&&startdate[0].value==enddate[0].value&&starthour[0].value==endhour[0].value&&startmin[0].value>endmin[0].value){
		alert("请输入正确的时间段!");
		return false;
	}
	document.getElementsByName('checktype')[0].value = getType();
            var url='qmsglog.action'
            document.forms[0].action=url;  
            document.forms[0].method="post";  
            document.forms[0].enctype="multipart/form-data" 
            document.forms[0].target="MSG_PHONEBOOK_FRAME";
            
            document.forms[0].submit();
            return false;
}
function query(form,mycmd){
	if(form.startDate.value!="" && form.endDate.value==""){
		alert("请填写时间段！");
		return false;
	}
	if(form.startDate.value>form.endDate.value){
		alert("请输入正确的时间段");
		return false;
	}
	if(form.startDate.value!=""&&form.startDate.value==form.endDate.value&&form.startHour.value>form.endHour.value){
		alert("请输入正确的时间段");
		return false;
	}
	if(form.startDate.value!=""&&form.startDate.value==form.endDate.value&&form.startHour.value==form.endHour.value&&form.startMin.value>form.endMin.value){
		alert("请输入正确的时间段");
		return false;
	}
	form.cmd.value=mycmd;
	frmMain.target="MSG_PHONEBOOK_FRAME";
	document.getElementById('checktype').value = getType();
 	form.submit();
 	return false;
}
function gotoPage(form,mycmd,pageNow){ 
 	form.pageNow.value=pageNow;
 	form.target='_self';
 	return execMyCommand2(frmMain,mycmd,'_self');
 }
 
function execMyCommand2(form,mycmd,targetName){
	form.cmd.value=mycmd;
	form.target=targetName;

	form.submit(); 
	return false;
}
 

</script>
</html>