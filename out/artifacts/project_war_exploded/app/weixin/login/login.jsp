<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="iwork" uri="/WEB-INF/sys-commonsTags.tld" %>
<html>
<head><script type="text/javascript"> var Device = "H", DeviceVersion="1.0.0", DeviceTAB="TabDefault";</script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>关联系统账号</title>
    <script src="https://mobile.cmbchina.com/MobileHtml/Base/doc/scripts/Transaction.js" type="text/javascript"></script>
    <link href="https://mobile.cmbchina.com/MobileHtml/Base/doc/styles/LoginV2.css" type="text/css" rel="Stylesheet" />
    <link href="https://mobile.cmbchina.com/MobileHtml/Base/doc/styles/LoginBtn.css" type="text/css" rel="Stylesheet" />
    <script type="text/javascript">
        var submitControl = new SubmitControl("https://mobile.cmbchina.com/MobileHtml/ThirdParty/IMSP/DC_BindIM.aspx", "92986EB2307601690576437414CA6365647145474595394900114412");
        function DoLogin() {
            if (BSGetElement("AccountNoA").value == "") {
                alert("卡号不能为空,请重新输入!"); return;
            }
            if (BSGetElement("PwdA").value == "") {
                alert("查询密码不能为空,请重新输入!"); return;
            }
            if (BSGetElement("ExtraPwdA").value == "") {
                alert("附加码不能为空,请重新输入!"); return;
            }
            if (BSGetElement("AccountNoA").value.length == 8) {
                alert("请输入8位卡号前面的4位地区码，地区码请参看您的一卡通卡片!"); return;
            }
         
            var vRecommendationNo = BSGetElementValue("RecommendationNo");
            if(!CheckRecommendationNo(vRecommendationNo))
            {
                 alert("活动邀请码只能为数字,请重新输入!"); return;
            }
            submitControl.clearFields();
            submitControl.setCommand("CMD_DOLOGIN");
            if (typeof (navigator.userAgent) != "undefined") {
                submitControl.addFieldByNameValue("UserAgent", navigator.userAgent);
            }
            if (typeof (screen.width) != "undefined") {
                submitControl.addFieldByNameValue("screenW", screen.width + "");
            }
            if (typeof (screen.height) != "undefined") {
                submitControl.addFieldByNameValue("screenH", screen.height + "");
            }
            if (typeof (navigator.platform) != "undefined") {
                submitControl.addFieldByNameValue("OS", navigator.platform);
            }
            submitControl.addFieldByNameValue("AccountNoA", BSGetElementValue("AccountNoA"));
            submitControl.addFieldByElementId("PwdA", "ExtraPwdA", "RecommendationNo");
            submitControl.submit();
        }
        function ErrCallBack() {
            var errMsg;
            
            if (typeof errMsg != "undefined") 
                alert(errMsg);            
        }
        function CloseAppBanner()
        {
            BSGetElement("AppBanner").style.display = "none";
        }
        function GoToApp()
        {
            var appUrl = "http://m.cmbchina.com/";
            var sUserAgent = navigator.userAgent.toLowerCase();
            if(sUserAgent.indexOf("micromessenger")!=-1){
               displayMicroMessengerTip(true);
               return;
            }
            if(sUserAgent.match(/iphone os/i) == "iphone os")
            {
               appUrl = "http://itunes.apple.com/cn/app/id392899425?mt=8";
            }
            else if(sUserAgent.match(/android/i) == "android")
            {
                appUrl = "http://szdl.cmbchina.com/download/PB/CMBMobileBank.apk";
            }
            else if(sUserAgent.match(/MIUI/i) == "MIUI")
            {
                 appUrl = "http://app.mi.com/detail/778";
            }
            window.location = appUrl;
        }
        function displayMicroMessengerTip(isShow){
            if(navigator.userAgent.toLowerCase().match(/iphone os/i) == "iphone os"){
                var obj=document.getElementById("MicroMessengerTip").getElementsByTagName("div")[1];
                if(obj.className.indexOf(" MicroMessengerTipIos")==-1)
                obj.className += " MicroMessengerTipIos";
            }
            var display = isShow ? "" : "none";
            document.getElementById("MicroMessengerTip").style.display = display;
            document.getElementById("shadow").style.display = display;
        }
        function CheckRecommendationNo(RecommendationNo)
        {
            if(RecommendationNo.length > 0){
		        return /(^\d+$)/.test(RecommendationNo);
	            }
            return true;
        }
    </script>
</head>
<body onload="javascript:ErrCallBack();">
    <form id="_form">
    <input id="ClientNo" name="ClientNo" type="hidden" /><input id="Command" name="Command"
        type="hidden" /><input id="XmlReq" name="XmlReq" type="hidden" /></form>
    <a href="#" onclick="javascript:GoToApp();return false;" style="display: block; position: absolute;
        right: 40px; top: 0; width: 87.5%; height: 49px; z-index: 99;"></a>

        <div id="MicroMessengerTip" style="display: none" onclick="displayMicroMessengerTip(false);">
            <div>
            </div>
            <div>
            </div>
        </div>
    <div id="LogoBg_Topbar" >
        <div style ="background-position:center; background-size:contain; 
                    -moz-background-size:contain; -webkit-background-size:contain; 
                    -o-background-size:contain;">
        </div>
    </div>
    <div id="Block">
        <table id="InputTitle">
            <tr>
                <td width="34%">
                    <table>
                        <tr>
                            <td class="bg_titlebar_on" style="border-left: none; border-right: none;">
                                    一卡通信息提交
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
                    <td class="left" align="right">
                        <label id="IMInfoLabel">微信用户</label>
                    </td>
                    <td class="right" align="left">
                        <label id="IMInfo" class="word_left">微信测试</label>
                    </td>
                    <td class="right_margin">
                    </td>
                </tr>
            </table>
            <table class="InputLine">
                <tr>
                    <td class="left_margin">
                    </td>
                    <td class="left" align="right">
                        卡&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号
                    </td>
                    <td class="right" align="left">
                        <input name="AccountNoA" type="text" id="AccountNoA" class="NoneInput90" maxlength="16" pattern="[0-9]*" />
                    </td>
                    <td class="right_margin">
                    </td>
                </tr>
            </table>
            <table class="InputLine">
                <tr>
                    <td class="left_margin">
                    </td>
                    <td class="left" align="right">
                        查询密码
                    </td>
                    <td class="right" align="left">
                        <input type="password" id="PwdA" maxlength="6" class="NoneInput90" pattern="[0-9]*" />
                    </td>
                    <td class="right_margin">
                    </td>
                </tr>
            </table>
            <table class="InputLine">
                <tr>
                    <td class="left_margin">
                    </td>
                    <td class="left" align="right">
                        附&nbsp;&nbsp;加&nbsp;&nbsp;码
                    </td>
                    <td class="right" style="width: 100px;" align="left">
                        <input type="text" id="ExtraPwdA" maxlength="4" class="NoneInput90" pattern="[0-9]*" />
                    </td>
                    <td class="right_1" style="padding-top: 2px;">
                        <input type="image" src="https://mobile.cmbchina.com/MobileHtml/Login/ExtraPwd.aspx?ClientNo=92986EB2307601690576437414CA6365647145474595394900114412"
                            alt="" style="text-align: right;" onclick="javascript:this.src='https://mobile.cmbchina.com/MobileHtml/Login/ExtraPwd.aspx?ClientNo=92986EB2307601690576437414CA6365647145474595394900114412&random='+Math.random();" />
                    </td>
                    <td class="right_margin">
                    </td>
                </tr>
            </table>
            <table class="InputLine">
                <tr>
                    <td class="left_margin">
                    </td>
                    <td class="left" align="right">
                        活动邀请码
                    </td>
                    <td class="right" align="left">
                        <input name="RecommendationNo" type="text" id="RecommendationNo" class="NoneInput90"
                            maxlength="30" pattern="[0-9]*" placeholder="（选填）" />
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
        <input type="button" class="btn " id='BtnSubmit' value="提交" style="" onclick="javascript:DoLogin();"/>
    </div>



        </div>
    </div>
    <div id="AssureWord">
        本页面由招商银行为您提供安全保障，请您放心使用。
    </div>
    <div id="AppBanner" style="position: relative;">
        <table>
            <tr>
                <td style="width: 45px;">
                    <img src="https://mobile.cmbchina.com/MobileHtml/Base/doc/images/banner_icon.png" />
                </td>
                <td style="width: 210px;">
                    <span style="font-size: 14px; color: #255085;">手机转账</span><span style="color: Red;
                        font-size: 15px;">0</span><span style="font-size: 14px; color: #255085;">费用，享</span><span
                            style="color: Red; font-size: 15px;">3</span><span style="font-size: 14px; color: #255085;">年！</span><br />
                    <span style="font-size: 10px; color: #666666;">每日额度</span><span style="font-size: 14px;
                        color: #666666;">20</span><span style="font-size: 10px; color: #666666;">万，更多功能下载客户端</span>
                </td>
                <td style="width: 80px;">
                    <a href="#" onclick="javascript:GoToApp();return false;">下 载</a>
                </td>
                <td style="width: 20px" align="right">
                    <img src="https://mobile.cmbchina.com/MobileHtml/Base/doc/images/close.png" onclick="javascript:CloseAppBanner()" />
                </td>
            </tr>
        </table>

    </div>
</body>
</html>
<script type="text/javascript"></script>