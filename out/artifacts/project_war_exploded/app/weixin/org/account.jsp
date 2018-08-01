<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
<html>
<head><script type="text/javascript"> var Device = "H", DeviceVersion="1.0.0", DeviceTAB="TabDefault";</script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <script src="http://cdnjs.gtimg.com/cdnjs/libs/jquery/1.6.1/jquery.min.js" type="text/javascript"></script>
    
    <title>关联系统账号</title>
    <style type="text/css">
    	.btn{
    		 -webkit-appearance: none;
		    -webkit-user-select: none;
		    width: 80%;
		    margin: 0 auto;
		    text-align: center;
		    height: auto;
		    line-height: normal;
		    cursor: pointer;
		    font-weight: bold;
		    font-size: 16px;
		    color: #ffffff;
		    padding: 10px 0px;
		    background-color: #1D76C7;
		    border-radius: 6px;
		    -webkit-border-radius: 6px;
		    -moz-border-radius: 6px;
		    -o-border-radius: 6px;
		    -ms-border-radius: 6px;
		    border: 1px solid #4876b0;
    	}
    	*{
				    margin: 0 auto;
				    padding: 0px;
		}
				
				
				body
				{
				    background: 0 49px repeat-x #F3F3F3;
				}
				
				
				table
				{
				    width: 100%;
				    height: 100%;
				    border: none;
				    border-collapse: collapse;
				}
				
				
				.bg_titlebar_on
				{
				    line-height: 53px;/* */
				    font-size: large;
				    font-weight: bold;
				    color: #2E61BC;
				    height: 43px;
				}
				#Block
				{
				    overflow: hidden;
				    height: 100%;
				    text-align: center;
				}
				
				
				#InputTitle
				{
				    height: 53px;/* */
				    background:#efefef;;
				}
				#InputTitle td
				{
				    line-height: 53px;/* */
				    text-align: center;
				    vertical-align: middle;
				    font-size: 18px;
				    font-weight: bolder;
				    letter-spacing: 1px;
				}
				#InputBlock
				{
				    height: 185px;
				    -webkit-border-bottom-left-radius: 10px;
				    -webkit-border-bottom-right-radius: 10px;
				    -moz-border-radius-bottomleft: 10px;
				    -moz-border-radius-bottomright: 10px;
				    background-color: #ececec;
				}
				
				
				.InputLine
				{
				    height: 36px;/* */
				    padding-bottom:2px;/* */
				    padding-top:2px;/* */
				
				}
				.InputLine .left
				{
				    height: 30px;
				    width: 80px;
				    font-size: 16px;
				    color: #4f4f4f;
				    font-weight: bold;
				    border-bottom-style: solid;
				    border-bottom-width: 1px;
				    border-bottom-color: #CDCDCD;
				}
				.InputLine .left_noline
				{
				    height: 30px;
				    width: 80px;
				    font-size: 16px;
				    color: #4f4f4f;
				    font-weight: bold;
				}
				.InputLine .left_margin
				{
				    height: 30px;
				    width: 15px;
				    font-size: 14px;
				    color: #4f4f4f;
				    font-weight: bold;
				}
				.InputLine .word_left
				{
				    padding-left: 15px;
				    height: 30px;
				    width: 80px;
				    font-size: 16px;
				    color: #4f4f4f;
				}
				.InputLine .right
				{
				    padding-left: 15px;
				    height: 28px; /*后修改*/
				    border-bottom-style: solid;
				    border-bottom-width: 1px;
				    border-bottom-color: #CDCDCD;
				}
				.InputLine .right_noline
				{
				    padding-left: 15px;
				    height: 30px;
				}
				.InputLine .right_1
				{
				    padding-left: 15px;
				    height: 30px;
				    text-align: right;
				    border-bottom-style: solid;
				    border-bottom-width: 1px;
				    border-bottom-color: #CDCDCD;
				}
				.InputLine .right_margin
				{
				    width: 15px;
				    height: 30px;
				}
				
				.Line
				{
				    background: url('../images/line.png');
				}
				
				.line15px
				{
				    height: 15px;
				}
				.line10px
				{
				    height: 10px;
				}
				.line5px
				{
				    height: 5px;
				}
				.line20px
				{
				    height: 20px;
				}
				.input90
				{
				    border: solid 1px #acacac;
				    background-color: #fff;
				    height: 28px;/* */
				    line-height: 30px;
				    font-size: 16px;
				    color: #646464;
				    font-family: Arial;
				    margin-top: 0px;
				    width: 90%;
				    font-family: Arial;
				}
				
				.NoneInput90
				{
				    width: 90%;
				    height: 28px; /* 后修改*/
				    text-align: left;
				    border-top-style: none;
				    border-right-style: none;
				    border-left-style: none;
				    background-color: #F3F3F3;
				    border-bottom-style: none;
				    font-size: 16px;
				    color: #646464;
				    font-family: Arial;
				}
				.NoneSelect90
				{
				    width: 90%;
				    height: 30px;
				    text-align: left;
				    border-top-style: none;
				    border-right-style: none;
				    border-left-style: none;
				    background-color: #F3F3F3;
				    border-bottom-style: none;
				    font-size: 16px;
				    color: #646464;
				    font-family: Arial;
				}
				.NoneInput90_1
				{
				    width: 90%;
				    height: 30px;
				    text-align: right;
				    border-top-style: none;
				    border-right-style: none;
				    border-left-style: none;
				    background-color: #F3F3F3;
				    border-bottom-style: none;
				    font-size: 16px;
				    color: #646464;
				    font-family: Arial;
				}
				.right_1 input:focus
				{
				    text-align: left;
				}
				
				.input100px
				{
				    width: 100px;
				    font-family: Arial;
				}
				#AssureWord
				{
				    text-align:center;/* */
				    color: #B3B3B3;
				    font-size: 12px;
				    height: 18px;
				    padding-top: 5px;/* */
				    padding-left: 15px;
				    padding-bottom: 0px;/* */
				}
    </style>
    <script type="text/javascript">
    	function doBind(){
    		var weixinCode = $("#weixinCode").val();
    		var userid = $("#userid").val();
    		var password = $("#PwdA").val();
    		var extraPwdA = $("#ExtraPwdA").val();
    		
    		if(weixinCode==''){
    			alert("微信账号不允许为空");
    			return;
    		}
    		if(userid==''){
    			alert("系统账号不允许为空");
    			return;
    		}
    		if(password==''){
    			alert("密码不允许为空");
    			return;
    		}
    		if(extraPwdA==''){
    			alert("验证码不允许为空");
    			return;
    		}
    		$("#editForm").attr("action","weixin_org_dobind.action");
    		$("#editForm").submit();
    	}
    	function changeValidateCode(obj) {  
    	    var currentTime= new Date().getTime();  
    	    obj.src = "weixin_identifyCode.action?d=" + currentTime;  
    	}
    </script>
</head> 
<body >
    <form id="editForm">
    	<s:hidden id="weixinCode" name="weixinCode"></s:hidden>
    
    <a href="#" onclick="javascript:GoToApp();return false;" style="display: block; position: absolute;
        right: 40px; top: 0; width: 87.5%; height: 49px; z-index: 99;"></a>
        <div id="MicroMessengerTip" style="display: none" onclick="displayMicroMessengerTip(false);">
            <div>
            </div>
            <div>
            </div>
        </div>

    <div id="Block">
        <table id="InputTitle">
            <tr>
                <td width="34%">
                    <table>
                        <tr>
                            <td class="bg_titlebar_on" style="border-left: none; border-right: none;">
                                    系统绑定微信账号
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <div class="InputBlockBody">
            <div class="line5px">
            </div>
            
            <table class="InputLine">
                <tr>
                    <td class="left_margin">
                    </td>
                    <td class="left" align="right">系统账号</td>
                    <td class="right" align="left"><s:property value="userid"/> </td>
                    <td class="right_margin">
                    </td>
                </tr>
            </table>
            <table class="InputLine">
                <tr>
                    <td class="left_margin">
                    </td>
                    <td class="left" align="right">
                        微信账号
                    </td>
                    <td class="right" align="left">
                    	<s:property value="weixinCode"/> 
                    </td>
                    <td class="right_margin">
                    </td>
                </tr>
            </table>
            <div class="line5px">
            </div>
        </div>
        <div style="text-align: center;">
    <div class="btnDIV">
        <input type="button" class="btn" style="width:300px" id='BtnSubmit' value="解出绑定" style="" onclick="javascript:doBind();"/>
    </div>
        </div>
    </div>
    <div id="AssureWord">
        本页面由业务系统为您提供安全保障，请您放心使用。
    </div>
    </form>
</body>
</html>
