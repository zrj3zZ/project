<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
//读取Cookie
 Cookie[] cookies = request.getCookies();
 String chkedn="";
 String chkedp="";
 if(cookies!=null)
{
   for(int i=0;i<cookies.length;i++)  
    {   
  if(cookies[i].getName().equals("name")){
  request.setAttribute("name",cookies[i].getValue());
  chkedn="checked";
    } 
  if(cookies[i].getName().equals("pwd")){
  request.setAttribute("pwd",cookies[i].getValue());
  chkedp="checked";
    } 
  }
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<title>[登录]BPM综合应用平台－客户端</title>
<LINK href="iwork_css/Login.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.validate.js"   charset="utf-8"  ></script>
<script type="text/javascript" src="iwork_js/jqueryjs/jquery.metadata.js"  charset="utf-8"   ></script>
<script language="javascript">
	if (top.location != self.location)top.location=self.location;
</script>
<sysframetag:getcontentid sql="select code, orgname from ou_organize order by code " id="lstCode" name="lstOrgname"/>

<script Language="JavaScript">
var mainFormValidator;
$().ready(function() {
	mainFormValidator =  $("#login").validate({});
	mainFormValidator.resetForm();
});
function submitForm(act,cmd){
	var valid = mainFormValidator.form(); //执行校验操作
	if(!valid){
		return false;
	}
	document.LoginForm.submit();
}
function changeValidateCode(obj) {  
    var currentTime= new Date().getTime();  
    obj.src = "showidentifyCode.action?d=" + currentTime;  
}  
</script>
</head>

<body>  
<form id="login" name="login" action="dologin.action" method="POST">
<div class="BackGround">
  <div class="left_bg"></div>
  <div class="right_bg"></div>
  <div class="login_frame"></div>
 <!--  <div class="logo"></div> -->

  <div class="login_frame"></div> 
  <div class="login_box">
  
    <div class="login_box">
    <div class="input1"><input type="text" class="loginText {string:true}" name="userid"  value="${name}" style="background:transparent none!important;"/></div>
    <div class="input2"><input type="password" class="loginText {string:true}" name="password"  value="${pwd}" style="background:transparent none!important;" /></div>
    <div class="input3"><input type="text" name="identifyCode" style="width:40px;background:transparent none!important;" /></div>
    </div>
     
    <div><input  type="submit" class="btn_sumit" value=""/></div>
    <div><input  type="reset" class="btn_reset" value=""/> </div>
    
    <div class="checkbox_div">
      <input type="checkbox" id="rememberName" name="rememberName" <%=chkedn%> value="true"  class="checkbox"/>
      <label for="记住用户名">记住用户名</label>
      <input type="checkbox" id="rememberPwd" name="rememberPwd" <%=chkedp%> value="true"  class="checkbox"/>
           <label for="记住用户名">记住密码</label>
    </div>
    <div class="validate_div"><img id="identifyPic" src="showidentifyCode.action" onclick="changeValidateCode(this)" width="62" height="23" /></div>
    <div class="genghuan"><a href="javascript:changeValidateCode(document.getElementById('identifyPic'))">换一张</a></div>
    <div class="error_div"><s:property value="loginInfo"  escapeHtml="false"/></div>
 
  </div>  
  </div>  
  </form>
</body>
</html>
<script language="javascript">
	if (document.forms[0].usercode) 
		document.forms[0].usercode.focus();
	
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