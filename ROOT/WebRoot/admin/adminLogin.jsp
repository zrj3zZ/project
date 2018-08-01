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
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD> 
<TITLE>用户登录</TITLE> 
    <LINK href="admin/css/login.css" type=text/css rel=stylesheet>
    <META http-equiv=Content-Type content="text/html; charset=UTF-8">
    <script type="text/javascript" src="iwork_js/jqueryjs/jquery-3.0.7.min.js"></script>
    <script src="iwork_js/tools/security.js" type="text/javascript"></script>
    <script type="text/javascript">
    $(function(){  
    	  
    	 $(document).keydown(function(e) {  
             if (e.keyCode == 13) {  
            	 subAn();  
             }  
      });  
  
        });
    function subAn(){
    	<% if(isRSA!=null&&isRSA.equals("on")){ %>
		var pwd = $("#txtPWD").val();
		RSAUtils.setMaxDigits(130);
		var key = new RSAUtils.getKeyPair("${publicKeyExponent}", "", "${publicKeyModulus}");  
		var encrypedPwd = RSAUtils.encryptedString(key,pwd.split("").reverse().join(""));
		$("#txtPWD").val(encrypedPwd);
		<%}%>
    	$("#login").submit();
    }
		if (top.location != self.location)top.location=self.location;
    	function submitForm(act,cmd){
			document.LoginForm.submit(); 
			//$("#txtPWD").val();
		}
		function changeValidateCode(obj) {  
		    var currentTime= new Date().getTime();  
		    obj.src = "showidentifyCode.action?d=" + currentTime;  
		}  
		/* $(function(){
			$.ajax({
            	type : "post",
            	dataType:"json",
            	url : "getcookieuser.action",
           		success:function(data){
           		
           		$("#userid").val(data[0].userid);
           		
           		$("#txtPWD").val(data[0].txtPWD);
           		var rememberyhm=false;
           		if(data[0].rememberyhm=="true"){
           		$("#rememberyhm").attr("checked","checked");
           		}else{
           		$("#rememberyhm").attr("checked",false);
           		}
           		if(data[0].remembermm=="true"){
           		
           		$("#remembermm").attr("checked","checked");	
           		}else{
           		
           		$("#remembermm").attr("checked",false);	
           		}
           		}
          		}) 
		
		
		
		}) */
    </script>
</HEAD>

<body style="background:#f4f4f4">
<form id="login" name="login" action="adminDoLogin.action"  method="POST">
<DIV class="console_top"><div class="console_logo"></div></DIV>
<DIV class="console_bg"></DIV>
<div class="console_login_box">
<div class="text">
  <p>用户名:</p>
  <p>密码:</p>
  <p>验证码:</p>
</div>
     <%
     Cookie[] cookies = request.getCookies();
     	String userid="";
     	String txtPWD="";
     	String rememberyhm="";
     	String remembermm="";
     	if(cookies!=null){
	     	for(int i=0;i<cookies.length;i++){
	     			if(cookies[i].getName().equals("yhm")){
						userid=cookies[i].getValue();
						
						break;
					}	
			  } 	
	     		for(int i=0;i<cookies.length;i++){
	     			if(cookies[i].getName().equals("mm")){
						txtPWD=cookies[i].getValue();
						break;
					}	
			  } 	
			  for(int i=0;i<cookies.length;i++){
	     			if(cookies[i].getName().equals("checkyhm")){
						rememberyhm=cookies[i].getValue();
					
						break;
							
					}	
			  } 
			  for(int i=0;i<cookies.length;i++){
	     			if(cookies[i].getName().equals("checkmm")){
						remembermm=cookies[i].getValue();
						
						break;
				}
	     	}
     	}
     
     
      %>
 <div class="console_input1"><input name="userid"  id="userid" type="text"  class="input_style"  value="<%=userid%>"/></div>
    <div class="console_input2"><input name="txtPWD"  id="txtPWD" type="password" class="input_style" value="<%=txtPWD%>" /></div>
    <div class="console_input3"><input name="identifyCode" type="text" class="input_style" style="width:60px;"/></div>
     <div class="console_checkbox">
   		<% if(rememberyhm=="true"||"true".equals(rememberyhm)){
   		
   		if(remembermm=="true"||"true".equals(remembermm)){
   		%>
      <input type="checkbox" name="rememberyhm" id="rememberyhm"  class="checkbox" value="true" checked="checked" />
      <label for="rememberyhm">记住用户名</label>
      <input name="remembermm" type="checkbox" id="remembermm"  class="checkbox" value="true"  checked="checked"/>
      <label for="remembermm">记住密码</label>
 		<%}else{ %>
 		
 		 <input type="checkbox" name="rememberyhm" id="rememberyhm"  class="checkbox" value="true" checked="checked" />
      <label for="rememberyhm">记住用户名</label>
      <input name="remembermm" type="checkbox" id="remembermm"  class="checkbox" value="true"  />
      <label for="remembermm">记住密码</label>
 			<%} }else{%>
 			 <input type="checkbox" name="rememberyhm" id="rememberyhm"  class="checkbox" value="true"  />
      <label for="rememberyhm">记住用户名</label>
      <input name="remembermm" type="checkbox" id="remembermm"  class="checkbox" value="true"  />
      <label for="remembermm">记住密码</label>
 			
 			<%} %>
    </div>
   
    <div class="console_validate"><img id="identifyPic" style="margin-left:10px;" src="showidentifyCode.action" onclick="changeValidateCode(this)" width="62" height="23" />
                     </div>
   <div class="console_genghuan" style="padding-left:10px;"> <a href="javascript:changeValidateCode(document.getElementById('identifyPic'))">换一张</a></div>
    <div class="console_error"><s:property value="loginInfo"  escapeHtml="false"/></div>
    <div class="console_sumit"><input type="button" onclick="subAn()"  value="登录"  style=" width:90px; height:30px; background-color:#ff8400; color:#fff; font-size:14px; font-weight:bold; border:1px solid #bd4c00; cursor: pointer;"/></div>
  </div> 
</FORM>
</body>
</HTML>
