<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.iwork.app.conf.SystemConfig"%>
<%@ page language="java" import="com.ibpmsoft.project.zqb.util.ConfigUtil"%>
<%@ page language="java" import="com.iwork.commons.util.RSAUtils"%>
<%@ page language="java" import="java.security.interfaces.RSAPublicKey"%>
<%@ page language="java" import="java.security.interfaces.RSAPrivateKey"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String isRSA = SystemConfig._iworkServerConf.getIsRSA();
if(isRSA!=null&&isRSA.equals("on")){
	HashMap<String, Object> map = RSAUtils.getKeys();
	RSAPublicKey publicKey = (RSAPublicKey) map.get("RSAPublicKey");    
	RSAPrivateKey privateKey = (RSAPrivateKey) map.get("RSAPrivateKey");
	if(session!=null){
		session.setAttribute("RSAPrivateKey", privateKey);
		String publicKeyExponent = publicKey.getPublicExponent().toString(16);  
		String publicKeyModulus = publicKey.getModulus().toString(16);  
		request.setAttribute("publicKeyExponent", publicKeyExponent);  
		request.setAttribute("publicKeyModulus", publicKeyModulus);  
	}
}
//读取Cookie
 Cookie[] cookies = request.getCookies();
 String userid = request.getParameter("userid");
 if(userid!=null){
	 userid=userid.toUpperCase();
 }
 String password = request.getParameter("txtPWD");
 String rememberPwd = request.getParameter("rememberPwd");
 String loginVerify = SystemConfig._iworkServerConf.getLoginVerify();
 String isMultiLanguage = SystemConfig._iworkServerConf.getIsMultiLanguage();
 String chkedn="";
 String chkedp="";
 boolean isCheckPWD = false;
 boolean isCheckUser = false;
 if(cookies!=null){
   for(int i=0;i<cookies.length;i++){
	  if(cookies[i].getName().equals("name")){
		if(userid==null){
			request.setAttribute("name",cookies[i].getValue());
		}else{
			request.setAttribute("name",userid);
		}
		chkedn="checked";
		continue;
		
	  } 
	if(cookies[i].getName().equals("pwd")){
		if(userid==null){
			request.setAttribute("pwd",cookies[i].getValue());
		}else if(cookies[i].getValue()!=null&&!cookies[i].getValue().toString().equals(userid)){
			request.setAttribute("pwd","");
		}else{
			request.setAttribute("pwd",password);
		}
		chkedp="checked";
		continue;
	  }   
	  if(cookies[i].getName().equals("checkPwd")){
		  if(rememberPwd!=null){
			  isCheckPWD = true;
		  }else{
			  if(cookies[i].getValue().equals("true")){
				  isCheckPWD = true;
			  }else{
				  isCheckPWD = false;
			  }
		  }
		  continue;
	  } 
	  if(cookies[i].getName().equals("checkUser")){ 
		  if(cookies[i].getValue().equals("true")){
			  isCheckUser = true;
		  }else{
			  isCheckUser = false;
		  } 
		  continue;
	  }
	}
}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<title><s:text name="system.name"/></title>
<link rel="stylesheet" type="text/css" href="iwork_css/login/themes/default/login.css">
<link rel="stylesheet" type="text/css" media="screen" href="iwork_css/jquerycss/validate/screen.css" />
<script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.7.min.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.timers-1.1.2.js"></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/languages/messages_cn.js"  ></script>
<script src="iwork_js/tools/security.js" type="text/javascript"></script>
<script type="text/javascript">
//----- js 全局变量定义
var $last_check_num = '';			//最后一次校验是否为北京移动用户的手机号
</script>
    <script language="javascript" type="text/javascript">
    var mainFormValidator;
    $().ready(function() {
 	window.alert = function(str) {
    		return ;
    }
    mainFormValidator =  $("#loginForm").validate({
     });
     mainFormValidator.resetForm();
    });
    $(document).ready(function() {
    	$("#identifyPic").attr("src","showidentifyCode.action?uuid="+Math.random());
    	if (top.location != self.location)top.location=self.location;
    	if($("#userid").val()!=""){
    		$("#userid").prev('span').hide();
   		 }
    	if($("#txtPWD").val()!=""){
    		$("#txtPWD").prev('span').hide();
   		 }
    	seeMode();
    	if($("#isShowYZMorNot").val() == "0"){
    		$("#schmali").hide();
    		$("#getschma").hide();
    	}
    })
   var logincount = 5;
    	function dologin(){
    		
			var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}
    		var mode="";
	    	var radio=document.getElementsByName("loginMode");
			for(var i=0;i<radio.length;i++){
				if(radio[i].checked==true){
					mode=radio[i].value;
				}
			}
			if(mode=='1'){
	    		if($("#userid").val()!=""&&$("#txtPWD").val()!=""&&$("#identifyCode").val()!=""&&$("#identifyCode").val()!="验证码"){
	    			<% if(isRSA!=null&&isRSA.equals("on")){ %>
			    			var pwd = $("#txtPWD").val();
							RSAUtils.setMaxDigits(130);
							var key = new RSAUtils.getKeyPair("${publicKeyExponent}", "", "${publicKeyModulus}");  
							var encrypedPwd = RSAUtils.encryptedString(key,pwd.split("").reverse().join(""));
							$("#txtPWD").val(encrypedPwd);
							
							var uid = $("#userid").val();
                    uid=encodeURI(uid);
							RSAUtils.setMaxDigits(130);
							var keys = new RSAUtils.getKeyPair("${publicKeyExponent}", "", "${publicKeyModulus}");  
							var encrypedPwds = RSAUtils.encryptedString(keys,uid.split("").reverse().join(""));
							$("#userid").val(encrypedPwds);
	    			<%}%>
	    			$("#loginForm").attr("action","dologin.action");
	    			$("#loginForm").submit();
	    			$("#loginclick").removeAttr("onclick");
	    			loginCountDown = setInterval("loginCount()",1000);
	    		}else{
	    			if($("#userid").val()==""){
	    				$("#userid").focus(); 
	    				$("#ListMsg").html("请填写登录账号");
	    			}else if($("#txtPWD").val()==""){
	    				$("#txtPWD").focus(); 
	    				$("#ListMsg").html("请填写登录密码");
	    			}
	    		<%if(loginVerify!=null&&loginVerify.equals("on")){ %> 
	    			else if($("#identifyCode").val()==""||$("#identifyCode").val()=="验证码"){
	    				$("#identifyCode").focus(); 
	    				$("#ListMsg").html("请填写验证码");
	    			}
	    		 <%} %>
	    			return;
	    			
	    		}
			}else{
				if($("#phone").val()!=""&&$("#smsNum").val()!=""){
					if(isYzmNum()){
						<% if(isRSA!=null&&isRSA.equals("on")){ %>
								var pwd = $("#txtPWD").val();
								RSAUtils.setMaxDigits(130);
								var key = new RSAUtils.getKeyPair("${publicKeyExponent}", "", "${publicKeyModulus}");  
								var encrypedPwd = RSAUtils.encryptedString(key,encodeURIComponent(pwd.split("").reverse().join("")));
								$("#txtPWD").val(encrypedPwd);
								
								var uid = $("#userid").val();
								RSAUtils.setMaxDigits(130);
								var keys = new RSAUtils.getKeyPair("${publicKeyExponent}", "", "${publicKeyModulus}");  
								var encrypedPwds = RSAUtils.encryptedString(keys,uid.split("").reverse().join(""));
								$("#userid").val(encrypedPwds);
	    				<%}%>
		    			$("#loginForm").attr("action","dologin.action");
		    			$("#loginForm").submit();
					}else{
						return ;
					}
	    		}else{
	    			if($("#phone").val()==""){
	    				$("#phone").focus(); 
	    				$("#ListMsgYzm").html("请填写手机号码");
	    			}else if($("#smsNum").val()==""){
	    				$("#smsNum").focus(); 
	    				$("#ListMsgYzm").html("请填写验证码");
	    			}
	    		}
			}
    	}
    	
    	function loginCount(){
    		if(logincount > 0){
    			$("#loginclick").html(logincount+"秒后重试");
    			logincount--;
    		}else{
    			$("#loginclick").html("登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录");
    			$("#loginclick").attr("onclick","dologin();");
    			logincount = 5;
    			clearInterval(loginCountDown);
    		}
    	}
    	function changeValidateCode(obj) {  
    	    var currentTime= new Date().getTime();  
    	    obj.src = "showidentifyCode.action?d=" + currentTime;  
    	}  
    	
    	function showMS($xt){
    		$("#numLink").hide();
    		$('#info1').html("一分钟可获取一次短信密码"); 
    		$('#info1').show();
    		var $t = 60;
    		var $tx = 61;
    		if($xt!=0){
    			$t = $t - $xt;
    			$tx = $t + 1;
    		}
    		$('#numLink').everyTime('1s', function() {
    			if($t==0){
    				$("#numLink").show();
    				$('#info1').html("一分钟可获取一次短信密码");
    			}else{
    				$('#info1').html($t+"秒后可再次获取");
    			}
    			$t--;
    		},$tx);
    	}
    	function showMessage($message,$errorCode){
    		messageHide();
    		clearBorder();
    		$("#"+$errorCode).css("border-color","red");
    		$("#errorinfo"+$errorCode).show();
    		$("#errorinfo"+$errorCode).html($message);
    	}
    	
    	function clearBorder(){
    		$('#phone').css("border-color","#ccc");
    		$('#smsNum').css("border-color","#ccc");
    	}
    	
    	function messageHide(){
    		$('#errorinfophone').hide();
    		$('#errorinfosmsNum').hide();
    	}
    	
    	function checkSuc($obj){
    		$('#errorinfo'+$obj).hide();
    		$('#'+$obj).css("border-color","#ccc");
    	}
    	
    	$(function (){
    		$("#net1").click(function(){
    			seeBQ1();
    		});
    		
    		//选择 随机短信
    		$("#net2").click(function(){
    			seeBQ2();
    		});
    		
    		$("#phone").keyup(function(){
    			var $mobile = $(this).val();
    			//登录方式										//随机短信 或 服务密码 登录
    			if(!isNum($mobile)){
    				showMessage("温馨提示：请正确输入11位手机号码!","phone");
    			}
    		});
    		
    		$("#phone").focus(function(){
    			var $mobile = $(this).val();
    			//登录方式
    				if($mobile!=$last_check_num){						//当前输入的手机号未进行归属地校验
    					//判断手机号是否为北京移动手机号（true 进行异步校验）
    					checkMBPhone($mobile);
    				}
    			$(this).css("color","#000000");
    		});
    		$("#smsNum").focus(function(){
    			var $phone = $("#phone").val();
    			if(!checkMobileNo($phone)){
    				$("#phone").val("");
    				$("#phone").focus();
    				showMessage("温馨提示：请正确输入11位手机号码!","phone");
    			}
    			$(this).css("color","#000000");
    		});
    	});
    	$(function(){
    	      var jsUserName = $("#userid").val().toLowerCase();
    	      if(jsUserName!=""&&jsUserName!=null){
	    	      if($.browser.msie)        // IE浏览器
	    	      {
	    	      	$("#userid").get(0).onpropertychange = setJsUserId;
	    	      }
	    	      else        // 其他浏览器
	    	      {
	    	    	  	var intervalName;        // 定时器句柄
	    	              $("#userid").get(0).addEventListener("input",setJsUserId,false);
	    	              // 获得焦点时，启动定时器
	    	              $("#userid").focus(function(){
	    	                      intervalName = setInterval(handle,1000);
	    	              });
	
	    	              // 失去焦点时，清除定时器
	    	              $("#userid").blur(function()
	    	              {
	    	                      clearInterval(intervalName);
	    	              });
	    	      }
    	      }
    	      // 设置jsUserName input的值
    	      function setJsUserId()
    	      {
    	    	  var value=$("#userid").val().toLowerCase();
    	    	  if(jsUserName!=value){
    	    		  jsUserName = $("#userid").val().toLowerCase();
    	              $("#txtPWD").val("");
    	    	  }
    	      }
    	      // jsUserName input的值改变时执行的函数
    	      function handle()
    	      {     
    	              // IE浏览器此处判断没什么意义，但为了统一，且提取公共代码而这样处理。
    	              if($("#userid").val().toLowerCase() != jsUserName)
    	              {
    	            	  $("#txtPWD").val("");
    	              }
    	      }
    	});
    	function getLoginMode(){
    		 return $('input:radio[name=loginMode]:checked').val();
    	}
    	
    	function checkMBPhone(phone){
    	    var GSMPhNo = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}/; //以134(5、6、7、8、9)或159,158,151,150开头;
    	    var num11 = /^\d{11}$/; //11位数字;
    	    if( "" != phone ){
    	      if(num11.exec(phone)){
    	        if(GSMPhNo.exec(phone)){
    	          checkSuc("phone");
    	          return true;
    	        }else{
    	          showMessage("温馨提示：请正确输入11位手机号码!","phone");
    	          return false;
    	        }
    	      }else{
    	        showMessage("温馨提示：请正确输入11位手机号码!","phone");
    	        return false;
    	      }
    	    }else{
    	      showMessage("温馨提示：请正确输入11位手机号码!","phone");
    	      return false;
    	    }
    	}
    	
    	function checkMobileNo(mobileNo){
    		  if((/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/.test(mobileNo))){
    		  	 checkSuc("phone");
    		     return  true;
    		  }else{
    		     return  false;
    		  }
    		}
    	
    	function isNum($num){
    		 var num11 = /^\d{0,11}$/; //数字;
    		 if(num11.exec($num)){
    		 	checkSuc("phone");					//清除样式信息
    		 	return true;
    		 }else{
    		 	showMessage("温馨提示：请正确输入11位手机号码!","phone");
    		  	return false;
    		 }
    	}
    	
    	function seeMode(){
    		var mode="";
        	var radio=document.getElementsByName("loginMode");
    		for(var i=0;i<radio.length;i++){
    			if(radio[i].checked==true){
    				mode=radio[i].value;
    			}
    		}
        	if(mode=='1'){
        		seeBQ1();
        	}else{
        		seeBQ2();
        	}
        	if($("#phone").val()!=""){
        		$("#phone").prev('span').hide();
       		 }
        	if($("#smsNum").val()!=""){
        		$("#smsNum").prev('span').hide();
       		 }
    	}
    	
    	function seeBQ1(){
    		$("#phone").hide();
    		$("#numLink").hide();
    		$("#smsNum").hide();
    		$("#info1").hide();
    		$("#yzmli").hide();
    		$("#sjhli").hide();
    		$("#numLinkli").hide();
    		$("#msgYzmli").hide();
    		$("#errorinfophone").hide();
    		$("#ListMsgYzm").hide();
    		
    		$("#ListMsg").show();
    		$("#userid").show();
    		$("#txtPWD").show();
    		$("#identifyCode").show();
    		$("#identifyPic").show();
    		$("#useridli").show();
    		$("#tyzmli").show();
    		$("#pwdli").show();
    		if($("#isShowYZMorNot").val() != "0"){
    			$("#schmali").show();
        		$("#getschma").show();
    		}
    	}
    	
    	function seeBQ2(){
    		$("#userid").hide();
    		$("#password").hide();
    		$("#identifyCode").hide();
    		$("#identifyPic").hide();
    		$("#useridli").hide();
    		$("#tyzmli").hide();
    		$("#pwdli").hide();
    		$("#ListMsg").hide();
    		$("#schmali").hide();
    		$("#getschma").hide();
    		
    		$("#phone").show();
    		$("#numLink").show();
    		$("#smsNum").show();
    		$("#info1").show();
    		$("#yzmli").show();
    		$("#sjhli").show();
    		$("#numLinkli").show();
    		$("#msgYzmli").show();
    		$("#ListMsgYzm").show();
    	}
    	var fscount = 60;
        function getCodesClick(){
        	var valid = mainFormValidator.form(); //执行校验操作
			if(!valid){
				return false;
			}
        	var $userid = $("#userid").val();
        	var $password = $("#txtPWD").val();
        	var $isShowYZMorNot = $("#isShowYZMorNot").val();
        	var $identifyCode = $("#identifyCode").val();
        	var $ssid=$("#ssid").val();
        	if($userid==""){
        		$("#ListMsg").html("温馨提示：请输入用户名");
        		return;
        	}
        	if($password==""){
        		$("#ListMsg").html("温馨提示：请输入密码");
        		return;
        	}
        	$password = encodeURIComponent($password);
        	<% if(isRSA!=null&&isRSA.equals("on")){ %>
				RSAUtils.setMaxDigits(130);
				var key = new RSAUtils.getKeyPair("${publicKeyExponent}", "", "${publicKeyModulus}");  
				$password = RSAUtils.encryptedString(key,$password.split("").reverse().join(""));
			<%}%>
       		$.ajax({ 
           		url:'getmobileyzm.action', 
           		async:false,
           		data:{'userid':$userid, 'password':$password, 'isShowYZMorNot':$isShowYZMorNot, 'identifyCode':$identifyCode,'ssid':$ssid}, 
           		success: function(data) {
           			try{
           				var dataJson = eval("(" + data + ")");
       			        var info = dataJson[0].INFO;
       			        var flag = dataJson[0].flag;
               			if(flag){
               				$("#ListMsg").html("温馨提示："+info);
               				if(info=="验证码发送成功"){
               					$("#userid").attr("readonly","readonly");
               					$("#txtPWD").attr("readonly","readonly");
               					$("#identifyCode").attr("readonly","readonly");
               					/* $("#identifyCodelocal").val($("#identifyCode").val()); */
               					
               					$("#identifyCode").removeAttr("onblur");
               					$("#identifyCode").removeAttr("onfocus");
               					$("#identifyCode").removeAttr("onkeydown");
               					$("#getschma").removeAttr("onclick");
               					myCountDown = setInterval("countDown()",1000);
               				}
               			}
           			}catch (e){
           				location.href="login.action";
           			} 
           		}, 
           		error: function(err) { 
           			$("#ListMsg").html("温馨提示："+err);
           		}
           	});
        }
    	function countDown(){
    		if(fscount > 0){
    			fscount--;
        		$("#getschmahref").text("稍等 "+ fscount +" 秒");
    		}else{
    			$("#getschma").attr("onclick","getCodesClick();");
    			fscount = 60;
    			clearInterval(myCountDown);
    			/* $("#identifyPic").click(); */
    			$("#userid").removeAttr("readonly");/* $("#useridlocal").val(""); */
    			$("#txtPWD").removeAttr("readonly");/* $("#passwordlocal").val(""); */
    			$("#identifyCode").removeAttr("readonly");/* $("#identifyCodelocal").val(""); */
    			
    			$("#identifyCode").attr("onblur","if(!value){$(this).val('验证码')}");
    			$("#identifyCode").attr("onfocus","$(this).val('');$('#ListMsg').html('');");
    			$("#getschmahref").text("获取验证码");
    		}
    	}
    	function getYAM(){
    		var $phone = $("#phone").val();
    		if (checkMBPhone($phone)){
    			$.ajax({
    				  url:'getPhoneYZ.action',
    				  type:"POST",
    				  data:"phone="+$phone,
    				  //contentType:"application/json; charset=utf-8",
    				  success: function($data){
    			         if($data=='success'){	//短信发送成功
    			         	showMessage("","smsNum");
    			         	showMS(0);
    			         }
    				  },
    				  error: function (msg) {
    				  	 showMessage(msg.name,"phone");
    		      	  }
    			 });
    		 } 
		}
    	
    	function isYzmNum(){
    		var yzmnum=$("#smsNum").val();
    	    var num11 = /^\d{6}$/; //11位数字;
    	    if( "" != yzmnum ){
    	      if(num11.exec(yzmnum)){
    	          return true;
    	      }else{
 				$("#ListMsgYzm").html("验证码格式不正确！");
    	        return false;
    	      }
    	    }
    	}
    </script>
</head>

<body onkeydown="if(event.keyCode==13) dologin();" style="padding-top:10px;" >
<form method="post" name="loginForm" id="loginForm">
<div class="logo wap"></div>
<div class="lo_ct">
    <div class="lo_bg wap" id="login1">
        <ul class="lo_list">
        <div style="margin-top:5px;margin-left: 25px;" class="logtype">
			<span style="display: none"><input type="radio" checked="checked" class="us_name {number:true}" name="loginMode" id="net1" value="1"></input>&nbsp;帐号登录</span>
			<span style="display: none"><input type="radio" class="us_name {number:true}" name="loginMode" id="net2" value="2"></input>&nbsp;手机登录</span>
		</div>
            <li class="lo_list_text" id="useridli">
            	<span style="display: block;"><s:text name="login.username.tip"/></span>
            	<input type="text" class="us_name {string:true}" name="userid" maxlength="256" id="userid" onfocus="$(this).prev('span').hide();$('#ListMsg').html('');" onblur="if(!value){$(this).prev('span').show()}" value=""></input>
            </li>
            <li class="lo_list_text" id="sjhli">
            	<span style="display: block;">手机号码</span>
            	<input type="text" onfocus="$(this).prev('span').hide();" 
            		onblur="if(!value){$(this).prev('span').show()}"
            		class="us_name {maxlength:14,number:true}" name="phone" id="phone" style="border-color: rgb(204, 204, 204); color: rgb(0, 0, 0);" value=""></input>
            </li>
            	<div style="display: none;margin-left: 30px;color: red;" class="prompt_mistake redtip" id="errorinfophone">温馨提示：请正确输入11位手机号码!</div>
            <li id="numLinkli">
            <span style="float:left;">
				<a style="color: rgb(4, 121, 207); text-decoration: underline; display: inline;" id="numLink" onclick="getYAM();" title="获取随机密码" href="#">获取随机密码</a>
			</span>
			<li class="lo_list_text" id="yzmli">
				<span style="display: block;">随机密码</span>
				<input type="text" name="smsNum" id="smsNum" class="schma {maxlength:6,string:true}" 
				onfocus="$(this).prev('span').hide()" onblur="if(!value){$(this).prev('span').show()}" 
				style="border-color: rgb(204, 204, 204); color: rgb(0, 0, 0);"></input>
            </li>
            	<!-- <div id="numLink" class="getschma" onclick="getYAM();">&nbsp;&nbsp;&nbsp;&nbsp;获取随机密码</div> -->
	            <div style="display: none;" class="prompt_mistake redtip" id="errorinfosmsNum">随机密码已经发送，请注意查收！</div>
	            <div id="info1" style="line-height: 20px; margin-top: -10px; margin-left: 5px; color: rgb(153, 153, 153); text-align: center;">一分钟可获取一次短信密码</div>
          	<li id="msgYzmli">
          		<span class="errotip" id="ListMsgYzm"><s:property value="loginInfo2"/></span>
          	</li>
			
            <li class="lo_list_text" id="pwdli">
            	<span style="display: block;"><s:text name="login.password.tip"/></span>
            	<input type="password" class="us_pwd {string:true}"  name="txtPWD" id="txtPWD" autocomplete="off"  maxlength="256"
            	onfocus="$(this).prev('span').hide();$('#ListMsg').html('');" onblur="if(!value){$(this).prev('span').show()}" value="${pwd}" ></input>
           </li>
            <%if(loginVerify!=null&&loginVerify.equals("on")){ %> 
            <li class="spot_item" id="tyzmli" style="width: 150px;">
            	<table  border="0" cellspacing="0" cellpadding="0"><tr>
            	<td> <input style="color:#999;width:60px;" type="text" class="us_spot {string:true}" name="identifyCode" id="identifyCode"  onfocus="$(this).val('');$('#ListMsg').html('');" onblur="if(!value){$(this).val('验证码')}" value="验证码" ></input>
            	</td>
            	<td><img id="identifyPic"  alt="看不清楚？点击换一张"   onclick="changeValidateCode(this)" class="identifier"  />
            	<!-- src="showidentifyCode.action" img标签里面的 -->
            	</td>
            	</tr></table>
             </li>
             <%} %>
           <!-- 验证码 -->
           <li class="spot_item" id="schmali">
            	<table  border="0" cellspacing="0" cellpadding="0"><tr>
            	<td>
            		<input type="text" class="schma" name="schma" id="schma" onfocus="$(this).val('')" onblur="if(!value){$(this).val('验证码')}" value="验证码"></input>
            	</td>
            	<td id="getschma" onclick="getCodesClick();">
            			<a id="getschmahref" class="getschma" style="text-decoration:none;color:#fff">获取验证码</a>
            	</td>
            	</tr></table>
             </li>
              <%if(isMultiLanguage!=null&&isMultiLanguage.equals("on")){ %> 
	            <li> 
	                <span class="lt" >
	                	<s:select name="locale"  listKey="key" listValue="value"  list="#{'zh_CN':'中文','en_US':'English'}"></s:select>
	                </span>
	            </li>
           		<%} %>
            <li>
                <span class="lt" onmouseover="$('em').show()" onmouseout="$('em').hide()">
                <%-- Cookie携带明文账号密码信息  <%
                	if(isCheckPWD){
                		%>
                <input type="checkbox" name="rememberPwd" class="IsSave {string:true}" id="rememberPwd" title="" checked="checked" value="true"></input><label for="rememberPwd" title="">记住我</label>
                		<% 
                	}else{
                		%>
                <input type="checkbox" name="rememberPwd" class="IsSave {string:true}" id="rememberPwd" title="" value="true"></input><label for="rememberPwd" title="">记住我</label>
                <%
                	}
                %> --%>
                </span>
                <em class="remberpass">&nbsp;请勿在公共电脑使用此选项</em>
                <!-- <a class="forget rt" href="##" title="忘记密码">忘记密码</a> -->
                <span class="errotip" id="ListMsg"><s:property value="loginInfo"/></span>
            </li>
            <li>
                <a id="loginclick" class="lo_bnt lt" href="###" onclick="dologin()" title="登录">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</a>
            </li>
            <%
			//获取isShowYZM
			String isShowYZM = ConfigUtil.readAllProperties("/common.properties").get("isShowYZM");
			if("0".equals(isShowYZM)){
				out.print("<input type=\"hidden\" name=\"isShowYZMorNot\" id=\"isShowYZMorNot\" class=\"us_spot {maxlength:6,string:true}\" value=\""+isShowYZM+"\"></input>");
			}else{
				out.print("<input type=\"hidden\" name=\"isShowYZMorNot\" id=\"isShowYZMorNot\" class=\"us_spot {maxlength:1,string:true}\" value=\"1\"></input>");
			}
			%>
        </ul>
    </div>
    <s:hidden id="loginModeTest" name="loginModeTest" cssClass="{number:true}"></s:hidden>
    <script language="javascript" type="text/javascript">
		var count=$("#loginModeTest").val();
		if(count!=null&&count!=""){
			var size=count-1;
			if(count==1){
				$("input[type='radio'][name='loginMode']").get(size).checked = true;//选中第一个
			}else{
				$("input[type='radio'][name='loginMode']").get(size).checked = true;//选中第一个
			}
		}
	</script>
</div>
    <input type=hidden id="ssid" name="ssid" value='<%=request.getSession()==null?"":request.getSession().getId()%>'></input>
    </form>
</body>
</html>
<script language="JavaScript">
	jQuery.validator.addMethod("string", function(value, element) {
		var sqlstr=[" and "," exec ", " count ", " chr ", " mid ", " master ", " or ", " truncate ", " char ", " declare ", " join ","insert ", "select ", "delete ", "update ","create ","drop "]
		var patrn=/[“”`~!#$%^&*+<>?"{},;'[]（）—。[\]]/im;
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