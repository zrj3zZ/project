var xmlHttp;
function createXmlHttp(){
	if(window.XMLHttpRequest){
	       xmlHttp = new XMLHttpRequest();
	}else{
	    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}
function addFile(){
   i=0;
   var attachId = document.getElementById("attachId").value;
   sendRequest("control=addFile&attachId="+attachId+"&");
}

function doTest(url){
	alert(url);
}
function doSubmit(form,url){
    var attachId = document.getElementById("attachId").value;
	var thisForm = form;
	thisForm.action = "upload-iframeRequest.action?url=" + encodeURI(url)+"&attachId="+attachId;
	thisForm.submit();
	//alert(thisForm.action);
    startStatusCheck();
}
function emptyFile() {
    var attachId = document.getElementById("attachId").value;
    sendRequest("control=emptyFile&attachId="+attachId+"&");
}
function cancleFile() {
    createXmlHttp();
    xmlHttp.onreadystatechange = function(){
    if (xmlHttp.readyState == 4) {
        document.uploadForm.submit();
        document.getElementById("uploads").innerHTML = xmlHttp.responseText;
        document.getElementById("upload").outerHTML=document.getElementById("upload").outerHTML.replace(/(value=\").+\"/i,"$1\"");
        document.getElementById("cleanbutton").disabled="";
	    document.getElementById("bester").disabled="";
	    document.getElementById("canclebutton").disabled="true";
	    alert('取消成功!');
        addFile();	    
    }    
    };
    xmlHttp.open("POST", "upload-ajaxRequest.action", true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send("control=cancleFile&timestamp=" + new Date().getTime());
}
function delFile(fileId) {
if(i==1){
alert('上传中，不允许此操作!');
return;
}
    createXmlHttp();
    xmlHttp.onreadystatechange = function(){
    if (xmlHttp.readyState == 4) {
        document.getElementById("uploads").innerHTML = xmlHttp.responseText;
        document.getElementById("upload").outerHTML=document.getElementById("upload").outerHTML.replace(/(value=\").+\"/i,"$1\"");
        document.getElementById("cleanbutton").disabled="";
	    document.getElementById("bester").disabled="";
	    addFile();
    }    
    };
    xmlHttp.open("POST", "upload-ajaxRequest.action", true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send("control=delFile&fileId="+fileId+"&timestamp=" + new Date().getTime());
}
function sendRequest(params) {
    createXmlHttp();
    xmlHttp.onreadystatechange = showUploadFile;
    xmlHttp.open("POST", "upload-ajaxRequest.action", true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send(params+"timestamp=" + new Date().getTime());
}
function showUploadFile() {
    if (xmlHttp.readyState == 4) {
        document.getElementById("uploads").innerHTML = xmlHttp.responseText;
        document.getElementById("upload").outerHTML=document.getElementById("upload").outerHTML.replace(/(value=\").+\"/i,"$1\"");
        document.getElementById("cleanbutton").disabled="";
	    document.getElementById("bester").disabled="";
   }
}
var i = 0;
function startStatusCheck(){
    createXmlHttp();
    document.getElementById("canclebutton").disabled="";
    i=1;
    xmlHttp.onreadystatechange = showUploadFilePercent;
    xmlHttp.open("POST", "upload-returnValue.action", true);
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlHttp.send("timestamp=" + new Date().getTime());
}
String.prototype.trim = function()
{
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
function showUploadFilePercent()
{
    if (xmlHttp.readyState == 4) {
           document.getElementById("percentText").innerHTML = xmlHttp.responseText;
           if(xmlHttp.responseText.trim()!=''){
	           if(document.getElementById("messageInfo").value!='null' && document.getElementById("messageInfo").value!=''){
                    document.getElementById("upload").outerHTML=document.getElementById("upload").outerHTML.replace(/(value=\").+\"/i,"$1\"");
	                document.uploadForm.submit();
	                alert(document.getElementById("messageInfo").value);
			        document.getElementById("cleanbutton").disabled="";
				    document.getElementById("bester").disabled="";
				    document.getElementById("canclebutton").disabled="true";
				    i=0;
	                return;
	           }else{
	             document.getElementById("cleanbutton").disabled="true";
	             document.getElementById("bester").disabled="true";
                 startStatusCheck();
	           }
           }else{
                 addFile();
                 document.getElementById("canclebutton").disabled="true";
           }
          }
}
